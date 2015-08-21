/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ConvertMOT2CAF.java 1.0, October 19, 2005
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
import vn.spring.mot2caf.MOTtoCAF;

import com.sun.xml.tree.*;

import java.util.Enumeration;
import java.util.Vector;
import java.util.LinkedList;

import javax.servlet.http.*;
import java.io.*;

public class ConvertMOT2WOW extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ConvertMOT2WOW() {}

    private boolean validCourse(AuthorsConfig ac, String course) {
        Vector allcourses = new Vector();
        for (Enumeration keys = ac.AuthorHash.keys();keys.hasMoreElements();) {
            Object key = keys.nextElement();
            allcourses.addAll(((WowAuthor)ac.AuthorHash.get(key)).getCourseList());
        }
        return (!allcourses.contains(course));
    }

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
            //test WOW! part
            String sauthor = request.getParameter("author");
            String password = request.getParameter("password");
            String course = request.getParameter("course");
            if ((sauthor == null) || (password == null) || (course == null))
                throw new WOWException("The form is not properly filled.");
            AuthorsConfig ac = new AuthorsConfig();
            WowAuthor author = ac.GetAuthor(sauthor);
            if (author == null) throw new WOWException("Invalid author.");
            if (!author.checkPasswd(password)) throw new WOWException("Invalid password.");
            if (!author.getCourseList().contains(course)) {
                //course not managed by author
                if (validCourse(ac, course)) {
                    //the course is not used by somebody else
                    author.getCourseList().add(course);
                    ac.StoreConfig();
                } else throw new WOWException("That course is already in use by another author.");
            }

            //test MOT part
            String mothost = request.getParameter("mothost");
            String motuser = request.getParameter("motuser");
            String motpassword = request.getParameter("motpassword");
            String motdatabase = request.getParameter("motdatabase");
            String motlesson = request.getParameter("motlesson");
            String makecaf = request.getParameter("makecaf");
System.out.println("CAF:"+makecaf);
            String wowroot = WOWStatic.config.Get("WOWROOT");
            if (motlesson == null) throw new WOWException("Invalid lesson.");
            if (motlesson.trim().equals("")) throw new WOWException("Invalid lesson.");

//WOWStatic.timeElapsed();
            //execute MOT part
            String[] args = new String[] {null, motlesson, mothost, motuser, motpassword, motdatabase};
            MOTtoCAF mtoc = new MOTtoCAF(("on".equals(makecaf)?wowroot+course+"/"+course+".xml":null), motlesson, args);
            XmlDocument doc = mtoc.getDocument();
//System.out.println("MOT: "+WOWStatic.timeElapsed());

            //execute WOW! part
            LinkedList argsl = new LinkedList();
            argsl.add("-r"+wowroot);
            argsl.add("-o");
            argsl.add("-d");
            argsl.add("-s");
            argsl.add(wowroot+course+"/"+course+".xml");
            argsl.add(course);
            argsl.add(sauthor);
            argsl.addAll(findPrograms(wowroot+course));
            //run conversion
            CAFtoWOW ctoa = new CAFtoWOW((String[])argsl.toArray(new String[] {}));
            ctoa.startProcess(doc);
//System.out.println("WOW: "+WOWStatic.timeElapsed());

            //add to WOW! database
            ConceptDB cdb = WOWStatic.DB().getConceptDB();
            if (cdb instanceof XMLConceptDB) ((XMLConceptDB)cdb).disableIndexWriting();
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
            if (cdb instanceof XMLConceptDB) ((XMLConceptDB)cdb).enableIndexWriting();
//System.out.println("DB: "+WOWStatic.timeElapsed());

            //output succes reply
            PrintWriter out = null;
            try {
                out = new PrintWriter(response.getWriter());
            } catch (IOException e) {throw new IllegalStateException("WOWStatic.showError: Unable to obtain PrintWriter.");}
            response.setContentType("text/html;charset=UTF-8");
            out.println("<h1>Convert from MOT database to WOW! course</h1>");
            out.println("<p>Conversion succesful</p>");
            out.close();
        } catch (Exception e) {e.printStackTrace();WOWStatic.showError(response, "An error has occurred while converting: "+e.toString(), false);}
    }
}
