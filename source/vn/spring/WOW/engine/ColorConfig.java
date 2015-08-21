/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ColorConfig.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import java.io.*;
import java.util.*;
import java.awt.Color;

/**
 * The class ColorConfig is a servlet that lets the user change
 * the color for specific links.
 */
public class ColorConfig {

    private StringBuffer sb;
    public  Profile profile;
  // The following colors are the default values used for different types of links.
  public Color GoodLinkColor=Color.blue; // color for desired links
  public Color BadLinkColor=new Color(32,32,32);  // color for undesired links
  public Color NeutralLinkColor=Color.magenta.darker().darker(); // color for uninteresting links
  public Color ExternalLinkColor=Color.red; // color for new external links
  public Color ExternalVisitedColor=new Color(127,0,0); // color for external visited links
  public Color ActiveLinkColor=Color.gray; // color for active links

  /**
   * This method writes out the user model as an HTML form that can be used
   * for updating the model.
   * There are two String parameters: a title to use for the HTML page and
   * a URL for the script or servlet to handle the submitted form.
   */
    public Resource genConfig(Profile profile, String login) throws InvalidAttributeException {

    this.profile = profile;
    String ChangeCgi = "?handler=ColorChange";
    sb = new StringBuffer();

    sb.append("<html><body>");

    sb.append("<h2>Color Configuration for " + login + "</h2>");
    sb.append("<form method=\"POST\" action=\"" + ChangeCgi + "\">");

    sb.append("<p>\nOnly change the colors you want. The others will remain as they are now.\n</p>");

    Color [] colors = { Color.black, Color.blue, Color.cyan, Color.gray,
                Color.green, Color.magenta, Color.orange, Color.pink,
                Color.red, Color.yellow };
    String [] colornames = { "black", "blue", "cyan", "gray", "green", "magenta", "orange", "pink", "red", "yellow" };

    sb.append("<dl>");

    Vector linkclasses = WOWStatic.CourseInfoTbl.getCourseInfo(profile.getAttributeValue("personal", "course")).linkclasses;
    for (int i=0;i<linkclasses.size();i++) {
        String linkclass = (String)linkclasses.get(i);
        String scolor = "0x000000";
        String visitedcolor = null;
        try {scolor = "0x"+profile.getAttributeValue("personal", "class_"+linkclass);} catch (InvalidAttributeException e) {}
        try {visitedcolor = "0x"+profile.getAttributeValue("personal", "class_"+linkclass+"_visited");} catch (InvalidAttributeException e) {}
        sb.append("<dt><b>Links for class '"+linkclass+"':</b> currently <span style=\"color:#" +  printColor(new Color(Integer.decode(scolor).intValue())) + "\">this color</span>.<br /></dt>\n<dd>Choose color: ");
        for (int c = 0; c < colors.length; ++c) {
          sb.append("<input type=\"RADIO\" name=\"class_"+linkclass+"\" value=\"" + printColor(colors[c]) + "\">");
          sb.append("<span style=\"color:#" + printColor(colors[c]) + "\">" + colornames[c] + "</span>");
        }
        sb.append("<br />Choose brightness: ");
        sb.append("<input type=\"RADIO\" name=\"class_"+linkclass+"_brightness\" value=\"darker\">darker");
        sb.append("<input type=\"RADIO\" name=\"class_"+linkclass+"_brightness\" value=\"normal\">normal");
        sb.append("<input type=\"RADIO\" name=\"class_"+linkclass+"_brightness\" value=\"lighter\">lighter\n<br /><br /></dd>\n");
        if (visitedcolor != null) {
            sb.append("<dt><b>Visited links for class '"+linkclass+"':</b> currently <span style=\"color:#" +  printColor(new Color(Integer.decode(visitedcolor).intValue())) + "\">this color</span>.<br /></dt>\n<dd>Choose color: ");
            for (int c = 0; c < colors.length; ++c) {
              sb.append("<input type=\"RADIO\" name=\"class_"+linkclass+"_visited\" value=\"" + printColor(colors[c]) + "\">");
              sb.append("<span style=\"color:#" + printColor(colors[c]) + "\">" + colornames[c] + "</span>");
            }
            sb.append("<br />Choose brightness: ");
            sb.append("<input type=\"RADIO\" name=\"class_"+linkclass+"_visited_brightness\" value=\"darker\">darker");
            sb.append("<input type=\"RADIO\" name=\"class_"+linkclass+"_visited_brightness\" value=\"normal\">normal");
            sb.append("<input type=\"RADIO\" name=\"class_"+linkclass+"_visited_brightness\" value=\"lighter\">lighter\n<br /><br /></dd>\n");
        }
    }
    sb.append("</dl><input type=\"SUBMIT\" value=\"Effectuate Changes\">");
    sb.append("<input type=\"RESET\" value=\"Reset to Previous Settings\">");
    sb.append("</form>");
    sb.append("\n</body>\n</html>");
    InputStream insb = new ByteArrayInputStream(sb.toString().getBytes());
    return new Resource(insb, new ResourceType("text/html"));
  }

  /**
   * Return a String representation of a Color. The String has the hexadecimal
   * pattern required for color representations in HTML.
   */
  public static String printColor(Color c) {
    int i = c.getRed();
    String r,g,b;
    if (i<10)
      r = "0" + Integer.toHexString(c.getRed());
    else
      r = Integer.toHexString(c.getRed());
    i = c.getGreen();
    if (i<10)
      g = "0" + Integer.toHexString(c.getGreen());
    else
      g = Integer.toHexString(c.getGreen());
    i = c.getBlue();
    if (i<10)
      b = "0" + Integer.toHexString(c.getBlue());
    else
      b = Integer.toHexString(c.getBlue());
    return r + g + b;
  }

}
