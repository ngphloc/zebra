/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ResourceType.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.datacomponents;

/**
 * The class ResourceType is used to store the type of a particular
 * resource. A resource's type can be simply the MIME-type of the
 * resource or it can contain some additional information. This
 * additional information may be used to identify different kinds of
 * XML files for instance.
 */
public class ResourceType implements Cloneable {

    // The MIME type of this resource
    private String mime = null;

    // The sub type of this resource
    private String subtype = null;

    /**
     * Creates a new resource type with the specified MIME type.
     * @param mime The MIME type of this resource type.
     */
    public ResourceType(String mime) {
        this(mime, null);
    }

    /**
     * Creates a new resource type with the specified MIME type
     * and subtype.
     * @param mime The MIME type of this resource type.
     * @param subtype The subtype of this resource type.
     */
    public ResourceType(String mime, String subtype) {
        this.mime = mime;
        this.subtype = subtype;
    }

    /**
     * Returns the MIME type of this resource type.
     * @return The MIME type of this resource type.
     */
    public String getMime() {
        return mime;
    }

    /**
     * Returns the subtype of this resource type.
     * @return The subtype of this resource type.
     */
    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    /**
     * Compares this resource type with another.
     * @param type The <code>ResourceType</code> to compare to.
     * @return If this <code>ResourceType</code> equals <code>type
     *         </code>.
     */
    public boolean equals(ResourceType type) {
        return ((this.subtype.equals(type.getSubtype())) && (this.mime.equals(type.getMime())));
    }

    public Object clone() {
        ResourceType result = new ResourceType(mime, subtype);
        return result;
    }
}
