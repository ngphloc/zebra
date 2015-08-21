/**
 * 
 */
package vn.spring.zebra.um;

import java.util.*;

import vn.spring.WOW.datacomponents.Profile;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserInfos {
	private ArrayList<UserInfo> users = new ArrayList<UserInfo>();
	
	public UserInfos(List<UserInfo> users) {
		if(users != null) this.users.addAll(users);
	}
	public UserInfos(ArrayList<Profile> profiles) {
		ArrayList<UserInfo> users = new ArrayList<UserInfo>(); 
		for(Profile profile : profiles) {
			try {
				users.add(new UserInfo(profile));
			}
			catch(Exception e) {e.printStackTrace();}
		}
		Collections.sort(users, new Comparator<UserInfo>() {
			public int compare(UserInfo user1, UserInfo user2) {
				return user1.toString().compareTo(user2.toString());
			}
		});
		this.users = users;
	}
	public ArrayList<UserInfo> getUsers() {return users;}
	public void insertUser(UserInfo user, int idx) {
		if(idx >= this.users.size()) {
			this.users.add(user);
			return;
		}
		
		ArrayList<UserInfo> users = new ArrayList<UserInfo>();
		if(idx == 0) {
			users.add(user);
			users.addAll(this.users);
		}
		else {
			for(int i = 0; i < idx; i++) users.add(this.users.get(i));
			users.add(user);
			for(int i = idx; i < this.users.size(); i++) users.add(this.users.get(i));
		}
		this.users.clear();
		this.users.addAll(users);
	}
	public void removeUser(String userid) {
		int idx = -1;
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getUserId().equals(userid)) {
				idx = i;
				break;
			}
		}
		if(idx != -1) users.remove(idx);
	}
	public String toString() {return "Users";}
}
