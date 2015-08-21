/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software;

*/
/**
 * CRTListItem.java 1.0, September 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.util.LinkedList;
import java.util.Vector;


/**
 *
 *
 */
public class StrategyElseStatement {
    public Vector actionList;
    public LinkedList selectList;
    public LinkedList sortList;

    /**
     * default constructor.
     */
    public StrategyElseStatement() {
        actionList = new Vector();
        selectList = new LinkedList();
        sortList = new LinkedList();
    }
}