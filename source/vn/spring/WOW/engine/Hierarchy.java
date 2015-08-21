/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Hierarchy.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import java.io.*;
import java.util.*;
import java.util.Hashtable;

import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.*;


/**
 *
 */
public class Hierarchy  implements Serializable{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


//the indexNodeTable on names for this database(name,TNode)
   private Hashtable indexNodeTable = new Hashtable();


  //the concepts directory
 // private File root;

  private TNode rootNode=null;

  //HTML code string for all hierarchy nodes
  //private StringBuffer menuHTMLCode=null;

  //Constructor
  public Hierarchy (File proot) {

    WOWStatic.checkNull(proot, "root");
    //root = new File(proot, "concept");

  }

/**
 * Reads the Hierarchy fields from the concept files and generates in-mem
 * representation the Hierarchy tree
 */
    public void readHierarchy(String coursename){

      try{
      String firstConceptName=getFirstConceptName(coursename);
      if(firstConceptName==null) throw new Exception("No first concept");

         //create the root node for the tree and the firstConcept Node
         TNode firstNode=new TNode(firstConceptName);
         rootNode=new TNode(firstNode,null);
         rootNode.setName("root");

        /* fill indexNodeTable (conceptname-node) containing all concepts of the course
        nodes only contain conceptnames; use index file*/

         //Get all the conceptnames
         Vector keylist=WOWStatic.DB().getConceptDB().getConceptList();
         Iterator iter=keylist.iterator();

         // Fill Hashtables with conceptnames  pared with TNodes
         String conceptname=null;
         while(iter.hasNext()){
           conceptname=iter.next().toString();
           if (conceptname.startsWith(coursename)){
              if(conceptname.equals(firstNode.getName()))
                indexNodeTable.put(conceptname, firstNode);
              else
                indexNodeTable.put(conceptname, new TNode(conceptname));
           }
         }

     /* fill the nodes from the indexNodeTable with data*/

        fillNodes(rootNode.getFirstChild());

      }catch(Exception e){System.out.println("Reading Hierarchy Failed! "+e.getMessage());}

    }


    /**
     * Fills the indexNodeTable nodes with data
     */
     private void fillNodes(TNode curNode){

        try {

          TNode pNode=null; //pointer Node
          //get concept corresponding to curNode
          long id=WOWStatic.DB().getConceptDB().findConcept(curNode.getName());
          Concept concept=WOWStatic.DB().getConceptDB().getConcept(id);

          //Set curNodes resource
          curNode.setResource(concept.getResourceURL()==null?null:concept.getResourceURL().toString());
          //Set curNodes type
          curNode.setType(concept.getType());

          //Set curNodes first child and process it recursively
          if(concept.getHierStruct().firstchild!=null){
            pNode=(TNode) indexNodeTable.get(concept.getHierStruct().firstchild);
            curNode.setFirstChild(pNode);
            //Recursively process this child
            if (pNode !=null) fillNodes(pNode);
          }
          //Set curNodes next sibling and process it recursively
          if(concept.getHierStruct().nextsib!=null){
            pNode=(TNode) indexNodeTable.get(concept.getHierStruct().nextsib);
            curNode.setNextSib(pNode);
            //Recursively process next sibling
            if (pNode !=null) fillNodes(pNode);
          }
          //Set curNodes parent
          if(concept.getHierStruct().parent!=null){
            pNode=(TNode) indexNodeTable.get(concept.getHierStruct().parent);
            curNode.setParent(pNode);
          }

        }
          catch (Exception e) {
              e.printStackTrace();
            System.err.println("Hierarchy.fillNodes: "+ e.getMessage());
          }

    }



    /* Reads the first course concept from the firstConceptFile file */
    private String getFirstConceptName(String coursename){

        //changed by @David @16-10-2008
        //the first concept is the concept with hierarchy, but without a parent
        Concept found = null;
        try {
            ConceptDB cdb = WOWStatic.DB().getConceptDB();
            Vector list = cdb.getConceptList();
            for (int i=0;i<list.size();i++) {
                String cname = (String)list.get(i);
                if (cname.toLowerCase().startsWith(coursename.toLowerCase()+".")) {
                    Concept c = cdb.getConcept(cdb.findConcept(cname));
                    ConceptHierStruct chs = c.getHierStruct();
                    if (chs != null) {
                        if (chs.parent == null) found = c; else {
                            if (chs.parent.trim().equals("")) found = c;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return found.getName();

        /*
      String FConceptName=null;
     // coursename="c"+coursename; //concept files have prefix c (course) for course names

      try{
            //Open the file
            File begConF = new File(WOWStatic.conceptRoot, this.firstConceptFile);
            if (!begConF.exists()) throw new IOException("Begin Concept File does not exist!");

            //Parse the file
            XmlDocument doc=null;
            try {doc = XMLUtil.getXML(begConF);} catch (IOException e) {throw new DatabaseException("unable to read begin concept from the Begin concept file!");}

            //Get all 'course' nodes
            NodeList list = doc.getElementsByTagName("course");
            int listLength=list.getLength();
            if (listLength==0) {throw new DatabaseException("No courses in the Beginconcept file!");}
            //Find the current course node
            int i=0;
            Node curNode; //coursename node
            curNode=list.item(i).getFirstChild();
            while ((!XMLUtil.nodeValue(curNode).toString().equals(coursename)) && (i<listLength)) {
                    i=i+1;
                    if (i<listLength) curNode=list.item(i).getFirstChild();
                    }

            //Get the value of the firstConcept node
            if(!XMLUtil.nodeValue(curNode).equals(coursename))
              throw new IOException("No BeginConcept found!");
            else
              FConceptName=XMLUtil.nodeValue(curNode.getNextSibling());

      }
      catch(Exception e){
        System.out.println("Hierarchy.getFirstConceptName:"+ e.getMessage());
      }

      return FConceptName;
        */
        //end changed by @David @16-10-2008

    }

    /**
     * Returns reference to the rootNode
     */
     public TNode getRootNode(){
      return rootNode;
    }

    /**
     * Returns TNode of concept 'name'
     */
    public TNode getTNode(String name){

      TNode node= (TNode) this.indexNodeTable.get(name);
      return node;
    }

}


