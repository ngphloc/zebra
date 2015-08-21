/**
 * 
 */
package vn.spring.zebra.helperservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.spring.zebra.Initialization;
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
public class UserRemoteLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
        String msg = "";
        BufferedReader reader = req.getReader();
    	String line = null;
    	while ((line = reader.readLine()) != null) {
    		if(line.length() == 0) continue;
    		if(msg.length() > 0) msg += "\n";
    		msg += line;
    	}
    	reader.close();
    	String[] infos = msg.trim().split("\n");
        String userid = infos[0];
        String pwd = infos[1];
        String course = null, directory = null;
        if(infos.length > 2) {
        	course = infos[2];
        	directory = infos[3];
        }

        boolean exist = false;
        try { 
            ProfileDB pdb = ZebraStatic.getProfileDB();
        	long id = pdb.findProfile(userid);
	        if (id != 0) {
	        	Profile profile = pdb.getProfile(id);
	            exist = profile.getAttributeValue("personal", "password").equals(pwd);
	        }
	        else
	        	exist = false;
        }
        catch(Exception e) {System.out.println("Error: " + e.getMessage()); exist = false;}
        
        if(exist == true && course != null && directory != null) {
            Initialization.initWOWandZebra(course, directory, false);
        }

        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println( exist? "true" : "false");
        out.close();
        
	}

}
