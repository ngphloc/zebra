/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ChangeProfile.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import java.util.*;
import java.io.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.Utils.*;

/**
 * The ChangeProfile servlet processes the input parameters from
 * the Update user profile form.
 */

public class ChangeProfile {

    public Profile profile;
    private String login;
    private String course;
    private StringBuffer sb;
    private boolean updateerror;

    public Resource getOutput(Profile profile, String login, String course, Map params) {
        try {
            this.profile = profile;
            this.login = login;
            this.course = course;
            sb = new StringBuffer();
            updateerror = false;
            // Preprocess the form input parameters
            CgiUtil Util = new CgiUtil(params);

            changeUserProfile(sb, Util, profile);
            WOWStatic.DB().getProfileDB().setProfile(profile.id, profile);
        } catch (Exception e) {
            System.out.println("ColorChange Error: "+e.toString());
        }
        InputStream insb = new ByteArrayInputStream(sb.toString().getBytes());
        return new Resource(insb, new ResourceType("text/html"));
    }


  /**
   * This method reads input from a POST request and replaces the user model
   * by the values contained in the request.
   * The form for this POST request is supposed to be the one generated by
   * the "genConfig" method.
   * There is a string parameter which contains the title for the
   * generated HTML page.
   */
  public StringBuffer changeUserProfile(StringBuffer sb, CgiUtil Util, Profile profile) throws DatabaseException, InvalidProfileException, InvalidConceptException, InvalidAttributeException {
    ConceptDB cdb = WOWStatic.DB().getConceptDB();
    boolean ok = true;
    boolean nonascii = false;
    for (Enumeration e = Util.keys();e.hasMoreElements();) {
        String incomingString = (String)e.nextElement();
        String value = (String)Util.get(incomingString);
        value = value.trim();
        if (incomingString.indexOf('.') != -1 ) {

            int index = incomingString.lastIndexOf('.');
            String attribute = incomingString.substring((index + 1));
            incomingString = incomingString.substring(0, index);

            index = incomingString.lastIndexOf('.');
            String conceptName = incomingString.substring((index + 1));

            try {
                if (!course.trim().equals("")) {
                    if (!conceptName.trim().equals("personal")) conceptName = course + "." + conceptName;
                }
                // only perform the update if the input field is not empty.
                if (!"".equals(value)) {
                    if (!conceptName.trim().equals("personal")) {
                        Attribute attr = cdb.getAttribute(cdb.findConcept(conceptName),attribute);
                        if (attr.getType() == AttributeType.ATTRINT) {
                            try {
                                Integer.parseInt(value);
                            } catch (NumberFormatException nfe) {
                                nfe.printStackTrace();
                                ok = false;
                                break;// value is not a number
                            }
                        } else if (attr.getType() == AttributeType.ATTRBOOL) {
                            if (!value.equals("true") && !value.equals("false")) {
                                ok = false;
                                break;// value is not a boolean
                            }
                        }
                    }
                    if (!WOWStatic.checkascii(value)) {
                        ok = false;
                        nonascii = true;
                        break;
                    }
                    if (ok) {
                        AttributeValue avalue = new AttributeValue(false);
                        avalue.setValue(value);
                        ProfileUpdate update = new ProfileUpdate(profile, WOWStatic.PM());
                        update.updateAttribute(conceptName, attribute, avalue);
                    }
                }
            } catch (Exception esetattribute) {
                sb.append("<h3> Error updating the profile for: " + login + "</h3>");
                sb.append("<p>The error occured with the following attribute: "+conceptName+"."+attribute+"</p>");
                updateerror = true;
            }
        }
    }
    if (!ok) {
        sb.append("<h3> Error updating the profile for " + login + "</h3>");
        if (nonascii) sb.append("The data you entered contains a non-ascii character.<br>Please do not use letters with accents or other non-ascii symbols.");
        else sb.append("Please, check types of the attributes!");
        updateerror = true;
    }
    if (!updateerror) {
        WOWStatic.PM().purgeProfile(profile);
        WOWStatic.DB().getProfileDB().setProfile(profile.id, profile);
        sb.append("<h3> The user profile for: " + login + " has been updated.</h3>");
        sb.append("You can now go back and continue reading using the updated setup.");
    }
    return sb;
  }
}