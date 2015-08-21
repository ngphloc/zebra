/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ExportFile.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package authorservlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import vn.spring.WOW.util.*;
import vn.spring.WOW.WOWStatic;

public class ExportFile extends HttpServlet {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
                    throws IOException, ServletException
  {
     response.setContentType("text/html;charset=UTF-8");
     PrintWriter out = response.getWriter();
     String filename = request.getParameter("fileName");
     String author = request.getParameter("author");
     boolean created = false;
     try {
       String graphauthorfiles = (String) WOWStatic.AUTHORFILESPATH;
       String wowroot = (String) WOWStatic.config.Get("WOWROOT");
       String fileUrl = wowroot + graphauthorfiles.substring(1) + author + "/" + filename +".wow";
       created = (new ConceptList()).getConceptList(fileUrl, filename);
     } catch (Exception e) {
       System.out.println("Error while exportingen file: " + filename);
     }
     out.println(created);
  }
}