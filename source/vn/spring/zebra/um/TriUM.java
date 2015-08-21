package vn.spring.zebra.um;

import java.io.File;
import java.util.Date;

import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.DotString;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.bn.ZebraNetworker;
import vn.spring.zebra.search.UserSearchService;
import vn.spring.zebra.um.OverlayBayesUM.OBUM_BAYESNET_TYPE;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.mining.CourseAccessApriori;
import vn.spring.zebra.mining.CourseAccessSequences2;
import vn.spring.zebra.mining.LearningHistoryData;
import vn.spring.zebra.mining.ZebraMiner;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUM {
	public static final String[] INFER_PATTERN = {
		"do$bayes$infer", "do$bayes$infer$0", "do$bayes$infer$1", "do$bayes$infer$valueidx",
		"do$dynbayes$infer", "do$dynbayes$infer$0", "do$dynbayes$infer$1", "do$dynbayes$infer$valueidx"
	};

	private String               userid = null;
	private String               course = null;
	private Profile              profile = null;
	
	private Date                lastQueryDate = new Date();
	
	private ZebraNetworker      zebraNetworker = null;
	private ZebraMiner          zebraMiner = null;
	
	private OverlayBayesUM         staticKnowledge = null;
	private DynOverlayBayesUM      dynKnowledge = null;
	private LearningStyleModel     learningStyleModel = null;
	private CourseAccessApriori    courseRecommender = null;
	private CourseAccessSequences2 courseSequence = null;
	private LearningHistoryData    hisData = null;
	private UserSearchService      userSearchService = null;
	
	
	public TriUM(String userid, String course, ZebraNetworker zebraNetworker, ZebraMiner zebraMiner) 
		throws ZebraException, Exception {
		try {
			if(!zebraNetworker.isStarted() || !zebraMiner.isStarted())
				throw new Exception("Zebra networker or Zebra miner is already stopped!");

			this.userid = userid; this.course = course;
			ProfileDB pdb = ZebraStatic.getProfileDB();
			this.profile = pdb.getProfile( pdb.findProfile(userid) );
			
			this.zebraNetworker = zebraNetworker;
			this.learningStyleModel = this.zebraNetworker.addLearningStyleModelDaemon(this.userid, this.course);
			this.dynKnowledge = this.zebraNetworker.addDynKnowledgeDaemon(this.userid, this.course);
			this.staticKnowledge = this.zebraNetworker.addStaticKnowledgeDaemon(this.userid, this.course);
			
			this.zebraMiner = zebraMiner;
			this.hisData = this.zebraMiner.addUserHisDataDaemon(userid, course);
			this.courseRecommender = this.zebraMiner.addUserRecommenderDaemon(this.userid, this.course);
			this.courseSequence = this.zebraMiner.addUserSequenceDaemon(this.userid, this.course);
			this.userSearchService = this.zebraMiner.addUserSearchServiceDaemon(this.userid, this.course);
			
			updateLastQueryDate();
		}
		catch(Exception e) {
			ZebraStatic.traceService.trace("TriUM constructor() causes error: " + e.getMessage());
			if(zebraNetworker != null) zebraNetworker.removeDaemons(userid, course);
			if(zebraMiner != null) zebraMiner.removeUserDaemons(userid, course);
			throw e;
		}
	}
	public TriUM(String userid, String course) throws ZebraException, Exception {
		this(userid, course, new ZebraNetworker(), 
				new ZebraMiner(ZebraStatic.MINER_USE_GLOBAL_DAEMON, ZebraStatic.MINER_OWN_PRIVATE_LOG));
	}
	
	@Override
	protected void finalize() throws Throwable {
		try {zebraNetworker.removeDaemons(userid, course);}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUM.discard->zebraNetworker.removeTimers() causes error: " + e.getMessage());}
		
		try {zebraMiner.removeUserDaemons(userid, course);}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUM.discard->zebraMiner.removeTimers() causes error: " + e.getMessage());}
		super.finalize();
	}
	public void discard() {
		try {zebraNetworker.removeDaemons(userid, course);}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUM.discard->zebraNetworker.removeTimers() causes error: " + e.getMessage());}
		try {zebraMiner.removeUserDaemons(userid, course);}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUM.discard->zebraMiner.removeTimers() causes error: " + e.getMessage());}
		
		try {if(isChanged()) save();}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUM.discard->save causes error: " + e.getMessage());}
	}
	public Float doSomeThing(String briefConceptName, String pattern) throws ZebraException {
		//do$bayes$infer, do$bayes$infer$0, do$bayes$infer$1, do$bayes$infer$valueidx
		//do$dynbayes$infer, do$dynbayes$infer$0, do$dynbayes$infer$1, do$dynbayes$infer$valueidx
		//valueidx la lay index cua cai lon nhat
		DotString orders = new DotString(pattern, "$");
    	
    	if(!orders.get(0).equals("do")) throw new ZebraException("Not implement yet");
    	String something = orders.get(1) + "$" + orders.get(2);
    	if(!something.equals("bayes$infer") &&  !something.equals("dynbayes$infer"))
    		throw new ZebraException("Not implement yet");
    	
    	InferenceResult result = null;
    	if(orders.get(1).equals("bayes"))
    		result = staticKnowledge.inferMariginalPosterior(briefConceptName, OverlayBayesUM.OBUM_DEFAULT_ALGORITHM_TYPE);
    	else
    		result = dynKnowledge.inferMariginalPosterior(briefConceptName);
		double[] values = result.getResultDouble();

		Float value = new Float(0);
    	int order = 0;
    	if(orders.size() == 4) {
    		if(orders.get(3).equals("valueidx"))
    			order = -1;// la lay index cua cai nao lon nhat
    		else
    			order = Integer.parseInt(orders.get(3));
    	}
    	if(order == -1) {//la orders.get(3).equals("valueidx")
    		int    maxidx = -1;
    		double max = -1;
    		for(int i = 0; i < values.length; i++) {
    			if(max < values[i]) {
    				max = values[i];
    				maxidx = i; 
    			}
    		}
    		value = new Float(maxidx);
    	}
    	else {
        	order = (order < 0 ? 0 : order);
        	order = (order > values.length - 1) ? (values.length - 1) : order;
    		value = new Float(values[order] * 100.0);
    	}
    	
    	
    	String v = "";for(int i = 0; i < values.length; i++) v += values[i] + ", "; v = v.substring(0, v.lastIndexOf(",")); 
    	String msg = "Bayesian inference give result: " + briefConceptName + "=" + value + ", posterior probabilities=(" + v + ")";
		ZebraStatic.traceService.trace(msg);
		
		return value;
	}
	
	public String  getUserId() {return userid;}
	public String  getCourse() {return course;}
	public Profile getProfile() {return profile;}
	public Profile refreshProfile() throws Exception {
		ProfileDB pdb = ZebraStatic.getProfileDB();
		this.profile = pdb.getProfile( pdb.findProfile(this.userid) );
		return this.profile;
	}
	
    public Date getLastQueryDate() { return lastQueryDate;}
    public void updateLastQueryDate() {lastQueryDate = new Date();}

    public ZebraNetworker getZebraNetworker() {return zebraNetworker;}
    public ZebraMiner getZebraMiner() {return zebraMiner;}

	public OverlayBayesUM         getStaticKnowledge() {return staticKnowledge;}
    public DynOverlayBayesUM      getDynKnowledge() {return dynKnowledge;}
	public LearningStyleModel     getLearningStyleModel() {return learningStyleModel;}
	public CourseAccessApriori    getRecommender() {return courseRecommender;}
	public CourseAccessSequences2 getSequence() {return courseSequence;}
	public LearningHistoryData    getHisData() {return hisData;}
	public UserSearchService      getSearchService() {return userSearchService;}
	
    public boolean isChanged() {
    	return staticKnowledge.isChanged() || dynKnowledge.isChanged() ||
    		learningStyleModel.isChanged();
    }
    public void save() throws Exception {
    	if(staticKnowledge.isChanged())
    		staticKnowledge.saveToDatabase(userid, course, OverlayBayesUMFactory.getFormatType(OverlayBayesUM.OBUM_EXT), false);
    	if(dynKnowledge.isChanged())       dynKnowledge.save();
    	if(learningStyleModel.isChanged()) learningStyleModel.save();
    }
    
    @Override
	public String toString() {
		return ZebraStatic.makeId(userid, course);
	}
    
	public final static OverlayBayesUM getBayesUM(String userid, String course) {
    	if(OverlayBayesUM.OBUM_DEFAULT_BAYESNET_TYPE != OBUM_BAYESNET_TYPE.JAVABAYES) return null;
    	
    	OverlayBayesUM um = null;
    	try {
    		String bayesDir = ZebraStatic.getWowXmlRoot() + "/" +  OverlayBayesUM.OBUM_DIR_NAME;
            String courseFilePath = bayesDir + "/" + course +  OverlayBayesUM.OBUM_EXT;
            String userFilePath = bayesDir + "/" + userid + "$" + course +  OverlayBayesUM.OBUM_EXT;
            File courseFile = new File(courseFilePath);
            File userFile = new File(userFilePath);
            if(courseFile == null || userFile == null ||
            		userFile.lastModified() < courseFile.lastModified()) {
            	um = null;
            	System.out.println("Reload Bayesian network because of not up-to-date");
            }
            else
            	um = OverlayBayesUMFactory.loadFromDatabase(userid, course, OverlayBayesUM.OBUM_DEFAULT_BAYESNET_TYPE, false);
    	}
    	catch(Exception e) {
    		um = null; ZebraStatic.traceService.trace(e.getMessage() + ".\n OverlayBayesUM should be created");
    	}
    	if(um != null) return um;
    	
    	try {
    		um = OverlayBayesUMFactory.loadFromDatabase(null, course, OverlayBayesUM.OBUM_DEFAULT_BAYESNET_TYPE, false);
    		if(um != null) um.saveToDatabase(userid, course, OverlayBayesUMFactory.getFormatType(OverlayBayesUM.OBUM_EXT), false);
    	}
    	catch(Exception e) {
    		um = null; ZebraStatic.traceService.trace(e.getMessage() + ".\n OverlayBayesUM can not be created");
    	}
    	return um;
    }
    
}

