/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * GPConverter.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.GraphAuthor;

import com.jgraph.*;

import com.jgraph.graph.GraphModel;


/**
 * This class converts the graph model for exporting (gxl and images).
 *
 */

// This class is part of the jGraphPad application
// copied and modified by Brendan Rousseau
// LGNU license
// not used in current project.

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.util.Hashtable;
import java.util.Iterator;


public class GPConverter {


  //
   // Image Converter
   //

   // Create a buffered image of the specified graph.
   public static BufferedImage toImage(JGraph graph) {
     Object[] cells = graph.getRoots();

     if (cells.length > 0) {
                               Rectangle bounds = graph.getCellBounds(cells);
                               graph.toScreen(bounds);

                               // Create a Buffered Image
                               Dimension d = bounds.getSize();
                               BufferedImage img = new BufferedImage(d.width+10,
                                                     d.height+10, BufferedImage.TYPE_INT_RGB);
                               Graphics2D graphics = img.createGraphics();
                               graphics.translate(-bounds.x+5, -bounds.y+5);
                               graph.paint(graphics);

                               return img;
     }
     return null;
   }



  //
    // GXL Converter
    //
    static transient Hashtable hash;

    // Create a GXL-representation for the specified cells.
    public static String toGXL(JGraph graph, Object[] cells) {
        hash = new Hashtable();

        String gxl = "<gxl><graph>";

        // Create external keys for nodes
        for (int i = 0; i < cells.length; i++)

            if (cells[i].getClass().getName()
                        .equals("com.jgraph.graph.DefaultGraphCell")) {
                hash.put(cells[i], new Integer(hash.size()));
            }

        // Convert Nodes
        Iterator it = hash.keySet().iterator();

        while (it.hasNext()) {
            Object node = it.next();
            gxl += vertexGXL(graph, hash.get(node), node);
        }

        // Convert Edges
        int edges = 0;

        for (int i = 0; i < cells.length; i++)
            if (cells[i].getClass().getName()
                        .equals("com.jgraph.graph.DefaultEdge")) {
                gxl += edgeGXL(graph, new Integer(edges++), cells[i]);
            }


        // Close main tags
        gxl += "\n</graph></gxl>";

        return gxl;
    }

    public static String vertexGXL(JGraph graph, Object id, Object vertex) {
        String label = graph.convertValueToString(vertex);

        return "\n\t<node id=\"node" + id.toString() + "\">" +
               "\n\t\t<attr name=\"Label\">" + "\n\t\t\t<string>" + label +
               "</string>" + "\n\t\t</attr>" + "\n\t</node>";
    }

    public static String edgeGXL(JGraph graph, Object id, Object edge) {
        GraphModel model = graph.getModel();
        String from = "";

        if (model.getSource(edge) != null) {
            Object source = hash.get(model.getParent(model.getSource(edge)));

            if (source != null) {
                from = "node" + source.toString();
            }
        }

        String to = "";

        if (model.getTarget(edge) != null) {
            Object target = hash.get(model.getParent(model.getTarget(edge)));

            if (target != null) {
                to = "node" + target.toString();
            }
        }

        if ((from != null) && (to != null)) {
            String label = graph.convertValueToString(edge);

            return "\n\t<edge id=\"edge" + id.toString() + "\"" + " from=\"" +
                   from + "\"" + " to=\"" + to + "\">" +
                   "\n\t\t<attr name=\"Label\">" + "\n\t\t\t<string>" +
                   label + "</string>" + "\n\t\t</attr>" + "\n\t</edge>";
        } else {
            return "";
        }
    }
}