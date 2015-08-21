/*
   This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University
   WOW! is also open source software; 
   
 */

/**
 * NodeInfo.java 1.0, August 30, 2008.
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.formeditor;

import org.w3c.dom.*;


/**
 * Class to provide info about nodes
 *
 */
public class NodeInfo {
    private Node domNode;

    /**
     * Constructor for NodeInfo
     *
     * @param node the node for which the info is to be provided
     */
    public NodeInfo(AdapterNode node) {
        domNode = node.domNode;
    }

    /**
     * Is the node a ConceptList
     *
     * @return boolean is the node a ConceptList
     */
    public boolean isConceptList() {
        if (domNode.getNodeName().equals("conceptList")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Is the node a Concept
     *
     * @return boolean is the node a Concept
     */
    public boolean isConcept() {
        if (domNode.getNodeName().equals("concept")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Is the node an attribute
     *
     * @return boolean is the node an attribute
     */
    public boolean isAttr() {
        boolean answer = false;

        if (domNode.getNodeName().equals("attribute")) {
            answer = true;
        }

        return answer;
    }

    /**
     * Is the attribute changeable
     *
     * @return boolean is the attribute changeable
     */
    public boolean isAttrChangeable() {
        boolean answer = false;
        String s = "";

        if (domNode.getNodeName().equals("attribute")) {
            NamedNodeMap attributeList = domNode.getAttributes();
            Node name = attributeList.getNamedItem("isChangeable");
            s = name.getNodeValue();

            if (s.equals("true")) {
                answer = true;
            }
        }

        return answer;
    }

    /**
     * Gets the name of the attribute
     *
     * @return String the name of the attribute
     */
    public String getAttrName() {
        String s = "";

        if (domNode.getNodeName().equals("attribute")) {
            org.w3c.dom.NamedNodeMap attributeList = domNode.getAttributes();
            org.w3c.dom.Node name = attributeList.getNamedItem("name");
            s = name.getNodeValue();
        }

        return s;
    }

    /**
     * Gets the type of the attribute
     *
     * @return String the type of the attribute
     */
    public String getType() {
        String s = "";

        if (domNode.getNodeName().equals("attribute")) {
            org.w3c.dom.NamedNodeMap attributeList = domNode.getAttributes();
            org.w3c.dom.Node name = attributeList.getNamedItem("type");
            s = name.getNodeValue();
        }

        return s;
    }

    /**
     * Gets the description belonging to the attribute
     *
     * @return String the description belonging to the attribute
     */
    public String getDescription() {
        String s = "";

        if (domNode.getNodeName().equals("attribute")) {
            org.w3c.dom.Node name = domNode.getFirstChild();

            while ((name != null) && !name.getNodeName().equals("description")) {
                name = name.getNextSibling();
            }

            if (name == null) {
                s += "No Description";
            } else {
                AdapterNode adpNode = new AdapterNode(name);
                s += adpNode.content();
            }
        }

        return s;
    }

    /**
     * Gets the name of the concept
     *
     * @return String the name of the concept
     */
    public String getConceptName() {
        String s = "";

        if (domNode.getNodeName().equals("attribute")) {
            org.w3c.dom.Node name = domNode.getParentNode();

            while ((name != null) && !name.getNodeName().equals("concept")) {
                name = name.getNextSibling();
            }

            if (name == null) {
                s += "ConceptName";
            } else {
                name = name.getFirstChild();

                while ((name != null) && !name.getNodeName().equals("name")) {
                    name = name.getNextSibling();
                }

                if (name == null) {
                    s += "ConceptName";
                } else {
                    AdapterNode adpNode = new AdapterNode(name);
                    s += adpNode.content();
                }
            }
        }

        return s;
    }
}
