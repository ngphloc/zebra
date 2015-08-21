/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AttributeIdentifier.java 1.0, November 16, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.datacomponents;

import vn.spring.WOW.exceptions.*;

public class AttributeIdentifier {
    private String concept;
    private String attribute;

    public AttributeIdentifier(String aid) throws InvalidAttributeException {
        if (aid == null) throw new InvalidAttributeException("the supplied string is not a valid attribute identifier");
        if (aid.indexOf(".")==-1) throw new InvalidAttributeException("the supplied string is not a valid attribute identifier");
        DotString ds = new DotString(aid);
        attribute = ds.get(ds.size()-1);
        ds.set(ds.size()-1, null);
        concept = ds.toString();
    }

    public String toString() {
        return concept + "." + attribute;
    }

    public String concept() {
        return concept;
    }

    public String attribute() {
        return attribute;
    }
}