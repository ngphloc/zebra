/**
 * 
 */
package vn.spring.zebra.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

import es.uco.WOW.Utils.EvalTestLogStudent;
import es.uco.WOW.Utils.Student;
import es.uco.WOW.Utils.TestLogStudent;

import vn.spring.WOW.datacomponents.Concept;
import vn.spring.WOW.engine.ConceptInfo;
import vn.spring.WOW.engine.CourseInfo;
import vn.spring.WOW.engine.PNode;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.StudyTime;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.evaluation.EvalUtil;
import vn.spring.zebra.feedback.UserFeedback;
import vn.spring.zebra.log.CourseAccessLog;
import vn.spring.zebra.log.UserAccessLog;
import vn.spring.zebra.log.UserAccessRecord;
import vn.spring.zebra.log.UserAccessSession;
import vn.spring.zebra.search.CourseSearchLog;
import vn.spring.zebra.search.SearchLogService;
import vn.spring.zebra.search.UserSearchLog;
import vn.spring.zebra.search.UserSearchRecord;
import vn.spring.zebra.search.UserSearchSession;
import vn.spring.zebra.server.TriUMServer;
import vn.spring.zebra.um.ConceptNodeTypeWrapper;
import vn.spring.zebra.um.PersonalInfo;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.um.UserCourseInfo;
import vn.spring.zebra.util.ConceptUtil;
import vn.spring.zebra.util.MathUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserReportService {
	protected TriUM um = null;
	protected UserCourseInfo userCourseInfo = null;
	protected TriUMQuery query = null;
	
	public UserReportService(TriUM um) throws Exception {
		this.um = um;
		this.userCourseInfo = new UserCourseInfo(um.getUserId(), um.getCourse());
		this.query = TriUMServer.getInstance().getCommunicateService().getQueryDelegator();
	}
	public String genHtmlReport() {
		StringBuffer html = new StringBuffer();
		String username = um.getUserId();
		try {
			username = um.getProfile().getAttributeValue("personal", "name");
		}
		catch(Exception e) {e.printStackTrace();}
		
		html.append("<div>" + "\n");
		
		html.append("<h1><center>Report on \"" + username + "\"" + 
			" in \"" + um.getCourse() + "\" course" + "</center></h1>" + "\n");
		html.append("<a name=\"toc_" + um.getUserId() + "_" + um.getCourse() + "\"/>" + "\n");
		html.append("TOC" + "\n");
		html.append("<ol>" + "\n");
		html.append("  <li>" + "<a href=\"#personal_" + um.getUserId() + "_" + um.getCourse() + "\">Person Information</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#ls_" + um.getUserId() + "_" + um.getCourse() + "\">Learning Style</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#interest_" + um.getUserId() + "_" + um.getCourse() + "\">Interest</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#lp_" + um.getUserId() + "_" + um.getCourse() + "\">Learning Path</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#community_" + um.getUserId() + "_" + um.getCourse() + "\">Your Community</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#courseeval_" + um.getUserId() + "_" + um.getCourse() + "\">Course Evaluation - Study Result</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#studytime_" + um.getUserId() + "_" + um.getCourse() + "\">Study Time</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#testlog_" + um.getUserId() + "_" + um.getCourse() + "\">Test Log</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#accesslog_" + um.getUserId() + "_" + um.getCourse() + "\">Access Log</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#searchlog_" + um.getUserId() + "_" + um.getCourse() + "\">Search Log</a>" + "</li>" + "\n");
		html.append("  <li>" + "<a href=\"#feedback_" + um.getUserId() + "_" + um.getCourse() + "\">User Feedback</a>" + "</li>" + "\n");
		html.append("</ol>" + "\n");
		html.append("<hr/>" + "\n");
		
		html.append("<h2>Person Information</h2>" + "\n");
		try {
			html.append(genPersonalHtmlReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + um.getUserId() + "_" + um.getCourse() + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");
		
		html.append("<h2>Learning Style</h2>" + "\n");
		try {
			html.append(genLearningStyleHtmlReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + um.getUserId() + "_" + um.getCourse() + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>Interest</h2>" + "\n");
		try {
			html.append(genInterestHtmlReport(um.getUserId(), userCourseInfo.getCourseInfo()) + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + um.getUserId() + "_" + um.getCourse() + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>Learning Path</h2>" + "\n");
		try {
			html.append(genLearningPathHtmlReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + um.getUserId() + "_" + um.getCourse() + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>Your Community</h2>" + "\n");
		try {
			html.append(genCommunityHtmlReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + um.getUserId() + "_" + um.getCourse() + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>Course Evaluation - Study Result</h2>" + "\n");
		try {
			html.append(genCourseEvalHtmlReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + um.getUserId() + "_" + um.getCourse() + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>Study Time</h2>" + "\n");
		try {
			html.append(genStudyTimeHtmlReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + um.getUserId() + "_" + um.getCourse() + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>Test Log</h2>" + "\n");
		try {
			html.append(genTestLogHtmlReport(um.getUserId(), um.getCourse()) + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + um.getUserId() + "_" + um.getCourse() + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>Access Log</h2>" + "\n");
		try {
			html.append(genAccessLogHtmlReport(um.getUserId(), userCourseInfo.getCourseInfo(), um.getHisData().getLog()) + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + um.getUserId() + "_" + um.getCourse() + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>Search Log</h2>" + "\n");
		try {
			html.append(genSearchLogHtmlReport(um.getUserId(), um.getCourse()) + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + um.getUserId() + "_" + um.getCourse() + "\">toc</a>" + "</font>" + "\n");
		html.append("<hr/>" + "\n");

		html.append("<h2>User Feedback</h2>" + "\n");
		try {
			html.append(genFeedbackHtmlReport() + "\n");
		}
		catch(Exception e) {e.printStackTrace();}
		html.append("<br/><font size=\"-2\">" + "<a href=\"#toc_" + um.getUserId() + "_" + um.getCourse() + "\">toc</a>" + "</font>" + "\n");

		html.append("</div>");
		return html.toString();
	}
	public String genPersonalHtmlReport() throws Exception {
		PersonalInfo personalInfo = new PersonalInfo(um.getUserId());
		StringBuffer html = new StringBuffer();
		HashMap<String, String> infos = personalInfo.getInfos2();
		
		html.append("<div>" + "\n");
		html.append("<a name=\"personal_" + um.getUserId() + "_" + um.getCourse() + "\"/>" + "\n");
		html.append("<table border=\"1\">");
		ArrayList<String> keys = personalInfo.getUnremovableKeys();
		for(String key : keys) {
			String value = infos.get(key);
			if(value == null || value.length() == 0) continue;
			infos.remove(key);
			html.append("\n");
			html.append("  <tr><td>" + key + "</td><td>" + value +"</td></tr>");
		}
		
		keys.clear();
		keys.addAll(infos.keySet());
		for(String key : keys) {
			String value = infos.get(key);
			if(value == null || value.length() == 0) continue;
			html.append("\n");
			html.append("  <tr><td>" + key + "</td><td>" + value +"</td></tr>");
		}
		
		html.append("\n");
		html.append("</table>" + "\n");
		html.append("</div>");
		return html.toString();
	}
	
	public String genLearningStyleHtmlReport() throws Exception {
		StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		html.append("<a name=\"ls_" + um.getUserId() + "_" + um.getCourse() + "\"/>" + "\n");
		html.append("<h3><font color=\"0000FF\">" + query.learningStyleQuery(um.getUserId(), um.getCourse()) + "</font></h3>");
		html.append("</div>");
		return html.toString();
	}
	public static String genInterestHtmlReport(String userid, CourseInfo courseInfo) throws Exception {
		TriUMQuery query = TriUMServer.getInstance().getCommunicateService().getQueryDelegator();

		StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		html.append("<a name=\"interest_" + userid + "_" + courseInfo.courseName + "\"/>" + "\n");
		String cTitle =  query.getDocClass(userid, courseInfo.courseName);
		if(cTitle == null) cTitle = "";
		
		if(cTitle.length() > 0) {
			try {
				cTitle = courseInfo.conceptsInfoTbl.
					getConceptInfo(cTitle).getTitle();
			}
			catch(Throwable e) {e.printStackTrace();}
		}
		html.append("<h3><font color=\"0000FF\">" + cTitle + "</font></h3>");
		html.append("<br/>" + "\n");
		html.append("<font color=\"#FF0000\"><em>(The search log used to mine interest is shown in <a href=\"#searchlog_" + 
				userid + "_" + courseInfo.courseName + "\">Search Log</a>)</em></font>");
		
		html.append("\n");
		html.append("</div>");
		return html.toString();
	}
	public String genLearningPathHtmlReport() throws Exception {
		StringBuffer html = new StringBuffer();
		String result = "";
		ArrayList<ArrayList<String>> learningPath = query.learningPathQuery(um.getUserId(), um.getCourse());
		for(int i = learningPath.size() - 1; i >= 0 ; i--) {
			String set = "(";
			for(int j = 0; j < learningPath.get(i).size(); j++) {
				String cTitle = learningPath.get(i).get(j);
				try {
					cTitle = userCourseInfo.getCourseInfo().conceptsInfoTbl.
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
		html.append("<a name=\"lp_" + um.getUserId() + "_" + um.getCourse() + "\"/>" + "\n");
		html.append("<font color=\"0000FF\">" + result + "</font>");
		html.append("<br/><br/>" + "\n");
		html.append("<font color=\"#FF0000\"><em>(The access log used to mine learning path is shown in <a href=\"#accesslog_" + um.getUserId() + "_" + um.getCourse() + "\">Access Log</a>)</em></font>");
		
		html.append("\n");
		html.append("</div>");
		return html.toString();
	}
	public String genCommunityHtmlReport() throws Exception {
		StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		html.append("<a name=\"community_" + um.getUserId() + "_" + um.getCourse() + "\"/>" + "\n");
		
		ArrayList<String> userids = query.communityQuery(um.getUserId(), um.getCourse());
		html.append("<ul>" + "\n");
		for(String userid :  userids) {
			String username = userid;
			try {
				username = userCourseInfo.getUserInfo().getProfile().getAttributeValue("personal", "name");
			}
			catch(Exception e) {e.printStackTrace();}
			if(username == null || username.length() == 0) username = userid;
			html.append("<li>" + username + "</li>" + "\n");
		}
		html.append("</ul>" + "\n");
		html.append("</div>");
		return html.toString();
	}
	public String genCourseEvalHtmlReport() throws Exception {
		PNode root = new PNode(
				userCourseInfo.getCourseInfo().hierarchy.getRootNode().getFirstChild(), 
				userCourseInfo.getUserInfo().getProfile());//root name = "javatutorial.javatutorial"
		StringBuffer html = new StringBuffer();
		
		html.append("<div>" + "\n");
		html.append("<a name=\"courseeval_" + um.getUserId() + "_" + um.getCourse() + "\"/>" + "\n");
		html.append("<table border=\"1\">" + "\n");
		
		html.append(createRow(root.getName(), 0, false));
		if(root.getFirstChild() != null) html.append("\n");
		menuList(root, 1, html, false);
		
		html.append("\n");
		html.append("</table>" + "\n");
		html.append("</div>");
		return html.toString();
	}
	
	public String genStudyTimeHtmlReport() throws Exception {
		PNode root = new PNode(
				userCourseInfo.getCourseInfo().hierarchy.getRootNode().getFirstChild(), 
				userCourseInfo.getUserInfo().getProfile());//root name = "javatutorial.javatutorial"
		StringBuffer html = new StringBuffer();
		
		html.append("<div>" + "\n");
		html.append("<a name=\"studytime_" + um.getUserId() + "_" + um.getCourse() + "\"/>" + "\n");
		html.append("<table border=\"1\">" + "\n");
		
		html.append(createRow(root.getName(), 0, true));
		if(root.getFirstChild() != null) html.append("\n");
		menuList(root, 1, html, true);
		
		html.append("\n");
		html.append("</table>" + "\n");
		html.append("</div>");
		return html.toString();
	}

	private void menuList(PNode parent, int listindex, StringBuffer table, boolean isStudyTime) throws Exception {
        PNode childNode = parent.getFirstChild();
        while (childNode != null) {
        	boolean exist = isStudyTime ? true : query.doesExist(
        		um.getUserId(), 
        		um.getCourse(), 
        		ConceptUtil.getBriefConceptName(userCourseInfo.getCourse(),  childNode.getName()), 
        		ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, 
        		false);
        	
        	if(exist) {
	        	table.append("\n");
	            table.append(createRow(childNode.getName(), listindex*25, isStudyTime));
	            menuList(childNode, listindex + 1, table, isStudyTime);
        	}
            childNode = childNode.getNextSib();
        }
    }
    private String createRow(String cName, int width, boolean isStudyTime) throws Exception {
    	String cTitle = 
    		"<font color=\"#0000FF\">" + 
    		userCourseInfo.getCourseInfo().conceptsInfoTbl.getConceptInfo(cName).getTitle() +
    		"</font>";
    	if(isStudyTime) {
			StudyTime studyTime = query.studyTimeQuery(um.getUserId(), um.getCourse(), 
				ConceptUtil.getBriefConceptName(um.getCourse(), cName));
    		double conceptMinute = MathUtil.round((double)studyTime.conceptInterval / 60000.0, 2);
    		double courseMinute = MathUtil.round((double)studyTime.courseInterval / 60000.0, 2);
    		double totalMinute = MathUtil.round((double)studyTime.totalInterval / 60000.0, 2);
    		String rootConceptName = userCourseInfo.getCourseInfo().hierarchy.getRootNode().getFirstChild().getName();
    		if(rootConceptName.equals(cName)) {
    	        cTitle +=
    	        	"<font size=\"-1\" color=\"#FF0000\">" +
    	        	": " + courseMinute + "(this course) / " + totalMinute + " (all courses) minutes" +
    	        	"</font>";
    		}
    		else {
    	        cTitle +=
    	        	"<font size=\"-1\" color=\"#FF0000\">" +
    	        	": " + conceptMinute + " / " + courseMinute + " / " + totalMinute + " minutes" +
    	        	"</font>";
    		}
    	}
    	else {
	        double curknowledge = ConceptUtil.knowledgeQuery(query, 
	        		um.getUserId(), um.getCourse(), cName);
	        cTitle +=
	        	"<em><font size=\"-1\" color=\"#FF0000\">" +
	        	": " + ((int)(curknowledge * 100.0)) + "% mastered" +
	        	"</font></em>";
    	}
    	String row = "  <tr><td style=\"padding-left:" + width + "px\">" +
    			cTitle + "</td></tr>";
        return row;
    }
	public static String genTestLogHtmlReport(String userid, String course) throws Exception {
    	StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		html.append("<a name=\"testlog_" + userid + "_" + course + "\"/>" + "\n");
		
		ArrayList<Concept> cList = ConceptUtil.getConceptList(course);
		Collections.sort(cList, new Comparator<Concept>() {
			public int compare(Concept o1, Concept o2) {
				String cName1 = o1.getTitle(), cName2 = o2.getTitle();
				if(cName1 == null || cName1.trim().length() == 0) cName1 = o1.getName();
				if(cName2 == null || cName2.trim().length() == 0) cName2 = o2.getName();
				return cName1.compareTo(cName2);
			}
			
		});
		html.append("<table border=\"1\">" + "\n");
		html.append("<tr><th>Test Name</th><th>History</th></tr>" + "\n");
		for(Concept concept : cList) {
			boolean finished = true;
			Student student = EvalUtil.getStudentFinishedTest(userid, course, concept.getName());
			if(student == null) {
				finished = false;
				student = EvalUtil.getStudentNotFinishedTest(userid, course, concept.getName());
			}
			if(student == null) continue;
			Vector<?> testLogStudentVector = student.getTest();
			if(testLogStudentVector.size() == 0) continue;
			
			TestLogStudent test = (TestLogStudent)testLogStudentVector.get(0);
			html.append("<tr>" + "\n");
			
			html.append("  <td>" + "\n");
			html.append("  <font size=\"+1\" color=\"0000FF\"><strong>" + test.getName() + "</strong></font><br/>" + 
				(finished ? "Finished" : "Not finished") + "<br/>" +
				"Make: " + test.getMake() + " times" + "<br/>" +
				"<font color=\"FF0000\">LAST SCORE: " + test.getLastScore() + "</font>");
			html.append("  </td>" + "\n");

			html.append("  <td>" + "\n");
			html.append("  <ul>" + "\n");
			Vector<?> evalTestLogStudentVector = test.getEvaluatedTest();
			for(int i = 0; i < evalTestLogStudentVector.size(); i++) {
				EvalTestLogStudent eval = (EvalTestLogStudent)evalTestLogStudentVector.get(i);
				html.append("    <li>" + "\n");
				html.append("        Last performance - <font color=\"FF0000\">score:" + eval.getScore() + "</font>" +
					", date:" + eval.getDate() + 
					", correct:" + eval.getCorrect() +
					", incorrect:" + eval.getIncorrect() +
					", without-answer:" + eval.getWithoutAnswer()
					+ "\n");
				html.append("    </li>" + "\n");
			}
			html.append("  </ul>" + "\n");
			html.append("  </td>" + "\n");

			html.append("</tr>" + "\n");
		}
		html.append("</table>");
		html.append("</div>");
		
		return html.toString();
	}
    public static String genAccessLogHtmlReport(String userid, CourseInfo courseInfo, CourseAccessLog courseLog) throws Exception {
    	int found = courseLog.findUserLog(userid);
    	
    	StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		html.append("<a name=\"accesslog_" + userid + "_" + courseInfo.courseName + "\"/>" + "\n");
    	if(found == -1) {
    		html.append("</div>");
    		return html.toString();
    	}
    	
    	UserAccessLog userLog = courseLog.getUserLog(found);
		html.append("<ol>" + "\n");
    	for(int i = 0; i < userLog.size(); i++) {
    		UserAccessSession sess = userLog.getSession(i);
    		html.append("  <li>Session: " + sess.getEarliestAccessDate().toGMTString() + "\n");
    		html.append("    <ul>" + "\n");
    		for(int j = 0; j < sess.size(); j++) {
    			UserAccessRecord record = sess.getRecord(j);
    			ConceptInfo cInfo = null;
    			String info = null;
    			if(record.isLogout())
    				info = "Logout at " + record.getAccessDate();
    			else {
        			cInfo = courseInfo.conceptsInfoTbl.getConceptInfo(record.getConceptName());
        			if(cInfo == null) continue;
        			ConceptNodeTypeWrapper wrapper = new ConceptNodeTypeWrapper(cInfo);
        			if(wrapper.isTestEvidence())
            			info = "Do test \"" + 
            			"<font color=\"0000FF\">" + cInfo.getTitle() + "</font>" + "\" at " + 
            			"<font color=\"FF0000\">" + record.getAccessDate().toGMTString() + "</font>";
        			else
            			info = "Access \"" + 
            			"<font color=\"0000FF\">" + cInfo.getTitle() + "</font>" + "\" at " + 
            			"<font color=\"FF0000\">" + record.getAccessDate().toGMTString() + "</font>";
    			}
    			
    			html.append("      <li>" + info + "</li>");
    			
    		}
    		html.append("    </ul>" + "\n");
    		html.append("  </li>" + "\n");
    	}
		html.append("</ol>" + "\n");
		html.append("</div>");
		
		return html.toString();
    }

    public static String genSearchLogHtmlReport(String userid, String course) throws Exception {
    	SearchLogService service = new SearchLogService();
    	CourseSearchLog courseLog = service.getCourseSearchLog(userid, course, ZebraStatic.MINER_LOG_BEGIN_DATE);

    	StringBuffer html = new StringBuffer();
		html.append("<div>" + "\n");
		html.append("<a name=\"searchlog_" + userid + "_" + course + "\"/>" + "\n");
		if(courseLog == null || courseLog.size() == 0) {
    		html.append("</div>");
    		return html.toString();
		}
		
    	UserSearchLog userLog = courseLog.getUserLog(0);
		html.append("<ol>");
    	for(int i = 0; i < userLog.size(); i++) {
    		UserSearchSession sess = userLog.getSession(i);
    		html.append("  <li>Session: " + sess.getEarliestAccessDate().toGMTString() + "\n");
    		html.append("    <ul>" + "\n");
    		for(int j = 0; j < sess.size(); j++) {
    			UserSearchRecord record = sess.getRecord(j);
    			html.append("      <li>" + "QUERY at " + 
    				"<font color=\"FF0000\">" + record.accessdate.toGMTString() + "</font> is: " +
    				"<font color=\"0000FF\">" + record.query + "</font>" + "</li>");
    		}
    		html.append("    </ul>" + "\n");
    		html.append("  </li>");
    	}
		html.append("</ol>" + "\n");
		
//		DocClassifier classifier = um.getZebraMiner().getCourseDocClassifier(um.getCourse());
//		if(classifier == null)
//			classifier = um.getZebraMiner().addCourseDocClassifier(um.getCourse());
//			
//		SearchUserDoc doc = new SearchUserDoc(um.getUserId(), um.getCourse());
//		doc.load(classifier.getKeywordPattern());
//		html.append("<br/>" + "\n");
//		html.append("<em>User Searching Document:</em>" + "<br/>" + "\n");
//		html.append("<font size=\"-1\" color=\"0000FF\">" +  doc.toString() + "</font>" + "\n");
		
		html.append("</div>");
		return html.toString();
    }
    
    public String genFeedbackHtmlReport() throws Exception {
    	UserFeedback feedback = new UserFeedback(um.getUserId(), um.getCourse());
    	feedback.load();
    	return "<div>" + "\n" + 
    		"<a name=\"feedback_" + um.getUserId() + "_" + um.getCourse() + "\"/>" + "\n" + 
    		feedback.genHTML(false) + "\n" +
    		"</div>";
    }
}
