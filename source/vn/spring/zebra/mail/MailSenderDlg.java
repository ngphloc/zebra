/**
 * 
 */
package vn.spring.zebra.mail;

import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class MailSenderDlg extends JDialog {
	private static final long serialVersionUID = 1L;

	public MailSenderDlg(JFrame parent, ArrayList<String> userids) {
		super(parent, "Sending Mail", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		add(new MailSenderGUI(this, userids));
		
		setSize(600, 400);
		setVisible(true);
	}
	public MailSenderDlg(JFrame parent, String mlCourse, ArrayList<String> mlSentUserIds) {
		super(parent, "Sending Mail", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		add(new MailSenderGUI(this, mlCourse, mlSentUserIds));
		
		setSize(600, 400);
		setVisible(true);
	}
}
