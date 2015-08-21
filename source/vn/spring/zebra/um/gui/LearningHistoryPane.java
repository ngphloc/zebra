/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import vn.spring.zebra.um.TriUM;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class LearningHistoryPane extends JPanel implements UIDispose {
	private static final long serialVersionUID = 1L;

	protected JTabbedPane mainTab = null;

	protected LearningHistoryDataPane dataPane = null;
	protected CourseRecommenderPane courseRecPane = null;
	protected LearningPathPane pathPane = null;

	public LearningHistoryPane(TriUM triUM) throws Exception {
		super();
		setLayout(new BorderLayout());
		mainTab = new JTabbedPane();
		add(mainTab, BorderLayout.CENTER);
		
		dataPane = new LearningHistoryDataPane(triUM.getUserId(), triUM.getHisData());
		mainTab.addTab("History Data", dataPane);
		
		courseRecPane = new CourseRecommenderPane(triUM.getRecommender());
		mainTab.addTab("Course Recommender", new JScrollPane(courseRecPane));
		
		pathPane = new LearningPathPane(triUM.getSequence());
		mainTab.addTab("Learning Path", pathPane);
	}
	public void dispose() {
		try{dataPane.dispose();}
		catch(Exception e) {System.out.println("LearningHistoryPane.dispose call LearningHistoryDataPane.dispose() causes error " + e.getMessage());}

		try{courseRecPane.dispose();}
		catch(Exception e) {System.out.println("LearningHistoryPane.dispose call CourseRecommenderPane.dispose causes error " + e.getMessage());}

		try{pathPane.dispose();}
		catch(Exception e) {System.out.println("LearningHistoryPane.dispose call LearningPathPane.dispose causes error " + e.getMessage());}
	}
	public LearningHistoryDataPane getDataPane() {return dataPane;}
	public CourseRecommenderPane getCourseRecommenderPane() {return courseRecPane;}
	public LearningPathPane getLearningPathPane() {return pathPane;}

	public void selectDataPane() {mainTab.setSelectedIndex(0);}
	public void selectCourseRecommenderPane() {mainTab.setSelectedIndex(1);}
	public void selectLearningPathPane() {mainTab.setSelectedIndex(2);}
}
