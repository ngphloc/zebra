/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * HelpView.java 1.0, February 22, 2005
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.Layout;


import java.util.*;
import java.io.*;

import vn.spring.WOW.engine.*;
import vn.spring.WOW.parser.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
/**
 * Help View
 */
public class HelpView implements View {

  final static private String viewType="HELPVIEW";
  final static private boolean stat=false;

  private String viewName=null;
  private String title=null;
  private String background=null;
  private String mime="text/html";

  private String initParams=null;   //view specific parameters from view.xml file

  private SecWndLinks secWndLinks=null;

  //Constructor
  public HelpView(String name,String bg,String params,String title,SecWndLinks swls){
    this.viewName=name;
    this.background=bg;
    this.initParams=params;
    this.title=title;

    this.secWndLinks=swls;

  }

  public HelpView(){

 }

  public void setSecWndLinks(SecWndLinks swl){
    this.secWndLinks=swl;
  }

  public void setViewName(String name){
    this.viewName=name;
  }

  public void setBackgound(String back){
    this.background=back;
  }

  public void setParams(String params){
    this.initParams=params;
  }

  public void setTitle(String title){
    this.title=title;
  }




  /**
   * returns secWndLInks table
   */
  public SecWndLinks getSecWndLinks(){
    return this.secWndLinks;
  }

   /**
   * returns true is view needs to be updated when MAIN updated
   */
   public boolean isStatic(){
     return HelpView.stat;
   }



  /**
   * returns the view with name viewName
  */
   public String getViewName(){
     return viewName;
   }

   /**
   * returns the viewtype of this view(HELPVIEW)
   */
   public String getViewType(){
      return viewType;
   }

   /**
    * returns the Mime type of the response generated by the view
    */
    public String getMime(){
      return this.mime;
  }

 /**
 * generates the the Browers code(HTML/XHTML) for this view
 */
    public InputStream genBrsCode(String conceptname, Profile profile, Map params){

      HTMLAnchor link=new HTMLAnchor();
      String conceptName=conceptname;
      String course = ""; try {course = profile.getAttributeValue("personal", "course");} catch (Exception e) {e.printStackTrace();}
      ConceptDependences cd=(ConceptDependences) WOWStatic.CourseInfoTbl.getCourseInfo(course).dependences.getDependences().get(conceptName);
      String cName=null;

      //Construct HTML code for the menu output file
      StringBuffer outstr = new StringBuffer();

      try {
         String directory = null;
        directory = profile.getAttributeValue("personal","directory");

        String conceptpath=WOWStatic.config.Get("CONTEXTPATH");
        outstr.append("<html><head>");
        outstr.append("<meta http-equiv='Cache-Control' content='no-cache' />\n<meta http-equiv='Expires' content='-1' />\n<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\" />");


        outstr.append("<title>"+this.title+"</title></head><body background='"+conceptpath+"/"+directory+"/"+background+"' >\n\n");
        //put the hole output in a second cell of a 2-cell table(for shifting to the right)
        outstr.append("<table width=100%><tr><td width="+WOWStatic.VM().getParam("leftspace",this.initParams)+">&nbsp;</td><td>\n");
        } catch (InvalidAttributeException iae) {System.out.println(iae);}

    //======================NEW CODE============
        LinkedList pre=cd.getPrerequisites();       // concepts conceptName has as prerequisite
        LinkedList out=new LinkedList();
        //Show prerequisites
        //Sort the prerequisites list
        TreeSet treeSet=new TreeSet(pre);
        Iterator iterator=treeSet.iterator();
try {
        if(iterator.hasNext()){

            outstr.append("<b><h3>These pages could help you understanding<br>\'") ;
            outstr.append(link.genLinkTo(conceptName,profile,false)+"\':</h3></b>\n");
            while(iterator.hasNext()){
              cName=(String)iterator.next();
              cd=(ConceptDependences) WOWStatic.CourseInfoTbl.getCourseInfo(course).dependences.getDependences().get(cName);
              LinkedList tempout = cd.getOutcomeINs();              // concepts with cName as outcome
                 if (tempout==null)
                 {}else{
                      if (tempout.size()>1)
                      {tempout.remove(cName);
                      }
                      out.removeAll(tempout);
                      out.addAll(tempout);          //all items only occur once now
                 }
            }

            //Show outcome pages
            //Sort the outcomes list
            // NOTE this should be improved/changed
             treeSet=new TreeSet(out);
             LinkedList sortedOut = new LinkedList(helpSort(out, pre, course));

            boolean suitable = false;
            boolean visited = false;

             //first suitable non visited
             iterator=sortedOut.iterator();
             if(iterator.hasNext()) {
                 while(iterator.hasNext()){
                   cName=(String)iterator.next();
                   suitable = conceptState(cName, "suitability", profile);
                   visited  = conceptState(cName, "visited", profile);
                    if (suitable && !visited)
                    {
                       HTMLAnchor link1=new HTMLAnchor();
                       outstr.append(link1.genLinkTo(cName,profile)+"<br />\n");
                    }
                }
             }
             //second suitable visited
             iterator=sortedOut.iterator();
             if(iterator.hasNext()) {
                 while(iterator.hasNext()){
                   cName=(String)iterator.next();
                   suitable = conceptState(cName, "suitability", profile);
                   visited  = conceptState(cName, "visited", profile);
                    if ( suitable && visited)
                    {
                       HTMLAnchor link1=new HTMLAnchor();
                       outstr.append(link1.genLinkTo(cName,profile)+"<br />\n");
                    }
                }
             }
             //third not suitable
             iterator=sortedOut.iterator();
             if(iterator.hasNext()) {
                 while(iterator.hasNext()){
                   cName=(String)iterator.next();
                   suitable = conceptState(cName, "suitability", profile);
                    if (!suitable)
                    {
                       HTMLAnchor link1=new HTMLAnchor();
                       outstr.append(link1.genLinkTo(cName,profile)+"<br />\n");
                    }
                }
             }
         }  else{
            outstr.append("<b><h3>I'm sorry. I can't help you</h3></b><br>\n") ;
         }
         }
         catch (Exception e) {
         outstr.append("<b><h3>I'm sorry. I can't help you. </h3></b>\nSomething went wrong.<br>\n") ;
         System.err.println("HelpView: "+ e.getMessage());
         e.printStackTrace(System.out);
         }
    //==================================
        outstr.append("</TD></TABLE>");
        outstr.append("</BODY></HTML>");



      //Convert output to inputstream(needed by the servlet)
     InputStream insb = new ByteArrayInputStream(outstr.toString().getBytes());

      return insb;
    }

    private boolean conceptState(String cname, String Attr, Profile profile) {
        UMVariableLocator umvl = new UMVariableLocator(profile, WOWStatic.DB().getConceptDB());
        Object r = null;
        try {
            r = umvl.getVariableValue(cname+"." + Attr);
        } catch (ParserException e) {
            return false;
        }
        if (r instanceof String) return false;
        else if (r instanceof Float) return ((Float)r).intValue()>0;
        else if (r instanceof Boolean) return ((Boolean)r).booleanValue();
        else return false;
    }

    private LinkedList helpSort(LinkedList pages, LinkedList prereqs, String course) {
        TreeSet bin;
        LinkedList bins = new LinkedList();
        int preSize = prereqs.size();
        //make enough bins
        for (int i = 0; i <= preSize; i++) {
           bin = new TreeSet();
           bins.add(bin);
        }
        //fill the bins
        Iterator iterator=pages.iterator();
        String cName;
        if(iterator.hasNext()){
            while(iterator.hasNext()){
              cName=(String)iterator.next();
              ConceptDependences cd=(ConceptDependences) WOWStatic.CourseInfoTbl.getCourseInfo(course).dependences.getDependences().get(cName);
              LinkedList tempout = new LinkedList(cd.getOutcomes());                // concepts cName has as outcome
              tempout.retainAll(prereqs);
              bin = (TreeSet)bins.get(preSize-tempout.size());
              bin.add(cName);
            }
        }
        //serialize bins for output
        LinkedList outp = new LinkedList();
        Iterator binsIterator=bins.iterator();
        if(binsIterator.hasNext()){
            while(binsIterator.hasNext()){
                bin=(TreeSet)binsIterator.next();
                iterator=bin.iterator();
                if(iterator.hasNext()){
                    while(iterator.hasNext()){
                    outp.add((String)iterator.next());
                    }
                }
            }
        }
        return outp;
    }

}