/**
 * 
 */
package vn.spring.zebra.util;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import vn.spring.zebra.server.TriUMServer;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseList extends JComboBox {
	private static final long serialVersionUID = 1L;

	public CourseList() {
		super();
		try {
			ArrayList<String> cList = 
				TriUMServer.getInstance().getCommunicateService().getQueryDelegator().listCourses();
			setModel(new DefaultComboBoxModel(cList.toArray()));
		}
		catch(Exception e) {e.printStackTrace();}
	}
	
	
}
