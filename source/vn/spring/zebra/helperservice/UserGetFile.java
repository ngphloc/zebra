package vn.spring.zebra.helperservice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public class UserGetFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
    	throws ServletException, IOException {

		String filepath = req.getParameter("filepath");//E.g: filepath = W:/wow/xmlroot/bayes/tutorial.bif
		if(filepath.indexOf(":") != -1) {
			//In case filepath = bayes/tutorial.bif
			filepath = ZebraStatic.getWowRoot() + filepath;
		}
		
		res.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = res.getWriter();
		String outstring = null;
		try {
			FileReader fr = new FileReader(filepath);
			BufferedReader in = new BufferedReader(fr);
			
			while ((outstring = in.readLine()) != null) {
				out.println(outstring);
			}
			in.close();
		}
		catch(IOException e) {
			System.out.println("File not found! The getfile servlet couldn`t find: " + filepath);
		}
		out.close();
	}
	
}
