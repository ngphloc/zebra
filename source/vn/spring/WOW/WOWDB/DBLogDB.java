/*


 This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

 WOW! is also open source software; 


 */
/**
 * DBLogDB.java 1.0 04-02-2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.WOWDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.datacomponents.DotString;
import vn.spring.WOW.exceptions.DatabaseException;
import vn.spring.WOW.exceptions.WOWException;
import vn.spring.WOW.ConnectionPool;
import vn.spring.zebra.log.UserAccessRecord;
import vn.spring.zebra.log.UserAccessRecord.ACTION;

/**
 * <p>
 * Title: WOW Engine
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Technische Universiteit Eindhoven
 * </p>
 * @author WOW Team
 * @version 1.0
 */

/**
 * This interface must be implemented by a class to function as a log
 * storage means.
 */
public class DBLogDB extends GenericLogDB {
    private String lastSessionID = "";

    private long lastInsertAccessID = 0;

    public long lastInsertUMID = 0;

    /*
     * Gets the logging configuration entry out of the wowconfig.xml
     * file this determines if the system must log. Gets the logging
     * configuration entry out of the wowconfig.xml file this
     * determines if the system must log. @return logging must be done
     * or not
     */
    public boolean needLogging() {
        boolean logging;
        try {
            logging = new Boolean(WOWStatic.config.Get("logging"))
                    .booleanValue();
        } catch (NullPointerException e) {
            logging = false;
        }

        return logging;
    }

    /**
     * Creates a new Log database interface. using specified
     * host,user,database and password
     */
    public DBLogDB(String jdbcUrl, String user, String password) throws DatabaseException {
        // Register the driver

    }

    public void addTestLog(String user, String testname, Vector results) throws DatabaseException {
    }

    /*
     * query="CREATE TABLE IF NOT EXISTS accesslog( id bigint not null
     * unique auto_increment, accessdate timestamp not null, sessionid
     * varchar(25) not null, name varchar(100) not null, user
     * varchar(50) not null, fragment tinyint(1) not null, primary key
     * (id))"; query="CREATE TABLE IF NOT EXISTS updateumlog( id int
     * not null unique auto_increment, accessdate timestamp not null,
     * sessionid varchar(25) not null, name varchar(100) not null,
     * oldval varchar(25) not null, newval varchar(25) not null,
     * pageaccessid bigint not null, user varchar(50) not null,
     * primary key (id))";
     */
    public void addAccessLog(String name, String user, String sessionId,
            boolean fragment) throws DatabaseException {
        /*
         * Adds a record to the accesslog in the  database this
         * can be useful for the author to see how many times a
         * certain is accessed
         */
        // skip all of this when no logging is needed
        if (needLogging()) {
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                // get timestamp
                Timestamp t = null;
                t = new Timestamp(System.currentTimeMillis());

                // save this sessionID
                if (fragment == false) {
                    lastSessionID = sessionId;
                }
                conn = ConnectionPool.getConnection();
                stmt = conn
                        .prepareStatement("INSERT INTO accesslog(accessdate,sessionid,name,user,fragment) VALUES (?,?,?,?,?)");

                stmt.setTimestamp(1, t);
                // if it is a fragment, insert a 1 in the fragment
                // field,
                // and fill in the session id that belongs
                // to the real page (that was logged before this
                // fragment)
                // this is done because there was no access to the
                // sessionid in the xmlhandler
                if (fragment == true) {
                    stmt.setString(2, lastSessionID);
                    stmt.setInt(5, 1);
                } else {
                    stmt.setString(2, sessionId);
                    stmt.setInt(5, 0);
                }
                stmt.setString(3, name);
                stmt.setString(4, user);

                stmt.executeUpdate();
                rs = stmt.executeQuery("SELECT max(id) from accesslog");
                if (!rs.first())
                    ;
                lastInsertAccessID = rs.getLong(1);
                // actionid = (int)
                // ((com.mysql.jdbc.PreparedStatement)
                // stmt).getLastInsertID();
            } catch (SQLException e) {
                String msg = "unable to add a line to the access log";

                if (e.getMessage() != null) {
                    msg = msg + ": " + e.getMessage();
                } else {
                    msg = msg + ": (unknown)";
                }

                throw new DatabaseException(msg);
            } catch (Exception ge) {
                System.out
                        .println("Error: DBLogDB: addAccessLog: unable to add a line to the access log: "
                                + ge.getMessage());
            } finally {
                try {
                    rs.close();
                } catch (Exception e) {
                }
                try {
                    stmt.close();
                } catch (Exception e) {
                }
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        } // end of if (needLogging())

    }

    /**
     * adds a record the the UMupdate log table. adds a record the the
     * UMupdate log table.
     */
    public void addUMUpdate(String name, String oldval, String newval,
            String user) throws DatabaseException {

        // skip all of this when no logging is needed
        if (needLogging()) {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                // get timestamp
                Timestamp t = null;
                t = new Timestamp(System.currentTimeMillis());
                conn = ConnectionPool.getConnection();
                stmt = conn
                        .prepareStatement("INSERT INTO updateumlog(accessdate,sessionid,name,oldval,newval,pageaccessid,user) VALUES (?,?,?,?,?,?,?)");

                stmt.setTimestamp(1, t);
                stmt.setString(2, lastSessionID);
                stmt.setString(3, name);
                stmt.setString(4, oldval);
                stmt.setString(5, newval);
                stmt.setLong(6, lastInsertAccessID);
                stmt.setString(7, user);
                stmt.executeUpdate();
                // save the last inserted id
                ResultSet rs = stmt
                        .executeQuery("SELECT max(id) from updateumlog");
                if (!rs.first())
                    ;
                lastInsertUMID = rs.getLong(1);
            } catch (SQLException e) {
                String msg = "unable to add a line to the user model update log";

                if (e.getMessage() != null) {
                    msg = msg + ": " + e.getMessage();
                } else {
                    msg = msg + ": (unknown)";
                }

                throw new DatabaseException(msg);
            } catch (Exception ge) {
                System.out
                        .println("Error: DBLogDB: addUMUpdate: unable to add a line to the user model update log: "
                                + ge.getMessage());
            } finally {
                try {
                    stmt.close();
                } catch (Exception e) {
                }
                try {
                    conn.close();
                } catch (Exception e) {
                }
            }
        } // end of if (needLogging())
    }

    
    public void clearUMLog() throws DatabaseException {
    	clearLog("DELETE FROM updateumlog");
    }
    public void clearUMLog(String loginid) throws DatabaseException {
    	clearLog("DELETE FROM updateumlog WHERE user='" + loginid + "'");
    }
    
    public void clearAccessLog() throws DatabaseException {
    	clearLog("DELETE FROM accesslog");
    }
    public void clearAccessLog(String loginid) throws DatabaseException {
    	clearLog("DELETE FROM accesslog WHERE user='" + loginid + "'");
    }
    
    
    /**
     * removes all records from the log table, so clears it as
     * the name implies. removes all records from the log
     * table, so clears it as the name implies.
     */
    private void clearLog(String sql) throws DatabaseException {
        /*
         * removes all records from the updateumlog table, so clears
         * it as the name implies
         */
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionPool.getConnection();
            // delete all the existing casevalue items for this
            // umlog
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            String msg = "unable to clear the updateumlog table ";

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

            throw new DatabaseException(msg);
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
            }
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }
    
    public ArrayList<UserAccessRecord> getUserAccessLog(String userid, String coursename, Date beginDate) throws WOWException {
    	ArrayList<UserAccessRecord> useraccesslog = new ArrayList<UserAccessRecord>();
        Connection conn = null;
        PreparedStatement stmt = null;
    	try {
    		String sql = "SELECT accessdate,sessionid,name,user,fragment FROM accesslog WHERE user='" + userid + "'";
            conn = ConnectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
			ConceptDB cdb = WOWStatic.DB().getConceptDB();
			
            while(rs.next()) {
				UserAccessRecord record = new UserAccessRecord();
				//ID
				record.setUserId(userid);
				
				//Access date
				Date date = rs.getDate(1);
				if(beginDate == null)
					record.setAccessDate(date);
				else if(!date.before(beginDate))
					record.setAccessDate(date);
				else
					continue;
				
				//Session id
				record.setSessionId(rs.getString(2));

				//resource
				String name = rs.getString(3);
				DotString dsResourse = new DotString(name, "|");
				String resource = dsResourse.get(0);
				record.setResource(resource);
				if(dsResourse.size() > 2) {
					if(dsResourse.get(1).equals("logoff") || dsResourse.get(1).equals("logout"))
						record.setAction(ACTION.LOGOUT);
					else
						record.setAction(ACTION.VISIT);
				}
				else
					record.setAction(ACTION.VISIT);
				String conceptname = cdb.getLinkedConcept(resource);
				if(conceptname == null) continue; //or do something
				//Set course name
				if(coursename == null || coursename.length() == 0) {
					DotString ds = new DotString(conceptname);
					if(ds.size() < 2) continue;
					record.setCourseName(ds.get(0));
				}
				else
					record.setCourseName(coursename);
				//Set concept name
				record.setConceptName(conceptname);
				
				//Fragment
				int fragment = rs.getInt(5);
				record.setFragment(fragment == 0 ? false : true);
				
				if(record.isValid()) useraccesslog.add(record);
            }
    	}
    	catch(Exception e) {
    		System.out.println("getUserAccessLog causes error: " + e.getMessage());
    		//e.printStackTrace();
    	}
    	finally {
            try {stmt.close();} catch (Exception e) {}
            try {conn.close();} catch (Exception e) {}
    	}
    	return useraccesslog;
    }

}
