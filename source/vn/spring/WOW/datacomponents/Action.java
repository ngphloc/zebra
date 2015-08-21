/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Action.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.datacomponents;

import java.util.Vector;

/**
 * This class defines a single action, containing a condition, a set
 * of statements if the condition is true and a set of statements if
 * it is false.
 */
public class Action {

    //the actual condition of this action
    private String expr = null;
    //the list of statements that should be executed when the condition is true
    private Vector statTrue = null;
    //the list of statements that should be executed when the condition is false
    private Vector statFalse = null;
    //the trigger flag
    private boolean trigger = true;

    /**
     * Creates a new action.
     */
    public Action() {
        statTrue = new Vector();
        statFalse = new Vector();
    }

    /**
     * Sets the condition part of this action.
     */
    public void setCondition(String expr) {
        this.expr = expr;
    }

    /**
     * Returns the condition part of this action.
     */
    public String getCondition() {
        return expr;
    }

    /**
     * Sets the trigger flag.
     */
    public void setTrigger(boolean trigger) {this.trigger = trigger;}

    /**
     * Returns the trigger flag.
     */
    public boolean getTrigger() {return trigger;}

    /**
     * Returns a list of actions that should be executed if the
     * condition is true.
     */
    public Vector getTrueStatements() {return statTrue;}

    /**
     * Returns a list (possibly empty) of actions that should be
     * executed of the condition is false.
     */
    public Vector getFalseStatements() {return statFalse;}

    /**
     * Returns a string representation of this Action.
     */
    public String toString() {
        return expr;
    }

    /**
     * Returns a copy of this Action.
     */
    public Action copy(String source, String dest) {
        Action result = new Action();
        result.setCondition((expr==null?null:expr.replaceAll(source, dest)));
        Assignment assign;
        for (int i=0;i<statTrue.size();i++) {
            assign = (Assignment)statTrue.get(i);
            result.getTrueStatements().add(assign.copy(source, dest));
        }
        for (int i=0;i<statFalse.size();i++) {
            assign = (Assignment)statFalse.get(i);
            result.getFalseStatements().add(assign.copy(source, dest));
        }
        result.setTrigger(trigger);
        return result;
    }
}