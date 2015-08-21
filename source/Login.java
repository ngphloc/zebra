/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Login.java 1.0, August 30, 2008
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
import vn.spring.zebra.helperservice.WOWContextListener;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.servlet.http.*;


public class Login extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Added by @Tomi (WOW-Pitt version integration)
    //List of initialized courses
    private LinkedList initCourses=new LinkedList();
    //End @Tomi

    private Hashtable parameters = new Hashtable();
    private Profile profile;

    public void service(HttpServletRequest request, HttpServletResponse response) {
        // Servlet for non-anonymous login.

        try {
        String buttonName = null;
        Enumeration paramNames = request.getParameterNames();
        // Check form elements
        while (paramNames.hasMoreElements()) {
        String paramName = (String)paramNames.nextElement();
        if ("enter_button".equals(paramName))
            buttonName = paramName;
        else {
            // Handle all form fields that are not the submit button
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
            String paramValue = paramValues[0];
            if (paramValue.length() != 0)
                if (!WOWStatic.checkascii(paramValue))
                	throw new WOWException("The data you entered contains a non-ascii character.<br>\nPlease do not use letters with accents or other non-ascii symbols.");
                parameters.put(paramName, paramValue);
            }
            else
            	throw new WOWException("The form contains a field with more than one value. This is not allowed. Please notify the author.");
        }
        }
        if (buttonName==null)
        	throw new WOWException("The Registration form must have a submit button named \"enter_button\" (not " + buttonName + "\".");

        ProfileDB pdb = WOWStatic.DB().getProfileDB();
        String loginid = request.getParameter("login");
        if (loginid == null)
        	throw new WOWException("User id field is missing. Please notify the author, because without this field you cannot log on.");
        loginid = loginid.trim();
        if (loginid.equals(""))
        	throw new WOWException("No user id was entered. You must provide an identification to get access to this hyperdocument.");

        // When we reach this point the login form is acceptable
        // and we can let the user log in.
        long id = pdb.findProfile(loginid);
        if (id == 0) throw new WOWException("Profile not found.");
        profile = pdb.getProfile(id);

        String password = request.getParameter("password");
        if (password == null)
        	throw new WOWException("Password field is missing. Please notify the author, because without this field you cannot log on.");
        password = password.trim();
        if (password.equals(""))
        	throw new WOWException("No password was entered. You must provide a password for authentication purposes.");
        
        String course = request.getParameter("course");
        if (course == null)
        	throw new WOWException("Application name field is missing. Please notify the author, because without this field you cannot log on.");
        course = course.trim();
        if (course.equals(""))
        	throw new WOWException("There is no valid application name. Please notify the author, because without this field you cannot log on.");

        //added by Loc Nguyen 2009 October
        String start = null;
        try {
        	String mycourse = profile.getAttributeValue("personal", "course");
        	if(mycourse.equals(course)) {
        		start = profile.getAttributeValue("personal", "start");
        		ConceptDB cdb = WOWStatic.DB().getConceptDB();
        		cdb.findConcept(mycourse + "." + start);
        	}
        }
        catch(Exception e) {start = null; }
        boolean updateStart = false;
        if(start == null) {
        	start = request.getParameter("start");
        	updateStart = true;
        }
        if (start == null) throw new WOWException("The form does not indicate a start page. Please notify the author.");
        start = start.trim();
        if (start.equals("")) throw new WOWException("The form does not indicate a start page. Please notify the author.");
        if(updateStart) {
            Hashtable values = profile.getValues();
            AttributeValue value = new AttributeValue(true);
            value.setValue(start);
            values.put("personal.start", value);
        }
        
        String directory = request.getParameter("directory");
        if (directory == null)
        	throw new WOWException("Application directory field is missing. Please notify the author, because without this field you cannot log on.");
        directory = directory.trim();
        if (directory.equals(""))
        	throw new WOWException("There is no valid application directory. Please notify the author, because without this field you cannot log on.");

        if (!profile.getAttributeValue("personal", "password").equals(password))
        	throw new WOWException("Wrong Password, please go back and try again.");
        // Check whether the user returns to the same application as before.
        if (!course.equals(profile.getAttributeValue("personal","course"))) {
        	// Different application, adjust personal profile accordingly
        	profile.setAttributeValue("personal","course", course);
        	if (!directory.equals(profile.getAttributeValue("personal","directory")))
        		profile.setAttributeValue("personal","directory", directory);
        	pdb.setProfile(profile.id, profile);
        }
        else if(updateStart) {
        	pdb.setProfile(profile.id, profile);
        	System.out.println("Write personal.start sucessfully");
        }

        // Align profile for this application.
        check(profile, course);
        // Added by @Tomi(WOW-Pitt version integration)
        String activeconcept="";
        createSession(request, loginid, course, directory, activeconcept);

        //Initialize in-mem objects
        if(!initCourses.contains(course)){
          Initialization.fillInMemStructs(request.getSession(), false);
        }
        else
          initCourses.add(course);

        //Set active concept
        try{
         // WOWStatic.VM().activeConcept=WOWStatic.DB().getConceptDB().getLinkedConcept(start);
          activeconcept=course+"."+start;
        }
        catch (Exception e) {System.out.println("login: Set activeconcept error: " + e.getClass().getName() + ": " + e.getMessage());}
        //Loc Nguyen added October 11 2009
        WOWContextListener.getInstance().getTriUMServer().monitorUM(loginid, course);
        
        //Redirect to 'Get' servlet
        StringBuffer requestURL = request.getRequestURL();
        requestURL.delete(requestURL.length()-5, requestURL.length());
        requestURL.append("Get/"+directory+"/?concept="+activeconcept);
        System.out.println("Redirecting to: "+requestURL.toString());
        response.sendRedirect(response.encodeRedirectURL(requestURL.toString()));
        // End @Tomi
        //createSession(request, loginid, course);
        //response.sendRedirect(request.getContextPath()+"/Get/"+start);
    } catch (Exception e) {WOWStatic.showError(response, "An error has occurred while accessing the database. "+e.toString(), false);}
 }

    private void createSession(HttpServletRequest request, String loginid, String course, String directory, String activeconcept) throws WOWException {
         // Changed by @Loc Nguyen @ 22-01-2008
         // added this functionality in the check method, this removes one loop of the concepts
        HttpSession ses = request.getSession(true);
        ses.setAttribute("login", loginid);
        ses.setAttribute("course", course);
        ses.setAttribute("directory", directory);
        ses.setAttribute("activeconcept", activeconcept); //Added by @Tomi (WOW-Pitt version integration)
        this.getServletContext().setAttribute(loginid, profile);
    }
/*
    // Sets the default values in-memory
    private void cachDefaultValues() throws WOWException {
        // Keeps the default values in-memory
        ConceptDB cdb = WOWStatic.DB().getConceptDB();
        Hashtable values = profile.getValues();
        String key = null;
        AttributeValue value = null;
        Attribute attr = null;
        DotString ds = null;
        long id;

        // ZZZ: This algorithm should still be improved as it takes
        // quadratic time (worst case).

        for (Enumeration keys = values.keys();keys.hasMoreElements();) {
            key = (String)keys.nextElement();
            value = (AttributeValue)values.get(key);
            //if (!value.isPersistent()) {
            if (!value.isPersistent() && !key.equals("personal.umlocked")) {
                ds = new DotString(key);
                key = ds.get(ds.size()-1);
                ds.set(ds.size()-1, null);
                id = cdb.findConcept(ds.toString());
                attr = cdb.getAttribute(id, key);
                value.setDefault(attr.getDefault());
            }
        }
    }
*/
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

        //update the personal attributes (except start which isn't in the profile)
        for (Enumeration e = parameters.keys();e.hasMoreElements();) {
            String key = (String)e.nextElement();
            String keyvalue = (String)parameters.get(key);
            if (!key.equals("start")) {
                AttributeValue value = new AttributeValue(true);
                value.setValue(keyvalue);
                values.put("personal."+key, value);
            }
        }
        WOWStatic.DB().getProfileDB().setProfile(profile.id, profile);
    }
}