/**
 * 
 */
package vn.spring.zebra.client;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMQueryArg implements Serializable {
	private final static long serialVersionUID = 1L;
	public final static String QUERY_STRING_SEP = "&";
	public final static String EMPTY_VALUE = "emptyvalue";

	public QUERY_TYPE type = QUERY_TYPE.OVERLAY;
	public String userid = null;
	public String password = null;
	public String course = null;
	public String concept = null;
	
    public static HashMap<String, String> parseQueryArg(String http_args) {
    	HashMap<String, String> argMap = new HashMap<String, String>();
    	if(http_args == null || http_args.length() == 0) return argMap;
    	String[] params = http_args.split(QUERY_STRING_SEP);
    	if(params.length == 0) return argMap;
    	
    	for(int i = 0; i < params.length; i++) {
    		params[i] = params[i].trim();
    		if(params[i].length() == 0) continue;
    		
    		String[] param = params[i].split("=");
    		if(param.length != 2) continue;
    		param[0] = param[0].trim(); param[1] = param[1].trim();
    		if(param[0].length() == 0 || param[1].length() == 0) continue;
    		
    		argMap.put(param[0].toLowerCase(), param[1]);
    	}
    	return argMap;
    }
    protected static HashMap<String, String> parseConsoleQueryArg(String[] args) {
    	//In form of -protocol=rmi
    	HashMap<String, String> argMap = new HashMap<String, String>();
    	
		for(int i = 0; i < args.length; i++) {
			String[] param = args[i].split("=");
			if(param.length != 2) continue;
			param[0] = param[0].trim(); param[1] = param[1].trim();
			if(param[0].length() == 0) continue;
			if(param[1].length() == 0) continue;
			if(param[0].length() == 1 && param[0].charAt(0) == '-') continue;
			String key = param[0];
			if(key.charAt(0) == '-') key = key.substring(1);
			
			argMap.put(key.toLowerCase(), param[1]);
		}
    	return argMap;
    }
    public static HashMap<String, String> parseQueryArg(String[] args) {
    	HashMap<String, String> argMap = new HashMap<String, String>();
    	
		for(int i = 0; i < args.length; i++) {
			String key = null, value = null;
			String[] param = args[i].split("=");
			if(param.length == 2) {// if in form of "protocol=rmi", "-protocol=rmi" 
				key = param[0].trim().toLowerCase();
				if(key.length() == 0) continue;
				if(key.charAt(0) != '-') key = "-" + key;
				if(key.length() < 2) continue;
				value = param[1].trim();
			}
			else { // if in form of "-protocol rmi"
				if (i == args.length - 1) continue;
				key = args[i].trim().toLowerCase();
				if(key.length() < 2 || key.charAt(0) != '-') continue;
				value = args[i+1].trim();
				i++;
			}
			if(value.length() == 0 || value.charAt(0) == '-') {
				argMap.put(key.substring(1), EMPTY_VALUE);
				continue;
			}
			
			if(key.equals("-protocol") || key.equals("-p")) {
				argMap.put("protocol", value);
			}
			else if(key.equals("-host") || key.equals("-h")) {
				argMap.put("host", value);
			}
			else if(key.equals("-port")) {
				argMap.put("port", value);
			}
			else if(key.equals("-type") || key.equals("-t")) {
				argMap.put("type", value);
			}
			else if(key.equals("-userid") || key.equals("-user") || key.equals("-u")) {
				argMap.put("userid", value);
			}
			else if(key.equals("-password") || key.equals("-pwd")) {
				argMap.put("password", value);
			}
			else if(key.equals("-course") || key.equals("-c")) {
				argMap.put("course", value);
			}
			else if(key.equals("-concept")) {
				argMap.put("concept", value);
			}
			else if(key.equals("-subject") || key.equals("-s")) {
				argMap.put("subject", value);
			}
			else if(key.equals("-topic")) {
				argMap.put("topic", value);
			}
			else {
				argMap.put(key, value);
			}
		}
    	return argMap;
    }
    public static TriUMQueryArg createQueryArg(String http_args) throws Exception {
    	return createQueryArg(parseQueryArg(http_args));
    }
    public static TriUMQueryArg createQueryArg(Map<String, String> map) throws Exception {
    	TriUMQueryArg arg = new TriUMQueryArg();
    	HashMap<String, String> argMap = normalizeMap(map);
    	if(argMap.size() == 0) throw new Exception("Empty arguments");
    	String type = argMap.get("type");
    	if(type == null) throw new Exception("Invalid arguments");
    	
    	if(type.equals("overlay")) {
    		arg.type = QUERY_TYPE.OVERLAY;
    	}
    	else if(type.equals("bayes") || type.equals("bayesian") ||
    		type.equals("overlaybayes") || type.equals("overlaybayesian")) {
    		arg.type = QUERY_TYPE.OVERLAY_BAYESIAN;
    	}
    	else if(type.equals("dyn") || type.equals("dynbayes") || type.equals("dynbayesian") ||
    		type.equals("dynoverlaybayes") || type.equals("dynoverlaybayesian") ||
    		type.equals("dynamic") || type.equals("dynamicbayes") || type.equals("dynamicbayesian") ||
    		type.equals("dynamicoverlaybayes") || type.equals("dynamicoverlaybayesian")) {
    		arg.type = QUERY_TYPE.DYN_OVERLAY_BAYESIAN;
    	}
    	else if(type.equals("ls") || type.equals("style") || type.equals("learningstyle")) {
    		arg.type = QUERY_TYPE.LEARNING_STYLE;
    	}
    	else if(type.equals("rec") || type.equals("prerec") || type.equals("recommend") || type.equals("prerecommend")) {
    		arg.type = QUERY_TYPE.RECOMMENDED_PRE_CONCEPT;
    	}
    	else if(type.equals("postrec") || type.equals("postrecommend")) {
    		arg.type = QUERY_TYPE.RECOMMENDED_POST_CONCEPT;
    	}
    	else if(type.equals("lp") || type.equals("path") || type.equals("learningpath")) {
    		arg.type = QUERY_TYPE.LEARNING_PATH;
    	}
    	else if(type.equals("community") || type.equals("cluster") || type.equals("usercluster")) {
    		arg.type = QUERY_TYPE.COMMUNITY;
    	}
    	else if(type.equals("personalinfo") || type.equals("personal")) {
    		arg.type = QUERY_TYPE.PERSONAL_INFO;
    	}
    	else if(type.equals("conceptinfo") || type.equals("concept")) {
    		arg.type = QUERY_TYPE.CONCEPT_INFO;
    	}
    	else if(type.equals("studytime") || type.equals("studytimespend")) {
    		arg.type = QUERY_TYPE.STUDY_TIME;
    	}
    	else if(type.equals("doesexist")) {
    		arg.type = QUERY_TYPE.DOES_EXIST;
    	}
    	else if(type.equals("isknowledgeevidence") || type.equals("evidence")) {
    		arg.type = QUERY_TYPE.IS_KNOWLEDGE_EVIDENCE;
    	}
    	else if(type.equals("listcourses") || type.equals("listcourse")) {
    		arg.type = QUERY_TYPE.LIST_COURSES;
    	}
    	else if(type.equals("listconcepts") || type.equals("listconcept")) {
    		arg.type = QUERY_TYPE.LIST_CONCEPTS;
    	}
    	else if(type.equals("docclass") || type.equals("interest")) {
    		arg.type = QUERY_TYPE.DOCCLASS;
    	}
    	else if(type.equals("userreport")) {
    		arg.type = QUERY_TYPE.USERREPORT;
    	}
    	else if(type.equals("feedbackreport")) {
    		arg.type = QUERY_TYPE.FEEDBACKREPORT;
    	}
    	else if(type.equals("totalreport")) {
    		arg.type = QUERY_TYPE.TOTALREPORT;
    	}
    	else if(type.equals("mlreg") || type.equals("mailingreg") || type.equals("mailingregister")) {
    		arg.type = QUERY_TYPE.MAILINGLIST_REG;
    	}
    	else if(type.equals("mlunreg") || type.equals("mailingunreg") || type.equals("mailingunregister")) {
    		arg.type = QUERY_TYPE.MAILINGLIST_UNREG;
    	}

    	
    	if(arg.type == QUERY_TYPE.OVERLAY || arg.type == QUERY_TYPE.OVERLAY_BAYESIAN || arg.type == QUERY_TYPE.DYN_OVERLAY_BAYESIAN ||
    		arg.type == QUERY_TYPE.RECOMMENDED_PRE_CONCEPT || arg.type == QUERY_TYPE.RECOMMENDED_POST_CONCEPT ||
    		arg.type == QUERY_TYPE.CONCEPT_INFO ||
    		arg.type == QUERY_TYPE.STUDY_TIME ||
    		arg.type == QUERY_TYPE.DOES_EXIST ||
    		arg.type == QUERY_TYPE.IS_KNOWLEDGE_EVIDENCE) {
    		arg.userid = argMap.get("userid");
    		arg.password = argMap.get("password");
    		arg.course = argMap.get("course");
    		if(arg.userid == null || arg.course == null) throw new Exception("Invalid arguments: User ID or Course is null");
    		arg.concept = argMap.get("concept");
    		if(arg.concept == null) {
    			arg.concept = argMap.get("subject");
    			if(arg.concept == null) arg.concept = argMap.get("topic");
    		}
    		if(arg.concept == null) arg.concept = arg.course;
    		
    		String[] ds = arg.concept.split(".");
    		if(ds.length > 1 && ds[1].length() > 0) arg.concept = ds[1];
    		return arg;
    	}
    	else if(arg.type == QUERY_TYPE.LEARNING_STYLE || 
    			arg.type == QUERY_TYPE.LEARNING_PATH || 
    			arg.type == QUERY_TYPE.COMMUNITY ||
				arg.type == QUERY_TYPE.DOCCLASS ||
				arg.type == QUERY_TYPE.USERREPORT ||
				arg.type == QUERY_TYPE.MAILINGLIST_REG || arg.type == QUERY_TYPE.MAILINGLIST_UNREG) {
    		arg.userid = argMap.get("userid");
    		arg.password = argMap.get("password");
    		arg.course = argMap.get("course");
    		if(arg.userid == null || arg.course == null) throw new Exception("Invalid arguments: : User ID or Course is null");
    		return arg;
    	}
    	else if(arg.type == QUERY_TYPE.PERSONAL_INFO) {
    		arg.userid = argMap.get("userid");
    		arg.password = argMap.get("password");
    		if(arg.userid == null) throw new Exception("Invalid arguments: : User ID or Course is null");
    		return arg;
    	}
    	else if(arg.type == QUERY_TYPE.LIST_COURSES) {
    		//No need parameters
    	}
    	else if(arg.type == QUERY_TYPE.LIST_CONCEPTS ||
    		arg.type == QUERY_TYPE.FEEDBACKREPORT ||
    		arg.type == QUERY_TYPE.TOTALREPORT) {
    		arg.course = argMap.get("course");
    		if(arg.course == null) throw new Exception("Invalid arguments: : Course is null");
    		return arg;
    	}
    	throw new Exception("Not implement yet for other arguments");
    }
	@SuppressWarnings("unchecked")
	public static String createHttpQueryString(Map<String, String> map) {
		String queryString = "";
		
    	HashMap<String, String> argMap = normalizeMap(map);
    	Object[] entries = argMap.entrySet().toArray();
    	for(int i = 0; i < entries.length; i++) {
    		Entry<String, String> entry = (Entry<String, String>) (entries[i]);
    		if(entry.getValue().length() == 0) continue;
    		queryString += entry.getKey() + "=" + entry.getValue();
    		if(i < entries.length - 1) queryString += QUERY_STRING_SEP; 
    	}
		return queryString;
	}
    private static HashMap<String, String> normalizeMap(Map<String, String> map) {
    	HashMap<String, String> argMap = new HashMap<String, String>();
    	Set<String> keys = map.keySet();
    	Iterator<String> iter = keys.iterator();
    	while(iter.hasNext()) {
    		String key = iter.next();
    		if(key == null) continue;
    		String value = map.get(key);
    		if(value == null) continue;
    		
    		key = key.trim(); value = value.trim();
    		if(key.length() == 0 || value.length() == 0) continue;
    		argMap.put(key.toLowerCase(), value);
    	}
    	return argMap;
    }
    public static QUERY_TYPE translateKnowledgeQueryType(String queryType) throws Exception {
    	if(queryType.equals("overlay")) {
    		return QUERY_TYPE.OVERLAY;
    	}
    	else if(queryType.equals("bayes") || queryType.equals("bayesian") ||
    			queryType.equals("overlaybayes") || queryType.equals("overlaybayesian")) {
    		return QUERY_TYPE.OVERLAY_BAYESIAN;
    	}
    	else if(queryType.equals("dyn") || queryType.equals("dynbayes") || queryType.equals("dynbayesian") ||
    			queryType.equals("dynoverlaybayes") || queryType.equals("dynoverlaybayesian") ||
    			queryType.equals("dynamic") || queryType.equals("dynamicbayes") || queryType.equals("dynamicbayesian") ||
    			queryType.equals("dynamicoverlaybayes") || queryType.equals("dynamicoverlaybayesian")) {
    		return QUERY_TYPE.DYN_OVERLAY_BAYESIAN;
    	}
    	else throw new Exception("This knowledge query type not implemented yet");
    	
    }
    //do reservedly
    public static String rTranslateKnowledgeQueryType(QUERY_TYPE queryType) throws Exception {
    	if(queryType == QUERY_TYPE.OVERLAY) {
    		return "overlay";
    	}
    	else if(queryType == QUERY_TYPE.OVERLAY_BAYESIAN) {
    		return "bayes";
    	}
    	else if(queryType == QUERY_TYPE.DYN_OVERLAY_BAYESIAN) {
    		return "dynbayes";
    	}
    	else throw new Exception("This knowledge type is invalid");
    }
}
