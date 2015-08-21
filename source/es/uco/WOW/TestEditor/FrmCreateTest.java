package es.uco.WOW.TestEditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.StringTokenizer;
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
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import es.uco.WOW.Utils.Course;
import es.uco.WOW.Utils.Question;
import es.uco.WOW.Utils.QuestionsFileTest;
import es.uco.WOW.Utils.Test;

/**
 * <p>Title: Wow! TestEditor</p> 
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: FrmCreateTest.
 * FUNCTION: This class show trhe user a screen with the necessary data
 * to create a test, of adaptive type or classic.
 * LAST MODIFICATION: 17-08-2008
 *
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class FrmCreateTest extends TestEditorInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Stores the list of courses that belongs to the user
	 */
	private Course courseConcept;
	/**
	 * The course that is being used at the moment
	 */
	private Course course = null;
	/**
	 * Stores the name of the question files that have already been
	 * added to the jListQuestionsFileAdded object 
	 */
	private Vector questionsFileNamesAdded = new Vector();
	/**
	 * Stores the names of the concept that the test will be associated to
	 */
	private Vector conceptVector = null;
	/**
	 * Stores the names of the concepts of the course
	 */
	private Vector conceptListFinal = null;
	/**
	 * Stores the name of each question file whose question
	 * will be obtained
	 */
	private String questionsFileName = ""; 
	/**
	 * Stores question objects that represent the data of
	 * a question from a file. This Vector object will contain
	 * the data of all the question of a file.
	 */
	private Vector questionsVector = null;
	/**
	 * Stores the name of the question file in the first position
	 * and a Vector that contains all the questions of that file
	 * in the second position
	 */
	private Vector nameFileAndQuestions; // Almacena el nombre del fichero de

	// preguntas en su primera posici�n y en
	// la segunda el vector que contiene todas
	// las preguntas de dicho fichero
	// (questionsList).
	private Vector questionsOfMultipleFiles; // Almacena en cada una de sus

	// posiciones un objeto Vector del
	// tipo anteriormente descrito
	// (nameFileAndQuestions).
	private Vector testVector; // Almacenar� en cada una de sus posiciones un

	// objeto de tipo QuestionsFileTest, cada uno de
	// los cuales contendr� el nombre de un fichero de
	// preguntas que se est� usando para formar la
	// bater�a de test y un Vector, el cual contendr�
	// los c�digos de las preguntas del fichero antes
	// mencionado que se incluir�n en el test.
	private String questionsFileNameToAdd = ""; // Almacena un String que
																// contiene

	// el nombre del fichero de preguntas
	// del cual se ha seleccionado una
	// pregunta para ser
	// a�adida al test.
	private String questionsFileNameToDelete = ""; // Almacena un String que
																	// contiene

	// el nombre del fichero de preguntas
	// del cual se ha seleccionado una
	// pregunta para ser
	// eliminada del test.
	private String codeOfQuestionsToAdd = ""; // Almacena un String que contiene

	// el c�digo de la pregunta que
	// se ha seleccionado para
	// a�adir al test. La estructura de
	// este String es la siguiente:
	// "Code of Question: n"
	// siendo n el c�digo de dicha
	// pregunta.
	private String codeOfQuestionsToDelete = ""; // Almacena un String que
																// contiene

	// el c�digo de una pregunta
	// ya a�adida al test, y que se
	// desea eliminar del mismo.
	private DefaultMutableTreeNode nodeQuestion; // Almacenar� el nodo donde est�

	// contenida la pregunta que se
	// desea a�adir al test. Es
	// utilizado por todos los
	// JTree de este frame.
	private DefaultMutableTreeNode nodeConcept;

	private DefaultMutableTreeNode nodeQuestionTest;// Almacenar� el nodo donde

	// est� contenida la pregunta
	// que se ha a�adido al test.
	// Es utilizado por todos los
	// JTree de este
	// frame.
	private DefaultMutableTreeNode rootNodeTestQuestion = null; // Almacena el
																					// nodo

	// ra�z del
	// jTreeTestQuestion.
	private int contQuestionsTest = 0;

	private int tabSelect = 0; // Indica que tabb fue seleccionado la ultima vez

	private URL codeBase; // Almacena la URL del servidor del que se ha descargado

	// Variables que almacenas las posibles dificultades y discriminaciones que
	// se
	// dan a elegir para una pregunta.
	private double DIFFICULTY_MAX;

	private double DIFFICULTY_MIN;

	private double DISCRIMINATION_MAX;

	private double DISCRIMINATION_MIN;

	private double GUESSING_MAX;

	private double GUESSING_MIN;

	private int NUMBER_OF_ANSWERS_MAX;

	private int NUMBER_OF_ANSWERS_MIN;

	private double EXHIBITION_RATE_MAX;

	private double EXHIBITION_RATE_MIN;

	private int ANSWER_TIME_MAX;

	private int ANSWER_TIME_MIN;

	private double SUCCESS_RATE_MAX;

	private double SUCCESS_RATE_MIN;

	private int INITIAL_PROFICIENCY_MIN = -2;

	private double INITIAL_PROFICIENCY_DEFAULT = 0.5;

	private int INITIAL_PROFICIENCY_MAX = 2;

	private int STANDARD_ERROR_MIN = 0;

	private double STANDARD_ERROR_DEFAULT = 0.33;

	private int STANDARD_ERROR_MAX = 1;

	private int configurationType = 0;
	
	private int testType = 0;

	private int executionType = 0;

	private String executionChar = "";

	private String step = "0";

	private JPanel jPanelContent;

	private JPanel jPanelInstructions;

	private JPanel jPanelOptions;

	private JPanel jPanelLabelTestType;

	private JPanel jPanelTypeTest;

	private JPanel jPanelActivity;

	private JPanel jPanelExam;

	private JPanel jPanelAbstractConcept;

	private JPanel jPanelLabelAbstractConcept;

	private JPanel jPanelLabelAssociateConcept;

	private JPanel jPanelAssociateConcept;

	private JPanel jPanelTestFileName;

	private JPanel jPanelManually;

	private JPanel jPanelLabelManually;

	private JPanel jPanelRandom;

	private JPanel jPanelLabelRandom;

	private JPanel jPanelRandomRestrictions;

	private JPanel jPanelLabelRandomRestrictions;

	private JPanel jPanelButton;

	private JPanel jPanelLabelQuestionsFile;

	private JPanel jPanelQuestionsFile;

	private JPanel jPanelSelectQuestionsFileButton;

	private JPanel jPanelListQuestionsFile;

	private JPanel jPanelSelectQuestionsFile;

	private JPanel jPanelLabelquestionsFileNamesAdded;

	private JPanel jPanelQuestionsFilesAdded;

	private JPanel jPanelSelectquestionsFileNamesAddedButton;

	private JPanel jPanelSelectquestionsFileNamesAdded;

	private JPanel jPanelNumQuestions;

	private JPanel[] jPanelTree;

	private JPanel jPanelStatus;

	private JPanel jPanelFinalTree;

	private JPanel jPanelFinalTreeButton;

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

	private JPanel jPanelIrtParameters;

	private JPanel jPanelDifficulty;

	private JPanel jPanelDifficultyMax;

	private JPanel jPanelDifficultyMin;

	private JPanel jPanelDiscrimination;

	private JPanel jPanelDiscriminationMax;

	private JPanel jPanelDiscriminationMin;

	private JPanel jPanelGuessing;

	private JPanel jPanelGuessingMax;

	private JPanel jPanelGuessingMin;

	private JPanel jPanelStatisticParameters;

	private JPanel jPanelExhibitionRate;

	private JPanel jPanelExhibitionRateMax;

	private JPanel jPanelExhibitionRateMin;

	private JPanel jPanelAnswerTime;

	private JPanel jPanelAnswerTimeMax;

	private JPanel jPanelAnswerTimeMin;

	private JPanel jPanelSuccessRate;

	private JPanel jPanelSuccessRateMax;

	private JPanel jPanelSuccessRateMin;

	private JPanel jPanelContentParameters;

	private JPanel jPanelNumberOfAnswers;

	private JPanel jPanelNumberOfAnswersMax;

	private JPanel jPanelNumberOfAnswersMin;

	private JPanel jPanelQuestionsInOtherTest;

	private JPanel jPanelCheckBoxQuestionsInOtherTest;

	private JPanel jPanelQuestionsWithImage;

	private JScrollPane jScrollPane;

	private JScrollPane jScrollPaneListAssociateConcept;

	private JScrollPane jScrollPaneListQuestionsFile;

	private JScrollPane jScrollPaneListquestionsFileNamesAdded;

	private JScrollPane[] jScrollPaneTree;

	private JScrollPane jScrollPaneFinalTree;

	private JScrollPane jScrollPanePresentationParameters;

	private JScrollPane jScrollPaneHtmlParameters;

	private JScrollPane jScrollPaneEvaluationParameters;

	private JScrollPane jScrollPaneAdaptiveParameters;

	private JLabel jLabelInstructions;

	private JLabel jLabelTestType;

	private JLabel jLabelActivity;

	private JLabel jLabelExam;

	private JLabel jLabelAbstractConcept;

	private JLabel jLabelAssociateConcept;

	private JLabel jLabelTestFileName;

	private JLabel jLabelTestName;

	private JLabel jLabelManually1;

	private JLabel jLabelManually2;

	private JLabel jLabelManually3;

	private JLabel jLabelRandom1;

	private JLabel jLabelRandom2;

	private JLabel jLabelRandom3;

	private JLabel jLabelRandomRestrictions1;

	private JLabel jLabelRandomRestrictions2;

	private JLabel jLabelRandomRestrictions3;

	private JLabel jLabelRandomRestrictions4;

	private JLabel jLabelQuestionsFile;

	private JLabel jLabelquestionsFileNamesAdded;

	private JLabel jLabelNumQuestions1;

	private JLabel jLabelNumQuestions2;

	private JLabel jLabelShowInitialInfo;

	private JLabel jLabelQuestionsOrder;

	private JLabel jLabelQuestionsOrderInfo1;

	private JLabel jLabelQuestionsOrderInfo2;

	private JLabel jLabelAnswersOrder;

	private JLabel jLabelAnswersOrderInfo1;

	private JLabel jLabelAnswersOrderInfo2;

	private JLabel jLabelShowQuestionCorrection;

	private JLabel jLabelVerbose;

	private JLabel jLabelShowCorrectAnswers;

	private JLabel jLabelTimeToAnswerInfo;

	private JLabel jLabelRepeatWithoutAnswerInfo;

	private JLabel jLabelShowFinalInfo;

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

	private JLabel jLabelDifficultyMax;

	private JLabel jLabelDifficultyMin;

	private JLabel jLabelDiscriminationMax;

	private JLabel jLabelDiscriminationMin;

	private JLabel jLabelGuessingMax;

	private JLabel jLabelGuessingMin;

	private JLabel jLabelExhibitionRateMax;

	private JLabel jLabelExhibitionRateMin;

	private JLabel jLabelAnswerTimeMax;

	private JLabel jLabelAnswerTimeMaxSeconds;

	private JLabel jLabelAnswerTimeMin;

	private JLabel jLabelAnswerTimeMinSeconds;

	private JLabel jLabelSuccessRateMax;

	private JLabel jLabelSuccessRateMin;

	private JLabel jLabelNumberOfAnswersMax;

	private JLabel jLabelNumberOfAnswersMin;

	private JLabel jLabelQuestionsWithImage;

	private JLabel jLabelStatus;

	private JButton jButtonActivity;

	private JButton jButtonExam;

	private JButton jButtonManually;

	private JButton jButtonRandom;

	private JButton jButtonRandomRestrictions;

	private JButton jButtonCancel;

	private JButton jButtonNext;

	private JButton jButtonPrevious;

	private JButton jButtonAdd;

	private JButton jButtonSelectAll;

	private JButton jButtonReload;

	private JButton jButtonDelete;

	private JButton jButtonClear;

	private JButton[] jButtonAddQuestionsToTest;

	private JButton jButtonDeleteQuestionToTest;

	private JButton jButtonSetTitle;

	private JButton jButtonSetBgColor;

	private JButton jButtonSetBackground;

	private JButton jButtonSetTitleColor;

	private JButton jButtonResetTitle;

	private JButton jButtonResetBgColor;

	private JButton jButtonResetBackground;

	private JButton jButtonResetTitleColor;

	private JButton jButtonResetAll;

	private JRadioButton jRadioButtonQuestionsOrderRandom;

	private JRadioButton jRadioButtonQuestionsOrderSequential;

	private JRadioButton jRadioButtonAnswersOrderRandom;

	private JRadioButton jRadioButtonAnswersOrderSequential;

	private JRadioButton jRadioButtonIncorrectAnswersPenalizeYes;

	private JRadioButton jRadioButtonIncorrectAnswersPenalizeNo;

	private JRadioButton jRadioButtonWithoutAnswersPenalizeYes;

	private JRadioButton jRadioButtonWithoutAnswersPenalizeNo;

	private JRadioButton jRadioButtonImageYes;

	private JRadioButton jRadioButtonImageNo;

	private JRadioButton jRadioButtonImageIndifferent;

	private JRadioButton jRadioButton1P;

	private JRadioButton jRadioButton2P;

	private JRadioButton jRadioButton3P;

	private JRadioButton jRadioButtonStandardError;

	private JRadioButton jRadioButtonNumberItems;

	private ButtonGroup jRadioButtonQuestionsOrderGroup;

	private ButtonGroup jRadioButtonAnswersOrderGroup;

	private ButtonGroup jRadioButtonIncorrectAnswersGroup;

	private ButtonGroup jRadioButtonWithoutAnswersGroup;

	private ButtonGroup jRadioButtonImageGroup;

	private ButtonGroup jRadioButtonIrtModelGroup;

	private ButtonGroup jRadioButtonStopCriterionGroup;

	private JList jListAssociateConcept;

	private JList jListQuestionsFile;

	private JList jListQuestionsFileNamesAdded;

	private JTextField jTextFieldTestFileName;

	private JTextField jTextFieldNumQuestions;

	private JTextField jTextFieldDifficultyMax;

	private JTextField jTextFieldDifficultyMin;

	private JTextField jTextFieldDiscriminationMax;

	private JTextField jTextFieldDiscriminationMin;

	private JTextField jTextFieldGuessingMax;

	private JTextField jTextFieldGuessingMin;

	private JTextField jTextFieldExhibitionRateMax;

	private JTextField jTextFieldExhibitionRateMin;

	private JTextField jTextFieldAnswerTimeMax;

	private JTextField jTextFieldAnswerTimeMin;

	private JTextField jTextFieldSuccessRateMax;

	private JTextField jTextFieldSuccessRateMin;

	private JTextField jTextFieldNumberOfAnswersMax;

	private JTextField jTextFieldNumberOfAnswersMin;

	private JTextField jTextFieldTimeToAnswer;

	private JTextField jTextFieldKnowledge;

	private JTextField jTextFieldTestName;

	private JTextField jTextFieldBgColor;

	private JTextField jTextFieldBackground;

	private JTextField jTextFieldTitleColor;

	private JTextField jTextFieldInitialProficiency;

	private JTextField jTextFieldStandardError;

	private JTextField jTextFieldNumberItems;

	private JProgressBar jProgressBar;

	private JEditorPane jEditorPaneHtmlParameters;

	private JTree[] jTreeQuestionsFile;

	private JTree jTreeTestQuestion;

	private JSplitPane jSplitPane;

	private JTabbedPane jTabbedPane;

	private JCheckBox jCheckBoxShowInitialInfo;

	private JCheckBox jCheckBoxShowQuestionCorrection;

	private JCheckBox jCheckBoxVerbose;

	private JCheckBox jCheckBoxShowCorrectAnswers;

	private JCheckBox jCheckBoxTimeToAnswer;

	private JCheckBox jCheckBoxRepeatWithoutAnswer;

	private JCheckBox jCheckBoxShowFinalInfo;

	private JCheckBox jCheckBoxQuestionsInOtherClassicTest;

	private JCheckBox jCheckBoxQuestionsInOtherAdaptiveTest;

	private JSlider jSliderDifficultyMax;

	private JSlider jSliderDifficultyMin;

	private JSlider jSliderDiscriminationMax;

	private JSlider jSliderDiscriminationMin;

	private JSlider jSliderGuessingMax;

	private JSlider jSliderGuessingMin;

	private JSlider jSliderExhibitionRateMax;

	private JSlider jSliderExhibitionRateMin;

	private JSlider jSliderAnswerTimeMax;

	private JSlider jSliderAnswerTimeMin;

	private JSlider jSliderSuccessRateMax;

	private JSlider jSliderSuccessRateMin;

	private JSlider jSliderNumberOfAnswersMax;

	private JSlider jSliderNumberOfAnswersMin;

	private JSlider jSliderTimeToAnswer;

	private JSlider jSliderKnowledge;

	private JSlider jSliderInitialProficiency;

	private JSlider jSliderStandardError;

	private JSlider jSliderNumberItems;

	private JComboBox jComboBoxAbstractConcept;

	private JComboBox jComboBoxAssociateConcept;

	private JComboBox jComboBoxIncorrectAnswers;

	private JComboBox jComboBoxWithoutAnswers;

	private BoxLayout boxLayoutPanelContent;

	private BoxLayout boxLayoutPanelOptions;

	private BoxLayout boxLayoutPanelAbstractConcept;

	private BoxLayout boxLayoutPanelAssociateConcept;

	private BoxLayout boxLayoutPanelLabelManually;

	private BoxLayout boxLayoutPanelLabelRandom;

	private BoxLayout boxLayoutPanelLabelRandomRestrictions;

	private BoxLayout boxLayoutPanelQuestionsFile;

	private BoxLayout boxLayoutPanelSelectQuestionsFile;

	private BoxLayout boxLayoutPanelquestionsFileNamesAdded;

	private BoxLayout boxLayoutPanelSelectquestionsFileNamesAdded;

	private BoxLayout boxLayoutPanelNumQuestions;

	private BoxLayout[] boxLayoutPanelTree;

	private BoxLayout boxLayoutPanelFinalTree;

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

	private BoxLayout boxLayoutPanelIrtParameters;

	private BoxLayout boxLayoutPanelDifficulty;

	private BoxLayout boxLayoutPanelDiscrimination;

	private BoxLayout boxLayoutPanelGuessing;

	private BoxLayout boxLayoutPanelStatisticParameters;

	private BoxLayout boxLayoutPanelExhibitionRate;

	private BoxLayout boxLayoutPanelAnswerTime;

	private BoxLayout boxLayoutPanelSuccessRate;

	private BoxLayout boxLayoutPanelContentParameters;

	private BoxLayout boxLayoutPanelNumberOfAnswers;

	private BoxLayout boxLayoutPanelCheckBoxQuestionsInOtherTest;

	private FlowLayout flowLayoutPanelInstructions;

	private FlowLayout flowLayoutPanelLabelTestType;

	private FlowLayout flowLayoutPanelLabelAbstractConcept;

	private FlowLayout flowLayoutPanelLabelAssociateConcept;

	private FlowLayout flowLayoutPanelActivity;

	private FlowLayout flowLayoutPanelExam;

	private FlowLayout flowLayoutPanelTypeTest;

	private FlowLayout flowLayoutPanelTestFileName;

	private FlowLayout flowLayoutPanelManually;

	private FlowLayout flowLayoutPanelRandom;

	private FlowLayout flowLayoutPanelRandomRestrictions;

	private FlowLayout flowLayoutPanelListQuestionsFile;

	private FlowLayout flowLayoutPanelSelectQuestionsFileButton;

	private FlowLayout flowLayoutPanelSelectquestionsFileNamesAddedButton;

	private FlowLayout flowLayoutPanelButton;

	private FlowLayout flowLayoutPanelOptions;

	private FlowLayout flowLayoutPanelStatus;

	private FlowLayout flowLayoutPanelFinalTreeButton;

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

	private FlowLayout flowLayoutPanelStartCriterion;

	private FlowLayout flowLayoutPanelLabelStartCriterion;

	private FlowLayout flowLayoutPanelSliderStartCriterion;

	private FlowLayout flowLayoutPanelIrtModel1P;

	private FlowLayout flowLayoutPanelIrtModel2P;

	private FlowLayout flowLayoutPanelIrtModel3P;

	private FlowLayout flowLayoutPanelStandardError;

	private FlowLayout flowLayoutPanelNumberItems;

	private FlowLayout flowLayoutPanelStopCriterion;

	private FlowLayout flowLayoutPanelDifficultyMax;

	private FlowLayout flowLayoutPanelDifficultyMin;

	private FlowLayout flowLayoutPanelDiscriminationMax;

	private FlowLayout flowLayoutPanelDiscriminationMin;

	private FlowLayout flowLayoutPanelGuessingMax;

	private FlowLayout flowLayoutPanelGuessingMin;

	private FlowLayout flowLayoutPanelExhibitionRateMax;

	private FlowLayout flowLayoutPanelExhibitionRateMin;

	private FlowLayout flowLayoutPanelAnswerTimeMax;

	private FlowLayout flowLayoutPanelAnswerTimeMin;

	private FlowLayout flowLayoutPanelSuccessRateMax;

	private FlowLayout flowLayoutPanelSuccessRateMin;

	private FlowLayout flowLayoutPanelNumberOfAnswersMax;

	private FlowLayout flowLayoutPanelNumberOfAnswersMin;

	private FlowLayout flowLayoutPanelQuestionsInOtherTest;

	private FlowLayout flowLayoutPanelQuestionsWithImage;

	private ImageIcon iconManually32;

	private ImageIcon iconRandom32;

	private ImageIcon iconRandomRestrictions32;

	private ImageIcon iconCancel32;

	private ImageIcon iconAddArrow32;

	private ImageIcon iconSelectAll32;

	private ImageIcon iconReload32;

	private ImageIcon iconDelete32;

	private ImageIcon iconClear32;

	private ImageIcon iconSetTitle32;

	private ImageIcon iconResetTitle32;

	private ImageIcon iconSetBgColor32;

	private ImageIcon iconResetBgColor32;

	private ImageIcon iconSetTitleColor32;

	private ImageIcon iconResetTitleColor32;

	private ImageIcon iconAddImage32;

	private ImageIcon iconDeleteImage32;

	private ImageIcon iconNext32;

	private ImageIcon iconPrevious32;

	private ImageIcon iconInfoGreen16;

	private ImageIcon iconInfoRed16;

	private ImageIcon iconInfoBlue16;

	private ImageIcon iconEyes16;

	private ImageIcon iconHtml16;

	private ImageIcon iconEvaluation16;

	private ImageIcon iconGreenLed32;

	private ImageIcon iconRedLed32;

	private int process;

	private boolean ImageIconLoad = false;

	private boolean questionsFileNamesSelected = false;

	private boolean introducedNumQuestions = false;

	private boolean questionsFileLoaded = false;

	private File backgroundFile = null;

	/**
	 * Creates the class and prepares it to be shown in the parent MDI form.
	 * @param aTitle Title of the window
	 * @param aWidth Width of the window
	 * @param aHeight Height of the window
	 * @param userLogin Author's login
	 * @param userPassword Author's password
	 * @param courseConcept Course that the test belongs to 
	 * @param codeBase URL of the WOW system
	 * @param anWOWPath Path of the WOW system in the server
	 * @param iconPath Relative path to the icons folder in the server
	 * @param father A reference to the parent window
	 * @param executionType Indicates if the type of test to create is classic or adaptive 
	 */
	public FrmCreateTest(final String aTitle, final int aWidth, final int aHeight, final String userLogin,
			final String userPassword, final Course courseConcept, final URL codeBase, final String anWOWPath,
			final String iconPath, final TestEditor father, final int executionType) {
	//
		// Calls the parent constructor
		super(aTitle, aWidth, aHeight, father, anWOWPath, iconPath);

		// Sets user variables
		this.courseConcept = courseConcept;
		this.course = courseConcept;
		this.codeBase = codeBase;
		this.executionType = executionType;
		
		if (executionType == TestEditor.CLASSIC_NUM) {
			executionChar = "C";
		} else if (executionType == TestEditor.ADAPTIVE_NUM) {
			executionChar = "A";
		}

		this.DIFFICULTY_MAX = father.DIFFICULTY_MAX;
		this.DIFFICULTY_MIN = father.DIFFICULTY_MIN;
		this.DISCRIMINATION_MAX = father.DISCRIMINATION_MAX;
		this.DISCRIMINATION_MIN = father.DISCRIMINATION_MIN;
		this.GUESSING_MAX = father.GUESSING_MAX;
		this.GUESSING_MIN = father.GUESSING_MIN;
		this.NUMBER_OF_ANSWERS_MAX = TestEditor.NUMBER_OF_ANSWERS_MAX;
		this.NUMBER_OF_ANSWERS_MIN = TestEditor.NUMBER_OF_ANSWERS_MIN;
		this.EXHIBITION_RATE_MAX = father.EXHIBITION_RATE_MAX;
		this.EXHIBITION_RATE_MIN = father.EXHIBITION_RATE_MIN;
		this.ANSWER_TIME_MAX = father.ANSWER_TIME_MAX;
		this.ANSWER_TIME_MIN = father.ANSWER_TIME_MIN;
		this.SUCCESS_RATE_MAX = father.SUCCESS_RATE_MAX;
		this.SUCCESS_RATE_MIN = father.SUCCESS_RATE_MIN;

		try {			
			if (course == null || course.getConceptNames() == null || course.getConceptNames().isEmpty()) {
				// There was an error obtaining the concepts of the course
				String message = "ERROR: Doesn't exist any concept in the course to create a test";
				JOptionPane.showMessageDialog(this, message, "Create Test Error", JOptionPane.ERROR_MESSAGE);
				this.dispose();
				return;
			}

			// Shows the initial screen
			JImageIconInit();
			Step0Init();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Initializes the ImageIcon objects of the frame,
	 * obtaining the icons from the WOW server
	 */
	private void JImageIconInit() {
		// Builds the string to request the icons to the server
		String theIconPath = new StringBuffer(wowPath).append(iconTestEditorPath).toString();
		String petitionManually32 = theIconPath + "manually32.gif";
		String petitionRandom32 = theIconPath + "random32.gif";
		String petitionRandomRestrictions32 = theIconPath + "randomRestrictions32.gif";
		String petitionCancel32 = theIconPath + "cancel32.gif";
		String petitionAddArrow32 = theIconPath + "addArrow32.gif";
		String petitionSelectAll32 = theIconPath + "selectAll32.gif";
		String petitionReload32 = theIconPath + "reload32.gif";
		String petitionDelete32 = theIconPath + "deleteFile32.gif";
		String petitionClear32 = theIconPath + "clear32.gif";
		String petitionSetTitle32 = theIconPath + "setTitle32.gif";
		String petitionResetTitle32 = theIconPath + "resetTitle32.gif";
		String petitionSetBgColor32 = theIconPath + "setBgColor32.gif";
		String petitionResetBgColor32 = theIconPath + "resetBgColor32.gif";
		String petitionSetTitleColor32 = theIconPath + "setTitleColor32.gif";
		String petitionResetTitleColor32 = theIconPath + "resetTitleColor32.gif";
		String petitionAddImage32 = theIconPath + "addImage32.gif";
		String petitionDeleteImage32 = theIconPath + "deleteImage32.gif";
		String petitionNext32 = theIconPath + "next32.gif";
		String petitionPrevious32 = theIconPath + "previous32.gif";
		String petitionInfoGreen16 = theIconPath + "info_green_16.gif";
		String petitionInfoRed16 = theIconPath + "info_red_16.gif";
		String petitionInfoBlue16 = theIconPath + "info_blue_16.gif";
		String petitionEyes16 = theIconPath + "eyes16.gif";
		String petitionHtml16 = theIconPath + "html16.gif";
		String petitionEvaluation16 = theIconPath + "evaluation16.gif";
		String petitionGreenLed32 = theIconPath + "greenled32.gif";
		String petitionRedLed32 = theIconPath + "redled32.gif";

		// Connect with the server
		try {
			URL urlManually32 = new URL(codeBase, petitionManually32);
			URL urlRandom32 = new URL(codeBase, petitionRandom32);
			URL urlRandomRestrictions32 = new URL(codeBase, petitionRandomRestrictions32);
			URL urlCancel32 = new URL(codeBase, petitionCancel32);
			URL urlAddArrow32 = new URL(codeBase, petitionAddArrow32);
			URL urlSelectAll32 = new URL(codeBase, petitionSelectAll32);
			URL urlReload32 = new URL(codeBase, petitionReload32);
			URL urlDelete32 = new URL(codeBase, petitionDelete32);
			URL urlClear32 = new URL(codeBase, petitionClear32);
			URL urlSetTitle32 = new URL(codeBase, petitionSetTitle32);
			URL urlResetTitle32 = new URL(codeBase, petitionResetTitle32);
			URL urlSetBgColor32 = new URL(codeBase, petitionSetBgColor32);
			URL urlResetBgColor32 = new URL(codeBase, petitionResetBgColor32);
			URL urlSetTitleColor32 = new URL(codeBase, petitionSetTitleColor32);
			URL urlResetTitleColor32 = new URL(codeBase, petitionResetTitleColor32);
			URL urlAddImage32 = new URL(codeBase, petitionAddImage32);
			URL urlDeleteImage32 = new URL(codeBase, petitionDeleteImage32);
			URL urlNext32 = new URL(codeBase, petitionNext32);
			URL urlPrevious32 = new URL(codeBase, petitionPrevious32);
			URL urlInfoGreen16 = new URL(codeBase, petitionInfoGreen16);
			URL urlInfoRed16 = new URL(codeBase, petitionInfoRed16);
			URL urlInfoBlue16 = new URL(codeBase, petitionInfoBlue16);
			URL urlEyes16 = new URL(codeBase, petitionEyes16);
			URL urlHtml16 = new URL(codeBase, petitionHtml16);
			URL urlEvaluation16 = new URL(codeBase, petitionEvaluation16);
			URL urlGreenLed32 = new URL(codeBase, petitionGreenLed32);
			URL urlRedLed32 = new URL(codeBase, petitionRedLed32);
			
			// Creates the ImageIcon objects
			iconManually32 = new ImageIcon(urlManually32);
			iconRandom32 = new ImageIcon(urlRandom32);
			iconRandomRestrictions32 = new ImageIcon(urlRandomRestrictions32);
			iconCancel32 = new ImageIcon(urlCancel32);
			iconAddArrow32 = new ImageIcon(urlAddArrow32);
			iconSelectAll32 = new ImageIcon(urlSelectAll32);
			iconReload32 = new ImageIcon(urlReload32);
			iconDelete32 = new ImageIcon(urlDelete32);
			iconClear32 = new ImageIcon(urlClear32);
			iconSetTitle32 = new ImageIcon(urlSetTitle32);
			iconResetTitle32 = new ImageIcon(urlResetTitle32);
			iconSetBgColor32 = new ImageIcon(urlSetBgColor32);
			iconResetBgColor32 = new ImageIcon(urlResetBgColor32);
			iconSetTitleColor32 = new ImageIcon(urlSetTitleColor32);
			iconResetTitleColor32 = new ImageIcon(urlResetTitleColor32);
			iconAddImage32 = new ImageIcon(urlAddImage32);
			iconDeleteImage32 = new ImageIcon(urlDeleteImage32);
			iconNext32 = new ImageIcon(urlNext32);
			iconPrevious32 = new ImageIcon(urlPrevious32);
			iconInfoGreen16 = new ImageIcon(urlInfoGreen16);
			iconInfoRed16 = new ImageIcon(urlInfoRed16);
			iconInfoBlue16 = new ImageIcon(urlInfoBlue16);
			iconEyes16 = new ImageIcon(urlEyes16);
			iconHtml16 = new ImageIcon(urlHtml16);
			iconEvaluation16 = new ImageIcon(urlEvaluation16);
			iconGreenLed32 = new ImageIcon(urlGreenLed32);
			iconRedLed32 = new ImageIcon(urlRedLed32);

			// The images are loaded
			ImageIconLoad = true;
		} catch (java.net.MalformedURLException e) {
			ImageIconLoad = false;
		}
	}

	/**
	 * Configures the screen for the step 0 of the wizard that
	 * guides the author to create a test
	 */
	private void Step0Init() {
		// Waiting cursor
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		if (step.equals("0")) {
			JComboBoxInitStep0();
			JListAssociateConceptInitStep0();
			JTextFieldInitStep0();
		}
		JLabelInitStep0();
		JButtonInitStep0();
		JProgressBarInit();
		JPanelInitStep0();

		if (!step.equals("0")) {
			jButtonNext.setEnabled(true);
			jPanelAssociateConcept.setVisible(jPanelAssociateConcept.isVisible());
		}

		try {
			frameInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		step = "0";
		
		// Default cursor
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}


	/**
	 * Initializes the labels of the frame for the step 0 of the wizard
	 */
	private void JLabelInitStep0() {
		jLabelInstructions = new JLabel("Select the test type that you want to create.");
		jLabelInstructions.setFont(new java.awt.Font("Dialog", Font.BOLD, 20));

		jLabelTestType = new JLabel("Select the test type");
		jLabelTestType.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelActivity = new JLabel("This test evaluates only one concept.");
		jLabelActivity.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelExam = new JLabel("This test can evaluate several concepts.");
		jLabelExam.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelAbstractConcept = new JLabel("Select the abstract concept");
		jLabelAbstractConcept.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelAssociateConcept = new JLabel("Select the associate concept");
		jLabelAssociateConcept.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelTestFileName = new JLabel("Test File Name: ");
		jLabelTestFileName.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelStatus = new JLabel("Done                                         "
				+ "                                             " + "                    ");
		jLabelStatus.setFont(new java.awt.Font("Dialog", 0, 12));
	}

	/**
	 * Initializes the text fields of the frame for the step 0 of the wizard
	 */
	private void JTextFieldInitStep0() {
		jTextFieldTestFileName = new JTextField(35);
		//jTextFieldTestFileName.setFont(new java.awt.Font("Dialog", 1, 16));
		jTextFieldTestFileName.setFocusable(false);
	}

	/**
	 * Initializes the button objects of the frame for the step 0 of the wizard
	 */
	private void JButtonInitStep0() {
		jButtonCancel = null;
		if (ImageIconLoad == true) {
			jButtonActivity = new JButton("Activity", iconGreenLed32);
			jButtonExam = new JButton("Exam", iconRedLed32);
			jButtonNext = new JButton("Next", iconNext32);
			jButtonCancel = new JButton("Cancel", iconCancel32);
		} else {
			jButtonActivity = new JButton("Activity");
			jButtonExam = new JButton("Exam");
			jButtonNext = new JButton("Next");
			jButtonCancel = new JButton("Cancel");
		}

		// Disables the 'next' button
		jButtonNext.setEnabled(false);

		jButtonExam.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testType = TestEditor.EXAM_NUM;
				jComboBoxAssociateConcept.setVisible(false);
				jComboBoxAbstractConcept.setVisible(true);
				jPanelAbstractConcept.setVisible(true);
				jScrollPaneListAssociateConcept.setVisible(true);
				jListAssociateConcept.setVisible(true);
				jPanelAssociateConcept.setVisible(true);
				jPanelTestFileName.setVisible(true);

				if (jComboBoxAbstractConcept.getItemCount() == 0) {
					jTextFieldTestFileName.setText("");
					jButtonNext.setEnabled(false);
				} else {
					jTextFieldTestFileName.setText(jComboBoxAbstractConcept.getSelectedItem().toString().trim()
							+ "_" + course.getName() + "_" + executionChar + "_" + "exam");
				}

				if (jListAssociateConcept.getSelectedIndices().length == 0)
					jButtonNext.setEnabled(false);
				else
					jButtonNext.setEnabled(true);
				
				// Control of size of the window
				windowSizeControl();
			}
		});

		jButtonActivity.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testType = TestEditor.ACTIVITY_NUM;
				jComboBoxAbstractConcept.setVisible(false);
				jPanelAbstractConcept.setVisible(false);
				jListAssociateConcept.setVisible(false);
				jScrollPaneListAssociateConcept.setVisible(false);
				jComboBoxAssociateConcept.setVisible(true);
				jPanelAssociateConcept.setVisible(true);
				jPanelTestFileName.setVisible(true);
				jTextFieldTestFileName.setText(jComboBoxAssociateConcept.getSelectedItem().toString().trim()
						+ "_" + course.getName() + "_" + executionChar + "_" + "activity");
				jButtonNext.setEnabled(true);
				
				// Control of size of the window
				windowSizeControl();
			}
		});

		jButtonNext.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conceptVector = new Vector();

				if (testType == TestEditor.EXAM_NUM) {
					int numSelections = jListAssociateConcept.getSelectedIndices().length;
					for (int i = 0; i < numSelections; i++)
						conceptVector.add(course.getName() + "."
								+ jListAssociateConcept.getSelectedValues()[i].toString());
				} else {
					if (testType == TestEditor.ACTIVITY_NUM)
						conceptVector.add(course.getName() + "."
								+ jComboBoxAssociateConcept.getSelectedItem().toString());
				}
				Step1Init();
			}
		});

		jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closingWindow();
			}
		});
	}

	/*
	 * NOMBRE: JComboBoxInitStep0. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JButton del frame.
	 */

	private void JComboBoxInitStep0() {
		jComboBoxAbstractConcept = new JComboBox();
		jComboBoxAssociateConcept = new JComboBox();

		jComboBoxAbstractConcept.setVisible(false);
		jComboBoxAssociateConcept.setVisible(false);

		jComboBoxAbstractConcept.removeAllItems();
		jComboBoxAssociateConcept.removeAllItems();

		// Establecimiento de las fuentes de los JList.
		//jComboBoxAbstractConcept.setFont(new java.awt.Font("Dialog", 1, 16));
		//jComboBoxAssociateConcept.setFont(new java.awt.Font("Dialog", 1, 16));

		// Adici�n a jComboAbstractConcept y jComboAssociateConcept de la lista de
		// cursos del usuario.
		if (course != null) {
			Vector abstractConceptList = course.getAbstractConceptNames();
			if (abstractConceptList != null && abstractConceptList.isEmpty() == false) {
				for (int i = 0; i < abstractConceptList.size(); i++) {
					if (abstractConceptList.get(i).toString().indexOf(course.getName()) == 0)
						jComboBoxAbstractConcept.addItem(abstractConceptList.get(i).toString().substring(
								abstractConceptList.get(i).toString().indexOf(course.getName())
										+ course.getName().length() + 1));
					else
						jComboBoxAbstractConcept.addItem(abstractConceptList.get(i).toString());
				}
			}

			Vector conceptList = course.getConceptNames();
			if (conceptList != null && conceptList.isEmpty() == false) {
				for (int i = 0; i < conceptList.size(); i++) {
					if (conceptList.get(i).toString().indexOf(course.getName()) == 0)
						jComboBoxAssociateConcept.addItem(conceptList.get(i).toString().substring(
								conceptList.get(i).toString().indexOf(course.getName()) + course.getName().length()
										+ 1));
					else
						jComboBoxAssociateConcept.addItem(conceptList.get(i).toString());
				}
			}

			// Establecimiento de la funcionalidad de jComboBoxAbstractConcept.
			jComboBoxAbstractConcept.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jTextFieldTestFileName.setText(jComboBoxAbstractConcept.getSelectedItem().toString().trim()
							+ "_" + course.getName() + "_" + executionChar + "_" + "exam");
				}
			});

			// Establecimiento de la funcionalidad de jComboBoxAssociateConcept.
			jComboBoxAssociateConcept.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jTextFieldTestFileName.setText(jComboBoxAssociateConcept.getSelectedItem().toString().trim()
							+ "_" + course.getName() + "_" + executionChar + "_" + "activity");
				}
			});
		}
	}

	/*
	 * NOMBRE: JListAssociateConceptInitStep0. PERTENECE A: Clase FrmCreateTest.
	 * LLAMADA POR: LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JList del frame.
	 */

	private void JListAssociateConceptInitStep0() {
		Vector conceptList = new Vector();
		if (course != null) {
			for (int i = 0; i < course.getConceptNames().size(); i++) {
				if (course.getConceptNames().get(i).toString().indexOf(course.getName()) == 0)
					conceptList.add(course.getConceptNames().get(i).toString().substring(
							course.getConceptNames().get(i).toString().indexOf(course.getName())
									+ course.getName().length() + 1));
				else
					conceptList.add(course.getConceptNames().get(i).toString());
			}
		}

		// Reserva de memoria para los JList.
		jListAssociateConcept = new JList(conceptList);

		// Establecimiento de visibilidad a false.
		jListAssociateConcept.setVisible(false);

		// Establecimiento del scroll de los JList.
		jListAssociateConcept.setAutoscrolls(true);

		// Establecimiento de las fuentes de los JList.
		//jListAssociateConcept.setFont(new java.awt.Font("Dialog", 1, 16));

		// Establecimiento del borde de los JList.
		jListAssociateConcept.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Establecimiento del n�mero de filas visibles de los JList.
		jListAssociateConcept.setVisibleRowCount(10);

		// Establecimiento del modo de selecci�n de los JList.
		jListAssociateConcept.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// Establecimiento de la funcionalidad de jListQuestionsFile.
		jListAssociateConcept.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if (jComboBoxAbstractConcept.getItemCount() > 0
						&& jListAssociateConcept.getSelectedIndices().length > 0)
					jButtonNext.setEnabled(true);
				else
					jButtonNext.setEnabled(false);
			}
		});
	}

	/*
	 * NOMBRE: JPanelInitStep0. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JPanel del frame, reservando memoria para el
	 * mismo, estableciendo su color, layout...
	 */

	private void JPanelInitStep0() {
		// Reserva de memoria para el panel principal del frame y
		// establecimiento de su layout.
		jPanelContent = new JPanel();
		boxLayoutPanelContent = new BoxLayout(jPanelContent, BoxLayout.Y_AXIS);
		jPanelContent.setLayout(boxLayoutPanelContent);
		jPanelContent.setAlignmentX(RIGHT_ALIGNMENT);

		// Reserva de memoria para el jScrollPane, el JScrollPane principal del
		// frame.
		jScrollPane = new JScrollPane(jPanelContent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Reserva de memoria para el JPanel que contendr� al jLabelInstructions.
		jPanelInstructions = new JPanel();
		flowLayoutPanelInstructions = new FlowLayout(FlowLayout.CENTER);
		jPanelInstructions.setLayout(flowLayoutPanelInstructions);
		jPanelInstructions.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jPanelInstructions.add(jLabelInstructions);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al jLabelTestType.
		jPanelLabelTestType = new JPanel();
		flowLayoutPanelLabelTestType = new FlowLayout(FlowLayout.LEFT);
		jPanelLabelTestType.setLayout(flowLayoutPanelLabelTestType);
		jPanelLabelTestType.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelLabelTestType.setAlignmentX(RIGHT_ALIGNMENT);

		jLabelTestType.setAlignmentX(Component.CENTER_ALIGNMENT);

		jPanelLabelTestType.add(jLabelTestType);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al jButtonActivity.
		jPanelActivity = new JPanel();
		flowLayoutPanelActivity = new FlowLayout(FlowLayout.LEFT);
		jPanelActivity.setLayout(flowLayoutPanelActivity);
		jPanelActivity.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelActivity.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelActivity.setBackground(SystemColor.WHITE);

		jButtonActivity.setAlignmentX(Component.CENTER_ALIGNMENT);

		jPanelActivity.add(jButtonActivity);
		jPanelActivity.add(jLabelActivity);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al jButtonExam.
		jPanelExam = new JPanel();
		flowLayoutPanelExam = new FlowLayout(FlowLayout.LEFT);
		jPanelExam.setLayout(flowLayoutPanelExam);
		jPanelExam.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelExam.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelExam.setBackground(SystemColor.WHITE);

		jButtonExam.setAlignmentX(Component.CENTER_ALIGNMENT);

		jPanelExam.add(jButtonExam);
		jPanelExam.add(jLabelExam);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendra a jLabelAbstractConcept.
		jPanelLabelAbstractConcept = new JPanel();
		flowLayoutPanelLabelAbstractConcept = new FlowLayout(FlowLayout.LEFT);
		jPanelLabelAbstractConcept.setLayout(flowLayoutPanelLabelAbstractConcept);
		jPanelLabelAbstractConcept.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelLabelAbstractConcept.setAlignmentX(RIGHT_ALIGNMENT);

		jLabelAbstractConcept.setAlignmentX(Component.CENTER_ALIGNMENT);

		jPanelLabelAbstractConcept.add(jLabelAbstractConcept);

		// Reserva de memoria para el panel que contendr� a jLabelAbstractConcept,
		// jComboBoxAbstractConcept.
		jPanelAbstractConcept = new JPanel();

		// Establecimiento de su visibilidad a false.
		jPanelAbstractConcept.setVisible(false);
		boxLayoutPanelAbstractConcept = new BoxLayout(jPanelAbstractConcept, BoxLayout.Y_AXIS);
		jPanelAbstractConcept.setLayout(boxLayoutPanelAbstractConcept);
		jPanelAbstractConcept.setAlignmentX(CENTER_ALIGNMENT);

		jLabelAbstractConcept.setAlignmentX(Component.RIGHT_ALIGNMENT);
		jComboBoxAbstractConcept.setAlignmentX(Component.RIGHT_ALIGNMENT);

		jPanelAbstractConcept.add(jPanelLabelAbstractConcept);
		jPanelAbstractConcept.add(jComboBoxAbstractConcept);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al jLabelAssociateConcept.
		jPanelLabelAssociateConcept = new JPanel();
		flowLayoutPanelLabelAssociateConcept = new FlowLayout(FlowLayout.LEFT);
		jPanelLabelAssociateConcept.setLayout(flowLayoutPanelLabelAssociateConcept);
		jPanelLabelAssociateConcept.setAlignmentX(RIGHT_ALIGNMENT);

		jLabelAssociateConcept.setAlignmentX(Component.CENTER_ALIGNMENT);

		jPanelLabelAssociateConcept.add(jLabelAssociateConcept);

		// Reserva de memoria para el JScrollPane que contendr� a
		// jListAssociateConcept.
		jScrollPaneListAssociateConcept = new JScrollPane(jListAssociateConcept,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Reserva de memoria para el panel que contendr� a
		// jLabelAssociateConcept,
		// jComboBoxAssociateConcept y jListAssociateConcept.
		jPanelAssociateConcept = new JPanel();

		// Establecimiento de su visibilidad a false.
		jPanelAssociateConcept.setVisible(false);
		boxLayoutPanelAssociateConcept = new BoxLayout(jPanelAssociateConcept, BoxLayout.Y_AXIS);
		jPanelAssociateConcept.setLayout(boxLayoutPanelAssociateConcept);
		jPanelAssociateConcept.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelAssociateConcept.setAlignmentX(CENTER_ALIGNMENT);

		jLabelAssociateConcept.setAlignmentX(Component.RIGHT_ALIGNMENT);
		jComboBoxAssociateConcept.setAlignmentX(Component.RIGHT_ALIGNMENT);
		jListAssociateConcept.setAlignmentX(Component.RIGHT_ALIGNMENT);

		jPanelAssociateConcept.add(jPanelAbstractConcept);
		jPanelAssociateConcept.add(jPanelLabelAssociateConcept);
		jPanelAssociateConcept.add(Box.createVerticalGlue());
		jPanelAssociateConcept.add(jComboBoxAssociateConcept);
		jPanelAssociateConcept.add(jScrollPaneListAssociateConcept);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al JPanel anteriores.
		jPanelOptions = new JPanel();
		boxLayoutPanelOptions = new BoxLayout(jPanelOptions, BoxLayout.Y_AXIS);
		jPanelOptions.setLayout(boxLayoutPanelOptions);
		jPanelOptions.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelOptions.setAlignmentX(RIGHT_ALIGNMENT);

		jPanelOptions.add(jPanelLabelTestType);
		jPanelOptions.add(jPanelActivity);
		jPanelOptions.add(Box.createVerticalStrut(50));
		jPanelOptions.add(jPanelExam);

		// Reserva de memoria para el jPanelTypeTest.
		jPanelTypeTest = new JPanel();
		flowLayoutPanelTypeTest = new FlowLayout(FlowLayout.CENTER);
		jPanelTypeTest.setLayout(flowLayoutPanelTypeTest);
		jPanelTypeTest.setBorder(BorderFactory.createTitledBorder("Select the test type."));

		jPanelOptions.setAlignmentX(CENTER_ALIGNMENT);
		jPanelAssociateConcept.setAlignmentX(CENTER_ALIGNMENT);

		jPanelTypeTest.add(jPanelOptions);
		jPanelTypeTest.add(Box.createHorizontalGlue());
		jPanelTypeTest.add(jPanelAssociateConcept);

		// Reserva de memoria y establecimiento del layout del jPanelTestFileName.
		jPanelTestFileName = new JPanel();
		flowLayoutPanelTestFileName = new FlowLayout(FlowLayout.CENTER);
		jPanelTestFileName.setLayout(flowLayoutPanelTestFileName);
		jPanelTestFileName.setVisible(false);
		jPanelTestFileName.setBackground(SystemColor.WHITE);
		jPanelTestFileName.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jLabelTestFileName.setAlignmentX(CENTER_ALIGNMENT);
		jTextFieldTestFileName.setAlignmentX(CENTER_ALIGNMENT);

		jLabelTestFileName.setAlignmentY(CENTER_ALIGNMENT);
		jTextFieldTestFileName.setAlignmentY(CENTER_ALIGNMENT);

		// Adici�n de los componentes de este JPanel.
		jPanelTestFileName.add(jLabelTestFileName);
		jPanelTestFileName.add(jTextFieldTestFileName);

		// Reserva de memoria para el JPanel que contendr� a los JButton.
		jPanelButton = new JPanel();
		flowLayoutPanelButton = new FlowLayout(FlowLayout.CENTER);
		jPanelButton.setLayout(flowLayoutPanelButton);

		jPanelButton.add(jButtonNext);
		jPanelButton.add(Box.createHorizontalGlue());
		jPanelButton.add(jButtonCancel);

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
		jPanelContent.add(jPanelInstructions);
		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jPanelTypeTest);
		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jPanelTestFileName);
		jPanelContent.add(jPanelButton);
		// jPanelContent.add(jPanelStatus);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// ////////////CONFIGURACI�N DE LOS COMPONENTES DEL
	// JINTERNAFRAME////////////////
	// ////////////PARA EL PASO
	// 1////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: Step1Init. PERTENECE A: Clase FrmCreateTest. LLAMADA POR: Al hacer
	 * click en el jButtonNext configuarado en LLAMA A: nada.
	 * RECIBE: void. DEVUELVE: void. FUNCI�N: Inicializa los componentes del
	 * frame, necesarios para el primer paso del asistente que
	 * configurar� la bateria de test, reservando memoria para los mismos y
	 * estableciendo su tama�o, su contenido, funcionalidad...
	 */

	private void Step1Init() {
		// Waiting cursor
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		initFrame = true;

		if (step.equals("0")) {
			// Llamada al m�todo de la clase padre que devuelve los nombres de los
			// ficheros de preguntas asociados a conceptos.
			Course courseAux = new Course();
			courseAux.setName(courseConcept.getName());
			courseAux.setConceptNames(conceptVector);
			courseAux.setAbstractConceptNames(courseConcept.getAbstractConceptNames());

			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			courseAux = parent.getQuestionsFileAndConceptsNames(courseAux);
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			if (courseAux == null) {
				JOptionPane.showMessageDialog(this, "There are not question files "
						+ "associated to the concepts.", "Info", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				course = courseAux;

				if (course.getConceptNames().size() < conceptVector.size()) {
					JOptionPane.showMessageDialog(this, "Some concepts don't have associated files of questions. "
							+ "\n" + "These concepts won't be evaluated.", "Info", JOptionPane.WARNING_MESSAGE);
				}
			}
		}

		// Limpiado del contenido de questionsFileNamesAdded.
		questionsFileNamesAdded.clear();

		// Establecimiento de ciertas variables a sus valores iniciales.
		introducedNumQuestions = false;
		questionsFileNamesSelected = false;
		rootNodeTestQuestion = null;

		JLabelInitStep1();
		JButtonInitStep1();
		JPanelInitStep1();

		try {
			frameInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		step = "1";
		
		//	Default cursor
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: JLabelInitStep1. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step1Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JLabel del frame, reservando memoria para los
	 * mismos y estableciendo su color, su contenido...
	 */

	private void JLabelInitStep1() {
		// Reserva de memoria y establecimiento del contenido para los JLabel.
		String testTypeTxt = TestEditor.CLASSIC; 
		if (executionType != TestEditor.CLASSIC_NUM) {
			testTypeTxt = TestEditor.ADAPTIVE;
		}
		jLabelInstructions = new JLabel("Options to create the " + testTypeTxt + " test");
		jLabelInstructions.setFont(new java.awt.Font("Dialog", 1, 20));

		jLabelManually1 = new JLabel("Select the question files");
		jLabelManually1.setFont(new java.awt.Font("Dialog", 1, 14));
		jLabelManually2 = new JLabel("and the questions of each one");
		jLabelManually2.setFont(new java.awt.Font("Dialog", 1, 14));
		jLabelManually3 = new JLabel("that you want to include in the test");
		jLabelManually3.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelRandom1 = new JLabel("Select the question files");
		jLabelRandom1.setFont(new java.awt.Font("Dialog", 1, 14));
		jLabelRandom2 = new JLabel("and how many questions of each one you want to include,");
		jLabelRandom2.setFont(new java.awt.Font("Dialog", 1, 14));
		jLabelRandom3 = new JLabel("and TestEditor will generate a test automatically");
		jLabelRandom3.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelRandomRestrictions1 = new JLabel("Select the question files");
		jLabelRandomRestrictions1.setFont(new java.awt.Font("Dialog", 1, 14));
		jLabelRandomRestrictions2 = new JLabel("and how many questions of each one want to include,");
		jLabelRandomRestrictions2.setFont(new java.awt.Font("Dialog", 1, 14));
		jLabelRandomRestrictions3 = new JLabel("and TestEditor will generate a test automatically,");
		jLabelRandomRestrictions3.setFont(new java.awt.Font("Dialog", 1, 14));
		jLabelRandomRestrictions4 = new JLabel("adjusting it to some parameters you should indicate");
		jLabelRandomRestrictions4.setFont(new java.awt.Font("Dialog", 1, 14));
	}

	/*
	 * NOMBRE: JButtonInitStep1. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step1Init(). LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JButton del frame.
	 */

	private void JButtonInitStep1() {
		jButtonCancel = null;
		// Reserva de memoria para los JButton, dependiendo de si los
		// iconos del JInteralFrame se han cargado con �xito o no.
		if (ImageIconLoad == true) {
			jButtonManually = new JButton("Manual", iconManually32);
			jButtonRandom = new JButton("Random", iconRandom32);
			jButtonRandomRestrictions = new JButton("Random with Restrictions", iconRandomRestrictions32);
			jButtonPrevious = new JButton("Previous", iconPrevious32);
			jButtonCancel = new JButton("Cancel", iconCancel32);
		} else {
			jButtonManually = new JButton("Manual");
			jButtonRandom = new JButton("Random");
			jButtonRandomRestrictions = new JButton("Random with Restrictions");
			jButtonPrevious = new JButton("Previous");
			jButtonCancel = new JButton("Cancel");
		}

		// Establecimiento de la funcionalidad de jButtonManually.
		jButtonManually.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configurationType = TestEditor.MANUALLY;
				introducedNumQuestions = true;
				Step2Init();
			}
		});

		// Establecimiento de la funcionalidad de jButtonRandom.
		jButtonRandom.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configurationType = TestEditor.RANDOM;
				introducedNumQuestions = false;
				Step2Init();
			}
		});

		// Establecimiento de la funcionalidad de jButtonRandomRestrictions.
		jButtonRandomRestrictions.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configurationType = TestEditor.RANDOM_WITH_RESTRICTIONS;
				introducedNumQuestions = false;
				Step2Init();
			}
		});

		// Establecimiento de la funcionalidad de jButtonPrevious.
		jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Step0Init();
			}
		});

		// Establecimiento de la funcionalidad de jButtonCancel.
		jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closingWindow();
			}
		});
	}

	/*
	 * NOMBRE: JPanelInitStep1. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step1Init(). LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JPanel del frame, reservando memoria para el
	 * mismo, estableciendo su color, layout...
	 */

	private void JPanelInitStep1() {
		// Reserva de memoria para el panel principal del frame y
		// establecimiento de su layout.
		jPanelContent = new JPanel();
		boxLayoutPanelContent = new BoxLayout(jPanelContent, BoxLayout.Y_AXIS);
		jPanelContent.setLayout(boxLayoutPanelContent);
		jPanelContent.setAlignmentX(RIGHT_ALIGNMENT);

		// Reserva de memoria para el jScrollPane, el JScrollPane principal del
		// frame.
		jScrollPane = new JScrollPane(jPanelContent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Reserva de memoria para el JPanel que contendr� al jLabelInstructions.
		jPanelInstructions = new JPanel();
		flowLayoutPanelInstructions = new FlowLayout(FlowLayout.CENTER);
		jPanelInstructions.setLayout(flowLayoutPanelInstructions);
		jPanelInstructions.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jPanelInstructions.add(jLabelInstructions);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� a los jLabelManually.
		jPanelLabelManually = new JPanel();
		boxLayoutPanelLabelManually = new BoxLayout(jPanelLabelManually, BoxLayout.Y_AXIS);
		jPanelLabelManually.setLayout(boxLayoutPanelLabelManually);
		jPanelLabelManually.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelLabelManually.setBackground(SystemColor.WHITE);

		jPanelLabelManually.add(jLabelManually1);
		jPanelLabelManually.add(jLabelManually2);
		jPanelLabelManually.add(jLabelManually3);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al jButtonManually y a jLabelManually.
		jPanelManually = new JPanel();
		flowLayoutPanelManually = new FlowLayout(FlowLayout.LEFT);
		jPanelManually.setLayout(flowLayoutPanelManually);
		jPanelManually.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelManually.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelManually.setBackground(SystemColor.WHITE);

		// Establecimiento de la alineaci�n para jButtonManually y
		// jPanelLabelManually.
		jButtonManually.setAlignmentX(Component.CENTER_ALIGNMENT);
		jPanelLabelManually.setAlignmentX(Component.CENTER_ALIGNMENT);

		jPanelManually.add(jButtonManually);
		jPanelManually.add(jPanelLabelManually);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� a los jLabelRandom.
		jPanelLabelRandom = new JPanel();
		boxLayoutPanelLabelRandom = new BoxLayout(jPanelLabelRandom, BoxLayout.Y_AXIS);
		jPanelLabelRandom.setLayout(boxLayoutPanelLabelRandom);
		jPanelLabelRandom.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelLabelRandom.setBackground(SystemColor.WHITE);

		jPanelLabelRandom.add(jLabelRandom1);
		jPanelLabelRandom.add(jLabelRandom2);
		jPanelLabelRandom.add(jLabelRandom3);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al jButtonRandom y a jLabelRandom.
		jPanelRandom = new JPanel();
		flowLayoutPanelRandom = new FlowLayout(FlowLayout.LEFT);
		jPanelRandom.setLayout(flowLayoutPanelRandom);
		jPanelRandom.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelRandom.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelRandom.setBackground(SystemColor.WHITE);

		// Establecimiento de la alineaci�n para jButtonRandom y
		// jPanelLabelRandom.
		jButtonRandom.setAlignmentX(Component.CENTER_ALIGNMENT);
		jPanelLabelRandom.setAlignmentX(Component.CENTER_ALIGNMENT);

		jPanelRandom.add(jButtonRandom);
		jPanelRandom.add(jPanelLabelRandom);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� a los jLabelRandomRestrictions.
		jPanelLabelRandomRestrictions = new JPanel();
		boxLayoutPanelLabelRandomRestrictions = new BoxLayout(jPanelLabelRandomRestrictions, BoxLayout.Y_AXIS);
		jPanelLabelRandomRestrictions.setLayout(boxLayoutPanelLabelRandomRestrictions);
		jPanelLabelRandomRestrictions.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelLabelRandomRestrictions.setBackground(SystemColor.WHITE);

		jPanelLabelRandomRestrictions.add(jLabelRandomRestrictions1);
		jPanelLabelRandomRestrictions.add(jLabelRandomRestrictions2);
		jPanelLabelRandomRestrictions.add(jLabelRandomRestrictions3);
		jPanelLabelRandomRestrictions.add(jLabelRandomRestrictions4);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al jButtonRandomRestrictions y a jLabelRandomRestrictions.
		jPanelRandomRestrictions = new JPanel();
		flowLayoutPanelRandomRestrictions = new FlowLayout(FlowLayout.LEFT);
		jPanelRandomRestrictions.setLayout(flowLayoutPanelRandomRestrictions);
		jPanelRandomRestrictions.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelRandomRestrictions.setAlignmentX(RIGHT_ALIGNMENT);
		jPanelRandomRestrictions.setBackground(SystemColor.WHITE);

		// Establecimiento de la alineaci�n para jButtonRandomRestrictions y
		// jPanelLabelRandomRestrictions.
		jButtonRandomRestrictions.setAlignmentX(Component.CENTER_ALIGNMENT);
		jPanelLabelRandomRestrictions.setAlignmentX(Component.CENTER_ALIGNMENT);

		jPanelRandomRestrictions.add(jButtonRandomRestrictions);
		jPanelRandomRestrictions.add(jPanelLabelRandomRestrictions);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al JPanel anteriores.
		jPanelOptions = new JPanel();
		boxLayoutPanelOptions = new BoxLayout(jPanelOptions, BoxLayout.Y_AXIS);
		jPanelOptions.setLayout(boxLayoutPanelOptions);
		String testTypeTxt = "";
		if (executionType == TestEditor.CLASSIC_NUM) {
			testTypeTxt = TestEditor.CLASSIC;
		} else {
			testTypeTxt = TestEditor.ADAPTIVE;
		}
		
		jPanelOptions.setBorder(BorderFactory.createTitledBorder("Options to create the " + testTypeTxt + " test"));
		jPanelOptions.setAlignmentX(RIGHT_ALIGNMENT);

		jPanelOptions.add(jPanelManually);
		jPanelOptions.add(Box.createVerticalGlue());
		jPanelOptions.add(jPanelRandom);
		jPanelOptions.add(Box.createVerticalGlue());
		jPanelOptions.add(jPanelRandomRestrictions);

		// Reserva de memoria para el JPanel que contendr� a los JButton.
		jPanelButton = new JPanel();
		flowLayoutPanelButton = new FlowLayout(FlowLayout.CENTER);
		jPanelButton.setLayout(flowLayoutPanelButton);

		jPanelButton.add(jButtonPrevious);
		jPanelButton.add(jButtonCancel);

		// Adici�n al jPanelContent de los paneles contenidos en el
		// frame.
		jPanelContent.add(jPanelInstructions);
		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jPanelOptions);
		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jPanelButton);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// ////////////CONFIGURACI�N DE LOS COMPONENTES DEL
	// JINTERNAFRAME////////////////
	// ////////////PARA EL PASO
	// 2////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: Step2Init. PERTENECE A: Clase FrmCreateTest. LLAMADA POR: Al hacer
	 * click sobre jButtonManually, jButtonRandom o jButtonRandomRestrictions.
	 * LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N: Inicializa los
	 * componentes del frame, necesarios para el segundo paso de la
	 * configuracion de la bateria de test, reservando memoria para los mismos y
	 * estableciendo su tama�o, su contenido, funcionalidad...
	 */

	private void Step2Init() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		initFrame = true;

		JLabelInitStep2();
		JButtonInitStep2();
		JListQuestionsFilesInitStep2();
		JListquestionsFileNamesAddedInitStep2();
		JProgressBarInit();

		if (configurationType == TestEditor.RANDOM || configurationType == TestEditor.RANDOM_WITH_RESTRICTIONS)
			JTextFieldInitStep2();

		tabSelect = 0;
		JPanelInitStep2();

		try {
			frameInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		step = "2";
		
		// Default cursor
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: JLabelInitStep2. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step2Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JLabel del frame, reservando memoria para los
	 * mismos y estableciendo su color, su contenido...
	 */

	private void JLabelInitStep2() {
		// Reserva de memoria y establecimiento del contenido para los JLabel.
		jLabelInstructions = new JLabel("Select the question files that take part on the test battery");
		jLabelInstructions.setFont(new java.awt.Font("Dialog", 1, 20));

		jLabelQuestionsFile = new JLabel("Question Files");
		jLabelQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelquestionsFileNamesAdded = new JLabel("Question Files Added");
		jLabelquestionsFileNamesAdded.setFont(new java.awt.Font("Dialog", 1, 14));

		if (configurationType == TestEditor.RANDOM || configurationType == TestEditor.RANDOM_WITH_RESTRICTIONS) {
			jLabelNumQuestions1 = new JLabel("Number of questions");
			jLabelNumQuestions1.setFont(new java.awt.Font("Dialog", 1, 12));
			jLabelNumQuestions2 = new JLabel("for the selected files:");
			jLabelNumQuestions2.setFont(new java.awt.Font("Dialog", 1, 12));
		}

		jLabelStatus = new JLabel("Done                                         "
				+ "                                             " + "                    ");
		jLabelStatus.setFont(new java.awt.Font("Dialog", 0, 12));
	}

	/*
	 * NOMBRE: JButtonInitStep2. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step2Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JButton del frame.
	 */

	private void JButtonInitStep2() {
		jButtonCancel = null;
		// Reserva de memoria para los JButton, dependiendo de si los
		// iconos del JInteralFrame se han cargado con �xito o no.
		if (ImageIconLoad == true) {
			jButtonAdd = new JButton("Add", iconAddArrow32);
			jButtonSelectAll = new JButton("Select All", iconSelectAll32);
			jButtonReload = new JButton("Reload", iconReload32);
			jButtonDelete = new JButton("Delete", iconDelete32);
			jButtonClear = new JButton("Clear", iconClear32);
			jButtonPrevious = new JButton("Previous", iconPrevious32);
			jButtonNext = new JButton("Next", iconNext32);
			jButtonCancel = new JButton("Cancel", iconCancel32);
		} else {
			jButtonAdd = new JButton("Add");
			jButtonSelectAll = new JButton("Select All");
			jButtonReload = new JButton("Reload");
			jButtonDelete = new JButton("Delete");
			jButtonClear = new JButton("Clear");
			jButtonPrevious = new JButton("Previous");
			jButtonNext = new JButton("Next");
			jButtonCancel = new JButton("Cancel");
		}

		// Deshabilitaci�n inicial de jButtonNext.
		jButtonNext.setEnabled(false);

		// Deshabilitaci�n inicial de jButtonAdd.
		jButtonAdd.setEnabled(false);

		// Deshabilitaci�n inicial de jButtonDelete.
		jButtonDelete.setEnabled(false);

		// Deshabilitaci�n inicial de jButtonClear.
		if (questionsFileNamesAdded == null || questionsFileNamesAdded.size() == 0) {
			jButtonClear.setEnabled(false);
			jButtonNext.setEnabled(false);
		} else {
			jButtonClear.setEnabled(true);
			jButtonNext.setEnabled(true);
		}

		// Establecimiento de la funcionalidad de jButtonReload.
		jButtonReload.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonReloadActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jButtonAdd.
		jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonAddActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jButtonSelectAll.
		jButtonSelectAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					jListQuestionsFile.setSelectionInterval(0, course.getQuestionsFileNames().size() - 1);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		// Establecimiento de la funcionalidad de jButtonDelete.
		jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonDeleteActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jButtonClear.
		jButtonClear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonClearActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jButtonPrevious.
		jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Step1Init();
			}
		});

		// Establecimiento de la funcionalidad de jButtonNext.
		if (configurationType == TestEditor.MANUALLY) {
			jButtonNext.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Aviso de duraci�n prolongada de la tarea.
					jLabelStatus.setText("Looking for questions of file... "
							+ "This process can take several minutes.");
					Step3AInit();
				}
			});
		} else if (configurationType == TestEditor.RANDOM) {
			jButtonNext.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Aviso de duraci�n prolongada de la tarea.
					jLabelStatus.setText("Looking for questions of file... "
							+ "This process can take several minutes.");
					Step3BInit();
				}
			});
		} else {
			if (configurationType == TestEditor.RANDOM_WITH_RESTRICTIONS) {
				jButtonNext.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Step3CInit();
					}
				});
			}
		}

		// Establecimiento de la funcionalidad de jButtonCancel.
		jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closingWindow();
			}
		});
	}

	/*
	 * NOMBRE: JListQuestionsFilesInitStep2. PERTENECE A: Clase FrmCreateTest.
	 * LLAMADA POR: Step2Init(); jButtonReloadActionPerformed(); LLAMA A: nada.
	 * RECIBE: void. DEVUELVE: void. FUNCI�N: Inicializa los JList del
	 * frame.
	 */

	private void JListQuestionsFilesInitStep2() {
		Vector questionsFileNameList = new Vector();

		for (int i = 0; i < course.getQuestionsFileNames().size(); i++) {
			questionsFileNameList.add(course.getQuestionsFileNames().get(i).toString() + " " + "("
					+ String.valueOf(course.getNumQuestionsOfFile().get(i).toString()) + ")     ");
		}

		// Reserva de memoria para los JList.
		jListQuestionsFile = new JList(questionsFileNameList);

		// Establecimiento del scroll de los JList.
		jListQuestionsFile.setAutoscrolls(true);

		// Establecimiento de las fuentes de los JList.
		//jListQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 16));

		// Establecimiento del borde de los JList.
		jListQuestionsFile.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Establecimiento del n�mero de filas visibles de los JList.
		jListQuestionsFile.setVisibleRowCount(13);

		// Establecimiento del modo de selecci�n de los JList.
		jListQuestionsFile.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// Establecimiento de la funcionalidad de jListQuestionsFile.
		jListQuestionsFile.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				jListQuestionsFileSelectionPerformed(e);
			}
		});
	}

	/*
	 * NOMBRE: JListquestionsFileNamesAddedInitStep2. PERTENECE A: Clase
	 * FrmCreateTest. LLAMADA POR: Step2Init(); LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa el jListquestionsFileNamesAdded del
	 * frame.
	 */

	private void JListquestionsFileNamesAddedInitStep2() {
		// Reserva de memoria para los JList.
		jListQuestionsFileNamesAdded = new JList(questionsFileNamesAdded);

		// Establecimiento del scroll de los JList.
		jListQuestionsFileNamesAdded.setAutoscrolls(true);

		// Establecimiento de las fuentes de los JList.
		//jListQuestionsFileNamesAdded.setFont(new java.awt.Font("Dialog", 1, 16));

		// Establecimiento del borde de los JList.
		jListQuestionsFileNamesAdded.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Establecimiento del n�mero de filas visibles de los JList.
		jListQuestionsFileNamesAdded.setVisibleRowCount(13);

		// Establecimiento del modo de selecci�n de los JList.
		jListQuestionsFileNamesAdded.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// Establecimiento de la funcionalidad de jListquestionsFileNamesAdded.
		jListQuestionsFileNamesAdded.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				jListQuestionsFileNamesAddedSelectionPerformed();
			}
		});
	}

	/*
	 * NOMBRE: JProgressBarInit. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step2Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa el JProgressBar del frame.
	 */

	private void JProgressBarInit() {
		// Reserva de memoria y establecimiento de algunas propiedades de los
		// JProgressBar.
		jProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
		jProgressBar.setBorderPainted(true);
		jProgressBar.setStringPainted(true);
	}

	/*
	 * NOMBRE: JTextFieldInitStep2. PERTENECE A: Clase FrmCreateTest. LLAMADA
	 * POR: Step2Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa el jTextFieldNumQuestions del frame.
	 */

	private void JTextFieldInitStep2() {
		// Reserva de memoria para los JTextField.
		jTextFieldNumQuestions = new JTextField();

		// Establecimiento de la fuente de los JTextField.
		//jTextFieldNumQuestions.setFont(new java.awt.Font("Dialog", 1, 16));

		// Establecimiento de la funcionalidad de jTextFieldNumQuestions.
		jTextFieldNumQuestions.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				jTextFieldNumQuestionsActionPerformed(e);
			}
		});
	}

	/*
	 * NOMBRE: JPanelInitStep2. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step2Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JPanel del frame, reservando memoria para el
	 * mismo, estableciendo su color, layout...
	 */

	private void JPanelInitStep2() {
		// Reserva de memoria para el panel principal del frame y
		// establecimiento de su layout.
		jPanelContent = new JPanel();
		boxLayoutPanelContent = new BoxLayout(jPanelContent, BoxLayout.Y_AXIS);
		jPanelContent.setLayout(boxLayoutPanelContent);
		jPanelContent.setAlignmentX(RIGHT_ALIGNMENT);

		// Reserva de memoria para el jScrollPane, el JScrollPane principal del
		// frame.
		jScrollPane = new JScrollPane(jPanelContent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Reserva de memoria para el JPanel que contendr� al jLabelInstructions.
		jPanelInstructions = new JPanel();
		flowLayoutPanelInstructions = new FlowLayout(FlowLayout.CENTER);
		jPanelInstructions.setLayout(flowLayoutPanelInstructions);
		jPanelInstructions.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		// jPanelInstructions.setAlignmentX(RIGHT_ALIGNMENT);

		jPanelInstructions.add(jLabelInstructions);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al jLabelQuestionsFile.
		jPanelLabelQuestionsFile = new JPanel();
		jPanelLabelQuestionsFile.setAlignmentX(CENTER_ALIGNMENT);

		jPanelLabelQuestionsFile.add(jLabelQuestionsFile);

		// Reserva de memoria para el JScrollPane que contendr� a
		// jListQuestionsFile.
		jScrollPaneListQuestionsFile = new JScrollPane(jListQuestionsFile,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Reserva de memoria y establecimiento del layout para el JPanel que
		// contendr� a jButtonAdd y jButtonReload.
		jPanelSelectQuestionsFileButton = new JPanel();
		flowLayoutPanelSelectQuestionsFileButton = new FlowLayout(FlowLayout.RIGHT);
		jPanelSelectQuestionsFileButton.setLayout(flowLayoutPanelSelectQuestionsFileButton);

		jPanelSelectQuestionsFileButton.add(jButtonSelectAll);
		jPanelSelectQuestionsFileButton.add(Box.createHorizontalGlue());
		jPanelSelectQuestionsFileButton.add(jButtonReload);
		jPanelSelectQuestionsFileButton.add(jButtonAdd);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� a jTextFieldNumQuestions.
		if (configurationType == TestEditor.RANDOM || configurationType == TestEditor.RANDOM_WITH_RESTRICTIONS) {
			jPanelNumQuestions = new JPanel();
			boxLayoutPanelNumQuestions = new BoxLayout(jPanelNumQuestions, BoxLayout.Y_AXIS);
			jPanelNumQuestions.setLayout(boxLayoutPanelNumQuestions);
			jPanelNumQuestions.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

			jPanelNumQuestions.add(jLabelNumQuestions1);
			jPanelNumQuestions.add(jLabelNumQuestions2);
			jPanelNumQuestions.add(jTextFieldNumQuestions);
		}

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� a jScrollPanelListQuestionsFile y a
		// jPanelTextFieldNumQuestions.
		jPanelListQuestionsFile = new JPanel();
		flowLayoutPanelListQuestionsFile = new FlowLayout(FlowLayout.CENTER);
		jPanelListQuestionsFile.setLayout(flowLayoutPanelListQuestionsFile);

		jPanelListQuestionsFile.add(jScrollPaneListQuestionsFile);
		if (configurationType == TestEditor.RANDOM || configurationType == TestEditor.RANDOM_WITH_RESTRICTIONS)
			jPanelListQuestionsFile.add(jPanelNumQuestions);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendra a jPanelListQuestionsFile y jPanelSelectQuestionsFileButton.
		jPanelQuestionsFile = new JPanel();
		boxLayoutPanelQuestionsFile = new BoxLayout(jPanelQuestionsFile, BoxLayout.Y_AXIS);
		jPanelQuestionsFile.setLayout(boxLayoutPanelQuestionsFile);
		jPanelQuestionsFile.setAlignmentX(CENTER_ALIGNMENT);

		jPanelQuestionsFile.add(jPanelListQuestionsFile);
		jPanelQuestionsFile.add(jPanelSelectQuestionsFileButton);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendra a los jPanelLabelQuestionsFile y jPanelQuestionsFile.
		jPanelSelectQuestionsFile = new JPanel();
		boxLayoutPanelSelectQuestionsFile = new BoxLayout(jPanelSelectQuestionsFile, BoxLayout.Y_AXIS);
		jPanelSelectQuestionsFile.setLayout(boxLayoutPanelSelectQuestionsFile);
		jPanelSelectQuestionsFile.setAlignmentX(LEFT_ALIGNMENT);
		jPanelSelectQuestionsFile.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jPanelSelectQuestionsFile.add(jPanelLabelQuestionsFile);
		jPanelSelectQuestionsFile.add(jPanelQuestionsFile);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al jLabelquestionsFileNamesAdded.
		jPanelLabelquestionsFileNamesAdded = new JPanel();
		jPanelLabelquestionsFileNamesAdded.setAlignmentX(CENTER_ALIGNMENT);

		jPanelLabelquestionsFileNamesAdded.add(jLabelquestionsFileNamesAdded);

		// Reserva de memoria para el JScrollPane que contendr� a
		// jListquestionsFileNamesAdded.
		jScrollPaneListquestionsFileNamesAdded = new JScrollPane(jListQuestionsFileNamesAdded,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Reserva de memoria y establecimiento del layout para el JPanel que
		// contendr� a jButtonDelete y jButtonClear.
		jPanelSelectquestionsFileNamesAddedButton = new JPanel();
		flowLayoutPanelSelectquestionsFileNamesAddedButton = new FlowLayout(FlowLayout.RIGHT);
		jPanelSelectquestionsFileNamesAddedButton.setLayout(flowLayoutPanelSelectquestionsFileNamesAddedButton);

		jPanelSelectquestionsFileNamesAddedButton.add(jButtonDelete);
		jPanelSelectquestionsFileNamesAddedButton.add(jButtonClear);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendra a jListquestionsFileNamesAdded y jButtonDelete.
		jPanelQuestionsFilesAdded = new JPanel();
		boxLayoutPanelquestionsFileNamesAdded = new BoxLayout(jPanelQuestionsFilesAdded, BoxLayout.Y_AXIS);
		jPanelQuestionsFilesAdded.setLayout(boxLayoutPanelquestionsFileNamesAdded);
		jPanelQuestionsFilesAdded.setAlignmentX(CENTER_ALIGNMENT);

		// Establecimiento de la horientacion de jButtonDelete.
		jButtonDelete.setAlignmentX(RIGHT_ALIGNMENT);

		jPanelQuestionsFilesAdded.add(jScrollPaneListquestionsFileNamesAdded);
		jPanelQuestionsFilesAdded.add(jPanelSelectquestionsFileNamesAddedButton);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendra a los jPanelLabelquestionsFileNamesAdded y
		// jPanelQuestionsFilesAdded.
		jPanelSelectquestionsFileNamesAdded = new JPanel();
		boxLayoutPanelSelectquestionsFileNamesAdded = new BoxLayout(jPanelSelectquestionsFileNamesAdded,
				BoxLayout.Y_AXIS);
		jPanelSelectquestionsFileNamesAdded.setLayout(boxLayoutPanelSelectquestionsFileNamesAdded);
		jPanelSelectquestionsFileNamesAdded.setAlignmentX(LEFT_ALIGNMENT);
		jPanelSelectquestionsFileNamesAdded.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelSelectquestionsFileNamesAdded.setSize(jPanelSelectQuestionsFile.getSize());

		jPanelSelectquestionsFileNamesAdded.add(jPanelLabelquestionsFileNamesAdded);
		jPanelSelectquestionsFileNamesAdded.add(jPanelQuestionsFilesAdded);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendra a los jPanelSelectQuestionsFile y
		// jPanelSelectquestionsFileNamesAdded.
		jPanelOptions = new JPanel();
		flowLayoutPanelOptions = new FlowLayout(FlowLayout.CENTER);
		jPanelOptions.setLayout(flowLayoutPanelOptions);
		jPanelOptions.setBorder(BorderFactory.createTitledBorder("Select files and press add button"));

		jPanelOptions.add(jPanelSelectQuestionsFile);
		jPanelOptions.add(Box.createHorizontalStrut(50));
		jPanelOptions.add(jPanelSelectquestionsFileNamesAdded);

		// Reserva de memoria y establecimento del layout para el JPanel que
		// contendr� a los JButton.
		jPanelButton = new JPanel();
		flowLayoutPanelButton = new FlowLayout(FlowLayout.CENTER);
		jPanelButton.setLayout(flowLayoutPanelButton);

		jPanelButton.add(jButtonPrevious);
		jPanelButton.add(jButtonNext);
		jPanelButton.add(Box.createHorizontalGlue());
		jPanelButton.add(jButtonCancel);

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
		jPanelContent.add(jPanelInstructions);
		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jPanelOptions);
		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jPanelButton);
		jPanelContent.add(jPanelStatus);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// ////////////CONFIGURACI�N DE LOS COMPONENTES DEL
	// JINTERNAFRAME////////////////
	// ////////////PARA EL PASO
	// 3A///////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: Step3AInit. PERTENECE A: Clase FrmCreateTest. LLAMADA POR: Al
	 * hacer click sobre jButtonNext cuando el frame est� configurado
	 * seg�n Step2Init(). LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los componentes del frame, necesarios para el tercer
	 * paso de la configuracion de la bateria de test, reservando memoria para
	 * los mismos y estableciendo su tama�o, su contenido, funcionalidad...
	 */

	private void Step3AInit() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Establecimiento del m�nimo y m�ximo para el jProgressBar.
		jProgressBar.setMinimum(0);
		jProgressBar.setMaximum(questionsFileNamesAdded.size());

		// boolean workerInit = false;
		// SwingWorker worker = null;

		// Llamada al m�todo de la clase padre que devuelve las preguntas para
		// un fichero de preguntas.
		if (!step.equals("4A") && !step.equals("4B")) {
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			jLabelStatus.setText("Loading questions of file... ");
			jPanelStatus.paint(this.jPanelStatus.getGraphics());

			/*
			 * worker = new SwingWorker() { public Object construct() { return new
			 * GetQuestions(); } };
			 */
			GetQuestions worker = new GetQuestions();
			// workerInit = true;
			worker.start();

			// Redibujo de la barra de progreso.
			while (questionsFileLoaded == false) {
				jLabelStatus.setText("Loading questions of file... " + questionsFileName);
				jProgressBar.setValue(process + 1);
				jPanelStatus.paint(this.jPanelStatus.getGraphics());
				this.paint(this.getGraphics());
				parent.paint(parent.getGraphics());
			}
			worker = null;
			questionsFileLoaded = false;

			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			jLabelStatus.setText("Loading questions of file... Finished");
			jProgressBar.setValue(jProgressBar.getMaximum());

			// Mensaje para informar al usuario del n�mero de ficheros de preguntas
			// con los que se formar� la bater�a de test.
			JOptionPane.showMessageDialog(parent, "The test will be created from "
					+ String.valueOf(questionsOfMultipleFiles.size()) + " question files.", "Info", 1);
		}

		JLabelInitStep3A();
		JButtonInitStep3A();

		if (rootNodeTestQuestion == null)
			rootNodeTestQuestion = new DefaultMutableTreeNode("Test");
		else {
			// Comprobaci�n de si se ha eliminado ficheros de la lista que se usan
			// para crear el test. Si es as� y existen preguntas a�adidas de dichos
			// ficheros, estas ser�n eliminadas.
			for (int i = 0; i < rootNodeTestQuestion.getChildCount(); i++) {
				if (!questionsFileNamesAdded.contains(rootNodeTestQuestion.getChildAt(i).toString())) {
					rootNodeTestQuestion.remove(i);
					i--;
				}
			}
		}
		JTreeQuestionsFilesInit();
		JTreeTestQuestionInit(rootNodeTestQuestion);
		JProgressBarInit();
		JPanelInitStep3A();

		try {
			frameInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		step = "3A";
		
		//	Default cursor
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: JLabelInitStep3A. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step3Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JLabel del frame, reservando memoria para los
	 * mismos y estableciendo su color, su contenido...
	 */

	private void JLabelInitStep3A() {
		// Reserva de memoria y establecimiento del contenido para los JLabel.
		jLabelInstructions = new JLabel(
				"Select the questions that take part on the test.");
		jLabelInstructions.setFont(new java.awt.Font("Dialog", 1, 20));

		jLabelStatus = new JLabel("Done                                         "
				+ "                                             " + "                    ");
		jLabelStatus.setFont(new java.awt.Font("Dialog", 0, 12));
	}

	/*
	 * NOMBRE: JButtonInitStep3A. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step3Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JButton del frame.
	 */

	private void JButtonInitStep3A() {
		jButtonCancel = null;
		// Reserva de memoria para los JButton, dependiendo de si los
		// iconos del JInteralFrame se han cargado con �xito o no.
		jButtonAddQuestionsToTest = new JButton[questionsOfMultipleFiles.size()];
		if (ImageIconLoad == true) {
			// Bucle para la reserva de memoria de los JButton del array
			// de JButton jButtonAddQuestionsToFile.
			for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
				jButtonAddQuestionsToTest[i] = new JButton("Add", iconAddArrow32);

				// Deshabilitaci�n inicial.
				jButtonAddQuestionsToTest[i].setEnabled(false);
			}

			jButtonDeleteQuestionToTest = new JButton("Delete", iconDelete32);
			jButtonClear = new JButton("Clear", iconClear32);
			jButtonPrevious = new JButton("Previous", iconPrevious32);
			jButtonNext = new JButton("Next", iconNext32);
			jButtonCancel = new JButton("Cancel", iconCancel32);
		} else {
			// Bucle para la reserva de memoria de los JButton del array
			// de JButton jButtonAddQuestionsToFile.
			for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
				jButtonAddQuestionsToTest[i] = new JButton("Add");

				// Deshabilitaci�n inicial.
				jButtonAddQuestionsToTest[i].setEnabled(false);
			}

			jButtonDeleteQuestionToTest = new JButton("Delete");
			jButtonClear = new JButton("Clear");
			jButtonPrevious = new JButton("Previous");
			jButtonNext = new JButton("Next");
			jButtonCancel = new JButton("Cancel");
		}

		// Deshabilitaci�n inicial de jButtonNext.
		jButtonNext.setEnabled(false);

		// Deshabilitaci�n inicial de jButtonDeleteQuestionsToTest.
		jButtonDeleteQuestionToTest.setEnabled(false);

		// Deshabilitaci�n inicial de jButtonClear.
		jButtonClear.setEnabled(false);

		// Establecimiento de la funcionalidad de los jButtonAddQuestionsToTest.
		for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
			jButtonAddQuestionsToTest[i].addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonAddQuestionToTestActionPerformed();
				}
			});
		}

		// Establecimiento de la funcionalidad de jButtonDeleteQuestionsToTest.
		jButtonDeleteQuestionToTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonDeleteQuestionToTestActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jButtonClear.
		jButtonClear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTreeTestQuestion = null;
				rootNodeTestQuestion = null;
				rootNodeTestQuestion = new DefaultMutableTreeNode("Test");
				JTreeQuestionsFilesInit();
				JTreeTestQuestionInit(rootNodeTestQuestion);
				JButtonInitStep3A();
				JPanelInitStep3A();
				try {
					frameInit();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				// Deshabilitaci�n del jButtonNext.
				jButtonNext.setEnabled(false);
			}
		});

		// Establecimiento de la funcionalidad de jButtonPrevious.
		jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTreeQuestionsFile = null;
				questionsOfMultipleFiles = null;
				Step2Init();
			}
		});

		// Establecimiento de la funcionalidad de jButtonNext.
		jButtonNext.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Aviso de duraci�n prolongada de la tarea.
				jLabelStatus.setText("Extracting data for the creation of the test battery...");
				Step4AInit();
			}
		});

		// Establecimiento de la funcionalidad de jButtonCancel.
		jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closingWindow();
			}
		});
	}

	/*
	 * NOMBRE: JTreeQuestionsFilesInit(). PERTENECE A: Clase FrmCreateTest.
	 * LLAMADA POR: Step3Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JTree y establece su contenido y funcionalidad.
	 */

	private void JTreeQuestionsFilesInit() {
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

		jTreeQuestionsFile = new JTree[questionsOfMultipleFiles.size()];

		// Bucle para la lectura de los datos de todas las preguntas, devueltos
		// por
		// el servidor, e inicializaci�n de los nodos del JTree con dichos datos.
		for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
			// Reserva de memoria para el nodo ra�z del JTree.
			DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(((Vector) questionsOfMultipleFiles
					.get(i)).get(0).toString());

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
					rootNode.add(conceptNode);

				rootNode.add(questionNode);
			}

			// Reserva de memoria para el JTree y establecimiento de su nodo ra�z.
			jTreeQuestionsFile[i] = new JTree(rootNode);

			String questionsFileName = ((Vector) questionsOfMultipleFiles.get(i)).get(0).toString();
			jTreeQuestionsFile[i].setCellRenderer(new CustomCellRenderer(questionsFileName, wowPath,
					iconTestEditorPath, codeBase));

			// Establecimiento del tama�o, tipo de letra y borde del JTree.
			jTreeQuestionsFile[i].setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			jTreeQuestionsFile[i].setFont(new java.awt.Font("Dialog", 1, 14));
			jTreeQuestionsFile[i].setToolTipText("Press Alt + click with the left button of the mouse,"
					+ "\n on the data of a question, to modify it.");
			jTreeQuestionsFile[i].setScrollsOnExpand(true);
			jTreeQuestionsFile[i].setShowsRootHandles(true);

			final int j = i;

			final DefaultMutableTreeNode rootNodeQuestionsFile = rootNode;

			// Establecimiento de la funcionalidad del JTree.
			jTreeQuestionsFile[i].addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					questionsFileNameToAdd = "";
					TreePath treePath = jTreeQuestionsFile[j].getSelectionPath();

					if (treePath != null && treePath.getPathCount() >= 2) {
						// Preparaci�n para la adici�n de 1 pregunta del fichero.
						nodeQuestion = (DefaultMutableTreeNode) treePath.getPathComponent(1);

						if (nodeQuestion.getChildCount() > 0) {
							codeOfQuestionsToAdd = nodeQuestion.getFirstChild().toString();
							questionsFileNameToAdd = treePath.getPathComponent(0).toString();

							nodeConcept = (DefaultMutableTreeNode) rootNodeQuestionsFile.getChildAt(0);
						} else {
							codeOfQuestionsToAdd = "ALL_FILE";
							nodeQuestion = (DefaultMutableTreeNode) treePath.getPathComponent(0);

							questionsFileNameToAdd = treePath.getPathComponent(0).toString();
						}

						// Habilitaci�n del JButton correspondiente.
						jButtonAddQuestionsToTest[j].setEnabled(true);
					} else {
						if (treePath != null) {
							// Preparaci�n para la adici�n de todas las preguntas de un
							// fichero
							nodeQuestion = null;
							nodeQuestion = (DefaultMutableTreeNode) treePath.getPathComponent(0);
							codeOfQuestionsToAdd = "ALL_FILE";
							questionsFileNameToAdd = treePath.getPathComponent(0).toString();

							// Habilitaci�n del JButton correspondiente.
							jButtonAddQuestionsToTest[j].setEnabled(true);
						}
					}
				}
			});
		}
	}

	/*
	 * NOMBRE: JPanelInitStep3A. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step3Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JPanel del frame, reservando memoria para el
	 * mismo, estableciendo su color, layout...
	 */

	private void JPanelInitStep3A() {
		// Reserva de memoria para el panel principal del frame y
		// establecimiento de su layout.
		jPanelContent = new JPanel();
		boxLayoutPanelContent = new BoxLayout(jPanelContent, BoxLayout.Y_AXIS);
		jPanelContent.setLayout(boxLayoutPanelContent);
		jPanelContent.setAlignmentX(CENTER_ALIGNMENT);

		// Reserva de memoria para el jScrollPane, el JScrollPane principal del frame.
		jScrollPane = new JScrollPane(jPanelContent, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Reserva de memoria para el JPanel que contendr� al jLabelInstructions.
		jPanelInstructions = new JPanel();
		flowLayoutPanelInstructions = new FlowLayout(FlowLayout.CENTER);
		jPanelInstructions.setLayout(flowLayoutPanelInstructions);
		jPanelInstructions.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jPanelInstructions.add(jLabelInstructions);

		// Reserva de memoria para el array de JPanel que contendr�n a cada JTree.
		jPanelTree = new JPanel[questionsOfMultipleFiles.size()];
		jScrollPaneTree = new JScrollPane[questionsOfMultipleFiles.size()];
		boxLayoutPanelTree = new BoxLayout[questionsOfMultipleFiles.size()];

		for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
			jPanelTree[i] = new JPanel();
			boxLayoutPanelTree[i] = new BoxLayout(jPanelTree[i], BoxLayout.Y_AXIS);
			jPanelTree[i].setLayout(boxLayoutPanelTree[i]);

			String questionsFileName = ((Vector) questionsOfMultipleFiles.get(i)).get(0).toString();
			jPanelTree[i].setBorder(BorderFactory.createTitledBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK), questionsFileName, TitledBorder.LEFT,
					TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));
			jPanelTree[i].setAlignmentX(CENTER_ALIGNMENT);

			// Reserfa del JScrollPaneTree para el jTreeQuestionsFile.
			jScrollPaneTree[i] = new JScrollPane(jTreeQuestionsFile[i], JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPaneTree[i].setBorder(BorderFactory.createEmptyBorder());
			jPanelTree[i].add(jScrollPaneTree[i]);

			jButtonAddQuestionsToTest[i].setAlignmentX(RIGHT_ALIGNMENT);
			jPanelTree[i].add(jButtonAddQuestionsToTest[i]);
		}

		// Rerserva de memoria para el JTabbedPane.
		jTabbedPane = new JTabbedPane();

		// Bucle para la adici�n de los jPanelTree al jTabbedPane.
		for (int i = 0; i < questionsOfMultipleFiles.size(); i++) {
			String questionsFileName = ((Vector) questionsOfMultipleFiles.get(i)).get(0).toString();
			jTabbedPane.addTab(questionsFileName, jPanelTree[i]);
		}

		// Especificaci�n del tabb mostrado.
		if (questionsOfMultipleFiles.size() > 0) {
			jTabbedPane.setSelectedIndex(tabSelect);
		}

		// Reserva de memoria para el JPanel que contendr� los JButtons
		// relacionados
		// con el jTreeTestQuestion.
		jPanelFinalTreeButton = new JPanel();
		flowLayoutPanelFinalTreeButton = new FlowLayout(FlowLayout.RIGHT);
		jPanelFinalTreeButton.setLayout(flowLayoutPanelFinalTreeButton);

		jButtonDeleteQuestionToTest.setAlignmentX(RIGHT_ALIGNMENT);
		jButtonClear.setAlignmentX(RIGHT_ALIGNMENT);

		jPanelFinalTreeButton.add(jButtonDeleteQuestionToTest);
		jPanelFinalTreeButton.add(jButtonClear);

		// Reserva de memoria para el JPanel que contendr� a jTreeTestQuestion.
		jPanelFinalTree = new JPanel();
		boxLayoutPanelFinalTree = new BoxLayout(jPanelFinalTree, BoxLayout.Y_AXIS);
		jPanelFinalTree.setLayout(boxLayoutPanelFinalTree);

		jPanelFinalTree.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Preliminary result of the test file", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));
		jPanelFinalTree.setAlignmentX(CENTER_ALIGNMENT);

		// Reserfa del JScrollPaneTree para el jTreeQuestionsFile.
		jScrollPaneFinalTree = new JScrollPane(jTreeTestQuestion, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneFinalTree.setBorder(BorderFactory.createEmptyBorder());

		jPanelFinalTree.add(jScrollPaneFinalTree);
		jPanelFinalTree.add(jPanelFinalTreeButton);

		// Reserva de memoria e inicializaci�n para jSplitPane.
		jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, jTabbedPane, jPanelFinalTree);
		jSplitPane.setOneTouchExpandable(true);
		jSplitPane.setDividerSize(10);
		jSplitPane.setDividerLocation(1.0);
		jSplitPane.setAlignmentX(CENTER_ALIGNMENT);
		jSplitPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		// Reserva de memoria y establecimento del layout para el JPanel que
		// contendr� a los JButton.
		jPanelButton = new JPanel();
		flowLayoutPanelButton = new FlowLayout(FlowLayout.CENTER);
		jPanelButton.setLayout(flowLayoutPanelButton);

		jPanelButton.add(jButtonPrevious);
		jPanelButton.add(jButtonNext);
		jPanelButton.add(Box.createHorizontalGlue());
		jPanelButton.add(jButtonCancel);

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
		jPanelContent.add(jPanelInstructions);
		jPanelContent.add(Box.createVerticalGlue());

		jPanelContent.add(jSplitPane);

		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jPanelButton);
		jPanelContent.add(jPanelStatus);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// ////////////CONFIGURACI�N DE LOS COMPONENTES DEL
	// JINTERNAFRAME////////////////
	// ////////////PARA EL PASO
	// 3B///////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: Step3BInit. PERTENECE A: Clase FrmCreateTest. LLAMADA POR: Al
	 * hacer click sobre jButtonNext cuando el frame est� configurado
	 * seg�n Step2Init(). LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los componentes del frame, necesarios para el tercer
	 * paso de la configuracion de la bateria de test, reservando memoria para
	 * los mismos y estableciendo su tama�o, su contenido, funcionalidad...
	 */

	private void Step3BInit() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Establecimiento del m�nimo y m�ximo para el jProgressBar.
		jProgressBar.setMinimum(0);
		jProgressBar.setMaximum(questionsFileNamesAdded.size());

		// boolean workerInit = false;
		// SwingWorker worker = null;

		// Llamada al m�todo de la clase padre que devuelve las preguntas para
		// un fichero de preguntas.
		if (!step.equals("4A") && !step.equals("4B")) {
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			jLabelStatus.setText("Loading questions of file... ");
			jPanelStatus.paint(this.jPanelStatus.getGraphics());

			/*
			 * worker = new SwingWorker() { public Object construct() { return new
			 * GetRandomQuestions(); } };
			 */

			GetRandomQuestions worker = new GetRandomQuestions();
			// workerInit = true;
			worker.start();

			// Redibujo de la barra de progreso.
			while (questionsFileLoaded == false) {
				jLabelStatus.setText("Loading questions of file... " + questionsFileName);
				jProgressBar.setValue(process + 1);
				jPanelStatus.paint(this.jPanelStatus.getGraphics());
				this.paint(this.getGraphics());
				parent.paint(parent.getGraphics());
			}
			worker = null;
			questionsFileLoaded = false;

			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			jLabelStatus.setText("Loading questions of file... Finished");
			jProgressBar.setValue(jProgressBar.getMaximum());

			// Mensaje para informar al usuario del n�mero de ficheros de preguntas
			// con los que se formar� la bater�a de test.

			JOptionPane.showMessageDialog(parent, "The test will be created from "
					+ String.valueOf(questionsOfMultipleFiles.size()) + " question files.", "Info", 1);
		}

		JLabelInitStep3B();
		JButtonInitStep3B();

		if (step.equals("2"))
			JTreeTestQuestionInit();
		else
			JTreeTestQuestionInit(rootNodeTestQuestion);

		JProgressBarInit();
		JPanelInitStep3B();

		try {
			frameInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		step = "3B";
		
		// Default cursor
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: JLabelInitStep3B. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step3Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JLabel del frame, reservando memoria para los
	 * mismos y estableciendo su color, su contenido...
	 */

	private void JLabelInitStep3B() {
		// Reserva de memoria y establecimiento del contenido para los JLabel.
		jLabelInstructions = new JLabel("Test file generated randomly.");
		jLabelInstructions.setFont(new java.awt.Font("Dialog", 1, 20));

		jLabelStatus = new JLabel("Done                                         "
				+ "                                             " + "                    ");
		jLabelStatus.setFont(new java.awt.Font("Dialog", 0, 12));
	}

	/*
	 * NOMBRE: JButtonInitStep3B. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step3Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JButton del frame.
	 */

	private void JButtonInitStep3B() {
		jButtonCancel = null;
		// Reserva de memoria para los JButton, dependiendo de si los
		// iconos del JInteralFrame se han cargado con �xito o no.
		if (ImageIconLoad == true) {
			jButtonDeleteQuestionToTest = new JButton("Delete", iconDelete32);
			jButtonPrevious = new JButton("Previous", iconPrevious32);
			jButtonNext = new JButton("Next", iconNext32);
			jButtonCancel = new JButton("Cancel", iconCancel32);
		} else {
			jButtonDeleteQuestionToTest = new JButton("Delete");
			jButtonPrevious = new JButton("Previous");
			jButtonNext = new JButton("Next");
			jButtonCancel = new JButton("Cancel");
		}

		// Deshabilitaci�n inicial de jButtonDeleteQuestionsToTest.
		jButtonDeleteQuestionToTest.setEnabled(false);

		// Establecimiento de la funcionalidad de jButtonDeleteQuestionsToTest.
		jButtonDeleteQuestionToTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonDeleteQuestionToTestActionPerformed(
				/*
				 * nodeQuestionTest, questionsFileNameToDelete,
				 * codeOfQuestionsToDelete
				 */);
			}
		});

		// Establecimiento de la funcionalidad de jButtonPrevious.
		jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTreeQuestionsFile = null;
				questionsOfMultipleFiles = null;
				if (step == "4B")
					Step3CInit();
				else
					Step2Init();
			}
		});

		// Establecimiento de la funcionalidad de jButtonNext.
		jButtonNext.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Aviso de duraci�n prolongada de la tarea.
				jLabelStatus.setText("Extracting data for the creation of the test battery...");
				Step4AInit();
			}
		});

		// Establecimiento de la funcionalidad de jButtonCancel.
		jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closingWindow();
			}
		});
	}

	/*
	 * NOMBRE: JTreeTestQuestionInit. PERTENECE A: Clase FrmCreateTest. LLAMADA
	 * POR: Step3Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JTree y establece su contenido y funcionalidad.
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
		// DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Test");
		rootNodeTestQuestion = new DefaultMutableTreeNode("Test");

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

		// Deshabilitaci�n del jButtonNext.
		if (rootNodeTestQuestion.isLeaf())
			jButtonNext.setEnabled(false);
		else
			jButtonNext.setEnabled(true);

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
				TreePath treePath = jTreeTestQuestion.getSelectionPath();

				if (treePath != null && treePath.getPathCount() >= 3) {
					nodeQuestionTest = (DefaultMutableTreeNode) treePath.getPathComponent(2);

					codeOfQuestionsToDelete = nodeQuestionTest.getFirstChild().toString();
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
	 * NOMBRE: JTreeTestQuestionInit. PERTENECE A: Clase FrmCreateTest. LLAMADA
	 * POR: jButtonAddQuestionToTestActionPerformed();, Step3AInit(); LLAMA A:
	 * nada. RECIBE: rootNodeTestQuestion: Objeto de tipo DefaultMutableTreeNode.
	 * Contiene el nodo ra�z para el JTree que se est� inicializando. DEVUELVE:
	 * void. FUNCI�N: Inicializa los JTree y establece su contenido y
	 * funcionalidad.
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

					codeOfQuestionsToDelete = nodeQuestionTest.getFirstChild().toString();
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

		// Deshabilitaci�n o habilitaci�n del jButtonNext.
		if (rootNodeTestQuestion.isLeaf())
			jButtonNext.setEnabled(false);
		else
			jButtonNext.setEnabled(true);
	}

	/*
	 * NOMBRE: JPanelInitStep3B. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step3BInit(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JPanel del frame, reservando memoria para el
	 * mismo, estableciendo su color, layout...
	 */

	private void JPanelInitStep3B() {
		// Reserva de memoria para el panel principal del frame y
		// establecimiento de su layout.
		jPanelContent = new JPanel();
		boxLayoutPanelContent = new BoxLayout(jPanelContent, BoxLayout.Y_AXIS);
		jPanelContent.setLayout(boxLayoutPanelContent);
		jPanelContent.setAlignmentX(CENTER_ALIGNMENT);

		// Reserva de memoria para el jScrollPane, el JScrollPane principal del
		// frame.
		jScrollPane = new JScrollPane(jPanelContent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Reserva de memoria para el JPanel que contendr� al jLabelInstructions.
		jPanelInstructions = new JPanel();
		flowLayoutPanelInstructions = new FlowLayout(FlowLayout.CENTER);
		jPanelInstructions.setLayout(flowLayoutPanelInstructions);
		jPanelInstructions.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		// jPanelInstructions.setAlignmentX(RIGHT_ALIGNMENT);

		jPanelInstructions.add(jLabelInstructions);

		// Reserva de memoria para el JPanel que contendr� los JButtons
		// relacionados
		// con el jTreeTestQuestion.
		jPanelFinalTreeButton = new JPanel();
		flowLayoutPanelFinalTreeButton = new FlowLayout(FlowLayout.RIGHT);
		jPanelFinalTreeButton.setLayout(flowLayoutPanelFinalTreeButton);

		jButtonDeleteQuestionToTest.setAlignmentX(RIGHT_ALIGNMENT);

		jPanelFinalTreeButton.add(jButtonDeleteQuestionToTest);

		// Reserva de memoria para el JPanel que contendr� a jTreeTestQuestion.
		jPanelFinalTree = new JPanel();
		boxLayoutPanelFinalTree = new BoxLayout(jPanelFinalTree, BoxLayout.Y_AXIS);
		jPanelFinalTree.setLayout(boxLayoutPanelFinalTree);

		jPanelFinalTree.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Arial", 0, 14)));
		jPanelFinalTree.setAlignmentX(CENTER_ALIGNMENT);

		// Reserfa del JScrollPaneTree para el jTreeQuestionsFile.
		jScrollPaneFinalTree = new JScrollPane(jTreeTestQuestion, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneFinalTree.setBorder(BorderFactory.createEmptyBorder());

		jPanelFinalTree.add(jScrollPaneFinalTree);
		jPanelFinalTree.add(jPanelFinalTreeButton);

		// Reserva de memoria y establecimento del layout para el JPanel que
		// contendr� a los JButton.
		jPanelButton = new JPanel();
		flowLayoutPanelButton = new FlowLayout(FlowLayout.CENTER);
		jPanelButton.setLayout(flowLayoutPanelButton);

		jPanelButton.add(jButtonPrevious);
		jPanelButton.add(jButtonNext);
		jPanelButton.add(Box.createHorizontalGlue());
		jPanelButton.add(jButtonCancel);

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
		jPanelContent.add(jPanelInstructions);
		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jPanelFinalTree);
		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jPanelButton);
		jPanelContent.add(jPanelStatus);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// ////////////CONFIGURACI�N DE LOS COMPONENTES DEL
	// JINTERNAFRAME////////////////
	// ////////////PARA EL PASO
	// 3C///////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: Step3CInit. PERTENECE A: Clase FrmCreateTest. LLAMADA POR: Al
	 * hacer click sobre jButtonNext cuando el frame est� configurado
	 * seg�n Step2Init(). LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los componentes del frame, necesarios para el tercer
	 * paso de la configuracion de la bateria de test, reservando memoria para
	 * los mismos y estableciendo su tama�o, su contenido, funcionalidad...
	 */

	private void Step3CInit() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// No alterar el orden de inicializacion.
		JLabelInitStep3C();
		JButtonInitStep3C();

		if (step != "4B") {
			JTextFieldInitStep3C();
			JSliderInitStep3C();
			JRadioButtonInitStep3C();
			JCheckBoxInitStep3C();
		}

		JProgressBarInit();
		JPanelInitStep3C();

		try {
			frameInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		step = "3C";
		
		// Default cursor
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: JLabelInitStep3C. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step3Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JLabel del frame, reservando memoria para los
	 * mismos y estableciendo su color, su contenido...
	 */

	private void JLabelInitStep3C() {
		// Reserva de memoria y establecimiento del contenido para los JLabel.
		jLabelInstructions = new JLabel("Select the restrictions for the test configuration.");
		jLabelInstructions.setFont(new java.awt.Font("Dialog", 1, 20));

		jLabelDifficultyMax = new JLabel("Difficulty Max ");
		jLabelDifficultyMax.setFont(new java.awt.Font("Dialog", 1, 12));
		jLabelDifficultyMin = new JLabel("Difficulty Min ");
		jLabelDifficultyMin.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelDiscriminationMax = new JLabel("Discrimination Max ");
		jLabelDiscriminationMax.setFont(new java.awt.Font("Dialog", 1, 12));
		jLabelDiscriminationMin = new JLabel("Discrimination Min ");
		jLabelDiscriminationMin.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelGuessingMax = new JLabel("Guessing Max ");
		jLabelGuessingMax.setFont(new java.awt.Font("Dialog", 1, 12));
		jLabelGuessingMin = new JLabel("Guessing Min ");
		jLabelGuessingMin.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelExhibitionRateMax = new JLabel("Exhibition Rate Max ");
		jLabelExhibitionRateMax.setFont(new java.awt.Font("Dialog", 1, 12));
		jLabelExhibitionRateMin = new JLabel("Exhibition Rate Min ");
		jLabelExhibitionRateMin.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelAnswerTimeMax = new JLabel("Answer Time Max ");
		jLabelAnswerTimeMax.setFont(new java.awt.Font("Dialog", 1, 12));
		jLabelAnswerTimeMaxSeconds = new JLabel("(Seconds)");
		jLabelAnswerTimeMaxSeconds.setFont(new java.awt.Font("Dialog", 1, 12));
		jLabelAnswerTimeMin = new JLabel("Answer Time Min ");
		jLabelAnswerTimeMin.setFont(new java.awt.Font("Dialog", 1, 12));
		jLabelAnswerTimeMinSeconds = new JLabel("(Seconds)");
		jLabelAnswerTimeMinSeconds.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelSuccessRateMax = new JLabel("Success Rate Max ");
		jLabelSuccessRateMax.setFont(new java.awt.Font("Dialog", 1, 12));
		jLabelSuccessRateMin = new JLabel("Success Rate Min ");
		jLabelSuccessRateMin.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelNumberOfAnswersMax = new JLabel("Number of Answers Max ");
		jLabelNumberOfAnswersMax.setFont(new java.awt.Font("Dialog", 1, 12));
		jLabelNumberOfAnswersMin = new JLabel("Number of Answers Min ");
		jLabelNumberOfAnswersMin.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelQuestionsWithImage = new JLabel("Questions with Images ");
		jLabelQuestionsWithImage.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelStatus = new JLabel("Done                                         "
				+ "                                             " + "                    ");
		jLabelStatus.setFont(new java.awt.Font("Dialog", 0, 12));
	}

	/*
	 * NOMBRE: JButtonInitStep3C. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step3Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JButton del frame.
	 */

	private void JButtonInitStep3C() {
		jButtonCancel = null;
		// Reserva de memoria para los JButton, dependiendo de si los
		// iconos del JInteralFrame se han cargado con �xito o no.
		if (ImageIconLoad == true) {
			jButtonClear = new JButton("Clear", iconClear32);
			jButtonPrevious = new JButton("Previous", iconPrevious32);
			jButtonNext = new JButton("Next", iconNext32);
			jButtonCancel = new JButton("Cancel", iconCancel32);
		} else {
			jButtonClear = new JButton("Clear");
			jButtonPrevious = new JButton("Previous");
			jButtonNext = new JButton("Next");
			jButtonCancel = new JButton("Cancel");
		}

		// Establecimiento de la funcionalidad de jButtonClear.
		jButtonClear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonClearRestrictionsActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad de jButtonPrevious.
		jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTreeQuestionsFile = null;
				questionsOfMultipleFiles = null;
				Step2Init();
			}
		});

		// Establecimiento de la funcionalidad de jButtonNext.
		jButtonNext.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Aviso de duraci�n prolongada de la tarea.
				jLabelStatus.setText("Looking for questions of file... "
						+ "This process can take several minutes.");
				Step4BInit();
			}
		});

		// Establecimiento de la funcionalidad de jButtonCancel.
		jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closingWindow();
			}
		});
	}

	/*
	 * NOMBRE: JTextFieldInitStep3C. PERTENECE A: Clase FrmCreateTest. LLAMADA
	 * POR: Step3CInit(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa el jTextFieldNumQuestions del frame.
	 */

	private void JTextFieldInitStep3C() {
		// En la configuraci�n de los JSlider, los valores de configuraci�n se
		// multiplican por 10 y por 100 debido a que los JSlider solo adminten
		// enteros y los valores con los que se trabaja son doubles.

		// Reserva de memoria para los JTextField.
		jTextFieldDifficultyMax = new JTextField(String.valueOf(DIFFICULTY_MAX), 4);
		jTextFieldDifficultyMin = new JTextField(String.valueOf(DIFFICULTY_MIN), 4);

		jTextFieldDiscriminationMax = new JTextField(String.valueOf(DISCRIMINATION_MAX), 4);
		jTextFieldDiscriminationMin = new JTextField(String.valueOf(DISCRIMINATION_MIN), 4);

		jTextFieldGuessingMax = new JTextField(String.valueOf(GUESSING_MAX), 4);
		jTextFieldGuessingMin = new JTextField(String.valueOf(GUESSING_MIN), 4);

		jTextFieldExhibitionRateMax = new JTextField(String.valueOf(EXHIBITION_RATE_MAX), 4);
		jTextFieldExhibitionRateMin = new JTextField(String.valueOf(EXHIBITION_RATE_MIN), 4);

		jTextFieldAnswerTimeMax = new JTextField(String.valueOf(ANSWER_TIME_MAX), 4);
		jTextFieldAnswerTimeMin = new JTextField(String.valueOf(ANSWER_TIME_MIN), 4);

		jTextFieldSuccessRateMax = new JTextField(String.valueOf(SUCCESS_RATE_MAX), 4);
		jTextFieldSuccessRateMin = new JTextField(String.valueOf(SUCCESS_RATE_MIN), 4);

		jTextFieldNumberOfAnswersMax = new JTextField(String.valueOf(NUMBER_OF_ANSWERS_MAX), 4);
		jTextFieldNumberOfAnswersMin = new JTextField(String.valueOf(NUMBER_OF_ANSWERS_MIN), 4);

		// Establecimiento de la funcionalidad de jTextFieldDifficultyMin.
		jTextFieldDifficultyMin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldDifficultyMin.getText().length() > 0) {
						if (Double.valueOf(jTextFieldDifficultyMin.getText()).doubleValue() < DIFFICULTY_MIN) {
							jTextFieldDifficultyMin.setText(String.valueOf(DIFFICULTY_MIN));
							jSliderDifficultyMin.setValue((int) DIFFICULTY_MIN * 10);
						} else {
							if (Double.valueOf(jTextFieldDifficultyMin.getText()).doubleValue() > DIFFICULTY_MAX) {
								jTextFieldDifficultyMin.setText(String.valueOf(DIFFICULTY_MAX));
								jSliderDifficultyMin.setValue((int) DIFFICULTY_MAX * 10);
							} else {
								jSliderDifficultyMin.setValue((int) (Double
										.valueOf(jTextFieldDifficultyMin.getText()).doubleValue() * 10));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldDifficultyMin.setText(String.valueOf(DIFFICULTY_MIN));
					jSliderDifficultyMin.setValue((int) DIFFICULTY_MIN * 10);
				}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldDifficultyMax.
		jTextFieldDifficultyMax.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldDifficultyMax.getText().length() > 0) {
						if (Double.valueOf(jTextFieldDifficultyMax.getText()).doubleValue() < DIFFICULTY_MIN) {
							jTextFieldDifficultyMax.setText(String.valueOf(DIFFICULTY_MIN));
							jSliderDifficultyMax.setValue((int) DIFFICULTY_MIN * 10);
						} else {
							if (Double.valueOf(jTextFieldDifficultyMax.getText()).doubleValue() > DIFFICULTY_MAX) {
								jTextFieldDifficultyMax.setText(String.valueOf(DIFFICULTY_MAX));
								jSliderDifficultyMax.setValue((int) DIFFICULTY_MAX * 10);
							} else {
								jSliderDifficultyMax.setValue((int) (Double
										.valueOf(jTextFieldDifficultyMax.getText()).doubleValue() * 10));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldDifficultyMax.setText(String.valueOf(DIFFICULTY_MAX));
					jSliderDifficultyMax.setValue((int) DIFFICULTY_MAX * 10);
				}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldDiscriminationMin.
		jTextFieldDiscriminationMin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldDiscriminationMin.getText().length() > 0) {
						if (Double.valueOf(jTextFieldDiscriminationMin.getText()).doubleValue() < DISCRIMINATION_MIN) {
							jTextFieldDiscriminationMin.setText(String.valueOf(DISCRIMINATION_MIN));
							jSliderDiscriminationMin.setValue((int) DISCRIMINATION_MIN * 10);
						} else {
							if (Double.valueOf(jTextFieldDiscriminationMin.getText()).doubleValue() > DISCRIMINATION_MAX) {
								jTextFieldDiscriminationMin.setText(String.valueOf(DISCRIMINATION_MAX));
								jSliderDiscriminationMin.setValue((int) DISCRIMINATION_MAX * 10);
							} else {
								jSliderDiscriminationMin.setValue((int) (Double.valueOf(
										jTextFieldDiscriminationMin.getText()).doubleValue() * 10));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldDiscriminationMin.setText(String.valueOf(DISCRIMINATION_MIN));
					jSliderDiscriminationMin.setValue((int) DISCRIMINATION_MIN * 10);
				}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldDiscriminationMax.
		jTextFieldDiscriminationMax.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldDiscriminationMax.getText().length() > 0) {
						if (Double.valueOf(jTextFieldDiscriminationMax.getText()).doubleValue() < DISCRIMINATION_MIN) {
							jTextFieldDiscriminationMax.setText(String.valueOf(DISCRIMINATION_MIN));
							jSliderDiscriminationMax.setValue((int) DISCRIMINATION_MIN * 10);
						} else {
							if (Double.valueOf(jTextFieldDiscriminationMax.getText()).doubleValue() > DISCRIMINATION_MAX) {
								jTextFieldDiscriminationMax.setText(String.valueOf(DISCRIMINATION_MAX));
								jSliderDiscriminationMax.setValue((int) DISCRIMINATION_MAX * 10);
							} else {
								jSliderDiscriminationMax.setValue((int) (Double.valueOf(
										jTextFieldDiscriminationMax.getText()).doubleValue() * 10));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldDiscriminationMax.setText(String.valueOf(DISCRIMINATION_MAX));
					jSliderDiscriminationMax.setValue((int) DISCRIMINATION_MAX * 10);
				}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldGuessingMin.
		jTextFieldGuessingMin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldGuessingMin.getText().length() > 0) {
						if (Double.valueOf(jTextFieldGuessingMin.getText()).doubleValue() < GUESSING_MIN) {
							jTextFieldGuessingMin.setText(String.valueOf(GUESSING_MIN));
							jSliderGuessingMin.setValue((int) (GUESSING_MIN * 100));
						} else {
							if (Double.valueOf(jTextFieldGuessingMin.getText()).doubleValue() > GUESSING_MAX) {
								jTextFieldGuessingMin.setText(String.valueOf(GUESSING_MAX));
								jSliderGuessingMin.setValue((int) (GUESSING_MAX * 100));
							} else {
								jSliderGuessingMin.setValue((int) (Double.valueOf(jTextFieldGuessingMin.getText())
										.doubleValue() * 100));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldGuessingMin.setText(String.valueOf(GUESSING_MIN));
					jSliderGuessingMin.setValue((int) (GUESSING_MIN * 100));
				}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldGuessingMax.
		jTextFieldGuessingMax.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldGuessingMax.getText().length() > 0) {
						if (Double.valueOf(jTextFieldGuessingMax.getText()).doubleValue() < GUESSING_MIN) {
							jTextFieldGuessingMax.setText(String.valueOf(GUESSING_MIN));
							jSliderGuessingMax.setValue((int) (GUESSING_MIN * 100));
						} else {
							if (Double.valueOf(jTextFieldGuessingMax.getText()).doubleValue() > GUESSING_MAX) {
								jTextFieldGuessingMax.setText(String.valueOf(GUESSING_MAX));
								jSliderGuessingMax.setValue((int) (GUESSING_MAX * 100));
							} else {
								jSliderGuessingMax.setValue((int) (Double.valueOf(jTextFieldGuessingMax.getText())
										.doubleValue() * 100));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldGuessingMax.setText(String.valueOf(GUESSING_MAX));
					jSliderGuessingMax.setValue((int) (GUESSING_MAX * 100));
				}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldExhibitionRateMin.
		jTextFieldExhibitionRateMin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldExhibitionRateMin.getText().length() > 0) {
						if (Double.valueOf(jTextFieldExhibitionRateMin.getText()).doubleValue() < EXHIBITION_RATE_MIN) {
							jTextFieldExhibitionRateMin.setText(String.valueOf(EXHIBITION_RATE_MIN));
							jSliderExhibitionRateMin.setValue((int) (EXHIBITION_RATE_MIN * 100));
						} else {
							if (Double.valueOf(jTextFieldExhibitionRateMin.getText()).doubleValue() > EXHIBITION_RATE_MAX) {
								jTextFieldExhibitionRateMin.setText(String.valueOf(EXHIBITION_RATE_MAX));
								jSliderExhibitionRateMin.setValue((int) (EXHIBITION_RATE_MAX * 100));
							} else {
								jSliderExhibitionRateMin.setValue((int) (Double.valueOf(
										jTextFieldExhibitionRateMin.getText()).doubleValue() * 100));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldExhibitionRateMin.setText(String.valueOf(EXHIBITION_RATE_MIN));
					jSliderExhibitionRateMin.setValue((int) (EXHIBITION_RATE_MIN * 100));
				}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldExhibitionRateMax.
		jTextFieldExhibitionRateMax.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldExhibitionRateMax.getText().length() > 0) {
						if (Double.valueOf(jTextFieldExhibitionRateMax.getText()).doubleValue() < EXHIBITION_RATE_MIN) {
							jTextFieldExhibitionRateMax.setText(String.valueOf(EXHIBITION_RATE_MIN));
							jSliderExhibitionRateMax.setValue((int) (EXHIBITION_RATE_MIN * 100));
						} else {
							if (Double.valueOf(jTextFieldExhibitionRateMax.getText()).doubleValue() > EXHIBITION_RATE_MAX) {
								jTextFieldExhibitionRateMax.setText(String.valueOf(EXHIBITION_RATE_MAX));
								jSliderExhibitionRateMax.setValue((int) (EXHIBITION_RATE_MAX * 100));
							} else {
								jSliderExhibitionRateMax.setValue((int) (Double.valueOf(
										jTextFieldExhibitionRateMax.getText()).doubleValue() * 100));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldExhibitionRateMax.setText(String.valueOf(EXHIBITION_RATE_MAX));
					jSliderExhibitionRateMax.setValue((int) (EXHIBITION_RATE_MAX * 100));

				}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldAnswerTimeMin.
		jTextFieldAnswerTimeMin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldAnswerTimeMin.getText().length() > 0) {
						if (Integer.valueOf(jTextFieldAnswerTimeMin.getText()).intValue() < ANSWER_TIME_MIN) {
							jTextFieldAnswerTimeMin.setText(String.valueOf(ANSWER_TIME_MIN));
							jSliderAnswerTimeMin.setValue(ANSWER_TIME_MIN);
						} else {
							if (Integer.valueOf(jTextFieldAnswerTimeMin.getText()).intValue() > ANSWER_TIME_MAX) {
								jTextFieldAnswerTimeMin.setText(String.valueOf(ANSWER_TIME_MAX));
								jSliderAnswerTimeMin.setValue(ANSWER_TIME_MAX);
							} else {
								jSliderAnswerTimeMin.setValue(Integer.valueOf(jTextFieldAnswerTimeMin.getText())
										.intValue());
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldAnswerTimeMin.setText(String.valueOf(ANSWER_TIME_MIN));
					jSliderAnswerTimeMin.setValue(ANSWER_TIME_MIN);
				}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldAnswerTimeMax.
		jTextFieldAnswerTimeMax.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldAnswerTimeMax.getText().length() > 0) {
						if (Integer.valueOf(jTextFieldAnswerTimeMax.getText()).intValue() < ANSWER_TIME_MIN) {
							jTextFieldAnswerTimeMax.setText(String.valueOf(ANSWER_TIME_MIN));
							jSliderAnswerTimeMax.setValue(ANSWER_TIME_MIN);
						} else {
							if (Integer.valueOf(jTextFieldAnswerTimeMax.getText()).intValue() > ANSWER_TIME_MAX) {
								jTextFieldAnswerTimeMax.setText(String.valueOf(ANSWER_TIME_MAX));
								jSliderAnswerTimeMax.setValue(ANSWER_TIME_MAX);
							} else {
								jSliderAnswerTimeMax.setValue(Integer.valueOf(jTextFieldAnswerTimeMax.getText())
										.intValue());
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldAnswerTimeMax.setText(String.valueOf(ANSWER_TIME_MAX));
					jSliderAnswerTimeMax.setValue(ANSWER_TIME_MAX);
				}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldSuccessRateMin.
		jTextFieldSuccessRateMin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldSuccessRateMin.getText().length() > 0) {
						if (Double.valueOf(jTextFieldSuccessRateMin.getText()).doubleValue() < SUCCESS_RATE_MIN) {
							jTextFieldSuccessRateMin.setText(String.valueOf(SUCCESS_RATE_MIN));
							jSliderSuccessRateMin.setValue((int) (SUCCESS_RATE_MIN * 100));
						} else {
							if (Double.valueOf(jTextFieldSuccessRateMin.getText()).doubleValue() > SUCCESS_RATE_MAX) {
								jTextFieldSuccessRateMin.setText(String.valueOf(SUCCESS_RATE_MAX));
								jSliderSuccessRateMin.setValue((int) (SUCCESS_RATE_MAX * 100));
							} else {
								jSliderSuccessRateMin.setValue((int) (Double.valueOf(
										jTextFieldSuccessRateMin.getText()).doubleValue() * 100));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldSuccessRateMin.setText(String.valueOf(SUCCESS_RATE_MIN));
					jSliderSuccessRateMin.setValue((int) (SUCCESS_RATE_MIN * 100));
				}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldSuccessRateMax.
		jTextFieldSuccessRateMax.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldSuccessRateMax.getText().length() > 0) {
						if (Double.valueOf(jTextFieldSuccessRateMax.getText()).doubleValue() < SUCCESS_RATE_MIN) {
							jTextFieldSuccessRateMax.setText(String.valueOf(SUCCESS_RATE_MIN));
							jSliderSuccessRateMax.setValue((int) (SUCCESS_RATE_MIN * 100));
						} else {
							if (Double.valueOf(jTextFieldSuccessRateMax.getText()).doubleValue() > SUCCESS_RATE_MAX) {
								jTextFieldSuccessRateMax.setText(String.valueOf(SUCCESS_RATE_MAX));
								jSliderSuccessRateMax.setValue((int) (SUCCESS_RATE_MAX * 100));
							} else {
								jSliderSuccessRateMax.setValue((int) (Double.valueOf(
										jTextFieldSuccessRateMax.getText()).doubleValue() * 100));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldSuccessRateMax.setText(String.valueOf(SUCCESS_RATE_MAX));
					jSliderSuccessRateMax.setValue((int) (SUCCESS_RATE_MAX * 100));
				}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldNumberOfAnswersMin.
		jTextFieldNumberOfAnswersMin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldNumberOfAnswersMin.getText().length() > 0) {
						if (Integer.valueOf(jTextFieldNumberOfAnswersMin.getText()).intValue() < NUMBER_OF_ANSWERS_MIN) {
							jTextFieldNumberOfAnswersMin.setText(String.valueOf(NUMBER_OF_ANSWERS_MIN));
							jSliderNumberOfAnswersMin.setValue(NUMBER_OF_ANSWERS_MIN);
						} else {
							if (Integer.valueOf(jTextFieldNumberOfAnswersMin.getText()).intValue() > NUMBER_OF_ANSWERS_MAX) {
								jTextFieldNumberOfAnswersMin.setText(String.valueOf(NUMBER_OF_ANSWERS_MAX));
								jSliderNumberOfAnswersMin.setValue(NUMBER_OF_ANSWERS_MAX);
							} else {
								jSliderNumberOfAnswersMin.setValue(Integer.valueOf(
										jTextFieldNumberOfAnswersMin.getText()).intValue());
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldNumberOfAnswersMin.setText(String.valueOf(NUMBER_OF_ANSWERS_MIN));
					jSliderNumberOfAnswersMin.setValue(NUMBER_OF_ANSWERS_MIN);
				}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldNumberOfAnswersMax.
		jTextFieldNumberOfAnswersMax.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldNumberOfAnswersMax.getText().length() > 0) {
						if (Integer.valueOf(jTextFieldNumberOfAnswersMax.getText()).intValue() < NUMBER_OF_ANSWERS_MIN) {
							jTextFieldNumberOfAnswersMax.setText(String.valueOf(NUMBER_OF_ANSWERS_MIN));
							jSliderNumberOfAnswersMax.setValue(NUMBER_OF_ANSWERS_MIN);
						} else {
							if (Integer.valueOf(jTextFieldNumberOfAnswersMax.getText()).intValue() > NUMBER_OF_ANSWERS_MAX) {
								jTextFieldNumberOfAnswersMax.setText(String.valueOf(NUMBER_OF_ANSWERS_MAX));
								jSliderNumberOfAnswersMax.setValue(NUMBER_OF_ANSWERS_MAX);
							} else {
								jSliderNumberOfAnswersMax.setValue(Integer.valueOf(
										jTextFieldNumberOfAnswersMax.getText()).intValue());
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldNumberOfAnswersMax.setText(String.valueOf(NUMBER_OF_ANSWERS_MAX));
					jSliderNumberOfAnswersMax.setValue(NUMBER_OF_ANSWERS_MAX);
				}
			}
		});
	}

	/*
	 * NOMBRE: JSliderInitStep3C. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step3CInit(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JSlider del frame.
	 */

	private void JSliderInitStep3C() {
		// En la configuraci�n de los JSlider, los valores de configuraci�n se
		// multiplican por 10 y por 100 debido a que los JSlider solo adminten
		// enteros y los valores con los que se trabaja son doubles.

		// Reserva de memoria para el jSliderDifficultyMax
		jSliderDifficultyMax = new JSlider(JSlider.HORIZONTAL, (int) (DIFFICULTY_MIN * 10),
				(int) (DIFFICULTY_MAX * 10), (int) (DIFFICULTY_MAX * 10));
		jSliderDifficultyMax.setPaintTicks(true);
		jSliderDifficultyMax.setPaintLabels(true);
		jSliderDifficultyMax.setMajorTickSpacing(10);
		jSliderDifficultyMax.setMinorTickSpacing(1);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderDifficultyMax.
		Dictionary jSliderDifficultyMaxLabelTable = new Hashtable();
		for (int i = (int) (DIFFICULTY_MIN * 10); i <= (int) (DIFFICULTY_MAX * 10); i++) {
			if (i % 10 == 0)
				jSliderDifficultyMaxLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 10)));
		}

		jSliderDifficultyMax.setLabelTable(jSliderDifficultyMaxLabelTable);

		// Reserva de memoria para el jSliderDifficultyMin.
		jSliderDifficultyMin = new JSlider(JSlider.HORIZONTAL, (int) (DIFFICULTY_MIN * 10),
				(int) (DIFFICULTY_MAX * 10), (int) (DIFFICULTY_MIN * 10));
		jSliderDifficultyMin.setPaintTicks(true);
		jSliderDifficultyMin.setPaintLabels(true);
		jSliderDifficultyMin.setMajorTickSpacing(10);
		jSliderDifficultyMin.setMinorTickSpacing(1);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderDifficultyMax.
		Dictionary jSliderDifficultyMinLabelTable = new Hashtable();
		for (int i = (int) (DIFFICULTY_MIN * 10); i <= (int) (DIFFICULTY_MAX * 10); i++) {
			if (i % 10 == 0)
				jSliderDifficultyMinLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 10)));
		}

		jSliderDifficultyMin.setLabelTable(jSliderDifficultyMinLabelTable);

		// Reserva de memoria para el jSliderDiscriminationMax.
		jSliderDiscriminationMax = new JSlider(JSlider.HORIZONTAL, (int) (DISCRIMINATION_MIN * 10),
				(int) (DISCRIMINATION_MAX * 10), (int) (DISCRIMINATION_MAX * 10));
		jSliderDiscriminationMax.setPaintTicks(true);
		jSliderDiscriminationMax.setPaintLabels(true);
		jSliderDiscriminationMax.setMajorTickSpacing(10);
		jSliderDiscriminationMax.setMinorTickSpacing(1);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderDifficultyMax.
		Dictionary jSliderDiscriminationMaxLabelTable = new Hashtable();
		for (int i = (int) (DISCRIMINATION_MIN * 10); i <= (int) (DISCRIMINATION_MAX * 10); i++) {
			if (i % 10 == 0)
				jSliderDiscriminationMaxLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 10.0)));
		}

		jSliderDiscriminationMax.setLabelTable(jSliderDiscriminationMaxLabelTable);

		// Reserva de memoria para el jSliderDiscriminationMin.
		jSliderDiscriminationMin = new JSlider(JSlider.HORIZONTAL, (int) (DISCRIMINATION_MIN * 10),
				(int) (DISCRIMINATION_MAX * 10), (int) (DISCRIMINATION_MIN * 10));
		jSliderDiscriminationMin.setPaintTicks(true);
		jSliderDiscriminationMin.setPaintLabels(true);
		jSliderDiscriminationMin.setMajorTickSpacing(10);
		jSliderDiscriminationMin.setMinorTickSpacing(1);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderDifficultyMax.
		Dictionary jSliderDiscriminationMinLabelTable = new Hashtable();
		for (int i = (int) (DISCRIMINATION_MIN * 10); i <= (int) (DISCRIMINATION_MAX * 10); i++) {
			if (i % 10 == 0)
				jSliderDiscriminationMinLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 10.0)));
		}

		jSliderDiscriminationMin.setLabelTable(jSliderDiscriminationMinLabelTable);

		// Reserva de memoria para el jSliderGuessingMax.
		jSliderGuessingMax = new JSlider(JSlider.HORIZONTAL, (int) (GUESSING_MIN * 100),
				(int) (GUESSING_MAX * 100), (int) (GUESSING_MAX * 100));
		jSliderGuessingMax.setPaintTicks(true);
		jSliderGuessingMax.setPaintLabels(true);
		jSliderGuessingMax.setMajorTickSpacing(10);
		jSliderGuessingMax.setMinorTickSpacing(1);

		// Bucle para el establecimiento de las etiquetas del jSliderGuessingMax.
		Dictionary jSliderGuessingMaxLabelTable = new Hashtable();
		for (int i = (int) (GUESSING_MIN * 100); i <= (int) (GUESSING_MAX * 100); i++) {
			if (i % 10 == 0)
				jSliderGuessingMaxLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 100.0)));
		}

		jSliderGuessingMax.setLabelTable(jSliderGuessingMaxLabelTable);

		// Reserva de memoria para el jSliderGuessingMin.
		jSliderGuessingMin = new JSlider(JSlider.HORIZONTAL, (int) (GUESSING_MIN * 100),
				(int) (GUESSING_MAX * 100), (int) (GUESSING_MIN * 100));
		jSliderGuessingMin.setPaintTicks(true);
		jSliderGuessingMin.setPaintLabels(true);
		jSliderGuessingMin.setMajorTickSpacing(10);
		jSliderGuessingMin.setMinorTickSpacing(1);

		// Bucle para el establecimiento de las etiquetas del jSliderGuessingMin.
		Dictionary jSliderGuessingMinLabelTable = new Hashtable();
		for (int i = (int) (GUESSING_MIN * 100); i <= (int) (GUESSING_MAX * 100); i++) {
			if (i % 10 == 0)
				jSliderGuessingMinLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 100.0)));
		}

		jSliderGuessingMin.setLabelTable(jSliderGuessingMinLabelTable);

		// Reserva de memoria para el jSliderExhibitionRateMax.
		jSliderExhibitionRateMax = new JSlider(JSlider.HORIZONTAL, (int) (EXHIBITION_RATE_MIN * 100),
				(int) (EXHIBITION_RATE_MAX * 100), (int) (EXHIBITION_RATE_MAX * 100));
		jSliderExhibitionRateMax.setPaintTicks(true);
		jSliderExhibitionRateMax.setPaintLabels(true);
		jSliderExhibitionRateMax.setMajorTickSpacing(10);
		jSliderExhibitionRateMax.setMinorTickSpacing(1);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderDifficultyMax.
		Dictionary jSliderExhibitionRateMaxLabelTable = new Hashtable();
		for (int i = (int) (EXHIBITION_RATE_MIN * 100); i <= (int) (EXHIBITION_RATE_MAX * 100); i++) {
			if (i % 20 == 0)
				jSliderExhibitionRateMaxLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 100.0)));
		}

		jSliderExhibitionRateMax.setLabelTable(jSliderExhibitionRateMaxLabelTable);

		// Reserva de memoria para el jSliderExhibitionRateMin.
		jSliderExhibitionRateMin = new JSlider(JSlider.HORIZONTAL, (int) (EXHIBITION_RATE_MIN * 100),
				(int) (EXHIBITION_RATE_MAX * 100), (int) (EXHIBITION_RATE_MIN * 100));
		jSliderExhibitionRateMin.setPaintTicks(true);
		jSliderExhibitionRateMin.setPaintLabels(true);
		jSliderExhibitionRateMin.setMajorTickSpacing(10);
		jSliderExhibitionRateMin.setMinorTickSpacing(1);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderDifficultyMax.
		Dictionary jSliderExhibitionRateMinLabelTable = new Hashtable();
		for (int i = (int) (EXHIBITION_RATE_MIN * 100); i <= (int) (EXHIBITION_RATE_MAX * 100); i++) {
			if (i % 20 == 0)
				jSliderExhibitionRateMinLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 100.0)));
		}

		jSliderExhibitionRateMin.setLabelTable(jSliderExhibitionRateMinLabelTable);

		// Reserva de memoria para el jSliderAnswerTimeMax
		jSliderAnswerTimeMax = new JSlider(JSlider.HORIZONTAL, ANSWER_TIME_MIN, ANSWER_TIME_MAX,
				ANSWER_TIME_MAX);
		jSliderAnswerTimeMax.setPaintTicks(true);
		jSliderAnswerTimeMax.setPaintLabels(true);
		jSliderAnswerTimeMax.setMajorTickSpacing(10);
		jSliderAnswerTimeMax.setMinorTickSpacing(1);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderAnswerTimeMax.
		Dictionary jSliderAnswerTimeMaxLabelTable = new Hashtable();
		for (int i = ANSWER_TIME_MIN; i <= ANSWER_TIME_MAX; i++) {
			if (i % 100 == 0)
				jSliderAnswerTimeMaxLabelTable.put(new Integer(i), new JLabel(String.valueOf(i)));
		}

		jSliderAnswerTimeMax.setLabelTable(jSliderAnswerTimeMaxLabelTable);

		// Reserva de memoria para el jSliderDifficultyMin.
		jSliderAnswerTimeMin = new JSlider(JSlider.HORIZONTAL, ANSWER_TIME_MIN, ANSWER_TIME_MAX,
				ANSWER_TIME_MIN);
		jSliderAnswerTimeMin.setPaintTicks(true);
		jSliderAnswerTimeMin.setPaintLabels(true);
		jSliderAnswerTimeMin.setMajorTickSpacing(10);
		jSliderAnswerTimeMin.setMinorTickSpacing(1);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderDifficultyMax.
		Dictionary jSliderAnswerTimeMinLabelTable = new Hashtable();
		for (int i = ANSWER_TIME_MIN; i <= ANSWER_TIME_MAX; i++) {
			if (i % 100 == 0)
				jSliderAnswerTimeMinLabelTable.put(new Integer(i), new JLabel(String.valueOf(i)));
		}

		jSliderAnswerTimeMin.setLabelTable(jSliderAnswerTimeMinLabelTable);

		// Reserva de memoria para el jSliderSuccessRateMax.
		jSliderSuccessRateMax = new JSlider(JSlider.HORIZONTAL, (int) (SUCCESS_RATE_MIN * 100),
				(int) (SUCCESS_RATE_MAX * 100), (int) (SUCCESS_RATE_MAX * 100));
		jSliderSuccessRateMax.setPaintTicks(true);
		jSliderSuccessRateMax.setPaintLabels(true);
		jSliderSuccessRateMax.setMajorTickSpacing(10);
		jSliderSuccessRateMax.setMinorTickSpacing(1);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderDifficultyMax.
		Dictionary jSliderSuccessRateMaxLabelTable = new Hashtable();
		for (int i = (int) (SUCCESS_RATE_MIN * 100); i <= (int) (SUCCESS_RATE_MAX * 100); i++) {
			if (i % 20 == 0)
				jSliderSuccessRateMaxLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 100.0)));
		}

		jSliderSuccessRateMax.setLabelTable(jSliderSuccessRateMaxLabelTable);

		// Reserva de memoria para el jSliderSuccessRateMin.
		jSliderSuccessRateMin = new JSlider(JSlider.HORIZONTAL, (int) (SUCCESS_RATE_MIN * 100),
				(int) (SUCCESS_RATE_MAX * 100), (int) (SUCCESS_RATE_MIN * 100));
		jSliderSuccessRateMin.setPaintTicks(true);
		jSliderSuccessRateMin.setPaintLabels(true);
		jSliderSuccessRateMin.setMajorTickSpacing(10);
		jSliderSuccessRateMin.setMinorTickSpacing(1);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderDifficultyMIN.
		Dictionary jSliderSuccessRateMinLabelTable = new Hashtable();
		for (int i = (int) (SUCCESS_RATE_MIN * 100); i <= (int) (SUCCESS_RATE_MAX * 100); i++) {
			if (i % 20 == 0)
				jSliderSuccessRateMinLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 100.0)));
		}

		jSliderSuccessRateMin.setLabelTable(jSliderSuccessRateMinLabelTable);

		// Reserva de memoria para el jSliderNumberOfAnswersMax.
		jSliderNumberOfAnswersMax = new JSlider(JSlider.HORIZONTAL, NUMBER_OF_ANSWERS_MIN,
				NUMBER_OF_ANSWERS_MAX, NUMBER_OF_ANSWERS_MAX);
		jSliderNumberOfAnswersMax.setPaintTicks(true);
		jSliderNumberOfAnswersMax.setPaintLabels(true);
		jSliderNumberOfAnswersMax.setMajorTickSpacing(10);
		jSliderNumberOfAnswersMax.setMinorTickSpacing(1);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderDifficultyMax.
		Dictionary jSliderNumberOfAnswersMaxLabelTable = new Hashtable();
		for (int i = NUMBER_OF_ANSWERS_MIN; i <= NUMBER_OF_ANSWERS_MAX; i++) {
			jSliderNumberOfAnswersMaxLabelTable.put(new Integer(i), new JLabel(String.valueOf(i)));
		}

		jSliderNumberOfAnswersMax.setLabelTable(jSliderNumberOfAnswersMaxLabelTable);

		// Reserva de memoria para el jSliderNumberOfAnswersMin.
		jSliderNumberOfAnswersMin = new JSlider(JSlider.HORIZONTAL, NUMBER_OF_ANSWERS_MIN,
				NUMBER_OF_ANSWERS_MAX, NUMBER_OF_ANSWERS_MIN);
		jSliderNumberOfAnswersMin.setPaintTicks(true);
		jSliderNumberOfAnswersMin.setPaintLabels(true);
		jSliderNumberOfAnswersMin.setMajorTickSpacing(10);
		jSliderNumberOfAnswersMin.setMinorTickSpacing(1);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderDifficultyMIN.
		Dictionary jSliderNumberOfAnswersMinLabelTable = new Hashtable();
		for (int i = NUMBER_OF_ANSWERS_MIN; i <= NUMBER_OF_ANSWERS_MAX; i++) {
			jSliderNumberOfAnswersMinLabelTable.put(new Integer(i), new JLabel(String.valueOf(i)));
		}

		jSliderNumberOfAnswersMin.setLabelTable(jSliderNumberOfAnswersMinLabelTable);

		// Establecimiento de la funcionalidad de jSliderDifficultyMin.
		jSliderDifficultyMin.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jSliderDifficultyMax.getValue() < jSliderDifficultyMin.getValue()) {
					int tempInt = jSliderDifficultyMax.getValue();
					jSliderDifficultyMax.setValue(jSliderDifficultyMin.getValue());
					jSliderDifficultyMin.setValue(tempInt);
				}
				jTextFieldDifficultyMin.setText(String.valueOf(jSliderDifficultyMin.getValue() / 10.0));
			}
		});

		// Establecimiento de la funcionalidad de jSliderDifficultyMax.
		jSliderDifficultyMax.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jSliderDifficultyMin.getValue() > jSliderDifficultyMax.getValue()) {
					int tempInt = jSliderDifficultyMin.getValue();
					jSliderDifficultyMin.setValue(jSliderDifficultyMax.getValue());
					jSliderDifficultyMax.setValue(tempInt);
				}

				jTextFieldDifficultyMax.setText(String.valueOf(jSliderDifficultyMax.getValue() / 10.0));
			}
		});

		// Establecimiento de la funcionalidad de jSliderDiscriminationMin.
		jSliderDiscriminationMin.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jSliderDiscriminationMax.getValue() < jSliderDiscriminationMin.getValue()) {
					int tempInt = jSliderDiscriminationMax.getValue();
					jSliderDiscriminationMax.setValue(jSliderDiscriminationMin.getValue());
					jSliderDiscriminationMin.setValue(tempInt);
				}

				jTextFieldDiscriminationMin.setText(String.valueOf(jSliderDiscriminationMin.getValue() / 10.0));
			}
		});

		// Establecimiento de la funcionalidad de jSliderDiscriminationMax.
		jSliderDiscriminationMax.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jSliderDiscriminationMin.getValue() > jSliderDiscriminationMax.getValue()) {
					int tempInt = jSliderDiscriminationMin.getValue();
					jSliderDiscriminationMin.setValue(jSliderDiscriminationMax.getValue());
					jSliderDiscriminationMax.setValue(tempInt);
				}

				jTextFieldDiscriminationMax.setText(String.valueOf(jSliderDiscriminationMax.getValue() / 10.0));
			}
		});

		// Establecimiento de la funcionalidad de jSliderGuessingMin.
		jSliderGuessingMin.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jSliderGuessingMax.getValue() < jSliderGuessingMin.getValue()) {
					int tempInt = jSliderGuessingMax.getValue();
					jSliderGuessingMax.setValue(jSliderGuessingMin.getValue());
					jSliderGuessingMin.setValue(tempInt);
				}

				jTextFieldGuessingMin.setText(String.valueOf(jSliderGuessingMin.getValue() / 100.0));
			}
		});

		// Establecimiento de la funcionalidad de jSliderGuessingMax.
		jSliderGuessingMax.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jSliderGuessingMin.getValue() > jSliderGuessingMax.getValue()) {
					int tempInt = jSliderGuessingMin.getValue();
					jSliderGuessingMin.setValue(jSliderGuessingMax.getValue());
					jSliderGuessingMax.setValue(tempInt);
				}

				jTextFieldGuessingMax.setText(String.valueOf(jSliderGuessingMax.getValue() / 100.0));
			}
		});

		// Establecimiento de la funcionalidad de jSliderExhibitionRateMin.
		jSliderExhibitionRateMin.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jSliderExhibitionRateMax.getValue() < jSliderExhibitionRateMin.getValue()) {
					int tempInt = jSliderExhibitionRateMax.getValue();
					jSliderExhibitionRateMax.setValue(jSliderExhibitionRateMin.getValue());
					jSliderExhibitionRateMin.setValue(tempInt);
				}
				jTextFieldExhibitionRateMin.setText(String.valueOf(jSliderExhibitionRateMin.getValue() / 100.0));
			}
		});

		// Establecimiento de la funcionalidad de jSliderExhibitionRateMax.
		jSliderExhibitionRateMax.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jSliderExhibitionRateMin.getValue() > jSliderExhibitionRateMax.getValue()) {
					int tempInt = jSliderExhibitionRateMin.getValue();
					jSliderExhibitionRateMin.setValue(jSliderExhibitionRateMax.getValue());
					jSliderExhibitionRateMax.setValue(tempInt);
				}

				jTextFieldExhibitionRateMax.setText(String.valueOf(jSliderExhibitionRateMax.getValue() / 100.0));
			}
		});

		// Establecimiento de la funcionalidad de jSliderAnswerTimeMin.
		jSliderAnswerTimeMin.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jSliderAnswerTimeMax.getValue() < jSliderAnswerTimeMin.getValue()) {
					int tempInt = jSliderAnswerTimeMax.getValue();
					jSliderAnswerTimeMax.setValue(jSliderAnswerTimeMin.getValue());
					jSliderAnswerTimeMin.setValue(tempInt);
				}
				jTextFieldAnswerTimeMin.setText(String.valueOf(jSliderAnswerTimeMin.getValue()));
			}
		});

		// Establecimiento de la funcionalidad de jSliderDifficultyMax.
		jSliderAnswerTimeMax.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jSliderAnswerTimeMin.getValue() > jSliderAnswerTimeMax.getValue()) {
					int tempInt = jSliderAnswerTimeMin.getValue();
					jSliderAnswerTimeMin.setValue(jSliderAnswerTimeMax.getValue());
					jSliderAnswerTimeMax.setValue(tempInt);
				}

				jTextFieldAnswerTimeMax.setText(String.valueOf(jSliderAnswerTimeMax.getValue()));
			}
		});

		// Establecimiento de la funcionalidad de jSliderSuccessRateMin.
		jSliderSuccessRateMin.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jSliderSuccessRateMax.getValue() < jSliderSuccessRateMin.getValue()) {
					int tempInt = jSliderSuccessRateMax.getValue();
					jSliderSuccessRateMax.setValue(jSliderSuccessRateMin.getValue());
					jSliderSuccessRateMin.setValue(tempInt);
				}
				jTextFieldSuccessRateMin.setText(String.valueOf(jSliderSuccessRateMin.getValue() / 100.0));
			}
		});

		// Establecimiento de la funcionalidad de jSliderSuccessRateMax.
		jSliderSuccessRateMax.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jSliderSuccessRateMin.getValue() > jSliderSuccessRateMax.getValue()) {
					int tempInt = jSliderSuccessRateMin.getValue();
					jSliderSuccessRateMin.setValue(jSliderSuccessRateMax.getValue());
					jSliderSuccessRateMax.setValue(tempInt);
				}

				jTextFieldSuccessRateMax.setText(String.valueOf(jSliderSuccessRateMax.getValue() / 100.0));
			}
		});

		// Establecimiento de la funcionalidad de jSliderNumberOfAnswersMin.
		jSliderNumberOfAnswersMin.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jSliderNumberOfAnswersMax.getValue() < jSliderNumberOfAnswersMin.getValue()) {
					int tempInt = jSliderNumberOfAnswersMax.getValue();
					jSliderNumberOfAnswersMax.setValue(jSliderNumberOfAnswersMin.getValue());
					jSliderNumberOfAnswersMin.setValue(tempInt);
				}
				jTextFieldNumberOfAnswersMin.setText(String.valueOf(jSliderNumberOfAnswersMin.getValue()));
			}
		});

		// Establecimiento de la funcionalidad de jSliderNumberOfAnswersMax.
		jSliderNumberOfAnswersMax.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jSliderNumberOfAnswersMin.getValue() > jSliderNumberOfAnswersMax.getValue()) {
					int tempInt = jSliderNumberOfAnswersMin.getValue();
					jSliderNumberOfAnswersMin.setValue(jSliderNumberOfAnswersMax.getValue());
					jSliderNumberOfAnswersMax.setValue(tempInt);
				}

				jTextFieldNumberOfAnswersMax.setText(String.valueOf(jSliderNumberOfAnswersMax.getValue()));
			}
		});
	}

	/*
	 * NOMBRE: JRadioButtonInitStep3C. PERTENECE A: Clase FrmCreateTest. LLAMADA
	 * POR: El m�todo Step3C(). LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JRadioButton que se usan para indicar si las
	 * preguntas a buscar tienen o no imagen asociada.
	 */

	private void JRadioButtonInitStep3C() {
		// Reserva de memoria para los JRadioButton.
		jRadioButtonImageYes = new JRadioButton("YES");
		jRadioButtonImageNo = new JRadioButton("NO");
		jRadioButtonImageIndifferent = new JRadioButton("Indifferent");

		// Reserva de memoria para el jRadioButtonGroupImage.
		jRadioButtonImageGroup = new ButtonGroup();

		// Adici�n de los JRadioButton al ButtonGroup, lo cual permite que no se
		// puedan seleccionar m�s de uno JRadioButton al mismo tiempo.
		jRadioButtonImageGroup.add(jRadioButtonImageYes);
		jRadioButtonImageGroup.add(jRadioButtonImageNo);
		jRadioButtonImageGroup.add(jRadioButtonImageIndifferent);

		// Establecimiento de la selecci�n por defecto.
		jRadioButtonImageIndifferent.setSelected(true);
	}

	/*
	 * NOMBRE: JCheckBoxInitStep3C. PERTENECE A: Clase FrmCreateTest. LLAMADA
	 * POR: El m�todo Step3C(). LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JCheckBox que se usan para indicar si las
	 * preguntas a buscar han sido usada en otros test o no.
	 */

	private void JCheckBoxInitStep3C() {
		// Reserva de memoria para los JRadioButton.
		jCheckBoxQuestionsInOtherClassicTest = new JCheckBox(
				"SELECT questions that are NOT IN any CLASSIC TEST.");
		jCheckBoxQuestionsInOtherAdaptiveTest = new JCheckBox(
				"SELECT questions that are NOT IN any ADAPTIVE TEST.");

		jCheckBoxQuestionsInOtherClassicTest.setFont(new java.awt.Font("Dialog", 1, 12));
		jCheckBoxQuestionsInOtherAdaptiveTest.setFont(new java.awt.Font("Dialog", 1, 12));
	}

	/*
	 * NOMBRE: JPanelInitStep3C. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * StepCInit(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JPanel del frame, reservando memoria para el
	 * mismo, estableciendo su color, layout...
	 */

	private void JPanelInitStep3C() {
		// Reserva de memoria para el panel principal del frame y
		// establecimiento de su layout.
		jPanelContent = new JPanel();
		boxLayoutPanelContent = new BoxLayout(jPanelContent, BoxLayout.Y_AXIS);
		jPanelContent.setLayout(boxLayoutPanelContent);
		jPanelContent.setAlignmentX(CENTER_ALIGNMENT);

		// Reserva de memoria para el jScrollPane, el JScrollPane principal del
		// frame.
		jScrollPane = new JScrollPane(jPanelContent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Reserva de memoria para el JPanel que contendr� al jLabelInstructions.
		jPanelInstructions = new JPanel();
		flowLayoutPanelInstructions = new FlowLayout(FlowLayout.CENTER);
		jPanelInstructions.setLayout(flowLayoutPanelInstructions);
		jPanelInstructions.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jPanelInstructions.add(jLabelInstructions);

		// Reserva de memoria para el jPanelDifficultyMax.
		jPanelDifficultyMax = new JPanel();
		flowLayoutPanelDifficultyMax = new FlowLayout(FlowLayout.CENTER);
		jPanelDifficultyMax.setLayout(flowLayoutPanelDifficultyMax);

		jPanelDifficultyMax.add(jLabelDifficultyMax);
		jPanelDifficultyMax.add(jSliderDifficultyMax);
		jPanelDifficultyMax.add(jTextFieldDifficultyMax);

		// Reserva de memoria para el jPanelDifficultyMim.
		jPanelDifficultyMin = new JPanel();
		flowLayoutPanelDifficultyMin = new FlowLayout(FlowLayout.CENTER);
		jPanelDifficultyMin.setLayout(flowLayoutPanelDifficultyMin);

		jPanelDifficultyMin.add(jLabelDifficultyMin);
		jPanelDifficultyMin.add(jSliderDifficultyMin);
		jPanelDifficultyMin.add(jTextFieldDifficultyMin);

		// Reserva de memoria para jPanelDifficulty.
		jPanelDifficulty = new JPanel();
		boxLayoutPanelDifficulty = new BoxLayout(jPanelDifficulty, BoxLayout.Y_AXIS);
		jPanelDifficulty.setLayout(boxLayoutPanelDifficulty);
		jPanelDifficulty.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Difficulty", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelDifficulty.add(jPanelDifficultyMin);
		jPanelDifficulty.add(jPanelDifficultyMax);

		// Reserva de memoria para jPanelDiscriminationMax.
		jPanelDiscriminationMax = new JPanel();
		flowLayoutPanelDiscriminationMax = new FlowLayout(FlowLayout.CENTER);
		jPanelDiscriminationMax.setLayout(flowLayoutPanelDiscriminationMax);

		jPanelDiscriminationMax.add(jLabelDiscriminationMax);
		jPanelDiscriminationMax.add(jSliderDiscriminationMax);
		jPanelDiscriminationMax.add(jTextFieldDiscriminationMax);

		// Reserva de memoria para jPanelDiscriminationMin.
		jPanelDiscriminationMin = new JPanel();
		flowLayoutPanelDiscriminationMin = new FlowLayout(FlowLayout.CENTER);
		jPanelDiscriminationMin.setLayout(flowLayoutPanelDiscriminationMin);

		jPanelDiscriminationMin.add(jLabelDiscriminationMin);
		jPanelDiscriminationMin.add(jSliderDiscriminationMin);
		jPanelDiscriminationMin.add(jTextFieldDiscriminationMin);

		// Reserva de memoria para jPanelDiscrimination.
		jPanelDiscrimination = new JPanel();
		boxLayoutPanelDiscrimination = new BoxLayout(jPanelDiscrimination, BoxLayout.Y_AXIS);
		jPanelDiscrimination.setLayout(boxLayoutPanelDiscrimination);
		jPanelDiscrimination.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Discrimination", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelDiscrimination.add(jPanelDiscriminationMin);
		jPanelDiscrimination.add(jPanelDiscriminationMax);

		// Reserva de memoria para el jPanelGuessingMax.
		jPanelGuessingMax = new JPanel();
		flowLayoutPanelGuessingMax = new FlowLayout(FlowLayout.CENTER);
		jPanelGuessingMax.setLayout(flowLayoutPanelGuessingMax);

		jPanelGuessingMax.add(jLabelGuessingMax);
		jPanelGuessingMax.add(jSliderGuessingMax);
		jPanelGuessingMax.add(jTextFieldGuessingMax);

		// Reserva de memoria para el jPanelGuessingMin.
		jPanelGuessingMin = new JPanel();
		flowLayoutPanelGuessingMin = new FlowLayout(FlowLayout.CENTER);
		jPanelGuessingMin.setLayout(flowLayoutPanelGuessingMin);

		jPanelGuessingMin.add(jLabelGuessingMin);
		jPanelGuessingMin.add(jSliderGuessingMin);
		jPanelGuessingMin.add(jTextFieldGuessingMin);

		// Reserva de memoria para jPanelGuessing.
		jPanelGuessing = new JPanel();
		boxLayoutPanelGuessing = new BoxLayout(jPanelGuessing, BoxLayout.Y_AXIS);
		jPanelGuessing.setLayout(boxLayoutPanelGuessing);
		jPanelGuessing.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Guessing", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelGuessing.add(jPanelGuessingMin);
		jPanelGuessing.add(jPanelGuessingMax);

		// Reserva de memoria para el jPanelIrtParameters.
		// establecimiento de su layout.
		jPanelIrtParameters = new JPanel();
		boxLayoutPanelIrtParameters = new BoxLayout(jPanelIrtParameters, BoxLayout.Y_AXIS);
		jPanelIrtParameters.setLayout(boxLayoutPanelIrtParameters);
		jPanelIrtParameters.setAlignmentX(CENTER_ALIGNMENT);

		jPanelIrtParameters.add(jPanelDifficulty);
		jPanelIrtParameters.add(Box.createVerticalGlue());
		jPanelIrtParameters.add(jPanelDiscrimination);
		jPanelIrtParameters.add(Box.createVerticalGlue());
		jPanelIrtParameters.add(jPanelGuessing);

		// Reserva de memoria para el jPanelExhibitionRateMax.
		jPanelExhibitionRateMax = new JPanel();
		flowLayoutPanelExhibitionRateMax = new FlowLayout(FlowLayout.CENTER);
		jPanelExhibitionRateMax.setLayout(flowLayoutPanelExhibitionRateMax);

		jPanelExhibitionRateMax.add(jLabelExhibitionRateMax);
		jPanelExhibitionRateMax.add(jSliderExhibitionRateMax);
		jPanelExhibitionRateMax.add(jTextFieldExhibitionRateMax);

		// Reserva de memoria para el jPanelExhibitionRateMim.
		jPanelExhibitionRateMin = new JPanel();
		flowLayoutPanelExhibitionRateMin = new FlowLayout(FlowLayout.CENTER);
		jPanelExhibitionRateMin.setLayout(flowLayoutPanelExhibitionRateMin);

		jPanelExhibitionRateMin.add(jLabelExhibitionRateMin);
		jPanelExhibitionRateMin.add(jSliderExhibitionRateMin);
		jPanelExhibitionRateMin.add(jTextFieldExhibitionRateMin);

		// Reserva de memoria para jPanelExhibitionRate.
		jPanelExhibitionRate = new JPanel();
		boxLayoutPanelExhibitionRate = new BoxLayout(jPanelExhibitionRate, BoxLayout.Y_AXIS);
		jPanelExhibitionRate.setLayout(boxLayoutPanelExhibitionRate);
		jPanelExhibitionRate.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Exhibition Rate", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelExhibitionRate.add(jPanelExhibitionRateMin);
		jPanelExhibitionRate.add(jPanelExhibitionRateMax);

		// Reserva de memoria para jPanelAnswerTimeMax.
		jPanelAnswerTimeMax = new JPanel();
		flowLayoutPanelAnswerTimeMax = new FlowLayout(FlowLayout.CENTER);
		jPanelAnswerTimeMax.setLayout(flowLayoutPanelAnswerTimeMax);

		jPanelAnswerTimeMax.add(jLabelAnswerTimeMax);
		jPanelAnswerTimeMax.add(jSliderAnswerTimeMax);
		jPanelAnswerTimeMax.add(jTextFieldAnswerTimeMax);
		jPanelAnswerTimeMax.add(jLabelAnswerTimeMaxSeconds);

		// Reserva de memoria para jPanelDiscriminationMin.
		jPanelAnswerTimeMin = new JPanel();
		flowLayoutPanelAnswerTimeMin = new FlowLayout(FlowLayout.CENTER);
		jPanelAnswerTimeMin.setLayout(flowLayoutPanelAnswerTimeMin);

		jPanelAnswerTimeMin.add(jLabelAnswerTimeMin);
		jPanelAnswerTimeMin.add(jSliderAnswerTimeMin);
		jPanelAnswerTimeMin.add(jTextFieldAnswerTimeMin);
		jPanelAnswerTimeMin.add(jLabelAnswerTimeMinSeconds);

		// Reserva de memoria para jPanelDiscrimination.
		jPanelAnswerTime = new JPanel();
		boxLayoutPanelAnswerTime = new BoxLayout(jPanelAnswerTime, BoxLayout.Y_AXIS);
		jPanelAnswerTime.setLayout(boxLayoutPanelAnswerTime);
		jPanelAnswerTime.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Answer Time", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelAnswerTime.add(jPanelAnswerTimeMin);
		jPanelAnswerTime.add(jPanelAnswerTimeMax);

		// Reserva de memoria para el jPanelSuccessRateMax.
		jPanelSuccessRateMax = new JPanel();
		flowLayoutPanelSuccessRateMax = new FlowLayout(FlowLayout.CENTER);
		jPanelSuccessRateMax.setLayout(flowLayoutPanelSuccessRateMax);

		jPanelSuccessRateMax.add(jLabelSuccessRateMax);
		jPanelSuccessRateMax.add(jSliderSuccessRateMax);
		jPanelSuccessRateMax.add(jTextFieldSuccessRateMax);

		// Reserva de memoria para el jPanelSuccessRateMin.
		jPanelSuccessRateMin = new JPanel();
		flowLayoutPanelSuccessRateMin = new FlowLayout(FlowLayout.CENTER);
		jPanelSuccessRateMin.setLayout(flowLayoutPanelSuccessRateMin);

		jPanelSuccessRateMin.add(jLabelSuccessRateMin);
		jPanelSuccessRateMin.add(jSliderSuccessRateMin);
		jPanelSuccessRateMin.add(jTextFieldSuccessRateMin);

		// Reserva de memoria para jPanelSuccessRate.
		jPanelSuccessRate = new JPanel();
		boxLayoutPanelSuccessRate = new BoxLayout(jPanelSuccessRate, BoxLayout.Y_AXIS);
		jPanelSuccessRate.setLayout(boxLayoutPanelSuccessRate);
		jPanelSuccessRate.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Success Rate", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelSuccessRate.add(jPanelSuccessRateMin);
		jPanelSuccessRate.add(jPanelSuccessRateMax);

		// Reserva de memoria para el jPanelStatisticsParameters.
		// establecimiento de su layout.
		jPanelStatisticParameters = new JPanel();
		boxLayoutPanelStatisticParameters = new BoxLayout(jPanelStatisticParameters, BoxLayout.Y_AXIS);
		jPanelStatisticParameters.setLayout(boxLayoutPanelStatisticParameters);
		jPanelStatisticParameters.setAlignmentX(CENTER_ALIGNMENT);

		jPanelStatisticParameters.add(jPanelExhibitionRate);
		jPanelStatisticParameters.add(Box.createVerticalGlue());
		jPanelStatisticParameters.add(jPanelAnswerTime);
		jPanelStatisticParameters.add(Box.createVerticalGlue());
		jPanelStatisticParameters.add(jPanelSuccessRate);

		// Reserva de memoria para el jPanelNumberOfAnswersMax.
		jPanelNumberOfAnswersMax = new JPanel();
		flowLayoutPanelNumberOfAnswersMax = new FlowLayout(FlowLayout.CENTER);
		jPanelNumberOfAnswersMax.setLayout(flowLayoutPanelNumberOfAnswersMax);

		jPanelNumberOfAnswersMax.add(jLabelNumberOfAnswersMax);
		jPanelNumberOfAnswersMax.add(jSliderNumberOfAnswersMax);
		jPanelNumberOfAnswersMax.add(jTextFieldNumberOfAnswersMax);

		// Reserva de memoria para el jPanelNumberOfAnswersMin.
		jPanelNumberOfAnswersMin = new JPanel();
		flowLayoutPanelNumberOfAnswersMin = new FlowLayout(FlowLayout.CENTER);
		jPanelNumberOfAnswersMin.setLayout(flowLayoutPanelNumberOfAnswersMin);

		jPanelNumberOfAnswersMin.add(jLabelNumberOfAnswersMin);
		jPanelNumberOfAnswersMin.add(jSliderNumberOfAnswersMin);
		jPanelNumberOfAnswersMin.add(jTextFieldNumberOfAnswersMin);

		// Reserva de memoria para jPanelNumberOfAnswers.
		jPanelNumberOfAnswers = new JPanel();
		boxLayoutPanelNumberOfAnswers = new BoxLayout(jPanelNumberOfAnswers, BoxLayout.Y_AXIS);
		jPanelNumberOfAnswers.setLayout(boxLayoutPanelNumberOfAnswers);
		jPanelNumberOfAnswers.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Number of Answers", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelNumberOfAnswers.add(jPanelNumberOfAnswersMin);
		jPanelNumberOfAnswers.add(jPanelNumberOfAnswersMax);

		// Reserva de memoria y establecimiento de layout para el
		// jPanelQuestionsUsesNumber.
		jPanelCheckBoxQuestionsInOtherTest = new JPanel();
		boxLayoutPanelCheckBoxQuestionsInOtherTest = new BoxLayout(jPanelCheckBoxQuestionsInOtherTest,
				BoxLayout.Y_AXIS);
		jPanelCheckBoxQuestionsInOtherTest.setLayout(boxLayoutPanelCheckBoxQuestionsInOtherTest);
		// Adici�n de los componentes del jPanelCheckBoxQuestionsInOtherTest.
		jPanelCheckBoxQuestionsInOtherTest.add(jCheckBoxQuestionsInOtherClassicTest);
		jPanelCheckBoxQuestionsInOtherTest.add(jCheckBoxQuestionsInOtherAdaptiveTest);

		// Reserva de memoria para el jQuestionsInOtherTest.
		jPanelQuestionsInOtherTest = new JPanel();
		flowLayoutPanelQuestionsInOtherTest = new FlowLayout(FlowLayout.CENTER);
		jPanelQuestionsInOtherTest.setLayout(flowLayoutPanelQuestionsInOtherTest);
		jPanelQuestionsInOtherTest.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Ownership of the questions to other test.",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		// Adici�n de los componentes pertenecientes al
		// jPanelQuestionsInOtherTest.
		jPanelQuestionsInOtherTest.add(jPanelCheckBoxQuestionsInOtherTest);

		// Reserva de memoria para jPanelQuestionsWithImage.
		jPanelQuestionsWithImage = new JPanel();
		flowLayoutPanelQuestionsWithImage = new FlowLayout(FlowLayout.CENTER);
		jPanelQuestionsWithImage.setLayout(flowLayoutPanelQuestionsWithImage);
		jPanelQuestionsWithImage.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Questions with Images", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelQuestionsWithImage.add(jLabelQuestionsWithImage);
		jPanelQuestionsWithImage.add(jRadioButtonImageYes);
		jPanelQuestionsWithImage.add(jRadioButtonImageNo);
		jPanelQuestionsWithImage.add(jRadioButtonImageIndifferent);

		// Reserva de memoria para el jPanelContentParameters.
		// establecimiento de su layout.
		jPanelContentParameters = new JPanel();
		boxLayoutPanelContentParameters = new BoxLayout(jPanelContentParameters, BoxLayout.Y_AXIS);
		jPanelContentParameters.setLayout(boxLayoutPanelContentParameters);
		jPanelContentParameters.setAlignmentX(CENTER_ALIGNMENT);

		jPanelContentParameters.add(jPanelNumberOfAnswers);
		jPanelContentParameters.add(jPanelQuestionsInOtherTest);
		jPanelContentParameters.add(jPanelQuestionsWithImage);

		// Reserva de memoria para el jTabbedPane.
		jTabbedPane = new JTabbedPane();
		if (ImageIconLoad == true) {
			jTabbedPane.addTab("IRT Parameters", iconInfoGreen16, jPanelIrtParameters);
			jTabbedPane.addTab("Statistic Parameters", iconInfoRed16, jPanelStatisticParameters);
			jTabbedPane.addTab("Content Parameters", iconInfoBlue16, jPanelContentParameters);
		} else {
			jTabbedPane.addTab("IRT Parameters", jPanelIrtParameters);
			jTabbedPane.addTab("Statistic Parameters", jPanelStatisticParameters);
			jTabbedPane.addTab("Content Parameters", jPanelContentParameters);
		}

		// Reserva de memoria y establecimento del layout para el JPanel que
		// contendr� a los JButton.
		jPanelButton = new JPanel();
		flowLayoutPanelButton = new FlowLayout(FlowLayout.CENTER);
		jPanelButton.setLayout(flowLayoutPanelButton);

		jPanelButton.add(jButtonPrevious);
		jPanelButton.add(jButtonNext);
		jPanelButton.add(Box.createHorizontalGlue());
		jPanelButton.add(jButtonCancel);
		jPanelButton.add(Box.createHorizontalStrut(20));
		jPanelButton.add(jButtonClear);

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
		jPanelContent.add(jPanelInstructions);
		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jTabbedPane);
		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jPanelButton);
		jPanelContent.add(jPanelStatus);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// ////////////CONFIGURACI�N DE LOS COMPONENTES DEL
	// JINTERNAFRAME////////////////
	// ////////////PARA EL PASO
	// 4A///////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: Step4AInit. PERTENECE A: Clase FrmCreateTest. LLAMADA POR: Al
	 * hacer click sobre jButtonNext cuando el frame est� configurado
	 * seg�n Step3Init(). LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los componentes del frame, necesarios para el cuarto
	 * paso de la configuracion de la bateria de test, reservando memoria para
	 * los mismos y estableciendo su tama�o, su contenido, funcionalidad... Este
	 * m�todo obtiene los datos directamente representados del jTreeTestQuestion,
	 * de modo que si se cambiara la representaci�n de los datos en la interfaza
	 * por usando otro componente distinto al jTreeTestQuestion, este m�todo
	 * deberia de ser cambiado.
	 */

	private void Step4AInit() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Obtenci�n del nodo ra�z de jTreeTestQuestion.
		TreePath treePath = jTreeTestQuestion.getPathForRow(0);
		DefaultMutableTreeNode rootNodeTestQuestion = (DefaultMutableTreeNode) treePath.getPathComponent(0);

		// Establecimiento del m�nimo y m�ximo para el jProgressBar.
		jProgressBar.setMinimum(0);
		jProgressBar.setMaximum(rootNodeTestQuestion.getChildCount());

		jLabelStatus.setText("Extracting questions codes of file... ");
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		// Obtenci�n de los c�digos de las preguntas de cada fichero de preguntas
		// con los que se formar� el test.

		// Reserva de memoria para el objeto testVector.
		testVector = new Vector();
		conceptListFinal = new Vector();
		contQuestionsTest = 0;

		// Bucle para la obtenci�n de los c�digos de las preguntas y de los
		// conceptos
		// asociados.
		for (int i = 0; i < rootNodeTestQuestion.getChildCount(); i++) {
			QuestionsFileTest questionsFileTest = new QuestionsFileTest();

			// Obtenci�n de un nodo que representa a un fichero de preguntas.
			DefaultMutableTreeNode questionsFileNode = (DefaultMutableTreeNode) rootNodeTestQuestion
					.getChildAt(i);

			DefaultMutableTreeNode conceptNode = (DefaultMutableTreeNode) questionsFileNode.getFirstChild();

			// Redibujado de la jProgressBar y de jLabelStatus
			jLabelStatus.setText("Extracting questions codes of file... " + questionsFileNode.toString());
			jProgressBar.setValue(i);
			jPanelStatus.paint(this.jPanelStatus.getGraphics());
			this.paint(this.getGraphics());
			parent.paint(parent.getGraphics());

			// Adici�n del nombre del fichero de preguntas al questionsFileVector.
			questionsFileTest.setQuestionsFileName(questionsFileNode.toString());

			// Adici�n del concepto relacionado con el fichero de preguntas.
			questionsFileTest.setConcept(conceptNode.toString().substring(
					conceptNode.toString().indexOf(": ") + 1));

			// Reserva de memoria para el vector que contendr� los codigos de las
			// preguntas de un fichero.
			Vector codeQuestionVector = new Vector();

			for (int j = 0; j < questionsFileNode.getChildCount(); j++) {
				if (questionsFileNode.getChildAt(j).getChildCount() > 0) {
					String codeQuestion = questionsFileNode.getChildAt(j).getChildAt(0).toString();
					codeQuestion = codeQuestion.substring(codeQuestion.indexOf(": ") + 1);

					// Adicion del c�digo de la pregunta al vector
					// codeQuestionVector.
					codeQuestionVector.add(codeQuestion);
					contQuestionsTest++;
				} else {
					String conceptName = questionsFileNode.getChildAt(j).toString();
					conceptName = conceptName.substring(conceptName.indexOf(": ") + 1);
					if (conceptListFinal.contains(conceptName.trim()) == false) {
						conceptListFinal.add(conceptName.trim());
					}
				}
			}
			questionsFileTest.setCodeQuestions(codeQuestionVector);

			// Adici�n del questionsFileVector al testVector.
			testVector.add(questionsFileTest);
		}

		if (conceptListFinal.size() < course.getConceptNames().size()) {
			JOptionPane.showMessageDialog(this, "You have not added questions for some concepts. " + "\n"
					+ "These concepts won't be evaluated.", "Info", JOptionPane.WARNING_MESSAGE);
		}

		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		jLabelStatus.setText("Extracting questions codes of files... Finished");
		jProgressBar.setValue(jProgressBar.getMaximum());

		// Reinicializaci�n del aspecto del frame.
		JLabelInitStep4A();
		JRadioButtonInitStep4A();
		JComboBoxInitStep4A();
		JCheckBoxInitStep4A();
		JTextFieldInitStep4A();
		JSliderInitStep4A();
		JEditorPaneInitStep4A();
		JButtonInitStep4A();
		JProgressBarInit();
		JPanelInitStep4A();

		// Visualiaci�n o no de los jSliderTimeToAnswer y del
		// jTextFieldTimeToAnswer.
		if (jCheckBoxTimeToAnswer.isSelected()) {
			jSliderTimeToAnswer.setVisible(true);
			jTextFieldTimeToAnswer.setVisible(true);
			jLabelTimeToAnswerInfo.setVisible(true);
		} else {
			jSliderTimeToAnswer.setVisible(false);
			jTextFieldTimeToAnswer.setVisible(false);
			jLabelTimeToAnswerInfo.setVisible(false);
		}

		// Visualiaci�n o no de los jPanelIncorrectAnswersPenalize y del
		// jPanelWithoutAnswersPenalize.
		if (jRadioButtonIncorrectAnswersPenalizeYes.isSelected() == false)
			jPanelIncorrectAnswersPenalize.setVisible(false);
		else
			jPanelIncorrectAnswersPenalize.setVisible(true);

		if (jRadioButtonWithoutAnswersPenalizeYes.isSelected() == false)
			jPanelWithoutAnswersPenalize.setVisible(false);
		else
			jPanelWithoutAnswersPenalize.setVisible(true);

		step = "4A";
		try {
			frameInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Default cursor
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: JLabelInitStep4A. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step4Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JLabel del frame, reservando memoria para los
	 * mismos y estableciendo su color, su contenido...
	 */

	private void JLabelInitStep4A() {
		// Reserva de memoria y establecimiento del contenido para los JLabel.
		jLabelInstructions = new JLabel("Select the additional parameters " + "for the test configuration.");
		jLabelInstructions.setFont(new java.awt.Font("Dialog", 1, 20));

		jLabelStatus = new JLabel("Done                                         "
				+ "                                             " + "                    ");
		jLabelStatus.setFont(new java.awt.Font("Dialog", 0, 12));

		jLabelShowInitialInfo = new JLabel("It indicates if the test presentation will be shown.");
		jLabelShowInitialInfo.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelQuestionsOrder = new JLabel(" Order of the questions: ");
		jLabelQuestionsOrder.setFont(new java.awt.Font("Dialog", 1, 18));

		jLabelQuestionsOrderInfo1 = new JLabel(" It indicates the way in which ");
		jLabelQuestionsOrderInfo1.setFont(new java.awt.Font("Dialog", 1, 12));
		jLabelQuestionsOrderInfo2 = new JLabel(" the questions will be shown in the test. ");
		jLabelQuestionsOrderInfo2.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelAnswersOrder = new JLabel(" Order of the answers: ");
		jLabelAnswersOrder.setFont(new java.awt.Font("Dialog", 1, 18));

		jLabelAnswersOrderInfo1 = new JLabel(" It indicates the way in which ");
		jLabelAnswersOrderInfo1.setFont(new java.awt.Font("Dialog", 1, 12));
		jLabelAnswersOrderInfo2 = new JLabel(" the answers will be shown in the test. ");
		jLabelAnswersOrderInfo2.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelShowQuestionCorrection = new JLabel(
				" It indicates if the evaluation of the answered question will be shown. ");
		jLabelShowQuestionCorrection.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelVerbose = new JLabel(" It indicates if the explanation of the answers will be "
				+ "shown when they are corrected. ");
		jLabelVerbose.setFont(new java.awt.Font(" Dialog", 1, 12));

		jLabelShowCorrectAnswers = new JLabel(" It indicates if the correct answers will be "
				+ "shown when the question is corrected. ");
		jLabelShowCorrectAnswers.setFont(new java.awt.Font(" Dialog", 1, 12));

		jLabelTimeToAnswerInfo = new JLabel(" (Seconds) ");
		jLabelTimeToAnswerInfo.setFont(new java.awt.Font("Dialog", 1, 14));

		jLabelRepeatWithoutAnswerInfo = new JLabel(
				"It indicates if the questions without being answered show again, "
						+ "to the student, before concluding the test.");
		jLabelRepeatWithoutAnswerInfo.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelShowFinalInfo = new JLabel(
				"It indicates if the test information will be shown when concluding it.");
		jLabelShowFinalInfo.setFont(new java.awt.Font("Dialog", 1, 12));

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

		jLabelTestName = new JLabel("Test Title     ");
		jLabelTestName.setFont(new java.awt.Font("Dialog", 1, 14));

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
	 * NOMBRE: JRadioButtonInitStep4A. PERTENECE A: Clase FrmCreateTest. LLAMADA
	 * POR: Step4Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JRadioButton del frame.
	 */

	private void JRadioButtonInitStep4A() {
		// Reserva de memoria e inicializaci�n de los JRadioButton.

		jRadioButtonQuestionsOrderRandom = new JRadioButton("Random", false);
		jRadioButtonQuestionsOrderRandom.setFont(new java.awt.Font("Dialog", 1, 14));
		jRadioButtonQuestionsOrderRandom.setBackground(SystemColor.LIGHT_GRAY);

		jRadioButtonQuestionsOrderSequential = new JRadioButton("Sequential", true);
		jRadioButtonQuestionsOrderSequential.setFont(new java.awt.Font("Dialog", 1, 14));
		jRadioButtonQuestionsOrderSequential.setBackground(SystemColor.LIGHT_GRAY);

		jRadioButtonAnswersOrderRandom = new JRadioButton("Random", false);
		jRadioButtonAnswersOrderRandom.setFont(new java.awt.Font("Dialog", 1, 14));
		jRadioButtonAnswersOrderRandom.setBackground(SystemColor.LIGHT_GRAY);

		jRadioButtonAnswersOrderSequential = new JRadioButton("Sequential", true);
		jRadioButtonAnswersOrderSequential.setFont(new java.awt.Font("Dialog", 1, 14));
		jRadioButtonAnswersOrderSequential.setBackground(SystemColor.LIGHT_GRAY);

		jRadioButtonIncorrectAnswersPenalizeYes = new JRadioButton("Penalize", true);
		jRadioButtonIncorrectAnswersPenalizeYes.setFont(new java.awt.Font("Dialog", 1, 14));
		jRadioButtonIncorrectAnswersPenalizeYes.setBackground(SystemColor.WHITE);

		jRadioButtonIncorrectAnswersPenalizeNo = new JRadioButton("Not Penalized", false);
		jRadioButtonIncorrectAnswersPenalizeNo.setFont(new java.awt.Font("Dialog", 1, 14));
		jRadioButtonIncorrectAnswersPenalizeNo.setBackground(SystemColor.WHITE);

		jRadioButtonWithoutAnswersPenalizeYes = new JRadioButton("Penalize", false);
		jRadioButtonWithoutAnswersPenalizeYes.setFont(new java.awt.Font("Dialog", 1, 14));
		jRadioButtonWithoutAnswersPenalizeYes.setBackground(SystemColor.WHITE);

		jRadioButtonWithoutAnswersPenalizeNo = new JRadioButton("Not Penalized", true);
		jRadioButtonWithoutAnswersPenalizeNo.setFont(new java.awt.Font("Dialog", 1, 14));
		jRadioButtonWithoutAnswersPenalizeNo.setBackground(SystemColor.WHITE);

		// Reserva de memoria para los jRadioButton relacionados con la ejecuci�n
		// adaptativa.
		jRadioButton1P = new JRadioButton("1 Parameter");
		jRadioButton1P.setFont(new java.awt.Font("Dialog", 1, 18));
		jRadioButton1P.setBackground(SystemColor.WHITE);

		jRadioButton2P = new JRadioButton("2 Parameters");
		jRadioButton2P.setFont(new java.awt.Font("Dialog", 1, 18));
		jRadioButton2P.setBackground(SystemColor.WHITE);

		jRadioButton3P = new JRadioButton("3 Parameters", true);
		jRadioButton3P.setFont(new java.awt.Font("Dialog", 1, 18));
		jRadioButton3P.setBackground(SystemColor.WHITE);

		jRadioButtonStandardError = new JRadioButton("Standard error", true);
		jRadioButtonStandardError.setFont(new java.awt.Font("Dialog", 1, 18));

		jRadioButtonNumberItems = new JRadioButton("Number of items administred");
		jRadioButtonNumberItems.setFont(new java.awt.Font("Dialog", 1, 18));

		// Establecimiento de la funcionalidad de
		// jRadioButtonIncorrectAnswersPenalizeYes.
		jRadioButtonIncorrectAnswersPenalizeYes.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jRadioButtonIncorrectAnswersPenalizeYes.isSelected())
					jPanelIncorrectAnswersPenalize.setVisible(true);
				else
					jPanelIncorrectAnswersPenalize.setVisible(false);
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
			}
		});

		// Reserva de memoria de lo ButtonGroup y asignaci�n a los mismos de sus
		// JRadioButton correspondientes.

		jRadioButtonQuestionsOrderGroup = new ButtonGroup();
		jRadioButtonQuestionsOrderGroup.add(jRadioButtonQuestionsOrderRandom);
		jRadioButtonQuestionsOrderGroup.add(jRadioButtonQuestionsOrderSequential);

		jRadioButtonAnswersOrderGroup = new ButtonGroup();
		jRadioButtonAnswersOrderGroup.add(jRadioButtonAnswersOrderRandom);
		jRadioButtonAnswersOrderGroup.add(jRadioButtonAnswersOrderSequential);

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
	 * NOMBRE: JComboBoxInitStep4A() PERTENECE A: Clase FrmCreateTest LLAMADA
	 * POR: El m�todo Step4AInit(). LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JComboBox del frame, reservando memoria
	 * para los mismoy estableciendo su tama�o y su funcionalidad.
	 */

	private void JComboBoxInitStep4A() {
		// Reserva de memoria para los JComboBox.
		jComboBoxIncorrectAnswers = new JComboBox();
		jComboBoxWithoutAnswers = new JComboBox();

		// Borrado de sus contenidos previos.
		jComboBoxIncorrectAnswers.removeAllItems();
		jComboBoxWithoutAnswers.removeAllItems();

		// Adici�n a jComboBoxIncorrectAnswer y jComboBoxWithoutAnswers
		// de sus valores.
		for (int i = NUMBER_OF_ANSWERS_MIN; i <= NUMBER_OF_ANSWERS_MAX; i++) {
			jComboBoxIncorrectAnswers.addItem(String.valueOf(i));
			jComboBoxWithoutAnswers.addItem(String.valueOf(i));
		}
	}

	/*
	 * NOMBRE: JCheckBoxInitStep4A(); PERTENECE A: Clase FrmCreateTest LLAMADA
	 * POR: El m�todo Step4AInit(). LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JCheckBox del frame, reservando memoria
	 * para los mismoy estableciendo su tama�o y su funcionalidad.
	 */

	private void JCheckBoxInitStep4A() {
		// Reserva de memoria para el jCheckBoxShowInitialInfo.
		jCheckBoxShowInitialInfo = new JCheckBox("Show test presentation.", true);
		jCheckBoxShowInitialInfo.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxShowInitialInfo.setBackground(SystemColor.WHITE);

		// Reserva de memoria para el jCheckBoxShowQuestionCorrection.
		jCheckBoxShowQuestionCorrection = new JCheckBox("Show question correction.", true);
		jCheckBoxShowQuestionCorrection.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxShowQuestionCorrection.setBackground(SystemColor.WHITE);

		// Establecimiento de la funcionalidad para
		// jCheckBoxShowQuestionCorrection.
		jCheckBoxShowQuestionCorrection.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (jCheckBoxShowQuestionCorrection.isSelected()) {
					jPanelVerbose.setVisible(true);
					jPanelShowCorrectAnswers.setVisible(true);
				} else {
					jPanelVerbose.setVisible(false);
					jPanelShowCorrectAnswers.setVisible(false);
				}
			}
		});

		// Reserva de memoria para el jCheckBoxVerbose.
		jCheckBoxVerbose = new JCheckBox("Verbose.", true);
		jCheckBoxVerbose.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxVerbose.setBackground(SystemColor.WHITE);

		// Reserva de memoria para el jCheckBoxShowCorrectAnswers.
		jCheckBoxShowCorrectAnswers = new JCheckBox("Show Correct Answers.", true);
		jCheckBoxShowCorrectAnswers.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxShowCorrectAnswers.setBackground(SystemColor.WHITE);

		// Reserva de memoria para el jCheckBoxTimeToAnswer.
		jCheckBoxTimeToAnswer = new JCheckBox("Maximum time to respond to the questions", false);
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
			}
		});

		// Reserva de memoria para el jCheckBoxRepeatWithoutAnswer.
		jCheckBoxRepeatWithoutAnswer = new JCheckBox("Repeat questions without answering.", false);
		jCheckBoxRepeatWithoutAnswer.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxRepeatWithoutAnswer.setBackground(SystemColor.WHITE);

		// Reserva de memoria para el jCheckBoxShowFinalInfo.
		jCheckBoxShowFinalInfo = new JCheckBox("Show final information of test.", false);
		jCheckBoxShowFinalInfo.setFont(new java.awt.Font("Dialog", 1, 18));
		jCheckBoxShowFinalInfo.setBackground(SystemColor.WHITE);
	}

	/*
	 * NOMBRE: JTextFieldInitStep4A. PERTENECE A: Clase FrmCreateTest. LLAMADA
	 * POR: Step4AInit(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa el jTextField del frame.
	 */

	private void JTextFieldInitStep4A() {
		// Reserva de memoria para jTextFieldTimeToAnswer.
		jTextFieldTimeToAnswer = new JTextField(String.valueOf(ANSWER_TIME_MIN + 20), 4);

		// Reserva de memoria para jTextFieldKnowledge;
		jTextFieldKnowledge = new JTextField("100", 4);

		// Reserva de memoria para jTextFieldTestName.
		jTextFieldTestName = new JTextField(jTextFieldTestFileName.getText().trim());
		jTextFieldTestName.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextFieldTestName.setFocusable(true);

		// Reserva de memoria para jTextFieldBgColor.
		jTextFieldBgColor = new JTextField("#FFFFFF");
		jTextFieldBgColor.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextFieldBgColor.setFocusable(false);

		// Reserva de memoria para jTextFieldBackground.
		jTextFieldBackground = new JTextField(8);
		jTextFieldBackground.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextFieldBackground.setFocusable(false);

		// Reserva de memoria para jTextFieldTitleColor.
		jTextFieldTitleColor = new JTextField("#000000");
		jTextFieldTitleColor.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextFieldTitleColor.setFocusable(false);

		// Reserva de memoria para el jTextFieldInitialProficiency.
		jTextFieldInitialProficiency = new JTextField(String.valueOf(INITIAL_PROFICIENCY_DEFAULT), 4);
		jTextFieldInitialProficiency.setFont(new java.awt.Font("Dialog", 0, 14));

		// Reserva de memoria para el jTextFieldStandardError.
		jTextFieldStandardError = new JTextField(String.valueOf(STANDARD_ERROR_DEFAULT), 4);
		jTextFieldStandardError.setFont(new java.awt.Font("Dialog", 0, 14));

		// Reserva de memoria para el jTextFieldNumberItems.
		jTextFieldNumberItems = new JTextField(String.valueOf(contQuestionsTest), 4);
		jTextFieldNumberItems.setFont(new java.awt.Font("Dialog", 0, 14));

		// Establecimiento de la funcionalidad de jTextFieldTimeToAnswer.
		jTextFieldTimeToAnswer.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldTimeToAnswer.getText().length() > 0) {
						if (Integer.valueOf(jTextFieldTimeToAnswer.getText()).intValue() < ANSWER_TIME_MIN + 20) {
							jTextFieldTimeToAnswer.setText(String.valueOf(ANSWER_TIME_MIN + 20));
							jSliderTimeToAnswer.setValue(ANSWER_TIME_MIN);
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
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldTimeToAnswer.setText(String.valueOf(ANSWER_TIME_MIN + 20));
					jSliderTimeToAnswer.setValue(ANSWER_TIME_MIN + 20);
				}
			}
		});

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
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldKnowledge.setText("100");
					jSliderKnowledge.setValue(100);
				}
			}
		});

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
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldNumberItems.
		jTextFieldNumberItems.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldNumberItems.getText().length() > 0) {
						if (Integer.valueOf(jTextFieldNumberItems.getText()).intValue() < 1) {
							jTextFieldNumberItems.setText(String.valueOf(1));
							jSliderNumberItems.setValue(1);
						} else {
							if (Integer.valueOf(jTextFieldNumberItems.getText()).intValue() > contQuestionsTest) {
								jTextFieldNumberItems.setText(String.valueOf(contQuestionsTest));
								jSliderNumberItems.setValue(contQuestionsTest);
							} else {
								jSliderNumberItems.setValue(Integer.valueOf(jTextFieldNumberItems.getText())
										.intValue());
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					jTextFieldNumberItems.setText(String.valueOf(contQuestionsTest));
					jSliderNumberItems.setValue(contQuestionsTest);
				}
			}
		});
	}

	/*
	 * NOMBRE: JEditorPaneInitStep4A. PERTENECE A: Clase FrmCreateTest. LLAMADA
	 * POR: Step4AInit(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JEditorPane del frame.
	 */

	private void JEditorPaneInitStep4A() {
		// Reserva de memoria para jEditorPaneHtmlParameters.
		jEditorPaneHtmlParameters = new JEditorPane();
		jEditorPaneHtmlParameters.setSize(500, 400);
		jEditorPaneHtmlParameters.setContentType("text/html;charset=UTF-8");
		jEditorPaneHtmlParameters.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
	}

	/*
	 * NOMBRE: getEditorPaneHtmlParametersText(). PERTENECE A: Clase
	 * FrmCreateTest. LLAMADA POR: JEditorPaneInitStep4A() y al hacer click sobre
	 * jButtonSetBgColor, jButtonResetBgColor, jButtonSetBackground,
	 * jButtonSetBackground, jButtonSetTitleColor, jButtonResetTitleColor;
	 * jButtonResetAllHtmlParameters. LLAMA A: nada. RECIBE: void. DEVUELVE:
	 * void. FUNCI�N: Establece el texto del jEditorPaneHtmlParameters.
	 */

	private String getEditorPaneHtmlParametersText() {
		String editorPaneHtmlParametersText = "<html>\n" + "<head>\n" + "</head>\n" + "<body bgcolor=\""
				+ jTextFieldBgColor.getText().trim() + "\" leftmargin=\"0\" topmargin=\"0\" "
				+ "marginwidth=\"0\" marginheight=\"0\" ";

		if (backgroundFile != null)
			editorPaneHtmlParametersText = editorPaneHtmlParametersText.concat("background=\"file:///"
					+ backgroundFile.getAbsolutePath() + "\">\n");
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
		} catch (java.net.MalformedURLException mue) {
			mue.printStackTrace();
		}

		editorPaneHtmlParametersText = editorPaneHtmlParametersText.concat("</center>\n" + "</td>\n"
				+ "</tr>\n" + "</table>\n" + "</center>\n" + "</body>\n" + "</html>\n");

		return editorPaneHtmlParametersText;
	}

	/*
	 * NOMBRE: JSliderInitStep4A. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step4AInit(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JSlider del frame.
	 */

	private void JSliderInitStep4A() {
		// Reserva de memoria para el jSliderTimeToAnswer.
		jSliderTimeToAnswer = new JSlider(JSlider.HORIZONTAL, ANSWER_TIME_MIN + 20, ANSWER_TIME_MAX,
				ANSWER_TIME_MIN + 20);
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

		// Reserva de memoria para el jSliderKnowledge.
		jSliderKnowledge = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
		jSliderKnowledge.setPaintTicks(true);
		jSliderKnowledge.setPaintLabels(true);
		jSliderKnowledge.setMajorTickSpacing(10);
		jSliderKnowledge.setMinorTickSpacing(1);

		jSliderKnowledge.setBackground(SystemColor.WHITE);

		// Bucle para el establecimiento de las etiquetas del jSliderKnowledge.
		Dictionary jSliderKnowledgeLabelTable = new Hashtable();
		for (int i = 0; i <= 100; i++) {
			if (i % 10 == 0)
				jSliderKnowledgeLabelTable.put(new Integer(i), new JLabel(String.valueOf(i)));
		}
		jSliderKnowledge.setLabelTable(jSliderKnowledgeLabelTable);

		// Reserva de memoria para el jSliderInitialProficiency.
		jSliderInitialProficiency = new JSlider(JSlider.HORIZONTAL, (INITIAL_PROFICIENCY_MIN * 10),
				(INITIAL_PROFICIENCY_MAX * 10), (int) (INITIAL_PROFICIENCY_DEFAULT * 10));
		jSliderInitialProficiency.setPaintTicks(true);
		jSliderInitialProficiency.setPaintLabels(true);
		jSliderInitialProficiency.setMajorTickSpacing(10);
		jSliderInitialProficiency.setMinorTickSpacing(1);
		jSliderInitialProficiency.setBackground(SystemColor.WHITE);

		// Bucle para el establecimiento de las etiquetas del
		// jSliderInitialProficiency.
		Dictionary jSliderInitialProficiencyLabelTable = new Hashtable();
		for (int i = (INITIAL_PROFICIENCY_MIN * 10); i <= (INITIAL_PROFICIENCY_MAX * 10); i++) {
			if (i % 10 == 0)
				jSliderInitialProficiencyLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 10)));
		}

		jSliderInitialProficiency.setLabelTable(jSliderInitialProficiencyLabelTable);

		// Reserva de memoria para el jSliderStandardError.
		jSliderStandardError = new JSlider(JSlider.HORIZONTAL, (STANDARD_ERROR_MIN * 100),
				(STANDARD_ERROR_MAX * 100), (int) (STANDARD_ERROR_DEFAULT * 100));
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

		// Reserva de memoria para el jSliderNumberItems.
		jSliderNumberItems = new JSlider(JSlider.HORIZONTAL, 1, contQuestionsTest,
				contQuestionsTest);

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

		for (int i = 1; i <= (contQuestionsTest); i++) {
			if (contQuestionsTest <= 10)
				jSliderNumberItemsLabelTable.put(new Integer(i), new JLabel(String.valueOf(i)));
			else {
				if (contQuestionsTest <= 100) {
					if (i % 10 == 0 || i == 1 || i == (contQuestionsTest))
						jSliderNumberItemsLabelTable.put(new Integer(i), new JLabel(String.valueOf(i)));
				} else {
					if (i % 50 == 0 || i == 1 || i == (contQuestionsTest))
						jSliderNumberItemsLabelTable.put(new Integer(i), new JLabel(String.valueOf(i)));

				}
			}
		}

		if (jSliderNumberItemsLabelTable != null && jSliderNumberItemsLabelTable.isEmpty() == false)
			jSliderNumberItems.setLabelTable(jSliderNumberItemsLabelTable);

		// Establecimiento de la funcionalidad de jSliderTimeToAnswer.
		jSliderTimeToAnswer.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jTextFieldTimeToAnswer.setText(String.valueOf(jSliderTimeToAnswer.getValue()));
			}
		});

		// Establecimiento de la funcionalidad de jSliderKnowledge.
		jSliderKnowledge.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jTextFieldKnowledge.setText(String.valueOf(jSliderKnowledge.getValue()));
			}
		});

		// Establecimiento de la funcionalidad de jSliderInitialProficiency.
		jSliderInitialProficiency.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jTextFieldInitialProficiency.setText(String.valueOf(jSliderInitialProficiency.getValue() / 10.0));
			}
		});

		// Establecimiento de la funcionalidad de jSliderStandardError.
		jSliderStandardError.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jTextFieldStandardError.setText(String.valueOf(jSliderStandardError.getValue() / 100.0));
			}
		});

		// Establecimiento de la funcionalidad de jSliderNumberItems.
		jSliderNumberItems.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jTextFieldNumberItems.setText(String.valueOf(jSliderNumberItems.getValue()));
			}
		});
	}

	/*
	 * NOMBRE: JButtonInitStep4A. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step4Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JButton del frame.
	 */

	private void JButtonInitStep4A() {
		jButtonCancel = null;
		// Reserva de memoria para los JButton, dependiendo de si los
		// iconos del JInteralFrame se han cargado con �xito o no.
		if (ImageIconLoad == true) {
			jButtonSetTitle = new JButton("Set title", iconSetTitle32);
			jButtonSetBgColor = new JButton("Set bgColor", iconSetBgColor32);
			jButtonSetTitleColor = new JButton("Set title color", iconSetTitleColor32);
			jButtonSetBackground = new JButton("Set background", iconAddImage32);
			jButtonResetTitle = new JButton("Reset title", iconResetTitle32);
			jButtonResetBgColor = new JButton("Reset bgColor", iconResetBgColor32);
			jButtonResetBackground = new JButton("Reset background", iconDeleteImage32);
			jButtonResetTitleColor = new JButton("Reset title color", iconResetTitleColor32);
			jButtonResetAll = new JButton("Reset All", iconClear32);
			jButtonClear = new JButton("Clear", iconClear32);
			jButtonPrevious = new JButton("Previous", iconPrevious32);
			jButtonNext = new JButton("Finish", iconNext32);
			jButtonCancel = new JButton("Cancel", iconCancel32);
		} else {
			jButtonSetTitle = new JButton("Set title");
			jButtonSetBgColor = new JButton("Set bgColor");
			jButtonSetTitleColor = new JButton("Set title color");
			jButtonSetBackground = new JButton("Set background");
			jButtonResetTitle = new JButton("Reset title");
			jButtonResetBgColor = new JButton("Reset bgColor");
			jButtonResetBackground = new JButton("Reset background");
			jButtonResetTitleColor = new JButton("Reset title color");
			jButtonResetAll = new JButton("Reset All");
			jButtonClear = new JButton("Clear");
			jButtonPrevious = new JButton("Previous");
			jButtonNext = new JButton("Finish");
			jButtonCancel = new JButton("Cancel");
		}

		// Establecimiento de la funcionalidad de jButtonSetTitle.
		jButtonSetTitle.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jTextFieldTestName.getText().trim().equals("") == true)
					jTextFieldTestName.setText(jTextFieldTestFileName.getText());

				jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
			}
		});

		// Establecimiento de la funcionalidad de jButtonSetBgColor.
		jButtonSetBgColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color bgColor = JColorChooser.showDialog(parent, "Select the background color", null);

				jTextFieldBgColor.setText("#"
						+ String.valueOf(Integer.toHexString(bgColor.getRGB()).substring(2)));

				jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
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
						String message = "The image is too big, the maximum size should be 1 Mb";
						JOptionPane.showMessageDialog(parent, message, "File Size", JOptionPane.ERROR_MESSAGE);
						return;
					}

					jTextFieldBackground.setText(backgroundFile.getAbsolutePath());
					jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());

				} catch (SecurityException sE) {
					// Mensaje de error por falta de descarga del fichero
					// .java.policy en el
					// directorio del ususario que est� usando la herramienta
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

				jTextFieldTitleColor.setText("#"
						+ String.valueOf(Integer.toHexString(titleColor.getRGB()).substring(2)));

				jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
			}
		});

		// Establecimiento de la funcionalidad de jButtonResetTitle.
		jButtonResetTitle.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextFieldTestName.setText(jTextFieldTestFileName.getText().trim());
				jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
			}
		});

		// Establecimiento de la funcionalidad de jButtonResetBgColor.
		jButtonResetBgColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextFieldBgColor.setText("#FFFFFF");
				jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
			}
		});

		// Establecimiento de la funcionalidad de jButtonResetBackground.
		jButtonResetBackground.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextFieldBackground.setText("");
				backgroundFile = null;
				jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
			}
		});

		// Establecimiento de la funcionalidad de jButtonResetTitleColor.
		jButtonResetTitleColor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextFieldTitleColor.setText("#000000");
				jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
			}
		});

		jButtonResetAll.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextFieldTestName.setText(jTextFieldTestFileName.getText().trim());
				jTextFieldBgColor.setText("#FFFFFF");
				jTextFieldBackground.setText("");
				backgroundFile = null;
				jTextFieldTitleColor.setText("#000000");
				jEditorPaneHtmlParameters.setText(getEditorPaneHtmlParametersText());
			}
		});

		// Establecimiento de la funcionalidad de jButtonPrevious.
		if (configurationType == TestEditor.MANUALLY) {
			jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					testVector = null;
					Step3AInit();
				}
			});
		} else if (configurationType == TestEditor.RANDOM) {
			jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					testVector = null;
					Step3BInit();
				}
			});
		} else {
			if (configurationType == TestEditor.RANDOM_WITH_RESTRICTIONS) {
				jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						testVector = null;
						Step4BInit();
					}
				});
			}
		}

		// Establecimiento de la funcionalidad de jButtonNext.
		jButtonNext.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Aviso de duraci�n prolongada de la tarea.
				jLabelStatus.setText("Creating the test file. " + "This can take some minutes...");
				jButtonCreateTestActionPerformed(executionType);
			}
		});

		// Establecimiento de la funcionalidad de jButtonCancel.
		jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closingWindow();
			}
		});

		// Establecimiento de la funcionalidad de jButtonClear.
		jButtonClear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonClearParametersActionPerformed();
			}
		});
	}

	/*
	 * NOMBRE: JPanelInitStep4A. PERTENECE A: Clase FrmCreateTest. LLAMADA POR:
	 * Step4Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los JPanel del frame, reservando memoria para el
	 * mismo, estableciendo su color, layout...
	 */

	private void JPanelInitStep4A() {
		// Reserva de memoria para el panel principal del frame y
		// establecimiento de su layout.
		jPanelContent = new JPanel();
		boxLayoutPanelContent = new BoxLayout(jPanelContent, BoxLayout.Y_AXIS);
		jPanelContent.setLayout(boxLayoutPanelContent);
		jPanelContent.setAlignmentX(CENTER_ALIGNMENT);

		// Reserva de memoria para el JPanel que contendr� al jLabelInstructions.
		jPanelInstructions = new JPanel();
		flowLayoutPanelInstructions = new FlowLayout(FlowLayout.CENTER);
		jPanelInstructions.setLayout(flowLayoutPanelInstructions);
		jPanelInstructions.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jPanelInstructions.add(jLabelInstructions);

		// Llamada al m�todo que inicializa el jPanelPresentationParameters.
		JPanelPresentationParametersInit();

		// Llamada al m�todo que inicializa el jPanelHtmlParameters.
		JPanelHtmlParametersInit();

		// Llamada al m�todo que inicializa el jPanelEvaluationParameters.
		JPanelEvaluationParametersInit();

		// Llamada al m�todo que inicializa el jPanelAdaptiveParameters en caso
		// necesario.
		if (this.executionType == TestEditor.ADAPTIVE_NUM)
			JPanelAdaptiveParametersInit();

		// Reserva de memoria para el jTabbedPane.
		jTabbedPane = new JTabbedPane();

		jTabbedPane.addTab("Presentation Parameters", iconEyes16, jScrollPanePresentationParameters);
		jTabbedPane.addTab("Html Parameters", iconHtml16, jScrollPaneHtmlParameters);
		jTabbedPane.addTab("Evaluation Parameters", iconEvaluation16, jScrollPaneEvaluationParameters);

		if (this.executionType == TestEditor.ADAPTIVE_NUM)
			jTabbedPane.addTab("Adaptive Parameters", iconInfoGreen16, jScrollPaneAdaptiveParameters);

		// Reserva de memoria y establecimento del layout para el JPanel que
		// contendr� a los JButton.
		jPanelButton = new JPanel();
		flowLayoutPanelButton = new FlowLayout(FlowLayout.CENTER);
		jPanelButton.setLayout(flowLayoutPanelButton);

		jPanelButton.add(jButtonPrevious);
		jPanelButton.add(jButtonNext);
		jPanelButton.add(Box.createHorizontalGlue());
		jPanelButton.add(jButtonCancel);
		jPanelButton.add(Box.createHorizontalStrut(20));
		jPanelButton.add(jButtonClear);

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
		jPanelContent.add(jPanelInstructions);
		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jTabbedPane);
		jPanelContent.add(Box.createVerticalGlue());
		jPanelContent.add(jPanelButton);
		jPanelContent.add(jPanelStatus);
	}

	/*
	 * NOMBRE: JPanelPresentationParametersInit. PERTENECE A: Clase
	 * FrmCreateTest. LLAMADA POR: Step4Init(); LLAMA A: nada. RECIBE: void.
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
		jLabelQuestionsOrderInfo2.setAlignmentX(LEFT_ALIGNMENT);

		jPanelLabelQuestionsOrder.add(jLabelQuestionsOrder);
		jPanelLabelQuestionsOrder.add(jLabelQuestionsOrderInfo1);
		jPanelLabelQuestionsOrder.add(jLabelQuestionsOrderInfo2);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al jRadioButtonQuestionsOrderRandom y
		// jRadioButtonQuestionsOrderSequential.
		jPanelQuestionsOrderRadioButton = new JPanel();
		flowLayoutPanelQuestionsOrderRadioButton = new FlowLayout(FlowLayout.LEFT);
		jPanelQuestionsOrderRadioButton.setLayout(flowLayoutPanelQuestionsOrderRadioButton);
		jPanelQuestionsOrderRadioButton.setAlignmentX(LEFT_ALIGNMENT);
		jPanelQuestionsOrderRadioButton.setBackground(SystemColor.LIGHT_GRAY);
		jPanelQuestionsOrderRadioButton.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Establecimiento de la alineaci�n para los jRadioButton.
		jRadioButtonQuestionsOrderRandom.setAlignmentX(Component.LEFT_ALIGNMENT);
		jRadioButtonQuestionsOrderSequential.setAlignmentX(Component.LEFT_ALIGNMENT);

		jPanelQuestionsOrderRadioButton.add(jRadioButtonQuestionsOrderRandom);
		jPanelQuestionsOrderRadioButton.add(jRadioButtonQuestionsOrderSequential);

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
		jLabelAnswersOrderInfo2.setAlignmentX(LEFT_ALIGNMENT);

		jPanelLabelAnswersOrder.add(jLabelAnswersOrder);
		jPanelLabelAnswersOrder.add(jLabelAnswersOrderInfo1);
		jPanelLabelAnswersOrder.add(jLabelAnswersOrderInfo2);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contendr� al jRadioButtonAnswersOrderRandom y
		// jRadioButtonAnswersOrderSequential.
		jPanelAnswersOrderRadioButton = new JPanel();
		flowLayoutPanelAnswersOrderRadioButton = new FlowLayout(FlowLayout.LEFT);
		jPanelAnswersOrderRadioButton.setLayout(flowLayoutPanelAnswersOrderRadioButton);
		jPanelAnswersOrderRadioButton.setAlignmentX(LEFT_ALIGNMENT);
		jPanelAnswersOrderRadioButton.setBackground(SystemColor.LIGHT_GRAY);
		jPanelAnswersOrderRadioButton.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Establecimiento de la alineaci�n para jButtonManually y
		// jPanelLabelManually.
		jRadioButtonAnswersOrderRandom.setAlignmentX(Component.LEFT_ALIGNMENT);
		jRadioButtonAnswersOrderSequential.setAlignmentX(Component.LEFT_ALIGNMENT);

		jPanelAnswersOrderRadioButton.add(jRadioButtonAnswersOrderRandom);
		jPanelAnswersOrderRadioButton.add(jRadioButtonAnswersOrderSequential);

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

		// Reserva de memoria para el jScrollPanePresentationParameters.
		jScrollPanePresentationParameters = new JScrollPane(jPanelPresentationParameters,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/*
	 * NOMBRE: JPanelHtmlParametersInit. PERTENECE A: Clase FrmCreateTest.
	 * LLAMADA POR: Step4Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
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
		flowLayoutPanelHtmlParameters = new FlowLayout(FlowLayout.CENTER);
		jPanelHtmlParameters.setLayout(flowLayoutPanelHtmlParameters);
		jPanelHtmlParameters.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jPanelHtmlParametersButton.setAlignmentX(CENTER_ALIGNMENT);
		jPanelHtmlParametersEditorPane.setAlignmentX(CENTER_ALIGNMENT);

		// Adici�n de los componentes del jPanelHtmlParameters.
		jPanelHtmlParameters.add(jPanelHtmlParametersButton);
		jPanelHtmlParameters.add(jPanelHtmlParametersEditorPane);

		// Reserva de memoria para el jScrollPaneHtmlParameters.
		jScrollPaneHtmlParameters = new JScrollPane(jPanelHtmlParameters,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/*
	 * NOMBRE: JPanelEvaluationParametersInit. PERTENECE A: Clase FrmCreateTest.
	 * LLAMADA POR: Step4Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JPanel del frame, reservando memoria para
	 * el mismo, estableciendo su color, layout...
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

		// Rerseva de memoria para el panel que contendra a los anteriores,
		// decir el jPanelEvalutationParameters.
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

		// Reserva de memoria para el jScrollPaneEvaluationParameters.
		jScrollPaneEvaluationParameters = new JScrollPane(jPanelEvaluationParameters,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/*
	 * NOMBRE: JPanelAdaptiveParametersInit. PERTENECE A: Clase FrmCreateTest.
	 * LLAMADA POR: Step4Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
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

	// //////////////////////////////////////////////////////////////////////////////
	// ////////////CONFIGURACI�N DE LOS COMPONENTES DEL
	// JINTERNAFRAME////////////////
	// ////////////PARA EL PASO
	// 4B///////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: Step4BInit. PERTENECE A: Clase FrmCreateTest. LLAMADA POR: Al
	 * hacer click sobre jButtonNext cuando el frame est� configurado
	 * seg�n Step3CInit(). LLAMA A: nada. RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los componentes del frame, necesarios para el cuarto
	 * paso de la configuracion de la bateria de test, reservando memoria para
	 * los mismos y estableciendo su tama�o, su contenido, funcionalidad...
	 */

	private void Step4BInit() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		// Establecimiento del m�nimo y m�ximo para el jProgressBar.
		jProgressBar.setMinimum(0);
		jProgressBar.setMaximum(questionsFileNamesAdded.size());

		// boolean workerInit = false;
		// SwingWorker worker = null;
		GetRandomRestrictionsQuestions worker = null;

		// Llamada al m�todo de la clase padre que devuelve las preguntas para
		// un fichero de preguntas.
		if (!step.equals("4A")) {
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			jLabelStatus.setText("Loading questions of file... ");
			jPanelStatus.paint(this.jPanelStatus.getGraphics());

			Question question = new Question();

			question.setDifficultyMax(Double.valueOf(jTextFieldDifficultyMax.getText().trim()).doubleValue());
			question.setDifficultyMin(Double.valueOf(jTextFieldDifficultyMin.getText().trim()).doubleValue());
			question.setDiscriminationMax(Double.valueOf(jTextFieldDiscriminationMax.getText().trim())
					.doubleValue());
			question.setDiscriminationMin(Double.valueOf(jTextFieldDiscriminationMin.getText().trim())
					.doubleValue());
			question.setGuessingMax(Double.valueOf(jTextFieldGuessingMax.getText().trim()).doubleValue());
			question.setGuessingMin(Double.valueOf(jTextFieldGuessingMin.getText().trim()).doubleValue());
			question.setExhibitionRateMax(Double.valueOf(jTextFieldExhibitionRateMax.getText().trim())
					.doubleValue());
			question.setExhibitionRateMin(Double.valueOf(jTextFieldExhibitionRateMin.getText().trim())
					.doubleValue());
			question.setAnswerTimeMax(Integer.valueOf(jTextFieldAnswerTimeMax.getText().trim()).intValue());
			question.setAnswerTimeMin(Integer.valueOf(jTextFieldAnswerTimeMin.getText().trim()).intValue());
			question.setSuccessRateMax(Double.valueOf(jTextFieldSuccessRateMax.getText().trim()).doubleValue());
			question.setSuccessRateMin(Double.valueOf(jTextFieldSuccessRateMin.getText().trim()).doubleValue());
			question.setNumberOfAnswersMax(Integer.valueOf(jTextFieldNumberOfAnswersMax.getText().trim())
					.intValue());
			question.setNumberOfAnswersMin(Integer.valueOf(jTextFieldNumberOfAnswersMin.getText().trim())
					.intValue());

			if (jCheckBoxQuestionsInOtherClassicTest.isSelected())
				question.setClassicTest("false");
			else
				question.setClassicTest("INDIFFERENT");

			if (jCheckBoxQuestionsInOtherAdaptiveTest.isSelected())
				question.setAdaptiveTest("false");
			else
				question.setAdaptiveTest("INDIFFERENT");

			if (jRadioButtonImageYes.isSelected())
				question.setExistImage("true");
			else if (jRadioButtonImageNo.isSelected())
				question.setExistImage("false");
			else if (jRadioButtonImageIndifferent.isSelected())
				question.setExistImage("INDIFFERENT");

			worker = new GetRandomRestrictionsQuestions(question);
			// workerInit = true;
			worker.start();

			// Redibujo de la barra de progreso.
			while (questionsFileLoaded == false) {
				jLabelStatus.setText("Loading questions of file... " + questionsFileName);
				jProgressBar.setValue(process + 1);
				jPanelStatus.paint(this.jPanelStatus.getGraphics());
				this.paint(this.getGraphics());
				parent.paint(parent.getGraphics());
			}
			worker = null;
			questionsFileLoaded = false;

			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			jLabelStatus.setText("Loading questions of file... Finished");
			jProgressBar.setValue(jProgressBar.getMaximum());

			if (questionsOfMultipleFiles == null || questionsOfMultipleFiles.size() == 0) {
				// Mensaje para informar al usuario de que no se han podido hayar
				// preguntas con dichas restricciones.
				JOptionPane.showMessageDialog(parent, "There are not questions that they are adjusted " + "\n"
						+ "with the introduced restrictions." + "\n" + "You should change the restrictions.",
						"Info", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				int totalQuestion = 0;
				for (int i = 0; i < questionsOfMultipleFiles.size(); i++)
					totalQuestion = totalQuestion
							+ ((Vector) ((Vector) questionsOfMultipleFiles.get(i)).get(1)).size();

				int questionSearch = 0;
				StringTokenizer numberQuestionsToReturn = null;
				for (int i = 0; i < questionsFileNamesAdded.size(); i++) {
					numberQuestionsToReturn = new StringTokenizer(questionsFileNamesAdded.get(i).toString()
							.substring(questionsFileNamesAdded.get(i).toString().lastIndexOf("(") + 1,
									questionsFileNamesAdded.get(i).toString().lastIndexOf(")")));

					String numQuestionsToReturn = numberQuestionsToReturn.nextToken(" ");

					questionSearch = questionSearch + Integer.valueOf(numQuestionsToReturn).intValue();
				}

				if (totalQuestion < questionSearch) {
					// Mensaje para informar al usuario de que el n�mero de ficheros
					// de
					// preguntas con los que se formar� la bater�a de test es menor
					// que
					// el indicado incialmente.
					JOptionPane.showMessageDialog(parent, "WARNING: The number of question files that they will "
							+ "form the test" + "\n" + "is smaller than the one introduced initially.", "Info",
							JOptionPane.WARNING_MESSAGE);
				} else {
					// Mensaje para informar al usuario del n�mero de ficheros de
					// preguntas
					// con los que se formar� la bater�a de test.
					JOptionPane.showMessageDialog(parent, "The test will be created from "
							+ String.valueOf(questionsOfMultipleFiles.size()) + " question files.", "Info", 1);
				}
			}
		}

		// Configuraci�n del frame de forma similar a las opciones
		// MANUALLY o TestEditor.RANDOM, puesto que coinciden en este paso.
		JLabelInitStep3B();
		JButtonInitStep3B();

		if (step.equals("3C"))
			JTreeTestQuestionInit();
		else
			JTreeTestQuestionInit(rootNodeTestQuestion);

		JProgressBarInit();
		JPanelInitStep3B();

		try {
			frameInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		step = "4B";
		
		// Default cursor
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Initializes the window, setting the event listeners and
	 * adjusting the minimun size of it.
	 * Makes visible the frame
	 */
	private void frameInit() throws Exception {
	//	
		// Sets the main content panel
		if (step.trim().equals("4A") == false) {
			this.setContentPane(jScrollPane);
		} else {
			this.setContentPane(jPanelContent);
		}
		
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

	/**
	 * Controls the closing event, and alerts the user if he wants
	 * @return the code that applies to the form
	 */
	protected int closingWindow() {
		// Mensaje para confirmar la salida.
		String message = "Are you sure to exit of test creation?";
		int result = JOptionPane.showConfirmDialog(this, message, "Exit", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			this.setVisible(false);
			this.dispose();
			return DISPOSE_ON_CLOSE;
		} else {
			this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			return DO_NOTHING_ON_CLOSE;
		}
	}

	/*
	 * NOMBRE: jButtonReloadActionPerformed. PERTENECE A: Clase FrmCreateTest.
	 * LLAMADA POR: Al hacer click sobre el jButtonReload. LLAMA A:
	 * getQuestionsFileListForOneCourse(); RECIBE: nada. DEVUELVE: void. FUNCI�N:
	 * Actualiza la lista de nombres de ficheros de preguntas y los componentes
	 * necesarios del frame para mostrarla de nuevo.
	 */

	void jButtonReloadActionPerformed() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Llamada al m�todo de la clase padre que devuelve los nombres de los
		// ficheros de preguntas asociados a conceptos.
		course = new Course();
		course.setName(courseConcept.getName());
		course.setConceptNames(conceptVector);

		course = parent.getQuestionsFileAndConceptsNames(course);
		if (course == null) {
			// Mensaje para indicar al usuario que se ha producido un error al
			// intentar
			// conseguir los nombres de los ficheros de preguntas del curso.
			JOptionPane.showMessageDialog(this, "ERROR : Doesn't exist any question file in the course "
					+ "to create a test.", "Create Test Error", JOptionPane.ERROR_MESSAGE);
			this.dispose();
			return;
		}

		// Reinicializaci�n de los componentes del frame.
		JListQuestionsFilesInitStep2();
		JPanelInitStep2();

		// Establecimiento de ciertas variables a sus valores iniciales.
		if (configurationType == TestEditor.RANDOM || configurationType == TestEditor.RANDOM_WITH_RESTRICTIONS)
			questionsFileNamesSelected = false;

		// Deshabilitaci�n del jButtonAdd.
		jButtonAdd.setEnabled(false);

		try {
			frameInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Cursor estandar.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: jButtonAddActionPerformed. PERTENECE A: Clase FrmCreateTest.
	 * LLAMADA POR: Al hacer click sobre el jButtonAdd. LLAMA A: nada. RECIBE:
	 * nada. DEVUELVE: void. FUNCI�N: A�ade los valores seleccionados en el
	 * jListQuestionsFile al jListquestionsFileNamesAdded.
	 */

	void jButtonAddActionPerformed() {
		// Obtenci�n de los valores seleccionados en jListQuestionsFile.
		Object[] questionsFileNameSelected = jListQuestionsFile.getSelectedValues();

		String nameQuestionsFile = "";
		StringTokenizer numberQuestionsInFile;
		String numberQuestions = "";

		// Bucle para la adici�n de los valores seleccionados.
		for (int i = 0; i < questionsFileNameSelected.length; i++) {
			// Obtenci�n del nombre del fichero de preguntas seleccionado y del
			// n�mero de preguntas que posee dicho fichero.
			nameQuestionsFile = questionsFileNameSelected[i].toString().substring(0,
					questionsFileNameSelected[i].toString().lastIndexOf(" ("));
			numberQuestionsInFile = new StringTokenizer(questionsFileNameSelected[i].toString().substring(
					questionsFileNameSelected[i].toString().lastIndexOf("(") + 1,
					questionsFileNameSelected[i].toString().lastIndexOf(")")));
			numberQuestions = numberQuestionsInFile.nextToken(" ");

			boolean add = true;
			for (int j = 0; j < questionsFileNamesAdded.size(); j++) {
				if (questionsFileNamesAdded.get(j).toString().startsWith(nameQuestionsFile + " (") == true
						|| Integer.valueOf(numberQuestions).intValue() == 0) {
					add = false;
					break;
				}
			}

			if (add == false)
				continue;
			else {
				if (configurationType == TestEditor.RANDOM || configurationType == TestEditor.RANDOM_WITH_RESTRICTIONS) {
					if (Integer.valueOf(numberQuestions).intValue() < Integer.valueOf(
							jTextFieldNumQuestions.getText()).intValue())
						questionsFileNamesAdded.add(nameQuestionsFile + " " + "(" + numberQuestions + ")");
					else
						questionsFileNamesAdded.add(nameQuestionsFile + " " + "("
								+ jTextFieldNumQuestions.getText() + ")");
				} else {
					questionsFileNamesAdded.add(nameQuestionsFile);
				}
			}
		}

		// Reinicializci�n de los componentes necesarios.
		JListquestionsFileNamesAddedInitStep2();
		JPanelInitStep2();

		try {
			frameInit();

			if (questionsFileNamesAdded.isEmpty() == false) {
				// Habilitaci�n del jButtonClear.
				jButtonClear.setEnabled(true);

				// Habilitaci�n del jButtonNext.
				jButtonNext.setEnabled(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * NOMBRE: jButtonDeleteActionPerformed. PERTENECE A: Clase FrmCreateTest.
	 * LLAMADA POR: Al hacer click sobre el jButtonDelete. LLAMA A: nada. RECIBE:
	 * nada. DEVUELVE: void. FUNCI�N: Elimina los valores seleccionados en el
	 * jListquestionsFileNamesAdded
	 * 
	 */

	void jButtonDeleteActionPerformed() {
		// Eliminaci�n de los valores seleccionados.
		Object[] questionsFileNameSelected = jListQuestionsFileNamesAdded.getSelectedValues();

		for (int i = 0; i < questionsFileNameSelected.length; i++) {
			questionsFileNamesAdded.remove(questionsFileNameSelected[i]);
		}

		// Reinicializci�n de los componentes necesarios.
		JListquestionsFileNamesAddedInitStep2();
		JPanelInitStep2();

		jListQuestionsFileNamesAdded.clearSelection();

		// Habilitaci�n o deshabilitaci�n del jButtonClear.
		if (questionsFileNamesAdded.isEmpty())
			jButtonClear.setEnabled(false);
		else
			jButtonClear.setEnabled(true);

		try {
			frameInit();

			// Deshabilitacion del jButtonDelete.
			jButtonDelete.setEnabled(false);

			// Deshabilitaci�n del jButtonNext si questionsFileNamesAdded esta
			// vacio.
			if (questionsFileNamesAdded.isEmpty())
				jButtonNext.setEnabled(false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * NOMBRE: jButtonClearActionPerformed. PERTENECE A: Clase FrmCreateTest.
	 * LLAMADA POR: Al hacer click sobre el jButtonClear en el paso Step2().
	 * LLAMA A: nada. RECIBE: nada. DEVUELVE: void. FUNCI�N: Elimina todos los
	 * valores en el jListquestionsFileNamesAdded
	 */

	void jButtonClearActionPerformed() {
		// Eliminaci�n de los valores seleccionados.
		questionsFileNamesAdded.removeAllElements();

		// Reinicializci�n de los componentes necesarios.
		JListquestionsFileNamesAddedInitStep2();
		JPanelInitStep2();

		jListQuestionsFileNamesAdded.clearSelection();

		// Habilitaci�n o deshabilitaci�n del jButtonClear.
		if (questionsFileNamesAdded.isEmpty())
			jButtonClear.setEnabled(false);
		else
			jButtonClear.setEnabled(true);

		try {
			frameInit();

			// Deshabilitacion del jButtonDelete.
			jButtonDelete.setEnabled(false);

			// Deshabilitaci�n del jButtonNext.
			jButtonNext.setEnabled(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * NOMBRE: jButtonClearRestrictionsActionPerformed. PERTENECE A: Clase
	 * FrmCreateTest. LLAMADA POR: Al hacer click sobre el jButtonClear en el
	 * paso Step3C(). LLAMA A: nada. RECIBE: nada. DEVUELVE: void. FUNCI�N:
	 * Resetea los valores de las restricciones para la configuraci�n del test,
	 * es decir, pone los JSlider y los JTextField en sus valores de inicio.
	 */

	void jButtonClearRestrictionsActionPerformed() {
		// Restauraci�n de los valores originales de los JSlider.
		jSliderDifficultyMin.setValue(jSliderDifficultyMin.getMinimum());
		jSliderDifficultyMax.setValue(jSliderDifficultyMax.getMaximum());

		jSliderDiscriminationMin.setValue(jSliderDiscriminationMin.getMinimum());
		jSliderDiscriminationMax.setValue(jSliderDiscriminationMax.getMaximum());

		jSliderGuessingMin.setValue(jSliderGuessingMin.getMinimum());
		jSliderGuessingMax.setValue(jSliderGuessingMax.getMaximum());

		jSliderExhibitionRateMin.setValue(jSliderExhibitionRateMin.getMinimum());
		jSliderExhibitionRateMax.setValue(jSliderExhibitionRateMax.getMaximum());

		jTextFieldAnswerTimeMin.setText("0.0");
		jTextFieldAnswerTimeMax.setText("INF");

		jSliderSuccessRateMin.setValue(jSliderSuccessRateMin.getMinimum());
		jSliderSuccessRateMax.setValue(jSliderSuccessRateMax.getMaximum());

		jSliderNumberOfAnswersMin.setValue(jSliderNumberOfAnswersMin.getMinimum());
		jSliderNumberOfAnswersMax.setValue(jSliderNumberOfAnswersMax.getMaximum());

		// Establecimiento de la selecci�n por defecto.
		jRadioButtonImageIndifferent.setSelected(true);
	}

	/*
	 * NOMBRE: jButtonClearParametersActionPerformed(). PERTENECE A: Clase
	 * FrmCreateTest. LLAMADA POR: Al hacer click sobre el jButtonClear en el
	 * paso Step4A(). LLAMA A: nada. RECIBE: nada. DEVUELVE: void. FUNCI�N:
	 * Resetea los valores de la configuraci�n del ejecuci�n test, es decir, pone
	 * los JSlider y los JTextField en sus valores de inicio.
	 */

	void jButtonClearParametersActionPerformed() {
		jCheckBoxShowInitialInfo.setSelected(true);
		jRadioButtonQuestionsOrderSequential.setSelected(true);
		jRadioButtonAnswersOrderSequential.setSelected(true);
		jCheckBoxShowQuestionCorrection.setSelected(true);
		jCheckBoxVerbose.setSelected(true);
		jSliderTimeToAnswer.setValue(ANSWER_TIME_MAX);

		jRadioButtonIncorrectAnswersPenalizeYes.setSelected(true);
		jRadioButtonWithoutAnswersPenalizeNo.setSelected(true);
		jComboBoxIncorrectAnswers.setSelectedIndex(0);
		jComboBoxWithoutAnswers.setSelectedIndex(0);
		jSliderKnowledge.setValue(jSliderKnowledge.getMaximum());

		jSliderInitialProficiency.setValue((int) (INITIAL_PROFICIENCY_DEFAULT * 10));
		jRadioButton3P.setSelected(true);
		jRadioButtonStandardError.setSelected(true);
		jSliderStandardError.setValue((int) (STANDARD_ERROR_DEFAULT * 100));
		jSliderNumberItems.setValue(contQuestionsTest);
	}

	/*
	 * NOMBRE: jListQuestionsFileSelectionPerformed. PERTENECE A: Clase
	 * FrmCreateTest. LLAMADA POR: Al hacer click sobre el jListQuestionsFile.
	 * LLAMA A: nada. RECIBE: nada. DEVUELVE: void. FUNCI�N: Comprueba la
	 * selecci�n para habilitar o deshabilitar ciertos JButton.
	 */

	void jListQuestionsFileSelectionPerformed(javax.swing.event.ListSelectionEvent e) {
		// Comprobaci�n de la selecci�n.
		if ((jListQuestionsFile.getSelectedValues()).length == 0)
			questionsFileNamesSelected = false;
		else
			questionsFileNamesSelected = true;

		// Habilitaci�n o deshabilitaci�n de jButtonAdd.
		if (questionsFileNamesSelected == true && introducedNumQuestions == true)
			jButtonAdd.setEnabled(true);
		else
			jButtonAdd.setEnabled(false);
	}

	/*
	 * NOMBRE: jListquestionsFileNamesAddedSelectionPerformed. PERTENECE A: Clase
	 * FrmCreateTest. LLAMADA POR: Al hacer click sobre el
	 * jListquestionsFileNamesAdded. LLAMA A: nada. RECIBE: nada. DEVUELVE: void.
	 * FUNCI�N: Comprueba la selecci�n para habilitar o deshabilitar ciertos
	 * JButton.
	 */

	void jListQuestionsFileNamesAddedSelectionPerformed() {
		// Comprobaci�n de la selecci�n.
		if ((jListQuestionsFileNamesAdded.getSelectedValues()).length == 0)
			jButtonDelete.setEnabled(false);
		else
			jButtonDelete.setEnabled(true);
	}

	/*
	 * NOMBRE: jTextFieldNumQuestionsActionPerformed. PERTENECE A: Clase
	 * FrmCreateTest. LLAMADA POR: Al pulsar una tecla del teclado con el foco
	 * del rat�n situado sobre jTextFieldNumQuestions. LLAMA A: nada. RECIBE: e:
	 * Objeto de tipo KeyEvent. Contiene el evento que produjo la llamada a este
	 * m�todo. DEVUELVE: void. FUNCI�N: Muestra un mensaje de error indicando al
	 * usuario que el car�cter introducido en jTextFieldNumQuestions no es
	 * correcto y habilita o deshabilita el jButtonNext seg�n el mismo criterio.
	 */

	void jTextFieldNumQuestionsActionPerformed(KeyEvent e) {
		if (jTextFieldNumQuestions.getText().equals("") == false) {
			try {
				int numQuestions = Integer.valueOf(jTextFieldNumQuestions.getText()).intValue();
				if (numQuestions <= 0) {
					JOptionPane.showMessageDialog(this, "ERROR : The introduced value is not correct.",
							"Incorrect data", 1);

					jTextFieldNumQuestions.setText("");
					introducedNumQuestions = false;
				} else
					introducedNumQuestions = true;
			} catch (java.lang.NumberFormatException jle) {
				// Mensaje para indicar al usuario que el car�cter introducido
				// no es v�lido.
				JOptionPane.showMessageDialog(this, "ERROR : The introduced value is not correct. "
						+ "\nYou should introduce a number.", "Incorrect data", 1);

				jTextFieldNumQuestions.setText("");
				introducedNumQuestions = false;
			}
		} else
			introducedNumQuestions = false;

		// Habilitaci�n o deshabilitaci�n de jButtonAdd.
		if (questionsFileNamesSelected == true && introducedNumQuestions == true)
			jButtonAdd.setEnabled(true);
		else
			jButtonAdd.setEnabled(false);
	}

	/*
	 * NOMBRE: jButtonAddQuestionsToTestActionPerformed. PERTENECE A: Clase
	 * FrmCreateTest. LLAMADA POR: Al hacer click sobre cualquier
	 * jButtonAddQuestionToTest. LLAMA A: nada. RECIBE: nada. DEVUELVE: void.
	 * FUNCI�N: A�ade los nodos con las preguntas que se desean incluir en el
	 * test al jTreeTestQuestion.
	 */

	void jButtonAddQuestionToTestActionPerformed() {
		// Establecimiento del tabb en el que se encuentra el fichero
		// de preguntas que se va a utilizar para el test.
		tabSelect = jTabbedPane.getSelectedIndex();

		TreePath treePath = jTreeTestQuestion.getPathForRow(0);

		rootNodeTestQuestion = (DefaultMutableTreeNode) treePath.getPathComponent(0);
		DefaultMutableTreeNode questionsFileNode = null;

		// Comprobaci�n para ver si se han a�adidos preguntas de este mismo
		// fichero
		// anteriormente.
		for (int i = 0; i < rootNodeTestQuestion.getChildCount(); i++) {
			if (rootNodeTestQuestion.getChildAt(i).toString().equals(questionsFileNameToAdd)) {
				// Comprobaci�n de si se va a�adir un fichero de preguntas completo.
				if (codeOfQuestionsToAdd.equals("ALL_FILE")) {
					// Borrado del nodo antiguo.
					rootNodeTestQuestion.remove(i);

					// Insertado del nuevo en la posici�n del antiguo.
					rootNodeTestQuestion.insert(nodeQuestion, i);

					// Reinicializaci�n del frame.
					// NO ALTERAR EL ORDEN!!!!!!!!!!
					int locationSplit = jSplitPane.getDividerLocation();

					JButtonInitStep3A();
					JTreeTestQuestionInit(rootNodeTestQuestion);
					JPanelInitStep3A();

					try {
						frameInit();
					} catch (Exception e) {
						e.printStackTrace();
					}

					jSplitPane.setDividerLocation(locationSplit);

					// Habilitaci�n del jButtonNext.
					jButtonNext.setEnabled(true);
					nodeQuestion = null;

					return;
				} else {
					questionsFileNode = (DefaultMutableTreeNode) rootNodeTestQuestion.getChildAt(i);
					for (int j = 0; j < questionsFileNode.getChildCount(); j++) {
						if (questionsFileNode.getChildAt(j).toString().equals(nodeQuestion.toString())) {
							// Reinicializaci�n del frame.
							int locationSplit = jSplitPane.getDividerLocation();

							JButtonInitStep3A();
							JPanelInitStep3A();
							try {
								frameInit();
							} catch (Exception e) {
								e.printStackTrace();
							}
							jSplitPane.setDividerLocation(locationSplit);

							// Habilitaci�n del jButtonNext.
							jButtonNext.setEnabled(true);
							nodeQuestion = null;

							return;
						}
					}
				}

				questionsFileNode.add(nodeQuestion);
				rootNodeTestQuestion.insert(questionsFileNode, i);

				// Reinicializaci�n del frame.
				int locationSplit = jSplitPane.getDividerLocation();

				JTreeTestQuestionInit(rootNodeTestQuestion);
				JButtonInitStep3A();

				JPanelInitStep3A();
				try {
					frameInit();
				} catch (Exception e) {
					e.printStackTrace();
				}
				jSplitPane.setDividerLocation(locationSplit);

				// Habilitaci�n del jButtonNext.
				jButtonNext.setEnabled(true);
				nodeQuestion = null;

				return;
			}
		}

		// Si sale del bucle significa que no se han a�adido preguntas de dicho
		// fichero anteriormente, con lo que se crea el nuevo nodo.
		if (codeOfQuestionsToAdd.equals("ALL_FILE"))
			rootNodeTestQuestion.add(nodeQuestion);
		else {
			questionsFileNode = new DefaultMutableTreeNode(questionsFileNameToAdd);
			questionsFileNode.add(nodeConcept);
			questionsFileNode.add(nodeQuestion);
			rootNodeTestQuestion.add(questionsFileNode);
		}

		// Reinicializaci�n del frame.
		int locationSplit = jSplitPane.getDividerLocation();

		JTreeTestQuestionInit(rootNodeTestQuestion);
		JButtonInitStep3A();

		JPanelInitStep3A();
		try {
			frameInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		jSplitPane.setDividerLocation(locationSplit);

		// Habilitaci�n del jButtonNext.
		jButtonNext.setEnabled(true);
		nodeQuestion = null;
	}

	/*
	 * NOMBRE: jButtonDeleteQuestionsToTestActionPerformed. PERTENECE A: Clase
	 * FrmCreateTest. LLAMADA POR: Al hacer click sobre cualquier
	 * jButtonDeleteQuestionsToTest. LLAMA A: nada. RECIBE: nada. DEVUELVE: void.
	 * FUNCI�N: Elimina nodos con las preguntas del test ya incluidas en el
	 * jTreeTestQuestion.
	 */

	void jButtonDeleteQuestionToTestActionPerformed() {
		TreePath treePath = jTreeTestQuestion.getPathForRow(0);
		rootNodeTestQuestion = (DefaultMutableTreeNode) treePath.getPathComponent(0);

		boolean delete = false;
		// Busqueda del nodo a borrar.
		for (int i = 0; i < rootNodeTestQuestion.getChildCount(); i++) {
			if (rootNodeTestQuestion.getChildAt(i).toString().equals(questionsFileNameToDelete)) {
				// Comprobaci�n de si se va borrar un fichero de preguntas completo.
				if (codeOfQuestionsToDelete.equals("ALL_FILE")) {
					// Borrado del nodo antiguo.
					rootNodeTestQuestion.remove(i);
					delete = true;
				} else {
					DefaultMutableTreeNode questionsFileNode = (DefaultMutableTreeNode) rootNodeTestQuestion
							.getChildAt(i);
					for (int j = 0; j < questionsFileNode.getChildCount(); j++) {
						if (questionsFileNode.getChildAt(j).toString().equals(nodeQuestionTest.toString())) {
							questionsFileNode.remove(j);
							delete = true;

							// Borrado del padre en el caso de que se convierta en
							// hoja.
							if (questionsFileNode.isLeaf())
								rootNodeTestQuestion.remove(questionsFileNode);

							break;
						}
					}
				}
			}
			if (delete == true)
				break;
		}

		// Reinicializaci�n del frame.
		// NO ALTERAR EL ORDEN !!!!!
		if (configurationType == TestEditor.MANUALLY) {
			JButtonInitStep3A();
			JTreeTestQuestionInit(rootNodeTestQuestion);
			JPanelInitStep3A();
		} else {
			JButtonInitStep3B();
			JTreeTestQuestionInit(rootNodeTestQuestion);
			JPanelInitStep3B();
		}
		try {
			frameInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Deshabilitaci�n del jButtonNext.
		if (rootNodeTestQuestion.isLeaf())
			jButtonNext.setEnabled(false);
		else
			jButtonNext.setEnabled(true);
	}

	/*
	 * NOMBRE: jButtonCreateTestActionPerformed. PERTENECE A: Clase
	 * FrmCreateTest. LLAMADA POR: Al hacer click sobre el jButtonNext
	 * configurado en Step4AInit() LLAMA A: createClassicTestFile(); RECIBE:
	 * executionType: Objeto de tipo int. Indica si el test que se va a crear es
	 * cl�sico o adaptativo. DEVUELVE: void. FUNCI�N: Solicita el nombre para el
	 * fichero de test que se va a crear, comprueba las restricciones y los
	 * par�metros de ejecuci�n del test y los prepara para ser almacenados en un
	 * fichero en el servidor.
	 */

	void jButtonCreateTestActionPerformed(int executionType) {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		boolean replace = false; // Indicar�, en el caso que exista un fichero
											// con
		// el mismo nombre que el que se va a crear, si este
		// debe ser reemplazado por el nuevo.

		jLabelStatus.setText("Done                                            ");
		jPanelStatus.paint(this.jPanelStatus.getGraphics());
		jProgressBar.setVisible(true);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		Test test = new Test();

		// Habilitaci�n por defecto del test.
		test.setEnable(true);

		// Establecimiento del tipo de test.
		if (executionType == TestEditor.CLASSIC_NUM)
			test.setExecutionType(TestEditor.CLASSIC);
		else if (executionType == TestEditor.ADAPTIVE_NUM)
			test.setExecutionType(TestEditor.ADAPTIVE);

		// Asignacion de la �ltima fecha de uso del test.
		test.setLastUse("0");

		// Establecimiento del m�nimo y m�ximo para el jProgressBar.
		jProgressBar.setMinimum(0);
		jProgressBar.setMaximum(16);
		int progress = 0;

		jLabelStatus.setText("Extracting the concept of the questions");
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		// Asignaci�n de los nombres de los ficheros de conceptos asociados con
		// este test.
		test.setConceptVector(conceptListFinal);

		// Asignaci�n de los nombre de los ficheros de preguntas que forman el
		// test
		// y de los c�digos de las preguntas de dichos ficheros.
		test.setTestVector(testVector);

		// LECTURA DE LOS PAR�METROS REFERENTES A LA EJECUCI�N DEL TEST.

		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		jLabelStatus.setText("Extracting the running parameters...");
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		// Lectura de si se mostrar� la presentaci�n del test.
		jLabelStatus.setText("Extracting show test presentation...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		test.setShowInitialInfo(jCheckBoxShowInitialInfo.isSelected());

		// Lectura del orden en el que se mostrar�n las preguntas.
		jLabelStatus.setText("Extracting questions order...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		if (jRadioButtonQuestionsOrderRandom.isSelected())
			test.setQuestionsOrder(TestEditor.RANDOM_STR);
		else
			test.setQuestionsOrder(TestEditor.SEQUENTIAL_STR);

		// Lectura del orden en el que se mostrar�n las respuestas de las
		// preguntas.
		jLabelStatus.setText("Extracting answers order...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		if (jRadioButtonAnswersOrderRandom.isSelected())
			test.setAnswersOrder(TestEditor.RANDOM_STR);
		else
			test.setAnswersOrder(TestEditor.SEQUENTIAL_STR);

		// Lectura del tipo de evaluaci�n del test.
		jLabelStatus.setText("Extracting the show question correction");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		test.setShowQuestionCorrection(jCheckBoxShowQuestionCorrection.isSelected());

		// Lectura de si se mostrar� o no el verbose.
		jLabelStatus.setText("Extracting verbose...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		if (jCheckBoxShowQuestionCorrection.isSelected())
			test.setVerbose(jCheckBoxVerbose.isSelected());
		else
			test.setVerbose(false);

		// Lectura de si se mostrar� o no las respuestas correctas.
		jLabelStatus.setText("Extracting show correct answers...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		if (jCheckBoxShowQuestionCorrection.isSelected())
			test.setShowCorrectAnswers(jCheckBoxShowCorrectAnswers.isSelected());
		else
			test.setShowCorrectAnswers(false);

		// Lectura del tiempo m�ximo para responder.
		jLabelStatus.setText("Extracting time to answer...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		if (jCheckBoxTimeToAnswer.isSelected())
			test.setTimeOfAnswer(String.valueOf(jSliderTimeToAnswer.getValue()));
		else
			test.setTimeOfAnswer("0");

		// Lectura de si se mostrar� la presentaci�n del test.
		jLabelStatus.setText("Extracting repetition of questions without answering...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		test.setRepeatWithoutAnswer(jCheckBoxRepeatWithoutAnswer.isSelected());

		// Lectura de si se mostrar� la presentaci�n del test.
		jLabelStatus.setText("Extracting show final information of test...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		test.setShowFinalInfo(jCheckBoxShowFinalInfo.isSelected());

		// Lectura del t�tulo del test que se mostrara en la p�gina que
		// mostar� el test.
		jLabelStatus.setText("Extracting test title...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		test.setTestName(jTextFieldTestName.getText().trim());

		// Lectura el color del t�tulo del test que se mostrara en la p�gina que
		// mostar� el test.
		jLabelStatus.setText("Extracting title color...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		test.setTitleColor(jTextFieldTitleColor.getText().trim());

		// Lectura el color de fondo de la p�gina web que mostrar� el test.
		jLabelStatus.setText("Extracting bgColor...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		test.setBgColor(jTextFieldBgColor.getText().trim());

		// Lectura de la imagen de fondo de la p�gina web que mostrar� el test.
		jLabelStatus.setText("Extracting background...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		if (backgroundFile != null)
			test.setBackgroundType(backgroundFile.getName().substring(backgroundFile.getName().lastIndexOf("."))
					.toLowerCase());
		else
			test.setBackgroundType("");

		// Lectura de la penalizaci�n a respuestas incorrectas.
		jLabelStatus.setText("Extracting incorrect answers penalization...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		if (jRadioButtonIncorrectAnswersPenalizeYes.isSelected()) {
			test.setIncorrectAnswersPenalize(true);
			test.setIncorrectAnswersPenalizeNumber(Integer.valueOf(
					String.valueOf(jComboBoxIncorrectAnswers.getSelectedItem())).intValue());
		} else {
			test.setIncorrectAnswersPenalize(false);
			test.setIncorrectAnswersPenalizeNumber(0);
		}

		// Lectura de la penalizaci�n a respuestas sin contestar.
		jLabelStatus.setText("Extracting the questions without answer penalization...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		if (jRadioButtonWithoutAnswersPenalizeYes.isSelected()) {
			test.setWithoutAnswersPenalize(true);
			test.setWithoutAnswersPenalizeNumber(Integer.valueOf(
					String.valueOf(jComboBoxWithoutAnswers.getSelectedItem())).intValue());
		} else {
			test.setWithoutAnswersPenalize(false);
			test.setWithoutAnswersPenalizeNumber(0);
		}

		// Lectura del porcentaje de conocimiento asociado al test.
		jLabelStatus.setText("Extracting knowledge percentage...");
		progress++;
		jProgressBar.setValue(progress);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		test.setKnowledgePercentage(String.valueOf(jSliderKnowledge.getValue()));

		// Asignaci�n de los valores para la configuraci�n adaptativa.
		if (executionType == TestEditor.ADAPTIVE_NUM) {
			// Lectura del criterio de comienzo del test adaptativo.
			test.setIrtInitialProficiency(Double.valueOf(jTextFieldInitialProficiency.getText().trim())
					.doubleValue());
			if (test.getIrtInitialProficiency() == 0.0)
				test.setIrtInitialProficiency(0.1);

			// Lectura del criterio de continuaci�n del test adaptativo.
			if (jRadioButton3P.isSelected())
				test.setIrtModel(3);
			else if (jRadioButton2P.isSelected())
				test.setIrtModel(2);
			else if (jRadioButton1P.isSelected())
				test.setIrtModel(1);

			// Lectura del criterio de parada del test adaptativo.
			if (jRadioButtonStandardError.isSelected()) {
				test.setIrtStopCriterion("standardError");
				test.setIrtStandardError(Double.valueOf(jTextFieldStandardError.getText().trim()).doubleValue());
			} else {
				if (jRadioButtonNumberItems.isSelected()) {
					test.setIrtStopCriterion("numberItemsAdministred");
					test.setIrtNumberItemsAdministred(Integer.valueOf(jTextFieldNumberItems.getText().trim())
							.intValue());
				}
			}
		}

		// Establecimiento del nombre para el fichero que se va a crear.
		test.setTestFileName(jTextFieldTestFileName.getText().trim());

		try {
			test.setCourse(course.getName());

			if (testType == TestEditor.ACTIVITY_NUM) {
				test.setTestType("activity");
			} else {
				if (testType == TestEditor.EXAM_NUM) {
					test.setTestType("exam");
					test.setAbstractConcept(course.getName() + "."
							+ jComboBoxAbstractConcept.getSelectedItem().toString().trim());
				}
			}

			// Sustituci�n de los los car�cteres " ", por "-" en el nombre
			// introducido.
			test.setTestFileName(test.getTestFileName().replaceAll(" ", "-"));

			// Llamada al m�todo de la clase padre que llama a su vez a un servlet
			// alojado en el servidor encargado de crear el fichero con la
			// configuraci�n
			// del test.
			boolean exitWhile = false;
			String createFile = "";
			String testFileNameOld = test.getTestFileName();
			String testFileName = test.getTestFileName();

			jLabelStatus.setText("Creating test file...                            ");
			progress++;
			jProgressBar.setValue(progress);
			jPanelStatus.paint(this.jPanelStatus.getGraphics());

			while (exitWhile == false) {
				createFile = parent.createTestFile(test, // Datos de
																							// configuraci�n
																							// del
																							// test.
						replace, // reemplazar un fichero ya existente.
						true, // comprobar alumnos sin finalizar el test.
						backgroundFile, // Fichero de imagen.
						null // Indica si borrar los test de los
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
					// Mensaje para preguntar si el fichero de preguntas, que tiene
					// el
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
										testFileName.lastIndexOf(course.getName()) - 1)
										+ " "
										+ wordToAdd.replaceAll("_", "-")
										+ testFileName.substring(testFileName.lastIndexOf(course.getName()) - 1);
								testFileName = testFileName.replaceAll(" ", "-");

								test.setTestFileName(testFileName);

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
						// Bucle para la busqueda de frames de tipo
						// FrmEditTest
						// que estuvier�n mostrando los datos de un fichero que acaba
						// de
						// ser sustituido por uno nuevo.

						// Obtenci�n de todos lo frame que se est�n
						// ejecutando
						// actualmente.
						JInternalFrame [] jInternalFrames = parent.getDesktopPane().getAllFrames();

						// Bucle para la llamada a los m�todos closingWindow de cada
						// uno de
						// los frame obtenidos anteriormente.
						for (int i = 0; i < jInternalFrames.length; i++) {
							try {
								FrmEditTest frmEditTest = (FrmEditTest) jInternalFrames[i];

								if (frmEditTest.courseName.equals(test.getCourse())
										&& frmEditTest.testFileName.equals(testFileName)) {
									frmEditTest.jButtonReloadActionPerformed();
								}
							} catch (java.lang.ClassCastException e1) {}
						}
					}
				}
			}

			jLabelStatus.setText("Done                                             ");
			jPanelStatus.paint(this.jPanelStatus.getGraphics());
			jProgressBar.setVisible(false);
			jPanelStatus.paint(this.jPanelStatus.getGraphics());
			this.setVisible(false);
			this.dispose();
		} catch (java.lang.NullPointerException e) {
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			jLabelStatus.setText("Done                                             ");
			jPanelStatus.paint(this.jPanelStatus.getGraphics());
			jProgressBar.setVisible(false);
			jPanelStatus.paint(this.jPanelStatus.getGraphics());
		}

		// Cursor estandar.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	// //////////////////////////////////////////////////////////////////////////////
	// ////////////////// CLASES PROPIOS DE LA CLASE FRMQUESTIONDATA
	// ////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: GetQuestions. FUNCION: Clase contenida dentro de la clase
	 * FrmCreateTest. Es llamada por el m�todo construct de la clase SwingWorker.
	 * Se encarga de invocar al m�todo getQuestions de la clase padre para
	 * obtener todas las preguntas de los ficheros de preguntas que se han
	 * seleccionado para formar la bater�a de test.
	 */

	class GetQuestions extends Thread {
		GetQuestions() {}

		public void run() {
			// Reserva de memoria para questionsOfMultipleFiles, el cual contendr�
			// todas las preguntas de todos los ficheros seleccionados para crear
			// la
			// bater�a de test.
			questionsOfMultipleFiles = new Vector();

			// Bucle para la obtenci�n de las preguntas de cada uno de los ficheros
			// seleccionados para crear la bater�a de test.
			for (int i = 0; i < questionsFileNamesAdded.size(); i++) {
				// Obtenci�n de la cadena con el nombre del fichero de preguntas.
				if (configurationType == TestEditor.RANDOM || configurationType == TestEditor.RANDOM_WITH_RESTRICTIONS) {
					questionsFileName = questionsFileNamesAdded.get(i).toString().substring(0,
							questionsFileNamesAdded.get(i).toString().lastIndexOf(" ("));
				} else
					questionsFileName = questionsFileNamesAdded.get(i).toString();

				process = i;

				try {
					Thread.sleep(100);
					// Llamada al m�todo de la clase padre que devuelve las preguntas
					// para
					// un fichero de preguntas.
					questionsVector = parent.getQuestions(course.getName(), questionsFileName);

					if (questionsVector != null) {
						// Adici�n del nombre del fichero de preguntas y del Array de
						// objetos
						// Vector con sus preguntas.
						nameFileAndQuestions = new Vector();
						nameFileAndQuestions.add(questionsFileName);
						nameFileAndQuestions.add(questionsVector);

						// Adici�n del vector anterior al vector
						// questionsOfMultipleFiles
						questionsOfMultipleFiles.add(nameFileAndQuestions);
					}
				} catch (java.lang.InterruptedException e) {
					e.printStackTrace();
				}
			}
			questionsFileLoaded = true;
		}
	}

	/*
	 * NOMBRE: GetRandomQuestions. FUNCION: Clase contenida dentro de la clase
	 * FrmCreateTest. Es llamada por el m�todo construct de la clase SwingWorker.
	 * Se encarga de invocar al m�todo getRandomQuestions de la clase padre para
	 * obtener las preguntas de los ficheros de preguntas que se han seleccionado
	 * para formar la bater�a de test.
	 */

	class GetRandomQuestions extends Thread {
		GetRandomQuestions() {}

		public void run() {
			// Reserva de memoria para questionsOfMultipleFiles, el cual contendr�
			// todas las preguntas de todos los ficheros seleccionados para crear
			// la
			// bater�a de test.
			questionsOfMultipleFiles = new Vector();

			// Bucle para la obtenci�n de las preguntas de cada uno de los ficheros
			// seleccionados para crear la bater�a de test.
			StringTokenizer numberQuestionsToGet;
			String numQuestionsToGet = null;
			for (int i = 0; i < questionsFileNamesAdded.size(); i++) {
				// Obtenci�n de la cadena con el nombre del fichero de preguntas.
				if (configurationType == TestEditor.RANDOM || configurationType == TestEditor.RANDOM_WITH_RESTRICTIONS) {
					questionsFileName = questionsFileNamesAdded.get(i).toString().substring(0,
							questionsFileNamesAdded.get(i).toString().lastIndexOf(" ("));

					numberQuestionsToGet = new StringTokenizer(questionsFileNamesAdded.get(i).toString()
							.substring(questionsFileNamesAdded.get(i).toString().lastIndexOf("(") + 1,
									questionsFileNamesAdded.get(i).toString().lastIndexOf(")")));
					numQuestionsToGet = numberQuestionsToGet.nextToken(" ");
				} else
					questionsFileName = questionsFileNamesAdded.get(i).toString();

				process = i;

				try {
					Thread.sleep(100);
					// Llamada al m�todo de la clase padre que devuelve las preguntas
					// para
					// un fichero de preguntas.
					questionsVector = parent.getRandomQuestions(course.getName(),
							questionsFileName, Integer.valueOf(numQuestionsToGet).intValue());
					if (questionsVector != null) {
						// Adici�n del nombre del fichero de preguntas y del Array de
						// objetos
						// Vector con sus preguntas.
						nameFileAndQuestions = new Vector();
						nameFileAndQuestions.add(questionsFileName);
						nameFileAndQuestions.add(questionsVector);

						// Adici�n del vector anterior al vector
						// questionsOfMultipleFiles
						questionsOfMultipleFiles.add(nameFileAndQuestions);
					}
				} catch (java.lang.InterruptedException e) {
					e.printStackTrace();
				}
			}
			questionsFileLoaded = true;
		}
	}

	/*
	 * NOMBRE: GetRandomRestrictionsQuestions. FUNCION: Clase contenida dentro de
	 * la clase FrmCreateTest. Es llamada por el m�todo construct de la clase
	 * SwingWorker. Se encarga de invocar al m�todo
	 * getRandomRestrictionsQuestions de la clase padre para obtener las
	 * preguntas de los ficheros de preguntas que se han seleccionado para formar
	 * la bater�a de test.
	 */

	class GetRandomRestrictionsQuestions extends Thread {
		Question question = null;

		GetRandomRestrictionsQuestions(Question question) {
			this.question = question;
		}

		public void run() {
			// Reserva de memoria para questionsOfMultipleFiles, el cual contendr�
			// todas las preguntas de todos los ficheros seleccionados para crear
			// la
			// bater�a de test.
			questionsOfMultipleFiles = new Vector();

			// Bucle para la obtenci�n de las preguntas de cada uno de los ficheros
			// seleccionados para crear la bater�a de test.
			StringTokenizer numberQuestionsToGet;
			String numQuestionsToGet = null;
			for (int i = 0; i < questionsFileNamesAdded.size(); i++) {
				// Obtenci�n de la cadena con el nombre del fichero de preguntas.
				if (configurationType == TestEditor.RANDOM || configurationType == TestEditor.RANDOM_WITH_RESTRICTIONS) {
					questionsFileName = questionsFileNamesAdded.get(i).toString().substring(0,
							questionsFileNamesAdded.get(i).toString().lastIndexOf(" ("));

					numberQuestionsToGet = new StringTokenizer(questionsFileNamesAdded.get(i).toString()
							.substring(questionsFileNamesAdded.get(i).toString().lastIndexOf("(") + 1,
									questionsFileNamesAdded.get(i).toString().lastIndexOf(")")));
					numQuestionsToGet = numberQuestionsToGet.nextToken(" ");
				} else
					questionsFileName = questionsFileNamesAdded.get(i).toString();

				process = i;

				try {
					Thread.sleep(100);
					// Llamada al m�todo de la clase padre que devuelve las preguntas
					// para
					// un fichero de preguntas.
					questionsVector = parent.getRandomRestrictionsQuestions(course.getName(),
							questionsFileName, Integer.valueOf(numQuestionsToGet).intValue(), question);
					if (questionsVector != null) {
						// Adici�n del nombre del fichero de preguntas y del Array de
						// objetos
						// Vector con sus preguntas.
						nameFileAndQuestions = new Vector();
						nameFileAndQuestions.add(questionsFileName);
						nameFileAndQuestions.add(questionsVector);

						// Adici�n del vector anterior al vector
						// questionsOfMultipleFiles
						questionsOfMultipleFiles.add(nameFileAndQuestions);
					}
				} catch (java.lang.InterruptedException e) {
					e.printStackTrace();
				}
			}
			questionsFileLoaded = true;
		}
	}

}
