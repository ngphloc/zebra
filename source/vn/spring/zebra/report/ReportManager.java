/**
 * 
 */
package vn.spring.zebra.report;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import vn.spring.zebra.server.TriUMServer;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.util.CourseList;
import vn.spring.zebra.util.UserList;
import vn.spring.zebra.util.UserListModel;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ReportManager extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	protected JFrame parent = null;
	protected UserList userList = null;
	protected CourseList courseList = null;
	
	public ReportManager(JFrame parent) {
		super(parent, "Total Report", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.parent = parent;
		setLayout(new BorderLayout());
		
		JPanel coursePanel = new JPanel(); 
		coursePanel.add(new JLabel("Course List: "));
		courseList = new CourseList();
		coursePanel.add(courseList);
		add(coursePanel, BorderLayout.NORTH);

		JPanel userPanel = new JPanel();
		userPanel.setLayout(new BorderLayout());
		userPanel.add(new JLabel("User List:"), BorderLayout.NORTH);
		userList = new UserList(new UserListModel());
		userPanel.add(new JScrollPane(userList), BorderLayout.CENTER);
		add(userPanel, BorderLayout.CENTER);
		
		JPanel toolbar = new JPanel();
		//
		JButton showCourseReport = new JButton("Show Course Report"); 
		showCourseReport.setActionCommand("Show Course Report"); 
		showCourseReport.addActionListener(this);
		toolbar.add(showCourseReport);
		//
		JButton showUserReport = new JButton("Show User Report"); 
		showUserReport.setActionCommand("Show User Report"); 
		showUserReport.addActionListener(this);
		toolbar.add(showUserReport);
		//
		add(toolbar, BorderLayout.SOUTH);
		
		setSize(800, 600);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand(); 
		if(cmd.equals("Show Course Report")) {
			new CourseReportDlg(parent, courseList.getSelectedItem().toString());
		}
		else if(cmd.equals("Show User Report")) {
			try {
				String userid = userList.getSelectedUserId();
				String course = courseList.getSelectedItem().toString();
				TriUM um = TriUMServer.getInstance().getUM(userid, course, false);
				if(um == null) {
					JOptionPane.showMessageDialog(parent, 
						"User not existing or monitored.\nYou should create and monitor it", "User report error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				new UserReportDlg(parent, um);
			}
			catch(Exception ex) {
				ex.fillInStackTrace();
				JOptionPane.showMessageDialog(parent, 
						"Error when showing user report", "User report error!", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
}
