/**
 * 
 */
package vn.spring.zebra.mining;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.log.CourseAccessLog;
import vn.spring.zebra.mining.LearningHistoryDataEvent.EVENT_TYPE;
import vn.spring.zebra.util.xmlviewer.DOMUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class LearningHistoryData {
	protected String userid = null;
	protected String course = null;
	
	protected CourseAccessLog log = null;
	protected Element courseData = null;
	
	protected HashSet<LearningHistoryDataListener> listeners = new HashSet<LearningHistoryDataListener>();
	
	public LearningHistoryData() {}
	
	public synchronized void setParameters(String userid, String course) {
		this.userid = userid;
		this.course = course;
	}
	public synchronized void update() throws Exception {
		log = ZebraStatic.getLogDB().getCourseAccessLog(this.course, ZebraStatic.MINER_LOG_BEGIN_DATE, ZebraStatic.IGNORE_ANONYMOUS);
    	if(log.size() > 0) {
	    	StringWriter strWriter = new StringWriter();
	    	log.exportXML(strWriter, true);
	    	strWriter.close();
	    	InputSource input = new InputSource(new StringReader(strWriter.getBuffer().toString()));
	    	courseData = DOMUtil.createDocument2(input).getDocumentElement();
	
    	}
    	new FireEvent(this, courseData, EVENT_TYPE.UPDATE);
	}
	
	public String getUserId() {return userid;}
	public String getCourse() {return course;}
	
	public synchronized CourseAccessLog getLog() {return log;}
	public static Element getUserData(String userid, Element courseData) {
		if(courseData == null) return null;
    	for(int i = 0; i < courseData.getChildNodes().getLength(); i++) {
    		Node node = courseData.getChildNodes().item(i);
    		if(node.getNodeType() != Node.ELEMENT_NODE || !node.getNodeName().equals("user"))
    			continue;
    		
        	String id = node.getAttributes().getNamedItem("id").getNodeValue();
    		if(id.equals(userid)) {
    			return (Element)node;
    		}
    	}
    	return null;
	}
	public synchronized Element getCourseData()  {return courseData;}

	public synchronized void addListener(LearningHistoryDataListener listener) {
		listeners.add(listener);
	}
	public synchronized void removeListener(LearningHistoryDataListener listener) {
		listeners.remove(listener);
	}
	
	private class FireEvent extends Thread {
		private LearningHistoryData his = null;
		private Element courseData = null;
		private EVENT_TYPE eventType = EVENT_TYPE.UPDATE;
		
		public FireEvent(LearningHistoryData his, Element courseData, EVENT_TYPE eventType) {
			super(); 
			this.his = his;
			this.courseData = courseData;
			this.eventType = eventType;
			start();
		}
		
		@Override
		public void run() {
			for(LearningHistoryDataListener listener : listeners) {
				try {
					Element userData = getUserData(listener.getUserId(), courseData);
					LearningHistoryDataEvent evt = new LearningHistoryDataEvent(his, courseData, userData, eventType);
					switch(eventType) {
					case UPDATE:
						listener.dataUpdated(evt);
					}
				}
				catch(Exception e) {
					System.out.println("LearningHistoryData.fireChangeEvent causes error: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

}
