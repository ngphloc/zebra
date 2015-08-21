/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * VariableLocator.java 1.0, December 7, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;

public interface VariableLocator {
    /**
     * This method retrieves the value of the specified variable.
     * Must return float values when dealing with integers.
     * @param variable The name of the variable whose value is
     *        requested.
     * @return The actual value of the specified variable. This can
     *         be a boolean, a String or an integer (type long).
     */
    public Object getVariableValue(String variable) throws ParserException;
}