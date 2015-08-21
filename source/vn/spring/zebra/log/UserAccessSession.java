package vn.spring.zebra.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import vn.spring.zebra.exceptions.ZebraException;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserAccessSession {
	public String userid;
	public String sessionid;
	public ArrayList<UserAccessRecord> userAccessRecords = new ArrayList<UserAccessRecord>();
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		UserAccessSession clone = new UserAccessSession();
		clone.userid = (this.userid == null ? null : new String(this.userid));
		clone.sessionid = (this.sessionid == null ? null : new String(this.sessionid));
		clone.userAccessRecords = new ArrayList<UserAccessRecord>();
		for(UserAccessRecord record : this.userAccessRecords) {
			clone.userAccessRecords.add((UserAccessRecord)record.clone());
		}
		return clone;
	}
	public int                  size() {return userAccessRecords.size();}
	public UserAccessRecord     getRecord(int i) {return userAccessRecords.get(i);}
	public UserAccessRecord     getRecord(String concept) {
		for(UserAccessRecord record : userAccessRecords) {
			if(record.conceptname.equals(concept)) return record;
		}
		return null;
	}
	
	public int getRecordIdx(int beginIdx, String concept) {
		for(int i = beginIdx; i < userAccessRecords.size(); i++) {
			UserAccessRecord record = userAccessRecords.get(i);
			if(record.conceptname.equals(concept)) return i;
		}
		return -1;
	}
	
	public Date getLastestAccessDate() {
		if(size() == 0) return new Date();
		Date date = userAccessRecords.get(0).getAccessDate();
		for(UserAccessRecord record : userAccessRecords) {
			if(record.getAccessDate().after(date)) date = record.getAccessDate();
		}
		return date;
	}
	public Date getEarliestAccessDate() {
		if(size() == 0) return new Date();
		Date date = userAccessRecords.get(0).getAccessDate();
		for(UserAccessRecord record : userAccessRecords) {
			if(record.getAccessDate().before(date)) date = record.getAccessDate();
		}
		return date;
	}
	
	public ArrayList<String> getConceptList(String course) {
		ArrayList<String> cList = new ArrayList<String>();
		for(UserAccessRecord record : userAccessRecords) {
			if(!record.coursename.equals(course)) continue;
			if(cList.indexOf(record.conceptname) != -1) continue;
			cList.add(record.conceptname);
		}
		return cList;
	}
	public ArrayList<String> getConceptList() {
		ArrayList<String> cList = new ArrayList<String>();
		for(UserAccessRecord record : userAccessRecords) {
			if(cList.indexOf(record.conceptname) != -1) continue;
			cList.add(record.conceptname);
		}
		return cList;
	}
	
	public ArrayList<TimeIntervalConcept> getTimeIntervalSortedConceptList() {
		ArrayList<TimeIntervalConcept> cList = new ArrayList<TimeIntervalConcept>();
		for(UserAccessRecord record : userAccessRecords) {
			if(record.isLogout()) continue;
			
			boolean found = false;
			for(int i = 0; i < cList.size(); i++) {
				if(cList.get(i).concept.equals(record.conceptname)) {
					found = true;
					break;
				}
			}
			if(found) continue;
			try {
				TimeIntervalConcept tiConcept = 
					new TimeIntervalConcept(record.conceptname, numberOfTimeSpend(record.conceptname));
				if(tiConcept != null) cList.add(tiConcept);
			}
			catch(Exception e) {e.printStackTrace();}
		}
		Collections.sort(cList, new Comparator<TimeIntervalConcept>() {
			public int compare(TimeIntervalConcept o1, TimeIntervalConcept o2) {
				if(o1.timeInterval < o2.timeInterval)
					return 1;
				else if(o1.timeInterval == o2.timeInterval)
					return 0;
				else
					return -1;
			}
			
		});
		return cList;
	}
	public long numberOfTimeSpend(String concept) throws ZebraException {// return milisecond
		return numberOfTimeSpend(0, concept, 0);
	}
	public long numberOfTimeSpendCourse(String course) throws ZebraException {// return milisecond
		ArrayList<String> cList = getConceptList(course);
		long interval = 0;
		for(String concept : cList) {
			interval += numberOfTimeSpend(concept);
		}
		return interval;
	}
	public long numberOfTimeSpend() throws ZebraException {// return milisecond
		ArrayList<String> cList = getConceptList();
		long interval = 0;
		for(String concept : cList) {
			interval += numberOfTimeSpend(concept);
		}
		return interval;
	}
	private long numberOfTimeSpend(int beginIdx, String concept, long old_interval) throws ZebraException {// return milisecond
		int idx = getRecordIdx(beginIdx, concept);
		if(idx == -1 || idx == userAccessRecords.size() - 1) return old_interval;
		
		UserAccessRecord next = null;
		int i = idx + 1;
		for(; i < userAccessRecords.size(); i++) {
			UserAccessRecord record = userAccessRecords.get(i);
			
			if(record.conceptname.endsWith("|logout") || record.isLogout()) {
				next = record;
				break;
			}
			else if(i == userAccessRecords.size() - 1 && record.conceptname.equals(concept)) {
				next = record;
				break;
			}
			else if(!record.conceptname.equals(concept)) {
				next = record;
				break;
			}
		}
		if(next == null) return old_interval;
		
		UserAccessRecord cur = getRecord(idx);
		long interval = next.getAccessDate().getTime() - cur.getAccessDate().getTime();
		if(interval < 0) throw new ZebraException("The interval of time spend can't be negative!");
		old_interval += interval;
		
		return numberOfTimeSpend(i+1, concept, old_interval);
	}
	public class TimeIntervalConcept {
		public String concept = null;
		public long   timeInterval = 0;
		public TimeIntervalConcept(String concept, long timeInterval) {
			this.concept = concept;
			this.timeInterval = timeInterval;
		}
	}
}
