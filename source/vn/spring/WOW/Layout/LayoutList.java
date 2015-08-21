/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * LayoutList.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.Layout;


import java.util.Hashtable;

public class LayoutList {

 private static Hashtable layoutList=null;
 static{
   layoutList=new Hashtable();
    }

  /**
   * Constructor
   */
  public LayoutList(){


  }

  /**
   * Returns a LayoutList object with name 'layoutname'
   */
  public Layout getLayout(String lname){

  	return (Layout) layoutList.get(lname);
  }

  /**
   * Adds an entry to the LayoutList table
   */
  public void addLayout(String lname,Layout l){

    layoutList.put(lname,l);
  }



}