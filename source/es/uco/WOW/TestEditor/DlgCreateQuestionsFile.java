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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import es.uco.WOW.Utils.Course;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p> 
 * @version 1.0
 */

/**
 * This class shows the list of courses that the user is owner and request
 * the name for the new question file that will be created in a course.
 * It also request for the default number of answers for the question file
 * that will be created. By default, this parameter is 2.
 * LAST MODIFICATION: 06-10-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class DlgCreateQuestionsFile extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Stores the list of courses of the author */
	private Vector courseVector = new Vector();

	/** Stores the selected course */
	private String courseName = "";

	protected boolean id = false;

	private JPanel jPanelTitle;

	private JPanel jPanelMain;

	private JLabel jLabelTitle;

	private JLabel jLabelCourse;

	private JLabel jLabelConcept;

	private JLabel jLabelQuestionsFile;

	private JLabel jLabelNumAnswers;

	private JButton OkButton;

	private JButton CancelButton;

	protected JComboBox jComboCourse;

	protected JComboBox jComboConcept;

	protected JComboBox jComboNumAnswers;

	protected JTextField jTextFieldQuestionsFile;


	/**
	 * Constructor of the class. Sets the look and feel, and initializes it.
	 * @param title Title of the dialog window
	 * @param aCourseVector Vector with the name of the courses that belongs to the author.
	 * @param parent Reference to the JFrame that invokes this dialog window.
	 */
	public DlgCreateQuestionsFile(final String title, final Vector aCourseVector, final TestEditor parent) {
		// Calls the parent constructor
		super(parent, title, true);

		// Sets the look and feel
		try {
			UIManager.setLookAndFeel(parent.lookAndFeelClassName);
		} catch (Exception e) {}

		// Sets the values of the class variables
		this.courseVector = aCourseVector;

		// Initializes the graphic components
		try {
			// DON'T MODIFY THE ORDER!!!!!!
			JPanelInit();
			JLabelInit();
			JButtonInit();
			JTextFieldInit();
			JComboInit();
			JDialogInit();

			// Sets graphic properties
			this.setSize(433, 285);
			this.setResizable(false);
			this.setLocation(200, 150);
			this.getContentPane().add(jPanelTitle);
			this.setVisible(true);
			this.setModal(true);
			this.setTitle(title);
			this.setBackground(SystemColor.window);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "info", 1);
		}
	}

	/**
	 * Initializes the JPanel objets of the window.
	 */
	private void JPanelInit() {
		jPanelTitle = new JPanel();
		jPanelTitle.setLayout(null);
		jPanelMain = new JPanel();
		jPanelMain.setBorder(BorderFactory.createLoweredBevelBorder());
		jPanelMain.setBounds(new Rectangle(10, 50, 407, 140));
		jPanelMain.setLayout(null);
	}

	/**
	 * Initializes the JLabel objets of the window.
	 */
	private void JLabelInit() {
		// Creates the JLabel objects
		jLabelTitle = new JLabel();
		jLabelCourse = new JLabel();
		jLabelConcept = new JLabel();
		jLabelQuestionsFile = new JLabel();
		jLabelNumAnswers = new JLabel();

		// Set their sizes, texts and borders
		jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 16));
		jLabelTitle.setBorder(BorderFactory.createLoweredBevelBorder());
		jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelTitle.setText(this.getTitle());
		jLabelTitle.setBounds(new Rectangle(10, 5, 407, 44));

		jLabelCourse.setBounds(new Rectangle(20, 17, 50, 17));
		jLabelCourse.setText("Course:");
		jLabelCourse.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelConcept.setBounds(new Rectangle(20, 47, 140, 17));
		jLabelConcept.setText("Associate concept:");
		jLabelConcept.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelQuestionsFile.setBounds(new Rectangle(20, 77, 140, 17));
		jLabelQuestionsFile.setText("Question file name:");
		jLabelQuestionsFile.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelNumAnswers.setBounds(new Rectangle(20, 107, 420, 17));
		jLabelNumAnswers.setText("Minimum number of answers for each question:");
		jLabelNumAnswers.setFont(new java.awt.Font("Dialog", 1, 12));
	}

	/**
	 * Initializes the JButton objets of the window.
	 */
	private void JButtonInit() {
		// Creates the JButton objects
		OkButton = new JButton();
		CancelButton = new JButton();

		// Sets the size, text and letter type for OkButton
		OkButton.setBounds(new Rectangle(195, 195, 100, 30));
		OkButton.setText("Ok");
		OkButton.setFont(new java.awt.Font("Button", 1, 12));

		// Sets the behaviour of OkButton.
		OkButton.setEnabled(false);
		OkButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OkButtonActionPerformed(e);
			}
		});

		// Sets the size, text and letter type for CancelButton.
		CancelButton.setBounds(new Rectangle(310, 195, 100, 30));
		CancelButton.setText("Cancel");
		CancelButton.setFont(new java.awt.Font("Button", 1, 12));

		// Sets the behaviour of CancelButton.
		CancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelButtonActionPerformed(e);
			}
		});
	}

	/**
	 * Initializes the JComboBox objets of the window.
	 */
	private void JComboInit() {
		// Creates the JComboBox objects
		jComboCourse = new JComboBox();
		jComboConcept = new JComboBox();
		jComboNumAnswers = new JComboBox();

		// Deletes all their previous content, if there was any
		jComboCourse.removeAllItems();
		jComboConcept.removeAllItems();
		jComboNumAnswers.removeAllItems();

		// Sets their sizes
		jComboCourse.setBounds(new Rectangle(86, 15, 295, 25));
		jComboConcept.setBounds(new Rectangle(166, 45, 215, 25));
		jComboNumAnswers.setBounds(new Rectangle(310, 105, 71, 25));

		// Adds the course list of the user to jComboCourse
		for (int i = 0; i < courseVector.size(); i++) {
			jComboCourse.addItem(((Course) courseVector.get(i)).getName());
		}

		Vector conceptList = ((Course) courseVector.get(0)).getConceptNames();

		// Adds the concept list of the first course to jComboConcept
		if (conceptList != null && conceptList.isEmpty() == false) {
			courseName = jComboCourse.getItemAt(0).toString().trim();

			for (int i = 0; i < conceptList.size(); i++) {
				String name = conceptList.get(i).toString().trim();
				jComboConcept.addItem(name.substring(name.indexOf(courseName) + courseName.length() + 1));
			}

			String name = conceptList.get(0).toString().trim();
			jTextFieldQuestionsFile.setText(name.substring(name.indexOf(courseName) + courseName.length() + 1)
					+ "_" + ((Course) courseVector.get(0)).getName().trim() + "_items");

			OkButton.setEnabled(true);
		} else {
			jComboConcept.removeAllItems();
			jTextFieldQuestionsFile.setText("");
			OkButton.setEnabled(false);
		}

		// Adds to jComboNumAnswers the possible values for the default number of answers
		// for the question file to create
		String numAnswers;
		for (int i = TestEditor.NUMBER_OF_ANSWERS_MIN; i <= TestEditor.NUMBER_OF_ANSWERS_MAX; i++) {
			numAnswers = String.valueOf(i);
			jComboNumAnswers.addItem(numAnswers);
		}

		// Sets the behaviour of jComboCourse
		jComboCourse.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboCourseActionPerformed();
			}
		});

		// Sets the behaviour of jComboConcept
		jComboConcept.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboConceptActionPerformed();
			}
		});
	}

	/**
	 * Initializes the JTextField objets of the window.
	 */
	private void JTextFieldInit() {
		jTextFieldQuestionsFile = new JTextField();
		jTextFieldQuestionsFile.setBounds(new Rectangle(166, 75, 215, 25));
		jTextFieldQuestionsFile.setFocusable(false);
	}

	/**
	 * Sets the objects in the window
	 */
	private void JDialogInit() throws Exception {
		jPanelTitle.add(jLabelTitle, null);
		jPanelTitle.add(OkButton, null);
		jPanelTitle.add(CancelButton, null);
		jPanelTitle.add(jPanelMain, null);
		jPanelMain.add(jLabelCourse, null);
		jPanelMain.add(jComboCourse, null);
		jPanelMain.add(jLabelConcept, null);
		jPanelMain.add(jComboConcept, null);
		jPanelMain.add(jLabelQuestionsFile, null);
		jPanelMain.add(jTextFieldQuestionsFile, null);
		jPanelMain.add(jLabelNumAnswers, null);
		jPanelMain.add(jComboNumAnswers, null);
	}

	/**
	 * Disposes the JDialog, setting the values of the graphical objects
	 * @param e Event that caused the call to this method
	 */
	private void OkButtonActionPerformed(final ActionEvent e) {
		id = true;
		this.setVisible(false);

		// Substitute the blank spaces for '-' characters
		String questionsFileName = jTextFieldQuestionsFile.getText().trim();
		questionsFileName = questionsFileName.replaceAll(" ", "-");
		jTextFieldQuestionsFile.setText(questionsFileName);

		this.dispose();
	}

	/**
	 * Disposes the JDialog, deleting the values of the graphical objects
	 * @param e Event that caused the call to this method
	 */
	private void CancelButtonActionPerformed(ActionEvent e) {
		id = false;
		this.setVisible(false);
		this.dispose();
	}


	/**
	 * Changes the content of the jComboConcept object when a course is selected.
	 * Sets the list of concepts associated to the course in the jComboConcept object 
	 */
	private void jComboCourseActionPerformed() {
		// Gets the name of the selected course
		String nameCourse = (String) jComboCourse.getSelectedItem();

		// Calculates the position that takes the vector of concepts
		// of the chosen course in the courseVector.
		Vector conceptVector = new Vector();
		int j = 0;
		for (j = 0; j < courseVector.size(); j++) {
			if (nameCourse.equals(((Course) courseVector.get(j)).getName())) {
				conceptVector = ((Course) courseVector.get(j)).getConceptNames();
				break;
			}
		}

		// If the concept list is not empty, then that list is shown.
		// Otherwise, the jComboConcept will be empty and the OkButton will
		// be disabled
		if (conceptVector != null && conceptVector.isEmpty() == false) {
			// Deletes the previous content from jComboQuestionsFile.
			jComboConcept.removeAllItems();

			// Adds the name of the concepts to jComboConcept
			courseName = (String) jComboCourse.getSelectedItem();
			for (int i = 0; i < conceptVector.size(); i++) {
				if (conceptVector.get(i).toString().indexOf(courseName) == 0)
					jComboConcept.addItem(conceptVector.get(i).toString().trim().substring(
							conceptVector.get(i).toString().trim().indexOf(courseName) + courseName.length() + 1));
				else
					jComboConcept.addItem(conceptVector.get(i).toString().trim());
			}

			jTextFieldQuestionsFile.setText(jComboConcept.getSelectedItem().toString().trim() + "_"
					+ jComboCourse.getSelectedItem().toString().trim() + "_items");

			// Enables the OkButton.
			OkButton.setEnabled(true);
		} else {
			jComboConcept.removeAllItems();
			jTextFieldQuestionsFile.setText("");
			OkButton.setEnabled(false);
		}
	}


	/**
	 * Changes the content of the jTextFieldQuestionsFile object when a concept is selected.
	 * Sets the name of the question file showed in the jTextFieldQuestionsFile object 
	 */
	private void jComboConceptActionPerformed() {
		String nameConcept = "";
		if (jComboConcept.getItemCount() > 0)
			nameConcept = jComboConcept.getSelectedItem().toString().trim();
		else
			nameConcept = "";

		if (nameConcept != null && nameConcept.equals("") == false) {
			jTextFieldQuestionsFile.setText(jComboConcept.getSelectedItem().toString().trim() + "_"
					+ jComboCourse.getSelectedItem().toString().trim() + "_items");
			// Enables the OkButton.
			OkButton.setEnabled(true);
		} else {
			// Deletes the content of jTextFieldQuestionsFile
			jTextFieldQuestionsFile.setText("");

			// Disables the OkButton
			OkButton.setEnabled(false);
		}
	}

} // End of DlgCreateQuestionsFile class