/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AuthorChange.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
import javax.servlet.*;
import javax.servlet.http.*;
import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.config.*;
import java.io.*;
import java.util.*;


public class AuthorChange extends HttpServlet {
	private static final long serialVersionUID = 1L;


  AuthorsConfig aconf;
  WowAuthor author=null;
  HttpSession session;

  public AuthorChange() {

  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

   session = request.getSession(true);
   response.setContentType("text/html;charset=UTF-8");
   response.addHeader("Cache-Control","no-cache");
   String login=request.getParameter("login");
   WowConfig conf=WOWStatic.config;
   aconf=new AuthorsConfig();
   author=aconf.GetAuthor(login);
   String passwHashed = author.getHashed();
   Vector v = new Vector();
   AuthorCourseList authorcourses = null;
   String courseslist = "";
   Hashtable authorHash = aconf.AuthorHash;
   for (Enumeration keys = authorHash.keys();keys.hasMoreElements();) {
       String key = (String)keys.nextElement();
       if (!key.equals(login)) {
       		WowAuthor wowAuthor=aconf.GetAuthor(key);
       		Vector authorCL = wowAuthor.getCourseList();
       		for (int i=0;i<authorCL.size();i++) {
			   courseslist = courseslist + authorCL.elementAt(i) + "|";
	   		}
	 	}
   }
   response.getWriter().write("<html>\n"+
   " <head>\n"+
   " <title>WOW! Author Workplace</title>\n"+
   "\n"+
   "  <!-- implement stylesheet -->\n"+
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
   "       for (var i=0; i<fromJava.length; i++) { \n"+
   "         if (fromJava[i]==newValue) { \n"+
   "           alert(\"One of the authors already has a course with this name! Please, enter another name!\"); \n"+
   "		      exists=1; \n"+
   "   	      break; \n"+
   "         } \n"+
   "       } \n"+
   "       if (exists==0) {\n"+
   "         var len = listField.length; \n"+
   "         for (var i=0; i<len; i++) { \n"+
   "        	  if (listField.options[i].value==newValue && listField.options[i].text==newValue) { \n"+
   "             alert(\"You have already added a course with this name!\"); \n"+
   "		        exists=1; \n"+
   "   		    break; \n"+
   "        	  } \n"+
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
   "\n"+
   " <body>\n"+
   "   <table>\n"+
   "   <tr>\n"+
   "     <td valign=\"top\">\n"+
   "\n"+
   "<!-- start menu -->\n"+
   "   <h2>AuthorWorkplace</h2>\n"+
   "   <br>\n");
   //String cepath = WOWStatic.config.Get("genlisteditorpath").substring(1)+"GenerateListEditor.html";
   response.getWriter().write(
   " <h3>Authoring tools</h3><br>\n"+
   " <a href=\""+WOWStatic.AMTPATH.substring(1)+"AMt.html"+"\" title=\"Application Management Tool\"><b>Application Manatement Tool</b></a><br>\n"+
   " <a href=\""+WOWStatic.GENLISTEDITORPATH.substring(1)+"GenerateListEditor.html"+"\" title=\"concept editor\"><b>Concept Editor</b></a><br>\n"+
   " <a href=\""+WOWStatic.GRAPHAUTHORPATH.substring(1)+"GraphAuthor.html"+"\" title=\"graph author\"><b>Graph Author</b></a><br>\n"+
   " <a href=\""+WOWStatic.FORMEDITORPATH.substring(1)+"FormEditor.html"+"\" title=\"form editor\"><b>FormEditor</b></a><br>\n"+
   " <br>\n"+
   " <a href=\""+WOWStatic.TESTEDITORPATH.substring(1)+"TestEditor.html"+"\" title=\"test editor\"><b>TestEditor</b></a><br>\n"+
   " <a href=\"AuthorChange?login="+(String)session.getAttribute("login")+"\" title=\"change author settings\">Change your settings</a><br>\n"+
   " <a href=\"AuthorLogout\" title=\"Exit\">Logout</a><br>\n");
   response.getWriter().write("<!-- end menu -->\n"+
   "     </td>\n"+
   "     <td>\n"+
   "<!-- start content -->\n"+
   "   <center>\n");
   if (request.getParameter("action")==null) {
	   if (author==null) {
	   response.getWriter().write("   <img src=\"images/no-agu-hcmuns.gif\" alt=\"No AGU and HCMUNS logo\">\n"+
	   "    <br><br>\n"+
	   "<font size=3 face=\"helvetica;ariel;sansserif\" color=\"#E00000\"> ACCESS DENIED");
	} else {
     authorcourses = new AuthorCourseList(author);
     response.getWriter().write(" \n"+
     " <form name=\"authorform\" method=POST>\n"+
     " <input type=hidden name=\"action\" value=\"save\">\n"+
     " <table>\n"+
     " <tr><td colspan=2>\n"+
     " <span id=cust style=\"position:relative;\"></span>\n"+
     " </td>\n"+
     " </tr>\n"+
	 " <br>You can change your password, but it will remain the same if you do not enter anything.\n"+
     " <tr>\n"+
     " <th>Author information</th>\n"+
     " <td>\n"+
     " Login:\n"+
     " </td>\n"+
     " <td>\n"+
     " <input name=\"alogin\" size=25 value='"+author.getLogin()+"'>\n"+
     " </td>\n"+
     " <td><input type=\"button\" value=\"OK\" onclick=\"checkpasswd()\"></td>\n"+
     " </tr>\n"+
     " <tr>\n"+
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
     " <input name=\"name\" size=25 value='"+author.getName()+"'>\n"+
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
     " \n");
	}
   }
   else {
	   boolean student=true;
	   try {
		   ProfileDB pdb = WOWStatic.DB().getProfileDB();
		   student = pdb.findProfile(request.getParameter("alogin")) == 0;
	   } catch (Exception e) {System.out.println(e);}
       if (conf.GetManager(request.getParameter("alogin")) != null) {
		   response.getWriter().write(" <h2>Warning</h2>\n"+
		   " There exists a manager with login "+request.getParameter("alogin")+".<br>\n"+
		   " Please, return back and try another login.\n");
	   }
       else if (!login.equals(request.getParameter("alogin")) && aconf.GetAuthor(request.getParameter("alogin")) != null) {
		   response.getWriter().write(" <h2>Warning</h2>\n"+
		   " Author with login "+request.getParameter("alogin")+" already exists.<br>\n"+
		   " Please, return back and try another login.\n");
	   }
	   else if (!student) {
		   response.getWriter().write(" <h2>Warning</h2>\n"+
		   " There exists a student with login "+request.getParameter("alogin")+".<br>\n"+
		   " Please, return back and try another login.\n");
	   } else {
		   aconf.RemoveAuthor(login);
		   String coursestr= request.getParameter("courses");
		   StringTokenizer tokenizer = new StringTokenizer(coursestr, "|");
		   while (tokenizer.hasMoreTokens()) {
			   String token = tokenizer.nextToken();
			   v.addElement(token);
		   }
		   author=new WowAuthor(request.getParameter("alogin"),
								request.getParameter("name"));
		   if (request.getParameter("password").equals("")) author.setHashed(passwHashed);
		   else author.setUnHashed(request.getParameter("password"));
		   author.setCourseList(v);
		   aconf.PutAuthor(author);
		   aconf.StoreConfig();
		   response.getWriter().write("<h2>Stored</h2>\n<br>The data has been saved.\n");
		   if (!login.equals(request.getParameter("alogin"))) {
			   response.getWriter().write("<br>You must logout first and enter with a new login.\n");
        	   boolean rename = (new File(WOWStatic.config.Get("WOWROOT")+WOWStatic.AUTHORFILESPATH.substring(1)+login)).renameTo(new File(WOWStatic.config.Get("WOWROOT")+WOWStatic.AUTHORFILESPATH.substring(1)+request.getParameter("alogin")));
			   if (!rename) response.getWriter().write("<h2>Warning</h2><br>You author folder has not been renamed.\nPlease, contact the administrator!");
		   }
	   }
   }
   response.getWriter().write("   <br></font>\n"+
   "\n"+
   "   </center>\n"+
   "<!-- end of content -->\n"+
   "   </td>\n"+
   "   </tr>\n"+
   "   </table>\n"+
   " </body>\n"+
   "</html>\n");
  //}
 }
}
