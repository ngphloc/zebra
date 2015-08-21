/**
 * 
 */
package vn.spring.zebra.search;

import java.util.ArrayList;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserSearchLog {
	public String userid = null;
	public ArrayList<UserSearchSession> userSearchSessions = new ArrayList<UserSearchSession>();

	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		UserSearchLog clone = new UserSearchLog();
		clone.userid = (this.userid == null ? null : new String(this.userid));
		clone.userSearchSessions = new ArrayList<UserSearchSession>();
		for(UserSearchSession session : this.userSearchSessions) {
			clone.userSearchSessions.add((UserSearchSession)session.clone());
		}
		
		return clone;
	}
	
	public int               size() {if(userSearchSessions==null) return 0; return userSearchSessions.size();}
	public UserSearchSession getSession(int i) {return userSearchSessions.get(i);}

	public int findUserSession(String sessionid) {
		int n = size();
		for (int i=0; i < n; i++) {
			UserSearchSession us = getSession(i);
			if(us.sessionid.equals(sessionid)) return i;
		}
		return -1;
	}
	public void addSeesion(UserSearchSession ses) {
		if(userSearchSessions.contains(ses)) return;
		userSearchSessions.add(ses);
	}

}
