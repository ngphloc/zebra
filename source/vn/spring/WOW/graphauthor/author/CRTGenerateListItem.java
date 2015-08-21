/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * CRTGenerateListItem.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

/**
 * CRTGenerateListItem data class to store a GenerateListItem.
 *
 */
public class CRTGenerateListItem {
    public String location;
    public Boolean propagating;
    public String requirement;
    public CRTTrueActions trueActions;
    public CRTFalseActions falseActions;

    /**
     * Default constructor.
     */
    public CRTGenerateListItem() {
        propagating = Boolean.TRUE;
        trueActions = new CRTTrueActions();
        falseActions = new CRTFalseActions();
        location = "";
        requirement = "";
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


                // lastDotPlace--;
                returnS = location.substring(firstDotPlace, lastDotPlace);
            }
        } catch (Exception e) {
        }

        return returnS;
    }
}