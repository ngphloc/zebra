/**
 * 
 */
package vn.spring.zebra.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;
import vn.spring.zebra.util.MathUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMQueryResult implements Serializable {
	private static final long serialVersionUID = 1L;
	public QUERY_TYPE queryType;
	
	public double knowledge = 0.0;
	public LearningStyle learningStyle = new LearningStyle(); 
	public ArrayList<String> preRecommendedConcepts = new ArrayList<String>();
	public ArrayList<String> postRecommendedConcepts = new ArrayList<String>();
	public ArrayList<ArrayList<String>> learningPath = new ArrayList<ArrayList<String>>();
	public ArrayList<String> community = new ArrayList<String>(); 
	public HashMap<String, String> personalInfo = new HashMap<String, String>();
	public HashMap<String, String> conceptInfo = new HashMap<String, String>();
	public StudyTime studyTime = new StudyTime();
	public boolean doesExist = false;
	public boolean isKnowledgeEvedence = false;
	public ArrayList<String> courseList = new ArrayList<String>();
	public ArrayList<String> briefConceptList = new ArrayList<String>();
	public String docClass = "";
	public String userReport = "";
	public String feedbackReport = "";
	public String totalReport = "";
	public boolean mlreg = false;
	public boolean mlunreg = false;
	
	public TriUMQueryResult() {}
	
	public Object getResult() throws Exception {
    	if(queryType == QUERY_TYPE.OVERLAY || queryType == QUERY_TYPE.OVERLAY_BAYESIAN || queryType == QUERY_TYPE.DYN_OVERLAY_BAYESIAN) {
    		return new Double(knowledge);
    	}
    	else if(queryType == QUERY_TYPE.LEARNING_STYLE) {
        	return learningStyle;
        }
    	else if(queryType == QUERY_TYPE.RECOMMENDED_PRE_CONCEPT) {
    		return preRecommendedConcepts;
    	}
    	else if(queryType == QUERY_TYPE.RECOMMENDED_POST_CONCEPT) {
    		return postRecommendedConcepts;
        }
    	else if(queryType == QUERY_TYPE.LEARNING_PATH) {
    		return learningPath;
        }
    	else if(queryType == QUERY_TYPE.COMMUNITY) {
    		return community;
        }
    	else if(queryType == QUERY_TYPE.PERSONAL_INFO) {
    		return personalInfo;
        }
    	else if(queryType == QUERY_TYPE.CONCEPT_INFO) {
    		return conceptInfo;
        }
    	else if(queryType == QUERY_TYPE.STUDY_TIME) {
    		return studyTime;
        }
    	else if(queryType == QUERY_TYPE.DOES_EXIST) {
    		return doesExist;
        }
    	else if(queryType == QUERY_TYPE.IS_KNOWLEDGE_EVIDENCE) {
    		return isKnowledgeEvedence;
        }
    	else if(queryType == QUERY_TYPE.LIST_COURSES) {
    		return courseList;
        }
    	else if(queryType == QUERY_TYPE.LIST_CONCEPTS) {
    		return briefConceptList;
        }
    	else if(queryType == QUERY_TYPE.DOCCLASS) {
    		return docClass;
        }
    	else if(queryType == QUERY_TYPE.USERREPORT) {
    		return userReport;
        }
    	else if(queryType == QUERY_TYPE.FEEDBACKREPORT) {
    		return feedbackReport;
        }
    	else if(queryType == QUERY_TYPE.TOTALREPORT) {
    		return totalReport;
        }
    	else if(queryType == QUERY_TYPE.MAILINGLIST_REG) {
    		return mlreg;
        }
    	else if(queryType == QUERY_TYPE.MAILINGLIST_UNREG) {
    		return mlunreg;
        }

    	throw new Exception("Not implement yet for other types");
	}
	public String toString() {
    	if(queryType == QUERY_TYPE.OVERLAY || queryType == QUERY_TYPE.OVERLAY_BAYESIAN || queryType == QUERY_TYPE.DYN_OVERLAY_BAYESIAN) {
    		return String.valueOf(knowledge);
    	}
    	else if(queryType == QUERY_TYPE.LEARNING_STYLE) {
        	return learningStyle.toString();
        }
    	else if(queryType == QUERY_TYPE.RECOMMENDED_PRE_CONCEPT || queryType == QUERY_TYPE.RECOMMENDED_POST_CONCEPT) {
    		ArrayList<String> concepts = null;
    		if(queryType == QUERY_TYPE.RECOMMENDED_PRE_CONCEPT) concepts = preRecommendedConcepts;
    		else                                                concepts = postRecommendedConcepts;
    		return CommonUtil.concat(concepts, ",");
        }
    	else if(queryType == QUERY_TYPE.LEARNING_PATH) {
    		String result = "";
    		for(int i = 0; i < learningPath.size(); i++) {
    			String set = "(";
    			for(int j = 0; j < learningPath.get(i).size(); j++) {
    				set += learningPath.get(i).get(j);
    				if(j < learningPath.get(i).size() - 1) set += ",";
    			}
    			set += ")";
    			result += set;
    			if(i < learningPath.size() - 1) result += "=>";
    		}
    		return result;
        }
    	else if(queryType == QUERY_TYPE.COMMUNITY) {
    		return CommonUtil.concat(community, ",");
    	}
    	else if(queryType == QUERY_TYPE.PERSONAL_INFO || queryType == QUERY_TYPE.CONCEPT_INFO) {
    		HashMap<String, String> attrs = (queryType == QUERY_TYPE.PERSONAL_INFO? personalInfo : conceptInfo);
    		return CommonUtil.concat(attrs, ",");
    	}
    	else if(queryType == QUERY_TYPE.STUDY_TIME) {
    		double conceptMinute = MathUtil.round((double)studyTime.conceptInterval / 60000.0, 2);
    		double courseMinute = MathUtil.round((double)studyTime.courseInterval / 60000.0, 2);
    		double totalMinute = MathUtil.round((double)studyTime.totalInterval / 60000.0, 2);
    		return "concept=" + conceptMinute + " / " + 
    			"course=" + courseMinute + " / " + 
    			"total=" + totalMinute + " (in minute)";
    	}
    	else if(queryType == QUERY_TYPE.DOES_EXIST) {
    		return String.valueOf(doesExist);
    	}
    	else if(queryType == QUERY_TYPE.IS_KNOWLEDGE_EVIDENCE) {
    		return String.valueOf(isKnowledgeEvedence);
    	}
    	else if(queryType == QUERY_TYPE.LIST_COURSES) {
    		return CommonUtil.concat(courseList, ",");
    	}
    	else if(queryType == QUERY_TYPE.LIST_CONCEPTS) {
    		return CommonUtil.concat(briefConceptList, ",");
    	}
    	else if(queryType == QUERY_TYPE.DOCCLASS) {
    		return docClass;
    	}
    	else if(queryType == QUERY_TYPE.USERREPORT) {
    		return userReport;
    	}
    	else if(queryType == QUERY_TYPE.FEEDBACKREPORT) {
    		return feedbackReport;
    	}
    	else if(queryType == QUERY_TYPE.TOTALREPORT) {
    		return totalReport;
    	}
    	else if(queryType == QUERY_TYPE.MAILINGLIST_REG) {
    		return String.valueOf(mlreg);
    	}
    	else if(queryType == QUERY_TYPE.MAILINGLIST_UNREG) {
    		return String.valueOf(mlunreg);
    	}
    	return "";
	}
	
}
