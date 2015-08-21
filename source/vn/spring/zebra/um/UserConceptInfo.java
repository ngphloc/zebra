/**
 * 
 */
package vn.spring.zebra.um;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import vn.spring.WOW.WOWDB.ConceptDB;
import vn.spring.WOW.datacomponents.Attribute;
import vn.spring.WOW.datacomponents.Concept;
import vn.spring.WOW.datacomponents.DotString;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.exceptions.InvalidAttributeException;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.server.TriUMServer;
import vn.spring.zebra.um.gui.UserAttributeTableModelChangeEvent;
import vn.spring.zebra.um.gui.UserAttributeTableModelChangeListener;
import vn.spring.zebra.util.ConceptUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserConceptInfo implements UserAttributeTableModelChangeListener {
	public boolean isShowTitle = true;
	public boolean isShowMastered = false;

	protected Concept concept = null;
	protected UserInfo userInfo = null;
	
	public UserConceptInfo(UserInfo userInfo, Concept concept) {
		this.userInfo = userInfo;
		this.concept = concept;
	}
	public UserConceptInfo(UserInfo userInfo, String conceptName) throws Exception {
		this.userInfo = userInfo;
		ConceptDB cdb = ZebraStatic.getConceptDB();
		Concept concept = cdb.getConcept(cdb.findConcept(conceptName));
		this.concept = concept;
	}
	public UserConceptInfo(String userid, String conceptName) throws Exception {
		this.userInfo = new UserInfo(userid);
		ConceptDB cdb = ZebraStatic.getConceptDB();
		Concept concept = cdb.getConcept(cdb.findConcept(conceptName));
		this.concept = concept;
	}
	public void updateInfo() throws Exception {
		this.userInfo.updateProfile();
		ConceptDB cdb = ZebraStatic.getConceptDB();
		Concept concept = cdb.getConcept(cdb.findConcept(this.concept.getName()));
		this.concept = concept;
	}
	
	public Concept getConcept() {return concept;}
	public HashMap<String, String> getUserConceptInfo() {
		HashMap<String, String> userConceptInfo = new HashMap<String, String>();
		
		Vector<?> attrs = concept.getAttributes();
		Profile profile = userInfo.getProfile();
		for(int i = 0; i < attrs.size(); i++) {
			Attribute attr = (Attribute)attrs.get(i);
			try {
				String value = profile.getAttributeValue(concept.getName(), attr.getName());
				userConceptInfo.put(attr.getName(), value);
			}
			catch(InvalidAttributeException e) {
				System.out.println("UsersPane.ConceptInfo.getUserConceptInfo causes error: " + e.getMessage());
			}
		}
		return userConceptInfo;
	}
	public void fillInfoTable(JTable infoTable) {
		HashMap<String, String> attrs = getUserConceptInfo();
		Set<String> keys = attrs.keySet();
		String[][] data = new String[keys.size()][2];
		int i = 0;
		for(String key : keys) {
			data[i][0] = key;
			data[i][1] = attrs.get(key);
			i++;
		}
		TableModel tableModel = new DefaultTableModel(data, new String[] {"Attribute", "Value"});
		infoTable.setModel(tableModel);
	}
	
	public String toString() {
		String name = isShowTitle ? concept.getTitle() : getBriefName();
		if(isShowMastered) {
			try {
				TriUMQuery query = TriUMServer.getInstance().getCommunicateService().getQueryDelegator();
	            double curknowledge = ConceptUtil.knowledgeQuery(query, 
	            		userInfo.getUserId(), getCourse(), concept.getName());
	            name += ": " + ((int)(curknowledge * 100.0)) + "% mastered";
			}
			catch(Exception e) {e.printStackTrace();}
		}
		return name;
	}
	public String getBriefName() {
		DotString ds = new DotString(concept.getName());
		if(ds.size() > 1) ds.set(0, null);
		return ds.toString();
	}
	public String getCourse() {
		DotString ds = new DotString(concept.getName());
		return ds.get(0);
	}
	public UserInfo getUserInfo() {return userInfo;}
	
	public boolean isRootConcept() {
		if(concept.getHierStruct() == null)
			return false;
		else if(concept.getHierStruct().parent == null)
			return true;
		else
			return false;
	}
	
	public ArrayList<String> getDisableKeys() {
		return new ArrayList<String>();
	}
	public ArrayList<String> getHideKeys() {
		return new ArrayList<String>();
	}
	public ArrayList<String> getKeys() {
		return new ArrayList<String>(getUserConceptInfo().keySet());
	}
	public ArrayList<String> getUnremovableKeys() {
		return new ArrayList<String>();
	}
	
	public void modelChanged(UserAttributeTableModelChangeEvent evt) {
		try {
			//updateInfo();
		}
		catch(Exception e) {e.printStackTrace();}
	}
}
