/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * CRTConceptRelationType.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

/**
 * CRTConceptRelationType data class to store crt.
 *
 */
public class CRTConceptRelationType {
    public String name;
    public String color;
    public String style;
    public CRTProperties properties;
    public CRTListItem listItem;

    /**
     * default constructor.
     */
    public CRTConceptRelationType() {
        properties = new CRTProperties();
        listItem = new CRTListItem();
        name = "";
        color = "";
        style = "";
    }
}