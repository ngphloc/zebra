/**
 * 
 */
package vn.spring.WOW.Layout;

import java.util.Iterator;
import java.util.LinkedList;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.Annotation;
import vn.spring.WOW.engine.ConceptInfo;
import vn.spring.WOW.engine.ConceptType;
import vn.spring.WOW.engine.CourseInfo;
import vn.spring.WOW.engine.IconAnnotation;
import vn.spring.WOW.engine.LinkAnnotation;
import vn.spring.WOW.exceptions.InvalidAttributeException;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class HTMLAnchor2 extends HTMLAnchor {

	public String genLinkTo(String conceptName, Profile profile, boolean hasIcon, boolean gray) {
		if(hasIcon && !gray) return super.genLinkTo(conceptName, profile, true);
	    try {
	        this.directory = profile.getAttributeValue("personal","directory");
	    }
	    catch (InvalidAttributeException iae) {System.out.println(iae);}
	    
	    StringBuffer rtn= new StringBuffer();
	    String linkClass=null;
	    LinkAnnotation linkAnno=null;
	    String coursename=null;
	    try{
	    	coursename=profile.getAttributeValue("personal","course");
	    }
	    catch(Exception e){System.out.println("genLinkTo Error:Course attribute not found in the profile");}
	    ConceptInfo conceptinfo=WOWStatic.CourseInfoTbl.getCourseInfo(coursename).conceptsInfoTbl.getConceptInfo(conceptName);
	    String type=conceptinfo.getTypeName();
	    String title=WOWStatic.normalize(conceptinfo.getTitle());

	    //    Window triggerWnd=WOWStatic.CourseInfoTbl.getCourseInfo(coursename).CT.getConceptType(type).getRepresentation().getTriggerWnd();
	    CourseInfo courseinfo=WOWStatic.CourseInfoTbl.getCourseInfo(coursename);
	    ConceptType concepttype= courseinfo.CT.getConceptType(type);
	    if (concepttype==null) {
	        System.out.println("No concepttype exists with concept type name '"+type+"' in concept "+conceptName+".");
	        return "Invalid concepttype: "+conceptName;
	    }
	    Layout representation=concepttype.getRepresentation();
	    if (representation==null) {
	    	System.out.println("No layout associated with concept type "+type+".");
	    }
	    Window triggerWnd=representation.getTriggerWnd();

	    Annotation anno=WOWStatic.CourseInfoTbl.getCourseInfo(coursename).CT.getConceptType(type).getAnnotation();
	    if (anno==null) System.out.println("HTMLAnchor Error: anno null");

	    linkAnno=anno.getLinkAnno();

	    //LinkAnnotation (!!!Doesn't use Conceptypes table but standard WOW-engine function)
	    //if(resource!=null && linkAnno!=null) linkClass=linkAnno.getLastGBN("Good","Bad","Neutral");
	    if(linkAnno!=null) linkClass=linkAnno.getLinkAnnotation(conceptName, profile, "MainView");
	    else linkClass="";
	    // rtn=rtn+"<a class='"+linkClass+"' HREF='"+WOWStatic.config.Get("CONTEXTPATH")+"/Get?concept="+conceptName+"' TARGET='"+triggerWnd.getName()+"' onClick=\"window.open(href, target, '"+triggerWnd.getWndOpts()+"'); return false\">"+title+"</a>";
	    int insertpos = rtn.length();
	    rtn.append("<a class='" + linkClass + 
	    	"' href='" + WOWStatic.config.Get("CONTEXTPATH") + "/Get?concept=" + conceptName + "' target='" + triggerWnd.getName() + "' ");
        if(gray) rtn.append("style='color: #808080' ");
        rtn.append(">" + title + "</a>");
        
        if(hasIcon) {
		    //Icon Annotation
		    IconAnnotation iconAnno=anno.getIconAnno();
		    LinkedList icons=iconAnno.getIconAnnotation(conceptName,profile,"MainView");
		    Iterator iter=icons.iterator();
		    String icon=null;
		    while(iter.hasNext()){
		    	icon=iter.next().toString();
		    	boolean front = false;
		    	if (icon.startsWith("<front>")) {icon = icon.substring(7);front = true;}
		    	String insstring = "<IMG src='"+WOWStatic.config.Get("CONTEXTPATH")+"/"+this.directory+"/"+icon+"' ALIGN='BOTTOM' ALT='' WIDTH=15 HEIGHT=14>";
		    	if (front) {
		    		rtn.insert(insertpos, insstring);
		    		insertpos += insstring.length();
		    	}
		    	else
		    		rtn.append(insstring);
		    }
        }
	    return rtn.toString();
	}

}
