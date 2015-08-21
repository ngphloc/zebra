package vn.spring.zebra.collab;

import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CollabDaemon extends Thread {
	private int port = 0;
	CollabServer chatServer = null;
	
	public CollabDaemon(int port) {
		this.port = port;
		setDaemon(true); 
	}
	
	public void run() {
		try {
			chatServer = new CollabServer(port);
			ZebraStatic.traceService.trace("Collabarative daemon run");
		}
		catch(Exception e) {
			ZebraStatic.traceService.trace("Collabarative daemon cause error: " + e.getMessage());
			e.printStackTrace();
			chatServer = null;
		}
	}

	public void destroy() {
		chatServer = null;
		super.destroy();
		ZebraStatic.traceService.trace("Collabarative daemon destroy");
	}

}
