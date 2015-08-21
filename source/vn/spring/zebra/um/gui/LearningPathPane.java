package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import vn.spring.zebra.mining.CourseAccessSequences2;
import vn.spring.zebra.mining.CourseAccessSequences2ChangeEvent;
import vn.spring.zebra.mining.CourseAccessSequences2ChangeListener;
import vn.spring.zebra.mining.gsp.Sequence;
import vn.spring.zebra.mining.gsp.SequenceDatabase;
import vn.spring.zebra.util.MathUtil;


/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class LearningPathPane extends JPanel implements CourseAccessSequences2ChangeListener, UIDispose {
	private static final long serialVersionUID = 1L;

	protected CourseAccessSequences2 sequences = null;
	protected LearningPathGraph graph = null;
	protected JTextArea result = null; 
	
	public LearningPathPane(CourseAccessSequences2 sequences) {
		super();
		this.sequences = sequences;
		this.sequences.addChangeListener(this);
		TitledBorder title = null;
		
		title = BorderFactory.createTitledBorder("COURSE LEARNING PATH");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		graph = new LearningPathGraph(sequences);
		JPanel graphPane = new JPanel();
		graphPane.setBorder(title);
		graphPane.setLayout(new BorderLayout());
		graphPane.add(graph, BorderLayout.CENTER);
		
		title = BorderFactory.createTitledBorder("COURSE SEQUENTIAL PATTERNS");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		result = new JTextArea(20, 16);
		result.setBorder(title);
		result.setEditable(false);
		result.setAutoscrolls(true);
		
		JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainPane.setDividerLocation(250);
		mainPane.add(new JScrollPane(graphPane));
		mainPane.add(new JScrollPane(result));

		setLayout(new BorderLayout());
		add(mainPane, BorderLayout.CENTER);
		
		update();
	}
	
	public void dispose() {
		try {sequences.removeChangeListener(this);}
		catch(Exception e) {System.out.println("LearningPathPane.dispose causes error: " + e.getMessage());}
		
		try {graph.dispose();}
		catch(Exception e) {System.out.println("LearningPathPane.dispose causes error: " + e.getMessage());}
	}
	public void courseAccessChanged(CourseAccessSequences2ChangeEvent evt) {
		update();
	}
	
	private void update() {
		result.setText("");
		ArrayList<Sequence> patterns = sequences.getSequentialPatterns();
		SequenceDatabase db = sequences.getDatabase();
		if(db != null && db.size() > 0) {
			double n = db.size();
			for(Sequence pattern : patterns) {
				double sup = MathUtil.round((double)pattern.getSupport() / n, 2);
				result.append(pattern.toString(sequences.getDatabase().getAttributes()) + 
						": support = " + sup + "\n\n");
			}
		}
		result.setCaretPosition(0);

		graph.update();
	}
	
}
