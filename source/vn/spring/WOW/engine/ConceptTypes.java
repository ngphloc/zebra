/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ConceptTypes.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.engine;

import java.util.Hashtable;

public class ConceptTypes {

 private static Hashtable conceptTypes=null;
 static{
   conceptTypes=new Hashtable();
    }

  /**
   * Constructor
   */
  public ConceptTypes(){

	//this.conceptTypes=new Hashtable();

  }

  /**
   * Returns a ConceptType object for type
   */
  public ConceptType getConceptType(String type){

  	return (ConceptType) conceptTypes.get(type);
  }

  /**
   * Adds an entry to the conceptTypes table
   */
  public void addType(String type,ConceptType ct){

    conceptTypes.put(type,ct);
  }



}