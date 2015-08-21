/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AuthorCourseList.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.config;
import java.util.*;

public class AuthorCourseList {
   public WowAuthor author;

   public AuthorCourseList(WowAuthor a) {
     author=a;
   }

   public String getOptionList() {
     Vector v=author.getCourseList();
     StringBuffer sb=new StringBuffer();
     for (int i=0;i<v.size();i++) {
       sb.append("    <option value=\"");
       sb.append((String)v.elementAt(i));
       sb.append("\">");
       sb.append((String)v.elementAt(i));
       sb.append("</option>\n");
	 }
     return sb.toString();
   }

}
