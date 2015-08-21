/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import vn.spring.zebra.um.PersonalInfo;
import vn.spring.zebra.um.UserConceptInfo;
import vn.spring.zebra.util.sortabletable.SortableTable;
import vn.spring.zebra.util.sortabletable.SortableTableModel;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserAttributeTable extends SortableTable implements ActionListener, UIDispose {
	private static final long serialVersionUID = 1L;
	protected UserAttributeTableModel model = null;
	protected UserAttributeTableModelChangeListener modelListener = null;
	
	public UserAttributeTable() {
		super();
		initialize();
	}
	public void load(String prefix, String userid, 
			ArrayList<String> keys, ArrayList<String> disableKeys, ArrayList<String> hideKeys, ArrayList<String> unremovableKeys,
			boolean isEditable)  throws Exception {
		model = new UserAttributeTableModel(prefix, userid, keys, disableKeys, hideKeys, unremovableKeys, isEditable);
		setModelEx(model);
		modelListener = null;
	}
	public void load(PersonalInfo personal) throws Exception {
		model = new UserAttributeTableModel(personal);
		setModelEx(model);
		modelListener = personal;
	}
	public void load(UserConceptInfo concept) throws Exception {
		model = new UserAttributeTableModel(concept);
		setModelEx(model);
		modelListener = concept;
	}
	public void load(Object[][] data, String[] columns) {
		model = new UserAttributeTableModel(data, columns);
		setModelEx(model);
		modelListener = null;
	}
	
	public void setModelEx(TableModel dataModel) {
		JTable me = this;
		if(me instanceof SortableTable)
			((SortableTable)me).setSortableModel((SortableTableModel)dataModel);
		else
			me.setModel(dataModel);
	}
	public void clear() {
		model = null;
		JTable me = this;
		if(me instanceof SortableTable)
			((SortableTable)me).setSortableModel(new UserAttributeTableModel());
		else
			me.setModel(new UserAttributeTableModel());
		modelListener = null;
	}
	
	private void initialize() {
		setPreferredScrollableViewportSize(new Dimension(250, 200));
		
		final FloatTextField floatTextField = new FloatTextField(4);
		setDefaultEditor(Float.class, new DefaultCellEditor(floatTextField) {
			private static final long serialVersionUID = 1L;
			@Override
			public Object getCellEditorValue() {
				return new Float((int)(floatTextField.getValue()));
			}
		});
		this.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				if(model == null || !isEditable()) return;
				if(e.getKeyCode() == KeyEvent.VK_DELETE) {
					ActionEvent evt = new ActionEvent(model, 0, "Remove");
					actionPerformed(evt);
				}
			}
		});
	}
	
	public boolean isEditable() {
		if(model == null) return false;
		return model.isEditable();
	}
	
	public void dispose() {if(model != null) model.removeAllChangeListener();}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(model == null) return;
		
		if(cmd.equals("Add")) {
			addNewAttr();
		}
		else if(cmd.equals("Remove")) {
			int selectedRow = getSelectedRow();
			if(selectedRow == -1) {
				JOptionPane.showMessageDialog(null, "You should select one row", "Remove", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			model.removeRow(selectedRow, true);
		}
		else if(cmd.equals("Refresh")) {
			if(modelListener != null) {
				model.reload(modelListener.getKeys());
				JOptionPane.showMessageDialog(null, "Refresh attributes successfully", "Refresh", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		UserAttributeTableModel model = (UserAttributeTableModel)getModel();
		TableCellRenderer renderer = getDefaultRenderer(model.getColumnClass(row, column));
		if(renderer == null) return super.getCellRenderer(row, column);
		
		return renderer;
	}
	
	@Override
    public TableCellEditor getCellEditor(int row, int column) {
    	UserAttributeTableModel model = (UserAttributeTableModel)getModel();
    	TableCellEditor editor = getDefaultEditor(model.getColumnClass(row, column));

    	if(editor == null) return super.getCellEditor(row, column);
        return editor;
    }
	
	public void addNewAttr() {
		if(model != null) {
			UserAttributeAddNewDlg addNewDlg = new UserAttributeAddNewDlg((JFrame)null, model);
			addNewDlg.setVisible(true);
		}
	}
}

class IntegerTextField extends JTextField {
	private static final long serialVersionUID = 1L;
	protected Toolkit toolkit = Toolkit.getDefaultToolkit();

	public IntegerTextField(int cols) {super(cols);}
	public int getValue() {
		try {return Integer.parseInt(getText());}
		catch(Exception e) {
			String err = "Must be digital character";
			System.out.println(err);toolkit.beep();
		}
		return 0;
	}
	public void setValue(int value) {setText(String.valueOf(value));}
	protected Document createDefaultModel() {return new NumberDocument();}
}

class FloatTextField extends JTextField {
	private static final long serialVersionUID = 1L;
	protected Toolkit toolkit = Toolkit.getDefaultToolkit();

	public FloatTextField(int cols) {super(cols);}
	public float getValue() {
		try {return Float.parseFloat(getText());}
		catch(Exception e) {
			String err = "Must be digital character";
			System.out.println(err);toolkit.beep();
		}
		return 0;
	}
	public void setValue(int value) {setText(String.valueOf(value));}
	
	@Override
	protected Document createDefaultModel() {return new NumberDocument();}
	
}

class NumberDocument extends PlainDocument {
	private static final long serialVersionUID = 1L;

	@Override
	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {
		char[] source = str.toCharArray();
		char[] dest = new char[source.length];
		
		int j = 0;
		for(int i = 0; i < source.length; i++) {
			if(Character.isDigit(source[i]) || source[i] == '.') {
				dest[j] = source[i];
				j++;
			}
			else {
				System.out.println("Not number character");
			}
		}
		super.insertString(offs, new String(dest, 0, j), a);
	}
}

