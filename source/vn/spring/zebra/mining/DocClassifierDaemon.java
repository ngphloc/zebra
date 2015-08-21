/**
 * 
 */
package vn.spring.zebra.mining;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.mining.textmining.DocClassifier;
import vn.spring.zebra.util.BasicDaemon;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class DocClassifierDaemon extends BasicDaemon {

	public DocClassifierDaemon(DocClassifier classifier) {
		super(classifier, 
			"DocClassifier daemon started!", ZebraStatic.DAEMON_DELAY, ZebraStatic.DAEMON_MINER_INTERVAL,
			false);
	}

    @Deprecated
	@Override
	public void task() throws Exception {
    	getDocClassifier().buildClassifier();
    	fireMsgListeners("Build Classifier Task in DocClassifier daemon");
	}

	public DocClassifier getDocClassifier() {
		return (DocClassifier)getInnerObject();
	}
	
	@Override
	public String getCourse() {
		return getDocClassifier().getCourse();
	}

	@Override
	public String getDaemonName() {
		return "DocClassifier daemon";
	}

	@Override
	public String getUserId() {
		return null;
	}


}
