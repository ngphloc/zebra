/**
 * 
 */
package vn.spring.WOW.Layout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import vn.spring.WOW.datacomponents.Profile;
import vn.spring.zebra.search.AjaxBackendSearchEngine;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ZebraSearchView extends AbstractView {
	public String getViewType() {return "ZEBRASEARCHVIEW";}
	
	public InputStream genBrsCode(String conceptname, Profile profile,
			Map params) {
		try {
			String wow_path = (String)params.get("wow_path"); //E.g: http://localhost:8080/wow
	        String userid = profile.getAttributeValue("personal", "id");
	        String course = profile.getAttributeValue("personal", "course");
	        String directory = profile.getAttributeValue("personal", "directory");
	        
	        
	        String ajaxCode = AjaxBackendSearchEngine.createAjaxCode(wow_path, userid, course, directory, "", 0, AjaxBackendSearchEngine.HITS_PER_PAGE);
            String html = "<html><head><title>Searching</title>" + createCSSlink(profile) + "</head>" + 
            	"<body>" + ajaxCode + "</body></html>" ;
            return new ByteArrayInputStream(html.getBytes());
		}
		catch (Exception e) {
			System.out.println("ZebraSearchView causes error: " + e.getMessage());
	        return errorStream("[Error generating ZebraSearchView]", e.getMessage());
		}
	}


}
