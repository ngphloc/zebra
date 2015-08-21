/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * XMLUtil.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.WOWDB;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import com.sun.xml.tree.*;
import com.sun.xml.parser.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import java.net.URL;

/**
 * This class provides some general functions used in processing
 * XML-documents.
 * //@see UserModelXMLDB
 * //@see ConceptXMLDB
 */
public class XMLUtil {
    /**
     * Loads an index (Hashtable) from a file.
     * @param root_ The file where the index is stored in.
     * @return The indextable.
     * @exception IOException If an internal error prevents the file
     *            from being read.
     */
    public static Hashtable loadIndex(File root_, String name) throws
                            IOException {
        File indexfile = new File(root_, name);
        Hashtable index = new Hashtable();
        if (indexfile.exists()) {
            LineNumberReader in = new LineNumberReader(new FileReader(indexfile));
            while (in.ready()) index.put(in.readLine(), new Long(in.readLine()));
            in.close();
        }
        return index;
    }

    /**
     * Saves an index (Hashtable) to a file.
     * @param root_ The file where the index is stored in.
     * @param index The indextable.
     * @exception IOException If an internal error prevents the file
     *            from being written.
     */
    public static void saveIndex(File root_, String name, Hashtable index) throws
                       IOException {
        File indexfile = new File(root_, name);
        PrintWriter out = new PrintWriter(new FileWriter(indexfile));
        Enumeration keys = index.keys();
        String key = null;
        while (keys.hasMoreElements()) {
            key = (String)keys.nextElement();
            out.println(key);
            out.println((Long)index.get(key));
        }
        out.close();
    }

    /**
     * Loads an index (Hashtable) from a file.
     * @param root_ The file where the index is stored in.
     * @return The indextable.
     * @exception IOException If an internal error prevents the file
     *            from being read.
     */
    public static Hashtable loadIndex(File root_) throws
                            IOException {
        return loadIndex(root_, "index");
    }

    /**
     * Saves an index (Hashtable) to a file.
     * @param root_ The file where the index is stored in.
     * @param index The indextable.
     * @exception IOException If an internal error prevents the file
     *            from being written.
     */
    public static void saveIndex(File root_, Hashtable index) throws
                       IOException {
        saveIndex(root_, "index", index);
    }

    /**
     * Tries to parse a XML document.
     * @param xmlfile The file that has to be parsed.
     * @return The parsed document.
     * @exception IOException If the document cannot be parsed.
     */
    public static XmlDocument getXML(File xmlfile) throws
                              IOException {
        XmlDocument doc = null;
        try {
            doc = XmlDocument.createXmlDocument(Resolver.createInputSource(xmlfile), true);
        } catch (SAXParseException e) {
            System.err.println("Error parsing document: "+xmlfile+ "\n"+", line " + e.getLineNumber ()+ ", uri " + e.getSystemId ());
            throw new IOException(e.toString());
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
        return doc;
    }

    /**
     * Tries to parse a XML document.
     * @param xmlfile The uri that has to be parsed.
     * @return The parsed document.
     * @exception IOException If the document cannot be parsed.
     */
    public static XmlDocument getXML(URL xmlfile) throws
                              IOException {
        XmlDocument doc = null;
        try {
            doc = XmlDocument.createXmlDocument(Resolver.createInputSource(xmlfile, false), true);
        } catch (SAXParseException e) {
            System.err.println("Error parsing document: "+xmlfile+ "\n"+", line " + e.getLineNumber ()+ ", uri " + e.getSystemId ());
            throw new IOException(e.toString());
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
        return doc;
    }

    /**
     * Tries to write a document to a outputstream.
     */
    public static void writeXML(OutputStream os, Document document) throws
    				   IOException {
		XmlDocument doc = (XmlDocument)document;
        doc.write(os);
	}

    /**
     * Returns the value of <code>Node</code>. This is done by
     * assuming that this node has one child that is a textnode. The
     * value of this textnode is returned.
     * @param node The node that has to be examined.
     * @return The value of the node.
     */
    public static String nodeValue(Node node) {
        if (node == null) return "";
        Node child = node.getFirstChild();
        if (child == null) return "";
        return child.toString();
    }

    /**
     * Conversion from null values to empty values.
     * @param s A string possibly null.
     * @return A string possibly empty.
     */
    public static String S2D(String s) {if (s == null) return ""; else return s;}

    /**
     * Conversion from empty values to null values.
     * @param s A string possibly empty.
     * @return A string possibly null.
     */
    public static String D2S(String s) {if (s.equals("")) return null; else return s;}
}