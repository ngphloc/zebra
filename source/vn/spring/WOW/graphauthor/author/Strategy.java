/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is free software


*/
/**
 * CRTConceptRelationType.java 1.0, September 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;
import java.util.LinkedList;
/**
 * CRTConceptRelationType data class to store crt.
 *
 */
public class Strategy {
    public String name;
    public String description;
    public LinkedList ifStatements;

    /**
     * default constructor.
     */
    public Strategy() {
        name = "";
        description = "";
        ifStatements = new LinkedList();
    }
}