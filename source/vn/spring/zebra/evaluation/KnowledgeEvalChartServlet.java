/**
 * 
 */
package vn.spring.zebra.evaluation;

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import vn.spring.WOW.datacomponents.DotString;
import vn.spring.zebra.client.TriUMQueryArg;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class KnowledgeEvalChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String userid = req.getParameter("userid").trim();
			String course = req.getParameter("course").trim();
			String briefConceptName = req.getParameter("concept");
			if(briefConceptName == null) briefConceptName = course;
			else {
				DotString ds = new DotString(briefConceptName);
				if(ds.size() > 1 && ds.get(0).equals(course)) ds.set(0, null);
				briefConceptName = ds.toString();
			}
			String evaltype = req.getParameter("evaltype").trim();
			
			int width = Integer.valueOf(req.getParameter("width").trim()).intValue();
			int height = Integer.valueOf(req.getParameter("height").trim()).intValue();
			JFreeChart chart = TestEvalChartUtil.getEvalChartOfKnowledge(userid, course, briefConceptName, TriUMQueryArg.translateKnowledgeQueryType(evaltype));
			byte[] chartBytes = ChartUtilities.encodeAsPNG(chart.createBufferedImage(width, height));
			ObjectOutputStream out = new ObjectOutputStream(resp.getOutputStream());
			out.writeObject(chartBytes);
			out.flush(); out.close();
		}
		catch(Exception e) {
			System.out.println("KnowledgeEvalChartServlet causes error: " + e.getMessage());
			throw new ServletException(e.getMessage());
		}
	}

}
