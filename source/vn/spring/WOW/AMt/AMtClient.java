//------------------------------------------------------------------------------
// Unit Name: AMtClient.java
// Author: T.J. Dekker, reviewed and modified by Loc Nguyen
// Date of Creation: 14-09-2008
// Purpose: Contains all functionality behind the AMtClientGUI class.
//
// DOCUMENT CHANGES
//
// Date:           Author:             Change:
// -----------------------------------------------------------------------------
// 14-09-2008      T.J. Dekker         Creation
//------------------------------------------------------------------------------

package vn.spring.WOW.AMt;

import java.io.*;
import java.net.*;
import javax.swing.JProgressBar;

import vn.spring.WOW.WOWStatic;

/**
 * Core class for the AMt Client application. Contains all functionality behind \
 * the AMtClientGUI class. This class is strongly connected to the
 * {$link AMtServer} class. The servlet handles the requests made by this
 * client.
 * @author T.J. Dekker, changed by Loc Nguyen
 * @version 1.0.0
 * $see AMtServer
 */
public class AMtClient {

  private String authorname;      //the name of the currently logged in author
    private URL url;                //the URL to the AMtServlet
    private AMtClientGUI gui;       //the User Interface accompanying this class
  private boolean cancelTransfer; //indicating the cancellation of a transfer


  /**
   * Constructor for usage of this class outside AMt
   * @param home the url to the WOW! server
   * @param aauthorname name of an author
   */
  public AMtClient(URL home, String aauthorname) {
    cancelTransfer = false;
    gui = null;

    String path = home.getPath();
    String temp = path.substring(1, path.length());
        int index = temp.indexOf("/");
        index++;
        String contextpath = path.substring(0, index);

    try {
      url = new URL(home.getProtocol()+"://"+home.getHost()+":"+
      home.getPort()+contextpath+"/" + AMtc.SERVLETNAME);
    }
    catch (Exception e) {
      System.out.println("AMtClient(URL, String): Could not creat servletURL");
    }

    authorname = aauthorname;
  }

  /**
   * Default Constructor, for usage of this class from AMtClientGUI
   * @param pgui the User Interface accompanying this class
   */
  public AMtClient(AMtClientGUI pgui) {
    try {
      gui = pgui;
      url = new URL(AMtc.SERVLETPATH);

      cancelTransfer = false;
    }
    catch (MalformedURLException e) {
      System.out.println("Error in AMtClient(): " + e.toString());
    }
  }

  /**
   * Sets the canceltransfer variable to <code>true</code>, indicating that the
   * current transfer (upload or download) should be cancelled.
   */
  public void cancelTransfer() {
    cancelTransfer = true;
  }

  /**
   * Logs in an author to the WOW! server, meaning that the {$link AMtServer}
   * servlet checks if the entered account data is a valid author account.`
   * @param aauthorname the name of the author
   * @param apassword   the password for the author
   * @return <Code>true</Code> if and only if the authoraccount exists
   */
  public boolean login(String aauthorname, String apassword) {
    try {
      String s = "amethod=login&aauthorname=" +
        e(aauthorname) + "&apassword=" + e(apassword);

        Boolean result = (Boolean) post(url, s.getBytes());

      //store name of logged in author for future use
      if (result.booleanValue()) {
        authorname = aauthorname;
      }

            return result.booleanValue();
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.login: " + e.toString());
        }
    return false;
  }

  /**
   * Changes the profile of the currently logged in author. This means that the
   * author's password and name are updated with new values.
   * @param aauthorname the loginname of the author
   * @param anewpassword the (new) password for the author
   * @param aname the name of the author
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  public int changeProfile(String aauthorname, String anewpassword, String aname) {
    try {
      String s = "amethod=changeProfile&aauthorname=" +
        e(aauthorname) + "&anewpassword=" + e(anewpassword) + "&aname=" + e(aname);

        Integer result = (Integer) post(url, s.getBytes());

            return result.intValue();
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.changeProfile: " + e.toString());
        }
    return AMtc.ERROR_NOT_CONNECTED;
  }

  /**
   * Adds an application for the logged in author to its registered list of
   * applications and creates a folder for the application if it did not exist.
   * @param aappname the name of the application.
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  public int addApp(String aappname) {
    try {
      String s = "amethod=addApp&aauthorname=" + e(authorname) + "&aappname=" +
      e(aappname);
        Integer result = (Integer) post(url, s.getBytes());

            return result.intValue();
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.addApp: " + e.toString());
        }
    return AMtc.ERROR_NOT_CONNECTED;
  }

  /**
   * Deletes an application for the logged in author from its registered list
   * of applications and deletes the application's folder if it exists.
   * @param aappname the name of the application
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  public int delApp(String aappname) {
    try {
        String s = "amethod=delApp&aauthorname=" + e(authorname) + "&aappname=" +
      e(aappname);
      Integer result = (Integer) post(url, s.getBytes());

            return result.intValue();
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.delApp: " + e.toString());
        }
    return AMtc.ERROR_NOT_CONNECTED;
  }

  /**
   * Deletes an applications domain model for the logged in author.
   * @param aappname the name of the application
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  public int delAppDomain(String aappname) {
    try {
        String s = "amethod=delAppDomain&aauthorname=" + e(authorname) + "&aappname=" +
      e(aappname);
      Integer result = (Integer) post(url, s.getBytes());

            return result.intValue();
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.delApp: " + e.toString());
        }
    return AMtc.ERROR_NOT_CONNECTED;
  }

  /**
   * Uploads a file to the WOW! server filesystem to the specified path
   * @param f An existing file on the local filesystem. This method can only
   * upload files, no directories.
   * @param apath The path on the WOW! server filesystem to upload to. Path must
   * be of form: <code>/app/folder1/concept1.xhtml</code> and relative to the
   * wow folder on the server
   * @param bar The progressbar for this upload, which will be filled with
   * the progress for this upload.
   * @param afilemode mode indicating location of the file, which is either
   * AMtc.APP_FILES_MODE or AMtc.AUTHOR_FILES_MODE
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  public int upload(File f, String apath, JProgressBar bar, int afilemode) {
      if (f == null) return AMtc.ERROR;
      if (!f.exists()) return AMtc.ERROR;
      if (f.isDirectory()) return AMtc.ERROR;
    //set path based on filemode
    String path = calcPath(apath, afilemode);
      if (path == null) return AMtc.ERROR;

    try {
      int error = AMtc.NO_ERRORS;
      int partno = 0;
      FileInputStream in = new FileInputStream(f);
      //double filesize = new Double("" + f.length()).doubleValue();
      double tosend = new Long(f.length()).doubleValue();
      double sentbytes = 0;
      int partsize;
      int buffer = AMtc.CLIENT_BUFFER_SIZE;

      //double starttime = System.currentTimeMillis();
      bar.setMaximum(100);

      //send file in parts with size of buffer
      while (tosend > 0 && !cancelTransfer) {
        partsize = Math.min(buffer, in.available());

        String s = "amethod=upload&aauthorname=" + e(authorname) + "&apath=" +
          e(path) + "&aappend=" + ((partno==0)?"0":"1") + "&afile=";

        byte[] args = s.getBytes();
        byte[] bytebuffer = new byte[partsize+args.length];

        System.arraycopy(args, 0, bytebuffer, 0, args.length);

        while (in.available() == 0) {
          Thread.sleep(AMtc.UPLOAD_SLEEP_TIME);
        }
        in.read(bytebuffer, args.length, partsize);

        //post bytebuffer
        error = ((Integer) post(url, bytebuffer)).intValue();
        if (error != AMtc.NO_ERRORS) {in.close(); return error;}
        partno++;
        sentbytes += partsize;
        tosend -= partsize;

        bar.setValue((int)Math.floor((sentbytes/f.length())*100));

        gui.repaintTable();
      }
      in.close();

      //double time = (new Double(System.currentTimeMillis()).doubleValue()-starttime)/1000.0;
      //System.out.println("" + new Double((filesize/time)/(new Double(""+1000).doubleValue())).shortValue() + " Kb/sec");

      if (cancelTransfer) {
        delete(apath, afilemode);
        cancelTransfer = false;
        return AMtc.ERROR_USER_ABORTED;
      }
            return error;
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.upload: " + e.toString());
      return AMtc.ERROR_NOT_CONNECTED;
        }
        catch (Exception e) {
          System.out.println("Exception in AMtClient.upload: " + e.toString());
      return AMtc.ERROR;
        }
  }

  /**
   * Downloads a file from the WOW! server filesystem at the specified path to
   * the local filesystem. This method can only download files, no directories.
   * @param afilepath The path on the WOW! server filesystem to download.
   * Path must be of form: <Code>/app/file1.xhtml</Code> and relative to the wow
   * folder on the server.
   * @param f the file to write to
   * @param size the size of the file to download
   * @param bar the progressbar for this download
   * @param afilemode mode indicating location of the file, which is either
   * AMtc.APP_FILES_MODE or AMtc.AUTHOR_FILES_MODE
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  public int download(String afilepath, File f, long size, JProgressBar bar,
                                                              int afilemode) {
      //set path based on filemode
    String path = calcPath(afilepath, afilemode);
      if (path == null) return AMtc.ERROR;

    try {
        String s = "amethod=download&aauthorname=" + e(authorname) +
      "&afilepath=" + e(path);
      int result = postDownload(url, s.getBytes(), f, size, bar);

            return result;
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.download: " + e.toString());
        }
    return AMtc.ERROR_NOT_CONNECTED;
  }

  /**
   * Lists all files that exist in the specified folder on the WOW! server
   * filesystem.
   * @param afolderpath The path on the WOW! server filesystem to list the files
   * for. Path must be of form <code>/app/folder1</code>  and relative to the
   * wow folder on the server
   * @param afilemode mode indicating location of the file, which is either
   * AMtc.APP_FILES_MODE or AMtc.AUTHOR_FILES_MODE
   * @return A list of all files and directories located in the folder at
   * afolderpath on the WOW! filesystem, or <code>null</code> if the specified
   * folder does not exist on the server. <Code>null</Code> is also returned if
   * the specified folder path leads outside the wow context dir or to an
   * application folder that the user does not own.
   */
  public TAMtFile[] listFiles(String afolderpath, int afilemode) {
    try {
        String method = null;
        if (afilemode == AMtc.APP_FILES_MODE)
          method = "listAppFiles";
        if (afilemode == AMtc.AUTHOR_FILES_MODE)
          method = "listAuthorFiles";

        if (method == null) return null;


      String s = "amethod=" + method + "&aauthorname=" + e(authorname) +
      "&afolderpath=" + e(afolderpath);
      //System.out.println(s);
      TAMtFile[] result = (TAMtFile[]) post(url, s.getBytes());

            return result;
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.listFiles: " + e.toString());
        }
    return null;
  }

 /**
   * Deletes a file or folder at the specified path from the WOW! server
   * filesystem. If the specified path is a folder, all contents of the folder
   * are deleted also.
   * @param apath the path to delete. Path must be of form
   * <code>/app/folder1</code> and relative to the wow folder on the server.
   * @param afilemode mode indicating location of the file, which is either
   * AMtc.APP_FILES_MODE or AMtc.AUTHOR_FILES_MODE
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  public int delete(String apath, int afilemode) {
      //set path based on filemode
    String path = calcPath(apath, afilemode);
      if (path == null) return AMtc.ERROR;

    try {
        String s = "amethod=delete&aauthorname=" + e(authorname) + "&apath=" +
      e(path);

      Integer result = (Integer) post(url, s.getBytes());
            return result.intValue();
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.delete: " + e.toString());
        }
    return AMtc.ERROR_NOT_CONNECTED;
  }

  /**
   * Renames a file or folder at the WOW! server filesystem.
   * @param apath the path to rename. Path must be of form
   * <code>/app/folder1</code> and relative to the wow folder on the server.
   * @param anewname  the new name of the file or folder located at apath
   * @param afilemode mode indicating location of the file, which is either
   * AMtc.APP_FILES_MODE or AMtc.AUTHOR_FILES_MODE
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  public int rename(String apath, String anewname, int afilemode) {
      //set path based on filemode
    String path = calcPath(apath, afilemode);
      if (path == null) return AMtc.ERROR;

    try {
        String s = "amethod=rename&aauthorname=" + e(authorname) + "&apath=" +
      e(path) + "&anewname=" + e(anewname);
      Integer result = (Integer) post(url, s.getBytes());

            return result.intValue();
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.rename: " + e.toString());
        }
    return AMtc.ERROR_NOT_CONNECTED;
  }

  /**
   * Copies a file or folder at the WOW! server filesystem.
   * @param asource sourcepath for the file to copy Path must be of form
   * <code>/app/folder1/abc.xhtml</code> and relative to the wow folder on the
   * server.
   * @param atarget the path of the folder to copy the file into. Path must be
   * of form <code>/app/folder1</code> and relative to the wow folder on the
   * server.
   * @param fromfilemode mode indicating location of the file to copy from,
   * which is either AMtc.APP_FILES_MODE or AMtc.AUTHOR_FILES_MODE
   * @param tofilemode mode indicating location of the file to copy to, which is
   * either AMtc.APP_FILES_MODE or AMtc.AUTHOR_FILES_MODE
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  public int copyFile(String asource, String atarget, int fromfilemode,
                                                               int tofilemode) {
    //set path based on filemode
    String sourcepath = calcPath(asource, fromfilemode);
    String targetpath = calcPath(atarget, tofilemode);
    if (sourcepath == null || targetpath == null) return AMtc.ERROR;

    try {
        String s = "amethod=copyFile&aauthorname=" + e(authorname) + "&asource=" +
      e(sourcepath) + "&atarget=" + e(targetpath);
      Integer result = (Integer) post(url, s.getBytes());

            return result.intValue();
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.copyFile: " + e.toString());
        }
    return AMtc.ERROR_NOT_CONNECTED;
  }

  /**
   * Constructs a CCC report for the specified application
   * @param aappname the name of an application.
   * @return the CCC report for the application.
   */
  public TCCCReport CCC(String aappname) {
    try {
        String s = "amethod=CCC&aauthorname=" + e(authorname) + "&aappname=" +
      e(aappname);
      TCCCReport result = (TCCCReport) post(url, s.getBytes());

            return result;
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.CCC: " + e.toString());
        }
    return null;
  }

  /**
   * Adds a folder to the WOW! server filesystem.
   * @param apath the path of the folder to add. Path must be of form
   * <code>/app/folder1</code> and relative to the wow folder on the server.
   * @param afilemode mode indicating location of the file, which is either
   * AMtc.APP_FILES_MODE or AMtc.AUTHOR_FILES_MODE
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  public int addFolder(String apath, int afilemode) {
      //set path based on filemode
    String path = calcPath(apath, afilemode);
      if (path == null) return AMtc.ERROR;

    try {
        String s = "amethod=addFolder&aauthorname=" + e(authorname) + "&apath=" +
      e(path);
      Integer result = (Integer) post(url, s.getBytes());

            return result.intValue();
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.addFolder: " + e.toString());
        }
    return AMtc.ERROR_NOT_CONNECTED;
  }

  /**
   * Retrieves the name of the logged in author.
   * @return The name of the author, or null if an error occured.
   */
  public String getName() {
    try {
        String s = "amethod=getName&aauthorname=" + e(authorname);
      String result = (String) post(url, s.getBytes());

            return result;
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.getName: " + e.toString());
        }
    return null;
  }

    /**
     * Encodes a string in UTF-8 format.
     * @param s the string to encode
     * @return the encoded string
     */
  private String e(String s) {
      try {
      return URLEncoder.encode(s, "UTF-8");
    }
    catch (Exception e) { System.out.println("Exception in AMtClient.e(): " +
      e.toString()); return "";
    }
    }

    /**
     * Calculates a path based on the filemode. This means that the path is or
     * is not adapted to lead to the authorfiles folder on the server.
     * @param path the path to transform
   * @param filemode mode indicating location of the file, which is either
   * AMtc.APP_FILES_MODE or AMtc.AUTHOR_FILES_MODE
   * @return The transformed path, or <Code>null</Code> if the filemode is
   * unknown or an error occured.
     */
  private String calcPath(String path, int filemode) {
    if (filemode == AMtc.APP_FILES_MODE)
      return path;
      else if (filemode == AMtc.AUTHOR_FILES_MODE) {
      String afpath = WOWStatic.AUTHORFILESPATH;
      if (afpath == null) return null;

      return afpath + authorname + path;
      }
      else return null;
    }

    /**
   * Adds an <Code>.wow</Code> file in the authorfiles folder of the currently
   * logged in author at the WOW! server filesystem.
   * @param aname the name for the <Code>.wow</Code> file to create. It is
   * recommended that this name is the same as an application name of the
   * author.
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
     */
  public int addWowFile(String aname) {
    try {
        String s = "amethod=addWowFile&aauthorname=" + e(authorname) + "&aname=" +
      e(aname);
      Integer result = (Integer) post(url, s.getBytes());

            return result.intValue();
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.addWowFile: " + e.toString());
        }
    return AMtc.ERROR_NOT_CONNECTED;
    }

    /**
   * Adds an <Code>.gaf</Code> file in the authorfiles folder of the currently
   * logged in author at the WOW! server filesystem.
   * @param aname the name for the <Code>.gaf</Code> file to create. It is
   * recommended that this name is the same as an application name of the
   * author.
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
     */
  public int addGafFile(String aname) {
    try {
        String s = "amethod=addGafFile&aauthorname=" + e(authorname) + "&aname=" +
      e(aname);
      Integer result = (Integer) post(url, s.getBytes());

            return result.intValue();
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.addGafFile: " + e.toString());
        }
    return AMtc.ERROR_NOT_CONNECTED;
    }
  public int addBifFile(String aname) {
	    try {
	        String s = "amethod=addBifFile&aauthorname=" + e(authorname) + "&aname=" +
	      e(aname);
	      Integer result = (Integer) post(url, s.getBytes());

	            return result.intValue();
	        }

	        catch (IOException e) {
	          System.out.println("IOException in AMtClient.addBifFile: " + e.toString());
	        }
	    return AMtc.ERROR_NOT_CONNECTED;
	    }

  /**
   * Downloads a file from the WOW! server filesystem at the specified path.
   * WARNING: this method should only be used to retrieve small non-binary
   * files. Use the <Code>download</Code> method if this is not the case.
   * @param afilepath The path on the WOW! server filesystem to download.
   * Path must be of form: <Code>/app/file1.xhtml</Code> and relative to the wow
   * folder on the server.
   * @param afilemode mode indicating location of the file, which is either
   * AMtc.APP_FILES_MODE or AMtc.AUTHOR_FILES_MODE
   * @return the requested file, or null if an error occurred
   */
  public StringBuffer getFile(String afilepath, int afilemode) {
      //set path based on filemode
    String path = calcPath(afilepath, afilemode);
      if (path == null) return null;

    try {
        String s = "amethod=getFile&aauthorname=" + e(authorname) +
      "&afilepath=" + e(path);
      StringBuffer result = (StringBuffer)post(url, s.getBytes());

            return result;
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.getFile: " + e.toString());
        }
    return null;
  }

  /**
   * Uploads a file to the WOW! server filesystem at the specified path.
   * WARNING: this method should only be used to upload small non-binary
   * files. Use the <Code>upload</Code> method if this is not the case.
   * @param afilepath The path on the WOW! server filesystem to upload to.
   * Path must be of form: <Code>/app/file1.xhtml</Code> and relative to the wow
   * folder on the server.
   * @param afilemode mode indicating location of the file, which is either
   * AMtc.APP_FILES_MODE or AMtc.AUTHOR_FILES_MODE
   * @param contents the contents of the file to upload
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  public int sendFile(String afilepath, int afilemode, String contents) {
      //set path based on filemode
    String path = calcPath(afilepath, afilemode);
      if (path == null) return AMtc.ERROR_INVALID_PATH;

    try {
        String s = "amethod=sendFile&aauthorname=" + e(authorname) +
      "&afilepath=" + e(path) + "&acontents=" + e(contents);
      int result = ((Integer)post(url, s.getBytes())).intValue();

            return result;
        }

        catch (IOException e) {
          System.out.println("IOException in AMtClient.sendFile: " + e.toString());
        }
    return AMtc.ERROR;
  }

  /**
     * Internal method used to complete the URL string with the type of data
     * transmitted.
     *
     * @param url url of the server
     * @param data data to be transmitted
     */
    private final Object post(URL url, byte[] data)
    throws IOException, MalformedURLException {

    try{
      ObjectInputStream in = post(url, data, "application/x-www-form-urlencoded");
      Object obj = in.readObject();
      in.close();
      return obj;
    }
    catch (ClassNotFoundException e){
      System.out.println("Exception in AMtClient.post: " + e.toString());
    }
    return null;
    }

    /**
     * Internal method used to complete the URL string with the type of data
     * transmitted. Method is used especially for downloading files from the WOW!
     * server and writes the downloaded file immediately to disk to minimize
     * memory usage.
     *
     * @param url url of the server
     * @param data data to be transmitted
     * @param f the file to write to
     * @param size the size of the file to download
     * @param bar The progressbar for this download
     */
    private int postDownload(URL url, byte[] data, File f, long size, JProgressBar bar)
    throws IOException, MalformedURLException {

        int sleep = AMtc.DOWNLOAD_SLEEP_TIME;

    BufferedInputStream in = null;
      DataOutputStream streamOut = null;
      bar.setMaximum(100);

      try {
      URLConnection servletConnection = url.openConnection();
      servletConnection.setDoInput(true);
      servletConnection.setDoOutput(true);
      servletConnection.setUseCaches(false);

      // set Content-Type
      servletConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

      // send request
      streamOut = new DataOutputStream(
        new java.io.BufferedOutputStream(servletConnection.getOutputStream()));

      streamOut.write(data);
      streamOut.flush();
      streamOut.close();
      streamOut = null;

          // Read the input from the servlet.(this is a series of bytes that need
      //  to be written to file directly.
          in = new BufferedInputStream(servletConnection.getInputStream());

      FileOutputStream out = new FileOutputStream(f);

      int partsize;
      int buffer = AMtc.CLIENT_BUFFER_SIZE;
      double readbytes = 0;
      double toread = new Long(size).doubleValue();
      double filesize = new Long(size).doubleValue();

      //send file in parts with size of buffer
      while (toread>0 && !cancelTransfer) {
        partsize = Math.min(buffer, in.available());
        //System.out.println(partsize);
        if (partsize == 0) {//wait a while
          try {
            Thread.sleep(sleep);
            //sleep *= 2;
          }
          catch (Exception e) {
            System.out.println("AMtClient.postDownload: " + e.toString());
          }
        }
        else {//read data with size partsize

          byte[] bytebuffer = new byte[partsize];

          in.read(bytebuffer, 0, partsize);
          out.write(bytebuffer);

          readbytes += partsize;
          toread -= partsize;

          bar.setValue((int)Math.floor((readbytes/filesize)*100));

          gui.repaintTable();
        }
      }

      out.flush();
      out.close();
      in.close();

      if (cancelTransfer) {
        f.delete();
        cancelTransfer = false;
        return AMtc.ERROR_USER_ABORTED;
      }
      return AMtc.NO_ERRORS;
      }

        catch(MalformedURLException murle) {
            throw murle;
        }

        catch(IOException ioe) {
        throw ioe;
        }
    }

    /**
     * Internal method used to send a string of text to the server and retrieve
     * an object of arbitrary type.
     *
     * @param url url of the server
     * @param data data to be sent to the server
     * @param contentType type of data being sent
     *
     * @return object of arbitrary type in reaction to the data request.
     */
    private final ObjectInputStream post(URL url, byte[] data, String
        contentType) throws IOException, MalformedURLException {

        ObjectInputStream result;

        DataOutputStream streamOut = null;

        try {
        URLConnection servletConnection = url.openConnection();
        servletConnection.setDoInput(true);
        servletConnection.setDoOutput(true);
        servletConnection.setUseCaches(false);

        // set Content-Type
        servletConnection.setRequestProperty("Content-Type", contentType);

        // send request
        streamOut = new DataOutputStream(
        new java.io.BufferedOutputStream(servletConnection.getOutputStream()));

        streamOut.write(data);
        streamOut.flush();
        streamOut.close();
        streamOut = null;

            // Read the input from the servlet.
            result = new ObjectInputStream(servletConnection.getInputStream());
        }

        catch(MalformedURLException murle) {
            throw murle;
        }

        catch(IOException ioe) {
        throw ioe;
        }

        return result;
    }
};