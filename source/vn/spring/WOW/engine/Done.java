/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 *Done.java 1.0 June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */


package vn.spring.WOW.engine;

import java.io.*;
import java.util.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.*;

public class Done {

    public Resource getList(Profile profile, String course) throws DatabaseException, InvalidConceptException, InvalidAttributeException {

        WOWDB db = WOWStatic.DB();
        ConceptDB cdb = db.getConceptDB();
        Hashtable done = profile.getValues();
        Vector v = new Vector();
        Enumeration e = done.keys();
        while (e.hasMoreElements()) {
            String key = (String)e.nextElement();
            if (key.endsWith(".visited") && key.substring(0, key.indexOf(".")).equals(course)) {
                String conceptname = key.substring(0,key.indexOf(".visited"));
                if (Integer.parseInt(profile.getAttributeValue(conceptname, "visited")) > 0) {
                    Concept concept = cdb.getConcept(cdb.findConcept(conceptname));
                    if (concept.getType().equals("page")) v.addElement(concept.getResourceURL().toString());
                }
            }

        }
        StringBuffer sb = new StringBuffer();
        // The header generation is "borrowed" from XMLHandler
        sb.append("<html><body>");
        sb.append("<h2>List of pages you have visited</h2><br />\n");
        sb.append("<p>Please note that this list contains pages for which the <em>visited</em> attribute is non-zero.\nWhether that means that you actually visited the pages or not depends on the application.<br />\n");
        Object[] names = WOWStatic.names(v);
        for (int i=0; i<names.length; i++) {
            String line = "<a href="+ cdb.getLinkedConcept(names[i].toString()) + " class=\"conditional\" target=\"MAIN\">" + names[i].toString() + "</a><br />\n";
            sb.append(line);
        }
        sb.append("</p>\n</body>\n</html>\n");
        InputStream insb = new ByteArrayInputStream(sb.toString().getBytes());
        return new Resource(insb, new ResourceType("text/html"));
    }
}
