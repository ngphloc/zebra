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
import vn.spring.WOW.WOWDB.ConceptDB;
import vn.spring.WOW.datacomponents.Concept;
import vn.spring.WOW.datacomponents.DotString;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.client.TriUMQueryArg;
import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;
import vn.spring.zebra.helperservice.WOWContextListener;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ZebraKnowledgeEvalChartView extends AbstractView {

	public String getViewType() {
		return "ZEBRAKNOWLEDGEEVALCHARTVIEW";
	}

	public InputStream genBrsCode(String conceptname, Profile profile,
			Map params) {
		try {
			String contextpath = (String)params.get("wow_path"); //E.g: http://localhost:8080/wow
	        String userid = profile.getAttributeValue("personal", "id");
	        String course = profile.getAttributeValue("personal", "course");
			String briefConceptName = conceptname;
			if(briefConceptName == null) briefConceptName = course;
			else {
				DotString ds = new DotString(briefConceptName);
				if(ds.size() > 1 && ds.get(0).equals(course)) ds.set(0, null);
				briefConceptName = ds.toString();
			}
			
	        QUERY_TYPE evaltype = ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE;
	        TriUMQuery query = (TriUMQuery) WOWContextListener.getInstance().getTriUMServer().
        				getCommunicateService().getQueryDelegator();
			ConceptDB  cdb = WOWStatic.DB().getConceptDB();
			Concept    concept = cdb.getConcept(cdb.findConcept(conceptname));
			String     cTitle = concept.getTitle();

            Document doc = newDocument(profile, title);
            Element  body = (Element)doc.getDocumentElement().getElementsByTagName("body").item(0);
            if(background != null && background.length() > 0) body.setAttribute("background", background);
	        if(!query.doesExist(userid, course, briefConceptName, evaltype, true)) {
	        	System.out.println(briefConceptName + " not exist in network (TestEvalChartView)");
	            String result = serializeXML(doc.getDocumentElement());
	            return new ByteArrayInputStream(result.getBytes());
	        }
			
            Element  h3 = doc.createElement("h3");
            Element  font = doc.createElement("font"); font.setAttribute("color", "#FF0000");
            font.appendChild(doc.createTextNode("Knowledge Evaluation for")); h3.appendChild(font);
            body.appendChild(h3);
            
            font = doc.createElement("font"); font.setAttribute("color", "#0000FF");
            font.appendChild(doc.createTextNode(cTitle)); body.appendChild(font);
            body.appendChild(doc.createElement("br"));
            
            Element applet = doc.createElement("applet");
            body.appendChild(applet);
            
            applet.setAttribute("codebase",  contextpath + "/lib");
            applet.setAttribute("code", "vn.spring.zebra.evaluation.KnowledgeEvalChartApplet.class");
            applet.setAttribute("name", "Evaluation");
            applet.setAttribute("width", "250");
            applet.setAttribute("height", "120");
            applet.setAttribute("hspace", "0");
            applet.setAttribute("vspace", "0");
            applet.setAttribute("align", "middle");
            applet.setAttribute("archive", "wow.jar, jgraph.jar, xerces.jar, xml.jar");
            
            Element param = null;
            
            param = doc.createElement("param");
            param.setAttribute("name", "userid");
            param.setAttribute("value", userid);
            applet.appendChild(param);

            param = doc.createElement("param");
            param.setAttribute("name", "course");
            param.setAttribute("value", course);
            applet.appendChild(param);

            param = doc.createElement("param");
            param.setAttribute("name", "concept");
            param.setAttribute("value", briefConceptName);
            applet.appendChild(param);

            param = doc.createElement("param");
            param.setAttribute("name", "evaltype");
            param.setAttribute("value", TriUMQueryArg.rTranslateKnowledgeQueryType(evaltype));
            applet.appendChild(param);

            String result = serializeXML(doc.getDocumentElement());
            return new ByteArrayInputStream(result.getBytes());
		}
		catch (Exception e) {
			System.out.println("ZebraKnowledgeEvalChartZebraView causes error: " + e.getMessage());
	        return errorStream("[Error generating ZebraKnowledgeEvalChartZebraView]", e.getMessage());
		}
	}


}
