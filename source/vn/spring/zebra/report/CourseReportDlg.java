/**
 * 
 */
package vn.spring.zebra.report;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.util.CommonUtil;
import vn.spring.zebra.util.HtmlViewer;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseReportDlg extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	protected JFrame parent = null;
	protected CourseReportService courseReportService = null;
	protected String htmlReport = null;
	
	public CourseReportDlg(JFrame parent, String course) {
		super(parent, "Course Report", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.parent = parent;
		
		try {
			courseReportService = new CourseReportService(course);
		}
		catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Can't create course report service", 
				"Course report service error!", JOptionPane.ERROR_MESSAGE);
			this.dispose();
			this.setVisible(false);
			return;
		}
		
		htmlReport = courseReportService.genReport();
		HtmlViewer htmlViewer = new HtmlViewer(htmlReport);
		add(new JScrollPane(htmlViewer), BorderLayout.CENTER);

		JPanel toolbar = new JPanel();
		JButton save = new JButton("Save Course Report"); 
		save.setActionCommand("Save Course Report"); 
		save.addActionListener(this);
		toolbar.add(save);
		add(toolbar, BorderLayout.SOUTH);
		
		setSize(800, 600);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand(); 
		if(cmd.equals("Save Course Report")) {
			CommonUtil.showAndSaveReportFile(parent, ZebraStatic.getWowXmlRoot(),
					new File(ZebraStatic.getWowXmlRoot() + "/report_" + courseReportService.courseInfo.courseName + ".html"), 
					"<html>\n<body>\n" + htmlReport + "\n</body>\n</html>");
		}
		
	}
	
	
}
