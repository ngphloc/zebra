package vn.spring.zebra.mining;

import vn.spring.WOW.engine.ConceptInfo;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.mining.textmining.DocClassifier;
import vn.spring.zebra.search.UserSearchService;
import vn.spring.zebra.search.UserSearchServiceDaemon;
import vn.spring.zebra.util.ConceptUtil;

import java.util.*;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ZebraMiner {
    private HashMap<String, CourseAccessAprioriDaemon> courseRecDaemons = new HashMap<String, CourseAccessAprioriDaemon>();
    private HashMap<String, CourseAccessSequencesDaemon> courseSeqDaemons = new HashMap<String, CourseAccessSequencesDaemon>();
    private HashMap<String, LearningHistoryDataDaemon> courseHisDataDaemons = new HashMap<String, LearningHistoryDataDaemon>();
    private HashMap<String, DocClassifierDaemon> courseDocClassifierDaemons = new HashMap<String, DocClassifierDaemon>();

    private HashMap<String, CourseAccessAprioriDaemon> userRecDaemons = new HashMap<String, CourseAccessAprioriDaemon>();
    private HashMap<String, CourseAccessSequencesDaemon> userSeqDaemons = new HashMap<String, CourseAccessSequencesDaemon>();
    private HashMap<String, LearningHistoryDataDaemon> userHisDataDaemons = new HashMap<String, LearningHistoryDataDaemon>();
    private HashMap<String, UserSearchServiceDaemon> userSearchServiceDaemons = new HashMap<String, UserSearchServiceDaemon>();
    

    protected boolean useGlobalDaemon = true;
    protected boolean ownPrivateLog = false;
    protected boolean isStarted = false;
    
    public ZebraMiner(boolean useGlobalDaemon, boolean ownPrivateLog) {
    	this.useGlobalDaemon = useGlobalDaemon;
    	this.ownPrivateLog = ownPrivateLog;
    	isStarted = true;
	}
    
    @Override
    protected void finalize() throws Throwable {
    	removeAllUserDaemons();
    	removeAllCourseDaemons();
    	super.finalize();
    }
    
	public CourseAccessApriori addCourseRecommenderDaemon(String course) throws Exception {
		if(!isStarted) {ZebraStatic.traceService.trace("Engine is stopped! Can't add daemon"); return null;}
		
		if(courseRecDaemons.get(course) != null)
			return courseRecDaemons.get(course).getCourseRecommend();
		CourseAccessApriori apriori = new CourseAccessApriori();
		ArrayList<ConceptInfo> filtered = ConceptUtil.getConceptInfos(course, ZebraStatic.RECOMMEND_DEEP_LEVEL, true);
		if(ownPrivateLog) {
			apriori.setParameters(null, course, filtered, ZebraStatic.RECOMMEND_MIN_SUP);
		}
		else {
			LearningHistoryDataDaemon hisDataDaemon = getCourseHisDataDaemon(course);
			LearningHistoryData hisData = null;
			if(hisDataDaemon != null)
				hisData = hisDataDaemon.getHisData();
			else
				hisData = addCourseHisDataDaemon(course);
			apriori.setParameters(hisData.getLog(), null, course, filtered, ZebraStatic.RECOMMEND_MIN_SUP);
		}
		apriori.perform();
		CourseAccessAprioriDaemon daemon = new CourseAccessAprioriDaemon(apriori);
		courseRecDaemons.put(course, daemon);
		
		daemon.fireMsgListeners("Perform Training Task in Recommender daemon (started)");
		return apriori;
	}
	public CourseAccessApriori addUserRecommenderDaemon(String userid, String course) throws Exception {
		if(!isStarted) {ZebraStatic.traceService.trace("Engine is stopped! Can't add daemon"); return null;}
		String id = ZebraStatic.makeId(userid, course);
		if(userRecDaemons.get(id) != null) return userRecDaemons.get(id).getCourseRecommend();
		if(useGlobalDaemon)                return addCourseRecommenderDaemon(course);
		
		CourseAccessApriori apriori = new CourseAccessApriori();
		ArrayList<ConceptInfo> filtered = ConceptUtil.getConceptInfos(course, ZebraStatic.RECOMMEND_DEEP_LEVEL, true);
		if(ownPrivateLog) {
			apriori.setParameters(userid, course, filtered, ZebraStatic.RECOMMEND_MIN_SUP);
		}
		else {
			LearningHistoryDataDaemon hisDataDaemon = getUserHisDataDaemon(userid, course);
			LearningHistoryData hisData = null;
			if(hisDataDaemon != null)
				hisData = hisDataDaemon.getHisData();
			else
				hisData = addCourseHisDataDaemon(course);
			apriori.setParameters(hisData.getLog(), userid, course, filtered, ZebraStatic.RECOMMEND_MIN_SUP);
		}
		apriori.perform();
		CourseAccessAprioriDaemon daemon = new CourseAccessAprioriDaemon(apriori);
		userRecDaemons.put(id, daemon);
		
		daemon.fireMsgListeners("Perform Training Task in Recommender daemon (started)");
		return apriori;
	}
	public CourseAccessSequences2 addCourseSequenceDaemon(String course) throws Exception {
		if(!isStarted) {ZebraStatic.traceService.trace("Engine is stopped! Can't add daemon"); return null;}

		if(courseSeqDaemons.get(course) != null)
			return courseSeqDaemons.get(course).getCourseSequence();
		CourseAccessSequences2 sequences = new CourseAccessSequences2();
		ArrayList<ConceptInfo> filtered = ConceptUtil.getConceptInfos(course, ZebraStatic.SEQUENCE_DEEP_LEVEL, true);

		if(ownPrivateLog) {
			sequences.setParameters(null, course, filtered, ZebraStatic.SEQUENCE_MIN_SUP);
		}
		else {
			LearningHistoryDataDaemon hisDataDaemon = getCourseHisDataDaemon(course);
			LearningHistoryData hisData = null;
			if(hisDataDaemon != null)
				hisData = hisDataDaemon.getHisData();
			else
				hisData = addCourseHisDataDaemon(course);
			sequences.setParameters(hisData.getLog(), null, course, filtered, ZebraStatic.SEQUENCE_MIN_SUP);
		}
		
		sequences.perform();
		CourseAccessSequencesDaemon daemon = new CourseAccessSequencesDaemon(sequences);
		courseSeqDaemons.put(course, daemon);
		
		daemon.fireMsgListeners("Perform Training Task in Learning Path daemon (started)");
		return sequences;
	}
	
	public CourseAccessSequences2 addUserSequenceDaemon(String userid, String course) throws Exception {
		if(!isStarted) {ZebraStatic.traceService.trace("Engine is stopped! Can't add daemon"); return null;}
		String id = ZebraStatic.makeId(userid, course);
		if(userSeqDaemons.get(id) != null) return userSeqDaemons.get(id).getCourseSequence();
		if(useGlobalDaemon)                return addCourseSequenceDaemon(course);
		
		CourseAccessSequences2 sequences = new CourseAccessSequences2();
		ArrayList<ConceptInfo> filtered = ConceptUtil.getConceptInfos(course, ZebraStatic.SEQUENCE_DEEP_LEVEL, true);

		if(ownPrivateLog) {
			sequences.setParameters(userid, course, filtered, ZebraStatic.SEQUENCE_MIN_SUP);
		}
		else {
			LearningHistoryDataDaemon hisDataDaemon = getUserHisDataDaemon(userid, course);
			LearningHistoryData hisData = null;
			if(hisDataDaemon != null)
				hisData = hisDataDaemon.getHisData();
			else
				hisData = addCourseHisDataDaemon(course);
			sequences.setParameters(hisData.getLog(), userid, course, filtered, ZebraStatic.SEQUENCE_MIN_SUP);
		}
		
		sequences.perform();
		CourseAccessSequencesDaemon daemon = new CourseAccessSequencesDaemon(sequences);
		userSeqDaemons.put(id, daemon);

		daemon.fireMsgListeners("Perform Training Task in Learning Path daemon (started)");
		return sequences;
	}
	
	public LearningHistoryData addCourseHisDataDaemon(String course) throws Exception {
		if(!isStarted) {ZebraStatic.traceService.trace("Engine is stopped! Can't add daemon"); return null;}
		
		if(courseHisDataDaemons.get(course) != null)
			return courseHisDataDaemons.get(course).getHisData();
		LearningHistoryData hisData = new LearningHistoryData();
		hisData.setParameters(null, course);
		hisData.update();
		LearningHistoryDataDaemon daemon = new LearningHistoryDataDaemon(hisData);
    	courseHisDataDaemons.put(course, daemon);

    	daemon.fireMsgListeners("Perform Collecting Task in Learning History Data daemon (started)");
    	return hisData;
	}

	public LearningHistoryData addUserHisDataDaemon(String userid, String course) throws Exception {
		if(!isStarted) {ZebraStatic.traceService.trace("Engine is stopped! Can't add daemon"); return null;}
		String id = ZebraStatic.makeId(userid, course);
		if(userHisDataDaemons.get(id) != null) return userHisDataDaemons.get(id).getHisData(); 
		if(useGlobalDaemon)                    return addCourseHisDataDaemon(course);
		
		LearningHistoryData hisData = new LearningHistoryData();
		hisData.setParameters(userid, course);
		hisData.update();
		LearningHistoryDataDaemon daemon = new LearningHistoryDataDaemon(hisData);
		userHisDataDaemons.put(id, daemon);

    	daemon.fireMsgListeners("Perform Collecting Task in Learning History Data daemon (started)");
    	return hisData;
	}

	public DocClassifier addCourseDocClassifierDaemon(String course) throws Exception {
		if(!isStarted) {ZebraStatic.traceService.trace("Engine is stopped! Can't add daemon"); return null;}
		if(courseDocClassifierDaemons.get(course) != null)
			return courseDocClassifierDaemons.get(course).getDocClassifier();
		
		DocClassifier classifier = new DocClassifier(course);
		classifier.buildClassifier();
		DocClassifierDaemon daemon = new DocClassifierDaemon(classifier);
    	courseDocClassifierDaemons.put(course, daemon);
    	
    	daemon.fireMsgListeners("Build Classifier Task in DocClassifier daemon (started)");
		return classifier;
	}
	
	public UserSearchService addUserSearchServiceDaemon(String userid, String course) throws Exception {
		String id = ZebraStatic.makeId(userid, course);
		if(userSearchServiceDaemons.get(id) != null)
			return userSearchServiceDaemons.get(id).getSearchUserService();
		UserSearchService service = new UserSearchService(userid, course);
		service.update();
		UserSearchServiceDaemon daemon = new UserSearchServiceDaemon(service);
		userSearchServiceDaemons.put(id, daemon);
		
		daemon.fireMsgListeners("Updating DocClass/User Interest in User Search Service daemon (started)");
		return service;
	}
	
	
	public void removeCourseRecommenderDaemon(String course) {
		CourseAccessAprioriDaemon daemon = courseRecDaemons.get(course);
		if(daemon != null)  {
			daemon.stop();
			courseRecDaemons.remove(course);
		}
	}
	public void removeUserRecommenderDaemon(String userid, String course) {
		CourseAccessAprioriDaemon daemon = userRecDaemons.get(ZebraStatic.makeId(userid, course));
		if(daemon != null)  {
			daemon.stop();
			userRecDaemons.remove(ZebraStatic.makeId(userid, course));
		}
	}
	
	public void removeCourseSequenceDaemon(String course) {
		CourseAccessSequencesDaemon daemon = courseSeqDaemons.get(course);
		if(daemon != null) {
			daemon.stop();
			courseSeqDaemons.remove(course);
		}
	}
	
	public void removeUserSequenceDaemon(String userid, String course) {
		CourseAccessSequencesDaemon daemon = userSeqDaemons.get(ZebraStatic.makeId(userid, course));
		if(daemon != null) {
			daemon.stop();
			userSeqDaemons.remove(ZebraStatic.makeId(userid, course));
		}
	}
	
	public void removeCourseHisDataDaemon(String course) {
		LearningHistoryDataDaemon daemon = courseHisDataDaemons.get(course);
		if(daemon != null) {
			daemon.stop();
			courseHisDataDaemons.remove(course);
		}
	}
	
	public void removeUserHisDataDaemon(String userid, String course) {
		LearningHistoryDataDaemon daemon = userHisDataDaemons.get(ZebraStatic.makeId(userid, course));
		if(daemon != null) {
			daemon.stop();
			userHisDataDaemons.remove(ZebraStatic.makeId(userid, course));
		}
	}

	public void removeCourseDocClassifierDaemon(String course) {
		DocClassifierDaemon daemon = courseDocClassifierDaemons.get(course);
		if(daemon != null) {
			daemon.stop();
			courseDocClassifierDaemons.remove(course);
		}
	}

	public void removeUserSearchServiceDaemon(String userid, String course) {
		UserSearchServiceDaemon daemon = userSearchServiceDaemons.get(ZebraStatic.makeId(userid, course));
		if(daemon != null)  {
			daemon.stop();
			userSearchServiceDaemons.remove(ZebraStatic.makeId(userid, course));
		}
	}
	
	public void removeAllCourseRecommenderDaemons() {
		operateAllCourseRecommenderDaemons(false);
		courseRecDaemons.clear();
	}
	public void removeAllUserRecommenderDaemons() {
		operateAllUserRecommenderDaemons(false);
		userRecDaemons.clear();
	}
	
	public void removeAllCourseSequenceDaemons() {
		operateAllCourseSequenceDaemons(false);
		courseSeqDaemons.clear();
	}
	
	public void removeAllUserSequenceDaemons() {
		operateAllUserSequenceDaemons(false);
		userSeqDaemons.clear();
	}
	
	public void removeAllCourseHisDataDaemons() {
		operateAllCourseHisDataDaemons(false);
		courseHisDataDaemons.clear();
	}
	
	public void removeAllUserHisDataDaemons() {
		operateAllUserHisDataDaemons(false);
		userHisDataDaemons.clear();
	}

	public void removeAllCourseDocClassifiers() {
		operateAllCourseDocClassifierDaemons(false);
		courseDocClassifierDaemons.clear();
	}

	public void removeAllUserSearchServiceDaemons() {
		operateAllUserSearchServiceDaemons(false);
		userSearchServiceDaemons.clear();
	}

	public void removeAllCourseDaemons() {
    	removeAllCourseRecommenderDaemons();
    	removeAllCourseSequenceDaemons();
    	removeAllCourseHisDataDaemons();
    	removeAllCourseDocClassifiers();
    }
    public void removeAllUserDaemons() {
    	removeAllUserRecommenderDaemons();
    	removeAllUserSequenceDaemons();
    	removeAllUserHisDataDaemons();
    	removeAllUserSearchServiceDaemons();
    }
    
    public void removeCourseDaemons(String course) {
    	removeCourseRecommenderDaemon(course);
    	removeCourseSequenceDaemon(course);
    	removeCourseHisDataDaemon(course);
    	removeCourseDocClassifierDaemon(course);
    }
    public void removeUserDaemons(String userid, String course) {
    	removeUserRecommenderDaemon(userid, course);
    	removeUserSequenceDaemon(userid, course);
    	removeUserHisDataDaemon(userid, course);
    	removeUserSearchServiceDaemon(userid, course);
    }

    public CourseAccessAprioriDaemon getCourseRecommenderDaemon(String course) {
		return courseRecDaemons.get(course);
    }
    public CourseAccessAprioriDaemon getUserRecommenderDaemon(String userid, String course) {
    	CourseAccessAprioriDaemon daemon = userRecDaemons.get(ZebraStatic.makeId(userid, course));
    	if(daemon == null) return getCourseRecommenderDaemon(course);
    	return daemon;
    }
    
    public CourseAccessSequencesDaemon getCourseSequenceDaemon(String course) {
		return courseSeqDaemons.get(course);
    }
    public CourseAccessSequencesDaemon getUserSequenceDaemon(String userid, String course) {
    	CourseAccessSequencesDaemon daemon = userSeqDaemons.get(ZebraStatic.makeId(userid, course));
    	if(daemon == null) return getCourseSequenceDaemon(course);
    	return daemon;
    }
    
    public LearningHistoryDataDaemon getCourseHisDataDaemon(String course) {
		return courseHisDataDaemons.get(course);
    }
    public LearningHistoryDataDaemon getUserHisDataDaemon(String userid, String course) {
    	LearningHistoryDataDaemon daemon = userHisDataDaemons.get(ZebraStatic.makeId(userid, course));
    	if(daemon == null) return getCourseHisDataDaemon(course);
    	return daemon;
    }
    
    public DocClassifierDaemon getCourseDocClassifierDaemon(String course) {
    	return courseDocClassifierDaemons.get(course);
    }
    
    public UserSearchServiceDaemon getUserSearchServiceDaemon(String userid, String course) {
    	return userSearchServiceDaemons.get(ZebraStatic.makeId(userid, course));
    }

    public boolean isStarted() {return isStarted;}
	public void start() {
		if(isStarted) {
			ZebraStatic.traceService.trace("Zebra miner is already started!");
			return;
		}
		operateAllCourseRecommenderDaemons(true);
		operateAllCourseSequenceDaemons(true);
		operateAllCourseHisDataDaemons(true);
		operateAllCourseDocClassifierDaemons(true);
		
		operateAllUserRecommenderDaemons(true);
		operateAllUserSequenceDaemons(true);
		operateAllUserHisDataDaemons(true);
		operateAllUserSearchServiceDaemons(true);

		isStarted = true;
	}
	public void restart() {
		if(isStarted) stop();
		start();
	}
	public void stop() {
		if(!isStarted) {
			ZebraStatic.traceService.trace("Zebra miner is already stopped!");
			return;
		}
		operateAllCourseRecommenderDaemons(false);
		operateAllCourseSequenceDaemons(false);
		operateAllCourseHisDataDaemons(false);
		operateAllCourseDocClassifierDaemons(false);
		
		operateAllUserRecommenderDaemons(false);
		operateAllUserSequenceDaemons(false);
		operateAllUserHisDataDaemons(false);
		operateAllUserSearchServiceDaemons(false);

		isStarted = false;
	}

	private void operateAllCourseRecommenderDaemons(boolean isStart) {
		Collection<CourseAccessAprioriDaemon> daemons = courseRecDaemons.values();
		Iterator<CourseAccessAprioriDaemon> iter = daemons.iterator();
		while(iter.hasNext()) {
			CourseAccessAprioriDaemon daemon = iter.next();
			if(daemon == null) continue;
			if(isStart) daemon.start();
			else        daemon.stop();
		}
	}
	private void operateAllUserRecommenderDaemons(boolean isStart) {
		Collection<CourseAccessAprioriDaemon> daemons = userRecDaemons.values();
		Iterator<CourseAccessAprioriDaemon> iter = daemons.iterator();
		while(iter.hasNext()) {
			CourseAccessAprioriDaemon daemon = iter.next();
			if(daemon == null) continue;
			if(isStart) daemon.start();
			else        daemon.stop();
		}
	}
	
	private void operateAllCourseSequenceDaemons(boolean isStart) {
		Collection<CourseAccessSequencesDaemon> daemons = courseSeqDaemons.values();
		Iterator<CourseAccessSequencesDaemon> iter = daemons.iterator();
		while(iter.hasNext()) {
			CourseAccessSequencesDaemon daemon = iter.next();
			if(daemon == null) continue;
			if(isStart) daemon.start();
			else        daemon.stop();
		}
	}
	private void operateAllUserSequenceDaemons(boolean isStart) {
		Collection<CourseAccessSequencesDaemon> daemons = userSeqDaemons.values();
		Iterator<CourseAccessSequencesDaemon> iter = daemons.iterator();
		while(iter.hasNext()) {
			CourseAccessSequencesDaemon daemon = iter.next();
			if(daemon == null) continue;
			if(isStart) daemon.start();
			else        daemon.stop();
		}
	}
	
	private void operateAllCourseHisDataDaemons(boolean isStart) {
		Collection<LearningHistoryDataDaemon> daemons = courseHisDataDaemons.values();
		Iterator<LearningHistoryDataDaemon> iter = daemons.iterator();
		while(iter.hasNext()) {
			LearningHistoryDataDaemon daemon = iter.next();
			if(daemon == null) continue;
			if(isStart) daemon.start();
			else        daemon.stop();
		}
	}
	
	private void operateAllUserHisDataDaemons(boolean isStart) {
		Collection<LearningHistoryDataDaemon> daemons = userHisDataDaemons.values();
		for(LearningHistoryDataDaemon daemon : daemons) {
			if(daemon == null) continue;
			if(isStart) daemon.start();
			else        daemon.stop();
		}
	}

	private void operateAllCourseDocClassifierDaemons(boolean isStart) {
		Collection<DocClassifierDaemon> daemons = courseDocClassifierDaemons.values();
		for(DocClassifierDaemon daemon : daemons) {
			if(daemon == null) continue;
			if(isStart) daemon.start();
			else        daemon.stop();
		}
	}

	private void operateAllUserSearchServiceDaemons(boolean isStart) {
		Collection<UserSearchServiceDaemon> daemons = userSearchServiceDaemons.values();
		for(UserSearchServiceDaemon daemon : daemons) {
			if(daemon == null) continue;
			if(isStart) daemon.start();
			else        daemon.stop();
		}
	}
	public static void main(String[] args) {
	}
}
