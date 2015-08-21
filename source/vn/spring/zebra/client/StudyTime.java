/**
 * 
 */
package vn.spring.zebra.client;

import java.io.Serializable;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class StudyTime implements Serializable {
	private static final long serialVersionUID = 1L;

	public long conceptInterval = 0; //milisecond
	public long courseInterval = 0; //milisecond
	public long totalInterval = 0; //milisecond
}
