/**
 * 
 */
package vn.spring.zebra.portlet;

import javax.portlet.*;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.helperservice.A_HREF;
import vn.spring.zebra.helperservice.AccessService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class WowPortlet extends GenericPortlet {
	private String jspPortletCourse = null;

	public void init(PortletConfig config) throws PortletException {
		super.init(config);
		//jspPortletCourse = pConfig.getInitParameter("portlet-course");
	}

	public void doView(RenderRequest request,
			RenderResponse response) throws PortletException,
			IOException {
		if(!request.getContextPath().equals(ZebraStatic.getWowContextPath())) {
			//ZebraConfig.traceService.trace("Portlet path=\"" + request.getContextPath() + "\"" +
			//	" is not identity with ZebraConfig.getWowContextPath()=" + ZebraConfig.getWowContextPath() + "\"");
		}
		String wowPath = request.getScheme() + "://" +
			request.getServerName() + ":" + request.getServerPort() + 
			ZebraStatic.getWowContextPath();
		String userid = request.getRemoteUser(); if(userid == null) ZebraStatic.traceService.trace("No user logined for WOW portlet!");

		PortletSession session = request.getPortletSession(true);
		String course = (String)session.getAttribute(AccessService.WOW_COURSE);
		String directory = (String)session.getAttribute(AccessService.WOW_DIRECTORY);
		String briefConceptName = request.getParameter(AccessService.WOW_CONCEPT);
		
		if(course == null || directory == null || briefConceptName == null) {
			doEdit(request, response);
			return;
		}

		AccessService access = (AccessService) (session.getAttribute(AccessService.class.getName()));
		if(access == null) {
			URL baseURL = new URL(wowPath);
			try {
				access = new AccessService(baseURL, session.getId(), userid, null, course, directory);
			}
			catch(Exception e) { throw new PortletException(e.getMessage()); }
			session.setAttribute(AccessService.class.getName(), access);
		}
		
		HashMap<String, String> results = new HashMap<String, String>();
		access.setA_HREF(new A_HREF_PortletURL(response.createRenderURL()));
		try {
			access.accessGetConcept(briefConceptName, results);
		}
		catch(Exception e) {
			ZebraStatic.traceService.trace("doView: " + e.getMessage());
			e.printStackTrace();
		}
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter    out = response.getWriter();
		out.print(AccessService.createPage(wowPath, results));
	}
	
	public void doEdit(RenderRequest request,
			RenderResponse response) throws PortletException,
			IOException {
		PortletSession session = request.getPortletSession(true);
		session.setAttribute(AccessService.WOW_COURSE, null);
		session.setAttribute(AccessService.WOW_DIRECTORY, null);
		session.setAttribute(AccessService.WOW_CONCEPT, null);
		session.setAttribute(AccessService.class.getName(), null);

		PortletContext context = getPortletContext();
		PortletRequestDispatcher rd = context.getRequestDispatcher(jspPortletCourse);
		try {
			rd.include(request, response);
			return;
		}
		catch(Exception ex) {
			ZebraStatic.traceService.trace(ex.getMessage() + " caused by no jspPortletCourse path");
		}

		String course = request.getParameter(AccessService.WOW_COURSE);
		String directory = request.getParameter(AccessService.WOW_DIRECTORY);
		String briefConceptName = request.getParameter(AccessService.WOW_CONCEPT);
		PortletPreferences prefs = request.getPreferences();
		course = (course == null ? prefs.getValue(AccessService.WOW_COURSE, null) : course);
		directory = (directory == null ? prefs.getValue(AccessService.WOW_DIRECTORY, null) : directory);
		briefConceptName = (briefConceptName == null ? prefs.getValue(AccessService.WOW_CONCEPT, null) : briefConceptName);
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		PortletURL actionURL = response.createActionURL();
		//actionURL.setPortletMode(PortletMode.VIEW);
		//actionURL.setWindowState(request.getWindowState());
		String xhtml = "<form name=\"register\" id=\"register\" method=\"POST\" action=\"" + actionURL.toString() + "\">" +
			"Course: " + 
			"<input type=\"text\" name=\"" + AccessService.WOW_COURSE + "\" id=\"" + AccessService.WOW_COURSE + "\" value=\"" + 
				(course == null ? "" : course) + "\" /><br/>" + 
			"Directory: " + 
			"<input type=\"text\" name=\"" + AccessService.WOW_DIRECTORY + "\" id=\"" + AccessService.WOW_DIRECTORY + "\" value=\"" + 
				(directory == null ? "" : directory) + "\" /><br/>" + 
			"Concept: " + 
			"<input type=\"text\" name=\"" + AccessService.WOW_CONCEPT + "\" id=\"" + AccessService.WOW_CONCEPT + "\" value=\"" + 
				(briefConceptName == null ? "" : briefConceptName) + "\" /><br/>" + 
			"<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Learn\" />" + 
		"</form>";
		out.print(xhtml);
	}

	public void doHelp(RenderRequest request,
			RenderResponse response) throws PortletException,
			IOException {
	}

	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException
	{
		//PortletMode mode = request.getPortletMode(); 
		//if(mode == PortletMode.EDIT) {}
		//else if(mode == PortletMode.HELP) {}
		//else if(mode == PortletMode.VIEW) {}
		
		String course = request.getParameter("wow_course");
		String directory = request.getParameter("wow_directory");
		String briefConceptName = request.getParameter("wow_concept");
		
		PortletSession session = request.getPortletSession(true);
		session.setAttribute("wow_course", course);
		session.setAttribute("wow_directory", directory);
		response.setRenderParameter("wow_concept", briefConceptName);
		response.setPortletMode(PortletMode.VIEW);
		//response.setWindowState(WindowState.MAXIMIZED);
	}
	
	private class A_HREF_PortletURL implements A_HREF {
		private PortletURL url = null;
		public String toString() {return url.toString();}
		public void setParameter(String key, String value) {
			url.setParameter(key, value);
		}
		public void setSessionAttribute(String key, String value) {}
		
		public A_HREF_PortletURL(PortletURL url) {this.url = url;}
	}
	
	public static void main(String[] args) {
	}
	
}
