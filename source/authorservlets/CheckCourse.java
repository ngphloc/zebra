/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * CheckCourse.java 1.0, August 30, 2008
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
import vn.spring.WOW.config.*;
import java.util.*;



public class CheckCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest req, HttpServletResponse res)
               throws ServletException, IOException {

         res.setContentType("text/plain;charset=UTF-8");
      PrintWriter out = res.getWriter();
      String username = req.getParameter("userName");
      String coursename = req.getParameter("courseName");
      if (coursename.endsWith(".gaf")) coursename = coursename.substring(0, coursename.indexOf(".gaf"));

      boolean courseExists = false;


         AuthorsConfig aconfig = new AuthorsConfig();


         for (Iterator i = aconfig.AuthorHash.entrySet().iterator(); i.hasNext();) {
                    Map.Entry m = (Map.Entry) i.next();
                    WowAuthor author = (WowAuthor) m.getValue();
					//changed by Natalia Stash, 21-07-2008
                   //if (!author.getName().equals(username)) {
				   if (!author.getLogin().equals(username)) {
                                   Vector courses = author.getCourseList();
                                   if (courses.contains(coursename)) {

                                           courseExists = true;
                                   }

                           }




}
// added by Natalia Stash, 15-07-2008
// when an author creates a new course it is added to authorlistfile.xml

		  if (!courseExists) {
			  WowAuthor wowAuthor = aconfig.GetAuthor(username);
			  Vector v = wowAuthor.getCourseList();
			  if (v.indexOf(coursename)==-1) {
				  v.addElement(coursename);
				  wowAuthor.setCourseList(v);
				  aconfig.PutAuthor(wowAuthor);
				  aconfig.StoreConfig();
			  }
		  }
            out.println(courseExists);



}
}
