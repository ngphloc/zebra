/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Attribute.java 1.0, August 30, 2008
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
 * This class defines a single attribute, including the type and
 * the actions that will be executed when this attribute is changed.
 */
public class Attribute {

    //the name of this attribute
    private String name = null;
    //the description of this attribute
    private String description = null;
    //the default value of this attribute
    private String def = null;
    //the type of this attribute
    private int type = 0;
    //the actions of this attribute
    private Vector actions = null;
    //the readonly flag of this attribute
    private boolean readonly = false;
    //the system flag of this attribute
    private boolean system = false;
    //the persistent flag of this attribute
    private boolean persistent = true;
    //the stable string tells the engine if this attribut must be kept stable
    // and how. Possible values: always,session,freeze
    private String stable = null;
    //the stable expression is only valid if stable is set to freeze
    //it defines the expression which determines if the attribute must be stabe or not
    private String stable_expression = null;

    /**
     * The constant of an string type.
     */
    public final static String STABLE_SESSION = "session";

    /**
     * The constant of a string type.
     */
    public final static String STABLE_ALWAYS = "always";

    /**
     * The constant of a string type.
     */
    public final static String STABLE_FREEZE = "freeze";

    /**
     * Casegroup is a vector with all cases
     */
    private CaseGroup casegroup = null;

    /**
     * Creates a new attribute with the specified name and type.
     */
    public Attribute(String name, int type) {
        this.name = name;
        this.type = type;
        actions = new Vector();
    }

    /**
     * Creates a new attribute with the specified name.
     */
    public Attribute(String name) {
        this(name, AttributeType.ATTRINT);
    }

    /**
     * Returns the name of this attribute
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this attribute
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of this attribute
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this attribute
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the default value of this attribute
     */
    public String getDefault() {
        return def;
    }

    /**
     * Sets the default value of this attribute
     */
    public void setDefault(String def) {
        this.def = def;
    }

    /**
     * Sets the type of this attribute.
     */
    public void setType(/* AttributeType */ int type) {
        this.type = type;
    }

    /**
     * Returns the type of this attribute.
     */
    public /* AttributeType */ int getType() {
        return type;
    }

    /**
     * Returns a list (possibly empty) of actions that are to be
     * executed if this attribute is changed.
     */
    public Vector getActions() {
        return actions;
    }

    /**
     * Added by @Barend
     * Changed by @Loc Nguyen @ 02-12-2008
     * Returns a list (possibly empty) of cases
     */
    public CaseGroup getCasegroup() {
        return casegroup;
    }

    /**
     * Added by @Barend
     * Changed by @Loc Nguyen @ 02-12-2008
     * Returns a list (possibly empty) of cases
     */
    public void setCasegroup(CaseGroup cg) {
      this.casegroup = cg;
    }

    /**
     * Returns whether this is a readonly attribute.
     */
    public boolean isReadonly() {return readonly;}

    /**
     * Sets whether this is a readonly attribute.
     */
    public void setReadonly(boolean readonly) {this.readonly = readonly;}

    /**
     * Returns whether this is a system attribute.
     */
    public boolean isSystem() {return system;}

    /**
     * Sets whether this is a system attribute.
     */
    public void setSystem(boolean system) {this.system = system;}

    /**
     * Returns whether this is a persistent attribute.
     */
    public boolean isPersistent() {return persistent;}

    /**
     * Sets whether this is a persistent attribute.
     */
    public void setPersistent(boolean persistent) {this.persistent = persistent;}

    /*
    * Added by @Bart 1-11-2008
    */

    /**
    * Sets what the stable property of this attribute is. Is optional so it can be null
    */
    public void setStable(String stable) {this.stable = stable;}

    /**
    * Returns what the stable property of this attribute is. Is optional so it can be null
    */
    public String getStable() {return stable;}

    /**
    * Sets what the stable expression property of this attribute is. Is optional so it can be null
    */
    public void setStableExpression(String stable_expression) {this.stable_expression = stable_expression;}

    /**
    * Returns what the stable expression property of this attribute is. Is optional so it can be null
    */
    public String getStableExpression() {return this.stable_expression;}

    /**
    * Returns whether the stable property is set
    */
    public boolean hasStableProperty() {
        if ( (this.stable == null) || (this.stable.equals("") ) ) {
            return false;
        } else {
            // check if the stable string is valid
            if ( this.stable.equals(STABLE_SESSION) || this.stable.equals(STABLE_FREEZE) || this.stable.equals(STABLE_ALWAYS) ) {
                return true;
            } else {
System.out.println("Attribute: Error, The stable property has an invalid value so it is ignored. The value was: '" +stable +"'");
                return false;
            }
        }
    }

    // Added by @Barend, @Loc Nguyen
    /**
    * Returns whether the casgroup node exists
    */
    public boolean hasGroupNode() {
        if (casegroup == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
    * Returns whether the stable_expression property is set .
    */
    public boolean hasStableExpressionProperty() {
        if ( (this.stable_expression == null) || (this.stable_expression.equals("")) ) {
            return false;
        } else {
            return true;
        }
    }

    /**
    * Return whether the stable property is equal to "session" .
    * If the stable property is "session" then this attribute is session stable .
    * @return boolean if it is session stable
    */
    public boolean isSessionStable() {
        // check if it has the stable property
        if (this.hasStableProperty()) {
            // check if it is equal with STABLE_SESSION
            if (this.stable.equals(STABLE_SESSION)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

        /**
        * Return whether the stable property is equal to "freeze" .
        * If the stable property is "freeze" then this attribute is freeze stable .
        * @return boolean if it is freeze stable
        */
        public boolean isFreezeStable() {
                // check if it has the stable property
                if (this.hasStableProperty()) {
                        // check if it is equal with STABLE_FREEZE
                        if (this.stable.equals(STABLE_FREEZE)) {
                                // check if the stable_expr is there
                                String expr = this.getStableExpression();
                                // check if the expr is null or empty, if so then report this
                                // and ignore this stability because it is not valid then
                                if ( (expr == null) || (expr.equals("") ) ) {
                                  System.out.println("Attribute: isFreezeStable:4: The attribute: " +this.name
                                      +"' is INVALID because the stable_expr field is empty or null."
                                      +" This attribute is now always kept stable");
                                  return false;
                                } else return true;
                        } else {
                                return false;
                        }
                } else {
                        return false;
                }
    }

    /*
    * end Added by @Bart 1-11-2008
    */
    /**
     * Returns a string representation of this attribute.
     */
    public String toString() {
        return name;
    }

    /**
     * Returns a copy of this Attribute.
     */
    public Attribute copy(String source, String dest) {
        Attribute result = new Attribute((name==null?null:name.replaceAll(source, dest)), type);
        result.setDescription(description);
        result.setDefault((def==null?null:def.replaceAll(source, dest)));
        result.setReadonly(readonly);
        result.setSystem(system);
        result.setPersistent(persistent);
        result.setStable(stable);
        result.setStableExpression((stable_expression==null?null:stable_expression.replaceAll(source, dest)));
        if (casegroup!=null) result.setCasegroup(casegroup.copy(source, dest));
        Action action;
        for (int i=0;i<actions.size();i++) {
            action = (Action)actions.get(i);
            result.getActions().add(action.copy(source, dest));
        }
        return result;
    }
}