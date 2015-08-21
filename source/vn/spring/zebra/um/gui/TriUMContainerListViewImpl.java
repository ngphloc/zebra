/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.zfqjava.swing.JListView;
import com.zfqjava.swing.cell.DefaultCellEditor;
import com.zfqjava.swing.cell.DefaultCellRenderer;

import vn.spring.zebra.server.TriUMServerUI;
import vn.spring.zebra.um.TriUM;

public class TriUMContainerListViewImpl extends JPanel implements TriUMContainer {
	private static final long serialVersionUID = 1L;
	private static final int LIST_VIEW_MODE = JListView.LIST_VIEW_MODE;
	
	protected TriUMServerUI serverUI = null;
    protected JListView listView = null;
    protected HashSet<ChangeListener> changeListeners = new HashSet<ChangeListener>();

	public TriUMContainerListViewImpl(TriUMServerUI serverUI) throws Exception {
		super();
		this.setLayout(new BorderLayout());
		this.serverUI = serverUI;
		this.listView = createListView();
		this.add(new JScrollPane(listView), BorderLayout.CENTER);
		TitledBorder titleBorder = BorderFactory.createTitledBorder("Monitored Users (You right-click or double-click on each item to see her/his TLM)");
		titleBorder.setBorder(BorderFactory.createLoweredBevelBorder());
		setBorder(titleBorder);
		
		this.addChangeListener(serverUI);
		this.update(this.serverUI.getServer().getAllMonitoredUM());
	}

	public int getUMCount() {
		ListModel listModel = (ListModel)listView.getListModel();
		return listModel.getSize();
	}

	public TriUM getCurUM() {
		Object o = listView.getSelectedValue();
		return (o == null ? null : (TriUM)o);
	}
	public TriUM getUM(int idx) {
		ListModel listModel = (ListModel)listView.getListModel();
		return (TriUM)listModel.getElementAt(idx);
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
		if(i < getUMCount() && listView.getSelectedIndex() != i) listView.setSelectedIndex(i);
	}

	public void addUM(TriUM um) throws Exception {
		if(getUM(um.getUserId(), um.getCourse()) != null) return;
		
		DefaultTableModel tableModel = (DefaultTableModel)listView.getTableModel();
		tableModel.addRow(createListViewRow(um));
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
				removeListViewRow(i);
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
				removeListViewRow(i);
				break;
			}
		}
	}
	public void removeUMs(String userid) {
		for(int i = 0; i < getUMCount(); i++) {
			TriUM um = getUM(i);
			String id = um.getUserId();
			if(userid == null || userid.length() == 0 || id.startsWith(userid)) {
				removeListViewRow(i);
				i--;
			}
		}
	}
	public void clearSelection() {
		listView.clearSelection();
	}

	public void addChangeListener(ChangeListener l) {
		changeListeners.add(l);
	}
	public void removeChangeListener(ChangeListener l) {
		changeListeners.remove(l);
	}
    public void refreshShowedInfo() {
    	JOptionPane.showMessageDialog(null, "Not implments refreshShowedInfo function yet", "Error", JOptionPane.ERROR_MESSAGE);
    }
    public void refreshShowedInfo(String userid) {
    	JOptionPane.showMessageDialog(null, "Not implments refreshShowedInfo function yet", "Error", JOptionPane.ERROR_MESSAGE);
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

    private JListView createListView() {
    	final JListView listView = new JListView(createTableModel());
    	listView.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
				TriUM um = getCurUM(); if(um == null) return;
				TriUMPane.showTriUMDlg(serverUI, um);
    		}
    	});
    	listView.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				ChangeEvent evt = new ChangeEvent(listView);
				for(ChangeListener listener : changeListeners) {
					listener.stateChanged(evt);
				}
			}
    	});
    	listView.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(!javax.swing.SwingUtilities.isRightMouseButton(e) ) return;
				TriUM um = getCurUM(); if(um == null) return;
				
				JPopupMenu contextMenu = TriUMPane.createContextMenu(um, serverUI);
				if(contextMenu != null) contextMenu.show((Component)e.getSource(), e.getX(), e.getY());
			}
    	});
    	
    	listView.setViewMode(LIST_VIEW_MODE);
    	listView.setCellRenderer(new ListViewCellRenderer());
    	listView.setCellEditor(new ListViewCellEditor());
    	listView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	return listView;
    }
    private TableModel createTableModel() {
    	DefaultTableModel tableModel = new DefaultTableModel();
    	tableModel.addColumn("ID");
    	tableModel.addColumn("User Name");
    	tableModel.addColumn("Course");
    	return tableModel;
    }
    private void removeListViewRow(int row) {
		DefaultTableModel tableModel = (DefaultTableModel)listView.getTableModel();
		tableModel.removeRow(row);
    }
    private Object[] createListViewRow(TriUM um) {
    	String username = um.toString();
    	try {
    		username = um.getProfile().getAttributeValue("personal", "name");
    	}
    	catch(Exception e) {e.printStackTrace();}
    	return new Object[] {um, username, um.getCourse()};
    }
    private class ListViewCellRenderer extends DefaultCellRenderer {
		public ListViewCellRenderer() {
			super();
	        //URL iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "listviewuser-48x48.gif");
	        //this.iconLabel = new JLabel(new ImageIcon(iconURL));
		}
    }
    private class ListViewCellEditor extends DefaultCellEditor {
		@Override
		public boolean isCellEditable(EventObject arg0) {
			return false;
		}
    }
}
