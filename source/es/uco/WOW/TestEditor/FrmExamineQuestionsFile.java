package es.uco.WOW.TestEditor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import es.uco.WOW.Utils.Question;

/**
 * <p>Title: Wow! TestEditor</p> <p>Description: Herramienta para la edicion
 * de preguntas tipo test adaptativas </p> <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/*
 * NOMBRE: FrmExamineQuestionsFile. FUNCION: frame que recibe en su
 * constructor entre otros par�metros el nombre de un curso propiedad del
 * usuario y el nombre de un fichero de preguntas de dicho curso. Este
 * frame solicita al servidor todos los datos de las preguntas
 * contenidas en el fichero de preguntas del curso pasados como parametros. Si
 * el servidor ha podido realizar la tarea solicitada, enviar� a este
 * frame los datos pedidos. Con estos datos este frame interno muestra
 * al usuario los datos de todas las preguntas de dicho fichero mediante un
 * JTree, que premite al usuario explorar todos los datos de las preguntas
 * simplemente desplegando las carpetas mostradas en el JTree. Ademas, esta
 * clase tambi�n permite modificar una pregunta del fichero. Si se selecciona el
 * c�digo de una pregunta, y se hace click en el bot�n "Modify or Delete
 * Question" se abrir� el frame de la clase FrmEditQuestion, el cual
 * permite realizar estas operaciones. LAST MODIFICATION: 06-02-2008
 */

public class FrmExamineQuestionsFile extends TestEditorInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userLogin; // Almacena el nombre de usuario.

	private String userPassword; // Almacena la contrase�a.

	private String courseName; // Almacena el nombre del curso sobre el que se
										// esta

	// actuando.
	private String questionsFileName; // Almacena el nombre del fichero de
												// preguntas

	// que se va a examinar.
	private URL codeBase; // Almacena la URL del servidor desde el que se ejecuta

	// este applet.
	private String numAnswersString; // Almacena el n�mero de respuestas de una

	// pregunta.
	private boolean ImageIconLoad = false; // Indica si los iconos de los botones han

	// sido cargados con �xito.
	private Vector questionVector = null; // Vector de objeto Question que almacena

	// todas las preguntas del fichero de
	// preguntas.
	private Vector questionsOfMultipleFilesVector;

	private String codeOfQuestionsToModify = ""; // Almacena un String que
																// contiene

	// el c�digo de la pregunta que
	// se ha seleccionado para
	// modificar. La estructura de
	// este String es la siguiente:
	// "Code of Question: n" siendo n
	// el c�digo de dicha pregunta.


	private int type = 0;

	// Componentes del frame.
	private JScrollPane jScrollPane;

	private JScrollPane jScrollPaneTree;

	private JScrollPane jScrollPaneTable;

	private JPanel jPanelContent; // JPanel principal del frame.

	private JPanel jPanelTree; // JPanel que contendr� al JTree.

	private JPanel jPanelButton; // JPanel que contendr� a los JButton.

	private JPanel jPanelTable;

	private JTree jTreeQuestionsFile; // Control JTree que mostrar� los datos de las

	// preguntas en forma de �rbol.

	private JSplitPane jSplitPane;

	private JTable jTable;

	private TableModel tableModel;

	private TableSorter tableSorter;

	private JButton jButtonShow;

	private JButton jButtonCancel;

	private BoxLayout boxLayoutPanelContent; // Layout para el jPanelContent.

	private BoxLayout boxLayoutPanelTree; // Layout para el jPanelTree.

	private FlowLayout flowLayoutPanelButton; // Layout del jPanelButton.

	private BoxLayout boxLayoutPanelTable;

	private ImageIcon iconShow32; // Icono del bot�n jButtonShow.

	private ImageIcon iconCancel32; // Icono del bot�n jButtonCancel.

	/*
	 * NOMBRE: FrmExamineQuestionsFile (Constructor de esta misma clase).
	 * PERTENECE A: Clase FrmExamineQuestionsFile. LLAMADA POR: Al invocar la
	 * clase. LLAMA A: JPanelInit(); JScrollPaneInit(); JImageIconInit();
	 * JTreeExamInit(); JTableExamInit(); JTreeFindInit(); JTableFindInit();
	 * JButtonInit(); ; RECIBE: title: Objeto de tipo String.
	 * Indica el t�tulo del frame. width: Objeto de tipo int. Indica la
	 * anchura inicial del frame. height: Objeto de tipo int. Indica la
	 * altura inicial del frame. userLogin: Objeto de tipo String.
	 * Indica el login del usuario. userPassword: Objeto de tipo String. Indica
	 * el password del usuario. courseName: Objeto de tipo String. Indica el
	 * nombre del curso sobre el que se est� actuando. questionsFileName: Objeto
	 * de tipo String. Contiene el nombre del fichero de preguntas que se va a
	 * examinar. questionsOfMultipleFilesVector: Objeto de tipo Vector. Almacena
	 * las preguntas que se le pasan a este frame cuando se inicializa
	 * para mostrar los resultados de una busqueda de preguntas. codeBase: Objeto
	 * de tipo URL. Contiene la URL del servidor desde donde se ha cargado el
	 * applet. wowPath: Objeto de tipo String. Contiene el path del sistema desde
	 * el que se descarg� el applet. iconTestEditorPath: Objeto de tipo String.
	 * Contiene el path del directorio que contiene los iconos usados por la
	 * interfaz gr�fica. : Objeto de tipo JFrame. Contiene el JFrame
	 * principal del applet y padre de este frame. type: Objeto de tipo
	 * int. Indica que tipo de aspecto y funcionalidad tendr� el frame,
	 * es decir, si se utilizar� para examinar un fichero de preguntas o para
	 * mostrar los resultados de una b�squeda de preguntas. DEVUELVE: nada.
	 * FUNCI�N: Constructor de la clase FrmExamineQuestionsFile. Se encarga de
	 * configurar el aspecto del frame, su funcionalidad, as� como de inicializar
	 * los componentes que componen el mismo.
	 */

	public FrmExamineQuestionsFile(String aTitle, int aWidth, int aHeight, String aUserLogin,
			String aUserPassword, String aCourseName, String aQuestionsFileName,
			Vector aQuestionsOfMultipleFilesVector, URL aCodeBase, String anWOWPath, String iconPath,
			TestEditor father, int aType) {
	//
		// Calls the parent constructor
		super(aTitle, aWidth, aHeight, father, anWOWPath, iconPath);
		// Sets user variables
		type = aType;
		userLogin = aUserLogin;
		userPassword = aUserPassword;
		courseName = aCourseName;
		questionsFileName = aQuestionsFileName;
		codeBase = aCodeBase;
		questionsOfMultipleFilesVector = aQuestionsOfMultipleFilesVector;
		
		try {
			if (type == TestEditor.EXAM_QUESTION_FILE) {
				// Obtenci�n de los datos de todas las preguntas del fichero de
				// preguntas
				questionVector = getQuestions(courseName, questionsFileName);

				// Si no se ha podido obtener los datos de las preguntas, se muestra
				// un
				// mensaje de error, en caso contrario se inicializan los
				// componentes del
				// frame.

				if (questionVector == null) {
					JOptionPane.showMessageDialog(this, "ERROR: Error when reading course or question file."
							+ "\n" + "It`s posible that the file doesn't have questions to view.",
							"Error to view questions", JOptionPane.ERROR_MESSAGE);

					dispose();
					return;
				}

				JTreeExamInit(questionVector);
				JTableExamInit(questionVector);
			} else if (type == TestEditor.FIND_QUESTIONS_OF_A_FILE) {
				questionsOfMultipleFilesVector = aQuestionsOfMultipleFilesVector;

				JTreeFindInit(questionsOfMultipleFilesVector);
				JTableFindInit(questionsOfMultipleFilesVector);
			}

			// Inicializaci�n de los componentes de la clase.
			JImageIconInit();
			JButtonInit();
			JPanelInit();

			frameInit();
			initFrame = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	// //////// INICIALIZACI�N DE LOS COMPONENTES DEL frame
	// ////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: JPanelInit PERTENECE A: Clase FrmExamineQuestionsFile. LLAMADA
	 * POR: El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa los JPanel del frame,
	 * reservando memoria para los mismos y estableciendo su color, su tipo de
	 * layout...
	 */

	private void JPanelInit() {
		// Reserva de memoria para los JPanel.
		jPanelContent = new JPanel();
		jPanelTree = new JPanel();
		jPanelButton = new JPanel();

		// Establecimiento del layout y color para jPanelContent.
		boxLayoutPanelContent = new BoxLayout(jPanelContent, BoxLayout.Y_AXIS);
		jPanelContent.setLayout(boxLayoutPanelContent);
		jPanelContent.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Reserva de memoria para jScrollPane.
		jScrollPane = new JScrollPane(jPanelContent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setBackground(Color.black);
		jScrollPane.setBorder(BorderFactory.createEmptyBorder());

		// Establecimiento del layout y color para jPanelTree.
		boxLayoutPanelTree = new BoxLayout(jPanelTree, BoxLayout.Y_AXIS);
		jPanelTree.setLayout(boxLayoutPanelTree);
		jPanelTree.setBorder(BorderFactory.createTitledBorder("File of Questions"));
		jPanelTree.setAlignmentX(CENTER_ALIGNMENT);

		// Reserva del JScrollPaneTree para el jTreeQuestionsFile.
		jScrollPaneTree = new JScrollPane(jTreeQuestionsFile, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneTree.setBorder(BorderFactory.createEmptyBorder());
		jPanelTree.add(jScrollPaneTree);

		// Reserva de memoria para el jPanelTable.
		jPanelTable = new JPanel();
		boxLayoutPanelTable = new BoxLayout(jPanelTable, BoxLayout.Y_AXIS);
		jPanelTable.setLayout(boxLayoutPanelTable);
		jPanelTable.setBorder(BorderFactory.createTitledBorder("File of Questions"));

		// Reserva de memoria para el jScrollPaneTable.
		jScrollPaneTable = new JScrollPane(jTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneTable.setBackground(Color.black);
		jScrollPaneTable.setBorder(BorderFactory.createEmptyBorder());

		jPanelTable.add(jScrollPaneTable);

		// Reserva de memoria e inicializaci�n para jSplitPane.
		jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, jPanelTree, jScrollPaneTable);// jPanelTable);
		jSplitPane.setOneTouchExpandable(true);
		jSplitPane.setDividerSize(10);
		jSplitPane.setDividerLocation(0.2);
		jSplitPane.setAlignmentX(CENTER_ALIGNMENT);
		jSplitPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		// Establecimiento del layout y color para jPanelButton.
		flowLayoutPanelButton = new FlowLayout(FlowLayout.LEFT);
		jPanelButton.setLayout(flowLayoutPanelButton);

		// Adici�n de los JButton al jPanelButton.
		jPanelButton.add(jButtonShow);
		jPanelButton.add(jButtonCancel);

		// Adici�n del jPanelButton al jPanelContent.
		jPanelContent.add(jSplitPane, 0);
		jPanelContent.add(jPanelButton, 1);
	}

	/*
	 * NOMBRE: JImageIconInit. PERTENECE A: Clase FrmExamineQuestionsFile.
	 * LLAMADA POR: El constructor de la clase a la que pertenece. LLAMA A: nada.
	 * RECIBE: void. DEVUELVE: void. FUNCI�N: Inicializa los iconos mostrados por
	 * los botones del frame.
	 */

	private void JImageIconInit() {
		// Path relativo de la situaci�n de los iconos buscados dentro del
		// sistema.
		String petitionShow32 = wowPath + iconTestEditorPath + "examine32.gif";
		String petitionCancel32 = wowPath + iconTestEditorPath + "cancel32.gif";

		// Conexi�n con el servidor.
		try {
			URL urlShow32 = new URL(codeBase, petitionShow32);
			URL urlCancel32 = new URL(codeBase, petitionCancel32);

			// Reserva de memoria para los ImageIcon.
			iconShow32 = new ImageIcon(urlShow32);
			iconCancel32 = new ImageIcon(urlCancel32);

			ImageIconLoad = true;
		} catch (java.net.MalformedURLException e) {
			ImageIconLoad = false;
		}
	}

	/*
	 * NOMBRE: JTreeExamInit. PERTENECE A: Clase FrmExamineQuestionsFile. LLAMADA
	 * POR: El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa el JTree y establece su
	 * contenido y funcionalidad.
	 */

	private void JTreeExamInit(Vector questionVector) {
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
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(questionsFileName);

		// Bucle para la lectura de los datos de todas las preguntas, devueltos
		// por
		// el servidor, e inicializaci�n de los nodos del JTree con dichos datos.
		for (int i = 0; i < questionVector.size(); i++) {
			// Reserva de memoria para el vector que almacenar� los datos de una
			// pregunta.
			Question question = (Question) questionVector.get(i);

			// Reserva de memoria y establecimiento de valor para los nodos
			// referentes
			// a los datos de la pregunta.
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
			// nodos
			// referentes a los datos de la pregunta.
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
				rootNode.add(conceptNode);

			rootNode.add(questionNode);
		}

		// Reserva de memoria para el JTree y establecimiento de su nodo ra�z.
		jTreeQuestionsFile = new JTree(rootNode);

		jTreeQuestionsFile.setCellRenderer(new CustomCellRenderer(questionsFileName, wowPath,
				iconTestEditorPath, codeBase));

		// Establecimiento del tama�o, tipo de letra y borde del JTree.
		jTreeQuestionsFile.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jTreeQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 14));
		jTreeQuestionsFile.setToolTipText("Press Alt + click with the left button of the mouse,"
				+ "\n on the data of a question, to modify it.");
		jTreeQuestionsFile.setScrollsOnExpand(true);
		jTreeQuestionsFile.setShowsRootHandles(true);

		// Establecimiento de la funcionalidad del JTree.
		jTreeQuestionsFile.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath treePath = jTreeQuestionsFile.getSelectionPath();

				if (treePath.getPathCount() >= 2) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getPathComponent(1);

					if (node.getChildCount() > 0) {
						codeOfQuestionsToModify = node.getFirstChild().toString();
						numAnswersString = String.valueOf(node.getChildAt(5).getChildCount());
						jButtonShow.setEnabled(true);
					} else
						jButtonShow.setEnabled(false);
				}
			}
		});

		jTreeQuestionsFile.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1 && e.isAltDown())
					jButtonShowActionPerformed();
			}
		});
	}

	/*
	 * NOMBRE: JTreeFindInit. PERTENECE A: Clase FrmExamineQuestionsFile. LLAMADA
	 * POR: Constructor de esta misma clase LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JTree y establece su contenido y
	 * funcionalidad.
	 */

	private void JTreeFindInit(Vector questionsOfMultipleFiles) {
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
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Question Files");

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
				// referentes
				// a los datos de la pregunta.
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
			rootNode.add(questionsFileNode);
		}

		// Reserva de memoria para el JTree y establecimiento de su nodo ra�z.
		jTreeQuestionsFile = new JTree(rootNode);
		jTreeQuestionsFile.setCellRenderer(new CustomCellRenderer("", wowPath, iconTestEditorPath, codeBase));

		// Establecimiento del tama�o, tipo de letra y borde del JTree.
		jTreeQuestionsFile.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jTreeQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 14));
		jTreeQuestionsFile.setScrollsOnExpand(true);
		jTreeQuestionsFile.setShowsRootHandles(true);

		// Establecimiento de la funcionalidad del JTree.
		jTreeQuestionsFile.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath treePath = jTreeQuestionsFile.getSelectionPath();

				if (treePath.getPathCount() >= 3) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getPathComponent(2);

					if (node.getChildCount() > 0) {
						codeOfQuestionsToModify = node.getFirstChild().toString();
						numAnswersString = String.valueOf(node.getChildAt(5).getChildCount());
						questionsFileName = node.getParent().toString();
						jButtonShow.setEnabled(true);
					} else
						jButtonShow.setEnabled(false);
				} else
					jButtonShow.setEnabled(false);
			}
		});

		jTreeQuestionsFile.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1 && e.isAltDown())
					jButtonShowActionPerformed();
			}
		});
	}

	/*
	 * NOMBRE: JTableExamInit. PERTENECE A: Clase FrmExamineQuestionsFile.
	 * LLAMADA POR: El constructor de la clase a la que pertenece. LLAMA A: nada.
	 * RECIBE: void. DEVUELVE: void. FUNCI�N: Inicializa el JTable y establece su
	 * contenido y funcionalidad.
	 */

	private void JTableExamInit(Vector questionVector) {
		Vector rowsVector = new Vector();
		Vector columnsVector = new Vector();

		columnsVector.add("Number Of Question");
		columnsVector.add("Code of Question");
		columnsVector.add("Difficulty");
		columnsVector.add("Discrimination");
		// columnsVector.add("Guessing");
		columnsVector.add("Exhibition Rate");
		columnsVector.add("Answer Time");
		columnsVector.add("Success Rate");
		columnsVector.add("Number Of Answers");
		columnsVector.add("Image");

		// Bucle para la lectura de los datos de todas las preguntas, devueltos
		// por
		// el servidor, e inicializaci�n de los nodos del JTree con dichos datos.
		for (int i = 0; i < questionVector.size(); i++) {
			// Reserva de memoria para el objeto Question.
			Question question = (Question) questionVector.get(i);

			Vector questionTableVector = new Vector();
			questionTableVector.add(String.valueOf(question.getNumberQuestionOrder() + 1));
			questionTableVector.add(question.getCodeQuestion());
			questionTableVector.add(String.valueOf(question.getDifficulty()));
			questionTableVector.add(String.valueOf(question.getDiscrimination()));
			// questionTableVector.add(String.valueOf(question.getGuessing()));
			questionTableVector.add(String.valueOf(question.getExhibitionRate()));
			questionTableVector.add(String.valueOf(question.getAnswerTime()));
			questionTableVector.add(String.valueOf(question.getSuccessRate()));
			questionTableVector.add(String.valueOf(question.getNumberOfAnswers()));

			if (question.getPathImage().trim().equals(""))
				questionTableVector.add(new Boolean(false));
			else
				questionTableVector.add(new Boolean(true));

			rowsVector.add(questionTableVector);
		}

		tableModel = new TableModel(rowsVector, columnsVector);
		tableSorter = new TableSorter(tableModel);
		jTable = new JTable(tableSorter);
		jTable.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jTable.setAutoscrolls(true);
		jTable.getColumnModel().getColumn(8).setCellEditor(new DefaultCellEditor(new JCheckBox()));

		tableSorter.addMouseListenerToHeaderInTable(jTable);

		// Establecimiento de la funcinalidad de jTable.
		jTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = jTable.getSelectedRow();
				int numQuestion = Integer.valueOf(jTable.getValueAt(row, 0).toString()).intValue();

				jTreeQuestionsFile.setSelectionRow(0);
				TreePath treePath = jTreeQuestionsFile.getSelectionPath();
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) treePath.getLastPathComponent();

				TreeNode treeNode = root.getChildAt(numQuestion);

				jTreeQuestionsFile.setSelectionPath(treePath.pathByAddingChild(treeNode));
			}
		});
	}

	/*
	 * NOMBRE: JTableFindInit. PERTENECE A: Clase FrmExamineQuestionsFile.
	 * LLAMADA POR: El constructor de la clase a la que pertenece. LLAMA A: nada.
	 * RECIBE: void. DEVUELVE: void. FUNCI�N: Inicializa el JTable y establece su
	 * contenido y funcionalidad.
	 */

	private void JTableFindInit(Vector questionsOfMultipleFiles) {
		Vector rowsVector = new Vector();
		Vector columnsVector = new Vector();

		columnsVector.add("Question File Name");
		columnsVector.add("Associate Concept");
		columnsVector.add("Number Of Question");
		columnsVector.add("Code of Question");
		columnsVector.add("Difficulty");
		columnsVector.add("Discrimination");
		// columnsVector.add("Guessing");
		columnsVector.add("Exhibition Rate");
		columnsVector.add("Answer Time");
		columnsVector.add("Success Rate");
		columnsVector.add("Number Of Answers");
		columnsVector.add("Image");

		// Bucle para la lectura de los datos de todas las preguntas, devueltos
		// por
		// el servidor, e inicializaci�n de los nodos del JTree con dichos datos.
		for (int k = 0; k < questionsOfMultipleFiles.size(); k++) {
			for (int i = 0; i < ((Vector) ((Vector) questionsOfMultipleFiles.get(k)).get(1)).size(); i++) {
				// Reserva de memoria para el objeto Question.
				Question question = (Question) ((Vector) ((Vector) questionsOfMultipleFiles.get(k)).get(1))
						.get(i);

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

				rowsVector.add(questionVector);
			}
		}

		tableModel = new TableModel(rowsVector, columnsVector);
		tableSorter = new TableSorter(tableModel);
		jTable = new JTable(tableSorter);
		jTable.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jTable.setAutoscrolls(true);
		tableSorter.addMouseListenerToHeaderInTable(jTable);

		// Establecimiento de la funcinalidad de jTable.
		jTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int childQuestionsFile = -1;
				int row = jTable.getSelectedRow();
				String questionsFileName = jTable.getValueAt(row, 0).toString();
				int numQuestion = Integer.valueOf(jTable.getValueAt(row, 2).toString()).intValue();

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
	 * NOMBRE: JButtonInit. PERTENECE A: Clase FrmExamineQuestionsFile. LLAMADA
	 * POR: El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa los JButton y establece su
	 * funcionalidad.
	 */

	private void JButtonInit() {
		// Si los iconos de los JButton han sido obtenidos correctamente, entonces
		// los JButton se inicializar�n con dicho iconos, sino, se har� sin ellos.
		if (ImageIconLoad == true) {
			// Reserva de memoria para los JButton con iconos.
			jButtonShow = new JButton("Show Question", iconShow32);
			jButtonCancel = new JButton("Cancel", iconCancel32);
		} else {
			// Reserva de memoria para los JButton sin iconos.
			jButtonShow = new JButton("Show Question");
			jButtonCancel = new JButton("Cancel");
		}

		// Deshabilitaci�n inicial del jButtonShow.
		jButtonShow.setEnabled(false);

		// Establecimiento de la funcionalidad del jButtonShow.
		jButtonShow.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonShowActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad del jButtonCancel.
		jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closingWindow();
			}
		});
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
	// //////// FUNCIONALIDADES DE LOS COMPONETES DEL frame
	// ////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: getQuestions. PERTENECE A: Clase FrmExamineQuestionsFile. LLAMADA
	 * POR: El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * courseName: Objeto de tipo String. Contiene el nombre del curso sobre el
	 * que se est� actuando. questionsFileName: Objeto de tipo String. Contiene
	 * el nombre del fichero de preguntas del que se van a obtener todos los
	 * datos de las preguntas para poder configurar el JTree. DEVUELVE: Array de
	 * objetos Vector. Cada posici�n del array contiene un Vector que contiene a
	 * su vez los datos de una de las preguntas del fichero de preguntas. Hay
	 * tanto objetos vector como preguntas tiene el fichero de preguntas.
	 * FUNCI�N: M�todo que invoca al m�todo de la clase padre "getQuestions" para
	 * obtener los datos de todas las preguntas contenidas en un fichero de
	 * preguntas.
	 */

	public Vector getQuestions(String courseName, String questionsFileName) {
		Vector questionVector; // Array de objetos Vector. Almacena los datos
		// de todas las preguntas del fichero de preguntas
		// que se quiere examinar.

		questionVector = parent.getQuestions(courseName, questionsFileName);
		// Devoluci�n de los datos enviados por el servidor.
		return questionVector;
	}

	/*
	 * NOMBRE: jButtonShowActionPerformed. PERTENECE A: Clase
	 * FrmExamineQuestionsFile. LLAMADA POR: Al hacer click sobre el jButtonShow.
	 * LLAMA A: nada. RECIBE: e: Objeto de tipo ActionEvent. Contiene el evento
	 * que produce la llamada al m�todo. DEVUELVE: void. FUNCI�N: M�todo que
	 * invoca un frame de la clase FrmEditQuestion para modificar la
	 * pregunta seleccionada de las mostradas por el JTree.
	 */

	void jButtonShowActionPerformed() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Comprobaci�n que no hay abierto otro frame de tipo
		// FrmEditQuestion con type = 2 y questionsFileName igual al
		// introducido por el usuario en el JDialog anterior.

		// Obtenci�n de todos lo frame que se est�n ejecutando
		// actualmente.
		JDesktopPane jDesktopPane = (JDesktopPane) getParent();
		JInternalFrame [] frames = jDesktopPane.getAllFrames();

		// Bucle para la llamada a los m�todos closingWindow de cada uno de los
		// frame obtenidos anteriormente.
		for (int i = 0; i < frames.length; i++) {
			try {
				FrmEditQuestion frmEditQuestion = (FrmEditQuestion) frames[i];

				if (frmEditQuestion.type == TestEditor.MODIFY_QUESTION_OF_A_FILE
						&& frmEditQuestion.courseName.equals(courseName)
						&& frmEditQuestion.questionsFileName.equals(questionsFileName)
						&& frmEditQuestion.isVisible() == true) {
					// Mensaje para indicar al usuario que no puede haber dos
					// frame de tipo FrmEditQuestion con type = 2 y con
					// el mismo questionsFileName que el intrudocido por el usuario
					// en el JDialog anterior.
					if (frmEditQuestion.questionModified == true) {
						// Mensaje para indicar al usuario que no puede haber dos
						// frame de tipo FrmEditQuestion con type = 2 y con
						// el mismo questionsFileName que el intrudocido por el
						// usuario
						// en el JDialog anterior.
						JOptionPane.showMessageDialog(this, "WARNING: You can not load the question data." + "\n"
								+ "You are modifying another question of this same file."
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

		// Obtenci�n del path del nodo que contiene el c�digo de la pregunta.
		// String selectionPath =
		// jTreeQuestionsFile.getSelectionPath().getLastPathComponent().toString();

		// Si no se ha seleccionado el nodo que contiene el c�digo de la pregunta
		// antes de hacer click sobre el jButtonShow, se muestra un mensaje
		// de error.
		if (codeOfQuestionsToModify.indexOf("Code of Question:") != -1) {
			// Obtenci�n del c�digo de la pregunta.
			String numberQuestionCode = codeOfQuestionsToModify.substring(codeOfQuestionsToModify
					.lastIndexOf(" ") + 1);

			Question question = parent.getQuestionByCode(courseName, questionsFileName,
					numberQuestionCode);
			if (question == null) {
				JOptionPane.showMessageDialog(this, "ERROR: It is possible that the question " + "\n"
						+ "doesn't exist inside the file." + "\n" + "Close this window and examine the file again"
						+ "\n" + "to obtain up-to-date information.", "ERROR", JOptionPane.ERROR_MESSAGE);

				// Cursor en espera.
				super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

				return;
			}

			// Llamada al frame de tipo FrmEditQuestion para modificar la
			// pregunta cuyo c�digo se ha seleccionado.
			FrmEditQuestion frmModifyQuestionData = new FrmEditQuestion("Edit Question.  Course: "
					+ courseName + " - Question File: " + questionsFileName, Integer.valueOf(numAnswersString)
					.intValue(), userLogin, userPassword, courseName, questionsFileName, codeBase,
					wowPath, iconTestEditorPath, parent, TestEditor.MODIFY_QUESTION_OF_A_FILE);

			// Adici�n del nuevo frame al JFrame principal del applet.
			getParent().add(frmModifyQuestionData);

			// Establecimiento en el nuevo frame de los datos de la
			// pregunta
			// cuyo c�digo se ha seleccionado antes de hacer click sobre el
			// jButtonShow.
			frmModifyQuestionData.setQuestionData(question, codeBase);

			try {
				frmModifyQuestionData.setSelected(true);
			} catch (java.beans.PropertyVetoException eP) {
				JOptionPane.showMessageDialog(this, eP.getMessage(), "ERROR 31: Error to modify question", 1);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Select the Code of Question to be able to modify it.",
					"Error to modify question", 1);
		}

		// Cursor estandar.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * NOMBRE: closingWindow. PERTENECE A: Clase FrmExamineQuestionsFile. LLAMADA
	 * POR: Al hacer click sobre el jButtonCancel. LLAMA A: nada. RECIBE: nada.
	 * DEVUELVE: void. FUNCI�N: M�todo que oculta al frame y lo cierra.
	 */

	public int closingWindow() {
		setVisible(false);
		dispose();
		return DISPOSE_ON_CLOSE;
	}
}