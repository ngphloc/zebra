package es.uco.WOW.AppletTestEngine;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;

import es.uco.WOW.TestEditor.TestEditor;
import es.uco.WOW.Utils.Adaptive;
import es.uco.WOW.Utils.AnswersTestLogStudent;
import es.uco.WOW.Utils.EvalTestLogStudent;
import es.uco.WOW.Utils.Question;
import es.uco.WOW.Utils.QuestionsFileTest;
import es.uco.WOW.Utils.Student;
import es.uco.WOW.Utils.Test;
import es.uco.WOW.Utils.TestLogStudent;

/**
 * <p> Title: Wow! TestEditor </p> 
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p> Copyright: Copyright (c) 2008 </p>
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: AppletTestEngine 
 * FUNCTION: This class shows to the student an interface where the test questions
 * are showed so that the student can make the test. This interface will be 
 * connected to the server with servlet calls. These servlets evaluates the 
 * answered questions and returns the data of the next question to 
 * be answered by the student or the result of the test.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, changed by Loc Nguyen
 */
public class AppletTestEngine extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * These variables are relationed with the test execution
	 */
	private String urlGoAfterEndTest;

	private String courseName;

	private String testFileName;

	private String login;

	private String executionType;

	private URL codeBase = null;

	private String wowPath = "";

	private String iconTestEditorPath = "";

	private String itemsPath = "";

	private Test test = null;

	private Student student = null;

	private Question question = null;

	private Question questionInitial = null;

	private Question questionShow = null;

	private TestLogStudent testLogStudent = null;

	private EvalTestLogStudent evalTestLogStudent = null;

	/**
	 * Stores all the questions of the test
	 */
	private Vector questionVector = null; 
	/**
	 * Stores the questions already done of the test
	 */
	private Vector questionDoneVector = new Vector(); 
	/**
	 * Contains in each position the value of the informacion
	 * function for the question that is in the same place
	 * in the questionVector Vector
	 */
	private Vector questionInformationVector = null; 	
	
	private Vector questionInformationValuesVector = new Vector();

	private Vector questionFunctionSIVector = new Vector();

	private double irr = 99999;

	private double standardError = 99999;

	/**
	 * Stores the codes of the answers checked
	 * by the student
	 */
	private Vector codeAnswersCheckVector = new Vector(); 	
	
	/**
	 * Running type of the adaptive test
	 * (1, 2 o 3 parameters)
	 */
	private int irtModel = 3; 
	
	private Vector answersCorrectVector;

	private Vector questionsWithoutAnswerVector = new Vector();

	private String score = "";

	private String answerValue = "";

	/**
	 * The following variables are relationed
	 * with the graphical interface
	 */
	protected String lookAndFeelClassName = "";

	private boolean imageIconLoad = false;

	private int taskDone = 0;

	private Timer timerToAnswer = null;

	private TimeControl workerTimeControl = null;

	private RepaintProgressBar workerRepaintProgressBar = null;

	private BeginTest workerBeginTest = null;

	private GetInformationValues workerGetInformation = null;

	private CorrectQuestion workerCorrectQuestion = null;

	private GetQuestion workerGetQuestion = null;

	private EndTest workerEndTest = null;

	private int timeToAnswer = 0;

	private int timeToAnswerInit = 0;

	private boolean repetitionWithoutAnswer = false;

	private int contRepetitionWithoutAnswer = 0;

	private int sizeRepetitionWithoutAnswer = 0;

	private boolean stopJApplet = false;

	private boolean appletInit = false;

	private boolean endTest = false;

	private boolean endedTest = false;

	private static String ACTIVITY = "activity";

	private double theta = 99999;

	private double theta_estimated = 99999;

	private Adaptive adaptive = null;

	private JApplet jApplet;

	/**
	 * Graphical interface components
	 */
	private JPanel jPanelContent;
	private JPanel jPanelInfo;
	private JPanel jPanelConcept;
	private JPanel jPanelNumTotalQuestions;
	private JPanel jPanelTimeToAnswer;
	private JPanel jPanelIncorrectAnswerPenalize;
	private JPanel jPanelWithoutAnswerPenalize;
	private JPanel jPanelKnowledge;
	private JPanel jPanelQuestion;
	private JPanel jPanelNumberOfQuestion;
	private JPanel jPanelEnunciate;
	private JPanel jPanelImage;
	private JPanel jPanelAnswers;
	private JPanel[] jPanelAnswer;
	private JPanel[] jPanelExplanation;
	private JPanel jPanelButtonCorrectReset;
	private JPanel jPanelButtonExitEnd;
	private JPanel jPanelButtonNextQuestionShowScore;
	private JPanel jPanelTitle;
	private JPanel[] jPanelResultCorrection;
	private JPanel jPanelOtherCorrectAnswers;
	private JPanel[] jPanelOtherCorrectAnswer;
	private JPanel jPanelButton;
	private JPanel jPanelStatus;
	private JScrollPane jScrollPane;
	private JScrollPane jScrollPaneJListConcept;
	private JScrollPane jScrollPaneJTextAreaEnunciate;
	private JScrollPane[] jScrollPaneJTextAreaAnswer;
	private JScrollPane[] jScrollPaneJTextAreaExplanation;
	private JScrollPane[] jScrollPaneJTextAreaOtherCorrectAnswer;
	private JLabel jLabelConcept;
	private JLabel jLabelNumTotalQuestions1;
	private JLabel jLabelNumTotalQuestions2;
	private JLabel jLabelTimeToAnswer1;
	private JLabel jLabelTimeToAnswer2;
	private JLabel jLabelTimeToAnswer3;
	private JLabel jLabelIncorrectAnswerPenalize1;
	private JLabel jLabelIncorrectAnswerPenalize2;
	private JLabel jLabelWithoutAnswerPenalize1;
	private JLabel jLabelWithoutAnswerPenalize2;
	private JLabel jLabelKnowledge1;
	private JLabel jLabelKnowledge2;
	private JLabel jLabelNumberOfQuestion;
	private JLabel jLabelImage;
	private JLabel jLabelTitle;
	private JLabel[] jLabelResultCorrection;
	private JLabel jLabelChart;
	private JLabel jLabelCorrect;
	private JLabel jLabelIncorrect;
	private JLabel jLabelWithoutAnswer;
	private JLabel jLabelQuestionsDone;
	private JLabel jLabelScore;
	private JLabel jLabelProficiencyPrevious;
	private JLabel jLabelProficiency;
	private JLabel jLabelStandardError;
	private JLabel jLabelStatus;
	private JButton jButtonBeginTest;
	private JButton jButtonNextQuestion;
	private JButton jButtonShowScore;
	private JButton jButtonExitTest;
	private JButton jButtonCorrectQuestion;
	private JButton jButtonResetAnswers;
	private JButton jButtonEndTest;
	private JTextField jTextFieldConcept;
	private JTextField jTextFieldNumTotalQuestions;
	private JTextField jTextFieldTimeToAnswerMinutes;
	private JTextField jTextFieldTimeToAnswerSeconds;
	private JTextField jTextFieldIncorrectAnswerPenalize;
	private JTextField jTextFieldWithoutAnswerPenalize;
	private JTextField jTextFieldKnowledge;
	private JTextField jTextFieldTotalQuestions;
	private JTextField jTextFieldCorrect;
	private JTextField jTextFieldIncorrect;
	private JTextField jTextFieldWithoutAnswer;
	private JTextField jTextFieldQuestionsDone;
	private JTextField jTextFieldScore;
	private JTextField jTextFieldProficiencyPrevious;
	private JTextField jTextFieldProficiency;
	private JTextField jTextFieldStandardError;
	private JList jListConcept;
	private JTextArea jTextAreaEnunciate;
	private JTextArea[] jTextAreaAnswers;
	private JTextArea[] jTextAreaExplanations;
	private JTextArea[] jTextAreaOtherCorrectAnswers;
	private JCheckBox[] jCheckBoxAnswers;
	private JRadioButton[] jRadioButtonAnswers;
	private ButtonGroup jRadioButtonAnswersGroup;
	private JProgressBar jProgressBarTime;
	private JProgressBar jProgressBar;
	private BoxLayout boxLayoutPanelContent;
	private BoxLayout boxLayoutPanelInfo;
	private BoxLayout boxLayoutPanelQuestion;
	private BoxLayout boxLayoutPanelAnswers;
	private BoxLayout boxLayoutPanelOtherCorrectAnswers;
	private FlowLayout flowLayoutPanelConcept;
	private FlowLayout flowLayoutPanelNumTotalQuestions;
	private FlowLayout flowLayoutPanelTimeToAnswer;
	private FlowLayout flowLayoutPanelIncorrectAnswerPenalize;
	private FlowLayout flowLayoutPanelWithoutAnswerPenalize;
	private FlowLayout flowLayoutPanelKnowledge;
	private FlowLayout flowLayoutPanelNumberOfQuestion;
	private FlowLayout flowLayoutPanelEnunciate;
	private FlowLayout flowLayoutPanelImage;
	private FlowLayout[] flowLayoutPanelAnswer;
	private FlowLayout[] flowLayoutPanelExplanation;
	private FlowLayout flowLayoutPanelButtonCorrectReset;
	private FlowLayout flowLayoutPanelButtonExitEnd;
	private FlowLayout flowLayoutPanelButtonNextQuestionShowScore;
	private FlowLayout[] flowLayoutPanelResultCorrection;
	private FlowLayout[] flowLayoutPanelOtherCorrectAnswer;
	private FlowLayout flowLayoutPanelButton;
	private FlowLayout flowLayoutPanelStatus;
	private JFrame jFrameQuestionImage = null;
	private ImageIcon questionImageIcon = null;
	private ImageIcon questionImageIconTemp = null;
	private Image questionImage = null;
	private ImageIcon iconBeginTest32;
	private ImageIcon iconCancel32;
	private ImageIcon iconClear32;
	private ImageIcon iconExit32;
	private ImageIcon iconTimeToAnswer32;
	private ImageIcon iconCorrect32;
	private ImageIcon iconIncorrect32;
	private ImageIcon iconWithoutAnswer32;
	private ImageIcon iconStatistic32;
	private DlgScore dlgScore = null;
	private byte[] chart = null;
	

	public void init() {
		// Gets the parameters sent to this applet
		urlGoAfterEndTest = this.getParameter("urlGoAfterEndTest");
		courseName = this.getParameter("courseName");
		testFileName = this.getParameter("testFileName");
		login = this.getParameter("login");		

		// Gets the URL of this applet
		codeBase = this.getCodeBase();

		// Gets the system's path
		String path = this.getCodeBase().getPath();
		String pathttemp = path.substring(1, path.length());
		int index = pathttemp.indexOf("/");
		index++;

		// Gets the work folder of the TestEditor tool
		wowPath = path.substring(0, index);
		if (wowPath.equals("/AppletTestEngine")) {
			wowPath = "";
		}

		// Sets the JApplet look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
		} catch (Exception e) {
			e.printStackTrace();	
		}

		// Gets the path where the icons used by the application are
		String iconTestEditorAndItemsPath = this.getIconAndItemsPath();
		iconTestEditorPath = iconTestEditorAndItemsPath.substring(0,
				iconTestEditorAndItemsPath.indexOf("\n"));
		itemsPath = iconTestEditorAndItemsPath
				.substring(iconTestEditorAndItemsPath.indexOf("\n") + 1);
		itemsPath = itemsPath.substring(0, itemsPath.indexOf("\n"));
		
		// Gets a reference to himself
		this.jApplet = this;
	}

	
	public void start() {
		// Initializes the ImageIcon objects
		JImageIconInit();

		// Loads the information about the test that will be done
		loadTest();

		if (test != null) {
			executionType = test.getExecutionType();
			if (executionType.trim().equals(TestEditor.ADAPTIVE)) {
				irtModel = test.getIrtModel();
				adaptive = new Adaptive(irtModel);
				questionDoneVector = new Vector();
			}

			if (test.getShowInitialInfo())
				presentTest();
			else
				jButtonBeginTestActionPerformed();
		} else {
			JOptionPane.showMessageDialog(this,
					"Error obtaining the test data.\n"
					+ "It is possible that the test file doesn't exist\n"
					+ "or there was an error connecting with the server\n",
					"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	
	public void stop()	
	{
		if (repetitionWithoutAnswer) 
			questionVector.removeAllElements();

		if (questionVector != null && questionVector.isEmpty())
		{
			if (! endedTest)
			{
				stopJApplet = true;
				endTest();

				if (score == null)
				{
					JOptionPane.showMessageDialog(this,
							"ERROR: The test has not been able to be concluded."
									+ "\n" + "Try it later.",
							"Error when ending the test.", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	
	////////////////////////////////////////////////////////////////////////////////
	//////////////////// METHODS TO INITIALIZE THE JAPPLET COMPONENTS //////////////
	///////////////////// WHEN THE TEST INFORMATION IS BEEN LOADED  ////////////////
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method is called from the init method of this class
	 * and sets in the test variable a reference to a Test object,
	 * that contains the necessary information about the test
	 * that will be executed.
	 * @return Test object
	 */
	private Test loadTest() {
		try {
			test = null;
			// Calls the getTest method
			test = getTest(courseName, testFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test;
	}

	////////////////////////////////////////////////////////////////////////////////
	//////////////////// METHODS TO INITIALIZE THE JAPPLET COMPONENTS //////////////
	//////////////////// WHEN THE TEST IS BEING SHOWED TO THE STUDENT //////////////
	////////////////////////////////////////////////////////////////////////////////

	/** 
	 * Initializes the JApplet to show the general information about
	 * the test that will be executed
	 */
	private void presentTest() {
		try {
			// Initializes the components of the JApplet
			JLabelInitPresentTest();

			if (test.getTestType().equals("exam")) {
				JListInitPresentTest();
			}

			JTextFieldInitPresentTest();
			JButtonInitPresentTest();
			JProgressBarInit();
			JPanelInitPresentTest();
			JAppletInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Initializes the JLabel of the JApplet, allocating memory
	 * for them and sets their color, their content... 
	 */
	private void JLabelInitPresentTest()
	{
		jLabelConcept = new JLabel("This test evaluates the following concepts: ");
		jLabelConcept.setFont(new java.awt.Font("Dialog", 1, 16));

		jLabelNumTotalQuestions1 = new JLabel("The test contains ");
		jLabelNumTotalQuestions1.setFont(new java.awt.Font("Dialog", 1, 16));

		jLabelNumTotalQuestions2 = new JLabel(" questions");
		jLabelNumTotalQuestions2.setFont(new java.awt.Font("Dialog", 1, 16));

		int timeToAnswer = Integer.valueOf(test.getTimeOfAnswer()).intValue();
		if (timeToAnswer > 0) {
			jLabelTimeToAnswer1 = new JLabel(
					" Maximum time to answer each question: ");
			if (timeToAnswer >= 120)
				jLabelTimeToAnswer2 = new JLabel(" minutes and ");
			else
				jLabelTimeToAnswer2 = new JLabel(" minute and ");

			jLabelTimeToAnswer2.setFont(new java.awt.Font("Dialog", 1, 16));

			jLabelTimeToAnswer3 = new JLabel(" seconds ");
			jLabelTimeToAnswer3.setFont(new java.awt.Font("Dialog", 1, 16));
		}
		else {
			jLabelTimeToAnswer1 = new JLabel(
											"There is no limit in time to answer each question.");
		}

		jLabelTimeToAnswer1.setFont(new java.awt.Font("Dialog", 1, 16));

		if (test.getIncorrectAnswersPenalize() == true)
		{
			jLabelIncorrectAnswerPenalize1 = new JLabel(" Each ");

			jLabelIncorrectAnswerPenalize2 = new JLabel(" incorrect answers, "
					+ "1 correct answer is subtracted.");
			jLabelIncorrectAnswerPenalize2.setFont(new java.awt.Font("Dialog", 1, 16));
		}
		else
			jLabelIncorrectAnswerPenalize1 = new JLabel(
					" The incorrect answers are not penalized. ");

		jLabelIncorrectAnswerPenalize1
				.setFont(new java.awt.Font("Dialog", 1, 16));

		if (test.getWithoutAnswersPenalize() == true)
		{
			jLabelWithoutAnswerPenalize1 = new JLabel(" Each ");

			jLabelWithoutAnswerPenalize2 = new JLabel(
					" questions without answering, "
							+ "1 correct answer is subtracted.");
			jLabelWithoutAnswerPenalize2
					.setFont(new java.awt.Font("Dialog", 1, 16));
		}
		else
			jLabelWithoutAnswerPenalize1 = new JLabel(
					" The questions not answered are not penalized. ");

		jLabelWithoutAnswerPenalize1.setFont(new java.awt.Font("Dialog", 1, 16));

		jLabelKnowledge1 = new JLabel(" The test represents the ");
		jLabelKnowledge1.setFont(new java.awt.Font("Dialog", 1, 16));
		jLabelKnowledge2 = new JLabel("% of knowledge. ");
		jLabelKnowledge2.setFont(new java.awt.Font("Dialog", 1, 16));

		jLabelStatus = new JLabel("Done                                         ");
	}

	
	/**
	 * Initializes the JList of the JApplet, allocating memory 
	 * for them and setting their colour, content...
	 */
	private void JListInitPresentTest()
	{
		Vector conceptVector = new Vector();
		for (int i = 0; i < test.getConceptVector().size(); i++)
		{
			if (test.getConceptVector().get(i).toString()
					.indexOf(test.getCourse()) == 0)
				conceptVector.add(test.getConceptVector().get(i).toString()
						.substring(
								test.getConceptVector().get(i).toString().indexOf(
										test.getCourse())
										+ test.getCourse().length() + 1));
			else
				conceptVector.add(test.getConceptVector().get(i).toString());
		}

		jListConcept = new JList(conceptVector);
		jListConcept.setVisible(true);
		jListConcept.setAutoscrolls(true);
		jListConcept.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jListConcept.setVisibleRowCount(3);
		jListConcept.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	/**
	 * Initializes the JTextField of the JApplet, allocating memory for 
	 * them and setting their colour and content
	 */
	private void JTextFieldInitPresentTest()
	{
		if (test.getTestType().equals("activity"))
		{
			if (test.getConceptVector().firstElement().toString().indexOf(
					test.getCourse()) == 0)
				jTextFieldConcept = new JTextField(test.getConceptVector()
						.firstElement().toString().substring(
								test.getConceptVector().firstElement().toString()
										.indexOf(test.getCourse())
										+ test.getCourse().length() + 1));
			else
				jTextFieldConcept = new JTextField(test.getConceptVector()
						.firstElement().toString());

			jTextFieldConcept.setFocusable(false);
			jTextFieldConcept.setFont(new java.awt.Font("Dialog", 1, 14));
		}

		jTextFieldNumTotalQuestions = new JTextField(String.valueOf(test
				.getTotalQuestion()));
		jTextFieldNumTotalQuestions.setFocusable(false);
		jTextFieldNumTotalQuestions.setFont(new java.awt.Font("Dialog", 1, 14));

		int timeToAnswer = Integer.valueOf(test.getTimeOfAnswer()).intValue();

		if (timeToAnswer > 0)
		{
			if (timeToAnswer > 60)
			{
				jTextFieldTimeToAnswerMinutes = new JTextField(String
						.valueOf(timeToAnswer / 60));
				jTextFieldTimeToAnswerMinutes.setFocusable(false);
				jTextFieldTimeToAnswerMinutes.setFont(new java.awt.Font("Dialog", 1, 14));

				jTextFieldTimeToAnswerSeconds = new JTextField(String
						.valueOf(timeToAnswer % 60));
			}
			else
			{
				jTextFieldTimeToAnswerSeconds = new JTextField(test
						.getTimeOfAnswer());
			}
			jTextFieldTimeToAnswerSeconds.setFocusable(false);
			jTextFieldTimeToAnswerSeconds.setFont(new java.awt.Font("Dialog", 1, 14));
		}

		if (test.getIncorrectAnswersPenalize() == true)
		{
			jTextFieldIncorrectAnswerPenalize = new JTextField(String.valueOf(test
					.getIncorrectAnswersPenalizeNumber()));
			jTextFieldIncorrectAnswerPenalize.setFocusable(false);
			jTextFieldIncorrectAnswerPenalize.setFont(new java.awt.Font("Dialog", 1, 14));
		}

		if (test.getWithoutAnswersPenalize() == true)
		{
			jTextFieldWithoutAnswerPenalize = new JTextField(String.valueOf(test
					.getWithoutAnswersPenalizeNumber()));
			jTextFieldWithoutAnswerPenalize.setFocusable(false);
			jTextFieldWithoutAnswerPenalize.setFont(new java.awt.Font("Dialog", 1, 14));
		}

		jTextFieldKnowledge = new JTextField(test.getKnowledgePercentage());
		jTextFieldKnowledge.setFocusable(false);
		jTextFieldKnowledge.setFont(new java.awt.Font("Dialog", 1, 14));
	}


	/**
	 * Initializes the JButton objects of the JApplet, 
	 * setting their colour and content
	 */
	private void JButtonInitPresentTest()
	{
		// If the JButton icons has been correctly loaded, then 
		// the buttons are initialized with that icons.
		if (imageIconLoad == true)
		{
			jButtonBeginTest = new JButton("Begin Test", iconBeginTest32);
			jButtonExitTest = new JButton("Exit Test", iconCancel32);
		}
		else
		{
			jButtonBeginTest = new JButton("Begin Test");
			jButtonExitTest = new JButton("Exit Test");
		}

		jButtonBeginTest.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButtonBeginTestActionPerformed();
			}
		});

		jButtonExitTest.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButtonExitTestActionPerformed();
			}
		});
	}


	/**
	 * Initializes the JPanel objects of the JApplet,
	 * setting their colour and content
	 */
	private void JPanelInitPresentTest()
	{
		jPanelContent = new JPanel();
		boxLayoutPanelContent = new BoxLayout(jPanelContent, BoxLayout.Y_AXIS);
		jPanelContent.setLayout(boxLayoutPanelContent);
		jPanelContent
				.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jScrollPane = new JScrollPane(jPanelContent,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK, 3));
		jScrollPane.setBackground(SystemColor.BLACK);

		jPanelConcept = new JPanel();
		flowLayoutPanelConcept = new FlowLayout(FlowLayout.CENTER);
		jPanelConcept.setLayout(flowLayoutPanelConcept);
		jPanelConcept.setBackground(SystemColor.WHITE);
		jPanelConcept
				.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jLabelConcept.setAlignmentX(CENTER_ALIGNMENT);
		jLabelConcept.setAlignmentY(CENTER_ALIGNMENT);

		if (test.getTestType().equals("exam"))
		{
			jListConcept.setAlignmentX(CENTER_ALIGNMENT);
			jListConcept.setAlignmentY(CENTER_ALIGNMENT);

			jScrollPaneJListConcept = new JScrollPane(jListConcept,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPaneJListConcept.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));
			jScrollPaneJListConcept.setBackground(SystemColor.BLACK);
		}
		else
		{
			if (test.getTestType().equals("activity"))
			{
				jTextFieldConcept.setAlignmentX(CENTER_ALIGNMENT);
				jTextFieldConcept.setAlignmentY(CENTER_ALIGNMENT);
			}
		}

		jPanelConcept.add(jLabelConcept);

		if (test.getTestType().equals("exam"))
			jPanelConcept.add(jScrollPaneJListConcept);
		else
		{
			if (test.getTestType().equals("activity"))
				jPanelConcept.add(jTextFieldConcept);
		}

		jPanelNumTotalQuestions = new JPanel();
		flowLayoutPanelNumTotalQuestions = new FlowLayout(FlowLayout.CENTER);
		jPanelNumTotalQuestions.setLayout(flowLayoutPanelNumTotalQuestions);
		jPanelNumTotalQuestions.setBackground(SystemColor.WHITE);
		jPanelNumTotalQuestions.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK));

		jLabelNumTotalQuestions1.setAlignmentX(CENTER_ALIGNMENT);
		jLabelNumTotalQuestions2.setAlignmentX(CENTER_ALIGNMENT);
		jTextFieldNumTotalQuestions.setAlignmentX(CENTER_ALIGNMENT);

		jLabelNumTotalQuestions1.setAlignmentY(CENTER_ALIGNMENT);
		jLabelNumTotalQuestions2.setAlignmentY(CENTER_ALIGNMENT);
		jTextFieldNumTotalQuestions.setAlignmentY(CENTER_ALIGNMENT);

		jPanelNumTotalQuestions.add(jLabelNumTotalQuestions1);
		jPanelNumTotalQuestions.add(jTextFieldNumTotalQuestions);
		jPanelNumTotalQuestions.add(jLabelNumTotalQuestions2);

		jPanelTimeToAnswer = new JPanel();
		flowLayoutPanelTimeToAnswer = new FlowLayout(FlowLayout.CENTER);
		jPanelTimeToAnswer.setLayout(flowLayoutPanelTimeToAnswer);
		jPanelTimeToAnswer.setBackground(SystemColor.WHITE);
		jPanelTimeToAnswer.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK));

		jLabelTimeToAnswer1.setAlignmentX(CENTER_ALIGNMENT);
		jLabelTimeToAnswer1.setAlignmentY(CENTER_ALIGNMENT);

		int timeToAnswer = Integer.valueOf(test.getTimeOfAnswer()).intValue();
		if (timeToAnswer > 0)
		{
			jTextFieldTimeToAnswerSeconds.setAlignmentX(CENTER_ALIGNMENT);
			jLabelTimeToAnswer3.setAlignmentX(CENTER_ALIGNMENT);

			jTextFieldTimeToAnswerSeconds.setAlignmentY(CENTER_ALIGNMENT);
			jLabelTimeToAnswer3.setAlignmentY(CENTER_ALIGNMENT);

			if (timeToAnswer > 60)
			{
				jTextFieldTimeToAnswerMinutes.setAlignmentX(CENTER_ALIGNMENT);
				jLabelTimeToAnswer2.setAlignmentX(CENTER_ALIGNMENT);

				jTextFieldTimeToAnswerMinutes.setAlignmentY(CENTER_ALIGNMENT);
				jLabelTimeToAnswer2.setAlignmentY(CENTER_ALIGNMENT);
			}
		}

		jPanelTimeToAnswer.add(jLabelTimeToAnswer1);

		if (timeToAnswer > 0)
		{
			if (timeToAnswer > 60)
			{
				jPanelTimeToAnswer.add(jTextFieldTimeToAnswerMinutes);
				jPanelTimeToAnswer.add(jLabelTimeToAnswer2);
			}

			jPanelTimeToAnswer.add(jTextFieldTimeToAnswerSeconds);
			jPanelTimeToAnswer.add(jLabelTimeToAnswer3);
		}

		jPanelIncorrectAnswerPenalize = new JPanel();
		flowLayoutPanelIncorrectAnswerPenalize = new FlowLayout(FlowLayout.CENTER);
		jPanelIncorrectAnswerPenalize
				.setLayout(flowLayoutPanelIncorrectAnswerPenalize);
		jPanelIncorrectAnswerPenalize.setBackground(SystemColor.WHITE);
		jPanelIncorrectAnswerPenalize.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK));

		jLabelIncorrectAnswerPenalize1.setAlignmentX(CENTER_ALIGNMENT);
		jLabelIncorrectAnswerPenalize1.setAlignmentY(CENTER_ALIGNMENT);

		if (test.getIncorrectAnswersPenalize() == true)
		{
			jTextFieldIncorrectAnswerPenalize.setAlignmentX(CENTER_ALIGNMENT);
			jLabelIncorrectAnswerPenalize2.setAlignmentX(CENTER_ALIGNMENT);

			jTextFieldIncorrectAnswerPenalize.setAlignmentY(CENTER_ALIGNMENT);
			jLabelIncorrectAnswerPenalize2.setAlignmentY(CENTER_ALIGNMENT);
		}

		jPanelIncorrectAnswerPenalize.add(jLabelIncorrectAnswerPenalize1);

		if (test.getIncorrectAnswersPenalize() == true)
		{
			jPanelIncorrectAnswerPenalize.add(jTextFieldIncorrectAnswerPenalize);
			jPanelIncorrectAnswerPenalize.add(jLabelIncorrectAnswerPenalize2);
		}

		jPanelWithoutAnswerPenalize = new JPanel();
		flowLayoutPanelWithoutAnswerPenalize = new FlowLayout(FlowLayout.CENTER);
		jPanelWithoutAnswerPenalize
				.setLayout(flowLayoutPanelWithoutAnswerPenalize);
		jPanelWithoutAnswerPenalize.setBackground(SystemColor.WHITE);
		jPanelWithoutAnswerPenalize.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK));

		jLabelWithoutAnswerPenalize1.setAlignmentX(CENTER_ALIGNMENT);
		jLabelWithoutAnswerPenalize1.setAlignmentY(CENTER_ALIGNMENT);

		if (test.getWithoutAnswersPenalize() == true)
		{
			jTextFieldWithoutAnswerPenalize.setAlignmentX(CENTER_ALIGNMENT);
			jLabelWithoutAnswerPenalize2.setAlignmentX(CENTER_ALIGNMENT);

			jTextFieldWithoutAnswerPenalize.setAlignmentY(CENTER_ALIGNMENT);
			jLabelWithoutAnswerPenalize2.setAlignmentY(CENTER_ALIGNMENT);
		}

		jPanelWithoutAnswerPenalize.add(jLabelWithoutAnswerPenalize1);

		if (test.getWithoutAnswersPenalize() == true)
		{
			jPanelWithoutAnswerPenalize.add(jTextFieldWithoutAnswerPenalize);
			jPanelWithoutAnswerPenalize.add(jLabelWithoutAnswerPenalize2);
		}

		jPanelKnowledge = new JPanel();
		flowLayoutPanelKnowledge = new FlowLayout(FlowLayout.CENTER);
		jPanelKnowledge.setLayout(flowLayoutPanelKnowledge);
		jPanelKnowledge.setBackground(SystemColor.WHITE);
		jPanelKnowledge.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK));

		jLabelKnowledge1.setAlignmentX(CENTER_ALIGNMENT);
		jTextFieldKnowledge.setAlignmentX(CENTER_ALIGNMENT);
		jLabelKnowledge2.setAlignmentX(CENTER_ALIGNMENT);

		jLabelKnowledge1.setAlignmentY(CENTER_ALIGNMENT);
		jTextFieldKnowledge.setAlignmentY(CENTER_ALIGNMENT);
		jLabelKnowledge2.setAlignmentY(CENTER_ALIGNMENT);

		jPanelKnowledge.add(jLabelKnowledge1);
		jPanelKnowledge.add(jTextFieldKnowledge);
		jPanelKnowledge.add(jLabelKnowledge2);

		jPanelInfo = new JPanel();
		boxLayoutPanelInfo = new BoxLayout(jPanelInfo, BoxLayout.Y_AXIS);
		jPanelInfo.setLayout(boxLayoutPanelInfo);
		jPanelInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK),
				"General information about the test", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14)));

		jPanelConcept.setAlignmentX(CENTER_ALIGNMENT);
		jPanelNumTotalQuestions.setAlignmentX(CENTER_ALIGNMENT);
		jPanelTimeToAnswer.setAlignmentX(CENTER_ALIGNMENT);
		jPanelIncorrectAnswerPenalize.setAlignmentX(CENTER_ALIGNMENT);
		jPanelWithoutAnswerPenalize.setAlignmentX(CENTER_ALIGNMENT);
		jPanelKnowledge.setAlignmentX(CENTER_ALIGNMENT);

		jPanelConcept.setAlignmentY(CENTER_ALIGNMENT);
		jPanelNumTotalQuestions.setAlignmentY(CENTER_ALIGNMENT);
		jPanelTimeToAnswer.setAlignmentY(CENTER_ALIGNMENT);
		jPanelIncorrectAnswerPenalize.setAlignmentY(CENTER_ALIGNMENT);
		jPanelWithoutAnswerPenalize.setAlignmentY(CENTER_ALIGNMENT);
		jPanelKnowledge.setAlignmentY(CENTER_ALIGNMENT);

		jPanelInfo.add(jPanelConcept);
		jPanelInfo.add(Box.createVerticalStrut(6));
		jPanelInfo.add(jPanelNumTotalQuestions);
		jPanelInfo.add(Box.createVerticalStrut(6));
		jPanelInfo.add(jPanelTimeToAnswer);
		jPanelInfo.add(Box.createVerticalStrut(6));
		jPanelInfo.add(jPanelIncorrectAnswerPenalize);
		jPanelInfo.add(Box.createVerticalStrut(6));
		jPanelInfo.add(jPanelWithoutAnswerPenalize);
		jPanelInfo.add(Box.createVerticalStrut(6));
		jPanelInfo.add(jPanelKnowledge);

		jPanelButton = new JPanel();
		flowLayoutPanelButton = new FlowLayout(FlowLayout.CENTER);
		jPanelButton.setLayout(flowLayoutPanelButton);

		jPanelButton.add(jButtonBeginTest);
		jPanelButton.add(jButtonExitTest);

		jPanelStatus = new JPanel();
		flowLayoutPanelStatus = new FlowLayout(FlowLayout.LEFT);
		jPanelStatus.setLayout(flowLayoutPanelStatus);
		jPanelStatus.setBorder(BorderFactory.createLoweredBevelBorder());

		jLabelStatus.setAlignmentX(LEFT_ALIGNMENT);
		jProgressBar.setAlignmentX(LEFT_ALIGNMENT);

		jPanelStatus.add(jLabelStatus);
		jPanelStatus.add(Box.createHorizontalStrut(10));
		jPanelStatus.add(jProgressBar);

		jPanelContent.add(jPanelInfo);
		jPanelContent.add(jPanelButton);
		jPanelContent.add(jPanelStatus);
		jPanelContent.add(Box.createVerticalStrut(5));
	}

	////////////////////////////////////////////////////////////////////////////////
	//////////////////// METHODS TO INITIALIZE THE JAPPLET COMPONENTS //////////////
	//////////////////////////////// TO SHOW A QUESTION  ///////////////////////////
	////////////////////////////////////////////////////////////////////////////////
		
	/**
	 * Initializes the JLabel objects of the JApplet,
	 * allocating memory for them and setting their content.
	 */
	private void JLabelInitShowQuestion()
	{
		if (repetitionWithoutAnswer == false)
			jLabelNumberOfQuestion = new JLabel("Question "
					+ String.valueOf(evalTestLogStudent.getQuestionsDone() + 1)
					+ " " + "of "
					+ String.valueOf(evalTestLogStudent.getTotalQuestion()));
		else
			jLabelNumberOfQuestion = new JLabel(
					"Repeat questions without answering. Question "
							+ String.valueOf(contRepetitionWithoutAnswer) 
							+ " of " + String.valueOf(sizeRepetitionWithoutAnswer));

		jLabelNumberOfQuestion.setFont(new java.awt.Font("Dialog", 1, 18));

		if (question.getPathImage().trim().equals("") == false)
		{
			jLabelImage = new JLabel();
			jLabelImage.setFocusable(true);
			jLabelImage.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));
			jLabelImage
					.setToolTipText("Make double click on the image to see it in the original size.");

			try
			{
				// Obtain the bytes from the image file in the server
				String petitionQuestionImage = wowPath + itemsPath
						+ question.getPathImage().trim();

				URL urlQuestionImage = new URL(codeBase, petitionQuestionImage);

				questionImageIconTemp = new ImageIcon(urlQuestionImage);
				questionImage = questionImageIconTemp.getImage();

				// Sets the size of the image
				if (questionImageIconTemp.getIconWidth() >= questionImageIconTemp
						.getIconHeight())
				{
					if (questionImageIconTemp.getIconWidth() > 500)
					{
						double scale = questionImageIconTemp.getIconWidth() / 500.0;

						questionImage = questionImage.getScaledInstance(500, Integer
								.valueOf(
										String.valueOf(Math.round(questionImageIconTemp
												.getIconHeight()
												/ scale))).intValue(),
								Image.SCALE_AREA_AVERAGING);
					}
				}
				else
				{
					if (questionImageIconTemp.getIconHeight() > 500)
					{
						double scale = questionImageIconTemp.getIconHeight() / 500.0;

						questionImage = questionImage.getScaledInstance(Integer
								.valueOf(
										String.valueOf(Math.round(questionImageIconTemp
												.getIconWidth()
												/ scale))).intValue(), 500,
								Image.SCALE_AREA_AVERAGING);
					}
				}

				questionImageIcon = new ImageIcon(questionImage);

				jLabelImage.setIcon(questionImageIcon);
				jLabelImage.setFocusable(true);
				jLabelImage.setBorder(BorderFactory
						.createLineBorder(SystemColor.BLACK));

				jLabelImage.addMouseListener(new java.awt.event.MouseAdapter()
				{
					public void mouseClicked(MouseEvent e)
					{
						if (e.getClickCount() == 2)
						{
							// Hides the possible JFrame opened before
							if (jFrameQuestionImage != null)
							{
								jFrameQuestionImage.setVisible(false);
								jFrameQuestionImage.dispose();
								jFrameQuestionImage = null;
							}

							JLabel jLabelImageFrameQuestionImage = new JLabel(
									questionImageIconTemp);

							// Sets the appearance of the JFrame to be showed
							jFrameQuestionImage = new JFrame("Image of the question.");
							jFrameQuestionImage.setSize(640, 440);
							jFrameQuestionImage.setLocation(0, 0);
							jFrameQuestionImage.setResizable(true);
							jFrameQuestionImage.setVisible(true);
							JScrollPane jScrollPane = new JScrollPane(
									jLabelImageFrameQuestionImage,
									JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
									JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

							jFrameQuestionImage.getContentPane().add(jScrollPane);

							jFrameQuestionImage.pack();
						}
					}
				});
			}
			catch (java.net.MalformedURLException e)
			{
				jLabelImage = new JLabel("ERROR TO LOAD IMAGE: " + e.getMessage());
				e.printStackTrace();
			}
		}

		int timeToAnswer = Integer.valueOf(test.getTimeOfAnswer()).intValue();
		if (timeToAnswer > 0)
		{
			jLabelTimeToAnswer1 = new JLabel(" Time to answer: ");
			jLabelTimeToAnswer1.setIcon(iconTimeToAnswer32);
			jLabelTimeToAnswer1.setFont(new java.awt.Font("Dialog", 1, 12));

			if (timeToAnswer >= 120)
				jLabelTimeToAnswer2 = new JLabel(" minutes and ");
			else
				jLabelTimeToAnswer2 = new JLabel(" minute and ");

			jLabelTimeToAnswer2.setFont(new java.awt.Font("Dialog", 1, 12));

			jLabelTimeToAnswer3 = new JLabel(" seconds ");
			jLabelTimeToAnswer3.setFont(new java.awt.Font("Dialog", 1, 12));
		}

		jLabelStatus = new JLabel("Done                                         ");
	}

	
	/**
	 * Initializes the JTextArea objects of the JApplet object,
	 * allocating memory for them and setting their colour, their content....
	 */
	private void JTextAreaInitShowQuestion()
	{
		jTextAreaEnunciate = new JTextArea(question.getEnunciate(), 2, 40);
		jTextAreaEnunciate.setLineWrap(true);
		jTextAreaEnunciate.setWrapStyleWord(true);
		jTextAreaEnunciate.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextAreaEnunciate.setBackground(SystemColor.WHITE);
		jTextAreaEnunciate.setBorder(BorderFactory.createLineBorder(SystemColor.black));
		jTextAreaEnunciate.setFocusable(false);

		// The following JtextArea objects will contain the data
		// of the questions and their explanations
		jTextAreaAnswers = new JTextArea[question.getCodeAnswers().size()];

		for (int i = 0; i < question.getCodeAnswers().size(); i++) {
			String answer = question.getTextAnswers().get(i).toString();
			jTextAreaAnswers[i] = new JTextArea(answer, 2, 40);
			jTextAreaAnswers[i].setLineWrap(true);
			jTextAreaAnswers[i].setWrapStyleWord(true);
			jTextAreaAnswers[i].setFont(new java.awt.Font("Dialog", 0, 12));
			jTextAreaAnswers[i].setBackground(SystemColor.WHITE);
			jTextAreaAnswers[i].setBorder(BorderFactory.createLineBorder(SystemColor.black));
			jTextAreaAnswers[i].setFocusable(false);
		}
	}

	
	/**
	 * Initializes the JTextField objects of the JApplet object,
	 * setting their colour, content...
	 */
	private void JTextFieldInitShowQuestion()
	{
		timeToAnswerInit = Integer.valueOf(test.getTimeOfAnswer()).intValue();
		if (timeToAnswerInit > 0)
		{
			if (timeToAnswerInit > 60)
			{
				jTextFieldTimeToAnswerMinutes = new JTextField(String
						.valueOf(timeToAnswerInit / 60), 3);
				jTextFieldTimeToAnswerMinutes.setFocusable(false);
				jTextFieldTimeToAnswerMinutes.setFont(new java.awt.Font("Dialog", 1, 14));
				jTextFieldTimeToAnswerMinutes.setBorder(BorderFactory
						.createLineBorder(SystemColor.BLACK));

				jTextFieldTimeToAnswerSeconds = new JTextField(String
						.valueOf(timeToAnswerInit % 60), 4);
			}
			else
			{
				jTextFieldTimeToAnswerSeconds = new JTextField(test
						.getTimeOfAnswer(), 4);
			}

			jTextFieldTimeToAnswerSeconds.setFocusable(false);
			jTextFieldTimeToAnswerSeconds.setFont(new java.awt.Font("Dialog", 1, 14));
			jTextFieldTimeToAnswerSeconds.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));
		}
	}

	
	/**
	 * Initializes the JProgressBar object of the JDialog object
	 */
	private void JProgressBarTimeInit()
	{
		jProgressBarTime = new JProgressBar(JProgressBar.HORIZONTAL);
		jProgressBarTime.setBorderPainted(true);
		jProgressBarTime.setStringPainted(true);
		jProgressBarTime.setSize(100, 20);
		jProgressBarTime.setVisible(true);

		if (Integer.valueOf(test.getTimeOfAnswer()).intValue() > 0)
		{
			jProgressBarTime.setMinimum(0);
			jProgressBarTime.setMaximum(Integer.valueOf(test.getTimeOfAnswer())
					.intValue());
		}
	}

	
	/**
	 * Intializes the JCheckBox objects of the JApplet object, 
	 * setting their colour, content....
	 */
	private void JCheckBoxInitShowQuestion()
	{
		// The JCheckBox array contains the data of the answers
		// for the questions
		jCheckBoxAnswers = new JCheckBox[question.getCodeAnswers().size()];

		for (int i = 0; i < question.getCodeAnswers().size(); i++)
		{
			final int j = i;
			jCheckBoxAnswers[i] = new JCheckBox();
			jCheckBoxAnswers[i].setFont(new java.awt.Font("Dialog", 0, 16));

			jCheckBoxAnswers[i]
					.addChangeListener(new javax.swing.event.ChangeListener()
					{
						public void stateChanged(ChangeEvent e)
						{
							if (jCheckBoxAnswers[j].isSelected()) {
								jTextAreaAnswers[j].setBackground(SystemColor.BLUE);
								jTextAreaAnswers[j].setFont(new java.awt.Font("Dialog", 1, 12));
								jTextAreaAnswers[j].paint(jTextAreaAnswers[j]
										.getGraphics());
							} else {
								jTextAreaAnswers[j].setBackground(SystemColor.WHITE);
								jTextAreaAnswers[j].setFont(new java.awt.Font("Dialog", 0, 12));
								jTextAreaAnswers[j].paint(jTextAreaAnswers[j]
										.getGraphics());
							}
						}
					});
		}
	}


	/**
	 * Initializes the JRadioButton object of the JApplet object,
	 * setting their colour,content...
	 */
	private void JRadioButtonInitShowQuestion()
	{
		jRadioButtonAnswers = new JRadioButton[question.getCodeAnswers().size()];

		for (int i = 0; i < question.getCodeAnswers().size(); i++)
		{
			final int j = i;

			jRadioButtonAnswers[i] = new JRadioButton();
			jRadioButtonAnswers[i].setFont(new java.awt.Font("Dialog", 0, 16));

			jRadioButtonAnswers[i]
					.addChangeListener(new javax.swing.event.ChangeListener()
					{
						public void stateChanged(ChangeEvent e)
						{
							if (jRadioButtonAnswers[j].isSelected()) {
								jTextAreaAnswers[j].setBackground(SystemColor.BLUE);
								jTextAreaAnswers[j].setFont(new java.awt.Font("Dialog", 1, 12));
								jTextAreaAnswers[j].paint(jTextAreaAnswers[j]
										.getGraphics());
							} else {
								jTextAreaAnswers[j].setBackground(SystemColor.WHITE);
								jTextAreaAnswers[j].setFont(new java.awt.Font("Dialog", 0, 12));
								jTextAreaAnswers[j].paint(jTextAreaAnswers[j]
										.getGraphics());
							}
						}
					});
		}
		jRadioButtonAnswersGroup = new ButtonGroup();

		for (int i = 0; i < question.getCodeAnswers().size(); i++)
			jRadioButtonAnswersGroup.add(jRadioButtonAnswers[i]);
	}

	
	/**
	 * Initializes the JButton objects of the JApplet object,
	 * setting their colour, content....
	 */
	private void JButtonInitShowQuestion()
	{
		// If the icons of the JButton has been correctly obtained,
		// they will be loaded in the JButton, otherwise not.
		if (imageIconLoad == true) {
			jButtonCorrectQuestion = new JButton("Correct Question", iconBeginTest32);
			jButtonResetAnswers = new JButton("Reset", iconClear32);
			jButtonExitTest = new JButton("Exit", iconExit32);
			jButtonEndTest = new JButton("End Test", iconCancel32);
		} else {
			jButtonCorrectQuestion = new JButton("Correct Question");
			jButtonResetAnswers = new JButton("Reset");
			jButtonExitTest = new JButton("Exit");
			jButtonEndTest = new JButton("End Test");
		}
		
		// Sets the funcionality of the JButtons
		jButtonCorrectQuestion
				.addActionListener(new java.awt.event.ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (executionType.trim().equals(TestEditor.ADAPTIVE))
							jButtonAdaptiveCorrectQuestionActionPerformed();
						else
							jButtonClassicCorrectQuestionActionPerformed();
					}
				});

		jButtonResetAnswers.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (question.getAnswersCorrect().size() > 1)
				{
					for (int i = 0; i < question.getCodeAnswers().size(); i++)
						jCheckBoxAnswers[i].setSelected(false);
				}
				else
				{
					for (int i = 0; i < question.getCodeAnswers().size(); i++)
					{
						jRadioButtonAnswersGroup.remove(jRadioButtonAnswers[i]);
						jRadioButtonAnswers[i].setSelected(false);
						jRadioButtonAnswersGroup.add(jRadioButtonAnswers[i]);
					}
				}
			}
		});

		jButtonExitTest.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButtonExitTestActionPerformed();
			}
		});

		jButtonEndTest.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButtonEndTestActionPerformed();
			}
		});
	}

	
	/**
	 * Initializes the JPanel objects of the JApplet, setting
	 * their colour, their content...
	 */
	private void JPanelInitShowQuestion()
	{
		jPanelContent = new JPanel();
		boxLayoutPanelContent = new BoxLayout(jPanelContent, BoxLayout.Y_AXIS);
		jPanelContent.setLayout(boxLayoutPanelContent);
		jPanelContent
				.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jScrollPane = new JScrollPane(jPanelContent,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK, 3));
		jScrollPane.setBackground(SystemColor.BLACK);

		jPanelNumberOfQuestion = new JPanel();
		flowLayoutPanelNumberOfQuestion = new FlowLayout(FlowLayout.CENTER);
		jPanelNumberOfQuestion.setLayout(flowLayoutPanelNumberOfQuestion);
		jPanelNumberOfQuestion.setBackground(SystemColor.WHITE);
		jPanelNumberOfQuestion.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK));

		jLabelNumberOfQuestion.setAlignmentX(CENTER_ALIGNMENT);
		jLabelNumberOfQuestion.setAlignmentY(CENTER_ALIGNMENT);

		jPanelNumberOfQuestion.add(jLabelNumberOfQuestion);

		jPanelEnunciate = new JPanel();
		flowLayoutPanelEnunciate = new FlowLayout(FlowLayout.CENTER);
		jPanelEnunciate.setLayout(flowLayoutPanelEnunciate);
		jPanelEnunciate.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Enunciate",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Dialog", 0, 14)));

		jTextAreaEnunciate.setAlignmentX(CENTER_ALIGNMENT);
		jTextAreaEnunciate.setAlignmentY(CENTER_ALIGNMENT);

		jScrollPaneJTextAreaEnunciate = new JScrollPane(jTextAreaEnunciate,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneJTextAreaEnunciate.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK));

		jPanelEnunciate.add(jScrollPaneJTextAreaEnunciate);

		if (question.getPathImage().trim().equals("") == false)
		{
			jPanelImage = new JPanel();
			flowLayoutPanelImage = new FlowLayout(FlowLayout.CENTER);
			jPanelImage.setLayout(flowLayoutPanelImage);
			jPanelImage.setBorder(BorderFactory.createTitledBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK), "Image",
					TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
					new java.awt.Font("Dialog", 0, 14)));

			jLabelImage.setAlignmentX(CENTER_ALIGNMENT);
			jLabelImage.setAlignmentY(CENTER_ALIGNMENT);

			jPanelImage.add(jLabelImage);
		}

		if (Integer.valueOf(test.getTimeOfAnswer()).intValue() > 0)
		{
			jPanelTimeToAnswer = new JPanel();
			flowLayoutPanelTimeToAnswer = new FlowLayout(FlowLayout.CENTER);
			jPanelTimeToAnswer.setLayout(flowLayoutPanelTimeToAnswer);
			jPanelTimeToAnswer.setBackground(SystemColor.WHITE);
			jPanelTimeToAnswer.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));

			if (Integer.valueOf(test.getTimeOfAnswer()).intValue() > 60)
			{
				jTextFieldTimeToAnswerMinutes.setAlignmentX(CENTER_ALIGNMENT);
				jLabelTimeToAnswer2.setAlignmentX(CENTER_ALIGNMENT);
			}

			jTextFieldTimeToAnswerSeconds.setAlignmentX(CENTER_ALIGNMENT);
			jLabelTimeToAnswer3.setAlignmentX(CENTER_ALIGNMENT);

			jPanelTimeToAnswer.add(jLabelTimeToAnswer1);

			if (Integer.valueOf(test.getTimeOfAnswer()).intValue() > 60)
			{
				jPanelTimeToAnswer.add(jTextFieldTimeToAnswerMinutes);
				jPanelTimeToAnswer.add(jLabelTimeToAnswer2);
			}
			jPanelTimeToAnswer.add(jTextFieldTimeToAnswerSeconds);
			jPanelTimeToAnswer.add(jLabelTimeToAnswer3);

			jPanelTimeToAnswer.add(jProgressBarTime);
		}

		jPanelAnswer = new JPanel[question.getCodeAnswers().size()];
		flowLayoutPanelAnswer = new FlowLayout[question.getCodeAnswers().size()];
		jScrollPaneJTextAreaAnswer = new JScrollPane[question.getCodeAnswers()
				.size()];

		for (int i = 0; i < question.getCodeAnswers().size(); i++)
		{
			jPanelAnswer[i] = new JPanel();
			flowLayoutPanelAnswer[i] = new FlowLayout(FlowLayout.CENTER);
			jPanelAnswer[i].setLayout(flowLayoutPanelAnswer[i]);

			if (question.getAnswersCorrect().size() > 1)
			{
				jCheckBoxAnswers[i].setAlignmentX(CENTER_ALIGNMENT);
				jCheckBoxAnswers[i].setAlignmentY(CENTER_ALIGNMENT);

				jPanelAnswer[i].add(jCheckBoxAnswers[i]);
			}
			else
			{
				jRadioButtonAnswers[i].setAlignmentX(CENTER_ALIGNMENT);
				jRadioButtonAnswers[i].setAlignmentY(CENTER_ALIGNMENT);

				jPanelAnswer[i].add(jRadioButtonAnswers[i]);
			}

			jScrollPaneJTextAreaAnswer[i] = new JScrollPane(jTextAreaAnswers[i],
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPaneJTextAreaAnswer[i].setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));

			jPanelAnswer[i].add(jScrollPaneJTextAreaAnswer[i]);
		}

		jPanelAnswers = new JPanel();
		boxLayoutPanelAnswers = new BoxLayout(jPanelAnswers, BoxLayout.Y_AXIS);
		jPanelAnswers.setLayout(boxLayoutPanelAnswers);
		jPanelAnswers.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Answers", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14)));

		for (int i = 0; i < question.getCodeAnswers().size(); i++)
		{
			jPanelAnswer[i].setAlignmentX(CENTER_ALIGNMENT);

			jPanelAnswers.add(jPanelAnswer[i]);
		}

		jPanelQuestion = new JPanel();
		boxLayoutPanelQuestion = new BoxLayout(jPanelQuestion, BoxLayout.Y_AXIS);
		jPanelQuestion.setLayout(boxLayoutPanelQuestion);

		jPanelNumberOfQuestion.setAlignmentX(CENTER_ALIGNMENT);
		jPanelEnunciate.setAlignmentX(CENTER_ALIGNMENT);

		if (question.getPathImage().trim().equals("") == false)
			jPanelImage.setAlignmentX(CENTER_ALIGNMENT);

		jPanelAnswers.setAlignmentX(CENTER_ALIGNMENT);

		jPanelNumberOfQuestion.setAlignmentY(CENTER_ALIGNMENT);
		jPanelEnunciate.setAlignmentY(CENTER_ALIGNMENT);

		if (question.getPathImage().trim().equals("") == false)
			jPanelImage.setAlignmentY(CENTER_ALIGNMENT);

		if (Integer.valueOf(test.getTimeOfAnswer()).intValue() > 0)
			jPanelTimeToAnswer.setAlignmentX(CENTER_ALIGNMENT);

		jPanelAnswers.setAlignmentY(CENTER_ALIGNMENT);

		jPanelQuestion.add(jPanelNumberOfQuestion);
		jPanelQuestion.add(Box.createVerticalStrut(6));
		jPanelQuestion.add(jPanelEnunciate);
		jPanelQuestion.add(Box.createVerticalStrut(6));

		if (question.getPathImage().trim().equals("") == false)
		{
			jPanelQuestion.add(jPanelImage);
			jPanelQuestion.add(Box.createVerticalStrut(6));
		}

		if (Integer.valueOf(test.getTimeOfAnswer()).intValue() > 0)
		{
			jPanelQuestion.add(jPanelTimeToAnswer);
			jPanelQuestion.add(Box.createVerticalStrut(6));
		}

		jPanelQuestion.add(jPanelAnswers);

		jPanelButtonCorrectReset = new JPanel();
		flowLayoutPanelButtonCorrectReset = new FlowLayout(FlowLayout.CENTER);
		jPanelButtonCorrectReset.setLayout(flowLayoutPanelButtonCorrectReset);
		jPanelButtonCorrectReset.setBackground(SystemColor.WHITE);
		jPanelButtonCorrectReset.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK));

		jButtonCorrectQuestion.setAlignmentX(CENTER_ALIGNMENT);
		jButtonCorrectQuestion.setAlignmentY(CENTER_ALIGNMENT);
		jButtonResetAnswers.setAlignmentX(CENTER_ALIGNMENT);
		jButtonResetAnswers.setAlignmentY(CENTER_ALIGNMENT);

		jPanelButtonCorrectReset.add(jButtonCorrectQuestion);
		jPanelButtonCorrectReset.add(jButtonResetAnswers);

		jPanelButtonExitEnd = new JPanel();
		flowLayoutPanelButtonExitEnd = new FlowLayout(FlowLayout.CENTER);
		jPanelButtonExitEnd.setLayout(flowLayoutPanelButtonExitEnd);
		jPanelButtonExitEnd.setBackground(SystemColor.WHITE);
		jPanelButtonExitEnd.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK));

		jButtonExitTest.setAlignmentX(CENTER_ALIGNMENT);
		jButtonExitTest.setAlignmentY(CENTER_ALIGNMENT);
		jButtonEndTest.setAlignmentX(CENTER_ALIGNMENT);
		jButtonEndTest.setAlignmentY(CENTER_ALIGNMENT);

		jPanelButtonExitEnd.add(jButtonExitTest);
		jPanelButtonExitEnd.add(jButtonEndTest);

		jPanelButton = new JPanel();
		flowLayoutPanelButton = new FlowLayout(FlowLayout.CENTER);
		jPanelButton.setLayout(flowLayoutPanelButton);

		jPanelButtonCorrectReset.setAlignmentX(CENTER_ALIGNMENT);
		jPanelButtonExitEnd.setAlignmentX(CENTER_ALIGNMENT);

		jPanelButton.add(jPanelButtonCorrectReset);
		jPanelButton.add(jPanelButtonExitEnd);

		jPanelStatus = new JPanel();
		flowLayoutPanelStatus = new FlowLayout(FlowLayout.LEFT);
		jPanelStatus.setLayout(flowLayoutPanelStatus);
		jPanelStatus.setBorder(BorderFactory.createLoweredBevelBorder());

		jLabelStatus.setAlignmentX(LEFT_ALIGNMENT);
		jProgressBar.setAlignmentX(LEFT_ALIGNMENT);

		jPanelStatus.add(jLabelStatus);
		jPanelStatus.add(Box.createHorizontalStrut(10));
		jPanelStatus.add(jProgressBar);

		jPanelContent.add(jPanelQuestion);
		jPanelContent.add(jPanelButton);
		jPanelContent.add(jPanelStatus);
		jPanelContent.add(Box.createVerticalStrut(5));
	}

	
	////////////////////////////////////////////////////////////////////////////////
	//////////////////// METHODS TO INITIALIZE THE JAPPLET COMPONENTS //////////////
	/////////////////////// TO SHOW THE CORRECTION TO A QUESTION  //////////////////
	////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Initializes the JLabel objects of the JApplet object,
	 * setting their colour, their content...
	 */
	private void JLabelInitShowCorrection()
	{
		if (repetitionWithoutAnswer == false)
			jLabelNumberOfQuestion = new JLabel("Question "
					+ String.valueOf(evalTestLogStudent.getQuestionsDone()) + " "
					+ "of " + String.valueOf(evalTestLogStudent.getTotalQuestion()));
		else
			jLabelNumberOfQuestion = new JLabel(
					"Repeat questions without answer. Question "
							+ String.valueOf(contRepetitionWithoutAnswer)
							+ " of " + String.valueOf(sizeRepetitionWithoutAnswer));

		jLabelNumberOfQuestion.setFont(new java.awt.Font("Dialog", 1, 18));

		jLabelTitle = new JLabel("Result of the correction");
		jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 20));

		if (question.getPathImage().trim().equals("") == false)
		{
			jLabelImage = new JLabel();
			jLabelImage.setFocusable(true);
			jLabelImage.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));
			jLabelImage
					.setToolTipText("Make double click on the image to see it in the original size.");

			try
			{
				// Obtains the image located in the server
				String petitionQuestionImage = wowPath + itemsPath
						+ question.getPathImage().trim();

				URL urlQuestionImage = new URL(codeBase, petitionQuestionImage);

				questionImageIconTemp = new ImageIcon(urlQuestionImage);
				questionImage = questionImageIconTemp.getImage();

				// Sets the size of the image
				if (questionImageIconTemp.getIconWidth() >= questionImageIconTemp
						.getIconHeight())
				{
					if (questionImageIconTemp.getIconWidth() > 500)
					{
						double scale = questionImageIconTemp.getIconWidth() / 500.0;

						questionImage = questionImage.getScaledInstance(500, Integer
								.valueOf(
										String.valueOf(Math.round(questionImageIconTemp
												.getIconHeight()
												/ scale))).intValue(),
								Image.SCALE_AREA_AVERAGING);
					}
				}
				else
				{
					if (questionImageIconTemp.getIconHeight() > 500)
					{
						double scale = questionImageIconTemp.getIconHeight() / 500.0;

						questionImage = questionImage.getScaledInstance(Integer
								.valueOf(
										String.valueOf(Math.round(questionImageIconTemp
												.getIconWidth()
												/ scale))).intValue(), 500,
								Image.SCALE_AREA_AVERAGING);
					}
				}

				questionImageIcon = new ImageIcon(questionImage);

				jLabelImage.setIcon(questionImageIcon);
				jLabelImage.setFocusable(true);
				jLabelImage.setBorder(BorderFactory
						.createLineBorder(SystemColor.BLACK));

				jLabelImage.addMouseListener(new java.awt.event.MouseAdapter()
				{
					public void mouseClicked(MouseEvent e)
					{
						if (e.getClickCount() == 2)
						{
							// Hides the possible JFrame opened before
							if (jFrameQuestionImage != null)
							{
								jFrameQuestionImage.setVisible(false);
								jFrameQuestionImage.dispose();
								jFrameQuestionImage = null;
							}

							JLabel jLabelImageFrameQuestionImage = new JLabel(
									questionImageIconTemp);

							jFrameQuestionImage = new JFrame("Image of the question.");
							jFrameQuestionImage.setSize(640, 440);
							jFrameQuestionImage.setLocation(0, 0);
							jFrameQuestionImage.setResizable(true);
							jFrameQuestionImage.setVisible(true);
							JScrollPane jScrollPane = new JScrollPane(
									jLabelImageFrameQuestionImage,
									JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
									JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

							jFrameQuestionImage.getContentPane().add(jScrollPane);

							jFrameQuestionImage.pack();
						}
					}
				});
			}
			catch (java.net.MalformedURLException mfe)
			{
				jLabelImage = new JLabel("ERROR TO LOAD IMAGE: " + mfe.getMessage());
			}
		}

		if (codeAnswersCheckVector.isEmpty())
		{
			jLabelResultCorrection = new JLabel[1];
			jLabelResultCorrection[0] = new JLabel("WITHOUT ANSWER");
			jLabelResultCorrection[0].setIcon(iconWithoutAnswer32);
			jLabelResultCorrection[0].setFont(new java.awt.Font("Dialog", 1, 20));
		}
		else
		{
			jLabelResultCorrection = new JLabel[codeAnswersCheckVector.size()];

			for (int i = 0; i < codeAnswersCheckVector.size(); i++)
			{
				if (question.getAnswersCorrect().contains(
						codeAnswersCheckVector.get(i).toString()))
				{
					jLabelResultCorrection[i] = new JLabel("CORRECT");
					jLabelResultCorrection[i].setIcon(iconCorrect32);
				}
				else
				{
					jLabelResultCorrection[i] = new JLabel("INCORRECT");
					jLabelResultCorrection[i].setIcon(iconIncorrect32);
				}

				jLabelResultCorrection[i]
						.setFont(new java.awt.Font("Dialog", 1, 20));
			}
		}

		jLabelStatus = new JLabel("Done                                         ");
	}

	
	/**
	 * Initializes the JTextArea objects of the JApplet object,
	 * setting their colour, their content....
	 */
	private void JTextAreaInitShowCorrection()
	{
		jTextAreaEnunciate = new JTextArea(question.getEnunciate(), 2, 40);
		jTextAreaEnunciate.setLineWrap(true);
		jTextAreaEnunciate.setWrapStyleWord(true);
		jTextAreaEnunciate.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextAreaEnunciate.setBackground(SystemColor.WHITE);
		jTextAreaEnunciate.setBorder(BorderFactory.createLineBorder(SystemColor.black));
		jTextAreaEnunciate.setFocusable(false);

		if (codeAnswersCheckVector.isEmpty() == false)
		{
			jTextAreaAnswers = new JTextArea[codeAnswersCheckVector.size()];
			jTextAreaExplanations = new JTextArea[codeAnswersCheckVector.size()];

			for (int i = 0; i < codeAnswersCheckVector.size(); i++)
			{
				String codeAnswer = codeAnswersCheckVector.get(i).toString();

				int index = question.getCodeAnswers().indexOf(codeAnswer);

				jTextAreaAnswers[i] = new JTextArea(question.getTextAnswers().get(index).toString(), 2, 40);
				jTextAreaAnswers[i].setLineWrap(true);
				jTextAreaAnswers[i].setWrapStyleWord(true);
				jTextAreaAnswers[i].setFont(new java.awt.Font("Dialog", 1, 12));
				jTextAreaAnswers[i].setBackground(SystemColor.WHITE);
				jTextAreaAnswers[i].setBorder(BorderFactory
						.createLineBorder(SystemColor.black));
				jTextAreaAnswers[i].setFocusable(false);

				jTextAreaExplanations[i] = new JTextArea(question.getTextExplanation().get(index).toString(), 2, 40);
				jTextAreaExplanations[i].setLineWrap(true);
				jTextAreaExplanations[i].setWrapStyleWord(true);
				jTextAreaExplanations[i]
						.setFont(new java.awt.Font("Dialog", 1, 12));
				jTextAreaExplanations[i].setBackground(SystemColor.WHITE);
				jTextAreaExplanations[i].setBorder(BorderFactory
						.createLineBorder(SystemColor.black));
				jTextAreaExplanations[i].setFocusable(false);

				if (jLabelResultCorrection[i].getText().trim().equals("CORRECT"))
				{
					jTextAreaAnswers[i].setBackground(SystemColor.GREEN);
					jTextAreaExplanations[i].setBackground(SystemColor.GREEN);
				}
				else
				{
					if (jLabelResultCorrection[i].getText().trim().equals(
							"INCORRECT"))
					{
						jTextAreaAnswers[i].setBackground(SystemColor.RED);
						jTextAreaExplanations[i].setBackground(SystemColor.RED);
					}
				}
			}
		}

		if (answersCorrectVector.isEmpty() == false)
		{
			jTextAreaOtherCorrectAnswers = new JTextArea[answersCorrectVector.size()];

			for (int i = 0; i < answersCorrectVector.size(); i++)
			{
				String codeAnswer = answersCorrectVector.get(i).toString();

				int index = question.getCodeAnswers().indexOf(codeAnswer);

				jTextAreaOtherCorrectAnswers[i] = new JTextArea(question
						.getTextAnswers().get(index).toString(), 2, 40);
				jTextAreaOtherCorrectAnswers[i].setLineWrap(true);
				jTextAreaOtherCorrectAnswers[i].setWrapStyleWord(true);
				jTextAreaOtherCorrectAnswers[i].setFont(new java.awt.Font("Dialog", 0, 12));
				jTextAreaOtherCorrectAnswers[i].setBackground(SystemColor.WHITE);
				jTextAreaOtherCorrectAnswers[i].setBorder(BorderFactory
						.createLineBorder(SystemColor.black));
				jTextAreaOtherCorrectAnswers[i].setFocusable(false);
			}
		}
	}

	
	/**
	 * Initializes the JButton objects of the JApplet object,
	 * setting their colour, their content....
	 */
	private void JButtonInitShowCorrection()
	{
		if (imageIconLoad == true)
		{
			jButtonNextQuestion = new JButton("Next Question", iconBeginTest32);
			jButtonShowScore = new JButton("Show Score", iconStatistic32);
			jButtonExitTest = new JButton("Exit", iconExit32);
			jButtonEndTest = new JButton("End Test", iconCancel32);
		}
		else
		{
			jButtonNextQuestion = new JButton("Next Question");
			jButtonShowScore = new JButton("Show Score");
			jButtonExitTest = new JButton("Exit");
			jButtonEndTest = new JButton("End Test");
		}

		// Enables or disables the JButtonShowScore object depending
		// if is the last question or if the test configuration indicates
		// that the final screen must be showed. This is done to avoid
		// showing two times the same information
		if (repetitionWithoutAnswer == false)
		{
			if (evalTestLogStudent.getQuestionsDone() == evalTestLogStudent
					.getTotalQuestion()
					&& test.getShowFinalInfo() == true)
				setEnabled(jButtonShowScore, false);
			else
				setEnabled(jButtonShowScore, true);

			if (test.getExecutionType().equals(TestEditor.ADAPTIVE))
			{
				if (endTest == true || questionVector.isEmpty())
				{
					jButtonNextQuestion.setText("Finish the test");
					setEnabled(jButtonExitTest, false);
					setEnabled(jButtonEndTest, false);

					if (test.getShowFinalInfo() == true)
						setEnabled(jButtonShowScore, false);
					else
						setEnabled(jButtonShowScore, true);

					jButtonNextQuestion
							.addActionListener(new java.awt.event.ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{
									jButtonEndTestActionPerformed();
								}
							});
				}
				else
				{
					jButtonNextQuestion
							.addActionListener(new java.awt.event.ActionListener()
							{
								public void actionPerformed(ActionEvent e)
								{
									showQuestionAdaptive();
								}
							});
				}
			}
			else
			{
				if (evalTestLogStudent.getQuestionsDone() == evalTestLogStudent
						.getTotalQuestion())
				{
					jButtonNextQuestion.setText("Finish the test");
					setEnabled(jButtonExitTest, false);
					setEnabled(jButtonEndTest, false);
				}

				jButtonNextQuestion
						.addActionListener(new java.awt.event.ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								showQuestionClassic();
							}
						});
			}
		}
		else
		{
			if (contRepetitionWithoutAnswer == sizeRepetitionWithoutAnswer
					&& test.getShowFinalInfo() == true)
				setEnabled(jButtonShowScore, false);
			else
				setEnabled(jButtonShowScore, true);

			if (contRepetitionWithoutAnswer == sizeRepetitionWithoutAnswer)
			{
				jButtonNextQuestion.setText("Finish the test");
				setEnabled(jButtonExitTest, false);
				setEnabled(jButtonEndTest, false);
			}

			jButtonNextQuestion
					.addActionListener(new java.awt.event.ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							showQuestionClassic();
						}
					});
		}

		jButtonShowScore.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButtonShowScoreActionPerformed();
			}
		});

		jButtonExitTest.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButtonExitTestActionPerformed();
			}
		});

		jButtonEndTest.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButtonEndTestActionPerformed();
			}
		});
	}

	
	/**
	 * Initializes the JPanel of the JApplet,
	 * setting their colour, their content...
	 */
	private void JPanelInitShowCorrection()
	{
		jPanelContent = new JPanel();
		boxLayoutPanelContent = new BoxLayout(jPanelContent, BoxLayout.Y_AXIS);
		jPanelContent.setLayout(boxLayoutPanelContent);
		jPanelContent
				.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jScrollPane = new JScrollPane(jPanelContent,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK, 3));
		jScrollPane.setBackground(SystemColor.BLACK);

		jPanelNumberOfQuestion = new JPanel();
		flowLayoutPanelNumberOfQuestion = new FlowLayout(FlowLayout.CENTER);
		jPanelNumberOfQuestion.setLayout(flowLayoutPanelNumberOfQuestion);
		jPanelNumberOfQuestion.setBackground(SystemColor.WHITE);
		jPanelNumberOfQuestion.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK));

		jLabelNumberOfQuestion.setAlignmentX(CENTER_ALIGNMENT);
		jLabelNumberOfQuestion.setAlignmentY(CENTER_ALIGNMENT);

		jPanelNumberOfQuestion.add(jLabelNumberOfQuestion);

		jPanelTitle = new JPanel();
		jPanelTitle.setLayout(flowLayoutPanelNumberOfQuestion);

		jLabelTitle.setAlignmentX(CENTER_ALIGNMENT);
		jLabelTitle.setAlignmentY(CENTER_ALIGNMENT);

		jPanelTitle.add(jLabelTitle);

		jPanelEnunciate = new JPanel();
		flowLayoutPanelEnunciate = new FlowLayout(FlowLayout.CENTER);
		jPanelEnunciate.setLayout(flowLayoutPanelEnunciate);
		jPanelEnunciate.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Enunciate",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Dialog", 0, 14)));

		jTextAreaEnunciate.setAlignmentX(CENTER_ALIGNMENT);
		jTextAreaEnunciate.setAlignmentY(CENTER_ALIGNMENT);

		jScrollPaneJTextAreaEnunciate = new JScrollPane(jTextAreaEnunciate,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneJTextAreaEnunciate.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK));

		jPanelEnunciate.add(jScrollPaneJTextAreaEnunciate);

		if (question.getPathImage().trim().equals("") == false)
		{
			jPanelImage = new JPanel();
			flowLayoutPanelImage = new FlowLayout(FlowLayout.CENTER);
			jPanelImage.setLayout(flowLayoutPanelImage);
			jPanelImage.setBorder(BorderFactory.createTitledBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK), "Image",
					TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
					new java.awt.Font("Dialog", 0, 14)));

			jLabelImage.setAlignmentX(CENTER_ALIGNMENT);
			jLabelImage.setAlignmentY(CENTER_ALIGNMENT);

			jPanelImage.add(jLabelImage);
		}

		if (codeAnswersCheckVector.isEmpty() == false)
		{
			jPanelAnswer = new JPanel[codeAnswersCheckVector.size()];
			flowLayoutPanelAnswer = new FlowLayout[codeAnswersCheckVector.size()];
			jScrollPaneJTextAreaAnswer = new JScrollPane[codeAnswersCheckVector
					.size()];

			jPanelResultCorrection = new JPanel[codeAnswersCheckVector.size()];
			flowLayoutPanelResultCorrection = new FlowLayout[codeAnswersCheckVector
					.size()];

			jPanelExplanation = new JPanel[codeAnswersCheckVector.size()];
			flowLayoutPanelExplanation = new FlowLayout[codeAnswersCheckVector
					.size()];
			jScrollPaneJTextAreaExplanation = new JScrollPane[codeAnswersCheckVector
					.size()];

			for (int i = 0; i < codeAnswersCheckVector.size(); i++)
			{
				jPanelAnswer[i] = new JPanel();
				flowLayoutPanelAnswer[i] = new FlowLayout(FlowLayout.CENTER);
				jPanelAnswer[i].setLayout(flowLayoutPanelAnswer[i]);
				jPanelAnswer[i].setBorder(BorderFactory.createTitledBorder(
						BorderFactory.createLineBorder(SystemColor.BLACK), "Answer",
						TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Dialog", 0, 14)));

				jScrollPaneJTextAreaAnswer[i] = new JScrollPane(
						jTextAreaAnswers[i],
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				jScrollPaneJTextAreaAnswer[i].setBorder(BorderFactory
						.createLineBorder(SystemColor.BLACK));

				jPanelAnswer[i].add(jScrollPaneJTextAreaAnswer[i]);

				jPanelResultCorrection[i] = new JPanel();
				flowLayoutPanelResultCorrection[i] = new FlowLayout(
						FlowLayout.CENTER);
				jPanelResultCorrection[i]
						.setLayout(flowLayoutPanelResultCorrection[i]);
				jPanelResultCorrection[i].setBorder(BorderFactory
						.createTitledBorder(BorderFactory
								.createLineBorder(SystemColor.BLACK), "This answer is",
								TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
								new java.awt.Font("Dialog", 0, 14)));

				jLabelResultCorrection[i].setAlignmentX(CENTER_ALIGNMENT);
				jLabelResultCorrection[i].setAlignmentX(CENTER_ALIGNMENT);

				jPanelResultCorrection[i].add(jLabelResultCorrection[i]);

				jPanelExplanation[i] = new JPanel();
				flowLayoutPanelExplanation[i] = new FlowLayout(FlowLayout.CENTER);
				jPanelExplanation[i].setLayout(flowLayoutPanelExplanation[i]);
				jPanelExplanation[i].setBorder(BorderFactory.createTitledBorder(
						BorderFactory.createLineBorder(SystemColor.BLACK),
						"Aditional comment", TitledBorder.LEFT,
						TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0,
								14)));

				jScrollPaneJTextAreaExplanation[i] = new JScrollPane(
						jTextAreaExplanations[i],
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				jScrollPaneJTextAreaExplanation[i].setBorder(BorderFactory
						.createLineBorder(SystemColor.BLACK));

				jPanelExplanation[i].add(jScrollPaneJTextAreaExplanation[i]);
			}
		}
		else
		{
			int i = 0;

			jPanelResultCorrection = new JPanel[1];
			flowLayoutPanelResultCorrection = new FlowLayout[1];

			jPanelResultCorrection[i] = new JPanel();
			flowLayoutPanelResultCorrection[i] = new FlowLayout(FlowLayout.CENTER);
			jPanelResultCorrection[i]
					.setLayout(flowLayoutPanelResultCorrection[i]);
			jPanelResultCorrection[i].setBorder(BorderFactory
					.createTitledBorder(BorderFactory
							.createLineBorder(SystemColor.BLACK), "This answer is",
							TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
							new java.awt.Font("Dialog", 0, 14)));

			jLabelResultCorrection[i].setAlignmentX(CENTER_ALIGNMENT);
			jLabelResultCorrection[i].setAlignmentX(CENTER_ALIGNMENT);

			jPanelResultCorrection[i].add(jLabelResultCorrection[i]);
		}

		jPanelAnswers = new JPanel();
		boxLayoutPanelAnswers = new BoxLayout(jPanelAnswers, BoxLayout.Y_AXIS);
		jPanelAnswers.setLayout(boxLayoutPanelAnswers);
		jPanelAnswers.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Your Answer(s)",
				TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Dialog", 0, 14)));

		if (codeAnswersCheckVector.isEmpty() == false)
		{
			for (int i = 0; i < codeAnswersCheckVector.size(); i++)
			{
				jPanelAnswer[i].setAlignmentX(CENTER_ALIGNMENT);
				jPanelResultCorrection[i].setAlignmentX(CENTER_ALIGNMENT);
				jPanelExplanation[i].setAlignmentX(CENTER_ALIGNMENT);

				jPanelAnswers.add(jPanelAnswer[i]);
				jPanelAnswers.add(jPanelResultCorrection[i]);

				if (test.getVerbose() == true)
					jPanelAnswers.add(jPanelExplanation[i]);

				jPanelAnswers.add(Box.createVerticalStrut(20));
			}
		}
		else
		{
			int i = 0;

			jPanelResultCorrection[i].setAlignmentX(CENTER_ALIGNMENT);

			jPanelAnswers.add(jPanelResultCorrection[i]);
			jPanelAnswers.add(Box.createVerticalStrut(20));
		}

		if (answersCorrectVector.isEmpty() == false)
		{
			jPanelOtherCorrectAnswer = new JPanel[answersCorrectVector.size()];
			flowLayoutPanelOtherCorrectAnswer = new FlowLayout[answersCorrectVector
					.size()];
			jScrollPaneJTextAreaOtherCorrectAnswer = new JScrollPane[answersCorrectVector
					.size()];

			for (int i = 0; i < answersCorrectVector.size(); i++)
			{
				jPanelOtherCorrectAnswer[i] = new JPanel();
				flowLayoutPanelOtherCorrectAnswer[i] = new FlowLayout(
						FlowLayout.CENTER);
				jPanelOtherCorrectAnswer[i]
						.setLayout(flowLayoutPanelOtherCorrectAnswer[i]);

				jScrollPaneJTextAreaOtherCorrectAnswer[i] = new JScrollPane(
						jTextAreaOtherCorrectAnswers[i],
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				jScrollPaneJTextAreaOtherCorrectAnswer[i].setBorder(BorderFactory
						.createLineBorder(SystemColor.BLACK));

				jPanelOtherCorrectAnswer[i]
						.add(jScrollPaneJTextAreaOtherCorrectAnswer[i]);
			}

			jPanelOtherCorrectAnswers = new JPanel();
			boxLayoutPanelOtherCorrectAnswers = new BoxLayout(
					jPanelOtherCorrectAnswers, BoxLayout.Y_AXIS);
			jPanelOtherCorrectAnswers.setLayout(boxLayoutPanelOtherCorrectAnswers);

			if (answersCorrectVector.size() == question.getAnswersCorrect().size())
				jPanelOtherCorrectAnswers.setBorder(BorderFactory
						.createTitledBorder(BorderFactory
								.createLineBorder(SystemColor.BLACK),
								"Correct answer(s)", TitledBorder.LEFT,
								TitledBorder.DEFAULT_POSITION, new java.awt.Font(
										"Dialog", 0, 14)));
			else
				jPanelOtherCorrectAnswers.setBorder(BorderFactory
						.createTitledBorder(BorderFactory
								.createLineBorder(SystemColor.BLACK),
								"Other correct answer(s)", TitledBorder.LEFT,
								TitledBorder.DEFAULT_POSITION, new java.awt.Font(
										"Dialog", 0, 14)));

			for (int i = 0; i < answersCorrectVector.size(); i++)
			{
				jPanelOtherCorrectAnswer[i].setAlignmentX(CENTER_ALIGNMENT);

				jPanelOtherCorrectAnswers.add(jPanelOtherCorrectAnswer[i]);
			}
		}

		jPanelQuestion = new JPanel();
		boxLayoutPanelQuestion = new BoxLayout(jPanelQuestion, BoxLayout.Y_AXIS);
		jPanelQuestion.setLayout(boxLayoutPanelQuestion);

		jPanelNumberOfQuestion.setAlignmentX(CENTER_ALIGNMENT);
		jPanelTitle.setAlignmentX(CENTER_ALIGNMENT);
		jPanelEnunciate.setAlignmentX(CENTER_ALIGNMENT);

		if (question.getPathImage().trim().equals("") == false)
			jPanelImage.setAlignmentX(CENTER_ALIGNMENT);

		jPanelAnswers.setAlignmentX(CENTER_ALIGNMENT);

		jPanelNumberOfQuestion.setAlignmentY(CENTER_ALIGNMENT);
		jPanelTitle.setAlignmentY(CENTER_ALIGNMENT);
		jPanelEnunciate.setAlignmentY(CENTER_ALIGNMENT);

		if (question.getPathImage().trim().equals("") == false)
			jPanelImage.setAlignmentY(CENTER_ALIGNMENT);

		jPanelAnswers.setAlignmentY(CENTER_ALIGNMENT);

		jPanelQuestion.add(jPanelNumberOfQuestion);
		jPanelQuestion.add(jPanelTitle);
		jPanelQuestion.add(Box.createVerticalStrut(6));
		jPanelQuestion.add(jPanelEnunciate);
		jPanelQuestion.add(Box.createVerticalStrut(6));

		if (question.getPathImage().trim().equals("") == false)
		{
			jPanelQuestion.add(jPanelImage);
			jPanelQuestion.add(Box.createVerticalStrut(6));
		}

		jPanelQuestion.add(jPanelAnswers);
		if (answersCorrectVector.isEmpty() == false
				&& test.getShowCorrectAnswers() == true)
		{
			// This avoid showing the correct answers if the not answered
			// questions will be repeat
			if (answerValue.trim().equals("withoutAnswer") == false)
			{
				jPanelQuestion.add(Box.createVerticalStrut(10));
				jPanelQuestion.add(jPanelOtherCorrectAnswers);
			}
			else
			{
				if (test.getRepeatWithoutAnswer() == false)
				{
					jPanelQuestion.add(Box.createVerticalStrut(10));
					jPanelQuestion.add(jPanelOtherCorrectAnswers);
				}
			}
		}

		jPanelButtonNextQuestionShowScore = new JPanel();
		flowLayoutPanelButtonNextQuestionShowScore = new FlowLayout(
				FlowLayout.CENTER);
		jPanelButtonNextQuestionShowScore
				.setLayout(flowLayoutPanelButtonNextQuestionShowScore);
		jPanelButtonNextQuestionShowScore.setBackground(SystemColor.WHITE);
		jPanelButtonNextQuestionShowScore.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK));

		jButtonNextQuestion.setAlignmentX(CENTER_ALIGNMENT);
		jButtonShowScore.setAlignmentX(CENTER_ALIGNMENT);
		jButtonNextQuestion.setAlignmentY(CENTER_ALIGNMENT);
		jButtonShowScore.setAlignmentY(CENTER_ALIGNMENT);

		jPanelButtonNextQuestionShowScore.add(jButtonNextQuestion);
		jPanelButtonNextQuestionShowScore.add(jButtonShowScore);

		jPanelButtonExitEnd = new JPanel();
		flowLayoutPanelButtonExitEnd = new FlowLayout(FlowLayout.CENTER);
		jPanelButtonExitEnd.setLayout(flowLayoutPanelButtonExitEnd);
		jPanelButtonExitEnd.setBackground(SystemColor.WHITE);
		jPanelButtonExitEnd.setBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK));

		jButtonExitTest.setAlignmentX(CENTER_ALIGNMENT);
		jButtonExitTest.setAlignmentY(CENTER_ALIGNMENT);
		jButtonEndTest.setAlignmentX(CENTER_ALIGNMENT);
		jButtonEndTest.setAlignmentY(CENTER_ALIGNMENT);

		jPanelButtonExitEnd.add(jButtonExitTest);
		jPanelButtonExitEnd.add(jButtonEndTest);

		jPanelButton = new JPanel();
		flowLayoutPanelButton = new FlowLayout(FlowLayout.CENTER);
		jPanelButton.setLayout(flowLayoutPanelButton);

		jPanelButtonCorrectReset.setAlignmentX(CENTER_ALIGNMENT);
		jPanelButtonExitEnd.setAlignmentX(CENTER_ALIGNMENT);

		jPanelButton.add(jPanelButtonNextQuestionShowScore);
		jPanelButton.add(jPanelButtonExitEnd);

		jPanelStatus = new JPanel();
		flowLayoutPanelStatus = new FlowLayout(FlowLayout.LEFT);
		jPanelStatus.setLayout(flowLayoutPanelStatus);
		jPanelStatus.setBorder(BorderFactory.createLoweredBevelBorder());

		jLabelStatus.setAlignmentX(LEFT_ALIGNMENT);
		jProgressBar.setAlignmentX(LEFT_ALIGNMENT);

		jPanelStatus.add(jLabelStatus);
		jPanelStatus.add(Box.createHorizontalStrut(10));
		jPanelStatus.add(jProgressBar);

		jPanelContent.add(jPanelQuestion);
		jPanelContent.add(jPanelButton);
		jPanelContent.add(jPanelStatus);
		jPanelContent.add(Box.createVerticalStrut(5));
	}

	
	////////////////////////////////////////////////////////////////////////////////
	//////////////////// METHODS TO INITIALIZE THE JAPPLET COMPONENTS //////////////
	//////////////////////// TO SHOW THE FINAL RESULT OF THE TEST //////////////////
	////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Initializes the JLabel objects of the JApplet object, setting
	 * their size and their content.
	 *
	 */
	private void JLabelInitShowEndTest()
	{
		jLabelTitle = new JLabel();
		jLabelChart = new JLabel();
		jLabelNumTotalQuestions1 = new JLabel();
		jLabelCorrect = new JLabel();
		jLabelIncorrect = new JLabel();
		jLabelWithoutAnswer = new JLabel();
		jLabelQuestionsDone = new JLabel();
		jLabelScore = new JLabel();
		jLabelProficiencyPrevious = new JLabel();
		jLabelProficiency = new JLabel();
		jLabelStandardError = new JLabel();
		jLabelStatus = new JLabel();

		jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 16));
		jLabelTitle.setBorder(BorderFactory.createLoweredBevelBorder());
		jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelTitle.setText("Final result of the test");
		
		if (chart != null) {
			jLabelChart.setFocusable(true);
			jLabelChart.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			ImageIcon chartImage = new ImageIcon(chart);
			jLabelChart.setIcon(chartImage);
		}

		jLabelNumTotalQuestions1.setText("Total questions ");
		jLabelNumTotalQuestions1.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelCorrect.setText("Correct ");
		jLabelCorrect.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelIncorrect.setText("Incorrect ");
		jLabelIncorrect.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelWithoutAnswer.setText("Without answer ");
		jLabelWithoutAnswer.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelQuestionsDone.setText("Questions done ");
		jLabelQuestionsDone.setFont(new java.awt.Font("Dialog", 1, 12));

		if (test.getExecutionType().equals(TestEditor.ADAPTIVE)) {
			jLabelScore.setText("Score");
			jLabelScore.setFont(new java.awt.Font("Dialog", 1, 14));

			jLabelProficiencyPrevious.setText("Previous proficiency");
			jLabelProficiencyPrevious.setFont(new java.awt.Font("Dialog", 1, 14));

			jLabelProficiency.setText("Proficiency");
			jLabelProficiency.setFont(new java.awt.Font("Dialog", 1, 14));
			
			jLabelStandardError.setText("Standard Error");
			jLabelStandardError.setFont(new java.awt.Font("Dialog", 1, 14));
		} else {
			jLabelScore.setText("Score");
			jLabelScore.setFont(new java.awt.Font("Dialog", 1, 14));
		}

		jLabelStatus.setText("Done");
		jLabelStatus.setFont(new java.awt.Font("Dialog", 0, 14));
	}

	
	/**
	 * Initializes the JTextField objects of the JApplet, setting
	 * their size and their functionality
	 */
	private void JTextFieldInitShowEndTest()
	{
		jTextFieldTotalQuestions = new JTextField(String
				.valueOf(evalTestLogStudent.getTotalQuestion()), 3);
		jTextFieldTotalQuestions.setFocusable(false);
		jTextFieldTotalQuestions.setFont(new java.awt.Font("Dialog", 1, 14));

		jTextFieldCorrect = new JTextField(String.valueOf(evalTestLogStudent
				.getCorrect()));
		jTextFieldCorrect.setFocusable(false);
		jTextFieldCorrect.setFont(new java.awt.Font("Dialog", 1, 14));
		jTextFieldCorrect.setBackground(SystemColor.GREEN);

		jTextFieldIncorrect = new JTextField(String.valueOf(evalTestLogStudent
				.getIncorrect()));
		jTextFieldIncorrect.setFocusable(false);
		jTextFieldIncorrect.setFont(new java.awt.Font("Dialog", 1, 14));
		jTextFieldIncorrect.setBackground(SystemColor.RED);

		jTextFieldWithoutAnswer = new JTextField(String
				.valueOf(evalTestLogStudent.getWithoutAnswer()));
		jTextFieldWithoutAnswer.setFocusable(false);
		jTextFieldWithoutAnswer.setFont(new java.awt.Font("Dialog", 1, 14));
		jTextFieldWithoutAnswer.setBackground(SystemColor.YELLOW);

		jTextFieldQuestionsDone = new JTextField(String
				.valueOf(evalTestLogStudent.getQuestionsDone()));
		jTextFieldQuestionsDone.setFocusable(false);
		jTextFieldQuestionsDone.setFont(new java.awt.Font("Dialog", 1, 14));
		jTextFieldQuestionsDone.setBackground(SystemColor.BLUE);

		if (test.getExecutionType().equals(TestEditor.ADAPTIVE)) {
			String score = String.valueOf(evalTestLogStudent.getScore());
			if (score.length() > 5) {
				score = score.substring(0, 5);
			}

			jTextFieldScore = new JTextField(score);
			jTextFieldScore.setFocusable(false);
			jTextFieldScore.setFont(new java.awt.Font("Dialog", 1, 14));
			jTextFieldScore.setBackground(SystemColor.LIGHT_GRAY);
			jTextFieldScore.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));

			String thetaPrevious = String.valueOf(evalTestLogStudent
					.getThetaPrevious());
			if (thetaPrevious.length() > 5)
				thetaPrevious = thetaPrevious.substring(0, 5);

			jTextFieldProficiencyPrevious = new JTextField(thetaPrevious);
			jTextFieldProficiencyPrevious.setFocusable(false);
			jTextFieldProficiencyPrevious.setFont(new java.awt.Font("Dialog", 1,
					14));
			jTextFieldProficiencyPrevious.setBackground(SystemColor.LIGHT_GRAY);
			jTextFieldProficiencyPrevious.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));

			String theta = String.valueOf(evalTestLogStudent.getTheta());
			if (theta.length() > 5) theta = theta.substring(0, 5);

			jTextFieldProficiency = new JTextField(theta);
			jTextFieldProficiency.setFocusable(false);
			jTextFieldProficiency.setFont(new java.awt.Font("Dialog", 1, 14));
			jTextFieldProficiency.setBackground(SystemColor.LIGHT_GRAY);
			jTextFieldProficiency.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));

			String standardError = String.valueOf(evalTestLogStudent
					.getStandardError());
			if (standardError.length() > 5)
				standardError = standardError.substring(0, 5);

			jTextFieldStandardError = new JTextField(standardError);
			jTextFieldStandardError.setFocusable(false);
			jTextFieldStandardError.setFont(new java.awt.Font("Dialog", 1, 14));
			jTextFieldStandardError.setBackground(SystemColor.LIGHT_GRAY);
			jTextFieldStandardError.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));
		}
		else
		{
			String score = String.valueOf(evalTestLogStudent.getScore());
			if (score.length() > 5) score = score.substring(0, 5);

			jTextFieldScore = new JTextField(score);
			jTextFieldScore.setFocusable(false);
			jTextFieldScore.setFont(new java.awt.Font("Dialog", 1, 32));
			jTextFieldScore.setBackground(SystemColor.LIGHT_GRAY);
			jTextFieldScore.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));
		}
	}

	
	/**
	 * Initializes the JButton objects of the JApplet, setting
	 * their size, their content, and their functionality.
	 */
	private void JButtonInitShowEndTest()
	{
		if (imageIconLoad == true) {
			jButtonExitTest = new JButton("Exit", iconBeginTest32);
		} else {
			jButtonExitTest = new JButton("Exit");
		}	

		jButtonExitTest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonExitTestActionPerformed();
			}
		});
	}

	
	/**
	 * Initializes the JPanel objects of the JApplet object, 
	 * setting their size and their layout.
	 */
	private void JPanelInitShowEndTest()
	{
		jPanelTitle = new JPanel();
		jPanelInfo = new JPanel();
		jPanelStatus = new JPanel();

		jPanelTitle.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK, 1));
		jPanelTitle.setLayout(new BorderLayout());

		jPanelInfo.setBorder(BorderFactory.createLoweredBevelBorder());
		jPanelInfo.setLayout(new BoxLayout(jPanelInfo, BoxLayout.Y_AXIS));

		jPanelStatus.setBorder(BorderFactory.createLoweredBevelBorder());
		jPanelStatus.setLayout(new BorderLayout());
	}

	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * Initializes the JApplet object. Sets its main panel,
	 * their functionality.....
	 * @return true if the appletInit has been done
	 * @throws Exception If an error occur
	 */
	private boolean JAppletInit() throws Exception {
		this.setContentPane(jScrollPane);

		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		this.paintAll(this.getGraphics());
		
		appletInit = true;
		
		return appletInit;
	}


	/**
	 * Sets the content of each JPanel object of the JDialog object
	 * @throws Exception If an error occur
	 */
	private void JAppletInitShowEndTest() throws Exception
	{
		jPanelTitle.add(jLabelTitle, BorderLayout.CENTER);
		jPanelInfo.add(jLabelNumTotalQuestions1, null);
		jPanelInfo.add(jTextFieldTotalQuestions, null);
		jPanelInfo.add(jLabelCorrect, null);
		jPanelInfo.add(jTextFieldCorrect, null);
		jPanelInfo.add(jLabelIncorrect, null);
		jPanelInfo.add(jTextFieldIncorrect, null);
		jPanelInfo.add(jLabelWithoutAnswer, null);
		jPanelInfo.add(jTextFieldWithoutAnswer, null);
		jPanelInfo.add(jLabelQuestionsDone, null);
		jPanelInfo.add(jTextFieldQuestionsDone, null);

		if (executionType.trim().equals(TestEditor.ADAPTIVE)) {
			jPanelInfo.add(jLabelScore, null);
			jPanelInfo.add(jTextFieldScore, null);
			jPanelInfo.add(jLabelProficiencyPrevious, null);
			jPanelInfo.add(jTextFieldProficiencyPrevious, null);
			jPanelInfo.add(jLabelProficiency, null);
			jPanelInfo.add(jTextFieldProficiency, null);
			jPanelInfo.add(jLabelStandardError, null);
			jPanelInfo.add(jTextFieldStandardError, null);
		} else {
			jPanelInfo.add(jLabelScore, null);
			jPanelInfo.add(jTextFieldScore, null);
		}
		
		JPanel jPanelChart = new JPanel();
		jPanelChart.setLayout(new BorderLayout());
		jPanelChart.add(jLabelChart, BorderLayout.NORTH);

		JPanel jPanelButtonExit = new JPanel();
		jPanelButtonExit.setLayout(new BorderLayout());
		jPanelButtonExit.add(jButtonExitTest, BorderLayout.EAST);
		jPanelStatus.add(jPanelButtonExit, BorderLayout.NORTH);
		jPanelStatus.add(jLabelStatus, BorderLayout.CENTER);

		JPanel jPanelTheInfo = new JPanel(); 
		jPanelTheInfo.setLayout(new BorderLayout());
		jPanelTheInfo.add(jPanelInfo, BorderLayout.NORTH);
		jPanelTheInfo.add(new JLabel(""), BorderLayout.CENTER);

		JPanel jPanelAll = new JPanel(); 
		jPanelAll.setLayout(new BorderLayout());
		jPanelAll.add(jPanelTitle, BorderLayout.NORTH);
		jPanelAll.add(jPanelChart, BorderLayout.WEST);
		jPanelAll.add(jPanelTheInfo, BorderLayout.CENTER);
		jPanelAll.add(jPanelStatus, BorderLayout.SOUTH);
		
		this.setContentPane(jPanelAll);

		this.paintAll(this.getGraphics());

		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	
	/**
	 * Initializes the ImageIcon objects of the JApplet object,
	 * setting their size, their content, their functionality..
	 * The icons are located on the server, so the JApplet object
	 * will make a petition to it the download the icons.
	 */
	private void JImageIconInit()
	{
		// Connect with the server
		try
		{
			// Build the String object that will be used to make the petition to the server
			String petitionBeginTest32 = wowPath + iconTestEditorPath + "go32.gif";
			String petitionCancel32 = wowPath + iconTestEditorPath
					+ "cancel32.gif";
			String petitionClear32 = wowPath + iconTestEditorPath + "clear32.gif";
			String petitionExit32 = wowPath + iconTestEditorPath + "exit32.gif";
			String petitionCorrect32 = wowPath + iconTestEditorPath
					+ "correct32.gif";
			String petitionIncorrect32 = wowPath + iconTestEditorPath
					+ "incorrect32.gif";
			String petitionWithoutAnswer32 = wowPath + iconTestEditorPath
					+ "withoutAnswer32.gif";
			String petitionStatistic32 = wowPath + iconTestEditorPath
					+ "statistic32.gif";
			String petitionTimeToAnswer32 = wowPath + iconTestEditorPath
					+ "timeToAnswer32.gif";

			URL urlBeginTest32 = new URL(codeBase, petitionBeginTest32);
			URL urlCancel32 = new URL(codeBase, petitionCancel32);
			URL urlClear32 = new URL(codeBase, petitionClear32);
			URL urlExit32 = new URL(codeBase, petitionExit32);
			URL urlCorrect32 = new URL(codeBase, petitionCorrect32);
			URL urlIncorrect32 = new URL(codeBase, petitionIncorrect32);
			URL urlWithoutAnswer32 = new URL(codeBase, petitionWithoutAnswer32);
			URL urlStatistic32 = new URL(codeBase, petitionStatistic32);
			URL urlTimeToAnswer32 = new URL(codeBase, petitionTimeToAnswer32);

			iconBeginTest32 = new ImageIcon(urlBeginTest32);
			iconCancel32 = new ImageIcon(urlCancel32);
			iconClear32 = new ImageIcon(urlClear32);
			iconExit32 = new ImageIcon(urlExit32);
			iconCorrect32 = new ImageIcon(urlCorrect32);
			iconIncorrect32 = new ImageIcon(urlIncorrect32);
			iconWithoutAnswer32 = new ImageIcon(urlWithoutAnswer32);
			iconStatistic32 = new ImageIcon(urlStatistic32);
			iconTimeToAnswer32 = new ImageIcon(urlTimeToAnswer32);

			imageIconLoad = true;
		}
		catch (java.net.MalformedURLException e)
		{
			imageIconLoad = false;
			e.printStackTrace();
		}
	}

	
	/**
	 * Initializes the JProgressBar object of the JDialog object
	 */
	private void JProgressBarInit()
	{
		jProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
		jProgressBar.setBorderPainted(true);
		jProgressBar.setStringPainted(true);
		jProgressBar.setSize(100, 20);
		jProgressBar.setVisible(true);
	}

	
	/**
	 * Repaint the JProgressBar to show the progress of a task
	 * @param message Contains the message to be showed in the jLabelStatus object
	 */
	private void repaintProgressBar(String message)
	{
		jPanelStatus.setVisible(true);
		jLabelStatus.setVisible(true);
		jProgressBar.setVisible(true);
		jLabelStatus.paint(this.jLabelStatus.getGraphics());
		jPanelStatus.paint(this.jPanelStatus.getGraphics());
		jProgressBar.paint(this.jProgressBar.getGraphics());
		jPanelStatus.paint(this.jPanelStatus.getGraphics());
		jLabelStatus.setText(message);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());
		jProgressBar.setMinimum(0);
		jProgressBar.setMaximum(15000);
		jProgressBar.setValue(0);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());

		taskDone = 0;

		int cont = 0;
		while (taskDone == 0)
		{
			cont = cont + 100;
			jLabelStatus.setText(message);
			jPanelStatus.paint(this.jPanelStatus.getGraphics());
			jProgressBar.setValue(cont);
			jPanelStatus.paint(this.jPanelStatus.getGraphics());

			try
			{
				Thread.sleep(100);
			}
			catch (java.lang.InterruptedException e)
			{
				e.printStackTrace();	
			}
		}

		if (taskDone != -1)
			jLabelStatus
					.setText("Done                                              ");
		else
			jLabelStatus.setText("Overcome time of wait. Try it later.");

		jPanelStatus.paint(this.jPanelStatus.getGraphics());
		jProgressBar.setValue(jProgressBar.getMaximum());
		jPanelStatus.paint(this.jPanelStatus.getGraphics());
		jProgressBar.setVisible(false);
		jPanelStatus.paint(this.jPanelStatus.getGraphics());
	}

	
	////////////////////////////////////////////////////////////////////////////////
	///////////////// METHODS INVOCATED BY THE EVENTS PRODUCED  ////////////////////
	//////////////// OVER THE ELEMENTS CONTAINED IN THE JAPPLET ////////////////////
	////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sends to the server the necessary information to create a
	 * student log file - if necessary - associated to the student 
	 * and the testtest, and obtains the data of the questions 
	 * that will be showed in the test.
	 */
	private void jButtonBeginTestActionPerformed()
	{
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		setEnabled(jButtonBeginTest, false);

		student = null;

		if (test.getShowInitialInfo())
		{
			workerBeginTest = new BeginTest(courseName, testFileName, login,
					executionType);
			workerBeginTest.start();

			workerRepaintProgressBar = new RepaintProgressBar(
					"Loading/creating student log file...");
			workerRepaintProgressBar.start();

			workerTimeControl = new TimeControl();
			workerTimeControl.start();

			try
			{
				workerBeginTest.join();
				workerRepaintProgressBar.join();
			}
			catch (java.lang.InterruptedException e)
			{
				e.printStackTrace();
			}

			workerTimeControl.stop();

			if (taskDone == -1)
			{
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				workerBeginTest.stop();
				workerRepaintProgressBar.stop();
				return;
			}
		}
		else
		{
			if (executionType.trim().equals(TestEditor.ADAPTIVE))
				student = beginTest(courseName, testFileName, login, executionType,
						test.getIrtInitialProficiency(), -1);
			else
				student = beginTest(courseName, testFileName, login, executionType,
						99999, 99999);
		}

		if (student != null)
		{
			// Obtains the TestLogStudent object
			testLogStudent = (TestLogStudent) student.getTest().get(0);

			// Obtains the EvalTestLogStudent object
			evalTestLogStudent = (EvalTestLogStudent) testLogStudent
					.getEvaluatedTest().get(0);
			questionVector = new Vector();
			Vector testVector = evalTestLogStudent.getTestVector();
			QuestionsFileTest questionsFileTest = null;

			// Loop to create the vector that contains all
			// the questions of the test.
			for (int i = 0; i < testVector.size(); i++)
			{
				questionsFileTest = (QuestionsFileTest) testVector.get(i);

				Vector codeQuestionVector = questionsFileTest.getCodeQuestions();
				Vector doneVector = questionsFileTest.getDone();
				Vector answersValueVector = questionsFileTest.getAnswersValues();

				for (int j = 0; j < codeQuestionVector.size(); j++)
				{
					// Initialize a Question object with basic data
					Question question = new Question();
					question.setCourse(courseName);
					question.setQuestionsFileName(questionsFileTest
							.getQuestionsFileName());
					question.setCodeQuestion(codeQuestionVector.get(j).toString());

					// It finds, in the testVector instance, the
					// QuestionFileTest which the question belongs to
					question.setNumberQuestionOrder(i);

					if (doneVector.get(j).toString().trim().equals("false"))
					{
						// Add the previous object to questionVector instance
						questionVector.add(question);
					}
					else
					{
						if (test.getExecutionType().equals(TestEditor.ADAPTIVE))
						{
							// Checks the response to the previous question
							if (answersValueVector.get(j).toString().trim().equals(
									"correct"))
								question.setCorrect(true);
							else
								question.setCorrect(false);

							questionDoneVector.add(question);
						}
					}
				}
			}

			if (test.getExecutionType().equals(TestEditor.ADAPTIVE)
					&& questionDoneVector != null
					&& questionDoneVector.isEmpty() == false)
			{
				questionDoneVector = getInformationValues(evalTestLogStudent
						.getThetaPrevious(), questionDoneVector, irtModel);

				// Loop to read the value of the information function
				// for the already answered questions
				questionInformationValuesVector = new Vector();
				for (int i = 0; i < questionDoneVector.size(); i++)
				{
					Question question = (Question) questionDoneVector.get(i);
					questionInformationValuesVector.add(String.valueOf(question
							.getInformationValue()));
				}

				// Evaluates the stop rule of the test to check if
				// the test has to be finished.
				if (test.getIrtStopCriterion().equals("numberItemsAdministred")
						&& (test.getIrtNumberItemsAdministred() <= questionDoneVector
								.size()))
				{
					// End the test
					endTest = true;
					jButtonEndTestActionPerformed();
					return;
				}
				else
				{
					if (questionInformationValuesVector != null
							&& questionInformationValuesVector.isEmpty() == false)
					{
						// Calculates the value of the Irr function
						irr = adaptive.Irr(questionInformationValuesVector);

						// Calculates the standard error
						standardError = adaptive.standardError(irr);

						if (standardError <= test.getIrtStandardError())
						{
							// End the test
							endTest = true;
							jButtonEndTestActionPerformed();
							return;
						}
					}
				}
			}

			// Calls to the method of this class that order
			// the questions, obtains the data of them, order their
			// answers and shows the data to the user.
			if (executionType.trim().equals(TestEditor.ADAPTIVE))
				showQuestionAdaptive();
			else
				showQuestionClassic();
		}
		else
		{
			JOptionPane.showMessageDialog(this,
					"YOU DON`T HAVE PERMISION TO REPEAT THE TEST.", "WARNING",
					JOptionPane.WARNING_MESSAGE);

			jButtonExitTestActionPerformed();
		}

		setEnabled(jButtonBeginTest, true);
	}

	
	/**
	 * Sends to the server the necessary information to evaluate
	 * the question that has been showed and returns the provisional
	 * score obtained in the classic test.
	 */
	private void jButtonClassicCorrectQuestionActionPerformed()
	{
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		correctClassicQuestion();

		if (score != null
				&& score.trim().equals(
						String.valueOf(evalTestLogStudent.getScore())))
		{
			// Deletes the already showed question from
			// the questions vector 
			questionVector.removeElement(questionInitial);
				
			// Shows the correction of the question
			if (test.getShowQuestionCorrection() == true)
				showCorrection();
			else
				showQuestionClassic();
		}
		else
		{
			JOptionPane.showMessageDialog(this,
					"ERROR: The data of the answered question have not been updated."
							+ "\n" + "Try it later.",
					"Error when updating the data.", JOptionPane.ERROR_MESSAGE);
			try
			{
				JAppletInit();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * Sends to the server the necessary information to evaluate
	 * the question that has been showed and returns the provisional 
	 * score obtained in the adaptive test.
	 */
	private void jButtonAdaptiveCorrectQuestionActionPerformed()
	{
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		correctAdaptiveQuestion();

		if (score != null
				&& score.trim().equals(
						String.valueOf(evalTestLogStudent.getScore())))
		{
			// Deletes the already showed question from the
			// questios vector
			questionVector.removeElement(questionInitial);

			if (test.getIrtStopCriterion().equals("standardError"))
			{
				if (standardError <= test.getIrtStandardError())
				{
					if (test.getShowQuestionCorrection() == true)
					{	
						endTest = true;
						showCorrection();
					}
					else
					{
						endTest = true;
						jButtonEndTestActionPerformed();
					}
				}
				else
				{
					if (test.getShowQuestionCorrection() == true)
					{
						showCorrection();
					}
					else
						showQuestionAdaptive();
				}
			}
			else
			{
				if (questionDoneVector.size() >= test
						.getIrtNumberItemsAdministred())
				{
					if (test.getShowQuestionCorrection() == true)
					{
						endTest = true;
						showCorrection();
					}
					else
					{
						endTest = true;
						jButtonEndTestActionPerformed();
					}
				}
				else
				{
					if (test.getShowQuestionCorrection() == true)
						showCorrection();
					else
						showQuestionAdaptive();
				}
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this,
					"ERROR: The data of the answered question have not been updated."
							+ "\n" + "Try it later.",
					"Error when updating the data.", JOptionPane.ERROR_MESSAGE);

			try
			{
				JAppletInit();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				}
		}
	}


	/**
	 * Sends to the server the necessary information to end the execution
	 * of the test by the student.
	 */
	private void jButtonEndTestActionPerformed()
	{
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if (questionVector.isEmpty() == false && endTest == false)
		{
			int result = JOptionPane
					.showConfirmDialog(
							this,
							"Are you sure of concluding the test?\n"
						 + "If the test conclude, you won't be able to carry out it again.",
							"Confirm", JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				endTest();
			} else {
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}
		} else {
			endTest();
		}

		if (score != null
		 && score.trim().equals(String.valueOf(evalTestLogStudent.getScore()))) {
			// The end
			endedTest = true;

			if (test.getShowFinalInfo()) {
				showEndTest();
			} else {
				jButtonExitTestActionPerformed();
			}

		} else {
			JOptionPane.showMessageDialog(this,
					"ERROR: The test has not been able to be concluded." + "\n"
							+ "Try it later.", "Error when ending the test.",
					JOptionPane.ERROR_MESSAGE);

			try
			{
				JAppletInit();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * Shows a JDialog object of type DlgScore 
	 */
	private void jButtonShowScoreActionPerformed()
	{
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if (dlgScore != null)
		{
			dlgScore.setVisible(false);
			dlgScore.dispose();
			dlgScore = null;
		}

		dlgScore = new DlgScore("Current score of the test", courseName,
					login, testFileName, executionType, evalTestLogStudent,
					jApplet);

		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	
//	/**
//	 * Shows the page where the user access to the test.
//	 */
//	private void jButtonExitTestActionPerformed()
//	{
//		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//
//		try {
//			URL url = new URL(urlGoAfterEndTest);
//			this.getAppletContext().showDocument(url, "_self");
//
//		} catch (java.net.MalformedURLException e) {
//			this.showStatus("MalformedURLException");
//			e.printStackTrace();
//		}
//	}

	
	private void jButtonExitTestActionPerformed() {
		// Calls javascript method "history.back" from the applet
		try {
		  Class c = Class.forName("netscape.javascript.JSObject"); // Also works in IE
		  Method [] ms = c.getMethods();
		  Method getw = null;
		  Method eval = null;
		  for (int i = 0; i < ms.length; i++) {
		      if (ms[i].getName().compareTo("getWindow") == 0) {
		      	getw = ms[i];
		      } else if (ms[i].getName().compareTo("eval") == 0) {
		      	eval = ms[i];
		      }
		  }
		  Object [] parameters = new Object[1];
		  parameters[0] = this; // this is the applet
		  Object jswin = getw.invoke(c, parameters); // This gets the JSObject
		  parameters[0] = "history.back();"; // Javascript command
		  eval.invoke(jswin, parameters); // Call method eval

		} catch (Exception e) {
			try {
				// If something fails, goes to the urlGoAfterEndTest
				URL url = new URL(urlGoAfterEndTest);
				this.getAppletContext().showDocument(url, "_self");

			} catch (java.net.MalformedURLException ex) {
				this.showStatus("MalformedURLException");
				e.printStackTrace();
			}
		}
	}	
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////// OWN METHODS OF THIS CLASS ///////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Calls the necessary methods of this class to order and obtains the
	 * questions to be showed for a classic test and organizes the JApplet
	 * appearance to show a question to the student.
	 */
	private void showQuestionClassic()
	{
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if (timerToAnswer != null)
		{
			timerToAnswer.stop();
			timerToAnswer = null;
		}

		if (questionVector.isEmpty() == false)
		{
			if (repetitionWithoutAnswer == true) 
				contRepetitionWithoutAnswer++;

			question = orderQuestion();

			// Saves the original question without altering the order
			// of the answers to permit locate and delete it from the
			// questionVector after
			questionInitial = question;

			// Obtains the data of the choosed question
			question = getQuestion(question.getCourse(), question
					.getQuestionsFileName(), question.getCodeQuestion());
			if (question != null)
			{
				// Order the answers of the question if necessary
				question = orderAnswers(question);

				// Reconfigurates the interface
				try
				{
					if (dlgScore != null)
					{
						dlgScore.setVisible(false);
						dlgScore.dispose();
						dlgScore = null;
					}

					JLabelInitShowQuestion();
					JTextAreaInitShowQuestion();
					JTextFieldInitShowQuestion();
					JProgressBarTimeInit();
					JProgressBarInit();

					if (question.getAnswersCorrect().size() > 1)
						JCheckBoxInitShowQuestion();
					else
						JRadioButtonInitShowQuestion();

					JButtonInitShowQuestion();
					JPanelInitShowQuestion();
					JAppletInit();

					// The timerToAnswer object that checks the time to answer
					timeToAnswer = 0;
					timerToAnswer = new Timer(1000, new TimerToAnswer());
					timerToAnswer.start();

				}
				catch (Exception e)
				{
					JOptionPane.showMessageDialog(this, "Exception: "
							+ e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this,
						"ERROR: Error when getting question data. " + "\n"
								+ "Try it later.", "Error to load question",
						JOptionPane.ERROR_MESSAGE);

				return;
			}
		}
		else
		{
			if (test.getRepeatWithoutAnswer() == true
					&& questionsWithoutAnswerVector != null
					&& questionsWithoutAnswerVector.isEmpty() == false)
			{
				repetitionWithoutAnswer = false;
				contRepetitionWithoutAnswer = 0;
				sizeRepetitionWithoutAnswer = 0;
				test.setRepeatWithoutAnswer(false);

				// Message to confirm if the student wants to go back to 
				// the question not answered
				int result = JOptionPane.showConfirmDialog(this,
						"Do you want see the questions that you have not "
								+ "answered previously again?",
						"Repetition of questions without answering",
						JOptionPane.YES_NO_OPTION);

				if (result == JOptionPane.YES_OPTION)
				{
					repetitionWithoutAnswer = true;
					questionVector = (Vector) questionsWithoutAnswerVector.clone();
					questionsWithoutAnswerVector = null;
					sizeRepetitionWithoutAnswer = questionVector.size();
					showQuestionClassic();
				}
				else
				{
					repetitionWithoutAnswer = false;

					// Ends the test
					jButtonEndTestActionPerformed();
				}
			}
			else
				// Ends the test
				jButtonEndTestActionPerformed();
		}
	}

	
	/**
	 * Obtains the basic data of a question in 
	 * random or sequential way.
	 * @return Question object 
	 */
	private Question orderQuestion()
	{
		Question question = null; 

		if (test.getQuestionsOrder().equals(TestEditor.RANDOM_STR))
		{
			Random random = new Random();
			int numRandom = random.nextInt(questionVector.size());

			question = (Question) questionVector.get(numRandom);
		}
		else
			question = (Question) questionVector.get(0);

		return question;
	}

	
	/**
	 * Re-order the data of the answers to the question passed as 
	 * parameter in random or sequential way.
	 * @param question Question that will be showed
	 * @return A Question object with their answers ordered
	 */
	private Question orderAnswers(Question question)
	{
		if (test.getAnswersOrder().trim().equals(TestEditor.RANDOM_STR))
		{
			Vector randomVector = new Vector();
			Random random = new Random();

			Vector codeAnswersVector = question.getCodeAnswers();
			Vector textAnswersVector = question.getTextAnswers();
			Vector correctAnswersVector = question.getCorrectAnswers();
			Vector textExplanationVector = question.getTextExplanation();

			Vector codeAnswersVectorAux = new Vector();
			Vector textAnswersVectorAux = new Vector();
			Vector correctAnswersVectorAux = new Vector();
			Vector textExplanationVectorAux = new Vector();

			while (randomVector.size() < codeAnswersVector.size())
			{
				int numRandom = random.nextInt(question.getCodeAnswers().size());
				if (randomVector.contains(String.valueOf(numRandom)) == false)
				{
					randomVector.add(String.valueOf(numRandom));

					codeAnswersVectorAux.add(codeAnswersVector.get(numRandom)
							.toString());
					textAnswersVectorAux.add(textAnswersVector.get(numRandom)
							.toString());
					correctAnswersVectorAux.add(correctAnswersVector.get(numRandom)
							.toString());
					textExplanationVectorAux.add(textExplanationVector
							.get(numRandom).toString());
				}
			}

			codeAnswersVector = null;
			textAnswersVector = null;
			correctAnswersVector = null;
			textExplanationVector = null;

			question.setCodeAnswers(codeAnswersVectorAux);
			question.setTextAnswers(textAnswersVectorAux);
			question.setCorrectAnswers(correctAnswersVectorAux);
			question.setTextExplanation(textExplanationVectorAux);
		}

		return question;
	}


	/**
	 * Calls the necessary methods of this class to order and obtain
	 * the questions to be showed for an adaptive test and organizes
	 * the JApplet appearance to show a question to the student.
	 */
	private void showQuestionAdaptive()
	{
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if (timerToAnswer != null)
		{
			timerToAnswer.stop();
			timerToAnswer = null;
		}

		if (questionVector.isEmpty() == false)
		{
			theta = evalTestLogStudent.getTheta();

			if (theta == 99999.0 || theta == 99999)
				theta = test.getIrtInitialProficiency();

			// Calls to the method of this class that returns
			// the vector with the information function values for
			// each not maked question of the test.
			if (jPanelStatus != null)
			{
				workerGetInformation = new GetInformationValues(theta,
						questionVector);
				workerGetInformation.start();

				// Repaint the progress bar
				workerRepaintProgressBar = new RepaintProgressBar(
						"Reading information values...");
				workerRepaintProgressBar.start();

				workerTimeControl = new TimeControl();
				workerTimeControl.start();

				try
				{
					workerGetInformation.join();
					workerRepaintProgressBar.join();
				}
				catch (java.lang.InterruptedException e)
				{
					e.printStackTrace();
					}

				workerTimeControl.stop();

				if (taskDone == -1)
				{
					super.setCursor(Cursor
							.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					workerGetInformation.stop();
					workerRepaintProgressBar.stop();
					return;
				}
			}
			else
				questionInformationVector = getInformationValues(theta,
						questionVector, irtModel);

			if (questionInformationVector != null
					&& questionInformationVector.isEmpty() == false)
			{
				// Choose the question with more information to create again
				// the questionInformationValuesVector Vector object.
				Question questionAux = null;
				questionAux = (Question) questionInformationVector.firstElement();
				int index = 0;
				for (int i = 0; i < questionInformationVector.size(); i++)
				{
					Question question = (Question) questionInformationVector.get(i);
					if (question.getInformationValue() > questionAux
							.getInformationValue())
					{
						questionAux = question;
						index = i;
					}
				}

				questionInitial = (Question) questionVector.get(index);
				questionShow = (Question) questionInformationVector.get(index);

				// Obtains the data of the chosen question
				question = getQuestion(questionAux.getCourse(), questionAux
						.getQuestionsFileName(), questionAux.getCodeQuestion());
				if (question != null)
				{
					question = orderAnswers(question);
					
					try
					{
						if (dlgScore != null)
						{
							dlgScore.setVisible(false);
							dlgScore.dispose();
							dlgScore = null;
						}

						JLabelInitShowQuestion();
						JTextAreaInitShowQuestion();
						JTextFieldInitShowQuestion();
						JProgressBarTimeInit();
						JProgressBarInit();

						if (question.getAnswersCorrect().size() > 1)
							JCheckBoxInitShowQuestion();
						else
							JRadioButtonInitShowQuestion();

						JButtonInitShowQuestion();
						JPanelInitShowQuestion();

						JAppletInit();

						timeToAnswer = 0;
						timerToAnswer = new Timer(1000, new TimerToAnswer());
						timerToAnswer.start();

					}
					catch (Exception e)
					{
						JOptionPane.showMessageDialog(this, "Exception: "
								+ e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
								
								e.printStackTrace();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this,
							"ERROR: Error when getting question data. " + "\n"
									+ "Try it later.", "Error to load question",
							JOptionPane.ERROR_MESSAGE);

					return;
				}
			}
			else
			{
				JOptionPane
						.showMessageDialog(this,
								"ERROR: Error when getting information values for questions. "
										+ "\n" + "Try it later.",
								"Error to load information values",
								JOptionPane.ERROR_MESSAGE);

				return;
			}
		}
		else
			// Ends the test
			jButtonEndTestActionPerformed();
	}

	
	/**
	 * Calculates the score of the test agreed to the correct,
	 * without answer and incorrect answers.
	 * @return The score of the test
	 */
	private int calculateScore()
	{
		// Obtains the values to calculate the score
		int correct = evalTestLogStudent.getCorrect();
		int incorrect = evalTestLogStudent.getIncorrect();
		int withoutAnswer = evalTestLogStudent.getWithoutAnswer();
		int totalQuestion = evalTestLogStudent.getTotalQuestion();

		double valueQuestion = 100.0 / totalQuestion;

		if (test.getIncorrectAnswersPenalize() == true)
			incorrect = Integer.valueOf(
					String.valueOf(incorrect
							/ test.getIncorrectAnswersPenalizeNumber())).intValue();
		else
			incorrect = 0;

		if (test.getWithoutAnswersPenalize() == true)
			withoutAnswer = Integer.valueOf(
					String.valueOf(withoutAnswer
							/ test.getWithoutAnswersPenalizeNumber())).intValue();
		else
			withoutAnswer = 0;

		correct = correct - incorrect - withoutAnswer;

		double score = (correct * valueQuestion);

		if (score < 0.0) score = 0.0;

		return Integer.valueOf(String.valueOf(Math.round(score))).intValue();
	}

	
	/**
	 * Obtains the data of the question identified by the question code
	 * passed as parameter.
	 * @param courseName Name of the course.
	 * @param questionsFileName Name of the question file.
	 * @param codeQuestion Code of the question whose data are requested.
	 * @return A Question object with the information of the question requested.
	 */
	private Question getQuestion(String courseName, String questionsFileName,
			String codeQuestion)
	{
		question = null;

		if (test.getShowInitialInfo())
		{
			final String courseNameFinal = courseName;
			final String questionsFileNameFinal = questionsFileName;
			final String codeQuestionFinal = codeQuestion;

			workerGetQuestion = new GetQuestion(courseNameFinal,
					questionsFileNameFinal, codeQuestionFinal);
			workerGetQuestion.start();

			// Repaint the progress bar
			workerRepaintProgressBar = new RepaintProgressBar(
					"Loading question data...");
			workerRepaintProgressBar.start();

			workerTimeControl = new TimeControl();
			workerTimeControl.start();

			try
			{
				workerGetQuestion.join();
				workerRepaintProgressBar.join();
			}
			catch (java.lang.InterruptedException e)
			{
				e.printStackTrace();
				}

			workerTimeControl.stop();

			if (taskDone == -1)
			{
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				workerGetQuestion.stop();
				workerRepaintProgressBar.stop();
			}
		}
		else
			question = getQuestionByCode(courseName, questionsFileName,
					codeQuestion);

		return question;
	}


	/**
	 * Updates the data of the student log file after the
	 * reply of a question that belongs to a classic test.
	 */
	private void correctClassicQuestion()
	{
		// Checks for the checked answers
		codeAnswersCheckVector = new Vector();

		// If there is only a correct answer....
		if (question.getAnswersCorrect().size() == 1)
		{
			for (int i = 0; i < question.getCodeAnswers().size(); i++)
			{
				if (jRadioButtonAnswers[i].isSelected())
				{
					// Obtains the code of the chosen answer 
					int index = question.getTextAnswers().indexOf(
							jTextAreaAnswers[i].getText().trim());

					String codeAnswer = question.getCodeAnswers().get(index)
							.toString();
					codeAnswersCheckVector.add(codeAnswer);
					break;
				}
			}
		}
		else
		{
			for (int i = 0; i < question.getCodeAnswers().size(); i++)
			{
				if (jCheckBoxAnswers[i].isSelected())
				{
					// Obtains the code of the chosen answers
					int index = question.getTextAnswers().indexOf(
							jTextAreaAnswers[i].getText().trim());

					String codeAnswer = question.getCodeAnswers().get(index)
							.toString();
					codeAnswersCheckVector.add(codeAnswer);
				}
			}
		}

		answersCorrectVector = (Vector) question.getAnswersCorrect().clone();

		if (codeAnswersCheckVector.isEmpty())
		{
			// Increase the number of not answered questions in 1
			if (repetitionWithoutAnswer == false)
				evalTestLogStudent.setWithoutAnswer(evalTestLogStudent
						.getWithoutAnswer() + 1);
			answerValue = "withoutAnswer";

			if (test.getRepeatWithoutAnswer() == true
					&& questionsWithoutAnswerVector != null)
				questionsWithoutAnswerVector.add(questionInitial);
		}
		else
		{
			// Corrects the question
			int sizeMin = Math.min(codeAnswersCheckVector.size(),
					answersCorrectVector.size());

			for (int i = 0; i < sizeMin; i++)
			{
				int index = answersCorrectVector.indexOf(codeAnswersCheckVector
						.get(i).toString());
				if (index != -1) answersCorrectVector.removeElementAt(index);
			}

			if (answersCorrectVector.isEmpty())
			{
				// Increase the number of correct answers in 1
				evalTestLogStudent.setCorrect(evalTestLogStudent.getCorrect() + 1);
				answerValue = "correct";

				if (repetitionWithoutAnswer == true)
					evalTestLogStudent.setWithoutAnswer(evalTestLogStudent
							.getWithoutAnswer() - 1);
			}
			else
			{
				// Increase the number of incorrect answers in 1
				evalTestLogStudent
						.setIncorrect(evalTestLogStudent.getIncorrect() + 1);
				answerValue = "incorrect";

				if (repetitionWithoutAnswer == true)
					evalTestLogStudent.setWithoutAnswer(evalTestLogStudent
							.getWithoutAnswer() - 1);
			}
		}

		// Increase the number of maked answers in 1
		if (repetitionWithoutAnswer == false)
			evalTestLogStudent.setQuestionsDone(evalTestLogStudent
					.getQuestionsDone() + 1);

		// Calculates the new score.
		evalTestLogStudent.setScore(calculateScore());

		// Updates the student log file with this score
		score = "";

		final Student studentAux = new Student();

		studentAux.setCourse(student.getCourse());
		studentAux.setFileName(student.getFileName());
		studentAux.setLogin(student.getLogin());

		QuestionsFileTest questionsFileTest = new QuestionsFileTest();
		questionsFileTest.setQuestionsFileName(question.getQuestionsFileName());

		Vector codeQuestionVector = new Vector();
		codeQuestionVector.add(question.getCodeQuestion());
		questionsFileTest.setCodeQuestions(codeQuestionVector);

		Vector answerValueVector = new Vector();
		answerValueVector.add(answerValue);
		questionsFileTest.setAnswersValues(answerValueVector);

		Vector timesVector = new Vector();
		timesVector.add(String.valueOf(timeToAnswer));
		questionsFileTest.setTimes(timesVector);

		Vector doneVector = new Vector();
		doneVector.add("true");
		questionsFileTest.setDone(doneVector);

		AnswersTestLogStudent answersTestLogStudent = new AnswersTestLogStudent();

		answersTestLogStudent.setCourse(student.getCourse());
		answersTestLogStudent.setCodeQuestion(question.getCodeQuestion());
		answersTestLogStudent.setQuestionsFileName(question
				.getQuestionsFileName());
		answersTestLogStudent.setCodeAnswers(codeAnswersCheckVector);

		Vector answersTestLogStudentVector = new Vector();
		answersTestLogStudentVector.add(answersTestLogStudent);
		questionsFileTest.setAnswers(answersTestLogStudentVector);

		EvalTestLogStudent evalTestLogStudentAux = new EvalTestLogStudent();

		Vector questionsFileTestVector = new Vector();
		questionsFileTestVector.add(questionsFileTest);
		evalTestLogStudentAux.setTestVector(questionsFileTestVector);
		evalTestLogStudentAux.setScore(evalTestLogStudent.getScore());
		evalTestLogStudentAux.setCorrect(evalTestLogStudent.getCorrect());
		evalTestLogStudentAux.setIncorrect(evalTestLogStudent.getIncorrect());
		evalTestLogStudentAux.setWithoutAnswer(evalTestLogStudent
				.getWithoutAnswer());
		evalTestLogStudentAux.setQuestionsDone(evalTestLogStudent
				.getQuestionsDone());

		TestLogStudent testLogStudentAux = new TestLogStudent();
		testLogStudentAux.setPath(testLogStudent.getPath());
		testLogStudentAux.setFileName(testLogStudent.getFileName());
		testLogStudentAux.setExecutionType(testLogStudent.getExecutionType());

		Vector evaluatedTestVector = new Vector();
		evaluatedTestVector.add(evalTestLogStudentAux);
		testLogStudentAux.setEvaluatedTest(evaluatedTestVector);

		Vector testVector = new Vector();
		testVector.add(testLogStudentAux);
		studentAux.setTest(testVector);

		// Returns the questions for a question file
		workerCorrectQuestion = new CorrectQuestion(studentAux);
		workerCorrectQuestion.start();

		// Repaint the progress bar
		workerRepaintProgressBar = new RepaintProgressBar(
				"Evaluating the question...");
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try
		{
			workerCorrectQuestion.join();
			workerRepaintProgressBar.join();
		}
		catch (java.lang.InterruptedException e)
		{
			e.printStackTrace();
			}

		workerTimeControl.stop();

		if (taskDone == -1)
		{
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerCorrectQuestion.stop();
			workerRepaintProgressBar.stop();
			return;
		}
	}

	
	/**
	 * Updates the data of the student log file stored 
	 * in the server after the reply of the question
	 * of an adaptive test.
	 */
	private void correctAdaptiveQuestion()
	{
		// Checks for the checked answers
		codeAnswersCheckVector = new Vector();

		// If there is only a correct answer
		if (question.getAnswersCorrect().size() == 1)
		{
			for (int i = 0; i < question.getCodeAnswers().size(); i++)
			{
				if (jRadioButtonAnswers[i].isSelected())
				{
					// Obtains the answer code of the chosen answer
					int index = question.getTextAnswers().indexOf(
							jTextAreaAnswers[i].getText().trim());

					String codeAnswer = question.getCodeAnswers().get(index)
							.toString();
					codeAnswersCheckVector.add(codeAnswer);
					break;
				}
			}
		}
		else
		{
			for (int i = 0; i < question.getCodeAnswers().size(); i++)
			{
				if (jCheckBoxAnswers[i].isSelected())
				{
					int index = question.getTextAnswers().indexOf(
							jTextAreaAnswers[i].getText().trim());

					String codeAnswer = question.getCodeAnswers().get(index)
							.toString();
					codeAnswersCheckVector.add(codeAnswer);
				}
			}
		}

		answersCorrectVector = (Vector) question.getAnswersCorrect().clone();

		if (codeAnswersCheckVector.isEmpty())
		{
			// Increases the number of not answered questions in 1
			if (repetitionWithoutAnswer == false)
				evalTestLogStudent.setWithoutAnswer(evalTestLogStudent
						.getWithoutAnswer() + 1);
			answerValue = "withoutAnswer";
			questionShow.setCorrect(false);

			if (test.getRepeatWithoutAnswer() == true
					&& questionsWithoutAnswerVector != null)
				questionsWithoutAnswerVector.add(questionInitial);
		}
		else
		{
			// Corrects the question
			int sizeMin = Math.min(codeAnswersCheckVector.size(),
					answersCorrectVector.size());

			for (int i = 0; i < sizeMin; i++)
			{
				int index = answersCorrectVector.indexOf(codeAnswersCheckVector
						.get(i).toString());
				if (index != -1) answersCorrectVector.removeElementAt(index);
			}

			if (answersCorrectVector.isEmpty())
			{
				// Increases the number of correct questions in 1
				evalTestLogStudent.setCorrect(evalTestLogStudent.getCorrect() + 1);
				answerValue = "correct";
				questionShow.setCorrect(true);

				if (repetitionWithoutAnswer == true)
					evalTestLogStudent.setWithoutAnswer(evalTestLogStudent
							.getWithoutAnswer() - 1);
			}
			else
			{
				// Increases the number of incorrect questions in 1
				evalTestLogStudent
						.setIncorrect(evalTestLogStudent.getIncorrect() + 1);
				answerValue = "incorrect";
				questionShow.setCorrect(false);

				if (repetitionWithoutAnswer == true)
					evalTestLogStudent.setWithoutAnswer(evalTestLogStudent
							.getWithoutAnswer() - 1);
			}
		}

		// Adds the answered question to the vector of answered questions
		questionDoneVector.add(questionShow);

		// Increases the number of done questions in 1
		evalTestLogStudent
				.setQuestionsDone(evalTestLogStudent.getQuestionsDone() + 1);
		// Calculates the new score
		evalTestLogStudent.setScore(calculateScore());

		// First we recalculate the information of the already showed
		// for the actual theta
		if (jPanelStatus != null)
		{
			workerGetInformation = new GetInformationValues(theta,
					questionDoneVector);
			workerGetInformation.start();

			workerRepaintProgressBar = new RepaintProgressBar(
					"Loading information values...");
			workerRepaintProgressBar.start();

			workerTimeControl = new TimeControl();
			workerTimeControl.start();

			try
			{
				workerGetInformation.join();
				workerRepaintProgressBar.join();
			}
			catch (java.lang.InterruptedException e)
			{
				e.printStackTrace();
				}

			workerTimeControl.stop();

			if (taskDone == -1)
			{
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				workerGetInformation.stop();
				workerRepaintProgressBar.stop();
			}
			else
				questionDoneVector = questionInformationVector;
		}
		else
			questionDoneVector = getInformationValues(theta, questionDoneVector,
					irtModel);

		// After we calculate the value of the si() function for all the
		// answered questions of the test.
		questionFunctionSIVector = new Vector();
		questionInformationValuesVector = new Vector();

		for (int i = 0; i < questionDoneVector.size(); i++)
		{
			Question question = (Question) questionDoneVector.get(i);
			int correct = 0;
			if (question.isCorrect())
				correct = 1;
			else
				correct = 0;

			double si = adaptive.si(theta, question.getDiscrimination(), question
					.getDifficulty(), question.getGuessing(), correct);

			questionFunctionSIVector.add(String.valueOf(si));

			questionInformationValuesVector.add(String.valueOf(question
					.getInformationValue()));
		}

		// Calculates the value of the Irr function
		irr = adaptive.Irr(questionInformationValuesVector);

		// Calculates the standar error
		standardError = adaptive.standardError(irr);

		// Calculates the estimated theta
		theta_estimated = adaptive.estimatedTheta(theta,
				questionFunctionSIVector, irr);
		
		// Sets the current theta value
		evalTestLogStudent.setThetaPrevious(theta);
		evalTestLogStudent.setTheta(theta_estimated);

		// Sets the standar error value
		evalTestLogStudent.setStandardError(standardError);

		// Updates the student log file 
		score = "";

		final Student studentAux = new Student();

		studentAux.setCourse(student.getCourse());
		studentAux.setFileName(student.getFileName());
		studentAux.setLogin(student.getLogin());

		QuestionsFileTest questionsFileTest = new QuestionsFileTest();
		questionsFileTest.setQuestionsFileName(question.getQuestionsFileName());

		Vector codeQuestionVector = new Vector();
		codeQuestionVector.add(question.getCodeQuestion());
		questionsFileTest.setCodeQuestions(codeQuestionVector);

		Vector answerValueVector = new Vector();
		answerValueVector.add(answerValue);
		questionsFileTest.setAnswersValues(answerValueVector);

		Vector timesVector = new Vector();
		timesVector.add(String.valueOf(timeToAnswer));
		questionsFileTest.setTimes(timesVector);

		Vector doneVector = new Vector();
		doneVector.add("true");
		questionsFileTest.setDone(doneVector);

		AnswersTestLogStudent answersTestLogStudent = new AnswersTestLogStudent();

		answersTestLogStudent.setCourse(student.getCourse());
		answersTestLogStudent.setCodeQuestion(question.getCodeQuestion());
		answersTestLogStudent.setQuestionsFileName(question
				.getQuestionsFileName());
		answersTestLogStudent.setCodeAnswers(codeAnswersCheckVector);

		Vector answersTestLogStudentVector = new Vector();
		answersTestLogStudentVector.add(answersTestLogStudent);
		questionsFileTest.setAnswers(answersTestLogStudentVector);

		EvalTestLogStudent evalTestLogStudentAux = new EvalTestLogStudent();

		Vector questionsFileTestVector = new Vector();
		questionsFileTestVector.add(questionsFileTest);
		evalTestLogStudentAux.setTestVector(questionsFileTestVector);
		evalTestLogStudentAux.setScore(evalTestLogStudent.getScore());

		evalTestLogStudentAux.setThetaPrevious(theta);
		evalTestLogStudentAux.setTheta(theta_estimated);
		evalTestLogStudentAux.setStandardError(standardError);

		evalTestLogStudentAux.setCorrect(evalTestLogStudent.getCorrect());
		evalTestLogStudentAux.setIncorrect(evalTestLogStudent.getIncorrect());
		evalTestLogStudentAux.setWithoutAnswer(evalTestLogStudent
				.getWithoutAnswer());
		evalTestLogStudentAux.setQuestionsDone(evalTestLogStudent
				.getQuestionsDone());

		TestLogStudent testLogStudentAux = new TestLogStudent();
		testLogStudentAux.setPath(testLogStudent.getPath());
		testLogStudentAux.setFileName(testLogStudent.getFileName());
		testLogStudentAux.setExecutionType(testLogStudent.getExecutionType());

		Vector evaluatedTestVector = new Vector();
		evaluatedTestVector.add(evalTestLogStudentAux);
		testLogStudentAux.setEvaluatedTest(evaluatedTestVector);

		Vector testVector = new Vector();
		testVector.add(testLogStudentAux);
		studentAux.setTest(testVector);

		workerCorrectQuestion = new CorrectQuestion(studentAux);
		workerCorrectQuestion.start();

		workerRepaintProgressBar = new RepaintProgressBar(
				"Evaluating the question...");
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try
		{
			workerCorrectQuestion.join();
			workerRepaintProgressBar.join();
		}
		catch (java.lang.InterruptedException e)
		{
			e.printStackTrace();
			}

		workerTimeControl.stop();

		if (taskDone == -1)
		{
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerCorrectQuestion.stop();
			workerRepaintProgressBar.stop();
			return;
		}
	}

	
	/**
	 * Calls the EndTest inner class that updates the data
	 * of the student log file and the test configuration file
	 * to end the execution of a test by the student.
	 */
	private void endTest() {
		score = "";

		final String courseName = student.getCourse();
		final String testFileName = testLogStudent.getFileName();
		final String login = student.getLogin();
		final double lastScore = evalTestLogStudent.getScore();
		final String executionType = testLogStudent.getExecutionType();
		final double theta = evalTestLogStudent.getTheta();
		final double standardError = evalTestLogStudent.getStandardError();

		String conceptAux = "";
		if (test.getTestType().trim().equals(AppletTestEngine.ACTIVITY)) {
			conceptAux = test.getConceptVector().firstElement().toString().trim();
		} else {
			conceptAux = test.getAbstractConcept().trim();
			System.out.println("Abstract concept = " + conceptAux);
			for (int i = 0; i < test.getConceptVector().size(); i++) {
				System.out.println("Associated concept = " + test.getConceptVector().get(i).toString());
			}
		}

		final String concept = conceptAux;

		workerEndTest = new EndTest(courseName, testFileName, login, lastScore,
											 executionType, theta, standardError, concept);

		workerEndTest.start();

		if (stopJApplet == false && appletInit == true) {
			workerRepaintProgressBar = new RepaintProgressBar("Ending the test...");
			workerRepaintProgressBar.start();

			workerTimeControl = new TimeControl();
			workerTimeControl.start();

			try {
				workerEndTest.join();
				workerRepaintProgressBar.join();
			} catch (java.lang.InterruptedException e) {
				e.printStackTrace();
			}

			workerTimeControl.stop();

			if (taskDone == -1) {
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				workerEndTest.stop();
				workerRepaintProgressBar.stop();
			}
		} else {
			while (taskDone == 0) {
				try {
					Thread.sleep(100);
				} catch (java.lang.InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		//Loc Nguyen add October 09 2009
//		URL base = getCodeBase();
//        String path = base.getPath();
//        String pathttemp = path.substring(1, path.length());
//        int index = pathttemp.indexOf("/");
//        index++;
//
//        String context = path.substring(0, index);
//        try {
//        	URL url = new URL(base.getProtocol() + "://" + base.getHost()+":" + base.getPort() + 
//        			context + "/AppletTestEngineCach");
//	    	
//        	HttpURLConnection uc = (HttpURLConnection) url.openConnection();
//	    	uc.setDoOutput(true);
//	    	uc.setUseCaches(false);
//	    	uc.setRequestMethod("POST");
//			
//	    	ObjectOutputStream objectOutputStream = new ObjectOutputStream(uc.getOutputStream());
//	    	objectOutputStream.writeObject(student);
//	    	objectOutputStream.flush();
//	    	objectOutputStream.close();
//	    	
//	    	uc.getResponseCode();
//	    	uc.disconnect();
//        }
//        catch(Exception e) {
//        	String err = "There is error: " + e.getMessage() + " in AppletTestEngin.endTest";
//        	JOptionPane.showMessageDialog(this, err, "Error in AppletTestEngin.endTest", JOptionPane.ERROR_MESSAGE);
//        	System.out.println(e.getMessage());
//        }
		
	}


	/**
	 * Configurates the appearance of the JApplet object to show the
	 * result of the correction of the question.
	 */
	private void showCorrection()
	{
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if (timerToAnswer != null)
		{
			timerToAnswer.stop();
			timerToAnswer = null;
		}

		try
		{
			// DON'T MODIFY THE ORDER
			JLabelInitShowCorrection();
			JTextAreaInitShowCorrection();
			JProgressBarInit();
			JButtonInitShowCorrection();
			JPanelInitShowCorrection();
			JAppletInit();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "Exception: " + e.getMessage(),
					"Info", 1);
					e.printStackTrace();
		}
	}

	
	/**
	 * Configurates the appearance of the JApplet to show the 
	 * end of the test.
	 */
	private void showEndTest() {

		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Calls the server to generate the graphic
		chart = getChart(courseName, login, testFileName,
				 			  executionType, 390, 295, true, "AppletTestEngine");

		if (timerToAnswer != null) {
			timerToAnswer.stop();
			timerToAnswer = null;
		}

		try {
			// DON'T MODIFY THE ORDER
			JPanelInitShowEndTest();
			JLabelInitShowEndTest();
			JButtonInitShowEndTest();
			JTextFieldInitShowEndTest();
			JAppletInitShowEndTest();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception: " + e.getMessage(), "Info", 1);
			e.printStackTrace();
		}
	}

	
	////////////////////////////////////////////////////////////////////////////////
	////////////////// METHODS THAT INVOKE THE SERVLETS OF THE SERVER //////////////
	////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Invokes the GetIconPath servlet of the server. This servlet
	 * returns the relative path where the icons used by the JFrame
	 * and the JInternal frame used by this tool are located.
	 * @return String
	 */
	private String getIconAndItemsPath()
	{
		// Stores the URL that the applet has been downloaded from 
		URL url = null;
		String line = "";

		// Builds the string that will be used for the request to the servlet
		String consult = "userLogin=" + "" + "&userPassword=" + "";

		// Connects with the servlet
		try
		{
			String petition = wowPath
					+ "/servlet/authorservlets.es.uco.WOW.TestEditorServlets."
					+ "GetIconAndItemsPath";
			url = new URL(getCodeBase(), petition);

			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Sends the request
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection
					.getOutputStream());
			bufferOut.write(consult);
			bufferOut.flush();

			// Recept the response from the servlet
			BufferedReader bufferIn = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			// Reads data
			String lineTemp = "";
			while ((lineTemp = bufferIn.readLine()) != null)
				line = line.concat(lineTemp) + "\n";

			bufferOut.close();
			bufferIn.close();

			return line;

		}
		catch (Exception e)
		{
			taskDone = 1;
			JOptionPane.showMessageDialog(this, e.getMessage(), "Icon Error", 1);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Calls the server and obtains a Test object.
	 * If there is any error, this method returns null.
	 * @param courseName Name of the course.
	 * @param testFileName Name of the test file.
	 * @return A Test object or null if an error ocurr
	 */
	private Test getTest(String courseName, String testFileName)
	{
		Test test; // Stores the data of the test
		URL url = null; // Stores the URL of the server

		// Build the data string to be send to the server
		String consult = "courseName=" + courseName + "&testFileName="
				+ testFileName;

		// Connect with the server
		try
		{
			String petition = wowPath
					+ "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.GetTest";
			url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Sends data to locate the Test File.
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection
					.getOutputStream());
			bufferOut.write(consult);
			bufferOut.flush();

			// Get the response
			ObjectInputStream bufferIn = new ObjectInputStream(connection
					.getInputStream());

			test = (Test) bufferIn.readObject();

			// Closes the communication
			bufferOut.close();
			bufferIn.close();

			// Returns the response of the server
			return test;
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
			taskDone = 1;

			// If some exception occurs while trying to connect to the server,
			// the reason is shown and it returns null
			JOptionPane
					.showMessageDialog(this, e.getMessage(),
							"Error when connecting to the server",
							JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	
	/**
	 * Sends to the server the necessary data to locate or creates,
	 * if necessary, a student log file associated with the test that will be
	 * done. The response of the server is the data of it stored in a Student 
	 * object. Returns null if any error occur.
	 * 
	 * @param courseName Name of the course.
	 * @param testFileName Name of the test file.
	 * @param login Login of the student that is going to make the test.
	 * @param executionType Type of test ("classic" or "adaptive")
	 * @param theta Knowledge of the student. If not needed, it must contain
	 *   the 99999 value.
	 * @param standardError Indicates the error taken to calculate the
	 *   estimation of the knowledge of the student. If not needed, it must
	 *   contain the 99999 value.
	 * @return a Student object
	 */
	private Student beginTest(String courseName, String testFileName,
			String login, String executionType, double theta, double standardError)
	{
		
		StringBuffer query = new StringBuffer();
		query.append("courseName=").append(courseName);
		query.append("&testFileName=").append(testFileName);
		query.append("&login=").append(login);
		query.append("&executionType=").append(executionType);
		query.append("&theta=").append(String.valueOf(theta));
		query.append("&standardError=").append(String.valueOf(standardError));

		try {
			String petition = wowPath
					+ "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.BeginTest";
			URL url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Sends the data
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(query.toString());
			bufferOut.flush();

			// Receives the response
			ObjectInputStream bufferIn = new ObjectInputStream(connection.getInputStream());

			Student student = (Student) bufferIn.readObject();

			bufferOut.close();
			bufferIn.close();

			return student;

		} catch (Exception e) {
			e.printStackTrace();
			taskDone = 1;

			JOptionPane.showMessageDialog(this, e.getMessage(),
								"Error connecting to the server",
								JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	
	/**
	 * Sends to the server the necessary data to locate the log student file
	 * and updates it with the data sent. The server returns the score sent
	 * in the 'student' object passed as parameter. If any error ocurr, this 
	 * method returns null. Otherwise returns the score received from the server
	 * @param student Necessary data to evaluate the student log file
	 * @return 
	 */
	private String updateEvaluatedTest(Student student)
	{
		String score = ""; 
		URL url = null; 
		
		// Connect to the server
		try
		{
			String petition = wowPath
					+ "/servlet/authorservlets.es.uco.WOW.TestEditorServlets."
					+ "UpdateEvaluatedTest";
			url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Sends data
			ObjectOutputStream bufferOut = new ObjectOutputStream(connection
					.getOutputStream());
			bufferOut.writeObject(student);
			bufferOut.flush();

			// Receives response
			ObjectInputStream bufferIn = new ObjectInputStream(connection
					.getInputStream());

			// DON'T MODIFY THE ORDER
			score = (String) bufferIn.readObject();

			bufferOut.close();
			bufferIn.close();

			return score;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			taskDone = 1;

			JOptionPane
					.showMessageDialog(this, e.getMessage(),
							"Error when connecting to the server",
							JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	
	/**
	 * Sends the necessary data to the server to locate the student
	 * log file. The server will update this file to set the test
	 * as finished. This operation is done in the test configuration 
	 * file and in the student log file. The server returns the same 
	 * score that was sent to the server.
	 * This method returns null if any error occur.
	 * @param courseName Name of the course
	 * @param testFileName Name of the test file
	 * @param login Login of the student that is doing the test.
	 * @param lastScore Final score of the student in the test.
	 * @param executionType Type of test ("classic" or "adaptive")
	 * @param theta Knowledge of the student. If not needed, use
	 *   the 99999 value.
	 * @param standardError Standar error taken to calculate the
	 *   knowledge of the student. If not needed, use the 99999 value.
	 * @param concept Indicates the name of the concept that 
	 *   is being evaluated
	 * @return A String object with the score sent or null in error case.
	 */
	private String endTheTest(
			final String courseName, final String testFileName, final String login,
			final double lastScore, final String executionType, final double theta,
			final double standardError, final String concept) {
	//{
		StringBuffer consult = new StringBuffer();
		consult.append("courseName=").append(courseName);
		consult.append("&testFileName=").append(testFileName);
		consult.append("&login=").append(login);
		consult.append("&lastScore=").append(String.valueOf(lastScore));
		consult.append("&executionType=").append(executionType);
		consult.append("&theta=").append(String.valueOf(theta));
		consult.append("&standardError=").append(String.valueOf(standardError));
		consult.append("&concept=").append(concept);

		// Connects with the server
		try {
			String petition = wowPath + "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.EndTest";
			URL url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Sends data
			OutputStreamWriter bufferOut = new OutputStreamWriter(connection.getOutputStream());
			bufferOut.write(consult.toString());
			bufferOut.flush();

			// Receives response
			ObjectInputStream bufferIn = new ObjectInputStream(connection
					.getInputStream());

			// Reads the data sent by the server
			String score = (String) bufferIn.readObject();

			bufferOut.close();
			bufferIn.close();

			return score;

		} catch (Exception e) {
			e.printStackTrace();
			taskDone = 1;
			JOptionPane.showMessageDialog(this, e.getMessage(),
							"Error connecting to the server", JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	
	/**
	 * Sends the necessary data to the server to locate the
	 * student log file. When located, the server generates
	 * a graphic about the current execution of the test. 
	 * This image will be returned in a byte array.
	 * 
	 * @param courseName Name of the course
	 * @param login Login of the student
	 * @param testFileName Name of the test file
	 * @param executionType Type of test ("classic" or "adaptive")
	 * @param width Width of the image
	 * @param height Height of the image
	 * @param ended Indicates if the test is finished or not
	 * @param chartClient Type of graphic requested. In this case, is
	 *   "AppletTestEngine". 
	 * @return A byte array with the data of the image.
	 */
	protected byte [] getChart(String courseName, String login, String testFileName,
			String executionType, int width, int height, boolean ended,
			String chartClient)
	{
		byte[] chart = null; 
		URL url = null;

		String consult = "courseName=" + courseName + "&testFileName="
				+ testFileName + "&login=" + login + "&executionType="
				+ executionType + "&width=" + String.valueOf(width) + "&height="
				+ String.valueOf(height) + "&ended=" + String.valueOf(ended)
				+ "&chartClient=" + chartClient;

		try {
			String petition = wowPath
					+ "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.GetChart";
			url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();
			
			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			OutputStreamWriter bufferOut = new OutputStreamWriter(connection
					.getOutputStream());
			bufferOut.write(consult);
			bufferOut.flush();

			ObjectInputStream bufferIn = new ObjectInputStream(connection
					.getInputStream());

			chart = (byte[]) bufferIn.readObject();

			bufferOut.close();
			bufferIn.close();

			return chart;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			taskDone = 1;

			JOptionPane
					.showMessageDialog(this, e.getMessage(),
							"Error connecting to the server",
							JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	
	/**
	 * Sends the necessary data to the server to get the data
	 * of a question. This question is identified by a code.
	 * The server returns the data of the question in a 
	 * Question object. Returns null if an error has occur.
	 * 
	 * @param courseName Name of the course
	 * @param questionsFileName Name of the question file.
	 * @param codeQuestion Code of the requested question
	 * @return A Question object with the data of the requested question
	 *   or null if an error occur
	 */
	private Question getQuestionByCode(String courseName,
			String questionsFileName, String codeQuestion)
	{
		Question question; 
		URL url = null;

		String consult = "courseName=" + courseName + "&questionsFileName="
				+ questionsFileName + "&codeQuestion=" + codeQuestion;

		try
		{
			String petition = wowPath
					+ "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.GetQuestionByCode";
			url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			OutputStreamWriter bufferOut = new OutputStreamWriter(connection
					.getOutputStream());
			bufferOut.write(consult);
			bufferOut.flush();

			ObjectInputStream bufferObjectIn = new ObjectInputStream(connection
					.getInputStream());

			question = (Question) bufferObjectIn.readObject();

			bufferOut.close();
			bufferObjectIn.close();

			return question;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			taskDone = 1;

			JOptionPane
					.showMessageDialog(this, e.getMessage(),
							"Error when connecting to the server",
							JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	
	/**
	 * Sends the necessary data to the server to locate the questions
	 * that are stored in the questionVector vector. The server will
	 * calculate the value of the information function for each question 
	 * that is contained in that vector and returns a Vector object 
	 * with that values. If any error occur, this method returns null.
	 * 
	 * @param theta Knowledge of the student.
	 * @param questionVector Vector object with a question on each 
	 *   position. Each Question object contains the following data:
	 *   - Name of the course
	 *   - Name of the question file
	 *   - Code of the question
	 *   This data is used to calculate the value of the information function.
	 * @param irtModel Type of execution of the adaptive test (1, 2 or 3 parameters)
	 * @return A Vector object with the values of the information function�
	 *   calculated by the server.
	 */
	private Vector getInformationValues(double theta, Vector questionVector,
			int irtModel)
	{
		try {
			String petition = wowPath
					+ "/servlet/authorservlets.es.uco.WOW.TestEditorServlets.GetInformationValues";

			URL url = new URL(codeBase, petition);

			URLConnection connection = url.openConnection();

			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Sends the data to the server
			ObjectOutputStream bufferObjectOut = new ObjectOutputStream(connection
					.getOutputStream());

			// DON'T MODIFY THE ORDER !!!!!!!!!!!!!!!
			bufferObjectOut.writeDouble(theta);
			bufferObjectOut.writeObject(questionVector);
			bufferObjectOut.writeInt(irtModel);

			bufferObjectOut.flush();

			// Receives the response
			ObjectInputStream bufferObjectIn = new ObjectInputStream(connection
					.getInputStream());

			Vector questionInformationVector = (Vector) bufferObjectIn.readObject();

			bufferObjectOut.close();
			bufferObjectIn.close();

			return questionInformationVector;

		} catch (Exception e) {
			e.printStackTrace();
			taskDone = 1;

			JOptionPane.showMessageDialog(this, e.getMessage(),
										"Error connecting to the server",
										JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	/**
	 * Enables or disables the button that is received as parameter. Checks before if this button is
	 * null.
	 * @param button JButton object
	 * @param enabled Indicates if the button must be enabled or disabled.
	 */
	private static void setEnabled(JButton button, boolean enabled) {
		if (button != null)
			button.setEnabled(enabled);
	}
	
	
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////// INNER CLASSES ///////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * This class invocates the beginTest method of the AppletTestEngine 
	 * class to obtain / create the student log file and obtain the
	 * data to do the test.
	 */
	private class BeginTest extends Thread {
		String aCourseName = "";

		String aTestFileName = "";

		String aLogin = "";

		String aExecutionType = "";

		/**
		 * Constructor
		 * @param courseName Name of the course
		 * @param testFileName Name of the test file
		 * @param login Login of the student
		 * @param executionType Type of test ("classic" or "adaptive")
		 */
		public BeginTest(String theCourseName, String theTestFileName, String theLogin, String theExecutionType) {
			this.aCourseName = theCourseName;
			this.aTestFileName = theTestFileName;
			this.aLogin = theLogin;
			this.aExecutionType = theExecutionType;
		}

		public void run() {
			taskDone = 0;

			try {
				Thread.sleep(100);
			} catch (java.lang.InterruptedException e) {
				e.printStackTrace();
			}

			if (aExecutionType.trim().equals(TestEditor.ADAPTIVE))
				student = beginTest(aCourseName, aTestFileName, aLogin, aExecutionType, test
						.getIrtInitialProficiency(), -1);
			else
				student = beginTest(aCourseName, aTestFileName, aLogin, aExecutionType, 99999, 99999);
			taskDone = 1;
		}
	}

	
	/**
	 * This class invocates the getQuestionByCode method of the AppletTestEngine class to obtain the
	 * data of the question that will be showed.
	 */
	private class GetQuestion extends Thread {
		String courseName = "";

		String questionsFileName = "";

		String codeQuestion = "";

		/**
		 * Constructor
		 * @param courseName Name of the course
		 * @param questionsFileName Name of the question file
		 * @param codeQuestion Code of the requested question
		 */
		public GetQuestion(String courseName, String questionsFileName, String codeQuestion) {
			this.courseName = courseName;
			this.questionsFileName = questionsFileName;
			this.codeQuestion = codeQuestion;
		}

		public void run() {
			taskDone = 0;

			try {
				Thread.sleep(100);
			} catch (java.lang.InterruptedException e) {
				e.printStackTrace();
			}

			question = getQuestionByCode(courseName, questionsFileName, codeQuestion);

			taskDone = 1;
		}
	}

	
	/**
	 * This class invocates the updateEvaluatedTest method of the AppletTestEngine class to update
	 * the data of the student log file after the answer of a question.
	 */
	private class CorrectQuestion extends Thread {
		Student student = null;

		/**
		 * Constructor
		 * @param student Student object
		 */
		public CorrectQuestion(Student student) {
			this.student = student;
		}

		public void run() {
			taskDone = 0;

			try {
				Thread.sleep(100);
			} catch (java.lang.InterruptedException e) {
				e.printStackTrace();
			}

			score = updateEvaluatedTest(student);

			taskDone = 1;
		}
	}


	/**
	 * This class invocates the endTest method of the AppletTestEngine class to update the data of
	 * the student log file and the configuration test file to end the execution of the test by the
	 * student.
	 */
	private class EndTest extends Thread {
		private String courseName = "";

		private String testFileName = "";

		private String login = "";

		private double lastScore = 0;

		private String executionType = "";

		private double theta = 0;

		private double standardError = -1;

		private String concept = "";

		/**
		 * Constructor
		 * @param courseName Name of the course
		 * @param testFileName Name of the test file
		 * @param login Login of the student
		 * @param lastScore Score obtained at the end of the test
		 * @param executionType Type of test
		 * @param theta Knowledge of the student.
		 * @param standardError Standar error taken to calculate the knowledge
		 * @param concept Concept that is evaluated in the test.
		 */
		public EndTest(final String courseName, final String testFileName, final String login,
				final double lastScore, final String executionType, final double theta,
				final double standardError, final String concept) {
		// {
			this.courseName = courseName;
			this.testFileName = testFileName;
			this.login = login;
			this.lastScore = lastScore;
			this.executionType = executionType;
			this.theta = theta;
			this.standardError = standardError;
			this.concept = concept;
		}

		public void run() {
			taskDone = 0;
			try {
				Thread.sleep(100);
			} catch (java.lang.InterruptedException e) {
				e.printStackTrace();
			}

			score = endTheTest(courseName, testFileName, login, lastScore, executionType, theta, standardError,
					concept);
			taskDone = 1;
		}
	}

	
	/**
	 * This class invocates the getInformationValues method of the 
	 * AppletTestEngine class to obtain the vector with the values
	 * of the information function for each question of the test
	 * that still hasn't been shown.
	 */
	private class GetInformationValues extends Thread {
		double theta = 0;

		Vector questionVector = null;

		public GetInformationValues(double theta, Vector questionVector) {
			this.theta = theta;
			this.questionVector = questionVector;
		}

		public void run() {
			taskDone = 0;

			try {
				Thread.sleep(100);
			} catch (java.lang.InterruptedException e) {
				e.printStackTrace();
			}

			questionInformationVector = getInformationValues(theta, questionVector, irtModel);
			taskDone = 1;
		}
	}

	
	/**
	 * This class checks that the progress bar don't get block.
	 */
	private class TimeControl extends Thread {
		public TimeControl() {}

		public void run() {
			try {
				Thread.sleep(20000);

				taskDone = -1;

				jLabelStatus.setText("Overcome time of wait. Try it later.");
			} catch (java.lang.InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * This class updates the value and repaints the value of the progress bar.
	 */
	private class RepaintProgressBar extends Thread {
		String message = "";

		public RepaintProgressBar(String message) {
			this.message = message;
		}

		public void run() {
			repaintProgressBar(message);
		}
	}

	
	/**
	 * This class measures the time that the student has taken to answer the question and evaluates
	 * if the clock must be repainted.
	 */
	private class TimerToAnswer implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			timeToAnswer++;

			if (timeToAnswerInit > 0) {
				timeToAnswerInit--;

				if (timeToAnswerInit > 60) {
					jTextFieldTimeToAnswerMinutes.setText(String.valueOf(timeToAnswerInit / 60));
					jTextFieldTimeToAnswerSeconds.setText(String.valueOf(timeToAnswerInit % 60));
				} else {
					if (jTextFieldTimeToAnswerMinutes != null)
						jTextFieldTimeToAnswerMinutes.setText("0");

					jTextFieldTimeToAnswerSeconds.setText(String.valueOf(timeToAnswerInit));
				}

				jProgressBarTime.setValue(timeToAnswer);
			}

			if (timeToAnswerInit <= 0 && Integer.valueOf(test.getTimeOfAnswer()).intValue() > 0) {
				timerToAnswer.stop();

				JOptionPane.showMessageDialog(jApplet.getParent(), "The time to answer has concluded.",
						"Exceeded time", JOptionPane.WARNING_MESSAGE);

				if (executionType.trim().equals(TestEditor.ADAPTIVE))
					jButtonAdaptiveCorrectQuestionActionPerformed();
				else
					jButtonClassicCorrectQuestionActionPerformed();
			}
		}
	}

} // End of AppletTestEngine class