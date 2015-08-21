package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import vn.spring.WOW.AMt.AMtc;
import vn.spring.WOW.AMt.ServerFileChooser;
import vn.spring.zebra.um.OverlayBayesItem;
import vn.spring.zebra.um.OverlayBayesUM;
import vn.spring.zebra.um.OverlayBayesUMFactory;

import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultGraphCell;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class OverlayBayesInspectorHelper {
	protected OverlayBayesInspector inspector = null;
	
	public OverlayBayesInspectorHelper(OverlayBayesInspector inspector) {
		this.inspector = inspector;
	}
	
	public void initialize() {
		inspector.setSize(600, 480);
		inspector.setTitle("Overlay Bayes UM Inspector");
		inspector.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		inspector.setJMenuBar(createMenuBar());
		Container contentPanel = inspector.getContentPane();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(createToolBar(), BorderLayout.NORTH);
		inspector.umGraph = new OverlayBayesUMGraph();
		contentPanel.add(new JScrollPane(inspector.umGraph), BorderLayout.CENTER);
	}
	
	private JMenuBar createMenuBar() {
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		JMenuItem open = new JMenuItem("Open");
		open.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					openUMdlg(false);
				}
				catch(Exception ex) {
					System.out.println(ex.getMessage());
					JOptionPane.showMessageDialog(inspector, "Can't open file");
				}
			}
		});
		JMenuItem create = new JMenuItem("Create From Author");
		create.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String course = JOptionPane.showInputDialog(inspector, "Course Name:");
					inspector.load(inspector.base, inspector.author, course, inspector.bayesType, true);
				}
				catch(Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					openUMdlg(true);
				}
				catch(Exception ex) {
					System.out.println(ex.getMessage());
					JOptionPane.showMessageDialog(inspector, "Can't save file");
				}
			}
		});
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inspector.dispose();
			}
		});
		menuFile.add(open);
		menuFile.add(create);
		menuFile.add(save);
		menuFile.addSeparator();
		menuFile.add(exit);
		
		JMenu menuEdit = new JMenu("Edit");
		menuEdit.setMnemonic(KeyEvent.VK_E);
		JMenuItem netProp = new JMenuItem("Network Property");
		netProp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inspector.umGraph.showNetPropertyDlg();
			}
		});
		JMenuItem varProp = new JMenuItem("Variable Property");
		varProp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object cell = inspector.umGraph.getSelectionCell();
				if(cell == null || cell instanceof DefaultEdge) {
					JOptionPane.showMessageDialog(inspector, "You should choose a knowledge item");
				}
				else {
					OverlayBayesItem item = (OverlayBayesItem) (((DefaultGraphCell)cell).getUserObject());
					inspector.umGraph.showVarPropertyDlg(item);
				}
			}
		});
		JMenuItem funcProp = new JMenuItem("Function Property");
		funcProp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object cell = inspector.umGraph.getSelectionCell();
				if(cell == null || cell instanceof DefaultEdge) {
					JOptionPane.showMessageDialog(inspector, "You should choose a knowledge item");
				}
				else {
					OverlayBayesItem item = (OverlayBayesItem) (((DefaultGraphCell)cell).getUserObject());
					inspector.umGraph.showFuncPropertyDlg(item);
				}
			}
		});
		menuEdit.add(netProp);
		menuEdit.addSeparator();
		menuEdit.add(varProp);
		menuEdit.add(funcProp);
		
		JMenu menuView = new JMenu("View");
		menuView.setMnemonic(KeyEvent.VK_V);
		JMenuItem zoom = new JMenuItem("Zoom 1:1");
		zoom.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inspector.umGraph.setScale(1.0);
			}
		});
		JMenuItem zoomIn = new JMenuItem("Zoom In");
		zoomIn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inspector.umGraph.setScale(2 * inspector.umGraph.getScale());
			}
		});
		JMenuItem zoomOut = new JMenuItem("Zoom Out");
		zoomOut.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inspector.umGraph.setScale(inspector.umGraph.getScale() / 2);
			}
		});
		menuView.add(zoom);
		menuView.add(zoomIn);
		menuView.add(zoomOut);
		
		JMenu menuInference = new JMenu("Inference");
		menuInference.setMnemonic(KeyEvent.VK_I);
		JMenuItem marginal_posterior = new JMenuItem("Marginal Posterior");
		marginal_posterior.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object cell = inspector.umGraph.getSelectionCell();
				if(cell == null || cell instanceof DefaultEdge) {
					JOptionPane.showMessageDialog(inspector, "You should choose a knowledge item");
				}
				else {
					OverlayBayesItem item = (OverlayBayesItem) (((DefaultGraphCell)cell).getUserObject());
					String result = inspector.umGraph.inferMariginalPosterior(item.getName()).toString();
					System.out.println(result);
					JOptionPane.showMessageDialog(inspector, result);
				}
			}
		});
		JMenuItem expectation = new JMenuItem("Expectation");
		expectation.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object cell = inspector.umGraph.getSelectionCell();
				if(cell == null || cell instanceof DefaultEdge) {
					JOptionPane.showMessageDialog(inspector, "You should choose a knowledge item");
				}
				else {
					OverlayBayesItem item = (OverlayBayesItem) (((DefaultGraphCell)cell).getUserObject());
					String result = inspector.umGraph.inferExpectation(item.getName()).toString();
					System.out.println(result);
					JOptionPane.showMessageDialog(inspector, result);
				}
			}
		});
		JMenuItem explanation = new JMenuItem("Explanation");
		explanation.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(inspector.umGraph.inferExplanation());
			}
		});
		JMenuItem full_explanation = new JMenuItem("Full Explanation");
		full_explanation.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(inspector.umGraph.inferFullExplanation());
			}
		});
		menuInference.add(marginal_posterior);
		menuInference.add(expectation);
		menuInference.addSeparator();
		menuInference.add(explanation);
		menuInference.add(full_explanation);
		
		JMenu menuLearning = new JMenu("Learning");
		menuLearning.setMnemonic(KeyEvent.VK_L);

		JMenu menuHelp = new JMenu("Help");
		menuHelp.setMnemonic(KeyEvent.VK_H);

		JMenuBar mainMenuBar = new JMenuBar();
		mainMenuBar.add(menuFile);
		mainMenuBar.add(menuEdit);
		mainMenuBar.add(menuView);
		mainMenuBar.add(menuInference);
		mainMenuBar.add(menuLearning);
		mainMenuBar.add(menuHelp);
		
		return mainMenuBar;
	}
	
	private void openUMdlg(boolean isSave) {
		ServerFileChooser loadDlg = new ServerFileChooser(inspector.base, inspector.author, AMtc.AUTHOR_FILES_MODE,
	            inspector,
	            true);
		
		try {
			if(!isSave) {
				String[] ff = {OverlayBayesUM.OBUM_EXT};
				loadDlg.showOpenDialog(ff);
				String course = ServerFileChooser.fileName.replaceAll(OverlayBayesUM.OBUM_EXT, "");
				inspector.load(inspector.base, inspector.author, course, inspector.bayesType, false);
			}
			else {
				inspector.umGraph.saveToAuthor(inspector.base, inspector.author, inspector.course, OverlayBayesUMFactory.getFormatType(OverlayBayesUM.OBUM_EXT), true);
			}
			JOptionPane.showMessageDialog(inspector, "Load or save successfully");
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			JOptionPane.showMessageDialog(inspector, "Load or save unsuccessfully\n" + e.getMessage());
		}
	}
	private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
		return toolBar;
	}
}
