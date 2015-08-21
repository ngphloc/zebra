/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/* DBSetup.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

import java.sql.*;
import vn.spring.WOW.WOWStatic;

public class DBSetup {
// this function should also
// work with postgreSQL i think

  public String jdbcUrl;
  public String dbuser, dbpassword;
  public String dbRootPassword=null; // root is always 'root'
  public String dbClient="localhost"; // client might be 'wowserver.mydomain.com'
                                         // only from this Database-client/webserver
                                         // connections are allowed

  private Connection conn = null;

/*
 * Connect without using root permission
 */

  public DBSetup(String jdbcUrl,String user,String password) {
    this.jdbcUrl = jdbcUrl;
    this.dbuser=user;
    this.dbpassword = password;
  }

/*
 * Connect with using root permission
 */

  public DBSetup(String jdbcUrl, String user, String password, String rootpass) {
     this(jdbcUrl, user, password);
     dbRootPassword=rootpass;
  }

  public void setRootPasswd(String rootpass) {
     dbRootPassword=rootpass;
  }

  public void setDBClient(String client) {
     dbClient=client;
  }

  public void openUserConnection() throws ClassNotFoundException,SQLException {
      Class.forName(WOWStatic.config.Get("jdbcDriver"));
      conn = DriverManager.getConnection(jdbcUrl + "?user=" + dbuser + "&password=" + dbpassword);
  }
  public void closeConnection() throws SQLException {
      conn.close();
  }

  public void openRootConnection() throws ClassNotFoundException,SQLException {
      Class.forName(WOWStatic.config.Get("jdbcDriver"));
      conn = DriverManager.getConnection(jdbcUrl.substring(0, jdbcUrl.lastIndexOf("/")) + "mysql?user=root&password=" + dbRootPassword);
  }

  public void openRootWowConnection() throws ClassNotFoundException,SQLException {
      Class.forName(WOWStatic.config.Get("jdbcDriver"));
      conn = DriverManager.getConnection(jdbcUrl + "?user=root&password="+dbRootPassword);
  }


  public String getVersion() throws SQLException {
    Statement stmt = null;
    String res=null;

    synchronized (conn) {
        stmt = conn.createStatement();
        ResultSet RS = stmt.executeQuery("SELECT value FROM constants WHERE id='dataversion'");
        if (RS.next()) {
                res=RS.getString(1);
        }
        // Clean up after ourselves
        RS.close();
        stmt.close();
    }
    return res;
  }

  public void SQLSetup() throws SQLException {
	String dbname = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + jdbcUrl.length() - 1);
    Statement stmt;
    stmt=conn.createStatement();
    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbname);
  }

  /*
   * checkTables tests the datebase for the NAMES or
   * the required tables (the system might then assume
   * the tables are created the right way).
   *
   */

  public boolean checkTables() throws SQLException {
        Statement stmt = null;
        synchronized (conn) {
            stmt = conn.createStatement();
            ResultSet RS = stmt.executeQuery("SHOW TABLES");
            while (RS.next()) {
//                    System.out.println(RS.getString(1));
//    verify this table to the 'required' list
//    if tablename

            }
            // Clean up after ourselves
            RS.close();
            stmt.close();
        }
// if constants exists
        synchronized (conn) {
            stmt = conn.createStatement();
            ResultSet RS = stmt.executeQuery("SELECT value FROM constants WHERE id='dataversion'");
//            while (RS.next()) {
//                    System.out.println(RS.getString(1));
//    verify this table to the 'required' list
//    if tablename
//
//            }
            // GET value

            // if value in [...) then ...
            // else

            // Clean up after ourselves
            RS.close();
            stmt.close();
        }

    return true;//fixit
  }
/*
 * Create a new user/permissions
 */

  public void UserSetup() throws SQLException {
     String dbname = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + jdbcUrl.length() - 1);
     PreparedStatement pstmt = null;
     Statement stmt;
     synchronized (conn) {
         pstmt = conn.prepareStatement("GRANT ALL PRIVILEGES ON " + dbname + ".* TO '" + dbuser + "'@'" + dbClient + "' IDENTIFIED BY ? WITH GRANT OPTION");
         pstmt.setString(1, dbpassword);
         pstmt.executeUpdate();
     }
     stmt=conn.createStatement();
     stmt.executeUpdate("FLUSH PRIVILEGES");
  }

  // this function sets up the WOW-Database according
  // changed by @Loc Nguyen @  03-05-2008
  // to the WOW! 1.0 specs
  // end changed by @Loc Nguyen @  03-05-2008
  // changed by @Loc Nguyen @  04-02-2008 to add a table for the logging of accesses and user model updates
  public void TableSetup() throws SQLException {


     // connect to database
     Statement stmt;
     stmt=conn.createStatement();
     String query;

     query="CREATE TABLE IF NOT EXISTS profile(id int not null unique auto_increment,primary key (id))";
     stmt.executeUpdate(query);
     // changed by @Loc Nguyen @  03-05-2008
     // added firsttimeupdated
     query="CREATE TABLE IF NOT EXISTS profrec(id int not null unique auto_increment, user int not null, name varchar(150) not null, value text default null, firsttimeupdated int not null default 0, primary key (id))";
     // end changed by @Loc Nguyen @  03-05-2008
     stmt.executeUpdate(query);

     // changed by @Loc Nguyen @  28-05-2008
     query="CREATE TABLE IF NOT EXISTS concept(id int not null unique auto_increment, name varchar(150) not null unique, description text default null, expr text default null, resource text default null, nocommit int not null default 0, stable text default null, stable_expr text default null, concepttype text default null, title text, firstchild text default null, nextsib text default null, parent text default null, primary key (id))";
     // end changed by @Bart
     stmt.executeUpdate(query);

     query="CREATE TABLE IF NOT EXISTS attribute(id int not null unique auto_increment, concept int not null, name varchar(150) not null, description text, def text, type int not null, readonly int not null, system int not null, persistent int not null, stable text, stable_expr text, primary key (id))";
     stmt.executeUpdate(query);

     query="CREATE TABLE IF NOT EXISTS action(id int not null unique auto_increment, attribute int not null, concept int not null, ptrigger int not null, pcondition text, primary key (id))";
     stmt.executeUpdate(query);

     query="CREATE TABLE IF NOT EXISTS assignment(id int not null unique auto_increment, action int not null, attribute int not null, concept int not null, truestat int not null, var text, expr text, primary key (id))";
     stmt.executeUpdate(query);

     // added by @Loc Nguyen @  03-05-2008
     query="CREATE TABLE IF NOT EXISTS casegroup(id int not null unique auto_increment, attribute int not null, concept int not null, defaultfragment text, primary key (id))";
     stmt.executeUpdate(query);

     query="CREATE TABLE IF NOT EXISTS casevalue(id int not null unique auto_increment, casegroup int not null, value text, returnfragment text, primary key (id))";
     stmt.executeUpdate(query);
     // end added by @Loc Nguyen @  03-05-2008

     // added by @Loc Nguyen @  04-02-2008
     query="CREATE TABLE IF NOT EXISTS accesslog(id bigint not null unique auto_increment, accessdate timestamp not null, sessionid  varchar(64) not null, name varchar(100) not null, user varchar(50) not null, fragment tinyint(1) not null, primary key (id))";
     stmt.executeUpdate(query);

     query="CREATE TABLE IF NOT EXISTS updateumlog(id int not null unique auto_increment,  accessdate timestamp not null, sessionid  varchar(64) not null, name varchar(100) not null, oldval varchar(25) not null, newval varchar(25) not null, pageaccessid bigint not null, user varchar(50) not null, primary key (id))";
     stmt.executeUpdate(query);
     // end added by @Loc Nguyen @  03-05-2008

// storing system constants within database
//   "dataversion" -> "1.0"  implies the data within this database
     query="DROP TABLE IF EXISTS constants";
     stmt.executeUpdate(query);
     query="CREATE TABLE IF NOT EXISTS constants(id char(16) not null, val varchar(150) not null, primary key (id))";
     stmt.executeUpdate(query);
     // result value (#affected rows) is always 0, useless!

     query="INSERT INTO constants VALUES ('version','1.0')";
     stmt.executeUpdate(query);

  }
}
