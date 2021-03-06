/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * GlossaryView.java 1.0, August 30, 2008
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
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.parser.UMVariableLocator;
import java.util.Vector;


/**
 * GlossaryView, Loc Nguyen modified 2009 October
 */
public class GlossaryView implements View {

	final static private String viewType="GlOSSARYVIEW";
	final static private boolean stat=true;

	private String viewName=null;
	private String title=null;
	public String background=null;
	private String mime="text/html";
	private String initParams=null;   //view specific params from view.xml file

	private String courseName=null;

	private SecWndLinks secWndLinks=null;

	private String contextpath=null;
	private String directory = null;

//=============================================================


	//Constructor
	public GlossaryView(String name,String bg,String params,String title,SecWndLinks swls){
		this.viewName=name;
		this.background=bg;
		this.initParams=params;
		this.title=title;
		
		this.secWndLinks=swls;
		this.contextpath=WOWStatic.config.Get("CONTEXTPATH");
	}

	public GlossaryView(){
		this.contextpath=WOWStatic.config.Get("CONTEXTPATH");
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
  
	public void setCourseName(String courseName) {
		this.courseName = courseName;
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
		return GlossaryView.stat;
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
	 * params: contains servlet params for the view in the format(name=value;name=value....)
	 */
	public InputStream genBrsCode(String conceptname ,Profile profile, Map params){
		try {this.courseName = profile.getAttributeValue("personal", "course");} catch (Exception e) {e.printStackTrace();}
		
		if(this.courseName == null) this.courseName = (String)params.get("wow_course");

		StringBuffer outstr = new StringBuffer();

		//Get the view params from the Request URL
		try {
			outstr.append("<html><head>");
			outstr.append("<meta http-equiv='Cache-Control' content='no-cache' />\n<meta http-equiv='Expires' content='-1' />\n<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\" />");
			outstr.append("<title>" + this.title + "</title></head>\n\n");

			//Check mode param and decide what HTML code to produce
			String mode=(String)params.get("mode");
			if(mode=="" || mode==null) mode="TOP";

			if(mode.toUpperCase().equals("TOP")) {
				params.put("concept", conceptname);
				outstr.append(genTOPCode(params));
			}
			if(mode.toUpperCase().equals("CHARS")) outstr.append(genCHARSCode());
			if(mode.toUpperCase().equals("GLOSSARY")) outstr.append(genGLOSSARYCode(params,profile));
			if(mode.toUpperCase().equals("CONCEPTS")) outstr.append(genCONCEPTSCode(params,profile));
			outstr.append("</html>");
			try {
				this.directory = profile.getAttributeValue("personal","directory");
			}
			catch (InvalidAttributeException iae) {System.out.println(iae);}
		}
		catch (Exception e) {
			System.err.println("GlossaryView: "+ e.getMessage());
			e.printStackTrace(System.err);
		}

		//Convert output to inputstream(needed by the servlet)
		InputStream insb = new ByteArrayInputStream(outstr.toString().getBytes());

		return insb;
	}

	/**
	 * Generates HTML code for TOP level of the view(Frames structure)
	 */
	private StringBuffer genTOPCode(Map params){
		StringBuffer s=new StringBuffer();
		String concept=(String)params.get("concept");
		s.append("<FRAMESET ROWS='45%,*' BORDER=0>");
		s.append("<FRAMESET COLS='45%,55%'>");
		s.append("<FRAME NAME='CHARS' SRC='" + this.contextpath + 
				"/ViewGet/" + this.courseName + "/?view=" + this.viewName + "&mode=CHARS' SCROLLING='AUTO'>");
		s.append("<FRAME NAME='GLOSSARY' SRC='" + this.contextpath + 
				"/ViewGet/" + this.courseName + "/?view=" + this.viewName + "&mode=GLOSSARY&concept=" + concept + "' SCROLLING='AUTO'>");
		s.append("</FRAMESET>");
		s.append("<FRAME NAME='CONCEPTS' SRC='" + this.contextpath + 
				"/ViewGet/" + this.courseName + "/?view=" + this.viewName + "&mode=CONCEPTS&concept=" + concept + "' SCROLLING='AUTO'>"); 
		s.append("</FRAMESET>");
		return s;
	}

	/**
	 * Generates HTML code for CHAR frame
	 */
	private StringBuffer genCHARSCode(){
		StringBuffer s=new StringBuffer();
		String[] alphabet={"ALL","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		String bg=WOWStatic.VM().getParam("bgCHAR",this.initParams);
		int t = 0;
		s.append("<body background=\""+this.contextpath+"/"+this.directory+"/"+bg+"\">\n");
		s.append("<ul><ul><h3><pre><b>\n");

		//Generate HTML anchor for each element of alphabet array
		while(t < alphabet.length) {
			s.append("<a href=\"" + this.contextpath + 
					"/ViewGet/" + this.courseName + "/?view=" + this.viewName + "&mode=GLOSSARY&filter=" + alphabet[t] + "\" onMouseOver=\"window.status='Select concepts'; return false;\" target=\"GLOSSARY\">" + alphabet[t] + "</a> ");
			if((t==2) || (((t-2)%4)==0)) s.append("<br>");

			t = t+1;
		}

		s.append("<br></b></pre></ul></ul></h3></body>\n");

		return s;
	}

	/**
	 * Generates HTML code for GLOSSARY Frame
	 */
	private StringBuffer genGLOSSARYCode(Map params,Profile profile) throws DatabaseException {

		StringBuffer s=new StringBuffer();
		Vector conceptList=WOWStatic.DB().getConceptDB().getConceptList();
		String bg=WOWStatic.VM().getParam("bgGLOSSARY",this.initParams);
		String cType=WOWStatic.VM().getParam("cType",this.initParams);
		
		String concept=(String)params.get("concept");
		Vector filteredList = new Vector();
		String filter = null;
		String fparam=(String)params.get("filter");
		if(fparam != null && !fparam.equals("") && !fparam.equals("ALL"))
			filter = fparam;
		else
			filter = "";

		//if concept View-param is not empty than set it's first letter as a filter
		ConceptInfo ci = null;
		if(concept != null && !concept.equals("")){
			ci = WOWStatic.CourseInfoTbl.getCourseInfo(this.courseName).conceptsInfoTbl.getConceptInfo(concept);
			if(ci == null)
				System.out.println("GlossaryView: Concept '"+concept+"' not found in concept table!");
			else
				filter = ci.getTitle().substring(0,1);
		}

		s.append("<body background=\"" + this.contextpath + "/" + this.directory + "/" + bg + "\">\n");

		//Add Glossary title
		s.append("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\"><tr><td><font size=\"4\">Glossary: ");
		if(filter.equals(""))
			s.append("All "+cType+" concepts");
		else
			s.append(filter);

		s.append("</font><br></td></tr></table>");

		//Add all the conceptnames that begin with 'filter' to 'filteredList'
		for( int i = 0; i < conceptList.size(); i++){
			//if required concept
			if(conceptList.get(i).toString().startsWith(this.courseName)){
				
				ConceptInfo cinfo = WOWStatic.CourseInfoTbl.getCourseInfo(this.courseName).conceptsInfoTbl.getConceptInfo(conceptList.get(i).toString());
				String title = cinfo.getTitle();
				if(title == null) continue;
				
				if(title.toUpperCase().startsWith(filter.toUpperCase())){
					if(cType != null && cType.length() > 0) {
						String type=cinfo.getTypeName();
						if(type == null) continue;
						if(type.toLowerCase().equals(cType)){
							filteredList.add((String) conceptList.get(i));
						}
					}
					else
						filteredList.add((String) conceptList.get(i));
				}
			}
		}

		//Sort the filtered list
		TreeSet treeSet=new TreeSet(filteredList);
		Iterator sortedFiltered=treeSet.iterator();

		//Add the filtered/sorted list to the HTML output
		String item=null;
		//s.append("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\"><tr><td width=\"70\">&nbsp;</td><td>");
		s.append("\n<nobr><ul>\n");
		while(sortedFiltered.hasNext()){
			item = sortedFiltered.next().toString();

			HTMLAnchor link = new HTMLAnchor();
			s.append(link.genLinkTo(item,profile)+"<br>\n");
		}

		//s.append("</td></tr></table>");

		s.append("\n</ul></nobr\n>");
		s.append("</body>\n");
		return s;
	}

	/**
	 * Generates HTML code for CONCEPTS frame
	 */
	private StringBuffer genCONCEPTSCode(Map params,Profile profile){

		StringBuffer s=new StringBuffer();
		String bg=WOWStatic.VM().getParam("bgCONCEPTS",this.initParams);
		String conceptName=(String)params.get("concept");
		//added by @Ewald @10-02-2005
		if (conceptName.equals(this.courseName + ".glossary_onthology_WOWSystemDoNotUse")) {
			s.append("<BODY BACKGROUND='"+this.contextpath+"/"+this.directory+"/"+bg+"'>\n");
			s.append("<FONT SIZE=3><UL><UL><p>");
			s.append("<b><h3>Glossary</h3></b><br><br>\n");
			s.append("</UL></UL></BODY>\n");

			return s;
		}
		//end added by @Ewald
		ConceptDependences cd=(ConceptDependences) WOWStatic.CourseInfoTbl.getCourseInfo(this.courseName).dependences.getDependences().get(conceptName);
		ConceptInfo ci=WOWStatic.CourseInfoTbl.getCourseInfo(this.courseName).conceptsInfoTbl.getConceptInfo(conceptName);

		if (cd == null) {
			System.out.println("genCONCEPTSCode: concept '"+/*ci.getTitle()*/conceptName+"' not found in dependences table");

			s.append("<body background=\"" + this.contextpath + "/" + this.directory + "/" + bg + "\">\n");
			s.append("<font size=\"3\"><ul><ul><br/>");
			s.append("<b><h3>" + /*ci.getTitle()*/conceptName + "</h3></b><br/><br/>\n");
			s.append("This concept does not influence any other concepts");
			s.append("</ul></ul></body>\n");

			return s;
		}
		//changed by @Ewald @10-01-2005
		LinkedList out=cd.getOutcomeINs();             // concepts with it as outcome
		LinkedList pre=cd.getPrerequisiteINs();        // concepts it is prerequisite for
		LinkedList pre2=cd.getPrerequisites();         // concepts it has as prerequisite
		LinkedList out2=cd.getOutcomes();              // concepts it has as outcome (concepts where it causes a knowledge update)

		out.remove(conceptName);
		out2.remove(conceptName);
		out2.remove(this.courseName + ".glossary_onthology_WOWSystemDoNotUse");
		//end changed by @Ewald
		String cName=null;

		s.append("<body background=\"" + this.contextpath + "/" + this.directory + "/" + bg + "\">\n");
		s.append("<table cellspacing=\"0\" width=\"100%\"><tr><td width=\"70\">&nbsp;</td><td>\n");
		s.append("<table cellspacing=\"0\" width=\"100%\"><tr><td colspan=\"2\">\n");
		s.append("<b><h3>" + ci.getTitle() + "</h3></b><br/>\n");
		//Show resource if there is one
		// Locate and process the resource
		Resource resource = null;
		try {
			ConceptDB cdb = WOWStatic.DB().getConceptDB();
			Concept concept = cdb.getConcept(cdb.findConcept(ci.getConceptName()));
			UMVariableLocator umvl = new UMVariableLocator(profile, cdb);
			String URI = concept.getActiveURL(umvl).toString();
			resource = WOWStatic.HM().locateResource(URI);
			WOWStatic.PM().accessedConcept(profile, concept.getName());
            String mime = resource.getType().getMime();
            
            //Changed by Loc Nguyen October 09 2009
            String baseID = (String)params.get("wow_path"); //E.g: http://localhost:8080/wow
            if(baseID != null && baseID.charAt(baseID.length() - 1) != '/') baseID = baseID + "/";
            resource.setBaseID(baseID); 
            
            resource.setConceptName(ci.getConceptName());
            if(!mime.startsWith("image")) {
            	resource = WOWStatic.HM().processComplete(resource, profile);
            }
		}
		catch (Exception e) {e.printStackTrace();}

		//Show processed resource on the screen(first convert from inputstream to string)
		//Send the output to the client

        //Changed by Loc Nguyen October 09 2009
		if((resource != null) && (!resource.equals(""))) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
				String line = null;
				while( (line = reader.readLine()) != null ) {
					s.append(line);
				}
			}
			catch (IOException e) {System.out.println("glossary: error output: " + e.getClass().getName() + ": " + e.getMessage());}
		}
		
		//changed by @Ewald @10-02-2005
		s.append("<br/>\n</td></tr>\n");
		s.append("<tr><td  width=\"50%\" valign=\"top\"> \n");

		//Show outprerequisite list
		//Sort the outprerequisite list
		TreeSet treeSet=new TreeSet(pre2);
		Iterator iterator=treeSet.iterator();

		if(iterator.hasNext()){
			s.append("<b>For this concept, knowledge is required about:</b>\n<ul>\n");
			//t=0;
			while(iterator.hasNext()){
				cName=(String)iterator.next();
				HTMLAnchor link=new HTMLAnchor();
				s.append(link.genLinkTo(cName,profile)+"<br/>\n");
			}
			s.append("</ul>\n");
		}

		s.append("&nbsp;</td><td width=\"50%\" valign=\"top\">\n");

		//Show prerequisite list
		//Sort the prerequisite list
		treeSet=new TreeSet(out2);
		iterator=treeSet.iterator();

		if(iterator.hasNext()){
			s.append("<b>This concepts adds knowledge to:</b>\n<ul>\n");
			while(iterator.hasNext()) {
				cName=(String)iterator.next();
				HTMLAnchor link=new HTMLAnchor();
				s.append(link.genLinkTo(cName,profile)+"<br/>\n");
			}
			s.append("</ul>\n");
		}

		s.append("&nbsp;</td></tr>\n");
		s.append("<tr><td valign=\"top\">\n");

		//Show prerequisites
		//Sort the prerequisites list
		treeSet=new TreeSet(pre);
		iterator=treeSet.iterator();

		if(iterator.hasNext()){
			s.append("<b>Knowledge about this concept is required for:</b>\n<ul>\n");
			while(iterator.hasNext()){
				cName=(String)iterator.next();
				HTMLAnchor link=new HTMLAnchor();
				s.append(link.genLinkTo(cName,profile)+"<br/>\n");
			}
			s.append("</ul>\n");
		}

		s.append("&nbsp;</td><td valign=\"top\">\n");
		//Show outcomes
		//Sort the outcomes list
		treeSet=new TreeSet(out);
		iterator=treeSet.iterator();

		if(iterator.hasNext()) {
			s.append("<b>This concept is introduced on these pages: </b>\n<ul>\n");
			while(iterator.hasNext()){
				cName=(String)iterator.next();//(String) out.get(t);
				HTMLAnchor link=new HTMLAnchor();
				s.append(link.genLinkTo(cName,profile)+"<br/>\n");
			}
			s.append("</ul>\n");
		}

		s.append("&nbsp;</td></tr>\n");
		s.append("</table></td></tr></table>\n");
		//end changed by @Ewald
		s.append("</body>\n");
		return s;
	}

}
