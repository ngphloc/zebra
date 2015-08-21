/**
 * 
 */
package vn.spring.WOW.Layout;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.ConceptDB;
import vn.spring.WOW.datacomponents.Concept;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.PNode;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.helperservice.WOWContextListener;
import vn.spring.zebra.util.ConceptUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ZebraMainView2 extends MainView {
	protected String     userid = null;
	protected String     course = null;
	protected TriUMQuery query = null;

	public String getViewType() {return "ZEBRAMAINVIEW2";}

	@Override
	public InputStream genBrsCode(String conceptname, Profile profile,
			Map params) {
		try {
	        this.userid = profile.getAttributeValue("personal", "id");
	        this.course = profile.getAttributeValue("personal", "course");
	        this.query = (TriUMQuery) WOWContextListener.getInstance().getTriUMServer().
	        	getCommunicateService().getQueryDelegator();

            PNode invisblenode = ConceptUtil.getUnmasteredNode(conceptname, this.query, this.userid, this.course);
            
            String fragment = null;
            if(invisblenode != null) {
	            ConceptDB cdb = ZebraStatic.getConceptDB();
	            Concept invisibleConcept = cdb.getConcept(cdb.findConcept(invisblenode.getName()));
	            fragment = "<h3><font color=\"#FF0000\">" + 
	            	"You don't have enough knowledge to study this concept. <br/>Must be mastered over the previous concept " +
	            	"<strong>\"" + invisibleConcept.getTitle() + "\"</strong>" +
	            	"</font></h3>";

	        	InputStream dataInput = super.genBrsCode(conceptname, profile, params);
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(dataInput));
	        	StringBuffer data = new StringBuffer();
	        	String line = null;
	        	while( (line = reader.readLine()) != null ) {
	        		data.append(line);
	        	}
	            return new ByteArrayInputStream(insertFragment(data.toString(), fragment).getBytes());
            }
            else
            	return super.genBrsCode(conceptname, profile, params);
		}
		catch(Exception e) {
			e.printStackTrace();
            return errorStream("[Error generating ZebraMainView]", e.getMessage());
		}
	}
    protected InputStream errorStream(String error, String detail) {
        String errorreport = "<html><head><title>" + WOWStatic.normalize(error) + "</title></head>" +
                             "<body><span style=\"color: #CC0000;\"><p><h1>"+WOWStatic.normalize(error)+"</h1></p></span>"+
                             WOWStatic.normalize(detail)+"</body></html>";
        return new ByteArrayInputStream(errorreport.getBytes());
    }
	protected String insertFragment(String data, String fragment) {
		if(data.indexOf("<body>") != -1) {
			data = data.replaceFirst("<body>", "<body>\n" + fragment);
		}
		else if(data.indexOf("<html>") != -1) {
			data = data.replaceFirst("<html>", "<html>\n" + fragment);
		}
		else
			data = "<div>" + fragment + "\n" + data + "</div>";
		return data;
	}
}
