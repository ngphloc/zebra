package vn.spring.zebra.evaluation;

import vn.spring.zebra.exceptions.ZebraException;


/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public interface TestEngineListener {
	void testEvaluated(TestEngineEvent evt) throws ZebraException ;
}
