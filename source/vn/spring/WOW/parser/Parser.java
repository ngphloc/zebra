/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Parser.java 1.0, May 25, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.parser;

import vn.spring.WOW.exceptions.*;
import java.util.Vector;

public class Parser {
    private VariableLocator vl = null;
    private IntScanner scanner = null;
    private Token token = null;
	private Vector[] OP = {
        new Vector(new MyVector(new int[] {Token.IMPLIESOP})),
        new Vector(new MyVector(new int[] {Token.OROP})),
        new Vector(new MyVector(new int[] {Token.ANDOP})),
        new Vector(new MyVector(new int[] {Token.LESSOP, Token.ATMOSTOP, Token.MOREOP, Token.ATLEASTOP, Token.EQOP, Token.NOTEQOP})),
        new Vector(new MyVector(new int[] {Token.PLUSOP, Token.MINUSOP})),
        new Vector(new MyVector(new int[] {Token.TIMESOP, Token.DIVOP, Token.REMOP})),
        new Vector(new MyVector(new int[] {Token.MINUSOP, Token.NOTOP, Token.HASHOP}))};

    public Parser(VariableLocator vl) {
        this.vl = vl;
    }

    public Object parse(String expr) throws ParserException {
        if ((expr==null) || ("".equals(expr))) return new Boolean(true);
        scanner = new IntScanner(expr);
        NEXTSYM();
        try {
            return EXPR();
        } catch (ParserException e) {
            e.printStackTrace();
            throw new ParserException(e.getMessage()+ " (expression: '"+expr+"')");
        }
    }

    private void NEXTSYM() throws ParserException {token = scanner.nextToken();}

    private Object EXPR() throws ParserException {
        return EX(0);
    }

    private Object EX(int i) throws ParserException {
        Object result = null;
        if (i<6) {
            result = EX(i+1);
            while ((token.symValue() == Token.OPSYM) && ( OP[i].contains(new Integer(token.getOperandType())) )) {
                int op=token.getOperandType();
                NEXTSYM();
                Object second = EX(i+1);
                result = doOperation(op, result, second);
            }
        } else if (i==6) {
            if ((token.symValue() == Token.OPSYM) && ( OP[6].contains(new Integer(token.getOperandType())) )) {
                int op=token.getOperandType();
                NEXTSYM();
                result = EX(6);
                result = doOperation(op, result);
            } else {
                if ( (token.symValue() == Token.CONSTSYM) || (token.symValue() == Token.LBRACKSYM) || (token.symValue() == Token.IDSYM) ) {
                    result = EX(7);
                } else
                    throw new ParserException("syntax error in expression");
            }
        } else {
            if (token.symValue() == Token.CONSTSYM) {
                result = DENOT();
            } else if (token.symValue() == Token.IDSYM) {
                result = VARP();
            } else if (token.symValue() == Token.LBRACKSYM) {
                NEXTSYM(); result = EX(1); TERM(Token.RBRACKSYM);
            }
        }
        return result;
    }

    private Object DENOT() throws ParserException {
        Object result = null;
        if (token.symValue() == Token.CONSTSYM) {
            result = token.getConstValue();
            NEXTSYM();
        } else throw new ParserException("constant expected");
        return result;
    }

    private Object VARP() throws ParserException {
        Object result = null;
        if (token.symValue() == Token.IDSYM) {
            result = vl.getVariableValue(token.getIDName());
            NEXTSYM();
        } else throw new ParserException("identifier expected");
        return result;
    }

    private void TERM(int sym) throws ParserException {
        if (token.symValue() == sym) NEXTSYM(); else throw new ParserException((new Token(sym)).symString()+" expected");
    }

    private Object doOperation(int op, Object first, Object second) throws ParserException {
        if (op == Token.MINUSOP) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Float( ((Float)first).floatValue() - ((Float)second).floatValue() );
            } else throw new ParserException("float expected");
        } else
        if (op == Token.PLUSOP) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Float( ((Float)first).floatValue() + ((Float)second).floatValue() );
            } else if ((first instanceof String) && (second instanceof String)) {
                return new String( ((String)first) + ((String)second) );
            } else throw new ParserException("float or string expected");
        } else
        if (op == Token.TIMESOP) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Float( ((Float)first).floatValue() * ((Float)second).floatValue() );
            } else throw new ParserException("float expected");
        } else
        if (op == Token.DIVOP) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Float( ((Float)first).floatValue() / ((Float)second).floatValue() );
            } else throw new ParserException("float expected");
        } else
        if (op == Token.REMOP) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Float( abs(((Float)first).floatValue()) % abs(((Float)second).floatValue()) );
            } else throw new ParserException("float expected");
        } else
        if (op == Token.LESSOP) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Boolean( ((Float)first).floatValue() < ((Float)second).floatValue() );
            } else throw new ParserException("float expected");
        } else
        if (op == Token.ATMOSTOP) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Boolean( ((Float)first).floatValue() <= ((Float)second).floatValue() );
            } else throw new ParserException("float expected");
        } else
        if (op == Token.MOREOP) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Boolean( ((Float)first).floatValue() > ((Float)second).floatValue() );
            } else throw new ParserException("float expected");
        } else
        if (op == Token.ATLEASTOP) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Boolean( ((Float)first).floatValue() >= ((Float)second).floatValue() );
            } else throw new ParserException("float expected");
        } else
        if (op == Token.EQOP) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Boolean( ((Float)first).floatValue() == ((Float)second).floatValue() );
            } else if ((first instanceof String) && (second instanceof String)) {
                return new Boolean( ((String)first).equals( ((String)second) ) );
            } else if ((first instanceof Boolean) && (second instanceof Boolean)) {
                return new Boolean( ((Boolean)first).booleanValue() == ((Boolean)second).booleanValue() );
            } else throw new ParserException("type mismatch");
        } else
        if (op == Token.NOTEQOP) {
            if ((first instanceof Float) && (second instanceof Float)) {
                return new Boolean( ((Float)first).floatValue() != ((Float)second).floatValue() );
            } else if ((first instanceof String) && (second instanceof String)) {
                return new Boolean( !((String)first).equals( ((String)second) ) );
            } else if ((first instanceof Boolean) && (second instanceof Boolean)) {
                return new Boolean( ((Boolean)first).booleanValue() != ((Boolean)second).booleanValue() );
            } else throw new ParserException("type mismatch");
        } else
        if (op == Token.IMPLIESOP) {
            if ((first instanceof Boolean) && (second instanceof Boolean)) {
                boolean f = ((Boolean)first).booleanValue();
                boolean s = ((Boolean)second).booleanValue();
                if (!f) return new Boolean(true);
                if ((f) && (s)) return new Boolean(true);
                return new Boolean(false);
            } else throw new ParserException("boolean expected");
        } else
        if (op == Token.OROP) {
            if ((first instanceof Boolean) && (second instanceof Boolean)) {
                return new Boolean( ((Boolean)first).booleanValue() || ((Boolean)second).booleanValue() );
            } else throw new ParserException("boolean expected");
        } else
        if (op == Token.ANDOP) {
            if ((first instanceof Boolean) && (second instanceof Boolean)) {
                return new Boolean( ((Boolean)first).booleanValue() && ((Boolean)second).booleanValue() );
            } else throw new ParserException("boolean expected");
        } else throw new ParserException("invalid operator");
    }

    private Object doOperation(int op, Object param) throws ParserException {
        if (op == Token.MINUSOP) {
            if (param instanceof Float) {return new Float(-((Float)param).floatValue());} else {
                throw new ParserException("float expected");
            }
        } else if (op == Token.HASHOP) {
            return new Float(param.toString().hashCode());
        } else if (op == Token.NOTOP) {
            if (param instanceof Boolean) {return new Boolean(!((Boolean)param).booleanValue());} else {
                throw new ParserException("boolean expected");
            }
        }
        return null;
    }

    private float abs(float f) {
        return (f<0?-f:f);
    }

    private class MyVector extends Vector {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MyVector(int[] list) {
            super();
            for (int i=0;i<list.length;i++) add(new Integer(list[i]));
        }
    }

    public static void main(String[] args) throws
            ParserException,
            DatabaseException,
            javax.naming.NamingException {
        if (args.length < 1) {
            System.out.println("usage: Parser <expr>");
            return;
        }
        vn.spring.WOW.datacomponents.Profile profile = new vn.spring.WOW.datacomponents.Profile();
        //init profile
        vn.spring.WOW.datacomponents.AttributeValue attrvalue = new vn.spring.WOW.datacomponents.AttributeValue();
        attrvalue.setValue("30");
        profile.getValues().put("test.concept.a1", attrvalue);
        attrvalue = new vn.spring.WOW.datacomponents.AttributeValue();
        attrvalue.setValue("true");
        profile.getValues().put("test.concept.b", attrvalue);
        attrvalue = new vn.spring.WOW.datacomponents.AttributeValue();
        attrvalue.setValue(" (test)");
        profile.getValues().put("test.concept.s", attrvalue);

        java.util.Hashtable utable = new java.util.Hashtable();
        utable.put("test.concept.a1", new Float(30));

        UMVariableLocator umvl = new UMVariableLocator(profile, new vn.spring.WOW.WOWDB.XMLConceptDB(vn.spring.WOW.WOWStatic.root));
        umvl.setUpdateTable(utable);
        Parser parser = new Parser(umvl);
        System.out.println(parser.parse(args[0]));
    }
}