/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * TermCycleListener 1.0, October 10, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.GraphAuthor;

/**
 * This interface should be implemented by any class that wants to
 * receive an event went the state of the cycle detection algoritm
 * changes. See also Termination.addCycleListener
 */
public interface TermCycleListener {
	public void cycleStateChanged(boolean state);
}
