/**
 * 
 */
package vn.spring.zebra.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.JXTreeTable;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.mining.DocClassifierDaemon;
import vn.spring.zebra.mining.textmining.AbstractKeywordDoc;
import vn.spring.zebra.mining.textmining.DocClassifier;
import vn.spring.zebra.search.CourseSearchLog;
import vn.spring.zebra.search.SearchLogService;
import vn.spring.zebra.util.xmlviewer.DOMUtil;
import vn.spring.zebra.util.xmlviewer.XMLTreeTable;
import vn.spring.zebra.util.xmlviewer.XMLTreeTableModel;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServerDocClassifierUI extends JDialog {
	private static final long serialVersionUID = 1L;
	
	protected TriUMServerUI serverUI = null;
	protected JComboBox course = null;
	protected JTextArea classifyingRule = null;
	protected XMLTreeTable searchXMLTable = null;
	
	public TriUMServerDocClassifierUI(TriUMServerUI serverUI) {
		super(serverUI, "User Document Classifier for discovering user interest", true);
		this.serverUI = serverUI;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		TitledBorder title = null;
		setLayout(new BorderLayout());
		
		ArrayList<String> courseList = ZebraStatic.getAuthorsConfig().getAllCourses();
		Collections.sort(courseList);
		
		JPanel load = new JPanel();
		load.add(new JLabel("Course:"));
		course = new JComboBox(courseList.toArray());
		load.add(course);
		load.add(new JButton(new AbstractAction("Load") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				Object courseText = course.getSelectedItem();
				if(courseText != null) loadCourse(courseText.toString());
			}
		}));
		add(load, BorderLayout.NORTH);
		
		JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainPane.setDividerLocation(200);
		//
		title = BorderFactory.createTitledBorder("Decision tree for classifying corpus in this course");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		classifyingRule = new JTextArea(10, 5);
		classifyingRule.setBorder(title);
		classifyingRule.setEditable(false);
		classifyingRule.setAutoscrolls(true);
		mainPane.add(new JScrollPane(classifyingRule), JSplitPane.TOP);
		//
		searchXMLTable = new XMLTreeTable();
		searchXMLTable.setAutoResizeMode(JXTreeTable.AUTO_RESIZE_ALL_COLUMNS);
		searchXMLTable.setAutoscrolls(true);
		title = BorderFactory.createTitledBorder("User Searching History in this course");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		JPanel xmlPane = new JPanel();
		xmlPane.setLayout(new BorderLayout());
		xmlPane.setBorder(title);
		xmlPane.add(new JScrollPane(searchXMLTable), BorderLayout.CENTER);
		//
		mainPane.add(xmlPane, JSplitPane.BOTTOM);
		
		add(mainPane, BorderLayout.CENTER);
		
		JButton docclass = new JButton(new AbstractAction("Find") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				findDocClass();
			}
		});
		JPanel toolbar = new JPanel();
		toolbar.add(new JLabel("Document classifying / Find user interest"));
		toolbar.add(docclass);
		add(toolbar, BorderLayout.SOUTH);
		
		Object courseText = course.getSelectedItem();
		if(courseText != null) loadCourse(courseText.toString());

		pack();
		setSize(600, 400);
		setVisible(true);
	}
	private void loadCourse(String course) {
		try {
			SearchLogService service = new SearchLogService();
			CourseSearchLog log = service.getCourseSearchLog(course, 
				ZebraStatic.MINER_LOG_BEGIN_DATE, ZebraStatic.IGNORE_ANONYMOUS);
			
	    	StringWriter strWriter = new StringWriter();
	    	log.exportXML(strWriter, true);
	    	strWriter.close();
	    	InputSource input = new InputSource(new StringReader(strWriter.getBuffer().toString()));
	    	Element courseData = DOMUtil.createDocument2(input).getDocumentElement();
	    	XMLTreeTableModel searchModel = new XMLTreeTableModel();
	    	if(courseData != null) searchModel = new XMLTreeTableModel(courseData);
	    	searchXMLTable.setTreeTableModel(searchModel);

			DocClassifier classifier = null;
			DocClassifierDaemon daemon = serverUI.server.getMiner().getCourseDocClassifierDaemon(course);
			if(daemon == null)
				classifier = serverUI.server.getMiner().addCourseDocClassifierDaemon(course);
			else
				classifier = daemon.getDocClassifier();
			classifyingRule.setText(classifier.toString());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void findDocClass() {
		String userid = JOptionPane.showInputDialog(serverUI, "Enter userid to find her/his interest\n (document class)", 
				"Document classifying in course \"" + course.getSelectedItem() + "\"", JOptionPane.INFORMATION_MESSAGE | JOptionPane.OK_CANCEL_OPTION);
		if(userid == null) return;
		userid = userid.trim();
		if(userid.length() == 0) return;
		
		try {
			String docclass = serverUI.server.communicateService.
				getQueryDelegator().getDocClass(userid, course.getSelectedItem().toString());
			if(AbstractKeywordDoc.isNoneDocClass(docclass))
				JOptionPane.showMessageDialog(serverUI, "User \"" + userid + "\" has no Document Class / User Interest in course \"" + course.getSelectedItem() + "\"", 
					"User Interest/Document Class", JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(serverUI, "Document Class/Interest of user \"" + 
					userid + "\" is \"" + docclass + "\".\n In the course \"" + course.getSelectedItem() + "\"", 
					"User Interest/Document Class", JOptionPane.INFORMATION_MESSAGE);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(serverUI, "Can't query user interest", 
					"Error classification", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
