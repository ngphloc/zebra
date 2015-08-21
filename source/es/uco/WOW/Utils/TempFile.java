package es.uco.WOW.Utils;

import java.io.File;

import vn.spring.WOW.WOWStatic;

/**
 * <p>Title: Wow! TestEditor</p> 
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * @version 1.0
 */
/**
 * NAME: TempFile
 * FUNCTION: This class manages all the options about creating temp files 
 * LAST MODIFICATION: 31-10-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public final class TempFile {

	/**
	 * A count to avoid creating same name folders
	 */
	private static int count = 0;
	/**
	 * Path to the temp folder
	 */
	private static String tempPath = WOWStatic.config.Get("WOWROOT") + "temp";

	/**
	 * Returns a new Id to create a folder in the temporal folder
	 * @return A new unique id
	 */
	public static synchronized final String getNewId() {
		String value = new Long(System.currentTimeMillis()).toString() + count;
		count++; // Avoid different requests in the same millisecond
		if (count == 100) {
			count = 0;
		}
		return value;
	}

	/**
	 * Creates a temp folder in WOWROOT to store temporal content there.
	 * @return the absolute path to the new temp folder
	 */
	public static synchronized String createTempFolder() throws Exception {
		// Creates the main temp folder if it doesn't exist
		String route = checkTempFolder();
		// Creates a new ID
		route += "/" + getNewId();
		// Creates the temp folder if it doesn't exists
		File temp = new File(route);
		if (temp.exists() && temp.isDirectory()) {
			// Deletes the folder if it exists
			deleteFolder(temp);
		}
		temp.mkdir();
		return route;
	}
	
	/**
	 * Deletes the folder that is represented
	 * by this absolute path.
	 * @param path Absolute path to a folder
	 */
	public static void deleteFolder(final File path) 
		throws Exception
  	{
   	if (path.exists()) {
      	File [] files = path.listFiles();
      	// Deletes all the files in the folder
      	for (int i = 0; i < files.length; i++) {
         	if(files[i].isDirectory()) {
         		deleteFolder(files[i]);
         	} else {
           		files[i].delete();
         	}
      	}
      	// Deletes the folder
      	path.delete();
    	}
  	}
	
	/**
	 * Creates a temp folder in WOWROOT to store temporal content there.
	 * @return the absolute path to the temp folder
	 */
	private static synchronized String checkTempFolder() {
		// Creates the temp folder if it doesn't exists
		File temp = new File(tempPath);
		if (!temp.exists()) {
			temp.mkdir();
		}
		return tempPath;
	}
	
} // End of TempFile class