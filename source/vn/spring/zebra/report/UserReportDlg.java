/**
 * 
 */
package vn.spring.zebra.report;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.mail.CourseMailingListDaemon;
import vn.spring.zebra.server.TriUMServer;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.util.CommonUtil;
import vn.spring.zebra.util.HtmlViewer;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserReportDlg extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	protected UserReportService userReportService = null;
	protected String htmlReport = null;
	protected JFrame parent;
	
	public UserReportDlg(JFrame parent, TriUM um) {
		super(parent, "User Total Report", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.parent = parent;
		
		try {
			userReportService = new UserReportService(um);
			setLayout(new BorderLayout());
			
			htmlReport = userReportService.genHtmlReport();
			HtmlViewer htmlViewer = new HtmlViewer(htmlReport);
			add(new JScrollPane(htmlViewer), BorderLayout.CENTER);
			
			JPanel toolbar = new JPanel();
			JButton save = new JButton("Save"); 
			save.setActionCommand("Save"); 
			save.addActionListener(this);
			toolbar.add(save);
			//
			JButton sendmail = new JButton("Send Mail"); 
			sendmail.setActionCommand("Send Mail"); 
			sendmail.addActionListener(this);
			toolbar.add(sendmail);
			add(toolbar, BorderLayout.SOUTH);
			
			setSize(800, 600);
			setVisible(true);
		}
		catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Can't show user report", "Report error", JOptionPane.ERROR_MESSAGE);
			this.setVisible(false);
			this.dispose();
		}
		
	}
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand(); 
		if(cmd.equals("Save")) {
			String userid = userReportService.um.getUserId();
			String course = userReportService.um.getCourse();
			CommonUtil.showAndSaveReportFile(parent, ZebraStatic.getWowXmlRoot(),
				new File(ZebraStatic.getWowXmlRoot() + "/report_" + userid + "_" + course + ".html"), 
				"<html>\n<body>\n" + htmlReport + "\n</body>\n</html>");
		}
		else if(cmd.equals("Send Mail")) {
			try {
				String userid = userReportService.um.getUserId();
				String course = userReportService.um.getCourse();
				CourseMailingListDaemon daemon = TriUMServer.getInstance().getMailingListService().getCourseMailingListDaemon(course);
				if(daemon != null) {
					ArrayList<String> userids = new ArrayList<String>();
					userids.add(userid);
					daemon.getCourseMailingList().sendReport(userids, "<html>\n<body>\n" + htmlReport + "\n</body>\n</html>");
				}
			}
			catch(Exception ex) {ex.printStackTrace();}
		}
	}
	
}
