/**
 * 
 */
package vn.spring.zebra.mining.textmining;

import java.util.ArrayList;
import java.util.HashMap;

import vn.spring.zebra.util.CommonUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TextMiningUtil {
	public static String HTMLTAG = "<\\w*>";
	public static String HTMLENTITY = "&\\w*;";
	public static String NONWORD = "[^a-zA-Z]";
	public static String[] EXPLITIVE_WORDS =
		{
			"a", "an", "the", "that", "those", "these", "not",
			"is", "are", "was", "were", "been", "be", "being",
			"of", "about", "to", "from", "with", "within", "up", "down", "in", "into", "as", "among",
			"can", "can't", "cannot", "could", "couldn't", "may", "might", "should", "shouldn't", "shall", "will", "won't",
			"have", "haven't", "has", "hasn't", 
			"who", "whom", "how", "where", "what", "which", "why"
		};

	public static int extractTerms(HashMap<String, Integer> outTermFreqs, String data, String[] pattern) {
		outTermFreqs.clear();
		
		String[] terms = wordSeg(data);
		int totalFreq = 0;
		for(String term : terms) {
			if(term == null || term.length() == 0) continue;
			
			totalFreq++;
			if(pattern != null && pattern.length > 0 && !CommonUtil.contains(term, pattern)) continue;
			
			Integer freq = outTermFreqs.get(term);
			if(freq == null)
				outTermFreqs.put(term, 1);
			else
				outTermFreqs.put(term, freq.intValue() + 1);
		}
		return totalFreq;
	}
	
	public static String[] wordSeg(String data) {
		ArrayList<String> words = new ArrayList<String>();
		data = data.replaceAll(HTMLTAG, " ").replaceAll(HTMLENTITY, " ").toLowerCase();
		
		String[] tempwords = data.split(NONWORD);
		for(int i = 0; i < tempwords.length; i++) {
			tempwords[i] = tempwords[i].trim();
			if(tempwords[i].length() == 0) continue;
			if(isExpletiveWord(tempwords[i])) continue;
			words.add(tempwords[i]);
		}
		String[] result = new String[words.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = words.get(i);
		}
		return result;
	}
	public static boolean doesQueryContains(String query, String keyword) {
		if(query.length() == 0 || keyword.length() == 0) return false;
		query = query.toLowerCase();
		keyword = keyword.toLowerCase();
		String[] words = TextMiningUtil.wordSeg(query);
		for(int i = 0; i < words.length; i++) {
			if(query.indexOf(words[i]) == -1) return false;
		}
		return true;
	}

	private static boolean isExpletiveWord(String word) {
		word = word.trim().toLowerCase();
		for(int i = 0; i < EXPLITIVE_WORDS.length; i++)
			if(word.equals((EXPLITIVE_WORDS[i].toLowerCase()))) return true;
		return false;
		
	}
	public static void main(String[] args) {
		String data = 
			"<html>adbdsbnds \n fs ; &quot; sg 9 dfgds , kf # sfg } sgs { \r bxb <table> | sdfsa < fa \" sgsf";
		String[] terms = wordSeg(data);
		for(int i = 0; i < terms.length; i++)
			System.out.println(terms[i]);
	}
}
