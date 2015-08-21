/**
 * 
 */
package vn.spring.zebra.mining.textmining;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.util.MathUtil;


/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class Keyword {
	public enum NOMINAL_KEYWORD_VALUE {TRUE, FALSE};

	public String name = null;
	public int    termFreq = 0;
	public int    totalTermFreq = 0;
	
	public int    docFreq = 0;
	public int    totalDocFreq = 0;
	
	@Override
	public boolean equals(Object obj) {
		return this.name.toLowerCase().equals(((Keyword)obj).name.toLowerCase());
	}
	
	@Override
	public String toString() {
		return name + ":" + MathUtil.round(tf(), 8) + "x" + MathUtil.round(idf(), 8); 
	}
	
	public double tf() {
		if(termFreq == 0) return 0;
		return (double)termFreq / (double)totalTermFreq;
	}
	public double idf() {
		if(docFreq == 0 && termFreq > 0) {
			docFreq = 1;
			totalDocFreq = 1;
		}
		if(docFreq == 0) return 0;
			
		return (double)1 / (double)docFreq;
	}
	public double weight() {
		return tf();
	}
	public double tf_idf() {
		return tf()*idf();
	}
	public NOMINAL_KEYWORD_VALUE norminalValue() {
		return norminalValueOf(weight());
	}
	
	public static NOMINAL_KEYWORD_VALUE norminalValueOf(double tf) {
		if(tf < ZebraStatic.CLASSIFIER_THRESHOLD) 
			return NOMINAL_KEYWORD_VALUE.FALSE;
		else 
			return NOMINAL_KEYWORD_VALUE.TRUE;
	}
	public static String nominalToString(NOMINAL_KEYWORD_VALUE value) {
		if(value == NOMINAL_KEYWORD_VALUE.TRUE) 
			return "1";
		else
			return "0";
	}
	public static NOMINAL_KEYWORD_VALUE stringToNominal(String value) {
		value = value.toLowerCase();
		if(value.equals("1")) 
			return NOMINAL_KEYWORD_VALUE.TRUE;
		else
			return NOMINAL_KEYWORD_VALUE.FALSE;
	}
	
	public static NOMINAL_KEYWORD_VALUE[] getDefaultNominalKeywordValue() {
		NOMINAL_KEYWORD_VALUE[] values = new NOMINAL_KEYWORD_VALUE[2];
		values[0] = NOMINAL_KEYWORD_VALUE.FALSE;
		values[1] = NOMINAL_KEYWORD_VALUE.TRUE;
		return values;
	}

	public static String[] getDefaultNorminalKeywordValueStr() {
		NOMINAL_KEYWORD_VALUE[] values = getDefaultNominalKeywordValue();
		String[] s_values = new String[values.length];
		for(int i = 0; i < values.length; i++) {
			s_values[i] = nominalToString(values[i]);
		}
		return s_values;
	}
}
