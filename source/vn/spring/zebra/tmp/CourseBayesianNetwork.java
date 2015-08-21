package vn.spring.zebra.tmp;

import vn.spring.bayes.BayesianNetworks.*;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;


import java.util.*;
import vn.spring.zebra.math.BinaryArrangementWithRepetitionGenerator;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseBayesianNetwork extends BayesNet {
	protected int     evidenceLevel = 3; // di tu 0->evidenceLevel-1 nen evidenceLevel co gia tri it nhat la 3
	protected String  course = "";
	
	private Hashtable<String, ConceptVariable> baseCluster = new Hashtable<String, ConceptVariable>();//chi di
	private Hashtable<String, ConceptVariable> middleCluster = new Hashtable<String, ConceptVariable>();//co den co di
	private Hashtable<String, ConceptVariable> middleJustInCluster = new Hashtable<String, ConceptVariable>(); //chi den, khong di
	private Hashtable<String, ConceptVariable> topCluster = new Hashtable<String, ConceptVariable>();//chi den, neu co di thi di vao evidence
	private Hashtable<String, ConceptVariable> evidenceCluster = new Hashtable<String, ConceptVariable>();
	private Hashtable<String, ConceptVariable> belowEvidenceCluster = new Hashtable<String, ConceptVariable>();

	private ArrayList<Rel> rels = new ArrayList<Rel>();
	private ArrayList<Rel> ev_rels = new ArrayList<Rel>();
	private ArrayList<Rel> belowev_rels = belowEvidenceCluster==null?null:new ArrayList<Rel>();
	
	public int      getEvidenceLevel() {return evidenceLevel;}
	public String   getCourse()              {return course;}
	
	public ArrayList<ConceptVariable> getBaseCluster() {
		if(baseCluster.size() == 0) return null;
		return new ArrayList<ConceptVariable>(baseCluster.values());
	}
	public ArrayList<ConceptVariable> getMiddleCluster() {
		if(middleCluster.size() == 0) return null;
		return new ArrayList<ConceptVariable>(middleCluster.values());
	}
	public ArrayList<ConceptVariable> getMiddleJustInCluster() {
		if(middleJustInCluster.size() == 0) return null;
		return new ArrayList<ConceptVariable>(middleJustInCluster.values());
	}
	public ArrayList<ConceptVariable> getTopCluster() {
		if(topCluster.size() == 0) return null;
		return new ArrayList<ConceptVariable>(topCluster.values());
	}
	public ArrayList<ConceptVariable> getEvidenceCluster() {
		if(evidenceCluster.size() == 0) return null;
		return new ArrayList<ConceptVariable>(evidenceCluster.values());
	}
	public ArrayList<ConceptVariable> getBelowEvidenceCluster() {
		if(belowEvidenceCluster.size() == 0) return null;
		return new ArrayList<ConceptVariable>(belowEvidenceCluster.values());
	}
	
	public int findRel(String rel_name) {
		return -1;
	}
	public Rel getRel(int idx) {
		return rels.get(idx);
	}
	public int getNumRels() {
		return rels.size();
	}

	public int findEvidenceRel(String evrel_name) {
		return -1;
	}
	public Rel getEvidenceRel(int idx) {
		return ev_rels.get(idx);
	}
	public int getNumEvidenceRels() {
		return ev_rels.size();
	}

	public int findBelowEvidenceRel(String belowevrel_name) {
		return -1;
	}
	public Rel getBelowEvidenceRel(int idx) {
		return belowev_rels.get(idx);
	}
	public int getNumBelowEvidenceRels() {
		return belowev_rels.size();
	}

	
	public ConceptVariable getBaseClusterVar(String var_name) {
		if(baseCluster.size() == 0) return null;
		return (ConceptVariable) (baseCluster.get(var_name));
	}
	public ConceptVariable getMiddleClusterVar(String var_name) {
		if(middleCluster.size() == 0) return null;
		return middleCluster.get(var_name);
	}
	public ConceptVariable getMiddleJustInClusterVar(String var_name) {
		if(middleJustInCluster.size() == 0) return null;
		return middleJustInCluster.get(var_name);
	}
	public ConceptVariable getTopClusterVar(String var_name) {
		if(topCluster.size() == 0) return null;
		return topCluster.get(var_name);
	}
	public ConceptVariable getEvidenceClusterVar(String var_name) {
		if(evidenceCluster.size() == 0) return null;
		return evidenceCluster.get(var_name);
	}
	public ConceptVariable getBelowEvidenceClusterVar(String var_name) {
		if(belowEvidenceCluster.size() == 0) return null;
		return belowEvidenceCluster.get(var_name);
	}
	
	public CourseBayesianNetwork() {
		super();
	}
		
	public void loadAuthor(String course, String author_uri) {
		this.course = course;

		DOMParser parser = new DOMParser();
        try {
        	parser.parse(author_uri);
        }
		catch (Exception e) {
			System.out.println("CourseBayesianNetwork: Parse error!  file: " + author_uri);
		}
		
        Document doc = parser.getDocument();
        Node     top = doc.getDocumentElement();
        NodeList mlist = top.getChildNodes();

        //doc de lay ra ds concept va quan he
        Node rel_node = null;
        Node c_node = null;
        for (int c = 0; c < mlist.getLength(); c++) {
        	Node m = mlist.item(c);
            if (m.getNodeName().equals("concept_relations")) {
            	rel_node = m;
            }

            if (m.getNodeName().equals("concept_information")) {
            	c_node = m;
            }
            if( (c_node != null) && (rel_node != null) ) break;
        }
        
        //ds concept
        Hashtable<String, ConceptVariable> tempvars  = new Hashtable<String, ConceptVariable>();
        NodeList  nlist = c_node.getChildNodes(); // ds concept_info
        for (int i = 0; i < nlist.getLength(); i++) {
        	Node n = nlist.item(i); // tung concept_info
            if (n.getNodeName().toString().equals("concept_info")) {
            	NodeList olist = n.getChildNodes(); //ds nut con cua concept_info
            	String var_name = null;
            	int    var_level = 0;
            	for(int j = 0; j < olist.getLength(); j ++) {
            		Node   o = olist.item(j); // tung nut con cua concept_info
            		String node_name = o.getNodeName().toString();
            		if(var_name == null) {
	            		if(node_name.equals("concept_name")) {
	                    	var_name = o.getFirstChild().getNodeValue();
	            		}
            		}
            		else {
            			if(node_name.equals("attribute_information")) {
	            			NodeList plist = o.getChildNodes(); // ds attribute_info
	            			Node     level_node = null;
	            			for(int k = 0; k < plist.getLength(); k++) {
	    						Node node = plist.item(k);// tung attribute_info
	    						if(node.getFirstChild() == null) continue;
	    						if(node.getFirstChild().getNodeName().equals("level")) {
	    							level_node = node;
	    							break;
	    						}
	            			}
	            			if(level_node != null) {
	        					NodeList level_list = level_node.getChildNodes();
	        					for(int l=0; l < level_list.getLength(); l++) {
	        						Node node = level_list.item(l);
	        						if(node.getNodeName().equals("attribute_default")) {
	        							var_level = Integer.parseInt(node.getFirstChild().getNodeValue());
	        							break;
	        						}
	        					}
	            			}
	            			break; // dung den nut attribute_information la het
            			}
            		}
            		
            	}
            	if(var_name != null) {
            		ConceptVariable var = new ConceptVariable(this, course + "." + var_name,
            				ProbabilityVariable.CHANCE, new String[] {  "True" , "False" });
            		var.setLevel(var_level);
            		tempvars.put(course + "." + var_name, var);
            	}
            	
            }
        }

        //ds quan he
        nlist = rel_node.getChildNodes();
        ArrayList<Rel>    temp_rels = new ArrayList<Rel>();
        for(int i = 0; i < nlist.getLength(); i++) {
        	Node   n = nlist.item(i); // ds concept_relation
        	if(n.getNodeName().equals("#text")) continue;
        	
        	String source_concept_name = null;
        	String destination_concept_name = null;
        	String relation_type = null;
        	//String relation_label = null;
        	double relation_weight = 0.0;
        	
        	NodeList olist = n.getChildNodes();
        	for(int j=0; j<olist.getLength(); j++) {
        		Node o = olist.item(j);
        		if(o.getFirstChild() == null) continue;
        		
        		if(o.getNodeName().equals("source_concept_name")) {
                	source_concept_name = course + "." + o.getFirstChild().getNodeValue();
        		}
        		else if(o.getNodeName().equals("destination_concept_name")) {
        			destination_concept_name = course + "." + o.getFirstChild().getNodeValue();
        		}
        		else if(o.getNodeName().equals("relation_type")) {
        			relation_type = o.getFirstChild().getNodeValue();
        		}
        		//else if(o.getNodeName().equals("relation_label")) {
        		//	relation_label = o.getFirstChild().getNodeValue();
        		//}
        		else if(o.getNodeName().equals("relation_weight")) {
        			relation_weight = Double.parseDouble(o.getFirstChild().getNodeValue());
        		}
        	}
        	
        	if(relation_type.compareTo("prerequisite") != 0) continue; //khong dung nhung quan he khac
        	
        	ConceptVariable parent_var = tempvars.get(source_concept_name);
        	ConceptVariable child_var = tempvars.get(destination_concept_name);
        	
        	if(parent_var.getLevel() == evidenceLevel) { //evidence
        		if(child_var.getLevel() == evidenceLevel) {
        	    	if(belowEvidenceCluster==null) continue; //neu khong luu nhung nut evidence thi khong quan tam moi quan he nay
        	    	
        			ConceptVariable t = parent_var; parent_var = child_var; child_var = t;//hoan vi
    	        	
        			parent_var.addChild(child_var, relation_weight);
    	        	child_var.addParent(parent_var, relation_weight);
   	        		
    	        	parent_var.setEvidence(true);
    	        	child_var.setEvidence(true);
        		}
        		else {
        			ConceptVariable t = parent_var; parent_var = child_var; child_var = t;//hoan vi

        			parent_var.addEvidence(child_var, relation_weight);
		        	child_var.addParent(parent_var, relation_weight);
		        	child_var.setEvidence(true);
        		}
        	}
        	else {
	        	parent_var.addChild(child_var, relation_weight);
	        	child_var.addParent(parent_var, relation_weight);
        	}
        	
        	Rel rel = new Rel(parent_var, child_var);
        	temp_rels.add(rel);
        }
        
    	baseCluster.clear();
    	middleCluster.clear();
    	middleJustInCluster.clear();
    	topCluster.clear();
    	evidenceCluster.clear();
    	if(belowEvidenceCluster!=null) belowEvidenceCluster.clear();
    	
    	rels.clear();
    	ev_rels.clear();
    	if(belowEvidenceCluster!=null) belowev_rels.clear();
        for (int i = 0; i < temp_rels.size(); i++) {
        	Rel rel = temp_rels.get(i);
        	ConceptVariable parent_var = rel.parent_var;     String parent_name = parent_var.get_name();
        	ConceptVariable child_var   = rel.child_var;     String child_name   = parent_var.get_name();

        	if (parent_var.isEvidence()) { // evidence
        		if(parent_var.hasParentEvidence()) {
            		if(!belowEvidenceCluster.containsKey(parent_name))  belowEvidenceCluster.put(parent_name, parent_var);
            		if(!belowev_rels.contains(rel)) belowev_rels.add(rel);
        		}
        		else {
            		if(!evidenceCluster.containsKey(parent_name))  evidenceCluster.put(parent_name, parent_var);
            		if(!ev_rels.contains(rel)) ev_rels.add(rel);
        		}

        		if(!belowEvidenceCluster.containsKey(child_name))  belowEvidenceCluster.put(child_name, child_var);
        	}
        	else {
        		if(parent_var.getNumParents() != 0) {// co cha
        			if(parent_var.getNumChilds() != 0) { //co con
        				if(!middleCluster.containsKey(parent_name)) middleCluster.put(parent_name, parent_var);
        			}
        			else {
        				if(!middleJustInCluster.containsKey(parent_name)) middleJustInCluster.put(parent_name, parent_var);
        			}
        		}
        		else { //khong cha
    				if(!topCluster.containsKey(parent_name)) topCluster.put(parent_name, parent_var);
        		}
        		
        		if (child_var.isEvidence()) {
            		if(!evidenceCluster.containsKey(child_name))  evidenceCluster.put(child_name, child_var);
            		if(!ev_rels.contains(rel)) ev_rels.add(rel);
        		}
        		else {
        			if(child_var.getNumChilds() != 0) {
        				if(!middleCluster.containsKey(child_name)) middleCluster.put(child_name, child_var);
        			}
        			else {
        				if(!middleJustInCluster.containsKey(child_name)) middleJustInCluster.put(child_name, child_var);
        			}
            		if(!rels.contains(rel)) rels.add(rel);
        		}
        		
        	}
        }
        
        ArrayList<DiscreteFunction> fs  = new ArrayList<DiscreteFunction>();
        
        ArrayList<ConceptVariable> hasProbDistVars = new ArrayList<ConceptVariable>();
        hasProbDistVars.addAll(baseCluster.values());
        hasProbDistVars.addAll(middleCluster.values());
        hasProbDistVars.addAll(middleJustInCluster.values());
        hasProbDistVars.addAll(topCluster.values());
        hasProbDistVars.addAll(evidenceCluster.values());
                
        for(int i=0 ; i<hasProbDistVars.size(); i++) {
        	ConceptVariable var = (ConceptVariable) (hasProbDistVars.get(i));
        	if(var.getNumParents() != 0) {
	        	int      r = var.getNumParents();
	        	int      stats = (int) (Math.pow(2, r + 1));
	        	
	        	double[] probs = new double [stats];
	        	BinaryArrangementWithRepetitionGenerator arr = BinaryArrangementWithRepetitionGenerator.parse(2, r);
	        	for(int j=0; j<arr.getNumArrangement(); j++) {
	        		int[] a = arr.getArrangement(j);
	        		probs[j] = 0.0;
	        		for(int k=0; k<r; k++)
	        			probs[j] += ( (double)(a[k]) ) * ( var.getParentWeight(k) );
	        	}
	        	
	        	for(int j=stats/2; j < stats; j++)
	        		probs[j] = probs[j] = 1 - probs[j-stats/2];
	        	
	        	ProbabilityFunction f =
	                new ProbabilityFunction((BayesNet)this, var.toDiscreteVariableArray(), probs, null);
	            fs.add(f);
        	}
        	else {
        		ProbabilityFunction f =
                    new ProbabilityFunction((BayesNet)this, var.toDiscreteVariableArray(), new double[] {0.5, 0.5}, null);
                fs.add(f);
        	}
        }
        
       
        //Cai dat vao mang Bayesian
        ProbabilityVariable[] aVars = new ProbabilityVariable[hasProbDistVars.size()];
        hasProbDistVars.toArray(aVars);
        ProbabilityFunction[] aFs = new ProbabilityFunction[fs.size()];
        fs.toArray(aFs);
        
        name = "CourseBayesianNetwork";
        properties = null;
        probability_variables = aVars;
        probability_functions = aFs;
		process_properties();
		for (int i=0; i<probability_variables.length; i++)
			process_probability_variable_properties(i);
        for (int i=0; i<probability_functions.length; i++)
        	process_probability_function_properties(i);
        
        
	}
	
	private class Rel {
		private ConceptVariable parent_var = null;
		private ConceptVariable child_var = null;

		public Rel(ConceptVariable parent_var, ConceptVariable child_var) {
			this.parent_var = parent_var;
			this.child_var   = child_var;
		}
		public void reverse() {
			ConceptVariable tmp = parent_var;
			parent_var = child_var;
			child_var = tmp;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CourseBayesianNetwork courseBayes = new CourseBayesianNetwork();
		courseBayes.loadAuthor("tutorial", "W:/wow/author/authorfiles/author/tutorial.gaf");
		
		courseBayes.toString();
	}

}
