/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/

/**
 * DomToTreeModelAdapter.java 1.0, August 30, 2008.
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.formeditor;


import java.util.*;

import javax.swing.event.*;

// For creating a TreeModel
import javax.swing.tree.*;


/**
 * This adapter converts the current Document (a DOM) into
 * a JTree model.
 */
public class DomToTreeModelAdapter implements javax.swing.tree.TreeModel {
    private Vector listenerList = new Vector();

    // Basic TreeModel operations
    public Object getRoot() {
        //System.err.println("Returning root: " +document);
        return new AdapterNode(GenerateListData.getDocument());
    }

    public boolean isLeaf(Object aNode) {
        // Determines whether the icon shows up to the left.
        // Return true for any node with no children
        AdapterNode node = (AdapterNode) aNode;

        if (node.childCount() > 0) {
            return false;
        }

        return true;
    }

    public int getChildCount(Object parent) {
        AdapterNode node = (AdapterNode) parent;

        return node.childCount();
    }

    public Object getChild(Object parent, int index) {
        AdapterNode node = (AdapterNode) parent;

        return node.child(index);
    }

    public int getIndexOfChild(Object parent, Object child) {
        AdapterNode node = (AdapterNode) parent;

        return node.index((AdapterNode) child);
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        // Null. We won't be making changes in the GUI
        // If we did, we would ensure the new value was really new
        // and then fire a TreeNodesChanged event.
    }

    public void addTreeModelListener(TreeModelListener listener) {
        if ((listener != null) && !listenerList.contains(listener)) {
            listenerList.addElement(listener);
        }
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        if (listener != null) {
            listenerList.removeElement(listener);
        }
    }

    public void fireTreeNodesChanged(TreeModelEvent e) {
        Enumeration listeners = listenerList.elements();

        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.nextElement();
            listener.treeNodesChanged(e);
        }
    }

    public void fireTreeNodesInserted(TreeModelEvent e) {
        Enumeration listeners = listenerList.elements();

        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.nextElement();
            listener.treeNodesInserted(e);
        }
    }

    public void fireTreeNodesRemoved(TreeModelEvent e) {
        Enumeration listeners = listenerList.elements();

        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.nextElement();
            listener.treeNodesRemoved(e);
        }
    }

    public void fireTreeStructureChanged(TreeModelEvent e) {
        Enumeration listeners = listenerList.elements();

        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.nextElement();
            listener.treeStructureChanged(e);
        }
    }
} // DomToTreeModelAdapter