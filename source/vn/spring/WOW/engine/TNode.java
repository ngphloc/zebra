/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * TNode.java 1.0, August 30, 2008, modified by Loc Nguyen 2009
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import java.io.Serializable;

// This class represents one node(concept) in the hierarchy tree
public class TNode  implements Serializable {

        /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private TNode nextSib=null;
        private TNode firstChild=null;
        private TNode parent=null;

        //concept type
        private String type=null;

        //concept name
        private String name=null;

        // needed to generate a link to a resource (conection menu-mainpage)
        private String resource=null;

    //Constructor1
    public TNode( TNode fChild, TNode next){
          nextSib=next;
          firstChild=fChild;
    }

    //Constructor2
    public TNode(String pname){
            name=pname;
        }

    //Constructor3
    public TNode(){

        }

        //returns the reference conected to the name(concept name)
        public String getName() { return name;}
        //returns the reference to the resource conected to concept
        public String getResource() { return resource;}

        //returns the reference to the next Node in the Tree
        public TNode  getNextSib() { return nextSib;    }

        //returns the reference to the firstChild Node in the Tree
        public TNode getFirstChild() { return firstChild;}

        //returns the reference to the parent Node in the Tree
        public TNode getParent() { return parent;}
        //sets name
        public void setName(String newName) {  name=newName;}

        //sets resource
        public void setResource(String newResource) {  resource=newResource;}

        //sets the reference to the next Node in the Tree
        public void setNextSib(TNode newNext) {  nextSib=newNext;}

        //sets the reference to the firstChild Node in the Tree
        public void setFirstChild(TNode newFirstChild) {  firstChild=newFirstChild;}

        //sets the reference to the parent Node in the Tree
        public void setParent(TNode newParent) {  parent=newParent;}

        public String getType() {return type;}
        public void setType(String type) {this.type = type;}
        
        public boolean isDecendentOf(TNode parent) {
        	if(parent == null) return false;
        	TNode child = parent.getFirstChild();
        	while(child != null) {
        		if(child.getName().equals(this.getName())) return true;
        		if(isDecendentOf(child)) return true;
        		child = child.getNextSib();
        	}
        	return false;
        }
        
}
