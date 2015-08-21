/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is free software
    

*/
/**
 * CRTAction.java 1.0, September 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

/**
 * StrategyAction data class to store the action of a strategy.
 *
 */
public class StrategyAction {
    public String UMvariable;
    public String expression;
    //public String UMupdate;

    /**
     * default constructor.
     */
    public StrategyAction() {
        UMvariable = "";
        expression = "";
        //UMupdate = "";
    }
}