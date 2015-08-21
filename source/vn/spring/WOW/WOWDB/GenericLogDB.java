/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * GenericLogDB.java 2.0 25-05-2008
 *
 * Copyright (c) 2008, 2008 University of Science. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 *
 */

package vn.spring.WOW.WOWDB;

import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.WOWStatic;

import java.util.*;


import vn.spring.zebra.log.UserAccessRecord;
import vn.spring.zebra.log.UserAccessSession;
import vn.spring.zebra.log.UserAccessLog;
import vn.spring.zebra.log.CourseAccessLog;

/**
 * Modified by Loc Nguyen 2008. This file is very important
 *
 */
public abstract class GenericLogDB implements LogDB {
    /**
     * Returns true if logging is enabled within the WOW! system.
     */
    public boolean needLogging() {
/* this part no longer needed, the check is done before calling any logging methods.
        boolean logging;
        try {
            logging = new Boolean(WOWStatic.config.Get("logging")).booleanValue();
        } catch (NullPointerException e) {
            logging = false;
        }
        return logging;
*/
        return true;
    }

    //Added by Loc Nguyen
    private CourseAccessLog analyzeAccessLog(List<UserAccessRecord> accesslog) throws  WOWException {
    	CourseAccessLog courselog = new CourseAccessLog();
    	int             n = accesslog.size();
    	int             i = 0;
    	for(i=0; i < n; i++) {
    		UserAccessRecord     record = (UserAccessRecord) (accesslog.get(i));
    		String               userid = record.getUserId();
    		String               sessionid = record.getSessionId();
    		int                  idxUL = courselog.findUserLog(userid);
    		if(idxUL == -1) {
    			UserAccessLog useraccesslog = new UserAccessLog();
    			useraccesslog.userid = userid;
    			useraccesslog.userAccessSessions = new ArrayList<UserAccessSession>();
    			
    			UserAccessSession sessionlog = new UserAccessSession();
    			sessionlog.userid = userid;
    			sessionlog.sessionid = sessionid;
    			sessionlog.userAccessRecords = new ArrayList<UserAccessRecord>();
    			sessionlog.userAccessRecords.add(record);
    			
    			useraccesslog.userAccessSessions.add(sessionlog);
    			
    			if(courselog.userAccessLogs == null)
    				courselog.userAccessLogs = new ArrayList<UserAccessLog>();
    			courselog.userAccessLogs.add(useraccesslog);
    		}
    		else {
    			UserAccessLog useraccesslog = courselog.getUserLog(idxUL);
    			int           idxLS = useraccesslog.findUserSession(sessionid);
    			
    			if(idxLS == -1) {
        			UserAccessSession sessionlog = new UserAccessSession();
        			sessionlog.userid = userid;
        			sessionlog.sessionid = sessionid;
        			sessionlog.userAccessRecords = new ArrayList<UserAccessRecord>();
        			sessionlog.userAccessRecords.add(record);
        			
        			useraccesslog.userAccessSessions.add(sessionlog);
    			}
    			else {
        			UserAccessSession sessionlog = useraccesslog.getSession(idxLS);
        			sessionlog.userid = userid;
        			sessionlog.sessionid = sessionid;
        			sessionlog.userAccessRecords.add(record);
    			}
    		}
    	}
    	return courselog;
    }
    
    //Added by Loc Nguyen
    public ArrayList<UserAccessRecord> getUserAccessLog(String userid, String course, Date beginDate) throws WOWException {
    	throw new WOWException();
    }
    
    //Added by Loc Nguyen
    public CourseAccessLog getCourseAccessLog(String userid, String course, Date beginDate) throws  WOWException {
    	List<UserAccessRecord> logs = getUserAccessLog(userid, course, beginDate);
    	CourseAccessLog courselog = analyzeAccessLog(logs);
    	courselog.coursename = course;
    	return courselog;
    }
    
    //Added by Loc Nguyen
    public CourseAccessLog getCourseAccessLog(String coursename, Date beginDate, boolean doesIgnoreAnonymous) throws  WOWException {
		List<UserAccessRecord> logs = new LinkedList();
		try {
			ProfileDB pdb = WOWStatic.DB().getProfileDB();
			Vector    v_personalid = pdb.getProfileList();
			int       n   = v_personalid.size(); 
			for (int i = 0; i < n; i++) {
				String  personalid = (String) v_personalid.get(i); 
				if(doesIgnoreAnonymous) {
					if(personalid.indexOf("an_userID_") != -1) continue;
				}

				LogDB logdb = WOWStatic.DB().getLogDB();
				List<UserAccessRecord> accesslog = logdb.getUserAccessLog(personalid, coursename, beginDate);
				if(accesslog != null && accesslog.size() > 0)
					logs.addAll(accesslog);
			}
		}
		catch (DatabaseException e) {
			e.printStackTrace();
		}
		CourseAccessLog courselog = analyzeAccessLog(logs);
		courselog.coursename = coursename;
		return courselog;
    }
}