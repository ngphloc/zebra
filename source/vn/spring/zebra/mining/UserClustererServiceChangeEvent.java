package vn.spring.zebra.mining;

import java.util.EventObject;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserClustererServiceChangeEvent extends EventObject {
    private static final long serialVersionUID = 5516075349620653480L;
    
    public enum CHANGE_TYPE {ADD, REMOVE, REMOVEALL}
    
    protected UserClusterer clusterer = null;
    protected CHANGE_TYPE changeType;
    
    public UserClustererServiceChangeEvent(UserClustererService service, UserClusterer clusterer, CHANGE_TYPE changeType) {
    	super(service);
    	this.clusterer = clusterer;
    	this.changeType = changeType;
    }
    public UserClusterer getClusterer() {return clusterer;}
}
