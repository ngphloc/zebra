/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import vn.spring.zebra.um.LearningStyleModel;
import vn.spring.zebra.um.HiddenMarkovUM.OptimalStateChain;

import com.jgraph.JGraph;
import com.jgraph.event.*;
import com.jgraph.graph.*;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class LearningStyleGraph extends JGraph implements MouseListener,
		GraphModelListener, UIDispose {
	private static final long serialVersionUID = 1L;

	public static final Color NODE_COLOR = Color.white;
	public static final int DX = 100;
	public static final int DY = 80;
	public static enum GET_EDGES {
		FROM, TO, BOTH
	}
	public final static int MAX_STATE = 10;

	private LearningStyleModel lshmm = null;
	
	public LearningStyleGraph(LearningStyleModel lshmm) {
		this.lshmm = lshmm;
		setModel(new DefaultGraphModel());
		getModel().addGraphModelListener(this);
		addMouseListener(this);
		update(this.lshmm.getVerbalVisualChain(), this.lshmm.getActiveReflectiveChain(), this.lshmm.getTheoristPragmatistChain());
	}
	public void dispose() {}
	
	public void update(OptimalStateChain verbal_visual, OptimalStateChain active_reflective, OptimalStateChain theorist_pragmatist) {
		clear();
		Point start = null;
		
		start = new Point(0, 0*DY);
		update(verbal_visual, start);
		
		start = new Point(0, 1*DY);
		update(active_reflective, start);
		
		start = new Point(0, 2*DY);
		update(theorist_pragmatist, start);
	}
	private void clear() {
		Object[] cells = getRoots();
		if(cells == null || cells.length == 0) return;
		CellView[] views = getView().getMapping(cells);
		for (int i = 0; i < views.length; i++) {
			getModel().remove(new Object[] {views[i].getCell()});
		}
	}
	private void update(OptimalStateChain chain, Point start) {
		if(chain == null) return;
		DefaultGraphCell preNode = null;
		int n = chain.states.length;
		for(int i = (n < MAX_STATE ? 0 : n - MAX_STATE); i < n; i++) {
			DefaultGraphCell node = new DefaultGraphCell(chain.states[i]);
			insertNode(node, start);
			if(preNode != null) {
				insertEdge(preNode, node);
			}
			start.translate(DX, 0);
			preNode = node;
		}
	}
	private void insertNode(DefaultGraphCell node, Point pos) {
		Rectangle bound = new Rectangle(pos.x < 0 ? 0 : pos.x, pos.y < 0 ? 0 : pos.y, 10, 10);
        node.add(new DefaultPort());
        
        Map map = GraphConstants.createMap();
        GraphConstants.setAutoSize(map, true);
        GraphConstants.setEditable(map, false);
        GraphConstants.setBounds(map, bound);
        GraphConstants.setBorderColor(map, Color.magenta);
        GraphConstants.setBackground(map, NODE_COLOR);
        GraphConstants.setOpaque(map, true);
        
        Map attributes = new Hashtable();
        attributes.put(node, map);
        getModel().insert(new Object[] { node }, null, null, attributes);
	}
	
	private void insertEdge(DefaultGraphCell srcNode, DefaultGraphCell dstNode) {
        DefaultPort      sp = new DefaultPort();
        srcNode.add(sp);
        
        DefaultPort dp = new DefaultPort();
        dstNode.add(dp);

        ConnectionSet    cs = new ConnectionSet();
        DefaultEdge      edge = new DefaultEdge();
        cs.connect(edge, sp, dp);
        
        Map map = GraphConstants.createMap();
        GraphConstants.setEditable(map, false);
        GraphConstants.setLineEnd(map, GraphConstants.SIMPLE);

        Hashtable attributes = new Hashtable();
        attributes.put(edge, map);
        getModel().insert(new Object[] {edge}, cs, null, attributes);
	}
	private void removeNode(State state) {
		ArrayList<DefaultEdge> rEdges = getEdges(state, GET_EDGES.BOTH);
		DefaultGraphCell node = getNode(state);
		if(node == null) return;
		
		getModel().remove(new Object[] {node});
		if(rEdges.size() > 0) getModel().remove(rEdges.toArray());
	}
	protected void removeNode(OptimalStateChain chain) {
		for(int i = 0; i < chain.states.length; i++) {
			State state = new State(chain, chain.states[i], i);
			removeNode(state);
		}
	}
	private DefaultGraphCell getNode(State state) {
		if(state == null) return null;
		
		Object[] cells = getRoots();
		if(cells == null || cells.length == 0) return null;
		
		CellView[] views = getView().getMapping(cells);
		for (int i = 0; i < views.length; i++) {
			Object cell = views[i].getCell();
			if( !(cell instanceof DefaultGraphCell) || (cell instanceof DefaultEdge) )
				continue;
			Object found = ((DefaultGraphCell)cell).getUserObject();
			if( (found == null) || !(found instanceof State) ) continue;
			if( ((State)found).equals(state) )
				return (DefaultGraphCell)cell;
		}
		return null;
    }
	
	private ArrayList<DefaultEdge> getEdges(State state, GET_EDGES flag) {
		ArrayList<DefaultEdge> rEdges = new ArrayList<DefaultEdge>();
		
		Object[] cells = getRoots();
		if(cells == null || cells.length == 0) return rEdges;
		
		CellView[] views = getView().getMapping(cells);
		for (int i = 0; i < views.length; i++) {
			if( !(views[i].getCell() instanceof DefaultEdge)) continue;
			DefaultEdge edge = (DefaultEdge)(views[i].getCell());
			Object source = getModel().getParent(getModel().getSource(edge));
			Object target = getModel().getParent(getModel().getTarget(edge));
			
			if(flag == GET_EDGES.BOTH) {
				State found = null;
				if( 
				   (source instanceof DefaultGraphCell) && 
				   (((DefaultGraphCell)source).getUserObject() instanceof State)
				  )
				{
					found = (State) (((DefaultGraphCell)source).getUserObject());
					if(found.equals(state))
						rEdges.add(edge);
					else
						found = null;
				}

				if(
				   (found == null) &&
				   (target instanceof DefaultGraphCell) && 
				   (((DefaultGraphCell)target).getUserObject() instanceof State)
				  )
				{
					found = (State) (((DefaultGraphCell)target).getUserObject());
					if(found.equals(state)) rEdges.add(edge);
				}
			}
			else if(flag == GET_EDGES.FROM) {
				if( 
				   (source instanceof DefaultGraphCell) && 
				   (((DefaultGraphCell)source).getUserObject() instanceof State)
				  )
				{
					State found = (State) (((DefaultGraphCell)source).getUserObject());
					if(found.equals(state)) rEdges.add(edge);
				}
			}
			else if(flag == GET_EDGES.TO) {
				if( 
				   (target instanceof DefaultGraphCell) && 
				   (((DefaultGraphCell)target).getUserObject() instanceof State)
				  )
				{
					State found = (State) (((DefaultGraphCell)target).getUserObject());
					if(found.equals(state)) rEdges.add(edge);
				}
			}
		}
		return rEdges;
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	public void graphChanged(GraphModelEvent arg0) {}
	
}

class State {
	private OptimalStateChain chain = null;
	private String state  = null;
	private int idx = -1;
	
	public State(OptimalStateChain chain, String state, int idx) {
		this.chain = chain;
		this.state = state;
		this.idx = idx;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof State)) return false;
		State state = (State)obj;
		return ((this.chain.equals(state.chain)) && (this.state.equals(state.state)) && (this.idx == state.idx));
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(chain == null || state == null || idx == -1)
			return super.toString();
		return "" + (idx +1) + ":" + state;
	}
	
}
