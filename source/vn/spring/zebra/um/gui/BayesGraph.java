package vn.spring.zebra.um.gui;

import com.jgraph.*;
import com.jgraph.graph.*;
import com.jgraph.event.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class BayesGraph extends JGraph implements KeyListener {
	private static final long serialVersionUID = 1L;
	public boolean graphChanged = false;
	
	// Construct the Graph using the Model as its Data Source
	public BayesGraph(GraphModel model) {
		super(model);

		addKeyListener(this);
        model.addGraphModelListener(new BayesGraphListener(this));

		// Use a Custom Marquee Handler
		setMarqueeHandler(new BayesMarqueeHandler(this));

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

	public JPopupMenu createPopupMenu(final Point pt, final Object cell) {
		JPopupMenu menu = new JPopupMenu();

        if (!isSelectionEmpty()) {
            // com.jgraph.graph.DefaultGraphCell
            if (getSelectionCell().getClass().toString()
                     .lastIndexOf("DefaultGraphCell") != -1) {
                //  menu.addSeparator();
                menu.add(new AbstractAction("What is it") {
                    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void actionPerformed(ActionEvent e) {
                        ;
                    }
                });
            }
        }
        return menu;
    }

	/* overides processMouseEvent
	   checks for negative coordinates
	   changed by Loc Nguyen: 25-06-2008
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
	
    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            this.removeSelectedCells();
        }
    }

    public void removeSelectedCells() {
        Object[] cells = getSelectionCells();
        getModel().remove(cells);
        this.graphChanged = true;
    }
    
    public void clear() {
        Object[] cells = getRoots();
        getModel().remove(cells);
        this.graphChanged = true;
    }
    
    public void editNodeLabel() {
        Object cell = getSelectionCell();

        if (cell != null) {
            if (cell instanceof com.jgraph.graph.DefaultGraphCell) {
                DefaultGraphCell dcell = (DefaultGraphCell) cell;
                Map prop = dcell.getAttributes();

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
                getModel().edit(null, propMap, null, null);
            }
        }
        this.graphChanged = true;
    }
    
    public void deleteNode(String nodename) {
        nodename = nodename.trim();

        LinkedList rcells = new LinkedList();
        Object[] cells = getRoots();

        if (cells != null) {
            CellView[] views = getView().getMapping(cells);

            for (int i = 0; i < views.length; i++) {
                if (views[i].getCell() instanceof com.jgraph.graph.DefaultGraphCell) {
                    if (views[i].getCell().toString().trim().equals(nodename)) {
                        Object dcell = views[i].getCell();
                        rcells.add(dcell);
                    }
                }

                if (views[i].getCell() instanceof com.jgraph.graph.DefaultEdge) {
                    DefaultEdge oEdge = (DefaultEdge) views[i].getCell();
                    GraphModel model = getModel();
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

                    if (source.equals(nodename) ||
                            destination.equals(nodename)) {
                        Object dcell = views[i].getCell();
                        rcells.add(dcell);
                    }
                }
            }
        }

        getModel().remove(rcells.toArray());
        this.graphChanged = true;
    }

    public void addNode(String nodename, int x, int y) {
        int ongraph = 0;
        nodename = nodename.trim();
        
        Object[] cells = getRoots();

        if (cells != null) {
            CellView[] views = getView().getMapping(cells);

            for (int i = 0; i < views.length; i++) {
                if (views[i].getCell() instanceof com.jgraph.graph.DefaultGraphCell) {
                    if (views[i].getCell().toString().trim().equals(nodename)) {
                        ongraph = 1;

                        DefaultGraphCell dGraph = (DefaultGraphCell) views[i].getCell();
                        setSelectionCell(dGraph);
                    }
                }
            }
        }

        if (ongraph == 0) {
          DefaultGraphCell vertex = new DefaultGraphCell(nodename);
          vertex.add(new DefaultPort());

          //                vertex.add(new DefaultPort());
          Point point = snap(new Point(x, y));
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

          getModel().insert(new Object[] {vertex}, null, null, attributes);
          this.graphChanged = true;
        }
    }
	public DefaultGraphCell findNode(String nodename) {
		nodename = nodename.trim();

		Object[] cells = getRoots();

		if (cells != null) {
			CellView[] views = getView().getMapping(cells);

			for (int i = 0; i < views.length; i++) {
				if (views[i].getCell() instanceof com.jgraph.graph.DefaultGraphCell) {
					if (views[i].getCell().toString().trim().equals(nodename)) {
						DefaultGraphCell df = (DefaultGraphCell) views[i].getCell();
						return df;
					}
				}
			}
		}

		return null;
    }

}


// Custom MarqueeHandler
// MarqueeHandler that Connects Vertices and Displays PopupMenus
class BayesMarqueeHandler extends BasicMarqueeHandler {
	//Parent Graph
	protected BayesGraph graph;
	
	// Holds the Start and the Current Point
	protected Point start;

	// Holds the Start and the Current Point
	protected Point current;

	// Holds the First and the Current Port
	protected PortView port;

	// Holds the First and the Current Port
	protected PortView firstPort;

	//constructor
	public BayesMarqueeHandler(BayesGraph graph) {
		this.graph = graph;
	}
	
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
            JPopupMenu menu = graph.createPopupMenu(e.getPoint(), cell);


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
    // Insert a new Edge between source and target
    public void connect(Port source, Port target) {
        // Connections that will be inserted into the Model
        ConnectionSet cs = new ConnectionSet();

        // Construct Edge with no label
        DefaultEdge edge = new DefaultEdge();

        // Create Connection between source and target using edge
        cs.connect(edge, source, target);

        // Create a Map thath holds the attributes for the edge
        Map map = GraphConstants.createMap();

        // Add a Line End Attribute
        GraphConstants.setLineEnd(map, GraphConstants.SIMPLE);

        // Construct a Map from cells to Maps (for insert)
        Hashtable attributes = new Hashtable();

        // Associate the Edge with its Attributes
        attributes.put(edge, map);

        // Insert the Edge and its Attributes
        graph.getModel().insert(new Object[] { edge }, cs, null, attributes);
        graph.graphChanged = true;

    }

}

class BayesGraphListener implements GraphModelListener {
	private BayesGraph graph;
	private static boolean inevent = false;
	public  BayesGraphListener(BayesGraph graph) {this.graph = graph;}
	public void graphChanged(GraphModelEvent e) {
        if (inevent) return; inevent = true;
    
        DefaultGraphModel gmodel = (DefaultGraphModel)graph.getModel();
//added by @Loc Nguyen @13-08-2008
        Object[] roots = graph.getRoots();
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
//end added by @Loc Nguyen @13-08-2008
        inevent = false;
    }
}
