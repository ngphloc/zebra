/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Evaluator.java 1.0, December 7, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;

public class Evaluator {
    private GenParser parser = null;
    private VariableLocator vl = null;

    public Evaluator(GenParser parser, VariableLocator vl) {
        if (parser == null) throw new NullPointerException();
        if (vl == null) throw new NullPointerException();
        this.parser = parser;
        this.vl = vl;
    }

    public Object evaluate(String expr) throws ParserException {
        ParseNode node = parser.parse("EXPR", expr);
        return evaluate(node);
    }

    public Object evaluate(ParseNode node) throws ParserException {
        return nodeValue(node);
    }

    public Object nodeValue(ParseNode node) throws ParserException {
        if (node.getType().equals("const")) return node.get("value");
        if (node.getType().equals("id")) return vl.getVariableValue((String)node.get("name"));
        if (node.getType().equals("expr")) {
            if (node.get("second") == null) {
                return doOperation((String)node.get("operator"), nodeValue((ParseNode)node.get("first")));
            } else {
                return doOperation((String)node.get("operator"), nodeValue((ParseNode)node.get("first")), nodeValue((ParseNode)node.get("second")));
            }
        }
        throw new ParserException("unknown parse node in expression: "+node.getType());
    }

    private Object doOperation(String op, Object first, Object second) throws ParserException {
        if (op.equals("-")) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Float( ((Float)first).floatValue() - ((Float)second).floatValue() );
            } else throw new ParserException("float expected");
        } else
        if (op.equals("+")) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Float( ((Float)first).floatValue() + ((Float)second).floatValue() );
            } else if ((first instanceof String) && (second instanceof String)) {
                return new String( ((String)first) + ((String)second) );
            } else throw new ParserException("float or string expected");
        } else
        if (op.equals("*")) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Float( ((Float)first).floatValue() * ((Float)second).floatValue() );
            } else throw new ParserException("float expected");
        } else
        if (op.equals("/")) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Float( ((Float)first).floatValue() / ((Float)second).floatValue() );
            } else throw new ParserException("float expected");
        } else
        if (op.equals("%")) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Float( abs(((Float)first).floatValue()) % abs(((Float)second).floatValue()) );
            } else throw new ParserException("float expected");
        } else
        if (op.equals("<")) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Boolean( ((Float)first).floatValue() < ((Float)second).floatValue() );
            } else throw new ParserException("float expected");
        } else
        if (op.equals("<=")) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Boolean( ((Float)first).floatValue() <= ((Float)second).floatValue() );
            } else throw new ParserException("float expected");
        } else
        if (op.equals(">")) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Boolean( ((Float)first).floatValue() > ((Float)second).floatValue() );
            } else throw new ParserException("float expected");
        } else
        if (op.equals(">=")) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Boolean( ((Float)first).floatValue() >= ((Float)second).floatValue() );
            } else throw new ParserException("float expected");
        } else
        if (op.equals("==")) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Boolean( ((Float)first).floatValue() == ((Float)second).floatValue() );
            } else if ((first instanceof String) && (second instanceof String)) {
                return new Boolean( ((String)first).equals( ((String)second) ) );
            } else if ((first instanceof Boolean) && (second instanceof Boolean)) {
                return new Boolean( ((Boolean)first).booleanValue() == ((Boolean)second).booleanValue() );
            } else throw new ParserException("type mismatch");
        } else
        if (op.equals("!=")) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Boolean( ((Float)first).floatValue() != ((Float)second).floatValue() );
            } else if ((first instanceof String) && (second instanceof String)) {
                return new Boolean( !((String)first).equals( ((String)second) ) );
            } else if ((first instanceof Boolean) && (second instanceof Boolean)) {
                return new Boolean( ((Boolean)first).booleanValue() != ((Boolean)second).booleanValue() );
            } else throw new ParserException("type mismatch");
        } else
        if (op.equals("=>")) {
            if ((first instanceof Boolean) && (second instanceof Boolean)) {
                boolean f = ((Boolean)first).booleanValue();
                boolean s = ((Boolean)second).booleanValue();
                if (!f) return new Boolean(true);
                if ((f) && (s)) return new Boolean(true);
                return new Boolean(false);
            } else throw new ParserException("boolean expected");
        } else
        if (op.equals("||")) {
            if ((first instanceof Boolean) && (second instanceof Boolean)) {
                return new Boolean( ((Boolean)first).booleanValue() || ((Boolean)second).booleanValue() );
            } else throw new ParserException("boolean expected");
        } else
        if (op.equals("&&")) {
            if ((first instanceof Boolean) && (second instanceof Boolean)) {
                return new Boolean( ((Boolean)first).booleanValue() && ((Boolean)second).booleanValue() );
            } else throw new ParserException("boolean expected");
        } else throw new ParserException("invalid operator");
    }

    private Object doOperation(String op, Object param) throws ParserException {
        if (op.equals("-")) {
            if (param instanceof Float) {return new Float(-((Float)param).floatValue());} else {
                throw new ParserException("float expected");
            }
        } else if (op.equals("!")) {
            if (param instanceof Boolean) {return new Boolean(!((Boolean)param).booleanValue());} else {
                throw new ParserException("boolean expected");
            }
        } else if (op.equals("~")) {
            return new Float(param.toString().hashCode());
        }
        return null;
    }

    private float abs(float f) {
        return (f<0?-f:f);
    }

    public static void main(String[] args) throws ParserException {
        GenParser parser = new GenParser();
        parser.registerParseComponent(new PCComment());
        parser.registerParseComponent(new PCIdentifier());
        parser.registerParseComponent(new PCCommon());
        Evaluator evaluator = new Evaluator(parser, new TestVL());
        System.out.println(evaluator.evaluate("\"test\"==teststr"));
    }

    private static class TestVL implements VariableLocator {
        public TestVL() {}

        /**
         * This method retrieves the value of the specified variable.
         * Must return float values when dealing with integers.
         * @param variable The name of the variable whose value is
         *        requested.
         * @return The actual value of the specified variable. This can
         *         be a boolean, a String or an integer (type long).
         */
        public Object getVariableValue(String variable) throws ParserException {
            if (variable.equals("test")) return new Boolean(false);
            if (variable.equals("testint")) return new Float(4);
            if (variable.equals("teststr")) return "test";
            throw new ParserException("variable '"+variable+"' not found");
        }
    }
}