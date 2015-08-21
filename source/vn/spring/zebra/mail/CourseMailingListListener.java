/**
 * 
 */
package vn.spring.zebra.mail;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public interface CourseMailingListListener {
	void mailSent(CourseMailingListEvent evt);
	void reportSent(CourseMailingListEvent evt);
	void userAdded(CourseMailingListEvent evt);
	void userRemoved(CourseMailingListEvent evt);
	void cleared(CourseMailingListEvent evt);
}
