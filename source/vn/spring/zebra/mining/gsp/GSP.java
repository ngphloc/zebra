/**
 * 
 */
package vn.spring.zebra.mining.gsp;

import java.util.ArrayList;

import vn.spring.zebra.math.CombinationGenerator;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class GSP {
	public final static int                  MAX_NUMBER_OF_SEQUENTIAL_PATTERNS = 100;
    protected double                         minSup = 0; 
    protected SequenceDatabase               db = null;   
    protected ArrayList<ArrayList<Sequence>> a_freqSeqs = new ArrayList<ArrayList<Sequence>>();
	
    public void setParameters(double minSup) {
    	this.minSup = minSup;
    }
    
    public void perform(SequenceDatabase db) {
    	this.db = db;
    	initialize();
    	
    	if(a_freqSeqs.size() == 0) return;
    	while(true) {
    		ArrayList<Sequence> lastFreqSeqs = a_freqSeqs.get(a_freqSeqs.size() - 1);
    		if(lastFreqSeqs.size() >= MAX_NUMBER_OF_SEQUENTIAL_PATTERNS) break;
    		ArrayList<Sequence> newFreqSeqs = genFreqSeqs();
    		if(newFreqSeqs.size() == 0) break;
    		a_freqSeqs.add(newFreqSeqs);
    	}
    }
    
    private void initialize() {
    	db.pack();
    	if(db.size() == 0) return;
    	
        ArrayList<Sequence> candidate = new ArrayList<Sequence>();   
        for(int i = 0; i < db.size(); i++) {   
        	Sequence seq = db.getSeq(i);
            for (Element element : seq.getElements()) {
            	Sequence tempSeq = new Sequence(); tempSeq.addElement(element);
            	int idx = candidate.indexOf(tempSeq);
            	if(idx != -1) {
            		candidate.get(idx).incrementSupport();
            		continue; //existing
            	}
            	int j = 1;
            	for(; j <= element.length(); j++) {
            		CombinationGenerator gen = new CombinationGenerator (element.length(), j);
            		while (gen.hasMore ()) {
            			int[] indices = gen.getNext ();
                        Element newElement = new Element();   
            			for (int k = 0; k < indices.length; k++) {
            				newElement.addItem(element.getItems().get(indices[k]));
            			}
                    	Sequence newTempSeq = new Sequence(); newTempSeq.addElement(newElement);
                    	int newIdx = candidate.indexOf(newTempSeq);
            			if(newIdx != -1) {
            				candidate.get(newIdx).incrementSupport();
            			}
            			else {
            				newTempSeq.incrementSupport();
                			candidate.add(newTempSeq);
            			}
            		}//end while
            	}//end for j
            }//end for element
        }//end for db.size
        
    	ArrayList<Sequence> newFreqSeqs = new ArrayList<Sequence>();
    	for(Sequence seq : candidate) {
            double support = (double)seq.getSupport() / (double)db.size();
            if (support >= minSup) newFreqSeqs.add(seq);
    	}
    	a_freqSeqs.add(newFreqSeqs);
    }
    
    private ArrayList<Sequence> genFreqSeqs() {
    	ArrayList<Sequence> newFreqSeqs = new ArrayList<Sequence>();
    	
    	if(a_freqSeqs.size() == 0) return newFreqSeqs;
    	ArrayList<Sequence> lastFreqSeqs = a_freqSeqs.get(a_freqSeqs.size() - 1);
    	
    	for(int i = 0; i < lastFreqSeqs.size() - 1; i++) {
    		for(int j = i + 1; j < lastFreqSeqs.size(); j++) {
    			Sequence s1 = lastFreqSeqs.get(i);
    			Sequence s2 = lastFreqSeqs.get(j);
    			Sequence freqSeq1 = join(s1, s2);
    			if(freqSeq1 != null) newFreqSeqs.add(freqSeq1);
    			Sequence freqSeq2 = join(s2, s1);
    			if(freqSeq2 != null) newFreqSeqs.add(freqSeq2);
    		}
    	}
    	return newFreqSeqs;
    }

    private Sequence join(Sequence s1, Sequence s2) {   
    	if(s1.length() == 0 || s1.length() != s2.length()) return null;
    	
    	int length = s1.length();
    	//length-1 elements must be equal
    	for(int i = 0; i < length - 1; i++) {
    		Element e1 = s1.getElement(i);
    		Element e2 = s2.getElement(i);
    		if(!e1.equals(e2)) return null;
    	}
    	//The last element must be different
    	if(length > 1 && s1.getElement(length - 1).equals(s2.getElement(length - 1))) return null;
    	
		Sequence newSeq = new Sequence(s1);
		newSeq.addElement(s2.getElement(length - 1));
		
        for(int i = 0; i < db.size(); i++) {   
        	Sequence s = db.getSeq(i);  
        	if (newSeq.isSubsequenceOf(s)) newSeq.incrementSupport();   
        }
        double support = (double)newSeq.getSupport() / (double)db.size();
		if(support >= this.minSup) return newSeq;
		else                       return null;
    }
    public ArrayList<Sequence> getSequentialPatterns() {
    	ArrayList<Sequence> patterns = new ArrayList<Sequence>();
    	if(a_freqSeqs.size() > 0) patterns.addAll(a_freqSeqs.get(a_freqSeqs.size() - 1));
    	return patterns;
    }
    public Sequence getMaximumSupportSequentialPattern() {
    	ArrayList<Sequence> patterns = getSequentialPatterns();
    	if(patterns.size() == 0) return null;
    	
    	int maxSupport = 0;
    	Sequence maxPattern = null;
    	for(Sequence pattern : patterns) {
    		if(pattern.getSupport() > maxSupport) {
    			maxSupport = pattern.getSupport();
    			maxPattern = pattern;
    		}
    		
    	}
    	return maxPattern;
    }
    
    @Override
	public String toString() {
    	if(db == null || db.size() == 0) return "No data";
    	
		String textResult = db.toString() + "\n" + "Results with support " + minSup + ": " + "\n";
		for(int i = 0; i < a_freqSeqs.size(); i++) {
			ArrayList<Sequence> seqs = a_freqSeqs.get(i);
			textResult += "Length: " + (i + 1) + "\n";
			for(int j = 0; j < seqs.size(); j++) {
				Sequence pattern = seqs.get(j);
				textResult += pattern.toString(db.getAttributes());
				if(j < seqs.size() - 1) textResult += "\n";
			}
			if(i < a_freqSeqs.size() - 1) textResult += "\n\n";
		}
		return textResult;
	}
    public SequenceDatabase getDatabase() {
    	return db;
    }
	public static void main(String[] args) {
        SequenceDatabase db= SequenceDatabase.autoGenerate();
        GSP gsp = new GSP();
        gsp.setParameters(0.7);
        gsp.perform(db); 
        System.out.println(gsp.toString());
    }
}
