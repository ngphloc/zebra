/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/

/**
 * GenerateListData.java 1.0 August 30, 2008.
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.formeditor;

// Read in xml-File.
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


// DOM definition en Exceptions
import org.w3c.dom.Document;
import org.w3c.dom.Element;


// exception.
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class GenerateListData {
  private static Document document;
  public static final int ELEMENT_TYPE = 1;
  public static final int ATTR_TYPE = 2;
  public static final int TEXT_TYPE = 3;
  public static final int CDATA_TYPE = 4;
  public static final int ENTITYREF_TYPE = 5;
  public static final int ENTITY_TYPE = 6;
  public static final int PROCINSTR_TYPE = 7;
  public static final int COMMENT_TYPE = 8;
  public static final int DOCUMENT_TYPE = 9;
  public static final int DOCTYPE_TYPE = 10;
  public static final int DOCFRAG_TYPE = 11;
  public static final int NOTATION_TYPE = 12;

  /**
   * Constructor
   */
  public GenerateListData() {
    buildDom();
  }

  /**
   * Constructor
   * @param filename
   */
  public GenerateListData(String filename) {

   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 //factory.setValidating(true);
   factory.setValidating(false);
   factory.setNamespaceAware(true);

  try {
    DocumentBuilder builder = factory.newDocumentBuilder();

    builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
      // ignore fatal errors (an exception is guaranteed)
      public void fatalError(SAXParseException exception)
                      throws SAXException {}

      // treat validation errors as fatal
      public void error(SAXParseException e)
                 throws SAXParseException {
        throw e;
      }

      // dump warnings too
      public void warning(SAXParseException err)
                   throws SAXParseException {
          System.out.println("** Warning" + ", line " +
                                 err.getLineNumber() + ", uri " +
                                 err.getSystemId());
          System.out.println("   " + err.getMessage());
      }
    });

    try {
        //URL url = new URL(filename);

       // System.out.println("in generatelistdata: " + url.toString());


        //URLConnection uc = url.openConnection();
        //InputStream in = uc.getInputStream();
        //InputStream in = url.openStream();
        //InputSource source = new InputSource(in);
        //source.setSystemId(url.getProtocol()+"://"+url.getHost()+":"+url.getPort()+"/FormEditor/Compiled/");
        //source.setSystemId(url.getProtocol()+"://"+"FormEditor/Compiled/");
        //document = builder.parse(source);
        //change done by Natasha
        document = builder.parse(filename);
    } catch (IOException e) {
        System.out.println("Error: " + e.toString());
    }
  } catch (SAXException sxe) {
      // Error generated during parsing
      Exception x = sxe;

      if (sxe.getException() != null) {
          x = sxe.getException();
      }

      x.printStackTrace();
  } catch (ParserConfigurationException pce) {
      // Parser with specified options can't be built
      pce.printStackTrace();
  }/*  catch (IOException ioe) {
  // I/O error
  ioe.printStackTrace();
  }*/
  }

  public static void buildDom() {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(true);
    factory.setNamespaceAware(true);

    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      document = builder.newDocument(); // Create from whole cloth

      Element root = (Element) document.createElement("conceptList");
      document.appendChild(root);
      root.appendChild(document.createElement("concept"));

      document.getDocumentElement().normalize();
    }
    catch (ParserConfigurationException pce) {
      // Parser with specified options can't be built
      pce.printStackTrace();
    }
  }

  public static Document getDocument() {
    return document;
  }
}