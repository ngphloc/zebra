/*
    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 
*/
/**
 * WOWOutConcept.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * WOWOutConcept stores the output concept
 *
 */
public class WOWOutConcept {
    public String name;
    public String description;
    public String resource;
    public String template;
    public boolean nocommit;
    public LinkedList attributeList;
    public String stable;
    public String stable_expr;
    //added by @David @18-05-2008
    public String concepttype;
    public String title;
    public String firstchild;
    public String nextsib;
    public String parent;
    //end added by @David @18-05-2008

    /**
      * Default constructor.
      */
    public WOWOutConcept() {
        attributeList = new LinkedList();
        name = "";
        description = "";
        resource = "";
        template ="";
        nocommit = false;
        concepttype="";
        title="";
        firstchild=null;
        nextsib=null;
        parent=null;
    }

    /**
     *
     * @param Name
     * @return The requested attribute for this concept or <Code>null</Code>
     * if the concept does not have the requested attribute.
     */
    public WOWOutAttribute getAttribute(String Name) {
      if (this.attributeList.isEmpty()) {
        return null;
      } else {
        // loop list
        for (Iterator i = this.attributeList.iterator(); i.hasNext();) {
          WOWOutAttribute attribute = (WOWOutAttribute) i.next();
          if (attribute.name.equals(Name)) {
            return attribute;
          }
        }
        // nothing found
        return null;
      }
    }

    /**
     * Adds the attributes of the specified template to the concept.
     * Adds the attributes of the specified template to the concept.
     * Added by @Bart
     */
    public void AddTemplateAttributes() {

      // find the template for a concept
      String templateName = this.template;
      for (Iterator k = AuthorSTATIC.templateList.iterator(); k.hasNext();) {
        ConceptTemplate cTemp = (ConceptTemplate) k.next();
        if (templateName.equals(cTemp.name)) {

          // process all attributes form the template
          for (Iterator l = cTemp.attributes.iterator(); l.hasNext();) {
            WOWOutAttribute tempAtt = (WOWOutAttribute) l.next();

            // new is needed to reserve new memory
            WOWOutAttribute cloneAtt = null;
            cloneAtt = tempAtt.cloneAttribute();

            // add attribute to this concept
            this.attributeList.add(cloneAtt);
          }
        }
      }
    }
}