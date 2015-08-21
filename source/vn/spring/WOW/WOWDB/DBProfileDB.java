/*


 This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

 WOW! is also open source software; 


 */
/**
 * DBProfileDB.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.WOWDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import vn.spring.WOW.datacomponents.AttributeValue;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.exceptions.DatabaseException;
import vn.spring.WOW.exceptions.InvalidProfileException;
import vn.spring.WOW.ConnectionPool;

/**
 * This interface must be implemented by a class to function as a user profile
 * storage means.
 */
public class DBProfileDB implements ProfileDB {
    /**
     * Creates a new profile database interface with custom values
     */
    public DBProfileDB(String jdbcUrl, String user, String password) throws DatabaseException {

    }

    /**
     * Creates a new user profile and returns the id.
     */
    public long createProfile() throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            int result = -1;
            conn = ConnectionPool.getConnection();
            stmt = conn.prepareStatement("INSERT INTO profile VALUES ();");
            stmt.executeUpdate();
            rs = stmt.executeQuery("SELECT max(id) from profile");
            if (!rs.first())
                ;
            result = (int) rs.getLong(1);
            return result;
        } catch (SQLException e) {
            throw new DatabaseException("Error: an unexpected error occurred: "
                    + e.getMessage());
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
    }

    /**
     * Loads a user profile with the specified id. This also creates the symbol
     * table for the jep parser.
     */
    public Profile getProfile(long id) throws DatabaseException,
            InvalidProfileException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Profile profile = new Profile();
            profile.id = id;
            Hashtable values = profile.getValues();
            AttributeValue value = null;
            conn = ConnectionPool.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM profile WHERE id = ?");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (!rs.first())
                throw new InvalidProfileException("profile does not exist: "
                        + id);
            stmt = conn
                    .prepareStatement("SELECT name, value, firsttimeupdated FROM profrec WHERE user = ?");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                String val = rs.getString(2);
                value = new AttributeValue();
                if (rs.getLong(3) == 1) {
                    value.setFirstTimeUpdated(rs.getBoolean(3));
                }
                value.setValue(val);
                value.clearUpdated();
                values.put(name, value);
            }

            return profile;
        } catch (SQLException e) {
            throw new DatabaseException("unable to read profile: " + id);
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
    }

    private int knot(boolean b) {
        return (b ? 1 : 0);
    }

    /**
     * Saves a user profile with the specified id. This is synchronized for
     * safety, but not all of this needs to be synchronized.
     */
    public void setProfile(long id, Profile profile) throws DatabaseException,
            InvalidProfileException {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtnew = null;
        PreparedStatement stmtupd = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            AttributeValue value = null;
            String key = null;

            stmt = conn.prepareStatement("SELECT * FROM profile WHERE id = ?");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (!rs.first())
                throw new InvalidProfileException("profile does not exist: "
                        + id + ".");
            stmtnew = conn
                    .prepareStatement("INSERT INTO profrec(user, name, value, firsttimeupdated) VALUES (?, ?, ?, ?)");
            stmtupd = conn
                    .prepareStatement("UPDATE profrec SET value = ?, firsttimeupdated = ? WHERE user = ? AND name = ?");
            for (Enumeration keys = profile.getValues().keys(); keys
                    .hasMoreElements();) {
                key = (String) keys.nextElement();
                value = (AttributeValue) profile.getValues().get(key);
                if (value.isNew()) {
                    stmtnew.setLong(1, id);
                    stmtnew.setString(2, key);
                    stmtnew.setString(3, value.getValue());
                    stmtnew.setInt(4, knot(value.isFirstTimeUpdated()));
                    stmtnew.executeUpdate();
                    value.clearNew();
                } else if (value.isUpdated()) {
                    stmtupd.setString(1, value.getValue());
                    stmtupd.setInt(2, knot(value.isFirstTimeUpdated()));
                    stmtupd.setLong(3, id);
                    stmtupd.setString(4, key);
                    stmtupd.executeUpdate();
                }
                value.clearUpdated();
            }
        } catch (SQLException se) {
            System.out.println("DBProfileDB: setProfile: SQL Exception");
            throw new DatabaseException("unable to read profile: " + id);
        } catch (Exception e) {
            e.printStackTrace();
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
                stmtnew.close();
            } catch (Exception e) {
            }
            try {
                stmtupd.close();
            } catch (Exception e) {
            }
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Finds the profile with a given login and return its id.
     */
    public long findProfile(String value) throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            stmt = conn
                    .prepareStatement("SELECT user FROM profrec WHERE name = \"personal.id\" AND value = ?");
            stmt.setString(1, value);
            rs = stmt.executeQuery();
            long out = 0;
            if (rs.next())
                out = rs.getLong(1);
            return out;
        } catch (SQLException e) {
            throw new DatabaseException("error searching profile");
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
    }

    /**
     * Return the list of all profile id's
     */
    public Vector getProfileList() throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            Vector result = new Vector();
            stmt = conn
                    .prepareStatement("SELECT value FROM profrec WHERE name = \"personal.id\"");
            rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException e) {
            String msg = "unable to retrieve list of profiles";
            if (e.getMessage() != null)
                msg = msg + ": " + e.getMessage();
            else
                msg = msg + ": (unknown)";
            throw new DatabaseException(msg);
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
    }

    /**
     * Removes the user profile with the specified id.
     */
    public void removeProfile(long id) throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionPool.getConnection();
            stmt = conn.prepareStatement("DELETE FROM profrec WHERE user = ?");
            stmt.setLong(1, id);
            stmt.executeUpdate();
            stmt = conn.prepareStatement("DELETE FROM profile WHERE id = ?");
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            String msg = "unable to remove profile";
            if (e.getMessage() != null)
                msg = msg + ": " + e.getMessage();
            else
                msg = msg + ": (unknown)";
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

}
