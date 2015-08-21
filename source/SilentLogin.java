/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * SilentLogin.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.Initialization;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.servlet.http.*;
import java.io.*;


public class SilentLogin extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Added by @Tomi (WOW-Pitt version integration)
    //List of initialized courses
    private LinkedList initCourses=new LinkedList();
    //End @Tomi

    private Profile profile;
    private PrintWriter pw;

    public void service(HttpServletRequest request, HttpServletResponse response) {
System.out.println("silent login...");
        boolean result = false;
        try {pw = response.getWriter();} catch (Exception e) {e.printStackTrace();return;}
        try {
            ProfileDB pdb = WOWStatic.DB().getProfileDB();
            String loginid = request.getParameter("login");
            if (loginid == null)
                throw new WOWException("User id field is missing. Please notify the author, because without this field you cannot log on.");
            loginid = loginid.trim();
            if (loginid.equals(""))
                throw new WOWException("No user id was entered. You must provide an identification to get access to this hyperdocument.");

            String password = request.getParameter("password");
            if (password == null)
                throw new WOWException("Password field is missing. Please notify the author, because without this field you cannot log on.");
            password = password.trim();
            if (password.equals(""))
                throw new WOWException("No password was entered. You must provide a password for authentication purposes.");

            String start = "welcome";
            String course = "tutorial";
            String directory = "tutorial";
System.out.println("username: "+loginid);
System.out.println("password: "+password);
System.out.println("start: "+start);
System.out.println("course: "+course);
System.out.println("directory: "+directory);

            long id = pdb.findProfile(loginid);
            if (id == 0)
                throw new WOWException("Profile not found.");
            profile = pdb.getProfile(id);
            if (!profile.getAttributeValue("personal", "password").equals(password))
                throw new WOWException("Wrong Password, please go back and try again.");
            profile.setAttributeValue("personal","course", course);
            profile.setAttributeValue("personal","directory", directory);
            pdb.setProfile(profile.id, profile);

            check(profile, course);
            String activeconcept="";
            createSession(request, loginid, course, directory, activeconcept);
            if(!initCourses.contains(course)){
                Initialization.fillInMemStructs(request.getSession(), false);
            } else
                initCourses.add(course);
            result = true;
        } catch (Exception e) {e.printStackTrace();}
        pw.print((result?"true":"false"));
    }

    private void createSession(HttpServletRequest request, String loginid, String course, String directory, String activeconcept) throws WOWException {
         // Changed by @Loc Nguyen @ 22-01-2008
         // added this functionality in the check method, this removes one loop of the concepts
        HttpSession ses = request.getSession(true);
        ses.setAttribute("login", loginid);
        ses.setAttribute("course", course);
        ses.setAttribute("directory", directory);
        ses.setAttribute("activeconcept", activeconcept);
        this.getServletContext().setAttribute(loginid, profile);
    }

    private Concept getConcept(String name) {
        ConceptDB cdb = WOWStatic.DB().getConceptDB();
        try {
            return cdb.getConcept(cdb.findConcept(name));
        } catch (Exception e) {}
        return null;
    }

    /**
     * resets the session stable attributes back to their default values and makes sure that
     * every attribute is present in the profile and that there are no "loose" attributes .
     * This function removes the user profile entries that do not exists anymore in the
     * concept / attribute part This is not done if the user profile entry starts with "personal"
     * this is ALSO NOT DONE when the profile entry contains the word pageStability
     * Concepts or attributes that are not existent in the profile are added to the profile
     * with the default value All attributes that are session stable are reset to their default
     * values.
     * @param profile User Profile
     */
    private void check(Profile profile, String course) throws WOWException {
        //remove session stable values from the profile
        Hashtable values = profile.getValues();
        for (Enumeration e = values.keys();e.hasMoreElements();) {
            String key = (String)e.nextElement();
            if (key.startsWith(course+".")) {
                int p = key.indexOf(".*$$*");
                if (p > 0) {
                    //page stability attribute
                    String conceptname = key.substring(0,p);
                    Concept concept = getConcept(conceptname);
                    if (concept != null) if (concept.isSessionStable()) values.remove(key);
                } else {
                    //not a page stability attribute
                    String conceptname = key.substring(0, key.lastIndexOf("."));
                    Concept concept = getConcept(conceptname);
                    if (concept != null) {
                        Attribute attr = null;
                        try {
                            attr = concept.getAttribute(key.substring(key.lastIndexOf(".")+1,key.length()));
                        } catch (Exception ex) {}
                        if (attr != null) {
                            if (attr.isSessionStable()) values.remove(key);
                        } else {System.out.println("removing key: "+key);values.remove(key);}
                    }
                }
            }
        }

        WOWStatic.DB().getProfileDB().setProfile(profile.id, profile);
    }
}