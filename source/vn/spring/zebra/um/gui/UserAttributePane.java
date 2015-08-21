/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.um.PersonalInfo;
import vn.spring.zebra.um.UserConceptInfo;
import vn.spring.zebra.util.CommonUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserAttributePane extends JPanel implements UIDispose {
	private static final long serialVersionUID = 1L;

	protected UserAttributeTable attrTable = null;
	protected JTextField   info = null;
	protected JToolBar toolbar = null;
	
	public UserAttributePane() {
		setLayout(new BorderLayout());
		attrTable = new UserAttributeTable();
		add(new JScrollPane(attrTable), BorderLayout.CENTER);
		
		info = new JTextField(8); info.setEditable(false);
		
		toolbar = new JToolBar(); toolbar.setFloatable(false);
		JButton add = CommonUtil.makeToolButton(
			getClass().getResource(ZebraStatic.IMAGE_DIR + "add-24x24.gif"), "Add", "Add", "Add", attrTable);
		JButton remove = CommonUtil.makeToolButton(
				getClass().getResource(ZebraStatic.IMAGE_DIR + "remove-24x24.gif"), "Remove", "Remove", "Remove", attrTable);
		JButton refresh = CommonUtil.makeToolButton(
				getClass().getResource(ZebraStatic.IMAGE_DIR + "refresh-24x24.gif"), "Refresh", "Refresh", "Refresh", attrTable);
		toolbar.add(add);
		toolbar.add(remove);
		toolbar.add(refresh);
		
		JPanel panel = new JPanel(); panel.setLayout(new BorderLayout());
		panel.add(info, BorderLayout.NORTH);
		panel.add(toolbar, BorderLayout.CENTER);

		add(panel, BorderLayout.NORTH);
		
		setToobarVisible(false); 
	}
	private void setToobarVisible(boolean flag) {toolbar.setVisible(flag);}
	public void setInfo(String info) {this.info.setText(info);}
	
	public void load(String prefix, String userid, 
			ArrayList<String> keys, ArrayList<String> disableKeys, ArrayList<String> hideKeys, ArrayList<String> unremovableKeys,
			boolean isEditable)  throws Exception {
		attrTable.load(prefix, userid, keys, disableKeys, hideKeys, unremovableKeys, isEditable);
		setToobarVisible(isEditable); 
		setInfo("There are " + attrTable.getRowCount() + " attributes");
		attrTable.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				setInfo("There are " + attrTable.getRowCount() + " attributes");
			}
		});
	}
	public void load(PersonalInfo personal) throws Exception {
		attrTable.load(personal);
		setToobarVisible(attrTable.isEditable()); 
		setInfo("There are " + attrTable.getRowCount() + " attributes");
		attrTable.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				setInfo("There are " + attrTable.getRowCount() + " attributes");
			}
		});
	}
	public void load(UserConceptInfo concept) throws Exception {
		attrTable.load(concept);
		setToobarVisible(attrTable.isEditable());
		setInfo("There are " + attrTable.getRowCount() + " attributes");
		attrTable.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				setInfo("There are " + attrTable.getRowCount() + " attributes");
			}
		});
	}
	public void load(Object[][] data, String[] columns) {
		attrTable.load(data, columns);
		setToobarVisible(attrTable.isEditable()); 
		setInfo("There are " + attrTable.getRowCount() + " attributes");
		attrTable.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				setInfo("There are " + attrTable.getRowCount() + " attributes");
			}
		});
	}
	public void clear() {
		attrTable.clear();
		setToobarVisible(false);
		setInfo("");
	}


	public void dispose() {attrTable.dispose();}
	
	public void addNewPersonalAttr() {
		attrTable.addNewAttr();
	}
	
}
