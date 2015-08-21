/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * CaseGroup.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.util.Vector;

/**
 * This class defines a single case statatement containing a value
 * and a return fragment.
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

    //end Natasha
    /**Changed by Loc Nguyen
     * Sets the default fragment as a string.
     * @param vec default case value
     */
    public void setCaseValues(Vector vec) {
        this.caseValues = vec;
    }
    //end Natasha
    
    /**
     * Returns the vector with casevalues.
     * @return caseValues caseValues
     */
    public Vector getCaseValues() {
        return caseValues;
    }

    // Added by @Loc Nguyen @ 13-05-2008
    // it's a debug function to see the casegroup
    public void printCaseGroup() {
      for (int i=0; i<this.caseValues.size(); i++) {
        Case tempCase = (Case) caseValues.get(i);
        //Loc Nguyen add
        if(tempCase == null) {}
      }
    }
    // End Added by @Loc Nguyen @ 13-05-2008
};
