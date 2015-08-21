/**
 * 
 */
package vn.spring.zebra.server;

import java.util.ArrayList;
import java.util.Date;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.util.BasicDaemon;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServerGarbageService extends BasicDaemon {
	public TriUMServerGarbageService(TriUMServer server) {
		super(server, "Garbage Collection service started!", 
				ZebraStatic.DAEMON_DELAY, ZebraStatic.DAEMON_GARBAGE_INTERVAL);
	}
	public TriUMServer getServer() {return (TriUMServer)(getInnerObject());}
	@Override
	public String getCourse() {
		return "";
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void task() throws Exception {
		TriUMServer server = getServer(); 
		synchronized(server) {
			ArrayList<TriUM> ums = server.getAllMonitoredUM();
			for(TriUM um : ums) {
				Date oldDate = um.getLastQueryDate();
				Date newDate = new Date();
				long interval = newDate.getTime() - oldDate.getTime();
				if(interval > ZebraStatic.DAEMON_GARBAGE_INTERVAL) {
					try {
						String userid = um.getUserId(), course = um.getCourse();
						server.unmonitorUM(userid, course);
						fireMsgListeners("Garbage Collection service remove triangular user model (userid=" + 
							userid + ", course=" + course + ")");
					}
					catch(Exception e) {
						ZebraStatic.traceService.trace("TriUMServerGarbageCollection.task causes error: " + e.getMessage());
						e.printStackTrace();
					}
				}//end if
			}//end for
		}//end synchronized
		
	}
	
	@Override
	public String getDaemonName() {
		// TODO Auto-generated method stub
		return "Garbage Collection service";
	}

}
