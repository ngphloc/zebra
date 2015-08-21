/**
 * 
 */
package vn.spring.zebra.search;

import java.util.EventObject;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserSearchServiceChangeEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	public UserSearchServiceChangeEvent(UserSearchService service) {
		super(service);
	}
}
