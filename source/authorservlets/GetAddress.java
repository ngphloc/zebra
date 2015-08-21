/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/* GetAddress.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package authorservlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import vn.spring.WOW.WOWStatic;

public class GetAddress extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String path;

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("text/plain;charset=UTF-8");
    PrintWriter out = response.getWriter();
    out.println("Error: this servlet does not support the GET method!");
    out.close();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
                                throws ServletException, IOException {
	// Read in the message from the servlet
	    StringBuffer msgBuf = new StringBuffer();
	    BufferedReader fromApplet = request.getReader();
    	String line;
    	while ((line=fromApplet.readLine())!=null) {
    	  if (msgBuf.length()>0) msgBuf.append('\n');
    	  msgBuf.append(line);
    	}
    	if (msgBuf.toString().endsWith("FormEditor/")) {
    		String contextpath = (String) vn.spring.WOW.WOWStatic.config.Get("CONTEXTPATH");
    		String xmlroot = (String) vn.spring.WOW.WOWStatic.config.Get("XMLROOT");
    		int index = xmlroot.lastIndexOf(contextpath)+contextpath.length();
			path = xmlroot.substring(index)+(String) WOWStatic.FORMPATH;
		}
    	else path = (String) WOWStatic.AUTHORFILESPATH;
    	fromApplet.close();

    response.setContentType("text/plain;charset=UTF-8");

    PrintWriter toApplet = response.getWriter();
    toApplet.println(path);
    toApplet.close();

  }

}