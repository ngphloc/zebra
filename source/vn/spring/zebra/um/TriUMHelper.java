/**
 * 
 */
package vn.spring.zebra.um;

import vn.spring.zebra.bn.DynOverlayBayesUMDaemon;
import vn.spring.zebra.bn.LearningStyleDaemon;
import vn.spring.zebra.bn.StaticOverlayBayesUMDaemon;
import vn.spring.zebra.mining.CourseAccessAprioriDaemon;
import vn.spring.zebra.mining.CourseAccessSequencesDaemon;
import vn.spring.zebra.mining.LearningHistoryDataDaemon;
import vn.spring.zebra.search.UserSearchServiceDaemon;
import vn.spring.zebra.util.DaemonMsgListener;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMHelper {
	protected TriUM um = null;
	
	public TriUMHelper(TriUM um) {
		this.um = um;
	}
	public StaticOverlayBayesUMDaemon getStaticKnowledgeDaemon() {
		return um.getZebraNetworker().getStaticKnowledgeDaemon(um.getUserId(), um.getCourse());
	}
    public DynOverlayBayesUMDaemon getDynKnowledgeDaemon() {
    	return um.getZebraNetworker().getDynKnowledgeDaemon(um.getUserId(), um.getCourse());
    }
	public LearningStyleDaemon getLearningStyleDaemon() {
		return um.getZebraNetworker().getLearningStyleDaemon(um.getUserId(), um.getCourse());
	}
	public CourseAccessAprioriDaemon getRecommenderDaemon() {
		return um.getZebraMiner().getUserRecommenderDaemon(um.getUserId(), um.getCourse());
	}
	public CourseAccessSequencesDaemon getSequenceDaemon() {
		return um.getZebraMiner().getUserSequenceDaemon(um.getUserId(), um.getCourse());
	}
	public LearningHistoryDataDaemon getHisDataDaemon() {
		return um.getZebraMiner().getUserHisDataDaemon(um.getUserId(), um.getCourse());
	}
	public UserSearchServiceDaemon getSearchServiceDaemon() {
		return um.getZebraMiner().getUserSearchServiceDaemon(um.getUserId(), um.getCourse());
	}
	
    public void addStaticKnowledgeMsgListerner(DaemonMsgListener listener) {
    	getStaticKnowledgeDaemon().addMsgListener(listener);
    }
    public void removeStaticKnowledgeMsgListerner(DaemonMsgListener listener) {
    	getStaticKnowledgeDaemon().removeMsgListener(listener, false);
    }
    
    public void addDynKnowledgeMsgListerner(DaemonMsgListener listener) {
    	getDynKnowledgeDaemon().addMsgListener(listener);
    }
    public void removeDynKnowledgeMsgListerner(DaemonMsgListener listener) {
    	getDynKnowledgeDaemon().removeMsgListener(listener, false);
    }
    
    public void addLearningStyleMsgListerner(DaemonMsgListener listener) {
    	getLearningStyleDaemon().addMsgListener(listener);
    }
    public void removeLearningStyleMsgListerner(DaemonMsgListener listener) {
    	getLearningStyleDaemon().removeMsgListener(listener, false);
    }

    public void addRecommenderMsgListerner(DaemonMsgListener listener) {
    	getRecommenderDaemon().addMsgListener(listener);
    }
    public void removeRecommenderMsgListerner(DaemonMsgListener listener) {
    	getRecommenderDaemon().removeMsgListener(listener, false);
    }

    public void addSequenceMsgListerner(DaemonMsgListener listener) {
    	getSequenceDaemon().addMsgListener(listener);
    }
    public void removeSequenceMsgListerner(DaemonMsgListener listener) {
    	getSequenceDaemon().removeMsgListener(listener, false);
    }

    public void addHisDataMsgListerner(DaemonMsgListener listener) {
    	getHisDataDaemon().addMsgListener(listener);
    }
    public void removeHisDataMsgListerner(DaemonMsgListener listener) {
    	getHisDataDaemon().removeMsgListener(listener, false);
    }

    public void addUserSearchServiceMsgListerner(DaemonMsgListener listener) {
    	getSearchServiceDaemon().addMsgListener(listener);
    }
    public void removeUserSearchServiceMsgListerner(DaemonMsgListener listener) {
    	getSearchServiceDaemon().removeMsgListener(listener, false);
    }

	public void addMsgListeners(DaemonMsgListener listener) {
		try {
			addStaticKnowledgeMsgListerner(listener);
		}
		catch(Exception e) {e.printStackTrace();System.out.println("TriUM.addStaticKnowledgeMsgListerner causes error: " + e.getMessage());}
		
		try {
			addDynKnowledgeMsgListerner(listener);
		}
		catch(Exception e) {System.out.println("TriUM.addDynKnowledgeMsgListerner causes error: " + e.getMessage());}

		try {
			addLearningStyleMsgListerner(listener);
		}
		catch(Exception e) {System.out.println("TriUM.addLearningStyleMsgListerner causes error: " + e.getMessage());}

		try {
			addHisDataMsgListerner(listener);
		}
		catch(Exception e) {System.out.println("TriUM.addHisDataMsgListerner causes error: " + e.getMessage());}

		try {
			addRecommenderMsgListerner(listener);
		}
		catch(Exception e) {System.out.println("TriUM.addRecommenderMsgListerner causes error: " + e.getMessage());}
		
		try {
			addSequenceMsgListerner(listener);
		}
		catch(Exception e) {System.out.println("TriUM.addSequenceMsgListerner causes error: " + e.getMessage());}

		try {
			addUserSearchServiceMsgListerner(listener);
		}
		catch(Throwable e) {e.printStackTrace(); System.out.println("TriUM.addUserSearchServiceMsgListerner causes error: " + e.getMessage());}
	}
	
	public void removeMsgListeners(DaemonMsgListener listener) {
		try {
			removeStaticKnowledgeMsgListerner(listener);
		}
		catch(Exception e) {System.out.println("TriUM.removeStaticKnowledgeMsgListerner causes error: " + e.getMessage());}

		try {
			removeDynKnowledgeMsgListerner(listener);
		}
		catch(Exception e) {System.out.println("TriUM.removeDynKnowledgeMsgListerner causes error: " + e.getMessage());}

		try {
			removeLearningStyleMsgListerner(listener);
		}
		catch(Exception e) {System.out.println("TriUM.removeLearningStyleMsgListerner causes error: " + e.getMessage());}

		try {
			removeHisDataMsgListerner(listener);
		}
		catch(Exception e) {System.out.println("TriUM.removeHisDataMsgListerner causes error: " + e.getMessage());}

		try {
			removeRecommenderMsgListerner(listener);
		}
		catch(Exception e) {System.out.println("TriUM.removeRecommenderMsgListerner causes error: " + e.getMessage());}
		
		try {
			removeSequenceMsgListerner(listener);
		}
		catch(Exception e) {System.out.println("TriUM.removeSequenceMsgListerner causes error: " + e.getMessage());}

		try {
			removeUserSearchServiceMsgListerner(listener);
		}
		catch(Exception e) {System.out.println("TriUM.removeUserSearchServiceMsgListerner causes error: " + e.getMessage());}
	}
}
