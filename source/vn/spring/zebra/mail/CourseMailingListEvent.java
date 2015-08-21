/**
 * 
 */
package vn.spring.zebra.mail;

import java.util.EventObject;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseMailingListEvent extends EventObject {
	private static final long serialVersionUID = 1L;
    public enum TYPE {SENDMAIL, SENDREPORT, ADDUSER, REMOVEUSER, CLEAR}
	
    protected TYPE type = TYPE.SENDMAIL;
    
	public CourseMailingListEvent(CourseMailingList service, TYPE type) {
		super(service);
		this.type = type;
	}
}
