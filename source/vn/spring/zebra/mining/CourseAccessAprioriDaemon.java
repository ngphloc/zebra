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
public class CourseAccessAprioriDaemon extends BasicDaemon {
	public CourseAccessAprioriDaemon(CourseAccessApriori apriori) {
		super(apriori, 
			"Recommender daemon started!", ZebraStatic.DAEMON_DELAY, ZebraStatic.DAEMON_MINER_INTERVAL);
	}

	@Override
	public void task() throws Exception {
		getCourseRecommend().perform();
		fireMsgListeners("Perform Training Task in Recommender daemon");
	}
	
	public CourseAccessApriori getCourseRecommend() {
		return (CourseAccessApriori)getInnerObject();
	}

	//@Override
	public String getUserId() {
		return getCourseRecommend().getUserId();
	}

	@Override
	public String getCourse() {
		return getCourseRecommend().getCourse();
	}

	@Override
	public String getDaemonName() {
		return "Recommender daemon";
	}
}
