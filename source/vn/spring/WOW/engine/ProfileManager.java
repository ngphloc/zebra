/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ProfileManager.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.engine;

import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.parser.*;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.bn.ConsiderConceptEvent;
import vn.spring.zebra.bn.ConsiderConceptListener;
import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;
import vn.spring.zebra.helperservice.WOWContextListener;
import vn.spring.zebra.server.TriUMServer;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.um.TriUMHelper;

import java.net.URL;
import java.util.*;

/**
 * This class is used in updating the user profile and in calculating
 * the suitability of concepts, based on a user's profile.
 */
public class ProfileManager {

    //a reference to the database
    private WOWDB db = null;

    /**
     * Creates a new profile manager based on the specified database.
     */
    public ProfileManager(WOWDB db) {
        WOWStatic.checkNull(db, "db");
        this.db = db;
    }

    // modidification URL has become String (29/07/2008, Rob)
    /**
     * This method is called by the get servlet to update the user
     * profile. It creates an instance of the ProfileUpdate class and
     * then updates the access attribute.
     */
    public void accessedResource(Profile profile, String resourceURL) throws
           InvalidAttributeException,
           InvalidProfileException,
           InvalidConceptException,
           DatabaseException,
           ParserException {
        String concept = db.getConceptDB().getLinkedConcept(resourceURL);
        if (concept != null) {
            if (!concept.equals("")) {
                ProfileUpdate update = new ProfileUpdate(profile, this);
                AttributeValue value = new AttributeValue(false);
                value.setValue((new Boolean(true)).toString());
                update.updateAttribute(concept, "access", value);
                purgeProfile(profile);
                db.getProfileDB().setProfile(profile.id, profile);

                //@Loc Nguyen add October 14 2009
                updateBayesianStaticKnowledgeModel(profile, concept);
            }
        }
    }

    /**
     * This method is called by the get servlet to update the user
     * profile. It creates an instance of the ProfileUpdate class and
     * then updates the access attribute.
     */
    public void accessedConcept(Profile profile, String concept) throws
           InvalidAttributeException,
           InvalidProfileException,
           InvalidConceptException,
           DatabaseException,
           ParserException {
        if (concept != null) {
            if (!concept.equals("")) {
                ProfileUpdate update = new ProfileUpdate(profile, this);
                AttributeValue value = new AttributeValue(false);
                value.setValue((new Boolean(true)).toString());
                update.updateAttribute(concept, "access", value);
                purgeProfile(profile);
                db.getProfileDB().setProfile(profile.id, profile);
                
                //@Loc Nguyen add October 14 2009
                updateBayesianStaticKnowledgeModel(profile, concept);
            }
        }
    } 

    private void updateBayesianStaticKnowledgeModel(Profile profile, String concept) {
        //@Loc Nguyen add October 14 2009
        try {
        	QUERY_TYPE evaltype = ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE;
        	if(evaltype == QUERY_TYPE.OVERLAY_BAYESIAN) {
	        	ConsiderConceptEvent evt = new ConsiderConceptEvent(profile, concept);
	        	String userid = profile.getAttributeValue("personal", "id");
	        	String course = profile.getAttributeValue("personal", "course");
	        	WOWContextListener context = WOWContextListener.getInstance();
	        	TriUMServer server = context.getTriUMServer();
	        	TriUM um = server.getUM(userid, course, true);
	        	ConsiderConceptListener listener = 
	        		(ConsiderConceptListener)(new TriUMHelper(um)).getStaticKnowledgeDaemon();
	        	listener.conceptConsidered(evt);
        	}
        }
        catch(Throwable e) {
        	System.out.println("Call Static learning parameters in ProfileManager.accessedConcept causes error " + e.getMessage());
        	e.printStackTrace();
        }
    }
    
    // modidification URL has become String (29/07/2008, Rob)
    /**
     * This method returns the suitability of the resource (if it is
     * associated with a concept).
     */
    public int getSuitability(Profile profile, URL resourceURL) throws
           InvalidConceptException,
           DatabaseException,
           ParserException {
        ConceptDB cdb = db.getConceptDB();
        String conceptname = cdb.getConcept(cdb.findConcept(cdb.getLinkedConcept(resourceURL.toString()))).getName();
        UMVariableLocator umvl = WOWStatic.createUMVariableLocator(profile);
        return ((Float)umvl.getVariableValue(conceptname+".suitability")).intValue();
    }

    /**
     * Returns a reference to the database interface.
     */
    public WOWDB getWOWDB() {return db;}

    /**
     * Removes all non-persistent attributes from the profile.
     */
    public void purgeProfile(Profile profile) {
        Hashtable values = profile.getValues();
        Vector dkeys = new Vector(); //keys to be deleted

        //process all values in the profile
        String key = null;
        for (Enumeration keys=values.keys();keys.hasMoreElements();) {
            key=(String)keys.nextElement();
            ConceptDB cdb = db.getConceptDB();
            Concept c = null;
            Attribute a = null;

            //find the attribute in the concept database with name 'key'
            try {
                AttributeIdentifier aid = new AttributeIdentifier(key);
                c = cdb.getConcept(cdb.findConcept(aid.concept()));
                a = c.getAttribute(aid.attribute());
            } catch (WOWException e) {
            }

            if (a != null) {
                //found the attribute for this value in the profile
                //mark it for deletion if needed
                if (!a.isPersistent()) dkeys.add(key);
            }
        }

        //delete all marked keys from the profile
        for (int i=0;i<dkeys.size();i++) {
            values.remove(dkeys.get(i));
        }
    }
}
