package es.uco.WOW.Utils;

import java.io.Serializable;
import java.util.Vector;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * @version 1.0
 */

/**
 * NAME: EvalTestLogStudent
 * FUNCTION: This class contains all the data to manage the information 
 *  relative to the evaluation of a test.
 * LAST MODIFICATION: 06-02-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class EvalTestLogStudent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Score obtained in the test.
	 */
	private double score = 0.0; 
	
	/**
	 * Time in seconds that has been taken to do the test.
	 */
	private int time = 0; 
	
	/**
	 * Date when the test has been done.
	 */
	private String date = ""; 
	
	/**
	 * Number of questions that take part of the test.
	 */
	private int totalQuestion = 0;
	
	/**
	 * Number of questions with correct answer in the test.
	 */
	private int correct = 0;
	
	/**
	 * Number of questions with incorrect answer in the test.
	 */
	private int incorrect = 0;
	
	/**
	 * Number of questions without answer in the test.
	 */
	private int withoutAnswer = 0;
	
	/**
	 * Number of questions that has been done at the moment.
	 */
	private int questionsDone = 0; 
	
	/**
	 * Knowledge of the student in the test.
	 */
	private double theta = 99999; 
	
	/**
	 * Previous knowledge of the student in the last answered
	 * question of the test. 
	 */
	private double thetaPrevious = 99999; 
	
	/**
	 * Error taken to calculate the knowledge of the student.
	 */
	private double standardError = 99999;
	
	/**
	 * This is a list where in each position stores a
	 * QuestionsFileTest object. Each one of that contains the relative path
	 * of a question file and a list with the code of the questions
	 * of that file that take part in the test.  
	 */
	private Vector testVector = null; 

	
	/**
	 * Sets the score obtained in the test.
	 * @param score double
	 */
	public void setScore(final double score) {
		this.score = score;
	}

	/**
	 * Sets the time in seconds that has been taken to do the test.
	 * @param time int
	 */
	public void setTime(final int time) {
		this.time = time;
	}

	/**
	 * Sets the date when the test has been done.
	 * @param date String
	 */
	public void setDate(final String date) {
		this.date = date;
	}

	/**
	 * Sets the number of questions that take part of the test.
	 * @param totalQuestion int
	 */
	public void setTotalQuestion(final int totalQuestion) {
		this.totalQuestion = totalQuestion;
	}

	/**
	 * Sets the number of questions with correct answer in the test.
	 * @param correct int
	 */
	public void setCorrect(final int correct) {
		this.correct = correct;
	}

	/**
	 * Sets the number of questions with incorrect answer in the test.
	 * @param incorrect int
	 */
	public void setIncorrect(final int incorrect) {
		this.incorrect = incorrect;
	}

	/**
	 * Sets the number of questions withouth answer in the test.
	 * @param withoutAnswer int
	 */
	public void setWithoutAnswer(final int withoutAnswer) {
		this.withoutAnswer = withoutAnswer;
	}

	/**
	 * Sets the number of questions that has been done at the moment.
	 * @param questionsDone int
	 */
	public void setQuestionsDone(final int questionsDone) {
		this.questionsDone = questionsDone;
	}

	/**
	 * Sets the knowledge of the student for this test.
	 * @param theta double
	 */
	public void setTheta(final double theta) {
		this.theta = theta;
	}

	/**
	 * Sets the previous knowledge of the student in the last answered
	 * question of the test. 
	 * @param thetaPrevious double
	 */
	public void setThetaPrevious(final double thetaPrevious) {
		this.thetaPrevious = thetaPrevious;
	}

	/**
	 * Sets the standar error taken to calculate the knowledge of the student.
	 * @param standardError double
	 */
	public void setStandardError(final double standardError) {
		this.standardError = standardError;
	}

	/**
	 * Sets a list where in each position stores a
	 * QuestionsFileTest object
	 * @param testVector Vector
	 */
	public void setTestVector(final Vector testVector) {
		this.testVector = testVector;
	}

	/**
	 * Returns the score obtained in the test.
	 * @return double
	 */
	public double getScore() {
		return score;
	}

	/**
	 * Returns the time in seconds that has been taken to do the test.
	 * @return int
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Returns the date where the test has been done.
	 * @return String
	 */
	public String getDate() {
		return date.trim();
	}

	/**
	 * Returns the total number of questions that has 
	 * taken part in the test.
	 * @return int
	 */
	public int getTotalQuestion() {
		return totalQuestion;
	}

	/**
	 * Returns the number of questions with correct answer in the test.
	 * @return int
	 */
	public int getCorrect() {
		return correct;
	}

	/**
	 * Returns the number of questions with incorrect answer in the test.
	 * @return int
	 */
	public int getIncorrect() {
		return incorrect;
	}

	/**
	 * Returns the number of questions withouth answer in the test.
	 * @return int
	 */
	public int getWithoutAnswer() {
		return withoutAnswer;
	}

	/**
	 * Returns the number of questions that has been done at the moment.
	 * @return int
	 */
	public int getQuestionsDone() {
		return questionsDone;
	}

	/**
	 * Returns the knowledge of the student in the test.
	 * @return double
	 */
	public double getTheta() {
		return theta;
	}

	/**
	 * Returns the previous knowledge of the student in the last answered
	 * question of the test. 
	 * @return double
	 */
	public double getThetaPrevious() {
		return thetaPrevious;
	}

	/**
	 * Returns the standar error taken to calculate the knowledge of
	 * the student in the test.
	 * @return double
	 */
	public double getStandardError() {
		return standardError;
	}

	/**
	 * Returns a list where in each position stores a
	 * QuestionsFileTest object.
	 * @return Vector
	 */
	public Vector getTestVector() {
		return testVector;
	}

} // End of EvalTestLogStudent class