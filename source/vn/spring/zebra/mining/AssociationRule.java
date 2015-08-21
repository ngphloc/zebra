package vn.spring.zebra.mining;

import java.io.Serializable;
import java.util.ArrayList;

import vn.spring.WOW.WOWDB.ConceptDB;
import vn.spring.WOW.datacomponents.Concept;
import vn.spring.zebra.ZebraStatic;
import weka.associations.ItemSet;
import weka.core.Instances;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class AssociationRule implements Serializable {
	private static final long serialVersionUID = 1L;

	private Instances instances = null;
	private ItemSet left = null;
	private ItemSet right = null;
	private double confidence = 0.0;
	
	public AssociationRule(Instances instances, ItemSet left, ItemSet right, double confidence) {
		this.instances = instances;
		this.left = left;
		this.right = right;
		this.confidence = confidence;
	}
	public String toString() {
		String strLeft = left.toString2(instances);
		String strRight = right.toString2(instances);
		
		return strLeft + " ==> " + strRight + "    conf:("+  confidence + ")";		
	}
	public String toString2() {
		ConceptDB cdb = ZebraStatic.getConceptDB();
		try {
			ArrayList<String> lefts = left.getItemNames(instances);
			String strLeft = "";
			for(int i = 0; i < lefts.size(); i++) {
				Concept concept = cdb.getConcept(cdb.findConcept(lefts.get(i)));
				strLeft += concept.getTitle();
				if(i < lefts.size() - 1) strLeft += ", ";
			}
			
			ArrayList<String> rights = right.getItemNames(instances);
			String strRight = "";
			for(int i = 0; i < rights.size(); i++) {
				Concept concept = cdb.getConcept(cdb.findConcept(rights.get(i)));
				strRight += concept.getTitle();
				if(i < rights.size() - 1) strRight += ", ";
			}
			
			return strLeft + " ==> " + strRight;
		}
		catch(Exception e) {
			e.printStackTrace();
			return "Error!";
		}
	}
	public ItemSet left() {return left;}
	public ItemSet right() {return right;}
	public double confidence() {return confidence;}
	public Instances getInstances() {return instances;}
}
