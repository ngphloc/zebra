package vn.spring.zebra.util.xmlviewer;

import java.awt.Color;
import java.awt.Dimension;
//import java.awt.event.InputEvent;
//import java.awt.event.MouseEvent;
//import java.util.EventObject;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.TreeTableModel;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class XMLTreeTable extends JXTreeTable { 
	private static final long serialVersionUID = 1L;
	
    public XMLTreeTable(){ 
        super(); 
        setShowGrid(true); 
        setGridColor(new Color(234, 234, 234)); 
        setIntercellSpacing(new Dimension(1, 1));
        setTreeCellRenderer(new XMLTreeTableCellRenderer());
        new TableColumnResizer(this);
    } 
    public XMLTreeTable(TreeTableModel treeTableModel){ 
        this();
        setTreeTableModel(treeTableModel);
    } 
 
//    public boolean editCellAt(int row, int column, EventObject e){ 
//        if(e instanceof MouseEvent){ 
//            MouseEvent me = (MouseEvent)e; 
//            // If the modifiers are not 0 (or the left mouse button), 
//            // tree may try and toggle the selection, and table 
//            // will then try and toggle, resulting in the 
//            // selection remaining the same. To avoid this, we 
//            // only dispatch when the modifiers are 0 (or the left mouse 
//            // button). 
//            if(me.getModifiers()==0 || me.getModifiers()==InputEvent.BUTTON1_MASK) { 
//                for(int counter = getColumnCount()-1; counter>= 0; counter--){ 
//                    if(getColumnClass(counter)==TreeTableModel.class){ 
//                        MouseEvent newME = new MouseEvent 
//                                (tree, me.getID(), 
//                                        me.getWhen(), me.getModifiers(), 
//                                        me.getX()-getCellRect(0, counter, true).x, 
//                                        me.getY(), me.getClickCount(), 
//                                        me.isPopupTrigger()); 
//                        tree.dispatchEvent(newME); 
//                        break; 
//                    } 
//                } 
//            } 
//            return false; 
//        } 
//        return super.editCellAt(row, column, e); 
//    } 
    
    //public boolean getScrollableTracksViewportHeight() { 
    //    return getPreferredSize().height < getParent().getHeight();
    //}

	@Override
	protected void configureEnclosingScrollPane() {
		// TODO Auto-generated method stub
		//super.configureEnclosingScrollPane();
	}
    
}
