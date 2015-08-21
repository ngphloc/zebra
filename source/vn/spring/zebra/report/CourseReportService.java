/**
 * 
 */
package vn.spring.zebra.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import vn.spring.WOW.datacomponents.Concept;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.ConceptInfo;
import vn.spring.WOW.engine.CourseInfo;
import vn.spring.WOW.engine.TNode;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.feedback.UserFeedbackService;
import vn.spring.zebra.log.CourseAccessLog;
import vn.spring.zebra.log.UserAccessLog;
import vn.spring.zebra.mining.AssociationRule;
import vn.spring.zebra.mining.CourseAccessApriori;
import vn.spring.zebra.mining.CourseAccessAprioriDaemon;
import vn.spring.zebra.mining.CourseAccessSequencesDaemon;
import vn.spring.zebra.mining.DistanceProfile;
import vn.spring.zebra.mining.DocClassifierDaemon;
import vn.spring.zebra.mining.LearningHistoryDataDaemon;
import vn.spring.zebra.mining.MinerUtil;
import vn.spring.zebra.mining.ProfileCluster;
import vn.spring.zebra.mining.UserClusterer;
import vn.spring.zebra.mining.textmining.DocClassifier;
import vn.spring.zebra.server.TriUMServer;
import vn.spring.zebra.util.ConceptUtil;
import vn.spring.zebra.util.MathUtil;
import vn.spring.zebra.util.UserUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseReportService {
	protected CourseInfo        courseInfo = null;
	protected ArrayList<Profile> profileList = new ArrayList<Profile>();
	protected ArrayList<Concept> conceptList = new ArrayList<Concept>();
	protected CourseAccessLog log = null;
	
	public CourseReportService(String course) throws Exception {
		this.courseInfo = ConceptUtil.getCourseInfo(course);
		this.profileList = UserUtil.getProfileList(true);
		this.conceptList = ConceptUtil.getConceptList(course);
		LearningHistoryDataDaemon daemon = TriUMServer.getInstance().getMiner().getCourseHisDataDaemon(courseInfo.courseName);
		if(daemon != null)
			this.log = daemon.getHisData().getLog();
		else {
			this.log = ZebraStatic.getLogDB().getCourseAccessLog(
					courseInfo.courseName, ZebraStatic.MINER_LOG_BEGIN_DATE, ZebraStatic.IGNORE_ANONYMOUS);
		}
		
		Collections.sort(this.profileList, new Comparator<Profile>() {
			public int compare(Profile o1, Profile o2) {
				String cName1 = null;
				String cName2 = null;
				try {
					cName1 = o1.getAttributeValue("personal", "name");
					cName2 = o2.getAttributeValue("personal", "name");
				}
				catch(Exception e) {e.printStackTrace();}
				
				try {
					if(cName1 == null || cName1.trim().length() == 0) 
						cName1 = o1.getAttributeValue("personal", "id");
					if(cName2 == null || cName2.trim().length() == 0) 
						cName2 = o2.getAttributeValue("personal", "id");
				}
				catch(Exception e) {e.printStackTrace(); return 0;}
				return cName1.compareTo(cName2);
			}
		});
	}
	
	public String genReport() {
		StringBuffer html = new StringBuffer();
		
		html.append("<div>" + "\n");
		
		html.append("<h1><center>Report on \"" + courseInfo.courseName + "\"</center></h1>" + "\n");
		html.append("<a name=\"toc_" + courseInfo.courseName + "\"/>" + "\n");
		html.append("TOC" + "\n");
		html.append("<ol>" + "\n");
		html.append("  <li>" + "<a href=\"#recommend_" + courseInfo.courseName + "\">Recommendation</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#lp_" + courseInfo.courseName + "\">Learning Path</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#docclass_" + courseInfo.courseName + "\">User Interests / Document Classification Rules</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#community_" + courseInfo.courseName + "\">Communities</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#studytime_" + courseInfo.courseName + "\">Study Time</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#testlog_" + courseInfo.courseName + "\">Test Log</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#accesslog_" + courseInfo.courseName + "\">Access Log</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#searchlog_" + courseInfo.courseName + "\">Search Log</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#feedback_" + courseInfo.courseName + "\">Feedback Report</a>" + "</li>" + "\n");
		html.append("</ol>" + "\n");
		html.append("<hr/>" + "\n");
		
		html.append("<h2>Recommendation</h2>" + "\n");
		try {
			html.append(genRecommendationReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + courseInfo.courseName + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");
		
		html.append("<h2>Learning Path</h2>" + "\n");
		try {
			html.append(genLearningPathReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + courseInfo.courseName + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>User Interests / Document Classification Rules</h2>" + "\n");
		try {
			html.append(genDocClassReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + courseInfo.courseName + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>Communities</h2>" + "\n");
		try {
			html.append(genCommunityReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + courseInfo.courseName + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>Study Time</h2>" + "\n");
		try {
			html.append(genStudyTimeReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + courseInfo.courseName + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>Test Log</h2>" + "\n");
		try {
			html.append(genTestLogReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + courseInfo.courseName + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>Access Log</h2>" + "\n");
		try {
			html.append(genAccessLogReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + courseInfo.courseName + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>Search Log</h2>" + "\n");
		try {
			html.append(genSearchLogReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + courseInfo.courseName + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>Feedback Report</h2>" + "\n");
		try {
			html.append(genFeedbackReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + courseInfo.courseName + "\">toc</a>" + "</font>" + "\n");

		html.append("</div>");
		return html.toString();
	}
	
	public String genRecommendationReport() throws Exception {
		StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		html.append("<a name=\"recommend_" + courseInfo.courseName + "\"/>" + "\n");
		CourseAccessApriori recommender = null;
		CourseAccessAprioriDaemon daemon = TriUMServer.getInstance().getMiner().
			getCourseRecommenderDaemon(courseInfo.courseName);
		if(daemon == null)
			recommender = MinerUtil.createCourseRecommender(courseInfo.courseName);
		else
			recommender = daemon.getCourseRecommend();
		
		ArrayList<AssociationRule> rules= recommender.getAssociationRules();
		html.append("<h3>Recommended Rules</h3>" + "\n");
		html.append("<ol>");
		for(int i = 0; i < rules.size(); i++) {
			html.append("<li><font color=\"#FF0000\"><strong>" + rules.get(i).toString2() + "</strong></font></li>" + "\n");
		}
		html.append("</ol>");
		
		ArrayList<String> cList = ConceptUtil.getConceptNameList(courseInfo.courseName, ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE);
		Collections.sort(cList);
		
		html.append("<h3>Recommended Concepts</h3>" + "\n");
		html.append("<table border=\"1\">" + "\n");
		html.append("<tr><th>Pre-recommended Concepts</th><th>CONCEPT</th><th>Post-recommended Concepts</th></tr>" + "\n");
		for(int i = 0; i < cList.size(); i++) {
			String concept = cList.get(i);
			ConceptInfo cInfo = courseInfo.conceptsInfoTbl.getConceptInfo(concept);
			ArrayList<String> preConcepts = recommender.getPreRecommendedConcepts(concept, ZebraStatic.RECOMMEND_MAX_CONCEPT);
			ArrayList<String> postConcepts = recommender.getPostRecommendedConcepts(concept, ZebraStatic.RECOMMEND_MAX_CONCEPT);
			if(preConcepts.size() == 0 && postConcepts.size() == 0) continue;
			
			html.append("<tr>" + "\n");
			html.append("<td>" + "\n");
			if(preConcepts.size() > 0) {
				html.append("<ul>" + "\n");
				for(int j = 0; j < preConcepts.size(); j++) {
					html.append("<li>" + courseInfo.conceptsInfoTbl.
							getConceptInfo(preConcepts.get(j)).getTitle() + "</li>" + "\n");
				}
				html.append("</ul>" + "\n");
			}
			html.append("</td>" + "\n");
			
			html.append("<td><font color=\"#0000FF\"><strong>" + cInfo.getTitle() + "</strong></font></td>" + "\n");

			html.append("<td>" + "\n");
			if(postConcepts.size() > 0) {
				html.append("<ul>" + "\n");
				for(int j = 0; j < postConcepts.size(); j++) {
					html.append("<li>" + cInfo.getTitle() + "</li>" + "\n");
				}
				html.append("</ul>" + "\n");
			}
			html.append("</td>" + "\n");
			
			html.append("</tr>" + "\n");
		}
		html.append("</table>" + "\n");
		html.append("<br/><font color=\"#FF0000\"><em>(The access log used to mine recommendation rules is shown in <a href=\"#accesslog_" + 
				courseInfo.courseName + "\">Access Log</a>)</em></font>");
		
		html.append("</div>");
		return html.toString();
	}
	public String genLearningPathReport() throws Exception {
		ArrayList<ArrayList<String>> learningPath = null;
		CourseAccessSequencesDaemon daemon = TriUMServer.getInstance().getMiner().
			getCourseSequenceDaemon(courseInfo.courseName);
		if(daemon == null)
			learningPath = MinerUtil.createCourseSequence(courseInfo.courseName).getMaxLearningPath();
		else
			learningPath = daemon.getCourseSequence().getMaxLearningPath();
		
		StringBuffer html = new StringBuffer();
		String result = "";
		for(int i = learningPath.size() - 1; i >= 0 ; i--) {
			String set = "(";
			for(int j = 0; j < learningPath.get(i).size(); j++) {
				String cTitle = learningPath.get(i).get(j);
				try {
					cTitle = courseInfo.conceptsInfoTbl.
						getConceptInfo(learningPath.get(i).get(j)).getTitle();
				}
				catch(Throwable e) {e.printStackTrace();}
				set += cTitle;
				if(j < learningPath.get(i).size() - 1) set += ",";
			}
			set += ")";
			result += set;
			if(i > 0) result += " => ";
		}
		
		html.append("<div>" + "\n");
		html.append("<a name=\"lp_" + courseInfo.courseName + "\"/>" + "\n");
		html.append("<font color=\"0000FF\">" + result + "</font>");
		html.append("<br/><br/>" + "\n");
		html.append("<font color=\"#FF0000\"><em>(The access log used to mine learning path is shown in <a href=\"#accesslog_" + 
				courseInfo.courseName + "\">Access Log</a>)</em></font>");
		
		html.append("\n");
		html.append("</div>");
		return html.toString();
	}
	public String genDocClassReport() {
		StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		html.append("<a name=\"docclass_" + courseInfo.courseName + "\"/>" + "\n");
		String result = "No result!";
		try {
			DocClassifier classifier = null;
			DocClassifierDaemon daemon = TriUMServer.getInstance().getMiner().
				getCourseDocClassifierDaemon(courseInfo.courseName);
			if(daemon == null)
				classifier = TriUMServer.getInstance().getMiner().
					addCourseDocClassifierDaemon(courseInfo.courseName);
			else
				classifier = daemon.getDocClassifier();
			
			result = classifier.toHtml(courseInfo);
		}
		catch(Exception e) {e.printStackTrace();}
		html.append(result + "\n");
		html.append("<br/><br/><font color=\"#FF0000\"><em>(The search log used to mine user interests / document classification rules is shown in <a href=\"#searchlog_" + 
				courseInfo.courseName + "\">Search Log</a>)</em></font>");
		
		html.append("</div>");
		return html.toString();
	}
	public String genCommunityReport() throws Exception {
		StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		html.append("<a name=\"community_" + courseInfo.courseName + "\"/>" + "\n");
		UserClusterer clusterer = 
			TriUMServer.getInstance().getUserClustererService().getClusterer(courseInfo.courseName, true);
		ArrayList<ProfileCluster> communities = clusterer.getClusters();
		
		html.append("<table border=\"1\">" + "\n");
		html.append("<tr><th>Community</th><th>Users</th></tr>" + "\n");
		for(int i = 0; i < communities.size(); i++) {
			ProfileCluster community = communities.get(i);
			html.append("<tr>" + "\n");
			html.append("<td><font color=\"FF0000\"><strong>COMMUNITY " + (i + 1) + "</strong></font></td>" + "\n");
			html.append("<td><ul>" + "\n");
			ArrayList<DistanceProfile> profiles = community.getProfiles();
			ArrayList<String> usernames = new ArrayList<String>();
			
			for(DistanceProfile profile : profiles) {
				String username = null;
				try {
					username = profile.profile.getAttributeValue("personal", "name");
				}
				catch(Exception e) {e.printStackTrace(); username = null;}
				if(username == null || username.trim().length() == 0) username = profile.userid;
				
				usernames.add(username);
			}

			for(String username : usernames) {
				html.append("<li>" + username + "</li>" + "\n");
			}
			html.append("</ul></td>" + "\n");
			html.append("</tr>" + "\n");
		}
		html.append("</table>" + "\n");
		
		html.append("</div>");
		return html.toString();
	}
	public String genStudyTimeReport() throws Exception {
		TNode root = courseInfo.hierarchy.getRootNode().getFirstChild();//root name = "javatutorial.javatutorial"
		StringBuffer html = new StringBuffer();
		
		html.append("<div>" + "\n");
		html.append("<a name=\"studytime_" + courseInfo.courseName + "\"/>" + "\n");
		if(log.size() == 0) {
			html.append("</div>");
			return html.toString();
		}
		
		html.append("<h3>Study Time for Concepts" + "</h3>" + "\n");
		html.append("<table border=\"1\">" + "\n");
		html.append(createRow(root.getName(), 0, true, log));
		if(root.getFirstChild() != null) html.append("\n");
		menuList(root, 1, html, true, log);
		html.append("\n");
		html.append("</table>" + "\n");
		
		long maxInterval = 0, minInterval = Long.MAX_VALUE;
		String maxIntervalConcept = null, minIntervalConcept = null;
		for(Concept concept : conceptList) {
			long interval = log.numberOfTimeSpend(concept.getName());
			if(maxInterval <= interval) {
				maxInterval = interval;
				maxIntervalConcept = concept.getTitle();
			}
			if(minInterval >= interval) {
				minInterval = interval;
				minIntervalConcept = concept.getTitle();
			}
		}
		html.append("<br/>" + "\n");
		html.append("Minimum Study Time Concept: <font color=\"#0000FF\">" + 
				minIntervalConcept + "</font> : <font color=\"#FF0000\">" + 
				MathUtil.round((double)minInterval / 60000.0, 2) + " minutes</font><br/>" + "\n");
		html.append("Maximum Study Time Concept: <font color=\"#0000FF\">" + 
				maxIntervalConcept + "</font> : <font color=\"#FF0000\">" + 
				MathUtil.round((double)maxInterval / 60000.0, 2) + " minutes</font>");

		html.append("<br/><br/>" + "\n");
		html.append("<h3>Study Time for Users" + "</h3>" + "\n");
		html.append("<table border=\"1\">" + "\n");
		maxInterval = 0; minInterval = Long.MAX_VALUE;
		String maxIntervalUserName = null, minIntervalUserName = null;
		for(Profile profile : profileList) {
			String userid = null;
			try {
				userid = profile.getAttributeValue("personal", "id");
			}
			catch(Exception e) {e.printStackTrace(); userid = null;}
			if(userid == null) continue;
			int idx = log.findUserLog(userid);
			if(idx == -1) continue;
			
			String username = null;
			try {
				username = profile.getAttributeValue("personal", "name");
			}
			catch(Exception e) {e.printStackTrace(); username = null;}
			if(username == null) username = userid;
			
			UserAccessLog userAccessLog = log.getUserLog(idx);
			long interval = userAccessLog.numberOfTimeSpend();
			if(maxInterval <= interval) {
				maxInterval = interval;
				maxIntervalUserName = username;
			}
			if(minInterval >= interval) {
				minInterval = interval;
				minIntervalUserName = username;
			}
			html.append("<tr><td>" + username + "</td><td><font color=\"#FF0000\">" + 
					MathUtil.round((double)interval / 60000.0, 2) + " minutes</font></td></tr>" + "\n");
		}
		html.append("</table>" + "\n");
		html.append("<br/>" + "\n");
		html.append("Minimum Study Time User: <font color=\"#0000FF\">" + 
				minIntervalUserName + "</font> : <font color=\"#FF0000\">" + 
				MathUtil.round((double)minInterval / 60000.0, 2) + " minutes</font><br/>" + "\n");
		html.append("Maximum Study Time User: <font color=\"#0000FF\">" + 
				maxIntervalUserName + "</font> : <font color=\"#FF0000\">" + 
				MathUtil.round((double)maxInterval / 60000.0, 2) + "minutes</font>" + "\n");

		html.append("</div>");
		return html.toString();
	}

	private void menuList(TNode parent, int listindex, StringBuffer table, boolean isStudyTime, CourseAccessLog log) throws Exception {
        TNode childNode = parent.getFirstChild();
        while (childNode != null) {
        	boolean exist = isStudyTime ? true : ConceptUtil.doesExist(
        		courseInfo.courseName, 
        		ConceptUtil.getBriefConceptName(courseInfo.courseName,  childNode.getName()), 
        		ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE);
        	
        	if(exist) {
	        	table.append("\n");
	            table.append(createRow(childNode.getName(), listindex*25, isStudyTime, log));
	            menuList(childNode, listindex + 1, table, isStudyTime, log);
        	}
            childNode = childNode.getNextSib();
        }
    }
    private String createRow(String cName, int width, boolean isStudyTime, CourseAccessLog log) throws Exception {
    	String cTitle = 
    		"<font color=\"#0000FF\">" + 
    		courseInfo.conceptsInfoTbl.getConceptInfo(cName).getTitle() +
    		"</font>";
    	if(isStudyTime) {
    		double conceptMinute = MathUtil.round((double)log.numberOfTimeSpend(cName) / 60000.0, 2);
    		double courseMinute = MathUtil.round((double)log.numberOfTimeSpend() / 60000.0, 2);
    		String rootConceptName = courseInfo.hierarchy.getRootNode().getFirstChild().getName();
    		if(rootConceptName.equals(cName)) {
    	        cTitle +=
    	        	"<font size=\"-1\" color=\"#FF0000\">" +
    	        	": " + courseMinute + " (total) minutes" +
    	        	"</font>";
    		}
    		else {
    	        cTitle +=
    	        	"<font size=\"-1\" color=\"#FF0000\">" +
    	        	": " + conceptMinute + " / " + courseMinute + " minutes" +
    	        	"</font>";
    		}
    	}
    	else {
    		throw new Exception("Not implemented yet");
    	}
    	String row = "  <tr><td style=\"padding-left:" + width + "px\">" +
    			cTitle + "</td></tr>";
        return row;
    }
	public String genTestLogReport() throws Exception {
    	StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		html.append("<a name=\"testlog_" + courseInfo.courseName + "\"/>" + "\n");
		
		for(Profile profile : profileList) {
			String userid = null;
			try                {userid = profile.getAttributeValue("personal", "id");}
			catch(Exception e) {e.printStackTrace(); continue;}
			
			String username = null;
			try {username = profile.getAttributeValue("personal", "name");}
			catch(Exception e) {username = userid;}

			html.append("<h3>Test Log of \"" + username + "\"</h3>" + "\n");
			html.append(UserReportService.genTestLogHtmlReport(userid, courseInfo.courseName) + "<br/><br/>" + "\n");
		}
		
		html.append("</div>");
		return html.toString();
	}
    
    public String genAccessLogReport() throws Exception {
		StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		html.append("<a name=\"accesslog_" + courseInfo.courseName + "\"/>" + "\n");
		
		for(Profile profile : profileList) {
			String userid = null;
			try                {userid = profile.getAttributeValue("personal", "id");}
			catch(Exception e) {e.printStackTrace(); continue;}
			
			String username = null;
			try {username = profile.getAttributeValue("personal", "name");}
			catch(Exception e) {username = userid;}
			
			html.append("<h3>Access Log of \"" + username + "\"</h3>" + "\n");
			html.append(UserReportService.genAccessLogHtmlReport(userid, courseInfo, log) + "<br/><br/>" + "\n");
		}
		
		html.append("</div>");
		return html.toString();
    }
    
    public String genSearchLogReport() throws Exception {
		StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		html.append("<a name=\"searchlog_" + courseInfo.courseName + "\"/>" + "\n");
		
		for(Profile profile : profileList) {
			String userid = null;
			try                {userid = profile.getAttributeValue("personal", "id");}
			catch(Exception e) {e.printStackTrace(); continue;}
			
			String username = null;
			try {username = profile.getAttributeValue("personal", "name");}
			catch(Exception e) {username = userid;}
			
			html.append("<h3>Search Log of \"" + username + "\"</h3>" + "\n");
			html.append(UserReportService.genSearchLogHtmlReport(userid, courseInfo.courseName)+ "\n");
		}
		
		html.append("</div>");
		return html.toString();
    }
    
    public String genFeedbackReport() {
    	UserFeedbackService feedbackService = new UserFeedbackService(courseInfo.courseName);
    	feedbackService.load();
		
    	StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		
		html.append("<a name=\"feedback_" + courseInfo.courseName + "\"/>" + "\n");
		html.append(feedbackService.reportSummaryHTML() + "\n");
    	
		html.append("</div>");
		return html.toString();
    }
    public static void main(String[] args) {
		try {
			CourseReportService service = new CourseReportService("javatutorial");
			System.out.println(service.genRecommendationReport());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
