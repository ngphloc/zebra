/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * VariableFilter.java 1.0, June 1, 2008
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
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.*;
import java.util.*;
import org.w3c.dom.*;

public class VariableFilter {
    public final static String USERNAMESTRING = "username";
    public final static String LOGINIDSTRING = "loginid";
    public final static String EMAILSTRING = "email";
    public final static String TITLESTRING = "coursename";
    public final static String COURSESTRING = "course";
    public final static String NUMBERDONESTRING = "numberdone";
    public final static String NUMBERTODOSTRING = "numbertodo";

    private Profile profile;
    private String course;

    public VariableFilter(Profile profile, Node node, String course) {

    this.profile = profile;
    this.course = course;

    }
    public StringBuffer Filter(Node node) {
        StringBuffer sb = new StringBuffer();
        NodeList nodelist = node.getChildNodes();
        try {
            //sb.append(deriveVariable(nodelist.item(1).getNodeName()));
            for (int j=0; j<nodelist.getLength();j++) {
                    sb.append(deriveVariable(profile, course, nodelist.item(j).getNodeName()));
            }
        } catch (Exception e) { System.out.println(e); }
        return sb;
    }

    public static String deriveVariable(Profile profile, String course, String var) throws InvalidAttributeException {
        String name = "";
        if (var.equals(USERNAMESTRING)) {
            try {
                name = profile.getAttributeValue("personal", "name");
            } catch (InvalidAttributeException e) { ; }
            if (name == null || name.equals(""))
                name = "Unknown user";
        }
        if (var.equals(LOGINIDSTRING)) name = profile.getAttributeValue("personal", "id");
        if (var.equals(EMAILSTRING)) {
            try {
                name = profile.getAttributeValue("personal", "email");
            } catch (InvalidAttributeException e) { ; }
            if (name == null || name.equals(""))
                name = "no email address";
        }
        if (var.equals(TITLESTRING)) {
            try {
                name = profile.getAttributeValue("personal", "title");
            } catch (InvalidAttributeException e) { ; }
            if (name == null || name.equals(""))
                name = "unknown title";
        }
        if (var.equals(COURSESTRING)) name = course;
        if (var.equals(NUMBERDONESTRING)) {

            try {
            name = getNumber(profile, course, "done");
            if (name.equals("1")) name = name + " page";
            else name = name + " pages";
            } catch(Exception e) {System.out.println("Exception getNumberDone");}
        }

        if (var.equals(NUMBERTODOSTRING)) {
            try {
            name = getNumber(profile, course, "todo");
            if (name.equals("1")) name = name + " page";
            else name = name + " pages";
            } catch(Exception e) {System.out.println("Exception getNumberTodo");}
        }
        return name;
    }

    public static String getNumber(Profile profile, String course, String pages) throws DatabaseException, InvalidConceptException, InvalidAttributeException {

        int NUM = 0;
        int ND = 0;

        WOWDB db = WOWStatic.DB();
        ConceptDB cdb = db.getConceptDB();

        Hashtable values = profile.getValues();
        Vector concepts = cdb.getConceptList();
        for (int i = 0; i < concepts.size(); i++) {
            String conceptname = (String) concepts.get(i);
            if (conceptname.substring(0, conceptname.indexOf(".")).equals(course)) {
            	Vector attributes = cdb.getAttributes(cdb.findConcept(conceptname));
            	for (int j=0;j<attributes.size();j++) {
            	    Attribute attr = (Attribute)attributes.get(j);
            	    if (attr.getName().equals("visited")) {
						Concept concept = cdb.getConcept(cdb.findConcept(conceptname));
						if (concept.getType().equals("page")) {
							NUM++;
							if (values.containsKey(conceptname+".visited")) {
								if (Integer.parseInt(profile.getAttributeValue(conceptname, "visited")) > 0) ND++;
							}
						}
					}

            	}
			}
        }
        if (pages.equals("done")) return String.valueOf(ND);
        else return String.valueOf(NUM-ND);
    }
}
