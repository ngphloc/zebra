/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is free software


*/
/**
 * ReadStrategyXML.java 1.0, September 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import java.io.*;

import java.net.*;

import java.util.*;

import javax.swing.*;

import org.apache.xerces.parsers.DOMParser;

import org.w3c.dom.*;

import org.xml.sax.*;


/**
 * ReadStrategyXML Reads the strategy into memory.
 *
 */
public class ReadStrategyXML {
    private Strategy strategy;
    public URL base;

    public ReadStrategyXML(String filename, URL home) {

        DOMParser p = new DOMParser();

        try {
            p.setFeature("http://xml.org/sax/features/validation", true);
        } catch (SAXException e) {
            System.out.println("error in setting up parser feature");
        }

        // sets error handling
        error_handler eh = new error_handler();
        p.setErrorHandler(eh);

        base = home;

        int dirTestW = filename.indexOf("/");

        if (dirTestW != -1) {
            System.out.println("ReadStrategyXML invalid input");
        }

        try {
            String path = home.getPath();
            String pathttemp = path.substring(1, path.length());
            int index = pathttemp.indexOf("/");
            index++;

            String dirname = path.substring(0, index);

            if (dirname.equals("/graphAuthor")) {
                dirname = "";
            }
            String spec = "http://" + home.getHost() + ":" +
            				home.getPort() + dirname +
            				"/servlet/authorservlets.GetStrategyFile?fileName=" +
            				filename + "&userName=" + AuthorSTATIC.authorName;
            URL url = new URL(spec);
            p.parse(url.toString());
        } catch (org.xml.sax.SAXParseException e) {
            System.out.println(e.toString() + "  Line: " + e.getLineNumber() +
                               "Column: " + e.getColumnNumber());
        } catch (org.xml.sax.SAXNotRecognizedException e) {
            System.out.println("Error SAXNotRecognizedException");
        } catch (org.xml.sax.SAXException e) {
            System.out.println("Error SAXException");
        } catch (IOException exp) {
            JOptionPane.showMessageDialog(null, "File unknown!", "Error",
                                          JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.out.println("error while reading xml file! ");
		}
        strategy = new Strategy();
        Document doc = p.getDocument();
        Node n = doc.getDocumentElement();
        while (n != null) {
            if (n.getNodeName().equals("strategy")) {
                ReadStrategy(n);
                n = n.getFirstChild();
            }

            if (n.getNodeName().equals("description")) {
                strategy.description = n.getFirstChild().getNodeValue().trim();
            }

            if (n.getNodeName().equals("if")) {
				StrategyIfStatement ifStatement = new StrategyIfStatement();
                ReadIf(ifStatement, n);
            }
        	n = n.getNextSibling();
        	
        }
		AuthorSTATIC.strategyInfoList.add(strategy);
    }

    private void ReadStrategy(Node n) {
        // reads the name from the dom model into the CRT structure
        Node nod = n;
        NamedNodeMap nmap = nod.getAttributes();

        if (nmap != null) {
            // read attributes
            int len = nmap.getLength();
            Attr attr;

            for (int i = 0; i < len; i++) {
                attr = (Attr) nmap.item(i);

                if (attr.getNodeName().equals("name")) {
                    strategy.name = attr.getNodeValue().trim();
                }
            }
        }
    }

    private void ReadIf(StrategyIfStatement ifStatement, Node n) {

        Node nod;
        nod = n.getFirstChild();
		nod = nod.getNextSibling();
		while (nod != null) {
        	if (nod.getNodeName().equals("condition")) {
        		ifStatement.condition = nod.getFirstChild().getNodeValue().trim();
			}
			if (nod.getNodeName().equals("then")) {
				StrategyThenStatement thenStatement = new StrategyThenStatement();
				ReadThen(thenStatement, nod);
				ifStatement.thenStatement = thenStatement;
			}
			/*if (nod.getNodeName().equals("else")) {
				System.out.println("else");
				StrategyElseStatement elseStatement = new StrategyElseStatement();
				ReadElse(elseStatement, nod);
				ifStatement.elseList.add(elseStatement);
			}*/
			nod = nod.getNextSibling();
		}
		strategy.ifStatements.add(ifStatement);
    }


    private void ReadThen(StrategyThenStatement thenStatement, Node n) {
        // reads the name from the dom model into the CRT structure
        Node nod;
        nod = n.getFirstChild();
		nod = nod.getNextSibling();
		while (nod != null) {
			if (nod.getNodeName().equals("select")) {
				ReadSelect(thenStatement, nod);
			}
			if (nod.getNodeName().equals("sort")) {
				ReadSort(thenStatement, nod);
			}
			if (nod.getNodeName().equals("action")) {
				ReadAction(thenStatement, nod);
			}
			if (nod.getNodeName().equals("setDefault")) {
				ReadSetDefault(thenStatement, nod);
			}
			nod = nod.getNextSibling();
		}
    }

    /*private void ReadElse(StrategyElseStatement thenStatement, Node n) {
        // reads the name from the dom model into the CRT structure
        Node nod;
        nod = n.getFirstChild();
		nod = nod.getNextSibling();
		while (nod != null) {
			if (nod.getNodeName().equals("select")) {
				ReadSelect(thenStatement, nod);
			}
			if (nod.getNodeName().equals("sort")) {
				ReadSort(thenStatement, nod);
			}
			if (nod.getNodeName().equals("action")) {
				ReadAction(thenStatement, nod);
			}
			nod = nod.getNextSibling();
		}
    }*/

    private void ReadSelect(StrategyThenStatement thenStatement, Node n) {
		//StrategyAttribute ..
		Node nod =n;
        NamedNodeMap nmap = nod.getAttributes();

        if (nmap != null) {
            // read attributes
            int len = nmap.getLength();
            Attr attr;

            for (int i = 0; i < len; i++) {
                attr = (Attr) nmap.item(i);

                if (attr.getNodeName().equals("attributeName")) {
                    thenStatement.selectAttribute = attr.getNodeValue();
                }
            }
        }

        nod = n.getFirstChild();
        nod = nod.getNextSibling();
		//read attribute name for select
        while (nod != null) {
            if (nod.getNodeName().equals("showContent") || nod.getNodeName().equals("showContentDefault") || nod.getNodeName().equals("showLink")) {
                Vector v = new Vector();
                if (nod.getNodeName().equals("showLink")) {
					Node node = nod.getFirstChild();
        			node = node.getNextSibling();
        			while (node != null) {
						if (node.getNodeName().equals("linkTo")) {
							v.add("\""+node.getFirstChild().getNodeValue().trim()+"\"");
							v.add("showLink");
							//nod = nod.getNextSibling();
							//nod = nod.getNextSibling();
						}
						if (node.getNodeName().equals("comment")) {
							v.add(node.getFirstChild().getNodeValue().trim());
							//nod = nod.getNextSibling();
							//nod = nod.getNextSibling();
						}
						node = node.getNextSibling();
					}
				}
                else {
                	v.add("\""+nod.getFirstChild().getNodeValue().trim()+"\"");
                	v.add(nod.getNodeName().trim());
				}
                thenStatement.selectList.add(v);
            }
            nod = nod.getNextSibling();
        }
    }

    private void ReadSort(StrategyThenStatement thenStatement, Node n) {
		Node nod = n;
        NamedNodeMap nmap = nod.getAttributes();

        if (nmap != null) {
            // read attributes
            int len = nmap.getLength();
            Attr attr;

            for (int i = 0; i < len; i++) {
                attr = (Attr) nmap.item(i);

                if (attr.getNodeName().equals("attributeName")) {
                    thenStatement.sortAttribute = attr.getNodeValue();
                }
            }
        }
        nod = n.getFirstChild();
        nod = nod.getNextSibling();
		//read attribute name for select
        while (nod != null) {
            if (nod.getNodeName().equals("linkTo")) {
                thenStatement.sortList.add("\""+nod.getFirstChild().getNodeValue()+"\"");
            }

            nod = nod.getNextSibling();
            nod = nod.getNextSibling();
        }

    }

    private void ReadAction(StrategyThenStatement thenStatement, Node n) {
		StrategyAction action = new StrategyAction();
		Node nod = n;
        NamedNodeMap nmap = nod.getAttributes();

        if (nmap != null) {
            // read attributes
            int len = nmap.getLength();
            Attr attr;

            for (int i = 0; i < len; i++) {
                attr = (Attr) nmap.item(i);

                if (attr.getNodeName().equals("attributeName")) {
                    //action.UMupdate = attr.getNodeValue();
                    thenStatement.actionAttribute = attr.getNodeValue();
                }
            }
        }
        nod = n.getFirstChild();
        nod = nod.getNextSibling();
		//read attribute name for select
        while (nod != null) {
            if (nod.getNodeName().equals("UMvariable")) {
                action.UMvariable = nod.getFirstChild().getNodeValue();
            }
            if (nod.getNodeName().equals("expression")) {
                action.expression = nod.getFirstChild().getNodeValue();
            }
            nod = nod.getNextSibling();
            nod = nod.getNextSibling();
        }
		thenStatement.actionList.add(action);
    }

    private void ReadSetDefault(StrategyThenStatement thenStatement, Node n) {
		//StrategyAction action = new StrategyAction();
		Node nod = n;
        NamedNodeMap nmap = nod.getAttributes();
        if (nmap != null) {
            // read attributes
            int len = nmap.getLength();
            Attr attr;

            for (int i = 0; i < len; i++) {
                attr = (Attr) nmap.item(i);

                if (attr.getNodeName().equals("attributeName")) {
                    //action.UMupdate = attr.getNodeValue();
                    thenStatement.setDefaultAttribute = attr.getNodeValue();
                }
            }
        }
        nod = n.getFirstChild();
        nod = nod.getNextSibling();
		//read attribute name for select
        while (nod != null) {
            if (nod.getNodeName().equals("expression")) {
                thenStatement.setDefaultExpression = nod.getFirstChild().getNodeValue();
            }
            nod = nod.getNextSibling();
            nod = nod.getNextSibling();
        }
    }

}

