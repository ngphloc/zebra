/*

    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 

*/
/**
 * EditorApplet.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.generatelisteditor;

import javax.swing.*;
//
import java.net.*;
import java.io.*;
import java.util.*;
import vn.spring.WOW.util.*;
/**
 * Class to test the GenerateList_Editor.
 */
public class EditorApplet extends JApplet {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private URL home; // codebase of the applet
  private String FAuthorname; // name of the author to start the ConceptEditor for
  private Editor editor; // the ConceptEditor

  /**
   * Constructor
   * does nothing.
   */
  public EditorApplet() {
  }

  /**
   * Constructor
   * Creates a new ConceptEditor and starts it for an author.
   * @param codebase the codebase for the applet
   * @param authorname the name of the author to start the ConceptEditor for
   */
  public EditorApplet(URL codebase, String authorname) {
    home = codebase;
    FAuthorname = authorname;
    startEditor();
  }

  /**
   * Constructor
   * Creates a new ConceptEditor and starts it for an author. The ConceptEditor
   * is started with the specified file opened.
   * @param codebase the codebase for the applet
   * @param authorname the name of the author to start the ConceptEditor for
   * @param afilename The name of the .wow file to open. This file should be
   *                 located in the authorfiles folder of the specified
   *                 author. The name should be of form "file.wow".
   */
  public EditorApplet(URL codebase, String authorname, String afilename) {
    home = codebase;
    FAuthorname = authorname;
    startEditor();
    Editor.openFile(afilename, authorname);
  }

  public static boolean isOpen() {
      if (Editor.myinstance == null) return false;
      return Editor.myinstance.isOpen();
  }

  /**
   * Initializes the applet. Sets the applet's codebase in the
   * <Code>home</Code> field and prompts the author for a login. Starts the
   * ConceptEditor if login is successful, otherwise shows an access denied page
   * in a browser window.
   */
  public void init() {
      if (isOpen()) {
          JOptionPane.showMessageDialog(null,
                                        "Concept Editor is already open",
                                        "information",
                                        JOptionPane.OK_OPTION);
          return;
      }


    home = getCodeBase();
    AuthorLogin alogin = new AuthorLogin(home);

    if (!alogin.login()) {//login failed
        try {
            String codebase = home.toString();
            int index = codebase.lastIndexOf("/");
            index = codebase.substring(0, index).lastIndexOf("/");
            String base = codebase.substring(0, index+1);
            getAppletContext().showDocument(new URL(base+"accessdenied.html"), "_top");
          } catch(Exception e) { e.printStackTrace(); }
      }
      else {//login successful, start the conceptEditor
        FAuthorname = alogin.getUserName();
        startEditor();
        }
  }

  /**
   * Starts the ConceptEditor for author with name <Code>FAuthorname</Code>
   * and codebase <Code>home</Code> and sets it visible.
   * The ConceptEditor that is created is stored in field <Code>editor</Code>.
   */
  public void startEditor() {
        StringBuffer courseslist = new StringBuffer();
    try {
      String path = home.getPath();
      String temp = path.substring(1, path.length());
            int index = temp.indexOf("/");
            index++;
            String contextpath = path.substring(0, index);

      // Create an object we can use to communicate with the servlet
        URL servletURL = new URL(home.getProtocol()+"://"+home.getHost()+":"+
        home.getPort()+contextpath+"/servlet/CoursesList");
            URLConnection servletConnection = servletURL.openConnection();
            servletConnection.setDoOutput(true);  // to allow us to write to the URL
            servletConnection.setUseCaches(false);  // to ensure that we do contact
                                              // the servlet and don't get
                                              // anything from the browser's
                                              // cache
        // Write the message to the servlet
        PrintStream out = new PrintStream(servletConnection.getOutputStream());
        out.println(FAuthorname);
        out.close();

        // Now read in the response
        InputStream in = servletConnection.getInputStream();
            int chr;
            while ((chr=in.read())!=-1) {
            courseslist.append((char) chr);
        }
        in.close();

        }
    catch(IOException ioe) {System.out.println("Error: "+ioe.toString());}

    //retrieve list of all applications not owned by this author
    Vector v = new Vector();
        StringTokenizer tokenizer =
      new StringTokenizer(courseslist.toString().trim(), "|");

    while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            v.addElement(token);
        }

        //start the ConceptEditor
        editor = new Editor();
    editor.makeFrame(home, FAuthorname, v);
  }
}