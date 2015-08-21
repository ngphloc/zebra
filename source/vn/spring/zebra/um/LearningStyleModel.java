/**
 * 
 */
package vn.spring.zebra.um;

import java.util.HashSet;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.bn.LSChangeEvent;
import vn.spring.zebra.bn.LSChangeListener;
import vn.spring.zebra.client.LearningStyle;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.um.HiddenMarkovUM.OptimalStateChain;
import vn.spring.zebra.um.HiddenMarkovUM.UserStateChains;
import vn.spring.zebra.util.FireEventTask;
import vn.spring.zebra.util.FireEventUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class LearningStyleModel {
	private HiddenMarkovUM hmmUM = new HiddenMarkovUM();
	private UserStateChains uChains = null;
	
	private String  userid = null;
	private String  course = null;
	private boolean isChanged = false;
	
	public String getUserId() {return userid;}
	public String getCourse() {return course;}

	protected HashSet<LSChangeListener> listeners = new HashSet<LSChangeListener>();
	
	public synchronized OptimalStateChain getVerbalVisualChain() {
		return (uChains == null || uChains.getChainSize() < 1) ? null : uChains.getChain(0);
	}
	public synchronized OptimalStateChain getActiveReflectiveChain() {
		return (uChains == null||uChains.getChainSize() < 2) ? null : uChains.getChain(1);
	}
	public synchronized OptimalStateChain getTheoristPragmatistChain() {
		return (uChains == null||uChains.getChainSize() < 3) ? null : uChains.getChain(2);
	}
	
	public synchronized void setParameters(String userid, String course) {
		this.userid = userid;
		this.course = course;
	}
	
	public synchronized void perform() throws ZebraException, Exception {
		uChains = hmmUM.inferUserStateChains(userid, course);
		isChanged = true;
		LSChangeEvent evt = new LSChangeEvent(this, 
				new LearningStyle(isVerbalizer(), isActivist(), isTheorist()), getVerbalVisualChain(), getActiveReflectiveChain(), getTheoristPragmatistChain());
		FireEventUtil.fireEvent(new FireChangeEventTask(evt), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
	public synchronized boolean isVerbalizer() throws ZebraException {
		if(getVerbalVisualChain() == null) return false;
		if( uChains.getPredictedState(0).
			toLowerCase().
			equals(HiddenMarkovUM.PERSONAL_STATE0[0].toLowerCase()) )
			return true;
		return false;
	}
	public synchronized boolean isActivist() throws ZebraException {
		if(getActiveReflectiveChain() == null) return false;
		if( uChains.getPredictedState(1).
			toLowerCase().
			equals(HiddenMarkovUM.PERSONAL_STATE1[0].toLowerCase()) )
			return true;
		return false;
	}
	public synchronized boolean isTheorist() throws ZebraException {
		if(getTheoristPragmatistChain() == null) return false;
		if( uChains.getPredictedState(2).
			toLowerCase().
			equals(HiddenMarkovUM.PERSONAL_STATE2[0].toLowerCase()) )
			return true;
		return false;
	}
	public synchronized void addChangeListener(LSChangeListener listener) {
		listeners.add(listener);
	}
	public synchronized void removeChangeListener(LSChangeListener listener) {
		listeners.remove(listener);
	}
	public boolean isChanged() {return isChanged;}
	public void save() throws ZebraException {
		ZebraStatic.traceService.trace("LearningStyleModel.save isn't implemented yet");
		isChanged = false;
	}

	private class FireChangeEventTask implements FireEventTask {
		private LSChangeEvent evt = null;
		
		public FireChangeEventTask(LSChangeEvent evt) {
			this.evt = evt;
		}
		
		public void run() {
			for(LSChangeListener listener : listeners) {
				try {
					listener.lsChanged(evt);
				}
				catch(Exception e) {
					ZebraStatic.traceService.trace("LearningStyleModel.fireChangeEvent causes error: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
}
