/**
 * 
 */
package vn.spring.zebra.helperservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserService {
	protected URL    base = null; //Example: http://localhost:8080/wow/Amt.html
	protected String context = null; //Example: /wow
	protected String userid = null;
	protected String course = null;
	protected String directory = null;

	public static UserService login(URL base, String userid, String pwd, String course, String directory) {
		base = AuthorService.checkBase(base);
		if(base == null) return null;
		
        String path = base.getPath();
        String pathttemp = path.substring(1, path.length());
        int index = pathttemp.indexOf("/");
        index++;
        String context = path.substring(0, index);
        
    	boolean exist = false;
        try {
        	// Create an object we can use to communicate with the servlet
        	URL url = new URL(base.getProtocol() + "://" + base.getHost()+":" + base.getPort() + 
        			context + "/UserRemoteLogin");
        	URLConnection servletConnection = url.openConnection();
        	servletConnection.setDoOutput(true);  // to allow us to write to the URL
        	servletConnection.setUseCaches(false);  // to ensure that we do contact
        	PrintStream out = new PrintStream(servletConnection.getOutputStream());
        	String msg = userid + "\n" + pwd;
        	if(course != null && directory != null && 
        	   course.length() > 0 && directory.length() > 0) {
        		msg += "\n" + userid + "\n" + pwd;
        	}
        	out.print(msg);
        	out.close();

        	// Now read in the response
        	InputStream in = servletConnection.getInputStream();
        	StringBuffer responce = new StringBuffer();
        	int chr = 0;
        	while ((chr=in.read()) != -1) {
        		responce.append((char) chr);
        	}
        	in.close();
        	if (responce.toString().trim().equals("true")) exist = true;
        }
        catch(IOException ioe) {System.out.println("AuthorLogin: login: Error: "+ioe.toString()); exist = false;}
        
        if(!exist) return null;
        
        UserService userService = new UserService();
        userService.base = base;
        userService.context = context;
        userService.userid = userid;
        userService.course = course;
        userService.directory = directory;
        return userService;
	}
	
    public URL getFileURL(String filepath) throws MalformedURLException {
    	//E.g: filepath = W:/wow/xmlroot/bayes/tutorial.bif or bayes/tutorial.bif 
        URL url = new URL(base.getProtocol() + "://" + base.getHost() + ":" + base.getPort() + 
        		context + "/UserGetFile?filepath=" + filepath);
        return url;
    }
    public String getFileData(String filepath) throws MalformedURLException, IOException {
    	//E.g: filepath = W:/wow/xmlroot/bayes/tutorial.bif or bayes/tutorial.bif
    	URL url = getFileURL(filepath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer data = new StringBuffer();
        String s = null;
        while( (s = reader.readLine()) != null ) {
        	data.append(s);
        }
        reader.close();
        return data.toString();
    }
    public void saveFile(String data, String filepath) {
    	try {
	    	URL url = new URL("http://" + base.getHost() + ":" + base.getPort() + 
	    			context + "/UserSaveList?filepath=" + filepath);
	    	HttpURLConnection uc = (HttpURLConnection) url.openConnection();
	    	uc.setDoOutput(true);
	    	uc.setUseCaches(false);
	
	    	PrintWriter out = new PrintWriter(uc.getOutputStream());
	    	out.print(data);
	    	out.flush();
	    	out.close();
	    	
            uc.getRequestMethod();
            uc.getResponseCode(); //uc.getInputStream().close();
            
    	}
    	catch (Exception e) {
    		JOptionPane.showMessageDialog(null, "Save file faulity: " + e.getMessage());
    		System.out.println("Save file faulity: " + e.getMessage());
    	}
    	
    }
    public void saveFile(byte[] data, String filepath) {
    	//E.g: filepath = xmlroot/bayes/tutorial.bif or bayes/tutorial.bif
    	saveFile(new String(data), filepath);
    }
	
}
