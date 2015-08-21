/**
 * 
 */
package vn.spring.zebra.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMQueryFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public TriUMQueryFrame(JFrame frmParent) {
		super("Triangular User Model (TLM) Query Client many network protocols");
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(new TriUMQueryGUI(null));
		
		
		setSize(500, 650);
		//pack();
	}
}