/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import vn.spring.WOW.engine.ConceptInfo;
import vn.spring.zebra.mining.AssociationRule;
import vn.spring.zebra.mining.CourseAccessApriori;
import vn.spring.zebra.mining.CourseAccessAprioriChangeEvent;
import vn.spring.zebra.mining.CourseAccessAprioriChangeListener;
import vn.spring.zebra.util.ConceptUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseRecommenderPane extends JPanel implements ListSelectionListener, CourseAccessAprioriChangeListener, UIDispose {
	private static final long serialVersionUID = 1L;
	
	protected String course = null;
	
	protected CourseAccessApriori apriori = null;
	protected JList aprioriList = null;
	protected JList cList = null;
	protected JList preList = null;
	protected JList postList = null;
	protected JTextArea arffText = null; 
	
	public CourseRecommenderPane(CourseAccessApriori apriori) throws Exception {
		super();
		TitledBorder title = null;
		
		title = BorderFactory.createTitledBorder("RECOMMENDATION RULES");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		aprioriList = new JList();
		aprioriList.setAutoscrolls(true);
		aprioriList.setBorder(title);
		
		title = BorderFactory.createTitledBorder("LIST OF CONCEPTS");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		cList = new JList();
		cList.setAutoscrolls(true);
		cList.setBorder(title);
		cList.addListSelectionListener(this);
		
		title = BorderFactory.createTitledBorder("PRE-RECOMMENDED CONCEPTS");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		preList = new JList();
		preList.setAutoscrolls(true);
		preList.setBorder(title);
		preList.setEnabled(false);
		
		title = BorderFactory.createTitledBorder("POST-RECOMMENDED CONCEPTS");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		postList = new JList();
		postList.setAutoscrolls(true);
		postList.setBorder(title);
		postList.setEnabled(false);

		JPanel aprioriPanel = new JPanel(); 
		aprioriPanel.setLayout(new BorderLayout(10, 10));
		aprioriPanel.add(aprioriList, BorderLayout.NORTH);
		//
		JPanel recommendPanel = new JPanel();
		recommendPanel.setLayout(new GridLayout(1, 0));
		recommendPanel.add(preList);
		recommendPanel.add(cList);
		recommendPanel.add(postList);
		aprioriPanel.add(recommendPanel, BorderLayout.CENTER);
		
		title = BorderFactory.createTitledBorder("ARFF DATA");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		arffText = new JTextArea();
		arffText.setAutoscrolls(true);
		arffText.setEditable(false);
		arffText.setBorder(title);
		JPanel arffPanel = new JPanel();
		//arffPanel.add(arffText);

		setLayout(new BorderLayout());
		add(aprioriPanel, BorderLayout.CENTER);
		add(arffPanel, BorderLayout.SOUTH);

		this.apriori = apriori;
		this.course = apriori.getCourse();
		update(apriori.getFiltered(), apriori.getArff(), apriori.getAssociationRules());
		apriori.addChangeListener(this);
	
	}
	public void dispose() {
		try {apriori.removeChangeListener(this);}
		catch(Exception e) {System.out.println("CourseRecommenderPane.dispose causes error: " + e.getMessage());}
	}
	
	protected void update(ArrayList<ConceptInfo> filtered, String arff, ArrayList<AssociationRule> rules) throws Exception {
		aprioriList.setListData(rules.toArray());
		
		ArrayList<ConceptInfo> cInfos = 
			( apriori.getFiltered().size() == 0? ConceptUtil.getConceptInfos(course) : filtered );
		cList.setListData(cInfos.toArray());
		
		arffText.setText(arff);
	}
	
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource() == cList ) {
			ConceptInfo cInfo = (ConceptInfo)cList.getSelectedValue();
			if(cInfo == null) return;
			preList.setListData(
					apriori.getPreRecommendedConcepts(cInfo.getConceptName(), 0).toArray()
				);
			postList.setListData(
					apriori.getPostRecommendedConcepts(cInfo.getConceptName(), 0).toArray()
				);
		}
	}
	public synchronized void courseAccessChanged(CourseAccessAprioriChangeEvent evt) {
		// TODO Auto-generated method stub
		try {
			update(evt.getFiltered(), evt.getArff(), evt.getAssociationRules());
		}
		catch(Exception e) {
			System.out.println("CourseRecommenderPane.courseAccessChanged causes error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
