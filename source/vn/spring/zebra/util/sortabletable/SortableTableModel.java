/**
 * 
 */
package vn.spring.zebra.util.sortabletable;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/* 
 * JCommon : a free general purpose class library for the Java(tm) platform
 * 
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
 * 
 * Project Info:  http://www.jfree.org/jcommon/index.html
 *
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, 
 * USA.  
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 * 
 * ------------------
 * SortableTable.java
 * ------------------
 * (C) Copyright 2000-2004, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited), Nobuo Tamemasa
 * Modified by Loc Nguyen 2009
 */

public class SortableTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	/** The column on which the data is sorted (-1 for no sorting). */
    private int sortingColumn = -1;

    /** Indicates ascending (true) or descending (false) order. */
    private boolean ascending = true;

    /**
     * Constructs a sortable table model.
     */
    public SortableTableModel() {
    	super();
    }

	public SortableTableModel(Object[] columnNames, int rowCount) {
		super(columnNames, rowCount);
	}

	public SortableTableModel(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
	}

	/**
     * Returns the index of the sorting column, or -1 if the data is not sorted
     * on any column.
     *
     * @return the column used for sorting.
     */
    public int getSortingColumn() {
        return this.sortingColumn;
    }

    /**
     * Returns <code>true</code> if the data is sorted in ascending order, and
     * <code>false</code> otherwise.
     *
     * @return <code>true</code> if the data is sorted in ascending order, and
     *         <code>false</code> otherwise.
     */
    public boolean isAscending() {
        return this.ascending;
    }

    /**
     * Sets the flag that determines whether the sort order is ascending or
     * descending.
     *
     * @param flag  the flag.
     */
    public void setAscending(final boolean flag) {
        this.ascending = flag;
    }

    /**
     * Sorts the table.
     *
     * @param column  the column to sort on (zero-based index).
     * @param ascending  a flag to indicate ascending order or descending order.
     */
    public void sortByColumn(final int column, final boolean ascending) {
        if (!isSortable(column)) return;
        this.sortingColumn = column;
        Vector data = getDataVector();
        if(data.size() > 0) {
	        Collections.sort(data, new ColumnSorter(column, ascending));
	        fireTableDataChanged();
        }
    }

    /**
     * Returns a flag indicating whether or not a column is sortable.
     *
     * @param column  the column (zero-based index).
     *
     * @return boolean.
     */
    public boolean isSortable(final int column) {
    	
        return false;
    }
}

class ColumnSorter implements Comparator<Vector> { 
	protected int colIndex = 0; 
	protected boolean ascending = true;
	
	public ColumnSorter(int colIndex, boolean ascending) { 
		this.colIndex = colIndex; 
		this.ascending = ascending;
	}
	
	public int compare(Vector a, Vector b) {
		Vector v1 = (Vector)a;
		Vector v2 = (Vector)b;
		Object o1 = v1.get(colIndex);
		Object o2 = v2.get(colIndex);
		
		if(o1 instanceof String && ((String)o1).length() == 0) 
			o1 = null;
		if (o2 instanceof String && ((String)o2).length() == 0)
			o2 = null;
		
		if (o1 == null && o2 == null) return 0;
		else if (o1 == null)          return 1;
		else if (o2 == null)          return -1;
		else if(o1.getClass().getName().equals(o2.getClass().getName())) { 
			if(o1 instanceof Number) {
				double d1 = ((Number)o1).doubleValue(); 
				double d2 = ((Number)o2).doubleValue();
				if(d1 < d2)       return ascending?-1:1;
				else if(d1 == d2) return 0;
				else              return ascending?1:-1;
			}
			else if (o1 instanceof Comparable) {
				if (ascending) return ((Comparable)o1).compareTo(o2);
				else           return ((Comparable)o2).compareTo(o1);
			}
			else { 
				if (ascending) return o1.toString().compareTo(o2.toString());
				else           return o2.toString().compareTo(o1.toString());
			}
		}
		else {
			if (ascending) return o1.toString().compareTo(o2.toString());
			else           return o2.toString().compareTo(o1.toString());
		}
	}
}