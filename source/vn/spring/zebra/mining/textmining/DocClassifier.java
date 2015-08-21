/**
 * 
 */
package vn.spring.zebra.mining.textmining;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import vn.spring.WOW.datacomponents.Concept;
import vn.spring.WOW.engine.ConceptInfo;
import vn.spring.WOW.engine.CourseInfo;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.mining.DocClassifierChangeEvent;
import vn.spring.zebra.mining.DocClassifierChangeListener;
import vn.spring.zebra.mining.textmining.Keyword.NOMINAL_KEYWORD_VALUE;
import vn.spring.zebra.search.SearchUserDoc;
import vn.spring.zebra.um.ConceptNodeTypeWrapper;
import vn.spring.zebra.util.CommonUtil;
import vn.spring.zebra.util.ConceptUtil;
import vn.spring.zebra.util.FireEventTask;
import vn.spring.zebra.util.FireEventUtil;
import weka.classifiers.trees.Id3;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class DocClassifier extends Id3 {
	private static final long serialVersionUID = 1L;

	private String course = null;
	protected Collection<KeywordDoc> docs = new ArrayList<KeywordDoc>();
	protected String[] keywordNamePattern = new String[0]; 
	protected String[] docClasses = new String[0];
	protected HashSet<DocClassifierChangeListener> listeners = new HashSet<DocClassifierChangeListener>();
	
	public DocClassifier(String course) {
		super();
		this.course = course;
	}
	public String getCourse() {return course;}
	
	public synchronized String[] getKeywordPattern() {return keywordNamePattern;}
	
	public synchronized String findDocClassOf(KeywordDoc doc) {
		if(doc.getKeywordCount() == 0) return KeywordDoc.NONE_DOC_CLASS;
		
		if (m_Attribute == null) {
			if (Instance.isMissingValue(m_ClassValue)) {
				return KeywordDoc.NONE_DOC_CLASS;
			}
			else {
				return m_ClassAttribute.value((int) m_ClassValue);
			} 
		}
		else {
			for (int j = 0; j < m_Attribute.numValues(); j++) {
				String keywordName = m_Attribute.name();
				int found = doc.findKeyword(keywordName);
				if(found == -1) continue;
				
				NOMINAL_KEYWORD_VALUE value = Keyword.stringToNominal(m_Attribute.value(j));
				if(doc.getKeyword(found).norminalValue() == value)
					return wrapper(m_Successors[j]).findDocClassOf(doc);
			}
		}
	    return KeywordDoc.NONE_DOC_CLASS;
	}
	public synchronized void buildClassifier() throws Exception {
		String[] pattern = createKeywordNamePattern(course);
		ArrayList<KeywordDoc> docs = FileKeywordDoc.loadCorpus(course, pattern);
		ArrayList<ConceptInfo> cList = ConceptUtil.getConceptInfos(course, ZebraStatic.CLASSIFIER_DEEP_LEVEL_AS_DOCCLASS, true);
		ArrayList<ConceptInfo> filterList = new ArrayList<ConceptInfo>();
		for(ConceptInfo cInfo : cList) {
			ConceptNodeTypeWrapper type = new ConceptNodeTypeWrapper(cInfo);
			if(!type.isEvidence()) filterList.add(cInfo);
		}
		if(cList.size() == 0) return;
		String[] docClasses = new String[filterList.size()];
		for(int i = 0; i < docClasses.length; i++) 
			docClasses[i] = filterList.get(i).getConceptName();
		
		buildClassifier(docs, pattern, docClasses);
	}
	public synchronized void buildClassifier(Collection<KeywordDoc> docs, String[] docClasses) throws Exception {
		String[] keywordNamePattern = createKeywordNamePattern(docs);
		buildClassifier(docs, keywordNamePattern, docClasses);
	}
	
	private void buildClassifier(Collection<KeywordDoc> docs, 
			String[] keywordNamePattern, String[] docClasses) throws Exception {
		clear();
		if(docs.size() == 0 || keywordNamePattern.length == 0 || docClasses.length == 0) {
			System.out.println("ERROR: Invalid parameters");
			return;
		}
		this.docs = docs;
		this.keywordNamePattern = keywordNamePattern;
		this.docClasses = docClasses;
		for(KeywordDoc doc : docs) {
			for(int i = 0; i < docClasses.length; i++) {
	    		if(ConceptUtil.isDecendentOf(course, docClasses[i], doc.getDocClass())) {
	    			doc.setDocClass(docClasses[i]);
	    			break;
	    		}
			}
		}
		
		StringBuffer arff = new StringBuffer();
		arff.append("@relation " + course + "\n\n");
		
		for(int i = 0; i < keywordNamePattern.length; i++) {
			arff.append("@attribute " + keywordNamePattern[i] + 
				" {" + CommonUtil.concatNames2(Keyword.getDefaultNorminalKeywordValueStr(), ",") + "}\n");
		}
		arff.append("@attribute class " + "{" + CommonUtil.concatNames(docClasses, ",") + "}\n\n");
		
		arff.append("@data\n\n");
		for(KeywordDoc doc : docs) {
			if(!CommonUtil.containsNoCase(doc.getDocClass(), docClasses)) continue;
			try {
				arff.append(createInstanceStr(doc, keywordNamePattern) + "\n");
			}
			catch(Exception e) {e.printStackTrace();}
		}
		
		StringReader reader = new StringReader(arff.toString());
		Instances instances = new Instances(reader);
		instances.setClassIndex(instances.numAttributes() - 1);
		reader.close();
		buildClassifier(instances);
		
		ZebraStatic.traceService.trace("INFO: Build classifier for course " + course + " successfully!");
		ZebraStatic.traceService.trace(toString());
		FireEventUtil.fireEvent(new FireChangeEventTask(new DocClassifierChangeEvent(this)), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}

	@Override
	public synchronized String toString() {
		if((m_Distribution == null) && (m_Successors == null)) {
			return "Classification: No model built yet.";
		}
		return "Classification Decision Rule:\n" + toString(0);
	}
	
	public synchronized String toHtml(CourseInfo courseInfo) {
		if((m_Distribution == null) && (m_Successors == null)) {
			return "Classification: No model built yet.";
		}
		return toHtml(0, courseInfo);
	}
	public synchronized void addChangeListener(DocClassifierChangeListener listener) {
		listeners.add(listener);
	}
	public synchronized void removeChangeListener(DocClassifierChangeListener listener) {
		listeners.remove(listener);
	}
	private class FireChangeEventTask implements FireEventTask {
		private DocClassifierChangeEvent evt = null;
		public FireChangeEventTask(DocClassifierChangeEvent evt) {
			this.evt = evt;
		}
		public void run() {
			for(DocClassifierChangeListener listener : listeners) {
				try {
					listener.docClassifierChanged(evt);
				}
				catch(Exception e) {
					ZebraStatic.traceService.trace("DocClassifier.fireChangeEvent causes error: " + e.getMessage());
				}
			}
		}
	}

	private void clear() {
		m_Successors = null;
		m_Attribute = null;
		m_ClassValue = 0;
		m_Distribution = null;
		m_ClassAttribute = null;

		docs = new ArrayList<KeywordDoc>();
		keywordNamePattern = new String[0]; 
		docClasses = new String[0];
	}

	protected static String[] createKeywordNamePattern(Collection<KeywordDoc> docs) {
		Collection<Keyword> keywords = new HashSet<Keyword>();
		for(KeywordDoc doc : docs) {
			ArrayList<Keyword> temp = doc.getKeywordList();
			Collections.sort((ArrayList<Keyword>)temp, new Comparator<Keyword>() {
				public int compare(Keyword o1, Keyword o2) {
					double tf_idf1 = o1.tf_idf(), tf_idf2 = o2.tf_idf();
					if(tf_idf1 < tf_idf2)       return 1;
					else if(tf_idf1 == tf_idf2) return 0;
					else                        return -1;
				}
			});
			
			int n = Math.min(ZebraStatic.CLASSIFIER_MAX_KEYWORD, temp.size());
			for(int i = 0; i < n; i++) keywords.add(temp.get(i));
		}
		
		keywords = new ArrayList<Keyword>(keywords);
		Collections.sort((ArrayList<Keyword>)keywords, new Comparator<Keyword>() {
			public int compare(Keyword o1, Keyword o2) {
				double idf1 = o1.idf(), idf2 = o2.idf();
				if(idf1 < idf2)       return 1;
				else if(idf1 == idf2) return 0;
				else                  return -1;
			}
		});
		int n = Math.min(ZebraStatic.CLASSIFIER_MAX_KEYWORD, keywords.size());
		String[] keywordNamePattern = new String[n];
		for(int i = 0; i < keywordNamePattern.length; i++) {
			keywordNamePattern[i] = ((ArrayList<Keyword>)keywords).get(i).name;
		}
		return keywordNamePattern;
	}
	private static String[] createKeywordNamePattern(String course) {
		ArrayList<Concept> conceptList = ConceptUtil.getConceptList(course);
		StringBuffer data = new StringBuffer();
		for(Concept concept : conceptList) {
			data.append(concept.getTitle() + "\n");
		}
		
		HashMap<String, Integer> outTermFreqs = new HashMap<String, Integer>();
		int totalTermFreq = TextMiningUtil.extractTerms(outTermFreqs, data.toString(), null);
		if(totalTermFreq == 0) return new String[0];
		
		ArrayList<Entry<String, Integer>> termFreqs = new ArrayList(outTermFreqs.entrySet());
		Collections.sort(termFreqs, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				int freq1 = o1.getValue().intValue(), freq2 = o2.getValue().intValue();
				if(freq1 < freq2)       return 1;
				else if(freq1 == freq2) return 0;
				else                    return -1;
			}
		});
		
		int n = Math.min(ZebraStatic.CLASSIFIER_MAX_KEYWORD, termFreqs.size());
		String[] pattern = new String[n];
		for(int i = 0; i < pattern.length; i++) pattern[i] = termFreqs.get(i).getKey();
		return pattern;
	}
	private static DocClassifier wrapper(Id3 id3) {
		DocClassifier docClassifier = new DocClassifier("");
		docClassifier.m_Successors = id3.m_Successors;
		docClassifier.m_Attribute = id3.m_Attribute;
		docClassifier.m_ClassValue = id3.m_ClassValue;
		docClassifier.m_Distribution = id3.m_Distribution;
		docClassifier.m_ClassAttribute = id3.m_ClassAttribute;
		return docClassifier;
	}
	private static String createInstanceStr(KeywordDoc doc, String[] keywordNamePattern) {
		StringBuffer instance = new StringBuffer();
		for(int i = 0; i < keywordNamePattern.length; i++) {
			String keywordName = keywordNamePattern[i];
			int found = doc.findKeyword(keywordName);
			if(found == -1) {
				instance.append(Keyword.nominalToString(NOMINAL_KEYWORD_VALUE.FALSE) + ",");
			}
			else {
				NOMINAL_KEYWORD_VALUE value = doc.getKeyword(found).norminalValue(); 
				instance.append(Keyword.nominalToString(value) + ",");
			}
		}
		instance.append(doc.getDocClass());
		return instance.toString();
	}
	
	public static void main(String[] args) {
		try {
			DocClassifier classifier = new DocClassifier("javatutorial");
			classifier.buildClassifier();
			System.out.println(classifier.toString());
			SearchUserDoc doc = new SearchUserDoc("guest", "javatutorial");
			doc.load(classifier.keywordNamePattern);
			System.out.println("Guest: " + doc);
			String docClass = classifier.findDocClassOf(doc);
			System.out.println("Class of user \"guest\" is: " + docClass);
		}
		catch(Exception e) {e.printStackTrace();}
	}
}
