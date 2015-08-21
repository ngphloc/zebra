/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * CSSHandler.java 1.0, June 7, 2008
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
import java.awt.Color;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.*;

import com.steadystate.css.parser.CSSOMParser;

public class CSSHandler implements ResourceHandler {
    private static final boolean debug = true;

    public CSSHandler() {}

    /**
     * Returns if this handler can process the given resource.
     * @param type The resource type that this handler is supposed to
     *        handle.
     * @return If this handler handles the specified resource.
     */
    public boolean handlesResource(ResourceType type) {
        String s = type.getMime();
        return "text/css".equals(s);
    }

    private void readcolors(CSSStyleSheet stylesheet, Profile profile) {
        try {
            Hashtable activecolors = new Hashtable();
            Hashtable visitedcolors = new Hashtable();
            Vector linkclasses = WOWStatic.CourseInfoTbl.getCourseInfo(profile.getAttributeValue("personal", "course")).linkclasses;
            ConceptDB cdb = WOWStatic.DB().getConceptDB();
            for (int i=0;i<stylesheet.getCssRules().getLength();i++) {
                CSSStyleRule rule = (stylesheet.getCssRules().item(i) instanceof CSSStyleRule?(CSSStyleRule)stylesheet.getCssRules().item(i):null);
                if (rule != null) {
                    if (rule.getSelectorText().startsWith("A.") || rule.getSelectorText().startsWith("a.")) {
                        String linkclass = rule.getSelectorText().substring(2);
                        if (linkclass.indexOf(":") >= 0) linkclass = linkclass.substring(0, linkclass.indexOf(":"));
                        if (linkclasses.contains(linkclass)) {
                            String scolor = rule.getStyle().getPropertyValue("color");
                            Color color = makeColor(scolor);
                            if (color != null) {
                                if (rule.getSelectorText().endsWith(":visited"))
                                    visitedcolors.put(linkclass, color); else
                                    activecolors.put(linkclass, color);
                            }
                        }
                    }
                }
            }
            for (Enumeration en=activecolors.keys();en.hasMoreElements();) {
                String linkclass = (String)en.nextElement();
                Color activecolor = (Color)activecolors.get(linkclass);
                Color visitedcolor = (Color)visitedcolors.get(linkclass);
                profile.setAttributeValue(cdb, "personal", "class_"+linkclass, ColorConfig.printColor(activecolor));
                if ((visitedcolor != null) && (!activecolor.equals(visitedcolor))) {
                    profile.setAttributeValue(cdb, "personal", "class_"+linkclass+"_visited", ColorConfig.printColor(visitedcolor));
                }
            }
            profile.setAttributeValue(cdb, "personal", "wow_readcolors", "true");
        } catch (Exception e) {if (debug) e.printStackTrace();}
    }

    private Color makeColor(String s) {
        StringTokenizer st = new StringTokenizer(s, " \t\n\r\f{}(),");
        Vector v = new Vector();
        while (st.hasMoreElements()) v.add(st.nextToken());
        if (v.size()==4) {
            int r = Integer.parseInt((String)v.get(1));
            int g = Integer.parseInt((String)v.get(2));
            int b = Integer.parseInt((String)v.get(3));
            return new Color(r,g,b);
        }
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
    public synchronized Resource processResource(Resource resource, Profile profile, WOWDB db) throws
                    ProcessorException {
        Resource result = (Resource)resource.clone();
        result.setReady(true);
        try {
            Reader r = new InputStreamReader(resource.getInputStream());
            CSSOMParser parser = new CSSOMParser();
            InputSource is = new InputSource(r);
            CSSStyleSheet stylesheet = parser.parseStyleSheet(is);
            //add extra rules
            try {
                stylesheet.insertRule(".error {color: #CC0000;}",stylesheet.getCssRules().getLength());
                Vector linkclasses = WOWStatic.CourseInfoTbl.getCourseInfo(profile.getAttributeValue("personal", "course")).linkclasses;
                Hashtable values = profile.getValues();
                if (values.get("personal.wow_readcolors") == null) readcolors(stylesheet, profile);
                for (int i=0;i<linkclasses.size();i++) {
                    String linkclass = (String)linkclasses.get(i);
                    AttributeValue value = ((AttributeValue)values.get("personal.class_"+linkclass));
                    AttributeValue visitedvalue = ((AttributeValue)values.get("personal.class_"+linkclass+"_visited"));
                    if (value != null) {
                        stylesheet.insertRule("a."+linkclass+":link {text-decoration: none; color: #"+value.getValue()+"}", stylesheet.getCssRules().getLength());
                        stylesheet.insertRule("a."+linkclass+":visited {text-decoration: none; color: #"+(visitedvalue == null?value.getValue():visitedvalue.getValue())+"}", stylesheet.getCssRules().getLength());
                        stylesheet.insertRule("a."+linkclass+":hover {text-decoration: none; color: #"+value.getValue()+"}", stylesheet.getCssRules().getLength());
                        stylesheet.insertRule("a."+linkclass+":active {text-decoration: none; color: #"+value.getValue()+"}", stylesheet.getCssRules().getLength());
                    }
                }
                try {
                    String bg = profile.getAttributeValue("personal", "background");
                    stylesheet.insertRule("body {background: url("+bg+")}",stylesheet.getCssRules().getLength());
                } catch (Exception e) {}
            } catch (Exception e) {if (debug) e.printStackTrace();}
            result.setInputStream(new ByteArrayInputStream(stylesheet.toString().getBytes()));
        } catch (Exception e) {
            if (debug) e.printStackTrace();
        }
        return result;
    }
}