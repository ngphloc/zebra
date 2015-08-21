/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ToolboxView.java 1.0, July 4, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.Layout;

import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;

import java.io.*;
import java.util.*;

import org.w3c.dom.*;

public class ToolboxView extends AbstractView {
    public ToolboxView() {}

    public String getViewType() {return "TOOLBOXVIEW";}

    public InputStream genBrsCode(String conceptname, Profile profile, Map params) {
        try {
            Document doc = newDocument(profile, title);
            Element body = (Element)doc.getDocumentElement().getElementsByTagName("body").item(0);
            Element center = doc.createElement("center"); body.appendChild(center);
            Element table = doc.createElement("table"); center.appendChild(table);
            
            String directory = profile.getAttributeValue("personal", "directory");
            for (Enumeration e=secwndlinks.getLinksNames();e.hasMoreElements();) {
                Element tr = doc.createElement("tr");
                String slink = (String)e.nextElement();
                SecWndLink swl = (SecWndLink)secwndlinks.getSwl(slink);
                Element a = doc.createElement("a"); tr.appendChild(a);
                a.setAttribute("href", WOWStatic.config.Get("CONTEXTPATH")+"/Get/?wndName=" + swl.getWndName());
                a.setAttribute("target", swl.getWndName());
                Element img = doc.createElement("img"); a.appendChild(img);
                img.setAttribute("src", WOWStatic.config.Get("CONTEXTPATH")+"/" + directory + "/" + swl.getImg());
                img.setAttribute("align", "bottom");
                img.setAttribute("alt", swl.getLinkName());
                img.setAttribute("border", "0");
                table.appendChild(tr);
            }
            String result = serializeXML(doc.getDocumentElement());
            return new ByteArrayInputStream(result.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return errorStream("[Error generating ToolboxView]", e.getMessage());
        }
    }
}