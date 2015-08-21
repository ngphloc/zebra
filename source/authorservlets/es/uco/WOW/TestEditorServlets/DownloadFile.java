package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.spring.WOW.WOWStatic;
import es.uco.WOW.Utils.UtilLog;

public class DownloadFile extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/***
	 * Root of the WOW system
	 */
	private static String wowRoot = WOWStatic.config.Get("WOWROOT");
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
	{
		doPost(req, resp);
	}
	
	/**
	 * Receives the path to a file in the server and downloads it
	 * After that, deletes all the files in the folder and the own folder
	 * that contains this file
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		try {
			String theZipFile = wowRoot + request.getParameter("zipFile");

			// Reads the file from the input
			File file = new File(theZipFile);
			if (file.exists()) {
				BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
				BufferedOutputStream output = new BufferedOutputStream(resp.getOutputStream());
				int i;
				while ((i = input.read()) != -1) {
				    output.write(i);
				}
				output.flush();
				input.close();
				output.close();
				
				// Deletes the file
				file.delete();
			} else {
				throw new Exception("The requested file doesn't exists");
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			PrintWriter out = resp.getWriter();
			resp.setContentType("text/plain;charset=UTF-8");
			out.print(e.toString());
			out.flush();
			out.close();
			
		}
		
	}
	
}
