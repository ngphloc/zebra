/**
 * 
 */
package vn.spring.zebra.feedback;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserFeedbackService {
	protected String                course = null;
	protected HashSet<UserFeedback> feedbacks = new HashSet<UserFeedback>();
	
	public UserFeedbackService(String course) {this.course = course;}
	
	public void addUserFeedback(UserFeedback feedback)    {feedbacks.add(feedback);}
	public void removeUserFeedback(UserFeedback feedback) {feedbacks.remove(feedback);}
	public void removeUserFeedback(String userid) {
		Iterator<UserFeedback> iter = feedbacks.iterator();
		while(iter.hasNext()) {
			UserFeedback feedback = iter.next();
			if(feedback.userid.equals(userid)) {
				feedbacks.remove(feedback);
				return;
			}
		}
	}
	public void clearUserFeedbackList() {feedbacks.clear();}
	public int  sizeOfUserFeedbackList() {return feedbacks.size();}
	
	public UserFeedback getUserFeedback(String userid) {
		Iterator<UserFeedback> iter = feedbacks.iterator();
		while(iter.hasNext()) {
			UserFeedback feedback = iter.next();
			if(feedback.userid.equals(userid)) {
				return feedback;
			}
		}
		return null;
	}
	public ArrayList<UserFeedback> getUserFeedbackList() {
		ArrayList<UserFeedback> feedbackList = new ArrayList<UserFeedback>();
		Iterator<UserFeedback> iter = feedbacks.iterator();
		while(iter.hasNext()) {
			feedbackList.add(iter.next());
		}
		return feedbackList;
	}
	
	public void load() {
		clearUserFeedbackList();
		File feedbackDir = new File(ZebraStatic.getWowXmlRoot() + ZebraStatic.FEEDBACK_DIR);
		File[] fileList = feedbackDir.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return (f.getName().endsWith(".xml") && f.getName().startsWith("feedback_"));
			}
		});
		
		for(File file : fileList) {
			try {
				String filename = file.getName();
				String userid = filename.substring("feedback_".length(), filename.indexOf("."));
				UserFeedback feedback = new UserFeedback(userid, course);
				feedback.loadXML(new FileReader(file));
				addUserFeedback(feedback);
			}
			catch(Exception e) {System.out.println("Feedback service causes error: " + e.getMessage());}
		}
	}
	
	public int numberUserAnswering(int qaID, int optionIdx) {
		int count = 0;
		for(UserFeedback feedback : feedbacks) {
			QA qa = feedback.getQAbyID(qaID);
			if(qa == null) continue;
			if(qa.isOptionChosen(optionIdx)) count++;
		}
		return count;
	}
	public int numberUserAnswering(int qaID, int answerIdxes[]) {
		int count = 0;
		for(UserFeedback feedback : feedbacks) {
			QA qa = feedback.getQAbyID(qaID);
			if(qa == null) continue;
			if(qa.isOptionChosen(answerIdxes)) count++;
		}
		return count;
		
	}
	
	public int numberUserNotAnswering(int qaID) {
		int count = 0;
		for(UserFeedback feedback : feedbacks) {
			QA qa = feedback.getQAbyID(qaID);
			if(qa == null) continue;
			if(!qa.isAnswered()) count++;
		}
		return count;
	}
	public String reportHTML() {
		StringBuffer html = new StringBuffer();
		try {
			UserFeedback pattern = new UserFeedback("temp", course);
			pattern.loadDefaultCVS();
			html.append("<div>" + "\n");
			html.append(reportHTML(pattern.getQAList()) + "\n");
			html.append("<br/><br/><hr/>" + "\n");
			html.append(reportHTMLUser(pattern.getQAList()) + "\n");
			html.append("</div>");
		}
		catch(Exception e) {e.printStackTrace();}
		return html.toString();
	}

	private String reportHTML(ArrayList<QA> patterns) {
		StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		html.append("<a name=\"top\"/><h1 align=\"center\">Feedback Report on Course \"" + course + "\"</h1>" + "\n");

		html.append("<h2>Summary</h1>" + "\n");
		html.append(reportSummaryHTML(patterns) + "\n");
		html.append("<br/><br/><hr/>");
		
		for(int i = 0; i < patterns.size(); i++) {
			html.append(reportHTML(patterns.get(i)) + "\n");
			if(i < patterns.size() - 1) html.append("<br/><br/><hr/>" + "\n");
		}
		
		html.append("</div>");
		return html.toString();
	}

	private String reportHTML(QA pattern) {
		StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		html.append("<a name=\"qa_" + pattern.ID + "\"/>" + "\n");
		html.append("<h2>Question " + pattern.ID + ": " + pattern.title + "</h2>");
		html.append("<table border=\"1\" cellspacing=\"1\" cellpadding=\"1\">" + "\n");
		
		html.append(pattern.genReportHTML() + "\n");
		
		int total = sizeOfUserFeedbackList();
		String summary = "";
		for(int i = 0; i < pattern.options.length; i++) {
			int answerNumber = numberUserAnswering(pattern.ID, i);
			int answerPercent = (int)(((double)answerNumber/(double)total)*100.0);
			summary += "Number of users answer \"" + pattern.options[i] +  "\": " + answerNumber + "/" + total + "(" + answerPercent + "%)" + "<br/>\n";
		}
		int notAnswerNumber = numberUserNotAnswering(pattern.ID);
		int notAnswerPercent = (int)(((double)notAnswerNumber/(double)total)*100.0);
		summary += "Number of users not answer: " + notAnswerNumber + "/" + total + "(" + notAnswerPercent + "%)" + "<br/>\n";
		html.append("<tr><td>Summary</td><td>" + summary + "</td></tr>" + "\n");

		ArrayList<UserFeedback> users = getUserFeedbackList();
		String optiondetail = "<ul>" + "\n";
		for(int i = 0; i < pattern.options.length; i++) {
			String chosenUsers = "";
			boolean hasChosen = false;
			for(UserFeedback user : users) {
				QA qa = user.getQAbyID(pattern.ID);
				if(qa == null) continue;
				if(qa.isOptionChosen(i)) {
					chosenUsers += "<a href=\"#userid_" + user.userid + "_" + pattern.ID + "\">" + 
						user.userid + "</a>, ";
					hasChosen = true;
				}
			}
			String optionhref = "<a name=\"qa_" + pattern.ID + "_" + i + "\"/>\"" + pattern.options[i] + "\"";
			if(hasChosen) {
				chosenUsers = chosenUsers.trim();
				if(chosenUsers.length() > 0 && chosenUsers.charAt(chosenUsers.length() - 1) == ',')
					chosenUsers = chosenUsers.substring(0, chosenUsers.length() - 1);
				optiondetail += "<li>Option " + optionhref + " was chosen by users: " + chosenUsers + "</li>\n";
			}
			else {
				optiondetail += "<li>Option " + optionhref + " wasn't chosen yet" + "</li>\n";
			}
		}
		optiondetail += "</ul>" + "\n";
		html.append("<tr><td>Option details</td><td>" + optiondetail + "</td></tr>" + "\n");
		
		html.append("</table>" + "\n");
		html.append("</div>");
		
		return html.toString();
	}
	private String reportHTMLUser(ArrayList<QA> patterns) {
		StringBuffer html = new StringBuffer();

		html.append("<div>" + "\n");
		html.append("<a name=\"user_top\"/>" + "\n");
		html.append("<h2>Users</h2>");
		html.append("<table border=\"2\" cellspacing=\"2\" cellpadding=\"2\">" + "\n");
		html.append("<tr><th>User ID</th><th>User Name</th><th>Answers</th></tr>" + "\n");
		
		ArrayList<UserFeedback> users = getUserFeedbackList();

		for(UserFeedback user : users) {
			html.append(reportHTMLUser(user.userid, patterns) + "\n");
		}
		html.append("</table>" + "\n");
		html.append("</div>");
		return html.toString();
	}
	private String reportHTMLUser(String userid, ArrayList<QA> patterns) {
		UserFeedback feedback = getUserFeedback(userid);
		if(feedback == null) return "";
		
		String username = null;
		try {
			ProfileDB pdb = ZebraStatic.getProfileDB();
			Profile profile = pdb.getProfile(pdb.findProfile(userid));
			username = profile.getAttributeValue("personal", "name");
		}
		catch(Exception e) {e.printStackTrace();}
		if(username == null) username = userid;
		
		StringBuffer html = new StringBuffer();
		html.append("<tr>" + "\n");
		html.append("<td><a name=\"userid_" + userid + "\"/>" + userid + "</td>" + "\n");
		html.append("<td>" + username + "</td>" + "\n");
		
		String userdetail = "<ul>" + "\n";
		for(QA pattern : patterns) {
			QA qa = feedback.getQAbyID(pattern.ID);
			String question = "Question <a href=\"#qa_" +  qa.ID + "\">" + qa.ID + "</a>: ";
			String userindex = "<a name=\"userid_" + userid + "_" + qa.ID + "\"/>";
			
			if(qa.isAnswered()) {
				String answers = "";
				for(int i = 0; i < qa.answerIdxes.length; i++) {
					if(qa.answerIdxes[i] != 0)
						answers += "<a href=\"#qa_" + qa.ID + "_" + i + "\">" + qa.getOption(i) + "</a>, ";
				}
				answers = answers.trim();
				if(answers.length() > 0 && answers.charAt(answers.length() - 1) == ',')
					answers = answers.substring(0, answers.length() - 1);
				userdetail += "<li>" + question + userindex + "(S)he chose: " + answers + "</li>\n";
			}
			else {
				userdetail += "<li>" + question + userindex + "(S)he hasn't answered this question yet" + "</li>\n";
			}
		}
		userdetail += "</ul>" + "\n";
		html.append("<td>" + userdetail + "</td>" + "\n");

		html.append("</tr>" + "\n");
		return html.toString();
	}
	
	private JPanel reportGUI(QA pattern) {
		JPanel main = new JPanel();
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		JPanel row = null;

		row = new JPanel(); row.setLayout(new BorderLayout());
		row.add(pattern.genReportGUI(), BorderLayout.CENTER);
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		main.add(row);

		JTextArea txtSummary = new JTextArea(5, 10);
		txtSummary.setEditable(false);
		int total = sizeOfUserFeedbackList();
		String summary = "";
		for(int i = 0; i < pattern.options.length; i++) {
			int answerNumber = numberUserAnswering(pattern.ID, i);
			int answerPercent = (int)(((double)answerNumber/(double)total)*100.0);
			summary += "Number of users answer \"" + pattern.options[i] +  "\": " + answerNumber + "/" + total + "(" + answerPercent + "%)" + "\n";
		}
		int notAnswerNumber = numberUserNotAnswering(pattern.ID);
		int notAnswerPercent = (int)(((double)notAnswerNumber/(double)total)*100.0);
		summary += "Number of users not answer: " + notAnswerNumber + "/" + total + "(" + notAnswerPercent + "%)" + "\n";
		txtSummary.setText(summary);
		row = new JPanel(); row.setLayout(new BorderLayout());
		row.add(new JLabel("Summary:"), BorderLayout.WEST); row.add(new JScrollPane(txtSummary), BorderLayout.CENTER);
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		main.add(row);
		
		ArrayList<UserFeedback> users = getUserFeedbackList();

		JTextArea txtOptionDetail = new JTextArea(5, 10);
		txtOptionDetail.setEditable(false);
		String optiondetail = "";
		for(int i = 0; i < pattern.options.length; i++) {
			String chosenUsers = "";
			boolean hasChosen = false;
			for(UserFeedback user : users) {
				QA qa = user.getQAbyID(pattern.ID);
				if(qa == null) continue;
				if(qa.isOptionChosen(i)) {
					chosenUsers += user.userid + ", ";
					hasChosen = true;
				}
			}
			if(hasChosen) {
				chosenUsers = chosenUsers.trim();
				if(chosenUsers.length() > 0 && chosenUsers.charAt(chosenUsers.length() - 1) == ',')
					chosenUsers = chosenUsers.substring(0, chosenUsers.length() - 1);
				optiondetail += "Option \"" + pattern.options[i] + "\" has chosen by users: " + chosenUsers + "\n\n";
			}
			else {
				optiondetail += "Option \"" + pattern.options[i] + "\" hasn't chosen yet" + "\n";
			}
		}
		txtOptionDetail.setText(optiondetail);
		row = new JPanel(); row.setLayout(new BorderLayout());
		row.add(new JLabel("Option Details:"), BorderLayout.WEST); row.add(new JScrollPane(txtOptionDetail), BorderLayout.CENTER);
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		main.add(row);
		
		JTextArea txtUserDetail = new JTextArea(5, 10);
		txtUserDetail.setEditable(false);
		String userdetail = "";
		for(UserFeedback user : users) {
			QA qa = user.getQAbyID(pattern.ID);
			if(qa == null) continue;
			if(qa.isAnswered()) {
				String answers = "";
				for(int i = 0; i < qa.answerIdxes.length; i++) {
					if(qa.answerIdxes[i] != 0) answers += qa.getOption(i) + ", ";
				}
				answers = answers.trim();
				if(answers.length() > 0 && answers.charAt(answers.length() - 1) == ',')
					answers = answers.substring(0, answers.length() - 1);
				userdetail += "User \"" + user.userid + "\" has chosen: " + answers + "\n\n";
			}
			else {
				userdetail += "User \"" + user.userid + "\" hasn't answered this question yet" + "\n";
			}
		}
		txtUserDetail.setText(userdetail);
		row = new JPanel(); row.setLayout(new BorderLayout());
		row.add(new JLabel("User Details:"), BorderLayout.WEST); row.add(new JScrollPane(txtUserDetail), BorderLayout.CENTER);
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		//main.add(row);

		return main;
	}

	public String reportSummaryHTML() {
		String html = "";
		try {
			UserFeedback pattern = new UserFeedback("temp", course);
			pattern.loadDefaultCVS();
			html += reportSummaryHTML(pattern.getQAList());
		}
		catch(Exception e) {e.printStackTrace();}
		return html;
	}
	protected JTable reportSummaryTable() {
		try {
			UserFeedback pattern = new UserFeedback("temp", course);
			pattern.loadDefaultCVS();
			return reportSummaryTable(pattern.getQAList());
		}
		catch(Exception e) {e.printStackTrace(); return new JTable(new MyDefaultTableModel(new Vector<Object>()));}
	}
	
	private String reportSummaryHTML(ArrayList<QA> patterns) {
		String html = "";
		html += "<table border=\"2\" cellspacing=\"4\" cellpadding=\"4\">" + "\n";
		html += "<tr><th>ID</th><th>Question</th><th>Answers of <a href=\"#user_top\">Users</a></th></tr>" + "\n";
		for(int i = 0; i < patterns.size(); i++)
			html += reportSummaryHTML(patterns.get(i)) + "\n";
		html += "</table>";
		return html;
	}
	private String reportSummaryHTML(QA pattern) {
		String index = "<a name=\"qa_" + pattern.ID + "_top\"/>";
		
		String html = "";
		html += "<tr>" + "\n";
		html += "  <td>" + index + "<a href=\"#qa_" + pattern.ID + "\">" + pattern.ID + "</a></td>" + "\n";
		html += "  <td>" + pattern.title + "</td>" + "\n";
		html += "  <td>" + "\n";
		html += "    <table border=\"1\">" + "\n";
		
		html += "      <tr>" + "\n";
		for(int i = 0; i < pattern.options.length; i++) {
		String optionhref = "<a href=\"#qa_" + pattern.ID + "_" + i + "\">\"" + pattern.options[i] + "\"</a>";
		html += "        <td>Number of users answering:<br/>" + optionhref + "</td>" + "\n";
		}
		html += "        <td>Number of users not answering</td>" + "\n"; 
		html += "      </tr>" + "\n";

		html += "      <tr>" + "\n";
		int total = sizeOfUserFeedbackList();
		for(int i = 0; i < pattern.options.length; i++) {
			int answerNumber = numberUserAnswering(pattern.ID, i);
			int answerPercent = (int)(((double)answerNumber/(double)total)*100.0);
			html += "        <td><strong>" +  answerNumber + "</strong>/" + total + "(" + answerPercent + "%)</td>" + "\n";
		}
		int notAnswerNumber = numberUserNotAnswering(pattern.ID);
		int notAnswerPercent = (int)(((double)notAnswerNumber/(double)total)*100.0);
		html += "        <td><strong>" + notAnswerNumber + "</strong>/" + total + "(" + notAnswerPercent + "%)</td>" + "\n"; 
		html += "      </tr>" + "\n";

		html += "    </table>" + "\n";
		html += "  </td>" + "\n";
		html += "</tr>";
		return html;
	}

	private JTable reportSummaryTable(final ArrayList<QA> patterns) {
		int maxCol = 0;
		for(int i = 0; i < patterns.size(); i++) {
			QA qa = patterns.get(i);
			if(maxCol < qa.options.length) maxCol = qa.options.length;
		}
		Vector<Object> colNames = new Vector<Object>();
		colNames.add("ID");
		colNames.add("Question");
		for(int i = 0; i < maxCol; i++) colNames.add("Summary" + (i + 1));
		colNames.add("Summary" + (maxCol + 1));
		
		final JTable table = new JTable(new MyDefaultTableModel(colNames));
		table.setPreferredScrollableViewportSize(new Dimension(800, 400));
		table.setAutoscrolls(true);
		table.getColumn("ID").setMaxWidth(25);
		for(int i = 0; i < patterns.size(); i++) {
			QA qa = patterns.get(i);
			int total = sizeOfUserFeedbackList();
			ArrayList<Object> row = new ArrayList<Object>();
			row.add(qa.ID);
			row.add(qa.title);
			for(int j = 0; j < qa.options.length; j++) {
				int answerNumber = numberUserAnswering(qa.ID, j);
				int answerPercent = (int)(((double)answerNumber/(double)total)*100.0);
				row.add("\"" + qa.options[j] +  "\": " + answerNumber + "/" + total + "(" + answerPercent + "%)");
			}
			int notAnswerNumber = numberUserNotAnswering(qa.ID);
			int notAnswerPercent = (int)(((double)notAnswerNumber/(double)total)*100.0);
			row.add("Not answer: " + notAnswerNumber + "/" + total + "(" + notAnswerPercent + "%)");
			((DefaultTableModel)table.getModel()).addRow(row.toArray());
		}

		table.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(javax.swing.SwingUtilities.isRightMouseButton(e) ) {
					JPopupMenu contextMenu = new JPopupMenu();
					JMenuItem open = new JMenuItem(new AbstractAction("Open") {
				    	private static final long serialVersionUID = 1L;
						public void actionPerformed(ActionEvent e) {
							int selectedRow = table.getSelectedRow();
							if(selectedRow == -1) return;
							int ID = (Integer)table.getModel().getValueAt(selectedRow, 0);
							QA qa = null;
							for(int i = 0; i < patterns.size(); i++) {
								if(patterns.get(i).ID == ID) {
									qa = patterns.get(i);
									break;
								}
							}
							if(qa == null) return;
							
							JDialog dlg = new JDialog();
							dlg.setModal(true);
							dlg.setTitle("User Feedback Report");
							dlg.add(new JScrollPane(reportGUI(qa)));
							dlg.setSize(600, 400);
							dlg.setVisible(true);
							
						}
					});
					contextMenu.add(open);
					if(contextMenu != null) contextMenu.show((Component)e.getSource(), e.getX(), e.getY());
				}
			}
		});
		return table;
	}
    private class MyDefaultTableModel extends DefaultTableModel {
    	private static final long serialVersionUID = 1L;

		public MyDefaultTableModel(Vector<Object> colNames) {
			super(colNames, 0);
			setColumnIdentifiers(colNames.toArray());
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
    }
	
	public static void main(String[] args) {
		UserFeedbackService service = new UserFeedbackService("javatutorial");
		service.load();
		//System.out.println(service.reportHTML());
		new UserFeedbackTableReportDlg(null, service);
	}
}
