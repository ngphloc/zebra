/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * convertDB.java 1.0, August 30, 2008
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


public class convertDB extends HttpServlet {
	private static final long serialVersionUID = 1L;

  public PrintWriter httpOut=null;

  public convertDB() {

  }

  public void doPost(HttpServletRequest request, HttpServletResponse  response)
      throws ServletException, IOException {
    doGet(request,response);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse  response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(true);
    httpOut = response.getWriter();

    Boolean loggedin=(Boolean)session.getAttribute("loggedin");
    WowConfig conf=(WowConfig)session.getAttribute("config");

    if((loggedin==null) || (!loggedin.booleanValue()) || (conf==null)) response.sendRedirect("Config");
    else {
      response.setContentType("text/html;charset=UTF-8");
      response.addHeader("Cache-Control","no-cache");

      httpOut=response.getWriter();
      httpOut.println("<html>\n<head>\n<title>WOW! Configuration Page</title>\n<link rel=\"stylesheet\" href=\"wow.css\">\n"+
                  "</head>\n<body>");

      String from=(String)request.getParameter("from");
      String to=(String)request.getParameter("to");
      String jdbcUrl=(String)request.getParameter("jdbcUrl");
      String dbuser=(String)request.getParameter("dbuser");
      String dbpassword=(String)request.getParameter("dbpassword");

      if ((from != null) && (to != null)) {
          if (from.equals("db") && to.equals("xml")) {
              try {
                new DB2XML(jdbcUrl, dbuser, dbpassword);
                new ProfileDB2XML(jdbcUrl, dbuser, dbpassword);
              } catch (Exception e) {
                DBError(e);
              }
              httpOut.println("<h2>Database Conversion</h2>\n"+
                "Your Database concept &amp; profile database has been transfered to the XML database.<br>You should restart the server before accessing the course.<br><a href=\"Logout\" title=\"Exit\">Logout</a>");
          } else if (from.equals("xml") && to.equals("db")) {
              try {
                new XML2DB(jdbcUrl, dbuser, dbpassword);
                new ProfileXML2DB(jdbcUrl, dbuser, dbpassword);
              } catch (Exception e) {
                DBError(e);
              }
              httpOut.println("<h2>Database Conversion</h2>\n"+
                "Your XML concept &amp; profile database has been transfered to the database Server.<br>You should restart the server before accessing the course.<br><a href=\"Logout\" title=\"Exit\">Logout</a>");
          } else {
              showInvalid(from,to);
          }
      } else {
          showInvalid(from,to);
      }

    }

  } //end doPost

  public void showInvalid(String from,String to) {

    httpOut.println("<h2>Invalid Request</h2>\n"+
      "Please check if this page was requested by the Wow Configuration Tool\n");
    httpOut.println("<!-- from="+from+" to="+to+" -->");

  }

  public void DBError(Exception e) {
    e.printStackTrace();
    httpOut.println("<p><h2>Error</h2>\n"+
      "An error has been reported transfering databases, please check your database configuration\n<br>");
    httpOut.println("<em>"+e.getMessage()+"</em><br>");

  }



} // end servlet
