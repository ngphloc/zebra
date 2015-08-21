/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import vn.spring.WOW.datacomponents.Profile;
import vn.spring.zebra.server.TriUMServerUI;
import vn.spring.zebra.server.TriUMServerUIHelper;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.util.sortabletable.SortableTable;
import vn.spring.zebra.util.sortabletable.SortableTableModel;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMContainerTableImpl extends JPanel implements TriUMContainer {
	private static final long serialVersionUID = 1L;
	
	protected TriUMServerUI serverUI = null;
    protected JTable table = null;
    protected HashSet<ChangeListener> changeListeners = new HashSet<ChangeListener>();
    protected long previousClick = 0; //in miliseconds

	public TriUMContainerTableImpl(TriUMServerUI serverUI) throws Exception {
		super();
		this.setLayout(new BorderLayout());
		this.serverUI = serverUI;
		this.table = createTable();
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		TitledBorder titleBorder = BorderFactory.createTitledBorder("Monitored Users (right-click or double-click on each item to see her/his TLM)");
		titleBorder.setBorder(BorderFactory.createLoweredBevelBorder());
		setBorder(titleBorder);
		
		this.addChangeListener(serverUI);
		this.update(this.serverUI.getServer().getAllMonitoredUM());
	}

	public int getUMCount() {
		return getTableModel().getRowCount();
	}

	public TriUM getCurUM() {
		int row = table.getSelectedRow();
		if(row == -1) return null;
		return (TriUM)getTableModel().getValueAt(row, 0);
	}
	public TriUM getUM(int idx) {
		return (TriUM)getTableModel().getValueAt(idx, 0);
	}
	public TriUM getUM(String userid, String course) {
		for(int i = 0; i < getUMCount(); i++) {
			TriUM um = getUM(i);
			if(um.getUserId().equals(userid) && um.getCourse().equals(course))
				return um;
		}
		return null;
	}
	public TriUMPane getUMPane(String userid, String course) {
		if(true) return null;
		
		TriUM um = getUM(userid, course);
		if(um == null) return null;
		try {
			return new TriUMPane(um, serverUI);
		}
		catch(Exception e) {e.printStackTrace(); return null;}
	}

	public void setCurUM(String userid, String course) {
		int i = 0;
		for(i = 0; i < getUMCount(); i++) {
			TriUM um = getUM(i);
			if(um.getUserId().equals(userid) && um.getCourse().equals(course))
				break;
		}
		if(i < getUMCount() && table.getSelectedRow() != i) table.setRowSelectionInterval(i, i);
	}

	public void addUM(TriUM um) throws Exception {
		if(getUM(um.getUserId(), um.getCourse()) != null) return;
		getTableModel().addRow(createTableRow(um));
		//@Loc Nguyen: I comment it but I can't explain
		//setCurUM(um.getUserId(), um.getCourse());
	}
	public void update(ArrayList<TriUM> ums) throws Exception {
		//remove not exist TriUM pane
		for(int i = 0; i < getUMCount(); i++) {
			TriUM thisum = getUM(i);
			int j = 0;
			for(j = 0; j < ums.size(); j++) {
				TriUM um = ums.get(j);
				if(thisum == um) break;
			}
			if(j == ums.size()) { //not found
				removeTableRow(i);
				i--;
			}
		}
		
		//add TriUM pane
		for(TriUM um : ums) {
			int i = 0;
			for(i = 0; i < getUMCount(); i++) {
				TriUM thisum = getUM(i);
				if(thisum == um) break;
			}
			if(i == getUMCount()) { //not found
				addUM(um);
			}
		}
		
		//@Loc Nguyen: I comment it but I can't explain
		//if(getUMCount() > 0) listView.setSelectedIndex(0);
	}

	public void removeUM(String userid, String course) {
		for(int i = 0; i < getUMCount(); i++) {
			TriUM um = getUM(i);
			if(um.getUserId().equals(userid) && um.getCourse().equals(course)) {
				removeTableRow(i);
				break;
			}
		}
	}
	
	public void removeUMs(String userid) {
		for(int i = 0; i < getUMCount(); i++) {
			TriUM um = getUM(i);
			String id = um.getUserId();
			if(userid == null || userid.length() == 0 || id.startsWith(userid)) {
				removeTableRow(i);
				i--;
			}
		}
	}
	
	public void clearSelection() {
		table.clearSelection();
	}
	public void addChangeListener(ChangeListener l) {
		changeListeners.add(l);
	}
	public void removeChangeListener(ChangeListener l) {
		changeListeners.remove(l);
	}
    public void refreshShowedInfo() {
    	for(int i = 0; i < getUMCount(); i++) {
    		TriUM um = getUM(i);
        	try {
        		Profile profile = um.refreshProfile();
        		String  username = profile.getAttributeValue("personal", "name");
        		table.getModel().setValueAt(username, i, 1);
        	}
        	catch(Exception e) {e.printStackTrace();}
    	}
    }
    public void refreshShowedInfo(String userid) {
    	for(int i = 0; i < getUMCount(); i++) {
    		TriUM um = getUM(i);
    		if(!um.getUserId().equals(userid)) continue;
        	try {
        		Profile profile = um.refreshProfile();
        		String  username = profile.getAttributeValue("personal", "name");
        		table.getModel().setValueAt(username, i, 1);
        	}
        	catch(Exception e) {e.printStackTrace();}
    	}
    }

	public void dispose() {
		try {
			removeUMs(null);
		}
		catch(Exception e) {
			System.out.println("TriUMContainerListViewImpl.dispose->removeUMs() causes error: " + e.getMessage());
		}

		try {removeChangeListener(serverUI);}
		catch(Exception e) {
			System.out.println("TriUMContainerListViewImpl.dispose->changeListeners.clear causes error: " + e.getMessage());
		}
	}
	
	private JTable createTable() {
		SortableTableModel model = new MySortableTableModel();
		final SortableTable table = new SortableTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setPreferredScrollableViewportSize(new Dimension(600, 400));
        table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(true);
		table.setToolTipText("Right-click on items for context menu. \nMaybe press keys: Enter, F5, Delete...");
        
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				System.out.println("Monitored User List (TriUMContainerTableImpl) fires selection event");
				ChangeEvent evt = new ChangeEvent(table);
				for(ChangeListener listener : changeListeners) {
					listener.stateChanged(evt);
				}
			}
		});
		table.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				TriUM um = getCurUM(); if(um == null) {previousClick = 0; return;}
				if(javax.swing.SwingUtilities.isRightMouseButton(e) ) {
					previousClick = 0;
					JPopupMenu contextMenu = TriUMPane.createContextMenu(um, serverUI);
					if(contextMenu != null) contextMenu.show((Component)e.getSource(), e.getX(), e.getY());
				}
				else {
					long now = new Date().getTime();
					if(previousClick == 0 || now - previousClick > 500) {
						previousClick = now;
					}
					else {
						previousClick = now;
						TriUMPane.showTriUMDlg(serverUI, um);
					}
				}
			}
		});
		
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				TriUM um = getCurUM();
				if(um == null) return;
				if(e.getKeyCode() == KeyEvent.VK_DELETE) {
					String userid = um.getUserId();
					String course = um.getCourse();
					TriUMServerUIHelper.createUnmonitorUserActionListener(serverUI, userid, course).
						actionPerformed(new ActionEvent(table, 0, "Unmonitor"));			
				}
				else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	    			try {
	    				new TriUMDlg(serverUI, um);
	    			}
	    			catch(Exception ex) {ex.printStackTrace();}
				}
				else if(e.getKeyCode() == KeyEvent.VK_F5) {
					refreshShowedInfo();
				}
			}
		});
		return table;
	}
	private SortableTableModel getTableModel() {
		return (SortableTableModel)table.getModel();
	}
	private Object[] createTableRow(TriUM um) {
    	String username = um.toString();
    	try {
    		username = um.getProfile().getAttributeValue("personal", "name");
    	}
    	catch(Exception e) {e.printStackTrace();}
    	return new Object[] {um, username, um.getCourse(), new Date()};
	}
    private void removeTableRow(int row) {
    	getTableModel().removeRow(row);
    }
    private class MySortableTableModel extends SortableTableModel {
    	private static final long serialVersionUID = 1L;

		public MySortableTableModel() {
			super(new String[] {"ID", "User Name", "Course", "Monitored Date"}, 0);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public boolean isSortable(int column) {
			return (column >= 0 && column < getColumnCount());
		}
    }
}

