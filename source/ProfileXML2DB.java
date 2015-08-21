/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ProfileXML2DB.java 1.0, August 30, 2008
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
import java.util.Enumeration;
import java.io.File;

public class ProfileXML2DB {
    private WOWDB msdb = null;
    private WOWDB xmldb = null;

    public ProfileXML2DB(String jdbcUrl, String user, String password) throws WOWException {

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
        ProfileDB pms = msdb.getProfileDB();
        ProfileDB pxml = xmldb.getProfileDB();

        // Set up some variables
        Vector list = pxml.getProfileList();
        Vector elist = pms.getProfileList();
        String login = null;
        Profile prof = null;
        Enumeration keys; String key;
        // Run through the list of profiles
        for (int i=0;i<list.size();i++) {
            login = (String)list.get(i);
            // Retrieve a profile
	    long id;
            id = pxml.findProfile(login);
	    if (id == 0)
	    	throw new WOWException("Cannot find id in XML profile database.");
            prof = pxml.getProfile(id);
            // Set the new flag for every attribute
            for (keys=prof.getValues().keys();keys.hasMoreElements();) {
                key = (String)keys.nextElement();
                ((AttributeValue)prof.getValues().get(key)).setNew();
            }
            // If it exists remove it, then create the new profile
            if (elist.contains(login)) {
                System.out.println("overwriting..."+new Long(prof.id).toString()+" "+login);
                id = pms.findProfile(login);
		if (id == 0)
		    throw new WOWException("Cannot find id in XML profile database.");
                pms.removeProfile(id);
            }
            prof.id = pms.createProfile();
            pms.setProfile(prof.id, prof);
        }
    }

    // Main method for stand-alone testing purposes.
    public static void main(String[] args) throws WOWException {
        new ProfileXML2DB("jdbc:mysql://localhost/wow","wowuser","wowpassword");
    }

}

