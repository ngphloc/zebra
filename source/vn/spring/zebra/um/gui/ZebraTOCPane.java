/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import vn.spring.WOW.Layout.ZebraTOCView;
import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.LinkAdaptation2;
import vn.spring.zebra.Initialization;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.util.ConceptUtil;
import vn.spring.zebra.util.HtmlViewer;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ZebraTOCPane extends JPanel implements UIDispose {
	private static final long serialVersionUID = 1L;
	
	protected HtmlViewer htmlViewer = null;
	protected String userid = null;
	protected String course = null;
	
	public ZebraTOCPane(String userid, String course) {
		super();
		this.userid = userid;
		this.course = course;
		
		htmlViewer = new HtmlViewer();
		TitledBorder title = BorderFactory.createTitledBorder("Table of Content");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		setBorder(title);
		
		setLayout(new BorderLayout());
		JPanel refresh = new JPanel(); refresh.setLayout(new BorderLayout());
		refresh.add(new JPanel(), BorderLayout.CENTER);
		refresh.add(new JButton(new AbstractAction("Refresh") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				try {
					refresh();
				}
				catch(Exception ex) {ex.printStackTrace();}
			}
		}), BorderLayout.WEST);
		add(refresh, BorderLayout.NORTH);
		add(htmlViewer, BorderLayout.CENTER);
		
		try {
			refresh();
		}
		catch(Exception ex) {ex.printStackTrace();}
	}
	
	private void refresh() throws Exception {
		htmlViewer.setText("");
		
		ProfileDB pdb = ZebraStatic.getProfileDB();
		Profile profile = pdb.getProfile(pdb.findProfile(userid));
		String course = profile.getAttributeValue("personal", "course");
		String directory = profile.getAttributeValue("personal", "directory");
		if(course == null || course.length() == 0 || directory == null || directory.length() == 0)
			return;
		if(!course.equals(this.course)) return;
		
		if(ZebraStatic.getCourseInfoTbl().getCourseInfo(course) == null)
			Initialization.initWOWandZebra(course, directory, false);
		
		ZebraTOCView tocView = new ZebraTOCView2();
		BufferedReader reader = new BufferedReader(new InputStreamReader( 
			tocView.genBrsCode(course + "." + course, profile, new HashMap<Object, Object>())));
		StringBuffer htmlData = new StringBuffer();
		while(true) {
			String line = reader.readLine();
			if(line == null) break;
			htmlData.append(line + "\n");
		}
		htmlViewer.setText(htmlData.toString());
		htmlViewer.setCaretPosition(0);
	}

	public void dispose() {}
}

class ZebraTOCView2 extends ZebraTOCView {
    public String getViewType() {return "ZEBRATOCVIEW2";}

	@Override
	protected Element createRowElement(String conceptname, int width, Document doc,
			Profile profile, boolean bold) {
        Element tr = doc.createElement("tr");
        Element td = doc.createElement("td"); tr.appendChild(td); td.setAttribute("style", "padding-left: "+width+"px;");
        if (bold) {Element b = doc.createElement("b"); td.appendChild(b); td = b;}
        try {
            td.appendChild(LinkAdaptation2.createLink(conceptname, profile, getViewType(), doc, false, false));
            double curknowledge = ConceptUtil.knowledgeQuery(query, userid, course, conceptname);
            td.appendChild(doc.createTextNode(" (" + ((int)(curknowledge * 100.0)) + "% mastered)"));
        } catch (Exception e) {
            e.printStackTrace();
            td.appendChild(doc.createTextNode("ERROR generating!"));
        }
        return tr;
	}
}


