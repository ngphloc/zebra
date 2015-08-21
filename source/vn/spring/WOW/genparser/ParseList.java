/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ParseList.java 1.0, November 26, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;

import java.util.*;

/**
 * ParseList maintains a list of tokens and a current position in
 * this list. ParseList is used by the various parse components to
 * obtain the current token.
 */
public class ParseList {
    private LinkedList tokens = null;
    private int current = 0;

    public ParseList(LinkedList tokens) {this.tokens = tokens;}

    public GenToken current() throws ParserException {
        if (!valid()) throw new ParserException("unexpected end of program");
        return (GenToken)tokens.get(current);
    }

    public int currentindex() {return current;}

    public void moveNext() {current++;}
    public void movePrevious() {if (current > 0) current--;}

    public boolean valid() {
        return ((current >= 0) && (current < tokens.size()));
    }

    public String toString() {return tokens.toString();}
}