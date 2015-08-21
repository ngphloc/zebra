/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * HandlerNameFilter.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import org.w3c.dom.*;


public class HandlerNameFilter {

	final String COLORSETTINGSSTRING = "colorsettings";
	final String KNOWLEDGESETTINGSSTRING = "knowledgesettings";
	final String PASSWORDSETTINGSSTRING = "passwordsettings";
	final String DONESTRING = "done";
	final String TODOSTRING = "todo";

	public HandlerNameFilter(Node n) {

	}
	public StringBuffer Filter(Node n) {
		StringBuffer sb = new StringBuffer();
		sb.append(deriveHandlerName(n.getNodeName()));
		return sb;
	}

	public String deriveHandlerName(String tagname) {
		if (tagname.equals(COLORSETTINGSSTRING)) return "ColorConfig";
		if (tagname.equals(KNOWLEDGESETTINGSSTRING)) return "KnowledgeConfig";
		if (tagname.equals(PASSWORDSETTINGSSTRING)) return "PasswordConfig";
		if (tagname.equals(DONESTRING)) return "Done";
		if (tagname.equals(TODOSTRING)) return "Todo";
		System.out.println("Handler name not recoqnized: "+tagname);
		return "";
	}
}
