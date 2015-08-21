/**
 * 
 */
package vn.spring.zebra.server;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.collab.CollabServer;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServerCollabService extends Thread {
	protected TriUMServer server = null;
	protected int port = 0;
	protected CollabServer collabServer = null;
	protected boolean isStarted = false;
	
	public TriUMServerCollabService(TriUMServer server, int port) {
		super();
		this.server = server;
		this.port = port;
		collabServer = new CollabServer(port);
		start();
	}
	
	protected void onDestroyed() {
		if(collabServer == null) return;
		try {
			collabServer.onDestroyed();
			stop();
			collabServer = null;
		}
		catch(Exception e) {
			System.out.println("TriUMServerCollabService.onDestroyed() causes error: " + e.getMessage());
			collabServer = null;
		}
	}
	
	public TriUMServer getServer() {return server;}
	public CollabServer getCollab() {return collabServer;}
	public void run() {
		try {
			collabServer.start();
		}
		catch(Exception e) {
			ZebraStatic.traceService.trace("TriUMServerCollabService.run (call CollabServer instructor) causes error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		if(isStarted) {
			ZebraStatic.traceService.trace("Collaborative service had already been started!");
			return;
		}
		super.start();
		isStarted = true;
	}
	
	public void stopSuspend() {
		if(!isStarted) {
			ZebraStatic.traceService.trace("Collaborative service had already been stopped!");
			return;
		}
		this.suspend();
		isStarted = false;
		ZebraStatic.traceService.trace("Collaborative service stopped! (suspend)");
	}
	public void startResume() {
		if(isStarted) {
			ZebraStatic.traceService.trace("Collaborative service had already been started!");
			return;
		}
		this.resume();
		isStarted = true;
		ZebraStatic.traceService.trace("Collaborative service started! (resume)");
	}
	public boolean isStarted() {return isStarted;}
}
