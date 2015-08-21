/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ParseComponent.java 1.0, November 23, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;

/**
 * Implemented by classes that add functionality to the general
 * parser.
 */
public interface ParseComponent {
    /**
     * A list of recursive decent parsing methods contained in this
     * parse component.
     */
    public String[] getParseMethods();

    /**
     * Uses the specified method to parse the next tokens in the
     * list.
     */
    public ParseNode parse(String method, ParseList pl, ParseInfo info) throws ParserException;

    /**
     * Scans the string to see if the first token in the string is
     * handled by this component.
     */
    public GenToken scan(ParseString ps, GenToken token) throws ParserException;
}