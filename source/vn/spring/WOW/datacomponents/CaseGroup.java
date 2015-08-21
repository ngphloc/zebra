/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Case.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.datacomponents;

import java.util.Vector;

/**
 * This class defines a single case statatement containing a value
 * and a return fragment.
 *
 */
public class CaseGroup {

    //the actual condition of this case
    private String defaultFragment = null;
    //the returnfragment indicated which fragment is connected to the value of "value"
    private Vector caseValues = null;

    /**
     * Creates a new case.
     */
    public CaseGroup() {
        caseValues = new Vector();
        defaultFragment = null;
    }

    /**
     * Returns the default fragment of this case as a string.
     * @return string default fragment
     */
    public String getDefaultFragment() {
        return this.defaultFragment;
    }

    /**
     * Sets the default fragment as a string.
     * @param def default fragment
     */
    public void setDefaultFragment(String def) {
        this.defaultFragment = def;
    }

    /**
     * Returns the vector with casevalues.
     * @return caseValues caseValues
     */
    public Vector getCaseValues() {
        return caseValues;
    }

    /**
     * Returns a copy of this CaseGroup.
     */
    public CaseGroup copy(String source, String dest) {
        CaseGroup result = new CaseGroup();
        result.setDefaultFragment((defaultFragment==null?null:defaultFragment.replaceAll(source, dest)));
        Case casevalue;
        for (int i=0;i<caseValues.size();i++) {
            casevalue = (Case)caseValues.get(i);
            result.getCaseValues().add(casevalue.copy(source, dest));
        }
        return result;
    }
}