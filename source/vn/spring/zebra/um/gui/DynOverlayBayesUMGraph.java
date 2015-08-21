package vn.spring.zebra.um.gui;

import java.awt.FlowLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import vn.spring.bayes.InterchangeFormat.IFException;

import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.um.DynOverlayBayesUM;
import vn.spring.zebra.um.DynOverlayBayesUMChangeEvent;
import vn.spring.zebra.um.DynOverlayBayesUMChangeListener;
import vn.spring.zebra.um.OverlayBayesUM;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class DynOverlayBayesUMGraph extends JPanel implements DynOverlayBayesUMChangeListener, UIDispose {
	private static final long serialVersionUID = 1L;
	
	protected DynOverlayBayesUM dynUM = null;
	protected JFrame frmParent = null;
	protected ArrayList<OverlayBayesUMGraph> graphs = new ArrayList<OverlayBayesUMGraph>();
	
	public DynOverlayBayesUMGraph(DynOverlayBayesUM dynUM, JFrame frmParent) throws ZebraException, IFException, IOException{
		super();
		this.dynUM = dynUM;
		this.frmParent = frmParent;
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		update();
		dynUM.addChangeListener(this);
	}
	public synchronized DynOverlayBayesUM getUserModel() {return dynUM;}
	
	public void dispose() {
		try {dynUM.removeChangeListener(this);}
		catch(Exception e) {System.out.println("DynOverlayBayesUMGraph.dispose causes error: " + e.getMessage());}
		
		try {if(dynUM.isChanged()) dynUM.save();}
		catch(Exception e) {System.out.println("DynOverlayBayesUMGraph.dispose causes error: " + e.getMessage());}
	}
	public void save() throws ZebraException, FileNotFoundException {
		dynUM.save();
	}
	protected void update() throws ZebraException, IFException, IOException {
		int m = dynUM.size();
		int n = graphs.size();
		
		int start = 0;
		if(m == n)      return;
		else if(m < n)  {start = 0; graphs.clear(); removeAll();}
		else            start = n;
		
		for(int i = start; i < m; i++) {
			OverlayBayesUM um = dynUM.getState(i).getUserModel();
			OverlayBayesUMGraph graph = new OverlayBayesUMGraph();
			graph.load(um, frmParent);
			
			graphs.add(graph);
			add(graph);
		}
	}

	public synchronized void stateAdded(DynOverlayBayesUMChangeEvent evt) {
		// TODO Auto-generated method stub
		OverlayBayesUMGraph graph = null;
		try {
			graph = new OverlayBayesUMGraph();
			graph.load(evt.getState().getUserModel(), frmParent);
		}
		catch(Exception e) {
			System.out.println("DynOverlayBayesUMGraph.stateAdded causes error: " + e.getMessage());
		}
		if(graph != null) {
			graphs.add(graph);
			add(graph);
		}
	}

	public synchronized void stateCleared(DynOverlayBayesUMChangeEvent evt) {
		// TODO Auto-generated method stub
		graphs.clear();
		removeAll();
	}
	public synchronized void stateReloaded(DynOverlayBayesUMChangeEvent evt) {
		// TODO Auto-generated method stub
		stateCleared(evt);
		stateAdded(evt);
	}
	
}
