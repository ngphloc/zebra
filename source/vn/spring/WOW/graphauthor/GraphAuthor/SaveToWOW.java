/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * SaveToWOW.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.GraphAuthor;

import vn.spring.WOW.graphauthor.author.*;

import java.net.*;

import java.util.*;

import javax.swing.tree.*;


/**
 * This class saves the graph in WOW format. Modified by Loc Nguyen 2008
 *
 */
public class SaveToWOW {
    public URL home;
    public boolean sysOutput;

    public SaveToWOW(URL base, boolean noOutput) {
        sysOutput = noOutput;
        home = base;
        // filename is projectname + extention
        String fname = GraphAuthor.projectName + ".gaf";
        this.ReadAuthor(home,fname);
    }

    public void writeElementRelations(DefaultMutableTreeNode element) {
     if (element == null) { return;}

     String course = GraphAuthor.projectName.trim();

      for (Enumeration i = element.children(); i.hasMoreElements();) {
            RELConceptRelation trel = new RELConceptRelation();
            DefaultMutableTreeNode tnode = (DefaultMutableTreeNode) i.nextElement();
            trel.destinationConceptName = (course + "." + element.toString()).trim();
            trel.sourceConceptName = (course + "." + tnode.toString()).trim();
            trel.relationType ="tree";
            trel.label="";
            this.writeElementRelations(tnode);
            AuthorSTATIC.relwow.conceptRelationList.add(trel);


      }
    }
   public void WriteTreeRelations() {
 // pre: GraphAutho.sharedConceptree must have the current tree
     DefaultMutableTreeNode root = (DefaultMutableTreeNode) GraphAuthor.sharedConceptTree.getModel().getRoot();
      //for (Enumeration i = root.children(); i.hasMoreElements();) {
      //  this.writeElementRelations((DefaultMutableTreeNode) i.nextElement());
      //}
      // fixed by Paul De Bra and Natalia Stash, reviewed and modified by Loc Nguyen, 29-07-2008
      this.writeElementRelations(root);
   }

 public void WriteTemplateRelations() {
   for (Iterator i = GraphAuthor.conceptList.iterator(); i.hasNext();) {
     WOWOutConcept concept = (WOWOutConcept) i.next();

      for ( Iterator j = AuthorSTATIC.templateList.iterator(); j.hasNext();) {
        ConceptTemplate cTemp = (ConceptTemplate) j.next();

        if (concept.template.equals(cTemp.name)) {
          for (Iterator k = cTemp.conceptRelations.entrySet().iterator(); k.hasNext();) {
            Map.Entry m = (Map.Entry) k.next();
            RELConceptRelation temprel = new RELConceptRelation();
            temprel.sourceConceptName = GraphAuthor.projectName + "." +concept.name;
            temprel.destinationConceptName = GraphAuthor.projectName + "." +concept.name;
            temprel.relationType = ( (String) m.getKey()).trim();
            temprel.label = ( (String) m.getValue()).trim();
            AuthorSTATIC.relwow.conceptRelationList.add(temprel);
            //   System.out.println("added template relation: " + temprel.relationType);
          }

        }

      }


   }

 }

    public void ReadAuthor(URL home, String fileName) {

      new ReadAuthorREL(home, fileName,GraphAuthor.projectName);
      // convert the emplate relation into author relations
      this.WriteTemplateRelations();
      // read the tree view as relations
      this.WriteTreeRelations();

      /*
      // read the concept relation types
      RELConceptRelation crel = null;
      try {
        crel = (RELConceptRelation) AuthorSTATIC.relwow.conceptRelationList.getFirst();
      }
      catch (Exception e) {
        System.out.println("exception crel empty!");
        return;
      }
      */

      AuthorSTATIC.conceptList = GraphAuthor.conceptList;
      AuthorSTATIC.projectName = GraphAuthor.projectName;
      
      // change the author relation to wow code
      AuthorToWow outwow = new AuthorToWow(home);
      if(outwow == null) {}
      
      String saveToWow = GraphAuthor.projectName + ".wow";
      // saves it to the server
      WriteWOWXML wwow = new WriteWOWXML(AuthorSTATIC.wowOut, home, saveToWow,
                                         GraphAuthor.projectName, sysOutput);
      if(wwow == null) {}
    }

}