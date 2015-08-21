/**
 * 
 */
package vn.spring.zebra.mining;

import java.util.ArrayList;

import vn.spring.WOW.engine.ConceptInfo;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.util.ConceptUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public final class MinerUtil {
	public static CourseAccessApriori createCourseRecommender(String course) throws Exception {
		CourseAccessApriori apriori = new CourseAccessApriori();
		ArrayList<ConceptInfo> filtered = ConceptUtil.getConceptInfos(course, ZebraStatic.RECOMMEND_DEEP_LEVEL, true);
		apriori.setParameters(null, course, filtered, ZebraStatic.RECOMMEND_MIN_SUP);
		apriori.perform();
		return apriori;
	}
	public static CourseAccessSequences2 createCourseSequence(String course) throws Exception {
		CourseAccessSequences2 sequences = new CourseAccessSequences2();
		ArrayList<ConceptInfo> filtered = ConceptUtil.getConceptInfos(course, ZebraStatic.SEQUENCE_DEEP_LEVEL, true);

		sequences.setParameters(null, course, filtered, ZebraStatic.SEQUENCE_MIN_SUP);
		sequences.perform();
		return sequences;
	}

}
