/**
 * 
 */
package vn.spring.zebra.server;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQuery;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServerCommunicateService {
	protected TriUMServer server = null;
	protected TriUMServerQueryDelegator queryDelegator = null;
	protected TriUMServerSocketService socketService = null;
	
	protected boolean isStarted = false;
	
	public TriUMServerCommunicateService(TriUMServer server) throws RemoteException, MalformedURLException {
		this.server = server;
		queryDelegator = new TriUMServerQueryDelegator(server);
		socketService = new TriUMServerSocketService(server, TriUMQuery.COMMUNICATE_SOCKET_SERVICE_PORT);
		isStarted = true;
		ZebraStatic.traceService.trace("Communicate service started!");
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		onDestroyed();
		super.finalize();
	}

	protected void onDestroyed() {
		if(queryDelegator != null) {
			try {
				queryDelegator.onDestroyed();
				queryDelegator = null;
			}
			catch(Exception e) {
				ZebraStatic.traceService.trace("TriUMServerCommunicateService.onDestroyed->TriUMServerQueryDelegator.onDestroyed causes error: " + 
						e.getMessage());
				queryDelegator = null;
			}
		}
		
		if(socketService != null) {
			try {
				socketService.onDestroyed();
				socketService = null;
			}
			catch(Exception e) {
				ZebraStatic.traceService.trace("TriUMServerCommunicateService.onDestroyed->TriUMServerSocketService.stop() causes error: " + 
						e.getMessage());
				socketService = null;
			}
		}
		
		ZebraStatic.traceService.trace("Communicate service was destroyed!");
	}
	public TriUMServerQueryDelegator getQueryDelegator() { return queryDelegator;}
	
	public void stopSuspend() {
		if(!isStarted) {
			ZebraStatic.traceService.trace("Communicate service had already been stopped!");
			return;
		}
		socketService.suspend();
		isStarted = false;
		ZebraStatic.traceService.trace("Communicate service stopped! (TriUMServerSocketService.suspend)");
	}
	public void startResume() {
		if(isStarted) {
			ZebraStatic.traceService.trace("Communicate service had already been started!");
			return;
		}
		socketService.resume();
		isStarted = true;
		ZebraStatic.traceService.trace("Communicate service started! (TriUMServerSocketService.suspend)");
	}
	public boolean isStarted() {return isStarted;}
	
}
