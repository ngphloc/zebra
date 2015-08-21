package vn.spring.zebra.log;

import java.util.Date;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserAccessRecord {
	public static enum ACTION {VISIT, LOGOUT}
	
	public  Object     reserved = null;
	
	protected String  userid = null;
	protected String  coursename = null;
	protected String  conceptname = null;
	protected Date    accessdate = null;
	protected String  sessionid = null;
	protected String  resource = null;
	protected boolean isfragment = false;
	protected ACTION  action = ACTION.VISIT;
	
	public String  getUserId()       {return userid;}
	public String  getCourseName()   {return coursename;}
	public String  getConceptName()  {return conceptname;}
	public Date    getAccessDate()   {return accessdate;}
	public String  getSessionId()    {return sessionid;}
	public String  getResource()     {return resource;}
	public boolean getFragment()     {return isfragment;}
	public ACTION  getAction()       {return action;}

	public void    setUserId(String userid)            {this.userid = userid;}
	public void    setCourseName(String coursename)    {this.coursename = coursename;}
	public void    setConceptName(String conceptname)  {this.conceptname = conceptname;}
	public void    setAccessDate(Date accessdate)      {this.accessdate = accessdate;}
	public void    setSessionId(String sessionid)      {this.sessionid = sessionid;}
	public void    setResource(String resource)        {this.resource = resource;}
	public void    setFragment(boolean isfragment)     {this.isfragment = isfragment;}
	public void    setAction(ACTION  action)           {this.action = action;}
	
	public UserAccessRecord() {
		
	}
	public boolean isValid() {
		if(userid == null || conceptname == null || accessdate == null) return false;
		if(userid.length() == 0 || conceptname.length()==0) return false;
		return true;
	}
	public boolean isLogout() {
		return (action == ACTION.LOGOUT);
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		UserAccessRecord clone = new UserAccessRecord();
		clone.reserved = (this.reserved == null ? null : this.reserved);
		
		clone.userid = (this.userid == null ? null : new String(this.userid));
		clone.coursename = (this.coursename == null ? null : new String(this.coursename));
		clone.conceptname = (this.conceptname == null ? null : new String(this.conceptname));
		clone.accessdate = (this.accessdate == null ? null : (Date)this.accessdate.clone());
		clone.sessionid = (this.sessionid == null ? null : new String(this.sessionid));
		clone.resource = (this.resource == null ? null : new String(this.resource));
		clone.isfragment = this.isfragment;
		clone.action = this.action;
		
		return clone;
	}
	
}
