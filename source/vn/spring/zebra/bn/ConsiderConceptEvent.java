package vn.spring.zebra.bn;

import java.util.EventObject;

import vn.spring.WOW.datacomponents.Profile;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public class ConsiderConceptEvent extends EventObject {
    private static final long serialVersionUID = 5516075349620653480L;
    
    protected String concept = null;

    public ConsiderConceptEvent(Profile profile, String concept) {
    	super(profile);
    	this.concept = concept;
    }

	public Profile getProfile() {return (Profile)getSource();}
	public String getConcept() {return concept;}
}
