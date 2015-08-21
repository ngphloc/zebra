/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * CgiUtil.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.Utils;
import java.util.*;

public class CgiUtil extends java.util.Hashtable {
	private static final long serialVersionUID = 1L;


  //private int cl = 0;
  //private byte[] byteline;
  //private String linestr;

  /**
   * The default constructor does nothing.
   */
  CgiUtil() {
    ;
  }

    /**
     * This constructor reads the input from the POST request and
     * stores it in a HashTable.
     */
    public CgiUtil(Map map) {
        super(map);
    }

/*
    super(10, (float)0.1);
    String attribute;
    String value;
    cl = ContentLength;
    if (cl > 0) { // otherwise no real forms input
      byteline = new byte[cl];
      try {
    int i = in.read(byteline, 0, cl);
    }
    catch (Exception e) {}

      linestr = new String(byteline);
      int low = 0, high;
      while (low < cl) {
    high = linestr.indexOf('=', low);
    if (high == -1) // this is actually an error
    System.out.println("Forms input does not end with '='.");
    attribute = linestr.substring(low, high);
    low = high+1;
    high = linestr.indexOf('&', low);
    if (high == -1) { // this happens with the last value
      value = linestr.substring(low, cl);
      value = value.replace('+', ' ');
      value = UnescapeUrl(value);
      this.put(attribute, value);
      break;
    } else {
      value = linestr.substring(low, high);
      value = value.replace('+', ' '); // plustospace
      value = UnescapeUrl(value);
    }
    low = high+1;
    this.put(attribute, value);
      }
    }
  }
*/
  /**
   * UnescapeUrl removes urlencoding from a string.
   *
   * @exception WowException    If there is a syntax error in the urlencoding.
   */
  String UnescapeUrl(String s)

  {
    char[] u = s.toCharArray(); // easier to work with
    String t;
    int x, y;
    for (x=0,y=0; y < u.length;++x,++y) {
      if ((u[x]=u[y]) == '%') {
    // construct and decode the 2-char hex number
    t = new String(u, y+1, 2);
    t = "0x" + t;
    try {
      u[x] = (char)Integer.decode(t).intValue();
    } catch (NumberFormatException e) {
    }
    y+=2;
      }
    }
    return String.copyValueOf(u, 0, x);
  }
}