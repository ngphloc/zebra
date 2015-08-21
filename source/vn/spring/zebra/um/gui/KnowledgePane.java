/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;


import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.um.DynOverlayBayesUM;
import vn.spring.zebra.um.OverlayBayesUM;
import vn.spring.zebra.um.OverlayBayesUMFactory;
import vn.spring.bayes.InterchangeFormat.IFException;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class KnowledgePane extends JPanel implements UIDispose, ActionListener {
	private static final long serialVersionUID = 1L;
	private String userid = null;
	private String course = null;
	private OverlayBayesUMGraph staticUMGraph = null;
	private DynOverlayBayesUMGraph dynUMGraph = null;
	protected JFrame frmParent = null;
	
	public KnowledgePane(String userid, String course,
			OverlayBayesUM staticUM, DynOverlayBayesUM dynUM, JFrame frmParent) throws ZebraException, IFException, IOException{
		super();

		this.userid = userid;
		this.course = course;
		this.frmParent = frmParent;
		TitledBorder title = null;

		JButton staticSave = new JButton("Save");
    	staticSave.setName("STATIC_SAVE");
    	staticSave.addActionListener(this);
    	JPanel staticButton = new JPanel();
    	staticButton.setLayout(new BorderLayout());
    	staticButton.add(staticSave, BorderLayout.WEST);
    	staticButton.add(new JPanel(), BorderLayout.CENTER);
    	//
		staticUMGraph = new OverlayBayesUMGraph();
		staticUMGraph.load(staticUM, frmParent);
		title = BorderFactory.createTitledBorder("STATIC OVERLAY BAYESIAN MODEL");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
    	JPanel staticPanel = new JPanel();
    	staticPanel.setBorder(title);
    	staticPanel.setLayout(new BorderLayout());
    	//
    	staticPanel.add(staticButton, BorderLayout.NORTH);
    	staticPanel.add(staticUMGraph, BorderLayout.CENTER);
    	
    	JButton dynSave = new JButton("Save");
    	dynSave.setName("DYN_SAVE");
    	dynSave.addActionListener(this);
    	JPanel dynButton = new JPanel();
    	dynButton.setLayout(new BorderLayout());
    	dynButton.add(dynSave, BorderLayout.WEST);
    	dynButton.add(new JPanel(), BorderLayout.CENTER);
    	//
    	dynUMGraph = new DynOverlayBayesUMGraph(dynUM, frmParent);
		title = BorderFactory.createTitledBorder("DYNAMIC OVERLAY BAYESIAN MODEL");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
    	JPanel dynPanel = new JPanel();
    	dynPanel.setLayout(new BorderLayout());
    	dynPanel.setBorder(title);
    	//
    	dynPanel.add(dynButton, BorderLayout.NORTH);
    	dynPanel.add(dynUMGraph, BorderLayout.CENTER);
    	
    	setLayout(new BorderLayout());
    	JTabbedPane mainPane = new JTabbedPane();
    	mainPane.addTab("Static", staticPanel);
    	mainPane.addTab("Dynamic", dynPanel);
    	mainPane.addTab("TOC Evaluation", new ZebraTOCPane2(this.userid, this.course));
    	add(mainPane, BorderLayout.CENTER);
	}
	public void dispose() {
		try {staticUMGraph.dispose();}
		catch(Exception e) {System.out.println("KnowledgePane.dispose causes error: " + e.getMessage());}
		
		try {dynUMGraph.dispose();}
		catch(Exception e) {System.out.println("KnowledgePane.dispose cause error: " + e.getMessage());}
	}
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(!(source instanceof JButton)) return;
		JButton button = (JButton)source;
		if(button.getName().equals("STATIC_SAVE")) {
			try {
				staticSave();
			}
			catch(Exception ex) {
				System.out.println("KnowledgePane.actionPerformed causes error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
		else if(button.getName().equals("DYN_SAVE")) {
			try {
				dynSave();
			}
			catch(Exception ex) {
				System.out.println("KnowledgePane.actionPerformed causes error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
	private void staticSave() throws ZebraException, FileNotFoundException {
		staticUMGraph.saveToDatabase(userid, course, OverlayBayesUMFactory.getFormatType(OverlayBayesUM.OBUM_EXT), false);
	}
	private void dynSave() throws ZebraException, FileNotFoundException {
		dynUMGraph.save();
	}
}
