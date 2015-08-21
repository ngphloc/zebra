/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.um.OverlayBayesItem;
import vn.spring.zebra.um.OverlayBayesUM;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ItemBetaGraphDlg extends JDialog {
	private static final long serialVersionUID = 1L;
	protected static final int NUMBER_ROW_IN_EXMAPLE_TRAINING_DATA = 5;
	
	protected OverlayBayesUMGraph graph = null;
	protected OverlayBayesUM um = null;
	protected OverlayBayesItem item = null;
	protected ItemBetaGraphPane betaPane = null;
	protected JTextArea arffText = null;
	
	public ItemBetaGraphDlg(Frame frmParent, OverlayBayesUMGraph graph, OverlayBayesUM um, OverlayBayesItem item) throws ZebraException {
		super(frmParent, "User Course Input", true);
		
		this.graph = graph;
		this.um = um;
		this.item = item;
    	setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(new Dimension(800, 600));
		JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    	getContentPane().add(mainPane);
		mainPane.setDividerLocation(400);
    	
		betaPane = new ItemBetaGraphPane(this.item);
		mainPane.add(betaPane, JSplitPane.TOP);

		JPanel dataPane = new JPanel();
		dataPane.setLayout(new BorderLayout());
		arffText = new JTextArea(10, 20);
		arffText.setAutoscrolls(true);
		arffText.setEditable(true);
		arffText.setBorder(new TitledBorder("Training data"));
		dataPane.add(new JScrollPane(arffText), BorderLayout.CENTER);
		
		JPanel learnPane = new JPanel(); learnPane.setLayout(new GridLayout(0, 2));
		learnPane.add(
			new JButton(new AbstractAction("Learn by EM") {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
					try {
						onLearnByEM();
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}));
		learnPane.add(
			new JButton(new AbstractAction("Learn by MLE") {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
					try {
						onLearnByMLE();
					}
					catch(Exception ex) {
						ex.printStackTrace();
					}
				}
			}));
		dataPane.add(learnPane, BorderLayout.NORTH);
		
		JPanel examplePane = new JPanel(); learnPane.setLayout(new GridLayout(0, 2));
		examplePane.add(
			new JButton(new AbstractAction("Load Example") {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
						onLoadExampleData();
				}
			}));
		examplePane.add(
			new JButton(new AbstractAction("Browse") {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
						onBrowse();
				}
			}));
		dataPane.add(examplePane, BorderLayout.SOUTH);
		
		mainPane.add(dataPane, JSplitPane.BOTTOM);
	}
	private void onExtractTrainingData(ArrayList<String> attrs, ArrayList<double[]> dataset) throws IOException {
		StringReader arff = new StringReader(arffText.getText());
		Instances instances = new Instances(arff);
		arff.close();
		
		attrs.clear();
		dataset.clear();	
		for(int i = 0; i < instances.numAttributes(); i++) {
			attrs.add(instances.attribute(i).name());
		}
		for(int i = 0; i < instances.numInstances(); i++) {
			Instance instance = instances.instance(i);
			double[] data = new double[attrs.size()];
			for(int j = 0; j < attrs.size(); j++) {
				if(instance.isMissing(j))
					data[j] = -1;
				else
					data[j] = instance.value(j);
			}
			dataset.add(data);
		}
	}
	private void onLearnByEM() throws Exception {
		if(true) {
			JOptionPane.showMessageDialog(this, "You mustn't run this function at here \n" + "" +
					"because it causes the bias for knowledge evaluation in adaptive learning.\n" +
					"I use it for testing!", 
					"Notice", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		ArrayList<String> attrs = new ArrayList<String>();
		ArrayList<double[]> dataset = new ArrayList<double[]>();
		onExtractTrainingData(attrs, dataset);
		if(attrs.size() == 0 || dataset.size() == 0) {
			JOptionPane.showMessageDialog(null, "No attribute or data");
			return;
		}
		if(um == null) {
			JOptionPane.showMessageDialog(null, "Not implemented yet in case that network is null");
			return;
		}
		um.betaLearningByEM(attrs, dataset, 1);
		betaPane.reload();
		if(graph != null) {
			graph.refreshGraph();
		}
	}
	private void onLearnByMLE() throws Exception {
		if(true) {
			JOptionPane.showMessageDialog(this, "You mustn't run this function at here \n" + "" +
					"because it causes the bias for knowledge evaluation in adaptive learning.\n" +
					"I use it for testing!", 
					"Notice", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		ArrayList<String> attrs = new ArrayList<String>();
		ArrayList<double[]> dataset = new ArrayList<double[]>();
		onExtractTrainingData(attrs, dataset);
		if(attrs.size() == 0 || dataset.size() == 0) {
			JOptionPane.showMessageDialog(null, "No attribute or data");
			return;
		}
		if(um == null) {
			JOptionPane.showMessageDialog(null, "Not implemented yet in case that network is null");
			return;
		}
		um.betaLearningByMLE(attrs, dataset, 1);
		betaPane.reload();
		if(graph != null) {
			graph.refreshGraph();
		}
	}
	private void onBrowse() {
		
	}
	private void onLoadExampleData() {
		if(this.um == null) {
			JOptionPane.showMessageDialog(null, "Not implemented yet in case that network is null");
			return;
		}
		if(this.item.isObserved()) {
			JOptionPane.showMessageDialog(null, "Not implemented yet in case that item is evidence");
			return;
		}

		String data = "";
		data += "@relation " + item.getName() + "\n";
		ArrayList<OverlayBayesItem> items = new ArrayList<OverlayBayesItem>();
		items.add(this.item);
		OverlayBayesItem[] parents = this.item.getParents();
		for(OverlayBayesItem parent : parents) {
			items.add(parent);
		}
		
		data += "\n";
		for(OverlayBayesItem item : items) {
			data += "@attribute " + item.getName() + " numeric\n";
		}
		
		data += "\n";
		data += "@data" + "\n";
		for(int i = 0; i < NUMBER_ROW_IN_EXMAPLE_TRAINING_DATA; i++) {
			String row = "";
			for(int j = 0; j < items.size(); j++) {
				double v = 1;//(Math.random() > 0.5 ? 1 : 0);
				row += (int)v;
				if(j < items.size() - 1) row += ",";
			}
			data += row + "\n";
		}
		arffText.setText(data);
	}
}
