package vn.spring.zebra.mining;

import java.util.ArrayList;

import weka.core.Instances;
import weka.associations.gsp.Element;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class SequentialRule
{
	public Element lefthand = null;
	public Element righthand = null;
	public double confidence = 0;
	
	public SequentialRule(Element lefthand, Element righthand, double confidence) {
		this.lefthand = lefthand;
		this.righthand = righthand;
		this.confidence = confidence;
	}
	public String toString(Instances dataset) {
		String left = lefthand.nomalizeString(dataset);
		String right = righthand.nomalizeString(dataset);
		return left + "->" + right + ":" + confidence;
	}
	public String[] LeftConcept(Instances dataset) {
		String left = lefthand.nomalizeString(dataset);
		left = left.substring(1, left.length() - 1);
		return left.split(",");
	}
	public String[] RightConcept(Instances dataset) {
		String right = lefthand.nomalizeString(dataset);
		right = right.substring(1, right.length() - 1);
		return right.split(",");
	}
	public static ArrayList<String> RecommendedConcepts(ArrayList<SequentialRule> rules, String[] cNames, Instances dataset) {
		ArrayList<String> results = new ArrayList<String>();
		if(rules.size() == 0) return results;
		for(SequentialRule rule : rules) {
			String[] lefts = rule.LeftConcept(dataset);
			boolean found = true;
			for(int i = 0; i < cNames.length; i++) {
				int j = 0;
				for(j = 0; j < lefts.length; j++) {
					if(cNames[i].equals(lefts[j])) {
						break;
					}
				}
				if(j == lefts.length) {
					found = false;
					break;
				}
			}
			if(!found) continue;
			String[] rights = rule.RightConcept(dataset);
			for(int k = 0; k < rights.length; k++) {
				if(results.indexOf(rights[k]) < 0) results.add(rights[k]);
			}
		}
		return results;
	}
	public static String toString(ArrayList<SequentialRule> rules, Instances dataset) {
		if(rules.size() == 0) return "";
		String result = "";
		for(SequentialRule rule : rules) {
			result += rule.toString(dataset) + "\n";
		}
		return result.substring(0, result.length() - 1);
	}
}
