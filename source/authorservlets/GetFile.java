/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * GetFile.java 1.0, August 30, 2008
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
import vn.spring.WOW.WOWStatic;



public class GetFile extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
               throws ServletException, IOException {

      String wowroot = (String) WOWStatic.config.Get("WOWROOT");
      String graphauthorpath = (String) WOWStatic.GRAPHAUTHORPATH;
      String graphauthorfiles = (String) WOWStatic.AUTHORFILESPATH;
      String contextPath = (String) WOWStatic.config.Get("CONTEXTPATH");
	  String graphauthorwebpath= contextPath + graphauthorpath;


      res.setContentType("text/plain;charset=UTF-8");
      PrintWriter out = res.getWriter();
      String filename = req.getParameter("fileName");
      String username = req.getParameter("userName");
      String outstring = null;
      String filen ="";
      if (username != null) {
	   // substring(1) added by Natalia Stash, 18-07-2008
     //filen = wowroot + graphauthorfiles + username + "\\" + filename;
     filen = wowroot + graphauthorfiles.substring(1) + username + "/" + filename;
  }
  else {
	   //filen = wowroot + graphauthorfiles + filename;
	   filen = wowroot + graphauthorfiles.substring(1) + filename;
  }
      String dirname = graphauthorwebpath;



        try {
            FileReader fr = new FileReader(filen);
            BufferedReader in = new BufferedReader(fr);
            // the path to the dtd should be in wowconfig!
            while ((outstring = in.readLine()) != null) {
             outstring = outstring.replaceFirst("concept_relation_type.dtd","http://"
             +  req.getServerName()+ ":" + req.getServerPort() + dirname + "/dtd/concept_relation_type.dtd");
             outstring = outstring.replaceFirst("wow_relation_type.dtd","http://"
             +  req.getServerName()+ ":" + req.getServerPort() + dirname + "/dtd/wow_relation_type.dtd");
             outstring = outstring.replaceFirst("author_relation_type.dtd","http://"
             +  req.getServerName()+ ":" + req.getServerPort() + dirname + "/dtd/author_relation_type.dtd");
             outstring = outstring.replaceFirst("attribute.dtd","http://"
             +  req.getServerName()+ ":" + req.getServerPort() + dirname + "/dtd/attribute.dtd");
             outstring = outstring.replaceFirst("template.dtd","http://"
             +  req.getServerName()+ ":" + req.getServerPort() + dirname + "/dtd/template.dtd");
             outstring = outstring.replaceFirst("generatelist4.dtd","http://"
             +  req.getServerName()+ ":" + req.getServerPort() + dirname + "/dtd/generatelist4.dtd");
             out.println(outstring);
            }

            in.close();
        } catch (IOException e) {
            System.out.println("File not found! The getfile servlet couldn`t find: " + filen);
        }

    }

}