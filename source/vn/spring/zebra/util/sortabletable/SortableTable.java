package vn.spring.zebra.util.sortabletable;

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


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class SortableTable extends JTable {
	private static final long serialVersionUID = 1L;
	
	/** A listener for sorting. */
    private SortableTableHeaderListener headerListener;

    /**
     * Standard constructor - builds a table for the specified model.
     *
     * @param model  the data.
     */
    public SortableTable(final SortableTableModel model) {
        super(model);
        init();
    }
    
    //added by @Loc Nguyen 2010.02.03
    public SortableTable() {
        super(new SortableTableModel());
        init();
    }
    
    //added by @Loc Nguyen 2010.02.03
    private void init() {
    	SortableTableModel model = (SortableTableModel)getModel();

    	final SortButtonRenderer renderer = new SortButtonRenderer();
        final TableColumnModel cm = getColumnModel();
        for (int i = 0; i < cm.getColumnCount(); i++) {
            cm.getColumn(i).setHeaderRenderer(renderer);
        }

        final JTableHeader header = getTableHeader();
        this.headerListener = new SortableTableHeaderListener((SortableTableModel)model, renderer);
        header.addMouseListener(this.headerListener);
        header.addMouseMotionListener(this.headerListener);

    	((SortableTableModel)model).sortByColumn(0, true);
    }
    /**
     * Changes the model for the table.  Takes care of updating the header listener at the
     * same time.
     *
     * @param model  the table model.
     *
     */
    public void setSortableModel(final SortableTableModel model) {
        super.setModel(model);
        this.headerListener.setTableModel(model);
        final SortButtonRenderer renderer = new SortButtonRenderer();
        final TableColumnModel cm = getColumnModel();
        for (int i = 0; i < cm.getColumnCount(); i++) {
            cm.getColumn(i).setHeaderRenderer(renderer);
        }
        model.sortByColumn(0, true);
    }

}

class SortableTableHeaderListener implements MouseListener, MouseMotionListener {

    /** A reference to the table model. */
    private SortableTableModel model;

    /** The header renderer. */
    private SortButtonRenderer renderer;

    /** The index of the column that is sorted - used to determine the state of the renderer. */
    private int sortColumnIndex;

    /**
     * Standard constructor.
     *
     * @param model  the model.
     * @param renderer  the renderer.
     */
    public SortableTableHeaderListener(final SortableTableModel model, 
                                       final SortButtonRenderer renderer) {
        this.model = model;
        this.renderer = renderer;
    }

    /**
     * Sets the table model for the listener.
     *
     * @param model  the model.
     */
    public void setTableModel(final SortableTableModel model) {
        this.model = model;
    }

    /**
     * Handle a mouse press event - if the user is NOT resizing a column and NOT dragging a column
     * then give visual feedback that the column header has been pressed.
     *
     * @param e  the mouse event.
     */
    public void mousePressed(final MouseEvent e) {

        final JTableHeader header = (JTableHeader) e.getComponent();

        if (header.getResizingColumn() == null) {  // resizing takes precedence over sorting
            if (header.getDraggedDistance() < 1) {   // dragging also takes precedence over sorting
                final int columnIndex = header.columnAtPoint(e.getPoint());
                final int modelColumnIndex 
                    = header.getTable().convertColumnIndexToModel(columnIndex);
                if (this.model.isSortable(modelColumnIndex)) {
                    this.sortColumnIndex = header.getTable().convertColumnIndexToModel(columnIndex);
                    this.renderer.setPressedColumn(this.sortColumnIndex);
                    header.repaint();
                    if (header.getTable().isEditing()) {
                        header.getTable().getCellEditor().stopCellEditing();
                    }
                }
                else {
                    this.sortColumnIndex = -1;
                }
            }
        }

    }

    /**
     * If the user is dragging or resizing, then we clear the sort column.
     *
     * @param e  the mouse event.
     */
    public void mouseDragged(final MouseEvent e) {

        final JTableHeader header = (JTableHeader) e.getComponent();

        if ((header.getDraggedDistance() > 0) || (header.getResizingColumn() != null)) {
            this.renderer.setPressedColumn(-1);
            this.sortColumnIndex = -1;
        }
    }

    /**
     * This event is ignored (not required).
     *
     * @param e  the mouse event.
     */
    public void mouseEntered(final MouseEvent e) {
        // not required
    }

    /**
     * This event is ignored (not required).
     *
     * @param e  the mouse event.
     */
    public void mouseClicked(final MouseEvent e) {
        // not required
    }

    /**
     * This event is ignored (not required).
     *
     * @param e  the mouse event.
     */
    public void mouseMoved(final MouseEvent e) {
        // not required
    }

    /**
     * This event is ignored (not required).
     *
     * @param e  the mouse event.
     */
    public void mouseExited(final MouseEvent e) {
        // not required
    }

    /**
     * When the user releases the mouse button, we attempt to sort the table.
     *
     * @param e  the mouse event.
     */
    public void mouseReleased(final MouseEvent e) {

        final JTableHeader header = (JTableHeader) e.getComponent();

        if (header.getResizingColumn() == null) {  // resizing takes precedence over sorting
            if (this.sortColumnIndex != -1) {
                final SortableTableModel model = (SortableTableModel) header.getTable().getModel();
                final boolean ascending = !model.isAscending();
                model.setAscending(ascending);
                model.sortByColumn(this.sortColumnIndex, ascending);

                this.renderer.setPressedColumn(-1);       // clear
                header.repaint();
            }
        }
    }

}


