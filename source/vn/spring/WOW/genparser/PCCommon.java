/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * PCCommon.java 1.0, November 23, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;

import java.util.*;

/**
 * This parse component scans for common tokens like constants and
 * symbols. Place this after the identifier parse component to
 * ensure that boolean constants are found.
 */
public class PCCommon implements ParseComponent {
	private Vector[] OP = {
        new Vector(new MyVector(new String[] {"=>"})),
        new Vector(new MyVector(new String[] {"||"})),
        new Vector(new MyVector(new String[] {"&&"})),
        new Vector(new MyVector(new String[] {"<", "<=", ">", ">=", "==", "!="})),
        new Vector(new MyVector(new String[] {"+", "-"})),
        new Vector(new MyVector(new String[] {"*", "/", "%"})),
        new Vector(new MyVector(new String[] {"-", "!", "~"}))};

    public PCCommon() {}

    public String[] getParseMethods() {return new String[] {"EXPR", "DENOT"};}

    /**
     * Scans the string to see if the first token in the string is
     * handled by this component.
     */
    public GenToken scan(ParseString ps, GenToken token) throws ParserException {
        if (token != null) {
            //a token is already found, so handle it here
            GenToken result = token;
            if (token.getType().equals("id")) {
                //token is an identifier
                String name = (String)token.get("name");
                if ((name.equals("true")) || (name.equals("false"))) {
                    //token is a boolean constant
                    result = new GenToken("const");
                    result.add("value", new Boolean(name));
                }
                if (name.equals("hash")) {
                    //token is a hash operator
                    result = opToken("~");
                }
            }
            return result;
        }
        //no token is found yet
        char ch = ps.nextChar();
        GenToken result = null;
        if (ch == ';') {
            result = new GenToken("semicol");
        } else if (ch == ',') {
            result = new GenToken("comma");
        } else if (ch == '(') {
            result = new GenToken("lbrack");
        } else if (ch == ')') {
            result = new GenToken("rbrack");
        } else if (ch == '{') {
            result = new GenToken("lacc");
        } else if (ch == '}') {
            result = new GenToken("racc");
        } else if (ch == '+') {
            char ach = ps.nextChar();
            if (ach=='=') result = new GenToken("plusassign"); else {
                ps.returnChar(ach);
                result = opToken("+");
            }
        } else if (ch == '-') {
            char ach = ps.nextChar();
            if (ach=='=') result = new GenToken("minusassign"); else {
                ps.returnChar(ach);
                result = opToken("-");
            }
        } else if (ch == '*') {
            result = opToken("*");
        } else if (ch == '/') {
            result = opToken("/");
        } else if (ch == '%') {
            result = opToken("%");
        } else if (ch == '~') {
            result = opToken("~");
        } else if (ch == '|') {
            char ach = ps.nextChar();
            if (ach == '|') result = opToken("||"); else ps.returnChar(ach);
        } else if (ch == '&') {
            char ach = ps.nextChar();
            if (ach == '&') result = opToken("&&"); else ps.returnChar(ach);
        } else if (ch == '=') {
            char ach = ps.nextChar();
            if (ach == '=') result = opToken("=="); else
            if (ach == '>') result = opToken("=>"); else {
                ps.returnChar(ach);
                result = new GenToken("assign");
            }
        } else if (ch == '!') {
            char ach = ps.nextChar();
            if (ach == '=') result = opToken("!="); else {
                ps.returnChar(ach);
                result = opToken("!");
            }
        } else if (ch == '<') {
            char ach = ps.nextChar();
            if (ach == '=') result = opToken("<="); else {
                ps.returnChar(ach);
                result = opToken("<");
            }
        } else if (ch == '>') {
            char ach = ps.nextChar();
            if (ach == '=') result = opToken(">="); else {
                ps.returnChar(ach);
                result = opToken(">");
            }
        } else if (ch == '"') {
            StringBuffer aconst = new StringBuffer();
            char ach = ps.nextChar();
            while ( ach != '"' ) {aconst.append(ach); ach = ps.nextChar();}
            result = new GenToken("const");
            result.add("value", aconst.toString());
        } else if (Character.isDigit(ch)) {
            StringBuffer aconst = new StringBuffer();
            aconst.append(ch);
            char ach = ps.nextChar();
            while ( (Character.isDigit(ach)) || (ch == '.') ) {aconst.append(ach); ach = ps.nextChar();}
            ps.returnChar(ach);

            float f = Float.parseFloat(aconst.toString());
            result = new GenToken("const");
            result.add("value", new Float(f));
        } else {
            ps.returnChar(ch);
        }
        return result;
    }

    private GenToken opToken(String s) {
        GenToken result = new GenToken("op");
        result.add("operation", s);
        return result;
    }

    /**
     * Uses the specified method to parse the next tokens in the
     * list.
     */
    public ParseNode parse(String method, ParseList pl, ParseInfo info) throws ParserException {
        if ("EXPR".equals(method)) return EXPR(pl, info);
        if ("DENOT".equals(method)) return DENOT(pl, info);
        throw new ParserException("method '"+method+"' is not handled by PCLAG");
    }

    public static class ConstNode extends ParseNode implements Cloneable {
        public Object value = null;

        public ConstNode(ParseNode parent) {super(parent);}

        public LinkedList getChildList() {
            return new LinkedList();
        }

        public Object get(String key) {
            if (key.equals("value")) return value;
            return null;
        }

        public String toString() {
            if (value instanceof String) return "\""+value+"\""; else
            if (value instanceof Float) return (new Integer(((Float)value).intValue())).toString(); else
                return value.toString();
        }

        public String getType() {return "const";}

        public Object clone() {
            ConstNode result = new ConstNode(parent);
            result.value = value;
            return result;
        }

        public int hashCode() {return (value==null?0:value.hashCode());}

        public boolean equals(Object object) {
            if (object == null) return false;
            if (!(object instanceof ConstNode)) return false;
            ConstNode node = (ConstNode)object;
            return (value==null?node.value==null:value.equals(node.value));
        }
    }

    public static class ExprNode extends ParseNode implements Cloneable {
        public ParseNode first = null;
        public String operator = null;
        public ParseNode second = null;

        public ExprNode(ParseNode parent) {super(parent);}

        public LinkedList getChildList() {
            LinkedList result = new LinkedList();
            result.add(first);
            if (second != null) result.add(second);
            return result;
        }

        public Object get(String key) {
            if (key.equals("first")) return first;
            if (key.equals("operator")) return operator;
            if (key.equals("second")) return second;
            return null;
        }

        public String toString() {
            if (second != null) {
                return "("+first+operator+second+")";
            } else {
                return "("+operator+first+")";
            }
        }

        public String getType() {return "expr";}

        public Object clone() {
            ExprNode result = new ExprNode(parent);
            if (first != null) result.first = (ParseNode)first.clone();
            if (second != null) result.second = (ParseNode)second.clone();
            result.operator = operator;
            return result;
        }

        public int hashCode() {
            int result = 0;
            if (first != null) result += first.hashCode();
            if (operator != null) result += operator.hashCode();
            if (second != null) result += second.hashCode();
            return result;
        }

        public boolean equals(Object object) {
            if (object == null) return false;
            if (!(object instanceof ExprNode)) return false;
            ExprNode node = (ExprNode)object;
            boolean result = true;
            result = result && (first==null?node.first==null:first.equals(node.first));
            result = result && (operator==null?node.operator==null:operator.equals(node.operator));
            result = result && (second==null?node.second==null:second.equals(node.second));
            return result;
        }
    }

    private ParseNode EXPR(ParseList pl, ParseInfo info) throws ParserException {
        return EX(0, pl, info);
    }

    private ParseNode EX(int i, ParseList pl, ParseInfo info) throws ParserException {
        ParseNode result = null;
        GenToken token = pl.current();
        if (i<6) {
            result = EX(i+1, pl, info);
            token = pl.current();
            while ((token.getType().equals("op")) && ( OP[i].contains(token.get("operation")) )) {
                String op = (String)token.get("operation");
                pl.moveNext();
                ParseNode second = EX(i+1, pl, info);
                token = pl.current();
                ParseNode first = result;
                result = new ExprNode(null);
                ((ExprNode)result).first = first; first.setParent(result);
                ((ExprNode)result).second = second; second.setParent(result);
                ((ExprNode)result).operator = op;
//                if (result.equals("(UM.GM.Concept == \"access\")")) result = "(specialLAGcurrentconceptname.access == true)";
            }
        } else if (i==6) {
            if ((token.getType().equals("op")) && ( OP[6].contains(token.get("operation")) )) {
                String op = (String)token.get("operation");
                pl.moveNext();
                ParseNode first = EX(6, pl, info);
                result = new ExprNode(null);
                ((ExprNode)result).first = first; first.setParent(result);
                ((ExprNode)result).operator = op;
            } else {
                result = EX(7, pl, info);
            }
        } else {
            if (token.getType().equals("const")) {
                result = info.parse("DENOT", pl);
            } else if (token.getType().equals("id")) {
                result = info.parse("VARP", pl);
                token = pl.current();
                String rname = (String)result.get("name");
                if ((token.getType().equals("lbrack")) && (info.existsMethod(rname.toLowerCase())) && (rname.equals(rname.toLowerCase()))) {
                    //assume function call
                    pl.movePrevious();
                    result = info.parse(rname, pl);
                }
            } else if (token.getType().equals("lbrack")) {
                pl.moveNext();
                result = EX(0, pl, info);
                token = pl.current();
                if (!token.getType().equals("rbrack")) throw new ParserException("'(' expected", token);
                pl.moveNext();
            } else
                throw new ParserException("syntax error in expression");
        }
        return result;
    }

    public ParseNode DENOT(ParseList pl, ParseInfo info) throws ParserException {
        GenToken token = pl.current();
        if (!token.getType().equals("const")) throw new ParserException("constant expected", token);
        ConstNode result = new ConstNode(null);
        result.value = token.get("value");
        pl.moveNext();
        return result;
    }

    private class MyVector extends Vector {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MyVector(String[] list) {
            super();
            for (int i=0;i<list.length;i++) add(list[i]);
        }
    }
}