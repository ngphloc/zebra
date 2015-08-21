/**
 * 
 */
package vn.spring.WOW.Layout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.VariableFilter;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class HeaderView extends AbstractView {

	public String getViewType() {
		return "HEADERVIEW";
	}

	public InputStream genBrsCode(String conceptname, Profile profile,
			Map params) {
		try {
	        String course = profile.getAttributeValue("personal", "directory");
	        String directory = profile.getAttributeValue("personal", "directory");
	        String contextpath = WOWStatic.config.Get("CONTEXTPATH");	

	        String username = VariableFilter.deriveVariable(profile, course, VariableFilter.USERNAMESTRING);
			String email = VariableFilter.deriveVariable(profile, course, VariableFilter.EMAILSTRING);

	        Document doc = newDocument(profile, title);
	        Element body = (Element)doc.getDocumentElement().getElementsByTagName("body").item(0);
	        Element table = doc.createElement("table"); body.appendChild(table);
			
	        Element tr = doc.createElement("tr"); table.appendChild(tr);
	        Element td1 = doc.createElement("td"); tr.appendChild(td1);
	        td1.setAttribute("align", "left");
	        Element img1 = doc.createElement("img"); td1.appendChild(img1);
	        img1.setAttribute("href", contextpath + "/" + directory + "/icons/hcmunslogo-tall.gif");
	        
	        Element td2 = doc.createElement("td"); tr.appendChild(td2);
	        td2.setAttribute("align", "right");
	        Element img2 = doc.createElement("img"); td2.appendChild(img2);
	        img2.setAttribute("href", contextpath + "/" + directory + "/icons/wowbanner-small.jpg");
	        
	        body.appendChild(doc.createElement("br"));
	        body.appendChild(doc.createTextNode("Welcome " + username + " (" + email + ") - "));
	        
	        Element toc = doc.createElement("a"); body.appendChild(toc);
	        toc.setAttribute("href", contextpath + "/Get?wndName=TOC");
	        toc.setAttribute("target", "TOC");
	        toc.appendChild(doc.createTextNode("TOC - "));
	        
	        Element glossary = doc.createElement("a"); body.appendChild(glossary);
	        glossary.setAttribute("href", contextpath + "/Get?wndName=Glossary");
	        glossary.setAttribute("target", "TOC");
	        glossary.appendChild(doc.createTextNode("Glossary - "));
	        
	        Element logoff = doc.createElement("a"); body.appendChild(logoff);
	        logoff.setAttribute("href", contextpath + "/Logoff");
	        glossary.setAttribute("target", "_top");
	        logoff.appendChild(doc.createTextNode("Logoff"));
	        body.appendChild(doc.createElement("hr"));
	        
            String result = serializeXML(doc.getDocumentElement());
            return new ByteArrayInputStream(result.getBytes());
		}
		catch (Exception e) {
			System.out.println("Header View->serializeXML causes error: " + e.getMessage());
	        return errorStream("[Error generating Header View]", e.getMessage());
		}
	}

}
