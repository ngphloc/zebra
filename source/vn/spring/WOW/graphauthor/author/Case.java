/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Case.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

/**
 * This class defines a single case statatement containing a value
 * and a return fragment.
 */

public class Case {
  //the actual condition of this case
  private String value = null;
  //the returnfragment indicated which fragment is connected to the value of "value"
  private String returnfragment = null;

  /**
   * Creates a new case.
   */
  public Case() {
      value = null;
      returnfragment = null;
  }

  /**returns the returnfragment as a string
   *
   */
  public String getReturnfragment() {
      if (this.returnfragment != null) {
        return this.returnfragment;
      } else {
        return "";
      }

  }

  /**returns the returnfragment as a string
   *
   */

  public String getValue() {
    if (this.value != null) {
      return this.value;
    } else {
      return "";
    }
  }

  public void setValue(String value){
    this.value=value;
  }

  public void setReturnfragment(String value){
    this.returnfragment=value;
  }

}