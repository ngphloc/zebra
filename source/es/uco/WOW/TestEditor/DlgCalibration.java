package es.uco.WOW.TestEditor;

import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import es.uco.WOW.Utils.Course;
import es.uco.WOW.Utils.EvalTestLogStudent;
import es.uco.WOW.Utils.Question;
import es.uco.WOW.Utils.QuestionsFileTest;
import es.uco.WOW.Utils.Student;
import es.uco.WOW.Utils.StudentTest;
import es.uco.WOW.Utils.Test;
import es.uco.WOW.Utils.TestLogStudent;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p> 
 * @version 1.0
 */

/**
 * This class shows the list of courses that the user is owner and the list
 * of question files for each one of that courses. The window allow to select
 * a question file to update the statistical data of the questions stored in it
 * LAST MODIFICATION: 06-10-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class DlgCalibration extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Stores the list of names of the questions files
	 * for each course 
	 */
	private Vector courseVector;
	/**
	 * Stores the data of the test to calibrate
	 */
	private Test test = null;
	/**
	 * Stores the data of the studens that have made the test
	 */
	private Vector studentTestVector = null;
	/**
	 * Indicates of the JComboCourse has changed or not
	 */
	private boolean changeJComboCourse = true;
	/**
	 * Indicates if the job has been done or not
	 */
	private boolean calibrated = false;

	/**
	 * Graphical components of the window 
	 */
	private TestEditor parent;
	private JPanel jPanelTitle;
	private JPanel jPanelMain;
	private JScrollPane jScrollPaneTextAreaStatus;
	private JScrollPane jScrollPaneListQuestionsFile;
	private JLabel jLabelTitle;
	private JLabel jLabelCourse;
	private JLabel jLabelQuestionsFile;
	private JLabel jLabelIteration;
	private JTextField jTextFieldIteration;
	private JButton ContinueButton;
	private JButton OkButton;
	private JButton StopButton;
	private JButton ExitButton;
	private JComboBox jComboCourse;
	private JList jListQuestionsFile;
	private JTextArea jTextAreaStatus;
	private JProgressBar jProgressBarQuestions;
	private JProgressBar jProgressBarQuestionsFile;
	private String title = "";

	/**
	 * Variables to update the statistical data
	 */
	private Vector questionsFileNameVector = null;
	private Vector questionsFileNameVectorCalibre = null;
	private Calibrate workerCalibrate = null;
	private Vector skillVector = null;
	private Vector correctVector = null;
	private double CPT = 0;
	private double A = 0;
	private int MAXIT = 0;
	private int NXL = 0;
	private int NIT = 0;
	private int k = 0;
	private double sfw = 0, sfwv = 0, sfwv2 = 0, sfwx = 0, sfwxv = 0, sfwx2 = 0;
	private double PI = 0, DEV = 0, PH = 0, W = 0, V = 0;
	private double p1 = 0, p2 = 2, p3 = 0, p4 = 0, p5 = 0, p6 = 0;
	private double DIFF = 0, DM = 0, DCPT = 0, DA = 0;


	/**
	 * Public constructor.
	 * @param title Title of the window
	 * @param courseVector Contains in each position a Course object with the data of a course. 
	 * @param theParent Reference to the TestEditor tool.
	 */
	public DlgCalibration(final String title, final Vector courseVector, final TestEditor theParent) {
		// Calls the parent constructor
		super(theParent, title, true);

		// Sets the look and feel
		try {
			UIManager.setLookAndFeel(parent.lookAndFeelClassName);
		} catch (Exception e) {}

		// Sets the values of the class member variables
		this.title = title;
		this.courseVector = courseVector;
		this.parent = theParent;

		// Initializes the components of the window
		try {
			// DON'T MODIFY THE ORDER!!!!!!!!!
			JLabelInit();
			JButtonInit();
			JTextFieldInit();
			JTextAreaInit();
			JProgressBarInit();
			JComboInit();
			JListInit();
			JPanelInit();
			JDialogInit();
		} catch (Exception e) {}
	}

	/**
	 * Initializes the JPanel objects of the window.
	 */
	private void JPanelInit() {
		jPanelTitle = new JPanel();
		jPanelMain = new JPanel();

		jPanelTitle.setDoubleBuffered(true);
		jPanelMain.setDoubleBuffered(true);

		// Sets their size, border and layout
		jPanelTitle.setLayout(null);
		jPanelMain.setBorder(BorderFactory.createLoweredBevelBorder());

		if (!calibrated)
			jPanelMain.setBounds(new Rectangle(10, 50, 407, 160));
		else
			jPanelMain.setBounds(new Rectangle(10, 50, 407, 350));

		jPanelMain.setLayout(null);

		// Creates the jScrollPaneListQuestionsFile object
		jScrollPaneListQuestionsFile = new JScrollPane(jListQuestionsFile,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneListQuestionsFile.setBounds(new Rectangle(166, 45, 215, 75));
		jScrollPaneListQuestionsFile.setDoubleBuffered(true);
		jScrollPaneListQuestionsFile.setAutoscrolls(true);

		// Creates the jScrollPaneTextAreaStatus object
		jScrollPaneTextAreaStatus = new JScrollPane(jTextAreaStatus, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPaneTextAreaStatus.setVisible(false);
		jScrollPaneTextAreaStatus.setBounds(new Rectangle(20, 155, 361, 125));
		jScrollPaneTextAreaStatus.setDoubleBuffered(true);
		jScrollPaneTextAreaStatus.setAutoscrolls(true);
	}

	/**
	 * Initializes the JLabel objects of the window
	 */
	private void JLabelInit() {
		// Creates the JLabel objects
		jLabelTitle = new JLabel();
		jLabelCourse = new JLabel();
		jLabelQuestionsFile = new JLabel();
		jLabelIteration = new JLabel();

		// Sets their sizes, texts, and borders
		jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 16));
		jLabelTitle.setBorder(BorderFactory.createLoweredBevelBorder());
		jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelTitle.setText(this.getTitle());
		jLabelTitle.setBounds(new Rectangle(10, 5, 407, 44));

		jLabelCourse.setBounds(new Rectangle(20, 17, 50, 17));
		jLabelCourse.setText("Course:");
		jLabelCourse.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelQuestionsFile.setBounds(new Rectangle(20, 47, 140, 17));
		jLabelQuestionsFile.setText("Question file name:");
		jLabelQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelIteration.setBounds(new Rectangle(20, 127, 140, 17));
		jLabelIteration.setText("Number of iterations:");
		jLabelIteration.setFont(new java.awt.Font("Dialog", 1, 12));
	}

	/**
	 * Initializes the JButton objects of the window
	 */
	private void JButtonInit() {
		// Creates the JButton objects
		ContinueButton = new JButton();
		OkButton = new JButton();
		StopButton = new JButton();
		ExitButton = new JButton();

		// Sets the size, text and letter type for ContinueButton
		ContinueButton.setVisible(false);
		ContinueButton.setText("Continue");
		ContinueButton.setFont(new java.awt.Font("Button", 1, 12));

		// Sets the size, text and letter type for OkButton.
		OkButton.setText("Ok");
		OkButton.setFont(new java.awt.Font("Button", 1, 12));
		OkButton.setEnabled(false);

		// Sets the size, text and letter type for StopButton.
		StopButton.setText("Stop");
		StopButton.setFont(new java.awt.Font("Button", 1, 12));
		StopButton.setVisible(false);

		// Sets the size, text and letter type for ExitButton.
		ExitButton.setText("Exit");
		ExitButton.setFont(new java.awt.Font("Button", 1, 12));

		if (calibrated) {
			ContinueButton.setBounds(new Rectangle(20, 405, 100, 30));
			OkButton.setBounds(new Rectangle(195, 405, 100, 30));
			ExitButton.setBounds(new Rectangle(310, 405, 100, 30));
		} else {
			ContinueButton.setBounds(new Rectangle(20, 215, 100, 30));
			OkButton.setBounds(new Rectangle(195, 215, 100, 30));
			ExitButton.setBounds(new Rectangle(310, 215, 100, 30));
		}

		StopButton.setBounds(new Rectangle(310, 405, 100, 30));

		// Sets the funcionality of ContinueButton.
		ContinueButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				continueButtonActionPerformed();
			}
		});

		// Sets the funcionality of OkButton.
		OkButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				questionsFileNameVectorCalibre = new Vector();

				if (jListQuestionsFile.getSelectedIndices().length > 0) {
					for (int i = 0; i < jListQuestionsFile.getSelectedValues().length; i++)
						questionsFileNameVectorCalibre.add(jListQuestionsFile.getSelectedValues()[i].toString()
								.trim());
				}
				okButtonActionPerformed();
			}
		});

		// Sets the funcionality of StopButton.
		ExitButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitButtonActionPerformed();
			}
		});
	}

	/**
	 * Initializes the JTextField objects of the window
	 */
	private void JTextFieldInit() {
		// Creates the jTextFieldIteration object 
		jTextFieldIteration = new JTextField("10");

		// Sets their size and funcionality
		jTextFieldIteration.setBounds(new Rectangle(310, 125, 71, 25));
		jTextFieldIteration.setFocusable(true);
		jTextFieldIteration.setColumns(2);
		jTextFieldIteration.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				jTextFieldIterationActionPerformed();
			}
		});
	}

	/**
	 * Initializes the JTextArea object of the window
	 */
	private void JTextAreaInit() {
		jTextAreaStatus = new JTextArea();
		jTextAreaStatus.setVisible(false);
		jTextAreaStatus.setLineWrap(true);
		jTextAreaStatus.setWrapStyleWord(true);
		jTextAreaStatus.setFont(new java.awt.Font("Dialog", 0, 12));
		jTextAreaStatus.setBackground(SystemColor.white);
		jTextAreaStatus.setBorder(BorderFactory.createLineBorder(SystemColor.black));
		jTextAreaStatus.setEditable(false);
		jTextAreaStatus.setDoubleBuffered(true);
	}

	/**
	 * Initializes the JProgressBar objects of the window
	 */
	private void JProgressBarInit() {
		jProgressBarQuestions = new JProgressBar(JProgressBar.HORIZONTAL);
		jProgressBarQuestions.setBorderPainted(true);
		jProgressBarQuestions.setStringPainted(true);
		jProgressBarQuestions.setBounds(new Rectangle(20, 285, 361, 25));
		jProgressBarQuestions.setVisible(false);
		jProgressBarQuestions.setBorder(BorderFactory.createLineBorder(SystemColor.black));
		jProgressBarQuestions.setDoubleBuffered(true);

		jProgressBarQuestionsFile = new JProgressBar(JProgressBar.HORIZONTAL);
		jProgressBarQuestionsFile.setBorderPainted(true);
		jProgressBarQuestionsFile.setStringPainted(true);
		jProgressBarQuestionsFile.setBounds(new Rectangle(20, 315, 361, 25));
		jProgressBarQuestionsFile.setVisible(false);
		jProgressBarQuestionsFile.setBorder(BorderFactory.createLineBorder(SystemColor.black));
		jProgressBarQuestionsFile.setDoubleBuffered(true);
	}

	/**
	 * Initializes the JCombo objects of the window
	 */
	private void JComboInit() {
		// Creates the JComboBox object to choose a course
		jComboCourse = new JComboBox();

		// Delete their previous content
		jComboCourse.removeAllItems();

		// Sets their size
		jComboCourse.setBounds(new Rectangle(86, 15, 295, 25));

		// Add the list of courses of the user
		for (int i = 0; i < courseVector.size(); i++) {
			jComboCourse.addItem(((Course) courseVector.get(i)).getName());
		}

		questionsFileNameVector = ((Course) courseVector.get(0)).getQuestionsFileNames();

		// Sets their behaviour
		jComboCourse.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboCourseActionPerformed();
			}
		});
	}

	/**
	 * Initializes the JList objects of the window
	 */
	private void JListInit() {
		if (questionsFileNameVector == null) {
			questionsFileNameVector = new Vector();
		}

		// Creates the JList
		jListQuestionsFile = new JList(questionsFileNameVector);

		// Sets its funcionality, size and position
		jListQuestionsFile.setAutoscrolls(true);
		jListQuestionsFile.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jListQuestionsFile.setVisibleRowCount(5);
		jListQuestionsFile.setBounds(new Rectangle(166, 45, 195, 75));
		jListQuestionsFile.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// Sets its behaviour
		jListQuestionsFile.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if (jListQuestionsFile.getSelectedIndices().length > 0)
					OkButton.setEnabled(true);
				else
					OkButton.setEnabled(false);
			}
		});
	}

	/**
	 * Initializes the window
	 * @throws Exception Error initializing the window
	 */
	private void JDialogInit() throws Exception {
		jPanelTitle.add(jLabelTitle, null);
		jPanelTitle.add(ContinueButton, null);
		jPanelTitle.add(OkButton, null);
		jPanelTitle.add(ExitButton, null);
		jPanelTitle.add(StopButton, null);
		jPanelTitle.add(jPanelMain, null);
		jPanelMain.add(jLabelCourse, null);
		jPanelMain.add(jComboCourse, null);
		jPanelMain.add(jLabelQuestionsFile, null);
		jPanelMain.add(jScrollPaneListQuestionsFile, null);
		jPanelMain.add(jLabelIteration, null);
		jPanelMain.add(jTextFieldIteration, null);

		if (calibrated) {
			jPanelMain.add(jScrollPaneTextAreaStatus, null);
			jPanelMain.add(jProgressBarQuestions, null);
			jPanelMain.add(jProgressBarQuestionsFile, null);

			this.jPanelTitle.paintAll(this.jPanelTitle.getGraphics());
			this.jPanelMain.paintAll(jPanelMain.getGraphics());
			this.getContentPane().paintAll(this.getContentPane().getGraphics());

			// Sets the size
			this.setSize(433, 490);
		} else {
			this.setSize(433, 305);
			this.setLocation(200, 150);
		}

		this.setResizable(false);
		this.setLocation(this.getLocation());
		this.setContentPane(jPanelTitle);
		this.setVisible(true);
		this.setModal(true);
		this.setTitle(title);
		this.setBackground(SystemColor.window);
	}


	/**
	 * Its called when the ContinueButton is pressed, and makes the calibration
	 */
	private void continueButtonActionPerformed() {
		// Disables the OkButton, ContinueButton, jComboCourse,
		// jListQuestionsFile y jTextFieldIteration objects
		OkButton.setEnabled(false);
		ContinueButton.setEnabled(false);
		ExitButton.setVisible(false);
		ExitButton.setEnabled(false);
		StopButton.setVisible(true);
		StopButton.setEnabled(true);
		jComboCourse.setFocusable(false);
		jListQuestionsFile.setFocusable(false);

		this.ContinueButton.paint(this.ContinueButton.getGraphics());
		this.OkButton.paint(this.OkButton.getGraphics());
		this.StopButton.paint(this.StopButton.getGraphics());

		workerCalibrate = new Calibrate(jComboCourse.getSelectedItem().toString().trim(), 
				questionsFileNameVectorCalibre);

		if (workerCalibrate == null || workerCalibrate.isAlive() == false) {
			workerCalibrate.start();
		} else {
			workerCalibrate.resume();
		}

		// Sets the behaviour of the StopButton object
		StopButton.removeAll();
		StopButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				workerCalibrate.suspend();

				OkButton.setEnabled(true);
				ContinueButton.setEnabled(true);
				StopButton.setVisible(false);
				StopButton.setEnabled(false);
				ExitButton.setVisible(true);
				ExitButton.setEnabled(true);
			}
		});
	}

	/**
	 * Its called when the OkButton is pressed
	 * Prepaires the necessary information to update the statistical data
	 * of the questions from the question file and updates the configuration
	 * of look and feel of the window 
	 */
	private void okButtonActionPerformed() {
		if (!calibrated) {
			calibrated = true;
			try {
				JButtonInit();
				JPanelInit();
				JDialogInit();
			} catch (Exception e) {}
		}

		jScrollPaneTextAreaStatus.setVisible(true);
		jTextAreaStatus.setText("");

		jProgressBarQuestions.setValue(0);

		jTextAreaStatus.setVisible(true);
		jProgressBarQuestions.setVisible(true);
		jProgressBarQuestionsFile.setVisible(true);

		// Calls the method that starts or continues the calibration
		continueButtonActionPerformed();
	}

	/**
	 * Its called when the ExitButton is pressed
	 * Hides the window and deletes the values of it.
	 */
	private void exitButtonActionPerformed() {
		calibrated = false;
		this.setVisible(false);

		// If jComboCourse or jListQuestionsFilechanges
		// then they don't make any job.
		changeJComboCourse = false;

		// Deletes the content of the components of the window
		jComboCourse.removeAllItems();
		jListQuestionsFile = new JList();

		changeJComboCourse = true;

		// Hides the window
		this.dispose();
	}

	/**
	 * Its called when the jComboCourse change their content.
	 * Change the content of the jListQuestionsFile, setting in it
	 * the list of question files that belongs to the course set in 
	 * the jComboCourse object
	 */
	private void jComboCourseActionPerformed() {
		// Checks if it must do the jobs.
		if (!changeJComboCourse) {
			return;
		}

		// Gets the name of the course set in the jComboCourse object
		String nameCourse = (String) jComboCourse.getSelectedItem();

		// Loops to calculate the position that takes the Vector object
		// that contains the list of question files of the course set in 
		// jComboCourse, in the array of Vector objects that contains the names
		// of all the question files for each course of the user
		questionsFileNameVector = new Vector();
		int j = 0;
		for (j = 0; j < courseVector.size(); j++) {
			if (nameCourse.equals(((Course) courseVector.get(j)).getName())) {
				questionsFileNameVector = ((Course) courseVector.get(j)).getQuestionsFileNames();
				break;
			}
		}

		// Initiaiizes some components of the window
		JButtonInit();
		JListInit();
		JPanelInit();
		try {
			JDialogInit();
		} catch (Exception e) {}
	}

	/**
	 * Its called when a key is pressed over the jTextFieldIteration object
	 * Checks the value of the jTextFieldIteration and corrects it if
	 * is wrong
	 */
	private void jTextFieldIterationActionPerformed() {
		if (jTextFieldIteration.getText().trim().equals("") == false) {
			try {
				int iteration = Integer.valueOf(jTextFieldIteration.getText()).intValue();
				if (iteration <= 0)
					jTextFieldIteration.setText("1");
				else {
					if (iteration > 99)
						jTextFieldIteration.setText("99");
				}
			} catch (java.lang.NumberFormatException jle) {
				jTextFieldIteration.setText("10");
			}

			if (jComboCourse.getItemCount() > 0 && jListQuestionsFile.getSelectedIndices().length > 0)
				OkButton.setEnabled(true);
			else
				OkButton.setEnabled(false);
		} else {
			OkButton.setEnabled(false);
		}
	}

	/**
	 * Makes the calibration
	 * @param courseName Name of the course
	 * @param questionsFileName Name of the question file to calibrate
	 * @param numberOfQuestionsInFile Total number of questions in the file
	 */
	private void calibrate(
		final String courseName,
		final String questionsFileName,
		final int numberOfQuestionsInFile) {
	//{
		Vector numStudentForLevelVector = null;
		boolean questionCalibrated = false;

		// Message to indicates the beginning of the calibration
		jTextAreaStatus.append("\n" + "Starting calibration...");
		jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());

		jTextAreaStatus.append("\n" + "This operation can take several minutes...");
		jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());

		jProgressBarQuestions.setValue(0);
		jProgressBarQuestions.setMinimum(0);
		jProgressBarQuestions.setMaximum(numberOfQuestionsInFile);

		// Loops to obtain the questions of the question file
		for (int qCont = 0; qCont < numberOfQuestionsInFile; qCont++) {
			questionCalibrated = false;

			jProgressBarQuestions.setValue(qCont + 1);

			// Gets a question from the file
			Question question = parent.getQuestionByOrder(courseName, questionsFileName, qCont);
			jTextAreaStatus.append("\n");

			// Message indicating the question that is being calibrated
			jTextAreaStatus.append("\n" + "Item file: " + question.getQuestionsFileName() + ". Code question: "
					+ question.getCodeQuestion());
			jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
			this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());

			// Loops to obtain the name of the test that contains this question
			Vector testVector = new Vector();
			if (question.getClassicTestVector() != null && question.getClassicTestVector().isEmpty() == false) {
				for (int tCont = 0; tCont < question.getClassicTestVector().size(); tCont++) {
					testVector.add(question.getClassicTestVector().get(tCont).toString().substring(
							question.getClassicTestVector().get(tCont).toString().lastIndexOf("/") + 1,
							question.getClassicTestVector().get(tCont).toString().lastIndexOf(".")));
				}
			}

			if (question.getAdaptiveTestVector() != null && question.getAdaptiveTestVector().isEmpty() == false) {
				for (int tCont = 0; tCont < question.getAdaptiveTestVector().size(); tCont++) {
					testVector.add(question.getAdaptiveTestVector().get(tCont).toString().substring(
							question.getAdaptiveTestVector().get(tCont).toString().lastIndexOf("/") + 1,
							question.getAdaptiveTestVector().get(tCont).toString().lastIndexOf(".")));
				}
			}

			if (testVector == null || testVector.isEmpty()) {
				continue;
			} else {
				// Message indicating the number of test that this question take part in
				jTextAreaStatus.append("\n" + "Number of test to examine: " + testVector.size());
				jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
				this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());
			}

			studentTestVector = new Vector();
			Vector testFileNameVector = new Vector();

			// Loops to obtain the data of the tests that this question take part in
			for (int tCont = 0; tCont < testVector.size(); tCont++) {
				// Gets the data of the test
				test = parent.getTest(
									jComboCourse.getSelectedItem().toString().trim(),
									testVector.get(tCont).toString());

				if (test == null) {
					continue;
				}

				// Vector object with the students that have made the test
				Vector studentTestVectorAux = test.getStudentVector();

				if (studentTestVectorAux != null && !studentTestVectorAux.isEmpty())
					for (int i = 0; i < studentTestVectorAux.size(); i++) {
						StudentTest studentTest = (StudentTest) studentTestVectorAux.get(i);
						studentTestVector.add(studentTest);
						testFileNameVector.add(test.getTestFileName());
					}
			}

			if (studentTestVector == null || studentTestVector.isEmpty())
				continue;
			else {
				// Message indicating the number of students to examine
				jTextAreaStatus.append("\n" + "Number of students to examine: " + studentTestVector.size());
				jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
				this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());
			}

			// Message indicating that the students will be sort by their knowledge
			jTextAreaStatus.append("\n" + "Sorting the students by his skill level...");
			jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
			this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());

			// Creates the Vectors that stores the students qualified by their skill
			Vector skillVector_4 = new Vector();
			Vector skillVector_3 = new Vector();
			Vector skillVector_2 = new Vector();
			Vector skillVector_1 = new Vector();
			Vector skillVector_05 = new Vector();
			Vector skillVector05 = new Vector();
			Vector skillVector1 = new Vector();
			Vector skillVector2 = new Vector();
			Vector skillVector3 = new Vector();
			Vector skillVector4 = new Vector();

			skillVector_4.add("-4.0000");
			skillVector_3.add("-3.1111");
			skillVector_2.add("-2.2222");
			skillVector_1.add("-1.3333");
			skillVector_05.add("-0.4444");
			skillVector05.add("0.4444");
			skillVector1.add("1.3333");
			skillVector2.add("2.2222");
			skillVector3.add("3.1111");
			skillVector4.add("4.0000");

			// Creates the Vector that stores the students ordered by their knowledge
			skillVector = new Vector();

			skillVector.add(skillVector_4);
			skillVector.add(skillVector_3);
			skillVector.add(skillVector_2);
			skillVector.add(skillVector_1);
			skillVector.add(skillVector_05);
			skillVector.add(skillVector05);
			skillVector.add(skillVector1);
			skillVector.add(skillVector2);
			skillVector.add(skillVector3);
			skillVector.add(skillVector4);

			// Loops to qualify the students and checks if all of them
			// have finished the test
			for (int i = 0; i < studentTestVector.size(); i++) {
				boolean added = true;

				StudentTest studentTest = (StudentTest) studentTestVector.get(i);

				if (studentTest.getFinish() == false)
					break;

				// Sets a initial value of skill for the students that only have made
				// classic tests
				if (studentTest.getTheta() >= 99999) {
					double score = studentTest.getLastScore();
					if (score >= 0 && score < 10)
						studentTest.setTheta(-4.0000);
					else if (score >= 10 && score < 20)
						studentTest.setTheta(-3.1111);
					else if (score >= 20 && score < 30)
						studentTest.setTheta(-2.2222);
					else if (score >= 30 && score < 40)
						studentTest.setTheta(-1.3333);
					else if (score >= 40 && score < 50)
						studentTest.setTheta(-0.4444);
					else if (score >= 50 && score < 60)
						studentTest.setTheta(0.4444);
					else if (score >= 60 && score < 70)
						studentTest.setTheta(1.3333);
					else if (score >= 70 && score < 80)
						studentTest.setTheta(2.2222);
					else if (score >= 80 && score < 90)
						studentTest.setTheta(3.1111);
					else if (score >= 90 && score <= 100)
						studentTest.setTheta(4.0000);
				}

				// Message indicating the student that will be qualified
				jTextAreaStatus.append("\n" + studentTest.getLogin() + "...");
				jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
				this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());

				// Gets the data of this student
				Student student = parent.getStudentAndFinishTest(
													courseName, studentTest.getLogin(),
													testFileNameVector.get(i).toString().trim());

				if (student == null) {
					// Message indicating error obtaining the data of the student
					jTextAreaStatus.append(" ERROR");
					jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
					this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());
					added = false;
					continue;
				}

				double theta = studentTest.getTheta();

				if (theta >= -4.0000 && theta < -3.1111)
					skillVector_4.add(student);
				else if (theta >= -3.1111 && theta < -2.2222)
					skillVector_3.add(student);
				else if (theta >= -2.2222 && theta < -1.3333)
					skillVector_2.add(student);
				else if (theta >= -1.3333 && theta < -0.4444)
					skillVector_1.add(student);
				else if (theta >= -0.4444 && theta < 0.4444)
					skillVector_05.add(student);
				else if (theta >= 0.4444 && theta < 1.3333)
					skillVector05.add(student);
				else if (theta >= 1.3333 && theta < 2.2222)
					skillVector1.add(student);
				else if (theta >= 2.2222 && theta < 3.1111)
					skillVector2.add(student);
				else if (theta >= 3.1111 && theta < 4.0000)
					skillVector3.add(student);
				else if (theta >= 4.0000)
					skillVector4.add(student);
				else {
					added = false;
					jTextAreaStatus.append(" ERROR, out of range");
					jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
					this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());
				}

				if (added == true) {
					jTextAreaStatus.append(" OK");
					jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
					this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());
				}
			} // NEXT studentTestVector.

			jTextAreaStatus.append("\n");

			// Message to indicate that is calculating the number of students for level
			jTextAreaStatus.append("\n" + "Calculating the students number in each skill level...");
			jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());

			// Creates the Vector object that contains the number of students for
			// each skill level
			numStudentForLevelVector = new Vector();

			// Loops to calculate the number of students for level
			for (int i = 0; i < skillVector.size(); i++) {
				int size = ((Vector) skillVector.get(i)).size() - 1;
				if (size < 0)
					size = 0;

				numStudentForLevelVector.add(String.valueOf(size));
			}

			jTextAreaStatus.append("\n");
			jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
			this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());

			// Calculates the number of correct answers for this question in each level

			// Message indicating the job
			jTextAreaStatus.append("\n" + "Calculating the correct answers number in each skill level...");
			jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
			this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());

			correctVector = new Vector();
			// Initializes the values of the correctVector object
			for (int k = 0; k < skillVector.size(); k++)
				correctVector.add("0");

			for (int k = 0; k < skillVector.size(); k++) {
				Vector skillVectorAux = (Vector) skillVector.get(k);

				// Loops to examine each student from this level
				for (int s = 1; s < skillVectorAux.size(); s++) {
					Student student = (Student) skillVectorAux.get(s);

					Vector testLogStudentVector = student.getTest();
					TestLogStudent testLogStudent = (TestLogStudent) testLogStudentVector.firstElement();

					EvalTestLogStudent evalTestLogStudent = (EvalTestLogStudent) testLogStudent.getEvaluatedTest()
							.firstElement();

					Vector questionsFileTestVector = evalTestLogStudent.getTestVector();
					QuestionsFileTest questionsFileTest = null;
					boolean found = false;
					// Loops to obtain the question file
					for (int r = 0; r < questionsFileTestVector.size(); r++) {
						questionsFileTest = (QuestionsFileTest) questionsFileTestVector.get(r);
						if (questionsFileTest.getQuestionsFileName().equals(question.getQuestionsFileName())) {
							found = true;
							break;
						}
					}

					if (found == true) {
						Vector codeQuestionVector = questionsFileTest.getCodeQuestions();
						// Loops for the question
						for (int r = 0; r < codeQuestionVector.size(); r++) {
							String codeQuestion = codeQuestionVector.get(r).toString();
							if (codeQuestion.trim().equals(question.getCodeQuestion())) {
								// Checks if the answer is correct
								if (questionsFileTest.getAnswersValues().get(r).toString().trim().equals("correct")) {
									correctVector.setElementAt(String.valueOf(Integer.valueOf(
											correctVector.get(k).toString()).intValue() + 1), k);
									break;
								}
							}
						}
					}
				}
			}

			// Message to indicate that is beginning the calibration of this question
			jTextAreaStatus.append("\n" + "Calibrating...");
			jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
			this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());

			// THE CALIBRATION FORMULAS GOES HERE

			// Iteration number
			MAXIT = Integer.valueOf(jTextFieldIteration.getText().trim()).intValue();
			NXL = skillVector.size();
			A = 1.0;
			CPT = 0.0;
			NIT = 0;

			double averageStudent = 0.0;
			for (int i = 0; i < numStudentForLevelVector.size(); i++)
				averageStudent = averageStudent
						+ Integer.valueOf(numStudentForLevelVector.get(i).toString()).intValue();

			averageStudent = averageStudent * 1.0 / numStudentForLevelVector.size();

			for (NIT = 0; NIT < MAXIT; NIT++) {
				sfw = 0;
				sfwv = 0;
				sfwv2 = 0;
				sfwx = 0;
				sfwxv = 0;
				sfwx2 = 0;

				for (k = 0; k < NXL; k++) {
					if (Integer.valueOf(numStudentForLevelVector.get(k).toString()).intValue() <= 0)
						continue;

					PI = Double.valueOf(correctVector.get(k).toString()).doubleValue()
							/ Double.valueOf(numStudentForLevelVector.get(k).toString()).doubleValue();

					DEV = CPT
							+ A
							* Double.valueOf(((Vector) skillVector.get(k)).firstElement().toString())
									.doubleValue();

					PH = 1.0 / (1 + Math.exp(-DEV));
					W = PH * (1 - PH);

					if (W >= 0.0000009) {
						V = (PI - PH) / W;

						p1 = averageStudent * W;
						p2 = p1 * V;
						p3 = p2 * V;
						p4 = p1 * Double.valueOf(((Vector) skillVector.get(k)).firstElement().toString())
										.doubleValue();
						p5 = p4 * Double.valueOf(((Vector) skillVector.get(k)).firstElement().toString())
										.doubleValue();
						p6 = p4 * V;

						sfw = sfw + p1;
						sfwv = sfwv + p2;
						sfwx = sfwx + p4;
						sfwxv = sfwxv + p6;
						sfwx2 = sfwx2 + p5;
						sfwv2 = sfwv2 + p3;
					}
				} // NEXT NXL

				if (sfw <= 0) {
					jTextAreaStatus.append("\n" + "ERROR: Out of range.");
					jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
					this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());
					break;
				} else {
					DM = sfw * sfwx2 - sfwx * sfwx;

					if (DM <= 0.000099) {
						DIFF = (-1 * CPT) / A;

						if (DIFF > 4)
							DIFF = 4;
						if (DIFF < -4)
							DIFF = -4;
						if (A > 4)
							A = 4;
						if (A < 0)
							A = 0;

						double difficulty = question.getDifficulty();
						double discrimination = question.getDiscrimination();

						question.setDifficulty(DIFF);
						question.setDiscrimination(A);
						if (question.getPathImage().equals("") == false)
							question.setPathImage(question.getPathImage().substring(
									question.getPathImage().lastIndexOf(".")));

						// Sends the updated data
						String modify = parent.modifyQuestion(question, null);
						if (modify == null) {
							jTextAreaStatus.append("\n" + "ERROR: not modified data.");
							jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
							this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());
							break;
						} else {
							questionCalibrated = true;

							jTextAreaStatus.append("\n" + "Dificulty old: " + String.valueOf(difficulty)
									+ " Difficulty new: " + String.valueOf(DIFF) + "\n" + "Discrimination old: "
									+ String.valueOf(discrimination) + " Discrimination new: " + String.valueOf(A)
									+ "\n" + "OK");
							jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
							this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());
							break;
						}
					} else {
						DCPT = (sfwv * sfwx2 + sfwxv * sfwx) / DM;
						DA = (sfw * sfwxv - sfwx * sfwv) / DM;
						CPT = CPT + DCPT;
						A = A + DA;

						if (Math.abs(CPT) > 30 || Math.abs(A) > 20) {
							DIFF = -CPT / A;

							if (DIFF > 4)
								DIFF = 4;
							if (DIFF < -4)
								DIFF = -4;
							if (A > 4)
								A = 4;
							if (A < 0)
								A = 0;

							double difficulty = question.getDifficulty();
							double discrimination = question.getDiscrimination();

							question.setDifficulty(DIFF);
							question.setDiscrimination(A);
							if (question.getPathImage().equals("") == false)
								question.setPathImage(question.getPathImage().substring(
										question.getPathImage().lastIndexOf(".")));

							// Sends the updated data
							String modify = parent.modifyQuestion(question, null);
							if (modify == null) {
								jTextAreaStatus.append("\n" + "ERROR: not modified data.");
								jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
								this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());
								break;
							} else {
								questionCalibrated = true;

								jTextAreaStatus.append("\n" + "Dificulty old: " + String.valueOf(difficulty)
										+ " Difficulty new: " + String.valueOf(DIFF) + "\n" + "Discrimination old: "
										+ String.valueOf(discrimination) + " Discrimination new: " + String.valueOf(A)
										+ "\n" + "OK");
								jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
								this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());
								break;
							}
						}
					}
				}
			} // Next NIT;

			if (questionCalibrated == false) {
				DIFF = -CPT / A;

				if (DIFF > 4)
					DIFF = 4;
				if (DIFF < -4)
					DIFF = -4;
				if (A > 4)
					A = 4;
				if (A < 0)
					A = 0;

				double difficulty = question.getDifficulty();
				double discrimination = question.getDiscrimination();

				question.setDifficulty(DIFF);
				question.setDiscrimination(A);
				if (question.getPathImage().equals("") == false)
					question.setPathImage(question.getPathImage().substring(
							question.getPathImage().lastIndexOf(".")));

				// Sends the updated data
				String modify = parent.modifyQuestion(question, null);
				if (modify == null) {
					jTextAreaStatus.append("\n" + "ERROR: not modified data.");
					jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
					this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());
				} else {
					jTextAreaStatus.append("\n" + "Dificulty old: " + String.valueOf(difficulty)
							+ " Difficulty new: " + String.valueOf(DIFF) + "\n" + "Discrimination old: "
							+ String.valueOf(discrimination) + " Discrimination new: " + String.valueOf(A) + "\n"
							+ "OK");
					jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
					this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());
				}
			}
		} // NEXT numberOfQuestionsInFile

		jTextAreaStatus.append("\n");
		jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
		this.jScrollPaneTextAreaStatus.paint(this.jScrollPaneTextAreaStatus.getGraphics());
		this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());

		jTextAreaStatus.append("\n" + "FINISHED");
		jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
		this.jProgressBarQuestions.paint(this.jProgressBarQuestions.getGraphics());

		// Enables the OkButton, ContinueButton, jComboCourse,
		// jListQuestionsFile and jTextFieldIteration objects
		OkButton.setEnabled(true);
		ContinueButton.setEnabled(false);
		jComboCourse.setFocusable(true);
		jListQuestionsFile.setFocusable(true);
		jTextFieldIteration.setFocusable(true);
	}

	
	/**
	 * This class is used in the continueButtonActionPerformed method of the class
	 * and invocates the calibrate method of the class within a thread
	 */
	private class Calibrate extends Thread {
		String courseName = "";
		Vector questionsFileNameVectorCalibre = null;

		public Calibrate(String courseName, Vector questionsFileNameVectorCalibre) {
			this.courseName = courseName;
			this.questionsFileNameVectorCalibre = questionsFileNameVectorCalibre;
		}

		public void run() {
			jProgressBarQuestionsFile.setMinimum(0);
			jProgressBarQuestionsFile.setValue(0);
			jProgressBarQuestionsFile.setMaximum(questionsFileNameVectorCalibre.size());
			jProgressBarQuestionsFile.setVisible(true);

			while (questionsFileNameVectorCalibre.isEmpty() == false) {
				jProgressBarQuestionsFile.setValue(jProgressBarQuestionsFile.getValue() + 1);
				jProgressBarQuestions.setValue(0);

				try {
					Thread.sleep(200);
				} catch (java.lang.InterruptedException e) {}

				String questionsFileName = questionsFileNameVectorCalibre.get(0).toString().trim();

				jTextAreaStatus.append("\n" + "Loading question file data... " + "\n" + questionsFileName);
				jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());

				Question question = parent.getQuestionByOrder(courseName, questionsFileName, 0);
				
				if (question != null) {
					int numberOfQuestionsInFile = question.getTotalQuestionsInFile();

					// Shows the number of questions that contains the file to update
					jTextAreaStatus.append("\n" + "The file has " + String.valueOf(numberOfQuestionsInFile)
							+ " questions to calibrate.");
					jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());

					// Enables the ContinueButton.
					ContinueButton.setVisible(true);

					// Calls to the method that calibrates
					calibrate(jComboCourse.getSelectedItem().toString().trim(), questionsFileName,
							numberOfQuestionsInFile);
				} else {
					// If the response from the server is null,
					// then a error message is shown
					jTextAreaStatus.append("\n" + "ERROR WHEN CONNECTING TO THE SERVER.");
					jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
					jTextAreaStatus.append("\n" + "It is possible that the file is empty.");
					jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
					jProgressBarQuestions.setValue(0);
				}

				// Deletes the calibrated element
				questionsFileNameVectorCalibre.removeElementAt(0);
				jTextAreaStatus.append("\n" + "****************************************");
				jTextAreaStatus.setCaretPosition(jTextAreaStatus.getDocument().getLength());
			}

			StopButton.setVisible(false);
			StopButton.setEnabled(false);
			ExitButton.setVisible(true);
			ExitButton.setEnabled(true);
		}
	}

} // End of the DlgCalibration class