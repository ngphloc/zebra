package vn.spring.zebra;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.Layout.ViewManager;
import vn.spring.WOW.WOWDB.ConceptDB;
import vn.spring.WOW.WOWDB.LogDB;
import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.config.AuthorsConfig;
import vn.spring.WOW.config.WowConfig;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.CourseInfoTbl;
import vn.spring.WOW.engine.HandlerManager;
import vn.spring.WOW.engine.ProfileManager;
import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;
import vn.spring.zebra.config.ZebraConfig;
import vn.spring.zebra.helperservice.TraceService;
import vn.spring.zebra.search.SearchLogService;


/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public final class ZebraStatic {
	//Configuration
    public final static ZebraConfig config = new ZebraConfig();

	//Trace, Log Service
	public final static boolean      TRACE_SERVICE_USE_FILE = true;
	public       static String       TRACE_SERVICE_DIR = "WEB-INF/logs/";
    public final static Log          log = LogFactory.getLog("vn.spring");
    public       static TraceService traceService = null;

    //Default courses
    public       static String[] DEFAULT_COURSES = new String[0];
    
	//Daemon
    public final static long DAEMON_DELAY = (1*60000);//allow delay 1 minute
    public       static long DAEMON_HIS_INTERVAL = (10*60000); //10 minutes
    //
    public       static long DAEMON_MINER_INTERVAL = (2*DAEMON_HIS_INTERVAL); 
    public       static long DAEMON_NETWORKER_INTERVAL = (2*DAEMON_HIS_INTERVAL); 
	public       static long DAEMON_GARBAGE_INTERVAL = (12*DAEMON_HIS_INTERVAL);//2 hours
	//
	public final static boolean FIRE_EVENT_WAIT_DONE = true; //whether waiting done to fire change event
	                                                         //if it is set to be false, server will run faster but the control panel GUI can have some risk
	
	//Log
    public final static boolean  IGNORE_ANONYMOUS = true;
    public final static Date     NETWORKER_LOG_BEGIN_DATE = null;// if null, read all
    public final static Date     MINER_LOG_BEGIN_DATE = null;//if null, read all
    
    //OverlayBayesUM
    public final static double OBUM_EM_MAX_PROBABILITY = 0.6; //The maximum value for expectation of beta distribution when applying EM algorithm 
    
    //Knowledge Evaluation
    public static QUERY_TYPE USER_KNOWLEDGE_EVAL_TYPE = QUERY_TYPE.OVERLAY_BAYESIAN;
    
	//DynOverlayBayesUM
	public final static int     DYN_MAX_STATES = 10;
	public       static double  DYN_DEFAULT_GUESS = 0.3;  //Guess factor in Dynamic Bayesian network
	public       static double  DYN_DEFAULT_FORGET = 0.2; //Forget factor in Dynamic Bayesian network
	public final static String  DYN_AUX_SIGN = "#";
	
	//ZebraNetworker: this is Belief Network Engine (BNE)
	public final static boolean  NETWORKER_LSHMM_RANDOM_IF_OBS_MISS = true;
	public final static boolean  NETWORKER_RUN_STATIC_KNOWLEDGE_DAEMON = false;
	public final static boolean  NETWORKER_RUN_DYN_KNOWLEDGE_DAEMON = false;
	public       static boolean  NETWORKER_USE_EM_ALGORITHM = false;
	
	//ZebraMiner: this is Mining Engine (ME)
	public final static boolean  MINER_USE_GLOBAL_DAEMON = true; //if false, every user own her/his miner; if true they share the same daemon (for accelerating) 
	//
	public final static int      MINER_MAX_ITEM = -1;//consider all
	public final static boolean  MINER_OWN_PRIVATE_LOG = false;//if false use global CourseAccessLog for mining task
	public final static boolean  MINER_CLUSTER_FOR_MINING_TASK = false;
	//
	public       static double   RECOMMEND_MIN_SUP = 0.3;
	public final static int      RECOMMEND_DEEP_LEVEL = 2;//Only recommending until th-level concepts
    public final static int      RECOMMEND_MAX_CONCEPT = 3;
	//
	public       static double   SEQUENCE_MIN_SUP = 0.5;
	public final static int      SEQUENCE_DEEP_LEVEL = 2;//Only recommending until th-level concepts
	public final static boolean  SEQUENCE_PERFORM_RANDOM = false; //randomization data if empty log when do sequential pattern mining
	public final static String   SEQUENCE_EMPTY_ITEM_NAME = "none";
    public final static int      SEQUENCE_MAX_ITEM_IN_ITEMSET_MUST_BE_FIXED = 10; //number item in itemset; consider all
	//
	public final static int      CLUSTER_CONCEPT_DEEP_LEVEL = 2;//Only clustering with th-level concepts
	public       static int      CLUSTER_MAX_CLUSTER = 4;
	public final static String[] CLUSTER_PERSONALATT = {"personal.VERBvsIM", "personal.GLvsAN"};
	//
	public final static int      CLASSIFIER_MAX_KEYWORD = 100;
	public final static int      CLASSIFIER_DEEP_LEVEL_AS_DOCCLASS = 2;
	public       static double   CLASSIFIER_THRESHOLD = 0.03;
	
	//Images, icons
	public final static String IMAGE_DIR = "/vn/spring/zebra/resource/images/";
	public final static String IMAGE_TINY_ICON_FILENAME = "zebra-24x24.png";
	public final static String IMAGE_SMALL_ICON_FILENAME = "zebra-48x48.png";
	public final static String IMAGE_LARGE_ICON_FILENAME = "zebra-128x128.png";

	//JavaScript
	public final static String JAVASCRIPT_DIR = "/vn/spring/zebra/resource/js/";
	
	//Search
	public       static String           SEARCH_INDEX_DIR = "/search/lucene/index/";
	public       static String           SEARCH_LOG_DIR = "/search/log/";
	public       static SearchLogService searchLogService = null;
	
	//User Feedback
	public       static String FEEDBACK_DIR = "/feedback/";
	public final static String FEEDBACK_QA_CSVFILENAME = "pattern.csv";
	
	//Mail
	public       static String MAIL_SMTP_HOST = "localhost";
	public       static String MAIL_DIR = "/mail/";
	public       static String MAIL_ADMIN_ADDRESS = "admin@localhost";
    public final static long   MAIL_MAILING_LIST_DELAY = (1*60000*60);//allow delay 1 hour
    public       static long   MAIL_MAILING_LIST_INTERVAL = (1*60000*60*24); //1 day
	
	//Global methods
    public final static String makeId(String userid, String course) {return userid + "$" + course;}

    // Legacy Variables need to be re-written
    public final static String getWowContextPath() {return WOWStatic.config.Get("CONTEXTPATH");}
    public final static String getWowWebRoot()     {return WOWStatic.config.Get("WEBROOT");}
    public final static String getWowRoot()        {return WOWStatic.config.Get("WOWROOT");}
    public final static String getWowXmlRoot()     {return WOWStatic.config.Get("XMLROOT");}
    public final static String getWowAuthorFilesPath() {return WOWStatic.AUTHORFILESPATH;}
    public final static File   root = WOWStatic.root;

    //Legacy methods need to be re-written
    public final static boolean    useLogging(Profile profile) {return WOWStatic.useLogging(profile);}
    public final static LogDB      getLogDB()                  {return WOWStatic.DB().getLogDB();}
    public final static ConceptDB  getConceptDB() {return WOWStatic.DB().getConceptDB();}
    public final static ProfileDB  getProfileDB() {return WOWStatic.DB().getProfileDB();}
    public final static WowConfig  getWowConfig() {return WOWStatic.config;}
    public final static ProfileManager getProfileManager() {return WOWStatic.PM();}
    public final static HandlerManager getHandlerManager() {return WOWStatic.HM();}
    public final static ViewManager    getViewManager()    {return WOWStatic.VM();}
    public final static CourseInfoTbl  getCourseInfoTbl()  {return WOWStatic.CourseInfoTbl;}
    public final static AuthorsConfig  getAuthorsConfig()  {return new AuthorsConfig();}
	public final static void logoff(HttpSession session)   {WOWStatic.logoff(session);}
    
    static {
    	new ZebraStaticVarInit();
    	traceService = new TraceService();
    	searchLogService = new SearchLogService();
    }
}
