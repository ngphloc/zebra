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

public class RegisterInputDlg extends JDialog implements UIDispose {
	private static final long serialVersionUID = 1L;
	public boolean isOK = false;
	
	public String userid = null;
	public String username = null;
	public String password = null;
	public String course = null;
	public String directory = null;
	public HashMap<String, String> personalAttrs = new HashMap<String, String>();
	
	public JTextField useridText = null;
	public JTextField usernameText = null;
	private JPasswordField passwordText = null;
	private JComboBox  courseText = null;
	private JTextField directoryText = null;
	
	public RegisterInputDlg(JFrame frmParent) {
		super(frmParent, "User Course Input for Register (Add)", true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new GridLayout(0, 2));
		
		useridText = new JTextField(32);
		mainPane.add(new JLabel("User ID (*):"));
		mainPane.add(useridText);
		
		usernameText = new JTextField(32);
		mainPane.add(new JLabel("User Name:"));
		mainPane.add(usernameText);

		passwordText = new JPasswordField(32);
		mainPane.add(new JLabel("Password (*):"));
		mainPane.add(passwordText);

		courseText = new JComboBox();
		mainPane.add(new JLabel("Course (*):"));
		mainPane.add(courseText);
		
		directoryText = new JTextField(32);
		mainPane.add(new JLabel("Directory (*):"));
		mainPane.add(directoryText);

		ArrayList<String> courseList = ZebraStatic.getAuthorsConfig().getAllCourses();
		Collections.sort(courseList);
		courseText.setModel(new DefaultComboBoxModel(courseList.toArray()));
		if(courseList.size() > 0) {
			courseText.setSelectedIndex(0);
			directoryText.setText(courseList.get(0));
		}
		courseText.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				try {
					String course = e.getItem().toString().toLowerCase();
					directoryText.setText(course);
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		setLayout(new BorderLayout());
		add(new JLabel("Enter user information"), BorderLayout.NORTH);
		add(mainPane, BorderLayout.CENTER);
		add(
			new JButton(new AbstractAction("OK") {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
					onOK();
				}
			}), BorderLayout.SOUTH);
		pack();
		setSize(200, 200);
		setVisible(true);
	}
	private void onOK() {
		userid = useridText.getText();
		if(userid != null) {
			userid = userid.trim();
			if(userid.length() == 0) userid = null; 
		}
		
		username = usernameText.getText();
		if(username != null) {
			username = username.trim();
			if(username.length() == 0) username = userid; 
		}
		else
			username = userid;
		
		password = passwordText.getText();
		if(password != null) {
			password = password.trim();
			if(password.length() == 0) password = null; 
		}

		if(courseText.getSelectedItem() == null)
			course = null;
		else
			course = courseText.getSelectedItem().toString();
		if(course != null) {
			course = course.trim();
			if(course.length() == 0) course = null; 
		}
		
		directory = directoryText.getText();
		if(directory != null) {
			directory = directory.trim();
			if(directory.length() == 0) directory = null; 
		}

		if(!checkValid()) {
			JOptionPane.showMessageDialog(getParent(), "Some fields are empty!", "Error!", JOptionPane.ERROR_MESSAGE);
			isOK = false;
			setVisible(false); dispose();
		}
		else {
			isOK = true;
			setVisible(false); dispose();
		}
	}
	private boolean checkValid() {
		if(userid == null || password == null || course == null || directory == null) return false;
		return true;
	}
}
