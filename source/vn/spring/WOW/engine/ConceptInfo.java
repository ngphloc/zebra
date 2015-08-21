/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ConceptInfo.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import java.io.Serializable;

public class ConceptInfo implements Serializable{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String conceptName=null;
  private String type=null;
  private String title=null;


  /**
   * Constructor
   */
  public ConceptInfo(String name,String type,String title){

    this.conceptName=name;
    this.type=type;
    this.title=title;

  }

  /**
    * Returns type of this concept
    */
  public String getTypeName(){

      return this.type;

  }

  /**
   * Returns title of this concept
   */
 public String getTitle(){

     return this.title;

  }

  /**
   * Returns name of this concept
   */
    public String getConceptName(){

        return this.conceptName;

  }

    @Override
	public String toString() {
    	return getConceptName();
	}



}