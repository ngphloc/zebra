/*

    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 

*/
/**
 * Editor.java 1.0, August 30, 2008/
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
/**
 * changes by David Smits, reviewed and modified by Loc Nguyen 9-08-2008
 */

package vn.spring.WOW.generatelisteditor;

// WOW
import vn.spring.WOW.AMt.AMtc;
import vn.spring.WOW.AMt.ServerFileChooser;
import vn.spring.WOW.datacomponents.*;

// GUI components and layouts
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import java.awt.Font;

// user friendly display of DOM stuff
import javax.swing.JSplitPane;

// GUI support classes
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.text.DefaultEditorKit;

// For creating a TreeModel
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;

import java.util.Vector;

public class Editor extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static GenerateListData data = null;
    private static String conceptFile;
    public  static boolean changed = false;
    private static String fromEmptyCL;
    private static String authortoolstr;
    private static StringBuffer authortoolpath;
    private static String dirname;
    static private String userName;
    public static final boolean compress = true;
    public static Editor myinstance = null;

    static final int windowHeight = 680;
    static final int leftWidth = 160;
    static final int rightWidth = 580;
    static final int windowWidth = leftWidth + rightWidth;
    static JFrame frame;
    static public URL home;

    static public JScrollPane editView = null;
    static public JTree tree = null;
    static public TreePath lastTreePath = null;

    public Editor() {
        myinstance = this;

        // Set up the tree
        tree = new JTree(new DomToTreeModelAdapter());
        tree.putClientProperty("JTree.lineStyle", "Angled");

        // Build left-side view
        JScrollPane treeView = new JScrollPane(tree);
        treeView.setPreferredSize(new Dimension( leftWidth, windowHeight ));

        JProtectedPanel editPane = new DefaultProtectedPanel();

        // Build right-side view
        editView = new JScrollPane(editPane);
        editView.setPreferredSize(new Dimension( rightWidth, windowHeight*4/5 ));

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                treeValueChanged(e.getNewLeadSelectionPath());
            }
        });

        //build right-side Split pane
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editView, new JPanel());
        rightSplitPane.setContinuousLayout(true);
        int splitPaneSize = windowHeight * 4/5;
        rightSplitPane.setDividerLocation(splitPaneSize);
        rightSplitPane.setLastDividerLocation(splitPaneSize);
        rightSplitPane.setPreferredSize(new Dimension(rightWidth, splitPaneSize + 10));

        // Build split-pane view
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView, rightSplitPane);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerLocation(leftWidth);
        splitPane.setPreferredSize(new Dimension( windowWidth + 10, windowHeight+10 ));

        // Add GUI components
        this.setLayout(new BorderLayout());
        this.add("Center", splitPane);
    }

    private void treeValueChanged(TreePath p) {
        changed = true;
        lastTreePath = p;
        if (p != null) {
            Object o = p.getLastPathComponent();
            if (o instanceof Concept) {
                // The selected item is a concept
                showConcept(new ConceptEditor((Concept)o));
            } else if (o instanceof Attribute) {
                // The selected item is an attribute
                if (p.getPathCount()>1) {
                    Concept c = (Concept)p.getPathComponent(p.getPathCount()-2);
                    showAttribute(new AttributeEditor((Attribute)o, c));
                }
            } else {
                // The selected item is the root
                JProtectedPanel editPane = new DefaultProtectedPanel();
                JTextField text = new JTextField("Concept List");
                text.setEditable(false);
                text.setBorder(null);
                Font font = text.getFont();
                text.setFont(font.deriveFont((float)30.0));
                editPane.add(text);
                showPanel(editPane);
            }
        }
    }

    private static void newConceptList() {
        conceptFile = "";
        data = new GenerateListData();
        setCreated("true");
        tree.updateUI();
        showPanel(new DefaultProtectedPanel());
    }

    public static JMenuBar makeMenu(final JFrame frame, final String temp, final String login, final Vector v) {
        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        menuBar.add(file);

        setCreated("true");

        JMenuItem newfile = new JMenuItem("New");
        newfile.setMnemonic(KeyEvent.VK_N);
        newfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                newConceptList();
            }
        });
        file.add(newfile);

        JMenuItem open = new JMenuItem("Open");
        open.setMnemonic(KeyEvent.VK_O);
        open.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
            //conceptFile = JOptionPane.showInputDialog(null,
            //  "What file do you want to edit?\nPlease, enter filename without "+
            //  "extension.","Open",JOptionPane.QUESTION_MESSAGE);
            ServerFileChooser loadDialog = new ServerFileChooser(home, userName,
                                           AMtc.AUTHOR_FILES_MODE, frame, true);
            String[] ff = {".wow"};
            loadDialog.showOpenDialog(ff);
            if (ServerFileChooser.fileName == null) return;
            openFile(ServerFileChooser.fileName, userName);
          }
        });

        file.add(open);

        JMenuItem reload = new JMenuItem("Reload");
        reload.setMnemonic(KeyEvent.VK_R);

                reload.addActionListener(new  ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        data.loadData();
                        tree.updateUI();
                        showPanel(new DefaultProtectedPanel());
                    }
                });

        file.add(reload);

        JMenuItem saveas = new JMenuItem("Save as...");
        saveas.setMnemonic(KeyEvent.VK_A);
        saveas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                ServerFileChooser saveDialog = new ServerFileChooser(home,
                  userName, AMtc.AUTHOR_FILES_MODE, frame, true);
                String[] ff = {".wow"};
                saveDialog.showSaveDialog(ff);

                conceptFile = ServerFileChooser.fileName;
                if (conceptFile == null) return;
                //JOptionPane.showInputDialog(null,"Give a filename.\nPlease, do not specify any extension.","Save as...",JOptionPane.QUESTION_MESSAGE);
                //if (conceptFile.indexOf(".")>-1) conceptFile = "";

                  //if (!conceptFile.equals("")) {
                if (Character.isDigit(conceptFile.charAt(0))) JOptionPane.showMessageDialog(null,"Filename should start with a character!\nPlease specify another name.","Error",JOptionPane.ERROR_MESSAGE);
                else if (v.indexOf(conceptFile)>-1) JOptionPane.showMessageDialog(null,"One of the authors already has a course with this name!\nPlease specify another name.","Error",JOptionPane.ERROR_MESSAGE);
                else {
                    try {
                        conceptFile = conceptFile+".wow";
                        URL url = new URL(temp+userName+"/"+conceptFile);
                        HttpURLConnection uc = (HttpURLConnection)url.openConnection();
                        uc.getInputStream();
                        int button = JOptionPane.showConfirmDialog(null,"File with this name already exists!\nSave it anyway?","Warning",JOptionPane.YES_NO_OPTION);
                        System.out.println("Editor: Save As: button clicked: " +String.valueOf(button) );
                        if (button  == 0) {
                            data = new GenerateListData(temp+userName+"/"+conceptFile, authortoolstr);
                            data.setAuthor(userName);
                            data.save(conceptFile, getCreated());
                            data.loadData();
                            tree.updateUI();
                            showPanel(new DefaultProtectedPanel());
                            changed = false;
                        }
                    } catch(IOException e) {
                        data = new GenerateListData(temp+userName+"/"+conceptFile, authortoolstr);
                        data.setAuthor(userName);
                        data.save(conceptFile, getCreated());
                        changed = false;
                        data.loadData();
                        tree.updateUI();
                        showPanel(new DefaultProtectedPanel());
                    }
                    setCreated("false");
                }
                  //} else JOptionPane.showMessageDialog(null,"You didn't specify any filename or you specified it wrongly!","Error",JOptionPane.ERROR_MESSAGE);
            }

            });

        file.add(saveas);

        JMenuItem save = new JMenuItem("Save");
        save.setMnemonic(KeyEvent.VK_S);

                save.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                      if (!conceptFile.equals("emptyCL.xml")) {
                        System.out.println("Editor: Save: saving the file");
                        // set author
                        data.setAuthor(userName);
                        data.save(conceptFile, getCreated());
                        changed = false;
                      } else JOptionPane.showMessageDialog(null,"Please, give a filename!","Error",JOptionPane.ERROR_MESSAGE);
                    }
                });

        file.add(save);

        JMenuItem savetowow = new JMenuItem("Save to WOW!");
                savetowow.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        if (!conceptFile.equals("emptyCL.xml")) {
                            boolean created = true;
                            try {
                            URL tmpurl = new URL(temp);
                            //adapted from Brendan Rousseau by Natalia Stash, 28-07-2008
                            URL url = new URL("http://" + tmpurl.getHost() + ":" +
                                              tmpurl.getPort() + dirname +
                                              "/servlet/authorservlets.ExportFile?fileName=" +
                                              conceptFile.substring(0, conceptFile.indexOf(".wow")) + "&author=" + userName);
                            HttpURLConnection test = (HttpURLConnection) url.openConnection();
                            test.setRequestMethod("GET");
                            test.getResponseCode();
                            test.getResponseMessage();
                            test.disconnect();
                            } catch (Exception ex) {created = false;}
                            if (created) JOptionPane.showConfirmDialog(null,
                                "This project is successfully added to the WOW! database, please restart the webserver!",
                               "Alert", JOptionPane.PLAIN_MESSAGE);
                            else JOptionPane.showMessageDialog(null,"There was an error adding the project to WOW! database!","Error",JOptionPane.ERROR_MESSAGE);

                        } else JOptionPane.showMessageDialog(null,"Please, save your project first!","Error",JOptionPane.ERROR_MESSAGE);
                    }
                });

        file.add(savetowow);

        file.addSeparator();

        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_X);
        file.add(exit);

        class exitWindows implements ActionListener
        {
            public void actionPerformed(ActionEvent e) {
                if (changed) {
                    int button = JOptionPane.showConfirmDialog(null,"Do you want to save the file before closing?","Warning",JOptionPane.YES_NO_OPTION);
                    if (button == 0) data.save(conceptFile, getCreated());
                    frame.dispose();
                }
                else frame.dispose();
                //System.exit(0);
            }
        }

        exit.addActionListener( new exitWindows() );


        // Edit
        JMenu edit = new JMenu("Edit");
        edit.setMnemonic(KeyEvent.VK_E);

        menuBar.add(edit);

        JMenuItem cut = new JMenuItem("Cut");
        cut.setMnemonic(KeyEvent.VK_T);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        cut.addActionListener(new DefaultEditorKit.CutAction());
        edit.add(cut);


        JMenuItem copy = new JMenuItem("Copy");
        copy.setMnemonic(KeyEvent.VK_C);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        copy.addActionListener(new DefaultEditorKit.CopyAction());
        edit.add(copy);

        JMenuItem paste = new JMenuItem("Paste");

        paste.setMnemonic(KeyEvent.VK_P);
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        paste.addActionListener(new DefaultEditorKit.PasteAction());
        edit.add(paste);

        // Manage The concepts and Attributes

        JMenu conceptMenu = new JMenu("Concept");
        conceptMenu.setMnemonic(KeyEvent.VK_C);
        menuBar.add(conceptMenu);

        JMenu attributeMenu = new JMenu("Attribute");
        attributeMenu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(attributeMenu);

        JMenuItem add = new JMenuItem("Add");
        add.setMnemonic(KeyEvent.VK_A);
        add.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));

        add.addActionListener(new ActionListener()
        {
            public void actionPerformed( ActionEvent ae)
            {
                addConcept();
            }
        });

        conceptMenu.add(add);

        addAttribute = new JMenuItem("Add");
        addAttribute.setMnemonic(KeyEvent.VK_A);
        addAttribute.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, ActionEvent.ALT_MASK));
        addAttribute.setEnabled(false);

        addAttribute.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent AE)
            {
                addAttribute();
            }
        });

        attributeMenu.add(addAttribute);

        removeConcept = new JMenuItem("Remove");
        removeConcept.setMnemonic(KeyEvent.VK_R);
        removeConcept.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        removeConcept.setEnabled(false);

                removeConcept.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent ae) {
                    removeConcept();
                  }
                });

        conceptMenu.add(removeConcept);

        removeAttribute = new JMenuItem("Remove");
        removeAttribute.setMnemonic(KeyEvent.VK_R);
        removeAttribute.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        removeAttribute.setEnabled(false);

                removeAttribute.addActionListener(new ActionListener() {
                  public void actionPerformed(ActionEvent ae) {
                    removeAttribute();
                  }
                });

        attributeMenu.add(removeAttribute);

        JMenu layoutMenu = new JMenu("Layout");
        layoutMenu.setMnemonic(KeyEvent.VK_L);
        menuBar.add(layoutMenu);



        class menuChoice implements ActionListener
        {
            String lookAndFeel = new String();

            public menuChoice(String lookAndFeel)
            {
                this.lookAndFeel = lookAndFeel;
            }

            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(this.lookAndFeel);
                    SwingUtilities.updateComponentTreeUI(frame);
                } catch (ClassNotFoundException exc) {
                    System.err.println("Can't find System look and feel: " + exc);
                } catch (Exception exc) {
                    System.err.println("Problem with look and feel: " + exc);
                }
            }
        }


        UIManager.LookAndFeelInfo installedLnF[] = UIManager.getInstalledLookAndFeels();

        for (int teller = 0; teller < installedLnF.length; ++teller)
        {
            JMenuItem lnf = new JMenuItem(installedLnF[teller].getName());
            layoutMenu.add(lnf);
            lnf.addActionListener(new menuChoice(installedLnF[teller].getClassName()));
        }

/*David*/
        JMenu extraMenu = new JMenu("Extra");
        menuBar.add(extraMenu);

        JMenuItem cycleTest = new JMenuItem("Cycle test...");
        cycleTest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testCycle();
            }
        });
        extraMenu.add(cycleTest);
/*End David*/

        JMenu helpMenu = new JMenu("Help");

        JMenuItem mnuItemAbout = new JMenuItem("About");
        mnuItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AboutBox about = new AboutBox(frame, home);
                about.show();
            }
        });
        helpMenu.add(mnuItemAbout);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private static JMenuItem addAttribute = null;
    private static JMenuItem removeConcept = null;
    private static JMenuItem removeAttribute = null;

    public static void addAttribute() {
        Concept c = null;
        if (lastTreePath.getLastPathComponent() instanceof Attribute) {
            c = (Concept)lastTreePath.getPathComponent(lastTreePath.getPathCount()-2);
        } else
        if (lastTreePath.getLastPathComponent() instanceof Concept) {
            c = (Concept)lastTreePath.getLastPathComponent();
        }
        if (c == null) return;

        String attrname = "attribute";
        attrname = JOptionPane.showInputDialog("Give a name for a new attribute: ", attrname);
        if (attrname == null || "".equals(attrname))
            return; // empty name or cancel pressed
        if (GenerateListData.containsAttribute(c.getName(), attrname)) {
            JOptionPane.showMessageDialog(null,"This attribute already exists","Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Attribute newAttribute = new Attribute(attrname, AttributeType.ATTRINT);
        c.getAttributes().add(newAttribute);
        tree.updateUI();
        showAttribute(new AttributeEditor(newAttribute, c));
    }

    public static void addConcept()
    {
        String cname = GenerateListData.coursename+".concept";
        cname = JOptionPane.showInputDialog("Give a name for a new concept: ", cname);
        if (cname == null || "".equals(cname))
            return; // empty name or cancel pressed
        if (GenerateListData.containsConcept(cname)) {
            JOptionPane.showMessageDialog(null,"This concept already exists","Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Concept newConcept = GenerateListData.rootConcept.copy("defaultname", cname);
        GenerateListData.concepts.add(newConcept);
        tree.updateUI();
        showConcept(new ConceptEditor(newConcept));
    }

    public static void removeConcept() {
        if (lastTreePath.getLastPathComponent() instanceof Concept) {
            Concept c = (Concept)lastTreePath.getLastPathComponent();
            GenerateListData.concepts.remove(c);
            tree.updateUI();
        }
    }

    public static void removeAttribute() {
        if (lastTreePath.getPathCount() > 1) {
            Concept c = (Concept)lastTreePath.getPathComponent(lastTreePath.getPathCount()-2);
            c.getAttributes().remove(lastTreePath.getLastPathComponent());
            tree.updateUI();
        }
    }

    public static void showConcept(JProtectedPanel editPane)
    {
        addAttribute.setEnabled(true);
        removeAttribute.setEnabled(false);
        removeConcept.setEnabled(true);

        JViewport view = Editor.editView.getViewport();
        JProtectedPanel pPanel = (JProtectedPanel)view.getView();
        pPanel.isSaved();
        view.setView(editPane);
    }

    public static void showAttribute(JProtectedPanel editPane)
    {
        addAttribute.setEnabled(false);
        removeAttribute.setEnabled(true);
        removeConcept.setEnabled(false);

        JViewport view = Editor.editView.getViewport();
        JProtectedPanel pPanel = (JProtectedPanel)view.getView();
        pPanel.isSaved();
        view.setView(editPane);
    }

    public static void showPanel(JProtectedPanel editPane)
    {
        addAttribute.setEnabled(false);
        removeAttribute.setEnabled(false);
        removeConcept.setEnabled(false);

        JViewport view = Editor.editView.getViewport();
        JProtectedPanel pPanel = (JProtectedPanel)view.getView();
        pPanel.isSaved();
        view.setView(editPane);
    }

    private static void testCycle() {
        StringBuffer result = new StringBuffer();
        Vector cycles = GenerateListData.agraph.cycles;
        for (int i=0;i<cycles.size();i++) result.append(cycles.get(i).toString());
        if (cycles.size()==0) result.append("There are no cycles detected");
        JOptionPane.showMessageDialog(null, result.toString());
    }

    public static String getCreated()
    {
        return fromEmptyCL;
    }

    public static void setCreated(String created)
    {
        fromEmptyCL=created;
    }

    public void makeFrame(URL url, String alogin, Vector v) {
      userName = alogin;

      // set Look and feel
      conceptFile = "emptyCL.xml";
      dirname = "";
      authortoolpath = new StringBuffer();
      try {
        home = url;
        String path = home.getPath();
        String temp = path.substring(1, path.length());
        int index = temp.indexOf("/");
        index++;
        dirname = path.substring(0, index);
        if (dirname.equals("/GenerateList_Editor")) {
            dirname = "";
        }
        // Create an object we can use to communicate with the servlet
        URL servletURL = new URL(home.getProtocol()+"://"+home.getHost()+":"+home.getPort()+dirname+"/servlet/authorservlets.GetAddress");
        URLConnection servletConnection = servletURL.openConnection();
        servletConnection.setDoOutput(true);  // to allow us to write to the URL
        servletConnection.setUseCaches(false);  // to ensure that we do contact
                                          // the servlet and don't get
                                          // anything from the browser's
                                          // cache
        // Write the message to the servlet
        PrintStream out = new PrintStream(servletConnection.getOutputStream());
        out.println(url);
        out.close();

        // Now read in the response
        InputStream in = servletConnection.getInputStream();
        int chr;
        while ((chr=in.read())!=-1) {
            authortoolpath.append((char) chr);
        }
        in.close();

      } catch(IOException ioe) {System.out.println("Error: "+ioe.toString());}

      String tmp = url.getProtocol()+"://"+url.getHost()+":"+url.getPort()+dirname+authortoolpath.toString().trim();
      authortoolstr = authortoolpath.toString().trim().substring(1);
      data = new GenerateListData(tmp+conceptFile, authortoolstr);
      data.loadData();
//      try
//      {
//          UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
//      } catch (ClassNotFoundException exc)
//      {
//          System.err.println("Can't find System look and feel: " + exc);
//      } catch (Exception exc)
//      {
//          System.err.println("Problem with look and feel: " + exc);
//      }
      // Set up a GUI framework
      frame = new JFrame("Generate List Editor");
      frame.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
              frame.dispose();
              //System.exit(0);
              }
      });
      // Set up the tree, the views, and display it all
      final Editor echoPanel = new Editor();
      frame.setJMenuBar(makeMenu(frame, tmp, userName, v));
      frame.getContentPane().add("Center", echoPanel );
      frame.pack();
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int w = windowWidth + 20;
      int h = windowHeight + 20;
//      frame.setLocation(screenSize.width/3 - w/2, screenSize.height/2 - h/2);
      frame.setLocation((screenSize.width - w)/2, (screenSize.height - h)/2);
      frame.setSize(w, h);
      frame.setVisible(true);
    } // makeFrame

    public boolean isOpen() {
        return frame.isVisible();
    }

    /**
     * Opens a file for a certain author. States an error message in a
     * JOptionPane if the file could not be found.
     * @param fileName The name of the .wow file to open. This file should be
     *                 located in the authorfiles folder of the specified
     *                 author. The name should be of form "file.wow".
     * @param authorName the author to open the file for
     */
    public static void openFile(String fileName, String authorName) {
      conceptFile = fileName;
      if (conceptFile != null) {
        if (!conceptFile.endsWith(".wow"))
          conceptFile = conceptFile+".wow";

        //retrieve conceptFile

        String tmp =
          home.getProtocol() + "://" +
          home.getHost()+":"+ home.getPort() +
          dirname + authortoolpath.toString().trim();

        String urlstring = tmp+authorName+"/"+conceptFile;
        //System.out.println(urlstring);

        try {
          URL url = new URL(urlstring);
          HttpURLConnection uc = (HttpURLConnection)url.openConnection();
          uc.getInputStream();
          data = new GenerateListData(urlstring, authortoolstr);
          data.loadData();
          tree.updateUI();
          showPanel(new DefaultProtectedPanel());
          setCreated("false");
        }
        catch (IOException e) {
          JOptionPane.showMessageDialog(null,"File unknown!","Error",
            JOptionPane.ERROR_MESSAGE);
          System.out.println(e.toString());
        }
      }
    }
}