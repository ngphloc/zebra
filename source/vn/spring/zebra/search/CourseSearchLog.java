/**
 * 
 */
package vn.spring.zebra.search;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseSearchLog {
	public String                      course = null;
	public ArrayList<UserSearchLog>    userSearchLogs = new ArrayList<UserSearchLog>();
	public Hashtable<String, String>   attributes = new Hashtable<String, String>();

	
	@Override
	public Object clone() throws CloneNotSupportedException {
		CourseSearchLog clone = new CourseSearchLog();
		clone.course = (this.course == null ? null : new String(this.course));
		clone.userSearchLogs = new ArrayList<UserSearchLog>();
		for(UserSearchLog userSearchLog : this.userSearchLogs) {
			clone.userSearchLogs.add((UserSearchLog)userSearchLog.clone());
		}
		clone.attributes = (Hashtable<String, String>)this.attributes.clone();
		return clone;
	}
	
	public int            size()            {if(userSearchLogs==null) return 0; return userSearchLogs.size();}
	public UserSearchLog  getUserLog(int i) {return userSearchLogs.get(i);}
	
	public int            findUserLog(String userid) {
		int n = size();
		for (int i=0; i < n; i++) {
			UserSearchLog ul = getUserLog(i);
			if(ul.userid.equals(userid)) return i;
		}
		return -1;
	}
	
	public String getAttribute(String att_name) {
		return attributes.get(att_name);
	}
	
	void sort() {
		Collections.sort(userSearchLogs, 
				new Comparator() {
					public int compare(Object o1, Object o2) {
						UserSearchLog ulog1 = (UserSearchLog)o1;
						UserSearchLog ulog2 = (UserSearchLog)o2;
						return ulog1.userid.compareTo(ulog2.userid);
					}
				}
			);

		int m = size();
		for(int i = 0; i<m; i++) {
			UserSearchLog ulog = getUserLog(i);
			Collections.sort(ulog.userSearchSessions, 
					new Comparator() {
						public int compare(Object o1, Object o2) {
							UserSearchSession session1 = (UserSearchSession)o1;
							UserSearchSession session2 = (UserSearchSession)o2;
							return session1.sessionid.compareTo(session2.sessionid);
						}
					}
				);

			int n = ulog.size();
			for(int j=0; j<n; j++) {
				UserSearchSession usession = ulog.getSession(j);
				
				Collections.sort(usession.userSearchRecords, 
						new Comparator() {
							public int compare(Object o1, Object o2) {
								UserSearchRecord record1 = (UserSearchRecord)o1;
								UserSearchRecord record2 = (UserSearchRecord)o2;
								return record1.course.compareTo(record2.course);
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
	        //pw.println("<!DOCTYPE log SYSTEM 'coursesearchlog.dtd'>\n");
		}
		
		pw.println("<course name=\"" + course + "\">\n");
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
		
		int  n = userSearchLogs.size();
		for (int i=0; i < n; i++) {
			UserSearchLog ualog = getUserLog(i);
    		pw.println("  <user id=\"" + ualog.userid + "\">");
    		
    		int m = ualog.size();
			for (int j=0; j < m; j++) {
				UserSearchSession uasession = ualog.getSession(j);
	    		pw.println("    <session id=\"" + uasession.sessionid + "\">");
	    		int h = uasession.size();
	    		for(int l=0; l < h ; l++) {
	    			UserSearchRecord uarecord = uasession.getRecord(l);
		    		pw.println("      <record>");
		    		pw.println("        <accessdate>" + uarecord.accessdate + "</accessdate>");
		    		pw.println("        <query>" + uarecord.query + "</query>");
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
		catch(IOException e) {System.out.println("CourseSearchLog.toString causes error: " + e.getMessage());}
		return sw.getBuffer().toString();
	}

}
