/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * TermType 1.0, October 10, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.GraphAuthor;

import java.util.Vector;

/**
 * This represents a type of an edge. The equivelent of a concept
 * relation type in internal format. This is the place where
 * information like the triggerlist and activatorlist is stored.
 */
public class TermType {
	public String name = null;
	public TermMapping mapping = null; //the mapping of this type
	public Vector triggerlist = null;
	public boolean starter = false;   //if this type can start a cycle (access attribute)
	public boolean truereq = false;   //if the requirement of this type is true
	public boolean sourcehalt = false;
	public boolean targethalt = false;
	public Vector haltActivators = new Vector();

	public TermType(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}