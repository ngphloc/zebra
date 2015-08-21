/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ActionQueue.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.datacomponents;

import java.util.LinkedList;
import java.util.Vector;

/**
 * This is the queue where all new actions are stored in and the next
 * to be executed action can be retrieved.
 */
public class ActionQueue {

    //the actual list of actions
    private LinkedList actionList = null;

    /**
     * Creates a new queue.
     */
    public ActionQueue() {
        actionList = new LinkedList();
    }

    /**
     * Adds the actions to the end of the list.
     */
    public void addActions(Vector actions) {
        actionList.addAll(actions);
    }

    /**
     * Returns whether the queue is empty.
     */
    public boolean isEmpty() {
        return (actionList.size()==0);
    }

    /**
     * Returns the current action and removes it from the list.
     */
    public Action next() {
        return (Action)actionList.removeFirst();
    }
}