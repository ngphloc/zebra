//------------------------------------------------------------------------------
// Unit Name: TCCCPopupInterface.java
// Author: T.J. Dekker, reviewed and modified by Loc Nguyen
// Date of Creation: 20-10-2008
// Purpose: Interface for the CCCPopup
//
// DOCUMENT CHANGES
//
// Date:           Author:             Change:
// -----------------------------------------------------------------------------
// 20-10-2008      T.J. Dekker         Creation
//------------------------------------------------------------------------------

package vn.spring.WOW.AMt;


/**
 * Interface for the CCCPopup
 * @author T.J. Dekker, changed by Loc Nguyen
 * @version 1.0.0
 */
public interface TCCCPopupInterface {

  /**
   * Retrieves a CCC report for an application
   * @param aauthorname name of an author
   * @param aappname name of an application
   * @return a CCC report for the requested application
   */
  public TCCCReport getReport(String aauthorname, String aappname);

  /**
   * Opens the <Code>.gaf</Code> file for an application.
   * @param aauthorname name of an author
   * @param aappname name of an application
   */
  public void openGafFile(String aauthorname, String aappname);

  public void openBifFile(String aauthorname, String aappname);
  /**
   * Opens the <Code>.wow</Code> file for an application.
   * @param aauthorname name of an author
   * @param aappname name of an application
   */
  public void openWowFile(String aauthorname, String aappname);

}