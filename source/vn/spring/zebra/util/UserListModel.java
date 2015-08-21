/**
 * 
 */
package vn.spring.zebra.util;

import java.util.ArrayList;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.mail.UserMailInfo;
import vn.spring.zebra.util.sortabletable.SortableTableModel;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserListModel extends SortableTableModel {
	private static final long serialVersionUID = 1L;
	private UserListModelHelper helper = null;
	
	public UserListModel(UserListModelHelper helper, ArrayList<String> excludeUserIdList) {
		super();
		this.helper = helper;
		setColumnIdentifiers(this.helper.createColumnNames());
		reload(excludeUserIdList);
	}
	public UserListModel(ArrayList<String> excludeUserIdList) {
		this(new UserListModelHelperImpl(), excludeUserIdList);
	}
	public UserListModel() {
		this(new ArrayList<String>());
	}

	public void reload(ArrayList<String> excludedUserIdList) {
		removeAllUser();
		ArrayList<String> userids = new ArrayList<String>();
		try {
			userids = UserUtil.getProfileIdList(ZebraStatic.IGNORE_ANONYMOUS);
		}
		catch(Exception e) {e.printStackTrace();}
		
		for(String userid : userids) {
			if(excludedUserIdList == null || !excludedUserIdList.contains(userid)) 
				addUser(userid);
		}
	}
	public void addUser(String userid) {
		Object[] row = helper.createRow(userid);
		if(row == null) return;
		addRow(row);
	}

	public void removeUser(String userid) {
		for(int i = 0; i < getRowCount(); i++) {
			if(getUserIdAt(i).equals(userid)) {
				removeRow(i);
				return;
			}
		}
	}
	public void removeAllUser() {
		ArrayList<String> userids = new ArrayList<String>();
		for(int i = 0; i < getRowCount(); i++) {
			userids.add(getUserIdAt(i));
		}
		
		for(String userid : userids) {
			removeUser(userid);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	@Override
	public boolean isSortable(int column) {
		return (column >= 0 && column < getColumnCount());
	}
	
	public ArrayList<String> getUserIdList() {
		ArrayList<String> userids = new ArrayList<String>();
		for(int i = 0; i < getRowCount(); i++) 
			userids.add(getUserIdAt(i));
		return userids;
	}

	public String getUserIdAt(int row) {
		return (String)getValueAt(row, helper.getUserIdIndex());
	}
	
}
class UserListModelHelperImpl implements UserListModelHelper {
	public Object[] createRow(String userid) {
		UserMailInfo info = new UserMailInfo(userid);
		if(info.userid == null || info.mail == null) return null;
		return new Object[] {info.userid, info.username, info.mail};
	}
	public Object[] createColumnNames() {
		return new Object[] {"User Id", "User Name", "Email"};
	}
	public int getUserIdIndex() {
		return 0;
	}
}