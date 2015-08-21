/**
 * 
 */
package vn.spring.zebra.search;

import java.util.Date;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserSearchRecord {

	public String  userid = null;
	public Date    accessdate = null;
	public String  sessionid = null;
	public String  course = null;
	public String  query = null;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		UserSearchRecord clone = new UserSearchRecord();
		
		clone.userid = (this.userid == null ? null : new String(this.userid));
		clone.course = (this.course == null ? null : new String(this.course));
		clone.accessdate = (this.accessdate == null ? null : (Date)this.accessdate.clone());
		clone.sessionid = (this.sessionid == null ? null : new String(this.sessionid));
		clone.query = (this.query == null ? null : new String(this.query));
		
		return clone;
	}
	public boolean isValid() {
		if(userid == null || userid.length() == 0) return false;
		if(accessdate == null) return false;
		if(sessionid == null || sessionid.length() == 0) return false;
		if(course == null || course.length() == 0) return false;
		if(query == null || query.length() == 0) return false;
		return true;
	}
	
}
