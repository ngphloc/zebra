package vn.spring.zebra.server;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public interface TriUMServerChangeUMListener {
	void umRegistered(TriUMServerChangeUMEvent evt);
	void umRegisteredDefault(TriUMServerChangeUMEvent evt);
	void umUnregistered(TriUMServerChangeUMEvent evt);
	void umUnregisteredAll(TriUMServerChangeUMEvent evt);
	void umMonitored(TriUMServerChangeUMEvent evt);
	void umMonitoredCourse(TriUMServerChangeUMEvent evt);
	void umUnmonitored(TriUMServerChangeUMEvent evt);
	void umUnmonitoredCourse(TriUMServerChangeUMEvent evt);
	void umUnmonitoredAll(TriUMServerChangeUMEvent evt);
}
