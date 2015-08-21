/**
 * 
 */
package vn.spring.zebra.client;

import java.rmi.Naming;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMQueryClientUtil {
	
	public final static TriUMQuery getTriUMQuery(String rmiHost, int rmiPort) {
		try {
			//if (System.getSecurityManager() == null) {System.setSecurityManager(new RMISecurityManager());}
			String rmiQueryURL = "rmi://" + rmiHost + ":" + String.valueOf(rmiPort) + "/" + TriUMQuery.RMI_QUERY_NAME;
			return (TriUMQuery) Naming.lookup(rmiQueryURL);
		}
		catch(Exception e) {
			System.out.println("TriUMQueryClientUtil.getTriUMQuery causes error " + e.getMessage());
			return null;
		}
	}
}
