/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ProfileDB2XML.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.config.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.*;
import java.util.Vector;
import java.io.File;

public class ProfileDB2XML {
    private WOWDB msdb = null;
    private WOWDB xmldb = null;

    public ProfileDB2XML(String jdbcUrl, String user, String password) throws WOWException {

        // Create both a XML database and a database server interface
        WowConfig conf = WOWStatic.config;
/*        msdb = new WOWDB(
           new DBConceptDB(
                conf.Get("jdbcUrl"),
                conf.Get("dbuser"),
                conf.Get("dbpassword")),
           new DBProfileDB(
                conf.Get("jdbcUrl"),
                conf.Get("dbuser"),
                conf.Get("dbpassword"))
         );
*/
        msdb = new WOWDB(
           new DBConceptDB(jdbcUrl, user, password),
           new DBProfileDB(jdbcUrl, user, password),
           new DBLogDB(jdbcUrl, user, password));
        File xmlroot = new File(conf.Get("XMLROOT"));
        xmldb = new WOWDB(new XMLConceptDB(xmlroot),
                          new XMLProfileDB(xmlroot),
                          new XMLLogDB(xmlroot));
        ProfileDB pxml = xmldb.getProfileDB();
        ProfileDB pms = msdb.getProfileDB();

        // Set up some variables
        Vector list = pms.getProfileList();
        Vector elist = pxml.getProfileList();
        String login = null;
        Profile prof = null;
        // Run through the list of profiles
        for (int i=0;i<list.size();i++) {
            login = (String)list.get(i);
            // Retrieve a profile
	    long id = pms.findProfile(login);
	    if (id == 0)
	    	// profile does not exist in database
	    	throw new WOWException("Cannot find profile in database server.");
	    prof = pms.getProfile(id);
	    // Create it if it does not exist, else overwrite
	    if (elist.contains(login)) {
	      id = pxml.findProfile(login);
	      if (id == 0)
	    	// profile does not exist in database
	    	throw new WOWException("Cannot find profile in XML database.");
	      pxml.setProfile(id, prof);
	    } else {
	      prof.id = pxml.createProfile();
	      pxml.setProfile(prof.id, prof);
	    }
        }
    }

    // Following is for stand-alone test purposes
    public static void main(String[] args) throws WOWException {
        new ProfileDB2XML("jdbc:mysql://localhost/wow", "wowuser", "wowpassword");
    }

}
