package es.uco.WOW.TestEditor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import es.uco.WOW.Utils.Course;
import es.uco.WOW.Utils.Question;
import es.uco.WOW.Utils.QuestionsFileTest;
import es.uco.WOW.Utils.StudentTest;
import es.uco.WOW.Utils.Test;

/**
 * <p>Title: Wow! TestEditor</p> <p>Description: Herramienta para la edicion
 * de preguntas tipo test adaptativas </p> <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/*
 * NOMBRE: FrmEditTest. FUNCION: Esta
 * clase presenta al usuario una interfaz gr�fica con los datos sobre la
 * configuraci�n de un test para que pueda realizar cambios sobre el mismo y
 * guardar dichas updates en el servidor. LAST MODIFICATION: 06-02-2008
 */

public class FrmEditTest extends TestEditorInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String titleOrigin = "";

	protected String courseName; // Almacena el nombre del curso sobre el que se
										// est�

	// actuando.
	protected String testFileName; // Almacena el nombre del fichero de test que
											// se

	// va a mostrar para ser modificado.
	private URL codeBase; // Almacena la URL del servidor del que se ha descargado

	// el applet.
	// este frame.
	private JInternalFrame internalFrame;

	private Test test; // Almacena los datos del test a

	// modificar.

	// private int testType = 0;

	private boolean testSaved = true;

	private boolean testModified = false;

	private boolean presentationParametersModified = false;

	private boolean htmlParametersModified = false;

	private boolean evaluationParametersModified = false;

	private boolean irtParametersModified = false;

	private boolean studentsModified = false;

	private boolean enableModified = false;

	private boolean imageDeleted = false;

	private boolean testTransformed = false;

	// private int tabSelect = 0; //Indica que tabb fue seleccionado la ultima
	// vez del jTabbedPane.
	private File backgroundFile = null;

	private Vector courseVector = null;

	private Vector studentTestVector = null;

	private Vector finishTestVector = null;

	private Vector canRepeatVector = null;

	private Vector deleteStudentVector = null;

	private Vector questionsVector = null; // Almacenara objetos Question que

	// contendr�n los datos de una pregunta
	// de un fichero. Este Vector contendr�
	// los datos de todas las preguntas de
	// un fichero.
	private Vector nameFileAndQuestions; // Almacena el nombre del fichero de

	// preguntas en su primera posici�n y en
	// la segunda el vector que contiene todas
	// las preguntas de dicho fichero
	// (questionsList).
	protected Vector questionsOfMultipleFiles = null; // Almacena en cada una de
																	// sus

	// posiciones un objeto Vector del
	// tipo anteriormente descrito
	// (nameFileAndQuestions).
	private Vector conceptVectorInitial;

	private Vector conceptVectorWithRepetition;

	private Vector testVector; // Almacenar� en cada una de sus posiciones un

	// objeto de tipo QuestionsFileTest, cada uno de
	// los cuales contendr� el nombre de un fichero de
	// preguntas que se est� usando para formar la
	// bater�a de test y un Vector, el cual contendr�
	// los c�digos de las preguntas del fichero antes
	// mencionado que se incluir�n en el test.
	private String codeOfQuestionsToDelete = "";

	private String questionsFileNameToDelete = "";

	private String codeOfQuestionsToAdd = "";

	private Vector questionsFileVector = null;

	private Vector questionsFileNameVector = null;

	private int taskDone = 0;

	private Timer timerNoTaskDone = null;

	// Componentes del frame.
	private JPanel jPanelContent;

	private JPanel jPanelTitle;

	private JPanel jPanelButton;

	private JPanel jPanelStatus;

	private JTabbedPane jTabbedPane;

	private JSplitPane jSplitPaneTestQuestion;

	private JSplitPane jSplitPaneAddQuestionToTest;

	private JPanel jPanelPresentationParameters;

	private JPanel jPanelShowInitialInfo;

	private JPanel jPanelLabelQuestionsOrder;

	private JPanel jPanelLabelAnswersOrder;

	private JPanel jPanelQuestionsOrderRadioButton;

	private JPanel jPanelAnswersOrderRadioButton;

	private JPanel jPanelShowQuestionCorrection;

	private JPanel jPanelVerbose;

	private JPanel jPanelShowCorrectAnswers;

	private JPanel jPanelTimeToAnswer;

	private JPanel jPanelRepeatWithoutAnswer;

	private JPanel jPanelShowFinalInfo;

	private JPanel jPanelPresentationParametersButton;

	private JPanel jPanelHtmlParameters;

	private JPanel jPanelHtmlParametersButton;

	private JPanel jPanelButtonTestName;

	private JPanel jPanelButtonBgColor;

	private JPanel jPanelButtonBackground;

	private JPanel jPanelButtonTitleColor;

	private JPanel jPanelButtonResetAll;

	private JPanel jPanelHtmlParametersEditorPane;

	private JPanel jPanelEvaluationParameters;

	private JPanel jPanelAnswersCorrection;

	private JPanel jPanelIncorrectAnswers;

	private JPanel jPanelIncorrectAnswersLabelRadioButton;

	private JPanel jPanelLabelIncorrectAnswers;

	private JPanel jPanelIncorrectAnswersRadioButton;

	private JPanel jPanelIncorrectAnswersPenalize;

	private JPanel jPanelLabelIncorrectAnswersPenalize12;

	private JPanel jPanelLabelIncorrectAnswersPenalize3;

	private JPanel jPanelWithoutAnswers;

	private JPanel jPanelWithoutAnswersLabelRadioButton;

	private JPanel jPanelLabelWithoutAnswers;

	private JPanel jPanelWithoutAnswersRadioButton;

	private JPanel jPanelWithoutAnswersPenalize;

	private JPanel jPanelLabelWithoutAnswersPenalize12;

	private JPanel jPanelLabelWithoutAnswersPenalize3;

	private JPanel jPanelKnowledge;

	private JPanel jPanelLabelKnowledge;

	private JPanel jPanelKnowledgeValue;

	private JPanel jPanelEvaluationParametersButton;

	private JPanel jPanelAdaptiveParameters;

	private JPanel jPanelLabelStartCriterion;

	private JPanel jPanelSliderStartCriterion;

	private JPanel jPanelStartCriterion;

	private JPanel jPanelContinuationCriterion;

	private JPanel jPanelIrtModel1P;

	private JPanel jPanelIrtModel2P;

	private JPanel jPanelIrtModel3P;

	private JPanel jPanelStandardError;

	private JPanel jPanelNumberItems;

	private JPanel jPanelStopCriterion;

	private JPanel jPanelStopCriterionRadioButton;

	private JPanel jPanelStudents;

	private JPanel jPanelStudentsLogin;

	private JPanel jPanelListLogin;

	private JPanel jPanelJListLoginButton;

	private JPanel jPanelStudentsData;

	private JPanel jPanelFinish;

	private JPanel jPanelCanRepeat;

	private JPanel jPanelDeleteStudent;

	private JPanel jPanelScore;

	private JPanel jPanelProficiency;

	private JPanel jPanelStudentStandardError;

	private JPanel jPanelMake;

	private JPanel jPanelStudentsButton;

	private JPanel jPanelStudentsButtonDelete;

	private JPanel jPanelEnableTest;

	private JPanel jPanelTestQuestion;

	private JPanel jPanelTreeTestQuestion;

	private JPanel jPanelTypeAndConcept;

	private JPanel jPanelTestType;

	private JPanel jPanelAbstractConcept;

	private JPanel jPanelConcept;

	private JPanel jPanelTotalQuestions;

	private JPanel jPanelTreeTestQuestionButton;

	private JPanel jPanelAddQuestionToTest;

	private JPanel jPanelAddQuestionToTestButton;

	private JPanel jPanelTreeQuestionsFile;

	private JScrollPane jScrollPanePresentationParameters;

	private JScrollPane jScrollPaneHtmlParameters;

	private JScrollPane jScrollPaneEvaluationParameters;

	private JScrollPane jScrollPaneAdaptiveParameters;

	private JScrollPane jScrollPaneStudents;

	private JScrollPane jScrollPaneTestQuestion;

	private JScrollPane jScrollPaneAddQuestionToTest;

	private JScrollPane jScrollPaneListLogin;

	private JScrollPane jScrollPaneListConcept;

	private JScrollPane jScrollPaneTreeTestQuestion;

	private JScrollPane jScrollPaneTreeQuestionsFile;

	private JScrollPane jScrollPaneTableTestQuestion;

	private JScrollPane jScrollPaneTableQuestionsFile;

	private JLabel jLabelTitle;

	private JLabel jLabelShowInitialInfo;

	private JLabel jLabelQuestionsOrder;

	private JLabel jLabelQuestionsOrderInfo1;

	private JLabel jLabelAnswersOrder;

	private JLabel jLabelAnswersOrderInfo1;

	private JLabel jLabelShowQuestionCorrection;

	private JLabel jLabelVerbose;

	private JLabel jLabelShowCorrectAnswers;

	private JLabel jLabelTimeToAnswerInfo;

	private JLabel jLabelRepeatWithoutAnswerInfo;

	private JLabel jLabelShowFinalInfo;

	private JLabel jLabelTestName;

	private JLabel jLabelIncorrectAnswers;

	private JLabel jLabelIncorrectAnswersPenalize1;

	private JLabel jLabelIncorrectAnswersPenalize2;

	private JLabel jLabelIncorrectAnswersPenalize3;

	private JLabel jLabelWithoutAnswers;

	private JLabel jLabelWithoutAnswersPenalize1;

	private JLabel jLabelWithoutAnswersPenalize2;

	private JLabel jLabelWithoutAnswersPenalize3;

	private JLabel jLabelKnowledge1;

	private JLabel jLabelKnowledge2;

	private JLabel jLabelKnowledge3;

	private JLabel jLabelKnowledgePercentage;

	private JLabel jLabelInitialProficiency;

	private JLabel jLabelInitialProficiencyInfo;

	private JLabel jLabel1PInfo;

	private JLabel jLabel2PInfo;

	private JLabel jLabel3PInfo;

	private JLabel jLabelLogin;

	private JLabel jLabelTotalStudents;

	private JLabel jLabelScore;

	private JLabel jLabelProficiency;

	private JLabel jLabelStandardError;

	private JLabel jLabelMake1;

	private JLabel jLabelMake2;

	private JLabel jLabelEnableTest;

	private JLabel jLabelTestType;

	private JLabel jLabelAbstractConcept;

	private JLabel jLabelConcept;

	private JLabel jLabelTotalQuestions;

	private JLabel jLabelQuestionsFile;

	private JLabel jLabelStatus;

	private JCheckBox jCheckBoxShowInitialInfo;

	private JCheckBox jCheckBoxShowQuestionCorrection;

	private JCheckBox jCheckBoxVerbose;

	private JCheckBox jCheckBoxShowCorrectAnswers;

	private JCheckBox jCheckBoxTimeToAnswer;

	private JCheckBox jCheckBoxRepeatWithoutAnswer;

	private JCheckBox jCheckBoxShowFinalInfo;

	private JCheckBox jCheckBoxFinish;

	private JCheckBox jCheckBoxCanRepeat;

	private JCheckBox jCheckBoxDeleteStudent;

	private JCheckBox jCheckBoxEnableTest;

	private JComboBox jComboBoxQuestionsOrder;

	private JComboBox jComboBoxAnswersOrder;
	
	private JRadioButton jRadioButtonIncorrectAnswersPenalizeYes;

	private JRadioButton jRadioButtonIncorrectAnswersPenalizeNo;

	private JRadioButton jRadioButtonWithoutAnswersPenalizeYes;

	private JRadioButton jRadioButtonWithoutAnswersPenalizeNo;

	private JRadioButton jRadioButton1P;

	private JRadioButton jRadioButton2P;

	private JRadioButton jRadioButton3P;

	private JRadioButton jRadioButtonStandardError;

	private JRadioButton jRadioButtonNumberItems;

	private ButtonGroup jRadioButtonIncorrectAnswersGroup;

	private ButtonGroup jRadioButtonWithoutAnswersGroup;

	private ButtonGroup jRadioButtonIrtModelGroup;

	private ButtonGroup jRadioButtonStopCriterionGroup;

	private JButton jButtonModify;

	private JButton jButtonCancel;

	private JButton jButtonReload;

	private JButton jButtonSaveAs;

	private JButton jButtonResetPresentation;

	private JButton jButtonResetEvaluation;

	private JButton jButtonSetTitle;

	private JButton jButtonSetBgColor;

	private JButton jButtonSetBackground;

	private JButton jButtonSetTitleColor;

	private JButton jButtonResetTitle;

	private JButton jButtonResetBgColor;

	private JButton jButtonResetBackground;

	private JButton jButtonResetTitleColor;

	private JButton jButtonResetAll;

	private JButton jButtonSelectAllStudents;

	private JButton jButtonResetStudents;

	private JButton jButtonFinishTest;

	private JButton jButtonCanRepeat;

	private JButton jButtonNoCanRepeat;

	private JButton jButtonDeleteStudent;

	private JButton jButtonNoDeleteStudent;

	private JButton jButtonExamineTestQuestion;

	private JButton jButtonDeleteQuestionToTest;

	private JButton jButtonResetQuestionToTest;

	private JButton jButtonGetQuestionsFileList;

	private JButton jButtonExamineQuestionsFile;

	private JButton jButtonAddQuestionToTest;

	private JProgressBar jProgressBar;

	private JSlider jSliderTimeToAnswer;

	private JSlider jSliderKnowledge;

	private JSlider jSliderInitialProficiency;

	private JSlider jSliderStandardError;

	private JSlider jSliderNumberItems;

	private JComboBox jComboBoxIncorrectAnswers;

	private JComboBox jComboBoxWithoutAnswers;

	private int indexComboIncorrect = -1;

	private int indexComboWithoutAnswer = -1;

	private JComboBox jComboBoxQuestionsFile;

	private JEditorPane jEditorPaneHtmlParameters;

	private JList jListLogin;

	private JList jListConcept;

	private JTree jTreeTestQuestion;

	private JTree jTreeQuestionsFile;

	private DefaultMutableTreeNode rootNodeTestQuestion;

	private DefaultMutableTreeNode rootNodeQuestionsFile;

	private DefaultMutableTreeNode nodeQuestionTest;

	private DefaultMutableTreeNode nodeQuestionAdd;

	private JTable jTableTestQuestion;

	private TableModel tableModelTestQuestion;

	private TableSorter tableSorterTestQuestion;

	private Vector rowsVectorTestQuestion;

	private Vector columnsVectorTestQuestion;

	private JTable jTableQuestionsFile;

	private TableModel tableModelQuestionsFile;

	private TableSorter tableSorterQuestionsFile;

	private Vector rowsVectorQuestionsFile;

	private Vector columnsVectorQuestionsFile;

	private JTextField jTextFieldTimeToAnswer;

	private JTextField jTextFieldTestName;

	private JTextField jTextFieldBgColor;

	private JTextField jTextFieldBackground;

	private JTextField jTextFieldTitleColor;

	private JTextField jTextFieldKnowledge;

	private JTextField jTextFieldTotalStudents;

	private JTextField jTextFieldScore;

	private JTextField jTextFieldProficiency;

	private JTextField jTextFieldStandardError;

	private JTextField jTextFieldMake;

	private JTextField jTextFieldTestType;

	private JTextField jTextFieldAbstractConcept;

	private JTextField jTextFieldConcept;

	private JTextField jTextFieldTotalQuestions;

	private JTextField jTextFieldInitialProficiency;

	private JTextField jTextFieldStudentStandardError;

	private JTextField jTextFieldNumberItems;

	// private double initialProficiency = -1;
	// private double standardError = -1;
	// private int numberItems = -1;
	// private int knowledge;

	private BoxLayout boxLayoutPanelContent;

	private BoxLayout boxLayoutPanelPresentationParameters;

	private BoxLayout boxLayoutPanelHtmlParametersButton;

	private BoxLayout boxLayoutPanelHtmlParametersEditorPane;

	private BoxLayout boxLayoutPanelEvaluationParameters;

	private BoxLayout boxLayoutPanelAnswersCorrection;

	private BoxLayout boxLayoutPanelIncorrectAnswersLabelRadioButton;

	private BoxLayout boxLayoutPanelIncorrectAnswersPenalize;

	private BoxLayout boxLayoutPanelWithoutAnswersLabelRadioButton;

	private BoxLayout boxLayoutPanelWithoutAnswersPenalize;

	private BoxLayout boxLayoutPanelLabelKnowledge;

	private BoxLayout boxLayoutPanelAdaptiveParameters;

	private BoxLayout boxLayoutPanelContinuationCriterion;

	private BoxLayout boxLayoutPanelStopCriterionRadioButton;

	private BoxLayout boxLayoutPanelJListLogin;

	private BoxLayout boxLayoutPanelStudentsData;

	private BoxLayout boxLayoutPanelStudents;

	private BoxLayout boxLayoutPanelTypeAndConcept;

	private BoxLayout boxLayoutPanelTestQuestion;

	private BoxLayout boxLayoutPanelAddQuestionToTestButton;

	private BoxLayout boxLayoutPanelAddQuestionToTest;

	private FlowLayout flowLayoutPanelTitle;

	private FlowLayout flowLayoutPanelButton;

	private FlowLayout flowLayoutPanelStatus;

	private FlowLayout flowLayoutPanelShowInitialInfo;

	private FlowLayout flowLayoutPanelLabelQuestionsOrder;

	private FlowLayout flowLayoutPanelLabelAnswersOrder;

	private FlowLayout flowLayoutPanelQuestionsOrderRadioButton;

	private FlowLayout flowLayoutPanelAnswersOrderRadioButton;

	private FlowLayout flowLayoutPanelShowQuestionCorrection;

	private FlowLayout flowLayoutPanelVerbose;

	private FlowLayout flowLayoutPanelShowCorrectAnswers;

	private FlowLayout flowLayoutPanelTimeToAnswer;

	private FlowLayout flowLayoutPanelRepeatWithoutAnswer;

	private FlowLayout flowLayoutPanelShowFinalInfo;

	private FlowLayout flowLayoutPanelPresentationParametersButton;

	private FlowLayout flowLayoutPanelHtmlParameters;

	private FlowLayout flowLayoutPanelButtonTestName;

	private FlowLayout flowLayoutPanelButtonBgColor;

	private FlowLayout flowLayoutPanelButtonBackground;

	private FlowLayout flowLayoutPanelButtonTitleColor;

	private FlowLayout flowLayoutPanelButtonResetAll;

	private FlowLayout flowLayoutPanelIncorrectAnswers;

	private FlowLayout flowLayoutPanelIncorrectAnswersRadioButton;

	private FlowLayout flowLayoutPanelLabelIncorrectAnswers;

	private FlowLayout flowLayoutPanelLabelIncorrectAnswersPenalize12;

	private FlowLayout flowLayoutPanelLabelIncorrectAnswersPenalize3;

	private FlowLayout flowLayoutPanelWithoutAnswers;

	private FlowLayout flowLayoutPanelWithoutAnswersRadioButton;

	private FlowLayout flowLayoutPanelLabelWithoutAnswers;

	private FlowLayout flowLayoutPanelLabelWithoutAnswersPenalize12;

	private FlowLayout flowLayoutPanelLabelWithoutAnswersPenalize3;

	private FlowLayout flowLayoutPanelKnowledge;

	private FlowLayout flowLayoutPanelKnowledgeValue;

	private FlowLayout flowLayoutPanelEvaluationParametersButton;

	private FlowLayout flowLayoutPanelStartCriterion;

	private FlowLayout flowLayoutPanelLabelStartCriterion;

	private FlowLayout flowLayoutPanelSliderStartCriterion;

	private FlowLayout flowLayoutPanelIrtModel1P;

	private FlowLayout flowLayoutPanelIrtModel2P;

	private FlowLayout flowLayoutPanelIrtModel3P;

	private FlowLayout flowLayoutPanelStandardError;

	private FlowLayout flowLayoutPanelNumberItems;

	private FlowLayout flowLayoutPanelStopCriterion;

	private FlowLayout flowLayoutPanelStudentsLogin;

	private FlowLayout flowLayoutPanelFinish;

	private FlowLayout flowLayoutPanelCanRepeat;

	private FlowLayout flowLayoutPanelDeleteStudent;

	private FlowLayout flowLayoutPanelScore;

	private FlowLayout flowLayoutPanelProficiency;

	private FlowLayout flowLayoutPanelStudentStandardError;

	private FlowLayout flowLayoutPanelMake;

	private FlowLayout flowLayoutPanelStudentsButton;

	private FlowLayout flowLayoutPanelStudentsButtonDelete;

	private FlowLayout flowLayoutPanelJListLoginButton;

	private FlowLayout flowLayoutPanelEnableTest;

	private FlowLayout flowLayoutPanelJTreeTestQuestion;

	private FlowLayout flowLayoutPanelTestType;

	private FlowLayout flowLayoutPanelAbstractConcept;

	private FlowLayout flowLayoutPanelConcept;

	private FlowLayout flowLayoutPanelTotalQuestions;

	private FlowLayout flowLayoutPanelJTreeTestQuestionButton;

	private FlowLayout flowLayoutPanelJTreeQuestionsFile;

	private ImageIcon iconModify32;

	private ImageIcon iconCancel32;

	private ImageIcon iconAddArrow32;

	private ImageIcon iconDelete32;

	private ImageIcon iconNoDelete32;

	private ImageIcon iconSetTitle32;

	private ImageIcon iconResetTitle32;

	private ImageIcon iconSetBgColor32;

	private ImageIcon iconResetBgColor32;

	private ImageIcon iconSetTitleColor32;

	private ImageIcon iconResetTitleColor32;

	private ImageIcon iconClear32;

	private ImageIcon iconAddImage32;

	private ImageIcon iconDeleteImage32;

	private ImageIcon iconInfoGreen16;

	private ImageIcon iconEyes16;

	private ImageIcon iconHtml16;

	private ImageIcon iconEvaluation16;

	private ImageIcon iconQuestion16;

	private ImageIcon iconStudents16;

	private ImageIcon iconAddQuestion16;

	private ImageIcon iconSelectAll32;

	private ImageIcon iconFinishTest32;

	private ImageIcon iconCanRepeat32;

	private ImageIcon iconNoCanRepeat32;

	private ImageIcon iconExamineQuestion32;

	private ImageIcon iconReload32;

	private ImageIcon iconCreateClassicTest32;

	private ImageIcon iconCreateAdaptiveTest32;

	private boolean ImageIconLoad = false;

	private int ANSWER_TIME_MAX;

	private int ANSWER_TIME_MIN;

	private int INITIAL_PROFICIENCY_MIN = -2;

	private double INITIAL_PROFICIENCY_DEFAULT = 0.5;

	private int INITIAL_PROFICIENCY_MAX = 2;

	private int STANDARD_ERROR_MIN = 0;

	private double STANDARD_ERROR_DEFAULT = 0.33;

	private int STANDARD_ERROR_MAX = 1;

	protected String executionTypeString = "";

	/*
	 * NOMBRE: FrmEditTest (Constructor de esta misma clase). PERTENECE A: Clase
	 * FrmEditTest. LLAMADA POR: Al invocar la clase. LLAMA A: JImageIconInit();
	 * JLabelInit(); JCheckBoxInit(); JRadioButtonInit(); JSliderInit();
	 * JSliderInit1(); JTextFieldInit(); JTextFieldInit1(); JEditorPaneInit();
	 * JComboBoxInit(); JListLoginInit(); JListConceptInit();
	 * JTreeTestQuestionInit(); JTreeQuestionsFileInit();
	 * JTableTestQuestionInit(); JTableQuestionsFileInit(); JProgressBarInit();
	 * JButtonInit(); JPanelInit();
	 * 
	 *  RECIBE: title: Objeto de tipo String. Indica el
	 * t�tulo del frame. width: Objeto de tipo int. Indica la anchura
	 * inicial del frame. height: Objeto de tipo int. Indica la altura
	 * inicial del frame. userLogin: Objeto de tipo String. Indica el
	 * login del usuario. userPassword: Objeto de tipo String. Indica el password
	 * del usuario. courseName: Objeto de tipo String. Contiene el nombre del
	 * curso donde se est� actuando. testFileName: Objeto de tipo String.
	 * Contiene el nombre del fichero de test. courseConcept: Objeto de tipo
	 * Course. Almacena el nombre del curso y los nombres de los ficheros de
	 * conceptos de ese curso. codeBase: Objeto de tipo URL. Contiene la URL del
	 * servidor desde donde se ha cargado el applet. wowPath: Objeto de tipo
	 * String. Contiene el path del sistema desde el que se descarg� el applet.
	 * iconTestEditorPath: Objeto de tipo String. Contiene el path del directorio
	 * que contiene los iconos usados por la interfaz gr�fica.  Objeto de
	 * tipo JFrame. Contiene el JFrame principal del applet y padre de este
	 * frame. executionType: Objeto de tipo int. Indica si se
	 * configurara un test cl�sico o adaptativo. DEVUELVE: nada. FUNCI�N:
	 * Constructor de la clase FrmEditTest. Se encarga de configurar el aspecto
	 * del frame, su funcionalidad, as� como de inicializar sus
	 * componentes.
	 */

	public FrmEditTest(String aTitle, int aWidth, int aHeight, String aUserLogin, String aUserPassword,
			String aCourseName, String aTestFileName, URL aCodeBase, String anWOWPath,
			String iconPath, TestEditor father, int anExecutionType) {
	//
		// Calls the parent constructor
		super(aTitle, aWidth, aHeight, father, anWOWPath, iconPath);
		
		// Sets user variables
		courseName = aCourseName;
		testFileName = aTestFileName;
		codeBase = aCodeBase;
		title = aTitle;
		titleOrigin = title;
		ANSWER_TIME_MAX = father.ANSWER_TIME_MAX;
		ANSWER_TIME_MIN = father.ANSWER_TIME_MIN;

		try{
	   	// Obtenci�n de los datos del test a modificar.
			test = father.getTest(courseName, testFileName);

			if (test == null) {
				// Mensaje para indicar al usuario que se ha producido un error al
				// obtener los datos del test.
				JOptionPane.showMessageDialog(this, "ERROR: The configuration data of the selected test " + "\n"
						+ "HAVE NOT BEEN loaded CORRECTLY.", "Load Test Error", JOptionPane.ERROR_MESSAGE);
				dispose();
				return;
			}

			// Lectura del tipo de ejecuci�n del test.
			executionTypeString = test.getExecutionType();

			// Lectura del nombre del fichero de test.
			testFileName = test.getTestFileName();

			// Lectura de los conceptos que evalua el test.
			conceptVectorInitial = new Vector();
			for (int i = 0; i < test.getConceptVector().size(); i++) {
				conceptVectorInitial.add(test.getConceptVector().get(i).toString());
			}

			// Inicializaci�n inicial de los componentes del frame.
			initFrame = false;

			try {
				JImageIconInit();
				JLabelInit();
				JCheckBoxInit();
				JRadioButtonInit();
				JSliderInit();
				JSliderInit1();
				JTextFieldInit();
				JTextFieldInit1();
				JEditorPaneInit();
				JComboBoxInit();
				JListLoginInit();
				JListConceptInit();
				JButtonInit();

				rootNodeTestQuestion = new DefaultMutableTreeNode("Test              ");
				JTreeTestQuestionInit(rootNodeTestQuestion);

				rootNodeQuestionsFile = new DefaultMutableTreeNode("Empty            ");
				JTreeQuestionsFileInit(rootNodeQuestionsFile);

				JTableTestQuestionInit();
				JTableQuestionsFileInit();
				JProgressBarInit();
				JPanelInit();

				frameInit();

				internalFrame = this;
			} catch (Exception e) {
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /////////////////////////////////////////////////////////////////////////////
	// //////// INICIALIZACI�N DE LOS COMPONENTES DEL JINTERNALFRAME
	// ////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: JImageIconInit. PERTENECE A: Clase FrmEditTest. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los ImageIcon del frame,
	 * reservando memoria para los mismos y estableciendo su tama�o, su
	 * contenido, funcionalidad... Los iconos se encuentran en el servidor, con
	 * lo que ser� solicitado el envio de los mismos desde el servidor al applet
	 * descargado.
	 */

	private void JImageIconInit() {
		// Construcci�n del String de solicitud de los iconos al servidor.
		String petitionModify32 = wowPath + iconTestEditorPath + "save32.gif";
		String petitionCancel32 = wowPath + iconTestEditorPath + "cancel32.gif";
		String petitionAddArrow32 = wowPath + iconTestEditorPath + "addArrow32.gif";
		String petitionDelete32 = wowPath + iconTestEditorPath + "deleteFile32.gif";
		String petitionNoDelete32 = wowPath + iconTestEditorPath + "noDeleteFile32.gif";
		String petitionSetTitle32 = wowPath + iconTestEditorPath + "setTitle32.gif";
		String petitionResetTitle32 = wowPath + iconTestEditorPath + "resetTitle32.gif";
		String petitionSetBgColor32 = wowPath + iconTestEditorPath + "setBgColor32.gif";
		String petitionResetBgColor32 = wowPath + iconTestEditorPath + "resetBgColor32.gif";
		String petitionSetTitleColor32 = wowPath + iconTestEditorPath + "setTitleColor32.gif";
		String petitionResetTitleColor32 = wowPath + iconTestEditorPath + "resetTitleColor32.gif";
		String petitionClear32 = wowPath + iconTestEditorPath + "clear32.gif";
		String petitionAddImage32 = wowPath + iconTestEditorPath + "addImage32.gif";
		String petitionDeleteImage32 = wowPath + iconTestEditorPath + "deleteImage32.gif";
		String petitionInfoGreen16 = wowPath + iconTestEditorPath + "info_green_16.gif";
		String petitionEyes16 = wowPath + iconTestEditorPath + "eyes16.gif";
		String petitionHtml16 = wowPath + iconTestEditorPath + "html16.gif";
		String petitionEvaluation16 = wowPath + iconTestEditorPath + "evaluation16.gif";
		String petitionQuestion16 = wowPath + iconTestEditorPath + "help16.gif";
		String petitionStudents16 = wowPath + iconTestEditorPath + "students16.gif";
		String petitionAddQuestion16 = wowPath + iconTestEditorPath + "addQuestionToTest16.gif";
		String petitionSelectAll32 = wowPath + iconTestEditorPath + "selectAll32.gif";
		String petitionFinishTest32 = wowPath + iconTestEditorPath + "finishTest32.gif";
		String petitionCanRepeat32 = wowPath + iconTestEditorPath + "canRepeat32.gif";
		String petitionNoCanRepeat32 = wowPath + iconTestEditorPath + "noCanRepeat32.gif";
		String petitionExamineQuestion32 = wowPath + iconTestEditorPath + "examine32.gif";
		String petitionReload32 = wowPath + iconTestEditorPath + "reload32.gif";
		String petitionCreateClassicTest32 = wowPath + iconTestEditorPath + "createClassicTest32.gif";
		String petitionCreateAdaptiveTest32 = wowPath + iconTestEditorPath + "createAdaptiveTest32.gif";
		// Conexi�n con el servidor.
		try {
			URL urlModify32 = new URL(codeBase, petitionModify32);
			URL urlCancel32 = new URL(codeBase, petitionCancel32);
			URL urlAddArrow32 = new URL(codeBase, petitionAddArrow32);
			URL urlDelete32 = new URL(codeBase, petitionDelete32);
			URL urlNoDelete32 = new URL(codeBase, petitionNoDelete32);
			URL urlSetTitle32 = new URL(codeBase, petitionSetTitle32);
			URL urlResetTitle32 = new URL(codeBase, petitionResetTitle32);
			URL urlSetBgColor32 = new URL(codeBase, petitionSetBgColor32);
			URL urlResetBgColor32 = new URL(codeBase, petitionResetBgColor32);
			URL urlSetTitleColor32 = new URL(codeBase, petitionSetTitleColor32);
			URL urlResetTitleColor32 = new URL(codeBase, petitionResetTitleColor32);
			URL urlClear32 = new URL(codeBase, petitionClear32);
			URL urlAddImage32 = new URL(codeBase, petitionAddImage32);
			URL urlDeleteImage32 = new URL(codeBase, petitionDeleteImage32);
			URL urlInfoGreen16 = new URL(codeBase, petitionInfoGreen16);
			URL urlEyes16 = new URL(codeBase, petitionEyes16);
			URL urlHtml16 = new URL(codeBase, petitionHtml16);
			URL urlEvaluation16 = new URL(codeBase, petitionEvaluation16);
			URL urlQuestion16 = new URL(codeBase, petitionQuestion16);
			URL urlStudents16 = new URL(codeBase, petitionStudents16);
			URL urlAddQuestion16 = new URL(codeBase, petitionAddQuestion16);
			URL urlSelectAll32 = new URL(codeBase, petitionSelectAll32);
			URL urlFinishTest32 = new URL(codeBase, petitionFinishTest32);
			URL urlCanRepeat32 = new URL(codeBase, petitionCanRepeat32);
			URL urlNoCanRepeat32 = new URL(codeBase, petitionNoCanRepeat32);
			URL urlExamineQuestion32 = new URL(codeBase, petitionExamineQuestion32);
			URL urlReload32 = new URL(codeBase, petitionReload32);
			URL urlCreateClassicTest32 = new URL(codeBase, petitionCreateClassicTest32);
			URL urlCreateAdaptiveTest32 = new URL(codeBase, petitionCreateAdaptiveTest32);

			// Reserva de memoria para los ImageIcon con los iconos obtenidos.
			iconModify32 = new ImageIcon(urlModify32);
			iconCancel32 = new ImageIcon(urlCancel32);
			iconAddArrow32 = new ImageIcon(urlAddArrow32);
			iconDelete32 = new ImageIcon(urlDelete32);
			iconNoDelete32 = new ImageIcon(urlNoDelete32);
			iconSetTitle32 = new ImageIcon(urlSetTitle32);
			iconResetTitle32 = new ImageIcon(urlResetTitle32);
			iconSetBgColor32 = new ImageIcon(urlSetBgColor32);
			iconResetBgColor32 = new ImageIcon(urlResetBgColor32);
			iconSetTitleColor32 = new ImageIcon(urlSetTitleColor32);
			iconResetTitleColor32 = new ImageIcon(urlResetTitleColor32);
			iconClear32 = new ImageIcon(urlClear32);
			iconAddImage32 = new ImageIcon(urlAddImage32);
			iconDeleteImage32 = new ImageIcon(urlDeleteImage32);
			iconInfoGreen16 = new ImageIcon(urlInfoGreen16);
			iconEyes16 = new ImageIcon(urlEyes16);
			iconHtml16 = new ImageIcon(urlHtml16);
			iconEvaluation16 = new ImageIcon(urlEvaluation16);
			iconQuestion16 = new ImageIcon(urlQuestion16);
			iconStudents16 = new ImageIcon(urlStudents16);
			iconAddQuestion16 = new ImageIcon(urlAddQuestion16);
			iconSelectAll32 = new ImageIcon(urlSelectAll32);
			iconFinishTest32 = new ImageIcon(urlFinishTest32);
			iconCanRepeat32 = new ImageIcon(urlCanRepeat32);
			iconNoCanRepeat32 = new ImageIcon(urlNoCanRepeat32);
			iconExamineQuestion32 = new ImageIcon(urlExamineQuestion32);
			iconReload32 = new ImageIcon(urlReload32);
			iconCreateClassicTest32 = new ImageIcon(urlCreateClassicTest32);
			iconCreateAdaptiveTest32 = new ImageIcon(urlCreateAdaptiveTest32);

			ImageIconLoad = true;
		} catch (java.net.MalformedURLException e) {
			ImageIconLoad = false;
		}
	}

	/*
	 * NOMBRE: JLabelInit. PERTENECE A: Clase FrmEditTest. LLAMADA POR: El
	 * constructor de la clase a la que pertenece.; LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JLabel del frame,
	 * reservando memoria para los mismos y estableciendo su color, su
	 * contenido...
	 */

	private void JLabelInit() {
		// Reserva de memoria y establecimiento del contenido para los JLabel
		// correspondiente al presentationParameters
		jLabelTitle = new JLabel("Parameters of test execution.");
		jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 20));

		jLabelStatus = new JLabel("Done                                         "
				+ "                                             "
				+ "                                             ");
		jLabelStatus.setFont(new java.awt.Font("Dialog", 0, 12));

		jLabelShowInitialInfo = new JLabel(" Indicates if the presentation of the test will be shown.");
		jLabelShowInitialInfo.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelQuestionsOrder = new JLabel(" Order of the questions: ");
		jLabelQuestionsOrder.setFont(new java.awt.Font("Dialog", 1, 18));

		jLabelQuestionsOrderInfo1 = new JLabel(" Indicates the way in which the questions will be shown in the test.");
		jLabelQuestionsOrderInfo1.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelAnswersOrder = new JLabel(" Order of the answers: ");
		jLabelAnswersOrder.setFont(new java.awt.Font("Dialog", 1, 18));

		jLabelAnswersOrderInfo1 = new JLabel(" Indicates the way in which the answers will be shown in the test.");
		jLabelAnswersOrderInfo1.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelShowQuestionCorrection = new JLabel(
				" Indicates if the result of evaluating the question will be shown.");
		jLabelShowQuestionCorrection.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelVerbose = new JLabel(" Indicates if the explanation of the answers will be "
				+ "shown when they are corrected. ");
		jLabelVerbose.setFont(new java.awt.Font(" Dialog", 1, 12));

		jLabelShowCorrectAnswers = new JLabel(" Indicates if the correct answers will be "
				+ "shown when the question is corrected. ");
		jLabelShowCorrectAnswers.setFont(new java.awt.Font(" Dialog", 1, 12));

		jLabelTimeToAnswerInfo = new JLabel(" (Seconds) ");
		jLabelTimeToAnswerInfo.setFont(new java.awt.Font("Dialog", 1, 12));
		if (Integer.valueOf(test.getTimeOfAnswer()).intValue() <= 0)
			jLabelTimeToAnswerInfo.setVisible(false);

		jLabelRepeatWithoutAnswerInfo = new JLabel(
				"Indicates if the questions not answered must be shown again to the student");
		jLabelRepeatWithoutAnswerInfo.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelShowFinalInfo = new JLabel(
				"Indicates if the test information will be shown when the student finishs it.");
		jLabelShowFinalInfo.setFont(new java.awt.Font("Dialog", 1, 12));

		// Reserva de memoria de los JLabels correspondiente al htmlParameters.
		jLabelTestName = new JLabel("Test Title     ");
		jLabelTestName.setFont(new java.awt.Font("Dialog", 1, 14));

		// Reserva de memoria para los JLabels correspondientest al
		// evaluationParameters.
		jLabelIncorrectAnswers = new JLabel(" Incorrect Answers: ");
		jLabelIncorrectAnswers.setFont(new java.awt.Font("Dialog", 1, 18));

		jLabelIncorrectAnswersPenalize1 = new JLabel(" Each ");
		jLabelIncorrectAnswersPenalize1.setFont(new java.awt.Font("Dialog", 1, 14));
		jLabelIncorrectAnswersPenalize2 = new JLabel(" incorrect questions, ");
		jLabelIncorrectAnswersPenalize2.setFont(new java.awt.Font("Dialog", 1, 14));
		jLabelIncorrectAnswersPenalize3 = new JLabel(" 1 correct question is subtracted. ");
		jLabelIncorrectAnswersPenalize3.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelWithoutAnswers = new JLabel(" Questions without answering: ");
		jLabelWithoutAnswers.setFont(new java.awt.Font("Dialog", 1, 18));

		jLabelWithoutAnswersPenalize1 = new JLabel(" Each ");
		jLabelWithoutAnswersPenalize1.setFont(new java.awt.Font("Dialog", 1, 14));
		jLabelWithoutAnswersPenalize2 = new JLabel(" questions without answering, ");
		jLabelWithoutAnswersPenalize2.setFont(new java.awt.Font("Dialog", 1, 14));
		jLabelWithoutAnswersPenalize3 = new JLabel(" 1 correct question is subtracted. ");
		jLabelWithoutAnswersPenalize3.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelKnowledge1 = new JLabel(" This parameters indicates the percentage of ");
		jLabelKnowledge1.setFont(new java.awt.Font("Dialog", 1, 12));
		jLabelKnowledge2 = new JLabel(" knowledge that the test represents whit ");
		jLabelKnowledge2.setFont(new java.awt.Font("Dialog", 1, 12));
		jLabelKnowledge3 = new JLabel(" regard to the evaluation of a concept. ");
		jLabelKnowledge3.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelKnowledgePercentage = new JLabel(" % ");
		jLabelKnowledgePercentage.setFont(new java.awt.Font("Dialog", 1, 14));

		// Reserva de memoria de los JLabel relacionados con los datos de los
		// estudiantes contenidos en el fichero de test.
		jLabelLogin = new JLabel("Login");
		jLabelLogin.setFont(new java.awt.Font("Dialog", 1, 20));

		jLabelTotalStudents = new JLabel("Number of students");
		jLabelTotalStudents.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelScore = new JLabel("Score");
		jLabelScore.setFont(new java.awt.Font("Dialog", 1, 18));

		jLabelProficiency = new JLabel("Proficiency");
		jLabelProficiency.setFont(new java.awt.Font("Dialog", 1, 18));

		jLabelStandardError = new JLabel("Standard error");
		jLabelStandardError.setFont(new java.awt.Font("Dialog", 1, 18));

		jLabelMake1 = new JLabel("Carried out test");
		jLabelMake1.setFont(new java.awt.Font("Dialog", 1, 18));

		jLabelMake2 = new JLabel("times");
		jLabelMake2.setFont(new java.awt.Font("Dialog", 1, 18));

		jLabelEnableTest = new JLabel("It disables the login from the " + "students to the test.");
		jLabelEnableTest.setFont(new java.awt.Font("Dialgo", 1, 12));

		// Reserva de memoria para los JLabel relacionados con las preguntas que
		// forman parte del test.
		jLabelTestType = new JLabel("Test Type");
		jLabelTestType.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelAbstractConcept = new JLabel("Abstract Concept");
		jLabelAbstractConcept.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelConcept = new JLabel("Evaluated Concepts");
		jLabelConcept.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelTotalQuestions = new JLabel("Number of questions in the test");
		jLabelTotalQuestions.setFont(new java.awt.Font("Dialog", 1, 14));

		// Reserva de memoria para los JLabel relacionados con las nuevas
		// preguntas
		// a a�adir al test.
		jLabelQuestionsFile = new JLabel("Question File Name");
		jLabelQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 14));

		// Reserva de memoria para los JLabel relacionados con los par�metros
		// adaptativos.
		jLabelInitialProficiency = new JLabel("Initial proficiency estimate");
		jLabelInitialProficiency.setFont(new java.awt.Font("Dialog", 1, 16));

		jLabelInitialProficiencyInfo = new JLabel("The students' estimated initial proficiency.");
		jLabelInitialProficiencyInfo.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabel1PInfo = new JLabel("(difficulty)");
		jLabel1PInfo.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabel2PInfo = new JLabel("(discrimination and difficulty)");
		jLabel2PInfo.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabel3PInfo = new JLabel("(discrimination, difficulty and guessing)");
		jLabel3PInfo.setFont(new java.awt.Font("Dialog", 1, 14));
	}

	/*
	 * NOMBRE: JCheckBoxInit(); PERTENECE A: Clase FrmEditTest LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JCheckBox del frame,
	 * reservando memoria para los mismoy estableciendo su tama�o y su
	 * funcionalidad.
	 */

	private void JCheckBoxInit() {
		// Reserva de memoria para el jCheckBoxShowInitialInfo.
		jCheckBoxShowInitialInfo = new JCheckBox("Show test presentation.", test.getShowInitialInfo());
		jCheckBoxShowInitialInfo.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxShowInitialInfo.setBackground(SystemColor.WHITE);

		// Establecimiento de la funcionalidad para jCheckBoxShowInitialInfo.
		jCheckBoxShowInitialInfo.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				presentationParametersModified = true;
			}
		});

		// Reserva de memoria para el jCheckBoxShowQuestionCorrection.
		jCheckBoxShowQuestionCorrection = new JCheckBox("Show question correction.", test
				.getShowQuestionCorrection());
		jCheckBoxShowQuestionCorrection.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxShowQuestionCorrection.setBackground(SystemColor.WHITE);

		// Establecimiento de la funcionalidad para
		// jCheckBoxTShowQuestionCorrection.
		jCheckBoxShowQuestionCorrection.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jCheckBoxShowQuestionCorrection.isSelected()) {
					jPanelVerbose.setVisible(true);
					jPanelShowCorrectAnswers.setVisible(true);
				} else {
					jPanelVerbose.setVisible(false);
					jPanelShowCorrectAnswers.setVisible(false);
				}
				presentationParametersModified = true;
			}
		});

		// Reserva de memoria para el jCheckBoxVerbose.
		jCheckBoxVerbose = new JCheckBox("Verbose", test.getVerbose());
		jCheckBoxVerbose.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxVerbose.setBackground(SystemColor.WHITE);

		// Establecimiento de la funcionalidad para jCheckBoxVerbose.
		jCheckBoxVerbose.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				presentationParametersModified = true;
			}
		});

		// Reserva de memoria para el jCheckBoxShowCorrectAnswers.
		jCheckBoxShowCorrectAnswers = new JCheckBox("Show Correct Answers", test.getShowCorrectAnswers());
		jCheckBoxShowCorrectAnswers.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxShowCorrectAnswers.setBackground(SystemColor.WHITE);

		// Establecimiento de la funcionalidad para jCheckBoxShowCorrectAnswers.
		jCheckBoxShowCorrectAnswers.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				presentationParametersModified = true;
			}
		});

		// Reserva de memoria para el jCheckBoxTimeToAnswer.
		jCheckBoxTimeToAnswer = new JCheckBox("Maximum time to answer the questions");

		if (Integer.valueOf(test.getTimeOfAnswer()).intValue() > 0)
			jCheckBoxTimeToAnswer.setSelected(true);
		else
			jCheckBoxTimeToAnswer.setSelected(false);

		jCheckBoxTimeToAnswer.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxTimeToAnswer.setBackground(SystemColor.WHITE);

		// Establecimiento de la funcionalidad para jCheckBoxTimeToAnswer.
		jCheckBoxTimeToAnswer.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jCheckBoxTimeToAnswer.isSelected()) {
					jSliderTimeToAnswer.setVisible(true);
					jTextFieldTimeToAnswer.setVisible(true);
					jLabelTimeToAnswerInfo.setVisible(true);
				} else {
					jSliderTimeToAnswer.setVisible(false);
					jTextFieldTimeToAnswer.setVisible(false);
					jLabelTimeToAnswerInfo.setVisible(false);
				}
				presentationParametersModified = true;
			}
		});

		// Reserva de memoria para el jCheckBoxRepeatWithoutAnswer.
		jCheckBoxRepeatWithoutAnswer = new JCheckBox("Repeat questions not answered", test
				.getRepeatWithoutAnswer());
		jCheckBoxRepeatWithoutAnswer.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxRepeatWithoutAnswer.setBackground(SystemColor.WHITE);

		// Establecimiento de la funcionalidad para jCheckBoxRepeatWithoutAnswer.
		jCheckBoxRepeatWithoutAnswer.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				presentationParametersModified = true;
			}
		});

		// Reserva de memoria para el jCheckBoxShowFinalInfo.
		jCheckBoxShowFinalInfo = new JCheckBox("Show final information of test", test.getShowFinalInfo());
		jCheckBoxShowFinalInfo.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxShowFinalInfo.setBackground(SystemColor.WHITE);

		// Establecimiento de la funcionalidad para jCheckBoxShowFinalInfo.
		jCheckBoxShowFinalInfo.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				presentationParametersModified = true;
			}
		});

		// Reserva de memoria para el jCheckBoxFinish.
		jCheckBoxFinish = new JCheckBox("Test Ended", false);
		jCheckBoxFinish.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxFinish.setBackground(SystemColor.WHITE);
		jCheckBoxFinish.setEnabled(false);

		// Establecimiento de la funcionalidad para jCheckBoxFinish.
		jCheckBoxFinish.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (jListLogin.getSelectedIndices().length == 1) {
					int index = jListLogin.getSelectedIndex();
					((StudentTest) studentTestVector.get(index)).setFinish(jCheckBoxFinish.isSelected());
				}
				studentsModified = true;
			}
		});

		// Establecimiento de la funcionalidad para jCheckBoxFinish.
		jCheckBoxFinish.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (jListLogin.getSelectedIndices().length == 1) {
						int index = jListLogin.getSelectedIndex();
						((StudentTest) studentTestVector.get(index)).setFinish(jCheckBoxFinish.isSelected());
					}
					studentsModified = true;
				}
			}
		});

		// Reserva de memoria para el jCheckBoxCanRepeat.
		jCheckBoxCanRepeat = new JCheckBox("Can repeat the test", false);
		jCheckBoxCanRepeat.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxCanRepeat.setBackground(SystemColor.WHITE);
		jCheckBoxCanRepeat.setEnabled(false);

		// Establecimiento de la funcionalidad para jCheckBoxCanRepeat.
		jCheckBoxCanRepeat.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (jListLogin.getSelectedIndices().length == 1) {
					int index = jListLogin.getSelectedIndex();
					((StudentTest) studentTestVector.get(index)).setCanRepeat(jCheckBoxCanRepeat.isSelected());
				}
				studentsModified = true;
			}
		});

		// Establecimiento de la funcionalidad para jCheckBoxFinish.
		jCheckBoxCanRepeat.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (jListLogin.getSelectedIndices().length == 1) {
					int index = jListLogin.getSelectedIndex();
					((StudentTest) studentTestVector.get(index)).setCanRepeat(jCheckBoxCanRepeat.isSelected());
				}
				studentsModified = true;
			}
		});

		// Reserva de memoria para el jCheckBoxDeleteStudent.
		jCheckBoxDeleteStudent = new JCheckBox("Delete", false);
		jCheckBoxDeleteStudent.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxDeleteStudent.setBackground(SystemColor.WHITE);
		jCheckBoxDeleteStudent.setEnabled(false);

		// Establecimiento de la funcionalidad para jCheckBoxDeleteStudent.
		jCheckBoxDeleteStudent.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (jListLogin.getSelectedIndices().length == 1) {
					int index = jListLogin.getSelectedIndex();
					if (jCheckBoxDeleteStudent.isSelected())
						deleteStudentVector.setElementAt("true", index);
					else
						deleteStudentVector.setElementAt("false", index);
				}
				studentsModified = true;
			}
		});

		// Establecimiento de la funcionalidad para jCheckBoxDeleteStudent.
		jCheckBoxDeleteStudent.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (jListLogin.getSelectedIndices().length == 1) {
					int index = jListLogin.getSelectedIndex();
					if (jCheckBoxDeleteStudent.isSelected())
						deleteStudentVector.setElementAt("true", index);
					else
						deleteStudentVector.setElementAt("false", index);
				}
				studentsModified = true;
			}
		});

		// Reserva de memoria para el jCheckBoxEnableTest.
		jCheckBoxEnableTest = new JCheckBox("Enable Test", test.getEnable());
		jCheckBoxEnableTest.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxEnableTest.setBackground(SystemColor.WHITE);

		// Establecimiento de la funcionalidad para jCheckBoxEnableTest.
		jCheckBoxEnableTest.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				enableModified = true;
			}
		});
	}

	/*
	 * NOMBRE: JRadioButtonInit. PERTENECE A: Clase FrmEditTest. LLAMADA POR: El
	 * constructor de la clase al que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JRadioButton del frame.
	 */

	private void JRadioButtonInit() {
		jComboBoxQuestionsOrder = new JComboBox();
		jComboBoxQuestionsOrder.setFont(new java.awt.Font("Dialog", 1, 12));
		jComboBoxQuestionsOrder.setBackground(SystemColor.LIGHT_GRAY);
		jComboBoxQuestionsOrder.insertItemAt("Sequential", 0);
		jComboBoxQuestionsOrder.insertItemAt("Random", 1);
		if (test.getQuestionsOrder().equals(TestEditor.RANDOM_STR)) {
			jComboBoxQuestionsOrder.setSelectedIndex(1);
		} else {
			jComboBoxQuestionsOrder.setSelectedIndex(0);
		}
		jComboBoxQuestionsOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presentationParametersModified = true;
			}
		});

		jComboBoxAnswersOrder = new JComboBox();
		jComboBoxAnswersOrder.setFont(new java.awt.Font("Dialog", 1, 12));
		jComboBoxAnswersOrder.setBackground(SystemColor.LIGHT_GRAY);
		jComboBoxAnswersOrder.insertItemAt("Sequential", 0);
		jComboBoxAnswersOrder.insertItemAt("Random", 1);
		if (test.getAnswersOrder().equals(TestEditor.RANDOM_STR)) {
			jComboBoxAnswersOrder.setSelectedIndex(1);
		} else {
			jComboBoxAnswersOrder.setSelectedIndex(0);
		}
		jComboBoxAnswersOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presentationParametersModified = true;
			}
		});

		jRadioButtonIncorrectAnswersPenalizeYes = new JRadioButton("Penalize");
		jRadioButtonIncorrectAnswersPenalizeYes.setFont(new java.awt.Font("Dialog", 1, 14));
		jRadioButtonIncorrectAnswersPenalizeYes.setBackground(SystemColor.WHITE);

		jRadioButtonIncorrectAnswersPenalizeNo = new JRadioButton("Not Penalized");
		jRadioButtonIncorrectAnswersPenalizeNo.setFont(new java.awt.Font("Dialog", 1, 14));
		jRadioButtonIncorrectAnswersPenalizeNo.setBackground(SystemColor.WHITE);

		if (test.getIncorrectAnswersPenalize() == true)
			jRadioButtonIncorrectAnswersPenalizeYes.setSelected(true);
		else
			jRadioButtonIncorrectAnswersPenalizeNo.setSelected(true);

		jRadioButtonWithoutAnswersPenalizeYes = new JRadioButton("Penalize");
		jRadioButtonWithoutAnswersPenalizeYes.setFont(new java.awt.Font("Dialog", 1, 14));
		jRadioButtonWithoutAnswersPenalizeYes.setBackground(SystemColor.WHITE);

		jRadioButtonWithoutAnswersPenalizeNo = new JRadioButton("Not Penalized");
		jRadioButtonWithoutAnswersPenalizeNo.setFont(new java.awt.Font("Dialog", 1, 14));
		jRadioButtonWithoutAnswersPenalizeNo.setBackground(SystemColor.WHITE);

		if (test.getWithoutAnswersPenalize() == true)
			jRadioButtonWithoutAnswersPenalizeYes.setSelected(true);
		else
			jRadioButtonWithoutAnswersPenalizeNo.setSelected(true);

		// Reserva de memoria para los jRadioButton relacionados con la ejecuci�n
		// adaptativa.
		jRadioButton1P = new JRadioButton("1 Parameter");
		jRadioButton1P.setFont(new java.awt.Font("Dialog", 1, 18));
		jRadioButton1P.setBackground(SystemColor.WHITE);

		jRadioButton2P = new JRadioButton("2 Parameters");
		jRadioButton2P.setFont(new java.awt.Font("Dialog", 1, 18));
		jRadioButton2P.setBackground(SystemColor.WHITE);

		jRadioButton3P = new JRadioButton("3 Parameters");
		jRadioButton3P.setFont(new java.awt.Font("Dialog", 1, 18));
		jRadioButton3P.setBackground(SystemColor.WHITE);

		if (test.getExecutionType().equals(TestEditor.ADAPTIVE)) {
			if (test.getIrtModel() == 3)
				jRadioButton3P.setSelected(true);
			else if (test.getIrtModel() == 2)
				jRadioButton2P.setSelected(true);
			else
				jRadioButton3P.setSelected(true);
		} else
			jRadioButton3P.setSelected(true);

		jRadioButtonStandardError = new JRadioButton("Standard error");
		jRadioButtonStandardError.setFont(new java.awt.Font("Dialog", 1, 18));

		jRadioButtonNumberItems = new JRadioButton("Number of items administred");
		jRadioButtonNumberItems.setFont(new java.awt.Font("Dialog", 1, 18));

		if (test.getExecutionType().equals(TestEditor.ADAPTIVE)) {
			if (test.getIrtStopCriterion().equals("standardError"))
				jRadioButtonStandardError.setSelected(true);
			else if (test.getIrtStopCriterion().equals("numberItemsAdministred"))
				jRadioButtonNumberItems.setSelected(true);
		} else
			jRadioButtonStandardError.setSelected(true);

		// Establecimiento de la funcionalidad de
		// jRadioButtonIncorrectAnswersPenalizeYes.
		jRadioButtonIncorrectAnswersPenalizeYes.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jRadioButtonIncorrectAnswersPenalizeYes.isSelected())
					jPanelIncorrectAnswersPenalize.setVisible(true);
				else
					jPanelIncorrectAnswersPenalize.setVisible(false);

				evaluationParametersModified = true;
			}
		});

		// Establecimiento de la funcionalidad de
		// jRadioButtonIncorrectAnswersPenalizeYes.
		jRadioButtonIncorrectAnswersPenalizeNo.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				evaluationParametersModified = true;
			}
		});

		// Establecimiento de la funcionalidad de
		// jRadioButtonWithoutAnswersPenalizeYes.
		jRadioButtonWithoutAnswersPenalizeYes.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jRadioButtonWithoutAnswersPenalizeYes.isSelected())
					jPanelWithoutAnswersPenalize.setVisible(true);
				else
					jPanelWithoutAnswersPenalize.setVisible(false);

				evaluationParametersModified = true;
			}
		});

		// Establecimiento de la funcionalidad de
		// jRadioButtonWithoutAnswersPenalizeNo.
		jRadioButtonWithoutAnswersPenalizeNo.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				evaluationParametersModified = true;
			}
		});

		// Establecimiento de la funcionalidad de
		// jRadioButton1P.
		jRadioButton1P.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				irtParametersModified = true;
			}
		});

		// Establecimiento de la funcionalidad de
		// jRadioButton2P.
		jRadioButton2P.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				irtParametersModified = true;
			}
		});

		// Establecimiento de la funcionalidad de
		// jRadioButton3P.
		jRadioButton3P.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				irtParametersModified = true;
			}
		});

		// Establecimiento de la funcionalidad de
		// jRadioButtonStandardError.
		jRadioButtonStandardError.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jRadioButtonStandardError.isSelected())
					jPanelStandardError.setVisible(true);
				else
					jPanelStandardError.setVisible(false);

				irtParametersModified = true;
			}
		});

		// Establecimiento de la funcionalidad de
		// jRadioButtonNumberItems.
		jRadioButtonNumberItems.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jRadioButtonNumberItems.isSelected())
					jPanelNumberItems.setVisible(true);
				else
					jPanelNumberItems.setVisible(false);

				irtParametersModified = true;
			}
		});
		jRadioButtonIncorrectAnswersGroup = new ButtonGroup();
		jRadioButtonIncorrectAnswersGroup.add(jRadioButtonIncorrectAnswersPenalizeYes);
		jRadioButtonIncorrectAnswersGroup.add(jRadioButtonIncorrectAnswersPenalizeNo);

		jRadioButtonWithoutAnswersGroup = new ButtonGroup();
		jRadioButtonWithoutAnswersGroup.add(jRadioButtonWithoutAnswersPenalizeYes);
		jRadioButtonWithoutAnswersGroup.add(jRadioButtonWithoutAnswersPenalizeNo);

		jRadioButtonIrtModelGroup = new ButtonGroup();
		jRadioButtonIrtModelGroup.add(jRadioButton1P);
		jRadioButtonIrtModelGroup.add(jRadioButton2P);
		jRadioButtonIrtModelGroup.add(jRadioButton3P);

		jRadioButtonStopCriterionGroup = new ButtonGroup();
		jRadioButtonStopCriterionGroup.add(jRadioButtonStandardError);
		jRadioButtonStopCriterionGroup.add(jRadioButtonNumberItems);
	}

	/*
	 * NOMBRE: JSliderInit. PERTENECE A: Clase FrmEditTest. LLAMADA POR: El
	 * Constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JSlider del frame.
	 */

	private void JSliderInit() {
		int timeToAnswer = Integer.valueOf(test.getTimeOfAnswer()).intValue();
		// Reserva de memoria para el jSliderTimeToAnswer.
		jSliderTimeToAnswer = new JSlider(JSlider.HORIZONTAL, ANSWER_TIME_MIN + 20, ANSWER_TIME_MAX,
				ANSWER_TIME_MIN + 20);

		if (timeToAnswer > 0)
			jSliderTimeToAnswer.setValue(timeToAnswer);
		else {
			jSliderTimeToAnswer.setValue(ANSWER_TIME_MIN + 20);
			jSliderTimeToAnswer.setVisible(false);
		}

		jSliderTimeToAnswer.setPaintTicks(true);
		jSliderTimeToAnswer.setPaintLabels(true);
		jSliderTimeToAnswer.setMajorTickSpacing(10);
		jSliderTimeToAnswer.setMinorTickSpacing(1);

		jSliderTimeToAnswer.setBackground(SystemColor.WHITE);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderDifficultyMax.
		Dictionary jSliderTimeToAnswerLabelTable = new Hashtable();
		jSliderTimeToAnswerLabelTable.put(new Integer(ANSWER_TIME_MIN + 20), new JLabel(String
				.valueOf(ANSWER_TIME_MIN + 20)));

		for (int i = ANSWER_TIME_MIN + 20; i <= ANSWER_TIME_MAX; i++) {
			if (i % 100 == 0)
				jSliderTimeToAnswerLabelTable.put(new Integer(i), new JLabel(String.valueOf(i)));
		}
		jSliderTimeToAnswer.setLabelTable(jSliderTimeToAnswerLabelTable);

		// Establecimiento de la funcionalidad de jSliderTimeToAnswer.
		jSliderTimeToAnswer.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jTextFieldTimeToAnswer.setText(String.valueOf(jSliderTimeToAnswer.getValue()));

				presentationParametersModified = true;
			}
		});

		// Reserva de memoria para el jSliderKnowledge.
		jSliderKnowledge = new JSlider(JSlider.HORIZONTAL, 0, 100, Integer.valueOf(
				test.getKnowledgePercentage()).intValue());
		jSliderKnowledge.setPaintTicks(true);
		jSliderKnowledge.setPaintLabels(true);
		jSliderKnowledge.setMajorTickSpacing(10);
		jSliderKnowledge.setMinorTickSpacing(1);

		jSliderKnowledge.setBackground(SystemColor.WHITE);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderDifficultyMax.
		Dictionary jSliderKnowledgeLabelTable = new Hashtable();
		for (int i = 0; i <= 100; i++) {
			if (i % 10 == 0)
				jSliderKnowledgeLabelTable.put(new Integer(i), new JLabel(String.valueOf(i)));
		}
		jSliderKnowledge.setLabelTable(jSliderKnowledgeLabelTable);

		// Establecimiento de la funcionalidad de jSliderKnowledge.
		jSliderKnowledge.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jTextFieldKnowledge.setText(String.valueOf(jSliderKnowledge.getValue()));
				evaluationParametersModified = true;
			}
		});

		// Reserva de memoria para el jSliderInitialProficiency.
		jSliderInitialProficiency = new JSlider(JSlider.HORIZONTAL, (INITIAL_PROFICIENCY_MIN * 10),
				(INITIAL_PROFICIENCY_MAX * 10), (int) (test.getIrtInitialProficiency() * 10));
		jSliderInitialProficiency.setPaintTicks(true);
		jSliderInitialProficiency.setPaintLabels(true);
		jSliderInitialProficiency.setMajorTickSpacing(10);
		jSliderInitialProficiency.setMinorTickSpacing(1);
		jSliderInitialProficiency.setBackground(SystemColor.WHITE);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderInitialProficiency.
		Dictionary jSliderInitialProficiencyLabelTable = new Hashtable();
		for (int i =  (INITIAL_PROFICIENCY_MIN * 10); i <= (INITIAL_PROFICIENCY_MAX * 10); i++) {
			if (i % 10 == 0)
				jSliderInitialProficiencyLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 10)));
		}

		jSliderInitialProficiency.setLabelTable(jSliderInitialProficiencyLabelTable);

		// Establecimiento de la funcionalidad de jSliderInitialProficiency.
		jSliderInitialProficiency.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jTextFieldInitialProficiency.setText(String.valueOf(jSliderInitialProficiency.getValue() / 10.0));
				irtParametersModified = true;
			}
		});

		// Reserva de memoria para el jSliderStandardError.
		jSliderStandardError = new JSlider(JSlider.HORIZONTAL, (STANDARD_ERROR_MIN * 100),
				(STANDARD_ERROR_MAX * 100), (int) (test.getIrtStandardError() * 100));
		jSliderStandardError.setPaintTicks(true);
		jSliderStandardError.setPaintLabels(true);
		jSliderStandardError.setMajorTickSpacing(10);
		jSliderStandardError.setMinorTickSpacing(1);
		jSliderStandardError.setBackground(SystemColor.WHITE);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderStandardError.
		Dictionary jSliderStandardErrorLabelTable = new Hashtable();
		for (int i = (STANDARD_ERROR_MIN * 100); i <= (STANDARD_ERROR_MAX * 100); i++) {
			if (i % 20 == 0)
				jSliderStandardErrorLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 100.0)));
		}

		jSliderStandardError.setLabelTable(jSliderStandardErrorLabelTable);

		// Establecimiento de la funcionalidad de jSliderStandardError.
		jSliderStandardError.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jTextFieldStandardError.setText(String.valueOf(jSliderStandardError.getValue() / 100.0));
				irtParametersModified = true;
			}
		});
	}

	/*
	 * NOMBRE: JSliderInit1. PERTENECE A: Clase FrmEditTest. LLAMADA POR: El
	 * Constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JSlider del frame.
	 */

	private void JSliderInit1() {
		int contQuestionsTest = 0;

		// Reserva de memoria para el jSliderNumberItems.
		if (questionsOfMultipleFiles == null || questionsOfMultipleFiles.isEmpty()) {
			jSliderNumberItems = new JSlider(JSlider.HORIZONTAL, 1, test.getTotalQuestion(), test
					.getTotalQuestion());
			contQuestionsTest = test.getTotalQuestion();
		} else {
			for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
				Vector nameAndQuestions = (Vector) questionsOfMultipleFiles.get(i);
				Vector questionsVector = (Vector) nameAndQuestions.get(1);

				contQuestionsTest = contQuestionsTest + questionsVector.size();
			}

			jSliderNumberItems = new JSlider(JSlider.HORIZONTAL, 1, contQuestionsTest, contQuestionsTest);
		}

		jSliderNumberItems.setPaintTicks(true);
		jSliderNumberItems.setPaintLabels(true);
		jSliderNumberItems.setPaintTrack(true);
		if (contQuestionsTest <= 10) {
			jSliderNumberItems.setMajorTickSpacing(1);
			jSliderNumberItems.setMinorTickSpacing(1);
		} else {
			if (contQuestionsTest <= 100) {
				jSliderNumberItems.setMajorTickSpacing(10);
				jSliderNumberItems.setMinorTickSpacing(1);
			} else {
				jSliderNumberItems.setMajorTickSpacing(50);
				jSliderNumberItems.setMinorTickSpacing(10);
			}
		}
		jSliderNumberItems.setBackground(SystemColor.WHITE);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderNumberItems.
		Dictionary jSliderNumberItemsLabelTable = new Hashtable();

		for (int i = 1; i <= contQuestionsTest; i++) {
			if (contQuestionsTest <= 10)
				jSliderNumberItemsLabelTable.put(new Integer(i), new JLabel(String.valueOf(i)));
			else {
				if (contQuestionsTest <= 100) {
					if (i % 10 == 0 || i == 1 || i == contQuestionsTest)
						jSliderNumberItemsLabelTable.put(new Integer(i), new JLabel(String.valueOf(i)));
				} else {
					if (i % 50 == 0 || i == 1 || i == contQuestionsTest)
						jSliderNumberItemsLabelTable.put(new Integer(i), new JLabel(String.valueOf(i)));

				}
			}
		}

		if (jSliderNumberItemsLabelTable != null && jSliderNumberItemsLabelTable.isEmpty() == false)
			jSliderNumberItems.setLabelTable(jSliderNumberItemsLabelTable);

		// Establecimiento de la funcionalidad de jSliderNumberItems.
		jSliderNumberItems.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jTextFieldNumberItems.setText(String.valueOf(jSliderNumberItems.getValue()));
				irtParametersModified = true;
			}
		});

	}

	/*
	 * NOMBRE: JTextFieldInit. PERTENECE A: Clase FrmEditTest. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa el jTextField del frame.
	 */

	private void JTextFieldInit() {
		int timeToAnswer = Integer.valueOf(test.getTimeOfAnswer()).intValue();
		// Reserva de memoria para jTextFieldTimeToAnswer.
		jTextFieldTimeToAnswer = new JTextField(4);

		if (timeToAnswer > 0)
			jTextFieldTimeToAnswer.setText(String.valueOf(timeToAnswer));
		else {
			jTextFieldTimeToAnswer.setText(String.valueOf(ANSWER_TIME_MIN + 20));
			jTextFieldTimeToAnswer.setVisible(false);
		}

		// Establecimiento de la funcionalidad de jTextFieldTimeToAnswer.
		jTextFieldTimeToAnswer.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldTimeToAnswer.getText().length() > 0) {
						if (Integer.valueOf(jTextFieldTimeToAnswer.getText()).intValue() < ANSWER_TIME_MIN + 20) {
							jTextFieldTimeToAnswer.setText(String.valueOf(ANSWER_TIME_MIN + 20));
							jSliderTimeToAnswer.setValue(ANSWER_TIME_MIN + 20);
						} else {
							if (Integer.valueOf(jTextFieldTimeToAnswer.getText()).intValue() > ANSWER_TIME_MAX) {
								jTextFieldTimeToAnswer.setText(String.valueOf(ANSWER_TIME_MAX));
								jSliderTimeToAnswer.setValue(ANSWER_TIME_MAX);
							} else {
								jSliderTimeToAnswer.setValue(Integer.valueOf(jTextFieldTimeToAnswer.getText())
										.intValue());
							}
						}
					}

					presentationParametersModified = true;
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldTimeToAnswer.setText(String.valueOf(ANSWER_TIME_MIN + 20));
					jSliderTimeToAnswer.setValue(ANSWER_TIME_MIN + 20);
				}
			}
		});

		// Reserva de memoria para jTextFieldTestName.
		jTextFieldTestName = new JTextField(test.getTestName());
		jTextFieldTestName.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextFieldTestName.setFocusable(true);

		// Reserva de memoria para jTextFieldBgColor.
		jTextFieldBgColor = new JTextField(test.getBgColor());
		jTextFieldBgColor.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextFieldBgColor.setFocusable(false);

		// Reserva de memoria para jTextFieldBackground.
		jTextFieldBackground = new JTextField(10);
		jTextFieldBackground.setText(test.getBackground().substring(test.getBackground().lastIndexOf("/") + 1));
		jTextFieldBackground.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextFieldBackground.setFocusable(false);

		// Reserva de memoria para jTextFieldTitleColor.
		jTextFieldTitleColor = new JTextField(test.getTitleColor());
		jTextFieldTitleColor.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextFieldTitleColor.setFocusable(false);

		// Reserva de memoria para jTextFieldKnowledge;
		jTextFieldKnowledge = new JTextField(test.getKnowledgePercentage(), 4);

		// Establecimiento de la funcionalidad de jTextFieldKnowledge.
		jTextFieldKnowledge.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldKnowledge.getText().length() > 0) {
						if (Integer.valueOf(jTextFieldKnowledge.getText()).intValue() < 0) {
							jTextFieldKnowledge.setText("0");
							jSliderKnowledge.setValue(0);
						} else {
							if (Integer.valueOf(jTextFieldKnowledge.getText()).intValue() > 100) {
								jTextFieldKnowledge.setText("100");
								jSliderKnowledge.setValue(100);
							} else {
								jSliderKnowledge.setValue(Integer.valueOf(jTextFieldKnowledge.getText()).intValue());
							}
						}
					}

					evaluationParametersModified = true;
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldKnowledge.setText("100");
					jSliderKnowledge.setValue(100);
				}
			}
		});

		// Reserva de memoria para jTextFieldTotalQuestions.
		int totalStudents = 0;
		if (test.getStudentVector() != null)
			totalStudents = test.getStudentVector().size();

		jTextFieldTotalStudents = new JTextField(String.valueOf(totalStudents), 5);
		jTextFieldTotalStudents.setFont(new java.awt.Font("Dialog", 1, 12));
		jTextFieldTotalStudents.setFocusable(false);

		// Reserva de memoria para jTextFieldScore.
		jTextFieldScore = new JTextField(5);
		jTextFieldScore.setFont(new java.awt.Font("Dialog", 1, 12));
		jTextFieldScore.setFocusable(false);

		// Reserva de memoria para jTextFieldProficiency.
		jTextFieldProficiency = new JTextField(15);
		jTextFieldProficiency.setFont(new java.awt.Font("Dialog", 1, 12));
		jTextFieldProficiency.setFocusable(false);

		// Reserva de memoria para jTextFieldStandardError.
		jTextFieldStudentStandardError = new JTextField(15);
		jTextFieldStudentStandardError.setFont(new java.awt.Font("Dialog", 1, 12));
		jTextFieldStudentStandardError.setFocusable(false);

		// Reserva de memoria para jTextFieldMake.
		jTextFieldMake = new JTextField(5);
		jTextFieldMake.setFont(new java.awt.Font("Dialog", 1, 12));
		jTextFieldMake.setFocusable(false);

		// Reserva de memoria para el jTextFieldTestType.
		jTextFieldTestType = new JTextField(test.getTestType(), 15);
		jTextFieldTestType.setFont(new java.awt.Font("Dialog", 1, 14));
		jTextFieldTestType.setFocusable(false);

		// Reserva de memoria para el jTextFieldInitialProficiency.
		jTextFieldInitialProficiency = new JTextField(String.valueOf(test.getIrtInitialProficiency()), 4);
		jTextFieldInitialProficiency.setFont(new java.awt.Font("Dialog", 0, 14));

		// Reserva de memoria para el jTextFieldStandardError.
		jTextFieldStandardError = new JTextField(String.valueOf(test.getIrtStandardError()), 4);
		jTextFieldStandardError.setFont(new java.awt.Font("Dialog", 0, 14));

		// Establecimiento de la funcionalidad de jTextFieldInitialProficiency.
		jTextFieldInitialProficiency.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldInitialProficiency.getText().length() > 0) {
						if (Double.valueOf(jTextFieldInitialProficiency.getText()).doubleValue() < INITIAL_PROFICIENCY_MIN) {
							jTextFieldInitialProficiency.setText(String.valueOf(INITIAL_PROFICIENCY_MIN));
							jSliderInitialProficiency.setValue(INITIAL_PROFICIENCY_MIN * 10);
						} else {
							if (Double.valueOf(jTextFieldInitialProficiency.getText()).doubleValue() > INITIAL_PROFICIENCY_MAX) {
								jTextFieldInitialProficiency.setText(String.valueOf(INITIAL_PROFICIENCY_MAX));
								jSliderInitialProficiency.setValue(INITIAL_PROFICIENCY_MAX * 10);
							} else {
								jSliderInitialProficiency.setValue((int) (Double.valueOf(
										jTextFieldInitialProficiency.getText()).doubleValue() * 10));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldInitialProficiency.setText(String.valueOf(INITIAL_PROFICIENCY_DEFAULT));
					jSliderInitialProficiency.setValue((int) (INITIAL_PROFICIENCY_DEFAULT * 10));
				}
				irtParametersModified = true;
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldStandardError.
		jTextFieldStandardError.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldStandardError.getText().length() > 0) {
						if (Double.valueOf(jTextFieldStandardError.getText()).doubleValue() < STANDARD_ERROR_MIN) {
							jTextFieldStandardError.setText(String.valueOf(STANDARD_ERROR_MIN));
							jSliderStandardError.setValue(STANDARD_ERROR_MIN * 100);
						} else {
							if (Double.valueOf(jTextFieldStandardError.getText()).doubleValue() > STANDARD_ERROR_MAX) {
								jTextFieldStandardError.setText(String.valueOf(STANDARD_ERROR_MAX));
								jSliderStandardError.setValue(STANDARD_ERROR_MAX * 100);
							} else {
								jSliderStandardError.setValue((int) (Double
										.valueOf(jTextFieldStandardError.getText()).doubleValue() * 100));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldStandardError.setText(String.valueOf(STANDARD_ERROR_DEFAULT));
					jSliderStandardError.setValue((int) (STANDARD_ERROR_DEFAULT * 100));
				}
				irtParametersModified = true;
			}
		});
	}

	/*
	 * NOMBRE: JTextFieldInit1. PERTENECE A: Clase FrmEditTest. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa el jTextField del frame.
	 */

	private void JTextFieldInit1() {
		int contQuestionsTest = 0;

		// Reserva de memoria para el jTextFieldAbstactConcept.
		if (test.getTestType().trim().equals("exam")) {
			jTextFieldAbstractConcept = new JTextField(test.getAbstractConcept().substring(
					test.getAbstractConcept().indexOf(test.getCourse()) + test.getCourse().length() + 1), 20);
			jTextFieldAbstractConcept.setFont(new java.awt.Font("Dialog", 1, 14));
			jTextFieldAbstractConcept.setFocusable(false);
		} else {
			if (conceptVectorInitial.firstElement().toString().indexOf(test.getCourse()) == 0)
				jTextFieldConcept = new JTextField(conceptVectorInitial.firstElement().toString().substring(
						conceptVectorInitial.firstElement().toString().indexOf(test.getCourse())
								+ test.getCourse().length() + 1), 15);
			else
				jTextFieldConcept = new JTextField(conceptVectorInitial.firstElement().toString());

			jTextFieldConcept.setFont(new java.awt.Font("Dialog", 1, 14));
			jTextFieldConcept.setFocusable(false);
		}

		// Reserva de memoria para el jTextFieldNumberItems.
		if (questionsOfMultipleFiles == null || questionsOfMultipleFiles.isEmpty())
			jTextFieldNumberItems = new JTextField(String.valueOf(test.getTotalQuestion()), 4);
		else {
			contQuestionsTest = 0;
			for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
				Vector nameAndQuestions = (Vector) questionsOfMultipleFiles.get(i);
				Vector questionsVector = (Vector) nameAndQuestions.get(1);

				contQuestionsTest = contQuestionsTest + questionsVector.size();
			}
			jTextFieldNumberItems = new JTextField(String.valueOf(contQuestionsTest), 4);
		}

		jTextFieldNumberItems.setFont(new java.awt.Font("Dialog", 0, 14));

		final int contQuestionsTestFinal = contQuestionsTest;

		// Establecimiento de la funcionalidad de jTextFieldNumberItems.
		jTextFieldNumberItems.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldNumberItems.getText().length() > 0) {
						if (Integer.valueOf(jTextFieldNumberItems.getText()).intValue() < 1) {
							jTextFieldNumberItems.setText(String.valueOf(1));
							jSliderNumberItems.setValue(1);
						} else {
							if (Integer.valueOf(jTextFieldNumberItems.getText()).intValue() > contQuestionsTestFinal) {
								jTextFieldNumberItems.setText(String.valueOf(contQuestionsTestFinal));
								jSliderNumberItems.setValue(contQuestionsTestFinal);
							} else {
								jSliderNumberItems.setValue(Integer.valueOf(jTextFieldNumberItems.getText())
										.intValue());
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldNumberItems.setText(String.valueOf(contQuestionsTestFinal));
					jSliderNumberItems.setValue(contQuestionsTestFinal);
				}
				irtParametersModified = true;
			}
		});

		// Reserva de memoria para el jTextFieldTotalQuestions.

		// Bucle comprobar cuantas preguntas forman el test.
		if (questionsOfMultipleFiles != null) {
			int totalQuestions = 0;
			for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
				Vector questionsVector = (Vector) ((Vector) questionsOfMultipleFiles.get(i)).get(1);
				totalQuestions = totalQuestions + questionsVector.size();
			}

			jTextFieldTotalQuestions = new JTextField(String.valueOf(totalQuestions), 20);
		} else
			jTextFieldTotalQuestions = new JTextField(String.valueOf(test.getTotalQuestion()), 5);

		jTextFieldTotalQuestions.setFont(new java.awt.Font("Dialog", 1, 14));
		jTextFieldTotalQuestions.setFocusable(false);
	}

	/*
	 * NOMBRE: JEditorPaneInit. PERTENECE A: Clase FrmEditTest. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JEditorPane del frame.
	 */

	private void JEditorPaneInit() {
		// Reserva de memoria para jEditorPaneHtmlParameters.
		jEditorPaneHtmlParameters = new JEditorPane();
		jEditorPaneHtmlParameters.setSize(500, 400);
		jEditorPaneHtmlParameters.setContentType("text/html;charset=UTF-8");
		jEditorPaneHtmlParameters.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
	}

	/*
	 * NOMBRE: getEditorPaneHtmlParametersText(). PERTENECE A: Clase FrmEditTest.
	 * LLAMADA POR: JEditorPaneInit() y al hacer click sobre jButtonSetBgColor,
	 * jButtonResetBgColor, jButtonSetBackground, jButtonSetBackground,
	 * jButtonSetTitleColor, jButtonResetTitleColor;
	 * jButtonResetAllHtmlParameters. LLAMA A: nada. RECIBE: void. DEVUELVE:
	 * void. FUNCI�N: Establece el texto del jEditorPaneHtmlParameters.
	 */

	private String getEditorPaneHtmlParametersText() {
		URL urlBackground = null;

		String editorPaneHtmlParametersText = "<html>\n" + "<head>\n" + "</head>\n" + "<body bgcolor=\""
				+ jTextFieldBgColor.getText().trim() + "\" leftmargin=\"0\" topmargin=\"0\" "
				+ "marginwidth=\"0\" marginheight=\"0\" ";

		try {
			String backgroundString = wowPath + parent.getItemsPath() + test.getBackground();

			urlBackground = new URL(codeBase, backgroundString);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "It is not possible to load the image: " + e.getMessage(),
					"Error on load image", JOptionPane.ERROR_MESSAGE);
		}

		if (backgroundFile != null)
			editorPaneHtmlParametersText = editorPaneHtmlParametersText.concat("background=\"file:///"
					+ backgroundFile.getAbsolutePath() + "\">\n");
		else if (imageDeleted == false)
			editorPaneHtmlParametersText = editorPaneHtmlParametersText.concat("background=\"" + urlBackground
					+ "\">\n");
		else
			editorPaneHtmlParametersText = editorPaneHtmlParametersText.concat("background=\"\">\n");

		editorPaneHtmlParametersText = editorPaneHtmlParametersText.concat("<center>\n" + "<br>\n"
				+ "<table width=\"450\" height=\"400\" border=\"0\" cellpadding=\"0\" " + "cellspacing=\"0\">\n"
				+ "<tr>\n" + "<td>\n" + "<center>\n" + "<strong><font size=\"7\" color=\""
				+ jTextFieldTitleColor.getText().trim() + "\">" + jTextFieldTestName.getText().trim()
				+ "</font><strong>\n" + "<p>&nbsp;</p>\n" + "</center>\n" + "</td>\n" + "</tr>\n" + "<tr>\n"
				+ "<td>\n" + "<center>\n");

		try {
			// String host = codeBase.getHost();
			String petition = wowPath + "/author/TestEditor/icon/applet.gif";
			URL url = new URL(codeBase, petition);

			editorPaneHtmlParametersText = editorPaneHtmlParametersText.concat("<img src=\"" + url.toString()
					+ "\"" + "width=\"350\" height=\"247\">" + "</img>\n");
		} catch (java.net.MalformedURLException mue) {}

		editorPaneHtmlParametersText = editorPaneHtmlParametersText.concat("</center>\n" + "</td>\n"
				+ "</tr>\n" + "</table>\n" + "</center>\n" + "</body>\n" + "</html>\n");

		return editorPaneHtmlParametersText;
	}

	/*
	 * NOMBRE: JComboBoxInit() PERTENECE A: Clase FrmEditTest LLAMADA POR: El
	 * constructor de la clase a la que pertenence. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JComboBox del frame,
	 * reservando memoria para los mismoy estableciendo su tama�o y su
	 * funcionalidad.
	 */

	private void JComboBoxInit() {
		// Reserva de memoria para los JComboBox.
		jComboBoxIncorrectAnswers = new JComboBox();
		jComboBoxWithoutAnswers = new JComboBox();
		jComboBoxQuestionsFile = new JComboBox();

		// Borrado de sus contenidos previos.
		jComboBoxIncorrectAnswers.removeAllItems();
		jComboBoxWithoutAnswers.removeAllItems();
		jComboBoxQuestionsFile.removeAllItems();

		jComboBoxQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 14));

		// Adici�n a jComboBoxIncorrectAnswer y jComboBoxWithoutAnswers
		// de sus valores.
		for (int i = TestEditor.NUMBER_OF_ANSWERS_MIN; i <= TestEditor.NUMBER_OF_ANSWERS_MAX; i++) {
			jComboBoxIncorrectAnswers.addItem(String.valueOf(i));
			jComboBoxWithoutAnswers.addItem(String.valueOf(i));
		}

		if (indexComboIncorrect == -1) {
			if (test.getIncorrectAnswersPenalizeNumber() > 0) {
				jComboBoxIncorrectAnswers.setSelectedItem(String
						.valueOf(test.getIncorrectAnswersPenalizeNumber()));

				indexComboIncorrect = jComboBoxIncorrectAnswers.getSelectedIndex();
			}
		} else
			jComboBoxIncorrectAnswers.setSelectedIndex(indexComboIncorrect);

		if (indexComboWithoutAnswer == -1) {
			if (test.getWithoutAnswersPenalizeNumber() > 0) {
				jComboBoxWithoutAnswers.setSelectedItem(String.valueOf(test.getWithoutAnswersPenalizeNumber()));

				indexComboWithoutAnswer = jComboBoxWithoutAnswers.getSelectedIndex();
			}
		} else
			jComboBoxWithoutAnswers.setSelectedIndex(indexComboWithoutAnswer);

		if (questionsFileNameVector != null && questionsFileNameVector.isEmpty() == false) {
			for (int i = 0; i < questionsFileNameVector.size(); i++)
				jComboBoxQuestionsFile.addItem(questionsFileNameVector.get(i).toString());
		}

		// Establecimiento de la funcionalidad de jComboBoxIncorrectAnswers.
		jComboBoxIncorrectAnswers.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				indexComboIncorrect = jComboBoxIncorrectAnswers.getSelectedIndex();
				evaluationParametersModified = true;
			}
		});

		// Establecimiento de la funcionalidad de jComboBoxWithoutAnswers.
		jComboBoxWithoutAnswers.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				indexComboWithoutAnswer = jComboBoxWithoutAnswers.getSelectedIndex();
				evaluationParametersModified = true;
			}
		});
	}

	/*
	 * NOMBRE: JListLoginInit. PERTENECE A: Clase FrmEditTest. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JList del frame.
	 */

	private void JListLoginInit() {
		Vector loginVector = new Vector();

		if (test.getStudentVector() != null && test.getStudentVector().isEmpty() == false) {
			studentTestVector = new Vector((Vector) test.getStudentVector().clone());
			finishTestVector = new Vector();
			canRepeatVector = new Vector();
			deleteStudentVector = new Vector();

			for (int i = 0; i < studentTestVector.size(); i++) {
				StudentTest studentTest = (StudentTest) studentTestVector.get(i);
				loginVector.add(studentTest.getLogin());
				finishTestVector.add(String.valueOf(studentTest.getFinish()));
				canRepeatVector.add(String.valueOf(studentTest.getCanRepeat()));
				deleteStudentVector.add("false");
			}
		}

		// Reserva de memoria para los JList.
		jListLogin = new JList(loginVector);

		// Establecimiento del scroll de los JList.
		jListLogin.setAutoscrolls(true);

		// Establecimiento de las fuentes de los JList.
		jListLogin.setFont(new java.awt.Font("Dialog", 1, 18));

		// Establecimiento del borde de los JList.
		jListLogin.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Establecimiento del n�mero de filas visibles de los JList.
		jListLogin.setVisibleRowCount(8);

		// Establecimiento del modo de selecci�n de los JList.
		jListLogin.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// Establecimiento de la funcionalidad de jListLogin.
		jListLogin.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if (jListLogin.getSelectedIndices().length == 1) {
					jButtonFinishTest.setEnabled(true);
					jButtonCanRepeat.setEnabled(true);
					jButtonNoCanRepeat.setEnabled(true);
					jButtonDeleteStudent.setEnabled(true);
					jButtonNoDeleteStudent.setEnabled(true);
					jButtonResetStudents.setEnabled(true);

					jCheckBoxFinish.setEnabled(true);
					jCheckBoxCanRepeat.setEnabled(true);
					jCheckBoxDeleteStudent.setEnabled(true);

					int index = jListLogin.getSelectedIndex();

					StudentTest studentTest = (StudentTest) studentTestVector.get(index);
					jCheckBoxFinish.setSelected(studentTest.getFinish());

					if (finishTestVector.get(index).toString().trim().equals("true"))
						jCheckBoxFinish.setEnabled(false);
					else
						jCheckBoxFinish.setEnabled(true);

					jCheckBoxCanRepeat.setSelected(studentTest.getCanRepeat());
					jCheckBoxDeleteStudent.setSelected(Boolean.valueOf(deleteStudentVector.get(index).toString())
							.booleanValue());
					jTextFieldScore.setText(String.valueOf(studentTest.getLastScore()));

					if (executionTypeString.trim().equals(TestEditor.ADAPTIVE)) {
						jTextFieldProficiency.setText(String.valueOf(studentTest.getTheta()));

						jTextFieldStudentStandardError.setText(String.valueOf(studentTest.getStandardError()));
					}

					jTextFieldMake.setText(String.valueOf(studentTest.getMake()));
				} else {
					jCheckBoxFinish.setSelected(false);
					jCheckBoxFinish.setEnabled(false);
					jCheckBoxCanRepeat.setSelected(false);
					jCheckBoxCanRepeat.setEnabled(false);
					jCheckBoxDeleteStudent.setSelected(false);
					jCheckBoxDeleteStudent.setEnabled(false);
					jTextFieldScore.setText("");
					jTextFieldProficiency.setText("");
					jTextFieldStudentStandardError.setText("");
					jTextFieldMake.setText("");

					if (jListLogin.getSelectedIndices().length == 0) {
						jButtonFinishTest.setEnabled(false);
						jButtonCanRepeat.setEnabled(false);
						jButtonNoCanRepeat.setEnabled(false);
						jButtonDeleteStudent.setEnabled(false);
						jButtonNoDeleteStudent.setEnabled(false);
						jButtonResetStudents.setEnabled(false);
					} else {
						jButtonFinishTest.setEnabled(true);
						jButtonCanRepeat.setEnabled(true);
						jButtonNoCanRepeat.setEnabled(true);
						jButtonDeleteStudent.setEnabled(true);
						jButtonNoDeleteStudent.setEnabled(true);
						jButtonResetStudents.setEnabled(true);
					}
				}
			}
		});
	}

	/*
	 * NOMBRE: JListConceptInit. PERTENECE A: Clase FrmEditTest. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JList del frame.
	 */

	private void JListConceptInit() {
		// Reserva de memoria del jListConcept.
		if (test.getTestType().trim().equals("exam")) {
			Vector conceptVector = new Vector();
			for (int i = 0; i < conceptVectorInitial.size(); i++) {
				if (conceptVectorInitial.get(i).toString().indexOf(test.getCourse()) == 0)
					conceptVector.add(conceptVectorInitial.get(i).toString().substring(
							conceptVectorInitial.get(i).toString().indexOf(test.getCourse())
									+ test.getCourse().length() + 1)
							+ "                                         ");
				else
					conceptVector.add(conceptVectorInitial.get(i).toString());
			}

			// Reserva de memoria para los JList.
			jListConcept = new JList(conceptVector);

			// Establecimiento del scroll de los JList.
			jListConcept.setAutoscrolls(true);

			// Establecimiento de las fuentes de los JList.
			jListConcept.setFont(new java.awt.Font("Dialog", 1, 14));

			// Establecimiento del borde de los JList.
			jListConcept.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

			// Establecimiento del n�mero de filas visibles de los JList.
			jListConcept.setVisibleRowCount(5);

			// Establecimiento del modo de selecci�n de los JList.
			jListConcept.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			// Establecimiento de la funcionalidad de jListConcept.
			jListConcept.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
					if (jListConcept.getSelectedIndices().length > 0) {
						jTreeTestQuestion.clearSelection();
						if (questionsOfMultipleFiles != null)
							jButtonDeleteQuestionToTest.setEnabled(true);
						else
							jButtonDeleteQuestionToTest.setEnabled(false);
					} else
						jButtonDeleteQuestionToTest.setEnabled(false);
				}
			});
		}
	}

	/*
	 * NOMBRE: JTreeTestQuestionInit. PERTENECE A: Clase FrmEditTest. LLAMADA
	 * POR: Constructor de esta misma clase. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JTree y establece su contenido y
	 * funcionalidad.
	 */

	private void JTreeTestQuestionInit() {
		// String enunciate = ""; //Almacena el texto del enunciado de la
		// pregunta.
		// String pathImage = ""; //Almacena el path de la imagen de la pregunta.
		// String difficulty = ""; //Almacena la dificultad de la pregunta.
		// String discrimination = ""; //Almacena la discriminaci�n de la
		// pregunta.
		// String guessing = ""; //Almacena la adivinanza de la pregunta.
		Vector textAnswers; // Almacena los textos de las respuestas de la
		// pregunta
		Vector correctAnswers; // Almacena si las respuestas de la pregunta son
		// verdaderas o falsas.
		Vector textExplanation; // Almacena los textos de las explicaciones de las
		// respuestas de la pregunta.
		int numAnswersInt; // Almacena el n�mero de respuestas de una pregunta.

		// Reserva de memoria para el nodo ra�z del JTree.
		rootNodeTestQuestion = new DefaultMutableTreeNode("Test                  ");

		// Bucle para la lectura de los datos de todas las preguntas, devueltos
		// por
		// el servidor, e inicializaci�n de los nodos del JTree con dichos datos.
		for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
			// Reserva de memoria para el nodo que contendr� las preguntas
			// devueltas
			// para un fichero de preguntas.
			DefaultMutableTreeNode questionsFileNode = new DefaultMutableTreeNode(
					((Vector) questionsOfMultipleFiles.get(i)).get(0).toString());

			for (int k = 0; k < ((Vector) ((Vector) questionsOfMultipleFiles.get(i)).get(1)).size(); k++) {
				// Reserva de memoria para el objeto Question.
				Question question = (Question) ((Vector) ((Vector) questionsOfMultipleFiles.get(i)).get(1))
						.get(k);

				// Reserva de memoria y establecimiento de valor para los nodos
				// referentes a los datos de la pregunta.
				DefaultMutableTreeNode conceptNode = new DefaultMutableTreeNode("Associate Concept: "
						+ question.getConcept());
				DefaultMutableTreeNode questionNode = new DefaultMutableTreeNode("Question "
						+ String.valueOf(question.getNumberQuestionOrder() + 1));
				DefaultMutableTreeNode codeQuestionNode = new DefaultMutableTreeNode("Code of Question: "
						+ question.getCodeQuestion());
				DefaultMutableTreeNode enunciateNode = new DefaultMutableTreeNode("Enunciate: "
						+ question.getEnunciate());

				// Comprobaci�n del tipo de path de la imagen asociada a la
				// pregunta.
				String nameFileImage = "";
				if (question.getPathImage().equals("") == false) {
					if (question.getPathImage().lastIndexOf("/") != -1)
						nameFileImage = question.getPathImage().substring(
								question.getPathImage().lastIndexOf("/") + 1);
					else
						nameFileImage = question.getPathImage().substring(
								question.getPathImage().lastIndexOf("\\") + 1);
				} else
					nameFileImage = "NO IMAGE";

				// Reserva de memoria y establecimiento de valor para el resto de
				// los
				// nodos referentes a los datos de la pregunta.
				DefaultMutableTreeNode imageNode = new DefaultMutableTreeNode("Image: " + nameFileImage);

				// Reserva de memoria para el nodo que contendr� todas las
				// respuestas
				// de una pregunta.
				DefaultMutableTreeNode answersNode = new DefaultMutableTreeNode("Answers");

				// Reserva de memoria para los array de String que contendr� todos
				// los
				// textos de las respuestas de una pregunta, explicaciones e
				// indicaciones
				// de si es correcta o falsa.
				textAnswers = question.getTextAnswers();
				correctAnswers = question.getCorrectAnswers();
				textExplanation = question.getTextExplanation();
				numAnswersInt = question.getNumberOfAnswers();

				// Bucle para la reserva de memoria de los nodos que contendr�n los
				// datos
				// de las respuestas de una pregunta, y establecimiento de su valor.
				numAnswersInt = textAnswers.size();
				for (int j = 0; j < numAnswersInt; j++) {
					// Reserva de memoria para el nodo que contendr� los datos de una
					// �nica
					// respuesta de una pregunta
					DefaultMutableTreeNode answerNode = new DefaultMutableTreeNode("Answer "
							+ String.valueOf(j + 1));

					DefaultMutableTreeNode textAnswerNode = new DefaultMutableTreeNode("Text of Answer: "
							+ textAnswers.get(j).toString());
					DefaultMutableTreeNode correctNode = new DefaultMutableTreeNode("Right: "
							+ correctAnswers.get(j).toString());
					DefaultMutableTreeNode textExplanationNode = new DefaultMutableTreeNode(
							"Text of Explanation: " + textExplanation.get(j).toString());

					// Adici�n de los datos de la respuesta al nodo "Answer".
					answerNode.add(textAnswerNode);
					answerNode.add(correctNode);
					answerNode.add(textExplanationNode);

					// Adici�n del nodo "Answer" al nodo "Answers", que contiene a
					// todas
					// las respuestas de una pregunta.
					answersNode.add(answerNode);
				}

				// Reserva de memoria y establecimiento de valor para los nodos que
				// contendr�n los par�metros IRT.
				DefaultMutableTreeNode difficultyNode = new DefaultMutableTreeNode("Difficulty: "
						+ question.getDifficulty());
				DefaultMutableTreeNode discriminationNode = new DefaultMutableTreeNode("Discrimination: "
						+ question.getDiscrimination());
				// DefaultMutableTreeNode guessingNode = new
				// DefaultMutableTreeNode("Guessing: " +question.getGuessing());

				DefaultMutableTreeNode irtParametersNode = new DefaultMutableTreeNode("IRT Parameters");
				irtParametersNode.add(difficultyNode);
				irtParametersNode.add(discriminationNode);
				// irtParametersNode.add(guessingNode);

				// Reserva de memoria y establecimiento de valor para los nodos que
				// contendr�n los par�metros de la pregunta.
				DefaultMutableTreeNode exhibitionRateNode = new DefaultMutableTreeNode("Exhibition rate: "
						+ question.getExhibitionRate());
				DefaultMutableTreeNode answerTimeNode = new DefaultMutableTreeNode("Half time of answer: "
						+ question.getAnswerTime());
				DefaultMutableTreeNode successRateNode = new DefaultMutableTreeNode("Success Rate: "
						+ question.getSuccessRate());

				DefaultMutableTreeNode statisticalParametersNode = new DefaultMutableTreeNode(
						"Statistical Parameters");
				statisticalParametersNode.add(exhibitionRateNode);
				statisticalParametersNode.add(answerTimeNode);
				statisticalParametersNode.add(successRateNode);

				// Reserva de memoria para el nodo que contendra los nombres de los
				// test
				// de los que la pregunta forma parte.
				DefaultMutableTreeNode testFilesNode = new DefaultMutableTreeNode("Test Files");

				Vector classicTestVector = question.getClassicTestVector();
				if (classicTestVector != null && classicTestVector.isEmpty() == false) {
					DefaultMutableTreeNode classicTestNode = new DefaultMutableTreeNode("Classic Test");
					for (int j = 0; j < classicTestVector.size(); j++) {
						DefaultMutableTreeNode testNode = new DefaultMutableTreeNode("Test: "
								+ String.valueOf(classicTestVector.get(j).toString()));
						classicTestNode.add(testNode);
					}
					testFilesNode.add(classicTestNode);
				}

				Vector adaptiveTestVector = question.getAdaptiveTestVector();
				if (adaptiveTestVector != null && adaptiveTestVector.isEmpty() == false) {
					DefaultMutableTreeNode adaptiveTestNode = new DefaultMutableTreeNode("Adaptive Test");
					for (int j = 0; j < adaptiveTestVector.size(); j++) {
						DefaultMutableTreeNode testNode = new DefaultMutableTreeNode("Test: "
								+ String.valueOf(adaptiveTestVector.get(j).toString()));
						adaptiveTestNode.add(testNode);
					}
					testFilesNode.add(adaptiveTestNode);
				}

				// Adici�n de todos los nodos anteriores al nodo "Question", que
				// contiene
				// todos los datos de una pregunta.
				questionNode.add(codeQuestionNode);
				questionNode.add(enunciateNode);
				questionNode.add(imageNode);
				questionNode.add(answersNode);

				questionNode.add(irtParametersNode);
				questionNode.add(statisticalParametersNode);

				if (testFilesNode.getChildCount() > 0)
					questionNode.add(testFilesNode);

				// Adici�n del nodo "Question" al nodo ra�z del JTree.
				if (k == 0)
					questionsFileNode.add(conceptNode);

				questionsFileNode.add(questionNode);
			}

			// Adicion del nodo questionsFileNode al nodo ra�z.
			rootNodeTestQuestion.add(questionsFileNode);
		}

		// Reserva de memoria para el JTree y establecimiento de su nodo ra�z.
		jTreeTestQuestion = new JTree(rootNodeTestQuestion);
		jTreeTestQuestion.setCellRenderer(new CustomCellRenderer("", wowPath, iconTestEditorPath, codeBase));

		// Establecimiento del tama�o, tipo de letra y borde del JTree.
		jTreeTestQuestion.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jTreeTestQuestion.setFont(new java.awt.Font("Dialog", 1, 14));
		jTreeTestQuestion.setScrollsOnExpand(true);
		jTreeTestQuestion.setShowsRootHandles(true);

		// Establecimiento de la funcionalidad del jTreeTestQuestion.
		jTreeTestQuestion.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// Perdida del foco del jListConcept.
				if (jListConcept != null)
					jListConcept.clearSelection();

				TreePath treePath = jTreeTestQuestion.getSelectionPath();

				if (treePath != null && treePath.getPathCount() >= 3) {
					nodeQuestionTest = (DefaultMutableTreeNode) treePath.getPathComponent(2);

					codeOfQuestionsToDelete = nodeQuestionTest.getFirstChild().toString().substring(
							nodeQuestionTest.getFirstChild().toString().lastIndexOf(": ") + 1).trim();

					questionsFileNameToDelete = treePath.getPathComponent(1).toString();
					jButtonDeleteQuestionToTest.setEnabled(true);
				} else {
					if (treePath != null && treePath.getPathCount() == 2) {
						// Preparaci�n para la adici�n de todas las preguntas de un
						// fichero.
						nodeQuestionTest = null;
						nodeQuestionTest = (DefaultMutableTreeNode) treePath.getPathComponent(1);
						codeOfQuestionsToDelete = "ALL_FILE";
						questionsFileNameToDelete = treePath.getPathComponent(1).toString();

						// Habilitaci�n del JButton correspondiente.
						jButtonDeleteQuestionToTest.setEnabled(true);
					} else
						jButtonDeleteQuestionToTest.setEnabled(false);
				}
			}
		});
	}

	/*
	 * NOMBRE: JTreeTestQuestionInit. PERTENECE A: Clase FrmEditTest. LLAMADA
	 * POR: jButtonAddQuestionsToTestActionPerformed(); LLAMA A: nada. RECIBE:
	 * rootNodeTestQuestion: Objeto de tipo DefaultMutableTreeNode. Contiene el
	 * nodo ra�z para el JTree que se est� inicializando. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JTree y establece su contenido y funcionalidad.
	 */

	private void JTreeTestQuestionInit(DefaultMutableTreeNode rootNodeTestQuestion) {
		// Reserva de memoria para el nodo ra�z del jTreeTestQuestion.
		jTreeTestQuestion = new JTree(rootNodeTestQuestion);
		jTreeTestQuestion.setCellRenderer(new CustomCellRenderer("", wowPath, iconTestEditorPath, codeBase));

		// Establecimiento del tama�o, tipo de letra y borde del JTree.
		jTreeTestQuestion.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jTreeTestQuestion.setFont(new java.awt.Font("Dialog", 1, 14));
		jTreeTestQuestion.setScrollsOnExpand(true);
		jTreeTestQuestion.setShowsRootHandles(true);

		// Establecimiento de la funcionalidad del jTreeTestQuestion.
		jTreeTestQuestion.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				TreePath treePath = jTreeTestQuestion.getSelectionPath();

				if (treePath != null && treePath.getPathCount() >= 3) {
					nodeQuestionTest = (DefaultMutableTreeNode) treePath.getPathComponent(2);

					codeOfQuestionsToDelete = nodeQuestionTest.getFirstChild().toString().substring(
							nodeQuestionTest.getFirstChild().toString().lastIndexOf(": ") + 1).trim();

					questionsFileNameToDelete = treePath.getPathComponent(1).toString();
					jButtonDeleteQuestionToTest.setEnabled(true);
				} else {
					if (treePath != null && treePath.getPathCount() == 2) {
						// Preparaci�n para la adici�n de todas las preguntas de un
						// fichero
						nodeQuestionTest = null;
						nodeQuestionTest = (DefaultMutableTreeNode) treePath.getPathComponent(1);
						codeOfQuestionsToDelete = "ALL_FILE";
						questionsFileNameToDelete = treePath.getPathComponent(1).toString();

						// Habilitaci�n del JButton correspondiente.
						jButtonDeleteQuestionToTest.setEnabled(true);
					} else
						jButtonDeleteQuestionToTest.setEnabled(false);
				}
			}
		});
	}

	/*
	 * NOMBRE: JTreeQuestionsFileInit. PERTENECE A: Clase FrmEditTest. LLAMADA
	 * POR: Constructor de esta misma clase. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JTree y establece su contenido y
	 * funcionalidad.
	 */

	private void JTreeQuestionsFileInit() {
		// String enunciate = ""; //Almacena el texto del enunciado de la
		// pregunta.
		// String pathImage = ""; //Almacena el path de la imagen de la pregunta.
		// String difficulty = ""; //Almacena la dificultad de la pregunta.
		// String discrimination = ""; //Almacena la discriminaci�n de la
		// pregunta.
		// String guessing = ""; //Almacena la adivinanza de la pregunta.
		Vector textAnswers; // Almacena los textos de las respuestas de la
		// pregunta
		Vector correctAnswers; // Almacena si las respuestas de la pregunta son
		// verdaderas o falsas.
		Vector textExplanation; // Almacena los textos de las explicaciones de las
		// respuestas de la pregunta.
		int numAnswersInt; // Almacena el n�mero de respuestas de una pregunta.

		// Reserva de memoria para el nodo ra�z del JTree.
		rootNodeQuestionsFile = new DefaultMutableTreeNode(((Question) questionsFileVector.firstElement())
				.getQuestionsFileName());

		for (int i = 0; i < questionsFileVector.size(); i++) {
			// Reserva de memoria para el objeto Question.
			Question question = (Question) questionsFileVector.get(i);

			// Reserva de memoria y establecimiento de valor para los nodos
			// referentes a los datos de la pregunta.
			DefaultMutableTreeNode conceptNode = new DefaultMutableTreeNode("Associate Concept: "
					+ question.getConcept());
			DefaultMutableTreeNode questionNode = new DefaultMutableTreeNode("Question "
					+ String.valueOf(question.getNumberQuestionOrder() + 1));
			DefaultMutableTreeNode codeQuestionNode = new DefaultMutableTreeNode("Code of Question: "
					+ question.getCodeQuestion());
			DefaultMutableTreeNode enunciateNode = new DefaultMutableTreeNode("Enunciate: "
					+ question.getEnunciate());

			// Comprobaci�n del tipo de path de la imagen asociada a la pregunta.
			String nameFileImage = "";
			if (question.getPathImage().equals("") == false) {
				if (question.getPathImage().lastIndexOf("/") != -1)
					nameFileImage = question.getPathImage()
							.substring(question.getPathImage().lastIndexOf("/") + 1);
				else
					nameFileImage = question.getPathImage().substring(
							question.getPathImage().lastIndexOf("\\") + 1);
			} else
				nameFileImage = "NO IMAGE";

			// Reserva de memoria y establecimiento de valor para el resto de los
			// nodos referentes a los datos de la pregunta.
			DefaultMutableTreeNode imageNode = new DefaultMutableTreeNode("Image: " + nameFileImage);

			// Reserva de memoria para el nodo que contendr� todas las respuestas
			// de una pregunta.
			DefaultMutableTreeNode answersNode = new DefaultMutableTreeNode("Answers");

			// Reserva de memoria para los array de String que contendr� todos los
			// textos de las respuestas de una pregunta, explicaciones e
			// indicaciones
			// de si es correcta o falsa.
			textAnswers = question.getTextAnswers();
			correctAnswers = question.getCorrectAnswers();
			textExplanation = question.getTextExplanation();
			numAnswersInt = question.getNumberOfAnswers();

			// Bucle para la reserva de memoria de los nodos que contendr�n los
			// datos
			// de las respuestas de una pregunta, y establecimiento de su valor.
			numAnswersInt = textAnswers.size();
			for (int j = 0; j < numAnswersInt; j++) {
				// Reserva de memoria para el nodo que contendr� los datos de una
				// �nica
				// respuesta de una pregunta
				DefaultMutableTreeNode answerNode = new DefaultMutableTreeNode("Answer " + String.valueOf(j + 1));
				DefaultMutableTreeNode textAnswerNode = new DefaultMutableTreeNode("Text of Answer: "
						+ textAnswers.get(j).toString());
				DefaultMutableTreeNode correctNode = new DefaultMutableTreeNode("Right: "
						+ correctAnswers.get(j).toString());
				DefaultMutableTreeNode textExplanationNode = new DefaultMutableTreeNode("Text of Explanation: "
						+ textExplanation.get(j).toString());

				// Adici�n de los datos de la respuesta al nodo "Answer".
				answerNode.add(textAnswerNode);
				answerNode.add(correctNode);
				answerNode.add(textExplanationNode);

				// Adici�n del nodo "Answer" al nodo "Answers", que contiene a todas
				// las respuestas de una pregunta.
				answersNode.add(answerNode);
			}

			// Reserva de memoria y establecimiento de valor para los nodos que
			// contendr�n los par�metros IRT.
			DefaultMutableTreeNode difficultyNode = new DefaultMutableTreeNode("Difficulty: "
					+ question.getDifficulty());
			DefaultMutableTreeNode discriminationNode = new DefaultMutableTreeNode("Discrimination: "
					+ question.getDiscrimination());
			// DefaultMutableTreeNode guessingNode = new
			// DefaultMutableTreeNode("Guessing: " +question.getGuessing());

			DefaultMutableTreeNode irtParametersNode = new DefaultMutableTreeNode("IRT Parameters");
			irtParametersNode.add(difficultyNode);
			irtParametersNode.add(discriminationNode);
			// irtParametersNode.add(guessingNode);

			// Reserva de memoria y establecimiento de valor para los nodos que
			// contendr�n los par�metros de la pregunta.
			DefaultMutableTreeNode exhibitionRateNode = new DefaultMutableTreeNode("Exhibition rate: "
					+ question.getExhibitionRate());
			DefaultMutableTreeNode answerTimeNode = new DefaultMutableTreeNode("Half time of answer: "
					+ question.getAnswerTime());
			DefaultMutableTreeNode successRateNode = new DefaultMutableTreeNode("Success Rate: "
					+ question.getSuccessRate());

			DefaultMutableTreeNode statisticalParametersNode = new DefaultMutableTreeNode(
					"Statistical Parameters");
			statisticalParametersNode.add(exhibitionRateNode);
			statisticalParametersNode.add(answerTimeNode);
			statisticalParametersNode.add(successRateNode);

			// Reserva de memoria para el nodo que contendra los nombres de los
			// test
			// de los que la pregunta forma parte.
			DefaultMutableTreeNode testFilesNode = new DefaultMutableTreeNode("Test Files");

			Vector classicTestVector = question.getClassicTestVector();
			if (classicTestVector != null && classicTestVector.isEmpty() == false) {
				DefaultMutableTreeNode classicTestNode = new DefaultMutableTreeNode("Classic Test");
				for (int j = 0; j < classicTestVector.size(); j++) {
					DefaultMutableTreeNode testNode = new DefaultMutableTreeNode("Test: "
							+ String.valueOf(classicTestVector.get(j).toString()));
					classicTestNode.add(testNode);
				}
				testFilesNode.add(classicTestNode);
			}

			Vector adaptiveTestVector = question.getAdaptiveTestVector();
			if (adaptiveTestVector != null && adaptiveTestVector.isEmpty() == false) {
				DefaultMutableTreeNode adaptiveTestNode = new DefaultMutableTreeNode("Adaptive Test");
				for (int j = 0; j < adaptiveTestVector.size(); j++) {
					DefaultMutableTreeNode testNode = new DefaultMutableTreeNode("Test: "
							+ String.valueOf(adaptiveTestVector.get(j).toString()));
					adaptiveTestNode.add(testNode);
				}
				testFilesNode.add(adaptiveTestNode);
			}

			// Adici�n de todos los nodos anteriores al nodo "Question", que
			// contiene
			// todos los datos de una pregunta.
			questionNode.add(codeQuestionNode);
			questionNode.add(enunciateNode);
			questionNode.add(imageNode);
			questionNode.add(answersNode);
			questionNode.add(irtParametersNode);
			questionNode.add(statisticalParametersNode);

			if (testFilesNode.getChildCount() > 0)
				questionNode.add(testFilesNode);

			// Adici�n del nodo "Question" al nodo ra�z del JTree.
			if (i == 0)
				rootNodeQuestionsFile.add(conceptNode);

			rootNodeQuestionsFile.add(questionNode);
		}

		// Reserva de memoria para el JTree y establecimiento de su nodo ra�z.
		jTreeQuestionsFile = new JTree(rootNodeQuestionsFile);
		jTreeQuestionsFile.setCellRenderer(new CustomCellRenderer("", wowPath, iconTestEditorPath, codeBase));

		// Establecimiento del tama�o, tipo de letra y borde del JTree.
		jTreeQuestionsFile.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jTreeQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 14));
		jTreeQuestionsFile.setScrollsOnExpand(true);
		jTreeQuestionsFile.setShowsRootHandles(true);

		// Establecimiento de la funcionalidad del jTreeQuestionsFile.
		jTreeQuestionsFile.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				TreePath treePath = jTreeQuestionsFile.getSelectionPath();

				if (treePath != null && treePath.getPathCount() >= 2) {
					nodeQuestionAdd = (DefaultMutableTreeNode) treePath.getPathComponent(1);
					if (nodeQuestionAdd.getChildCount() > 0) {
						codeOfQuestionsToAdd = nodeQuestionAdd.getFirstChild().toString().substring(
								nodeQuestionAdd.getFirstChild().toString().lastIndexOf(": ") + 1).trim();

						// questionsFileNameToAdd =
						// treePath.getPathComponent(1).toString();
						jButtonAddQuestionToTest.setEnabled(true);
					} else
						jButtonAddQuestionToTest.setEnabled(false);
				} else {
					if (treePath != null && treePath.getPathCount() == 1
							&& rootNodeQuestionsFile.isLeaf() == false) {
						// Preparaci�n para la adici�n de todas las preguntas de un
						// fichero.
						nodeQuestionAdd = null;
						nodeQuestionAdd = (DefaultMutableTreeNode) treePath.getPathComponent(0);
						codeOfQuestionsToAdd = "ALL_FILE";
						// questionsFileNameToAdd =
						// treePath.getPathComponent(0).toString();

						// Habilitaci�n del JButton correspondiente.
						jButtonAddQuestionToTest.setEnabled(true);
					} else
						jButtonAddQuestionToTest.setEnabled(false);
				}
			}
		});
	}

	/*
	 * NOMBRE: JTreeQuestionsFileInit. PERTENECE A: Clase FrmEditTest. LLAMADA
	 * POR: jButtonAddQuestionsToTestActionPerformed(); LLAMA A: nada. RECIBE:
	 * rootNodeQuestionsFile: Objeto de tipo DefaultMutableTreeNode. Contiene el
	 * nodo ra�z para el JTree que se est� inicializando. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JTree y establece su contenido y funcionalidad.
	 */

	private void JTreeQuestionsFileInit(DefaultMutableTreeNode rootNodeQuestionsFileIn) {
		final DefaultMutableTreeNode rootNodeQuestionsFile = rootNodeQuestionsFileIn;

		// Reserva de memoria para el nodo ra�z del jTreeTestQuestion.
		jTreeQuestionsFile = new JTree(rootNodeQuestionsFile);
		jTreeQuestionsFile.setCellRenderer(new CustomCellRenderer("", wowPath, iconTestEditorPath, codeBase));

		// Establecimiento del tama�o, tipo de letra y borde del JTree.
		jTreeQuestionsFile.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jTreeQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 14));
		jTreeQuestionsFile.setScrollsOnExpand(true);
		jTreeQuestionsFile.setShowsRootHandles(true);

		// Establecimiento de la funcionalidad del jTreeQuestionsFile.
		jTreeQuestionsFile.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				TreePath treePath = jTreeQuestionsFile.getSelectionPath();

				if (treePath != null && treePath.getPathCount() >= 2) {
					nodeQuestionAdd = (DefaultMutableTreeNode) treePath.getPathComponent(1);

					codeOfQuestionsToAdd = nodeQuestionTest.getFirstChild().toString().substring(
							nodeQuestionAdd.getFirstChild().toString().lastIndexOf(": ") + 1).trim();

					// questionsFileNameToAdd =
					// treePath.getPathComponent(1).toString();
					jButtonAddQuestionToTest.setEnabled(true);
				} else {
					if (treePath != null && treePath.getPathCount() == 1
							&& rootNodeQuestionsFile.isLeaf() == false) {
						// Preparaci�n para la adici�n de todas las preguntas de un
						// fichero.
						nodeQuestionAdd = null;
						nodeQuestionAdd = (DefaultMutableTreeNode) treePath.getPathComponent(0);
						codeOfQuestionsToAdd = "ALL_FILE";
						// questionsFileNameToAdd =
						// treePath.getPathComponent(0).toString();

						// Habilitaci�n del JButton correspondiente.
						jButtonAddQuestionToTest.setEnabled(true);
					} else
						jButtonAddQuestionToTest.setEnabled(false);
				}
			}
		});
	}

	/*
	 * NOMBRE: JTableTestQuestionInit. PERTENECE A: Clase FrmEditTest. LLAMADA
	 * POR: El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa el JTable y establece su
	 * contenido y funcionalidad.
	 */

	private void JTableTestQuestionInit() {
		rowsVectorTestQuestion = new Vector();
		columnsVectorTestQuestion = new Vector();

		columnsVectorTestQuestion.add("Question File Name");
		columnsVectorTestQuestion.add("Associate Concept");
		columnsVectorTestQuestion.add("Number Of Question");
		columnsVectorTestQuestion.add("Code of Question");
		columnsVectorTestQuestion.add("Difficulty");
		columnsVectorTestQuestion.add("Discrimination");
		// columnsVectorTestQuestion.add("Guessing");
		columnsVectorTestQuestion.add("Exhibition Rate");
		columnsVectorTestQuestion.add("Answer Time");
		columnsVectorTestQuestion.add("Success Rate");
		columnsVectorTestQuestion.add("Number Of Answers");
		columnsVectorTestQuestion.add("Image");

		if (questionsOfMultipleFiles != null) {
			// Bucle para la lectura de los datos de todas las preguntas, devueltos
			// por
			// el servidor, e inicializaci�n de los nodos del JTree con dichos
			// datos.
			for (int k = 0; k < questionsOfMultipleFiles.size(); k++) {
				for (int i = 0; i < ((Vector) ((Vector) questionsOfMultipleFiles.get(k)).get(1)).size(); i++) {
					// Reserva de memoria para el objeto Question.
					Question question = (Question) ((Vector) ((Vector) questionsOfMultipleFiles.get(k)).get(1))
							.get(i);

					Vector questionVector = new Vector();
					questionVector.add(question.getQuestionsFileName());
					questionVector
							.add(question.getConcept().substring(
									question.getConcept().indexOf(question.getCourse())
											+ question.getCourse().length() + 1));
					questionVector.add(String.valueOf(question.getNumberQuestionOrder() + 1));
					questionVector.add(question.getCodeQuestion());
					questionVector.add(String.valueOf(question.getDifficulty()));
					questionVector.add(String.valueOf(question.getDiscrimination()));
					// questionVector.add(String.valueOf(question.getGuessing()));
					questionVector.add(String.valueOf(question.getExhibitionRate()));
					questionVector.add(String.valueOf(question.getAnswerTime()));
					questionVector.add(String.valueOf(question.getSuccessRate()));
					questionVector.add(String.valueOf(question.getNumberOfAnswers()));

					if (question.getPathImage().trim().equals(""))
						questionVector.add(new Boolean(false));
					else
						questionVector.add(new Boolean(true));

					rowsVectorTestQuestion.add(questionVector);
				}
			}
		}

		tableModelTestQuestion = new TableModel(rowsVectorTestQuestion, columnsVectorTestQuestion);
		tableSorterTestQuestion = new TableSorter(tableModelTestQuestion);
		jTableTestQuestion = new JTable(tableSorterTestQuestion);
		jTableTestQuestion.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jTableTestQuestion.setAutoscrolls(true);
		tableSorterTestQuestion.addMouseListenerToHeaderInTable(jTableTestQuestion);

		// Establecimiento de la funcinalidad de jTableTestQuestion.
		jTableTestQuestion.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int childQuestionsFile = -1;
				int row = jTableTestQuestion.getSelectedRow();
				String questionsFileName = jTableTestQuestion.getValueAt(row, 0).toString();
				int numQuestion = Integer.valueOf(jTableTestQuestion.getValueAt(row, 2).toString()).intValue();

				jTreeTestQuestion.setSelectionRow(0);
				TreePath treePath = jTreeTestQuestion.getSelectionPath();
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) treePath.getLastPathComponent();

				for (int i = 0; i < root.getChildCount(); i++) {
					if (root.getChildAt(i).toString().trim().equals(questionsFileName.trim())) {
						childQuestionsFile = i;
						break;
					}
				}

				TreeNode questionsFileNode = root.getChildAt(childQuestionsFile);
				TreeNode questionNode = root.getChildAt(childQuestionsFile).getChildAt(numQuestion);

				jTreeTestQuestion.setSelectionPath(treePath.pathByAddingChild(questionsFileNode)
						.pathByAddingChild(questionNode));
			}
		});
	}

	/*
	 * NOMBRE: JTableQuestionsFileInit. PERTENECE A: Clase FrmEditTest. LLAMADA
	 * POR: El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa el JTable y establece su
	 * contenido y funcionalidad.
	 */

	private void JTableQuestionsFileInit() {
		rowsVectorQuestionsFile = new Vector();
		columnsVectorQuestionsFile = new Vector();

		columnsVectorQuestionsFile.add("Question File Name");
		columnsVectorQuestionsFile.add("Associate Concept");
		columnsVectorQuestionsFile.add("Number Of Question");
		columnsVectorQuestionsFile.add("Code of Question");
		columnsVectorQuestionsFile.add("Difficulty");
		columnsVectorQuestionsFile.add("Discrimination");
		// columnsVectorQuestionsFile.add("Guessing");
		columnsVectorQuestionsFile.add("Exhibition Rate");
		columnsVectorQuestionsFile.add("Answer Time");
		columnsVectorQuestionsFile.add("Success Rate");
		columnsVectorQuestionsFile.add("Number Of Answers");
		columnsVectorQuestionsFile.add("Image");

		if (questionsFileVector != null) {
			// Bucle para la lectura de los datos de todas las preguntas, devueltos
			// por
			// el servidor, e inicializaci�n de los nodos del JTree con dichos
			// datos.
			for (int i = 0; i < questionsFileVector.size(); i++) {
				// Reserva de memoria para el objeto Question.
				Question question = (Question) questionsFileVector.get(i);

				Vector questionVector = new Vector();
				questionVector.add(question.getQuestionsFileName());
				questionVector.add(question.getConcept().substring(
						question.getConcept().indexOf(question.getCourse()) + question.getCourse().length() + 1));
				questionVector.add(String.valueOf(question.getNumberQuestionOrder() + 1));
				questionVector.add(question.getCodeQuestion());
				questionVector.add(String.valueOf(question.getDifficulty()));
				questionVector.add(String.valueOf(question.getDiscrimination()));
				// questionVector.add(String.valueOf(question.getGuessing()));
				questionVector.add(String.valueOf(question.getExhibitionRate()));
				questionVector.add(String.valueOf(question.getAnswerTime()));
				questionVector.add(String.valueOf(question.getSuccessRate()));
				questionVector.add(String.valueOf(question.getNumberOfAnswers()));

				if (question.getPathImage().trim().equals(""))
					questionVector.add(new Boolean(false));
				else
					questionVector.add(new Boolean(true));

				rowsVectorQuestionsFile.add(questionVector);
			}
		}

		tableModelQuestionsFile = new TableModel(rowsVectorQuestionsFile, columnsVectorQuestionsFile);
		tableSorterQuestionsFile = new TableSorter(tableModelQuestionsFile);
		jTableQuestionsFile = new JTable(tableSorterQuestionsFile);
		jTableQuestionsFile.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jTableQuestionsFile.setAutoscrolls(true);
		tableSorterQuestionsFile.addMouseListenerToHeaderInTable(jTableQuestionsFile);

		// Establecimiento de la funcinalidad de jTableQuestionsFile.
		jTableQuestionsFile.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int childQuestionsFile = -1;
				int row = jTableQuestionsFile.getSelectedRow();
				String questionsFileName = jTableTestQuestion.getValueAt(row, 0).toString();
				int numQuestion = Integer.valueOf(jTableTestQuestion.getValueAt(row, 2).toString()).intValue();

				jTreeQuestionsFile.setSelectionRow(0);
				TreePath treePath = jTreeQuestionsFile.getSelectionPath();
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) treePath.getLastPathComponent();

				for (int i = 0; i < root.getChildCount(); i++) {
					if (root.getChildAt(i).toString().trim().equals(questionsFileName.trim())) {
						childQuestionsFile = i;
						break;
					}
				}

				TreeNode questionsFileNode = root.getChildAt(childQuestionsFile);
				TreeNode questionNode = root.getChildAt(childQuestionsFile).getChildAt(numQuestion);

				jTreeQuestionsFile.setSelectionPath(treePath.pathByAddingChild(questionsFileNode)
						.pathByAddingChild(questionNode));
			}
		});
	}

	/*
	 * NOMBRE: JProgressBarInit. PERTENECE A: Clase FrmEditTest. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa el JProgressBar del frame.
	 */

	private void JProgressBarInit() {
		// Reserva de memoria y establecimiento de algunas propiedades de los
		// JProgressBar.
		jProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
		jProgressBar.setMinimum(0);
		jProgressBar.setBorderPainted(true);
		jProgressBar.setStringPainted(true);
		jProgressBar.setAlignmentX(RIGHT_ALIGNMENT);
	}

	/*
	 * NOMBRE: JButtonInit. PERTENECE A: Clase FrmEditTest. LLAMADA POR: El
	 * constructor de la clase al que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JButton del frame.
	 */

	private void JButtonInit() {
		// Reserva de memoria para los JButton, dependiendo de si los
		// iconos del JInteralFrame se han cargado con �xito o no.
		if (ImageIconLoad == true) {
			jButtonResetPresentation = new JButton("Reset presentation", iconClear32);
			jButtonResetEvaluation = new JButton("Reset evaluation", iconClear32);
			jButtonSetTitle = new JButton("Set title", iconSetTitle32);
			jButtonSetBgColor = new JButton("Set bgColor", iconSetBgColor32);
			jButtonSetTitleColor = new JButton("Set title color", iconSetTitleColor32);
			jButtonSetBackground = new JButton("Set background", iconAddImage32);
			jButtonResetTitle = new JButton("Reset title", iconResetTitle32);
			jButtonResetBgColor = new JButton("Reset bgColor", iconResetBgColor32);
			jButtonResetBackground = new JButton("Delete background", iconDeleteImage32);
			jButtonResetTitleColor = new JButton("Reset title color", iconResetTitleColor32);
			jButtonResetAll = new JButton("Reset All", iconClear32);
			jButtonSelectAllStudents = new JButton("Select All", iconSelectAll32);
			jButtonFinishTest = new JButton("End the Test", iconFinishTest32);
			jButtonCanRepeat = new JButton("Can Repeat", iconCanRepeat32);
			jButtonNoCanRepeat = new JButton("No Can Repeat", iconNoCanRepeat32);
			jButtonDeleteStudent = new JButton("Delete", iconDelete32);
			jButtonNoDeleteStudent = new JButton("Delete", iconNoDelete32);
			jButtonResetStudents = new JButton("Reset", iconClear32);
			jButtonExamineTestQuestion = new JButton("Examine questions", iconExamineQuestion32);
			jButtonDeleteQuestionToTest = new JButton("Delete", iconDelete32);
			jButtonResetQuestionToTest = new JButton("Reset", iconClear32);
			jButtonGetQuestionsFileList = new JButton("Get Question File List", iconReload32);
			jButtonExamineQuestionsFile = new JButton("Examine question file", iconExamineQuestion32);
			jButtonAddQuestionToTest = new JButton("Add question to test", iconAddArrow32);

			jButtonModify = new JButton("Save", iconModify32);
			jButtonCancel = new JButton("Exit", iconCancel32);
			jButtonReload = new JButton("Reload", iconReload32);

			if (executionTypeString.equals(TestEditor.ADAPTIVE))
				jButtonSaveAs = new JButton("Transform to classic", iconCreateClassicTest32);
			else
				jButtonSaveAs = new JButton("Transform to adaptive", iconCreateAdaptiveTest32);
		} else {
			jButtonResetPresentation = new JButton("Reset presentation");
			jButtonResetEvaluation = new JButton("Reset evaluation");
			jButtonSetTitle = new JButton("Set title");
			jButtonSetBgColor = new JButton("Set bgColor");
			jButtonSetTitleColor = new JButton("Set title color");
			jButtonSetBackground = new JButton("Set background");
			jButtonResetTitle = new JButton("Reset title");
			jButtonResetBgColor = new JButton("Reset bgColor");
			jButtonResetBackground = new JButton("Delete background");
			jButtonResetTitleColor = new JButton("Reset title color");
			jButtonResetAll = new JButton("Reset All");
			jButtonSelectAllStudents = new JButton("Select All");
			jButtonFinishTest = new JButton("End the Test");
			jButtonCanRepeat = new JButton("Can Repeat");
			jButtonNoCanRepeat = new JButton("No Can Repeat");
			jButtonDeleteStudent = new JButton("Delete");
			jButtonNoDeleteStudent = new JButton("No Delete");
			jButtonResetStudents = new JButton("Reset");
			jButtonExamineTestQuestion = new JButton("Examine questions");
			jButtonDeleteQuestionToTest = new JButton("Delete");
			jButtonResetQuestionToTest = new JButton("Reset");
			jButtonGetQuestionsFileList = new JButton("Get Question File List");
			jButtonExamineQuestionsFile = new JButton("Examine question file");
			jButtonAddQuestionToTest = new JButton("Add question to test");

			jButtonModify = new JButton("Save");
			jButtonCancel = new JButton("Exit");
			jButtonReload = new JButton("Reload");

			if (executionTypeString.equals(TestEditor.ADAPTIVE))
				jButtonSaveAs = new JButton("Transform to classic");
			else
				jButtonSaveAs = new JButton("Transform to adaptive");
		}

		jButtonFinishTest.setEnabled(false);
		jButtonCanRepeat.setEnabled(false);
		jButtonNoCanRepeat.setEnabled(false);
		jButtonDeleteStudent.setEnabled(false);
		jButtonNoDeleteStudent.setEnabled(false);
		jButtonResetStudents.setEnabled(false);
		jButtonDeleteQuestionToTest.setEnabled(false);
		jButtonResetQuestionToTest.setEnabled(false);
		jButtonExamineQuestionsFile.setEnabled(false);
		jButtonAddQuestionToTest.setEnabled(false);

		// Establecimiento de la funcionalidad de jButtonResetPresentation.
		jButtonResetPresentation.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jCheckBoxShowInitialInfo.setSelected(test.getShowInitialInfo());
				if (test.getQuestionsOrder().trim().equals(TestEditor.RANDOM_STR))
					jComboBoxQuestionsOrder.setSelectedIndex(1);
				else
					jComboBoxQuestionsOrder.setSelectedIndex(0);

				if (test.getAnswersOrder().trim().equals(TestEditor.RANDOM_STR))
					jComboBoxAnswersOrder.setSelectedIndex(1);
				else
					jComboBoxAnswersOrder.setSelectedIndex(0);

				jCheckBoxShowQuestionCorrection.setSelected(test.getShowQuestionCorrection());
				jCheckBoxVerbose.setSelected(test.getVerbose());
				jCheckBoxShowCorrectAnswers.setSelected(test.getShowCorrectAnswers());
				if (Integer.valueOf(test.getTimeOfAnswer()).intValue() != 0) {
					jCheckBoxTimeToAnswer.setSelected(true);
					jSliderTimeToAnswer.setValue(Integer.valueOf(test.getTimeOfAnswer()).intValue());
				} else {
					jCheckBoxTimeToAnswer.setSelected(false);
					jSliderTimeToAnswer.setValue(ANSWER_TIME_MIN + 20);
				}

				jCheckBoxRepeatWithoutAnswer.setSelected(test.getRepeatWithoutAnswer());
				jCheckBoxShowFinalInfo.setSelected(test.getShowFinalInfo());

				presentationParametersModified = false;
			}
		});

		// Establecimiento de la funcionalidad de jButtonResetEvaluation.
		jButtonResetEvaluation.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (test.getIncorrectAnswersPenalize() == true)
					jRadioButtonIncorrectAnswersPenalizeYes.setSelected(true);
				else
					jRadioButtonIncorrectAnswersPenalizeNo.setSelected(true);

				if (test.getWithoutAnswersPenalize() == true)
					jRadioButtonWithoutAnswersPenalizeYes.setSelected(true);
				else
					jRadioButtonWithoutAnswersPenalizeNo.setSelected(true);

				if (test.getIncorrectAnswersPenalizeNumber() > 0) {
					jComboBoxIncorrectAnswers.setSelectedItem(String.valueOf(test
							.getIncorrectAnswersPenalizeNumber()));
					indexComboIncorrect = jComboBoxIncorrectAnswers.getSelectedIndex();
				}

				if (test.getWithoutAnswersPenalizeNumber() > 0) {
					jComboBoxWithoutAnswers
							.setSelectedItem(String.valueOf(test.getWithoutAnswersPenalizeNumber()));
					indexComboWithoutAnswer = jComboBoxWithoutAnswers.getSelectedIndex();
				}

				jSliderKnowledge.setValue(Integer.valueOf(test.getKnowledgePercentage()).intValue());
				evaluationParametersModified = false;
			}
		});

		// Establecimiento de la funcionalidad de jButtonSetTitle.
		jButtonSetTitle.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jTextFieldTestName.getText().trim().equals("") == true)
					jTextFieldTestName.setText(test.getTestName().trim());

				jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
				htmlParametersModified = true;
			}
		});

		// Establecimiento de la funcionalidad de jButtonSetBgColor.
		jButtonSetBgColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color bgColor = JColorChooser.showDialog(parent, "Select the background color", null);

				if (bgColor != null) {
					jTextFieldBgColor.setText("#"
							+ String.valueOf(Integer.toHexString(bgColor.getRGB()).substring(2)));

					jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
					htmlParametersModified = true;
				}
			}
		});

		// Establecimiento de la funcionalidad de jButtonSetBackground.
		jButtonSetBackground.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Reserva de memoria para jFileChoosers.
					JFileChooser jFileChooser = new JFileChooser(new File(System.getProperty("user.dir")));

					// Establecimiento del titulo.
					jFileChooser.setDialogTitle("Select Image");

					// Establecimiento del filtro para los tipos de archivos
					// permitidos.
					jFileChooser.addChoosableFileFilter(new ImageFilter());

					// Establecimiento del accesorio que permite previsualizar las
					// im�genes
					// en el propio jFileChooser.
					jFileChooser.setAccessory(new ImagePreview(jFileChooser));

					// Apertura del jFileChooser.
					jFileChooser.setVisible(true);
					jFileChooser.setSize(200, 100);
					int option = jFileChooser.showOpenDialog(parent);

					if (option == JFileChooser.CANCEL_OPTION)
						return;

					// Obtenci�n del fichero de imagen seleccionado por el usuario.
					backgroundFile = jFileChooser.getSelectedFile();

					byte[] data = new byte[(int) backgroundFile.length()];

					if (data.length > (1024 * 1024)) {
						JOptionPane.showMessageDialog(parent,
								"The image is too big, its maximum size should be 1 Mb.", "File Size",
								JOptionPane.ERROR_MESSAGE);

						return;
					}

					jTextFieldBackground.setText(backgroundFile.getAbsolutePath());

					imageDeleted = false;
					jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
					htmlParametersModified = true;
				} catch (SecurityException sE) {
					// Mensaje de error por falta de descarga del fichero
					// .java.policy en
					// el directorio del ususario que est� usando la herramienta
					// TestEditor.
					JOptionPane.showMessageDialog(parent, "ERROR: If you want to add images to the background"
							+ "\n" + "you need download the file .java.policy" + "\n" + "in their user directory."
							+ "\n" + "Download this file of the main page of the tool WOW! TestEditor." + "\n"
							+ "When it has finished their work session," + "\n"
							+ "it is advisable to eliminate this file of their system.", "Security Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Establecimiento de la funcionalidad de jButtonSetTitleColor.
		jButtonSetTitleColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color titleColor = JColorChooser.showDialog(parent, "Select the background color", null);
				if (titleColor != null) {
					jTextFieldTitleColor.setText("#"
							+ String.valueOf(Integer.toHexString(titleColor.getRGB()).substring(2)));

					jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
					htmlParametersModified = true;
				}
			}
		});

		// Establecimiento de la funcionalidad de jButtonResetTitle.
		jButtonResetTitle.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextFieldTestName.setText(test.getTestName());
				jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
			}
		});

		// Establecimiento de la funcionalidad de jButtonResetBgColor.
		jButtonResetBgColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextFieldBgColor.setText(test.getBgColor());
				jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
			}
		});

		// Establecimiento de la funcionalidad de jButtonResetBackground.
		jButtonResetBackground.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextFieldBackground.setText("");
				backgroundFile = null;
				imageDeleted = true;
				jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
			}
		});

		// Establecimiento de la funcionalidad de jButtonResetTitleColor.
		jButtonResetTitleColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextFieldTitleColor.setText(test.getTitleColor());
				jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
			}
		});

		jButtonResetAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextFieldTestName.setText(test.getTestName());
				jTextFieldBgColor.setText(test.getBgColor());
				jTextFieldBackground.setText(test.getBackground().substring(
						test.getBackground().lastIndexOf("/") + 1));
				backgroundFile = null;
				jTextFieldTitleColor.setText(test.getTitleColor());
				imageDeleted = false;
				jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
				htmlParametersModified = false;
			}
		});

		// Establecimiento de la funcionalidad de jButtonSelectAllStudents.
		jButtonSelectAllStudents.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					jListLogin.setSelectionInterval(0, test.getStudentVector().size() - 1);
				} catch (Exception ex) {}
			}
		});

		// Establecimiento de la funcionalidad de jButtonFinishTest.
		jButtonFinishTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < jListLogin.getSelectedIndices().length; i++) {
					int index = jListLogin.getSelectedIndices()[i];
					((StudentTest) studentTestVector.get(index)).setFinish(true);
					studentsModified = true;
				}

				if (jListLogin.getSelectedIndices().length == 1)
					jCheckBoxFinish.setSelected(true);
			}
		});

		// Establecimiento de la funcionalidad de jButtonCanRepeat.
		jButtonCanRepeat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < jListLogin.getSelectedIndices().length; i++) {
					int index = jListLogin.getSelectedIndices()[i];
					((StudentTest) studentTestVector.get(index)).setCanRepeat(true);

					studentsModified = true;
				}

				if (jListLogin.getSelectedIndices().length == 1)
					jCheckBoxCanRepeat.setSelected(true);
			}
		});

		// Establecimiento de la funcionalidad de jButtonNoCanRepeat.
		jButtonNoCanRepeat.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < jListLogin.getSelectedIndices().length; i++) {
					int index = jListLogin.getSelectedIndices()[i];
					((StudentTest) studentTestVector.get(index)).setCanRepeat(false);

					studentsModified = true;
				}

				if (jListLogin.getSelectedIndices().length == 1)
					jCheckBoxCanRepeat.setSelected(false);
			}
		});

		// Establecimiento de la funcionalidad de jButtonDeleteStudent.
		jButtonDeleteStudent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < jListLogin.getSelectedIndices().length; i++) {
					int index = jListLogin.getSelectedIndices()[i];
					deleteStudentVector.setElementAt("true", index);
					studentsModified = true;
				}

				if (jListLogin.getSelectedIndices().length == 1)
					jCheckBoxDeleteStudent.setSelected(true);
			}
		});

		// Establecimiento de la funcionalidad de jButtonNoDeleteStudent.
		jButtonNoDeleteStudent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < jListLogin.getSelectedIndices().length; i++) {
					int index = jListLogin.getSelectedIndices()[i];
					deleteStudentVector.setElementAt("false", index);
					studentsModified = true;
				}

				if (jListLogin.getSelectedIndices().length == 1)
					jCheckBoxDeleteStudent.setSelected(false);
			}
		});

		// Establecimiento de la funcionalidad de jButtonResetStudents.
		jButtonResetStudents.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < jListLogin.getSelectedIndices().length; i++) {
					int index = jListLogin.getSelectedIndices()[i];
					((StudentTest) studentTestVector.get(index)).setFinish(Boolean.valueOf(
							finishTestVector.get(index).toString()).booleanValue());

					((StudentTest) studentTestVector.get(index)).setCanRepeat(Boolean.valueOf(
							canRepeatVector.get(index).toString()).booleanValue());

					deleteStudentVector.setElementAt("false", index);
				}

				if (jListLogin.getSelectedIndices().length == 1) {
					int index = jListLogin.getSelectedIndex();
					jCheckBoxFinish.setSelected(Boolean.valueOf(finishTestVector.get(index).toString())
							.booleanValue());
					jCheckBoxCanRepeat.setSelected(Boolean.valueOf(canRepeatVector.get(index).toString())
							.booleanValue());
				}

				if (jListLogin.getSelectedIndices().length == test.getStudentVector().size())
					studentsModified = false;
			}
		});

		// Establecimiento de la funcionalidad de jButtonExamineQuestion.
		jButtonExamineTestQuestion.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonExamineTestQuestionActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jButtonDeleteQuestionToTest.
		jButtonDeleteQuestionToTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonDeleteQuestionToTestActionPerformed();
				testModified = true;
			}
		});

		// Establecimiento de la funcionalidad de jButtonResetQuestionToTest.
		jButtonResetQuestionToTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonResetQuestionToTestActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jButtonGetQuestionsFileList.
		jButtonGetQuestionsFileList.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonGetQuestionsFileListActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jButtonExamineQuestionsFile.
		jButtonExamineQuestionsFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (questionsOfMultipleFiles == null)
					jButtonExamineTestQuestionActionPerformed();

				jButtonExamineQuestionsFileActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jButtonAddQuestionToTest.
		jButtonAddQuestionToTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonAddQuestionToTestActionPerformed();
				testModified = true;
			}
		});

		// Establecimiento de la funcionalidad de jButtonNext.
		jButtonModify.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Aviso de duraci�n prolongada de la tarea.
				jLabelStatus.setText("Modifing the test file. " + "This can take some minutes...");
				jButtonModifyActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jButtonCancel.
		jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closingWindow();
			}
		});

		// Establecimiento de la funcionalidad de jButtonReload.
		jButtonReload.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonReloadActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jButtonSaveAs.
		jButtonSaveAs.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (executionTypeString.equals(TestEditor.ADAPTIVE)) {
					executionTypeString = TestEditor.CLASSIC;
					if (testFileName.lastIndexOf("_A_") != -1) {
						testFileName = testFileName.replaceAll("_A_", "_C_");

						// Cambio del t�tulo del frame.
						String title = internalFrame.getTitle();
						if (title.lastIndexOf("(Transform") != -1) {
							title = title.substring(0, title.lastIndexOf("(Transform")).concat(
									"(Transform to classic test with name: " + testFileName + ")");
						} else
							title = title
									.concat(" (Transform to classic test with name: " + testFileName + ")");

						internalFrame.setTitle(title);
					}

					jButtonSaveAs.setText("Transform to adaptive");
					jButtonSaveAs.setIcon(iconCreateAdaptiveTest32);

				} else {
					executionTypeString = TestEditor.ADAPTIVE;
					if (testFileName.lastIndexOf("_C_") != -1) {
						testFileName = testFileName.replaceAll("_C_", "_A_");

						// Cambio del t�tulo del frame.
						String title = internalFrame.getTitle();
						if (title.lastIndexOf("(Transform") != -1) {
							title = title.substring(0, title.lastIndexOf("(Transform")).concat(
									"(Transform to adaptive test " + "with name: " + testFileName + ")");
						} else
							title = title.concat(" (Transform to adaptive test " + "with name: " + testFileName
									+ ")");

						internalFrame.setTitle(title);
					}

					jButtonSaveAs.setText("Transform to classic");
					jButtonSaveAs.setIcon(iconCreateClassicTest32);
				}

				testTransformed = true;
				test.setStudentVector(null);

				JListLoginInit();
				JPanelInit();
				jButtonReload.setEnabled(false);
				try {
					frameInit();

					JOptionPane.showMessageDialog(null, "The new name of the test file is:" + "\n" + testFileName,
							"Info", JOptionPane.WARNING_MESSAGE);
				} catch (java.lang.Exception ex) {}
			}
		});
	}

	/*
	 * NOMBRE: JPanelInit. PERTENECE A: Clase FrmEditTest. LLAMADA POR: El
	 * Constructor de la clase al que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JPanel del frame,
	 * reservando memoria para el mismo, estableciendo su color, layout...
	 */

	private void JPanelInit() {
		// Reserva de memoria para el panel principal del frame y
		// establecimiento de su layout.
		jPanelContent = new JPanel();
		boxLayoutPanelContent = new BoxLayout(jPanelContent, BoxLayout.Y_AXIS);
		jPanelContent.setLayout(boxLayoutPanelContent);
		jPanelContent.setAlignmentX(CENTER_ALIGNMENT);

		// Reserva de memoria para el JPanel que contendr� al jLabelInstructions.
		jPanelTitle = new JPanel();
		flowLayoutPanelTitle = new FlowLayout(FlowLayout.CENTER);
		jPanelTitle.setLayout(flowLayoutPanelTitle);
		jPanelTitle.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jPanelTitle.add(jLabelTitle);

		// Llamada al m�todo que inicializa el jPanelPresentationParameters.
		JPanelPresentationParametersInit();

		// Llamada al m�todo que inicializa el jPanelHtmlParameters.
		JPanelHtmlParametersInit();

		// Llamada al m�todo que inicializa el jPanelEvaluationParameters.
		JPanelEvaluationParametersInit();

		// Llamada al m�todo que inicializa el jPanelAdaptiveParameters en caso
		// necesario.
		if (executionTypeString.equals(TestEditor.ADAPTIVE))
			JPanelAdaptiveParametersInit();

		// Llamada al m�todo que inicializa al jPanelStudents.
		JPanelStudentsInit();

		// Llamada al metodo que inicializa el jPanelTestQuestion.
		JPanelTestQuestionInit();

		// Llamada al metodo que inicializa el jPanelAddQuestionToTest.
		JPanelAddQuestionToTestInit();

		// Reserva de memoria para el jTabbedPane.
		jTabbedPane = new JTabbedPane();

		jTabbedPane.addTab("Presentation Parameters", iconEyes16, jScrollPanePresentationParameters);
		jTabbedPane.addTab("Html Parameters", iconHtml16, jScrollPaneHtmlParameters);
		jTabbedPane.addTab("Evaluation Parameters", iconEvaluation16, jScrollPaneEvaluationParameters);

		if (executionTypeString.equals(TestEditor.ADAPTIVE))
			jTabbedPane.addTab("Adaptive Parameters", iconInfoGreen16, jScrollPaneAdaptiveParameters);

		jTabbedPane.addTab("Students", iconStudents16, jScrollPaneStudents);
		jTabbedPane.addTab("Questions", iconQuestion16, jScrollPaneTestQuestion);
		jTabbedPane.addTab("Add Questions", iconAddQuestion16, jScrollPaneAddQuestionToTest);

		// Reserva de memoria y establecimento del layout para el JPanel que
		// contendr� a los JButton.
		jPanelButton = new JPanel();
		flowLayoutPanelButton = new FlowLayout(FlowLayout.CENTER);
		jPanelButton.setLayout(flowLayoutPanelButton);

		jPanelButton.add(jButtonModify);
		jPanelButton.add(jButtonCancel);
		jPanelButton.add(Box.createHorizontalStrut(30));
		jPanelButton.add(jButtonReload);
		jPanelButton.add(Box.createHorizontalStrut(30));
		jPanelButton.add(jButtonSaveAs);

		// Reserva de memoriay es establecimiento del layout para el jPanelStatus.
		jPanelStatus = new JPanel();
		flowLayoutPanelStatus = new FlowLayout(FlowLayout.LEFT);
		jPanelStatus.setBorder(BorderFactory.createLoweredBevelBorder());
		jPanelStatus.setLayout(flowLayoutPanelStatus);

		jLabelStatus.setAlignmentX(LEFT_ALIGNMENT);
		jProgressBar.setAlignmentX(CENTER_ALIGNMENT);

		jPanelStatus.add(jLabelStatus);
		jPanelStatus.add(jProgressBar);

		// Adici�n al jPanelContent de los paneles contenidos en el
		// frame.
		jPanelContent.add(jPanelTitle);
		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jTabbedPane);
		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jPanelButton);
		jPanelContent.add(jPanelStatus);
	}

	/*
	 * NOMBRE: JPanelPresentationParametersInit. PERTENECE A: Clase FrmEditTest.
	 * LLAMADA POR: El m�todo JPanelInit(); LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JPanel del frame,
	 * reservando memoria para el mismo, estableciendo su color, layout...
	 */

	private void JPanelPresentationParametersInit() {
		// Reserva de memoria y establecimiento del layout para el
		// jPanelShowInitialInfo.
		jPanelShowInitialInfo = new JPanel();
		flowLayoutPanelShowInitialInfo = new FlowLayout(FlowLayout.LEFT);
		jPanelShowInitialInfo.setLayout(flowLayoutPanelShowInitialInfo);
		jPanelShowInitialInfo.setAlignmentX(LEFT_ALIGNMENT);
		jPanelShowInitialInfo.setBackground(SystemColor.WHITE);
		jPanelShowInitialInfo.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jCheckBoxShowInitialInfo.setAlignmentX(LEFT_ALIGNMENT);
		jLabelShowInitialInfo.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentest del jPanelShowInitialInfo.
		jPanelShowInitialInfo.add(jCheckBoxShowInitialInfo);
		jPanelShowInitialInfo.add(jLabelShowInitialInfo);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� a los jLabelQuestionsOrder y jLabelQuestionsOrderExplanation.
		jPanelLabelQuestionsOrder = new JPanel();
		flowLayoutPanelLabelQuestionsOrder = new FlowLayout(FlowLayout.LEFT);
		jPanelLabelQuestionsOrder.setLayout(flowLayoutPanelLabelQuestionsOrder);
		jPanelLabelQuestionsOrder.setAlignmentX(LEFT_ALIGNMENT);
		jPanelLabelQuestionsOrder.setBackground(SystemColor.WHITE);
		jPanelLabelQuestionsOrder.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Establecimiento de la alineaci�n pra los JLabel.
		jLabelQuestionsOrder.setAlignmentX(LEFT_ALIGNMENT);
		jLabelQuestionsOrderInfo1.setAlignmentX(LEFT_ALIGNMENT);

		jPanelLabelQuestionsOrder.add(jLabelQuestionsOrder);
		jPanelLabelQuestionsOrder.add(jLabelQuestionsOrderInfo1);

		jPanelQuestionsOrderRadioButton = new JPanel();
		flowLayoutPanelQuestionsOrderRadioButton = new FlowLayout(FlowLayout.LEFT);
		jPanelQuestionsOrderRadioButton.setLayout(flowLayoutPanelQuestionsOrderRadioButton);
		jPanelQuestionsOrderRadioButton.setAlignmentX(LEFT_ALIGNMENT);
		jPanelQuestionsOrderRadioButton.setBackground(SystemColor.WHITE);
		jPanelQuestionsOrderRadioButton.add(jComboBoxQuestionsOrder);

		// Adici�n de cada JPanel a su lugar correspondiente.
		jPanelLabelQuestionsOrder.add(jPanelQuestionsOrderRadioButton);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� a los jLabelAnswersOrder y jLabelAnswersOrderExplanation.
		jPanelLabelAnswersOrder = new JPanel();
		flowLayoutPanelLabelAnswersOrder = new FlowLayout(FlowLayout.LEFT);
		jPanelLabelAnswersOrder.setLayout(flowLayoutPanelLabelAnswersOrder);
		jPanelLabelAnswersOrder.setAlignmentX(LEFT_ALIGNMENT);
		jPanelLabelAnswersOrder.setBackground(SystemColor.WHITE);
		jPanelLabelAnswersOrder.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Establecimiento de la alineaci�n pra los JLabel.
		jLabelAnswersOrder.setAlignmentX(LEFT_ALIGNMENT);
		jLabelAnswersOrderInfo1.setAlignmentX(LEFT_ALIGNMENT);
		jPanelLabelAnswersOrder.add(jLabelAnswersOrder);
		jPanelLabelAnswersOrder.add(jLabelAnswersOrderInfo1);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al jRadioButtonAnswersOrderRandom y
		// jRadioButtonAnswersOrderSequential.
		jPanelAnswersOrderRadioButton = new JPanel();
		flowLayoutPanelAnswersOrderRadioButton = new FlowLayout(FlowLayout.LEFT);
		jPanelAnswersOrderRadioButton.setLayout(flowLayoutPanelAnswersOrderRadioButton);
		jPanelAnswersOrderRadioButton.setAlignmentX(LEFT_ALIGNMENT);
		jPanelAnswersOrderRadioButton.setBackground(SystemColor.WHITE);

		jPanelAnswersOrderRadioButton.add(jComboBoxAnswersOrder);

		// Adici�n de cada JPanel a su lugar correspondiente.
		jPanelLabelAnswersOrder.add(jPanelAnswersOrderRadioButton);

		// Reserva de memoria y establecimiento del layout para el
		// jPanelShowQuestionCorrection.
		jPanelShowQuestionCorrection = new JPanel();
		flowLayoutPanelShowQuestionCorrection = new FlowLayout(FlowLayout.LEFT);
		jPanelShowQuestionCorrection.setLayout(flowLayoutPanelShowQuestionCorrection);
		jPanelShowQuestionCorrection.setAlignmentX(LEFT_ALIGNMENT);
		jPanelShowQuestionCorrection.setBackground(SystemColor.WHITE);
		jPanelShowQuestionCorrection.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jCheckBoxShowQuestionCorrection.setAlignmentX(LEFT_ALIGNMENT);
		jLabelShowQuestionCorrection.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentest del jPanelShowQuestionCorrection.
		jPanelShowQuestionCorrection.add(jCheckBoxShowQuestionCorrection);
		jPanelShowQuestionCorrection.add(jLabelShowQuestionCorrection);

		// Reserva de memoria y establecimiento del layout para el
		// jPanelVerbose.
		jPanelVerbose = new JPanel();
		flowLayoutPanelVerbose = new FlowLayout(FlowLayout.LEFT);
		jPanelVerbose.setLayout(flowLayoutPanelVerbose);
		jPanelVerbose.setAlignmentX(LEFT_ALIGNMENT);
		jPanelVerbose.setBackground(SystemColor.WHITE);
		jPanelVerbose.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Establecimiento de jPanelVerbose a invisible.
		if (jCheckBoxShowQuestionCorrection.isSelected() == false)
			jPanelVerbose.setVisible(false);

		jCheckBoxVerbose.setAlignmentX(LEFT_ALIGNMENT);
		jLabelVerbose.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentest del jPanelVerbose.
		jPanelVerbose.add(jCheckBoxVerbose);
		jPanelVerbose.add(jLabelVerbose);

		// Reserva de memoria y establecimiento del layout para el
		// jPanelShowCorrectAnswers.
		jPanelShowCorrectAnswers = new JPanel();
		flowLayoutPanelShowCorrectAnswers = new FlowLayout(FlowLayout.LEFT);
		jPanelShowCorrectAnswers.setLayout(flowLayoutPanelShowCorrectAnswers);
		jPanelShowCorrectAnswers.setAlignmentX(LEFT_ALIGNMENT);
		jPanelShowCorrectAnswers.setBackground(SystemColor.WHITE);
		jPanelShowCorrectAnswers.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Establecimiento de jPanelShowCorrectAnswers a invisible.
		if (jCheckBoxShowQuestionCorrection.isSelected() == false)
			jPanelShowCorrectAnswers.setVisible(false);

		jCheckBoxShowCorrectAnswers.setAlignmentX(LEFT_ALIGNMENT);
		jLabelShowCorrectAnswers.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentest del jPanelShowCorrectAnswers.
		jPanelShowCorrectAnswers.add(jCheckBoxShowCorrectAnswers);
		jPanelShowCorrectAnswers.add(jLabelShowCorrectAnswers);

		// Reserva de memoria y establecimiento del layout para el
		// jPanelTimeToAnswer.
		jPanelTimeToAnswer = new JPanel();
		flowLayoutPanelTimeToAnswer = new FlowLayout(FlowLayout.LEFT);
		jPanelTimeToAnswer.setLayout(flowLayoutPanelTimeToAnswer);
		jPanelTimeToAnswer.setAlignmentX(LEFT_ALIGNMENT);
		jPanelTimeToAnswer.setBackground(SystemColor.WHITE);
		jPanelTimeToAnswer.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jCheckBoxTimeToAnswer.setAlignmentX(LEFT_ALIGNMENT);
		jSliderTimeToAnswer.setAlignmentX(LEFT_ALIGNMENT);
		jTextFieldTimeToAnswer.setAlignmentX(LEFT_ALIGNMENT);
		jLabelTimeToAnswerInfo.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelTimeToAnswer.
		jPanelTimeToAnswer.add(jCheckBoxTimeToAnswer);
		jPanelTimeToAnswer.add(jSliderTimeToAnswer);
		jPanelTimeToAnswer.add(jTextFieldTimeToAnswer);
		jPanelTimeToAnswer.add(jLabelTimeToAnswerInfo);

		// Reserva de memoria y establecimiento del layout para el
		// jPanelRepeatWithoutAnswer.
		jPanelRepeatWithoutAnswer = new JPanel();
		flowLayoutPanelRepeatWithoutAnswer = new FlowLayout(FlowLayout.LEFT);
		jPanelRepeatWithoutAnswer.setLayout(flowLayoutPanelRepeatWithoutAnswer);
		jPanelRepeatWithoutAnswer.setAlignmentX(LEFT_ALIGNMENT);
		jPanelRepeatWithoutAnswer.setBackground(SystemColor.WHITE);
		jPanelRepeatWithoutAnswer.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jCheckBoxRepeatWithoutAnswer.setAlignmentX(LEFT_ALIGNMENT);
		jLabelRepeatWithoutAnswerInfo.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentest del jPanelRepeatWithoutAnswer.
		jPanelRepeatWithoutAnswer.add(jCheckBoxRepeatWithoutAnswer);
		jPanelRepeatWithoutAnswer.add(jLabelRepeatWithoutAnswerInfo);

		// Reserva de memoria y establecimiento del layout para el
		// jPanelShowFinalInfo.
		jPanelShowFinalInfo = new JPanel();
		flowLayoutPanelShowFinalInfo = new FlowLayout(FlowLayout.LEFT);
		jPanelShowFinalInfo.setLayout(flowLayoutPanelShowFinalInfo);
		jPanelShowFinalInfo.setAlignmentX(LEFT_ALIGNMENT);
		jPanelShowFinalInfo.setBackground(SystemColor.WHITE);
		jPanelShowFinalInfo.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jCheckBoxShowFinalInfo.setAlignmentX(LEFT_ALIGNMENT);
		jLabelShowFinalInfo.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentest del jPanelShowFinalInfo.
		jPanelShowFinalInfo.add(jCheckBoxShowFinalInfo);
		jPanelShowFinalInfo.add(jLabelShowFinalInfo);

		// Reserva de memoria para el jPanelPresentationParametersButton.
		jPanelPresentationParametersButton = new JPanel();
		flowLayoutPanelPresentationParametersButton = new FlowLayout(FlowLayout.RIGHT);
		jPanelPresentationParametersButton.setLayout(flowLayoutPanelPresentationParametersButton);

		jButtonResetPresentation.setAlignmentX(RIGHT_ALIGNMENT);

		// Adici�n de los componentes del jPanelPresentationParametersButton.
		jPanelPresentationParametersButton.add(jButtonResetPresentation);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al JPanel anteriores.
		jPanelPresentationParameters = new JPanel();
		boxLayoutPanelPresentationParameters = new BoxLayout(jPanelPresentationParameters, BoxLayout.Y_AXIS);
		jPanelPresentationParameters.setLayout(boxLayoutPanelPresentationParameters);
		jPanelPresentationParameters.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Parameters for the show of the test", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));
		jPanelPresentationParameters.setAlignmentX(CENTER_ALIGNMENT);

		jPanelShowInitialInfo.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelLabelQuestionsOrder.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelLabelAnswersOrder.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelShowQuestionCorrection.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelVerbose.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelShowCorrectAnswers.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelTimeToAnswer.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelRepeatWithoutAnswer.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelShowFinalInfo.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelPresentationParametersButton.setAlignmentX(RIGHT_ALIGNMENT);

		// Adici�n de los componentest del jPanelPresesntationParameters.
		jPanelPresentationParameters.add(jPanelShowInitialInfo);
		jPanelPresentationParameters.add(Box.createVerticalGlue());
		jPanelPresentationParameters.add(jPanelLabelQuestionsOrder);
		jPanelPresentationParameters.add(Box.createVerticalGlue());
		jPanelPresentationParameters.add(jPanelLabelAnswersOrder);
		jPanelPresentationParameters.add(Box.createVerticalGlue());
		jPanelPresentationParameters.add(jPanelShowQuestionCorrection);
		jPanelPresentationParameters.add(Box.createVerticalGlue());
		jPanelPresentationParameters.add(jPanelVerbose);
		jPanelPresentationParameters.add(Box.createVerticalGlue());
		jPanelPresentationParameters.add(jPanelShowCorrectAnswers);
		jPanelPresentationParameters.add(Box.createVerticalGlue());
		jPanelPresentationParameters.add(jPanelTimeToAnswer);
		jPanelPresentationParameters.add(Box.createVerticalGlue());
		jPanelPresentationParameters.add(jPanelRepeatWithoutAnswer);
		jPanelPresentationParameters.add(Box.createVerticalGlue());
		jPanelPresentationParameters.add(jPanelShowFinalInfo);
		jPanelPresentationParameters.add(Box.createVerticalGlue());
		jPanelPresentationParameters.add(jPanelPresentationParametersButton);

		// Reserva de memoria para el jScrollPanePresentationParameters.
		jScrollPanePresentationParameters = new JScrollPane(jPanelPresentationParameters,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/*
	 * NOMBRE: JPanelHtmlParametersInit. PERTENECE A: Clase FrmEditTest. LLAMADA
	 * POR: El m�todo JPanelInit(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JPanel del frame, reservando memoria para
	 * el mismo, estableciendo su color, layout...
	 */

	private void JPanelHtmlParametersInit() {
		// Reserva de memoria para el jPanelButtonTestName.
		jPanelButtonTestName = new JPanel();
		flowLayoutPanelButtonTestName = new FlowLayout(FlowLayout.CENTER);
		jPanelButtonTestName.setLayout(flowLayoutPanelButtonTestName);

		jPanelButtonTestName.setBackground(SystemColor.WHITE);
		jPanelButtonTestName.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jLabelTestName.setAlignmentX(CENTER_ALIGNMENT);
		jTextFieldTestName.setAlignmentX(CENTER_ALIGNMENT);
		jButtonSetTitle.setAlignmentX(CENTER_ALIGNMENT);
		jButtonResetTitle.setAlignmentX(CENTER_ALIGNMENT);

		// Adici�n de los componenes del jPanelButtonTestName.
		jPanelButtonTestName.add(jLabelTestName);
		jPanelButtonTestName.add(jTextFieldTestName);
		jPanelButtonTestName.add(jButtonSetTitle);
		jPanelButtonTestName.add(jButtonResetTitle);

		// Reserva de memoria para el jPanelButtonTitleColor.
		jPanelButtonTitleColor = new JPanel();
		flowLayoutPanelButtonTitleColor = new FlowLayout(FlowLayout.CENTER);
		jPanelButtonTitleColor.setLayout(flowLayoutPanelButtonTitleColor);

		jPanelButtonTitleColor.setBackground(SystemColor.WHITE);
		jPanelButtonTitleColor.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jButtonSetTitleColor.setAlignmentX(CENTER_ALIGNMENT);
		jTextFieldTitleColor.setAlignmentX(CENTER_ALIGNMENT);
		jButtonResetTitleColor.setAlignmentX(CENTER_ALIGNMENT);

		// Adici�n de los componenes del jPanelButtonTitleColor.
		jPanelButtonTitleColor.add(jButtonSetTitleColor);
		jPanelButtonTitleColor.add(jTextFieldTitleColor);
		jPanelButtonTitleColor.add(jButtonResetTitleColor);

		// Reserva de memoria para el jPanelButtonBgColor.
		jPanelButtonBgColor = new JPanel();
		flowLayoutPanelButtonBgColor = new FlowLayout(FlowLayout.CENTER);
		jPanelButtonBgColor.setLayout(flowLayoutPanelButtonBgColor);

		jPanelButtonBgColor.setBackground(SystemColor.WHITE);
		jPanelButtonBgColor.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jButtonSetBgColor.setAlignmentX(CENTER_ALIGNMENT);
		jTextFieldBgColor.setAlignmentX(CENTER_ALIGNMENT);
		jButtonResetBgColor.setAlignmentX(CENTER_ALIGNMENT);

		// Adici�n de los componenes del jPanelButtonBgColor.
		jPanelButtonBgColor.add(jButtonSetBgColor);
		jPanelButtonBgColor.add(jTextFieldBgColor);
		jPanelButtonBgColor.add(jButtonResetBgColor);

		// Reserva de memoria para el jPanelButtonBackground.
		jPanelButtonBackground = new JPanel();
		flowLayoutPanelButtonBackground = new FlowLayout(FlowLayout.CENTER);
		jPanelButtonBackground.setLayout(flowLayoutPanelButtonBackground);

		jPanelButtonBackground.setBackground(SystemColor.WHITE);
		jPanelButtonBackground.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jButtonSetBackground.setAlignmentX(CENTER_ALIGNMENT);
		jTextFieldBackground.setAlignmentX(CENTER_ALIGNMENT);
		jButtonResetBackground.setAlignmentX(CENTER_ALIGNMENT);

		// Adici�n de los componenes del jPanelButtonBackground.
		jPanelButtonBackground.add(jButtonSetBackground);
		jPanelButtonBackground.add(jTextFieldBackground);
		jPanelButtonBackground.add(jButtonResetBackground);

		// Reserva de memoria para el jPanelButtonResetAll.
		jPanelButtonResetAll = new JPanel();
		flowLayoutPanelButtonResetAll = new FlowLayout(FlowLayout.CENTER);
		jPanelButtonResetAll.setLayout(flowLayoutPanelButtonResetAll);

		jPanelButtonResetAll.setBackground(SystemColor.WHITE);
		jPanelButtonResetAll.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jButtonResetAll.setAlignmentX(CENTER_ALIGNMENT);

		// Adici�n de los componenes del jPanelButtonResetAll.
		jPanelButtonResetAll.add(jButtonResetAll);

		// Reserva de memoria para el jPanelHtmlParametersButton.
		jPanelHtmlParametersButton = new JPanel();
		boxLayoutPanelHtmlParametersButton = new BoxLayout(jPanelHtmlParametersButton, BoxLayout.Y_AXIS);
		jPanelHtmlParametersButton.setLayout(boxLayoutPanelHtmlParametersButton);
		jPanelHtmlParametersButton.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Html Parameters (Optional)", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelButtonTestName.setAlignmentX(CENTER_ALIGNMENT);
		jPanelButtonBgColor.setAlignmentX(CENTER_ALIGNMENT);
		jPanelButtonBackground.setAlignmentX(CENTER_ALIGNMENT);
		jPanelButtonTitleColor.setAlignmentX(CENTER_ALIGNMENT);
		jPanelButtonResetAll.setAlignmentX(CENTER_ALIGNMENT);

		// Adici�n de los componentes del jPanelHtmlParametersButton.
		jPanelHtmlParametersButton.add(jPanelButtonTestName);
		jPanelHtmlParametersButton.add(Box.createVerticalStrut(30));
		jPanelHtmlParametersButton.add(jPanelButtonTitleColor);
		jPanelHtmlParametersButton.add(Box.createVerticalStrut(30));
		jPanelHtmlParametersButton.add(jPanelButtonBgColor);
		jPanelHtmlParametersButton.add(Box.createVerticalStrut(30));
		jPanelHtmlParametersButton.add(jPanelButtonBackground);
		jPanelHtmlParametersButton.add(Box.createVerticalStrut(30));
		jPanelHtmlParametersButton.add(jPanelButtonResetAll);

		// Reserva de memoria para el jPanelHtmlParametersEditorPane.
		jPanelHtmlParametersEditorPane = new JPanel();
		boxLayoutPanelHtmlParametersEditorPane = new BoxLayout(jPanelHtmlParametersEditorPane, BoxLayout.Y_AXIS);
		jPanelHtmlParametersEditorPane.setLayout(boxLayoutPanelHtmlParametersEditorPane);
		jPanelHtmlParametersEditorPane.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Preview", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jEditorPaneHtmlParameters.setAlignmentX(CENTER_ALIGNMENT);

		// Adici�n de los componentes del jPanelHtmlParametersButton.
		jPanelHtmlParametersEditorPane.add(jEditorPaneHtmlParameters);

		// Reserva de memoria para el jPanelHtmlParameters.
		jPanelHtmlParameters = new JPanel();
		flowLayoutPanelHtmlParameters = new FlowLayout(FlowLayout.LEFT);
		jPanelHtmlParameters.setLayout(flowLayoutPanelHtmlParameters);
		jPanelHtmlParameters.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jPanelHtmlParametersButton.setAlignmentX(LEFT_ALIGNMENT);
		jPanelHtmlParametersEditorPane.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelHtmlParameters.
		jPanelHtmlParameters.add(jPanelHtmlParametersButton);
		jPanelHtmlParameters.add(jPanelHtmlParametersEditorPane);

		// Reserva de memoria para el jScrollPaneHtmlParameters.
		jScrollPaneHtmlParameters = new JScrollPane(jPanelHtmlParameters,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/*
	 * NOMBRE: JPanelEvaluationParametersInit. PERTENECE A: Clase FrmEditTest.
	 * LLAMADA POR: El m�todo JPanelInit(); LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JPanel del frame,
	 * reservando memoria para el mismo, estableciendo su color, layout...
	 */

	private void JPanelEvaluationParametersInit() {
		// Reserva de memoria y establecimineto del layout para
		// jPanelIncorrectAnswersRadioButton.
		jPanelIncorrectAnswersRadioButton = new JPanel();
		flowLayoutPanelIncorrectAnswersRadioButton = new FlowLayout(FlowLayout.LEFT);
		jPanelIncorrectAnswersRadioButton.setLayout(flowLayoutPanelIncorrectAnswersRadioButton);
		jPanelIncorrectAnswersRadioButton.setBackground(SystemColor.WHITE);

		jRadioButtonIncorrectAnswersPenalizeYes.setAlignmentX(LEFT_ALIGNMENT);
		jRadioButtonIncorrectAnswersPenalizeNo.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes pertenecientes a
		// jPanelIncorrectAnswersRadioButton.
		jPanelIncorrectAnswersRadioButton.add(jRadioButtonIncorrectAnswersPenalizeYes);
		jPanelIncorrectAnswersRadioButton.add(jRadioButtonIncorrectAnswersPenalizeNo);

		// Reserva de memoria y establecimiento de layout para el
		// jPanelLabelIncorrectAnswers.
		jPanelLabelIncorrectAnswers = new JPanel();
		flowLayoutPanelLabelIncorrectAnswers = new FlowLayout(FlowLayout.LEFT);
		jPanelLabelIncorrectAnswers.setLayout(flowLayoutPanelLabelIncorrectAnswers);
		jPanelLabelIncorrectAnswers.setBackground(SystemColor.WHITE);

		jLabelIncorrectAnswers.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelLabelIncorrectAnswers.
		jPanelLabelIncorrectAnswers.add(jLabelIncorrectAnswers);

		// Reserva de memoria para el panel que contendra al
		// jPanelIncorrectAnswersRadioButton y a jPanelLabelIncorrectAnswers.
		jPanelIncorrectAnswersLabelRadioButton = new JPanel();
		boxLayoutPanelIncorrectAnswersLabelRadioButton = new BoxLayout(jPanelIncorrectAnswersLabelRadioButton,
				BoxLayout.Y_AXIS);
		jPanelIncorrectAnswersLabelRadioButton.setLayout(boxLayoutPanelIncorrectAnswersLabelRadioButton);
		jPanelIncorrectAnswersLabelRadioButton.setBackground(SystemColor.WHITE);

		// Adicion de los componentes pertenecientes al
		// jPanelIncorrectAnswersLabelRadioButton.
		jPanelIncorrectAnswersLabelRadioButton.add(jPanelLabelIncorrectAnswers);
		jPanelIncorrectAnswersLabelRadioButton.add(jPanelIncorrectAnswersRadioButton);

		// Reserva de memoria para el panel que contendr� a los
		// jLabelIncorrectAnswersPenalize1, jComboBoxIncorrectAnswers y
		// jLabelIncorrectAnswersPenalize2.
		jPanelLabelIncorrectAnswersPenalize12 = new JPanel();
		flowLayoutPanelLabelIncorrectAnswersPenalize12 = new FlowLayout(FlowLayout.LEFT);
		jPanelLabelIncorrectAnswersPenalize12.setLayout(flowLayoutPanelLabelIncorrectAnswersPenalize12);
		jPanelLabelIncorrectAnswersPenalize12.setBackground(SystemColor.LIGHT_GRAY);

		jLabelIncorrectAnswersPenalize1.setAlignmentX(LEFT_ALIGNMENT);
		jComboBoxIncorrectAnswers.setAlignmentX(LEFT_ALIGNMENT);
		jLabelIncorrectAnswersPenalize2.setAlignmentX(LEFT_ALIGNMENT);

		// Adicion de los componentes pertenecientes al
		// jPanelLabelIncorrectAnswersPenalize12.
		jPanelLabelIncorrectAnswersPenalize12.add(jLabelIncorrectAnswersPenalize1);
		jPanelLabelIncorrectAnswersPenalize12.add(jComboBoxIncorrectAnswers);
		jPanelLabelIncorrectAnswersPenalize12.add(jLabelIncorrectAnswersPenalize2);

		// Reserva de memoria para el panel que contendr� a los
		// jLabelIncorrectAnswersPenalize3.
		jPanelLabelIncorrectAnswersPenalize3 = new JPanel();
		flowLayoutPanelLabelIncorrectAnswersPenalize3 = new FlowLayout(FlowLayout.LEFT);
		jPanelLabelIncorrectAnswersPenalize3.setLayout(flowLayoutPanelLabelIncorrectAnswersPenalize3);
		jPanelLabelIncorrectAnswersPenalize3.setBackground(SystemColor.LIGHT_GRAY);

		jLabelIncorrectAnswersPenalize3.setAlignmentX(LEFT_ALIGNMENT);

		// Adicion de los componentes pertenecientes al
		// jPanelLabelIncorrectAnswersPenalize3.
		jPanelLabelIncorrectAnswersPenalize3.add(jLabelIncorrectAnswersPenalize3);

		// Reserva de memoria y establecimiento de layout para el
		// jPanelIncorrectAnswersPenalize.
		jPanelIncorrectAnswersPenalize = new JPanel();
		boxLayoutPanelIncorrectAnswersPenalize = new BoxLayout(jPanelIncorrectAnswersPenalize, BoxLayout.Y_AXIS);
		jPanelIncorrectAnswersPenalize.setLayout(boxLayoutPanelIncorrectAnswersPenalize);
		jPanelIncorrectAnswersPenalize.setBackground(SystemColor.LIGHT_GRAY);
		jPanelIncorrectAnswersPenalize.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Adici�n de los componentes del jPanelLabelIncorrectAnswersPenalize.
		jPanelIncorrectAnswersPenalize.add(jPanelLabelIncorrectAnswersPenalize12);
		jPanelIncorrectAnswersPenalize.add(jPanelLabelIncorrectAnswersPenalize3);

		// Establecimiento de visible o invisible.
		if (test.getIncorrectAnswersPenalize() == true)
			jPanelIncorrectAnswersPenalize.setVisible(true);
		else
			jPanelIncorrectAnswersPenalize.setVisible(false);

		// Reserva de memoria para el jPanelIncorrectAnswers.
		jPanelIncorrectAnswers = new JPanel();
		flowLayoutPanelIncorrectAnswers = new FlowLayout(FlowLayout.LEFT);
		jPanelIncorrectAnswers.setLayout(flowLayoutPanelIncorrectAnswers);
		jPanelIncorrectAnswers.setBackground(SystemColor.WHITE);
		jPanelIncorrectAnswers.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Adicion de los componentes del jPanelIncorrectAnswers.
		jPanelIncorrectAnswers.add(jPanelIncorrectAnswersLabelRadioButton);
		jPanelIncorrectAnswers.add(jPanelIncorrectAnswersPenalize);

		// Reserva de memoria y establecimineto del layout para
		// jPanelWithoutAnswersRadioButton.
		jPanelWithoutAnswersRadioButton = new JPanel();
		flowLayoutPanelWithoutAnswersRadioButton = new FlowLayout(FlowLayout.LEFT);
		jPanelWithoutAnswersRadioButton.setLayout(flowLayoutPanelWithoutAnswersRadioButton);
		jPanelWithoutAnswersRadioButton.setBackground(SystemColor.WHITE);

		jRadioButtonWithoutAnswersPenalizeYes.setAlignmentX(LEFT_ALIGNMENT);
		jRadioButtonWithoutAnswersPenalizeNo.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes pertenecientes a
		// jPanelWithoutAnswersRadioButton.
		jPanelWithoutAnswersRadioButton.add(jRadioButtonWithoutAnswersPenalizeYes);
		jPanelWithoutAnswersRadioButton.add(jRadioButtonWithoutAnswersPenalizeNo);

		// Reserva de memoria y establecimiento de layout para el
		// jPanelLabelWithoutAnswers.
		jPanelLabelWithoutAnswers = new JPanel();
		flowLayoutPanelLabelWithoutAnswers = new FlowLayout(FlowLayout.LEFT);
		jPanelLabelWithoutAnswers.setLayout(flowLayoutPanelLabelWithoutAnswers);
		jPanelLabelWithoutAnswers.setBackground(SystemColor.WHITE);

		jLabelWithoutAnswers.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelLabelWithoutAnswers.
		jPanelLabelWithoutAnswers.add(jLabelWithoutAnswers);

		// Reserva de memoria para el panel que contendra al
		// jPanelWithoutAnswersRadioButton y a jPanelLabelWithoutAnswers.
		jPanelWithoutAnswersLabelRadioButton = new JPanel();
		boxLayoutPanelWithoutAnswersLabelRadioButton = new BoxLayout(jPanelWithoutAnswersLabelRadioButton,
				BoxLayout.Y_AXIS);
		jPanelWithoutAnswersLabelRadioButton.setLayout(boxLayoutPanelWithoutAnswersLabelRadioButton);
		jPanelWithoutAnswersLabelRadioButton.setBackground(SystemColor.WHITE);

		// Adicion de los componentes pertenecientes al
		// jPanelWithoutAnswersLabelRadioButton.
		jPanelWithoutAnswersLabelRadioButton.add(jPanelLabelWithoutAnswers);
		jPanelWithoutAnswersLabelRadioButton.add(jPanelWithoutAnswersRadioButton);

		// Reserva de memoria para el panel que contendr� a los
		// jLabelWithoutAnswersPenalize1, jComboBoxWithoutAnswers y
		// jLabelWithoutAnswersPenalize2.
		jPanelLabelWithoutAnswersPenalize12 = new JPanel();
		flowLayoutPanelLabelWithoutAnswersPenalize12 = new FlowLayout(FlowLayout.LEFT);
		jPanelLabelWithoutAnswersPenalize12.setLayout(flowLayoutPanelLabelWithoutAnswersPenalize12);
		jPanelLabelWithoutAnswersPenalize12.setBackground(SystemColor.LIGHT_GRAY);

		jLabelWithoutAnswersPenalize1.setAlignmentX(LEFT_ALIGNMENT);
		jComboBoxWithoutAnswers.setAlignmentX(LEFT_ALIGNMENT);
		jLabelWithoutAnswersPenalize2.setAlignmentX(LEFT_ALIGNMENT);

		// Adicion de los componentes pertenecientes al
		// jPanelLabelWithoutAnswersPenalize12.
		jPanelLabelWithoutAnswersPenalize12.add(jLabelWithoutAnswersPenalize1);
		jPanelLabelWithoutAnswersPenalize12.add(jComboBoxWithoutAnswers);
		jPanelLabelWithoutAnswersPenalize12.add(jLabelWithoutAnswersPenalize2);

		// Reserva de memoria para el panel que contendr� a los
		// jLabelWithoutAnswersPenalize3.
		jPanelLabelWithoutAnswersPenalize3 = new JPanel();
		flowLayoutPanelLabelWithoutAnswersPenalize3 = new FlowLayout(FlowLayout.LEFT);
		jPanelLabelWithoutAnswersPenalize3.setLayout(flowLayoutPanelLabelWithoutAnswersPenalize3);
		jPanelLabelWithoutAnswersPenalize3.setBackground(SystemColor.LIGHT_GRAY);

		jLabelWithoutAnswersPenalize3.setAlignmentX(LEFT_ALIGNMENT);

		// Adicion de los componentes pertenecientes al
		// jPanelLabelWithoutAnswersPenalize3.
		jPanelLabelWithoutAnswersPenalize3.add(jLabelWithoutAnswersPenalize3);

		// Reserva de memoria y establecimiento de layout para el
		// jPanelWithoutAnswersPenalize.
		jPanelWithoutAnswersPenalize = new JPanel();
		boxLayoutPanelWithoutAnswersPenalize = new BoxLayout(jPanelWithoutAnswersPenalize, BoxLayout.Y_AXIS);
		jPanelWithoutAnswersPenalize.setLayout(boxLayoutPanelWithoutAnswersPenalize);
		jPanelWithoutAnswersPenalize.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelWithoutAnswersPenalize.setBackground(SystemColor.LIGHT_GRAY);

		// Adici�n de los componentes del jPanelLabelWithoutAnswersPenalize.
		jPanelWithoutAnswersPenalize.add(jPanelLabelWithoutAnswersPenalize12);
		jPanelWithoutAnswersPenalize.add(jPanelLabelWithoutAnswersPenalize3);

		// Establecimiento de visible o invisible.
		if (test.getWithoutAnswersPenalize() == true)
			jPanelWithoutAnswersPenalize.setVisible(true);
		else
			jPanelWithoutAnswersPenalize.setVisible(false);

		// Reserva de memoria para el jPanelWithoutAnswers.
		jPanelWithoutAnswers = new JPanel();
		flowLayoutPanelWithoutAnswers = new FlowLayout(FlowLayout.LEFT);
		jPanelWithoutAnswers.setLayout(flowLayoutPanelWithoutAnswers);
		jPanelWithoutAnswers.setBackground(SystemColor.WHITE);
		jPanelWithoutAnswers.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Adicion de los componentes del jPanelWithoutAnswers.
		jPanelWithoutAnswers.add(jPanelWithoutAnswersLabelRadioButton);
		jPanelWithoutAnswers.add(jPanelWithoutAnswersPenalize);

		// Reserrva de memoria para el jPanelAnswersCorrection.
		jPanelAnswersCorrection = new JPanel();
		boxLayoutPanelAnswersCorrection = new BoxLayout(jPanelAnswersCorrection, BoxLayout.Y_AXIS);
		jPanelAnswersCorrection.setLayout(boxLayoutPanelAnswersCorrection);
		jPanelAnswersCorrection.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Answers Correction", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));
		jPanelAnswersCorrection.setAlignmentX(CENTER_ALIGNMENT);

		// Adicion de los componentes pertenecientes al jPanelAnswersCorrection.
		jPanelAnswersCorrection.add(jPanelIncorrectAnswers);
		jPanelAnswersCorrection.add(jPanelWithoutAnswers);

		// Reserva de memoria para el jPanelLabelKnowledge.
		jPanelLabelKnowledge = new JPanel();
		boxLayoutPanelLabelKnowledge = new BoxLayout(jPanelLabelKnowledge, BoxLayout.Y_AXIS);
		jPanelLabelKnowledge.setLayout(boxLayoutPanelLabelKnowledge);

		jLabelKnowledge1.setAlignmentX(LEFT_ALIGNMENT);
		jLabelKnowledge2.setAlignmentX(LEFT_ALIGNMENT);
		jLabelKnowledge3.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelLabelKnowledge.
		jPanelLabelKnowledge.add(jLabelKnowledge1);
		jPanelLabelKnowledge.add(jLabelKnowledge2);
		jPanelLabelKnowledge.add(jLabelKnowledge3);

		// Reserva de memoria para el jPanelKnowledgeValue.
		jPanelKnowledgeValue = new JPanel();
		flowLayoutPanelKnowledgeValue = new FlowLayout(FlowLayout.LEFT);
		jPanelKnowledgeValue.setLayout(flowLayoutPanelKnowledgeValue);
		jPanelKnowledgeValue.setBackground(SystemColor.WHITE);
		jPanelKnowledgeValue.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jSliderKnowledge.setAlignmentX(LEFT_ALIGNMENT);
		jTextFieldKnowledge.setAlignmentX(LEFT_ALIGNMENT);
		jLabelKnowledgePercentage.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes contenidos dentro de jPanelKnowledgeValue.
		jPanelKnowledgeValue.add(jSliderKnowledge);
		jPanelKnowledgeValue.add(jTextFieldKnowledge);
		jPanelKnowledgeValue.add(jLabelKnowledgePercentage);

		// Reserva de memoria para el panel que contendra a los anteriores,
		// es decir, jPanelKnowledge.
		jPanelKnowledge = new JPanel();
		flowLayoutPanelKnowledge = new FlowLayout(FlowLayout.LEFT);
		jPanelKnowledge.setLayout(flowLayoutPanelKnowledge);
		jPanelKnowledge.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Final Percentage of Knowledge", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14)));

		// Adicion de los componentes pertenecientes al jPanelKnowledge.
		jPanelKnowledge.add(jPanelLabelKnowledge);
		jPanelKnowledge.add(Box.createVerticalGlue());
		jPanelKnowledge.add(jPanelKnowledgeValue);

		// Reserva de memoria para el jPanelEvaluationParametersButton.
		jPanelEvaluationParametersButton = new JPanel();
		flowLayoutPanelEvaluationParametersButton = new FlowLayout(FlowLayout.RIGHT);
		jPanelEvaluationParametersButton.setLayout(flowLayoutPanelEvaluationParametersButton);

		jButtonResetEvaluation.setAlignmentX(RIGHT_ALIGNMENT);

		// Adici�n de los componentes del jPanelEvaluationParametersButton.
		jPanelEvaluationParametersButton.add(jButtonResetEvaluation);

		// Rerseva de memoria para el panel que contendra a los anteriores, es
		// decir, el jPanelEvalutationParameters.
		jPanelEvaluationParameters = new JPanel();
		boxLayoutPanelEvaluationParameters = new BoxLayout(jPanelEvaluationParameters, BoxLayout.Y_AXIS);
		jPanelEvaluationParameters.setLayout(boxLayoutPanelEvaluationParameters);
		jPanelEvaluationParameters.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Parameters for the student's evaluation",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));
		jPanelEvaluationParameters.setAlignmentX(CENTER_ALIGNMENT);

		jPanelEvaluationParameters.add(jPanelAnswersCorrection);
		jPanelEvaluationParameters.add(Box.createHorizontalGlue());
		jPanelEvaluationParameters.add(jPanelKnowledge);
		jPanelEvaluationParameters.add(jPanelEvaluationParametersButton);

		// Reserva de memoria para el jScrollPaneEvaluationParameters.
		jScrollPaneEvaluationParameters = new JScrollPane(jPanelEvaluationParameters,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/*
	 * NOMBRE: JPanelAdaptiveParametersInit. PERTENECE A: Clase FrmEditTest.
	 * LLAMADA POR: JPanelInit(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JPanel del frame, reservando memoria para
	 * el mismo, estableciendo su color, layout...
	 */

	private void JPanelAdaptiveParametersInit() {
		// Reserva de memoria para el jPanelLabelStartCriterion.
		jPanelLabelStartCriterion = new JPanel();
		flowLayoutPanelLabelStartCriterion = new FlowLayout(FlowLayout.LEFT);
		jPanelLabelStartCriterion.setLayout(flowLayoutPanelLabelStartCriterion);

		jLabelInitialProficiency.setAlignmentX(LEFT_ALIGNMENT);

		jPanelLabelStartCriterion.add(jLabelInitialProficiency);

		// Reserva de memoria para el jPanelSliderStartCriterion.
		jPanelSliderStartCriterion = new JPanel();
		flowLayoutPanelSliderStartCriterion = new FlowLayout(FlowLayout.LEFT);
		jPanelSliderStartCriterion.setLayout(flowLayoutPanelSliderStartCriterion);
		jPanelSliderStartCriterion.setBackground(SystemColor.WHITE);
		jPanelSliderStartCriterion.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jSliderInitialProficiency.setAlignmentX(LEFT_ALIGNMENT);
		jTextFieldInitialProficiency.setAlignmentX(LEFT_ALIGNMENT);

		jPanelSliderStartCriterion.add(jSliderInitialProficiency);
		jPanelSliderStartCriterion.add(jTextFieldInitialProficiency);

		// Reserva de memoria y establecimiento del layout para el
		// jPanelStartCriterion.
		jPanelStartCriterion = new JPanel();
		flowLayoutPanelStartCriterion = new FlowLayout(FlowLayout.LEFT);
		jPanelStartCriterion.setLayout(flowLayoutPanelStartCriterion);
		jPanelStartCriterion.setAlignmentX(LEFT_ALIGNMENT);
		jPanelStartCriterion.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Start criterion", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelLabelStartCriterion.setAlignmentX(LEFT_ALIGNMENT);
		jPanelSliderStartCriterion.setAlignmentX(LEFT_ALIGNMENT);
		jLabelInitialProficiencyInfo.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentest del jPanelStartCriterion.
		jPanelStartCriterion.add(jPanelLabelStartCriterion);
		jPanelStartCriterion.add(jPanelSliderStartCriterion);
		jPanelStartCriterion.add(jLabelInitialProficiencyInfo);

		// Reserva de memoria para el jPanelIrtModel1P.
		jPanelIrtModel1P = new JPanel();
		flowLayoutPanelIrtModel1P = new FlowLayout(FlowLayout.LEFT);
		jPanelIrtModel1P.setLayout(flowLayoutPanelIrtModel1P);
		jPanelIrtModel1P.setBackground(SystemColor.WHITE);
		jPanelIrtModel1P.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jRadioButton1P.setAlignmentX(LEFT_ALIGNMENT);
		jLabel1PInfo.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelIrtModel1P.
		jPanelIrtModel1P.add(jRadioButton1P);
		jPanelIrtModel1P.add(jLabel1PInfo);

		// Reserva de memoria para el jPanelIrtModel2P.
		jPanelIrtModel2P = new JPanel();
		flowLayoutPanelIrtModel2P = new FlowLayout(FlowLayout.LEFT);
		jPanelIrtModel2P.setLayout(flowLayoutPanelIrtModel2P);
		jPanelIrtModel2P.setBackground(SystemColor.WHITE);
		jPanelIrtModel2P.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jRadioButton2P.setAlignmentX(LEFT_ALIGNMENT);
		jLabel2PInfo.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelIrtModel2P.
		jPanelIrtModel2P.add(jRadioButton2P);
		jPanelIrtModel2P.add(jLabel2PInfo);

		// Reserva de memoria para el jPanelIrtModel3P.
		jPanelIrtModel3P = new JPanel();
		flowLayoutPanelIrtModel3P = new FlowLayout(FlowLayout.LEFT);
		jPanelIrtModel3P.setLayout(flowLayoutPanelIrtModel3P);
		jPanelIrtModel3P.setBackground(SystemColor.WHITE);
		jPanelIrtModel3P.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jRadioButton3P.setAlignmentX(LEFT_ALIGNMENT);
		jLabel3PInfo.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelIrtModel3P.
		jPanelIrtModel3P.add(jRadioButton3P);
		jPanelIrtModel3P.add(jLabel3PInfo);

		// Reserva de memoria para el jPanelContinuationCriterion.
		jPanelContinuationCriterion = new JPanel();
		boxLayoutPanelContinuationCriterion = new BoxLayout(jPanelContinuationCriterion, BoxLayout.Y_AXIS);
		jPanelContinuationCriterion.setLayout(boxLayoutPanelContinuationCriterion);
		jPanelContinuationCriterion.setAlignmentX(LEFT_ALIGNMENT);
		jPanelContinuationCriterion.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Continuation criterion (IRT Model)", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelIrtModel1P.setAlignmentX(LEFT_ALIGNMENT);
		jPanelIrtModel2P.setAlignmentX(LEFT_ALIGNMENT);
		jPanelIrtModel3P.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentest del jPanelContinuationCriterion.
		jPanelContinuationCriterion.add(jPanelIrtModel1P);
		jPanelContinuationCriterion.add(Box.createVerticalStrut(7));
		jPanelContinuationCriterion.add(jPanelIrtModel2P);
		jPanelContinuationCriterion.add(Box.createVerticalStrut(7));
		jPanelContinuationCriterion.add(jPanelIrtModel3P);

		// Rerserva de memoria para el jPanelStopCriterionRadioButton.
		jPanelStopCriterionRadioButton = new JPanel();
		boxLayoutPanelStopCriterionRadioButton = new BoxLayout(jPanelStopCriterionRadioButton, BoxLayout.Y_AXIS);
		jPanelStopCriterionRadioButton.setLayout(boxLayoutPanelStopCriterionRadioButton);

		jRadioButtonStandardError.setAlignmentX(LEFT_ALIGNMENT);
		jRadioButtonNumberItems.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelStopCriterionRadioButton.
		jPanelStopCriterionRadioButton.add(jRadioButtonStandardError);
		jPanelStopCriterionRadioButton.add(jRadioButtonNumberItems);

		// Reserva de memoria para el jPanelStandardError.
		jPanelStandardError = new JPanel();
		flowLayoutPanelStandardError = new FlowLayout(FlowLayout.CENTER);
		jPanelStandardError.setLayout(flowLayoutPanelStandardError);
		jPanelStandardError.setBackground(SystemColor.WHITE);
		jPanelStandardError.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jSliderStandardError.setAlignmentX(CENTER_ALIGNMENT);
		jTextFieldStandardError.setAlignmentX(CENTER_ALIGNMENT);

		// Adicion de los componenetes del jPanelStandardError.
		jPanelStandardError.add(jSliderStandardError);
		jPanelStandardError.add(jTextFieldStandardError);

		// Comprobaci�n de si mostrado u ocultado del panel.
		if (jRadioButtonStandardError.isSelected() == true)
			jPanelStandardError.setVisible(true);
		else
			jPanelStandardError.setVisible(false);

		// Reserva de memoria para el jPanelNumberItems.
		jPanelNumberItems = new JPanel();
		flowLayoutPanelNumberItems = new FlowLayout(FlowLayout.CENTER);
		jPanelNumberItems.setLayout(flowLayoutPanelNumberItems);
		jPanelNumberItems.setBackground(SystemColor.WHITE);
		jPanelNumberItems.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jSliderNumberItems.setAlignmentX(CENTER_ALIGNMENT);
		jTextFieldNumberItems.setAlignmentX(CENTER_ALIGNMENT);

		// Adicion de los componenetes del jPanelNumberItems.
		jPanelNumberItems.add(jSliderNumberItems);
		jPanelNumberItems.add(jTextFieldNumberItems);

		// Comprobaci�n de si mostrado u ocultado del panel.
		if (jRadioButtonNumberItems.isSelected() == true)
			jPanelNumberItems.setVisible(true);
		else
			jPanelNumberItems.setVisible(false);

		// Reserva de memoria y establecimiento del layout para el
		// jPanelStopCriterion.
		jPanelStopCriterion = new JPanel();
		flowLayoutPanelStopCriterion = new FlowLayout(FlowLayout.LEFT);
		jPanelStopCriterion.setLayout(flowLayoutPanelStopCriterion);
		jPanelStopCriterion.setAlignmentX(LEFT_ALIGNMENT);
		jPanelStopCriterion.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Stop criterion", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelStopCriterionRadioButton.setAlignmentX(LEFT_ALIGNMENT);
		jPanelStandardError.setAlignmentX(LEFT_ALIGNMENT);
		jPanelNumberItems.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentest del jPanelStopCriterion.
		jPanelStopCriterion.add(jPanelStopCriterionRadioButton);
		jPanelStopCriterion.add(jPanelStandardError);
		jPanelStopCriterion.add(jPanelNumberItems);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al JPanel anteriores.
		jPanelAdaptiveParameters = new JPanel();
		boxLayoutPanelAdaptiveParameters = new BoxLayout(jPanelAdaptiveParameters, BoxLayout.Y_AXIS);
		jPanelAdaptiveParameters.setLayout(boxLayoutPanelAdaptiveParameters);
		jPanelAdaptiveParameters.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Parameters for de adaptive execution ", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));
		jPanelAdaptiveParameters.setAlignmentX(CENTER_ALIGNMENT);

		jPanelStartCriterion.setAlignmentX(LEFT_ALIGNMENT);
		jPanelContinuationCriterion.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentest del jPanelPresesntationParameters.
		jPanelAdaptiveParameters.add(jPanelStartCriterion);
		jPanelAdaptiveParameters.add(Box.createVerticalGlue());
		jPanelAdaptiveParameters.add(jPanelContinuationCriterion);
		jPanelAdaptiveParameters.add(Box.createVerticalGlue());
		jPanelAdaptiveParameters.add(jPanelStopCriterion);

		// Reserva de memoria para el jScrollPanePresentationParameters.
		jScrollPaneAdaptiveParameters = new JScrollPane(jPanelAdaptiveParameters,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/*
	 * NOMBRE: JPanelStudentsInit. PERTENECE A: Clase FrmEditTest. LLAMADA POR:
	 * El m�todo JPanelInit(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JPanel del frame, reservando memoria para
	 * el mismo, estableciendo su color, layout...
	 */

	private void JPanelStudentsInit() {
		// Reserva de memoria para el jScrollPaneJListLogin.
		jScrollPaneListLogin = new JScrollPane(jListLogin, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Reserva de memoria para el jPanelJListLoginButton.
		jPanelJListLoginButton = new JPanel();
		flowLayoutPanelJListLoginButton = new FlowLayout(FlowLayout.RIGHT);
		jPanelJListLoginButton.setLayout(flowLayoutPanelJListLoginButton);

		// Adici�n de los componentes del jPanelJListLoginButton.
		jPanelJListLoginButton.add(jLabelTotalStudents);
		jPanelJListLoginButton.add(jTextFieldTotalStudents);
		jPanelJListLoginButton.add(Box.createHorizontalStrut(10));
		jPanelJListLoginButton.add(jButtonSelectAllStudents);

		// Reserva de memoria para el jPanelJlistLogin.
		jPanelListLogin = new JPanel();
		boxLayoutPanelJListLogin = new BoxLayout(jPanelListLogin, BoxLayout.Y_AXIS);
		jPanelListLogin.setLayout(boxLayoutPanelJListLogin);
		jPanelListLogin.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jLabelLogin.setAlignmentX(CENTER_ALIGNMENT);
		jLabelLogin.setAlignmentY(CENTER_ALIGNMENT);
		jListLogin.setAlignmentX(CENTER_ALIGNMENT);
		jListLogin.setAlignmentY(CENTER_ALIGNMENT);
		jPanelJListLoginButton.setAlignmentX(CENTER_ALIGNMENT);
		jPanelJListLoginButton.setAlignmentY(CENTER_ALIGNMENT);

		// Adici�n de los componentes del jPanelJListLogin.
		jPanelListLogin.add(jLabelLogin);
		jPanelListLogin.add(Box.createVerticalStrut(10));
		jPanelListLogin.add(jScrollPaneListLogin);
		jPanelListLogin.add(jPanelJListLoginButton);

		// Reserva de memoria para el jPanelFinish.
		jPanelFinish = new JPanel();
		flowLayoutPanelFinish = new FlowLayout(FlowLayout.LEFT);
		jPanelFinish.setLayout(flowLayoutPanelFinish);
		jPanelFinish.setBackground(SystemColor.WHITE);
		jPanelFinish.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jCheckBoxFinish.setAlignmentX(LEFT_ALIGNMENT);
		jCheckBoxFinish.setAlignmentY(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelFinish.
		jPanelFinish.add(jCheckBoxFinish);

		// Reserva de memoria para el jPanelCanRepeat.
		jPanelCanRepeat = new JPanel();
		flowLayoutPanelCanRepeat = new FlowLayout(FlowLayout.LEFT);
		jPanelCanRepeat.setLayout(flowLayoutPanelCanRepeat);
		jPanelCanRepeat.setBackground(SystemColor.WHITE);
		jPanelCanRepeat.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jCheckBoxCanRepeat.setAlignmentX(LEFT_ALIGNMENT);
		jCheckBoxCanRepeat.setAlignmentY(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelCanRepeat.
		jPanelCanRepeat.add(jCheckBoxCanRepeat);

		// Reserva de memoria para el jPanelDeleteStudent.
		jPanelDeleteStudent = new JPanel();
		flowLayoutPanelDeleteStudent = new FlowLayout(FlowLayout.LEFT);
		jPanelDeleteStudent.setLayout(flowLayoutPanelDeleteStudent);
		jPanelDeleteStudent.setBackground(SystemColor.WHITE);
		jPanelDeleteStudent.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jCheckBoxDeleteStudent.setAlignmentX(LEFT_ALIGNMENT);
		jCheckBoxDeleteStudent.setAlignmentY(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelDeleteStudent.
		jPanelDeleteStudent.add(jCheckBoxDeleteStudent);

		// Reserva de memoria para el jPanelScore.
		jPanelScore = new JPanel();
		flowLayoutPanelScore = new FlowLayout(FlowLayout.LEFT);
		jPanelScore.setLayout(flowLayoutPanelScore);
		jPanelScore.setBackground(SystemColor.WHITE);
		jPanelScore.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jLabelScore.setAlignmentX(LEFT_ALIGNMENT);
		jTextFieldScore.setAlignmentX(LEFT_ALIGNMENT);
		jLabelScore.setAlignmentY(LEFT_ALIGNMENT);
		jTextFieldScore.setAlignmentY(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelScore.
		jPanelScore.add(jLabelScore);
		jPanelScore.add(jTextFieldScore);

		// Reserva de memoria para el jPanelProficiency.
		jPanelProficiency = new JPanel();
		flowLayoutPanelProficiency = new FlowLayout(FlowLayout.LEFT);
		jPanelProficiency.setLayout(flowLayoutPanelProficiency);
		jPanelProficiency.setBackground(SystemColor.WHITE);
		jPanelProficiency.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jLabelProficiency.setAlignmentX(LEFT_ALIGNMENT);
		jTextFieldProficiency.setAlignmentX(LEFT_ALIGNMENT);
		jLabelProficiency.setAlignmentY(LEFT_ALIGNMENT);
		jTextFieldProficiency.setAlignmentY(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelProficiency.
		jPanelProficiency.add(jLabelProficiency);
		jPanelProficiency.add(jTextFieldProficiency);

		// Reserva de memoria para el jPanelStudentStandardError.
		jPanelStudentStandardError = new JPanel();
		flowLayoutPanelStudentStandardError = new FlowLayout(FlowLayout.LEFT);
		jPanelStudentStandardError.setLayout(flowLayoutPanelStudentStandardError);
		jPanelStudentStandardError.setBackground(SystemColor.WHITE);
		jPanelStudentStandardError.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jLabelStandardError.setAlignmentX(LEFT_ALIGNMENT);
		jTextFieldStudentStandardError.setAlignmentX(LEFT_ALIGNMENT);
		jLabelStandardError.setAlignmentY(LEFT_ALIGNMENT);
		jTextFieldStudentStandardError.setAlignmentY(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelStandardError.
		jPanelStudentStandardError.add(jLabelStandardError);
		jPanelStudentStandardError.add(jTextFieldStudentStandardError);

		// Reserva de memoria para el jPanelMake.
		jPanelMake = new JPanel();
		flowLayoutPanelMake = new FlowLayout(FlowLayout.LEFT);
		jPanelMake.setLayout(flowLayoutPanelMake);
		jPanelMake.setBackground(SystemColor.WHITE);
		jPanelMake.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jLabelMake1.setAlignmentX(LEFT_ALIGNMENT);
		jTextFieldMake.setAlignmentX(LEFT_ALIGNMENT);
		jLabelMake2.setAlignmentX(LEFT_ALIGNMENT);
		jLabelMake1.setAlignmentY(LEFT_ALIGNMENT);
		jTextFieldMake.setAlignmentY(LEFT_ALIGNMENT);
		jLabelMake2.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelMake.
		jPanelMake.add(jLabelMake1);
		jPanelMake.add(jTextFieldMake);
		jPanelMake.add(jLabelMake2);

		// Reserva de memoria para el jPanelStudetsButton.
		jPanelStudentsButton = new JPanel();
		flowLayoutPanelStudentsButton = new FlowLayout(FlowLayout.LEFT);
		jPanelStudentsButton.setLayout(flowLayoutPanelStudentsButton);

		jButtonFinishTest.setAlignmentX(LEFT_ALIGNMENT);
		jButtonCanRepeat.setAlignmentX(LEFT_ALIGNMENT);
		jButtonNoCanRepeat.setAlignmentX(LEFT_ALIGNMENT);
		jButtonResetStudents.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelStudentsButton.
		jPanelStudentsButton.add(jButtonFinishTest);
		jPanelStudentsButton.add(Box.createHorizontalStrut(20));
		jPanelStudentsButton.add(jButtonCanRepeat);
		jPanelStudentsButton.add(jButtonNoCanRepeat);
		jPanelStudentsButton.add(Box.createHorizontalStrut(20));
		jPanelStudentsButton.add(jButtonResetStudents);

		// Reserva de memoria para el jPanelStudetsButtonDelete.
		jPanelStudentsButtonDelete = new JPanel();
		flowLayoutPanelStudentsButtonDelete = new FlowLayout(FlowLayout.CENTER);
		jPanelStudentsButtonDelete.setLayout(flowLayoutPanelStudentsButtonDelete);

		jButtonDeleteStudent.setAlignmentX(CENTER_ALIGNMENT);
		jButtonNoDeleteStudent.setAlignmentX(CENTER_ALIGNMENT);

		// Adici�n de los componentes del jPanelStudentsButtonDelete.
		jPanelStudentsButtonDelete.add(jButtonDeleteStudent);
		jPanelStudentsButton.add(Box.createHorizontalStrut(20));
		jPanelStudentsButtonDelete.add(jButtonNoDeleteStudent);

		// Reserva de memoria para el jPanelStudentsData.
		jPanelStudentsData = new JPanel();
		boxLayoutPanelStudentsData = new BoxLayout(jPanelStudentsData, BoxLayout.Y_AXIS);
		jPanelStudentsData.setLayout(boxLayoutPanelStudentsData);

		jPanelFinish.setAlignmentX(CENTER_ALIGNMENT);
		jPanelCanRepeat.setAlignmentX(CENTER_ALIGNMENT);
		jPanelDeleteStudent.setAlignmentX(CENTER_ALIGNMENT);
		jPanelScore.setAlignmentX(CENTER_ALIGNMENT);
		jPanelProficiency.setAlignmentX(CENTER_ALIGNMENT);
		jPanelStudentStandardError.setAlignmentX(CENTER_ALIGNMENT);
		jPanelMake.setAlignmentX(CENTER_ALIGNMENT);
		jPanelStudentsButton.setAlignmentX(CENTER_ALIGNMENT);
		jPanelStudentsButtonDelete.setAlignmentX(CENTER_ALIGNMENT);

		// Adici�n de los componentes del jPanelStudentsData.
		int size = 10;
		if (executionTypeString.equals(TestEditor.ADAPTIVE))
			size = 3;
		else
			size = 10;
		jPanelStudentsData.add(jPanelFinish);
		jPanelStudentsData.add(Box.createVerticalStrut(size));
		jPanelStudentsData.add(jPanelCanRepeat);
		jPanelStudentsData.add(Box.createVerticalStrut(size));
		jPanelStudentsData.add(jPanelDeleteStudent);
		jPanelStudentsData.add(Box.createVerticalStrut(size));
		jPanelStudentsData.add(jPanelScore);
		jPanelStudentsData.add(Box.createVerticalStrut(size));

		if (executionTypeString.equals(TestEditor.ADAPTIVE)) {
			jPanelStudentsData.add(jPanelProficiency);
			jPanelStudentsData.add(Box.createVerticalStrut(size));
			jPanelStudentsData.add(jPanelStudentStandardError);
			jPanelStudentsData.add(Box.createVerticalStrut(size));
		}

		jPanelStudentsData.add(jPanelMake);
		jPanelStudentsData.add(Box.createVerticalStrut(size));
		jPanelStudentsData.add(jPanelStudentsButton);
		jPanelStudentsData.add(jPanelStudentsButtonDelete);

		// Reserva de memoria para el jPanelStudentsLogin.
		jPanelStudentsLogin = new JPanel();
		flowLayoutPanelStudentsLogin = new FlowLayout(FlowLayout.LEFT);
		jPanelStudentsLogin.setLayout(flowLayoutPanelStudentsLogin);
		jPanelStudentsLogin.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Students that have carried out the test.",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelListLogin.setAlignmentX(CENTER_ALIGNMENT);
		jPanelStudentsData.setAlignmentX(CENTER_ALIGNMENT);
		jPanelListLogin.setAlignmentY(CENTER_ALIGNMENT);
		jPanelStudentsData.setAlignmentY(CENTER_ALIGNMENT);

		// Adici�n de los componentes del jPanelStudents.
		jPanelStudentsLogin.add(jPanelListLogin);
		jPanelStudentsLogin.add(Box.createHorizontalStrut(5));
		jPanelStudentsLogin.add(jPanelStudentsData);

		// Reserva de memoria para el jPanelEnableTest.
		jPanelEnableTest = new JPanel();
		flowLayoutPanelEnableTest = new FlowLayout(FlowLayout.LEFT);
		jPanelEnableTest.setLayout(flowLayoutPanelEnableTest);
		jPanelEnableTest.setBackground(SystemColor.WHITE);
		jPanelEnableTest.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jCheckBoxEnableTest.setAlignmentX(LEFT_ALIGNMENT);
		jLabelEnableTest.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelEnableTest.
		jPanelEnableTest.add(jCheckBoxEnableTest);
		jPanelEnableTest.add(jLabelEnableTest);

		// Reserva de memoria para el jPanelStudents.
		jPanelStudents = new JPanel();
		boxLayoutPanelStudents = new BoxLayout(jPanelStudents, BoxLayout.Y_AXIS);
		jPanelStudents.setLayout(boxLayoutPanelStudents);

		jPanelStudentsLogin.setAlignmentX(LEFT_ALIGNMENT);
		jPanelEnableTest.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelStudents.
		jPanelStudents.add(jPanelEnableTest);
		jPanelStudents.add(Box.createVerticalGlue());
		jPanelStudents.add(jPanelStudentsLogin);

		// Reserva de memoria para el jScrollPaneEvaluationParameters.
		jScrollPaneStudents = new JScrollPane(jPanelStudents, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/*
	 * NOMBRE: JPanelTestQuestionInit. PERTENECE A: Clase FrmEditTest. LLAMADA
	 * POR: El m�todo JPanelInit(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JPanel del frame, reservando memoria para
	 * el mismo, estableciendo su color, layout...
	 */

	private void JPanelTestQuestionInit() {
		// Reserva de memoria para el jPanelTestType.
		jPanelTestType = new JPanel();
		flowLayoutPanelTestType = new FlowLayout(FlowLayout.RIGHT);
		jPanelTestType.setLayout(flowLayoutPanelTestType);

		jLabelTestType.setAlignmentX(RIGHT_ALIGNMENT);
		jTextFieldTestType.setAlignmentX(RIGHT_ALIGNMENT);

		// Adici�n de los componentes del jPanelTestType.
		jPanelTestType.add(jLabelTestType);
		jPanelTestType.add(jTextFieldTestType);

		// Reserva de memoria del jPanelAbastractConcept, si corresponde.
		if (test.getTestType().trim().equals("exam")) {
			jPanelAbstractConcept = new JPanel();
			flowLayoutPanelAbstractConcept = new FlowLayout(FlowLayout.RIGHT);
			jPanelAbstractConcept.setLayout(flowLayoutPanelAbstractConcept);

			jLabelAbstractConcept.setAlignmentX(RIGHT_ALIGNMENT);
			jTextFieldAbstractConcept.setAlignmentX(RIGHT_ALIGNMENT);

			// Adici�n de los componentes del jPanelAbstractConcept.
			jPanelAbstractConcept.add(jLabelAbstractConcept);
			jPanelAbstractConcept.add(jTextFieldAbstractConcept);
		}

		// Reserva de memoria del jPanelConcept.
		jPanelConcept = new JPanel();
		flowLayoutPanelConcept = new FlowLayout(FlowLayout.RIGHT);
		jPanelConcept.setLayout(flowLayoutPanelConcept);

		jLabelConcept.setAlignmentX(RIGHT_ALIGNMENT);

		// Adici�n de los componentes del jPanelConcept.
		jPanelConcept.add(jLabelConcept);

		if (test.getTestType().trim().equals("exam")) {
			jScrollPaneListConcept = new JScrollPane(jListConcept, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPaneListConcept.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			jScrollPaneListConcept.setAlignmentX(RIGHT_ALIGNMENT);
			jListConcept.setAlignmentX(RIGHT_ALIGNMENT);

			// Adici�n de los componentes del jPanelConcept.
			jPanelConcept.add(jScrollPaneListConcept);
		} else {
			jTextFieldConcept.setAlignmentX(RIGHT_ALIGNMENT);

			// Adici�n de los componentes del jPanelConcept.
			jPanelConcept.add(jTextFieldConcept);
		}

		// Reserva de memoria del jPanelTotalQuestions.
		jPanelTotalQuestions = new JPanel();
		flowLayoutPanelTotalQuestions = new FlowLayout(FlowLayout.RIGHT);
		jPanelTotalQuestions.setLayout(flowLayoutPanelTotalQuestions);

		jLabelTotalQuestions.setAlignmentX(LEFT_ALIGNMENT);
		jTextFieldTotalQuestions.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelTotalQuestions.
		jPanelTotalQuestions.add(jLabelTotalQuestions);
		jPanelTotalQuestions.add(jTextFieldTotalQuestions);

		// Reserva de memoria para el jPanelJTreeTestQuestionButton.
		jPanelTreeTestQuestionButton = new JPanel();
		flowLayoutPanelJTreeTestQuestionButton = new FlowLayout(FlowLayout.RIGHT);
		jPanelTreeTestQuestionButton.setLayout(flowLayoutPanelJTreeTestQuestionButton);

		jButtonExamineTestQuestion.setAlignmentX(RIGHT_ALIGNMENT);
		jButtonDeleteQuestionToTest.setAlignmentX(RIGHT_ALIGNMENT);
		jButtonResetQuestionToTest.setAlignmentX(RIGHT_ALIGNMENT);

		// Adici�n de los componentes del jPanelJTreeTestQuestionButton.
		jPanelTreeTestQuestionButton.add(jButtonExamineTestQuestion);
		jPanelTreeTestQuestionButton.add(Box.createHorizontalStrut(20));
		jPanelTreeTestQuestionButton.add(jButtonDeleteQuestionToTest);
		jPanelTreeTestQuestionButton.add(jButtonResetQuestionToTest);

		// Reserva de memoria del jPanelTypeAndConcept.
		jPanelTypeAndConcept = new JPanel();
		boxLayoutPanelTypeAndConcept = new BoxLayout(jPanelTypeAndConcept, BoxLayout.Y_AXIS);
		jPanelTypeAndConcept.setLayout(boxLayoutPanelTypeAndConcept);

		jPanelTestType.setAlignmentX(RIGHT_ALIGNMENT);
		if (test.getTestType().trim().equals("exam"))
			jPanelAbstractConcept.setAlignmentX(RIGHT_ALIGNMENT);

		jPanelConcept.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelTotalQuestions.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelTreeTestQuestionButton.setAlignmentX(RIGHT_ALIGNMENT);

		// Adici�n de los componentes del jPanelTypeAndConcept.
		jPanelTypeAndConcept.add(jPanelTestType);
		jPanelTypeAndConcept.add(Box.createVerticalStrut(10));

		if (test.getTestType().trim().equals("exam")) {
			jPanelTypeAndConcept.add(jPanelAbstractConcept);
			jPanelTypeAndConcept.add(Box.createVerticalStrut(10));
		}
		jPanelTypeAndConcept.add(jPanelConcept);
		jPanelTypeAndConcept.add(Box.createVerticalStrut(10));
		jPanelTypeAndConcept.add(jPanelTotalQuestions);
		jPanelTypeAndConcept.add(Box.createVerticalStrut(10));
		jPanelTypeAndConcept.add(jPanelTreeTestQuestionButton);

		// Reserva de memoria para el jPanelJTreeTestQuestion.
		jPanelTreeTestQuestion = new JPanel();
		flowLayoutPanelJTreeTestQuestion = new FlowLayout(FlowLayout.LEFT);
		jPanelTreeTestQuestion.setLayout(flowLayoutPanelJTreeTestQuestion);
		jPanelTreeTestQuestion.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jScrollPaneTreeTestQuestion = new JScrollPane(jTreeTestQuestion,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		jTreeTestQuestion.setAlignmentX(LEFT_ALIGNMENT);
		jScrollPaneTreeTestQuestion.setAlignmentX(LEFT_ALIGNMENT);
		jPanelTypeAndConcept.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelJTreeTestQuestion.
		jPanelTreeTestQuestion.add(jScrollPaneTreeTestQuestion);
		jPanelTreeTestQuestion.add(jPanelTypeAndConcept);

		// Reserva de memoria para el jScrollPaneJTableTestQuestion.
		jScrollPaneTableTestQuestion = new JScrollPane(jTableTestQuestion,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneTableTestQuestion.setAlignmentX(CENTER_ALIGNMENT);

		jScrollPaneTableTestQuestion.setSize(jTableTestQuestion.getSize());

		// Reserva de memoria para el jPanelTestQuestion.
		jPanelTestQuestion = new JPanel();
		boxLayoutPanelTestQuestion = new BoxLayout(jPanelTestQuestion, BoxLayout.Y_AXIS);
		jPanelTestQuestion.setLayout(boxLayoutPanelTestQuestion);
		jPanelTestQuestion.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Questions that form the test.", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelTreeTestQuestion.setAlignmentX(CENTER_ALIGNMENT);

		// Reserva de memoria e inicializaci�n para jSplitPane.
		jSplitPaneTestQuestion = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, jScrollPaneTableTestQuestion,
				jPanelTreeTestQuestion);
		jSplitPaneTestQuestion.setOneTouchExpandable(true);
		jSplitPaneTestQuestion.setDividerSize(10);
		jSplitPaneTestQuestion.setDividerLocation(0);
		jSplitPaneTestQuestion.setAlignmentX(CENTER_ALIGNMENT);

		// Adici�n de los componentes del jPanelTestQuestion.
		jPanelTestQuestion.add(jSplitPaneTestQuestion);

		// Reserva de memoria para el jScrollPaneTestQuestion.
		jScrollPaneTestQuestion = new JScrollPane(jPanelTestQuestion, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	}

	/*
	 * NOMBRE: JPanelAddQuestionToTestInit. PERTENECE A: Clase FrmEditTest.
	 * LLAMADA POR: El m�todo JPanelInit(); LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JPanel del frame,
	 * reservando memoria para el mismo, estableciendo su color, layout...
	 */

	private void JPanelAddQuestionToTestInit() {
		// Reserva de memoria para el jPanelAddQuestionToTestButton.
		jPanelAddQuestionToTestButton = new JPanel();
		boxLayoutPanelAddQuestionToTestButton = new BoxLayout(jPanelAddQuestionToTestButton, BoxLayout.Y_AXIS);
		jPanelAddQuestionToTestButton.setLayout(boxLayoutPanelAddQuestionToTestButton);

		jLabelQuestionsFile.setAlignmentX(CENTER_ALIGNMENT);
		jComboBoxQuestionsFile.setAlignmentX(CENTER_ALIGNMENT);
		jButtonGetQuestionsFileList.setAlignmentX(CENTER_ALIGNMENT);
		jButtonExamineQuestionsFile.setAlignmentX(CENTER_ALIGNMENT);
		jButtonAddQuestionToTest.setAlignmentX(CENTER_ALIGNMENT);

		// Adici�n de los componentes del jPanelAddQuestionToTestButton.
		jPanelAddQuestionToTestButton.add(jLabelQuestionsFile);
		jPanelAddQuestionToTestButton.add(jComboBoxQuestionsFile);
		jPanelAddQuestionToTestButton.add(Box.createVerticalStrut(10));
		jPanelAddQuestionToTestButton.add(jButtonGetQuestionsFileList);
		jPanelAddQuestionToTestButton.add(Box.createVerticalStrut(10));
		jPanelAddQuestionToTestButton.add(jButtonExamineQuestionsFile);
		jPanelAddQuestionToTestButton.add(Box.createVerticalStrut(10));
		jPanelAddQuestionToTestButton.add(jButtonAddQuestionToTest);

		// Reserva de memoria para el jPanelJTreeQuestionsFile.
		jPanelTreeQuestionsFile = new JPanel();
		flowLayoutPanelJTreeQuestionsFile = new FlowLayout(FlowLayout.LEFT);
		jPanelTreeQuestionsFile.setLayout(flowLayoutPanelJTreeQuestionsFile);
		jPanelTreeQuestionsFile.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jScrollPaneTreeQuestionsFile = new JScrollPane(jTreeQuestionsFile,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		jTreeQuestionsFile.setAlignmentX(LEFT_ALIGNMENT);
		jScrollPaneTreeQuestionsFile.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelJTreeQuestionsFile.
		jPanelTreeQuestionsFile.add(jPanelAddQuestionToTestButton);
		jPanelTreeQuestionsFile.add(Box.createHorizontalStrut(30));
		jPanelTreeQuestionsFile.add(jScrollPaneTreeQuestionsFile);

		// Reserva de memoria para el jScrollPaneJTableQuestionsFile.
		jScrollPaneTableQuestionsFile = new JScrollPane(jTableQuestionsFile,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneTableQuestionsFile.setAlignmentX(CENTER_ALIGNMENT);

		jScrollPaneTableQuestionsFile.setSize(jTableQuestionsFile.getSize());

		// Reserva de memoria para el jPanelAddQuestionToTest.
		jPanelAddQuestionToTest = new JPanel();
		boxLayoutPanelAddQuestionToTest = new BoxLayout(jPanelAddQuestionToTest, BoxLayout.Y_AXIS);
		jPanelAddQuestionToTest.setLayout(boxLayoutPanelAddQuestionToTest);
		jPanelAddQuestionToTest.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Questions to add to the test.", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelTreeQuestionsFile.setAlignmentX(CENTER_ALIGNMENT);

		// Reserva de memoria e inicializaci�n para jSplitPane.
		jSplitPaneAddQuestionToTest = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
				jScrollPaneTableQuestionsFile, jPanelTreeQuestionsFile);
		jSplitPaneAddQuestionToTest.setOneTouchExpandable(true);
		jSplitPaneAddQuestionToTest.setDividerSize(10);
		jSplitPaneAddQuestionToTest.setDividerLocation(0);
		jSplitPaneAddQuestionToTest.setAlignmentX(CENTER_ALIGNMENT);

		// Adici�n de los componentes del jPanelAddQuestionToTest.
		jPanelAddQuestionToTest.add(jSplitPaneAddQuestionToTest);

		// Reserva de memoria para el jScrollPaneTestQuestion.
		jScrollPaneAddQuestionToTest = new JScrollPane(jPanelAddQuestionToTest,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/**
	 * Initializes the window, setting the event listeners and
	 * adjusting the minimun size of it.
	 * Makes visible the frame
	 */
	private void frameInit() throws Exception {
	//	
		// Sets the main content panel
		this.setContentPane(jPanelContent);
		
		// Shows the window after it has been configured
		setVisible(true);

		if (initFrame == false) {
			// Sets the event listeners of the window
	      addInternalFrameListener(new javax.swing.event.InternalFrameAdapter(){
	      	public void internalFrameOpened(javax.swing.event.InternalFrameEvent e){
	         	try {
	         		e.getInternalFrame().setMaximum(true);
	         		parent.getDesktopPane().getDesktopManager().maximizeFrame(e.getInternalFrame());
	         		e.getInternalFrame().moveToFront();
	         	} catch (Exception e1) {
	         		e1.printStackTrace();
	         	}
	      	}
	      	public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent e) {
	      		try {
		      		e.getInternalFrame().moveToBack();
	         	} catch (Exception e1) {
	         		e1.printStackTrace();
	         	}
	      	}
	      	public void internalFrameActivated(javax.swing.event.InternalFrameEvent e) {
	      		try {
		      		e.getInternalFrame().moveToFront();
	         	} catch (Exception e1) {
	         		e1.printStackTrace();
	         	}
	      	}
	      	public void internalFrameIconified(javax.swing.event.InternalFrameEvent e) {
	      		try {
	      			e.getInternalFrame().moveToBack();
	         	} catch (Exception e1) {
	         		e1.printStackTrace();
	         	}
	      	}
	      	public void internalFrameClosing(javax.swing.event.InternalFrameEvent e) {
	      	  try {
	      			closingWindow();
	         	} catch (Exception e1) {
	         		e1.printStackTrace();
	         	}
	      	} 
	      });
	      
	      // Control of X and Y location
	      windowLocationControl();
		}
	
		// Control of size
		windowSizeControl();
		
		initFrame = true;
	}
	
	// //////////////////////////////////////////////////////////////////////////////
	// //////// FUNCIONALIDADES DE LOS COMPONETES DEL JINTERNALFRAME
	// ////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: closingWindow. PERTENECE A: Clase FrmEditTest. LLAMADA POR: Al
	 * cerrar el frame. LLAMA A: jButtonModifyActionPerformed(); RECIBE:
	 * nada. DEVUELVE: void. FUNCI�N: Comprueba si se ha salvado las udpates en
	 * el test antes de cerrar el frame. Si no se han salvado las
	 * updates, entonces pregunta al usuario si desea salvar dichos datos. En
	 * caso afirmativo llama a los m�todos de esta misma clase encargados de
	 * realizar esas operaciones.
	 */

	public int closingWindow() {
		// Comprobaci�n que la pregunta no ha sido salvada y si modificada.
		if (testModified == true || enableModified == true || testTransformed == true
				|| presentationParametersModified == true || htmlParametersModified == true
				|| studentsModified == true || evaluationParametersModified == true
				|| irtParametersModified == true) {
			// Mensaje para preguntar al usuario si desea salvar las updates
			// antes de cerrar el frame.
			int result = JOptionPane.showConfirmDialog(this,
					"Do you want to save the test configuration before exit?", "Save test",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				jButtonModifyActionPerformed();

				if (testSaved == false) {
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
					return DO_NOTHING_ON_CLOSE;
				} else {
					setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					dispose();
					return DISPOSE_ON_CLOSE;
				}
			} else {
				if (result == JOptionPane.NO_OPTION) {
					testSaved = true;
					setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					dispose();
					return DISPOSE_ON_CLOSE;
				} else {
					if (result == JOptionPane.CANCEL_OPTION)
						setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

					return DO_NOTHING_ON_CLOSE;
				}
			}
		} else {
			dispose();
			return DISPOSE_ON_CLOSE;
		}
	}

	/*
	 * NOMBRE: jButtonExamineTestQuestionActionPerformed. PERTENECE A: Clase
	 * FrmEditTest. LLAMADA POR: Al hacer click sobre cualquier
	 * jButtonExamineTestQuestion. LLAMA A: nada. RECIBE: nada. DEVUELVE: void.
	 * FUNCI�N: Asigna los valores del jTreeTestQuestion.
	 */

	void jButtonExamineTestQuestionActionPerformed() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		questionsOfMultipleFiles = null;
		questionsOfMultipleFiles = getQuestionData(test);

		if (questionsOfMultipleFiles == null || questionsOfMultipleFiles.isEmpty()) {
			JOptionPane.showMessageDialog(this, "ERROR: The questions data that form the test " + "\n"
					+ "HAVE NOT BEEN OBTAINED CORRECTLY.", "Error to search questions", JOptionPane.ERROR_MESSAGE);

			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			return;
		} else {
			int indexTab = jTabbedPane.getSelectedIndex();
			int locationSplitTestQuestion = jSplitPaneTestQuestion.getDividerLocation();
			int locationSplitAddQuestion = jSplitPaneAddQuestionToTest.getDividerLocation();
			JTreeTestQuestionInit();
			JTableTestQuestionInit();
			JTextFieldInit1();
			JPanelInit();

			try {
				frameInit();
			} catch (java.lang.Exception e) {}

			jTabbedPane.setSelectedIndex(indexTab);
			jSplitPaneTestQuestion.setDividerLocation(locationSplitTestQuestion);
			jSplitPaneAddQuestionToTest.setDividerLocation(locationSplitAddQuestion);

			jButtonExamineTestQuestion.setEnabled(false);
			jButtonResetQuestionToTest.setEnabled(true);

			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/*
	 * NOMBRE: jButtonDeleteQuestionToTestActionPerformed. PERTENECE A: Clase
	 * FrmEditTest. LLAMADA POR: Al hacer click sobre cualquier
	 * jButtonDeleteQuestionToTest. LLAMA A: nada. RECIBE: nada. DEVUELVE: void.
	 * FUNCI�N: Elimina nodos con las preguntas del test ya incluidas en el
	 * jTreeTestQuestion.
	 */

	void jButtonDeleteQuestionToTestActionPerformed() {
		boolean delete = false;
		// boolean deleteConcept = false;

		if (jListConcept != null && jListConcept.getSelectedIndices().length > 0
				&& questionsOfMultipleFiles != null) {
			for (int i = 0; i < jListConcept.getSelectedIndices().length; i++) {
				Vector indexVector = new Vector();

				for (int j = 0; j < questionsOfMultipleFiles.size(); j++) {
					Vector nameAndQuestion = (Vector) questionsOfMultipleFiles.get(j);
					Question question = (Question) ((Vector) nameAndQuestion.get(1)).firstElement();
					String concept = question.getConcept();
					String conceptSmall = "";
					if (concept.indexOf(question.getCourse()) == 0
							&& concept.lastIndexOf(String.valueOf(jListConcept.getSelectedValues()[i]).trim()) != -1)
						conceptSmall = concept.substring(concept.lastIndexOf(String.valueOf(
								jListConcept.getSelectedValues()[i]).trim()));

					if (questionsOfMultipleFiles.size() == 1) {
						JOptionPane.showMessageDialog(this, "You cannot remove the question file. " + "\n"
								+ "The test should be formed by one question file as minimum.", "Warning",
								JOptionPane.WARNING_MESSAGE);
						break;
					}

					// Borrado de los ficheros de preguntas relacionados con los
					// conceptos seleccionados de la jListConcept.
					if (conceptSmall.equals(String.valueOf(jListConcept.getSelectedValues()[i]).trim())) {
						while (conceptVectorWithRepetition.contains(concept))
							conceptVectorWithRepetition.remove(concept);

						conceptVectorInitial.remove(concept);
						indexVector.add(String.valueOf(j));
					}
				}

				// Borrado de las preguntas del Vector questionsOfMultipleFiles.
				for (int j = indexVector.size() - 1; j > -1; j--)
					questionsOfMultipleFiles.removeElementAt(Integer.valueOf(indexVector.get(j).toString())
							.intValue());
			}

			testModified = true;
		} else {
			if (jTreeTestQuestion.getSelectionCount() > 0) {
				// Comprobaci�n de si se va borrar un fichero de preguntas completo.
				if (codeOfQuestionsToDelete.equals("ALL_FILE")) {
					// Borrado de las filas necesarias del jTableTestQuestion.
					for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
						Vector nameAndQuestion = (Vector) questionsOfMultipleFiles.get(i);
						if (nameAndQuestion.get(0).toString().equals(questionsFileNameToDelete)) {
							if (questionsOfMultipleFiles.size() == 1) {
								JOptionPane.showMessageDialog(this, "You cannot remove the question file. " + "\n"
										+ "The test should be formed by one question file as minimum.", "Warning",
										JOptionPane.WARNING_MESSAGE);
								break;
							} else {
								// Lectura del nombre del concepto relacionado con el
								// fichero de
								// preguntas que se ha eliminado.
								String concept = ((Question) ((Vector) nameAndQuestion.get(1)).firstElement())
										.getConcept().trim();

								int index = conceptVectorInitial.indexOf(concept);
								if (index != -1 && jListConcept != null && conceptVectorInitial != null
										&& conceptVectorInitial.isEmpty() == false) {
									// Borrado del concepto del vector
									// conceptVectorWithRepetition.
									conceptVectorWithRepetition.removeElement(concept);

									// Comprobaci�n de si hay m�s ficheros que
									// representen este
									// concepto.
									if (conceptVectorWithRepetition.contains(concept) == false)
										conceptVectorInitial.removeElementAt(index);
								}

								questionsOfMultipleFiles.removeElementAt(i);
								delete = true;
								break;
							}
						}
					}
				} else {
					// Borrado de las filas necesarias del jTableTestQuestion.
					for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
						Vector nameAndQuestion = (Vector) questionsOfMultipleFiles.get(i);
						if (nameAndQuestion.get(0).toString().equals(questionsFileNameToDelete)) {
							for (int j = 0; j < ((Vector) nameAndQuestion.get(1)).size(); j++) {
								Question question = ((Question) ((Vector) nameAndQuestion.get(1)).get(j));
								if (question.getCodeQuestion().equals(codeOfQuestionsToDelete)) {
									if (questionsOfMultipleFiles.size() == 1
											&& ((Vector) nameAndQuestion.get(1)).size() == 1) {
										JOptionPane.showMessageDialog(this, "You cannot remove the question. " + "\n"
												+ "The test should be formed by one question as minimum.", "Warning",
												JOptionPane.WARNING_MESSAGE);
										delete = true;
										break;
									} else {
										((Vector) nameAndQuestion.get(1)).removeElementAt(j);

										// Borrado del padre en caso necesario.
										if (((Vector) nameAndQuestion.get(1)).isEmpty()) {
											// Lectura del nombre del concepto relacionado
											// con el
											// fichero de preguntas que se ha eliminado.
											String concept = ((Question) ((Vector) nameAndQuestion.get(1))
													.firstElement()).getConcept().trim();

											int index = conceptVectorInitial.indexOf(concept);
											if (index != -1 && jListConcept != null && conceptVectorInitial != null
													&& conceptVectorInitial.isEmpty() == false) {
												// Borrado del concepto del vector
												// conceptVectorWithRepetition.
												conceptVectorWithRepetition.removeElement(concept);

												// Comprobaci�n de si hay m�s ficheros que
												// representen
												// este concepto.
												if (conceptVectorWithRepetition.contains(concept) == false)
													conceptVectorInitial.removeElementAt(index);
											}
											questionsOfMultipleFiles.removeElementAt(i);
										}
										delete = true;
										break;
									}
								}
							}
							if (delete == true)
								break;
						}
					}
				}

				testModified = true;
			}
		}

		int indexTab = jTabbedPane.getSelectedIndex();
		int locationSplitTestQuestion = jSplitPaneTestQuestion.getDividerLocation();
		int locationSplitAddQuestion = jSplitPaneAddQuestionToTest.getDividerLocation();

		// Reinicializaci�n del frame.
		JSliderInit1();
		JTextFieldInit1();
		JListConceptInit();
		JTreeTestQuestionInit();
		JTableTestQuestionInit();
		JPanelInit();

		try {
			frameInit();
		} catch (java.lang.Exception e) {}

		jTabbedPane.setSelectedIndex(indexTab);
		jSplitPaneTestQuestion.setDividerLocation(locationSplitTestQuestion);
		jSplitPaneAddQuestionToTest.setDividerLocation(locationSplitAddQuestion);
		testModified = true;
	}

	/*
	 * NOMBRE: jButtonResetQuestionToTestActionPerformed. PERTENECE A: Clase
	 * FrmEditTest. LLAMADA POR: Al hacer click sobre cualquier
	 * jButtonResetQuestionToTest. LLAMA A: nada. RECIBE: nada. DEVUELVE: void.
	 * FUNCI�N: Reinicializa el jTreeTestQuestion.
	 */

	void jButtonResetQuestionToTestActionPerformed() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		questionsOfMultipleFiles = getQuestionData(test);

		// Lectura de los conceptos que evalua el test.
		conceptVectorInitial = new Vector();
		for (int i = 0; i < test.getConceptVector().size(); i++)
			conceptVectorInitial.add(test.getConceptVector().get(i).toString());

		int indexTab = jTabbedPane.getSelectedIndex();
		int locationSplitTestQuestion = jSplitPaneTestQuestion.getDividerLocation();
		int locationSplitAddQuestion = jSplitPaneAddQuestionToTest.getDividerLocation();
		JListConceptInit();
		JSliderInit1();
		JTextFieldInit1();
		JTreeTestQuestionInit();
		JTableTestQuestionInit();
		JPanelInit();

		jButtonResetQuestionToTest.setEnabled(true);
		jButtonDeleteQuestionToTest.setEnabled(false);

		try {
			frameInit();
		} catch (java.lang.Exception e) {}

		jTabbedPane.setSelectedIndex(indexTab);
		jSplitPaneTestQuestion.setDividerLocation(locationSplitTestQuestion);
		jSplitPaneAddQuestionToTest.setDividerLocation(locationSplitAddQuestion);

		testModified = false;

		// Cursor estandar.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: jButtonGetQuestionsFileListActionPerformed. PERTENECE A: Clase
	 * FrmEditTest. LLAMADA POR: Al hacer click sobre cualquier
	 * jButtonGetQuestionsFileList. LLAMA A: nada. RECIBE: nada. DEVUELVE: void.
	 * FUNCI�N: Reinicializa el jComboBoxQuestionsFile con los datos obtenidos
	 * del servidor.
	 */

	void jButtonGetQuestionsFileListActionPerformed() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		courseVector = null;
		final String courseName = test.getCourse();

		// Llamada al m�todo de la clase padre que devuelve los nombres de los
		// ficheros de preguntas para el curso.
		/*
		 * final SwingWorker worker = new SwingWorker() { public Object
		 * construct() { return new GetQuestionsFileNames(courseName); } };
		 */

		GetQuestionsFileNames worker = new GetQuestionsFileNames(courseName);
		worker.start();

		// Llamada al m�todo para repintar la barra de progreso.
		repaintJprogressBar();

		if (taskDone == -1)
			worker.stop();

		if (courseVector != null && courseVector.isEmpty() == false) {
			questionsFileNameVector = ((Course) courseVector.firstElement()).getQuestionsFileNames();
			int indexTab = jTabbedPane.getSelectedIndex();
			int locationSplitTestQuestion = jSplitPaneTestQuestion.getDividerLocation();
			int locationSplitAddQuestion = jSplitPaneAddQuestionToTest.getDividerLocation();

			JComboBoxInit();
			JPanelInit();

			jButtonExamineQuestionsFile.setEnabled(true);

			try {
				frameInit();
			} catch (java.lang.Exception e) {}

			jTabbedPane.setSelectedIndex(indexTab);
			jSplitPaneTestQuestion.setDividerLocation(locationSplitTestQuestion);
			jSplitPaneAddQuestionToTest.setDividerLocation(locationSplitAddQuestion);
		} else {
			// Mensaje para indicar al usuario que se ha producido un error al
			// intentar
			// conseguir los nombres de los ficheros de preguntas de los cursos.
			JOptionPane.showMessageDialog(this,
					"ERROR: Doesn't exist any question file to add more questions.", "Add Error",
					JOptionPane.ERROR_MESSAGE);
		}

		// Cursor estandar.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: jButtonExamineQuestionsFileActionPerformed. PERTENECE A: Clase
	 * FrmEditTest. LLAMADA POR: Al hacer click sobre cualquier
	 * jButtonExamineQuestionsFile. LLAMA A: nada. RECIBE: nada. DEVUELVE: void.
	 * FUNCI�N: Asigna los valores del jTreeQuestionsFile.
	 */

	void jButtonExamineQuestionsFileActionPerformed() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if (jComboBoxQuestionsFile.getItemCount() > 0) {
			// Llamada al m�todo de la clase padre para que conecte con el servidor
			// y
			// solicite los datos de las preguntas del fichero de preguntas
			// seleccionado
			// en el jComboBoxQuestionsFile.
			questionsFileVector = parent.getQuestions(test.getCourse(), jComboBoxQuestionsFile
					.getSelectedItem().toString());

			if (questionsFileVector == null) {
				JOptionPane.showMessageDialog(this, "ERROR: Error when reading course or question file. " + "\n"
						+ "It`s posible that the file doesn't have questions.", "Error to examine question file",
						JOptionPane.ERROR_MESSAGE);

				// Cursor estandar.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}

			int indexTab = jTabbedPane.getSelectedIndex();
			int locationSplitTestQuestion = jSplitPaneTestQuestion.getDividerLocation();
			int locationSplitAddQuestion = jSplitPaneAddQuestionToTest.getDividerLocation();
			JTreeQuestionsFileInit();
			JTableQuestionsFileInit();
			JPanelInit();

			jButtonAddQuestionToTest.setEnabled(false);

			try {
				frameInit();
			} catch (java.lang.Exception e) {}

			jTabbedPane.setSelectedIndex(indexTab);
			jSplitPaneTestQuestion.setDividerLocation(locationSplitTestQuestion);
			jSplitPaneAddQuestionToTest.setDividerLocation(locationSplitAddQuestion);

		}
		// Cursor estandar.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: jButtonAddQuestionToTestActionPerformed. PERTENECE A: Clase
	 * FrmEditTest. LLAMADA POR: Al hacer click sobre cualquier
	 * jButtonAddQuestionToTest. LLAMA A: nada. RECIBE: nada. DEVUELVE: void.
	 * FUNCI�N: A�ade una nueva pregunta al jTreeTestQuestion y al
	 * jTableTestQuestion y si es necesario, un concepto nuevo al jListConcept..
	 */

	void jButtonAddQuestionToTestActionPerformed() {
		boolean added = false;
		boolean replaced = false;
		Vector nameAndQuestion = null;
		Question question = null;

		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Adici�n de la nueva pregunta al test.
		if (jTreeQuestionsFile.getSelectionCount() > 0 && questionsOfMultipleFiles != null) {
			if (test.getTestType().trim().equals(TestEditor.ACTIVITY)) {
				nameAndQuestion = ((Vector) questionsOfMultipleFiles.firstElement());
				question = (Question) ((Vector) nameAndQuestion.get(1)).firstElement();

				if (question.getConcept().trim().equals(
						((Question) questionsFileVector.firstElement()).getConcept().trim()) == false) {
					JOptionPane.showMessageDialog(this, "ERROR: The question has NOT been ADDED. " + "\n"
							+ "The Test only evaluates a concept " + "\n"
							+ "that is different to the one that is associated to the " + "\n"
							+ "question that is attempted to add.", "Errot when adding", JOptionPane.ERROR_MESSAGE);

					// Cursor estandar.
					super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				}
			}
			if (codeOfQuestionsToAdd.equals("ALL_FILE")) {
				question = (Question) questionsFileVector.firstElement();

				nameAndQuestion = new Vector();
				nameAndQuestion.add(((Question) questionsFileVector.firstElement()).getQuestionsFileName());
				nameAndQuestion.add(questionsFileVector.clone());

				for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
					if (((Vector) questionsOfMultipleFiles.get(i)).get(0).toString().trim().equals(
							((Question) questionsFileVector.firstElement()).getQuestionsFileName())) {
						questionsOfMultipleFiles.remove(i);
						if (i == questionsOfMultipleFiles.size() - 1)
							questionsOfMultipleFiles.add(nameAndQuestion);
						else
							questionsOfMultipleFiles.add(i, nameAndQuestion);

						added = true;
						replaced = true;
						break;
					}
				}
				if (added == false) {
					questionsOfMultipleFiles.add(nameAndQuestion);
					added = true;
				}

				if (added == true && replaced == false) {
					// Adici�n del nuevo concepto a la lista.
					conceptVectorWithRepetition.add(question.getConcept());
					if (conceptVectorInitial.contains(question.getConcept()) == false)
						conceptVectorInitial.add(question.getConcept());
				}
			} else {
				// Obtenci�n de la pregunta a a�adir.
				for (int i = 0; i < questionsFileVector.size(); i++) {
					question = (Question) questionsFileVector.get(i);
					if (question.getCodeQuestion().equals(codeOfQuestionsToAdd))
						break;
				}

				// Busqueda del fichero de preguntas.
				for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
					nameAndQuestion = (Vector) questionsOfMultipleFiles.get(i);
					String questionsFileName = nameAndQuestion.get(0).toString();

					if (questionsFileName.trim().equals(question.getQuestionsFileName())) {
						Vector questionVector = (Vector) nameAndQuestion.get(1);

						for (int j = 0; j < questionVector.size(); j++) {
							Question questionAux = (Question) questionVector.get(j);
							if (questionAux.getCodeQuestion().equals(codeOfQuestionsToAdd)) {
								// Salida sin a�adir.
								added = true;
								break;
							}
						}
						if (added == false) {
							// Adici�n de la nueva pregunta.
							((Vector) ((Vector) questionsOfMultipleFiles.get(i)).get(1)).add(question);
							added = true;
							break;
						}
					}
				}

				// Adici�n de un nuevo fichero de preguntas con una sola pregunta.
				if (added == false) {
					nameAndQuestion = new Vector();
					nameAndQuestion.add(question.getQuestionsFileName());
					Vector questionsFileVectorTemp = new Vector();
					questionsFileVectorTemp.add(question);
					nameAndQuestion.add(questionsFileVectorTemp);

					questionsOfMultipleFiles.add(nameAndQuestion);

					// Adici�n del nuevo concepto a la lista.
					conceptVectorWithRepetition.add(question.getConcept());
					if (conceptVectorInitial.contains(question.getConcept()) == false)
						conceptVectorInitial.add(question.getConcept());
				}
			}

			testModified = true;
		}

		int indexTab = jTabbedPane.getSelectedIndex();
		int locationSplitTestQuestion = jSplitPaneTestQuestion.getDividerLocation();
		int locationSplitAddQuestion = jSplitPaneAddQuestionToTest.getDividerLocation();
		JTreeTestQuestionInit();
		JTableTestQuestionInit();
		JTreeQuestionsFileInit();
		JTableQuestionsFileInit();
		JSliderInit1();
		JTextFieldInit1();

		if (test.getTestType().trim().equals("exam"))
			JListConceptInit();

		JPanelInit();

		jButtonAddQuestionToTest.setEnabled(false);
		try {
			frameInit();
		} catch (java.lang.Exception e) {}

		jTabbedPane.setSelectedIndex(indexTab);
		jSplitPaneTestQuestion.setDividerLocation(locationSplitTestQuestion);
		jSplitPaneAddQuestionToTest.setDividerLocation(locationSplitAddQuestion);

		// Cursor estandar.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: jButtonModifyActionPerformed. PERTENECE A: Clase FrmEditTest.
	 * LLAMADA POR: Al hacer click sobre cualquier jButtonModify. LLAMA A: nada.
	 * RECIBE: nada. DEVUELVE: void. FUNCI�N: Invoca al m�todo de la clase padre
	 * para que cree un nuevo fichero de configuraci�n de test, con las updates
	 * realizadas, sustituyendo al antiguo.
	 */

	void jButtonModifyActionPerformed() {
		testSaved = true;
		Test testNew = new Test();

		// Lectura de los conceptos asociados al test.
		Vector conceptListFinal = new Vector(conceptVectorInitial);

		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Establecimiento del m�nimo y m�ximo para el jProgressBar.
		jProgressBar.setMinimum(0);

		if (questionsOfMultipleFiles != null && questionsOfMultipleFiles.isEmpty() == false) {
			jProgressBar.setMaximum(questionsOfMultipleFiles.size());

			jLabelStatus.setText("Extracting questions codes of file... ");
			jPanelStatus.paint(jPanelStatus.getGraphics());

			// Obtenci�n de los c�digos de las preguntas de cada fichero de
			// preguntas
			// con los que se formar� el test.

			// Reserva de memoria para el objeto testVector.
			testVector = new Vector();

			// Bucle para la obtenci�n de los c�digos de las preguntas y de los
			// conceptos asociados.
			for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
				QuestionsFileTest questionsFileTest = new QuestionsFileTest();

				Vector nameAndQuestion = (Vector) questionsOfMultipleFiles.get(i);
				String questionsFileName = nameAndQuestion.get(0).toString();
				Vector questionVector = (Vector) nameAndQuestion.get(1);

				// Redibujado de la jProgressBar y de jLabelStatus
				jLabelStatus.setText("Extracting questions codes of file...      " + questionsFileName
						+ "                       ");
				jProgressBar.setValue(i);
				jPanelStatus.paint(jPanelStatus.getGraphics());
				paint(getGraphics());
				parent.paint(parent.getGraphics());

				// Adici�n del nombre del fichero de preguntas al
				// questionsFileVector.
				questionsFileTest.setQuestionsFileName(questionsFileName);

				// Adici�n del concepto relacionado con el fichero de preguntas.
				questionsFileTest.setConcept(((Question) questionVector.firstElement()).getConcept());

				// Reserva de memoria para el vector que contendr� los codigos de
				// las
				// preguntas de un fichero.
				Vector codeQuestionVector = new Vector();

				for (int j = 0; j < questionVector.size(); j++) {
					Question question = (Question) questionVector.get(j);

					String codeQuestion = question.getCodeQuestion();

					// Adicion del c�digo de la pregunta al vector
					// codeQuestionVector.
					codeQuestionVector.add(codeQuestion);
				}
				questionsFileTest.setCodeQuestions(codeQuestionVector);

				// Adici�n del questionsFileVector al testVector.
				testVector.add(questionsFileTest);
			}

			jLabelStatus.setText("Extracting questions codes of files... Finished");
			jProgressBar.setValue(jProgressBar.getMaximum());
		} else
			testVector = test.getTestVector();

		// Lectura de los datos de configuraci�n del test.

		// Establecimiento del m�nimo y m�ximo para el jProgressBar.
		jProgressBar.setMinimum(0);
		jProgressBar.setMaximum(17);
		int progress = 0;

		jLabelStatus.setText("Extracting the concept of the questions");
		jPanelStatus.paint(jPanelStatus.getGraphics());

		// Asignacion de la �ltima fecha de uso del test.
		if (testTransformed == false)
			testNew.setLastUse(test.getLastUse());
		else
			testNew.setLastUse("");

		// Asignaci�n de los nombres de los ficheros de conceptos asociados con
		// este test.
		testNew.setConceptVector(conceptListFinal);

		// Asignaci�n del nombre del concepto abstracto que est� asociado a este
		// test.
		testNew.setAbstractConcept(test.getAbstractConcept());

		// Asignaci�n de los nombre de los ficheros de preguntas que forman el
		// test
		// y de los c�digos de las preguntas de dichos ficheros.
		testNew.setTestVector(testVector);

		// LECTURA DE LOS PAR�METROS REFERENTES A LA EJECUCI�N DEL TEST.

		jLabelStatus.setText("Extracting the running parameters...");
		jPanelStatus.paint(jPanelStatus.getGraphics());

		// Lectura de si se mostrar� la presentaci�n del test.
		jLabelStatus.setText("Extracting show test presentation...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		testNew.setShowInitialInfo(jCheckBoxShowInitialInfo.isSelected());

		// Lectura del orden en el que se mostrar�n las preguntas.
		jLabelStatus.setText("Extracting questions order...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		if (jComboBoxQuestionsOrder.getSelectedIndex() == 1)
			testNew.setQuestionsOrder(TestEditor.RANDOM_STR);
		else
			testNew.setQuestionsOrder(TestEditor.SEQUENTIAL_STR);

		// Lectura del orden en el que se mostrar�n las respuestas de las
		// preguntas.
		jLabelStatus.setText("Extracting answers order...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		if (jComboBoxAnswersOrder.getSelectedIndex() == 1)
			testNew.setAnswersOrder(TestEditor.RANDOM_STR);
		else
			testNew.setAnswersOrder(TestEditor.SEQUENTIAL_STR);

		// Lectura del tipo de evaluaci�n del test.
		jLabelStatus.setText("Extracting the show question correction");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		testNew.setShowQuestionCorrection(jCheckBoxShowQuestionCorrection.isSelected());

		// Lectura de si se mostrar� o no el verbose.
		jLabelStatus.setText("Extracting verbose...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		testNew.setVerbose(jCheckBoxVerbose.isSelected());

		// Lectura de si se mostrar� o no las respuestas correctas.
		jLabelStatus.setText("Extracting show correct answers...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		testNew.setShowCorrectAnswers(jCheckBoxShowCorrectAnswers.isSelected());

		// Lectura del tiempo m�ximo para responder.
		jLabelStatus.setText("Extracting time to answer...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		if (jCheckBoxTimeToAnswer.isSelected())
			testNew.setTimeOfAnswer(String.valueOf(jSliderTimeToAnswer.getValue()));
		else
			testNew.setTimeOfAnswer("0");

		// Lectura de si se mostrar� la presentaci�n del test.
		jLabelStatus.setText("Extracting repetition of questions without answering...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		testNew.setRepeatWithoutAnswer(jCheckBoxRepeatWithoutAnswer.isSelected());

		// Lectura de si se mostrar� la presentaci�n del test.
		jLabelStatus.setText("Extracting show final information of test...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		testNew.setShowFinalInfo(jCheckBoxShowFinalInfo.isSelected());

		// Lectura del t�tulo del test que se mostrara en la p�gina que
		// mostar� el test.
		jLabelStatus.setText("Extracting test title...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		testNew.setTestName(jTextFieldTestName.getText().trim());

		// Lectura el color del t�tulo del test que se mostrara en la p�gina que
		// mostar� el test.
		jLabelStatus.setText("Extracting title color...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		testNew.setTitleColor(jTextFieldTitleColor.getText().trim());

		// Lectura el color de fondo de la p�gina web que mostrar� el test.
		jLabelStatus.setText("Extracting bgColor...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		testNew.setBgColor(jTextFieldBgColor.getText().trim());

		// Lectura de la imagen de fondo de la p�gina web que mostrar� el test.
		jLabelStatus.setText("Extracting background...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		if (backgroundFile != null)
			testNew.setBackgroundType(backgroundFile.getName().substring(
					backgroundFile.getName().lastIndexOf(".")).toLowerCase());
		else {
			if (imageDeleted == false) {
				if (test.getBackground().trim().equals("") == false) {
					testNew.setBackground(test.getBackground().trim());
					testNew.setBackgroundType(test.getBackgroundType().trim());
				} else {
					testNew.setBackgroundType("");
					imageDeleted = false;
				}
			}
		}

		// Lectura de la penalizaci�n a respuestas incorrectas.
		jLabelStatus.setText("Extracting incorrect answers penalization...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		if (jRadioButtonIncorrectAnswersPenalizeYes.isSelected()) {
			testNew.setIncorrectAnswersPenalize(true);
			testNew.setIncorrectAnswersPenalizeNumber(Integer.valueOf(
					String.valueOf(jComboBoxIncorrectAnswers.getSelectedItem())).intValue());
		} else {
			testNew.setIncorrectAnswersPenalize(false);
			testNew.setIncorrectAnswersPenalizeNumber(0);
		}

		// Lectura de la penalizaci�n a respuestas sin contestar.
		jLabelStatus.setText("Extracting the questions without answer penalization...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		if (jRadioButtonWithoutAnswersPenalizeYes.isSelected()) {
			testNew.setWithoutAnswersPenalize(true);
			testNew.setWithoutAnswersPenalizeNumber(Integer.valueOf(
					String.valueOf(jComboBoxWithoutAnswers.getSelectedItem())).intValue());
		} else {
			testNew.setWithoutAnswersPenalize(false);
			testNew.setWithoutAnswersPenalizeNumber(0);
		}

		// Lectura del porcentaje de conocimiento asociado al test.
		jLabelStatus.setText("Extracting knowledge percentage...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		testNew.setKnowledgePercentage(String.valueOf(jSliderKnowledge.getValue()));

		// Establecimiento del nombre para el fichero que se va a crear.
		testNew.setTestFileName(testFileName);
		testNew.setCourse(test.getCourse());

		// Establecimiento del tipo de test que se est� modificando.
		if (test.getTestType().equals(TestEditor.ACTIVITY))
			testNew.setTestType(test.getTestType());
		else {
			if (test.getTestType().equals(TestEditor.EXAM)) {
				testNew.setTestType(test.getTestType());
				testNew.setAbstractConcept(test.getAbstractConcept());
			}
		}
		testNew.setExecutionType(executionTypeString);

		// Asignaci�n de los valores para la configuraci�n adaptativa.
		if (executionTypeString.equals(TestEditor.ADAPTIVE)) {
			// Lectura del criterio de comienzo del test adaptativo.
			testNew.setIrtInitialProficiency(Double.valueOf(jTextFieldInitialProficiency.getText().trim())
					.doubleValue());
			if (testNew.getIrtInitialProficiency() == 0.0)
				testNew.setIrtInitialProficiency(0.1);

			// Lectura del criterio de continuaci�n del test adaptativo.
			if (jRadioButton3P.isSelected())
				testNew.setIrtModel(3);
			else if (jRadioButton2P.isSelected())
				testNew.setIrtModel(2);
			else if (jRadioButton1P.isSelected())
				testNew.setIrtModel(1);

			// Lectura del criterio de parada del test adaptativo.
			if (jRadioButtonStandardError.isSelected()) {
				testNew.setIrtStopCriterion("standardError");
				testNew.setIrtStandardError(Double.valueOf(jTextFieldStandardError.getText().trim())
						.doubleValue());
			} else {
				if (jRadioButtonNumberItems.isSelected()) {
					testNew.setIrtStopCriterion("numberItemsAdministred");
					testNew.setIrtNumberItemsAdministred(Integer.valueOf(jTextFieldNumberItems.getText().trim())
							.intValue());
				}
			}
		}

		if (testTransformed == false) {
			// Lectura de los datos de los estudiantes que han realizado el test.
			// Lectura de si se mostrar� la presentaci�n del test.
			jLabelStatus.setText("Extracting students data...");
			progress++;
			jProgressBar.setValue(progress);
			jPanelStatus.paint(jPanelStatus.getGraphics());

			if (studentTestVector != null && studentTestVector.isEmpty() == false) {
				Vector indexDeleteVector = new Vector();
				if (testModified == true || evaluationParametersModified == true || irtParametersModified == true) {
					// Comprobaci�n de si se han finalizado todos los alumnos,
					// requisito para
					// poder modificar un test.
					for (int i = 0; i < studentTestVector.size(); i++) {
						paint(getGraphics());
						parent.paint(parent.getGraphics());

						StudentTest studentTest = (StudentTest) studentTestVector.get(i);
						if (studentTest.getFinish() == false
								&& deleteStudentVector.get(i).toString().trim().equals("false")) {
							JOptionPane.showMessageDialog(this, "ERROR: There are students without ending the test."
									+ "\n" + "You cannot modify neither the evalucaci�n parameters "
									+ "neither the test questions." + "\n" + "If you want to modify the test," + "\n"
									+ "finish the test realization for the students.", "Modify Error",
									JOptionPane.ERROR_MESSAGE);

							// Cursor estandar.
							super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

							jLabelStatus.setText("Done                                     ");
							progress++;
							jProgressBar.setValue(0);
							jPanelStatus.paint(jPanelStatus.getGraphics());
							paint(getGraphics());
							parent.paint(parent.getGraphics());

							return;
						} else if (deleteStudentVector.get(i).toString().trim().equals("true"))
							indexDeleteVector.add(String.valueOf(i));
					}
				} else {
					for (int i = 0; i < studentTestVector.size(); i++) {
						paint(getGraphics());
						parent.paint(parent.getGraphics());
						if (deleteStudentVector.get(i).toString().trim().equals("true"))
							indexDeleteVector.add(String.valueOf(i));
					}
				}

				// Bucle para el borrado de los estudiantes del vector
				// studentTestVector.
				for (int i = indexDeleteVector.size() - 1; i > -1; i--) {
					paint(getGraphics());
					parent.paint(parent.getGraphics());
					studentTestVector.removeElementAt(Integer.valueOf(indexDeleteVector.get(i).toString().trim())
							.intValue());
				}

				testNew.setStudentVector(studentTestVector);
			} else
				testNew.setStudentVector(null);
		} else
			testNew.setStudentVector(null);

		// Lectura de la habilitaci�n o deshabilitaci�n del test.
		testNew.setEnable(jCheckBoxEnableTest.isSelected());

		// Llamada al m�todo de la clase padre para la creaci�n del nuevo fichero
		// de configuraci�n del test sustituyendo el antiguo.
		jLabelStatus.setText("Creating test file...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(jPanelStatus.getGraphics());

		boolean exitWhile = false;
		String createFile = "";
		String testFileNameOld = testNew.getTestFileName();
		String testFileName = testNew.getTestFileName();
		boolean replace = true;

		if (testTransformed == true) {
			replace = false;
			testModified = true;
		} else
			replace = true;

		while (exitWhile == false) {
			createFile = parent.createTestFile(testNew, // Datos de
																							// configuraci�n
																							// del
																							// test.
					replace, // Reemplazar el fichero ya existente.
					testModified, // Comprobar alumnos sin finalizar test.
					backgroundFile, // Fichero de imagen.
					deleteStudentVector // Indica si borrar los test de los
					// log de los estudiantes.
					);

			// Si la respuestsa del servidor a la petici�n realizada es null, se
			// muestra un mensaje de error.
			if (createFile == null) {
				createFile = "ERROR: File has not been created. \n"
						+ "There was an error when connecting to the server.";
				exitWhile = true;
			}

			// Comprobaci�n de la respuesta del servidor.
			if (createFile.startsWith(TestEditor.TEXT_ANOTHER_FILE_EXISTS)) {
				// Mensaje para preguntar si el fichero de preguntas, que tiene el
				// mismo nombre que el que se va a crear, debe ser reemplazado.
				int result = JOptionPane.showConfirmDialog(this, createFile,
						"Result of the creation of the file", JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);

				if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION) {
					if (result == JOptionPane.NO_OPTION) {
						testFileName = testFileNameOld;

						// Petici�n al usuario de una palabra o palabras, que se
						// a�adir�n
						// al nombre por defecto ya creado, para distingirlo del
						// archivo
						// que ya existe.
						String wordToAdd = JOptionPane.showInputDialog(this, "Introduce a distinctive word for "
								+ "the name of test file:", "New Name", JOptionPane.WARNING_MESSAGE);
						if (wordToAdd != null && wordToAdd.equals("") == false) {
							testFileNameOld = testFileName;

							testFileName = testFileName.substring(0,
									testFileName.lastIndexOf(testNew.getCourse()) - 1)
									+ " "
									+ wordToAdd.replaceAll("_", "-")
									+ testFileName.substring(testFileName.lastIndexOf(testNew.getCourse()) - 1);
							testFileName = testFileName.replaceAll(" ", "-");

							testNew.setTestFileName(testFileName);

							JOptionPane.showMessageDialog(this, "The new name of the test file is: \n"
									+ testFileName, "New Name", JOptionPane.WARNING_MESSAGE);

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
				JOptionPane.showMessageDialog(this, createFile, "Result of the creation of the file", 1);

				if (createFile.startsWith(TestEditor.TEXT_TESTFILE_CREATED)) {
					// Obtenci�n de los datos del test modificado.)
					test = parent.getTest(courseName, testFileName);
					if (test == null) {
						// Mensaje para indicar al usuario que se ha producido un
						// error al
						// intentar obtener los datos del test.
						JOptionPane.showMessageDialog(this, "ERROR: The configuration data of the selected test "
								+ "\n" + "HAVE NOT BEEN loaded CORRECTLY.", "Load Test Error",
								JOptionPane.ERROR_MESSAGE);

						// Cursor estandar.
						super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						return;
					}

					try {
						int indexTab = jTabbedPane.getSelectedIndex();
						int locationSplitTestQuestion = jSplitPaneTestQuestion.getDividerLocation();
						int locationSplitAddQuestion = jSplitPaneAddQuestionToTest.getDividerLocation();
						JCheckBoxInit();
						JRadioButtonInit();
						JSliderInit();
						JSliderInit1();
						JTextFieldInit();
						JTextFieldInit1();
						JEditorPaneInit();
						JComboBoxInit();
						JListLoginInit();
						JListConceptInit();
						JTreeTestQuestionInit(rootNodeTestQuestion);
						JTreeQuestionsFileInit(rootNodeQuestionsFile);
						JTableTestQuestionInit();
						JTableQuestionsFileInit();
						JProgressBarInit();
						JPanelInit();

						frameInit();

						titleOrigin = titleOrigin.substring(0, titleOrigin.lastIndexOf(":") + 1);
						titleOrigin = titleOrigin.concat(" " + test.getTestFileName());
						internalFrame.setTitle(titleOrigin);

						jButtonReload.setEnabled(true);
						jButtonResetQuestionToTest.setEnabled(true);
						testSaved = true;
						testModified = false;
						imageDeleted = false;
						presentationParametersModified = false;
						htmlParametersModified = false;
						evaluationParametersModified = false;
						irtParametersModified = false;
						studentsModified = false;
						testTransformed = false;
						jTabbedPane.setSelectedIndex(indexTab);
						jSplitPaneTestQuestion.setDividerLocation(locationSplitTestQuestion);
						jSplitPaneAddQuestionToTest.setDividerLocation(locationSplitAddQuestion);
					} catch (java.lang.Exception e) {}
				}
			}
		}

		// Cursor estandar.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		jLabelStatus.setText("Done                                               ");
		progress++;
		jProgressBar.setValue(0);
		jPanelStatus.paint(jPanelStatus.getGraphics());
	}

	/*
	 * NOMBRE: jButtonReloadActionPerformed. PERTENECE A: Clase FrmEditTest.
	 * LLAMADA POR: Al hacer click sobre cualquier jButtonReload. LLAMA A: nada.
	 * RECIBE: nada. DEVUELVE: void. FUNCI�N: Invoca al m�todo de la clase padre
	 * para que devuelva de nuevo los datos de configuracion del test almacenados
	 * en el servidor y vuelve a reinicializar el frame como si se
	 * cargar� por primera vez.
	 */

	void jButtonReloadActionPerformed() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Obtenci�n de los datos del test a modificar.
		test = parent.getTest(courseName, testFileName);

		if (test == null) {
			// Mensaje para indicar al usuario que se ha producido un error al
			// obtener los datos del test.
			JOptionPane.showMessageDialog(this, "ERROR: The configuration data of the selected test " + "\n"
					+ "HAVE NOT BEEN loaded CORRECTLY.", "Load Test Error", JOptionPane.ERROR_MESSAGE);
		} else {
			// Lectura de los conceptos que evalua el test.
			conceptVectorInitial = new Vector();
			for (int i = 0; i < test.getConceptVector().size(); i++)
				conceptVectorInitial.add(test.getConceptVector().get(i).toString());

			try {
				studentTestVector = null;
				finishTestVector = null;
				canRepeatVector = null;
				deleteStudentVector = null;
				questionsVector = null;
				nameFileAndQuestions = null;
				questionsOfMultipleFiles = null;
				conceptVectorWithRepetition = null;
				testVector = null;
				codeOfQuestionsToDelete = "";
				questionsFileNameToDelete = "";
				codeOfQuestionsToAdd = "";
				questionsFileVector = null;
				questionsFileNameVector = null;

				JImageIconInit();
				JLabelInit();
				JCheckBoxInit();
				JRadioButtonInit();
				JSliderInit();
				JSliderInit1();
				JTextFieldInit();
				JTextFieldInit1();
				JEditorPaneInit();
				JComboBoxInit();
				JListLoginInit();
				JListConceptInit();
				JButtonInit();

				rootNodeTestQuestion = new DefaultMutableTreeNode("Test              ");
				JTreeTestQuestionInit(rootNodeTestQuestion);

				rootNodeQuestionsFile = new DefaultMutableTreeNode("Empty            ");
				JTreeQuestionsFileInit(rootNodeQuestionsFile);

				JTableTestQuestionInit();
				JTableQuestionsFileInit();
				JProgressBarInit();
				JPanelInit();

				frameInit();

				titleOrigin = titleOrigin.substring(0, titleOrigin.lastIndexOf(":") + 1);
				titleOrigin = titleOrigin.concat(" " + test.getTestFileName());
				internalFrame.setTitle(titleOrigin);

				jButtonExamineTestQuestion.setEnabled(true);
				jButtonDeleteQuestionToTest.setEnabled(false);
				jButtonResetQuestionToTest.setEnabled(false);
				testSaved = true;
				testModified = false;
				imageDeleted = false;
				presentationParametersModified = false;
				htmlParametersModified = false;
				evaluationParametersModified = false;
				irtParametersModified = false;
				studentsModified = false;
				testTransformed = false;

			} catch (java.lang.Exception e) {}
		}
		// Cursor estandar.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	// //////////////////////////////////////////////////////////////////////////////
	// ////////METHODS PROPIOS DEL JINTERNALFRAME
	// ///////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: getQuestionData. PERTENECE A: Clase FrmEditTest. LLAMADA POR: El
	 * constructor de la clase y por jButtonResetQuestionToTestActionPerformed.
	 * LLAMA A: getQuestionDataCode de la clase TestEditor. RECIBE: test: Objeto
	 * de tipo Test. Contine los datos para la ejecuci�n del test. DEVUELVE:
	 * questionsOfMultipleFiles: Objeto de tipo Vector. Contiene en cada posici�n
	 * un objeto de tipo vector el cual contiene a su vez en la primera posisic�n
	 * el nombre de un fichero de preguntas y en la segunda posici�n otro vector
	 * con los c�digos de las preguntas de dicho fichero que forman parte del
	 * test. FUNCI�N: Obtiene los datos de las preguntas del test.
	 */

	private Vector getQuestionData(Test test) {
		// Lectura de las preguntas que forman parte del test.
		testVector = test.getTestVector();
		questionsOfMultipleFiles = new Vector();
		conceptVectorWithRepetition = new Vector();
		jProgressBar.setMaximum(testVector.size());

		for (int i = 0; i < testVector.size(); i++) {
			nameFileAndQuestions = new Vector();
			QuestionsFileTest questionsFileTest = (QuestionsFileTest) testVector.get(i);

			String questionsFileName = questionsFileTest.getQuestionsFileName();
			jLabelStatus.setText("Loading questions of file... " + questionsFileName);
			jProgressBar.setValue(i + 1);
			jPanelStatus.paint(jPanelStatus.getGraphics());
			paint(getGraphics());
			parent.paint(parent.getGraphics());

			nameFileAndQuestions.add(questionsFileName);

			questionsVector = new Vector();
			for (int j = 0; j < questionsFileTest.getCodeQuestions().size(); j++) {
				paint(getGraphics());
				parent.paint(parent.getGraphics());
				Question question = parent.getQuestionByCode(test.getCourse(), questionsFileName,
						questionsFileTest.getCodeQuestions().get(j).toString());
				if (question != null)
					questionsVector.add(question.clone());
			}
			if (questionsVector.isEmpty() == false) {
				nameFileAndQuestions.add(questionsVector.clone());
				questionsOfMultipleFiles.add(nameFileAndQuestions.clone());

				conceptVectorWithRepetition
						.add(((Question) ((Vector) nameFileAndQuestions.get(1)).firstElement()).getConcept());
			}
		}

		jLabelStatus.setText("Done...                                      "
				+ "                                             ");
		jProgressBar.setValue(jProgressBar.getMinimum());

		return questionsOfMultipleFiles;
	}

	/*
	 * NOMBRE: deleteQuestionsFile. PERTENECE A: Clase FrmEditTest. LLAMADA POR:
	 * Nada. LLAMA A: Nada. RECIBE: questionsFileName: Objeto de tipo String.
	 * Contiene el nombre del fichero de preguntas que se va a eliminar del
	 * vector questionsOfMultipleFiles. DEVUELVE: Objeto de tipo boolean.
	 * FUNCI�N: Elimina un fichero de preguntas del vector
	 * questionsOfMultipleFiles
	 */

	public void deleteQuestionsFile(String questionsFileName) {
		if (questionsOfMultipleFiles != null && !questionsOfMultipleFiles.isEmpty()) {
			// Bucle para la b�squeda de la pregunta.
			for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
				Vector nameAndQuestions = (Vector) questionsOfMultipleFiles.get(i);
				if (nameAndQuestions.get(0).toString().trim().equals(questionsFileName)) {
					String concept = ((Question) ((Vector) nameAndQuestions.get(1)).firstElement()).getConcept();
					// Borrado del elemento.
					questionsOfMultipleFiles.removeElement(nameAndQuestions);
					// Borrado del concepto.
					conceptVectorWithRepetition.removeElement(concept);
					if (conceptVectorWithRepetition.contains(concept) == false) {
						conceptVectorInitial.removeElement(concept);
						test.getConceptVector().removeElement(concept);
					}

					// Eliminaci�n del contenido del jTreeQuestionsFile en caso de
					// que corresponda.
					if (jComboBoxQuestionsFile.getItemCount() > 0) {
						for (int j = 0; j < jComboBoxQuestionsFile.getItemCount(); j++) {
							if (jComboBoxQuestionsFile.getItemAt(j).toString().equals(questionsFileName))
								jComboBoxQuestionsFile.removeItemAt(j);
						}
					}
					rootNodeQuestionsFile = new DefaultMutableTreeNode("Empty            ");
					questionsVector = null;
					JTreeQuestionsFileInit(rootNodeQuestionsFile);
					JTableQuestionsFileInit();

					if (questionsOfMultipleFiles.isEmpty()) {
						dispose();
						return;
					}

					break;
				}
			}
		}

		Vector testVector = test.getTestVector();
		if (testVector != null && testVector.isEmpty() == false) {
			// Bucle para la busqueda de la pregunta a borrar.
			for (int i = 0; i < testVector.size(); i++) {
				QuestionsFileTest questionsFileTest = (QuestionsFileTest) testVector.get(i);
				if (questionsFileTest.getQuestionsFileName().equals(questionsFileName)) {
					// Vector codeVector = questionsFileTest.getCodeQuestions();
					String concept = questionsFileTest.getConcept();
					// Borrado del elemento.
					test.getTestVector().removeElementAt(i);
					// Borrado del concepto.
					if (conceptVectorWithRepetition != null) {
						conceptVectorWithRepetition.removeElement(concept);
						if (conceptVectorWithRepetition.contains(concept) == false) {
							conceptVectorInitial.removeElement(concept);
							test.getConceptVector().removeElement(concept);
						}
					} else {
						conceptVectorInitial.removeElement(concept);
						test.getConceptVector().removeElement(concept);
					}

					if (test.getTestVector().isEmpty()) {
						dispose();
						return;
					}

					break;
				}
			}
		}

		if (rootNodeTestQuestion != null && rootNodeTestQuestion.getChildCount() > 0) {
			int indexTab = jTabbedPane.getSelectedIndex();
			int locationSplitTestQuestion = jSplitPaneTestQuestion.getDividerLocation();
			int locationSplitAddQuestion = jSplitPaneAddQuestionToTest.getDividerLocation();

			// Reinicializaci�n del frame.
			JTextFieldInit1();
			JListConceptInit();
			JTreeTestQuestionInit();
			JTableTestQuestionInit();
			JPanelInit();

			try {
				frameInit();
			} catch (java.lang.Exception e) {}

			jTabbedPane.setSelectedIndex(indexTab);
			jSplitPaneTestQuestion.setDividerLocation(locationSplitTestQuestion);
			jSplitPaneAddQuestionToTest.setDividerLocation(locationSplitAddQuestion);
		}
	}

	/*
	 * NOMBRE: deleteQuestion. PERTENECE A: Clase FrmEditTest. LLAMADA POR: Nada.
	 * LLAMA A: Nada. RECIBE: questionsFileName: Objeto de tipo String. Contiene
	 * el nombre del fichero de preguntas que se va a eliminar del vector
	 * questionsOfMultipleFiles. codeQuestion: Objeto de tipo String. Contiene el
	 * c�digo de la pregunta a borrar. DEVUELVE: Objeto de tipo boolean. FUNCI�N:
	 * Elimina una pregunta de un fichero de preguntas del vector
	 * questionsOfMultipleFiles
	 */

	public void deleteQuestion(String questionsFileName, String codeQuestion) {
		boolean delete = false;

		if (questionsOfMultipleFiles != null && !questionsOfMultipleFiles.isEmpty()) {
			// Bucle para la b�squeda de la pregunta.
			for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
				Vector nameAndQuestion = (Vector) questionsOfMultipleFiles.get(i);
				Vector questionVector = (Vector) nameAndQuestion.get(1);
				String questionsFileNameTest = nameAndQuestion.get(0).toString();
				if (questionsFileNameTest.trim().equals(questionsFileName)) {
					for (int j = 0; j < questionVector.size(); j++) {
						Question question = (Question) questionVector.get(j);
						if (question.getCodeQuestion().equals(codeQuestion)) {
							// Borrado del elemento.
							String concept = question.getConcept();
							questionVector.removeElementAt(j);
							delete = true;
							if (questionVector.isEmpty()) {
								questionsOfMultipleFiles.removeElementAt(i);
								// Borrado del concepto.
								conceptVectorWithRepetition.removeElement(concept);
								if (conceptVectorWithRepetition.contains(concept) == false) {
									conceptVectorInitial.removeElement(concept);
									test.getConceptVector().removeElement(concept);
								}

								// Eliminaci�n del contenido del jTreeQuestionsFile en
								// caso de
								// que corresponda.
								if (jComboBoxQuestionsFile.getItemCount() > 0) {
									for (int k = 0; k < jComboBoxQuestionsFile.getItemCount(); k++) {
										if (jComboBoxQuestionsFile.getItemAt(k).toString().equals(questionsFileName))
											jComboBoxQuestionsFile.removeItemAt(j);
									}
								}
								rootNodeQuestionsFile = new DefaultMutableTreeNode("Empty            ");
								questionsVector = null;
								JTreeQuestionsFileInit(rootNodeQuestionsFile);
								JTableQuestionsFileInit();

								if (questionsOfMultipleFiles.isEmpty()) {
									dispose();
									return;
								}
							}
							break;
						}
					}
				}
				if (delete == true)
					break;
			}
		}

		delete = false;
		Vector testVector = test.getTestVector();
		if (testVector != null && testVector.isEmpty() == false) {
			// Bucle para la busqueda de la pregunta a borrar.
			for (int i = 0; i < testVector.size(); i++) {
				QuestionsFileTest questionsFileTest = (QuestionsFileTest) testVector.get(i);
				if (questionsFileTest.getQuestionsFileName().equals(questionsFileName)) {
					Vector codeVector = questionsFileTest.getCodeQuestions();
					String concept = questionsFileTest.getConcept();
					for (int j = 0; j < codeVector.size(); j++) {
						if (codeVector.contains(codeQuestion)) {
							// Borrado del elemento.
							delete = true;
							codeVector.removeElementAt(j);

							if (codeVector.isEmpty()) {
								testVector.removeElementAt(i);
								// Borrado del concepto.
								if (conceptVectorWithRepetition != null) {
									conceptVectorWithRepetition.removeElement(concept);
									if (conceptVectorWithRepetition.contains(concept) == false) {
										conceptVectorInitial.removeElement(concept);
										test.getConceptVector().removeElement(concept);
									} else {
										conceptVectorInitial.removeElement(concept);
										test.getConceptVector().removeElement(concept);
									}
								}
							}
							if (testVector.isEmpty()) {
								dispose();
								return;
							}

							break;
						}
					}
					if (delete == true)
						break;
				}
			}
		}

		if (rootNodeTestQuestion != null && rootNodeTestQuestion.getChildCount() > 0) {
			int indexTab = jTabbedPane.getSelectedIndex();
			int locationSplitTestQuestion = jSplitPaneTestQuestion.getDividerLocation();
			int locationSplitAddQuestion = jSplitPaneAddQuestionToTest.getDividerLocation();

			// Reinicializaci�n del frame.
			JTextFieldInit1();
			JListConceptInit();
			JTreeTestQuestionInit();
			JTableTestQuestionInit();
			JPanelInit();

			try {
				frameInit();
			} catch (java.lang.Exception e) {}

			jTabbedPane.setSelectedIndex(indexTab);
			jSplitPaneTestQuestion.setDividerLocation(locationSplitTestQuestion);
			jSplitPaneAddQuestionToTest.setDividerLocation(locationSplitAddQuestion);
		}
	}

	/*
	 * NOMBRE: checkQuestionInTest. PERTENECE A: Clase FrmEditTest. LLAMADA POR:
	 * Nada. LLAMA A: Nada. RECIBE: questionsFileName: Objeto de tipo String.
	 * Contiene el nombre del fichero de preguntas que se va a eliminar del
	 * vector questionsOfMultipleFiles. codeQuestion: Objeto de tipo String.
	 * Contiene el c�digo de la pregunta a borrar. DEVUELVE: Objeto de tipo
	 * boolean. FUNCI�N: Comprueba si una pregunta de un fichero de preguntas
	 * forma parte del test, aunque no se hayan salvado ninguna update a�n.
	 */

	public boolean checkQuestionInTest(String questionsFileName, String codeQuestion) {
		Vector testVector = test.getTestVector();
		if (testVector != null && testVector.isEmpty() == false) {
			for (int i = 0; i < testVector.size(); i++) {
				QuestionsFileTest questionsFileTest = (QuestionsFileTest) testVector.get(i);
				if (questionsFileTest.getQuestionsFileName().equals(questionsFileName)) {
					Vector codeVector = questionsFileTest.getCodeQuestions();
					for (int j = 0; j < codeVector.size(); j++) {
						if (codeVector.contains(codeQuestion))
							return true;
					}
				}
			}
		}

		if (questionsOfMultipleFiles != null && !questionsOfMultipleFiles.isEmpty()) {
			for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
				Vector nameAndQuestions = (Vector) questionsOfMultipleFiles.get(i);
				String questionsFileNameTest = nameAndQuestions.get(0).toString();
				if (questionsFileNameTest.equals(questionsFileName)) {
					Vector questionVector = (Vector) nameAndQuestions.get(1);
					for (int j = 0; j < questionVector.size(); j++) {
						Question question = (Question) questionVector.get(j);
						if (question.getCodeQuestion().equals(codeQuestion))
							return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * NOMBRE: checkQuestionsFileInTest. PERTENECE A: Clase FrmEditTest. LLAMADA
	 * POR: Nada. LLAMA A: Nada. RECIBE: questionsFileName: Objeto de tipo
	 * String. Contiene el nombre del fichero de preguntas que se va a eliminar
	 * del vector questionsOfMultipleFiles. DEVUELVE: Objeto de tipo boolean.
	 * FUNCI�N: Comprueba si un fichero de preguntas forma parte del test, aunque
	 * no se hayan salvado ninguna update a�n.
	 */

	public boolean checkQuestionsFileInTest(String questionsFileName) {
		Vector testVector = test.getTestVector();
		if (testVector != null && testVector.isEmpty() == false) {
			for (int i = 0; i < testVector.size(); i++) {
				QuestionsFileTest questionsFileTest = (QuestionsFileTest) testVector.get(i);
				if (questionsFileTest.getQuestionsFileName().equals(questionsFileName))
					return true;
			}
		}

		if (questionsOfMultipleFiles != null && !questionsOfMultipleFiles.isEmpty()) {
			for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
				Vector nameAndQuestions = (Vector) questionsOfMultipleFiles.get(i);
				String questionsFileNameTest = nameAndQuestions.get(0).toString();
				if (questionsFileNameTest.equals(questionsFileName))
					return true;
			}
		}
		return false;
	}

	/*
	 * NOMBRE: repaintJprogressBar. PERTENECE A: Clase FrmEditTest. LLAMADA POR:
	 * jButtonGetQuestionsFileListActionPerformed. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Este m�todo redibuja los JProgressBar para
	 * mostrar el progreso de una tarea.
	 */

	private void repaintJprogressBar() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		jPanelStatus.setVisible(true);
		jLabelStatus.setVisible(true);
		jProgressBar.setVisible(true);
		jLabelStatus.paint(jLabelStatus.getGraphics());
		jProgressBar.paint(jProgressBar.getGraphics());
		jPanelStatus.paint(jPanelStatus.getGraphics());
		jLabelStatus.setText("Loading...                                         ");
		jProgressBar.setMinimum(0);
		jProgressBar.setMaximum(15000);
		jProgressBar.setValue(0);
		getContentPane().paint(getContentPane().getGraphics());
		jPanelStatus.paint(jPanelStatus.getGraphics());

		taskDone = 0;

		timerNoTaskDone = new Timer(30000, new TimerNoTaskDone());
		timerNoTaskDone.start();

		int cont = 0;
		while (taskDone == 0) {
			if (taskDone != 0)
				break;

			cont = cont + 100;
			jLabelStatus.setText("Loading...                                      ");
			jPanelStatus.paint(jPanelStatus.getGraphics());
			jProgressBar.setValue(cont);
			jPanelStatus.paint(jPanelStatus.getGraphics());
			paint(getGraphics());
			parent.paint(parent.getGraphics());

			try {
				Thread.sleep(100);
			} catch (java.lang.InterruptedException e) {}
		}

		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		if (taskDone != -1)
			jLabelStatus.setText("Done                                         "
					+ "                                             ");
		else
			jLabelStatus.setText("Overcome time of wait. Attempt it later.");

		timerNoTaskDone.stop();

		jPanelStatus.paint(jPanelStatus.getGraphics());
		jProgressBar.setValue(jProgressBar.getMaximum());
		jPanelStatus.paint(jPanelStatus.getGraphics());
		jProgressBar.setValue(0);
		// jPanelStatus.paint(getGraphics());
		getContentPane().paint(getContentPane().getGraphics());
	}

	// //////////////////////////////////////////////////////////////////////////////
	// ////////////////CLASES CONTENIDAS EN EL
	// JINTERNALFRAME.///////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: GetQuestionsFileList. FUNCION: Clase contenida dentro de la clase
	 * FrmEditTest. Es llamada por el m�todo construct de la clase SwingWorker.
	 * Se encarga de invocar al m�todo getQuestionsFileList de la clase padre
	 * para obtener los nombres de los ficheros de preguntas pertenecientes a las
	 * versiones anteriores de WOW y que est�n contenidos en el curso sobre el
	 * que se est� actuando.
	 */

	class GetQuestionsFileNames extends Thread {
		String courseName = "";

		public GetQuestionsFileNames(String aCourseName) {
			courseName = aCourseName;
		}

		public void run() {
			taskDone = 0;

			try {
				Thread.sleep(100);
			} catch (java.lang.InterruptedException e) {}

			// Llamada al m�todo de esta misma clase que devuelve la lista de
			// nombres
			// de ficheros de todos los cursos.
			courseVector = parent.getQuestionsFileNames(courseName);

			taskDone = 1;
		}
	}

	/*
	 * NOMBRE: TimerNoTaskDone. FUNCION: Clase contenida dentro de la clase
	 * FrmEditTest. Es llamada por el m�todo repaintJProgressBar. Se encarga de
	 * controlar que la operaci�n no se alargue demasiado en el tiempo.
	 */

	class TimerNoTaskDone implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			taskDone = -1;
		}
	}
}