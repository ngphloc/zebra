/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * XHTMLHandler.java 1.0, March 28, 2008
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
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;
import vn.spring.zebra.helperservice.WOWContextListener;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import org.w3c.dom.*;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.*;

public class XHTMLHandler implements ResourceHandler {
    private static final boolean debug = true;

    private Resource resource = null;
    private Profile profile = null;
    private WOWDB db = null;
    private Document doc = null;
    private Concept concept = null;
    private int objectcount;
    private int tooltipcount;
    private int stabilitycount;

    public XHTMLHandler() {}

    /**
     * Returns if this handler can process the given resource.
     * @param type The resource type that this handler is supposed to
     *        handle.
     * @return If this handler handles the specified resource.
     */
    public boolean handlesResource(ResourceType type) {
        String s = type.getMime();
        return ("text/xhtml".equals(s)) && ("xml".equals(type.getSubtype()));
    }

    DOMParser createDOMParser() {
        DOMParser result = new DOMParser();
        try {
            result.setFeature("http://xml.org/sax/features/validation", false);
            result.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            result.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (Exception e) {if (debug) e.printStackTrace();}
        return result;
    }

    private Element getFirstElementByName(Element current, String name) {
        NodeList list = WOWStatic.getChildElementsByTagName(current,name);
        if (list.getLength()==0) return null; else return (Element)list.item(0);
    }

    private void checkEmptyTitle(Document doc) {
        Element root = doc.getDocumentElement();
        Element head = getFirstElementByName(root, "head");
        if (head == null) return;
        Element title = getFirstElementByName(head, "title");
        if (title == null) return;
        Node textnode = title.getFirstChild();
        if (textnode == null) {
            textnode = doc.createTextNode("title");
            title.appendChild(textnode);
        } else if (textnode.getNodeValue().trim().equals("")) textnode.setNodeValue("title");
    }

    private void traverse(Element element) {
        if (element == null) return;
        boolean object = false;

        //process several nodes
        String tag = element.getTagName().toLowerCase();
        Node node = element;
        if (tag.equals("html")) node = traverse_html(element);
        if (tag.equals("a")) node = traverse_a(element);
        if ((objectcount < 5) && (tag.equals("object"))) {objectcount++;object=true;node = traverse_object(element);}
        if (tag.equals("head")) node = traverse_head(element);
        if (tag.equals("title")) node = traverse_title(element);
        if (tag.equals("servlet")) node = traverse_servlet(element);
        if (tag.equals("script")) node = traverse_script(element);
        if (tag.equals("handler")) node = traverse_handler(element);
        if (tag.equals("span")) node = traverse_span(element);
        if (tag.equals("pre")) node = traverse_pre(element);
        //added by Loc Nguyen 2009 October 15
        if (tag.equals("learningpath")) node = traverse_learningpath(element);
        //added by Loc Nguyen 2009 November 5
        if (tag.equals("collaborative")) node = traverse_collaborative(element);
        //added by Loc Nguyen 2009 November 5
        if (tag.equals("search")) node = traverse_search(element);

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

    private Node traverse_span(Element element) {
        if (element.getAttribute("class").equals("conditional")) {
            try {
                String conceptname = getFullConceptName(element.getAttribute("id"));
                int v = 40;
                try {v = evaluateIntExpr(conceptname+".visibility");} catch (Exception e) {}
                int[] vc = {0,WOWStatic.VC1,WOWStatic.VC2,WOWStatic.VC3,WOWStatic.VC4,WOWStatic.VC5,WOWStatic.VC6,WOWStatic.VC7,WOWStatic.VC8,WOWStatic.VC9,WOWStatic.VC10};
                int i;for (i=10;i>=0 && v<vc[i];i--);
                element.setAttribute("class", "c"+i*10);
                if (i == 0) {
                    traverseChildren(element);
                    Comment comment = doc.createComment(serializeElement(element));
                    return replaceNode(element, comment);
                }
                if (i == 1) {
                    Element img = doc.createElement("img");
                    element.getParentNode().insertBefore(img, element);
                    String Y = "Y"+tooltipcount; String X = "X"+tooltipcount;
                    img.setAttribute("id", Y);
                    img.setAttribute("src", "/wow/lib/tooltipicon.gif");
                    img.setAttribute("onMouseOver", "showTip('"+Y+"','"+X+"')");
                    img.setAttribute("onMouseOut", "hideTip('"+X+"')");
                    element.setAttribute("style", "visibility: hidden; background: lightblue; position: absolute;");
                    element.setAttribute("id", X);
                    tooltipcount++;
                }
            } catch (Exception e) {if (debug) e.printStackTrace();}
        }
        return element;
    }

    private String getTextChild(Element element, String name) {
        Element child = (Element)WOWStatic.getChildElementsByTagName(element,name).item(0);
        if (child == null) return "";
        Node node = child.getFirstChild();
        if (node == null) return "";
        return node.getNodeValue();
    }

    //Modified by Loc Nguyen 2009.12
    private Element handler_servlet(Element element, boolean handler) {
        String name = getTextChild(element, "name").trim();
        QUERY_TYPE evalType = ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE;
        String link = getTextChild(element, "linkdescription").trim();
        Element a = doc.createElement("a");

        String href = "";
        //Added by Loc Nguyen 2009.12
        if(name.equals("KnowledgeConfig") && 
        	(evalType == QUERY_TYPE.OVERLAY_BAYESIAN || evalType ==QUERY_TYPE.DYN_OVERLAY_BAYESIAN) &&
        	WOWStatic.VM().isViewTypeExisting("ZebraTOCView")) {
        	try {
		    	href = WOWStatic.config.Get("CONTEXTPATH") + "/Get?wndName=ZebraTOC";
		    	String userid = profile.getAttributeValue("personal", "id");
		    	String course = profile.getAttributeValue("personal", "course");
		    	a.setAttribute("target", userid + "$" + course + "$" + "ZebraTOC");
        	}
        	catch(Exception e) {
        		e.printStackTrace();
        	}
        }
        else {
            href = (handler?WOWStatic.config.Get("CONTEXTPATH")+"/ViewGet/"+getCourse()+"/?handler="+name:WOWStatic.config.Get("CONTEXTPATH")+"/"+name);
        }
        
        a.setAttribute("href", href);
        Text text = doc.createTextNode(link);
        a.appendChild(text);
        return (Element)replaceNode(element, a);
    }

    private Element traverse_script(Element element) {
        if (element.getChildNodes().getLength() == 0) element.appendChild(doc.createTextNode(" "));
        element.setAttribute("xml:space", "preserve");
        return element;
    }

    private Element traverse_pre(Element element) {
        element.setAttribute("xml:space", "preserve");
        return element;
    }

    private Element traverse_handler(Element element) {
        traverseChildren(element);
        return handler_servlet(element, true);
    }

    private Element traverse_servlet(Element element) {
        traverseChildren(element);
        return handler_servlet(element, false);
    }

    private Element traverse_html(Element element) {
        NodeList nodes = WOWStatic.getChildElementsByTagName(element,"head");
        Element head;
        if (nodes.getLength() == 0) {
            head = doc.createElement("head");
            element.insertBefore(head, element.getFirstChild());
        } else {
            head = (Element)nodes.item(0);
        }
        nodes = WOWStatic.getChildElementsByTagName(element,"body");
        if (nodes.getLength() == 0) {
            Element body = doc.createElement("body");
            element.insertBefore(body, head.getNextSibling());
            for (Node next = body.getNextSibling();next != null;next = body.getNextSibling()) {
                body.appendChild(next);
            }
        }
        return element;
    }

    private Element traverse_head(Element element) {
        NodeList nodes = WOWStatic.getChildElementsByTagName(element,"title");
        Element title;
        if (nodes.getLength() == 0) {
            title = doc.createElement("title");
            element.insertBefore(title, element.getFirstChild());
        } else {
            title = (Element)nodes.item(0);
        }
        Element link = doc.createElement("link");
        element.appendChild(link);
        link.setAttribute("rel", "stylesheet");
        link.setAttribute("type", "text/css");
        link.setAttribute("href", WOWStatic.CourseInfoTbl.getCourseInfo(getCourse()).stylesheet);
        return element;
    }

    private Element traverse_title(Element element) {
        Text text;
        if (element.hasChildNodes()) text = (Text)element.getFirstChild(); else {text = doc.createTextNode("");element.appendChild(text);}
        if (text.getNodeValue().trim().equals("")) text.setNodeValue((concept!=null?concept.getTitle():getCourse()));
        return element;
    }

    private Element traverse_a(Element element) {
        String href = element.getAttribute("href");
        String cl = element.getAttribute("class");
        if (cl == null) cl = "";
        if (!(cl.toLowerCase().trim().equals("conditional") || cl.toLowerCase().trim().equals("unconditional"))) return element;
        if (href == null) return element;
        href = href.trim();
        cl = cl.trim().toLowerCase();
        String danchor = "";
        //destination anchor
        if (href.indexOf("#")>=0) {
            danchor = href.substring(href.indexOf("#")+1,href.length());
            href = href.substring(0,href.indexOf("#"));
        }
        //get the full url to the linked concept
        String concepturl = null;
        try {
            concepturl = (new URL(new URL(resource.getFakeURL()), href)).toString();
        } catch (MalformedURLException e) {if (debug) e.printStackTrace();}
        if (href.endsWith("|DoTest") || href.endsWith(".frm") || href.endsWith("|DoAppletTest")) href = WOWStatic.config.Get("CONTEXTPATH")+"/ViewGet/" + concepturl;
        //get the concept name of the linked concept
        String conceptname = null;
        try {
            conceptname = db.getConceptDB().getLinkedConcept(concepturl);
            if (conceptname == null) conceptname = getFullConceptName(href);
        } catch (Exception e) {if (debug) e.printStackTrace();}
        if (!("".equals(conceptname) || conceptname == null)) {
            element.setAttribute("href", conceptname);
            Element a = LinkAdaptation.createLink(element, profile, "MAINVIEW", doc);
            if (!"".equals(danchor)) {
                Element inta = (a.getTagName().equals("a")?a:(Element)WOWStatic.getChildElementsByTagName(a,"a").item(0));
                inta.setAttribute("href", inta.getAttribute("href")+(danchor.equals("")?"":"&danchor="+danchor+"#"+danchor));
            }
            return (Element)replaceNode(element, a);
        } else element.setAttribute("href", href);
        return element;
    }

    private Object evaluateExpr(String expr) throws ParserException {
        Parser parser = WOWStatic.createUMParser(profile);
        return parser.parse(expr);
    }

    private boolean evaluateBooleanExpr(String expr) throws ParserException {
        Object o = evaluateExpr(expr);
        if (o instanceof Float) return ((Float)o).intValue() != 0;
        if (o instanceof Boolean) return ((Boolean)o).booleanValue();
        return false;
    }

    private int evaluateIntExpr(String expr) throws ParserException {
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

    private Element traverse_object(Element element) {
        String errormessage = "unknown object";
        try {
            String type = element.getAttribute("type");
            if (!(type.startsWith("wow/") || type.equals("wow") || type.endsWith("/wow"))) return element;

            String data = element.getAttribute("data");
            String name = element.getAttribute("name");
            String returnfile = null;
            if (name.equals("") && !data.equals("")) {
                //object is a special inserted object like header or footer
                errormessage = data;
                try {
                    returnfile = (new URL(new URL(resource.getFakeURL()), data)).toString();
                } catch (MalformedURLException e) {if (debug) e.printStackTrace();}
            } else if (!name.equals("")) {
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
            if (!name.equals("")) objectr.setConceptName(name); else objectr.setConceptName((concept!=null?concept.getName():getCourse()));
            objectr = WOWStatic.HM().processComplete(objectr, profile);
            Object odata = objectr.getObjectData();
            Element importdoc;
            if (odata instanceof Document) {
                importdoc = (Element)doc.importNode(((Document)odata).getDocumentElement(), true);
            } else {
                importdoc = doc.createElement("pre");
                importdoc.setAttribute("xml:space", "preserve");
                StringBuffer sb = new StringBuffer();
                byte[] buf = new byte[1024];
                int nr = 1024;
                while (nr != -1) {
                    nr = objectr.getInputStream().read(buf, 0, 1024);
                    if (nr != -1) sb.append(new String(buf, 0, nr));
                }
                importdoc.appendChild(doc.createTextNode(sb.toString()));
            }
            replaceNode(element, importdoc);
            return null;
        } catch (Exception e) {
            if (debug) e.printStackTrace();
            return (Element)replaceNode(element, createErrorElement("[An unexpected error occured ("+e.getMessage()+"): "+errormessage+"]"));
        }
    }

    private String getShowability(String objectname) throws ParserException {
        if (concept == null) throw new ParserException("Unable to get showability (no concept related to this page).");
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

    public static String serializeElement(Element element) throws IOException {
        StringWriter writer = new StringWriter();
        OutputFormat of = new OutputFormat("html", "UTF-8", true);
        of.setOmitDocumentType(true);
        of.setLineWidth(100);
        of.setNonEscapingElements(new String[] {"style"});
        BaseMarkupSerializer ser = SerializeHandler.getSerializer(writer, of);
        ser.serialize(element);
        return writer.toString();
    }

    //added by Loc Nguyen 2009 October 15
    private Element traverse_learningpath(Element element) {
    	try {
	    	String userid = profile.getAttributeValue("personal", "id");
	    	String course = profile.getAttributeValue("personal", "course");
	    	ArrayList<ArrayList<String>> path = WOWContextListener.getInstance().getTriUMServer().
    			getCommunicateService().getQueryDelegator().
    			learningPathQuery(userid, course);

	    	Element div = doc.createElement("div");
	    	if(path.size() == 0) div.appendChild(doc.createTextNode("No result!"));
	    	
			for(int i = path.size() - 1; i >= 0; i--) {
				ArrayList<String> concepts = path.get(i);
				
				if(concepts.size() > 1) div.appendChild(doc.createTextNode("("));
				for(int j = 0; j < concepts.size(); j++) {
					div.appendChild(LinkAdaptation.createLink(concepts.get(j), profile, "MAINVIEW", doc));
					if(j < concepts.size() - 1) div.appendChild(doc.createTextNode(", "));
				}
				if(concepts.size() > 1) div.appendChild(doc.createTextNode(")"));
				
				if(i > 0) div.appendChild(doc.createTextNode(" => "));
			}
	    	return (Element)replaceNode(element, div);
			
    	}
    	catch(Throwable e) {
    		e.printStackTrace();
    		return (Element)replaceNode(element, createErrorElement("No result!"));
    	}
    }
    
    //added by Loc Nguyen 2009 November 5
    private Element traverse_search(Element element) {
    	try {
	    	String userid = profile.getAttributeValue("personal", "id");
	    	String course = profile.getAttributeValue("personal", "course");
	    	String context = WOWStatic.config.Get("CONTEXTPATH");

	    	Element a = doc.createElement("a");
	    	a.setAttribute("href", context + "/Get?wndName=Search");
	    	a.setAttribute("target", userid + "$" + course);
	    	a.appendChild(doc.createTextNode("Search"));
	    	return (Element)replaceNode(element, a);
    	}
    	catch(Throwable e) {
    		e.printStackTrace();
    		return (Element)replaceNode(element, createErrorElement("Error!"));
    	}
    }

    //added by Loc Nguyen 2009 November 5
    private Element traverse_collaborative(Element element) {
    	try {
	    	String userid = profile.getAttributeValue("personal", "id");
	    	String course = profile.getAttributeValue("personal", "course");
	    	String context = WOWStatic.config.Get("CONTEXTPATH");

	    	Element a = doc.createElement("a");
	    	a.setAttribute("href", context + "/Get?wndName=Collaborative");
	    	a.setAttribute("target", userid + "$" + course);
	    	a.appendChild(doc.createTextNode("Collaborative"));
	    	return (Element)replaceNode(element, a);
    	}
    	catch(Throwable e) {
    		e.printStackTrace();
    		return (Element)replaceNode(element, createErrorElement("Error!"));
    	}
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
                result.setReady(true);
                return result;
            }
            doc = (Document)data; 

            // process the document
            traverse(doc.getDocumentElement());

            // check empty title
            checkEmptyTitle(doc);
            result.setType(new ResourceType("text/xhtml", "xhtml"));
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