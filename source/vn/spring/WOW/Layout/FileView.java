/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ChildrenView.java 1.0, April 29, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.Layout;

import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.exceptions.*;

import java.io.*;
import java.util.*;

public class FileView extends AbstractView {
    public FileView() {}

    public String getViewType() {return "FILEVIEW";}

    public InputStream genBrsCode(String conceptname, Profile profile, Map params) {
        try {
            String filename = getParam("file");
            if (filename == null) throw new WOWException("No filename specified for FileView");
            String directory = profile.getAttributeValue("personal", "directory");
            Resource resource = WOWStatic.HM().locateResource("file:/"+directory+"/"+filename);
            resource = WOWStatic.HM().processComplete(resource, profile);
            return resource.getInputStream();
/*
            Document doc = newDocument(profile, title);
            Element body = (Element)doc.getDocumentElement().getElementsByTagName("body").item(0);
            Element table = doc.createElement("table"); body.appendChild(table);

            String course = profile.getAttributeValue("personal", "course");
            PNode rootnode = new PNode(WOWStatic.CourseInfoTbl.getCourseInfo(course).hierarchy.getTNode(conceptname), profile);
            PNode curnode = rootnode.getFirstChild();
            while (curnode != null) {
                Element tr = doc.createElement("tr"); table.appendChild(tr);
                Element td = doc.createElement("td"); tr.appendChild(td);
                td.appendChild(LinkAdaptation.createLink(curnode.getName(), profile, getViewType(), doc));
                curnode = curnode.getNextSib();
            }
            String result = serializeXML(doc.getDocumentElement());
            return new ByteArrayInputStream(result.getBytes());
*/
        } catch (Exception e) {
            e.printStackTrace();
            return errorStream("[Error generating FileView]", e.getMessage());
        }
    }
}