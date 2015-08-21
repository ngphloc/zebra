/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * TermModel 1.0, October 10, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.GraphAuthor;

import java.util.Vector;
import java.util.Hashtable;

/**
 * This class represents the internal graph. It keeps track of all
 * edges and vertices. Edges should only be added or removed by the
 * addEdge or removeEdge methods, since these update information in
 * the edges.
 */
public class TermModel {
    public Hashtable vertices = new Hashtable();
    public Vector edges = new Vector();

    public TermModel() {}

    public void addEdge(TermEdge edge) {
        if ((edge.source == null) || (edge.target == null)) {
            System.out.println("TermModel: edge not added: "+edge.source);
            return;
        }
        edge.source.edgesOut.add(edge);
        edge.target.edgesIn.add(edge);
        edges.add(edge);
    }

    public void removeEdge(TermEdge edge) {
        edge.source.edgesOut.remove(edge);
        edge.target.edgesIn.remove(edge);
        edges.remove(edge);
    }
}