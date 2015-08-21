/**
 * 
 */
package vn.spring.zebra.helperservice;

import java.io.*;


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
public class UserSaveList extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(true);
        if (session!=null) synchronized(session.getId()) {
            try {
				doPost(request, response);
            }
            catch (ServletException se) {
                System.out.println(se);
            }
            catch (IOException ie) {
                System.out.println(ie);
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        try {
        	//E.g: filepath = W:/wow/xmlroot/bayes/tutorial.bif
        	String filepath = request.getParameter("filepath");
    		if(filepath.indexOf(":") != -1) {
    			//In case filepath = bayes/tutorial.bif
    			filepath = ZebraStatic.getWowRoot() + filepath;
    		}
            
            InputStream is = request.getInputStream();
            OutputStream us = new FileOutputStream(new File(filepath));
            byte[] buffer = new byte[8192];
            int nr = 8192;
            while (nr != -1) {
                nr = is.read(buffer, 0, 8192);
                if (nr != -1) us.write(buffer, 0, nr);
            }
            us.close();
            is.close();
            response.getOutputStream().close();
            
        }
        catch(Exception e) {System.out.println(e);}
    }
    
}
