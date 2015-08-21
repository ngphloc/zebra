/**
 * 
 */
package vn.spring.zebra.feedback;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserFeedbackHtmlReportDlg extends JDialog {
	private static final long serialVersionUID = 1L;
	protected UserFeedbackService feedbackService = null;
	
	public UserFeedbackHtmlReportDlg(JFrame frmParent, UserFeedbackService feedbackService) {
		super(frmParent, "Feedback Report", true);
		this.feedbackService = feedbackService;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		JLabel title = new JLabel("Feedback Report");
		add(title, BorderLayout.NORTH);
		
		JLabel html = new JLabel("<html>\n<body>\n" + feedbackService.reportSummaryHTML() + "\n</body>\n</html>") {
			private static final long serialVersionUID = 1L;
            public Dimension getPreferredSize() {
                return new Dimension(800, 600);
            }
			
		};
		html.setVerticalAlignment(SwingConstants.TOP);
		html.setHorizontalAlignment(SwingConstants.LEFT);
		html.setAutoscrolls(true);
		add(new JScrollPane(html), BorderLayout.CENTER);
		
		setSize(800, 600);
		//pack();
		setVisible(true);
	}

}
