package vn.spring.zebra.mining;

import weka.core.*;
import weka.core.Capabilities.Capability;
import weka.associations.GeneralizedSequentialPatterns;
import weka.associations.gsp.*;
import vn.spring.WOW.engine.ConceptInfo;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.log.*;
import vn.spring.zebra.util.CommonUtil;
import vn.spring.zebra.util.ConceptUtil;
import vn.spring.zebra.util.FireEventTask;
import vn.spring.zebra.util.FireEventUtil;

import java.io.*;
import java.util.*;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseAccessSequences extends GeneralizedSequentialPatterns {
	private static final long serialVersionUID = 1L;
	
	protected String course = null;
	protected String userid = null;
	protected ArrayList<ConceptInfo> filtered = new ArrayList<ConceptInfo>();
	protected double min_sup = ZebraStatic.RECOMMEND_MIN_SUP;

	protected HashSet<CourseAccessSequencesChangeListener> listeners = new HashSet<CourseAccessSequencesChangeListener>();

	protected CourseAccessLog log = null;
	protected boolean         ownPrivateLog = true;// not use public log
	protected String          arff = null;
	protected Instances       instances = null;
	
	public synchronized void setParameters(String course, String userid, ArrayList<ConceptInfo> filtered, double min_sup) {
		this.course = course;
		this.filtered = filtered;
		this.userid = userid;
		this.min_sup = min_sup;
		this.ownPrivateLog = true;
	}
	public synchronized void setParameters(CourseAccessLog log,
			String course, String userid, ArrayList<ConceptInfo> filtered, double min_sup) {
		setParameters(course, userid, filtered, min_sup);
		this.log = log;
		this.ownPrivateLog = false;
	}
	
	public synchronized void perform() throws ZebraException, Exception {
		if(ZebraStatic.MINER_CLUSTER_FOR_MINING_TASK) throw new ZebraException("Not support for clustering when mining");
		
		ArrayList<String>  cNames = new ArrayList<String>();
		ArrayList<ConceptInfo> cInfos = 
			( this.filtered.size() == 0? ConceptUtil.getConceptInfos(this.course) : this.filtered );
		for(ConceptInfo cInfo : cInfos) {
			if(ZebraStatic.MINER_MAX_ITEM != -1 && cNames.size() == ZebraStatic.MINER_MAX_ITEM) break;
			cNames.add(cInfo.getConceptName());
		}
		if(cNames.size() == 0) return;
		
		CourseAccessLog log = null;
		if(ownPrivateLog) {
			this.log = ZebraStatic.getLogDB().getCourseAccessLog(this.course, ZebraStatic.MINER_LOG_BEGIN_DATE, ZebraStatic.IGNORE_ANONYMOUS);
			log = this.log;
		}
		else {
			synchronized(this.log) {
				log = (CourseAccessLog)this.log.clone();
			}
		}
		
		StringWriter swLog = new StringWriter();
		PrintWriter pwLog = new PrintWriter(swLog);
		pwLog.println("@relation " + this.course + "\n");
		
		int    n = log.size();
		pwLog.print("@attribute sid {");
		String sids = "";
		for(int i = 0; i < n; i++)
		{
			sids += "" + i + ",";
		}
		sids = sids.substring(0, sids.length() - 1);
		pwLog.println(sids + "}");
		
		for(int i= 0; i < cNames.size(); i++)
		{
			String concept_i = "concept" + i; 
			pwLog.print("@attribute " + concept_i + " {");
			pwLog.println(CommonUtil.concatNames(cNames, ",") + "," + ZebraStatic.SEQUENCE_EMPTY_ITEM_NAME + "}");
		}
		
		pwLog.println();
		pwLog.println("@data");
		for (int i=0; i < n; i++) {
			UserAccessLog ualog = log.getUserLog(i);
    		int m = ualog.size();
			for (int j=0; j < m; j++) {
				UserAccessSession uasession = ualog.getSession(j);
	    		int u = uasession.size();
	    		ArrayList<String> items = new ArrayList<String>();
	    		
	    		for(int k = 0; k < u ; k++) {
	    			UserAccessRecord uarecord = uasession.getRecord(k);
	    			if(uarecord.isLogout()) continue; //neu logout thi bo qua
	    			String conceptname = uarecord.getConceptName();

	    			//Kiem tra xem conceptname co tom tai trong cNames khong?
	    			if(cNames.indexOf(conceptname) < 0) continue;//neu khong tim thay thi cho qua
	    			
	    			//Kiem tra xem conceptname co ton tai trong items khong?
	    			if(items.indexOf(conceptname) >= 0) continue; //neu co roi thi cho qua
	    			
	    			if(items.size() < cNames.size()) items.add(conceptname);
	    		}
	    		
	    		if(items.size() == 0) continue;
	    		int remain = cNames.size() - items.size();//so luong du lieu missing
	    		for(int k = 0; k < remain; k++) {
	    			items.add(ZebraStatic.SEQUENCE_EMPTY_ITEM_NAME);//nguoc lai thay missing data bang '?'
	    		}
	    		String data = i + "," + CommonUtil.concatNames(items, ",");
	    		pwLog.println(data);
			}
		}//end for data
		
		pwLog.flush();
		String pwLogData = swLog.getBuffer().toString();
		swLog.close();
		pwLog.close();

		//ZebraConfig.traceService.trace("--- Course Access Log data which will be mined ---\n" + pwLogData);
		StringReader arff = new StringReader(pwLogData);
		Instances instances = new Instances(arff);
		arff.close();
		
		resetOptions();
		setMinSupport(min_sup);
		if(!getCapabilities().handles(Capability.MISSING_VALUES))
			getCapabilities().enableDependency(Capability.MISSING_VALUES);
		if(!getCapabilities().handles(Capability.MISSING_CLASS_VALUES))
			getCapabilities().enableDependency(Capability.MISSING_CLASS_VALUES);
		buildAssociations(instances);
		
		this.instances = instances;
		this.arff = pwLogData;

		FireEventUtil.fireEvent(new FireChangeEventTask(new CourseAccessSequencesChangeEvent(this)), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
	public synchronized ArrayList<Sequence> getSequentialPatterns() {
		ArrayList<Sequence> patterns = new ArrayList<Sequence>();
		if(m_AllSequentialPatterns == null) return patterns;
		int n = m_AllSequentialPatterns.size();
		if(n == 0) return patterns;
		
	    for (int i = 0; i < n; i++) {
	        FastVector kSequences = (FastVector) m_AllSequentialPatterns.elementAt(i);
			for(int j = 0; j < kSequences.size(); j++) {
				Sequence kSequence = (Sequence)(kSequences.elementAt(j));
				kSequence = normalize(kSequence);
				if(kSequence != null) patterns.add(kSequence);
			}
	    }
		//sap thu tu tu thap len cao
		Collections.sort(patterns, 
				new Comparator() {
					public int compare(Object o1, Object o2) {
						Sequence s1 = (Sequence)o1;
						Sequence s2 = (Sequence)o2;
						if(s1.length() < s2.length())
							return -1;
						else if(s1.length() == s2.length())
							return 0;
						else
							return 1;
					}
				}
			);
		return patterns;
	}
	public synchronized ArrayList<ArrayList<String>> getMaxLearningPath() {
		ArrayList<ArrayList<String>> path = new ArrayList<ArrayList<String>>();
		ArrayList<Sequence> patterns = getSequentialPatterns();
		if(patterns.size() == 0) return path;
		Sequence pattern = patterns.get(patterns.size() - 1);
		for(int i = 0; i < pattern.getElements().size(); i++) {
			Element element = (Element) (pattern.getElements().elementAt(i));
			ArrayList<String> concepts = new ArrayList<String>();
			int[] events = element.getEvents();
			for(int j = 0; j < events.length; j++) {
				if (events[i] <= -1) continue;			
				concepts.add( instances.attribute(j).value(events[j]) );
			}
			if(concepts.size() > 0) path.add(concepts);
		}
		return path;
	}
	public synchronized ArrayList<SequentialRule> breakSequentialPatterns(String concept) {
		ArrayList<SequentialRule> rules = new ArrayList<SequentialRule>();		
		FastVector oneElements = Element.getOneElements(instances);//lay tat ca nhung element(itemset) co 1 event (item)
		int n = oneElements.size();  if(n == 0) return rules;
		//Tim element chua concept
		Element found = null;
		for(int i = 0; i < n; i++) {
			Element element = (Element) (oneElements.elementAt(i));
			if(element.toNominalString(instances).equals("{" + concept + "}")) {
				found = element; break;
			}
		}
		if(found == null) return rules;
		
		rules = breakSequentialPatterns(found);
		return rules;
	}
	public synchronized void addChangeListener(CourseAccessSequencesChangeListener listener) {
		listeners.add(listener);
	}
	public synchronized void removeChangeListener(CourseAccessSequencesChangeListener listener) {
		listeners.remove(listener);
	}
	
	private class FireChangeEventTask implements FireEventTask {
		private CourseAccessSequencesChangeEvent evt = null;
		public FireChangeEventTask(CourseAccessSequencesChangeEvent evt) {
			this.evt = evt;
		}
		public void run() {
			for(CourseAccessSequencesChangeListener listener : listeners) {
				try {
					listener.courseAccessChanged(evt);
				}
				catch(Exception e) {
					ZebraStatic.traceService.trace("CourseAccessSequences.fireChangeEvent causes error: " + e.getMessage());
				}
			}
		}
	}
	
	private ArrayList<SequentialRule> breakSequentialPatterns(weka.associations.gsp.Element lefthand) {
		ArrayList<SequentialRule> rules = new ArrayList<SequentialRule>();
		int n = m_AllSequentialPatterns.size();
		if(n == 0) return rules;
		
		//Tao danh sach cac 2-sequences tu patterns, ma ve trai chua lefthand
		ArrayList<Sequence> patterns = getSequentialPatterns();
		ArrayList<Sequence> twoSequences = new ArrayList<Sequence>(); 
		for(int h = patterns.size() - 1; h >= 0; h--) {//phai di nguoc vi lay sequence dai nhat
			//Tim sequence chua lefthand
			Sequence pattern = (Sequence)(patterns.get(h));
			FastVector elements = pattern.getElements();
			int i = 0;
			for(i = 0; i < elements.size(); i++) {
				weka.associations.gsp.Element element = (weka.associations.gsp.Element)(elements.elementAt(i));
				if(element.contains(lefthand) && lefthand.contains(element)) break;
			}
			if(i == elements.size()) continue;//khong tim thay
			
			//Tao danh sach tat ca 2-sequence, be gay pattern tren
			for(int j = i+1; j < elements.size(); j++) {
				Sequence seq = new Sequence();
				FastVector seqElements = seq.getElements();
				seqElements.addElement(lefthand);//chua lefhand
				seqElements.addElement(elements.elementAt(j));//va righthand la tu element i+1 tro di
				
				//Kiem tra xem seq da tong tai trong danh sach twoSequences chua
				boolean found = false;
				for(int k = 0; k < twoSequences.size(); k++) {
					Sequence temp = (Sequence) (twoSequences.get(k));
					if(seq.contains(temp) && temp.contains(seq)) {
						found = true;
						break;
					}
				}
				if(!found) twoSequences.add(seq);//neu chua thi them vao
			}
		}
		
		//Tim support cua lefhandseq
		Sequence lefhandseq = new Sequence();
		lefhandseq.getElements().addElement(lefthand);
		double lefhand_sup = 0;
		for(int i =0; i < n; i++) {
			FastVector freSeqs = (FastVector) m_AllSequentialPatterns.elementAt(i);
			int m = freSeqs.size();
			Sequence found = null;
			for(int j =0; j < m; j++) {
				Sequence seq2 = (Sequence) freSeqs.elementAt(j);
				if(seq2.contains(lefhandseq) && lefhandseq.contains(seq2))
				{
					found = seq2;
					break;
				}
			}
			if(found != null) {
				lefhand_sup = found.getSupportCount();
				break;
			}
		}
		if(lefhand_sup == 0) return rules;
		
		//Tao danh sach SeqquentialRule
		for(Sequence seq : twoSequences) {
			for(int i =0; i < n; i++) {
				FastVector freSeqs = (FastVector) m_AllSequentialPatterns.elementAt(i);
				int m = freSeqs.size();
				Sequence found = null;
				//tim trong cac pattern da co 2-seq chua
				for(int j =0; j < m; j++) {
					Sequence seq2 = (Sequence) freSeqs.elementAt(j);
					if(seq2.contains(seq) && seq.contains(seq2))
					{
						found = seq2;
						break;
					}
				}
				//neu co moi tao Rule
				if(found != null) {
					double sup = found.getSupportCount();
					if(sup == 0) continue;
					double confidence = sup / lefhand_sup; 
					SequentialRule rule = new SequentialRule(
							(weka.associations.gsp.Element) (seq.getElements().elementAt(0)),
							(weka.associations.gsp.Element) (seq.getElements().elementAt(1)), 
							confidence);
					rules.add(rule);
					break;
				}
			}
		}
		
		//sap thu tu tu cao xuong thap
		Collections.sort(rules, 
				new Comparator() {
					public int compare(Object o1, Object o2) {
						SequentialRule r1 = (SequentialRule)o1;
						SequentialRule r2 = (SequentialRule)o2;
						if(r1.confidence < r2.confidence)
							return 1;
						else if(r1.confidence == r2.confidence)
							return 0;
						else
							return -1;
					}
				}
			);
		return rules;
	}
	
	public static ArrayList<String> forExample(String course, String concept, double min_sup, String outputFileName) throws ZebraException, IOException, Exception {
		ArrayList<String> recommendedList = new ArrayList<String>();  
		
		CourseAccessLog courselog = ZebraStatic.getLogDB().getCourseAccessLog(course, ZebraStatic.MINER_LOG_BEGIN_DATE, ZebraStatic.IGNORE_ANONYMOUS); 
		CourseAccessSequences gsp = new CourseAccessSequences();
		gsp.setParameters(course, null, new ArrayList<ConceptInfo>(), ZebraStatic.RECOMMEND_MIN_SUP);
		gsp.perform();
		
		if(gsp == null) {return recommendedList;}
		
		ArrayList<SequentialRule> rules = gsp.breakSequentialPatterns(concept);
		recommendedList = SequentialRule.RecommendedConcepts(rules, new String[] {concept}, gsp.instances);
		String rules_str = SequentialRule.toString(rules, gsp.instances);
		
		if(outputFileName != null) {
			StringWriter log = new StringWriter();
			courselog.exportXML(log, true); log.close();
			
			PrintWriter ps = new PrintWriter(new File(outputFileName));
			ps.println("============================");
			ps.println("=User Access================");
			ps.println("============================");
			ps.println(log.toString());
			ps.println("============================");
			ps.println("=User Sequences=============");
			ps.println("============================");
			ps.println(gsp.arff);
			ps.println("============================");
			ps.println("=Sequential Pattern=========");
			ps.println("============================");
			ps.println(gsp.toString());
			ps.println("============================");
			ps.println("=Broken Sequential Rules====");
			ps.println("============================");
			ps.println(rules_str);
			ps.println("============================");
			ps.println("=Recommended Concepts=======");
			ps.println("============================");
			for(int i = 0; i < recommendedList.size(); i++)
				ps.println(recommendedList.get(i));
			ps.close();
		}
		return recommendedList;
	}
	private Sequence normalize(Sequence kSequence) {
		Sequence newSequence = new Sequence();
		int n = kSequence.getElements().size();
		for(int i = 0; i < n; i++) {
			Element element = (Element) (kSequence.getElements().elementAt(i));
			Element newElement = element.clone();
			int[] events = element.getEvents();
			for(int j = 0; j < events.length; j++) {
				if (events[i] <= -1) continue;			
				if( instances.attribute(j).value(events[j]).equals(ZebraStatic.SEQUENCE_EMPTY_ITEM_NAME) ) {
					newElement.getEvents()[j] = -1;
				}
			}
			if(!newElement.isEmpty()) newSequence.getElements().addElement(newElement);
		}
		if(newSequence.getElements().size() == 0) return null;
		return newSequence;
	}
	public static void main(String[] args) {
	}

}

