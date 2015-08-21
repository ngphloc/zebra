/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * SecWndLink.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.Layout;


public class SecWndLink {

  private String linkName=null;
  private String wndName=null;
  private String img=null;


  /**
   * Constructor
   */
  public SecWndLink(String l,String w,String i){

	this.linkName=l;
	this.wndName=w;
	this.img=i;

  }


  /**
  * Returns linkname
  */
  public String getLinkName(){
    return this.linkName;

  }

  /**
  * Returns wndName
  */
  public String getWndName(){
    return this.wndName;

 }

 /**
 * Returns img
 */
 public String getImg(){
   return this.img;

 }

}