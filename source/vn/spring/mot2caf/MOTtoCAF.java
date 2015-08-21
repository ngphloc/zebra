/**
 * MOTtoCAF.java 1.1, September 14, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.mot2caf;

import java.util.*;
import java.io.*;
import java.sql.*;
import org.w3c.dom.*;

import com.sun.xml.tree.*;

public class MOTtoCAF {
    private Connection conn = null;
    private XmlDocument doc = null;
    private Element root = null;
    private String lesson = null;
    private Hashtable standardattr = new Hashtable();
    private Hashtable akeytocaf = new Hashtable();
    private Hashtable ackeytocaf = new Hashtable();
    private Hashtable acrinfo = new Hashtable();
    private Vector convertedDomains = new Vector();

    public MOTtoCAF(String filename, String lesson, String[] args) throws SQLException, IOException {
        this.lesson = lesson;
        String[] params = new String[] {"localhost", "barnout", "blaat", "testdb"};
        for (int i=2;i<args.length && i<6;i++) {params[i-2] = args[i];}
        initMOT(params[0], params[1], params[2], params[3]);
        initCAF();
        startConvert();
        if (filename != null) {
            PrintWriter pw = new PrintWriter(new FileWriter(filename));
            doc.write(pw);
            pw.close();
        }
    }

    public XmlDocument getDocument() {return doc;}

    private void initMOT(String host, String user, String password, String database) throws
                 SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e.getMessage());
        }
        conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "?user=" + user + "&password=" + password);
        ResultSet rs = conn.createStatement().executeQuery("SELECT id, name FROM standardattribute");
        while (rs.next()) {
            standardattr.put(new Long(rs.getLong(1)), rs.getString(2));
        }
    }

    private void initCAF() {
        doc = new XmlDocument();
        doc.setSystemId("CAF");
        doc.setDoctype(null, "../WOWstandard/CAF.dtd", null);
        Element eCAF = doc.createElement("CAF");
        doc.appendChild(eCAF);
        root = eCAF;
    }

    private void startConvert() throws SQLException {
        Element edomain = doc.createElement("domainmodel");
        root.appendChild(edomain);

        Element egoal = doc.createElement("goalmodel");
        root.appendChild(egoal);
        convertGoal(egoal, edomain);

        convertRelatedness();
    }

    private void convertRelatedness() {
        for (Enumeration keys = acrinfo.keys(); keys.hasMoreElements();) {
            Element key = (Element)keys.nextElement();
            Vector info = (Vector)acrinfo.get(key);
            Element erelation = doc.createElement("relation");
            key.appendChild(erelation);
            Element ername = doc.createElement("name");
            erelation.appendChild(ername);
            ername.appendChild(doc.createTextNode("relatedness"));
            for (int i=0;i<info.size();i++) {
                RelatednessInfo ri = (RelatednessInfo)info.get(i);
                convertRI(erelation, ri);
            }
        }
    }

    private void convertRI(Element erelation, RelatednessInfo info) {
        Element erelationlink = doc.createElement("relationlink");
        erelation.appendChild(erelationlink);
        erelationlink.setAttribute("weight", new Float(info.weight).toString());
        erelationlink.setAttribute("label", info.label);
        erelationlink.setAttribute("type", info.type);
        erelationlink.appendChild(doc.createTextNode(N2E((String)ackeytocaf.get(new Long(info.rcid)))));
    }

    private void convertGoal(Element egoal, Element edomain) throws SQLException {
        PreparedStatement stm = conn.prepareStatement("SELECT lesson.toplessonid, lessonhierarchy.weight, lessonhierarchy.label FROM lesson, lessonhierarchy WHERE (lesson.name LIKE ?) AND (lessonhierarchy.sublessonid1 = lesson.toplessonid)");
        lesson = lesson.replace('*', '%');
        stm.setString(1, lesson);
        ResultSet rs = stm.executeQuery();
        boolean b = false;
        if (rs.next()) {
            b = true;
            convertLesson(rs.getLong(1), egoal, edomain, new Object[] {rs.getString(2), rs.getString(3)});
        }
        stm.close();
        if (!b) throw new SQLException("No lesson found with name: "+lesson.replace('%','*'));
    }

    private void convertLesson(long lid, Element eparent, Element edomain, Object[] params) throws SQLException {
        PreparedStatement getlinfostm = conn.prepareStatement(
            "SELECT lessonhierarchy.sublessonid2, sublesson.attributeid, lessonhierarchy.weight, lessonhierarchy.label FROM lessonhierarchy, sublesson " +
            "WHERE lessonhierarchy.sublessonid2 = sublesson.id AND lessonhierarchy.sublessonid1 = ? " +
            "ORDER BY lessonhierarchy.orderind;"
        );

        Vector sublessons = new Vector();

        Element elesson = doc.createElement("lesson");
        eparent.appendChild(elesson);
        elesson.setAttribute("weight", (String)params[0]);
        elesson.setAttribute("label", (String)params[1]);

        getlinfostm.setLong(1, lid);
        ResultSet rs = getlinfostm.executeQuery();
        while (rs.next()) {
            long sublesson = rs.getLong(1);
            long attrid = rs.getLong(2);
            if (attrid == -1) sublessons.add(new Object[] {new Long(sublesson), rs.getString(3), rs.getString(4)}); else {
                Element econtents = doc.createElement("link");
                elesson.appendChild(econtents);
                econtents.setAttribute("weight", rs.getString(3));
                econtents.setAttribute("label", rs.getString(4));

                //is the used domain conceptmap converted
                if (!(akeytocaf.containsKey(new Long(attrid)))) {
                    //if not... try to convert
                    convertDomain(edomain, findRootConceptID(attrid));
                }

                econtents.appendChild(doc.createTextNode(N2E((String)akeytocaf.get(new Long(attrid)))));
            }
        }
        getlinfostm.close();

        for (int i=0;i<sublessons.size();i++) {
            Object[] slinfo = (Object[])sublessons.get(i);
            long sublesson = ((Long)slinfo[0]).longValue();
            convertLesson(sublesson, elesson, edomain, new Object[] {slinfo[1], slinfo[2]});
        }
    }

    private long findRootConceptID(long attrid) throws SQLException {
        PreparedStatement getconceptidstm = conn.prepareStatement("SELECT ConceptId FROM conceptattribute WHERE Id = ?");
        getconceptidstm.setLong(1, attrid);
        ResultSet rs = getconceptidstm.executeQuery();
        if (rs.next()) {
            return findRootConcept(rs.getLong(1));
        }
        System.out.println("Referenced attribute '"+attrid+"' without concept");
        return -1;
    }

    private long findRootConcept(long concid) throws SQLException {
        PreparedStatement getparentstm = conn.prepareStatement("SELECT ConceptId1 FROM concepthierarchy WHERE ConceptId2 = ?");
        getparentstm.setLong(1, concid);
        ResultSet rs = getparentstm.executeQuery();
        if (rs.next()) {
            long newid = rs.getLong(1);
            if (newid != -1) return findRootConcept(rs.getLong(1));
                        else return concid;
        } else {
            return concid;
        }
    }

    private void convertDomain(Element edomain, long rootconceptid) throws SQLException {
        //check if this domain is already converted
        if (convertedDomains.contains(new Long(rootconceptid))) return;

        //this domain is not yet converted
        UniqueConstraint uc = new UniqueConstraint();
        PreparedStatement stm = conn.prepareStatement("SELECT Name, rootconceptid FROM conceptmap WHERE rootconceptid = ?");
        stm.setLong(1, rootconceptid);
        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            Element ecmap = doc.createElement("concept");
            edomain.appendChild(ecmap);
            Element ename = doc.createElement("name");
            ecmap.appendChild(ename);
            String name = uc.makeUnique(N2E(rs.getString(1)));
            ename.appendChild(doc.createTextNode(name));
            convertDomainConcept(rs.getLong(2), ecmap, new UniqueConstraint(), name);
        }
        stm.close();

        //this domain is now converted
        convertedDomains.add(new Long(rootconceptid));
    }

    private void convertDomainConcept(long cid, Element parent, UniqueConstraint uc, String path) throws SQLException {
        PreparedStatement getcattrstm = conn.prepareStatement("SELECT standardattributeid, Name, Contents, Id FROM conceptattribute WHERE ConceptId = ?");
        PreparedStatement getchildconceptsstm = conn.prepareStatement("SELECT ConceptId2 FROM concepthierarchy WHERE ConceptId1 = ?");
        PreparedStatement getrelatednessstm = conn.prepareStatement("SELECT conceptid2, name, weight, type FROM relatedness WHERE conceptid1 = ?");

        Hashtable tempkeys = new Hashtable();
        String name;
        String aname;

        Element econcept = doc.createElement("concept");
        parent.appendChild(econcept);

        //add attributes and name
        name = "";
        tempkeys.clear();
        getcattrstm.setLong(1, cid);
        ResultSet rs = getcattrstm.executeQuery();
        Node refChild = null;
        while (rs.next()) {
            long said = rs.getLong(1);
            if (said == 1) {
                //found title
                Element ename = doc.createElement("name");
                name = uc.makeUnique(N2E(rs.getString(3)));
                ename.appendChild(doc.createTextNode(name));
                if (refChild == null) econcept.appendChild(ename); else econcept.insertBefore(ename, refChild);
            }
            //not title
            Element eattr = doc.createElement("attribute");
            econcept.appendChild(eattr); if (refChild == null) refChild = eattr;
            Element ename = doc.createElement("name");
            eattr.appendChild(ename);
            if (said == 100) {
                //no standard attribute
                aname = N2E(rs.getString(2));
            } else {
                //standard attribute
                if (standardattr.containsKey(new Long(said))) {
                    aname = N2E((String)standardattr.get(new Long(said)));
                } else {
                    aname = N2E(rs.getString(2));
                }
            }
            ename.appendChild(doc.createTextNode(aname));
            Element econtents = doc.createElement("contents");
            eattr.appendChild(econtents);
            econtents.appendChild(doc.createTextNode(FilterChars(N2E(rs.getString(3)))));
            tempkeys.put(new Long(rs.getLong(4)), aname);
        }
        getcattrstm.close();

        //process tempkeys table
        for (Enumeration keys=tempkeys.keys();keys.hasMoreElements();) {
            Long key = (Long)keys.nextElement();
            aname = (String)tempkeys.get(key);
            akeytocaf.put(key, path + "\\" + name + "\\" + aname);
        }

        //add relatedness info
        ackeytocaf.put(new Long(cid), path + "\\" + name);
        getrelatednessstm.setLong(1, cid);
        rs = getrelatednessstm.executeQuery();
        Vector relatedness = new Vector();
        while (rs.next()) {
            RelatednessInfo info = new RelatednessInfo();
            info.rcid = rs.getLong(1);
            info.label = rs.getString(2);
            info.weight = rs.getFloat(3);
            info.type = (String)standardattr.get(new Long(rs.getLong(4)));
            if (info.type == null) info.type = "unknown";
            relatedness.add(info);
        }
        getrelatednessstm.close();
        if (relatedness.size() != 0) acrinfo.put(econcept, relatedness);

        //add child concepts
        getchildconceptsstm.setLong(1, cid);
        rs = getchildconceptsstm.executeQuery();
        UniqueConstraint cuc = new UniqueConstraint();
        while (rs.next()) {
            convertDomainConcept(rs.getLong(1), econcept, cuc, path + "\\" + name);
        }
        getchildconceptsstm.close();
    }

    public static void main(String[] args) throws SQLException, IOException {
        if (args.length < 2) {
            System.out.println("Usage: MOTtoCAF <filename> <lesson>");
            return;
        }
        new MOTtoCAF(args[0], args[1], args);
    }

    private String N2E(String s) {return (s==null?"":s);}

    //method to filter invalid UTF-8 characters
    private String FilterChars(String s) {
        byte[] bytes = s.getBytes();
        for (int i=0;i<bytes.length;i++) if (bytes[i]<0) bytes[i]=32;
        return new String(bytes);
    }

    private class UniqueConstraint {
        private Vector mem = new Vector();
        public UniqueConstraint() {}
        public String makeUnique(String s) {
            String res = s;
            int i = 1;
            while (mem.contains(res)) {
                res = s+i;
                i++;
            }
            mem.add(res);
            return res;
        }
    }

    private class RelatednessInfo {
        public long rcid = -1;
        public String label = "";
        public float weight = 1;
        public String type = "unknown";
        public RelatednessInfo() {}
    }
}