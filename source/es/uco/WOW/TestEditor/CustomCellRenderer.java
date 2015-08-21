package es.uco.WOW.TestEditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p> 
 * @version 1.0
 */

/**
 * This class modifies the appearance of the JTree used in TestEditor.
 * Extends from JLabel and implements TreeCellRenderer
 * LAST MODIFICATION: 06-10-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class CustomCellRenderer extends JLabel implements TreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Root folder of the WOW! system
	 */
	private String wowPath;
	/**
	 * Relative path to the folder that contains the icons used by TestEditor
	 */
	private String iconTestEditorPath;
	/**
	 * Icon for the name of the course
	 */
	private ImageIcon iconQuestionsFile16;
	/**
	 * Icon for each question of JTree
	 */
	private ImageIcon iconQuestion16;
	/**
	 * Icon of the information about IRT of the question
	 */
	private ImageIcon iconInfoGreen16;
	/**
	 * Icon of the information about other parameters of the question
	 */
	private ImageIcon iconInfoRed16;
	/**
	 * Icon for the text data showed in the JTree
	 */
	private ImageIcon iconText16;
	/**
	 * Icon for the node that contains all the answers
	 */
	private ImageIcon iconAnswers16;
	/**
	 * Icon for the nodes of each one answer
	 */
	private ImageIcon iconAnswer16;
	/**
	 * Icon for the node that contains the names of the test that the
	 * questions belongs to.
	 */
	private ImageIcon iconTestFiles16;
	/**
	 * Icon for the node of the classic tests
	 */
	private ImageIcon iconClassicTest16;
	/**
	 * Icon for the node of the adaptive tests
	 */
	private ImageIcon iconAdaptiveTest16;
	/**
	 * Indicates if the icons of the buttons have been loaded
	 */
	boolean ImageIconLoad = false;
	/**
	 * Name of the question file that shows the JTree
	 */
	private String questionsFileName;
	/**
	 * URL of the server of the WOW! system
	 */
	private URL codeBase;
	/**
	 * Indicates if the JTree is selected or not
	 */
	private boolean bSelected;
	/**
	 * Font of the JTree
	 */
	private Font theFont = null;

	/**
	 * Default constructor
	 * @param theQuestionsFileName
	 * @param anWowPath
	 * @param aIconTestEditorPath
	 * @param theCodeBase
	 */
	public CustomCellRenderer(String theQuestionsFileName, String anWowPath, String aIconTestEditorPath, URL theCodeBase) {
		this.questionsFileName = theQuestionsFileName;
		this.wowPath = anWowPath;
		this.iconTestEditorPath = aIconTestEditorPath;
		this.codeBase = theCodeBase;

		// Creates the font
		theFont = new java.awt.Font("Arial", 1, 14);
		
		// Calls the method that loads the images
		JImageIconInit();
	}

	
	/**
	 * Sets the value of the current tree cell to value. If selected is true,
	 * the cell will be drawn as if selected.
	 * If expanded is true the node is currently expanded and if leaf is true the node
	 * represets a leaf and if hasFocus is true the node currently has focus.
	 * tree is the JTree the receiver is being configured for.
	 * @return the Component that the renderer uses to draw the value.
	 */
	public Component getTreeCellRendererComponent(JTree jTree, Object value, boolean selected,
			boolean bExpanded, boolean bLeaf, int iRow, boolean bHasFocus) {
		// Sets object variables
		this.bSelected = selected;

		// Sets the node to renderize
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		String labelText = (String) node.getUserObject();

		// Sets the icon that this node must show
		if (node.isLeaf()) {
			setIcon(iconText16);
			setFont(new java.awt.Font("Arial", 0, 14));
		} else {
			if (labelText.indexOf(questionsFileName) != -1 && node.isLeaf() == false) {
				setIcon(iconQuestionsFile16);
				setFont(theFont);
			}
			if (labelText.indexOf("Question") != -1 && labelText.indexOf("Parameters") == -1) {
				setIcon(iconQuestion16);
				setFont(theFont);
			} else {
				if (labelText.indexOf("IRT") != -1) {
					setIcon(iconInfoGreen16);
					setFont(theFont);
				} else {
					if (labelText.indexOf("Parameters") != -1) {
						setIcon(iconInfoRed16);
						setFont(theFont);
					} else {
						if (labelText.indexOf("Answers") != -1) {
							setIcon(iconAnswers16);
							setFont(theFont);
						} else {
							if (labelText.indexOf("Answer") != -1 && labelText.indexOf("Answers") == -1) {
								setIcon(iconAnswer16);
								setFont(theFont);
							} else {
								if (labelText.indexOf("Test Files") != -1) {
									setIcon(iconTestFiles16);
									setFont(theFont);
								} else {
									if (labelText.indexOf("Classic Test") != -1) {
										setIcon(iconClassicTest16);
										setFont(theFont);
									} else {
										if (labelText.indexOf("Adaptive Test") != -1) {
											setIcon(iconAdaptiveTest16);
											setFont(theFont);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		// Adds the text with the icon
		setText(labelText);

		return this;
	}

	
	/**
	 * Paint the JTree. Overwrite the parent method.
	 */
	public void paint(Graphics g) {
		Color bColor = bSelected ? Color.CYAN : Color.WHITE;
		g.setColor(bColor);
		g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
		super.paint(g);
	}


	/**
	 * Initializes the icons that will be showed in the JTree
	 */
	private void JImageIconInit() {
		// Builds the absolute path to the images
		String petitionQuestionsFile16 = wowPath + iconTestEditorPath + "newFile16.gif";
		String petitionQuestion16 = wowPath + iconTestEditorPath + "question16.gif";
		String petitionInfoGreen16 = wowPath + iconTestEditorPath + "info_green_16.gif";
		String petitionInfoRed16 = wowPath + iconTestEditorPath + "info_red_16.gif";
		String petitionAnswers16 = wowPath + iconTestEditorPath + "answers16.gif";
		String petitionAnswer16 = wowPath + iconTestEditorPath + "answer16.gif";
		String petitionText16 = wowPath + iconTestEditorPath + "text16.gif";
		String petitionTestFiles = wowPath + iconTestEditorPath + "testFiles16.gif";
		String petitionClassicTest = wowPath + iconTestEditorPath + "createClassicTest16.gif";
		String petitionAdaptiveTest = wowPath + iconTestEditorPath + "createAdaptiveTest16.gif";

		// Connects with the server
		try {
			URL urlQuestionsFile16 = new URL(codeBase, petitionQuestionsFile16);
			URL urlQuestion16 = new URL(codeBase, petitionQuestion16);
			URL urlInfoGreen16 = new URL(codeBase, petitionInfoGreen16);
			URL urlInfoRed16 = new URL(codeBase, petitionInfoRed16);
			URL urlAnswers16 = new URL(codeBase, petitionAnswers16);
			URL urlAnswer16 = new URL(codeBase, petitionAnswer16);
			URL urlText16 = new URL(codeBase, petitionText16);
			URL urlTestFiles16 = new URL(codeBase, petitionTestFiles);
			URL urlClassicTest16 = new URL(codeBase, petitionClassicTest);
			URL urlAdaptiveTest16 = new URL(codeBase, petitionAdaptiveTest);

			// Creates the ImageIcon objects
			iconQuestionsFile16 = new ImageIcon(urlQuestionsFile16);
			iconQuestion16 = new ImageIcon(urlQuestion16);
			iconInfoGreen16 = new ImageIcon(urlInfoGreen16);
			iconInfoRed16 = new ImageIcon(urlInfoRed16);
			iconAnswers16 = new ImageIcon(urlAnswers16);
			iconAnswer16 = new ImageIcon(urlAnswer16);
			iconText16 = new ImageIcon(urlText16);
			iconTestFiles16 = new ImageIcon(urlTestFiles16);
			iconClassicTest16 = new ImageIcon(urlClassicTest16);
			iconAdaptiveTest16 = new ImageIcon(urlAdaptiveTest16);

			ImageIconLoad = true;
		} catch (java.net.MalformedURLException e) {
			ImageIconLoad = false;
		}
	}

} // End of CustomCellRenderer class