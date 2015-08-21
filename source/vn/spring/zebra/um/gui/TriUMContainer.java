/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.util.ArrayList;

import javax.swing.event.ChangeListener;

import vn.spring.zebra.um.TriUM;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public interface TriUMContainer extends UIDispose {
	int       getUMCount();

	TriUM     getUM(int idx);
	TriUM     getUM(String userid, String course);
	TriUM     getCurUM();
	TriUMPane getUMPane(String userid, String course);
	void      setCurUM(String userid, String course);
	
	void      addUM(TriUM um) throws Exception;
	void      update(ArrayList<TriUM> ums) throws Exception;
	
	void      removeUM(String userid, String course);
	void      removeUMs(String userid);
	
	void clearSelection();

	void addChangeListener(ChangeListener l);
    void removeChangeListener(ChangeListener l);

    void refreshShowedInfo();
    void refreshShowedInfo(String userid);
}
