/**
 * 
 */
package vn.spring.zebra.um;

import java.util.*;
import java.util.Map.Entry;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.AttributeValue;
import vn.spring.WOW.datacomponents.DotString;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.um.gui.UserAttributeTableModelChangeEvent;
import vn.spring.zebra.um.gui.UserAttributeTableModelChangeListener;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class PersonalInfo implements UserAttributeTableModelChangeListener {
	private HashMap<String, AttributeValue> infos = new HashMap<String, AttributeValue>();
	private UserInfo userInfo = null;
	
	public PersonalInfo(UserInfo userInfo) throws Exception {
		this.userInfo = userInfo;
		updateInfo();
	}
	public PersonalInfo(String userid) throws Exception {
		ProfileDB pdb = ZebraStatic.getProfileDB();
		Profile profile = pdb.getProfile(pdb.findProfile(userid));
		this.userInfo = new UserInfo(profile);
		updateInfo();
	}
	public void updateInfo() throws Exception {
		userInfo.updateProfile();
		infos.clear();
		Profile profile = userInfo.getProfile();
		
		Hashtable<?, ?> values = profile.getValues();
		Set<?> keys = values.keySet();
		Iterator<?> iter = keys.iterator();
		while(iter.hasNext()) {
			String key = (String)iter.next();
			DotString ds = new DotString(key, ".");
			if(ds.get(0).equals("personal")) {
				infos.put(key, (AttributeValue)values.get(key));
			}
		}
	}

	public UserInfo getUserInfo() {return userInfo;}
	public HashMap<String, AttributeValue> getInfos() {return infos;}
	public HashMap<String, String> getInfos2() {
		Set<Entry<String, AttributeValue>> entrySet = infos.entrySet();
		HashMap<String, String> infos2 = new HashMap<String, String>();
		for(Entry<String, AttributeValue> entry : entrySet) {
			DotString ds = new DotString(entry.getKey(), ".");
			ds.set(0, null);
			infos2.put(ds.toString(), entry.getValue().getValue());
		}
		return infos2;
	}
	public ArrayList<String> getSortedBriefInfoList() {
		ArrayList<String> list = new ArrayList<String>();
		
		Set<String> keys = infos.keySet();
		Iterator<String> iter = keys.iterator();
		while(iter.hasNext()) {
			String key = (String)iter.next();
			DotString ds = new DotString(key, ".");
			ds.set(0, null);
			list.add(ds.toString() + "=" + infos.get(key).getValue());
		}
		Collections.sort(list);
		return list;
	}
	public ArrayList<String> getInfoValues(ArrayList<String> excludeKeys) {
		ArrayList<String> list = new ArrayList<String>();
		
		Set<String> keys = infos.keySet();
		Iterator<String> iter = keys.iterator();
		while(iter.hasNext()) {
			String key = (String)iter.next();
			DotString ds = new DotString(key);
			if(ds.size() > 1 && ds.get(0).equals("personal")) ds.set(0, null);
			if(excludeKeys.contains(ds.toString())) continue;
			String value = infos.get(key).getValue();
			list.add(value);
		}
		return list;
	}
	
	public void fillInfoTable(JTable infoTable) {
		HashMap<String, String> attrs = getInfos2();
		Set<String> keys = attrs.keySet();
		String[][] data = new String[keys.size()][2];
		int i = 0;
		for(String key : keys) {
			data[i][0] = key;
			if(key.toLowerCase().equals("password"))
				data[i][1] = "***";
			else
				data[i][1] = attrs.get(key);
			i++;
		}
		TableModel tableModel = new DefaultTableModel(data, new String[] {"Attribute", "Value"});
		infoTable.setModel(tableModel);
	}

	public String toString() {
		return "personal";
	}
	
	public ArrayList<String> getDisableKeys() {
		ArrayList<String> attrs = new ArrayList<String>();
		attrs.add("id"); attrs.add("login"); attrs.add("name"); 
		attrs.add("course"); attrs.add("directory");attrs.add("start");attrs.add("title");
		return attrs;
	}
	public ArrayList<String> getHideKeys() {
		ArrayList<String> attrs = new ArrayList<String>();
		attrs.add("password");
		return attrs;
	}
	public ArrayList<String> getUnremovableKeys() {
		ArrayList<String> attrs = new ArrayList<String>();
		attrs.add("id"); attrs.add("login"); attrs.add("name"); attrs.add("password");
		attrs.add("course"); attrs.add("directory"); attrs.add("start");attrs.add("title");
		attrs.add("email");
		attrs.add("umlocked");
		return attrs;
	}
	
	public ArrayList<String> getKeys() {
		return new ArrayList<String>(getInfos2().keySet());
	}
	public void modelChanged(UserAttributeTableModelChangeEvent evt) {
		try {
			//updateInfo();
		}
		catch(Exception e) {e.printStackTrace();}
	}
	public String toXml() throws Exception {
		StringBuffer xml = new StringBuffer();
		HashMap<String, String> infos = getInfos2();
		
		xml.append("<personal>");
		ArrayList<String> keys = getUnremovableKeys();
		for(String key : keys) {
			String value = infos.get(key);
			if(value == null || value.length() == 0) continue;
			infos.remove(key);
			xml.append("\n");
			xml.append("  <attr key=\"" + key + "\">" + value + "</attr>");
		}
		
		keys.clear();
		keys.addAll(infos.keySet());
		for(String key : keys) {
			String value = infos.get(key);
			if(value == null || value.length() == 0) continue;
			xml.append("\n");
			xml.append("  <attr key=\"" + key + "\">" + value + "</attr>");
		}
		
		xml.append("\n");
		xml.append("</personal>");
		return xml.toString();
	}
	
}
