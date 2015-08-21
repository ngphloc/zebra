/**
 * 
 */
package vn.spring.zebra.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserChooser extends JDialog {
	private static final long serialVersionUID = 1L;
	protected UserList userList = null;
	protected ArrayList<String> chosenUserIds = new ArrayList<String>();

	public UserChooser(JFrame parent, ArrayList<String> excludeUserIdList) {
		super(parent, "Choosing User", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		userList = new UserList(new UserListModel(excludeUserIdList));
		userList.setPreferredScrollableViewportSize(new Dimension(600, 400));
		userList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		add(new JScrollPane(userList), BorderLayout.CENTER);
		add(new JLabel("Please choose user(s) and press OK after"), BorderLayout.NORTH);
		
		JPanel toolbar = new JPanel();
		JButton ok = new JButton(new AbstractAction("OK") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				chosenUserIds = userList.getSelectedUserIdList();
				setVisible(false);
				dispose();
			}
		});
		JButton cancel = new JButton(new AbstractAction("Cancel") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				chosenUserIds.clear();
				setVisible(false);
				dispose();
			}
		});
		toolbar.add(ok);
		toolbar.add(cancel);
		add(toolbar, BorderLayout.SOUTH);
		
		setSize(600, 400);
		setVisible(true);
	}
	public ArrayList<String> getChosenUserIds() {return chosenUserIds;}
}
