package vn.spring.zebra.um;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import vn.spring.WOW.graphauthor.author.*;
import vn.spring.WOW.graphauthor.GraphAuthor.*;
import vn.spring.zebra.exceptions.ZebraException;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ConceptGraph {
	private String                   name = null;
	private ArrayList<ConceptNode>   cNodes = null;
	private ArrayList<ConceptEdge>   cEdges = null;
	
	private ConceptGraph() {}
	
	public String getName() {return name;};
	
	public int getConcepNodeIndex(String concept_name) {
		int n = cNodes.size();
		for(int i=0; i<n; i++) {
			if(cNodes.get(i).getName().equals(concept_name)) {
				return i;
			}
		}
		return -1;
	}
	public ConceptNode getConceptNode(int idx) {
		return cNodes.get(idx);
	}
	public int getNumConceptNode() {
		return cNodes.size();
	}
	
	public ArrayList<ConceptNode> getRootConceptNodes() throws ZebraException {
		ArrayList<ConceptNode> roots = new ArrayList<ConceptNode>();
		int n = cNodes.size();
		for(int i=0; i<n; i++) {
			if(cNodes.get(i).parents.size()==0) roots.add(cNodes.get(i));
		}
		if(roots.size()==0) throw new ZebraException("There must have root nodes");
		return roots;
	}
	
	public int getConcepEdgeIndex(String source_name, String dest_name) {
		int n = cEdges.size();
		for(int i=0; i<n; i++) {
			if(cEdges.get(i).source.getName().equals(source_name) && 
			   cEdges.get(i).dest.getName().equals(dest_name)) {
				return i;
			}
		}
		return -1;
	}
	public ConceptEdge getConceptEdge(int idx) {
		return cEdges.get(idx);
	}
	public int getNumConceptEdge() {
		return cEdges.size();
	}
	
	public static ConceptGraph createFromAuthorFile(URL base, String author, String course, ArrayList<String> filteredRelationTypes) throws ZebraException, MalformedURLException, Exception {
		AuthorCourseModel courseModel = AuthorCourseModel.loadFromAuthorFile(base, author, course, filteredRelationTypes);
		return createFromAuthorCourseModel(courseModel);
	}

	private static ConceptGraph createFromAuthorCourseModel(AuthorCourseModel courseModel) throws ZebraException, Exception {
		LinkedList               rels = courseModel.getRelations().conceptRelationList;
		LinkedList<ConceptCoord> coords = courseModel.getConceptCoordList();

		ConceptGraph cGraph = new ConceptGraph();
		cGraph.cNodes = new ArrayList<ConceptNode>();
		cGraph.cEdges = new ArrayList<ConceptEdge>();
		for(int i=0; i<rels.size(); i++) {
			RELConceptRelation rel = (RELConceptRelation) rels.get(i);
			String             source_name = rel.sourceConceptName;
			String             dest_name = rel.destinationConceptName;
			
			int                source_idx = cGraph.getConcepNodeIndex(source_name);
			ConceptNode        source_node = (source_idx==-1)?null:cGraph.cNodes.get(source_idx);
			int                dest_idx = cGraph.getConcepNodeIndex(dest_name);
			ConceptNode        dest_node = (dest_idx==-1)?null:cGraph.cNodes.get(dest_idx);
			
			if(source_node==null) {
				//long    source_cnr=ZebraConfig.getConceptDB().findConcept(courseModel.getName() + "." + source_name);
		        //Concept source_concept = ZebraConfig.getConceptDB().getConcept(source_cnr);
		        WOWOutConcept source_concept = courseModel.getConcept(source_name);
		        
		        ConceptCoord source_coord = null;
		        for(int k=0; k<coords.size(); k++) {
		        	ConceptCoord coord = coords.get(k);
		        	if(coord.name.equals(source_name)) {
		        		source_coord = coord;
		        		break;
		        	}
		        }
		        
		        source_node = new ConceptNode();
		        source_node.concept = source_concept;
		        source_node.attr = new ConceptNodeAttr();
		        source_node.attr.put("coord", source_coord);
		        source_node.parents = new ArrayList<ConceptNode.NWeight>(); 
		        source_node.childs = new ArrayList<ConceptNode.NWeight>(); 
		        
		        cGraph.cNodes.add(source_node);
			}
			if(dest_node==null) {
				//long    dest_cnr=ZebraConfig.getConceptDB().findConcept(courseModel.getName() + "." + dest_name);
		        //Concept dest_concept = ZebraConfig.getConceptDB().getConcept(dest_cnr);
		        WOWOutConcept dest_concept = courseModel.getConcept(dest_name);
		        
		        ConceptCoord dest_coord = null;
		        for(int k=0; k<coords.size(); k++) {
		        	ConceptCoord coord = coords.get(k);
		        	if(coord.name.equals(dest_name)) {
		        		dest_coord = coord;
		        		break;
		        	}
		        }
		        
		        dest_node = new ConceptNode();
		        dest_node.concept = dest_concept;
		        dest_node.attr = new ConceptNodeAttr();
		        dest_node.attr.put("coord", dest_coord);
		        dest_node.parents = new ArrayList<ConceptNode.NWeight>(); 
		        dest_node.childs = new ArrayList<ConceptNode.NWeight>(); 

		        cGraph.cNodes.add(dest_node);
			}
			
	        source_node.childs.add(source_node.new NWeight(dest_node, rel.weight));
	        dest_node.parents.add(dest_node.new NWeight(source_node, rel.weight));
	        
	        ConceptEdge cEdge = new ConceptEdge(source_node, dest_node, rel.weight);
	        if(cGraph.getConcepEdgeIndex(source_node.getName(), dest_node.getName())==-1) 
	        	cGraph.cEdges.add(cEdge);
		}
		//if(cGraph.getNumConceptNode() < 2 || cGraph.getNumConceptEdge() < 1) return null;
		cGraph.name = courseModel.getName();
		return cGraph;
	}
}

