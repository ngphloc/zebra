/**
 * 
 */
package vn.spring.zebra.mining;

import java.util.EventObject;

import org.w3c.dom.Element;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class LearningHistoryDataEvent extends EventObject {
    private static final long serialVersionUID = 5516075349620653480L;
    public enum EVENT_TYPE {UPDATE}

    private Element courseData = null;
    private Element userData = null;
    private EVENT_TYPE eventType = EVENT_TYPE.UPDATE;
    
    public LearningHistoryDataEvent(LearningHistoryData hisData, Element courseData, Element userData, EVENT_TYPE eventType) {
    	super(hisData);
    	this.courseData = courseData;
    	this.userData = userData;
    	this.eventType = eventType;
    }
    public Element getUserData() {return userData;}
    public Element getCourseData() {return courseData;}
    public EVENT_TYPE getEventType() {return eventType;}
}
