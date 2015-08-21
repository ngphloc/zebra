/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AAttribute.java 1.0
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.Utils;

import java.util.Vector;

public class AAttribute {

    public String name;
    public Vector actions;
    public Vector referred;

    public AAttribute(String name) {
        actions = new Vector();
        referred = new Vector();
        this.name = name;
    }

    public String toString() {
		StringBuffer r = new StringBuffer("(");
		r.append("attribute ");
		r.append(name);
		r.append(": ");
		r.append(actions.toString());
		r.append(")");
		return r.toString();
	}
}
