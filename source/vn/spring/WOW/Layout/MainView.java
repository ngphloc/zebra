/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * MainView.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.Layout;


import java.io.*;
import java.util.*;
import java.net.URL;

import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.parser.*;


/**
 * Main View- contains the old Get servlet functionality but as implementation of an View
 */

public class MainView implements View {

  final static private String viewType="MAINVIEW";
  final static private boolean stat=false;

  private String viewName=null;
  public String title=null;
  public String background=null;
  private String mime=null;

  public String initParams=null;   //view specific parameters from view.xml file
  private SecWndLinks secWndLinks=null;

//=============================================================


//Constructor
  public MainView(String name,String bg,String params,String title,SecWndLinks swls){
    this.viewName=name;
    this.background=bg;
    this.initParams=params;
    this.title=title;

    this.secWndLinks= swls;

  }

  public MainView(){

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
   * returns returns true is view needs to be updated when MAIN updated
   */
   public boolean isStatic(){
     return MainView.stat;
   }


  /**
   * returns the view with name viewName
  */
   public String getViewName(){
     return viewName;
   }

   /**
   * returns the viewtype of this view(CONCEPTBARVIEW)
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


       //Check profile
       if (profile==null) System.out.println("MAINView:no Profile?? I just fetched it, did I? Dereferenced from context??");

       String URI = "";

       String activeconcept = conceptname;
       ConceptDB cdb = WOWStatic.DB().getConceptDB();
       try {
           Concept concept = cdb.getConcept(cdb.findConcept(activeconcept));
           UMVariableLocator umvl = new UMVariableLocator(profile, cdb);
           URL               url = concept.getActiveURL(umvl);
           if (url != null)  URI = url.toString();
       } catch (Exception e) {
           e.printStackTrace();
       }

       // Locate and process the resource
       Resource resource;
       try {
           resource = WOWStatic.HM().locateResource(URI);
       } catch (Exception e) {
           resource = null;
       }

       if (resource != null) {
           try {
               String baseID = (String)params.get("wow_path"); //E.g: http://localhost:8080/wow
               if(baseID != null && baseID.charAt(baseID.length() - 1) != '/') baseID = baseID + "/";
               
               resource.setCallerID("Get");
               resource.setBaseID(baseID);
               resource.setConceptName(activeconcept);
               // ignore images, not really ignore but do not execute rules for them (they don't have them)
               // do not log this access in the log db
               String mime2 = resource.getType().getMime();
               if (! mime2.startsWith("image")) {
                      resource = WOWStatic.HM().processComplete(resource, profile);
                      if (resource == null)
                        throw new WOWException("The resource is null");

               }
           }catch (Exception e) {
               System.out.println("MainView: error output: " + e.getClass().getName() + ": " + e.getMessage());
               e.printStackTrace(System.out);
           }
           //Set mime
           this.mime=resource.getType().getMime();
           // if (this.mime==null) mime="text/html";
           this.mime="text/html";
           //Convert output to inputstream(needed by the servlet)
           InputStream insb = resource.getInputStream();
           return insb;
       } else {
           StringBuffer res = new StringBuffer();
           res.append("<html><body><h1>");
           Concept concept = null;
           try {
               concept = WOWStatic.DB().getConceptDB().getConcept(WOWStatic.DB().getConceptDB().findConcept(activeconcept));
           } catch (Exception e) {}
           res.append(concept.getTitle());
           res.append("</body></html>");
           return new ByteArrayInputStream(res.toString().getBytes());
       }
    }
}