/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Logoff.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

import javax.servlet.http.*;

import vn.spring.WOW.WOWStatic;

import java.io.*;

public class Logoff extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest request, HttpServletResponse response) {
        //Loc Nguyen added October 11 2009

		HttpSession session  = request.getSession();
		String directory = (String)session.getAttribute("directory");
        WOWStatic.logoff(session);
		
        PrintWriter out = null;
        try {
            out = new PrintWriter(response.getWriter());
        } catch (IOException e) {throw new IllegalStateException("unable to obtain PrintWriter.");}
        response.setContentType("text/html;charset=UTF-8");
        out.println("<h1>Logoff successfull</h1>");
        out.println("<div align=\"right\"><a href=\"" + directory + 
        	"\" target=\"_top\">Go back to the login page.</a></div>\n");
        out.close();
    }
	
}
