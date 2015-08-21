/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Authors.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import vn.spring.WOW.config.*;
import java.util.*;
import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.WOWStatic;


public class Authors extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String author = "";
	private String passwHashed = "";
	private boolean add = false;
	private boolean change = false;

  public Authors() {

  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(true);

    Boolean loggedin=(Boolean)session.getAttribute("loggedin");
    WowConfig conf=(WowConfig)session.getAttribute("config");
	AuthorsConfig aconf= new AuthorsConfig();
    if((loggedin==null) || (!loggedin.booleanValue()) || (conf==null)) response.sendRedirect("Config");

    else {
      response.setContentType("text/html;charset=UTF-8");
      response.addHeader("Cache-Control","no-cache");
      String courseslist = "";
      Hashtable authorHash = aconf.AuthorHash;
      for (Enumeration keys = authorHash.keys();keys.hasMoreElements();) {
          String key = (String)keys.nextElement();
          WowAuthor wowAuthor=aconf.GetAuthor(key);
          Vector authorCL = wowAuthor.getCourseList();
          for (int i=0;i<authorCL.size();i++) {
			courseslist = courseslist + authorCL.elementAt(i) + "|";
		  }
      }
  response.getWriter().write(" <html>\n"+
  " <head>\n"+
  " \n"+
  " <link rel=\"stylesheet\" href=\"wow.css\">\n"+
  " <script language=\"javascript\"> <!--\n" +
  "   function checkpasswd() { \n"+
  "     if(document.authorform.password.value==document.authorform.password2.value) { \n"+
  "       listField = document.authorform.courselist; \n"+
  "       var len = listField.length; \n"+
  "       var totalvalue=\"\";\n"+
  "       for (var i=0; i<len; i++) { \n"+
  "         if (listField.options[i].value!=\"\") \n"+
  "           totalvalue=totalvalue+listField.options[i].value+\"|\"; \n"+
  "       }\n"+
  "       document.authorform.courses.value=totalvalue;\n"+
  "       document.authorform.submit() \n"+
  "     } else { \n"+
  "       alert(\"Passwords are inequal\") \n" +
  "     } \n"+
  "   } \n"+
  "   function addToList() { \n"+
  "     listField = document.authorform.courselist; \n"+
  "     newValue = document.authorform.coursename.value; \n"+
  "     coursesValue = document.authorform.courseslist.value; \n"+
  "     fromJava = coursesValue.split(\"|\");\n"+
  "     if (newValue==\"\") { \n"+
  "       alert(\"You cannot add blank values!\"); \n"+
  "     } else if (newValue.indexOf('|')>-1) { \n"+
  "       alert(\"Course name should not contain '|' sign!\"); \n"+
  "     } else if (IsDigit(newValue.charAt(0))) { \n"+
  "       alert(\"Course name should start with a character!\"); \n"+
  "     } else {\n"+
  "       var exists=0; \n"+
  "       var len = listField.length; \n"+
  "       for (var i=0; i<len; i++) { \n"+
  "         if (listField.options[i].value==newValue && listField.options[i].text==newValue) { \n"+
  "           alert(\"You have already assigned this course for this author!\"); \n"+
  "		      exists=1; \n"+
  "           break; \n"+
  "         } \n"+
  "       } \n"+
  "       if (exists==0) {\n"+
  "         for (var i=0; i<fromJava.length; i++) { \n"+
  "           if (fromJava[i]==newValue) { \n"+
  "             alert(\"One of the authors already has a course with this name! Please, enter another name!\"); \n"+
  "		        exists=1; \n"+
  "   	       break; \n"+
  "           } \n"+
  "         } \n"+
  "       } \n"+
  "       if (exists==0) { \n"+
  "         len = listField.length++; // Increase the size of list and return the size \n"+
  "         listField.options[len].value = newValue; \n"+
  "         listField.options[len].text = newValue; \n"+
  "         listField.selectedIndex = len; // Highlight the one just entered (shows the user that it was entered) \n"+
  "       } // Ends the check to see if the value entered on the form is empty \n"+
  "     } \n"+
  "   } \n"+
  "   function removeFromList() {\n"+
  "     listField = document.authorform.courselist; \n"+
  "     if ( listField.length == -1) { // If the list is empty \n"+
  "       alert(\"There are no values which can be removed!\"); \n"+
  "     } else { \n"+
  "       var selected = listField.selectedIndex; \n"+
  "       if (selected == -1 || listField.value==\"\") { \n"+
  "         alert(\"You must select an entry to be removed!\"); \n"+
  "       } else {  // Build arrays with the text and values to remain \n"+
  "         var replaceTextArray = new Array(listField.length-1); \n"+
  "         var replaceValueArray = new Array(listField.length-1); \n"+
  "         for (var i = 0; i < listField.length; i++) { \n"+
  "			  // Put everything except the selected one into the array \n"+
  "			  if ( i < selected) { replaceTextArray[i] = listField.options[i].text; } \n"+
  "			  if ( i > selected ) { replaceTextArray[i-1] = listField.options[i].text; } \n"+
  "			  if ( i < selected) { replaceValueArray[i] = listField.options[i].value; } \n"+
  "			  if ( i > selected ) { replaceValueArray[i-1] = listField.options[i].value; } \n"+
  "			} \n"+
  "			listField.length = replaceTextArray.length; // Shorten the input list \n"+
  "			for (i = 0; i < replaceTextArray.length; i++) { // Put the array back into the list \n"+
  "			  listField.options[i].value = replaceValueArray[i]; \n"+
  "			  listField.options[i].text = replaceTextArray[i]; \n"+
  "			} \n"+
  "       } // Ends the check to make sure something was selected\n"+
  "	    } // Ends the check for there being none in the list \n"+
  "	  } \n"+
  "   function IsDigit(txtChar){\n"+
  "     var digits = \"0123456789\";\n"+
  "     if (digits.indexOf(txtChar)<0)\n"+
  "			return false;\n"+
  "     return true;\n"+
  "   }\n"+
  " // -->\n" +
  " </script>\n" +
  " </head>\n"+
  " \n"+
  " <body>\n"+
  " <table>\n"+
  " <tr>\n"+
  " <td valign=\"top\">\n"+
  " \n"+
  " <!-- start menu -->\n"+
  " <h2>wow</h2>\n"+
  " <b>The WOW configurator</b>\n"+
  " <br><br>\n"+
  " <a href=\"ConfDB\">Configure DataBase</a><br>\n"+
  " <a href=\"Managers\" title=\"new managers and settings\">Manager Configuration</a><br>\n"+
  " <a href=\"Authors\" title=\"authors and settings\">Authors</a><br>\n"+
  " <a href=\"ConvertCL\" title=\"convert concept list\">Convert concept list from internal format to XML file</a><br>\n"+
  " <a href=\"ConvertXML\" title=\"convert XML file\">Convert concept list from XML file to internal format</a><br>\n"+
  " <a href=\"Logout\" title=\"Exit\">Logout</a><br>\n"+
  " <!-- end menu -->\n"+
  " </td>\n"+
  " <td>\n"+
  " <!-- start content -->\n"+
  " \n");
  WowAuthor victim=null;
  Vector v = new Vector();
  AuthorCourseList authorcourses=null;
  if (request.getParameter("action")==null) {
    WowAuthorList authorlist=new WowAuthorList(aconf.AuthorHash);
    response.getWriter().write(" <form method=POST>\n"+
    " <table>\n"+
    " <tr>\n"+
    " <th>Authors administration</th>\n"+
    " <td>\n"+
    " <select name=\"action\">\n"+
    " <option value=\"\">--choose an action--</option>\n"+
    " <option value=\"add\">Add a new author</option>\n"+
    " <option value=\"change\">Change an existing author</option>\n"+
    " <option value=\"remove\">Remove an existing author</option>\n"+
    " </select>\n"+
    " </td>\n"+
    " <td><input type=\"submit\"></td>\n"+
    " </tr>\n"+
    " <tr>\n"+
    " <th></th>\n"+
    " <td>\n"+
    " <select name=\"alogin\">\n"+
    authorlist.getOptionList()+
    " </select>\n"+
    " </td>\n"+
    " </tr>\n"+
    " </table>\n"+
    " </form>\n");

    } else
    // WARNING!
    // what is about to happen here is not suitable for the
    // poor eyes of little children or conservative programmers
    // Asumed is that no well-thinking human being will alter
    // the hashing function (for the sake of people like me).
    // Possibly I'm mistaken.

    // I would like a switch(String x) statement...
    switch(request.getParameter("action").hashCode()) {
      case -1361636432: //"change"
      	author = request.getParameter("alogin");
      	if (author == null) {
      		response.getWriter().write(" <h2>Warning</h2>Please, select an author to change!\n");
      		break;
      	}
      	else {
			change = true;
        	victim=aconf.GetAuthor(author);
        	passwHashed = victim.getHashed();
		}
      case 96417: //"add"
        add = true;
        if (victim==null) {
          // add
          victim=new WowAuthor("","");
          victim.setUnHashed("");
        }
  authorcourses = new AuthorCourseList(victim);
  response.getWriter().write(" \n"+
  " <html>\n"+
 " <body>\n"+
  " \n"+
  " <form name=\"authorform\" method=POST>\n"+
  " <input type=hidden name=\"action\" value=\"save\">\n"+
  " <table>\n"+
  " <tr><td colspan=2>\n"+
  " <span id=cust style=\"position:relative;\"></span>\n"+
  " </td>\n"+
  " </tr>\n"+
  " <tr>\n"+
  " <th>Author information</th>\n"+
  " <td>\n"+
  " Login:\n"+
  " </td>\n"+
  " <td>\n"+
  " <input name=\"alogin\" size=25 value='"+victim.getLogin()+"'>\n"+
  " </td>\n"+
  " <td><input type=\"button\" value=\"OK\" onclick=\"checkpasswd()\"></td>\n"+
  " </tr>\n");
  if (change) response.getWriter().write(" <br>You can change author's password, but it will remain the same if you do not enter anything.\n");
  response.getWriter().write(" <tr>\n"+
  " <th></th>\n"+
  " <td>\n"+
  " Password:\n"+
  " </td>\n"+
  " <td>\n"+
  " <input type=password name=\"password\" size=25 value=\"\">\n"+
  " </td>\n"+
  " </tr>\n"+
  " <tr>\n"+
  " <th></th>\n"+
  " <td>\n"+
  " Password (verify):\n"+
  " </td>\n"+
  " <td>\n"+
  " <input type=password name=\"password2\" size=25 value=\"\">\n"+
  " </td>\n"+
  " </tr>\n"+
  " <tr>\n"+
  " <th></th>\n"+
  " <td>\n"+
  " Name:\n"+
  " </td>\n"+
  " <td>\n"+
  " <input name=\"name\" size=25 value='"+victim.getName()+"'>\n"+
  " </td>\n"+
  " </tr>\n"+
  " <tr>\n"+
  " <th></th>\n"+
  " <tr>\n"+
  " <th></th>\n"+
  " <td>\n"+
  " <b>List of courses:</b>\n"+
  " </td>\n"+
  " <td>\n"+
  " <select name=\"courselist\">\n"+
  " <option value=\"\">--choose a course--</option>\n"+
  authorcourses.getOptionList()+
  " </select>\n"+
  " </td>\n"+
  " <td><input type=\"button\" value=\"Remove\" onclick=\"removeFromList()\"></td>\n"+
  " </tr>\n"+
  " <tr>"+
  " <th></th>\n"+
  " <th></th>\n"+
  " <td><input name=\"coursename\" size=25 value=\"\"></td>\n"+
  " <td><input type=\"button\" value=\"Add\" onclick=\"addToList()\"></td>\n"+
  " </tr>\n"+
  " <tr>\n"+
  " <td>\n"+
  " <input type=hidden name=\"courses\">\n"+
  " <input type=hidden name=\"courseslist\" value=\""+courseslist+"\">\n"+
  " </td>\n"+
  " </tr>\n"+
  " </table>\n"+
  " </form>\n"+
  " \n"+
  " </body>\n"+
  " </html>\n\n");
        break;
      case 3522941: // "save"
     	if (change) aconf.RemoveAuthor(author);
      	if (add && conf.GetManager(request.getParameter("alogin")) != null && aconf.GetAuthor(request.getParameter("alogin")) != null) {
		    response.getWriter().write(" <h2>Warning</h2>\n"+
		    " You are already registered as an author.\n");
		    break;
		}
      	if (add && aconf.GetAuthor(request.getParameter("alogin")) != null) {
		    response.getWriter().write(" <h2>Warning</h2>\n"+
		    " Author with login <b>"+request.getParameter("alogin")+"</b> already exists.<br>\n"+
		    " Please, return back and try another login.\n");
		    break;
		}
		boolean student=true;
		try {
			ProfileDB pdb = WOWStatic.DB().getProfileDB();
			student = pdb.findProfile(request.getParameter("alogin")) == 0;
		} catch (Exception e) {}
      	if (add && !student) {
		    response.getWriter().write(" <h2>Warning</h2>\n"+
		    " There exists a student with login <b>"+request.getParameter("alogin")+"</b>.<br>\n"+
		    " Please, return back and try another login.\n");
		    break;
		}
		String coursestr= request.getParameter("courses");
		StringTokenizer tokenizer = new StringTokenizer(coursestr, "|");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			v.addElement(token);
		}
        victim=new WowAuthor(request.getParameter("alogin"),
                              request.getParameter("name"));
        if (change && request.getParameter("password").equals("")) victim.setHashed(passwHashed);
        else victim.setUnHashed(request.getParameter("password"));
        victim.setCourseList(v);
        aconf.PutAuthor(victim);
        aconf.StoreConfig();
        if (change) {
        	boolean rename = (new File(WOWStatic.config.Get("WOWROOT")+WOWStatic.AUTHORFILESPATH.substring(1)+author)).renameTo(new File(WOWStatic.config.Get("WOWROOT")+WOWStatic.AUTHORFILESPATH.substring(1)+request.getParameter("alogin")));
			if (!rename) response.getWriter().write("<h2>Warning</h2>\nThe author folder has not been renamed!\n");
		}
        else {
        	boolean create = (new File(WOWStatic.config.Get("WOWROOT")+WOWStatic.AUTHORFILESPATH.substring(1)+request.getParameter("alogin"))).mkdir();
        	if (!create) response.getWriter().write("<h2>Warning</h2>\nThe author folder has not been created!\n");
		}
        // should some exception be caught here??
        response.getWriter().write("<h2>Stored</h2>\n<br>The data has been saved.\n");
        change = false;
        break;
      case -934610812: // "remove"
      author = request.getParameter("alogin");
      //if (author == null)
      if (author == null) {
		  response.getWriter().write(" <h2>Warning</h2>Please, select an author to remove!\n");
	      break;
      }
      else {
		  aconf.RemoveAuthor(request.getParameter("alogin"));
		  aconf.StoreConfig();
		  File directory = new File(WOWStatic.config.Get("WOWROOT")+WOWStatic.AUTHORFILESPATH.substring(1)+request.getParameter("alogin"));
		  File[] listfiles = directory.listFiles();
		  for (int i=0;i<listfiles.length;i++) {
			  listfiles[i].delete();
		  }
		  boolean remove = directory.delete();
		  if (!remove) response.getWriter().write(" <h2>Warning</h2>\n"+
		  " Files created by the author with login <b>"+request.getParameter("alogin")+"</b>\n"+
		  " have not been removed from the Wow System.\n");
		  else response.getWriter().write(" <h2>Removed</h2>\n"+
		  " Author with login <b>"+request.getParameter("alogin")+"</b> has been\n"+
		  " removed from the Wow System, this alteration has been saved.\n");
		  break;
	  }
      default:
    response.getWriter().write(" <h2>Default</h2>\n"+
  " not implemented, possibly hashing error\n"+
  " please use SUN JSDK or JRE v1.4, or\n"+
  " adjust the hashcodes in 'Managers.java'\n");
        break;
    } // end switch("action")
    response.getWriter().write(
   " \n <!-- end of content -->\n"+
   " </td>\n </tr>\n </table>\n"+
   " </body>\n </html>\n");


   } // if loggedin==null || loggedin==false || conf==null

  }
}