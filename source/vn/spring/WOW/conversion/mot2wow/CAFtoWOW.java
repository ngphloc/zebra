/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * CAFtoWOW.java 2.0, December 7, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.conversion.mot2wow;

import java.io.*;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;

import org.w3c.dom.*;
import com.sun.xml.tree.*;

import vn.spring.WOW.genparser.*;
import vn.spring.WOW.WOWDB.XMLUtil;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.exceptions.InvalidAttributeException;
import vn.spring.WOW.WOWStatic;

public class CAFtoWOW {
    //arguments
    private static boolean debug = false;
    private boolean filexml = true;
    private boolean silent = false;
    private LinkedList programfiles = new LinkedList();
    private String author = null;
    private String course = null;
    private String CAFfile = null;
    private String WOWdir = "C:\\wow";
    //CAF data
    private LinkedList rootConcepts = new LinkedList();
    private CAFLesson rootLesson = null;
    private Hashtable CAFlinks = new Hashtable();
    //WOW data
    public Vector concepts = new Vector();
    private Hashtable WOWtoCAFlinks = new Hashtable();
    private Hashtable conceptbyname = new Hashtable();
    private Vector fragmentnames = new Vector();
    //overall data
    private Hashtable properties = new Hashtable();
    private Hashtable motinfo = new Hashtable();

    public CAFtoWOW(String[] args) throws ParserException, IOException, InvalidParametersException {
        debug = false;
        analyzeArguments(args);
        if (filexml) {
            XmlDocument doc = XMLUtil.getXML(new File(CAFfile));
            startProcess(doc);
        }
    }

    public void startProcess(XmlDocument doc) throws ParserException, IOException {
        if (!silent) System.out.print("Reading CAF file...");
        readCAF(doc);
        if (!silent) System.out.print(" done!\nCreating WOW! concepts...");
        createConcepts();
        if (!silent) System.out.print(" done!\nWriting WOW! xhtml files...");
        writeXHTMLfiles();
        if (programfiles.size() != 0) if (!silent) System.out.print(" done!\nAnalyzing program...");
        interpretPrograms();
        if (!silent) System.out.print(" done!\nWriting WOW! concept files...");
        writeWOW();
        if (!silent) System.out.println(" done!");
    }

/*
 ***** analyzeArguments *****
 */

    private void analyzeArguments(String[] args) throws InvalidParametersException {
        LinkedList list = new LinkedList();
        for (int i=0;i<args.length;i++) list.add(args[i]);
        int arg=1;
        while (list.size() > 0) {
            String s = (String)list.getFirst();
            if (s.equals("-d")) debug = true; else
            if (s.equals("-s")) silent = true; else
            if (s.equals("-o")) filexml = false; else
            if (s.startsWith("-r")) {
                WOWdir = s.substring(2).trim();
            } else {
                if (arg == 1) CAFfile = s; else
                if (arg == 2) course = s; else
                if (arg == 3) author = s; else
                if (arg >= 4) programfiles.add(s);
                arg++;
            }
            list.removeFirst();
        }
        //check number of arguments
        if (arg < 4) throw new InvalidParametersException("Invalid number of arguments");
        //check if the CAF file exists
        if (filexml) {
            if (!(new File(CAFfile)).exists()) throw new InvalidParametersException("File not found: "+CAFfile);
        }
        //check if coursename is valid
        boolean b = course.length() > 0;
        if (b) b = Character.isJavaIdentifierStart(course.charAt(0));
        for (int i=1;(i<course.length()) && (b);i++) b = b && Character.isJavaIdentifierPart(course.charAt(i));
        if (!b) throw new InvalidParametersException("The specified coursename is not a valid identifier");
        //check if WOW! dir is valid
        if (!(new File(WOWdir)).exists()) throw new InvalidParametersException("The WOW! directory does not exist: "+WOWdir);
        //check if author exists
        if (!(new File(WOWdir, "author/authorfiles/"+author)).exists()) throw new InvalidParametersException("The specified author '"+author+"' does not exist");
    }

/*
 ***** readCAF *****
 */

    private void readCAF(XmlDocument doc) throws IOException {
        Element eroot = doc.getDocumentElement();
        NodeList nodes = eroot.getChildNodes();
        for (int i=0;i<nodes.getLength();i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals("domainmodel")) readDomain((Element)node);
            if (node.getNodeName().equals("goalmodel")) readGoal((Element)node);
        }
        translateRelatednessKeys();
    }

    private void translateRelatednessKeys() {
        for (int i=0;i<rootConcepts.size();i++) {
            translateRelatednessConcept((CAFConcept)rootConcepts.get(i));
        }
    }

    private void translateRelatednessConcept(CAFConcept c) {
        for (Enumeration e=c.relations.elements();e.hasMoreElements();) {
            Vector v = (Vector)e.nextElement();
            for (int i=0;i<v.size();i++) translateRelatednessRel((CAFRelation)v.get(i));
        }
        for (int i=0;i<c.concepts.size();i++) translateRelatednessConcept((CAFConcept)c.concepts.get(i));
    }

    private void translateRelatednessRel(CAFRelation rel) {
        rel.related = (CAFConcept)CAFlinks.get("\\"+(String)rel.attrs.get("linkkey"));
        if (rel.related == null) System.out.println("rel link not found for: \\"+rel.attrs.get("linkkey"));
    }

    private void readGoal(Element egoal) throws IOException {
        rootLesson = readGoalLesson((Element)egoal.getChildNodes().item(0), null, 1, 1);
    }

    private CAFLesson readGoalLesson(Element elesson, CAFLesson parent, int order, int level) throws IOException {
        CAFLesson result = new CAFLesson(parent);
        NodeList nodes = elesson.getChildNodes();
        int sorder = 1;
        for (int i=0;i<nodes.getLength();i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals("link")) {
                CAFLink link = new CAFLink("\\"+XMLUtil.nodeValue(node), result);
                String type = "[unknown]";
                if (!CAFlinks.containsKey(link.link)) System.out.println("Invalid key: "+link.link); else
                    type = ((CAFAttribute)CAFlinks.get(link.link)).name;
                if (!((CAFAttribute)CAFlinks.get(link.link)).contents.trim().equals("")) {
                    addCAFattributes((Element)node, link.attrs, sorder, level+1, type);
                    result.links.add(link);
                }
                sorder++;
            } else if (node.getNodeName().equals("lesson")) {
                result.lessons.add(readGoalLesson((Element)node, result, sorder, level+1)); sorder++;
            }
        }
        addCAFattributes(elesson, result.attrs, order, level, "[group]");
        return result;
    }

    private void addCAFattributes(Element eitem, Hashtable attrs, int order, int level, String type) {
        NamedNodeMap map = eitem.getAttributes();
        for (int i=0;i<map.getLength();i++) {
            if (map.item(i).getNodeName().equals("weight")) {
                if (map.item(i).getNodeValue().trim().equals("")) {
                    attrs.put("lag_"+map.item(i).getNodeName(), new Integer(0));
                } else {
                    attrs.put("lag_"+map.item(i).getNodeName(), new Integer(map.item(i).getNodeValue()));
                }
            } else {
                attrs.put("lag_"+map.item(i).getNodeName(), map.item(i).getNodeValue());
            }
        }
        attrs.put("lag_order", new Integer(order));
        attrs.put("lag_level", new Integer(level));
        attrs.put("lag_type", type);
    }

    private void readDomain(Element edomain) throws IOException {
        NodeList nodes = edomain.getChildNodes();
        for (int i=0;i<nodes.getLength();i++) {
            Node node = nodes.item(i);
            rootConcepts.add(readDomainConcept((Element)node, null, ""));
        }
    }

    private CAFConcept readDomainConcept(Element econcept, CAFConcept parent, String sparent) throws IOException {
        CAFConcept result = new CAFConcept(parent);
        NodeList nodes = econcept.getChildNodes();
        for (int i=0;i<nodes.getLength();i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals("name")) {
                result.name = XMLUtil.nodeValue(node);
            } else if (node.getNodeName().equals("attribute")) {
                result.attributes.add(readDomainAttribute((Element)node, result, sparent + "\\" + result.name));
            } else if (node.getNodeName().equals("concept")) {
                result.concepts.add(readDomainConcept((Element)node, result, sparent + "\\" + result.name));
            } else if (node.getNodeName().equals("relation")) {
                readRelation((Element)node, result);
            }
        }
        addCAFlink(sparent + "\\" + result.name, result);
        return result;
    }

    private void readRelation(Element erelation, CAFConcept concept) {
        NodeList nodes = erelation.getChildNodes();
        String name = null;
        for (int i=0;i<nodes.getLength();i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals("name")) {
                name = XMLUtil.nodeValue(node);
            } else if (node.getNodeName().equals("relationlink")) {
                addToMappedVector(concept.relations, name, readRelationLink((Element)node));
            }
        }
    }

    private CAFRelation readRelationLink(Element erelationlink) {
        CAFRelation result = new CAFRelation();
        NamedNodeMap map = erelationlink.getAttributes();
        for (int i=0;i<map.getLength();i++) {
            if (map.item(i).getNodeName().equals("weight")) {
                if (map.item(i).getNodeValue().trim().equals("")) {
                    result.attrs.put(map.item(i).getNodeName(), new Float(0));
                } else {
                    result.attrs.put(map.item(i).getNodeName(), new Float(map.item(i).getNodeValue()));
                }
            } else {
                result.attrs.put(map.item(i).getNodeName(), map.item(i).getNodeValue());
            }
        }
        result.attrs.put("linkkey", XMLUtil.nodeValue(erelationlink));
        return result;
    }

    private void addToMappedVector(Hashtable map, Object key, Object value) {
        Vector v = null;
        if (map.containsKey(key)) {
            v = (Vector)map.get(key);
        } else {
            v = new Vector();
            map.put(key, v);
        }
        v.add(value);
    }

    private CAFAttribute readDomainAttribute(Element eattribute, CAFConcept parent, String sconcept) throws IOException {
        CAFAttribute result = new CAFAttribute(parent);
        NodeList nodes = eattribute.getChildNodes();
        for (int i=0;i<nodes.getLength();i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals("name")) {
                result.name = XMLUtil.nodeValue(node);
            } else if (node.getNodeName().equals("contents")) {
                result.contents = XMLUtil.nodeValue(node);
            }
        }
        addCAFlink(sconcept + "\\" + result.name, result);
        return result;
    }

    private void addCAFlink(String link, Object item) throws IOException {
        if (CAFlinks.containsKey(link)) {
            throw new IOException("a key conflict, caused by the use of a backslash in a name, has occured: "+link);
        }
        CAFlinks.put(link, item);
    }

/*
 ***** createConcepts *****
 */
    private int ccuid = 1;

    private void createConcepts() {
        ccProcessLesson(rootLesson);
        rootLesson.concept.setHierStruct(new ConceptHierStruct());
        ccBuildHierarchy(rootLesson);
    }

    private void ccBuildHierarchy(CAFLesson lesson) {
        if (lesson.lessons.size() > 0)
            lesson.concept.getHierStruct().firstchild = ((CAFLesson)lesson.lessons.get(0)).concept.getName();
        String lastsib = null;
        for (int i=lesson.lessons.size()-1;i>=0;i--) {
            CAFLesson sublesson = (CAFLesson)lesson.lessons.get(i);
            ConceptHierStruct chs = new ConceptHierStruct();
            sublesson.concept.setHierStruct(chs);
            chs.parent = lesson.concept.getName();
            chs.nextsib = lastsib;
            ccBuildHierarchy(sublesson);
            lastsib = sublesson.concept.getName();
        }
        ccBuildSuitability(lesson);
    }

    private void ccBuildSuitability(CAFLesson lesson) {
        Attribute sattr = null;
        Concept concept = lesson.concept;
        try {
            sattr = concept.getAttribute("suitability");
        } catch (InvalidAttributeException e) {return;}
        //set suitability
        sattr.setPersistent(false);
        PCCommon.ExprNode enode;
        PCLAGIdentifier.IDNode inode;
        ParseNode current;
        if (lesson.links.size() > 0) {
            inode = new PCLAGIdentifier.IDNode(null);
            inode.name = ((CAFLink)lesson.links.get(0)).concept.getName()+".suitability";
            current = inode;
            for (int i=1;i<lesson.links.size();i++) {
                CAFLink link = (CAFLink)lesson.links.get(i);
                inode = new PCLAGIdentifier.IDNode(null);
                inode.name = link.concept.getName()+".suitability";
                enode = new PCCommon.ExprNode(null);
                enode.operator = "||";
                enode.first = current;
                enode.second = inode;
                current = enode;
            }
            sattr.setDefault(current.toString());
        } else {
            sattr.setDefault("true");
        }
    }

    private void ccProcessLesson(CAFLesson lesson) {
        Concept concept = createLessonConcept(lesson);
        WOWtoCAFlinks.put(concept, lesson);
        conceptbyname.put(concept.getName(), concept);
        lesson.concept = concept;
        if (concept != null) concepts.add(concept);
        for (int i=0;i<lesson.links.size();i++) ccProcessLink((CAFLink)lesson.links.get(i));
        for (int i=0;i<lesson.lessons.size();i++) ccProcessLesson((CAFLesson)lesson.lessons.get(i));
    }

    private void ccProcessLink(CAFLink link) {
        Concept concept = createLinkConcept(link);
        WOWtoCAFlinks.put(concept, link);
        conceptbyname.put(concept.getName(), concept);
        fragmentnames.add(concept.getName());
        link.concept = concept;
        if (concept != null) concepts.add(concept);
    }

    private Concept createLinkConcept(CAFLink link) {
        int myuid = ccuid; ccuid++;
        Concept result = new Concept(course+".a"+myuid);
        //try to get a description
        if (!CAFlinks.containsKey(link.link)) return null;
        CAFAttribute attr = (CAFAttribute)CAFlinks.get(link.link);
        String description = attr.parent.name+"\\"+attr.name;
        result.setDescription(description);
        result.setTitle(description);
        result.setType("fragment");
        try {
            result.setResourceURL(new URL("file:/"+course+"/a"+myuid+".html"));
        } catch (MalformedURLException e) {}
        addDefaultAttributes(result);
        Attribute showability = createAttribute("showability", "", "1", AttributeType.ATTRSTR, true, true, true);
        CaseGroup cg = new CaseGroup();
        showability.setCasegroup(cg);
        cg.setDefaultFragment("file:/"+course+"/a"+myuid+".html");
        Case c = new Case();
        c.setValue("1");
        c.setReturnfragment("file:/"+course+"/empty.xhtml");
        cg.getCaseValues().add(c);
        result.getAttributes().add(showability);
        addSpecialAttributes(link.attrs, result);
        Attribute sattr = null;
        try {
            sattr = result.getAttribute("suitability");
        } catch (InvalidAttributeException e) {return result;}
        Vector vt = new Vector(); vt.add(new Assignment(result.getName()+".showability", "0"));
        Vector vf = new Vector(); vf.add(new Assignment(result.getName()+".showability", "1"));
        addRule(sattr, result.getName()+".suitability", vt, vf);
        return result;
    }

    private Concept createLessonConcept(CAFLesson lesson) {
        int myuid = ccuid; ccuid++;
        Concept result = new Concept(course+".l"+myuid);
        String description = findLessonDescription(lesson);
        result.setDescription(description);
        result.setTitle(description);
        result.setType("page");
        try {
            result.setResourceURL(new URL("file:/"+course+"/l"+myuid+".xhtml"));
        } catch (MalformedURLException e) {}
        addDefaultAttributes(result);
        addSpecialAttributes(lesson.attrs, result);
        return result;
    }

    private void addSpecialAttributes(Hashtable attrs, Concept concept) {
        for (Enumeration e=attrs.keys();e.hasMoreElements();) {
            String name = (String)e.nextElement();
            Object value = attrs.get(name);
            int type = (value instanceof Integer?AttributeType.ATTRINT:(value instanceof Boolean?AttributeType.ATTRBOOL:AttributeType.ATTRSTR));
            String svalue = (type == AttributeType.ATTRSTR?"\""+value+"\"":value.toString());
            concept.getAttributes().add(createAttribute(name, "", svalue, type, true, false, false));
        }
    }

    private Attribute createAttribute(String name, String des, String def, int type, boolean readonly, boolean system, boolean persistent) {
        Attribute result = new Attribute(name, type);
        result.setDescription(des);
        result.setDefault(def);
        result.setReadonly(readonly);
        result.setSystem(system);
        result.setPersistent(persistent);
        return result;
    }

    private void addRule(Attribute a, String expr, Vector trues, Vector falses) {
        Action aa = new Action();
        aa.setCondition(expr);
        aa.getTrueStatements().addAll(trues);
        aa.getFalseStatements().addAll(falses);
        a.getActions().add(aa);
    }

    private void addDefaultAttributes(Concept c) {
        Attribute a = createAttribute("access", "", "", AttributeType.ATTRBOOL, true, true, false);
        Vector v = new Vector(); v.add(new Assignment(c.getName()+".visited", c.getName()+".visited+1"));
        addRule(a, "true", v, new Vector());
        c.getAttributes().add(a);
        c.getAttributes().add(createAttribute("visited", "", "0", AttributeType.ATTRINT, false, true, true));
        c.getAttributes().add(createAttribute("suitability", "", "false", AttributeType.ATTRBOOL, false, true, true));
        c.getAttributes().add(createAttribute("knowledge", "", "0", AttributeType.ATTRINT, false, false, true));
    }

/*
 ***** writeXHTMLfiles *****
 */

    private void writeXHTMLfiles() throws IOException {
        File dir = new File(WOWdir, course);
        if (!dir.exists()) dir.mkdir();
        xhtmlProcessLesson(rootLesson);
    }

    private void xhtmlProcessLesson(CAFLesson lesson) throws IOException {
        writeXHTMLlesson(lesson);
        for (int i=0;i<lesson.links.size();i++) writeXHTMLlink((CAFLink)lesson.links.get(i));
        for (int i=0;i<lesson.lessons.size();i++) xhtmlProcessLesson((CAFLesson)lesson.lessons.get(i));
    }

    private void writeXHTMLlink(CAFLink link) throws IOException {
        File f = new File(WOWdir, link.concept.getName().replaceAll("\\.","/")+".html");
        FileWriter fw = new FileWriter(f);
        boolean title = false;
        String ftext = "";
        if (CAFlinks.containsKey(link.link)) {
            CAFAttribute ca = (CAFAttribute)CAFlinks.get(link.link);
            title = ca.name.trim().toLowerCase().equals("title");
            ftext = ca.contents;
        }
        if (title)
            fw.write("<h1><hr>\n"+ftext+"\n</h1>\n"); else
            fw.write("<span><hr>\n"+ftext+"\n</span>\n");
        fw.close();
    }

    private void writeXHTMLlesson(CAFLesson lesson) throws IOException {
        StringBuffer html = new StringBuffer();
        html.append("<!DOCTYPE html SYSTEM \"../WOWstandard/xhtml-wowext-1.dtd\">\n");
        html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n");
        html.append("  <head>\n    <title>\n      "+findLessonDescription(lesson)+"\n    </title>\n  </head>\n");
        html.append("  <body>\n");
        for (int i=0;i<lesson.links.size();i++) {
            html.append("\n    <object name=\""+((CAFLink)lesson.links.get(i)).concept.getName()+"\" type=\"wow/text\"></object>\n    <br/>\n");
        }
        html.append("  </body>\n</html>\n");

        File f = new File(WOWdir, lesson.concept.getName().replaceAll("\\.", "/")+".xhtml");
        FileWriter fw = new FileWriter(f);
        fw.write(html.toString());
        fw.close();
    }

/*
 ***** interpretProgram *****
 */

    private static GenParser parser = null;
    private static Evaluator eval = null;
    private LinkedList programs = new LinkedList();
    private class ProgramComparator implements Comparator {
        public ProgramComparator() {}
        public int compare(Object o1, Object o2) {
            PCLAG.ProgramNode n1 = (PCLAG.ProgramNode)o1;
            PCLAG.ProgramNode n2 = (PCLAG.ProgramNode)o2;
            return n1.priority-n2.priority;
        }
        public boolean equals(Object obj) {return super.equals(obj);}
    }

    private void interpretPrograms() throws ParserException, IOException {
        //add default properties
        properties.put("next", new Boolean(true));
        properties.put("todo", new Boolean(true));
        properties.put("menu", new Boolean(true));

        //check if a program was specified
        if (programfiles == null) return;
        if (programfiles.size() == 0) return;

        //prepare parser
        parser = new GenParser();
        parser.registerParseComponent(new PCComment());
        parser.registerParseComponent(new PCLAGIdentifier());
        parser.registerParseComponent(new PCCommon());
        parser.registerParseComponent(new PCLAG());

        //prepare evaluator
        eval = new Evaluator(parser, new ConceptVariableLocator());

        //read all programs
        for (int i=0;i<programfiles.size();i++) {
            String programfile = (String)programfiles.get(i);
            //read input
            BufferedReader bin = new BufferedReader(new FileReader(programfile));
            StringBuffer r = new StringBuffer("");
            String s;
            do {
                s = bin.readLine();
                if (s != null) r.append(s+"\n");
            } while (s != null);

            //parse program
            ParseNode node = parser.parse("PROGRAM", r.toString());

            //pre-process
            LAGPreProcessor.course = course;
            LAGPreProcessor.conceptconversion = buildConceptConversion();
            node = LAGPreProcessor.process(node);

            //variable finder
            findVariables(node);
            programs.add(node);
        }
        Collections.sort(programs, new ProgramComparator());

        //process all programs
        for (Iterator i=programs.iterator();i.hasNext();) {
            ParseNode node = (ParseNode)i.next();
            //actual interpretation
            PCCommon.ConstNode enode = new PCCommon.ConstNode(null);
            enode.value = new Boolean(true);
            processNodes((ParseNode)node.get("initialization"), enode, true);
            processNodes((ParseNode)node.get("implementation"), enode, false);
        }
    }

    private Hashtable buildConceptConversion() {
        Hashtable result = new Hashtable();
        bccLesson(rootLesson, result);
        return result;
    }

    private void bccLesson(CAFLesson lesson, Hashtable result) {
        if (lesson.links.size()!=0) {
            CAFLink link = (CAFLink)lesson.links.get(0);
            DotString ds = new DotString(link.link, "\\");
            ds.set(ds.size()-1, null);
            result.put(ds.toString(), lesson.concept.getName());
            for (int i=0;i<lesson.links.size();i++) bccLink((CAFLink)lesson.links.get(i), result);
        }
        for (int i=0;i<lesson.lessons.size();i++) bccLesson((CAFLesson)lesson.lessons.get(i), result);
    }

    private void bccLink(CAFLink link, Hashtable result) {
        result.put(link.link, link.concept.getName());
    }

    private void findVariables(ParseNode node) throws ParserException {
        Concept variables = (Concept)conceptbyname.get(course+".variables");
        if (variables == null) variables = new Concept(course+".variables");
        conceptbyname.put(variables.getName(), variables);
        variables.setType("abstract");
        LinkedList simpleassign = findSimpleAssignments(node);
        for (int i=0;i<simpleassign.size();i++) {
            PCLAG.AssignNode anode = (PCLAG.AssignNode)simpleassign.get(i);
            String name = (String)anode.variable.get("name");
            Object value = anode.expr.get("value");
            if (name.startsWith("specialLAGcurrentconcept.")) {
                for (int j=0;j<fragmentnames.size();j++) {
                    addVariable(((String)fragmentnames.get(j))+name.substring(24), value);
                }
            } else {
                addVariable(name, value);
            }
        }
        if (variables.getAttributes().size()>0) concepts.add(variables);
    }

    private void addVariable(String name, Object value) throws ParserException {
        DotString ds = new DotString(name);
        String aname = ds.get(ds.size()-1);
        ds.set(ds.size()-1, null);
        String cname = ds.toString();
        Concept concept = (Concept)conceptbyname.get(cname);
        if (concept == null) return;
        if (concept.hasAttribute(aname)) return;
        int atype;
        if (value instanceof Float) atype = AttributeType.ATTRINT; else
        if (value instanceof Boolean) atype = AttributeType.ATTRBOOL; else
            atype = AttributeType.ATTRSTR;
        String def = (atype==AttributeType.ATTRINT?"0":(atype==AttributeType.ATTRBOOL?"false":"\"\""));
        Attribute attr = createAttribute(aname, "", def, atype, true, false, true);
        concept.getAttributes().add(attr);
    }

    private LinkedList findSimpleAssignments(ParseNode node) {
        LinkedList result = new LinkedList();
        if (node.getType().equals("assign")) {
            if ( ((ParseNode)node.get("expr")).getType().equals("const") ) result.add(node);
        } else {
            LinkedList snodes = node.getChildList();
            for (int i=0;i<snodes.size();i++) {
                result.addAll(findSimpleAssignments((ParseNode)snodes.get(i)));
            }
        }
        return result;
    }

    private void processNodes(ParseNode node, ParseNode enode, boolean init) throws ParserException {
        LinkedList snodes = (LinkedList)node.get("stats");
        if (snodes == null) {
            //assume single statement instead of set of statements
            snodes = new LinkedList();
            snodes.add(node);
        }
        while (snodes.size()!=0) {
            PCLAG.StatsNode stats = new PCLAG.StatsNode(null);
            boolean b = true;
            LinkedList remove = new LinkedList();
            for (int i=0;i<snodes.size()&&b;i++) {
                ParseNode snode = (ParseNode)snodes.get(i);
                if (snode instanceof PCLAG.AssignNode) {
                    stats.stats.add(snode);
                    remove.add(snode);
                } else b = false;
            }
            processAssign(stats, enode, init);
            for (int i=0;i<remove.size();i++) snodes.remove(remove.get(i));

            if (snodes.size()!=0) {
                ParseNode snode = (ParseNode)snodes.get(0);
                if (snode instanceof PCLAG.IfNode) {
                    PCLAG.IfNode ifnode = (PCLAG.IfNode)snode;
                    PCCommon.ExprNode nenode = new PCCommon.ExprNode(null);
                    nenode.operator = "&&";
                    nenode.first = ifnode.expr;
                    nenode.second = enode;
                    processNodes(ifnode.truestats, nenode, init);
                    if (ifnode.falsestats != null) {
                        PCCommon.ExprNode notnode = new PCCommon.ExprNode(nenode);
                        notnode.operator = "!";
                        notnode.first = ifnode.expr;
                        nenode.first = notnode;
                        processNodes(ifnode.falsestats, nenode, init);
                    }
                }
                snodes.remove(snode);
            }
        }
    }

    private void processAssign(ParseNode node, ParseNode enode, boolean init) throws ParserException {
        PCLAG.IfNode ifnode = new PCLAG.IfNode(null);
        ifnode.expr = enode;
        ifnode.truestats = node;
        if (isRelative(ifnode)) {
            for (int i=0;i<fragmentnames.size();i++) {
                PCLAG.IfNode newnode = (PCLAG.IfNode)ifnode.clone();
                replaceIDs(newnode, "specialLAGcurrentconcept", (String)fragmentnames.get(i));
                processParentChild(newnode, init);
            }
        } else {
            processParentChild(ifnode, init);
        }
    }

    private void processParentChild(ParseNode ifnode, boolean init) throws ParserException {
        LinkedList childifnodes = findParentChildIfNodes(ifnode);
        for (int j=0;j<childifnodes.size();j++) {
            PCLAG.IfNode inode = (PCLAG.IfNode)childifnodes.get(j);
            processNonRelative(inode.truestats, inode.expr, init);
        }
    }

    //creates a list of lists of non-relative ids from a list of relative ids
    private LinkedList nrIDs(LinkedList ids) throws ParserException {
        LinkedList work = new LinkedList();
        work.add(ids);
        LinkedList donelist = new LinkedList();
//System.out.println("-----");
        do {
//System.out.println(work);
            LinkedList current = (LinkedList)work.get(0);
            work.removeFirst();
            //find something to do
            boolean done = false;
            String id;
            LinkedList nrids = null;
            for (int i=0;i<current.size()&&!done;i++) {
                id = (String)current.get(i);
                nrids = nrID(id);
                if (nrids != null) done = true;
            }
            if (!done) {
                //didn't find anything to change so this list is done
                donelist.add(current);
            } else {
                //something changed so replace similar ids
                LinkedList newlists = replaceIDlists(current, nrids);
                for (int i=0;i<newlists.size();i++) work.add(newlists.get(i));
            }
        } while (work.size()>0);
//System.out.println(donelist);
        return donelist;
    }

    private LinkedList nrID(String id) throws ParserException {
        DotString ds = new DotString(id);
        boolean b = true;
        for (int k=0;k<ds.size()&&b;k++) {
            String idpart = ds.get(k);
            if (idpart.equals("children")) {
                b = false;
                if (k==0) throw new ParserException("children of no current concept in:\n"+id+"\n");
                if (k==ds.size()-1) throw new ParserException("no attribute of children in:\n"+id+"\n");
                LinkedList result = new LinkedList();
                result.add(ds.getString(0,k));
                result.addAll(findChildIDs(ds.getString(0,k-1)));
                return result;
            } else if (idpart.equals("subgroups")) {
                b = false;
                if (k==0) throw new ParserException("subgroups of no current concept in:\n"+id+"\n");
                if (k==ds.size()-1) throw new ParserException("no attribute of subgroups in:\n"+id+"\n");
                LinkedList result = new LinkedList();
                result.add(ds.getString(0,k));
                result.addAll(findSubgroupIDs(ds.getString(0,k-1)));
                return result;
            } else if (idpart.startsWith("child[")) {
                b = false;
                if (k==0) throw new ParserException("child of no current concept in:\n"+id+"\n");
                if (k==ds.size()-1) throw new ParserException("no attribute of child in:\n"+id+"\n");
                LinkedList result = new LinkedList();
                result.add(ds.getString(0,k));
                String s = idpart.substring(6,idpart.length()-1);
                result.addAll(findChildGroupIDs(ds.getString(0,k-1),s));
                return result;
            } else if (idpart.equals("Attribute") || idpart.equals("Concept")) {
                b = false;
                if (k==0) throw new ParserException("attribute of no current concept in:\n"+id+"\n");
                if (k==ds.size()-1) throw new ParserException("no attribute of concept attribute in:\n"+id+"\n");
                LinkedList result = new LinkedList();
                result.add(ds.getString(0,k));
                result.addAll(findAttributeIDs(ds.getString(0,k-1)));
                return result;
            } else if (idpart.equals("parent")) {
                b = false;
                if (k==0) throw new ParserException("parent of no current concept in:\n"+id+"\n");
                if (k==ds.size()-1) throw new ParserException("no attribute of parent concept in:\n"+id+"\n");
                LinkedList result = new LinkedList();
                result.add(ds.getString(0,k));
                String parent = findParentID(findParentID(ds.getString(0,k-1)));
                if (parent == null) return result;
                result.add(parent);
                return result;
            } else if (idpart.equals("group")) {
                b = false;
                if (k==0) throw new ParserException("group of no current concept in:\n"+id+"\n");
                if (k==ds.size()-1) throw new ParserException("no attribute of group concept in:\n"+id+"\n");
                LinkedList result = new LinkedList();
                result.add(ds.getString(0,k));
                String parent = findParentID(ds.getString(0,k-1));
                if (parent == null) return result;
                result.add(parent);
                return result;
            } else if (idpart.equals("Relatedness")) {
                b = false;
                if (k==0) throw new ParserException("relatedness of no current concept in:\n"+id+"\n");
                if (k==ds.size()-1) throw new ParserException("no attribute of relatedness relation in:\n"+id+"\n");
                LinkedList result = new LinkedList();
                result.add(ds.getString(0,k));
                k++;
                String nextpart = ds.get(k);
                if (!"Concept".equals(nextpart)) throw new ParserException("Relatedness.Concept construct required in:\n"+id+"\n");
                if (k==ds.size()-1) throw new ParserException("no attribute of relatedness relation in:\n"+id+"\n");
                result.addAll(findRelatednessIDs(ds.getString(0,k-2)));
                return result;
            }
        }
        return null;
    }

    private LinkedList replaceIDlists(LinkedList current, LinkedList nrids) {
        LinkedList result = new LinkedList();
        String search = (String)nrids.get(0);
        for (int i=1;i<nrids.size();i++) {
            String replace = (String)nrids.get(i);
            LinkedList list = replaceIDlist(current, search, replace);
            result.add(list);
        }
        return result;
    }

    private LinkedList replaceIDlist(LinkedList current, String search, String replace) {
        LinkedList result = new LinkedList();
        for (int i=0;i<current.size();i++) {
            StringBuffer s = new StringBuffer((String)current.get(i));
            if (s.indexOf(search) >= 0) s.replace(s.indexOf(search), search.length(), replace);
            result.add(s.toString());
        }
        return result;
    }

    private LinkedList findParentChildIfNodes(ParseNode ifnode) throws ParserException {
        try {
            LinkedList ids = findIDs(ifnode);
            LinkedList nrids = nrIDs(ids);
            LinkedList result = new LinkedList();
            for (int i=0;i<nrids.size();i++) {
                PCLAG.IfNode newifnode = (PCLAG.IfNode)ifnode.clone();
                LinkedList curids = (LinkedList)nrids.get(i);
                LinkedList curidnodes = findIDnodes(newifnode);
                for (int j=0;j<curidnodes.size();j++) {
                    PCLAGIdentifier.IDNode inode = (PCLAGIdentifier.IDNode)curidnodes.get(j);
                    inode.name = (String)curids.get(ids.indexOf(inode.name));
                }
                result.add(newifnode);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParserException(e.getMessage()+"\n"+ifnode);
        }
    }

    private LinkedList findRelatednessIDs(String parentid) throws ParserException {
        LinkedList result = new LinkedList();
        try {
            Concept concept = getConcept(parentid);
            Object cafobject = WOWtoCAFlinks.get(concept);
            if (cafobject instanceof CAFLesson) throw new ParserException("unable to compute relatedness of lesson: "+parentid);
            if (cafobject instanceof CAFLink) {
                CAFLink link = (CAFLink)cafobject;
                CAFAttribute attr = (CAFAttribute)CAFlinks.get(link.link);
                Vector rels = (Vector)attr.parent.relations.get("relatedness");
                if (rels == null) return result;
                for (int i=0;i<rels.size();i++) {
                    CAFRelation rel = (CAFRelation)rels.get(i);
                    LinkedList v = getRelLessons(rel.related);
                    for (int j=0;j<v.size();j++) if (!result.contains(v.get(j))) result.add(v.get(j));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParserException("unknown concept: "+parentid);
        }
        return result;
    }

    private LinkedList getRelLessons(CAFConcept c) throws ParserException {
        LinkedList result = new LinkedList();
        for (int i=0;i<fragmentnames.size();i++) {
            String name = (String)fragmentnames.get(i);
            Concept concept = getConcept(name);
            CAFLink link = (CAFLink)WOWtoCAFlinks.get(concept);
            CAFAttribute attr = (CAFAttribute)CAFlinks.get(link.link);
            if (c.attributes.contains(attr)) {
                String cname = link.parent.concept.getName();
                if (!result.contains(cname)) result.add(cname);
            }
        }
        return result;
    }

    private LinkedList findAttributeIDs(String parentid) throws ParserException {
        Concept concept = getConcept(parentid);
        Object cafobject = WOWtoCAFlinks.get(concept);
        if (cafobject instanceof CAFLink) throw new ParserException(".Attribute or .Concept can only be applied on a concept or group: "+parentid);
        LinkedList result = new LinkedList();
        addLinkChildren(result, (CAFLesson)cafobject);
        return result;
    }

    private LinkedList findChildIDs(String parentid) throws ParserException {
        LinkedList result = new LinkedList();
        try {
            Concept concept = getConcept(parentid);
            Object cafobject = WOWtoCAFlinks.get(concept);
            if (cafobject instanceof CAFLesson) {
                addLinkChildren(result, (CAFLesson)cafobject);
                addLessonChildren(result, (CAFLesson)cafobject);
            }
        } catch (Exception e) {
            throw new ParserException("unknown concept: "+parentid);
        }
        return result;
    }

    private LinkedList findSubgroupIDs(String parentid) throws ParserException {
        LinkedList result = new LinkedList();
        try {
            Concept concept = getConcept(parentid);
            Object cafobject = WOWtoCAFlinks.get(concept);
            if (cafobject instanceof CAFLesson) {
                addLessonChildren(result, (CAFLesson)cafobject);
            }
        } catch (Exception e) {
            throw new ParserException("unknown concept: "+parentid);
        }
        return result;
    }

    private void addLinkChildren(LinkedList children, CAFLesson lesson) {
        for (int i=0;i<lesson.links.size();i++) {
            CAFLink link = (CAFLink)lesson.links.get(i);
            children.add(link.concept.getName());
        }
    }

    private void addLessonChildren(LinkedList children, CAFLesson lesson) {
        for (int i=0;i<lesson.lessons.size();i++) {
            CAFLesson l = (CAFLesson)lesson.lessons.get(i);
            children.add(l.concept.getName());
        }
    }

    private LinkedList findChildGroupIDs(String id, String s) throws ParserException {
        try {
            LinkedList result = new LinkedList();
            ParseNode node = parser.parse("EXPR", s);
            Concept concept = getConcept(id);
            Object cafobject = WOWtoCAFlinks.get(concept);
            if (!(cafobject instanceof CAFLesson)) return null;
            CAFLesson lesson = (CAFLesson)cafobject;
            LinkedList ids = new LinkedList();
            addLinkChildren(ids, lesson);
            addLessonChildren(ids, lesson);
            for (int i=0;i<ids.size();i++) {
                String cid = (String)ids.get(i);
                ParseNode cnode = (ParseNode)node.clone();
                replaceIDs(cnode, "", cid+".lag_");
                Object o = eval.evaluate(cnode);
                if (o instanceof Float) {
                    int gid = ((Float)o).intValue();
                    if ((gid >= 0) && (gid < lesson.lessons.size()))
                        result.add( ((CAFLesson)lesson.lessons.get(gid)).concept.getName() );
                    return result;
                } else if (o instanceof Boolean) {
                    if (((Boolean)o).booleanValue()) result.add(cid);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParserException(e.getMessage());
        }
    }

    private String findParentID(String id) throws ParserException {
        try {
            Concept concept = getConcept(id);
            Object cafobject = WOWtoCAFlinks.get(concept);
            CAFLesson parent = null;
            if (cafobject instanceof CAFLesson) parent = ((CAFLesson)cafobject).parent;
            if (cafobject instanceof CAFLink) parent = ((CAFLink)cafobject).parent;
            if (parent == null) return null;
            return parent.concept.getName();
        } catch (Exception e) {
            throw new ParserException("unknown concept: "+id);
        }
    }

    private void processNonRelative(ParseNode node, ParseNode enode, boolean init) throws ParserException {
        if (init) processInit(node, enode); else processImpl(node, enode);
    }

    //replaces find by currentconcept recursively
    private void replaceIDs(ParseNode node, String find, String currentconcept) {
        if (node.getType().equals("id")) {
            replaceID((PCLAGIdentifier.IDNode)node, find, currentconcept);
        } else {
            LinkedList snodes = node.getChildList();
            for (int i=0;i<snodes.size();i++) {
                replaceIDs((ParseNode)snodes.get(i), find, currentconcept);
            }
        }
    }

    //replaces find by currentconcept in this idnode
    private void replaceID(PCLAGIdentifier.IDNode node, String find, String currentconcept) {
        if (node.name == null) return;
        if (node.name.startsWith(find+".")) {
            node.name = currentconcept + node.name.substring(find.length());
        } else if ("".equals(find)) {
            node.name = currentconcept + node.name;
        }
    }

    private LinkedList findIDs(ParseNode node) {
        LinkedList result = new LinkedList();
        LinkedList idnodes = findIDnodes(node);
        for (int i=0;i<idnodes.size();i++) {
            ParseNode idnode = (ParseNode)idnodes.get(i);
            String name = (String)idnode.get("name");
            if (!result.contains(name)) result.add(name);
        }
        return result;
    }

    private LinkedList findIDnodes(ParseNode node) {
        LinkedList result = new LinkedList();
        if (node.getType().equals("id")) {
            result.add(node);
        } else {
            LinkedList snodes = node.getChildList();
            for (int i=0;i<snodes.size();i++) {
                ParseNode snode = (ParseNode)snodes.get(i);
                result.addAll(findIDnodes(snode));
            }
        }
        return result;
    }

    private boolean isRelative(ParseNode node) {
        LinkedList ids = findIDs(node);
        boolean result = false;
        for (int i=0;i<ids.size();i++) {
            result = result || ((String)ids.get(i)).startsWith("specialLAGcurrentconcept.");
        }
        return result;
    }

    private void processInit(ParseNode node, ParseNode enode) throws ParserException {
        //evaluate enode
        Object evalue = eval.evaluate(enode);
        if (!(evalue instanceof Boolean)) throw new ParserException("invalid expression: "+enode);
        if (((Boolean)evalue).booleanValue()) {
            //execute statements
            LinkedList snodes = (LinkedList)node.get("stats");
            for (int i=0;i<snodes.size();i++) {
                PCLAG.AssignNode snode = (PCLAG.AssignNode)snodes.get(i);
                if (!internalInit(snode)) {
                    Attribute attr = findAttribute((String)snode.variable.get("name"));
                    //evaluate expression
                    Object res = eval.evaluate(snode.expr);
                    if (res instanceof Float) res = new Integer(((Float)res).intValue());
                    if (res instanceof String) attr.setDefault("\""+res+"\""); else attr.setDefault(res.toString());
                    //might also need to change the showability
                    DotString ds = new DotString((String)snode.variable.get("name"));
                    if (ds.get(ds.size()-1).equals("suitability")) {
                        ds.set(ds.size()-1, "showability");
                        attr = findAttribute(ds.toString());
                        if (res instanceof Boolean) attr.setDefault( (((Boolean)res).booleanValue()?"0":"1") );
                    }
                }
            }
        }
    }

    //is this an internal directive (like changing layout)
    private boolean internalInit(PCLAG.AssignNode node) throws ParserException {
        DotString ds = new DotString((String)node.variable.get("name"));
        if ( ((ds.size() == 3) && (ds.toString().startsWith("PM.GM"))) ||
             ((ds.size() == 2) && (ds.get(0).equals("PM"))) ) {
            String command = ds.get(ds.size()-1).toLowerCase();
            if ((command.equals("lag_todo")) || (command.equals("lag_next")) || (command.equals("lag_menu"))) {
                properties.put(command.substring(4), eval.evaluate(node.expr));
                return true;
            }
        }
        return false;
    }

    private ParseNode reduceExpression(ParseNode node, String attrname) throws ParserException {
        if (node == null) return null;
        if (node instanceof PCLAGIdentifier.IDNode) return reduceID((PCLAGIdentifier.IDNode)node, attrname); else
        if (node instanceof PCCommon.ExprNode) return reduceEXPR((PCCommon.ExprNode)node, attrname); else
            return node;
    }

    private ParseNode reduceID(PCLAGIdentifier.IDNode node, String attrname) throws ParserException {
        String varname = (String)node.get("name");
        PCCommon.ConstNode cnode = new PCCommon.ConstNode(null);
        if (varname.endsWith(".access")) {
            cnode.value = new Boolean(varname.equals(attrname));
            return cnode;
        }
        Attribute attr = findAttribute(varname);
        if ( (!attr.isPersistent()) && (!attr.isSystem()) ) {
            cnode.value = eval.evaluate(varname);
            return cnode;
        } else {
            return node;
        }
    }

    private ParseNode reduceEXPR(PCCommon.ExprNode node, String attrname) throws ParserException {
        String[] bos = new String[] {"+","-","*","/","==","!=","<","<=",">",">="};
        Vector bo = new Vector();
        for (int i=0;i<bos.length;i++) bo.add(bos[i]);

        ParseNode first = reduceExpression(node.first, attrname);
        ParseNode second = reduceExpression(node.second, attrname);
        PCCommon.ExprNode nnode = new PCCommon.ExprNode(node.getParent());
        nnode.first = first;
        nnode.second = second;
        nnode.operator = node.operator;
        node = nnode;
        if (second == null) return node;
        ParseNode aux;
        if (second.getType().equals("const")) {aux = second; second = first; first = aux;}
        if (!first.getType().equals("const")) return node;
        Object value = first.get("value");
        if (node.operator.equals("&&")) {
            if (value instanceof Boolean) {
                boolean b = ((Boolean)value).booleanValue();
                if (b) return second; else return first;
            }
        } else if (node.operator.equals("||")) {
            if (value instanceof Boolean) {
                boolean b = ((Boolean)value).booleanValue();
                if (b) return first; else return second;
            }
        } else if (bo.contains(node.operator)) {
            if (second.getType().equals("const")) {
                PCCommon.ConstNode cnode = new PCCommon.ConstNode(null);
                cnode.value = eval.evaluate(node);
                return cnode;
            }
        }
        return node;
    }

    private void processImpl(ParseNode node, ParseNode enode) throws ParserException {
        //find all dependencies of the expression
        LinkedList ids = findIDs(enode);
        //for every dependency: create rule
        for (int i=0;i<ids.size();i++) {
            String id = (String)ids.get(i);
            //try to reduce the expression
            ParseNode redenode = reduceExpression(enode, id);
            //if the reduces expression results in 'false' then don't add the rule
            if (redenode.toString().equals("false")) continue;
            Attribute attr = findAttribute(id);
            //if this attribute is not persistent and it is not system then don't add any rules
            if ( (!attr.isPersistent()) && (!attr.isSystem()) ) continue;
            Vector v = new Vector();
            LinkedList snodes = node.getChildList();
            //add the rule: "if redenode then node"
            for (int j=0;j<snodes.size();j++) {
                PCLAG.AssignNode anode = (PCLAG.AssignNode)snodes.get(j);
                v.add(new Assignment(anode.variable.toString(), anode.expr.toString()));
            }
            addRule(attr, redenode.toString(), v, new Vector());
        }
    }

    private Attribute findAttribute(String name) throws ParserException {
        DotString ds = new DotString(name);
        String aname = ds.get(ds.size()-1);
        ds.set(ds.size()-1, null);
        String cname = ds.toString();
        Concept concept = getConcept(cname);
        Attribute attr = null;
        try {
            attr = concept.getAttribute(aname);
        } catch (InvalidAttributeException e) {
            throw new ParserException("attribute not found: "+name);
        }
        return attr;
    }

    private class ConceptVariableLocator implements VariableLocator {
        public ConceptVariableLocator() {}

        /**
         * This method retrieves the value of the specified variable.
         * Must return float values when dealing with integers.
         * @param variable The name of the variable whose value is
         *        requested.
         * @return The actual value of the specified variable. This can
         *         be a boolean, a String or an integer (type long).
         */
        public Object getVariableValue(String variable) throws ParserException {
            Attribute attr = findAttribute(variable);
            if (attr.getType() == AttributeType.ATTRINT) return new Float(attr.getDefault()); else
            if (attr.getType() == AttributeType.ATTRBOOL) return new Boolean(attr.getDefault());
            else {
                String res = attr.getDefault();
                if (res.startsWith("\"")) {
                    return res.substring(1, res.length()-1);
                } else return res;
            }
        }
    }

    private Concept getConcept(String conceptname) throws ParserException {
        Concept result = (Concept)conceptbyname.get(conceptname);
        if (result == null) throw new ParserException("unknown concept: "+conceptname);
        return result;
    }

/*
 ***** writeWOW *****
 */

    private void writeWOW() throws IOException {
        if (!silent) writeWOWauthorfile();
        writeHTMLpages();
        writeLayout();
        copyDefaultConcept();
        collectMOTInfo();
        writeMOTInfo();
    }

    private void collectMOTInfo() {
        motinfo.put("MOT Lesson", rootLesson.concept.getDescription());
        motinfo.put("Author Name", author);
        motinfo.put("Applied Strategies", (programfiles.size()==0?"None":programfiles.toString()));
        motinfo.put("WOW! Course", course);
    }

    private void writeMOTInfo() throws IOException {
        XmlDocument infodoc = new XmlDocument();
        Element root = infodoc.createElement("motinfo");
        infodoc.appendChild(root);
        for (Enumeration keys=motinfo.keys();keys.hasMoreElements();) {
            String key = (String)keys.nextElement();
            String value = motinfo.get(key).toString();
            Element entry = infodoc.createElement("entry");
            root.appendChild(entry);
            Element ekey = infodoc.createElement("key");
            Element evalue = infodoc.createElement("value");
            entry.appendChild(ekey);
            entry.appendChild(evalue);
            ekey.appendChild(infodoc.createTextNode(key));
            evalue.appendChild(infodoc.createTextNode(value));
        }
        File dir = new File(WOWdir, course);
        File infofile = new File(dir, course+".mot");
        PrintWriter pw = new PrintWriter(new FileWriter(infofile));
        infodoc.write(pw);
        pw.close();
    }

    private void writeHTMLpages() throws IOException {
        DotString ds = new DotString(rootLesson.concept.getName());
        ds.set(0, null);
        String rootConcept = ds.toString();
        StringBuffer sb = new StringBuffer();
        String context = WOWStatic.config.Get("CONTEXTPATH");
        sb.append("<html>\n<head>\n<title>"+course+"</title>\n</head>\n");
        sb.append("<body>\n<hr />");
        sb.append("<form method=\"post\" action=\""+context+"/Register\">\n");
        sb.append("<table>\n");
        sb.append("<tr>\n");
        sb.append("<td>Name:</td>\n");
        sb.append("<td><input type=\"text\" name=\"name\" size=\"40\" value=\"\"></td>\n");
        sb.append("</tr>\n");
        sb.append("<tr>\n");
        sb.append("<td>E-mail address:</td>\n");
        sb.append("<td><input type=\"text\" name=\"email\" size=\"40\" value=\"\"></td>\n");
        sb.append("</tr>\n");
        sb.append("<tr>\n");
        sb.append("<td>User id:</td>\n");
        sb.append("<td><input type=\"text\" name=\"login\" size=\"10\" value=\"\"> Please invent a user id for yourself</td>\n");
        sb.append("</tr>\n");
        sb.append("<tr>\n");
        sb.append("<td>Password:</td>\n");
        sb.append("<td><input type=\"password\" name=\"password\" size=\"10\" value=\"\"></td>\n");
        sb.append("</tr>\n");
        sb.append("</table>\n");
        sb.append("<br />\n");
        sb.append("<input type=\"hidden\" name=\"start\" value=\""+rootConcept+"\" />\n");
        sb.append("<input type=\"hidden\" name=\"course\" value=\""+course+"\" />\n");
        sb.append("<input type=\"hidden\" name=\"directory\" value=\""+course+"\" />\n");
        sb.append("<input type=\"hidden\" name=\"title\" value=\""+course+"\" />\n");
//        sb.append("<input type=\"hidden\" name=\"background\" value=\"/wow/defaultcourse/icons/hcmunsbg.gif\" />\n");
        sb.append("Select <input type=\"submit\" name=\"enter_button\" value=\"Start course\" />\nto submit this form, or\n");
        sb.append("<input type=\"reset\" value=\"Reset\" /> to clear the form.\n");
        sb.append("</form>\n<hr />\n\n");
        sb.append("<p>If you do not wish to provide personal information you can also\nuse this tutorial anonymously. In order for this to work across\nmultiple sessions it requires you to use the same browser and computer\nfor all sessions and to preserve cookies.\n");
        sb.append("<form method=\"post\" action=\""+context+"/Anonymous\">\n");
        sb.append("<input type=\"hidden\" name=\"name\" value=\"Anonymous user\" />\n");
        sb.append("<input type=\"hidden\" name=\"start\" value=\""+rootConcept+"\" />\n");
        sb.append("<input type=\"hidden\" name=\"course\" value=\""+course+"\" />\n");
        sb.append("<input type=\"hidden\" name=\"directory\" value=\""+course+"\" />\n");
        sb.append("<input type=\"hidden\" name=\"title\" value=\""+course+"\" />\n");
//        sb.append("<input type=\"hidden\" name=\"background\" value=\"/wow/defaultcourse/icons/hcmunsbg.gif\" />\n");
        sb.append("Please select <input type=\"submit\" name=\"session_button\" value=\"New session\" />\n");
        sb.append("to start a new anonymous session.\n");
        sb.append("</form>\n</p>\n</body>\n</html>");
        PrintWriter pw = new PrintWriter(new FileWriter(new File(WOWdir, course+"/registration.html")));
        pw.write(sb.toString());
        pw.close();

        sb = new StringBuffer();
        sb.append("<html>\n<head>\n<title>"+course+"</title>\n</head>\n");
        sb.append("<body>\n<hr />");
        sb.append("<p>\nIf you are a new user please go to the\n<a href=\"registration.html\">registration page</a>.\n</p>\n");
        sb.append("<form method=\"post\" action=\""+context+"/Login\">\n");
        sb.append("<table>\n");
        sb.append("<tr>\n");
        sb.append("<td>User id:</td>\n");
        sb.append("<td><input type=\"text\" name=\"login\" size=\"10\" value=\"\"></td>\n");
        sb.append("</tr>\n");
        sb.append("<tr>\n");
        sb.append("<td>Password:</td>\n");
        sb.append("<td><input type=\"password\" name=\"password\" size=\"10\" value=\"\"></td>\n");
        sb.append("</tr>\n");
        sb.append("</table>\n");
        sb.append("<br />\n");
        sb.append("<input type=\"hidden\" name=\"start\" value=\""+rootConcept+"\" />\n");
        sb.append("<input type=\"hidden\" name=\"course\" value=\""+course+"\" />\n");
        sb.append("<input type=\"hidden\" name=\"directory\" value=\""+course+"\" />\n");
        sb.append("<input type=\"hidden\" name=\"title\" value=\""+course+"\" />\n");
//        sb.append("<input type=\"hidden\" name=\"background\" value=\"/wow/defaultcourse/icons/hcmunsbg.gif\" />\n");
        sb.append("Select <input type=\"submit\" name=\"enter_button\" value=\"Start course\" />\nto submit this form, or\n");
        sb.append("<input type=\"reset\" value=\"Reset\" /> to clear the form.\n");
        sb.append("</form>\n<hr />\n\n");
        sb.append("<form method=\"post\" action=\""+context+"/Anonymous\">\n");
        sb.append("<input type=\"hidden\" name=\"name\" value=\"Anonymous user\" />\n");
        sb.append("<input type=\"hidden\" name=\"start\" value=\""+rootConcept+"\" />\n");
        sb.append("<input type=\"hidden\" name=\"course\" value=\""+course+"\" />\n");
        sb.append("<input type=\"hidden\" name=\"directory\" value=\""+course+"\" />\n");
        sb.append("<input type=\"hidden\" name=\"title\" value=\""+course+"\" />\n");
//        sb.append("<input type=\"hidden\" name=\"background\" value=\"/wow/defaultcourse/icons/hcmunsbg.gif\" />\n");
        sb.append("You can also\n<input type=\"submit\" name=\"resume_button\" value=\"resume\" />\nan anonymous session, if you used the same browser on the same computer\nfor your previous visit(s).\n");
        sb.append("</form>\n</p>\n</body>\n</html>");
        pw = new PrintWriter(new FileWriter(new File(WOWdir, course+"/index.html")));
        pw.write(sb.toString());
        pw.close();
    }

    private void writeLayout() throws IOException {
        boolean todo = ((Boolean)properties.get("todo")).booleanValue();
        boolean next = ((Boolean)properties.get("next")).booleanValue();
        boolean menu = ((Boolean)properties.get("menu")).booleanValue();

        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE layoutconfig SYSTEM '../WOWstandard/LayoutConfig.dtd'>\n\n");
        sb.append("<layoutconfig>\n");
        sb.append("  <viewlist>\n");
        sb.append("    <view name=\"v1\" type=\"MainView\" title=\"Main View\" />\n");
        sb.append("    <view name=\"v2\" type=\"TreeView\" title=\"Tree View\" params=\"leftspace=10\" />\n");
        sb.append("    <view name=\"v3\" type=\"NextView\" title=\"Next View\" />\n");
        sb.append("    <view name=\"v4\" type=\"TodoView\" title=\"Todo View\" />\n");
        sb.append("    <view name=\"v5\" type=\"TOCView\" title=\"Table of Contents\" params=\"leftspace=70\" />\n");
        sb.append("    <view name=\"v6\" type=\"GlossaryView\" title=\"Glossary And Concepts\" params=\"cType=fragment\" />\n");
        sb.append("    <view name=\"v7\" type=\"ToolboxView\" title=\"Toolbox\">\n");
        sb.append("      <secwnds>\n");
        sb.append("        <secwnd link=\"TOC\" viewgroup=\"TOC\" img=\"icons/ContentBtn.bmp\"/>\n");
        sb.append("        <secwnd link=\"Glossary\" viewgroup=\"Glossary\" img=\"icons/GlossaryBtn.bmp\"/>\n");
        sb.append("      </secwnds>\n");
        sb.append("    </view>\n");
        sb.append("  </viewlist>\n");
        sb.append("  <layoutlist>\n");
        sb.append("    <layout name=\"page_c_layout\" trigger=\""+course+"\">\n");
        sb.append("      <viewgroup name=\""+course+"\" wndOpt=\"status=1,menubar=1,resizable=1,toolbar=1,width=800,height=600\">\n");
        sb.append("        <compound framestruct=\"cols=25%,*"+(todo?",20%":"")+"\" border=\"1\">\n");
        if (next || menu) {
            sb.append("          <compound framestruct=\"rows="+(menu?"*,":"")+"96"+(next?",84":"")+"\" border=\"1\">\n");
            if (menu) sb.append("            <viewref name=\"v2\" />\n");
            sb.append("            <viewref name=\"v7\" />\n");
            if (next) sb.append("            <viewref name=\"v3\" />\n");
            sb.append("          </compound>\n");
        } else {
            sb.append("          <viewref name=\"v7\" />\n");
        }
        sb.append("          <viewref name=\"v1\" />\n");
        if (todo) sb.append("          <viewref name=\"v4\" />\n");
        sb.append("        </compound>\n");
        sb.append("      </viewgroup>\n");
        sb.append("      <viewgroup name=\"TOC\" secondary=\"true\" wndOpt=\"status=1,menubar=1,resizable=1,toolbar=1,width=300,height=400\">\n");
        sb.append("        <viewref name=\"v5\"/>\n");
        sb.append("      </viewgroup>\n");
        sb.append("      <viewgroup name=\"Glossary\" secondary=\"true\" wndOpt=\"status=1,menubar=1,resizable=1,toolbar=1,width=600,height=500\">\n");
        sb.append("        <viewref name=\"v6\"/>\n");
        sb.append("      </viewgroup>\n");
        sb.append("    </layout>\n");
        sb.append("    <layout name=\"glossary_layout\" trigger=\"Glossary\" >\n");
        sb.append("      <viewgroup name=\"Glossary\" wndOpt=\"status=1,menubar=1,resizable=1,toolbar=1,width=600,height=500\">\n");
        sb.append("        <viewref name=\"v6\"/>\n");
        sb.append("      </viewgroup>\n");
        sb.append("    </layout>\n");
        sb.append("  </layoutlist>\n");
        sb.append("</layoutconfig>\n");

        FileWriter fw = new FileWriter(new File(WOWdir, course+"/LayoutConfig.xml"));
        fw.write(sb.toString());
        fw.close();
    }

    private void copyFile(File source, File dest) throws IOException {
        byte[] buf = new byte[8192];
        FileInputStream fi = new FileInputStream(source);
        FileOutputStream fo = new FileOutputStream(dest);
        int readbytes;
        while ((readbytes = fi.read(buf)) != -1) fo.write(buf, 0, readbytes);
        fi.close();
        fo.close();
    }

    private File getDestFile(File file, File sourceroot, File destroot) throws IOException {
        StringBuffer sb = new StringBuffer(file.toString());
        sb.delete(0, sourceroot.toString().length());
        sb.insert(0, destroot.toString());
        return new File(sb.toString());
    }

    private void addFiles(File root, File absroot, File destroot) throws IOException {
        File[] files = root.listFiles();
        for (int i=0;i<files.length;i++) {
            if (files[i].isFile()) copyFile(files[i], getDestFile(files[i], absroot, destroot));
            if (files[i].isDirectory()) {
                File destfile = getDestFile(files[i], absroot, destroot);
                if (!destfile.exists()) destfile.mkdir();
                addFiles(files[i], absroot, destroot);
            }
        }
    }

    private void copyDefaultConcept() throws IOException {
        File destroot = new File(WOWdir, course);
        File sourceroot = new File(WOWdir, "defaultcourse");
        addFiles(sourceroot, sourceroot, destroot);
    }

    private void writeWOWauthorfile() throws IOException {
        XmlDocument doc = new XmlDocument();

        Element econceptlist = doc.createElement("conceptList");
        doc.appendChild(econceptlist);
        Element ename = doc.createElement("name");
        econceptlist.appendChild(ename);
        ename.appendChild(doc.createTextNode("WOW! conceptlist: " + course));

        for (int i=0;i<concepts.size();i++) {
            Concept c = (Concept)concepts.get(i);
            Node node = vn.spring.WOW.util.KoenFormat.getKoenXML(c, doc);
            doc.getDocumentElement().appendChild(node);
        }

        PrintWriter pw = new PrintWriter(new FileWriter(new File(WOWdir, "author/authorfiles/"+author+"/"+course+".wow")));
        doc.setSystemId("conceptList");
        doc.setDoctype(null, "../generatelist4.dtd", null);
        doc.write(pw);
        pw.close();
    }

/*
 ***** other *****
 */

    private String findLessonDescription(CAFLesson lesson) {
        String description = "";
        if (lesson.links.size() > 0) {
            CAFLink link = (CAFLink)lesson.links.get(0);
            if (CAFlinks.containsKey(link.link)) {
                CAFAttribute attr = (CAFAttribute)CAFlinks.get(link.link);
                description = attr.parent.name;
            }
        }
        return description;
    }

    public static void main(String[] args) throws Exception {
        try {
            new CAFtoWOW(args);
        } catch (InvalidParametersException ipe) {
            System.out.println(ipe.getMessage()+"\n");
            System.out.println("Usage: CAFtoWOW <CAF filename> <WOW! coursename> <WOW! author>\n         [<MOT programname>] [-r <rootWOWdir>] [-d]");
            System.out.println("  -r specify the root directory of your WOW installation");
            System.out.println("  -d debug information");
            return;
        } catch (ParserException pe) {
            if (debug) throw pe;
            System.out.println("An error has occured while parsing code: "+pe.getMessage());
            System.out.println("  use the '-d' option to see more information");
        } catch (Exception e) {
            if (debug) throw e;
            System.out.println("An error has occured: "+e.getMessage());
            System.out.println("  use the '-d' option to see more information");
        }
    }

    public class InvalidParametersException extends Exception {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public InvalidParametersException() {super();}
        public InvalidParametersException(String s) {super(s);}
    }

/*
 ***** Definitions of CAF data *****
 */

    public class CAFLesson {
        public CAFLesson parent = null;
        public LinkedList links = new LinkedList();
        public LinkedList lessons = new LinkedList();
        public Hashtable attrs = new Hashtable();
        public CAFLesson() {}
        public CAFLesson(CAFLesson parent) {this.parent = parent;}

        public Concept concept = null;
    }

    public class CAFRelation {
        public CAFConcept related = null;
        public Hashtable attrs = new Hashtable();
        public CAFRelation() {}
    }

    public class CAFLink {
        public CAFLesson parent = null;
        public String link = null;
        public Hashtable attrs = new Hashtable();
        public CAFLink() {}
        public CAFLink(CAFLesson parent) {this.parent = parent;}
        public CAFLink(String link) {this.link = link;}
        public CAFLink(String link, CAFLesson parent) {this.link = link; this.parent = parent;}

        public Concept concept = null;
    }

    public class CAFConcept {
        public String name = null;
        public CAFConcept parent = null;
        public LinkedList attributes = new LinkedList();
        public LinkedList concepts = new LinkedList();
        public Hashtable relations = new Hashtable();
        public CAFConcept() {}
        public CAFConcept(String name) {this.name = name;}
        public CAFConcept(CAFConcept parent) {this.parent = parent;}
        public CAFConcept(String name, CAFConcept parent) {this.parent = parent;}
    }

    public class CAFAttribute {
        public String name = null;
        public String contents = null;
        public CAFConcept parent = null;
        public CAFAttribute() {}
        public CAFAttribute(String name) {this.name = name;}
        public CAFAttribute(String name, String contents) {this.name = name; this.contents = contents;}
        public CAFAttribute(CAFConcept parent) {this.parent = parent;}
        public CAFAttribute(String name, CAFConcept parent) {this.name = name; this.parent = parent;}
        public CAFAttribute(String name, String contents, CAFConcept parent) {this.name = name; this.contents = contents; this.parent = parent;}
    }
}