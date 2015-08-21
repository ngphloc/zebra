package es.uco.WOW.TestEditor;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import vn.spring.WOW.config.WowAuthor;
import vn.spring.WOW.util.AuthorLogin;

/**
 * <p>Title: Wow! TestEditor</p> <p>Description: Herramienta para la edicion
 * de preguntas tipo test adaptativas </p> <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: TestEditorApplet 
 * FUNCTION: This class extends from JApplet. Request the
 * user his user name and password, checks it and if both data are correct, then
 * calls the TestEditor class, that is the main frame of the TestEditor Tool.
 * Before create the TestEditor class request the server the list of courses
 * that belongs to the author, to pass this data to the constructor of the
 * TestEditor class. 
 * LAST MODIFICATION: 06-02-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class TestEditorApplet extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Stores the Course objects
	 */
	private Vector courseVector = new Vector();
	/**
	 * Stores the login of the user
	 */
	private String userLogin = "";
	/**
	 * Password of the user
	 */
	private String userPassword = "";
	/**
	 * Folder where the TestEditor tool will be run
	 */
	private String dirname;
	/**
	 * Relative path to the folder that contains the icons used by TestEditor
	 */
	private String iconTestEditorPath;
	/**
	 * Relative path to the folder that contains the items
	 */
	private String itemsPath;
	/**
	 * Codebase of the applet
	 */
	private URL home;


	/**
	 * Default Constructor
	 */
	public TestEditorApplet() {}

	/**
	 * Constructor Creates the TestEditor tool and starts it for an author.
	 * @param codebase the codebase for the applet
	 * @param authorname The name of the author that starts the TestEditor tool
	 */
	public TestEditorApplet(final URL codebase, final String authorname) {
		home = codebase;
		userLogin = authorname;
		startEditor();
	}
	
	/**
	 * Constructor Creates the TestEditor tool and starts it for an author.
	 * @param codebase the codebase for the applet
	 * @param authorname The name of the author that starts the TestEditor tool
	 * @param password The password of the author that starts the TestEditor tool
	 */
	public TestEditorApplet(final URL codebase, final String authorname, final String password) {
		home = codebase;
		userLogin = authorname;
		userPassword = password;
		startEditor();
	}

	/**
	 * Called when the applet is created
	 */
	public void init() {
		home = getCodeBase();
		
		// Creates a AuthorLogin object that request the user login and the password
		AuthorLogin alogin = new AuthorLogin(home);

		// Creates an WowAuthor object, that codifies the password of the user
		WowAuthor author = new WowAuthor();

		// If login or password are not correct, then a error message is showed
		// and deny access to the TestEditor tool
		if (!alogin.login()) {
			try {
				String codebase = home.toString();
				int index = codebase.lastIndexOf("/");
				index = codebase.substring(0, index).lastIndexOf("/");
				String base = codebase.substring(0, index + 1);
				getAppletContext().showDocument(new URL(base + "accessdenied.html"), "_top");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			userLogin = alogin.getUserName();

			// Reads the password from the user
			userPassword = alogin.password;

			// Ciphers the password of the user
			userPassword = author.showHashString(userPassword);

			// Start TestEditor
			startEditor();
		}
	}

	
	/**
	 * Starts the TestEditor for author with name <Code>FAuthorname</Code> and
	 * codebase <Code>home</Code> and sets it visible.
	 */
	private void startEditor() {

		// Gets the path of the system
		String path = home.getPath();
		String pathttemp = path.substring(1, path.length());
		int index = pathttemp.indexOf("/");
		index++;

		// Gets the folder of the TestEditor tool
		dirname = path.substring(0, index);
		if (dirname.equals("/TestEditor"))
			dirname = "";

		// Gets the Course object from the server
		courseVector = getCoursesAndConceptsNames();

		// Gets the icons path used by TestEditor.
		String iconTestEditorAndItemsPath = this.getIconAndItemsPath();
		iconTestEditorPath = iconTestEditorAndItemsPath.substring(0, iconTestEditorAndItemsPath.indexOf("\n"));
		itemsPath = iconTestEditorAndItemsPath.substring(iconTestEditorAndItemsPath.indexOf("\n") + 1);
		itemsPath = itemsPath.substring(0, itemsPath.indexOf("\n"));

		// If there is no courses, a error message is showed to the user
		// and ends the TestEditor
		if (courseVector == null || courseVector.isEmpty()) {
			String message = "ERROR: A problem exists with its author user, " + "\n";
			message += "it is possible that you exist as author, " + "\n";
			message += "but don't have assigned any course, please add courses to its list.";
			JOptionPane.showMessageDialog(this, message, "Without Courses", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		// Creates the TestEditor object
		TestEditor testEditor = new TestEditor(userLogin, userPassword, courseVector, home,
				this.dirname, this.iconTestEditorPath, this.itemsPath);

		// Defines its size and location
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int minimumWidth = 800;
		int minimumHeight = 600;
		if (minimumWidth > screenSize.width) {
			minimumWidth = screenSize.width;
		}
		if (minimumHeight > screenSize.height) {
			minimumHeight = screenSize.height;
		}
		testEditor.setLocation(0, 0);
		testEditor.setSize(screenSize.width, screenSize.height);
		testEditor.setMinimumSize(new Dimension(minimumWidth, minimumHeight));

		// Opens it maximized
		testEditor.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		// Shows to the user
		testEditor.toFront(); // Puts it on the front
		testEditor.setVisible(true);
	}


	/**
	 * Request to the server the name of the courses and the names of the
	 * concepts associated to that courses, that belongs to the author.
	 * This information will be returned in a Vector object that stores
	 * in each position a Course object.
	 * @return A vector object
	 */
	private Vector getCoursesAndConceptsNames() {
		// Stores the Course objects
		Vector vector = null;
		URL url = null;

		// Builds the request string
		String consult = "userLogin=" + userLogin + "&userPassword=" + userPassword;

		try {
			// Creates the petition to the servlet
			String petition = dirname + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets."
					+ "GetCoursesAndConceptsNames";

			url = new URL(home, petition);
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Sends the request
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(consult);
			bufferOut.flush();

			// Gets the response
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());
			vector = (Vector) bufferIn.readObject();

			bufferOut.close();
			bufferIn.close();

		} catch (Exception e) {
			// If any error occurs, a error message is showed to the user
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error taking concepts", 1);
			return null;
		}

		return vector;
	}


	/**
	 * Request the server the relative path where the icons that TestEditor 
	 * tool uses.
	 * @return Relative path to the icons folder
	 */
	private String getIconAndItemsPath() {
		URL url = null;
		String line = "";

		// Builds the request to the servlet
		String consult = "userLogin=" + "" + "&userPassword=" + "";

		try {
			// Creates the final request to the server
			String petition = dirname + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets."
					+ "GetIconAndItemsPath";

			url = new URL(home, petition);
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Sends the request
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(consult);
			bufferOut.flush();

			// Gets the response
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			// Read data
			String lineTemp = "";
			while ((lineTemp = bufferIn.readLine()) != null) {
				line = line.concat(lineTemp) + "\n";
			}

			bufferOut.close();
			bufferIn.close();
			return line;

		} catch (Exception e) {
			// If any error occurs, a error message is showed to the user
			JOptionPane.showMessageDialog(this, e.getMessage(), "Icon Error", 1);
			return null;
		}
	}
	
	
	public static void main (String [] args) {
		try {
			String userLogin = "author";
			String userPassword = "wow";
			URL codebase = new URL("http://localhost:8085/wow/lib/");
			
			// Ciphers the password of the user
			WowAuthor author = new WowAuthor();
			userPassword = author.showHashString(userPassword);
			System.out.println("Arrancando TestEditor en modo local....");
			new TestEditorApplet(codebase, userLogin, userPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

} // End of TestEditorApplet class