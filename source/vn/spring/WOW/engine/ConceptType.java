/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ConceptType.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.engine;

import vn.spring.WOW.Layout.*;
import vn.spring.WOW.WOWStatic;

public class ConceptType {

  private String typename=null;
  private String representation=null;
  private Annotation annotation=null;
  private String courseName=null;


  /**
   * Constructor
   */
  public ConceptType(String name,String r, Annotation a, String courseName){

	this.typename=name;
	this.representation=r;
	this.annotation=a;
        this.courseName=courseName;
  }


  /**
  * Returns representation(layout) of this concept type
  */
  public Layout getRepresentation(){

    return WOWStatic.CourseInfoTbl.getCourseInfo(this.courseName).layoutList.getLayout(this.representation);

  }

  /**
   * Returns representation(layout) of this concept type
   */
   public Annotation getAnnotation(){

     return this.annotation;

   }


   /**
    * Returns the name of the concept type
    */
   public String getTypeName(){
     return this.typename;
   }




}