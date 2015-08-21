package es.uco.WOW.TestEditor;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import es.uco.WOW.Utils.Question;

/**
 * <p>Title: Wow! TestEditor</p> <p>Description: Herramienta para la edicion
 * de preguntas tipo test adaptativas </p> <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/*
 * NOMBRE: FrmEditQuestion. FUNCION: Clae que extiende de frame. Esta
 * clase realiza varias funciones seg�n para la operaci�n que sea invocada. Si
 * el objeto tipo int que se le pasa al constructor es igual a 1 entonces el
 * frame se configurar� para que el usuario de la herramienta
 * TestEditor introduzca los datos para una nueva pregunta, que ser� a�adida al
 * fichero de preguntas cuyo nombre ha sido recibido por el constructor. Si el
 * objeto de tipo int pasado como par�metro al constructor es igual a 2,
 * entonces este frame ser� usado para modificar los datos de alguna
 * pregunta existente en el fichero de preguntas o incluso para eliminarla. LAST
 * MODIFICATION: 06-02-2008
 */

public class FrmEditQuestion extends TestEditorInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The initial width of the window
	 */
	public static final short INITIAL_WIDTH = 800;
	/**
	 * The initial height of the window
	 */
	public static final short INITIAL_HEIGHT = 600;

	
	private int numAnswers; // Almacena el n�mero de respuestas que posee cada

	// pregunta del fichero por defecto.
	private int numAnswersOrigin; // Almacena el n�mero de respuestas que posee

	// cada pregunta del fichero por defecto, de
	// forma permanente, esta variable no modifica
	// su valor.
	private int numQuestionShow; // Almacena el orden que ocupa la pregunta que

	// se est� mostrado en el fichero de preguntas,
	// en caso que esta clase se est� usando para
	// modificar o eliminar preguntas.
	protected String courseName; // Almacena el nombre del curso sobre el que se

	// est�

	// actuando.
	protected String questionsFileName; // Almacena el nombre del fichero de

	// preguntas

	// sobre el que se est� actuando.
	private String concept = ""; // Almacena el nombre del concepto que esta

	// asociado a la pregunta.
	private String userLogin; // Almacena el login del usuario.

	private String userPassword; // Almacena el password del usuario.

	protected URL codeBase; // Almacena la URL del servidor del que se ha descargado

	// Variables sobre los datos de la pregunta que se ha creado o la que se est�
	// modificando.
	protected String codeQuestion; // Almacena el c�digo de la pregunta que se

	// est�

	// modificando.
	private String numberQuestionOfFile; // Almacena el n�mero total de

	// preguntas

	// que contiene el fichero de preguntas.
	private int numberQuestionOfFileInt; // Almacena el n�mero total de

	// preguntas

	// que contiene el fichero de preguntas.
	// private String codeQuestionAux= ""; //Almacena el c�digo de la pregunta
	// que se
	// est� modificando.
	private File imageFile = null; // Almacenar� el fichero de imagen que puede

	// ser

	// asociado a una pregunta, al crearla o
	// modificarla.
	private String pathImage = ""; // Almacena el path de la imagen asociada a

	// la

	// pregunta.

	// Variables sobre el estado del frame y las operaci�nes realizadas
	// en el mismo.
	protected boolean questionSaved = false; // Indica si la pregunta que se ha creando,

	// o

	// las updates realizadas a una pregunta, han
	// sido salvadas.
	protected boolean questionModified = false; // Indica si la pregunta que mostrada

	// ha sido modificada.
	private boolean imageModified = false; // Indica si a la pregunta mostrada se le ha

	// modificado su imagen asociada, es decir,
	// si se ha a�adidio una nueva imagen, o
	// eliminado la que tenia.
	private boolean onClosing = false; // Indica si la inform�cion actual del

	// frame est� siendo modificada por la de
	// otra pregunta o si el frame se est�
	// cerrando.
	private boolean ImageIconLoad = false; // Indica si la carga de los iconos de los

	// JButton de este frame se ha
	// producido con �xito.
	private boolean saveInOtherFile = false; // Indica si se est� realizando la operaci�n

	// de salvar la pregunta en otro fichero
	// de preguntas distinto.
	private boolean copyImage = false;

	protected boolean dataRight = true; // Indica si los datos introducidos para crear una

	// nueva pregunta son correctos.


	protected int type = 0;

	// Variables que almacenas las posibles dificultades y discriminaciones que
	// se
	// dan a elegir para una pregunta.
	private double DIFFICULTY_MAX;

	private double DIFFICULTY_NORMAL;

	private double DIFFICULTY_MIN;

	private double DIFFICULTY_SELECT;

	private double DISCRIMINATION_MAX;

	private double DISCRIMINATION_MIN;

	private double DISCRIMINATION_NORMAL;

	private double DISCRIMINATION_SELECT;

	private double GUESSING_NORMAL;

	private double GUESSING_SELECT;

	private int NUMBER_OF_ANSWERS_MAX;

	private int NUMBER_OF_ANSWERS_MIN;

	private double difficulty;

	private double discrimination;

	private double guessing;

	// Componentes del frame.
	private JTextArea jTextAreaEnunciate;

	private JTextArea[] jTextAreaAnswers;

	private JTextArea[] jTextAreaExplanation;

	private JRadioButton[] jRadioButtonTrue;

	private JRadioButton[] jRadioButtonFalse;

	private ButtonGroup[] jRadioButtonAnswer;

	private JCheckBox[] jCheckBoxDeleteAnswer;

	private JPanel jPanelContent;

	private JPanel jPanelNumberQuestionOfFile;

	private JPanel jPanelNavigation;

	private JPanel jPanelGotoQuestion;

	private JPanel jPanelNavigationButton;

	private JPanel jPanelNumberQuestion;

	private JPanel jPanelEnunciate;

	private JPanel jPanelImage;

	private JPanel jPanelImageContent;

	private JPanel jPanelImageButton;

	private JPanel jPanelIrtParameters;

	private JPanel jPanelStatisticalParameters;

	private JPanel jPanelTest;

	private JPanel jPanelClassicTest;

	private JPanel jPanelAdaptiveTest;

	private JPanel jPanelDifficulty;

	private JPanel jPanelDiscrimination;

	private JPanel jPanelGuessing;

	private JPanel jPanelExhibitionRate;

	private JPanel jPanelAnswerTime;

	private JPanel jPanelSuccessRate;

	private JPanel jPanelAnswer;

	private JPanel[] jPanelAnswersRadioButton;

	private JPanel[] jPanelAnswers;

	private JPanel[] panelLabelAnswer;

	private JPanel[] panelLabelExplanation;

	private JPanel jPanelButtonAdd;

	private JPanel jPanelButtonModify;

	private JScrollPane jScrollPane;

	private JScrollPane jScrollPaneEnunciate;

	private JScrollPane[] jScrollPaneAnswers;

	private JScrollPane[] jScrollPaneExplanation;

	private JScrollPane jScrollPaneImage;

	private JTabbedPane jTabbedPane;

	private JLabel jLabelNumberQuestionOfFile;

	private JLabel jLabelGotoQuestion;

	private JLabel jLabelNumberQuestion;

	private JLabel jLabelCodeQuestion;

	private JLabel jLabelImage;

	private JLabel[] jLabelAnswer;

	private JLabel[] jLabelExplanation;

	private JLabel jLabelDifficulty;

	private JLabel jLabelDiscrimination;

	private JLabel jLabelGuessing;

	private JLabel jLabelExhibitionRate;

	private JLabel jLabelAnswerTime;

	private JLabel jLabelSuccessRate;

	private JLabel jLabelClassicTest;

	private JLabel jLabelAdaptiveTest;

	private JComboBox jComboBoxNavigation;

	private int indexComboNavigation = 0;

	protected JComboBox jComboBoxClassicTest;

	private JComboBox jComboBoxAdaptiveTest;

	private JButton jButtonGo;

	private JButton jButtonPrevious;

	private JButton jButtonNext;

	private JButton jButtonAddImage;

	private JButton jButtonDeleteImage;

	private JButton jButtonIrtHelp;

	private JButton jButtonSave;

	private JButton jButtonCancel;

	private JButton jButtonClear;

	private JButton jButtonAddAnswer;

	private JButton jButtonDelete;

	private JButton jButtonSaveInOtherFile;

	private JButton jButtonAddQuestionToClassicTest;

	private JButton jButtonAddQuestionToAdaptiveTest;

	private JButton jButtonSetIrtParameters;

	private ImageIcon questionImageIcon = null;

	private ImageIcon questionImageIconTemp = null;

	private Image questionImage = null;

	private JFrame jFrameQuestionImage = null;

	private JTextField jTextFieldNumberOfQuestion;

	private JTextField jTextFieldCodeQuestion;

	private JTextField jTextFieldNumberQuestionOfFile;

	private JTextField jTextFieldGotoQuestion;

	private JTextField jTextFieldDifficulty;

	private JTextField jTextFieldDiscrimination;

	private JTextField jTextFieldGuessing;

	private JTextField jTextFieldExhibitionRate;

	private JTextField jTextFieldAnswerTime;

	private JTextField jTextFieldSuccessRate;

	private BoxLayout boxLayoutPanelContent;

	private BoxLayout boxLayoutPanelEnunciate;

	private BoxLayout boxLayoutPanelImage;

	private BoxLayout boxLayoutPanelImageContent;

	private BoxLayout boxLayoutPanelAnswer;

	private BoxLayout[] boxLayoutPanelAnswers;

	private FlowLayout[] flowLayoutPanelAnswersRadioButton;

	private FlowLayout flowLayoutPanelButtonAdd;

	private FlowLayout flowLayoutPanelButtonModify;

	private FlowLayout[] flowLayoutPanelLabelAnswer;

	private FlowLayout[] flowLayoutPanelJlabelExplanation;

	private FlowLayout flowLayoutPanelIrtParameters;

	private FlowLayout flowLayoutPanelStatisticalParameters;

	private FlowLayout flowLayoutPanelTest;

	private FlowLayout flowLayoutPanelClassicTest;

	private FlowLayout flowLayoutPanelAdaptiveTest;

	private FlowLayout flowLayoutPanelNavigationButton;

	private FlowLayout flowLayoutPanelImageButton;

	private FlowLayout flowLayoutPanelNumberQuestionOfFile;

	private FlowLayout flowLayoutPanelGotoQuestion;

	private FlowLayout flowLayoutPanelNumberQuestion;

	private ImageIcon iconAddImage;

	private ImageIcon iconDeleteImage;

	private ImageIcon iconDelete;

	private ImageIcon iconGo;

	private ImageIcon iconPrevious;

	private ImageIcon iconNext;

	private ImageIcon iconSave;

	private ImageIcon iconCancel;

	private ImageIcon iconClear;

	private ImageIcon iconAddAnswer;

	private ImageIcon iconAddQuestionToClassicTest;

	private ImageIcon iconAddQuestionToAdaptiveTest;

	private ImageIcon iconInfoGreen32;

	private ImageIcon iconHelp32;

	private ImageIcon iconInfoGreen16;

	private ImageIcon iconInfoRed16;

	private ImageIcon iconTest16;

	// jTextAreaExplanation.
	private boolean existImage = false; // Indica si existe una imagen asociada a la

	private DlgIrtParameters dlgIrtParameters = null;

	/*
	 * NOMBRE: FrmEditQuestion (Constructor de esta misma clase). PERTENECE A:
	 * Clase FrmEditQuestion. LLAMADA POR: Al invocar la clase. LLAMA A:
	 * JLabelInit(); JTextFieldInit(); JImageIconInit(); JComboBoxInit();
	 * JPanelInit(); JButtonAddAndDeleteImageInit(); JRadioButtonInit();
	 * JCheckBoxInit(); ButtonGroupInit(); JTextAreaInit(); JButtonInit();
	 * setQuestionData(question, codeBase); );
	 * getQuestionData(); RECIBE: title: Objeto de tipo String. Indica el t�tulo
	 * del frame. numAnswersString: Objeto de tipo String. Indica el
	 * n�mero de respuetas por defecto que tienen las preguntas del fichero que
	 * se va a examinar. width: Objeto de tipo int. Indica la anchura inicial del
	 * frame. height: Objeto de tipo int. Indica la altura inicial del
	 * frame. userLogin: Objeto de tipo String. Indica el login del
	 * usuario. userPassword: Objeto de tipo String. Indica el password del
	 * usuario. courseName: Objeto de tipo String. Indica el nombre del curso
	 * sobre el que se est� actuando. questionsFileName: Objeto de tipo String.
	 * Contiene el nombre del fichero de preguntas que se va a examinar.
	 * codeBase: Objeto de tipo URL. Contiene la URL del servidor desde donde se
	 * ha cargado el applet. wowPath: Objeto de tipo String. Contiene el path del
	 * sistema desde el que se descarg� el applet. iconTestEditorPath: Objeto de
	 * tipo String. Contiene el path del directorio que contiene los iconos
	 * usados por la interfaz gr�fica.  Objeto de tipo JFrame. Contiene el
	 * JFrame principal del applet y padre de este frame. type: Objeto
	 * de tipo int. Indica que tipo de aspecto y funcionalidad tendr� el
	 * frame, es decir, si se utilizar� para a�adir nuevas preguntas a
	 * un fichero de preguntas o para modificar o eliminar alguna ya existente en
	 * el fichero. DEVUELVE: nada. FUNCI�N: Constructor de la clase
	 * FrmEditQuestion. Se encarga de configurar el aspecto del frame,
	 * su funcionalidad, as� como de inicializar sus componentes.
	 */

	public FrmEditQuestion(String aTitle, int aNumAnswers, String aUserLogin,
			String aUserPassword, String aCourseName, String aQuestionsFileName, URL aCodeBase,
			String anWOWPath, String iconPath, TestEditor father, int aType) {
	//
		// Calls the parent constructor
		super(aTitle, INITIAL_WIDTH, INITIAL_HEIGHT, father, anWOWPath, iconPath);
		
		// Sets user variables
		userLogin = aUserLogin;
		userPassword = aUserPassword;
		courseName = aCourseName;
		questionsFileName = aQuestionsFileName;
		codeBase = aCodeBase;
		numAnswers = aNumAnswers;
		numAnswersOrigin = aNumAnswers;
		type = aType;
		
		// Inicializaci�n de variables internas.
		DIFFICULTY_MAX = father.DIFFICULTY_MAX;
		DIFFICULTY_NORMAL = father.DIFFICULTY_NORMAL;
		father.DIFFICULTY_SELECT = father.DIFFICULTY_NORMAL;
		DIFFICULTY_MIN = father.DIFFICULTY_MIN;
		DISCRIMINATION_MAX = father.DISCRIMINATION_MAX;
		DISCRIMINATION_MIN = father.DISCRIMINATION_MIN;
		DISCRIMINATION_NORMAL = father.DISCRIMINATION_NORMAL;
		father.DISCRIMINATION_SELECT = father.DISCRIMINATION_NORMAL;
		NUMBER_OF_ANSWERS_MAX = TestEditor.NUMBER_OF_ANSWERS_MAX;
		NUMBER_OF_ANSWERS_MIN = TestEditor.NUMBER_OF_ANSWERS_MIN;
		difficulty = DIFFICULTY_NORMAL;
		discrimination = DISCRIMINATION_NORMAL;
		guessing = 1.0 / Double.valueOf(String.valueOf(numAnswers)).doubleValue();

		try {
			// Si el frame se ha invocado para modificar preguntas,
			// entonces
			// se cargan los datos de la primera pregunta del fichero antes de
			// configurar los componente del frame.
			if (type == TestEditor.MODIFY_QUESTION_OF_A_FILE) {
				// Primera pregunta para solicitar sus datos.
				numQuestionShow = 0;

				// Solicitud de los datos de la primera pregunta al servidor.
				Question question = father.getQuestionByOrder(courseName, questionsFileName, numQuestionShow);

				// Si la respuesta del servidor es null se muestra un mensaje de
				// error.
				if (question == null) {
					JOptionPane.showMessageDialog(this, "ERROR: Error reading course or question file. "
							+ "\n" + "It`s posible that the file doesn't have questions to modify.",
							"Error modifying question", JOptionPane.ERROR_MESSAGE);

					// Ocultaci�n y cerrado del frame.
					setVisible(false);
					setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					doDefaultCloseAction();
				} else {
					// Modificamos el n�mero de respuestas asignandole el
					// correspondiente
					// a la pregunta devuelta por el servlet.
					numAnswers = question.getNumberOfAnswers();

					// Configuraci�n de los componentes del frame.
					JLabelInit();
					JLabelImageInit();
					JTextFieldInit();
					JImageIconInit();
					JComboBoxInit();
					JButtonAddAndDeleteImageInit();
					JButtonInit();
					JRadioButtonInit();
					ButtonGroupInit();
					JCheckBoxInit();
					JTextAreaInit();

					// Establecimiento de los datos de la pregunta, devueltos por el
					// servidor, en los componentes ya inicializados del
					// frame.
					setQuestionData(question, codeBase);

					JPanelInit();
					frameInit();
				}
			} else {
				// Configuraci�n de los componentes del frame.
				JLabelInit();
				JLabelImageInit();
				JTextFieldInit();
				JImageIconInit();
				JButtonAddAndDeleteImageInit();
				JButtonInit();
				JRadioButtonInit();
				ButtonGroupInit();
				JCheckBoxInit();
				JTextAreaInit();
				JPanelInit();
				frameInit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	// //////// INICIALIZACI�N DE LOS COMPONENTES DEL JINTERNALFRAME
	// ////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: JLabelInit. PERTENECE A: Clase FrmEditQuestion. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JLabel del frame,
	 * reservando memoria para los mismos y estableciendo su color, su
	 * contenido...
	 */

	private void JLabelInit() {
		// Si el frame se usar� para modificar o borrar preguntas,
		// entonces
		// se inicializan m�s JLabel, ya que est� tipo de frame muestra
		// m�s
		// informaci�n al usuario.
		if (type == TestEditor.MODIFY_QUESTION_OF_A_FILE) {
			// Reserva de memoria y establecimiento del contenido para los JLabel
			// especificos del uso del frame para modificar o borrar
			// preguntas
			jLabelNumberQuestionOfFile = new JLabel("Number of questions in the file: ");
			jLabelNumberQuestionOfFile.setFont(new java.awt.Font("Dialog", 1, 16));

			jLabelGotoQuestion = new JLabel("Go to question: ");
			jLabelGotoQuestion.setFont(new java.awt.Font("Dialog", 1, 16));

			jLabelNumberQuestion = new JLabel("Question ");
			jLabelNumberQuestion.setFont(new java.awt.Font("Dialog", 1, 20));

			jLabelCodeQuestion = new JLabel("Code ");
			jLabelCodeQuestion.setFont(new java.awt.Font("Dialog", 1, 20));

			jLabelExhibitionRate = new JLabel("Exhibition rate");
			jLabelExhibitionRate.setFont(new java.awt.Font("Dialog", 1, 12));

			jLabelAnswerTime = new JLabel("Half time of answer");
			jLabelAnswerTime.setFont(new java.awt.Font("Dialog", 1, 12));

			jLabelSuccessRate = new JLabel("Success Rate");
			jLabelSuccessRate.setFont(new java.awt.Font("Dialog", 1, 12));

			jLabelClassicTest = new JLabel("Classic Test");
			jLabelClassicTest.setFont(new java.awt.Font("Dialog", 1, 12));

			jLabelAdaptiveTest = new JLabel("Adaptive Test");
			jLabelAdaptiveTest.setFont(new java.awt.Font("Dialog", 1, 12));
		}

		// Reserva de memoria para los JLabel que siempre aparecen en el
		// frame, independientemente del uso para el que se haya
		// invocado.
		jLabelAnswer = new JLabel[numAnswers];
		jLabelExplanation = new JLabel[numAnswers];

		for (int i = 0; i < numAnswers; i++) {
			jLabelAnswer[i] = new JLabel("Answer (Obligatory): ");
			jLabelAnswer[i].setFont(new java.awt.Font("Dialog", 1, 12));

			jLabelExplanation[i] = new JLabel("Explanation (Optional): ");
			jLabelExplanation[i].setFont(new java.awt.Font("Dialog", 1, 12));
		}

		jLabelDifficulty = new JLabel("Difficulty");
		jLabelDifficulty.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelDiscrimination = new JLabel("Discrimination");
		jLabelDiscrimination.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelGuessing = new JLabel("Guessing");
		jLabelGuessing.setFont(new java.awt.Font("Dialog", 1, 12));

	}

	/*
	 * NOMBRE: JLabelImageInit. PERTENECE A: Clase FrmEditQuestion. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa el jLabelImage del
	 * frame, reservando memoria para el mismo y estableciendo su color,
	 * su contenido...
	 */

	private void JLabelImageInit() {
		jLabelImage = new JLabel();
		jLabelImage.setSize(500, 375);

		if (questionImageIcon != null) {
			jLabelImage.setFocusable(true);
			jLabelImage.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			jLabelImage.setToolTipText("Make double click on the image to see it in the original size.");

			jLabelImage.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						// Oculatado del posible JFrame abierto anteriormente.
						if (jFrameQuestionImage != null) {
							jFrameQuestionImage.setVisible(false);
							jFrameQuestionImage.dispose();
							jFrameQuestionImage = null;
						}

						// Reserva de memoria para el JLabel que contendr� la imagen
						// a mostrar en el JFrame.
						JLabel jLabelImageFrameQuestionImage = new JLabel(questionImageIconTemp);

						// Establecimiento del aspecto del JFrame a mostrar.
						jFrameQuestionImage = new JFrame("Image of the question.");
						jFrameQuestionImage.setSize(640, 440);
						jFrameQuestionImage.setLocation(0, 0);
						jFrameQuestionImage.setResizable(true);
						jFrameQuestionImage.setVisible(true);
						JScrollPane jScrollPaneImage = new JScrollPane(jLabelImageFrameQuestionImage,
								JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

						jFrameQuestionImage.getContentPane().add(jScrollPaneImage);

						jFrameQuestionImage.pack();
					}
				}
			});
		}
	}

	/*
	 * NOMBRE: JTextFieldInit. PERTENECE A: Clase FrmEditQuestion. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa los JTextField del
	 * frame, reservando memoria para los mismos y estableciendo su
	 * tama�o, su contenido, funcionalidad...
	 */

	private void JTextFieldInit() {
		guessing = 1.0 / Double.valueOf(String.valueOf(numAnswers)).doubleValue();
		GUESSING_NORMAL = guessing;
		GUESSING_SELECT = guessing;
		parent.GUESSING_NORMAL = GUESSING_NORMAL;
		parent.GUESSING_SELECT = GUESSING_SELECT;

		if (type == TestEditor.MODIFY_QUESTION_OF_A_FILE) {
			// Reserva de memoria y establecimiento de la funcionalidad.
			jTextFieldNumberOfQuestion = new JTextField(4);
			jTextFieldNumberOfQuestion.setFont(new java.awt.Font("Dialog", 1, 14));
			jTextFieldNumberOfQuestion.setFocusable(false);

			jTextFieldCodeQuestion = new JTextField(4);
			jTextFieldCodeQuestion.setFont(new java.awt.Font("Dialog", 1, 14));
			jTextFieldCodeQuestion.setFocusable(false);

			jTextFieldDifficulty = new JTextField(8);
			jTextFieldDifficulty.setText(String.valueOf(DIFFICULTY_NORMAL));
			jTextFieldDifficulty.setFont(new java.awt.Font("Dialog", 1, 12));

			jTextFieldDiscrimination = new JTextField(8);
			jTextFieldDiscrimination.setText(String.valueOf(DISCRIMINATION_NORMAL));
			jTextFieldDiscrimination.setFont(new java.awt.Font("Dialog", 1, 12));

			jTextFieldGuessing = new JTextField(8);
			jTextFieldGuessing.setText(String.valueOf(guessing));
			jTextFieldGuessing.setFocusable(false);
			jTextFieldGuessing.setFont(new java.awt.Font("Dialog", 1, 12));

			jTextFieldNumberQuestionOfFile = new JTextField(4);
			jTextFieldNumberQuestionOfFile.setFocusable(false);
			jTextFieldNumberQuestionOfFile.setFont(new java.awt.Font("Dialog", 1, 14));

			jTextFieldGotoQuestion = new JTextField(4);
			jTextFieldGotoQuestion.setFont(new java.awt.Font("Dialog", 1, 14));

			jTextFieldExhibitionRate = new JTextField(8);
			jTextFieldExhibitionRate.setFocusable(false);
			jTextFieldExhibitionRate.setFont(new java.awt.Font("Dialog", 1, 12));

			jTextFieldAnswerTime = new JTextField(8);
			jTextFieldAnswerTime.setFocusable(false);
			jTextFieldAnswerTime.setFont(new java.awt.Font("Dialog", 1, 12));

			jTextFieldSuccessRate = new JTextField(8);
			jTextFieldSuccessRate.setFocusable(false);
			jTextFieldSuccessRate.setFont(new java.awt.Font("Dialog", 1, 12));
		}
	}

	/*
	 * NOMBRE: JImageIconInit. PERTENECE A: Clase FrmEditQuestion. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa los ImageIcon del
	 * frame, reservando memoria para los mismos y estableciendo su
	 * tama�o, su contenido, funcionalidad... Los iconos se encuentran en el
	 * servidor, con lo que ser� solicitado el envio de los mismos desde el
	 * servidor al applet descargado.
	 */

	private void JImageIconInit() {
		// Construcci�n del String de solicitud de los iconos al servidor.
		String petitionGo = wowPath + iconTestEditorPath + "go32.gif";
		String petitionPrevious = wowPath + iconTestEditorPath + "previous32.gif";
		String petitionNext = wowPath + iconTestEditorPath + "next32.gif";
		String petitionAddImage = wowPath + iconTestEditorPath + "addImage32.gif";
		String petitionDeleteImage = wowPath + iconTestEditorPath + "deleteImage32.gif";
		String petitionDelete = wowPath + iconTestEditorPath + "deleteFile32.gif";
		String petitionSave = wowPath + iconTestEditorPath + "save32.gif";
		String petitionCancel = wowPath + iconTestEditorPath + "cancel32.gif";
		String petitionClear = wowPath + iconTestEditorPath + "clear32.gif";
		String petitionAddAnswer = wowPath + iconTestEditorPath + "addAnswer32.gif";
		String petitionAddQuestionToClassicTest = wowPath + iconTestEditorPath
				+ "addQuestionToClassicTest32.gif";
		String petitionAddQuestionToAdaptiveTest = wowPath + iconTestEditorPath
				+ "addQuestionToAdaptiveTest32.gif";
		String petitionInfoGreen32 = wowPath + iconTestEditorPath + "info_green_32.gif";
		String petitionHelp32 = wowPath + iconTestEditorPath + "help32.gif";
		String petitionInfoGreen16 = wowPath + iconTestEditorPath + "info_green_16.gif";
		String petitionInfoRed16 = wowPath + iconTestEditorPath + "info_red_16.gif";
		String petitionTest16 = wowPath + iconTestEditorPath + "testFiles16.gif";

		// Conexi�n con el servidor.
		try {
			URL urlGo = new URL(codeBase, petitionGo);
			URL urlPrevious = new URL(codeBase, petitionPrevious);
			URL urlNext = new URL(codeBase, petitionNext);
			URL urlAddQImage = new URL(codeBase, petitionAddImage);
			URL urlDeleteImage = new URL(codeBase, petitionDeleteImage);
			URL urlDelete = new URL(codeBase, petitionDelete);
			URL urlSave = new URL(codeBase, petitionSave);
			URL urlCancel = new URL(codeBase, petitionCancel);
			URL urlClear = new URL(codeBase, petitionClear);
			URL urlAddAnswer = new URL(codeBase, petitionAddAnswer);
			URL urlAddQuestionToClassicTest = new URL(codeBase, petitionAddQuestionToClassicTest);
			URL urlAddQuestionToAdaptiveTest = new URL(codeBase, petitionAddQuestionToAdaptiveTest);
			URL urlInfoGreen32 = new URL(codeBase, petitionInfoGreen32);
			URL urlHelp32 = new URL(codeBase, petitionHelp32);
			URL urlInfoGreen16 = new URL(codeBase, petitionInfoGreen16);
			URL urlInfoRed16 = new URL(codeBase, petitionInfoRed16);
			URL urlTest16 = new URL(codeBase, petitionTest16);

			// Reserva de memoria para los ImageIcon con los iconos obtenidos.
			iconGo = new ImageIcon(urlGo);
			iconPrevious = new ImageIcon(urlPrevious);
			iconNext = new ImageIcon(urlNext);
			iconAddImage = new ImageIcon(urlAddQImage);
			iconDeleteImage = new ImageIcon(urlDeleteImage);
			iconDelete = new ImageIcon(urlDelete);
			iconSave = new ImageIcon(urlSave);
			iconCancel = new ImageIcon(urlCancel);
			iconClear = new ImageIcon(urlClear);
			iconAddAnswer = new ImageIcon(urlAddAnswer);
			iconAddQuestionToClassicTest = new ImageIcon(urlAddQuestionToClassicTest);
			iconAddQuestionToAdaptiveTest = new ImageIcon(urlAddQuestionToAdaptiveTest);
			iconInfoGreen32 = new ImageIcon(urlInfoGreen32);
			iconHelp32 = new ImageIcon(urlHelp32);
			iconInfoGreen16 = new ImageIcon(urlInfoGreen16);
			iconInfoRed16 = new ImageIcon(urlInfoRed16);
			iconTest16 = new ImageIcon(urlTest16);

			ImageIconLoad = true;
		} catch (java.net.MalformedURLException e) {
			ImageIconLoad = false;
		}
	}

	/*
	 * NOMBRE: JPanelInit. PERTENECE A: Clase FrmEditQuestion. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JPanel del frame,
	 * reservando memoria para el mismo, estableciendo su color, layout...
	 */

	private void JPanelInit() {
		// Reserva de memoria para el panel principal del frame y
		// establecimiento de su layout.
		jPanelContent = new JPanel();
		boxLayoutPanelContent = new BoxLayout(jPanelContent, BoxLayout.Y_AXIS);
		jPanelContent.setLayout(boxLayoutPanelContent);

		// Reserva de memoria para el jScrollPane, el JScrollPane principal del
		// frame.
		jScrollPane = new JScrollPane(jPanelContent,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Si el frame ha sido invocado para realizar operaciones de
		// update o borrado de preguntas, entonces se inicializan JPanel
		// adicionales, ya que en este caso se muestra m�s informaci�n al usuario.
		if (type == TestEditor.MODIFY_QUESTION_OF_A_FILE) {
			// Reserva de memoria y establecimiento del layout del JPanel que
			// contiene a jLabelGotoQuestion y a jTextFieldGotoQuestion.
			jPanelGotoQuestion = new JPanel();
			flowLayoutPanelGotoQuestion = new FlowLayout(FlowLayout.LEFT);
			jPanelGotoQuestion.setLayout(flowLayoutPanelGotoQuestion);
			jPanelGotoQuestion.add(jLabelGotoQuestion);
			jPanelGotoQuestion.add(jComboBoxNavigation);
			jPanelGotoQuestion.add(jTextFieldGotoQuestion);
			jPanelGotoQuestion.add(jButtonGo);

			// Reserva de memoria y establecimiento del layout del JPanel que
			// contiene a jButtonPrevious y jButtonNext.
			jPanelNavigationButton = new JPanel();
			flowLayoutPanelNavigationButton = new FlowLayout(FlowLayout.CENTER);
			jPanelNavigationButton.setLayout(flowLayoutPanelNavigationButton);

			// Adicion de los JButton anteriores a su JPanel correspondiente.
			jPanelNavigationButton.add(jButtonPrevious);
			jPanelNavigationButton.add(jButtonNext);
			jPanelNavigation = new JPanel();
			jPanelNavigation.add(jPanelNavigationButton);
			jPanelNavigation.add(jPanelGotoQuestion);

			// Reserva de memoria y establecimiento del layout del JPanel que
			// contiene a jLabelNumberQuestionOfFile y a
			// jTextFieldNumberQuestionOfFile.
			jPanelNumberQuestionOfFile = new JPanel();
			flowLayoutPanelNumberQuestionOfFile = new FlowLayout(FlowLayout.CENTER);
			jPanelNumberQuestionOfFile.setLayout(flowLayoutPanelNumberQuestionOfFile);

			jPanelNumberQuestionOfFile.add(jLabelNumberQuestionOfFile);
			jPanelNumberQuestionOfFile.add(jTextFieldNumberQuestionOfFile);

			// Reserva de memoria y establecimiento del layout del JPanel que
			// contiene a jLabelNumberQuestion.
			jPanelNumberQuestion = new JPanel();
			flowLayoutPanelNumberQuestion = new FlowLayout(FlowLayout.CENTER);
			jPanelNumberQuestion.setLayout(flowLayoutPanelNumberQuestion);

			jPanelNumberQuestion.add(jLabelNumberQuestion);
			jPanelNumberQuestion.add(jTextFieldNumberOfQuestion);
			jPanelNumberQuestion.add(Box.createHorizontalStrut(10));
			jPanelNumberQuestion.add(jLabelCodeQuestion);
			jPanelNumberQuestion.add(jTextFieldCodeQuestion);
			jPanelNumberQuestion.add(jPanelNumberQuestionOfFile);

			jPanelDifficulty = new JPanel();
			jPanelDifficulty.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			jPanelDifficulty.setBackground(SystemColor.WHITE);

			jPanelDifficulty.add(jLabelDifficulty);
			jPanelDifficulty.add(jTextFieldDifficulty);

			jPanelDiscrimination = new JPanel();
			jPanelDiscrimination.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			jPanelDiscrimination.setBackground(SystemColor.WHITE);

			jPanelDiscrimination.add(jLabelDiscrimination);
			jPanelDiscrimination.add(jTextFieldDiscrimination);

			jPanelGuessing = new JPanel();
			jPanelGuessing.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			jPanelGuessing.setBackground(SystemColor.WHITE);

			jPanelGuessing.add(jLabelGuessing);
			jPanelGuessing.add(jTextFieldGuessing);

			jPanelIrtParameters = new JPanel();
			flowLayoutPanelIrtParameters = new FlowLayout(FlowLayout.CENTER);
			jPanelIrtParameters.setLayout(flowLayoutPanelIrtParameters);

			jPanelIrtParameters.add(jPanelDifficulty);
			jPanelIrtParameters.add(jPanelDiscrimination);
			// jPanelIrtParameters.add(jPanelGuessing);
			jPanelIrtParameters.add(Box.createHorizontalStrut(20));
			jPanelIrtParameters.add(jButtonIrtHelp);

			jPanelExhibitionRate = new JPanel();
			jPanelExhibitionRate.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			jPanelExhibitionRate.setBackground(SystemColor.WHITE);

			jPanelExhibitionRate.add(jLabelExhibitionRate);
			jPanelExhibitionRate.add(jTextFieldExhibitionRate);

			jPanelAnswerTime = new JPanel();
			jPanelAnswerTime.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			jPanelAnswerTime.setBackground(SystemColor.WHITE);

			jPanelAnswerTime.add(jLabelAnswerTime);
			jPanelAnswerTime.add(jTextFieldAnswerTime);

			jPanelSuccessRate = new JPanel();
			jPanelSuccessRate.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			jPanelSuccessRate.setBackground(SystemColor.WHITE);

			jPanelSuccessRate.add(jLabelSuccessRate);
			jPanelSuccessRate.add(jTextFieldSuccessRate);

			jPanelStatisticalParameters = new JPanel();
			flowLayoutPanelStatisticalParameters = new FlowLayout(FlowLayout.CENTER);
			jPanelStatisticalParameters.setLayout(flowLayoutPanelStatisticalParameters);

			jPanelStatisticalParameters.add(jPanelExhibitionRate);
			jPanelStatisticalParameters.add(jPanelAnswerTime);
			jPanelStatisticalParameters.add(jPanelSuccessRate);

			// Reserva de memoria para el panel que contendr� a los componentes que
			// informan de los ficheros de test cl�sicos a los que pertenece
			// la pregunta.
			jPanelClassicTest = new JPanel();
			flowLayoutPanelClassicTest = new FlowLayout(FlowLayout.CENTER);
			jPanelClassicTest.setLayout(flowLayoutPanelClassicTest);
			jPanelClassicTest.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			jPanelClassicTest.setBackground(SystemColor.WHITE);

			jLabelClassicTest.setAlignmentX(LEFT_ALIGNMENT);

			jPanelClassicTest.add(jLabelClassicTest);
			jPanelClassicTest.add(jComboBoxClassicTest);

			// Reserva de memoria para el panel que contendr� a los componentes que
			// informan de los ficheros de test adaptativos a los que pertenece
			// la pregunta.
			jPanelAdaptiveTest = new JPanel();
			flowLayoutPanelAdaptiveTest = new FlowLayout(FlowLayout.CENTER);
			jPanelAdaptiveTest.setLayout(flowLayoutPanelAdaptiveTest);
			jPanelAdaptiveTest.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			jPanelAdaptiveTest.setBackground(SystemColor.WHITE);

			jLabelAdaptiveTest.setAlignmentX(LEFT_ALIGNMENT);

			jPanelAdaptiveTest.add(jLabelAdaptiveTest);
			jPanelAdaptiveTest.add(jComboBoxAdaptiveTest);

			// Reserva de memoria para el JPanel que contendr� a los dos
			// anteriores.
			jPanelTest = new JPanel();
			flowLayoutPanelTest = new FlowLayout(FlowLayout.CENTER);
			jPanelTest.setLayout(flowLayoutPanelTest);

			jPanelTest.add(jPanelClassicTest);
			jPanelTest.add(jPanelAdaptiveTest);

			// Reserva de memoria para el jTabbedPane.
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

			jTabbedPane.addTab("IRT Parameters", iconInfoGreen16, jPanelIrtParameters);
			jTabbedPane.addTab("Statistical Parameters", iconInfoRed16, jPanelStatisticalParameters);
			jTabbedPane.addTab("Test", iconTest16, jPanelTest);

			// Reserva de memoria del el JPanel que contendra a los JButton.
			jPanelButtonModify = new JPanel();
			flowLayoutPanelButtonModify = new FlowLayout(FlowLayout.CENTER);
			jPanelButtonModify.setLayout(flowLayoutPanelButtonModify);

			// Adicion de los JButton anteriores a su JPanel correspondiente.
			jPanelButtonModify.add(jButtonDelete);
			jPanelButtonModify.add(jButtonSaveInOtherFile);
			jPanelButtonModify.add(jButtonAddQuestionToClassicTest);
			jPanelButtonModify.add(jButtonAddQuestionToAdaptiveTest);
		}

		// Reserva de memoria para los paneles que contendr�n los componentes
		// del JInteralFrame, independientemente de que haya sido invocado para
		// a�adir preguntas o para modificar o borrar.
		jPanelEnunciate = new JPanel();
		boxLayoutPanelEnunciate = new BoxLayout(jPanelEnunciate, BoxLayout.Y_AXIS);
		jPanelEnunciate.setLayout(boxLayoutPanelEnunciate);
		jScrollPaneEnunciate = new JScrollPane(jTextAreaEnunciate,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPaneEnunciate.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelEnunciate.add(jScrollPaneEnunciate);

		jPanelImage = new JPanel();
		boxLayoutPanelImage = new BoxLayout(jPanelImage, BoxLayout.Y_AXIS);
		jPanelImage.setLayout(boxLayoutPanelImage);

		jPanelImageButton = new JPanel();
		flowLayoutPanelImageButton = new FlowLayout(FlowLayout.CENTER);
		jPanelImageButton.setLayout(flowLayoutPanelImageButton);

		// Adici�n de los JButton a su JPanel correspondiente.
		jPanelImageButton.add(jButtonAddImage);
		jPanelImageButton.add(jButtonDeleteImage);

		// Reserva de memoria para el jPanelImageContent.
		jPanelImageContent = new JPanel();
		boxLayoutPanelImageContent = new BoxLayout(jPanelImageContent, BoxLayout.Y_AXIS);
		jPanelImageContent.setLayout(boxLayoutPanelImageContent);
		jPanelImageContent.setSize(500, 375);

		jLabelImage.setAlignmentX(CENTER_ALIGNMENT);

		// Adici�n de los componentes del jPanelImageContent.
		jPanelImageContent.add(jLabelImage);

		// Reserva de memoria para el jScrollPaneImage.
		jScrollPaneImage = new JScrollPane(jPanelImageContent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneImage.setBorder(BorderFactory.createEmptyBorder());
		jScrollPaneImage.setAlignmentX(CENTER_ALIGNMENT);
		
		// Adici�n de los componentest del jPanelImage.
		jPanelImage.add(jScrollPaneImage);
		jPanelImage.add(jPanelImageButton);

		jPanelAnswer = new JPanel();
		boxLayoutPanelAnswer = new BoxLayout(jPanelAnswer, BoxLayout.Y_AXIS);
		jPanelAnswer.setLayout(boxLayoutPanelAnswer);

		jPanelAnswers = new JPanel[numAnswers];
		jPanelAnswersRadioButton = new JPanel[numAnswers];

		panelLabelAnswer = new JPanel[numAnswers];
		flowLayoutPanelLabelAnswer = new FlowLayout[numAnswers];

		panelLabelExplanation = new JPanel[numAnswers];
		flowLayoutPanelJlabelExplanation = new FlowLayout[numAnswers];

		// Bucle para la reserva de memoria de los paneles que contienen los datos
		// de las respuestas de la pregunta que se va a a�adir, modificar o borrar
		for (int i = 0; i < numAnswers; i++) {
			flowLayoutPanelLabelAnswer[i] = new FlowLayout(FlowLayout.LEFT);
			panelLabelAnswer[i] = new JPanel();
			panelLabelAnswer[i].setLayout(flowLayoutPanelLabelAnswer[i]);
			panelLabelAnswer[i].add(jLabelAnswer[i]);

			flowLayoutPanelJlabelExplanation[i] = new FlowLayout(FlowLayout.LEFT);
			panelLabelExplanation[i] = new JPanel();
			panelLabelExplanation[i].setLayout(flowLayoutPanelJlabelExplanation[i]);
			panelLabelExplanation[i].add(jLabelExplanation[i]);
		}

		// Reserva de memoria para el JPanel que contendr� a los JButton
		// anteriores.
		jPanelButtonAdd = new JPanel();
		flowLayoutPanelButtonAdd = new FlowLayout(FlowLayout.CENTER);
		jPanelButtonAdd.setLayout(flowLayoutPanelButtonAdd);

		// Adici�n de los JButton a sus JPanel correspondientes.
		if (type == TestEditor.NEW_QUESTION_FILE) {
			jPanelButtonAdd.add(jButtonSetIrtParameters);
			jPanelButtonAdd.add(Box.createHorizontalStrut(20));
		}

		jPanelButtonAdd.add(jButtonSave);
		jPanelButtonAdd.add(jButtonCancel);
		jPanelButtonAdd.add(jButtonClear);
		jPanelButtonAdd.add(jButtonAddAnswer);

		// Establecimiento de las propiedades de los JPanel.
		jPanelContent.setBackground(SystemColor.control);
		jPanelContent.setSize(getWidth(), getHeight());

		// Adici�n al jPanelContent de los JPanel especificos de la utilizaci�n
		// del
		// frame para modificar o borrar preguntas.
		if (type == TestEditor.MODIFY_QUESTION_OF_A_FILE) {
			// jPanelContent.add(jPanelNumberQuestionOfFile);
			jPanelContent.add(jPanelNavigation);
			jPanelContent.add(jPanelNumberQuestion);
		}

		// Adici�n al jPanelContent de los paneles contenidos en el frame
		// independientemente de que este haya sido invocado para a�adir,
		// modificar
		// o borrar preguntas.
		jPanelContent.add(jPanelEnunciate);
		jPanelContent.add(jPanelImage);

		if (type == TestEditor.MODIFY_QUESTION_OF_A_FILE) {
			// jPanelContent.add(jPanelIrtParameters);
			// jPanelContent.add(jPanelStatisticalParameters);
			// jPanelContent.add(jPanelTest);
			jPanelContent.add(jTabbedPane);
		}

		jPanelContent.add(jPanelAnswer);

		// Adici�n del JPanel anterior al JPanel principal del frame.
		jPanelContent.add(jPanelButtonAdd);

		if (type == TestEditor.MODIFY_QUESTION_OF_A_FILE) {
			// Adici�n del jPanelButtonModify al JPanel principal del
			// frame.
			jPanelContent.add(jPanelButtonModify);
		}

		// Establecimiento de mas propiedades de los JPanel.
		// jPanelQuestion.setBackground(SystemColor.control);
		jPanelEnunciate.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Enunciate (Obligatory)", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		// jPanelImage.setBackground(SystemColor.control);
		jPanelImage.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Image (Optional)", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		//jPanelAnswer.setBorder(BorderFactory.createTitledBorder(BorderFactory
		//		.createLineBorder(SystemColor.BLACK), "Answers", TitledBorder.LEFT,
		//		TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		boxLayoutPanelAnswers = new BoxLayout[numAnswers];
		flowLayoutPanelAnswersRadioButton = new FlowLayout[numAnswers];

		// Bucle para el establecimiento de propiedades de los JPanel que
		// contienen
		// los componentes relacionados con las respuestas de la pregunta.
		String cont;
		for (int i = 0; i < numAnswers; i++) {
			cont = String.valueOf(i + 1);

			jPanelAnswers[i] = new JPanel();
			boxLayoutPanelAnswers[i] = new BoxLayout(jPanelAnswers[i], BoxLayout.Y_AXIS);

			jPanelAnswers[i].setLayout(boxLayoutPanelAnswers[i]);
			jPanelAnswers[i].setBorder(BorderFactory.createTitledBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK), "Answer " + cont, TitledBorder.LEFT,
					TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

			jPanelAnswersRadioButton[i] = new JPanel();
			flowLayoutPanelAnswersRadioButton[i] = new FlowLayout(FlowLayout.LEFT);
			jPanelAnswersRadioButton[i].setLayout(flowLayoutPanelAnswersRadioButton[i]);

			// Adici�n de los JRadioButton a sus paneles correspondientes.
			jPanelAnswersRadioButton[i].add(jRadioButtonTrue[i]);
			jPanelAnswersRadioButton[i].add(jRadioButtonFalse[i]);

			// Adici�n de los jCheckBoxDeleteAnswer a sus paneles correspondientes.
			if (type == TestEditor.MODIFY_QUESTION_OF_A_FILE)
				jPanelAnswersRadioButton[i].add(jCheckBoxDeleteAnswer[i], FlowLayout.RIGHT);

			// Adici�n de los JPanel, relacionados con los componentes que
			// almacenan
			// las respuestas de la pregunta, a su JPanel correspondiente.
			jPanelAnswers[i].add(panelLabelAnswer[i]);
			jPanelAnswers[i].add(jScrollPaneAnswers[i]);
			jPanelAnswers[i].add(jPanelAnswersRadioButton[i]);
			jPanelAnswers[i].add(panelLabelExplanation[i]);
			jPanelAnswers[i].add(jScrollPaneExplanation[i]);
			jPanelAnswer.add(jPanelAnswers[i]);
		}
	}

	/*
	 * NOMBRE: JButtonAddAndDeleteImageInit. PERTENECE A: Clase FrmEditQuestion.
	 * LLAMADA POR: El constructor de la clase a la que pertenece. LLAMA A: nada.
	 * RECIBE: void. DEVUELVE: void. FUNCI�N: Inicializa los JButton que se usan
	 * para a�adir una imagen o eliminarla de una pregunta.
	 */

	private void JButtonAddAndDeleteImageInit() {
		// Inicializaci�n de los JButton dependiendo de la funcionalidad para la
		// que ha sido invocada el frame.
		if (type == TestEditor.MODIFY_QUESTION_OF_A_FILE) {
			// Si los iconos de los botones se han inicializado con �xito, entonces
			// los botones los mostraran, en caso contrario solo tendr�n texto.
			if (ImageIconLoad == true) {
				jButtonAddImage = new JButton("Modify Image", iconAddImage);
				jButtonDeleteImage = new JButton("Delete Image", iconDeleteImage);
			} else {
				jButtonAddImage = new JButton("Modify Image");
				jButtonDeleteImage = new JButton("Delete Image");
			}

			if (questionImageIcon == null)
				jButtonDeleteImage.setEnabled(false);
			else
				jButtonDeleteImage.setEnabled(true);

		} else {
			if (ImageIconLoad == true) {
				jButtonAddImage = new JButton("Add Image", iconAddImage);
				jButtonDeleteImage = new JButton("Delete Image", iconDeleteImage);
			} else {
				jButtonAddImage = new JButton("Add Image");
				jButtonDeleteImage = new JButton("Delete Image", iconDeleteImage);
			}
			jButtonDeleteImage.setEnabled(false);
		}

		// Establecimiento de la funcionalidad de jButtonAddImage.
		jButtonAddImage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonAddImageActionPerformed(e);
			}
		});

		// Establecimiento de la funcionalidad de jButtonDeleteImage.
		jButtonDeleteImage.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonDeleteImageActionPerformed();
			}
		});
	}

	/*
	 * NOMBRE: JRadioButtonInit. PERTENECE A: Clase FrmEditQuestion. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa los JRadioButton que se usan
	 * para indicar si una respuestas es correcta o no.
	 */

	private void JRadioButtonInit() {
		// Reserva de memoria para el array de JRadioButton que contiene a todos
		// los JRadioButton.
		jRadioButtonTrue = new JRadioButton[numAnswers];
		jRadioButtonFalse = new JRadioButton[numAnswers];

		// Bucle para la reserva de memoria de todos los JRadioButton.
		for (int i = 0; i < numAnswers; i++) {
			jRadioButtonTrue[i] = new JRadioButton("True");
			jRadioButtonFalse[i] = new JRadioButton("False");

			// Establecimiento de la funcionalidad para los jRaditoButtonTrue.
			jRadioButtonTrue[i].addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					questionModified = true;
				}
			});

			// Establecimiento de la funcionalidad para los jRadioButtonFalse.
			jRadioButtonFalse[i].addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					questionModified = true;
				}
			});
		}
	}

	/*
	 * NOMBRE: JCheckBoxInit. PERTENECE A: Clase FrmEditQuestion. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JCheckBox que se usan para indicar
	 * si una respuestas va ser eliminada o no.
	 */

	private void JCheckBoxInit() {
		// Reserva de memoria para el array de JCheckBox que contiene a todos
		// los jCheckBoxDeleteAnswer.
		jCheckBoxDeleteAnswer = new JCheckBox[numAnswers];

		// Bucle para la reserva de memoria de todos los jCheckBoxDeleteAnswer.
		for (int i = 0; i < numAnswers; i++) {
			jCheckBoxDeleteAnswer[i] = new JCheckBox("Delete this Answer");

			// Establecimiento de la funcionalidad para los jCheckBoxDeleteAnswer.
			jCheckBoxDeleteAnswer[i].addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					questionModified = true;
				}
			});
		}
	}

	/*
	 * NOMBRE: JComboBoxInit. PERTENECE A: Clase FrmEditQuestion. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JCombo del frame,
	 * reservando memoria para los mismos y estableciendo su tama�o y su
	 * funcionalidad.
	 */

	private void JComboBoxInit() {
		// Reserva de memoria para los JComboBox.
		jComboBoxClassicTest = new JComboBox();
		jComboBoxAdaptiveTest = new JComboBox();
		jComboBoxNavigation = new JComboBox();

		// Borrado de sus contenidos previos.
		jComboBoxClassicTest.removeAllItems();
		jComboBoxAdaptiveTest.removeAllItems();
		jComboBoxNavigation.removeAllItems();

		jComboBoxClassicTest.setFont(new java.awt.Font("Dialog", 1, 12));
		jComboBoxAdaptiveTest.setFont(new java.awt.Font("Dialog", 1, 12));
		jComboBoxNavigation.setFont(new java.awt.Font("Dialog", 1, 12));

		// Establecimiento de valores para jComboBoxNavigation.
		jComboBoxNavigation.addItem("number");
		jComboBoxNavigation.addItem("code");

		jComboBoxNavigation.setSelectedIndex(indexComboNavigation);

		// Establecimiento de la funcionalidad de jComboBoxNavigation.
		jComboBoxNavigation.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jComboBoxNavigation.getItemCount() > 0)
					indexComboNavigation = jComboBoxNavigation.getSelectedIndex();
			}
		});

	}

	/*
	 * NOMBRE: ButtonGroupInit. PERTENECE A: Clase FrmEditQuestion. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa los ButtonGroup que se encargan
	 * de que los JRadioButton anteriores no est�n activos al mismo tiempo.
	 */

	private void ButtonGroupInit() {
		// Reserva de memoria para el array que contiene todos los ButtonGroup.
		jRadioButtonAnswer = new ButtonGroup[numAnswers];

		// Bucle para la reserva de memoria de todos los ButtonGroup.
		for (int i = 0; i < numAnswers; i++) {
			jRadioButtonAnswer[i] = new ButtonGroup();

			// Adici�n al ButtonGroup de los JRadioButton que lo componen.
			jRadioButtonAnswer[i].add(jRadioButtonTrue[i]);
			jRadioButtonAnswer[i].add(jRadioButtonFalse[i]);
		}
	}

	/*
	 * NOMBRE: JTextAreaInit. PERTENECE A: Clase FrmEditQuestion. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JTextArea del frame.
	 * Estos se encargan de contener los datos para a�adir una pregunta o de
	 * mostrar los datos de una pregunta que se va a modificar o borrar.
	 */

	private void JTextAreaInit() {
		// Reserva de memoria para el jTextAreaEnunciate, que contendr� el
		// enunciado de la pregunta.
		jTextAreaEnunciate = new JTextArea("Write here the text question.", 2, 2);
		jTextAreaEnunciate.setLineWrap(true);
		jTextAreaEnunciate.setWrapStyleWord(true);
		jTextAreaEnunciate.setFont(new java.awt.Font("Dialog", 0, 13));
		jTextAreaEnunciate.setBackground(SystemColor.white);
		jTextAreaEnunciate.setBorder(BorderFactory.createLineBorder(SystemColor.black));

		// Establecimiento del comportamiento de jTextAreaEnunciate, si el
		// frame ha sido invocado para a�adir pregunas a un fichero.
		if (type == TestEditor.NEW_QUESTION_FILE) {
			jTextAreaEnunciate.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusGained(FocusEvent e) {
					jTextAreaEnunciate.selectAll();
				}
			});
		}

		// Establecimiento de la funcionalidad de jTextAreaEnunciate.
		jTextAreaEnunciate.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				questionModified = true;
			}
		});

		// Reserva de memoria para los array de JTextArea que contendr�n los datos
		// de las respuestas de la pregunta.
		jTextAreaAnswers = new JTextArea[numAnswers];
		jTextAreaExplanation = new JTextArea[numAnswers];
		jScrollPaneAnswers = new JScrollPane[numAnswers];
		jScrollPaneExplanation = new JScrollPane[numAnswers];

		// Bucle para la reserva de memoria de cada uno de los JTextArea que
		// contendr�n los datos de las respuestas.
		for (int i = 0; i < numAnswers; i++) {
			final int j = i;

			// Reserva de memoria para los jTextAreaAnswers.
			jTextAreaAnswers[i] = new JTextArea("Write here the answer.", 2, 2);
			jScrollPaneAnswers[i] = new JScrollPane(jTextAreaAnswers[i],
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPaneAnswers[i].setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			jTextAreaAnswers[i].setLineWrap(true);
			jTextAreaAnswers[i].setWrapStyleWord(true);
			jTextAreaAnswers[i].setFont(new java.awt.Font("Dialog", 0, 13));
			jTextAreaAnswers[i].setBackground(SystemColor.white);
			jTextAreaAnswers[i].setBorder(BorderFactory.createLineBorder(SystemColor.black));

			// Reserva de memoria para los jTextAreaExplanation.
			jTextAreaExplanation[i] = new JTextArea("Write here the explanation.", 2, 2);
			jScrollPaneExplanation[i] = new JScrollPane(jTextAreaExplanation[i],
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPaneExplanation[i].setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			jTextAreaExplanation[i].setLineWrap(true);
			jTextAreaExplanation[i].setWrapStyleWord(true);
			jTextAreaExplanation[i].setFont(new java.awt.Font("Dialog", 0, 13));
			jTextAreaExplanation[i].setBackground(SystemColor.white);
			jTextAreaExplanation[i].setBorder(BorderFactory.createLineBorder(SystemColor.black));

			// Establecimiento del comportamiento de los jTextAreaAnswers si el
			// frame ha sido invocado para a�adir preguntas a un fichero.
			if (type == TestEditor.NEW_QUESTION_FILE) {
				jTextAreaAnswers[i].addFocusListener(new java.awt.event.FocusAdapter() {
					public void focusGained(FocusEvent e) {
						jTextAreaAnswers[j].selectAll();
					}
				});
			}

			// Establecimiento de la funcionalidad de los jTextAreaAnswers.
			jTextAreaAnswers[i].addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					questionModified = true;
				}
			});

			// Establecimiento del comportamiento de los jTextAreaAExplanation si
			// el
			// frame ha sido invocado para a�adir preguntas a un fichero.
			if (type == TestEditor.NEW_QUESTION_FILE) {
				jTextAreaExplanation[i].addFocusListener(new java.awt.event.FocusAdapter() {
					public void focusGained(FocusEvent e) {
						jTextAreaExplanation[j].selectAll();
					}
				});
			}

			// Establecimiento de la funcionalidad de los jTextAreaExplanation.
			jTextAreaExplanation[i].addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					questionModified = true;
				}
			});
		}
	}

	/*
	 * NOMBRE: JButtonInit. PERTENECE A: Clase FrmEditQuestion. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JButton del frame. Estos
	 * se encargan de realizar las operaciones de adici�n de preguntas a un
	 * fichero, modificar una pregunta, eliminar una pregunta de un fichero, etc.
	 */

	private void JButtonInit() {
		// Inicializaci�n del jButtonSave dependiendo de la funci�n para la que
		// haya
		// sido invocado el frame.
		if (type == TestEditor.MODIFY_QUESTION_OF_A_FILE) {
			// Si los iconos de los botones del frame se cargaron con
			// �xito,
			// entonces los botones los mostrar�n, en caso contrario los JButton
			// solo
			// contendr�n texto.
			if (ImageIconLoad == true)
				jButtonSave = new JButton("Save", iconSave);
			else
				jButtonSave = new JButton("Save");

			// Establecimiento de la funcionalidad de jButtonSave.
			jButtonSave.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonModifyActionPerformed();
				}
			});
		} else {
			// Si los iconos de los botones del frame se cargaron con
			// �xito,
			// entonces los botones los mostrar�n, en caso contrario los JButton
			// solo
			// contendr�n texto.
			if (ImageIconLoad == true) {
				jButtonSave = new JButton("Save", iconSave);
				jButtonSetIrtParameters = new JButton("Set Irt Parameters", iconInfoGreen32);
			} else {
				jButtonSave = new JButton("Save");
				jButtonSetIrtParameters = new JButton("Set Irt Parameters");
			}

			// Establecimiento de la funcionalidad de jButtonSave.
			jButtonSave.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonSaveActionPerformed();
				}
			});

			// Establecimiento de la funcionalidad de jButtonSetIrtParameters.
			jButtonSetIrtParameters.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonSetIrtParametersActionPerformed();
				}
			});
		}

		// Reserva de memoria para el resto de los JButton, dependiendo de si los
		// iconos del JInteralFrame se han cargado con �xito o no.
		if (ImageIconLoad == true) {
			jButtonCancel = new JButton("Cancel", iconCancel);
			jButtonClear = new JButton("Clear", iconClear);
			jButtonAddAnswer = new JButton("Add Answer", iconAddAnswer);
		} else {
			jButtonCancel = new JButton("Cancel");
			jButtonClear = new JButton("Clear");
			jButtonAddAnswer = new JButton("Add Answer");
		}

		// Establecimiento de la funcionalidad de jButtonCancel.
		jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonCancelActionPerformed(e);
			}
		});

		// Establecimiento de la funcionalidad de jButtonClear.
		jButtonClear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonClearActionPerformed(e);
			}
		});

		// Establecimiento de la funcionalidad de jButtonAddAnswer.
		jButtonAddAnswer.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonAddAnswerActionPerformed(e);
			}
		});

		// Inicializacion de los botones de navegacion.
		if (type == TestEditor.MODIFY_QUESTION_OF_A_FILE) {
			// Si los iconos de los botones del frame se cargaron con
			// �xito,
			// entonces los botones los mostrar�n, en caso contrario los JButton
			// solo
			// contendr�n texto.
			if (ImageIconLoad == true) {
				jButtonGo = new JButton("Go", iconGo);
				// jButtonGoCode = new JButton("Go", iconGo);
				jButtonPrevious = new JButton("Previuos", iconPrevious);
				jButtonNext = new JButton("Next", iconNext);
				jButtonDelete = new JButton("Delete", iconDelete);
				jButtonSaveInOtherFile = new JButton("Save In Other File", iconSave);
				jButtonAddQuestionToClassicTest = new JButton("Add to Classic Test",
						iconAddQuestionToClassicTest);
				jButtonAddQuestionToAdaptiveTest = new JButton("Add to Adaptive Test",
						iconAddQuestionToAdaptiveTest);
				jButtonIrtHelp = new JButton("Help", iconHelp32);
			} else {
				jButtonGo = new JButton("Go");
				// jButtonGoCode = new JButton("Go");
				jButtonPrevious = new JButton("<<<---Previuos---");
				jButtonNext = new JButton("---Next--->>>");
				jButtonDelete = new JButton("Delete Question");
				jButtonSaveInOtherFile = new JButton("Save Question In Other File");
				jButtonAddQuestionToClassicTest = new JButton("Add Qestion to Classic Test File");
				jButtonIrtHelp = new JButton("Help");
			}

			// Establecimiento de la funcionalidad del jButtonGo.
			jButtonGo.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (jComboBoxNavigation.getSelectedItem().toString().equals("number"))
						jButtonGoActionPerformed();
					else
						jButtonGoCodeActionPerformed();
				}
			});

			// Establecimiento de la funcionalidad del jButtonIrtHelp.
			jButtonIrtHelp.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, "Difficulty: \n" + "Very Easy = "
							+ String.valueOf(DIFFICULTY_MIN) + "\n" + "Normal = "
							+ String.valueOf(DIFFICULTY_NORMAL) + "\n" + "Very Difficult = "
							+ String.valueOf(DIFFICULTY_MAX) + "\n" + "\n" + "Discrimination: \n"
							+ "Not very discriminatory = " + String.valueOf(DISCRIMINATION_MIN) + "\n" + "Normal = "
							+ String.valueOf(DISCRIMINATION_NORMAL) + "\n" + "Very discriminatory = "
							+ String.valueOf(DISCRIMINATION_MAX),// + "\n" +
							"Irt Help", 1);
				}
			});

			// Establecimiento de la funcionalidad del jButtonDelete.
			jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonDeleteActionPerformed(e);
				}
			});

			// Establecimiento de la funcionalidad del jButtonSaveInOtherFile.
			jButtonSaveInOtherFile.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonSaveInOtherFileActionPerformed(e);
				}
			});

			// Establecimiento de la funcionalidad de jButtonPrevious.
			jButtonPrevious.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonPreviousActionPerformed();
				}
			});

			// Establecimiento de la funcionalidad de jButtonNext.
			jButtonNext.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonNextActionPerformed();
				}
			});

			// Establecimiento de la funcionalidad de
			// jButtonAddQuestionToClassicTest.
			jButtonAddQuestionToClassicTest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonAddQuestionToTestActionPerformed(TestEditor.CLASSIC);
				}
			});

			// Establecimiento de la funcionalidad de
			// jButtonAddQuestionToAdaptiveTest.
			jButtonAddQuestionToAdaptiveTest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jButtonAddQuestionToTestActionPerformed(TestEditor.ADAPTIVE);
				}
			});

		}
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
	 * NOMBRE: jButtonGoActionPerformed. PERTENECE A: Clase FrmEditQuestion.
	 * LLAMADA POR: Al hacer click sobre el jButtonGo. LLAMA A:
	 * getQuestionData(); repaintFrmQuestionData(); setQuestionData(); RECIBE: e:
	 * Objeto de tipo ActionEvent. Contiene el evento que produjo la llamada a
	 * este m�todo. DEVUELVE: void. FUNCI�N: Invoca a un m�todo de esta propia
	 * clase que env�a al servidor los datos necesarios para localizar una
	 * pregunta por el orden que ocupa en el fichero de preguntas, y obtiene
	 * devuelto por dicho m�todo, un objeto de tipo Vector que contiene los datos
	 * de la pregunta deseada, o null en caso de producirse alg�n error en el
	 * proceso.
	 */
	private void jButtonGoActionPerformed() {
		if (jTextFieldGotoQuestion.getText().trim().equals("") == false) {
			// Waiting cursor
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			try {
				// Reads the value from the user in the jTextFieldGotoQuestion.
				numQuestionShow = Integer.valueOf(jTextFieldGotoQuestion.getText()).intValue() - 1;
				numberQuestionOfFileInt = Integer.valueOf(jTextFieldNumberQuestionOfFile.getText()).intValue();
			} catch (java.lang.NumberFormatException e) {
				return;
			}

			// Corrects that value if is incorrect
			if (numQuestionShow >= numberQuestionOfFileInt) {
				numQuestionShow = numberQuestionOfFileInt - 1;
			} else if (numQuestionShow <= 0) {
				numQuestionShow = 0;
			}

			// Checks that the question has not been saved but already modified.
			// If yes asks the user to save the updates before showing the next
			// question
			if (questionSaved == false && questionModified == true) {
				// Message to ask the user
				String message = "Do you want to save this question?";
				int result = JOptionPane.showConfirmDialog(this, message, "Save Question",
						JOptionPane.YES_NO_OPTION);

				// If the response is yes, makes the update
				if (result == JOptionPane.YES_OPTION) {
					onClosing = true;

					// makes the update
					jButtonModifyActionPerformed();

					if (questionSaved == false) {
						setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
					}
				} else {
					// Lose the changes
					questionModified = false;
					imageModified = false;
				}

				// Calls the method that request the server the information about
				// the next question
				Question question = parent.getQuestionByOrder(courseName, questionsFileName,
						numQuestionShow);

				// If the Vector object returns by the getQuestionData method is
				// null,
				// then shows a error message and close the frame.
				if (question == null) {
					JOptionPane.showMessageDialog(this, "ERROR: Error reading course or question file. "
							+ "\n" + "It`s posible that the file doesn't have questions to modify.",
							"Error getting question", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					// Updates the number of answers with the value of the question
					// returned by the server
					numAnswers = question.getNumberOfAnswers();

					// Puts the data of the question in the components of the
					// frame
					repaintFrmQuestionData();
					setQuestionData(question, codeBase);
				}
			} else {
				// Calls the method that request the server the information about
				// the question
				Question question = parent.getQuestionByOrder(courseName, questionsFileName,
						numQuestionShow);

				// If the Vector object returns by the getQuestionData method is
				// null,
				// then shows a error message and close the frame.
				if (question == null) {
					JOptionPane.showMessageDialog(this, "ERROR: Error reading course or question file. "
							+ "\n" + "It`s posible that the file doesn't have questions to modify.",
							"Error getting question", JOptionPane.ERROR_MESSAGE);
				} else {
					// Updates the number of answers with the value of the question
					// returned by the server
					numAnswers = question.getNumberOfAnswers();

					// Puts the data of the question in the components of the
					// frame
					repaintFrmQuestionData();
					setQuestionData(question, codeBase);
				}
			}
			// Default cursor.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/*
	 * NOMBRE: jButtonGoCodeActionPerformed. PERTENECE A: Clase FrmEditQuestion.
	 * LLAMADA POR: Al hacer click sobre el jButtonGoCode. LLAMA A:
	 * getQuestionDataCode(); repaintFrmQuestionData(); setQuestionData();
	 * RECIBE: nada. DEVUELVE: void. FUNCI�N: Invoca a un m�todo de esta propia
	 * clase que env�a al servidor los datos necesarios para localizar una
	 * pregunta por su c�digo en el fichero de preguntas, y obtiene devuelto por
	 * dicho m�todo, un objeto de tipo Vector que contiene los datos de la
	 * pregunta deseada, o null en caso de producirse alg�n error en el proceso.
	 */
	private void jButtonGoCodeActionPerformed() {
		if (jTextFieldGotoQuestion.getText().trim().equals("") == false) {
			// Waiting cursor
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// Read the value introduced by the user
			int codeQuestionSearchInt = -1;
			try {
				codeQuestionSearchInt = Integer.valueOf(jTextFieldGotoQuestion.getText()).intValue();
			} catch (java.lang.NumberFormatException e) {
				return;
			}

			String codeQuestionSearch = String.valueOf(codeQuestionSearchInt);

			// Checks if the current question has not been saved but modified.
			// If yes, asks the user if he wants to update the question before
			// showing the next question
			if (questionSaved == false && questionModified == true) {
				// Asks the user to update the current question
				String message = "Do you want to save this question?";
				int result = JOptionPane.showConfirmDialog(this, message, "Save Question",
						JOptionPane.YES_NO_OPTION);

				// If yes, updates the question
				if (result == JOptionPane.YES_OPTION) {
					onClosing = true;

					// Calls the method that makes the update
					jButtonModifyActionPerformed();

					if (questionSaved == false) {
						setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
					}
				} else {
					questionModified = false;
					imageModified = false;
				}

				// Reads the following question from the server.
				Question question = parent.getQuestionByCode(courseName, questionsFileName,
						codeQuestionSearch);

				// If the Vector object returned by the server is null,
				// then shows a error message and closes the frame.
				if (question == null) {
					JOptionPane.showMessageDialog(this, "ERROR: Error when reading course or question file. "
							+ "\n" + "It is possible that any question doesn't exist with that code.",
							"Error to load question", JOptionPane.ERROR_MESSAGE);

					return;
				} else {
					// Updates the number of questions witht the value of the
					// following question
					numAnswers = question.getNumberOfAnswers();

					// Updates the value of the variable that indicates the order
					// that a question takes in the question file.
					numQuestionShow = question.getNumberQuestionOrder();

					// Puts the data in the window
					repaintFrmQuestionData();
					setQuestionData(question, codeBase);
				}
			} else {
				// Reads the following question from the server.
				Question question = parent.getQuestionByCode(courseName, questionsFileName,
						codeQuestionSearch);

				// If the Vector object returned by the server is null,
				// then shows a error message and closes the frame.
				if (question == null) {
					JOptionPane.showMessageDialog(this, "ERROR: Error when reading course or question file. "
							+ "\n" + "It is possible that any question doesn't exist with that code.",
							"Error to load question", JOptionPane.ERROR_MESSAGE);
				} else {
					// Updates the number of questions witht the value of the
					// following question
					numAnswers = question.getNumberOfAnswers();

					// Updates the value of the variable that indicates the order
					// that a question takes in the question file.
					numQuestionShow = question.getNumberQuestionOrder();

					// Puts the data in the window
					repaintFrmQuestionData();
					setQuestionData(question, codeBase);
				}
			}

			// Default cursor
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/*
	 * NOMBRE: jButtonAddImageActionPerformed. PERTENECE A: Clase
	 * FrmEditQuestion. LLAMADA POR: Al hacer click sobre el jButtonAddImage.
	 * LLAMA A: nada. RECIBE: e: Objeto de tipo ActionEvent. Contiene el evento
	 * que produjo la llamada a este m�todo. DEVUELVE: void. FUNCI�N: Muestra un
	 * objeto de tipo JFileChooser para que el usuario seleccione de su disco
	 * duro el fichero de imagen que desea asociar a la pregunta que est� creando
	 * o modificando. Este m�todo mostrar� un mensaje de error si se intenta
	 * asociar una imagen a una pregunta sin haberse descargado previamente el
	 * fichero .java.policy.
	 */

	void jButtonAddImageActionPerformed(ActionEvent e) {
		try {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// Reserva de memoria para jFileChoosers.
			JFileChooser jFileChooser = new JFileChooser(new File(System.getProperty("user.dir")));

			// Establecimiento del titulo.
			jFileChooser.setDialogTitle("Select Image");

			// Establecimiento del filtro para los tipos de archivos permitidos.
			jFileChooser.addChoosableFileFilter(new ImageFilter());

			// Establecimiento del accesorio que permite previsualizar las im�genes
			// en el propio jFileChooser.
			jFileChooser.setAccessory(new ImagePreview(jFileChooser));

			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			// Apertura del jFileChooser.
			jFileChooser.setVisible(true);
			jFileChooser.setSize(200, 100);
			int option = jFileChooser.showOpenDialog(this);

			if (option == JFileChooser.CANCEL_OPTION)
				return;

			// Obtenci�n del fichero de imagen seleccionado por el usuario.
			imageFile = jFileChooser.getSelectedFile();
			byte[] data = new byte[(int) imageFile.length()];

			if (data.length > (1024 * 1024)) {
				JOptionPane.showMessageDialog(this, "The image is too big, its maximum size should be 1 Mb.",
						"File Size", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				// Repaint the jLabelImage.
				questionImageIconTemp = new ImageIcon(imageFile.getAbsolutePath());

				// Llamada al m�todo que cambia el tama�o del imagenIcon.
				scaledImageIcon();

				JLabelImageInit();
				jLabelImage.setIcon(questionImageIcon);
				JPanelInit();
				frameInit();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error loading image " + ex.getMessage(), "ERROR",
						JOptionPane.ERROR_MESSAGE);

				return;
			}

			// Establecimiento a true de las variables que indican que la pregunta
			// posee una imagen asociada o que ha sido modificada.
			existImage = true;
			questionModified = true;
			imageModified = true;
			pathImage = imageFile.getAbsolutePath();

			jButtonDeleteImage.setEnabled(true);
		} catch (SecurityException sE) {
			// Mensaje de error por falta de descarga del fichero .java.policy en
			// el
			// directorio del ususario que est� usando la herramienta TestEditor.
			JOptionPane.showMessageDialog(this,
					"ERROR: If you want to add images to the questions that it will create," + "\n"
							+ "you need download the file. java.policy in their user directory." + "\n"
							+ "Download this file of the main page of the tool WOW! TestEditor." + "\n"
							+ "When it has finished their work session," + "\n"
							+ "it is advisable to eliminate this file of their system.", "Security Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * NOMBRE: jButtonDeleteImageActionPerformed. PERTENECE A: Clase
	 * FrmEditQuestion. LLAMADA POR: Al hacer click sobre el jButtonDeleteImage.
	 * LLAMA A: Nada. RECIBE: Nada. DEVUELVE: void. FUNCI�N: Elimina la imagen
	 * asociada a la pregunta.
	 */

	void jButtonDeleteImageActionPerformed() {
		// Repintado del jLabelImage.
		try {
			// Repintado del jLabelImage.
			questionImageIcon = null;
			JLabelImageInit();
			jLabelImage.setIcon(null);
			JPanelInit();
			frameInit();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error to load image " + ex.getMessage(), "ERROR",
					JOptionPane.ERROR_MESSAGE);

			return;
		}

		questionModified = true;
		existImage = false;
		imageModified = true;
		imageFile = null;
		pathImage = "";

		jButtonDeleteImage.setEnabled(false);
	}

	/*
	 * NOMBRE: jButtonSaveActionPerformed. PERTENECE A: Clase FrmEditQuestion.
	 * LLAMADA POR: Al hacer click sobre el jButtonSave. LLAMA A:
	 * savedQuestion(); savedFile(); RECIBE: nada. DEVUELVE: void. FUNCI�N:
	 * Comprueba que los datos intotroducidos por el usuario para la creaci�n de
	 * una nueva pregunta son correctos. Si dichos datos son correctos, entonces
	 * se produce la llamada al m�todo de esta misma clase savedQuestion, el cual
	 * env�a los datos de la pregunta al servidor para que sean almacenados.
	 */

	void jButtonSaveActionPerformed() {
		int answersCorrect = 0;

		// Comprobaci�n que jTextAreaEnunciate no est� vacio.
		if (jTextAreaEnunciate.getText().equals("")) {
			dataRight = false;
		} else {
			dataRight = true;
		}

		// Bucle para la comprobaci�n que los jTextAreaAnswers
		// no est�n vacios y que los jRadioButtonTrue o jRadioButtonFalse est�n
		// seleccionados.
		if (dataRight == true) {
			for (int i = 0; i < numAnswers; i++) {
				// Comprobaci�n que los jTextAreaAnswers no est�n vacios.
				if (jTextAreaAnswers[i].getText().equals("")) {
					dataRight = false;
					break;
				} else
					dataRight = true;

				// Comprobaci�n que los jRadioButtonTrue o jRadioButtonFalse est�n
				// seleccionados.
				if (jRadioButtonTrue[i].isSelected() == false && jRadioButtonFalse[i].isSelected() == false) {
					dataRight = false;
					break;
				} else {
					dataRight = true;

					if (jRadioButtonTrue[i].isSelected())
						answersCorrect++;
				}
			}
		}
		// Si las comprobaciones anteriores no son correctas se muestra un mensaje
		// de error.
		if (dataRight == false) {
			JOptionPane.showMessageDialog(this, "You should complete all the data.", "Warning",
					JOptionPane.WARNING_MESSAGE);
		} else {
			if (answersCorrect == 0) {
				JOptionPane.showMessageDialog(this, "At least a question should be correct.", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Construcci�n del objeto Question que se enviar� al servidor.
			Question question = new Question();

			question.setCourse(courseName);
			question.setQuestionsFileName(questionsFileName);
			question.setConcept(concept);
			question.setEnunciate(jTextAreaEnunciate.getText().trim());
			question.setExistImage(String.valueOf(existImage));

			if (existImage == true) {
				question.setExistImage("true");
				if (saveInOtherFile == false) {
					question.setPathImage(imageFile.getName().substring(imageFile.getName().lastIndexOf("."))
							.toLowerCase());
					copyImage = false;
				} else {
					question.setPathImage(pathImage);
					copyImage = true;
				}
			} else {
				question.setExistImage("false");
				question.setPathImage("");
				copyImage = false;
			}

			question.setDifficulty(difficulty);
			question.setDiscrimination(discrimination);
			question.setGuessing(guessing);

			// Bucle para la continuaci�n de la construcci�n de la cadena de datos
			// para
			// enviar al servidor.
			Vector textAnswers = new Vector(numAnswers);
			Vector correctAnswers = new Vector(numAnswers);
			Vector textExplanation = new Vector(numAnswers);

			for (int i = 0; i < numAnswers; i++) {
				textAnswers.add(jTextAreaAnswers[i].getText().trim());

				if (jRadioButtonTrue[i].isSelected() == true)
					correctAnswers.add("true");
				else
					correctAnswers.add("false");

				textExplanation.add(jTextAreaExplanation[i].getText().trim());
			}

			question.setTextAnswers(textAnswers);
			question.setCorrectAnswers(correctAnswers);
			question.setTextExplanation(textExplanation);

			question.setNumberOfAnswers(numAnswers);

			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			String saveQuestion = ""; // Almacena la respuesta del servidor a la
			// solicitud de almacenamiento del fichero
			// asociado a al pregunta.

			// Llamada al m�todo que env�a los datos de la pregunta al servidor.
			saveQuestion = parent.saveQuestion(question, imageFile, copyImage);

			// Si el resultado de la solicitud en null, se muestra un mensaje de
			// error.
			if (saveQuestion == null) {
				saveQuestion = "ERROR: Question not saved." + "\n"
						+ "There was an error connecting with the server.";
				questionSaved = false;

				JOptionPane.showMessageDialog(this, saveQuestion, "Result of saving the question",
						JOptionPane.ERROR_MESSAGE);
			}

			// Cursor estandar.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			// Comprobaci�n de las operaciones realizadas por el usuario para
			// preguntarle o no, si desea a�adir mas preguntas al fichero.
			if (saveQuestion != null
			 && saveQuestion.startsWith(TestEditor.TEXT_QUESTION_SAVED)
			 && onClosing == false
			 && saveInOtherFile == false) {
				// Sets the question as saved
				questionSaved = true;

				// Mensaje de pregunta.
				int result = JOptionPane.showConfirmDialog(this, saveQuestion
						+ "Do you want to add more questions to the question file?",
						"Result of saving the question", JOptionPane.YES_NO_OPTION);

				// Si la respuesta al mensaje es afirmativa, se muestra otro
				// frame para volver a pedir los datos de la nueva
				// pregunta.
				if (result == JOptionPane.YES_OPTION) {
					// Ocultaci�n del frame actual.
					setVisible(false);

					// Reserva de memoria para el nuevo frame.
					FrmEditQuestion frmEditQuestion = new FrmEditQuestion("Question Data.  Course: " + courseName
							+ " - Question File: " + questionsFileName, numAnswersOrigin, userLogin,
							userPassword, courseName, questionsFileName, codeBase, wowPath, iconTestEditorPath,
							parent, TestEditor.NEW_QUESTION_FILE);

					// Adici�n al padre del frame actual del nuevo
					// frame.
					getParent().add(frmEditQuestion);
					try {
						frmEditQuestion.setSelected(true);
					} catch (java.beans.PropertyVetoException eP) {}
					dispose();
				} else {
					// Cerrado del frame actual en caso de responder
					// negativamente
					// a la pregunta de si se desea a�adir m�s preguntas al fichero.
					setVisible(false);
					dispose();
				}
			} else {
				if (saveQuestion != null
				 && saveQuestion.startsWith(TestEditor.TEXT_QUESTION_SAVED)
				 && onClosing == true) {
					questionSaved = true;
				}

				// Mensaje con el resultado de la petici�n de almacenamiento de la
				// pregunta en caso de que esta no haya podido ser realizada.
				JOptionPane.showMessageDialog(this, saveQuestion, "Result of saving the question", 1);
			}
		}
	}

	/*
	 * NOMBRE: jButtonSetIrtParametersActionPerformed. PERTENECE A: Clase
	 * FrmEditQuestion. LLAMADA POR: Al hacer click sobre el
	 * jButtonSetIrtParameters. LLAMA A: nada. RECIBE: nada. DEVUELVE: void.
	 * FUNCI�N: Muestra un JDialog de tipo DlgIrtParameters para que el usuario
	 * introduzca los parametros irt deseados.
	 */

	void jButtonSetIrtParametersActionPerformed() {
		// Reserva de memoria para el JDialog tipo DlgIrtParameters.
		dlgIrtParameters = new DlgIrtParameters("SELECT DE IRT PARAMETERS", parent);
		if (dlgIrtParameters.id == true) {
			difficulty = Double.valueOf(dlgIrtParameters.jTextFieldDifficulty.getText().trim()).doubleValue();
			discrimination = Double.valueOf(dlgIrtParameters.jTextFieldDiscrimination.getText().trim())
					.doubleValue();
			guessing = Double.valueOf(dlgIrtParameters.jTextFieldGuessing.getText().trim()).doubleValue();

			DIFFICULTY_SELECT = difficulty;
			DISCRIMINATION_SELECT = discrimination;
			GUESSING_SELECT = guessing;

			parent.DIFFICULTY_SELECT = difficulty;
			parent.DISCRIMINATION_SELECT = discrimination;
			parent.GUESSING_SELECT = guessing;
		} else {
			difficulty = DIFFICULTY_SELECT;
			discrimination = DISCRIMINATION_SELECT;
		}
	}

	/**
	 * Checks that the data introduced by the user for the update of a question
	 * is correct. If yes, then calls the modifiedQuestion method that sends the
	 * server the data of the question to be updated.
	 */
	void jButtonModifyActionPerformed() {
		// Indicates that the data of the question is correct or not
		boolean dataRight = true;
		// Indicates if the answers of the questions can be removed or not
		boolean deleteAnswer = false;
		// Stores the number of answers to be removed
		int numAnswersDelete = 0;
		int answersCorrect = 0;
		String message = null;

		// Checks if the jTextAreaEnunciate is empty
		dataRight = !(jTextAreaEnunciate.getText().equals(""));

		// Checks if the jTextField are not empty
		dataRight = !(jTextFieldDifficulty.getText().equals(""));
		dataRight = !(jTextFieldDiscrimination.getText().equals(""));

		// Checks if the values from the JTextField are correct
		try {
			double difficulty = Double.valueOf(jTextFieldDifficulty.getText()).doubleValue();
			if (difficulty < DIFFICULTY_MIN || difficulty > DIFFICULTY_MAX) {
				dataRight = false;
				message = "ERROR: The value of the difficulty should be understood between";
				message += String.valueOf(DIFFICULTY_MIN) + " and " + String.valueOf(DIFFICULTY_MAX);
				JOptionPane.showMessageDialog(this, message, "Data Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (java.lang.NumberFormatException e) {
			dataRight = false;
		}

		try {
			double discrimination = Double.valueOf(jTextFieldDiscrimination.getText()).doubleValue();
			if (discrimination < DISCRIMINATION_MIN || discrimination > DISCRIMINATION_MAX) {
				dataRight = false;
				message = "ERROR: The value of the discrimination should be understood between";
				message += String.valueOf(DISCRIMINATION_MIN) + " and " + String.valueOf(DISCRIMINATION_MIN);
				JOptionPane.showMessageDialog(this, message, "Data Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (java.lang.NumberFormatException e) {
			dataRight = false;
		}

		// Checks that the jTextAreaAnswers are not empty and the jRadioButtonTrue
		// or
		// jRadioButtonFalse and the jCheckBoxDeleteAnswer are selected
		if (dataRight == true) {
			for (int i = 0; i < numAnswers; i++) {
				// Checks if the answer will be removed or not
				if (jCheckBoxDeleteAnswer[i].isSelected()) {
					numAnswersDelete++;
					deleteAnswer = true;
				}
				// Checks that the jTextAreaAnswers are not empty
				if (jTextAreaAnswers[i].getText().equals("") && !jCheckBoxDeleteAnswer[i].isSelected()) {
					dataRight = false;
					break;
				} else {
					dataRight = true;
				}

				// Checks that the jRadioButtonTrue or jRadioButtonFalse are
				// selected
				if (!jRadioButtonTrue[i].isSelected() && !jRadioButtonFalse[i].isSelected()
						&& !jCheckBoxDeleteAnswer[i].isSelected()) {
					dataRight = false;
					break;
				} else {
					dataRight = true;

					if (jRadioButtonTrue[i].isSelected()) {
						answersCorrect++;
					}
				}
			}
		}

		// If the data is not correct, the shows a error message
		if (dataRight == false) {
			message = "You should complete all the data.";
			JOptionPane.showMessageDialog(this, message, "Data Error", JOptionPane.ERROR_MESSAGE);
		} else {
			if (answersCorrect == 0) {
				message = "At least a question should be correct";
				JOptionPane.showMessageDialog(this, message, "Data Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Checks if any answer will be removed from the question
			if (deleteAnswer == true) {
				// Calculates the number of questions to delete
				if ((numAnswers - numAnswersDelete) < NUMBER_OF_ANSWERS_MIN) {
					// Message to indicate the user that the chosen answers can not
					// be removed
					// because there must be 2 answers at least
					message = "ERROR: The selected answers cannot be eliminated." + "\n";
					message += "The minimum number of answers should be 2.";
					JOptionPane.showMessageDialog(this, message, "Data Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			// Builds the Question object that will be sent to the server
			Question question = new Question();
			question.setCourse(courseName);
			question.setQuestionsFileName(questionsFileName);
			question.setConcept(concept);
			question.setCodeQuestion(codeQuestion);
			question.setExhibitionRate(Double.valueOf(jTextFieldExhibitionRate.getText().trim()).doubleValue());
			question.setAnswerTime(Integer.valueOf(jTextFieldAnswerTime.getText().trim()).intValue());
			question.setSuccessRate(Double.valueOf(jTextFieldSuccessRate.getText().trim()).doubleValue());
			question.setEnunciate(jTextAreaEnunciate.getText().trim());
			question.setExistImage(String.valueOf(existImage));

			if (existImage == true) {
				if (imageModified == true) {
					question.setPathImage(imageFile.getName().substring(imageFile.getName().lastIndexOf("."))
							.toLowerCase());
				} else {
					question.setPathImage(pathImage.substring(pathImage.lastIndexOf(".")));
				}
			}

			question.setDifficulty(Double.valueOf(jTextFieldDifficulty.getText().trim()).doubleValue());
			question.setDiscrimination(Double.valueOf(jTextFieldDiscrimination.getText().trim()).doubleValue());

			Vector textAnswers = new Vector(numAnswers);
			Vector correctAnswers = new Vector(numAnswers);
			Vector textExplanation = new Vector(numAnswers);

			for (int i = 0; i < numAnswers; i++) {
				if (jCheckBoxDeleteAnswer[i].isSelected() == false) {
					textAnswers.add(jTextAreaAnswers[i].getText().trim());

					if (jRadioButtonTrue[i].isSelected() == true) {
						correctAnswers.add("true");
					} else {
						correctAnswers.add("false");
					}

					textExplanation.add(jTextAreaExplanation[i].getText().trim());

				}
			}
			question.setTextAnswers(textAnswers);
			question.setCorrectAnswers(correctAnswers);
			question.setTextExplanation(textExplanation);

			numAnswers = numAnswers - numAnswersDelete;

			// Calculates the guessing of the question
			double guessing = 1.0 / Double.valueOf(String.valueOf(numAnswers)).doubleValue();
			question.setGuessing(guessing);
			question.setNumberOfAnswers(numAnswers);

			// Waiting cursor
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// Makes the update
			message = parent.modifyQuestion(question, imageFile);

			// If the response from the server is null, shows a error message
			if (message == null) {
				questionSaved = false;
				message = "ERROR: Not modified question." + "\n";
				message += "There was an error when connecting with the server.";
				JOptionPane.showMessageDialog(this, message, "Result of saving the question",
						JOptionPane.ERROR_MESSAGE);

			} else if (message.trim().indexOf(TestEditor.TEXT_QUESTION_MODIFIED) >= 0) {
				// Shows the result
				JOptionPane.showMessageDialog(this, message, "Result of the modification of the question", 1);

				// Requests the information of the question to the server
				question = parent.getQuestionByCode(courseName, questionsFileName, codeQuestion);

				// Updates the number of answers with the value of the question
				// returned by the server
				numAnswers = question.getNumberOfAnswers();

				// Puts the data in the window
				repaintFrmQuestionData();
				setQuestionData(question, codeBase);

				questionSaved = true;

			} else {
				// Shows the message returned by the server
				JOptionPane.showMessageDialog(this, message, "Result of the modification of the question", 1);
			}

			// Default cursor
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/*
	 * NOMBRE: jButtonCancelActionPerformed. PERTENECE A: Clase FrmEditQuestion.
	 * LLAMADA POR: Al hacer click sobre el jButtonCancel. LLAMA A: nada. RECIBE:
	 * e: Objeto de tipo ActionEvent. Contiene el evento que produjo la llamada a
	 * est� m�todo. DEVUELVE: void. FUNCI�N: Cierra el frame.
	 */

	void jButtonCancelActionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();
	}

	/*
	 * NOMBRE: jButtonClearActionPerformed. PERTENECE A: Clase FrmEditQuestion.
	 * LLAMADA POR: Al hacer click sobre el jButtonClear. LLAMA A: nada. RECIBE:
	 * e: Objeto de tipo ActionEvent. Contiene el evento que produjo la llamada a
	 * est� m�todo. DEVUELVE: void. FUNCI�N: Vac�a o limpia el contenido de los
	 * componentes del frame.
	 */

	void jButtonClearActionPerformed(ActionEvent e) {
		// Borrado del contenido de jTextAreaEnunciate.
		jTextAreaEnunciate.setText("");

		jButtonDeleteImageActionPerformed();

		// Establecimiento de los jComboBoxDifficulty y jComboBoxDiscrimination en
		// sus valores por defecto.
		if (type == TestEditor.MODIFY_QUESTION_OF_A_FILE) {
			jTextFieldDifficulty.setText(String.valueOf(DIFFICULTY_NORMAL));
			jTextFieldDiscrimination.setText(String.valueOf(DISCRIMINATION_NORMAL));
			double guessing = 1.0 / Double.valueOf(String.valueOf(numAnswers)).doubleValue();
			jTextFieldGuessing.setText(String.valueOf(guessing));
		}

		// Bucle para el borrado del contenido de los jTextAreaAnswers y los
		// jTextAreaExplanation y la deselecci�n de lo jRadioButtonTrue y
		// jRadioButtonFalse.
		for (int i = 0; i < numAnswers; i++) {
			jTextAreaAnswers[i].setText("");
			jRadioButtonAnswer[i].remove(jRadioButtonTrue[i]);
			jRadioButtonAnswer[i].remove(jRadioButtonFalse[i]);
			jRadioButtonTrue[i].setSelected(false);
			jRadioButtonFalse[i].setSelected(false);
			jRadioButtonAnswer[i].add(jRadioButtonTrue[i]);
			jRadioButtonAnswer[i].add(jRadioButtonFalse[i]);
			jTextAreaExplanation[i].setText("");
		}

		DIFFICULTY_SELECT = DIFFICULTY_NORMAL;
		DISCRIMINATION_SELECT = DISCRIMINATION_NORMAL;
		GUESSING_SELECT = 1.0 / Double.valueOf(String.valueOf(numAnswers)).doubleValue();

		parent.DIFFICULTY_SELECT = parent.DIFFICULTY_NORMAL;
		parent.DISCRIMINATION_SELECT = parent.DISCRIMINATION_NORMAL;
		parent.GUESSING_SELECT = 1.0 / Double.valueOf(String.valueOf(numAnswers)).doubleValue();
	}

	/*
	 * NOMBRE: jButtonAddAnswerActionPerformed. PERTENECE A: Clase
	 * FrmEditQuestion. LLAMADA POR: Al hacer click sobre el jButtonAddAnswer.
	 * LLAMA A: repaintFrmQuestionData(); setQuestionData(); RECIBE: e: Objeto de
	 * tipo ActionEvent. Contiene el evento que produjo la llamada a est� m�todo.
	 * DEVUELVE: void. FUNCI�N: Se encarga de reinicializar los componenes del
	 * frame para poder introducir los datos de otra respuesta a la
	 * pregunta.
	 */

	void jButtonAddAnswerActionPerformed(ActionEvent e) {
		// Comprobaci�n de que al a�adir mas respuestas no se supera el n�mero
		// m�ximo establecido en 9.
		if ((numAnswers + 1) > NUMBER_OF_ANSWERS_MAX) {
			// Mensaje para indicar al usuario que no se pueden a�adir m�s
			// respuestas.
			JOptionPane.showMessageDialog(this, "More answers cannot be added." + "\n"
					+ "The allowed maximum number is " + String.valueOf(NUMBER_OF_ANSWERS_MAX),
					"Result of the modification of the question", JOptionPane.WARNING_MESSAGE);

			return;
		}

		if (type == TestEditor.MODIFY_QUESTION_OF_A_FILE) {
			// Reserva de memoria las variables que almacenar�n los valores
			// que se encontraban en los componentes del frame antes de
			// reinicializarlo.
			Question questionTemp = new Question();

			Vector textAnswers = new Vector(numAnswers);
			Vector correctAnswers = new Vector(numAnswers);
			Vector textExplanation = new Vector(numAnswers);

			// C�lculo del nuevo n�mero de respuestas.
			// numAnswers++;

			// Obtencion de los valores de la pregunta.
			questionTemp.setCodeQuestion(codeQuestion);
			questionTemp.setEnunciate(jTextAreaEnunciate.getText().trim());
			questionTemp.setPathImage(pathImage);
			questionTemp.setDifficulty(Double.valueOf(jTextFieldDifficulty.getText().trim()).doubleValue());
			questionTemp.setDiscrimination(Double.valueOf(jTextFieldDiscrimination.getText().trim())
					.doubleValue());

			// Calculo de la nueva adivinanza de la pregunta.
			double guessing = 1.0 / Double.valueOf(String.valueOf(numAnswers)).doubleValue();
			questionTemp.setGuessing(guessing);

			// Imagen de la pregunta.
			questionImageIcon = (ImageIcon) jLabelImage.getIcon();

			// Bucle para la lectura de los valores que contienen los
			// jTextAreaAnswers, jTextAreaExplanation y jRadioButtonTrue o
			// jRadioButtonFalse.
			for (int i = 0; i < numAnswers; i++) {
				textAnswers.add(jTextAreaAnswers[i].getText().trim());

				if (jRadioButtonTrue[i].isSelected())
					correctAnswers.add("true");
				else if (jRadioButtonFalse[i].isSelected())
					correctAnswers.add("false");
				else
					correctAnswers.add("no selected");

				textExplanation.add(jTextAreaExplanation[i].getText().trim());
			}
			questionTemp.setTextAnswers(textAnswers);
			questionTemp.setCorrectAnswers(correctAnswers);
			questionTemp.setTextExplanation(textExplanation);
			questionTemp.setTotalQuestionsInFile(Integer
					.valueOf(jTextFieldNumberQuestionOfFile.getText().trim()).intValue());

			// Calculo del orden que ocupa la pregunta que se est� modificando
			// dentro del fichero.
			if (jButtonPrevious.isEnabled() == true && jButtonNext.isEnabled() == true)
				questionTemp.setFirstLastQuestion("NoLastNoFirst");
			else if (jButtonPrevious.isEnabled() == true && jButtonNext.isEnabled() == false)
				questionTemp.setFirstLastQuestion("last");
			else if (jButtonPrevious.isEnabled() == false && jButtonNext.isEnabled() == true)
				questionTemp.setFirstLastQuestion("first");
			else if (jButtonPrevious.isEnabled() == false && jButtonNext.isEnabled() == false)
				questionTemp.setFirstLastQuestion("one question");

			questionTemp.setNumberOfAnswers(numAnswers);
			questionTemp.setNumberQuestionOrder(numQuestionShow);
			questionTemp.setExhibitionRate(Double.valueOf(jTextFieldExhibitionRate.getText().trim())
					.doubleValue());
			questionTemp.setAnswerTime(Integer.valueOf(jTextFieldAnswerTime.getText().trim()).intValue());
			questionTemp.setSuccessRate(Double.valueOf(jTextFieldSuccessRate.getText().trim()).doubleValue());

			Vector classicTestVector = new Vector();
			for (int i = 0; i < jComboBoxClassicTest.getItemCount(); i++)
				classicTestVector.add(jComboBoxClassicTest.getItemAt(i).toString());

			Vector adaptiveTestVector = new Vector();
			for (int i = 0; i < jComboBoxAdaptiveTest.getItemCount(); i++)
				adaptiveTestVector.add(jComboBoxAdaptiveTest.getItemAt(i).toString());

			questionTemp.setClassicTestVector(classicTestVector);
			questionTemp.setAdaptiveTestVector(adaptiveTestVector);

			// Nuevo valor para numAnswers.
			numAnswers++;

			// Reinicializaci�n de los componentes del frame.
			repaintFrmQuestionData();

			// Asiganci�n del n�mero de respuestas antiguo, para poder poner en los
			// componenes del frame los valores antiguos.
			numAnswers--;

			// Asignaci�n de los valores antiguos a los componentes del
			// frame.
			setQuestionData(questionTemp, codeBase);

			// Asignaci�n del n�mero de respuestas actual.
			numAnswers++;

			// Calculo del nuevo guessing.
			guessing = 1.0 / Double.valueOf(String.valueOf(numAnswers)).doubleValue();
			jTextFieldGuessing.setText(String.valueOf(guessing));
		} else {
			String enunciate = ""; // Almacena el texto del enunciado de la
			// pregunta.
			// String pathImage = ""; //Almacena el path de la imagen asociada.
			Vector textAnswers; // Almacena todos los textos de las respuestas de
			// la
			// pregunta.
			Vector correctAnswers; // Almacena si las respuestas de la pregunta
			// son
			// verdaderas o falsas.
			Vector textExplanation; // Almacena los textos de las explicaciones de
			// las
			// respuestas de la pregunta.

			// double guessingOld = guessing;

			// Lectura de los datos que se encuntran actualmente en el
			// frame.
			enunciate = jTextAreaEnunciate.getText().trim();

			// Reserva de memoria las variables que almacenar�n los valores
			// que se encontraban en los componentes del frame antes de
			// reinicializarlo.
			textAnswers = new Vector(numAnswers);
			correctAnswers = new Vector(numAnswers);
			textExplanation = new Vector(numAnswers);

			// Bucle para la lectura de los valores que contienen los
			// jTextAreaAnswers, jTextAreaExplanation y jRadioButtonTrue o
			// jRadioButtonFalse.
			for (int i = 0; i < numAnswers; i++) {
				textAnswers.add(jTextAreaAnswers[i].getText().trim());

				if (jRadioButtonTrue[i].isSelected())
					correctAnswers.add("true");
				else if (jRadioButtonFalse[i].isSelected())
					correctAnswers.add("false");
				else
					correctAnswers.add("no selected");

				textExplanation.add(jTextAreaExplanation[i].getText().trim());
			}

			// Nuevo valor para numAnswersInt.
			numAnswers++;

			// Reinicializaci�n de los componentes del frame.
			repaintFrmQuestionData();

			// Establecimiento de los valores leidos antes de la reinicializaci�n
			// de
			// los componentes del frame, en sus componentes
			// correspondientes.
			jTextAreaEnunciate.setText(enunciate);

			if (questionImageIconTemp != null)
				scaledImageIcon();

			jLabelImage.setIcon(questionImageIcon);

			// Habilitaci�n del jButtonDeleteImage.
			if (questionImageIcon != null)
				jButtonDeleteImage.setEnabled(true);
			else
				jButtonDeleteImage.setEnabled(false);

			// String guessingNew =
			// String.valueOf(1/Double.valueOf(String.valueOf(numAnswers)).doubleValue());

			guessing = 1 / Double.valueOf(String.valueOf(numAnswers)).doubleValue();

			/*
			 * //Mensaje para confirmar el la nueva adivinanza de la pregunta. int
			 * result = JOptionPane.showConfirmDialog(this, "The guessing
			 * calculated for the question is " + String.valueOf(guessingNew) + "." +
			 * "\nDo you want to conserve the old one?", "New Guessing",
			 * JOptionPane.YES_NO_OPTION);
			 * 
			 * if(result == JOptionPane.NO_OPTION) guessing =
			 * Double.valueOf(guessingNew).doubleValue(); else { guessing =
			 * guessingOld; GUESSING_NORMAL = guessing; GUESSING_SELECT = guessing;
			 * TestEditor.GUESSING_SELECT = guessing; TestEditor.GUESSING_NORMAL =
			 * guessing; }
			 */

			// Bucle para asignar los valores de las respuestas.
			for (int i = 0; i < numAnswers - 1; i++) {
				jTextAreaAnswers[i].setText(textAnswers.get(i).toString());

				if (correctAnswers.get(i).toString().equals("true"))
					jRadioButtonTrue[i].setSelected(true);
				else if (correctAnswers.get(i).toString().equals("false"))
					jRadioButtonFalse[i].setSelected(true);

				jTextAreaExplanation[i].setText(textExplanation.get(i).toString());
			}
		}

		try {
			JPanelInit();
			frameInit();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error to load image " + ex.getMessage(), "ERROR",
					JOptionPane.ERROR_MESSAGE);

			return;
		}
		questionModified = true;
	}

	/*
	 * NOMBRE: jButtonDeleteActionPerformed. PERTENECE A: Clase FrmEditQuestion.
	 * LLAMADA POR: Al hacer click sobre el jButtonDelete. LLAMA A:
	 * deleteQuestion(); getQuestionData(); repaintFrmQuestionData();
	 * setQuestionData(); RECIBE: nada. DEVUELVE: void. FUNCI�N: Llama al m�todo
	 * de esta misma clase que envia al servidor la informaci�n necesaria para
	 * que elimine una pregunta del fichero de preguntas. Si esta operaci�n se ha
	 * realizado con �xito, entonces se muestra la pregunta previa a la que se
	 * habia borrado.
	 */

	void jButtonDeleteActionPerformed(ActionEvent e) {
		// Mensaje para confirmar el borrado de la pregunta.
		int result = JOptionPane.showConfirmDialog(this,
				"Do you really want to delete the question from the file?", "Delete Question",
				JOptionPane.YES_NO_OPTION);

		// Si la respuesta al mensaje es afirmativa, se procede al borrado de la
		// pregunta.
		if (result == JOptionPane.YES_OPTION) {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			// Llamada al m�todo de esta misma clase que env�a al servidor los
			// datos
			// necesarios para que elimine una pregunta del fichero.
			String resultDeleteQuestion = parent.deleteQuestion(courseName, questionsFileName,
					codeQuestion);

			// Si la respuesta a la petici�n no es null, se intenta mostrar la
			// pregunta
			// previa a la que se ha eliminado.
			if (resultDeleteQuestion != null) {
				// Mostrado de la respuesta del servidor a la petici�n de borrado de
				// la
				// pregunta.
				JOptionPane.showMessageDialog(this, resultDeleteQuestion, "Result", 1);

				if (!resultDeleteQuestion.startsWith(TestEditor.TEXT_QUESTION_DELETED)) {
					// Cursor estandar.
					super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				} else {
					// Obtenci�n de todos los frame que se est�n ejecutando.
					JInternalFrame [] jInternalFrames = parent.getDesktopPane().getAllFrames();

					// Bucle para la comprobaci�n de cuales de los frame
					// anteriores est�n usando el fichero que se va a eliminar.
					for (int i = 0; i < jInternalFrames.length; i++) {
						try {
							FrmEditTest frmEditTest = (FrmEditTest) jInternalFrames[i];

							if (frmEditTest.courseName.equals(courseName)) {
								if (frmEditTest.checkQuestionInTest(questionsFileName, codeQuestion))
									frmEditTest.deleteQuestion(questionsFileName, codeQuestion);
							}
						} catch (java.lang.ClassCastException e1) {}
					}
				}

				// Calculo del nuevo n�mero de pregunta a mostrar, seg�n el orden
				// que
				// ocupa en el fichero.
				numQuestionShow--;
				if (numQuestionShow < 0)
					numQuestionShow = 0;

				// Llamada al m�todo de esta misma clase que env�a los datos
				// necesarios
				// al servidor para que env�e de vuelta los datos de una pregunta
				// del
				// fichero.
				Question question = parent.getQuestionByOrder(courseName, questionsFileName,
						numQuestionShow);

				// Si la respuesta a la petici�n es null, se muestra un mensaje de
				// error.
				if (question == null) {
					JOptionPane.showMessageDialog(this, "Error when reading course or question file." + "\n"
							+ "It is possible that there are not more questions in the file.",
							"Error to load the next question", JOptionPane.WARNING_MESSAGE);
					dispose();
				} else {
					// Modificamos el n�mero de respuestas asignandole el
					// correspondiente
					// a la pregunta devuelta por el servlet.
					numAnswers = question.getNumberOfAnswers();

					// Reestructuraci�n de los componentes del frame y
					// establecimiento en los mismos de los datos de la pregunta
					// obtenida
					// previamente.
					repaintFrmQuestionData();
					setQuestionData(question, codeBase);
				}
			} else {
				// Mensaje de error indicando que no se ha podido eliminar la
				// pregunta
				// del fichero.
				JOptionPane.showMessageDialog(this, "ERROR: The question has not been eliminated," + "\n"
						+ "there was an error when connecting with the server.", "Error Delete Question",
						JOptionPane.ERROR_MESSAGE);
			}

			// Cursor estandar.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/*
	 * NOMBRE: jButtonSaveInOtherFileActionPerformed. PERTENECE A: Clase
	 * FrmEditQuestion. LLAMADA POR: Al hacer click sobre el
	 * jButtonSaveInOtherFile. LLAMA A: jButtonSaveActionPerformed();
	 * movedImage(); getQuestionsFilesList(); RECIBE: e: Objeto de tipo
	 * ActionEvent. Contiene el evento que produjo la llamada a este m�todo.
	 * DEVUELVE: void. FUNCI�N: Solicita al usuario el nombre de un curso y de un
	 * fichero de preguntas, comprueba los datos y llama al los m�todos de esta
	 * misma clase necesarios para copiar la pregunta en otro fichero.
	 */

	void jButtonSaveInOtherFileActionPerformed(ActionEvent e) {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Reserva de memoria para el objeto Vector que contendr�
		// el nombre de todos los ficheros de preguntas de los cursos de los
		// que el autor es propietario.
		Vector courseVector = new Vector();

		// Llamada al m�todo de esta misma clase que env�a al servidor los datos
		// necesarios para que devuelta los nombres de los ficheros de preguntas
		// de los cursos de los que el usuario es propietario.
		courseVector = parent.getQuestionsFileNames("");

		// Cursor estandar.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		String newCourse; // Almacena el nombre del curso destino.
		String newQuestionsFileName; // Almacena el nombre del fichero de
		// preguntas
		// destino.

		// Si se obtiene los nombres de los ficheros de preguntas de los
		// cursos pertenecientes sin nig�n problema, entonces se intenta
		// copiar la pregunta a otro fichero.
		if (courseVector != null) {
			// Reserva de memoria y muestra del JDialog que pide al usuario
			// el nombre del curso destino y el fichero de preguntas destino.
			DlgSelectCourseAndQuestionsFile dlgCourseAndQuestionsFile = new DlgSelectCourseAndQuestionsFile(
					"Select Course and Question File", courseVector, null);

			if (dlgCourseAndQuestionsFile.id == true) {
				// Cursor en espera.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				// Obtenci�n de los datos introducidos por el usuario en el JDialog
				// anterior.
				newCourse = (String) dlgCourseAndQuestionsFile.jComboCourse.getSelectedItem();
				newQuestionsFileName = (String) dlgCourseAndQuestionsFile.jComboQuestionsFile.getSelectedItem();

				dlgCourseAndQuestionsFile = null;

				if (newQuestionsFileName.equals(questionsFileName) && newCourse.equals(courseName)) {
					JOptionPane.showMessageDialog(this, "It cannot saves the question. " + "\n"
							+ "The file destination is the same one " + "that the current one.",
							"Error Save Question", JOptionPane.ERROR_MESSAGE);
				} else {
					// Intercambio de los datos introducidos por el usuario en el
					// JDialog
					// anterior.
					String courseNameOld = courseName;
					String questionsFileNameOld = questionsFileName;
					courseName = newCourse;
					questionsFileName = newQuestionsFileName;

					// Llamada al m�todo de esta misma clase que se encarga de enviar
					// los datos necesarios al servidor para que almacene la pregunta
					// en su nuevo destino.
					saveInOtherFile = true;
					jButtonSaveActionPerformed();
					saveInOtherFile = false;

					// Nuevo intercambio de los datos introducidos por el usuario en
					// el
					// JDialog, para dejarlo en su estado original.
					courseName = courseNameOld;
					questionsFileName = questionsFileNameOld;
					newCourse = null;
					newQuestionsFileName = null;
					courseNameOld = null;
					questionsFileNameOld = null;
				}

				// Cursor estandar.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	/*
	 * NOMBRE: jButtonAddQuestionToClassicTestActionPerformed. PERTENECE A: Clase
	 * FrmEditQuestion. LLAMADA POR: Al hacer click sobre el
	 * jButtonAddQuestionToClassicTest. LLAMA A:
	 * 
	 * RECIBE: executionType: Objeto de tipo String. Indica si el test al que se
	 * va a a�adir la pregunta es cl�sico o adaptativo. Puede tener dos valores
	 * "classic" o "adaptive". DEVUELVE: void. FUNCI�N: Solicita al usuario el
	 * nombre de un curso y de un fichero de test, comprueba los datos y llama al
	 * los m�todos de esta misma clase necesarios para a�adir la pregunta en
	 * dicho fichero de test.
	 */

	void jButtonAddQuestionToTestActionPerformed(String executionType) {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Llamada al m�todo de la clase padre que env�a al servidor los datos
		// necesarios para que devuelta los nombres de los ficheros de test
		// de los cursos de los que el usuario es propietario.
		Vector courseVector = parent.getTestsFileNames(courseName, executionType);

		// String newCourseName; //Almacena el nombre del curso destino.
		String testFileName; // Almacena el nombre del fichero de test
		// destino.

		// Si se obtiene los nombres de los ficheros de test de los
		// cursos pertenecientes sin nig�n problema, entonces se intenta
		// copiar la pregunta.
		if (courseVector != null) {
			// Reserva de memoria y muestra del JDialog que pide al usuario
			// el nombre del curso destino y el fichero de preguntas destino.
			DlgSelectCourseAndTestFile dlgCourseAndTestFile = new DlgSelectCourseAndTestFile("Select Test File",
					courseVector, null);

			if (dlgCourseAndTestFile.id == true) {
				// Obtenci�n de los datos introducidos por el usuario en el JDialog
				// anterior.
				// newCourseName =
				// (String)dlgCourseAndTestFile.jComboCourse.getSelectedItem();
				testFileName = (String) dlgCourseAndTestFile.jComboTestFile.getSelectedItem();
				dlgCourseAndTestFile = null;

				String addQuestion = parent.addQuestionToTest(courseName, questionsFileName,
						codeQuestion, testFileName, executionType);

				// Si la respuesta del servidor es null, se prepara un mensaje de
				// error.
				if (addQuestion == null) {
					addQuestion = "ERROR: The question has NOT been ADDED to the test." + "\n"
							+ "There was an error when connecting to the server.";
				} else {
					// Obtenci�n de todos los frame que se est�n ejecutando.
					JInternalFrame [] jInternalFrames = parent.getDesktopPane().getAllFrames();

					// Bucle para la comprobaci�n de cuales de los frame
					// anteriores est�n usando el fichero que se va a eliminar.
					for (int i = 0; i < jInternalFrames.length; i++) {
						try {
							FrmEditTest frmEditTest = (FrmEditTest) jInternalFrames[i];

							if (frmEditTest.courseName.equals(courseName)) {
								if (frmEditTest.testFileName.equals(testFileName)) {
									if (frmEditTest.executionTypeString.trim().equals(executionType)
											&& frmEditTest.questionsOfMultipleFiles != null)
										frmEditTest.jButtonReloadActionPerformed();
								}
							}
						} catch (java.lang.ClassCastException e1) {}
					}
				}

				JOptionPane.showMessageDialog(this, addQuestion, "Add Question To Classic Test", 1);

				jTextFieldGotoQuestion.setText(codeQuestion);
				jButtonGoCodeActionPerformed();
			}
		} else {
			// Mensaje para indicar al usuario que se ha producido un error al
			// intentar
			// conseguir los nombres de los ficheros de test de los cursos.
			JOptionPane.showMessageDialog(this, "ERROR: There is not any test file to add quetions.",
					"Add Question To Test Error", JOptionPane.ERROR_MESSAGE);
		}

		// Cursor estandar.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: jButtonPreviousActionPerformed. PERTENECE A: Clase
	 * FrmEditQuestion. LLAMADA POR: Al hacer click sobre el jButtonPrevious.
	 * LLAMA A: jButtonModifyActionPerformed(); getQuestionData();
	 * repaintFrmQuestionData(); setQuestionData(); RECIBE: nada DEVUELVE: void.
	 * FUNCI�N: Llama al m�todo de esta propia clase encargado de enviar al
	 * servidor los datos necearios para que encuentre la pregunta, del fichero
	 * de preguntas actual, previa a la que se est� mostrando actualmente.
	 */
	void jButtonPreviousActionPerformed() {
		// Waiting cursor
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Checks if the current question has been modified but not saved.
		// If yes, then ask the user if he wants to save the changes before
		// showing the previous question
		if (questionSaved == false && questionModified == true) {
			// Ask the user if he wants to save the changes
			String message = "Do you want to save this question?";
			int result = JOptionPane
					.showConfirmDialog(this, message, "Save Question", JOptionPane.YES_NO_OPTION);

			// If yes, makes the update
			if (result == JOptionPane.YES_OPTION) {
				onClosing = true;

				// Calls the method that makes the update
				jButtonModifyActionPerformed();

				if (questionSaved == false) {
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}
			} else {
				questionModified = false;
				imageModified = false;
			}

			// Gets the information about the previous question
			numQuestionShow--;
			Question question = parent.getQuestionByOrder(courseName, questionsFileName,
					numQuestionShow);

			// If the server returns null, then a error message is showed
			if (question == null) {
				JOptionPane.showMessageDialog(this, "ERROR: Reading course or question file.",
						"Error loading question", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				// Updates the number of answers with the value of the question
				// returned
				// by the server
				numAnswers = question.getNumberOfAnswers();

				// Puts the data in the window
				repaintFrmQuestionData();
				setQuestionData(question, codeBase);
			}
		} else {
			// Gets the information about the previous question
			numQuestionShow--;
			Question question = parent.getQuestionByOrder(courseName, questionsFileName,
					numQuestionShow);

			// If the server returns null, then a error message is showed
			if (question == null) {
				JOptionPane.showMessageDialog(this, "ERROR: Error when reading course or question file.",
						"Error modifying question", JOptionPane.ERROR_MESSAGE);
				dispose();
			} else {
				// Update the number of answers with the value of the question
				// returned
				// by the server
				numAnswers = question.getNumberOfAnswers();

				// Puts the data in the window
				repaintFrmQuestionData();
				setQuestionData(question, codeBase);
			}
		}

		// Default cursor
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: jButtonNextActionPerformed. PERTENECE A: Clase FrmEditQuestion.
	 * LLAMADA POR: Al hacer click sobre el jButtonNext. LLAMA A:
	 * jButtonModifyActionPerformed(); getQuestionData();
	 * repaintFrmQuestionData(); setQuestionData(); RECIBE: nada DEVUELVE: void.
	 * FUNCI�N: Llama al m�todo de esta propia clase encargado de enviar al
	 * servidor los datos necearios para que encuentre la pregunta, del fichero
	 * de preguntas actual, siguente a la que se est� mostrando actualmente.
	 */
	void jButtonNextActionPerformed() {
		// Waiting cursor
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Checks if the question has been modified but not saved. If yes,
		// asks the user if he wants to save the changes before showing the next
		// question.
		if (questionSaved == false && questionModified == true) {
			// Ask the user to save the changes
			String message = "Do you want to save this question?";
			int result = JOptionPane
					.showConfirmDialog(this, message, "Save Question", JOptionPane.YES_NO_OPTION);

			// If yes, then makes the update
			if (result == JOptionPane.YES_OPTION) {
				onClosing = true;

				// Makes the update
				jButtonModifyActionPerformed();

				if (questionSaved == false) {
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}
			} else {
				questionModified = false;
				imageModified = false;
			}

			// Gets the data of the following question
			numQuestionShow++;
			Question question = parent.getQuestionByOrder(courseName, questionsFileName,
					numQuestionShow);

			// If the response from the server is null, then shows a error message
			if (question == null) {
				JOptionPane.showMessageDialog(this, "ERROR: Error when reading course or question file.",
						"Error to load question", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				// Updates the number of answers with the value of the question
				// returned by the server
				numAnswers = question.getNumberOfAnswers();

				// Puts the data on the window
				repaintFrmQuestionData();
				setQuestionData(question, codeBase);
			}
		} else {
			// Gets the data of the following question
			numQuestionShow++;
			Question question = parent.getQuestionByOrder(courseName, questionsFileName,
					numQuestionShow);

			// If the response from the server is null, then shows a error message
			if (question == null) {
				JOptionPane.showMessageDialog(this, "ERROR: Error when reading course or question file.",
						"Error to load question", JOptionPane.ERROR_MESSAGE);
				dispose();
			} else {
				// Updates the number of answers with the value of the question
				// returned by the server
				numAnswers = question.getNumberOfAnswers();

				// Puts the data on the window
				repaintFrmQuestionData();
				setQuestionData(question, codeBase);
			}
		}

		// Default cursor
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Called when the user push the closing button of the internal window.
	 * Checks if the question has been modified but no saved. If yes, then ask
	 * the user if he wants to save the changes before closing.
	 * 
	 * @return A code that indicates what to do with the internal window
	 */
	public int closingWindow() {
		// Checks if the question has been modified but no saved
		if (questionSaved == false && questionModified == true) {
			// If yes, asks the user if he wants to save the changes
			String message = "Do you want to save the question before exit?";
			int result = JOptionPane.showConfirmDialog(this, message, "Save Question",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				// The user wants to save the changes
				onClosing = true;
				if (type == TestEditor.MODIFY_QUESTION_OF_A_FILE) {
					// Updates the values of the question
					jButtonModifyActionPerformed();
				} else {
					// Saves the values of the new question
					jButtonSaveActionPerformed();
				}

				if (questionSaved == false) {
					// If there is any error when saving, the window is not closed
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
					return DO_NOTHING_ON_CLOSE;
				} else {
					setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					dispose();
					return DISPOSE_ON_CLOSE;
				}
			} else if (result == JOptionPane.NO_OPTION) {
				questionSaved = true;
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				dispose();
				return DISPOSE_ON_CLOSE;
			} else {
				if (result == JOptionPane.CANCEL_OPTION) {
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}
				return DO_NOTHING_ON_CLOSE;
			}
		} else {
			return DISPOSE_ON_CLOSE;
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	// ////////////////// METHODS PROPIOS DE LA CLASE FRMQUESTIONDATA
	// ///////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: setQuestionData. PERTENECE A: Clase FrmEditQuestion. LLAMADA POR:
	 * jButtonGoto, jButtonPrevious, jButtonNext, jButtonDelete, jButtonGotoCode
	 * y por el constructor de esta misma clase cuando este frame es
	 * inicializado para modificar o eliminar preguntas de un fichero. LLAMA A:
	 * nada. RECIBE: question: Objeto de tipo Vector. Contiene los datos de la
	 * pregunta que se va a mostrar. codeBase: Objeto de tipo URL. Contiene la
	 * URL del servidor donde se enviar�n los datos. DEVUELVE: void. FUNCI�N:
	 * M�todo que extrae los datos de del objeto Vector devuelto por el servidor
	 * y los coloca en su correspondiente componente del frame.
	 */

	public void setQuestionData(Question question, URL codeBase) {
		String firstLastQuestion = ""; // Indica si la pregunta ocupa, dentro
		// del
		// fichero de preguntas, el primer, �ltimo
		// lugar, o si es una pregunta intermedia
		// o si es la �nica pregunta del fichero.
		String exhibitionRate = ""; // Almacena la tasa de exposici�n de la
		// pregunta.
		String answerTime = ""; // Almacena el tiempo medio de respuesta de la
		// pregunta.
		String successRate = ""; // Almacena la tasa de acierto de la pregunta.
		String enunciate; // Almacena el enunciado de la pregunta.
		String difficulty; // Almacena la dificultad de la pregunta.
		String discrimination; // Almacena la discriminaci�n de la pregunta.
		String guessing; // Almacena la adivinanza de la pregunta.
		Vector textAnswers; // Almacena todos los textos de las respuestas de la
		// pregunta.
		Vector correctAnswers; // Almacena las indicaciones de si las respesutas
		// son correctas o no.
		Vector textExplanation; // Almacena todos los textos de las explicaciones
		// de las respuestas.
		Vector classicTestVector; // Almacena los nombres de los ficheros de test
		// cl�sicos a los que pertenece la pregunta.
		Vector adaptiveTestVector; // Almacena los nombres de los ficheros de test
		// adaptativos a los que pertenece la pregunta.

		// Obtencion de los valores de la pregunta.
		concept = question.getConcept();
		codeQuestion = question.getCodeQuestion();
		enunciate = question.getEnunciate();
		pathImage = question.getPathImage();
		difficulty = String.valueOf(question.getDifficulty());
		discrimination = String.valueOf(question.getDiscrimination());
		guessing = String.valueOf(question.getGuessing());

		textAnswers = question.getTextAnswers();
		correctAnswers = question.getCorrectAnswers();
		textExplanation = question.getTextExplanation();

		numberQuestionOfFile = String.valueOf(question.getTotalQuestionsInFile());
		firstLastQuestion = question.getFirstLastQuestion();
		numAnswers = question.getNumberOfAnswers();
		numQuestionShow = question.getNumberQuestionOrder();
		exhibitionRate = String.valueOf(question.getExhibitionRate());
		answerTime = String.valueOf(question.getAnswerTime());
		successRate = String.valueOf(question.getSuccessRate());
		classicTestVector = question.getClassicTestVector();
		adaptiveTestVector = question.getAdaptiveTestVector();

		// Inicializacion del numbero de preguntas en el fichero.
		jTextFieldNumberQuestionOfFile.setText(numberQuestionOfFile);

		// Inicializacion del numero de pregunta a mostrar (jLabelNumberQuestion).
		jTextFieldNumberOfQuestion.setText(String.valueOf(numQuestionShow + 1));
		jTextFieldCodeQuestion.setText(codeQuestion);

		// Inicializacion de los botones de navegaci�n.
		if (firstLastQuestion.equals("first") == true) {
			jButtonPrevious.setEnabled(false);
			jButtonNext.setEnabled(true);
		} else {
			if (firstLastQuestion.equals("last") == true) {
				jButtonPrevious.setEnabled(true);
				jButtonNext.setEnabled(false);
			} else {
				if (firstLastQuestion.equals("one question")) {
					jButtonPrevious.setEnabled(false);
					jButtonNext.setEnabled(false);
				} else {
					jButtonPrevious.setEnabled(true);
					jButtonNext.setEnabled(true);
				}
			}
		}

		// Inicializacion del texto de la pregunta.
		jTextAreaEnunciate.setText(enunciate);

		// Inicializacion de la imagen de la pregunta.
		if (pathImage.equals("") == false) {
			if (imageModified == false) {
				try {
					String petitionQuestionImage = wowPath + parent.getItemsPath() + pathImage;

					URL urlQuestionImage = new URL(codeBase, petitionQuestionImage);

					questionImageIconTemp = new ImageIcon(urlQuestionImage);

					// Llamada al m�todo que cambia el tama�o del imagenIcon.
					scaledImageIcon();

					JLabelImageInit();
					jLabelImage.setIcon(questionImageIcon);
					jButtonDeleteImage.setEnabled(true);
					existImage = true;
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this, "It is not possible to load the image: " + e.getMessage(),
							"Error on load image", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				try {
					// Repintado del jLabelImage.
					questionImageIconTemp = new ImageIcon(imageFile.getAbsolutePath());

					// Llamada al m�todo que cambia el tama�o del imagenIcon.
					scaledImageIcon();

					JLabelImageInit();
					jLabelImage.setIcon(questionImageIcon);
					JPanelInit();
					frameInit();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Error to load image " + ex.getMessage(), "ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			questionImageIconTemp = null;
			questionImage = null;
			questionImageIcon = null;
			JLabelImageInit();
			jLabelImage.setIcon(null);
			jButtonDeleteImage.setEnabled(false);
			existImage = false;
		}

		// Inicializacion de los parametros de la pregunta.
		jTextFieldDifficulty.setText(difficulty);
		jTextFieldDiscrimination.setText(discrimination);
		jTextFieldGuessing.setText(guessing);

		jTextFieldExhibitionRate.setText(exhibitionRate);
		jTextFieldAnswerTime.setText(answerTime);
		jTextFieldSuccessRate.setText(successRate);

		// Limipieza del contenido anterior de los JComboBox.
		jComboBoxClassicTest.removeAllItems();
		jComboBoxAdaptiveTest.removeAllItems();

		// Adici�n del contenido de los JComboBox.
		if (classicTestVector != null) {
			for (int i = 0; i < classicTestVector.size(); i++) {
				String testFileName = classicTestVector.get(i).toString();
				testFileName = testFileName.substring(testFileName.lastIndexOf("/") + 1);
				jComboBoxClassicTest.addItem(testFileName);
			}
		}

		if (adaptiveTestVector != null) {
			for (int i = 0; i < adaptiveTestVector.size(); i++) {
				String testFileName = adaptiveTestVector.get(i).toString();
				testFileName = testFileName.substring(testFileName.lastIndexOf("/") + 1);
				jComboBoxAdaptiveTest.addItem(testFileName);
			}
		}

		// Limpiar contenido de los jTextArea de las respuestas.
		for (int i = 0; i < numAnswers; i++) {
			jTextAreaAnswers[i].setText("");
			jTextAreaExplanation[i].setText("");
		}

		// Bucle para establecer el contenido de los jTextAreaAnswers, los
		// jTextAreaExplanation y los jRadioButtonTrue o jRadioButtonFalse.
		for (int i = 0; i < numAnswers; i++) {
			jTextAreaAnswers[i].setText(textAnswers.get(i).toString());

			if (correctAnswers.get(i).toString().equals("true"))
				jRadioButtonTrue[i].setSelected(true);
			else if (correctAnswers.get(i).toString().equals("false"))
				jRadioButtonFalse[i].setSelected(true);

			jTextAreaExplanation[i].setText(textExplanation.get(i).toString());
		}
		questionSaved = false;
		questionModified = false;
		imageModified = false;

		try {
			JPanelInit();
			frameInit();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error reload frame " + ex.getMessage(), "ERROR",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	/*
	 * NOMBRE: repaintFrmQuestionData. PERTENECE A: Clase FrmEditQuestion.
	 * LLAMADA POR: jButtonGoto, jButtonPrevious, jButtonNext, jButtonDelete,
	 * jButtonGotoCode y por el constructor de esta misma clase cuando este
	 * frame es inicializado para modificar o eliminar preguntas de un
	 * fichero. LLAMA A: JLabelInit(); JTextFieldInit(); JImageIconInit();
	 * JComboBoxInit(); JPanelInit(); JButtonAddAndDeleteImageInit();
	 * JRadioButtonInit(); ButtonGroupInit(); JTextAreaInit(); JButtonInit();
	 * frameInit(); RECIBE: nada. DEVUELVE: void. FUNCI�N: M�todo que se
	 * encarga de reinicializar los componentes de esta propia clase.
	 */

	public void repaintFrmQuestionData() {
		// Liberaci�n de memoria para los componentes del frame.
		jTextAreaEnunciate = null;
		jTextAreaAnswers = null;
		jTextAreaExplanation = null;

		jRadioButtonTrue = null;
		jRadioButtonFalse = null;

		jCheckBoxDeleteAnswer = null;

		jRadioButtonAnswer = null;

		jScrollPane = null;
		jScrollPaneEnunciate = null;
		jScrollPaneAnswers = null;
		jScrollPaneExplanation = null;
		jScrollPaneImage = null;

		jPanelContent = null;
		jPanelNumberQuestionOfFile = null;
		jPanelNavigation = null;
		jPanelGotoQuestion = null;
		jPanelNavigationButton = null;
		jPanelNumberQuestion = null;
		jPanelEnunciate = null;
		jPanelImage = null;
		jPanelImageButton = null;
		jPanelIrtParameters = null;
		jPanelDifficulty = null;
		jPanelDiscrimination = null;
		jPanelGuessing = null;
		jPanelExhibitionRate = null;
		jPanelAnswerTime = null;
		jPanelSuccessRate = null;
		jPanelAnswer = null;
		jPanelAnswersRadioButton = null;
		jPanelAnswers = null;
		panelLabelAnswer = null;
		panelLabelExplanation = null;
		jPanelButtonAdd = null;
		jPanelButtonModify = null;

		jLabelNumberQuestionOfFile = null;
		jLabelGotoQuestion = null;
		jLabelNumberQuestion = null;
		jLabelImage = null;
		jLabelAnswer = null;
		jLabelExplanation = null;
		jLabelDifficulty = null;
		jLabelDiscrimination = null;
		jLabelGuessing = null;
		jLabelExhibitionRate = null;
		jLabelAnswerTime = null;
		jLabelSuccessRate = null;

		jButtonGo = null;
		jButtonPrevious = null;
		jButtonNext = null;
		jButtonAddImage = null;
		jButtonDeleteImage = null;
		jButtonSave = null;
		jButtonCancel = null;
		jButtonClear = null;
		jButtonDelete = null;
		jButtonSaveInOtherFile = null;

		jFrameQuestionImage = null;

		jTextFieldNumberQuestionOfFile = null;
		jTextFieldNumberOfQuestion = null;
		jTextFieldCodeQuestion = null;
		jTextFieldGotoQuestion = null;
		jTextFieldDifficulty = null;
		jTextFieldDiscrimination = null;
		jTextFieldGuessing = null;
		jTextFieldExhibitionRate = null;
		jTextFieldAnswerTime = null;
		jTextFieldSuccessRate = null;

		boxLayoutPanelContent = null;
		boxLayoutPanelEnunciate = null;
		boxLayoutPanelImage = null;
		boxLayoutPanelAnswer = null;
		boxLayoutPanelAnswers = null;

		flowLayoutPanelAnswersRadioButton = null;
		flowLayoutPanelButtonAdd = null;
		flowLayoutPanelButtonModify = null;
		flowLayoutPanelLabelAnswer = null;
		flowLayoutPanelJlabelExplanation = null;
		flowLayoutPanelIrtParameters = null;
		flowLayoutPanelNavigationButton = null;
		flowLayoutPanelImageButton = null;
		flowLayoutPanelNumberQuestionOfFile = null;
		flowLayoutPanelGotoQuestion = null;
		flowLayoutPanelNumberQuestion = null;
		// flowLayoutPanelGotoQuestionCode = null;

		iconAddImage = null;
		iconDelete = null;
		iconPrevious = null;
		iconNext = null;
		iconSave = null;
		iconCancel = null;
		iconClear = null;

		// Nueva inicializaci�n de los componentes del frame.
		JLabelInit();
		JLabelImageInit();
		JTextFieldInit();
		JImageIconInit();
		JComboBoxInit();
		JButtonAddAndDeleteImageInit();
		JButtonInit();
		JRadioButtonInit();
		ButtonGroupInit();
		JCheckBoxInit();
		JTextAreaInit();
		JPanelInit();

		try {
			frameInit();
		} catch (java.lang.Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Repaint Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * NOMBRE: scaledImageIcon. PERTENECE A: Clase FrmEditQuestion. LLAMADA POR:
	 * jButtonAddImageActionPerformed, setQuestionData,
	 * jButtonAddAnswerActionPerformed. LLAMA A: nada. RECIBE: void. DEVUELVE:
	 * void. FUNCI�N: Cambia el tama�o de la imagen que se va a mostrar en el
	 * frame.
	 */

	public void scaledImageIcon() {
		questionImage = questionImageIconTemp.getImage();

		if (questionImageIconTemp.getIconWidth() >= questionImageIconTemp.getIconHeight()) {
			if (questionImageIconTemp.getIconWidth() > 500) {
				double scale = questionImageIconTemp.getIconWidth() / 500.0;

				questionImage = questionImage.getScaledInstance(500, Integer.valueOf(
						String.valueOf(Math.round(questionImageIconTemp.getIconHeight() / scale))).intValue(),
						Image.SCALE_AREA_AVERAGING);
			}
		} else {
			if (questionImageIconTemp.getIconHeight() > 500) {
				double scale = questionImageIconTemp.getIconHeight() / 500.0;

				questionImage = questionImage.getScaledInstance(Integer.valueOf(
						String.valueOf(Math.round(questionImageIconTemp.getIconWidth() / scale))).intValue(), 500,
						Image.SCALE_AREA_AVERAGING);
			}
		}
		questionImageIcon = new ImageIcon(questionImage);
	}

}