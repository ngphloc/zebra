/**
 * 
 */
package vn.spring.zebra.server;

import java.util.EventObject;


/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServerUserPaneSelectionEvent extends EventObject {
    private static final long serialVersionUID = 5516075349620653480L;

    public enum CHANGE_TYPE {ADD, CLEAR, RELOAD}
    
    protected String userid = null;
    protected String course;
    
    public TriUMServerUserPaneSelectionEvent(TriUMServerUIUserPane usersPane, String userid, String course) {
    	super(usersPane);
    	this.userid = userid;
    	this.course = course;
    }
    public String getUserId() {return userid;}
    public String getCourse() {return course;}

}
