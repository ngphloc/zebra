package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.spring.WOW.WOWStatic;
import es.uco.WOW.Utils.TempFile;
import es.uco.WOW.Utils.UtilLog;
import es.uco.WOW.Utils.UtilZip;

public class UploadFile extends HttpServlet {
	
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
	 * Stores a file in the server and returns the absolute path
	 * to the file or an empty string if there is any error
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		String theEntireFile = "";
		try {
			// Creates a temp folder to save the file into
			String uploadFolderName = TempFile.createTempFolder();
			File uploadFolder = new File(uploadFolderName);
			String zipFileName = new Long(System.currentTimeMillis()).toString() + ".zip";

			// Creates the upload folder
			uploadFolder.mkdir();
			// Creates the images folder
			File images = new File(uploadFolderName + "/images");
			images.mkdir();

			theEntireFile = uploadFolderName + "/" + zipFileName;
			
			// Reads the file from the input
			BufferedInputStream input = new BufferedInputStream(req.getInputStream());
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(theEntireFile));
			int i = 0;
			while ((i = input.read()) != -1) {
			    output.write(i);
			}
			output.flush();
			input.close();
			output.close();
			
			File tempFile = new File(theEntireFile);
			
			// Unzip the file in the created folder
			try {
				UtilZip.unzipFile(tempFile, new File(uploadFolderName));				
			} catch (Exception e) {
				throw e;
			}
			
			// Deletes the temp zip file
			tempFile.delete();
			
			// Returns the relative name of the folder in the server
			theEntireFile = uploadFolderName.substring(wowRoot.length());
			
		} catch (Exception e) {
			UtilLog.writeException(e);
			theEntireFile = "";
		}
		
		resp.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.print(theEntireFile);
		out.flush();
		out.close();
		
	}

}
