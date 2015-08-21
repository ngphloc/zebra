/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import vn.spring.WOW.WOWDB.ConceptDB;
import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.parser.UMVariableLocator;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.um.PersonalInfo;
import vn.spring.zebra.um.UserConceptInfo;
import vn.spring.zebra.util.sortabletable.SortableTableModel;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserAttributeTableModel extends SortableTableModel implements TableModelListener {
	private static final long serialVersionUID = 1L;
	
	protected String prefix = null;
	protected boolean isEditable = false;
	protected UMVariableLocator umvl = null;
	protected ArrayList<String> disableKeys = new ArrayList<String>();
	protected ArrayList<String> hideKeys = new ArrayList<String>();
	protected ArrayList<String> unremovableKeys = new ArrayList<String>();
	
	protected HashSet<UserAttributeTableModelChangeListener> listeners = new HashSet<UserAttributeTableModelChangeListener>();
	
	private void construct(String prefix, String userid, 
			ArrayList<String> keys, 
			ArrayList<String> disableKeys, 
			ArrayList<String> hideKeys, 
			ArrayList<String> unremovableKeys,
			boolean isEditable) throws Exception {
		this.prefix = prefix;
		ProfileDB pdb = ZebraStatic.getProfileDB();
		Profile   profile = pdb.getProfile(pdb.findProfile(userid));
		ConceptDB cdb = ZebraStatic.getConceptDB();
		this.umvl = new UMVariableLocator(profile, cdb);
		this.isEditable = isEditable;
		this.disableKeys.clear(); this.disableKeys.addAll(disableKeys);
		this.hideKeys.clear(); this.hideKeys.addAll(hideKeys);
		this.unremovableKeys.clear(); this.unremovableKeys.addAll(unremovableKeys);
		
		reload(keys);
		addTableModelListener(this);
	}
	public UserAttributeTableModel(String prefix, String userid, 
			ArrayList<String> keys, 
			ArrayList<String> disableKeys, 
			ArrayList<String> hideKeys, 
			ArrayList<String> unremovableKeys, 
			boolean isEditable) throws Exception {
		super(new String[] {"Attribute", "Value"}, 0);
		construct(prefix, userid, keys, disableKeys, hideKeys, unremovableKeys, isEditable);
	}
	public UserAttributeTableModel(PersonalInfo personal) throws Exception {
		super(new String[] {"Attribute", "Value"}, 0);
		construct("personal", personal.getUserInfo().getUserId(), 
				personal.getKeys(), 
				personal.getDisableKeys(), 
				personal.getHideKeys(),
				personal.getUnremovableKeys(),
				true);
		addChangeListener(personal);
	}
	public UserAttributeTableModel(UserConceptInfo concept) throws Exception {
		super(new String[] {"Attribute", "Value"}, 0);
		construct(concept.getConcept().getName(), concept.getUserInfo().getUserId(), 
				concept.getKeys(), 
				concept.getDisableKeys(), 
				concept.getHideKeys(),
				concept.getUnremovableKeys(),
				false);
		addChangeListener(concept);
	}
	public UserAttributeTableModel(Object[][] data, String[] columns) {
		super(data, columns);
		isEditable = false;
	}
	public UserAttributeTableModel() {
		super(/*new Object[] {"Attribute", "Value"}, 0*/);
		isEditable = false;
	}
	
	public Class<?> getColumnClass(int rowIndex, int columnIndex) {
		if(columnIndex == 0) return String.class;
		
		Object value = null;
		try {
			value = umvl.getVariableValue(prefix + "." + getValueAt(rowIndex, 0));
		}
		catch(Exception e) {value = null;}
		
		if(value == null) return super.getColumnClass(columnIndex);
		return value.getClass();		
	}

	protected void reload(ArrayList<String> keys) {
		removeAll(false);
		Collections.sort(keys);
		for(String key : keys) {
			try {
				addRow(this.prefix + "." + key, false);
			}
			catch(Exception e) {e.printStackTrace();}
		}
	}
	private void removeAll(boolean isUpdateDB) {
		while(getRowCount() > 0) {
			if(isUpdateDB) {
				String name = (String)getValueAt(0, 0);
				removeDB(name);
			}
			removeRow(0);
		}
	}
	
	public void addRow(String variable, boolean isUpdateDB) {
		String name = null;
		Object value = null;
		try {
			value = umvl.getVariableValue(variable);
		}
		catch(Exception e) {e.printStackTrace();}
		if(value == null) return;
		
		if(prefix == null || prefix.length() == 0) {
			name = variable;
		}
		else {
			int idx = variable.indexOf(prefix + ".");
			if(idx != 0) return;
			name = variable.substring((prefix + ".").length());
		}
		addRow(name, value, isUpdateDB);
	}
	public void addRow(String name, Object value, boolean isUpdateDB) {
		if(hideKeys.contains(name) || checkExist(name)) {
			return;
		}
		addRow(new Object[] {name, value});
		if(isUpdateDB) {
			updateDB(name, value);
		}
	}
	public void removeRow(int row, boolean isUpdateDB) {
		String name = (String)getValueAt(row, 0);
		if(isUpdateDB && (disableKeys.contains(name)  || unremovableKeys.contains(name))) {
			return;
		}
		
		super.removeRow(row);
		if(isUpdateDB) {
			removeDB(name);
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		if(column == 0 || !isEditable) return false;
		String name = (String)getValueAt(row, 0);
		if(disableKeys.contains(name)) return false;
		return super.isCellEditable(row, column);
	}
	
	@Override
	public void setValueAt(Object value, int row, int column) {
		if(value instanceof String) {
			String sValue = ((String)value).trim();
			if(sValue.length() == 0) return;
		}
		
		super.setValueAt(value, row, column);
	}
	public void updateDB() {
		Profile profile = umvl.getProfile();
		ConceptDB cdb = umvl.getConceptDB();
		for(int i = 0; i < getRowCount(); i++) {
			String name = (String)getValueAt(i, 0);
			if(disableKeys.contains(name) || hideKeys.contains(name)) continue;
			
			String value = String.valueOf(getValueAt(i, 1));
			value = value.trim();
			if(value.length() == 0) continue;
			
			try {
				profile.setAttributeValue(cdb, prefix, name, value);
			}
			catch(Exception e) {e.printStackTrace();}
		}
		
		try {
			ProfileDB pdb = ZebraStatic.getProfileDB();
			pdb.setProfile(profile.id, profile);
		}
		catch(Exception e) {e.printStackTrace();}
		fireChangeEvent(new UserAttributeTableModelChangeEvent(this));
	}
	
	public boolean isEditable() {return isEditable;}
	
	public void tableChanged(TableModelEvent e) {
		if(e.getType()== TableModelEvent.UPDATE) {
			if(getRowCount() == 0 || getColumnCount() == 0) return;
			
			int row = e.getFirstRow();
			int col = e.getColumn();
			if(col <= 0) return;
			String attrName = (String)getValueAt(row, 0);
			Object attrValue = getValueAt(row, col);
			updateDB(attrName, attrValue);
		}
	}
	private void updateDB(String attrName, Object attrValue) {
		try {
			Profile profile = umvl.getProfile();
			ConceptDB cdb = umvl.getConceptDB();
			ProfileDB pdb = ZebraStatic.getProfileDB();
			profile.setAttributeValue(cdb, prefix, attrName, String.valueOf(attrValue));

			pdb.setProfile(profile.id, profile);
		}
		catch(Exception e) {e.printStackTrace();}
		fireChangeEvent(new UserAttributeTableModelChangeEvent(this));
	}
	private void removeDB(String attrName) {
		try {
			Profile profile = umvl.getProfile();
			ProfileDB pdb = ZebraStatic.getProfileDB();
			profile.removeAttribute(prefix, attrName);

			pdb.setProfile(profile.id, profile);
		}
		catch(Exception e) {e.printStackTrace();}
		fireChangeEvent(new UserAttributeTableModelChangeEvent(this));
	}
	
	protected boolean checkExist(String attrName) {
		for(int i = 0; i < getRowCount(); i++) {
			String name = (String)getValueAt(i, 0);
			if(name.equals(attrName)) return true;
		}
		return false;
	}

	public void addChangeListener(UserAttributeTableModelChangeListener listener) {
		listeners.add(listener);
	}
	public void removeChangeListener(UserAttributeTableModelChangeListener listener) {
		listeners.remove(listener);
	}
	public void removeAllChangeListener() {
		listeners.clear();
	}
	
	@Override
	public boolean isSortable(int column) {
		return (column >= 0 && column < getColumnCount());
	}

	private void fireChangeEvent (UserAttributeTableModelChangeEvent evt) {
		for(UserAttributeTableModelChangeListener listener : listeners) {
			try {
				listener.modelChanged(evt);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public Object getValueAt(int row, int column) {
		if(getRowCount() == 0 || getColumnCount() == 0) return null;
		return super.getValueAt(row, column);
	}
	
}
