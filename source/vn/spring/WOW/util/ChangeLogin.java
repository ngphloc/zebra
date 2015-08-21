/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ChangeLogin.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.util;

import vn.spring.WOW.WOWStatic;
import com.sun.xml.tree.*;
import com.sun.xml.parser.*;
import java.util.Vector;
import java.io.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

public class ChangeLogin {


	public ChangeLogin(Vector v, String login) {
		String authorfiles = WOWStatic.config.Get("WOWROOT")+WOWStatic.AUTHORFILESPATH.substring(1);
	    for (int i=0; i<v.size();i++) {
	    	try  {
				InputSource input = Resolver.createInputSource(new File(authorfiles+(String)v.elementAt(i)+".wow"));
				XmlDocument document = XmlDocument.createXmlDocument(input,true);
        		NodeList authorlist = document.getElementsByTagName("author");
        		Node authorNode = authorlist.item(0);
				Element aNode = (Element) document.createElement("author");
				aNode.appendChild(document.createTextNode(login));
				document.getDocumentElement().replaceChild(aNode, authorNode);
                PrintWriter pw = new PrintWriter(new FileWriter(authorfiles+(String)v.elementAt(i)+".wow"));
                document.setSystemId("conceptList");
                document.setDoctype(null, "generatelist4.dtd", null);
                document.write(pw);
                pw.close();
			} catch (Exception e) {System.out.println(e);}
		}
	}
}