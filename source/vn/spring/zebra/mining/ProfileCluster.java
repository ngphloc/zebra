/**
 * 
 */
package vn.spring.zebra.mining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import weka.core.Instance;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ProfileCluster {
	public HashMap<String, DistanceProfile> profiles = new HashMap<String, DistanceProfile>();
	public Instance center = null;

	public ArrayList<DistanceProfile> getProfiles() {
		ArrayList<DistanceProfile> profiles = new ArrayList<DistanceProfile>();
		Object[] entries = this.profiles.entrySet().toArray();
		for(int i = 0; i < entries.length; i++) {
			Entry<String, DistanceProfile> entry = (Entry<String, DistanceProfile>)(entries[i]);
			profiles.add(entry.getValue());
		}
		return profiles;
	}
}

