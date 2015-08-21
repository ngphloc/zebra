package es.uco.WOW.AppletTestEngine;

import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import es.uco.WOW.TestEditor.TestEditor;
import es.uco.WOW.Utils.EvalTestLogStudent;

/**
 * <p>Title: Wow! TestEditor</p> 
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: DlgScore 
 * FUNCTION: Shows to the student the provisional score obtain in the test and 
 *   some information about the number of correct, incorrect, not answered and 
 *   done questions at the current moment. 
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Loc Nguyen
 */
public class DlgScore extends JDialog
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Components of the window 
	 */
	private JPanel jPanelTitle;

	private JPanel jPanelMain;

	private JPanel jPanelScore;

	private JLabel jLabelTitle;

	private JLabel jLabelChart;

	private JLabel jLabelTotalQuestions;

	private JLabel jLabelCorrect;

	private JLabel jLabelIncorrect;

	private JLabel jLabelWithoutAnswer;

	private JLabel jLabelQuestionsDone;

	private JLabel jLabelScore;

	private JLabel jLabelProficiencyPrevious;

	private JLabel jLabelProficiency;

	private JLabel jLabelStandardError;

	private JButton OkButton;

	private JTextField jTextFieldTotalQuestions;

	private JTextField jTextFieldCorrect;

	private JTextField jTextFieldIncorrect;

	private JTextField jTextFieldWithoutAnswer;

	private JTextField jTextFieldQuestionsDone;

	private JTextField jTextFieldScore;

	private JTextField jTextFieldProficiencyPrevious;

	private JTextField jTextFieldProficiency;

	private JTextField jTextFieldStandardError;

	private JDialog jDialog;

	private EvalTestLogStudent evalTestLogStudent;

	private String title;

	private String executionType = "";

	private byte[] chart = null;


	/**
	 * Constructor. Configurates the appearance of the JDialog object,
	 * their functionality and initializes the components that 
	 * belongs to the JDialog object.
	 * 
	 * @param title Title of the JDialog window.
	 * @param courseName Name of the course.
	 * @param login Login of the student.
	 * @param testFileName Name of the test file.
	 * @param executionType Type of test ("classic" or "adaptive").
	 * @param evalTestLogStudent Information about the test that is being done.
	 * @param parent A reference to the object that invokes this class
	 */ 
	public DlgScore(String title, String courseName, String login,
			String testFileName, String executionType,
			EvalTestLogStudent evalTestLogStudent, JApplet parent) 
	{
		try
		{
			UIManager.setLookAndFeel(((AppletTestEngine) parent).lookAndFeelClassName);
		}
		catch (Exception e)
		{}

		this.evalTestLogStudent = evalTestLogStudent;
		this.title = title;
		this.executionType = executionType;
		this.jDialog = this;

		// Calls the method of the parent class that generates the chart
		chart = ((AppletTestEngine) parent).getChart(courseName, login,
				testFileName, executionType, 390, 180, false, "AppletTestEngine");

		// Initializes the components of the class
		try
		{
			// DON'T MODIFY THE ORDER
			JPanelInit();
			JLabelInit();
			JButtonInit();
			JTextFieldInit();

			JDialogInit();

			this.setSize(432, 480);
			this.setResizable(false);
			this.setLocation(200, 150);
			this.getContentPane().add(jPanelTitle);
			this.setVisible(true);
			this.setModal(true);
			this.setTitle(title);
			this.setBackground(SystemColor.window);
		}
		catch (java.lang.Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage(), "Info", 1);
		}
		catch (java.lang.NoClassDefFoundError ncfe)
		{
			JOptionPane.showMessageDialog(this, ncfe.getMessage(), "Info", 1);
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// INITIALIZES THE COMPONENTS /////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	
	/**
	 * Initializes the JPanel objects of the JDialog object,
	 * setting their size and their layout type.	 
	 */
	private void JPanelInit()
	{
		jPanelTitle = new JPanel();
		jPanelMain = new JPanel();
		jPanelScore = new JPanel();

		jPanelTitle.setLayout(null);

		jPanelMain.setBorder(BorderFactory.createLoweredBevelBorder());
		jPanelMain.setBounds(new Rectangle(10, 50, 407, 345));
		jPanelMain.setLayout(null);

		jPanelScore.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		jPanelScore.setBackground(SystemColor.WHITE);
		jPanelScore.setBounds(225, 195, 175, 145);
		jPanelScore.setLayout(null);
	}

	
	/**
	 * Initializes the JLabel objects of the JDialog objects,
	 * setting their size and their texts.
	 */
	private void JLabelInit()
	{
		jLabelTitle = new JLabel();
		jLabelTotalQuestions = new JLabel();
		jLabelCorrect = new JLabel();
		jLabelIncorrect = new JLabel();
		jLabelWithoutAnswer = new JLabel();
		jLabelQuestionsDone = new JLabel();
		jLabelScore = new JLabel();
		jLabelProficiencyPrevious = new JLabel();
		jLabelProficiency = new JLabel();
		jLabelStandardError = new JLabel();
		jLabelChart = new JLabel();

		jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 16));
		jLabelTitle.setBorder(BorderFactory.createLoweredBevelBorder());
		jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		jLabelTitle.setText(title);
		jLabelTitle.setBounds(new Rectangle(10, 5, 407, 44));

		jLabelTotalQuestions.setBounds(new Rectangle(10, 195, 200, 20));
		jLabelTotalQuestions.setText("Total number of questions ");
		jLabelTotalQuestions.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelCorrect.setBounds(new Rectangle(10, 225, 170, 20));
		jLabelCorrect.setText("Correct ");
		jLabelCorrect.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelIncorrect.setBounds(new Rectangle(10, 255, 170, 20));
		jLabelIncorrect.setText("Incorrect ");
		jLabelIncorrect.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelWithoutAnswer.setBounds(new Rectangle(10, 285, 170, 20));
		jLabelWithoutAnswer.setText("Without answer ");
		jLabelWithoutAnswer.setFont(new java.awt.Font("Dialog", 1, 12));

		jLabelQuestionsDone.setBounds(new Rectangle(10, 315, 170, 20));
		jLabelQuestionsDone.setText("Questions done ");
		jLabelQuestionsDone.setFont(new java.awt.Font("Dialog", 1, 12));

		if (executionType.trim().equals(TestEditor.ADAPTIVE))
		{
			jLabelScore.setText("Score");
			jLabelScore.setFont(new java.awt.Font("Dialog", 1, 12));
			jLabelScore.setBounds(new Rectangle(5, 10, 150, 25));

			jLabelProficiencyPrevious.setText("Previous proficiency");
			jLabelProficiencyPrevious.setFont(new java.awt.Font("Dialog", 1, 12));
			jLabelProficiencyPrevious.setBounds(new Rectangle(5, 40, 150, 25));

			jLabelProficiency.setText("Proficiency");
			jLabelProficiency.setFont(new java.awt.Font("Dialog", 1, 12));
			jLabelProficiency.setBounds(new Rectangle(5, 70, 150, 25));

			jLabelStandardError.setText("Standard Error");
			jLabelStandardError.setFont(new java.awt.Font("Dialog", 1, 12));
			jLabelStandardError.setBounds(new Rectangle(5, 100, 150, 25));
		}
		else
		{
			jLabelScore.setBounds(new Rectangle(35, 10, 150, 50));
			jLabelScore.setText("Score");
			jLabelScore.setFont(new java.awt.Font("Dialog", 1, 28));
		}

		jLabelChart.setBounds(10, 10, 390, 180);

		if (chart != null)
		{
			jLabelChart.setFocusable(true);
			jLabelChart.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));
			ImageIcon chartImage = new ImageIcon(chart);
			jLabelChart.setIcon(chartImage);
		}
	}

	
	/**
	 * Initializes the JButton objects of the JDialog object, 
	 * setting their size and their functionality.
	 */
	private void JButtonInit()
	{
		OkButton = new JButton();

		OkButton.setBounds(new Rectangle(310, 400, 100, 30));
		OkButton.setText("Ok");
		OkButton.setFont(new java.awt.Font("Button", 1, 12));

		OkButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jDialog.setVisible(false);
				jDialog.dispose();
			}
		});
	}

	
	/**
	 * Initializes the JTextField objects of the JDialog object,
	 * setting their size and their functionality.
	 */
	private void JTextFieldInit()
	{
		jTextFieldTotalQuestions = new JTextField(String
				.valueOf(evalTestLogStudent.getTotalQuestion()), 4);
		jTextFieldTotalQuestions.setBounds(new Rectangle(180, 195, 40, 25));
		jTextFieldTotalQuestions.setFocusable(false);
		jTextFieldTotalQuestions.setFont(new java.awt.Font("Dialog", 1, 14));

		jTextFieldCorrect = new JTextField(String.valueOf(evalTestLogStudent
				.getCorrect()), 4);
		jTextFieldCorrect.setBounds(new Rectangle(180, 225, 40, 25));
		jTextFieldCorrect.setFocusable(false);
		jTextFieldCorrect.setFont(new java.awt.Font("Dialog", 1, 14));
		jTextFieldCorrect.setBackground(SystemColor.GREEN);

		jTextFieldIncorrect = new JTextField(String.valueOf(evalTestLogStudent
				.getIncorrect()), 4);
		jTextFieldIncorrect.setBounds(new Rectangle(180, 255, 40, 25));
		jTextFieldIncorrect.setFocusable(false);
		jTextFieldIncorrect.setFont(new java.awt.Font("Dialog", 1, 14));
		jTextFieldIncorrect.setBackground(SystemColor.RED);

		jTextFieldWithoutAnswer = new JTextField(String
				.valueOf(evalTestLogStudent.getWithoutAnswer()), 4);
		jTextFieldWithoutAnswer.setBounds(new Rectangle(180, 285, 40, 25));
		jTextFieldWithoutAnswer.setFocusable(false);
		jTextFieldWithoutAnswer.setFont(new java.awt.Font("Dialog", 1, 14));
		jTextFieldWithoutAnswer.setBackground(SystemColor.YELLOW);

		jTextFieldQuestionsDone = new JTextField(String
				.valueOf(evalTestLogStudent.getQuestionsDone()), 4);
		jTextFieldQuestionsDone.setBounds(new Rectangle(180, 315, 40, 25));
		jTextFieldQuestionsDone.setFocusable(false);
		jTextFieldQuestionsDone.setFont(new java.awt.Font("Dialog", 1, 14));
		jTextFieldQuestionsDone.setBackground(SystemColor.BLUE);

		if (executionType.trim().equals(TestEditor.ADAPTIVE))
		{
			String score = String.valueOf(evalTestLogStudent.getScore());
			if (score.length() > 5) score = score.substring(0, 5);

			jTextFieldScore = new JTextField(score, 5);
			jTextFieldScore.setBounds(new Rectangle(130, 10, 40, 25));
			jTextFieldScore.setFocusable(false);
			jTextFieldScore.setFont(new java.awt.Font("Dialog", 1, 14));
			jTextFieldScore.setBackground(SystemColor.LIGHT_GRAY);
			jTextFieldScore.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));

			String thetaPrevious = String.valueOf(evalTestLogStudent
					.getThetaPrevious());
			if (thetaPrevious.length() > 5)
				thetaPrevious = thetaPrevious.substring(0, 5);

			jTextFieldProficiencyPrevious = new JTextField(thetaPrevious, 5);
			jTextFieldProficiencyPrevious
					.setBounds(new Rectangle(130, 40, 40, 25));
			jTextFieldProficiencyPrevious.setFocusable(false);
			jTextFieldProficiencyPrevious.setFont(new java.awt.Font("Dialog", 1,
					14));
			jTextFieldProficiencyPrevious.setBackground(SystemColor.LIGHT_GRAY);
			jTextFieldProficiencyPrevious.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));

			String theta = String.valueOf(evalTestLogStudent.getTheta());
			if (theta.length() > 5) theta = theta.substring(0, 5);

			jTextFieldProficiency = new JTextField(theta, 5);
			jTextFieldProficiency.setBounds(new Rectangle(130, 70, 40, 25));
			jTextFieldProficiency.setFocusable(false);
			jTextFieldProficiency.setFont(new java.awt.Font("Dialog", 1, 14));
			jTextFieldProficiency.setBackground(SystemColor.LIGHT_GRAY);
			jTextFieldProficiency.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));

			String standardError = String.valueOf(evalTestLogStudent
					.getStandardError());
			if (standardError.length() > 5)
				standardError = standardError.substring(0, 5);

			jTextFieldStandardError = new JTextField(standardError, 5);
			jTextFieldStandardError.setBounds(new Rectangle(130, 100, 40, 25));
			jTextFieldStandardError.setFocusable(false);
			jTextFieldStandardError.setFont(new java.awt.Font("Dialog", 1, 14));
			jTextFieldStandardError.setBackground(SystemColor.LIGHT_GRAY);
			jTextFieldStandardError.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));
		}
		else
		{
			String score = String.valueOf(evalTestLogStudent.getScore());
			if (score.length() > 5) score = score.substring(0, 5);

			jTextFieldScore = new JTextField(score, 5);
			jTextFieldScore.setBounds(new Rectangle(30, 70, 100, 50));
			jTextFieldScore.setFocusable(false);
			jTextFieldScore.setFont(new java.awt.Font("Dialog", 1, 28));
			jTextFieldScore.setBackground(SystemColor.LIGHT_GRAY);
			jTextFieldScore.setBorder(BorderFactory
					.createLineBorder(SystemColor.BLACK));
		}
	}

	
	/**
	 * Initializes the JDialog object. Sets the content
	 * of each JPanel object of the JDialog object.
	 * @throws Exception If any error occur.
	 */
	private void JDialogInit() throws Exception
	{
		jPanelTitle.add(jLabelTitle, null);
		jPanelTitle.add(OkButton, null);
		jPanelTitle.add(jPanelMain, null);
		jPanelMain.add(jLabelChart, null);
		jPanelMain.add(jLabelTotalQuestions, null);
		jPanelMain.add(jTextFieldTotalQuestions, null);
		jPanelMain.add(jLabelCorrect, null);
		jPanelMain.add(jTextFieldCorrect, null);
		jPanelMain.add(jLabelIncorrect, null);
		jPanelMain.add(jTextFieldIncorrect, null);
		jPanelMain.add(jLabelWithoutAnswer, null);
		jPanelMain.add(jTextFieldWithoutAnswer, null);
		jPanelMain.add(jLabelQuestionsDone, null);
		jPanelMain.add(jTextFieldQuestionsDone, null);
		jPanelMain.add(jPanelScore, null);
		if (executionType.trim().equals(TestEditor.ADAPTIVE))
		{
			jPanelScore.add(jLabelScore, null);
			jPanelScore.add(jTextFieldScore, null);
			jPanelScore.add(jLabelProficiencyPrevious, null);
			jPanelScore.add(jTextFieldProficiencyPrevious, null);
			jPanelScore.add(jLabelProficiency, null);
			jPanelScore.add(jTextFieldProficiency, null);
			jPanelScore.add(jLabelStandardError, null);
			jPanelScore.add(jTextFieldStandardError, null);
		}
		else
		{
			jPanelScore.add(jLabelScore, null);
			jPanelScore.add(jTextFieldScore, null);
		}
	}

} // End of DlgScore class