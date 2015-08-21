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
public class CourseAccessSequencesDaemon extends BasicDaemon {
	public CourseAccessSequencesDaemon(CourseAccessSequences2 sequences) {
		super(sequences, "Learning Path daemon started!", 
				ZebraStatic.DAEMON_DELAY, ZebraStatic.DAEMON_MINER_INTERVAL);
	}

	@Override
	public void task() throws Exception {
		getCourseSequence().perform();
		fireMsgListeners("Perform Training Task in Learning Path daemon");
	}
	public CourseAccessSequences2 getCourseSequence() {
		return (CourseAccessSequences2)getInnerObject();
	}
	
	@Override
	public String getUserId() {
		return getCourseSequence().getUserId();
	}

	@Override
	public String getCourse() {
		return getCourseSequence().getCourse();
	}
	
	@Override
	public String getDaemonName() {
		return "Learning Path daemon";
	}
}
