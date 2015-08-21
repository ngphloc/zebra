/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Annotation.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.engine;

public class Annotation {

  private IconAnnotation iconAnno=null;
  private LinkAnnotation linkAnno=null;

  /**
   * Constructor
   */
  public Annotation(LinkAnnotation la, IconAnnotation ia){

    this.linkAnno=la;
    this.iconAnno=ia;

  }

  /**
   * Constructor
   */
  public Annotation(){

  }

  /**
   * Returns iconannotation object
   */
  public IconAnnotation getIconAnno(){

    return this.iconAnno;
  }

  /**
   * Returns linkannotation object
   */
  public LinkAnnotation getLinkAnno(){

   return this.linkAnno;
  }

  /**
   * Sets iconannotation object
   */
  public void setIconAnno(IconAnnotation ia){

    this.iconAnno=ia;
  }

  /**
    * Sets linkannotation object
    */
   public void setLinkAnno(LinkAnnotation la){

     this.linkAnno=la;
  }

}