/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Config.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
import javax.servlet.*;
import javax.servlet.http.*;

import vn.spring.WOW.config.*;
import java.io.*;


public class Config extends HttpServlet {
	private static final long serialVersionUID = 1L;

  WowConfig conf;
  WowManager man=null;
  HttpSession session;

  public Config() {


  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

   session = request.getSession(true);
   if (session.getAttribute("config")==null) {
     // no configuration file loaded...
     conf=new WowConfig("wowconfig.xml");
     session.setAttribute("config",conf);
   } else {
     conf=(WowConfig)session.getAttribute("config");
   }
   response.setContentType("text/html;charset=UTF-8");
   response.addHeader("Cache-Control","no-cache");

   String login=request.getParameter("login");
   Boolean loggedin=(Boolean)session.getAttribute("loggedin");

   if (((loggedin==null) || loggedin.equals(Boolean.FALSE)) && (login==null)) {


   response.getWriter().write("<html>\n"+
   " <head>\n"+
   " <title>WOW! Configuration Page</title>\n"+
   "\n"+
   "  <!-- implement stylesheet -->\n"+
   " </head>\n"+
   "\n"+
   " <body>\n"+
   "\n"+
   "   <center>\n"+
   "\n"+
   "   <form method=POST>\n"+
   "   <table>\n"+
   "     <tr>\n"+
   "      <th>Login</th><td><input name=\"login\" type=\"text\" size=16></td>\n"+
   "     </tr>\n"+
   "     <tr>\n"+
   "      <th>Password</th><td><input name=\"password\" type=\"password\" size=16></td>\n"+
   "     </tr>\n"+
   "     <tr><td colspan=2 align=\"center\"><input type=submit value=\"Login\"></td></tr>\n"+
   "   </table>\n"+
   "   </form>\n"+
   "   Default administrator: admin, default password: test\n"+
   "   </center>\n"+
   "  </body>\n"+
   "</html>\n");

   } else {

     // you're almost welcome!
     String passwd=request.getParameter("password");

     if((login!=null) && (passwd!=null)) {
       // check login/password
        man=conf.GetManager(login);
        loggedin=new Boolean((man!=null) && man.checkPasswd(passwd));
        session.setAttribute("loggedin",loggedin);
     }



   response.getWriter().write("<html>\n"+
   " <head>\n"+
   " <title>WOW! Configuration Page</title>\n"+
   "\n"+
   "  <!-- implement stylesheet -->\n"+
   " <link rel=\"stylesheet\" href=\"wow.css\">\n"+
   " </head>\n"+
   "\n"+
   " <body>\n"+
   "   <table>\n"+
   "   <tr>\n"+
   "     <td valign=\"top\">\n"+
   "\n"+
   "<!-- start menu -->\n"+
   "   <h2>WowConfig</h2>\n"+
   "   <b>The WOW configurator</b>\n"+
   "   <br><br>\n");
   if(loggedin.booleanValue()) {
     response.getWriter().write(
   "   <a href=\"ConfDB\">Configure DataBase</a><br>\n"+
   "   <a href=\"Managers\" title=\"new managers and settings\">Manager Configuration</a><br>\n"+
   " <a href=\"Authors\" title=\"authors and settings\">Authors</a><br>\n"+
   " <a href=\"ConvertCL\" title=\"convert concept list\">Convert concept list from internal format to XML file</a><br>\n"+
   " <a href=\"ConvertXML\" title=\"convert XML file\">Convert concept list from XML file to internal format</a><br>\n"+
   "   <a href=\"Logout\" title=\"Exit\">Logout</a><br>\n");
   } else {
    response.getWriter().write(" <a href=\"Config\" title=\"Please login first\">login</a>\n");
   }
   response.getWriter().write("<!-- end menu -->\n"+
   "     </td>\n"+
   "     <td>\n"+
   "<!-- start content -->\n"+
   "   <center>\n");
   if(loggedin.booleanValue()) {
     response.getWriter().write("   <table bgcolor=\"#FFFFFF\"><tr><td><img src=\"images/hcmuns.gif\" alt=\"University of Science logo\"/></tr></td></table>\n"+
   "    <br><br>\n"+
   "<font size=3 face=\"helvetica;ariel;sansserif\""+
   "color=\"#00E000\"> ACCESS GRANTED");
   } else {
     response.getWriter().write("   <table bgcolor=\"#FFFFFF\"><tr><td><img src=\"images/no-hcmuns.gif\" alt=\"No University of Science logo\"/></tr></td></table>\n"+
   "    <br><br>\n"+
   "<font size=3 face=\"helvetica;ariel;sansserif\" color=\"#E00000\"> ACCESS DENIED");
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

   } // end (if config!=null)
  }
}
