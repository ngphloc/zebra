/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Resource.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.datacomponents;

import vn.spring.WOW.WOWStatic;
import java.io.InputStream;

/**
 * The class Resource contains the data of a resource and a
 * definition of the format of the resource. Furthermore the class
 * has methods that can be used to determine if this resource is
 * ready to be sent to the client. If it is not ready to be sent to
 * the client, this resource needs further processing.
 * @author David Smits, changed by Phuoc-Loc Nguyen
 * @version 1.0
 */

// modification at 29/7: Rob - adding callerID,isFile and turned URL to String
// (to make file access possible)
public class Resource implements Cloneable {

    // The <code>InputStream</code> of data for this resource
    private InputStream data = null;

    // The type of this resource
    private ResourceType type = null;

    // Is this resource ready?
    private boolean ready = false;

    // Is the resource a local file
    // or a URL
    private boolean isfile = false;

    // The original URL/filename of this resource
    private String name = null;

    //The callers identifier, usually a URL f.i.
    // http://localhost:8080/Get/
    // this is usefull to determine the
    private String callerID = null;

    //added by Natalia Stash, 03-04-2008
    private String baseID = null;

    // The name of the concept that resource is associated with
    private String conceptname = null;

    // The object data of this resource
    private Object objectdata = null;

    /**
     * Creates a new resource with the specified data and type.
     * @param data The <code>InputStream</code> from which this
     *        resource should be read.
     * @param type The type of this resource.
     */
    public Resource(InputStream data, ResourceType type) {
        this.data = data;
        this.type = type;
    }

    /**
     * Returns if this resource can be sent to the client or not.
     * @return If this resource can be sent to the client or not.
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Sets if this resource can be sent to the client or not.
     * @param ready If this resource can be sent to the client or not.
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setInputStream(InputStream data) {this.data = data;}
    public void setType(ResourceType type) {this.type = type;}

    /**
     * Sets the original URL of this resource
     */
    public void setURL(String original) {
        this.name = original;
    }

    /**
     * Returns the original URL of this resource
     */
    public String getURL() {
//        System.out.print("getURL():");
        if (isfile) {
           return "file:"+WOWStatic.config.Get("WEBROOT")+name;
        } else {
           return name;
        }
    }

    public String getFakeURL() {
//        System.out.print("getFakeURL():");
        if (isfile) {
           return "file:/"+name;
        } else {
           return name;
        }
    }

    /**
     * Returns the URL OR filename of this resource
     */
    public String getName() {
      return name;
    }
    /**
     * Sets the CallerID of this resource
     */
    public void setCallerID(String caller) {
       callerID=caller;
    }

    /**
     * Gets the CallerID of this resource
     */
    public String getCallerID() {
       return callerID;

    }

    /**
     * Sets whether this is a local file
     */
    public void setIsFile(boolean x) {
       isfile=x;
    }

    /**
     * Gets whether this is a local file
     */

    public boolean isFile() {
       return isfile;
    }
    /**
     * Returns the type of this resource.
     * @return The type of this resource.
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * Returns an inputstream that returns the data for this resource.
     * @return An inputstream that returns the data for this resource.
     */
    public InputStream getInputStream() {
        return data;
    }

    /**
     * Sets the baseID for this resource
     */
    public void setBaseID(String base) {
       baseID=base;
    }

    /**
     * Gets the baseID of this resource
     */
    public String getBaseID() {
       return baseID;

    }

    /**
     * Sets the name of the concept this resource is associated with.
     */
    public void setConceptName(String conceptname) {
        this.conceptname = conceptname;
    }

    /**
     * Gets the name of the concept this resource is associated with.
     */
    public String getConceptName() {
        return conceptname;
    }

    public Object getObjectData() {return objectdata;}
    public void setObjectData(Object objectdata) {this.objectdata = objectdata;}

    public Object clone() {
        ResourceType ntype = (ResourceType)type.clone();
        Resource result = new Resource(data, ntype);
        result.setConceptName(conceptname);
        result.setObjectData(objectdata);
        result.setCallerID(callerID);
        result.setReady(ready);
        result.setIsFile(isfile);
        result.setBaseID(baseID);
        result.setURL(name);
        return result;
    }
}
