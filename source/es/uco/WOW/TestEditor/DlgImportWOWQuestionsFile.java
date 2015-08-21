package es.uco.WOW.TestEditor;

import java.awt.Cursor;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import es.uco.WOW.Utils.Course;

/**
 * <p>Title: Wow! TestEditor</p> <p>Description: Herramienta para la edicion
 * de preguntas tipo test adaptativas </p> <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * @version 1.0
 */

/**
 * NAME: DlgImportQuestionsFile
 * FUNCTION: Window that shows a wizard to import a
 * file of questions in another format to other file of questions in the WOW!
 * current format.
 * LAST MODIFICATION: 04-09-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class DlgImportWOWQuestionsFile extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The path of the WOW system
	 */
	private String wowPath;

	// Variables del usuario.
	private Vector courseVector; // Almacena la lista de cursos

	// pertenecientes al usuario.
	private Vector questionsFileNameOldVector = null; // Almacenar� los nombres
																		// de los

	// ficheros de test antiguos.
	private String title = "";

	private String testOldPath = "";

	private String courseName = ""; // Almacena el curso seleccionado.

	// private String concept = ""; //Almacena el concepto seleccionado.
	// private String questionsFileName = ""; //Almacena el nombre del fichero de
	// preguntas seleccionado.
	// private int numAnswersInt = 0; //Almacena el n�mero de respuestas por
	// defecto
	// del fichero de preguntas seleccionado.
	// private String numAnswersString = ""; //Almacena el n�mero de respuestas
	// por
	// defecto del fichero de preguntas
	// seleccionado.

	// Componentes de la ventana.
	private JPanel jPanelTitle; // JPanel que muestra el t�tulo en jPanelMain.

	private JPanel jPanelMain; // JPanel principal del JDialog.

	private JLabel jLabelTitle;

	private JLabel jLabelCourse;

	private JLabel jLabelConcept;

	private JLabel jLabelQuestionsFile;

	private JLabel jLabelNewQuestionsFile;

	private JLabel jLabelPath;

	private JLabel jLabelStatus;

	private JProgressBar jProgressBar;

	private TestEditor parent;

	private boolean id = false;

	private boolean taskDone = false;

	private String step = "0";

	private JButton NavigationButton;

	private JButton OkButton;

	private JButton CancelButton;

	private JComboBox jComboCourse;

	private JComboBox jComboConcept;

	private JComboBox jComboQuestionsFile;

	private JTextField jTextFieldDirectoryTestOldPath;

	private JTextField jTextFieldQuestionsFile;

	private TimeControl workerTimeControl = null;

	private RepaintProgressBar workerRepaintProgressBar = null;

	private GetQuestionsFileListOld workerGet = null;
	
	
	/*
	 * NOMBRE: DlgImportQuestionsFile (Constructor de esta misma clase).
	 * PERTENECE A: Clase DlgImportQuestionsFile. LLAMADA POR: Al invocar la
	 * clase. LLAMA A: Step0Init(); RECIBE: title: Objeto de tipo String. Indica
	 * el t�tulo del JDialog. courseVector: Objeto de tipo Vector. Contiene en
	 * cada posici�n un objeto tipo Course. parent: Objeto de tipo JFrame. Hace
	 * referencia al JFrame que invoc� a este JDialog, osea su padre. DEVUELVE:
	 * nada. FUNCI�N: Constructor de la clase DlgImportQuestionsFile. Se encarga
	 * de configurar el aspecto del dialogo, su funcionalidad, asi como de
	 * inicializar los componentes que componen el mismo.
	 */
	/**
	 * 
	 * @param theTitle
	 * @param aCourseVector
	 * @param theParent
	 * @param anWOWPath
	 */
	public DlgImportWOWQuestionsFile(final String theTitle, final Vector aCourseVector,
			final TestEditor theParent, final String anWOWPath) {
		// Llamada al constructor de la clase padre.
		super(theParent, theTitle, true);

		// Establecimiento de los valores de las variables propias de la clase
		// con los recibidos.
		this.wowPath = anWOWPath;
		this.title = theTitle;
		this.courseVector = aCourseVector;
		this.parent = theParent;

		// Establecimiento del aspecto del JDialog.
		try {
			UIManager.setLookAndFeel(parent.lookAndFeelClassName);
		} catch (Exception e) {}

		// Inicializaci�n de los componentes de la clase.
		try {
			Step0Init();
		} catch (Exception e) {}
	}

	// //////////////////////////////////////////////////////////////////////////////
	// //////// INICIALIZACI�N DE LOS COMPONENTES DEL JDIALOG PARA EL STEP
	// 0/////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: Step0Init. PERTENECE A: Clase DlgImportQuestionsFile. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A:
	 * JTextFieldInitStep0(); JPanelInitStep0(); JLabelInitStep0();
	 * JButtonInitStep0(); JDialogInit(); RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los componentes del JDialog, necesarios para el paso 0 del
	 * asistente para la importaci�n de ficheros, reservando memoria para los
	 * mismos y estableciendo su tama�o, su contenido, funcionalidad...
	 */

	private void Step0Init() {
		if (step.equals("0"))
			JTextFieldInitStep0();

		JPanelInitStep0();
		JLabelInitStep0();
		JButtonInitStep0();
		JProgressBarInit();

		if (!step.equals("0"))
			NavigationButton.setEnabled(true);

		try {
			JDialogInitStep0();
		} catch (java.lang.Exception e) {}

		step = "0";
	}

	/*
	 * NOMBRE: JTextFieldInitStep0. PERTENECE A: Clase DlgImportQuestionsFile.
	 * LLAMADA POR: Step0Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JTextField del JDialog, reservando memoria para
	 * los mismo y estableciendo su tama�o y su funcionalidad.
	 */

	private void JTextFieldInitStep0() {
		// Reserva de memoria para el JTextFieldNewQuestionsFile.
		jTextFieldDirectoryTestOldPath = new JTextField();

		jTextFieldDirectoryTestOldPath.setBounds(new Rectangle(80, 17, 300, 25));

		// Establecimiento de su funcionalidad.
		jTextFieldDirectoryTestOldPath.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				jTextFieldDirectoryTestOldPathActionPerformed();
			}
		});
	}

	/*
	 * NOMBRE: jTextFieldDirectoryTestOldPathActionPerformed. PERTENECE A: Clase
	 * DlgImportQuestionsFile. LLAMADA POR: Al escribir en el
	 * jTextFieldDirectoryTestOldPath. LLAMA A: nada. RECIBE: nada. DEVUELVE:
	 * void. FUNCI�N: Habilita o desabilita el NavigationButton.
	 */

	void jTextFieldDirectoryTestOldPathActionPerformed() {
		if (jTextFieldDirectoryTestOldPath.getText().trim().equals("") == false)
			NavigationButton.setEnabled(true);
		else
			NavigationButton.setEnabled(false);
	}

	/*
	 * NOMBRE: JLabelInitStep0. PERTENECE A: Clase DlgImportQuestionsFile.
	 * LLAMADA POR: Step0Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JLabel del JDialog, reservando memoria para los
	 * mismo, estableciendo sus tama�osy sus textos.
	 */

	private void JLabelInitStep0() {
		// Reserva de memoria par los JLabel.
		jLabelTitle = new JLabel();
		jLabelPath = new JLabel();
		jLabelStatus = new JLabel();

		// Establecimiento de sus tama�os, textos y bordes.
		jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 16));
		jLabelTitle.setBorder(BorderFactory.createLoweredBevelBorder());
		jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelTitle.setText("Introduce the path to the source file");
		jLabelTitle.setBounds(new Rectangle(10, 5, 407, 44));

		jLabelPath.setBounds(new Rectangle(20, 20, 50, 20));
		jLabelPath.setText(wowPath + "/");
		jLabelPath.setFont(new java.awt.Font("Dialog", 1, 16));

		jLabelStatus.setBounds(new Rectangle(20, 50, 70, 20));
		jLabelStatus.setText("Done          ");
		jLabelStatus.setFont(new java.awt.Font("Dialog", 0, 12));
		jLabelStatus.setVisible(false);
	}

	/*
	 * NOMBRE: JButtonInitStep0. PERTENECE A: Clase DlgImportQuestionsFile.
	 * LLAMADA POR: Step0Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JButton del JDialog, reservando memoria para los
	 * mismo y estableciendo sus tama�os y funcionalidades.
	 */

	private void JButtonInitStep0() {
		// Reserva de memoria para los JButton.
		NavigationButton = new JButton();
		CancelButton = new JButton();

		// Establecimiento del tama�o, textos y tipo de letra para OkButton.
		NavigationButton.setBounds(new Rectangle(195, 145, 100, 30));

		NavigationButton.setText("Next >");
		NavigationButton.setFont(new java.awt.Font("Button", 1, 12));
		NavigationButton.setEnabled(false);

		// Establecimiento de la funcionalidad de OkButton.
		NavigationButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NavigationButtonStep0ActionPerformed();
			}
		});

		// Establecimiento del tama�o, texto y tipo de letra para CancelButton.
		CancelButton.setBounds(new Rectangle(310, 145, 100, 30));

		CancelButton.setText("Cancel");
		CancelButton.setFont(new java.awt.Font("Button", 1, 12));

		// Establecimiento de la funcionalidad de CancelButton.
		CancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelButtonActionPerformed();
			}
		});
	}

	/*
	 * NOMBRE: NavigationButtonActionPerformed. PERTENECE A: Clase
	 * DlgImportQuestionsFile. LLAMADA POR: Al hacer click sobre CancelButton
	 * configurado en Step0Init. LLAMA A: nada. RECIBE: nada. DEVUELVE: void.
	 * FUNCI�N: Invoca a la clase GetQuestionsFileListOld.
	 */

	void NavigationButtonStep0ActionPerformed() {
		testOldPath = jTextFieldDirectoryTestOldPath.getText().trim();
		if (testOldPath.trim().endsWith("/") == false)
			testOldPath = testOldPath.concat("/");

		workerGet = new GetQuestionsFileListOld(testOldPath);
		workerGet.start();

		// Llamada a la clase para repintar la barra de progreso.
		workerRepaintProgressBar = new RepaintProgressBar();
		workerRepaintProgressBar.start();

		workerTimeControl = new TimeControl();
		workerTimeControl.start();

		try {
			workerGet.join();
			workerRepaintProgressBar.join();
		} catch (java.lang.InterruptedException ie) {}

		workerTimeControl.stop();

		if (taskDone == false) {
			// Cursor en espera.
			super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			workerGet.stop();
			workerRepaintProgressBar.stop();
			return;
		}

		// Si la respuesta del servidor es null, se muestra un mensaje de error.
		if (questionsFileNameOldVector != null)
			Step1Init();
		else {
			// Mensaje para indicar al usuario que se ha producido un error al
			// intentar
			// conseguir los nombres de los ficheros de preguntas de los cursos.
			JOptionPane dlgMessage = new JOptionPane();
			dlgMessage.setFont(new java.awt.Font("Dialog", 0, 16));
			JOptionPane.showMessageDialog(this,
					"ERROR: There is no test files to import in that folder", "Import Error",
					JOptionPane.ERROR_MESSAGE);
			jProgressBar.setValue(0);
		}
	}

	/*
	 * NOMBRE: CancelButtonActionPerformed. PERTENECE A: Clase
	 * DlgImportQuestionsFile. LLAMADA POR: Al hacer click sobre CancelButton
	 * configurado en Step0Init. LLAMA A: nada. RECIBE: nada. DEVUELVE: void.
	 * FUNCI�N: Oculta el JDialog, eliminando los valores en el contenido.
	 */

	void CancelButtonActionPerformed() {
		id = false;
		this.setVisible(false);

		// Deletes the content of the components
		// Prevents exceptions during event dispaching
		if (jTextFieldDirectoryTestOldPath != null) {
			jTextFieldDirectoryTestOldPath.setText("");
		}
		if (jComboCourse != null) {
			jComboCourse.removeAllItems();
		}
		if (jComboConcept != null) {
			jComboConcept.removeAllItems();
		}
		if (jComboQuestionsFile != null) {
			jComboQuestionsFile.removeAllItems();
		}

		this.dispose();
	}

	/*
	 * NOMBRE: JPanelInitStep0. PERTENECE A: Clase DlgImportQuestionsFile.
	 * LLAMADA POR: El constructor de la clase a la que pertenece. LLAMA A: nada.
	 * RECIBE: void. DEVUELVE: void. FUNCI�N: Inicializa los JPanel del JDialog,
	 * reservando memoria para los mismo y estableciendo su tama�o y su tipo de
	 * layout.
	 */

	private void JPanelInitStep0() {
		// Reserva de memoria para los JPanel.
		jPanelTitle = new JPanel();
		jPanelMain = new JPanel();

		// Establecimiento de sus tama�os, bordes y layout.
		jPanelTitle.setLayout(null);
		jPanelMain.setBorder(BorderFactory.createLoweredBevelBorder());

		jPanelMain.setBounds(new Rectangle(10, 50, 407, 90));

		jPanelMain.setLayout(null);
	}

	/*
	 * NOMBRE: JDialogInitStep0. PERTENECE A: Clase DlgImportQuestionsFile.
	 * LLAMADA POR: El constructor de la clase a la que pertenece. LLAMA A: nada.
	 * RECIBE: void. DEVUELVE: void. FUNCI�N: Inicializa el JDialog. Establece el
	 * contenido de cada JPanel del JDialog.
	 */

	private void JDialogInitStep0() throws Exception {
		jPanelTitle.add(jLabelTitle, null);
		jPanelTitle.add(NavigationButton, null);
		jPanelTitle.add(CancelButton, null);
		jPanelTitle.add(jPanelMain, null);
		jPanelMain.add(jLabelPath, null);
		jPanelMain.add(jTextFieldDirectoryTestOldPath, null);
		jPanelMain.add(jLabelStatus, null);
		jPanelMain.add(jProgressBar, null);

		// Establecimiento de las caracter�sticas del JDialog, tama�o...
		this.setSize(433, 240);

		this.setResizable(false);
		this.setLocation(200, 150);
		this.setContentPane(jPanelTitle);
		this.setVisible(true);
		this.setModal(true);
		this.setTitle(title);
		this.setBackground(SystemColor.window);
	}

	/*
	 * NOMBRE: JProgressBarInit. PERTENECE A: Clase DlgImportQuestionsFile.
	 * LLAMADA POR: Step0Init. LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa el JProgressBar del JDialog.
	 */

	private void JProgressBarInit() {
		// Reserva de memoria y establecimiento de algunas propiedades de los
		// JProgressBar.
		jProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
		jProgressBar.setBorderPainted(true);
		jProgressBar.setStringPainted(true);
		jProgressBar.setBounds(new Rectangle(80, 47, 300, 25));
		jProgressBar.setVisible(false);
	}

	/*
	 * NOMBRE: repaintJProgressBar. PERTENECE A: Clase DlgImportQuestionsFile.
	 * LLAMADA POR: NavigationButtonStep0ActionPerformed(); LLAMA A: nada.
	 * RECIBE: void. DEVUELVE: void. FUNCI�N: Este m�todo redibuja los
	 * JProgressBar para mostrar el progreso de una tarea.
	 */

	private void repaintProgressBar() {
		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		jLabelStatus.setVisible(true);
		jProgressBar.setVisible(true);
		jLabelStatus.paint(this.jLabelStatus.getGraphics());
		jProgressBar.paint(this.jProgressBar.getGraphics());
		jLabelStatus.setText("Loading...");
		jProgressBar.setMinimum(0);
		jProgressBar.setMaximum(20000);
		jProgressBar.setValue(0);
		this.getContentPane().paint(this.getContentPane().getGraphics());

		taskDone = false;

		int cont = 0;
		while (taskDone == false) {
			cont = cont + 100;
			jLabelStatus.setText("Loading...");
			jProgressBar.setValue(cont);
			jLabelStatus.paint(this.jLabelStatus.getGraphics());
			jProgressBar.paint(this.jProgressBar.getGraphics());
			this.paint(this.getGraphics());
			parent.paint(parent.getGraphics());

			try {
				Thread.sleep(100);
			} catch (java.lang.InterruptedException e) {}
		}

		// Cursor en espera.
		super.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		if (taskDone == true)
			jLabelStatus.setText("Done                                             ");
		else
			jLabelStatus.setText("Overcome time of wait. Try it later.");

		jProgressBar.setValue(jProgressBar.getMaximum());
		this.getContentPane().paint(this.getContentPane().getGraphics());
	}

	// //////////////////////////////////////////////////////////////////////////////
	// //////// INICIALIZACI�N DE LOS COMPONENTES DEL JDIALOG PARA EL STEP
	// 1/////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: Step1Init. PERTENECE A: Clase FrmImportQuestionsFile. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A:
	 * JTextFieldInitStep1(); JPanelInitStep1(); JLabelInitStep1();
	 * JButtonInitStep1(); JDialogInitStep1(); RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los componentes del JDialog, necesarios para el paso 0
	 * del asistente para la importaci�n de ficheros, reservando memoria para los
	 * mismos y estableciendo su tama�o, su contenido, funcionalidad...
	 */

	private void Step1Init() {
		JPanelInitStep1();
		JLabelInitStep1();
		JButtonInitStep1();
		JTextFieldInitStep1();
		JComboInitStep1();

		try {
			JDialogInitStep1();
		} catch (java.lang.Exception e) {}

		step = "1";
	}

	/*
	 * NOMBRE: JPanelInitStep1. PERTENECE A: Clase DlgImportQuestionsFile.
	 * LLAMADA POR: Step1Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JPanel del JDialog, reservando memoria para los
	 * mismo y estableciendo su tama�o y su tipo de layout.
	 */

	private void JPanelInitStep1() {
		// Reserva de memoria para los JPanel.
		jPanelTitle = new JPanel();
		jPanelMain = new JPanel();

		// Establecimiento de sus tama�os, bordes y layout.
		jPanelTitle.setLayout(null);
		jPanelMain.setBorder(BorderFactory.createLoweredBevelBorder());
		jPanelMain.setBounds(new Rectangle(10, 50, 407, 140));

		jPanelMain.setLayout(null);
	}

	/*
	 * NOMBRE: JLabelInitStep1. PERTENECE A: Clase DlgImportQuestionsFile.
	 * LLAMADA POR: Step1Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JLabel del JDialog, reservando memoria para los
	 * mismo, estableciendo sus tama�osy sus textos.
	 */

	private void JLabelInitStep1() {
		// Reserva de memoria par los JLabel.
		jLabelTitle = new JLabel();
		jLabelCourse = new JLabel();
		jLabelConcept = new JLabel();
		jLabelQuestionsFile = new JLabel();
		jLabelNewQuestionsFile = new JLabel();

		// Establecimiento de sus tama�os, textos y bordes.
		jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 16));
		jLabelTitle.setBorder(BorderFactory.createLoweredBevelBorder());
		jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelTitle.setText("Select course and concept");
		jLabelTitle.setBounds(new Rectangle(10, 5, 407, 44));

		jLabelCourse.setBounds(new Rectangle(20, 17, 50, 17));
		jLabelCourse.setText("Course:");
		jLabelCourse.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelConcept.setBounds(new Rectangle(20, 47, 140, 17));
		jLabelConcept.setText("Associated concept");
		jLabelConcept.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelQuestionsFile.setBounds(new Rectangle(20, 77, 140, 17));
		jLabelQuestionsFile.setText("Question file to import:");
		jLabelQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelNewQuestionsFile.setBounds(new Rectangle(20, 107, 170, 17));
		jLabelNewQuestionsFile.setText("Question file name:");
		jLabelNewQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 12));
	}

	/*
	 * NOMBRE: JButtonInitStep1 PERTENECE A: Clase DlgImportQuestionsFile.
	 * LLAMADA POR: Step1Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JButton del JDialog, reservando memoria para los
	 * mismo y estableciendo sus tama�os y funcionalidades.
	 */

	private void JButtonInitStep1() {
		// Reserva de memoria para los JButton.
		NavigationButton = new JButton();
		OkButton = new JButton();
		CancelButton = new JButton();

		OkButton.setBounds(new Rectangle(195, 195, 100, 30));
		OkButton.setText("Ok");
		OkButton.setFont(new java.awt.Font("Button", 1, 12));

		// Establecimiento de la funcionalidad de OkButton.
		OkButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OkButtonActionPerformed();
			}
		});

		NavigationButton.setBounds(new Rectangle(20, 195, 120, 30));
		NavigationButton.setText("< Previous");
		NavigationButton.setFont(new java.awt.Font("Button", 1, 12));

		// Establecimiento de la funcionalidad de OkButton.
		NavigationButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Step0Init();
			}
		});

		CancelButton.setBounds(new Rectangle(310, 195, 100, 30));
		CancelButton.setText("Cancel");
		CancelButton.setFont(new java.awt.Font("Button", 1, 12));

		// Establecimiento de la funcionalidad de CancelButton.
		CancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelButtonActionPerformed();
			}
		});
	}

	/*
	 * NOMBRE: OkButtonActionPerformed. PERTENECE A: Clase
	 * DlgImportQuestionsFile. LLAMADA POR: Al hacer click sobre OkButton. LLAMA
	 * A: nada. RECIBE: nada, DEVUELVE: void. FUNCI�N: Oculta el JDialog,
	 * permaneciendo los valores en el contenido.
	 */

	void OkButtonActionPerformed() {
		id = true;
		this.setVisible(false);

		// Sustituci�n de los espacios en blanco por "_".
		String questionsFileName = jTextFieldQuestionsFile.getText().trim();
		questionsFileName = questionsFileName.replaceAll(" ", "_");
		jTextFieldQuestionsFile.setText(questionsFileName);

		this.dispose();
	}

	/*
	 * NOMBRE: JTextFieldInitStep1. PERTENECE A: Clase DlgImportQuestionsFile.
	 * LLAMADA POR: Step1Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JTextField del JDialog, reservando memoria para
	 * los mismo y estableciendo su tama�o y su funcionalidad.
	 */

	private void JTextFieldInitStep1() {
		// Reserva de memoria para el JTextFieldNewQuestionsFile.
		jTextFieldQuestionsFile = new JTextField();

		// Establecimiento de su tama�o.
		jTextFieldQuestionsFile.setBounds(new Rectangle(166, 105, 215, 25));

		jTextFieldQuestionsFile.setFocusable(false);
	}

	/*
	 * NOMBRE: JComboInitStep1. PERTENECE A: Clase DlgImportQuestionsFile.
	 * LLAMADA POR: Step1Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JCombo del JDialog, reservando memoria para los
	 * mismo y estableciendo su tama�o y su funcionalidad.
	 */

	private void JComboInitStep1() {
		// Reserva de memoria para los JComboBox.
		jComboCourse = new JComboBox();
		jComboConcept = new JComboBox();
		jComboQuestionsFile = new JComboBox();

		// Borrado de sus contenidos previos.
		jComboCourse.removeAllItems();
		jComboConcept.removeAllItems();
		jComboQuestionsFile.removeAllItems();

		// Establecimiento de sus tama�os.
		jComboCourse.setBounds(new Rectangle(86, 15, 295, 25));
		jComboConcept.setBounds(new Rectangle(166, 45, 215, 25));
		jComboQuestionsFile.setBounds(new Rectangle(166, 75, 215, 25));

		// Adici�n a jComboCourse de la lista de cursos del usuario.
		for (int i = 0; i < courseVector.size(); i++) {
			jComboCourse.addItem(((Course) courseVector.get(i)).getName());
		}

		// Only selects the test type concepts
		// TODO - test type concepts - before we make this
		//Vector conceptList = ((Course) courseVector.get(0)).getTestConceptNames();
		Vector conceptList = ((Course) courseVector.get(0)).getConceptNames();

		// Adici�n de la lista de ficheros de preguntas.
		if (conceptList != null && conceptList.isEmpty() == false) {
			courseName = (String) jComboCourse.getItemAt(0);
			for (int i = 0; i < conceptList.size(); i++) {
				if (conceptList.get(i).toString().indexOf(courseName) == 0)
					jComboConcept.addItem(conceptList.get(i).toString().trim().substring(
							conceptList.get(i).toString().trim().indexOf(courseName) + courseName.length() + 1));
				else
					jComboConcept.addItem(conceptList.get(i).toString().trim());
			}
		} else
			jComboConcept.removeAllItems();

		// Adici�n de la lista de ficheros de preguntas disponibles para importar.
		if (questionsFileNameOldVector != null && questionsFileNameOldVector.isEmpty() == false) {
			for (int i = 0; i < questionsFileNameOldVector.size(); i++)
				jComboQuestionsFile.addItem(questionsFileNameOldVector.get(i).toString().trim());

			// Establecimiento del nuevo nombre que recibira el fichero de
			// preguntas
			// en caso de que el JDialog haya sido invocado para exportar o
			// importar.
			jTextFieldQuestionsFile.setText(conceptList.get(0).toString().trim().substring(
					conceptList.get(0).toString().trim().indexOf(courseName) + courseName.length() + 1)
					+ "_" + ((Course) courseVector.get(0)).getName().trim() + "_items");
		} else
			jComboQuestionsFile.removeAllItems();

		if (jComboConcept.getItemCount() > 0 && jComboQuestionsFile.getItemCount() > 0)
			OkButton.setEnabled(true);
		else
			OkButton.setEnabled(false);

		// Establecimiento de la funcionalidad de jComboCourse.
		jComboCourse.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboCourseStep1ActionPerformed();
			}
		});

		jComboConcept.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboConceptStep1ActionPerformed();
			}
		});
	}

	/*
	 * NOMBRE: jComboCourseStep1ActionPerformed. PERTENECE A: Clase
	 * DlgImportQuestionsFile. LLAMADA POR: Al cambiar el valor mostrado por el
	 * jComboCourse. LLAMA A: nada. RECIBE: nada. DEVUELVE: void. FUNCI�N: Cambia
	 * el contenido del jComboConcept, estableciendo en el mismo la lista de
	 * ficheros de conceptos correspondientes al curso actualmente mostrado en el
	 * jComboCourse.
	 */

	void jComboCourseStep1ActionPerformed() {
		// Obtenci�n del nombre del curso actualmente mostrado en jComboCourse.
		String nameCourse = (String) jComboCourse.getSelectedItem();

		// Bucle para calcular la posici�n que ocupa el vector que contiene
		// la lista de ficheros de coceptos del curso mostrado en jComboCourse.
		Vector conceptVector = new Vector();
		int j = 0;
		for (j = 0; j < courseVector.size(); j++) {
			if (nameCourse.equals(((Course) courseVector.get(j)).getName())) {
				// TODO - test type concepts - before we make this
				//conceptVector = ((Course) courseVector.get(j)).getTestConceptNames();
				conceptVector = ((Course) courseVector.get(j)).getConceptNames();
				break;
			}
		}

		// Si la lista de ficheros de conceptos para el curso mostrado en
		// jComboCourse no es vac�a, entonces se muestra dicha lista, en caso
		// contrario, el contenido del jComboConcept queda vac�o,
		// y adem�s se deshabilita el OkButton.
		if (conceptVector != null && conceptVector.isEmpty() == false) {
			// Eliminado del contenido previo del jComboQuestionsFile.
			jComboConcept.removeAllItems();

			// Bucle para adici�n de los
			// nombres de los conceptos, correspondientes al curso
			// mostrado en jComboCourse, en jComboConcept.
			courseName = (String) jComboCourse.getSelectedItem();
			for (int i = 0; i < conceptVector.size(); i++) {
				jComboConcept.addItem(conceptVector.get(i).toString().substring(
						conceptVector.get(i).toString().trim().indexOf(courseName) + courseName.length() + 1));
			}

			jTextFieldQuestionsFile.setText(jComboConcept.getSelectedItem().toString().trim() + "_"
					+ jComboCourse.getSelectedItem().toString().trim() + "_items");
			// Habilitacion de OkButton.
			OkButton.setEnabled(true);
		} else {
			// Borrado del contenido previo de jComboConcept,
			// y deshabilitaci�n del OkButton.
			jComboConcept.removeAllItems();
			jTextFieldQuestionsFile.setText("");
			OkButton.setEnabled(false);
		}
	}

	/*
	 * NOMBRE: jComboConceptStep1ActionPerformed. PERTENECE A: Clase
	 * DlgImportQuestionsFile. LLAMADA POR: Al cambiar el valor mostrado por el
	 * jComboConcept. LLAMA A: nada. RECIBE: nada. DEVUELVE: void. FUNCI�N:
	 * Cambia el contenido de jTextFieldQuestionsFile seg�n el nombre del
	 * concepto mostrado actualmente en jComboConcept.
	 */

	void jComboConceptStep1ActionPerformed() {
		// String nameCourse = jComboCourse.getSelectedItem().toString().trim();

		String nameConcept = "";
		if (jComboConcept.getItemCount() > 0)
			nameConcept = jComboConcept.getSelectedItem().toString().trim();
		else
			nameConcept = "";

		if (nameConcept != null && nameConcept.equals("") == false) {
			jTextFieldQuestionsFile.setText(jComboConcept.getSelectedItem().toString().trim() + "_"
					+ jComboCourse.getSelectedItem().toString().trim() + "_items");
			// Habilitacion de OkButton.
			OkButton.setEnabled(true);
		} else {
			// Borrado del contenido de jTextFieldQuestionsFile.
			jTextFieldQuestionsFile.setText("");

			// Deshabilitaci�n de OkButton.
			OkButton.setEnabled(false);
		}
	}

	/*
	 * NOMBRE: JDialogInitStep1. PERTENECE A: Clase DlgImportQuestionsFile.
	 * LLAMADA POR: Step1Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa el JDialog. Establece el contenido de cada JPanel del
	 * JDialog.
	 */

	private void JDialogInitStep1() throws Exception {
		jPanelTitle.add(jLabelTitle, null);
		jPanelTitle.add(NavigationButton, null);
		jPanelTitle.add(OkButton, null);
		jPanelTitle.add(CancelButton, null);
		jPanelTitle.add(jPanelMain, null);
		jPanelMain.add(jLabelCourse, null);
		jPanelMain.add(jComboCourse, null);
		jPanelMain.add(jLabelConcept, null);
		jPanelMain.add(jComboConcept, null);
		jPanelMain.add(jLabelQuestionsFile, null);
		jPanelMain.add(jComboQuestionsFile, null);
		jPanelMain.add(jLabelNewQuestionsFile, null);
		jPanelMain.add(jTextFieldQuestionsFile, null);

		// Establecimiento de las caracter�sticas del JDialog, tama�o...
		this.setSize(433, 285);
		this.setResizable(false);
		this.setLocation(200, 150);
		this.setContentPane(jPanelTitle);
		this.setVisible(true);
		this.setModal(true);
		this.setTitle(title);
		this.setBackground(SystemColor.window);

		this.jPanelTitle.paint(this.jPanelTitle.getGraphics());
		this.jPanelMain.paint(this.jPanelMain.getGraphics());
		this.getContentPane().paint(this.getContentPane().getGraphics());
	}

	
	protected boolean getId() {
		return id;
	}
	
	protected String getCourseName()  {
		return (String) jComboCourse.getSelectedItem();
	}	

	protected String getConcept() {
		return (String) jComboConcept.getSelectedItem();
	}
	protected String getQuestionsFileName()  {
		return (String) jComboQuestionsFile.getSelectedItem();
	}

	protected String getNewQuestionsFileName() {
		return jTextFieldQuestionsFile.getText().trim();
	}
	
	protected String getTestOldPath() {
		return jTextFieldDirectoryTestOldPath.getText().trim();
	}
	
	
	/*
	 * NOMBRE: GetQuestionsFileListOld. FUNCION: Clase contenida dentro de la
	 * clase TestEditor. Es llamada por el m�todo construct de la clase
	 * SwingWorker. Se encarga de invocar al m�todo getQuestionsFileListOld de la
	 * clase padre para obtener los nombres de los ficheros de preguntas
	 * pertenecientes a las versiones anteriores de WOW y que est�n contenidos en
	 * el curso sobre el que se est� actuando.
	 */

	class GetQuestionsFileListOld extends Thread {
		String testOldPath = "";

		public GetQuestionsFileListOld(String testOldPath) {
			this.testOldPath = testOldPath;
		}

		public void run() {
			taskDone = false;

			try {
				Thread.sleep(100);
			} catch (java.lang.InterruptedException e) {}

			// Llamada al m�todo de esta misma clase que devuelve la lista de
			// nombres
			// de ficheros de todos los cursos, pero en este caso solo se le env�a
			// como
			// par�metro el nombre de un curso.
			questionsFileNameOldVector = parent.getQuestionsFileNamesOld(testOldPath);
			taskDone = true;
		}
	}

	/*
	 * NOMBRE: TimeControl. FUNCION: Clase contenida dentro de la clase
	 * DlgImportQuestionsFile. Se encarga de controlar que no se quede bloquada
	 * la barra de progreso.
	 */

	class TimeControl extends Thread {
		public TimeControl() {}

		public void run() {
			try {
				Thread.sleep(30000);

				taskDone = false;

				jLabelStatus.setText("Overcome time of wait. Try it later.");

				if (workerRepaintProgressBar != null)
					workerRepaintProgressBar.stop();

				if (workerGet != null)
					workerGet.stop();

			} catch (java.lang.InterruptedException e) {}
		}
	}

	/*
	 * NOMBRE: RepaintProgressBar. FUNCION: Clase contenida dentro de la clase
	 * DlgImportQuestionsFile. Se encarga de actualizar el valor y repintar la
	 * barra de progreso (jProgressBar).
	 */

	class RepaintProgressBar extends Thread {
		public void run() {
			repaintProgressBar();
		}
	}
	
}
