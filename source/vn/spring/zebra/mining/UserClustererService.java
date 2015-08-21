/**
 * 
 */
package vn.spring.zebra.mining;

import java.util.*;

import vn.spring.WOW.engine.ConceptInfo;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.util.ConceptUtil;
import vn.spring.zebra.util.FireEventTask;
import vn.spring.zebra.util.FireEventUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserClustererService {
    protected HashMap<String, UserClustererDaemon> userClusterers = new HashMap<String, UserClustererDaemon>(); //for course
	protected HashSet<UserClustererServiceChangeListener> userClustererServiceChangeListeners = new HashSet<UserClustererServiceChangeListener>();
	protected boolean isStarted = false;
    
	public UserClustererService() {
		isStarted = true;
	}
	public synchronized UserClusterer addClusterer(String course) throws Exception {
		if(!isStarted) {ZebraStatic.traceService.trace("Engine is stopped! Can't add daemon"); return null;}
		try {
			if(userClusterers.get(course) != null)
				return userClusterers.get(course).getClusterer();
			
			ArrayList<ConceptInfo> filtered = ConceptUtil.getConceptInfos(course, 
					ZebraStatic.CLUSTER_CONCEPT_DEEP_LEVEL, true);
			ArrayList<String> vars = new ArrayList<String>();
			for(ConceptInfo info : filtered) {vars.add(info.getConceptName());}
			String[] vars2 = new String[vars.size()];
			for(int i = 0; i < vars.size(); i++) vars2[i] = vars.get(i);
			
			UserClusterer userClusterer = new UserClusterer();
			userClusterer.setParameters(course, vars2, ZebraStatic.CLUSTER_MAX_CLUSTER);
			userClusterer.perform();
			UserClustererDaemon userClustererDaemon = new UserClustererDaemon(userClusterer);
			userClustererDaemon.fireMsgListeners("Perform Clustering Task in User Community daemon (started)");
			
			userClusterers.put(course, userClustererDaemon);
			
			UserClustererServiceChangeEvent evt = new UserClustererServiceChangeEvent(this, userClusterer, UserClustererServiceChangeEvent.CHANGE_TYPE.ADD);
			FireEventUtil.fireEvent(new FireChangeEventTask(evt), ZebraStatic.FIRE_EVENT_WAIT_DONE);
			return userClusterer;
		}
		catch(Exception e) {
			UserClustererDaemon daemon = userClusterers.get(course);
			if(daemon != null) {
				daemon.stop();
				userClusterers.remove(course);
			}
			ZebraStatic.traceService.trace("UserClustererService.addClusterer causes error: " + e.getMessage());
			throw e;
		}
	}
	public synchronized void removeClusterer(String course) {
		UserClustererDaemon daemon = userClusterers.get(course);
		if(daemon == null) return;
		daemon.stop();
		userClusterers.remove(course);
		UserClustererServiceChangeEvent evt = new UserClustererServiceChangeEvent(this, daemon.getClusterer(), UserClustererServiceChangeEvent.CHANGE_TYPE.REMOVE);
		FireEventUtil.fireEvent(new FireChangeEventTask(evt), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
	
	public synchronized void removeAllClusterers() {
		operateAllClusteres(false);
		userClusterers.clear();
		UserClustererServiceChangeEvent evt = new UserClustererServiceChangeEvent(this, null, UserClustererServiceChangeEvent.CHANGE_TYPE.REMOVEALL);
		FireEventUtil.fireEvent(new FireChangeEventTask(evt), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
    public synchronized UserClustererDaemon getUserClustererDaemon(String course) {
		return userClusterers.get(course);
    }
    public synchronized UserClusterer getClusterer(String course, boolean isCreate) throws Exception {
    	UserClustererDaemon daemon = getUserClustererDaemon(course);
    	if(daemon != null) return daemon.getClusterer();
    	else if(isCreate)  return addClusterer(course);
    	else               return null;
    }
    public synchronized ArrayList<UserClusterer> getAllClusterers() {
		ArrayList<UserClusterer> clusterers = new ArrayList<UserClusterer>();
		Collection<UserClustererDaemon> daemons = userClusterers.values();
		Iterator<UserClustererDaemon> iter = daemons.iterator();
		while(iter.hasNext()) {
			UserClustererDaemon daemon = iter.next();
			clusterers.add(daemon.getClusterer());
		}
		return clusterers;
    }
    
	public synchronized void addUserClustererServiceChangeListener(UserClustererServiceChangeListener listener) {
		userClustererServiceChangeListeners.add(listener);
	}
	public synchronized void removeUserClustererServiceChangeListener(UserClustererServiceChangeListener listener) {
		userClustererServiceChangeListeners.remove(listener);
	}
	
    public boolean isStarted() {return isStarted;}
	public void start() {
		if(isStarted) {
			ZebraStatic.traceService.trace("Clusterer service is already started!");
			return;
		}
		operateAllClusteres(true);
		isStarted = true;
	}
	public void restart() {
		if(isStarted) stop();
		start();
	}
	public void stop() {
		if(!isStarted) {
			ZebraStatic.traceService.trace("Clusterer service is already stopped!");
			return;
		}
		operateAllClusteres(false);
		isStarted = false;
	}

	private synchronized void operateAllClusteres(boolean isStarted) {
		Collection<UserClustererDaemon> daemons = userClusterers.values();
		Iterator<UserClustererDaemon> iter = daemons.iterator();
		while(iter.hasNext()) {
			UserClustererDaemon daemon = iter.next();
			if(isStarted) daemon.start();
			else          daemon.stop();
		}
	}
	
	private class FireChangeEventTask implements FireEventTask {
		private UserClustererServiceChangeEvent evt = null;
		public FireChangeEventTask(UserClustererServiceChangeEvent evt) {
			this.evt = evt;
		}
		public void run() {
			for(UserClustererServiceChangeListener listener : userClustererServiceChangeListeners) {
				try {
					switch(evt.changeType) {
					case ADD:
						listener.clustererAdded(evt);
						break;
					case REMOVE:
						listener.clustererRemoved(evt);
						break;
					case REMOVEALL:
						listener.clustererRemovedAll(evt);
						break;
					}
				}
				catch(Exception e) {
					ZebraStatic.traceService.trace("UserClustererService.FireUserClustererServerChangeEvent causes error: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
}

