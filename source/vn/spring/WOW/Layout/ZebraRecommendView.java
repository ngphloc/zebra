/**
 * 
 */
package vn.spring.WOW.Layout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import vn.spring.WOW.datacomponents.DotString;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.LinkAdaptation2;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.helperservice.WOWContextListener;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ZebraRecommendView extends AbstractView {
	protected String wowUrl = null;
	
	public String getViewType() {
		return "ZEBRARECOMMENDVIEW";
	}
	
	public InputStream genBrsCode(String conceptname, Profile profile,
			Map params) {
		//Construct HTML code for the menu output file
		try {
			TriUMQuery triUMQuery = (TriUMQuery) (WOWContextListener.getInstance().getTriUMServer().getCommunicateService().getQueryDelegator());
	        String userid = profile.getAttributeValue("personal", "id");
	        String course = profile.getAttributeValue("personal", "course");
	        DotString ds = new DotString(conceptname);
	        if(ds.get(0).equals(course)) ds.set(0, null);
	        String briefConceptName = ds.toString();
	        	
            Document doc = newDocument(profile, title);
            Element  body = (Element)doc.getDocumentElement().getElementsByTagName("body").item(0);
            if(background != null && background.length() > 0) body.setAttribute("background", background);
            Element  h3 = null, font = null, div = null;
            
            //Learning Path
	    	ArrayList<ArrayList<String>> path = triUMQuery.learningPathQuery(userid, course);
	    	div = doc.createElement("div");   body.appendChild(div);
    		h3 = doc.createElement("h3");     div.appendChild(h3);
            font = doc.createElement("font"); font.setAttribute("color", "#FF0000");
            font.appendChild(doc.createTextNode("Learning Path"));
            h3.appendChild(font);
			for(int i = path.size() - 1; i >= 0; i--) {
				ArrayList<String> concepts = path.get(i);
				
				if(concepts.size() > 1) div.appendChild(doc.createTextNode("("));
				for(int j = 0; j < concepts.size(); j++) {
					div.appendChild(LinkAdaptation2.createLink(concepts.get(j), profile, getViewType(), doc, false, false));
					if(j < concepts.size() - 1) div.appendChild(doc.createTextNode(", "));
				}
				if(concepts.size() > 1) div.appendChild(doc.createTextNode(")"));
				
				if(i > 0) div.appendChild(doc.createTextNode(" => "));
			}
            
            //Recommended concepts
			ArrayList<String> preConcepts = triUMQuery.recommendedPreConceptQuery(userid, course, briefConceptName);
    		ArrayList<String> postConcepts = triUMQuery.recommendedPostConceptQuery(userid, course, briefConceptName);;
	    	div = doc.createElement("div");   body.appendChild(div);
    		h3 = doc.createElement("h3");     div.appendChild(h3);
            font = doc.createElement("font"); font.setAttribute("color", "#FF0000");
            font.appendChild(doc.createTextNode("Recommended Concepts"));
            h3.appendChild(font);
            div.appendChild(createRecommendConcept(preConcepts, doc, profile, true));
            div.appendChild(createRecommendConcept(postConcepts, doc, profile, false));
            
            String result = serializeXML(doc.getDocumentElement());
            return new ByteArrayInputStream(result.getBytes());
		}
		catch (Exception e) {
			System.out.println("ZebraRecommendeView causes error: " + e.getMessage());
	        return errorStream("[Error generating ZebraRecommendeView]", e.getMessage());
		}
	}
	
	protected Element createRecommendConcept(ArrayList<String> rConcepts, Document doc, Profile profile, boolean isPreRecommend) {
        Element  p = doc.createElement("p");
        Element  strong = doc.createElement("strong"); p.appendChild(strong);
        Element  font = doc.createElement("font"); font.setAttribute("color", "#000066"); strong.appendChild(font);
        font.appendChild(doc.createTextNode("Should be read " + (isPreRecommend ? "before:" : "after:")));
        for(int i = 0; i < rConcepts.size(); i++) {
        	font.appendChild(doc.createElement("br"));
        	Element a = LinkAdaptation2.createLink(rConcepts.get(i), profile, getViewType(), doc, true, false);
			for(int j = 0; j < ZebraStatic.RECOMMEND_MAX_CONCEPT - i; j++) {
				//Element img = doc.createElement("img");
				//img.setAttribute("src", "icons/recommend.jpg");
				//a.appendChild(img);
			}
        	font.appendChild(a);
        }
        return p;
	}
}
