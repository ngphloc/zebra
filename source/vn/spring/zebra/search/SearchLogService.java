/**
 * 
 */
package vn.spring.zebra.search;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.exceptions.DatabaseException;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.util.CommonUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class SearchLogService {
    protected File root = null;

    public SearchLogService() {
    	root = new File(ZebraStatic.getWowXmlRoot() + ZebraStatic.SEARCH_LOG_DIR);
    	if(!root.exists()) root.mkdir();
    	
    	File logdtd = new File(root, "search.dtd");
    	if(logdtd.exists()) return;
    	
        StringBuffer outstr = new StringBuffer();
        outstr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        outstr.append("<!ELEMENT log (user, record*)>\n\n");
        outstr.append("<!ELEMENT user (#PCDATA)>\n");
        outstr.append("<!ELEMENT record (accessdate, sessionid, course, query)>\n");
        outstr.append("<!ELEMENT accessdate (#PCDATA)>\n");
        outstr.append("<!ELEMENT sessionid (#PCDATA)>\n");
        outstr.append("<!ELEMENT course (#PCDATA)>\n");
        outstr.append("<!ELEMENT query (#PCDATA)>\n");
        try {
	        FileWriter dtdout = new FileWriter(logdtd);
	        dtdout.write(outstr.toString());
	        dtdout.close();
        }
        catch(Exception e) {e.printStackTrace();}
    }
    
    public void addSearchLog(String userid, String sessionId, String course, String query) throws  Exception {
        File logfile = new File(root, "search_" + userid + ".xml");
        boolean newfile = !logfile.exists();
        RandomAccessFile raf = new RandomAccessFile(logfile, "rw");
        if (newfile) {
            raf.writeBytes("<?xml version=\"1.0\"?>\n\n");
            raf.writeBytes("<!DOCTYPE log SYSTEM 'search.dtd'>\n\n");
            raf.writeBytes("<log>\n");
            raf.writeBytes("  <user>" + userid + "</user>\n");
            raf.writeBytes("</log>");
        }
        raf.seek(raf.length()-6);
        raf.writeBytes("  <record>\n");
        raf.writeBytes("    <accessdate>" + CommonUtil.DateToString(new Date()) + "</accessdate>\n");
        raf.writeBytes("    <sessionid>" + sessionId + "</sessionid>\n");
        raf.writeBytes("    <course>" + course + "</course>\n");
        raf.writeBytes("    <query>"+String.valueOf(query)+"</query>\n");
        raf.writeBytes("  </record>\n");
        raf.writeBytes("</log>");
        raf.close();
    }
    
    public void clearLog() {
		clearLog(new FileFilter() {
			public boolean accept(File f) {
				return (f.getName().endsWith(".xml") && f.getName().startsWith("search_"));
			}
		});
    }
    public void clearSearchLog(final String userid) throws  DatabaseException {
		clearLog(new FileFilter() {
			public boolean accept(File f) {
				return (f.getName().endsWith(".xml") && f.getName().startsWith("search_" + userid));
			}
		});
    }
    private void clearLog(FileFilter filter) {
        File[] filelist = root.listFiles(filter);
        for (int i = 0; i < filelist.length; i++) {
            filelist[i].delete();
        }
    }
    
    public ArrayList<UserSearchRecord> getUserSearchLog(String userid, String course, Date beginDate) {
    	ArrayList<UserSearchRecord> usersearchlog = new ArrayList<UserSearchRecord>();
        File searchlogfile = new File(root, "search_" + userid + ".xml");
    	if(!searchlogfile.exists()) return usersearchlog;
        
    	try {
			DOMParser parser = new DOMParser();
			parser.parse(searchlogfile.toString());
			
			Document doc = parser.getDocument();
			Node usernode = doc.getDocumentElement().getFirstChild().getNextSibling();
			if(usernode.getNodeName().equals(userid))
				throw new Exception("This log file doesn't belong to " + userid);
			
			Node recordnode = usernode.getNextSibling();
			while(recordnode != null) {
				NodeList childs = recordnode.getChildNodes();
				UserSearchRecord record = new UserSearchRecord();
				record.userid = userid;
				
				for(int i = 0; i < childs.getLength(); i++) {
					Node child = childs.item(i);
					String childname = child.getNodeName();
					String childvalue = (child.getFirstChild() == null ? null : child.getFirstChild().getNodeValue());
					if(childvalue == null) continue;
					else {
						childvalue = childvalue.trim();
						if(childvalue.length() == 0) continue;
					}
					
					if(childname.equals("accessdate")) {
						//set access date
						Date date = null;
						try {date = CommonUtil.StringToDate(childvalue);}
						catch(Exception e) {continue;}
						if(beginDate == null)
							record.accessdate = date;
						else if(!date.before(beginDate))
							record.accessdate = date;
						else
							continue;
					}
					else if(childname.equals("sessionid")) {
						record.sessionid = childvalue;
					}
					else if(childname.equals("course")) {
						if(!childvalue.equals(course)) continue;
						record.course = childvalue;
					}
					else if(childname.equals("query")) {
						record.query = childvalue;
					}
				}//end for child list
				if(record.isValid()) {
					usersearchlog.add(record);
				}
				recordnode = recordnode.getNextSibling();
			}
    	}
    	catch (Exception e) {
    		System.out.println("getUserSearchLog causes error: " + e.getMessage());
    		//e.printStackTrace();
    	}
    	return usersearchlog;    	
    }
    
    private CourseSearchLog analyzeSearchLog(List<UserSearchRecord> searchlog) {
    	CourseSearchLog courselog = new CourseSearchLog();
    	int             n = searchlog.size();
    	int             i = 0;
    	for(i=0; i < n; i++) {
    		UserSearchRecord     record = (UserSearchRecord) (searchlog.get(i));
    		String               userid = record.userid;
    		String               sessionid = record.sessionid;
    		int                  idxUL = courselog.findUserLog(userid);
    		if(idxUL == -1) {
    			UserSearchLog usersearchlog = new UserSearchLog();
    			usersearchlog.userid = userid;
    			usersearchlog.userSearchSessions = new ArrayList<UserSearchSession>();
    			
    			UserSearchSession sessionlog = new UserSearchSession();
    			sessionlog.userid = userid;
    			sessionlog.sessionid = sessionid;
    			sessionlog.userSearchRecords = new ArrayList<UserSearchRecord>();
    			sessionlog.userSearchRecords.add(record);
    			
    			usersearchlog.userSearchSessions.add(sessionlog);
    			
    			if(courselog.userSearchLogs == null)
    				courselog.userSearchLogs = new ArrayList<UserSearchLog>();
    			courselog.userSearchLogs.add(usersearchlog);
    		}
    		else {
    			UserSearchLog usersearchlog = courselog.getUserLog(idxUL);
    			int           idxLS = usersearchlog.findUserSession(sessionid);
    			
    			if(idxLS == -1) {
        			UserSearchSession sessionlog = new UserSearchSession();
        			sessionlog.userid = userid;
        			sessionlog.sessionid = sessionid;
        			sessionlog.userSearchRecords = new ArrayList<UserSearchRecord>();
        			sessionlog.userSearchRecords.add(record);
        			
        			usersearchlog.userSearchSessions.add(sessionlog);
    			}
    			else {
        			UserSearchSession sessionlog = usersearchlog.getSession(idxLS);
        			sessionlog.userid = userid;
        			sessionlog.sessionid = sessionid;
        			sessionlog.userSearchRecords.add(record);
    			}
    		}
    	}
    	return courselog;
    }

    public CourseSearchLog getCourseSearchLog(String userid, String course, Date beginDate) {
    	List<UserSearchRecord> logs = getUserSearchLog(userid, course, beginDate);
    	CourseSearchLog courselog = analyzeSearchLog(logs);
    	courselog.course = course;
    	return courselog;
    }
    public CourseSearchLog getCourseSearchLog(String course, Date beginDate, boolean doesIgnoreAnonymous) {
		List<UserSearchRecord> logs = new LinkedList();
		try {
			ProfileDB pdb = WOWStatic.DB().getProfileDB();
			Vector    v_personalid = pdb.getProfileList();
			int       n   = v_personalid.size(); 
			for (int i = 0; i < n; i++) {
				String  personalid = (String) v_personalid.get(i); 
				if(doesIgnoreAnonymous) {
					if(personalid.indexOf("an_userID_") != -1) continue;
				}

				List<UserSearchRecord> searchlog = getUserSearchLog(personalid, course, beginDate);
				if(searchlog.size() > 0) logs.addAll(searchlog);
			}
		}
		catch (DatabaseException e) {
			e.printStackTrace();
		}
		CourseSearchLog courselog = analyzeSearchLog(logs);
		courselog.course = course;
		return courselog;
    }
}
