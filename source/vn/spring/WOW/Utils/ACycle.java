/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ACycle.java 1.0
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.Utils;

import java.util.Enumeration;

public class ACycle {

    public NullHashtable attributes;

    public ACycle() {
        attributes = new NullHashtable();
    }

    public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("[");
		Enumeration keys = attributes.keys();
		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			result.append(key);
			result.append(", ");
		}
		return result.substring(0,result.length()-2)+"]\n";
	}
}
