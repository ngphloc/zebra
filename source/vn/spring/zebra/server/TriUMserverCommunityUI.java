/**
 * 
 */
package vn.spring.zebra.server;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;


import vn.spring.zebra.collab.CollabClient;
import vn.spring.zebra.collab.CollabMsgEvent;
import vn.spring.zebra.collab.CollabMsgListener;
import vn.spring.zebra.collab.UserClustererPane;
import vn.spring.zebra.mining.UserClustererService;
import vn.spring.zebra.mining.UserClustererServiceChangeEvent;
import vn.spring.zebra.mining.UserClustererServiceChangeListener;
import vn.spring.zebra.mining.UserClusterer;
import vn.spring.zebra.mining.UserClustererDaemon;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMserverCommunityUI extends JFrame implements UserClustererServiceChangeListener, CollabMsgListener {
	private static final long serialVersionUID = 1L;
	
	protected UserClustererService userClustererService = null;
	protected TriUMServerCollabService collabService = null;
	
	protected TriUMServerUI serverUI = null;
	protected JTabbedPane mainTab = null;
	protected JCheckBoxMenuItem menuItem = null;
	
	public TriUMserverCommunityUI(UserClustererService userClustererService, TriUMServerCollabService collabService,
			TriUMServerUI serverUI) {
		super("User Communities & Collabarative Area");
		this.userClustererService= userClustererService;
		this.collabService = collabService;
		this.serverUI = serverUI;
		
		setSize(600, 480);
		setResizable(true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		Container contentPanel = this.getContentPane();
		contentPanel.setLayout(new BorderLayout());
		mainTab = new JTabbedPane();
		contentPanel.add(mainTab, BorderLayout.CENTER);
		
		update();
		this.userClustererService.addUserClustererServiceChangeListener(this);
		this.collabService.getCollab().addMsgListener(this);
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		menuItem.setSelected(b);
	}

	@Override
	public synchronized void dispose() {
		try {userClustererService.removeUserClustererServiceChangeListener(this);}
		catch(Exception e) {System.out.println("CommunityUI.finalize causes error: " + e.getMessage());}
		
		try {this.collabService.getCollab().removeMsgListener(this);}
		catch(Exception e) {System.out.println("CommunityUI.finalize causes error: " + e.getMessage());}

		try{removeAllClustererPanes();}
		catch(Exception e) {System.out.println("CommunityUI.finalize causes error: " + e.getMessage());}
		
		try {setVisible(false);} catch(Exception e) {}
		super.dispose();
	}
	public synchronized UserClustererPane getClustererPane(String course) {
		for(int i = 0; i < mainTab.getComponentCount(); i++) {
			if(!(mainTab.getComponent(i) instanceof UserClustererPane)) continue;
			UserClustererPane clustererPane = (UserClustererPane) (mainTab.getComponent(i));
			if(clustererPane.getCourse().equals(course))
				return clustererPane;
		}
		return null;
	}
	public synchronized UserClustererPane getCurClustererPane() {
		Component component = mainTab.getSelectedComponent();
		if(!(component instanceof UserClustererPane)) return null;
		return (UserClustererPane) component;
	}
	public synchronized void selectClustererPane(String course) {
		for(int i = 0; i < mainTab.getComponentCount(); i++) {
			if(!(mainTab.getComponent(i) instanceof UserClustererPane)) continue;
			UserClustererPane clustererPane = (UserClustererPane) (mainTab.getComponent(i));
			if(clustererPane.getCourse().equals(course)) {
				mainTab.setSelectedIndex(i);
				return;
			}
		}
	}
	
	protected synchronized int getClustererPaneCount() {return mainTab.getComponentCount();}
	
	private void update() {
		ArrayList<UserClusterer> clusterers = userClustererService.getAllClusterers();
		
		for(int i = 0; i < mainTab.getComponentCount(); i++) {
			if(!(mainTab.getComponent(i) instanceof UserClustererPane)) continue;
			UserClustererPane clustererPane = (UserClustererPane) mainTab.getComponent(i);
			int j = 0;
			for(j = 0; j < clusterers.size(); j++) {
				UserClusterer clusterer = clusterers.get(j);
				if(clustererPane.getClusterer() == clusterer) break;
			}
			if(j == clusterers.size()) { //not found
				mainTab.remove(i);
				String course = clustererPane.getCourse();
				clustererPane.dispose();
				removeClustererMsgListenerForServerUI(course);
				i--;
			}
		}
		
		for(UserClusterer clusterer : clusterers) {
			int i = 0;
			for(i = 0; i < mainTab.getComponentCount(); i++) {
				if(!(mainTab.getComponent(i) instanceof UserClustererPane)) continue;
				UserClustererPane clustererPane = (UserClustererPane) mainTab.getComponent(i);
				if(clustererPane.getClusterer() == clusterer) break;
			}
			if(i == mainTab.getComponentCount()) { //not found
				UserClustererPane clustererPane = new UserClustererPane(clusterer); 
				mainTab.add(clusterer.getCourse(), clustererPane);
			}
			addClustererMsgListenerForServerUI(clusterer.getCourse());
		}
		
	}

	private void addClustererPane(UserClusterer clusterer) {
		String course = clusterer.getCourse();
		if(getClustererPane(course) == null) {
			UserClustererPane clustererPane = new UserClustererPane(clusterer); 
			mainTab.add(course, clustererPane);
			mainTab.setSelectedComponent(clustererPane);
			addClustererMsgListenerForServerUI(course);
		}
	}
	private void removeClustererPane(String course) {
		for(int i = 0; i < mainTab.getComponentCount(); i++) {
			if(!(mainTab.getComponent(i) instanceof UserClustererPane)) continue;
			UserClustererPane clustererPane = (UserClustererPane) (mainTab.getComponent(i));
			if(clustererPane.getCourse().equals(course)) {
				mainTab.remove(i);
				clustererPane.dispose();
				removeClustererMsgListenerForServerUI(course);
				return;
			}
		}
	}
	private void removeAllClustererPanes() {
		for(int i = 0; i < mainTab.getComponentCount(); i++) {
			if(!(mainTab.getComponent(i) instanceof UserClustererPane)) continue;
			UserClustererPane clustererPane = (UserClustererPane) (mainTab.getComponent(i));
			clustererPane.dispose();
			removeClustererMsgListenerForServerUI(clustererPane.getCourse());
		}
		mainTab.removeAll();
	}
	private void addClustererMsgListenerForServerUI(String course) {
		if(serverUI != null) {
			UserClustererDaemon daemon = userClustererService.getUserClustererDaemon(course);
			if(daemon != null) daemon.addMsgListener(serverUI);
		}
		
	}
	private void removeClustererMsgListenerForServerUI(String course) {
		if(serverUI != null) {
			UserClustererDaemon daemon = userClustererService.getUserClustererDaemon(course);
			if(daemon != null) daemon.removeMsgListener(serverUI, true);
		}
	}
	public synchronized void clustererAdded(UserClustererServiceChangeEvent evt) {
		// TODO Auto-generated method stub
		addClustererPane(evt.getClusterer());
	}

	public synchronized void clustererRemoved(UserClustererServiceChangeEvent evt) {
		// TODO Auto-generated method stub
		removeClustererPane(evt.getClusterer().getCourse());
	}

	public synchronized void clustererRemovedAll(UserClustererServiceChangeEvent evt) {
		// TODO Auto-generated method stub
		removeAllClustererPanes();
	}

	public void msgSended(CollabMsgEvent evt) {
		// TODO Auto-generated method stub
		String plainMsg = evt.getPlainMsg();
		if(plainMsg == null || plainMsg.length() == 0) return;
		
		int idx = plainMsg.indexOf(CollabClient.USERID_SEP);
		if(idx == -1) return;
		String fromUserId = plainMsg.substring(0, idx);
		for(int i = 0; i < mainTab.getComponentCount(); i++) {
			if(!(mainTab.getComponent(i) instanceof UserClustererPane)) continue;
			UserClustererPane clustererPane = (UserClustererPane) (mainTab.getComponent(i));
			UserClusterer clusterer = clustererPane.getClusterer();
			if(clusterer.getClusterOf(fromUserId) != null) {
				clustererPane.msgSended(evt);
				return;
			}
		}
	}
	
	
}
