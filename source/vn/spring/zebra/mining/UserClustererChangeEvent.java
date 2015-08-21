package vn.spring.zebra.mining;

import java.util.ArrayList;
import java.util.EventObject;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public class UserClustererChangeEvent extends EventObject {
    private static final long serialVersionUID = 5516075349620653480L;

    protected UserClusterer userClusterer = null;
    protected ArrayList<ProfileCluster> clusters = null;
    protected String arff = null;
    
    public UserClustererChangeEvent(UserClusterer userClusterer, ArrayList<ProfileCluster> clusters, String arff) {
    	super(userClusterer);
    	
    	this.clusters = clusters;
    	this.arff = arff;
    }
	public  String                    getArff() {return arff;}
	public ArrayList<ProfileCluster>  getClusterers() {return clusters;}
}
