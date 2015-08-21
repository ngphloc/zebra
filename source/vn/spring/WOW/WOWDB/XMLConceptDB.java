/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * XMLConceptDB.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.WOWDB;

import com.sun.xml.tree.*;
import org.w3c.dom.*;
import java.io.*;

import java.net.URL;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.exceptions.*;


/**
 * This interface must be implemented by a class to function as a
 * concept storage means.
 */
public class XMLConceptDB implements ConceptDB {
    //the root directory of this database
    private File root = null;

    //the dtd of the XML format used by the interface
    private File dtd = null;

    //a boolean to keep track of the indexing state
    private boolean indexing = true;

    //the index table on names for this database
    private Hashtable index = null;

    //the index table on resources for this database
    private Hashtable idxresource = null;

    //the concept cache
    private Hashtable cache = new Hashtable();

    /**
     * Creates a new XML concept database interface.
     */
    public XMLConceptDB(File proot) throws DatabaseException {
        WOWStatic.checkNull(proot, "root");

        try {
            if (!proot.exists()) {
                proot.mkdir();
            }
            //set and create the directory if necessary
            root = new File(proot, "concept");

            if (!root.exists()) {
                root.mkdir();
            }
            //set and create the dtd if necessary
            dtd = new File(root, "concept.dtd");

            if (!dtd.exists()) {
                StringBuffer outstr = new StringBuffer();
                outstr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                // changed by @Bart 25-04-2008
                // changed by @Bart 14-05-2008
                outstr.append("<!ELEMENT concept (name, description, attributes, resource?, nocommit?, stable?, stable_expr?,concepttype,title,hierarchy?)>\n");
                // end changed by  @Bart
                // end changed by  @Bart
                outstr.append("<!ELEMENT attributes (attribute)*>\n");

                // changed by @Bart 01-11-2008
                // changed by @Barend, @Loc Nguyen 18-11-2008
                outstr.append("<!ELEMENT attribute (name, description, default, type, actions, readonly, system, persistent, stable?, stable_expr?,casegroup?)>\n");
                // end changed by @Barend, @Loc Nguyen 18-11-2008
                // end changed by @bart 01-11-2008
                outstr.append("<!ELEMENT actions (action)*>\n");
                outstr.append("<!ELEMENT action (expr, trigger, truestat, falsestat)>\n");
                outstr.append("<!ELEMENT truestat (assignment)*>\n");
                outstr.append("<!ELEMENT falsestat (assignment)*>\n");
                outstr.append("<!ELEMENT assignment (variable, expr)>\n\n");

                // ADDED by @Barend, @Loc Nguyen 18-11-2008
                outstr.append("<!ELEMENT casegroup (defaultfragment, casevalue*)>\n");
                outstr.append("<!ELEMENT casevalue (value,returnfragment)>\n");
                outstr.append("<!ELEMENT defaultfragment (#PCDATA)>\n");
                outstr.append("<!ELEMENT value (#PCDATA)>\n");
                outstr.append("<!ELEMENT returnfragment (#PCDATA)>\n");
                // end ADDED by @Barend, @Loc Nguyen 18-11-2008

                outstr.append("<!ELEMENT name (#PCDATA)>\n");
                outstr.append("<!ELEMENT description (#PCDATA)>\n");
                outstr.append("<!ELEMENT expr (#PCDATA)>\n");
                outstr.append("<!ELEMENT default (#PCDATA)>\n");
                outstr.append("<!ELEMENT resource (#PCDATA)>\n");
                // added by @Loc Nguyen @ 25-04-2008
                outstr.append("<!ELEMENT nocommit (#PCDATA)>\n");
                // end added
                outstr.append("<!ELEMENT type (#PCDATA)>\n");
                outstr.append("<!ELEMENT readonly (#PCDATA)>\n");
                outstr.append("<!ELEMENT system (#PCDATA)>\n");
                outstr.append("<!ELEMENT persistent (#PCDATA)>\n");

                // ADDED by @Bart 01-11-2008
                outstr.append("<!ELEMENT stable (#PCDATA)>\n");
                outstr.append("<!ELEMENT stable_expr (#PCDATA)>\n");
                // end ADDED by @Bart 01-11-2008

                outstr.append("<!ELEMENT trigger (#PCDATA)>\n");
                outstr.append("<!ELEMENT variable (#PCDATA)>\n");

                //Added by  @Tomi (WOW-Pitt integration)
                outstr.append("<!ELEMENT hierarchy (firstchild, nextsib, parent)>\n\n");
                outstr.append("<!ELEMENT firstchild (#PCDATA)>\n");
                outstr.append("<!ELEMENT nextsib (#PCDATA)>\n");
                outstr.append("<!ELEMENT parent (#PCDATA)>\n");
                outstr.append("<!ELEMENT concepttype (#PCDATA)>\n");
                outstr.append("<!ELEMENT title (#PCDATA)>\n");
                //End Tomi

                FileWriter dtdout = new FileWriter(dtd);
                dtdout.write(outstr.toString());
                dtdout.close();
            }


            //load the index file
            index = XMLUtil.loadIndex(root);
            idxresource = XMLUtil.loadIndex(root, "idxresource");
        } catch (IOException e) {
            throw new DatabaseException("unable to create XMLConceptDB in " + root + ": " + e.getMessage());
        }
    }

    public void enableIndexWriting() throws DatabaseException {
        indexing = true;
        updateIndex();
    }

    public void disableIndexWriting() {
        indexing = false;
    }

    /**
     * Creates a concept with the specified name and returns the id.
     */
    public long createConcept(String name) throws DatabaseException {
        long id = WOWStatic.getUID();

        //create the file object
        File conceptxml = new File(root, (new Long(id)).toString());

        if (conceptxml.exists()) {
            throw new DatabaseException("unable to create concept: invalid id");
        }

        //create the in-memory document
        XmlDocument doc = new XmlDocument();
        Element econcept = doc.createElement("concept");
        doc.appendChild(econcept);

        Element ename = doc.createElement("name");
        econcept.appendChild(ename);
        ename.appendChild(doc.createTextNode(name));

        // added by @Loc Nguyen @ 25-04-2008
        Element edesc = doc.createElement("description");
        econcept.appendChild(edesc);
        edesc.appendChild(doc.createTextNode(""));
        // end added by @Bart

        Element eexpr = doc.createElement("expr");
        econcept.appendChild(eexpr);
        eexpr.appendChild(doc.createTextNode(""));

        Element eattributes = doc.createElement("attributes");
        econcept.appendChild(eattributes);

        //Added by @Tomi (WOW-Pitt integration)
        Element ectype = doc.createElement("concepttype"); econcept.appendChild(ectype);
        ectype.appendChild(doc.createTextNode(""));

        Element etitle = doc.createElement("title"); econcept.appendChild(etitle);
        etitle.appendChild(doc.createTextNode(""));
        //End Tomi

        doc.setSystemId("concept");
        doc.setDoctype(null, "concept.dtd", null);

        //try to save it to the disk
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(conceptxml));
            doc.write(pw);
            pw.close();
        } catch (IOException e) {
            throw new DatabaseException("unable to create concept: " + e.getMessage());
        }

        //update the index
        index.put(name, new Long(id));
        updateIndex();

        //return the new identifier
        return id;
    }

    //update the index
    private void updateIndex() throws DatabaseException {
        if (!indexing) return;
        try {
            XMLUtil.saveIndex(root, index);
        } catch (IOException e) {
            throw new DatabaseException("unable to write index: " + e.getMessage());
        }

        try {
            XMLUtil.saveIndex(root, "idxresource", idxresource);
        } catch (IOException e) {
            throw new DatabaseException("unable to write index: " + e.getMessage());
        }
    }

    /**
     * Returns the id of the specified concept.
     */
    public long findConcept(String name) throws DatabaseException, InvalidConceptException {
        WOWStatic.checkNull(name, "name");

        if (!index.containsKey(name)) {
            throw new InvalidConceptException("concept " + name + " does not exist");
        }

        return ((Long) index.get(name)).longValue();
    }

    /**
     * Links the specified concept to the specified resource.
     * Concepts and resources may be linked only once.
     */
    public void linkResource(long id, String resource) throws InvalidConceptException, DatabaseException {
        Concept concept = getConcept(id);

        try {
            concept.setResourceURL(new URL(resource));
        } catch (Exception e) {
            throw new DatabaseException("invalid URL: " + resource);
        }

        setConcept(id, concept);
    }

    /**
     * Removes a link from the specified concept to a resource.
     */
    public void unlinkResource(long id) throws InvalidConceptException, DatabaseException {
        Concept concept = getConcept(id);
        concept.setResourceURL(null);
        setConcept(id, concept);
    }

    /**
     * Returns the resource that is linked to the specified concept.
     */
    public String getLinkedResource(long id) throws InvalidConceptException, DatabaseException {
        Concept concept = getConcept(id);

        if (concept.getResourceURL() == null) {
            return null;
        } else {
            return concept.getResourceURL().toString();
        }
    }

    /**
     * Returns the concept that is linked to the specified resource.
     */
    public String getLinkedConcept(String resource) throws DatabaseException {
        if (!idxresource.containsKey(resource)) {
            return null;
        } else {
            long id = ((Long) idxresource.get(resource)).longValue();
            String key;

            for (Enumeration keys = index.keys(); keys.hasMoreElements();) {
                key = (String) keys.nextElement();

                if (((Long) index.get(key)).longValue() == id) {
                    return key;
                }
            }
        }

        return null;
    }

    /**
     * Sets the specified attribute.
     */
    public void setAttribute(long id, Attribute attr) throws InvalidConceptException, DatabaseException {
        Concept concept = getConcept(id);

        for (int i = 0; i < concept.getAttributes().size(); i++) {
            if (((Attribute) concept.getAttributes().get(i)).getName().equals(attr.getName())) {
                concept.getAttributes().remove(i);
            }
        }

        concept.getAttributes().add(attr);
        setConcept(id, concept);
    }

    /**
     * Returns the specified attribute.
     */
    public Attribute getAttribute(long id, String name) throws InvalidConceptException, DatabaseException, InvalidAttributeException {
        Concept concept = getConcept(id);
        Attribute result = null;

        for (int i = 0; i < concept.getAttributes().size(); i++) {
            if (((Attribute) concept.getAttributes().get(i)).getName().equals(name)) {
                result = (Attribute) concept.getAttributes().get(i);
            }
        }

        if (result == null) throw new InvalidAttributeException("Attribute not found: "+name);
        return result;
    }

    /**
     * Removes the specified attribute.
     */
    public void removeAttribute(long id, String name) throws InvalidConceptException, DatabaseException {
        Concept concept = getConcept(id);

        for (int i = 0; i < concept.getAttributes().size(); i++) {
            if (((Attribute) concept.getAttributes().get(i)).getName().equals(name)) {
                concept.getAttributes().remove(i);
            }
        }

        setConcept(id, concept);
    }

    /**
     * Returns the list of names of all attributes of a concept.
     */
    public Vector getAttributeNames(long id) throws InvalidConceptException, DatabaseException {
        Concept concept = getConcept(id);
        Vector result = new Vector();

        for (int i = 0; i < concept.getAttributes().size(); i++) {
            result.add(((Attribute) concept.getAttributes().get(i)).getName());
        }

        return result;
    }

    /**
     * Returns all attributes of a concept.
     */
    public Vector getAttributes(long id) throws InvalidConceptException, DatabaseException {
        Concept concept = getConcept(id);

        return concept.getAttributes();
    }

    /**
     * Sets all attributes of a concept.
     */
    public void setAttributes(long id, Vector attrs) throws InvalidConceptException, DatabaseException {
        Concept concept = getConcept(id);
        concept.getAttributes().clear();
        concept.getAttributes().addAll(attrs);
        setConcept(id, concept);
    }

    //removes the linked resource where the concept is id
    private void removeFromIndex(Hashtable idx, long id) {
        String key;

        for (Enumeration keys = idx.keys(); keys.hasMoreElements();) {
            key = (String) keys.nextElement();

            if (((Long) idx.get(key)).longValue() == id) {
                idx.remove(key);
            }
        }
    }

    /**
     * Saves a concept with the specified id.
     */
    public void setConcept(long id, Concept concept) throws InvalidConceptException, DatabaseException {
        //create the file object
        File conceptxml = new File(root, (new Long(id)).toString());

        if (!conceptxml.exists()) {
            throw new InvalidConceptException("concept does not exist: " + id);
        }

        //create the in memory xml document
        XmlDocument doc = new XmlDocument();
        Element econcept = doc.createElement("concept");
        doc.appendChild(econcept);

        Element ename = doc.createElement("name");
        econcept.appendChild(ename);
        ename.appendChild(doc.createTextNode(concept.getName()));

        Element edescription = doc.createElement("description");
        econcept.appendChild(edescription);
        edescription.appendChild(doc.createTextNode(XMLUtil.S2D(concept.getDescription())));

        Element eattributes = doc.createElement("attributes");
        econcept.appendChild(eattributes);

        if (concept.getResourceURL() != null) {
            String resource = concept.getResourceURL().toString();

            if (idxresource.containsKey(resource)) {
                if (((Long) idxresource.get(resource)).longValue() != id) {
                    throw new DatabaseException("the resource is already linked");
                }
            }

            Element eresource = doc.createElement("resource");
            econcept.appendChild(eresource);
            eresource.appendChild(doc.createTextNode(concept.getResourceURL().toString()));

            //update the index
            removeFromIndex(idxresource, id);
            idxresource.put(resource, new Long(id));
        }

        // added by @Loc Nguyen @ 28-05-2008
        // add nocommit part if needed
        if (concept.getNoCommit()) {
          Element enocommit = doc.createElement("nocommit");
          econcept.appendChild(enocommit);
          enocommit.appendChild(doc.createTextNode(XMLUtil.S2D(String.valueOf(concept.getNoCommit()))));
        }
        // end added by @Loc Nguyen @ 28-05-2008

        // added by @Loc Nguyen @ 14-05-2008
        if (concept.hasStableProperty()) {
          Element estable = doc.createElement("stable");
          econcept.appendChild(estable);
          estable.appendChild(doc.createTextNode(concept.getStable()));

          if (concept.getStable().equals("freeze")) {
            if (concept.hasStableExpressionProperty()) {

              Element estable_expr = doc.createElement("stable_expr");
              econcept.appendChild(estable_expr);
              estable_expr.appendChild(doc.createTextNode(concept.getStableExpression()));
            }
          }
        }
        // end added by @Loc Nguyen @ 14-05-2008

        //Added by @TOmi (WOW-Pitt integration)
        //Add concept type
        Element ectype = doc.createElement("concepttype"); econcept.appendChild(ectype);
        ectype.appendChild(doc.createTextNode(concept.getType()));

        //Add concept title
        Element etitle = doc.createElement("title"); econcept.appendChild(etitle);
        etitle.appendChild(doc.createTextNode(concept.getTitle()));

        //Add hierarchy struct
        if(concept.getHierStruct()!=null){
          Element ehier = doc.createElement("hierarchy"); econcept.appendChild(ehier);
          Element efirstchild = doc.createElement("firstchild"); ehier.appendChild(efirstchild);
          if(concept.getHierStruct().firstchild!=null)
            efirstchild.appendChild(doc.createTextNode(concept.getHierStruct().firstchild));
          Element enextsib = doc.createElement("nextsib"); ehier.appendChild(enextsib);
          if(concept.getHierStruct().nextsib!=null)
            enextsib.appendChild(doc.createTextNode(concept.getHierStruct().nextsib));
          Element eparent = doc.createElement("parent"); ehier.appendChild(eparent);
          if(concept.getHierStruct().parent!=null)
            eparent.appendChild(doc.createTextNode(concept.getHierStruct().parent));
        }
        //End Tomi

        for (int i = 0; i < concept.getAttributes().size(); i++) {
            eattributes.appendChild(createAttributeNode(doc, (Attribute) concept.getAttributes().get(i)));
        }

        doc.setSystemId("concept");
        doc.setDoctype(null, "concept.dtd", null);

        //try to save it to the disk
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(conceptxml));
            doc.write(pw);
            pw.close();
        } catch (IOException e) {
            throw new DatabaseException("unable to create concept: " + e.getMessage());
        }

        //update the index
        removeFromIndex(index, id);
        index.put(concept.getName(), new Long(id));
        updateIndex();

        //update the cache
        setCachedConcept(id, concept);
    }

    //creates a new attribute node from an attribute object
    private Node createAttributeNode(Document doc, Attribute attr) {
        Element result = doc.createElement("attribute");
        Element ename = doc.createElement("name");
        result.appendChild(ename);

        Element edescription = doc.createElement("description");
        result.appendChild(edescription);

        Element edefault = doc.createElement("default");
        result.appendChild(edefault);

        Element etype = doc.createElement("type");
        result.appendChild(etype);

        Element eactions = doc.createElement("actions");
        result.appendChild(eactions);

        Element ereadonly = doc.createElement("readonly");
        result.appendChild(ereadonly);

        Element esystem = doc.createElement("system");
        result.appendChild(esystem);

        Element epersistent = doc.createElement("persistent");
        result.appendChild(epersistent);

        Element estable = null;
        Element estableexpr = null;

        // added by @Loc Nguyen @ 03-05-2008
        Element edeffrag = null;
        Element ecasegroup = null;
        // end added by @Loc Nguyen @ 03-05-2008

        // Added by @Bart 01-11-2008
        // add only the stable part if it is really there..
        if (attr.hasStableProperty()) {
            estable = doc.createElement("stable");
            result.appendChild(estable);

            if (attr.hasStableExpressionProperty()) {
                estableexpr = doc.createElement("stable_expr");
                result.appendChild(estableexpr);
            }
        }
        // end added by @Bart 01-11-2008

        //added by @Barend, @Loc Nguyen
        //add only the casegroup part if it is really there...

        if (attr.hasGroupNode()) {
            ecasegroup = doc.createElement("casegroup");
            result.appendChild(ecasegroup);
        }
        //end added by @Barend, @Loc Nguyen

        ename.appendChild(doc.createTextNode(attr.getName()));
        edescription.appendChild(doc.createTextNode(XMLUtil.S2D(attr.getDescription())));
        edefault.appendChild(doc.createTextNode(XMLUtil.S2D(attr.getDefault())));
        etype.appendChild(doc.createTextNode((new Long(attr.getType())).toString()));
        ereadonly.appendChild(doc.createTextNode((new Boolean(attr.isReadonly())).toString()));
        esystem.appendChild(doc.createTextNode((new Boolean(attr.isSystem())).toString()));
        epersistent.appendChild(doc.createTextNode((new Boolean(attr.isPersistent())).toString()));

        // Added by @Bart 04-11-2008
        // add only the stable expression part if it is really there..
        if (attr.hasStableProperty()) {
            estable.appendChild(doc.createTextNode(attr.getStable()));

            if (attr.hasStableExpressionProperty()) {
                estableexpr.appendChild(doc.createTextNode(attr.getStableExpression()));
            }
        }
        // end Added by @Bart 04-11-2008

        for (int i = 0; i < attr.getActions().size(); i++) {
            eactions.appendChild(createActionNode(doc, (Action) attr.getActions().get(i)));
        }

        // added by @Loc Nguyen @ 03-05-2008
        if (attr.hasGroupNode()) {
            edeffrag = doc.createElement("defaultfragment");
            ecasegroup.appendChild(edeffrag);
            edeffrag.appendChild(doc.createTextNode(attr.getCasegroup().getDefaultFragment()));

            /**
             added by @Barend, @Loc Nguyen 19-11-2008 fill the casegroup vector with cases
             */
            for (int i = 0; i < attr.getCasegroup().getCaseValues().size(); i++) {
                ecasegroup.appendChild(createCaseNode(doc, (Case) attr.getCasegroup().getCaseValues().get(i) ));
            }
        }
        // end added by @Loc Nguyen @ 03-05-2008



        return result;
    }

    //creates a new action node from an action object
    private Node createActionNode(Document doc, Action action) {
        Element result = doc.createElement("action");
        Element eexpr = doc.createElement("expr");
        result.appendChild(eexpr);

        Element etrigger = doc.createElement("trigger");
        result.appendChild(etrigger);

        Element etruestat = doc.createElement("truestat");
        result.appendChild(etruestat);

        Element efalsestat = doc.createElement("falsestat");
        result.appendChild(efalsestat);
        eexpr.appendChild(doc.createTextNode(XMLUtil.S2D(action.getCondition())));
        etrigger.appendChild(doc.createTextNode((new Boolean(action.getTrigger())).toString()));

        int i;

        for (i = 0; i < action.getTrueStatements().size(); i++) {
            etruestat.appendChild(createAssignmentNode(doc, (Assignment) action.getTrueStatements().get(i)));
        }

        for (i = 0; i < action.getFalseStatements().size(); i++) {
            efalsestat.appendChild(createAssignmentNode(doc, (Assignment) action.getFalseStatements().get(i)));
        }

        return result;
    }

    //creates a new assignment node from an assignment object
    private Node createAssignmentNode(Document doc, Assignment assign) {
        Element result = doc.createElement("assignment");
        Element evariable = doc.createElement("variable");
        result.appendChild(evariable);

        Element eexpr = doc.createElement("expr");
        result.appendChild(eexpr);
        evariable.appendChild(doc.createTextNode(assign.getVariable()));
        eexpr.appendChild(doc.createTextNode(assign.getExpression()));

        return result;
    }

    // Added by @Barend, @Loc Nguyen @01-12-2008
    // creates a new case node from an case object
    private Node createCaseNode(Document doc, Case casevalue) {
        Element result = doc.createElement("casevalue");
        Element evalue = doc.createElement("value");
        Element ereturnfragment = doc.createElement("returnfragment");

        result.appendChild(evalue);
        result.appendChild(ereturnfragment);

        evalue.appendChild(doc.createTextNode(casevalue.getValue()));
        ereturnfragment.appendChild(doc.createTextNode(casevalue.getReturnfragment()));
        return result;
    }
    // end Added by Barend

    /**
     * Loads a concept with the specified id.
     */
    // Changed by @Loc Nguyen @ 28-05-2008
    // added readout of nocommit value and changed the way that was done
    public Concept getConcept(long id) throws DatabaseException, InvalidConceptException {
        //check caching
        Concept cached = getCachedConcept(id);
        if (cached != null) return cached;

        //try to load the document in memory
        File conceptxml = new File(root, (new Long(id)).toString());

        if (!conceptxml.exists()) {
            throw new InvalidConceptException("concept does not exist: " + id);
        }

        XmlDocument doc = null;

        try {
            doc = XMLUtil.getXML(conceptxml);
        } catch (IOException e) {
            throw new DatabaseException("unable to read concept: " + id);
        }
// <!ELEMENT concept (name, description, expr, attributes, resource?, nocommit?, stable?, stable_expr?)>

        //create the concept
        NodeList list = doc.getDocumentElement().getChildNodes();
        Concept concept = new Concept(XMLUtil.nodeValue(list.item(0)));
        concept.id = id;

        for (int j = 1; j < list.getLength(); j++) {
          Node childNode = list.item(j);
          if (childNode.getNodeName().equals("description")) {
            concept.setDescription(XMLUtil.D2S(XMLUtil.nodeValue(childNode)));
          }
          if (childNode.getNodeName().equals("resource")) {
            try {
              concept.setResourceURL(new URL(XMLUtil.nodeValue(childNode)));
            } catch (Exception e) {
              throw new DatabaseException("invalid URL in concept: " + id);
            }
          }
          if (childNode.getNodeName().equals("nocommit")) {
            String tempValue = XMLUtil.nodeValue(childNode);
            concept.setNoCommit(Boolean.valueOf(tempValue).booleanValue());
//            concept.setNoCommit(Boolean.getBoolean(XMLUtil.nodeValue(childNode)));
          }

          // added by @Loc Nguyen @ 14-05-2008
          if (childNode.getNodeName().equals("stable")) {
              // add the stable property
              concept.setStable(XMLUtil.D2S(XMLUtil.nodeValue(childNode)));
          }

          if (childNode.getNodeName().equals("stable_expr")) {
              // add the stable property
              concept.setStableExpression(XMLUtil.D2S(XMLUtil.nodeValue(childNode)));
          }
          // end added by @Loc Nguyen @ 14-05-2008

          if (childNode.getNodeName().equals("attributes")) {
            NodeList list2 = childNode.getChildNodes();
            for (int i = 0; i < list2.getLength(); i++) {
                concept.getAttributes().add(createAttributeObject(list2.item(i)));
            }
          }

          //Added by @Tomi (WOW-Pitt integration)
           if (childNode.getNodeName().equals("concepttype")) {
             String type= XMLUtil.D2S(XMLUtil.nodeValue(childNode));
             concept.setType(type);
           }

           if (childNode.getNodeName().equals("title")) {
             String title= XMLUtil.D2S(XMLUtil.nodeValue(childNode));
             concept.setTitle(title);
           }

           if (childNode.getNodeName().equals("hierarchy")) {
             ConceptHierStruct hier= new ConceptHierStruct();

             NodeList list2 = childNode.getChildNodes();
             for (int i = 0; i < list2.getLength(); i++) {
               if(list2.item(i).getNodeName().equals("firstchild")){
                 hier.firstchild= XMLUtil.D2S(XMLUtil.nodeValue(list2.item(i)));
               }
               if(list2.item(i).getNodeName().equals("nextsib")){
                 hier.nextsib= XMLUtil.D2S(XMLUtil.nodeValue(list2.item(i)));
               }
               if(list2.item(i).getNodeName().equals("parent")){
                 hier.parent= XMLUtil.D2S(XMLUtil.nodeValue(list2.item(i)));
               }
            }

            concept.setHierStruct(hier);
           }

          //End Tomi
        }

        //update the cache
        setCachedConcept(id, concept);

        return concept;
    }

    //creates a new attribute object from an attribute node
    private Attribute createAttributeObject(Node node) {
        int templistitemexists = 0;

        NodeList templist;
        NodeList list = node.getChildNodes();
        Attribute result = new Attribute(XMLUtil.nodeValue(list.item(0)), Integer.parseInt(XMLUtil.nodeValue(list.item(3))));
        result.setDescription(XMLUtil.D2S(XMLUtil.nodeValue(list.item(1))));
        result.setDefault(XMLUtil.D2S(XMLUtil.nodeValue(list.item(2))));
        result.setReadonly((new Boolean(XMLUtil.nodeValue(list.item(5)))).booleanValue());
        result.setSystem((new Boolean(XMLUtil.nodeValue(list.item(6)))).booleanValue());
        result.setPersistent((new Boolean(XMLUtil.nodeValue(list.item(7)))).booleanValue());

        // Added by @Bart 04-11-2008
        // add the stable and stable_expr from the xml file to the attribute object
        // test first if the properties are available in the xml because they are optional
        // this test returns the actual index number if the item exists
        templistitemexists = (listitemexists(node,"stable"));
        if (templistitemexists != -1) {
            // add the stable property
            result.setStable(XMLUtil.D2S(XMLUtil.nodeValue(list.item(templistitemexists))));
        }

        templistitemexists = (listitemexists(node,"stable_expr"));
        if (templistitemexists != -1) {
            // add the stable property
            result.setStableExpression(XMLUtil.D2S(XMLUtil.nodeValue(list.item(templistitemexists))));
        }
        // End Added by @Bart 04-11-2008

        // start added by @Barend, @Loc Nguyen 19-11-2008
        // changed by @Loc Nguyen @ 03-05-2008
        // check if casegroup exists in the itemlist, the returned value is then the index nr
        templistitemexists = (listitemexists(node,"casegroup"));
        templist=list;
        if (templistitemexists != -1) {
                  templist = templist.item(templistitemexists).getChildNodes();
                  // the first child node is the default fragment node
                  CaseGroup cg = new CaseGroup();
                  cg.setDefaultFragment(XMLUtil.D2S(XMLUtil.nodeValue(templist.item(0))));
                  result.setCasegroup(cg);
                  // add the other child nodes (which are casevalues
                  for (int i = 1; i < templist.getLength(); i++) {
                      result.getCasegroup().getCaseValues().add(createCaseObject(templist.item(i)));
                  }
        }
        // end changed by @Loc Nguyen @ 03-05-2008
        // end Added by @Barend, @Loc Nguyen 19-11-2008

        //add all actions this is not a nice construction because it changes the list object
        list = list.item(4).getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
          result.getActions().add(createActionObject(list.item(i)));
        }

        return result;
    }

    /** added by @Barend, @Loc Nguyen
    * private function which returns if an object exists and if so it's index
    * -1 mean's does not exisist =>0 means exists with number as index.
    * @param node the XML node
    * @param searchitem the string that must exists in the XML node
    * @return int actual index number or -1
    */
    public int listitemexists(Node node, String searchitem) {
      //init listitemexists on -1 (not found)
      int listitemexists=-1;
      NodeList list = node.getChildNodes();
      int i = 0;
      int j = 0;
      j = list.getLength();
      while (i != j) {
        if (list.item(i).getNodeName().equals(searchitem)) {
            listitemexists = i;
            i = j - 1;
         }
         i++;
      }
      return listitemexists;
    }
    //End added by @Barend, @Loc Nguyen

    //creates a new action object from an action node
    private Action createActionObject(Node node) {
        NodeList list = node.getChildNodes();
        Action result = new Action();
        result.setCondition(XMLUtil.D2S(XMLUtil.nodeValue(list.item(0))));
        result.setTrigger((new Boolean(XMLUtil.nodeValue(list.item(1)))).booleanValue());

        int i;
        NodeList childlist = list.item(2).getChildNodes();

        for (i = 0; i < childlist.getLength(); i++) {
            result.getTrueStatements().add(createAssignmentObject(childlist.item(i)));
        }

        childlist = list.item(3).getChildNodes();

        for (i = 0; i < childlist.getLength(); i++) {
            result.getFalseStatements().add(createAssignmentObject(childlist.item(i)));
        }

        return result;
    }
  //Added by @Barend, @Loc Nguyen
  //creates a new case object from an case node
    private Case createCaseObject(Node node) {
        NodeList list = node.getChildNodes();
        Case result = new Case();
        result.setValue(XMLUtil.D2S(XMLUtil.nodeValue(list.item(0))));
        result.setReturnfragment(XMLUtil.D2S(XMLUtil.nodeValue(list.item(1))));
        return result;
    }

    //creates a new assignment object from an assignment node
    private Assignment createAssignmentObject(Node node) {
        NodeList list = node.getChildNodes();
        Assignment result = new Assignment(XMLUtil.nodeValue(list.item(0)), XMLUtil.nodeValue(list.item(1)));

        return result;
    }

    /**
     * Return the list of all concepts.
     */
    public Vector getConceptList() throws DatabaseException {
        return new Vector(index.keySet());
    }

    private void setCachedConcept(long id, Concept concept) {
        Long lid = new Long(id);
        cache.put(lid, concept);
    }

    private Concept getCachedConcept(long id) {
        Long lid = new Long(id);
        if (cache.containsKey(lid)) return (Concept)cache.get(lid); else return null;
    }

    /**
     * Delete a concept.
     */
    public void removeConcept(long id) throws
           InvalidConceptException,
           DatabaseException {
        //remove the actual file
        File conceptxml = new File(root, (new Long(id)).toString());
        if (!conceptxml.exists()) {
            throw new InvalidConceptException("concept does not exist: " + id);
        }
        conceptxml.delete();
        //remove from index
        removeFromIndex(idxresource, id);
        removeFromIndex(index, id);
        updateIndex();
        //remove from cache
        cache.remove(new Long(id));
    }
}