/**
 * 
 */
package vn.spring.zebra.feedback;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Map;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import vn.spring.WOW.Utils.CgiUtil;
import vn.spring.WOW.datacomponents.Resource;
import vn.spring.WOW.datacomponents.ResourceType;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.util.CommonUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserFeedback {
	protected String userid = null;
	protected String course = null;
	protected ArrayList<QA> qaList = new ArrayList<QA>();
	
	public UserFeedback(String userid, String course) throws ZebraException {
		if(userid == null || userid.length() == 0 || course == null || course.length() == 0) throw new ZebraException("Invalid parameters");
		this.userid = userid;
		this.course = course;
		this.qaList = new ArrayList<QA>();
	}

	public UserFeedback(String userid, String course, Collection<QA> qaList) throws ZebraException {
		this(userid, course);
		for(QA qa : qaList) {
			if(!this.qaList.contains(qa)) {
				qa.resetAnswer();
				this.qaList.add(qa);
			}
		}
	}
	
	public QA getQAbyIndex(int index) {
		return qaList.get(index);
	}
	public QA getQAbyID(int ID) {
		for(QA qa : qaList) {
			if(qa.ID == ID) return qa;
		}
		return null;
	}
	public int  getNumberQA() {return qaList.size();}
	public void addQA(QA qa) {qaList.add(qa);}
	public void removeQA(int qaID) {
		QA qa = getQAbyID(qaID);
		if(qa != null) qaList.remove(qa);
	}
	protected ArrayList<QA> getQAList() {return qaList;}
	
	public void resetAllAnswers() {
		for(QA qa : qaList) qa.resetAnswer();
	}
	
	@Override
	public String toString() {
		StringBuffer xml = new StringBuffer();
		xml.append("<feedback userid=\"" + userid + "\">" + "\n");
		xml.append("<course name=\"" + course + "\">");
		for(int i = 0; i < qaList.size(); i++) {
			xml.append(qaList.get(i).toString() + "\n");
		}
		xml.append("</course>");
		xml.append("</feedback>" + "\n");
		return xml.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return userid == ((UserFeedback)obj).userid;
	}

	public void saveXML() throws Exception {
		File feedbackDir = new File(ZebraStatic.getWowXmlRoot() + ZebraStatic.FEEDBACK_DIR);
		if(!feedbackDir.exists()) feedbackDir.mkdir();
		
		File file = new File(feedbackDir, "feedback_" + userid + ".xml");
        
		Document doc = null;
		if(!file.exists()) {
	        doc = new DocumentImpl();

	        Element newfeedback = doc.createElement("feedback");
	        newfeedback.setAttribute("userid", userid);
	        newfeedback.appendChild(createCourseElement(doc));

	        doc.appendChild(newfeedback);
		}
		else {
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(new FileReader(file)));
			doc = parser.getDocument();
			Element feedback = null;
			Node node = doc.getFirstChild();
			while(node != null) {
				if(node.getNodeName().equals("feedback")) {
					feedback = (Element)node;
					break;
				}
				node = node.getNextSibling();
			}
			
			Element oldCourseElement = getCourseElement(doc);
			Element newCourseElement = createCourseElement(doc);
			if(oldCourseElement == null)
				feedback.appendChild(newCourseElement);
			else 
				feedback.replaceChild(newCourseElement, oldCourseElement);
			
		}
		FileWriter writer = new FileWriter(file);
		writer.write(CommonUtil.serializeXML(doc.getDocumentElement()));
		writer.flush();
		writer.close();
	}
	public void loadXML() throws ZebraException {
		try {
			qaList.clear();
			File file = new File(ZebraStatic.root, "feedback/feedback_" + userid + ".xml");
			if(!file.exists()) return;
			loadXML(new FileReader(file));
		}
		catch(Exception e) {throw new ZebraException(e.getMessage());}
	}
	
	public void loadDefaultCVS() throws ZebraException {
		qaList.clear();
		File file = new File(ZebraStatic.getWowXmlRoot() + ZebraStatic.FEEDBACK_DIR + ZebraStatic.FEEDBACK_QA_CSVFILENAME);
		if(!file.exists()) return;
		try {
			loadDefaultCVS(new FileReader(file));
		}
		catch(FileNotFoundException e) {throw new ZebraException(e.getMessage());}
	}
	public void load() throws ZebraException {
		File xmlfile = new File(ZebraStatic.root, "feedback/feedback_" + userid + ".xml");
		if(!xmlfile.exists()) {
			loadDefaultCVS();
			return;
		}
		
		File cvsfile = new File(ZebraStatic.getWowXmlRoot() + ZebraStatic.FEEDBACK_DIR + ZebraStatic.FEEDBACK_QA_CSVFILENAME);
		if(!cvsfile.exists()) {
			loadXML();
			return;
		}
		
		if(xmlfile.lastModified() > cvsfile.lastModified())
			loadXML();
		else {
			loadXML();
			UserFeedback tempFeedback = new UserFeedback(this.userid, this.course);
			tempFeedback.loadDefaultCVS();
			for(int i = 0; i < this.qaList.size(); i++) {
				QA thisQA = this.qaList.get(i);
				QA tempQA = tempFeedback.getQAbyID(thisQA.ID);
				if(tempQA == null) {
					this.qaList.remove(i);
					i--;
				}
			}
			
			for(QA tempQA : tempFeedback.qaList) {
				QA thisQA = this.getQAbyID(tempQA.ID);
				if(thisQA == null)
					this.addQA(tempQA);
				else {
					thisQA.copy(tempQA);
				}
			}//end for
			sortQAList();
		}
	}
	
	public void receiveAnswer(ArrayList<FeedbackAnswer> answers) {
		for(FeedbackAnswer answer : answers) {
			QA qa = getQAbyID(answer.qaID);
			try {
				if(qa != null) qa.setAnswer(answer.answerIdxes);
			}
			catch(ZebraException e) {e.printStackTrace();}
		}
	}

	public Resource service(Map params) throws Exception {
		load();

		CgiUtil util =new CgiUtil(params);
        Enumeration enums = util.keys();
        ArrayList<FeedbackAnswer> answers = new ArrayList<FeedbackAnswer>();
        String userid = null;
        while(enums.hasMoreElements()) {
        	String paramname = (String)(enums.nextElement());
        	if(paramname.equals("userid"))
        		userid = (String)(util.get(paramname));
        	else {
            	int id = QA.getQAIDFromElementName(paramname);
            	if(id == -1) continue;
            	QA qa = getQAbyID(id);
            	if(qa == null) continue;
        		answers.add(new FeedbackAnswer(qa, util));
        	}
        }
        
		StringBuffer html = new StringBuffer();
		html.append("<html><body>" + "\n");
		html.append("<h2><center>FEEDBACK IN COURSE \"" + course + "\"</center></h2>" + "\n");
		
		if(userid != null && userid.equals(this.userid)) {
			html.append("<h3><center>You have already feedbacked sucessfully!</center></h3>" + "\n");
			receiveAnswer(answers);
			saveXML();
		}
		else {
			html.append("<h3><center>Please answer following questions in order to finish your feedback</center></h3>" + "\n");
		}
		html.append(genHTML("?handler=UserFeedback"));
		html.append("</html><body>" + "\n");

        InputStream insb = new ByteArrayInputStream(html.toString().getBytes());
        return new Resource(insb, new ResourceType("text/html"));
	}

	public String genHTML(boolean enable) {
		String html = "";
		html += "<table border=\"2\" cellspacing=\"4\" cellpadding=\"4\">" + "\n";
		html += "<tr><th>ID</th><th>Question</th><th>Answer</th></tr>" + "\n";
		for(int i = 0; i < qaList.size(); i++)
			html += qaList.get(i).genHTML(enable) + "\n";
		html += "</table>";
		return html;
	}
	private String genHTML(String action) {
		String toolbar = 
			"<table border=\"0\">" + "\n" +
			"<tr>" + "\n" +
			"<td><input name=\"answer\" type=\"submit\" value=\"Answer\"></td>" + "\n" +
			"<td><input name=\"reset\" type=\"reset\" value=\"Reset\"></td>" + "\n" +
			"</tr>" + "\n" +
			"</table>";
			
		String html = "";
		html += "<form name=\"userfeedback\" action=\"" + action + "\" method=\"POST\">" + "\n";
		html += toolbar + "\n";
		html += "<input name=\"userid\" type=\"hidden\" value=\"" + userid + "\">" + "\n";
		html += genHTML(true) + "\n";
		html += toolbar + "\n";
		html += "</form>";
		return html;
	}
	private Element createCourseElement(Document doc) {
		Element  courseElement = doc.createElement("course");
		courseElement.setAttribute("name", course);
		for(int i = 0; i < qaList.size(); i++) {
			courseElement.appendChild(qaList.get(i).toElement(doc));
		}
		return courseElement;
	}
	private Element getCourseElement(Document doc) {
		Element  userElement = doc.getDocumentElement();
		if(userElement == null) return null;
		
		Element  courseElement = null;
		NodeList courseList = userElement.getChildNodes();
		for(int i = 0; i < courseList.getLength(); i++) {
			String nodename = courseList.item(i).getNodeName();
			if(!nodename.equals("course")) continue;
			Element tempCourse = (Element)courseList.item(i);
			if(tempCourse.getAttribute("name").equals(course)) {
				courseElement = tempCourse;
				break;
			}
		}
		return courseElement;
	}
	private void loadXML(Document doc) throws ZebraException {
		qaList.clear();
    	try {
			Element  userElement = doc.getDocumentElement();
    		userid = userElement.getAttribute("userid");
    		Element  courseElement = getCourseElement(doc);
    		if(courseElement == null) {
    			loadDefaultCVS();
    			return;
    		}
    		
    		NodeList optionList = courseElement.getChildNodes();
    		for(int i = 0; i < optionList.getLength(); i++) {
    			String nodename = optionList.item(i).getNodeName();
    			if(!nodename.equals("qa")) continue;
    			
    			Element qaElement = (Element)optionList.item(i);
    			try {
    				QA qa = new QA(qaElement);
    				qaList.add(qa);
    			}
    			catch(Exception e) {e.printStackTrace();}
    		}
    	}
    	catch(Exception e) {throw new ZebraException(e.getMessage());}
    	if(userid == null) throw new ZebraException("Error xml");
		sortQAList();
	}
	protected void loadXML(Reader reader) throws ZebraException {
		try {
			DOMParser parser = new DOMParser();
			parser.parse(new InputSource(reader));
			Document doc = parser.getDocument();
			loadXML(doc);
		}
		catch(Exception e) {throw new ZebraException(e.getMessage());}
	}
	private void loadDefaultCVS(Reader reader) throws ZebraException {
		qaList.clear();
		BufferedReader buffReader = new BufferedReader(reader);
		String cvsLine = null;
		while(true) {
			try {
				cvsLine = buffReader.readLine();
				if(cvsLine == null) break;
				cvsLine = cvsLine.trim(); if(cvsLine.length() == 0) continue;
				
				if(course.toLowerCase().equals("temp")) {
					if(cvsLine.startsWith("[") && cvsLine.endsWith("]")) break;
				}
				else if(cvsLine.toLowerCase().equals("[" + course.toLowerCase() + "]"))
					break;
			}
			catch(Exception e) {e.printStackTrace(); cvsLine = null;}
		}
		if(cvsLine == null) return;
		
		while(true) {
			try {
				cvsLine = buffReader.readLine();
				if(cvsLine == null || cvsLine.startsWith("[")) break;
				cvsLine = cvsLine.trim(); if(cvsLine.length() == 0) continue;
				
				QA qa = new QA(cvsLine);
				qaList.add(qa);
			}
			catch(Exception e) {e.printStackTrace(); cvsLine = null;}
		}
		sortQAList();
	}
	private void sortQAList() {
		Collections.sort(qaList, new Comparator<QA>() {
			public int compare(QA qa1, QA qa2) {
				if(qa1.ID < qa2.ID)       return -1;
				else if(qa1.ID == qa2.ID) return 0;
				else                      return 1;
			}
		});
	}
}

class FeedbackAnswer {
	public int qaID;
	public int[] answerIdxes;
	public FeedbackAnswer(int qaID, int[] answerIdxes) {
		this.qaID = qaID;
		this.answerIdxes = answerIdxes;
	}
	public FeedbackAnswer(QA qa, CgiUtil util) throws ZebraException {
		this.qaID = qa.ID;
		this.answerIdxes = new int[qa.answerIdxes.length];
		for(int i = 0; i < this.answerIdxes.length; i++) this.answerIdxes[i] = 0;
		
        Enumeration enums = util.keys();
        ArrayList<String> names = new ArrayList<String>();
        while(enums.hasMoreElements()) {
        	String name = (String)(enums.nextElement());
        	int id = QA.getQAIDFromElementName(name);
        	if(id != -1 && id == qaID) {
        		names.add(name);
        	}
        }
        
        if(names.size() > this.answerIdxes.length) throw new ZebraException("Error parameters");
        if(names.size() != 1 && !qa.multichoice) throw new ZebraException("Error parameters");
        for(String name : names) {
    		String value = (String)util.get(name);
			int idx = Integer.parseInt((String)value);
			this.answerIdxes[idx] = 1;
        }
	}
}
