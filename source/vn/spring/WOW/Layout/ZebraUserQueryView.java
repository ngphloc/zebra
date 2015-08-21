/**
 * 
 */
package vn.spring.WOW.Layout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import vn.spring.WOW.datacomponents.Profile;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ZebraUserQueryView extends AbstractView {
	public String getViewType() {
		return "ZEBRAUSERQUERYVIEW";
	}

	public InputStream genBrsCode(String conceptname, Profile profile,
			Map params) {
		try {
			String wow_path = (String)params.get("wow_path"); //E.g: http://localhost:8080/wow
			
	        String userid = profile.getAttributeValue("personal", "id");
	        String username = profile.getAttributeValue("personal", "name");
	        if(username == null || username.trim().length() == 0) username = userid;
	        
            Document doc = newDocument(profile, title);
            Element  body = (Element)doc.getDocumentElement().getElementsByTagName("body").item(0);
            if(background != null && background.length() > 0) body.setAttribute("background", background);
            Element  h3 = doc.createElement("h3");
            Element  font = doc.createElement("font"); font.setAttribute("color", "#FF0000");
            font.appendChild(doc.createTextNode("User Query View (" + username + ")"));
            h3.appendChild(font);
            body.appendChild(h3);
            
            String axisJars = 
            	"axis.jar, jaxrpc.jar, " + 
            	"commons-discovery-0.2.jar, commons-logging-1.0.4.jar, " + 
            	"wsdl4j-1.5.1.jar, activation.jar, mail_1_4_3.jar";
            Element applet = doc.createElement("applet");
            body.appendChild(applet);
            applet.setAttribute("codebase",  wow_path + "/lib");
            applet.setAttribute("code", "vn.spring.zebra.client.TriUMQueryApplet.class");
            applet.setAttribute("name", "UserQuery");
            applet.setAttribute("width", "500");
            applet.setAttribute("height", "650");
            applet.setAttribute("hspace", "0");
            applet.setAttribute("vspace", "0");
            applet.setAttribute("align", "middle");
            applet.setAttribute("archive", "zebra-client.jar, " + axisJars);
            
            Element param = doc.createElement("param");
            param.setAttribute("name", "userid");
            param.setAttribute("value", userid);
            applet.appendChild(param);
            
            String result = serializeXML(doc.getDocumentElement());
            return new ByteArrayInputStream(result.getBytes());
		}
		catch (Exception e) {
			System.out.println("ZebraUserQueryView causes error: " + e.getMessage());
	        return errorStream("[Error generating ZebraCollaborativeView]", e.getMessage());
		}
	}
	
}
