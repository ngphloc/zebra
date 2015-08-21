/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * DotString.java 3.1, January 25, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.datacomponents;

import java.util.LinkedList;

/**
 * This class describes a dot separated string. That is a string
 * consisting of substrings that are separated by the dot. It
 * contains methods for getting and setting individual substrings
 * and for determining the number of substrings.
 * @author David Smits, changed by Phuoc-Loc Nguyen
 * @version 3.1
 */
public class DotString {
    //The string
    private LinkedList list = null;

    //separator
    private String sep = ".";

    /**
     * Creates a new DotString from a <code>java.lang.String</code>.
     * @param str The string this DotString is based on.
     * @exception NullPointerException If str is null.
     */
    public DotString(String str) throws
           NullPointerException {
        if (str == null) throw new NullPointerException("String object 'str' is null");
        parseString(str);
    }

    /**
     * Creates a new DotString from a <code>java.lang.String</code>
     * with the specified separator.
     * @param str The string this DotString is based on.
     * @param separator The separator that is to be used.
     * @exception NullPointerException If str is null.
     */
    public DotString(String str, String separator) throws
           NullPointerException {
        if (str == null) throw new NullPointerException("String object 'str' is null");
        if (separator == null) throw new NullPointerException("String object 'separator' is null");
        sep = separator;
        parseString(str);
    }

    private void parseString(String str) {
        String s = str;
        list = new LinkedList();
        while (s.indexOf(sep)!=-1) {
            list.add(s.substring(0,s.indexOf(sep)));
            s = s.substring(s.indexOf(sep)+sep.length());
        }
        list.add(s);
    }

    /**
     * Returns the number of substrings.
     * @return The number of substrings.
     */
    public int size() {
        return list.size();
    }

    /**
     * Returns a particular substring
     * @param index The index of the substring requested.
     *        <code>0 <= index < size()</code>
     * @exception IndexOutOfBoundsException If the index is smaller
     *            than zero or larger or equal to <code>size()</code>.
     * @return The substring.
     */
    public String get(int index) throws
                  IndexOutOfBoundsException {
        return (String)list.get(index);
    }

    /**
     * Sets a particular substring.
     * @param index The index of the substring to be set.
     *        <code>0 <= index <= size()</code>
     * @param setstr The new substring.
     * @exception IndexOutOfBoundsException If the index is smaller
     *            than zero or larger than <code>size()</code>.
     */
    public void set(int index, String setstr) throws
           IndexOutOfBoundsException {
        //Check the bounds
        if ((index < 0) || (index > size())) throw new IndexOutOfBoundsException();

        if (index == size()) {if (setstr != null) list.add(setstr);} else {
            if (setstr != null) {
                list.set(index, setstr);
            } else {
                list.remove(index);
            }
        }
    }

    /**
     * Returns this DotString as a string.
     * @return This DotString as a string.
     */
    public String toString() {
        if (size()==0) return ""; else return getString(0, size()-1);
    }

    /**
     * Returns this DotString as a string starting with 'start' and
     * ending with 'end'.
     */
    public String getString(int start, int end) {
        if (start > end) throw new IndexOutOfBoundsException();
        if (start < 0) throw new IndexOutOfBoundsException();
        if (end >= size()) throw new IndexOutOfBoundsException();
        StringBuffer result = new StringBuffer();
        result.append((String)list.get(start));
        for (int i=start+1;i<=end;i++) {
            result.append(sep);
            result.append((String)list.get(i));
        }
        return result.toString();
    }
    
    public int indexOf(String search) {
    	if(search == null) return -1;
    	if(size() == 0) return -1;
    	for(int i = 0; i < size(); i++) {
    		String str = get(i);
    		if(str.equals(search)) return i;
    	}
    	return -1;
    }
    public int indexOfNot(String search) {
    	if(search == null) return -1;
    	if(size() == 0) return -1;
    	for(int i = 0; i < size(); i++) {
    		String str = get(i);
    		if(!str.equals(search)) return i;
    	}
    	return -1;
    }
}