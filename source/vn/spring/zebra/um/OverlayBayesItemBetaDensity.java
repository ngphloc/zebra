/**
 * 
 */
package vn.spring.zebra.um;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.math.BetaDensity;
import vn.spring.zebra.math.BinaryArrangementWithRepetitionGenerator;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class OverlayBayesItemBetaDensity {
	private OverlayBayesItem item = null;
	private OverlayBayesItem[] parents = new OverlayBayesItem[0];
	private ArrayList<double[]> parentPatterns = new ArrayList<double[]>();
	private ArrayList<BetaDensity> betas = new ArrayList<BetaDensity>();
	
	public static boolean checkValid(OverlayBayesItem item) {
		try {
			ConceptNodeTypeWrapper type = new ConceptNodeTypeWrapper(item);
			boolean isEvidence = type.isEvidence();
			int  base = OverlayBayesUMFactory.getAndCheckNumberOfDefaultVariableValues(type);
			if(base == 2 && !isEvidence) return true;
		}
		catch(Exception e) {}
		return false;
	}
	public OverlayBayesItemBetaDensity(OverlayBayesItem item) throws ZebraException {
		if(!checkValid(item)) {
			String err= "OverlayBayesItemBetaDensity Just only supporting for binary and non-evidence variables";
			JOptionPane.showMessageDialog(null, err, "OverlayBayesItemBetaDensity error", JOptionPane.ERROR_MESSAGE);
			throw new ZebraException(err);
		}
		this.item = item;
		int  base = OverlayBayesUMFactory.getAndCheckNumberOfDefaultVariableValues(new ConceptNodeTypeWrapper(this.item));
		
    	parents = this.item.getParents();
		if(parents.length == 0) {
			double[] probs = item.getFunctionValues();
    		double a = probs[0]*BetaDensity.SAMPLE_SIZE;
    		double b = BetaDensity.SAMPLE_SIZE - a;
			betas.add(new BetaDensity(a, b));
		}
		else {
	    	double[] probs = this.item.getFunctionValues();
	    	double[] values = new double[] {1.0, 0.0};
	    	BinaryArrangementWithRepetitionGenerator arrs = BinaryArrangementWithRepetitionGenerator.parse(base, parents.length);
	    	for(int i = 0; i < arrs.getNumArrangement(); i++) {
	    		int[] arr = arrs.getArrangement(i);
	    		double[] parentPattern = new double[parents.length];
	    		for(int j = 0; j < parents.length; j++) parentPattern[j] = values[arr[j]];
	    		
	    		//E.g.: parentPattern = 111010
	    		parentPatterns.add(parentPattern);
	    		double a = probs[i]*BetaDensity.SAMPLE_SIZE;
	    		double b = BetaDensity.SAMPLE_SIZE - a;
	    		betas.add(new BetaDensity(a, b));
	    	}
		}
	}
	public ArrayList<BetaDensity> getBetas() {
		return betas;
	}
	public BetaDensity getOneBeta() {
		if(hasParent()) return null;
		return betas.get(0);
	}
	public BetaDensity getBeta(ArrayList<String> attrs, double[] pattern) throws ZebraException {
		//E.g: pattern = 11100
		//If pattern[j]==-1: have no this attribute
		if(attrs.size() != pattern.length) throw new ZebraException("Invalid parameters");
		if(!hasParent()) return null;
		
		ArrayList<String> parentNames = new ArrayList<String>();
		for(OverlayBayesItem item : parents) parentNames.add(item.getName());
		
		double[] find = new double[parentNames.size()];
		for(int i = 0; i < parentNames.size(); i++) {
			int j = attrs.indexOf(parentNames.get(i));
			if(j==-1) return null;
			if(pattern[j] == -1) return null;//this is missing value
			find[i] = pattern[j];
		}
		return getBeta(find);
	}
	public void updateBackItem() throws ZebraException {
		int  base = OverlayBayesUMFactory.getAndCheckNumberOfDefaultVariableValues(new ConceptNodeTypeWrapper(this.item));
    	
		if(parents.length == 0) {
			double[] probs = item.getFunctionValues();
    		BetaDensity beta = betas.get(0); beta.repair();
    		probs[0] = beta.expectation();
    		probs[1] = 1.0 - probs[0];
    		item.setFunctionValues(probs);
		}
		else {
			int r = parents.length;
	    	int stats = (int) (Math.pow(base, r + 1));
	    	
	    	double[] temp_probs = new double [stats / 2];
	    	for(int i = 0; i < temp_probs.length; i++) {
	    		BetaDensity beta = betas.get(i); beta.repair();
	    		temp_probs[i] = beta.expectation();
	    	}
	    	
	    	double[] probs = new double [stats];
	    	for(int i = 0; i < stats / 2; i++) {
	    		probs[i] = (float)temp_probs[i];
	    	}
	    	for(int j = stats / 2; j < stats; j++)  probs[j] = 1 - probs[j - stats / 2];
			
    		item.setFunctionValues(probs);
		}
	}
	public boolean hasParent() {
		return (parents.length > 0);
	}
	
	public OverlayBayesItem getItem() {return item;}
	
	public String getOneProbName() {
		if(hasParent()) return "";
		return "Pr(" + item.getName() + "=1)";
	}
	public String getParentProbName(int idx) {
		return translateParentPattern(parentPatterns.get(idx));
	}
	public String getParentProbName(String parentName) {
		int idx = parentPatterns.indexOf(parentName);
		if(idx == -1) return "";
		return translateParentPattern(parentPatterns.get(idx));
	}
	
	private String translateParentPattern(double[] parentPattern) {
		String str = "";
		if(!hasParent()) return str;
		str += "Pr(" + item.getName() + "=1 | ";
		for(int i = 0; i < parentPattern.length; i++) {
			String parentName = parents[i].getName();
			str += parentName + "=" + ((int)parentPattern[i]);
			if(i < parentPattern.length - 1) str += ",";
		}
		str += ")";
		return str;
	}
	
	private BetaDensity getBeta(double[] parentPattern) {
		//Must have parents
		for(int i = 0; i < parentPatterns.size(); i++) {
			double[] thispattern = parentPatterns.get(i);
			if(thispattern.length != parentPattern.length) continue;
			
			boolean found = true;
			for(int j = 0; j < thispattern.length; j++) {
				if(thispattern[j] != parentPattern[j]) {
					found = false; break;
				}
			}
			if(found) return betas.get(i);
		}
		return null;
	}
}
