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

/**
 * Halt activators are edges that are either an activatoredge or an
 * anti-activatoredge. Only haType's 1 and 3 are used.
 */
public class TermHaltActivator {
	public TermType type = null;
	public int haType = 0; //0 = no type; 1 = immediate activator; 2 = cycle activator; 3 = anti activator

	public TermHaltActivator(TermType type) {this.type = type;}
}