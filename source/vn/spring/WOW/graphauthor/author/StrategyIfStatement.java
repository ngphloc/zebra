/*


    This file is part of WOW! (Adaptive Hypermedia for All)

    WOW! is free software


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


/**
 *
 *
 */
public class StrategyIfStatement {
    public String condition;
    public StrategyThenStatement thenStatement;
    public StrategyElseStatement elseStatement;

    /**
     * default constructor.
     */
    public StrategyIfStatement() {
        condition = "";
        thenStatement = new StrategyThenStatement();
        elseStatement = new StrategyElseStatement();
    }
}