/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Termination 1.0, October 10, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.GraphAuthor;

/**
 * This class maps the concept relation types used in the graph
 * author to the types used in the internal graph by the termination
 * algorithm.
 */
public class TermMapping {
	public int source; //0 is source, 1 is destination
	public int target; //0 is source, 1 is destination
	public String name;
	public String req;        //the requirement of this mapping
	public String actionattr; //the concept+attribute of the action of this mapping
	public String actionexpr; //the expression of the action of this mapping

	public TermMapping(String name) {this.name = name;}

	public String toString() {return name;}
}