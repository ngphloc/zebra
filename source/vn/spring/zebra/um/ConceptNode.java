package vn.spring.zebra.um;

import java.util.ArrayList;
import vn.spring.WOW.graphauthor.author.WOWOutConcept;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ConceptNode implements Comparable<ConceptNode>{
	public class NWeight {
		public ConceptNode node;
		public double      weight;
		public NWeight(ConceptNode node, double weight) {
			this.node = node;
			this.weight = weight;
		}
	}
	//public Concept       concept = null;
	public WOWOutConcept       concept = null;
	public ConceptNodeAttr     attr = null;
	public ArrayList<NWeight>  parents = null;
	public ArrayList<NWeight>  childs = null;
	
	public int compareTo(ConceptNode cNode) {
		return getName().compareTo(cNode.getName());
	}
	public String getName() {
		//return concept.getName().substring(concept.getName().indexOf(".")+1);
		return concept.name;
	}
	public boolean isEvidence() {
		return ( isTestEvidence() || isTempEvidence());
	}
	public boolean isTestEvidence() {
		return concept.concepttype.toLowerCase().equals("test");
	}
	public boolean isTempEvidence() {
		return concept.concepttype.toLowerCase().equals("evidencetemp") || 
			concept.concepttype.toLowerCase().equals("tempevidence");
	}
	public String getConceptType() {
		return concept.concepttype;
	}
}
