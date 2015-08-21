/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ConceptList.java 1.0, June 1, 2008
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
import com.sun.xml.tree.*;
import java.io.*;
import java.util.*;
import org.w3c.dom.*;

public class ConceptList {

    public ConceptList() {
    }

    public boolean getConceptList(String fileName, String pconcept)
           throws DatabaseException, InvalidConceptException {
        boolean created = true;
        WOWDB db = WOWStatic.DB();
        ConceptDB cdb = db.getConceptDB();
        Vector list = cdb.getConceptList();
        Vector deletelist = new Vector();
        Vector conceptlist = new Vector();
        //create a list of all concepts that should be deleted
        //because they are no longer in the current definition
        for (int i=0;i<list.size();i++) if (((String)list.get(i)).startsWith(pconcept+".")) deletelist.add(list.get(i));
        if (cdb instanceof XMLConceptDB) ((XMLConceptDB)cdb).disableIndexWriting();
        try {
            XmlDocument doc = XMLUtil.getXML(new File(fileName));
            Node node = (Element)doc.getDocumentElement();
            NodeList nodelist = node.getChildNodes();
            for (int i=0; i<nodelist.getLength(); i++) {
                Node child = nodelist.item(i);
                if (child.getNodeName().equals("concept")) {
                    Concept concept = KoenFormat.getKoenConcept(child);
                    if (concept != null) {
                        conceptlist.add(concept);
                        //this concept is still in the definition so don't delete it
                        deletelist.remove(concept.getName());
                    }
                }
            }
            //delete all the concepts not in the definition
            for (int i=0;i<deletelist.size();i++)
                cdb.removeConcept(cdb.findConcept((String)deletelist.get(i)));
            for (int i=0;i<conceptlist.size();i++) {
                Concept concept = (Concept)conceptlist.get(i);
                if (list.contains(concept.getName())) {
                    cdb.setConcept(cdb.findConcept(concept.getName()), concept);
                } else {
                	//Be careful the dot
                	String cname = concept.getName();
                	if(cname.startsWith(pconcept+".")) {
	                    concept.id = cdb.createConcept(cname);
	                    cdb.setConcept(concept.id, concept);
                	}
                }
            }
        } catch (Exception e) {
            System.out.println("ConceptList: getConceptList: " +e);
            created = false;
        }
        if (cdb instanceof XMLConceptDB) ((XMLConceptDB)cdb).enableIndexWriting();
        return created;
    }
}
