/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ConceptDependences.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import java.util.LinkedList;
import java.io.Serializable;


public class ConceptDependences  implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//List of concepts for which concept this.conceptName is in prerequisites
private LinkedList prerequisiteINs=null;

//List of concepts for which concept this.conceptName is in actions part(outcomes)
private LinkedList outcomeINs=null;

//List of concepts that are prerequisite to this.concepName
private LinkedList prerequisites=null;

//List of concepts that are outome to this.concepName
private LinkedList outcomes=null;


private String conceptName=null;


/**
 * Constructor
 */
public ConceptDependences(String name){
  this.conceptName=name;

  this.prerequisites=new LinkedList();
  this.outcomes=new LinkedList();

  this.prerequisiteINs=new LinkedList();
  this.outcomeINs=new LinkedList();

}

public String getConceptName(){
  return this.conceptName;
}

public LinkedList getPrerequisiteINs(){
  return this.prerequisiteINs;
}

public LinkedList getOutcomeINs(){
  return this.outcomeINs;
}

public void AddToPrerequisiteINs(String concept){
  this.prerequisiteINs.add(concept);
 }

public void AddToOutcomeINs(String concept){
  this.outcomeINs.add(concept);
  }

public void AddToPrerequisites(String concept){
  this.prerequisites.add(concept);
 }

public void AddToOutcomes(String concept){
  this.outcomes.add(concept);
  }

public LinkedList getPrerequisites(){
    return this.prerequisites;
  }

public LinkedList getOutcomes(){
    return this.outcomes;
}


}