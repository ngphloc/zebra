/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * HTMLParser.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import java.io.*;
import javax.swing.text.html.parser.*;
import javax.swing.text.html.HTMLEditorKit.*;


public class HTMLParser implements GenericParser {

  ParserCallback parsercallback = null;

  /**
   *  Creates new HTMLParser
   */
  public HTMLParser() {
  }

  public void setHandler(Object ob) {
    parsercallback = (ParserCallback) ob;
  }

  public void parse(Reader in) {

    ParserDelegator pd = new ParserDelegator();
    try {
      pd.parse(in, parsercallback, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}