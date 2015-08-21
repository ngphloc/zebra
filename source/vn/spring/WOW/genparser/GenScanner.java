/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * GenScanner.java 1.0, November 23, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;

/**
 * General scanner that uses seperate modules allowing reuse of code
 * in scanning different languages.
 */
public class GenScanner {
    // a reference to the parse string
    private ParseString expr;

    private ParseInfo info = null;

    /**
     * Creates a new general lexical scanner using the specified
     * expression.
     * @param expr the expression to scan
     * @param info a reference to the object containing information
     *        about the current parser (cannot be null)
     */
    public GenScanner(String expr, ParseInfo info) {
        this.expr = new ParseString(expr);
        if (info == null) throw new NullPointerException();
        this.info = info;
    }

    /**
     * Returns the next token in the expression.
     * @return The next token.
     */
    public GenToken nextToken() throws ParserException {
        char ch;
        do {ch = expr.nextChar();} while ((Character.isWhitespace(ch)) && (ch != '\f'));
        expr.returnChar(ch);
        GenToken result = null;
        if (ch == '\f') return new GenToken("endofprogram");
        int lcount = expr.lineCount();
        int ccount = expr.charCount();
        try {
            result = info.scan(expr);
        } catch (Exception e) {
            result = null;
        }
        if (result == null) throw new ParserException("Error scanning: line "+expr.lineCount()+" char "+expr.charCount());
        result.lcount = lcount;
        result.ccount = ccount;
        return result;
    }
}