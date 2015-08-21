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
public class FooterView extends AbstractView {

	public String getViewType() {
		return "FOOTERVIEW";
	}

	public InputStream genBrsCode(String conceptname, Profile profile,
			Map params) {

		try {
	        String course = profile.getAttributeValue("personal", "directory");
	        String contextpath = WOWStatic.config.Get("CONTEXTPATH");	

	        String numberdone = VariableFilter.deriveVariable(profile, course, VariableFilter.NUMBERDONESTRING);
	        String numbertodo = VariableFilter.deriveVariable(profile, course, VariableFilter.NUMBERTODOSTRING);
	        String title = VariableFilter.deriveVariable(profile, course, VariableFilter.TITLESTRING);

	        Document doc = newDocument(profile, title);
	        Element body = (Element)doc.getDocumentElement().getElementsByTagName("body").item(0);
	        
	        body.appendChild(doc.createElement("hr"));
	        body.appendChild(doc.createTextNode(
	        		"You have read " + numberdone + " and still have " + numbertodo + " to read - "));
	        
	        Element readedlist = doc.createElement("a"); body.appendChild(readedlist);
	        readedlist.setAttribute("href", contextpath + "/ViewGet/" + course + "/?handler=Done");
	        readedlist.appendChild(doc.createTextNode("list of read pages - "));

	        Element readinglist = doc.createElement("a"); body.appendChild(readinglist);
	        readinglist.setAttribute("href", contextpath + "/ViewGet/" + course + "/?handler=Todo");
	        readinglist.appendChild(doc.createTextNode("pages still to be read"));

	        body.appendChild(doc.createElement("br"));
	        body.appendChild(doc.createTextNode("Changeable settings: "));

	        Element colorconfig = doc.createElement("a"); body.appendChild(colorconfig);
	        colorconfig.setAttribute("href", contextpath + "/ViewGet/" + course + "/?handler=ColorConfig");
	        colorconfig.appendChild(doc.createTextNode("link colors - "));

	        Element knowledgeconfig = doc.createElement("a"); body.appendChild(knowledgeconfig);
	        knowledgeconfig.setAttribute("href", contextpath + "/ViewGet/" + course + "/?handler=KnowledgeConfig");
	        knowledgeconfig.appendChild(doc.createTextNode("knowledge of " + title + " - "));

	        Element changepassword = doc.createElement("a"); body.appendChild(changepassword);
	        changepassword.setAttribute("href", contextpath + "/ViewGet/" + course + "/?handler=PasswordConfig");
	        changepassword.appendChild(doc.createTextNode("Change Password"));

	        body.appendChild(doc.createElement("br"));
	        body.appendChild(doc.createTextNode("WOW is the adpative learning system based on open AHA! (Paul De Bra - Eindhoven University of Technology)"));

            String result = serializeXML(doc.getDocumentElement());
            return new ByteArrayInputStream(result.getBytes());
		}
		catch (Exception e) {
			System.out.println("Header View->serializeXML causes error: " + e.getMessage());
	        return errorStream("[Error generating Header View]", e.getMessage());
		}
	}


}
