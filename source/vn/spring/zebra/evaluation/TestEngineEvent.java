package vn.spring.zebra.evaluation;

import java.util.ArrayList;
import java.util.EventObject;

import vn.spring.WOW.datacomponents.DotString;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.um.ConceptNodeTypeWrapper;
import vn.spring.zebra.um.Evidence;
import vn.spring.zebra.um.OverlayBayesUM;
import vn.spring.zebra.um.OverlayBayesUMFactory;
import vn.spring.zebra.um.OverlayBayesUM.OBUM_CONCEPT_NODE_TYPE;

import es.uco.WOW.Utils.Student;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public class TestEngineEvent extends EventObject {
    private static final long serialVersionUID = 5516075349620653480L;
    private Student student = null;
    
    public TestEngineEvent(Student student, String course) throws ZebraException {
    	super(new Object());
    	ArrayList<Evidence> evidences = Evidence.getTestResultEvidence(student, course);
    	this.source = evidences.get(0);
    	this.student = student;
    }
    
    public TestEngineEvent(String userid, String course, String concept, double testScore) throws ZebraException {
    	super(new Object());
		DotString ds = new DotString(concept);
		if(ds.size() > 1 && ds.get(0).equals(course)) ds.set(0, null);
		String briefConceptName = ds.toString();
		
		ConceptNodeTypeWrapper type = new ConceptNodeTypeWrapper(OBUM_CONCEPT_NODE_TYPE.TEST_EVIDENCE);
		double maxTestCore = (double)OverlayBayesUM.OBUM_DEFAULT_TEST_MAX_KNOWLEDG;
		double maxDefautCore = OverlayBayesUMFactory.getAndCheckNumberOfDefaultVariableValues(type)-1; 
		double score = Math.round((testScore / maxTestCore) * maxDefautCore);
		int scoreIdx = ((int)maxDefautCore) - ((int)score);

		String[] values = OverlayBayesUMFactory.createValuesForVar(type);
		
		this.source = new Evidence(
				userid, course, briefConceptName, values[scoreIdx], OBUM_CONCEPT_NODE_TYPE.TEST_EVIDENCE);
    }
    
	public Student   getStudent() {return student;}
	public Evidence  getEvidence() {return (Evidence)getSource();}
}
