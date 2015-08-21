/**
 * 
 */
package vn.spring.zebra.mail;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import vn.spring.zebra.util.UserChooser;
import vn.spring.zebra.util.sortabletable.SortableTable;
import vn.spring.zebra.util.sortabletable.SortableTableModel;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseMailingListManager extends JDialog {
	private static final long serialVersionUID = 1L;
	
	protected JFrame parent = null;
	protected CourseMailingList ml = null;
	
	public CourseMailingListManager(JFrame parent, CourseMailingList ml) {
		super(parent, "Course Mailing List Manager", true);
		this.ml = ml;
		setLayout(new BorderLayout());
		add(new JLabel("User Mailing List of Course \"" + this.ml.course + "\""), BorderLayout.NORTH);

		final MLTable mlTable = new MLTable(ml);
		mlTable.setPreferredScrollableViewportSize(new Dimension(600, 400));
		add(new JScrollPane(mlTable), BorderLayout.CENTER);
		
		JPanel toolbar = new JPanel();
		JButton addUser = new JButton(new AbstractAction("Add User") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				mlTable.addUser();
			}
			
		});
		toolbar.add(addUser);
		//
		JButton removeUser = new JButton(new AbstractAction("Remove User") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				mlTable.removeUser();
			}
			
		});
		toolbar.add(removeUser);
		//
		JButton removeAllUser = new JButton(new AbstractAction("Remove All User") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				mlTable.removeAllUser();
			}
			
		});
		toolbar.add(removeAllUser);
		
		JButton sendMail = new JButton(new AbstractAction("Send Mail") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				mlTable.sendMail();
			}
			
		});
		toolbar.add(sendMail);
		//
		JButton sendReport = new JButton(new AbstractAction("Send Report") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				mlTable.sendReport();
			}
			
		});
		toolbar.add(sendReport);
		
		add(toolbar, BorderLayout.SOUTH);
		
		setSize(600, 400);
		setVisible(true);
	}
	
}

class MLTable extends SortableTable {
	private static final long serialVersionUID = 1L;
	
	public MLTable(CourseMailingList ml) {
		super(new MLTableModel(ml));
	}
	
	public void addUser() {
		MLTableModel model = getMLTableModel();
		UserChooser uc = new UserChooser(null, model.getUserIdList());
		ArrayList<String> userids = uc.getChosenUserIds();
		if(userids.size() == 0) {
			JOptionPane.showMessageDialog(null, "No user chosen");
			return;
		}
		for(String userid : userids) {
			model.addUser(userid);
		}
	}
	
	public void removeUser() {
		ArrayList<String> selectedUserIdList = getSelectedUserIdList();
		if(selectedUserIdList.size() == 0) {
			JOptionPane.showMessageDialog(null, "You should select one user at least to remove");
			return;
		}
		MLTableModel model = getMLTableModel();
		for(String userid : selectedUserIdList) {
			model.removeUser(userid);
		}
	}
	
	public void removeAllUser() {
		getMLTableModel().removeAllUser();
	}
	
	public void sendMail() {
		int aws = JOptionPane.showConfirmDialog(null, 
				"Do you want to send all users.\nPress YES: Send all users.\nPress NO: Send only selected users",
				"Sending Mail or Report Confirm",
				JOptionPane.YES_NO_CANCEL_OPTION
				);
		if(aws == JOptionPane.CANCEL_OPTION) return;
		boolean all = (aws == JOptionPane.OK_OPTION);
		
		CourseMailingList ml = getMLTableModel().ml;
		if(all) {
			new MailSenderDlg(null, ml.course, new ArrayList<String>());
		}
		else {
			ArrayList<String> selectedUserIdList = getSelectedUserIdList();
			if(selectedUserIdList.size() == 0) {
				JOptionPane.showMessageDialog(null, "You should select one user at least to send mail");
				return;
			}
			new MailSenderDlg(null, ml.course, selectedUserIdList);
		}
	}
	public void sendReport() {
		int aws = JOptionPane.showConfirmDialog(null, 
				"Do you want to send all users.\nPress YES: Send all users.\nPress NO: Send only selected users",
				"Sending Mail or Report Confirm",
				JOptionPane.YES_NO_CANCEL_OPTION
				);
		if(aws == JOptionPane.CANCEL_OPTION) return;
		boolean all = (aws == JOptionPane.OK_OPTION);
		
		CourseMailingList ml = getMLTableModel().ml;
		if(all) {
			ml.sendReport(null, null);
		}
		else {
			ArrayList<String> selectedUserIdList = getSelectedUserIdList();
			if(selectedUserIdList.size() == 0) {
				JOptionPane.showMessageDialog(null, "You should select one user at least to send mail");
				return;
			}
			ml.sendReport(selectedUserIdList, null);
		}
		JOptionPane.showMessageDialog(null, "Report sent");
	}
	private ArrayList<String> getSelectedUserIdList() {
		ArrayList<String> userids = new ArrayList<String>();
		
		MLTableModel model = getMLTableModel();
		int[] selectedRows = getSelectedRows();
		if(selectedRows == null || selectedRows.length == 0) return userids;
		
		for(int row : selectedRows) {
			String userid = model.getUserIdAt(row);
			userids.add(userid);
		}
		return userids;
	}
	private MLTableModel getMLTableModel() {return (MLTableModel)getModel();}
}

class MLTableModel extends SortableTableModel {
	private static final long serialVersionUID = 1L;
	protected CourseMailingList ml = null;
	
	public MLTableModel(CourseMailingList ml) {
		super(new String[] {"User ID", "User Name", "Mail"}, 0);
		this.ml = ml;
		
		ArrayList<String> userids = ml.getUserIdList();
		for(String userid : userids) {
			Object[] row = createRow(userid);
			super.addRow(row);
		}
	}
	
	public void addUser(String userid) {
		Object[] row = createRow(userid);
		if(row == null) return;
		
		ml.addUser(userid);
		addRow(row);
	}

	public void removeUser(String userid) {
		ml.removeUser(userid);
		for(int i = 0; i < getRowCount(); i++) {
			if(getUserIdAt(i).equals(userid)) {
				removeRow(i);
				return;
			}
		}
	}
	public void removeAllUser() {
		ArrayList<String> userids = new ArrayList<String>();
		for(int i = 0; i < getRowCount(); i++) {
			userids.add(getUserIdAt(i));
		}
		
		for(String userid : userids) {
			removeUser(userid);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	@Override
	public boolean isSortable(int column) {
		return (column >= 0 && column < getColumnCount());
	}
	
	public Object[] createRow(String userid) {
		UserMailInfo info = new UserMailInfo(userid);
		
		if(info.userid == null || info.mail == null) return null;
		return new Object[] {info.userid, info.username, info.mail};
	}
	public String getUserIdAt(int row) {
		return (String)getValueAt(row, 0);
	}
	public ArrayList<String> getUserIdList() {
		ArrayList<String> userids = new ArrayList<String>();
		for(int i = 0; i < getRowCount(); i++) 
			userids.add(getUserIdAt(i));
		return userids;
	}
}
