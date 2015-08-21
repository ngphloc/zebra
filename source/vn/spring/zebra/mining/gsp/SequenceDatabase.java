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
public class SequenceDatabase {
	private ArrayList<Sequence> seqs = new ArrayList<Sequence>();
	private String[] attrs = new String[0];
	
	public SequenceDatabase(ArrayList<String> attrs) {
		this.attrs = new String[attrs.size()];
		for(int i = 0; i < attrs.size(); i++) {
			this.attrs[i] = attrs.get(i);
		}
	}
	public SequenceDatabase(String[] attrs) {
		this.attrs = new String[attrs.length];
		for(int i = 0; i < attrs.length; i++) {
			this.attrs[i] = attrs[i];
		}
	}
	public int indexOf(String attr) {
		for(int i = 0; i < attrs.length; i++) {
			if(attrs[i].equals(attr)) return i;
		}
		return -1;
	}
	public int[] indexOf(ArrayList<String> attrs) {
		ArrayList<Integer> idxes = new ArrayList<Integer>();
		for(int i = 0; i < attrs.size(); i++) {
			int idx = indexOf(attrs.get(i));
			if(idx != -1) idxes.add(new Integer(idx));
		}
		if(idxes.size() == 0) return new int[0];
		
		int[] result = new int[idxes.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = idxes.get(i);
		}
		return result;
	}
	public void pack() {
		ArrayList<Sequence> seqs = new ArrayList<Sequence>();
		for(Sequence seq : this.seqs) {
			seq.pack();
			if(seq.length() > 0) seqs.add(seq);
		}
		this.seqs.clear();
		this.seqs.addAll(seqs);
	}
	
	public static SequenceDatabase autoGenerate() {   
		SequenceDatabase    db = new SequenceDatabase(new String[] {"a", "b", "c", "d", "e"});
        Sequence seq = null;   
   
        // <{0 4}{1}{2}{3}>    
        seq = new Sequence();   
        seq.addElement(new Element(new int[] { 0, 4 }));   
        seq.addElement(new Element(new int[] { 1 }));   
        seq.addElement(new Element(new int[] { 2 }));   
        seq.addElement(new Element(new int[] { 3 }));   
        db.seqs.add(seq);   
   
        // <{0}{2}{3}{2 4}>    
        seq = new Sequence();   
        seq.addElement(new Element(new int[] { 0 }));   
        seq.addElement(new Element(new int[] { 2 }));   
        seq.addElement(new Element(new int[] { 3 }));   
        seq.addElement(new Element(new int[] { 2, 4 }));   
        db.seqs.add(seq);   
   
        // <{0}{1}{2}{3}>    
        seq = new Sequence();   
        seq.addElement(new Element(new int[] { 0 }));   
   
        seq.addElement(new Element(new int[] { 1 }));   
   
        seq.addElement(new Element(new int[] { 2 }));   
        seq.addElement(new Element(new int[] { 3 }));   
        db.seqs.add(seq);   
   
        // <{0}{2}{4}>    
        seq = new Sequence();   
        seq.addElement(new Element(new int[] { 0 }));   
        seq.addElement(new Element(new int[] { 2 }));   
        seq.addElement(new Element(new int[] { 4 }));   
        db.seqs.add(seq);   
   
        // <{3}{4}>    
   
        seq = new Sequence();   
        seq.addElement(new Element(new int[] { 3 }));   
        seq.addElement(new Element(new int[] { 3, 4 }));   
        db.seqs.add(seq);
        
        return db;
	}   
	
	public String[] getAttributes() {return attrs;}
	
	public int size() {
		return this.seqs.size();
	}
	public Sequence getSeq(int i) {   
        return this.seqs.get(i);   
    }
	
	public void addSeq(Sequence seq) {
		seqs.add(seq);
	}
	public String toString() {
		StringBuffer s = new StringBuffer();
		for(int i = 0; i < seqs.size(); i++) {
			s.append(seqs.get(i).toString(attrs));
			if(i < seqs.size() - 1) s.append("\n");
		}
		return s.toString();
	}
	public Sequence genRandomSeq() {
		int m = (int)(Math.random()*(attrs.length+1));
		Sequence seq = new Sequence();
		for(int i = 0; i < m; i++) {
			int n = (int)(Math.random()*(attrs.length+1));
			ArrayList<Integer> items = new ArrayList<Integer>();
			for(int j = 0; j < n; j++) {
				int k = (int)(Math.random()*(attrs.length));
				if(items.indexOf(k) == -1) items.add(k);
			}
			
			Element element = new Element();
			for(int j = 0; j < items.size(); j++) element.addItem(items.get(j).intValue());
			seq.addElement(element);
		}
		return seq;
	}
}
