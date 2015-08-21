//------------------------------------------------------------------------------
// Unit Name: TAMtProgressBarTableCellRenderer.java
// Author: T.J. Dekker, reviewed and modified by Loc Nguyen
// Date of Creation: 28-09-Author: T.J. Dekker
// Purpose: A TableCellRenderer that displays a JProgressBar as its component
//
// DOCUMENT CHANGES
//
// Date:           Author:             Change:
// -----------------------------------------------------------------------------
// 28-09-2008      T.J. Dekker         Creation
//------------------------------------------------------------------------------

package vn.spring.WOW.AMt;

import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;



/**
 * A TableCellRenderer that displays a JProgressBar as its
 * component.
 * @author T.J. Dekker, changed by Loc Nguyen
 * @version 1.0.0
 */
public class TAMtProgressBarTableCellRenderer extends JProgressBar
                           implements TableCellRenderer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructor
     */
    public TAMtProgressBarTableCellRenderer() {
    }

    /**
     * Renders a cell.
     */
    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {

        if (value instanceof JProgressBar) {
          //System.out.println("drawing progress bar");
          JProgressBar bar = (JProgressBar)value;
          setMinimum(bar.getMinimum());
          setMaximum(bar.getMaximum());
          setValue(bar.getValue());
          return this;
        }
        else {
          DefaultTableCellRenderer tr = new DefaultTableCellRenderer();
          return tr.getTableCellRendererComponent(table, value, isSelected,
            hasFocus, row, column);
        }
    }
}