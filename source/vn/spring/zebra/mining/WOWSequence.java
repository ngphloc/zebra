package vn.spring.zebra.mining;

import weka.associations.gsp.Sequence;
import weka.core.FastVector;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class WOWSequence extends Sequence {
	private static final long serialVersionUID = 1L;
	
	public WOWSequence() {
		super();
	}
	public FastVector getElements() {
		return this.m_Elements;
	}
}
