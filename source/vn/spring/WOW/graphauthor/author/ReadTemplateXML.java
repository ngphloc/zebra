/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ReadTemplateXML.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.net.*;

import java.util.Hashtable;
import java.util.Vector;
import java.util.LinkedList;
import javax.swing.*;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.*;


/**
 * ReadAttributeXML: Reads the attributeXML into memory.
 *
 */
public class ReadTemplateXML {
    public URL home;

    public ReadTemplateXML(String filename, URL base) {
        AuthorSTATIC.templateList = new LinkedList();
        home = base;
        try {
            Hashtable reqinfo = new Hashtable();
            reqinfo.put("name", "authordir");
            reqinfo.put("dir", "templates");
            Hashtable resinfo = vn.spring.WOW.graphauthor.GraphAuthor.GraphAuthor.getExecRequest(reqinfo, base);
            Vector filenames = (Vector)resinfo.get("files");
            if (filenames == null) return;
            for (int i=0;i<filenames.size();i++) {
                String fn = (String)filenames.get(i);
                if (fn.endsWith(".ct")) ReadFromFile("templates/"+fn);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    public void ReadFromFile(String xmlfile) {
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
            String path = home.getPath();
            String pathttemp = path.substring(1, path.length());
            int index = pathttemp.indexOf("/");
            index++;

            String dirname = path.substring(0, index);

            if (dirname.equals("/graphAuthor")) {
                dirname = "";
            }

            URL url = new URL("http://" + home.getHost() + ":" +
                              home.getPort() + dirname +
                              "/servlet/authorservlets.GetFile?fileName=" +
                              xmlfile);

            p.parse(url.toString());
        } catch (Exception e) {
            System.out.println("url error");
        }

        ConceptTemplate cTemplate = new ConceptTemplate();

        Document doc = p.getDocument();
        Node node = doc.getDocumentElement();

        org.w3c.dom.Node tNode = node.getFirstChild();

        while (tNode != null) {
            if (tNode.getNodeName().equals("name")) {
                cTemplate.name = tNode.getFirstChild().getNodeValue();
               }

            if (tNode.getNodeName().equals("attributes")) {
                Node cNode = tNode.cloneNode(true);
                      cNode = cNode.getFirstChild();

                while (cNode != null) {
                    if (cNode.getNodeName().equals("attribute")) {
                        WOWOutAttribute attribute = this.ReadAttribute(cNode);
                       cTemplate.attributes.add(attribute);
                    }

                    cNode = cNode.getNextSibling();
                }
            }

            if (tNode.getNodeName().equals("hasresource")) {
                cTemplate.hasresource = tNode.getFirstChild().getNodeValue();
               }

            if (tNode.getNodeName().equals("concepttype")) {
                cTemplate.concepttype = tNode.getFirstChild().getNodeValue();
               }

            if (tNode.getNodeName().equals("conceptrelations")) {
                Node cNode = tNode.cloneNode(true);
               cNode = cNode.getFirstChild();

                while (cNode != null) {
                    if (cNode.getNodeName().equals("conceptrelation")) {
                        templateConceptRelation tcr = this.ReadConceptRelations(
                                                              cNode);
                        cTemplate.conceptRelations.put(tcr.name, tcr.label);
                        }

                    cNode = cNode.getNextSibling();
                }
            }

            tNode = tNode.getNextSibling();
        }

        AuthorSTATIC.templateList.add(cTemplate);
    }

    public templateConceptRelation ReadConceptRelations(Node n) {
        templateConceptRelation tcr = new templateConceptRelation();
        n = n.getFirstChild();

        while (n != null) {
            if (n.getNodeName().equals("name")) {
                tcr.name = n.getFirstChild().getNodeValue();
            }

            if (n.getNodeName().equals("label")) {
                try {
                    tcr.label = n.getFirstChild().getNodeValue();
                } catch (Exception e) {
                    tcr.label = "";
                }
            }

            n = n.getNextSibling();
        }

        return tcr;
    }

    public WOWOutAttribute ReadAttribute(Node n) {
        WOWOutAttribute att = new WOWOutAttribute();

        n = n.getFirstChild();

        while (n != null) {
            if (n.getNodeName().equals("name")) {
                att.name = n.getFirstChild().getNodeValue();
            }

            if (n.getNodeName().equals("type")) {
                att.type = n.getFirstChild().getNodeValue();
            }

            if (n.getNodeName().equals("description")) {
                try {
                    att.description = n.getFirstChild().getNodeValue();
                } catch (Exception e) {
                    att.description = "";
                }
            }

            // PDB: do not ignore default
            if (n.getNodeName().equals("default")) {
            att.setDefaultList = new CRTSetDefault();
            try {
                    att.setDefaultList.setdefault = n.getFirstChild().getNodeValue();
                } catch (Exception e) {
                    att.setDefaultList.setdefault = "";
                }
            }

            if (n.getNodeName().equals("isPersistent")) {
                att.isPersistent = new Boolean(n.getFirstChild().getNodeValue());
            }

            if (n.getNodeName().equals("isSystem")) {
                att.isSystem = new Boolean(n.getFirstChild().getNodeValue());
            }

            if (n.getNodeName().equals("isChangeable")) {
                att.isChangeable = new Boolean(n.getFirstChild().getNodeValue());
            }

            n = n.getNextSibling();
        }

        return att;

        // AuthorSTATIC.attributeList.add(att);
    }

    public static void main(String[] args) {
    }

    public class templateConceptRelation {
        public String name;
        public String label;
        public String hasresource;
        public String concepttype;

        public templateConceptRelation() {
            name = "";
            label = "";
            hasresource = "";
            concepttype = "";
        }
    }
}