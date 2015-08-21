/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * LinkAdaptation.java 1.0, April 23, 2008 modified by Loc Nguyen 2009
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.engine;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.Layout.*;

import java.util.*;

import org.w3c.dom.*;

public class LinkAdaptation {
    public static Element createLink(Element a, Profile profile, String view, Document doc) {
        Element result = doc.createElement("span");
        a = (Element)a.cloneNode(true);
        result.appendChild(a);

        String course = getCourse(profile);
        String conceptname = a.getAttribute("href");
        ConceptInfo conceptinfo = WOWStatic.CourseInfoTbl.getCourseInfo(course).conceptsInfoTbl.getConceptInfo(conceptname);
        if (conceptinfo == null) {
            a.removeAttribute("class");
            result.appendChild(createErrorElement("[No conceptinfo for concept '"+conceptname+"']", doc));
            return result;
        }
        String type = conceptinfo.getTypeName();
        CourseInfo courseinfo = WOWStatic.CourseInfoTbl.getCourseInfo(course);

        ConceptType concepttype = courseinfo.CT.getConceptType(type);
        if (concepttype == null) {
            a.removeAttribute("class");
            result.appendChild(createErrorElement("[No concepttype exists with concept type name '"+type+"' in concept '"+conceptname+"']", doc));
            return result;
        }
        Window triggerwnd = concepttype.getRepresentation().getTriggerWnd();
        a.setAttribute("target", triggerwnd.getName());

        //Link annotation
        Annotation anno = WOWStatic.CourseInfoTbl.getCourseInfo(course).CT.getConceptType(type).getAnnotation();
        LinkAnnotation linkanno = anno.getLinkAnno();
        a.setAttribute("class", linkanno.getLinkAnnotation(conceptname, profile, view));
        a.setAttribute("href", WOWStatic.config.Get("CONTEXTPATH")+"/Get/"+getDirectory(profile)+"/?concept="+conceptname);

        //Icon annotation
        IconAnnotation iconanno = anno.getIconAnno();
        LinkedList icons = iconanno.getIconAnnotation(conceptname, profile, view);
        boolean useicon = false;
        for (int i=0;i<icons.size();i++) {
            useicon = true;
            String icon = (String)icons.get(i);
            boolean front = false;
            if (icon.startsWith("<front>")) {icon = icon.substring(7);front = true;}
            Element img = doc.createElement("img");
            img.setAttribute("src", WOWStatic.config.Get("CONTEXTPATH")+"/"+getDirectory(profile)+"/"+icon);
            img.setAttribute("align", "bottom");
            img.setAttribute("alt", "");
            if (front) result.insertBefore(img, a); else result.appendChild(img);
        }

        if (useicon) return result; else return a;
    }

    public static Element createLink(String conceptname, Profile profile, String view, Document doc) {
        String course = getCourse(profile);
        ConceptInfo conceptinfo = WOWStatic.CourseInfoTbl.getCourseInfo(course).conceptsInfoTbl.getConceptInfo(conceptname);
        String title = conceptinfo.getTitle();
        Element a = doc.createElement("a");
        a.setAttribute("href", conceptname);
        Text text = doc.createTextNode(title);
        a.appendChild(text);
        return createLink(a, profile, view, doc);
    }

    protected static String getCourse(Profile profile) {return getProfileValue("personal", "course", profile);}
    protected static String getDirectory(Profile profile) {return getProfileValue("personal", "directory", profile);}

    protected static String getProfileValue(String concept, String attribute, Profile profile) {
        String value = null;
        try {value = profile.getAttributeValue(concept, attribute);} catch (InvalidAttributeException e) {e.printStackTrace();}
        return value;
    }

    protected static Element createErrorElement(String text, Document doc) {
        Element s = doc.createElement("span");
        s.setAttribute("class", "error");
        s.appendChild(doc.createTextNode(text));
        return s;
    }
}