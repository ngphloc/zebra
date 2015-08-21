/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * PCSingleQuote.java 1.0, February 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;


/**
 * This parse component implements the single quote string constant.
 */
public class PCSingleQuote implements ParseComponent {
    public PCSingleQuote() {}

    public String[] getParseMethods() {return new String[] {};}

    /**
     * Scans the string to see if the first token in the string is
     * handled by this component.
     */
    public GenToken scan(ParseString ps, GenToken token) throws ParserException {
        if (token != null) {
            //a token is already found, so handle it here
            return token;
        }
        //no token is found yet
        char ch = ps.nextChar();
        GenToken result = null;
        if (ch == '\'') {
            StringBuffer aconst = new StringBuffer();
            char ach = ps.nextChar();
            while ( ach != '\'' ) {aconst.append(ach); ach = ps.nextChar();}
            result = new GenToken("const");
            result.add("value", aconst.toString());
        } else ps.returnChar(ch);
        return result;
    }

    /**
     * Uses the specified method to parse the next tokens in the
     * list.
     */
    public ParseNode parse(String method, ParseList pl, ParseInfo info) throws ParserException {
        throw new ParserException("method '"+method+"' is not handled by PCSingleQuote");
    }
}