package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.server.TriUMServerUI;
import vn.spring.zebra.server.TriUMServerUIHelper;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.util.CommonUtil;

import vn.spring.bayes.InterchangeFormat.IFException;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMPane extends JPanel implements UIDispose {
	private static final long serialVersionUID = 1L;
	
	protected TriUM triUM = null;
	protected JFrame frmParent = null;
	
	protected JTabbedPane mainTab = null;
	protected KnowledgePane knowledgePane = null;
	protected LearningStylePane lsPane = null;
	protected LearningHistoryPane hisPane = null;
	
	public LearningHistoryPane getHisPane() {return hisPane;}
	public void selectKnowledgePane() {mainTab.setSelectedIndex(0);}
	public void selectLearningStylePane() {mainTab.setSelectedIndex(1);}
	public void selectLearningHistoryPane() {mainTab.setSelectedIndex(2);}
	
	public TriUMPane(TriUM triUM, JFrame frmParent) throws IOException, ZebraException, IFException, Exception {
		super();
		this.triUM = triUM;
		this.frmParent = frmParent;
		
		String userid = triUM.getUserId();
		String course = triUM.getCourse();
		knowledgePane = new KnowledgePane(userid, course,
				triUM.getStaticKnowledge(), 
				triUM.getDynKnowledge(),
				frmParent);
		lsPane = new LearningStylePane(triUM.getLearningStyleModel());
		hisPane = new LearningHistoryPane(triUM);

		mainTab = new JTabbedPane();
		mainTab.addTab("Knowledge", new JScrollPane(knowledgePane));
		mainTab.addTab("Learning Style", new JScrollPane(lsPane));
		mainTab.addTab("Learning History", hisPane);
		
		setLayout(new BorderLayout());
		add(mainTab, BorderLayout.CENTER);
	}
	
	public void dispose() {
		try {knowledgePane.dispose();}
		catch(Exception e) {System.out.println("TriUMPane.dispose call KnowledgePane.dispose causes error: " + e.getMessage()); e.printStackTrace();}
		
		try {lsPane.dispose();}
		catch(Exception e) {System.out.println("TriUMPane.dispose call LearningStylePane.dispose causes error: " + e.getMessage()); e.printStackTrace();}

		try {hisPane.dispose();}
		catch(Exception e) {System.out.println("TriUMPane.dispose call LearningHistoryPane.dispose causes error: " + e.getMessage()); e.printStackTrace();}
	}
	
	
	public TriUM getTriUM() {return triUM;}
	
    public static JPopupMenu createContextMenu(final TriUM um, final TriUMServerUI serverUI) {
		JPopupMenu contextMenu = new JPopupMenu();
		String     userid = um.getUserId();
		String     course = um.getCourse();
		
		JMenuItem openTLM = CommonUtil.makeMenuItem(null, "Open TLM", 
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
    				showTriUMDlg(serverUI, um);
				}
			});
		contextMenu.add(openTLM);
		contextMenu.addSeparator();

		JMenuItem unmonitorUser = CommonUtil.makeMenuItem(null, "Unmonitor User", 
				TriUMServerUIHelper.createUnmonitorUserActionListener(serverUI, userid, course));
		contextMenu.add(unmonitorUser);
		contextMenu.addSeparator();
		
		JMenuItem docclass = CommonUtil.makeMenuItem(null, "Interest/DocClass", 
				TriUMServerUIHelper.createDocClassActionListener(serverUI, userid, course));
		contextMenu.add(docclass);
		contextMenu.addSeparator();

		JMenuItem userReport = CommonUtil.makeMenuItem(null, "User Report", 
				TriUMServerUIHelper.createUserReportActionListener(serverUI, userid, course));
		contextMenu.add(userReport);
		JMenuItem feedbackReport = CommonUtil.makeMenuItem(null, "Feedback Report", 
				TriUMServerUIHelper.createUserFeedbackReportActionListener(serverUI, userid, course));
		contextMenu.add(feedbackReport);
		
    	return contextMenu;
    }
    public static void showTriUMDlg(JFrame parent, TriUM um) {
		try {
			TriUMDlg dlg = new TriUMDlg(parent, um);
			dlg.dispose();
			dlg = null; 
		}
		catch(Exception ex) {ex.printStackTrace();}
		
		CommonUtil.gc();
    }
}
