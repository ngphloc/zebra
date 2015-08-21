/**
 * 
 */
package vn.spring.zebra.helperservice;

import java.io.*;


import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.JOptionPane;


import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.mining.gsp.GSP;
import vn.spring.zebra.mining.gsp.SequenceDatabase;


/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TestServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		
		try {
			doPost(request, response);
		}
		catch(Exception e) {
			ZebraStatic.traceService.trace(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	    PrintWriter out=response.getWriter();

		try {
			HttpSession session = request.getSession(true);
			session.getAttribute("attr");
			request.getParameter("para");

			response.setContentType("text/plain;charset=UTF-8");
			response.addHeader("Cache-Control","no-cache");
			
			serviceTest(out);
			
//			AccessService login = new AccessService(new URL("http://localhost:8080/wow"), request.getSession(true).getId(), "ng_phloc", "maiphuong", "tutorial", "tutorial");
//			HashMap<String, String> results = new HashMap<String, String>();
//			login.accessGetConcept("welcome", results);
//			out.println(results.get(AccessService.MAINVIEW));
//			
//			out.println("<body><html>");
//			out.println("<br/>TestServlet!<br/>");
//			out.println("</body></html>");
		}
		catch (Exception e) {
			ZebraStatic.traceService.trace(e.getMessage());
			e.printStackTrace();
		}
		

	}


	static void serviceTest(PrintWriter out) throws Exception {
		//new TriUMServerUI(WOWContextListener.getInstance().getTriUMServer());
		out.println("Sequential pattern mining: Learning Path");
        SequenceDatabase db= SequenceDatabase.autoGenerate();
        GSP gsp = new GSP();
        gsp.setParameters(0.7);
        gsp.perform(db);
        out.println(gsp.toString());
	}
	static void mainTest(PrintWriter out) throws Exception {
		//new OverlayBayesInspector(new URL("http://localhost:8080/wow"), OverlayBayesUM.BAYESNET_TYPE.JAVABAYES);
	}
	public static void main(String[] args) {
		try {
			mainTest(new PrintWriter(new OutputStreamWriter(System.out)));
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			ZebraStatic.traceService.trace(e.getMessage());
			e.printStackTrace();
		}
	}

}
