/*


    This file is part of WOW! (Adaptive Hypermedia for All)

    WOW! is free software


*/
/**
 * GetStrategyFile.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Technology.
 */

package authorservlets;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;



public class GetStrategyFile extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse res)
               throws ServletException, IOException {

      String wowroot = (String) vn.spring.WOW.WOWStatic.config.Get("WOWROOT");
      String graphauthorfiles = (String) vn.spring.WOW.WOWStatic.config.Get("graphauthorfiles");
      String contextPath = (String) vn.spring.WOW.WOWStatic.config.Get("CONTEXTPATH");


      res.setContentType("text/plain;charset=UTF-8");
      PrintWriter out = res.getWriter();
      String filename = req.getParameter("fileName");
      String username = req.getParameter("userName");
      String outstring = null;
      String filen ="";
      String dtdpath = "";
      if (username != null) {
     filen = wowroot + graphauthorfiles.substring(1) + username + "/" + filename + ".xml";
     dtdpath = "../";
  }
  else {
	  //standard strategies
	   filen = wowroot + graphauthorfiles.substring(1) + filename + ".xml";
  }
        try {
            FileReader fr = new FileReader(filen);
            BufferedReader in = new BufferedReader(fr);
            // the path to the dtd should be in wowconfig!
            //?set system id??

            while ((outstring = in.readLine()) != null) {
             if (username != null)
             outstring = outstring.replaceFirst(dtdpath + "strategy.dtd","http://"
             +  req.getServerName()+ ":" + req.getServerPort() + contextPath + graphauthorfiles + "strategy.dtd");
             out.println(outstring);
            }

            in.close();
        } catch (IOException e) {
            System.out.println("File not found! The getfile servlet couldn`t find: " + filen);
        }

    }

}