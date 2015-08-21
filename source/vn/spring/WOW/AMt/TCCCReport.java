//------------------------------------------------------------------------------
// Unit Name: TCCCReport.java
// Author: T.J. Dekker, reviewed and modified by Loc Nguyen
// Date of Creation: 14-09-2008
// Purpose: Type representing the report for a CCC
//
// DOCUMENT CHANGES
//
// Date:           Author:             Change:
// -----------------------------------------------------------------------------
// 14-09-2008      T.J. Dekker         Creation
//------------------------------------------------------------------------------

package vn.spring.WOW.AMt;

import java.util.Vector;

/**
 * Type representing the report for a CCC.
 * @author T.J. Dekker, changed by Loc Nguyen
 * @version 1.0.0
 */
public class TCCCReport implements java.io.Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String FAuthorname;
  private String FAppname;
  private Vector FReportList;

  /**
   * Constructor
   * @param aauthorname name of the author
   * @param aappname name of the application
   */
  public TCCCReport(String aauthorname, String aappname) {
    FAuthorname = aauthorname;
    FAppname = aappname;
    FReportList = new Vector();
  }

  /**
   * Adds part of the CCC Report to the reportlist
   * @param c the part of the CCC Report
   */
  public void add(TCCCReportPart c) {
    FReportList.add(c);
  }

  /**
   * Retrieves the name of the author
   * @return name of the author (the FAuthorname field)
   */
  public String getAuthorName() {
    return FAuthorname;
  }

  /**
   * Retrieves the name of the application
   * @return name of the application (the FAppname field)
   */
  public String getAppName() {
    return FAppname;
  }

  /**
   * Retrieves the list of all CCCReportParts
   * @return The list of all CCC report parts in Vector representation.
   * Items of the Vector are of type TCCCReportPart.
   */
  public Vector getRepList() {
    return FReportList;
  }

};