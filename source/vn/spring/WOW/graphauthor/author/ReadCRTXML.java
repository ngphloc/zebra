/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ReadCRTXML.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.net.*;

import java.util.*;

import javax.swing.*;

import org.apache.xerces.parsers.DOMParser;

import org.w3c.dom.*;

import org.xml.sax.*;


/**
 * ReadCRTXML Read the CRTs into memory.
 *
 */
public class ReadCRTXML {
    private CRTConceptRelationType CRT;
    public URL base;

    public ReadCRTXML(String filename, URL home) {
        AuthorSTATIC.CRTList = new LinkedList();
        base = home;
        try {
            Hashtable reqinfo = new Hashtable();
            reqinfo.put("name", "authordir");
            reqinfo.put("dir", "crt");
            Hashtable resinfo = vn.spring.WOW.graphauthor.GraphAuthor.GraphAuthor.getExecRequest(reqinfo, base);
            Vector filenames = (Vector)resinfo.get("files");
            if (filenames == null) return;
            for (int i=0;i<filenames.size();i++) {
                String fn = (String)filenames.get(i);
                if (fn.endsWith(".wow")) {
                    String stemp = "crt/"+fn.substring(0,fn.length()-4);
                    CRT = new CRTConceptRelationType();
                    StartReadCRTXML(stemp + ".wow");
                    StartReadCRTXML(stemp + ".author");
                    if (CRT.properties.concept_hierarchy.booleanValue() == true) AuthorSTATIC.trel = CRT;
                    AuthorSTATIC.CRTList.add(CRT);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ReadName(Node n) {
        // reads the name from the dom model into the CRT structure
        Node nod;
        nod = n.getFirstChild();
        CRT.name = nod.getNodeValue().trim();
    }

    private void ReadColor(Node n) {
        Node nod;
        nod = n.getFirstChild();
        CRT.color = nod.getNodeValue().trim();
    }

    private void ReadStyle(Node n) {
        Node nod;
        nod = n.getFirstChild();
        CRT.style = nod.getNodeValue().trim();
    }

    private void ReadProperties(Node n) {
        Node nod;
        NamedNodeMap nmap;
        nod = n;


        // default values
        CRT.properties.acyclic = Boolean.FALSE;
        CRT.properties.concept_hierarchy = Boolean.FALSE;
        CRT.properties.unary = Boolean.FALSE;

        nmap = nod.getAttributes();

        if (nmap != null) {
            // read attributes
            int len = nmap.getLength();
            Attr attr;

            for (int i = 0; i < len; i++) {
                attr = (Attr) nmap.item(i);

                if (attr.getNodeName().equals("acyclic")) {
                    CRT.properties.acyclic = new Boolean(attr.getNodeValue());
                }

                if (attr.getNodeName().equals("unary")) {
                    CRT.properties.unary = new Boolean(attr.getNodeValue());
                }

                if (attr.getNodeName().equals("concept_hierarchy")) {
                    CRT.properties.concept_hierarchy = new Boolean(
                                                               attr.getNodeValue());
                }
            }
        }
    }

    private void ReadAction(CRTAction act, Node n) {
        Node nod = n;
        NamedNodeMap nmap;
        nmap = nod.getAttributes();
        act.combination = "NONE";

        if (nmap != null) {
            // read attributes
            int len = nmap.getLength();
            Attr attr;

            for (int i = 0; i < len; i++) {
                attr = (Attr) nmap.item(i);

                if (attr.getNodeName().equals("combination")) {
                    act.combination = attr.getNodeValue().trim();
                }
            }
        }

        Node nd;

        nod = nod.getFirstChild();
        nod = nod.getNextSibling();
        nd = nod.getFirstChild();

        if (nd != null) {
            act.conceptName = nd.getNodeValue().trim();
        } else {
            act.conceptName = "";
        }

        nod = nod.getNextSibling();
        nod = nod.getNextSibling();

        nd = nod.getFirstChild();

        if (nd != null) {
            act.attributeName = nd.getNodeValue().trim();
        } else {
            act.attributeName = "";
        }

        nod = nod.getNextSibling();
        nod = nod.getNextSibling();
        nd = nod.getFirstChild();
        act.expression = nd.getNodeValue().trim();
    }

    private void ReadTrueActions(CRTGenerateListItem glitem, Node n) {
        Node nod = n.getFirstChild();
        CRTAction act;
        nod = nod.getNextSibling();

        while (nod != null) {
            if (nod.getNodeName().equals("action")) {
                act = new CRTAction();
                ReadAction(act, nod);
                glitem.trueActions.actionList.add(act);
            }

            nod = nod.getNextSibling();
        }
    }

    private void ReadFalseActions(CRTGenerateListItem glitem, Node n) {
        Node nod = n.getFirstChild();
        CRTAction act;
        nod = nod.getNextSibling();

        while (nod != null) {
            if (nod.getNodeName().equals("action")) {
                act = new CRTAction();
                ReadAction(act, nod);
                glitem.falseActions.actionList.add(act);
            }

            nod = nod.getNextSibling();
        }
    }

    private void ReadGenerateListItem(Node n) {
        CRTGenerateListItem gli = new CRTGenerateListItem();
        gli.location = "default";
        gli.propagating = Boolean.FALSE;

        Node nod = n;
        NamedNodeMap nmap = nod.getAttributes();

        if (nmap != null) {
            // read attributes
            int len = nmap.getLength();
            Attr attr;

            for (int i = 0; i < len; i++) {
                attr = (Attr) nmap.item(i);

                if (attr.getNodeName().equals("location")) {
                    gli.location = attr.getNodeValue();
                }

                if (attr.getNodeName().equals("isPropagating")) {
                    gli.propagating = new Boolean(attr.getNodeValue());
                }
            }

            nod = nod.getFirstChild();
            nod = nod.getNextSibling();
            gli.requirement = nod.getFirstChild().getNodeValue();

            //while van maken
            while (nod != null) {
                nod = nod.getNextSibling();
                nod = nod.getNextSibling();

                if ((nod != null) &&
                        (nod.getNodeName().equals("trueActions"))) {
                    ReadTrueActions(gli, nod);
                }

                if ((nod != null) &&
                        (nod.getNodeName().equals("falseActions"))) {
                    ReadFalseActions(gli, nod);
                }
            }
        }


        // add object to list
        CRT.listItem.generateListItemList.add(gli);
    }

    private void ReadSetDefault(Node n) {
        CRTSetDefault sdef = new CRTSetDefault();
        Node nod = n;
        NamedNodeMap nmap = nod.getAttributes();

        if (nmap != null) {
            // read attributes
            int len = nmap.getLength();
            Attr attr;

            for (int i = 0; i < len; i++) {
                attr = (Attr) nmap.item(i);

                if (attr.getNodeName().equals("location")) {
                    sdef.location = attr.getNodeValue();
                }

                if (attr.getNodeName().equals("combination")) {
                    sdef.combination = attr.getNodeValue();
                }
            }
        }

        nod = nod.getFirstChild();
        sdef.setdefault = nod.getNodeValue();
        CRT.listItem.setDefaultList.add(sdef);
    }

    private void ReadListItems(Node n) {
        Node nod = n.getFirstChild();
        nod = nod.getNextSibling();

        while (nod != null) {
            if (nod.getNodeName().equals("setdefault")) {
                ReadSetDefault(nod);
            }

            if (nod.getNodeName().equals("generateListItem")) {
                ReadGenerateListItem(nod);
            }

            nod = nod.getNextSibling();
            nod = nod.getNextSibling();
        }
    }

    private void StartReadCRTXML(String xmlfile) {
        //    CRT = new CRTConceptRelationType();
        DOMParser p = new DOMParser();

        try {
            p.setFeature("http://xml.org/sax/features/validation", true);
        } catch (SAXException e) {
            System.out.println("error in setting up parser feature");
        }

        // sets error handling
        error_handler eh = new error_handler();
        p.setErrorHandler(eh);

        try {
            String path = base.getPath();
            String pathttemp = path.substring(1, path.length());
            int index = pathttemp.indexOf("/");
            index++;

            String dirname = path.substring(0, index);

            if (dirname.equals("/graphAuthor")) {
                dirname = "";
            }

            URL url = new URL("http://" + base.getHost() + ":" +
                              base.getPort() + dirname +
                              "/servlet/authorservlets.GetFile?fileName=" +
                              xmlfile);

            p.parse(url.toString());
        } catch (org.xml.sax.SAXParseException e) {
            System.out.println(e.toString() + "  Line: " + e.getLineNumber() +
                               "Column: " + e.getColumnNumber());
        } catch (org.xml.sax.SAXNotRecognizedException e) {
            System.out.println("Error SAXNotRecognizedException");
        } catch (org.xml.sax.SAXException e) {
            System.out.println("Error SAXException");
        } catch (Exception e) {
            System.out.println("error while reading xml file! ");
        }

        ;

        Document doc = p.getDocument();
        Node n = doc.getDocumentElement();
        n = n.getFirstChild();
        n = n.getNextSibling();

        while (n != null) {
            if (n.getNodeName().equals("name")) {
                ReadName(n);
            }

            if (n.getNodeName().equals("color")) {
                ReadColor(n);
            }

            if (n.getNodeName().equals("style")) {
                ReadStyle(n);
            }

            if (n.getNodeName().equals("properties")) {
                ReadProperties(n);
            }

            if (n.getNodeName().equals("listitems")) {
                ReadListItems(n);
            }

            n = n.getNextSibling();
        }
    }

    void StartReadCRTXMLauthor(String xmlfile) {
        DOMParser p = new DOMParser();

        try {
            String path = base.getPath();
            String pathttemp = path.substring(1, path.length());
            int index = pathttemp.indexOf("/");
            index++;

            String dirname = path.substring(0, index);

            if (dirname.equals("/graphAuthor")) {
                dirname = "";
            }

            URL url = new URL("http://" + base.getHost() + ":" +
                              base.getPort() + dirname +
                              "/servlet/authorservlets.GetFile?fileName=" +
                              xmlfile + ".author");

            p.parse(url.toString());
        } catch (Exception e) {
            System.out.println("Error while parsing: " + xmlfile);
        }

        Document doc = p.getDocument();
        Node n = doc.getDocumentElement();
        n = n.getFirstChild();
        n = n.getNextSibling();

        while (n != null) {
            if (n.getNodeName().equals("name")) {
                ReadName(n);
            }

            if (n.getNodeName().equals("color")) {
                ReadColor(n);
            }

            if (n.getNodeName().equals("style")) {
                ReadStyle(n);
            }

            if (n.getNodeName().equals("properties")) {
                ReadProperties(n);
            }

            if (n.getNodeName().equals("listitems")) {
                ReadListItems(n);
            }

            n = n.getNextSibling();
        }

        // AuthorSTATIC.CRTList.add(CRT);
    }
}