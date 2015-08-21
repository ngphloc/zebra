/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * PathView.java 1.0, April 29, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.Layout;

import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.engine.*;
import vn.spring.WOW.WOWStatic;

import java.io.*;
import java.util.*;

import org.w3c.dom.*;

public class PathView extends AbstractView {
    public PathView() {}

    public String getViewType() {return "PATHVIEW";}

    public InputStream genBrsCode(String conceptname, Profile profile, Map params) {
        try {
            Document doc = newDocument(profile, title);
            Element body = (Element)doc.getDocumentElement().getElementsByTagName("body").item(0);
            Element table = doc.createElement("table"); body.appendChild(table);

            String course = profile.getAttributeValue("personal", "course");
            PNode rootnode = new PNode(WOWStatic.CourseInfoTbl.getCourseInfo(course).hierarchy.getTNode(conceptname), profile);

            PNode curnode = rootnode.getParent();
            LinkedList path = new LinkedList();
            while (curnode != null) {path.addFirst(curnode.getName());curnode = curnode.getParent();}
            for (int i=0;i<path.size();i++) table.appendChild(createRowElement((String)path.get(i), i*15, doc, profile, false));
            if (rootnode.getParent() != null) curnode = rootnode.getParent().getFirstChild(); else curnode = rootnode;
            while (curnode != null) {
                table.appendChild(createRowElement(curnode.getName(), path.size()*15, doc, profile, curnode.getName().equals(rootnode.getName())));
                curnode = curnode.getNextSib();
            }
            String result = serializeXML(doc.getDocumentElement());
            return new ByteArrayInputStream(result.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return errorStream("[Error generating PathView]", e.getMessage());
        }
    }

    private Element createRowElement(String conceptname, int width, Document doc, Profile profile, boolean bold) {
        Element tr = doc.createElement("tr");
        Element td = doc.createElement("td"); tr.appendChild(td); td.setAttribute("style", "padding-left: "+width+"px;");
        if (bold) {Element b = doc.createElement("b"); td.appendChild(b); td = b;}
        td.appendChild(LinkAdaptation.createLink(conceptname, profile, getViewType(), doc));
        return tr;
    }
}