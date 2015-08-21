/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ServletNameFilter.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.engine;


import org.w3c.dom.*;


public class ServletNameFilter {

	final String LOGOFFSTRING = "logoff";

	public ServletNameFilter(Node n) {

	}
	public StringBuffer Filter(Node n) {
		StringBuffer sb = new StringBuffer();
		sb.append(deriveServletName(n.getNodeName()));
		return sb;
	}

	public String deriveServletName(String tagname) {
		if (tagname.equals(LOGOFFSTRING)) return "Logoff";
		System.out.println("Servlet name not recoqnized: "+tagname);
		return "";
	}
}
