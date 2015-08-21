package vn.spring.zebra.tmp;

import java.util.Date;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseBayesianNetworkState extends CourseBayesianNetwork {

	private Date time_stamp = new Date();
	
	public Date getTimeStamp() {
		return time_stamp;
	}
	
	private CourseBayesianNetworkState() {
	}

	public static CourseBayesianNetworkState create(String course, String author_uri) {
		CourseBayesianNetworkState net_state = new CourseBayesianNetworkState();
		net_state.loadAuthor(course, author_uri);
		return net_state;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
