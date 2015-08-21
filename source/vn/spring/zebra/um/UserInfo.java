/**
 * 
 */
package vn.spring.zebra.um;

import java.util.*;

import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.DotString;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.exceptions.InvalidAttributeException;
import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserInfo {
	private Profile profile = null;
	private TreeSet<String> keys = new TreeSet<String>();

	public UserInfo(Profile profile) throws Exception {
		this.profile = profile;
		updateInfo();
	}
	public UserInfo(String userid) throws Exception {
		ProfileDB pdb = ZebraStatic.getProfileDB();
		Profile profile = pdb.getProfile(pdb.findProfile(userid));
		this.profile = profile;
		updateInfo();
	}
	public void updateInfo() throws Exception {
		keys.clear();
		updateProfile();
		
		Hashtable<?, ?> values = profile.getValues();
		Set<?> keys = values.keySet();
		Iterator<?> iter = keys.iterator();
		while(iter.hasNext()) {
			String key = (String)iter.next();
			DotString ds = new DotString(key);
			if(ds.size() < 2) continue;
			for(int i = 2; i < ds.size(); i++) ds.set(i, null);
			//Personal or root concept
			if(ds.get(0).equals("personal") || ds.get(0).equals(ds.get(1)))
				this.keys.add(ds.toString());
		}
	}
	public void updateProfile() throws Exception {
		ProfileDB pdb = ZebraStatic.getProfileDB();
		this.profile = pdb.getProfile(pdb.findProfile(getUserId()));
	}
	
	public Profile getProfile() {return profile;}
	public String toString() { return getUserName();}
	public String getUserId() {
    	try {
        	return profile.getAttributeValue("personal", "id");
    	}
    	catch(InvalidAttributeException e) {
    		System.out.println("UserInfo.getUserId causes error: " + e.getMessage());
    		return "";
    	}
	}
	public String getUserName() {
		String username = null;
    	try {
    		username = profile.getAttributeValue("personal", "name");
    	}
    	catch(Exception e) {
    		System.out.println("UserInfo.getUserName causes error: " + e.getMessage());
    	}
    	if(username == null || username.length() == 0) username = getUserId();
    	return username;
	}
	public String getUserMail() {
    	try {
    		return profile.getAttributeValue("personal", "email");
    	}
    	catch(Exception e) {
    		System.out.println("UserInfo.getUserMail causes error: " + e.getMessage());
    	}
		return null;
	}
	public TreeSet<String> getSortedCourseNameList() {
		TreeSet<String> set = new TreeSet<String>();
		for(String key : keys) {
			DotString ds = new DotString(key, ".");
			if(!ds.get(0).equals("personal")) set.add(ds.get(0));
		}
		return set;
	}
}
