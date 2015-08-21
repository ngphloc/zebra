/*


    This file is part of WOW! (Adaptive Hypermedia for All)

    WOW! is free software


*/
/**
 * SortedView.java 1.0, November 18, 2008
 * a version of the TreeView.java with concepts sorted according to their desirability
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.Layout;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;

import vn.spring.WOW.engine.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.parser.*;

/**
 * Path from root to active concept View plus children with concepts sorted according to their desirability
 */
public class SortedView implements View {


  final static private String viewType="SORTEDVIEW";
  final static private boolean stat=false;

  private String viewName=null;
  private String title=null;
  public  String background=null;
  private String mime="text/html";
  private String initParams=null;   //view specific params from view.xml file

  private SecWndLinks secWndLinks=null;
  public  String directory = null;

  //--------------------------------
  // Vars for local use
  private Profile profile=null;

//=============================================================
    final String GOOD = "Good";
    final String BAD = "Bad";
    final String NEUTRAL = "Neutral";

  //Constructor
  public SortedView(String name,String bg,String params,String title,SecWndLinks swls){
    this.viewName=name;
    this.background=bg;
    this.initParams=params;
    this.title=title;

    this.secWndLinks=swls;

  }

  public SortedView(){

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
      return SortedView.stat;
   }


  /**
   * returns the view with name viewName
   */
   public String getViewName(){
     return viewName;
   }

  /**
  * returns the viewtype of this view(TOCVIEW)
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


  public InputStream genBrsCode(String conceptname, Profile profile, Map params) {
	  return null;
  }
  
 /**
 * generates the the Browers code(HTML/XHTML) for this view
 */
    public InputStream genBrsCode(HttpServletRequest request,Profile profile){


      //Construct HTML code for the menu output file
      StringBuffer outstr = new StringBuffer(2000);
      HTMLAnchor link=new HTMLAnchor();

      this.profile=profile;
        try {
        this.directory = profile.getAttributeValue("personal","directory");
        } catch (InvalidAttributeException iae) {System.out.println(iae);}
      WOWStatic.config.Get("CONTEXTPATH");

        try {
        ObjectFilter of = new ObjectFilter(profile);
        outstr.append("<html>\n<head>\n");
        outstr.append("<style type=\"text/css\">");
        outstr.append("\na:link { color: #");
        outstr.append(of.getExternalLink());
        outstr.append(" }\na:visited { color: #");
        outstr.append(of.getExternalVisitedLink());
        outstr.append(" }\na:active { color: #");
        outstr.append(of.getActiveLink());
        outstr.append(" }\na.Good:link { text-decoration: none; color: #");
        outstr.append(of.getGoodLink());
        outstr.append(" }\na.Good:visited { text-decoration: none; color: #");
        outstr.append(of.getGoodLink());
        outstr.append(" }\na.Good:hover { text-decoration: none; color: #");
        outstr.append(of.getGoodLink());
        outstr.append(" }\na.Good:active { text-decoration: none; color: #");
        outstr.append(of.getGoodLink());
        outstr.append(" }\na.Bad:link { text-decoration: none; color: #");
        outstr.append(of.getBadLink());
        outstr.append(" }\na.Bad:visited { text-decoration: none; color: #");
        outstr.append(of.getBadLink());
        outstr.append(" }\na.Bad:hover { text-decoration: none; color: #");
        outstr.append(of.getBadLink());
        outstr.append(" }\na.Bad:active { text-decoration: none; color: #");
        outstr.append(of.getBadLink());
        outstr.append(" }\na.Neutral:link { text-decoration: none; color: #");
        outstr.append(of.getNeutralLink());
        outstr.append(" }\na.Neutral:visited { text-decoration: none; color: #");
        outstr.append(of.getNeutralLink());
        outstr.append(" }\na.Neutral:hover { text-decoration: none; color: #");
        outstr.append(of.getNeutralLink());
        outstr.append(" }\na.Neutral:active { text-decoration: none; color: #");
        outstr.append(of.getNeutralLink());
        outstr.append(" }\na.Unconditional:link { text-decoration: none; color: #");
        outstr.append(of.getGoodLink());
        outstr.append(" }\na.Unconditional:visited { text-decoration: none; color: #");
        outstr.append(of.getNeutralLink());
        outstr.append(" }\na.Unconditional:hover { text-decoration: none; color: #");
        outstr.append(of.getGoodLink());
        outstr.append(" }\na.Unconditional:active { text-decoration: none; color: #");
        outstr.append(of.getGoodLink());
        outstr.append(" }\na.unconditional:link { text-decoration: none; color: #");
        outstr.append(of.getGoodLink());
        outstr.append(" }\na.unconditional:visited { text-decoration: none; color: #");
        outstr.append(of.getNeutralLink());
        outstr.append(" }\na.unconditional:hover { text-decoration: none; color: #");
        outstr.append(of.getGoodLink());
        outstr.append(" }\na.unconditional:active { text-decoration: none; color: #");
        outstr.append(of.getGoodLink());
        outstr.append(" }");
            String bg = profile.getAttributeValue("personal", "background");
            outstr.append("\nBODY {background: url(\"");
            outstr.append(bg);
            outstr.append("\")}");
        } catch (Exception e) {
        // maybe the background attribute does not exist
        }
        outstr.append("\n</style>\n");

      outstr.append("<meta http-equiv='Cache-Control' content='no-cache' />\n<meta http-equiv='Expires' content='-1' />\n<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\" />");
      outstr.append("<title>"+this.title+"</title></head><body>");
      if(WOWStatic.CourseInfoTbl.getCourseInfo(request.getSession().getAttribute("course").toString()).hierarchy.getRootNode()!=null){
      //Move everything a litle bit to the right
      outstr.append("<table cellspacing=0 width=100%><tr><td width='"+WOWStatic.VM().getParam("leftspace",this.initParams)+"'>&nbsp;</td><td>\n\n");

    //Generate HTML code for each Node in the Path starting with the rootNode
      //Get path
      LinkedList path=new LinkedList();
      TNode rNode=WOWStatic.CourseInfoTbl.getCourseInfo(request.getSession().getAttribute("course").toString()).hierarchy.getTNode(request.getSession().getAttribute("activeconcept").toString());
      TNode curNode = rNode;
      while(curNode!=null){
			  path.addFirst(curNode.getName());
			  curNode=curNode.getParent();
      }
      //Show Path
      Iterator iter=path.iterator();
      int width=20;
      while (iter.hasNext()) {

        String item = iter.next().toString();

        if(item.toLowerCase().equals(request.getSession().getAttribute("activeconcept").toString().toLowerCase())) {
          if (rNode.getParent()!=null) curNode=rNode.getParent().getFirstChild(); else curNode=rNode;
          if (curNode.getFirstChild() != null) {
			  while (curNode!=null) {
				  if (curNode.getName().toLowerCase().equals(item.toLowerCase())) {
					  if (isAllowedConcept(curNode.getName())) outstr.append(HTMLrep(WOWStatic.CourseInfoTbl.getCourseInfo(request.getSession().getAttribute("course").toString()).conceptsInfoTbl.getConceptInfo(curNode.getName()).getTitle(), width+14, true));
					  TNode cnode = curNode.getFirstChild();
					  while (cnode != null) {
						  if (isAllowedConcept(cnode.getName()) && (evaluateExpression(profile, cnode.getName()+".suitability")) && (evaluateExpression(profile, cnode.getName() + ".visited")?NEUTRAL:GOOD).equals("Good")) outstr.append(HTMLrep(link.genLinkTo(cnode.getName(),this.profile), width+20, false));
						  cnode = cnode.getNextSib();
					  }
					  cnode = curNode.getFirstChild();
					  while (cnode != null) {
						  if (isAllowedConcept(cnode.getName()) && (evaluateExpression(profile, cnode.getName()+".suitability")) && (evaluateExpression(profile, cnode.getName() + ".visited")?NEUTRAL:GOOD).equals("Neutral")) outstr.append(HTMLrep(link.genLinkTo(cnode.getName(),this.profile), width+20, false));
						  cnode = cnode.getNextSib();
					  }
					  cnode = curNode.getFirstChild();
					  while (cnode != null) {
						  if (isAllowedConcept(cnode.getName()) && !evaluateExpression(profile, cnode.getName()+".suitability")) outstr.append(HTMLrep(link.genLinkTo(cnode.getName(),this.profile), width+20, false));
						  cnode = cnode.getNextSib();
					  }
				  } else {
					  if (isAllowedConcept(curNode.getName())) outstr.append(HTMLrep(link.genLinkTo(curNode.getName(),this.profile), width, false));
				  }
				  curNode = curNode.getNextSib();
			  }
		  }
		  else {
			  while (curNode!=null) {
				  if (curNode.getName().toLowerCase().equals(item.toLowerCase())) {
					  if (isAllowedConcept(curNode.getName()) && (evaluateExpression(profile, curNode.getName()+".suitability")) && (evaluateExpression(profile, curNode.getName() + ".visited")?NEUTRAL:GOOD).equals("Good")) outstr.append(HTMLrep(WOWStatic.CourseInfoTbl.getCourseInfo(request.getSession().getAttribute("course").toString()).conceptsInfoTbl.getConceptInfo(curNode.getName()).getTitle(), width+14, true));
				  } else {
					  if (isAllowedConcept(curNode.getName()) && (evaluateExpression(profile, curNode.getName()+".suitability")) && (evaluateExpression(profile, curNode.getName() + ".visited")?NEUTRAL:GOOD).equals("Good")) outstr.append(HTMLrep(link.genLinkTo(curNode.getName(),this.profile), width, false));
				  }
				  curNode = curNode.getNextSib();
			  }
			  if (rNode.getParent()!=null) curNode=rNode.getParent().getFirstChild(); else curNode=rNode;
			  while (curNode!=null) {
				  if (curNode.getName().toLowerCase().equals(item.toLowerCase())) {
					  if (isAllowedConcept(curNode.getName()) && (evaluateExpression(profile, curNode.getName()+".suitability")) && (evaluateExpression(profile, curNode.getName() + ".visited")?NEUTRAL:GOOD).equals("Neutral")) outstr.append(HTMLrep(WOWStatic.CourseInfoTbl.getCourseInfo(request.getSession().getAttribute("course").toString()).conceptsInfoTbl.getConceptInfo(curNode.getName()).getTitle(), width+14, true));
				  } else {
					  if (isAllowedConcept(curNode.getName()) && (evaluateExpression(profile, curNode.getName()+".suitability")) && (evaluateExpression(profile, curNode.getName() + ".visited")?NEUTRAL:GOOD).equals("Neutral")) outstr.append(HTMLrep(link.genLinkTo(curNode.getName(),this.profile), width, false));
				  }
				  curNode = curNode.getNextSib();
			  }
			  if (rNode.getParent()!=null) curNode=rNode.getParent().getFirstChild(); else curNode=rNode;
			  while (curNode!=null) {
				  if (curNode.getName().toLowerCase().equals(item.toLowerCase())) {
					  if (isAllowedConcept(curNode.getName()) && !evaluateExpression(profile, curNode.getName()+".suitability")) outstr.append(HTMLrep(WOWStatic.CourseInfoTbl.getCourseInfo(request.getSession().getAttribute("course").toString()).conceptsInfoTbl.getConceptInfo(curNode.getName()).getTitle(), width+14, true));
				  } else {
					  if (isAllowedConcept(curNode.getName()) && !evaluateExpression(profile, curNode.getName()+".suitability")) outstr.append(HTMLrep(link.genLinkTo(curNode.getName(),this.profile), width, false));
				  }
				  curNode = curNode.getNextSib();
			  }
		  }
        } else {
            if (isAllowedConcept(item)) outstr.append(HTMLrep(link.genLinkTo(item,this.profile), width, false));
        }
        width+=20;
      }
      }
      else{
        System.out.println("SortedView Error: NO Hierarchy found-> NO Tree view code");
      }
      outstr.append("</body></html>");

      //Convert output to inputstream(needed by the servlet)
      InputStream insb = new ByteArrayInputStream(outstr.toString().getBytes());

      return insb;
    }

    private boolean isAllowedConcept(String name) {
        boolean result = true;
        ConceptDB cdb = WOWStatic.DB().getConceptDB();
        UMVariableLocator umvl = new UMVariableLocator(profile, cdb);
        try {
            Object value = umvl.getVariableValue(name+".hierarchy");
            if (value instanceof Boolean) result = ((Boolean)value).booleanValue();
            if (value instanceof Float) result = ((Float)value).floatValue()!=0;
        } catch (Exception e) {}
        return result;
    }

    private String HTMLrep(String item, int width, boolean bold) {
        StringBuffer res = new StringBuffer();
        res.append("<table cellspacing=0 border=0 width=100%><tr><td width=\""+width+"\"></td>");
        res.append("<td>"+(bold?"<b>":"")+item+(bold?"</b>":""));
        res.append("</td></tr></table>\n");
        return res.toString();
    }
    //adopted from Link.java
    public String hasVisited(Concept concept) throws InvalidAttributeException {
        return (evaluateExpression(profile, concept.getName() + ".visited")?NEUTRAL:GOOD);
    }
	//adopted from Link.java
    public boolean evaluateExpression(Profile profile, String expression) {
        try {
            vn.spring.WOW.parser.Parser parser = WOWStatic.createUMParser(profile);
            Object result = parser.parse(expression);
            if (result instanceof Float) return ((Float)result).floatValue() > 0;
            if (result instanceof Boolean) return ((Boolean)result).booleanValue();
            return false;
        } catch (Exception e) {
            System.out.println("Evaluate expression: Exception "+e.toString());
            return false;
        }
    }
}
