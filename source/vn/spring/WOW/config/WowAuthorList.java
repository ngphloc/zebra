/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * WowAuthorList.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.config;
import java.util.*;

public class WowAuthorList {
   public Hashtable authorhash;

   public WowAuthorList(Hashtable h) {
     authorhash=h;
   }

   public String getOptionList() {
     WowAuthor wowAuthor;
     StringBuffer sb=new StringBuffer();
     for (Enumeration e = authorhash.elements() ; e.hasMoreElements() ;) {
       wowAuthor=(WowAuthor)e.nextElement();
       sb.append("    <option value=\"");
       sb.append(wowAuthor.getLogin());
       sb.append("\">");
       sb.append(wowAuthor.getName());
       sb.append("</option>\n");
     }
     return sb.toString();
   }

}
