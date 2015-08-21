/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.server.TriUMServerUI;
import vn.spring.zebra.um.TriUM;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMContainerTabbedImpl extends JTabbedPane implements TriUMContainer {
	private static final long serialVersionUID = 1L;
	protected TriUMServerUI serverUI = null;

	public TriUMContainerTabbedImpl(TriUMServerUI serverUI) throws Exception {
		super();
		this.serverUI = serverUI;
		
		this.addChangeListener(serverUI);
		this.update(this.serverUI.getServer().getAllMonitoredUM());
	}

	public TriUM getUM(int idx) {
		TriUMPane umPane = _getUMPane(idx);
		return (umPane == null ? null : umPane.triUM); 
	}
	public TriUM getUM(String userid, String course) {
		TriUMPane umPane = getUMPane(userid, course);
		return (umPane == null?null:umPane.triUM); 
	}
	public TriUM getCurUM() {
		TriUMPane umPane = _getCurUMPane();
		return (umPane == null ? null : umPane.triUM);
	}
	public TriUMPane getUMPane(String userid, String course) {
		return _getUMPane(userid, course);
	}

	public void setCurUM(String userid, String course) {
		TriUMPane umPane = getUMPane(userid, course);
		if(umPane != null && getSelectedComponent() != umPane)
			setSelectedComponent(umPane);
	}
	
	public int getUMCount() {
		return getComponentCount();
	}

	public void addUM(TriUM um) throws Exception {
		if(getUMPane(um.getUserId(), um.getCourse()) != null) return;
		
		String key = ZebraStatic.makeId(um.getUserId(), um.getCourse());
		TriUMPane umPane = new TriUMPane(um, serverUI);
		
		add(key, umPane);
		//@Loc Nguyen: I comment it but I can't explain
		//setCurUM(um.getUserId(), um.getCourse());
	}
	public void update(ArrayList<TriUM> ums) throws Exception {
		//remove not exist TriUM pane
		for(int i = 0; i < getUMCount(); i++) {
			TriUMPane umPane = _getUMPane(i);
			int j = 0;
			for(j = 0; j < ums.size(); j++) {
				TriUM um = ums.get(j);
				if(umPane.getTriUM() == um) break;
			}
			if(j == ums.size()) { //not found
				umPane.dispose();
				remove(i);
				i--;
			}
		}
		
		//add TriUM pane
		for(TriUM um : ums) {
			int i = 0;
			for(i = 0; i < getUMCount(); i++) {
				TriUMPane umPane = _getUMPane(i);
				if(umPane.getTriUM() == um) break;
			}
			if(i == getUMCount()) { //not found
				addUM(um);
			}
		}
			
		//@Loc Nguyen: I comment it but I can't explain
		//if(getPaneCount() > 0) setSelectedIndex(0);
	}

	public void removeUM(String userid, String course) {
		for(int i = 0; i < getUMCount(); i++) {
			TriUMPane umPane = _getUMPane(i);
			TriUM um = umPane.getTriUM();
			if(um.getUserId().equals(userid) && um.getCourse().equals(course)) {
				umPane.dispose();
				remove(i);
				break;
			}
		}
	}
	public void removeUMs(String userid) {
		for(int i = 0; i < getUMCount(); i++) {
			TriUMPane umPane = _getUMPane(i);
			String id = umPane.getTriUM().getUserId();
			if(userid == null || userid.length() == 0 || id.startsWith(userid)) {
				umPane.dispose();
				remove(i);
				i--;
			}
		}
	}
	public void clearSelection() {
		System.out.println("TriUMContainerTabbedImpl clear selection");
	}

	public void dispose() {
		try {removeUMs(null);}
		catch(Exception e) {
			System.out.println("TriUMPaneContainerTabbedImpl.dispose->removeUMs() causes error: " + e.getMessage());
		}
		
		try {removeChangeListener(serverUI);}
		catch(Exception e) {e.printStackTrace();}
	}
    public void refreshShowedInfo() {
    	JOptionPane.showMessageDialog(null, "Not implments refreshShowedInfo function yet", "Error", JOptionPane.ERROR_MESSAGE);
    }
    public void refreshShowedInfo(String userid) {
    	JOptionPane.showMessageDialog(null, "Not implments refreshShowedInfo function yet", "Error", JOptionPane.ERROR_MESSAGE);
    }
	
	private TriUMPane _getUMPane(int idx) {
		TriUMPane umPane = (TriUMPane) getComponent(idx);
		return umPane; 
	}
	private TriUMPane _getUMPane(String userid, String course) {
		for(int i = 0; i < getUMCount(); i++) {
			TriUMPane umPane = _getUMPane(i);
			TriUM um = umPane.getTriUM();
			if(um.getUserId().equals(userid) && um.getCourse().equals(course))
				return umPane;
		}
		return null;
	}
	private TriUMPane _getCurUMPane() {
		if(getUMCount() == 0) return null;
		return (TriUMPane) getSelectedComponent();
	}
	
}
