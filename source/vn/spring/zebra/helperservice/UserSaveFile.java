package vn.spring.zebra.helperservice;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.*;
import javax.servlet.http.*;

import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserSaveFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	HttpSession session = request.getSession(true);

    	if (session != null) {
    		synchronized (session.getId()) {
    			try {
    				doPost(request, response);
    			} catch (ServletException se) {
    				System.out.println("ServletException");
    			} catch (IOException ie) {
    				System.out.println("IOException");
    			}
    		}
    	}
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    	throws java.io.IOException, ServletException {


    	String xmlfile = URLDecoder.decode(request.getParameter("xmlfile"),"UTF-8");
    	int startIndex = xmlfile.indexOf("[") + 1;
    	int endIndex = xmlfile.indexOf("]");
    	String filepath = xmlfile.substring(startIndex, endIndex); //E.g: W:/wow/xmlroot/bayes/tutorial.bif
		if(filepath.indexOf(":") != -1) {
			//In case filepath = bayes/tutorial.bif
			filepath = ZebraStatic.getWowRoot() + filepath;
		}

    	int startData = xmlfile.indexOf("\n") + 1;
    	String xmlData = xmlfile.substring(startData);

    	try {
    		FileWriter out = new FileWriter(filepath);

    		out.write(xmlData);
    		out.close();
    	} catch (Exception e) {
    		System.out.println("Exception while trying to save!");
    	}
    }
    
}
