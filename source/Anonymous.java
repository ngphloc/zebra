/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Anonymous.java 1.0, July 31, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.Initialization;

import javax.servlet.http.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.LinkedList;

public class Anonymous extends HttpServlet {
// Servlet for registering a new anonymous user.

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Hashtable parameters;
    private Profile profile;
    //Added by @Tomi (WOW-Pitt version integration)
    //List of initialized courses
    private LinkedList initCourses=new LinkedList();
    //End @Tomi

    public void service(HttpServletRequest request, HttpServletResponse response) {

        try {
            parameters = new Hashtable();
            profile = null;
            String buttonName = null;
            Enumeration paramNames = request.getParameterNames();
            // Check form elements
            while (paramNames.hasMoreElements()) {
                String paramName = (String)paramNames.nextElement();
                if ("session_button".equals(paramName) || "resume_button".equals(paramName))
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
                    } else throw new WOWException("The form contains a field with more than one value. This is not allowed. Please notify the author.");
                }
            }
            if (buttonName == null) throw new WOWException("Anonymous Login form uses the wrong submit button.");
            ProfileDB pdb = WOWStatic.DB().getProfileDB();
            String password = "";
            String loginid = "";

            String start = request.getParameter("start");
            if (start == null) throw new WOWException("The form does not indicate a start page. Please notify the author.");
            start = start.trim();
            if (start.equals("")) throw new WOWException("The form does not indicate a start page. Please notify the author.");

            String course = request.getParameter("course");
            if (course == null) throw new WOWException("The form does not indicate the application name. Please notify the author.");
            course = course.trim();
            if (course.equals("")) throw new WOWException("The form does not indicate the application name. Please notify the author.");

            String directory = request.getParameter("directory");
            if (directory == null) throw new WOWException("The form does not indicate the application directory. Please notify the author.");
            directory = directory.trim();
            if (directory.equals("")) throw new WOWException("The form does not indicate the application directory. Please notify the author.");

            if ("session_button".equals(buttonName)) {
                // Create new anonymous session
                long ct = System.currentTimeMillis();
                loginid="an_userID_" + String.valueOf(ct);
                registerProfile(loginid, password, course);
                Cookie WOW_userID = new Cookie("WOW_userID", loginid);
                WOW_userID.setMaxAge(63072000);
                response.addCookie(WOW_userID);
            }
            if ("resume_button".equals(buttonName)) {
                Cookie[] cookies = request.getCookies();
                int i;
            if (cookies == null)
                throw new WOWException("You do not appear to have any WOW! cookies. There is no anonymous session to resume.");
            for (i=0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("WOW_userID")) {
                    String Cookie_Value = cookies[i].getValue();
                    loginid = Cookie_Value;
                }
            }
            if (!loginid.startsWith("an_userID_"))
                throw new WOWException("There is no anonymous WOW! session to resume.");
            }
            if (profile == null) {
                long id = pdb.findProfile(loginid);
                profile = pdb.getProfile(id);
            }
            if ("resume_button".equals(buttonName)) {
                // Align the existing user model with the current concept
                // structure of the application.
                check(profile, course);
            }
            if (!course.equals(profile.getAttributeValue("personal","course"))) {
                // Detected that the application the user logs onto is
                // different from the previously accessed application.
                // So change the "course" value to the current application.
                profile.setAttributeValue("personal","course", course);
        if (!directory.equals(profile.getAttributeValue("personal","directory")))
            profile.setAttributeValue("personal","directory", directory);
                pdb.setProfile(profile.id, profile);
            }


                        // Added by @Tomi(WOW-Pitt version integration)
                        String activeconcept="";
                        createSession(request, loginid, profile, course, directory, activeconcept);

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
                        requestURL.delete(requestURL.length()-9, requestURL.length());
                        requestURL.append("Get/"+directory+"/?concept="+activeconcept);
                        System.out.println("Redirecting to: "+requestURL.toString());
                        response.sendRedirect(response.encodeRedirectURL(requestURL.toString()));
                        // End @Tomi

            //createSession(request, loginid, profile, course);
            //response.sendRedirect(request.getContextPath()+"/Get/"+start);
        } catch (Exception e) {e.printStackTrace();WOWStatic.showError(response, "An error has occurred while creating an anonymous session. "+e.toString(), false);}
    }

    public void registerProfile(String loginid, String password, String course) throws
           WOWException {

        ProfileDB pdb = WOWStatic.DB().getProfileDB();
        WOWStatic.DB().getConceptDB();
        if (pdb.findProfile(loginid)==0) {
            // Profile does not seem to exist.
            //Create profile and initialize variables
            AttributeValue value = null;
            profile = pdb.getProfile(pdb.createProfile());
            Hashtable values = profile.getValues();
            //Add personal attributes
            value = new AttributeValue(true);
            value.setValue(loginid);
            values.put("personal.id", value);
            Enumeration e = parameters.keys();
            while (e.hasMoreElements()) {
                String key = (String)e.nextElement();
                String keyvalue = ((String)parameters.get(key)).trim();
                if (!key.equals("start")) {
                    if (keyvalue.equals("true") || keyvalue.equals("false")) {
                    value = new AttributeValue(true);
                    } else if (keyvalue.matches("[0-9]+")) {
                    value = new AttributeValue(true);
                    } else {
                    value = new AttributeValue(true);
                    }
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
            //added by @Barend, @Loc Nguyen to make a UM lock-unlock system
            value = new AttributeValue(true);
            value.setValue("false");
            values.put("personal.umlocked", value);
            //end added by @Barend, @Loc Nguyen to make a UM lock-unlock system

            pdb.setProfile(profile.id, profile);
        } else { throw new WOWException("The profile already exists");
        }
    }

    private void createSession(HttpServletRequest request, String loginid, Profile profile, String course, String directory, String activeconcept) throws
            WOWException {
        // Create session at http level
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
