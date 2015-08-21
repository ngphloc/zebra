/*

    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * XMLProfileDB.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.WOWDB;

import com.sun.xml.tree.*;

import java.io.*;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.exceptions.*;

import org.w3c.dom.*;


/**
 * This interface must be implemented by a class to function as a
 * user profile storage means.
 */
public class XMLProfileDB implements ProfileDB {
    //the root directory of this database
    private File root = null;

    //the dtd of the XML format used by the interface
    private File dtd = null;

    //the index table for this database
    private Hashtable index = null;

    /**
     * Creates a new XML profile database interface.
     */
    public XMLProfileDB(File proot) throws DatabaseException {
        WOWStatic.checkNull(proot, "root");
        try {
            if (!proot.exists()) {
                proot.mkdir();
            }

            //set and create the directory if necessary
            root = new File(proot, "profile");

            if (!root.exists()) {
                root.mkdir();
            }

            //set and create the dtd if necessary
            dtd = new File(root, "profile.dtd");

            if (!dtd.exists()) {
                StringBuffer outstr = new StringBuffer();
                outstr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                outstr.append("<!ELEMENT profile (record)*>\n\n");
                outstr.append("<!ELEMENT record (key, value, firsttimeupdated)>\n");
                outstr.append("<!ELEMENT key (#PCDATA)>\n");
                outstr.append("<!ELEMENT value (#PCDATA)>\n");
                outstr.append("<!ELEMENT firsttimeupdated (#PCDATA)>\n");

                FileWriter dtdout = new FileWriter(dtd);
                dtdout.write(outstr.toString());
                dtdout.close();
            }

            //load the index file
            index = XMLUtil.loadIndex(root);
        } catch (IOException e) {
            throw new DatabaseException("unable to create XMLProfileDB in " + root + ": " + e.getMessage());
        }
    }

    /**
     * Creates a new user profile and returns the id.
     */
    public synchronized long createProfile() throws DatabaseException {
        long id = WOWStatic.getUID();
        //create the file object
        File profilexml = new File(root, (new Long(id)).toString());

        if (profilexml.exists()) {
            throw new DatabaseException("unable to create profile: invalid id");
        }

        //create the in-memory document
        XmlDocument doc = new XmlDocument();
        doc.appendChild(doc.createElement("profile"));
        doc.setSystemId("profile");
        doc.setDoctype(null, "profile.dtd", null);

        //try to save it to the disk
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(profilexml));
            doc.write(pw);
            pw.close();
        } catch (IOException e) {
            throw new DatabaseException("unable to create profile: " + e.getMessage());
        }

        //return the new identifier
        return id;
    }

    /**
     * Loads a user profile with the specified id.
     */
    public synchronized Profile getProfile(long id) throws DatabaseException, InvalidProfileException {
        //try to load the document in memory
        File profilexml = new File(root, String.valueOf(id));

        if (!profilexml.exists()) {
            throw new InvalidProfileException("profile does not exist: " + id);
        }

        XmlDocument doc = null;

        try {
            doc = XMLUtil.getXML(profilexml);
        } catch (IOException e) {
            throw new DatabaseException("unable to read profile: " + id);
        }

        //create the profile
        Profile profile = new Profile();
        profile.id = id;
        Hashtable values = profile.getValues();
        AttributeValue value = null;
        NodeList list = doc.getDocumentElement().getChildNodes();
        NodeList childlist = null;

        for (int i = 0; i < list.getLength(); i++) {
            childlist = list.item(i).getChildNodes();
            String name = XMLUtil.nodeValue(childlist.item(0));
            String val = XMLUtil.nodeValue(childlist.item(1));
            value = new AttributeValue();
            value.setFirstTimeUpdated( (new Boolean(XMLUtil.nodeValue(childlist.item(2)))).booleanValue() );
            value.setValue(val);
            value.clearUpdated();
            values.put(name, value);
        }

        return profile;
    }

    /**
     * Saves a user profile with the specified id.
     */
    public synchronized void setProfile(long id, Profile profile) throws
           DatabaseException, InvalidProfileException {
        WOWStatic.checkNull(profile, "profile");

        //try to create the file object
        File profilexml = new File(root, (new Long(id)).toString());

        if (!profilexml.exists()) {
            throw new InvalidProfileException("profile does not exist: " + id);
        }

        //create the in memory xml document
        XmlDocument doc = new XmlDocument();
        Element eprofile = doc.createElement("profile");

        for (Enumeration keys = profile.getValues().keys(); keys.hasMoreElements();) {
            String key = (String) keys.nextElement();
            AttributeValue value = (AttributeValue) profile.getValues().get(key);
            value.clearUpdated();
            value.clearNew();

            eprofile.appendChild(createRecordNode(doc, key, value));

            if (key.equals("personal.id")) {
                index.put(value.getValue(), new Long(id));
                try {
                    XMLUtil.saveIndex(root, index);
                } catch (IOException e) {
                    throw new DatabaseException("unable to write index: " + e.getMessage());
                }
            }
        }

        doc.appendChild(eprofile);
        doc.setSystemId("profile");
        doc.setDoctype(null, "profile.dtd", null);

        //try to save it to the disk
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(profilexml));
            doc.write(pw);
            pw.close();
        } catch (IOException e) {
            throw new DatabaseException("unable to create profile: " + e.getMessage());
        }
    }

    //creates a record node based on the specified key and value
    private Node createRecordNode(Document doc, String key, AttributeValue value) {
        Element result = doc.createElement("record");

        Element ekey = doc.createElement("key");
        result.appendChild(ekey);
        ekey.appendChild(doc.createTextNode(key));

        Element evalue = doc.createElement("value");
        result.appendChild(evalue);
        evalue.appendChild(doc.createTextNode(value.getValue()));

        Element eftu = doc.createElement("firsttimeupdated");
        result.appendChild(eftu);
        eftu.appendChild(doc.createTextNode((new Boolean(value.isFirstTimeUpdated())).toString()));

        return result;
    }

    /**
     * Finds the profile with a given login and return its id.
     */
    public synchronized long findProfile(String value) throws
           DatabaseException {
        if (index.containsKey(value)) return ((Long)index.get(value)).longValue();
        return 0;
    }

    /**
     * Return the list of all profile id's
     */
    public Vector getProfileList() throws
           DatabaseException {
        return new Vector(index.keySet());
    }

    /**
     * Removes the user profile with the specified id.
     */
    public void removeProfile(long id) throws
           DatabaseException {
        File profilexml = new File(root, (new Long(id)).toString());
        profilexml.delete();
        index.values().remove(new Long(id));
        try {
            XMLUtil.saveIndex(root, index);
        } catch (IOException e) {
            throw new DatabaseException("unable to write index: " + e.getMessage());
        }
        
    }
}