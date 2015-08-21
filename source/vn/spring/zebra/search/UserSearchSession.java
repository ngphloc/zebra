/**
 * 
 */
package vn.spring.zebra.search;

import java.util.ArrayList;
import java.util.Date;


/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserSearchSession {
	public String userid;
	public String sessionid;
	public ArrayList<UserSearchRecord> userSearchRecords = new ArrayList<UserSearchRecord>();
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		UserSearchSession clone = new UserSearchSession();
		clone.userid = (this.userid == null ? null : new String(this.userid));
		clone.sessionid = (this.sessionid == null ? null : new String(this.sessionid));
		clone.userSearchRecords = new ArrayList<UserSearchRecord>();
		for(UserSearchRecord record : this.userSearchRecords) {
			clone.userSearchRecords.add((UserSearchRecord)record.clone());
		}
		return clone;
	}
	public int                  size() {return userSearchRecords.size();}
	public UserSearchRecord     getRecord(int i) {return userSearchRecords.get(i);}

	public void addRecord(UserSearchRecord record) {userSearchRecords.add(record);}
	
	@Override
	public boolean equals(Object obj) {
		UserSearchSession ses = (UserSearchSession)obj;
		return (ses.userid.equals(this.userid) && ses.sessionid.equals(this.sessionid));
	}
	
	public Date getEarliestAccessDate() {
		if(size() == 0) return new Date();
		Date date = userSearchRecords.get(0).accessdate;
		for(UserSearchRecord record : userSearchRecords) {
			if(record.accessdate.before(date)) date = record.accessdate;
		}
		return date;
	}
}
