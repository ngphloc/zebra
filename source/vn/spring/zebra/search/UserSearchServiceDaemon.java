/**
 * 
 */
package vn.spring.zebra.search;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.util.BasicDaemon;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserSearchServiceDaemon extends BasicDaemon {

	public UserSearchServiceDaemon(UserSearchService service) {
		super(service, 
			"User Search Service daemon started!", ZebraStatic.DAEMON_DELAY, ZebraStatic.DAEMON_MINER_INTERVAL);
	}


	@Override
	public void task() throws Exception {
		getSearchUserService().update();
    	fireMsgListeners("Updating DocClass/User Interest in User Search Service daemon");
	}

	public UserSearchService getSearchUserService() {
		return (UserSearchService)getInnerObject();
	}

	@Override
	public String getDaemonName() {
		return "User Search Service daemon";
	}

	@Override
	public String getUserId() {
		return getSearchUserService().getUserId();
	}

	@Override
	public String getCourse() {
		return getSearchUserService().getCourse();
	}

}
