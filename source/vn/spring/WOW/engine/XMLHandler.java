/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * XMLHandler.java 1.0, May 20, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.engine;

import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.parser.*;

import java.util.*;
import java.io.*;
import java.net.*;

import org.w3c.dom.*;
import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.InputSource;

public class XMLHandler implements ResourceHandler {
    private static final boolean debug = true;

    private Resource resource = null;
    private Profile profile = null;
    private WOWDB db = null;
    private Document doc = null;
    private Concept concept = null;
    private int objectcount;
    public  int tooltipcount;
    private int stabilitycount;

    public XMLHandler() {}

    /**
     * Returns if this handler can process the given resource.
     * @param type The resource type that this handler is supposed to
     *        handle.
     * @return If this handler handles the specified resource.
     */
    public boolean handlesResource(ResourceType type) {
        String s = type.getMime();
        return ("text/xhtml".equals(s) || "text/xml".equals(s) || "application/xml".equals(s) || "application/smil".equals(s)) && (type.getSubtype() == null);
    }

    private DOMParser createDOMParser() {
        DOMParser result = new DOMParser();
        try {
            result.setFeature("http://xml.org/sax/features/validation", false);
//            result.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
//            result.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (Exception e) {if (debug) e.printStackTrace();}
        return result;
    }

    Element getFirstElementByName(Element current, String name) {
        NodeList list = WOWStatic.getChildElementsByTagName(current,name);
        if (list.getLength()==0) return null; else return (Element)list.item(0);
    }

    private void traverse(Element element) {
        if (element == null) return;
        boolean object = false;

        //process several nodes
        String tag = element.getTagName().toLowerCase();
        Node node = element;
        if (tag.equals("if")) node = traverse_if(element);
        if (tag.equals("variable")) node = traverse_variable(element);

        //process the child elements
        traverseChildren(node);
        if (object) objectcount--;
    }

    private void traverseChildren(Node node) {
        if (node == null) return;
        Node nextnode = node.getFirstChild();
        Node startnode = node;
        while (nextnode != null) {
            node = nextnode;
            nextnode = node.getNextSibling();
            if (node instanceof Element) traverse((Element)node);
        }
        startnode.normalize();
    }

    private String findNumberDone(String name) throws DatabaseException, ParserException {
        if (!"numberdone".equals(name) && !"numbertodo".equals(name)) return null;
        Vector v = db.getConceptDB().getConceptList();
        Vector r = new Vector();
        String course = getCourse();
        for (int i=0;i<v.size();i++) if (((String)v.get(i)).startsWith(course+".")) {
            String conceptname = (String)v.get(i);
            Concept concept = null;
            try {concept = WOWStatic.DB().getConceptDB().getConcept(WOWStatic.DB().getConceptDB().findConcept(conceptname));}
            catch (Exception e) {}
            if (concept != null) {
            	String type = concept.getType();
            	if (type != null && type.equals("page")) r.add(v.get(i));
            }
        }
        int total = r.size();
        v = new Vector();
        for (int i=0;i<r.size();i++) if (!evaluateBooleanExpr((String)r.get(i)+".visited")) v.add(r.get(i));
        int result = 0;
        if ("numberdone".equals(name)) result = total-v.size(); else result = v.size();
        String resultstring = new Integer(result).toString();
        resultstring = resultstring + (result == 1?" page":" pages");
        return resultstring;
    }

    private Node traverse_variable(Element element) {
        try {
            String name = element.getAttribute("name");
            String result = findNumberDone(name);
            if (result == null) result = evaluateStringExpr(name);
            Node nresult = replaceNode(element, doc.createTextNode(result));
            return nresult;
        } catch (Exception e) {
            if (debug) e.printStackTrace();
            return replaceNode(element, createErrorElement("["+e.getMessage()+"]"));
        }
    }

    String getTextChild(Element element, String name) {
        Element child = (Element)WOWStatic.getChildElementsByTagName(element,name).item(0);
        if (child == null) return "";
        Node node = child.getFirstChild();
        if (node == null) return "";
        return node.getNodeValue();
    }

    private Element traverse_if(Element element) {
        try {
            String expr = element.getAttribute("expr");
            Element block = null;
            NodeList blocks = WOWStatic.getChildElementsByTagName(element, "block");
            if (evaluateBooleanExpr(expr)) block = (Element)blocks.item(0); else if (blocks.getLength()>1) block = (Element)blocks.item(1);
            if (block == null) {
                element.getParentNode().removeChild(element);
                return null;
            }
            traverseChildren(block);
            DocumentFragment frag = doc.createDocumentFragment();
            NodeList children = block.getChildNodes();
            while (children.getLength()>0) frag.appendChild(children.item(0));
            replaceNode(element, frag);
        } catch (Exception e) {
            if (debug) e.printStackTrace();
            return (Element)replaceNode(element, createErrorElement("["+e.getMessage()+"]"));
        }
        return null;
    }

    private Object evaluateExpr(String expr) throws ParserException {
        Parser parser = WOWStatic.createUMParser(profile);
        expr = (concept != null?WOWStatic.fixExpr(expr, getCourse(), concept.getName()):expr);
        return parser.parse(expr);
    }

    private boolean evaluateBooleanExpr(String expr) throws ParserException {
        Object o = evaluateExpr(expr);
        if (o instanceof Float) return ((Float)o).intValue() != 0;
        if (o instanceof Boolean) return ((Boolean)o).booleanValue();
        return false;
    }

    int evaluateIntExpr(String expr) throws ParserException {
        Object o = evaluateExpr(expr);
        if (o instanceof Float) return ((Float)o).intValue();
        return -1;
    }

    private String evaluateStringExpr(String expr) throws ParserException {
        Object o = evaluateExpr(expr);
        if (o instanceof Float) return new Integer(((Float)o).intValue()).toString();
        return o.toString();
    }

    private String getCourse() {
        String course = null;
        try {course = profile.getAttributeValue("personal", "course");} catch (InvalidAttributeException e) {if (debug) e.printStackTrace();}
        return course;
    }

    private String getFullConceptName(String cname) {
        String course = getCourse();
        ConceptDB cdb = db.getConceptDB();
        try {
            try {
                cdb.findConcept(cname);
                return cname;
            } catch (InvalidConceptException ie1) {
                try {
                    cdb.findConcept(course+"."+cname);
                    return course+"."+cname;
                } catch (InvalidConceptException ie2) {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    Element traverse_object(Element element) {
        String errormessage = "unknown object";
        try {
            String type = element.getAttribute("type");
            if (!(type.startsWith("wow/") || type.equals("wow") || type.endsWith("/wow"))) return element;

            String data = element.getAttribute("data");
            String name = element.getAttribute("name");
            String returnfile = null;
            if (name.equals("")) {
                //object is a special inserted object like header or footer
                errormessage = data;
                try {
                    returnfile = (new URL(new URL(resource.getFakeURL()), data)).toString();
                } catch (MalformedURLException e) {if (debug) e.printStackTrace();}
            } else {
                //object is a conditional WOW! object
                stabilitycount++;
                //get concept name
                errormessage = name;
                name = getFullConceptName(name);
                if (name == null) return (Element)replaceNode(element, createErrorElement("[Object not found: "+errormessage+"]"));

                //update user model
                WOWStatic.PM().accessedConcept(profile, name);

                //get return fragment
                String show = "";
                try {
                    show = getShowability(name);
                } catch (Exception e) {if (debug) e.printStackTrace(); return (Element)replaceNode(element, createErrorElement("["+e.getMessage()+": "+errormessage+"]"));}
                if (show.equals("")) return (Element)replaceNode(element, createErrorElement("[Invalid showability value: "+errormessage+"]"));
                ConceptDB cdb = db.getConceptDB();
                CaseGroup casegroup = cdb.getConcept(cdb.findConcept(name)).getAttribute("showability").getCasegroup();
                int i = 0; boolean casefound = false;
                while (i != (casegroup.getCaseValues().size())) {
                    String casevalue = ((Case)casegroup.getCaseValues().get(i)).getValue().trim();
                    if (show.equals(casevalue)) {casefound = true;break;}
                    i++;
                }
                if (casefound) returnfile = ((Case)casegroup.getCaseValues().get(i)).getReturnfragment().trim();
                    else returnfile = casegroup.getDefaultFragment().trim();
            }
            if (returnfile == null) return (Element)replaceNode(element, createErrorElement("[Invalid object: "+errormessage+"]"));

            //parse object
            Resource objectr = null;
            try {objectr = WOWStatic.HM().locateResource(returnfile);} catch (Exception e) {
                return (Element)replaceNode(element, createErrorElement("[Unable to locate object file: "+returnfile+"]"));
            }
            InputSource input = new InputSource(objectr.getInputStream());
            DOMParser parser = createDOMParser();
            parser.parse(input);
            Document objectdoc = parser.getDocument();

            Element importdoc = (Element)doc.importNode(objectdoc.getDocumentElement(), true);
            return (Element)replaceNode(element, importdoc);
        } catch (Exception e) {
            if (debug) e.printStackTrace();
            return (Element)replaceNode(element, createErrorElement("[An unexpected error occured ("+e.getMessage()+"): "+errormessage+"]"));
        }
    }

    private String getShowability(String objectname) throws ParserException {
        String result = evaluateStringExpr(objectname+".showability").trim();
        //Create unique object reference name
        String uname = concept.getName()+".*$$*"+resource.getFakeURL()+"_"+stabilitycount+"_pageStability";
        //Should we use stability?
        if (concept == null) return result;
        if (!concept.hasStableProperty()) return result;
        if (concept.isFreezeStable()) if (!evaluateBooleanExpr(concept.getStableExpression())) {
            //Remove stability information
            profile.getValues().remove(uname);
            return result;
        }
        //Get stability information
        AttributeValue value = (AttributeValue)profile.getValues().get(uname);
        if (value != null) {
            return value.getValue();
        }
        //Store stability information
        value = new AttributeValue();
        value.setValue(result);
        profile.getValues().put(uname, value);
        return result;
    }

    private Element createErrorElement(String text) {
        Element s = doc.createElement("span");
        s.setAttribute("class", "error");
        s.appendChild(doc.createTextNode(text));
        return s;
    }

    private Node replaceNode(Node oldnode, Node newnode) {
        oldnode.getParentNode().replaceChild(newnode, oldnode);
        return newnode;
    }
    
    
    /**
     * Processes the resource using the user model and database and
     * returns the new resource.
     * @param resource The resource to be processed.
     * @param profile The user profile that is to be used.
     * @param db A reference to the database that is to be used.
     * @return The processed resource.
     */
    public synchronized Resource processResource(Resource resource, Profile profile, WOWDB db) throws
                    ProcessorException {
        objectcount = 0; tooltipcount = 0; stabilitycount = 0;
        Resource result = (Resource)resource.clone();
        this.resource = resource;
        this.profile = profile;
        this.db = db;
        try {
            if (resource.getConceptName() != null) concept = db.getConceptDB().getConcept(db.getConceptDB().findConcept(resource.getConceptName()));
        } catch (Exception e) {if (debug) e.printStackTrace();}
        try {
            // get the document
            Object data = resource.getObjectData();
            if ((data == null) || (!(data instanceof Document))) {
                // parse the document if it doesn't exist
                InputSource input = new InputSource(resource.getInputStream());
                try {
                    String course = profile.getAttributeValue("personal", "course");
                    String baseID = resource.getBaseID();
                    //Loc Nguyen add
                    //if(baseID == null) baseID = "http://localhost:8080/wow/";
                    //System.out.println("@Loc Nguyen: name=" + resource.getURL() + ", #" + resource.getBaseID());
                    input.setSystemId( (new URL(new URL(baseID), course)).toString()+"/" );
                } catch (Exception e){
                	System.out.println("XMLHandler: unable to set base URL: "+resource.getBaseID());
                }
                DOMParser parser = createDOMParser();
                parser.parse(input);
                doc = parser.getDocument();
            } else {
                doc = (Document)data;
            }

            // process the document
            traverse(doc.getDocumentElement());

            // store the data
            result.setObjectData(doc);
            result.getType().setSubtype("xml");
        } catch (Exception e) {
            if (debug) e.printStackTrace();
            String errorreport = "<html><head><title>Error loading document: "+resource.getFakeURL()+"</title></head>"+
                                 "<body><span class=\"error\"><p><h1>[Error loading document: "+resource.getFakeURL()+"]</h1></p></span>"+
                                 WOWStatic.normalize(e.getMessage())+"</body></html>";
            InputStream ais = new ByteArrayInputStream(errorreport.getBytes());
            result.setInputStream(ais);
            result.setType(new ResourceType("text/xhtml"));
            result.setReady(false);
        }
        return result;
    }
}
