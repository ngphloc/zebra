/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AbstractView.java 1.0, April 29, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.Layout;

import vn.spring.WOW.engine.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.WOWStatic;

import java.util.*;
import java.io.*;

import org.w3c.dom.*;
import org.apache.xml.serialize.*;
import org.apache.xerces.dom.*;

public abstract class AbstractView implements View {
    protected String viewname = "";
    protected String mime = "text/html";
    protected boolean staticview = false;
    protected SecWndLinks secwndlinks = null;
    protected String background = "";
    protected String params = "";
    protected String title = "";

    public String getViewName() {return viewname;}
    public String getMime() {return mime;}
    public boolean isStatic() {return staticview;}
    public SecWndLinks getSecWndLinks() {return secwndlinks;}
    public void setSecWndLinks(SecWndLinks swl) {secwndlinks = swl;}
    public void setViewName(String name) {viewname = name;}
    public void setBackgound(String back) {background = back;}
    public void setParams(String params) {this.params = params;}
    public void setTitle(String title) {this.title = title;}
    protected String getParam(String name) {
        if (name == null) return null;
        StringTokenizer paramtokens = new StringTokenizer(params, "&");
        while (paramtokens.hasMoreTokens()) {
            String token = paramtokens.nextToken();
            if (token.startsWith(name+"=")) return token.substring(name.length()+1);
        }
        return null;
    }

    protected String serializeXML(Element element) throws IOException {
        StringWriter writer = new StringWriter();
        OutputFormat of = new OutputFormat("html", "UTF-8", true);
        of.setOmitDocumentType(true);
        of.setLineWidth(100);
        of.setNonEscapingElements(new String[] {"style"});
        BaseMarkupSerializer ser = SerializeHandler.getSerializer(writer, of);
        ser.serialize(element);
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"+writer.toString();
    }

    protected Document newDocument(Profile profile, String title) {
        String course = "";
        try {course = profile.getAttributeValue("personal", "course");} catch (InvalidAttributeException e) {e.printStackTrace();}
        Document doc = new DocumentImpl();
        Element html = doc.createElement("html"); doc.appendChild(html);
        Element head = doc.createElement("head"); html.appendChild(head);
        Element etitle = doc.createElement("title"); head.appendChild(etitle);
        etitle.appendChild(doc.createTextNode(title));
        Element link = doc.createElement("link"); head.appendChild(link);
        link.setAttribute("rel", "stylesheet");
        link.setAttribute("type", "text/css");
        link.setAttribute("href", WOWStatic.CourseInfoTbl.getCourseInfo(course).stylesheet);
        Element body = doc.createElement("body"); html.appendChild(body);
        return doc;
    }
    //added by Loc Nguyen October 22 2009
    protected String createCSSlink(Profile profile) {
        String course = "";
        try {course = profile.getAttributeValue("personal", "course");} catch (InvalidAttributeException e) {e.printStackTrace();}
    	String link = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + 
    		WOWStatic.CourseInfoTbl.getCourseInfo(course).stylesheet + "\"/>";
    	return link;
    }
    
    protected InputStream errorStream(String error, String detail) {
        String errorreport = "<html><head><title>"+WOWStatic.normalize(error)+"</title></head>"+
                             "<body><span style=\"color: #CC0000;\"><p><h1>"+WOWStatic.normalize(error)+"</h1></p></span>"+
                             WOWStatic.normalize(detail)+"</body></html>";
        return new ByteArrayInputStream(errorreport.getBytes());
    }
}