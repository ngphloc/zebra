/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ProfileDB.java 1.0, June 1, 2008
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
 * user profile storage means.
 */
public interface ProfileDB {
    /**
     * Creates a new user profile and returns the id.
     */
    public long createProfile() throws
           DatabaseException;

    /**
     * Loads a user profile with the specified id.
     */
    public Profile getProfile(long id) throws
           DatabaseException,
           InvalidProfileException;

    /**
     * Saves a user profile with the specified id.
     */
    public void setProfile(long id, Profile profile) throws
           DatabaseException,
           InvalidProfileException;

    /**
     * Finds all profiles that match the login return the id.
     */
    public long findProfile(String value) throws
           DatabaseException;

    /**
     * Return the list of all profile id's
     */
    public Vector getProfileList() throws
           DatabaseException;

    /**
     * Removes the user profile with the specified id.
     */
    public void removeProfile(long id) throws
           DatabaseException;
}