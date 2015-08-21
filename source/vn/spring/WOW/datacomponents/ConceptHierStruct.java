/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ConceptHierStruct.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.datacomponents;

/**
 * This class is used in defining the hierarchy of concepts.
 */
public class ConceptHierStruct{
    public String firstchild=null;
    public String nextsib=null;
    public String parent=null;

    //Constructor
    public ConceptHierStruct(){}

    /**
     * Returns a copy of this ConceptHierStruct.
     */
    public ConceptHierStruct copy(String source, String dest) {
        ConceptHierStruct result = new ConceptHierStruct();
        result.firstchild = (firstchild==null?null:firstchild.replaceAll(source, dest));
        result.nextsib = (nextsib==null?null:nextsib.replaceAll(source, dest));
        result.parent = (parent==null?null:parent.replaceAll(source, dest));
        return result;
    }
}