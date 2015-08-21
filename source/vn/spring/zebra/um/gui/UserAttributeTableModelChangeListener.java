/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.util.ArrayList;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public interface UserAttributeTableModelChangeListener {
	void modelChanged(UserAttributeTableModelChangeEvent evt);
	ArrayList<String> getKeys();
	ArrayList<String> getDisableKeys();
	ArrayList<String> getHideKeys();
	ArrayList<String> getUnremovableKeys();
}
