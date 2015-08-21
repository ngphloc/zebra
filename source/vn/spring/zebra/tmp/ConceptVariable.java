package vn.spring.zebra.tmp;

import vn.spring.bayes.BayesianNetworks.*;
import java.util.ArrayList;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ConceptVariable extends ProbabilityVariable {
	private int      level = 0;
	private boolean  isEvidence = false;
	
	public ArrayList<ConceptVariable> child_vars = new ArrayList<ConceptVariable>();
	public ArrayList<Double> child_weights = new ArrayList<Double>();
	
	public ArrayList<ConceptVariable> parent_vars = new ArrayList<ConceptVariable>();
	public ArrayList<Double> parent_weights = new ArrayList<Double>();
	
	public ArrayList<ConceptVariable> evidence_vars = new ArrayList<ConceptVariable>();
	public ArrayList<Double> evidence_weights = new ArrayList<Double>();

	public int  getLevel()            {return level;}
	public void setLevel(int level)   {this.level = level;}

	public boolean  isEvidence(){
		return isEvidence;
	}
	public void     setEvidence(boolean isEvidence) {
		this.isEvidence = isEvidence;
	}

	public boolean  isBelowEvidence() {
		if(!isEvidence) return false;
		return hasParentEvidence();
	}

	public int    getNumChilds() {return child_vars.size();}
	public void   addChild(ConceptVariable child, double weight) {child_vars.add(child); child_weights.add(new Double(weight));}
	public        ConceptVariable getChild(int index) {return child_vars.get(index);}
	public double getChildWeight(int index) {return child_weights.get(index).doubleValue();}
	public int    findChild(String child_name) {
		int n = child_vars.size();
		for(int i=0; i<n; i++) {
			ConceptVariable child = child_vars.get(i);
			if(child.get_name().compareTo(child_name) == 0) return i;
		}
		return -1;
	}
	public void removeChild(int idx) {
		child_vars.remove(idx);
		child_weights.remove(idx);
	}
	public boolean hasChildEvidence() {
		int n = getNumChilds();
		if(n==0) return false;
		for(int i=0; i<n; i++) {
			if(getChild(i).isEvidence) return true;
		}
		return false;
	}
	public ConceptVariable[] toChildsArray() {
		int n = child_vars.size();
		if(n==0) return null;
		
		ConceptVariable[] aChilds = new ConceptVariable[n];
		for(int i=0; i<n; i++) aChilds[i] = child_vars.get(i);
		return aChilds;
	}
	
	public int    getNumParents() {return parent_vars.size();}
	public void   addParent(ConceptVariable parent, double weight) {parent_vars.add(parent); parent_weights.add(new Double(weight));}
	public        ConceptVariable getParent(int index) {return parent_vars.get(index);}
	public double getParentWeight(int index) {return parent_weights.get(index).doubleValue();}
	public        int findParent(String parent_name) {
		int n = parent_vars.size();
		for(int i=0; i<n; i++) {
			ConceptVariable parent = parent_vars.get(i);
			if(parent.get_name().compareTo(parent_name) == 0) return i;
		}
		return -1;
	}
	public void removeParent(int idx) {
		parent_vars.remove(idx);
		parent_weights.remove(idx);
	}
	public boolean hasParentEvidence() {
		int n = getNumParents();
		if(n==0) return false;
		for(int i=0; i<n; i++) {
			if(getParent(i).isEvidence) return true;
		}
		return false;
	}
	public ConceptVariable[] toParentsArray() {
		int n = parent_vars.size();
		if(n==0) return null;

		ConceptVariable[] aParents = new ConceptVariable[n];
		for(int i=0; i<n; i++) aParents[i] = parent_vars.get(i);
		return aParents;
	}
	public DiscreteVariable[] toDiscreteVariableArray() {
		int n = parent_vars.size();

		DiscreteVariable[] avars = new ConceptVariable[n + 1];
		
		avars[0] = this;
		for(int i=1; i<n+1; i++) avars[i] = parent_vars.get(i-1);
		return avars;
	}
	
	public int    getNumEvidences() {return evidence_vars.size();}
	public void   addEvidence(ConceptVariable evidence, double weight) {evidence_vars.add(evidence); evidence_weights.add(new Double(weight));}
	public        ConceptVariable getEvidence(int index) {return evidence_vars.get(index);}
	public double getEvidenceWeight(int index) {return evidence_weights.get(index).doubleValue();}
	public        int getEvidence(String evidence_name) {
		int n = evidence_vars.size();
		for(int i=0; i<n; i++) {
			ConceptVariable evidence = evidence_vars.get(i);
			if(evidence.get_name().compareTo(evidence_name) == 0) return i;
		}
		return -1;
	}
	public void removeEvidence(int idx) {
		evidence_vars.remove(idx);
		evidence_weights.remove(idx);
	}
	public ConceptVariable[] toEvidencesArray() {
		int n = evidence_vars.size();
		if(n==0) return null;
		
		ConceptVariable[] aEvidences = new ConceptVariable[n];
		for(int i=0; i<n; i++) aEvidences[i] = evidence_vars.get(i);
		return aEvidences;
	}

	public ConceptVariable(BayesNet b_n, String n_vb, int vi,
		     String vl[]) {
		super(b_n, n_vb, vi, vl, null);
	}

}
