package vn.spring.zebra.mining.gsp;

import java.util.ArrayList;   

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class Element {   
   
    private ArrayList<Integer> itemset = new ArrayList<Integer>();
   
    public Element() {}   
   
    public Element(int[] items) {   
        this();   
        for (int i = 0; i < items.length; i++) {   
            this.addItem(items[i]);   
        }   
    }   
   
   
    public void addItem(int item) {   
        int i = 0;   
        for (i = 0; i < itemset.size(); i++) {   
            if (item < itemset.get(i)) {   
                break;   
            }   
        }   
   
        itemset.add(i, item);   
    }   
   
    public ArrayList<Integer> getItems() {   
        return this.itemset;   
   
    }   
   
    public int getLastItem() {   
        if (this.itemset.size() > 0) {   
            return itemset.get(itemset.size() - 1);   
        }   
        else {   
            System.err.println("Error: Element.getLastItem()");   
            return 0;   
        }   
    }   
   
    public boolean isContainIn(Element e) {   
        if (this.itemset.size() > e.itemset.size()) {
            return false;   
        }   
   
        int i = 0, j = 0;   
        while (j < e.length() && i < this.length()) {   
   
            if (this.itemset.get(i).intValue() == e.itemset.get(j).intValue()) {   
                i++;   
                j++;   
            } else {   
                j++;   
            }   
        }   
   
        if (i == this.itemset.size()) {   
            return true;   
        } else {   
            return false;   
        }   
    }   
   
    public Element getWithoutFistItem() {   
        Element e = new Element();   
        for (int i = 1; i < this.itemset.size(); i++) {   
            e.addItem(this.itemset.get(i).intValue());   
        }   
        return e;   
    }   
   
    public Element getWithoutLastItem() {   
        Element e = new Element();   
        for (int i = 0; i < this.itemset.size() - 1; i++) {   
            e.addItem(this.itemset.get(i).intValue());   
        }   
        return e;   
    }   
   
    public int removeItem(int i) {   
        if (i < this.itemset.size()) {   
            return this.itemset.remove(i).intValue();   
        }   
   
        System.err.println("Error in Element.removeItem");   
        return -1;   
    }   
   
    public int compareTo(Object o) {   
        Element e = (Element) o;   
        int r = 0;   
        int i = 0, j = 0;   
        while (i < this.itemset.size() && j < e.itemset.size()) {   
            if (this.itemset.get(i).intValue() < e.itemset.get(j).intValue()) {   
                r = -1;
                break;   
            } else {   
                if (this.itemset.get(i).intValue() > e.itemset.get(j)   
                        .intValue()) {   
                    r = 1;
                    break;   
                }   
            }   
            i++;   
            j++;
        }   
        if (r == 0) {
            if (this.itemset.size() > e.itemset.size()) {   
                r = 1;   
            }   
            if (this.itemset.size() < e.itemset.size()) {   
                r = -1;   
            }   
        }   
        return r;   
    }   
   
    public int length() {   
        return this.itemset.size();   
    }   
   
    public Element clone() {   
        Element clone = new Element();   
        for (int i : this.itemset) {   
            clone.addItem(i);   
        }   
        return clone;   
    }   
   
    public Element clone(int beginIdx, int length) {
    	if(beginIdx >= this.length() || this.length() == 0) return new Element();
    	if(beginIdx + length > this.length()) length = this.length() - beginIdx;
        Element clone = new Element();   
    	for(int i = 0; i < length; i++) {
    		clone.addItem(this.itemset.get(i + beginIdx));
    	}
        return clone;   
    }   

    public boolean equals(Object o) {   
        boolean equal = true;   
        Element e = (Element) o;   
        if (this.itemset.size() != e.itemset.size()) {
            equal = false;   
        }   
   
        for (int i = 0; equal && i < this.itemset.size(); i++) {   
            if (this.itemset.get(i).intValue() != e.itemset.get(i).intValue()) {   
                equal = false;   
            }   
        }   
        return equal;   
    }   
   
    public String toString(String[] attrs) {
        StringBuffer s = new StringBuffer();   
        if (this.itemset.size() > 1) {   
            s.append("(");   
        }   
   
        for (int i = 0; i < this.itemset.size(); i++) {
        	int v = this.itemset.get(i).intValue();
            s.append( ((attrs == null || attrs.length == 0) ? v : attrs[v]) );   
            if (i < this.itemset.size() - 1) {   
                s.append(",");   
            }   
        }   
   
        if (this.itemset.size() > 1) {   
            s.append(")");   
        }   
   
        return s.toString();   
    }
    
    //remove repeated items
    public void pack() {
    	ArrayList<Integer> itemset = new ArrayList<Integer>();
    	for(Integer item : this.itemset) {
    		if(item.intValue() != -1 && itemset.indexOf(item) == -1) itemset.add(item);
    	}
    	this.itemset.clear();
    	this.itemset.addAll(itemset);
    }
}   

