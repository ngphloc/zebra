package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import vn.spring.zebra.bn.LSChangeEvent;
import vn.spring.zebra.bn.LSChangeListener;
import vn.spring.zebra.client.LearningStyle;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.um.LearningStyleModel;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class LearningStylePane extends JPanel implements LSChangeListener, UIDispose  {
	private static final long serialVersionUID = 1L;

	private LearningStyleModel lshmm = null;
	private LearningStyleGraph lsGraph = null;
	private JLabel lsResult = null;
	
	public LearningStylePane(LearningStyleModel lshmm) throws ZebraException {
		super();
		
		this.lshmm = lshmm;
		setLayout(new BorderLayout(10, 10));
		TitledBorder title = null;
		
		title = BorderFactory.createTitledBorder("DISCOVERY USER LEARNING STYLES");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		lsResult = new JLabel(
				new LearningStyle(lshmm.isVerbalizer(), lshmm.isActivist(), lshmm.isTheorist()).toString()
			);
		lsResult.setForeground(Color.RED);
		lsResult.setBorder(title);
		add(lsResult, BorderLayout.NORTH);

		title = BorderFactory.createTitledBorder("LEARNING STYLE TRANSITION");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		lsGraph = new LearningStyleGraph(lshmm);
		JPanel graphPane = new JPanel();
		graphPane.setBorder(title);
		graphPane.add(lsGraph);
		add(graphPane, BorderLayout.CENTER);
		
		title = BorderFactory.createTitledBorder("HIDDEN MARKOV MODEL for LEARNING STYLE MODEL");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		LearningStyleImagePane imagePane = new LearningStyleImagePane();
		imagePane.setBorder(title);
		imagePane.setAlignmentX(LEFT_ALIGNMENT);
		add(imagePane, BorderLayout.SOUTH);

		this.lshmm.addChangeListener(this);
	}
	public void dispose() {
		try {lshmm.removeChangeListener(this);}
		catch(Exception e) {System.out.println("LearningStylePane.dispose causes error: " + e.getMessage());}
		
		try {lsGraph.dispose();}
		catch(Exception e) {System.out.println("LearningStylePane.dispose causes error: " + e.getMessage());}
	}
	
	public synchronized void lsChanged(LSChangeEvent evt) {
		// TODO Auto-generated method stub
		lsGraph.update(evt.getVerbalVisualChain(), evt.getActiveReflectiveChain(), evt.getTheoristPragmatistChain());
		lsResult.setText(evt.getLearningStyle().toString());
	}

}
