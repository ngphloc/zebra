package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.DefaultCategoryDataset;
import org.jfree.data.DefaultPieDataset;

import es.uco.WOW.StudentFile.StudentFile;
import es.uco.WOW.TestEditor.TestEditor;
import es.uco.WOW.TestFile.TestFile;
import es.uco.WOW.Utils.EvalTestLogStudent;
import es.uco.WOW.Utils.Student;
import es.uco.WOW.Utils.StudentTest;
import es.uco.WOW.Utils.Test;
import es.uco.WOW.Utils.TestLogStudent;

/**
 * <p> Title: Wow! TestEditor </p> 
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p> Copyright: Copyright (c) 2008 </p>
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: GetChart
 * FUNCTION: Receives the name of a course, the name of a test file,
 * the lofin of a Student and a image type and returns the image data
 * in byte form
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public final class GetChart extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		/**
		 * Name of the course
		 */
		String courseName;
		
		/**
		 * Name of the test file
		 */
		String testFileName;
		
		/**
		 * Student's login
		 */
		String login;
		
		/**
		 * Type of test. Possible values are "classic" and "adaptive"
		 */
		String executionType; 
		
		/**
		 * Desired width of the image
		 */
		int width = 400;
		
		/**
		 * Desired height of the image
		 */
		int height = 300;
		
		/**
		 * Checks wether the test must be finished or not 
		 */
		boolean ended = false;
		
		/**
		 * Type of required image
		 */
		String chartClient;
		
		ObjectOutputStream objectOutputStream = null;
		JFreeChart pieChart = null;
		JFreeChart scoreBarChart = null;
		JFreeChart proficiencyBarChart = null;
		byte [] pieChartBytes = null;
		byte [] scoreChartBytes = null;
		byte [] proficiencyChartBytes = null;

		// Get the parameters
		courseName = req.getParameter("courseName").trim();
		testFileName = req.getParameter("testFileName").trim();
		login = req.getParameter("login").trim();
		executionType = req.getParameter("executionType").trim();
		width = Integer.valueOf(req.getParameter("width").trim()).intValue();
		height = Integer.valueOf(req.getParameter("height").trim()).intValue();
		ended = Boolean.valueOf(req.getParameter("ended")).booleanValue();
		chartClient = req.getParameter("chartClient").trim();

		if (!courseName.trim().equals("") && !testFileName.trim().equals("")
				&& !executionType.trim().equals("") && width > 0 && height > 0
				&& !chartClient.trim().equals(""))
		{
			if (chartClient.trim().equals("AppletTestEngine"))
				pieChart = appletTestEngine(courseName, login, testFileName,
						executionType, ended);
			else
			{
				if (chartClient.trim().equals("score"))
					scoreBarChart = statisticTest("score", courseName, testFileName);
				else if (chartClient.trim().equals("proficiency"))
					proficiencyBarChart = statisticTest("proficiency", courseName, testFileName);
			}
		}
		
		// Returns the Chart object or null in error case
		try
		{
			if (chartClient.trim().equals("AppletTestEngine"))
			{
				pieChartBytes = ChartUtilities.encodeAsPNG(pieChart.createBufferedImage(width, height));
				objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
				objectOutputStream.writeObject(pieChartBytes);
			}
			else
			{
				if (chartClient.trim().equals("score"))
				{
					scoreChartBytes = ChartUtilities.encodeAsPNG(scoreBarChart
							.createBufferedImage(width, height));
					objectOutputStream = new ObjectOutputStream(resp
							.getOutputStream());
					objectOutputStream.writeObject(scoreChartBytes);
				}
				else if (chartClient.trim().equals("proficiency"))
				{
					proficiencyChartBytes = ChartUtilities
							.encodeAsPNG(proficiencyBarChart.createBufferedImage(
									width, height));
					objectOutputStream = new ObjectOutputStream(resp
							.getOutputStream());
					objectOutputStream.writeObject(proficiencyChartBytes);
				}
			}
		}
		catch (IOException e)
		{
			objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			objectOutputStream.writeObject(null);
		} finally {
			objectOutputStream.close();
		}
	}
	
	
	/**
	 * Generates the ouput image
	 * @param chartType Indicates if the output image represent the score or the accuracy
	 *   of the Student. Take two possible values ("score" or "proficiency").
	 * @param courseName Name of the course
	 * @param testFileName Name of the Test file.
	 * @return A JFreeChart object as output image
	 */
	private JFreeChart statisticTest(
			final String chartType,
			final String courseName,
			final String testFileName) {
	//{
		JFreeChart barChart = null;

		// Checks if the test file exists and gets the data
		TestFile testFile = new TestFile(courseName, testFileName);
		if (testFile.exists()) {
			Test test = testFile.getTest();
			if (test != null) {
				if (chartType.trim().equals("score")) {
					barChart = generateScoreBarChart(test);
				} else if (chartType.trim().equals("proficiency")) {
					barChart = generateProficiencyBarChart(test);
				}
			}
		}
		return barChart;
	}

	
	/**
	 * Generates the output image
	 * @param courseName Name of the course
	 * @param login Student's login
	 * @param testFileName Name of the test file
	 * @param executionType Type of test ("classic" or "adaptive")
	 * @param ended Indicates if the test must be finished or not
	 * @return A JFreeChart object representing the output image
	 */
	private JFreeChart appletTestEngine(
			final String courseName,
			final String login,
			final String testFileName,
			final String executionType,
			final boolean ended) {
	//{
		JFreeChart pieChart = null;
		Student student = null;
		TestLogStudent testLogStudent = null;
		EvalTestLogStudent evalTestLogStudent = null;

		// Checks if the test file exists
		StudentFile studentFile = new StudentFile(courseName, login);
		if (studentFile.exists()) {
			// Get the student data
			// Checks if the test must be finished or not
			if (ended == false) {
				student = studentFile.getStudentNotFinishedTest(testFileName, executionType);
			} else {
				student = studentFile.getStudentFinishedTest(testFileName);
			}

			if (student != null) {
				testLogStudent = (TestLogStudent) student.getTest().firstElement();
				evalTestLogStudent = (EvalTestLogStudent) testLogStudent
						.getEvaluatedTest().firstElement();
				pieChart = generatePieChart(evalTestLogStudent);
			}
		}

		return pieChart;
	}

	
	/**
	 * Generates a PieChart type image with the questions well done,
	 * the incorrect questions and the questions without answer
	 * @param evalTestLogStudent Log file of the Student
	 * @return A JFreeChart object that represents the output image
	 */
	private JFreeChart generatePieChart(final EvalTestLogStudent evalTestLogStudent) 
	{
		JFreeChart chart = null;

		DefaultPieDataset defaultPieDataset = new DefaultPieDataset();

		defaultPieDataset
				.setValue("Incorrect", evalTestLogStudent.getIncorrect());
		defaultPieDataset.setValue("Questions not done", evalTestLogStudent
				.getTotalQuestion()
				- evalTestLogStudent.getQuestionsDone());
		defaultPieDataset.setValue("Correct", evalTestLogStudent.getCorrect());
		defaultPieDataset.setValue("Without Answer", evalTestLogStudent
				.getWithoutAnswer());

		chart = ChartFactory.createPieChart3D("Final statistic of the test",
				defaultPieDataset, true, // legend?
				true, // tooltips?
				false // URLs?
				);

		return chart;
	}

	
	/**
	 * Generates a ScoreBarChart object that represents the score of the students
	 * @param test 
	 * @return a JFreeChart object representing the image object
	 */
	private JFreeChart generateScoreBarChart(final Test test) 
	{
		JFreeChart scoreBarChart = null;

		// Allocates memory for the output graphic
		String labelScoreSerie1 = "[0 , 25)";
		String labelScoreSerie2 = "[25 , 50)";
		String labelScoreSerie3 = "[50 , 75)";
		String labelScoreSerie4 = "[75 , 100]";

		int serie1Score = 0;
		int serie2Score = 0;
		int serie3Score = 0;
		int serie4Score = 0;

		if (test.getStudentVector() != null && !test.getStudentVector().isEmpty())
		{
			// Calculates the number of the students comprended in each
			// score interval
			for (int i = 0; i < test.getStudentVector().size(); i++)
			{
				StudentTest studentTest = (StudentTest) test.getStudentVector()
						.get(i);
				double score = studentTest.getLastScore();

				if (score >= 0 && score < 25)
					serie1Score++;
				else if (score >= 25 && score < 50)
					serie2Score++;
				else if (score >= 50 && score < 75)
					serie3Score++;
				else if (score >= 75 && score <= 100) serie4Score++;
			}
		}

		DefaultCategoryDataset scoreCategoryDataset = new DefaultCategoryDataset();
		scoreCategoryDataset.addValue(serie1Score, labelScoreSerie1, "");
		scoreCategoryDataset.addValue(serie2Score, labelScoreSerie2, "");
		scoreCategoryDataset.addValue(serie3Score, labelScoreSerie3, "");
		scoreCategoryDataset.addValue(serie4Score, labelScoreSerie4, "");

		scoreBarChart = ChartFactory.createBarChart3D("Score", "Score",
				"Students", scoreCategoryDataset, PlotOrientation.VERTICAL, true, // legend?
				true, // tooltips?
				false // URLs?
				);

		return scoreBarChart;
	}

	
	/**
	 * Generates a ProficiencyBarChart object that represents the 
	 * accuracy of the students
	 * @param test Test object
	 * @return a JFreeChart object that represents the output image
	 */
	private JFreeChart generateProficiencyBarChart(final Test test) 
	{
		JFreeChart proficiencyBarChart = null;
		
		// Allocates memory for the output image
		if (test.getExecutionType().trim().equals(TestEditor.ADAPTIVE))
		{
			String labelProficiencySerie1 = "[-4 , -3)";
			String labelProficiencySerie2 = "[-3 , -2)";
			String labelProficiencySerie3 = "[-2 , -1)";
			String labelProficiencySerie4 = "[-1 , 0)";
			String labelProficiencySerie5 = "[0 , 1)";
			String labelProficiencySerie6 = "[1 , 2)";
			String labelProficiencySerie7 = "[2 , 3)";
			String labelProficiencySerie8 = "[3 , 4]";

			int serie1Proficiency = 0;
			int serie2Proficiency = 0;
			int serie3Proficiency = 0;
			int serie4Proficiency = 0;
			int serie5Proficiency = 0;
			int serie6Proficiency = 0;
			int serie7Proficiency = 0;
			int serie8Proficiency = 0;

			if (test.getStudentVector() != null
					&& !test.getStudentVector().isEmpty())
			{
				// Calculates the number of students comprended in each
				// score interval
				for (int i = 0; i < test.getStudentVector().size(); i++)
				{
					StudentTest studentTest = (StudentTest) test.getStudentVector().get(i);
					double theta = studentTest.getTheta();

					if (theta >= -4 && theta < -3)
						serie1Proficiency++;
					else if (theta >= -3 && theta < -2)
						serie2Proficiency++;
					else if (theta >= -2 && theta < -1)
						serie3Proficiency++;
					else if (theta >= -1 && theta < 0)
						serie4Proficiency++;
					else if (theta >= 0 && theta < 1)
						serie5Proficiency++;
					else if (theta >= 1 && theta < 2)
						serie6Proficiency++;
					else if (theta >= 2 && theta < 3)
						serie7Proficiency++;
					else if (theta >= 3 && theta <= 4) serie8Proficiency++;
				}
			}

			DefaultCategoryDataset proficiencyCategoryDataset = new DefaultCategoryDataset();

			proficiencyCategoryDataset.addValue(serie1Proficiency,
					labelProficiencySerie1, "");
			proficiencyCategoryDataset.addValue(serie2Proficiency,
					labelProficiencySerie2, "");
			proficiencyCategoryDataset.addValue(serie3Proficiency,
					labelProficiencySerie3, "");
			proficiencyCategoryDataset.addValue(serie4Proficiency,
					labelProficiencySerie4, "");
			proficiencyCategoryDataset.addValue(serie5Proficiency,
					labelProficiencySerie5, "");
			proficiencyCategoryDataset.addValue(serie6Proficiency,
					labelProficiencySerie6, "");
			proficiencyCategoryDataset.addValue(serie7Proficiency,
					labelProficiencySerie7, "");
			proficiencyCategoryDataset.addValue(serie8Proficiency,
					labelProficiencySerie8, "");

			proficiencyBarChart = ChartFactory.createBarChart3D("Proficiency",
					"Proficiency", "Students", proficiencyCategoryDataset,
					PlotOrientation.VERTICAL, true, // legend?
					true, // tooltips?
					false // URLs?
					);
		}

		return proficiencyBarChart;
	}
}