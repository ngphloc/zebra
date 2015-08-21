/**
 * 
 */
package vn.spring.zebra.um;

import java.util.Enumeration;

import vn.spring.WOW.datacomponents.Concept;
import vn.spring.WOW.engine.ConceptInfo;
import vn.spring.zebra.um.OverlayBayesUM.OBUM_CONCEPT_NODE_TYPE;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public final class ConceptNodeTypeWrapper {
	private OBUM_CONCEPT_NODE_TYPE type = OBUM_CONCEPT_NODE_TYPE.NON_EVIDENCE;
	
	public ConceptNodeTypeWrapper(OBUM_CONCEPT_NODE_TYPE type) {
		this.type = type;
	}
	public ConceptNodeTypeWrapper(ConceptNode node) {
		if(node.isEvidence()) {
			if(node.isTempEvidence())
				type = OBUM_CONCEPT_NODE_TYPE.TEMP_EVIDENCE;
			else if(node.isTestEvidence())
				type = OBUM_CONCEPT_NODE_TYPE.TEST_EVIDENCE;
		}
		else type = OBUM_CONCEPT_NODE_TYPE.NON_EVIDENCE;
	}
	public ConceptNodeTypeWrapper(ConceptInfo info) {
		String cType = info.getTypeName();
		if(cType == null || cType.length() == 0)
			type = OBUM_CONCEPT_NODE_TYPE.NON_EVIDENCE;
		else if(cType.toLowerCase().equals("test")) 
			type = OBUM_CONCEPT_NODE_TYPE.TEST_EVIDENCE;
		else if(cType.toLowerCase().equals("evidencetemp") || cType.toLowerCase().equals("tempevidence")) 
			type = OBUM_CONCEPT_NODE_TYPE.TEMP_EVIDENCE;
		else
			type = OBUM_CONCEPT_NODE_TYPE.NON_EVIDENCE;
	}
	public ConceptNodeTypeWrapper(Concept concept) {
		String cType = concept.getType();
		if(cType == null || cType.length() == 0)
			type = OBUM_CONCEPT_NODE_TYPE.NON_EVIDENCE;
		else if(cType.toLowerCase().equals("test")) 
			type = OBUM_CONCEPT_NODE_TYPE.TEST_EVIDENCE;
		else if(cType.toLowerCase().equals("evidencetemp") || cType.toLowerCase().equals("tempevidence")) 
			type = OBUM_CONCEPT_NODE_TYPE.TEMP_EVIDENCE;
		else
			type = OBUM_CONCEPT_NODE_TYPE.NON_EVIDENCE;
	}
	public ConceptNodeTypeWrapper(OverlayBayesItem item) {
		for (Enumeration<?> e = item.getVariableProperties().elements(); e.hasMoreElements(); ) {
			String property = ((String) e.nextElement()).trim();
			
			// If property is not position, skip it
			if (!property.startsWith("type")) continue;
			String[] pair = property.split("=");
			if(pair == null || pair.length != 2) continue;
			pair[1] = pair[1].trim();
			
			if(pair[1].equals("evidencetemp") || pair[1].equals("tempevidence"))
				type = OBUM_CONCEPT_NODE_TYPE.TEMP_EVIDENCE;
			else if(pair[1].equals("test") || pair[1].equals("test"))
				type = OBUM_CONCEPT_NODE_TYPE.TEST_EVIDENCE;
			else
				type = OBUM_CONCEPT_NODE_TYPE.NON_EVIDENCE;
			return;
		}
		type = OBUM_CONCEPT_NODE_TYPE.NON_EVIDENCE;
	}
	public boolean isEvidence() {
		return (type == OBUM_CONCEPT_NODE_TYPE.TEMP_EVIDENCE ||
				type == OBUM_CONCEPT_NODE_TYPE.TEST_EVIDENCE);
	}
	public boolean isTestEvidence() {
		return (type == OBUM_CONCEPT_NODE_TYPE.TEST_EVIDENCE);
	}
	public boolean isTempEvidence() {
		return (type == OBUM_CONCEPT_NODE_TYPE.TEMP_EVIDENCE);
	}
	public OBUM_CONCEPT_NODE_TYPE getType() {
		return type;
	}
}
