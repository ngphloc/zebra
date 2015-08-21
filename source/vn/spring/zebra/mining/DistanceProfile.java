/**
 * 
 */
package vn.spring.zebra.mining;

import vn.spring.WOW.datacomponents.Profile;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class DistanceProfile implements Comparable<DistanceProfile> {
	public String userid = null;
	public Profile profile = null;
	public double dis = 0;
	
	public DistanceProfile(String userid, Profile profile, double dis) {
		this.userid = userid;
		this.profile = profile;
		this.dis = dis;
	}
	public int compareTo(DistanceProfile o) { // lon truoc, nho sau
		if(dis < o.dis) return 1;
		else if(dis == o.dis) return 0;
		else return -1;
	}
}
