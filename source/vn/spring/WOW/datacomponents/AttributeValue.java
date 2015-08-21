/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AttributeValue.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.datacomponents;

/**
 * This class holds the value of a particular attribute.
 */
public class AttributeValue {

    //the new flag
    private boolean isnew = false;
    //the updated flag
    private boolean updated = false;
    //the actual value as a string
    private String value = null;
    // indicates whether the attribute is updated for the first time or not
    private boolean firsttimeupdated = false;

    /**
     * Creates a new attribute value based on the specified type,
     * whether this value is new in the profile and whether it is
     * persistent.
     */
    public AttributeValue(boolean isnew) {
        this.isnew = isnew;
        if (isnew == true) updated = true;
    }

    /**
     * Creates a new attribute value based on the specified type
     * and whether it is persistent.
     */
    public AttributeValue() {
        this(false);
    }

    /**
     * Sets the value of this attribute.
     */
    public void setValue(String value) {
        if (!value.equals(this.value)) {
            this.value = value;
            updated = true;
        }
    }

    /**
     * Returns the value of this attribute.
     */
    public String getValue() {return value;}

    /**
     * Returns whether this is a new attribute value.
     */
    public boolean isNew() {return isnew;}

    /**
     * Returns whether this is an updated attribute value.
     */
    public boolean isUpdated() {return updated;}

    /**
     * Clears the 'new' flag.
     */
    public void clearNew() {isnew = false;}

    /**
     * Clears the 'updated' flag.
     */
    public void clearUpdated() {updated = false;}

    /**
     * Return whether the attributeValue is updated for the first time.
     * If the property firsttimeupdated is true then the attribute value is
     * already updated for the first time.
     * @return boolean if it is first time updated
     */
    public boolean isFirstTimeUpdated() {return firsttimeupdated;}

    /**
     * Sets the property firsttimeupdated of the attributeValue to indicate that it is updated for the first time.
     * @param firsttimeupdated if it is first time updated
     */
    public void setFirstTimeUpdated(boolean firsttimeupdated) {
        this.firsttimeupdated = firsttimeupdated;
    }

    /**
     * Sets the 'new' flag (and the 'updated' flag).
     */
    public void setNew() {isnew = true; updated = true;}
}