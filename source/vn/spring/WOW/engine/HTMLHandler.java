/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * HTMLHandler.java 1.0, March 28, 2008
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

import java.util.*;
import java.io.*;

import org.w3c.dom.*;
import org.w3c.tidy.*;

public class HTMLHandler implements ResourceHandler {
    public static final boolean debug = true;

    public HTMLHandler() {}

    /**
     * Returns if this handler can process the given resource.
     * @param type The resource type that this handler is supposed to
     *        handle.
     * @return If this handler handles the specified resource.
     */
    public boolean handlesResource(ResourceType type) {
        String s = type.getMime();
        return ("text/html".equals(s));
    }

    private String process(Document dom, Resource res, WOWDB db) {
        ConceptDB cdb = db.getConceptDB();
        Concept c;
        try {
            c = (res.getConceptName()!=null?cdb.getConcept(cdb.findConcept(res.getConceptName())):null);
            if (c == null) return null;
            if ("fragment".equals(c.getType())) {
                Element el = (Element)dom.getDocumentElement().getElementsByTagName("body").item(0);
                if (WOWStatic.getChildElementsByTagName(el,"span").getLength()==0) {
                    NodeList n = el.getChildNodes();
                    el = dom.createElement("span");
                    for (int i=0;i<n.getLength();i++) el.appendChild(n.item(i));
                } else {el = (Element)WOWStatic.getChildElementsByTagName(el,"span").item(0);}
                return XHTMLHandler.serializeElement(el);
            }
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }

    /**
     * Processes the resource using the user model and database and
     * returns the new resource.
     * @param resource The resource to be processed.
     * @param profile The user profile that is to be used.
     * @param db A reference to the database that is to be used.
     * @return The processed resource.
     */
    public Resource processResource(Resource resource, Profile profile, WOWDB db) throws
                    ProcessorException {
        Resource result = (Resource)resource.clone();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Tidy tidy = new Tidy();
        tidy.setTidyMark(false);
        tidy.setDocType("omit");
        tidy.setQuiet(true);
        tidy.setXmlOut(true);
        tidy.setXHTML(true);
        StringWriter errors = new StringWriter();
        tidy.setErrout(new PrintWriter(errors));
        Properties props = new Properties();
        props.setProperty("new-inline-tags", "if,variable,linkdescription,name");
        props.setProperty("new-blocklevel-tags", "block,handler,servlet");
        tidy.getConfiguration().addProps(props);
        Document dom = tidy.parseDOM(resource.getInputStream(), os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
//System.out.println("/nHTML/n/n"+new String(os.toByteArray())+"/n");
        String s = process(dom, resource, db);
        if (s != null) is = new ByteArrayInputStream(s.getBytes());
        if (tidy.getParseErrors() > 0) {
            String errorstring = WOWStatic.normalize(errors.getBuffer().toString());
            String[] es = errorstring.split("\n");
            StringBuffer esbuf = new StringBuffer();
            for (int i=0;i<es.length;i++) if (es[i].indexOf("Error:") != -1) esbuf.append("<p>"+es[i]+"</p>");
            String errorreport = "<html><head><title>Error loading document: "+resource.getFakeURL()+"</title></head>"+
                                 "<body><span class=\"error\"><p><h1>[Error loading document: "+resource.getFakeURL()+"]</h1></p></span>"+
                                 esbuf.toString()+"</body></html>";
            is = new ByteArrayInputStream(errorreport.getBytes());
        }

        result.setType(new ResourceType("text/xhtml"));
        result.setInputStream(is);
        result.setReady(false);
        return result;
    }
}