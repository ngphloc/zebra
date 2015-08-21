package vn.spring.zebra.um;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import es.uco.WOW.TestFile.TestFile;
import es.uco.WOW.Utils.Student;
import es.uco.WOW.Utils.Test;
import es.uco.WOW.Utils.TestLogStudent;
import vn.spring.WOW.WOWDB.ConceptDB;
import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.DotString;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.parser.UMVariableLocator;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.log.UserAccessRecord;
import vn.spring.zebra.um.OverlayBayesUM.OBUM_CONCEPT_NODE_TYPE;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class Evidence {
	public String userid = null;
	public String course = null;
	public String briefName = null;
	public String value = null;
	public OBUM_CONCEPT_NODE_TYPE evidence_type = OBUM_CONCEPT_NODE_TYPE.TEST_EVIDENCE;
	
	public Evidence(String userid, String course, String briefName, String value, 
			OBUM_CONCEPT_NODE_TYPE evidence_type) {
		this.userid = userid;
		this.course = course;
		this.briefName = briefName;
		this.value = value;
		this.evidence_type = evidence_type;
	}

    public static ArrayList<Evidence> getTestResultEvidence(Student student, String course) throws ZebraException {
    	ArrayList<Evidence> evidences = new ArrayList<Evidence>();
    	
		Vector<?> testLogs = student.getTest();
		TestLogStudent foundLog = null;
		String         concept = null;
		for(int i = 0; i < testLogs.size(); i++) {
			TestLogStudent testLog = (TestLogStudent)testLogs.get(i);
			
			String testFileName = testLog.getFileName();
			TestFile testFile = new TestFile(student.getCourse(), testFileName);
			Test test = testFile.getTest();
			String student_course = test.getCourse();
			concept = test.getAbstractConcept();
			if(concept == null) concept = (String)test.getConceptVector().get(0);
			if(!student_course.trim().equals(course)) continue;
			
			foundLog = testLog;
			break;
		}
		if(foundLog == null) return evidences;
		
		DotString ds = new DotString(concept);
		if(ds.size() > 1 && ds.get(0).equals(course)) ds.set(0, null);
		ConceptNodeTypeWrapper type = new ConceptNodeTypeWrapper(OBUM_CONCEPT_NODE_TYPE.TEST_EVIDENCE);
		
		double maxTestCore = (double)OverlayBayesUM.OBUM_DEFAULT_TEST_MAX_KNOWLEDG;
		double maxDefautCore = OverlayBayesUMFactory.getAndCheckNumberOfDefaultVariableValues(type)-1; 
		double score = Math.round((foundLog.getLastScore() / maxTestCore) * maxDefautCore);
		int scoreIdx = ((int)maxDefautCore) - ((int)score);

		String[] values = OverlayBayesUMFactory.createValuesForVar(type);
		Evidence evidence = new Evidence(student.getLogin(), course, ds.toString(), values[scoreIdx], OBUM_CONCEPT_NODE_TYPE.TEST_EVIDENCE);
		
		evidences.add(evidence);
		return evidences;
    }
    
	public static ArrayList<Evidence> getTempEvidences(String userid, String course, OverlayBayesUM um, Date beginDate) 
			throws ZebraException, Exception {
		ArrayList<Evidence> evidences = new ArrayList<Evidence>();
		
		ConceptDB cdb = ZebraStatic.getConceptDB();
		ProfileDB pdb = ZebraStatic.getProfileDB();
		Profile profile = pdb.getProfile(pdb.findProfile(userid));
		UMVariableLocator umvl = new UMVariableLocator(profile, cdb);
	
		List<UserAccessRecord> userLog = ZebraStatic.getLogDB().getUserAccessLog(userid, course, beginDate);
		for(int i = 0; i < userLog.size(); i++) {
			UserAccessRecord record = (UserAccessRecord) (userLog.get(i));
			String cname = record.getConceptName();
			String itemName = new DotString(cname).get(1); 
			OverlayBayesItem item = um.getItem(itemName);
			if(item == null) continue;
			ConceptNodeTypeWrapper type = new ConceptNodeTypeWrapper(item);
			if(!type.isTempEvidence()) continue;
			
			Object v = null;
			try {
				v = umvl.getVariableValue(cname + "." + OverlayBayesUM.OBUM_ROBSERVED);
			}
			catch(Exception e) {
				System.out.println(cname + " is not an evidence");
				v = null;
			}
			if(v == null) continue;
			
			String[] values = item.getValues();
			int index = ((Float)v).intValue(); 
			if(index == -1) {
				System.out.println(cname + " is not an evidence");continue;
			}
			index = ( (index > values.length - 1)? (0) : (values.length - 1 - index) );
			
			String value = values[index];
			evidences.add(new Evidence(userid, course, itemName, value, 
					new ConceptNodeTypeWrapper(item).getType()));
		}
		return evidences;
	}
}
