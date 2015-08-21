/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * TermEdge 1.0, October 10, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.GraphAuthor;

/**
 * This class represents an edge in the internal graph.
 */
public class TermEdge {
	public TermVertex source = null; //the source vertex
	public TermVertex target = null; //the target vertex
	public TermType type = null;     //the type of this edge
	public boolean reachable = false;//can this edge be reached by a startable edge

	public TermEdge(TermVertex source, TermVertex target, TermType type) {
		this.source = source;
		this.target = target;
		this.type = type;
	}

	public String toString() {
		return source+" -> ("+type+") "+target;
	}
}