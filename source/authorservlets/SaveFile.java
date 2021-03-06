/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * SaveFile.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package authorservlets;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Vector;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.config.*;


public class SaveFile extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String filename;

    /**
     * Initializes the servlet
     *
     * @param config the <code>ServletConfig</code>
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * Overrides the <code>service</code> from <code>HttpServlet</code>
     *
     * @param request the <code>HttpServletRequest</code>
     * @param response the <code>HttpServletResponse</code>
     * @throws IOException generated by <code>doPost</code>
     * @throws ServletException when something goes wrong with servlet
     */
    public void service(HttpServletRequest request,
                        HttpServletResponse response) throws IOException,
                                                             ServletException {
        HttpSession session = request.getSession(true);

        if (session != null) {
            synchronized (session.getId()) {
                try {
                    doPost(request, response);
                } catch (ServletException se) {
                    System.out.println("ServletException");
                } catch (IOException ie) {
                    System.out.println("IOException");
                }
            }
        }
    }

    /**
     * Overrides the <code>doPost</code> from <code>HttpServlet</code>
     *
     * @param request the <code>HttpServletRequest</code>
     * @param response the <code>HttpServletResponse</code>
     * @throws IOException when something goes wrong with writing to file
     * @throws ServletException when something goes wrong with servlet
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
                throws java.io.IOException, ServletException {


      final String userName = request.getParameter("userName").trim();


      String xmlFile = URLDecoder.decode(request.getParameter("xmlFile"),"UTF-8");
	          int startIndex = xmlFile.indexOf("[") + 1;
	          int endIndex = xmlFile.indexOf("]");
	          String fileName = xmlFile.substring(startIndex, endIndex);


	          int startData = xmlFile.indexOf("\n") + 1;
	          String xmlData = xmlFile.substring(startData);


	          try {

	          String wowroot = (String) vn.spring.WOW.WOWStatic.config.Get("WOWROOT");
	          //changed by Natalia Stash, 15-07-2008
	          //String graphauthorfiles = (String) vn.spring.WOW.WOWStatic.config.Get("graphauthorfiles");
	          String graphauthorfiles = (String) WOWStatic.AUTHORFILESPATH;

		        String filen ="";

	        if (userName != null) {
	        //changed by Natalia Stash, 15-07-2008
	        //filen = wowroot + graphauthorfiles + userName + "\\" + fileName;
	        filen = wowroot + graphauthorfiles.substring(1) + userName + "/" + fileName;
		}
		else {
			     filen = wowroot + graphauthorfiles + fileName;
			}


	              FileWriter out = new FileWriter(filen);

	              out.write(xmlData);
	              out.close();
	          } catch (Exception e) {
	              System.out.println("Exception while trying to save!");
	          }
	   // export to WOW!
	          if (fileName.trim().equals("savetowow.wow"));
	if ((fileName.indexOf(".wow") > 0) || (fileName.indexOf(".frm") > 0) ){

	      this.setAuthor(userName, fileName);
	  }

    }

    public void setAuthor(String authorname, String course) {


		   AuthorsConfig aconfig = new AuthorsConfig();
	       WowAuthor author =  aconfig.GetAuthor(authorname);
			Vector courses = author.getCourseList();

			course = course.replaceAll(".wow","");
			course = course.replaceAll(".frm","");


			if (!courses.contains(course))
			 {
			   courses.add(course);
			   author.setCourseList(courses);

	   }
	   aconfig.StoreConfig();

	}

}