/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * WOWAuthorDOM.java 2.0, August 8, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.config;

import java.io.*;
import java.util.Hashtable;
import org.apache.xerces.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;
import java.util.Vector;

public class WowAuthorDOM {

	private Hashtable AuthorHash;

    private String authorName;
    private String authorLogin;
    private String authorPass;
    private Vector v;
    private boolean name;
    private boolean course;

	public WowAuthorDOM(Hashtable a, String authorlist) {
		AuthorHash=a;
		try {
			v=new Vector();
			name=false;
			course=false;
			DOMParser parser = new DOMParser();
			// Trick through File below is to make sure the
			// parser gets a valid URL even when the file is C:...
			File listfile = new File(authorlist);
			String urlforlist = listfile.toURL().toString();
			parser.parse(urlforlist);
			Document document = parser.getDocument();
			traverse(document);
		} catch(Exception e) { System.out.println(e); }
	}

	public void traverse (Node node) {

		int type = node.getNodeType();
	    switch (type) {

			case Node.DOCUMENT_NODE: {
				NodeList children = node.getChildNodes();
	 			for (int i=0; i< children.getLength(); i++) { traverse (children.item(i)); }
				break;
			}

			case Node.ELEMENT_NODE: {

				if(node.getNodeName().equals("author")) {
					Attr attrs[] = sortAttributes(node.getAttributes());
					for ( int i = 0; i < attrs.length; i++ ) {
						Attr attr = attrs[i];
						if (attr.getNodeName().equals("login")) {
							authorLogin=attr.getNodeValue();
						}
						if (attr.getNodeName().equals("password")) {
							authorPass=attr.getNodeValue();
						}
					}
				} else if (node.getNodeName().equals("name")) {
					name=true;
				} else if (node.getNodeName().equals("courseName")) {
					course=true;
				}
				NodeList children = node.getChildNodes();
				for (int i=0; i< children.getLength(); i++) { traverse (children.item(i)); }
				break;
			}

			case Node.TEXT_NODE: {
				if (name && node.getNodeValue()!=null) { authorName=node.getNodeValue(); name=false; }
				if (course && node.getNodeValue()!=null) { course=false;  v.addElement(node.getNodeValue()); }
				break;
			}

		}
		if (type == Node.ELEMENT_NODE) {
			if (node.getNodeName().equals("author")) {
				WowAuthor man=new WowAuthor(authorLogin,authorName);
				man.setHashed(authorPass);
				man.setCourseList(v);
        		AuthorHash.put(authorLogin,man);
        		v=new Vector();
			}
		}
   }
	protected Attr[] sortAttributes(NamedNodeMap attrs) {

		int len = (attrs != null) ? attrs.getLength() : 0;
        Attr array[] = new Attr[len];
        for ( int i = 0; i < len; i++ ) {
			array[i] = (Attr)attrs.item(i);
        }
        for ( int i = 0; i < len - 1; i++ ) {
        	String name  = array[i].getNodeName();
        	int    index = i;
        	for ( int j = i + 1; j < len; j++ ) {
				String curName = array[j].getNodeName();
				if ( curName.compareTo(name) < 0 ) {
					name  = curName;
					index = j;
                }
			}
            if ( index != i ) {
				Attr temp    = array[i];
				array[i]     = array[index];
				array[index] = temp;
            }
		}

        return(array);

    }

}
