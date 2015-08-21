package vn.spring.zebra.bn;

import java.util.EventObject;

import vn.spring.zebra.client.LearningStyle;
import vn.spring.zebra.um.LearningStyleModel;
import vn.spring.zebra.um.HiddenMarkovUM.OptimalStateChain;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public class LSChangeEvent extends EventObject {
    private static final long serialVersionUID = 5516075349620653480L;
    
    protected LearningStyle ls = null;

    protected OptimalStateChain verbal_visual = null;
    protected OptimalStateChain active_reflective = null;
    protected OptimalStateChain theorist_pragmatist = null;
    
    public LSChangeEvent(LearningStyleModel lshmm, 
    		LearningStyle ls,
    		OptimalStateChain verbal_visual, OptimalStateChain active_reflective, OptimalStateChain theorist_pragmatist) {
    	super(lshmm);
    	this.ls = ls;
    	this.verbal_visual = verbal_visual;
    	this.active_reflective = active_reflective;
    	this.theorist_pragmatist = theorist_pragmatist;
    }

    public LearningStyle getLearningStyle() {return ls;}
	public OptimalStateChain getVerbalVisualChain() {
		return verbal_visual;
	}
	public OptimalStateChain getActiveReflectiveChain() {
		return active_reflective;
	}
	public OptimalStateChain getTheoristPragmatistChain() {
		return theorist_pragmatist;
	}
    
}
