package vn.spring.zebra.tmp;

import java.util.ArrayList;
import java.util.Date;

public class DynamicCourseBayesianNetworks {
	ArrayList<CourseBayesianNetworkState> nets = new ArrayList<CourseBayesianNetworkState>();
	double guess_rate = 0.5;
	double slip_rate = 0.5;
	
	public CourseBayesianNetworkState getNetState(int idx) {
		return nets.get(idx);
	}
	public int findState(Date time_stamp) {
		int n = nets.size();
		for(int i=0; i<n; i++) {
			CourseBayesianNetworkState state = nets.get(i);
			if(state.getTimeStamp().compareTo(time_stamp) == 0)
				return i;
		}
		return -1;
	}
	
	public DynamicCourseBayesianNetworks() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
