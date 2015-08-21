/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * NullHashtable.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.Utils;

import java.util.Hashtable;

public class NullHashtable extends Hashtable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Object nullObject = new Object();

	public Object get(Object key) {
		Object result = super.get(key);
		if (result == nullObject) return null; else return result;
	}

	public Object put(Object key, Object value) {
		if (value == null) value = nullObject;
		Object result = super.put(key, value);
		return result;
	}

/*	public String toString() {
		Enumeration keys;
		String key;

		StringBuffer r = new StringBuffer("{");
		for (keys=this.keys();keys.hasMoreElements();) {
			key = (String)keys.nextElement();
			r.append(key);
			r.append("=");
			r.append( ((AAttribute)this.get(key)).toString() );
			r.append(", ");
		}
		r.append("}");
		return r.toString();
	}
*/}