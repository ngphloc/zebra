/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ParseInfo.java 1.0, November 23, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;

import java.util.*;

/**
 * This class contains information about the current general parser.
 * It contains a list of all registered parse components and all
 * associated recursive decent parsing methods.
 */
public class ParseInfo {
    // list of registered parse components
    private LinkedList parsecomponents = new LinkedList();
    // list of recursive decent parsing methods and there respective PCs
    private Hashtable parsemethods = new Hashtable();

    public ParseInfo() {}

    public void registerParseComponent(ParseComponent pc) {
        String[] methods = pc.getParseMethods();
        for (int i=0;i<methods.length;i++) {
            parsemethods.put(methods[i], pc);
        }
        parsecomponents.add(pc);
    }

    public LinkedList getParseComponents() {return parsecomponents;}

    /**
     * Scans the string to see if the first token in the string is
     * handled by any registered component.
     */
    public GenToken scan(ParseString ps) throws ParserException {
        GenToken token = null;
        ListIterator li = parsecomponents.listIterator(0);
        while (li.hasNext()) {
            ParseComponent pc = (ParseComponent)li.next();
            token = pc.scan(ps, token);
        }
        return token;
    }

    /**
     * Tries to find the specified recursive decent parsing method to
     * handle the first tokens in the list.
     */
    public ParseNode parse(String method, ParseList pl) throws ParserException {
        ParseComponent pc = (ParseComponent)parsemethods.get(method);
        if (pc == null) throw new ParserException("no such method '"+method+"'");
        return pc.parse(method, pl, this);
    }

    /**
     * Returns wether the specified parse method exists.
     */
    public boolean existsMethod(String method) {
        return parsemethods.containsKey(method);
    }
}