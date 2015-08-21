/**
 * 
 */
package vn.spring.zebra.collab;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;

import vn.spring.zebra.mining.DistanceProfile;
import vn.spring.zebra.mining.ProfileCluster;
import vn.spring.zebra.mining.UserClusterer;
import vn.spring.zebra.um.gui.UIDispose;

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
public class UserClustererGraph extends JGraph implements UIDispose {
	private static final long serialVersionUID = 1L;

	public static final int COL_NUM = 5;
	public static final int DX = 200;
	public static final int DY = 100;
	public static final int DY2 = 2*DY;
	public static final Color NODE_COLOR = Color.white;
	public static final Color CENTER_COLOR = Color.red;
	
	protected UserClusterer userClusterer = null;
	
	public void dispose() {}
	protected UserClustererGraph() {
		setModel(new DefaultGraphModel());
	}
	public UserClustererGraph(UserClusterer userClusterer) {
		this();
		update(userClusterer);
	}
	public void update(UserClusterer userClusterer) {
		this.userClusterer = userClusterer;
		update(userClusterer.getClusters());
	}
	protected void update(ArrayList<ProfileCluster> clusters) {
		clear();
		Point start = new Point(0, 0);
		for(ProfileCluster cluster : clusters) {
			ArrayList<DistanceProfile> profiles = cluster.getProfiles();
			
			Dimension size = new Dimension();
			size.width = ((profiles.size() < COL_NUM ? profiles.size() : COL_NUM) - 1)*DX;
			size.height = (profiles.size() - 1)/COL_NUM *DY;
			
			Point centerPos = new Point(start.x + size.width/2, start.y + size.height / 2);
			DefaultGraphCell centerNode = new DefaultGraphCell("center");
			insertNode(centerNode, centerPos, true);
			for(int i = 0; i < profiles.size(); i++) {
				int row = i/COL_NUM;
				int col = i%COL_NUM;
				Point pos = new Point(start.x + col*DX, start.y + row*DY);
				
				DistanceProfile profile = profiles.get(i);
				DefaultGraphCell node = new DefaultGraphCell(profile.userid);
				insertNode(node, pos, false);
				insertEdge(centerNode, node, profile.dis);
			}
			start.translate(0, size.height + DY2);
		}
	}
	private void clear() {
		Object[] cells = getRoots();
		if(cells == null || cells.length == 0) return;
		CellView[] views = getView().getMapping(cells);
		for (int i = 0; i < views.length; i++) {
			getModel().remove(new Object[] {views[i].getCell()});
		}
	}
	private void insertNode(DefaultGraphCell node, Point pos, boolean isCenter) {
		Rectangle bound = new Rectangle(pos.x < 0 ? 0 : pos.x, pos.y < 0 ? 0 : pos.y, 100, 20);
        node.add(new DefaultPort());
        
        Map map = GraphConstants.createMap();
        GraphConstants.setAutoSize(map, false);
        GraphConstants.setEditable(map, false);
        GraphConstants.setBounds(map, bound);
        GraphConstants.setBorderColor(map, Color.magenta);
        GraphConstants.setBackground(map, isCenter?CENTER_COLOR:NODE_COLOR);
        GraphConstants.setOpaque(map, true);
        
        Map attributes = new Hashtable();
        attributes.put(node, map);
        getModel().insert(new Object[] { node }, null, null, attributes);
	}
	
	private void insertEdge(DefaultGraphCell srcNode, DefaultGraphCell dstNode, double dis) {
		String weight = String.valueOf(dis);
		int idx = weight.indexOf(".");
		if(idx == -1) weight = weight.substring(0, 4);
		else {
			if(idx == weight.length() - 1)
				weight = weight.substring(0, idx);
			else {
				weight = weight.substring(0, idx) + "." + 
					weight.substring(idx+1, idx + 3 > weight.length() ? weight.length() : idx + 3 );
			}
		}
        DefaultPort      sp = new DefaultPort();
        srcNode.add(sp);
        
        DefaultPort dp = new DefaultPort();
        dstNode.add(dp);

        ConnectionSet    cs = new ConnectionSet();
        DefaultEdge      edge = new DefaultEdge(weight);
        cs.connect(edge, sp, dp);
        
        Map map = GraphConstants.createMap();
        GraphConstants.setEditable(map, false);
        GraphConstants.setLineEnd(map, GraphConstants.NONE);

        Hashtable attributes = new Hashtable();
        attributes.put(edge, map);
        getModel().insert(new Object[] {edge}, cs, null, attributes);
	}

}
