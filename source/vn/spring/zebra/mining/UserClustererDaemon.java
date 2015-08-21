/**
 * 
 */
package vn.spring.zebra.mining;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.util.BasicDaemon;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserClustererDaemon extends BasicDaemon {
	public UserClustererDaemon(UserClusterer clusterer) {
		super(clusterer, "User Community daemon started!", 
				ZebraStatic.DAEMON_DELAY, ZebraStatic.DAEMON_MINER_INTERVAL);
	}

	@Override
	public void task() throws Exception {
		// TODO Auto-generated method stub
		getClusterer().perform();
		fireMsgListeners("Perform Clustering Task in User Community daemon");
	}
	
	public UserClusterer getClusterer() {
		return (UserClusterer)getInnerObject();
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCourse() {
		// TODO Auto-generated method stub
		return getClusterer().getCourse();
	}

	@Override
	public String getDaemonName() {
		// TODO Auto-generated method stub
		return "User Community daemon";
	}
}
