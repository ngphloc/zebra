/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * CoursesList.java 1.0, August 30, 2008
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
import java.util.*;


public class CoursesList extends HttpServlet {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
AuthorsConfig aconf;

  public CoursesList() {
  }

 public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("text/plain;charset=UTF-8");
    PrintWriter out = response.getWriter();
    out.println("Error: this servlet does not support the GET method!");
    out.close();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
                                throws ServletException, IOException {
	// Read in the message from the servlet
	    aconf=new AuthorsConfig();
	    Hashtable authorHash = aconf.AuthorHash;
	    StringBuffer msgBuf = new StringBuffer();
	    BufferedReader fromApplet = request.getReader();
    	String line;
    	while ((line=fromApplet.readLine())!=null) {
    	  if (msgBuf.length()>0) msgBuf.append('\n');
    	  msgBuf.append(line);
    	}
    	fromApplet.close();
		String login = msgBuf.toString().trim();
		String coursestr="";
   		for (Enumeration keys = authorHash.keys();keys.hasMoreElements();) {
   	    	String key = (String)keys.nextElement();
   	    	if (!key.equals(login)) {
       			WowAuthor wowAuthor=aconf.GetAuthor(key);
       			Vector authorCL = wowAuthor.getCourseList();
       			for (int i=0;i<authorCL.size();i++) {
				   coursestr = coursestr + authorCL.elementAt(i) + "|";
	   			}
	 		}
   		}
	    response.setContentType("text/plain;charset=UTF-8");
	    PrintWriter toApplet = response.getWriter();
	    toApplet.println(coursestr);
	    toApplet.close();

  }

}
