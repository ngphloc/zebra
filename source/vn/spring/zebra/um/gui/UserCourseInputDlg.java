/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserCourseInputDlg extends JDialog implements UIDispose {
	private static final long serialVersionUID = 1L;
	public boolean isOK = false;
	public String userid = null;
	public String course = null;
	
	public JTextField useridText = null;
	private JComboBox  courseText = null;
	public UserCourseInputDlg(JFrame frmParent, String default_userid, String default_course, String title) {
		super(frmParent, title, true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new GridLayout(0, 2));
		useridText = new JTextField(16);
		mainPane.add(new JLabel("User ID (*):"));
		mainPane.add(useridText);
		courseText = new JComboBox();
		mainPane.add(new JLabel("Course (*):"));
		mainPane.add(courseText);
		
		ArrayList<String> courseList = ZebraStatic.getAuthorsConfig().getAllCourses();
		Collections.sort(courseList);
		courseText.setModel(new DefaultComboBoxModel(courseList.toArray()));

		if(default_userid != null && default_userid.length() > 0)
			this.useridText.setText(default_userid);
		if(default_course != null && default_course.length() > 0)
			this.courseText.setSelectedItem(default_course);

		setLayout(new BorderLayout());
		add(new JLabel("Enter user id and course"), BorderLayout.NORTH);
		add(mainPane, BorderLayout.CENTER);
		add(
			new JButton(new AbstractAction("OK") {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
					onOK();
				}
			}), BorderLayout.SOUTH);
		pack();
		setVisible(true);
	}

	private void onOK() {
		isOK = true;
		
		userid = useridText.getText();
		if(userid != null && userid.length() == 0) {
			userid = userid.trim();
			if(userid.length() == 0) userid = null; 
		}
		
		if(courseText.getSelectedItem() == null)
			course = null;
		else
			course = courseText.getSelectedItem().toString();
		if(course != null && course.length() == 0) {
			course = course.trim();
			if(course.length() == 0) course = null; 
		}
		
		if(!checkValid()) {
			if(JOptionPane.showConfirmDialog(getParent(), "Some fields are empty.\n Do you want to continue?") == JOptionPane.OK_OPTION) {
				setVisible(false); dispose();
			}
		}
		else {
			setVisible(false); dispose();
		}
	}
	private boolean checkValid() {
		if(userid == null || course == null) return false;
		return true;
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
