/**
 * 
 */
package vn.spring.zebra.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public interface TriUMQuery extends Remote {
	
	public static final int    HTTP_SERVICE_PORT = 8080;
	public static final int    SOAP_QUERY_PORT = 8080;
	public static final int    COMMUNICATE_SOCKET_SERVICE_PORT = 8417;
	public static final int    COLLAB_SERVICE_PORT = 8419;
	public static final int    RMI_QUERY_PORT = 1099;
	public static final String RMI_DEFAULT_QUERY_HOST = "localhost";
	public static final String RMI_QUERY_NAME = "TriUMQuery";
	public static final String RMI_QUERY_SERVER_URL = "rmi://" + RMI_DEFAULT_QUERY_HOST + ":" + RMI_QUERY_PORT + "/" + RMI_QUERY_NAME;
	
	public static enum QUERY_TYPE {
		OVERLAY, OVERLAY_BAYESIAN, DYN_OVERLAY_BAYESIAN, 
		LEARNING_STYLE,
		RECOMMENDED_PRE_CONCEPT, RECOMMENDED_POST_CONCEPT,
		LEARNING_PATH,
		COMMUNITY,
		PERSONAL_INFO, CONCEPT_INFO,
		STUDY_TIME,
		DOES_EXIST,
		IS_KNOWLEDGE_EVIDENCE,
		LIST_COURSES, LIST_CONCEPTS,
		DOCCLASS,
		USERREPORT,
		FEEDBACKREPORT,
		TOTALREPORT,
		MAILINGLIST_REG, MAILINGLIST_UNREG
	}
	
	double knowledgeQuery(String userid, String course, String briefConceptName, QUERY_TYPE evaltype, boolean isLoadTriUM) throws RemoteException;
	LearningStyle learningStyleQuery(String userid, String course) throws RemoteException;
	ArrayList<String> recommendedPreConceptQuery(String userid, String course, String briefConceptName) throws RemoteException;
	ArrayList<String> recommendedPostConceptQuery(String userid, String course, String briefConceptName) throws RemoteException;
	ArrayList<ArrayList<String>> learningPathQuery(String userid, String course) throws RemoteException;
	ArrayList<String> communityQuery(String userid, String course) throws RemoteException;
	HashMap<String, String> personalInfoQuery(String userid) throws RemoteException;
	HashMap<String, String> conceptInfoQuery(String userid, String course, String briefconcept) throws RemoteException;
	StudyTime studyTimeQuery(String userid, String course, String briefconcept) throws RemoteException;
	boolean doesExist(String userid, String course, String briefvar, QUERY_TYPE evaltype, boolean isLoadTriUM) throws RemoteException;
	boolean isKnowledgeEvidence(String userid, String course, String briefConceptName, QUERY_TYPE evaltype, boolean isLoadTriUM) throws RemoteException;
	ArrayList<String> listCourses() throws RemoteException;
	ArrayList<String> listBriefConcepts(String course) throws RemoteException;
	String getDocClass(String userid, String course) throws RemoteException;
	String userReport(String userid, String course) throws RemoteException;
	String feedbackReport(String course) throws RemoteException;
	String totalReport(String course) throws RemoteException;
	boolean mlReg(String userid, String course) throws RemoteException;
	boolean mlUnreg(String userid, String course) throws RemoteException;
	
	TriUMQueryResult query(TriUMQueryArg arg) throws RemoteException;
	TriUMQueryResult query(HashMap<String, String> args) throws RemoteException;
	TriUMQueryResult query(String http_args) throws RemoteException;
}
