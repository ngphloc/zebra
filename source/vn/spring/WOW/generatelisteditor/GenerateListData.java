/*

    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 

*/
/**
 * GenerateListData.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.generatelisteditor;

import vn.spring.WOW.util.*;

// WOW
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.Utils.*;

// Read in xml-File.
import java.io.*;
import java.io.IOException;
// data Types ...
import java.util.Vector;

import javax.swing.JOptionPane;

// XML utils
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

// DOM definition en Exceptions
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import java.net.*;

public class GenerateListData
{

  private String xmlfile;
  private String location;
  private String root;
  public static String coursename;
  private final String defaultConcept = "defaultConcept.xml";
  public static Concept rootConcept;
  private static Document document;
  private static String author;
  public static Node authorNode;
/*David*/public static Vector concepts = new Vector();
/*David*/public static AGraph agraph = new AGraph();

    public GenerateListData ()
    {
        concepts.clear();
        System.gc();
        xmlfile = "";
        location = "";
        root = "";
        coursename = "noname";
        agraph = new AGraph();
    }

    public GenerateListData (String filename, String authortoolstr)
    {
        this.xmlfile = filename;
        this.root = filename.substring(0, filename.indexOf(authortoolstr));
        this.location = root + authortoolstr;
        int index = filename.lastIndexOf("/");
        GenerateListData.coursename = filename.substring(index+1, filename.lastIndexOf("."));
        agraph = new AGraph();
        //concepts.clear();
        System.gc();
    }

        public void loadData() {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            factory.setNamespaceAware(true);

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();

                builder.setErrorHandler( new org.xml.sax.ErrorHandler() {
                    // ignore fatal errors (an exception is guaranteed)
                    public void fatalError(SAXParseException exception) throws SAXException {
                            }

                            // treat validation errors as fatal
                            public void error(SAXParseException e) throws SAXParseException
                            {
                                    throw e;
                            }

                            // dump warnings too
                            public void warning(SAXParseException err) throws SAXParseException
                            {
                                    System.out.println("** Warning"
                                            + ", line " + err.getLineNumber()
                                            + ", uri " + err.getSystemId());
                                    System.out.println("   " + err.getMessage());
                            }
                    }
                );
                //document = builder.parse(xmlfile);
                document = vn.spring.WOW.WOWDB.XMLUtil.getXML(new URL(xmlfile));
/*David*/       loadConceptList(document);
/*David*/       loadAGraph();

                Document rootDoc = vn.spring.WOW.WOWDB.XMLUtil.getXML(new URL(location+defaultConcept));
                Node rnode = ((Element)rootDoc.getDocumentElement());
                rootConcept = KoenFormat.getKoenConcept(rnode);
/*            } catch (SAXException sxe) {
                    // Error generated during parsing
                    System.out.println("GenerateListData: loadData: SAXException");
                    Exception  x = sxe;
                    if (sxe.getException() != null)
                            x = sxe.getException();
                    x.printStackTrace();
*/
            } catch (ParserConfigurationException pce) {
              System.out.println("GenerateListData: loadData: parserconfigurationexception");
                            // Parser with specified options can't be built
                            pce.printStackTrace();
                } catch (IOException ioe) {
                            // I/O error
                    ioe.printStackTrace();
            }

        }

   public void save(String conceptFile, String created) {
        try {
            URL url = new URL(root+"SaveList");
            HttpURLConnection uc = (HttpURLConnection)url.openConnection();
            uc.setDoInput(true);
            uc.setDoOutput(true);
            uc.setRequestProperty("Filename", conceptFile);
            uc.setRequestProperty("Author", author);
            uc.setRequestProperty("Created", created);
            OutputStream output = uc.getOutputStream();

/*          XMLSerializer ser = new XMLSerializer(output,new OutputFormat("xml", "UTF-8", true));
            ser.serialize(document);
*/
            vn.spring.WOW.WOWDB.XMLUtil.writeXML(output, saveConceptList());

            output.close();
            if (uc.getResponseCode()==200) {
            	JOptionPane.showMessageDialog(null,
                "Concept list has been saved successfully");
            } else {
                JOptionPane.showMessageDialog(null,
                "An error occured while saving the concept list",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            }
        } catch (MalformedURLException e) {System.out.println(e);
        } catch (IOException ioe) {System.out.println(ioe);}
        catch (Exception e) {System.out.println(e);}


    }

    public void setAuthor(String login) {
        author = login;
    }

    // The following methods are used to replace a concept name

    private static boolean idPart(char c) {
        return (Character.isUnicodeIdentifierPart(c) || c == '.');
    }

    private static String replaceCNexpr(String expr, String oldName, String newName, boolean search) {
        StringBuffer result = new StringBuffer(expr);
        String sub;
        int start = 0;
        int stop;
        DotString ds;

        while (result.indexOf(oldName, start) != -1) {
            start = result.indexOf(oldName, start);
            stop = start+oldName.length();
            while ((start > 0) && ( idPart(result.charAt(start-1)) )) start--;
            while ((stop < result.length()) && ( idPart(result.charAt(stop)) )) stop++;

            sub = result.substring(start, stop);
            if (sub.equals(oldName)) {
                result.replace(start, stop, newName);
            } else {
                ds = new DotString(sub);
                ds.set(ds.size()-1, null);
                sub = ds.toString();
                if ((sub.equals(oldName)) && (search))
                    result.replace(start, start+oldName.length(), newName);
            }
            start=stop;
        }
        return result.toString();
    }

    private static void replaceAssignments(Vector assignments, String oldName, String newName, boolean search) {
        Assignment assign;
        String expr;
        for (int i=0;i<assignments.size();i++) {
            assign = (Assignment)assignments.get(i);
            expr = assign.getVariable();
            assign.setVariable(replaceCNexpr(expr, oldName, newName, search));
            expr = assign.getExpression();
            assign.setExpression(replaceCNexpr(expr, oldName, newName, search));
        }
    }

    private static void replaceActions(Vector actions, String oldName, String newName, boolean search) {
        Action action;
        String expr;
        for (int i=0;i<actions.size();i++) {
            action = (Action)actions.get(i);
            expr = action.getCondition();
            action.setCondition(replaceCNexpr(expr, oldName, newName, search));
            replaceAssignments(action.getTrueStatements(), oldName, newName, search);
            replaceAssignments(action.getFalseStatements(), oldName, newName, search);
        }
    }

    private static void replaceAttributes(Vector attrs, String oldName, String newName, boolean search) {
        Attribute attr;
        String expr;
        for (int i=0;i<attrs.size();i++) {
            attr = (Attribute)attrs.get(i);
            expr = attr.getDefault();
            attr.setDefault(replaceCNexpr(expr, oldName, newName, search));
            replaceActions(attr.getActions(), oldName, newName, search);
        }
    }

    private static void updateAGraphNames(Concept concept, String oldName) {
        Attribute attr;
        for (int i=0;i<concept.getAttributes().size();i++) {
            attr = (Attribute)concept.getAttributes().get(i);
            setGraphAttribute(concept.getName(), oldName+"."+attr.getName(), attr);
        }
    }

    private static void replaceNames(String oldName, String newName, boolean search) {
        Concept concept;
        for (int i=0;i<concepts.size();i++) {
            concept = (Concept)concepts.get(i);
            if ((search) && concept.getName().equals(newName)) updateAGraphNames(concept, oldName);
            replaceAttributes(concept.getAttributes(), oldName, newName, search);
        }
    }

    /**
     * This method will replace all occurences of the old name of
     * a concept to its new name.
     */
    public static void replaceConceptNames(String oldName, String newName) {
        replaceNames(oldName, newName, true);
    }

    /**
     * This method will replace all occurences of the old name of
     * an attribute to its new name.
     */
    public static void replaceAttributeNames(String oldName, String newName) {
        replaceNames(oldName, newName, false);
    }

    public static boolean containsConcept(String cname) {
        boolean result = false;
        int i;
        for (i=0;i<concepts.size();i++) {
            result |= ((Concept)concepts.get(i)).getName().equals(cname);
        }
        return result;
    }

    public static boolean containsAttribute(String cname, String attrname) {
        boolean result = false;
        int i; int j; Concept c;
        for (i=0;i<concepts.size();i++) {
            c = (Concept)concepts.get(i);
            if (c.getName().equals(cname)) {
                for (j=0;j<c.getAttributes().size();j++) {
                    result |= ((Attribute)c.getAttributes().get(j)).getName().equals(attrname);
                }
            }
        }
        return result;
    }

/*David*/
    /*
     * Converts the DOM model to a list of 'concept' data objects.
     */
    private void loadConceptList(Document doc) {
        // Empty the list
        concepts.clear();
        System.gc();

        // Load the concepts from the DOM model
        NodeList nodelist = ((Element)doc.getDocumentElement()).getChildNodes();
        Node child = null;
        vn.spring.WOW.datacomponents.Concept concept = null;
        for (int i=0; i<nodelist.getLength(); i++) {
            child = nodelist.item(i);
            if (child.getNodeName().equals("concept")) {
                concept = KoenFormat.getKoenConcept(child);
                concepts.add(concept);
            }
        }
    }

/*David*/
    /*
     * Does the reverse of "loadConceptList".
     */
    private Document saveConceptList() {
        // Set up a new document
        com.sun.xml.tree.XmlDocument doc = new com.sun.xml.tree.XmlDocument();
        doc.setSystemId("conceptList");
        doc.setDoctype(null, "../generatelist4.dtd", null);

        // Create the basic structure
        Element econceptlist = doc.createElement("conceptList"); doc.appendChild(econceptlist);
        Element ename = doc.createElement("name"); econceptlist.appendChild(ename);
        ename.appendChild(doc.createTextNode(coursename));

        // Add the concepts
        vn.spring.WOW.datacomponents.Concept concept = null;
        Node node = null;
        for (int i=0;i<concepts.size();i++) {
            concept = (vn.spring.WOW.datacomponents.Concept)concepts.get(i);
            node = KoenFormat.getKoenXML(concept, doc);
            doc.getDocumentElement().appendChild(node);
        }

        return doc;
    }

    /**
     * Loads the activation graph from the concept list.
     */
    private void loadAGraph() {
        agraph = new AGraph();
        int i; int j; Concept c;
        for (i=0;i<concepts.size();i++) {
            c = (Concept)concepts.get(i);
            for (j=0;j<c.getAttributes().size();j++)
                setGraphAttribute(c.getName(), null, (Attribute)c.getAttributes().get(j));
        }
    }

    /**
     * Sets an attribute in the activation graph.
     */
    public static void setGraphAttribute(String cname, String oldName, Attribute attr) {
        int i; int j;
        AAttribute aattr = new AAttribute(cname+"."+attr.getName());
        AAction aaction;
        Action action;
        for (i=0;i<attr.getActions().size();i++) {
            action = (Action)attr.getActions().get(i);
            aaction = new AAction(aattr, action.getTrigger());
            aaction.condition = action.getCondition();
            for (j=0;j<action.getTrueStatements().size();j++)
                addReferenceAttribute(aaction, (Assignment)action.getTrueStatements().get(j));
            for (j=0;j<action.getFalseStatements().size();j++)
                addReferenceAttribute(aaction, (Assignment)action.getFalseStatements().get(j));
            aattr.actions.add(aaction);
        }
        agraph.setAttribute(oldName, aattr);
    }

    private static void addReferenceAttribute(AAction aaction, Assignment assign) {
        aaction.attributes.put(assign.getVariable(), null);
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