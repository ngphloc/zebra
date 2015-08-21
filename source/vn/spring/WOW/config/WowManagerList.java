/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * WowManagerList.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

//    WOW Manager Information in listings

// to generate listings for the Manager Management
// in the Java Server Pages
package vn.spring.WOW.config;
import java.util.*;

public class WowManagerList {
   public Hashtable manhash;

   public WowManagerList(Hashtable h) {
     manhash=h;
   }

   public String getOptionList() {
   // create ("<option value='$login'>$name</option>")*
     WowManager man;
     StringBuffer sb=new StringBuffer();
     for (Enumeration e = manhash.elements() ; e.hasMoreElements() ;) {
       man=(WowManager)e.nextElement();
       sb.append("    <option value=\"");
       sb.append(man.getLogin());
       sb.append("\">");
       sb.append(man.getName());
       sb.append("</option>\n");
     }
     return sb.toString();
   }

}
