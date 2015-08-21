/*


 This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

 WOW! is also open source software; 


 */
/**
 * DBConceptDB.java 1.0, June 1, 2008.
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.WOWDB;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import vn.spring.WOW.datacomponents.Action;
import vn.spring.WOW.datacomponents.Assignment;
import vn.spring.WOW.datacomponents.Attribute;
import vn.spring.WOW.datacomponents.Case;
import vn.spring.WOW.datacomponents.CaseGroup;
import vn.spring.WOW.datacomponents.Concept;
import vn.spring.WOW.datacomponents.ConceptHierStruct;
import vn.spring.WOW.exceptions.DatabaseException;
import vn.spring.WOW.exceptions.InvalidAttributeException;
import vn.spring.WOW.exceptions.InvalidConceptException;
import vn.spring.WOW.ConnectionPool;

/**
 * This interface must be implemented by a class to function as a
 * concept storage means.
 */
public class DBConceptDB implements ConceptDB {
    // cashing table
    private Hashtable conceptcache = new Hashtable();

    /**
     * Creates a new concept database interface. using specified
     * host,user,database and password
     */
    public DBConceptDB(String jdbcUrl, String user, String password) throws DatabaseException {
        // Register the driver

    }

    /**
     * Creates a concept with the specified name and returns the id.
     */
    public long createConcept(String name) throws DatabaseException {
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            long result = -1;
            conn = ConnectionPool.getConnection();
            stmt = conn
                    .prepareStatement("INSERT INTO concept(name) VALUES (?)");
            stmt.setString(1, name);
            stmt.executeUpdate();
            rs = stmt.executeQuery("SELECT max(id) from concept");
            if (!rs.first())
                ;
            result = rs.getLong(1);
            return result;
        } catch (SQLException e) {
            String msg = "unable to create concept";

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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
     * Returns the id of the specified concept.
     */
    public long findConcept(String name) throws DatabaseException,
            InvalidConceptException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            long result = -1;
            conn = ConnectionPool.getConnection();
            stmt = conn
                    .prepareStatement("SELECT id FROM concept WHERE name = ?");
            stmt.setString(1, name);
            rs = stmt.executeQuery();

            if (!rs.first()) {
                throw new InvalidConceptException(
                        "concept does not exist: " + name);
            }
            result = rs.getLong(1);
            return result;
        } catch (SQLException e) {
            String msg = "error searching concept";

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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
     * Links the specified concept to the specified resource. Concepts
     * and resources may be linked only once.
     */
    public void linkResource(long id, String resource)
            throws InvalidConceptException, DatabaseException {
        conceptcache.remove(new Long(id));
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            stmt = conn
                    .prepareStatement("UPDATE concept SET resource = ? WHERE id = ?");
            stmt.setString(1, resource);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            String msg = "error linking resource";

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

    /**
     * Removes a link from the specified concept to a resource.
     */
    public void unlinkResource(long id) throws InvalidConceptException,
            DatabaseException {
        conceptcache.remove(new Long(id));
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            stmt = conn
                    .prepareStatement("UPDATE concept SET resource = NULL WHERE ID = ?");
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            String msg = "error removing link";

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

    /**
     * Returns the resource that is linked to the specified concept.
     */
    public String getLinkedResource(long id)
            throws InvalidConceptException, DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            String result = null;
            conn = ConnectionPool.getConnection();
            stmt = conn
                    .prepareStatement("SELECT resource FROM concept WHERE id = ?");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();

            if (!rs.first()) {
                throw new InvalidConceptException(
                        "concept does not exist: " + id);
            }

            result = rs.getString(1);
            return result;
        } catch (SQLException e) {
            String msg = "error retrieving linked resource";

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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
     * Returns the concept that is linked to the specified resource.
     */
    public String getLinkedConcept(String resource)
            throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            String result = null;
            conn = ConnectionPool.getConnection();
            stmt = conn
                    .prepareStatement("SELECT name FROM concept WHERE resource = ?");
            stmt.setString(1, resource);
            rs = stmt.executeQuery();
            // removed by Natalia Stash, 31-07-2008
            // if (!rs.first()) throw new
            // DatabaseException("resource does not exist:
            // "+resource);
            if (rs.first())
                result = rs.getString(1);
            return result;
        } catch (SQLException e) {
            String msg = "error retrieving linked concept";

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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

    private void setAssignment(long id, long attrid, long actionid,
            boolean truestat, Assignment assignment)
            throws DatabaseException {
        conceptcache.remove(new Long(id));
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionPool.getConnection();
            stmt = conn
                    .prepareStatement("INSERT INTO assignment(action, attribute, concept, truestat, var, expr) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setLong(1, actionid);
            stmt.setLong(2, attrid);
            stmt.setLong(3, id);
            stmt.setInt(4, knot(truestat));
            stmt.setString(5, assignment.getVariable());
            stmt.setString(6, assignment.getExpression());
            stmt.executeUpdate();
        } catch (SQLException e) {
            String msg = "unable to set assignment " + id;

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

    private void setAction(long id, long attrid, Action action)
            throws DatabaseException {
        conceptcache.remove(new Long(id));
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionPool.getConnection();
            long actionid;
            int i;
            stmt = conn
                    .prepareStatement("INSERT INTO action(attribute, concept, ptrigger, pcondition) VALUES (?, ?, ?, ?)");
            stmt.setLong(1, attrid);
            stmt.setLong(2, id);
            stmt.setInt(3, knot(action.getTrigger()));
            stmt.setString(4, action.getCondition());
            stmt.executeUpdate();
            ResultSet rs = stmt.executeQuery("SELECT max(id) from action");
            if (!rs.first())
                ;
            actionid = rs.getLong(1);

            for (i = 0; i < action.getTrueStatements().size(); i++) {
                setAssignment(id, attrid, actionid, true,
                        (Assignment) action.getTrueStatements().get(i));
            }

            for (i = 0; i < action.getFalseStatements().size(); i++) {
                setAssignment(id, attrid, actionid, false,
                        (Assignment) action.getTrueStatements().get(i));
            }
        } catch (SQLException e) {
            String msg = "unable to set action " + id;

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

    /**
     * Sets the specified attribute.
     */
    public void setAttribute(long id, Attribute attr)
            throws InvalidConceptException, DatabaseException {
        conceptcache.remove(new Long(id));
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            long attrid;

            stmt = conn
                    .prepareStatement("SELECT id FROM attribute WHERE concept = ? AND name = ?");
            stmt.setLong(1, id);
            stmt.setString(2, attr.getName());
            rs = stmt.executeQuery();

            // remove the existing attribute first
            if (rs.first()) {
                attrid = rs.getLong(1);
                removeAttribute(id, attr.getName());
            }

            // changed by @Loc Nguyen @ 03-05-2008
            // added stable and stable_expr
            stmt = conn
                    .prepareStatement("INSERT INTO attribute(concept, name, type, readonly, system, persistent, description, def, stable, stable_expr) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            // end changed by @Loc Nguyen @ 03-05-2008
            stmt.setLong(1, id);
            stmt.setString(2, attr.getName());
            stmt.setInt(3, attr.getType());
            stmt.setInt(4, knot(attr.isReadonly()));
            stmt.setInt(5, knot(attr.isSystem()));
            stmt.setInt(6, knot(attr.isPersistent()));
            stmt.setString(7, attr.getDescription());
            stmt.setString(8, attr.getDefault());

            // added by @Loc Nguyen @ 03-05-2008
            if ((attr.getStable() == null)
                    || ("".equals(attr.getStable()))) {
                stmt.setNull(9, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(9, attr.getStable());
            }

            if ((attr.getStableExpression() == null)
                    || ("".equals(attr.getStableExpression()))) {
                stmt.setNull(10, java.sql.Types.LONGVARCHAR);
            } else {
                stmt.setString(10, attr.getStableExpression());
            }
            // end added by @Loc Nguyen @ 03-05-2008

            stmt.executeUpdate();
            rs = stmt.executeQuery("SELECT max(id) from attribute");
            if (!rs.first())
                ;
            attrid = rs.getLong(1);
            for (int i = 0; i < attr.getActions().size(); i++) {
                setAction(id, attrid, (Action) attr.getActions().get(i));
            }

            // added by @Loc Nguyen @ 03-05-2008
            // add casegroup entry in the casegroup table
            if (attr.hasGroupNode()) {
                setCaseGroup(id, attrid, attr);
            }

            // end added by @Loc Nguyen @ 03-05-2008
        } catch (SQLException e) {
            String msg = "unable to set attribute " + id;

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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
     * Adds the casegroup properties to the casegroup table. Adds the
     * casegroup properties to the casegroup table.
     * @param conceptID the concept ID in the database
     * @param attrbuteID the attribute ID in the database
     * @param attr the attribute itself
     */
    private void setCaseGroup(long conceptID, long attributeID,
            Attribute attr) throws DatabaseException {
        conceptcache.remove(new Long(conceptID));
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            long casegroupId;

            // test if casegroup does already exists
            stmt = conn
                    .prepareStatement("SELECT defaultfragment FROM casegroup WHERE attribute = ? and concept = ?");
            stmt.setLong(1, attributeID);
            stmt.setLong(2, conceptID);
            rs = stmt.executeQuery();

            if (!rs.first()) {
                // casegroup does not exists
                stmt = conn
                        .prepareStatement("INSERT INTO casegroup(attribute, concept, defaultfragment) VALUES (?, ?, ?)");
                stmt.setLong(1, attributeID);
                stmt.setLong(2, conceptID);

                // CHANGE THIS .... if barend has fixed it
                // must add the default value
                stmt
                        .setString(3, attr.getCasegroup()
                                .getDefaultFragment());
                stmt.executeUpdate();
                rs = stmt.executeQuery("SELECT max(id) from casegroup");
                if (!rs.first())
                    ;
                casegroupId = rs.getLong(1);
            } else {
                // casegroup does exists
                // update default value and remove all the
                // casevalues
                stmt = conn
                        .prepareStatement("UPDATE casegroup SET defaultfragment = ? WHERE ID = ?");
                casegroupId = rs.getInt(1);
                stmt
                        .setString(1, attr.getCasegroup()
                                .getDefaultFragment());
                stmt.setLong(2, casegroupId);
                stmt.executeUpdate();
            }
            // update the casevalues for this casegroup
            setCaseValues(casegroupId, attr.getCasegroup().getCaseValues());
        } catch (SQLException e) {
            String msg = "unable to set casegroup ";

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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
     * Adds the casevalue properties to the casevalue table. Adds the
     * casegroup properties to the casevalue table.
     * @param conceptID the concept ID in the database
     * @param attrbuteID the attribute ID in the database
     * @param attr the attribute itself
     */
    private void setCaseValues(long caseGroupID, Vector caseValues)
            throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionPool.getConnection();
            int i;
            Case currentCase = null;

            // update the casevalues for this casegroup
            // test if casegroup does already exists
            // delete all the existing casevalue items for this
            // casegroup
            stmt = conn
                    .prepareStatement("DELETE FROM casevalue where casegroup = ?");
            stmt.setLong(1, caseGroupID);
            stmt.executeUpdate();

            // loop all the casevalues
            // add all the new casevalues for this casegroup
            for (i = 0; i < caseValues.size(); i++) {
                currentCase = (Case) caseValues.get(i);

                // add this case
                stmt = conn
                        .prepareStatement("INSERT INTO casevalue(casegroup, value, returnfragment) VALUES (?, ?, ?)");
                stmt.setLong(1, caseGroupID);
                stmt.setString(2, currentCase.getValue());
                stmt.setString(3, currentCase.getReturnfragment());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            String msg = "unable to set casevalue ";

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

    private void getAssignments(long actionid, Action action)
            throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            Assignment assignment = null;
            stmt = conn
                    .prepareStatement("SELECT truestat, var, expr FROM assignment WHERE action = ?");
            stmt.setLong(1, actionid);
            rs = stmt.executeQuery();

            while (rs.next()) {
                assignment = new Assignment(rs.getString(2), rs
                        .getString(3));

                if (rs.getLong(1) == 1) {
                    action.getTrueStatements().add(assignment);
                } else {
                    action.getFalseStatements().add(assignment);
                }
            }
        } catch (SQLException e) {
            String msg = "unable to retreive assignment";

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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

    private void getActions(long attrid, Attribute attribute)
            throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            Action action = null;
            stmt = conn
                    .prepareStatement("SELECT ptrigger, pcondition, id FROM action WHERE attribute = ?");
            stmt.setLong(1, attrid);
            rs = stmt.executeQuery();

            while (rs.next()) {
                action = new Action();
                action.setTrigger(rs.getInt(1) == 1);
                action.setCondition(rs.getString(2));
                getAssignments(rs.getLong(3), action);
                attribute.getActions().add(action);
            }
        } catch (SQLException e) {
            String msg = "unable to retreive action";

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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
     * Returns the specified attribute. Changed by
     * changed by Phuoc-Loc Nguyen 03-05-2008 *
     */
    public Attribute getAttribute(long id, String name)
            throws InvalidConceptException, DatabaseException,
            InvalidAttributeException {
        if (conceptcache.containsKey(new Long(id)))
            return ((Concept) conceptcache.get(new Long(id)))
                    .getAttribute(name);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            Attribute attribute = null;
            long attributeID;
            // concept, name, type, readonly, system, persistent,
            // description, def, stable, stable_expr
            // changed by @Loc Nguyen @ 02-12-2008
            stmt = conn
                    .prepareStatement("SELECT type, readonly, system, persistent, description, def, id, stable, stable_expr FROM attribute WHERE concept = ? AND name = ?");

            // end changed by @Loc Nguyen @ 02-12-2008
            stmt.setLong(1, id);
            stmt.setString(2, name);
            rs = stmt.executeQuery();

            if (!rs.first()) {
                throw new InvalidAttributeException(
                        "Attribute not found: " + name);
            }

            attribute = new Attribute(name, rs.getInt(1));
            attribute.setReadonly(rs.getInt(2) == 1);
            attribute.setSystem(rs.getInt(3) == 1);
            attribute.setPersistent(rs.getInt(4) == 1);
            // changed by @Loc Nguyen @ 02-12-2008
            attribute.setDescription(rs.getString(5));
            attribute.setDefault(rs.getString(6));

            // end changed by @Loc Nguyen @ 02-12-2008
            // added by @Loc Nguyen @ 02-12-2008
            if (!("".equals(rs.getString(8)) || rs.getString(8) == null)) {
                attribute.setStable(rs.getString(8));
            }

            if (!("".equals(rs.getString(9)) || rs.getString(9) == null)) {
                attribute.setStableExpression(rs.getString(9));
            }

            // end added by @Loc Nguyen @ 02-12-2008
            // changed by @Loc Nguyen @ 02-12-2008
            attributeID = rs.getLong(7);
            getActions(attributeID, attribute);

            // end changed by @Loc Nguyen @ 02-12-2008
            // Added by @Loc Nguyen @ 02-12-2008
            // get casegroup and all the casevalues
            getCaseGroup(attributeID, id, attribute);

            // end Added by @Loc Nguyen @ 02-12-2008
            return attribute;
        } catch (SQLException e) {
            String msg = "unable to retreive attribute";

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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
     * Set the casegroup object with the info out the casegroup table.
     * Set the casegroup object with the info out the casegroup table.
     * @param attrid the attribute ID in the database
     * @param conceptID the concept ID in the database
     * @param attribute the attribute itself Added by
     * @Loc Nguyen @ 03-05-2008
     */
    private void getCaseGroup(long attrid, long conceptID,
            Attribute attribute) throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            long caseGroupID;
            stmt = conn
                    .prepareStatement("SELECT defaultfragment, id FROM casegroup WHERE attribute = ? and concept = ? ");
            stmt.setLong(1, attrid);
            stmt.setLong(2, conceptID);
            rs = stmt.executeQuery();

            if (!rs.first()) {
                // there is no casegroup available so set it to null
                CaseGroup casegroup = null;
                attribute.setCasegroup(casegroup);
            } else {
                CaseGroup casegroup = null;

                // get the caseGroupID
                caseGroupID = rs.getLong(2);

                // get the casegroup
                casegroup = attribute.getCasegroup();

                // test if the casegroup is initialised
                if (casegroup == null) {
                    // the casegroup class does not exists yet, create
                    // it
                    casegroup = new CaseGroup();

                    // set default fragment
                    casegroup.setDefaultFragment(rs.getString(1));

                    // set the newly created casegroup class in the
                    // attribute
                    attribute.setCasegroup(casegroup);
                } else {
                    // the casegroup class existed already, so set the
                    // default fragment
                    casegroup.setDefaultFragment(rs.getString(1));
                }

                // add the casevalues if present
                getCaseValues(caseGroupID, casegroup);
            }
        } catch (SQLException e) {
            String msg = "unable to retreive action";

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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
     * Set the case objects with the info out the casevalues table.
     * Set the case objects with the info out the casevalues table.
     * @param caseGroupID the casegroup ID in the database
     * @param cg the casegroup class itself Added by
     * @Loc Nguyen @  03-05-2008
     */
    private void getCaseValues(long caseGroupID, CaseGroup cg)
            throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            Case casevalue = null;

            // perform query to get all the casevalues
            stmt = conn
                    .prepareStatement("SELECT id, value, returnfragment FROM casevalue WHERE casegroup = ? ");
            stmt.setLong(1, caseGroupID);
            rs = stmt.executeQuery();

            // remove all the existing cases (if any)
            cg.getCaseValues().removeAllElements();

            // loop all case values
            while (rs.next()) {
                // create case
                casevalue = new Case();

                // set case values
                casevalue.setValue(rs.getString(2));
                casevalue.setReturnfragment(rs.getString(3));

                // add case to the vector
                cg.getCaseValues().add(casevalue);
            }
        } catch (SQLException e) {
            String msg = "unable to retreive action";

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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
     * Removes the specified attribute.
     */
    public void removeAttribute(long id, String name)
            throws InvalidConceptException, DatabaseException {
        conceptcache.remove(new Long(id));
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            long attrid;
            stmt = conn
                    .prepareStatement("SELECT id FROM attribute WHERE concept = ? AND name = ?");
            stmt.setLong(1, id);
            stmt.setString(2, name);
            rs = stmt.executeQuery();

            if (rs.first()) {
                attrid = rs.getLong(1);
                stmt = conn
                        .prepareStatement("DELETE FROM assignment WHERE attribute = ?");
                stmt.setLong(1, attrid);
                stmt.executeUpdate();
                stmt = conn
                        .prepareStatement("DELETE FROM action WHERE attribute = ?");
                stmt.setLong(1, attrid);
                stmt.executeUpdate();
                stmt = conn
                        .prepareStatement("DELETE FROM attribute WHERE id = ?");
                stmt.setLong(1, attrid);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            String msg = "unable to remove attribute" + id;

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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
     * Returns the list of names of all attributes of a concept.
     */
    public Vector getAttributeNames(long id)
            throws InvalidConceptException, DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            Vector result = new Vector();
            stmt = conn
                    .prepareStatement("SELECT name FROM attribute WHERE concept = ?");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException e) {
            String msg = "unable to retrieve list of attribute names";

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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
     * Returns all attributes of a concept.
     */
    public Vector getAttributes(long id) throws InvalidConceptException,
            DatabaseException {
        if (conceptcache.containsKey(new Long(id)))
            return ((Concept) conceptcache.get(new Long(id)))
                    .getAttributes();
        Vector names = getAttributeNames(id);
        Vector result = new Vector();
        String name = null;

        try {
            for (int i = 0; i < names.size(); i++) {
                name = (String) names.get(i);
                result.add(getAttribute(id, name));
            }
        } catch (InvalidAttributeException e) {
            throw new DatabaseException(e.getMessage());
        }

        return result;
    }

    /**
     * Sets all attributes of a concept.
     */
    public void setAttributes(long id, Vector attrs)
            throws InvalidConceptException, DatabaseException {
        conceptcache.remove(new Long(id));
        Vector names = getAttributeNames(id);
        String name = null;
        int i;

        for (i = 0; i < names.size(); i++) {
            name = (String) names.get(i);
            removeAttribute(id, name);
        }

        for (i = 0; i < attrs.size(); i++) {
            setAttribute(id, (Attribute) attrs.get(i));
        }
    }

    /**
     * Saves a concept with the specified id.
     */
    public void setConcept(long id, Concept concept)
            throws InvalidConceptException, DatabaseException {
        conceptcache.remove(new Long(id));
        // concept.getResourceURL().toString() might fail
        URL resourceURL = concept.getResourceURL();
        String resourceName;

        if (resourceURL == null) {
            resourceName = null;
        } else {
            resourceName = resourceURL.toString();
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionPool.getConnection();
            // changed by @Loc Nguyen @  14-05-2008; concepttype,title
            // added by @Tomi(WOW-Pitt intergration)
            stmt = conn
                    .prepareStatement("UPDATE concept SET name = ?, resource = ?, description = ?, nocommit = ?, stable = ?, stable_expr = ?, concepttype = ?, title = ?, firstchild = ?, nextsib = ?, parent = ? WHERE id = ?");
            stmt.setString(1, concept.getName());
            stmt.setString(2, resourceName);
            stmt.setString(3, concept.getDescription());
            stmt.setInt(4, knot(concept.getNoCommit()));

            // added by @Loc Nguyen @  14-05-2008
            if ((concept.getStable() == null)
                    || ("".equals(concept.getStable()))) {
                stmt.setNull(5, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(5, concept.getStable());
            }

            if ((concept.getStableExpression() == null)
                    || ("".equals(concept.getStableExpression()))) {
                stmt.setNull(6, java.sql.Types.LONGVARCHAR);
            } else {
                stmt.setString(6, concept.getStableExpression());
            }

            // Added by @Tomi (WOW-Pitt integration)
            stmt.setString(7, concept.getType());
            stmt.setString(8, concept.getTitle());
            // end Tomi

            ConceptHierStruct chs = concept.getHierStruct();
            if (chs != null) {
                if (chs.firstchild != null)
                    stmt.setString(9, chs.firstchild);
                else
                    stmt.setNull(9, java.sql.Types.LONGVARCHAR);
                if (chs.nextsib != null)
                    stmt.setString(10, chs.nextsib);
                else
                    stmt.setNull(10, java.sql.Types.LONGVARCHAR);
                if (chs.parent != null)
                    stmt.setString(11, chs.parent);
                else
                    stmt.setNull(11, java.sql.Types.LONGVARCHAR);
            } else {
                stmt.setNull(9, java.sql.Types.LONGVARCHAR);
                stmt.setNull(10, java.sql.Types.LONGVARCHAR);
                stmt.setNull(11, java.sql.Types.LONGVARCHAR);
            }

            // end added by @Loc Nguyen @  14-05-2008

            stmt.setLong(12, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            String msg = "error setting concept" + id;

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

        setAttributes(id, concept.getAttributes());

        // Added by @Tomi (WOW-Pitt integration)
        // Update Hierarchy table
        // setHierarchy(id,concept.getHierStruct());
        // end Tomi
    }

    /**
     * Loads a concept with the specified id.
     */
    public Concept getConcept(long id) throws DatabaseException,
            InvalidConceptException {
        if (conceptcache.containsKey(new Long(id)))
            return (Concept) conceptcache.get(new Long(id));
        Concept concept = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            // changed by @Loc Nguyen @  14-05-2008
            stmt = conn
                    .prepareStatement("SELECT name, resource, description, nocommit, stable, stable_expr,concepttype,title, firstchild, nextsib, parent FROM concept WHERE id = ?");
            stmt.setLong(1, id);
            rs = stmt.executeQuery();

            if (!rs.first()) {
                throw new InvalidConceptException(
                        "concept does not exist: " + id);
            }

            concept = new Concept(rs.getString(1));
            concept.id = id;

            try {
                if (rs.getString(2) != null) {
                    concept.setResourceURL(new URL(rs.getString(2)));
                }
            } catch (Exception e) {
                String msg = "unable to retrieve concept";

                if (e.getMessage() != null) {
                    msg = msg + ": " + e.getMessage();
                } else {
                    msg = msg + ": (unknown)";
                }

                throw new DatabaseException(msg);
            }

            concept.setDescription(rs.getString(3));
            concept.setNoCommit(rs.getLong(4) == 1);
            // added by @Loc Nguyen @  14-05-2008
            concept.setStable(rs.getString(5));
            concept.setStableExpression(rs.getString(6));
            // end added by @Loc Nguyen @  14-05-2008

            // Added by @Tomi (WOW-Pitt integration)
            concept.setType(rs.getString("concepttype"));
            concept.setTitle(rs.getString("title"));
            // concept.setHierStruct(getHierarchy(concept.id));
            // End Tomi
            ConceptHierStruct chs = new ConceptHierStruct();
            chs.firstchild = rs.getString("firstchild");
            chs.nextsib = rs.getString("nextsib");
            chs.parent = rs.getString("parent");
            if ((chs.firstchild != null) || (chs.nextsib != null)
                    || (chs.parent != null)) {
                concept.setHierStruct(chs);
            }
        } catch (SQLException e) {
            String msg = "unable to retrieve concept" + id;

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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

        Vector names = getAttributeNames(id);
        String name = null;

        try {
            for (int i = 0; i < names.size(); i++) {
                name = (String) names.get(i);
                concept.getAttributes().add(getAttribute(id, name));
            }
        } catch (InvalidAttributeException e) {
            throw new DatabaseException(e.getMessage());
        }

        conceptcache.put(new Long(id), concept);
        return concept;
    }

    /**
     * Return the list of all concepts.
     */
    public Vector getConceptList() throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            Vector result = new Vector();
            stmt = conn.prepareStatement("SELECT name FROM concept");
            rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(rs.getString(1));
            }
            return result;
        } catch (SQLException e) {
            String msg = "uunable to retrieve list of concepts";

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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

    private int knot(boolean b) {
        return (b ? 1 : 0);
    }

    /**
     * Removes the specified concept. added by Natalia Stash
     */
    /*
     * public void removeConcept(String name) throws
     * InvalidConceptException, DatabaseException { try {
     * PreparedStatement stmt = null; ResultSet rs = null;
     * synchronized (conn) { stmt = conn.prepareStatement("DELETE FROM
     * concept WHERE name = ?"); stmt.setString(1, name);
     * stmt.executeUpdate(); } } catch (SQLException e) { String
     * msg="unable to remove concept "+name; if (e.getMessage()!=null)
     * msg=msg+": "+e.getMessage(); else msg=msg+": (unknown)"; throw
     * new DatabaseException(msg); } }
     */
    /**
     * Adds a record in table 'Hierarchy' for concept id added by
     * @Tomi
     */
    void setHierarchy(long id, ConceptHierStruct hier)
            throws DatabaseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionPool.getConnection();
            stmt = conn
                    .prepareStatement("INSERT INTO hierarchy(firstchild, nextsib,parent,conceptid) VALUES (?, ?, ?, ?)");
            stmt.setString(1, hier.firstchild);
            stmt.setString(2, hier.nextsib);
            stmt.setString(3, hier.parent);
            stmt.setLong(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            String msg = "error setting hierarchy for concept" + id;

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

    /**
     * Returns hierarchy struct for concept id; if no records found
     * returns null added by
     * @Tomi
     */
    ConceptHierStruct getHierarchy(long conceptID)
            throws DatabaseException {

        ConceptHierStruct hier = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.getConnection();
            stmt = conn
                    .prepareStatement("SELECT * FROM hierarchy WHERE conceptid = ? ");
            stmt.setLong(1, conceptID);
            rs = stmt.executeQuery();

            if (rs.first()) {

                hier = new ConceptHierStruct();
                hier.firstchild = rs.getString("firstchild");
                hier.firstchild = rs.getString("nextsib");
                hier.firstchild = rs.getString("parent");
            }
        } catch (SQLException e) {
            String msg = "unable to retreive action";

            if (e.getMessage() != null) {
                msg = msg + ": " + e.getMessage();
            } else {
                msg = msg + ": (unknown)";
            }

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
        return hier;
    }

    /**
     * Delete a concept.
     */
    public void removeConcept(long id) throws InvalidConceptException,
            DatabaseException {
        conceptcache.remove(new Long(id));
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionPool.getConnection();
            stmt = conn
                    .prepareStatement("DELETE FROM concept WHERE id = ?");
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            String msg = "error removing concept";

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
}
