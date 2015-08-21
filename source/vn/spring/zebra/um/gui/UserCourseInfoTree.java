/**
 * 
 */
package vn.spring.zebra.um.gui;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import vn.spring.WOW.engine.PNode;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.server.TriUMServer;
import vn.spring.zebra.um.UserConceptInfo;
import vn.spring.zebra.um.UserCourseInfo;
import vn.spring.zebra.um.UserInfo;
import vn.spring.zebra.util.ConceptUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserCourseInfoTree extends JTree {
	private static final long serialVersionUID = 1L;
	protected UserCourseInfo userCourseInfo = null;
	
	public UserCourseInfoTree() {
		super();
	}
	public UserCourseInfoTree(String userid, String course) throws Exception {
		super();
		load(userid, course);
	}
	public DefaultMutableTreeNode getRoot() {
		return (DefaultMutableTreeNode)getModel().getRoot();
	}
	public UserConceptInfo getRootUserConceptInfo() {
		return (UserConceptInfo)getRoot().getUserObject();
	}
	public void load(String userid, String course) throws Exception {
		userCourseInfo = new UserCourseInfo(userid, course);
		DefaultTreeModel model = createModel();
		setModel(model);
	}
	public void expandAll() {
		expandAll(getRoot());
	}
	
	private DefaultTreeModel createModel() throws Exception {
		PNode rootConceptNode = new PNode(
				userCourseInfo.getCourseInfo().hierarchy.getRootNode().getFirstChild(), 
				userCourseInfo.getUserInfo().getProfile());//root name = "javatutorial.javatutorial"
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(
			createUserConceptInfo(userCourseInfo.getUserInfo(), rootConceptNode.getName()));
		completeConceptNode(userCourseInfo.getUserInfo(), rootConceptNode, root);
		DefaultTreeModel model = new DefaultTreeModel(root);
		return model;
	}
	//note: parentConceptNode correspondings to parentTreeNode
	private void completeConceptNode(UserInfo userInfo, PNode parentConceptNode, DefaultMutableTreeNode parentTreeNode) throws Exception {
        PNode childConceptNode = parentConceptNode.getFirstChild();
        while (childConceptNode != null) {
        	TriUMQuery query = TriUMServer.getInstance().getCommunicateService().getQueryDelegator();
        	boolean exist = query.doesExist(
        		userInfo.getUserId(), 
        		userCourseInfo.getCourse(), 
        		ConceptUtil.getBriefConceptName(userCourseInfo.getCourse(),  childConceptNode.getName()), 
        		ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, 
        		false);
        	
        	if(exist) {
	    		DefaultMutableTreeNode childTreeNode = 
	    			new DefaultMutableTreeNode(createUserConceptInfo(userInfo, childConceptNode.getName()));
	    		parentTreeNode.add(childTreeNode);
	    		completeConceptNode(userInfo, childConceptNode, childTreeNode);
        	}
    		childConceptNode = childConceptNode.getNextSib();
        }
	}
	private UserConceptInfo createUserConceptInfo(UserInfo userInfo, String conceptName) throws Exception {
		UserConceptInfo userConceptInfo = new UserConceptInfo(userInfo, conceptName);
		userConceptInfo.isShowTitle = true;
		userConceptInfo.isShowMastered = true;
		return userConceptInfo;
	}
	private void expandAll(DefaultMutableTreeNode parent) {
		if(parent == null) return;
		if(parent.isLeaf()) {
			expandPath(new TreePath(parent.getPath()));
			return;
		}
		DefaultMutableTreeNode child = (DefaultMutableTreeNode)parent.getFirstChild();
		while(child != null) {
			expandAll(child);
			child = child.getNextSibling();
		}
	}
	
}
