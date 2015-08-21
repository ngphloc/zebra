/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * TodoView.java 1.0, October 19, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.Layout;

import vn.spring.WOW.datacomponents.Profile;
import java.io.*;
import vn.spring.WOW.engine.*;
import vn.spring.WOW.parser.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.exceptions.*;
import java.util.*;

/**
 * This is a view representing the next suitable concept.
 */
public class TodoView implements View {

    private String name;
    public  String back;
    public  String title;
    public  String params;
    private SecWndLinks swl;

    /**
     * generates the the Browers code(HTML/XHTML) for the view
     */
    public InputStream genBrsCode(String conceptname, Profile profile, Map params) {
        HTMLAnchor link = new HTMLAnchor();
        String course = ""; try {course = profile.getAttributeValue("personal", "course");} catch (Exception e) {e.printStackTrace();}
        Hierarchy hierarchy = WOWStatic.CourseInfoTbl.getCourseInfo(course).hierarchy;
        StringBuffer outstr = new StringBuffer();
        outstr.append("<html>\n<body>\n");
        outstr.append("<table>");
        PNode rnode=new PNode(hierarchy.getRootNode(), profile);
        while (rnode!=null) {
            if (suitableConcept(rnode.getName(), profile)) {
                //add this concept to the list
                genHTML(outstr, link.genLinkTo(rnode.getName(), profile));
            }
            rnode = nextNode(rnode);
        }

        InputStream insb = new ByteArrayInputStream(outstr.toString().getBytes());
        return insb;
    }

    private void genHTML(StringBuffer sb, String link) {
        sb.append("<tr><td>");
        sb.append(link);
        sb.append("</td></tr>\n");
    }

    private boolean suitableConcept(String cname, Profile profile) {
        UMVariableLocator umvl = new UMVariableLocator(profile, WOWStatic.DB().getConceptDB());
        boolean result = true;
        Object r = null;
        try {
            r = umvl.getVariableValue(cname+".suitability");
        } catch (ParserException e) {
            return false;
        }
        result = result && suitableValue(r);
        try {
            r = umvl.getVariableValue(cname+".visited");
        } catch (ParserException e) {
            return false;
        }
        result = result && !suitableValue(r);
        return result;
    }

    private boolean suitableValue(Object o) {
        if (o instanceof String) return false;
        else if (o instanceof Float) return ((Float)o).intValue()>0;
        else if (o instanceof Boolean) return ((Boolean)o).booleanValue();
        else return false;
    }

    private PNode nextNode(PNode node) {
        PNode nnode = node.getFirstChild();
        if (nnode != null) return nnode;
        nnode = node.getNextSib();
        if (nnode != null) return nnode;
        nnode = node;
        while (nnode.getParent() != null) {
            nnode = nnode.getParent();
            if (nnode.getNextSib() != null) return nnode.getNextSib();
        }
        return null;
    }

    /**
     * returns the view with name viewName
     */
    public String getViewName() {return name;}

    /**
     * returns the view with name viewName
     */
    public String getViewType() {return "NEXTVIEW";}

    /**
     * returns the Mime type of the response generated by the view
     */
    public String getMime() {return "text/html";}

    public boolean isStatic() {return false;}

    /**
     * returns the name of the Secondary window that is being opened by
     * secondary link 'linkname'
     */
    public SecWndLinks getSecWndLinks() {return swl;}

    public void setSecWndLinks(SecWndLinks swl) {this.swl = swl;}
    public void setViewName(String name) {this.name = name;}
    public void setBackgound(String back) {this.back = back;}
    public void setParams(String params) {this.params = params;}
    public void setTitle(String title) {this.title = title;}
}