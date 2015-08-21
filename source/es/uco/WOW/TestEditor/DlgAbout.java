package es.uco.WOW.TestEditor;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p> 
 * @version 1.0
 */

/**
 * This class shows the user the information about the version
 * of the TestEditor Tool that is being run. This dialog window is based
 * in the AboutBox class of the ConceptEditor tool that exists in the WOW! system.
 * LAST MODIFICATION: 06-10-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class DlgAbout {

	/**
	 * The path of the WOW system
	 */
	protected String wowPath;
	/**
	 * Relative path to the folder where the icons of the TestEditor tool are stored
	 */
	protected String iconTestEditorPath;
	
	private TitledBorder titledBorder;

	private JPanel jPanelTitle = new JPanel();

	private JPanel jPanelButtonGif = new JPanel();

	private JPanel jPanelTextArea = new JPanel();

	private JLabel jLabelInfo = new JLabel();

	private URL base = null;

	private JDialog jDialog;

	private JTextArea jTextAreaInfo = new JTextArea();

	private JButton jButtonOk = new JButton();

	
	/**
	 * Constructor.
	 * @param parent Reference to the TestEditor tool that is running.
	 * @param theBase Base URL of the TestEditor.
	 * @param anWOWPath Path to the WOW! System.
	 * @param aIconPath Relative path to the icons in the WOW! system.
	 */
	public DlgAbout(
		final TestEditor parent,
		final URL theBase,
		final String anWOWPath,
		final String aIconPath) {
	//{
		this.base = theBase;
		this.wowPath = anWOWPath;
		this.iconTestEditorPath = aIconPath;
		
		// Sets the look and feel of the window
		try {
			UIManager.setLookAndFeel(parent.lookAndFeelClassName);
		} catch (Exception e) {}

		try {
			JDialogInit();
			jDialog = new JDialog(parent, "Test Editor application", true);
			jDialog.setSize(500, 430);
			jDialog.setLocation(100, 100);
			jDialog.getContentPane().add(jPanelTitle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Initializes the window.
	 * @throws Exception Error initializing window
	 */
	private void JDialogInit() throws Exception {
		titledBorder = new TitledBorder("");
		jLabelInfo.setFont(new java.awt.Font("Dialog", 0, 18));
		jLabelInfo.setBorder(titledBorder);
		jLabelInfo.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelInfo.setText("About Test Editor");
		jLabelInfo.setBounds(new Rectangle(11, 7, 463, 47));
		jPanelTitle.setLayout(null);

		jPanelButtonGif.setBorder(BorderFactory.createEtchedBorder());
		jPanelButtonGif.setBounds(new Rectangle(12, 61, 464, 139));
		jPanelButtonGif.setLayout(null);

		URL url = null;

		try {
			url = new URL(base, wowPath + iconTestEditorPath + "hcmuns-te.gif");
		} catch (Exception e) {}

		JButton buttonGif = new JButton(new ImageIcon(url));
		buttonGif.setBounds(new Rectangle(1, 1, 461, 131));
		buttonGif.setBorder(null);
		jPanelTextArea.setBorder(BorderFactory.createEtchedBorder());
		jPanelTextArea.setBounds(new Rectangle(12, 206, 465, 99));
		jPanelTextArea.setLayout(null);

		jTextAreaInfo.setText(" Test Editor v 1.00 " + "\n" + " Design by: Cristobal Romero Morales " + "\n modified by: Loc Nguyen (October 2009)\n"
				+ " Build date: " + "20080122");
		jTextAreaInfo.setBounds(new Rectangle(5, 6, 455, 84));
		jButtonOk.setBounds(new Rectangle(186, 315, 73, 29));
		jButtonOk.setText("Ok");
		jButtonOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonOk_actionPerformed(e);
			}
		});
		jPanelTitle.add(jLabelInfo, null);
		jPanelTitle.add(jPanelButtonGif, null);
		jPanelButtonGif.add(buttonGif);
		jPanelTitle.add(jPanelTextArea, null);
		jPanelTextArea.add(jTextAreaInfo, null);
		jPanelTitle.add(jButtonOk, null);
	}

	/**
	 * Shows the window
	 */
	public void show() {
		jDialog.setVisible(true);
	}

	/**
	 * Closes the window
	 */
	void jButtonOk_actionPerformed(final ActionEvent e) {
		jDialog.setVisible(false);
	}

} // End of DlgAbout class