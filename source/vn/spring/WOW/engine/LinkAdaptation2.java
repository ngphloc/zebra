/**
 * 
 */
package vn.spring.WOW.engine;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.Layout.Window;
import vn.spring.WOW.datacomponents.Profile;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class LinkAdaptation2 extends LinkAdaptation {
    public static Element createLink(Element a, Profile profile, String view, Document doc, 
    		boolean hasIcon, boolean gray) {
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
        if(gray) a.setAttribute("style", "color: #808080");
        
        //Icon annotation
        if(hasIcon) {
	        IconAnnotation iconanno = anno.getIconAnno();
	        LinkedList icons = iconanno.getIconAnnotation(conceptname, profile, view);
	        for (int i=0;i<icons.size();i++) {
	            String icon = (String)icons.get(i);
	            boolean front = false;
	            if (icon.startsWith("<front>")) {icon = icon.substring(7);front = true;}
	            Element img = doc.createElement("img");
	            img.setAttribute("src", WOWStatic.config.Get("CONTEXTPATH")+"/"+getDirectory(profile)+"/"+icon);
	            img.setAttribute("align", "bottom");
	            img.setAttribute("alt", "");
	            if (front) result.insertBefore(img, a); else result.appendChild(img);
	        }
        }
        return result;
    }

    public static Element createLink(String conceptname, Profile profile, String view, Document doc, 
    		boolean hasIcon, boolean gray) {
        String course = getCourse(profile);
        ConceptInfo conceptinfo = WOWStatic.CourseInfoTbl.getCourseInfo(course).conceptsInfoTbl.getConceptInfo(conceptname);
        String title = conceptinfo.getTitle();
        Element a = doc.createElement("a");
        a.setAttribute("href", conceptname);
        Text text = doc.createTextNode(title);
        a.appendChild(text);
        return createLink(a, profile, view, doc, hasIcon, gray);
    }

}
