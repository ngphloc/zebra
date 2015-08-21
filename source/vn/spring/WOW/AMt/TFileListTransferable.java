//------------------------------------------------------------------------------
// Unit Name: TFileListTransferable.java
// Author: TJ Dekker
// Date of Creation: 22-09-2008
// Purpose: Support class for enabling the drag-transfer of items from a JTree
// for items of type File and TAMtFile
//
// DOCUMENT CHANGES
//
// Date:           Author:             Change:
// -----------------------------------------------------------------------------
// 22-09-2008      TJ Dekker     Creation
//------------------------------------------------------------------------------
package vn.spring.WOW.AMt;

import java.awt.datatransfer.*;
import java.util.List;

/**
 * Support class for enabling the drag-transfer of items from a JTree
 * for items of type File and TAMtFile
 */
public class TFileListTransferable implements Transferable {

 /**
  * Default Constructor
  * Sets the supported dataflavors as DataFlavor.javaFileListFlavor and TAMtFileListFlavor
  */
  public TFileListTransferable() {
    FList = null;
    FAMtArray = null;
    flavors = new DataFlavor[] {DataFlavor.javaFileListFlavor, TAMtFileListFlavor};
  }

  /**
   * Constructor
   * Sets the supported flavors as DataFlavor.javaFileListFlavor
   * @param lst the filelist to store in this transferable
   */
  public TFileListTransferable(java.util.List lst) {
    FList = lst;
    FAMtArray = null;
    flavors = new DataFlavor[] {DataFlavor.javaFileListFlavor};
  }

  /**
   * Constructor
   * Sets the supported flavors as TAMtFileListFlavor
   * @param arr the array of TAMtFiles to store in this transferable
   */
  public TFileListTransferable(TAMtFile[] arr) {
    FAMtArray = arr;
    FList = null;
    flavors = new DataFlavor[] {DataFlavor.javaFileListFlavor, TAMtFileListFlavor};
  }

  /**
   * Retrieves all dataflavors that this transferable supports
   * @return all supported dataflavors
   */
  public DataFlavor[] getTransferDataFlavors() {
    return flavors;
  }

  /**
   * Requests if a certain dataflavor is supported by this transferable
   * @param fl a dataflavor
   * @return <Code>true</Code> if and only if <Code>fl</Code> is supported
   */
  public boolean isDataFlavorSupported(DataFlavor fl) {
    for (int i = 0; i < flavors.length; i++) {
      if (fl.equals(flavors[i])) {
        return true;
      }
    }
    return false;
  }

  /**
   * Retrieves the data contained in this transferable, based on the requested
   * dataflavor.
   * @param fl the dataflavor to retrieve the data for
   * @return the data contained in this transferable, of the type requested by
   * <Code>fl</Code>.
   */
  public Object getTransferData(DataFlavor fl) {
    //System.out.println("getTransferData");
    if (!isDataFlavorSupported(fl)) {
      return null;
    }
    else if (fl.equals(DataFlavor.javaFileListFlavor)) {
      //System.out.println("getTransferData: filelist");
      if (FList != null) {
        //System.out.println("Flist not null");
        return FList;
      }
      else {
        //System.out.println("FList null");
        //TODO: add support for downloading AMtFiles
        return null;
      }
    }
    else if (fl.equals(TAMtFileListFlavor)) {
      //System.out.println("getTransferData: amtfilelist");
      return FAMtArray;
    }
    return null;
  }

  public static final DataFlavor TAMtFileListFlavor =
                new DataFlavor(TAMtFile[].class, "AMtFile");

  private List FList;                  //javaFileListFlavor data
  private TAMtFile[] FAMtArray;        //TAMtFileListFlavor data
  private DataFlavor[] flavors = null; //all supported dataflavors

}