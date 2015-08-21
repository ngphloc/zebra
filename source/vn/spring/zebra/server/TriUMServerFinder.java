/**
 * 
 */
package vn.spring.zebra.server;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import vn.spring.zebra.um.PersonalInfo;
import vn.spring.zebra.um.UserConceptInfo;
import vn.spring.zebra.um.UserInfo;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
class TriUMServerFinder {
	public static int FIND_TYPE_USER = 1;
	public static int FIND_TYPE_PERSONALINFO = 2;
	public static int FIND_TYPE_CONCEPT = 4;
	
	protected DefaultMutableTreeNode root = null;
	protected int                    findType = 0;
	protected String                 query = null;
	protected ArrayList<DefaultMutableTreeNode> founds = new ArrayList<DefaultMutableTreeNode>();
	protected int                    start = -1;
	
	public TriUMServerFinder(DefaultMutableTreeNode root) {
		this.root = root;
	}
	
	public DefaultMutableTreeNode find(int findType, String query) {
		this.findType = findType;
		this.query = query;
		this.founds.clear();
		this.start = -1;
		
		if(this.query == null) return null;
		this.query = this.query.trim();
		if(this.query.length() == 0) return null;
		
		if(match(this.root, this.findType, this.query)) this.founds.add(this.root);
		findChildrenOf(this.root);
		if(this.founds.size() == 0) {
			return null;
		}
		else {
			this.start = 0;
			return this.founds.get(start);
		}
	}
	public DefaultMutableTreeNode findNext() {
		if(start == -1) return null;
		start ++;
		if(start >= founds.size()) {
			start = -1;
			founds.clear();
			return null;
		}
		return founds.get(start);
	}
	
	private void findChildrenOf(DefaultMutableTreeNode start) {
		if(start == null) return ;
		
		if(start.getChildCount() > 0) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)start.getFirstChild();
			while(child != null) {
				if(match(child, findType, query)) founds.add(child);
				findChildrenOf(child);
				child = child.getNextSibling();
			}
		}
	}
	
	protected boolean match(DefaultMutableTreeNode node, int findType, String query) {
		Object object = node.getUserObject();
		if(object instanceof UserInfo) {
			if((findType & FIND_TYPE_USER) != 0) {
				UserInfo userInfo = (UserInfo)object;
				String userid = userInfo.getUserId();
				String username = userInfo.getUserName();
				if(match(userid, query) || match(username, query)) return true;
			}
		}
		else if(object instanceof PersonalInfo) {
			if((findType & FIND_TYPE_PERSONALINFO) != 0) {
				PersonalInfo personalInfo = (PersonalInfo)object;
				ArrayList<String> excludeKeys = new ArrayList<String>();
				excludeKeys.addAll(personalInfo.getDisableKeys());
				excludeKeys.addAll(personalInfo.getHideKeys());
				
				ArrayList<String> values= personalInfo.getInfoValues(excludeKeys);
				boolean found = false;
				for(String value : values) {
					if(match(value, query)) {
						found = true;
						break;
					}
				}
				if(found) return true;
			}
		}
		else if(object instanceof UserConceptInfo) {
			if((findType & FIND_TYPE_CONCEPT) != 0) {
				UserConceptInfo userConceptInfo = (UserConceptInfo)object;
				String cName = userConceptInfo.getConcept().getName();
				String cTitle = userConceptInfo.getConcept().getTitle();
				if(match(cName, query) || match(cTitle, query)) return true;
			}
		}
		return false;
	}
	protected boolean match(String data, String query) {
		if(data == null || query == null) return false;
		data = data.trim().toLowerCase();
		query = query.trim().toLowerCase();
		if(data.length() == 0 || query.length() == 0) return false;
		
		String[] s_data = data.split(" ");
		String[] s_query = query.split(" ");
		
		ArrayList<String> a_data = new ArrayList<String>();
		for(String word : s_data) {
			if(word == null) continue;
			word = word.trim();
			if(word.length() == 0) continue;
			a_data.add(word);
		}

		ArrayList<String> a_query = new ArrayList<String>();
		for(String key : s_query) {
			if(key == null) continue;
			key = key.trim();
			if(key.length() == 0) continue;
			a_query.add(key);
		}
		
		for(String key : a_query) {
			boolean found = false;
			for(String word : a_data) {
				if(word.indexOf(key) != -1) {
					found = true;
					break;
				}
			}
			if(!found) return false;
		}
		return true;
	}
}
