/**
 * 
 */
package vn.spring.zebra.mining.gsp;
import java.util.ArrayList;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class Sequence {   
    private int support = 0;
    private ArrayList<Element> sequence = new ArrayList<Element>();
   
    public Sequence() {}   
   
    public Sequence(Sequence s) {   
    	this();
        for (int i = 0; i < s.length(); i++) {   
            this.sequence.add(s.getElement(i).clone());   
        }   
    }   
   
    public void addElement(Element e) {   
        this.sequence.add(e);   
    }   
    public void addElement(ArrayList<Element> elements) {   
        this.sequence.addAll(elements);   
    }   
   
    public void insertElement(int index, Element e) {   
        this.sequence.add(index, e);   
    }   
   
    public Element removeElement(int index) {   
        if (index < this.sequence.size()) {   
            return this.sequence.remove(index);   
        } else   
            return null;   
    }   
   
    public Element getElement(int index) {   
        if (index >= 0 && index < this.sequence.size()) {   
            return this.sequence.get(index);   
   
        } else {   
            System.err.println("index outof bound in Sequence.getElement()");   
            return null;   
        }   
    }   
   
    public ArrayList<Element> getElements() {   
        return this.sequence;   
    }   
   
    public int length() {   
        return this.sequence.size();   
    }   
   
    public boolean notInSeqs(ArrayList<Sequence> seqs) {   
        Sequence s;   
        for (int i = 0; i < seqs.size(); i++) {   
            s = seqs.get(i);   
            if (this.isSubsequenceOf(s) && s.isSubsequenceOf(this)) {   
                return false;   
            }   
        }   
        return true;   
    }   
   
    public boolean isSubsequenceOf(Sequence s) {   
        int i = 0, j = 0;   
        while (j < s.length() && i < this.length()) {   
            if (this.getElement(i).isContainIn(s.getElement(j))) {   
                i++;   
                j++;   
                if (i == this.sequence.size()) {   
                    return true;   
                }   
            } else {   
                j++;   
            }   
        }   
        return false;   
    }   
   
    @Override
	public boolean equals(Object obj) {
    	Sequence s = (Sequence)obj;
    	if(this.length() != s.length()) return false;
    	for(int i = 0; i < this.length(); i++) {
    		Element e1 = this.getElement(i);
    		Element e2 = s.getElement(i);
    		if(!e1.equals(e2)) return false;
    	}
		return true;
	}

	public void incrementSupport() {   
        this.support++;   
    }   
   
    public int getSupport() {   
        return this.support;   
    }   
    
    public String toString(String[] attrs) {   
        StringBuffer s = new StringBuffer();   
        s.append("<");   
        for (int i = 0; i < this.sequence.size(); i++) {   
            s.append(this.sequence.get(i).toString(attrs));   
            if (i != this.sequence.size() - 1) {   
                s.append(", ");   
            }   
        }   
        s.append(">");   
        return s.toString();   
    }
    
    //Each element no repeated items in each elemet
    public void pack() {
    	ArrayList<Element> sequence = new ArrayList<Element>();
    	for(Element element : this.sequence) {
    		element.pack();
    		if(element.length() > 0) sequence.add(element);
    	}
    	this.sequence.clear();
    	this.sequence.addAll(sequence);
    }
}   
