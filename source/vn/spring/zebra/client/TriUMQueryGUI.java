/**
 * 
 */
package vn.spring.zebra.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMQueryGUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int TEXT_FIELD_COLUMNS = 32;
	
	protected String fixUserId = null;

	public JComboBox protocol = null;
	public JTextField host = null;
	public JTextField port = null;
	public JComboBox type = null;
	public JTextField userid = null;
	public JComboBox course = null;
	public JComboBox concept = null;
	
	public JTextField knowledge = null;
	public JTextField ls = null;
	public JTextField prerec = null;
	public JTextField postrec = null;
	public JTextField lp = null;
	public JTextField community = null;
	public JTextField studytime = null;
	public JTextField docclass = null;
	public JTable     infoTable = null;
	
	private JTextArea msg = null;
	
	private JPanel createRowPanel(JComponent left, JComponent right) {
		JPanel row = new JPanel();
		row.setLayout(new GridLayout(0, 2));
		row.add(left); row.add(right);
		return row;
	}
	private class QueryType {
		public String name = null;
		public String text = null;
		public QueryType(String name, String text) {
			this.name = name;
			this.text = text;
		}
		
		@Override
		public String toString() {return text;}
		
	}
	public TriUMQueryGUI(String fixUserId) {
		if(fixUserId == null || fixUserId.length() == 0)
			this.fixUserId = null;
		else
			this.fixUserId = fixUserId;
		
		JPanel mainPane = new JPanel(); mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
		//
		mainPane.add(createRowPanel(new JLabel("ENTER NECESSARY PARAMETERS:"), new JLabel("")));
		//
		protocol = new JComboBox(new String[] {
				"rmi", "soap", "http", "socket"
			});
		protocol.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String protocol = e.getItem().toString().toLowerCase();
				if(protocol.equals("rmi"))
					port.setText(String.valueOf(TriUMQuery.RMI_QUERY_PORT));
				else if(protocol.equals("soap"))
					port.setText(String.valueOf(TriUMQuery.SOAP_QUERY_PORT));
				else if(protocol.equals("http"))
					port.setText(String.valueOf(TriUMQuery.HTTP_SERVICE_PORT));
				else if(protocol.equals("socket"))
					port.setText(String.valueOf(TriUMQuery.COMMUNICATE_SOCKET_SERVICE_PORT));
			}
		});
		mainPane.add(createRowPanel(new JLabel("Protocol:"), protocol));
		//
		host = new JTextField(TEXT_FIELD_COLUMNS); host.setText("localhost");
		mainPane.add(createRowPanel(new JLabel("Host:"), host));
		//
		port = new JTextField(TEXT_FIELD_COLUMNS); port.setText(String.valueOf(TriUMQuery.RMI_QUERY_PORT));
		mainPane.add(createRowPanel(new JLabel("Port:"), port));
		//
		type = new JComboBox(new QueryType[] {
				new QueryType("personalinfo", "Personal Information"), new QueryType("conceptinfo", "Concept Information"),
				new QueryType("studytime", "Study Time"),
				new QueryType("overlay", "Overlay Model"), new QueryType("bayes", "Static Bayesian Model"), new QueryType("dynbayes", "Dynamic Bayesian Model"), 
				new QueryType("learningstyle", "Learning Style"), 
				new QueryType("prerecommend", "Pre-recommended Concepts"), new QueryType("postrecommend", "Post-recommended Concepts"), 
				new QueryType("learningpath", "Learning Path"),
				new QueryType("community", "User Community"),
				new QueryType("interest", "User Interest"),
				new QueryType("userreport", "User Report"),
				new QueryType("feedbackreport", "Feedback Report"),
				new QueryType("totalreport", "Total Report"),
				new QueryType("mlreg", "Register Mailing List"),
				new QueryType("mlunreg", "Unregister Mailing List")
			});
		type.setSelectedIndex(0);
		mainPane.add(createRowPanel(new JLabel("Query type:"), type));
		//
		userid = new JTextField(TEXT_FIELD_COLUMNS);
		if(fixUserId == null)
			userid.setText("guest");
		else {
			userid.setText(fixUserId);
			userid.setEditable(false);
		}
		mainPane.add(createRowPanel(new JLabel("User ID:"), userid));
		//
		JPanel coursePanel = new JPanel(); 
		coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.X_AXIS));
		course = new JComboBox();
		course.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				try {
					String course = e.getItem().toString().toLowerCase();
					TriUMQuery query = TriUMQueryClientUtil.getTriUMQuery(
							TriUMQuery.RMI_DEFAULT_QUERY_HOST, 
							TriUMQuery.RMI_QUERY_PORT);
					ArrayList<String> conceptList = query.listBriefConcepts(course);
					concept.setModel(new DefaultComboBoxModel(conceptList.toArray()));
				}
				catch(RemoteException ex) {
					ex.printStackTrace();
				}
			}
			
		});
		JButton getCourseList = new JButton(new AbstractAction("Get") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				try {
					TriUMQuery query = TriUMQueryClientUtil.getTriUMQuery(
							TriUMQuery.RMI_DEFAULT_QUERY_HOST, 
							TriUMQuery.RMI_QUERY_PORT);
					ArrayList<String> courseList = query.listCourses();
					course.setModel(new DefaultComboBoxModel(courseList.toArray()));
					if(courseList.size() > 0) {
						course.setSelectedIndex(0);
						ArrayList<String> conceptList = query.listBriefConcepts(courseList.get(0));
						concept.setModel(new DefaultComboBoxModel(conceptList.toArray()));
					}
				}
				catch(RemoteException ex) {
					ex.printStackTrace();
				}
			}
		});
		coursePanel.add(course);
		coursePanel.add(getCourseList);
		mainPane.add(createRowPanel(new JLabel("Course:"), coursePanel));
		//
		concept = new JComboBox();
		mainPane.add(createRowPanel(new JLabel("Concept:"), concept));
		//
		JButton query = new JButton(new AbstractAction("QUERY") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				onQueryGUI();
			}
		});
		JButton clear = new JButton(new AbstractAction("CLEAR") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				clearResult();
			}
		});
		mainPane.add(createRowPanel(query, clear));
		//
		knowledge = new JTextField(TEXT_FIELD_COLUMNS);
		knowledge.setAutoscrolls(true); knowledge.setEditable(false);
		mainPane.add(createRowPanel(new JLabel("Knowledge:"), knowledge));
		//
		ls = new JTextField(TEXT_FIELD_COLUMNS);
		ls.setAutoscrolls(true); ls.setEditable(false);
		mainPane.add(createRowPanel(new JLabel("Learning Style:"), ls));
		//
		prerec = new JTextField(TEXT_FIELD_COLUMNS);
		prerec.setAutoscrolls(true); prerec.setEditable(false);
		mainPane.add(createRowPanel(new JLabel("Pre-recommended Concepts:"), prerec));
		//
		postrec = new JTextField(TEXT_FIELD_COLUMNS);
		postrec.setAutoscrolls(true); postrec.setEditable(false);
		mainPane.add(createRowPanel(new JLabel("Post-recommended Concepts:"), postrec));
		//
		lp = new JTextField(TEXT_FIELD_COLUMNS);
		lp.setAutoscrolls(true); lp.setEditable(false);
		mainPane.add(createRowPanel(new JLabel("Learning Path:"), lp));
		//
		community = new JTextField(TEXT_FIELD_COLUMNS);
		community.setMaximumSize(new Dimension(400, 10));
		community.setAutoscrolls(true); community.setEditable(false);
		mainPane.add(createRowPanel(new JLabel("Community:"), community));
		//
		studytime = new JTextField(TEXT_FIELD_COLUMNS);
		studytime.setAutoscrolls(true); studytime.setEditable(false);
		mainPane.add(createRowPanel(new JLabel("Study Time:"), studytime));
		//
		docclass = new JTextField(TEXT_FIELD_COLUMNS);
		docclass.setAutoscrolls(true); docclass.setEditable(false);
		mainPane.add(createRowPanel(new JLabel("Interest/DocClass:"), docclass));
		//
		infoTable = new JTable();
		infoTable.setAutoscrolls(true);
		mainPane.add(createRowPanel(new JLabel("Info:"), new JScrollPane(infoTable)));

		//Msg
		msg = new JTextArea(10, TEXT_FIELD_COLUMNS);
		msg.setLineWrap(true);
		msg.setAutoscrolls(true);
		msg.setEditable(false);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(500);
		splitPane.add(mainPane, JSplitPane.TOP);
		splitPane.add(new JScrollPane(msg), JSplitPane.BOTTOM);
		this.setLayout(new BorderLayout());
		this.add(splitPane, BorderLayout.CENTER);
		this.setSize(500, 650);
	}
	
	public void onQueryGUI() {
		String protocolText = null;
		try {
			protocolText = protocol.getSelectedItem().toString();
			String hostText = host.getText();
			String portText = port.getText();
			String typeText = ((QueryType)type.getSelectedItem()).name;
			String useridText = userid.getText();
			String courseText = null;
			String conceptText = null;
			if(course.getSelectedItem() != null)  courseText = course.getSelectedItem().toString();
			if(concept.getSelectedItem() != null) conceptText = concept.getSelectedItem().toString();
			
			HashMap<String, String> argMap = new HashMap<String, String>();
			argMap.put("protocol", protocolText);
			argMap.put("host", hostText);
			argMap.put("port", portText);
			argMap.put("type", typeText);
			argMap.put("userid", useridText);
			argMap.put("course", courseText);
			argMap.put("concept", conceptText);
			
			String msgText = "Client connects to host \"" + hostText + "\" at port " + portText +
				" through protocol \"" + protocolText + "\" in order to query \"" + typeText + "\"\n";
			msg.append(msgText); msg.setCaretPosition(msg.getDocument().getLength());
			TriUMQueryArg arg = TriUMQueryArg.createQueryArg(argMap);
			showResult(arg.type, TriUMQueryClient.onQuery(argMap));
		}
		catch(Throwable e) {
			try {
				String err = "TriUMQueryRMI.onQuery causes error: " + e.getMessage() + ".";
				if(protocolText.toLowerCase().equals("soap")) {
					err += "\nMaybe the cause is that applet can't access local computer due to security policy.\n";
					err += "This error often raise when using SOAP protocol.\n";
					err += "You should change the security of JRE or sign key to this applet or choose another protocol such as RMI, HTTP, Socket.\n";
					err += "It is ensured that the SOAP protocol works properly if you run the zebra-client as Java application (zebra-client.jar) instead of applet";
				}
				JOptionPane.showMessageDialog(null, err);
				System.out.println(err);
				e.printStackTrace();
				
				msg.append(err);
			}
			catch(Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}
	}
	private void clearResult() {
		knowledge.setText("");
		ls.setText("");
		prerec.setText("");
		postrec.setText("");
		lp.setText("");
		community.setText("");
		studytime.setText("");
		docclass.setText("");
		infoTable.setModel(new DefaultTableModel());
		msg.setText("");
	}
	private void showResult(QUERY_TYPE type, String result) {
		if(result == null || result.length() == 0) {
			JOptionPane.showMessageDialog(null, "There is no information");
			return;
		}
		if(type == QUERY_TYPE.OVERLAY || type == QUERY_TYPE.OVERLAY_BAYESIAN ||
				type == QUERY_TYPE.DYN_OVERLAY_BAYESIAN) {
			knowledge.setText(result);
			knowledge.setCaretPosition(0);
		}
		else if(type == QUERY_TYPE.LEARNING_STYLE) {
			ls.setText(result);
			ls.setCaretPosition(0);
		}
		else if(type == QUERY_TYPE.RECOMMENDED_PRE_CONCEPT) {
			prerec.setText(result);
			prerec.setCaretPosition(0);
		}
		else if(type == QUERY_TYPE.RECOMMENDED_POST_CONCEPT) {
			postrec.setText(result);
			postrec.setCaretPosition(0);
		}
		else if(type == QUERY_TYPE.LEARNING_PATH) {
			lp.setText(result);
			lp.setCaretPosition(0);
		}
		else if(type == QUERY_TYPE.COMMUNITY) {
			community.setText(result);
			community.setCaretPosition(0);
		}
		else if(type == QUERY_TYPE.PERSONAL_INFO || type == QUERY_TYPE.CONCEPT_INFO) {
			String[] items = result.split(",");
			
			String[][] data = new String[items.length][2];
			for(int i = 0; i < items.length; i++) {
				String[] attr = items[i].split("=");
				if(attr.length != 2) continue;
				
				String key = attr[0].trim();
				data[i][0] = key;
				if(key.toLowerCase().equals("password"))
					data[i][1] = "***";
				else
					data[i][1] = attr[1];
			}
			TableModel tableModel = new DefaultTableModel(data, new String[] {"Attribute", "Value"}) {
				private static final long serialVersionUID = 1L;
				
				@Override
			    public boolean isCellEditable(int row, int column) {
			        return false;
			    }
				
			};
			infoTable.setModel(tableModel);
		}
		else if(type == QUERY_TYPE.STUDY_TIME) {
			studytime.setText(result);
		}
		else if(type == QUERY_TYPE.DOCCLASS) {
			docclass.setText(result);
		}
		else if(type == QUERY_TYPE.USERREPORT || type == QUERY_TYPE.FEEDBACKREPORT || type == QUERY_TYPE.TOTALREPORT) {
			new UserReportDlg(null, result);
		}
		else if(type == QUERY_TYPE.MAILINGLIST_REG || type == QUERY_TYPE.MAILINGLIST_UNREG) {
			String str = type == QUERY_TYPE.MAILINGLIST_REG ? "Registering" : "Unregistering";
			if(result.toLowerCase().equals("true"))
				JOptionPane.showMessageDialog(this, str + " Mailing List success!", 
						"Result of " + str + " Mailing List", JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(this, str + " Mailing List fail!", 
						"Result of " + str + " Unregistering Mailing List", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}

class UserReportDlg extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	protected JFrame parent = null;
	protected String htmlReport = null;
	
	public UserReportDlg(JFrame parent, String htmlReport) {
		super(parent, "User Total Report", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.parent = parent;
		this.htmlReport = htmlReport;
		
		try {
			setLayout(new BorderLayout());
			
			HtmlViewer htmlViewer = new HtmlViewer(htmlReport);
			add(new JScrollPane(htmlViewer), BorderLayout.CENTER);
			
			JPanel toolbar = new JPanel();
			JButton save = new JButton("Save Report"); 
			save.setActionCommand("Save Report"); 
			save.addActionListener(this);
			toolbar.add(save);
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
		if(cmd.equals("Save Report")) {
			CommonUtil.showAndSaveReportFile(parent, new File("report.html"), 
				"<html>\n<body>\n" + htmlReport + "\n</body>\n</html>");
		}
	}
	
}

class HtmlViewer extends JEditorPane implements HyperlinkListener {
	private static final long serialVersionUID = 1L;
	
	protected HTMLEditorKit htmlKit = null;
	protected HTMLDocument doc = null;
	
	public HtmlViewer() {
		super();
		this.htmlKit = new HTMLEditorKit();
		this.doc = (HTMLDocument)this.htmlKit.createDefaultDocument();
        this.setEditorKit(htmlKit);
        this.setDocument(doc);
        this.setEditable(false);
        this.addHyperlinkListener(this);
	}
	public HtmlViewer(String htmlData) {
		this();
		setText(htmlData);
        this.setCaretPosition(0);
	}
	public HtmlViewer(URL url) {
		this();
		try {
			setPage(url);
		}
		catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Load Page Error in HtmlViewer: " + e.getMessage(), 
					"HtmlViewer Load Page Error", JOptionPane.ERROR_MESSAGE);
		}
        this.setCaretPosition(0);
	}
	public void addCSSEntry(String entry) {
		//For example: addCSSEntry("body {color:#000; font-family:times; margin: 4px; }")
		htmlKit.getStyleSheet().addRule(entry);
	}
	
	public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (e instanceof HTMLFrameHyperlinkEvent) {
                HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
                HTMLDocument doc = (HTMLDocument)this.getDocument();
                doc.processHTMLFrameHyperlinkEvent(evt);
            }
            else {
                try {
                	URL url = e.getURL();
                	if(url != null) this.setPage(url);
                }
                catch(Throwable t) {
                    t.printStackTrace();
                }
            }
        }
	}
	
}
