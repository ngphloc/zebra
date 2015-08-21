package es.uco.WOW.TestEditor;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import es.uco.WOW.Utils.Course;
import es.uco.WOW.Utils.Question;

/**
 * <p>Title: Wow! TestEditor</p> <p>Description: Herramienta para la edicion
 * de preguntas tipo test adaptativas </p> <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/*
 * NOMBRE: FrmFindQuestions. FUNCION: Clase que extiende de frame. Esta
 * clase presenta al usuario los componentes necesarios para que est� introduzca
 * los par�metros que desee para realizar la busqueda de preguntas dentro de un
 * fichero de preguntas. Entres est�s par�metros se encuentran, el nombre del
 * curso y del fichero de preguntas, en el que desea buscar, una expresi�n de
 * texto para buscar en el enunciado de las preguntas, la dificultad de la
 * pregunta, la discriminaci�n de la pregunta, si la pregunta tiene imagen o no,
 * etc. Solamente uno de los par�metros es obligatorio, cualquiera de ellos,
 * pero solamente uno como m�nimo. LAST MODIFICATION: 06-02-2008
 */

public class FrmFindQuestions extends TestEditorInternalFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Vector courseNameVector = new Vector(); // Almacen la lista de

	// nombres de los cursos
	// de los que
	// el autor es due�o.
	private Vector courseVector; // Almacena la lista con los

	// con los nombres de los
	// ficheros de preguntas para
	// todos los cursos que son
	// propiedad del usuario.
	private Vector questionsOfMultipleFilesVector = null;

	private Vector questionsFileVector;

	private String questionsFileName; // Almacena el nombre del fichero de preguntas
										// sobre

	// el que se est� actuando.
	private String userLogin; // Almacena el login del usuario.

	private String userPassword; // Almacena el password del usuario.

	private URL codeBase; // Almacena la URL del servidor del que se ha descargado el

	// el applet.

	private boolean ImageIconLoad = false; // Indica si la carga de los iconos de los

	// JButton de este frame se ha
	// producido con �xito.
	// Variables que almacenas las posibles dificultades y discriminaciones que
	// se
	// dan a elegir para una pregunta.
	private double DIFFICULTY_MAX;

	private double DIFFICULTY_NORMAL;

	private double DIFFICULTY_MIN;

	private double DISCRIMINATION_MAX;

	private double DISCRIMINATION_MIN;

	private double DISCRIMINATION_NORMAL;

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

	// Componentes del frame.
	private JTextArea jTextAreaQuestion;

	private JRadioButton jRadioButtonImageYes;

	private JRadioButton jRadioButtonImageNo;

	private JRadioButton jRadioButtonImageIndifferent;

	private JRadioButton jRadioButtonClassicTestYes;

	private JRadioButton jRadioButtonClassicTestNo;

	private JRadioButton jRadioButtonClassicTestIndifferent;

	private JRadioButton jRadioButtonAdaptiveTestYes;

	private JRadioButton jRadioButtonAdaptiveTestNo;

	private JRadioButton jRadioButtonAdaptiveTestIndifferent;

	private ButtonGroup jRadioButtonGroupImage;

	private ButtonGroup jRadioButtonGroupClassicTest;

	private ButtonGroup jRadioButtonGroupAdaptiveTest;

	private JScrollPane jScrollPane;

	private JScrollPane jScrollPaneJListQuestionsFile;

	private JScrollPane jScrollPaneQuestion;

	private JPanel jPanelContent;

	private JPanel jPanelInstructions;

	private JPanel jPanelCourse;

	private JPanel jPanelQuestionsFile;

	private JPanel jPanelCourseQuestionsFile;

	private JPanel jPanelCodeQuestion;

	private JPanel jPanelQuestion;

	private JPanel jPanelRadioButtonImage;

	private JPanel jPanelDifficulty;

	private JPanel jPanelDifficultyMinMax;

	private JPanel jPanelDiscrimination;

	private JPanel jPanelDiscriminationMinMax;

	private JPanel jPanelGuessing;

	private JPanel jPanelGuessingMinMax;

	private JPanel jPanelExhibitionRate;

	private JPanel jPanelExhibitionRateMinMax;

	private JPanel jPanelTimeAnswer;

	private JPanel jPanelTimeAnswerMinMax;

	private JPanel jPanelSuccessRate;

	private JPanel jPanelSuccessRateMinMax;

	private JPanel jPanelIrtParameters;

	private JPanel jPanelStatisticParameters;

	private JPanel jPanelNumberOfAnswers;

	private JPanel jPanelTest;

	private JPanel jPanelClassicTest;

	private JPanel jPanelRadioButtonClassicTest;

	private JPanel jPanelAdaptiveTest;

	private JPanel jPanelRadioButtonAdaptiveTest;

	private JPanel jPanelButton;

	private JPanel jPanelStatus;

	private JTabbedPane jTabbedPane;

	private JLabel jLabelInstructions;

	private JLabel jLabelCourse;

	private JLabel jLabelQuestionsFile;

	private JLabel jLabelCodeQuestion;

	private JLabel jLabelDifficulty;

	private JLabel jLabelDifficultyMin;

	private JLabel jLabelDifficultyMax;

	private JLabel jLabelDifficultyInfo;

	private JLabel jLabelDiscrimination;

	private JLabel jLabelDiscriminationMin;

	private JLabel jLabelDiscriminationMax;

	private JLabel jLabelDiscriminationInfo;

	private JLabel jLabelGuessing;

	private JLabel jLabelGuessingMin;

	private JLabel jLabelGuessingMax;

	private JLabel jLabelGuessingInfo;

	private JLabel jLabelExhibitionRate;

	private JLabel jLabelExhibitionRateMin;

	private JLabel jLabelExhibitionRateMax;

	private JLabel jLabelExhibitionRateInfo;

	private JLabel jLabelTimeAnswer;

	private JLabel jLabelTimeAnswerMin;

	private JLabel jLabelTimeAnswerMax;

	private JLabel jLabelTimeAnswerInfo;

	private JLabel jLabelSuccessRate;

	private JLabel jLabelSuccessRateMin;

	private JLabel jLabelSuccessRateMax;

	private JLabel jLabelSuccessRateInfo;

	private JLabel jLabelNumberOfAnswers;

	private JLabel jLabelNumberOfAnswersMax;

	private JLabel jLabelNumberOfAnswersMin;

	private JLabel jLabelClassicTest;

	private JLabel jLabelAdaptiveTest;

	private JLabel jLabelStatus;

	private JComboBox jComboBoxCourse;

	private JComboBox jComboBoxNumberOfAnswersMax;

	private JComboBox jComboBoxNumberOfAnswersMin;

	private JList jListQuestionsFile;

	private JButton jButtonFind;

	private JButton jButtonCancel;

	private JButton jButtonClear;

	private JProgressBar jProgressBar;

	private JTextField jTextFieldCodeQuestion;

	private JTextField jTextFieldDifficultyMin;

	private JTextField jTextFieldDifficultyMax;

	private JTextField jTextFieldDiscriminationMin;

	private JTextField jTextFieldDiscriminationMax;

	private JTextField jTextFieldGuessingMin;

	private JTextField jTextFieldGuessingMax;

	private JTextField jTextFieldExhibitionRateMin;

	private JTextField jTextFieldExhibitionRateMax;

	private JTextField jTextFieldAnswerTimeMin;

	private JTextField jTextFieldAnswerTimeMax;

	private JTextField jTextFieldSuccessRateMin;

	private JTextField jTextFieldSuccessRateMax;

	private BoxLayout boxLayoutPanelContent;

	private BoxLayout boxLayoutPanelQuestion;

	private BoxLayout boxLayoutPanelDifficulty;

	private BoxLayout boxLayoutPanelDiscrimination;

	private BoxLayout boxLayoutPanelGuessing;

	private BoxLayout boxLayoutPanelExhibitionRate;

	private BoxLayout boxLayoutPanelTimeAnswer;

	private BoxLayout boxLayoutPanelSuccessRate;

	private BoxLayout boxLayoutPanelClassicTest;

	private BoxLayout boxLayoutPanelAdaptiveTest;

	private FlowLayout flowLayoutPanelInstructions;

	private FlowLayout flowLayoutPanelCourse;

	private FlowLayout flowLayoutPanelQuestionsFile;

	private FlowLayout flowLayoutPanelCourseQuestionsFile;

	private FlowLayout flowLayoutPanelCodeQuestion;

	private FlowLayout flowLayoutPanelRadioButtonImage;

	private FlowLayout flowLayoutPanelDifficultyMinMax;

	private FlowLayout flowLayoutPanelDiscriminationMinMax;

	private FlowLayout flowLayoutPanelGuessingMinMax;

	private FlowLayout flowLayoutPanelExhibitionRateMinMax;

	private FlowLayout flowLayoutPanelTimeAnswerMinMax;

	private FlowLayout flowLayoutPanelSuccessRateMinMax;

	private FlowLayout flowLayoutPanelIrtParameters;

	private FlowLayout flowLayoutPanelStatisticParameters;

	private FlowLayout flowLayoutPanelNumberOfAnswers;

	private FlowLayout flowLayoutPanelTest;

	private FlowLayout flowLayoutPanelRadioButtonClassicTest;

	private FlowLayout flowLayoutPanelRadioButtonAdaptiveTest;

	private FlowLayout flowLayoutPanelButton;

	private FlowLayout flowLayoutPanelStatus;

	private ImageIcon iconFind;

	private ImageIcon iconCancel;

	private ImageIcon iconClear;

	private ImageIcon iconInfoGreen16;

	private ImageIcon iconInfoRed16;

	private ImageIcon iconTest16;

	/*
	 * NOMBRE: FrmFindQuestions (Constructor de esta misma clase). PERTENECE A:
	 * Clase FrmFindQuestions. LLAMADA POR: Al invocar la clase. LLAMA A:
	 * getQuestionsFileList(); JLabelInit(); JTextFieldInit(); JImageIconInit();
	 * JComboBoxInit(); JListInit(); JPanelInit(); JRadioButtonInit();
	 * JTextAreaInit(); JButtonInit(); JProgressBarInit(); 
	 * * RECIBE: title: Objeto de tipo String. Indica el t�tulo del frame.
	 * width: Objeto de tipo int. Indica la anchura inicial del frame.
	 * height: Objeto de tipo int. Indica la altura inicial del frame.
	 * userLogin: Objeto de tipo String. Indica el login del usuario.
	 * userPassword: Objeto de tipo String. Indica el password del usuario.
	 * courseVector: Objeto de tipo Vector. Contiene en cada posici�n un objeto
	 * de tipo Course con los datos usados del curso. codeBase: Objeto de tipo
	 * URL. Contiene la URL del servidor desde donde se ha cargado el applet.
	 * wowPath: Objeto de tipo String. Contiene el path del sistema desde el que
	 * se descarg� el applet. iconTestEditorPath: Objeto de tipo String. Contiene
	 * el path del directorio que contiene los iconos usados por la interfaz
	 * gr�fica. : Objeto de tipo JFrame. Contiene el JFrame principal del
	 * applet y padre de este frame. DEVUELVE: nada. FUNCI�N:
	 * Constructor de la clase FrmFindQuestions. Se encarga de configurar el
	 * aspecto del frame, su funcionalidad, as� como de inicializar sus
	 * componentes.
	 */

	public FrmFindQuestions(String aTitle, int aWidth, int aHeight, String aUserLogin, String aUserPassword,
			Vector aCourseVector, URL aCodeBase, String anWOWPath, String iconPath, TestEditor father) {
	//
		// Calls the parent constructor
		super(aTitle, aWidth, aHeight, father, anWOWPath, iconPath);
		
		// Sets user variables
		userLogin = aUserLogin;
		userPassword = aUserPassword;
		courseVector = aCourseVector;
		courseNameVector = new Vector();

		for (int i = 0; i < courseVector.size(); i++) {
			courseNameVector.add(((Course) courseVector.get(i)).getName().trim());
		}

		// Inicializaci�n de questionsFileVector con los nombres de los ficheros
		// de preguntas del curso que se mostrara al iniciar el frame en
		// el jComboCourse.
		questionsFileVector = ((Course) courseVector.get(0)).getQuestionsFileNames();
		codeBase = aCodeBase;

		DIFFICULTY_MAX = father.DIFFICULTY_MAX;
		DIFFICULTY_NORMAL = father.DIFFICULTY_NORMAL;
		DIFFICULTY_MIN = father.DIFFICULTY_MIN;
		DISCRIMINATION_MAX = father.DISCRIMINATION_MAX;
		DISCRIMINATION_MIN = father.DISCRIMINATION_MIN;
		DISCRIMINATION_NORMAL = father.DISCRIMINATION_NORMAL;
		GUESSING_MAX = father.GUESSING_MAX;
		GUESSING_MIN = father.GUESSING_MIN;
		NUMBER_OF_ANSWERS_MAX = TestEditor.NUMBER_OF_ANSWERS_MAX;
		NUMBER_OF_ANSWERS_MIN = TestEditor.NUMBER_OF_ANSWERS_MIN;
		EXHIBITION_RATE_MAX = father.EXHIBITION_RATE_MAX;
		EXHIBITION_RATE_MIN = father.EXHIBITION_RATE_MIN;
		ANSWER_TIME_MAX = father.ANSWER_TIME_MAX;
		ANSWER_TIME_MIN = father.ANSWER_TIME_MIN;
		SUCCESS_RATE_MAX = father.SUCCESS_RATE_MAX;
		SUCCESS_RATE_MIN = father.SUCCESS_RATE_MIN;

		try {
			JLabelInit();
			JTextAreaInit();
			JTextFieldInit();
			JComboBoxInit();
			JListInit(questionsFileVector);
			JRadioButtonInit();
			JImageIconInit();
			JButtonInit();
			JProgressBarInit();
			JPanelInit();
			frameInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	// //////////////////////////////////////////////////////////////////////////////
	// //////// INICIALIZACI�N DE LOS COMPONENTES DEL JINTERNALFRAME
	// ////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: JLabelInit. PERTENECE A: Clase FrmFindQuestions. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JLabel del frame,
	 * reservando memoria para los mismos y estableciendo su color, su
	 * contenido...
	 */

	private void JLabelInit() {
		// Reserva de memoria y establecimiento del contenido para los JLabel.
		jLabelInstructions = new JLabel("Introduce the parameters to search");
		jLabelInstructions.setFont(new java.awt.Font("Dialog", 1, 20));

		jLabelCourse = new JLabel("Course: ");
		jLabelCourse.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelQuestionsFile = new JLabel("Question File: ");
		jLabelQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelCodeQuestion = new JLabel("Code of Question: ");
		jLabelCodeQuestion.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelDifficulty = new JLabel(" Difficulty ");
		jLabelDifficulty.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelDifficultyMin = new JLabel(" Min ");
		jLabelDifficultyMin.setFont(new java.awt.Font("Dialg", 1, 12));

		jLabelDifficultyMax = new JLabel(" Max ");
		jLabelDifficultyMax.setFont(new java.awt.Font("Dialg", 1, 12));

		jLabelDifficultyInfo = new JLabel(" Very easy = " + String.valueOf(DIFFICULTY_MIN) + " || "
				+ "Normal = " + String.valueOf(DIFFICULTY_NORMAL) + " || " + "Very difficult = "
				+ String.valueOf(DIFFICULTY_MAX) + " ");
		jLabelDifficultyInfo.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelDiscrimination = new JLabel(" Discrimination");
		jLabelDiscrimination.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelDiscriminationMin = new JLabel(" Min ");
		jLabelDiscriminationMin.setFont(new java.awt.Font("Dialg", 1, 12));

		jLabelDiscriminationMax = new JLabel(" Max ");
		jLabelDiscriminationMax.setFont(new java.awt.Font("Dialg", 1, 12));

		jLabelDiscriminationInfo = new JLabel(" Not very discriminatory = "
				+ String.valueOf(DISCRIMINATION_MIN) + "|| " + "Normal = "
				+ String.valueOf(DISCRIMINATION_NORMAL) + " || " + "Very discriminatory = "
				+ String.valueOf(DISCRIMINATION_MAX) + " ");
		jLabelDiscriminationInfo.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelGuessing = new JLabel(" Guessing ");
		jLabelGuessing.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelGuessingMin = new JLabel(" Min ");
		jLabelGuessingMin.setFont(new java.awt.Font("Dialg", 1, 12));

		jLabelGuessingMax = new JLabel(" Max ");
		jLabelGuessingMax.setFont(new java.awt.Font("Dialg", 1, 12));

		jLabelGuessingInfo = new JLabel(" Value between " + String.valueOf(GUESSING_MIN) + " and "
				+ String.valueOf(GUESSING_MAX) + " ");
		jLabelGuessingInfo.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelNumberOfAnswers = new JLabel("Number of Answers: ");
		jLabelNumberOfAnswers.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelNumberOfAnswersMin = new JLabel("Min ");
		jLabelNumberOfAnswersMin.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelNumberOfAnswersMax = new JLabel("Max ");
		jLabelNumberOfAnswersMax.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelExhibitionRate = new JLabel("Exhibition rate");
		jLabelExhibitionRate.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelExhibitionRateMin = new JLabel("Min");
		jLabelExhibitionRateMin.setFont(new java.awt.Font("Dialg", 1, 12));

		jLabelExhibitionRateMax = new JLabel("Max");
		jLabelExhibitionRateMax.setFont(new java.awt.Font("Dialg", 1, 12));

		jLabelExhibitionRateInfo = new JLabel("Value between " + String.valueOf(EXHIBITION_RATE_MIN) + " and "
				+ String.valueOf(EXHIBITION_RATE_MAX));
		jLabelExhibitionRateInfo.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelTimeAnswer = new JLabel("Half time of answer");
		jLabelTimeAnswer.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelTimeAnswerMin = new JLabel("Min");
		jLabelTimeAnswerMin.setFont(new java.awt.Font("Dialg", 1, 12));

		jLabelTimeAnswerMax = new JLabel("Max");
		jLabelTimeAnswerMax.setFont(new java.awt.Font("Dialg", 1, 12));

		jLabelTimeAnswerInfo = new JLabel("Value between " + String.valueOf(ANSWER_TIME_MIN) + " and "
				+ String.valueOf(ANSWER_TIME_MAX));
		jLabelTimeAnswerInfo.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelSuccessRate = new JLabel("Success Rate");
		jLabelSuccessRate.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelSuccessRateMin = new JLabel("Min");
		jLabelSuccessRateMin.setFont(new java.awt.Font("Dialg", 1, 12));

		jLabelSuccessRateMax = new JLabel("Max");
		jLabelSuccessRateMax.setFont(new java.awt.Font("Dialg", 1, 12));

		jLabelSuccessRateInfo = new JLabel("Value between " + String.valueOf(SUCCESS_RATE_MIN) + " and "
				+ String.valueOf(SUCCESS_RATE_MAX));
		jLabelSuccessRateInfo.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelClassicTest = new JLabel(" The questions SHOULD BELONG to some CLASSIC test: ");
		jLabelClassicTest.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelAdaptiveTest = new JLabel(" The questions SHOULD BELONG to some ADAPTIVE test: ");
		jLabelAdaptiveTest.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelStatus = new JLabel("Done                                      "
				+ "                                          ");
		jLabelStatus.setFont(new java.awt.Font("Dialog", 0, 12));
	}

	/*
	 * NOMBRE: JTextAreaInit. PERTENECE A: Clase FrmFindQuestions. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa los JTextArea del
	 * frame.
	 */

	private void JTextAreaInit() {
		// Reserva de memoria para el jTextAreaQuestion, que contendr� el
		// enunciado de la pregunta.
		jTextAreaQuestion = new JTextArea("", 1, 2);
		jTextAreaQuestion.setLineWrap(true);
		jTextAreaQuestion.setFont(new java.awt.Font("Dialog", 0, 13));
		jTextAreaQuestion.setBackground(SystemColor.white);
		jTextAreaQuestion.setBorder(BorderFactory.createLineBorder(SystemColor.black));
		// Reserfa del JScrollPane para el jTextAreaQuestion.
		jScrollPaneQuestion = new JScrollPane(jTextAreaQuestion, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPaneQuestion.setBorder(BorderFactory.createEmptyBorder());
	}

	/*
	 * NOMBRE: JTextFieldInit. PERTENECE A: Clase FrmFindQuestions. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa los JTextField del
	 * frame, reservando memoria para los mismos y estableciendo su
	 * tama�o, su contenido, funcionalidad...
	 */
	private void JTextFieldInit() {
		// Reserva de memoria y establecimiento de la funcionalidad.
		jTextFieldCodeQuestion = new JTextField(4);
		jTextFieldCodeQuestion.setFocusable(true);

		// Reserva de memoria para los JTextField.
		jTextFieldDifficultyMax = new JTextField(String.valueOf(DIFFICULTY_MAX), 4);
		jTextFieldDifficultyMin = new JTextField(String.valueOf(DIFFICULTY_MIN), 4);

		jTextFieldDiscriminationMax = new JTextField(String.valueOf(DISCRIMINATION_MAX), 4);
		jTextFieldDiscriminationMin = new JTextField(String.valueOf(DISCRIMINATION_MIN), 4);

		jTextFieldGuessingMax = new JTextField(String.valueOf(GUESSING_MAX), 4);
		jTextFieldGuessingMin = new JTextField(String.valueOf(GUESSING_MIN), 4);

		// Establecimiento de la funcionalidad de jTextFieldDifficultyMin.
		jTextFieldDifficultyMin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldDifficultyMin.getText().length() > 0) {
						if (Double.valueOf(jTextFieldDifficultyMin.getText()).doubleValue() < DIFFICULTY_MIN) {
							jTextFieldDifficultyMin.setText(String.valueOf(DIFFICULTY_MIN));
						} else {
							if (Double.valueOf(jTextFieldDifficultyMin.getText()).doubleValue() > DIFFICULTY_MAX) {
								jTextFieldDifficultyMin.setText(String.valueOf(DIFFICULTY_MAX));
							} else {
								if (Double.valueOf(jTextFieldDifficultyMin.getText()).doubleValue() > Double.valueOf(
										jTextFieldDifficultyMax.getText()).doubleValue()) {
									String difficultyTemp = jTextFieldDifficultyMin.getText().trim();
									jTextFieldDifficultyMin.setText(jTextFieldDifficultyMax.getText());
									jTextFieldDifficultyMax.setText(difficultyTemp);
								}
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					if (jTextFieldDifficultyMin.getText().trim().length() > 1)
						jTextFieldDifficultyMin.setText(String.valueOf(DIFFICULTY_MIN));
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
						} else {
							if (Double.valueOf(jTextFieldDifficultyMax.getText()).doubleValue() > DIFFICULTY_MAX) {
								jTextFieldDifficultyMax.setText(String.valueOf(DIFFICULTY_MAX));
							} else {
								if (Double.valueOf(jTextFieldDifficultyMax.getText()).doubleValue() < Double.valueOf(
										jTextFieldDifficultyMin.getText()).doubleValue()) {
									String difficultyTemp = jTextFieldDifficultyMin.getText().trim();
									jTextFieldDifficultyMin.setText(jTextFieldDifficultyMax.getText());
									jTextFieldDifficultyMax.setText(difficultyTemp);
								}
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					if (jTextFieldDifficultyMax.getText().trim().length() > 1)
						jTextFieldDifficultyMax.setText(String.valueOf(DIFFICULTY_MAX));
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
						} else {
							if (Double.valueOf(jTextFieldDiscriminationMin.getText()).doubleValue() > DISCRIMINATION_MAX) {
								jTextFieldDiscriminationMin.setText(String.valueOf(DISCRIMINATION_MAX));
							} else {
								if (Double.valueOf(jTextFieldDiscriminationMin.getText()).doubleValue() > Double
										.valueOf(jTextFieldDiscriminationMax.getText()).doubleValue()) {
									String discriminationTemp = jTextFieldDiscriminationMin.getText().trim();
									jTextFieldDiscriminationMin.setText(jTextFieldDiscriminationMax.getText());
									jTextFieldDiscriminationMax.setText(discriminationTemp);
								}
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					if (jTextFieldDiscriminationMin.getText().trim().length() > 1)
						jTextFieldDiscriminationMin.setText(String.valueOf(DISCRIMINATION_MIN));
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
						} else {
							if (Double.valueOf(jTextFieldDiscriminationMax.getText()).doubleValue() > DISCRIMINATION_MAX) {
								jTextFieldDiscriminationMax.setText(String.valueOf(DISCRIMINATION_MAX));
							} else {
								if (Double.valueOf(jTextFieldDiscriminationMax.getText()).doubleValue() < Double
										.valueOf(jTextFieldDiscriminationMin.getText()).doubleValue()) {
									String discriminationTemp = jTextFieldDiscriminationMin.getText().trim();
									jTextFieldDiscriminationMin.setText(jTextFieldDiscriminationMax.getText());
									jTextFieldDiscriminationMax.setText(discriminationTemp);
								}
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					if (jTextFieldDiscriminationMax.getText().trim().length() > 1)
						jTextFieldDiscriminationMax.setText(String.valueOf(DISCRIMINATION_MAX));
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
						} else {
							if (Double.valueOf(jTextFieldGuessingMin.getText()).doubleValue() > DISCRIMINATION_MAX) {
								jTextFieldGuessingMin.setText(String.valueOf(GUESSING_MAX));
							} else {
								if (Double.valueOf(jTextFieldGuessingMax.getText()).doubleValue() > Double.valueOf(
										jTextFieldGuessingMin.getText()).doubleValue()) {
									String guessingTemp = jTextFieldGuessingMin.getText().trim();
									jTextFieldGuessingMin.setText(jTextFieldGuessingMax.getText());
									jTextFieldGuessingMax.setText(guessingTemp);
								}
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					if (jTextFieldGuessingMin.getText().trim().length() > 1)
						jTextFieldGuessingMin.setText(String.valueOf(GUESSING_MIN));
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
						} else {
							if (Double.valueOf(jTextFieldGuessingMax.getText()).doubleValue() > DISCRIMINATION_MAX) {
								jTextFieldGuessingMax.setText(String.valueOf(GUESSING_MAX));
							} else {
								if (Double.valueOf(jTextFieldGuessingMax.getText()).doubleValue() < Double.valueOf(
										jTextFieldGuessingMin.getText()).doubleValue()) {
									String guessingTemp = jTextFieldGuessingMin.getText().trim();
									jTextFieldGuessingMin.setText(jTextFieldGuessingMax.getText());
									jTextFieldGuessingMax.setText(guessingTemp);
								}

							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					if (jTextFieldGuessingMax.getText().trim().length() > 1)
						jTextFieldGuessingMax.setText(String.valueOf(GUESSING_MAX));
				}
			}
		});

		jTextFieldExhibitionRateMax = new JTextField(String.valueOf(EXHIBITION_RATE_MAX), 4);
		jTextFieldExhibitionRateMin = new JTextField(String.valueOf(EXHIBITION_RATE_MIN), 4);

		jTextFieldAnswerTimeMax = new JTextField(String.valueOf(ANSWER_TIME_MAX), 4);
		jTextFieldAnswerTimeMin = new JTextField(String.valueOf(ANSWER_TIME_MIN), 4);

		jTextFieldSuccessRateMax = new JTextField(String.valueOf(SUCCESS_RATE_MAX), 4);
		jTextFieldSuccessRateMin = new JTextField(String.valueOf(SUCCESS_RATE_MIN), 4);

		// Establecimiento de la funcionalidad de jTextFieldExhibitionRateMin.
		jTextFieldExhibitionRateMin.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldExhibitionRateMin.getText().length() > 0) {
						if (Double.valueOf(jTextFieldExhibitionRateMin.getText()).doubleValue() < EXHIBITION_RATE_MIN) {
							jTextFieldExhibitionRateMin.setText(String.valueOf(EXHIBITION_RATE_MIN));
						} else {
							if (Double.valueOf(jTextFieldExhibitionRateMin.getText()).doubleValue() > EXHIBITION_RATE_MAX) {
								jTextFieldExhibitionRateMin.setText(String.valueOf(EXHIBITION_RATE_MAX));
							} else {
								if (Double.valueOf(jTextFieldExhibitionRateMin.getText()).doubleValue() > Double
										.valueOf(jTextFieldExhibitionRateMax.getText()).doubleValue()) {
									String exhibitionRateTemp = jTextFieldExhibitionRateMin.getText().trim();
									jTextFieldExhibitionRateMin.setText(jTextFieldExhibitionRateMax.getText());
									jTextFieldExhibitionRateMax.setText(exhibitionRateTemp);
								}
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					if (jTextFieldExhibitionRateMin.getText().trim().length() > 1)
						jTextFieldExhibitionRateMin.setText(String.valueOf(EXHIBITION_RATE_MIN));
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
						} else {
							if (Double.valueOf(jTextFieldExhibitionRateMax.getText()).doubleValue() > EXHIBITION_RATE_MAX) {
								jTextFieldExhibitionRateMax.setText(String.valueOf(EXHIBITION_RATE_MAX));
							} else {
								if (Double.valueOf(jTextFieldExhibitionRateMin.getText()).doubleValue() > Double
										.valueOf(jTextFieldExhibitionRateMax.getText()).doubleValue()) {
									String exhibitionRateTemp = jTextFieldExhibitionRateMin.getText().trim();
									jTextFieldExhibitionRateMin.setText(jTextFieldExhibitionRateMax.getText());
									jTextFieldExhibitionRateMax.setText(exhibitionRateTemp);
								}
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					if (jTextFieldExhibitionRateMax.getText().trim().length() > 1)
						jTextFieldExhibitionRateMax.setText(String.valueOf(EXHIBITION_RATE_MAX));
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
						} else {
							if (Integer.valueOf(jTextFieldAnswerTimeMin.getText()).intValue() > ANSWER_TIME_MAX) {
								jTextFieldAnswerTimeMin.setText(String.valueOf(ANSWER_TIME_MAX));
							} else {
								if (Double.valueOf(jTextFieldAnswerTimeMin.getText()).doubleValue() > Double.valueOf(
										jTextFieldAnswerTimeMax.getText()).doubleValue()) {
									String answerTimeTemp = jTextFieldAnswerTimeMin.getText().trim();
									jTextFieldAnswerTimeMin.setText(jTextFieldAnswerTimeMax.getText());
									jTextFieldAnswerTimeMax.setText(answerTimeTemp);
								}
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					if (jTextFieldAnswerTimeMin.getText().trim().length() > 1)
						jTextFieldAnswerTimeMin.setText(String.valueOf(ANSWER_TIME_MIN));
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
						} else {
							if (Integer.valueOf(jTextFieldAnswerTimeMax.getText()).intValue() > ANSWER_TIME_MAX) {
								jTextFieldAnswerTimeMax.setText(String.valueOf(ANSWER_TIME_MAX));
							} else {
								if (Double.valueOf(jTextFieldAnswerTimeMin.getText()).doubleValue() > Double.valueOf(
										jTextFieldAnswerTimeMax.getText()).doubleValue()) {
									String answerTimeTemp = jTextFieldAnswerTimeMin.getText().trim();
									jTextFieldAnswerTimeMin.setText(jTextFieldAnswerTimeMax.getText());
									jTextFieldAnswerTimeMax.setText(answerTimeTemp);
								}
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					if (jTextFieldAnswerTimeMax.getText().trim().length() > 1)
						jTextFieldAnswerTimeMax.setText(String.valueOf(ANSWER_TIME_MAX));
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
						} else {
							if (Double.valueOf(jTextFieldSuccessRateMin.getText()).doubleValue() > SUCCESS_RATE_MAX) {
								jTextFieldSuccessRateMin.setText(String.valueOf(SUCCESS_RATE_MAX));
							} else {
								if (Double.valueOf(jTextFieldSuccessRateMin.getText()).doubleValue() > Double
										.valueOf(jTextFieldSuccessRateMax.getText()).doubleValue()) {
									String successRateTemp = jTextFieldSuccessRateMin.getText().trim();
									jTextFieldSuccessRateMin.setText(jTextFieldSuccessRateMax.getText());
									jTextFieldSuccessRateMax.setText(successRateTemp);
								}
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					if (jTextFieldSuccessRateMin.getText().trim().length() > 1)
						jTextFieldSuccessRateMin.setText(String.valueOf(SUCCESS_RATE_MIN));
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
						} else {
							if (Double.valueOf(jTextFieldSuccessRateMax.getText()).doubleValue() > SUCCESS_RATE_MAX) {
								jTextFieldSuccessRateMax.setText(String.valueOf(SUCCESS_RATE_MAX));
							} else {
								if (Double.valueOf(jTextFieldSuccessRateMin.getText()).doubleValue() > Double
										.valueOf(jTextFieldSuccessRateMax.getText()).doubleValue()) {
									String successRateTemp = jTextFieldSuccessRateMin.getText().trim();
									jTextFieldSuccessRateMin.setText(jTextFieldSuccessRateMax.getText());
									jTextFieldSuccessRateMax.setText(successRateTemp);
								}
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {
					if (jTextFieldSuccessRateMax.getText().trim().length() > 1)
						jTextFieldSuccessRateMax.setText(String.valueOf(SUCCESS_RATE_MAX));
				}
			}
		});
	}

	/*
	 * NOMBRE: JComboBoxInit. PERTENECE A: Clase FrmFindQuestions. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa los JComboBox del
	 * frame, reservando memoria para los mismos y estableciendo su
	 * tama�o, su contenido, funcionalidad...
	 */

	private void JComboBoxInit() {
		// Reserva de memoria para los JComboBox.
		jComboBoxCourse = new JComboBox();
		jComboBoxNumberOfAnswersMin = new JComboBox();
		jComboBoxNumberOfAnswersMax = new JComboBox();

		// Establecimiento de sus tama�os.
		jComboBoxCourse.setSize(new Dimension(25, 200));
		jComboBoxNumberOfAnswersMin.setSize(new Dimension(26, 70));
		jComboBoxNumberOfAnswersMax.setSize(new Dimension(26, 70));

		// Borrado de sus contenidos previos.
		jComboBoxNumberOfAnswersMin.removeAllItems();
		jComboBoxNumberOfAnswersMax.removeAllItems();

		// Bucle para a�adir los valores a jComboBoxNumberOfAnswersMin y
		// jComboBoxNumberOfAnswersMax.
		for (int i = NUMBER_OF_ANSWERS_MIN; i <= NUMBER_OF_ANSWERS_MAX; i++) {
			jComboBoxNumberOfAnswersMin.addItem(String.valueOf(i));
			jComboBoxNumberOfAnswersMax.addItem(String.valueOf(i));
		}

		// Selecci�n por defecto de los jComboBoxNumberOfAnswers.
		jComboBoxNumberOfAnswersMin.setSelectedItem(String.valueOf(NUMBER_OF_ANSWERS_MIN));
		jComboBoxNumberOfAnswersMax.setSelectedItem(String.valueOf(NUMBER_OF_ANSWERS_MAX));

		// Borrado de sus contenidos previos.
		jComboBoxCourse.removeAllItems();

		// Adici�n a jComboBoxCourse de la lista de cursos del usuario.
		for (int i = 0; i < courseNameVector.size(); i++)
			jComboBoxCourse.addItem(courseNameVector.get(i));

		// Establecimiento de la funcionalidad de jComboBoxCourse.
		jComboBoxCourse.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboBoxCourseActionPerformed();
			}
		});
	}

	/*
	 * NOMBRE: JListInit. PERTENECE A: Clase FrmFindQuestions. LLAMADA POR:
	 * Constructor de esta misma clase. LLAMA A: nada. RECIBE:
	 * questionsFileVector: Objeto de tipo Vector. Contiene los nombres de los
	 * ficheros de preguntas pertenecientes al curso mostrado en el jComboCourse.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JList del frame.
	 */

	private void JListInit(Vector questionsFileVector) {
		// Reserva de memoria para los JList.
		jListQuestionsFile = new JList(questionsFileVector);

		// Establecimiento de visibilidad a false.
		jListQuestionsFile.setVisible(true);

		// Establecimiento del scroll de los JList.
		jListQuestionsFile.setAutoscrolls(true);

		// Establecimiento del borde de los JList.
		jListQuestionsFile.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Establecimiento del n�mero de filas visibles de los JList.
		jListQuestionsFile.setVisibleRowCount(3);

		// Establecimiento del modo de selecci�n de los JList.
		jListQuestionsFile.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// Establecimiento de la funcionalidad de jListQuestionsFile.
		jListQuestionsFile.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if (jListQuestionsFile.getSelectedIndices().length > 0)
					jButtonFind.setEnabled(true);
				else
					jButtonFind.setEnabled(false);
			}
		});
	}

	/*
	 * NOMBRE: JRadioButtonInit. PERTENECE A: Clase FrmFindQuestions. LLAMADA
	 * POR: El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa los JRadioButton que se usan
	 * para indicar si una pregunta a buscar tiene o no imagen asociada.
	 */

	private void JRadioButtonInit() {
		// Reserva de memoria para los JRadioButton.
		jRadioButtonImageYes = new JRadioButton("With Image");
		jRadioButtonImageNo = new JRadioButton("Without Image");
		jRadioButtonImageIndifferent = new JRadioButton("Indifferent");

		jRadioButtonClassicTestYes = new JRadioButton("Yes");
		jRadioButtonClassicTestYes.setBackground(SystemColor.WHITE);

		jRadioButtonClassicTestNo = new JRadioButton("No");
		jRadioButtonClassicTestNo.setBackground(SystemColor.WHITE);

		jRadioButtonClassicTestIndifferent = new JRadioButton("Indifferent");
		jRadioButtonClassicTestIndifferent.setBackground(SystemColor.WHITE);

		jRadioButtonAdaptiveTestYes = new JRadioButton("Yes");
		jRadioButtonAdaptiveTestYes.setBackground(SystemColor.WHITE);

		jRadioButtonAdaptiveTestNo = new JRadioButton("No");
		jRadioButtonAdaptiveTestNo.setBackground(SystemColor.WHITE);

		jRadioButtonAdaptiveTestIndifferent = new JRadioButton("Indifferent");
		jRadioButtonAdaptiveTestIndifferent.setBackground(SystemColor.WHITE);

		// Reserva de memoria para el jRadioButtonGroupImage.
		jRadioButtonGroupImage = new ButtonGroup();
		jRadioButtonGroupClassicTest = new ButtonGroup();
		jRadioButtonGroupAdaptiveTest = new ButtonGroup();

		// Adici�n de los JRadioButton al ButtonGroup, lo cual permite que no se
		// puedan seleccionar m�s de uno JRadioButton al mismo tiempo.
		jRadioButtonGroupImage.add(jRadioButtonImageYes);
		jRadioButtonGroupImage.add(jRadioButtonImageNo);
		jRadioButtonGroupImage.add(jRadioButtonImageIndifferent);

		jRadioButtonGroupClassicTest.add(jRadioButtonClassicTestYes);
		jRadioButtonGroupClassicTest.add(jRadioButtonClassicTestNo);
		jRadioButtonGroupClassicTest.add(jRadioButtonClassicTestIndifferent);

		jRadioButtonGroupAdaptiveTest.add(jRadioButtonAdaptiveTestYes);
		jRadioButtonGroupAdaptiveTest.add(jRadioButtonAdaptiveTestNo);
		jRadioButtonGroupAdaptiveTest.add(jRadioButtonAdaptiveTestIndifferent);

		// Establecimiento de la selecci�n por defecto.
		jRadioButtonImageIndifferent.setSelected(true);
		jRadioButtonClassicTestIndifferent.setSelected(true);
		jRadioButtonAdaptiveTestIndifferent.setSelected(true);
	}

	/*
	 * NOMBRE: JImageIconInit. PERTENECE A: Clase FrmFindQuestions. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa los ImageIcon del
	 * frame, reservando memoria para los mismos y estableciendo su
	 * tama�o, su contenido, funcionalidad... Los iconos se encuentran en el
	 * servidor, con lo que ser� solicitado el envio de los mismos desde el
	 * servidor al applet descargado.
	 */

	private void JImageIconInit() {
		// Construcci�n del String de solicitud de los iconos al servidor.
		String petitionFind = wowPath + iconTestEditorPath + "find32.gif";
		String petitionCancel = wowPath + iconTestEditorPath + "cancel32.gif";
		String petitionClear = wowPath + iconTestEditorPath + "clear32.gif";
		String petitionInfoGreen16 = wowPath + iconTestEditorPath + "info_green_16.gif";
		String petitionInfoRed16 = wowPath + iconTestEditorPath + "info_red_16.gif";
		String petitionTest16 = wowPath + iconTestEditorPath + "testFiles16.gif";

		// Conexi�n con el servidor.
		try {
			URL urlFind = new URL(codeBase, petitionFind);
			URL urlCancel = new URL(codeBase, petitionCancel);
			URL urlClear = new URL(codeBase, petitionClear);
			URL urlInfoGreen16 = new URL(codeBase, petitionInfoGreen16);
			URL urlInfoRed16 = new URL(codeBase, petitionInfoRed16);
			URL urlTest16 = new URL(codeBase, petitionTest16);

			// Reserva de memoria para los ImageIcon con los iconos obtenidos.
			iconFind = new ImageIcon(urlFind);
			iconCancel = new ImageIcon(urlCancel);
			iconClear = new ImageIcon(urlClear);
			iconInfoGreen16 = new ImageIcon(urlInfoGreen16);
			iconInfoRed16 = new ImageIcon(urlInfoRed16);
			iconTest16 = new ImageIcon(urlTest16);

			ImageIconLoad = true;
		} catch (java.net.MalformedURLException e) {
			ImageIconLoad = false;
		}
	}

	/*
	 * NOMBRE: JButtonInit. PERTENECE A: Clase FrmFindQuestions. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JButton del frame. Estos
	 * se encargan de realizar las operaciones de busqueda de preguntas en un
	 * fichero.
	 */

	private void JButtonInit() {
		// Reserva de memoria para los JButton, dependiendo de si los
		// iconos del JInteralFrame se han cargado con �xito o no.
		if (ImageIconLoad == true) {
			jButtonFind = new JButton("Find", iconFind);
			jButtonCancel = new JButton("Cancel", iconCancel);
			jButtonClear = new JButton("Clear", iconClear);
		} else {
			jButtonFind = new JButton("Find");
			jButtonCancel = new JButton("Cancel");
			jButtonClear = new JButton("Clear");
		}

		jButtonFind.setEnabled(false);

		// Establecimiento de la funcionalidad de jButtonFind.
		jButtonFind.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonFindActionPerformed();
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
				jButtonClearActionPerformed();
			}
		});
	}

	/*
	 * NOMBRE: JProgressBarInit. PERTENECE A: Clase FrmFindQuestions. LLAMADA
	 * POR: El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa el JProgressBar del JFrame.
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
	 * NOMBRE: JPanelInit. PERTENECE A: Clase FrmFindQuestions. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JPanel del frame,
	 * reservando memoria para el mismo, estableciendo su color, layout...
	 */

	private void JPanelInit() {
		// Reserva de memoria para el panel principal del frame y
		// establecimiento de su layout.
		jPanelContent = new JPanel();
		boxLayoutPanelContent = new BoxLayout(jPanelContent, BoxLayout.PAGE_AXIS);
		jPanelContent.setLayout(boxLayoutPanelContent);

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
		// contendr� a los jLabelCourse....
		jPanelCourse = new JPanel();
		flowLayoutPanelCourse = new FlowLayout(FlowLayout.CENTER);
		jPanelCourse.setLayout(flowLayoutPanelCourse);
		jPanelCourse.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jPanelCourse.add(jLabelCourse);
		jPanelCourse.add(jComboBoxCourse);

		jPanelQuestionsFile = new JPanel();
		flowLayoutPanelQuestionsFile = new FlowLayout(FlowLayout.CENTER);
		jPanelQuestionsFile.setLayout(flowLayoutPanelQuestionsFile);
		jPanelQuestionsFile.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jScrollPaneJListQuestionsFile = new JScrollPane(jListQuestionsFile,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		jPanelQuestionsFile.add(jLabelQuestionsFile);
		jPanelQuestionsFile.add(jScrollPaneJListQuestionsFile);

		// Reserva de memoria y establecimiento del layout del JPanel que
		// contiene a jLabelCodeQuestion y a jTextFieldCodeQuestion.
		jPanelCodeQuestion = new JPanel();
		flowLayoutPanelCodeQuestion = new FlowLayout(FlowLayout.LEFT);
		jPanelCodeQuestion.setLayout(flowLayoutPanelCodeQuestion);
		jPanelCodeQuestion.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jPanelCodeQuestion.add(jLabelCodeQuestion);
		jPanelCodeQuestion.add(jTextFieldCodeQuestion);

		jPanelCourseQuestionsFile = new JPanel();
		flowLayoutPanelCourseQuestionsFile = new FlowLayout(FlowLayout.CENTER);
		jPanelCourseQuestionsFile.setLayout(flowLayoutPanelCourseQuestionsFile);
		jPanelCourseQuestionsFile.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), " Course and Question File ", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelCourseQuestionsFile.add(jPanelCourse);
		jPanelCourseQuestionsFile.add(jPanelQuestionsFile);
		jPanelCourseQuestionsFile.add(jPanelCodeQuestion);

		// Reserva de memoria para los paneles que contendr�n los componentes
		// del JInteralFrame, independientemente de que haya sido invocado para
		// a�adir preguntas o para modificar o borrar.
		jPanelQuestion = new JPanel();
		jPanelQuestion.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), " Enunciate of the Question ", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		boxLayoutPanelQuestion = new BoxLayout(jPanelQuestion, BoxLayout.Y_AXIS);
		jPanelQuestion.setLayout(boxLayoutPanelQuestion);

		jPanelQuestion.add(jScrollPaneQuestion);

		// Reserva de memoria para el JPanel que contendra al jCheckBoxImage.
		jPanelRadioButtonImage = new JPanel();
		flowLayoutPanelRadioButtonImage = new FlowLayout(FlowLayout.LEFT);
		jPanelRadioButtonImage.setLayout(flowLayoutPanelRadioButtonImage);
		jPanelRadioButtonImage.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), " Question with image ", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelRadioButtonImage.add(jRadioButtonImageYes);
		jPanelRadioButtonImage.add(jRadioButtonImageNo);
		jPanelRadioButtonImage.add(jRadioButtonImageIndifferent);

		// Reserva de memoria para el JPanel que contendr� al
		// jLabelNumberOfAnswers
		// y al jComboBoxNumberOfAnswers.
		jPanelNumberOfAnswers = new JPanel();
		flowLayoutPanelNumberOfAnswers = new FlowLayout(FlowLayout.CENTER);
		jPanelNumberOfAnswers.setLayout(flowLayoutPanelNumberOfAnswers);
		jPanelNumberOfAnswers.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jPanelNumberOfAnswers.add(jLabelNumberOfAnswers);
		jPanelNumberOfAnswers.add(jLabelNumberOfAnswersMin);
		jPanelNumberOfAnswers.add(jComboBoxNumberOfAnswersMin);
		jPanelNumberOfAnswers.add(jLabelNumberOfAnswersMax);
		jPanelNumberOfAnswers.add(jComboBoxNumberOfAnswersMax);

		// Reserva de memoria para el jPanelDifficultyMin.
		jPanelDifficultyMinMax = new JPanel();
		flowLayoutPanelDifficultyMinMax = new FlowLayout(FlowLayout.CENTER);
		jPanelDifficultyMinMax.setLayout(flowLayoutPanelDifficultyMinMax);
		jPanelDifficultyMinMax.setBackground(SystemColor.WHITE);

		jPanelDifficultyMinMax.add(jLabelDifficultyMin);
		jPanelDifficultyMinMax.add(jTextFieldDifficultyMin);
		jPanelDifficultyMinMax.add(jLabelDifficultyMax);
		jPanelDifficultyMinMax.add(jTextFieldDifficultyMax);

		// Reserva de memoria para el JPanel que contiene a los par�metros de la
		// pregunta.
		jPanelDifficulty = new JPanel();
		boxLayoutPanelDifficulty = new BoxLayout(jPanelDifficulty, BoxLayout.Y_AXIS);
		jPanelDifficulty.setLayout(boxLayoutPanelDifficulty);
		jPanelDifficulty.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelDifficulty.setBackground(SystemColor.WHITE);

		jLabelDifficulty.setAlignmentX(LEFT_ALIGNMENT);
		jPanelDifficultyMinMax.setAlignmentX(LEFT_ALIGNMENT);
		jLabelDifficultyInfo.setAlignmentX(LEFT_ALIGNMENT);

		jPanelDifficulty.add(jLabelDifficulty);
		jPanelDifficulty.add(jPanelDifficultyMinMax);
		jPanelDifficulty.add(jLabelDifficultyInfo);

		// Reserva de memoria para el jPanelDiscriminationMin.
		jPanelDiscriminationMinMax = new JPanel();
		flowLayoutPanelDiscriminationMinMax = new FlowLayout(FlowLayout.CENTER);
		jPanelDiscriminationMinMax.setLayout(flowLayoutPanelDiscriminationMinMax);
		jPanelDiscriminationMinMax.setBackground(SystemColor.WHITE);

		jPanelDiscriminationMinMax.add(jLabelDiscriminationMin);
		jPanelDiscriminationMinMax.add(jTextFieldDiscriminationMin);
		jPanelDiscriminationMinMax.add(jLabelDiscriminationMax);
		jPanelDiscriminationMinMax.add(jTextFieldDiscriminationMax);

		jPanelDiscrimination = new JPanel();
		boxLayoutPanelDiscrimination = new BoxLayout(jPanelDiscrimination, BoxLayout.Y_AXIS);
		jPanelDiscrimination.setLayout(boxLayoutPanelDiscrimination);
		jPanelDiscrimination.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelDiscrimination.setBackground(SystemColor.WHITE);

		jLabelDiscrimination.setAlignmentX(LEFT_ALIGNMENT);
		jPanelDiscriminationMinMax.setAlignmentX(LEFT_ALIGNMENT);
		jLabelDiscriminationInfo.setAlignmentX(LEFT_ALIGNMENT);

		jPanelDiscrimination.add(jLabelDiscrimination);
		jPanelDiscrimination.add(jPanelDiscriminationMinMax);
		jPanelDiscrimination.add(jLabelDiscriminationInfo);

		// Reserva de memoria para el jPanelGuessingMin.
		jPanelGuessingMinMax = new JPanel();
		flowLayoutPanelGuessingMinMax = new FlowLayout(FlowLayout.CENTER);
		jPanelGuessingMinMax.setLayout(flowLayoutPanelGuessingMinMax);
		jPanelGuessingMinMax.setBackground(SystemColor.WHITE);

		jPanelGuessingMinMax.add(jLabelGuessingMin);
		jPanelGuessingMinMax.add(jTextFieldGuessingMin);
		jPanelGuessingMinMax.add(jLabelGuessingMax);
		jPanelGuessingMinMax.add(jTextFieldGuessingMax);

		jPanelGuessing = new JPanel();
		boxLayoutPanelGuessing = new BoxLayout(jPanelGuessing, BoxLayout.Y_AXIS);
		jPanelGuessing.setLayout(boxLayoutPanelGuessing);
		jPanelGuessing.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelGuessing.setBackground(SystemColor.WHITE);

		jLabelGuessing.setAlignmentX(LEFT_ALIGNMENT);
		jPanelGuessingMinMax.setAlignmentX(LEFT_ALIGNMENT);
		jLabelGuessingInfo.setAlignmentX(LEFT_ALIGNMENT);

		jPanelGuessing.add(jLabelGuessing);
		jPanelGuessing.add(jPanelGuessingMinMax);
		jPanelGuessing.add(jLabelGuessingInfo);

		jPanelIrtParameters = new JPanel();
		flowLayoutPanelIrtParameters = new FlowLayout(FlowLayout.CENTER);
		jPanelIrtParameters.setLayout(flowLayoutPanelIrtParameters);

		jPanelDifficulty.setAlignmentX(LEFT_ALIGNMENT);
		jPanelDiscrimination.setAlignmentX(LEFT_ALIGNMENT);
		jPanelGuessing.setAlignmentX(LEFT_ALIGNMENT);

		jPanelIrtParameters.add(jPanelDifficulty);
		jPanelIrtParameters.add(jPanelDiscrimination);
		// jPanelIrtParameters.add(jPanelGuessing);

		// Reserva de memoria para el jPanelExhibitionRateMin.
		jPanelExhibitionRateMinMax = new JPanel();
		flowLayoutPanelExhibitionRateMinMax = new FlowLayout(FlowLayout.CENTER);
		jPanelExhibitionRateMinMax.setLayout(flowLayoutPanelExhibitionRateMinMax);
		jPanelExhibitionRateMinMax.setBackground(SystemColor.WHITE);

		jPanelExhibitionRateMinMax.add(jLabelExhibitionRateMin);
		jPanelExhibitionRateMinMax.add(jTextFieldExhibitionRateMin);
		jPanelExhibitionRateMinMax.add(jLabelExhibitionRateMax);
		jPanelExhibitionRateMinMax.add(jTextFieldExhibitionRateMax);

		jPanelExhibitionRate = new JPanel();
		boxLayoutPanelExhibitionRate = new BoxLayout(jPanelExhibitionRate, BoxLayout.Y_AXIS);
		jPanelExhibitionRate.setLayout(boxLayoutPanelExhibitionRate);
		jPanelExhibitionRate.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelExhibitionRate.setBackground(SystemColor.WHITE);

		jPanelExhibitionRate.add(jLabelExhibitionRate);
		jPanelExhibitionRate.add(jPanelExhibitionRateMinMax);
		jPanelExhibitionRate.add(jLabelExhibitionRateInfo);

		// Reserva de memoria para el jPanelTimeAnswerMin.
		jPanelTimeAnswerMinMax = new JPanel();
		flowLayoutPanelTimeAnswerMinMax = new FlowLayout(FlowLayout.CENTER);
		jPanelTimeAnswerMinMax.setLayout(flowLayoutPanelTimeAnswerMinMax);
		jPanelTimeAnswerMinMax.setBackground(SystemColor.WHITE);

		jPanelTimeAnswerMinMax.add(jLabelTimeAnswerMin);
		jPanelTimeAnswerMinMax.add(jTextFieldAnswerTimeMin);
		jPanelTimeAnswerMinMax.add(jLabelTimeAnswerMax);
		jPanelTimeAnswerMinMax.add(jTextFieldAnswerTimeMax);

		jPanelTimeAnswer = new JPanel();
		boxLayoutPanelTimeAnswer = new BoxLayout(jPanelTimeAnswer, BoxLayout.Y_AXIS);
		jPanelTimeAnswer.setLayout(boxLayoutPanelTimeAnswer);
		jPanelTimeAnswer.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelTimeAnswer.setBackground(SystemColor.WHITE);

		jPanelTimeAnswer.add(jLabelTimeAnswer);
		jPanelTimeAnswer.add(jPanelTimeAnswerMinMax);
		jPanelTimeAnswer.add(jLabelTimeAnswerInfo);

		// Reserva de memoria para el jPanelSuccessRateMin.
		jPanelSuccessRateMinMax = new JPanel();
		flowLayoutPanelSuccessRateMinMax = new FlowLayout(FlowLayout.CENTER);
		jPanelSuccessRateMinMax.setLayout(flowLayoutPanelSuccessRateMinMax);
		jPanelSuccessRateMinMax.setBackground(SystemColor.WHITE);

		jPanelSuccessRateMinMax.add(jLabelSuccessRateMin);
		jPanelSuccessRateMinMax.add(jTextFieldSuccessRateMin);
		jPanelSuccessRateMinMax.add(jLabelSuccessRateMax);
		jPanelSuccessRateMinMax.add(jTextFieldSuccessRateMax);

		jPanelSuccessRate = new JPanel();
		boxLayoutPanelSuccessRate = new BoxLayout(jPanelSuccessRate, BoxLayout.Y_AXIS);
		jPanelSuccessRate.setLayout(boxLayoutPanelSuccessRate);
		jPanelSuccessRate.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelSuccessRate.setBackground(SystemColor.WHITE);

		jPanelSuccessRate.add(jLabelSuccessRate);
		jPanelSuccessRate.add(jPanelSuccessRateMinMax);
		jPanelSuccessRate.add(jLabelSuccessRateInfo);

		jPanelStatisticParameters = new JPanel();
		flowLayoutPanelStatisticParameters = new FlowLayout(FlowLayout.CENTER);
		jPanelStatisticParameters.setLayout(flowLayoutPanelStatisticParameters);

		jPanelStatisticParameters.add(jPanelExhibitionRate);
		jPanelStatisticParameters.add(jPanelTimeAnswer);
		jPanelStatisticParameters.add(jPanelSuccessRate);

		// Reserva de memoria para el panel que contendr� a los
		// JRadioButtonClassicTest.
		jPanelRadioButtonClassicTest = new JPanel();
		flowLayoutPanelRadioButtonClassicTest = new FlowLayout(FlowLayout.CENTER);
		jPanelRadioButtonClassicTest.setLayout(flowLayoutPanelRadioButtonClassicTest);
		jPanelRadioButtonClassicTest.setBackground(SystemColor.WHITE);

		jRadioButtonClassicTestYes.setAlignmentX(LEFT_ALIGNMENT);
		jRadioButtonClassicTestNo.setAlignmentX(LEFT_ALIGNMENT);
		jRadioButtonClassicTestIndifferent.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes a su JPanel correspondiente.
		jPanelRadioButtonClassicTest.add(jRadioButtonClassicTestYes);
		jPanelRadioButtonClassicTest.add(jRadioButtonClassicTestNo);
		jPanelRadioButtonClassicTest.add(jRadioButtonClassicTestIndifferent);

		// Reserva de memoria para el jPanelClassicTest
		jPanelClassicTest = new JPanel();
		boxLayoutPanelClassicTest = new BoxLayout(jPanelClassicTest, BoxLayout.Y_AXIS);
		jPanelClassicTest.setLayout(boxLayoutPanelClassicTest);
		jPanelClassicTest.setBackground(SystemColor.WHITE);
		jPanelClassicTest.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jLabelClassicTest.setAlignmentX(LEFT_ALIGNMENT);
		jPanelRadioButtonClassicTest.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelClassicTest.
		jPanelClassicTest.add(jLabelClassicTest);
		jPanelClassicTest.add(jPanelRadioButtonClassicTest);

		// Reserva de memoria para el panel que contendr� a los
		// JRadioButtonAdaptiveTest.
		jPanelRadioButtonAdaptiveTest = new JPanel();
		flowLayoutPanelRadioButtonAdaptiveTest = new FlowLayout(FlowLayout.CENTER);
		jPanelRadioButtonAdaptiveTest.setLayout(flowLayoutPanelRadioButtonAdaptiveTest);
		jPanelRadioButtonAdaptiveTest.setBackground(SystemColor.WHITE);

		jRadioButtonAdaptiveTestYes.setAlignmentX(LEFT_ALIGNMENT);
		jRadioButtonAdaptiveTestNo.setAlignmentX(LEFT_ALIGNMENT);
		jRadioButtonAdaptiveTestIndifferent.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes a su JPanel correspondiente.
		jPanelRadioButtonAdaptiveTest.add(jRadioButtonAdaptiveTestYes);
		jPanelRadioButtonAdaptiveTest.add(jRadioButtonAdaptiveTestNo);
		jPanelRadioButtonAdaptiveTest.add(jRadioButtonAdaptiveTestIndifferent);

		// Reserva de memoria para el jPanelAdaptiveTest
		jPanelAdaptiveTest = new JPanel();
		boxLayoutPanelAdaptiveTest = new BoxLayout(jPanelAdaptiveTest, BoxLayout.Y_AXIS);
		jPanelAdaptiveTest.setLayout(boxLayoutPanelAdaptiveTest);
		jPanelAdaptiveTest.setBackground(SystemColor.WHITE);
		jPanelAdaptiveTest.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jLabelAdaptiveTest.setAlignmentX(LEFT_ALIGNMENT);
		jPanelRadioButtonAdaptiveTest.setAlignmentX(LEFT_ALIGNMENT);

		// Adici�n de los componentes del jPanelAdaptiveTest.
		jPanelAdaptiveTest.add(jLabelAdaptiveTest);
		jPanelAdaptiveTest.add(jPanelRadioButtonAdaptiveTest);

		// Reserva de memoria para el JPanel que contendr� al
		// jLabelNumberOfAnswers
		// y al jComboBoxNumberOfAnswers.
		jPanelTest = new JPanel();
		flowLayoutPanelTest = new FlowLayout(FlowLayout.CENTER);
		jPanelTest.setLayout(flowLayoutPanelTest);

		jPanelTest.add(jPanelClassicTest);
		jPanelTest.add(jPanelAdaptiveTest);

		// Reserva de memoria para el jTabbedPane.
		jTabbedPane = new JTabbedPane();
		jTabbedPane.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jTabbedPane.addTab("IRT Parameters", iconInfoGreen16, jPanelIrtParameters);
		jTabbedPane.addTab("Statistical Parameters", iconInfoRed16, jPanelStatisticParameters);
		jTabbedPane.addTab("Test", iconTest16, jPanelTest);

		// Reserva de memoria para el JPanel que contendr� a los JButton.
		jPanelButton = new JPanel();
		flowLayoutPanelButton = new FlowLayout(FlowLayout.CENTER);
		jPanelButton.setLayout(flowLayoutPanelButton);

		jPanelButton.add(jButtonFind);
		jPanelButton.add(jButtonCancel);
		jPanelButton.add(jButtonClear);

		// Reserva de memoriay es establecimiento del layout para el jPanelStatus.
		jPanelStatus = new JPanel();
		flowLayoutPanelStatus = new FlowLayout(FlowLayout.LEFT);
		jPanelStatus.setBorder(BorderFactory.createLoweredBevelBorder());
		jPanelStatus.setLayout(flowLayoutPanelStatus);

		jLabelStatus.setAlignmentX(LEFT_ALIGNMENT);
		jProgressBar.setAlignmentX(RIGHT_ALIGNMENT);

		jPanelStatus.add(jLabelStatus);
		jPanelStatus.add(jProgressBar);

		// Adici�n al jPanelContent de los paneles contenidos en el
		// frame.
		jPanelContent.add(jPanelInstructions);
		jPanelContent.add(Box.createHorizontalStrut(5));
		jPanelContent.add(jPanelCourseQuestionsFile);
		jPanelContent.add(Box.createHorizontalStrut(5));
		// jPanelContent.add(jPanelCodeQuestion);
		// jPanelContent.add(Box.createHorizontalStrut(5));
		jPanelContent.add(jPanelQuestion);
		jPanelContent.add(Box.createHorizontalStrut(5));
		jPanelContent.add(jPanelRadioButtonImage);
		jPanelContent.add(Box.createHorizontalStrut(5));
		jPanelContent.add(jPanelNumberOfAnswers);
		jPanelContent.add(Box.createHorizontalStrut(5));
		// jPanelContent.add(jPanelIrtParameters);
		// jPanelContent.add(jPanelStatisticParameters);
		jPanelContent.add(jTabbedPane);
		jPanelContent.add(Box.createHorizontalStrut(5));
		// jPanelContent.add(jPanelTest);
		// jPanelContent.add(Box.createHorizontalStrut(5));
		jPanelContent.add(jPanelButton);
		jPanelContent.add(jPanelStatus);
	}

	/**
	 * Initializes the window, setting the event listeners and
	 * adjusting the minimun size of it.
	 * Makes visible the frame
	 */
	private void frameInit() throws Exception {
	//	
		// Sets the main content panel
		this.setContentPane(jScrollPane);

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
	 * NOMBRE: jComboBoxCourseActionPerformed. PERTENECE A: Clase
	 * FrmFindQuestions. LLAMADA POR: Al cambiar el valor mostrado por el
	 * jComboBoxCourse. LLAMA A: nada. RECIBE: nada. DEVUELVE: void. FUNCI�N:
	 * Cambia el contenido del jComboBoxQuestionsFile, estableciendo en el mismo
	 * la lista de ficheros de preguntas correspondientes al curso actualmente
	 * mostrado en el jComboBoxCourse.
	 */

	void jComboBoxCourseActionPerformed() {
		// Obtenci�n del nombre del curso actualmente mostrado en jComboBoxCourse.
		String nameCourse = (String) jComboBoxCourse.getSelectedItem();

		// Bucle para calcular la posici�n que ocupa el vector que contiene
		// la lista de ficheros de preguntas del curso mostrado en
		// jComboBoxCourse,
		// en el objeto Vector que contiene los nombres de todos los
		// ficheros de preguntas para cada curso propiedad del usuario.
		Vector questionsFileVector = new Vector();
		int j = 0;
		for (j = 0; j < courseVector.size(); j++) {
			if (nameCourse.equals(((Course) courseVector.get(j)).getName())) {
				questionsFileVector = ((Course) courseVector.get(j)).getQuestionsFileNames();
				break;
			}
		}

		if (questionsFileVector == null)
			questionsFileVector = new Vector();

		try {
			JButtonInit();
			JListInit(questionsFileVector);
			JPanelInit();
			frameInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * NOMBRE: jButtonFindActionPerformed. PERTENECE A: Clase FrmFindQuestions.
	 * LLAMADA POR: Al hacer click sobre el jButtonFind. LLAMA A: nada. RECIBE:
	 * nada. DEVUELVE: void. FUNCI�N: M�todo que comprueba que los datos para la
	 * busqueda de preguntas son correctos. Si lo son, entonces invoca a otro
	 * m�todo de esta misma clase encargado de enviar al servidor los datos
	 * necesarios para que localize y devuelva los datos de las preguntas que
	 * coincidan con los introducidos en los componentes del frame.
	 */

	void jButtonFindActionPerformed() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		Question question = new Question();

		// Lectura de los datos introducidos por el usuario en los componentes del
		// frame.

		// Lectura del curso y fichero de preguntas en el que se va a buscar.
		String courseName = jComboBoxCourse.getSelectedItem().toString().trim();
		Object[] questionsFileNameSelected = jListQuestionsFile.getSelectedValues();

		// Lectura del curso donde se va a buscar.
		question.setCourse(courseName);

		// Lectura del c�digo de la pregunta que se va a buscar.
		String codeQuestion = jTextFieldCodeQuestion.getText();
		if (codeQuestion.equals(""))
			question.setCodeQuestion("NoCode");
		else
			question.setCodeQuestion(codeQuestion);

		// Lectura de la expresi�n que se va a buscar en los enunciados de las
		// preguntas.
		String enunciate = jTextAreaQuestion.getText().trim();
		question.setEnunciate(enunciate);

		// Lectura de si la pregunta a buscar tiene imangen asociada o no, o no se
		// sabe.
		// String questionImage = "";
		if (jRadioButtonImageIndifferent.isSelected())
			question.setExistImage("INDIFFERENT");
		else if (jRadioButtonImageYes.isSelected())
			question.setExistImage("true");
		else if (jRadioButtonImageNo.isSelected())
			question.setExistImage("false");

		// Comprobaci�n de los valores introducidos en los JTextField de los
		// parametros de la pregunta.
		if (jTextFieldDifficultyMin.getText().equals(""))
			question.setDifficultyMin(DIFFICULTY_MIN);
		else {
			try {
				double difficultyDouble = Double.valueOf(jTextFieldDifficultyMin.getText()).doubleValue();
				question.setDifficultyMin(difficultyDouble);
			} catch (java.lang.NumberFormatException nfe) {
				question.setDifficultyMin(DIFFICULTY_MIN);
			}
		}

		if (jTextFieldDifficultyMax.getText().equals(""))
			question.setDifficultyMax(DIFFICULTY_MAX);
		else {
			try {
				double difficultyDouble = Double.valueOf(jTextFieldDifficultyMax.getText()).doubleValue();
				question.setDifficultyMax(difficultyDouble);
			} catch (java.lang.NumberFormatException nfe) {
				question.setDifficultyMax(DIFFICULTY_MAX);
			}
		}

		if (jTextFieldDiscriminationMin.getText().equals(""))
			question.setDiscriminationMin(DISCRIMINATION_MIN);
		else {
			try {
				double discriminationDouble = Double.valueOf(jTextFieldDiscriminationMin.getText()).doubleValue();
				question.setDiscriminationMin(discriminationDouble);
			} catch (java.lang.NumberFormatException nfe) {
				question.setDiscriminationMin(DISCRIMINATION_MIN);
			}
		}

		if (jTextFieldDiscriminationMax.getText().equals(""))
			question.setDiscriminationMax(DISCRIMINATION_MAX);
		else {
			try {
				double discriminationDouble = Double.valueOf(jTextFieldDiscriminationMax.getText()).doubleValue();
				question.setDiscriminationMax(discriminationDouble);
			} catch (java.lang.NumberFormatException nfe) {
				question.setDiscriminationMax(DISCRIMINATION_MAX);
			}
		}

		if (jTextFieldGuessingMin.getText().equals(""))
			question.setGuessingMin(GUESSING_MIN);
		else {
			try {
				double guessingDouble = Double.valueOf(jTextFieldGuessingMin.getText()).doubleValue();
				question.setGuessingMin(guessingDouble);
			} catch (java.lang.NumberFormatException nfe) {
				question.setGuessingMin(GUESSING_MIN);
			}
		}

		if (jTextFieldGuessingMax.getText().equals(""))
			question.setGuessingMax(GUESSING_MAX);
		else {
			try {
				double guessingDouble = Double.valueOf(jTextFieldGuessingMax.getText()).doubleValue();
				question.setGuessingMax(guessingDouble);
			} catch (java.lang.NumberFormatException nfe) {
				question.setGuessingMax(GUESSING_MAX);
			}
		}

		if (jTextFieldExhibitionRateMin.getText().equals(""))
			question.setExhibitionRateMin(EXHIBITION_RATE_MIN);
		else {
			try {
				double exhibitionRateDouble = Double.valueOf(jTextFieldExhibitionRateMin.getText()).doubleValue();
				question.setExhibitionRateMin(exhibitionRateDouble);
			} catch (java.lang.NumberFormatException nfe) {
				question.setExhibitionRateMin(EXHIBITION_RATE_MIN);
			}
		}

		if (jTextFieldExhibitionRateMax.getText().equals(""))
			question.setExhibitionRateMax(EXHIBITION_RATE_MAX);
		else {
			try {
				double exhibitionRateDouble = Double.valueOf(jTextFieldExhibitionRateMax.getText()).doubleValue();
				question.setExhibitionRateMax(exhibitionRateDouble);
			} catch (java.lang.NumberFormatException nfe) {
				question.setExhibitionRateMax(EXHIBITION_RATE_MAX);
			}
		}

		if (jTextFieldAnswerTimeMin.getText().equals(""))
			question.setAnswerTimeMin(ANSWER_TIME_MIN);
		else {
			try {
				int answersTimeInt = Integer.valueOf(jTextFieldAnswerTimeMin.getText()).intValue();
				question.setAnswerTimeMin(answersTimeInt);
			} catch (java.lang.NumberFormatException nfe) {
				question.setAnswerTimeMin(ANSWER_TIME_MIN);
			}
		}

		if (jTextFieldAnswerTimeMax.getText().equals(""))
			question.setAnswerTimeMax(ANSWER_TIME_MAX);
		else {
			try {
				int answersTimeInt = Integer.valueOf(jTextFieldAnswerTimeMax.getText()).intValue();
				question.setAnswerTimeMax(answersTimeInt);
			} catch (java.lang.NumberFormatException nfe) {
				question.setAnswerTimeMax(ANSWER_TIME_MAX);
			}
		}

		if (jTextFieldSuccessRateMin.getText().equals(""))
			question.setSuccessRateMin(SUCCESS_RATE_MAX);
		else {
			try {
				double successRateDouble = Double.valueOf(jTextFieldSuccessRateMin.getText()).doubleValue();
				question.setSuccessRateMin(successRateDouble);
			} catch (java.lang.NumberFormatException nfe) {
				question.setSuccessRateMin(SUCCESS_RATE_MIN);
			}
		}

		if (jTextFieldSuccessRateMax.getText().equals(""))
			question.setSuccessRateMax(SUCCESS_RATE_MAX);
		else {
			try {
				double successRateDouble = Double.valueOf(jTextFieldSuccessRateMax.getText()).doubleValue();
				question.setSuccessRateMax(successRateDouble);
			} catch (java.lang.NumberFormatException nfe) {
				question.setSuccessRateMax(SUCCESS_RATE_MAX);
			}
		}

		// Lectura del n�mero de respuestas que debe de tener las preguntas que se
		// buscan.
		question.setNumberOfAnswersMin(Integer
				.valueOf(jComboBoxNumberOfAnswersMin.getSelectedItem().toString()).intValue());
		question.setNumberOfAnswersMax(Integer
				.valueOf(jComboBoxNumberOfAnswersMax.getSelectedItem().toString()).intValue());

		// Lectura de si las preguntas a buscar deben pertenecer obligatoriamente
		// a un test cl�sico o adaptativo
		if (jRadioButtonClassicTestYes.isSelected())
			question.setClassicTest("true");
		else if (jRadioButtonClassicTestNo.isSelected())
			question.setClassicTest("false");
		else
			question.setClassicTest("INDIFFERENT");

		if (jRadioButtonAdaptiveTestYes.isSelected())
			question.setAdaptiveTest("true");
		else if (jRadioButtonAdaptiveTestNo.isSelected())
			question.setAdaptiveTest("false");
		else
			question.setAdaptiveTest("INDIFFERENT");

		// Llamada al m�todo de esta misma clase que se encarga de enviar al
		// servidor los datos necesarios para localizar las preguntas y
		// devolverlas
		questionsOfMultipleFilesVector = new Vector();

		jProgressBar.setMinimum(0);
		jProgressBar.setMaximum(questionsFileNameSelected.length);
		for (int i = 0; i < questionsFileNameSelected.length; i++) {
			Vector questionsFileVector = new Vector();

			String questionsFileName = questionsFileNameSelected[i].toString().trim();

			// Lectura del nombre del fichero de preguntas donde se va a buscar.
			question.setQuestionsFileName(questionsFileName);

			jLabelStatus.setText("Loading questions of file... " + questionsFileName);
			jProgressBar.setValue(i + 1);
			jPanelStatus.paint(jPanelStatus.getGraphics());
			paint(getGraphics());
			parent.paint(parent.getGraphics());

			Vector questionVector = parent.findQuestions(question);

			if (questionVector != null && questionVector.isEmpty() == false) {
				questionsFileVector.add(questionsFileName);
				questionsFileVector.add(questionVector);

				questionsOfMultipleFilesVector.add(questionsFileVector);
			}
		}

		jLabelStatus.setText("Done                                            "
				+ "                                                ");
		jProgressBar.setValue(jProgressBar.getMinimum());
		jPanelStatus.paint(jPanelStatus.getGraphics());

		// Si no se ha podido obtener los datos de las preguntas, se muestra un
		// mensaje de error.
		if (questionsOfMultipleFilesVector == null || questionsOfMultipleFilesVector.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Questions not found", "Not found",
					JOptionPane.WARNING_MESSAGE);

		} else {
			// Reserva de memoria y mostrado del frame de tipo
			// FrmExamQuestionsFile.
			FrmExamineQuestionsFile frmExamineQuestionsFile = new FrmExamineQuestionsFile(
					"Result of the search of questions.  " + "Course: " + courseName + " - Question File: "
							+ questionsFileName, 620, 400, userLogin, userPassword, courseName, questionsFileName,
					questionsOfMultipleFilesVector, codeBase, wowPath, iconTestEditorPath, parent,
					TestEditor.FIND_QUESTIONS_OF_A_FILE);

			// Adici�n del frame anterior al JFrame principal del applet.
			getParent().add(frmExamineQuestionsFile);

			try {
				frmExamineQuestionsFile.setSelected(true);
			} catch (java.beans.PropertyVetoException eP) {}
		}

		// Cursor estandar.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: closingWindow. PERTENECE A: Clase FrmFindQuestions. LLAMADA POR:
	 * Al hacer click sobre el jButtonCancel. LLAMA A: nada. RECIBE: nada.
	 * DEVUELVE: void. FUNCI�N: Cierra el frame.
	 */

	public int closingWindow() {
		setVisible(false);
		dispose();
		return DISPOSE_ON_CLOSE;
	}

	/*
	 * NOMBRE: jButtonClearActionPerformed. PERTENECE A: Clase FrmQuestionData.
	 * LLAMADA POR: Al hacer click sobre el jButtonClear. LLAMA A: nada. RECIBE:
	 * nada. DEVUELVE: void. FUNCI�N: Vac�a o limpia el contenido de los
	 * componentes del frame.
	 */

	void jButtonClearActionPerformed() {

		// Limpiado de la selecci�n del jListQuestionsFile.
		jListQuestionsFile.clearSelection();

		// Borrado del contenido de jTextFieldCodeQuestion.
		jTextFieldCodeQuestion.setText("");

		// Borrado del contenido de jTextAreaQuestion.
		jTextAreaQuestion.setText("");

		// Deselecci�n de jCheckBoxImage.
		jRadioButtonImageIndifferent.setSelected(true);
		jRadioButtonImageYes.setSelected(false);
		jRadioButtonImageNo.setSelected(false);

		jRadioButtonClassicTestIndifferent.setSelected(true);
		jRadioButtonClassicTestYes.setSelected(false);
		jRadioButtonClassicTestNo.setSelected(false);

		jRadioButtonAdaptiveTestIndifferent.setSelected(true);
		jRadioButtonAdaptiveTestYes.setSelected(false);
		jRadioButtonAdaptiveTestNo.setSelected(false);

		// Establecimiento de los jComboBoxDifficulty y jComboBoxDiscrimination en
		// sus valores por defecto.
		jTextFieldDifficultyMin.setText(String.valueOf(DIFFICULTY_MIN));
		jTextFieldDiscriminationMin.setText(String.valueOf(DISCRIMINATION_MIN));
		jTextFieldGuessingMin.setText(String.valueOf(GUESSING_MIN));

		jTextFieldDifficultyMax.setText(String.valueOf(DIFFICULTY_MAX));
		jTextFieldDiscriminationMax.setText(String.valueOf(DISCRIMINATION_MAX));
		jTextFieldGuessingMax.setText(String.valueOf(GUESSING_MAX));

		jTextFieldExhibitionRateMin.setText(String.valueOf(EXHIBITION_RATE_MIN));
		jTextFieldAnswerTimeMin.setText(String.valueOf(ANSWER_TIME_MIN));
		jTextFieldSuccessRateMin.setText(String.valueOf(SUCCESS_RATE_MIN));

		jTextFieldExhibitionRateMax.setText(String.valueOf(EXHIBITION_RATE_MAX));
		jTextFieldAnswerTimeMax.setText(String.valueOf(ANSWER_TIME_MAX));
		jTextFieldSuccessRateMax.setText(String.valueOf(SUCCESS_RATE_MAX));
	}
}