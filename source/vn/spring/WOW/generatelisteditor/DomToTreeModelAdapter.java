/*

    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 

*/
/**
 * DomToTreeModelAdapter.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.generatelisteditor;

// WOW
import vn.spring.WOW.datacomponents.*;

// For creating a TreeModel
import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.*;

import java.util.Vector;

/**
 * This adapter converts the current Document (a DOM) into
 * a JTree model.
 */
public class DomToTreeModelAdapter implements javax.swing.tree.TreeModel
{
	private static final String clist = "Concept List";

	// Basic TreeModel operations
	public Object getRoot()
	{
		return clist;
	}
	public boolean isLeaf(Object aNode)
	{
		return (getChildCount(aNode)==0);
	}
	public int getChildCount(Object parent)
	{
		if (parent instanceof String) return GenerateListData.concepts.size(); else
		if (parent instanceof Concept) return ((Concept)parent).getAttributes().size(); else
			return 0;
	}
	public Object getChild(Object parent, int index)
	{
		if (parent instanceof String) return GenerateListData.concepts.get(index); else
		if (parent instanceof Concept) return ((Concept)parent).getAttributes().get(index); else
			return null;
	}
	public int getIndexOfChild(Object parent, Object child)
	{
		if (parent instanceof String) return GenerateListData.concepts.indexOf(child); else
		if (parent instanceof Concept) return ((Concept)parent).getAttributes().indexOf(child); else
			return -1;
	}
	public void valueForPathChanged(TreePath path, Object newValue)
	{
		// Null. We won't be making changes in the GUI
		// If we did, we would ensure the new value was really new
		// and then fire a TreeNodesChanged event.
	}

	private Vector listenerList = new Vector();
	public void addTreeModelListener( TreeModelListener listener )
	{
		if ( listener != null && ! listenerList.contains( listener ) )
		{
			listenerList.addElement( listener );
		}
	}
	public void removeTreeModelListener( TreeModelListener listener )
	{
		if ( listener != null )
		{
			listenerList.removeElement( listener );
		}
	}
	public void fireTreeNodesChanged( TreeModelEvent e )
	{
		Enumeration listeners = listenerList.elements();
		while ( listeners.hasMoreElements() )
		{
			TreeModelListener listener = (TreeModelListener) listeners.nextElement();
			listener.treeNodesChanged( e );
		}
	}
	public void fireTreeNodesInserted( TreeModelEvent e )
	{
		Enumeration listeners = listenerList.elements();
		while ( listeners.hasMoreElements() )
		{
			TreeModelListener listener = (TreeModelListener) listeners.nextElement();
			listener.treeNodesInserted( e );
		}
	}
	public void fireTreeNodesRemoved( TreeModelEvent e )
	{
		Enumeration listeners = listenerList.elements();
		while ( listeners.hasMoreElements() )
		{
			TreeModelListener listener = (TreeModelListener) listeners.nextElement();
			listener.treeNodesRemoved( e );
		}
	}
	public void fireTreeStructureChanged( TreeModelEvent e )
	{
		Enumeration listeners = listenerList.elements();
		while ( listeners.hasMoreElements() )
		{
			TreeModelListener listener = (TreeModelListener) listeners.nextElement();
			listener.treeStructureChanged( e );
		}
	}

} // DomToTreeModelAdapter