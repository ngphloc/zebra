//------------------------------------------------------------------------------
// Unit Name: TFileSystemTreeEditor.java
// Author: T.J. Dekker, reviewed and modified by Loc Nguyen
// Date of Creation: 29-09-2008
// Purpose: Type representing the editor for a filesystem tree. The editor
// allows editing of a JTree containing DefaultMutableTreeNodes with UserObject
// of type java.io.File or TAMtFile
//
// DOCUMENT CHANGES
//
// Date:           Author:             Change:
// -----------------------------------------------------------------------------
// 29-09-2008      T.J. Dekker         Creation
//------------------------------------------------------------------------------

package vn.spring.WOW.AMt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.Object;
import java.util.EventObject;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeCellEditor;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;


/**
 * Type representing the editor for a filesystem tree. The editor
 * allows editing of a JTree containing DefaultMutableTreeNodes with UserObject
 * of type java.io.File or TAMtFile
 * @author T.J. Dekker, changed by Loc Nguyen
 * @version 1.0.0
 */
public class TFileSystemTreeEditor extends JPanel
              implements TreeCellEditor, ActionListener, TreeSelectionListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private AMtClientGUI FGui;                  //user interface
  private JTree FTree;                        //tree being edited
  private TFileSystemTreeRenderer FRenderer;  //renderer of tree
  private CellEditorListener FListener;       //listener of cell editor
  private JLabel lblIcon;                     //contains icon for node being edited
  private JTextField txt;                     //textfield for node being edited
  private TreePath lastPath = null;           //last selected path in FTree
  private DefaultMutableTreeNode FEditValue;  //value of the node being edited
  private Timer tmr = null;                   //timer indicating the start of an editing session
  private TreePath tmrPath = null;            //path selected in FTree at tmr start

  /**
   * Constructor
   * @param gui User Interface
   * @param tree the tree being edited
   * @param renderer the renderer for the tree
   */
  public TFileSystemTreeEditor(AMtClientGUI gui, JTree tree,
                                          TFileSystemTreeRenderer renderer) {
    super();

    FGui = gui;

    setTree(tree);
    FRenderer = renderer;
    FListener = null;
    FEditValue = null;

    //create panel

    setLayout(null);

    //create label
    lblIcon = new JLabel();
    add(lblIcon);

    //create textfield
    txt = new JTextField();
    txt.setEditable(true);
    txt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        //signal editing stopped
        //System.out.println("actionperformed");
        Object obj = renameFile();
        FEditValue.setUserObject(obj);

        if (FListener != null)
          FListener.editingStopped(new ChangeEvent(FEditValue));
      }
    });

    txt.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyTyped(java.awt.event.KeyEvent evt) {
          setCompSize();
        }
    });

    add(txt);

    Color bg = new Color(255,255,255);
    setBackground(bg);
    txt.setBackground(bg);
    lblIcon.setBackground(bg);
  }

  /**
   * Edits a node
   * @return a JPanel containing an icon and a textfield.
   */
  public Component getTreeCellEditorComponent(JTree tree, Object value,
    boolean isSelected, boolean expanded, boolean leaf, int row) {

    setTree(tree);

    //find icon and text for node to be edited
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
    Object obj = node.getUserObject();
    FEditValue = node;


    if (obj instanceof File) {//set Text and Icon
      lblIcon.setIcon(FRenderer.getIcon((File)obj));
      txt.setText(((File)obj).getName());
    }
    else if (obj instanceof TAMtFile) {//set Text and Icon
      lblIcon.setIcon(FRenderer.getIcon((TAMtFile)obj));
      txt.setText(((TAMtFile)obj).getName());
    }

    txt.setSelectionStart(0);
    txt.setSelectionEnd(txt.getText().length());

    //set txt and icon sizes
    setCompSize();

    return this;
  }

  /**
   * Adds a listener to the list that's notified when the editor stops, or
   * cancels editing.
   */
  public void addCellEditorListener(CellEditorListener c) {
    FListener = c;
  }

  /**
   * Tells the editor to cancel editing and not accept any partially edited
   * value.
   */
  public void cancelCellEditing() {
    FTree.cancelEditing();
    FListener.editingCanceled(new ChangeEvent(FEditValue));
  }

  /**
   * retrieves the value of this CellEditor
   * @return the value contained in the editor.
   */
  public Object getCellEditorValue() {

    Object obj = FEditValue.getUserObject();

    if (obj instanceof File) {//rename file
      File f = (File)obj;
      String filepath =
        f.getParent() +
        (f.getParent().endsWith(File.separator)?"":File.separator) +
        txt.getText();

      new File(filepath);

    }
    else if (obj instanceof TAMtFile) {//rename amtfile
      TAMtFile f = (TAMtFile)obj;
      TAMtFile newf = (TAMtFile)f.clone();
      newf.setName(txt.getText());

    }
    else {//illegal node type
      System.out.println("TFileSystemTreeEditor.getCellEditorValue(): Illegal node type");
      obj = "Illegal node type";
      return obj;
    }


    return obj;
  }

  /**
   * Asks the editor if it can start editing using anEvent.
   * @param anEvent the event that triggered this action
   * @return <Code>true</Code> if and only if the cell is editable with the
   * given event
   */
  public boolean isCellEditable(EventObject anEvent) {

    if (anEvent == null) //event probably called through FTree.startEditingPath
      return true;

    if(anEvent.getSource() instanceof JTree)
      setTree((JTree)anEvent.getSource());

    if (anEvent instanceof MouseEvent) {
      //cell editable if clicked on same component twice within 1,2 sec
      MouseEvent evt = (MouseEvent)anEvent;

      TreePath tp = FTree.getClosestPathForLocation(evt.getX(), evt.getY());

      if (evt.getClickCount() == 1 && evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
        //check if on same component as last click
        if (tp.equals(lastPath)) {
          tmr = new Timer(1200, this);
          tmr.setRepeats(false);

          tmrPath = tp;
          tmr.start();
        }

      }
      else {
        if (tmr != null && tmr.isRunning()) {
          tmr.stop();
          tmrPath = null;
        }
      }
    }

    return false;
  }

  /**
   * Removes a listener from the list that's notified
   */
  public void removeCellEditorListener(CellEditorListener l) {
    if (FListener.equals(l)) FListener = null;
  }

  /**
   * If a cell should be selected. Always returns false
   * @return <Code>false</Code>
   */
  public boolean shouldSelectCell(EventObject anEvent) {
    return false;
  }

  /**
   * Tells the editor to stop editing and accept any partially edited value
   * as the value of the editor.
   * @return <Code>true</Code>
   */
  public boolean stopCellEditing() {
    return true;
  }

  /**
   * Sets the size of this component, which is a panel containing an icon
   * and a textfield. The size of the component is set the same as the size
   * of the node in FTree
   */
  private void setCompSize() {
    Graphics g = FTree.getGraphics();
    FontMetrics fm = g.getFontMetrics();

    Rectangle r = FTree.getRowBounds(0);

    //set icon bounds
    Icon icn = lblIcon.getIcon();
    lblIcon.setBounds(0, 0, icn.getIconWidth(), icn.getIconHeight());

    //set txt bounds
    int txtx = icn.getIconWidth()+3;
    int txtwidth = fm.stringWidth(txt.getText())+10;
    int pnlwidth = Math.min(txtx+txtwidth, FTree.getWidth());
    int pnlheight = Math.max(icn.getIconHeight(),
      new Double(r.getHeight()).intValue());

    txt.setBounds(txtx, 0, pnlwidth-txtx, pnlheight);

    //set panel bounds
    Dimension prefSize = new Dimension(pnlwidth, pnlheight);
    setMinimumSize(prefSize);
    setPreferredSize(prefSize);

    setBounds(getX(), getY(), pnlwidth, getHeight());
  }

  /**
   * Renames the node of FTree that is currently being edited
   * @return renamed userobject of FEditValue
   */
  private Object renameFile() {
    Object obj = FEditValue.getUserObject();

    if (obj instanceof File) {//rename file
      File f = (File)obj;
      String filepath =
        f.getParent() +
        (f.getParent().endsWith(File.separator)?"":File.separator) +
        txt.getText();

      File newf = new File(filepath);

      int error = FGui.renameFile(f, newf);

      if (error == AMtc.NO_ERRORS) //renaming succeeded
        return newf;
      else {//renaming failed
        FGui.showError(error);
        return f;
      }
    }
    else if (obj instanceof TAMtFile) {//rename amtfile
      TAMtFile f = (TAMtFile)obj;
      TAMtFile newf = (TAMtFile)f.clone();
      newf.setName(txt.getText());

      if (!f.getPath().equals("/")) {
        String oldpath = f.getPath();
        newf.setPath(oldpath.substring(0, oldpath.lastIndexOf(f.getName())) + newf.getName());
      }

      int error = FGui.renameFile(f, newf);

      if (error == AMtc.NO_ERRORS) //renaming succeeded
        return newf;
      else {//renaming failed
        FGui.showError(error);
        return f;
      }
    }
    else {//illegal node type
      System.out.println("TFileSystemTreeEditor.renameFile(): Illegal node type");
      obj = "Illegal node type";
      return obj;
    }
  }

  /**
   * ActionPerformed event for Timer tmr. Starts editing if the currently
   * selected path is the same as the selected path on timer start
   * @param e the event that triggered this action
   */
  public void actionPerformed(ActionEvent e) {
    if (tmrPath.equals(lastPath))
      FTree.startEditingAtPath(tmrPath);
  }


  /**
   * Responds to a change in selection of FTree.
   * @param treeselectionevent the event that triggered this action
   */
  public void valueChanged(TreeSelectionEvent treeselectionevent) {
    if(FTree != null) {
      if(FTree.getSelectionCount() == 1)
        lastPath = FTree.getSelectionPath();
      else
        lastPath = null;
    }
    if(tmr != null)
      tmr.stop();
  }

  /**
   * sets FTree and adds this selectionlistener to it if it did not already
   * was added before.
   * @param tri the tree to set
   */
  protected void setTree(JTree tri) {
    if(FTree != tri) {
      if(FTree != null)
        FTree.removeTreeSelectionListener(this);

      FTree = tri;

      if(FTree != null)
        FTree.addTreeSelectionListener(this);

      if(tmr != null)
        tmr.stop();
    }
  }


};