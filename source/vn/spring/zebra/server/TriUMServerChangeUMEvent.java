/**
 * 
 */
package vn.spring.zebra.server;

import java.util.EventObject;

import vn.spring.zebra.um.TriUM;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServerChangeUMEvent extends EventObject {
    private static final long serialVersionUID = 5516075349620653480L;
    
    public enum CHANGE_TYPE {REGISTER, REGISTERDEFAULT, UNREGISTER, UNREGISTERALL, MONITOR, MONITORCOURSE, UNMONITOR, UNMONITORCOURSE, UNMONITORALL}
    
    protected TriUM um = null;
    protected CHANGE_TYPE changeType;
    protected String userid = null;
    protected String course = null;
    protected Object obj = null;
    
    public TriUMServerChangeUMEvent(TriUMServer server, TriUM um, CHANGE_TYPE changeType) {
    	super(server);
    	this.um = um;
    	this.changeType = changeType;
    	this.userid = um.getUserId();
    	this.course = um.getCourse();
    }
    public TriUMServerChangeUMEvent(TriUMServer server, String userid, String course, CHANGE_TYPE changeType) {
    	super(server);
    	this.um = null;
    	this.changeType = changeType;
    	this.userid = userid;
    	this.course = course;
    }
    public TriUM getTriUM()    {return um;}
    public String getUserId()  {return userid;}
    public String getCourse()  {if(course == null) return um.getCourse(); return course;}
    public Object getObject()  {return obj;}
}
