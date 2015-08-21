/**
 * 
 */
package vn.spring.zebra.mail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.util.DaemonMsgListener;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class MailingListService {
    protected boolean isStarted = false;
    
    private HashMap<String, CourseMailingListDaemon> courseMailingListDaemons = new HashMap<String, CourseMailingListDaemon>();
    
    public MailingListService() {
    	isStarted = true;
	}

    @Override
    protected void finalize() throws Throwable {
    	removeAllCourseDaemons();
    	super.finalize();
    }
    
    public CourseMailingList addCourseMailingListDaemon(String course) throws Exception {
		if(!isStarted) {ZebraStatic.traceService.trace("Engine is stopped! Can't add daemon"); return null;}
		
		if(courseMailingListDaemons.get(course) != null)
			return courseMailingListDaemons.get(course).getCourseMailingList();
		CourseMailingList mailingList = new CourseMailingList(course);
		courseMailingListDaemons.put(course, new CourseMailingListDaemon(mailingList));
		return mailingList;
    }
    
	public void removeCourseMailingListDaemon(String course) {
		CourseMailingListDaemon daemon = courseMailingListDaemons.get(course);
		if(daemon != null)  {
			daemon.stop();
			courseMailingListDaemons.remove(course);
		}
	}
    
	public void removeAllCourseMailingListDaemons() {
		operateAllCourseMailingListDaemons(false);
		courseMailingListDaemons.clear();
	}
	
	public void removeAllCourseDaemons() {
		removeAllCourseMailingListDaemons();
    }
	
    public void removeCourseDaemons(String course) {
    	removeCourseMailingListDaemon(course);
    }
    
    public CourseMailingListDaemon getCourseMailingListDaemon(String course) {
		return courseMailingListDaemons.get(course);
    }
    
    public boolean isStarted() {return isStarted;}
	public void start() {
		if(isStarted) {
			ZebraStatic.traceService.trace("Mailing List Service is already started!");
			return;
		}
		operateAllCourseMailingListDaemons(true);

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
		operateAllCourseMailingListDaemons(false);

		isStarted = false;
	}
    
	private void operateAllCourseMailingListDaemons(boolean isStart) {
		Collection<CourseMailingListDaemon> daemons = courseMailingListDaemons.values();
		Iterator<CourseMailingListDaemon> iter = daemons.iterator();
		while(iter.hasNext()) {
			CourseMailingListDaemon daemon = iter.next();
			if(daemon == null) continue;
			if(isStart) daemon.start();
			else        daemon.stop();
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////////	
	public void addMsgListener(DaemonMsgListener listener) {
		Collection<CourseMailingListDaemon> daemons = courseMailingListDaemons.values();
		for(CourseMailingListDaemon daemon : daemons) {
			daemon.addMsgListener(listener);
		}
	}
	public void removeMsgListener(DaemonMsgListener listener) {
		Collection<CourseMailingListDaemon> daemons = courseMailingListDaemons.values();
		for(CourseMailingListDaemon daemon : daemons) {
			daemon.removeMsgListener(listener, true);
		}
	}
	public ArrayList<CourseMailingListDaemon> getCourseMailingListDaemons() {
		return new ArrayList<CourseMailingListDaemon>(courseMailingListDaemons.values());
	}
}
