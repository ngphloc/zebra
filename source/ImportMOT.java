/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ExtractCAF.java 1.0, Febuary 1, 2005
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.config.*;
import vn.spring.WOW.exceptions.*;

import vn.spring.mot2caf.MOTtoCAF;

import javax.servlet.http.*;
import java.io.*;

public class ImportMOT extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImportMOT() {}

    public void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            //test author part
            String sauthor = request.getParameter("author");
            String password = request.getParameter("password");
            String course = request.getParameter("course");
            if ((sauthor == null) || (password == null) || (course == null))
                throw new WOWException("The form is not properly filled.");
            AuthorsConfig ac = new AuthorsConfig();
            WowAuthor author = ac.GetAuthor(sauthor);
            if (author == null) throw new WOWException("Invalid author.");
            if (!author.checkPasswd(password)) throw new WOWException("Invalid password.");
            if (!author.getCourseList().contains(course)) throw new WOWException("Course not managed by author.");

            //test MOT part
            String mothost = request.getParameter("mothost");
            String motuser = request.getParameter("motuser");
            String motpassword = request.getParameter("motpassword");
            String motdatabase = request.getParameter("motdatabase");
            String motlesson = request.getParameter("motlesson");
            String wowroot = WOWStatic.config.Get("WOWROOT");
            String motfile = wowroot+course+"/"+course+".xml";
            if (motlesson == null) throw new WOWException("Invalid lesson.");
            if (motlesson.trim().equals("")) throw new WOWException("Invalid lesson.");

            String[] args = new String[] {motfile, motlesson, mothost, motuser, motpassword, motdatabase};
            new MOTtoCAF(motfile, motlesson, args);

            PrintWriter out = null;
            try {
                out = new PrintWriter(response.getWriter());
            } catch (IOException e) {throw new IllegalStateException("WOWStatic.showError: Unable to obtain PrintWriter.");}
            response.setContentType("text/html;charset=UTF-8");
            out.println("<h1>Import MOT database to CAF file</h1>");
            out.println("<p>MOT database succesfully imported</p>");
            out.close();
        } catch (Exception e) {WOWStatic.showError(response, "An error has occurred while importing MOT database: "+e.toString(), false);}
    }
}