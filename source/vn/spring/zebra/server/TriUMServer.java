/**
 * 
 */
package vn.spring.zebra.server;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.bn.ZebraNetworker;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.helperservice.RegisterService;
import vn.spring.zebra.mail.MailingListService;
import vn.spring.zebra.mining.UserClustererService;
import vn.spring.zebra.mining.ZebraMiner;
import vn.spring.zebra.server.TriUMServerChangeUMEvent.CHANGE_TYPE;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.util.CommonUtil;
import vn.spring.zebra.util.UserUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServer
{
	private static final long serialVersionUID = 1L;
	private static TriUMServer instance = null;
	
	protected ZebraNetworker zebraNetworker = null;
	protected ZebraMiner     zebraMiner = null;
    protected UserClustererService userClustererService = null; //for course
    
	protected TriUMServerCommunicateService communicateService = null;
	protected TriUMServerCollabService collabService = null;
	protected TriUMServerGarbageService garbageService = null;
	protected MailingListService        mailingListService = null;

	protected HashMap<String, TriUM> triUMs = new HashMap<String, TriUM>();

    protected HashSet<TriUMServerChangeUMListener> changeUMListeners = new HashSet<TriUMServerChangeUMListener>();
	
	protected TriUMServerSysTray sysTray = null;
	
	public static TriUMServer getInstance() throws Exception {
		if(instance == null) instance = new TriUMServer();
		return instance;
	}
	
	private TriUMServer() throws RemoteException, MalformedURLException {
		super();
		zebraNetworker = new ZebraNetworker();
		zebraMiner = new ZebraMiner(ZebraStatic.MINER_USE_GLOBAL_DAEMON, ZebraStatic.MINER_OWN_PRIVATE_LOG);
		userClustererService = new UserClustererService();
				
		communicateService = new TriUMServerCommunicateService(this);
		collabService = new TriUMServerCollabService(this, TriUMQuery.COLLAB_SERVICE_PORT);
		garbageService = new TriUMServerGarbageService(this);
		mailingListService = new MailingListService();
		
		new TriUMServerHelper(this).initCourses();
		
		sysTray = TriUMServerSysTray.getSeverSysTray(this);
        ZebraStatic.traceService.trace("TriUMServer created!");
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		onDestroyed();
		super.finalize();
        ZebraStatic.traceService.trace("TriUMServer.finalize");
	}
	
	@SuppressWarnings("deprecation")
	public void onDestroyed() {
		try {zebraNetworker.removeAllDaemons();}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUMServer.onDestroyed->zebraNetworker.removeAllTimers causes error: " + e.getMessage());}

		try {zebraMiner.removeAllUserDaemons();}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUMServer.onDestroyed->zebraMiner.removeAllUserDaemons causes error: " + e.getMessage());}

		try {zebraMiner.removeAllCourseDaemons();}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUMServer.onDestroyed->zebraMiner.removeAllCourseDaemons causes error: " + e.getMessage());}

		try {mailingListService.removeAllCourseDaemons();}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUMServer.onDestroyed->removeAllMailingList causes error: " + e.getMessage());}
		
		try {userClustererService.removeAllClusterers();}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUMServer.onDestroyed->removeAllClusterers causes error: " + e.getMessage());}

		TriUMServerControlPanel cp = new TriUMServerControlPanel(this);
		try {cp.stopCommunicateService();}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUMServer.onDestroyed->CommunicateService.stop causes error: " + e.getMessage());}
		try {cp.stopCollabService();}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUMServer.onDestroyed->CollabService.stop causes error: " + e.getMessage());}
		try {cp.stopGarbageService();}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUMServer.onDestroyed->GarbageCollection.stop causes error: " + e.getMessage());}

		try {collabService.onDestroyed();}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUMServer.onDestroyed->CollabService.onDestroyed causes error: " + e.getMessage());}
		try {communicateService.onDestroyed();}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUMServer.onDestroyed->CommunicateService.onDestroyed causes error: " + e.getMessage());}
		
		try {unmonitorAllUMs();}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUMServer.onDestroyed->unmonitorAllUM causes error: " + e.getMessage());}
        ZebraStatic.traceService.trace("TriUMServer.onDestroyed");
        
		try {if(sysTray != null) sysTray.onDestroyed();}
		catch(Exception e) {ZebraStatic.traceService.trace("TriUMServer.onDestroyed->sysTray.onDestruyed causes error: " + e.getMessage());}
	
		CommonUtil.gc();
	}
	
	public synchronized TriUM getUM(String userid, String course, boolean isMonitor) throws RemoteException {
		String key = ZebraStatic.makeId(userid, course);
		TriUM um = triUMs.get(key);

		if(um == null && isMonitor == true) {
			try {um = monitorUM(userid, course); triUMs.put(key, um);}
			catch(Exception e) {
				String errMsg = "TriUMServer.addUM causes error: " + e.getMessage();
				ZebraStatic.traceService.trace(errMsg); e.printStackTrace(); throw new RemoteException(errMsg);
			}
		}
		return um;
	}

	public synchronized ArrayList<TriUM> getAllMonitoredUM() {
		ArrayList<TriUM> ums = new ArrayList<TriUM>();
		
		Iterator<Entry<String, TriUM>> entries = triUMs.entrySet().iterator();
		while(entries.hasNext()) {
			ums.add(entries.next().getValue());
		}
		return ums;
	}

	public synchronized Profile registerUM(String userid, String password,
			String course, String directory, 
			HashMap<String, String> personalAttrs) throws ZebraException, Exception {
		
		try {
			ProfileDB pdb = ZebraStatic.getProfileDB();
			long id = pdb.findProfile(userid);
			if(id != 0) {
				ZebraStatic.traceService.trace("User profile exists");
				return pdb.getProfile(id);
			}
		}
		catch(Exception e) {}
		
		Profile profile = null;
		try {
			profile = RegisterService.registerUserProfile(userid, password, course, directory, personalAttrs);
		}
		catch(Exception e) {
			ZebraStatic.traceService.trace("TriUMServer.addUM causes error: " + e.getMessage());
			throw e;
		}
		
		new TriUMServerHelper(this).
			fireChangeUMEvent(new TriUMServerChangeUMEvent(this, userid, course, CHANGE_TYPE.REGISTER));
		return profile;
	}
	public synchronized void registerDefaultUMs(Reader reader) {
		new TriUMServerHelper(this).discardUMs(null, null);

		if(reader == null)
			reader = new InputStreamReader(getClass().getResourceAsStream("/vn/spring/zebra/resource/users.txt"));
		RegisterService.createUserProfiles(reader);
		
		CommonUtil.gc();
		new TriUMServerHelper(this).fireChangeUMEvent(new TriUMServerChangeUMEvent(this, (String)null, (String)null, CHANGE_TYPE.REGISTERDEFAULT));
	}
	
	public synchronized void unregisterUM(String userid) throws ZebraException, Exception {
		new TriUMServerHelper(this).discardUMs(userid, null);
		
		if(!UserUtil.isProfileExists(userid)) {
			ZebraStatic.traceService.trace("User profile not exists");
			CommonUtil.gc();
		}
		else {
			UserUtil.removeUser(userid);
			CommonUtil.gc();
	
			new TriUMServerHelper(this).
				fireChangeUMEvent(new TriUMServerChangeUMEvent(this, userid, (String)null, CHANGE_TYPE.UNREGISTER));
		}
	}
	
	public synchronized void unregisterAllUMs() {
		new TriUMServerHelper(this).discardUMs(null, null);
		UserUtil.removeAllUsers();
		
		CommonUtil.gc();
		TriUMServerChangeUMEvent evt = new TriUMServerChangeUMEvent(this, (String)null, (String)null, CHANGE_TYPE.UNREGISTERALL);
		evt.obj = getAllMonitoredUM();
		new TriUMServerHelper(this).fireChangeUMEvent(evt);
	}

	public synchronized void monitorUM(TriUM um) {
		String key = ZebraStatic.makeId(um.getUserId(), um.getCourse());
		if(triUMs.get(key) != null) return;
		triUMs.put(key, um);
		
		new TriUMServerHelper(this).
			fireChangeUMEvent(new TriUMServerChangeUMEvent(this, um, CHANGE_TYPE.MONITOR));
	}
	public synchronized TriUM monitorUM(String userid, String course) throws ZebraException, Exception {
		String key = ZebraStatic.makeId(userid, course);
		TriUM um = triUMs.get(key);
		if(um != null) return um;
		if(!zebraNetworker.isStarted() || !zebraMiner.isStarted())
			throw new Exception("Zebra networker or Zebra miner is already stopped!");
		try {
			um = new TriUM(userid, course, zebraNetworker, zebraMiner);
		}
		catch(Exception e) {
			ZebraStatic.traceService.trace("TriUMServer.addUM causes error: " + e.getMessage());
			zebraNetworker.removeDaemons(userid, course);
			zebraMiner.removeUserDaemons(userid, course);
			throw e;
		}
		triUMs.put(key, um);
		
		new TriUMServerHelper(this).
			fireChangeUMEvent(new TriUMServerChangeUMEvent(this, um, CHANGE_TYPE.MONITOR));
		return um;
	}
	public synchronized ArrayList<TriUM> monitorCourse(String course) throws Exception {
		if(!zebraNetworker.isStarted() || !zebraMiner.isStarted())
			throw new Exception("Zebra networker or Zebra miner is already stopped!");

		ArrayList<TriUM> monitoredUMs = new ArrayList<TriUM>();
		ArrayList<String> userids = UserUtil.getProfileIdList(ZebraStatic.IGNORE_ANONYMOUS);
		if(userids.size() == 0) return monitoredUMs;
		for(String userid : userids) {
			String key = ZebraStatic.makeId(userid, course);
			TriUM um = triUMs.get(key);
			if(um == null) {
				try {
					um = new TriUM(userid, course, zebraNetworker, zebraMiner);
				}
				catch(Exception e) {
					ZebraStatic.traceService.trace("TriUMServer.addUM causes error: " + e.getMessage());
					zebraNetworker.removeDaemons(userid, course);
					zebraMiner.removeUserDaemons(userid, course);
					throw e;
				}
				triUMs.put(key, um);
				monitoredUMs.add(um);
			}
			else
				monitoredUMs.add(um);
		}
		TriUMServerChangeUMEvent evt = new TriUMServerChangeUMEvent(this, (String)null, course, CHANGE_TYPE.MONITORCOURSE);
		evt.obj = monitoredUMs;
		new TriUMServerHelper(this).fireChangeUMEvent(evt);
		return monitoredUMs;
	}
	public synchronized void unmonitorUM(String userid, String course) {
		String key = ZebraStatic.makeId(userid, course);
		TriUM um = triUMs.remove(key);
		if(um == null) return;
		um.discard();
		
		CommonUtil.gc();
		new TriUMServerHelper(this).fireChangeUMEvent(new TriUMServerChangeUMEvent(this, um, CHANGE_TYPE.UNMONITOR));
	}
	public synchronized void unmonitorCourse(String course) {
		new TriUMServerHelper(this).discardUMs(null, course);
		zebraMiner.removeCourseDaemons(course);
		
		CommonUtil.gc();
		TriUMServerChangeUMEvent evt = new TriUMServerChangeUMEvent(this, (String)null, course, CHANGE_TYPE.UNMONITORCOURSE);
		evt.obj = this.getAllMonitoredUM();
		new TriUMServerHelper(this).fireChangeUMEvent(evt);
	}
	public synchronized void unmonitorAllUMs() {
		boolean isFireEvent = (triUMs.size() > 0);
		new TriUMServerHelper(this).discardUMs(null, null);
		
		CommonUtil.gc();
		if(isFireEvent) {
			new TriUMServerHelper(this).
				fireChangeUMEvent(new TriUMServerChangeUMEvent(this, (String)null, (String)null, CHANGE_TYPE.UNMONITORALL));
		}
	}
	
	public void addChangeUMListener(TriUMServerChangeUMListener listener) {
		synchronized(changeUMListeners) {changeUMListeners.add(listener);}
	}
	public void removeChangeUMListener(TriUMServerChangeUMListener listener) {
		synchronized(changeUMListeners) {changeUMListeners.remove(listener);}
	}

    public ZebraNetworker                 getNetworker() {return zebraNetworker;}
    public ZebraMiner                     getMiner() {return zebraMiner;}
	public UserClustererService           getUserClustererService() {return userClustererService;}
    public TriUMServerCommunicateService  getCommunicateService() {return communicateService;}
    public TriUMServerCollabService       getCollabService() {return collabService;}
    public TriUMServerGarbageService      getGarbageService() {return garbageService;}
    public MailingListService             getMailingListService() {return mailingListService;}
    
    public static void main(String[] args) throws Exception {
    	TriUMServer.getInstance();
    }
}


