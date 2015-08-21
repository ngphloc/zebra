/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import vn.spring.zebra.mining.CourseAccessSequences2;

import com.jgraph.JGraph;
import com.jgraph.graph.CellView;
import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.GraphConstants;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class LearningPathGraph extends JGraph implements UIDispose {
	private static final long serialVersionUID = 1L;

	public static final Color NODE_COLOR = Color.white;
	public static final int DX = 200;
	public static final int DY = 40;
	
	protected CourseAccessSequences2 sequences = null;
	
	public LearningPathGraph(CourseAccessSequences2 sequences) {
		super();
		this.sequences = sequences;
		setModel(new DefaultGraphModel());
	}
	public void dispose() {}
	public void update() {
		clear();
		ArrayList<ArrayList<String>> path = sequences.getMaxLearningPath();
		
		DefaultGraphCell preNode = null;
		for(int i = 0; i < path.size(); i++) {
			DefaultGraphCell preNode2 = null;
			for(int j = 0; j < path.get(i).size(); j++) {
		        DefaultGraphCell node = new DefaultGraphCell(path.get(i).get(j));
		        insertNode(node, new Point(i*DX, j*DY));
		        
		        if(j == 0) {
		        	if(preNode != null) insertEdge(preNode, node, true);
		        	preNode = node;
		        	preNode2 = node;
		        }
		        else {
		        	if(preNode2 != null) insertEdge(preNode2, node, false);
		        	preNode2 = node;
		        }
				
			}
		}
		
	}
	public void clear() {
		Object[] cells = getRoots();
		if(cells == null || cells.length == 0) return;
		
		CellView[] views = getView().getMapping(cells);
		for (int i = 0; i < views.length; i++) {
			getModel().remove(new Object[] {views[i].getCell()});
		}
	}
	public void insertNode(DefaultGraphCell node, Point pos) {
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

	public void insertEdge(DefaultGraphCell srcNode, DefaultGraphCell destNode, boolean isArrow) {
        DefaultPort      sp = new DefaultPort();
        srcNode.add(sp);
        
        DefaultPort dp = new DefaultPort();
        destNode.add(dp);

        ConnectionSet    cs = new ConnectionSet();
        DefaultEdge      edge = new DefaultEdge();
        cs.connect(edge, sp, dp);
        
        Map map = GraphConstants.createMap();
        GraphConstants.setEditable(map, false);
        if(isArrow)
        	GraphConstants.setLineEnd(map, GraphConstants.SIMPLE);
        else
        	GraphConstants.setLineEnd(map, GraphConstants.NONE);
        
        
        Hashtable attributes = new Hashtable();
        attributes.put(edge, map);
        getModel().insert(new Object[] {edge}, cs, null, attributes);
	}
	
}
