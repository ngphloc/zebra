/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Register.java 1.0, August 30, 2008
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
import java.util.LinkedList;

import javax.servlet.http.*;
import java.util.Hashtable;
import java.util.Enumeration;

public class Register extends HttpServlet {
    // Servlet for registering a new user (not anonymous).

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Hashtable parameters = new Hashtable();
    private Profile profile;
    //Added by @Tomi (WOW-Pitt version integration)
    //List of initialized courses
    private LinkedList initCourses=new LinkedList();
    //End @Tomi


    public void service(HttpServletRequest request, HttpServletResponse response) {

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
            } else
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

        String password = request.getParameter("password");
            if (password == null)
        throw new WOWException("Password field is missing. Please notify the author, because without this field you cannot log on.");
        password = password.trim();
        if (password.equals(""))
        throw new WOWException("No password was entered. You must provide a password for authentication purposes.");

        String start = request.getParameter("start");
        if (start == null)
        throw new WOWException("The form does not indicate a start page. Please notify the author.");
        start = start.trim();
        if (start.equals(""))
        throw new WOWException("The form does not indicate a start page. Please notify the author.");

        String course = request.getParameter("course");
            if (course == null)
        throw new WOWException("Application name field is missing. Please notify the author, because without this field you cannot log on.");
        course = course.trim();
        if (course.equals(""))
        throw new WOWException("There is no valid application name. Please notify the author, because without this field you cannot log on.");

        String directory = request.getParameter("directory");
            if (directory == null)
        throw new WOWException("Application directory field is missing. Please notify the author, because without this field you cannot log on.");
        directory = directory.trim();
        if (directory.equals(""))
        throw new WOWException("There is no valid application directory. Please notify the author, because without this field you cannot log on.");

            if (loginid.equals(password))
        throw new WOWException("The chosen password is too easy to guess. Please try again with a more difficult password.");

        // When we reach this point the regisration form is acceptable
        // and the user can be registered.
        registerProfile(loginid, password, course);

        if (profile == null) {
        long id = pdb.findProfile(loginid);
        profile = pdb.getProfile(id);
        }

        if (!course.equals(profile.getAttributeValue("personal","course"))) {
        // Detected that the application the user logs onto is
        // different from the previously accessed application.
        // So change the "course" value to the current application.
        profile.setAttributeValue("personal","course", course);
        pdb.setProfile(profile.id, profile);
        }
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

                        //Redirect to 'Get' servlet
                        StringBuffer requestURL = request.getRequestURL();
                        requestURL.delete(requestURL.length()-8, requestURL.length());
                        requestURL.append("Get/"+directory+"/?concept="+activeconcept);
                        System.out.println("Redirecting to: "+requestURL.toString());
                        response.sendRedirect(response.encodeRedirectURL(requestURL.toString()));
                        // End @Tomi
       // createSession(request, loginid, course);
       // response.sendRedirect(request.getContextPath()+"/Get/"+start);
    } catch (Exception e) {WOWStatic.showError(response, "An error has occurred while trying to register new user. "+e.toString(), false);}
    }

    public void registerProfile(String loginid, String password, String course) throws
           WOWException {
        ProfileDB pdb = WOWStatic.DB().getProfileDB();
        WOWStatic.DB().getConceptDB();
        // We used to forbid that an end-user and author are the same.
        // We allow it from now on until it would turn out to be a problem.
        // AuthorsConfig aconf= new AuthorsConfig();
        // if (aconf.GetAuthor(loginid)!=null) throw new WOWException("There exists an author with this login. Please, return back and try another login");
        if (pdb.findProfile(loginid)==0) {
            // Profile does not seem to exist... create profile
            AttributeValue value = null;
            profile = pdb.getProfile(pdb.createProfile());
            Hashtable values = profile.getValues();

            //Add personal attributes
            Enumeration e = parameters.keys();
            while (e.hasMoreElements()) {
                String key = (String)e.nextElement();
                String keyvalue = (String)parameters.get(key);
                // hack: "login" in form becomes "id" in profile
                if (key.equals("login")) key = "id";
                if (!key.equals("start")) {
                    value = new AttributeValue(true);
                    value.setValue(keyvalue);
                    values.put("personal."+key, value);
                }
            }
/*
            value = new AttributeValue(true);
            value.setValue("0000ff");
            values.put("personal.goodlink", value);
            value = new AttributeValue(true);
            value.setValue("202020");
            values.put("personal.badlink", value);
            value = new AttributeValue(true);
            value.setValue("7c007c");
            values.put("personal.neutrallink", value);
            value = new AttributeValue(true);
            value.setValue("ff0000");
            values.put("personal.externallink", value);
            value = new AttributeValue(true);
            value.setValue("7f0000");
            values.put("personal.externalvisitedlink", value);
            value = new AttributeValue(true);
            value.setValue("808080");
            values.put("personal.activelink", value);
*/
            value = new AttributeValue(true);
            value.setValue("false");
            values.put("personal.umlocked", value);

            pdb.setProfile(profile.id, profile);
        } else {
            throw new WOWException("The profile already exists");
        }
    }

    private void createSession(HttpServletRequest request, String loginid, String course, String directory, String activeconcept) throws
            WOWException {
        HttpSession ses = request.getSession(true);
        ses.setAttribute("login", loginid);
        ses.setAttribute("course", course);
        ses.setAttribute("directory", directory);
        ses.setAttribute("activeconcept", activeconcept); //Added by @Tomi (WOW-Pitt version integration)
        this.getServletContext().setAttribute(loginid, profile);
    }
/*
    // Sets the default values in-memory
    private void cachDefaultValues() throws
            WOWException {
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
            //changed by Barend on 17-3-2008
            if (!value.isPersistent() && (!key.equals("personal.umlocked"))) {
              //end changed by Barend on 17-03-2008
                ds = new DotString(key);
                key = ds.get(ds.size() - 1);
                ds.set(ds.size() - 1, null);
                id = cdb.findConcept(ds.toString());
                attr = cdb.getAttribute(id, key);
                value.setDefault(attr.getDefault());
            }
        }
    }

*/
}
