/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * MOTFormat.java 1.0, July 13, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.util;

import java.util.Vector;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;

import vn.spring.WOW.exceptions.*;

public class MOTFormat {

    // the connection to the MOT database
    private static Connection conn;

    // Initialized?
    private static boolean init = false;

    public static void initMOT(String host, String user, String password, String database) throws
                       DatabaseException {
        if (init) return;

        //Register the driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            throw new DatabaseException("MySql driver not found");
        }

        //Open a connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?user=" + user + "&password=" + password);
        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
            throw new DatabaseException("WOW database not found");
        }

        init = true;
    }

    public static Vector conceptlist;
    public static void convertMOTtoGA(long motlesson, String author) throws
                       DatabaseException {
        if (!init) {System.out.println("MOTFormat.convertMOTtoGA: not initialized!"); return;}
        conceptlist = new Vector();
        try {
	        ResultSet rs = execQuery("");
	        rs.close();
        }
	    catch(SQLException e) {
	    	   
	    }
    }

    private static ResultSet execQuery(String query) throws DatabaseException {
        ResultSet rs;
        try {
            rs = conn.createStatement().executeQuery(query);
        } catch (SQLException e) {throw new DatabaseException(e.getMessage());}
        return rs;
    }
}