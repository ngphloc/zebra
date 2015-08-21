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
public class LearningHistoryDataDaemon extends BasicDaemon {
	public LearningHistoryDataDaemon(LearningHistoryData hisData) {
		super(hisData, "Learning History Data daemon started!", 
				ZebraStatic.DAEMON_DELAY, ZebraStatic.DAEMON_HIS_INTERVAL);
	}
	public LearningHistoryData getHisData() {return (LearningHistoryData)getInnerObject();}
	
	@Override
	public void task() throws Exception {
		// TODO Auto-generated method stub
		getHisData().update();
		fireMsgListeners("Perform Collecting Task in Learning History Data daemon");
	}
	public String getUserId() {return getHisData().getUserId();}
	public String getCourse() {return getHisData().getCourse();}
	
	@Override
	public String getDaemonName() {
		// TODO Auto-generated method stub
		return "Learning History Data daemon";
	}
}
