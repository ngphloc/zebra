package es.uco.WOW.TestEditor;

import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.UIManager;

/**
 * <p>Title: Wow! TestEditor</p> <p>Description: Herramienta para la edicion
 * de preguntas tipo test adaptativas </p> <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * @version 1.0
 */

/**
 * This class is the super class of all the internal windows that
 * are opened in the TestEditor tool.
 * Contains the main behaviour of them and control of their size.
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 * @version 1.0
 */
public class TestEditorInternalFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Title of the window
	 */
	protected String title;
	/**
	 * Size of the window
	 */
	protected Dimension size;
	/**
	 * Reference to the TestEditor tool
	 */
	protected TestEditor parent;
	/**
	 * The path of the WOW system
	 */
	protected String wowPath;
	/**
	 * Relative path to the folder where the icons of the TestEditor tool are stored
	 */
	protected String iconTestEditorPath;
	/**
	 * Indicates if the frame has been already configured or not
	 */
	protected boolean initFrame = false;

	/**
	 * Creates the class and prepares it to be shown in the parent MDI form.
	 * @param aTitle Title of the window
	 * @param aWidth Width of the window
	 * @param aHeight Height of the window
	 * @param anWOWPath Path of the WOW system in the server
	 * @param iconPath Relative path to the icons folder in the server
	 * @param father A reference to the parent window
	 */
	public TestEditorInternalFrame(final String aTitle, final int aWidth, final int aHeight,
			final TestEditor father, final String anWOWPath, final String iconPath) {
	//{
		super(aTitle, true, true, true, true);
		title = aTitle;
		size = new Dimension(aWidth, aHeight);
		parent = father;
		wowPath = anWOWPath;
		iconTestEditorPath = iconPath; 

		// The frame is not already configured
		initFrame = false;
		
		//	Hides the frame until the window will be totally configured
		setVisible(false);
		
		//	Sets some parameters
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setResizable(true);
		setTitle(title);
		setSize(size);
		
		// Sets the Look And Feel
		try {
			UIManager.setLookAndFeel(father.lookAndFeelClassName);
		} catch (Exception e) {}

		setDoubleBuffered(true);
	}
	//
	
	/**
	 * Makes the location size of the window
	 */
	protected final void windowLocationControl(){
		this.setLocation(0, 0);
	}
	
	/**
	 * Makes the control of size of the window
	 */
	protected final Dimension windowSizeControl () {
		if (!this.isMaximum && !this.isIcon && !this.isClosed) {
			int height = size.height;
			int width = size.height;
			
			// Gets the current size
			int currentHeight = this.getHeight();
			int currentWidth = this.getWidth();
			
			// Makes a pack() to get the minimum required size
			this.pack();
			
			// Gets the pack size
			int packHeight = this.getHeight();
			int packWidth = this.getWidth();
			
			// Calculates the ideal height
			if (packHeight > currentHeight) {
				height = packHeight;
			} else {
				height = currentHeight;
			}
			// Calculates the ideal width
			if (packWidth > currentWidth) {
				width = packWidth;
			} else {
				width = currentWidth;
			}
			
			// It must not be greater than the desktop size
			int desktopHeight = parent.getDesktopPane().getHeight();
			int desktopWidth = parent.getDesktopPane().getWidth();
			
			if (width > desktopWidth){
				width = desktopWidth;
			}
			if (packWidth > desktopWidth){
				packWidth = desktopWidth;
			}
			if (height > desktopHeight){
				height = desktopHeight;
			}
			if (packHeight > desktopHeight){
				packHeight = desktopHeight;
			}
			
			// Resizes the window and sets the minimum allowed size
			size = new Dimension(width, height);
			setSize(size);
			setMinimumSize(new Dimension(packWidth, packHeight));
		}
		return size;
	}

} // End of TestEditorInternalFrame class