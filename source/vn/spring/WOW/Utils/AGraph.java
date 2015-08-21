/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AGraph.java 1.0
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.Utils;

import java.util.Vector;
import java.util.Enumeration;

public class AGraph {

    public NullHashtable attributes;
    public Vector cycles;

    public AGraph() {
        attributes = new NullHashtable();
        cycles = new Vector();
    }

    public void setAttribute(AAttribute attribute) {
		setAttribute(null, attribute);
	}

    public void setAttribute(String oldname, AAttribute attribute) {
        //check if not null
        if (attribute == null) throw new NullPointerException("AGraph.setAttribute: attribute null");

        String key;
        Enumeration keys;
        AAttribute ref;
        AAction action;
        int i;

		if (oldname == null) oldname = attribute.name;

        //check if attribute exists
        if (attributes.containsKey(oldname)) {
            //attribute exists

			//repair the actions that refer to this attribute
			AAttribute oldattr = (AAttribute)attributes.get(oldname);
			for (i=0;i<oldattr.referred.size();i++) {
				action = (AAction)oldattr.referred.get(i);
				action.attributes.remove(oldname);
				action.attributes.put(attribute.name, attribute);
				attribute.referred.add(action);
			}

			attributes.remove(oldname);
        } else {
            //attribute does not exist

            //set references in other entries and referred list
            for (keys=attributes.keys();keys.hasMoreElements();) {
                key = (String)keys.nextElement();
                ref = (AAttribute)attributes.get(key);
                for (i=0;i<ref.actions.size();i++) {
                    action = (AAction)ref.actions.get(i);
                    if (action.attributes.containsKey(attribute.name)) {
                        action.attributes.remove(attribute.name);
                        action.attributes.put(attribute.name, attribute);
                        attribute.referred.add(action);
                    }
				}
            }
		}

        //set references in this attribute
        for (i=0;i<attribute.actions.size();i++) {
            action = (AAction)attribute.actions.get(i);
            for (keys=action.attributes.keys();keys.hasMoreElements();) {
                key = (String)keys.nextElement();
                if (attributes.containsKey(key)) {
					ref = (AAttribute)attributes.get(key);
					ref.referred.add(action);
					action.attributes.put(key, ref);
				}
			}
		}

		checkCycles(attribute);

        attributes.put(attribute.name, attribute);
    }

    public void clearAttribute(String name) {

    }

    private boolean checkOneCycle(AAttribute attribute) {
		Vector done = new Vector();
		Vector current = new Vector();
		Vector newcurrent;
		boolean result = false;
		int i;
		int j;
		Enumeration keys;
		String key;
		AAttribute ref;
		AAction action;

		done = new Vector();
		current = new Vector();
		result = false;
		done.add(attribute);
		current.add(attribute);
		while ((current.size() > 0) && (!result)) {
//d			System.out.println("done: " + done.toString());
//d			System.out.println("current: " + current.toString());
			newcurrent = new Vector();
			for (i=0;i<current.size();i++) {
				ref = (AAttribute)current.get(i);
//d				System.out.println("found: "+ref.name);
				for (j=0;j<ref.actions.size();j++) {
					action = (AAction)ref.actions.get(j);
					if (action.propagate) {
//d						System.out.println("propagating: "+ref.name);
						for (keys=action.attributes.keys();keys.hasMoreElements();) {
							key = (String)keys.nextElement();
							result = result || (key.equals(attribute.name));
							if ((action.attributes.get(key) != null) && (!done.contains(action.attributes.get(key))))
								newcurrent.add(action.attributes.get(key));
						}
					}
				}
			}
//d			System.out.println("newcurrent: " + newcurrent.toString());
//d			System.out.println();
			current = newcurrent;
			done.addAll(current);
		}
		return result;
	}

    private void checkCycles(AAttribute attribute) {
		for (int i=cycles.size()-1;i>-1;i--)
			if (attributes.containsKey(cycles.get(i)))
				if (!checkOneCycle( (AAttribute)attributes.get(cycles.get(i)) )) cycles.remove(i);
		if (( !cycles.contains(attribute.name) ) && (checkOneCycle( attribute ))) cycles.add(attribute.name);
	}

	public String toString() {
		return attributes.toString();
	}
}