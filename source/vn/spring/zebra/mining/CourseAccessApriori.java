package vn.spring.zebra.mining;

import java.io.*;
import java.util.*;

import vn.spring.WOW.datacomponents.DotString;
import vn.spring.WOW.engine.ConceptInfo;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.log.*;
import vn.spring.zebra.util.CommonUtil;
import vn.spring.zebra.util.ConceptUtil;
import vn.spring.zebra.util.FireEventTask;
import vn.spring.zebra.util.FireEventUtil;
import weka.associations.Apriori;
import weka.associations.ItemSet;
import weka.core.Instances;
import weka.core.Capabilities.Capability;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseAccessApriori extends Apriori {
	private static final long serialVersionUID = 1L;
	
	protected String userid = null;
	protected String course = null;
	protected ArrayList<ConceptInfo> filtered = new ArrayList<ConceptInfo>();
	protected double min_sup = ZebraStatic.RECOMMEND_MIN_SUP;
	
	protected HashSet<CourseAccessAprioriChangeListener> listeners = new HashSet<CourseAccessAprioriChangeListener>();

	private CourseAccessLog log = null;
	protected boolean       ownPrivateLog = true;// not use public log
	private String          arff = null;
	public  String          getArff() {return arff;}

	public ArrayList<ConceptInfo> getFiltered() {return filtered;}
	public String getUserId() {return userid;}
	public String getCourse() {return course;}
	
	public synchronized void setParameters(String userid, String course, ArrayList<ConceptInfo> filtered, double min_sup) {
		this.userid = userid;
		this.course = course;
		this.filtered = filtered;
		this.min_sup = min_sup;
		this.ownPrivateLog = true;
	}
	public synchronized void setParameters(CourseAccessLog log,
			String userid, String course, ArrayList<ConceptInfo> filtered, double min_sup) {
		setParameters(userid, course, filtered, min_sup);
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
		
		CourseAccessLog   log = null;
		if(this.ownPrivateLog) {
			this.log = ZebraStatic.getLogDB().getCourseAccessLog(this.course, ZebraStatic.MINER_LOG_BEGIN_DATE, ZebraStatic.IGNORE_ANONYMOUS);
			log = this.log;
		}
		else {
			synchronized(this.log) {
				log = (CourseAccessLog)this.log.clone();
			}
		}
		//ZebraStatic.traceService.trace("--- Course Access Log ---\n" + log.toString());
		if(log.size() == 0) return;

		StringWriter      swLog = new StringWriter();
		PrintWriter       pwLog = new PrintWriter(swLog);
		boolean           emptyEnd = true;
		ArrayList<String> backup = new ArrayList<String>(); 
		
		pwLog.println("@relation " + log.coursename + "\n");
		for(int i= 0; i < cNames.size(); i++)
		{
			String concept_i = "concept" + i; 
			pwLog.print("@attribute " + concept_i + " {");
			pwLog.println(CommonUtil.concatNames(cNames,",") + "}");
		}
		
		pwLog.println();
		pwLog.println("@data");
		//Duyet tung user
		for (int i=0; i < log.size(); i++) {
			UserAccessLog ualog = log.getUserLog(i);
    		int m = ualog.size();
    		//duyet tung session
			for (int j=0; j < m; j++) {
				UserAccessSession uasession = ualog.getSession(j);
	    		int u = uasession.size();
	    		ArrayList<String> items = new ArrayList<String>();
	    		
	    		//duyet tung record trong session
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
	    		
	    		if(items.size() < 2) continue;//each type has two item at least
	    		int remain = cNames.size() - items.size();//so luong du lieu missing
	    		for(int k = 0; k < remain; k++) {
	    			items.add("?");//nguoc lai thay missing data bang '?'
	    		}
	    		if(!items.get(items.size() - 1).equals("?")) emptyEnd = false;//muc du lieu cuoi cung cua 1 record
	    		String data = CommonUtil.concatNames(items, ",");
	    		backup.add(data);
			}
		}//end for data
		
		//Neu moi muc du lieu cuoi cung cua moi row la missing data
		if(emptyEnd && backup.size() > 0) {
			for(int i = backup.size() - 1; i >=0; i--) {
				DotString ds = new DotString(backup.get(i), ",");
				int notIdx = ds.indexOfNot("?");
				if(notIdx != -1) {
					int last = ds.size() - 1; 
					if(ds.get(last).equals("?")) ds.set(last, ds.get(notIdx));
					backup.set(i, ds.toString());
					break;
				}
			}
		}
		if(backup.size() < 2) {
			System.out.println("NOTICE: data must have at least two record! So crazy!");
			return;
		}
		
		
		for(int i = 0; i < backup.size(); i++) pwLog.println(backup.get(i));
		pwLog.flush();
		String pwLogData = swLog.getBuffer().toString();
		swLog.close();
		pwLog.close();
		
		//ZebraStatic.traceService.trace("--- Course Access Log data which will be mined ---\n" + pwLogData);

		StringReader arff = new StringReader(pwLogData);
		Instances instances = new Instances(arff);
		arff.close();
		
		resetOptions();
        //m_minSupport = m_upperBoundMinSupport (default 1.0)- m_delta (default 0.05);
        //m_minSupport = (m_minSupport < m_lowerBoundMinSupport (default 0.1)) ? m_lowerBoundMinSupport : m_minSupport;
		setDelta(1.0);
		setUpperBoundMinSupport(1.0);
		setLowerBoundMinSupport(this.min_sup);
		if(!getCapabilities().handles(Capability.MISSING_VALUES))
			getCapabilities().enableDependency(Capability.MISSING_VALUES);
		if(!getCapabilities().handles(Capability.MISSING_CLASS_VALUES))
			getCapabilities().enableDependency(Capability.MISSING_CLASS_VALUES);
		buildAssociations(instances);
		
		//ZebraStatic.traceService.trace("--- Results of Course Access Log mining--- \n" + this.toString());
		this.arff = pwLogData;
		CourseAccessAprioriChangeEvent evt = new CourseAccessAprioriChangeEvent(this, this.filtered, this.arff, this.getAssociationRules());
		FireEventUtil.fireEvent(new FireChangeEventTask(evt), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
	
	public synchronized ArrayList<AssociationRule> getAssociationRules() {
		ArrayList<AssociationRule> rules = new ArrayList<AssociationRule>();
		if(m_allTheRules == null || m_allTheRules[0] == null) return rules;
        for (int i = 0; i < m_allTheRules[0].size(); i++) {
        	AssociationRule rule = new AssociationRule(
	        		m_instances,
	        		((ItemSet)m_allTheRules[0].elementAt(i)),
	        		((ItemSet)m_allTheRules[1].elementAt(i)),
	        		((Double)m_allTheRules[2].elementAt(i)).doubleValue()
        		);
    		String leftStr = rule.left().toString2(rule.getInstances());
    		String rightStr = rule.right().toString2(rule.getInstances());
        	if( rule.left().equals(rule.right()) || leftStr.equals(rightStr)) { //left must be different from right
        		continue;//ignore rules have the same left and right
        	}
        	rules.add(rule);
        }
        
//    	Collections.sort(rules, 
//				new Comparator() {
//					public int compare(Object o1, Object o2) {
//						AssociationRule r1 = (AssociationRule)o1;
//						AssociationRule r2 = (AssociationRule)o2;
//						if(r1.confidence() < r2.confidence())
//							return 1;
//						else if(r1.confidence() == r2.confidence())
//							return 0;
//						else
//							return -1;
//					}
//				}
//			);        
		return rules;
	}
	
	public synchronized ArrayList<String> getPreRecommendedConcepts(String cName, int maxConcept) {
		ArrayList<String> rNames = new ArrayList<String>();
		ArrayList<AssociationRule> rules = getAssociationRules();
		if(rules.size() == 0) return rNames;
		
		for(int i = 0; i < rules.size(); i++) {
			if(maxConcept > 0 && rNames.size() == maxConcept) break;
			AssociationRule rule = rules.get(i);
			ArrayList<String> leftNames = rule.left().getItemNames(m_instances);
			if(leftNames.size() != 1) continue;
			
			if(leftNames.get(0).equals(cName)) {
				ArrayList<String> rightNames = rule.right().getItemNames(m_instances);
				for(int j = 0; j < rightNames.size(); j++) {
					String rightName = rightNames.get(j);
					if(rNames.indexOf(rightName) == -1) rNames.add(rightName);
				}
			}
		}
		return rNames;
	}
	public synchronized ArrayList<String> getPostRecommendedConcepts(String cName, int maxConcept) {
		ArrayList<String> lNames = new ArrayList<String>();
		ArrayList<AssociationRule> rules = getAssociationRules();
		if(rules.size() == 0) return lNames;
		
		for(int i = 0; i < rules.size(); i++) {
			if(maxConcept > 0 && lNames.size() == maxConcept) break;
			AssociationRule rule = rules.get(i);
			ArrayList<String> leftNames = rule.left().getItemNames(m_instances);
			if(leftNames.size() != 1) continue;
			String leftName = leftNames.get(0);
			
			ArrayList<String> rightNames = rule.right().getItemNames(m_instances);
			if(rightNames.indexOf(cName) != -1 && lNames.indexOf(leftName) == -1) {
				lNames.add(leftName);
			}
		}
		return lNames;
	}

	public synchronized void addChangeListener(CourseAccessAprioriChangeListener listener) {
		listeners.add(listener);
	}
	public synchronized void removeChangeListener(CourseAccessAprioriChangeListener listener) {
		listeners.remove(listener);
	}

	private class FireChangeEventTask implements FireEventTask {
		private CourseAccessAprioriChangeEvent evt = null;
		public FireChangeEventTask(CourseAccessAprioriChangeEvent evt) {
			this.evt = evt;
		}
		public void run() {
			for(CourseAccessAprioriChangeListener listener : listeners) {
				try {
					listener.courseAccessChanged(evt);
				}
				catch(Exception e) {
					ZebraStatic.traceService.trace("CourseAccessApriori.fireChangeEvent causes error: " + e.getMessage());
				}
			}
		}
	}

}
