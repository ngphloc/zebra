/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is free software


*/
/**
 * CRTListItem.java 1.0, September 1, 2008
 *
 * Copyright (c) 2001, 2002, 2003 University of Science.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.util.Vector;


/**
 *
 *
 */
public class StrategyThenStatement {
    public Vector actionList;
    public String actionAttribute;
    public String setDefaultAttribute;
    public String setDefaultExpression;
    public Vector selectList;
    public String selectAttribute;
    public Vector sortList;
    public String sortAttribute;

    /**
     * default constructor.
     */
    public StrategyThenStatement() {
        actionList = new Vector();
        actionAttribute = "";
        //should be a list
        setDefaultAttribute = "";
        setDefaultExpression = "";
        selectList = new Vector();
        selectAttribute = "";
        sortList = new Vector();
        sortAttribute = "";
    }
}