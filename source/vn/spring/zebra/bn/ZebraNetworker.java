/**
 * 
 */
package vn.spring.zebra.bn;

import java.util.*;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.um.DynOverlayBayesUM;
import vn.spring.zebra.um.LearningStyleModel;
import vn.spring.zebra.um.OverlayBayesUM;
import vn.spring.zebra.um.TriUM;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ZebraNetworker {
    protected HashMap<String, LearningStyleDaemon> courseLearningStyleModelDaemons = new HashMap<String, LearningStyleDaemon>();
    protected HashMap<String, DynOverlayBayesUMDaemon> courseDynKnowledgeDaemons = new HashMap<String, DynOverlayBayesUMDaemon>();
    protected HashMap<String, StaticOverlayBayesUMDaemon> courseStaticKnowledgeDaemons = new HashMap<String, StaticOverlayBayesUMDaemon>();

    protected boolean isStarted = false;

    public ZebraNetworker() {
    	isStarted = true;
	}
    
    @Override
    protected void finalize() throws Throwable {
    	removeAllDaemons();
    	super.finalize();
    }
    
    
	public LearningStyleModel addLearningStyleModelDaemon(String userid, String course) throws ZebraException, Exception {
		if(!isStarted) {ZebraStatic.traceService.trace("Engine is stopped! Can't add daemon"); return null;}

		if(courseLearningStyleModelDaemons.get(ZebraStatic.makeId(userid, course)) != null)
			return courseLearningStyleModelDaemons.get(ZebraStatic.makeId(userid, course)).getLearningStyleModel();
		LearningStyleModel lsm = new LearningStyleModel();
		lsm.setParameters(userid, course);
		lsm.perform();
		LearningStyleDaemon daemon = new LearningStyleDaemon(lsm);
		daemon.fireMsgListeners("Perform Training Task in Learning Style daemon (started)");
		courseLearningStyleModelDaemons.put(ZebraStatic.makeId(userid, course), daemon);
		return lsm;
	}
	public DynOverlayBayesUM addDynKnowledgeDaemon(String userid, String course) throws ZebraException, Exception {
		if(!isStarted) {ZebraStatic.traceService.trace("Engine is stopped! Can't add daemon"); return null;}

		if(courseDynKnowledgeDaemons.get(ZebraStatic.makeId(userid, course)) != null)
			return courseDynKnowledgeDaemons.get(ZebraStatic.makeId(userid, course)).getDynOverlayBayesUM();
		DynOverlayBayesUM dynUM = new DynOverlayBayesUM(userid, course, ZebraStatic.DYN_DEFAULT_GUESS, ZebraStatic.DYN_DEFAULT_FORGET);
		DynOverlayBayesUMDaemon daemon = new DynOverlayBayesUMDaemon(dynUM);
		daemon.fireMsgListeners("Initialize Dynamic Knowledge Bayesian Network daemon");
		courseDynKnowledgeDaemons.put(ZebraStatic.makeId(userid, course), daemon);
		return dynUM;
	}
	public OverlayBayesUM addStaticKnowledgeDaemon(String userid, String course) throws ZebraException, Exception {
		if(!isStarted) {ZebraStatic.traceService.trace("Engine is stopped! Can't add daemon"); return null;}

		if(courseStaticKnowledgeDaemons.get(ZebraStatic.makeId(userid, course)) != null)
			return courseStaticKnowledgeDaemons.get(ZebraStatic.makeId(userid, course)).getStaticOverlayBayesUM();
		OverlayBayesUM staticUM = TriUM.getBayesUM(userid, course);
		StaticOverlayBayesUMDaemon daemon = new StaticOverlayBayesUMDaemon(staticUM, userid, course);
		daemon.fireMsgListeners("Initialize Static Knowledge Bayesian Network daemon");
		courseStaticKnowledgeDaemons.put(ZebraStatic.makeId(userid, course), daemon);
		return staticUM;
	}
	public void removeLearningStyleModelDaemon(String userid, String course) {
		LearningStyleDaemon daemon = courseLearningStyleModelDaemons.get(ZebraStatic.makeId(userid, course));
		if(daemon != null) {
			daemon.stop();
			courseLearningStyleModelDaemons.remove(ZebraStatic.makeId(userid, course));
		}
	}
	public void removeDynKnowledgeDaemon(String userid, String course) {
		DynOverlayBayesUMDaemon daemon = courseDynKnowledgeDaemons.get(ZebraStatic.makeId(userid, course));
		if(daemon != null) {
			daemon.stop();
			courseDynKnowledgeDaemons.remove(ZebraStatic.makeId(userid, course));
			try {
				DynOverlayBayesUM dyn = daemon.getDynOverlayBayesUM();
				if(dyn.isChanged()) dyn.save();
			}
			catch(Exception e) {ZebraStatic.traceService.trace("removeDynKnowledgeDaemon causes error: " + e.getMessage());}
		}
	}
	public void removeStaticKnowledgeDaemon(String userid, String course) {
		StaticOverlayBayesUMDaemon daemon = courseStaticKnowledgeDaemons.get(ZebraStatic.makeId(userid, course));
		if(daemon != null) {
			daemon.stop();
			courseStaticKnowledgeDaemons.remove(ZebraStatic.makeId(userid, course));
		}
	}
	public void removeAllLearningStyleModelDaemons() {
		operateAllLearningStyleModelDaemons(false);
		courseLearningStyleModelDaemons.clear();
	}
	public void removeAllDynKnowledgeDaemons() {
		operateAllDynKnowledgeDaemons(false);
		courseDynKnowledgeDaemons.clear();
	}
	public void removeAllStaticKnowledgeDaemons() {
		operateAllStaticKnowledgeDaemons(false);
		courseStaticKnowledgeDaemons.clear();
	}
    public void removeAllDaemons() {
    	removeAllLearningStyleModelDaemons();
    	removeAllDynKnowledgeDaemons();
    	removeAllStaticKnowledgeDaemons();
    }
    public void removeDaemons(String userid, String course) {
    	removeLearningStyleModelDaemon(userid, course);
    	removeDynKnowledgeDaemon(userid, course);
    	removeStaticKnowledgeDaemon(userid, course);
    }
    public LearningStyleDaemon getLearningStyleDaemon(String userid, String course) {
		return courseLearningStyleModelDaemons.get(ZebraStatic.makeId(userid, course));
    }
    public DynOverlayBayesUMDaemon getDynKnowledgeDaemon(String userid, String course) {
		return courseDynKnowledgeDaemons.get(ZebraStatic.makeId(userid, course));
    }
    public StaticOverlayBayesUMDaemon getStaticKnowledgeDaemon(String userid, String course) {
		return courseStaticKnowledgeDaemons.get(ZebraStatic.makeId(userid, course));
    }
    
    public boolean isStarted() {return isStarted;}
	public void start() {
		if(isStarted) {
			ZebraStatic.traceService.trace("Zebra miner is already started!");
			return;
		}
		operateAllLearningStyleModelDaemons(true);
		operateAllDynKnowledgeDaemons(true);
		operateAllStaticKnowledgeDaemons(true);
		isStarted = true;
	}
	public void restart() {
		if(isStarted) stop();
		start();
	}
	public void stop() {
		if(!isStarted) {
			ZebraStatic.traceService.trace("Zebra miner is already stopped!");
			return;
		}
		operateAllLearningStyleModelDaemons(false);
		operateAllDynKnowledgeDaemons(false);
		operateAllStaticKnowledgeDaemons(false);
		isStarted = false;
	}

	private void operateAllLearningStyleModelDaemons(boolean isStart) {
		Collection<LearningStyleDaemon> daemons = courseLearningStyleModelDaemons.values();
		Iterator<LearningStyleDaemon> iter = daemons.iterator();
		while(iter.hasNext()) {
			LearningStyleDaemon daemon = iter.next();
			if(daemon == null) continue;
			if(isStart) daemon.start();
			else        daemon.stop();
		}
	}
	private void operateAllDynKnowledgeDaemons(boolean isStart) {
		Collection<DynOverlayBayesUMDaemon> daemons = courseDynKnowledgeDaemons.values();
		Iterator<DynOverlayBayesUMDaemon> iter = daemons.iterator();
		while(iter.hasNext()) {
			DynOverlayBayesUMDaemon daemon = iter.next();
			if(daemon == null) continue;
			if(isStart)
				daemon.start();
			else {
				daemon.stop();
				try {
					DynOverlayBayesUM dyn = daemon.getDynOverlayBayesUM();
					if(dyn.isChanged()) dyn.save();
				}
				catch(Exception e) {ZebraStatic.traceService.trace("operateAllDynKnowledgeDaemons causes error: " + e.getMessage());}
			}
		}
	}
	private void operateAllStaticKnowledgeDaemons(boolean isStart) {
		Collection<StaticOverlayBayesUMDaemon> daemons = courseStaticKnowledgeDaemons.values();
		Iterator<StaticOverlayBayesUMDaemon> iter = daemons.iterator();
		while(iter.hasNext()) {
			StaticOverlayBayesUMDaemon daemon = iter.next();
			if(daemon == null) continue;
			if(isStart) daemon.start();
			else        daemon.stop();
		}
	}
    
}
