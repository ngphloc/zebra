package es.uco.WOW.TestEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import es.uco.WOW.Utils.Course;
import es.uco.WOW.Utils.Question;
import es.uco.WOW.Utils.Student;
import es.uco.WOW.Utils.Test;
import es.uco.WOW.Utils.UtilZip;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p> 
 * @version 1.0
 */

/**
 * This class extends from JFrame. This is the principal frame of the applet.
 * Shows the user the main graphic interface and the main options of the TestEditor tool
 * LAST MODIFICATION: 04-09-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class TestEditor extends JFrame implements ClipboardOwner {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Constants used in application
	/** Classic test type */
	public static final String CLASSIC = "classic";
	public static final int CLASSIC_NUM = 1;

	/** Adaptive test type */
	public static final String ADAPTIVE = "adaptive";
	public static final int ADAPTIVE_NUM = 2;

	/** All types of tests */
	public static final String ALL_TEST = "all";

	public static final String SEQUENTIAL_STR = "SEQUENTIAL";
	public static final String RANDOM_STR = "RANDOM";
	
	public static final int MANUALLY = 1;
	public static final int RANDOM = 2;
	public static final int RANDOM_WITH_RESTRICTIONS = 3;
	
	public static final String ACTIVITY = "activity";
	public static final int ACTIVITY_NUM = 1;

	public static final String EXAM = "exam";
	public static final int EXAM_NUM = 2;
	
	protected static final int NEW_QUESTION_FILE = 1;
	protected static final int MODIFY_QUESTION_OF_A_FILE = 2;
	
	protected static final int EXAM_QUESTION_FILE = 1;
	protected static final int FIND_QUESTIONS_OF_A_FILE = 2;
	
	protected static final int NUMBER_OF_ANSWERS_MAX = 10;
	protected static final int NUMBER_OF_ANSWERS_MIN = 2;
	
	/**
	 * Text returned by the server when a question has been deleted
	 */
	public final static String TEXT_QUESTION_DELETED = "The question has been deleted";
	/**
	 * Text returned by the server if the question has been saved
	 */
	public static final String TEXT_QUESTION_SAVED = "Question Saved";
	/**
	 * Text returned by the server if the question has been saved
	 */
	public static final String TEXT_QUESTION_MODIFIED = "Question Saved";
	/**
	 * Text returned by the server when a test file has been created ok
	 */
	public static final String TEXT_TESTFILE_CREATED = "Test file has been created";
	/**
	 * Text returned by the server that indicates that the file been created
	 */
	public static final String TEXT_FILE_CREATED = "File has been created"; 
	/**
	 * Text returned by the server when another file exists
	 */
	public static final String TEXT_ANOTHER_FILE_EXISTS = "Another file already exists with the same name";
	/**
	 * Text returned by the server when a file has been deleted
	 */
	public static final String TEXT_FILE_DELETED = "The file has been deleted";

	/**
	 * Represents the WOW v2.0 format
	 */
	public static final short WOWv2d0_FORMAT = 0;
	/**
	 * Represents the GIFT format
	 */
	public static final short GIFT_FORMAT = 1;
	/**
	 * Represents the Hot Potatoes v6 format
	 */
	public static final short HOTPOTATOESv6_FORMAT = 2;
	/**
	 * Represents the Moodle XML format
	 */
	public static final short MOODLEXML_FORMAT = 3;
	/**
	 * Represents the QTI v1.2 format
	 */
	public static final short QTIv1d2_FORMAT = 4;
	/**
	 * Represents the QTI v2.0 format
	 */
	public static final short QTIv2d0_FORMAT = 5;
	/**
	 * Represents the Siette format
	 */
	public static final short SIETTE_FORMAT = 6;
	/**
	 * Represents the WebCT format
	 */
	public static final short WEBCT_FORMAT = 7;

	
// Class variables -->
	/**
	 * Number of subframes opened
	 */
	private int layer = 0;

	/**
	 * Name of the root folder of the WOW system
	 */
	private String wowPath;
	/**
	 * Relative path to the folder that contains the icons of the TestEditor tool
	 */
	private String iconTestEditorPath;
	/**
	 * Relative path to the folder that contains the items
	 */
	private String itemsPath;
	/**
	 * Name of the user
	 */
	private String userLogin = "";
	/**
	 * Password of the user
	 */
	private String userPassword = "";
	/**
	 * A Vector object with the names of the courses and their associated concepts
	 */
	private Vector courseConceptVector = new Vector();
	/**
	 * A Vector course with the courses
	 */
	private Vector courseVector = new Vector();
	/**
	 * Stores the configuration data of a test
	 */
	private Test test;
	/**
	 * Name of the courses that belongs to the user
	 */
	private Vector courseNameVector = new Vector();
	/**
	 * Stores the name of the current course that the user works with
	 */
	private String courseName = "";
	/**
	 * Name of the question file that the user is working with
	 */
	private String questionsFileName = "";
	/**
	 * Variables with the state of the internal frames of this JFrame.
	 */ 
	private boolean imageIconLoad = false;
	/**
	 * This variable stores a value that indicates if the current thread
	 * has finished or not.
	 */
	private int taskDone = 0;
	private TimeControl workerTimeControl = null;
	private RepaintProgressBar workerRepaintProgressBar = null;
	private GetQuestionsFileNames workerGetQuestionsFileNames = null;
	private GetTestsFileNames workerGetTestsFileNames = null;
	/**
	 * Net variables
	 */
	private URL codeBase;
	/** 
	 * Variables that store the possible difficulty and discrimination
	 * that will be chosen for a question
	 */
	protected double DIFFICULTY_MAX = 4.00;
	protected double DIFFICULTY_NORMAL = 0.0;
	protected double DIFFICULTY_MIN = -4.00;
	protected double DIFFICULTY_SELECT = 0.0;
	protected double DISCRIMINATION_MAX = 4.0;
	protected double DISCRIMINATION_MIN = 0.0;
	protected double DISCRIMINATION_NORMAL = 1.0;
	protected double DISCRIMINATION_SELECT = 1.0;
	protected double GUESSING_MAX = 0.5;
	protected double GUESSING_MIN = 0.0;
	protected double GUESSING_NORMAL;
	protected double GUESSING_SELECT;
	protected double EXHIBITION_RATE_MAX = 1.0;
	protected double EXHIBITION_RATE_MIN = 0.0;
	protected int ANSWER_TIME_MAX = 600;
	protected int ANSWER_TIME_MIN = 0;
	protected double SUCCESS_RATE_MAX = 1.0;
	protected double SUCCESS_RATE_MIN = 0.0;
	
	/**
	 * Components of the JFrame
	 */
	protected String lookAndFeelClassName;

	/**
	 * A reference to itself
	 */
	private TestEditor myInstance;
	
	private JMenuBar jMenuBar;

	private JToolBar jToolBar;

	private JPanel jPanelStatus;

	private JScrollPane jScrollPaneDesktop;

	private JDesktopPane jDesktopPane;

	private FlowLayout flowLayoutPanelStatus;

	private JLabel jLabelStatus;

	private JProgressBar jProgressBar;

	private ImageIcon iconNewFile32;

	private ImageIcon iconDeleteFile32;

	private ImageIcon iconExamineFile32;

	private ImageIcon iconAddQuestion32;

	private ImageIcon iconEditQuestion32;

	private ImageIcon iconFindQuestion32;

	private ImageIcon iconCreateClassicTest32;

	private ImageIcon iconCreateAdaptiveTest32;

	private ImageIcon iconNewFile16;

	private ImageIcon iconDeleteFile16;

	private ImageIcon iconExamineFile16;

	private ImageIcon iconAddQuestion16;

	private ImageIcon iconEditQuestion16;

	private ImageIcon iconFindQuestion16;

	private ImageIcon iconCreateClassicTest16;

	private ImageIcon iconCreateAdaptiveTest16;

	private DlgCreateQuestionsFile dlgCreateQuestionsFile = null;

	private DlgSelectCourseAndQuestionsFile dlgCourseAndQuestionsFile = null;

	private DlgSelectCourse dlgSelectCourse = null;

	private DlgSelectCourseAndTestFile dlgCourseAndTestFile = null;

	private DlgAbout dlgAbout = null;


	/**
	 * @return Returns the itemsPath.
	 */
	protected final String getItemsPath() {
		return itemsPath;
	}

	/**
	 * @return Returns the jDesktopPane.
	 */
	protected final JDesktopPane getDesktopPane() {
		return jDesktopPane;
	}

	/**
	 * Default constructor. Initialize the components of the window.
	 * @param aUserLogin Login of the user
	 * @param aUserPassword Password of the user
	 * @param aCourseVector Vector with the courses that belongs to the user
	 * @param aCodeBase Codebase of the applet
	 * @param anWowPath Path of the system where the applet has been downloaded
	 * @param aIconTestEditorPath Relative path to the icons folder
	 * @param aItemsPath Relative path to the folder where the items will be located
	 */
	public TestEditor(
			final String aUserLogin,
			final String aUserPassword,
			final Vector aCourseVector,
			final URL aCodeBase,
			final String anWowPath,
			final String aIconTestEditorPath,
			final String aItemsPath) {
	//{
		// Gets a reference of itself
		this.myInstance = this;
		
		// Sets visible until it wont be configured
		setVisible(false);

		// Sets the look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
		} catch (Exception e) {}

		// Sets user variables
		userLogin = aUserLogin;
		userPassword = aUserPassword;
		courseConceptVector = aCourseVector;
		courseVector = aCourseVector;
		courseNameVector = new Vector();

		for (int i = 0; i < courseVector.size(); i++) {
			courseNameVector.add(((Course) courseVector.get(i)).getName().trim());
		}

		this.codeBase = aCodeBase;
		this.wowPath = anWowPath;
		this.iconTestEditorPath = aIconTestEditorPath;
		this.itemsPath = aItemsPath;

		try {
			// Initializes the components of the JFrame
			JImageIconInit();
			JMenuBarInit();
			JToolBarInit();
			JDesktopPaneInit();
			JScrollPaneInit();

			JLabelInit();
			JProgressBarInit();
			JPanelInit();

			// Initializes the frame at the end
			JFrameInit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	// //////////////////////////////////////////////////////////////////////////////
	// ////////////// INICIALIZACI�N DE LOS COMPONENTES DEL JFRAME
	// //////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: JImageIconInit. PERTENECE A: Clase TestEditor. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los ImageIcon del JFrame, reservando
	 * memoria para los mismos y estableciendo su tama�o, su contenido,
	 * funcionalidad... Los iconos se encuentran en el servidor, con lo que ser�
	 * solicitado el envio de los mismos desde el servidor al applet descargado.
	 */

	private void JImageIconInit() {
		// Conexi�n con el servidor.
		// Carga de los iconos para los JButton anteriores. Estos iconos se
		// encuentran en el servidor, con lo que ser�n cargados desde el mismo.
		try {
			// Construcci�n de los String para la petici�n de los iconos al
			// servidor.
			String pathToIcons = wowPath + iconTestEditorPath;
			String petitionNewFile32 = pathToIcons + "newFile32.gif";
			String petitionDeleteFile32 = pathToIcons + "deleteFile32.gif";
			String petitionExamineQuestionFile32 = pathToIcons + "examine32.gif";
			String petitionAddQuestion32 = pathToIcons + "addQuestion32.gif";
			String petitionEditQuestion32 = pathToIcons + "modifyDeleteQuestion32.gif";
			String petitionFindQuestion32 = pathToIcons + "find32.gif";
			String petitionCreateClassicTest32 = pathToIcons + "createClassicTest32.gif";
			String petitionCreateAdaptiveTest32 = pathToIcons + "createAdaptiveTest32.gif";
			String petitionNewFile16 = pathToIcons + "newFile16.gif";
			String petitionDeleteFile16 = pathToIcons + "deleteFile16.gif";
			String petitionExamineQuestionFile16 = pathToIcons + "examine16.gif";
			String petitionAddQuestion16 = pathToIcons + "addQuestion16.gif";
			String petitionEditQuestion16 = pathToIcons + "modifyDeleteQuestion16.gif";
			String petitionFindQuestion16 = pathToIcons + "find16.gif";
			String petitionCreateClassicTest16 = pathToIcons + "createClassicTest16.gif";
			String petitionCreateAdaptiveTest16 = pathToIcons + "createAdaptiveTest16.gif";

			// Creaci�n de las conexiones con el servidor.
			URL urlNewFile32 = new URL(codeBase, petitionNewFile32);
			URL urlDeleteFile32 = new URL(codeBase, petitionDeleteFile32);
			URL urlExamineQuestionFile32 = new URL(codeBase, petitionExamineQuestionFile32);
			URL urlAddQuestion32 = new URL(codeBase, petitionAddQuestion32);
			URL urlEditQuestion32 = new URL(codeBase, petitionEditQuestion32);
			URL urlFindQuestion32 = new URL(codeBase, petitionFindQuestion32);
			URL urlCreateClassicTest32 = new URL(codeBase, petitionCreateClassicTest32);
			URL urlCreateAdaptiveTest32 = new URL(codeBase, petitionCreateAdaptiveTest32);

			URL urlNewFile16 = new URL(codeBase, petitionNewFile16);
			URL urlDeleteFile16 = new URL(codeBase, petitionDeleteFile16);
			URL urlExamineQuestionFile16 = new URL(codeBase, petitionExamineQuestionFile16);
			URL urlAddQuestion16 = new URL(codeBase, petitionAddQuestion16);
			URL urlEditQuestion16 = new URL(codeBase, petitionEditQuestion16);
			URL urlFindQuestion16 = new URL(codeBase, petitionFindQuestion16);
			URL urlCreateClassicTest16 = new URL(codeBase, petitionCreateClassicTest16);
			URL urlCreateAdaptiveTest16 = new URL(codeBase, petitionCreateAdaptiveTest16);

			// Reserva de memoria para los ImageIcon (los iconos de los JButton del
			// jToolBar).
			iconNewFile32 = new ImageIcon(urlNewFile32);
			iconDeleteFile32 = new ImageIcon(urlDeleteFile32);
			iconExamineFile32 = new ImageIcon(urlExamineQuestionFile32);
			iconAddQuestion32 = new ImageIcon(urlAddQuestion32);
			iconEditQuestion32 = new ImageIcon(urlEditQuestion32);
			iconFindQuestion32 = new ImageIcon(urlFindQuestion32);
			iconCreateClassicTest32 = new ImageIcon(urlCreateClassicTest32);
			iconCreateAdaptiveTest32 = new ImageIcon(urlCreateAdaptiveTest32);

			iconNewFile16 = new ImageIcon(urlNewFile16);
			iconDeleteFile16 = new ImageIcon(urlDeleteFile16);
			iconExamineFile16 = new ImageIcon(urlExamineQuestionFile16);
			iconAddQuestion16 = new ImageIcon(urlAddQuestion16);
			iconEditQuestion16 = new ImageIcon(urlEditQuestion16);
			iconFindQuestion16 = new ImageIcon(urlFindQuestion16);
			iconCreateClassicTest16 = new ImageIcon(urlCreateClassicTest16);
			iconCreateAdaptiveTest16 = new ImageIcon(urlCreateAdaptiveTest16);

			imageIconLoad = true;
		} catch (java.net.MalformedURLException e) {
			imageIconLoad = false;
		}
	}

	/*
	 * NOMBRE: JMenuBarInit. PERTENECE A: Clase TestEditor. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa el JMenuBar del JFrame, reservando
	 * memoria para el mismos y estableciendo sus men�s y submen�s, asi como su
	 * color y otros par�metros.
	 */

	private void JMenuBarInit() throws Exception {
		// Reserva de memoria para jMenuBar y establecimiento de su color.
		jMenuBar = new JMenuBar();
		jMenuBar.setBackground(SystemColor.menu);

		// Inicializacion Componentes del jMenuBar.

		// Reserva de memoria para jMenuQuestions, establecimiento de su color y
		// de
		// sutexto.
		JMenu jMenuQuestions = new JMenu();
		jMenuQuestions.setBackground(SystemColor.menu);
		jMenuQuestions.setText("Questions");

		// Reserva de memoria para jMenuTestsMaintenance, establecimiento de su
		// color
		// y de su texto.
		JMenu jMenuTest = new JMenu();
		jMenuTest.setBackground(SystemColor.menu);
		jMenuTest.setText("Test");

		// Reserva de memoria para jMenuLayou, establecimiento de su color y de su
		// texto.
		JMenu jMenuLayout = new JMenu("Layout");
		jMenuLayout.setBackground(SystemColor.menu);
		jMenuLayout.setMnemonic(KeyEvent.VK_L);

		// Reserva de memoria para jMenuHelp, establecimiento de su color y de su
		// texto.
		JMenu jMenuHelp = new JMenu();
		jMenuHelp.setBackground(SystemColor.menu);
		jMenuHelp.setText("Help");

		// Inicializacion Componentes de jMenuQuestions.

		// Reserva de memoria para jMenuItemNewQuestionFile, establecimiento de su
		// color y de su texto.
		JMenuItem jMenuItemCreateQuestionFile = new JMenuItem();
		jMenuItemCreateQuestionFile.setBackground(SystemColor.menu);
		jMenuItemCreateQuestionFile.setText("Create Question File");

		// Reserva de memoria para jMenuItemDeleteQuestionFile, establecimiento de
		// su
		// color y de su texto.
		JMenuItem jMenuItemDeleteQuestionFile = new JMenuItem();
		jMenuItemDeleteQuestionFile.setBackground(SystemColor.menu);
		jMenuItemDeleteQuestionFile.setText("Delete Question File");

		// Reserva de memoria para jMenuItemExamineQuestionFile, establecimiento
		// de
		// su color y de su texto.
		JMenuItem jMenuItemExamineQuestionFile = new JMenuItem();
		jMenuItemExamineQuestionFile.setBackground(SystemColor.menu);
		jMenuItemExamineQuestionFile.setText("Examine File of Questions");

		// Reserva de memoria para jMenuItemAddQuestionToFile, establecimiento de
		// su
		// color y de su texto.
		JMenuItem jMenuItemAddQuestionToFile = new JMenuItem();
		jMenuItemAddQuestionToFile.setBackground(SystemColor.menu);
		jMenuItemAddQuestionToFile.setText("Add Questions To File");

		// Reserva de memoria para jMenuItemEditQuestion, establecimiento de su
		// color y de su texto.
		JMenuItem jMenuItemEditQuestion = new JMenuItem();
		jMenuItemEditQuestion.setBackground(SystemColor.menu);
		jMenuItemEditQuestion.setText("Edit Questions of a File");

		// Reserva de memoria para jMenuItemFindQuestions, establecimiento de su
		// color y de su texto.
		JMenuItem jMenuItemFindQuestions = new JMenuItem();
		jMenuItemFindQuestions.setBackground(SystemColor.menu);
		jMenuItemFindQuestions.setText("Find Questions");

		JMenu jMenuExportQuestionFile = new JMenu();
		jMenuExportQuestionFile.setBackground(SystemColor.menu);
		jMenuExportQuestionFile.setText("Export File of Questions");
		jMenuExportQuestionFile.setToolTipText("Export WOW! question file to other file format");
		JMenuItem jMenuExportWOW2d0 = new JMenuItem();
		jMenuExportWOW2d0.setBackground(SystemColor.menu);
		jMenuExportWOW2d0.setText("Export to WOW! v2.0 Format");
		JMenuItem jMenuExportGift = new JMenuItem();
		jMenuExportGift.setBackground(SystemColor.menu);
		jMenuExportGift.setText("Export to Gift Format");
		JMenuItem jMenuExportHotPotatoesd6 = new JMenuItem();
		jMenuExportHotPotatoesd6.setBackground(SystemColor.menu);
		jMenuExportHotPotatoesd6.setText("Export to Hot Potatoes v6 Format");
		JMenuItem jMenuExportMoodleXML = new JMenuItem();
		jMenuExportMoodleXML.setBackground(SystemColor.menu);
		jMenuExportMoodleXML.setText("Export to Moodle XML Format");
		JMenuItem jMenuExportQti1d2 = new JMenuItem();
		jMenuExportQti1d2.setBackground(SystemColor.menu);
		jMenuExportQti1d2.setText("Export to Qti v1.2 Format");
		JMenuItem jMenuExportQti2d0 = new JMenuItem();
		jMenuExportQti2d0.setBackground(SystemColor.menu);
		jMenuExportQti2d0.setText("Export to Qti v2.0 Format");
		JMenuItem jMenuExportSiette = new JMenuItem();
		jMenuExportSiette.setBackground(SystemColor.menu);
		jMenuExportSiette.setText("Export to Siette Format");
		JMenuItem jMenuExportWebCT = new JMenuItem();
		jMenuExportWebCT.setBackground(SystemColor.menu);
		jMenuExportWebCT.setText("Export to WebCT Format");
		jMenuExportQuestionFile.add(jMenuExportWOW2d0);
		jMenuExportQuestionFile.add(jMenuExportGift);
		jMenuExportQuestionFile.add(jMenuExportHotPotatoesd6);
		jMenuExportQuestionFile.add(jMenuExportMoodleXML);
		jMenuExportQuestionFile.add(jMenuExportQti1d2);
		jMenuExportQuestionFile.add(jMenuExportQti2d0);
		jMenuExportQuestionFile.add(jMenuExportSiette);
		jMenuExportQuestionFile.add(jMenuExportWebCT);

		JMenu jMenuImportQuestionFile = new JMenu();
		jMenuImportQuestionFile.setBackground(SystemColor.menu);
		jMenuImportQuestionFile.setToolTipText("Import question file to the WOW! current version");
		jMenuImportQuestionFile.setText("Import File of Questions");
		JMenuItem jMenuImportWOW2d0 = new JMenuItem();
		jMenuImportWOW2d0.setBackground(SystemColor.menu);
		jMenuImportWOW2d0.setText("From WOW! v2.0 Format");
		JMenuItem jMenuImportGift = new JMenuItem();
		jMenuImportGift.setBackground(SystemColor.menu);
		jMenuImportGift.setText("From Gift Format");
		JMenuItem jMenuImportHotPotatoesd6 = new JMenuItem();
		jMenuImportHotPotatoesd6.setBackground(SystemColor.menu);
		jMenuImportHotPotatoesd6.setText("From Hot Potatoes v6 Format");
		JMenuItem jMenuImportMoodleXML = new JMenuItem();
		jMenuImportMoodleXML.setBackground(SystemColor.menu);
		jMenuImportMoodleXML.setText("From Moodle XML Format");
		JMenuItem jMenuImportQti1d2 = new JMenuItem();
		jMenuImportQti1d2.setBackground(SystemColor.menu);
		jMenuImportQti1d2.setText("From Qti v1.2 Format");
		JMenuItem jMenuImportQti2d0 = new JMenuItem();
		jMenuImportQti2d0.setBackground(SystemColor.menu);
		jMenuImportQti2d0.setText("From Qti v2.0 Format");
		JMenuItem jMenuImportSiette = new JMenuItem();
		jMenuImportSiette.setBackground(SystemColor.menu);
		jMenuImportSiette.setText("From Siette Format");
		JMenuItem jMenuImportWebCT = new JMenuItem();
		jMenuImportWebCT.setBackground(SystemColor.menu);
		jMenuImportWebCT.setText("From WebCT Format");
		jMenuImportQuestionFile.add(jMenuImportWOW2d0);
		jMenuImportQuestionFile.add(jMenuImportGift);
		jMenuImportQuestionFile.add(jMenuImportHotPotatoesd6);
		jMenuImportQuestionFile.add(jMenuImportMoodleXML);
		jMenuImportQuestionFile.add(jMenuImportQti1d2);
		jMenuImportQuestionFile.add(jMenuImportQti2d0);
		jMenuImportQuestionFile.add(jMenuImportSiette);
		jMenuImportQuestionFile.add(jMenuImportWebCT);

		JMenuItem jMenuItemCalibrateQuestionsFile = new JMenuItem();
		jMenuItemCalibrateQuestionsFile.setBackground(SystemColor.menu);
		jMenuItemCalibrateQuestionsFile.setText("Calibrate File of Questions");

		JMenuItem jMenuItemUpdateStatisticData = new JMenuItem();
		jMenuItemUpdateStatisticData.setBackground(SystemColor.menu);
		jMenuItemUpdateStatisticData.setText("Update Statistical Data");

		JMenuItem jMenuItemExit = new JMenuItem();
		jMenuItemExit.setBackground(SystemColor.menu);
		jMenuItemExit.setText("Exit");

		// Inicializacio Componentes de jMenuTest.

		// Reserva de memoria para jMenuItemCreateClassicTest, establecimiento de
		// su
		// color y de su texto.
		JMenuItem jMenuItemCreateClassicTest = new JMenuItem();
		jMenuItemCreateClassicTest.setBackground(SystemColor.menu);
		jMenuItemCreateClassicTest.setText("Create Classic Test");

		// Reserva de memoria para jMenuItemCreateAdaptiveTest, establecimiento de
		// su color y de su texto.
		JMenuItem jMenuItemCreateAdaptiveTest = new JMenuItem();
		jMenuItemCreateAdaptiveTest.setBackground(SystemColor.menu);
		jMenuItemCreateAdaptiveTest.setText("Create Adaptive Test");

		// Reserva de memoria para jMenuItemDeleteClassicTest, establecimiento de
		// su
		// color y de su texto.
		JMenuItem jMenuItemDeleteClassicTest = new JMenuItem();
		jMenuItemDeleteClassicTest.setBackground(SystemColor.menu);
		jMenuItemDeleteClassicTest.setText("Delete Test");

		// Item to mobile JAR for classic test
		JMenuItem jMenuItemCreateMobileClassicTest = new JMenuItem();
		jMenuItemCreateMobileClassicTest.setBackground(SystemColor.menu);
		jMenuItemCreateMobileClassicTest.setText("Generate Mobile JAR (CLDC 1.1)");
		
		//	Item to create mobile JAR for adaptive test
		JMenuItem jMenuItemCreateMobileAdaptiveTest = new JMenuItem();
		jMenuItemCreateMobileAdaptiveTest.setBackground(SystemColor.menu);
		jMenuItemCreateMobileAdaptiveTest.setText("Generate Mobile JAR (CLDC 1.1)");

		// Item 
		JMenuItem jMenuItemEditClassicTest = new JMenuItem();
		jMenuItemEditClassicTest.setBackground(SystemColor.menu);
		jMenuItemEditClassicTest.setText("Edit Test");

		// Reserva de memoria para jMenuItemTestStatistic, establecimiento de su
		// color y de su texto.
		JMenuItem jMenuItemClassicTestStatistic = new JMenuItem();
		jMenuItemClassicTestStatistic.setBackground(SystemColor.menu);
		jMenuItemClassicTestStatistic.setText("Statistic of the test");

		// Reserva de memoria para jMenuItemDeleteAdaptiveTest, establecimiento de
		// su
		// color y de su texto.
		JMenuItem jMenuItemDeleteAdaptiveTest = new JMenuItem();
		jMenuItemDeleteAdaptiveTest.setBackground(SystemColor.menu);
		jMenuItemDeleteAdaptiveTest.setText("Delete Test");

		// Reserva de memoria para jMenuItemModifyAdaptiveTest, establecimiento de
		// su
		// color y de su texto.
		JMenuItem jMenuItemEditAdaptiveTest = new JMenuItem();
		jMenuItemEditAdaptiveTest.setBackground(SystemColor.menu);
		jMenuItemEditAdaptiveTest.setText("Edit Test");

		// Reserva de memoria para jMenuItemAdaptiveTestStatistic, establecimiento
		// de su color y de su texto.
		JMenuItem jMenuItemAdaptiveTestStatistic = new JMenuItem();
		jMenuItemAdaptiveTestStatistic.setBackground(SystemColor.menu);
		jMenuItemAdaptiveTestStatistic.setText("Statistic of the test");

		// Reserva de memoria para jMenuClassicTest, establecimiento de
		// su color y de su texto.
		JMenu jMenuClassicTest = new JMenu();
		jMenuClassicTest.setBackground(SystemColor.menu);
		jMenuClassicTest.setText("Classic Test");

		// Adicion a jMenuClassicTest de sus submenus.
		jMenuClassicTest.add(jMenuItemCreateClassicTest);
		jMenuClassicTest.addSeparator();
		jMenuClassicTest.add(jMenuItemEditClassicTest);
		jMenuClassicTest.add(jMenuItemDeleteClassicTest);
		jMenuClassicTest.addSeparator();
		jMenuClassicTest.add(jMenuItemCreateMobileClassicTest);
		jMenuClassicTest.addSeparator();
		jMenuClassicTest.add(jMenuItemClassicTestStatistic);

		// Reserva de memoria para jMenuItemAdaptiveTest, establecimiento
		// de su color y de su texto.
		JMenu jMenuAdaptiveTest = new JMenu();
		jMenuAdaptiveTest.setBackground(SystemColor.menu);
		jMenuAdaptiveTest.setText("Adaptive Test");

		// Adicion a jMenuAdaptiveTest de sus submenus.
		jMenuAdaptiveTest.add(jMenuItemCreateAdaptiveTest);
		jMenuAdaptiveTest.addSeparator();
		jMenuAdaptiveTest.add(jMenuItemEditAdaptiveTest);
		jMenuAdaptiveTest.add(jMenuItemDeleteAdaptiveTest);
		jMenuAdaptiveTest.addSeparator();
		jMenuAdaptiveTest.add(jMenuItemCreateMobileAdaptiveTest);
		jMenuAdaptiveTest.addSeparator();
		jMenuAdaptiveTest.add(jMenuItemAdaptiveTestStatistic);

		// Reserva de memoria para los jMenuItems contenidos en jMenuLayout.
		final UIManager.LookAndFeelInfo installedLnF[] = UIManager.getInstalledLookAndFeels();

		final JMenuItem[] jMenuItemLookAndFeels = new JMenuItem[installedLnF.length];

		for (int j = 0; j < installedLnF.length; j++) {
			final int i = j;

			jMenuItemLookAndFeels[i] = new JMenuItem(installedLnF[i].getName());
			jMenuLayout.add(jMenuItemLookAndFeels[i]);

			// Establecimiento de la funcionalidad de jMenuItemNewQuestionFile.
			jMenuItemLookAndFeels[i].addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						UIManager.setLookAndFeel(installedLnF[i].getClassName());
						SwingUtilities.updateComponentTreeUI(myInstance);

						lookAndFeelClassName = installedLnF[i].getClassName();
					} catch (ClassNotFoundException exc) {
						JOptionPane.showMessageDialog(null, "Can't find System look and feel: " + exc, "ERROR",
								JOptionPane.ERROR_MESSAGE);
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(null, "Problem with look and feel: " + exc, "ERROR",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}

		// Establecimiento de los iconos de los JMenuItem.
		if (imageIconLoad == true) {
			jMenuItemCreateQuestionFile.setIcon(iconNewFile16);
			jMenuItemDeleteQuestionFile.setIcon(iconDeleteFile16);
			jMenuItemExamineQuestionFile.setIcon(iconExamineFile16);
			jMenuItemAddQuestionToFile.setIcon(iconAddQuestion16);
			jMenuItemEditQuestion.setIcon(iconEditQuestion16);
			jMenuItemFindQuestions.setIcon(iconFindQuestion16);
			jMenuItemCreateClassicTest.setIcon(iconCreateClassicTest16);
			jMenuItemCreateAdaptiveTest.setIcon(iconCreateAdaptiveTest16);
		}

		// Inicializacio Componentes de jMenuHelp

		// Reserva de memoria para jMenuItemAboutTestEditor, establecimiento de su
		// color y de su texto.
		JMenuItem jMenuItemAboutTestEditor = new JMenuItem();
		jMenuItemAboutTestEditor.setBackground(SystemColor.menu);
		jMenuItemAboutTestEditor.setText("About TestEditor");

		// Adici�n de los JMenuItem correspondientes a jMenuQuestions.
		jMenuQuestions.add(jMenuItemCreateQuestionFile);
		jMenuQuestions.add(jMenuItemDeleteQuestionFile);
		jMenuQuestions.addSeparator();
		jMenuQuestions.add(jMenuItemAddQuestionToFile);
		jMenuQuestions.add(jMenuItemEditQuestion);
		jMenuQuestions.addSeparator();
		jMenuQuestions.add(jMenuItemFindQuestions);
		jMenuQuestions.add(jMenuItemExamineQuestionFile);
		jMenuQuestions.addSeparator();
		jMenuQuestions.add(jMenuExportQuestionFile);
		jMenuQuestions.add(jMenuImportQuestionFile);
		jMenuQuestions.addSeparator();
		jMenuQuestions.add(jMenuItemCalibrateQuestionsFile);
		jMenuQuestions.add(jMenuItemUpdateStatisticData);
		jMenuQuestions.addSeparator();
		jMenuQuestions.add(jMenuItemExit);

		// Adici�n de los JMenuItem correspondientes a jMenuTest.
		jMenuTest.add(jMenuClassicTest);
		jMenuTest.add(jMenuAdaptiveTest);

		// Adici�n de los JMenuItem correspondientes a jMenuHelp.
		jMenuHelp.add(jMenuItemAboutTestEditor);
		// jMenuHelp.addSeparator();

		// Adici�n de los JMenu al JMenuBar.
		jMenuBar.add(jMenuQuestions);
		jMenuBar.add(jMenuQuestions);
		jMenuBar.add(jMenuTest);
		jMenuBar.add(jMenuLayout);
		jMenuBar.add(jMenuHelp);

		// Establecimiento de las abreviaturas de teclado para los JMenu y
		// JMenuItem anteriores.

		// Establecimiento de las abreviaturas de teclado para jMenuQuestions.
		jMenuQuestions.setMnemonic(KeyEvent.VK_Q);

		// Establecimiento de las abreviaturas de teclado para jMenuTest.
		jMenuTest.setMnemonic(KeyEvent.VK_T);

		// Establecimiento de las abreviaturas de teclado para jMenuHelp.
		jMenuHelp.setMnemonic(KeyEvent.VK_H);

		// Establecimiento de las abreviaturas de teclado para los JMenuItem de
		// pertenecientes a jMenuQuestions.
		jMenuItemCreateQuestionFile.setMnemonic(KeyEvent.VK_C);
		jMenuItemDeleteQuestionFile.setMnemonic(KeyEvent.VK_D);
		jMenuItemAddQuestionToFile.setMnemonic(KeyEvent.VK_A);
		jMenuItemEditQuestion.setMnemonic(KeyEvent.VK_M);
		jMenuItemFindQuestions.setMnemonic(KeyEvent.VK_F);
		jMenuItemExamineQuestionFile.setMnemonic(KeyEvent.VK_S);
		jMenuItemCalibrateQuestionsFile.setMnemonic(KeyEvent.VK_L);
		jMenuItemUpdateStatisticData.setMnemonic(KeyEvent.VK_U);
		jMenuItemExit.setMnemonic(KeyEvent.VK_E);

		// Establecimiento de las abreviaturas de teclado para los JMenuItem de
		// pertenecientes a jMenuTest.
		jMenuItemCreateClassicTest.setMnemonic(KeyEvent.VK_C);
		jMenuItemCreateAdaptiveTest.setMnemonic(KeyEvent.VK_A);

		// Establecimiento de las abreviaturas de teclado para los JMenuItem de
		// pertenecientes a jMenuTestsMaintenance.
		jMenuItemEditClassicTest.setMnemonic(KeyEvent.VK_E);
		jMenuItemDeleteClassicTest.setMnemonic(KeyEvent.VK_D);
		jMenuItemClassicTestStatistic.setMnemonic(KeyEvent.VK_S);

		jMenuItemEditAdaptiveTest.setMnemonic(KeyEvent.VK_E);
		jMenuItemDeleteAdaptiveTest.setMnemonic(KeyEvent.VK_D);
		jMenuItemAdaptiveTestStatistic.setMnemonic(KeyEvent.VK_S);

		// Establecimiento de las abreviaturas de teclado para los JMenuItem de
		// pertenecientes a jMenuHelp.
		jMenuItemAboutTestEditor.setMnemonic(KeyEvent.VK_A);

		// Establecimiento de las distintas funcionalidades de los JMenuItem
		// anteriores.

		// Establecimiento de la funcionalidad de jMenuItemNewQuestionFile.
		jMenuItemCreateQuestionFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemCreateQuestionsFileActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemDeleteQuestionFile.
		jMenuItemDeleteQuestionFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemDeleteQuestionsFileActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemAddQuestionToFile.
		jMenuItemAddQuestionToFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemAddQuestionToFileActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemModifyQuestions.
		jMenuItemEditQuestion.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemEditQuestionActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemFindQuestions.
		jMenuItemFindQuestions.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemFindQuestionsActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemExamineQuestionFile.
		jMenuItemExamineQuestionFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemExamineQuestionsFileActionPerformed();
			}
		});
		
		jMenuExportWOW2d0.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuExportWOWActionPerformed();
			}
		});

		jMenuExportGift.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuExportActionPerformed(GIFT_FORMAT);
			}
		});
		
		jMenuExportHotPotatoesd6.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuExportActionPerformed(HOTPOTATOESv6_FORMAT);
			}
		});
		
		jMenuExportMoodleXML.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuExportActionPerformed(MOODLEXML_FORMAT);
			}
		});
		
		jMenuExportQti1d2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuExportActionPerformed(QTIv1d2_FORMAT);
			}
		});
		
		jMenuExportQti2d0.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuExportActionPerformed(QTIv2d0_FORMAT);
			}
		});
		
		jMenuExportSiette.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuExportActionPerformed(SIETTE_FORMAT);
			}
		});
		
		jMenuExportWebCT.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuExportActionPerformed(WEBCT_FORMAT);
			}
		});

		jMenuImportWOW2d0.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuImportWOWActionPerformed();
			}
		});

		jMenuImportGift.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuImportActionPerformed(GIFT_FORMAT);
			}
		});
		
		jMenuImportHotPotatoesd6.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuImportActionPerformed(HOTPOTATOESv6_FORMAT);
			}
		});
		
		jMenuImportMoodleXML.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuImportActionPerformed(MOODLEXML_FORMAT);
			}
		});
		
		jMenuImportQti1d2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuImportActionPerformed(QTIv1d2_FORMAT);
			}
		});
		
		jMenuImportQti2d0.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuImportActionPerformed(QTIv2d0_FORMAT);
			}
		});
		
		jMenuImportSiette.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuImportActionPerformed(SIETTE_FORMAT);
			}
		});
		
		jMenuImportWebCT.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuImportActionPerformed(WEBCT_FORMAT);
			}
		});

		jMenuItemCalibrateQuestionsFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemCalibrateQuestionsFileActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemUpdateStatisticData.
		jMenuItemUpdateStatisticData.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemUpdateStatisticDataActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemExit.
		jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closingWindow();
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemCreateClassicTest.
		jMenuItemCreateClassicTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemCreateTestActionPerformed(CLASSIC);
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemEditClassicTest.
		jMenuItemEditClassicTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemEditTestActionPerformed(CLASSIC);
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemDeleteClassicTest.
		jMenuItemDeleteClassicTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemDeleteTestActionPerformed(CLASSIC);
			}
		});

		// Sets the function of the jMenuItemCreateMobileClassicTest.
		jMenuItemCreateMobileClassicTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemCreateMobileTestActionPerformed(CLASSIC);
			}
		});

		//	Sets the function of the jMenuItemCreateMobileAdaptiveTest.
		jMenuItemCreateMobileAdaptiveTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemCreateMobileTestActionPerformed(ADAPTIVE);
			}
		});
		
		// Establecimiento de la funcionalidad de jMenuItemClassicTestStatistic.
		jMenuItemClassicTestStatistic.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemTestStatisticActionPerformed(CLASSIC);
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemCreateAdaptiveTest.
		jMenuItemCreateAdaptiveTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemCreateTestActionPerformed(ADAPTIVE);
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemEditAdaptiveTest.
		jMenuItemEditAdaptiveTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemEditTestActionPerformed(ADAPTIVE);
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemDeleteAdaptiveTest.
		jMenuItemDeleteAdaptiveTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemDeleteTestActionPerformed(ADAPTIVE);
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemAdaptiveTestStatistic.
		jMenuItemAdaptiveTestStatistic.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemTestStatisticActionPerformed(ADAPTIVE);
			}
		});

		// Establecimiento de la funcionalidad de jMenuItemAboutTestEditor.
		jMenuItemAboutTestEditor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlgAbout = new DlgAbout(myInstance, codeBase, wowPath, iconTestEditorPath);
				dlgAbout.show();
			}
		});
	}

	/*
	 * NOMBRE: JToolBarInit. PERTENECE A: Clase TestEditor. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa el JToolBar del JFrame, reservando
	 * memoria para el mismos y estableciendo sus botones, asi como su color y
	 * otros par�metros.
	 */

	private void JToolBarInit() {
		// Reserva de memoria para jToolBar y establecimiento de algunas
		// carater�sticas.
		jToolBar = new JToolBar();
		jToolBar.setBorderPainted(true);
		jToolBar.setRollover(true);
		jToolBar.setFloatable(false);

		// Reserva de memoria para los JButton pertenecientes a jToolBar.
		JButton jButtonCreateQuestionFile = new JButton();
		JButton jButtonDeleteQuestionFile = new JButton();
		JButton jButtonExamineQuestionFile = new JButton();
		JButton jButtonAddQuestionToFile = new JButton();
		JButton jButtonEditQuestion = new JButton();
		JButton jButtonFindQuestion = new JButton();
		JButton jButtonCreateClassicTest = new JButton();
		JButton jButtonCreateAdaptiveTest = new JButton();

		// Establecimiento de "TipText" de los JButton anteriores.
		jButtonCreateQuestionFile.setToolTipText("Create a new file of questions");
		jButtonDeleteQuestionFile.setToolTipText("Delete a file of questions");
		jButtonExamineQuestionFile.setToolTipText("Examine a file of questions");
		jButtonAddQuestionToFile.setToolTipText("Add more questions to a file");
		jButtonEditQuestion.setToolTipText("Edit questions of a file");
		jButtonFindQuestion.setToolTipText("Find questions in a file");
		jButtonCreateClassicTest.setToolTipText("Create Classic Test");
		jButtonCreateAdaptiveTest.setToolTipText("Create Adaptive Test");

		// Carga de los iconos para los JButton anteriores. Estos iconos se
		// encuentran en el servidor, con lo que ser�n cargados desde el mismo.
		if (imageIconLoad == true) {
			// Establecimiento de los �conos en los JButton.
			jButtonCreateQuestionFile.setIcon(iconNewFile32);
			jButtonDeleteQuestionFile.setIcon(iconDeleteFile32);
			jButtonExamineQuestionFile.setIcon(iconExamineFile32);
			jButtonAddQuestionToFile.setIcon(iconAddQuestion32);
			jButtonEditQuestion.setIcon(iconEditQuestion32);
			jButtonFindQuestion.setIcon(iconFindQuestion32);
			jButtonCreateClassicTest.setIcon(iconCreateClassicTest32);
			jButtonCreateAdaptiveTest.setIcon(iconCreateAdaptiveTest32);
		} else {
			// En caso de producirse alguna excepci�n al intentar cargar los iconos
			// de
			// los JButton, pertenecientes al jToolBar, estos tendr�n texto en
			// lugar
			// de iconos.
			jButtonCreateQuestionFile.setText("New Question File");
			jButtonDeleteQuestionFile.setText("Delete Question File");
			jButtonExamineQuestionFile.setText("Examine Question File");
			jButtonAddQuestionToFile.setText("Add Question To File");
			jButtonEditQuestion.setText("Edit Questions of a File");
			jButtonFindQuestion.setText("Find Questions");
			jButtonCreateClassicTest.setText("Create Classic Test");
			jButtonCreateAdaptiveTest.setText("Create Adaptive Test");
		}

		// Establecimiento de las funcionaldidades para los JButton.

		// Establecimiento de la funcionaldidad de jButtonNewQuestionFile.
		jButtonCreateQuestionFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemCreateQuestionsFileActionPerformed();
			}
		});

		// Establecimiento de la funcionaldidad de jButtonDeleteQuestionFile.
		jButtonDeleteQuestionFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemDeleteQuestionsFileActionPerformed();
			}
		});

		// Establecimiento de la funcionaldidad de jButtonExamineQuestionFile.
		jButtonExamineQuestionFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemExamineQuestionsFileActionPerformed();
			}
		});

		// Establecimiento de la funcionaldidad de jButtonAddQuestionToFile.
		jButtonAddQuestionToFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemAddQuestionToFileActionPerformed();
			}
		});

		// Establecimiento de la funcionaldidad de jButtonEditQuestion.
		jButtonEditQuestion.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemEditQuestionActionPerformed();
			}
		});

		// Establecimiento de la funcionaldidad de jButtonFindQuestion.
		jButtonFindQuestion.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemFindQuestionsActionPerformed();
			}
		});

		// Establecimiento de la funcionaldidad de jButtonCreateClassicTest.
		jButtonCreateClassicTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemCreateTestActionPerformed(CLASSIC);
			}
		});

		// Establecimiento de la funcionaldidad de jButtonCreateAdaptiveTest.
		jButtonCreateAdaptiveTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuItemCreateTestActionPerformed(ADAPTIVE);
			}
		});

		// Adici�n de los JButton al jToolBar.
		jToolBar.add(jButtonCreateQuestionFile);
		jToolBar.add(jButtonDeleteQuestionFile);
		jToolBar.add(jButtonExamineQuestionFile);
		jToolBar.addSeparator();
		jToolBar.add(jButtonAddQuestionToFile);
		jToolBar.add(jButtonEditQuestion);
		jToolBar.add(jButtonFindQuestion);
		jToolBar.addSeparator();
		jToolBar.add(jButtonCreateClassicTest);
		jToolBar.add(jButtonCreateAdaptiveTest);
		jToolBar.setBorder(jMenuBar.getBorder());
	}

	/*
	 * NOMBRE: JDesktopPaneInit. PERTENECE A: Clase TestEditor. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa el JDesktopPane del JFrame, reservando
	 * memoria para el mismos y estableciendo alguno de sus par�metros. El
	 * jDesktopPane es el encargado de contener a los frames internos que se
	 * muestran dentro del JFrame, durante la utilizaci�n de algunas de las
	 * opciones de la herramienta TestEditor.
	 */

	private void JDesktopPaneInit() {
		// Reserva de memoria para jDesktopPane y establecimiento de su layout,
		// color, borde,...
		jDesktopPane = new JDesktopPane();
		jDesktopPane.putClientProperty("JDesktopPane.dragMode", "outline");

		jDesktopPane.setBorder(BorderFactory.createEmptyBorder());
		jDesktopPane.setBackground(SystemColor.LIGHT_GRAY);
	}

	/*
	 * NOMBRE: JScrollPaneInit. PERTENECE A: Clase TestEditor. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa el JScrollPane. del JFrame, reservando
	 * memoria para el mismos y estableciendo alguno de sus par�metros. El
	 * JScrollPane es el componente que permite navegar por el JFrame, es decir,
	 * muestra las barras de scroll para poder visusalizar los frames internos en
	 * su totalidad, independientemente de que por su tama�o no quepan en la
	 * pantalla.
	 */

	private void JScrollPaneInit() {
		jScrollPaneDesktop = new JScrollPane(jDesktopPane,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		jScrollPaneDesktop.setBackground(Color.LIGHT_GRAY);
		jScrollPaneDesktop.setBorder(BorderFactory.createEmptyBorder());
	}

	/*
	 * NOMBRE: JLabelInit. PERTENECE A: Clase TestEditor. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JLabel del JFrame, reservando
	 * memoria para el mismos y estableciendo sus men�s y submen�s, asi como su
	 * color y otros par�metros.
	 */

	private void JLabelInit() {
		// Reserva de memoria y establecimiento de algunos de los par�metros de
		// los
		// JLabel.
		jLabelStatus = new JLabel("Done                                        ");
		jLabelStatus.setFont(new java.awt.Font("Dialog", 0, 12));
	}

	/*
	 * NOMBRE: JProgressBarInit. PERTENECE A: Clase TestEditor. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa el JProgressBar del JFrame.
	 */

	private void JProgressBarInit() {
		// Reserva de memoria y establecimiento de algunas propiedades de los
		// JProgressBar.
		jProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
		jProgressBar.setBorderPainted(true);
		jProgressBar.setStringPainted(true);
		jProgressBar.setAlignmentX(RIGHT_ALIGNMENT);
	}

	/*
	 * NOMBRE: JPanelInit. PERTENECE A: Clase TestEditor. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa el JPanel del JFrame.
	 */

	private void JPanelInit() {
		// Reserva de memoriay es establecimiento del layout para el jPanelStatus.
		jPanelStatus = new JPanel();
		flowLayoutPanelStatus = new FlowLayout(FlowLayout.LEFT);
		jPanelStatus.setBorder(BorderFactory.createLoweredBevelBorder());
		jPanelStatus.setLayout(flowLayoutPanelStatus);

		jLabelStatus.setAlignmentX(LEFT_ALIGNMENT);
		jProgressBar.setAlignmentX(RIGHT_ALIGNMENT);

		jPanelStatus.add(jLabelStatus);
		jPanelStatus.add(jProgressBar);
	}

	/*
	 * NOMBRE: JFrameInit. PERTENECE A: Clase TestEditor. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Se encarga de establecer los par�metros del
	 * JFrame, tales como, su t�tulo, tama�o, localizaci�n, le asigna un panel
	 * principal, le a�ade la jMenuBar, y el resto de paneles que componen al
	 * JFrame y vuelve ha hacerlo visible una vez que ya finalizado su
	 * configuraci�n.
	 */

	private void JFrameInit() throws Exception {
		setTitle("Test Editor");
		setLocale(java.util.Locale.getDefault());
		getContentPane().add(jToolBar, BorderLayout.NORTH);
		getContentPane().add(jDesktopPane, BorderLayout.CENTER);
		getContentPane().add(jPanelStatus, BorderLayout.SOUTH);

		setJMenuBar(jMenuBar);
		setSize(getMaximumSize());
		setVisible(true);

		// Establecimiento de la funcionalidad para el JFrame.
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closingWindow();
			}
		});
		
		addComponentListener(new java.awt.event.ComponentListener() {
			public void componentHidden(final ComponentEvent e) {}
			public void componentMoved(ComponentEvent e)  {}
         public void componentShown(ComponentEvent e) {}
         public void componentResized(ComponentEvent e) {
         	JInternalFrame[] frames = jDesktopPane.getAllFrames();
         	for (int i = 0; i < frames.length; i++) {
         		JInternalFrame theFrame = frames[i];
         		try {
         			TestEditorInternalFrame internal = (TestEditorInternalFrame) theFrame;
         			internal.windowSizeControl();
         		} catch (ClassCastException exc) {
         			exc.printStackTrace();
         		}
         	}
         }
		});
	}

	// //////////////////////////////////////////////////////////////////////////////
	// //////// FUNCIONALIDADES DE LOS COMPONENTES DEL FRAME
	// ////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: jMenuItemCreateQuestionsFileActionPerformed. PERTENECE A: Clase
	 * TestEditor. LLAMADA POR: Al hacer click sobre
	 * jMenuBar->jMenuItemCreateQuestionFile. LLAMA A: createQuestionsFile();
	 * RECIBE: nada. DEVUELVE: void. FUNCI�N: Este m�todo muestra al usuario un
	 * JDialog, de tipo DlgSelectCourseAndConcept, para que seleccione el curso,
	 * dentro del cual se va a crear el nuevo fichero de preguntas, e indique a
	 * que concepto le va a asociar el fichero. A continuaci�n invoca al m�todo
	 * de esta misma clase createdFile, para que este env�e la informaci�n
	 * necesaria al servidor para que cree el nuevo fichero de preguntas. Si la
	 * operaci�n sucede con �xito, entonces se muestra al usuario un
	 * frame interno de tipo FrmEditQuestion configurado para que el usuario
	 * introduzca la informaci�n necesaria para la creaci�n de la nueva pregunta
	 * del fichero.
	 */

	void jMenuItemCreateQuestionsFileActionPerformed() {
		String numAnswersString = ""; // Almacena el n�mero de respuestas por
		// defecto que contienen las preguntas del
		// fichero que se va a crear.
		int numAnswersInt = 0; // Almacena el n�mero de respuestas por
		// defecto que contienen las preguntas del
		// fichero que se va a crear.
		boolean replace = false; // Indicar�, en el caso que exista un fichero
		// con
		// el mismo nombre que el que se va a crear, si este
		// debe ser reemplazado por el nuevo.

		// Reserva de memoria para el JDialog de tipo
		// DlgSelectCourseAndConcept.
		dlgCreateQuestionsFile = new DlgCreateQuestionsFile("Create question file", courseConceptVector, this);

		if (dlgCreateQuestionsFile.id) {
			paint(getGraphics());

			// Lectura de los datos introducidos por el usuario en el
			// dlgCourseQuestionsFile.
			String courseName = (String) dlgCreateQuestionsFile.jComboCourse.getSelectedItem();
			String concept = (String) dlgCreateQuestionsFile.jComboConcept.getSelectedItem();
			String questionsFileName = dlgCreateQuestionsFile.jTextFieldQuestionsFile.getText().trim();
			numAnswersString = (String) dlgCreateQuestionsFile.jComboNumAnswers.getSelectedItem();
			numAnswersInt = Integer.valueOf(numAnswersString).intValue();

			dlgCreateQuestionsFile = null;

			// Llamada al m�todo de esta misma clase que env�a los datos necesarios
			// al servidor para que cree el nuevo fichero de preguntas.
			boolean exitWhile = false;
			String createFile = "";
			String questionsFileNameOld = questionsFileName;

			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			while (exitWhile == false) {
				paint(getGraphics());

				createFile = createQuestionsFile(courseName, concept, questionsFileName, numAnswersInt, replace,
						userLogin);

				// Si la respuestsa del servidor a la petici�n realizada es null, se
				// muestra un mensaje de error.
				if (createFile == null) {
					createFile = "ERROR: File has not been created. \n"
							+ "There was an error when connecting to the server.";
					exitWhile = true;
				}

				// Comprobaci�n de la respuesta del servidor.
				if (createFile.startsWith(TestEditor.TEXT_ANOTHER_FILE_EXISTS)) {
					// Mensaje para preguntar si el fichero de preguntas, que tiene
					// el
					// mismo nombre que el que se va a crear, debe ser reemplazado.
					JOptionPane dlgMessage = new JOptionPane(createFile, 1, 0);
					dlgMessage.setFont(new java.awt.Font("Dialog", 0, 16));
					int result = JOptionPane.showConfirmDialog(this, createFile,
							"Confirm file replace", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE);

					if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
						if (result == JOptionPane.NO_OPTION) {
							questionsFileName = questionsFileNameOld;

							// Petici�n al usuario de una palabra o palabras, que se
							// a�adir�n
							// al nombre por defecto ya creado, para distingirlo del
							// archivo
							// que ya existe.
							String wordToAdd = JOptionPane.showInputDialog(this, "Introduce a distinctive word for "
									+ "the name of question file:", "New Name", JOptionPane.WARNING_MESSAGE);
							if (wordToAdd != null && wordToAdd.equals("") == false) {
								questionsFileNameOld = questionsFileName;

								questionsFileName = questionsFileName.substring(0, questionsFileName
										.lastIndexOf(courseName) - 1)
										+ " "
										+ wordToAdd
										+ questionsFileName.substring(questionsFileName.lastIndexOf(courseName) - 1);
								questionsFileName = questionsFileName.replaceAll(" ", "-");

								JOptionPane.showMessageDialog(this, "The new name of the question file is: \n"
										+ questionsFileName, "New Name", JOptionPane.WARNING_MESSAGE);

								replace = false;
							} else
								exitWhile = true;
						} else
							replace = true;
					} else
						exitWhile = true;
				} else {
					exitWhile = true;

					// Mensaje para mostrar la respuesta del servidor a la petici�n.
					if (!createFile.startsWith(TEXT_FILE_CREATED)) {
						JOptionPane.showMessageDialog(this, createFile, "Result of creating the file", 1);
					}
				}
			}

			// Comprobaci�n de respuesta afirmativa para la petici�n por parte del servidor.
			if (createFile.startsWith(TestEditor.TEXT_FILE_CREATED)) {
				// Muestra el formulario para que el usario introduzca los datos
				// necesarios para la creaci�n de la primera pregunta del fichero.
				String title = "Add Question To File.  Course: " + courseName + " - Question File: " + questionsFileName;
				FrmEditQuestion frmEditQuestion = new FrmEditQuestion(title, numAnswersInt,
						userLogin, userPassword, courseName, questionsFileName, codeBase, wowPath,
						iconTestEditorPath, this, NEW_QUESTION_FILE);

				jDesktopPane.add(frmEditQuestion, layer);
				layer++;

				try {
					frmEditQuestion.setSelected(true);
				} catch (java.beans.PropertyVetoException eP) {}
			}

			// Cursor estandar.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		dlgCreateQuestionsFile = null;
		jMenuBar.setSelected(null);
	}

	/*
	 * NOMBRE: jMenuItemDeleteQuestionFileActionPerformed. PERTENECE A: Clase
	 * TestEditor. LLAMADA POR: Al hacer click sobre
	 * jMenuBar->jMenuItemDeleteQuestionFile. LLAMA A: La clase
	 * GetQuestionsFileNames. M�todo deleteQuestionsFile. RECIBE: Nada. DEVUELVE:
	 * void. FUNCI�N: Este m�todo muestra al usuario un JDialog, de tipo
	 * DlgSelectCourseAndQuestionsFile, para que seleccione un curso, y un
	 * fichero de preguntas, de ese curso, que va a ser eliminado del sistema. A
	 * continuaci�n invoca al m�todo de esta misma clase deletedFile, para que
	 * este env�e la informaci�n necesaria al servidor para que elimine el
	 * fichero de preguntas. Este m�todo tambi�n muestra al usuario un mensaje
	 * con el resultado de la operaci�n.
	 */

	void jMenuItemDeleteQuestionsFileActionPerformed() {
		courseVector = null;

		workerGetQuestionsFileNames = new GetQuestionsFileNames("");
		workerGetQuestionsFileNames.start();

		// Llamada a la clase para repintar la barra de progreso.
		workerRepaintProgressBar = new RepaintProgressBar();
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try {
			workerGetQuestionsFileNames.join();
			workerRepaintProgressBar.join();
		} catch (java.lang.InterruptedException ie) {}

		workerTimeControl.stop();

		if (taskDone == -1) {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerGetQuestionsFileNames.stop();
			workerRepaintProgressBar.stop();
			return;
		}

		// Llamada al m�todo de esta misma clase que env�a al servidor los datos
		// para que este devuelta los nombres de los ficheros de preguntas de
		// todos
		// los cursos.

		// Si la respuesta del servidor es null, se muestra un mensaje de error.
		if (courseVector != null) {
			// Reserva de memoria y mostrado del JDialog de tipo
			// DlgSelectCourseAndQuestionsFile.
			dlgCourseAndQuestionsFile = new DlgSelectCourseAndQuestionsFile("Delete question file",
					courseVector, this);

			if (dlgCourseAndQuestionsFile.id == true) {
				paint(getGraphics());

				// Lectura de los datos introducidos por el usuario en el JDialog
				// anterior.
				String courseName = (String) dlgCourseAndQuestionsFile.jComboCourse.getSelectedItem();
				String questionsFileName = (String) dlgCourseAndQuestionsFile.jComboQuestionsFile
						.getSelectedItem();

				dlgCourseAndQuestionsFile = null;

				// Mensaje para confirmar el borrado del fichero de preguntas.
				JOptionPane dlgMessage = new JOptionPane();
				dlgMessage.setFont(new java.awt.Font("Dialog", 0, 16));
				int result = JOptionPane.showConfirmDialog(this,
						"Are you sure you want to delete the question file?", "Delete Question File",
						JOptionPane.YES_NO_OPTION);

				// Si la respuesta al mensaje anterior es NO entonces se termina la
				// ejecuci�n.
				if (result == JOptionPane.NO_OPTION)
					return;

				// Cursor en espera.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				// C�lculo del n�mero de frame internos que se est�n usando
				// actualmente.
				int numberInternalFrames = jDesktopPane.getComponentCount();
				boolean useCurrentQuestionsFile = false;

				// Si hay frames internos ejecutandose actualmente, se comprueba si
				// estos
				// est�n usando el fichero de preguntas que se va a eliminar.
				if (numberInternalFrames > 0) {
					// Obtenci�n de todos los frames iternos que se est�n ejecutando.
					JInternalFrame[] frames = jDesktopPane.getAllFrames();

					// Bucle para la comprobaci�n de cuales de los frames internos
					// anteriores est�n usando el fichero que se va a eliminar.
					for (int i = 0; i < frames.length; i++) {
						paint(getGraphics());

						try {
							FrmEditQuestion frmEditQuestion = (FrmEditQuestion) frames[i];
							if (frmEditQuestion.courseName.equals(courseName)
									&& frmEditQuestion.questionsFileName.equals(questionsFileName)
									&& frmEditQuestion.isVisible()) {
								useCurrentQuestionsFile = true;
								break;
							}
						} catch (java.lang.ClassCastException cce) {
							try {
								FrmEditTest frmEditTest = (FrmEditTest) frames[i];

								if (frmEditTest.courseName.equals(courseName)) {
									if (frmEditTest.checkQuestionsFileInTest(questionsFileName)) {
										useCurrentQuestionsFile = true;
										break;
									}
								}
							} catch (java.lang.ClassCastException e1) {}
						}
					}
				}

				// Si alguno de los frames internos anteriores est� usando el fichero
				// a eliminar, entonces se muestra un mensaje al usuario para que
				// confirme la eliminaci�n del fichero.
				if (useCurrentQuestionsFile == true) {
					result = JOptionPane.showConfirmDialog(this, "You are using the file that you want to delete."
							+ "\n" + "If you delete the file, the modifications will get lost."
							+ "Are you sure of continuing?", "File in use", JOptionPane.YES_NO_CANCEL_OPTION);

					// Si la respuesta es afirmativa, se procede al borrado del
					// fichero.
					if (result == JOptionPane.YES_OPTION) {
						// Llamada al m�todo de esta misma clase que env�a al servidor
						// los datos necesarios para que elimine el fichero de
						// preguntas.
						String deleteFile = deleteQuestionsFile(courseName, questionsFileName);

						// Si la respuesta del servidor es null, se prepara un mensaje
						// de
						// error.
						if (deleteFile == null)
							deleteFile = "ERROR: The file has not been deleted,"
									+ "\nthere was an error when connecting to the server.";

						// Mensaje con el resultado de la petici�n.
						dlgMessage = new JOptionPane();
						JOptionPane.showMessageDialog(this, deleteFile, "Result of elimination of the file", 1);
						dlgCourseAndQuestionsFile = null;

						// Objetenci�n del n�mero de frames internos que se est�n
						// ejecutando.
						numberInternalFrames = jDesktopPane.getComponentCount();

						if (numberInternalFrames > 0 && deleteFile.startsWith(TestEditor.TEXT_FILE_DELETED)) {
							// Obtenci�n de todos los frames internos que se est�n
							// ejecutando.
							JInternalFrame[] frames = jDesktopPane.getAllFrames();

							// Bucle para el cerrado de los frames que estaban
							// usando el fichero de preguntas que se ha borrado.
							for (int i = 0; i < frames.length; i++) {
								paint(getGraphics());

								try {
									FrmEditQuestion frmEditQuestion = (FrmEditQuestion) frames[i];
									if (frmEditQuestion.courseName.equals(courseName)
											&& frmEditQuestion.questionsFileName.equals(questionsFileName))
										frmEditQuestion.dispose();
								} catch (java.lang.ClassCastException cce) {
									try {
										FrmEditTest frmEditTest = (FrmEditTest) frames[i];

										if (frmEditTest.courseName.equals(courseName))
											frmEditTest.deleteQuestionsFile(questionsFileName);

									} catch (java.lang.ClassCastException e1) {}
								}
							}
						}
					}
				} else {
					// Llamada al m�todo de esta misma clase que env�a al servidor
					// los datos necesarios para que elimine el fichero de preguntas.
					String deleteFile = deleteQuestionsFile(courseName, questionsFileName);

					// Si la respuesta del servidor es null, se prepara un mensaje de
					// error.
					if (deleteFile == null)
						deleteFile = "ERROR: The file has not been deleted,"
								+ "\nthere was an error when connecting to the server.";

					// Mensaje con el resultado de la petici�n.
					dlgMessage = new JOptionPane();
					JOptionPane.showMessageDialog(this, deleteFile, "Result of elimination of the file", 1);
					dlgCourseAndQuestionsFile = null;
				}

				// Cursor estandar.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		} else {
			dlgCourseAndQuestionsFile = null;

			// Mensaje para indicar al usuario que se ha producido un error al
			// intentar
			// conseguir los nombres de los ficheros de preguntas de los cursos.
			JOptionPane dlgMessage = new JOptionPane();
			dlgMessage.setFont(new java.awt.Font("Dialog", 0, 16));
			JOptionPane.showMessageDialog(this, "ERROR: There is not question files to delete.",
					"Delete Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * NOMBRE: jMenuItemExamineQuestionFileActionPerformed. PERTENECE A: Clase
	 * TestEditor. LLAMADA POR: Al hacer click sobre
	 * jMenuBar->jMenuItemExamineQuestionFile. LLAMA A: La clase
	 * GetQuestionsFileNames. RECIBE: nada, DEVUELVE: void. FUNCI�N: Este m�todo
	 * muestra al usuario un JDialog, de tipo DlgSelectCourseAndQuestionsFile,
	 * para que seleccione un curso, y el fichero de preguntas, de ese curso, que
	 * va a ser examinado. A continuaci�n inicializa y muestra un frame interno
	 * de tipo FrmExamineQuestionsFile que contiene los datos de todas las
	 * preguntas del fichero de preguntas que va a ser examinado.
	 */

	void jMenuItemExamineQuestionsFileActionPerformed() {
		// String numAnswersString = ""; //Almacena el n�mero de respuestas por
		// defecto de las preguntas del fichero.
		// int numAnswersInt = 0; //Almacena el n�mero de respuestas por
		// defecto de las preguntas del fichero.

		// Llamada al m�todo de esta misma clase que env�a al servidor los datos
		// para que este devuelta los nombres de los ficheros de preguntas de
		// todos
		// los cursos.

		workerGetQuestionsFileNames = new GetQuestionsFileNames("");
		workerGetQuestionsFileNames.start();

		// Llamada a la clase para repintar la barra de progreso.
		workerRepaintProgressBar = new RepaintProgressBar();
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try {
			workerGetQuestionsFileNames.join();
			workerRepaintProgressBar.join();
		} catch (java.lang.InterruptedException ie) {}

		workerTimeControl.stop();

		if (taskDone == -1) {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerGetQuestionsFileNames.stop();
			workerRepaintProgressBar.stop();
			return;
		}

		// Si la respuesta del servidor es null, se muestra un mensaje de error.
		if (courseVector != null) {
			// Reserva de memoria y mostrado del JDialgo de tipo
			// DlgSelectCourseAndQuestionsFile.
			dlgCourseAndQuestionsFile = new DlgSelectCourseAndQuestionsFile("Examine question file",
					courseVector, this);

			if (dlgCourseAndQuestionsFile.id == true) {
				paint(getGraphics());

				// Cursor en espera.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				// Lectura de los datos introducidos por el usuario en el JDialog
				// anterior.
				String courseName = (String) dlgCourseAndQuestionsFile.jComboCourse.getSelectedItem();
				String questionsFileName = (String) dlgCourseAndQuestionsFile.jComboQuestionsFile
						.getSelectedItem();
				// numAnswersString =
				// dlgCourseAndQuestionsFile.jTextFieldNumAnswers.getText().trim();
				dlgCourseAndQuestionsFile = null;

				// Reserva de memoria y mostrado del frame interno de tipo
				// FrmExamineQuestionsFile.
				FrmExamineQuestionsFile frmExamineQuestionsFile = new FrmExamineQuestionsFile(
						"Examine File of Questions.  Course: " + courseName + " - Question File: "
								+ questionsFileName, 620, 400, userLogin, userPassword, courseName,
						questionsFileName, null, codeBase, wowPath, iconTestEditorPath, this,
						EXAM_QUESTION_FILE);

				// Adici�n del frame interno anterior al JFrame principal del
				// applet.
				jDesktopPane.add(frmExamineQuestionsFile, layer);
				layer++;

				try {
					frmExamineQuestionsFile.setSelected(true);
				} catch (java.beans.PropertyVetoException eP) {}

				// Cursor estandar.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		} else {
			dlgCourseAndQuestionsFile = null;
			// Mensaje para indicar al usuario que se ha producido un error al
			// intentar
			// conseguir los nombres de los ficheros de preguntas de los cursos.
			JOptionPane dlgMessage = new JOptionPane();
			dlgMessage.setFont(new java.awt.Font("Dialog", 0, 16));
			JOptionPane.showMessageDialog(this, "ERROR: There is not any question to examine.", "Examine Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * NOMBRE: jMenuItemExportQuestionFileActionPerformed. PERTENECE A: Clase
	 * TestEditor. LLAMADA POR: Al hacer click sobre
	 * jMenuBar->jMenuItemExportQuestionFile. LLAMA A: La clase
	 * GetQuestionsFileNames. M�todo exportQuestionsFile. RECIBE: nada DEVUELVE:
	 * void. FUNCI�N: Este m�todo muestra al usuario un JDialog, de tipo
	 * DlgExportQuestionsFile, para que seleccione un curso, y el fichero de
	 * preguntas, de ese curso, que va a ser exportado. A continuaci�n invoca al
	 * m�todo de esta misma clase exportQuestionsFile para que este env�e al
	 * servidor los datos necesarios para que se produzca la transformaci�n del
	 * fichero actual en uno pertenecientes a las versiones previas de WOW. Por
	 * �ltimo muestra un mensaje con el resultado de la petici�n al servidor.
	 */

	/**
	 * 
	 * @param theFormat
	 */
	void jMenuExportActionPerformed(final short theFormat) {
		courseVector = null;

		workerGetQuestionsFileNames = new GetQuestionsFileNames("");
		workerGetQuestionsFileNames.start();

		// Llamada a la clase para repintar la barra de progreso.
		workerRepaintProgressBar = new RepaintProgressBar();
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try {
			workerGetQuestionsFileNames.join();
			workerRepaintProgressBar.join();
		} catch (java.lang.InterruptedException ie) {}

		workerTimeControl.stop();

		if (taskDone == -1) {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerGetQuestionsFileNames.stop();
			workerRepaintProgressBar.stop();
			return;
		}

		// Si la respuesta del servidor es null, se muestra un mensaje de error.
		if (courseVector != null) {
			// Reserva de memoria y mostrado del JDialgo de tipo
			// DlgExportQuestionsFile.
			DlgExportQuestionsFile exportDialog = null;
			String title = "Export question file";
			String title2 = "";
			switch (theFormat) {
				case GIFT_FORMAT:
					title2 += " to GIFT format";
					break;
				case HOTPOTATOESv6_FORMAT:
					title2 += " to Hot Potatoes v6 format";
					break;
				case MOODLEXML_FORMAT:
					title2 += " to Moodle XML format";
					break;
				case QTIv1d2_FORMAT:
					title2 += " to Qti v1.2 format";
					break;
				case QTIv2d0_FORMAT:
					title2 += " to Qti v2.0 format";
					break;
				case SIETTE_FORMAT:
					title2 += " to Siette format";
					break;
				case WEBCT_FORMAT:
					title2 += " to WebCT format";
					break;
			}
			title += title2;
			
			exportDialog = new DlgExportQuestionsFile(title, courseVector, this);

			if (exportDialog.getId() == true) {
				paint(getGraphics());

				// Cursor en espera.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				// Lectura de los datos introducidos por el usuario en el JDialog
				// anterior.
				String aCourseName = exportDialog.getCourseName();
				String aQuestionsFileName = exportDialog.getQuestionsFileName();
				String newQuestionsFileName = exportDialog.getNewQuestionsFileName();

				exportDialog = null;
				paint(getGraphics());

				String exportFile = exportQuestionsFile(aCourseName, aQuestionsFileName, newQuestionsFileName, theFormat);
				
				// Error message if the response from the server is null
				if (exportFile == null || exportFile.trim().equals("")) {
					exportFile = "ERROR: The file has not been exported\n";
					exportFile += "There was an error exporting it.";
					JOptionPane.showMessageDialog(this, exportFile, "Error exporting file",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.home")));
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		 		chooser.setMultiSelectionEnabled(false);
		 		chooser.setDialogTitle("Select a folder where the test and the images will be exported to");
//		 		TestEditorFileFilter filter = new TestEditorFileFilter();
//		 		filter.addExtension("zip");
//		 		filter.setDescription("Zip Files");
//		 		filter.setExtensionListInDescription(true);
//		 		chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(this);
			   if (returnVal != JFileChooser.APPROVE_OPTION) {
			       return;
			   }

			   String theFolderOut = chooser.getSelectedFile().getAbsolutePath();

				// Receives the file
				try {
					// Requests the file to the server
					String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.DownloadFile";
					URL url = new URL(codeBase, petition);
					URLConnection connection = url.openConnection();
					connection.setDoInput(true);
					connection.setDoOutput(true);
					OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
					bufferOut.write("zipFile=" + URLEncoder.encode(exportFile, "utf-8"));
					bufferOut.flush();
					
					File tempFile = File.createTempFile(new Long(System.currentTimeMillis()).toString(), ".zip");
					BufferedInputStream input = new BufferedInputStream(connection.getInputStream());
					BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(tempFile));
					int i;
					while ((i = input.read()) != -1) {
						output.write(i);
					}
					output.flush();
					input.close();
					output.close();
					
					// Unzips the ZIP file in the folder
					try {
						UtilZip.unzipFile(tempFile, new File(theFolderOut));				
					} catch (Exception e) {
						throw e;
					}
					
					// Deletes the temp zip file
					tempFile.delete();

					JOptionPane.showMessageDialog(this, "Question file and their images have been exported in " + theFolderOut, "Result of file exporting",
							JOptionPane.INFORMATION_MESSAGE);
					
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, "Can't download the file from the server", "Error exporting file",
							JOptionPane.ERROR_MESSAGE);
				}
				
				
				// Cursor estandar.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		} else {
			dlgCourseAndQuestionsFile = null;
			// Mensaje para indicar al usuario que se ha producido un error al intentar
			// conseguir los nombres de los ficheros de preguntas de los cursos.
			JOptionPane.showMessageDialog(this, "ERROR: There is no question file to export", "Export Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/**
	 * 
	 */
	void jMenuExportWOWActionPerformed() {
		boolean replace = false;
		courseVector = null;

		workerGetQuestionsFileNames = new GetQuestionsFileNames("");
		workerGetQuestionsFileNames.start();

		// Llamada a la clase para repintar la barra de progreso.
		workerRepaintProgressBar = new RepaintProgressBar();
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try {
			workerGetQuestionsFileNames.join();
			workerRepaintProgressBar.join();
		} catch (java.lang.InterruptedException ie) {}

		workerTimeControl.stop();

		if (taskDone == -1) {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerGetQuestionsFileNames.stop();
			workerRepaintProgressBar.stop();
			return;
		}

		// Si la respuesta del servidor es null, se muestra un mensaje de error.
		if (courseVector != null) {
			// Reserva de memoria y mostrado del JDialgo de tipo
			// DlgExportQuestionsFile.
			DlgExportWOWQuestionsFile exportDialog = null;
			String title = "Export question file to WOW v2.0 format";
			exportDialog = new DlgExportWOWQuestionsFile(title, courseVector, this, wowPath);

			if (exportDialog.getId() == true) {
				paint(getGraphics());

				// Cursor en espera.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				// Lectura de los datos introducidos por el usuario en el JDialog
				// anterior.
				String courseName = exportDialog.getCourseName();
				String questionsFileName = exportDialog.getQuestionsFileName();
				String newQuestionsFileName = exportDialog.getNewQuestionsFileName();
				String testOldPath = exportDialog.getTestOldPath();

				exportDialog = null;

				if (!testOldPath.trim().endsWith("/")) {
					testOldPath = testOldPath.trim().concat("/");
				}

				// Formaci�n del path relativo del fichero destino.
				testOldPath = testOldPath.concat(newQuestionsFileName);

				boolean exitWhile = false;
				String exportFile = "";

				while (exitWhile == false) {
					paint(getGraphics());

					exportFile = exportWOWQuestionsFile(courseName, questionsFileName, testOldPath, replace);

					// Error message if the response from the server is null
					if (exportFile == null) {
						exportFile = "ERROR: The file has not been exported\n";
						exportFile += "There was an error connecting with the server.";
						exitWhile = true;
					}

					// Comprobaci�n de la respuesta del servidor.
					if (exportFile.startsWith(TEXT_ANOTHER_FILE_EXISTS)) {
						// Mensaje para preguntar si el fichero de preguntas, que tiene el
						// mismo nombre que el que se va a crear, debe ser reemplazado.
						JOptionPane dlgMessage = new JOptionPane(exportFile, 1, 0);
						dlgMessage.setFont(new java.awt.Font("Dialog", 0, 16));
						int result = JOptionPane.showConfirmDialog(this, exportFile,
								"Result of exporting the file", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.WARNING_MESSAGE);

						if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
							if (result == JOptionPane.NO_OPTION) {
								// Petici�n al usuario de cambio de nombre, puesto que ya existe
								// un fichero en la ruta destino con ese nombre.
								String inputValue = JOptionPane.showInputDialog(this, "Introduce a new name:", "New Name",
										JOptionPane.WARNING_MESSAGE);
								testOldPath = testOldPath.substring(0, testOldPath.lastIndexOf("/") + 1) + inputValue;

								if (!testOldPath.equals("")) {
									testOldPath = testOldPath.trim().replaceAll(" ", "-");

									JOptionPane.showMessageDialog(this, "The new name of the new question file is: \n"
											+ testOldPath.substring(testOldPath.lastIndexOf("/") + 1), "New Name",
											JOptionPane.WARNING_MESSAGE);

									replace = false;
								} else
									exitWhile = true;
							} else
								replace = true;
						} else
							exitWhile = true;
					} else {
						exitWhile = true;

						// Mensaje para mostrar la respuesta del servidor a la
						// petici�n.
						JOptionPane.showMessageDialog(this, exportFile, "Result of exporting the file", 1);
					}
				}

				// Cursor estandar.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		} else {
			dlgCourseAndQuestionsFile = null;
			// Mensaje para indicar al usuario que se ha producido un error al intentar
			// conseguir los nombres de los ficheros de preguntas de los cursos.
			JOptionPane.showMessageDialog(this, "ERROR: There is no question file to export", "Export Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	


	/**
	 * 
	 * @param theFormat
	 */
	void jMenuImportActionPerformed(final short theFormat) {
		DlgImportQuestionsFile importDialog = null;
		String title = "Import question file";
		String title2 = "";
		switch (theFormat) {
			case WOWv2d0_FORMAT:
				title2 += " from WOW v2.0 format";
				break;
			case GIFT_FORMAT:
				title2 += " from GIFT format";
				break;
			case HOTPOTATOESv6_FORMAT:
				title2 += " from Hot Potatoes v6 format";
				break;
			case MOODLEXML_FORMAT:
				title2 += " from Moodle XML format";
				break;
			case QTIv1d2_FORMAT:
				title2 += " from Qti v1.2 format";
				break;
			case QTIv2d0_FORMAT:
				title2 += " from Qti v2.0 format";
				break;
			case SIETTE_FORMAT:
				title2 += " from Siette format";
				break;
			case WEBCT_FORMAT:
				title2 += " from WebCT format";
				break;
		}
		title += title2;
		
		JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.home")));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
 		chooser.setMultiSelectionEnabled(false);
 		chooser.setDialogTitle("Select a file with the questions");
// 		TestEditorFileFilter filter = new TestEditorFileFilter();
// 		filter.addExtension("zip");
// 		filter.setDescription("Zip Files");
// 		filter.setExtensionListInDescription(true);
// 		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
	   if (returnVal != JFileChooser.APPROVE_OPTION) {
	       return;
	   }

      // Takes the values from the user
	   //String theZipName = chooser.getSelectedFile().getName();
	   String theFile = chooser.getSelectedFile().getName();
		String theFolder =  chooser.getSelectedFile().getAbsolutePath();
		theFolder = theFolder.substring(0, theFolder.length() - (theFile.length() + 1));
	   
		// Calculates the number of files and folders
		File folder = new File(theFolder);
		int numFiles = 0;
		int numFolders = 0;
		File [] listFiles = folder.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			if (listFiles[i].isDirectory()) {
				numFolders++;
			} else {
				numFiles++;
			}
		}
		
		// Ask the user if he wants to zip the entire folder
		StringBuffer message = new StringBuffer();
		message.append("All the files in the directory ").append(theFolder);
		message.append(" will be compressed and sent to the WOW! server");
		message.append("\nThere are ");
		if (numFolders > 0) {
			message.append(numFolders).append(" folders and ");
		}
		message.append(numFiles).append(" files in that directory.");
		message.append("\nDo you really want to continue?");
		int value = JOptionPane.showConfirmDialog(this, message.toString(), "Confirm file import", JOptionPane.YES_NO_OPTION);
		
		if (value != JOptionPane.YES_OPTION) {
			return;
		}

		// ZIPs the entire folder
		String theZipName = "";
	   File tempZipFile = null;
		try {
			theZipName = new Long(System.currentTimeMillis()).toString() + ".zip";
			tempZipFile = File.createTempFile(theZipName, ".zip");
			tempZipFile.deleteOnExit();
			theZipName = tempZipFile.getAbsolutePath();

			Runtime r = Runtime.getRuntime();
			String command = "jar cfM  \"" + theZipName + "\" -C \"" + theFolder + "\" .";
			Process p = r.exec(command);
	   	// Waits for the process to stop
	   	int result = p.waitFor();
	   	// Takes a look at the result of the process
	   	if (result != 0) {
	   		JOptionPane.showMessageDialog(this, "ERROR: Error zipping the folder " + theFolder,
						"Error importing file", JOptionPane.ERROR_MESSAGE);
	   		return;
	   	}
		} catch (Exception e) {
			e.printStackTrace();
		}
   	
		// Now sends the folder to the server
		String folderInTheServer = "";

		try {
			// Sends the file to the server
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.UploadFile";
			URL url = new URL(codeBase, petition);
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			int i;
			BufferedInputStream  input = new BufferedInputStream(new FileInputStream(theZipName));
			BufferedOutputStream output = new BufferedOutputStream(connection.getOutputStream());
			while ((i = input.read()) != -1) {
			    output.write(i);
			}
			output.flush();
			input.close();
			output.close();
			
			// Receives the response
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lineTemp = "";
			while ((lineTemp = bufferIn.readLine()) != null) {
				folderInTheServer = folderInTheServer.concat(lineTemp);
			}
			bufferIn.close();
			
			if (folderInTheServer == null || folderInTheServer.trim().equals("")) {
				throw new Exception("Error sending the file to the server");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "ERROR: Error sending the ZIP file to the server",
					"Error importing file", JOptionPane.ERROR_MESSAGE);
   		return;
		}
		
		// Now deletes the zip temp file sent to the server
		tempZipFile.delete();

		importDialog = new DlgImportQuestionsFile(title, courseConceptVector, this, theFile);		

		if (!importDialog.getId()) {
			return;
		}

		try {
			paint(getGraphics());

			// Complete route to the question file in the server
			theFile = folderInTheServer + "/" + theFile;
			
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// Lectura de los datos introducidos por el usuario en el JDialog
			// anterior.
			String courseName = importDialog.getCourseName();
			String concept = importDialog.getConcept();
			//String questionsFileName = importDialog.getQuestionsFileName();
			String newQuestionsFileName = importDialog.getNewQuestionsFileName();
			importDialog = null;

			// Llamada al m�todo de esta misma clase que env�a al servidor
			// los datos necesarios para que importe el fichero de preguntas.
			boolean exitWhile = false;
			String importFile = "";
			String newQuestionsFileOldName = newQuestionsFileName;
			boolean replace = false;

			while (!exitWhile) {

				paint(getGraphics());

				importFile = importQuestionsFile(courseName, concept, theFile, newQuestionsFileName, replace, userLogin, theFormat);

				// Error if the response from the server is null
				if (importFile == null) {
					importFile = "ERROR: The file has not been imported";
					importFile += "There was an error connecting to the server.";
					exitWhile = true;
				}

				// Comprobaci�n de la respuesta del servidor.
				if (importFile.startsWith(TEXT_ANOTHER_FILE_EXISTS)) {
					// Mensaje para preguntar si el fichero de preguntas, que tiene el
					// mismo nombre que el que se va a crear, debe ser reemplazado.
					int result = JOptionPane.showConfirmDialog(this, importFile,
							"Result of importing the file" + title2, JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE);

					if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
						if (result == JOptionPane.NO_OPTION) {
							newQuestionsFileName = newQuestionsFileOldName;

							// Petici�n al usuario de una palabra o palabras, que se a�adir�n
							// al nombre por defecto ya creado, para distingirlo del archivo
							// que ya existe.
							String text = "Introduce a distinctive word for the name of the new question file:";
							String wordToAdd = JOptionPane.showInputDialog(this, text, "New Name", JOptionPane.WARNING_MESSAGE);
							if (wordToAdd != null && wordToAdd.equals("") == false) {
								newQuestionsFileOldName = newQuestionsFileName;

								newQuestionsFileName = newQuestionsFileName.substring(0, newQuestionsFileName
										.lastIndexOf(courseName) - 1)
										+ " "
										+ wordToAdd
										+ newQuestionsFileName
												.substring(newQuestionsFileName.lastIndexOf(courseName) - 1);
								newQuestionsFileName = newQuestionsFileName.replaceAll(" ", "-");

								JOptionPane.showMessageDialog(this, "The new name of the question file is: \n"
										+ newQuestionsFileName, "New Name", JOptionPane.WARNING_MESSAGE);
							
								replace = false;
							} else
								exitWhile = true;
						} else
							replace = true;
					} else
						exitWhile = true;
				} else {
					exitWhile = true;

					// Mensaje para mostrar la respuesta del servidor a la petici�n.
					JOptionPane.showMessageDialog(this, importFile, "Result of importing the file" + title2, 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally  {
			// Default cursor
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	

	/**
	 * 
	 */
	void jMenuImportWOWActionPerformed() {
		boolean replace = false;

		DlgImportWOWQuestionsFile importDialog = null;
		String title = "Import question file from WOW v2.0 format";
		importDialog = new DlgImportWOWQuestionsFile(title, courseConceptVector, this, wowPath);

		if (importDialog.getId()) {
			paint(getGraphics());

			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// Lectura de los datos introducidos por el usuario en el JDialog
			// anterior.
			String courseName = importDialog.getCourseName();
			String concept = importDialog.getConcept();
			String questionsFileName = importDialog.getQuestionsFileName();
			String newQuestionsFileName = importDialog.getNewQuestionsFileName();
			String testOldPath = importDialog.getTestOldPath();
			importDialog = null;

			if (!testOldPath.endsWith("/")) {
				testOldPath = testOldPath.concat("/");
			}

			// Creaci�n del path relativo del fichero a importar.
			testOldPath = testOldPath + questionsFileName;

			// Llamada al m�todo de esta misma clase que env�a al servidor
			// los datos necesarios para que importe el fichero de preguntas.
			boolean exitWhile = false;
			String importFile = "";
			String newQuestionsFileOldName = newQuestionsFileName;

			while (!exitWhile) {
				paint(getGraphics());

				importFile = importWOWQuestionsFile(courseName, concept, testOldPath, newQuestionsFileName, replace, userLogin);

				// Error if the response from the server is null
				if (importFile == null) {
					importFile = "ERROR: The file has not been imported";
					importFile += "There was an error connecting to the server.";
					exitWhile = true;
				}

				// Comprobaci�n de la respuesta del servidor.
				if (importFile.startsWith(TEXT_ANOTHER_FILE_EXISTS)) {
					// Mensaje para preguntar si el fichero de preguntas, que tiene el
					// mismo nombre que el que se va a crear, debe ser reemplazado.
					int result = JOptionPane.showConfirmDialog(this, importFile,
							"Result of importing the file", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE);

					if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
						if (result == JOptionPane.NO_OPTION) {
							newQuestionsFileName = newQuestionsFileOldName;

							// Petici�n al usuario de una palabra o palabras, que se a�adir�n
							// al nombre por defecto ya creado, para distingirlo del archivo
							// que ya existe.
							String text = "Introduce a distinctive word for the name of the new question file:";
							String wordToAdd = JOptionPane.showInputDialog(this, text, "New Name", JOptionPane.WARNING_MESSAGE);
							if (wordToAdd != null && wordToAdd.equals("") == false) {
								newQuestionsFileOldName = newQuestionsFileName;

								newQuestionsFileName = newQuestionsFileName.substring(0, newQuestionsFileName
										.lastIndexOf(courseName) - 1)
										+ " "
										+ wordToAdd
										+ newQuestionsFileName
												.substring(newQuestionsFileName.lastIndexOf(courseName) - 1);
								newQuestionsFileName = newQuestionsFileName.replaceAll(" ", "-");

								JOptionPane.showMessageDialog(this, "The new name of the question file is: \n"
										+ newQuestionsFileName, "New Name", JOptionPane.WARNING_MESSAGE);

								replace = false;
							} else
								exitWhile = true;
						} else
							replace = true;
					} else
						exitWhile = true;
				} else {
					exitWhile = true;

					// Mensaje para mostrar la respuesta del servidor a la petici�n.
					JOptionPane.showMessageDialog(this, importFile, "Result of importing the file", 1);
				}
			}
			// Cursor estandar.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	
	/*
	 * NOMBRE: jMenuItemExitActionPerformed. PERTENECE A: Clase TestEditor.
	 * LLAMADA POR: Al hacer click sobre jMenuBar->jMenuItemExit. LLAMA A:
	 * closingWindow(); RECIBE: Nada. DEVUELVE: void. FUNCI�N: Este m�todo invoca
	 * al m�todo de la misma clase closingWindow, el cual realiza una serie de
	 * operaciones y comprobaciones antes de cerra el programa principal.
	 */

	void jMenuItemExitActionPerformed() {
		closingWindow();
	}

	/*
	 * NOMBRE: jMenuItemAddQuestionToFileActionPerformed. PERTENECE A: Clase
	 * TestEditor. LLAMADA POR: Al hacer click sobre
	 * jMenuBar->jMenuItemAddQuestionToFile. LLAMA A: La clase
	 * GetQuestionsFileNames. RECIBE: nada. DEVUELVE: void. FUNCI�N: Este m�todo
	 * muestra al usuario un JDialog de tipo DlgSelectCourseAndQuestionsFile,
	 * para que seleccione un curso y un fichero de preguntas de dicho curso, al
	 * cual se le van a a�adir m�s preguntas. A continuaci�n inicializa y muestra
	 * un frame interno de tipo FrmEditQuestion para que el usuario introduzca
	 * los datos necesarios para crear la pregunta que se va a a�adir.
	 */

	void jMenuItemAddQuestionToFileActionPerformed() {
		String numAnswersString = ""; // Almacena el n�mero de respuestas por
		// defecto de las preguntas del fichero
		// de preguntas al que se le va a a�adir
		// una pregunta.
		// int numAnswersInt = 0; //Almacena el n�mero de respuestas por
		// defecto de las preguntas del fichero
		// de preguntas al que se le va a a�adir
		// una pregunta.

		courseVector = null;

		workerGetQuestionsFileNames = new GetQuestionsFileNames("");
		workerGetQuestionsFileNames.start();

		// Llamada a la clase para repintar la barra de progreso.
		workerRepaintProgressBar = new RepaintProgressBar();
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try {
			workerGetQuestionsFileNames.join();
			workerRepaintProgressBar.join();
		} catch (java.lang.InterruptedException ie) {}

		workerTimeControl.stop();

		if (taskDone == -1) {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerGetQuestionsFileNames.stop();
			workerRepaintProgressBar.stop();
			return;
		}

		// Si la respuesta del servidor es null, se muestra un mensaje de error.
		if (courseVector != null) {
			// Reserva de memoria y mostrado del JDialgo de tipo
			// DlgSelectCourseAndQuestionsFile.
			dlgCourseAndQuestionsFile = new DlgSelectCourseAndQuestionsFile("Add question to file",
					courseVector, this);

			if (dlgCourseAndQuestionsFile.id == true) {
				// Lectura de los datos introducidos por el usuario en el JDialog
				// anterior.
				String courseName = (String) dlgCourseAndQuestionsFile.jComboCourse.getSelectedItem();
				String questionsFileName = (String) dlgCourseAndQuestionsFile.jComboQuestionsFile
						.getSelectedItem();
				numAnswersString = dlgCourseAndQuestionsFile.jTextFieldNumAnswers.getText();
				// Comprobaci�n de los datos introducidos por el usuario en el
				// JDialog.
				if (numAnswersString.equals("0") || courseName.equals("") == true
						|| questionsFileName.equals("") == true) {
					// Mensaje para indicar al usuario un error al leer los datos
					// introducidos anteriormente en el JDialog.
					JOptionPane.showMessageDialog(this, "ERROR: Error when reading course or questionsFile.",
							"Error to add question", JOptionPane.ERROR_MESSAGE);
					dlgCourseAndQuestionsFile = null;
				} else {
					dlgCourseAndQuestionsFile = null;
					paint(getGraphics());

					// Reserva de memoria y mostrado del frame interno de tipo
					// FrmEditQuestion para que el usario introduzca los datos
					// necesarios
					// para la creaci�n de la pregunta que se va a a�adir al fichero.
					FrmEditQuestion frmAddQuestionData = new FrmEditQuestion("Add Question To File.  Course: "
							+ courseName + " - Question File: " + questionsFileName, Integer.valueOf(
							numAnswersString).intValue(), userLogin, userPassword, courseName,
							questionsFileName, codeBase, wowPath, iconTestEditorPath, this,
							NEW_QUESTION_FILE);
					jDesktopPane.add(frmAddQuestionData, layer);
					layer++;

					try {
						frmAddQuestionData.setSelected(true);
					} catch (java.beans.PropertyVetoException eP) {}
				}
			} else
				dlgCourseAndQuestionsFile = null;
		} else {
			dlgCourseAndQuestionsFile = null;
			// Mensaje para indicar al usuario que se ha producido un error al
			// intentar
			// conseguir los nombres de los ficheros de preguntas de los cursos.
			JOptionPane.showMessageDialog(this,
					"ERROR: Doesn't exist any question file to add more questions.", "Add Error",
					JOptionPane.ERROR_MESSAGE);
		}
		jMenuBar.setSelected(null);
	}

	/*
	 * NOMBRE: jMenuItemEditQuestionActionPerformed. PERTENECE A: Clase
	 * TestEditor. LLAMADA POR: Al hacer click sobre jMenuBar->
	 * jMenuItemEditQuestion. LLAMA A: La clase GetQuestionsFileNames. RECIBE:
	 * Nada. DEVUELVE: void. FUNCI�N: Este m�todo muestra al usuario un JDialog
	 * de tipo DlgSelectCourseAndQuestionsFile, para que seleccione un curso y un
	 * fichero de preguntas de dicho curso, al cual se le van a realizar updates
	 * a alguna de sus preguntas. A continuaci�n inicializa y muestra un
	 * frame interno de tipo FrmEditQuestion para que el usuario busque la
	 * pregunta deseada y la modifique.
	 */

	void jMenuItemEditQuestionActionPerformed() {
		String numAnswersString = ""; // Almacena el n�mero de respuestas por
		// defecto de las preguntas del fichero
		// de preguntas al que se le va a a�adir
		// una pregunta.
		// int numAnswersInt = 0; //Almacena el n�mero de respuestas por
		// defecto de las preguntas del fichero
		// de preguntas al que se le va a a�adir
		// una pregunta.

		courseVector = null;

		workerGetQuestionsFileNames = new GetQuestionsFileNames("");
		workerGetQuestionsFileNames.start();

		// Llamada a la clase para repintar la barra de progreso.
		workerRepaintProgressBar = new RepaintProgressBar();
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try {
			workerGetQuestionsFileNames.join();
			workerRepaintProgressBar.join();
		} catch (java.lang.InterruptedException ie) {}

		workerTimeControl.stop();

		if (taskDone == -1) {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerGetQuestionsFileNames.stop();
			workerRepaintProgressBar.stop();
			return;
		}

		// Si la respuesta del servidor es null, se muestra un mensaje de error.
		if (courseVector != null) {
			// Reserva de memoria y mostrado del JDialgo de tipo
			// DlgSelectCourseAndQuestionsFile.
			dlgCourseAndQuestionsFile = new DlgSelectCourseAndQuestionsFile("Edit question", courseVector, this);

			if (dlgCourseAndQuestionsFile.id == true) {
				paint(getGraphics());

				// Cursor en espera.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				// Lectura de los datos introducidos por el usuario en el JDialog
				// anterior.
				courseName = (String) dlgCourseAndQuestionsFile.jComboCourse.getSelectedItem();
				questionsFileName = (String) dlgCourseAndQuestionsFile.jComboQuestionsFile.getSelectedItem();
				numAnswersString = dlgCourseAndQuestionsFile.jTextFieldNumAnswers.getText();

				// Comprobaci�n de los datos introducidos por el usuario en el
				// JDialog.
				if (numAnswersString.equals("0") || courseName.equals("") == true
						|| questionsFileName.equals("") == true) {
					// Mensaje para indicar al usuario un error al leer los datos
					// introducidos anteriormente en el JDialog.
					JOptionPane.showMessageDialog(this, "ERROR: Error when reading course or question file."
							+ "\n" + "Data Incomplete.", "Error to modify question", JOptionPane.ERROR_MESSAGE);
					dlgCourseAndQuestionsFile = null;
				} else {
					dlgCourseAndQuestionsFile = null;

					// Comprobaci�n que no hay abierto otro frame interno de tipo
					// FrmEditQuestion con type = 2 y questionsFileName igual al
					// introducido por el usuario en el JDialog anterior.

					// Obtenci�n de todos lo frames internos que se est�n ejecutando
					// actualmente.
					JInternalFrame[] frames = jDesktopPane.getAllFrames();

					// Bucle para la llamada a los m�todos closingWindow de cada uno
					// de los
					// frames internos obtenidos anteriormente.
					for (int i = 0; i < frames.length; i++) {
						paint(getGraphics());

						try {
							FrmEditQuestion frmEditQuestion = (FrmEditQuestion) frames[i];

							if (frmEditQuestion.type == MODIFY_QUESTION_OF_A_FILE
									&& frmEditQuestion.courseName.equals(courseName)
									&& frmEditQuestion.questionsFileName.equals(questionsFileName)
									&& frmEditQuestion.isVisible() == true) {
								if (frmEditQuestion.questionModified == true) {
									// Mensaje para indicar al usuario que no puede haber
									// dos
									// frames internos de tipo FrmEditQuestion con type =
									// 2 y con
									// el mismo questionsFileName que el intrudocido por
									// el usuario
									// en el JDialog anterior.
									JOptionPane.showMessageDialog(this, "WARNING: You can not load the question data."
											+ "\n" + "You are modifying another question of this same file."
											+ "Save these modifications and attempt it again..", "WARNING",
											JOptionPane.WARNING_MESSAGE);

									// Cursor estandar.
									super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

									// Mostrado de dicho forumulario.
									try {
										frmEditQuestion.moveToFront();
									} catch (Exception pve) {}
									return;
								} else
									frmEditQuestion.dispose();
							}
						} catch (java.lang.ClassCastException cce) {}
					}

					// Reserva de memoria y mostrado del frame iterno de tipo
					// FrmEditQuestion para que el usario introduzca los datos necesarios
					// para la creaci�n de la pregunta que se va a a�adir al fichero.
					FrmEditQuestion frmModifyQuestionData = new FrmEditQuestion("Question edition.  Course: "
							+ courseName + " - Question file: " + questionsFileName, Integer.valueOf(
							numAnswersString).intValue(), userLogin, userPassword, courseName,
							questionsFileName, codeBase, wowPath, iconTestEditorPath, this,
							MODIFY_QUESTION_OF_A_FILE);
					jDesktopPane.add(frmModifyQuestionData, layer);
					layer++;

					try {
						frmModifyQuestionData.setSelected(true);
					} catch (java.beans.PropertyVetoException eP) {
						// Mensaje para indicar al usuario que se ha producido un
						// error
						// al intentar mostrar el frame interno anterior.
						JOptionPane.showMessageDialog(this, eP.getMessage(), "Error to modify question",
								JOptionPane.ERROR_MESSAGE);
						dlgCourseAndQuestionsFile = null;
					}
				}

				// Cursor estandar.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} else
				dlgCourseAndQuestionsFile = null;
		} else {
			dlgCourseAndQuestionsFile = null;
			// Mensaje para indicar al usuario que se ha producido un error al
			// intentar
			// conseguir los nombres de los ficheros de preguntas de los cursos.
			JOptionPane.showMessageDialog(this, "ERROR : Doesn't exist any question to modify.", "Modify Error",
					JOptionPane.ERROR_MESSAGE);
		}
		jMenuBar.setSelected(null);
	}

	/*
	 * NOMBRE: jMenuItemFindQuestionsActionPerformed. PERTENECE A: Clase
	 * TestEditor. LLAMADA POR: Al hacer click sobre jMenuBar->
	 * jMenuItemFindQuestions. LLAMA A: La clase GetQuestionsFileNames. RECIBE:
	 * nada. DEVUELVE: void. FUNCI�N: Este m�todo muestra un frame interno de
	 * tipo FrmFindQuestions para que el usuario introduzca los par�metros
	 * necesarios para realizar la busqueda de una o varias preguntas dentro de
	 * un fichero de preguntas.
	 */

	void jMenuItemFindQuestionsActionPerformed() {
		courseVector = null;

		workerGetQuestionsFileNames = new GetQuestionsFileNames("");
		workerGetQuestionsFileNames.start();

		// Llamada a la clase para repintar la barra de progreso.
		workerRepaintProgressBar = new RepaintProgressBar();
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try {
			workerGetQuestionsFileNames.join();
			workerRepaintProgressBar.join();
		} catch (java.lang.InterruptedException ie) {}

		workerTimeControl.stop();

		if (taskDone == -1) {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerGetQuestionsFileNames.stop();
			workerRepaintProgressBar.stop();
			return;
		}

		if (courseVector != null) {
			paint(getGraphics());

			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// Reserva de memoria y mostrado del frame interno de tipo
			// FrmExamineQuestionsFile.
			FrmFindQuestions frmFindQuestions = new FrmFindQuestions("Find Questions in a File of Questions",
					620, 400, userLogin, userPassword, courseVector, codeBase, wowPath, iconTestEditorPath, this);

			// Adici�n del frame interno anterior al JFrame principal del applet.
			jDesktopPane.add(frmFindQuestions, layer);
			layer++;

			try {
				frmFindQuestions.setSelected(true);
			} catch (java.beans.PropertyVetoException eP) {}

			// Cursor estandar.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} else {
			// Mensaje para indicar al usuario que se ha producido un error al
			// intentar
			// conseguir los nombres de los ficheros de preguntas de los cursos.
			JOptionPane.showMessageDialog(this, "ERROR: Doesn't exist any question file to find questions.",
					"Find Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * NOMBRE: jMenuItemCreateTestActionPerformed. PERTENECE A: Clase TestEditor.
	 * LLAMADA POR: Al hacer click sobre jMenuBar-> jMenuItemCreateClassicTest. y
	 * jMenuItemCreateAdaptiveTest. LLAMA A: Nada. RECIBE: testType: Objeto de
	 * tipo String. Contiene "classic", o "adaptive". DEVUELVE: void. FUNCI�N:
	 * Este m�todo muestra un frame interno de tipo FrmCreateClassicTest para
	 * que el usuario introduzca los par�metros necesarios para realizar la
	 * construcci�n de un fichero de configuraci�n de una bateria de test
	 * cl�sica.
	 */

	void jMenuItemCreateTestActionPerformed(String testType) {
		// Reserva de memoria y mostrado del JDialog de tipo
		// DlgSelectCourse.
		dlgSelectCourse = new DlgSelectCourse("Create test file", courseNameVector, this);
		if (dlgSelectCourse.id == true) {
			paint(getGraphics());

			// Lectura de los datos introducidos por el usuario en el JDialog
			// anterior.
			final String courseName = dlgSelectCourse.jComboCourse.getSelectedItem().toString();
			Course course = null;
			for (int i = 0; i < courseConceptVector.size(); i++) {
				if (courseName.equals(((Course) courseConceptVector.get(i)).getName())) {
					course = (Course) courseConceptVector.get(i);
					break;
				}
			}

			// Si la respuesta del servidor es null, se muestra un mensaje de
			// error.
			if (course != null) {
				paint(getGraphics());

				// Cursor en espera.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				// Reserva de memoria y mostrado del frame interno para
				// CreateTest para que el usario introduzca los datos necesarios
				// para la creaci�n de una bateria de test de tipo cl�sico.
				int testTypeInt;
				String testTypeTxt = null;
				if (testType.equals(CLASSIC)) {
					testTypeInt = CLASSIC_NUM;
					testTypeTxt = TestEditor.CLASSIC;
				} else {
					testTypeInt = ADAPTIVE_NUM;
					testTypeTxt = TestEditor.ADAPTIVE;
				}
				String title = "Creation of " + testTypeTxt + " test - " + "Course: " + course.getName();
				FrmCreateTest createTest = new FrmCreateTest(title, 620, 400, userLogin, userPassword, course, codeBase, wowPath,
						iconTestEditorPath, myInstance, testTypeInt);
				
				jDesktopPane.add(createTest, layer);
				layer++;
				
				try {
					createTest.setSelected(true);
				} catch (java.beans.PropertyVetoException eP) {
					eP.printStackTrace();
				}

				// Cursor estandar.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			} else {
				// Mensaje para indicar al usuario que el curso seleccionado no
				// tiene
				// ficheros de preguntas
				JOptionPane.showMessageDialog(this, "ERROR: The selected course doesn't have concepts."
						+ "\nYou cannot create any test.", "Create Test Error", JOptionPane.ERROR_MESSAGE);
			}
			dlgSelectCourse = null;
		}
	}

	/*
	 * NOMBRE: jMenuItemDeleteClassicTestActionPerformed. PERTENECE A: Clase
	 * TestEditor. LLAMADA POR: Al hacer click sobre jMenuItemDeleteClassicTest y
	 * jMenuItemDeleteAdaptiveTest. LLAMA A: La clase GetTestsFileNames. M�todo
	 * deleteTestFile. RECIBE: testType: Objeto de tipo String. Contiene
	 * "classic", "adaptive" o "all" seg�n el tipo de test que se quiera
	 * modificar. DEVUELVE: void. FUNCI�N: Este m�todo muestra al usuario un
	 * JDialog, de tipo DlgSelectCourseAndTestFile, para que seleccione un curso,
	 * y el fichero de test, de ese curso, que va a ser eliminado.
	 */

	void jMenuItemDeleteTestActionPerformed(String testType) {
		courseVector = null;
		final String finalTestType = testType;

		workerGetTestsFileNames = new GetTestsFileNames("", finalTestType);
		workerGetTestsFileNames.start();

		// Llamada a la clase para repintar la barra de progreso.
		workerRepaintProgressBar = new RepaintProgressBar();
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try {
			workerGetTestsFileNames.join();
			workerRepaintProgressBar.join();
		} catch (java.lang.InterruptedException ie) {}

		workerTimeControl.stop();

		if (taskDone == -1) {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerGetTestsFileNames.stop();
			workerRepaintProgressBar.stop();
			return;
		}

		// Si la respuesta del servidor es null, se muestra un mensaje de error.
		if (courseVector != null) {
			// Reserva de memoria y mostrado del JDialgo de tipo
			// DlgSelectCourseAndTestFile.
			dlgCourseAndTestFile = new DlgSelectCourseAndTestFile("Delete test file", courseVector, this);
			if (dlgCourseAndTestFile.id == true) {
				paint(getGraphics());

				// Lectura de los datos introducidos por el usuario en el JDialog
				// anterior.
				String courseName = (String) dlgCourseAndTestFile.jComboCourse.getSelectedItem();
				String testFileName = (String) dlgCourseAndTestFile.jComboTestFile.getSelectedItem();
				dlgCourseAndTestFile = null;

				// Mensaje para confirmar el borrado del fichero de preguntas.
				int result = JOptionPane.showConfirmDialog(this,
						"Are you sure you want to eliminate the test file?", "Delete Test File",
						JOptionPane.YES_NO_OPTION);

				// Si la respuesta al mensaje anterior es NO entonces se termina la
				// ejecuci�n.
				if (result == JOptionPane.NO_OPTION)
					return;

				// Cursor en espera.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				// Llamada al m�todo de esta misma clase que env�a al servidor
				// los datos necesarios para que elimine el fichero de preguntas.
				String deleteFile = deleteTestFile(courseName, testFileName);

				// Si la respuesta del servidor es null, se prepara un mensaje de
				// error.
				if (deleteFile == null)
					deleteFile = "ERROR: The file has not been deleted,"
							+ "\nthere was an error when connecting to the server.";

				// Mensaje con el resultado de la petici�n.
				JOptionPane.showMessageDialog(this, deleteFile, "Result of elimination of the file", 1);

				dlgCourseAndQuestionsFile = null;

				// C�lculo del n�mero de frame interno que se est�n usando
				// actualmente.
				int numberInternalFrames = jDesktopPane.getComponentCount();

				// Si hay frame internos ejecutandose actualmente, se comprueba si
				// estos
				// est�n usando el fichero de preguntas que se va a eliminar.
				if (numberInternalFrames > 0) {
					// Obtenci�n de todos los frames internos que se est�n ejecutando.
					JInternalFrame[] frames = jDesktopPane.getAllFrames();

					// Bucle para la comprobaci�n de cuales de los frames internos
					// anteriores est�n usando el fichero que se va a eliminar.
					for (int i = 0; i < frames.length; i++) {
						paint(getGraphics());

						try {
							FrmEditQuestion frmEditQuestion = (FrmEditQuestion) frames[i];
							if (frmEditQuestion.courseName.equals(courseName)
									&& frmEditQuestion.type == MODIFY_QUESTION_OF_A_FILE
									&& frmEditQuestion.jComboBoxClassicTest.getItemCount() > 0
									&& frmEditQuestion.isVisible()) {
								Question question = getQuestionByCode(frmEditQuestion.courseName,
										frmEditQuestion.questionsFileName, frmEditQuestion.codeQuestion);
								frmEditQuestion.setQuestionData(question, frmEditQuestion.codeBase);
							}
						} catch (java.lang.ClassCastException cce) {
							try {
								FrmEditTest frmEditTest = (FrmEditTest) frames[i];

								if (frmEditTest.courseName.equals(test.getCourse())
										&& frmEditTest.testFileName.equals(testFileName)) {
									frmEditTest.dispose();
								}
							} catch (java.lang.ClassCastException e1) {}
						}
					}
				}

				// Cursor estandar.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		} else {
			dlgCourseAndTestFile = null;
			// Mensaje para indicar al usuario que se ha producido un error al
			// intentar
			// conseguir los nombres de los ficheros de test de los cursos.
			JOptionPane.showMessageDialog(this, "ERROR: There is not any test file to remove.",
					"Delete Test Error", JOptionPane.ERROR_MESSAGE);
		}

		// Cursor estandar.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}


	/**
	 * Shows a window where the user can select a test and generate a mobile JAR
	 * with that test.
	 * @param testType Type of test (ADAPTIVE or CLASSIC)
	 */
	protected void jMenuItemCreateMobileTestActionPerformed(final String testType) {
		courseVector = null;
		workerGetTestsFileNames = new GetTestsFileNames("", testType);
		workerGetTestsFileNames.start();

		// Repaints the progress bar
		workerRepaintProgressBar = new RepaintProgressBar();
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try {
			workerGetTestsFileNames.join();
			workerRepaintProgressBar.join();
		} catch (java.lang.InterruptedException ie) {}

		workerTimeControl.stop();

		if (taskDone == -1) {
			// Default cursor
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerGetTestsFileNames.stop();
			workerRepaintProgressBar.stop();
			return;
		}

		// If the response from the server is null, a error message is showed
		if (courseVector == null) {
			JOptionPane.showMessageDialog(this, "ERROR: There is not any " + testType + " test file to examine",
					"Create Mobile JAR error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Else, shows a window to select the test to export 
		dlgCourseAndTestFile = new DlgSelectCourseAndTestFile("Create Mobile JAR", courseVector, this);

		if (dlgCourseAndTestFile.id == true) {
			paint(getGraphics());

			// Waiting cursor
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// Reads the data from the user
			String theCourseName = (String) dlgCourseAndTestFile.jComboCourse.getSelectedItem();
			String theTestFileName = (String) dlgCourseAndTestFile.jComboTestFile.getSelectedItem();

			// Calls the server
			String result = createMobileJar(theCourseName, theTestFileName);

			// Default Cursor
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			if (result != null) {
				// Gets the URL from the WOW server
				String wowServer = codeBase.getProtocol() + "://";
				wowServer += codeBase.getHost() + ":" + codeBase.getPort();
				wowServer += codeBase.getFile();
				if (wowServer.indexOf("wow/") > 0) {
					wowServer = wowServer.substring(0, wowServer.indexOf("wow/") + 4);
				}
				result = wowServer + result;
				StringBuffer text = new StringBuffer();
				text.append("JAR file created in the hard disc of the server\n");
	   	   text.append("You can download it from ").append(result);
	   	   
	   	   // Copies the URL to the system clipboard to use it easily
	   	   try {
	   	   	Clipboard clipboard = getToolkit().getSystemClipboard();
		   	   StringSelection contents = new StringSelection(result);
		   	   clipboard.setContents(contents, this);
		   	   text.append("\nThis URL is on the system clipboard");
	   	   } catch (Exception e) {
	   	   	e.printStackTrace();
	   	   }
	   	   
				JOptionPane.showMessageDialog(this, text.toString(), "Create Mobile JAR", JOptionPane.INFORMATION_MESSAGE);
			}	
		}
	}

	/**
	 * Interface ClipboardOwner. This method notifies the lost of the clipboard
	 * @param clipboard System clipboard
	 * @param contents The contents that this window sets in the clipboard. Now are deleted.
	 */
	public void lostOwnership(final Clipboard clipboard, final Transferable contents) {
		// Do nothing
	}
	

	/*
	 * NOMBRE: jMenuItemEditTestActionPerformed. PERTENECE A: Clase TestEditor.
	 * LLAMADA POR: Al hacer click sobre jMenuItemEditClassicTest y
	 * jMenuItemEditAdaptiveTest. LLAMA A: La clase GetTestsFileNames. RECIBE:
	 * testType: Objeto de tipo String. Contiene "classic", "adaptive" o "all"
	 * seg�n el tipo de test que se quiera modificar. DEVUELVE: void. FUNCI�N:
	 * Este m�todo muestra al usuario un JDialog, de tipo
	 * DlgSelectCourseAndTestFile, para que seleccione un curso, y el fichero de
	 * test, de ese curso. Posteriormente mostrar� al usuario un frame interno
	 * de tipo FrmEditTest con en que se mostrar�n los par�metros de
	 * configuraci�n para la ejecuci�n del test.
	 */

	void jMenuItemEditTestActionPerformed(String testType) {
		courseVector = null;
		final String finalTestType = testType;

		workerGetTestsFileNames = new GetTestsFileNames("", finalTestType);
		workerGetTestsFileNames.start();

		// Llamada a la clase para repintar la barra de progreso.
		workerRepaintProgressBar = new RepaintProgressBar();
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try {
			workerGetTestsFileNames.join();
			workerRepaintProgressBar.join();
		} catch (java.lang.InterruptedException ie) {}

		workerTimeControl.stop();

		if (taskDone == -1) {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerGetTestsFileNames.stop();
			workerRepaintProgressBar.stop();
			return;
		}

		// Si la respuesta del servidor es null, se muestra un mensaje de error.
		if (courseVector != null) {
			// Reserva de memoria y mostrado del JDialgo de tipo
			// DlgSelectCourseAndTestFile.
			dlgCourseAndTestFile = new DlgSelectCourseAndTestFile("Edit test", courseVector, this);
			if (dlgCourseAndTestFile.id == true) {
				paint(getGraphics());

				// Cursor en espera.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				// Lectura de los datos introducidos por el usuario en el JDialog
				// anterior.
				String courseName = (String) dlgCourseAndTestFile.jComboCourse.getSelectedItem();
				String testFileName = (String) dlgCourseAndTestFile.jComboTestFile.getSelectedItem();
				dlgCourseAndTestFile = null;

				// Course course = null;
				// for(int i = 0; i < courseConceptVector.size(); i++)
				// {
				// if(courseName.equals(((Course)courseConceptVector.get(i)).getName()))
				// {
				// course = (Course)courseConceptVector.get(i);
				// break;
				// }
				// }

				// Obtenci�n de todos lo frames que se est�n ejecutando
				// actualmente.
				JInternalFrame[] frames = jDesktopPane.getAllFrames();

				// Bucle para la llamada a los m�todos closingWindow de cada uno de
				// los
				// frames internos obtenidos anteriormente.
				for (int i = 0; i < frames.length; i++) {
					paint(getGraphics());

					try {
						FrmEditTest frmEditTest = (FrmEditTest) frames[i];

						if (frmEditTest.courseName.equals(courseName)
								&& frmEditTest.testFileName.equals(testFileName) && frmEditTest.isVisible() == true) {
							frmEditTest.moveToFront();

							// Cursor estandar.
							super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							return;
						}
					} catch (java.lang.ClassCastException cce) {} 
				}

				// Reserva de memoria y mostrado del frame interno de tipo
				// FrmEditTest para que el usario modifique la configuraci�n de
				// ejecuci�n de la bateria de test.
				FrmEditTest frmEditTest = new FrmEditTest("Test edition. - " + "Course: " + courseName
						+ " - Test file: " + testFileName, 620, 400, userLogin, userPassword, courseName,
						testFileName, codeBase, wowPath, iconTestEditorPath, this, CLASSIC_NUM);

				jDesktopPane.add(frmEditTest, layer);
				layer++;

				try {
					frmEditTest.setSelected(true);
				} catch (java.beans.PropertyVetoException eP) {}

				// Cursor estandar.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		} else {
			dlgCourseAndTestFile = null;

			// Mensaje para indicar al usuario que se ha producido un error al
			// intentar
			// conseguir los nombres de los ficheros de test de los cursos.
			JOptionPane.showMessageDialog(this, "ERROR: There is not any test file to examine.",
					"Modify Test Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * NOMBRE: jMenuItemTestStatisticActionPerformed. PERTENECE A: Clase
	 * TestEditor. LLAMADA POR: Al hacer click sobre jMenuItemTestStatistic.
	 * LLAMA A: La clase GetTestsFileNames. RECIBE: testType: Objeto de tipo
	 * String. Contiene "classic", "adaptive" o "all" seg�n el tipo de test que
	 * se quiera modificar. DEVUELVE: void. FUNCI�N: Este m�todo muestra al
	 * usuario un JDialog, de tipo DlgSelectCourseAndTestFile, para que
	 * seleccione un curso, y el fichero de test, de ese curso. Posteriormente
	 * mostrar� al usuario un frame interno de tipo FrmStatisticTest con el que
	 * se mostrar�n algunos datos sobre la realizaci�n de test por parte de los
	 * estudiantes..
	 */

	void jMenuItemTestStatisticActionPerformed(String testType) {
		courseVector = null;
		final String finalTestType = testType;

		workerGetTestsFileNames = new GetTestsFileNames("", finalTestType);
		workerGetTestsFileNames.start();

		// Llamada a la clase para repintar la barra de progreso.
		workerRepaintProgressBar = new RepaintProgressBar();
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try {
			workerGetTestsFileNames.join();
			workerRepaintProgressBar.join();
		} catch (java.lang.InterruptedException ie) {}

		workerTimeControl.stop();

		if (taskDone == -1) {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerGetTestsFileNames.stop();
			workerRepaintProgressBar.stop();
			return;
		}

		// Si la respuesta del servidor es null, se muestra un mensaje de error.
		if (courseVector != null) {
			// Reserva de memoria y mostrado del JDialgo de tipo
			// DlgSelectCourseAndTestFile.
			dlgCourseAndTestFile = new DlgSelectCourseAndTestFile("Statistic of the test", courseVector, this);

			if (dlgCourseAndTestFile.id == true) {
				paint(getGraphics());

				// Cursor en espera.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				// Lectura de los datos introducidos por el usuario en el JDialog
				// anterior.
				String courseName = (String) dlgCourseAndTestFile.jComboCourse.getSelectedItem();
				String testFileName = (String) dlgCourseAndTestFile.jComboTestFile.getSelectedItem();
				dlgCourseAndTestFile = null;

				// Obtenci�n de todos lo frames que se est�n ejecutando
				// actualmente.
				JInternalFrame[] frames = jDesktopPane.getAllFrames();

				// Bucle para la llamada a los m�todos closingWindow de cada uno de
				// los
				// frames internos obtenidos anteriormente.
				for (int i = 0; i < frames.length; i++) {
					paint(getGraphics());

					try {
						FrmStatisticTest frmStatisticTest = (FrmStatisticTest) frames[i];

						if (frmStatisticTest.courseName.equals(courseName)
						&& frmStatisticTest.testFileName.equals(testFileName)
						&& frmStatisticTest.isVisible() == true) {
							frmStatisticTest.moveToFront();
							// Cursor estandar.
							super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							return;
						}
					} catch (java.lang.ClassCastException cce) { 
					}
				}

				// Reserva de memoria y mostrado del frame interno de tipo
				// FrmStatisticTest.
				FrmStatisticTest frmStatisticTest = new FrmStatisticTest("Statistic of the test - " + "Course: "
						+ courseName + " - Test file: " + testFileName, 620, 400, userLogin, userPassword,
						courseName, testFileName, codeBase, wowPath, iconTestEditorPath, this);

				jDesktopPane.add(frmStatisticTest, layer);
				layer++;

				try {
					frmStatisticTest.setSelected(true);
				} catch (java.beans.PropertyVetoException eP) {}

				// Cursor estandar.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		} else {
			dlgCourseAndTestFile = null;

			// Mensaje para indicar al usuario que se ha producido un error al
			// intentar
			// conseguir los nombres de los ficheros de test de los cursos.
			JOptionPane.showMessageDialog(this, "ERROR: There is not any test file to examine.",
					"Test Statistic Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * NOMBRE: jMenuItemCalibrateQuestionsFileActionPerformed. PERTENECE A: Clase
	 * TestEditor. LLAMADA POR: Al hacer click sobre
	 * jMenuItemCalibrateQuestionsFile. LLAMA A: La clase GetQuestionsFileNames.
	 * RECIBE: nada. DEVUELVE: void. FUNCI�N: Este m�todo muestra al usuario un
	 * JDialog, de tipo DlgCalibration, para que seleccione un curso, y el
	 * fichero de preguntas, de ese curso, que va a ser calibrado.
	 */

	void jMenuItemCalibrateQuestionsFileActionPerformed() {
		courseVector = null;

		workerGetQuestionsFileNames = new GetQuestionsFileNames("");
		workerGetQuestionsFileNames.start();

		// Llamada a la clase para repintar la barra de progreso.
		workerRepaintProgressBar = new RepaintProgressBar();
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try {
			workerGetQuestionsFileNames.join();
			workerRepaintProgressBar.join();
		} catch (java.lang.InterruptedException ie) {}

		workerTimeControl.stop();

		if (taskDone == -1) {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerGetQuestionsFileNames.stop();
			workerRepaintProgressBar.stop();
			return;
		}

		// Si la respuesta del servidor es null, se muestra un mensaje de error.
		if (courseVector != null) {
			// Reserva de memoria y mostrado del JDialgo de tipo
			// DlgCalibration.
			new DlgCalibration("Calibrate question file", courseVector, this);
		} else {
			// Mensaje para indicar al usuario que se ha producido un error al
			// intentar
			// conseguir los nombres de los ficheros de test de los cursos.
			JOptionPane.showMessageDialog(this, "ERROR: There is not any question file to calibrate.",
					"Calibrate Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * NOMBRE: jMenuItemUpdateStatisticDataActionPerformed. PERTENECE A: Clase
	 * TestEditor. LLAMADA POR: Al hacer click sobre
	 * jMenuItemUpdateStatisticData. LLAMA A: La clase GetQuestionsFileNames.
	 * RECIBE: nada. DEVUELVE: void. FUNCI�N: Este m�todo muestra al usuario un
	 * JDialog, de tipo DlgUpdateStatisticData, para que seleccione un curso, y
	 * el fichero de preguntas, de ese curso, que va a ser actualizado.
	 */

	private void jMenuItemUpdateStatisticDataActionPerformed() {
		courseVector = null;

		workerGetQuestionsFileNames = new GetQuestionsFileNames("");
		workerGetQuestionsFileNames.start();

		// Llamada a la clase para repintar la barra de progreso.
		workerRepaintProgressBar = new RepaintProgressBar();
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try {
			workerGetQuestionsFileNames.join();
			workerRepaintProgressBar.join();
		} catch (java.lang.InterruptedException ie) {}

		workerTimeControl.stop();

		if (taskDone == -1) {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerGetQuestionsFileNames.stop();
			workerRepaintProgressBar.stop();
			return;
		}

		// Si la respuesta del servidor es null, se muestra un mensaje de error.
		if (courseVector != null) {
			// Shows the dialog to update the statistical data
			new DlgUpdateStatisticData("Update Statistical Data", courseVector, this);
		} else {
			// Mensaje para indicar al usuario que se ha producido un error al
			// intentar
			// conseguir los nombres de los ficheros de test de los cursos.
			JOptionPane.showMessageDialog(this, "ERROR: There is not any question file to update.",
					"Update Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	// ////////////////// METHODS PROPIOS DE LA CLASE TESTEDITOR
	// ////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: saveQuestion. PERTENECE A: Clase TestEditor. LLAMADA POR: nada.
	 * LLAMA A: nada. RECIBE: question: Objeto de tipo Question. Contiene los
	 * datos de la pregunta a almacenar en el servidor. imageFile: Objeto de tipo
	 * File. Contiene la imagen a asociar a la pregunta. copyImage: Objeto de
	 * tipo boolean. Indica si la imagen que va a ser asociada a esta pregunta,
	 * proviene de mover una imagen ya contenida en el servidor. En este caso, el
	 * m�todo getPathImage() de la clase question, devolver� el path de la imagen
	 * origen. DEVUELVE: Objeto de tipo String. FUNCI�N: M�todo que env�a los
	 * datos necesarios al servidor para que almacene una pregunta en un fichero
	 * de preguntas. Este m�todo tambi�n recibe la respuesta del servidor a la
	 * peticion solicitada y la envi� de vuelta al m�todo que invoc� a este
	 * mismo.
	 */

	public String saveQuestion(Question question, File imageFile, boolean copyImage) {
		URL url = null; // Almacena la URL donde se enviar� la petici�n.
		String line = ""; // Almacena la respuesta a la petici�n.

		// Conexi�n con el servidor.
		try {
			// String host = codeBase.getHost();
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.SaveQuestion";

			url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			ObjectOutputStream bufferObjectOut = new ObjectOutputStream(connection.getOutputStream());

			// Envio al servidor de los datos necesarios para almacenar la
			// pregunta.

			// NO ALTERAR EL ORDEN!!!!!!!!!!!!!!!
			bufferObjectOut.writeObject(question);
			bufferObjectOut.writeObject(userLogin);

			if (imageFile != null) {
				// Transformaci�n del fichero de imagen en un array de bytes y envio
				// del mismo al servidor.
				byte[] data = new byte[(int) imageFile.length()];
				FileInputStream fileInputStream = new FileInputStream(imageFile);
				DataInputStream dataInputStream = new DataInputStream(fileInputStream);
				dataInputStream.read(data);

				bufferObjectOut.writeObject(imageFile.getName().substring(imageFile.getName().lastIndexOf("."))
						.toLowerCase());
				bufferObjectOut.writeInt((int) imageFile.length());
				bufferObjectOut.write(data);
			} else {
				byte[] data = new byte[0];
				bufferObjectOut.writeObject("");
				bufferObjectOut.writeInt(0);
				bufferObjectOut.write(data);
			}
			bufferObjectOut.writeBoolean(copyImage);

			bufferObjectOut.flush();

			// Recepci�n de la respuesta.
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String lineTemp = "";
			while ((lineTemp = bufferIn.readLine()) != null)
				line = line.concat(lineTemp) + "\n";

			// Cerrado de la comunicaci�n con el servidor.
			bufferObjectOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return line;

		} catch (Exception e) {
			taskDone = 1;
			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Result of saving the question",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	/**
	 * Sends the server the necessary data to modify a question from a question
	 * file.
	 * @param question Data of the question to update in the server
	 * @param imageFile Data of the image file to associate to the question
	 * @return Una cadena con el resultado de la modificacion
	 */
	public String modifyQuestion(final Question question, final File imageFile) {
		URL url = null;
		String line = "";
		try {
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.ModifyQuestion";
			url = new URL(codeBase, petition);
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send the data
			ObjectOutputStream bufferObjectOut = new ObjectOutputStream(connection.getOutputStream());
			// DON'T MODIFY THE ORDER
			bufferObjectOut.writeObject(question);

			if (imageFile != null) {
				// Puts the imagen file into a byte array and sends it to the server
				byte[] data = new byte[(int) imageFile.length()];
				FileInputStream fileInputStream = new FileInputStream(imageFile);
				DataInputStream dataInputStream = new DataInputStream(fileInputStream);
				dataInputStream.read(data);

				bufferObjectOut.writeObject(imageFile.getName().substring(imageFile.getName().lastIndexOf("."))
						.toLowerCase());
				bufferObjectOut.writeInt((int) imageFile.length());
				bufferObjectOut.write(data);
			}

			bufferObjectOut.flush();

			// Gets the response from the server
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lineTemp = "";
			while ((lineTemp = bufferIn.readLine()) != null) {
				line = line.concat(lineTemp) + "\n";
			}

			// Closes the socket with the server
			bufferObjectOut.close();
			bufferIn.close();

			// Returns the response from the server
			return line;

		} catch (Exception e) {
			taskDone = 1;
			// If any error occurred, then shows the error and returns null
			JOptionPane.showMessageDialog(this, e.getMessage(), "Result of modifying the question", 1);
			return null;
		}
	}

	/*
	 * NOMBRE: deleteQuestion. PERTENECE A: Clase TestEditor. LLAMADA POR:
	 * jButtonDelete. LLAMA A: nada. RECIBE: courseName: Objeto de tipo String.
	 * Contiene el nombre del curso sobre el que se est� actuando.
	 * questionsFileName: Objeto de tipo String. Contiene el nombre del fichero
	 * de preguntas del que se va a eliminar una pregunta. codeQuestion: Objeto
	 * de tipo String. Contiene el c�digo de la pregunta que se va a eliminar del
	 * fichero de preguntas. DEVUELVE: Objeto de tipo String. FUNCI�N: M�todo que
	 * env�a los datos necesarios al servidor para que elimine una pregunta del
	 * fichero de preguntas indicado. Este m�todo recibe la respuesta del
	 * servidor a la peticion solicitada y la envi� de vuelta al m�todo que
	 * invoc� a este mismo.
	 */

	public String deleteQuestion(String courseName, String questionsFileName, String codeQuestion) {
		// Construcci�n de la cadena de datos para enviar al servidor.
		StringBuffer query = new StringBuffer();
		query.append("codeQuestion=").append(codeQuestion);
		query.append("&courseName=").append(courseName);
		query.append("&questionsFileName=").append(questionsFileName);
		query.append("&authorName=").append(userLogin);

		// Conexi�n con el servidor.
		try {
			// String host = codeBase.getHost();
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.DeleteQuestion";
			URL url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos para eliminar la pregunta del fichero de
			// preguntas.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Recepci�n de la respuesta.
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lineTemp = "";
			String line = "";
			while ((lineTemp = bufferIn.readLine()) != null)
				line = line.concat(lineTemp) + "\n";

			// Cerrado de la comunicaci�n.
			bufferOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return line;
		} catch (Exception e) {
			taskDone = 1;
			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR);

			return null;
		}
	}

	/*
	 * NOMBRE: addQuestionToTest. PERTENECE A: Clase TestEditor. LLAMADA POR:
	 * jButtonAddQuestionToTest de la clase FrmEditQuestion. LLAMA A: nada.
	 * RECIBE: courseName: Objeto de tipo String. Contiene el nombre del curso al
	 * que pertenece el fichero de preguntas el que se est� actuando.
	 * questionsFileName: Objeto de tipo String. Contiene el nombre del fichero
	 * de preguntas del que se va a a�adir una pregunta a un fichero de test.
	 * codeQuestion: Objeto de tipo String. Contiene el c�digo de la pregunta que
	 * se va a a�adir a un test cl�sico. testFileName: Objeto de tipo String.
	 * Contiene el nombre del fichero de test cl�sico al que se va a a�adir la
	 * pregunta. executionType: Objeto de tipo String. Indica si se trata de un
	 * test cl�sico o adaptativo. Los valores que puede tomar son "classic"o
	 * "adaptive". DEVUELVE: Objeto de tipo String. FUNCI�N: M�todo que env�a los
	 * datos necesarios al servidor para que a�ada la pregunta que se encuentra
	 * cargada en la pantalla a un fichero de test cuyo nombre se pasa como
	 * par�metro.
	 */

	public String addQuestionToTest(
			final String courseName,
			final String questionsFileName,
			final String codeQuestion,
			final String testFileName,
			final String executionType) {
	//{
		// Builds the query string
		StringBuffer query = new StringBuffer();
		query.append("codeQuestion=").append(codeQuestion);
		query.append("&courseName=").append(courseName);
		query.append("&questionsFileName=").append(questionsFileName);
		query.append("&testFileName=").append(testFileName);
		query.append("&executionType=").append(executionType);

		// Connects with the server
		try {
			String petition = wowPath
					+ "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.AddQuestionToTest";

			URL url = new URL(codeBase, petition);
			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send the data
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Receives the response
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String lineTemp = "";
			String line = "";
			while ((lineTemp = bufferIn.readLine()) != null)
				line = line.concat(lineTemp) + "\n";

			// Closes the streams
			bufferOut.close();
			bufferIn.close();

			// Returns the response
			return line;

		} catch (Exception e) {
			taskDone = 1;

			// In error case, shows a error message and returns null
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	/*
	 * NOMBRE: closingWindow. PERTENECE A: Clase TestEditor. LLAMADA POR: Al
	 * cerrar el JFrame principal del applet. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Este m�todo comprueba que los datos contenidos en
	 * los frame internos, pertenecientes al JFrame principal del applet, han
	 * sido salvados antes de salir del programa. Para comprobar esto obtiene
	 * todos los frame que se est�n ejecutando actualmente e invoca a
	 * los m�todos closingWindow de cada uno de ellos.
	 */

	void closingWindow() {
		// boolean dataSaved = false; //Indica si se han salvado los datos
		// contenidos
		// en los frame internos que se est�n
		// ejecutando actualmente.

		// C�lculo del n�mero de frame internos que se est�n ejecutando
		// actualmente.
		int numberInternalFrames = jDesktopPane.getComponentCount();

		if (numberInternalFrames > 0) {
			// Obtenci�n de todos lo frame interno que se est�n ejecutando
			// actualmente.
			JInternalFrame[] frames = jDesktopPane.getAllFrames();

			// Bucle para la llamada a los m�todos closingWindow de cada uno de los
			// frame internos obtenidos anteriormente.
			Vector closeVector = new Vector();
			for (int i = 0; i < frames.length; i++) {
				try {
					FrmEditQuestion frmEditQuestion = (FrmEditQuestion) frames[i];
					if (frmEditQuestion.isVisible()) {
						int close = frmEditQuestion.closingWindow();

						if (close == JInternalFrame.DO_NOTHING_ON_CLOSE)
							closeVector.add(String.valueOf(DO_NOTHING_ON_CLOSE));
						else
							closeVector.add(String.valueOf(DISPOSE_ON_CLOSE));

						// Comprobaci�n.
						if (frmEditQuestion.questionSaved == false && frmEditQuestion.dataRight == false)
							closeVector.add(String.valueOf(DO_NOTHING_ON_CLOSE));
						else
							closeVector.add(String.valueOf(DISPOSE_ON_CLOSE));
					}
				} catch (java.lang.ClassCastException e1) {
					try {
						FrmCreateTest frmCreateTest = (FrmCreateTest) frames[i];
						if (frmCreateTest.isVisible()) {
							int close = frmCreateTest.closingWindow();

							if (close == JInternalFrame.DO_NOTHING_ON_CLOSE)
								closeVector.add(String.valueOf(DO_NOTHING_ON_CLOSE));
							else {
								if (close == JInternalFrame.DISPOSE_ON_CLOSE)
									closeVector.add(String.valueOf(DISPOSE_ON_CLOSE));
							}
						}
					} catch (java.lang.ClassCastException e2) {
						try {
							FrmEditTest frmEditTest = (FrmEditTest) frames[i];
							if (frmEditTest.isVisible()) {
								int close = frmEditTest.closingWindow();

								if (close == JInternalFrame.DO_NOTHING_ON_CLOSE)
									closeVector.add(String.valueOf(DO_NOTHING_ON_CLOSE));
								else {
									if (close == JInternalFrame.DISPOSE_ON_CLOSE)
										closeVector.add(String.valueOf(DISPOSE_ON_CLOSE));
								}
							}
						} catch (java.lang.ClassCastException ei) {}
					}
				}
			}

			if (closeVector.contains(String.valueOf(DO_NOTHING_ON_CLOSE)) == false)
				dispose();
			else
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		} else
			dispose();
	}

	/*
	 * NOMBRE: RepaintJprogressBar. PERTENECE A: Clase TestEditor. LLAMADA POR:
	 * Los mismos m�todos que llaman a la clase GetQuestionsFile. LLAMA A: nada.
	 * RECIBE: void. DEVUELVE: void. FUNCI�N: Este m�todo redibuja los
	 * JProgressBar para mostrar el progreso de una tarea.
	 */

	private void repaintProgressBar() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		jPanelStatus.setVisible(true);
		jLabelStatus.setVisible(true);
		jProgressBar.setVisible(true);
		jLabelStatus.paint(jLabelStatus.getGraphics());
		jProgressBar.paint(jProgressBar.getGraphics());
		jPanelStatus.paint(jPanelStatus.getGraphics());
		jLabelStatus.setText("Loading...                                        "
				+ "                                                  ");
		jProgressBar.setMinimum(0);
		jProgressBar.setMaximum(15000);
		jProgressBar.setValue(0);
		getContentPane().paint(getContentPane().getGraphics());
		jPanelStatus.paint(jPanelStatus.getGraphics());

		taskDone = 0;

		int cont = 0;
		while (taskDone == 0) {
			if (taskDone != 0)
				break;

			cont = cont + 100;
			jLabelStatus.setText("Loading...                                       "
					+ "                                                 ");
			jPanelStatus.paint(jPanelStatus.getGraphics());
			jProgressBar.setValue(cont);
			jPanelStatus.paint(jPanelStatus.getGraphics());
			paint(getGraphics());
			try {
				Thread.sleep(100);
			} catch (java.lang.InterruptedException e) {}
		}

		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		if (taskDone != -1)
			jLabelStatus.setText("Done                                             ");
		else
			jLabelStatus.setText("Overcome time of wait. Attempt it later.");

		jPanelStatus.paint(jPanelStatus.getGraphics());
		jProgressBar.setValue(jProgressBar.getMaximum());
		jPanelStatus.paint(jPanelStatus.getGraphics());
		jProgressBar.setValue(0);
		getContentPane().paint(getContentPane().getGraphics());
	}

	/*
	 * NOMBRE: createQuestionsFile. PERTENECE A: Clase TestEditor. LLAMADA POR:
	 * Al hacer click sobre jMenuItemNewQuestionFile. LLAMA A: nada. RECIBE:
	 * courseName: Objeto de tipo String. Contiene el nombre del curso sobre el
	 * que se est� actuando. concept: Objeto de tipo String. Contiene el nombre
	 * del concepto al que se va a asociar el fichero de preguntas.
	 * questionsFileName: Objeto de tipo String. Contiene el nombre del fichero
	 * de preguntas en el que se va a almacenar la pregunta. numAnswers: Objeto
	 * de tipo int. Almacena el n�mero de respuestas por defecto que contendr�n
	 * las respuestas del fichero que se va a crear. replace: Objeto de tipo
	 * boolean. Indica si se debe reemplazar o no el fichero con el mismo nombre
	 * que el que se va a crear, en caso de que este exista. DEVUELVE: Objeto de
	 * tipo String. FUNCI�N: M�todo que env�a los datos necesarios al servidor
	 * para que este cree un nuevo fichero de preguntas vacio. Adem�s recibe la
	 * respuesta del servidor a la petici�n y la devuelve al m�todo que invoc� a
	 * este mismo.
	 */

	public String createQuestionsFile(
			final String courseName,
			final String concept,
			final String questionsFileName,
			final int numAnswers,
			final boolean replace,
			final String authorName) {
	//{
		// Builds the query string
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&concept=").append(concept);
		query.append("&questionsFileName=").append(questionsFileName);
		query.append("&numAnswers=").append(String.valueOf(numAnswers));
		query.append("&replace=").append(String.valueOf(replace));
		query.append("&authorName=").append(String.valueOf(authorName));

		// Connects with the server
		try {
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets."
					+ "CreateQuestionsFile";

			URL url = new URL(codeBase, petition);
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Sends the data
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Recepci�n de la respuesta.
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lineTemp = "";
			String line = "";
			while ((lineTemp = bufferIn.readLine()) != null) {
				line = line.concat(lineTemp) + "\n";
			}

			// Closes the streams
			bufferOut.close();
			bufferIn.close();

			// Returns the response
			return line;

		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	/*
	 * NOMBRE: getQuestionsFilesList. PERTENECE A: Clase TestEditor. LLAMADA POR:
	 * Al hacer click sobre jMenuItemAddQuestionToFile, jMenuItemDeleteQuestion,
	 * jMenuItemExamineQuestionFile, jMenuItemExportQuestionFile,
	 * jMenuItemModifyQuestionFile. LLAMA A: nada. RECIBE: courseName: Objeto de
	 * tipo String. Contiene la lista de nombres de los cursos de los que el
	 * autor es propietario. DEVUELVE: Objeto de tipo Vector. FUNCI�N: M�todo que
	 * env�a al servidor la informaci�n necesaria para que este devuelva una
	 * lista con los nombres de los ficheros de preguntas de todos los cursos que
	 * pertenecen al usuario.
	 */

	public Vector getQuestionsFileNames(String courseName) {
		// Builds the query string
		StringBuffer query = new StringBuffer();
		if (courseName == null || courseName.equals("")) {
			query.append("userLogin=").append(userLogin);
			query.append("&userPassword=").append(userPassword);
		} else {
			query.append("userLogin=").append(userLogin);
			query.append("&userPassword=").append(userPassword);
			query.append("&courseName=").append(courseName);
		}

		// Conexi�n con el servidor.
		try {
			// String host = codeBase.getHost();
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets."
					+ "GetQuestionsFileNames";

			URL url = new URL(codeBase, petition);
			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos al servidor.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Recepci�n de la respuesta del servidor.
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());
			Vector courseVector = (Vector) bufferIn.readObject();

			// Cerrado de la comunicaci�n.
			bufferOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return courseVector;

		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	/*
	 * NOMBRE: getQuestionsFileNameAndConcept. PERTENECE A: Clase TestEditor.
	 * LLAMADA POR: nada. LLAMA A: nada. RECIBE: course: Objeto de tipo Course.
	 * Contiene el nombre de un curso y los nombres de ficheros de conceptos de
	 * ese curso. DEVUELVE: Objeto de tipo Course. FUNCI�N: M�todo que env�a al
	 * servidor la informaci�n necesaria para que este devuelva una lista con los
	 * nombres de los ficheros de preguntas de todos los cursos que pertenecen al
	 * usuario.
	 */

	public Course getQuestionsFileAndConceptsNames(Course course) {
		Course courseReturn = null; // Almacenar� objetos tipo Course
		// con los nombres de los ficheros
		// de preguntas.
		URL url = null; // Alamacena la URL del servidor.
		// String consult = ""; //Almacenar� la cadena de petici�n al servidor.

		// Conexi�n con el servidor.
		try {
			// String host = codeBase.getHost();
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets."
					+ "GetQuestionsFileAndConceptsNames";
			url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos para modificar la pregunta del fichero de
			// preguntas.
			ObjectOutputStream bufferObjectOut = new ObjectOutputStream(connection.getOutputStream());

			bufferObjectOut.writeObject(course);
			bufferObjectOut.flush();

			// Recepci�n de la respuesta del servidor.
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());
			courseReturn = (Course) bufferIn.readObject();

			// Cerrado de la comunicaci�n.
			bufferObjectOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return courseReturn;
		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	/*
	 * NOMBRE: getQuestionsFileNamesOld. PERTENECE A: Clase TestEditor. LLAMADA
	 * POR: Al hacer click sobre jMenuItemImportQuestionFile. LLAMA A: nada.
	 * RECIBE: testOldPath: Objeto de String. Contiene el path relativo de los
	 * ficheros de preguntas de la versi�n anterior. DEVUELVE: Objetos Vector.
	 * FUNCI�N: M�todo que env�a al servidor la informaci�n necesaria para que
	 * este devuelva una lista con los nombres de los ficheros de preguntas, que
	 * pertenecen a las versiones anteriores de WOW.
	 */

	public Vector getQuestionsFileNamesOld(String testOldPath) {
		Vector questionsFileNameOldVector = new Vector();
		URL url = null; // Alamacena la URL del servidor.

		// Construcci�n de la cadena de datos para enviar al servidor.
		String consult = "testOldPath=" + testOldPath;

		// Conexi�n con el servidor.
		try {
			// String host = codeBase.getHost();
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets."
					+ "GetQuestionsFileNamesOld";

			url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos al servidor.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(consult);
			bufferOut.flush();

			// Recepci�n de la respuesta del servidor.
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());
			questionsFileNameOldVector = (Vector) bufferIn.readObject();

			// Cerrado de la comunicaci�n.
			bufferOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return questionsFileNameOldVector;
		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	/*
	 * NOMBRE: getQuestionByOrder. PERTENECE A: Clase TestEditor. LLAMADA POR:
	 * jButtonGoto, jButtonPrevious, jButtonNext, jButtonDelete, de la clase
	 * FrmEditQuestion y por el constructor de esta misma clase cuando este
	 * frame es inicializado para modificar o eliminar preguntas de un
	 * fichero. LLAMA A: nada. RECIBE: courseName: Objeto de tipo String.
	 * Contiene el nombre del curso sobre el que se est� actuando.
	 * questionsFileName: Objeto de tipo String. Contiene el nombre del fichero
	 * de preguntas del que se va a eliminar una pregunta. numQuestion: Objeto de
	 * tipo int. Contiene el n�mero que ocupa la pregunta que se va a mostrar
	 * dentro del fichero de preguntas. DEVUELVE: Objeto de tipo Question.
	 * FUNCI�N: M�todo que env�a los datos necesarios al servidor para que
	 * localize una pregunta por su orden dentro del fichero de preguntas
	 * indicado. Una vez localizada dicha pregunta, el servidor manda los datos
	 * de la misma, almacenados en un objeto de tipo Question de vuelta a este
	 * m�todo. Si ocurre alguna excepci�n mientras se realiza la conexi�n con el
	 * servidor, busqueda de la pregunta deseada, o devoluci�n de los datos de la
	 * misma este m�todo devoler� null al m�todo que lo invoco.
	 */

	public Question getQuestionByOrder(String courseName, String questionsFileName, int numQuestion) {
		// Construcci�n de la cadena de datos para enviar al servidor.
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&questionsFileName=").append(questionsFileName);
		query.append("&numQuestion=").append(numQuestion);

		// Conexi�n con el servidor.
		try {
			// String host = codeBase.getHost();
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets."
					+ "GetQuestionByOrder";
			URL url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos para localizar la pregunta del fichero de
			// preguntas.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Recepci�n de la respuesta.
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());

			Question question = (Question) bufferIn.readObject();

			// Cerrado de la comunicaci�n.
			bufferOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return question;
		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	/*
	 * NOMBRE: getQuestionByCode. PERTENECE A: Clase TestEditor. LLAMADA POR:
	 * jButtonGotoCode de la clase FrmEditQuestion y al hacer click sobre
	 * jButtonModifyDelete de la clase FrmExamineQuestionsFile. LLAMA A: nada.
	 * RECIBE: courseName: Objeto de tipo String. Contiene el nombre del curso
	 * sobre el que se est� actuando. questionsFileName: Objeto de tipo String.
	 * Contiene el nombre del fichero de preguntas del que se va a eliminar una
	 * pregunta. codeQuestion: Objeto de tipo int. Contiene el c�digo de la
	 * pregunta que se va a mostrar. DEVUELVE: Objeto de tipo Question. FUNCI�N:
	 * M�todo que env�a los datos necesarios al servidor para que localize una
	 * pregunta por su c�digo dentro del fichero de preguntas indicado. Una vez
	 * localizada dicha pregunta, el servidor manda los datos de la misma,
	 * almacenados en un objeto de tipo Question de vuelta a este m�todo. Si
	 * ocurre alguna excepci�n mientras se realiza la conexi�n con el servidor,
	 * busqueda de la pregunta deseada, o devoluci�n de los datos de la misma
	 * este m�todo devoler� null al m�todo que lo invoco.
	 */

	public Question getQuestionByCode(String courseName, String questionsFileName, String codeQuestion) {
		// Construcci�n de la cadena de datos para enviar al servidor.
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&questionsFileName=").append(questionsFileName);
		query.append("&codeQuestion=").append(codeQuestion);

		// Conexi�n con el servidor.
		try {
			// String host = codeBase.getHost();
			String petition = wowPath
					+ "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.GetQuestionByCode";
			URL url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos para localizar la pregunta del fichero de
			// preguntas.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Recepci�n de la respuesta.
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());

			Question question = (Question) bufferIn.readObject();

			// Cerrado de la comunicaci�n.
			bufferOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return question;
		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	/*
	 * NOMBRE: getQuestions. PERTENECE A: Clase TestEditor. LLAMADA POR: El
	 * m�todo Step3Init() de la clase FrmCreateClassicTest. LLAMA A: nada.
	 * RECIBE: courseName: Objeto de tipo String. Contiene el nombre del curso
	 * sobre el que se est� actuando. questionsFileName: Objeto de tipo String.
	 * Contiene el nombre del fichero de preguntas del que se van a obtener todos
	 * los datos de todas las preguntas. DEVUELVE: Objeto tipo Vector. Cada
	 * posici�n del Vector hay un Question que contiene a su vez los datos de una
	 * de las preguntas del fichero de preguntas. FUNCI�N: M�todo que invoca al
	 * servlet GetQuestions, contenido en el servidor, para obtener los datos de
	 * todas las preguntas contenidas en el fichero de preguntas cuyo nombre se
	 * ha pasado como par�metro a esta clase.
	 */

	public Vector getQuestions(String courseName, String questionsFileName) {
		// Builds the query string
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&questionsFileName=").append(questionsFileName);

		// Conexi�n con el servidor.
		try {
			// String host = codeBase.getHost();
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.GetQuestions";
			URL url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de la petici�n.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Obtenci�n de la respuestas del servidor.
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());
			Vector questionsVector = (Vector) bufferIn.readObject();

			bufferOut.close();
			bufferIn.close();
			
			return questionsVector;

		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n al conectar en el servidor se
			// muestra un mensaje de error.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	/*
	 * NOMBRE: getRandomQuestions. PERTENECE A: Clase TestEditor. LLAMADA POR: El
	 * m�todo Step3Init() de la clase FrmCreateClassicTest. LLAMA A: nada.
	 * RECIBE: courseName: Objeto de tipo String. Contiene el nombre del curso
	 * sobre el que se est� actuando. questionsFileName: Objeto de tipo String.
	 * Contiene el nombre del fichero de preguntas del que se van a obtener todos
	 * los datos de las preguntas. numQuestionsToGet: Objeto de tipo int.
	 * Contiene el n�mero de preguntas que se han solicitado. DEVUELVE: Objeto
	 * tipo Vector. Cada posici�n del Vector hay un Question que contiene a su
	 * vez los datos de una de las preguntas del fichero de preguntas. FUNCI�N:
	 * M�todo que invoca al servlet GetRandomQuestions, contenido en el servidor,
	 * para obtener los datos de las preguntas contenidas en el fichero de
	 * preguntas cuyo nombre se ha pasado como par�metro a esta clase.
	 */

	public Vector getRandomQuestions(String courseName, String questionsFileName, int numQuestionsToGet) {
		// Builds the query string
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&questionsFileName=").append(questionsFileName);
		query.append("&numQuestionsToGet=").append(numQuestionsToGet);

		// Conexi�n con el servidor.
		try {
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.GetRandomQuestions";

			URL url = new URL(codeBase, petition);
			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de la petici�n.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Obtenci�n de la respuestas del servidor.
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());
			Vector questionsVector = (Vector) bufferIn.readObject();

			bufferOut.close();
			bufferIn.close();
			// Devoluci�n de los datos enviados por el servidor.
			return questionsVector;

		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n al conectar en el servidor se
			// muestra un mensaje de error.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	/*
	 * NOMBRE: getRandomRestrictionsQuestions. PERTENECE A: Clase TestEditor.
	 * LLAMADA POR: El m�todo Step3Init() de la clase FrmCreateClassicTest. LLAMA
	 * A: nada. RECIBE: courseName: Objeto de tipo String. Contiene el nombre del
	 * curso sobre el que se est� actuando. questionsFileName: Objeto de tipo
	 * String. Contiene el nombre del fichero de preguntas del que se van a
	 * obtener todos los datos de las preguntas para poder configurar el JTree.
	 * numQuestionsToGet: Objeto de tipo int. Contiene el n�mero de preguntas que
	 * se han solicitado. question: Objeto de tipo Question. Contiene los datos
	 * con los que se realizar� la busqueda de preguntas. DEVUELVE: Objeto de
	 * tipo Vector. Cada posici�n del Vector que contiene a su vez los datos de
	 * una de las preguntas del fichero de preguntas. FUNCI�N: M�todo que invoca
	 * al servlet GetRandomRestrictionssQuestions, contenido en el servidor, para
	 * obtener los datos de las preguntas contenidas en el fichero de preguntas
	 * cuyo nombre se ha pasado como par�metro a esta clase.
	 */

	public Vector getRandomRestrictionsQuestions(String courseName, String questionsFileName,
			int numQuestionsToGet, Question question) {
		Vector questionsVector; // Almacena objetos Question con los datos
		// de todas las preguntas del fichero de preguntas
		// que se quiere examinar.
		URL url = null; // Almacenar� la URL del servidor que contiene el
		// servlet que
		// se va a invocar.

		// Conexi�n con el servidor.
		try {
			// String host = codeBase.getHost();
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.GetRandomRestrictionsQuestions";
			url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de la petici�n.
			ObjectOutputStream bufferObjectOut = new ObjectOutputStream(connection.getOutputStream());

			// Envio al servidor de los datos necesarios para almacenar la
			// pregunta.
			// NO ALTERAR EL ORDEN!!!!!!!!!!!!!!!
			bufferObjectOut.writeObject(courseName);
			bufferObjectOut.writeObject(questionsFileName);
			bufferObjectOut.writeInt(numQuestionsToGet);
			bufferObjectOut.writeObject(question);
			bufferObjectOut.flush();

			// Obtenci�n de la respuestas del servidor.
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());
			questionsVector = (Vector) bufferIn.readObject();

			bufferObjectOut.close();
			bufferIn.close();

			// Devoluci�n de los datos enviados por el servidor.
			return questionsVector;
		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n al conectar en el servidor se
			// muestra un mensaje de error.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	/*
	 * NOMBRE: findQuestions. PERTENECE A: Clase TestEditor. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * question: Objeto de tipo Question. Contiene los datos introducidos para
	 * realizar la busqueda. DEVUELVE: Objeto Vector. Cada posici�n del Vector
	 * contiene a su vez un objeto Question con los datos de una de las preguntas
	 * del fichero de preguntas. Hay tanto objetos Question como preguntas se han
	 * encontrado en el fichero de preguntas que coinciden con el patr�n de
	 * busqueda. FUNCI�N: M�todo que invoca al servlet FindQuestions, contenido
	 * en el servidor, para obtener los datos de las preguntas buscadas
	 * contenidas en el fichero de preguntas cuyo nombre se ha pasado como
	 * par�metro a esta clase.
	 */

	public Vector findQuestions(Question question) {
		Vector questionsVector; // Almacena objetos de tipo Question que contienen
		// los datos de las preguntas buscadas en el fichero
		// de preguntas.
		URL url = null; // Almacenar� la URL del servidor que contiene el
		// servlet que
		// se va a invocar.

		// Conexi�n con el servidor.
		try {
			// String host = codeBase.getHost();
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.FindQuestions";
			url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de la petici�n.
			ObjectOutputStream bufferObjectOut = new ObjectOutputStream(connection.getOutputStream());

			// Envio al servidor de los datos necesarios para almacenar la
			// pregunta.
			// NO ALTERAR EL ORDEN!!!!!!!!!!!!!!!
			bufferObjectOut.writeObject(question);
			bufferObjectOut.flush();

			// Obtenci�n de la respuestas del servidor.
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());
			questionsVector = (Vector) bufferIn.readObject();

			// Cerrado de la comunicaci�n
			bufferObjectOut.close();
			bufferIn.close();

			// Devoluci�n de los datos enviados por el servidor.
			return questionsVector;
		} catch (Exception e) {
			taskDone = 1;
			// En caso de producirse alguna excepci�n al conectar en el servidor se
			// muestra un mensaje de error.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}


	/**
	 * Calls the server to ask for the name of the test file of the courses.
	 * Returns null if any error occurs.
	 * @param theCourseName Name of the course. Optional parameter.
	 * @param testType Type of test (CLASSIC or ADAPTIVE)
	 * @return A vector with the names of the tests files
	 */
	public Vector getTestsFileNames(final String theCourseName, final String testType) {
		// Builds the data string to make the request to the server
		StringBuffer consult = new StringBuffer();
		if (theCourseName == null || theCourseName.trim().equals("")) {
			consult.append("userLogin=").append(userLogin);
			consult.append("&userPassword=").append(userPassword);
			consult.append("&testType=").append(testType);
		} else {
			consult.append("userLogin=").append(userLogin);
			consult.append("&userPassword=").append(userPassword);
			consult.append("&testType=").append(testType);
			consult.append("&courseName=").append(theCourseName);
		}	

		// Connect to the server
		try {
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.GetTestsFileNames";
			URL url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Makes the request and sends data
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(consult.toString());
			bufferOut.flush();

			// Receives the response
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());
			Vector list = (Vector) bufferIn.readObject();

			// Closes the streams
			bufferOut.close();
			bufferIn.close();

			// Returns the response
			return list;

		} catch (Exception e) {
			taskDone = 1;

			// Shows a error message
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting with the server",
					JOptionPane.ERROR_MESSAGE);

			// Returns null
			return null;
		}
	}

	/*
	 * NOMBRE: getTest. PERTENECE A: Clase TestEditor. LLAMADA POR: nada. LLAMA
	 * A: nada. RECIBE: courseName: Objeto de tipo String. Contiene el nombre del
	 * curso sobre el que se est� actuando. testFileName: Objeto de tipo String.
	 * Contiene el nombre del fichero de test. DEVUELVE: Objeto de tipo Test.
	 * FUNCI�N: M�todo que env�a los datos necesarios al servidor para que
	 * localize una fichero de test cl�sico. Una vez localizado dicha test, el
	 * servidor manda los datos del mismo, almacenados en un objeto de tipo Test
	 * de vuelta a este m�todo. Si ocurre alguna excepci�n mientras se realiza la
	 * conexi�n con el servidor, busqueda de la pregunta deseada, o devoluci�n de
	 * los datos de la misma este m�todo devoler� null al m�todo que lo invoco.
	 */

	public Test getTest(String courseName, String testFileName) {
		// Builds the query string
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&testFileName=").append(testFileName);

		// Conexi�n con el servidor.
		try {
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.GetTest";
			URL url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos para localizar el fichero de test.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Recepci�n de la respuesta.
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());

			Test test = (Test) bufferIn.readObject();

			// Cerrado de la comunicaci�n.
			bufferOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return test;
		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	/*
	 * NOMBRE: getChart. PERTENECE A: Clase TestEditor. LLAMADA POR: nada. LLAMA
	 * A: nada. RECIBE: courseName: Objeto de tipo String. Indica el nombre del
	 * curso. login: Objeto de tipo String. Indica el login del alumno y por lo
	 * tanto el nombre del fichero de log del estudiante. testFileName: Objeto de
	 * tipo String. Indica el nombre del test. executionType: Objeto de tipo
	 * String. Indica el tipo de ejecuci�n del test, puede tomar dos valores,
	 * "classic" o "adaptive". width: Objeto de tipo String. Indica la altura de
	 * la gr�fica. height: Objeto de tipo int. Indica la altura de la grafica.
	 * ended: Objeto de tipo boolean. Indica si el test se ha finalizado o no.
	 * chartClient: Objeto de tipo String. Indica el tipo de gr�fica que se
	 * quiere obtener, en este caso puede tomar dos valores, "score" o
	 * "proficiency". DEVUELVE: Array de objetos byte. FUNCI�N: M�todo que env�a
	 * los datos necesarios al servidor para que localize, el fichero de log del
	 * estudiante Una vez localizado dicho fichero, el servidor realizar� los
	 * c�lculos necesarios para generar una gr�fica sobre la ejecuci�n actual del
	 * test. Esta gr�fica ser� devuelta en forma de una imagen, contenida en un
	 * array de byte.
	 */

	public byte[] getChart(
			final String courseName,
			final String login,
			final String testFileName,
			final String executionType,
			final int width,
			final int height,
			final boolean ended,
			final String chartClient) {
	//{
		// Builds the query string
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&testFileName=").append(testFileName);
		query.append("&login=").append(login);
		query.append("&executionType=").append(executionType);
		query.append("&width=").append(width);
		query.append("&height=").append(height);
		query.append("&ended=").append(ended);
		query.append("&chartClient=").append(chartClient);

		// Conexi�n con el servidor.
		try {
			// String host = codeBase.getHost();
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.GetChart";

			URL url = new URL(codeBase, petition);
			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos para localizar el fichero de test.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Recepci�n de la respuesta.
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());

			// Lectura de los datos enviados por el servidor.
			byte [] chart = (byte[]) bufferIn.readObject();

			// Cerrado de la comunicaci�n.
			bufferOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return chart;
		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	/*
	 * NOMBRE: getStudentAndFinishTest. PERTENECE A: Clase TestEditor. LLAMADA
	 * POR: nada. LLAMA A: nada. RECIBE: courseName: Objeto de tipo String.
	 * Contiene el nombre del curso. testFileName: Objeto de tipo String.
	 * Contiene el nombre del fichero de test. login: Objeto de tipo String.
	 * Contiene el login del alumno. DEVUELVE: Objeto de tipo Student. FUNCI�N:
	 * M�todo que env�a los datos necesarios al servidor para que localize, a un
	 * estudiante. Una vez localizado el fichero log de dicho estudiante, el
	 * servidor manda los datos del mismo, almacenados en un objeto de tipo
	 * Student de vuelta a este m�todo. Si ocurre alguna excepci�n mientras se
	 * realiza la conexi�n con el servidor, busqueda del fichero de estudiante, o
	 * devoluci�n de los datos de la misma este m�todo devoler� null al m�todo
	 * que lo invoco.
	 */

	public Student getStudentAndFinishTest(String courseName, String login, String testFileName) {
		// Builds the query string
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&testFileName=").append(testFileName);
		query.append("&login=").append(login);

		// Conexi�n con el servidor.
		try {
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.GetStudentAndFinishTest";

			URL url = new URL(codeBase, petition);
			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos para localizar el fichero de test.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Recepci�n de la respuesta.
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());

			Student student = (Student) bufferIn.readObject();

			// Cerrado de la comunicaci�n.
			bufferOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return student;
		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	/*
	 * NOMBRE: deleteQuestionsFile. PERTENECE A: Clase TestEditor. LLAMADA POR:
	 * Al hacer click sobre jMenuItemDeleteQuestionFile. LLAMA A: nada. RECIBE:
	 * courseName: Objeto de tipo String. Contiene el nombre del curso sobre el
	 * que se est� actuando. questionsFileName: Objeto de tipo String. Contiene
	 * el nombre del fichero de preguntas que va a ser eliminado. DEVUELVE:
	 * Objeto de tipo String. FUNCI�N: M�todo que env�a al servidor la
	 * informaci�n necesaria para que este elimin� un fichero de preguntas del
	 * sistema.
	 */

	public String deleteQuestionsFile(String courseName, String questionsFileName) {
		// Builds the query string
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&questionsFileName=").append(questionsFileName);
		query.append("&authorName=").append(userLogin);

		// Conexi�n con el servidor.
		try {
			// String host = codeBase.getHost();
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.DeleteQuestionsFile";

			URL url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos al servidor.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Recepci�n de la respuesta del servidor.
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lineTemp = "";
			String line = "";
			while ((lineTemp = bufferIn.readLine()) != null)
				line = line.concat(lineTemp) + "\n";

			// Cerrado de la comunicaci�n.
			bufferOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return line;
		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Delete Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	/*
	 * NOMBRE: deleteTestFile. PERTENECE A: Clase TestEditor. LLAMADA POR: Al
	 * hacer click sobre jMenuItemDeleteClassicTestFile. LLAMA A: nada. RECIBE:
	 * courseName: Objeto de tipo String. Contiene el nombre del curso sobre el
	 * que se est� actuando. testFileName: Objeto de tipo String. Contiene el
	 * nombre del fichero de test que va a ser eliminado. DEVUELVE: Objeto de
	 * tipo String. FUNCI�N: M�todo que env�a al servidor la informaci�n
	 * necesaria para que este elimin� un fichero de test del sistema.
	 */

	public String deleteTestFile(String courseName, String testFileName) {
		// Builds the query string
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&testFileName=").append(testFileName);
		query.append("&authorName=").append(userLogin);

		// Conexi�n con el servidor.
		try {
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.DeleteTestFile";

			URL url = new URL(codeBase, petition);
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos al servidor.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Recepci�n de la respuesta del servidor.
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lineTemp = "";
			String line = "";
			while ((lineTemp = bufferIn.readLine()) != null)
				line = line.concat(lineTemp) + "\n";

			// Cerrado de la comunicaci�n.
			bufferOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return line;

		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	/*
	 * NOMBRE: exportQuestionsFile. PERTENECE A: Clase TestEditor. LLAMADA POR:
	 * Al hacer click sobre jMenuItemExportQuestionFile. LLAMA A: nada. RECIBE:
	 * courseName: Objeto de tipo String. Contiene el nombre del curso sobre el
	 * que se est� actuando. questionsFileName: Objeto de tipo String. Contiene
	 * el nombre del fichero de preguntas que va a ser exportado. testOldPath:
	 * Objeto de tipo String. Contiene el path relativo de destino del fichero
	 * resultado de la exportaci�n. replace: Objeto de tipo boolean. Indica si se
	 * va a reemplazar alg�n fichero de preguntas existente por el que se va a
	 * exportar. DEVUELVE: Objeto de tipo String. FUNCI�N: M�todo que env�a al
	 * servidor la informaci�n necesaria para que este exporte un fichero de
	 * preguntas del sistema, perteneciente a la versi�n actual de WOW, a un
	 * fichero de preguntas perteneciente a las versiones anteriores de WOW.
	 */

	public String exportWOWQuestionsFile(String courseName, String questionsFileName, String testOldPath, boolean replace) {
		// Builds the query string
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&questionsFileName=").append(questionsFileName);
		query.append("&testOldPath=").append(testOldPath);
		query.append("&replace=").append(replace);

		// Conexi�n con el servidor.
		try {
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.ExportWOWQuestionsFile";

			URL url = new URL(codeBase, petition);
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos al servidor.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Recepci�n de la respuesta del servidor.
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lineTemp = "";
			String line = "";
			while ((lineTemp = bufferIn.readLine()) != null)
				line = line.concat(lineTemp) + "\n";

			// Cerrado de la comunicaci�n.
			bufferOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return line;
		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	/*
	 * NOMBRE: exportQuestionsFile. PERTENECE A: Clase TestEditor. LLAMADA POR:
	 * Al hacer click sobre jMenuItemExportQuestionFile. LLAMA A: nada. RECIBE:
	 * courseName: Objeto de tipo String. Contiene el nombre del curso sobre el
	 * que se est� actuando. questionsFileName: Objeto de tipo String. Contiene
	 * el nombre del fichero de preguntas que va a ser exportado. testOldPath:
	 * Objeto de tipo String. Contiene el path relativo de destino del fichero
	 * resultado de la exportaci�n. replace: Objeto de tipo boolean. Indica si se
	 * va a reemplazar alg�n fichero de preguntas existente por el que se va a
	 * exportar. DEVUELVE: Objeto de tipo String. FUNCI�N: M�todo que env�a al
	 * servidor la informaci�n necesaria para que este exporte un fichero de
	 * preguntas del sistema, perteneciente a la versi�n actual de WOW, a un
	 * fichero de preguntas perteneciente a las versiones anteriores de WOW.
	 */

	public String exportQuestionsFile(String aCourseName, String aQuestionsFileName, String newQuestionsFileName, short theFormat) {
		// Builds the query string
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&questionsFileName=").append(questionsFileName);
		query.append("&newFileName=").append(newQuestionsFileName);
		query.append("&theFormat=").append(theFormat);

		// Conexi�n con el servidor.
		try {
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.ExportQuestionsFile";

			URL url = new URL(codeBase, petition);
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos al servidor.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Recepci�n de la respuesta del servidor.
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lineTemp = "";
			String line = "";
			while ((lineTemp = bufferIn.readLine()) != null)
				line = line.concat(lineTemp);

			// Cerrado de la comunicaci�n.
			bufferOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return line;

		} catch (Exception e) {
			taskDone = 1;
			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}
	
	
	/*
	 * NOMBRE: importQuestionsFile. PERTENECE A: Clase TestEditor. LLAMADA POR:
	 * Al hacer click sobre jMenuItemImportQuestionFile. LLAMA A: nada. RECIBE:
	 * courseName: Objeto de tipo String. Contiene el nombre del curso sobre el
	 * que se est� actuando. testOldPath: Objeto de tipo String. Contiene el path
	 * relativo fichero de preguntas que va a ser importado.
	 * newQuestionsFileName: Objeto de tipo String. Contiene el nuevo nombre que
	 * va a recibir el fichero de preguntas, al ser importado. replace: Objeto de
	 * tipo boolean. Indica si se va a reemplazar alg�n fichero de preguntas
	 * existente por el que se va a importar. DEVUELVE: Objeto de tipo String.
	 * FUNCI�N: M�todo que env�a al servidor la informaci�n necesaria para que
	 * este importe un fichero de preguntas del sistema, perteneciente a las
	 * versiones anteriores de WOW, a un fichero de preguntas perteneciente a la
	 * versi�n actual de WOW.
	 */

	public String importWOWQuestionsFile(String courseName, String concept, String testOldPath,
			String newQuestionsFileName, boolean replace, String authorName) {
		// Construcci�n de la cadena de datos para enviar al servidor.
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&concept=").append(concept);
		query.append("&testOldPath=").append(testOldPath);
		query.append("&newQuestionsFileName=").append(newQuestionsFileName);
		query.append("&replace=").append(replace);
		query.append("&authorName=").append(authorName);
		

		// Conexi�n con el servidor.
		try {
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.ImportWOWQuestionsFile";

			URL url = new URL(codeBase, petition);
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos al servidor.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Recepci�n de la respuesta del servidor.
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lineTemp = "";
			String line = "";
			while ((lineTemp = bufferIn.readLine()) != null)
				line = line.concat(lineTemp) + "\n";

			// Cerrado de la comunicaci�n.
			bufferOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return line;
		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	
	/*
	 * NOMBRE: importQuestionsFile. PERTENECE A: Clase TestEditor. LLAMADA POR:
	 * Al hacer click sobre jMenuItemImportQuestionFile. LLAMA A: nada. RECIBE:
	 * courseName: Objeto de tipo String. Contiene el nombre del curso sobre el
	 * que se est� actuando. testOldPath: Objeto de tipo String. Contiene el path
	 * relativo fichero de preguntas que va a ser importado.
	 * newQuestionsFileName: Objeto de tipo String. Contiene el nuevo nombre que
	 * va a recibir el fichero de preguntas, al ser importado. replace: Objeto de
	 * tipo boolean. Indica si se va a reemplazar alg�n fichero de preguntas
	 * existente por el que se va a importar. DEVUELVE: Objeto de tipo String.
	 * FUNCI�N: M�todo que env�a al servidor la informaci�n necesaria para que
	 * este importe un fichero de preguntas del sistema, perteneciente a las
	 * versiones anteriores de WOW, a un fichero de preguntas perteneciente a la
	 * versi�n actual de WOW.
	 */
	public String importQuestionsFile(String aCourseName, String concept, String testOldPath,
			String newQuestionsFileName, boolean replace, String authorName, short theFormat) {
		// Construcci�n de la cadena de datos para enviar al servidor.
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&concept=").append(concept);
		query.append("&testOldPath=").append(testOldPath);
		query.append("&newQuestionsFileName=").append(newQuestionsFileName);
		query.append("&replace=").append(replace);
		query.append("&authorName=").append(authorName);
		query.append("&theFormat=").append(theFormat);

		// Conexi�n con el servidor.
		try {
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.ImportQuestionsFile";
			URL url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Envio de los datos al servidor.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Recepci�n de la respuesta del servidor.
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lineTemp = "";
			String line = "";
			while ((lineTemp = bufferIn.readLine()) != null)
				line = line.concat(lineTemp) + "\n";

			// Cerrado de la comunicaci�n.
			bufferOut.close();
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return line;

		} catch (Exception e) {
			taskDone = 1;
			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);
			
			return null;
		}
	}
	
	
	/*
	 * NOMBRE: createTestFile. PERTENECE A: Clase TestEditor. LLAMADA POR: Al
	 * hacer click sobre jButtonNext configurado en el Step4A() de la clase
	 * CreateClassicTest. LLAMA A: nada. RECIBE: test: Objeto de tipo Test.
	 * Contiene los datos necesarios para crear el fichero de test. replace:
	 * Objeto de tipo boolean. Indica si se reemplazar� o no un fichero de test
	 * que exista con el mismo nombre del que se va a crear. checkFinishStudents:
	 * Objeto de tipo boolean. Indica si antes de la creaci�n del fichero de
	 * configuracion de test, se comprobar� si existen estudiantes sin haberlo
	 * finalizado. Esto solo se har� si el objeto test no contiene ning�n vector
	 * de estudiantes o este est� vac�o. En caso contrar�o este par�metro no se
	 * tendr� en cuenta. backgroundFile: Objeto de tipo File. Contiene el
	 * background del test. deleteStudents: Objeto de tipo Vector. Contiene en
	 * cada posici�n el String "true" o "false", depediendo de si se desea
	 * eliminar el test del fichero log del alumno. DEVUELVE: Objeto de tipo
	 * String. FUNCI�N: M�todo que env�a los datos necesarios al servidor para
	 * que este cree un nuevo fichero de test con los par�metros necesarios para
	 * su ejecuci�n. Adem�s recibe la respuesta del servidor a la petici�n y la
	 * devuelve al m�todo que invoc� a este mismo.
	 */

	public String createTestFile(Test test, boolean replace, boolean checkFinishStudents, File backgroundFile,
			Vector deleteStudents) {
		URL url = null; // Almacena la URL donde se enviar� la petici�n.
		String line = ""; // Almacena la respuesta a la petici�n.

		// Conexi�n con el servidor.
		try {
			// //String host = codeBase.getHost();
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.CreateTestFile";
			url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Indicaci�n al servidor del tipo de datos enviados.
			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);

			ObjectOutputStream bufferObjectOut = new ObjectOutputStream(connection.getOutputStream());

			// Envio al servidor de los datos necesarios para crear el fichero de
			// test.

			// NO ALTERAR EL ORDEN!!!!!!!!!!!!!!!
			bufferObjectOut.writeObject(test);
			bufferObjectOut.writeObject(userLogin);
			bufferObjectOut.writeBoolean(replace);
			bufferObjectOut.writeBoolean(checkFinishStudents);
			bufferObjectOut.writeObject(deleteStudents);

			if (backgroundFile != null) {
				// Transformaci�n del fichero de imagen en un array de bytes y envio
				// del mismo al servidor.
				byte[] data = new byte[(int) backgroundFile.length()];
				FileInputStream fileInputStream = new FileInputStream(backgroundFile);
				DataInputStream dataInputStream = new DataInputStream(fileInputStream);
				dataInputStream.read(data);

				bufferObjectOut.writeObject(backgroundFile.getName().substring(
						backgroundFile.getName().lastIndexOf(".")).toLowerCase());
				bufferObjectOut.writeInt((int) backgroundFile.length());
				bufferObjectOut.write(data);
			} else {
				byte[] data = new byte[0];
				bufferObjectOut.writeObject("");
				bufferObjectOut.writeInt(0);
				bufferObjectOut.write(data);
			}

			bufferObjectOut.flush();

			// Cerrado de la comunicaci�n.
			bufferObjectOut.close();

			// Recepci�n de la respuesta.
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String lineTemp = "";
			while ((lineTemp = bufferIn.readLine()) != null)
				line = line.concat(lineTemp) + "\n";

			// Cerrado de la comunicaci�n.
			bufferIn.close();

			// Devoluci�n de la respuesta del servidor.
			return line;
		} catch (Exception e) {
			taskDone = 1;

			// En caso de producirse alguna excepci�n mientras se intenta conectar
			// con el servidor, se muestra la causa de la misma y se devuelve null.
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	// /////////////////////CLASES PROPIAS DE LA CLASE
	// TESTEDITOR////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: GetQuestionsFileNames. FUNCION: Clase contenida dentro de la clase
	 * TestEditor. Se encarga de invocar al m�todo getQuestionsFileNames de la
	 * clase padre para obtener los nombres de los ficheros de preguntas
	 * pertenecientes a las versiones anteriores de WOW y que est�n contenidos en
	 * el curso sobre el que se est� actuando.
	 */

	protected class GetQuestionsFileNames extends Thread {
		String theCourseName = "";

		public GetQuestionsFileNames(String aCourseName) {
			theCourseName = aCourseName;
		}

		public void run() {
			taskDone = 0;

			try {
				Thread.sleep(100);
			} catch (java.lang.InterruptedException e) {}

			// Calls the method that returns the question files of all the courses
			courseVector = getQuestionsFileNames("");
			taskDone = 1;
		}
	}

	/*
	 * NOMBRE: GetQuestions. FUNCION: Clase contenida dentro de la clase
	 * TestEditor. Se encarga de invocar al m�todo getQuestions de la clase padre
	 * para obtener las preguntas pertenecientes a un fichero de preguntas y que
	 * est�n contenido en el curso sobre el que se est� actuando.
	 */

	protected class GetQuestions {
		public GetQuestions(String courseName, String questionsFileName) {
			taskDone = 0;

			try {
				Thread.sleep(100);
			} catch (java.lang.InterruptedException e) {}

			// Llamada al m�todo de esta misma clase que devuelve los datos de la
			// de una pregunta.
			//questionsVector = getQuestions(courseName, questionsFileName);

			taskDone = 1;
		}
	}

	/*
	 * NOMBRE: GetClassicTestsFileNames. FUNCION: Clase contenida dentro de la
	 * clase TestEditor.Se encarga de invocar al m�todo getTestsFileNames de la
	 * clase padre para obtener los nombres de los ficheros de preguntas
	 * pertenecientes a las versiones anteriores de WOW y que est�n contenidos en
	 * el curso sobre el que se est� actuando
	 */

	protected class GetTestsFileNames extends Thread {
		private String theCourseName = "";
		private String theExecutionType = "";

		public GetTestsFileNames(final String aCourseName, final String aExecutionType) {
			theCourseName = aCourseName;
			theExecutionType = aExecutionType;
		}

		public void run() {
			taskDone = 0;

			try {
				Thread.sleep(100);
			} catch (java.lang.InterruptedException e) {}

			// Calls the method that returns the list of tests of this course
			courseVector = getTestsFileNames(theCourseName, theExecutionType);

			taskDone = 1;
		}
	}


	/**
	 * Innner class that checks the progress bar
	 */
	protected class TimeControl extends Thread {
		public TimeControl() {}

		public void run() {
			try {
				Thread.sleep(20000);

				taskDone = -1;

				jLabelStatus.setText("Overcome time of wait. Try it later.");

				if (workerRepaintProgressBar != null) {
					workerRepaintProgressBar.stop();
				}
				if (workerGetQuestionsFileNames != null) {
					workerGetQuestionsFileNames.stop();
				}
				if (workerGetTestsFileNames != null) {
					workerGetTestsFileNames.stop();
				}

			} catch (java.lang.InterruptedException e) {}
		}
	}

	public String createMobileJar(final String theCourseName, final String theTestFileName) {
		URL url = null; // URL with the request to the WOW! server
		String line = ""; // Stores the result of the request

		// Build the URL to make the request to the server
		String consult = "courseName=" + theCourseName + "&testFileName=" + theTestFileName;

		// Connect with the server
		try {
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.CreateMobileJar";
			url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send the data
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(consult);
			bufferOut.flush();

			// Receives the response
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String lineTemp = "";
			while ((lineTemp = bufferIn.readLine()) != null)
				line = line.concat(lineTemp) + "\n";

			// Closes the communication
			bufferOut.close();
			bufferIn.close();

			// Returns the response of the server
			return line;
		} catch (Exception e) {
			taskDone = 1;
			// In error case, we shown an error message and null is returned
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error connecting to the server",
					JOptionPane.ERROR);

			return null;
		}
	}

	/*
	 * NOMBRE: RepaintProgressBar. FUNCION: Clase contenida dentro de la clase
	 * TestEditor. Se encarga de actualizar el valor y repintar la barra de
	 * progreso (jProgressBar).
	 */
	protected class RepaintProgressBar extends Thread {
		public void run() {
			repaintProgressBar();
		}
	}
}