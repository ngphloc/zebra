/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * XmlReader.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.Utils;

import com.sun.xml.tree.*;
import com.sun.xml.parser.*;
import org.xml.sax.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.*;
	public class XmlReader {
		XmlDocument doc;
		Document document;
	public XmlReader(String name, boolean v) throws Exception {
		InputSource input;
		String error;

		try {
			input = Resolver.createInputSource(new File(name));
			doc = XmlDocument.createXmlDocument(input,v);
		} catch (SAXParseException err) {
			System.out.println("Error parsing document: "+name+ "\n"+", line " + err.getLineNumber ()+ ", uri " + err.getSystemId ());
			System.out.println("   " + err.getMessage ());
			error = new String("Error parsing document: "+name+ "\n"+", line " + err.getLineNumber ()+ ", uri " + err.getSystemId ());
			error = error + new String("   " + err.getMessage ());
			throw new Exception(error);
		} catch (Exception e) {
			System.out.println(e);
			error = new String("File not found: "+name);
			throw new Exception(error);
		}
	}
	public Element getRoot() {
		return (Element)doc.getDocumentElement();
	}
}
