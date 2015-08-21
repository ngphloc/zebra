package vn.spring.zebra.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import vn.spring.zebra.exceptions.ZebraException;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserAccessLog {
	public String userid = null;
	public ArrayList<UserAccessSession> userAccessSessions = new ArrayList<UserAccessSession>();

	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		UserAccessLog clone = new UserAccessLog();
		clone.userid = (this.userid == null ? null : new String(this.userid));
		clone.userAccessSessions = new ArrayList<UserAccessSession>();
		for(UserAccessSession session : this.userAccessSessions) {
			clone.userAccessSessions.add((UserAccessSession)session.clone());
		}
		
		return clone;
	}
	
	public int               size() {if(userAccessSessions==null) return 0; return userAccessSessions.size();}
	public UserAccessSession getSession(int i) {return userAccessSessions.get(i);}

	public int findUserSession(String sessionid) {
		int n = size();
		for (int i=0; i < n; i++) {
			UserAccessSession us = getSession(i);
			if(us.sessionid.equals(sessionid)) return i;
		}
		return -1;
	}
	public Date getLastestAccessDate() {
		if(size() == 0) return new Date();
		
		Date date = getSession(0).getLastestAccessDate();
		for(UserAccessSession session : userAccessSessions) {
			if(session.getLastestAccessDate().after(date)) date = session.getLastestAccessDate();
		}
		return date;
	}
	public Date getEarliestAccessDate() {
		if(size() == 0) return new Date();
		Date date = getSession(0).getEarliestAccessDate();
		for(UserAccessSession session : userAccessSessions) {
			if(session.getEarliestAccessDate().before(date)) date = session.getEarliestAccessDate();
		}
		return date;
	}
	public ArrayList<String> getConceptList(String course) {
		ArrayList<String> cList = new ArrayList<String>();
		HashSet<String> set = new HashSet<String>();
		for(UserAccessSession session : userAccessSessions) {
			set.addAll(session.getConceptList(course));
		}
		cList.addAll(set);
		return cList;
	}
	public ArrayList<String> getConceptList() {
		ArrayList<String> cList = new ArrayList<String>();
		HashSet<String> set = new HashSet<String>();
		for(UserAccessSession session : userAccessSessions) {
			set.addAll(session.getConceptList());
		}
		cList.addAll(set);
		return cList;
	}
	public long numberOfTimeSpend(String concept) throws ZebraException {// return milisecond
		long interval = 0;
		for(UserAccessSession session : userAccessSessions) {
			interval += session.numberOfTimeSpend(concept);
		}
		return interval;
	}
	public long numberOfTimeSpendCourse(String course) throws ZebraException {// return milisecond
		long interval = 0;
		for(UserAccessSession session : userAccessSessions) {
			interval += session.numberOfTimeSpendCourse(course);
		}
		return interval;
	}
	public long numberOfTimeSpend() throws ZebraException {// return milisecond
		long interval = 0;
		for(UserAccessSession session : userAccessSessions) {
			interval += session.numberOfTimeSpend();
		}
		return interval;
	}
}
