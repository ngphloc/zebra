package vn.spring.zebra.helperservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.zebra.ZebraStatic;


/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class WOWServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		//Example: http://localhost:8080/wow/Get/tutorial/?concept=tutorial.welcome
		//request.getScheme(): http
		//request.getServerName(): localhost
		//request.getServerPort(): 8080
		//request.getContextPath(): /wow
		//request.getServletPath(): /Get
		//request.getPathInfo(): /tutorial/
		//request.getPathTranslated(): W:\wow\tutorial
		//request.getQueryString(): concept=tutorial.welcome
		//request.getRequestURI(): /wow/Get/tutorial/
		//request.getRequestURL(): http://localhost:8080/wow/Get/tutorial/
		
		String wowPath = request.getScheme() + "://" +
			request.getServerName() + ":" + request.getServerPort() + 
			request.getContextPath();
		HttpSession session = request.getSession(true);
	
		String wow_flag = request.getParameter("wow_flag");
		if(wow_flag == null || wow_flag.length() == 0) {
			String userid = (String)session.getAttribute(AccessService.WOW_USERID);
			String course = (String)session.getAttribute(AccessService.WOW_COURSE);
			String directory = (String)session.getAttribute(AccessService.WOW_DIRECTORY);
			String briefConceptName = request.getParameter(AccessService.WOW_CONCEPT);
	
			if(userid == null) {
				userid = request.getParameter(AccessService.WOW_USERID);
				if(userid == null) {ZebraStatic.traceService.trace("No User ID"); doLogin(request, response); return;}
				String password = request.getParameter(AccessService.WOW_PASSWORD);
				if(password == null || password.length() == 0) {
					doLogin(request, response); return;
				}
				
				password = password.trim();
		        ProfileDB pdb = ZebraStatic.getProfileDB();
		        try {
			        if(!pdb.getProfile(pdb.findProfile(userid)).
			        	getAttributeValue("personal", "password").equals(password)) {
			        	ZebraStatic.traceService.trace("Wrong login!");
			        	doLogin(request, response);
			        	return;
			        }
		        }
		        catch(Exception e) {
		        	ZebraStatic.traceService.trace("Wrong login! caused by " + e.getMessage());
		        	e.printStackTrace();
		        	doLogin(request, response);
		        	return;
		        }
				session.setAttribute(AccessService.WOW_USERID, userid);
			}
			if(course == null) {
				course = request.getParameter(AccessService.WOW_COURSE);
				session.setAttribute(AccessService.WOW_COURSE, course);
			}
			if(directory == null) {
				directory = request.getParameter(AccessService.WOW_DIRECTORY);
				session.setAttribute(AccessService.WOW_DIRECTORY, directory);
			}
			
			if(course == null || directory == null || briefConceptName == null) {
				doLogin(request, response);
				return;
			}
	
			URL baseURL = new URL(wowPath);
			AccessService access = null;
			try {
				access = new AccessService(baseURL, session.getId(), userid, null, course, directory);
			}
			catch(Exception e) { throw new ServletException(e.getMessage()); }
			
			HashMap<String, String> results = new HashMap<String, String>();
			A_HREF_ServletURL a_href = new A_HREF_ServletURL(request.getRequestURL().toString());
			access.setA_HREF(a_href);
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
			out.close();
		}
		else if(wow_flag.toLowerCase().equals("login")) {
			doLogin(request, response);
		}
	}

    protected void doLogin(HttpServletRequest request,
    		HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
    	
		String userid = (String)session.getAttribute(AccessService.WOW_USERID);
		String course = (String)session.getAttribute(AccessService.WOW_COURSE);
		String directory = (String)session.getAttribute(AccessService.WOW_DIRECTORY);
		String briefConceptName = request.getParameter(AccessService.WOW_CONCEPT);
    	
		userid = (userid == null ? request.getParameter(AccessService.WOW_USERID) : userid);
		course = (course == null ? request.getParameter(AccessService.WOW_COURSE) : course);
		directory = (directory == null ? request.getParameter(AccessService.WOW_DIRECTORY) : directory);
		
		String xhtml = 
		"<form name=\"wow_form\"" +  " id=\"wow_form\"" + " method=\"POST\" action=\"WOWServlet\">" + "\n" +
		"<table border=\"0\">" + "\n" + "\n" +
		
		"<td>User ID: </td><td><input type=\"text\" name=\"" + AccessService.WOW_USERID + "\" id=\"" + AccessService.WOW_USERID + "\" value=\"" + "\n" + 
		(userid == null ? "" : userid) + "\" /></td>" + "\n" + 
		"</tr>" + "\n" + "\n" +

		"<td>Password: </td><td><input type=\"password\" name=\"" + AccessService.WOW_PASSWORD + "\" id=\"" + AccessService.WOW_PASSWORD + "\" value=\"\"" + "\n" +
		"</tr>" + "\n" + "\n" +

		"<tr>" + "\n" + 
		"<td>Course: </td><td><input type=\"text\" name=\"" + AccessService.WOW_COURSE + "\" id=\"" + AccessService.WOW_COURSE + "\" value=\"" + "\n" + 
			(course == null ? "" : course) + "\" /></td>" + "\n" + 
		"</tr>" + "\n" + "\n" +
		
		"<tr>" + "\n" + 
		"<td>Directory: </td><td><input type=\"text\" name=\"" + AccessService.WOW_DIRECTORY + "\" id=\"" + AccessService.WOW_DIRECTORY + "\" value=\"" + "\n" + 
			(directory == null ? "" : directory) + "\" /></td>" + "\n" + 
		"</tr>" + "\n" + "\n" +  

		"<tr>" + "\n" + 
		"<td>Concept: </td><td><input type=\"text\" name=\"" + AccessService.WOW_CONCEPT + "\" id=\"" + AccessService.WOW_CONCEPT + "\" value=\"" + "\n" + 
			(briefConceptName == null ? "" : briefConceptName) + "\" /></td>" + "\n" + 
		"</tr>" + "\n" + "\n" +
		
		"<tr>" + "\n" + 
		"<td colspan=\"2\"><input type=\"submit\" name=\"wow_submit\" id=\"wow_submit\" value=\"Login\" /></td>" + "\n" + 
		"</tr>" + "\n" + "\n" +

		"</table>" + "\n" +
		"</form>";
		
		session.setAttribute(AccessService.WOW_USERID, null);
		session.setAttribute(AccessService.WOW_COURSE, null);
		session.setAttribute(AccessService.WOW_DIRECTORY, null);
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(xhtml);
		
    }
	private class A_HREF_ServletURL implements A_HREF {
		private String servletURL = null;
		private String  wow_userid = null;
		private String  wow_course = null;
		private String  wow_directory = null;
		private String  wow_concept = null;
		
		public String toString() {
			return servletURL + "?" + 
				AccessService.WOW_USERID + "=" + wow_userid + "&" +
				AccessService.WOW_COURSE + "=" + wow_course + "&" +
				AccessService.WOW_DIRECTORY + "=" + wow_directory + "&" +
				AccessService.WOW_CONCEPT + "=" + wow_concept;
		}
		public void setParameter(String key, String value) {
			if(key.equals(AccessService.WOW_USERID)) wow_userid = value;
			if(key.equals(AccessService.WOW_COURSE)) wow_course = value;
			if(key.equals(AccessService.WOW_DIRECTORY)) wow_directory = value;
			if(key.equals(AccessService.WOW_CONCEPT)) wow_concept = value;
		}
		public void setSessionAttribute(String key, String value) {}
		
		public A_HREF_ServletURL(String servletURL) {this.servletURL = servletURL;}
	}
	
}
