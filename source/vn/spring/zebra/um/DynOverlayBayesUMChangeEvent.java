package vn.spring.zebra.um;

import java.util.EventObject;

import vn.spring.zebra.um.DynOverlayBayesUM.TOverlayBayesUM;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class DynOverlayBayesUMChangeEvent extends EventObject {
    private static final long serialVersionUID = 5516075349620653480L;

    public enum CHANGE_TYPE {ADD, CLEAR, RELOAD}
    
    protected TOverlayBayesUM state = null;
    protected CHANGE_TYPE changeType;
    
    public DynOverlayBayesUMChangeEvent(DynOverlayBayesUM um, TOverlayBayesUM state, CHANGE_TYPE changeType) {
    	super(um);
    	this.state = state;
    	this.changeType = changeType;
    }
    public TOverlayBayesUM getState() {return state;}
}
