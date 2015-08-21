/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * GraphAuthor 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.GraphAuthor;

import com.jgraph.JGraph;

import com.jgraph.graph.*;
import com.jgraph.event.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import vn.spring.WOW.AMt.AMtc;
import vn.spring.WOW.AMt.ServerFileChooser;
import vn.spring.WOW.graphauthor.author.*;
import vn.spring.WOW.util.*;
import vn.spring.zebra.um.OverlayBayesUMFactory;
import vn.spring.zebra.um.OverlayBayesUM;

/**
 * Main class for the gui. Modified by Loc Nguyen 2008 because this file is very important
 *
 */
public class GraphAuthor extends JApplet implements KeyListener,
                                                    TreeSelectionListener,
                                                    DropTargetListener {
	private static final long      serialVersionUID = 1L;
	
    public static GraphAuthor      myinstance = null;
    public static LinkedList       conceptList = null;
    public static JTree            sharedConceptTree = null;
    public static DragTree         conceptTree = null; //Loc Nguyen add keyword "static"
    static String                  projectName = null;
	static MyGraph                 graph = null;
    static DefaultMutableTreeNode  loadTree = null;
    static Vector                  visListData = null;
    static Vector                  filteredListData = null;
    //Natasha
    static Vector                  authorStrategies = null;
    static Vector                  existingStrategies = null;
    //end Natasha
    static int                     opened = 0;
    
    public  boolean                graphChanged = false;
    public  Map                    attributes;
    public  ReadCRTXML             crt;
    public  ReadTemplateXML        templateList;
    public  Vector                 listDataRel = new Vector();
    public  LinkedList             treeConceptList = new LinkedList();
    public  URL                    home = null;
    private GraphModel             model;
    private boolean                isStandalone = false;
    public  String                 dirname;
    // added @David @11-10-2008
    private Termination            termination = null;
    // end added @David @11-10-2008
    private boolean                setresource;

    private JPanel                 pnlMain = new JPanel();
    private JFrame                 mainFrame;
    private JSplitPane             splConceptGraph = new JSplitPane();
    public  JButton                AddConceptButton = new JButton();
    //private JLabel                 jLabel2 = new JLabel(); //NOT USED
    private JComboBox              RelationList = new JComboBox();
    public  JButton                processButton = new JButton();
    public  JButton                Load_Button = new JButton();
    private JMenuBar               menuBar = new JMenuBar();
    public  JButton                Export_button = new JButton();
    private String                 selection = "";
    private JScrollPane            spnConceptTree = new JScrollPane();
    //private JPanel                 balk = new JPanel();
    //private FlowLayout             flowLayout1 = new FlowLayout(); //NOT USED
    //private FlowLayout             flowLayout2 = new FlowLayout(); //NOT USED
    public  JScrollPane            graphScroll;
    public  String                 selectedFilter = "No filter";
    public  JButton                filterButton = new JButton();
    private JToolBar               toolBar;
    public  String                 cutConcept = "";
    private TreePath               cutPath = null;
    private DefaultMutableTreeNode cutTreeNode = null;
    private JCheckBoxMenuItem      showAdvanced = null;
    private JMenuItem              menuAdvAttributes = null;

    /**
     * Constructor
     * does nothing.
     */
    public GraphAuthor() {
    }

    /**
     * Constructor. Initializes the graph author and shows the main frame for a
     * certain author without the need to log in.
     * @param codebase The codebase for this applet. Necessary if this class
     *                 is not the entry class of the started applet. (i.e.
     *                 to start the Graph Author from the AMt)
     * @param authorname the name of the author to load the file for
     * @param show if the mainFrame should be set visible
     */
    public GraphAuthor(URL codebase, String authorname, boolean show) {
      home = codebase;

      //init graph author
      try {
        AuthorSTATIC.authorName = authorname;

        initAuthor();
        jbInit();
        initTree();
        AddExtraComponents();
        AddMenu();
        //create frame - do not use addMainFrame(), since this also displays the
        //JFrame. This is unwanted.
        mainFrame = new JFrame(
                            "Graphical Author tool for WOW! applications v1.0");
        mainFrame.setJMenuBar(menuBar);
        mainFrame.setSize(800, 620);
        mainFrame.setLocation(100, 100);
        mainFrame.getContentPane().add(pnlMain);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                mainFrame_Closing(e);
            }
        });

      }
      catch (Exception e) {
        e.printStackTrace();
      }

      mainFrame.repaint();

      mainFrame.setVisible(show);
    }

    /**
     * Constructor. Initializes the graph author, shows the main frame and
     * loads a .gaf file for a certain author without the need to log in.
     * @param codebase The codebase for this applet. Necessary if this class
     *                 is not the entry class of the started applet. (i.e.
     *                 to start the Graph Author from the AMt)
     * @param authorname the name of the author to load the file for
     * @param filename The name of the file to open, ending with ".gaf".
     *                 This file exists in the authorfiles directory of the
     *                 specified author
     */
    public GraphAuthor(URL codebase, String authorname, String filename) {
      this(codebase, authorname, false);

      //open .gaf file
      // added @David @14-10-2008
      termination.nocheck();
      // end added @David @14-10-2008

      NewAuthor(null, false);

      AuthorIn ain = new AuthorIn(home, filename);
      ain.LoadFromServer();
      spnConceptTree.getViewport().remove(conceptTree);
      //added by Natalia Stash
      String tmp = filename.trim();
      if (tmp.endsWith(".gaf")) tmp = tmp.substring(0, tmp.length()-4);
      projectName = tmp;

      conceptTree = new DragTree();
      initTree();
      spnConceptTree.getViewport().add(conceptTree, null);
      filterEdges();
      setAdvanced(false);

      // added @David @14-10-2008
      termination.check();
      termination.modelChanged();
      // end added @David @14-10-2008

      mainFrame.setVisible(true);
    }

    public void initAuthor() {
        try {
        	//Khoi dong cac bien static
        	myinstance         = this;
        	graph              = null;
        	conceptList        = new LinkedList();
        	sharedConceptTree  = new JTree();
        	conceptTree        = new DragTree(); //Loc Nguyen add keyword static
            projectName        = "unnamed";
        	loadTree           = null;
        	visListData        = new Vector();
        	filteredListData   = new Vector();
            //Natasha
        	authorStrategies   = new Vector();
        	existingStrategies = new Vector();
            //end Natasha

            String path = home.getPath();
            String pathttemp = path.substring(1, path.length());
            int index = pathttemp.indexOf("/");
            index++;
            dirname = path.substring(0, index);
            if (dirname.equals("/GraphAuthor")) {
              dirname = "";
            }

            crt = new ReadCRTXML("crtlist.txt", home);
            templateList = new ReadTemplateXML("templatelist.txt", home);
            //Natasha
            AuthorSTATIC.strategyList = new Vector();
            //check that this is done in one place
     		//urlTemp
     		URL urlTemp = new URL("http://" + home.getHost() + ":" + home.getPort() +
                       dirname +
                       "/servlet/authorservlets.ListFiles?extention=" +
                       ".xml" + "&userName=" + AuthorSTATIC.authorName);
     		BufferedReader in = new BufferedReader(new InputStreamReader(urlTemp.openStream()));
     		String sFile = "";
     		do {
				sFile = in.readLine();
				if (sFile != null) {
					authorStrategies.add(sFile.trim().replaceAll(".xml",""));
				}
     		} while (sFile != null);
     		in.close();
            existingStrategies = authorStrategies;
            //end Natasha
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOpen() {return mainFrame.isVisible();}

    public void AddMainFrame() {
        try {
            mainFrame = new JFrame("Graphical Author tool for WOW! applications v1.0");
            mainFrame.setJMenuBar(menuBar);
            mainFrame.setSize(800, 620);
            mainFrame.setLocation(100, 100);
            mainFrame.getContentPane().add(pnlMain);
            mainFrame.setVisible(true);
            mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            mainFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    mainFrame_Closing(e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void mainFrame_Closing(WindowEvent e) {
        int result = 0;

        if (this.graphChanged) {
            result = JOptionPane.showConfirmDialog(this,
                                                   "There is unsaved data! \n Do you want to save these changes?",
                                                   "alert",
                                                   JOptionPane.YES_NO_CANCEL_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                this.save_Button_actionPerformed(null, false);
                mainFrame.setVisible(false);
                mainFrame.dispose();
            } else {
                if (result == JOptionPane.NO_OPTION) {
                    mainFrame.setVisible(false);
                    mainFrame.dispose();
                } else {
                    mainFrame.setVisible(true);
                }
            }
        } else {
            result = JOptionPane.showConfirmDialog( this,
                                                   "This action will exit Graph Author \n Are you sure?",
                                                   "alert",
                                                   JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                mainFrame.setVisible(false);
                mainFrame.dispose();
            } else {
                mainFrame.setVisible(true);
            }
        }
    }

    // changed by @Loc Nguyen @  27-03-2008, added advanced menu
    public void AddMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");

        newItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NewAuthor(e, true);
            }
        });

        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Load_Button_actionPerformed(e);
            }
        });

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save_Button_actionPerformed(e, false);
            }
        });

        JMenuItem saveasItem = new JMenuItem("Save to WOW!");
        saveasItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Export_button_actionPerformed(e);
            }
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit_button_actionPerformed(e);
            }
        });

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveasItem);
        fileMenu.add(exitItem);

        JMenu conceptMenu = new JMenu("Concept");

        JMenuItem addConceptItem = new JMenuItem("Add Concept");
        addConceptItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddConceptButton_actionPerformed(e);
            }
        });

        JMenuItem filterRelationItem = new JMenuItem("Filter Concept Relations");
        filterRelationItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterButton_actionPerformed(e);
            }
        });

        JMenuItem zoomItem = new JMenuItem("Zoom 1:1");
        zoomItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graph.setScale(1.0);
            }
        });

        JMenuItem zoomInItem = new JMenuItem("Zoom In");
        zoomInItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graph.setScale(2 * graph.getScale());
            }
        });

        JMenuItem zoomOutItem = new JMenuItem("Zoom Out");
        zoomOutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graph.setScale(graph.getScale() / 2);
            }
        });

        conceptMenu.add(addConceptItem);
        conceptMenu.addSeparator();
        conceptMenu.add(filterRelationItem);
        conceptMenu.addSeparator();
        conceptMenu.add(zoomItem);
        conceptMenu.add(zoomInItem);
        conceptMenu.add(zoomOutItem);

        // added by @Natasha @ 12-04-2004
        JMenu strategyMenu = new JMenu("Strategies");
        JMenuItem applyStrategiesItem = new JMenuItem("Apply strategies");
        applyStrategiesItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //applyStrategyButton_actionPerformed(e);
        		Strategies strategiesWindow = new Strategies(mainFrame);
        		strategiesWindow.show();
            }
        });
        strategyMenu.add(applyStrategiesItem);
        // end added by @Natasha

        // added by @Loc Nguyen @  27-03-2008
        JMenu advancedMenu = new JMenu("Advanced");
        showAdvanced = new JCheckBoxMenuItem("Advanced mode");
        setAdvanced(false);
        showAdvanced.addItemListener(new java.awt.event.ItemListener() {
          public void itemStateChanged(ItemEvent e) {
            AbstractButton button = (AbstractButton) e.getItem();
            if (button.isSelected()) {
              setAdvanced(true);
            } else {
              setAdvanced(false);
            }
          }
        });

        advancedMenu.add(showAdvanced);
        // end added by @Bart

        JMenu helpMenu = new JMenu("Help");
        JMenuItem howToItem = new JMenuItem("How to use GraphAuthor");
        howToItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HelpBox help = new HelpBox(mainFrame, home);
                help.show();
            }
        });

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AboutBox about = new AboutBox(mainFrame, home);
                about.show();
            }
        });

        helpMenu.add(howToItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(conceptMenu);
        //Natasha
        menuBar.add(strategyMenu);
        //endNatasha
        // added by @Loc Nguyen @  27-03-2008
        menuBar.add(advancedMenu);
        menuBar.add(helpMenu);
    }

    public void pasteTree() {
        this.graphChanged = true;

        TreePath p = conceptTree.getSelectionPath();

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) cutPath.getLastPathComponent();
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
        parent.remove(node);

        if (p != null) {
            DefaultMutableTreeNode n = (DefaultMutableTreeNode) p.getLastPathComponent();
            n.add(cutTreeNode);
        } else {
            DefaultMutableTreeNode n = (DefaultMutableTreeNode) conceptTree.getModel()
                                                                           .getRoot();
            n.add(cutTreeNode);
        }

        if (p != null) {
            ((DefaultTreeModel) conceptTree.getModel()).reload();
            conceptTree.setSelectionPath(p);
            conceptTree.expandPath(p);
        }
    }

    public void initTree() {
        conceptTree.addTreeSelectionListener(this);

        // popup menu
        final JPopupMenu jpop = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("add concept");
        JMenuItem cutItem = new JMenuItem("cut");
        JMenuItem pasteItem = new JMenuItem("paste");
        JMenuItem deleteItem = new JMenuItem("delete");
        JMenuItem editItem = new JMenuItem("edit");
        JMenuItem assignResources = new JMenuItem("assign resources");
        menuAdvAttributes = new JMenuItem("attributes");

        addItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddConceptButton_actionPerformed(e);
            }
        });

        cutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TreePath p = conceptTree.getSelectionPath();

                if (p != null) {
                    cutConcept = selection;
                    cutPath = p;
                    cutTreeNode = (DefaultMutableTreeNode) p.getLastPathComponent();
                    JOptionPane.showMessageDialog(null,
                                                  "Please select the destination node and click on paste",
                                                  "information",
                                                  JOptionPane.OK_OPTION);
                }
            }
        });

        pasteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cutTreeNode != null) {
                    pasteTree();
                }
            }
        });

        deleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                conceptTreeKeyEvent((KeyEvent) null);
            }
        });

        editItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editConceptEvent(e);
            }
        });

        // added by @Loc Nguyen @  27-03-2008
        assignResources.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
              // show resources dialog
              assignResourcesEvent(e);
            }
        });

        menuAdvAttributes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
              // edit attributes
              editAttributesEvent(e);
            }
        });
        // end added by @Loc Nguyen @  27-03-2008

        jpop.add(addItem);
        jpop.addSeparator();
        jpop.add(cutItem);
        jpop.add(pasteItem);
        jpop.add(deleteItem);
        jpop.add(editItem);
        jpop.add(assignResources);
        jpop.add(menuAdvAttributes);

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                boolean rightClicked = e.isMetaDown();

                if (rightClicked) {
                    TreePath p = conceptTree.getSelectionPath();

                    if (p != null) {
                        jpop.show((Component) e.getSource(), e.getX(), e.getY());
                    }
                }
            }
        };

        conceptTree.addMouseListener(mouseListener);

        KeyListener keyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    conceptTreeKeyEvent(e);
                }
            }
        };

        conceptTree.addKeyListener(keyListener);

        DefaultMutableTreeNode n;

        if (loadTree == null) {
            n = new DefaultMutableTreeNode(GraphAuthor.projectName);
        } else {
            // sets the loaded tree
            n = loadTree;


            // loadTree must by null so the system can see that the load function is finished.
            loadTree = null;
        }

        DefaultTreeModel m = new DefaultTreeModel(n);
        conceptTree.setModel(m);

        // added @David @11-10-2008
        GraphAuthor.sharedConceptTree.setModel(GraphAuthor.conceptTree.getModel());
        conceptTree.getModel().addTreeModelListener(new MyTreeModelListener());
        // end added @David

        conceptTree.invalidate();
    }

    //Get a parameter value
    public String getParameter(String key, String def) {
        return isStandalone
               ? System.getProperty(key, def)
               : ((getParameter(key) != null) ? getParameter(key) : def);
    }


    //Initialize the applet
    public void init() {
        home = getCodeBase();
        if (opened!=0) {
                    JOptionPane.showMessageDialog(null,
                                                  "Graph Author is already open",
                                                  "information",
                                                  JOptionPane.OK_OPTION);
                                                  return;
        }

        AuthorLogin alogin = new AuthorLogin(home);
    	if (!alogin.login()) {
    		try {
				String codebase = home.toString();
				int index = codebase.lastIndexOf("/");
				index = codebase.substring(0, index).lastIndexOf("/");
				String base = codebase.substring(0, index+1);
				getAppletContext().showDocument(new URL(base+"accessdenied.html"), "_top");
			} catch(Exception e) { e.printStackTrace(); }
    	}
        else {

        	try {

				AuthorSTATIC.authorName = alogin.getUserName();
				
				this.initAuthor();
				this.jbInit();
				this.initTree();
				this.AddExtraComponents();
				this.AddMenu();
				this.AddMainFrame();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }

    //Start the applet
    public void start() {
        opened--;
    }

    //Stop the applet
    public void stop() {
        opened++;
    }

    //Destroy the applet
    public void destroy() {
    	//Huy cac bien static
    	myinstance         = null;
    	graph              = null;
    	conceptList        = null;
    	sharedConceptTree  = null;
    	conceptTree        = null; //Loc Nguyen them vao static
        projectName        = null;
    	loadTree           = null;
    	visListData        = null;
    	filteredListData   = null;
        //Natasha
    	authorStrategies   = null;
    	existingStrategies = null;
        //end Natasha
    }

    //Get Applet information
    public String getAppletInfo() {
        return "GrapAuthor applet v 1.0";
    }

    //Get parameter info
    public String[][] getParameterInfo() {
        return null;
    }

    private void jbInit() throws Exception {

        pnlMain.setLayout(new BorderLayout());
        splConceptGraph.setBorder(BorderFactory.createLoweredBevelBorder());
        splConceptGraph.setLastDividerLocation(50);
        spnConceptTree.setMinimumSize(new Dimension(50, 50));
        spnConceptTree.setPreferredSize(new Dimension(50, 50));
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        this.AddToolBarItems();

        pnlMain.add(toolBar, "North");
        pnlMain.add(splConceptGraph, null);
        splConceptGraph.add(spnConceptTree, JSplitPane.LEFT);
        spnConceptTree.getViewport().add(conceptTree, null);
        splConceptGraph.setDividerLocation(150);
    }

    public void AddToolBarItems() {
        try {
            URL url = new URL(this.home.toString() + "icons/new.gif");

            JButton buttonNew = new JButton(new ImageIcon(url));
            buttonNew.setToolTipText("New WOW! application");
            buttonNew.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    NewAuthor(e, true);
                }
            });
            toolBar.add(buttonNew);

            url = new URL(this.home.toString() + "icons/open.gif");

            JButton buttonOpen = new JButton(new ImageIcon(url));
            buttonOpen.setToolTipText("Open WOW! application");
            buttonOpen.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Load_Button_actionPerformed(e);
                }
            });
            toolBar.add(buttonOpen);

            url = new URL(this.home.toString() + "icons/save.gif");

            JButton buttonSave = new JButton(new ImageIcon(url));
            buttonSave.setToolTipText("Save WOW! application");
            buttonSave.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    save_Button_actionPerformed(e, false);
                }
            });
            toolBar.add(buttonSave);

            url = new URL(this.home.toString() + "icons/export.gif");

            JButton buttonExport = new JButton(new ImageIcon(url));
            buttonExport.setToolTipText("Commit into WOW! database");
            buttonExport.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Export_button_actionPerformed(e);
                }
            });
            toolBar.add(buttonExport);
            toolBar.addSeparator();

            url = new URL(this.home.toString() + "icons/add.gif");

            JButton buttonAdd = new JButton(new ImageIcon(url));
            buttonAdd.setToolTipText("Add new Concept");
            buttonAdd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AddConceptButton_actionPerformed(e);
                }
            });

            toolBar.add(buttonAdd);

            url = new URL(this.home.toString() + "icons/filter.gif");

            JButton buttonFilter = new JButton(new ImageIcon(url));
            buttonFilter.setToolTipText("Filter concept relations");
            buttonFilter.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    filterButton_actionPerformed(e);
                }
            });
            toolBar.add(buttonFilter);

            url = new URL(this.home.toString() + "icons/cycle.gif");
            JButton buttonCycle = new JButton(new ImageIcon(url));
            buttonCycle.setToolTipText("Test cycles");
            buttonCycle.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    TestCycle();
                }
            });
            toolBar.add(buttonCycle);

            toolBar.addSeparator();
            url = new URL(this.home.toString() + "icons/zoom.gif");
            JButton buttonZoom = new JButton(new ImageIcon(url));
            buttonZoom.setToolTipText("Zoom 1:1");
            buttonZoom.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    graph.setScale(1.0);
                }
            });
            toolBar.add(buttonZoom);

            url = new URL(this.home.toString() + "icons/zoomin.gif");

            JButton buttonZoomIn = new JButton(new ImageIcon(url));
            buttonZoomIn.setToolTipText("Zoom in");
            buttonZoomIn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    graph.setScale(2 * graph.getScale());
                }
            });
            toolBar.add(buttonZoomIn);

            url = new URL(this.home.toString() + "icons/zoomout.gif");

            JButton buttonZoomOut = new JButton(new ImageIcon(url));
            buttonZoomOut.setToolTipText("Zoom out");
            buttonZoomOut.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    graph.setScale(graph.getScale() / 2);
                }
            });
            toolBar.add(buttonZoomOut);

            Dimension dim = new Dimension(180, 6);
            toolBar.addSeparator(dim);

            JLabel crtLabel = new JLabel("crt: ");
            toolBar.add(crtLabel);
            RelationList.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    RelationList_actionPerformed(e);
                }
            });
            toolBar.add(RelationList);
        } catch (Exception e) {
        }
    }

    public void TestCycle() {
        if (termination.containsCycle())
            JOptionPane.showMessageDialog(this,
                                          "Cycles have been detected!",
                                          "information",
                                          JOptionPane.OK_OPTION);
        else
            JOptionPane.showMessageDialog(this,
                                          "No cycles found",
                                          "information",
                                          JOptionPane.INFORMATION_MESSAGE);
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            this.graphChanged = true;
            this.RemoveSelectedItems();
        }
    }

    public void RemoveSelectedItems() {
        Object[] cells = GraphAuthor.graph.getSelectionCells();
        graph.getModel().remove(cells);
    }

    void AddExtraComponents() {
        model = new DefaultGraphModel(true);
        graph = new MyGraph(model);
        // added @David @11-10-2008
        termination = new Termination();
        termination.addCycleListener(new MyCycleListener());
        model.addGraphModelListener(new MyGraphListener());
        // end added @David
        new DropTarget(graph, DnDConstants.ACTION_COPY_OR_MOVE, this);
        graph.addKeyListener(this);

        graphScroll = new JScrollPane(graph);
        splConceptGraph.add(graphScroll, JSplitPane.RIGHT);

        for (Iterator i = AuthorSTATIC.CRTList.iterator(); i.hasNext();) {
            CRTConceptRelationType crel = (CRTConceptRelationType) i.next();
            RelationList.addItem(crel.name);
            GraphAuthor.visListData.add(crel.name);

            if (crel.properties.unary.booleanValue() == true) {
            }
        }
    }

    void NewAuthor(ActionEvent e, boolean confirmation) {
        int result = 0;

        if (confirmation && (this.graphChanged)) {
            result = JOptionPane.showConfirmDialog( this,
                                                   "There is unsaved data. This action will erase all unsaved data \n Are you sure?",
                                                   "alert",
                                                   JOptionPane.YES_NO_OPTION);
        }

        if (result == 0) {
            this.graphChanged = false;

            Object[] cells = GraphAuthor.graph.getRoots();
            graph.getModel().remove(cells);
            conceptList.clear();

            spnConceptTree.getViewport().remove(conceptTree);
            conceptTree = new DragTree();
            GraphAuthor.projectName = "unnamed";
            this.initTree();
            spnConceptTree.getViewport().add(conceptTree, null);
            //Natasha
            //add author strategies
            authorStrategies.clear();
			try {
				URL url = new URL("http://" + home.getHost() + ":" + home.getPort() +
				                  dirname +
				                  "/servlet/authorservlets.ListFiles?extention=" +
				                  ".xml" + "&userName=" + AuthorSTATIC.authorName);
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String sFile = "";

				do {
					sFile = in.readLine();
					if (sFile != null) {
						authorStrategies.add(sFile.trim().replaceAll(".xml",""));
					}

				} while (sFile != null);
				in.close();

			} catch (Exception ex) {
			}
			existingStrategies = authorStrategies;
			AuthorSTATIC.strategyList = new Vector();
			//end Natasha
        }
    }

    public void valueChanged(TreeSelectionEvent event) {
        if (conceptTree.getLastSelectedPathComponent() != null) {
            selection = conceptTree.getLastSelectedPathComponent().toString().trim();
        }
    }

    public void getConceptsFromTree(DefaultMutableTreeNode element) {
        for (Enumeration i = element.children(); i.hasMoreElements();) {
            DefaultMutableTreeNode tnode = (DefaultMutableTreeNode) i.nextElement();
            this.treeConceptList.add(tnode.toString());
            this.getConceptsFromTree(tnode);
        }
    }

    // changed by @Loc Nguyen @  23-04-2008
    void editConceptEvent(ActionEvent e) {
        this.graphChanged = true;

        WOWOutConcept aout = null;
        WOWOutConcept aoutToEdit = null;

        String oldName = this.selection;
        try {
          oldName = oldName.trim();
        } catch (Exception te) {
        }

        // find description and resource information
        String oldResource = "";
        String oldDescription = "";
        String oldTemplate="";
        boolean oldNoCommit = false;
        String oldStable = "";
        String oldStable_expr = "";
        String oldConcepttype = "";
        String oldTitle = "";

        for (Iterator i = GraphAuthor.conceptList.iterator(); i.hasNext();) {
            aout = (WOWOutConcept) i.next();

            if (aout.name.trim().equals(oldName.trim())) {
              aoutToEdit = aout;
              oldResource = aout.resource.trim();
              oldDescription = aout.description.trim();
              oldTemplate = aout.template.trim();
              oldNoCommit = aout.nocommit;
              oldStable = aout.stable;
              oldStable_expr = aout.stable_expr;
              oldConcepttype = aout.concepttype;
              oldTitle = aout.title;
            }
        }

        EditDialog editD = new EditDialog(mainFrame, oldName, oldDescription,oldResource, oldTemplate, oldNoCommit, showAdvanced.isSelected(), oldStable, oldStable_expr, oldConcepttype, oldTitle);
        editD.show();

        if (editD.cancelled) {
            return;
        }
        setresource = false;
        for (Iterator i = AuthorSTATIC.templateList.iterator(); i.hasNext();) {
            ConceptTemplate ctemp = (ConceptTemplate) i.next();
            if (ctemp.name.equals(editD.newTemplate)) {
                if (ctemp.hasresource.trim().toLowerCase().equals("true"))
                setresource = true;
                break;
            }
        }
        if ((editD.newResource.indexOf("http:") == -1) &&
                (editD.newResource.indexOf("file:") == -1) && setresource) {
            JOptionPane.showConfirmDialog( this,
                                                       "Invalid resource url \n Use 'http:/' or ''file:/'",
                                                       "alert",
                                                       JOptionPane.PLAIN_MESSAGE);
        return;
        }
          if (editD.newResource.equals("file:/")) {
            JOptionPane.showConfirmDialog( this,
                "Please, specify a correct resource url!",
                "alert",
                JOptionPane.PLAIN_MESSAGE);
            return;
          }

        try {
          editD.newConceptName.trim();
        } catch (Exception te) {
          // hmm new concept name must be null
        }

        if (this.inTree(editD.newConceptName.trim()) &&
                (!oldName.equals(editD.newConceptName))) {
            JOptionPane.showMessageDialog( this,
                                          "Concept is not unique, rename is cancelled!",
                                          "information", JOptionPane.OK_OPTION);

            return;
        }

        DefaultMutableTreeNode selObj = (DefaultMutableTreeNode) GraphAuthor.conceptTree.getLastSelectedPathComponent();

        // update tree
        if ((null == selObj) ||
                (!(selObj instanceof DefaultMutableTreeNode))) {
            System.out.println("invalid selection for editing!");

            return;
        }

        try {
            selObj.setUserObject(editD.newConceptName.trim());
            ((DefaultTreeModel) conceptTree.getModel()).reload();
            this.EditConceptInGraph(oldName, editD.newConceptName.trim());
        } catch (Exception e1) {
            System.out.println("error during editing.");
        }

        // update conceptlist
        aoutToEdit.name = editD.newConceptName.trim();
        try {
          aoutToEdit.description = editD.newDescription.trim();
        }
        catch (Exception e1) {
          aoutToEdit.description = editD.newDescription;
        }
        try {
          aoutToEdit.resource = editD.newResource.trim();
        }
        catch (Exception e1) {
          aoutToEdit.resource = editD.newResource;
        }
        aoutToEdit.concepttype = editD.newconcepttype;
        if (aoutToEdit.concepttype!=null) aoutToEdit.concepttype = aoutToEdit.concepttype.trim();
        aoutToEdit.title = editD.newtitle;
        if (aoutToEdit.title!=null) aoutToEdit.title = aoutToEdit.title.trim();

        aoutToEdit.template = editD.newTemplate.trim();
        if (!aoutToEdit.template.equals(oldTemplate)) {
            //aoutToEdit.attributeList = new LinkedList();
            aoutToEdit.attributeList.clear();
            aoutToEdit.AddTemplateAttributes();
        }

        //added @David @11-11-2008
        termination.modelChanged();
        //end added @David @11-11-2008

        // added by @Loc Nguyen @  28-05-2008
        if (showAdvanced.isSelected()) {
          aoutToEdit.nocommit = editD.newNoCommit;
          aoutToEdit.stable = editD.newStable;
          aoutToEdit.stable_expr = editD.newStable_expr;
        } else {
          aoutToEdit.nocommit = oldNoCommit;
          aoutToEdit.stable = oldStable;
          aoutToEdit.stable_expr = oldStable_expr;
        }

/*
        this.conceptList.remove(aoutToRemove); //removes the old concept

        WOWOutConcept anew = new WOWOutConcept();
        anew.name = editD.newConceptName.trim();
        try {
          anew.description = editD.newDescription.trim();
        } catch (Exception e1) {
          anew.description = editD.newDescription;
        }
        try {
          anew.resource = editD.newResource.trim();
        } catch (Exception e1) {
          anew.resource = editD.newResource;
        }
        anew.template = editD.newTemplate.trim();
        // added by @Loc Nguyen @  28-05-2008
        if (showAdvanced.isSelected()) {
          anew.nocommit = editD.newNoCommit;
        } else {
          anew.nocommit = oldNoCommit;
        }
        // end added by @Bart
        this.conceptList.add(anew);
*/


    }

    /**
     *
     * @param e click event
     * added by @Loc Nguyen @ 01-05-2008
     */
    public void editAttributesEvent(ActionEvent e) {
      WOWOutConcept aout = null;
      WOWOutConcept concept = null;
      String oldName = this.selection;
      
      //Loc Nguyen add
      if(oldName == null) {}
      
      for (Iterator i = GraphAuthor.conceptList.iterator(); i.hasNext();) {
          aout = (WOWOutConcept) i.next();

          if (aout.name.trim().equals(selection.trim())) {
              // concept found
              concept = aout;
          }
      }
      Attributes attributeWindow = new Attributes(mainFrame,concept);
      attributeWindow.show();
    }

    /**
     *
     * @param e
     * Added by Bart @ 07-04-2008
     */
    public void assignResourcesEvent(ActionEvent e) {
      WOWOutConcept aout = null;
      WOWOutConcept concept = null;
      String oldName = this.selection;

      //Loc Nguyen add
      if(oldName == null) {}

      for (Iterator i = GraphAuthor.conceptList.iterator(); i.hasNext();) {
          aout = (WOWOutConcept) i.next();
          if (aout.name.trim().equals(selection.trim())) {
              // concept found
              concept = aout;
          }
      }
      if (concept != null) {
        ResourcesEditor resEditor = new ResourcesEditor(mainFrame, concept);
        resEditor.show();
      } else {
        System.out.println("GraphAuthor: assignResourcesEvent: no concept selected.");
      }
    }

    // added by @Loc Nguyen @  31-03-2008
    // checks or unchecks the "Advanced" menu entry
    private void setAdvanced(boolean selected) {
      showAdvanced.setSelected(selected);
      menuAdvAttributes.setVisible(selected);
    }

    public void EditConceptInGraph(String oldName, String newName) {
        Object[] cells = graph.getRoots();

        if (cells != null) {
            CellView[] views = GraphAuthor.graph.getView().getMapping(cells);

            for (int i = 0; i < views.length; i++) {
                if (views[i].getCell().getClass().getName()
                            .equals("com.jgraph.graph.DefaultGraphCell")) {
                    DefaultGraphCell dcell = (DefaultGraphCell) views[i].getCell();

                    if (dcell.toString().equals(oldName)) {
                        dcell.setUserObject(newName);

                        Map prop = dcell.getAttributes();
                        Map propMap = new Hashtable();
                        propMap.put(dcell, prop);
                        graph.getModel().edit(null, propMap, null, null);
                    }
                }
            }
        }
    }

    public void RemoveUnaryRelations() {
        this.graphChanged = true;

        Object cell = graph.getSelectionCell();

        if (cell != null) {
            if (cell.getClass().getName()
                    .equals("com.jgraph.graph.DefaultGraphCell")) {
                DefaultGraphCell dcell = (DefaultGraphCell) cell;
                Map prop = dcell.getAttributes();
                Map propMap = new Hashtable();
                GraphConstants.setBackground(prop, Color.lightGray);
                propMap.put(dcell, prop);
                graph.getModel().edit(null, propMap, null, null);
            }
        }
    }

    public void AddUnaryRelations() {
        Object cell = graph.getSelectionCell();

        if (cell != null) {
            if (cell.getClass().getName()
                    .equals("com.jgraph.graph.DefaultGraphCell")) {
                DefaultGraphCell dcell = (DefaultGraphCell) cell;
                Map prop = dcell.getAttributes();
                UnaryDialog uDialog = new UnaryDialog(this.mainFrame,
                                                      (Hashtable) prop.get(
                                                              "unaryRelations"));
                uDialog.show();

                if (uDialog.cancelled == true) {
                    return;
                }

                prop.put("unaryRelations", uDialog.unaryRel);

                Map propMap = new Hashtable();

                if (uDialog.unaryRel.isEmpty()) {
                    GraphConstants.setBackground(prop, Color.lightGray);
                } else {
                    GraphConstants.setBackground(prop, Color.red);
                }

                propMap.put(dcell, prop);
                graph.getModel().edit(null, propMap, null, null);
            }
        }
    }

    public void EditLabel() {
        Object cell = graph.getSelectionCell();

        if (cell != null) {
            if (cell.getClass().getName()
                    .equals("com.jgraph.graph.DefaultGraphCell")) {
                DefaultGraphCell dcell = (DefaultGraphCell) cell;
                Map prop = dcell.getAttributes();
                Hashtable testcrt = (Hashtable) prop.get("unaryRelations");
                //Loc Nguyen add
                if(testcrt == null) {}


                String oldlabel = (String) prop.get("ulabel");
                String label = JOptionPane.showInputDialog("Please enter the label:",
                                                     oldlabel);

                if (label == null) {
                    label = oldlabel;
                }

                prop.put("ulabel", label);

                Map propMap = new Hashtable();
                GraphConstants.setBackground(prop, Color.red);
                propMap.put(dcell, prop);
                graph.getModel().edit(null, propMap, null, null);
            }
        }
    }

    //
    // PopupMenu
    //
    public JPopupMenu createPopupMenu(final Point pt, final Object cell) {
        JPopupMenu menu = new JPopupMenu();

        if (!graph.isSelectionEmpty()) {
            //        com.jgraph.graph.DefaultGraphCell
            if (graph.getSelectionCell().getClass().toString()
                     .lastIndexOf("DefaultGraphCell") != -1) {
                //  menu.addSeparator();
                menu.add(new AbstractAction("Unary Relations") {
                    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void actionPerformed(ActionEvent e) {
                        AddUnaryRelations();
                    }
                });
            }
        }

        return menu;
    }

    public void deleteInGraph(String delConcept) {
        delConcept = delConcept.trim();

        LinkedList rcells = new LinkedList();
        Object[] cells = GraphAuthor.graph.getRoots();

        if (cells != null) {
            CellView[] views = GraphAuthor.graph.getView().getMapping(cells);

            for (int i = 0; i < views.length; i++) {
                if (views[i].getCell().getClass().getName()
                            .equals("com.jgraph.graph.DefaultGraphCell")) {
                    if (views[i].getCell().toString().trim()
                                .equals(delConcept.trim())) {
                        Object dcell = views[i].getCell();
                        rcells.add(dcell);
                    }
                }

                if (views[i].getCell().getClass().getName()
                            .equals("com.jgraph.graph.DefaultEdge")) {
                    DefaultEdge oEdge = (DefaultEdge) views[i].getCell();
                    GraphModel model = GraphAuthor.graph.getModel();
                    DefaultEdge dedge = (DefaultEdge) oEdge;
                    dedge.getAttributes();
                    String source = "";
                    String destination = "";

                    if (model.getSource(oEdge) != null) {
                        source = model.getParent(model.getSource(oEdge)).toString().trim();
                    }

                    if (model.getTarget(oEdge) != null) {
                        destination = model.getParent(model.getTarget(oEdge)).toString().trim();
                    }

                    if (source.equals(delConcept) ||
                            destination.equals(delConcept)) {
                        Object dcell = views[i].getCell();
                        rcells.add(dcell);
                    }
                }
            }
        }

        graph.getModel().remove(rcells.toArray());
    }

    public void conceptTreeKeyEvent(KeyEvent e) {
        // removes the cut selection
        this.cutTreeNode = null;

        TreePath p = conceptTree.getSelectionPath();
        DefaultMutableTreeNode n = (DefaultMutableTreeNode) p.getLastPathComponent();
        this.getConceptsFromTree(n);

        for (Iterator i = this.treeConceptList.iterator(); i.hasNext();) {
            String conceptn = (String) i.next();
            this.deleteInGraph(conceptn);

            // find de concept in the internal list and remove it
            WOWOutConcept delConcept = null;

            for (Iterator j = GraphAuthor.conceptList.iterator(); j.hasNext();) {
                WOWOutConcept removeConcept = (WOWOutConcept) j.next();

                if (removeConcept.name.equals(conceptn.toString().trim())) {
                    delConcept = removeConcept;
                }
            }

            if (delConcept != null) {
            	GraphAuthor.conceptList.remove(delConcept);

            }
        }

        this.deleteInGraph(n.toString());

        WOWOutConcept delConcept = null;

        for (Iterator j = GraphAuthor.conceptList.iterator(); j.hasNext();) {
            WOWOutConcept removeConcept = (WOWOutConcept) j.next();

            if (removeConcept.name.equals(n.toString().trim())) {
                delConcept = removeConcept;

            }
        }

        if (delConcept != null) {
            GraphAuthor.conceptList.remove(delConcept);

        }

        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) n.getParent();
        parent.remove(n);

        ((DefaultTreeModel) conceptTree.getModel()).reload();

        p = new TreePath(parent.getPath());
        conceptTree.expandPath(p);
    }

    public boolean inTree(String conceptname) {
        boolean result = false;

        for (Iterator i = conceptList.iterator(); i.hasNext();) {
            WOWOutConcept concept = (WOWOutConcept) i.next();

            if (concept.name.equals(conceptname)) {
                result = true;
                return result;
            }
        }

        return false;
    }

    void AddConceptButton_actionPerformed(ActionEvent e) {
        TreePath p = null;
        AddConcept addconcept = new AddConcept(mainFrame, showAdvanced.isSelected());
        addconcept.show();

        if (addconcept.cancelled == false) {
            this.graphChanged = true;
            p = conceptTree.getSelectionPath();

            for (Iterator i = addconcept.conceptlist.iterator(); i.hasNext();) {
                WOWOutConcept cout = (WOWOutConcept) i.next();
                if (this.inTree(cout.name) == false) {
                    conceptList.add(cout); //adds concept to the internal conceptlist

                    //  conceptTree.add(cout.name);
                    if (p != null) {
                        DefaultMutableTreeNode n = (DefaultMutableTreeNode) p.getLastPathComponent();
                        n.add(new DefaultMutableTreeNode(cout.name));
                    } else {
                        DefaultMutableTreeNode n = (DefaultMutableTreeNode) conceptTree.getModel().getRoot();
                        new DefaultMutableTreeNode("test");
                        n.add(new DefaultMutableTreeNode(cout.name));
                    }

                    //  ConceptList.setListData(listData);
                } else {
                	JOptionPane.showMessageDialog(this,"Concept: " + cout.name +" already exists!", "Error",JOptionPane.ERROR_MESSAGE);
                }
            }

            if (p != null) {
                ((DefaultTreeModel) conceptTree.getModel()).reload();
                conceptTree.setSelectionPath(p);
                conceptTree.expandPath(p);
            // added by @Loc Nguyen @  13-05-2008
            // fixes bug that the tree model wasn't updated when a concept was added
            // this happend when nothing was selected when that was done
            } else {
              ((DefaultTreeModel) conceptTree.getModel()).reload();
            }
            // end added by @Loc Nguyen @  13-05-2008
        }
    }

    public void addConcept(String conceptName, int x, int y) {
        int ongraph = 0;
        this.graphChanged = true;

        Object[] cells = GraphAuthor.graph.getRoots();

        if (cells != null) {
            CellView[] views = GraphAuthor.graph.getView().getMapping(cells);

            for (int i = 0; i < views.length; i++) {
                if (views[i].getCell().getClass().getName().equals("com.jgraph.graph.DefaultGraphCell")) {
                    if (views[i].getCell().toString().trim().equals(conceptName.trim())) {
                        ongraph = 1;

                        DefaultGraphCell dGraph = (DefaultGraphCell) views[i].getCell();
                        graph.setSelectionCell(dGraph);
                    }
                }
            }
        }

        if (ongraph == 0) {
          DefaultGraphCell vertex = new DefaultGraphCell(conceptName);
          vertex.add(new DefaultPort());

          //                vertex.add(new DefaultPort());
          Point point = graph.snap(new Point(x, y));
          Dimension size = new Dimension(80, 20);
          Map map = GraphConstants.createMap();
          GraphConstants.setBounds(map, new Rectangle(point, size));
          GraphConstants.setBorderColor(map, Color.magenta);
          GraphConstants.setBackground(map, Color.lightGray);
          GraphConstants.setOpaque(map, true);
          GraphConstants.setEditable(map, false);
          GraphConstants.setAutoSize(map, true);
          GraphConstants.setSizeable(map, true);

          Hashtable attributes = new Hashtable();
          attributes.put(vertex, map);

          graph.getModel().insert(new Object[] {vertex}, null, null, attributes);
        }
    }

    /**
     *  Retrieves the style and color from the concept relation type.
     *
     * @return ArrowStyle the color and style of the crt.
     */
    public ArrowStyle getArrowStyle() {
        ArrowStyle aStyle = new ArrowStyle();
        aStyle.color = Color.black;

        String relationName = (String) RelationList.getSelectedItem();
        aStyle.crtname = relationName;

        for (Iterator i = AuthorSTATIC.CRTList.iterator(); i.hasNext();) {
            CRTConceptRelationType crel = (CRTConceptRelationType) i.next();

            if (relationName.equals(crel.name)) {
                if (crel.color.equals("black")) {
                    aStyle.color = Color.black;
                }

                if (crel.color.equals("blue")) {
                    aStyle.color = Color.blue;
                }

                if (crel.color.equals("cyan")) {
                    aStyle.color = Color.cyan;
                }

                if (crel.color.equals("gray")) {
                    aStyle.color = Color.gray;
                }

                if (crel.color.equals("green")) {
                    aStyle.color = Color.green;
                }

                if (crel.color.equals("magenta")) {
                    aStyle.color = Color.magenta;
                }

                if (crel.color.equals("red")) {
                    aStyle.color = Color.red;
                }

                if (crel.color.equals("yellow")) {
                    aStyle.color = Color.yellow;
                }

                if (crel.style.equals("full")) {
                    aStyle.lineStyle[0] = 1;
                    aStyle.lineStyle[1] = 0;
                }

                if (crel.style.equals("dashed")) {
                    aStyle.lineStyle[0] = 5;
                    aStyle.lineStyle[1] = 6;
                }

                if (crel.style.equals("dotted")) {
                    aStyle.lineStyle[0] = 1;
                    aStyle.lineStyle[1] = 1;
                }
            }
        }

        return aStyle;
    }

    // Insert a new Edge between source and target
    public void connect(Port source, Port target) {
        ArrowStyle aStyle;
        aStyle = this.getArrowStyle();

        // Connections that will be inserted into the Model
        ConnectionSet cs = new ConnectionSet();

        // Construct Edge with no label
        DefaultEdge edge = new DefaultEdge();


        // Create Connection between source and target using edge
        cs.connect(edge, source, target);

        // Create a Map thath holds the attributes for the edge
        Map map = GraphConstants.createMap();


        //GraphConstants.set
        GraphConstants.setLineColor(map, aStyle.color);
        GraphConstants.setDashPattern(map, aStyle.lineStyle);


        // custom properties
        map.put("crt", aStyle.crtname);


        // Add a Line End Attribute
        GraphConstants.setLineEnd(map, GraphConstants.SIMPLE);

        // Construct a Map from cells to Maps (for insert)
        Hashtable attributes = new Hashtable();


        // Associate the Edge with its Attributes
        attributes.put(edge, map);


        // Insert the Edge and its Attributes
        graph.getModel().insert(new Object[] { edge }, cs, null, attributes);
    }

    // Insert a new Vertex at point
    public void insert(Point point) {
        // Construct Vertex with no Label
        DefaultGraphCell vertex = new DefaultGraphCell();


        // Add one Floating Port
        vertex.add(new DefaultPort());


        // Snap the Point to the Grid
        point = graph.snap(new Point(point));

        // Default Size for the new Vertex
        Dimension size = new Dimension(25, 25);

        // Create a Map that holds the attributes for the Vertex
        Map map = GraphConstants.createMap();


        // Add a Bounds Attribute to the Map
        GraphConstants.setBounds(map, new Rectangle(point, size));


        // Add a Border Color Attribute to the Map
        GraphConstants.setBorderColor(map, Color.black);


        // Add a White Background
        GraphConstants.setBackground(map, Color.white);


        // Make Vertex Opaque
        GraphConstants.setOpaque(map, true);

        // Construct a Map from cells to Maps (for insert)
        Hashtable attributes = new Hashtable();


        // Associate the Vertex with its Attributes
        attributes.put(vertex, map);


        // Insert the Vertex and its Attributes
        graph.getModel()
             .insert(new Object[] { vertex }, null, null, attributes);
    }

    void RelationList_actionPerformed(ActionEvent e) {
        String relationName = ((String) RelationList.getSelectedItem()).trim();

        if (GraphAuthor.filteredListData.contains(relationName)) {
        	GraphAuthor.filteredListData.removeElement(relationName);
        	GraphAuthor.visListData.add(relationName);
            this.filterEdges();
        }
    }

    public void edgeGXL(JGraph graph, Object id, Object edge) {
        graph.getModel();
        DefaultEdge dedge = (DefaultEdge) edge;
        dedge.getAttributes();
    }

    public void testEdge() {
        Object[] cells = graph.getDescendants(graph.getRoots());
        int edges = 0;

        for (int i = 0; i < cells.length; i++)
            if (cells[i].getClass().getName()
                        .equals("com.jgraph.graph.DefaultEdge")) {
                this.edgeGXL(graph, new Integer(edges++), cells[i]);
            }
    }

    public void getEdgeInfo(Object oEdge) {
        graph.getModel();
        DefaultEdge dedge = (DefaultEdge) oEdge;
        Map edgeAtt = dedge.getAttributes();

        edgeAtt.get("crt");
    }

    /**
     * Handles mouseclicks performed on the save button.
     * The currently opened file is saved with the current name or with a
     * filename entered by the user.
     * @param e the event that triggered this action
     * @param noOutput output data to WOW! database
     */
    private void save_Button_actionPerformed(ActionEvent e, boolean noOutput) {
      //sets the current dragtree model into a static/shared tree
      sharedConceptTree.setModel(conceptTree.getModel());

      //FileSave saveDialog = new FileSave(this.home, mainFrame, this.projectName );
      ServerFileChooser saveDialog =
        new ServerFileChooser(
              home,
              AuthorSTATIC.authorName,
              AMtc.AUTHOR_FILES_MODE,
              mainFrame,
              true
      );

      String oldProjectName = projectName;
      if (noOutput) {//use existing filename
    	  ServerFileChooser.fileName = projectName;
      }
      else {//prompt user to enter filename
        String[] ff = {".gaf"};
        saveDialog.showSaveDialog(ff);
      }

      String fileName = ServerFileChooser.fileName;
      if (fileName == null) return;

      if (fileName.endsWith(".gaf"))
        fileName = fileName.trim().substring(0, fileName.length()-4);

      //save file
      boolean exists = false;

      WOWOutConcept wowc = null;
      WOWOutConcept aproject = null;

      //added by Natalia Stash
      Vector conceptsvector = new Vector();
      String wowcresource = "";

      for (Iterator i = conceptList.iterator(); i.hasNext();) {//check for double resource usage
        wowc = (WOWOutConcept) i.next();
        //check name of root concept
        if (wowc.name.equals(oldProjectName)) wowc.name = fileName;
        wowcresource = wowc.resource.trim();
        if (conceptsvector.contains(wowcresource) && !wowcresource.equals("")){
          exists = true;
          break;
        }
        else conceptsvector.add(wowcresource);
        //if (!oldProjectName.equals("unnamed") && wowc.name.trim().equals(oldProjectName)) {
        //  wowc.name = this.projectName.trim();
        //  break;
        //}
      }
      if (exists) {
        JOptionPane.showConfirmDialog(
          this,
          "There are 2 concepts with the same resource! The file is not saved!",
          "alert",
          JOptionPane.PLAIN_MESSAGE
        );
      }
      else {//no double resources, continue saving file
        graphChanged = false;
        projectName = fileName;

        if (oldProjectName.equals("unnamed")) {
          aproject = new WOWOutConcept();
          aproject.name = projectName;
          aproject.description = "";
          aproject.resource = "";
          aproject.concepttype = "abstract";
          aproject.template = "abstract concept";
          aproject.nocommit = false;
          aproject.stable = "";
          aproject.stable_expr = "";
          aproject.AddTemplateAttributes();
          conceptList.add(aproject);
        }
        DefaultMutableTreeNode oldRoot =
          (DefaultMutableTreeNode)conceptTree.getModel().getRoot();
        String rootName = fileName.trim();
        oldRoot.setUserObject(rootName);

        ((DefaultTreeModel) conceptTree.getModel()).reload();
        AuthorOut aout = new AuthorOut(home, fileName);
        aout.WriteAuthorXML(noOutput);
        new SaveToWOW(home, noOutput);
        
        //Loc Nguyen add 2009.02.08
        OverlayBayesUM um = null;
        try {
        	um = OverlayBayesUMFactory.createFromAuthor(home, AuthorSTATIC.authorName, fileName, OverlayBayesUM.OBUM_DEFAULT_BAYESNET_TYPE);
        	
        	um.saveToAuthor(home, AuthorSTATIC.authorName, 
        			fileName, OverlayBayesUMFactory.getFormatType(OverlayBayesUM.OBUM_EXT), true);
    		JOptionPane.showMessageDialog(this, "Save Bayes network sucesssfully");
        }
        catch(Exception ex) {
        	JOptionPane.showMessageDialog(this, "Can not save Bayes file\n" + ex.getMessage());
        	System.out.println(ex.getMessage());
        }
      }
    }
    
    /**
     * Handles mouseclicks performed on the load button.
     * Requests the user to save unsaved data of the currently opened file.
     * Depending on the users answer, the file is saved or not.
     * A file open dialog box is shown where a user can select a file from it's
     * authorfiles folder. The selected file is opened in the current view.
     * @param e the event that triggered this action
     */
    private void Load_Button_actionPerformed(ActionEvent e) {

      if (graphChanged) {//request user to save unsaved data
        int result = JOptionPane.showConfirmDialog(
          this,
          "There is unsaved data! \n Do you want to save these changes?",
          "alert",
          JOptionPane.YES_NO_CANCEL_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            graphChanged = false;
            save_Button_actionPerformed(e, false);
        }

        if (result == JOptionPane.CANCEL_OPTION) return;
      }

      //show open file dialog, load selected file

      ServerFileChooser loadDialog =
        new ServerFileChooser(
          home,
          AuthorSTATIC.authorName,
          AMtc.AUTHOR_FILES_MODE,
          mainFrame,
          true
      );
      String[] ff = {".gaf"};
      loadDialog.showOpenDialog(ff);
      //FileLoad loadDialog = new FileLoad(home, mainFrame);
      //loadDialog.show();
      String fileName = ServerFileChooser.fileName;

      if (fileName != null) {//cancelled == false) {
        // added @David @14-10-2008
        termination.nocheck();
        // end added @David @14-10-2008

        NewAuthor(e, false);

        AuthorIn ain = new AuthorIn(home, fileName);
        ain.LoadFromServer();
        //Natasha here check that the strategies which have been already selected are not in the list of existing strategies
        //check why authorStrategies vector is not the same
        for (int i=0; i<AuthorSTATIC.strategyList.size(); i++) {
			if (authorStrategies.contains(AuthorSTATIC.strategyList.elementAt(i))) {
				existingStrategies.remove(AuthorSTATIC.strategyList.elementAt(i));
			}
		}
        spnConceptTree.getViewport().remove(conceptTree);

        //added by Natalia Stash
        String tmp = fileName.trim();
        if (tmp.endsWith(".gaf")) tmp = tmp.substring(0, tmp.length()-4);
        projectName = tmp;
        //this.projectName = loadDialog.fileName.trim();

        conceptTree = new DragTree();
        initTree();
        spnConceptTree.getViewport().add(conceptTree, null);
        filterEdges();
        setAdvanced(false);

        // added @David @14-10-2008
        termination.check();
        termination.modelChanged();
        // end added @David @14-10-2008
        
        
      }
    }

    void exit_button_actionPerformed(ActionEvent e) {
        mainFrame.setVisible(false);
    }

    void Export_button_actionPerformed(ActionEvent e) {
    	int result = JOptionPane.showConfirmDialog(this,
                                                   "This action will erase ALL data from the WOW! database!!! \n Use with care !",
                                                   "alert",
                                                   JOptionPane.OK_CANCEL_OPTION);

        // exit on cancel
        if (result != 0) {
            return;
        }
        save_Button_actionPerformed(e, true);

        boolean created = false;
        try {

        	URL url = new URL("http://" + home.getHost() + ":" +
                              home.getPort() + dirname +
                              "/servlet/authorservlets.ExportFile?fileName=" +
                              GraphAuthor.projectName + "&author=" + AuthorSTATIC.authorName);
        	BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        	String sFile = "";
        	while( (sFile = in.readLine()) != null ) {
        		if (sFile.trim().equals("true")) {
        			created = true;
        		}
        	}
        	in.close();

        }
        catch (Exception ex) {
        	String error = "Calling authorservlets.ExportFile through URL connection in" + 
        		"GraphAuthor.Export_button_actionPerformed causes error when saving to WOW! database\n" + 
        		ex.getMessage();
        	System.out.println(error);
        	JOptionPane.showMessageDialog(this, error);
        }
        if (created) {
        	JOptionPane.showConfirmDialog(this,
        		"This project is successfully added to the WOW! database!",
        		"alert", JOptionPane.PLAIN_MESSAGE);
                //Loc Nguyen add 2009.02.08
        }
        else
        	JOptionPane.showConfirmDialog(this,
        			"There was an error adding this project to the WOW! database!\nPlease, check if you have entered all the data correctly!",
        			"Error", JOptionPane.PLAIN_MESSAGE);
    }

    public void filterEdges() {
        Object[] cells = graph.getRoots();

        if (cells != null) {
            CellView[] views = GraphAuthor.graph.getView().getMapping(cells);

            for (int i = 0; i < views.length; i++) {
                if (views[i].getCell().getClass().getName()
                            .equals("com.jgraph.graph.DefaultEdge")) {
                    DefaultEdge dedge = (DefaultEdge) views[i].getCell();
                    Map edgeAtt = dedge.getAttributes();
                    String svalue = ((String) edgeAtt.get("crt")).trim();
                    Map prop = dedge.getAttributes();
                    GraphConstants.setVisible(prop, true);

                    for (ListIterator j = GraphAuthor.filteredListData.listIterator();
                         j.hasNext();) {
                        String listvalue = ((String) j.next()).trim();

                        if (svalue.equals(listvalue)) {
                            GraphConstants.setVisible(prop, false);
                        }
                    }

                    Map propMap = new Hashtable();
                    propMap.put(dedge, prop);
                    graph.getModel().edit(null, propMap, null, null);
                }
            }
        }
    }

    void filterButton_actionPerformed(ActionEvent e) {
        Filters filterDialog = new Filters(mainFrame);
        filterDialog.show();

        if (filterDialog.cancelled == false) {
            this.filterEdges();
        }
    }

    public void drop(DropTargetDropEvent e) {
    	GraphAuthor.conceptTree.getModel().getRoot().toString();
        
/*
        if (this.conceptTree.getSelectionPath().getPathCount() == 1) {
            String error = "The project name is not dragable.";
            javax.swing.JOptionPane.showMessageDialog(null, error, "Error",
                                                      javax.swing.JOptionPane.ERROR_MESSAGE);

            return;
        }
*/
        int x = (int) (e.getLocation().x / graph.getScale());
        int y = (int) (e.getLocation().y / graph.getScale());
        addConcept(this.selection, x, y);
        cutTreeNode = null;
    }

    public void dragEnter(DropTargetDragEvent e) {
    }

    public void dragExit(DropTargetEvent e) {
    }

    public void dragOver(DropTargetDragEvent e) {
    }

    public void dropActionChanged(DropTargetDragEvent e) {
    }

    public static Hashtable getExecRequest(Hashtable reqinfo, URL home) throws IOException {
        String path = home.getPath();
        String pathttemp = path.substring(1, path.length());
        int index = pathttemp.indexOf("/");
        index++;

        String dirname = path.substring(0, index);

        if (dirname.equals("/graphAuthor")) {
            dirname = "";
        }

        URL url;
        try {
            url = new URL("http://" + home.getHost() + ":" +
                          home.getPort() + dirname +
                          "/Exec");
        } catch (MalformedURLException e) {
            return new Hashtable();
        }
        URLConnection con = url.openConnection();
        con.setDoOutput(true);
        ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
        oos.writeObject(reqinfo);
        ObjectInputStream ios = new ObjectInputStream(con.getInputStream());
        try {
            Hashtable result = (Hashtable)ios.readObject();
            return result;
        } catch (ClassNotFoundException e) {
            return new Hashtable();
        }
    }

    public static class DragTree extends JTree implements DragGestureListener,
                                            DragSourceListener {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DragTree() {
            DragSource dragSource = DragSource.getDefaultDragSource();
            dragSource.createDefaultDragGestureRecognizer(this, // component where drag originates
                                                          DnDConstants.ACTION_COPY_OR_MOVE, // actions
                                                          this);
        }

        public void dragGestureRecognized(DragGestureEvent e) {
            e.startDrag(DragSource.DefaultCopyDrop, // cursor
                        new StringSelection("blackoak"), // transferable
                        this); // drag source listener
        }

        public void dragDropEnd(DragSourceDropEvent e) {
        }

        public void dragEnter(DragSourceDragEvent e) {
        }

        public void dragExit(DragSourceEvent e) {
        }

        public void dragOver(DragSourceDragEvent e) {
        }

        public void dropActionChanged(DragSourceDragEvent e) {
        }
    }

    public class MyGraph extends JGraph {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;


		// Construct the Graph using the Model as its Data Source
        public MyGraph(GraphModel model) {
            super(model);


            // Use a Custom Marquee Handler
            setMarqueeHandler(new MyMarqueeHandler());


            // Tell the Graph to Select new Cells upon Insertion
            setSelectNewCells(true);


            // Make Ports Visible by Default
            setPortsVisible(true);


            // Use the Grid (but don't make it Visible)
            setGridEnabled(true);


            // Set the Grid Size to 10 Pixel
            setGridSize(6);


            // Set the Snap Size to 2 Pixel
            setSnapSize(1);


            // no need to press enter after edit
            setInvokesStopCellEditing(true);
        }


        /* overides processMouseEvent
           checks for negative coordinates
           changed by brendan: 23-04-2008
        */
        protected void processMouseEvent(MouseEvent e) {
          if ((e.getPoint().x < 0) || (e.getPoint().y) <0) {
            return;
          }
          else {
            super.processMouseEvent(e);
          }

        }


        // Override Superclass Method to Return Custom EdgeView
        protected EdgeView createEdgeView(Edge e, CellMapper cm) {
            // Return Custom EdgeView
            return new EdgeView(e, this, cm) {
                /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				// Override Superclass Method
                public boolean isAddPointEvent(MouseEvent event) {
                    // Points are Added using Shift-Click
                    return event.isShiftDown();
                }

                // Override Superclass Method
                public boolean isRemovePointEvent(MouseEvent event) {
                    // Points are Removed using Shift-Click
                    return event.isShiftDown();
                }
            };
        }
    }

    // Custom MarqueeHandler
    // MarqueeHandler that Connects Vertices and Displays PopupMenus
    public class MyMarqueeHandler extends BasicMarqueeHandler {
        // Holds the Start and the Current Point
        protected Point start;

        // Holds the Start and the Current Point
        protected Point current;

        // Holds the First and the Current Port
        protected PortView port;

        // Holds the First and the Current Port
        protected PortView firstPort;

        // Override to Gain Control (for PopupMenu and ConnectMode)
        public boolean isForceMarqueeEvent(MouseEvent e) {
            // If Right Mouse Button we want to Display the PopupMenu
            if (SwingUtilities.isRightMouseButton(e)) {
                return true;
            }


            // Find and Remember Port
            port = getSourcePortAt(e.getPoint());

            // If Port Found and in ConnectMode (=Ports Visible)
            if ((port != null) && graph.isPortsVisible()) {
                return true;
            }

            // Else Call Superclass
            return super.isForceMarqueeEvent(e);
        }

        // Display PopupMenu or Remember Start Location and First Port
        public void mousePressed(final MouseEvent e) {
            // If Right Mouse Button
            if (SwingUtilities.isRightMouseButton(e)) {
                // Scale From Screen to Model
                Point loc = graph.fromScreen(e.getPoint());

                // Find Cell in Model Coordinates
                Object cell = graph.getFirstCellForLocation(loc.x, loc.y);

                // Create PopupMenu for the Cell
                JPopupMenu menu = createPopupMenu(e.getPoint(), cell);


                // Display PopupMenu
                menu.show(graph, e.getX(), e.getY());

                // Else if in ConnectMode and Remembered Port is Valid
            } else if ((port != null) && !e.isConsumed() &&
                           graph.isPortsVisible()) {
                // Remember Start Location
                start = graph.toScreen(port.getLocation(null));


                // Remember First Port
                firstPort = port;


                // Consume Event
                e.consume();
            } else {
                // Call Superclass
                super.mousePressed(e);
            }
        }

        // Find Port under Mouse and Repaint Connector
        public void mouseDragged(MouseEvent e) {
            // If remembered Start Point is Valid
            if ((start != null) && !e.isConsumed()) {
                // Fetch Graphics from Graph
                Graphics g = graph.getGraphics();


                // Xor-Paint the old Connector (Hide old Connector)
                paintConnector(Color.black, graph.getBackground(), g);


                // Reset Remembered Port
                port = getTargetPortAt(e.getPoint());

                // If Port was found then Point to Port Location
                if (port != null) {
                    current = graph.toScreen(port.getLocation(null));
                }
                // Else If no Port was found then Point to Mouse Location
                else {
                    current = graph.snap(e.getPoint());
                }


                // Xor-Paint the new Connector
                paintConnector(graph.getBackground(), Color.black, g);


                // Consume Event
                e.consume();
            }


            // Call Superclass
            super.mouseDragged(e);
        }

        /*   protected void prepareForUIInstall() {
        // Data member initializations
           stopEditingInCompleteEditing = true;
           preferredSize = new Dimension();
           setView(graph.getView());
           setModel(graph.getModel());
           } */
        public PortView getSourcePortAt(Point point) {
            // Scale from Screen to Model
            Point tmp = graph.fromScreen(new Point(point));

            // Find a Port View in Model Coordinates and Remember
            return graph.getPortViewAt(tmp.x, tmp.y);
        }

        // Find a Cell at point and Return its first Port as a PortView
        protected PortView getTargetPortAt(Point point) {
            // Find Cell at point (No scaling needed here)
            Object cell = graph.getFirstCellForLocation(point.x, point.y);

            // Loop Children to find PortView
            for (int i = 0; i < graph.getModel().getChildCount(cell); i++) {
                // Get Child from Model
                Object tmp = graph.getModel().getChild(cell, i);


                // Get View for Child using the Graph's View as a Cell Mapper
                tmp = graph.getView().getMapping(tmp, false);

                // If Child View is a Port View and not equal to First Port
                if (tmp instanceof PortView && (tmp != firstPort)) {
                    // Return as PortView
                    return (PortView) tmp;
                }
            }

            // No Port View found
            return getSourcePortAt(point);
        }

        // Connect the First Port and the Current Port in the Graph or Repaint
        public void mouseReleased(MouseEvent e) {
            // If Valid Event, Current and First Port
            if ((e != null) && !e.isConsumed() && (port != null) &&
                    (firstPort != null) && (firstPort != port)) {
                // Then Establish Connection
                connect((Port) firstPort.getCell(), (Port) port.getCell());


                // Consume Event
                e.consume();

                // Else Repaint the Graph
            } else {
                graph.repaint();
            }


            // Reset Global Vars
            firstPort = port = null;
            start = current = null;


            // Call Superclass
            super.mouseReleased(e);
        }

        // Show Special Cursor if Over Port
        public void mouseMoved(MouseEvent e) {
            // Check Mode and Find Port
            if ((e != null) && (getSourcePortAt(e.getPoint()) != null) &&
                    !e.isConsumed() && graph.isPortsVisible()) {
                // Set Cusor on Graph (Automatically Reset)
                graph.setCursor(new Cursor(Cursor.HAND_CURSOR));


                // Consume Event
                e.consume();
            }


            // Call Superclass
            super.mouseReleased(e);
        }

        // Use Xor-Mode on Graphics to Paint Connector
        protected void paintConnector(Color fg, Color bg, Graphics g) {
            // Set Foreground
            g.setColor(fg);


            // Set Xor-Mode Color
            g.setXORMode(bg);


            // Highlight the Current Port
            paintPort(graph.getGraphics());

            // If Valid First Port, Start and Current Point
            if ((firstPort != null) && (start != null) && (current != null)) {
                // Then Draw A Line From Start to Current Point
                g.drawLine(start.x, start.y, current.x, current.y);
            }
        }

        // Use the Preview Flag to Draw a Highlighted Port
        protected void paintPort(Graphics g) {
            // If Current Port is Valid
            if (port != null) {
                // If Not Floating Port...
                boolean o = (GraphConstants.getOffset(port.getAttributes()) != null);

                // ...Then use Parent's Bounds
                Rectangle r = o
                              ? port.getBounds()
                              : port.getParentView().getBounds();


                // Scale from Model to Screen
                r = graph.toScreen(new Rectangle(r));


                // Add Space For the Highlight Border
                r.setBounds(r.x - 3, r.y - 3, r.width + 6, r.height + 6);


                // Paint Port in Preview (=Highlight) Mode
                graph.getUI().paintCell(g, port, r, true);
            }
        }
    }
//added @David @11-10-2008
    static boolean inevent = false;
    public class MyGraphListener implements GraphModelListener {
        public MyGraphListener() {}
        public void graphChanged(GraphModelEvent e) {
            if (inevent) return; inevent = true;
            DefaultGraphModel gmodel = (DefaultGraphModel)GraphAuthor.graph.getModel();
            GraphModelEvent.GraphModelChange gchange = e.getChange();
            boolean b = ((gchange.getInserted()!=null) || (gchange.getRemoved()!=null) || (gchange.getChanged()!=null));
//added by @Loc Nguyen @13-08-2008
            Object[] roots = GraphAuthor.graph.getRoots();
            Vector delroots = new Vector();
            for (int i=0;i<roots.length;i++) {
                if (roots[i] instanceof DefaultEdge) {
                    DefaultEdge edge = (DefaultEdge)roots[i];
                    boolean d = ((gmodel.getSource(edge)!=null)&&(gmodel.getTarget(edge)!=null));
                    if (d) d = ((gmodel.getParent(gmodel.getSource(edge))!=null)&&(gmodel.getParent(gmodel.getTarget(edge))!=null));
                    if (!d) {
                        delroots.add(edge);
                        System.out.println("Autodeleting edge: "+edge);
                    }
                }
            }
            gmodel.remove(delroots.toArray());
//end added by @David @13-01-2005
            if (b) termination.modelChanged();
            inevent = false;
        }
    }

    public class MyTreeModelListener implements TreeModelListener {
        public MyTreeModelListener() {}
        public void treeNodesChanged(TreeModelEvent e) {termination.modelChanged();}
        public void treeNodesInserted(TreeModelEvent e) {termination.modelChanged();}
        public void treeNodesRemoved(TreeModelEvent e) {termination.modelChanged();}
        public void treeStructureChanged(TreeModelEvent e) {termination.modelChanged();}
    }

    public class MyCycleListener implements TermCycleListener {
        public MyCycleListener() {}
        public void cycleStateChanged(boolean state) {
            if (state) TestCycle();
        }
    }
//end added @david @11-10-2008
    public static void main(String[] args) {
    	try {
    		//new GraphAuthor(new URL("file:/W:/wow/WEB-INF/classes/vn/spring/WOW/graphauthor/GraphAuthor"), "author", true);
    		new GraphAuthor(new URL("http://localhost:8080/wow/lib"), "author", true);
    	}
    	catch (MalformedURLException e) {
    		e.printStackTrace();
    	}
    }
}