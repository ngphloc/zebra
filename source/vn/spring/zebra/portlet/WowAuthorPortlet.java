package vn.spring.zebra.portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.*;

import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class WowAuthorPortlet extends GenericPortlet {

	public void doView(RenderRequest request,
			RenderResponse response) throws PortletException,
			IOException {
		if(! (request.getWindowState() == WindowState.MINIMIZED) ) { 
			System.out.println("Wow Author portlet in minimize state"); return;
		}

		if(!request.getContextPath().equals(ZebraStatic.getWowContextPath())) {
			//ZebraConfig.traceService.trace.trace("Portlet path=\"" + request.getContextPath() + "\"" +
			//	" is not identity with ZebraConfig.getWowContextPath()=" + ZebraConfig.getWowContextPath() + "\"");
		}
		String portletPath = request.getScheme() + "://" +
			request.getServerName() + ":" + request.getServerPort() + 
			ZebraStatic.getWowContextPath();

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter    out = response.getWriter();
		
		out.println("<h1>WOW! 1.0 Author Toolkit</h1>");
		String wow_author = request.getParameter("wow_author");
		if( wow_author != null && wow_author.equals("toolkit")) {
			out.print(
				"<applet codebase=\"" + portletPath + "/lib\" " +
					"code     = \"vn.spring.WOW.AMt.AMtApplet.class\" " +
					"name     = \"AMtApplet\" " +
					"width    = \"1\" " +
					"height   = \"1\" " +
					"hspace   = \"0\" " +
					"vspace   = \"0\" " +
					"align    = \"middle\" " +
					"archive  = \"AMt.jar, wow.jar, jgraph.jar, xerces.jar, xml.jar\"" +
				"/>"
			);
		}
		
		out.print(
			"<form method=\"POST\" action=\"" + response.createActionURL() + "\">" + 
				"<input type=\"hidden\" name=\"wow_author\" value=\"toolkit\"/>" + 
				"<input type=\"submit\" name=\"submit\" value=\"Author Toolkit\"/>" + 
			"</form>"
		);
	}
	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
		String wow_author = request.getParameter("wow_author");
		response.setRenderParameter("wow_author", wow_author);
	}
}
