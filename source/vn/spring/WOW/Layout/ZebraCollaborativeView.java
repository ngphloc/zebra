package vn.spring.WOW.Layout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import vn.spring.WOW.datacomponents.Profile;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.helperservice.WOWContextListener;
import vn.spring.zebra.util.CommonUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ZebraCollaborativeView extends AbstractView {
	protected String wowUrl = null; //E.g: http://localhost:8080/wow
	
	public String getViewType() {
		return "ZEBRACOLLABORATIVEVIEW";
	}
	
	public InputStream genBrsCode(String conceptname, Profile profile,
			Map params) {
		try {
			String wow_path = (String)params.get("wow_path"); //E.g: http://localhost:8080/wow
			
			TriUMQuery triUMQuery = (TriUMQuery) (WOWContextListener.getInstance().getTriUMServer().getCommunicateService().getQueryDelegator());
	        String userid = profile.getAttributeValue("personal", "id");
	        String course = profile.getAttributeValue("personal", "course");
            ArrayList<String> userids = triUMQuery.communityQuery(userid, course);
	        
            Document doc = newDocument(profile, title);
            Element  body = (Element)doc.getDocumentElement().getElementsByTagName("body").item(0);
            if(background != null && background.length() > 0) body.setAttribute("background", background);
            Element  h3 = doc.createElement("h3");
            Element  font = doc.createElement("font"); font.setAttribute("color", "#FF0000");
            font.appendChild(doc.createTextNode("Collaborative Area"));
            h3.appendChild(font);
            body.appendChild(h3);
            
            body.appendChild(doc.createTextNode("Your community: "));
			for(int i = 0; i < userids.size(); i++) {
				String id = userids.get(i);
				Element a = doc.createElement("a");
				a.setAttribute("href", wow_path + "/Interact?userid=" + id);
				a.setAttribute("target", "_blank");
				a.appendChild(doc.createTextNode(id));
				body.appendChild(a);
				if(i < userids.size() - 1) body.appendChild(doc.createTextNode(", "));
			}
			body.appendChild(doc.createElement("br"));
            
            Element applet = doc.createElement("applet");
            body.appendChild(applet);
            applet.setAttribute("codebase",  wow_path + "/lib");
            applet.setAttribute("code", "vn.spring.zebra.collab.CollabApplet.class");
            applet.setAttribute("name", "Collaborative");
            applet.setAttribute("width", "400");
            applet.setAttribute("height", "400");
            applet.setAttribute("hspace", "0");
            applet.setAttribute("vspace", "0");
            applet.setAttribute("align", "middle");
            applet.setAttribute("archive", "wow.jar, jgraph.jar, xerces.jar, xml.jar");
            
            Element param = null;
            URL url = new URL(wow_path);
            param = doc.createElement("param");
            param.setAttribute("name", "host");
            param.setAttribute("value", url.getHost());
            applet.appendChild(param);
            
            param = doc.createElement("param");
            param.setAttribute("name", "port");
            param.setAttribute("value", "" + TriUMQuery.COLLAB_SERVICE_PORT);
            applet.appendChild(param);

            param = doc.createElement("param");
            param.setAttribute("name", "userid");
            param.setAttribute("value", userid);
            applet.appendChild(param);

            param = doc.createElement("param");
            param.setAttribute("name", "group");
            param.setAttribute("value", CommonUtil.concatNames(userids, ","));
            applet.appendChild(param);
            
            String result = serializeXML(doc.getDocumentElement());
            return new ByteArrayInputStream(result.getBytes());
		}
		catch (Exception e) {
			System.out.println("ZebraCollaborativeView causes error: " + e.getMessage());
	        return errorStream("[Error generating ZebraCollaborativeView]", e.getMessage());
		}
	}


}
