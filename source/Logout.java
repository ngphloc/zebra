/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Logout.java 1.0, August 30, 2008
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

public class Logout extends HttpServlet {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
HttpSession session;

  public Logout() {


  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  // if we don't kill the session it will haunt us
  // within WOW
  HttpSession mysession=request.getSession();

  response.setContentType("text/html;charset=UTF-8");
  response.addHeader("Cache-Control","no-cache");

  WowConfig conf=(WowConfig) mysession.getAttribute("config");
  if(conf!=null) { conf.StoreConfig();
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
   "   <img src=\"images/no-hcmuns.gif\" alt=\"No AGU HCMUNS logo\">\n"+
   "    <br><br>\n"+
   "   <font size=+2 face=\"helvetica;ariel;sansserif\" color=\"#0000ff\">\n"+
   "     <b>LOGGED OFF</b>\n"+
   "   </font>\n"+
   "\n"+
   "   </center>\n"+
   "   <div align=\"left\"><a href=\"Config\">Login</a></div>\n"+
   "   <div align=\"right\"><a href=\"index.html\">Go back to index page</a></div>\n"+
   "  </body>\n"+
   "</html>\n");
  } else {
    response.getWriter().write("<html><body><h2>Session exited</h2>\n"+
              "Session has expired, cannot store your configuration.\n"+
              "Sorry, but you have to configure this system again (without\n"+
              "hesitations).<br><a href=\"Config\">Login</a></body></html>");
  }
  request.getSession().invalidate();

  }

}
