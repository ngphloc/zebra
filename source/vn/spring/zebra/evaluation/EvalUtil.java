/**
 * 
 */
package vn.spring.zebra.evaluation;

import es.uco.WOW.StudentFile.StudentFile;
import es.uco.WOW.Utils.Student;
import vn.spring.WOW.datacomponents.DotString;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class EvalUtil {
	public final static String EXECUTION_TYPE = "adaptive";
	
	public static Student getStudentFinishedTest(String userid, String course, String concept) {
		StudentFile studentFile = new StudentFile(course, userid);
		return studentFile.getStudentFinishedTest(getTestFileName(concept));
	}
	public static Student getStudentNotFinishedTest(String userid, String course, String concept) {
		StudentFile studentFile = new StudentFile(course, userid);
		return studentFile.getStudentNotFinishedTest(getTestFileName(concept), EXECUTION_TYPE);
	}
	
	public static String getTestFileName(String concept) {
		DotString ds = new DotString(concept);
		if(ds.size() != 2) return null;
		return ds.get(1) + "_" + ds.get(0) + "_A_activity";
	}
}
