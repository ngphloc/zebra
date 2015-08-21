/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * CRTSetDefault.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

/**
 * ConceptDivideInfo data class to store the setdefault values of a crt.
 *
 */
public class CRTSetDefault {
    /**
     * Contains the location of setDefault
     */
    public String location;

    /**
     * AND | OR combination if there are multiple setdefaults
     */
    public String combination;

    /**
     * The default value of an attribute
     */
    public String setdefault;

    /**
     * Default constructor with emptey var init.
     */
    public CRTSetDefault() {
        location = "";
        combination = "NONE";
        setdefault = "";
    }

    /**
     * Extract the attribute out of the location variable.
     *
     * @return The attribute
     */
    public String getAttribute() {
        String returnS = "";

        if (location.equals("")) {
            return returnS;
        }

        int firstDotPlace = location.indexOf(".");
        int lastDotPlace = location.lastIndexOf(".");

        try {
            if (firstDotPlace == lastDotPlace) {
                firstDotPlace++;
                returnS = location.substring(firstDotPlace);
            } else {
                firstDotPlace++;


                //  lastDotPlace--;
                returnS = location.substring(firstDotPlace, lastDotPlace);
            }
        } catch (Exception e) {
          //  System.out.println("kan geen attribute vinden");
        }

        return returnS;
    }
}