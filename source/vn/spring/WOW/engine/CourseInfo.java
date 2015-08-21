/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * CourseInfo.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import java.util.Vector;
import vn.spring.WOW.Layout.*;


public class CourseInfo  {

  public  String courseName=null;

  public  Dependences dependences=null;

  public  Hierarchy hierarchy=null;

  public  ConceptsInfoTbl conceptsInfoTbl=null;

  public  ConceptTypes CT = null;

  public  LayoutList layoutList = null;

  public  String stylesheet = null;

  public  Vector linkclasses = null;

}