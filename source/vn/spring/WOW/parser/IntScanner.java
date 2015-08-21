/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * IntScanner.java 1.0, May 25, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.parser;

import vn.spring.WOW.exceptions.*;

/**
 * This is the lexical scanner of the expression parser.
 */
public class IntScanner {
    // the current part of the expression we are scanning
    private StringBuffer currentexpr;

    // debug count
    private int dcount;

    /**
     * Creates a new lexical scanner using the specified expression.
     * @param expr The expression to scan.
     */
    public IntScanner(String expr) {
        currentexpr = new StringBuffer(expr);
        dcount = 0;
    }

    private char nextChar() {
        if (!containsChars()) {if (dcount == -1) throw new IndexOutOfBoundsException("Error scanning: end of expression"); dcount = -1; return '\f';}
        char ch = currentexpr.charAt(0);
        currentexpr.deleteCharAt(0);
        dcount++;
        return ch;
    }

    private void returnChar(char ch) {
        currentexpr.insert(0, ch);
        dcount--;
    }

    private boolean containsChars() {
        return currentexpr.length()!=0;
    }

    /**
     * Returns the next token in the expression.
     * @return The next token.
     */
    public Token nextToken() throws ParserException {
        try {
            char ch;
            do {ch = nextChar();} while ((Character.isWhitespace(ch)) && (ch != '\f'));
            if (Character.isJavaIdentifierStart(ch)) {
                StringBuffer idname = new StringBuffer();
                idname.append(ch);
                ch = nextChar();
                while ( (Character.isJavaIdentifierPart(ch)) || (ch == '.') ) {idname.append(ch); ch = nextChar();}
                returnChar(ch);
                if (idname.toString().equals("true")) return new Token(Token.CONSTSYM, new Boolean(true));
                if (idname.toString().equals("false")) return new Token(Token.CONSTSYM, new Boolean(false));
                if (idname.toString().equals("hash")) return new Token(Token.OPSYM, Token.HASHOP);
                return new Token(Token.IDSYM, idname.toString());
            } else if (ch == '(') {
                return new Token(Token.LBRACKSYM);
            } else if (ch == ')') {
                return new Token(Token.RBRACKSYM);
            } else if (ch == '+') {
                return new Token(Token.OPSYM, Token.PLUSOP);
            } else if (ch == '-') {
                return new Token(Token.OPSYM, Token.MINUSOP);
            } else if (ch == '*') {
                return new Token(Token.OPSYM, Token.TIMESOP);
            } else if (ch == '/') {
                return new Token(Token.OPSYM, Token.DIVOP);
            } else if (ch == '%') {
                return new Token(Token.OPSYM, Token.REMOP);
            } else if (ch == '~') {
                return new Token(Token.OPSYM, Token.HASHOP);
            } else if (ch == '|') {
                ch = nextChar();
                if (ch == '|') return new Token(Token.OPSYM, Token.OROP);
            } else if (ch == '&') {
                ch = nextChar();
                if (ch == '&') return new Token(Token.OPSYM, Token.ANDOP);
            } else if (ch == '=') {
                ch = nextChar();
                if (ch == '=') return new Token(Token.OPSYM, Token.EQOP);
                if (ch == '>') return new Token(Token.OPSYM, Token.IMPLIESOP);
            } else if (ch == '!') {
                ch = nextChar();
                if (ch == '=') return new Token(Token.OPSYM, Token.NOTEQOP);
                returnChar(ch);
                return new Token(Token.OPSYM, Token.NOTOP);
            } else if (ch == '<') {
                ch = nextChar();
                if (ch == '=') return new Token(Token.OPSYM, Token.ATMOSTOP);
                returnChar(ch);
                return new Token(Token.OPSYM, Token.LESSOP);
            } else if (ch == '>') {
                ch = nextChar();
                if (ch == '=') return new Token(Token.OPSYM, Token.ATLEASTOP);
                returnChar(ch);
                return new Token(Token.OPSYM, Token.MOREOP);
            } else if (ch == '"') {
                StringBuffer aconst = new StringBuffer();
                ch = nextChar();
                while ( ch != '"' ) {aconst.append(ch); ch = nextChar();}
                return new Token(Token.CONSTSYM, aconst.toString());
            } else if (ch == '\'') {
                StringBuffer aconst = new StringBuffer();
                ch = nextChar();
                while ( ch != '\'' ) {aconst.append(ch); ch = nextChar();}
                return new Token(Token.CONSTSYM, aconst.toString());
            } else if (Character.isDigit(ch)) {
                StringBuffer aconst = new StringBuffer();
                aconst.append(ch);
                ch = nextChar();
                while ( (Character.isDigit(ch)) || (ch == '.') ) {aconst.append(ch); ch = nextChar();}
                returnChar(ch);

                float result = Float.parseFloat(aconst.toString());
                return new Token(Token.CONSTSYM, new Float(result));
            } else if (ch == '\f') return new Token(Token.NOSYM);
            throw new ParserException("Error scanning: char "+dcount+", '"+ch+"'");
        } catch (Exception e) {
            throw new ParserException(e.getMessage());
        }
    }

    public static void main(String[] args) throws ParserException {
        String s = "(test.gtr.read == \"hello\") || (b==false)";
        IntScanner myScanner = new IntScanner(s);
        System.out.println(s);
        Token token;
        do {
            token = myScanner.nextToken();
            if (token != null) System.out.println(token);
        } while (token != null);
    }

}