/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * WOWDB.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.WOWDB;

import vn.spring.WOW.WOWStatic;

/**
 * This is the main class that can be used to access the database.
 * It contains all the methods to retrieve and store information
 * and to register means of storage. Modified by Loc Nguyen 2009
 */
public class WOWDB {

    //a reference to a ProfileDB instance
    private ProfileDB profiledb = null;
    //a reference to a ConceptDB instance
    private ConceptDB conceptdb = null;
    // a reference to a LogDB instance
    private LogDB logdb = null;

    /**
     * Creates a new WOWDB class and registers the specified
     * components.
     */
    public WOWDB(ConceptDB concept, ProfileDB profile, LogDB log) {
        WOWStatic.checkNull(concept, "concept");
        WOWStatic.checkNull(profile, "profile");
        WOWStatic.checkNull(log, "log");
        conceptdb = concept;
        profiledb = profile;
        logdb = log;
    }

    /**
     * Registers another ConceptDB class.
     */
    public void registerConceptDB(ConceptDB concept) {
        WOWStatic.checkNull(concept, "concept");
        conceptdb = concept;
    }

    /**
     * Registers another UserDB class.
     */
    public void registerProfileDB(ProfileDB profile) {
        WOWStatic.checkNull(profile, "profile");
        profiledb = profile;
    }

    /**
     * Registers another LogDB class.
     */
    public void registerLogDB(LogDB log) {
        WOWStatic.checkNull(log, "log");
        logdb = log;
    }

    /**
     * Returns the registered UserDB.
     */
    public ProfileDB getProfileDB() {return profiledb;}

    /**
     * Returns the registered ConceptDB.
     */
    public ConceptDB getConceptDB() {return conceptdb;}

    /**
     * Returns the registered LogDB.
     */
    public LogDB getLogDB() {return logdb;}
}