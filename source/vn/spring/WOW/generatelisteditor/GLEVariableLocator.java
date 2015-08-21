/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * VariableLocator.java 1.0, May 25, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.generatelisteditor;

import vn.spring.WOW.exceptions.ParserException;
import vn.spring.WOW.parser.VariableLocator;
import vn.spring.WOW.datacomponents.Concept;
import vn.spring.WOW.datacomponents.Attribute;
import vn.spring.WOW.datacomponents.AttributeType;

import java.util.Vector;

public class GLEVariableLocator implements VariableLocator {
    private Vector concepts = null;

    public GLEVariableLocator(Vector concepts) {
        this.concepts = concepts;
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
        Concept foundconcept = null;
        Concept concept;
        String ivar = variable.trim().toLowerCase();
        if (ivar.startsWith("_")) ivar = ivar.substring(1);
        String cname;
        String temp;
        //find the correct concept for this variable
        for (int i=0;i<concepts.size();i++) {
            concept = (Concept)concepts.get(i);
            cname = concept.getName().trim().toLowerCase();
            if (ivar.startsWith(cname)) {
                if (ivar.equals(cname)) {
                    return defaultvalue(concept, "suitability");
                }
                temp = ivar.substring(cname.length()+1);
                if ((temp.length() > 0) && (temp.indexOf('.') == -1)) foundconcept = concept;
            }
        }
        //return the default value of the found attribute
        if (foundconcept == null) throw new ParserException("variable '"+variable+"' not found");
        return defaultvalue(foundconcept, ivar.substring(foundconcept.getName().trim().length()+1));
    }

    private Object defaultvalue(Concept concept, String attrname) throws ParserException {
        Vector attributes = concept.getAttributes();
        Attribute attr;
        Attribute foundattr = null;
        String aname;
        for (int i=0;i<attributes.size();i++) {
            attr = (Attribute)attributes.get(i);
            aname = attr.getName().trim().toLowerCase();
            if (aname.equals(attrname)) foundattr = attr;
        }
        if (foundattr == null) throw new ParserException("variable '"+concept.getName().trim().toLowerCase()+"."+attrname+"' not found");
        if (foundattr.getType() == AttributeType.ATTRINT) return new Float(0);
        if (foundattr.getType() == AttributeType.ATTRSTR) return "";
        if (foundattr.getType() == AttributeType.ATTRBOOL) return new Boolean(false);
        throw new ParserException("variable '"+concept.getName().trim().toLowerCase()+"."+attrname+"' is of an unknown type");
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
        } catch (ParserException e) {
            return false;
        }
        return true;
    }
}