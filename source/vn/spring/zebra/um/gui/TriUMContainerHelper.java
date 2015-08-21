/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.util.ArrayList;

import vn.spring.zebra.um.TriUM;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMContainerHelper {
	protected TriUMContainer container = null;
	
	public TriUMContainerHelper(TriUMContainer container) {
		this.container = container;
	}
	public ArrayList<String> getUserIds() {
		ArrayList<String> userids = new ArrayList<String>();
		int n = container.getUMCount();
		for(int i = 0; i < n; i++) {
			TriUM um = container.getUM(i);
			String userid = um.getUserId();
			if(!userids.contains(userid)) userids.add(userid);
		}
		
		return userids;
	}
	public ArrayList<TriUM> getUMs(String userid) {
		ArrayList<TriUM> ums = new ArrayList<TriUM>();
		int n = container.getUMCount();
		for(int i = 0; i < n; i++) {
			TriUM um = container.getUM(i);
			if(um.getUserId().equals(userid)) ums.add(um);
		}
		return ums;
	}
	public boolean isMonitored(String userid) {
		int n = container.getUMCount();
		for(int i = 0; i < n; i++) {
			TriUM um = container.getUM(i);
			if(um.getUserId().equals(userid)) return true;
		}
		return false;
	}

}
