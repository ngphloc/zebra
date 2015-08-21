//------------------------------------------------------------------------------
// Unit Name: AMtServer.java
// Author: T.J. Dekker, reviewed and modified by Loc Nguyen
// Date of Creation: 14-09-2008
// Purpose: Server side component for the AMt Application. This servlet serves
// all requests made by the client side.
//
// DOCUMENT CHANGES
//
// Date:           Author:             Change:
// -----------------------------------------------------------------------------
// 14-09-2008      T.J. Dekker         Creation
// 29-09-2008      T.J. Dekker         made inputstream sbIn and bIn local
//                                     variables for thread safety. Problem was
//                                     that while handling one inputstream, it
//                                     could be overwritten by another incoming
//                                     request.
// 4-11-2008       T.J. Dekker         Added copyFile method
//------------------------------------------------------------------------------

import java.io.*;
import java.net.*;
import java.nio.channels.FileChannel;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.*;

import vn.spring.WOW.AMt.AMtc;
import vn.spring.WOW.AMt.AMtCCC;
import vn.spring.WOW.AMt.TAMtFile;
import vn.spring.WOW.AMt.TCCCReport;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.config.AuthorsConfig;
import vn.spring.WOW.config.WowAuthor;
import vn.spring.WOW.exceptions.WOWException;
import vn.spring.zebra.um.OverlayBayesUM;

/**
 * Server side component for the AMt Application. This servlet serves all
 * requests made by the client side.
 * @author T.J. Dekker, changed by Phuoc-Loc Nguyen
 * @version 1.0.0
 */
public class AMtServer extends HttpServlet {

  //StringBuffer sbIn;//inputstream in stringbuffer format
  //byte[] bIn;//inputstream in byte[] format

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**
   * Called by the server (via the service method) to allow a servlet to handle a
   * GET request.This method inspects the sent parameters and calls the
   * corresponding method in AMtServer.
   * @param  request  an HttpServletRequest object that contains the request the
   *                  client has made of the servlet
   * @param  response an HttpServletResponse object that contains the response
   *                  the servlet sends to the client
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    //store inputstream
    Vector v = getInputStream(request);
    StringBuffer sbIn = (StringBuffer)v.get(0);
    byte[] bIn = (byte[])v.get(1);

    String method = getParam("amethod", sbIn);

    if (method.equals("login")) {
      // login an author
      String aauthorname = getParam("aauthorname", sbIn);
      String apassword = getParam("apassword", sbIn);
      Boolean result = new Boolean(login(aauthorname, apassword));
      sendObject(response, result);
    }
    else if (method.equals("addApp")) {
      // add an application
      String aauthorname = getParam("aauthorname", sbIn);
      String aappname = getParam("aappname", sbIn);
      Integer result = new Integer(addApp(aauthorname, aappname));
      sendObject(response, result);
    }
    else if (method.equals("delApp")) {
      // delete an application
      String aauthorname = getParam("aauthorname", sbIn);
      String aappname = getParam("aappname", sbIn);
      Integer result = new Integer(delApp(aauthorname, aappname));
      sendObject(response, result);
    }
    else if (method.equals("delAppDomain")) {
      // delete an applications domain model
      String aauthorname = getParam("aauthorname", sbIn);
      String aappname = getParam("aappname", sbIn);
      Integer result = new Integer(delAppDomain(aauthorname, aappname));
      sendObject(response, result);
    }
    else if (method.equals("upload")) {
      // upload a file
      String aauthorname = getParam("aauthorname", sbIn);
      String apath = getParam("apath", sbIn);
      String aappend = getParam("aappend", sbIn);
      byte[] afile = getFileParam("afile", sbIn, bIn);
      Integer result = new Integer(upload(aauthorname, afile, apath,
        (aappend.equals("1"))));
      sendObject(response, result);
    }
    else if (method.equals("download")) {
      // download a file
      String aauthorname = getParam("aauthorname", sbIn);
      String afilepath = getParam("afilepath", sbIn);
      /*byte[] result = */download(aauthorname, afilepath, response);
      //sendObject(response, result);
    }
    else if (method.equals("listAppFiles")) {
      // list all files in a folder
      String aauthorname = getParam("aauthorname", sbIn);
      String afolderpath = getParam("afolderpath", sbIn);
      TAMtFile[] result = listAppFiles(aauthorname, afolderpath);
      sendObject(response, result);
    }
    else if (method.equals("listAuthorFiles")) {
      // list all files in a folder located in the authors authorfiles folder
      String aauthorname = getParam("aauthorname", sbIn);
      String afolderpath = getParam("afolderpath", sbIn);
      TAMtFile[] result = listAuthorFiles(aauthorname, afolderpath);
      sendObject(response, result);
    }
    else if (method.equals("delete")) {
      // delete a file
      String aauthorname = getParam("aauthorname", sbIn);
      String apath = getParam("apath", sbIn);
      //System.out.println(apath);
      Integer result = new Integer(delete(aauthorname, apath));
      sendObject(response, result);
    }
    else if (method.equals("rename")) {
      // rename a file
      String aauthorname = getParam("aauthorname", sbIn);
      String apath = getParam("apath", sbIn);
      String anewname = getParam("anewname", sbIn);
      Integer result = new Integer(rename(aauthorname, apath, anewname));
      sendObject(response, result);
    }
    else if (method.equals("CCC")) {
      // perform a concept consistency check (CCC)
      String aauthorname = getParam("aauthorname", sbIn);
      String aappname = getParam("aappname", sbIn);
      TCCCReport result = CCC(aauthorname, aappname);
      sendObject(response, result);
    }
    else if (method.equals("addFolder")) {
      // add a folder
      String aauthorname = getParam("aauthorname", sbIn);
      String apath = getParam("apath", sbIn);
      Integer result = new Integer(addFolder(aauthorname, apath));
      sendObject(response, result);
    }
    else if (method.equals("addWowFile")) {
      //add a .wow file
      String aauthorname = getParam("aauthorname", sbIn);
      String aname = getParam("aname", sbIn);
      Integer result = new Integer(addWowFile(aauthorname, aname));
      sendObject(response, result);
    }
    else if (method.equals("addGafFile")) {
      //add a .gaf file
      String aauthorname = getParam("aauthorname", sbIn);
      String aname = getParam("aname", sbIn);
      Integer result = new Integer(addGafFile(aauthorname, aname));
      sendObject(response, result);
    }
    else if (method.equals("addBifFile")) {
        String aauthorname = getParam("aauthorname", sbIn);
        String aname = getParam("aname", sbIn);
        Integer result = new Integer(addBifFile(aauthorname, aname));
        sendObject(response, result);
      }
    else if (method.equals("getFile")) {
      //get a file
      String aauthorname = getParam("aauthorname", sbIn);
      String afilepath = getParam("afilepath", sbIn);
      StringBuffer result = getFile(aauthorname, afilepath);
      sendObject(response, result);
    }
    else if (method.equals("sendFile")) {
      //get a file
      String aauthorname = getParam("aauthorname", sbIn);
      String afilepath = getParam("afilepath", sbIn);
      String acontents = getParam("acontents", sbIn);
      Integer result = new Integer(sendFile(aauthorname, afilepath, acontents));
      sendObject(response, result);
    }
    else if (method.equals("copyFile")) {
      //copy a file
      String aauthorname = getParam("aauthorname", sbIn);
      String asourcepath = getParam("asource", sbIn);
      String atargetpath = getParam("atarget", sbIn);
      Integer result = new Integer(copyFile(aauthorname, asourcepath, atargetpath));
      sendObject(response, result);
    }
    else if (method.equals("changeProfile")) {
      //change authors profile
      String aauthorname = getParam("aauthorname", sbIn);
      String anewpassword = getParam("anewpassword", sbIn);
      String aname = getParam("aname", sbIn);
      Integer result = new Integer(changeProfile(aauthorname, anewpassword, aname));
      sendObject(response, result);
    }
    else if (method.equals("getName")) {
      //retrieve author name
      String aauthorname = getParam("aauthorname", sbIn);
      String result = getName(aauthorname);
      sendObject(response, result);
    }
    else { //unknown method
      System.out.println("Unknown method in AMtServer.doGet(): " + method);
      sendObject(response, null);
    }
  }

  /**
   * Called by the server (via the service method) to allow a servlet to handle a
   * POST request. This method just calls doGet();
   * @param  request  an HttpServletRequest object that contains the request the
   *                  client has made of the servlet
   * @param  response an HttpServletResponse object that contains the response
   *                  the servlet sends to the client
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
       doGet(request, response);
  }

 /**
  * Sends an Object back to the client.
  * @param  response  an HttpServletResponse object that contains the response
  *                   the servlet sends to the client
  * @param  returnobject the object that is to be sent to the client
  */
  private void sendObject(HttpServletResponse response, Object returnobject)
  {
    ObjectOutputStream out;
    try
    {
      out = new ObjectOutputStream(response.getOutputStream());

      out.writeObject(returnobject);
      out.flush();
      out.close();
    }

    catch (IOException e)
   { e.printStackTrace();
   }
  }


  /**
   * Logs in an author, meaning that is checked if the entered account data is
   * a valid author account
   * @param aauthorname the name of the author
   *        apassword   the password for the author
   * @return <Code>true</Code> if and only if the authoraccount exists
   */
  private boolean login(String aauthorname, String apassword) {
    AuthorsConfig aconf = new AuthorsConfig();
    WowAuthor author = null;
    String login = aauthorname;
    String passwd = apassword;

    if((login!=null) && (passwd!=null)) {
      author = aconf.GetAuthor(login);
      return ((author!=null) && author.checkPasswd(passwd));
    }

    return false;
  }

  /**
   * Constructs a list of all applications of an author
   * @param aauthorname the name of the author
   * @return A list of all applications of the author, or null if
   * no applications exist for this author or if the author does not exist.
   */
  private TAMtFile[] getAppList(String aauthorname) {
    AuthorsConfig aconf = new AuthorsConfig();
    WowAuthor author = aconf.GetAuthor(aauthorname);

    Vector apps = author.getCourseList();
    Object[] appArray = apps.toArray();
    Arrays.sort(appArray);

    //store apps in TAMtFile array
    TAMtFile[] files = new TAMtFile[appArray.length];
    for (int i = 0;i<files.length ;i++ ) {
      String app = (String)appArray[i];
      files[i] = new TAMtFile("/" + app, app, true, true);
    }

    return files;
  }

  /**
   * Adds an application for the specified author to its registered list of
   * applications and creates a folder for the application if it did not exist.
   * If the application is already registered with an author, an error code is
   * returned.
   * @param aauthorname the name of the author
   * @param aappname    the name of the application
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  private int addApp(String aauthorname, String aappname) {
    if (!aappname.matches("[a-zA-Z_][a-zA-Z_0-9]*")) return AMtc.ERROR_INVALID_APP_NAME;
    AuthorsConfig aconf = new AuthorsConfig();
    WowAuthor author = aconf.GetAuthor(aauthorname);

    Vector authors = new Vector(aconf.AuthorHash.values());

    Vector allApps = new Vector();
    Vector authorApps = author.getCourseList();

    for (int i = 0;i<authors.size() ;i++ ) {
      WowAuthor a = (WowAuthor)authors.get(i);
      allApps.addAll(a.getCourseList());

    }

    //check if application to be added is already registered for an author
    if (allApps.contains(aappname)) return AMtc.ERROR_APP_ALREADY_EXISTS;
    else {//add app to list, create folder if no folder exists
      authorApps.add(aappname);
      author.setCourseList(authorApps);

      String path = getServletContext().getRealPath(aappname);
      File f = new File(path);
      boolean b = f.mkdir();

      if (!b) //dir could not be created
        return AMtc.ERROR_COULD_NOT_CREATE_APP_DIR;

      //dir has been created, save new application to authors list.
      aconf.StoreConfig();

      return AMtc.NO_ERRORS;
    }
  }

  /**
   * Deletes an application for the specified author from its registered list
   * of applications and deletes the application's folder if it exists
   * @param aauthorname the name of the author
   * @param aappname    the name of the application
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  private int delApp(String aauthorname, String aappname) {
    AuthorsConfig aconf = new AuthorsConfig();
    WowAuthor author = aconf.GetAuthor(aauthorname);

    Vector apps = author.getCourseList();

    //check if application to be deleted is in courselist
    if (!apps.contains(aappname)) return AMtc.ERROR_NO_SUCH_REMOTE_APP;
    else {//delete app from list, delete folder if it exists
      apps.remove(aappname);
      author.setCourseList(apps);
      aconf.StoreConfig();
      int error = internalDelAppDomain(aappname);
      if (error != AMtc.NO_ERRORS) return error;

      String path = getServletContext().getRealPath(aappname);
      File f = new File(path);
      return rmDir(f);
    }
  }

  /**
   * Removes an applications domain model from the WOW!&nbsp;database.
   * @param aauthorname the name of the author
   * @param aappname    the name of the application
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  private int delAppDomain(String aauthorname, String aappname) {
    AuthorsConfig aconf = new AuthorsConfig();
    WowAuthor author = aconf.GetAuthor(aauthorname);

    Vector apps = author.getCourseList();

    //check if applications model to be deleted is in courselist
    if (!apps.contains(aappname)) return AMtc.ERROR_NO_SUCH_REMOTE_APP;
    else {//delete apps domain model
      return internalDelAppDomain(aappname);
    }
  }

  private int internalDelAppDomain(String aappname) {
    try {
      vn.spring.WOW.WOWDB.ConceptDB cdb = WOWStatic.DB().getConceptDB();
      Vector clist = cdb.getConceptList();
      for (int i=0;i<clist.size();i++) {
        String s = (String)clist.get(i);
        if (s.startsWith(aappname+".")) cdb.removeConcept(cdb.findConcept(s));
      }
    } catch (Exception e) {e.printStackTrace(); return AMtc.ERROR;}
    return AMtc.NO_ERRORS;
  }

  /**
   * Uploads a file to the server filesystem to the specified path
   * @param aauthorname the name of the author
   * @param afile the file to upload in byte[] format
   * @param apath The relative path on the server filesystem to upload to. Path
   *              must be of form: <Code>/app/folder1</Code> and relative to the
   *              wow folder on the server
   * @param aappend afile has to be appended to an existing file at apath.
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  private int upload(String aauthorname, byte[] afile, String apath,
    boolean aappend) {
     try {
        File f;

      f = new File(getServletContext().getRealPath(apath));

      if (f.exists() && !aappend) return AMtc.ERROR;

      FileOutputStream out = new FileOutputStream(f,aappend);

      out.write(afile);
      out.flush();
      out.close();

      return AMtc.NO_ERRORS;
    }
    catch(Exception e) {
      System.out.println("error in AMtServer.upload(): " + e.toString());
      return AMtc.ERROR;
    }
  }

  /**
   * Downloads a file from the server filesystem at the specified path and sends
   * it back to the client.
   * @param aauthorname the name of the author
   * @param afilepath The relative path on the server filesystem to download.
   *                  Path must be of form: <Code>/app/folder1.xhtml</Code> and
   *                  relative to the wow folder on the server
   * @param response the response back to the client
   */
  private void download(String aauthorname, String afilepath,
                                                 HttpServletResponse response) {
    File f = new File(getServletContext().getRealPath(afilepath));

    if (f.exists() && !f.isDirectory()) {
      try {
        FileInputStream in = new FileInputStream(f);
        BufferedOutputStream out =
          new BufferedOutputStream(response.getOutputStream());
        int partsize;
        int buffer = AMtc.SERVER_BUFFER_SIZE;

        while (in.available() > 0) {

          partsize = Math.min(buffer, in.available());
          //System.out.println(partsize);
          byte[] bytebuffer = new byte[partsize];
          in.read(bytebuffer, 0, partsize);

          //send part back to client
          try { out.write(bytebuffer);}
          catch (IOException e) {in.close(); return;}
        }
        out.flush();
        out.close();
        in.close();
      }
      catch (Exception e) { System.out.println("AMtServer.download(): " +
        "Error retrieving file: " + e.toString());
      }
    }
    //nonexisting file or directory
  }

  /**
   * Lists all files that exist in the specified folder on the server
   * filesystem. If the path = "/", this returns a list of all the authors
   * applications and also creates a directory for an application if there
   * exists no directory for the application.
   * @param aauthorname the name of the author
   * @param afolderpath The path on the server filesystem to list the files
   *                    for. Path must be of form <Code>/app/folder1</Code> and
   *                    relative to the wow folder on the server
   * @return A list of all files and directories located in the folder at
   * afolderpath on the server filesystem, or null if the specified folder does
   * not exist on the server. null is also returned if the specified folder path
   * leads outside the wow context dir or to an application folder that the user
   * does not own.
   */
  private TAMtFile[] listAppFiles(String aauthorname, String afolderpath) {
    String fs = System.getProperty("file.separator");
    String path = afolderpath;
    String realpath = getServletContext().getRealPath("");
    StringBuffer s = new StringBuffer(path);
    File[] files;
    TAMtFile[] result;

    //replace all instances of / by the file separator for platform independancy
    if (!"/".equals(fs)) {
      while (s.indexOf("/") != -1) {
        s.replace(s.indexOf("/"), s.indexOf("/")+1, fs);
      }
    }
    path = getServletContext().getRealPath(s.toString());

    File f = new File(path);
    File rf = new File(realpath);

    //check if path is valid
    try {
      String canprf = rf.getCanonicalPath();
      String canpf = f.getCanonicalPath();
      //check if root, if so return all apps and create directory for all apps
      //if no directory exists
      if (canpf.equals(canprf)) {
        result =  getAppList(aauthorname);
        for (int i = 0; i<result.length;i++ ) {
          File appdir = new File(path+result[i].getName());
          appdir.mkdir();
        }
        return result;
      }

      //check if path is outside wow context dir
      if (!canpf.startsWith(canprf))
        return null;
      //check if path starts with a dir that is not an application of the user
      if (canpf.startsWith(canprf)) {
        String temppath = canpf.substring(canprf.length()+1);
        //get first dir
        if (temppath.indexOf(File.separator) != -1)
          temppath = temppath.substring(0, temppath.indexOf(File.separator));

        //compare dir against appnames
        TAMtFile[] apps = getAppList(aauthorname);
        boolean exists = false;

        for (int i = 0; i<apps.length ;i++ ) {
          exists |= apps[i].getName().equals(temppath);
        }
        if (!exists) return null;
      }
    }
    catch (IOException e) {
      return null;
    }

    //valid path, list files if requested dir exists
    if (f.exists() && f.isDirectory()) {
      files = f.listFiles();
      files = sort(files);

       //store filedata in result
       result = new TAMtFile[files.length];
       for (int i = 0;i<result.length ;i++ ) {
         File a = files[i];
         TAMtFile af = new TAMtFile(afolderpath +"/"+ a.getName(), a.getName(),
           a.length(), a.lastModified(), a.isDirectory(), false);
         result[i] = af;
       }
       return result;
    }

    return null;
  }

  /**
   * Lists all files that exist in the specified folder in an authors
   * authorfiles folder on the server filesystem.
   * @param aauthorname the name of the author
   * @param afolderpath The path on the server filesystem to list the files
   * for. Path must be of form <Code>/folder1/folder2</Code> and relative to the
   * authorfiles folder
   * @return A list of all files and directories located in the folder at
   * afolderpath on the server filesystem, or null if the specified folder does
   * not exist on the server. null is also returned if the specified folder path
   * leads outside the authorfiles context dir
   */
  private TAMtFile[] listAuthorFiles(String aauthorname, String afolderpath) {
    String fs = System.getProperty("file.separator");
    String authorfilespath = WOWStatic.AUTHORFILESPATH;
    String path = authorfilespath + aauthorname + afolderpath;
    String realpath = getServletContext().getRealPath(
                        fs + "author" + fs + "authorfiles" + fs + aauthorname);
    StringBuffer s = new StringBuffer(path);
    File[] files;
    TAMtFile[] result;

    //replace all instances of / by the file separator for platform independancy
    if (!"/".equals(fs)) {
      while (s.indexOf("/") != -1) {
        s.replace(s.indexOf("/"), s.indexOf("/")+1, fs);
      }
    }
    path = getServletContext().getRealPath(s.toString());

    File f = new File(path);
    File rf = new File(realpath);

    //check if path is valid
    try {
      String canprf = rf.getCanonicalPath();
      String canpf = f.getCanonicalPath();

      //check if path is outside authorfiles context dir
      if (!canpf.startsWith(canprf))
        return null;
    }
    catch (IOException e) {
      return null;
    }

    //valid path, list files if requested dir exists
    if (f.exists() && f.isDirectory()) {
      files = f.listFiles();
      files = sort(files);

       //store filedata in result
       result = new TAMtFile[files.length];
       for (int i = 0;i<result.length ;i++ ) {
         File a = files[i];
         TAMtFile af = new TAMtFile(
                  afolderpath + (afolderpath.equals("/")?"":"/") + a.getName(),
                  a.getName(),
                  a.length(),
                  a.lastModified(),
                  a.isDirectory(),
                  false
         );
         result[i] = af;
       }
       return result;
    }

    return null;
  }

 /**
  * Deletes a file or folder at the specified path from the server
  * filesystem. If the specified path is a folder, all contents of the folder
  * are deleted also.
  * @param aauthorname the name of the author
  * @param apath The path to delete. Path must be of form
  * <Code>/app/folder1</Code> and relative to the wow folder on the server.
  * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
  */
  private int delete(String aauthorname, String apath) {
    File f = new File(getServletContext().getRealPath(apath));
    if (f.exists()) {
      if (f.isDirectory()) return rmDir(f);
        if (!f.delete()) return AMtc.ERROR_COULD_NOT_DELETE_FILE;
        else return AMtc.NO_ERRORS;
    }
    return AMtc.ERROR_COULD_NOT_DELETE_FILE;
  }

  /**
   * Renames a file or folder at the WOW! server filesystem.
   * @param aauthorname the name of the author
   * @param apath The path to rename. Path must be of form
   * <Code>/app/folder1</Code> and relative to the wow folder on the server.
   * @param anewname the new name of the file or folder located at apath
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  private int rename(String aauthorname, String apath, String anewname) {
    File f = new File(getServletContext().getRealPath(apath));


    if (apath.lastIndexOf("/") == 0) {//application
      return renApp(aauthorname, apath.substring(1), anewname);
    }

    if (f.exists()) {
      String newpath = apath.substring(0, apath.lastIndexOf(f.getName()));
      File nf = new File(getServletContext().getRealPath(newpath+anewname));
      if (nf.exists()) return AMtc.ERROR_FILE_ALREADY_EXISTS;
      if (!f.renameTo(nf)) return AMtc.ERROR_COULD_NOT_RENAME_FILE;

    }
    return AMtc.NO_ERRORS;
  }

  /**
   * Constructs a CCC report for the specified application
   * @param aauthorname the name of an author
   * @param aapname the name of an application
   * @return the CCC report for the application
   */
  private TCCCReport CCC(String aauthorname, String aappname) {
    AMtCCC ccc = new AMtCCC();
    return ccc.performCheck(aauthorname, aappname);
  }

  /**
   * Removes a directory and all its contents
   * @param f the dir to remove
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  private int rmDir(File f) {
    try {
      if (!f.exists()) return AMtc.ERROR_NO_SUCH_REMOTE_DIR;
      int error = AMtc.NO_ERRORS;
      File[] files = f.listFiles();

      for (int i = 0;i<files.length ;i++ ) {
        if (files[i].isDirectory()) {
          int j= rmDir(files[i]);
          if (j != AMtc.NO_ERRORS) error = j;
        }
        else if (!files[i].delete()) error = AMtc.ERROR_COULD_NOT_DELETE_FILE;
      }
      if (!f.delete()) error = AMtc.ERROR_COULD_NOT_DELETE_DIR;
      return error;
    }
    catch (Exception e) {
      System.out.println("Exception in AMtServer.rmDir: " + e.toString());
      return AMtc.ERROR;
    }
  }

  /**
   * Adds a folder to the server filesystem.
   * @param aauthorname the name of an author
   * @param apath The path of the folder to add. Path must be of form
   * <Code>/app/folder1</Code> and relative to the wow folder on the server.
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  private int addFolder(String aauthorname, String apath) {
    String fs = System.getProperty("file.separator");
    StringBuffer s = new StringBuffer(apath);

    if (apath.lastIndexOf("/") == 0)
      return addApp(aauthorname, apath.substring(1));

    //replace all instances of / by the file separator for platform independency
    if (!"/".equals(fs)) {
      while (s.indexOf("/") != -1) {
        s.replace(s.indexOf("/"), s.indexOf("/")+1, fs);
      }
    }
    apath = getServletContext().getRealPath(s.toString());

    File f = new File(apath);
    if (f.exists()) return AMtc.ERROR_FOLDER_ALREADY_EXISTS;
    else if (!f.mkdir()) return AMtc.ERROR_COULD_NOT_CREATE_DIR;
    else return AMtc.NO_ERRORS;
  }

    /**
   * Adds an <Code>.wow</Code> file in the authorfiles folder of an author.
   * @param aauthorname the name of an author
   * @param aname the name for the <Code>.wow</Code> file to create. It is
   * recommended that this name is the same as an application name of the
   * author.
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
     */
  private int addWowFile(String aauthorname, String aname) {
    String authorfilespath = WOWStatic.AUTHORFILESPATH + aauthorname + File.separator;
    String apath = getServletContext().getRealPath(authorfilespath + aname + ".wow");
    File f = new File(apath);

    try {
      if (f.exists()) return AMtc.ERROR_FILE_ALREADY_EXISTS;
      else if (!f.createNewFile()) return AMtc.ERROR_COULD_NOT_CREATE_FILE;
      else {//send data from template .wow file to newly created file f
        String templatePath = getServletContext().getRealPath(AMtc.WOWTEMPLATEPATH);
        File tf = new File(templatePath);
        copyFile(tf, f);
        return AMtc.NO_ERRORS;
      }
    }
    catch (Exception e) {
      System.out.println("AMtServer.addWowFile(): " + e.toString());
      return AMtc.ERROR;
    }
  }

    /**
   * Adds an <Code>.gaf</Code> file in the authorfiles folder of an author.
   * @param aauthorname the name of an author
   * @param aname the name for the <Code>.gaf</Code> file to create. It is
   * recommended that this name is the same as an application name of the
   * author.
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
     */
  private int addGafFile(String aauthorname, String aname) {
    String authorfilespath = WOWStatic.AUTHORFILESPATH + aauthorname + File.separator;
    String apath = getServletContext().getRealPath(authorfilespath + aname + ".gaf");
    File f = new File(apath);

    try {
      if (f.exists()) return AMtc.ERROR_FILE_ALREADY_EXISTS;
      else if (!f.createNewFile()) return AMtc.ERROR_COULD_NOT_CREATE_FILE;
      else {//send data from template .gaf file to newly created file f
        String templatePath = getServletContext().getRealPath(AMtc.GAFTEMPLATEPATH);
        File tf = new File(templatePath);
        createGafFile(tf, f);
        return AMtc.NO_ERRORS;
      }
    }
    catch (Exception e) {
      System.out.println("AMtServer.addGafFile(): " + e.toString());
      e.printStackTrace();
      return AMtc.ERROR;
    }
  }

  private int addBifFile(String aauthorname, String aname) {
	    String authorfilespath = WOWStatic.AUTHORFILESPATH + aauthorname + File.separator;
	    String apath = getServletContext().getRealPath(authorfilespath + aname + OverlayBayesUM.OBUM_EXT);
	    File f = new File(apath);

	    try {
	      if (f.exists()) return AMtc.ERROR_FILE_ALREADY_EXISTS;
	      else if (!f.createNewFile()) return AMtc.ERROR_COULD_NOT_CREATE_FILE;
	      else {//send data from template file to newly created file f
	        String templatePath = getServletContext().getRealPath(AMtc.BIFTEMPLATEPATH);
	        File tf = new File(templatePath);
	        createBifFile(tf, f);
	        return AMtc.NO_ERRORS;
	      }
	    }
	    catch (Exception e) {
	      System.out.println("AMtServer.addBifFile(): " + e.toString());
	      e.printStackTrace();
	      return AMtc.ERROR;
	    }
	  }

  /**
   * Retrieves a file from the WOW! server filesystem at the specified path.
   * WARNING: this method should only be used to retrieve small non-binary
   * files. Use the <Code>download</Code> method if this is not the case.
   * @param aauthorname the name of an author
   * @param afilepath The path on the WOW! server filesystem to download.
   * Path must be of form: <Code>/app/file1.xhtml</Code> and relative to the wow
   * folder on the server.
   * @param afilemode mode indicating location of the file, which is either
   * AMtc.APP_FILES_MODE or AMtc.AUTHOR_FILES_MODE
   * @return the requested file, or null if an error occurred
   */
  private StringBuffer getFile(String aauthorname, String afilepath) {
    File f = new File(getServletContext().getRealPath(afilepath));

    if (f.exists() && !f.isDirectory()) {
      try {
        FileInputStream in = new FileInputStream(f);

        int partsize;
        long filesize = f.length();
        long readbytes = 0L;
        long toread = filesize;
        int buffer = AMtc.SERVER_BUFFER_SIZE;
        StringBuffer s = new StringBuffer("");

        while (toread > 0) {

          partsize = Math.min(buffer, in.available());
          //System.out.println(partsize);
          byte[] bytebuffer = new byte[partsize];
          in.read(bytebuffer, 0, partsize);

          s.append(new String(bytebuffer));

          readbytes += partsize;
          toread -= partsize;
        }
        in.close();

        return s;
      }
      catch (Exception e) { System.out.println("AMtServer.getFile(): " +
        "Error retrieving file: " + e.toString());
      }
    }
    //nonexisting file or directory
    return null;
  }


  /**
   * Uploads a file to the WOW! server filesystem at the specified path.
   * WARNING: this method should only be used to upload small non-binary
   * files. Use the <Code>upload</Code> method if this is not the case.
   * @param aauthorname the name of an author
   * @param afilepath The path on the WOW! server filesystem to upload to.
   * Path must be of form: <Code>/app/file1.xhtml</Code> and relative to the wow
   * folder on the server.
   * @param contents the contents of the file to upload
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  private int sendFile(String aauthorname, String afilepath, String contents) {
    try {
        File f;

      f = new File(getServletContext().getRealPath(afilepath));

      boolean exists = false;

      if (f.exists()) {
        exists = !f.delete();
      }

      if (!exists) {
        return upload(aauthorname, contents.getBytes(), afilepath, false);
      }

    }
    catch(Exception e) {
      System.out.println("error in AMtServer.sendFile(): " + e.toString());
    }

    return AMtc.ERROR;
  }

  /**
   * Copies a file or folder at the WOW! server filesystem.
   * @param asource sourcepath for the file to copy Path must be of form
   * <code>/app/folder1/abc.xhtml</code> and relative to the wow folder on the
   * server.
   * @param atarget the path of the folder to copy the file into. Path must be
   * of form <code>/app/folder1</code> and relative to the wow folder on the
   * server.
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  private int copyFile(String aauthorname, String asourcepath,
                                                           String atargetpath) {
    try {
      //System.out.println("in copyfile at server: " + asourcepath +" to " +atargetpath);
      String name = asourcepath.substring(asourcepath.lastIndexOf("/"));
        File source = new File(getServletContext().getRealPath(asourcepath));
        File target = new File(getServletContext().getRealPath(atargetpath+"/" + name));

      //handle all illegal cases for copying.
      if (!source.exists()) return AMtc.ERROR;
      if (source.equals(target)) return AMtc.ERROR_COPY_TARGET_IS_SOURCE;
      if (!source.isDirectory() && (atargetpath.equals("/")))
                                 return AMtc.ERROR_CAN_NOT_UPLOAD_FILES_TO_ROOT;
      if (source.isDirectory() && (atargetpath.equals("/")))
                                 return addApp(aauthorname, source.getName());
      if (!target.createNewFile()) return AMtc.ERROR;

      //both files exist and differ from each other
      copyFile(source, target);
    }
    catch(Exception e) {
      System.out.println("error in AMtServer.copyFile(): " + e.toString());
      return AMtc.ERROR;
    }

    return AMtc.NO_ERRORS;
  }


    /**
     * Decodes a string in UTF-8 format.
     * @param s The string to decode
     * @return The decoded string.
     */
    private String d(String s) {
      try {
      return URLDecoder.decode(s, "UTF-8");
    }
    catch (Exception e) { System.out.println("Exception in AMtServer.d(): " +
      e.toString()); return "";
    }
    }

  /**
   * Reads the inputstream and retrieves it in byte[] and StringBuffer
   * representation respectively.
   * @param request the HTTPRequest to read
   * @throws IOException Throws an IOException if there were error reading the
   * inputstream.
   * @return A Vector containing two items, The first being the inputStream
   * in StringBuffer format, the second being the inputStream in byte[] format.
   */
  private Vector getInputStream(HttpServletRequest request) throws IOException {
    int contentlength = request.getContentLength();
    ServletInputStream in = request.getInputStream();
    byte[] bytebuffer = new byte[contentlength];
    StringBuffer sbIn = new StringBuffer("");
    byte[] bIn = new byte[contentlength];

    //read inputstream
    int toread = contentlength;
    int totalread = 0;

    while (toread > 0) {
      int readpart = in.readLine(bytebuffer, 0, contentlength);

      if (readpart != -1) {
        sbIn.append(new String(bytebuffer, 0, readpart));

        System.arraycopy(bytebuffer, 0, bIn, totalread, readpart);
      }
      else {
        System.out.println(
          "AMtServer: error reading inputstream, stream is empty.");
      }
      toread -= readpart;
      totalread += readpart;
    }
    in.close();

    Vector v = new Vector();
    v.add(sbIn);
    v.add(bIn);

    return v;
  }

    /**
     *  Returns the value of parameter par from inputstream sbIn
     *  @param par the parameter to retrieve the value of
     *  @param sbIn the inputStream in stringBuffer format
     *  @return The parameter value of parameter par or an empty string if there
   *  is no such parameter in inputstream sbIn
     */
  private String getParam(String par, StringBuffer sbIn) {
    int beginIndex = sbIn.indexOf(par + "=")+par.length()+1;
    int endIndex = sbIn.indexOf("&", beginIndex);
    if (endIndex == -1) endIndex = sbIn.length();
    if (beginIndex == -1) return "";
    String result = sbIn.substring(beginIndex, endIndex);
    return d(result);
    }

    /**
     *  Returns the parameter par from inputstream sbIn.
     *  Thie is a special case for file param at end of normal parameters
     *  for upload purposes. Retrieves the file in byte[] representation
     *  @param par The parameter to retrieve a file for. The parameter should be
     *  the last parameter of the inputstream. This is necessary, because illegal
     *  characters may occur in the binary file data, which can make parameters
     *  occurring after the file unaccessible.
     *  @param sbIn the inputStream in stringBuffer format
     *  @param bIn the inputStream in byte[] format
     *  @return the file in byte[] representation
     */
  private byte[] getFileParam(String par, StringBuffer sbIn, byte[] bIn) {
    int beginIndex = sbIn.indexOf(par + "=")+par.length()+1;
    int endIndex = sbIn.length();

    byte[] result = new byte[endIndex-beginIndex];
    System.arraycopy(bIn, beginIndex, result, 0, result.length);

    return result;
    }

  /**
   * Sorts an array of files with directories first. Ordering is done
   * alpabetically in ascending order ignoring case.
   * @param f the array to sort
   * @return the sorted array
   */
    private File[] sort(File[] f) {
    File[] files = f;
    Arrays.sort(files, new Comparator() {
      public int compare(Object a, Object b) {
        File filea = (File)a;
        File fileb = (File)b;
        //--- Sort directories before files,
        //    otherwise alphabetical ignoring case.
        if (filea.isDirectory() && !fileb.isDirectory()) {
            return -1;
        } else if (!filea.isDirectory() && fileb.isDirectory()) {
            return 1;
        } else {
            return filea.getName().compareToIgnoreCase(fileb.getName());
        }
      }
    });

    return files;
    }

  /**
   * Copies all contents of one file to another file, thus creating two
   * identical files.
   * @param from the file to copy from (source)
   * @param to the file to copy to (destination)
   */
    private void copyFile(File from, File to)
    throws IOException, FileNotFoundException {
      //System.out.println("copying data of files");
    FileInputStream in = new FileInputStream(from);
    FileOutputStream out = new FileOutputStream(to);

    FileChannel cin = in.getChannel();
    FileChannel cout = out.getChannel();

    cin.transferTo(0, cin.size(), cout);

    in.close();
    out.flush();
    out.close();
    }

  /**
   * Renames an application for the specified author. Renames the folder
   * of the application as well.
   * TODO: rename all authoring files for this application as well as name of
   * old app inside files...
   * If the application is already registered with an author, an error code is
   * returned.
   * @param aauthorname the name of the author
   * @param aappname    the name of the application to rename
   * @param anewname    the new name for the application
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  private int renApp(String aauthorname, String aappname, String anewname) {
    if (!anewname.matches("[a-zA-Z_][a-zA-Z_0-9]*")) return AMtc.ERROR_INVALID_APP_NAME;
    AuthorsConfig aconf = new AuthorsConfig();
    WowAuthor author = aconf.GetAuthor(aauthorname);

    Vector authors = new Vector(aconf.AuthorHash.values());

    Vector allApps = new Vector();
    Vector authorApps = author.getCourseList();

    for (int i = 0;i<authors.size() ;i++ ) {
      WowAuthor a = (WowAuthor)authors.get(i);
      allApps.addAll(a.getCourseList());
    }

    //check if application to be added is already registered for an author
    if (allApps.contains(anewname)) return AMtc.ERROR_APP_ALREADY_EXISTS;
    String newpath = getServletContext().getRealPath(anewname);
    File nf = new File(newpath);
    if (nf.exists())
      return AMtc.ERROR_COULD_NOT_RENAME_APP_DIR;
    else {//rename app, rename application folder

      String oldpath = getServletContext().getRealPath(aappname);
      File of = new File(oldpath);
      boolean b = of.renameTo(nf);

      if (!b) //dir could not be renamed
        return AMtc.ERROR_COULD_NOT_RENAME_APP_DIR;

      //dir has been renamed, save new application to authors list.
      authorApps.add(anewname);
      authorApps.remove(aappname);
      author.setCourseList(authorApps);
      aconf.StoreConfig();

      //TODO: rename authorfiles as well

      return AMtc.NO_ERRORS;
    }
  }

  /**
   * Creates a new .gaf file from a .gaf template. This essentially means that
   * the filename that is stored in the .gaf template is changed to the new
   * filename.
   * @param from the .gaf template to copy from (source)
   * @param to the file to copy to (destination)
   */
    private void createGafFile(File from, File to)
    throws IOException, FileNotFoundException {
    FileInputStream in = new FileInputStream(from);
    FileOutputStream out = new FileOutputStream(to);

    StringBuffer s = new StringBuffer("");
    long filesize = from.length();
    long readbytes = 0L;
    long toread = filesize;
    int partsize;

    //copy file in parts with size of buffer
    while (toread > 0) {
      partsize = in.available();
      byte[] bytebuffer = new byte[partsize];
      in.read(bytebuffer, 0, partsize);

      s.append(new String(bytebuffer, 0, partsize));

      readbytes += partsize;
      toread -= partsize;
    }
    in.close();

    String name = to.getName().substring(0, to.getName().lastIndexOf(".gaf"));
    //replace template name with name
    String first = "<concept_name>template</concept_name>";
    String firstrepl = "<concept_name>" + name + "</concept_name>";
    String second = "<node_name>template</node_name>";
    String secondrepl = "<node_name>" + name + "</node_name>";

    s.replace(
      s.indexOf(first),
      s.indexOf(first)+first.length(),
      firstrepl
    );

    s.replace(
      s.indexOf(second),
      s.indexOf(second)+second.length(),
      secondrepl
    );

    out.write(s.toString().getBytes());
    out.flush();
    out.close();
    }
    private void createBifFile(File from, File to) throws WOWException {
    	String error = "Not implement function AMtServer.createBifFile yet.\n" +
    		"Caused by AMtClientGUI requires AMtServer to add an Bayes file when it doesn't exist.\n" +
    		"AMtClientGUI calls AMtClientGUI.mniOpenBifActionPerformed when Bayes file choosen and AMtClientGUI.mniOpenBifActionPerformed calls AMtClientGUI.openBifFile.\n" + 
    		"Again, AMtClientGUI.openBifFile calls AMtClient.addBifFile (AMtClientGUI contains variable AMt having type AMtClient).\n" +
    		"AMtClient.addBifFile calls AMtServer through URL connection e.g: http://host/wow/AmtServer?amethod=addBifFile&aauthorname=author&aname=tutorial" +
    		"AMtServer.doGet calls AMtServer.addBifFile, consequently calls AMtServer.createBifFile (here) if Bayes file not exists";
    	System.out.println(error);
    	JFrame frame = new JFrame("AMtServer.createBifFile error");
    	JOptionPane.showMessageDialog(frame, error);
    	throw new WOWException(error);
    }

  /**
   * Changes the profile of an author. This means that the author's password
   * and name are updated with new values.
   * @param aauthorname the loginname of the author
   * @param anewpassword the (new) password for the author
   * @param aname the name of the author
   * @return An error code, see the error code listing in {@link AMtc} for all
   * possible error codes.
   */
  private int changeProfile(String aauthorname, String anewpassword, String aname) {
    AuthorsConfig aconf = new AuthorsConfig();
    WowAuthor author = aconf.GetAuthor(aauthorname);

    try {
      author.setUnHashed(anewpassword);
      author.setName(aname);
      aconf.RemoveAuthor(aauthorname);
      aconf.PutAuthor(author);
      aconf.StoreConfig();
    }
    catch (Exception e) {
      System.out.println("Exception in AMtServer.changeProfile:");
      e.printStackTrace();
      return AMtc.ERROR;
    }

      return AMtc.NO_ERRORS;
    }

  /**
   * Retrieves the name of an author.
   * @param aauthorname the loginname of the author
   * @return name of the author
   */
  private String getName(String aauthorname) {
    AuthorsConfig aconf = new AuthorsConfig();
    WowAuthor author = aconf.GetAuthor(aauthorname);

    if (author == null) return null;
    return author.getName();
    }


};