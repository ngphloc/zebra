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

import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.WOW.util.AuthorLogin;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class AuthorService {
	private URL base = null; //Example: http://localhost:8080/wow/Amt.html
	private String context = null; //Example: /wow
	private String author = null;

	public static URL checkBase(URL base) {
        String path = base.getPath();
        String pathttemp = path.substring(1, path.length());
        int index = pathttemp.indexOf("/");
        index++;

        String context = path.substring(0, index);
        if(context == null || context.length() == 0) context = path + "/";
        if(context.charAt(context.length() - 1) != '/') context = context + "/";
        //Eg: http://localhost:8080/wow/
        try {
        	URL url = new URL(base.getProtocol() + "://" + base.getHost() + ":" + base.getPort() + context);
        	return url;
        }
        catch(MalformedURLException e) {System.out.println(e.getMessage());}
        return null;
	}
	public static AuthorService login(URL base) {
		AuthorLogin dlgLogin = new AuthorLogin(base);
		if(dlgLogin.login()) return new AuthorService(base, dlgLogin.getUserName());
		return null;
	}
	
	public String getAuthor() {return author;}
	
	public AuthorService(URL base, String author) {
		base = checkBase(base);
		this.base = base;
        String path = base.getPath();
        String pathttemp = path.substring(1, path.length());
        int index = pathttemp.indexOf("/");
        index++;

        this.context = path.substring(0, index);
        
        this.author = author;
	}
	public boolean checkCourse(String course) throws ZebraException {
		try {
			URL url = new URL(base.getProtocol() + "://" + base.getHost() + ":" + base.getPort() +
		                         context +
		                         "/servlet/authorservlets.CheckCourse?courseName=" +
		                         course + "&userName=" + author);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String sFile = "";
			do {
				sFile = in.readLine();
				if (sFile.trim().equals("true")) return true;
			}
			while (sFile != null);
			in.close();
		} catch (Exception e) {}
		return false;
	}
	
	public boolean checkPwd(String pwd) {
    	boolean exists = false;
        try {
        	// Create an object we can use to communicate with the servlet
        	URL url = new URL(base.getProtocol() + "://" + base.getHost()+":" + base.getPort() + 
        			context + "/AppletLogin");
        	URLConnection servletConnection = url.openConnection();
        	servletConnection.setDoOutput(true);  // to allow us to write to the URL
        	servletConnection.setUseCaches(false);  // to ensure that we do contact
        	// the servlet and don't get
        	// anything from the browser's
        	// cache
        	// Write the message to the servlet
        	PrintStream out = new PrintStream(servletConnection.getOutputStream());
        	out.println(author + "\n" + pwd);
        	out.close();

        	// Now read in the response
        	InputStream in = servletConnection.getInputStream();
        	StringBuffer responce = new StringBuffer();
        	int chr = 0;
        	while ((chr=in.read()) != -1) {
        		responce.append((char) chr);
        	}
        	in.close();
        	if (responce.toString().trim().equals("true")) exists = true;
        }
        catch(IOException ioe) {System.out.println("AuthorLogin: login: Error: "+ioe.toString());}
        return exists;
	}
    public URL getFileURL(String fileName) throws MalformedURLException {
        URL url = new URL(base.getProtocol() + "://" + base.getHost() + ":" + base.getPort() + 
        		context +
        		"/servlet/authorservlets.GetFile?fileName=" + fileName +  "&userName=" + author);
        return url;
    }
    public String getFileData(String fileName) throws MalformedURLException, IOException {
        URL url = getFileURL(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer data = new StringBuffer();
        String s = null;
        while( (s = reader.readLine()) != null ) {
        	data.append(s);
        }
        reader.close();
        return data.toString();
    }
    public void saveFile(byte[] data, String fileName, boolean isDatabase) {
    	try {
	    	URL url = new URL("http://" + base.getHost() + ":" + base.getPort() + context + "/SaveList");
	    	HttpURLConnection uc = (HttpURLConnection) url.openConnection();
	    	uc.setDoOutput(true);
	    	uc.setUseCaches(false);
	    	uc.setRequestProperty("Filename", fileName);
	    	uc.setRequestProperty("Author", author);
	    	uc.setRequestProperty("Created", "");
	    	if(isDatabase) uc.setRequestProperty("Database", "bayes");
	
	    	PrintWriter out = new PrintWriter(uc.getOutputStream());
	    	out.print(new String(data));
	    	out.flush();
	    	out.close();
	    	
            uc.getRequestMethod();
            uc.getResponseCode(); //uc.getInputStream().close();
            
    	}
    	catch (Exception e) {
    		JOptionPane.showMessageDialog(null, "Save Bayesian network faulity:" + e.getMessage());
    		System.out.println("Save Bayesian network faulity:" + e.getMessage());
    	}
    	
    }
    
//    private static boolean validateAuthor(String author, String password) {
//    	AuthorsConfig aconf=new AuthorsConfig();
//    	WowAuthor wowAuthor=aconf.GetAuthor(author);
//        return ((wowAuthor!=null) && wowAuthor.checkPasswd(password));
//    }
//    private static boolean validateCourse(String author, String course) {
//    	AuthorsConfig aconf=new AuthorsConfig();
//    	WowAuthor wowAuthor=aconf.GetAuthor(author);
//    	Vector courselist = wowAuthor.getCourseList();
//    	for(Object o : courselist) {
//    		if(course.equals((String)o)) return true;
//    	}
//    	return false;
//    }
    public static void main(String[] args) {
	}

}
