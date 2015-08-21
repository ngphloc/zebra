/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AuthorLogin.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.util;

import javax.swing.*;
import java.net.*;
import java.io.*;

//Modified by Loc Nguyen 2008
public class AuthorLogin {

    public URL home;
    public String username = "";
    public String password = "";

    public AuthorLogin(URL home) {
		this.home = home;
	}

    public boolean login() {
		boolean userValid = false;
		LoginDialog login = new LoginDialog(new JFrame(""));
		if (login.id) {
			username = login.username.getText();
			setUserName(username);
			char[] passwd = login.password.getPassword();
			password = new String(passwd);
			userValid = validateUser(username, password);
		} else System.out.println("Cancel was pressed");
		login.dispose();
		return userValid;
	}

	private boolean validateUser(String usr, String pwd) {

    	boolean exists = false;
    	String contextpath = "";
    	StringBuffer responce = new StringBuffer();
        try {
          String path = home.getPath();
          String temp = path.substring(1, path.length());
          int index = temp.indexOf("/");
          index++;
          contextpath = path.substring(0, index);
          // Create an object we can use to communicate with the servlet
          URL servletURL = new URL(home.getProtocol()+"://"+home.getHost()+":"+home.getPort()+contextpath+"/AppletLogin");
          URLConnection servletConnection = servletURL.openConnection();
          servletConnection.setDoOutput(true);  // to allow us to write to the URL
          servletConnection.setUseCaches(false);  // to ensure that we do contact
          // the servlet and don't get
          // anything from the browser's
          // cache
          // Write the message to the servlet
          PrintStream out = new PrintStream(servletConnection.getOutputStream());
          out.println(usr+"\n"+pwd);
          out.close();

          // Now read in the response
          InputStream in = servletConnection.getInputStream();
          int chr;
          while ((chr=in.read())!=-1) {
            responce.append((char) chr);
          }
          in.close();
          if (responce.toString().trim().equals("true")) exists = true;
        } catch(IOException ioe) {System.out.println("AuthorLogin: login: Error: "+ioe.toString());}

        return exists;
      }

	public void setUserName(String name) {
		username=name;
	}

	public String getUserName() {
		return username;
	}

}
