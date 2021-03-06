/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/* SaveList.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.*;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.config.*;
import vn.spring.zebra.um.OverlayBayesUM;

import java.util.Vector;

public class SaveList extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String filename;
    private String authorname;
    public  String created;

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
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        if (session!=null) synchronized(session.getId()) {
            filename = request.getHeader("Filename");
            authorname = request.getHeader("Author");
            created = request.getHeader("Created");
            try {
                doPost(request, response);
            } catch (ServletException se) {
                System.out.println(se);
            } catch (IOException ie) {
                System.out.println(ie);
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {

        try {
        	//Loc Nguyen review
        	String database = request.getHeader("Database");
        	if(database != null && database.equals("bayes")) {
                File root = new File((String) WOWStatic.config.Get("XMLROOT") + "/" + 
                		OverlayBayesUM.OBUM_DIR_NAME);
                if (!root.exists()) root.mkdir();
                
                InputStream is = request.getInputStream();
                OutputStream us = new FileOutputStream(new File(root, filename));
                byte[] buffer = new byte[8192];
                int nr = 8192;
                while (nr != -1) {
                    nr = is.read(buffer, 0, 8192);
                    if (nr != -1) us.write(buffer, 0, nr);
                }
                us.close();
                response.getOutputStream().close();
        		return;
        	}
        	
            String authorfiles = WOWStatic.config.Get("WOWROOT")+WOWStatic.AUTHORFILESPATH.substring(1);
            InputStream is = request.getInputStream();
            OutputStream us = new FileOutputStream(new File(authorfiles+authorname+"/"+filename));
            byte[] buffer = new byte[8192];
            int nr = 8192;
            while (nr != -1) {
                nr = is.read(buffer, 0, 8192);
                if (nr != -1) us.write(buffer, 0, nr);
            }
            us.close();
            response.getOutputStream().close();
            
            AuthorsConfig aconf = new AuthorsConfig();
            WowAuthor wowAuthor = aconf.GetAuthor(authorname);
            Vector v = wowAuthor.getCourseList();
            String coursename = filename.replaceAll(".wow","").replaceAll(".gaf","").replaceAll(OverlayBayesUM.OBUM_EXT, "");
            if (!v.contains(coursename)) {
                v.add(coursename);
                aconf.StoreConfig();
            }
        } catch(Exception e) {System.out.println(e);}
    }
}