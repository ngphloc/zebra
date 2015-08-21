/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * SecWndLinks.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.Layout;

import java.util.Hashtable;
import java.util.Enumeration;

public class SecWndLinks {

  Hashtable secWndLinks=null;


  /**
   * Constructor
   */
  public SecWndLinks(){

    this.secWndLinks=new Hashtable();
  }


  public Enumeration getLinksNames(){

    return this.secWndLinks.keys();

  }

  /**
  * Returns window from this layout that has name 'wndName'
  */
  public SecWndLink getSwl(String linkName){
    return (SecWndLink) this.secWndLinks.get(linkName);

  }

  /**
  * Adds a window to this layout
  */
  public void addSwl(String linkName,SecWndLink swl){
        this.secWndLinks.put(linkName,swl);

  }

}