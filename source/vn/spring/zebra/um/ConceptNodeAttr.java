package vn.spring.zebra.um;

import java.util.HashMap;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ConceptNodeAttr {
	private HashMap<String, Object> attrHash = new HashMap<String, Object>();
	
	public Object put(String key, Object value) {
		return attrHash.put(key, value);
	}
	public Object get(String key) {
		return attrHash.get(key);
	}
}
