package vn.spring.zebra.server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import vn.spring.WOW.datacomponents.DotString;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.CourseInfo;
import vn.spring.WOW.engine.Hierarchy;
import vn.spring.WOW.engine.PNode;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.StudyTime;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.evaluation.TestEvalChartUtil;
import vn.spring.zebra.um.PersonalInfo;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.um.UserConceptInfo;
import vn.spring.zebra.um.UserInfo;
import vn.spring.zebra.um.UserInfos;
import vn.spring.zebra.um.gui.TriUMContainerHelper;
import vn.spring.zebra.um.gui.UIDispose;
import vn.spring.zebra.um.gui.UserAttributePane;
import vn.spring.zebra.um.gui.UserCourseInputDlg;
import vn.spring.zebra.um.gui.ZebraTOCPane2;
import vn.spring.zebra.util.CommonUtil;
import vn.spring.zebra.util.ConceptUtil;
import vn.spring.zebra.util.MathUtil;
import vn.spring.zebra.util.UserUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServerUIUserPane extends JPanel implements TreeSelectionListener, UIDispose {
	private static final long serialVersionUID = 1L;
	protected TriUMServerUI serverUI = null;
	
	protected JTree userTree = null;
	protected AuxPane auxPane = null;
	protected UserAttributePane attrPane = null;
	protected DefaultMutableTreeNode root = null; 
	protected HashSet<TriUMServerUserPaneSelectionListener> listeners = new HashSet<TriUMServerUserPaneSelectionListener>();
	
	public JTree getUsersTree() {return userTree;}
	public DefaultMutableTreeNode getRoot() {return root;}
	
	public TriUMServerUIUserPane(final TriUMServerUI serverUI) throws Exception {
		super();
		this.serverUI = serverUI;
		this.addSelectionListener(serverUI);
		
		root = new DefaultMutableTreeNode();
		userTree = new JTree(root);
		ToolTipManager.sharedInstance().registerComponent(userTree);
		userTree.setCellRenderer(new UsersTreeRenderer());
		userTree.addTreeSelectionListener(this);
		userTree.setToolTipText("Right-click on items for context menu.\n Maybe press keys: F5, Delete...");
		userTree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				clickMouse(e);
			}
		});
		userTree.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				pressKey(e);
			}
		});
		
		JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainPane.setDividerLocation(350);
		//
		JPanel treePanel = new JPanel(); treePanel.setLayout(new BorderLayout());
		//
		JPanel treeToolbar = new JPanel();
		treeToolbar.setLayout(new BoxLayout(treeToolbar, BoxLayout.PAGE_AXIS));
		TriUMServerFinderGUI finderGUI = new TriUMServerFinderGUI(this);
		treeToolbar.add(finderGUI);
		JTextField tooltip = new JTextField(8);
		tooltip.setText("Right-click on items for context menu. Maybe press keys: F5, Delete...");
		tooltip.setCaretPosition(0);
		tooltip.setEditable(false);
		treeToolbar.add(tooltip);
		//
		treePanel.add(treeToolbar, BorderLayout.NORTH);
		treePanel.add(new JScrollPane(userTree), BorderLayout.CENTER);
		//
		mainPane.add(treePanel, JSplitPane.TOP);
		
		JPanel infoPane= new JPanel(); infoPane.setLayout(new BorderLayout());
		auxPane = new AuxPane();
		infoPane.add(auxPane, BorderLayout.NORTH);
		attrPane = new UserAttributePane();
		infoPane.add(attrPane, BorderLayout.CENTER);
		mainPane.add(new JScrollPane(infoPane), JSplitPane.BOTTOM);

		setLayout(new BorderLayout());
		add(mainPane, BorderLayout.CENTER);
		
		update();
	}
	public void outsideSelectUser(String userid, String course) {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) userTree.getLastSelectedPathComponent();
		if(selectedNode != null) {
			String[] sUserIdCourse = getUserAndCourseOf(selectedNode);
			if(sUserIdCourse != null) {
				if(sUserIdCourse[0].equals(userid) && sUserIdCourse[1].equals(course)) {
					return;
				}
			}
		}
		DefaultMutableTreeNode treeFound = findNode(userid, course);
		if(treeFound == null) treeFound = findNode(userid);
		if(treeFound == null) return;
		selectNode(treeFound);
	}
	public boolean isIn(DefaultMutableTreeNode node, String userid, String course) {
		DefaultMutableTreeNode found = findNode(userid, course);
		if(found == null) return false;
		
		TreeNode[] path = node.getPath();
		for(int i = 0; i < path.length; i++) {
			if(path[i] == node) return true;
		}
		return false;
	}
	public void addUser(String userid) throws Exception {
		if(findNode(userid) != null) System.out.println("User " + userid + " existed");
		UserInfo userInfo = new UserInfo(userid);
		DefaultMutableTreeNode userNode = createUserNode(userInfo);
		
		int insertIdx = findRightPositionOfUser(userInfo);
		((DefaultTreeModel)userTree.getModel()).insertNodeInto(userNode, root, insertIdx);
		((UserInfos)root.getUserObject()).insertUser(userInfo, insertIdx);
		
		updateSelectedUser();
	}
	private int findRightPositionOfUser(UserInfo userInfo) {
		int insertIdx = root.getChildCount();
		for(int i = 0; i < root.getChildCount(); i++) {
			UserInfo tempUserInfo = (UserInfo)(((DefaultMutableTreeNode)root.getChildAt(i)).getUserObject());
			if(tempUserInfo.toString().compareTo(userInfo.toString()) > 0) {
				insertIdx = i;
				break;
			}
		}
		return insertIdx;
	}
	public void removeUser(String userid) {
		DefaultMutableTreeNode found = findNode(userid);
		if(found == null) System.out.println("User \"" + userid + "\" not existed");
		DefaultTreeModel treeModel = (DefaultTreeModel)userTree.getModel();
		treeModel.removeNodeFromParent(found);
		((UserInfos)root.getUserObject()).removeUser(userid);
		
		updateSelectedUser();
	}
	public void rearrangeUser(String userid) {
		DefaultMutableTreeNode found = findNode(userid);
		if(found == null) System.out.println("User \"" + userid + "\" not existed");
		UserInfo userInfo = ((UserInfo)found.getUserObject()); 
		try {
			userInfo.updateInfo();
		}
		catch(Exception e) {e.printStackTrace();}

		//Remove current position
		DefaultTreeModel treeModel = (DefaultTreeModel)userTree.getModel();
		treeModel.removeNodeFromParent(found);
		
		//Add other position
		int insertIdx = findRightPositionOfUser(userInfo);
		treeModel.insertNodeInto(found, root, insertIdx);

		updateSelectedUser();
	}
	
	//Tree selection changed
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) userTree.getLastSelectedPathComponent();
		if(selectedNode == null) return;
		
		Object object = selectedNode.getUserObject();
		if(object instanceof UserInfos) {
			updateUserInfos((UserInfos)object);
		}
		else if(object instanceof UserInfo) {
			updateUserInfo((UserInfo)object);
		}
		else if(object instanceof PersonalInfo) {
			PersonalInfo personInfo = (PersonalInfo)object;
			
			try {personInfo.updateInfo(); attrPane.load(personInfo); auxPane.clear();}
			catch(Exception ex) {ex.printStackTrace();}
		}
		else if(object instanceof UserConceptInfo) {
			UserConceptInfo userConceptInfo = (UserConceptInfo)object;
			updateUserConceptInfo(userConceptInfo);
		}
		else {
			try {attrPane.clear(); auxPane.clear();}
			catch(Exception ex) {ex.printStackTrace();}
		}
		
		String[] sUserIdCourse = getUserAndCourseOf(selectedNode);
		if(sUserIdCourse != null) {
			String userid = sUserIdCourse[0];
			String course = sUserIdCourse[1];
			
			fireSelectionListeners(new TriUMServerUserPaneSelectionEvent(this, userid, course));
		}
		else {
			String userid = getUserOf(selectedNode);
			fireSelectionListeners(new TriUMServerUserPaneSelectionEvent(this, userid, null));
		}
	}
	
	public void dispose() {removeAllSelectionListener(); attrPane.dispose();}
	
	public void update() throws Exception {
		root.removeAllChildren();
		ArrayList<Profile> profiles = UserUtil.getAllProfiles(ZebraStatic.IGNORE_ANONYMOUS);
		UserInfos userInfos = new UserInfos(profiles);
		root.setUserObject(userInfos);
		DefaultTreeModel treeModel = ((DefaultTreeModel)userTree.getModel());
		treeModel.reload();
		for(UserInfo userInfo : userInfos.getUsers()) {
			DefaultMutableTreeNode userNode = createUserNode(userInfo);
			treeModel.insertNodeInto(userNode, root, root.getChildCount());
		}
		userTree.expandPath(new TreePath(root));

		auxPane.clear();
		attrPane.clear();
	}
	public void updateGUI() {
		updateSelectedUser();
		userTree.repaint();
	}
	private void updateSelectedUser() {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) userTree.getLastSelectedPathComponent();
		if(selectedNode == null) {attrPane.clear(); return;}
		Object object = selectedNode.getUserObject();
		
		if(object instanceof UserInfos) {
			updateUserInfos((UserInfos)object);
		}
		else if(object instanceof UserInfo) {
			updateUserInfo((UserInfo)object);
		}
		else if(object instanceof UserConceptInfo) {
			UserConceptInfo userConceptInfo = (UserConceptInfo)object;
			updateUserConceptInfo(userConceptInfo);
		}
	}
	private void updateUserInfo(UserInfo userInfo) {
		auxPane.clear();
		
		try {userInfo.updateInfo();}
		catch(Exception e) {e.printStackTrace();}
		
		String userid = userInfo.getUserId();
		String username = userInfo.getUserName();
		String monitored = "";
		String info = "";
		TriUMContainerHelper helper = new TriUMContainerHelper(serverUI.getMonitoredUserList());
		ArrayList<TriUM> ums = helper.getUMs(userid);
		if(ums.size() == 0) {
			info = "User hasn't monitored yet";
			monitored = "<<not yet>>";
		}
		else {
			info = "User has monitored in " + ums.size() + " course(s)";
			for(int i = 0; i < ums.size(); i++) {
				monitored += ums.get(i).getCourse();
				if(i < ums.size() - 1) monitored += ", ";
			}
		}
		
		String[][] data = new String[][] {{"User ID", userid}, {"User Name", username}, 
				{"Monitored in course(s)", monitored}};
		attrPane.load( data, new String[]{"Attribute", "Value"} );
		attrPane.setInfo(info);
	}
	private void updateUserInfos(UserInfos userInfos) {
		auxPane.clear();

		TriUMContainerHelper helper = new TriUMContainerHelper(serverUI.getMonitoredUserList());
		ArrayList<UserInfo> users = userInfos.getUsers();
		int n = users.size();
		String[][] data = new String[n][];
		int count = 0;
		for(int i = 0; i < n; i++) {
			UserInfo user = users.get(i);
			String[] row = new String[2];
			row[0] = user.getUserName();
			if(helper.isMonitored(user.getUserId())) {
				row[1] = "Already";
				count++;
			}
			else
				row[1] = "Not Yet";
			data[i] = row;
		}
		attrPane.load(data, new String[] {"User Name", "Monitored"});
		
		attrPane.setInfo("There are " + count + "/" + n + " users monitored");
	}
	
	private void updateUserConceptInfo(UserConceptInfo userConceptInfo) {
		try {
			userConceptInfo.updateInfo(); 
			attrPane.load(userConceptInfo);
			
			auxPane.clear();
			DotString ds = new DotString(userConceptInfo.getConcept().getName());
			String userid = userConceptInfo.getUserInfo().getUserId(); 
			String course = ds.get(0);
			String briefConcept = userConceptInfo.getBriefName();
			TriUM um = TriUMServer.getInstance().getUM(userid, course, false);
			if(um == null) return;
			
			TriUMQuery query = (TriUMQuery)TriUMServer.getInstance().
				getCommunicateService().getQueryDelegator();
			if(query != null) {
				try {
					StudyTime studyTime = query.studyTimeQuery(userid, course, briefConcept);
		    		double conceptMinute = MathUtil.round((double)studyTime.conceptInterval / 60000.0, 2);
		    		double courseMinute = MathUtil.round((double)studyTime.courseInterval / 60000.0, 2);
		    		double totalMinute = MathUtil.round((double)studyTime.totalInterval / 60000.0, 2);
					auxPane.text.setText("Study Time: " + 
							conceptMinute + " / " + 
							courseMinute + " / " +
							totalMinute + "(minute)");
				}
				catch(Exception e) {e.printStackTrace();}
			}
			
			JFreeChart chart = TestEvalChartUtil.getEvalChartOfKnowledge(
					userid, 
					course, 
					briefConcept, 
					ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE);
			if(chart != null) {
				byte[] chartBytes = ChartUtilities.encodeAsPNG(chart.createBufferedImage(230, 150));
				ImageIcon chartIcon = new ImageIcon(chartBytes);
				auxPane.label.setIcon(chartIcon);
			}
		}
		catch(Exception ex) {ex.printStackTrace();}
	}
	
	private class AuxPane extends JPanel {
		private static final long serialVersionUID = 1L;
		public JTextField text = null;
		public JLabel     label = null;
		public AuxPane() {
			super();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			text = new JTextField(8);
			text.setEditable(false);
			add(text);
			
			label = new JLabel();
			add(label);
		}
		public void clear() {
			text.setText("");
			label.setText("");
			label.setIcon(null);
		}
	}

	protected void selectNode(DefaultMutableTreeNode node) {
		TreePath path = new TreePath(node.getPath());
		if(!userTree.isPathSelected(path)) {
			userTree.collapsePath(userTree.getSelectionPath());
			userTree.expandPath(path);
			userTree.setSelectionPath(path);
			userTree.scrollPathToVisible(path);
		}
	}
	private DefaultMutableTreeNode findNode(String userid, String course) {
		int m = root.getChildCount();
		for(int i = 0; i < m; i++) {
			DefaultMutableTreeNode userNode = (DefaultMutableTreeNode)root.getChildAt(i);
			UserInfo userInfo = (UserInfo)userNode.getUserObject();
			if(!userInfo.getUserId().equals(userid)) continue;
			
			int n = userNode.getChildCount();
			for(int j = 0; j < n; j++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)userNode.getChildAt(j);
				Object object = node.getUserObject();
				if(!(object instanceof UserConceptInfo)) continue;
				
				UserConceptInfo userConceptInfo = (UserConceptInfo)object;
				if(userConceptInfo.getBriefName().equals(course)) return node;
			}
		}
		return null;
	}
	private DefaultMutableTreeNode findNode(String userid) {
		int m = root.getChildCount();
		for(int i = 0; i < m; i++) {
			DefaultMutableTreeNode userNode = (DefaultMutableTreeNode)root.getChildAt(i);
			UserInfo userInfo = (UserInfo)userNode.getUserObject();
			if(userInfo.getUserId().equals(userid)) return userNode;
		}
		return null;
	}
	private String[] getUserAndCourseOf(DefaultMutableTreeNode node) {
		return getUserAndCourseOf(node.getPath());
	}
	private String getUserOf(DefaultMutableTreeNode node) {
		return getUserOf(node.getPath());
	}
	private String[] getUserAndCourseOf(TreeNode[] path) {
		if(path == null) return null;
		String userid = null;
		String course = null;
		for(int i = 0; i < path.length; i++) {
			if(!(path[i] instanceof DefaultMutableTreeNode)) continue;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)(path[i]); 
			Object object = node.getUserObject();
			if(object instanceof UserInfo) {
				userid = ((UserInfo)(object)).getUserId();
			}
			else if(object instanceof UserConceptInfo) {
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)(node.getParent());
				if(parentNode.getUserObject() instanceof UserInfo) {
					course = ((UserConceptInfo)(object)).getCourse();
				}
			}
			
			if(userid != null && course != null) break;
		}
		if(userid != null && course != null) return new String[] {userid, course};
		return null;
	}
	private String getUserOf(TreeNode[] path) {
		if(path == null) return null;
		for(int i = 0; i < path.length; i++) {
			if(!(path[i] instanceof DefaultMutableTreeNode)) continue;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)(path[i]); 
			Object object = node.getUserObject();
			if(object instanceof UserInfo) {
				return ((UserInfo)(object)).getUserId();
			}
			else if(object instanceof UserConceptInfo) {
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)(node.getParent());
				if(parentNode.getUserObject() instanceof UserInfo) {
					return ((UserConceptInfo)(object)).getUserInfo().getUserId();
				}
			}
		}
		return null;
	}

	private void fireSelectionListeners(TriUMServerUserPaneSelectionEvent evt) {
		System.out.println("TriUMServerUIUserPane fires selection event");
		for(TriUMServerUserPaneSelectionListener listener : listeners) {
			final TriUMServerUserPaneSelectionListener final_listener = listener;
			final TriUMServerUserPaneSelectionEvent final_evt = evt;
			final_listener.userPaneSelectionChanged(final_evt);
		}
	}
	private void addSelectionListener(TriUMServerUserPaneSelectionListener listener) {
		listeners.add(listener);
	}
	private void removeAllSelectionListener() {
		listeners.clear();
	}
	
	private DefaultMutableTreeNode createUserNode(UserInfo userInfo) throws Exception {
		DefaultMutableTreeNode userInfoNode = new DefaultMutableTreeNode(userInfo);
		PersonalInfo personalInfo = new PersonalInfo(userInfo);
		
		DefaultMutableTreeNode personalInfoNode = new DefaultMutableTreeNode(personalInfo);
//		ArrayList<String> personalStrList = personalInfo.getSortedBriefInfoList();
//		for(String personalStr : personalStrList) {
//			if(personalStr.indexOf("password") == 0) personalStr = "password=***";
//			personalInfoNode.add(new DefaultMutableTreeNode(personalStr));
//		}
		userInfoNode.add(personalInfoNode);
		
		TreeSet<String> courses = userInfo.getSortedCourseNameList();
		for(String course : courses) {
			Hierarchy hierarchy = null; 
			try { 
				CourseInfo courseInfo = ZebraStatic.getCourseInfoTbl().getCourseInfo(course);
				if(courseInfo != null)
					hierarchy = courseInfo.hierarchy;
				else
					hierarchy = ConceptUtil.getCourseHierarchy(course);
			}
			catch(Exception e) {
				System.out.println("CommonUtil.getCourseInfo in UsersPane.createUserNode causes error: " + e.getMessage());
				e.printStackTrace();
				hierarchy = null;
			}
			if(hierarchy == null || hierarchy.getRootNode().getFirstChild() == null) continue;
			
			
			PNode topLevelConceptNode = new PNode(
					hierarchy.getRootNode().getFirstChild(), userInfo.getProfile());//root name = "javatutorial.javatutorial"
			//topLevelConceptNode is javatutorial.javatutorial
			DefaultMutableTreeNode topLevelTreeNode = new DefaultMutableTreeNode(
					new UserConceptInfo(userInfo, topLevelConceptNode.getName()));
			userInfoNode.add(topLevelTreeNode);
			
			completeConceptNode(userInfo, topLevelConceptNode, topLevelTreeNode);
		}
		
		return userInfoNode;
	}
	
	//note: parentConceptNode correspondings to parentTreeNode
	private void completeConceptNode(UserInfo parentUserInfo, PNode parentConceptNode, DefaultMutableTreeNode parentTreeNode) throws Exception {
        PNode childConceptNode = parentConceptNode.getFirstChild();
        while (childConceptNode != null) {
    		DefaultMutableTreeNode childTreeNode = 
    			new DefaultMutableTreeNode(new UserConceptInfo(parentUserInfo, childConceptNode.getName()));
    		parentTreeNode.add(childTreeNode);
    		completeConceptNode(parentUserInfo, childConceptNode, childTreeNode);
    		childConceptNode = childConceptNode.getNextSib();
        }
	}
	
	private void receiveDeleteAction() {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) userTree.getLastSelectedPathComponent();
		if(selectedNode == null) return;
		
		Object object = selectedNode.getUserObject();
		
		if(object instanceof UserInfos) {
			TriUMServerUIHelper.createUnregisterUserActionListener(serverUI, null).
				actionPerformed(new ActionEvent(this, 0, "Unregister"));			
		}
		else if(object instanceof UserInfo) {
			String userid = ((UserInfo)object).getUserId();
			TriUMServerUIHelper.createUnregisterUserActionListener(serverUI, userid).
				actionPerformed(new ActionEvent(this, 0, "Unregister"));			
		}
		else if(object instanceof UserConceptInfo) {
			UserConceptInfo userConcept = (UserConceptInfo)object;
			DotString ds = new DotString(userConcept.getConcept().getName());
			if(ds.size() != 2) return;
			String userid = userConcept.getUserInfo().getUserId(); 
			String course = ds.get(0);
			TriUMServerUIHelper.createUnmonitorUserActionListener(serverUI, 
				userid, course).actionPerformed(new ActionEvent(this, 0, "Unmonitor"));
		}
	}
	
	private void clickMouse(MouseEvent e) {
		if(!javax.swing.SwingUtilities.isRightMouseButton(e) ) return;
		TreePath path = userTree.getPathForLocation(e.getX(), e.getY());
		if(path == null) return;
		DefaultMutableTreeNode curNode = (DefaultMutableTreeNode) path.getLastPathComponent();
		if(curNode == null) return;
		JPopupMenu contextMenu = createContextMenu(curNode);
		if(contextMenu != null) contextMenu.show((Component)e.getSource(), e.getX(), e.getY());
	}
	
	private void pressKey(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_F5) {
			try {
				update();
			}
			catch(Exception ex) {ex.printStackTrace();}
		}
		else if(keyCode == KeyEvent.VK_DELETE) {
			receiveDeleteAction();
		}
		else if(keyCode == KeyEvent.VK_M && e.isAltDown() && e.isControlDown()) {
			if(!e.isShiftDown()) {
				TriUMServerUIHelper.createMonitorCourseActionListener(serverUI, null).
					actionPerformed(new ActionEvent(this, 0, "monitorcourse"));
			}
			else {
				TriUMServerUIHelper.createUnmonitorCourseActionListener(serverUI, null).
					actionPerformed(new ActionEvent(this, 0, "unmonitorcourse"));
			}
		}
		else if(keyCode == KeyEvent.VK_C && e.isAltDown() && e.isControlDown()) {
			if(!e.isShiftDown()) {
				TriUMServerUIHelper.createRegisterDefaultUsersActionListener(serverUI, false).
					actionPerformed(new ActionEvent(this, 0, "registerdefault"));
			}
			else {
				TriUMServerUIHelper.createUnregisterAllUsersActionListener(serverUI).
					actionPerformed(new ActionEvent(this, 0, "unregisterall"));
			}
		}
	}

	private JPopupMenu createContextMenu(DefaultMutableTreeNode curNode) {
		Object object = curNode.getUserObject();
		if(object == null) return null;
		
		JPopupMenu contextMenu = null;
		
		if(object instanceof UserInfos) {
			ArrayList<UserInfo> users = ((UserInfos)object).getUsers();
			ArrayList<String> userids = new ArrayList<String>();
			for(UserInfo user : users) userids.add(user.getUserId());
			
			contextMenu = new JPopupMenu();
			JMenuItem register = CommonUtil.makeMenuItem(null, "Register User", 
					TriUMServerUIHelper.createRegisterUserActionListener(serverUI));
			contextMenu.add(register);
			
			JMenuItem registerdefault = CommonUtil.makeMenuItem(null, "Register Default Users", 
					TriUMServerUIHelper.createRegisterDefaultUsersActionListener(serverUI, true));
			contextMenu.add(registerdefault);
			
			contextMenu.addSeparator();
			
			JMenuItem monitorCourse = CommonUtil.makeMenuItem(null, "Monitor Course", 
					TriUMServerUIHelper.createMonitorCourseActionListener(serverUI, null));
			contextMenu.add(monitorCourse);
			JMenuItem unmonitorCourse = CommonUtil.makeMenuItem(null, "Unmonitor Course", 
					TriUMServerUIHelper.createUnmonitorCourseActionListener(serverUI, null));
			contextMenu.add(unmonitorCourse);

			contextMenu.addSeparator();

			JMenuItem feedback = CommonUtil.makeMenuItem(null, "Feedback Report", 
					TriUMServerUIHelper.createUserFeedbackReportActionListener(serverUI, null, null));
			contextMenu.add(feedback);
			
			contextMenu.addSeparator();
			
			JMenu ml = new JMenu("Mailing List");
			contextMenu.add(ml);
			JMenuItem mlManager = CommonUtil.makeMenuItem(null, "Mailing List Manager", 
					TriUMServerUIHelper.createMLManagerActionListener(serverUI, null));
			ml.add(mlManager);
			ml.addSeparator();
			//
			JMenuItem mlReg = CommonUtil.makeMenuItem(null, "Add to Mailing List (ALL)", 
					TriUMServerUIHelper.createAllMLRegActionListener(serverUI, userids, null, true));
			ml.add(mlReg);
			JMenuItem mlUnreg = CommonUtil.makeMenuItem(null, "Remove from Mailing List (ALL)", 
					TriUMServerUIHelper.createAllMLRegActionListener(serverUI, userids, null, false));
			ml.add(mlUnreg);
			//
			ml.addSeparator();
			//
			JMenuItem mlSendMail = CommonUtil.makeMenuItem(null, "Send Mail (Mailing List)", 
				TriUMServerUIHelper.createMLSendMailActionListener(serverUI, null));
			ml.add(mlSendMail);
			//
			JMenuItem mlSendReport = CommonUtil.makeMenuItem(null, "Send Report (Mailing List)", 
				TriUMServerUIHelper.createMLSendReportActionListener(serverUI, null));
			ml.add(mlSendReport);

			contextMenu.addSeparator();

			JMenuItem mailsender = CommonUtil.makeMenuItem(null, "Send Mail", 
					TriUMServerUIHelper.createMailSenderActionListener(serverUI, userids));
			contextMenu.add(mailsender);

			contextMenu.addSeparator();

			JMenuItem unregisterall = CommonUtil.makeMenuItem(null, "Unregister All users", 
					TriUMServerUIHelper.createUnregisterAllUsersActionListener(serverUI));
			contextMenu.add(unregisterall);
		}
		else if(object instanceof UserInfo) {
			final String cur_userid = ((UserInfo)object).getUserId();
			contextMenu = new JPopupMenu();
			
			JMenuItem monitorUser = CommonUtil.makeMenuItem(null, "Monitor User", 
					TriUMServerUIHelper.createMonitorUserActionListener(serverUI, cur_userid, null));
			contextMenu.add(monitorUser);
			JMenuItem unmonitorUser = CommonUtil.makeMenuItem(null, "Unmonitor User", 
					TriUMServerUIHelper.createUnmonitorUserActionListener(serverUI, cur_userid, null));
			contextMenu.add(unmonitorUser);
			
			contextMenu.addSeparator();
			
			JMenuItem tocEval = CommonUtil.makeMenuItem(null, "TOC Evaluation", 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						UserCourseInputDlg input = new UserCourseInputDlg(
								serverUI, cur_userid, null, "Enter User ID and course");
						if(!input.isOK) return;
						String course = input.course;
						boolean isMonitored = (serverUI.getMonitoredUserList().
								getUM(cur_userid, course) == null ? false : true);
						if(!isMonitored) {
							JOptionPane.showMessageDialog(serverUI, "This user must be monitored before");
							return;
						}
						JDialog tocEvalDlg = new JDialog(serverUI, "TOC Evaluation", true);
						tocEvalDlg.add(new ZebraTOCPane2(cur_userid, course));
						tocEvalDlg.setSize(600, 400);
						tocEvalDlg.setVisible(true);
					}
				});
			contextMenu.add(tocEval);
			
			contextMenu.addSeparator();

			JMenuItem changeNameUser = CommonUtil.makeMenuItem(null, "Change User Name", 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						serverUI.changeUserName(cur_userid);
					}
			});
			contextMenu.add(changeNameUser);
			JMenuItem setPwdUser = CommonUtil.makeMenuItem(null, "Set User Password", 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						UserUtil.showSetUserPasswordDlg(serverUI, cur_userid);
					}
			});
			contextMenu.add(setPwdUser);
			
			contextMenu.addSeparator();

			JMenuItem docclass = CommonUtil.makeMenuItem(null, "Interest/DocClass", 
					TriUMServerUIHelper.createDocClassActionListener(serverUI, cur_userid, null));
			contextMenu.add(docclass);
			
			contextMenu.addSeparator();

			JMenuItem userReport = CommonUtil.makeMenuItem(null, "User Report", 
					TriUMServerUIHelper.createUserReportActionListener(serverUI, cur_userid, null));
			contextMenu.add(userReport);
			JMenuItem feedbackReport = CommonUtil.makeMenuItem(null, "Feedback Report", 
					TriUMServerUIHelper.createUserFeedbackReportActionListener(serverUI, cur_userid, null));
			contextMenu.add(feedbackReport);
			
			contextMenu.addSeparator();
			
			JMenu ml = new JMenu("Mailing List");
			contextMenu.add(ml);
			JMenuItem mlReg = CommonUtil.makeMenuItem(null, "Add to Mailing List", 
					TriUMServerUIHelper.createMLRegActionListener(serverUI, cur_userid, null, true));
			ml.add(mlReg);
			JMenuItem mlUnreg = CommonUtil.makeMenuItem(null, "Remove from Mailing List", 
					TriUMServerUIHelper.createMLRegActionListener(serverUI, cur_userid, null, false));
			ml.add(mlUnreg);
			
			contextMenu.addSeparator();

			ArrayList<String> userids = new ArrayList<String>();
			userids.add( ((UserInfo)object).getUserId() );
			JMenuItem mailsender = CommonUtil.makeMenuItem(null, "Send Mail", 
					TriUMServerUIHelper.createMailSenderActionListener(serverUI, userids));
			contextMenu.add(mailsender);
			
			contextMenu.addSeparator();
			
			JMenuItem unregisterUser = CommonUtil.makeMenuItem(null, "Unregister User", 
					TriUMServerUIHelper.createUnregisterUserActionListener(serverUI, cur_userid));
			contextMenu.add(unregisterUser);
		}
		else if(object instanceof PersonalInfo) {
			TreePath path = new TreePath(curNode.getPath());
			if(!userTree.isPathSelected(path)) return contextMenu;
			
			contextMenu = new JPopupMenu();
			JMenuItem add = CommonUtil.makeMenuItem(null, "Add New Personal Attribute", 
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						attrPane.addNewPersonalAttr();
					}
			});
			contextMenu.add(add);
		}
		else if(object instanceof UserConceptInfo) {
			UserConceptInfo userConcept = (UserConceptInfo)object;
			DotString ds = new DotString(userConcept.getConcept().getName());
			if(ds.size() != 2) return contextMenu;
			contextMenu = new JPopupMenu();
			
			String userid = userConcept.getUserInfo().getUserId(); 
			String course = ds.get(0);
			
			boolean isMonitored = (serverUI.getMonitoredUserList().getUM(userid, course) == null ? false : true);
			if(isMonitored) {
				JMenuItem unmonitorUser = CommonUtil.makeMenuItem(null, "Unmonitor User", 
						TriUMServerUIHelper.createUnmonitorUserActionListener(serverUI, userid, course));
				contextMenu.add(unmonitorUser);
				
			}
			else {
				JMenuItem monitorUser = CommonUtil.makeMenuItem(null, "Monitor User", 
						TriUMServerUIHelper.createMonitorUserActionListener(serverUI, userid, course));
				contextMenu.add(monitorUser);
			}
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
		}
		return contextMenu;
	}

	private class UsersTreeRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 1L;
		
		private ImageIcon monitoredIcon = null;
		private ImageIcon unmonitoredIcon = null;
		
        public UsersTreeRenderer() {
        	super();
            URL monitoredIconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "monitoredtreenode-24x24.gif");
            monitoredIcon = new ImageIcon(monitoredIconURL);

            URL unmonitoredIconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "unmonitoredtreenode-24x24.gif");
            unmonitoredIcon = new ImageIcon(unmonitoredIconURL);
        }

        public Component getTreeCellRendererComponent(
                            JTree tree,
                            Object value,
                            boolean sel,
                            boolean expanded,
                            boolean leaf,
                            int row,
                            boolean hasFocus) {

            super.getTreeCellRendererComponent(
                            tree, value, sel,
                            expanded, leaf, row,
                            hasFocus);

    		DefaultMutableTreeNode treenode = (DefaultMutableTreeNode)value;
    		
    		Object o = treenode.getUserObject();
    		if(o instanceof UserInfo) {
        		UserInfo userInfo = (UserInfo)o;
        		TriUMContainerHelper helper = new TriUMContainerHelper(serverUI.getMonitoredUserList());
        		if(helper.isMonitored(userInfo.getUserId())) {
        			setIcon(monitoredIcon);
        		}
        		else {
        			setIcon(unmonitoredIcon);
        		}
        	}
    		else if(o instanceof UserConceptInfo) {
    			UserConceptInfo uconcept = (UserConceptInfo)o;
    			if(uconcept.isRootConcept()) {
    				String course = uconcept.getBriefName();
    				if(serverUI.getMonitoredUserList().getUM(uconcept.getUserInfo().getUserId(), course) != null) {
            			setIcon(monitoredIcon);
            			setToolTipText("User \"" + uconcept.getUserInfo().getUserId() + "\" is being monitored in course \"" + course + "\"");
    				}
    				else {
            			setIcon(unmonitoredIcon);
            			setToolTipText("User \"" + uconcept.getUserInfo().getUserId() + "\" isn't monitored in course \"" + course + "\" yet");
    				}
    			}
    		}
    		
            return this;
        }
    }
}
