/**
 * 
 */
package vn.spring.zebra.bn;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.um.LearningStyleModel;
import vn.spring.zebra.util.BasicDaemon;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class LearningStyleDaemon extends BasicDaemon {
	public LearningStyleDaemon(LearningStyleModel lsm) {
		super(lsm, "Learning Style daemon started!",
				ZebraStatic.DAEMON_DELAY, ZebraStatic.DAEMON_NETWORKER_INTERVAL);
	}

	@Override
	public void task() throws Exception {
		// TODO Auto-generated method stub
		getLearningStyleModel().perform();
		fireMsgListeners("Perform Training Task in Learning Style daemon");
	}
	
	public LearningStyleModel getLearningStyleModel() {
		return (LearningStyleModel)getInnerObject(); 
	}
	
	public String getUserId() {return getLearningStyleModel().getUserId();}
	public String getCourse() {return getLearningStyleModel().getCourse();}
	
	@Override
	public String getDaemonName() {
		// TODO Auto-generated method stub
		return "Learning Style daemon";
	}
}
