/*
   This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University
   WOW! is also open source software; 
   
 */

/**
 * AdapterNode.java 1.0, August 30, 2008.
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.formeditor;

import java.util.*;

public class AdapterNode {
    // An array of names for DOM node-types
    // (Array indexes = nodeType() values.)
    static final String[] typeName = {
        "none", "Element", "Attr", "Text", "CDATA", "EntityRef", "Entity",
        "ProcInstr", "Comment", "Document", "DocType", "DocFragment",
        "Notation",
    };

    // The list of elements to display in the tree
    static String[] treeElementNames = { "conceptList", "concept", "attribute" };
    public org.w3c.dom.Node domNode;

    /** Creates new AdapterNode */
    public AdapterNode(org.w3c.dom.Node node) {
        domNode = node;
    }

    boolean treeElement(String elementName) {
        for (int i = 0; i < treeElementNames.length; i++) {
            if (elementName.equals(treeElementNames[i])) {
                return true;
            }
        }

        return false;
    }

    // Return a string that identifies this node in the tree
    // *** Refer to table at top of org.w3c.dom.Node ***
    public String toString() {
        String s = "";

        if (FormEditor.compress) {
            if (domNode.getNodeName().equals("concept")) {
                // s = "ConceptName";
                org.w3c.dom.Node name = domNode.getFirstChild();

                while ((name != null) && !name.getNodeName().equals("name")) {
                    name = name.getNextSibling();
                }

                if (name == null) {
                    s += "ConceptName";
                } else {
                    AdapterNode adpNode = new AdapterNode(name);
                    s += adpNode.content();
                }
            } else if (domNode.getNodeName().equals("conceptList")) {
                // s = "ConceptName";
                org.w3c.dom.Node name = domNode.getFirstChild();

                while ((name != null) && !name.getNodeName().equals("name")) {
                    name = name.getNextSibling();
                }

                if (name == null) {
                    s += "ConceptList";
                } else {
                    AdapterNode adpNode = new AdapterNode(name);
                    s += adpNode.content();
                }
            } else if (domNode.getNodeName().equals("attribute")) {
                org.w3c.dom.NamedNodeMap attributeList =
                        domNode.getAttributes();
                org.w3c.dom.Node name = attributeList.getNamedItem("name");
                s = name.getNodeValue();
            } else {
                String nodeName = domNode.getNodeName();

                if (!nodeName.startsWith("#")) {
                    s = nodeName;
                } else {
                    s = typeName[domNode.getNodeType()];
                }
            }

            return s;
        }

        s = typeName[domNode.getNodeType()];

        String nodeName = domNode.getNodeName();

        if (!nodeName.startsWith("#")) {
            s += (": " + nodeName);
        }

        if (domNode.getNodeValue() != null) {
            if (s.startsWith("ProcInstr")) {
                s += ", ";
            } else {
                s += ": ";
            }

            // Trim the value to get rid of NL's at the front
            String t = domNode.getNodeValue().trim();
            int x = t.indexOf("\n");

            if (x >= 0) {
                t = t.substring(0, x);
            }

            s += t;
        }

        return s;
    }

    public String content() {
        String s = "";

        org.w3c.dom.NodeList nodeList = domNode.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            org.w3c.dom.Node node = nodeList.item(i);
            int type = node.getNodeType();
            AdapterNode adpNode = new AdapterNode(node);

            if (type == GenerateListData.ELEMENT_TYPE) {
                if (treeElement(node.getNodeName())) {
                    continue;
                }

                s += ("<" + node.getNodeName() + ">");
                s += adpNode.content();
                s += ("</" + node.getNodeName() + ">");
            } else if (type == GenerateListData.TEXT_TYPE) {
                s += node.getNodeValue();
            } else if (type == GenerateListData.ENTITYREF_TYPE) {
                // The content is in the TEXT node under it
                s += adpNode.content();
            } else if (type == GenerateListData.CDATA_TYPE) {
                StringBuffer sb = new StringBuffer(node.getNodeValue());

                for (int j = 0; j < sb.length(); j++) {
                    if (sb.charAt(j) == '<') {
                        sb.setCharAt(j, '&');
                        sb.insert(j + 1, "lt;");
                        j += 3;
                    } else if (sb.charAt(j) == '&') {
                        sb.setCharAt(j, '&');
                        sb.insert(j + 1, "amp;");
                        j += 4;
                    }
                }

                s += ("<pre>" + sb + "\n</pre>");
            }
        }

        return s;
    }

    public boolean isAttrChangeable() {
        boolean answer = false;
        String s = "";

        if (domNode.getNodeName().equals("attribute")) {
            org.w3c.dom.NamedNodeMap attributeList = domNode.getAttributes();
            org.w3c.dom.Node name = attributeList.getNamedItem("isChangeable");
            s = name.getNodeValue();

            if (s.equals("true")) {
                answer = true;
            }
        }

        return answer;
    }

    public int index(AdapterNode child) {
        //System.err.println("Looking for index of " + child);
        int count = childCount();

        for (int i = 0; i < count; i++) {
            AdapterNode n = this.child(i);

            if (child == n) {
                return i;
            }
        }

        return -1; // Should never get here.
    }

    public AdapterNode child(int searchIndex) {
        //Note: JTree index is zero-based.
        org.w3c.dom.Node node = domNode.getChildNodes().item(searchIndex);

        if (FormEditor.compress) {
            TreeSet ChildSet = new TreeSet(new AdapterNodeComparator());
            org.w3c.dom.NodeList childNodes = domNode.getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i++) {
                node = childNodes.item(i);

                if ((node.getNodeType() == GenerateListData.ELEMENT_TYPE) &&
                        treeElement(node.getNodeName())) {
                    ChildSet.add(new AdapterNode(node));
                }
            }

            Object obj = ChildSet.toArray()[searchIndex];

            return (AdapterNode) obj;

            // Return Nth displayable node

            /**
             * Old Code
             * int elementNodeIndex = 0;
             * for (int i=0; i<domNode.getChildNodes().getLength(); i++) {
             * node = (domNode.getChildNodes()).item(i);
             * if (node.getNodeType() == GenerateListData.ELEMENT_TYPE
             * && treeElement( node.getNodeName() )
             * && elementNodeIndex++ == searchIndex) {
             * break;
             * }
             *
             * }*/
        }

        return new AdapterNode(node);
    }

    public int childCount() {
        if (!FormEditor.compress) {
            // Indent this
            return domNode.getChildNodes().getLength();
        }

        int count = 0;

        for (int i = 0; i < domNode.getChildNodes().getLength(); i++) {
            org.w3c.dom.Node node = domNode.getChildNodes().item(i);

            if ((node.getNodeType() == GenerateListData.ELEMENT_TYPE) &&
                    treeElement(node.getNodeName())) {
                ++count;
            }
        }

        return count;
    }

    class AdapterNodeComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
        }

        public boolean equals(Object obj) {
            return this.equals(obj);
        }
    }
}
