package vn.spring.zebra.bn;

import java.util.ArrayList;

import vn.spring.WOW.datacomponents.DotString;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.evaluation.TestEngineEvent;
import vn.spring.zebra.evaluation.TestEngineListener;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.log.CourseAccessLog;
import vn.spring.zebra.log.UserAccessLog;
import vn.spring.zebra.log.UserAccessRecord;
import vn.spring.zebra.log.UserAccessSession;
import vn.spring.zebra.um.Evidence;
import vn.spring.zebra.um.OverlayBayesItem;
import vn.spring.zebra.um.OverlayBayesItemBetaDensity;
import vn.spring.zebra.um.OverlayBayesUM;
import vn.spring.zebra.um.OverlayBayesUMFactory;
import vn.spring.zebra.util.BasicDaemon;
import vn.spring.zebra.util.ConceptUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class StaticOverlayBayesUMDaemon extends BasicDaemon implements TestEngineListener, ConsiderConceptListener {
	public static final boolean IS_LEARNING_PARAMETER = false;
	
	private String userid = null;
	private String course = null;
	
	public StaticOverlayBayesUMDaemon(OverlayBayesUM staticUM, String userid, String course) {
		super(staticUM, "(userid=" + userid + ", course=" + course + "): " + 
				"Static Knowledge Bayesian Network daemon started!", 
				ZebraStatic.DAEMON_DELAY, ZebraStatic.DAEMON_NETWORKER_INTERVAL, 
				ZebraStatic.NETWORKER_RUN_STATIC_KNOWLEDGE_DAEMON);
		this.userid = userid;
		this.course = course;
	}
	
	@Override
    @Deprecated
	public void task() throws Exception {
		_update(userid, course);
		fireMsgListeners("Perform Training task in Static Knowledge Bayesian Network daemon");
	}
	
	public OverlayBayesUM getStaticOverlayBayesUM() {return (OverlayBayesUM)getInnerObject();}
	public String getUserId() {return userid;}
	public String getCourse() {return course;}
	
	public synchronized void testEvaluated(TestEngineEvent evt) throws ZebraException {
		// TODO Auto-generated method stub
		OverlayBayesUM um = getStaticOverlayBayesUM();
		ArrayList<Evidence> evidences = new ArrayList<Evidence>();
		evidences.add(evt.getEvidence());
		
		for(Evidence evidence : evidences) {
			OverlayBayesItem item = um.getItem(evidence.briefName);
			if(item == null) continue;
			item.setObservedValue(evidence.value);
		}
		try {
	    	um.saveToDatabase(userid, course, OverlayBayesUMFactory.getFormatType(OverlayBayesUM.OBUM_EXT), false);
		}
		catch(Exception e) {throw new ZebraException(e.getMessage());}
		fireMsgListeners("Updating Test Evidences in Static Knowledge Bayesian Network daemon");
	}

	public void conceptConsidered(ConsiderConceptEvent evt) throws ZebraException {
		// Not use EM algorithm
		if(!ZebraStatic.NETWORKER_USE_EM_ALGORITHM) return;
		
		OverlayBayesUM um = getStaticOverlayBayesUM();
		String briefConceptName = ConceptUtil.getBriefConceptName(course, evt.getConcept());
		OverlayBayesItem item = um.getItem(briefConceptName);
		if(item == null) return;
		if(item.hasParent()) return;
		if(!OverlayBayesItemBetaDensity.checkValid(item)) return;
		
		//EM learning
		OverlayBayesItemBetaDensity beta = new OverlayBayesItemBetaDensity(item);
		if(beta.getOneBeta().expectation() > ZebraStatic.OBUM_EM_MAX_PROBABILITY) return;
		System.out.println("### Before EM learning: Beta function for concept \"" + evt.getConcept() + "\": " + 
			beta.getOneBeta().toString());
		beta.getOneBeta().incA();
		beta.updateBackItem();
		System.out.println("### After EM learning: Beta function for concept \"" + evt.getConcept() + "\": " + 
			beta.getOneBeta().toString());
		
		try {
	    	um.saveToDatabase(userid, course, OverlayBayesUMFactory.getFormatType(OverlayBayesUM.OBUM_EXT), false);
		}
		catch(Exception e) {throw new ZebraException(e.getMessage());}
		
	}

    @Deprecated
	public synchronized void _update(String userid, String course) throws Exception {
		if(true) throw new Exception("Now the function StaticOverlayBayesUMDaemon._update isn't still supported");
		
		_updateEvidences(getStaticOverlayBayesUM(), userid, course);
		if(IS_LEARNING_PARAMETER) {
			ZebraStatic.traceService.trace("Learning Parameter in method TriUM.updateKnowledgeUM");
			_learningParameterKnowledgeUM(getStaticOverlayBayesUM(), userid, course);
		}
	}
	//The return value indicates whether the static UM is changed
    @Deprecated
    private static void _updateEvidences(OverlayBayesUM staticUM, String userid, String course) throws ZebraException, Exception {
		if(true) throw new Exception("Now the function StaticOverlayBayesUMDaemon._updateEvidences isn't still supported");

		ArrayList<Evidence> evidences = Evidence.getTempEvidences(userid, course, staticUM, ZebraStatic.NETWORKER_LOG_BEGIN_DATE);
		for(Evidence evidence : evidences) {
			OverlayBayesItem item = staticUM.getItem(evidence.briefName);
			if(item == null) continue;
			
			if(item.isObserved()) {
				item.setObservedValue(evidence.value);
			}
		}
    }
	//The return value indicates whether the static UM is changed
    @Deprecated
    private static void _learningParameterKnowledgeUM(OverlayBayesUM staticUM, String userid, String course) throws Exception {
		if(true) throw new Exception("Now the function StaticOverlayBayesUMDaemon._learningParameterKnowledgeUM isn't still supported");

		//EM learning
		CourseAccessLog log = ZebraStatic.getLogDB().getCourseAccessLog(userid, course, ZebraStatic.NETWORKER_LOG_BEGIN_DATE);
		if(log.size() == 0) return;
		UserAccessLog userlog = log.getUserLog(0);
		if(userlog.size() == 0) return;
		ArrayList<String> attrs = new ArrayList<String>();
		OverlayBayesItem[] items = staticUM.getItems();
		if(items.length == 0) return;
		for(OverlayBayesItem item : items) attrs.add(item.getName());
		
		ArrayList<double[]> dataset = new ArrayList<double[]>();
		for(int i = 0; i < userlog.size(); i++) {
			UserAccessSession session = userlog.getSession(i);
			if(session.size() == 0) continue;
			
			double[] data = new double[attrs.size()];
			for(int k = 0; k < attrs.size(); k++) data[k] = -1;
			for(int j = 0; j < session.size(); j++) {
				UserAccessRecord record = session.getRecord(j);
				DotString ds = new DotString(record.getConceptName());
				int k = attrs.indexOf(ds.get(ds.size() - 1));
				if(k != -1) data[k] = 1;
			}
			dataset.add(data);
		}
		if(dataset.size() > 0) staticUM.betaLearningByEM(attrs, dataset, ZebraStatic.OBUM_EM_MAX_PROBABILITY);
    }
	
	@Override
	public String getDaemonName() {
		// TODO Auto-generated method stub
		return "Static Knowledge Bayesian Network daemon";
	}
}
