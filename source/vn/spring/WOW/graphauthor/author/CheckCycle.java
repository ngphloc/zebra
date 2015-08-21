/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * CheckCycle.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.util.*;


public class CheckCycle {
    public boolean cycle;
    public String startCycle = "";
    public LinkedList currentPath = new LinkedList();

    public CheckCycle() {
        cycle = false;
    }

    public void FindCycle() {
        LinkedList groupedRelations = AuthorSTATIC.relwow.SplitConceptRelations();

        for (Iterator i = groupedRelations.iterator(); i.hasNext();) {
            LinkedList groupedRel = (LinkedList) i.next();

            if (cycle == false) {
                this.CheckRelation(groupedRel);
            }
        }
    }

    public void CheckRelation(LinkedList crelList) {
        // if de crt is not acyclic then return
        RELConceptRelation crel = (RELConceptRelation) crelList.getFirst();
        String relType = crel.relationType.trim();

        for (Iterator j = AuthorSTATIC.CRTList.iterator(); j.hasNext();) {
            CRTConceptRelationType relConceptType = (CRTConceptRelationType) j.next();

            if (relConceptType.name.equals(relType)) {

                if (relConceptType.properties.acyclic.booleanValue() == false) {

                    return;
                }
            }
        }

        for (Iterator i = crelList.iterator(); i.hasNext();) {
            crel = (RELConceptRelation) i.next();

            String root = crel.sourceConceptName;
            this.currentPath = new LinkedList();
            currentPath.add(root);

            String destination = crel.destinationConceptName;
            this.currentPath.add(destination);

            LinkedList destinationList = this.findNext(crelList, destination);

            while (!destinationList.isEmpty()) {
                for (Iterator j = destinationList.iterator(); j.hasNext();) {
                    String element = (String) j.next();
                    this.currentPath.add(element);

                    if (this.TestForCycle(this.currentPath, element)) {

                        return;
                    }
                }

                if (findElement(destinationList, root)) {
                    this.cycle = true;
                    this.startCycle = root;

                    return;
                }

                try {
                    System.in.read();
                } catch (Exception e) {
                    System.out.println("in exception");
                }

            }
        }
    }

    public boolean TestForCycle(LinkedList clist, String lastElement) {
        int n = 0;

        for (Iterator i = clist.iterator(); i.hasNext();) {
            String element = (String) i.next();

            if (element.equals(lastElement)) {
                n++;
            }
        }

        if (n > 1) {
            return true;
        }

        return false;
    }

    public boolean findElement(LinkedList destList, String element) {
        for (Iterator i = destList.iterator(); i.hasNext();) {
            String concept = (String) i.next();

            if (concept.equals(element)) {

                return true;
            }
        }

        return false;
    }

    public LinkedList findNext(LinkedList crelList, String element) {
        LinkedList returnList = new LinkedList();

        for (Iterator i = crelList.iterator(); i.hasNext();) {
            RELConceptRelation crel = (RELConceptRelation) i.next();

            if (element.equals(crel.sourceConceptName)) {
                returnList.add(crel.destinationConceptName);
            }
        }

        return returnList;
    }

}