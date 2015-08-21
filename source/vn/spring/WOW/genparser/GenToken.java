/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * GenToken.java 1.0, November 23, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;

import java.util.Hashtable;

/**
 * Represents a token in the general parser environment.
 */
public class GenToken {
    private String type = null;
    private Hashtable props = new Hashtable();

    // used in error reporting
    protected int lcount = 0;
    protected int ccount = 0;

    /**
     * Creates a new token of the specified type. To avoid confusion
     * types should be uniquely identifyable by their 'type' string.
     * @param type a string identifying the type of this token
     */
    public GenToken(String type) {
        this.type = type;
    }

    /**
     * Returns a string identifying the type of this token.
     * @return the type of this token
     */
    public String getType() {return type;}

    /**
     * Adds a property to this token. For instance the name of an
     * identifier if this token represents an identifier.
     * @param key the name of the property
     * @param o the value of the property
     */
    public void add(String key, Object o) {
        props.put(key, o);
    }

    /**
     * Returns a property of this token. If the property does not
     * exist, <code>null</code> is returned.
     * @param key the name of the property
     * @return the value of the property or <code>null</code> if it
     *         could not be found
     */
    public Object get(String key) {
        return props.get(key);
    }

    public String toString() {
        return "<"+type+", "+props+">";
    }
}