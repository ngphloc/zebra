package es.uco.WOW.TestEditor;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/*
 NOMBRE: CustomCellRenderer.
 FUNCION: Clase que ser� usada para modificar el aspecto de los JTable usados
          en la herramienta TestEditor. Extiende de AbstracTableModel.
 LAST MODIFICATION: 06-02-2008
*/

class TableModel extends AbstractTableModel
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public Vector rowsVector; //Contiene los valores de las filas de la tabla.
  public Vector columnsVector; //Contiene los nombre de las columnas.

  public TableModel(Vector aRowsVector, Vector aColumnsVector)
  {
    this.rowsVector = aRowsVector;
    this.columnsVector = aColumnsVector;
  }

  public int getColumnCount() {
    return columnsVector.size();
  }
  public int getRowCount() {
    return rowsVector.size();
  }

  public String getColumnName(int col) {
    return columnsVector.get(col).toString();
  }

  public Object getValueAt(int row, int col) {
    return ((Vector)rowsVector.get(row)).get(col).toString();
  }

  public Class getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }
}