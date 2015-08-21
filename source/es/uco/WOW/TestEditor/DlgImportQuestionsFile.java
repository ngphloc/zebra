package es.uco.WOW.TestEditor;

import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
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
 * NAME: DlgImportQuestionsFile
 * FUNCTION: Window that shows a wizard to import a
 * file of questions in another format to other file of questions in the WOW!
 * current format.
 * LAST MODIFICATION: 04-09-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class DlgImportQuestionsFile extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Vector courseVector;

	private String title = "";

	private String courseName = "";
	
	private String theFilePath = ""; 
	
	// Componentes de la ventana.
	private JPanel jPanelTitle; // JPanel que muestra el t�tulo en jPanelMain.

	private JPanel jPanelMain; // JPanel principal del JDialog.

	private JLabel jLabelTitle;

	private JLabel jLabelCourse;

	private JLabel jLabelConcept;

	private JLabel jLabelQuestionsFile;

	private JLabel jLabelNewQuestionsFile;

	private boolean id = false;

	private JButton OkButton;

	private JButton CancelButton;

	private JComboBox jComboCourse;

	private JComboBox jComboConcept;

	private JComboBox jComboQuestionsFile;

	private JTextField jTextFieldQuestionsFile;

	/**
	 * 
	 * @param aTitle
	 * @param theCourseVector
	 * @param parent
	 * @param thefilePath
	 */
	public DlgImportQuestionsFile(final String aTitle, final Vector theCourseVector,
			final TestEditor parent, final String thefilePath) {
		// Llamada al constructor de la clase padre.
		super(parent, aTitle, true);

		// Establecimiento de los valores de las variables propias de la clase
		// con los recibidos.
		this.title = aTitle;
		this.courseVector = theCourseVector;
		this.theFilePath = thefilePath;

		// Establecimiento del aspecto del JDialog.
		try {
			UIManager.setLookAndFeel(parent.lookAndFeelClassName);
		} catch (Exception e) {}

		// Inicializaci�n de los componentes de la clase.
		try {
			Step1Init();
		} catch (Exception e) {}
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

		//	 Deletes the content of the components
		// Prevents exceptions during event dispaching
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
		// Sustituci�n de los espacios en blanco por "_".
		String questionsFileName = jTextFieldQuestionsFile.getText().trim();
		if (!questionsFileName.equals("")) {
			id = true;
			this.setVisible(false);
			questionsFileName = questionsFileName.replaceAll(" ", "_");
			jTextFieldQuestionsFile.setText(questionsFileName);
			this.dispose();
		} else {
			OkButton.setEnabled(false);
		}
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

		// It only can import to a test concept type
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

		// Adici�n del fichero de preguntas disponible para importar.
		jComboQuestionsFile.addItem(theFilePath.trim());

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
		
		jComboCourseStep1ActionPerformed();
	}
	
	/**
	 *
	 */
	void jComboCourseStep1ActionPerformed() {
		// Obtenci�n del nombre del curso actualmente mostrado en jComboCourse.
		String nameCourse = (String) jComboCourse.getSelectedItem();

		if (nameCourse == null) {
			// There is no element selected
			return;
		}

		// Bucle para calcular la posici�n que ocupa el vector que contiene
		// la lista de ficheros de coceptos del curso mostrado en jComboCourse.
		Vector conceptVector = new Vector();
		int j = 0;
		for (j = 0; j < courseVector.size(); j++) {
			Course theCourse = (Course) courseVector.get(j);
			if (theCourse != null)  {
				String theName = (theCourse).getName();
				if (nameCourse.equals(theName)) {
					// TODO - test type concepts - before we make this
					//conceptVector = ((Course) courseVector.get(j)).getTestConceptNames();
					conceptVector = ((Course) courseVector.get(j)).getConceptNames();
					break;
				}
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


	
}
