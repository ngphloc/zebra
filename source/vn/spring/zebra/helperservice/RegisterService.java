/**
 * 
 */
package vn.spring.zebra.helperservice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.AttributeValue;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.bn.ZebraNetworker;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.mining.ZebraMiner;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.util.UserUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public final class RegisterService {
	
	public final static Profile registerUserProfile(String userid, String password, String course, String directory, 
			HashMap<String, String> personalAttrs) throws ZebraException, Exception {
		
		course = course.trim();
		if(course.length() == 0)
			throw new ZebraException("There is no valid application name. Please notify the author, because without this field you cannot log on.");

		password = password.trim();
		if(password.length() == 0)
			throw new ZebraException("No password was entered. You must provide a password for authentication purposes.");

        ProfileDB pdb = ZebraStatic.getProfileDB();
        ZebraStatic.getConceptDB();
        
        long profileid = pdb.findProfile(userid);
        if (profileid == 0) {
            // Profile does not seem to exist... create profile
            AttributeValue value = null;
            Profile profile = pdb.getProfile(pdb.createProfile());
            Hashtable<String, AttributeValue> values = profile.getValues();

            value = new AttributeValue(true);
            value.setValue(userid);
            values.put("personal.id", value);

            value = new AttributeValue(true);
            value.setValue(password);
            values.put("personal.password", value);
            
            value = new AttributeValue(true);
            value.setValue(course);
            values.put("personal.course", value);

            value = new AttributeValue(true);
            value.setValue(directory);
            values.put("personal.directory", value);

            Iterator<String> keys = personalAttrs.keySet().iterator();
            while(keys.hasNext()) {
                String key = keys.next();
                String keyvalue = personalAttrs.get(key);
                
                value = new AttributeValue(true);
                value.setValue(keyvalue);
                values.put("personal." + key, value);
            }
            value = new AttributeValue(true);
            value.setValue("false");
            values.put("personal.umlocked", value);

            pdb.setProfile(profile.id, profile);
        }
        else {
        	System.out.println("User " + userid + " has already existed!");
            return pdb.getProfile(profileid);
        }
        
        Profile profile = pdb.getProfile(pdb.findProfile(userid));
        if (!course.equals(profile.getAttributeValue("personal","course"))) {
        	profile.setAttributeValue("personal","course", course);
        	pdb.setProfile(profile.id, profile);
        }
        if (!course.equals(profile.getAttributeValue("personal","directory"))) {
        	profile.setAttributeValue("personal","directory", course);
        	pdb.setProfile(profile.id, profile);
        }
        System.out.println("Register successfully" + 
        	" User ID=\"" + userid + "\"" +
        	"\tName=\"" + personalAttrs.get("name")
        	);
        return profile;
	}
	public final static TriUM registerTriUM(String userid, String password,
			String course, String directory, 
			HashMap<String, String> personalAttrs) throws ZebraException, Exception {
		registerUserProfile(userid, password, course, directory, personalAttrs);
		return new TriUM(userid, course);
	}
	public final static TriUM registerTriUM(String userid, String password,
			String course, String directory, 
			HashMap<String, String> personalAttrs,
			ZebraNetworker zebraNetworker, ZebraMiner zebraMiner) throws ZebraException, Exception {
		registerUserProfile(userid, password, course, directory, personalAttrs);
		return new TriUM(userid, course, zebraNetworker, zebraMiner);
	}
	
	public static Profile registerUserProfile(String line) throws Exception {
		String userid = null;
		String password = null;
		String course = null;
		String directory = null;

		String[] infos = line.split(",");
		if(infos.length == 0) return null;
		
		HashMap<String, String> personalAttrs = new HashMap<String, String>();
		for(int i = 0; i < infos.length; i++) {
			String info = infos[i].trim();
			if(info.length() == 0) continue;
			String[] pair = infos[i].trim().split("=");
			if(pair.length != 2) continue;
			
			String attr = pair[0].trim();
			String value = pair[1].trim();
			if(attr.length() == 0 || value.length() == 0) continue;
			
			if(attr.equals("userid") || attr.equals("login") || attr.equals("loginid")) {
				userid = value;
			}
			else if(attr.equals("password")) {
				password = value;
			}
			else if(attr.equals("course")) {
				course = value;
			}
			else if(attr.equals("directory")) {
				directory = value;
			}
			else if(attr.equals("name")) {
				personalAttrs.put("name", value);
			}
			else if(attr.equals("email")) {
				personalAttrs.put("email", value);
			}
			else if(attr.equals("start")) {
				personalAttrs.put("start", value);
			}
			else if(attr.equals("title")) {
				personalAttrs.put("title", value);
			}
			else if(attr.equals("background")) {
				personalAttrs.put("background", value);
			}
			else {
				personalAttrs.put(attr, value);
			}
		}// end for
		if(userid == null || password == null || course == null || directory == null) return null;
		return registerUserProfile(userid, password, course, directory, personalAttrs);
	}

	public static void registerUserProfiles(Reader reader) {
		BufferedReader in = new BufferedReader(reader);
		String line = null;
		do{ 
			try {
				line = in.readLine();
				if(line != null && line.length() > 0) registerUserProfile(line);
			}
			catch(Throwable e) {
				System.out.println("registerUserProfiles causes error: " + e.getMessage());
				e.printStackTrace();
			}
		}
		while(line != null);
	}
	
	public static void createUserProfiles(Reader reader) {
		try {
			UserUtil.removeAllUsers();
		}
		catch(Throwable e) {System.out.println("refreshUserProfiles->removeAllUsers causes error: " + e.getMessage());}

		try {
			registerUserProfiles(reader);
		}
		catch(Throwable e) {System.out.println("refreshUserProfiles->registerUserProfiles->removeAllUsers causes error: " + e.getMessage());}
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length != 2) throw new Exception("Must have 2 arguments");
		String flag = args[0];
		if(flag.equals("-c") || flag.equals("-create")) {
			String createFilePath = args[1];
			Reader reader = null;
			try {
				reader = new FileReader(createFilePath);
			}
			catch(FileNotFoundException e) {
				reader = new InputStreamReader(Class.class.getResourceAsStream(createFilePath));
			}
			createUserProfiles(reader);
		}
		else throw new Exception("Not support this parameter");
			
	}
}
