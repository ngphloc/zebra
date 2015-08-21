/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * PCIdentifier.java 1.0, December 7, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;

import java.util.*;

/**
 * This parse components contains scanning and parsing methods for
 * identifiers that can contain periods.
 */
public class PCIdentifier implements ParseComponent {
    public PCIdentifier() {}

    public String[] getParseMethods() {return new String[] {"VARP"};}

    /**
     * Scans the string to see if the first token in the string is
     * handled by this component.
     */
    public GenToken scan(ParseString ps, GenToken token) throws ParserException {
        if (token != null) return token; //if a token is already found, do nothing with it
        char ch = ps.nextChar();
        if (Character.isJavaIdentifierStart(ch)) {
            GenToken result = new GenToken("id");
            StringBuffer idname = new StringBuffer();
            while ( (Character.isJavaIdentifierPart(ch)) || (ch == '.') ) {
                idname.append(ch);
                ch = ps.nextChar();
            }
            ps.returnChar(ch);
            result.add("name", idname.toString());
            return result;
        } else {
            //not an identifier
            ps.returnChar(ch);
            return null;
        }
    }

    /**
     * Uses the specified method to parse the next tokens in the
     * list. In this class there are no parsing methods.
     */
    public ParseNode parse(String method, ParseList pl, ParseInfo info) throws ParserException {
        if ("VARP".equals(method)) return VARP(pl, info);
        throw new ParserException("method '"+method+"' is not handled by PCLAG");
    }

    public static class IDNode extends ParseNode implements Cloneable {
        public String name = null;

        public IDNode(ParseNode parent) {super(parent);}

        public LinkedList getChildList() {
            return new LinkedList();
        }

        public Object get(String key) {
            if (key.equals("name")) return name;
            return null;
        }

        public String toString() {return name;}

        public String getType() {return "id";}

        public Object clone() {
            IDNode result = new IDNode(parent);
            result.name = name;
            return result;
        }

        public int hashCode() {return (name==null?0:name.hashCode());}

        public boolean equals(Object object) {
            if (object == null) return false;
            if (!(object instanceof IDNode)) return false;
            IDNode node = (IDNode)object;
            return (name==null?node.name==null:name.equals(node.name));
        }
    }

    public ParseNode VARP(ParseList pl, ParseInfo info) throws ParserException {
        GenToken token = pl.current();
        if (!token.getType().equals("id")) throw new ParserException("identifier expected");
        IDNode result = new IDNode(null);
        result.name = (String)token.get("name");
        pl.moveNext();
        return result;
    }
}