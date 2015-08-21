package vn.spring.zebra.helperservice;

import vn.spring.WOW.Layout.StaticTreeView;
import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.datacomponents.*;


import vn.spring.WOW.engine.Annotation;
import vn.spring.WOW.engine.IconAnnotation;
import vn.spring.WOW.engine.LinkAnnotation;
import vn.spring.WOW.parser.UMVariableLocator;
import vn.spring.zebra.Initialization;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.client.LearningStyle;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.server.TriUMServer;
import vn.spring.zebra.util.CommonUtil;

import java.net.*;
import java.util.*;
import java.io.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class AccessService {
	public static final String JAVASCRIPT_GETCONCEPT = "javascript:getConcept";
	
	public static final String WOW_USERID = "wow_userid";
	public static final String WOW_PASSWORD = "wow_password";
	public static final String WOW_SESSIONID = "wow_sessionid";
	public static final String WOW_COURSE = "wow_course";
	public static final String WOW_DIRECTORY = "wow_directory";
	public static final String WOW_CONCEPT = "wow_concept";
	
	public static final String TREEVIEW = "TREEVIEW";
	public static final String RECOMMENDVIEW = "RECOMMENDVIEW";
	public static final String LEARNINGPATHVIEW = "LEARNINGPATHVIEW";
	public static final String MAINVIEW = "MAINVIEW";
	public static final String LSVIEW = "LSVIEW";
	public static final String COLLABORATIVEVIEW = "COLLABORATIVEVIEW";
	
	protected URL         baseURL = null; //Example: http://www.spring.vn:8080/wow
	protected A_HREF      a_href = null;

	protected String  wow_userid = null;
	protected String  wow_sessionid = null;
	protected String  wow_course = null;
	protected String  wow_directory = null;
	
	private TriUMQuery triUMQuery = null;
	private boolean isLogin = false;
	private Profile profile = null;
	
	public String getContextPath() { //E.g: /wow
        String path = baseURL.getPath().substring(1, this.baseURL.getPath().length());
        int index = path.indexOf("/");
        String cpath = null;
        if(index == -1)
        	cpath = "/" + path;
        else
        	cpath = "/" + path.substring(0, index);
        return cpath;
	}
	public String getNormalizeURL() {//E.g: http://localhost:8080/wow
		return baseURL.getProtocol() + "://" + baseURL.getHost() + ":" + baseURL.getPort() + getContextPath();
	}
	public String getGetPath() {return getContextPath() + "/Get/" + wow_directory;} //E.g: /wow/Get/tutorial
	public void setA_HREF(A_HREF a_href) {this.a_href = a_href;}
	
	public AccessService(URL baseURL, String  sessionid, String  wow_userid, String password, String  wow_course, String wow_directory) throws ZebraException, Exception {
		wow_course = wow_course.trim(); wow_directory = wow_directory.trim();
		if (baseURL == null) throw new ZebraException("No WOW URL");
		if (sessionid == null) throw new ZebraException("No Seesion ID");
		if (wow_userid == null) throw new ZebraException("No User ID");
		if (wow_course == null) throw new ZebraException("No course");
		if (wow_directory == null) throw new ZebraException("No Directory");
		this.baseURL = baseURL;
		this.wow_sessionid = sessionid;
		this.wow_userid = wow_userid.trim(); 
		this.wow_course = wow_course;
		this.wow_directory = wow_directory;

		if(!(ZebraStatic.getWowContextPath().equals(getContextPath()))) throw new ZebraException("Invalid WOW base url");
		
        ProfileDB pdb = ZebraStatic.getProfileDB();
        long id = pdb.findProfile(wow_userid);
        if (id == 0) throw new ZebraException("Profile not found.");
        
        this.profile = pdb.getProfile(id);
        if( (password != null) && (!profile.getAttributeValue("personal", "password").equals(password)) )
        	throw new ZebraException("Wrong Password, please go back and try again.");
        if (!wow_course.equals(profile.getAttributeValue("personal","course"))) {
            profile.setAttributeValue("personal","course", wow_course);
            if (!wow_directory.equals(profile.getAttributeValue("personal","directory")))
                profile.setAttributeValue("personal","directory", wow_directory);
            pdb.setProfile(profile.id, profile);
        	
        }
        checkProfile(profile, this.wow_course);
        
        Initialization.initWOWandZebra(this.wow_course, this.wow_directory, false);
        triUMQuery = (TriUMQuery) (TriUMServer.getInstance().getCommunicateService().getQueryDelegator());
		isLogin = true;
	}
	
    private void checkProfile(Profile profile, String course) throws Exception {
        ConceptDB cdb = ZebraStatic.getConceptDB();
        boolean isUpdate = false;
        
        //remove session stable values from the profile
        Hashtable values = profile.getValues();
        for (Enumeration e = values.keys();e.hasMoreElements();) {
        	String key = (String)e.nextElement();
        	if (key.startsWith(course + ".")) {
        		int p = key.indexOf(".*$$*");
        		if (p > 0) {
        			//page stability attribute
        			Concept concept = null;
        			String conceptname = key.substring(0,p);
        			try {
        				concept = cdb.getConcept(cdb.findConcept(conceptname));
        			}
        			catch(Exception ex) {
        				System.out.println("ConceptDB.getConcept causes error: " + ex.getMessage());
        			}
        			if (concept != null) if (concept.isSessionStable()) {
        				values.remove(key);
        				isUpdate = true;
        			}
        		}
        		else {
        			//not a page stability attribute
        			String conceptname = key.substring(0, key.lastIndexOf("."));
        			Concept concept = null;
        			try {
        				concept = cdb.getConcept(cdb.findConcept(conceptname));
        			}
        			catch(Exception ex) {
        				System.out.println("ConceptDB.getConcept causes error: " + ex.getMessage());
        			}
        			
        			if (concept != null) {
        				Attribute attr = null;
        				try {
        					attr = concept.getAttribute(key.substring(key.lastIndexOf(".")+1,key.length()));
        				}
        				catch (Exception ex) {
            				System.out.println("Concept.getAttribute causes error: " + ex.getMessage());
            			}
        				
        				if (attr != null) {
        					if (attr.isSessionStable()) {
        						values.remove(key);
                				isUpdate = true;
        					}
        				}
        				else {
        					values.remove(key);
            				isUpdate = true;
        				}
        			}
        		}//end if p > 0
        	}//end if key.startsWith(course + ".")
        }//end for
        
        if(isUpdate) ZebraStatic.getProfileDB().setProfile(profile.id, profile);
    }

	@SuppressWarnings("deprecation")
	public void accessGetConcept(String briefConceptName, HashMap<String, String> out) throws Exception, ZebraException, SAXException, IOException {
		if (!isLogin) throw new ZebraException("Not login yet");
		out.clear();
		String conceptName = wow_course + "." + briefConceptName;
		
		ConceptDB         cdb = ZebraStatic.getConceptDB();
        long              cnr=cdb.findConcept(conceptName);
        Concept           concept = ZebraStatic.getConceptDB().getConcept(cnr);
        UMVariableLocator umvl = new UMVariableLocator(profile, cdb);
        URL               conceptURL = concept.getActiveURL(umvl);

        ZebraStatic.getProfileManager().accessedConcept(profile, conceptName);
        //do logging
        if (ZebraStatic.useLogging(profile)) {
            if (conceptURL != null)
            	ZebraStatic.getLogDB().addAccessLog(conceptURL.toString(), profile.getAttributeValue("personal", "id"), wow_sessionid, false);
        }
        
        //TREEVIEW
        {
	        StaticTreeView treeView = new StaticTreeView();
	        InputStream treeStream = treeView.genBrsCode(conceptName, profile, new HashMap<Object, Object>());
	        if(treeStream != null) {
		        out.put(TREEVIEW, createDocumentXML(this, treeStream) );
	        }
        }
        
        //RECOMMENDVIEW
        {
        	String rView = "<div>";
        	try {
        		
        		rView += "<strong><font color=\"#000066\">Should be read before:</font></strong><br/>";
        		ArrayList<String> preNames = triUMQuery.recommendedPreConceptQuery(wow_userid, wow_course, briefConceptName);
        		rView += serializeXHTMLRecommendConcept(preNames);
        		
        		rView += "<strong><font color=\"#000066\">Should be read after:</font></strong><br/>";
        		ArrayList<String> postNames = triUMQuery.recommendedPostConceptQuery(wow_userid, wow_course, briefConceptName);;
        		rView += serializeXHTMLRecommendConcept(postNames);

        	}
        	catch(Exception e) {
        		System.out.println("Running course mining causes error: " + e.getMessage());
        	}
        	rView += "</div>";
        	InputStream recommendStream = new StringBufferInputStream(rView);
        	out.put(RECOMMENDVIEW, createDocumentXML(this, recommendStream));
        	recommendStream.close();
        }
        
        //LEARNINGPATH VIEW
        {
        	String pathView = "<div>";
        	try {
        		pathView += "<strong><font color=\"#FF0000\" size=\"+1\">" +
        					serializeXHTMLLearningPath(triUMQuery.learningPathQuery(wow_userid, wow_course)) +
        					"</font></strong>";
        	}
        	catch(Exception e) {
        		System.out.println("Running course sequential pattern mining causes error: " + e.getMessage());
        	}
        	pathView += "</div>";
        	InputStream pathStream = new StringBufferInputStream(pathView);
        	out.put(LEARNINGPATHVIEW, createDocumentXML(this, pathStream));
        	pathStream.close();
        }
        
        //MAIN VIEW
        {
	        Resource resource = ZebraStatic.getHandlerManager().locateResource(conceptURL.toString());
	        if(resource != null) {
		        resource.setCallerID("Get");
		        String baseID = baseURL.toString(); if(baseID.charAt(baseID.length() - 1) != '/') baseID += '/';
		        resource.setBaseID(baseID);
		        resource.setConceptName(conceptName);
		        
		        if (! resource.getType().getMime().startsWith("image")) {
		        	try {
		        		resource = ZebraStatic.getHandlerManager().processComplete(resource, profile);
		        		if (resource == null) throw new ZebraException("The resource is null");
		        	}
		        	catch(Exception ex) {System.out.println(ex.getMessage());}
		        }
		
		        out.put( MAINVIEW, createDocumentXML(this, resource.getInputStream()) );
	        }
        }

        //LSVIEW
        {
        	String lsView = "<div>";
        	try {
        		LearningStyle ls = triUMQuery.learningStyleQuery(wow_userid, wow_course);
        		
        		lsView += "<strong><font color=\"#00FF00\" size=\"+1\">";
        		if(ls.isVerbalizer) lsView += "Verbal, ";
        		else                      lsView += "Visual, ";
        		
        		if(ls.isActivist)   lsView += "Active, ";
        		else                lsView += "Reflective, ";
        		
        		if(ls.isTheorist)   lsView += "Theorist";
        		else                lsView += "Prgamatist";
        		
        		lsView += "</font></strong>";
        		
        	}
        	catch(Exception e) {
        		System.out.println("Running learning style inference by Hidden Markov Model causes error: " + e.getMessage());
        	}
        	lsView += "</div>";
        	out.put(LSVIEW, lsView);
        }
        
        //COLLABORATIVEVIEW
        {
        	String cView = "<div>";
        	try {
            	cView += "You should collaborate with following persons in your community:<br/>";
        		
        		ArrayList<String> userids = triUMQuery.communityQuery(wow_userid, wow_course);
    			for(int i = 0; i < userids.size(); i++) {
    				String userid = userids.get(i);
    				String a_href = 
    					"<a href=\"" + getNormalizeURL() + "/Interact?userid=" + userid + "\">" +
    						userid +
    					"</a>";
    				cView += a_href;
    				if( i < userids.size() - 1) cView += ", ";
    			}
	        	//cView +=
	        	//	"<br/><em>For example:</em><br/>Young pig <strong>Dung</strong> (puzzled): What is the meaning of <strong>AEHS</strong>?<br/>" +
				//	"Old goal <strong>Loc</strong> (smile): <strong>AEHS</strong> has no meaning!";
	        	cView += "<br/>" +
				"<applet" + "\n" +
					"codebase = \"" + getNormalizeURL() + "/lib\"" + "\n" +
					"code     = \"vn.spring.zebra.collab.CollabApplet.class\"" + "\n" +
					"name     = \"Collaborative\"" + "\n" +
					"width    = \"300\"" + "\n" +
					"height   = \"100\"" + "\n" +
					"hspace   = \"0\"" + "\n" +
					"vspace   = \"0\"" + "\n" +
					"align    = \"middle\"" + "\n" +
					"archive  = \"wow.jar, jgraph.jar, xerces.jar, xml.jar\">" + "\n" +
				"<param name=\"host\" value=\"" + baseURL.getHost() + "\" />" + "\n" +
				"<param name=\"port\" value=\"" + TriUMQuery.COLLAB_SERVICE_PORT + "\" />" + "\n" +
				"<param name=\"userid\" value=\"" + wow_userid + "\" />" + "\n" +
				"<param name=\"group\" value=\"" + CommonUtil.concatNames(userids, ",") + "\" />" + "\n" +
	        	"</applet>";
        	}
        	catch(Exception e) {System.out.println("Clusterer causes error: " + e.getMessage()); e.printStackTrace();}
        	cView += "</div>";
        	out.put(COLLABORATIVEVIEW, cView);
        }

	}
	
	public static String createPage(String wowPath, HashMap<String, String> results) {
		String treeview = results.get(AccessService.TREEVIEW);
		String recommendview = results.get(AccessService.RECOMMENDVIEW);
		String learningpathview = results.get(AccessService.LEARNINGPATHVIEW);
		String mainview = results.get(AccessService.MAINVIEW);
		String lsview = results.get(AccessService.LSVIEW);
		String collaborativeview = results.get(AccessService.COLLABORATIVEVIEW);
		
		treeview = (treeview == null ? "" : treeview);
		recommendview = (recommendview == null ? "" : recommendview);
		collaborativeview = (collaborativeview == null ? "" : collaborativeview);
		mainview = (mainview == null ? "" : mainview);
		
		String coverImagePath = wowPath + "/images/post_left_ctr.gif";
		String content_xhtml =
			"<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" +
			"<tr>" +
		    	"<td align=\"left\" valign=\"top\" width=\"200px\">" +
					"<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" +
					"<tr><td>" + treeview + "</td></tr>" +
					"<tr><td bgcolor=\"#FFFF99\"><br/><strong><font color=\"#000066\" size=\"+1\">Recommendation:</font></strong><br/>" + 
						recommendview + "</td></tr>" +
					"</table>" +
		    	"</td>" +
				"<td align=\"left\" valign=\"top\" with=\"40px\" background=\"" + coverImagePath + "\">" + 
					"<img with=\"40px\" src=\"" + coverImagePath+ "\"/>" + 
				"</td>" +
				"<td>&nbsp;&nbsp;</td>" +
		    	"<td align=\"left\" valign=\"top\">" + 
					"<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" +
					"<tr><td bgcolor=\"#FFFF99\"><br/><strong><font color=\"#000066\" size=\"+1\">Learning Path:</font></strong>" + 
						learningpathview + "</td></tr>" +
					"<tr><td>" + mainview + "</td></tr>" +
					"<tr><td bgcolor=\"#FFCCFF\"><br/><strong><font color=\"#FF0000\" size=\"+1\">Your Learning Styles:</font></strong>" + 
						lsview + "</td></tr>" +
					"<tr><td bgcolor=\"#FFFF99\"><br/><strong><font color=\"#000066\" size=\"+1\">Collaborative Area:</font></strong><br/>" + 
						collaborativeview + "</td></tr>" +
					"</table>" +
			    "</td>" +
		    "</tr>" +
		    "</table>";

		String hearder_xhtml = 
			"<table cellspacing=\"0\" cellpadding=\"0\">" +
			"<tr>" +
    			"<td>" + 
    				"<img src=\"" + wowPath + "/images/wowbanner-smaller.jpg\"/>" + 
    			"</td>" +
    		"</tr>" +
		    "</table>";
		
		String footer_xhtml = 
			"<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" +
			"<tr>" +
    			"<td></td>" +
    		"</tr>" +
		    "</table>";
		
		String page_xhtml = 
			"<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" +
			"<tr>" +
				"<td align=\"center\" valign=\"middle\" border=\"0\">" + hearder_xhtml + "</td>" +
			"</tr>" + "<tr><td><p>&nbsp;</p></td></tr>" +
			"<tr>" +
				"<td align=\"center\" valign=\"middle\" border=\"0\">" + content_xhtml + "</td>" +
			"</tr>" + "<tr><td><p>&nbsp;</p></td></tr>" +
			"<tr>" +
				"<td align=\"center\" valign=\"middle\" border=\"0\">" + footer_xhtml + "</td>" +
			"</tr>" +
		    "</table>";
		return page_xhtml;
	}

	public static String createDocumentXML(AccessService login, InputStream input) throws SAXException, IOException {
		Document doc = createDocument(login, input);
		return serializeDiv(login, doc.getDocumentElement());
	}

	public static Document createDocument(AccessService login, InputStream input) throws SAXException, IOException {
		Document doc = CommonUtil.createDOMParser(input);
		traverse(login, doc.getDocumentElement());
		return doc;
	}
	
    private static void traverse(AccessService login, Element element) {
        if (element == null) return;

        //process several nodes
        String tag = element.getTagName().toLowerCase();
        Node node = element;
        if (tag.equals("a")) node = traverse_a(login, element);
        if (tag.equals("span")) node = traverse_span(login, element);
        
        if(node != null)
        	traverseChildren(login, node);
        else if(element.getParentNode() != null)
        	element.getParentNode().removeChild(element);
    }
    private static void traverseChildren(AccessService login, Node startnode) {
        if (startnode == null) return;
        Node nextnode = startnode.getFirstChild();
        while (nextnode != null) {
            Node node = nextnode;
            nextnode = node.getNextSibling();
            if (node instanceof Element) traverse(login, (Element)node);
        }
        startnode.normalize();
    }
    private static Element traverse_a(AccessService login, Element element) {
        String href = element.getAttribute("href");
        
        String get_regex = login.getGetPath() + "/?concept=";
        if(href.indexOf(get_regex) != -1) {
        	String cname = href.substring(get_regex.length(), href.length());
        	DotString dstring = new DotString(cname, ".");
        	String briefConceptname = ( dstring.size() > 1?dstring.get(1):dstring.get(0) );
        	
        	if(login.a_href == null) {
				String replace_url = JAVASCRIPT_GETCONCEPT + "(" + 
				"'" + login.getNormalizeURL() + "', " +
				"'" + login.wow_userid + "'" + ", " + 
				"'" + login.wow_sessionid + "', " +
				"'" + login.wow_directory + "', " +
				"'" + login.wow_course + "', " +
				"'" + briefConceptname + "'" +
				")\"";
				href = replace_url;
        	}
        	else {
        		login.a_href.setSessionAttribute(WOW_USERID, login.wow_userid);
        		login.a_href.setSessionAttribute(WOW_SESSIONID, login.wow_sessionid);
        		login.a_href.setSessionAttribute(WOW_DIRECTORY, login.wow_directory);
        		login.a_href.setSessionAttribute(WOW_COURSE, login.wow_course);
        		login.a_href.setSessionAttribute(WOW_CONCEPT, briefConceptname);
        		login.a_href.setParameter(WOW_USERID, login.wow_userid);
        		login.a_href.setParameter(WOW_SESSIONID, login.wow_sessionid);
        		login.a_href.setParameter(WOW_DIRECTORY, login.wow_directory);
        		login.a_href.setParameter(WOW_COURSE, login.wow_course);
        		login.a_href.setParameter(WOW_CONCEPT, briefConceptname);
        		href = login.a_href.toString();
        	}
        }
        element.setAttribute("href", href);
        element.removeAttribute("target");
        return element;
    }
    private static Element traverse_span(AccessService login, Element element) {
    	try {
	    	String xml = CommonUtil.serializeXHTML(element);
	    	if(xml.indexOf("TOC") != -1 && xml.indexOf("Glossary") != -1 && xml.indexOf("Log off") != -1) {
	    		return null;
	    	}
	    	if(xml.indexOf("list of read pages") != -1 && xml.indexOf("pages still to be read") != -1)
	    		return null;
	    	return element;
    	}
    	catch(Exception e) {return null;}
    }
    
    private static String serializeDiv(AccessService login, Element element) throws IOException {
    	String div = CommonUtil.serializeXHTML(element);
        int begin = 0, end = 0;
        begin = div.indexOf("<html>");
        if(begin != -1) {
        	end = div.indexOf("</html>");
        	end = (end == -1 ? div.length() : end);
            div = div.substring( begin +  "<html>".length(), end);
        }
        
        begin = div.indexOf("<body>");
        if(begin != -1) {
        	end = div.indexOf("</body>");
        	end = (end == -1 ? div.length() : end);
            div = div.substring( begin +  "<body>".length(), end);
        }

        String link = "<link href=\"" + ZebraStatic.getCourseInfoTbl().getCourseInfo(login.wow_course).stylesheet + 
        	"\" rel=\"stylesheet\" type=\"text/css\" />";        
        div = "<div>" + link + div + "</div>"; 
        return div;
    }
	private String serializeXHTMLRecommendConcept(ArrayList<String> rNames) {
		String rView = "";
		int r = rNames.size();
		for(int i = 0; i < r; i++) {
			String img = "";
			for(int j = 0; j < ZebraStatic.RECOMMEND_MAX_CONCEPT - i; j++) {
				img += " <img src=\"" + getNormalizeURL() + "/images/recommend.jpg\"/>";
			}
			rView += createLinkAdaptation(rNames.get(i)) +  img;
			rView += "<br/>";
		}
		return rView;
	}
	protected String serializeXHTMLLearningPath(ArrayList<ArrayList<String>> path) {
		String pathView = "";
		int m = path.size();
		for(int i = 0; i < m; i++) {
			ArrayList<String> concepts = path.get(i);
			int n = concepts.size();
			if(n == 1) {
				pathView += createLinkAdaptationNoIcon(concepts.get(0));
			}
			else if(n > 1) {
				String s = "";
				for(int j = 0; j < n; j++) {
					s += createLinkAdaptationNoIcon(concepts.get(j));
					if(j < n - 1) s += ",";
				}
				pathView += s;
			}
			if(i < m -1) pathView += " -> ";
		}
		return pathView;
	}
	private String createLinkAdaptation(String concept) {
        Annotation anno = ZebraStatic.getCourseInfoTbl().getCourseInfo(wow_course).CT.getConceptType("page").getAnnotation();
        LinkAnnotation linkanno = anno.getLinkAnno();
        String _class = linkanno.getLinkAnnotation(concept, profile, "STATICTREEVIEW");
        String href = getGetPath() + "/?concept=" + concept;
        
        //Icon annotation
        IconAnnotation iconanno = anno.getIconAnno();
        LinkedList<?> icons = iconanno.getIconAnnotation(concept, profile, "STATICTREEVIEW");
        
        String img = "";
        for (int i = 0; i < icons.size(); i++) {
            String icon = (String)icons.get(i);
            if (icon.startsWith("<front>")) {icon = icon.substring(7);}
            img += "<img src=\"" +
            	getNormalizeURL() +  "/" + wow_directory + "/" + icon + "\" " +
            	"align=\"bottom\" alt=\"\"/>";
        }
        DotString dstr = new DotString(concept, ".");
        String brief = ( dstr.size() > 1 ? dstr.get(1) : concept);
        return img + " <a class=\"" + _class + "\" href=\"" + href + "\">" + brief + "</a>";
	}
	private String createLinkAdaptationNoIcon(String concept) {
        Annotation anno = ZebraStatic.getCourseInfoTbl().getCourseInfo(wow_course).CT.getConceptType("page").getAnnotation();
        LinkAnnotation linkanno = anno.getLinkAnno();
        String _class = linkanno.getLinkAnnotation(concept, profile, "STATICTREEVIEW");
        String href = getGetPath() + "/?concept=" + concept;
        
        DotString dstr = new DotString(concept, ".");
        String brief = ( dstr.size() > 1 ? dstr.get(1) : concept);
        return " <a class=\"" + _class + "\" href=\"" + href + "\">" + brief + "</a>";
	}
    
	public static void main(String[] args) {
	}

}
