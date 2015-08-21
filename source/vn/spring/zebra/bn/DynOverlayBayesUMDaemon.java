/**
 * 
 */
package vn.spring.zebra.bn;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.evaluation.TestEngineEvent;
import vn.spring.zebra.evaluation.TestEngineListener;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.um.DynOverlayBayesUM;
import vn.spring.zebra.util.BasicDaemon;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class DynOverlayBayesUMDaemon extends BasicDaemon  implements TestEngineListener {
	public DynOverlayBayesUMDaemon(DynOverlayBayesUM dynUM) {
		super(dynUM, "Dynamic Knowledge Bayesian Network daemon started!", 
				ZebraStatic.DAEMON_DELAY, ZebraStatic.DAEMON_NETWORKER_INTERVAL,
				ZebraStatic.NETWORKER_RUN_DYN_KNOWLEDGE_DAEMON);
	}

	@Override
    @Deprecated
	public void task() throws Exception {
		getDynOverlayBayesUM()._learn();
		fireMsgListeners("Perform Training Task in Dynamic Knowledge Bayesian Network daemon");
	}
	
	public void testEvaluated(TestEngineEvent evt) throws ZebraException {
		// TODO Auto-generated method stub
		if(!evt.getEvidence().userid.equals(getUserId())) return;
		getDynOverlayBayesUM().testEvaluated(evt);
		fireMsgListeners("Updating Test Evidences in Dynamic Knowledge Bayesian Network daemon");
	}

	public DynOverlayBayesUM getDynOverlayBayesUM() {
		return (DynOverlayBayesUM)getInnerObject();
	}
	public String getUserId() {return getDynOverlayBayesUM().getUserId();}
	public String getCourse() {return getDynOverlayBayesUM().getCourse();}
	
	@Override
	public String getDaemonName() {
		// TODO Auto-generated method stub
		return "Dynamic Knowledge Bayesian Network daemon";
	}
}
