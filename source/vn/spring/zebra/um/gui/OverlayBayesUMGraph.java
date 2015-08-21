package vn.spring.zebra.um.gui;

import com.jgraph.*;
import com.jgraph.graph.*;
import com.jgraph.event.*;

import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.*;

import javax.swing.*;

import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.um.InferenceResult;
import vn.spring.zebra.um.OverlayBayesItem;
import vn.spring.zebra.um.OverlayBayesItemBetaDensity;
import vn.spring.zebra.um.OverlayBayesUM;
import vn.spring.zebra.um.OverlayBayesUM.OBUM_BAYESNET_TYPE;
import vn.spring.zebra.um.OverlayBayesUM.OBUM_ALGORITHM_TYPE;
import vn.spring.zebra.um.OverlayBayesUM.OBUM_FILE_FORMAT_TYPE;

import vn.spring.zebra.um.OverlayBayesUMFactory;
import vn.spring.bayes.InferenceGraphs.InferenceGraph;
import vn.spring.bayes.InterchangeFormat.IFException;
import vn.spring.bayes.JavaBayesInterface.FunctionTablePanel;
import vn.spring.bayes.JavaBayesInterface.PropertyManager;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class OverlayBayesUMGraph extends JGraph implements MouseListener, KeyListener, GraphModelListener, UIDispose {
	private static final long serialVersionUID = 1L;

	private static enum CTXMENU_ACTION_TYPE {
		NET_PROPERTY, VAR_PROPERTY, FUNC_PROPERTY, VAR_OBSERVED, 
		VAR_MARGINAL_POSTERIOR, VAR_EXPECTATION, VAR_DELETE,
		NET_EXPLANATION, NET_FULL_EXPLANATION,
		BETA_DISTRIBUTION, BETA_DISTRIBUTION_ALL
	}
	public static enum GET_EDGES {
		FROM, TO, BOTH
	}
	public static final Color NORMAL_NODE_COLOR = Color.white;
	public static final Color OBSERVED_NODE_COLOR = Color.green;
	public static final Color EXPLANATION_NODE_COLOR = Color.orange;

	private OverlayBayesUM um = null;
	private JFrame frmParent = null;
	private OBUM_ALGORITHM_TYPE algorithmType = OBUM_ALGORITHM_TYPE.VARIABLE_ELIMINATION;
    private DefaultGraphCell dragCell = null;

	public OBUM_ALGORITHM_TYPE getAlgorithmType() {return algorithmType;}
	public void           setAlgorithmType(OBUM_ALGORITHM_TYPE algorithmType) {this.algorithmType = algorithmType;}

	// Construct the Graph using the Model as its Data Source
	public void dispose() {}

	public OverlayBayesUMGraph() {
		super();
		setModel(new DefaultGraphModel());
		getModel().addGraphModelListener(this);
		addKeyListener(this);
		setMarqueeHandler(new UMMarqueeHandler());
		addMouseListener(this);
	}
	
	public void load(OverlayBayesUM um, JFrame frmParent)  throws IOException, IFException, ZebraException {
		clearGraph();
		if(um == null) throw new ZebraException("User Model not provided");
		this.frmParent = frmParent;
		this.um = um;
		
		OverlayBayesItem[] items = this.um.getItems();
		for(int i=0; i<items.length; i++) {
			insertGraphNode(items[i]);
		}

		for(int i=0; i<items.length; i++) {
			OverlayBayesItem    item = items[i];
			OverlayBayesItem[]  parents = item.getParents();
			if(parents == null) continue;
			
			for(int j=0; j<parents.length; j++) {
				OverlayBayesItem   parent = parents[j];
				insertGraphEdge(parent, item);
			}
		}
	}
	public void load(String filePath, JFrame frmParent, OBUM_BAYESNET_TYPE bayesNetType)  throws IOException, IFException, ZebraException {
		OverlayBayesUM um = OverlayBayesUMFactory.create(bayesNetType);
		um.load(filePath);
		load(um, frmParent);
	}
	public void load(URL url, JFrame frmParent, OBUM_BAYESNET_TYPE bayesNetType)  throws IOException, IFException, ZebraException {
		OverlayBayesUM um = OverlayBayesUMFactory.create(bayesNetType);
		um.load(url);
		load(um, frmParent);
	}
	public void loadFromAuthor(URL base, String author, String course, JFrame frmParent, OBUM_BAYESNET_TYPE bayesNetType)  throws IOException, IFException, ZebraException {
		//E.g: base = http://localhost:8080/wow
		OverlayBayesUM um = OverlayBayesUMFactory.loadFromAuthor(base, author, course, bayesNetType);
		load(um, frmParent);
	}
	public void createFromAuthor(URL base, String author, String course, JFrame frmParent, OBUM_BAYESNET_TYPE bayesNetType)  throws IOException, IFException, ZebraException, Exception {
		//E.g: base = http://localhost:8080/wow
		OverlayBayesUM um = OverlayBayesUMFactory.createFromAuthor(base, author, course, bayesNetType);
		load(um, frmParent);
	}
	public void save(PrintStream out, OBUM_FILE_FORMAT_TYPE format) throws ZebraException {
		um.save(out, format);
	}
	public void save(String filePath) throws ZebraException, IOException {
		PrintStream out = new PrintStream(filePath);
		if(filePath.toLowerCase().endsWith(".bif")) {
			save(out, OBUM_FILE_FORMAT_TYPE.BIF);
		}
		else if(filePath.toLowerCase().endsWith(".bif")) {
			save(out, OBUM_FILE_FORMAT_TYPE.XML);
		}
		else throw new ZebraException("Not support this format type");
	}
	public void saveToAuthor(URL base, String author, String course, OBUM_FILE_FORMAT_TYPE format, boolean isSaveToDatabase) throws ZebraException, IOException {
		//E.g: base = http://localhost:8080/wow 
		um.saveToAuthor(base, author, course, format, isSaveToDatabase);
	}
	public void saveToDatabase(String userid, String course, OBUM_FILE_FORMAT_TYPE format, boolean isDynamic)  throws FileNotFoundException, ZebraException {
		um.saveToDatabase(userid, course, format, isDynamic);
	}
	
	public void clearGraph() {
		if(um == null) return;
		removeNode(um.getItems());
	}
	public void refreshGraph() throws Exception {
		Object[] cells = getRoots();
		CellView[] views = getView().getMapping(cells);
		ArrayList<Object> rCells = new ArrayList<Object>();
		for (int i = 0; i < views.length; i++) {
			Object cell = views[i].getCell();
			rCells.add(cell);
		}
		getModel().remove(rCells.toArray());
		
		OverlayBayesItem[] items = this.um.getItems();
		for(int i=0; i<items.length; i++) {
			insertGraphNode(items[i]);
		}

		for(int i=0; i<items.length; i++) {
			OverlayBayesItem    item = items[i];
			OverlayBayesItem[]  parents = item.getParents();
			if(parents == null) continue;
			
			for(int j=0; j<parents.length; j++) {
				OverlayBayesItem   parent = parents[j];
				insertGraphEdge(parent, item);
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void insertGraphNode(OverlayBayesItem item) {
		Point     pos = item.getPos();
		Rectangle bound = new Rectangle(pos.x < 0 ? 0 : pos.x, pos.y < 0 ? 0 : pos.y, 10, 10);
        DefaultGraphCell node = new DefaultGraphCell(item);
        node.add(new DefaultPort());
        
        Map<?, ?> map = GraphConstants.createMap();
        GraphConstants.setAutoSize(map, true);
        GraphConstants.setEditable(map, false);
        GraphConstants.setBounds(map, bound);
        GraphConstants.setBorderColor(map, Color.magenta);
        if(item.isObserved())
        	GraphConstants.setBackground(map, OBSERVED_NODE_COLOR);
        else if(item.isExplanation())
        	GraphConstants.setBackground(map, EXPLANATION_NODE_COLOR);
        else
        	GraphConstants.setBackground(map, NORMAL_NODE_COLOR);
        GraphConstants.setOpaque(map, true);
        
        Map attributes = new Hashtable();
        attributes.put(node, map);
        getModel().insert(new Object[] { node }, null, null, attributes);
	}
	
	@SuppressWarnings("unchecked")
	private void insertGraphEdge(OverlayBayesItem srcItem, OverlayBayesItem dstItem) {
        DefaultGraphCell srcNode = getNode(srcItem);
        DefaultGraphCell destNode = getNode(dstItem);
        
        DefaultPort      sp = new DefaultPort();
        srcNode.add(sp);
        
        DefaultPort dp = new DefaultPort();
        destNode.add(dp);

        ConnectionSet    cs = new ConnectionSet();
        DefaultEdge      edge = new DefaultEdge(new ItemEdge(this.getUserModel(), srcItem, dstItem));
        cs.connect(edge, sp, dp);
        
        Map<?, ?> map = GraphConstants.createMap();
        GraphConstants.setEditable(map, false);
        GraphConstants.setLineEnd(map, GraphConstants.SIMPLE);
        if(srcItem.getName().charAt(srcItem.getName().length()-1) == '#')
        	GraphConstants.setDashPattern(map, new float[] {(float)2, (float)2});
        
        Hashtable attributes = new Hashtable();
        attributes.put(edge, map);
        getModel().insert(new Object[] {edge}, cs, null, attributes);
	}
	private void removeNode(DefaultGraphCell cell) {
		if(cell.getUserObject() instanceof OverlayBayesItem) {
			OverlayBayesItem item = (OverlayBayesItem)cell.getUserObject();
			ArrayList<DefaultEdge> rEdges = getEdges(item, GET_EDGES.BOTH);
			
			getModel().remove(new Object[] {cell});
			if(rEdges.size() > 0) getModel().remove(rEdges.toArray());
			um.removeItem(item);
		}
	}
	public void removeNode(OverlayBayesItem item) {
		if(um == null) return;
		ArrayList<DefaultEdge> rEdges = getEdges(item, GET_EDGES.BOTH);
		DefaultGraphCell node = getNode(item);
		if(node == null) return;
		
		getModel().remove(new Object[] {node});
		if(rEdges.size() > 0) getModel().remove(rEdges.toArray());
		um.removeItem(item);
	}
	public void removeNode(String nodename) {
		if(um == null) return;
		removeNode(um.getItem(nodename));
	}
	public void removeNode(OverlayBayesItem[] items) {
		for(int i = 0; i < items.length; i++)
			removeNode(items[i]);
	}
	public DefaultGraphCell getNode(String nodename) {
		if(um == null) return null;
		return getNode(um.getItem(nodename));
	}
	public DefaultGraphCell getNode(OverlayBayesItem item) {
		if(item == null) return null;
		
		Object[] cells = getRoots();
		if(cells == null) return null;
		
		CellView[] views = getView().getMapping(cells);
		for (int i = 0; i < views.length; i++) {
			Object cell = views[i].getCell();
			if( !(cell instanceof DefaultGraphCell) || (cell instanceof DefaultEdge) )
				continue;
			Object found = ((DefaultGraphCell)cell).getUserObject();
			if( (found == null) || !(found instanceof OverlayBayesItem) ) continue;
			if( ((OverlayBayesItem)found) == item)
				return (DefaultGraphCell)cell;
		}
		return null;
    }
	public ArrayList<DefaultEdge> getEdges(OverlayBayesItem item, GET_EDGES flag) {
		ArrayList<DefaultEdge> rEdges = new ArrayList<DefaultEdge>();
		
		Object[] cells = getRoots();
		if(cells == null) return rEdges;
		
		CellView[] views = getView().getMapping(cells);
		for (int i = 0; i < views.length; i++) {
			if( !(views[i].getCell() instanceof DefaultEdge)) continue;
			DefaultEdge edge = (DefaultEdge)(views[i].getCell());
			Object source = getModel().getParent(getModel().getSource(edge));
			Object target = getModel().getParent(getModel().getTarget(edge));
			if(flag == GET_EDGES.BOTH) {
				OverlayBayesItem found = null;
				if( 
				   (source instanceof DefaultGraphCell) && 
				   (((DefaultGraphCell)source).getUserObject() instanceof OverlayBayesItem)
				  )
				{
					found = (OverlayBayesItem) (((DefaultGraphCell)source).getUserObject());
					if(found == item) rEdges.add(edge);
					else              found = null;
				}

				if(
				   (found == null) &&
				   (target instanceof DefaultGraphCell) && 
				   (((DefaultGraphCell)target).getUserObject() instanceof OverlayBayesItem)
				  )
				{
					found = (OverlayBayesItem) (((DefaultGraphCell)target).getUserObject());
					if(found == item) rEdges.add(edge);
				}
			}
			else if(flag == GET_EDGES.FROM) {
				if( 
				   (source instanceof DefaultGraphCell) && 
				   (((DefaultGraphCell)source).getUserObject() instanceof OverlayBayesItem)
				  )
				{
					OverlayBayesItem found = (OverlayBayesItem) (((DefaultGraphCell)source).getUserObject());
					if(found == item) rEdges.add(edge);
				}
			}
			else if(flag == GET_EDGES.TO) {
				if( 
				   (target instanceof DefaultGraphCell) && 
				   (((DefaultGraphCell)target).getUserObject() instanceof OverlayBayesItem)
				  )
				{
					OverlayBayesItem found = (OverlayBayesItem) (((DefaultGraphCell)target).getUserObject());
					if(found == item) rEdges.add(edge);
				}
			}
		}
		return rEdges;
	}

	public OverlayBayesUM getUserModel() {
		return um;
	}
	public Frame getParentFrame() {
		return frmParent;
	}
	
	private OverlayBayesUMGraph getGraph() {
		return this;
	}
	
	@SuppressWarnings("null")
	private void showNodeContextMenu(final Point pos, final Object cell) {
		JPopupMenu contextMenu = null;
		if(cell == null) {
			contextMenu = new JPopupMenu();
			contextMenu.add(new ItemContextMenuAction(null, null, CTXMENU_ACTION_TYPE.NET_PROPERTY));
			contextMenu.addSeparator();
			contextMenu.add(new ItemContextMenuAction(null, null, CTXMENU_ACTION_TYPE.NET_EXPLANATION));
			contextMenu.add(new ItemContextMenuAction(null, null, CTXMENU_ACTION_TYPE.NET_FULL_EXPLANATION));
		}
		else if( (cell instanceof DefaultGraphCell) && !(cell instanceof DefaultEdge) ) {
			contextMenu = new JPopupMenu();
			OverlayBayesItem item = (OverlayBayesItem) (((com.jgraph.graph.DefaultGraphCell)cell).getUserObject());
			
			contextMenu.add(new ItemContextMenuAction(item, null, CTXMENU_ACTION_TYPE.VAR_PROPERTY));
			contextMenu.add(new ItemContextMenuAction(item, null, CTXMENU_ACTION_TYPE.FUNC_PROPERTY));
			contextMenu.addSeparator();
			contextMenu.add(new ItemContextMenuAction(item, null, CTXMENU_ACTION_TYPE.VAR_OBSERVED));
			contextMenu.addSeparator();
			contextMenu.add(new ItemContextMenuAction(item, null, CTXMENU_ACTION_TYPE.VAR_MARGINAL_POSTERIOR));
			contextMenu.add(new ItemContextMenuAction(item, null, CTXMENU_ACTION_TYPE.VAR_EXPECTATION));
			contextMenu.addSeparator();
			contextMenu.add(new ItemContextMenuAction(item, null, CTXMENU_ACTION_TYPE.VAR_DELETE));
			contextMenu.addSeparator();
			contextMenu.add(new ItemContextMenuAction(item, null, CTXMENU_ACTION_TYPE.BETA_DISTRIBUTION_ALL));
		}
		else if(cell instanceof DefaultEdge) {
			contextMenu = new JPopupMenu();
			ItemEdge itemEdge = (ItemEdge) (((com.jgraph.graph.DefaultEdge)cell).getUserObject());
			contextMenu.add(new ItemContextMenuAction(itemEdge.child, itemEdge.parent, CTXMENU_ACTION_TYPE.BETA_DISTRIBUTION_ALL));
			
		}
        // Display context menu
        if(contextMenu != null) contextMenu.show(this, (int)pos.getX(), (int)pos.getY());
	}
	
    public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			Point loc = fromScreen(e.getPoint());
            Object cell = getFirstCellForLocation(loc.x, loc.y);
            showNodeContextMenu(e.getPoint(), cell);
        }
    }

    public void mousePressed(MouseEvent e) {
		Point loc = fromScreen(e.getPoint());
        Object cell = getFirstCellForLocation(loc.x, loc.y);
        if(cell != null && cell instanceof DefaultGraphCell) {
        	dragCell = (DefaultGraphCell)cell;
        }
        else
        	dragCell = null;
    }
    public void mouseReleased(MouseEvent e) {
		Point loc = fromScreen(e.getPoint());
		
        // Find Cell in Model Coordinates
        Object cell = getFirstCellForLocation(loc.x, loc.y);
        if(cell != null && cell instanceof DefaultGraphCell) {
        	DefaultGraphCell curCell = (DefaultGraphCell)cell;
        	if(curCell == dragCell) {
        		Object object = dragCell.getUserObject();
        		if(object instanceof OverlayBayesItem) {
	        		OverlayBayesItem item = (OverlayBayesItem)object;
	            	CellView[] views = getView().getMapping(new Object[] {dragCell});
	                Map<?, ?> map = views[0].getAttributes();
	                Rectangle rec = GraphConstants.getBounds(map);
	                rec.x = rec.x < 0? 0 : rec.x;
	                rec.y = rec.y < 0? 0 : rec.y;
	                um.setItemPos(item, new Point(rec.x, rec.y));
        		}
        	}
        }
        dragCell = null;
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_DELETE) {
			Object[] cells = getSelectionCells();
			if(cells.length == 0) return;
			for(int i = 0; i < cells.length; i++) {
				if(!(cells[i] instanceof DefaultGraphCell)) continue;
				DefaultGraphCell node = (DefaultGraphCell)cells[i];
				removeNode(node);
			}
		}
	}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	public void graphChanged(GraphModelEvent e) {
		GraphModel graphModel = (GraphModel) (e.getSource());
		graphModel.getRootCount();
	}

	private class ItemContextMenuAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		private OverlayBayesItem item = null;
		private OverlayBayesItem parent = null;
		private CTXMENU_ACTION_TYPE actionType;
		
		public ItemContextMenuAction(OverlayBayesItem item, OverlayBayesItem parent, CTXMENU_ACTION_TYPE actionType) {
			super();
			if(actionType == CTXMENU_ACTION_TYPE.NET_PROPERTY)
				this.putValue(Action.NAME, "Network Property");
			else if(actionType == CTXMENU_ACTION_TYPE.VAR_PROPERTY)
				this.putValue(Action.NAME, "Variable Property");
			else if(actionType == CTXMENU_ACTION_TYPE.FUNC_PROPERTY)
				this.putValue(Action.NAME, "Probability Property");
			else if(actionType == CTXMENU_ACTION_TYPE.VAR_OBSERVED)
				this.putValue(Action.NAME, "Set Observed");
			else if(actionType == CTXMENU_ACTION_TYPE.VAR_MARGINAL_POSTERIOR)
				this.putValue(Action.NAME, "Marginal Posterior (Eval)");
			else if(actionType == CTXMENU_ACTION_TYPE.VAR_EXPECTATION)
				this.putValue(Action.NAME, "Expectation");
			else if(actionType == CTXMENU_ACTION_TYPE.VAR_DELETE)
				this.putValue(Action.NAME, "Delete");
			else if(actionType == CTXMENU_ACTION_TYPE.BETA_DISTRIBUTION)
				this.putValue(Action.NAME, "Beta distribution");
			else if(actionType == CTXMENU_ACTION_TYPE.BETA_DISTRIBUTION_ALL)
				this.putValue(Action.NAME, "Beta distribution");
				
			this.item = item;
			this.parent = parent;
			this.actionType = actionType;
		}
		public void actionPerformed(ActionEvent e) {
			if(actionType == CTXMENU_ACTION_TYPE.NET_PROPERTY) {
				showNetPropertyDlg();
			}
			else if(actionType == CTXMENU_ACTION_TYPE.VAR_PROPERTY) {
				showVarPropertyDlg(item);
			}
			else if(actionType == CTXMENU_ACTION_TYPE.FUNC_PROPERTY) {
				showFuncPropertyDlg(item);
			}
			else if(actionType == CTXMENU_ACTION_TYPE.VAR_OBSERVED) {
				showObserveDlg(item);
			}
			else if(actionType == CTXMENU_ACTION_TYPE.VAR_MARGINAL_POSTERIOR) {
				InferenceResult result = inferMariginalPosterior(this.item.getName());
				JOptionPane.showMessageDialog(null, result.toString());
				System.out.println(result.toString());
			}
			else if(actionType == CTXMENU_ACTION_TYPE.VAR_EXPECTATION) {
				InferenceResult result = inferExpectation(this.item.getName());
				JOptionPane.showMessageDialog(null, result.toString());
				System.out.println(result);
			}
			else if(actionType == CTXMENU_ACTION_TYPE.VAR_DELETE) {
				removeNode(item);
				System.out.println("Remove variable \"" + item.getName() + "\"");
			}
			else if(actionType == CTXMENU_ACTION_TYPE.NET_EXPLANATION) {
				InferenceResult result = inferExplanation();
				JOptionPane.showMessageDialog(null, result.toString());
				System.out.println(result);
			}
			else if(actionType == CTXMENU_ACTION_TYPE.NET_FULL_EXPLANATION) {
				InferenceResult result = inferFullExplanation();
				JOptionPane.showMessageDialog(null, result.toString());
				System.out.println(result);
			}
			else if(actionType == CTXMENU_ACTION_TYPE.BETA_DISTRIBUTION) {
				JOptionPane.showMessageDialog(null, "Not Implemented yet " + parent.getName());
			}
			else if(actionType == CTXMENU_ACTION_TYPE.BETA_DISTRIBUTION_ALL) {
				try {
					if(!OverlayBayesItemBetaDensity.checkValid(item)) {
						String err= "OverlayBayesItemBetaDensity Just only supporting for binary and non-evidence variables";
						JOptionPane.showMessageDialog(null, err, "OverlayBayesItemBetaDensity error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					else {
						ItemBetaGraphDlg itemBetaGraphDlg = new ItemBetaGraphDlg(getParentFrame(), getGraph(), getUserModel(), item);
						itemBetaGraphDlg.setVisible(true);
					}
				}
				catch(ZebraException ex) {
					ex.printStackTrace();
				}
			}

		}
		
	}
	public void showNetPropertyDlg() {
		um.resetMarginal();
		um.resetExpectation();
		Dialog propDlg = new NetPropertyDlg(this);
		propDlg.setVisible(true);
	}
	public void showVarPropertyDlg(OverlayBayesItem item) {
		um.resetMarginal();
		um.resetExpectation();
		Dialog propDlg = new VarPropertyDlg(this, item);
		propDlg.setVisible(true);
	}
	public void showFuncPropertyDlg(OverlayBayesItem item) {
		um.resetMarginal();
		um.resetExpectation();
		Dialog propDlg = new FuncPropertyDlg(this, item);
		propDlg.setVisible(true);
	}
	public void showObserveDlg(OverlayBayesItem item) {
		um.resetMarginal();
		um.resetExpectation();
		Dialog propDlg = new ObserveDlg(this, item);
		propDlg.setVisible(true);
	}
	public InferenceResult inferMariginalPosterior(String itemName) {
		return getUserModel().inferMariginalPosterior(itemName, getAlgorithmType());
	}
	public InferenceResult inferExpectation(String itemName) {
		return getUserModel().inferExpectation(itemName, getAlgorithmType());
	}
	public InferenceResult inferExplanation() {
		return getUserModel().inferExplanation();
	}
	public InferenceResult inferFullExplanation() {
		return getUserModel().inferFullExplanation();
	}
	
	private class UMMarqueeHandler extends BasicMarqueeHandler {
		UMMarqueeHandler() {
		}
		// Display PopupMenu or Remember Start Location and First Port
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
		}
	}
}

class ItemEdge {
	private OverlayBayesUM   um = null;
	public  OverlayBayesItem parent = null; 
	public  OverlayBayesItem child = null;
	
	private ItemEdge() {}
	public ItemEdge(OverlayBayesUM um, OverlayBayesItem parent, OverlayBayesItem child) {
		this();
		this.um = um;
		this.parent = parent;
		this.child = child;
	}
	public String toString() {
		if(um != null && um.getConceptGraph() != null) {
			int idx = um.getConceptGraph().getConcepEdgeIndex(parent.getName(), child.getName());
			if(idx == -1) return "";
			double value = um.getConceptGraph().getConceptEdge(idx).weight;
			if(value == 0) return "";
			String svalue = String.valueOf(value); return svalue.substring(0, svalue.length() > 4?4:svalue.length());
		}
    	OverlayBayesItem[] parents = child.getParents();
    	int i = 0;
    	for(i = 0; i < parents.length; i++) {
    		if(parents[i].getName().equals(parent.getName())) break;
    	}
    	if(i == parents.length) return "";
    	try {
			double value = child.getParentWeights()[i];
			String svalue = String.valueOf(value); return svalue.substring(0, svalue.length() > 4?4:svalue.length());
    	}
    	catch(ZebraException e) {
    		System.out.println("ItemEdge.toString causes error: " + e.getMessage());
    	}
		return "";
	}
}
class VarPropertyDlg extends Dialog {
	private static final long serialVersionUID = 1L;

	private OverlayBayesUMGraph umGraph = null;
    private OverlayBayesItem item = null;

    // Variables that hold the contents of the dialog.
    int number_extreme_points;
    PropertyManager variable_property_manager;
    PropertyManager function_property_manager;

    // Variables used to construct the dialog.
    int displayed_variable_property_index, displayed_function_property_index;
    Panel np, nvp, tp, ttp, vpp, fpp, npp, cbp, pp, gnp, gncp, okp, qbp, qbpp;
    Label name, new_value, type;
    Label variable_properties, function_properties, local_parameter;
    TextField text_name, text_new_value, text_local_parameter;
    CheckboxGroup types, function_types;
    Checkbox chance_type, explanation_type, no_local_credal_set_type, local_credal_set_type;
    Button new_variable_property, next_variable_property;
    Button new_function_property, next_function_property;
    TextField variable_properties_text, function_properties_text;
    Button dist_button, ok_button, dismiss_button;
    
    // Constants used to construct the dialog.
    private final static int TOP_INSET = 5;
    private final static int LEFT_INSET = 10;
    private final static int RIGHT_INSET = 10;
    private final static int BOTTOM_INSET = 0;

    // Labels for the various elements of the dialog.
    private final static String name_label = "Name:";
    private final static String new_value_label = "Values:";
    private final static String type_label = "Types:";
    private final static String chance_type_label = "Chance node";
    private final static String explanation_type_label = "Explanatory node";
    private final static String no_local_credal_set_label = "Single distribution";
    private final static String local_credal_set_label = "Credal set with extreme points";
    private final static String variable_properties_label = "Variable properties:";
    private final static String function_properties_label = "Probability properties:";
    private final static String next_property_label = "Next";
    private final static String new_property_label = "New";
    private final static String edit_function_label = "Edit probability";
    private final static String ok_label = "Apply";
    private final static String dismiss_label = "Cancel";

    public VarPropertyDlg(OverlayBayesUMGraph umGraph, OverlayBayesItem item) {
    	super(umGraph.getParentFrame(), "Edit: " + item.getName(), true);
    	
    	this.umGraph = umGraph;
    	this.item = item;

    	// Compose the frame

        // Panel for name, values and type

        // Panel for the name
        np = new Panel();
        np.setLayout(new BorderLayout());
        name = new Label(name_label);
        text_name = new TextField(30);
        np.add("West", name);
        np.add("Center", text_name);

        // Panel for the values
        nvp = new Panel();
        nvp.setLayout(new BorderLayout());
        new_value = new Label(new_value_label);
        text_new_value = new TextField(60);
        nvp.add("West", new_value);
        nvp.add("Center", text_new_value);

        // Panel for the type
        tp = new Panel();
        tp.setLayout(new BorderLayout());
        type = new Label(type_label);

        ttp = new Panel();
        ttp.setLayout(new GridLayout(2, 1));
        types = new CheckboxGroup();
        chance_type = new Checkbox(chance_type_label, types, true);
        explanation_type = new Checkbox(explanation_type_label, types, false);
        ttp.add(chance_type);
        ttp.add(explanation_type);

        qbp = new Panel();
        qbp.setLayout(new GridLayout(2,1));
        function_types = new CheckboxGroup();
        no_local_credal_set_type = new Checkbox(no_local_credal_set_label, function_types, true);
        local_credal_set_type = new Checkbox(local_credal_set_label, function_types, false);

        qbp.add(no_local_credal_set_type);
        qbp.add(local_credal_set_type);

        tp.add("North", type);
        tp.add("West", ttp);
        tp.add("East", qbp);

        // Finish panel for name, values and type
        cbp = new Panel();
    	cbp.setLayout(new BorderLayout(10,10));
        cbp.add("North", np);
        cbp.add("Center", nvp);
        cbp.add("South", tp);

        // Panel for properties (variable, function and network)
        pp = new Panel();
        pp.setLayout(new BorderLayout());

        // Variable properties
        vpp = new Panel();
        vpp.setLayout(new BorderLayout());
        variable_properties = new Label(variable_properties_label);
        next_variable_property = new Button(next_property_label);
        new_variable_property = new Button(new_property_label);
        variable_properties_text = new TextField(40);
        vpp.add("North", variable_properties);
        vpp.add("West", next_variable_property);
        vpp.add("Center", variable_properties_text);
        vpp.add("East", new_variable_property);

        // Function properties
        fpp = new Panel();
        fpp.setLayout(new BorderLayout());
        function_properties = new Label(function_properties_label);
        next_function_property = new Button(next_property_label);
        new_function_property = new Button(new_property_label);
        function_properties_text = new TextField(40);
        fpp.add("North", function_properties);
        fpp.add("West", next_function_property);
        fpp.add("Center", function_properties_text);
        fpp.add("East", new_function_property);

        // Finish panel for properties
        pp.add("North", vpp);
        pp.add("Center", fpp);

        // Return buttons
     	okp = new Panel();
	    okp.setLayout(new FlowLayout(FlowLayout.CENTER));
    	dist_button = new Button(edit_function_label);
    	okp.add(dist_button);
    	ok_button = new Button(ok_label);
        dismiss_button = new Button(dismiss_label);
        okp.add(ok_button);
        okp.add(dismiss_button);
        setLayout(new BorderLayout());
    	add("North", cbp);
    	add("Center", pp);
	    add("South", okp);

	    // Pack the whole window
    	pack();

    	// Initialize values
    	fill_dialog();
    }

    /**
     * Customized show method.
     */ 
    @SuppressWarnings("deprecation")
	public void show() {
    	if(getParent() != null) {
	       	Rectangle bounds = getParent().bounds();
	    	Rectangle abounds = bounds();
	
	    	move(bounds.x + (bounds.width - abounds.width)/ 2,
	    	     bounds.y + (bounds.height - abounds.height)/2);
    	}
    	super.show();
    }

    /**    
     * Customize insets method.              
     */
    @SuppressWarnings("deprecation")
	public Insets insets() {
        Insets ins = super.insets();
        return(new Insets(ins.top + TOP_INSET, ins.left + LEFT_INSET,
                          ins.bottom + BOTTOM_INSET, ins.right + RIGHT_INSET));
    }
    /**
     * Handle the possible destruction of the window.
     */
    @SuppressWarnings("deprecation")
	public boolean handleEvent(Event evt) {
        if (evt.id == Event.WINDOW_DESTROY)
            dispose();
        return(super.handleEvent(evt));
    }

    /**
     * Fill the values in the dialog area.
     */
    @SuppressWarnings("deprecation")
	private void fill_dialog() {
        String values[], all_values = "";

        // Synchronize the network if necessary.
        umGraph.getUserModel().getInnerBayesNet();

        // Fill name
        text_name.setText(item.getName());

        // Fill values
        values = item.getValues();
        for (int i=0; i<values.length; i++) {
            all_values += values[i];
            if (i != (values.length - 1)) all_values += ", ";
        }
        text_new_value.setText(all_values);

        // Set type: explanatory or chance.
        if (item.isExplanation())
            types.setCurrent(explanation_type);
        else
            types.setCurrent(chance_type);

        // Set type: standard or credal.
        if (item.isCredalSet())
            function_types.setCurrent(local_credal_set_type);
        else
            function_types.setCurrent(no_local_credal_set_type);

        // Fill and store properties
        variable_property_manager = 
            new PropertyManager(item.getVariableProperties(),
                                variable_properties_text);
        function_property_manager = 
            new PropertyManager(item.getFunctionProperties(),
                                function_properties_text);
    }
                                
    /**
     * Handle the events.  
     */
    @SuppressWarnings("deprecation")
	public boolean action(Event evt, Object arg) {
        try {
	        if (evt.target == dismiss_button) {
		        dispose();
		    } else if (evt.target == ok_button) {
		        update_dialog();
		        dispose();
		    } else if (evt.target == new_variable_property) {
	            variable_property_manager.new_property();
	        } else if (evt.target == next_variable_property) {
	            variable_property_manager.next_property();
	        } else if (evt.target == new_function_property) {
	            function_property_manager.new_property();
	        } else if (evt.target == next_function_property) {
	            function_property_manager.next_property();
	        } else if (evt.target == variable_properties_text) {
	            variable_property_manager.update_property();
	        } else if (evt.target == function_properties_text) {
	            function_property_manager.update_property();
	        } else if (evt.target == dist_button) {
	        	Dialog fDlg = new FuncPropertyDlg(this.umGraph, this.item);
	        	fDlg.setVisible(true);
	        	
	        } else return super.action(evt, arg);
	
	    	return true;
        }
        catch(ZebraException e) {
        	System.out.println(e.getMessage());
        	return false;
        }
    }

    /**
     * Parse the values stated in the values TextField. 
     */
    private String[] parse_values(String all_values) {
        String delimiters = " ,\n\t\r";
        StringTokenizer st = new StringTokenizer(all_values, delimiters);
        String vals[] = new String[ st.countTokens() ];
        int i = 0;

        while (st.hasMoreTokens()) {
            vals[i] = validate_value(st.nextToken());
            i++;
        }
        return(vals);
    }
    private String validate_value(String value) {
      	StringBuffer str = new StringBuffer(value);
        for (int i=0; i < str.length(); i++) {
            if (str.charAt(i) == ' ')
       	       str.setCharAt(i, '_');
        }
        return str.toString();
    }
    
	/*
	 * Update the contents of the network when the        
	 * dialog exits.                                      
	 */
	private void update_dialog() throws ZebraException {
        // Update the name of the variable.
	    String checked_name = umGraph.getUserModel().checkName(text_name.getText());
	    if (checked_name != null)
    	    item.setName(checked_name);
	    // Update the values of the variable.
        String[] values = parse_values(text_new_value.getText());
        if (values != null)
        	umGraph.getUserModel().changeItemValues(item, values);
        // Update the explanatory/chance type.
        if (types.getSelectedCheckbox() == chance_type) 
	        item.setExplanation(false);
        else 
            item.setExplanation(true);
        
        // Update the standard/credal type.
        if (function_types.getSelectedCheckbox() == no_local_credal_set_type) 
            item.setNoLocalCredalSet();
        else 
            item.setLocalCredalSet();
        // Update the variable properties (if necessary).
        Vector<?> vprop = variable_property_manager.update_property_on_exit();
        if (vprop != null) {
            item.setVariableProperties(vprop);
            for (Enumeration<?> e = vprop.elements(); e.hasMoreElements(); )
                item.updatePositionFromProperty((String)(e.nextElement()));
        }
        // Update the function properties (if necessary).
        Vector<?> fprop = function_property_manager.update_property_on_exit();
        if (fprop != null)
            item.setFunctionProperties(fprop);
    }
}

class FuncPropertyDlg extends Dialog {
	private static final long serialVersionUID = 1L;

	// Variables used to construct the dialog.
    EditFunctionPanel efp;
    Panel buttons;
    Button ok_button, dismiss_button;

    // Constants used to construct the dialog.
    private final static int TOP_INSET = 5;
    private final static int LEFT_INSET = 10;
    private final static int RIGHT_INSET = 10;
    private final static int BOTTOM_INSET = 0;
    
    protected OverlayBayesUMGraph umGraph = null;
    
    // Labels for the various elements of the dialog.
    private final static String ok_label = "Apply";
    private final static String dialog_title = "Edit Probability";
    private final static String dismiss_label = "Cancel";

    /**
     * Default constructor for an EditFunctionDialog.
     */
    public FuncPropertyDlg(OverlayBayesUMGraph umGraph, OverlayBayesItem item) {
        super(umGraph.getParentFrame(), dialog_title, true);
        this.umGraph = umGraph;
        
        setLayout(new BorderLayout());
        efp = dispatch(item);
        buttons = new Panel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
        ok_button = new Button(ok_label);
        dismiss_button = new Button(dismiss_label);
        buttons.add(ok_button);
        buttons.add(dismiss_button);
        add("Center", efp);
        add("South", buttons);
        pack();
    }

    /*
     * Create the appropriate instance of EditFunctionPanel,
     * based on the function in the node.
     */
    private EditFunctionPanel dispatch(OverlayBayesItem item) {
        if (item.isCredalSet())
            return(new EditCredalSet(item));
        else
            return(new EditProbability(item));
    }

    /**
     * Customized show method.
     */
    @SuppressWarnings("deprecation")
	public void show() {
    	if(getParent() != null) {
	    	Rectangle bounds = getParent().bounds();
	    	Rectangle abounds = bounds();
	
	    	move(bounds.x + (bounds.width - abounds.width)/ 2,
	    	     bounds.y + (bounds.height - abounds.height)/2);
    	}
    	super.show();
    }

    /**
     * Customize insets method.
     */
    @SuppressWarnings("deprecation")
	public Insets insets() {
        Insets ins = super.insets();
        return(new Insets(ins.top + TOP_INSET, ins.left + LEFT_INSET,
                          ins.bottom + BOTTOM_INSET, ins.right + RIGHT_INSET));
    }

    /**
     * Handle the possible destruction of the window.
     */
    @SuppressWarnings("deprecation")
	public boolean handleEvent(Event evt) {
        if (evt.id == Event.WINDOW_DESTROY)
            dispose();
        return(super.handleEvent(evt));
    }
    
    /**
     * Handle events in the dialog.
     */
    @SuppressWarnings("deprecation")
	public boolean action(Event evt, Object arg) {
        // Check whether to dismiss
        if (evt.target == dismiss_button) {
            efp.dismiss();
	        dispose();
	        return(true);
	    } else if (evt.target == ok_button) {
	        efp.accept();
	        dispose();
	        return(true);
	    }
        return(super.action(evt, arg));
	}
    
    private abstract class EditFunctionPanel extends Panel {
    	private static final long serialVersionUID = 1L;
        
        abstract void accept();
        
        abstract void dismiss();
    }
    private class EditProbability extends EditFunctionPanel {
    	private static final long serialVersionUID = 1L;

    	// The graph and node that contain the probability function.
        private OverlayBayesItem item;

        // Variables that hold the relevant information from the node.
        String all_variable_names[];
        String all_variable_values[][];
        double probability_values[];

        FunctionTablePanel probability_table;
            
        /**
         * Default constructor for an EditProbability.
         */
        public EditProbability(OverlayBayesItem item) {
         	this.item = item;
         	
            // Copy the probability values in the node.
            double original_probability_values[] = item.getFunctionValues();
            probability_values = new double[original_probability_values.length];
            for (int i=0; i<probability_values.length; i++) 
                probability_values[i] = original_probability_values[i];
            
            // Get the variable names.
            all_variable_names = item.getAllNames();
            
            // Get the variable values.
            all_variable_values = item.getAllValues();

            // Construct the name of the probability function.
            Label probability_name = create_probability_name();
            
            // Construct the table of probability values.
            probability_table = new FunctionTablePanel(all_variable_names,
                all_variable_values, probability_values);

            // Set the final layout
            setLayout(new BorderLayout());
            add("North", probability_name);
            add("Center", probability_table);
        }

        /*
         * Create a Label containing a description of the probability function.
         */
        private Label create_probability_name() {
            StringBuffer name = new StringBuffer("p(");
            name.append(item.getName());
            if (item.hasParent()) {
                name.append(" |");
                OverlayBayesItem[] parents = item.getParents();
                for(int i = 0; i < parents.length; i++)
                    name.append(" " + parents[i].getName() + ",");
                name.setCharAt(name.length() - 1, ')');
            }
            else 
                name.append(")");
            return(new Label(name.toString(), Label.CENTER));
        }

        void accept() {
            double EPSILON = 1e-6;
            // Get the values from the table.
            probability_values = probability_table.get_table();
            // Check whether things add up to one.
            int number_values = item.getNumberValues();
            int number_conditioning_values = 
                probability_values.length / number_values;
            double verification_counters[] = 
                new double[number_conditioning_values];
            for (int i=0; i<probability_values.length; i++)
                verification_counters[ i % number_conditioning_values ] +=
                    probability_values[i];
            for (int j=0; j<verification_counters.length; j++) {
                if ( Math.abs(verification_counters[j]-1.0) >= EPSILON ) {
                	System.out.println("Some of the probability values " + 
                               "you have edited add up to " +
                               verification_counters[j] + ". Please check it.\n\n");
                }
            }
            // Set the values.
        
            item.setFunctionValues(probability_values);
        }
        
        void dismiss() {
            // No-op.
        }
    }
    
    private class EditCredalSet extends EditFunctionPanel {
    	private static final long serialVersionUID = 1L;

    	// The graph and node that contain the probability function.
        private OverlayBayesItem item;

        // Variables that hold the relevant information from the node.
        private String all_variable_names[];
        private String all_variable_values[][];
        private double all_probability_values[][];
        private int index_extreme_point;
        
        // Components used to construct the panel.
        private FunctionTablePanel probability_table;
        private Panel csp, ics, qbpp;
        private Choice credal_set_choice;
        private Label local_parameter;
        private TextField text_local_parameter;

        // Constants used to construct the panel.
        private final static String credal_set_specification = "Credal set specification";
        private final static String credal_set = "Index of extreme distribution:";
        private final static String number_extreme_points_label = "Number of extreme points:";

        /**
         * Default constructor for an EditCredalSet.
         */
        public EditCredalSet(OverlayBayesItem item) {
         	this.item = item;

            // Copy the probability values in the node.
            copy_probability_values();

            // Get the variable names.
            all_variable_names = item.getAllNames();
            
            // Get the variable values.
            all_variable_values = item.getAllValues();

            // Construct the name of the probability function.
            Label probability_name = create_credal_set_name();

            // Construct the table of probability values.
            index_extreme_point = 0;
            probability_table = new FunctionTablePanel(all_variable_names,
                all_variable_values, all_probability_values[index_extreme_point]);
                
            // Credal set panel.
            generate_credal_set_panel();

            // Set the final layout
            setLayout(new BorderLayout());
            add("North", probability_name);
            add("Center", probability_table);
            add("South", csp);
        }
        
        /*
         * Copy the probability values into internal variables.
         */
        private void copy_probability_values() {
            double original_probability_values[];
            all_probability_values = new double[item.numberExtremeDistributions()][];
            for (int i=0; i<all_probability_values.length; i++) {
                original_probability_values = item.getFunctionValues(i);
                all_probability_values[i] = new double[original_probability_values.length];
                for (int j=0; j<all_probability_values[i].length; j++)
                    all_probability_values[i][j] = original_probability_values[j];
            }
        }

        /*
         * Create a Label containing a description of the credal set.
         */
        private Label create_credal_set_name() {
            StringBuffer name = new StringBuffer("K(");
            name.append(item.getName());
            if (item.hasParent()) {
                name.append(" |");
                OverlayBayesItem[] parents = item.getParents();
                for(int i = 0; i < parents.length; i++)
                    name.append(" " + parents[i].getName() + ",");
                name.setCharAt(name.length() - 1, ')');
            }
            else 
                name.append(")");
            return(new Label(name.toString(), Label.CENTER));
        }

        void accept() {
            int i, k;
            all_probability_values[index_extreme_point] = probability_table.get_table();
            for (i=0; i<all_probability_values.length; i++)
                item.setFunctionValues(i, all_probability_values[i]);
            // Update the number of extreme points.
            try {
                int old_number_extreme_points = all_probability_values.length;
                int number_extreme_points =
                   (new Integer( text_local_parameter.getText() )).intValue();
                if (number_extreme_points != all_probability_values.length) {
                   item.setLocalCredalSet(number_extreme_points);
                   copy_probability_values();
                   if (index_extreme_point >= number_extreme_points)
                        index_extreme_point = number_extreme_points - 1;
                   probability_table.insert_table(all_probability_values[index_extreme_point]);
                   if (number_extreme_points > old_number_extreme_points)
                        for (k=old_number_extreme_points; k<number_extreme_points; k++)
                            credal_set_choice.addItem( String.valueOf(k) );
                   if (old_number_extreme_points > number_extreme_points)
                        for (k=(old_number_extreme_points-1); k>=number_extreme_points; k--)
                            credal_set_choice.remove(k);
                   credal_set_choice.select(index_extreme_point);
                }
            } catch (NumberFormatException ex) {  }
        }
        
        void dismiss() {
            // No-op.
        }
            
        /**
         * Generate a panel for credal set.
         */
        private void generate_credal_set_panel() {
            csp = new Panel();
            csp.setLayout(new BorderLayout());
            
            Label credal_set_specification_label = 
                new Label(credal_set_specification, Label.CENTER);
                
            ics = new Panel();
            ics.setLayout(new BorderLayout());
            Label credal_set_label = new Label(credal_set);
            credal_set_choice = new Choice();
            for (int i=0; i<item.numberExtremeDistributions(); i++)
                credal_set_choice.addItem( String.valueOf(i) );
            ics.add("West", credal_set_label);
            ics.add("Center", credal_set_choice);
            
            qbpp = new Panel();
            qbpp.setLayout(new BorderLayout());
            local_parameter = new Label(number_extreme_points_label);
            text_local_parameter = new TextField(5);
            int number_extreme_points = item.numberExtremeDistributions();
            text_local_parameter.setText( String.valueOf(number_extreme_points) );
            qbpp.add("West", local_parameter);
            qbpp.add("Center", text_local_parameter);
            
            csp.add("North", credal_set_specification_label);
            csp.add("Center", qbpp);
            csp.add("South", ics);
        }
        
        /**
         * Handle the events.
         */
        @SuppressWarnings("deprecation")
		public boolean action(Event evt, Object arg) {
            if (evt.target == credal_set_choice) {
                all_probability_values[index_extreme_point] = probability_table.get_table();
                index_extreme_point = credal_set_choice.getSelectedIndex();
                probability_table.insert_table(all_probability_values[index_extreme_point]);
                return(true);
            }
            return(super.action(evt, arg));        
        }
    }
}

class ObserveDlg extends Dialog {
	private static final long serialVersionUID = 1L;

	private OverlayBayesUMGraph umGraph = null;
    private OverlayBayesItem item = null;

    boolean observed;

    Checkbox observedBox;
    List valuesList;

    @SuppressWarnings("deprecation")
	public ObserveDlg(OverlayBayesUMGraph umGraph, OverlayBayesItem item) {
    	super(umGraph.getParentFrame(), "Set Observe Value", true);
    	this.umGraph = umGraph;
    	this.item = item;

    	Panel cbp = new Panel();
    	cbp.setLayout(new FlowLayout(FlowLayout.CENTER));

    	observed = item.isObserved();
    	observedBox = new Checkbox("Observed", null, observed);
    	cbp.add(observedBox);

    	Panel listp = new Panel();
	    listp.setLayout(new GridLayout(1, 1));
    	valuesList = new List(6, false);

    	String[] values = item.getValues();
    	for (int i=0; i < values.length; i++)
	        valuesList.addItem(new String(values[i]));

    	if (observed) {
    	    valuesList.select(item.getObservedValue());
        }

    	listp.add(valuesList);

    	Panel okp = new Panel();
	    okp.setLayout(new FlowLayout(FlowLayout.CENTER));
    	okp.add(new Button("Ok"));
    	okp.add(new Button("Cancel"));

        setLayout(new BorderLayout());
    	add("North", cbp);
    	add("Center", listp);
	    add("South", okp);
    	pack();
    }

    @SuppressWarnings("deprecation")
	public boolean action(Event evt, Object arg) {
    	if (evt.target == observedBox) {
    		observed = observedBox.getState();
    		if (observed)
    			valuesList.select(0);	// select first value by default
    		else	// clear any selection
    			valuesList.deselect(valuesList.getSelectedIndex());
    		return super.action(evt, arg);
    	}
    	else if (evt.target == valuesList) {
    		if (! observed) {
    			observed = true;
    			observedBox.setState(observed);
    		}
    		return super.action(evt, arg);
    	}
    	else if (arg.equals("Ok")) {
    		String selValue = null;
    		observed = observedBox.getState();
    		selValue = valuesList.getSelectedItem();
    		if (observed && selValue == null) {
    			System.out.println("Observed value error");
    			return true; // do not close this dialog box
    		}
    		if (observed)
    			item.setObservedValue(selValue);
    		else
    			item.clearObserved();
    		DefaultGraphCell node = umGraph.getNode(item);
    		Map<?, ?> attrs = node.getAttributes();
            if(item.isObserved())
            	GraphConstants.setBackground(attrs, OverlayBayesUMGraph.OBSERVED_NODE_COLOR);
            else if(item.isExplanation())
            	GraphConstants.setBackground(attrs, OverlayBayesUMGraph.EXPLANATION_NODE_COLOR);
            else
            	GraphConstants.setBackground(attrs, OverlayBayesUMGraph.NORMAL_NODE_COLOR);
            node.setAttributes(attrs);
    		dispose();
    	}
    	else if (arg.equals("Cancel")) {
    		dispose();
    	}
    	else return super.action(evt, arg);
    	return true;
    }
}

class NetPropertyDlg extends Dialog {
	private static final long serialVersionUID = 1L;

	// The InferenceGraph object that contains the network.
    OverlayBayesUMGraph umGraph = null;

    // Variables that hold the properties in the dialog.
    PropertyManager property_manager;
    
    // Variables used to construct the dialog.
    int displayed_network_property_index;
    Panel np, npp, gnp, gncp, gnpp, tp, okp;
    Label name, network_properties;
    TextField text_name, text_global_parameter;
    Label global, global_parameter;
    CheckboxGroup globals;
    Checkbox no_global, epsilon_global, ratio_global, total_global, bounded_global;
    Button new_network_property, next_network_property;
    TextField network_properties_text;
    Button ok_button, dismiss_button;
    
    // Constants used to construct the dialog.
    private final static int TOP_INSET = 5;
    private final static int LEFT_INSET = 10;
    private final static int RIGHT_INSET = 10;
    private final static int BOTTOM_INSET = 0;

    // Labels for the various elements of the dialog.
    private final static String dialog_title = "Edit Network";
    private final static String name_label = "Name:";
    private final static String network_properties_label = "Network properties:";
    private final static String next_property_label = "Next";
    private final static String new_property_label = "New";
    private final static String global_label = "Network neighborhood model:";
    private final static String no_global_label = "No global neighborhood";
    private final static String epsilon_global_label = "Epsilon contaminated neighborhood";
    private final static String ratio_global_label = "Constant density ratio neighborhood";
    private final static String total_global_label = "Total variation neighborhood";
    private final static String bounded_global_label = "Constant density bounded neighborhood";
    private final static String global_parameter_label = "Global neighborhood parameter:";
    private final static String ok_label = "Apply";
    private final static String dismiss_label = "Cancel";

    /**
     * Default constructor for an EditNetworkDialog object.  
     */
    public NetPropertyDlg(OverlayBayesUMGraph umGraph) {
    	super(umGraph.getParentFrame(), dialog_title, true);
    	this.umGraph = umGraph;

    	// Compose the whole frame.

        // Panel for the name.
        np = new Panel();
        np.setLayout(new BorderLayout());
        name = new Label(name_label);
        text_name = new TextField(30);
        np.add("West", name);
        np.add("Center", text_name);

        // Network properties.
        npp = new Panel();
        npp.setLayout(new BorderLayout());
        network_properties = new Label(network_properties_label);
        next_network_property = new Button(next_property_label);
        new_network_property = new Button(new_property_label);
        network_properties_text = new TextField(40);

        npp.add("North", network_properties);
        npp.add("West", next_network_property);
        npp.add("Center", network_properties_text);
        npp.add("East", new_network_property);

        // Global neighborhood parameters
        gnp = new Panel();
        gnp.setLayout(new BorderLayout());
        global = new Label(global_label);

        gncp = new Panel();
        gncp.setLayout(new GridLayout(5, 1));
        globals = new CheckboxGroup();
        no_global = new Checkbox(no_global_label, globals, true);
        epsilon_global = new Checkbox(epsilon_global_label, globals, false);
        ratio_global = new Checkbox(ratio_global_label, globals, false);
        total_global = new Checkbox(total_global_label, globals, false);
        bounded_global = new Checkbox(bounded_global_label, globals, false);
        gncp.add(no_global);
        gncp.add(epsilon_global);
        gncp.add(ratio_global);
        gncp.add(total_global);
        gncp.add(bounded_global);

        gnpp = new Panel();
        gnpp.setLayout(new BorderLayout());
        global_parameter = new Label(global_parameter_label);
        text_global_parameter = new TextField(10);
        gnpp.add("West", global_parameter);
        gnpp.add("Center", text_global_parameter);

        gnp.add("North", global);
        gnp.add("Center", gncp);
        gnp.add("South", gnpp);

        // All the network parameters
        tp = new Panel();
        tp.setLayout(new BorderLayout());
    	tp.add("North", np);
    	tp.add("Center", npp);
    	tp.add("South", gnp);

        // Return buttons
     	okp = new Panel();
	    okp.setLayout(new FlowLayout(FlowLayout.CENTER));
        ok_button =	new Button(ok_label);
        dismiss_button = new Button(dismiss_label);
        okp.add(ok_button);
        okp.add(dismiss_button);

        setLayout(new BorderLayout());
        add("North", tp);
        add("Center", okp);

	    // Pack the whole window
    	pack();

    	// Initialize values
    	fill_dialog();
    }

    /**
     * Customized show method.
     */
    @SuppressWarnings("deprecation")
	public void show() {
    	Rectangle bounds = getParent().bounds();
    	Rectangle abounds = bounds();

    	move(bounds.x + (bounds.width - abounds.width)/ 2,
    	     bounds.y + (bounds.height - abounds.height)/2);

    	super.show();
    }

    /**
     * Customized insets method. 
     */
    @SuppressWarnings("deprecation")
	public Insets insets() {
        Insets ins = super.insets();
        return(new Insets(ins.top + TOP_INSET, ins.left + LEFT_INSET,
                          ins.bottom + BOTTOM_INSET, ins.right + RIGHT_INSET));
    }

    /**
     * Handle the possible destruction of the window.
     */
    @SuppressWarnings("deprecation")
	public boolean handleEvent(Event evt) {
        if (evt.id == Event.WINDOW_DESTROY)
            dispose();
        return(super.handleEvent(evt));
    }

    /*
     * Fill the values in the dialog area.    
     */
    @SuppressWarnings("deprecation")
	private void fill_dialog() {
        // Synchronize the network if necessary.
        umGraph.getUserModel().getInnerBayesNet();

        // Fill the name.
        text_name.setText(umGraph.getUserModel().getName());

        // Fill and store network properties
        property_manager = new PropertyManager(umGraph.getUserModel().getNetworkProperties(), 
                                               network_properties_text);
                                               
        // Set global neighborhood
        switch (umGraph.getUserModel().getGlobalNeighborhoodType()) {
            case InferenceGraph.NO_CREDAL_SET:
            globals.setCurrent(no_global);
            break;
            case InferenceGraph.CONSTANT_DENSITY_RATIO:
            globals.setCurrent(ratio_global);
            break;
            case InferenceGraph.EPSILON_CONTAMINATED:
            globals.setCurrent(epsilon_global);
            break;
            case InferenceGraph.CONSTANT_DENSITY_BOUNDED:
            globals.setCurrent(bounded_global);
            break;
            case InferenceGraph.TOTAL_VARIATION:
            globals.setCurrent(total_global);
            break;
        }

        double par = umGraph.getUserModel().getGlobalNeighborhoodParameter();
        text_global_parameter.setText( String.valueOf(par) );
    }

    /**
     * Handle the possible events.
     */
    @SuppressWarnings("deprecation")
	public boolean action(Event evt, Object arg) {
        if (evt.target == dismiss_button) {
	        dispose();
	    } else if (evt.target == ok_button) {
	        update_dialog();
	        dispose();
	    } else if (evt.target == new_network_property) {
	        property_manager.new_property();
        } else if (evt.target == next_network_property) {
            property_manager.next_property();
        } else if (evt.target == network_properties_text) {
            property_manager.update_property();
        } else return super.action(evt, arg);
        
        return(true);
    }	        

	/*
	 * Update the contents of the network when the        
	 * dialog exits.                                      
	 */
	@SuppressWarnings("deprecation")
	private void update_dialog() {
        // Update the name of the network.
	    String new_network_name = text_name.getText();
	    if ( !(new_network_name.equals( umGraph.getUserModel().getName() )) ) {
	        new_network_name = umGraph.getUserModel().checkName(new_network_name);
	        if (new_network_name != null)
	        	umGraph.getUserModel().setName(new_network_name);
    	}

        // Update the properties (if necessary).
        Vector<?> prop = property_manager.update_property_on_exit();
        if (prop != null)
        	umGraph.getUserModel().setNetworkProperties(prop);
        
        // Update the global neighborhood parameters.
        Checkbox selected_global_neighborhood = globals.getCurrent();
        if (selected_global_neighborhood == no_global) 
        	umGraph.getUserModel().setGlobalNeighborhoodType(InferenceGraph.NO_CREDAL_SET);
        else if (selected_global_neighborhood == epsilon_global) 
        	umGraph.getUserModel().setGlobalNeighborhoodType(InferenceGraph.EPSILON_CONTAMINATED);
        else if (selected_global_neighborhood == ratio_global) 
        	umGraph.getUserModel().setGlobalNeighborhoodType(InferenceGraph.CONSTANT_DENSITY_RATIO);
        else if (selected_global_neighborhood == bounded_global) 
        	umGraph.getUserModel().setGlobalNeighborhoodType(InferenceGraph.CONSTANT_DENSITY_BOUNDED);
        else if (selected_global_neighborhood == total_global) 
        	umGraph.getUserModel().setGlobalNeighborhoodType(InferenceGraph.TOTAL_VARIATION);

        try {
            double par = 
                (new Double( text_global_parameter.getText() ).doubleValue() );
            if (par <= 0.0) par = 0.0;
            umGraph.getUserModel().setGlobalNeighborhoodParameter(par);
        } catch (NumberFormatException e) { } // Leave parameter as is if in error.
    }
}

