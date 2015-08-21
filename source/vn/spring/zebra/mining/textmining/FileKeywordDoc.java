/**
 * 
 */
package vn.spring.zebra.mining.textmining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import vn.spring.WOW.WOWDB.ConceptDB;
import vn.spring.WOW.datacomponents.Concept;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.um.ConceptNodeTypeWrapper;
import vn.spring.zebra.util.ConceptUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class FileKeywordDoc extends AbstractKeywordDoc {
	
	public static ArrayList<KeywordDoc> loadCorpus(String course, String[] keywordNamePattern) {
		ArrayList<KeywordDoc> docs = new ArrayList<KeywordDoc>();
		
	    File  docDir = new File(ZebraStatic.getWowRoot() + course + "/xhtml");
	    if(!docDir.exists()) docDir = new File(ZebraStatic.getWowRoot() + course + "/xml");
	    if(!docDir.exists()) docDir = new File(ZebraStatic.getWowRoot() + course + "/html");
	    
        File[] filelist = docDir.listFiles(new FileFilter() {
			public boolean accept(File f) {
				String filename = f.getName();
				if(filename.endsWith(".xml") || filename.endsWith(".xhtml") || filename.endsWith(".html") ||
					filename.endsWith(".txt"))
					return true;
				return false;
			}
        });
        
		ConceptDB cdb = ZebraStatic.getConceptDB();
        for(File f : filelist) {
        	String docClass = null;
        	try {
        		docClass = ConceptUtil.getLinkedConceptOfRealFilePath(f.getAbsolutePath());
        		Concept concept = cdb.getConcept(cdb.findConcept(docClass));
        		ConceptNodeTypeWrapper type = new ConceptNodeTypeWrapper(concept);
    			if(type.isEvidence()) continue;
        	}
        	catch(Exception e) {
        		System.out.println("CommonUtil.getLinkedConceptOfRealFilePath in FileKeywordDoc.loadCorpus causes error: " + e.getMessage()); 
        		docClass = null;
        	}
        	if(docClass == null || AbstractKeywordDoc.isNoneDocClass(docClass)) continue;
        	
        	HashMap<String, Integer> termFreqs = new HashMap<String, Integer>();
        	int totalFreq = 0;
        	try {
            	BufferedReader reader = new BufferedReader(new FileReader(f));
            	StringBuffer data = new StringBuffer();
            	String line = null;
            	while( (line = reader.readLine()) != null) {data.append(line + "\n");}
            	
            	totalFreq = TextMiningUtil.extractTerms(termFreqs, data.toString(), keywordNamePattern);
            	if(totalFreq == 0 || termFreqs.size() == 0) continue;
        	}
        	catch(Exception e) {e.printStackTrace(); continue;}
            	
        	KeywordDoc doc = new FileKeywordDoc();
        	Set<String> terms = termFreqs.keySet();
        	
        	for(String term : terms) {
        		boolean found = false; //are there doc keyword
        		for(int i = 0; i < docs.size(); i++) {
        			KeywordDoc tempdoc = docs.get(i);
        			int idx = tempdoc.findKeyword(term);
        			if(idx != -1) {
        				found = true;
        				tempdoc.getKeyword(idx).docFreq = tempdoc.getKeyword(idx).docFreq + 1; //update doc frequency remaining docs
        			}
        		}
        		if(!found) {
	        		Keyword keyword = new Keyword();
	        		keyword.name = term;
	        		keyword.termFreq = termFreqs.get(term).intValue();
	        		keyword.totalTermFreq = totalFreq;
	        		keyword.docFreq = 1;
	        		doc.addKeyword(keyword);
        		}
        	}
        	
			if(doc.getKeywordCount() > 0) {
				doc.setDocClass(docClass);
				docs.add(doc);
			}
        }//end for
        
		for(int i = 0; i < docs.size(); i++) {
			KeywordDoc doc = docs.get(i);
			for(int j = 0; j < doc.getKeywordCount(); j++) {
				Keyword keyword = doc.getKeyword(j);
				keyword.totalDocFreq = docs.size();
			}
		}
		return docs;
	}
	
	
}
