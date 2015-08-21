package vn.spring.zebra.um;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ConceptEdge {
	public ConceptNode source = null;
	public ConceptNode dest = null;
	public double      weight = 0.0;
	
	public ConceptEdge(ConceptNode source, ConceptNode dest, double weight) {
		this.source = source;
		this.dest = dest;
		this.weight = weight  ;
	}
}
