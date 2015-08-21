/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ConvertCL.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
 /*
  * Servlet that generates the interface to change from the internal format to the xml format.
 */

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import vn.spring.WOW.util.*;
import vn.spring.WOW.*;

public class ConvertCL extends HttpServlet {
  private static final long serialVersionUID = 1L;


  public ConvertCL() {


  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  response.setContentType("text/html;charset=UTF-8");
  response.getWriter().write(" <html>\n"+
      " <head>\n"+
      " <title>WOW! Configuration Page</title>\n"+
      " \n"+
      " <link rel=\"stylesheet\" href=\"wow.css\">\n"+
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
      " <a href=\"ConvertXML\" title=\"convert XML file\">Create concept list from XML file to internal format</a><br>\n"+
      " <a href=\"Logout\" title=\"Exit\">Logout</a><br>\n"+
      " <!-- end menu -->\n"+
      " </td>\n"+
      " <td>\n"+
      " \n");
  if (request.getParameter("coursename")==null) {
      response.setContentType("text/html;charset=UTF-8");
      response.getWriter().write(
          " <form method=\"POST\" action=\"ConvertCL\">\n"+
          " <h2>Concept List Conversion</h2><table><tr>\n"+
          " <tr>\n<td>Enter the authorname: </td>\n"+
          " <td><input type=\"text\" name=\"authorname\" size=30></td>\n"+
          " <td><input type=\"submit\" name=\"convertsubmit\" value=\"OK\"></td></tr>\n"+
          " <tr>\n<td>and the coursename to convert: </td>\n"+
          " <td><input type=\"text\" name=\"coursename\" size=30></td>\n</tr>\n"+
          "</tr></table>\n"+
          " </form>\n");
  } else {
      try {
          String author = request.getParameter("authorname");
          String concept = request.getParameter("coursename");
          String fileName = WOWStatic.config.Get("WOWROOT") + WOWStatic.AUTHORFILESPATH.substring(1) + author + "/" + concept + ".wow";
          boolean created = (new ConceptXML()).createConceptList(fileName, concept);
          if (created) response.getWriter().write(" <h2>Concept List</h2>\nThe concept list in internal format has been <b>succesfully converted</b> to an XML file.\n");
              else response.getWriter().write(" <h2>Warning!</h2>\nThere was an <b>error</b> in converting the concept list from internal format to an XML file.\n");
      } catch(Exception e) {
          System.out.println(e);
          response.getWriter().write(" <h2>Warning!</h2>\nThere was an <b>exception</b> in converting the concept list from internal format to an XML file.\n");
      }
  }
  response.getWriter().write(" </tr>\n</table>\n</body>\n</html>");
}
}