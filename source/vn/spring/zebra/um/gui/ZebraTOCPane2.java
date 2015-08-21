/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ZebraTOCPane2 extends JPanel implements UIDispose {
	private static final long serialVersionUID = 1L;
	
	protected UserCourseInfoTree userCourseInfoTree = null;
	protected String userid = null;
	protected String course = null;
	
	public ZebraTOCPane2(String userid, String course) {
		super();
		this.userid = userid;
		this.course = course;
		
		try {
			userCourseInfoTree = new UserCourseInfoTree(userid, course);
			userCourseInfoTree.expandAll();
		}
		catch(Exception e) {e.printStackTrace();}
		if(userCourseInfoTree == null) userCourseInfoTree = new UserCourseInfoTree();
		
		TitledBorder title = BorderFactory.createTitledBorder("TABLE OF CONTENT");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		setBorder(title);
		
		setLayout(new BorderLayout());
		JPanel refresh = new JPanel(); refresh.setLayout(new BorderLayout());
		refresh.add(new JPanel(), BorderLayout.CENTER);
		refresh.add(new JButton(new AbstractAction("Refresh") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				try {
					refresh();
				}
				catch(Exception ex) {ex.printStackTrace();}
			}
		}), BorderLayout.WEST);
		add(refresh, BorderLayout.NORTH);
		add(new JScrollPane(userCourseInfoTree), BorderLayout.CENTER);
		
	}
	
	private void refresh() throws Exception {
		userCourseInfoTree.load(userid, course);
		userCourseInfoTree.expandAll();
	}

	public void dispose() {}
}
