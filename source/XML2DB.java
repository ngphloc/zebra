/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University
    WOW! is also open source software; 


*/
/* XML2DB.java 1.0, August 30, 2008
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

public class XML2DB {
    private WOWDB msdb = null;
    private WOWDB xmldb = null;

    public XML2DB(String jdbcUrl, String user, String password) throws WOWException {

        // Create both a XML and a database interface
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
        ConceptDB cms = msdb.getConceptDB();
        ConceptDB cxml = xmldb.getConceptDB();

        // Set up some variables
        Vector list = cxml.getConceptList();
        Vector elist = cms.getConceptList();
        String name = null;
        Concept con = null;
        // Run through the list of concepts
        for (int i=0;i<list.size();i++) {
            name = (String)list.get(i);
            // Retrieve a concept
            con = cxml.getConcept(cxml.findConcept(name));
            // Create it if it does not exist, else overwrite
            if (elist.contains(name)) {
                cms.setConcept(cms.findConcept(name), con);
            } else {
                con.id = cms.createConcept(name);
                cms.setConcept(con.id, con);
            }
        }
    }

/*    public static void main(String[] args) throws WOWException {
        new XML2DB();
    }
*/
}