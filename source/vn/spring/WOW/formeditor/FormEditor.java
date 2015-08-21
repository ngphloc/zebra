/*
   This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University
   WOW! is also open source software; 
   
 */

/**
 * FormEditor.java 1.0, August 30, 2008.
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.formeditor;

import vn.spring.WOW.AMt.AMtc;
import vn.spring.WOW.AMt.AMtClient;
import vn.spring.WOW.AMt.ServerFileChooser;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleEditableText;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;

import java.io.*;

import java.net.*;

import java.util.*;


/**
 * Class for construction and working of the FormEditor.
 *
 */
public class FormEditor extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final boolean compress = true;
    public static final int windowHeight = 460;
    public static final int leftWidth = 300;
    public static final int rightWidth = 340;
    public static final int windowWidth = leftWidth + rightWidth;
    public static String userName = "";
    private URL home;
    private String address;
    public GenerateListData data; //NOT USED
    public FormProperties prop;
    private int countElem = 0;
    public String formpath = new String();
    public String formeditorpath = new String();
    private String lastSelectedText = "";
    private Stack editActions;
    private JFrame frm;      //this frame

    private String filepath; //path for the currently opened file
    private int filemode;    //filemode for the currently opened file

    // Variables declaration
    private JMenuBar mb;
    private JMenu mnFile;
    private JMenu mnEdit;
    private JMenu mnView;
    private JMenu mnLF;
    private JMenu mnHelp;
    private JMenuItem mniNew;
    private JMenuItem mniOpen;
    private JMenuItem mniSaveAs;
    private JMenuItem mniSave;
    private JMenuItem mniExit;
    private JMenuItem mniUndo;
    private JMenuItem mniCut;
    private JMenuItem mniCopy;
    private JMenuItem mniPaste;
    private JMenuItem mniDelete;
    private JMenuItem mniContent;
    private JMenuItem mniAbout;
    private JMenuItem mniLoadWOW; // open WOW! application
    private JTabbedPane tabXmlPreview;
    private JEditorPane edpXml;
    private JEditorPane edpPreview;
    private JScrollPane spnXml;
    private JScrollPane spnPreview;
    private JScrollPane spnConcepts;
    public JTree triConcepts;
    private JToolBar tb;
    private JLabel lblElement;
    private JButton btnInput;
    private JButton btnSelect;
    private JButton btnOption;
    private JButton btnButton;
    private JButton btnDescription;
    private String dirname;
    //private String fname = "";   //NOT USED

    /**
     * Creates new frame FormEditor.
     *
     * @param name gives a name to the FormEditor
     * @param base provides the codebase of the applet running this frame
     * @param inUserName name of the author accessing the form editor
     * @see javax.swing.JFrame
     */
    public FormEditor(String name, URL base, String inUserName) {
//      try {
//          UIManager.setAndFeel(UIManager.getSystemLookAndFeelClassName());
//      } catch (ClassNotFoundException exc) {
//          System.err.println("Can't find System look and feel: " + exc);
//      } catch (Exception exc) {
//          System.err.println("Problem with look and feel: " + exc);
//      }
      frm = this;

      FormEditor.userName = inUserName;
      addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          ExitAction();
        }
      });

      home = base;
      //fname = name;

      try {
      	String path = home.getPath();
      	String pathttemp = path.substring(1, path.length());
      	int index = pathttemp.indexOf("/");
      	index++;

        dirname = path.substring(0, index);
      	if (dirname.equals("/FormEditor"))
	 	      dirname = "";

        // Create an object we can use to communicate with the servlet
        URL servletURL = new URL(home.getProtocol() + "://" + home.getHost() +
        ":" +home.getPort() + dirname + "/servlet/authorservlets.GetAddress");
        URLConnection servletConnection = servletURL.openConnection();
        servletConnection.setDoOutput(true); // to allow us to write to the URL
        servletConnection.setUseCaches(false); // to ensure that we do contact

        PrintStream out = new PrintStream(servletConnection.getOutputStream());
        out.println(path);
        out.close();

        // Now read in the response
        InputStream in = servletConnection.getInputStream();
        int chr;
        StringBuffer buffer = new StringBuffer();

        while ((chr = in.read()) != -1) {
          buffer.append((char) chr);
        }

        in.close();
        formpath = buffer.toString().trim();
      }
      catch (IOException e) {
        System.out.println("FormEditor(String, URL, String): "+ e.toString());
      }

      address = home.getProtocol() + "://" + home.getHost() + ":" +
                                                     home.getPort() + dirname;
      formeditorpath = home.toString().substring(address.length());

      String temp = address + formpath + "emptyCL.xml";

      prop = new FormProperties(home);
      data = new GenerateListData(temp);
      editActions = new Stack();
      initComponents();
      setTitle(name);
    }

    /**
     * Initializes the components of the FormEditor.
     */
    private void initComponents() {
      mb = new JMenuBar();

      mnFile = new JMenu();
      mnEdit = new JMenu();
      mnView = new JMenu();
      mnHelp = new JMenu();
      mnLF = new JMenu();

      mniNew = new JMenuItem();
      mniOpen = new JMenuItem();
      mniSaveAs = new JMenuItem();
      mniSave = new JMenuItem();
      mniExit = new JMenuItem();
      mniUndo = new JMenuItem();
      mniCut = new JMenuItem();
      mniCopy = new JMenuItem();
      mniPaste = new JMenuItem();
      mniDelete = new JMenuItem();
      mniContent = new JMenuItem();
      mniAbout = new JMenuItem();
      mniLoadWOW = new JMenuItem(); // open WOW! application

      tabXmlPreview = new JTabbedPane(SwingConstants.RIGHT);

      edpXml = new JEditorPane();
      edpPreview = new JEditorPane();

      triConcepts = new JTree(new DomToTreeModelAdapter());
      ToolTipManager.sharedInstance().registerComponent(triConcepts);

      spnXml = new JScrollPane(edpXml);
      spnPreview = new JScrollPane(edpPreview);
      spnConcepts = new JScrollPane(triConcepts);

      tb = new JToolBar();
      lblElement = new JLabel();
      btnInput = new JButton();
      btnSelect = new JButton();
      btnOption = new JButton();
      btnButton = new JButton();
      btnDescription = new JButton();

      mnFile.setText("File");
      mnFile.setMnemonic(KeyEvent.VK_F);

      mniNew.setText("New");
      mniNew.setMnemonic(KeyEvent.VK_N);
      mniNew.addActionListener(new MenuAction());
      mnFile.add(mniNew);

      mniOpen.setText("Open");
      mniOpen.setMnemonic(KeyEvent.VK_O);
      mniOpen.addActionListener(new MenuAction());
      mnFile.add(mniOpen);

      mniSaveAs.setText("Save as...");
      mniSaveAs.setMnemonic(KeyEvent.VK_A);
      mniSaveAs.addActionListener(new MenuAction());
      mnFile.add(mniSaveAs);

      mniSave.setText("Save");
      mniSave.setMnemonic(KeyEvent.VK_S);
      mniSave.addActionListener(new MenuAction());
      mnFile.add(mniSave);

      // add load wow course item
      mniLoadWOW.setText("Load WOW! application");
      mniLoadWOW.setMnemonic(KeyEvent.VK_C);
      mniLoadWOW.addActionListener(new MenuAction());
      mnFile.add(mniLoadWOW);

      mnFile.add(new JSeparator());

      mniExit.setText("Exit");
      mniExit.setMnemonic(KeyEvent.VK_X);
      mniExit.addActionListener(new MenuAction());
      mnFile.add(mniExit);

      mb.add(mnFile);

      mnEdit.setText("Edit");
      mnEdit.setMnemonic(KeyEvent.VK_E);

      mniUndo.setText("Undo");
      mniUndo.setMnemonic(KeyEvent.VK_U);
      mniUndo.addActionListener(new MenuAction());
      mniUndo.setEnabled(false);
      mnEdit.add(mniUndo);

      mnEdit.add(new JSeparator());

      mniCut.setText("Cut");
      mniCut.setMnemonic(KeyEvent.VK_T);
      mniCut.addActionListener(new MenuAction());
      mnEdit.add(mniCut);

      mniCopy.setText("Copy");
      mniCopy.setMnemonic(KeyEvent.VK_C);
      mniCopy.addActionListener(new MenuAction());
      mnEdit.add(mniCopy);

      mniPaste.setText("Paste");
      mniPaste.setMnemonic(KeyEvent.VK_P);
      mniPaste.addActionListener(new MenuAction());
      mnEdit.add(mniPaste);

      mniDelete.setText("Delete");
      mniDelete.setMnemonic(KeyEvent.VK_D);
      mniDelete.addActionListener(new MenuAction());
      mnEdit.add(mniDelete);

      mb.add(mnEdit);

      mnView.setText("View");
      mnView.setMnemonic(KeyEvent.VK_V);

      mnLF.setText("Look & Feel");
      mnLF.setMnemonic(KeyEvent.VK_L);
      mnView.add(mnLF);
      mnView.add(new JSeparator());

      mb.add(mnView);

      mnHelp.setText("Help");
      mnHelp.setMnemonic(KeyEvent.VK_H);

      mniContent.setText("Content");
      mniContent.setMnemonic(KeyEvent.VK_C);
      mniContent.addActionListener(new MenuAction());
      mnHelp.add(mniContent);

      mnHelp.add(new JSeparator());

      mniAbout.setText("About");
      mniAbout.setMnemonic(KeyEvent.VK_A);
      mniAbout.addActionListener(new MenuAction());
      mnHelp.add(mniAbout);

      mb.add(mnHelp);

      UIManager.LookAndFeelInfo[] installedLnF = UIManager.getInstalledLookAndFeels();
      ButtonGroup group = new ButtonGroup();

      for (int teller = 0; teller < installedLnF.length; ++teller) {
        JRadioButtonMenuItem lnf = new JRadioButtonMenuItem(installedLnF[teller].getName());

        if (installedLnF[teller].getClassName().equals(UIManager.getSystemLookAndFeelClassName()))
          lnf.setSelected(true);

        group.add(lnf);
        mnLF.add(lnf);
        lnf.addActionListener(new menuChoice(installedLnF[teller].getClassName()));
      }

      //spnXml.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      //spnXml.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      edpXml.setEditable(true);
      edpXml.setText(prop.getXML());
      edpXml.addKeyListener(new myKeyListener());

      //spnPreview.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      //spnPreview.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      edpPreview.setEditable(false);
      edpPreview.setEditorKit(new javax.swing.text.html.HTMLEditorKit());
      edpPreview.setText(prop.getHTML());

      tabXmlPreview.addTab("XML", null, spnXml, "View of XML");
      tabXmlPreview.addTab("Preview", null, spnPreview, "Preview of form");
      tabXmlPreview.addChangeListener(new myChangeListener());

      //getContentPane().add(tabXmlPreview, BorderLayout.CENTER);
      //spnConcepts.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      //spnConcepts.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      spnConcepts.setToolTipText("Select attribute from concept to create form-element");
      triConcepts.setBackground(Color.lightGray);

      //triConcepts.setPreferredSize(new Dimension(200, 100));
      triConcepts.setEditable(false);
      triConcepts.setScrollsOnExpand(true);
      triConcepts.addTreeSelectionListener(new myTreeSelectionListener());

      triConcepts.setCellRenderer(new MyRenderer());

      //getContentPane().add(spnConcepts, BorderLayout.WEST);
      JSplitPane rightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
        spnConcepts, tabXmlPreview);
      getContentPane().add(rightSplitPane);

      tb.setPreferredSize(new Dimension(18, 30));
      tb.setFloatable(false);
      lblElement.setText("Element: ");
      tb.add(lblElement);

      tb.addSeparator();

      btnInput.setText("Input");
      btnInput.setToolTipText("Construct input element");
      btnInput.addActionListener(new ButtonAction(this));
      tb.add(btnInput);

      tb.addSeparator();

      btnSelect.setText("Select");
      btnSelect.setToolTipText("Construct select element");
      btnSelect.addActionListener(new ButtonAction(this));
      tb.add(btnSelect);

      tb.addSeparator();

      btnOption.setText("Option");
      btnOption.setToolTipText("Construct option element");
      btnOption.addActionListener(new ButtonAction(this));
      tb.add(btnOption);

      tb.addSeparator();

      btnButton.setText("Button");
      btnButton.setToolTipText("Construct button");
      btnButton.addActionListener(new ButtonAction(this));
      tb.add(btnButton);

      tb.addSeparator();

      btnDescription.setText("Description");
      btnDescription.setToolTipText("Construct description");
      btnDescription.addActionListener(new ButtonAction(this));
      tb.add(btnDescription);

      getContentPane().add(tb, BorderLayout.SOUTH);

      setJMenuBar(mb);
    }

    /**
     * Returns the number of elements constructed. Used for naming the elements.
     * @return int the number of elements constructed
     */
    public int getCountElem() {
      return countElem;
    }

    /**
     * Sets the content of the EditorPanes edpXml and edpPreview.
     */
    public void setEditorPanes() {
     	// XML is the XML source view
      edpXml.setText(prop.getXML());
    	// HTML is the HTML preview
      edpPreview.setText(prop.getHTML());
    }

    /**
     * Saves the XML of the constructed form in a file.
     * @param afilename the name of the file where the XML will be stored
     */
    public void SaveAs(String afilename) {
        //URL url = null;

//        try {
        String path = home.getPath();
        String pathttemp = path.substring(1, path.length());
        int index = pathttemp.indexOf("/");
        index++;

        String dirname = path.substring(0, index);
        //Loc Nguyen modified
        if(dirname == null) {}

        AMtClient AMt = new AMtClient(home, userName);

        int error = AMt.sendFile(filepath, filemode, edpXml.getText());
        showError(error);
//            url = new URL("http://" + home.getHost() + ":" + home.getPort() + dirname +
//                    "/servlet/authorservlets.SaveFile?userName=" + userName);
//
//            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
//            uc.setDoOutput(true);
//            uc.setUseCaches(false);
//
//            PrintWriter outb = new PrintWriter(uc.getOutputStream());
//            outb.println("xmlFile=[" + filename + ".frm" + "]");
//            outb.println(edpXml.getText());
//            outb.flush();
//            outb.close();
//
//            uc.getResponseCode();
//
//            if (uc.getResponseCode() == 200) {
//                JOptionPane o = new JOptionPane();
//                o.showMessageDialog(null, "The form is saved.");
//            } else {
//                JOptionPane o = new JOptionPane();
//                o.showMessageDialog(null, "An error occured while saving the form", "Error",
//                    JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (IOException except) {
//            System.out.println("error!: " + except.toString());
//        }


    }

    /**
     * The action performed when the FormEditor is closed.
     */
    public void ExitAction() {
      onClosingForm();
      dispose();
    }

    /**
     * Handles the closing of the current form. If there is unsaved data, the
     * user is prompted if he wants to save this data. If the user wants to
     * save the data, a save dialog is shown, otherwise the file is not saved.
     * If there is no unsaved data, nothing is done.
     */
    private void onClosingForm() {
      if (prop.isSaved()) return;

      //save form if user confirms
      int answer = JOptionPane.showConfirmDialog(
          null,
          "Do you wish to save the current form?",
          "Warning!",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.WARNING_MESSAGE
      );

      if (answer == JOptionPane.YES_OPTION) {//save form
        saveAction();
      }
    }

      /**
       * Saves the current form at the WOW! server. If the form already is known
       * to have a filename, this name is used, otherwise the user is prompted
       * for a filename, after which this name is used to save the form.
       */
      private void saveAction() {
        if (!(prop.getFilename().length() > 0)) {//ask for filename

          saveAsAction();
        }
        else {//save file with known name
          SaveAs(prop.getFilename().replaceAll(".frm",""));
          prop.setSaved(true);
        }
      }

      /**
       * Prompts the user for a filename to save the current form and saves the
       * form at the WOW! server under that filename.
       */
      private void saveAsAction() {
        //FormSave saveDialog = new FormSave(home);
        //saveDialog.show();
        ServerFileChooser saveDialog =
          new ServerFileChooser(home, userName, AMtc.APP_FILES_MODE, frm, true);
        String[] ff = {".frm"};
        saveDialog.showSaveDialog(ff);

        if (ServerFileChooser.fileName != null) { //saveDialog.cancelled == false) {
          prop.setFilename(ServerFileChooser.fileName.trim().replaceAll(".frm", ""));
          filepath = ServerFileChooser.filePath;
          filemode = AMtc.APP_FILES_MODE;

          SaveAs(prop.getFilename());
          prop.setSaved(true);
        }
      }

    /**
     * Opens a file for a certain author.
     * @param authorName the author to open the file for
     * @param afilename The name of the <Code>.frm</Code> file to open. This
     * file should be located in the authorfiles folder of the specified author.
     * The name should be of form <Code>file.frm</Code>.
     * @param afilepath The path on the WOW! server filesystem to download.
     * Path must be of form: <Code>/app/file1.frm</Code> and relative to the
     * wow folder on the server.
     * @param afilemode the mode indicating location of the file, which is
     * either AMtc.APP_FILES_MODE or AMtc.AUTHOR_FILES_MODE
     */
    public void openFile(String authorName, String afilename, String afilepath,
                                                                int afilemode) {
      userName = authorName;

      AMtClient AMt = new AMtClient(home, userName);

      StringBuffer s = AMt.getFile(afilepath, afilemode);

      if (s == null) {
        JOptionPane.showMessageDialog(this, "File could not be retrieved!",
          "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      prop.setFilename(afilename);
      filepath = afilepath;
      filemode = afilemode;

      String instring = s.toString();
//      try {
//        String path = home.getPath();
//        String pathttemp = path.substring(1, path.length());
//        int index = pathttemp.indexOf("/");
//        index++;
//
//        String dirname = path.substring(0, index);
//
//        if (dirname.equals("/FormEditor")) {
//            dirname = "";
//        }
//
//        URL url = new URL("http://" + home.getHost() + ":" + home.getPort() +
//                dirname + "/servlet/?fileName=" + fileName +
//                "&userName=" + userName);
//        prop.setFilename(fileName);
//
//        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
//        InputStream in = uc.getInputStream();
//        byte[] b = new byte[1];
//        String instring = new String("");
//
//        while (in.read(b, 0, 1) > 0) {
//            String temp = new String(b);
//            instring = instring + temp;
//        }

      prop.setXML(instring);
      edpXml.setText(prop.getXML());
      edpPreview.setText(prop.getHTML());

      prop.setSaved(true);

        //in.close();
//      } catch (IOException e) {
//          JOptionPane.showMessageDialog(this, "File unknown!", "Error",
//              JOptionPane.ERROR_MESSAGE);
//      }
    }

    /**
     * Shows a JOptionPane error dialog box with the specified error.
     * @param error the error code to display (from AMtc)
     */
    public void showError(int error) {
      if ((error == AMtc.NO_ERRORS) || (error == AMtc.ERROR_USER_ABORTED))
        return;
      else
        JOptionPane.showMessageDialog(frm, AMtc.getErrorMsg(error),
        AMtc.MESSAGE_HEADER_ERROR, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Implementation of an <code>ActionListener</code> for the menus of the
     * FormEditor.
     *
     * @see java.awt.event.ActionListener
     */
    class MenuAction implements ActionListener {
      /**
       * Overrides the <code>actionPerformed</code> of
       * <code>ActionListener</code>.
       * @param evt the event that triggers the action
       */
      public void actionPerformed(ActionEvent evt) {
        JMenuItem source = (JMenuItem) (evt.getSource());
        String item = source.getText();

        if (item.equals("New")) {//show a new file
          onClosingForm();

          prop.reset();
          filepath = null;
          filemode = -1;

          edpXml.setText(prop.getXML());
          edpPreview.setText(prop.getHTML());
          prop.setSaved(false);
        }
        else if (item.equals("Open")) {//open a file
          onClosingForm();

          // load file dialog
          //FormLoad loadDialog = new FormLoad(home);
          ServerFileChooser loadDialog =
            new ServerFileChooser(home, userName, AMtc.APP_FILES_MODE, frm, true);
          String[] ff = {".frm"};
          loadDialog.showOpenDialog(ff);

           //                if (answer != null) {
          if (ServerFileChooser.fileName != null) {//loadDialog.cancelled == false) {//open file
            openFile(userName, ServerFileChooser.fileName, ServerFileChooser.filePath,
                      AMtc.APP_FILES_MODE);
          }
        }
        else if (item.equals("Save as...")) {//save current form with user specified name
          saveAsAction();
        }
        else if (item.equals("Save")) {//save current form
          saveAction();
        }
        else if (item.equals("Exit")) { //exit the FormEditor
          ExitAction();
        }
        else if (item.equals("Load WOW! application")) {//load triConcepts of an WOW! application
          //WOWLoad wowDialog = new WOWLoad(home);
          //wowDialog.show();
          ServerFileChooser wowDialog = new ServerFileChooser(home, userName,
            AMtc.APP_FILES_MODE, frm, true);
          wowDialog.showLoadAppDialog();

          if (ServerFileChooser.fileName != null) {//cancelled == false) {
            address = home.getProtocol() + "://" + home.getHost() + ":" +
              home.getPort() + dirname;
            formeditorpath = home.toString().substring(address.length());
            String temp = address + formpath + "/" + userName + "/" +
            ServerFileChooser.fileName.trim().replaceAll(".wow", "") + ".wow";
            data = new GenerateListData(temp);
            prop = new FormProperties(home);
            triConcepts.setModel(new DomToTreeModelAdapter());
            triConcepts.updateUI();
          }
        }
        else if (item.equals("Undo")) {//undo last action
          AccessibleContext context =  edpXml.getAccessibleContext();
          AccessibleEditableText text = context.getAccessibleEditableText();
          String[] actionitem = (String[]) editActions.pop();

          if (actionitem[0].equals("cut") || actionitem[0].equals("delete")) {
            try {
              text.insertTextAtIndex(Integer.parseInt(actionitem[2]),
                actionitem[1]);
            }
            catch (NumberFormatException nfe) {}
          }
          else if (actionitem[0].equals("paste")) {
            try {
              text.delete(Integer.parseInt(actionitem[2]),
                Integer.parseInt(actionitem[2]) + actionitem[1].length());
            }
            catch (NumberFormatException nfe) {}
          }

          if (editActions.empty()) {
            mniUndo.setEnabled(false);
          }

          prop.setXML(edpXml.getText());
          prop.setSaved(false);
        }
        else if (item.equals("Cut")) {//cut selected text of edpXml
          AccessibleContext context =  edpXml.getAccessibleContext();
          AccessibleEditableText text = context.getAccessibleEditableText();
          lastSelectedText = text.getSelectedText();

          String[] actionitem = {
            "cut",
            lastSelectedText,
            String.valueOf(text.getSelectionStart())
          };
          editActions.push(actionitem);
          text.delete(text.getSelectionStart(), text.getSelectionEnd());

          prop.setXML(edpXml.getText());
          prop.setSaved(false);
          mniUndo.setEnabled(true);
        }
        else if (item.equals("Copy")) {//copy selected text of edpXml
          AccessibleContext context =  edpXml.getAccessibleContext();
          AccessibleEditableText text = context.getAccessibleEditableText();
          lastSelectedText = text.getSelectedText();

          prop.setSaved(false);
        }
        else if (item.equals("Paste")) {//paste lastSelectedText in edpXml
          AccessibleContext context =  edpXml.getAccessibleContext();
          AccessibleEditableText text = context.getAccessibleEditableText();

          String[] actionitem = {
            "paste",
            lastSelectedText,
            String.valueOf(text.getCaretPosition())
          };
          editActions.push(actionitem);
          text.insertTextAtIndex(text.getCaretPosition(), lastSelectedText);

          prop.setXML(edpXml.getText());
          prop.setSaved(false);
          mniUndo.setEnabled(true);
        }
        else if (item.equals("Delete")) {//delete selected text in edpXml
          AccessibleContext context =  edpXml.getAccessibleContext();
          AccessibleEditableText text = context.getAccessibleEditableText();
          lastSelectedText = text.getSelectedText();

          String[] actionitem = {
            "delete",
            lastSelectedText,
            String.valueOf(text.getSelectionStart())
          };
          editActions.push(actionitem);
          text.delete(text.getSelectionStart(), text.getSelectionEnd());

          prop.setXML(edpXml.getText());
          prop.setSaved(false);
          mniUndo.setEnabled(true);
        }
        else if (item.equals("Content")) {//open new window with help file contents
          new HelpWindow(home);
        }
        else if (item.equals("About")) {//show about box
        	JOptionPane.showMessageDialog(null,
            "FormEditor v2.0 \nUniversity of Science\n2008",
            "About FormEditor", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    }

    /**
     * Implementation of an <code>ActionListener</code> for the buttons of the
     * FormEditor.
     * @see java.awt.event.ActionListener
     */
    class ButtonAction implements ActionListener {
      private FormEditor editor;

      /**
       * Constructor for the ButtonAction.
       * @param f the FormEditor for which the actions are performed
       */
      public ButtonAction(FormEditor f) {
        editor = f;
      }

      /**
       * Overrides the <code>actionPerformed</code> of
       * <code>ActionListener</code>.
       * @param evt the event that triggers the action
       */
      public void actionPerformed(ActionEvent evt) {
        JButton source = (JButton) (evt.getSource());
        String item = source.getText();

        if (item.equals("Input")) {
          if (prop.getChangeable()) {
            countElem++;

            new ElementWindow(editor, 1, prop.getConcept(),
              prop.getAttribute(), prop.getType(), prop.getDescription());
          }
          else {
        	  JOptionPane.showMessageDialog(editor,
                "This is not a changeable attribute, \nso it can't be included in a form!",
                "Error", JOptionPane.ERROR_MESSAGE);
          }
        }
        else if (item.equals("Select")) {
          if (prop.getChangeable()) {
            countElem++;

            new ElementWindow(editor, 2, prop.getConcept(),
                    prop.getAttribute(), prop.getType(), prop.getDescription());
          }
          else {
            JOptionPane.showMessageDialog(editor,
                "This is not a changeable attribute, so it can't be included in a form!",
                "Error", JOptionPane.ERROR_MESSAGE);
          }
        }
        else if (item.equals("Option")) {
          if (prop.getChangeable()) {
            countElem++;

            new ElementWindow(editor, 3, prop.getConcept(),
              prop.getAttribute(), prop.getType(), prop.getDescription());
          }
          else {
            JOptionPane.showMessageDialog(editor,
                "This is not a changeable attribute, so it can't be included in a form!",
                "Error", JOptionPane.ERROR_MESSAGE);
          }
        }
        else if (item.equals("Button")) {
          new ButtonWindow(editor);
        }
        else if (item.equals("Description")) {
          countElem++;

          new DescriptionWindow(editor);
        }
      }
    }

    /**
     * Implementation of an <code>ActionListener</code> for the Look-and-Feel
     * menu of the FormEditor.
     *
     * @see java.awt.event.ActionListener
     */
    class menuChoice implements ActionListener {
      String lookAndFeel = new String();

      /**
       * Constructor for <code>menuChoice</code>.
       * @param lookAndFeel the selected look-and-feel
       */
      public menuChoice(String lookAndFeel) {
        this.lookAndFeel = lookAndFeel;
      }

      /**
       * Overrides the <code>actionPerformed</code> of
       * <code>ActionListener</code>.
       * @param evt the event that triggers the action
       */
      public void actionPerformed(ActionEvent e) {
        try {
          UIManager.setLookAndFeel(this.lookAndFeel);
          SwingUtilities.updateComponentTreeUI(getContentPane());
          SwingUtilities.updateComponentTreeUI(mb);
        }
        catch (ClassNotFoundException exc) {
          System.err.println("Can't find System look and feel: " + exc);
        }
        catch (Exception exc) {
          System.err.println("Problem with look and feel: " + exc);
        }
      }
    }

    /**
     * Implementation of a <code>KeyListener</code> for the key actions in the
     * FormEditor.
     * @see java.awt.event.KeyListener
     */
    class myKeyListener implements KeyListener {

      public myKeyListener() {}

      public void keyPressed(KeyEvent evt) {}

      public void keyReleased(KeyEvent evt) {
          prop.setXML(edpXml.getText());
          prop.setSaved(false);
      }

      public void keyTyped(KeyEvent evt) {}
    }

    /**
     * Implementation of a <code>ChangeListener</code> for the tabs in the
     * FormEditor.
     * @see javax.swing.event.ChangeListener
     */
    class myChangeListener implements ChangeListener {
      /**
       * Overrides the <code>stateChanged</code> of <code>ChangeListener</code>.
       * @param e the event that triggers the change
       */
      public void stateChanged(ChangeEvent e) {
        if (tabXmlPreview.getSelectedIndex() == 1) {
          if (prop.isXMLChanged()) {
            prop.setHTML(prop.getXML());
            edpPreview.setContentType("text/html;charset=UTF-8");
            edpPreview.setText(this.RemoveXHTML(edpXml.getText()));
          }
        }
      }
// Brendan: convert the xhtml into html 3.2 (jeditorpane can only display 3.2)
//  unsolved "gt;" bug

      public String RemoveXHTML(String xhtml) {
        String html = "";
        int index = xhtml.indexOf("</object>");
        html = "<html> <head> </head> \n <body>" + xhtml.substring(index + 9);
        html = html.replaceAll( "type=\"int\"","type=\"text\"");
        //html = html.replaceAll("<button type=\"submit\">","><INPUT TYPE=SUBMIT VALUE=");
        //html = html.replaceAll("<button type=\"reset\">","><INPUT TYPE=RESET VALUE=");
        //html = html.replaceAll("</button>",">");
        return html;
      }
    }

    /**
     * Implementation of a <code>TreeSelectionListener</code> for the tree in
     * the FormEditor.
     * @see javax.swing.event.TreeSelectionListener
     */
    class myTreeSelectionListener implements TreeSelectionListener {

      /**
       * Overrides the <code>valueChanged</code> of
       * <code>TreeSelectionListener</code>.
       * @param e the event that triggers the change
       */
      public void valueChanged(TreeSelectionEvent e) {
        TreePath p = e.getNewLeadSelectionPath();
        triConcepts.scrollPathToVisible(p);

        if (p != null) {
          AdapterNode adpNode = (AdapterNode) p.getLastPathComponent();
          NodeInfo info = new NodeInfo(adpNode);

          if (info.isAttr()) {
            prop.setConcept(info.getConceptName());
            prop.setAttribute(info.getAttrName());
            prop.setType(info.getType());
            prop.setDescription(info.getDescription());
            prop.setChangeable(info.isAttrChangeable());
          }
          else prop.setChangeable(false);
        }
      }
    }

    /**
     * Extension of <code>DefaultTreeCellRenderer</code> for the rendering of
     * the tree in the FormEditor.
     * @see javax.swing.tree.DefaultTreeCellRenderer
     */
    class MyRenderer extends DefaultTreeCellRenderer {
      /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	ImageIcon StopIcon;

      /**
       * Constructor for MyRenderer.
       *
       */
      public MyRenderer() {
        try {
// Location is still wrong, but we'll fix that in a later release.
// For now we copied stop.gif to the "lib" directory.
            URL loc = new URL(home.toString() + "stop.gif");
            StopIcon = new ImageIcon(loc);
        }
        catch (IOException e) {
          System.out.println(e);
        }
      }

      /**
       * Overloads the <code>getTreeCellRendererComponent</code> of
       * <code>DefaultTreeCellRenderer</code>
       */
      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
        boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if (!leaf) {
          setToolTipText("Select attribute from concept to create form-element");
        }

        if (leaf && !isChangeable(value)) {
          setIcon(StopIcon);
          setToolTipText("Attribute not changeable!");
        }

        return this;
      }

      /**
       * Returns whether or not an object is a changeable node.
       *
       * @param value the object that is queried
       * @return boolean the value is (not) changeable
       */
      protected boolean isChangeable(Object value) {
        AdapterNode adpNode = (AdapterNode) value;
        NodeInfo info = new NodeInfo(adpNode);

        return info.isAttrChangeable();
      }
    }
}