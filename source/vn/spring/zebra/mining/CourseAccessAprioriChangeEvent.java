package vn.spring.zebra.mining;

import java.util.ArrayList;
import java.util.EventObject;

import vn.spring.WOW.engine.ConceptInfo;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public class CourseAccessAprioriChangeEvent extends EventObject {
    private static final long serialVersionUID = 5516075349620653480L;

    protected ArrayList<ConceptInfo> filtered = new ArrayList<ConceptInfo>();
    protected String arff = null;
    protected ArrayList<AssociationRule> rules = new ArrayList<AssociationRule>();
    
    public CourseAccessAprioriChangeEvent(CourseAccessApriori apriori, 
    		ArrayList<ConceptInfo> filtered, String arff, ArrayList<AssociationRule> rules) {
    	super(apriori);
    	
    	this.filtered = filtered;
    	this.arff = arff;
    	this.rules = rules;
    }
	public  String                    getArff() {return arff;}
	public ArrayList<ConceptInfo>     getFiltered() {return filtered;}
	public ArrayList<AssociationRule> getAssociationRules() {return rules;}    
}
