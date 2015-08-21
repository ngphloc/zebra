/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * UMVariableLocator.java 1.0, May 25, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.parser;

import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.WOWDB.ConceptDB;
import vn.spring.WOW.datacomponents.*;
import vn.spring.zebra.helperservice.WOWContextListener;
import vn.spring.zebra.um.OverlayBayesUM;

import java.util.Hashtable;

/**
 * Modified by Loc Nguyen 2008. This file is very important
 *
 */
public class UMVariableLocator implements VariableLocator {

    // reference to the profile used as variable store
    private Profile profile = null;
    // reference to the concept database
    private ConceptDB cdb = null;
    // reference to the update table
    private Hashtable updatetable = null;

    /**
     * Initiates a new instance of the 'User Model Variable Locator'.
     * This class will use a specific profile to look up the values
     * of variables encountered in expressions.
     */
    public UMVariableLocator(Profile profile, ConceptDB cdb) {
        this.profile = profile;
        this.cdb = cdb;
    }

    //Natasha
    public UMVariableLocator() {
    }

    //added by Loc Nguyen 2009.12.29
    public Profile getProfile() {return profile;}
    //added by Loc Nguyen 2009.12.29
    public ConceptDB getConceptDB() {return cdb;}
    
    /**
     * This method sets an optional update table. The update table
     * contains entries that define the value a relative variable. If
     * no update table is specified then a default value is returned
     * for relative variables.
     */
    public void setUpdateTable(Hashtable updatetable) {
        this.updatetable = updatetable;
    }

    /**
     * This method retrieves the value of the specified variable.
     * Must return float values when dealing with integers.
     * @param variable The name of the variable whose value is
     *        requested.
     * @return The actual value of the specified variable. This can
     *         be a boolean, a String or an integer (type long).
     */
    public Object getVariableValue(String variable) throws ParserException {
        boolean relative = false;
        //check if it is a relative attribute or not
        if ((variable.length()>0) && (variable.charAt(0)=='_')) {
            variable = variable.substring(1);
            relative = true;
        }
        //check the database to see if the requested variable is in fact a concept
        long c = -1;
        try {c = cdb.findConcept(variable);}
        catch (InvalidConceptException e) {}
        catch (DatabaseException e) {throw new ParserException("DatabaseException: "+e.getMessage());}
        if (c != -1) {
            //if the variable is a concept... return the value of the suitability attribute
            variable = variable+".suitability";
        }
        //the variable is not a concept, but presumably an attribute... so split it in parts first
        DotString dstring = new DotString(variable);
        if (dstring.size() <= 1) throw new ParserException("variable '"+variable+"' not found");
        String attrname = dstring.get(dstring.size()-1);
        dstring.set(dstring.size()-1, null);
        String conceptname = dstring.toString();
        //check if this variable is part of the special 'personal' concept
        if (conceptname.equals("personal") || conceptname.endsWith(".personal")) {
            //return the value stored in the profile
            try {
                return interpretType(profile.getAttributeValue(conceptname, attrname));
            } catch (InvalidAttributeException e) {
                return "";
            }
        }
        //check if the attribute refers to a special concept attribute (like the name or type)
        if (attrname.startsWith("$") && attrname.endsWith("$")) {
            try {
                Concept concept = cdb.getConcept(cdb.findConcept(conceptname));
                Object special = getSpecialValue(concept, attrname);
                if (special == null) throw new ParserException("variable '"+variable+"' not found");
                return special;
            } catch (Exception e) {throw new ParserException(e.getMessage());}
        }
        //Loc Nguyen add 2009.01.08
        //check if the attribute referes to Bayesian inference
        if(attrname.indexOf(OverlayBayesUM.OBUM_ATTR_STATIC_BAYES_POSTERIOR_INFER_PREFIX) == 0 ||
        		attrname.indexOf(OverlayBayesUM.OBUM_ATTR_DYN_BAYES_POSTERIOR_INFER_PREFIX) == 0 ) { //do$bayes$infer, do$bayes$infer$0, do$bayes$infer$1, do$bayes$infer$valueidx
        	try {
            	String userid = profile.getAttributeValue("personal", "id");
            	String course = dstring.get(0);
        		String briefConceptName = dstring.get(1);
        		
        		userid = profile.getAttributeValue("personal", "id");
        		Float value = WOWContextListener.getInstance().
        			getTriUMServer().getUM(userid, course, true).doSomeThing(briefConceptName, attrname);
            	return value;
            }
    		catch(Exception e) {
    			String err = "UMVariableLocator.getVariableValue causes error when performing Bayes inference:\n"  + e.getMessage(); 
    			System.out.println(err);
    			throw new ParserException(err);
    		}
        }
        
        //check if the concept exists and retrieve the specified attribute info
        Attribute attr = null;
        try {c = cdb.findConcept(conceptname); attr = cdb.getAttribute(c, attrname);}
        catch (InvalidConceptException e) {throw new ParserException("variable '"+variable+"' not found");}
        catch (InvalidAttributeException e) {throw new ParserException("variable '"+variable+"' not found");}
        catch (DatabaseException e) {throw new ParserException("DatabaseException: "+e.getMessage());}

        if (attr==null) throw new ParserException("variable '"+variable+"' not found");
        if (attr.isPersistent()) {
            //the attribute is persistent and its value is thus stored in the profile
            if (relative) {
                if ((updatetable != null) && (updatetable.containsKey(variable))) {
                    return updatetable.get(variable);
                } else {
                    return defaultValue(attr);
                }
            } else {
                try {
                    String value = profile.getAttributeValue(conceptname, attrname);
                    return objectValue(attr, value);
                } catch (InvalidAttributeException e) {
                    //not in the profile, but perhaps there is a default value
                    String def = attr.getDefault();
                    if ((def != null) && (!def.equals(""))) {
                        Parser parser = new Parser(this);
                        Object result;
                        try {result = parser.parse(def);}
                        catch (ParserException pe) {
                            throw new ParserException("error in default expression '"+def+"' of attribute '"+variable+"': "+pe.getMessage());
                        }
                        return result;
                    }
                    return defaultValue(attr);
                }
            }
        } else {
            //the attribute is not persistent, but might have a default value
            String def = attr.getDefault();
            if ((def != null) && (!def.equals(""))) {
                Parser parser = new Parser(this);
                Object result;
                try {result = parser.parse(def);}
                catch (ParserException e) {
                    throw new ParserException("error in default expression '"+def+"' of attribute '"+variable+"': "+e.getMessage());
                }
                return result;
            } else {
                //there is no default value... but maybe there is a value in the profile
                if (profile.getValues().containsKey(variable)) {
                    try {
                        String value = profile.getAttributeValue(conceptname, attrname);
                        return objectValue(attr, value);
                    } catch (InvalidAttributeException e) {
                        throw new ParserException(e.getMessage());
                    }
                }
                //there is no default value and no value in the profile... generate a default value
                return defaultValue(attr);
            }
        }
    }

    private Object defaultValue(Attribute attr) {
        if (attr.getType() == AttributeType.ATTRINT) {return new Float(0);}
        if (attr.getType() == AttributeType.ATTRBOOL) {return new Boolean(false);}
        return new String("");
    }

    private Object objectValue(Attribute attr, String value) {
        if (attr.getType() == AttributeType.ATTRINT) {return new Float(value);}
        if (attr.getType() == AttributeType.ATTRBOOL) {return new Boolean(value);}
        return value;
    }

    private Object interpretType(String value) {
        if (value.equals("true")) return new Boolean(true);
        if (value.equals("false")) return new Boolean(false);
        try {
            return new Float(value);
        } catch (NumberFormatException e) {
            return value;
        }
    }

    private Object getSpecialValue(Concept concept, String attrname) {
        if (attrname.equals("$name$")) return concept.getName();
        if (attrname.equals("$type$")) return concept.getType();
        if (attrname.equals("$description$")) return concept.getDescription();
        if (attrname.equals("$title$")) return concept.getTitle();
        if (attrname.equals("$haschildren$")) {
            try {
                String course = profile.getAttributeValue("personal", "course");
                vn.spring.WOW.engine.PNode node = new vn.spring.WOW.engine.PNode(vn.spring.WOW.WOWStatic.CourseInfoTbl.getCourseInfo(course).hierarchy.getTNode(concept.getName()), profile);
                return new Boolean(node.getFirstChild() != null);
            } catch (Exception e) {return new Boolean(false);}
        }
        return null;
    }

    /**
     * This method returns true if the specified variable exists.
     * @param variable The name of the variable.
     * @return A boolean indicating whether the specified variable
     *         exists.
     */
    public boolean existsVariable(String variable) {
        try {
            getVariableValue(variable);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}