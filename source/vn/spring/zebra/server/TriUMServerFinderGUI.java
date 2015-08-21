/**
 * 
 */
package vn.spring.zebra.server;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.tree.DefaultMutableTreeNode;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.util.CommonUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServerFinderGUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int  DEFAULT_FIND_TYPE = 
		TriUMServerFinder.FIND_TYPE_USER | TriUMServerFinder.FIND_TYPE_PERSONALINFO;//|| TriUMServerFinder.FIND_TYPE_CONCEPT;
	
	protected TriUMServerUIUserPane  serverUIUser = null;
	protected TriUMServerFinder finder = null; 
	protected JTextField findText = null;
	
	public TriUMServerFinderGUI(TriUMServerUIUserPane serverUIUser) {
		super();
		this.serverUIUser = serverUIUser;
		finder = new TriUMServerFinder(serverUIUser.root);
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		findText = new JTextField(8);
		findText.setToolTipText("Fill query string and press Enter");
		addKeyListener(findText);
		add(findText);
		
		JToolBar toolbar = new JToolBar(); toolbar.setFloatable(false);
		JButton find = CommonUtil.makeToolButton(
			getClass().getResource(ZebraStatic.IMAGE_DIR + "find-24x24.gif"), "Find", "Find (Press F2)", "Find", 
			new AbstractAction() {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
					find();
				}
			});
		addKeyListener(find);
		//
		JButton findnext = CommonUtil.makeToolButton(
			getClass().getResource(ZebraStatic.IMAGE_DIR + "findnext-24x24.gif"), "Find Next", "Find Next (Press F3)", "Find Next", 
			new AbstractAction() {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
					findNext();
				}
			});
		addKeyListener(findnext);
		toolbar.add(find);
		toolbar.add(findnext);
		toolbar.setAlignmentX(LEFT_ALIGNMENT);
		add(toolbar);
		
	}

	private void find() {
		String query = findText.getText();
		query = query.trim();
		if(query.length() == 0) {
			JOptionPane.showMessageDialog(serverUIUser.serverUI, 
					"You should fill in text finding box!", "Empty text finding box", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		DefaultMutableTreeNode found = finder.find(DEFAULT_FIND_TYPE, query);
		if(found == null)
			JOptionPane.showMessageDialog(serverUIUser.serverUI, 
					"Not found", "Not found", JOptionPane.INFORMATION_MESSAGE);
		else
			serverUIUser.selectNode(found);
	}
	private void findNext() {
		DefaultMutableTreeNode found = finder.findNext();
		if(found == null)
			JOptionPane.showMessageDialog(serverUIUser.serverUI, 
					"Not found! End of Finding", "Not found", JOptionPane.INFORMATION_MESSAGE);
		else
			serverUIUser.selectNode(found);
	}
	
	private void addKeyListener(JComponent component) {
		component.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_F2)
					find();
				else if(e.getKeyCode() == KeyEvent.VK_F3)
					findNext();
			}
		});
	}
}
