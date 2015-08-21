package es.uco.WOW.TestEditor;

import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/*
 * NOMBRE: DlgIrtParameters. FUNCION: JDialog que solicita al usuario los
 * valores para los par�metros de la dificultad, discriminaci�n y adivinanza.
 * LAST MODIFICATION: 06-02-2008
 */

public class DlgIrtParameters extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Variables de usuario.
	public double DIFFICULTY_MAX;

	public double DIFFICULTY_NORMAL;

	public double DIFFICULTY_MIN;

	public double DIFFICULTY_SELECT;

	public double DISCRIMINATION_MAX;

	public double DISCRIMINATION_MIN;

	public double DISCRIMINATION_NORMAL;

	public double DISCRIMINATION_SELECT;

	public double GUESSING_MAX;

	public double GUESSING_MIN;

	public double GUESSING_NORMAL;

	public double GUESSING_SELECT;

	// Componentes de la ventana.
	private JPanel jPanelTitle; // JPanel que muestra el t�tulo en jPanelMain.

	private JPanel jPanelMain; // JPanel principal del JDialog.

	private JLabel jLabelTitle;

	private JLabel jLabelDifficulty;

	private JLabel jLabelDiscrimination;

	private JLabel jLabelGuessing;

	public boolean id = false;

	private JButton OkButton;

	private JButton CancelButton;

	private JButton ResetButton;

	JSlider jSliderDifficulty;

	JSlider jSliderDiscrimination;

	JSlider jSliderGuessing;

	public JTextField jTextFieldDifficulty;

	public JTextField jTextFieldDiscrimination;

	public JTextField jTextFieldGuessing;

	/*
	 * NOMBRE: DlgIrtParameters (Constructor de esta misma clase). PERTENECE A:
	 * Clase DlgIrtParameters. LLAMADA POR: Al invocar la clase. LLAMA A:
	 * JPanelInit(); JLabelInit(); JSliderInit(); JButtonInit();
	 * JTextFieldInit(); JDialogInit(); RECIBE: title: Objeto de tipo String.
	 * Indica el t�tulo del JDialog. parent: Objeto de tipo JFrame. Hace
	 * referencia al JFrame que invoc� a este JDialog, osea su padre. DEVUELVE:
	 * nada. FUNCI�N: Constructor de la clase DlgIrtParameters. Se encarga de
	 * configurar el aspecto del dialogo, su funcionalidad, as� como de
	 * inicializar los componentes que componen el mismo.
	 */

	public DlgIrtParameters(String title, TestEditor padre) {
		// Llamada al constructor de la clase padre.
		super(padre, title, true);

		// Establecimiento del aspecto del JDialog.
		try {
			UIManager.setLookAndFeel(padre.lookAndFeelClassName);
		} catch (Exception e) {}

		// Establecimiento de los valores de las variables propias de la clase
		// con los recibidos.
		DIFFICULTY_MAX = padre.DIFFICULTY_MAX;
		DIFFICULTY_NORMAL = padre.DIFFICULTY_NORMAL;
		DIFFICULTY_MIN = padre.DIFFICULTY_MIN;
		DIFFICULTY_SELECT = padre.DIFFICULTY_SELECT;
		DISCRIMINATION_MAX = padre.DISCRIMINATION_MAX;
		DISCRIMINATION_MIN = padre.DISCRIMINATION_MIN;
		DISCRIMINATION_NORMAL = padre.DISCRIMINATION_NORMAL;
		DISCRIMINATION_SELECT = padre.DISCRIMINATION_SELECT;
		GUESSING_MAX = padre.GUESSING_MAX;
		GUESSING_MIN = padre.GUESSING_MIN;
		GUESSING_NORMAL = padre.GUESSING_NORMAL;
		GUESSING_SELECT = padre.GUESSING_SELECT;

		// Inicializaci�n de los componentes de la clase.
		try {
			// �������NO CAMBIAR EL ORDEN!!!!!!
			JPanelInit();
			JLabelInit();
			JSliderInit();
			JButtonInit();
			JTextFieldInit();

			JDialogInit();

			// Establecimiento de las caracter�sticas del JDialog, tama�o...
			setSize(433, 260);
			setResizable(false);
			setLocation(200, 150);
			getContentPane().add(jPanelTitle);
			setVisible(true);
			setModal(true);
			setTitle(title);
			setBackground(SystemColor.window);
		} catch (java.lang.Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "info", 1);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	// //////// INICIALIZACI�N DE LOS COMPONENTES DEL JDIALOG
	// ///////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: JPanelInit PERTENECE A: Clase DlgIrtParameters. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JPanel del JDialog, reservando
	 * memoria para los mismo y estableciendo su tama�o y su tipo de layout.
	 */

	private void JPanelInit() {
		// Reserva de memoria para los JPanel.
		jPanelTitle = new JPanel();
		jPanelMain = new JPanel();

		// Establecimiento de sus tama�os, bordes y layout.
		jPanelTitle.setLayout(null);
		jPanelMain.setBorder(BorderFactory.createLoweredBevelBorder());
		jPanelMain.setBounds(new Rectangle(10, 50, 407, 125));
		jPanelMain.setLayout(null);
	}

	/*
	 * NOMBRE: JLabelInit PERTENECE A: Clase DlgIrtParameters. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JLabel del JDialog, reservando
	 * memoria para los mismo, estableciendo sus tama�osy sus textos.
	 */

	private void JLabelInit() {
		// Reserva de memoria par los JLabel.
		jLabelTitle = new JLabel();
		jLabelDifficulty = new JLabel();
		jLabelDiscrimination = new JLabel();
		jLabelGuessing = new JLabel();

		// Establecimiento de sus tama�os, textos y bordes.
		jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 16));
		jLabelTitle.setBorder(BorderFactory.createLoweredBevelBorder());
		jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelTitle.setText(getTitle());
		jLabelTitle.setBounds(new Rectangle(10, 5, 407, 44));

		jLabelDifficulty.setBounds(new Rectangle(20, 15, 150, 20));
		jLabelDifficulty.setText("Difficulty ");
		jLabelDifficulty.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelDiscrimination.setBounds(new Rectangle(20, 65, 150, 20));
		jLabelDiscrimination.setText("Discrimination ");
		jLabelDiscrimination.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelGuessing.setBounds(new Rectangle(20, 115, 150, 20));
		jLabelGuessing.setText("Guessing ");
		jLabelGuessing.setFont(new java.awt.Font("Dialog", 1, 12));
	}

	/*
	 * NOMBRE: JButtonInit PERTENECE A: Clase DlgIrtParameters. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JButton del JDialog, reservando
	 * memoria para los mismo y estableciendo sus tama�os y funcionalidades.
	 */

	private void JButtonInit() {
		// Reserva de memoria para los JButton.
		ResetButton = new JButton();
		OkButton = new JButton();
		CancelButton = new JButton();

		// Establecimiento del tama�o, textos y tipo de letra para OkButton.
		ResetButton.setBounds(new Rectangle(20, 180, 100, 30));
		ResetButton.setText("Reset");
		ResetButton.setFont(new java.awt.Font("Button", 1, 12));

		ResetButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResetButtonActionPerformed();
			}
		});

		// Establecimiento del tama�o, textos y tipo de letra para OkButton.
		OkButton.setBounds(new Rectangle(195, 180, 100, 30));
		OkButton.setText("Ok");
		OkButton.setFont(new java.awt.Font("Button", 1, 12));

		OkButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OkButtonActionPerformed();
			}
		});

		// Establecimiento del tama�o, texto y tipo de letra para CancelButton.
		CancelButton.setBounds(new Rectangle(310, 180, 100, 30));
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
	 * NOMBRE: JSliderInit PERTENECE A: Clase DlgIrtParameters. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa los JSlider del JDialog, reservando
	 * memoria para los mismo y estableciendo su tama�o y su funcionalidad.
	 */

	private void JSliderInit() {
		// Reserva de memoria para el jSliderDifficulty
		jSliderDifficulty = new JSlider(JSlider.HORIZONTAL, (int) (DIFFICULTY_MIN * 10),
				(int) (DIFFICULTY_MAX * 10), (int) (DIFFICULTY_SELECT * 10));
		jSliderDifficulty.setPaintTicks(true);
		jSliderDifficulty.setPaintLabels(true);
		jSliderDifficulty.setMajorTickSpacing(10);
		jSliderDifficulty.setMinorTickSpacing(1);
		jSliderDifficulty.setBounds(new Rectangle(120, 15, 230, 45));

		// Bucle para el establecimiento de las etiquetas del jSliderDifficulty.
		Dictionary jSliderDifficultyLabelTable = new Hashtable();
		for (int i = (int) (DIFFICULTY_MIN * 10); i <= (int) (DIFFICULTY_MAX * 10); i++) {
			if (i % 10 == 0)
				jSliderDifficultyLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 10)));
		}

		jSliderDifficulty.setLabelTable(jSliderDifficultyLabelTable);

		// Reserva de memoria para el jSliderDiscrimination.
		jSliderDiscrimination = new JSlider(JSlider.HORIZONTAL, (int) (DISCRIMINATION_MIN * 10),
				(int) (DISCRIMINATION_MAX * 10), (int) (DISCRIMINATION_SELECT * 10));
		jSliderDiscrimination.setPaintTicks(true);
		jSliderDiscrimination.setPaintLabels(true);
		jSliderDiscrimination.setMajorTickSpacing(10);
		jSliderDiscrimination.setMinorTickSpacing(1);
		jSliderDiscrimination.setBounds(new Rectangle(120, 65, 230, 45));

		// Bucle para el establecimiento de las etiquetas del jSliderDifficulty.
		Dictionary jSliderDiscriminationLabelTable = new Hashtable();
		for (int i = (int) (DISCRIMINATION_MIN * 10); i <= (int) (DISCRIMINATION_MAX * 10); i++) {
			if (i % 10 == 0)
				jSliderDiscriminationLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 10.0)));
		}

		jSliderDiscrimination.setLabelTable(jSliderDiscriminationLabelTable);

		// Reserva de memoria para el jSliderGuessing.
		jSliderGuessing = new JSlider(JSlider.HORIZONTAL, (int) (GUESSING_MIN * 100),
				(int) (GUESSING_MAX * 100), (int) (GUESSING_SELECT * 100));
		jSliderGuessing.setPaintTicks(true);
		jSliderGuessing.setPaintLabels(true);
		jSliderGuessing.setMajorTickSpacing(10);
		jSliderGuessing.setMinorTickSpacing(1);
		jSliderGuessing.setBounds(new Rectangle(120, 115, 230, 45));

		// Bucle para el establecimiento de las etiquetas del jSliderGuessing.
		Dictionary jSliderGuessingLabelTable = new Hashtable();
		for (int i = (int) (GUESSING_MIN * 100); i <= (int) (GUESSING_MAX * 100); i++) {
			if (i % 10 == 0)
				jSliderGuessingLabelTable.put(new Integer(i), new JLabel(String.valueOf(i / 100.0)));
		}

		jSliderGuessing.setLabelTable(jSliderGuessingLabelTable);

		// Establecimiento de la funcionalidad de jSliderDifficulty.
		jSliderDifficulty.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jTextFieldDifficulty.setText(String.valueOf(jSliderDifficulty.getValue() / 10.0));
			}
		});

		// Establecimiento de la funcionalidad de jSliderDiscrimination.
		jSliderDiscrimination.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jTextFieldDiscrimination.setText(String.valueOf(jSliderDiscrimination.getValue() / 10.0));
			}
		});

		// Establecimiento de la funcionalidad de jSliderGuessing.
		jSliderGuessing.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jTextFieldGuessing.setText(String.valueOf(jSliderGuessing.getValue() / 100.0));
			}
		});
	}

	/*
	 * NOMBRE: JTextFieldInit PERTENECE A: Clase DlgIrtParameters. LLAMADA POR:
	 * El constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE:
	 * void. DEVUELVE: void. FUNCI�N: Inicializa los JTextField del JDialog,
	 * reservando memoria para los mismo y estableciendo su tama�o y su
	 * funcionalidad.
	 */

	private void JTextFieldInit() {
		// Reserva de memoria para los JTextField.
		jTextFieldDifficulty = new JTextField(String.valueOf(DIFFICULTY_SELECT), 4);
		jTextFieldDifficulty.setBounds(new Rectangle(355, 15, 40, 25));

		jTextFieldDiscrimination = new JTextField(String.valueOf(DISCRIMINATION_SELECT), 4);
		jTextFieldDiscrimination.setBounds(new Rectangle(355, 65, 40, 25));

		jTextFieldGuessing = new JTextField(String.valueOf(GUESSING_SELECT), 4);
		jTextFieldGuessing.setBounds(new Rectangle(355, 115, 40, 25));

		// Establecimiento de la funcionalidad de jTextFieldDifficulty.
		jTextFieldDifficulty.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldDifficulty.getText().length() > 0) {
						if (Double.valueOf(jTextFieldDifficulty.getText()).doubleValue() < DIFFICULTY_MIN) {
							jTextFieldDifficulty.setText(String.valueOf(DIFFICULTY_MIN * 10));
							jSliderDifficulty.setValue((int) DIFFICULTY_MIN * 10);
						} else {
							if (Double.valueOf(jTextFieldDifficulty.getText()).doubleValue() > DIFFICULTY_MAX) {
								jTextFieldDifficulty.setText(String.valueOf(DIFFICULTY_MAX * 10));
								jSliderDifficulty.setValue((int) DIFFICULTY_MAX * 10);
							} else {
								jSliderDifficulty.setValue((int) (Double.valueOf(jTextFieldDifficulty.getText())
										.doubleValue() * 10));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldDiscrimination.
		jTextFieldDiscrimination.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldDiscrimination.getText().length() > 0) {
						if (Double.valueOf(jTextFieldDiscrimination.getText()).doubleValue() < DISCRIMINATION_MIN) {
							jTextFieldDiscrimination.setText(String.valueOf(DISCRIMINATION_MIN * 10));
							jSliderDiscrimination.setValue((int) DISCRIMINATION_MIN * 10);
						} else {
							if (Double.valueOf(jTextFieldDiscrimination.getText()).doubleValue() > DISCRIMINATION_MAX) {
								jTextFieldDiscrimination.setText(String.valueOf(DISCRIMINATION_MAX * 10));
								jSliderDiscrimination.setValue((int) DISCRIMINATION_MAX * 10);
							} else {
								jSliderDiscrimination.setValue((int) (Double.valueOf(
										jTextFieldDiscrimination.getText()).doubleValue() * 10));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {}
			}
		});

		// Establecimiento de la funcionalidad de jTextFieldGuessing.
		jTextFieldGuessing.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					if (jTextFieldGuessing.getText().length() > 0) {
						if (Double.valueOf(jTextFieldGuessing.getText()).doubleValue() < GUESSING_MIN) {
							jTextFieldGuessing.setText(String.valueOf(GUESSING_MIN));
							jSliderGuessing.setValue((int) (GUESSING_MIN * 100));
						} else {
							if (Double.valueOf(jTextFieldGuessing.getText()).doubleValue() > GUESSING_MAX) {
								jTextFieldGuessing.setText(String.valueOf(GUESSING_MAX));
								jSliderGuessing.setValue((int) (GUESSING_MAX * 100));

								JOptionPane.showMessageDialog(null, String.valueOf((int) (GUESSING_MAX * 100)),
										"info", 1);
							} else {
								jSliderGuessing.setValue((int) (Double.valueOf(jTextFieldGuessing.getText())
										.doubleValue() * 100));
							}
						}
					}
				} catch (java.lang.NumberFormatException nfe) {}
			}
		});
	}

	/*
	 * NOMBRE: JDialogInit PERTENECE A: Clase DlgIrtParameters. LLAMADA POR: El
	 * constructor de la clase a la que pertenece. LLAMA A: nada. RECIBE: void.
	 * DEVUELVE: void. FUNCI�N: Inicializa el JDialog. Establece el contenido de
	 * cada JPanel del JDialog.
	 */

	private void JDialogInit() throws Exception {
		jPanelTitle.add(jLabelTitle, null);
		jPanelTitle.add(ResetButton, null);
		jPanelTitle.add(OkButton, null);
		jPanelTitle.add(CancelButton, null);
		jPanelTitle.add(jPanelMain, null);
		jPanelMain.add(jLabelDifficulty, null);
		jPanelMain.add(jSliderDifficulty, null);
		jPanelMain.add(jTextFieldDifficulty, null);
		jPanelMain.add(jLabelDiscrimination, null);
		jPanelMain.add(jSliderDiscrimination, null);
		jPanelMain.add(jTextFieldDiscrimination, null);
		// jPanelMain.add(jLabelGuessing, null);
		// jPanelMain.add(jSliderGuessing, null);
		// jPanelMain.add(jTextFieldGuessing, null);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// //////// FUNCIONALIDADES DE LOS COMPONETES DEL JDIALOG
	// ///////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/*
	 * NOMBRE: ResetButtonActionPerformed. PERTENECE A: Clase DlgIrtParameters.
	 * LLAMADA POR: Al hacer click sobre ResetButton. LLAMA A: nada. RECIBE:
	 * nada. DEVUELVE: void. FUNCI�N: Resetea los valores mostrados en el
	 * JDialog.
	 */

	public void ResetButtonActionPerformed() {
		// Establecimiento de los valores por defecto en los JTextField.
		jTextFieldDifficulty.setText(String.valueOf(DIFFICULTY_NORMAL));

		jTextFieldDiscrimination.setText(String.valueOf(DISCRIMINATION_NORMAL));
		jTextFieldGuessing.setText(String.valueOf(GUESSING_NORMAL));

		jSliderDifficulty.setValue((int) (DIFFICULTY_NORMAL * 10));
		jSliderDiscrimination.setValue((int) (DISCRIMINATION_NORMAL * 10));
		jSliderGuessing.setValue((int) (GUESSING_NORMAL * 100));
	}

	/*
	 * NOMBRE: OkButtonActionPerformed. PERTENECE A: Clase DlgIrtParameters.
	 * LLAMADA POR: Al hacer click sobre OkButton. LLAMA A: nada. RECIBE: nada.
	 * DEVUELVE: void. FUNCI�N: Oculta el JDialog, permaneciendo los valores en
	 * el contenido.
	 */

	public void OkButtonActionPerformed() {
		id = true;
		setVisible(false);
		dispose();
	}

	/*
	 * NOMBRE: CancelButtonActionPerformed. PERTENECE A: Clase DlgIrtParameters.
	 * LLAMADA POR: Al hacer click sobre CancelButton. LLAMA A: nada. RECIBE:
	 * nada. DEVUELVE: void. FUNCI�N: Oculta el JDialog, eliminando los valores
	 * en el contenido.
	 */

	public void CancelButtonActionPerformed() {
		id = false;
		setVisible(false);

		// Establecimiento de los valores por defecto en los JTextField.
		jTextFieldDifficulty.setText(String.valueOf(DIFFICULTY_SELECT));
		jTextFieldDiscrimination.setText(String.valueOf(DISCRIMINATION_SELECT));
		jTextFieldGuessing.setText(String.valueOf(GUESSING_SELECT));

		dispose();
	}
}
