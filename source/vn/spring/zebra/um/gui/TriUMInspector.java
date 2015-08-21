package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.helperservice.UserService;
import vn.spring.zebra.um.TriUM;
import vn.spring.bayes.InterchangeFormat.IFException;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMInspector extends JFrame implements UIDispose {
	private static final long serialVersionUID = 1L;
	
	protected UserService userService = null;
	protected TriUM       triUM = null;

	public TriUMInspector(TriUM triUM) throws IOException, IFException, ZebraException, Exception {
		super();
		this.triUM = triUM;
		initialize();
		setVisible(true);
	}
	public TriUMInspector(String userid, String course) throws IOException, IFException, ZebraException, Exception {
		super();
		TriUM triUM = new TriUM(userid, course);
		this.triUM = triUM;
		initialize();
		setVisible(true);
	}
	
	private JMenuBar createMenuBar() {
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);

		JMenu menuEdit = new JMenu("Edit");
		menuEdit.setMnemonic(KeyEvent.VK_E);
		
		JMenu menuView = new JMenu("View");
		menuView.setMnemonic(KeyEvent.VK_V);
		
		JMenu menuHelp = new JMenu("Help");
		menuHelp.setMnemonic(KeyEvent.VK_H);

		JMenuBar mainMenuBar = new JMenuBar();
		mainMenuBar.add(menuFile);
		mainMenuBar.add(menuEdit);
		mainMenuBar.add(menuView);
		mainMenuBar.add(menuHelp);
		
		return mainMenuBar;
	}
	
	private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
		return toolBar;
	}
	
	private void initialize() throws IOException, ZebraException, IFException, Exception {
		this.setSize(600, 480);
		this.setTitle("Triangular User Model Inspector");
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				doExit();
			}
		});

		this.setJMenuBar(createMenuBar());
		Container contentPanel = this.getContentPane();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(createToolBar(), BorderLayout.NORTH);
		
		if(triUM != null) {
			contentPanel.add(new TriUMPane(triUM, this), BorderLayout.CENTER);
		}
	}

	private void doExit() {
		setVisible(false);
		dispose();
		System.exit(0);
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}
	
}
