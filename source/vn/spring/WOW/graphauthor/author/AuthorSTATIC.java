/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AuthorSTATIC.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.util.LinkedList;
import java.util.Vector;


/**
 * AuthorSTATIC stores the complete datamodel.
 *
 */
public class AuthorSTATIC {
    public static LinkedList CRTList;
    public static CRTConceptRelationType trel;
    public static RELConceptRelations relwow;
    public static WOWOutConceptList wowOut;
//    public static LinkedList attributeList;
    public static LinkedList conceptInfoList;
    public static LinkedList templateList;
    public static LinkedList conceptList;
    public static String projectName = "unnamed";
    public static String authorName = "";
    //Natasha
    public static LinkedList strategyInfoList;
    public static Vector strategyList;
    //end Natasha

    /**
      * Default constructor.
      */
    public AuthorSTATIC() {
        CRTList = new LinkedList();
        trel = new CRTConceptRelationType();
        relwow = new RELConceptRelations();
        wowOut = new WOWOutConceptList();
       // attributeList = new LinkedList();
        conceptInfoList = new LinkedList();
        templateList = new LinkedList();
        conceptList = new LinkedList();
        //Natasha
        strategyInfoList = new LinkedList();
        strategyList = new Vector();
        //end Natasha
    }
}