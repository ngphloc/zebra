/**
 * 
 */
package vn.spring.zebra.feedback;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.DefaultCategoryDataset;
import org.jfree.data.DefaultPieDataset;

import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.feedback.QA.OptionPane;
import vn.spring.zebra.util.CommonUtil;
import vn.spring.zebra.util.HtmlViewer;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserFeedbackReportViewer extends JDialog {
	private static final long serialVersionUID = 1L;

	public UserFeedbackReportViewer(JFrame frmParent, String course, String html, boolean autoSaved) {
		super(frmParent, "Feedback Report Viewer of course \"" + course + "\"", true);
		add(createReportHtmlViewer(frmParent, course, html, autoSaved));
		
		setSize(800, 600);
		setVisible(true);
	}
	public UserFeedbackReportViewer(final JFrame frmParent, final UserFeedbackService feedbackService) {
		super(frmParent, "Feedback Report Viewer of course \"" + feedbackService.course + "\"", true);
		JTabbedPane mainPane = new JTabbedPane();
		mainPane.addTab("HTML Viewer", createReportHtmlViewer(frmParent, feedbackService.course, 
				"<html>\n<body>\n" + feedbackService.reportHTML() + "\n</body>\n</html>", 
				true));
		add(mainPane);
		
		UserFeedback pattern = null;
		try{
			pattern = new UserFeedback("temp", feedbackService.course);
			pattern.loadDefaultCVS();
		}
		catch(Exception e) {e.printStackTrace(); pattern = null;}
		
		int n = pattern.getNumberQA();
		if(pattern != null && n > 0) {
			ArrayList<Integer> QAs = new ArrayList<Integer>();
			
			for(int i = 0; i < n; i++) {
				QA qa = pattern.getQAbyIndex(i);
				QAs.add(qa.ID);
			}
			final UserFeedback finalPattern = pattern;
			
			JPanel qaStat = new JPanel();
			qaStat.setLayout(new BorderLayout());
			final JLabel chartLabel = new JLabel();
			final JTextField qaTitle = new JTextField(32);
			final JComboBox qaIds = new JComboBox(QAs.toArray());
			final JPanel optionPane = new JPanel();
			qaIds.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					int qaId = (Integer)e.getItem();
					QA qa = finalPattern.getQAbyID(qaId);
					qaTitle.setText(qa.title);
					chartLabel.setIcon(new ImageIcon());
					optionPane.removeAll();
					optionPane.add(qa.new OptionPane(qa));
					optionPane.revalidate();
					optionPane.repaint();
				}
			});
			
			qaStat.add(new JScrollPane(chartLabel), BorderLayout.CENTER);

			JPanel qaIDPanel = new JPanel(); qaIDPanel.setLayout(new BorderLayout());
			JPanel temp = new JPanel();
			temp.add(qaIds); temp.add(qaTitle);
			qaIDPanel.add(temp, BorderLayout.NORTH);
			qaIDPanel.add(optionPane, BorderLayout.CENTER);
			qaIDPanel.add(new JButton(new AbstractAction("Draw Graph") {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
					chartLabel.setIcon(new ImageIcon());
					
					double total = feedbackService.getUserFeedbackList().size();
					if(total == 0) {
						JOptionPane.showMessageDialog(frmParent, "There is no user feeding back");
						return;
					}

					OptionPane option = (OptionPane)optionPane.getComponent(0);
					DefaultPieDataset dataset = new DefaultPieDataset();
					QA qa = option.qa;
					if(!option.isSelected()) {
						JOptionPane.showMessageDialog(frmParent, "Must choose one answer at least");
						return;
					}
					else {
						double not = feedbackService.numberUserNotAnswering(qa.ID);
						if(not == total) {
							dataset.setValue("Not answer", not);
						}
						else {
							int[] chosen = option.getChosenIdxes();
							double answer = feedbackService.numberUserAnswering(qa.ID, chosen);
							
							double other= total - answer; 
							
							dataset.setValue("Not answer", not);
							dataset.setValue("This option", answer);
							dataset.setValue("Other options", other);
						}
					}
					JFreeChart chart = ChartFactory.createPieChart("", dataset, true, false, false);
					byte[] chartBytes = ChartUtilities.encodeAsPNG(chart.createBufferedImage(400, 350));
					ImageIcon chartIcon = new ImageIcon(chartBytes);
					chartLabel.setIcon(chartIcon);
				}
			}), BorderLayout.SOUTH);
			qaStat.add(qaIDPanel, BorderLayout.NORTH);
			
			//
			Integer qaId = (Integer)qaIds.getSelectedItem();
			if(qaId != null) {
				QA qa = finalPattern.getQAbyID(qaId);
				qaTitle.setText(qa.title);
				chartLabel.setIcon(new ImageIcon());
				optionPane.removeAll();
				optionPane.add(qa.new OptionPane(qa));
				optionPane.revalidate();
				optionPane.repaint();
			}
			mainPane.addTab("Answering", new JScrollPane(qaStat));
			
			//
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			JLabel notAnswerLabel = new JLabel();
			mainPane.addTab("Not answering", new JScrollPane(notAnswerLabel));
			for(int i = 0; i < n; i++) {
				QA qa = pattern.getQAbyIndex(i);
				double value = feedbackService.numberUserNotAnswering(qa.ID);
				dataset.addValue(value, "Question " + qa.ID, "");
			}
			JFreeChart notAnswerChart = ChartFactory.createBarChart3D("Not answer", "Question ID",
					"Number of users", dataset, PlotOrientation.VERTICAL, true, // legend?
					true, // tooltips?
					false // URLs?
					);
			byte[] chartBytes = ChartUtilities.encodeAsPNG(notAnswerChart.createBufferedImage(400, 350));
			notAnswerLabel.setIcon(new ImageIcon(chartBytes));
			
		}
		setSize(800, 600);
		setVisible(true);
	}
	public UserFeedbackReportViewer(JFrame frmParent, UserFeedback feedback) {
		String username = null;
		try {
			ProfileDB pdb = ZebraStatic.getProfileDB();
			Profile profile = pdb.getProfile(pdb.findProfile(feedback.userid));
			username = profile.getAttributeValue("personal", "name");
		}
		catch(Exception e) {e.printStackTrace();}
		if(username == null) username = feedback.userid;
		String html = "<html>\n<body>\n" + "\n<h1>Feedback report of " + username + "</h1>\n" +
			feedback.genHTML(false) + "\n</body>\n</html>";
		add(createReportHtmlViewer(frmParent, feedback.course, html, false));
		
		setSize(800, 600);
		setVisible(true);
	}
	
	private static JPanel createReportHtmlViewer(final JFrame frmParent, final String course, final String html, boolean autoSaved) {
		JPanel htmlViewer = new JPanel();
		htmlViewer.setLayout(new BorderLayout());
		
		htmlViewer.add(new JLabel("Feedback Report Viewer"), BorderLayout.NORTH);
		URL url = null;
		final String feedbackDir = ZebraStatic.getWowXmlRoot() + ZebraStatic.FEEDBACK_DIR;
		String filepath = feedbackDir + "feedbackreport_" + course + ".html";
		if(autoSaved) {
			try {
				FileWriter writer = new FileWriter(filepath);
	        	writer.write(html); writer.flush(); writer.close();
				url = new URL("file:/" + filepath);
			}
			catch(Exception ex) {ex.printStackTrace(); url = null;}
		}
		
		if(autoSaved && url != null) {
			JOptionPane.showMessageDialog(frmParent, "The default feedback report file was saved automatically:\n\"" + 
					filepath + 
					"\"\n\n An now you click OK to see it!", 
					"Notice about feedback report file", JOptionPane.INFORMATION_MESSAGE);
			htmlViewer.add(new JScrollPane(new HtmlViewer(url)), BorderLayout.CENTER);
		}
		else {
			htmlViewer.add(new JScrollPane(new HtmlViewer(html)), BorderLayout.CENTER);
			JButton save = new JButton(new AbstractAction("Save Report") {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
					CommonUtil.showAndSaveReportFile(frmParent, feedbackDir, null, html);
				}
			});
			htmlViewer.add(save, BorderLayout.SOUTH);
		}
		
		return htmlViewer;
	}
	
	public static void main(String[] args) {
		UserFeedbackService service = new UserFeedbackService("javatutorial");
		service.load();
		new UserFeedbackReportViewer(null, service);
	}
}
