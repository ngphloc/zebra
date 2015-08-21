/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * WOWOutAttribute.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.util.LinkedList;
import java.util.Vector;

/**
 * WOWOutAttribute stores the attribute values.
 *
 */
public class WOWOutAttribute {
    public String description;
    public LinkedList generateListItemList;
    public CRTSetDefault setDefaultList;
    public String type;
    public Boolean isChangeable;
    public Boolean isPersistent;
    public Boolean isSystem;
    public String name;
    public String stable;
    public String stable_expr;
    public CaseGroup casegroup = null;

    /**
     * Default constructor.
     */
    public WOWOutAttribute() {
        isChangeable = Boolean.FALSE;
        isPersistent = Boolean.FALSE;
        isSystem = Boolean.FALSE;
        generateListItemList = new LinkedList();
        setDefaultList = new CRTSetDefault();
        description = "";
        type = "bool"; //default value
        name = "";
        stable="";
        stable_expr="";
    }

    /**
     * Checks if there is a casegroup object, if so return true else false.
     * @return <Code>true</Code> if and only if there is a casegroup for this
     * attribute.
     */
    public boolean hasCaseGroup() {
      if (this.casegroup == null) {
        return false;
      } else {
        return true;
      }
    }

    /**
     * copies all the values of the original attribute to the clone attribute so that they contain the same information.
     * @return the cloned attribute
     * Added by @Loc Nguyen @  25-03-2008
     */
    public WOWOutAttribute cloneAttribute() {
      WOWOutAttribute outAttribute = new WOWOutAttribute();
      Case originalCase = null;
      Case cloneCase = null;
      Vector originalCaseVector = null;
      Vector cloneCaseVector = null;

      outAttribute.description = this.description;
      outAttribute.isChangeable = this.isChangeable;
      outAttribute.isPersistent = this.isPersistent;
      outAttribute.isSystem = this.isSystem;
      outAttribute.name = this.name;
      outAttribute.type = this.type;
      outAttribute.stable = this.stable;
      outAttribute.stable_expr = this.stable_expr;
      outAttribute.setDefaultList.setdefault = this.setDefaultList.setdefault;
      if (this.hasCaseGroup() ) {
        if (!outAttribute.hasCaseGroup()) {
          outAttribute.casegroup = new CaseGroup();
        }
        outAttribute.casegroup.setDefaultFragment(this.casegroup.getDefaultFragment());
        // copy the casevalues
        originalCaseVector = this.casegroup.getCaseValues();
        cloneCaseVector = outAttribute.casegroup.getCaseValues();
        for (int i=0; i< originalCaseVector.size(); i++) {
          originalCase = (Case) originalCaseVector.get(i);
          cloneCase = new Case();
          cloneCase.setReturnfragment(originalCase.getReturnfragment());
          cloneCase.setValue(originalCase.getValue());
          cloneCaseVector.add(cloneCase);
        }
      }
      return outAttribute;
    }
}