/**
 * 
 */
package vn.spring.zebra.mining.gsp;
import java.util.ArrayList;   
import java.util.HashMap;   
import java.util.Map;   

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class GeneralizedSequentialPatterns {   
    protected SequenceDatabase db = null;   
    protected double min_sup = 0.0;

    private int support = 0; 
    private ArrayList<Sequence> candidateSequences = new ArrayList<Sequence>();
    private ArrayList<Sequence> frequentSequences = new ArrayList<Sequence>();
    private ArrayList<Sequence> result = new ArrayList<Sequence>();   

    public void setParameters(double min_sup) {
    	this.min_sup = min_sup;
    }
    
    public void perform(SequenceDatabase db) {
        initialize(db);   
   
        for (int i = 0; i < frequentSequences.size(); i++) {   
            genCandidate();   
            if (!(candidateSequences.size() > 0)) {   
                break;   
            }   
            pruneCandidate();   

            generateFrequent();   

            addToResult(frequentSequences);
        }   
    } 
    public ArrayList<Sequence> getSequentialPatterns() {
    	return this.result;
    }
   
    private void initialize(SequenceDatabase db) {
    	this.db = db;
        this.support = (int) (min_sup * (double)db.size());
        this.candidateSequences = new ArrayList<Sequence>();
        this.frequentSequences = new ArrayList<Sequence>();   
        this.result = new ArrayList<Sequence>();
        
        Map<Integer, Integer> can = new HashMap<Integer, Integer>();   
   
        for (int i = 0; i < this.db.size(); i++) {  
        	Sequence s = this.db.getSeq(i);
            for (Element e : s.getElements()) {   
                for (int j : e.getItems()) {   
                    if (can.containsKey(j)) {   
                        int count = can.get(j).intValue() + 1;   
                        can.put(j, count);   
                    } else {   
                        can.put(j, 1);   
                    }   
                }   
            }   
        }   
       
        for (int i : can.keySet()) {   
            if (can.get(i).intValue() >= support) {   
                Element e = new Element(new int[] { i });   
                Sequence seq = new Sequence();   
                seq.addElement(e);   
                this.frequentSequences.add(seq);   
            }   
        }   
   
        this.addToResult(frequentSequences);   
    }   
   
    private void genCandidate() {   
        this.candidateSequences = new ArrayList<Sequence>();   
   
        for (int i = 0; i < this.frequentSequences.size(); i++) {   
            for (int j = i; j < this.frequentSequences.size(); j++) {   
                this.joinAndInsert(frequentSequences.get(i), frequentSequences.get(j));   
                if (i != j) {   
                    this.joinAndInsert(frequentSequences.get(j), frequentSequences.get(i));   
                }   
            }   
        }   
    }   
   
    private void joinAndInsert(Sequence s1, Sequence s2) {   
        Sequence s = null, st = null;   
        Element ef = s1.getElement(0).getWithoutFistItem();   
        Element ee = s2.getElement(s2.length() - 1).getWithoutLastItem();   
   
        int i = 0, j = 0;   
        if (ef.length() == 0) {   
            i++;   
        }   
   
        for (; i < s1.length() && j < s2.length(); i++, j++) {   
            Element e1, e2;   
            if (i == 0) {   
                e1 = ef;   
            } else {   
                e1 = s1.getElement(i);   
            }   
   
            if (j == s2.length() - 1) {   
                e2 = ee;   
            } else {   
                e2 = s2.getElement(j);   
            }   
   
            if (!e1.equals(e2)) {   
                return;   
            }   
   
        } // end of for    
   
        s = new Sequence(s1);   
        (s.getElement(s.length() - 1)).addItem(s2.getElement(s2.length() - 1).getLastItem());
        if (s.notInSeqs(candidateSequences)) {   
            candidateSequences.add(s);   
        }   
   
        st = new Sequence(s1);   
        st.addElement(new Element(new int[] { s2.getElement(s2.length() - 1).getLastItem() }));   
        if (st.notInSeqs(candidateSequences)) {   
            candidateSequences.add(st);   
        }   
   
        return;   
    }   
   
    private void pruneCandidate() {   
        Sequence s = null;   
   
        for (int i = 0; i < this.candidateSequences.size(); i++) {   
            s = candidateSequences.get(i);   
   
            for (int j = 0; j < s.length(); j++) {   
                Element ce = s.getElement(j);   
                boolean prune = false;   

                if (ce.length() == 1) {   
                    s.removeElement(j);   
                    if (s.notInSeqs(this.frequentSequences)) {   
                        prune = true;   
                    }   
   
                    s.insertElement(j, ce);   
                } else {   
                    for (int k = 0; k < ce.length(); k++) {   
                        int item = ce.removeItem(k);   
                        if (s.notInSeqs(this.frequentSequences)) {   
                            prune = true;   
                        }   
   
                        ce.addItem(item);   
                    }   
                }   
   
                if (prune) {   
                    candidateSequences.remove(i);   
                    i--;   
                    break;   
                }   
            }   
        }   
    }   
   
    private void generateFrequent() {   
        this.frequentSequences = new ArrayList<Sequence>();   
        for (int i = 0; i < db.size(); i++) {   
        	Sequence s = db.getSeq(i);
            for (Sequence seq : this.candidateSequences) {   
                if (seq.isSubsequenceOf(s)) {   
                    seq.incrementSupport();   
                }   
            }   
        }   
   
        for (Sequence seq : this.candidateSequences) {   
            if (seq.getSupport() >= this.support) {   
                this.frequentSequences.add(seq);   
            }   
        }   
    }   
   
    private void addToResult(ArrayList<Sequence> frequentSequences) {   
        for (int i = 0; i < frequentSequences.size(); i++) {
            this.result.add(frequentSequences.get(i));

        }   
    }   
   
    public String toString() {
    	if(db.size() == 0) return "No data";
		String textResult = db.toString() + "\n" + "Results with support " + min_sup + ": " + "\n";
		ArrayList<Sequence> patterns = getSequentialPatterns();
		for(Sequence pattern : patterns) {
			textResult += pattern.toString(db.getAttributes());
		}
    	return textResult;
    }
    public static void main(String[] args) {
        SequenceDatabase db= SequenceDatabase.autoGenerate();
        GeneralizedSequentialPatterns gsp = new GeneralizedSequentialPatterns();
        gsp.setParameters(0.4);
        gsp.perform(db); 
        System.out.println(gsp);
    }
}   
