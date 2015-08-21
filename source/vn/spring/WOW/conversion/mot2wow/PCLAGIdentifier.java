/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * PCLAGIdentifier.java 1.0, November 23, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.conversion.mot2wow;

import vn.spring.WOW.genparser.*;

import java.util.*;

/**
 * This parse component has only got a scanning part. It scans for
 * identifiers as they are used in the LAG language. These
 * identifiers can contain spaces when thay are accompanied by single
 * quotes.
 */
public class PCLAGIdentifier implements ParseComponent {
    public PCLAGIdentifier() {}

    public String[] getParseMethods() {return new String[] {"VARP"};}

    /**
     * Scans the string to see if the first token in the string is
     * handled by this component.
     */
    public GenToken scan(ParseString ps, GenToken token) throws ParserException {
        if (token != null) return token; //if a token is already found, do nothing with it
        char ch = ps.nextChar();
        if ((Character.isJavaIdentifierStart(ch)) || (ch == '\'')) {
            GenToken result = new GenToken("id");
            StringBuffer idname = new StringBuffer();
            while ( (Character.isJavaIdentifierPart(ch)) || (ch == '.') || (ch == '\'') || (ch == '[') || (ch == ']')) {
                if (ch == '\'') {
                    ch = ps.nextChar();
                    while (ch != '\'') {
                        idname.append(ch);
                        ch = ps.nextChar();
                    }
                    ch = ps.nextChar();
                } else if (ch == '[') {
                    idname.append(ch);
                    while (ch != ']') {
                        ch = ps.nextChar();
                        idname.append(ch);
                    }
                    ch = ps.nextChar();
                } else {
                    idname.append(ch);
                    ch = ps.nextChar();
                }
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