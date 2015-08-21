/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ParseNode.java 1.0, December 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;

import java.util.LinkedList;

/**
 * Represents a single node in the parse tree.
 */
public abstract class ParseNode implements Cloneable {
    protected ParseNode parent = null;

    /**
     * Creates a new parse node with the specified parent. Parent may
     * be null if this is the root node in the tree.
     */
    public ParseNode(ParseNode parent) {
        this.parent = parent;
    }

    public abstract LinkedList getChildList();

    public ParseNode getParent() {
        return parent;
    }

    public void setParent(ParseNode parent) {
        this.parent = parent;
    }

    public String toString() {
        String result = "{";
        LinkedList childlist = getChildList();
        for (int i=0;i<childlist.size();i++) result += childlist.get(i).toString();
        result += "}";
        return result;
    }

    public String getType() {return "abstract";}

    public abstract Object get(String key);

    public abstract Object clone();

    public abstract int hashCode();

    public abstract boolean equals(Object object);
}