package vn.spring.zebra.mining;

import java.util.ArrayList;
import java.util.HashSet;

import vn.spring.WOW.engine.ConceptInfo;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.log.CourseAccessLog;
import vn.spring.zebra.log.UserAccessLog;
import vn.spring.zebra.log.UserAccessSession;
import vn.spring.zebra.mining.gsp.Element;
import vn.spring.zebra.mining.gsp.GSP;
import vn.spring.zebra.mining.gsp.SequenceDatabase;
import vn.spring.zebra.mining.gsp.Sequence;
import vn.spring.zebra.util.ConceptUtil;
import vn.spring.zebra.util.FireEventTask;
import vn.spring.zebra.util.FireEventUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseAccessSequences2 extends GSP {
	protected String userid = null;
	protected String course = null;
	protected ArrayList<ConceptInfo> filtered = new ArrayList<ConceptInfo>();

	protected CourseAccessLog log = null;
	protected boolean         ownPrivateLog = true;// not use public log
	protected HashSet<CourseAccessSequences2ChangeListener> listeners = new HashSet<CourseAccessSequences2ChangeListener>();

	public String getUserId() {return userid;}
	public String getCourse() {return course;}
	
	public synchronized void setParameters(String userid, String course, ArrayList<ConceptInfo> filtered, double minSup) {
		this.userid = userid;
		this.course = course;
		this.filtered = filtered;
		this.minSup = minSup;
		this.ownPrivateLog = true; //not use public log
	}
	public synchronized void setParameters(CourseAccessLog publicLog,
			String userid, String course, ArrayList<ConceptInfo> filtered, double min_sup) {
		setParameters(userid, course, filtered, min_sup);
		this.log = publicLog;
		this.ownPrivateLog = false; //use public log
	}

	public synchronized void perform() throws Exception, ZebraException {
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
		
		SequenceDatabase db = new SequenceDatabase(cNames);
		if(ZebraStatic.SEQUENCE_PERFORM_RANDOM) {
			for (int i=0; i < log.size(); i++) {
				db.addSeq(db.genRandomSeq());
			}
		}
		else {
			//duyet tung user
			for (int i=0; i < log.size(); i++) {
				UserAccessLog ualog = log.getUserLog(i);
				Sequence seq = new Sequence();//each sequence is user
	    		int m = ualog.size();
	    		//duyet tung session
				for (int j=0; j < m; j++) {
					UserAccessSession uasession = ualog.getSession(j);
					//Get concept and its study time in accordance with descending list
					ArrayList<UserAccessSession.TimeIntervalConcept> tisession = uasession.getTimeIntervalSortedConceptList();
					
		    		int u = tisession.size();
		    		ArrayList<String> items = new ArrayList<String>();
		    		
		    		//browse each record (concept)
		    		for(int k = 0; k < u ; k++) {
		    			String conceptname = tisession.get(k).concept;
	
		    			//Kiem tra xem conceptname co tom tai trong cNames khong?
		    			if(cNames.indexOf(conceptname) < 0) continue;//neu khong tim thay thi cho qua
		    			
		    			//Kiem tra xem conceptname co ton tai trong items khong?
		    			if(items.indexOf(conceptname) >= 0) continue; //neu co roi thi cho qua
		    			
		    			if(items.size() < cNames.size()) items.add(conceptname);
		    		}
		    		//
		    		if(items.size() ==0) continue;
		    		
			    	Element element = new Element();
			    	for(int k = 0; k < items.size(); k++) {
			    		int item = db.indexOf(items.get(k));
			    		if(item == -1) continue;
			    		element.addItem(item);
			    	}
			    	if(element.length() < 2) continue;//each element has two item at least
			    	if(ZebraStatic.SEQUENCE_MAX_ITEM_IN_ITEMSET_MUST_BE_FIXED != -1 && element.length() > ZebraStatic.SEQUENCE_MAX_ITEM_IN_ITEMSET_MUST_BE_FIXED)
			    		element = element.clone(0, ZebraStatic.SEQUENCE_MAX_ITEM_IN_ITEMSET_MUST_BE_FIXED);
			    	seq.addElement(element); 
			    	//ZebraConfig.traceService.trace(seq.toString(db.getAttributes()) + "\n\n\n");
				}
				if(seq.length() > 0) db.addSeq(seq);
			}//end for
		}//end if SEQUENCE_PERFORM_RANDOM
		if(db.size() == 0) return;
		
		//Should turn on following line
		perform(db);
		FireEventUtil.fireEvent(new FireChangeEventTask(new CourseAccessSequences2ChangeEvent(this)), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
	public synchronized ArrayList<ArrayList<String>> getMaxLearningPath() {
		ArrayList<ArrayList<String>> path = new ArrayList<ArrayList<String>>();
		Sequence pattern = getMaximumSupportSequentialPattern();
		if(pattern == null) return path;
		
		for(int i = 0; i < pattern.getElements().size(); i++) {
			Element element = pattern.getElements().get(i);
			ArrayList<String> concepts = new ArrayList<String>();
			for(int j = 0; j < element.getItems().size(); j++) {
				int item = element.getItems().get(j);
				concepts.add( db.getAttributes()[item] );
			}
			if(concepts.size() > 0) path.add(concepts);
		}
		return path;
	}
	
	public synchronized void addChangeListener(CourseAccessSequences2ChangeListener listener) {
		listeners.add(listener);
	}
	public synchronized void removeChangeListener(CourseAccessSequences2ChangeListener listener) {
		listeners.remove(listener);
	}

	private class FireChangeEventTask implements FireEventTask {
		private CourseAccessSequences2ChangeEvent evt = null;
		public FireChangeEventTask(CourseAccessSequences2ChangeEvent evt) {
			this.evt = evt;
		}
		public void run() {
			for(CourseAccessSequences2ChangeListener listener : listeners) {
				try {
					listener.courseAccessChanged(evt);
				}
				catch(Exception e) {
					ZebraStatic.traceService.trace("CourseAccessSequences2.fireChangeEvent causes error: " + e.getMessage());
				}
			}
		}
	}
	
}
