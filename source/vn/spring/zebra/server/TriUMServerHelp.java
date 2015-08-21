/**
 * 
 */
package vn.spring.zebra.server;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import vn.spring.zebra.ZebraStatic;

/**
 * @author Phuoc - Loc Nguyen
 *
 */
public class TriUMServerHelp extends JFrame {
	private static final long serialVersionUID = 1L;

	public TriUMServerHelp() {
		super("Zebra Server Help Content");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		try {
	        Image image = Toolkit.getDefaultToolkit().getImage(
	        		getClass().getResource(ZebraStatic.IMAGE_DIR + ZebraStatic.IMAGE_LARGE_ICON_FILENAME));
	        setIconImage(image);
		}
		catch(Exception e) {e.printStackTrace();}
		
		Container contentPanel = getContentPane();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(new JLabel("Thank for chosing Zebra! The help content comming soon..."), BorderLayout.NORTH);
		
		setSize(new Dimension(600, 400));
		setVisible(true);
	}

}
