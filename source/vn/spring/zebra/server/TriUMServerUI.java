package vn.spring.zebra.server;

import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.um.TriUMHelper;
import vn.spring.zebra.um.gui.TriUMContainer;
import vn.spring.zebra.util.CommonUtil;
import vn.spring.zebra.util.DaemonMsgEvent;
import vn.spring.zebra.util.DaemonMsgListener;
import vn.spring.bayes.InterchangeFormat.IFException;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServerUI extends JFrame implements 
		TriUMServerChangeUMListener, DaemonMsgListener, TriUMServerUserPaneSelectionListener, TriUMServerQueryListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private boolean isDisposed = false;
	
	protected TriUMServer server = null;
	protected TriUMContainer monitoredUsers = null;
	protected JTextArea msgArea = null;
	protected TriUMServerUIUserPane userPane = null;
	protected TriUMserverCommunityUI clustererUI = null;
	
	public TriUMServer getServer() {return server;}
	public TriUMServerUI(TriUMServer server) throws ZebraException, IFException, IOException, Exception {
		super();
		this.server = server;
		
		new TriUMServerUIHelper(this).initialize();
		this.server.addChangeUMListener(this);
		this.server.getCommunicateService().getQueryDelegator().addQueryListener(this);
		this.server.getGarbageService().addMsgListener(this);
		this.server.getMailingListService().addMsgListener(this);
		System.out.println("TriUMServerUI created");
	}
	
	public void umRegistered(TriUMServerChangeUMEvent evt) {
		try {
			String userid = evt.getUserId();
			userPane.addUser(userid);
			notice("(user=" + userid + ") added");
		}
		catch(Exception e) {
			System.out.println("TriUMServerUI.umRegistered causes error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void umRegisteredDefault(TriUMServerChangeUMEvent evt) {
		ArrayList<TriUM> ums = (ArrayList<TriUM>)evt.getObject();
		try {
			monitoredUsers.removeUMs(null);
			if(ums != null) monitoredUsers.update(ums);
		}
		catch(Exception e) {e.printStackTrace();}
		
		try {userPane.update();}
		catch(Exception e) {e.printStackTrace();}

		notice("Default users are registered");
	}
	public void umUnregistered(TriUMServerChangeUMEvent evt) {
		String userid = evt.getUserId();
		monitoredUsers.removeUMs(userid);
		userPane.removeUser(userid);
		notice("(user=" + userid + ") removed");
	}
	
	public void umUnregisteredAll(TriUMServerChangeUMEvent evt) {
		monitoredUsers.removeUMs(null);
		try {userPane.update();}
		catch(Exception e) {e.printStackTrace();}
		
		notice("All users unregistered (removed)");
	}
	
	public void umMonitored(TriUMServerChangeUMEvent evt) {
		TriUM um = evt.getTriUM();
		try {
			String userid = um.getUserId(), course = um.getCourse();
			monitoredUsers.addUM(um);
			userPane.updateGUI();
			notice("(user=" + userid + ", course=" + course + ") monitored"); 
			
			new TriUMHelper(um).addMsgListeners(this);
		}
		catch(Exception e) {
			System.out.println("TriUMServerUI.umMonitored causes error: " + e.getMessage());
			e.printStackTrace();
		}
	} 
	
	public void umMonitoredCourse(TriUMServerChangeUMEvent evt) {
		ArrayList<TriUM> ums = (ArrayList<TriUM>)evt.getObject();
		try {
			for(TriUM um : ums) {
				monitoredUsers.addUM(um); 
				new TriUMHelper(um).addMsgListeners(this);
			}
		}
		catch(Exception e) {e.printStackTrace();}
		
		try {
			userPane.updateGUI();
		}
		catch(Exception e) {e.printStackTrace();}
		
		notice("Course \"" + evt.getCourse() + "\" monitored");
	}
	
	public void umUnmonitored(TriUMServerChangeUMEvent evt) {
		TriUM um = evt.getTriUM();
		String userid = um.getUserId();
		String course = um.getCourse();
		monitoredUsers.removeUM(userid, course);
		userPane.updateGUI();
		notice("(user=" + userid + ", course=" + course + ") unmonitored");
	}
	
	public void umUnmonitoredCourse(TriUMServerChangeUMEvent evt) {
		ArrayList<TriUM> ums = (ArrayList<TriUM>)evt.getObject();
		
		try {
			monitoredUsers.update(ums);
		}
		catch(Exception e) {
			System.out.println("TriUMServerUI.umAdded causes error: " + e.getMessage());
			e.printStackTrace();
		}
		userPane.updateGUI();
		
		notice("Course \"" + evt.getCourse() + "\" unmonitored");
	}
	public void umUnmonitoredAll(TriUMServerChangeUMEvent evt) {
		monitoredUsers.removeUMs(null);
		userPane.updateGUI();
		notice("Unmonitor all TriUM");
	}
	
	//UsersPane selected
	public void userPaneSelectionChanged(TriUMServerUserPaneSelectionEvent e) {
		final String userid = e.getUserId();
		final String course = e.getCourse();
		if(userid == null) {
			monitoredUsers.clearSelection();
		}
		else if(course == null) {
			TriUM monitoredUM = monitoredUsers.getCurUM();
			if(monitoredUM == null) return;
			if(!monitoredUM.getUserId().equals(userid))
				monitoredUsers.clearSelection();
		}
		else {
			TriUM thisum = monitoredUsers.getUM(userid, course);
			if(thisum == null) {
				try {
					TriUM um = server.getUM(userid, course, false);
					if(um != null) {
						monitoredUsers.addUM(um);
					}
					else {
						if(JOptionPane.showConfirmDialog(null, "Do you want to monitor this user model in this course?", 
								"Monitor user model", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
							server.monitorUM(userid, course);
						}
					}
				}
				catch(Exception ex) {System.out.println(ex.getMessage()); ex.printStackTrace();}
			}
			else if(monitoredUsers.getCurUM() != thisum)
				monitoredUsers.setCurUM(thisum.getUserId(), thisum.getCourse());
		}
	}

	//Monitored User is changed
	public void stateChanged(ChangeEvent e) {
		if(monitoredUsers.getUMCount() == 0 || monitoredUsers.getUMCount() == 1) return;
		TriUM um = monitoredUsers.getCurUM();
		if(um == null) return;
		userPane.outsideSelectUser(um.getUserId(), um.getCourse());
	}
	
	public TriUMContainer getMonitoredUserList() {
		return monitoredUsers;
	}
	
	protected void changeUserName(final String userid) {
		new ChangeUserNameDlg(this, userid);
	}

	public void msgReceived(DaemonMsgEvent evt) {
		notice(evt.getMsg());
	}

	public void queryReceived(TriUMServerQueryEvent evt) {
		notice(evt.getMsg());
	}
	
	private void notice(String msg) {
		//if(!msgArea.isVisible()) return;
		msg = new Date() + ": " + msg;
		if(msgArea.getDocument().getLength() > 10000) msgArea.setText("");
		msgArea.append(msg + "\n");
	}
	

	@Override
	public void dispose() {
		try {
			for(int i = 0; i < monitoredUsers.getUMCount(); i++) {
				TriUM um = monitoredUsers.getUM(i);
				new TriUMHelper(um).removeMsgListeners(this);
			}
		}
		catch(Exception e) {System.out.println("TriUMServerUI.dispose->TriUM.removeMsgListeners() causes error: " + e.getMessage());}
		
		try {monitoredUsers.dispose();}
		catch(Exception e) {System.out.println("TriUMServerUI.dispose->monitoredUsers.dispose() causes error: " + e.getMessage());}

		try {clustererUI.setVisible(false);clustererUI.dispose();}
		catch(Exception e) {System.out.println("TriUMServerUI.dispose->clustererUI.dispose() causes error: " + e.getMessage());}

		try {userPane.dispose();}
		catch(Exception e) {System.out.println("TriUMServerUI.dispose->usersPane.dispose causes error: " + e.getMessage());}

		try {server.removeChangeUMListener(this);}
		catch(Exception e) {System.out.println("TriUMServerUI.dispose->server.removeChangeUMListener causes error: " + e.getMessage());}
		
		try {server.getCommunicateService().getQueryDelegator().removeQueryListener(this);}
		catch(Exception e) {System.out.println("TriUMServerUI.dispose->removeQueryListener causes error: " + e.getMessage());}

		try {server.getGarbageService().removeMsgListener(this, true);}
		catch(Exception e) {System.out.println("TriUMServerUI.dispose->server.removeMsgGarbageListener causes error: " + e.getMessage());}

		try {server.getMailingListService().removeMsgListener(this);}
		catch(Exception e) {System.out.println("TriUMServerUI.dispose->server.removeMailingListListener causes error: " + e.getMessage());}

		super.dispose();
		isDisposed = true;
		System.out.println("TriUMServerUI disposed");
		
		CommonUtil.gc();
	}
	public boolean isDisposed() {return isDisposed;}
}



