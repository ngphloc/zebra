/**
 * 
 */
package vn.spring.WOW.Layout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
public class ZebraMainView extends MainView {
	protected String     userid = null;
	protected String     course = null;
	protected TriUMQuery query = null;

	public String getViewType() {
		return "ZEBRAMAINVIEW";
	}

	@Override
	public InputStream genBrsCode(String conceptname, Profile profile,
			Map params) {
		try {
	        this.userid = profile.getAttributeValue("personal", "id");
	        this.course = profile.getAttributeValue("personal", "course");
	        this.query = (TriUMQuery) WOWContextListener.getInstance().getTriUMServer().
	        	getCommunicateService().getQueryDelegator();

            PNode invisblenode = ConceptUtil.getUnmasteredNode(conceptname, this.query, this.userid, this.course);
            
            if(invisblenode != null) {
	            StringBuffer res = new StringBuffer();
	            ConceptDB cdb = ZebraStatic.getConceptDB();
	            Concept invisibleConcept = cdb.getConcept(cdb.findConcept(invisblenode.getName()));
	            res.append("<html><body><h3>" + 
	            	"You don't have enough knowledge to study this concept. Must be mastered over the previous concept" +
	            	"<strong>\"" + invisibleConcept.getTitle() + "\"</strong>" +
	            	"</h3></body></html>");
	            return new ByteArrayInputStream(res.toString().getBytes());
            }
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

}
