package vn.spring.zebra.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.ConceptInfo;
import vn.spring.WOW.parser.UMVariableLocator;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.LearningStyle;
import vn.spring.zebra.client.StudyTime;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.client.TriUMQueryArg;
import vn.spring.zebra.client.TriUMQueryResult;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.feedback.UserFeedbackService;
import vn.spring.zebra.log.CourseAccessLog;
import vn.spring.zebra.log.UserAccessLog;
import vn.spring.zebra.mail.CourseMailingListDaemon;
import vn.spring.zebra.mining.CourseAccessApriori;
import vn.spring.zebra.mining.CourseAccessSequences2;
import vn.spring.zebra.mining.DistanceProfile;
import vn.spring.zebra.mining.UserClusterer;
import vn.spring.zebra.report.CourseReportService;
import vn.spring.zebra.report.UserReportService;
import vn.spring.zebra.um.ConceptNodeTypeWrapper;
import vn.spring.zebra.um.DynOverlayBayesUM;
import vn.spring.zebra.um.OverlayBayesUM;
import vn.spring.zebra.um.LearningStyleModel;
import vn.spring.zebra.um.PersonalInfo;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.um.UserConceptInfo;
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
public class TriUMServerQueryDelegator extends UnicastRemoteObject implements TriUMQuery {
	private static final long serialVersionUID = 1L;
	protected TriUMServer server = null;
	protected HashSet<TriUMServerQueryListener> queryListeners = new HashSet<TriUMServerQueryListener>();
	
	public TriUMServerQueryDelegator(TriUMServer server) throws RemoteException, MalformedURLException {
		this.server = server;
		try {
			LocateRegistry.createRegistry(TriUMQuery.RMI_QUERY_PORT);
		}
		catch(RemoteException re) {
			System.out.println("ERROR: LocateRegistry.createRegistry cause error: " + re.getMessage() + 
					", so call Naming.lookup and Naming.unbind");
			try {
				Naming.lookup(TriUMQuery.RMI_QUERY_SERVER_URL);
				System.out.println("ERROR: Naming.lookup successfully!");
				try {
					Naming.unbind(TriUMQuery.RMI_QUERY_SERVER_URL);
					System.out.println("ERROR: Naming.unbind successfully!");
				}
				catch(Exception e) {
					System.out.println("ERROR: Naming.unbind error!");
				}
			}
			catch(Exception e) {
				System.out.println("ERROR: Naming.lookup error!");
			}
		}
		//if (System.getSecurityManager() == null) {System.setSecurityManager(new RMISecurityManager());}
		Naming.rebind(TriUMQuery.RMI_QUERY_SERVER_URL, this);
	}
	
	@Override
	protected void finalize() throws Throwable {
		onDestroyed();
		super.finalize();
	}

	protected void onDestroyed() {
		if(server == null) return;
		
		try {
			Naming.unbind(TriUMQuery.RMI_QUERY_SERVER_URL);
			server = null;
			removeAllQueryListeners();
		}
		catch(Exception e) {
			ZebraStatic.traceService.trace("TriUMServerQueryDelegator.onDestroyed->Naming.unbind causes error: " + 
				e.getMessage());
			server = null;
			removeAllQueryListeners();
		}
		ZebraStatic.traceService.trace("Query delegator was destroyed!");
	}
	
	public double knowledgeQuery(String userid, String course, String briefConceptName, QUERY_TYPE evaltype, boolean isLoadTriUM) throws RemoteException {
		if(evaltype == QUERY_TYPE.OVERLAY) {
			try {
				UMVariableLocator umvl = null;
				Profile           profile = null;
				if(isLoadTriUM) {
					TriUM um = server.getUM(userid, course, true);
					profile = um.getProfile();
				}
				else {
					ProfileDB pdb = ZebraStatic.getProfileDB();
					profile = pdb.getProfile(pdb.findProfile(userid));
				}
				
				umvl = new UMVariableLocator(profile, ZebraStatic.getConceptDB());
				Object value = umvl.getVariableValue(course + "." + briefConceptName + ".knowledge");
				return ((Float)value).doubleValue();
			}
			catch(Exception e) {
				String errMsg = "TriUMServer.knowledgeQuery->UMVariableLocator.getVariableValue causes error: " + e.getMessage();
				ZebraStatic.traceService.trace(errMsg); e.printStackTrace(); throw new RemoteException(errMsg);
			}
		}
		else if(evaltype == QUERY_TYPE.OVERLAY_BAYESIAN) {
			try {
				OverlayBayesUM  knowledge = null;
				if(isLoadTriUM) {
					TriUM um = server.getUM(userid, course, true);
					knowledge = um.getStaticKnowledge();
				}
				else knowledge = TriUM.getBayesUM(userid, course);
				
				return knowledge.
					inferMariginalPosterior(briefConceptName, OverlayBayesUM.OBUM_DEFAULT_ALGORITHM_TYPE).
					getResultDouble()[0];
			}
			catch(ZebraException e) {
				String errMsg = "TriUMServer.knowledgeQuery->OverlayBayesUM.inferMariginalPosterior causes error: " + e.getMessage();
				ZebraStatic.traceService.trace(errMsg); e.printStackTrace(); throw new RemoteException(errMsg);
			}
		}
		else if(evaltype == QUERY_TYPE.DYN_OVERLAY_BAYESIAN) {
			try {
				if(isLoadTriUM) {
					TriUM um = server.getUM(userid, course, true);
					return um.getDynKnowledge().
						inferMariginalPosterior(briefConceptName).getResultDouble()[0];
				}
				else {
					return DynOverlayBayesUM.getDynBayesUM(userid, course).
						inferMariginalPosterior(briefConceptName, OverlayBayesUM.OBUM_DEFAULT_ALGORITHM_TYPE).
						getResultDouble()[0];
				}
			}
			catch(ZebraException e) {
				String errMsg = "TriUMServer.knowledgeQuery->DynOverlayBayesUM.inferMariginalPosterior causes error: " + e.getMessage();
				ZebraStatic.traceService.trace(errMsg); e.printStackTrace(); throw new RemoteException(errMsg);
			}
		}
		fireQueryEvent(new TriUMServerQueryEvent(server, server.getUM(userid, course, false),
				"(user=" + userid + ", course=" + course + "): query knowledge remotely"));
		
		throw new RemoteException("Not implement yet");
	}
	public LearningStyle learningStyleQuery(String userid, String course)  throws RemoteException {
		TriUM um = server.getUM(userid, course, true);
		
		LearningStyleModel lshmm = um.getLearningStyleModel();
		LearningStyle result = new LearningStyle();
		try {
			result.isVerbalizer = lshmm.isVerbalizer();
			result.isActivist = lshmm.isActivist();
			result.isTheorist = lshmm.isTheorist();
		}
		catch(ZebraException e) {
			String errMsg = "TriUMServer.learningStyleQuery.LearningStyleModel causes error: " + e.getMessage();
			ZebraStatic.traceService.trace(errMsg); e.printStackTrace(); throw new RemoteException(errMsg);
		}
		
		fireQueryEvent(new TriUMServerQueryEvent(server, um,
				"(user=" + userid + ", course=" + course +
				"): query learning style remotely"));
		return result;
	}
	public ArrayList<String> recommendedPreConceptQuery(String userid, String course, String briefConceptName) throws RemoteException {
		TriUM um = server.getUM(userid, course, true);
		
		CourseAccessApriori recommender = um.getRecommender();
		fireQueryEvent(new TriUMServerQueryEvent(server, um,
				"(user=" + userid + ", course=" + course + "): query pre-recommended concepts remotely"));
		return recommender.getPreRecommendedConcepts(course + "." + briefConceptName, 0);
	}
	public ArrayList<String> recommendedPostConceptQuery(String userid, String course, String briefConceptName) throws RemoteException {
		TriUM um = server.getUM(userid, course, true);
		
		CourseAccessApriori recommender = um.getRecommender();
		fireQueryEvent(new TriUMServerQueryEvent(server, um,
				"(user=" + userid + ", course=" + course + "): query post-recommended concepts remotely"));
		return recommender.getPostRecommendedConcepts(course + "." + briefConceptName, 0);
	}
	public ArrayList<ArrayList<String>> learningPathQuery(String userid, String course) throws RemoteException {
		TriUM um = server.getUM(userid, course, true);
		
		CourseAccessSequences2 sequences = um.getSequence();
		fireQueryEvent(new TriUMServerQueryEvent(server, um,
				"(user=" + userid + ", course=" + course + "): query learning path remotely"));
		return sequences.getMaxLearningPath();
	}
	public ArrayList<String> communityQuery(String userid, String course) throws RemoteException {
		try {
			ArrayList<String> userids = new ArrayList<String>();
			UserClusterer clusterer = server.getUserClustererService().getClusterer(course, true);
			ArrayList<DistanceProfile> profiles = clusterer.getNeighborsOf(userid);
			for(DistanceProfile profile : profiles) {
				userids.add(profile.userid);
			}

			TriUM um = server.getUM(userid, course, true);
			fireQueryEvent(new TriUMServerQueryEvent(server, um, 
				"(user=" + userid + ", course=" + course + "): query her/his community remotely"));
			return userids;
		}
		catch(Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}
	public HashMap<String, String> personalInfoQuery(String userid)
			throws RemoteException {
		HashMap<String, String> attrs = new HashMap<String, String>();
		try {
			PersonalInfo personalInfo = new PersonalInfo(userid);
			attrs.putAll(personalInfo.getInfos2());
		}
		catch(Exception e) {e.printStackTrace(); throw new RemoteException(e.getMessage());}
		fireQueryEvent(new TriUMServerQueryEvent(server, null, 
				"(user=" + userid + ": query her/his personal information remotely"));
		return attrs;
	}
	public HashMap<String, String> conceptInfoQuery(String userid, String course, String briefconcept)
			throws RemoteException {
		HashMap<String, String> attrs = new HashMap<String, String>();
		try {
			UserConceptInfo userConceptInfo = new UserConceptInfo(userid, course + "." + briefconcept);
			attrs.putAll(userConceptInfo.getUserConceptInfo());
		}
		catch(Exception e) {e.printStackTrace(); throw new RemoteException(e.getMessage());}
		fireQueryEvent(new TriUMServerQueryEvent(server, null, 
				"(user=" + userid + ", course=" + course + "): query concept information remotely"));
		return attrs;
	}

	public StudyTime studyTimeQuery(String userid, String course,
			String briefconcept) throws RemoteException {
		try {
			TriUM um = server.getUM(userid, course, true);
			
			StudyTime studyTime = new StudyTime();
			CourseAccessLog courseLog = um.getHisData().getLog();
			if(courseLog.size() == 0) return studyTime;
			UserAccessLog userLog = courseLog.getUserLog(0);
			studyTime.conceptInterval = userLog.numberOfTimeSpend(course + "." + briefconcept);
			studyTime.courseInterval = userLog.numberOfTimeSpendCourse(course);
			studyTime.totalInterval = userLog.numberOfTimeSpend();
			
			fireQueryEvent(new TriUMServerQueryEvent(server, null, 
					"(user=" + userid + ", course=" + course + "): query her/his study time remotely"));
			
			return studyTime;
		}
		catch(Exception e) {throw new RemoteException(e.getMessage());}
	}
	
	public boolean doesExist(String userid, String course, String briefvar, QUERY_TYPE evaltype, boolean isLoadTriUM) throws RemoteException {
		if(evaltype == QUERY_TYPE.OVERLAY) {
			try {
				UMVariableLocator umvl = null;
				Profile           profile = null;
				if(isLoadTriUM) {
					TriUM um = server.getUM(userid, course, true);
					profile = um.getProfile();
				}
				else {
					ProfileDB pdb = ZebraStatic.getProfileDB();
					profile = pdb.getProfile(pdb.findProfile(userid));
				}
				
				umvl = new UMVariableLocator(profile, ZebraStatic.getConceptDB());
				Object value = umvl.getVariableValue(course + "." + briefvar + ".knowledge");
				return (value != null);
			}
			catch(Throwable e) {
				return false;
			}
		}
		else if(evaltype == QUERY_TYPE.OVERLAY_BAYESIAN) {
			try {
				OverlayBayesUM  knowledge = null;
				if(isLoadTriUM) {
					TriUM um = server.getUM(userid, course, true);
					knowledge = um.getStaticKnowledge();
				}
				else knowledge = TriUM.getBayesUM(userid, course);
				
				return (knowledge.getItem(briefvar) != null);
			}
			catch(Throwable e) {
				return false;
			}
		}
		else if(evaltype == QUERY_TYPE.DYN_OVERLAY_BAYESIAN) {
			try {
				if(isLoadTriUM) {
					TriUM um = server.getUM(userid, course, true);
					DynOverlayBayesUM knowledge = um.getDynKnowledge();
					return (knowledge.getCurState().getUserModel().getItem(briefvar) != null);
				}
				else {
					OverlayBayesUM knowledge = DynOverlayBayesUM.getDynBayesUM(userid, course);
					return (knowledge.getItem(briefvar) != null);
				}
			}
			catch(Throwable e) {
				return false;
			}
		}
		else if(evaltype == QUERY_TYPE.LEARNING_STYLE) {
			if(isLoadTriUM) {
				try {
					TriUM um = server.getUM(userid, course, true);
					um = server.getUM(userid, course, true);
					LearningStyleModel lshmm = um.getLearningStyleModel();
					return (lshmm != null);
				}
				catch(Throwable e) {
					return false;
				}
			}
			else throw new RemoteException("Not implement yet for learning style");
		}
		
		throw new RemoteException("Not implement yet");
	}
	
	public boolean isKnowledgeEvidence(String userid, String course,
			String briefConceptName, QUERY_TYPE evaltype, boolean isLoadTriUM) throws RemoteException {
		try {
			if(evaltype == QUERY_TYPE.OVERLAY) {
                ConceptInfo cInfo = ZebraStatic.getCourseInfoTbl().getCourseInfo(course).
        			conceptsInfoTbl.getConceptInfo(course + "." + briefConceptName);
                ConceptNodeTypeWrapper wrapper = new ConceptNodeTypeWrapper(cInfo);
                return wrapper.isEvidence();
			}
			else if(evaltype == QUERY_TYPE.OVERLAY_BAYESIAN) {
				OverlayBayesUM  knowledge = null;
				if(isLoadTriUM) {
					TriUM um = server.getUM(userid, course, true);
					knowledge = um.getStaticKnowledge();
				}
				else 
					knowledge = TriUM.getBayesUM(userid, course);
				
                ConceptNodeTypeWrapper wrapper = new ConceptNodeTypeWrapper(knowledge.getItem(briefConceptName));
                return wrapper.isEvidence();
			}
			else if(evaltype == QUERY_TYPE.DYN_OVERLAY_BAYESIAN) {
				OverlayBayesUM knowledge = null;
				
				if(isLoadTriUM) {
					TriUM um = server.getUM(userid, course, true);
					DynOverlayBayesUM dyn = um.getDynKnowledge();
					knowledge = dyn.getCurState().getUserModel();
				}
				else {
					knowledge = DynOverlayBayesUM.getDynBayesUM(userid, course);
				}
				
				ConceptNodeTypeWrapper wrapper = new ConceptNodeTypeWrapper(knowledge.getItem(briefConceptName));
                return wrapper.isEvidence();
			}
			else
				return false;
		}
		catch(Throwable e) {
			return false;
		}
	}

	public ArrayList<String> listCourses() throws RemoteException {
		ArrayList<String> courseList = ZebraStatic.getAuthorsConfig().getAllCourses();
		Collections.sort(courseList);
		return courseList;
	}
	
	public ArrayList<String> listBriefConcepts(String course)
			throws RemoteException {
		ArrayList<String> cList = ConceptUtil.getConceptNameList(course);
		ArrayList<String> briefList = new ArrayList<String>();
		for(String concept : cList) {
			briefList.add(concept.substring(concept.indexOf(".") + 1));
		}
		Collections.sort(briefList);
		return briefList;
	}

	public String getDocClass(String userid, String course) throws RemoteException {
		try {
			TriUM um = server.getUM(userid, course, true);

			fireQueryEvent(new TriUMServerQueryEvent(server, um, 
					"(user=" + userid + ", course=" + course + "): query her/his interest/DocClass"));
			return um.getSearchService().getDocClass();
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RemoteException("getDocClass causes error: " + e.getMessage());
		}
	}

	public String userReport(String userid, String course)
			throws RemoteException {
		try {
			TriUM um = server.getUM(userid, course, true);

			UserReportService reportService = new UserReportService(um);
			fireQueryEvent(new TriUMServerQueryEvent(server, null, 
					"(user=" + userid + ", course=" + course + "): query her/his report"));
			return reportService.genHtmlReport();
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RemoteException("getDocClass causes error: " + e.getMessage());
		}
	}

	public String feedbackReport(String course)
			throws RemoteException {
		try {
			UserFeedbackService reportService = new UserFeedbackService(course);
			reportService.load();
			return reportService.reportHTML();
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RemoteException("feedbackReport causes error: " + e.getMessage());
		}
	}

	public String totalReport(String course) throws RemoteException {
		try {
			CourseReportService reportService = new CourseReportService(course);
			return reportService.genReport();
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RemoteException("totalReport causes error: " + e.getMessage());
		}
	}

	public boolean mlReg(String userid, String course) throws RemoteException {
		try {
			CourseMailingListDaemon daemon = TriUMServer.getInstance().getMailingListService().getCourseMailingListDaemon(course);
			if(daemon == null) return false;
			daemon.getCourseMailingList().addUser(userid);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RemoteException("totalReport causes error: " + e.getMessage());
		}
	}

	public boolean mlUnreg(String userid, String course) throws RemoteException {
		try {
			CourseMailingListDaemon daemon = TriUMServer.getInstance().getMailingListService().getCourseMailingListDaemon(course);
			if(daemon == null) return false;
			daemon.getCourseMailingList().removeUser(userid);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RemoteException("totalReport causes error: " + e.getMessage());
		}
	}

	public TriUMQueryResult query(String http_args) throws RemoteException {
		try {
			return query(TriUMQueryArg.createQueryArg(http_args));
		}
		catch(Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}
	public TriUMQueryResult query(HashMap<String, String> args) throws RemoteException {
		try {
			TriUMQueryResult result = query(TriUMQueryArg.createQueryArg(args));
			return result;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}
	
	public TriUMQueryResult query(TriUMQueryArg arg) throws RemoteException {
		TriUMQueryResult result =  new TriUMQueryResult();
		result.queryType = arg.type;
		
		if(arg.type == QUERY_TYPE.OVERLAY || arg.type == QUERY_TYPE.OVERLAY_BAYESIAN || arg.type == QUERY_TYPE.DYN_OVERLAY_BAYESIAN) {
			result.knowledge = knowledgeQuery(arg.userid, arg.course, arg.concept, arg.type, true);
		}
		else if(arg.type == QUERY_TYPE.LEARNING_STYLE) {
    		result.learningStyle = learningStyleQuery(arg.userid, arg.course);
    	}
		else if(arg.type == QUERY_TYPE.RECOMMENDED_PRE_CONCEPT) {
			result.preRecommendedConcepts = recommendedPreConceptQuery(arg.userid, arg.course, arg.concept);
		}
		else if(arg.type == QUERY_TYPE.RECOMMENDED_POST_CONCEPT) {
			result.postRecommendedConcepts = recommendedPostConceptQuery(arg.userid, arg.course, arg.concept);
		}
		else if(arg.type == QUERY_TYPE.LEARNING_PATH) {
			result.learningPath = learningPathQuery(arg.userid, arg.course);
		}
		else if(arg.type == QUERY_TYPE.COMMUNITY) {
			result.community = communityQuery(arg.userid, arg.course);
		}
		else if(arg.type == QUERY_TYPE.PERSONAL_INFO) {
			result.personalInfo = personalInfoQuery(arg.userid);
		}
		else if(arg.type == QUERY_TYPE.CONCEPT_INFO) {
			result.conceptInfo = conceptInfoQuery(arg.userid, arg.course, arg.concept);
		}
		else if(arg.type == QUERY_TYPE.STUDY_TIME) {
			result.studyTime = studyTimeQuery(arg.userid, arg.course, arg.concept);
		}
		else if(arg.type == QUERY_TYPE.DOES_EXIST) {
			result.isKnowledgeEvedence = doesExist(arg.userid, arg.course, arg.concept, ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
		}
		else if(arg.type == QUERY_TYPE.IS_KNOWLEDGE_EVIDENCE) {
			result.doesExist = isKnowledgeEvidence(arg.userid, arg.course, arg.concept, ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
		}
		else if(arg.type == QUERY_TYPE.LIST_COURSES) {
			result.courseList = listCourses();
		}
		else if(arg.type == QUERY_TYPE.LIST_CONCEPTS) {
			result.briefConceptList = listBriefConcepts(arg.course);
		}
		else if(arg.type == QUERY_TYPE.DOCCLASS) {
			result.docClass = getDocClass(arg.userid, arg.course);
		}
		else if(arg.type == QUERY_TYPE.USERREPORT) {
			result.userReport = userReport(arg.userid, arg.course);
		}
		else if(arg.type == QUERY_TYPE.FEEDBACKREPORT) {
			result.feedbackReport = feedbackReport(arg.course);
		}
		else if(arg.type == QUERY_TYPE.TOTALREPORT) {
			result.totalReport = totalReport(arg.course);
		}
		else if(arg.type == QUERY_TYPE.MAILINGLIST_REG) {
			result.mlreg = mlReg(arg.userid, arg.course);
		}
		else if(arg.type == QUERY_TYPE.MAILINGLIST_UNREG) {
			result.mlunreg = mlReg(arg.userid, arg.course);
		}
		else
			throw new RemoteException("Not implement yet for other type");
		return result;
	}

	public synchronized void addQueryListener(TriUMServerQueryListener listener) {
		queryListeners.add(listener);
	}
	public synchronized void removeQueryListener(TriUMServerQueryListener listener) {
		queryListeners.remove(listener);
	}
	private synchronized void removeAllQueryListeners() {
		queryListeners.clear();
	}
	
	//For future
	protected boolean checkPwd(String userid, String course) {
		return true;
	}
	
	private class FireQueryEventTask implements FireEventTask {
		private TriUMServerQueryEvent evt = null;
		public FireQueryEventTask(TriUMServerQueryEvent evt) {
			this.evt = evt;
		}
		public void run() {
			for(TriUMServerQueryListener listener : queryListeners) {
				try {
					listener.queryReceived(evt);
					TriUM um = evt.getTriUM();
					if(um != null) um.updateLastQueryDate();
				}
				catch(Exception e) {
					ZebraStatic.traceService.trace("TriUMServer.FireQueryEvent causes error: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	private void fireQueryEvent(TriUMServerQueryEvent evt) {
		FireEventUtil.fireEvent(new FireQueryEventTask(evt), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
}
