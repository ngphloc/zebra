/**
 * 
 */
package vn.spring.zebra.util;

import java.util.ArrayList;

import vn.spring.zebra.util.sortabletable.SortableTable;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserList extends SortableTable {
	private static final long serialVersionUID = 1L;

	public UserList(UserListModel model) {
		super(model);
	}
	
	public String getSelectedUserId() {
		UserListModel model = getUserListModel();
		int selectedRow = getSelectedRow();
		if(selectedRow == -1) return null;
		return model.getUserIdAt(selectedRow);
	}
	public ArrayList<String> getSelectedUserIdList() {
		ArrayList<String> selectedUserIdList = new ArrayList<String>();
		int[] rows = getSelectedRows();
		if(rows == null || rows.length == 0) return selectedUserIdList;
		
		UserListModel model = getUserListModel();
		for(int row : rows) {
			String userid = model.getUserIdAt(row);
			if(userid != null && userid.length() > 0)
				selectedUserIdList.add(userid);
		}
		return selectedUserIdList;
	}
	public UserListModel getUserListModel() {return (UserListModel)getModel();}
}

