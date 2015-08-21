/**
 * 
 */
package vn.spring.zebra.server;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
class TriUMServerUIAbout extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public TriUMServerUIAbout(JFrame frmParent) {
		super(frmParent, "About Dialog", true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});
		try {
	        Image image = Toolkit.getDefaultToolkit().getImage(
	        		getClass().getResource(ZebraStatic.IMAGE_DIR + ZebraStatic.IMAGE_LARGE_ICON_FILENAME));
	        setIconImage(image);
		}
		catch(Exception e) {
			System.out.println("Load Image icon cause error: " + e.getMessage());
			e.printStackTrace();
		}
		JPanel introPane = new JPanel();
		JLabel imageIntro = new JLabel();
		try {
			ImageIcon zebra = new ImageIcon(
				this.getClass().getResource(ZebraStatic.IMAGE_DIR + ZebraStatic.IMAGE_LARGE_ICON_FILENAME)); 
			imageIntro.setIcon(zebra);
		}
		catch(Exception e) {
			System.out.println("Load Image icon cause error: " + e.getMessage());
			e.printStackTrace();
		}
		JTextArea textIntro = new JTextArea("Thank you for choosing Zebra: The User Modeling Server.\n" +
				"User in Zebra is modeled as Triangular User Model constituted of three features:\n" +
				"Knowledge, Learning Style, Learning History");
		textIntro.setWrapStyleWord(true);
		textIntro.setEditable(false);
		introPane.add(imageIntro);
		introPane.add(textIntro);
		
		setLayout(new BorderLayout());
		add(introPane, BorderLayout.CENTER);
		add(
			new JButton(new AbstractAction("OK") {
				private static final long serialVersionUID = 1L;
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
					dispose();
				}
			}), BorderLayout.SOUTH);
		
		setSize(600, 200);
		setVisible(true);
	}
}
