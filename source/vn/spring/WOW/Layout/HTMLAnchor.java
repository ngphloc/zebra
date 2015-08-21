/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * HTMLAnchor.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.Layout;

import java.util.*;
import vn.spring.WOW.engine.*;
import vn.spring.WOW.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.exceptions.*;

public class HTMLAnchor {

  /**
   * Constructor
   */
  protected String directory = null;
  public HTMLAnchor(){

  }

  /**
   * Generates string containing HTML anchor code to new concept(all features on)
   */
  public String genLinkTo(String conceptName,Profile profile){
    try {
        this.directory = profile.getAttributeValue("personal","directory");
    } catch (InvalidAttributeException iae) {System.out.println(iae);}
    return genLinkTo(conceptName, profile,true);
  }

  /**
   * Generates string containing HTML anchor code to new concept
   */
  public String genLinkTo(String conceptName,Profile profile,boolean frontOn){

    StringBuffer rtn= new StringBuffer();
    String linkClass=null;
    LinkAnnotation linkAnno=null;
    String coursename=null;
    try{
      coursename=profile.getAttributeValue("personal","course");
    }catch(Exception e){System.out.println("genLinkTo Error:Course attribute not found in the profile");}
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
    rtn.append("<a class='"+linkClass+"' href='"+WOWStatic.config.Get("CONTEXTPATH")+"/Get?concept="+conceptName+"' target='"+triggerWnd.getName()+"'>"+title+"</a>");

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
      } else
          rtn.append(insstring);
    }

    return rtn.toString();
  }

  /**
   * Generates string containing HTML anchor code to sec wnd 'wndName' of activeconcept
   */
  public String genLinkTo(SecWndLink swl,String activeconcept,String coursename, String dir){

    String rtn=null;
    String type=null;
    //String JS_TrgWndName=null;

    try{
      long id=WOWStatic.DB().getConceptDB().findConcept(activeconcept);
      Concept concept=WOWStatic.DB().getConceptDB().getConcept(id);
      type=concept.getType();

      Layout layout=WOWStatic.CourseInfoTbl.getCourseInfo(coursename).CT.getConceptType(type).getRepresentation();
      Window wnd=layout.getWnd(swl.getWndName());
      //JS_TrgWndName=layout.getTriggerWnd().getName();
      //String JS_Var="w=top; while(w.name.toString()!='"+JS_TrgWndName+"'){\n w=w.opener;}  alert(w.name); w";
      //rtn="<a HREF='"+WOWStatic.config.Get("CONTEXTPATH")+"/Get?wndName="+wnd.getName()+"' TARGET='"+wnd.getName()+"' onClick=\""+JS_Var+"."+wnd.getName()+"=window.open(href, target, '"+wnd.getWndOpts()+"'); alert(w."+wnd.getName()+".name);return false\"><IMG SRC='"+WOWStatic.config.Get("CONTEXTPATH")+"/images/"+swl.getImg()+"' ALIGN='BOTTOM' ALT='"+swl.getLinkName()+"' BORDER=0 WIDTH=70 HEIGHT=19></a>";
      rtn="<a HREF='"+WOWStatic.config.Get("CONTEXTPATH")+"/Get?wndName="+wnd.getName()+"' TARGET='"+wnd.getName()+"'><IMG SRC='"+WOWStatic.config.Get("CONTEXTPATH")+"/"+dir+"/"+swl.getImg()+"' ALIGN='BOTTOM' ALT='"+swl.getLinkName()+"' BORDER=0 WIDTH=70 HEIGHT=19></a>";
      }
    catch(Exception e) {System.out.println("HTMLAnchor.getLinkTo Error"+e.getMessage());}
    return rtn;
  }




}
