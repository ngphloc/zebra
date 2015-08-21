/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AuthorWorkplace.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
import javax.servlet.*;
import javax.servlet.http.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.config.*;
import java.io.*;


public class AuthorWorkplace extends HttpServlet {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
AuthorsConfig aconf;
  WowAuthor wowAuthor=null;
  HttpSession session;

  public AuthorWorkplace() {


  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

   PrintWriter out = response.getWriter();

   session = request.getSession(true);
   response.setContentType("text/html;charset=UTF-8");
   response.addHeader("Cache-Control","no-cache");
   String login=request.getParameter("login");
   Boolean awloggedin=(Boolean)session.getAttribute("awloggedin");
   aconf=new AuthorsConfig();
   if (((awloggedin==null) || awloggedin.equals(Boolean.FALSE)) && (login==null)) {

   out.write("<html>\n"+
   " <head>\n"+
   " <title>WOW! Author Workplace</title>\n"+
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
   "   </center>\n"+
   "   Default author: author, default password: test\n"+
   "  </body>\n"+
   "</html>\n");

   } else {
     String passwd=request.getParameter("password");
     if((login!=null) && (passwd!=null)) {
        wowAuthor=aconf.GetAuthor(login);
        awloggedin=new Boolean((wowAuthor!=null) && wowAuthor.checkPasswd(passwd));
        session.setAttribute("awloggedin",awloggedin);
        session.setAttribute("login", login);
     }
   out.write("<html>\n"+
   " <head>\n"+
   " <title>WOW! Author Workplace</title>\n"+
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
   "   <h2>AuthorWorkplace</h2>\n"+
   "   <br>\n");

   if(awloggedin.booleanValue()) {
	//String cepath = WOWStatic.config.Get("genlisteditorpath").substring(1)+"GenerateListEditor.html";
     out.write(
   " <h3>Authoring tools</h3><br>\n"+
   " <a href=\""+WOWStatic.AMTPATH.substring(1)+"AMt.html"+"\" title=\"Application Management Tool\"><b>Application Manatement Tool</b></a><br>\n"+
   " <a href=\""+WOWStatic.BAYESAUTHORPATH.substring(1)+"BayesAuthor.html"+"\" title=\"Bayesian Network Tool\"><b>Bayesian Network Tool</b></a><br>\n"+
   " <a href=\""+WOWStatic.GENLISTEDITORPATH.substring(1)+"GenerateListEditor.html"+"\" title=\"concept editor\"><b>Concept Editor</b></a><br>\n"+
   " <a href=\""+WOWStatic.GRAPHAUTHORPATH.substring(1)+"GraphAuthor.html"+"\" title=\"graph author\"><b>Graph Author</b></a><br>\n"+
   " <a href=\""+WOWStatic.FORMEDITORPATH.substring(1)+"FormEditor.html"+"\" title=\"form editor\"><b>FormEditor</b></a><br>\n"+
   " <a href=\""+WOWStatic.TESTEDITORPATH.substring(1)+"TestEditor.html"+"\" title=\"test editor\"><b>TestEditor</b></a><br>\n"+
   " <br>\n"+
   " <a href=\"AuthorChange?login="+(String)session.getAttribute("login")+"\" title=\"change author settings\">Change your settings</a><br>\n"+
   " <a href=\"AuthorLogout\" title=\"Exit\">Logout</a><br>\n");
   } else {
    out.write(" <a href=\"AuthorWorkplace\" title=\"Please login first\">login</a>\n");
   }
   out.write("<!-- end menu -->\n"+
   "     </td>\n"+
   "     <td>\n"+
   "<!-- start content -->\n"+
   "   <center>\n");
   if(awloggedin.booleanValue()) {
     out.write("   <table bgcolor=\"#FFFFFF\"><tr><td><img src=\"images/hcmuns.gif\" alt=\"University of Science logo\"/></td></tr></table>\n"+
   "    <br><br>\n"+
   "<font size=3 face=\"helvetica;ariel;sansserif\""+
   "color=\"#00E000\"> ACCESS GRANTED");
   } else {
     out.write("   <table bgcolor=\"#FFFFFF\"><tr><td><img src=\"images/no-hcmuns.gif\" alt=\"No University of Science logo\"/></td></tr></table>\n"+
   "    <br><br>\n"+
   "<font size=3 face=\"helvetica;ariel;sansserif\" color=\"#E00000\"> ACCESS DENIED");
   }


   out.write("   <br></font>\n"+
   "\n"+
   "   </center>\n"+
   "<!-- end of content -->\n"+
   "   </td>\n"+
   "   </tr>\n"+
   "   </table>\n"+
   " </body>\n"+
   "</html>\n");

   }
  }
}
