/**
 * 
 */
package vn.spring.zebra.mining;

import java.util.EventObject;

import vn.spring.zebra.mining.textmining.DocClassifier;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class DocClassifierChangeEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	public DocClassifierChangeEvent(DocClassifier source) {
		super(source);
	}

}
