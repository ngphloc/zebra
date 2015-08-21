/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * WOWOutConceptList.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * WOWOutConcept stores the output concept
 *
 */
public class WOWOutConceptList {
    public String name;
    public LinkedList conceptList;

    /**
     * Default constructor
     */
    public WOWOutConceptList() {
        conceptList = new LinkedList();
        name = "";
    }

    /**
     * Returns a Hashtable with the concept names as keys
     */
    public Hashtable GetConceptNames() {
        Hashtable conceptNames = new Hashtable();

        for (Iterator i = conceptList.iterator(); i.hasNext();) {
            WOWOutConcept aout = (WOWOutConcept) i.next();
            conceptNames.put(aout.name.trim(), null);
        }

        return conceptNames;
    }
}