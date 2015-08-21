/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * KoenFormat.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.util;

import com.sun.xml.tree.*;

import vn.spring.WOW.WOWDB.XMLUtil;
import vn.spring.WOW.datacomponents.*;

import org.w3c.dom.*;

import java.net.URL;


/**
 * This class is used to convert between the "Koen"-format of
 * concept definitions in XML.
 *
 */
public class KoenFormat {
    public static String Message = "";

    /**
     * This method is used to convert a concept that is in internal
     * format to the Koen format ready to be stored in an XML file.
     */

    public static Node getKoenXML(Concept concept, XmlDocument doc) {
        Element econcept = doc.createElement("concept");
        Element ename = doc.createElement("name");
        econcept.appendChild(ename);
        ename.appendChild(doc.createTextNode(concept.getName()));

        Element edescription = doc.createElement("description");
        econcept.appendChild(edescription);

        if (concept.getDescription() != null) {
            edescription.appendChild(doc.createTextNode(concept.getDescription()));
        }

        Element eresource = doc.createElement("resource");
        econcept.appendChild(eresource);

        if (concept.getResourceURL() != null) {
            eresource.appendChild(doc.createTextNode(concept.getResourceURL().toString()));
        }

        // nocommit part
        // added by @Loc Nguyen @ 07-05-2008
        if (concept.getNoCommit()) {
          Element enocommit = doc.createElement("nocommit");
          enocommit.appendChild(doc.createTextNode(Boolean.toString(concept.getNoCommit())));
          econcept.appendChild(enocommit);
        }
        // end added by @Loc Nguyen @ 07-05-2008
        // added by @Loc Nguyen @ 10-06-2008
        // stable and stable_expr for the concept part (page stability)
        if (concept.getStable() != null) {
          if (!concept.getStable().equals("")) {
            Element estable = doc.createElement("stable");
            estable.appendChild(doc.createTextNode(concept.getStable()));
            econcept.appendChild(estable);
          }
        }
        if (concept.getStableExpression() != null) {
          if (!concept.getStableExpression().equals("")) {
            Element estable_expr = doc.createElement("stable_expr");
            estable_expr.appendChild(doc.createTextNode(concept.getStableExpression()));
            econcept.appendChild(estable_expr);
          }
        }
        // end added by @Loc Nguyen @ 10-06-2008

        // added by @David @14-05-2008
        Element etype = doc.createElement("concepttype");
        econcept.appendChild(etype);
        if (concept.getType() != null) {
            etype.appendChild(doc.createTextNode(concept.getType()));
        }

        Element etitle = doc.createElement("title");
        econcept.appendChild(etitle);
        if (concept.getTitle() != null) {
            etitle.appendChild(doc.createTextNode(concept.getTitle()));
        }

        if (concept.getHierStruct() != null) {
            Element ehier = doc.createElement("hierarchy");
            econcept.appendChild(ehier);
            ConceptHierStruct hier = concept.getHierStruct();

            Element efirstchild = doc.createElement("firstchild");
            ehier.appendChild(efirstchild);
            if (hier.firstchild != null) {
                efirstchild.appendChild(doc.createTextNode(hier.firstchild));
            }
            Element enextsib = doc.createElement("nextsib");
            ehier.appendChild(enextsib);
            if (hier.nextsib != null) {
                enextsib.appendChild(doc.createTextNode(hier.nextsib));
            }
            Element eparent = doc.createElement("parent");
            ehier.appendChild(eparent);
            if (hier.parent != null) {
                eparent.appendChild(doc.createTextNode(hier.parent));
            }
        }
        // end added by @David @14-05-2008

        for (int i = 0; i < concept.getAttributes().size(); i++) {
            econcept.appendChild(createAttributeNode((Attribute) concept.getAttributes().get(i), doc));
        }

        return econcept;
    }

    private static Node createAttributeNode(Attribute attr, XmlDocument doc) {
        Element eattribute = doc.createElement("attribute");
        eattribute.setAttribute("name", attr.getName());
        eattribute.setAttribute("type", getKoenType(attr.getType()));
        eattribute.setAttribute("isPersistent", new Boolean(attr.isPersistent()).toString());
        eattribute.setAttribute("isSystem", new Boolean(attr.isSystem()).toString());
        eattribute.setAttribute("isChangeable", new Boolean(!attr.isReadonly()).toString());

        Element edescription = doc.createElement("description");
        eattribute.appendChild(edescription);

        if (attr.getDescription() != null) {
            edescription.appendChild(doc.createTextNode(attr.getDescription()));
        }

        Element edefault = doc.createElement("default");
        eattribute.appendChild(edefault);

        if (attr.getDefault() != null) {
            edefault.appendChild(doc.createTextNode(attr.getDefault()));
        }

        // add stable, stable_expr and casegroup conversion here
        // added by @Loc Nguyen @ 07-05-2008
        if (attr.getStable() != null) {
          if (attr.getStable() != "") {
            Element estable = doc.createElement("stable");
            estable.appendChild(doc.createTextNode(attr.getStable()));
            eattribute.appendChild(estable);

            if (attr.getStable().equals("freeze")) {
              if (attr.getStableExpression() != "") {
                Element estable_expr = doc.createElement("stable_expr");
                estable_expr.appendChild(doc.createTextNode(attr.getStableExpression()));
                eattribute.appendChild(estable_expr);
              }
            }
          }
        }

        // casegroup
        if (attr.getCasegroup() != null) {
          // casegroup
          CaseGroup cg = null;
          cg = attr.getCasegroup();

          Element ecasegroup = doc.createElement("casegroup");
          eattribute.appendChild(ecasegroup);

          Element edefaultfragment = doc.createElement("defaultfragment");
          edefaultfragment.appendChild(doc.createTextNode(cg.getDefaultFragment()));
          ecasegroup.appendChild(edefaultfragment);

          // loop the casevalues
          for(int i=0; i<cg.getCaseValues().size(); i++) {
            Case caseValue = (Case) cg.getCaseValues().get(i);
            Element ecasevalue = doc.createElement("casevalue");
            ecasegroup.appendChild(ecasevalue);

            // add value
            Element evalue = doc.createElement("value");
            evalue.appendChild(doc.createTextNode(caseValue.getValue()));
            ecasevalue.appendChild(evalue);
            // add return fragment
            Element ereturnfragment = doc.createElement("returnfragment");
            ereturnfragment.appendChild(doc.createTextNode(caseValue.getReturnfragment()));
            ecasevalue.appendChild(ereturnfragment);
          }
        }
        // end added by @Bart
        for (int i = 0; i < attr.getActions().size(); i++) {
            eattribute.appendChild(createActionNode((Action) attr.getActions().get(i), doc));
        }

        return eattribute;
    }

    private static Node createActionNode(Action action, XmlDocument doc) {
        Element egeneratelist = doc.createElement("generateListItem");
        egeneratelist.setAttribute("isPropagating", new Boolean(action.getTrigger()).toString());

        Element erequirement = doc.createElement("requirement");
        egeneratelist.appendChild(erequirement);
        erequirement.appendChild(doc.createTextNode(XMLUtil.S2D(action.getCondition())));

        int i;
        Element etrueactions = doc.createElement("trueActions");
        egeneratelist.appendChild(etrueactions);

        for (i = 0; i < action.getTrueStatements().size(); i++) {
            etrueactions.appendChild(createAssignmentNode((Assignment) action.getTrueStatements().get(i), doc));
        }

        if (action.getFalseStatements().size() > 0) {
            Element efalseactions = doc.createElement("falseActions");
            egeneratelist.appendChild(efalseactions);

            for (i = 0; i < action.getFalseStatements().size(); i++) {
                efalseactions.appendChild(createAssignmentNode((Assignment) action.getFalseStatements().get(i), doc));
            }
        }

        return egeneratelist;
    }

    private static Node createAssignmentNode(Assignment assign, XmlDocument doc) {
        Element eaction = doc.createElement("action");

        DotString dsconcept = new DotString(assign.getVariable());
        String attribute = dsconcept.get(dsconcept.size() - 1);
        dsconcept.set(dsconcept.size() - 1, null);

        String concept = dsconcept.toString();

        Element econcept = doc.createElement("conceptName");
        eaction.appendChild(econcept);

        Element eattribute = doc.createElement("attributeName");
        eaction.appendChild(eattribute);
        econcept.appendChild(doc.createTextNode(concept));
        eattribute.appendChild(doc.createTextNode(attribute));

        Element eexpr = doc.createElement("expression");
        eaction.appendChild(eexpr);
        eexpr.appendChild(doc.createTextNode(assign.getExpression()));

        return eaction;
    }

    /**
     * This method is used to create the internal format of a concept
     * from the Koen format in an XML file.
     */
    public static Concept getKoenConcept(Node node) {
        Concept concept = null;
        NodeList nodes = node.getChildNodes();
        int i = 0;
        concept = new Concept(XMLUtil.nodeValue(nodes.item(i)));
        i++;

        if (i < nodes.getLength()) {
            if (nodes.item(i).getNodeName().equals("description")) {
                concept.setDescription(XMLUtil.nodeValue(nodes.item(i)));
                i++;
            }
        }

        if (i < nodes.getLength()) {
            if (nodes.item(i).getNodeName().equals("resource")) {
                String resourcestr = XMLUtil.nodeValue(nodes.item(i));

                if (!resourcestr.equals("")) {
                    try {
                        concept.setResourceURL(new URL(resourcestr));
                    } catch (Exception e) {
                        System.out.println(e);
                        setMessage("<h2>Warning!</h2>\nThe resource attribute for concept \"" + concept.getName() + "\" is not defined correctly.<br>Check if it starts with \"http:\", \"ftp:\" or \"file:\".");
                    }
                }

                i++;
            }
        }

        // added by @Loc Nguyen @ 07-05-2008
        if (i < nodes.getLength()) {
            if (nodes.item(i).getNodeName().equals("nocommit")) {
                concept.setNoCommit(Boolean.getBoolean(XMLUtil.nodeValue(nodes.item(i))));
                i++;
            }
        }
        // end added by @Loc Nguyen @ 07-05-2008

        // added by @Loc Nguyen @ 10-06-2008
        if (i < nodes.getLength()) {
            if (nodes.item(i).getNodeName().equals("stable")) {
                concept.setStable(XMLUtil.nodeValue(nodes.item(i)));
                i++;
            }
        }

        if (i < nodes.getLength()) {
            if (nodes.item(i).getNodeName().equals("stable_expr")) {
                concept.setStableExpression(XMLUtil.nodeValue(nodes.item(i)));
                i++;
            }
        }
        // end added by @Loc Nguyen @ 10-06-2008

        // added by @David @14-05-2008
        if (i < nodes.getLength()) {
            if (nodes.item(i).getNodeName().equals("concepttype")) {
                concept.setType(XMLUtil.nodeValue(nodes.item(i)));
                i++;
            }
        }

        if (i < nodes.getLength()) {
            if (nodes.item(i).getNodeName().equals("title")) {
                concept.setTitle(XMLUtil.nodeValue(nodes.item(i)));
                i++;
            }
        }

        if (i < nodes.getLength()) {
            if (nodes.item(i).getNodeName().equals("hierarchy")) {
                NodeList hier = nodes.item(i).getChildNodes();
                ConceptHierStruct nhier = new ConceptHierStruct();
                nhier.firstchild = XMLUtil.nodeValue(hier.item(0));
                nhier.nextsib = XMLUtil.nodeValue(hier.item(1));
                nhier.parent = XMLUtil.nodeValue(hier.item(2));
                concept.setHierStruct(nhier);
                i++;
            }
        }
        // end added by @David @14-05-2008

        for (; i < nodes.getLength(); i++) {
            concept.getAttributes().add(createAttributeObject(nodes.item(i)));
        }

        return concept;
    }

    private static Attribute createAttributeObject(Node node) {
        Attribute attr = null;
        NodeList nodes = node.getChildNodes();
        int i = 0;
        attr = new Attribute(((Element) node).getAttribute("name"), setKoenType(((Element) node).getAttribute("type")));
        attr.setReadonly(!(new Boolean(((Element) node).getAttribute("isChangeable"))).booleanValue());
        attr.setSystem((new Boolean(((Element) node).getAttribute("isSystem"))).booleanValue());
        attr.setPersistent((new Boolean(((Element) node).getAttribute("isPersistent"))).booleanValue());

        if (i < nodes.getLength()) {
            if (nodes.item(i).getNodeName().equals("description")) {
                attr.setDescription(XMLUtil.nodeValue(nodes.item(i)));
                i++;
            }
        }

        if (i < nodes.getLength()) {
            if (nodes.item(i).getNodeName().equals("default")) {
                attr.setDefault(XMLUtil.nodeValue(nodes.item(i)));
                i++;
            }
        }

        // Added by Barend at 8-4-2008
        if (i < nodes.getLength()) {
            if (nodes.item(i).getNodeName().equals("stable")) {
                attr.setStable(XMLUtil.nodeValue(nodes.item(i)));
                i++;
            }
        }

        if (i < nodes.getLength()) {
            if (nodes.item(i).getNodeName().equals("stable_expr")) {
                attr.setStableExpression(XMLUtil.nodeValue(nodes.item(i)));
                i++;
            }
        }

        if (i < nodes.getLength()) {

            if (nodes.item(i).getNodeName().equals("casegroup")) {
                CaseGroup cg = new CaseGroup();

                NodeList nodelist = nodes.item(i).getChildNodes();

                for (int ii = 0; ii < nodelist.getLength(); ii++) {
                    if (nodelist.item(ii).getNodeName().equals("defaultfragment")) {
                        cg.setDefaultFragment(nodelist.item(ii).getFirstChild().getNodeValue());
                    }

                    if (nodelist.item(ii).getNodeName().equals("casevalue")) {
                        cg.getCaseValues().add(createCaseObject(nodelist.item(ii)));
                    }
                }

                attr.setCasegroup(cg);
            }

            //end added by Barend at 8-4-2008
        }

        if (i<nodes.getLength()) {
          if (nodes.item(i).getNodeName().equals("generateListItem")){
              for (; i < nodes.getLength(); i++) {
                  attr.getActions().add(createActionObject(nodes.item(i)));
              }
          }
        }

        /*        while (nodes.item(i).getNodeName().equals("actions"))
                {
                  attr.getActions().add(createActionObject(nodes.item(i)));
                  i++;
                }
        */
        /*
        for (; i < nodes.getLength(); i++)
        {
           attr.getActions().add(createActionObject(nodes.item(i)));
        }
        */


        return attr;
    }

    //added by Barend on 9-4-2008
    private static Case createCaseObject(Node node) {
        Case casevalue = new Case();

        //get the value value of case statement
        casevalue.setValue(node.getFirstChild().getFirstChild().getNodeValue());

        //get the returnfragment of this case statement
        casevalue.setReturnfragment(node.getChildNodes().item(1).getFirstChild().getNodeValue());
        return casevalue;
    }

    //end added by Barend on 9-4-2008
    private static Action createActionObject(Node node) {
        Action action = new Action();
        NodeList nodes = node.getChildNodes();
        int i = 0;
        action.setTrigger((new Boolean(((Element) node).getAttribute("isPropagating"))).booleanValue());
        action.setCondition(XMLUtil.nodeValue(nodes.item(i)));
        i++;

        int j;

        for (j = 0; j < nodes.item(i).getChildNodes().getLength(); j++) {
            action.getTrueStatements().add(createAssignmentObject(nodes.item(i).getChildNodes().item(j)));
        }

        i++;

        if (i < nodes.getLength()) {
            for (j = 0; j < nodes.item(i).getChildNodes().getLength(); j++) {
                action.getFalseStatements().add(createAssignmentObject(nodes.item(i).getChildNodes().item(j)));
            }
        }

        return action;
    }

    private static Assignment createAssignmentObject(Node node) {
        Assignment assign = null;
        NodeList nodes = node.getChildNodes();
        int i = 0;
        String name = XMLUtil.nodeValue(nodes.item(i)) + ".";
        i++;
        name = name + XMLUtil.nodeValue(nodes.item(i));
        i++;
        assign = new Assignment(name, XMLUtil.nodeValue(nodes.item(i)));

        return assign;
    }

    private static String getKoenType(int type) {
        String result = "";

        if (type == AttributeType.ATTRINT) {
            result = "int";
        }

        if (type == AttributeType.ATTRSTR) {
            result = "string";
        }

        if (type == AttributeType.ATTRBOOL) {
            result = "bool";
        }

        return result;
    }

    private static int setKoenType(String type) {
        int result = 0;

        if (type.equals("int")) {
            result = AttributeType.ATTRINT;
        }

        if (type.equals("string")) {
            result = AttributeType.ATTRSTR;
        }

        if (type.equals("bool")) {
            result = AttributeType.ATTRBOOL;
        }

        return result;
    }

    public static String getMessage() {
        return Message;
    }

    public static void setMessage(String msg) {
        Message = msg;
    }
}
