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
import javax.swing.JPanel;
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
 * NAME: DlgExportQuestionsFile
 * FUNCTION: Window that shows a wizard to export a
 * file of questions in the WOW! format to another file of questions in another
 * format.
 * LAST MODIFICATION: 04-09-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class DlgExportQuestionsFile extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Variables del usuario.
	private Vector courseVector; // Almacena la lista de cursos

	// pertenecientes al usuario.
	private String title = "";

	// Componentes de la ventana.
	private JPanel jPanelTitle; // JPanel que muestra el t�tulo en jPanelMain.

	private JPanel jPanelMain; // JPanel principal del JDialog.

	private JLabel jLabelTitle;

	private JLabel jLabelCourse;

	private JLabel jLabelQuestionsFile;

	private JLabel jLabelNewQuestionsFile;

	private boolean id = false;

	private JButton OkButton;

	private JButton CancelButton;

	private JComboBox jComboCourse;

	private JComboBox jComboQuestionsFile;

	private JTextField jTextFieldQuestionsFile;


	/**
	 * 
	 * @param aTitle
	 * @param aCourseVector
	 * @param parent
	 */
	public DlgExportQuestionsFile(final String aTitle, final Vector aCourseVector,
			final TestEditor parent) {
		// Calls the parent constructor
		super(parent, aTitle, true);

		// Sets the user variables
		this.title = aTitle;
		this.courseVector = aCourseVector;

		// Sets the look and feel
		try {
			UIManager.setLookAndFeel(parent.lookAndFeelClassName);
		} catch (Exception e) {}

		// Initializes the components of the dialog
		try {
			Step0Init();
		} catch (Exception e) {}
	}

	
	// //////////////////////////////////////////////////////////////////////////////
	// //////// INICIALIZACI�N DE LOS COMPONENTES DEL JDIALOG PARA EL STEP
	// 0/////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: Step0Init. PERTENECE A: Clase FrmExportQuestionsFile. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A:
	 * JTextFieldInitStep0(); JPanelInitStep0(); JLabelInitStep0();
	 * JButtonInitStep0(); JDialogInit(); RECIBE: void. DEVUELVE: void. FUNCI�N:
	 * Inicializa los componentes del JDialog, necesarios para el paso 0 del
	 * asistente para la importaci�n de ficheros, reservando memoria para los
	 * mismos y estableciendo su tama�o, su contenido, funcionalidad...
	 */

	private void Step0Init() {
		JPanelInitStep0();
		JLabelInitStep0();
		JButtonInitStep0();
		JTextFieldInitStep0();
		JComboInitStep0();
		
		try {
			JDialogInitStep0();
		} catch (java.lang.Exception e) {}
	}

	/*
	 * NOMBRE: JPanelInitStep0. PERTENECE A: Clase DlgExportQuestionsFile.
	 * LLAMADA POR: Step0Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JPanel del JDialog, reservando memoria para los
	 * mismo y estableciendo su tama�o y su tipo de layout.
	 */

	private void JPanelInitStep0() {
		// Reserva de memoria para los JPanel.
		jPanelTitle = new JPanel();
		jPanelMain = new JPanel();

		// Establecimiento de sus tama�os, bordes y layout.
		jPanelTitle.setLayout(null);
		jPanelMain.setBorder(BorderFactory.createLoweredBevelBorder());
		jPanelMain.setBounds(new Rectangle(10, 50, 407, 110));

		jPanelMain.setLayout(null);
	}

	/*
	 * NOMBRE: JLabelInitStep0. PERTENECE A: Clase DlgExportQuestionsFile.
	 * LLAMADA POR: Step0Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JLabel del JDialog, reservando memoria para los
	 * mismo, estableciendo sus tama�osy sus textos.
	 */

	private void JLabelInitStep0() {
		// Reserva de memoria par los JLabel.
		jLabelTitle = new JLabel();
		jLabelCourse = new JLabel();
		jLabelQuestionsFile = new JLabel();
		jLabelNewQuestionsFile = new JLabel();

		// Establecimiento de sus tama�os, textos y bordes.
		jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 16));
		jLabelTitle.setBorder(BorderFactory.createLoweredBevelBorder());
		jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelTitle.setText("Select Course and Concept");
		jLabelTitle.setBounds(new Rectangle(10, 5, 407, 44));

		jLabelCourse.setBounds(new Rectangle(20, 17, 50, 17));
		jLabelCourse.setText("Course:");
		jLabelCourse.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelQuestionsFile.setBounds(new Rectangle(20, 47, 140, 17));
		jLabelQuestionsFile.setText("Question file to export:");
		jLabelQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelNewQuestionsFile.setBounds(new Rectangle(20, 77, 170, 17));
		jLabelNewQuestionsFile.setText("New question file name:");
		jLabelNewQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 12));
	}

	/*
	 * NOMBRE: JButtonInitStep0. PERTENECE A: Clase DlgExportQuestionsFile.
	 * LLAMADA POR: Step0Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JButton del JDialog, reservando memoria para los
	 * mismo y estableciendo sus tama�os y funcionalidades.
	 */

	private void JButtonInitStep0() {
		// Reserva de memoria para los JButton.
		OkButton = new JButton();
		OkButton.setBounds(new Rectangle(195, 165, 100, 30));
		OkButton.setText("Ok");
		OkButton.setFont(new java.awt.Font("Button", 1, 12));
		OkButton.setEnabled(false);

		// Establecimiento de la funcionalidad de OkButton.
		OkButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OkButtonActionPerformed();
			}
		});

		CancelButton = new JButton();
		// Establecimiento del tama�o, texto y tipo de letra para CancelButton.
		CancelButton.setBounds(new Rectangle(310, 165, 100, 30));
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
	 * NOMBRE: JTextFieldInitStep0. PERTENECE A: Clase DlgExportQuestionsFile.
	 * LLAMADA POR: Step0Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JTextField del JDialog, reservando memoria para
	 * los mismo y estableciendo su tama�o y su funcionalidad.
	 */

	private void JTextFieldInitStep0() {
		// Reserva de memoria para el JTextFieldNewQuestionsFile.
		jTextFieldQuestionsFile = new JTextField();

		// Establecimiento de su tama�o.
		jTextFieldQuestionsFile.setBounds(new Rectangle(166, 75, 215, 25));

		// Establecimiento de su funcionalidad.
		jTextFieldQuestionsFile.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				jTextFieldQuestionsFileActionPerformed();
			}
		});

	}

	/*
	 * NOMBRE: jTextFieldQuestionsFileActionPerformed. PERTENECE A: Clase
	 * DlgExportQuestionsFile. LLAMADA POR: Al escribir en el
	 * jTextFieldQuestionsFile. LLAMA A: nada. RECIBE: nada. DEVUELVE: void.
	 * FUNCI�N: Habilita o desabilita el OkButton.
	 */
	private void jTextFieldQuestionsFileActionPerformed() {
		if (jTextFieldQuestionsFile.getText().trim().equals("") == false) {
			OkButton.setEnabled(true);
		} else {
			OkButton.setEnabled(false);
		}

		if (jComboQuestionsFile.getItemCount() == 0) {
			OkButton.setEnabled(false);
		}
	}

	/*
	 * NOMBRE: JComboInitStep0. PERTENECE A: Clase DlgExportQuestionsFile.
	 * LLAMADA POR: Step0Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa los JCombo del JDialog, reservando memoria para los
	 * mismo y estableciendo su tama�o y su funcionalidad.
	 */

	private void JComboInitStep0() {
		// Reserva de memoria para los JComboBox.
		jComboCourse = new JComboBox();
		jComboQuestionsFile = new JComboBox();

		// Borrado de sus contenidos previos.
		jComboCourse.removeAllItems();
		jComboQuestionsFile.removeAllItems();

		// Establecimiento de sus tama�os.
		jComboCourse.setBounds(new Rectangle(86, 15, 295, 25));
		jComboQuestionsFile.setBounds(new Rectangle(166, 45, 215, 25));

		// Adici�n a jComboCourse de la lista de cursos del usuario.
		for (int i = 0; i < courseVector.size(); i++)
			jComboCourse.addItem(((Course) courseVector.get(i)).getName());

		Vector questionsFileNameVector = ((Course) courseVector.get(0)).getQuestionsFileNames();

		// Adici�n de la lista de ficheros de preguntas.
		if (questionsFileNameVector != null && questionsFileNameVector.isEmpty() == false) {
			for (int i = 0; i < questionsFileNameVector.size(); i++)
				jComboQuestionsFile.addItem(questionsFileNameVector.get(i).toString().trim());

		} else {
			jComboQuestionsFile.removeAllItems();
		}

		if (jComboQuestionsFile.getItemCount() == 0) {
			OkButton.setEnabled(false);
		}

		// Establecimiento de la funcionalidad de jComboCourse.
		jComboCourse.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboCourseStep0ActionPerformed();
			}
		});
	}

	/*
	 * NOMBRE: jComboCourseStep0ActionPerformed. PERTENECE A: Clase
	 * DlgExportQuestionsFile. LLAMADA POR: Al cambiar el valor mostrado por el
	 * jComboCourse. LLAMA A: nada. RECIBE: nada. DEVUELVE: void. FUNCI�N: Cambia
	 * el contenido del jComboConcept, estableciendo en el mismo la lista de
	 * ficheros de conceptos correspondientes al curso actualmente mostrado en el
	 * jComboCourse.
	 */

	private void jComboCourseStep0ActionPerformed() {
		// Obtenci�n del nombre del curso actualmente mostrado en jComboCourse.
		String nameCourse = (String) jComboCourse.getSelectedItem();

		if (nameCourse == null) {
			return;
		}
		
		// Bucle para calcular la posici�n que ocupa el vector que contiene
		// la lista de ficheros de coceptos del curso mostrado en jComboCourse.
		Vector questionsFileNameVector = new Vector();
		int j = 0;
		for (j = 0; j < courseVector.size(); j++) {
			String value = ((Course) courseVector.get(j)).getName();
			if (value != null && nameCourse.equals(value)) {
				questionsFileNameVector = ((Course) courseVector.get(j)).getQuestionsFileNames();
				break;
			}
		}

		// Si la lista de ficheros de preguntas para el curso mostrado en
		// jComboCourse no es vac�a, entonces se muestra dicha lista, en caso
		// contrario, el contenido del jComboQuestionsFile queda vac�o,
		// y adem�s se deshabilita el NavigationButton.
		if (questionsFileNameVector != null && questionsFileNameVector.isEmpty() == false) {
			// Eliminado del contenido previo del jComboQuestionsFile.
			jComboQuestionsFile.removeAllItems();

			// Bucle para adici�n de los
			// nombres de los ficheros de preguntas, correspondientes al curso
			// mostrado en jComboCourse, en jComboQuestionsFile.
			for (int i = 0; i < questionsFileNameVector.size(); i++) {
				jComboQuestionsFile.addItem(questionsFileNameVector.get(i).toString());
			}

			if (jTextFieldQuestionsFile.getText().trim().equals("") == false) {
				OkButton.setEnabled(true);
			}
		} else {
			// Borrado del contenido previo de jComboConcept,
			// y deshabilitaci�n del OkButton.
			jComboQuestionsFile.removeAllItems();
			jTextFieldQuestionsFile.setText("");
			OkButton.setEnabled(false);
		}
	}

	/*
	 * NOMBRE: JDialogInitStep0. PERTENECE A: Clase DlgExportQuestionsFile.
	 * LLAMADA POR: Step0Init(); LLAMA A: nada. RECIBE: void. DEVUELVE: void.
	 * FUNCI�N: Inicializa el JDialog. Establece el contenido de cada JPanel del
	 * JDialog.
	 */

	private void JDialogInitStep0() throws Exception {
		jPanelTitle.add(jLabelTitle, null);
		jPanelTitle.add(OkButton, null);
		jPanelTitle.add(CancelButton, null);
		jPanelTitle.add(jPanelMain, null);
		jPanelMain.add(jLabelCourse, null);
		jPanelMain.add(jComboCourse, null);
		jPanelMain.add(jLabelQuestionsFile, null);
		jPanelMain.add(jComboQuestionsFile, null);
		jPanelMain.add(jLabelNewQuestionsFile, null);
		jPanelMain.add(jTextFieldQuestionsFile, null);

		// Establecimiento de las caracter�sticas del JDialog, tama�o...
		this.setSize(433, 265);
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

	/*
	 * NOMBRE: OkButtonActionPerformed. PERTENECE A: Clase
	 * DlgExportQuestionsFile. LLAMADA POR: Al hacer click sobre OkButton. LLAMA
	 * A: nada. RECIBE: nada, DEVUELVE: void. FUNCI�N: Oculta el JDialog,
	 * permaneciendo los valores en el contenido.
	 */
	private void OkButtonActionPerformed() {
		this.setVisible(false);

		// Sustituci�n de los espacios en blanco por "_".
		String questionsFileName = jTextFieldQuestionsFile.getText().trim();
		questionsFileName = questionsFileName.replaceAll(" ", "_");
		jTextFieldQuestionsFile.setText(questionsFileName);
		id = true;

		this.dispose();
	}

	/*
	 * NOMBRE: CancelButtonActionPerformed. PERTENECE A: Clase
	 * DlgExportQuestionsFile. LLAMADA POR: Al hacer click sobre CancelButton
	 * configurado en Step0Init. LLAMA A: nada. RECIBE: nada. DEVUELVE: void.
	 * FUNCI�N: Oculta el JDialog, eliminando los valores en el contenido.
	 */

	private void CancelButtonActionPerformed() {
		id = false;
		this.setVisible(false);

		// Borrado del contenido de los componentes del JDialog.
		jComboCourse.removeAllItems();
		jComboQuestionsFile.removeAllItems();
		jTextFieldQuestionsFile.setText("");
		this.dispose();
	}


	protected boolean getId() {
		return id;
	}
	
	protected String getCourseName()  {
		return (String) jComboCourse.getSelectedItem();
	}	

	protected String getQuestionsFileName()  {
		return (String) jComboQuestionsFile.getSelectedItem();
	}

	protected String getNewQuestionsFileName() {
		return jTextFieldQuestionsFile.getText();
	}
}
