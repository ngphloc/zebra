/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ConceptXML.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.util;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.config.*;
import com.sun.xml.tree.*;
import java.io.*;
import java.util.*;
import org.w3c.dom.*;

public class ConceptXML {

    public ConceptXML() {
    }

    public boolean createConceptList(String fileName, String pconcept)
           throws DatabaseException, InvalidConceptException {

        String author = "";
        AuthorsConfig aconf= new AuthorsConfig();
        Hashtable authorHash = aconf.AuthorHash;
        for (Enumeration keys = authorHash.keys();keys.hasMoreElements();) {
            String key = (String)keys.nextElement();
            WowAuthor wowAuthor=aconf.GetAuthor(key);
            Vector v = wowAuthor.getCourseList();
            for (int i=0;i<v.size();i++) {
              String course = (String)v.elementAt(i);
              if (course.equals(pconcept)) author = key;
            }
        }
        //Why
        if(author==null) {}
        
        boolean created = true;
        WOWDB db = WOWStatic.DB();
        ConceptDB cdb = db.getConceptDB();
        try {
                XmlDocument doc = new XmlDocument();
                Element econceptlist = doc.createElement("conceptList");
                doc.appendChild(econceptlist);
                Element ename = doc.createElement("name");
                econceptlist.appendChild(ename);
                ename.appendChild(doc.createTextNode("WOW! conceptlist: " + pconcept));
/*
                // changed by @Loc Nguyen @ 07-05-2008
                Element eauthor = doc.createElement("author");
                econceptlist.appendChild(eauthor);
                eauthor.appendChild(doc.createTextNode(author));
                // end changed by @Loc Nguyen @ 07-05-2008
*/
                Vector concepts = cdb.getConceptList();
                for (int i=0;i<concepts.size();i++) {
                    String concname = (String)concepts.get(i);
                    if ( (concname.indexOf(".") == -1) || (concname.startsWith(pconcept+".")) ) {
                        Concept concept = cdb.getConcept(cdb.findConcept(concname));
                        Node node = KoenFormat.getKoenXML(concept, doc);
                        doc.getDocumentElement().appendChild(node);
                    }
                }
                //try to save it to the disk
                PrintWriter pw = new PrintWriter(new FileWriter(fileName));
                doc.setSystemId("conceptList");
                doc.setDoctype(null, "../generatelist4.dtd", null);
                doc.write(pw);
                pw.close();

        } catch (Exception e) {
            System.out.println("ConceptXML: createConceptList: " +e);
            created = false;
        }
        return created;
    }

}
