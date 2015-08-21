/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ArrowStyle.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.GraphAuthor;

import java.awt.Color;


/**
 * This class stores the style and color of the edge.
 *
 */
public class ArrowStyle {
    public Color color;
    public String crtname;
    float[] lineStyle = new float[2];
  /**
   * default constructor.
   */

    public ArrowStyle() {
        lineStyle[0] = 1;
        lineStyle[1] = 0;
        color = new Color(10, 200, 10);
        crtname = "";
    }
}