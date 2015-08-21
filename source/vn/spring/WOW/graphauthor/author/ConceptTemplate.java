/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ConceptTemplate.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.util.Hashtable;
import java.util.LinkedList;


public class ConceptTemplate {
   public String name;
   public  LinkedList attributes;
   public String hasresource;
   public String concepttype;
   public Hashtable conceptRelations;

    public ConceptTemplate() {
        name = "";
        attributes = new LinkedList();
        hasresource = "";
        concepttype = "";
        conceptRelations = new Hashtable();
    }
}