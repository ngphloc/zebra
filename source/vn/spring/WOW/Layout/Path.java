/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is free software


*/
/**
 * Path.java 1.0, January 10, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science
 */

package vn.spring.WOW.Layout;

import java.io.*;
import java.util.Map;
import vn.spring.WOW.engine.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.parser.*;


/**
 * Table Of Contest View
 */
public class Path extends AbstractView {
	private String initParams=null;   //view specific params from view.xml file

	//--------------------------------
	// Vars for local use
	private Profile profile=null;
	
	//=============================================================
	public Path(){
	}

	/**
	 * returns the viewtype of this view(PATH)
	 */
	public String getViewType() {return "PATH";}

	/**
	 * generates the the Browers code(HTML/XHTML) for this view
	 */
	public InputStream genBrsCode(String conceptname, Profile profile, Map params) {
		this.profile = profile;
		//Construct HTML code for the menu output file
		StringBuffer outstr = new StringBuffer();

		try {
			ObjectFilter of = new ObjectFilter(profile);
			outstr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"DTD/xhtml1-strict.dtd\">\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>");
			outstr.append("<style type=\"text/css\">\na:link { color: #");
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
		outstr.append("\")}\n</style>\n</head>\n<body>\n");
		//Move everything a litle bit to the right
		outstr.append("<table cellspacing=0><tr><td width='"+WOWStatic.VM().getParam("leftspace",this.initParams)+"'>&nbsp;</td><td>\n\n");

		//if there is a hierarchy then show it
		try {
			String course = profile.getAttributeValue("personal", "course");
			if(WOWStatic.CourseInfoTbl.getCourseInfo(course).hierarchy.getRootNode()!=null){
				//generate HTML code for each Node in the Hierarchy starting with first child of the rootNode
				genNodesCode(WOWStatic.CourseInfoTbl.getCourseInfo(course).hierarchy.getRootNode().getFirstChild(),outstr,0);
            }
            else {
            	System.err.println("TOC View Error: No Hierarchy present->No TOC view code!!!");
            }
		}
		catch(InvalidAttributeException e) {}

		outstr.append("\n</td></tr></table>\n");
		outstr.append("</body></html>");

		//Convert output to inputstream(needed by the servlet)
		InputStream insb = new ByteArrayInputStream(outstr.toString().getBytes());

		return insb;
	}

    /**
   * Generates recursively HTML menu code for each node in the Hierarchy
   * width can have two values:0 pixels or 10 pixels)
   */
   private void genNodesCode(TNode curNode, StringBuffer outstr, int width){
     String name=curNode.getName();
     HTMLAnchor link=new HTMLAnchor();

     String coursename = null;
     try{
         coursename=profile.getAttributeValue("personal","course");
     }catch(InvalidAttributeException e){System.out.println("genLinkTo Error:Course attribute not found in the profile");}

     ConceptInfo conceptinfo=WOWStatic.CourseInfoTbl.getCourseInfo(coursename).conceptsInfoTbl.getConceptInfo(name);
     String type=conceptinfo.getTypeName();
     if(curNode!=null){
       if (curNode.getFirstChild()!=null) {
         //Node has children

         if (isAllowedConcept(name)) {
             if (type.equals("page")) outstr.append(HTMLrep(link.genLinkTo(curNode.getName(),this.profile), width+10, false));
         }

         //Add code for the children
         genNodesCode(curNode.getFirstChild(),outstr,width+30);
       }

       else{
         //Node has NO children

         if (isAllowedConcept(name)) {
             if (type.equals("page")) outstr.append(HTMLrep(link.genLinkTo(curNode.getName(),this.profile), width+10, false));
         }
       }


       //Add the code for the siblings
       if (curNode.getNextSib()!=null) {genNodesCode(curNode.getNextSib(),outstr,width);}

     }
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

}
