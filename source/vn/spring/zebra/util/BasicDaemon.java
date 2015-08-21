/**
 * 
 */
package vn.spring.zebra.util;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public abstract class BasicDaemon extends Timer {
//public abstract class BasicDaemon extends ZebraTimer {
    protected HashSet<DaemonMsgListener> listeners = new HashSet<DaemonMsgListener>();
	protected Object  innerObject = null;
	protected long    delay = 0, period = 0;
	protected boolean isStarted = false;

	public BasicDaemon(Object innerObject, String daemonMsg, long delay, long period) {
		this(innerObject, daemonMsg, delay, period, true);
	}
	public BasicDaemon(Object innerObject, String daemonMsg, long delay, long period, boolean start) {
		super(true);
		this.innerObject = innerObject;
		this.delay = delay;
		this.period = period;
		
		if(start) {
			super.schedule(new BaseDaemonTask(), this.delay, this.period);
			fireMsgListeners(daemonMsg);
			isStarted = true;
		}
	}
	
	public Object getInnerObject() {return innerObject;}
	public synchronized void addMsgListener(DaemonMsgListener listener)    {
		if(!listeners.contains(listener))
			listeners.add(listener);
	}
	public synchronized void removeMsgListener(DaemonMsgListener listener, boolean forceRemoveCourse) {
		if(forceRemoveCourse)
			listeners.remove(listener);
		else {
			String userid = getUserId();
			if(userid != null && userid.length() > 0)
				listeners.remove(listener);
		}
	}
	private void removeAllMsgListeners() {listeners.clear();}
	public synchronized HashSet<DaemonMsgListener> getListeners() {return listeners;}
	
	public synchronized void fireMsgListeners(String msg) {
		FireEventUtil.fireEvent(new FireMsgEventTask(msg), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
	private class FireMsgEventTask implements FireEventTask {
		private String msg = null;
		public FireMsgEventTask(String msg) {
			this.msg = prepareMsg(msg);
		}
		public void run() {
			for(DaemonMsgListener listener : listeners) {
				try {
					listener.msgReceived(new DaemonMsgEvent(this, msg));
				}
				catch(Exception e) {
					listeners.remove(listener);
					ZebraStatic.traceService.trace(getDaemonName() + ".fireMsgListeners() causes error: " + e.getMessage());
					e.printStackTrace();
				}
			}
			ZebraStatic.traceService.trace(msg);
		}
	}
	
	@Override
	public void cancel() {
		try {
			super.cancel(); //super.cancelMayBeScheduled();
			removeAllMsgListeners();
			isStarted = false;
			ZebraStatic.traceService.trace(prepareMsg(getDaemonName() + " stopped (canceled)!"));
		}
		catch(Exception e) {e.printStackTrace();}
	}
	public void start() {
		try {
			if(isStarted) {
				ZebraStatic.traceService.trace(prepareMsg(getDaemonName() + " had already been started!"));
				return;
			}
			super.schedule(new BaseDaemonTask(), delay, period);
			isStarted = true;
		}
		catch(Exception e) {e.printStackTrace();}
	}
	
	public void restart() {
		if(isStarted) stop();
		start();
	}
	public void stop() {
		if(!isStarted) {
			ZebraStatic.traceService.trace(getDaemonName() + " had already been stopped!");
			return;
		}
		cancel();
		isStarted = false;
	}
	public boolean isStarted() {return isStarted;}
	
	public abstract void task() throws Exception ;
	public abstract String getUserId();
	public abstract String getCourse();
	public abstract String getDaemonName();

	private class BaseDaemonTask extends TimerTask {
		public BaseDaemonTask() {
			super();
	    }
		public void run() {
			try {
				task();
			}
			catch(Exception e) {
				ZebraStatic.traceService.trace(getDaemonName() + " task causes error: " + e.getMessage());
				e.printStackTrace();
			}
			
			CommonUtil.gc();
		}
	}
	private String prepareMsg(String msg) {
		if(getUserId() != null || getCourse() != null)
			return "(" + "user=" + (getUserId() == null ? "" : getUserId()) + ", course=" + 
				getCourse() + "): " + msg;
		else
			return msg;
	}
	
	
}
