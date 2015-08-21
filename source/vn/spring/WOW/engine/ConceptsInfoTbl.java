/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ConceptInfoTbl.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Iterator;
import java.io.Serializable;

import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;

public class ConceptsInfoTbl implements Serializable {
	private static final long serialVersionUID = 1L;


  private Hashtable conceptsInfoTbl=null;


  /**
   * Constructor
   */
  public ConceptsInfoTbl(){

    this.conceptsInfoTbl=new Hashtable();


  }


  /**
   * Returns ConceptInfo for conceptName
   */
  public ConceptInfo getConceptInfo(String conceptName){

    return (ConceptInfo) this.conceptsInfoTbl.get(conceptName);

  }

  /**
   * Returns conceptsInfoTbl Hashtable
   */
    public Hashtable getConceptsInfoTbl(){
      return this.conceptsInfoTbl;
    }



//read Dependences from the DB
 public Hashtable readConceptInfo(String cName) throws DatabaseException,InvalidConceptException{
   String coursename=null;
   Vector conceptlist=null;
   long id;
   Concept concept=null;


   //xml file use 'c'"coursename" coursenaming
    //coursename="c"+cName;
    coursename=cName;

    conceptlist=WOWStatic.DB().getConceptDB().getConceptList();

    Iterator iter=conceptlist.iterator();
    while(iter.hasNext()){
      String conceptname=iter.next().toString();
      if(conceptname.startsWith(coursename + ".")){
        //System.out.println("Adding concept:"+conceptname);
        try{
          id=WOWStatic.DB().getConceptDB().findConcept(conceptname);
          concept=WOWStatic.DB().getConceptDB().getConcept(id);
          //System.out.println("cname"+concept.getName()+" type:"+concept.getType());
          //System.out.println("res:"+res);
          ConceptInfo ci=new ConceptInfo(concept.getName(),concept.getType(),concept.getTitle());
          //System.out.println("Conceptinfo made:"+ci.getConceptName()+" type:"+ci.getTypeName()+" res:"+ci.getResourceName());
          //Add new concept info to conceptinfo table
          this.conceptsInfoTbl.put(ci.getConceptName(),ci);

          //System.out.println("ConceptInfo record added to CI table: cname:"+conceptname+" cresource="+res+" type:"+ci.getTypeName());
        }
        catch(Exception e) {System.out.println("Error adding concept to concept table:"+e.getMessage());}
      }

    }

   return conceptsInfoTbl;
 }





}