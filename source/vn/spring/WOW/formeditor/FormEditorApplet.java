/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/

/**
 * FormEditorApplet.java 1.0 August 30, 2008.
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.formeditor;

import java.awt.*;
import java.net.*;
import javax.swing.*;

import vn.spring.WOW.util.*;


/**
 * Class to test the FormEditor.
 *
 *
 */
public class FormEditorApplet extends JApplet {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
URL home; //codebase of the applet
  String FAuthorname; //name of the author to start the formeditor for
  FormEditor editor; //the formeditor

  /**
   * Constructor
   * does nothing.
   */
  public FormEditorApplet() {
  }

  /**
   * Constructor
   * Creates a new FormEditor and starts it for an author.
   * @param codebase the codebase for the applet
   * @param authorname the name of the author to start the formeditor for
   */
  public FormEditorApplet(URL codebase, String authorname) {
    home = codebase;
    FAuthorname = authorname;
    startEditor();
  }

  /**
   * Constructor
   * Creates a new FormEditor and starts it for an author. The FormEditor is
   * started with the specified file opened.
   * @param codebase the codebase for the applet
   * @param authorname the name of the author to start the formeditor for
   * @param filename The name of the .frm file to open. This file should be
   *                 located in the authorfiles folder of the specified
   *                 author. The name should be of form "file.frm".
   * @param filepath The path on the WOW! server filesystem to download.
   * Path must be of form: <Code>/app/file1.frm</Code> and relative to the
   * wow folder on the server.
   * @param filemode the mode indicating location of the file, which is
   */
  public FormEditorApplet(URL codebase, String authorname, String filename,
                                            String filepath, int filemode) {
    home = codebase;
    FAuthorname = authorname;
    startEditor();
    editor.openFile(authorname, filename, filepath, filemode);
  }

  /**
   * Initializes the applet. Sets the applet's codebase in the
   * <Code>home</Code> field and prompts the author for a login. Starts the
   * form editor if login is successful, otherwise shows an access denied page
   * in a browser window.
   */
  public void init() {
    home = getCodeBase();
    AuthorLogin alogin = new AuthorLogin(home);

    if (!alogin.login()) {//login failed
      try {
        String codebase = home.toString();
        int index = codebase.lastIndexOf("/");
        index = codebase.substring(0, index).lastIndexOf("/");

        String base = codebase.substring(0, index + 1);
        getAppletContext()
            .showDocument(new URL(base + "accessdenied.html"), "_top");
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    else {//login successful, start the formeditor
      FAuthorname = alogin.getUserName();
      startEditor();
    }
  }

  /**
   * Starts the FormEditor for author with name <Code>FAuthorname</Code>
   * and codebase <Code>home</Code> and sets it visible.
   * The FormEditor that is created is stored in field <Code>editor</Code>.
   */
  private void startEditor() {
    editor = new FormEditor("Form Editor", home, FAuthorname);
    editor.pack();

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int w = FormEditor.windowWidth + 10;
    int h = FormEditor.windowHeight + 10;
    editor.setLocation((screenSize.width / 3) - (w / 2),
                    (screenSize.height / 2) - (h / 2));
    editor.setSize(w, h);
    editor.setVisible(true);
  }
}