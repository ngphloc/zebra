/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * PCComment.java 1.0, November 23, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;

/**
 * This parse component has only got a scanning part. It filters out
 * comments from the source program. Place it first in the list of
 * parse components.
 */
public class PCComment implements ParseComponent {
    public PCComment() {}

    public String[] getParseMethods() {return new String[0];}

    /**
     * Scans the string to see if the first token in the string is
     * handled by this component.
     */
    public GenToken scan(ParseString ps, GenToken token) throws ParserException {
        char ch = ps.nextChar();
        boolean comment;
        do {
            comment = false;
            if (ch == '/') {
                ch = ps.nextChar();
                if (ch == '/') {
                    //comment
                    comment = true;
                    do {ch = ps.nextChar();} while (ch != '\n');
                    do {ch = ps.nextChar();} while ((Character.isWhitespace(ch)) && (ch != '\f'));
                } else {
                    //not a comment
                    ps.returnChar(ch);
                    ps.returnChar('/');
                }
            } else {
                //not a comment
                ps.returnChar(ch);
            }
        } while (comment);
        return null;
    }

    /**
     * Uses the specified method to parse the next tokens in the
     * list. In this class there are no parsing methods.
     */
    public ParseNode parse(String method, ParseList pl, ParseInfo info) throws ParserException {
        throw new ParserException("method '"+method+"' is not handled by PCLAG");
    }
}