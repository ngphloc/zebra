/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import flanagan.plot.PlotGraph;

import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.math.BetaDensity;
import vn.spring.zebra.um.OverlayBayesItem;
import vn.spring.zebra.um.OverlayBayesItemBetaDensity;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ItemBetaGraphPane extends JTabbedPane {
    protected static final long serialVersionUID = 1L;  // serial version unique identifier
	protected ArrayList<PlotGraph> plotGraphs = new ArrayList<PlotGraph>();
	protected OverlayBayesItem item = null;
    
    public ItemBetaGraphPane(OverlayBayesItem item) throws ZebraException {
    	super();
    	this.item = item;
    	reload();
    }
    public void reload() throws ZebraException {
    	int selectedTabIdx = this.getSelectedIndex();
    	
    	plotGraphs.clear();
    	this.removeAll();
    	OverlayBayesItemBetaDensity itembeta = new OverlayBayesItemBetaDensity(this.item);
    	if(!itembeta.hasParent()) {
    		BetaDensity beta = itembeta.getOneBeta();
    		PlotGraph graph = beta.getGraph();
    		String title = beta.toString() + " of " + itembeta.getOneProbName();
    		graph.setGraphTitle(title);
    		plotGraphs.add(graph);
    		addTab(beta.toString(), graph);
    	}
    	else {
    		ArrayList<BetaDensity> betas = itembeta.getBetas(); 
    		for(int i = 0; i < betas.size(); i++) {
    			BetaDensity beta = betas.get(i);
        		PlotGraph graph = beta.getGraph();
        		String title = beta.toString() + " of " + itembeta.getParentProbName(i);
        		graph.setGraphTitle(title);
        		plotGraphs.add(graph);
        		addTab(beta.toString(), graph);
    		}
    	}
    	if(selectedTabIdx != -1 && selectedTabIdx < getTabCount())
    		setSelectedIndex(selectedTabIdx);
    }
    public void show(JFrame frame) {
    	JDialog dialog = new JDialog(frame, "Beta distribution", true);
    	dialog.add(this, BorderLayout.CENTER);
    	dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    	dialog.setSize(600, 400);
    	dialog.setVisible(true);
    }
    public void show() {
    	JFrame frame = new JFrame("Beta distribution");
    	frame.getContentPane().add(this, BorderLayout.CENTER);
    	frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    	frame.setSize(600, 400);
    	frame.setVisible(true);
    }
}
