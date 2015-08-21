/**
 * 
 */
package vn.spring.zebra.feedback;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.util.CommonUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserFeedbackTableReportDlg extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	protected UserFeedbackService feedbackService = null;
	protected JFrame frmParent = null;
	
	public UserFeedbackTableReportDlg(JFrame frmParent, UserFeedbackService feedbackService) {
		super(frmParent, "Feedback Report of course \"" + feedbackService.course + "\"", true);
		this.frmParent = frmParent;
		this.feedbackService = feedbackService;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		JLabel title = new JLabel("You right-click on each item to see report of each question");
		add(title, BorderLayout.NORTH);
		
		add(new JScrollPane(feedbackService.reportSummaryTable()), BorderLayout.CENTER);
		
		JPanel toolbar = new JPanel();
		JButton save = new JButton("Save Report"); save.setActionCommand("Save Report"); save.addActionListener(this);
		toolbar.add(save);
		JButton show = new JButton("Show Report"); show.setActionCommand("Show Report"); show.addActionListener(this);
		toolbar.add(show);
		add(toolbar, BorderLayout.SOUTH);
		
		setSize(800, 400);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand(); 
		if(cmd.equals("Save Report")) {
			String feedbackDir = ZebraStatic.getWowXmlRoot() + ZebraStatic.FEEDBACK_DIR;
			CommonUtil.showAndSaveReportFile(frmParent, feedbackDir,
					new File(feedbackDir + "feedbackreport_" + feedbackService.course + ".html"), 
					"<html>\n<body>\n" + feedbackService.reportHTML() + "\n</body>\n</html>");
		}
		else if(cmd.equals("Show Report")) {
			new UserFeedbackReportViewer(frmParent, feedbackService);
		}
		else if(cmd.equals("Print Report")) {
			JOptionPane.showMessageDialog(this, "Printing report isn't implemented yet", 
					"Printing report", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	
}
