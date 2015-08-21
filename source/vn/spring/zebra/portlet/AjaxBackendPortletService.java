package vn.spring.zebra.portlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.helperservice.A_HREF;
import vn.spring.zebra.helperservice.AccessService;
import vn.spring.zebra.util.CommonUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class AjaxBackendPortletService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String PORTLET_NAMESPACE = "vn$spring$zebra$portlet";

	
    protected void doGet(HttpServletRequest request,
    		HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }
    
    protected void doPost(HttpServletRequest request,
    		HttpServletResponse response) throws ServletException, IOException {
		if(!request.getContextPath().equals(ZebraStatic.getWowContextPath())) {
			//ZebraConfig.traceService.trace("Portlet path=\"" + request.getContextPath() + "\"" +
			//	" is not identity with ZebraConfig.getWowContextPath=" + ZebraConfig.getWowContextPath() + "\"");
		}
		String wowPath = request.getScheme() + "://" +
			request.getServerName() + ":" + request.getServerPort() + 
			ZebraStatic.getWowContextPath();
		HttpSession session = request.getSession(true);
		
		String wow_flag = request.getParameter("wow_flag");
		if(wow_flag == null || wow_flag.length() == 0) {
			String userid = (String)session.getAttribute(AccessService.WOW_USERID);
			String course = (String)session.getAttribute(AccessService.WOW_COURSE);
			String directory = (String)session.getAttribute(AccessService.WOW_DIRECTORY);
			String briefConceptName = request.getParameter("wow_concept");
	
			if(userid == null) {
				userid = request.getParameter(AccessService.WOW_USERID);
				if(userid == null) {ZebraStatic.traceService.trace("No User ID"); return;}
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
				doEdit(request, response);
				return;
			}
	
			URL baseURL = new URL(wowPath);
			AccessService access = null;
			try {
				access = new AccessService(baseURL, session.getId(), userid, null, course, directory);
			}
			catch(Exception e) { throw new ServletException(e.getMessage()); }
			
			HashMap<String, String> results = new HashMap<String, String>();
			A_HREF_AjaxURL a_href = new A_HREF_AjaxURL(PORTLET_NAMESPACE + "_loadWow");
			a_href.setFlag("");
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
		}
		else if(wow_flag.toLowerCase().equals("edit")) {
			doEdit(request, response);
		}
    }
    protected void doEdit(HttpServletRequest request,
    		HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
    	
		String userid = (String)session.getAttribute(AccessService.WOW_USERID);
		String course = (String)session.getAttribute(AccessService.WOW_COURSE);
		String directory = (String)session.getAttribute(AccessService.WOW_DIRECTORY);
		String briefConceptName = request.getParameter(AccessService.WOW_CONCEPT);
    	
		userid = (userid == null ? request.getParameter(AccessService.WOW_USERID) : userid);
		course = (course == null ? request.getParameter(AccessService.WOW_COURSE) : course);
		directory = (directory == null ? request.getParameter(AccessService.WOW_DIRECTORY) : directory);
		
		String submit = "\"javascript: " + PORTLET_NAMESPACE + "_editaction()\"";
		String xhtml = 
		"<form name=\"" + PORTLET_NAMESPACE + "_edit\"" +  " id=\"" + PORTLET_NAMESPACE + "_edit\"" + " method=\"POST\" onsubmit=" + submit + ">" + "\n" +
		"<input type=\"hidden\" name=\"" + AccessService.WOW_USERID + "\" id=\"" + AccessService.WOW_USERID + "\" value=\"" + userid + "\" /><br/>" + "\n" +
		"<table border=\"0\">" + "\n" + "\n" +
		
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
		"<td colspan=\"2\"><input type=\"button\" name=\"wow_submit\" id=\"wow_submit\" value=\"Learn\" onclick=" + submit + "/></td>" + "\n" + 
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
	public static String createAjaxCode(String wowPath, 
			String wow_userid, String wow_sessionid, String wow_course, String wow_directory, String wow_concept, String wow_flag) {
		String code = "" +
		
		"<div id=\"" + PORTLET_NAMESPACE + "_main\">" + "\n" +
		CommonUtil.generateIncludeJavaScriptCode(ZebraStatic.JAVASCRIPT_DIR + "ajax/dojo.js") + "\n" +

		"<script type=\"text/javascript\">" + "\n" +
		"function " + PORTLET_NAMESPACE + "_loadWow(wow_userid, wow_sessionid, wow_course, wow_directory, wow_concept, wow_flag) {" + "\n" +
		"   if (wow_concept != null && wow_concept != \"null\") {" + "\n" +
		"       var querystring = new Array();" + "\n" +
		"       if(wow_userid != null && wow_userid != \"null\" && wow_userid != \"\")" + "\n" +
		"           querystring['" + AccessService.WOW_USERID + "'] = wow_userid;" + "\n" +
		"       if(wow_sessionid != null && wow_sessionid != \"null\" && wow_sessionid != \"\")" + "\n" +
		"           querystring['" + AccessService.WOW_SESSIONID + "'] = wow_sessionid;" + "\n" +
		"       if(wow_course != null && wow_course != \"null\" && wow_course != \"\")" + "\n" + 
		"           querystring['" + AccessService.WOW_COURSE + "'] = wow_course;" + "\n" +
		"       if(wow_directory != null && wow_directory != \"null\" && wow_directory != \"\")" + "\n" + 
		"           querystring['" + AccessService.WOW_DIRECTORY + "'] = wow_directory;" + "\n" +
		"       if(wow_concept != null && wow_concept != \"null\" && wow_concept != \"\")" + "\n" + 
		"           querystring['" + AccessService.WOW_CONCEPT + "'] = wow_concept;" + "\n" +
		"       if(wow_flag != null && wow_flag != \"null\" && wow_flag != \"\")" + "\n" + 
		"           querystring['wow_flag'] = wow_flag;" + "\n" +
		"       var bindArgs = {" + "\n" +
		"           url: \"" +  wowPath + "/AjaxBackendPortletService\"," + "\n" +
		"           method: \"POST\"," + "\n" +
		"           content: querystring," + "\n" +
		"           handle: function(type, data, evt) {" + "\n" +
		"               if (type == \"error\") {" + "\n" +
		"                   data = \"<p style='color:red'>\" + data.message + \"</p>\";" + "\n" +
		"               }" + "\n" +
		"               var div = document.getElementById(\"" + PORTLET_NAMESPACE + "_content\");" + "\n" +
		"               //div.innerHTML = \"\";" + "\n" +
		"               div.innerHTML = data;" + "\n" +
		"           }," + "\n" +
		"           mimetype: \"text/html\"" + "\n" +
		"       };" + "\n" +
		"       dojo.io.bind(bindArgs);" + "\n" +
		"   }" + "\n" +
		"};" + "\n" + "\n" +
		
		"function " + PORTLET_NAMESPACE + "_editaction() {" + "\n" +
		"    var f = document.getElementById(\"" + PORTLET_NAMESPACE + "_edit\");" + "\n" +
		"    var userid = f.wow_userid.value;" + "\n" +
		"    var course = f.wow_course.value;" + "\n" +
		"    var directory = f.wow_directory.value;" + "\n" +
		"    var briefConceptName = f.wow_concept.value;" + "\n" +
		        PORTLET_NAMESPACE + "_loadWow(" +
					"userid, '', course, directory, briefConceptName, '');" + "\n" +
		"};" + "\n" + "\n" +

		PORTLET_NAMESPACE + "_loadWow(" +
				"\"" + wow_userid + "\", " +
				"\"" + wow_sessionid + "\", " +
				"\"" + wow_course + "\", " +
				"\"" + wow_directory + "\", " +
				"\"" + wow_concept + "\", " +
				"\"" + wow_flag + "\"" +
			");" + "\n" +

		"</script>" + "\n" +
		"<div id=\"" + PORTLET_NAMESPACE + "_content\"></div>" + "\n" +
		"</div>";
		
		return code;
	}
    
	private class A_HREF_AjaxURL implements A_HREF {
		private String  ajax_func = null;
		private String  wow_userid = null;
		private String  wow_sessionid = null;
		private String  wow_course = null;
		private String  wow_directory = null;
		private String  wow_concept = null;
		private String  wow_flag = null;
		
		public void setFlag(String wow_flag) {
			this.wow_flag = wow_flag;
		}
		public String toString() {
			return "javascript:" + ajax_func + "(" + 
					"'" + wow_userid + "', " + 
					"'" + wow_sessionid + "', " + 
					"'" + wow_course + "', " + 
					"'" + wow_directory + "', " + 
					"'" + wow_concept + "', " + 
					"'" + (wow_flag == null ? "" : wow_flag) + "'" + 
				")";
		}
		public void setParameter(String key, String value) {
			if(key.equals(AccessService.WOW_USERID)) wow_userid = value;
			if(key.equals(AccessService.WOW_SESSIONID)) wow_sessionid = value;
			if(key.equals(AccessService.WOW_COURSE)) wow_course = value;
			if(key.equals(AccessService.WOW_DIRECTORY)) wow_directory = value;
			if(key.equals(AccessService.WOW_CONCEPT)) wow_concept = value;
			if(key.equals("wow_flag")) wow_flag = value;
		}
		public void setSessionAttribute(String key, String value) {}
		
		public A_HREF_AjaxURL(String ajax_func) {this.ajax_func = ajax_func;}
	}
}
