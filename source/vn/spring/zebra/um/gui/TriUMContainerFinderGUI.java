/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.util.CommonUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMContainerFinderGUI extends JPanel {
	private static final long serialVersionUID = 1L;

	protected JFrame parent = null;
	protected TriUMContainerFinder finder = null; 

	public TriUMContainerFinderGUI(JFrame parent, TriUMContainer container) {
		super();
		this.parent = parent;
		finder = new TriUMContainerFinder(container);
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
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
		toolbar.setAlignmentX(RIGHT_ALIGNMENT);
		add(toolbar);
		
	}

	private void find() {
		UserCourseFinderInput input = new UserCourseFinderInput(parent);
		if(!input.isOK) return;
		
		TriUMContainerFinder.TriUMContainerQuery query = finder.new TriUMContainerQuery(input.user, input.course);
		
		TriUM found = finder.find(query);
		if(found == null)
			JOptionPane.showMessageDialog(parent, 
					"Not found", "Not found", JOptionPane.INFORMATION_MESSAGE);
		else
			finder.container.setCurUM(found.getUserId(), found.getCourse());
	}
	private void findNext() {
		TriUM found = finder.findNext();
		if(found == null)
			JOptionPane.showMessageDialog(parent, 
					"Not found! End of Finding", "Not found", JOptionPane.INFORMATION_MESSAGE);
		else
			finder.container.setCurUM(found.getUserId(), found.getCourse());
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

class UserCourseFinderInput extends JDialog {
	private static final long serialVersionUID = 1L;
	public boolean isOK = false;
	public String user= null;
	public String course = null;
	
	protected JFrame parent = null;
	protected JTextField userText = null;
	protected JTextField  courseText = null;
	
	public UserCourseFinderInput(JFrame parent) {
		super(parent, "Finder Input", true);
		this.parent = parent;
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new GridLayout(0, 2));
		userText = new JTextField(16);
		mainPane.add(new JLabel("User:"));
		mainPane.add(userText);
		courseText = new JTextField(16);
		mainPane.add(new JLabel("Course:"));
		mainPane.add(courseText);
		
		setLayout(new BorderLayout());
		add(new JLabel("Enter query string"), BorderLayout.NORTH);
		add(mainPane, BorderLayout.CENTER);
		add(
			new JButton(new AbstractAction("OK") {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
					onOK();
				}
			}), BorderLayout.SOUTH);
		pack();
		setVisible(true);
	}

	private void onOK() {
		isOK = true;
		
		user = userText.getText();
		if(user != null && user.length() == 0) {
			user = user.trim();
			if(user.length() == 0) user = null; 
		}
		
		if(courseText.getText() == null)
			course = null;
		else
			course = courseText.getText();
		if(course != null && course.length() == 0) {
			course = course.trim();
			if(course.length() == 0) course = null; 
		}
		
		if(!checkValid()) JOptionPane.showMessageDialog(parent, 
			"You haven't fill in finding box!", "Empty finding box", JOptionPane.INFORMATION_MESSAGE);
		setVisible(false);
		dispose();
	}
	private boolean checkValid() {
		if(user == null && course == null) return false;
		return true;
	}

}
