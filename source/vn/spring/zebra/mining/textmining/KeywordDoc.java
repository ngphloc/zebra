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
public interface KeywordDoc {
	final static String NONE_DOC_CLASS = "";
	
	int findKeyword(String keywordName);
	Keyword getKeyword(int idx);
	int getKeywordCount();
	void addKeyword(Keyword keyword);
	ArrayList<Keyword> getKeywordList();
	
	String[] names();
	double[] weights();
	NOMINAL_KEYWORD_VALUE[] nominalWeights();
	
	String getDocClass();
	void setDocClass(String docClass);
	boolean hasDocClass();
	
	public void clear();
}
