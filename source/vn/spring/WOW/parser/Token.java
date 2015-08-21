/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Token.java 1.0, May 25, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.parser;

/**
 * This class represents a single scanned token.
 */
public class Token {
    // the following constants are used as possible values for the 'sym' field
    public static final int NOSYM = 0;
    public static final int IDSYM = 1;
    public static final int OPSYM = 2;
    public static final int CONSTSYM = 3;
    public static final int LBRACKSYM = 4;
    public static final int RBRACKSYM = 5;

    // the following constants are used as possible values for the 'minor' field
    public static final int NOOP = 0;
    public static final int IMPLIESOP = 1;
    public static final int OROP = 2;
    public static final int ANDOP = 3;
    public static final int LESSOP = 4;
    public static final int MOREOP = 5;
    public static final int ATMOSTOP = 6;
    public static final int ATLEASTOP = 7;
    public static final int NOTEQOP = 8;
    public static final int EQOP = 9;
    public static final int PLUSOP = 10;
    public static final int MINUSOP = 11;
    public static final int TIMESOP = 12;
    public static final int DIVOP = 13;
    public static final int NOTOP = 14;
    public static final int REMOP = 15;
    public static final int HASHOP = 16;

    /**
     * This field contains the name of the variable if this tokens
     * major type is IDSYM.
     */
    private String idname = null;

    /**
     * This field contains the minor type of the operation if this
     * tokens major type is OPSYM.
     */
    private int minor = NOOP;

    /**
     * This field represents the constant value if this tokens major
     * type is CONSTSYM.
     */
    private Object constvalue = null;

    /**
     * This field represents the major type of this token
     */
    private int sym = NOSYM;

    /**
     * Creates a new token of the specified type.
     * @param psym The type of this token.
     */
    public Token(int psym) {
        if ((psym<0) || (psym>5)) throw new IndexOutOfBoundsException("Not a valid major sym type");
        sym = psym;
    }

    /**
     * Creates a new token of the specified type.
     * @param psym The type of this token.
     * @param obj An extra parameter used in various ways (dependant
     *        on the actual value of psym.
     */
    public Token(int psym, Object obj) {
        this(psym);
        if (sym == IDSYM) {
            if (!(obj instanceof String)) throw new IllegalArgumentException("Identifier names must be of type string");
            idname = (String)obj;
        } else if (sym == CONSTSYM) {
            if ((!(obj instanceof Float)) && (!(obj instanceof Boolean)) && (!(obj instanceof String))) throw new IllegalArgumentException("The specified constant is not a String, boolean or number");
            constvalue = obj;
        }
    }

    /**
     * Creates a new token of the specified type.
     * @param psym The type of this token.
     */
     //@param obj An extra parameter used in various ways (dependant
     //       on the actual value of psym.
    public Token(int psym, int op) {
        this(psym);
        if (sym == OPSYM) {
            if ((op<0) || (op>16)) throw new IndexOutOfBoundsException("Not a valid minor operand type");
            minor = op;
        }
    }

    /**
     * Returns the major type of this token.
     */
    public int symValue() {return sym;}

    /**
     * Returns the constant value of this token.
     */
    public Object getConstValue() {
        if (sym == CONSTSYM) {
            return constvalue;
        }
        throw new UnsupportedOperationException("This method is only available to CONSTSYM tokens");
    }

    /**
     * Returns the identifier name of this token.
     */
    public String getIDName() {
        if (sym == IDSYM) {
            return idname;
        }
        throw new UnsupportedOperationException("This method is only available to IDSYM tokens");
    }

    /**
     * Returns the minor operand type of this token.
     */
    public int getOperandType() {
        if (sym == OPSYM) {
            return minor;
        }
        throw new UnsupportedOperationException("This method is only available to OPSYM tokens");
    }

    /**
     * Returns a string representation of this token.
     */
    public String toString() {
        if (sym == LBRACKSYM) return "(LBRACKSYM)";
        if (sym == RBRACKSYM) return "(RBRACKSYM)";
        if (sym == IDSYM) return "(IDSYM, "+getIDName()+")";
        if (sym == OPSYM) return "(OPSYM, "+getOperandType()+")";
        if (sym == CONSTSYM) return "(CONSTSYM, "+getConstValue()+")";
        return "(NOSYM)";
    }

    /**
     * Returns a string specifying the type of token.
     */
    public String symString() {
        if (sym == LBRACKSYM) return "'('";
        if (sym == RBRACKSYM) return "')'";
        if (sym == IDSYM) return "identifier";
        if (sym == OPSYM) return "operand";
        if (sym == CONSTSYM) return "constant";
        return "unknown token";
    }
}