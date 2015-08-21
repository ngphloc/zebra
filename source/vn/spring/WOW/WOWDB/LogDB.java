/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * LogDB.java 2.0 25-05-2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.WOWDB;

import vn.spring.WOW.exceptions.*;

import java.util.*;

import vn.spring.zebra.log.CourseAccessLog;
import vn.spring.zebra.log.UserAccessRecord;

/**
 * Thin interface must be implemented by a class to function as means
 * of storing log information. Modified by Loc Nguyen because the file is very important
 */
public interface LogDB {
    public void addUMUpdate(String name,String oldval,String newval, String user) throws  DatabaseException;

    public void addAccessLog(String name, String user, String sessionId, boolean fragment) throws  DatabaseException;

    public void addTestLog(String user, String testname, Vector results) throws DatabaseException;

    public void clearAccessLog() throws  DatabaseException;
    public void clearAccessLog(String loginid) throws  DatabaseException;
    public void clearUMLog() throws  DatabaseException;
    public void clearUMLog(String loginid) throws  DatabaseException;

    /**
     * Returns true if logging is enabled within the WOW! system.
     */
    public boolean needLogging();

    //added by Loc Nguyen 2008
    public ArrayList<UserAccessRecord> getUserAccessLog(String userid, String course, Date beginDate) throws WOWException;
    public CourseAccessLog getCourseAccessLog(String coursename, Date beginDate, boolean doesIgnoreAnonymous) throws  WOWException;
    public CourseAccessLog getCourseAccessLog(String userid, String course, Date beginDate) throws  WOWException;
}