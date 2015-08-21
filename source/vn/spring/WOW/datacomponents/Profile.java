/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Profile.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.datacomponents;

import vn.spring.WOW.exceptions.*;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * This class defines the user profile. It actually is nothing more
 * then a large collection of name value pairs. Reviewed by Loc Nguyen 2008
 */
public class Profile {

    //the actual table of fields
    private Hashtable values = null;
//    private Interpreter interpreter = null;

    /** The database identifier of this profile */
    public long id = -1;

    /**
     * Creates a new user profile.
     */
    public Profile() {
        values = new Hashtable();
//        interpreter = new Interpreter();
    }

    /**
     * Returns the table of values.
     */
    public Hashtable getValues() {
        return values;
    }

    /**
     * Returns the interpreter.
     */
/*    public Interpreter getInterpreter() {
        return interpreter;
    }
*/
    /**
     * Returns the value of the specified attribute.
     */
    public String getAttributeValue(String concept, String name) throws
           InvalidAttributeException {
        if (!values.containsKey(concept + "." + name)) throw new InvalidAttributeException("the specified attribute, " +concept + "." + name +", does not exist");
        return ((AttributeValue)values.get(concept + "." + name)).getValue();
    }

    /**
     * Sets the value of the specified attribute.
     */
    public void setAttributeValue(String concept, String name, String value) throws
           InvalidAttributeException {
        if (!values.containsKey(concept + "." + name)) throw new InvalidAttributeException("the specified attribute does not exist");
        ((AttributeValue)values.get(concept + "." + name)).setValue(value);
    }

    /**
     * this function removes all the pageStability settings for a page because the page was expression stable and the expression evaluated to false.
     * @param pageConceptName
     */
    public void removePageStabilityEntries(String pageConceptName) {
      // loop values
      try {
        for (Enumeration keys = values.keys(); keys.hasMoreElements(); ) {
          String key = (String) keys.nextElement();
          if (key.startsWith(pageConceptName)) {
            if (key.endsWith("pageStability")) {
              values.remove(key);
            }
          }
        }
      } catch (Exception e) {
        System.out.println("Profile: removePageStabilityEntries: Exception occured, elements are not removed");
      }
    }

    /**
     * Sets the value of the specified attribute. First checks if the
     * attribute is valid. If the attribute is valid it is added to
     * the profile if it is not already there. Then its value is set.
     */
    public void setAttributeValue(vn.spring.WOW.WOWDB.ConceptDB cdb, String concept, String name, String value) throws
           InvalidAttributeException {
        if (values.containsKey(concept + "." + name)) {
            setAttributeValue(concept, name, value);
            return;
        }
        try {
            if (!concept.equals("personal")) {
                cdb.getConcept(cdb.findConcept(concept));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidAttributeException(e.getMessage());
        }
        values.put(concept + "." + name, new AttributeValue(true));
        setAttributeValue(concept, name, value);
    }

    //added by Loc Nguyen 2009.12
    public void removeAttribute(String concept, String name) {
    	if(!concept.equals("personal")) {
    		System.out.println("Only remove personal information!");
    		return;
    	}
    	if(name.equals("id") || name.equals("login") || name.equals("name") || name.equals("password") || 
    			name.equals("course") || name.equals("directory") || name.equals("start") || 
    			name.equals("email") || 
    			name.equals("umlocked")) {
    		System.out.println("Can't remove special personal information");
    		return;
    	}
    	String key = concept + "." + name;
    	values.remove(key);
    }
}