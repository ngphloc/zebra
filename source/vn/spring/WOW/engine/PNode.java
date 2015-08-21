/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * PNode.java 1.0, November 23, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import vn.spring.WOW.parser.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.exceptions.*;

/**
 * This class represents one node (concept) in the hierarchy tree,
 * while taking into account the 'order' attribute of the user
 * profile.
 */
public class PNode {
    private TNode thisnode = null;
    private PNode anextSib = null;
    private PNode afirstChild = null;
    private PNode aparent = null;
    private boolean defined = false;
    private Profile profile = null;

    public PNode(TNode thisnode, Profile profile) {
        this.thisnode = thisnode;
        this.profile = profile;
    }

    public PNode getPreviousSib() {
    	PNode parent = getParent();
    	if(parent == null) return null;
    	PNode child = parent.getFirstChild();
    	if(child == null) return null;
    	
    	if(child.getName().equals(this.getName())) return null;
    	
    	while(child != null) {
    		PNode nextchild = child.getNextSib();
    		if(nextchild.getName().equals(this.getName())) return child;
    		child = nextchild;
    	}
    	return null;
    }
    public PNode getNextSib() {
        if (!defined) define();
        return anextSib;
    }

    public PNode getFirstChild() {
        if (!defined) define();
        return afirstChild;
    }

    public PNode getParent() {
        if (!defined) define();
        return aparent;
    }

    public String getName() {
        return thisnode.getName();
    }

    public String getType() {
        return thisnode.getType();
    }

    public TNode getTNode() {return thisnode;}

    public boolean isDecendentOf(PNode parent) {
    	if(parent == null) return false;
    	PNode child = parent.getFirstChild();
    	while(child != null) {
    		if(child.getName().equals(this.getName())) return true;
    		if(isDecendentOf(child)) return true;
    		child = child.getNextSib();
    	}
    	return false;
    }
    private void define() {
        //variable locator
        UMVariableLocator umvl = WOWStatic.createUMVariableLocator(profile);

        //parent
        aparent = pnode(thisnode.getParent());

        //nextsib
        int thisorder = order(thisnode, umvl);
        if (thisorder == 0) {
            //return the first next sibling with order 0
            TNode next = nextNode(thisnode, umvl);
            while ((next != null) && (order(next, umvl) != 0)) next = nextNode(next, umvl);
            if (next == null) anextSib = null; else anextSib = pnode(next);
        } else {
            //return the node in this subtree that has the next order value
            if (thisnode.getParent() == null) anextSib = null; else {
                //this node is not the root node
                //start with the first node in this subtree
                TNode node = thisnode.getParent().getFirstChild(); if (!allowedNode(node, umvl)) node = nextNode(node, umvl);
                TNode first = null;
                TNode best = null; int bestorder = Integer.MAX_VALUE;
                boolean pastcurrent = false;
                while ((node != null) && (anextSib == null)) {
                    int order = order(node, umvl);
                    if ((pastcurrent) && (order == thisorder)) anextSib = pnode(node); else {
                        if (order != 0) {
                            if ((order > thisorder) && (order < bestorder)) {
                                best = node;
                                bestorder = order;
                            }
                        } else if (first == null) first = node;
                    }
                    if (node == thisnode) pastcurrent = true;
                    node = nextNode(node, umvl);
                }
                if (anextSib == null) {
                    if (best == null) anextSib = pnode(first); else anextSib = pnode(best);
                }
            }
        }

        //firstchild
        TNode node = thisnode.getFirstChild(); if (!allowedNode(node, umvl)) node = nextNode(node, umvl);
        TNode best = null; int bestorder = Integer.MAX_VALUE;
        TNode first = null;
        while (node != null) {
            int order = order(node, umvl);
            if (order != 0) {
                if (order < bestorder) {
                    bestorder = order;
                    best = node;
                }
            } else if (first == null) first = node;
            node = node.getNextSib();
        }
        if (best == null) afirstChild = pnode(first); else afirstChild = pnode(best);
    }

    private PNode pnode(TNode node) {
        if (node == null) return null; else return new PNode(node, profile);
    }

    //returns the order of the concept named 'cname'
    private int order(TNode node, VariableLocator vl) {
        if (node == null) return 0;
        try {
            Object o = vl.getVariableValue(node.getName()+".order");
            if (!(o instanceof Float)) return 0;
            return ((Float)o).intValue();
        } catch (ParserException e) {
            return 0;
        }
    }

    private TNode nextNode(TNode node, VariableLocator vl) {
        if (node == null) return null;
        TNode result = node.getNextSib();
        while ((result != null) && (!allowedNode(result, vl))) result = result.getNextSib();
        return result;
    }

    private boolean allowedNode(TNode node, VariableLocator vl) {
        if (node == null) return false;
        boolean result = !node.getType().equals("fragment");
        try {
            Object o = vl.getVariableValue(node.getName()+".hierarchy");
            if (o instanceof Float) result = result && (((Float)o).intValue() > 0);
            if (o instanceof Boolean) result = result && ((Boolean)o).booleanValue();
            return result;
        } catch (ParserException e) {
            return result;
        }
    }
}