/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * HandlerFilter.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import java.util.Hashtable;
import org.w3c.dom.*;

public class HandlerFilter {

	private Profile profile;
	private String course;
	private Node LastChild = null;
	private static Hashtable wowTags = new Hashtable();
	private static String[] wowTagsTable = { "done", "todo", "colorsettings", "knowledgesettings", "passwordsettings" };
	static {
		Integer tags = new Integer(0);
		for (int i = 0; i < wowTagsTable.length; i++)
		wowTags.put(wowTagsTable[i], tags);
	};

	public HandlerFilter(Profile profile, Node node, String course) {

	this.profile = profile;
	this.course = course;


	}
	public StringBuffer Filter(Node node) throws InvalidAttributeException {
		StringBuffer sb = new StringBuffer();
		profile.getAttributeValue("personal", "id");
		node.getChildNodes();
		StringBuffer handler = getText(node);
		String href = WOWStatic.config.Get("CONTEXTPATH").trim() + "/ViewGet/" + handler.toString().trim();
		StringBuffer title = getText(LastChild);
		sb.append("<a href=\"");
		sb.append(href);
		sb.append("\">");
		sb.append(title.toString().trim());
		sb.append("</a>");
		return sb;
	}

	public StringBuffer getText(Node node) {

		StringBuffer buffer = new StringBuffer();
		NodeList nodelist = node.getChildNodes();
		for (int i=0; i<nodelist.getLength(); i++) {
			Node child = nodelist.item(i);
			if (child.getNodeName().equals("variable")) {
				VariableFilter vf = new VariableFilter(profile, child, course);
				buffer.append(vf.Filter(child).toString());
			}
			if (child.getNodeName().equals("handlername")) {
				buffer = getText(child);
			}
			if (child.getNodeName().equals("linkdescription")) {
				LastChild = child;
			}
			if (wowTags.containsKey(child.getNodeName())) {
				buffer.append((new HandlerNameFilter(child).Filter(child)).toString());
			}
			if (child.getNodeType() == Node.CDATA_SECTION_NODE)
			{
				buffer.append(child.getNodeValue());
			}
			if (child.getNodeType() == Node.TEXT_NODE)
			{
				buffer.append(child.getNodeValue());
			}
		}
		return buffer;
	}

}
