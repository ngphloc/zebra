/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * TOCView.java 1.0, June 20, 2008
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

public class TOCView extends AbstractView {
    public TOCView() {}

    public String getViewType() {return "TOCVIEW";}

    public InputStream genBrsCode(String conceptname, Profile profile, Map params) {
        try {
            Document doc = newDocument(profile, title);
            Element body = (Element)doc.getDocumentElement().getElementsByTagName("body").item(0);
            Element table = doc.createElement("table"); body.appendChild(table);

            String course = profile.getAttributeValue("personal", "course");
            PNode rootnode = new PNode(WOWStatic.CourseInfoTbl.getCourseInfo(course).hierarchy.getTNode(conceptname), profile);

            PNode curnode = rootnode;
            LinkedList path = new LinkedList();
            while (curnode != null) {path.addFirst(curnode);curnode = curnode.getParent();}
            curnode = (PNode)path.get(0);
            table.appendChild(createRowElement(curnode.getName(), 0, doc, profile, curnode.getName().equals(rootnode)));
            menuList(curnode, 1, table, rootnode, doc, profile);
            String result = serializeXML(doc.getDocumentElement());
            return new ByteArrayInputStream(result.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return errorStream("[Error generating TOCView]", e.getMessage());
        }
    }

    private void menuList(PNode parent, int listindex, Element table, PNode rootnode, Document doc, Profile profile) {
        PNode node = parent.getFirstChild();
        while (node != null) {
            table.appendChild(createRowElement(node.getName(), listindex*25, doc, profile, node.getName().equals(rootnode.getName())));
            menuList(node, listindex+1, table, rootnode, doc, profile);
            node = node.getNextSib();
        }
    }

    private Element createRowElement(String conceptname, int width, Document doc, Profile profile, boolean bold) {
        Element tr = doc.createElement("tr");
        Element td = doc.createElement("td"); tr.appendChild(td); td.setAttribute("style", "padding-left: "+width+"px;");
        if (bold) {Element b = doc.createElement("b"); td.appendChild(b); td = b;}
        try {
            td.appendChild(LinkAdaptation.createLink(conceptname, profile, getViewType(), doc));
        } catch (Exception e) {
            e.printStackTrace();
            td.appendChild(errorElement("["+e.getMessage()+", "+conceptname+"]", doc));
        }
        return tr;
    }

    private Element errorElement(String message, Document doc) {
        Element result = doc.createElement("span");
        result.setAttribute("class", "error");
        result.appendChild(doc.createTextNode(message));
        return result;
    }
}