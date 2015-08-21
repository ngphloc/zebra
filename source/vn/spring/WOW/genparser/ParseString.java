/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ParseString.java 1.0, November 23, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;

/**
 * Represents the part of the program that still needs to be parsed.
 */
public class ParseString {
    // the current part of the expression being scanned
    private StringBuffer expr;

    // debug char count
    private int ccount;

    // debug line count
    private int lcount;

    /**
     * Creates a new parse string based on the specified program. The
     * parse string specified cannot be <code>null</code>.
     * @param s the program that needs to be parsed
     */
    public ParseString(String s) {
        if (s==null) throw new NullPointerException();
        expr = new StringBuffer(s);
        ccount = 1;
        lcount = 1;
    }

    public int lineCount() {return lcount;}
    public int charCount() {return ccount;}

    public char nextChar() {
        if (!containsChars()) {if (ccount == -1) throw new IndexOutOfBoundsException("Error scanning: end of program"); ccount = -1; return '\f';}
        char ch = expr.charAt(0);
        expr.deleteCharAt(0);
        ccount++;
        //check new line
        if (ch == '\n') {
            lcount++;
            ccount=1;
        }
        return ch;
    }

    public void returnChar(char ch) {
        expr.insert(0, ch);
        ccount--;
        if (ch == '\n') {lcount--; ccount=1;}
    }

    public boolean containsChars() {
        return expr.length()!=0;
    }
}