/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * RELConceptRelations.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * RELconceptRelations data class to store the conceptRelationList and the divideInformation.
 *
 */
public class RELConceptRelations {
    public LinkedList conceptRelationList;
    public Hashtable divideInformation;

    /**
     * default constructor.
     */
    public RELConceptRelations() {
        conceptRelationList = new LinkedList();
        divideInformation = new Hashtable();
    }

    public ConceptDivideInfo GetTreeInfo(String conceptName) {
        LinkedList llInfo = new LinkedList();
        ConceptDivideInfo cDivInfo = new ConceptDivideInfo();
        int sourceCount = 0;
        int destinationCount = 0;

        for (Iterator j = AuthorSTATIC.relwow.conceptRelationList.iterator();
             j.hasNext();) {
            RELConceptRelation crel = (RELConceptRelation) j.next();

            if (crel.relationType.equals("tree")) {
                if (crel.sourceConceptName.trim().equals(conceptName)) {
                    sourceCount = sourceCount + 1;
                } else if (crel.destinationConceptName.trim().equals(conceptName)) {
                    destinationCount = destinationCount + 1;
                }
            }
        }

        cDivInfo.crtName = "tree";
        cDivInfo.source = sourceCount;
        cDivInfo.destination = destinationCount;
        llInfo.add(cDivInfo);

        return cDivInfo;
    }

    public void AddDivideInformation() {
        Hashtable conceptNames = this.GetConceptNames();

        for (Enumeration e = conceptNames.elements(); e.hasMoreElements();) {
            String conceptName = ((String) e.nextElement()).trim();
            LinkedList llInfo = new LinkedList();

            // the tree crt
            for (Iterator i = AuthorSTATIC.CRTList.iterator(); i.hasNext();) {
                CRTConceptRelationType crt = (CRTConceptRelationType) i.next();
                ConceptDivideInfo cDivInfo = new ConceptDivideInfo();
                String crtName = crt.name.trim();
                int sourceCount = 0;
                int destinationCount = 0;
                for (Iterator j = AuthorSTATIC.relwow.conceptRelationList.iterator();
                     j.hasNext();) {
                    RELConceptRelation crel = (RELConceptRelation) j.next();
                    if (crel.relationType.trim().equals(crtName)) {
                        if (crel.sourceConceptName.trim().equals(conceptName)) {
                            sourceCount = sourceCount + 1;
                        } else if (crel.destinationConceptName.trim()
                                                              .equals(conceptName)) {
                            destinationCount = destinationCount + 1;
                        }
                    }
                }

                cDivInfo.crtName = crtName;
                cDivInfo.source = sourceCount;
                cDivInfo.destination = destinationCount;
                llInfo.add(cDivInfo);
            }

            llInfo.add(this.GetTreeInfo(conceptName));
            this.divideInformation.put(conceptName, llInfo);
        }
    }

    public Hashtable GetConceptNames() {
        Hashtable conceptNames = new Hashtable();

        for (Iterator i = conceptRelationList.iterator(); i.hasNext();) {
            RELConceptRelation crel = (RELConceptRelation) i.next();
            conceptNames.put(crel.sourceConceptName.trim(),
                             crel.sourceConceptName.trim());
            conceptNames.put(crel.destinationConceptName.trim(),
                             crel.destinationConceptName.trim());
        }

        return conceptNames;
    }

    public LinkedList SplitConceptRelations() {
        if (conceptRelationList.isEmpty()) {
            return new LinkedList();
        }

        this.SortRelConceptRelations();

        LinkedList returnlist = new LinkedList();
        LinkedList sublist = new LinkedList();
        String relationname;

        RELConceptRelation conceptrel = (RELConceptRelation) conceptRelationList.getFirst();
        relationname = conceptrel.relationType;

        for (Iterator i = conceptRelationList.iterator(); i.hasNext();) {
            conceptrel = (RELConceptRelation) i.next();

            if (conceptrel.relationType.equals(relationname)) {
                sublist.add(conceptrel);
            } else {
                returnlist.add(sublist);
                sublist = new LinkedList();
                relationname = conceptrel.relationType;
                sublist.add(conceptrel);
            }
        }


        // add last sublist to returnlist
        returnlist.add(sublist);

        return returnlist;
    }

    public void SortRelConceptRelations() {
        // if (conceptRelationList.isEmpty()) { return; }
        if (conceptRelationList.size() < 2) {
            return;
        }

        LinkedList sortedlist = new LinkedList();
        String relationname;

        while (conceptRelationList.isEmpty() == false) {
            RELConceptRelation conceptrel = (RELConceptRelation) conceptRelationList.getFirst();
            relationname = conceptrel.relationType;

            for (Iterator i = conceptRelationList.iterator(); i.hasNext();) {
                conceptrel = (RELConceptRelation) i.next();

                if (conceptrel.relationType.equals(relationname)) {
                    sortedlist.add(conceptrel);
                    i.remove();
                }
            }
        }

        conceptRelationList = sortedlist;
    }
}