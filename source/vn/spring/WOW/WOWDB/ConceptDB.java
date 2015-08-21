/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ConceptDB.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.WOWDB;

import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;

import java.util.Vector;

/**
 * This interface must be implemented by a class to function as a
 * concept storage means.
 */
public interface ConceptDB {
    /**
     * Creates a concept with the specified name and returns the id.
     */
    public long createConcept(String name) throws
           DatabaseException;

    /**
     * Returns the id of the specified concept.
     */
    public long findConcept(String name) throws
           DatabaseException,
           InvalidConceptException;

    /**
     * Links the specified concept to the specified resource.
     * Concepts and resources may be linked only once.
     */
    public void linkResource(long id, String resource) throws
           InvalidConceptException,
           DatabaseException;

    /**
     * Removes a link from the specified concept to a resource.
     */
    public void unlinkResource(long id) throws
           InvalidConceptException,
           DatabaseException;

    /**
     * Returns the resource that is linked to the specified concept.
     */
    public String getLinkedResource(long id) throws
           InvalidConceptException,
           DatabaseException;

    /**
     * Returns the concept that is linked to the specified resource.
     */
    public String getLinkedConcept(String resource) throws
           DatabaseException;

    /**
     * Sets the specified attribute.
     */
    public void setAttribute(long id, Attribute attr) throws
           InvalidConceptException,
           DatabaseException;

    /**
     * Returns the specified attribute.
     */
    public Attribute getAttribute(long id, String name) throws
           InvalidConceptException,
           DatabaseException,
           InvalidAttributeException;

    /**
     * Removes the specified attribute.
     */
    public void removeAttribute(long id, String name) throws
           InvalidConceptException,
           DatabaseException;

    /**
     * Returns the list of names of all attributes of a concept.
     */
    public Vector getAttributeNames(long id) throws
           InvalidConceptException,
           DatabaseException;

    /**
     * Returns all attributes of a concept.
     */
    public Vector getAttributes(long id) throws
           InvalidConceptException,
           DatabaseException;

    /**
     * Sets all attributes of a concept.
     */
    public void setAttributes(long id, Vector attrs) throws
           InvalidConceptException,
           DatabaseException;

    /**
     * Saves a concept with the specified id.
     */
    public void setConcept(long id, Concept concept) throws
           InvalidConceptException,
           DatabaseException;

    /**
     * Loads a concept with the specified id.
     */
    public Concept getConcept(long id) throws
           InvalidConceptException,
           DatabaseException;

    /**
     * Return the list of all concepts.
     */
    public Vector getConceptList() throws
           DatabaseException;

    /**
     * Delete a concept.
     */
    public void removeConcept(long id) throws
           InvalidConceptException,
           DatabaseException;
}