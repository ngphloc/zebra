/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * TermVertex 1.0, October 10, 2008
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
 * This class represents a single vertex in the internal graph. It
 * also keeps track of edges in and out of this vertex for quick
 * reference. Don't modify these vectors directly, but use
 * TermModel.addEdge and TermModel.removeEdge.
 */
public class TermVertex {
	public String name = null;
	public Vector edgesOut = new Vector();
	public Vector edgesIn = new Vector();

	public TermVertex(String name) {this.name = name;}

	public String toString() {return name;}
}
