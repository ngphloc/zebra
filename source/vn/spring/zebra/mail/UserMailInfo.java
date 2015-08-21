/**
 * 
 */
package vn.spring.zebra.mail;

import org.w3c.dom.Element;

import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public class UserMailInfo {
	public String userid = null;
	public String username = null;
	public String mail = null;
	
	public UserMailInfo(Element user) {
		this.userid = user.getAttribute("id");
		this.username = user.getAttribute("name");
		this.mail = user.getAttribute("mail");
	}
	public UserMailInfo(String userid) {
		try {
			ProfileDB pdb = ZebraStatic.getProfileDB();
			Profile profile = null;
			String mail = null;
			profile = pdb.getProfile(pdb.findProfile(userid));
			
			try {
				mail = profile.getAttributeValue("personal", "email");
			}
			catch(Exception e) {e.printStackTrace();}
			if(mail == null || mail.length() == 0) mail = null;
			
			String username = null;
			try {
				username = profile.getAttributeValue("personal", "name");
			}
			catch(Exception e) {e.printStackTrace();}
			if(username == null || username.length() == 0)
				username = userid;
			
			this.userid = userid;
			this.username = username;
			this.mail = mail;
		}
		catch(Exception e) {e.printStackTrace();}
	}
	public UserMailInfo(Profile profile) {
		if(profile == null) return;
		
		String userid = null;
		try {
			userid = profile.getAttributeValue("personal", "id");
		}
		catch(Exception e) {e.printStackTrace();}
		if(userid == null || userid.length() == 0) return;

		String mail = null;
		try {
			mail = profile.getAttributeValue("personal", "email");
		}
		catch(Exception e) {e.printStackTrace();}
		if(mail == null || mail.length() == 0) mail = null;
		
		String username = null;
		try {
			username = profile.getAttributeValue("personal", "name");
		}
		catch(Exception e) {e.printStackTrace();}
		if(username == null || username.length() == 0)
			username = userid;
		
		this.userid = userid;
		this.username = username;
		this.mail = mail;
	}
}

