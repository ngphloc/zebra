/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Assignment.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.datacomponents;

/**
 * This class defines a single assignment from one expression to a
 * variable.
 */
public class Assignment {

    //the name of the variable to set
    private String variable = null;
    //the expression that represents the new value of this variable
    private String expr = null;

    /**
     * Creates a new statement with the specified expression that
     * should be assigned to the specified variable.
     */
    public Assignment(String variable, String expr) {
        setVariable(variable);
        setExpression(expr);
    }

    /**
     * Sets the variable that should be assigned by this assignment.
     */
    public void setVariable(String variable) {
        this.variable = variable;
    }

    /**
     * Returns the variable that should be assigned by this assignment.
     */
    public String getVariable() {return variable;}

    /**
     * Sets the expression that should be assigned to a variable by
     * this assignment.
     */
    public void setExpression(String expr) {
        this.expr = expr;
    }

    /**
     * Returns the expression that should be assigned to a variable
     * by this assigment.
     */
    public String getExpression() {return expr;}

    /**
     * Returns a string representation of this assignment.
     */
    public String toString() {
        return variable+"="+expr;
    }

    /**
     * Returns a copy of this Assignment.
     */
    public Assignment copy(String source, String dest) {
        return new Assignment((variable==null?null:variable.replaceAll(source, dest)), (expr==null?null:expr.replaceAll(source, dest)));
    }
}