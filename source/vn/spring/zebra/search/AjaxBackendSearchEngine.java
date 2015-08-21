/**
 * 
 */
package vn.spring.zebra.search;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import vn.spring.WOW.Layout.HTMLAnchor2;
import vn.spring.WOW.WOWDB.ConceptDB;
import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.Concept;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.PNode;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.server.TriUMServer;
import vn.spring.zebra.util.CommonUtil;
import vn.spring.zebra.util.ConceptUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class AjaxBackendSearchEngine extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String ZEBRA_SEARCH_NAMESPACE = "vn$spring$zebra$helperservice$AjaxBackendSearchEngine";
	private final static int MAX_RESULTS = 1000;
	public final static int HITS_PER_PAGE = 50;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String userid = request.getParameter("userid");
		String course = request.getParameter("course");
		String directory = request.getParameter("directory");
        String queryString = request.getParameter("query");           //get the search criteria
        String startatStr    = request.getParameter("startat");         //get the start index, string version of startindex
        String hitsperpageStr  = request.getParameter("hitsperpage");      //get max results per page, string version of maxpage
        String firsttime = request.getParameter("firsttime"); //if true: user press submit for searching in the first time

        //
        Profile   profile = null;
        try {
            ProfileDB pdb = ZebraStatic.getProfileDB();
        	profile = pdb.getProfile(pdb.findProfile(userid));
        }
        catch(Throwable e) {e.printStackTrace();}
        //
		PrintWriter out=response.getWriter();
	    response.setContentType("text/html;charset=UTF-8");
		
	    String submit = "javascript: " + ZEBRA_SEARCH_NAMESPACE + "_submit()";
		out.println( 
	    	"<center>" + "<h3>WOW! Search Engine</h3>" +
	    	"<form method= \"POST\" id=\"" + ZEBRA_SEARCH_NAMESPACE + "_searchform\" onsubmit=\"" + submit + ";return false;\">" +
	    		"<p>" +
	    			"<input name=\"query\" size=\"44\" value=\"" + (queryString==null?"":queryString) + "\"/> Enter keywords" +
	    		"</p>" +
	    		"<p>" +
    			    "<input type=\"hidden\" name=\"userid\" value=\"" + (userid==null?"":userid) + "\"/>" +
	    			"<input type=\"hidden\" name=\"course\" value=\"" + (course==null?"":course) + "\"/>" +
	    			"<input type=\"hidden\" name=\"directory\" value=\"" + (directory==null?"":directory) + "\"/>" +
	    			"<input type=\"hidden\" name=\"startat\" value=\"" + (startatStr==null?"":startatStr) + "\"/>" +
	    			"<input type=\"hidden\" name=\"hitsperpage\" size=\"4\" value=\"" + (hitsperpageStr==null?String.valueOf(HITS_PER_PAGE):hitsperpageStr) + "\"/>" +
	    			"<input type=\"button\" value=\"Search\" onclick=\"" + submit + "\"/> (" + HITS_PER_PAGE + " results per page)" +
	    		"</p>" +
	            "</form>" +
	        "</center>");
	    if(queryString == null || queryString.length() == 0) return;

	    boolean error = false;                                        //used to control flow for error messages
        String indexLocation = ZebraStatic.getWowXmlRoot() + ZebraStatic.SEARCH_INDEX_DIR + course; //Index Location
        IndexSearcher searcher = null;   //the searcher used to open/search the index
        Query query = null;              //the Query created by the QueryParser
        TopDocs hits = null;             //the search results
        int startat = 0;                 //the first index displayed on this page
        int hitsperpage = HITS_PER_PAGE; //the maximum items displayed on this page
        int thispage = 0;                //used for the for/next either maxpage or
                                         //hits.totalHits - startindex - whichever is less
                                         //this page make the anchor to search, it is the last item but not visible in one search
        try {
          IndexReader reader = IndexReader.open(FSDirectory.open(new File(indexLocation)), true); // only searching, so read-only=true
          searcher = new IndexSearcher(reader);
        }
        catch (Exception e) {
			out.println("<p>ERROR opening the Index - contact sysadmin!</p>");
			out.println("<p>Error message:" +  e.getMessage() + "</p>");
			error = true;
        }
        
        if (error == false) {
            try {
            	hitsperpage    = Integer.parseInt(hitsperpageStr);    //parse the max results first
            	startat = Integer.parseInt(startatStr);      //then the start index  
            } catch (Exception e) { } //we don't care if something happens we'll just start at 0
                                      //or end at 50
            if (queryString == null) throw new ServletException("no query");
            
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
            try {
            	QueryParser qp = new QueryParser("contents", analyzer);
            	query = qp.parse(queryString); //parse the 
            }
            catch (ParseException e) {
    			out.println("<p>Error while parsing query: " + e.getMessage() + "</p>");
    			error = true;
            }
        }
        
        if (error == false && searcher != null) { 
        	thispage = hitsperpage; // default last element to hitsperpage
        	hits = searcher.search(query, MAX_RESULTS); // run the query 
        	
        	if (hits.totalHits == 0) { // if we got no results tell the user
        		out.println("<p>No result</p>");
        		error = true; // don't bother with the rest of the page
        	}
        }
        
        if (error == false && searcher != null) {
        	if ((startat + hitsperpage) > hits.totalHits)
        		thispage = hits.totalHits - startat; // set the max index to hitsperpage or last actual search result whichever is less
        	
        	String previous = "", next = "";
        	if(startat > 0) {
        		int newStartAt = startat - hitsperpage;
        		if(newStartAt < 0) newStartAt = 0;
        		previous =
        			"<a href=\"javascript:" + 
        			callSearch(userid, course, directory, queryString/*URLEncoder.encode(queryString)*/, newStartAt, hitsperpage) +
        			"\">Previous</a>";
        	}
        	if(startat + hitsperpage < hits.totalHits) {
        		int newStartAt = startat + hitsperpage;
        		next =
        			"<a href=\"javascript:" + 
        			callSearch(userid, course, directory, queryString/*URLEncoder.encode(queryString)*/, newStartAt, hitsperpage) +
        			"\">Next</a>";
        	}

        	out.println("<p>" + (startat + 1) + "-" + (startat + thispage) + " / " +
        			"<strong>" + hits.totalHits + " results found" + "</strong></p>");
    		
        	out.println("<center>" + previous + "&nbsp;&nbsp;&nbsp;&nbsp;" + next + "</center>");
        	out.println("<table border=\"1\" width=\"100%\" cellpadding=\"1\">");
        	out.println("<tr>");
        	out.println("<th>DOCUMENT</th>");
        	out.println("<th>MASTERED</th>");
        	out.println("</tr>");
        	for (int i = startat; i < (startat + thispage); i++) {  // for each element
        		int docidx = hits.scoreDocs[i].doc;
        		Document doc = searcher.doc(docidx); //get the next document 
        		String docpath = doc.get("path"); //get its path field
        		if (docpath != null && docpath.startsWith("../webapps/")) docpath = docpath.substring(10);
        		
        		String a_link = null;
        		String mastered = "";
        		try {
        			String cName = ConceptUtil.getLinkedConceptOfRealFilePath(docpath);
        			if(cName == null) continue;
    	            ConceptDB cdb = ZebraStatic.getConceptDB();
    	            Concept concept = cdb.getConcept(cdb.findConcept(cName));
        			TriUMQuery knowledgeQuery = TriUMServer.getInstance().getCommunicateService().getQueryDelegator();
        			
        			//Test evidence
        			boolean isEvidence = knowledgeQuery.isKnowledgeEvidence(userid, course, 
        					ConceptUtil.getBriefConceptName(course, cName), 
        					ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
        			if(isEvidence) {
        				a_link = "<s><font color=\"#808080\">" + concept.getTitle() + "</font>" +
        					"<font size=\"-1\" color=\"#808080\"><em> (is the test or evidence)</em>" + "</font>" +
        					"</s>";
                		out.println("<tr><td>" + a_link + "</td><td>" + mastered + "</td></tr>");
        				continue;
        			}

        			//Invisible because of unmastery
        			PNode unmastered = ConceptUtil.getUnmasteredNode(cName, knowledgeQuery, userid, course);
        			if(unmastered != null) {
	        			HTMLAnchor2 link = new HTMLAnchor2();
        				a_link = link.genLinkTo(cName, profile, false, true);
        				a_link += "<font size=\"-1\" color=\"#808080\">" + 
        					"<em> (not enough knowledge to learn)</em>" +
        					"</font>";
	            		out.println("<tr><td>" + a_link + "</td><td>" + mastered + "</td></tr>");
	            		continue;
        			}

        			boolean doesExist = knowledgeQuery.doesExist(userid, course, 
        					ConceptUtil.getBriefConceptName(course, cName), 
        					ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
        			//Not exist in network
        			if(!doesExist) {
	        			HTMLAnchor2 link = new HTMLAnchor2();
	        			a_link = link.genLinkTo(cName, profile, false, false);
	            		out.println("<tr><td>" + a_link + "</td><td>" + mastered + "</td></tr>");
        				continue;
        			}
        			
        			
        			HTMLAnchor2 link = new HTMLAnchor2();
        			a_link = link.genLinkTo(cName, profile, false, false);
        			double knowledge = knowledgeQuery.knowledgeQuery(userid, course, 
        					ConceptUtil.getBriefConceptName(course, cName), 
        					ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
        			mastered = String.valueOf((int)(knowledge*100.0)) + "%";
            		out.println("<tr><td>" + a_link + "</td><td>" + mastered + "</td></tr>");
            		continue;
        		}
        		catch(Throwable e) {e.printStackTrace(); a_link = null;}
        	
        		//all remaining situations
        		if(a_link == null) {
        			String doctitle = doc.get("title");
            		if(doctitle == null || doctitle.equals("")) doctitle = docpath;
        			a_link = "<a href=\"" + docpath + "\">" + doctitle + "</a>";
            		out.println("<tr><td>" + a_link + "</td><td>" + mastered + "</td></tr>");
        		}
        	}
        	out.println("</table>");
    		out.println("<center>" + previous + "&nbsp;&nbsp;&nbsp;&nbsp;" + next + "</center>");
        }                                            //then include our footer.
        if (searcher != null) searcher.close();
        
        //Log search
        if(firsttime.equals("true")) {
	        try {
	        	String sessionid = request.getSession(true).getId();
	            ZebraStatic.searchLogService.addSearchLog(userid, sessionid, course, queryString);
	        }
	        catch(Exception e) {e.printStackTrace();}
        }
	}//end doPost
	
	public static String createAjaxCode(String contextpath, String userid, String course, String directory,
			String query, int startat, int hitsperpage) {
		//E.g: contextpath = http://localhost:8080/wow
		if(hitsperpage == 0) hitsperpage = 100;
		
		String code = "" +
		"<div id=\"" + ZEBRA_SEARCH_NAMESPACE + "_main\">" + "\n" +
		CommonUtil.generateIncludeJavaScriptCode(ZebraStatic.JAVASCRIPT_DIR + "ajax/dojo.js") + "\n" +

		"<script type=\"text/javascript\">" + "\n" +
		"function " + ZEBRA_SEARCH_NAMESPACE + "_loadSearch(userid, course, directory, query, startat, hitsperpage, firsttime) {" + "\n" +
		"   var wait_div = document.getElementById(\"" + ZEBRA_SEARCH_NAMESPACE + "_wait\");" + "\n" +
		"   wait_div.innerHTML=\"<h3>Waiting for a while when searching...<h3>\";" + "\n" +
		"   var querystring = new Array();" + "\n" +
		"   if(userid != null && userid != \"null\" && userid != \"\")" + "\n" +
		"       querystring['userid'] = userid;" + "\n" +
		"   if(course != null && course != \"null\" && course != \"\")" + "\n" + 
		"      querystring['course'] = course;" + "\n" +
		"   if(directory != null && directory != \"null\" && directory != \"\")" + "\n" + 
		"      querystring['directory'] = directory;" + "\n" +
		"   if(query != null && query != \"null\" && query != \"\")" + "\n" + 
		"      querystring['query'] = query;" + "\n" +
		"   if(startat != null && startat != \"null\" && startat != \"\")" + "\n" + 
		"      querystring['startat'] = startat;" + "\n" +
		"   if(hitsperpage != null && hitsperpage != \"null\" && hitsperpage != \"\")" + "\n" + 
		"      querystring['hitsperpage'] = hitsperpage;" + "\n" +
		"   if(firsttime != null && firsttime != \"null\" && firsttime != \"\")" + "\n" + 
		"      querystring['firsttime'] = firsttime;" + "\n" +
		"   var bindArgs = {" + "\n" +
		"      url: \"" +  contextpath + "/AjaxBackendSearchEngine\"," + "\n" +
		"      method: \"POST\"," + "\n" +
		"      content: querystring," + "\n" +
		"      handle: function(type, data, evt) {" + "\n" +
		"          if (type == \"error\") {" + "\n" +
		"              data = \"<p style='color:red'>\" + data.message + \"</p>\";" + "\n" +
		"          }" + "\n" +
		"          var content_div = document.getElementById(\"" + ZEBRA_SEARCH_NAMESPACE + "_content\");" + "\n" +
		"          content_div.innerHTML = data;" + "\n" +
		"          var wait_div = document.getElementById(\"" + ZEBRA_SEARCH_NAMESPACE + "_wait\");" + "\n" +
		"          wait_div.innerHTML=\"\";" + "\n" +
		"      }," + "\n" +
		"      mimetype: \"text/html\"" + "\n" +
		"   };" + "\n" +
		"   dojo.io.bind(bindArgs);" + "\n" +
		"};" + "\n" + "\n" +

		"function " + ZEBRA_SEARCH_NAMESPACE + "_submit() {" + "\n" +
		"    var f = document.getElementById(\"" + ZEBRA_SEARCH_NAMESPACE + "_searchform\");" + "\n" +
		"    var userid = f.userid.value;" + "\n" +
		"    var course = f.course.value;" + "\n" +
		"    var directory = f.directory.value;" + "\n" +
		"    var query = f.query.value;" + "\n" +
		"    var startat = 0;//f.startat.value;" + "\n" + //Note: in the old version: startat = f.startat.value;
		"    var hitsperpage = f.hitsperpage.value;" + "\n" +
			 ZEBRA_SEARCH_NAMESPACE + "_loadSearch(" +
					"userid, course, directory, query, startat, hitsperpage, \"true\");" + "\n" +
		"};" + "\n" + "\n" +
		"</script>" + "\n" +
		
		"<div id=\"" + ZEBRA_SEARCH_NAMESPACE + "_wait\"></div>" + "\n" +
		"<div id=\"" + ZEBRA_SEARCH_NAMESPACE + "_content\"></div>" + "\n" +

		"<script type=\"text/javascript\">" + "\n" +
		ZEBRA_SEARCH_NAMESPACE + "_loadSearch(" +
		"\"" + userid + "\", " +
		"\"" + course + "\", " +
		"\"" + directory + "\", " +
		"\"" + query + "\", " +
		"\"" + startat + "\", " +
		"\"" + hitsperpage + "\", " +
		"\"" + "true" + "\"" +
		");" + "\n" +
		"</script>" + "\n" +
		
		"</div>";
		
		
		
		return code;
	}
	
	private String callSearch(String userid, String course, String directory, String query, int startat, int hitsperpage) {
		if(hitsperpage == 0) hitsperpage = 100;
		String href =  "javascript:" + ZEBRA_SEARCH_NAMESPACE + "_loadSearch(" +
			"'" + userid + "', " +
			"'" + course + "', " +
			"'" + directory + "', " +
			"'" + query + "', " +
			"'" + startat + "', " +
			"'" + hitsperpage + "', " +
			"'" + "false" + "'" +
			");";
		return href;
	}
	
}
