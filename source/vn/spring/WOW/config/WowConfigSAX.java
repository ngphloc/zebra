/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * WowConfigSAX.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
 //   WowConfigSAX  -- SAX handler object for searching/reading the configuration file
 //    it handles SAX events and puts the information in a hashtable
 //    which it returns to its parent.
 //    see also WowConfig
//

package vn.spring.WOW.config;

import java.util.Hashtable;
import java.util.Stack;


import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class WowConfigSAX extends DefaultHandler {
   private Hashtable ConfigHash,ManagerHash;
   public String ConfigFile;


   // constructor
   public WowConfigSAX(Hashtable c,Hashtable m) {
      ConfigHash=c;
      ManagerHash=m;
   }

    // a function usefull for debugging
    void showString(String s) {
      if (s==null) {
        System.out.print("(null)");
      } else {
        System.out.print(s);
      }
    }

    // -------------------------
    // Starting handler section
    // -------------------------

    private boolean found;
    private String VarName;
    private String VarPass;
    public  String VarValue;
    private StringBuffer ValueBuffer;
    private Stack TreePath = new Stack();

    public void startElement(String namespaceURI,
                             String sName, // simple name
                             String qName, // qualified name
                             Attributes attrs)
    throws SAXException
    {
      // check whether we are within a 'WowConfig' or in a 'Access' tag
      TreePath.push(qName);

      found=false;
      if (TreePath.toString().equals("[WowConfig, Settings, Variable]")) {
        if (attrs != null)
            for (int i = 0; i < attrs.getLength(); i++)  {
                if ("id".equals(attrs.getQName(i))) {
                   VarName=attrs.getValue(i);
                   found=true;
                }
            }
      } else if (TreePath.toString().equals("[WowConfig, Access, User]")) {
        VarName=null; VarPass=null; // one may not be altered
        if (attrs != null)
            for (int i = 0; i < attrs.getLength(); i++)  {
                if ("login".equals(attrs.getQName(i))) {
                   VarName=attrs.getValue(i);
                   found=true;
                }
                if ("password".equals(attrs.getQName(i))) {
                   VarPass=attrs.getValue(i);
                }
            }
      } // else not a <Variable> or <User> tag
//      showString("namespace='");
//      showString(namespaceURI);
//      showString("', qname='");
//      showString(qName);
//      showString("', sname='");
//      showString(sName);
//      showString("'");
//      System.out.println();
    }

    // check whether these character intervals are not chunked (have to be
    // glued). This can be tested using a file with an extremely large config value

    public void characters(char content[], int start, int len)
    throws SAXException
    {
        // check if where within a 'Variable' tag (otherwise newlines and other tags are included)
        if (found) {
          String s = new String(content, start, len);
          if (ValueBuffer == null) {
             ValueBuffer = new StringBuffer(s);
          } else {
             ValueBuffer.append(s);
          }
        }

    }

    public void endElement(String namespaceURI, String sName, String qName)
    throws SAXException
    {
      if (found && TreePath.toString().equals("[WowConfig, Settings, Variable]")) {
        ConfigHash.put(VarName,ValueBuffer.toString());
      } else if (found && TreePath.toString().equals("[WowConfig, Access, User]")) {
        WowManager man=new WowManager(VarName,ValueBuffer.toString());
        // is there ALWAYS a password??
        man.setHashed(VarPass);
        ManagerHash.put(VarName,man);
      }
      found=false;
      ValueBuffer = null;

      String lastOne=(String)TreePath.pop();
      if (qName!=lastOne) System.err.println(qName+" does not match with tag "+lastOne);

    }

// on document-start
    public void startDocument()
    throws SAXException
    {
       // we get the hashtable from the
       // constructor
    }

// on document-end
    public void endDocument()
    throws SAXException
    {
    }
}
