/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/

/**
 * ListFiles.java 1.0, August 30, 2008
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
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.config.*;
import java.io.*;
import java.util.*;


public class ListFiles extends HttpServlet {


   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public Vector courseList = new Vector();

    public void doGet(HttpServletRequest req, HttpServletResponse res)
               throws ServletException, IOException {

      String wowroot = (String) vn.spring.WOW.WOWStatic.config.Get("WOWROOT");
      String graphauthorfiles = (String) WOWStatic.AUTHORFILESPATH;

      res.setContentType("text/plain;charset=UTF-8");
      PrintWriter out = res.getWriter();
      // extention
      final String extention = req.getParameter("extention").trim();
      final String userName = req.getParameter("userName").trim();


      String dirname = "";

      if (userName != null) {
		  // substring(1) added by Natlia Stash, 18-07-2008
		  //dirname = wowroot + graphauthorfiles + "\\" + userName;
		  dirname = wowroot + graphauthorfiles.substring(1) + userName;
  }
  else {
	   //dirname = wowroot + graphauthorfiles;
	   dirname = wowroot + graphauthorfiles.substring(1);
  }

      this.checkAuthor(userName);


        try {



           File file_obj = new File(dirname);

                            FileFilter extfilter = new FileFilter() {
                            public boolean accept(File f) {
                                // filter the file extention
                                String fileName = f.getName().toLowerCase();
                                return fileName.endsWith(extention);
                            }
                            };


                            File[] filenames = file_obj.listFiles(extfilter);

                            for (int i = 0; i< filenames.length;) {
                          //  System.out.println("== file: " + filenames[i]);
                            Filename fname = new Filename(filenames[i].toString(),File.separatorChar,'.');
							String tempFile = fname.filename().replaceAll(extention,"");

							if (courseList.contains(tempFile)) {
                            out.println(fname.filename()+ "." + fname.extension());

							}



                            i++;
                            }

            }


         catch (Exception e) {
            System.out.println("Path not found! : " + dirname + " " + e);
        }
               }
   public void checkAuthor(String authorname) {

   AuthorsConfig aconfig = new AuthorsConfig();
            WowAuthor author =  aconfig.GetAuthor(authorname);

            for (Enumeration i =  author.getCourseList().elements(); i.hasMoreElements();) {
             String course = (String) i.nextElement();
             courseList.add(course);
   }


   }


 public class Filename {
     private String fullPath;
     private char pathSeparator, extensionSeparator;

     public Filename(String str, char sep, char ext) {
         fullPath = str;
         pathSeparator = sep;
         extensionSeparator = ext;
     }

     public String extension() {
         int dot = fullPath.lastIndexOf(extensionSeparator);
         return fullPath.substring(dot + 1);
     }

     public String filename() {
         int dot = fullPath.lastIndexOf(extensionSeparator);
         int sep = fullPath.lastIndexOf(pathSeparator);
         return fullPath.substring(sep + 1, dot);
     }

     public String path() {
         int sep = fullPath.lastIndexOf(pathSeparator);
         return fullPath.substring(0, sep);
     }
 }


}