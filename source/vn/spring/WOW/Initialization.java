/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Initialization.java 1.0, August 30, 2008
 *
 * Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW;

import vn.spring.WOW.WOWStatic;
import java.io.*;
import java.util.*;

import vn.spring.WOW.engine.*;
import vn.spring.WOW.Layout.*;
import org.w3c.dom.*;
import com.sun.xml.tree.*;


import vn.spring.WOW.WOWDB.XMLUtil;

import javax.servlet.http.HttpSession;



/**
 * This class initialize the in-mem representation of data by reading the data from
 * XML files. Modified by Loc Nguyen 2008
 */
public class Initialization {
	/**
	 *   Main method for initializing of in-mem data structs
	 *   This method calls all other methods of initialization process
	 */
	static public void fillInMemStructs(HttpSession ses, boolean reload){
		fillInMemStructs(ses.getAttribute("course").toString(), ses.getAttribute("directory").toString(), reload);
	}
  	
	//Loc Nguyen add 2009.02.16
  	static public void fillInMemStructs(String course, String directory, boolean reload){
  	    //fill High level in-mem data structs
  		fillHLViewsDataSources(course, reload);
	    
  	    //fill concept-types object (ConceptTypes)
  		fillConceptTypes(course, directory);
	    
  	    //fill layout objects
  		loadCourseLayout(course, directory);
  	}


  /**
   * Fills ConceptTypes object
   */
  static public void fillConceptTypes(String courseName, String courseDir){
	//@Loc Nguyen add 2010.01.28
	//if(WOWStatic.CourseInfoTbl.getCourseInfo(courseName).CT != null) return;

	//Create instance of concept info table
    WOWStatic.CourseInfoTbl.getCourseInfo(courseName).linkclasses = new Vector();
    Vector linkclasses = WOWStatic.CourseInfoTbl.getCourseInfo(courseName).linkclasses;
    WOWStatic.CourseInfoTbl.getCourseInfo(courseName).CT=new ConceptTypes();

    File file=new File(WOWStatic.config.Get("WOWROOT"),courseDir+"/ConceptTypeConfig.xml");
    XmlDocument doc=null;

    try{  doc = XMLUtil.getXML(file);}
    catch(Exception e){ System.out.println("fillConceptTypes:Error reading concepttypeconfig.xml");}

    Element element=null; //XML hierarchy element

  //Get all the concepttypes
    element = (Element) doc.getElementsByTagName("concepttypes").item(0);
    element= (Element) element.getFirstChild(); //concepttype

    while(element!=null){

      Element ctch=(Element) element.getFirstChild(); //type el
      String type=XMLUtil.nodeValue(ctch);

      ctch=(Element) ctch.getNextSibling(); //representation el
      String representation=XMLUtil.nodeValue(ctch);

      ctch=(Element) ctch.getNextSibling(); //annotation el

      //create annotation object
      Annotation anno=new Annotation();

      //get linkannotation (in the future this element can be used to init Linkannotation
      Element linkAnnoEl = (Element) ctch.getFirstChild();//linkanno
      LinkAnnotation linkAnno = new LinkAnnotation();

      Element linkEl = null;
      linkEl = (Element)linkAnnoEl.getFirstChild();

      while (linkEl != null) {
          String linkclass = XMLUtil.nodeValue(linkEl);
          if (!linkclasses.contains(linkclass)) linkclasses.add(linkclass);
          String expr = linkEl.getAttribute("expr");
          String view = linkEl.getAttribute("view");
          linkAnno.addLinkInfo(linkclass, expr, view);
          linkEl = (Element)linkEl.getNextSibling();
      }
      //add linkannotation to its annotation object
      anno.setLinkAnno(linkAnno);

      //get iconannotation
      Element iconAnnoEl = (Element)linkAnnoEl.getNextSibling();
      IconAnnotation iconAnno = new IconAnnotation();

      Element iconEl = null;
      iconEl = (Element)iconAnnoEl.getFirstChild();

      while (iconEl != null) {
          String iconname = XMLUtil.nodeValue(iconEl);
          String expr = iconEl.getAttribute("expr");
          String place = iconEl.getAttribute("place");
          String view = iconEl.getAttribute("view");
          iconAnno.addIconInfo(iconname, expr, place, view);
          iconEl = (Element)iconEl.getNextSibling();
      }

      //add iconannotation to its annotation object
       anno.setIconAnno(iconAnno);

      //make concepttype object
      ConceptType ct=new ConceptType(type,representation,anno,courseName);

      //add new type to the concepttype table
      WOWStatic.CourseInfoTbl.getCourseInfo(courseName).CT.addType(type,ct);
      //System.out.print("ConceptType added to concept type table: "+type);
      //get next concepttype
       element=(Element) element.getNextSibling();
    }

  }

  /**
   * Fills layout objects
   */
  static public void loadCourseLayout(String courseName, String courseDir){
		//@Loc Nguyen add 2010.01.28
	//CourseInfo courseInfo = WOWStatic.CourseInfoTbl.getCourseInfo(courseName);
	//if(courseInfo.layoutList != null && courseInfo.stylesheet != null) return;

	String type=null,title=null,background=null,params=null,name=null; //parameters
    File file=new File(WOWStatic.config.Get("WOWROOT"),courseDir+"/LayoutConfig.xml");
    XmlDocument doc=null;
    Element element=null; //XML hierarchy element

    try{  doc = XMLUtil.getXML(file);}
    catch(Exception e){ System.out.println("loadCourseLayout:Error reading LayoutConfig.xml");}

    //Create instance of concept info table
    WOWStatic.CourseInfoTbl.getCourseInfo(courseName).layoutList=new LayoutList();

  //Get all the views
    element = (Element) doc.getElementsByTagName("viewlist").item(0);
    element= (Element) element.getFirstChild(); //first view

    while(element!=null){

      //read views paramenters
       type=element.getAttribute("type");
       title=element.getAttribute("title");
       background=element.getAttribute("background");
       params=element.getAttribute("params").replaceAll(",","&");
       name=element.getAttribute("name");

       //get its children(secondary link/window list)
       SecWndLinks swls=null;
       Element secWndsEl=(Element) element.getFirstChild();

       if (secWndsEl!=null){

         //get all its children(secWndLinks)
          Element secWndEl= (Element) secWndsEl.getFirstChild();
          swls=new SecWndLinks();
          while(secWndEl!=null){
            //Add secwndlink to swls table
            SecWndLink swl=new SecWndLink(secWndEl.getAttribute("link"),secWndEl.getAttribute("viewgroup"),secWndEl.getAttribute("img"));
            swls.addSwl(swl.getLinkName(),swl);
            secWndEl=(Element) secWndEl.getNextSibling();
          }
       }

       //Make View obj and add it to ViewManager
       try{
        View v=null;
        Class viewclass = Class.forName("vn.spring.WOW.Layout."+type);
        v = (View) viewclass.newInstance();
        v.setViewName(name);
        v.setBackgound(background);
        v.setParams(params);
        v.setTitle(title);
        v.setSecWndLinks(swls);
        WOWStatic.VM().addView(v);
       }
       catch(Exception e){System.out.println("Initialization: Error registering view");}

       //get next view
       element=(Element) element.getNextSibling();
       }

       //Views done ======================================================================

    //Get Layout
       element = (Element) doc.getElementsByTagName("layoutlist").item(0);
       element= (Element) element.getFirstChild();

       //for each layout
       while(element!=null){

         String lName=element.getAttribute("name");
         String triggerWndName=element.getAttribute("trigger");
         Layout layout=new Layout(lName,triggerWndName);

         //get all the viewgroups(windows)of this layout
         Element vgEl= (Element) element.getFirstChild();
         //for each viewgroup
         while(vgEl!=null){

           String wndName=vgEl.getAttribute("name");
           String secondary=vgEl.getAttribute("secondary");
           String wndOpt=vgEl.getAttribute("wndOpt");
           Window wnd=new Window(wndName,secondary,wndOpt);

           //generate brscode(framesStruct) for this window and add views to it
           StringBuffer str=new StringBuffer("<html><head><meta http-equiv=\"Cache-Control\" Content=\"no-cache\" />\n");
           str.append("<meta http-equiv=\"Expires\" content=\"-1\" /><meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\" /><title>"+wnd.getName()+"</title></head>\n");

           boolean oneview=vgEl.getFirstChild().getNodeName().toLowerCase().equals("viewref");
           if(oneview){
             //only one view in the window so make a frameset around it
             str.append("<frameset rows='*' border=0>");
           }

           wnd.setBrsCode(str);

           genHtmlFrames((Element) vgEl.getFirstChild(),wnd);

           StringBuffer sb=wnd.getBrsCode();
           if(oneview){

             sb.append("</frameset>");

           }
           sb.append("</html>");

           wnd.setBrsCode(sb);

           //add this viewgroup(window) to the layout
           layout.addWnd(wnd);

           //next viewgroup
           vgEl=(Element) vgEl.getNextSibling();
         }

         //add this layout to the layout list
         WOWStatic.CourseInfoTbl.getCourseInfo(courseName).layoutList.addLayout(layout.getName(),layout);

         //next layout
         element=(Element) element.getNextSibling();
       }

       // Stylesheet
       NodeList list = doc.getElementsByTagName("stylesheet");
       String stylesheet;
       //Loc Nguyen repaired, very carefully
       if (list.getLength()==0) {
    	   //stylesheet = "../WOWstandard/wow.css";
    	   stylesheet = WOWStatic.config.Get("CONTEXTPATH") + "/WOWstandard/wow.css"; //for using portlet
       } else {
           element = (Element)list.item(0);
           //stylesheet = element.getFirstChild().getNodeValue();
    	   stylesheet = WOWStatic.config.Get("CONTEXTPATH") + "/" + courseDir + "/" + element.getFirstChild().getNodeValue(); //for using portlet
       }
       WOWStatic.CourseInfoTbl.getCourseInfo(courseName).stylesheet = stylesheet;

  }

    /**
     * This recursive method generates html Frames code from XML document "element"
     */
    static private void genHtmlFrames(Element element,Window wnd){
       String layout=null,border=null,name=null; //parameters
       StringBuffer htmlCode=wnd.getBrsCode();

       //Check if it is a compound or view element
      if(element.getNodeName().equals("viewref")){//"viewref" element
        name=element.getAttribute("name");

        //Add its html code
        String contextpath=WOWStatic.config.Get("CONTEXTPATH");
        htmlCode.append("<frame src='"+contextpath+"/ViewGet<**>?view=<#"+name+"#>' name='"+name+"' />\n");

        //Add this to wnd
        wnd.addViewName(name);
       }

      else { //"compound" element

        layout=element.getAttribute("framestruct");
        if (element.getAttribute("border").equals(""))
          border="border=0";
        else
          border="border="+element.getAttribute("border");

        htmlCode.append("<frameset "+layout+" "+border+" >\n");
        element=(Element) element.getFirstChild();

        //Generate html code also for all children of 'compound' element
        while(element!=null){

           wnd.setBrsCode(htmlCode);
           genHtmlFrames(element,wnd);
           htmlCode=wnd.getBrsCode();
           element=(Element) element.getNextSibling();
          }

         //Close the frameset
          htmlCode.append("</frameset>\n");
        }

        wnd.setBrsCode(htmlCode);


    }


  /**
   * Fills Hierarchy, Dependancies and ConceptInfo objects(uses data from the DB or temp files)
   */
  static public void fillHLViewsDataSources(String courseName, boolean reload){
	  //Loc Nguyen add 2010.01.28
	  if(reload && WOWStatic.CourseInfoTbl.getCourseInfo(courseName) != null) return;

      // changed by @Loc Nguyen @2009.01.28
      CourseInfo ci = null;
      try {
          //Make new CourseInfo object and add it to the CourseInfoTbl
          ci=new CourseInfo();
          ci.courseName=courseName;
          //Fill Dependences obj with DB data
          ci.dependences=new Dependences();
          ci.dependences.readDependences(courseName);
          //Fill Hierarchy obj with DB data
          ci.hierarchy=new Hierarchy(WOWStatic.root);
          ci.hierarchy.readHierarchy(courseName);
          //Fill ConceptInfo obj with DB data
          ci.conceptsInfoTbl=new ConceptsInfoTbl();
          ci.conceptsInfoTbl.readConceptInfo(courseName);
      } catch (Throwable e) {
          e.printStackTrace();
          ci = null;
      }
	  //Loc Nguyen add 2010.01.28
      if(ci != null) WOWStatic.CourseInfoTbl.setCourseInfo(ci);
   }


}
