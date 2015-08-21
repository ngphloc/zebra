package es.uco.WOW.TestEditor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import es.uco.WOW.Utils.EvalTestLogStudent;
import es.uco.WOW.Utils.Student;
import es.uco.WOW.Utils.StudentTest;
import es.uco.WOW.Utils.Test;
import es.uco.WOW.Utils.TestLogStudent;

/**
 * <p>Title: Wow! TestEditor</p> <p>Description: Herramienta para la edicion
 * de preguntas tipo test adaptativas </p> <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/*
 * NOMBRE: FrmStatisticTest. FUNCION: frame que recibe en su
 * constructor entre otros par�metros el nombre de un curso propiedad del
 * usuario y el nombre de un fichero de test de dicho curso. Este frame
 * solicita al servidor todos los datos del test del curso pasados como
 * parametros. Si el servidor ha podido realizar la tarea solicitada, enviar� a
 * este frame los datos pedidos. Con estos datos este frame interno
 * muestra al usuario los datos principales sobre la ejecuci�n del test por
 * parte de los alumnos. LAST MODIFICATION: 06-02-2008
 */

public class FrmStatisticTest extends TestEditorInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String courseName; // Almacena el nombre del curso sobre el que se
										// esta

	// actuando.
	protected String testFileName; // Almacena el nombre del fichero de test que
											// se

	// va a examinar.
	private Test test = null; // Almacenar� los datos del test.

	private URL codeBase; // Almacena la URL del servidor desde el que se ejecuta

	// este applet.
	private boolean ImageIconLoad = false; // Indica si los iconos de los botones han

	// sido cargados con �xito.
	
	private Vector studentVector = null; // Vector de objeto StudentTest que

	// almacena todos los datos de la �ltima
	// ejecuci�n del test por parte de los
	// estudiantes.

	private String executionType = "";

	// Componentes del frame.
	private JScrollPane jScrollPane;

	private JScrollPane jScrollPaneTable;

	private JPanel jPanelContent; // private JPanel principal del frame.

	private JPanel jPanelTitle;

	private JPanel jPanelUsesAndTime;

	private JPanel jPanelTable;

	private JPanel jPanelButton; // private JPanel que contendr� a los private JButton.

	private JPanel jPanelStatus;

	private JTabbedPane jTabbedPaneChart;

	private JTextField jTextFieldUses;

	private JTextField jTextFieldTotalQuestions;

	private JTextField jTextFieldTotalStudents;

	private JTextField jTextFieldMinutes;

	private JTextField jTextFieldSeconds;

	private JLabel jLabelTitle;

	private JLabel jLabelScoreChart;

	private JLabel jLabelProficiencyChart;

	private JLabel jLabelUses;

	private JLabel jLabelUses1;

	private JLabel jLabelTotalQuestions;

	private JLabel jLabelTotalStudents;

	private JLabel jLabelTime;

	private JLabel jLabelMinutes;

	private JLabel jLabelSeconds;

	private JLabel jLabelStatus;

	private byte[] scoreChart = null;

	private byte[] proficiencyChart = null;

	private JButton jButtonExamineStudents;

	private JButton jButtonExit;

	private JProgressBar jProgressBar;

	private JTable jTable;

	private TableModel tableModel;

	private TableSorter tableSorter;

	private BoxLayout boxLayoutPanelContent; // Layout para el jPanelContent.

	private BoxLayout boxLayoutPanelTable;

	private FlowLayout flowLayoutPanelTitle;

	private FlowLayout flowLayoutPanelUsesAndTime; // Layout para el jPanelUsesAndTime.

	private FlowLayout flowLayoutPanelButton; // Layout del jPanelButton.

	private FlowLayout flowLayoutPanelStatus;

	private ImageIcon iconShow32; // Icono del bot�n jButtonShow.

	private ImageIcon iconCancel32; // Icono del bot�n jButtonExit.

	private ImageIcon iconInfoGreen16; // Icono de la pesta�a de la gr�fica relacionada

	// con la habilidad de los estudiantes.
	private ImageIcon iconInfoBlue16; // Icono de la pesta�a de la gr�fica relacionada

	// con la puntuaci�n de los estudiantes.
	

	/*
	 * RECIBE: title: Objeto de tipo String. Indica el
	 * t�tulo del frame. width: Objeto de tipo int. Indica la anchura
	 * inicial del frame. height: Objeto de tipo int. Indica la altura
	 * inicial del frame. userLogin: Objeto de tipo String. Indica el
	 * login del usuario. userPassword: Objeto de tipo String. Indica el password
	 * del usuario. courseName: Objeto de tipo String. Indica el nombre del curso
	 * sobre el que se est� actuando. testFileName: Objeto de tipo String.
	 * Contiene el nombre del fichero de test que se va a examinar. codeBase:
	 * Objeto de tipo URL. Contiene la URL del servidor desde donde se ha cargado
	 * el applet. wowPath: Objeto de tipo String. Contiene el path del sistema
	 * desde el que se descarg� el applet. iconTestEditorPath: Objeto de tipo
	 * String. Contiene el path del directorio que contiene los iconos usados por
	 * la interfaz gr�fica. : Objeto de tipo JFrame. Contiene el JFrame
	 * principal del applet y padre de este frame. DEVUELVE: nada.
	 * FUNCI�N: Constructor de la clase FrmStatisticTest. Se encarga de
	 * configurar el aspecto del frame, su funcionalidad, as� como de inicializar
	 * los componentes que componen el mismo.
	 */

	public FrmStatisticTest(String aTitle, int aWidth, int aHeight, String aUserLogin, String aUserPassword,
			String aCourseName, String aTestFileName, URL aCodeBase, String anWOWPath,
			String iconPath, TestEditor father) {
	//
		// Calls the parent constructor
		super(aTitle, aWidth, aHeight, father, anWOWPath, iconPath);
		
		// Sets user variables
		courseName = aCourseName;
		testFileName = aTestFileName;
		codeBase = aCodeBase;

		// Obtenci�n de los datos del test a modificar.
		test = father.getTest(courseName, testFileName);

		if (test == null) {
			// Mensaje para indicar al usuario que se ha producido un error al
			// obtener los datos del test.
			JOptionPane.showMessageDialog(this, "ERROR: The test data HAVE NOT BEEN loaded CORRECTLY.",
					"Load Test Error", JOptionPane.ERROR_MESSAGE);
			dispose();
			return;
		}

		// Lectura del tipo de ejecuci�n del test.
		executionType = test.getExecutionType();

		// Lectura del nombre del fichero de test.
		testFileName = test.getTestFileName();

		// Llamada al m�todo de la clase padre encargado de invocar al servidor
		// para
		// que genere la gr�fica.
		scoreChart = father.getChart(courseName, "", testFileName, executionType, 750, 500, false, "score");

		if (executionType.equals(TestEditor.ADAPTIVE)) {
			proficiencyChart = father.getChart(courseName, "", testFileName, executionType, 750, 500, false, "proficiency");
		}
		
		// Inicializaci�n de los componentes de la clase.
		JImageIconInit();
		JButtonInit();
		JTextFieldInit();
		JLabelInit();
		JProgressBarInit();
		JTableInit();
		JPanelInit();
		try {
			frameInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	// //////// INICIALIZACI�N DE LOS COMPONENTES DEL FRAME
	// ////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: JImageIconInit. PERTENECE A: Clase FrmStatisticTest. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa los iconos mostrados por los
	 * botones del frame.
	 */

	private void JImageIconInit() {
		// Path relativo de la situaci�n de los iconos buscados dentro del
		// sistema.
		String petitionShow32 = wowPath + iconTestEditorPath + "examine32.gif";
		String petitionCancel32 = wowPath + iconTestEditorPath + "cancel32.gif";
		String petitionInfoGreen16 = wowPath + iconTestEditorPath + "info_green_16.gif";
		String petitionInfoBlue16 = wowPath + iconTestEditorPath + "info_blue_16.gif";

		// Conexi�n con el servidor.
		try {
			URL urlShow32 = new URL(codeBase, petitionShow32);
			URL urlCancel32 = new URL(codeBase, petitionCancel32);
			URL urlInfoGreen16 = new URL(codeBase, petitionInfoGreen16);
			URL urlInfoBlue16 = new URL(codeBase, petitionInfoBlue16);

			// Reserva de memoria para los ImageIcon.
			iconShow32 = new ImageIcon(urlShow32);
			iconCancel32 = new ImageIcon(urlCancel32);
			iconInfoGreen16 = new ImageIcon(urlInfoGreen16);
			iconInfoBlue16 = new ImageIcon(urlInfoBlue16);

			ImageIconLoad = true;
		} catch (java.net.MalformedURLException e) {
			ImageIconLoad = false;
		}
	}

	/*
	 * NOMBRE: JButtonInit. PERTENECE A: Clase FrmStatisticTest. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JButton y establece su
	 * funcionalidad.
	 */

	private void JButtonInit() {
		// Si los iconos de los JButton han sido obtenidos correctamente, entonces
		// los JButton se inicializar�n con dicho iconos, sino, se har� sin ellos.
		if (ImageIconLoad == true) {
			// Reserva de memoria para los JButton con iconos.
			jButtonExamineStudents = new JButton("Examine students", iconShow32);
			jButtonExit = new JButton("Exit", iconCancel32);
		} else {
			// Reserva de memoria para los JButton sin iconos.
			jButtonExamineStudents = new JButton("Examine students");
			jButtonExit = new JButton("Exit");
		}

		// Establecimiento de la funcionalidad del jButtonExamineStudents.
		jButtonExamineStudents.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonExamineStudentsActionPerformed();
			}
		});

		// Establecimiento de la funcionalidad del jButtonExit.
		jButtonExit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closingWindow();
			}
		});

		if (test.getStudentVector() == null || test.getStudentVector().isEmpty())
			jButtonExamineStudents.setEnabled(false);
		else
			jButtonExamineStudents.setEnabled(true);
	}

	/*
	 * NOMBRE: JTextFieldInit. PERTENECE A: Clase FrmStatisticTest. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa los JTextField del
	 * frame, reservando memoria para los mismos y estableciendo su
	 * color, su contenido...
	 */

	private void JTextFieldInit() {
		int uses = 0;
		int totalStudents = 0;
		// C�lculo del n�mero de usos del test.
		if (test.getStudentVector() != null && !test.getStudentVector().isEmpty()) {
			totalStudents = test.getStudentVector().size();
			for (int i = 0; i < test.getStudentVector().size(); i++) {
				StudentTest studentTest = (StudentTest) test.getStudentVector().get(i);
				uses = uses + studentTest.getMake();
			}
		}

		// Reserva de memoria para los JTextField.
		jTextFieldUses = new JTextField(String.valueOf(uses), 3);
		jTextFieldUses.setFocusable(false);
		jTextFieldUses.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextFieldUses.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jTextFieldTotalQuestions = new JTextField(String.valueOf(test.getTotalQuestion()), 3);
		jTextFieldTotalQuestions.setFocusable(false);
		jTextFieldTotalQuestions.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextFieldTotalQuestions.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jTextFieldTotalStudents = new JTextField(String.valueOf(totalStudents), 3);
		jTextFieldTotalStudents.setFocusable(false);
		jTextFieldTotalStudents.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextFieldTotalStudents.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jTextFieldMinutes = new JTextField(3);
		jTextFieldMinutes.setVisible(false);
		jTextFieldMinutes.setFocusable(false);
		jTextFieldMinutes.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextFieldMinutes.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jTextFieldSeconds = new JTextField(3);
		jTextFieldSeconds.setVisible(false);
		jTextFieldSeconds.setFocusable(false);
		jTextFieldSeconds.setFont(new java.awt.Font("Dialog", 0, 14));
		jTextFieldSeconds.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
	}

	/*
	 * NOMBRE: JLabelInit. PERTENECE A: Clase FrmStatisticTest. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JLabel del JApplet, reservando
	 * memoria para los mismos y estableciendo su color, su contenido...
	 */

	private void JLabelInit() {
		// Reserva de memoria para el jLabelTitle.
		jLabelTitle = new JLabel("Statistic of the test");
		jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 28));

		// Reserva de memoria para jLabelScoreChart.
		jLabelScoreChart = new JLabel();
		if (scoreChart != null) {
			jLabelScoreChart.setFocusable(true);
			jLabelScoreChart.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			ImageIcon chartImage = new ImageIcon(scoreChart);
			jLabelScoreChart.setIcon(chartImage);
		}

		// Reserva de memoria para jLabelProficiencyChart.
		jLabelProficiencyChart = new JLabel();
		if (proficiencyChart != null) {
			jLabelProficiencyChart.setFocusable(true);
			jLabelProficiencyChart.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
			ImageIcon chartImage = new ImageIcon(proficiencyChart);
			jLabelProficiencyChart.setIcon(chartImage);
		}

		// Reserva de memoria para el jLabelUses.
		jLabelUses = new JLabel("Carried out Test");
		jLabelUses1 = new JLabel("times");
		jLabelUses.setFont(new java.awt.Font("Dialog", 1, 12));
		jLabelUses1.setFont(new java.awt.Font("Dialog", 1, 12));

		// Reserva de memoria para el jLabelTotalQuestions.
		jLabelTotalQuestions = new JLabel("Number of questions");
		jLabelTotalQuestions.setFont(new java.awt.Font("Dialog", 1, 12));

		// Reserva de memoria para el jLabelTotalStudents.
		jLabelTotalStudents = new JLabel("Number of students");
		jLabelTotalStudents.setFont(new java.awt.Font("Dialog", 1, 12));

		// Reserva de memoria para los jLabelTime.
		jLabelTime = new JLabel("Average time of test realization");
		jLabelTime.setVisible(false);
		jLabelTime.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelMinutes = new JLabel("minutes");
		jLabelMinutes.setVisible(false);
		jLabelMinutes.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelSeconds = new JLabel("seconds");
		jLabelSeconds.setVisible(false);
		jLabelSeconds.setFont(new java.awt.Font("Dialog", 1, 12));

		// Reserva de memoria para jLabelStatus.
		jLabelStatus = new JLabel("Done                                       "
				+ "                                           ");
	}

	/*
	 * NOMBRE: JProgressBarInit. PERTENECE A: Clase FrmStatisticTest. LLAMADA
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
		jProgressBar.setValue(0);
		jProgressBar.setMinimum(0);
	}

	/*
	 * NOMBRE: JTableInit. PERTENECE A: Clase FrmStatisticTest. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa el JTable y establece su contenido y
	 * funcionalidad.
	 */

	private void JTableInit() {
		Vector rowsVector = new Vector();
		Vector columnsVector = new Vector();

		columnsVector.add("Login");
		columnsVector.add("Make");
		columnsVector.add("Score");
		columnsVector.add("Finish");
		columnsVector.add("Can repeat");

		if (test.getExecutionType().trim().equals(TestEditor.ADAPTIVE)) {
			columnsVector.add("Proficiency");
			columnsVector.add("Standard error");
		}

		if (test.getStudentVector() != null) {
			// Bucle para la lectura de los datos b�sicos de los estudiantes que
			// han
			// realizado el test.
			for (int i = 0; i < test.getStudentVector().size(); i++) {
				// Reserva de memoria para el objeto StudentTest.
				StudentTest studentTest = (StudentTest) test.getStudentVector().get(i);

				Vector studentTestTableVector = new Vector();
				studentTestTableVector.add(studentTest.getLogin());
				studentTestTableVector.add(String.valueOf(studentTest.getMake()));
				studentTestTableVector.add(String.valueOf(studentTest.getLastScore()));
				studentTestTableVector.add(String.valueOf(studentTest.getFinish()));
				studentTestTableVector.add(String.valueOf(studentTest.getCanRepeat()));

				if (test.getExecutionType().trim().equals(TestEditor.ADAPTIVE)) {
					studentTestTableVector.add(String.valueOf(studentTest.getTheta()));
					studentTestTableVector.add(String.valueOf(studentTest.getStandardError()));
				}

				rowsVector.add(studentTestTableVector);
			}
		}
		tableModel = new TableModel(rowsVector, columnsVector);
		tableSorter = new TableSorter(tableModel);
		jTable = new JTable(tableSorter);
		jTable.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jTable.setAutoscrolls(true);

		tableSorter.addMouseListenerToHeaderInTable(jTable);
	}

	/*
	 * NOMBRE: JTableExamineInit. PERTENECE A: Clase FrmStatisticTest. LLAMADA
	 * POR: El m�todo jButtonExamineStudentsActionPerformed. LLAMA A: nada.
	 * RECIBE: void. DEVUELVE: void. FUNCI�N: Inicializa el JTable y establece su
	 * contenido y funcionalidad.
	 */

	private void JTableExamineInit() {
		Vector rowsVector = new Vector();
		Vector columnsVector = new Vector();

		columnsVector.add("Login");
		columnsVector.add("Make");
		columnsVector.add("Score");
		columnsVector.add("Finish");
		columnsVector.add("Can repeat");

		if (test.getExecutionType().trim().equals(TestEditor.ADAPTIVE)) {
			columnsVector.add("Proficiency");
			columnsVector.add("Standard error");
		}

		columnsVector.add("Correct");
		columnsVector.add("Incorrect");
		columnsVector.add("Without answer");
		columnsVector.add("Done");
		columnsVector.add("Not done");
		columnsVector.add("Total questions in test");
		columnsVector.add("Time");
		columnsVector.add("Date");

		if (studentVector != null) {
			// Bucle para la lectura de los datos de los estudiantes que han
			// realizado el test.
			for (int i = 0; i < studentVector.size(); i++) {
				StudentTest studentTest = (StudentTest) test.getStudentVector().get(i);

				// Reserva de memoria para el objeto StudentTest.
				Student student = (Student) studentVector.get(i);
				TestLogStudent testLogStudent = (TestLogStudent) student.getTest().firstElement();
				EvalTestLogStudent evalTestLogStudent = (EvalTestLogStudent) testLogStudent.getEvaluatedTest()
						.firstElement();

				Vector studentTableVector = new Vector();
				studentTableVector.add(student.getLogin());
				studentTableVector.add(String.valueOf(studentTest.getMake()));
				studentTableVector.add(String.valueOf(studentTest.getLastScore()));
				studentTableVector.add(String.valueOf(studentTest.getFinish()));
				studentTableVector.add(String.valueOf(studentTest.getCanRepeat()));

				if (test.getExecutionType().trim().equals(TestEditor.ADAPTIVE)) {
					studentTableVector.add(String.valueOf(studentTest.getTheta()));
					studentTableVector.add(String.valueOf(studentTest.getStandardError()));
				}

				studentTableVector.add(String.valueOf(evalTestLogStudent.getCorrect()));
				studentTableVector.add(String.valueOf(evalTestLogStudent.getIncorrect()));
				studentTableVector.add(String.valueOf(evalTestLogStudent.getWithoutAnswer()));
				studentTableVector.add(String.valueOf(evalTestLogStudent.getQuestionsDone()));
				studentTableVector.add(String.valueOf(evalTestLogStudent.getTotalQuestion()
						- evalTestLogStudent.getQuestionsDone()));
				studentTableVector.add(String.valueOf(evalTestLogStudent.getTotalQuestion()));
				studentTableVector.add(String.valueOf(evalTestLogStudent.getTime()));
				studentTableVector.add(String.valueOf(evalTestLogStudent.getDate()));

				rowsVector.add(studentTableVector);
			}
		}
		tableModel = new TableModel(rowsVector, columnsVector);
		tableSorter = new TableSorter(tableModel);
		jTable = new JTable(tableSorter);
		jTable.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jTable.setAutoscrolls(true);

		tableSorter.addMouseListenerToHeaderInTable(jTable);
	}

	/*
	 * NOMBRE: JPanelInit. PERTENECE A: Clase FrmStatisticTest. LLAMADA POR: El
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

		// Reserva de memoria para el JPanel que contendr� al jPanelTitle.
		jPanelTitle = new JPanel();
		flowLayoutPanelTitle = new FlowLayout(FlowLayout.CENTER);
		jPanelTitle.setLayout(flowLayoutPanelTitle);
		jPanelTitle.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		jPanelTitle.add(jLabelTitle);

		// Reserva de memoria para el jTabbedPane.
		jTabbedPaneChart = new JTabbedPane();

		jTabbedPaneChart.addTab("Score", iconInfoBlue16, jLabelScoreChart);

		if (test.getExecutionType().trim().equals(TestEditor.ADAPTIVE))
			jTabbedPaneChart.addTab("Proficiency", iconInfoGreen16, jLabelProficiencyChart);

		// Reserva de memoria para el jPanelUsesAndTime.
		jPanelUsesAndTime = new JPanel();
		flowLayoutPanelUsesAndTime = new FlowLayout(FlowLayout.CENTER);
		jPanelUsesAndTime.setLayout(flowLayoutPanelUsesAndTime);
		jPanelUsesAndTime.setBackground(SystemColor.WHITE);
		jPanelUsesAndTime.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));

		// Adici�n de los componentes del jPanelUsesAndTime.
		jPanelUsesAndTime.add(jLabelUses);
		jPanelUsesAndTime.add(jTextFieldUses);
		jPanelUsesAndTime.add(jLabelUses1);
		jPanelUsesAndTime.add(Box.createHorizontalStrut(10));
		jPanelUsesAndTime.add(jLabelTotalQuestions);
		jPanelUsesAndTime.add(jTextFieldTotalQuestions);
		jPanelUsesAndTime.add(Box.createHorizontalStrut(10));
		jPanelUsesAndTime.add(jLabelTotalStudents);
		jPanelUsesAndTime.add(jTextFieldTotalStudents);
		jPanelUsesAndTime.add(Box.createHorizontalStrut(10));
		jPanelUsesAndTime.add(jLabelTime);
		jPanelUsesAndTime.add(jTextFieldMinutes);
		jPanelUsesAndTime.add(jLabelMinutes);
		jPanelUsesAndTime.add(jTextFieldSeconds);
		jPanelUsesAndTime.add(jLabelSeconds);

		// Reserva de memoria para el jPanelTable.
		jPanelTable = new JPanel();
		boxLayoutPanelTable = new BoxLayout(jPanelTable, BoxLayout.Y_AXIS);
		jPanelTable.setLayout(boxLayoutPanelTable);
		jPanelTable.setBorder(BorderFactory.createTitledBorder(BorderFactory
				.createLineBorder(SystemColor.BLACK), "Students data", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14)));

		jPanelTable.setAlignmentX(CENTER_ALIGNMENT);
		jPanelTable.setAlignmentY(CENTER_ALIGNMENT);

		// Reserva de memoria para el jScrollPaneTable.
		jScrollPaneTable = new JScrollPane(jTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneTable.setBackground(Color.black);
		jScrollPaneTable.setBorder(BorderFactory.createEmptyBorder());

		// Adici�n de los componentes del jPanelTable.
		jPanelTable.add(jScrollPaneTable);

		// Reserva de memoria para el jPanelButton.
		jPanelButton = new JPanel();
		flowLayoutPanelButton = new FlowLayout(FlowLayout.CENTER);
		jPanelButton.setLayout(flowLayoutPanelButton);

		// Adici�n de los componentes del jPanelButton.
		jPanelButton.add(jButtonExamineStudents);
		jPanelButton.add(jButtonExit);

		// Reserva de memoria para el jPanelStatus.
		jPanelStatus = new JPanel();
		flowLayoutPanelStatus = new FlowLayout(FlowLayout.LEFT);
		jPanelStatus.setLayout(flowLayoutPanelStatus);
		jPanelStatus.setBorder(BorderFactory.createLoweredBevelBorder());

		// Adici�n de los componentes del jPanelStatus.
		jPanelStatus.add(jLabelStatus);
		jPanelStatus.add(jProgressBar);

		// Adici�n de los componentes del jPanelContent.
		jPanelTitle.setAlignmentX(CENTER_ALIGNMENT);
		jTabbedPaneChart.setAlignmentX(CENTER_ALIGNMENT);
		jPanelUsesAndTime.setAlignmentX(CENTER_ALIGNMENT);
		jPanelTable.setAlignmentX(CENTER_ALIGNMENT);
		jPanelButton.setAlignmentX(CENTER_ALIGNMENT);
		jPanelStatus.setAlignmentX(CENTER_ALIGNMENT);

		jPanelContent.add(jPanelTitle);
		jPanelContent.add(Box.createHorizontalStrut(10));
		jPanelContent.add(jTabbedPaneChart);
		jPanelContent.add(Box.createHorizontalStrut(10));
		jPanelContent.add(jPanelUsesAndTime);
		jPanelContent.add(Box.createHorizontalStrut(10));
		jPanelContent.add(jPanelTable);
		jPanelContent.add(Box.createHorizontalStrut(10));
		jPanelContent.add(jPanelButton);
		jPanelContent.add(Box.createHorizontalStrut(10));
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
	// //////// FUNCIONALIDADES DE LOS COMPONETES DEL FRAME
	// ////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: jButtonExamineStudentsActionPerformed. PERTENECE A: Clase
	 * FrmFindQuestions. LLAMADA POR: Al pulsar sobre el jButtonExamineStudents.
	 * LLAMA A: nada. RECIBE: nada. DEVUELVE: void. FUNCI�N: M�todo que solicita
	 * al servidor informaci�n detallada de cada uno de los estudiantes que han
	 * realizado el test y sobre el desarrollo del mismo por parte de dichos
	 * alumnos.
	 */

	void jButtonExamineStudentsActionPerformed() {
		// Comprobaci�n de que existen alumnos que han realizado el test
		if (test.getStudentVector() != null && !test.getStudentVector().isEmpty()) {
			int time = 0;

			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			jProgressBar.setMinimum(0);
			jProgressBar.setValue(0);
			jProgressBar.setMaximum(test.getStudentVector().size());

			jLabelStatus.paint(jPanelStatus.getGraphics());
			jProgressBar.paint(jProgressBar.getGraphics());
			jPanelStatus.paint(jPanelStatus.getGraphics());
			paint(getGraphics());

			studentVector = new Vector();

			// Bucle para la obtenci�n de los datos de los estudiantes.
			for (int i = 0; i < test.getStudentVector().size(); i++) {
				StudentTest studentTest = (StudentTest) test.getStudentVector().get(i);

				jLabelStatus.setText("Loading..... " + studentTest.getLogin());
				jProgressBar.setValue(i + 1);
				jLabelStatus.paint(jPanelStatus.getGraphics());
				jProgressBar.paint(jProgressBar.getGraphics());
				jPanelStatus.paint(jPanelStatus.getGraphics());
				paint(getGraphics());

				// Llamada al m�todo de la clase padre que devuelve los datos del
				// estudiante.
				Student student = parent.getStudentAndFinishTest(test.getCourse(), studentTest
						.getLogin(), test.getTestFileName());
				if (student != null) {
					TestLogStudent testLogStudent = (TestLogStudent) student.getTest().firstElement();
					EvalTestLogStudent evalTestLogStudent = (EvalTestLogStudent) testLogStudent.getEvaluatedTest()
							.firstElement();
					time = time + evalTestLogStudent.getTime();
					studentVector.add(student);
				}
			}

			// Llamada al m�todo de esta misma clase que configura de nuevo a
			// jTable.
			JTableExamineInit();
			JPanelInit();
			try {
				frameInit();
			} catch (Exception e) {}

			// C�lculo del tiempo m�dio de respuesta del test.
			time = Math.round(time / studentVector.size());
			int minutes = time / 60;
			int seconds = time % 60;
			jTextFieldMinutes.setText(String.valueOf(minutes));
			jTextFieldSeconds.setText(String.valueOf(seconds));
			jTextFieldMinutes.setVisible(true);
			jTextFieldSeconds.setVisible(true);
			jLabelTime.setVisible(true);
			jLabelMinutes.setVisible(true);
			jLabelSeconds.setVisible(true);

			jLabelStatus.setText("Done...                                          "
					+ "                                                 ");
			jProgressBar.setValue(0);
			jLabelStatus.paint(jPanelStatus.getGraphics());
			jProgressBar.paint(jProgressBar.getGraphics());
			jPanelStatus.paint(jPanelStatus.getGraphics());
			paint(getGraphics());
		}
	}

	/*
	 * NOMBRE: closingWindow. PERTENECE A: Clase FrmStatisticTest. LLAMADA POR:
	 * Al hacer click sobre el jButtonExit. LLAMA A: nada. RECIBE: nada.
	 * DEVUELVE: void. FUNCI�N: Cierra el frame.
	 */

	public int closingWindow() {
		setVisible(false);
		dispose();
		return DISPOSE_ON_CLOSE;
	}
}