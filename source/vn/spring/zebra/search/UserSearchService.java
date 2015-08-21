/**
 * 
 */
package vn.spring.zebra.search;

import java.util.HashSet;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.mining.DocClassifierDaemon;
import vn.spring.zebra.mining.textmining.DocClassifier;
import vn.spring.zebra.server.TriUMServer;
import vn.spring.zebra.util.FireEventTask;
import vn.spring.zebra.util.FireEventUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserSearchService {
	protected String userid = null;
	protected String course = null;
	protected SearchUserDoc   userDoc = null;
	protected String          docClass = null;
	
	protected HashSet<UserSearchServiceChangeListener> listeners = new HashSet<UserSearchServiceChangeListener>();
	
	public UserSearchService(String userid, String course) {
		this.userid = userid;
		this.course = course;
	}
	public String getUserId() {return userid;}
	public String getCourse() {return course;}
	
	public synchronized void setParameters() {}
	
	public synchronized void update() throws Exception {
		DocClassifierDaemon daemon = TriUMServer.getInstance().getMiner().getCourseDocClassifierDaemon(course);
		DocClassifier classifier = null;
		if(daemon != null) classifier = daemon.getDocClassifier();
		else {
			classifier = TriUMServer.getInstance().getMiner().addCourseDocClassifierDaemon(course);
		}
		userDoc = new SearchUserDoc(userid, course);
		userDoc.load(classifier.getKeywordPattern());
		docClass = classifier.findDocClassOf(userDoc);
		FireEventUtil.fireEvent(new FireChangeEventTask(new UserSearchServiceChangeEvent(this)), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
	public synchronized String getDocClass() {
		return docClass;
	}
	
	public synchronized void addChangeListener(UserSearchServiceChangeListener listener) {
		listeners.add(listener);
	}
	public synchronized void removeChangeListener(UserSearchServiceChangeListener listener) {
		listeners.remove(listener);
	}
	
	private class FireChangeEventTask implements FireEventTask {
		private UserSearchServiceChangeEvent evt = null; 
		public FireChangeEventTask(UserSearchServiceChangeEvent evt) {
			this.evt = evt;
		}
		public void run() {
			for(UserSearchServiceChangeListener listener : listeners) {
				try {
					listener.searchUserServiceChanged(evt);
				}
				catch(Exception e) {
					ZebraStatic.traceService.trace("SearchUserService.fireChangeEvent causes error: " + e.getMessage());
				}
			}
		}
	}

}
