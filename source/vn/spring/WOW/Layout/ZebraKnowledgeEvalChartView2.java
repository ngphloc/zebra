/**
 * 
 */
package vn.spring.WOW.Layout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.ConceptDB;
import vn.spring.WOW.datacomponents.Concept;
import vn.spring.WOW.datacomponents.DotString;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;
import vn.spring.zebra.helperservice.WOWContextListener;
import vn.spring.zebra.util.CommonUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ZebraKnowledgeEvalChartView2 extends AbstractView {

	public String getViewType() {
		return "ZEBRAKNOWLEDGEEVALCHARTVIEW2";
	}

	public InputStream genBrsCode(String conceptname, Profile profile, Map params) {
		try {
			String html = "<html><body>";
			
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
	        if(!query.doesExist(userid, course, briefConceptName, evaltype, true)) {
	        	System.out.println(briefConceptName + " not exist in network (TestEvalChartView)");
	        	html += "</body></html>";
	            return new ByteArrayInputStream(html.getBytes());
	        }
	        
        	html += "<h3><font color=\"#FF0000\">Knowledge Evaluation for</font></h3>";
        	html += "<font color=\"#0000FF\">" + cTitle + "</font><br/>";
        	html += generateEvalChartJavaScript(query.knowledgeQuery(userid, course, briefConceptName, evaltype, true));
        	html += "</body></html>";
            return new ByteArrayInputStream(html.getBytes());
		}
		catch (Exception e) {
			System.out.println("ZebraKnowledgeEvalChartZebraView2 causes error: " + e.getMessage());
	        return errorStream("[Error generating ZebraKnowledgeEvalChartZebraView2]", e.getMessage());
		}
	}
	
	private String generateEvalChartJavaScript(double knowledge) {
		String baseDir = ZebraStatic.JAVASCRIPT_DIR;
		String code = "";
		code += "<div><canvas id=\"evalgraph\" height=\"150\" width=\"150\"></canvas></div>" + "\n";
		
		//code += CommonUtil.generateIncludeJavaScriptCode(baseDir + "mochikit/MochiKit.js") + "\n";
		code += CommonUtil.generateIncludeJavaScriptCode(baseDir + "mochikit/Base.js") + "\n";
		code += CommonUtil.generateIncludeJavaScriptCode(baseDir + "mochikit/Color.js") + "\n";
		code += CommonUtil.generateIncludeJavaScriptCode(baseDir + "mochikit/DOM.js") + "\n";
		code += CommonUtil.generateIncludeJavaScriptCode(baseDir + "mochikit/Format.js") + "\n";
		code += CommonUtil.generateIncludeJavaScriptCode(baseDir + "plotkit/Base.js") + "\n";
		code += CommonUtil.generateIncludeJavaScriptCode(baseDir + "plotkit/Layout.js") + "\n";
		code += CommonUtil.generateIncludeJavaScriptCode(baseDir + "plotkit/Canvas.js") + "\n";
		code += CommonUtil.generateIncludeJavaScriptCode(baseDir + "plotkit/SweetCanvas.js") + "\n";

		code += "<script language=\"javascript\">" + "\n";
		code +=
			"var options = {" + "\n" +
			//"         \"colorScheme\": PlotKit.Base.palette(PlotKit.Base.baseColors()[0])," + "\n" +
			"		  \"padding\": {left: 2, right: 2, top: 2, bottom: 2}," + "\n" +
			"		  \"xTicks\": [{v:0, label:\"mastered\"}, {v:1, label:\"not mastered\"}]," + "\n" +
			"		   \"drawYAxis\": false" + "\n" +
			"     };" + "\n";
		code +=
			"function drawGraph() {" + "\n" +
			"   var layout = new PlotKit.Layout(\"pie\", {});alert(MochiKit.DOM);" + "\n" +
			"   layout.addDataset(\"sqrt\", [[0," + knowledge + "], [1," + (1- knowledge)+ "]]);" + "\n" +
			"   layout.evaluate();" + "\n" +
			"   var canvas = MochiKit.DOM.getElement(\"evalgraph\");" + "\n" +
			"   var plotter = new PlotKit.SweetCanvasRenderer(canvas, layout, options);" + "\n" +
			"   plotter.render();" + "\n" +
			"}" + "\n";
		code += "MochiKit.DOM.addLoadEvent(drawGraph);" + "\n";
		code += "</script>";

		return code;
	}

}
