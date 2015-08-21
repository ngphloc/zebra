/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Dependences.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import java.util.Hashtable;
import java.util.Vector;
import java.io.Serializable;

import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.parser.*;


public class Dependences implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Hashtable dependences=null;

  /**
   * Constructor
   */
  public Dependences(){

    this.dependences=new Hashtable();

  }

  /**
   * Returns dependences Hashtable
   */
  public Hashtable getDependences(){
    return this.dependences;
  }

//read Dependences from the DB
 public Hashtable readDependences(String cName) throws DatabaseException,InvalidConceptException{
   String coursename=null;

   //xml file use 'c'"coursename" coursenaming
    //coursename="c"+cName;
   coursename=cName;

   //For each course-concept do the check for dependences in this concept file
        String conceptname=null;
        long cFileNr;
        Vector conceptNames=WOWStatic.DB().getConceptDB().getConceptList();
        int t=0;
        int size=conceptNames.size();

        while(t<size){
          conceptname=(String) conceptNames.get(t);
          if (conceptname.startsWith(coursename + ".")){
            //System.out.println("ReadDependences: Checking concept: "+conceptname);
            //Open it's file
            cFileNr= WOWStatic.DB().getConceptDB().findConcept(conceptname);
            Concept concept=WOWStatic.DB().getConceptDB().getConcept(cFileNr);

            Vector outcomeconcepts = getOutcomeConcepts(concept.getName());

            //Fill prerequisite object and outcome object for this concept
            fillPrereqOutcomes(concept, outcomeconcepts);

            //Fill prerequisiteIN object for this concept
            fillPrerequisitesIN(concept);

            //Fill OutcomeIN object for this concept
            fillOutcomesIN(concept, outcomeconcepts);

          }
          t=t+1;
        }

   return dependences;
 }

 /**
   * Fill Prerequisite object and Outcomes object for this concept
   */
  private void fillPrereqOutcomes(Concept concept, Vector actionConcepts) throws DatabaseException,InvalidConceptException{
    Vector reqConcepts=getRequirementConcepts(concept.getName());
    String conceptName=concept.getName();
    ConceptDependences cd=null;
    int t,size;

    if(!this.dependences.containsKey(conceptName)){
        cd=new ConceptDependences(conceptName);
        this.dependences.put(conceptName,cd);
      }
      else{
        cd=(ConceptDependences) this.dependences.get(conceptName);
       }

    size=reqConcepts.size();
    //Add each concept in requirement rel. to prerequistites of concept 'conceptname'
    for(t=0;t<size;t=t+1){
      cd.AddToPrerequisites(reqConcepts.get(t).toString());
    }

    size=actionConcepts.size();
    //Add each concept in actions to outcomes of concept 'conceptname'
    for(t=0;t<size;t=t+1){
      cd.AddToOutcomes(actionConcepts.get(t).toString());
    }

  }


 /**
  * Fill prerequisiteIN object for this concept
  */
 private void fillPrerequisitesIN(Concept concept) throws DatabaseException,InvalidConceptException{
   String conceptName=null;
   Vector reqConcepts=getRequirementConcepts(concept.getName());
   int t=0;
   int size=reqConcepts.size();

  //For each concept check occurrence in requirement relation(prerequisites)
   for(t=0;t<size;t=t+1){
       //get concept name from requirement vector
       conceptName=(String) reqConcepts.get(t);

       //add file concept to the prerequisites of concept 'conceptname'
       ConceptDependences cd=null;
       if(!this.dependences.containsKey(conceptName)){
         cd=new ConceptDependences(conceptName);
         this.dependences.put(conceptName,cd);
       }
       else{
         cd=(ConceptDependences) this.dependences.get(conceptName);
       }

       cd.AddToPrerequisiteINs(concept.getName());
       ConceptDependences cdtest=null;
       cdtest=(ConceptDependences) this.dependences.get(conceptName);

       java.util.LinkedList ll=cdtest.getPrerequisiteINs();
       java.util.Iterator iter=ll.iterator();
       while (iter.hasNext()) {
         iter.next();
       }
       ll=cdtest.getOutcomeINs();
       iter=ll.iterator();
       while (iter.hasNext()) {
        iter.next();
       }
   }
 }

   /**
   * Fill outcomeIN object for this concept
   */
   private void fillOutcomesIN(Concept concept, Vector reqConcepts)  throws DatabaseException,InvalidConceptException{

     String conceptName=null;
     int t=0;
     int size=reqConcepts.size();

     //For each concept check occurrence in requirement relation(prerequisites)
     for(t=0;t<size;t=t+1){
       //get concept name from requirement vector
       conceptName=(String) reqConcepts.get(t);

       //add file concept to the prerequisites of concept 'conceptname'
       ConceptDependences cd=null;
       if(!this.dependences.containsKey(conceptName)){
         cd=new ConceptDependences(conceptName);
         this.dependences.put(conceptName,cd);

       }
       else{
         cd=(ConceptDependences) this.dependences.get(conceptName);
       }

       cd.AddToOutcomeINs(concept.getName());

       ConceptDependences cdtest=null;
       cdtest=(ConceptDependences) this.dependences.get(conceptName);

       java.util.LinkedList ll=cdtest.getPrerequisiteINs();
       java.util.Iterator iter=ll.iterator();
       while (iter.hasNext()) {
         iter.next();
       }
       ll=cdtest.getOutcomeINs();
       iter=ll.iterator();
       while (iter.hasNext()) {
        iter.next();
       }

     }
 }

   /**
    * Returns all conceptnames used in Requirement expression in concept file
    */
   public Vector getRequirementConcepts(String conceptName) throws DatabaseException, InvalidConceptException {
         String nodeValue=null;
         Vector rtnVector=new Vector();

//changed by @David @15-06-2008
         ConceptDB cdb = WOWStatic.DB().getConceptDB();
         Attribute attr = null;
         try {
             attr = cdb.getAttribute(cdb.findConcept(conceptName), "suitability");
         }
         catch (InvalidAttributeException e) {return rtnVector;}
         if (attr.isPersistent()) return rtnVector;
         nodeValue = attr.getDefault();
//end changed by @David @15-06-2008
        GetConceptsFromExprVL gcvl = new GetConceptsFromExprVL();
        Parser parser = new Parser(gcvl);
        try {
            parser.parse(nodeValue);
            return gcvl.concepts;
        } catch (Exception e) {
            e.printStackTrace();
            return new Vector();
        }
    }

    private class GetConceptsFromExprVL implements VariableLocator {
        public Vector concepts = new Vector();
        private UMVariableLocator umvl = new UMVariableLocator(new Profile(), WOWStatic.DB().getConceptDB());
        public GetConceptsFromExprVL() {}
        public boolean existsVariable(String variable) {return umvl.existsVariable(variable);}
        public Object getVariableValue(String variable) throws ParserException {
            try {
                try {
                    WOWStatic.DB().getConceptDB().findConcept(variable);
                    if (!concepts.contains(variable)) concepts.add(variable);
                } catch (InvalidConceptException e) {
                    DotString ds = new DotString(variable);
                    ds.set(ds.size()-1, null);
                    if (!concepts.contains(ds.toString())) concepts.add(ds.toString());
                }
            } catch (Exception e) {e.printStackTrace();}
            return umvl.getVariableValue(variable);
        }
    }

    /**
     * Returns all concepts used in Action tags in concept file
     */
    public Vector getOutcomeConcepts(String conceptName) throws
           DatabaseException,
           InvalidConceptException {
        Vector result = new Vector();
        ConceptDB cdb = WOWStatic.DB().getConceptDB();
        Concept c = cdb.getConcept(cdb.findConcept(conceptName));
        Vector attrs = c.getAttributes();
        for (int i=0;i<attrs.size();i++) {
            Attribute attr = (Attribute)attrs.get(i);
            Vector actions = attr.getActions();
            for (int j=0;j<actions.size();j++) {
                Action action = (Action)actions.get(j);
                for (int k=0;k<action.getTrueStatements().size();k++)
                    addAssignmentToOutcome((Assignment)action.getTrueStatements().get(k), result);
                for (int k=0;k<action.getFalseStatements().size();k++)
                    addAssignmentToOutcome((Assignment)action.getFalseStatements().get(k), result);
            }
        }
        return result;
    }

    private void addAssignmentToOutcome(Assignment a, Vector outcome) {
        String cname = null;
        try {
            cname = (new AttributeIdentifier(a.getVariable())).concept();
        } catch (Exception e) {
            return;
        }
        if (!outcome.contains(cname)) outcome.add(cname);
    }

//following method replaced by David by method above
   /**
   * Returns all concepts used in Action tags in concept file
   */
/*  public Vector getOutcomeConcepts(String conceptName)  throws DatabaseException,InvalidConceptException{
      Vector rtnVector=new Vector();
      String shortConceptName=null; //only the name of the concept
      String longConceptName=null;  //coursename.conceptname
      Hashtable usedConceptNames =new Hashtable();

      long id;

      id=WOWStatic.DB().getConceptDB().findConcept(conceptName);

      //get all attributes
      Vector attrs=WOWStatic.DB().getConceptDB().getAttributes(id);
      int t,t2,t3,t4;

      //Check all attributes for assigments(any concept in assign in outcome concept)
      for(t=0;t<attrs.size();t++){
        Attribute attr=(Attribute)attrs.get(t);
        Vector actions= attr.getActions();

        //Check actions of the attribute
        for(t2=0;t2<actions.size();t2++){
          Action action=(Action)actions.get(t2);
          Vector trueStats,falseStats;
          trueStats=action.getTrueStatements();
          falseStats=action.getFalseStatements();

          //Check all true assignments of the action
          for(t3=0;t3<trueStats.size();t3++){
            Assignment assign=null;
            assign=(Assignment) trueStats.get(t3);
            String longVar=assign.getVariable();//long concept name (coursename.conceptname)
            String var= longVar.substring(longVar.indexOf(".")+1,longVar.lastIndexOf("."));//shortconceptname

            //Calculate annotation for the links(value of linkClass)
            longConceptName=getCourseName(longVar,longVar.indexOf("."))+"."+var;

            //Add the conceptname to used concepts table to avoid dubbles
            usedConceptNames.put(var,"");

            //Add concept to rtnVector
            rtnVector.add(longConceptName);

          }

          //Check all false assignments of the action
          for(t4=0;t4<falseStats.size();t4++){
            String longVar=null;
            Assignment assign=null;
            assign=(Assignment) falseStats.get(t4);
            longVar=assign.getVariable();
            String var= longVar.substring(longVar.indexOf(".")+1,longVar.lastIndexOf("."));

            //Calculate annotation for the links(value of linkClass)
            //Get the resource name of this concept
            longConceptName=getCourseName(longVar,longVar.indexOf("."))+"."+var;

            //Add the conceptname to used concepts table to avoid doubles
            usedConceptNames.put(var,"");

            //Add concept to rtnVector
            rtnVector.add(longConceptName);

          }
        }
      }

System.out.println("\nDependences: "+conceptName);
System.out.println(rtnVector);

      return rtnVector;
   }

   //Returns a course name from a string
  private String getCourseName(String nodeValue,int pidx){

          String courseName="";
          int t=1;
          String c;
          c=String.valueOf(nodeValue.charAt(pidx-t));
          while((pidx>=t) && !c.equals(" ")){
            courseName=c+courseName;
            t=t+1;
            if (pidx>=t) c=String.valueOf(nodeValue.charAt(pidx-t));
           }

           return courseName;

  }
*/

}