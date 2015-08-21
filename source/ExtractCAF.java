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

import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.config.*;
import vn.spring.WOW.conversion.mot2wow.*;

import java.util.Vector;
import java.util.LinkedList;

import javax.servlet.http.*;
import java.io.*;

public class ExtractCAF extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExtractCAF() {}

    private LinkedList findPrograms(String path) throws IOException {
        LinkedList result = new LinkedList();
        File root = new File(path);
        File[] children = root.listFiles();
        if (children == null) return result;
        for (int i=0;i<children.length;i++) {
            if (children[i].getName().endsWith(".lag")) result.add(children[i].toString());
        }
        return result;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            //test input
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
            String wowroot = WOWStatic.config.Get("WOWROOT");
            //execute WOW! part
            LinkedList argsl = new LinkedList();
            argsl.add("-r"+wowroot);
            argsl.add("-d");
            argsl.add("-s");
            argsl.add(wowroot+course+"/"+course+".xml");
            argsl.add(course);
            argsl.add(sauthor);
            argsl.addAll(findPrograms(wowroot+course));
            //run conversion
            CAFtoWOW ctoa = new CAFtoWOW((String[])argsl.toArray(new String[] {}));
            //add to WOW! database
            ConceptDB cdb = WOWStatic.DB().getConceptDB();
            Vector list = cdb.getConceptList();
            for (int i=0;i<ctoa.concepts.size();i++) {
                Concept concept = (Concept)ctoa.concepts.get(i);
                if (list.contains(concept.getName())) {
                    cdb.setConcept(cdb.findConcept(concept.getName()), concept);
                } else {
                    concept.id = cdb.createConcept(concept.getName());
                    cdb.setConcept(concept.id, concept);
                }
            }
            //output succes reply
            PrintWriter out = null;
            try {
                out = new PrintWriter(response.getWriter());
            } catch (IOException e) {throw new IllegalStateException("WOWStatic.showError: Unable to obtain PrintWriter.");}
            response.setContentType("text/html;charset=UTF-8");
            out.println("<h1>Extract CAF files to WOW!</h1>");
            out.println("<p>Extraction succesful</p>");
            out.close();
        } catch (Exception e) {e.printStackTrace();WOWStatic.showError(response, "An error has occurred while extracting the CAF file: "+e.toString(), false);}
    }
}