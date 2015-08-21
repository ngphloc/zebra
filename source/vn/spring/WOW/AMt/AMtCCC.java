//------------------------------------------------------------------------------
// Unit Name: AMtCCC.java
// Author: T.J. Dekker, reviewed and modified by Loc Nguyen
// Date of Creation: 14-09-2008
// Purpose: Contains all functionality for performing a Concept Consistency
//          Check (CCC) for the AMt Application
//
// DOCUMENT CHANGES
//
// Date:           Author:             Change:
// -----------------------------------------------------------------------------
// 14-09-2008      T.J. Dekker         Creation
//------------------------------------------------------------------------------

package vn.spring.WOW.AMt;

import vn.spring.WOW.WOWDB.XMLUtil;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.datacomponents.Attribute;
import vn.spring.WOW.datacomponents.Case;
import vn.spring.WOW.datacomponents.CaseGroup;
import vn.spring.WOW.datacomponents.Concept;
import vn.spring.WOW.exceptions.InvalidAttributeException;
import vn.spring.WOW.util.KoenFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

/**
 * Contains all functionality for performing a Concept Consistency Check (CCC)
 * for the AMt Application.
 * @author T.J. Dekker, changed by Loc Nguyen
 * @version 1.0.0
 */
public class AMtCCC {

  private String wowrootpath;

  /**
   * Default Constructor
   */
  public AMtCCC() {
    wowrootpath = WOWStatic.config.Get("WOWROOT");
    wowrootpath += (wowrootpath.endsWith(File.separator)?"":File.separator);
  }

  /**
   * Performs a Concept Consistency check for an application. This means that
   * all resources mentioned in the concept files of the application are
   * checked for existence.
   * @param aauthorname the name of the author to which the application belongs
   * @param aappname the name of the application to perform the check for
   * @return the CCCReport for the application
   */
  public TCCCReport performCheck(String aauthorname, String aappname) {
    //get conceptlist for application (stored in WOWStatic database)

    String authorfilespath = WOWStatic.AUTHORFILESPATH.substring(1);
    String path = wowrootpath + authorfilespath + aauthorname + File.separator +
    aappname + ".wow";
    File f = new File(path);

    TCCCReport result = new TCCCReport(aauthorname, aappname);

    try {
      //System.out.println(path);
      Document document = XMLUtil.getXML(f);
      Vector conceptlist = getConceptList(document);

      //sort conceptlist by conceptname
      Object[] carr = conceptlist.toArray();
      Arrays.sort(carr, new Comparator() {
        public int compare(Object a, Object b) {
          Concept ca = (Concept)a;
          Concept cb = (Concept)b;
          //--- Sort by conceptname
          return ca.getName().compareToIgnoreCase(cb.getName());
        }
      });



      for (int i = 0; i< carr.length ;i++ ) {//add reports for each concept
        Concept c = (Concept)carr[i];

        TCCCReportPart[] reps = checkConcept(c);
        for (int j = 0;j<reps.length ;j++) {//add all reportparts
          result.add(reps[j]);
        }
      }

      return result;
    }
    catch (Exception e) {
      System.out.println("Exception in AMtCCC.performCheck(): " + e.toString());
      e.printStackTrace();
      return result;
    }
  }

  /**
   * Checks if all resources mentioned in a concept exist
   * This check checks the following locations of a concept for resources:
   *      -resource attribute of a 'page' concept
   *      -casegroup of the showability attribute of a 'page' concept.
   *      -casegroup of the showability attribute of a 'fragment' concept.
   * @param c the concept to check
   * @return the reports for all resources of this concept
   */
  private TCCCReportPart[] checkConcept(Concept c) {
    Vector result = new Vector();

    //check resource attribute if concept is a page concept
    if (c.getType().equals("page")) {
      //check Concept's Resource
      result.add(checkConceptResource(c));
    }
    if (c.getType().equals("page") || c.getType().equals("fragment")) {
      //check showability casegroup (if existent)
      Attribute attr = null;

      try {
        attr = c.getAttribute("showability");
      }
      catch (InvalidAttributeException e) {
        //System.out.println("AMtCCC.checkConcept: " + e.toString());
      }

      if (attr != null) {
        //check showability casegroup resources
        TCCCReportPart[] sarr = checkConceptShowabilityCaseGroup(c, attr);
        for (int i = 0; i<sarr.length; i++) {
          result.add(sarr[i]);
        }
      }
    }

    //copy vector to array
    TCCCReportPart[] arr = new TCCCReportPart[result.size()];
    result.copyInto(arr);

    return arr;
  }

  /**
   * Checks if the resource found in the resource attirbute of a concept exists
   * @param c the concept to check
   * @return the report for the resource of this concept
   */
  private TCCCReportPart checkConceptResource(Concept c) {
    URL u = c.getResourceURL();

    if (u == null) //no resource for this concept
      return new TCCCReportPart(c.getName(), "<no resource>", true);

    String resource = u.toString();

    if (resource == null || resource.equals("")) {//no resource for this concept
      return new TCCCReportPart(c.getName(), "<no resource>", true);
    }

    return new TCCCReportPart(c.getName(), resource, resourceExists(resource));
  }

  /**
   * Checks if the resources mentioned in the casegroup of the showability
   * attribute of a page concept exist.
   * @param c the concept to check
   * @param a the showability attribute
   * @return the reports for the resources of the showability casegroup
   */
  private TCCCReportPart[] checkConceptShowabilityCaseGroup(Concept c, Attribute a) {
    Vector result = new Vector();

    CaseGroup cg = a.getCasegroup();
    if(cg != null) {//Loc Nguyen added October 2009 so as to fix null exception
	    //check default fragment
	    String resource = cg.getDefaultFragment();
	    result.add(new TCCCReportPart(c.getName() + ".showability", resource,
	                                      resourceExists(resource)));
	
	    //check case values
	    Vector v = cg.getCaseValues();
	    for (int i = 0; i<v.size() ;i++ ) {
	      Case cs = (Case)v.get(i);
	      resource = cs.getReturnfragment();
	
	      result.add(new TCCCReportPart(c.getName() + ".showability", resource,
	                                      resourceExists(resource)));
	    }
    }
    //copy vector into array
    TCCCReportPart[] arr = new TCCCReportPart[result.size()];
    result.copyInto(arr);
    return arr;
  }

  /**
   * Checks if a resource exists. Checking for this existence is based on the
   * string that the resource starts with, i.e. <Code>file:/</Code> or
   * <Code>http://</Code>.
   * @param res the resource to check
   * @return true if and only if the resource exists
   */
  private boolean resourceExists(String res) {
    boolean exists = false;

    if (res.startsWith("file:/")) { //resource of form: file:/course1/Concept1.xhtml
      exists = localResourceExists(res);
    }
    else if (res.startsWith("html://")) {//external resource. check if it exists.
      try {
        URL u = new URL(res);
        exists = httpResourceExists(u);
      }
      catch (MalformedURLException e) {
        exists = false;
      }

    }
    else {//add support for more resource types
      try {
        URL u = new URL(res);
        exists = otherResourceExists(u);
      }
      catch (MalformedURLException e) {
        exists = false;
      }
    }

    return exists;
  }

  /**
   * Checks if a resource exists. The resource is known to be of type 'file:/'
   * @param res the resource to check, starting with 'file:/'
   * @return true if and only if the resource exists
   */
  private boolean localResourceExists(String res) {
    String resource = res.substring("file:/".length());

    String respath = wowrootpath + resource;

    File resfile = new File(respath);

    return resfile.exists();
  }

  /**
   * Checks if a resource exists. The resource is known to be of type 'http://'
   * @param u the url of the resource to check
   * @return true if and only if the resource exists
   */
  private boolean httpResourceExists(URL u) {
    try {
      HttpURLConnection.setFollowRedirects(true);
      HttpURLConnection con = (HttpURLConnection)u.openConnection();

      con.setRequestMethod("HEAD");
      if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
        return true;
    }
    catch (Exception e) {}

    return false;
  }

  /**
   * Checks if a resource exists. The type of resource is not of type 'file:/'
   * nor of type 'http://'
   * @param u the url of the resource to check
   * @return true if and only if the resource exists
   */
  private boolean otherResourceExists(URL u) {
    try {
      URLConnection con = (URLConnection)u.openConnection();
      con.connect();

      return true;
    }
    catch (Exception e) {}

    return false;
  }

  /**
   * Constructs a concept list for a certain document (XML file), which should
   * be a <code>.wow</code> file generated by the concept editor
   * @param d the document to scan
   * @return A list of all concepts encountered in the document. The Vector
   * contains elements of type <code>vn.spring.WOW.datacomponents.Concept</code>
   */
  private Vector getConceptList(Document d) {
    Vector concepts = new Vector();
    // Load the concepts from the DOM model
    NodeList nodelist = ((Element)d.getDocumentElement()).getChildNodes();
    Node child = null;
    Concept concept = null;
    for (int i=0; i<nodelist.getLength(); i++) {
        child = nodelist.item(i);
        if (child.getNodeName().equals("concept")) {
            concept = KoenFormat.getKoenConcept(child);
            concepts.add(concept);
        }
    }

    return concepts;
  }

};