package vn.spring.zebra.log;

import java.io.*;
import java.util.*;

import vn.spring.zebra.exceptions.ZebraException;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseAccessLog {
	public String                      coursename = null;
	public ArrayList<UserAccessLog>    userAccessLogs = new ArrayList<UserAccessLog>();
	public Hashtable<String, String>   attributes = new Hashtable<String, String>();

	
	@Override
	public Object clone() throws CloneNotSupportedException {
		CourseAccessLog clone = new CourseAccessLog();
		clone.coursename = (this.coursename == null ? null : new String(this.coursename));
		clone.userAccessLogs = new ArrayList<UserAccessLog>();
		for(UserAccessLog userAccessLog : this.userAccessLogs) {
			clone.userAccessLogs.add((UserAccessLog)userAccessLog.clone());
		}
		clone.attributes = (Hashtable<String, String>)this.attributes.clone();
		return clone;
	}
	
	public int            size()            {if(userAccessLogs==null) return 0; return userAccessLogs.size();}
	public UserAccessLog  getUserLog(int i) {return userAccessLogs.get(i);}
	
	public int            findUserLog(String userid) {
		int n = size();
		for (int i=0; i < n; i++) {
			UserAccessLog ul = getUserLog(i);
			if(ul.userid.equals(userid)) return i;
		}
		return -1;
	}
	
	public String getAttribute(String att_name) {
		return attributes.get(att_name);
	}
	
	void sort() {
		Collections.sort(userAccessLogs, 
				new Comparator() {
					public int compare(Object o1, Object o2) {
						UserAccessLog ulog1 = (UserAccessLog)o1;
						UserAccessLog ulog2 = (UserAccessLog)o2;
						return ulog1.userid.compareTo(ulog2.userid);
					}
				}
			);

		int m = size();
		for(int i = 0; i<m; i++) {
			UserAccessLog ulog = getUserLog(i);
			Collections.sort(ulog.userAccessSessions, 
					new Comparator() {
						public int compare(Object o1, Object o2) {
							UserAccessSession session1 = (UserAccessSession)o1;
							UserAccessSession session2 = (UserAccessSession)o2;
							return session1.sessionid.compareTo(session2.sessionid);
						}
					}
				);

			int n = ulog.size();
			for(int j=0; j<n; j++) {
				UserAccessSession usession = ulog.getSession(j);
				
				Collections.sort(usession.userAccessRecords, 
						new Comparator() {
							public int compare(Object o1, Object o2) {
								UserAccessRecord record1 = (UserAccessRecord)o1;
								UserAccessRecord record2 = (UserAccessRecord)o2;
								return record1.getCourseName().compareTo(record2.getCourseName());
							}
						}
					);
			}
		}
	}
	public void exportXML(String xmlfilepath, boolean isShowDTD) {
		StringWriter string_writer = new StringWriter();
		exportXML(string_writer, isShowDTD);
		
		try {
			FileWriter fw = new FileWriter(xmlfilepath);
			fw.write(string_writer.toString());
			fw.flush();
			fw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void exportXML(StringWriter string_writer, boolean isShowDTD) {
		PrintWriter pw = new PrintWriter(string_writer);
		if(isShowDTD) {
			pw.println("<?xml version=\"1.0\"?>\n\n");
	        //pw.println("<!DOCTYPE log SYSTEM 'courseaccesslog.dtd'>\n");
		}
		
		pw.println("<course name=\"" + coursename + "\">\n");
		pw.println("  <attributes>");
		if(attributes != null) {
    		Enumeration<String> keys = attributes.keys();
    		while(keys.hasMoreElements()) {
    			String att_name = keys.nextElement();
    			String att_value = attributes.get(att_name);
        		pw.println("    <attribute name=\"" + att_name + "\">" + att_value + "</attribute>");
    			
    		}
		}
		pw.println("  </attributes>\n");
		
		int  n = userAccessLogs.size();
		for (int i=0; i < n; i++) {
			UserAccessLog ualog = getUserLog(i);
    		pw.println("  <user id=\"" + ualog.userid + "\">");
    		
    		int m = ualog.size();
			for (int j=0; j < m; j++) {
				UserAccessSession uasession = ualog.getSession(j);
	    		pw.println("    <session id=\"" + uasession.sessionid + "\">");
	    		int h = uasession.size();
	    		for(int l=0; l < h ; l++) {
	    			UserAccessRecord uarecord = uasession.getRecord(l);
		    		pw.println("      <record>");
		    		pw.println("        <conceptname>" + uarecord.getConceptName() + "</conceptname>");
		    		pw.println("        <accessdate>" + uarecord.getAccessDate() + "</accessdate>");
		    		pw.println("        <resource>" + uarecord.getResource() + "</resource>");
		    		pw.println("        <fragment>" + String.valueOf(uarecord.getFragment()) + "</fragment>");
		    		pw.println("      </record>");
	    		}
	    		pw.println("    </session>");
				
			}
    		
    		pw.println("  </user>\n");
			
		}
		pw.println("</course>");
		pw.flush();
		pw.close();
	}
	public String toString() {
		StringWriter sw = new StringWriter();
		exportXML(sw, true);
		try {
			sw.close();
		}
		catch(IOException e) {System.out.println("CourseAccessLog.toString causes error: " + e.getMessage());}
		return sw.getBuffer().toString();
	}
	public long numberOfTimeSpend(String concept) throws ZebraException {
		long interval = 0;
		for(UserAccessLog log : userAccessLogs) {
			interval += log.numberOfTimeSpend(concept);
		}
		return interval;
	}
	public long numberOfTimeSpend() throws ZebraException {
		long interval = 0;
		for(UserAccessLog log : userAccessLogs) {
			interval += log.numberOfTimeSpend();
		}
		return interval;
	}
}
