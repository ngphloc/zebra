/**
 * 
 */
package vn.spring.zebra;

import java.util.ArrayList;

import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
final class ZebraStaticVarInit {
	
    //mail directory
    static {
    	String var = ZebraStatic.config.Get("TRACE_SERVICE_DIR");
    	if(var != null && var.length() > 0) {
    		ZebraStatic.TRACE_SERVICE_DIR = var;
    	}
    	System.out.println("TRACE_SERVICE_DIR = " + ZebraStatic.TRACE_SERVICE_DIR);
    }
    
    //Knowledge Evaluation
    static {
    	String evaltype = ZebraStatic.config.Get("USER_KNOWLEDGE_EVAL_TYPE");
    	if(evaltype != null && evaltype.length() > 0) {
			evaltype = evaltype.toLowerCase().trim();
			if(evaltype.equals("overlay"))
				ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE = QUERY_TYPE.OVERLAY;
			else if(evaltype.equals("bayes"))
				ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE = QUERY_TYPE.OVERLAY_BAYESIAN;
			else if(evaltype.equals("dynbayes"))
				ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE = QUERY_TYPE.DYN_OVERLAY_BAYESIAN;
			else
				ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE = QUERY_TYPE.OVERLAY_BAYESIAN;
    	}
    	System.out.println("USER_KNOWLEDGE_EVAL_TYPE = " + ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE);
    }//end static
    
    static {
    	long lInterval = (long)(parsePossitiveNumberVar(
    			ZebraStatic.config.Get("UPDATE_LEARNING_HISTORY_INTERVAL"))*60000.0);
    	if(lInterval > 0) {
    		ZebraStatic.DAEMON_HIS_INTERVAL = lInterval; //10 minutes
    		ZebraStatic.DAEMON_MINER_INTERVAL = (2*ZebraStatic.DAEMON_HIS_INTERVAL); 
    		ZebraStatic.DAEMON_NETWORKER_INTERVAL = (2*ZebraStatic.DAEMON_HIS_INTERVAL); 
    		ZebraStatic.DAEMON_GARBAGE_INTERVAL = (12*ZebraStatic.DAEMON_HIS_INTERVAL);//2 hours
    	}
    	System.out.println("DAEMON_HIS_INTERVAL = " + 
    			((double)ZebraStatic.DAEMON_HIS_INTERVAL) / (60000.0) + " minute(s)");
    }//end static
    
    //Guess factor in Dynamic Bayesian network
    static {
    	double guess = parsePossitiveNumberVar(ZebraStatic.config.Get("DYN_DEFAULT_GUESS"));
    	if(guess > 0) {
    		ZebraStatic.DYN_DEFAULT_GUESS = guess;
    	}
    	System.out.println("DYN_DEFAULT_GUESS = " + ZebraStatic.DYN_DEFAULT_GUESS);
    }//end static

    //Guess factor in Dynamic Bayesian network
    static {
    	double forget = parsePossitiveNumberVar(ZebraStatic.config.Get("DYN_DEFAULT_FORGET"));
    	if(forget > 0) {
    		ZebraStatic.DYN_DEFAULT_FORGET = forget;
    	}
    	System.out.println("DYN_DEFAULT_FORGET = " + ZebraStatic.DYN_DEFAULT_FORGET);
    }//end static

    //Recommendation minimum support
    static {
    	double min_sup = parsePossitiveNumberVar(ZebraStatic.config.Get("RECOMMEND_MIN_SUP"));
    	if(min_sup > 0) {
    		ZebraStatic.RECOMMEND_MIN_SUP = min_sup;
    	}
    	System.out.println("RECOMMEND_MIN_SUP = " + ZebraStatic.RECOMMEND_MIN_SUP);
    }//end static

    //Sequential pattern minimum support
    static {
    	double min_sup = parsePossitiveNumberVar(ZebraStatic.config.Get("SEQUENCE_MIN_SUP"));
    	if(min_sup > 0) {
    		ZebraStatic.SEQUENCE_MIN_SUP = min_sup;
    	}
    	System.out.println("SEQUENCE_MIN_SUP = " + ZebraStatic.SEQUENCE_MIN_SUP);
    }//end static

    //Maximum number of clusters
    static {
    	int maxCommunity = (int)parsePossitiveNumberVar(ZebraStatic.config.Get("MAX_COMMUNITY"));
    	if(maxCommunity > 0) {
    		ZebraStatic.CLUSTER_MAX_CLUSTER = maxCommunity;
    	}
    	System.out.println("CLUSTER_MAX_CLUSTER = " + ZebraStatic.CLUSTER_MAX_CLUSTER);
    }//end static
    
    //Document classification threshold
    static {
    	double classifier_threshold = parsePossitiveNumberVar(ZebraStatic.config.Get("CLASSIFIER_THRESHOLD"));
    	if(classifier_threshold > 0) {
    		ZebraStatic.CLASSIFIER_THRESHOLD = classifier_threshold;
    	}
    	System.out.println("CLASSIFIER_THRESHOLD = " + ZebraStatic.CLASSIFIER_THRESHOLD);
    }//end static
    
    //Whether using EM algorithm for Bayesian network
    static {
    	ZebraStatic.NETWORKER_USE_EM_ALGORITHM = parseBooleanVar(ZebraStatic.config.Get("USE_EM_ALGORITHM"));
    	System.out.println("NETWORKER_USE_EM_ALGORITHM = " + ZebraStatic.NETWORKER_USE_EM_ALGORITHM);
    }
    
    //default courses
    static {
    	String var = ZebraStatic.config.Get("DEFAULT_COURSES");
    	if(var != null && var.length() > 0) {
	    	try {
	    		String[] vars = var.split(",");
	    		ArrayList<String> courses = new ArrayList<String>();
	    		for(String course : vars) {
	    			course = course.trim();
	    			if(course.length() > 0) courses.add(course);
	    		}
	    		var = "";
	    		if(courses.size() == 0)
	    			ZebraStatic.DEFAULT_COURSES = new String[0];
	    		else {
	    			ZebraStatic.DEFAULT_COURSES = new String[courses.size()];
		    		for(int i = 0; i < courses.size(); i++) {
		    			ZebraStatic.DEFAULT_COURSES[i] = courses.get(i);
		    			var += courses.get(i);
		    			if(i < courses.size() - 1) var += ", ";
		    		}
	    		}
	    	}
	    	catch(Throwable e) {ZebraStatic.DEFAULT_COURSES = new String[0]; var = "";}
    	}
    	System.out.println("DEFAULT_COURSES = " + var);
    }

    //search engine index dir
    static {
    	String var = ZebraStatic.config.Get("SEARCH_INDEX_DIR");
    	if(var != null && var.length() > 0) {
    		ZebraStatic.SEARCH_INDEX_DIR = var;
    	}
    	System.out.println("SEARCH_INDEX_DIR = " + ZebraStatic.SEARCH_INDEX_DIR);
    }
    
    //search log dir
    static {
    	String var = ZebraStatic.config.Get("SEARCH_LOG_DIR");
    	if(var != null && var.length() > 0) {
    		ZebraStatic.SEARCH_LOG_DIR = var;
    	}
    	System.out.println("SEARCH_LOG_DIR = " + ZebraStatic.SEARCH_LOG_DIR);
    }

    //feedback dir
    static {
    	String var = ZebraStatic.config.Get("FEEDBACK_DIR");
    	if(var != null && var.length() > 0) {
    		ZebraStatic.FEEDBACK_DIR = var;
    	}
    	System.out.println("FEEDBACK_DIR = " + ZebraStatic.FEEDBACK_DIR);
    }

    //sending mail smtp host
    static {
    	String var = ZebraStatic.config.Get("MAIL_SMTP_HOST");
    	if(var != null && var.length() > 0) {
    		ZebraStatic.MAIL_SMTP_HOST = var;
    	}
    	System.out.println("MAIL_SMTP_HOST = " + ZebraStatic.MAIL_SMTP_HOST);
    }

    //mail directory
    static {
    	String var = ZebraStatic.config.Get("MAIL_DIR");
    	if(var != null && var.length() > 0) {
    		ZebraStatic.MAIL_DIR = var;
    	}
    	System.out.println("MAIL_DIR = " + ZebraStatic.MAIL_DIR);
    }

    //mail admin address
    static {
    	String var = ZebraStatic.config.Get("MAIL_ADMIN_ADDRESS");
    	if(var != null && var.length() > 0) {
    		ZebraStatic.MAIL_ADMIN_ADDRESS = var;
    	}
    	System.out.println("MAIL_ADMIN_ADDRESS = " + ZebraStatic.MAIL_ADMIN_ADDRESS);
    }

    //mailing list period (in day), default value is 1 day
    static {
    	long lInterval = (long)(parsePossitiveNumberVar(
    			ZebraStatic.config.Get("MAIL_MAILING_LIST_INTERVAL"))*60000.0*60.0*24.0);
    	if(lInterval > 0) {
    		ZebraStatic.MAIL_MAILING_LIST_INTERVAL = lInterval;//1 day
    	}
    	System.out.println("MAIL_MAILING_LIST_INTERVAL = " + 
    			((double)ZebraStatic.MAIL_MAILING_LIST_INTERVAL) / (60000.0*60.0*24.0) + " day(s)");
    }//end static

    private static double parsePossitiveNumberVar(String var) {
    	double number = 0;
    	if(var != null && var.length() > 0) {
	    	try {
	    		number = Double.parseDouble(var);
	    	}
	    	catch(Throwable e) {number = 0;}
    	}
		return number;
    }
    private static boolean parseBooleanVar(String var) {
    	boolean bool = false;
    	if(var != null && var.length() > 0) {
	    	try {
	    		bool = Boolean.parseBoolean(var);
	    	}
	    	catch(Throwable e) {bool = false;}
    	}
    	return bool;
    }

}
