/**
 * 
 */
package vn.spring.zebra.mining.textmining;

import java.util.ArrayList;

import vn.spring.zebra.mining.textmining.Keyword.NOMINAL_KEYWORD_VALUE;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public abstract class AbstractKeywordDoc implements KeywordDoc {
	protected ArrayList<Keyword> keywords = new ArrayList<Keyword>();
	protected String docClass = KeywordDoc.NONE_DOC_CLASS;

	public int findKeyword(String keywordName) {
		for(int i = 0; i < keywords.size(); i++) {
			if(keywords.get(i).name.equals(keywordName)) return i;
		}
		return -1;
	}
	public Keyword getKeyword(int idx) {
		return keywords.get(idx);
	}
	public int getKeywordCount() {return keywords.size();}
	
	public void addKeyword(Keyword keyword) {
		if(findKeyword(keyword.name) != -1) return;
		keywords.add(keyword);
	}
	public ArrayList<Keyword> getKeywordList() {
		return new ArrayList<Keyword>(keywords);
	}
	public String[] names() {
		String[] names = new String[keywords.size()];
		for(int i = 0; i < names.length; i++)
			names[i] = keywords.get(i).name;
		return names;
	}
	public double[] weights() {
		double[] weights = new double[keywords.size()];
		for(int i = 0; i < weights.length; i++)
			weights[i] = keywords.get(i).weight();
		return weights;
	}
	public NOMINAL_KEYWORD_VALUE[] nominalWeights() {
		NOMINAL_KEYWORD_VALUE[] nominalWeights = new NOMINAL_KEYWORD_VALUE[keywords.size()];
		for(int i = 0; i < nominalWeights.length; i++)
			nominalWeights[i] = keywords.get(i).norminalValue();
		return nominalWeights;
	}
	
	
	public String getDocClass() {return docClass;}
	public void setDocClass(String docClass) {
		if(docClass == null || docClass.length() == 0)
			this.docClass = NONE_DOC_CLASS;
		else
			this.docClass = docClass;
		
	}
	
	public boolean hasDocClass() {
		return !docClass.equals(KeywordDoc.NONE_DOC_CLASS);
	}
	public void clear() {
		keywords.clear();
	}
	
	public static boolean isNoneDocClass(String docClass) {
		return docClass.equals(KeywordDoc.NONE_DOC_CLASS) || docClass.length() == 0;
	}
	
	@Override
	public String toString() {
		StringBuffer to = new StringBuffer();
		to.append("[");
		for(int i = 0; i < keywords.size(); i++) {
			to.append(keywords.get(i) + ", ");
		}
		to.append(docClass + "]");
		return to.toString();
	}
	
}
