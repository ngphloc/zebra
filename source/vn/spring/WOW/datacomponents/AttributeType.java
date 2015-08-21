/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AttributeType.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.datacomponents;

/**
 * This class is used to indicate the type of an attribute.
 */
public class AttributeType {

    /**
     * The constant of an integer type.
     */
    public final static int ATTRINT = 1;

    /**
     * The constant of a string type.
     */
    public final static int ATTRSTR = 2;

    /**
     * The constant of a boolean type.
     */
    public final static int ATTRBOOL = 3;

    /**
     * Creates a new attribute type.
     */

    /**
     * Returns a string representation of an AttributeType.
     */

    public static String toString(int type) {

        String result = "invalid";

        if (type == 1) result = "integer";
        if (type == 2) result = "string";
        if (type == 3) result = "boolean";
        return result;
    }

}