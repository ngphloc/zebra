//------------------------------------------------------------------------------
// Unit Name: TCCCReportPart.java
// Author: T.J. Dekker, reviewed and modified by Loc Nguyen
// Date of Creation: 20-09-2008
// Purpose: Type representing the report of an individual concept-resource link
//          for a CCC
//
// DOCUMENT CHANGES
//
// Date:           Author:             Change:
// -----------------------------------------------------------------------------
// 20-09-2008      T.J. Dekker         Creation
//------------------------------------------------------------------------------

package vn.spring.WOW.AMt;

/**
 * Type representing the report of an individual concept-resource link for a
 * CCC.
 * @author T.J. Dekker, changed by Loc Nguyen
 * @version 1.0.0
 */
public class TCCCReportPart implements java.io.Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String FConceptName;
  private String FResourceName;
  private boolean FPassed;

  /**
   * Default Constructor
   * @param aconceptname the name of the concept
   * @param aresourcename the name of the resource
   * @param haspassed <Code>true</Code> if and only if the resource exists
   */
  public TCCCReportPart(String aconceptname, String aresourcename, boolean haspassed) {
    FConceptName = aconceptname;
    FResourceName = aresourcename;
    FPassed = haspassed;
  }

  /**
   * Retrieves the name of the concept
   * @return name of the concept (the FConceptName field)
   */
  public String getConceptName() {
    return FConceptName;
  }

  /**
   * Retrieves the name of the resource
   * @return Name of the resource (the FResourceName field)
   */
  public String getResourceName() {
    return FResourceName;
  }

  /**
   * Retrieves the status for this concept-resource link
   * @return <Code>true</Code> if and only if the resource exists for this
   * concept.
   */
  public boolean isPassed() {
    return FPassed;
  }
};