/**
 * 
 */
package vn.spring.zebra.mail;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.util.BasicDaemon;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseMailingListDaemon extends BasicDaemon implements CourseMailingListListener {
	
	public CourseMailingListDaemon(CourseMailingList mailingList) {
		super(mailingList, 
			"Mailing List daemon started!", ZebraStatic.MAIL_MAILING_LIST_DELAY, ZebraStatic.MAIL_MAILING_LIST_INTERVAL);
		mailingList.addListener(this);
	}

	@Override
	public void task() throws Exception {
		try {
			getCourseMailingList().sendReport(null, null);
		}
		catch(Exception e) {e.printStackTrace();}
	}

	public CourseMailingList getCourseMailingList() {
		return (CourseMailingList)getInnerObject();
	}

	@Override
	public String getCourse() {
		return getCourseMailingList().course;
	}

	@Override
	public String getDaemonName() {
		return "Mailing List Daemon";
	}

	@Override
	public String getUserId() {
		return null;
	}

	public void cleared(CourseMailingListEvent evt) {
		fireMsgListeners("Mailing List daemon clear user list");
	}

	public void mailSent(CourseMailingListEvent evt) {
		fireMsgListeners("Mailing List daemon sent mail to each user");
	}

	public void reportSent(CourseMailingListEvent evt) {
		fireMsgListeners("Mailing List daemon sent report to each user");
	}

	public void userAdded(CourseMailingListEvent evt) {
		fireMsgListeners("Mailing List daemon add user");
	}

	public void userRemoved(CourseMailingListEvent evt) {
		fireMsgListeners("Mailing List daemon remove user");
	}
}
