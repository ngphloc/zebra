package es.uco.WOW.Utils;

import java.io.Serializable;
import java.util.Vector;

/**
 * <p>Title: Wow! TestEditor</p>
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: AnswersTestLogStudent
 * FUNCTION: This class stores all the main data about the answers of 
 *   a question that belongs to a test.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez
 */
public class AnswersTestLogStudent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the course
	 */
	private String course = ""; 
	
	/**
	 * Name of the question file
	 */
	private String questionsFileName = ""; 
	
	/**
	 * Code of the question.
	 */
	private String codeQuestion = ""; 

	/**
	 * This is Vector object that contains in each position
	 * a String object with the codes of the answers of the student.
	 */
	private Vector codeAnswers = null;

	
	/**
	 * Returns the code list of the answers
	 * of the student
	 * @return Vector
	 */
	public Vector getCodeAnswers() {
		return codeAnswers;
	}

	/**
	 * Returns the question code
	 * @return String
	 */
	public String getCodeQuestion() {
		return codeQuestion;
	}

	/**
	 * Returns the name of the course
	 * @return String
	 */
	public String getCourse() {
		return course;
	}

	/**
	 * Returns the name of the question file.
	 * @return String
	 */
	public String getQuestionsFileName() {
		return questionsFileName;
	}

	/**
	 * Sets the Vector with the codes of the answers of the student.
	 * @param vector Vector
	 */
	public void setCodeAnswers(Vector vector) {
		codeAnswers = vector;
	}

	/**
	 * Sets the question code.
	 * @param s String
	 */
	public void setCodeQuestion(String s) {
		codeQuestion = s;
	}

	/**
	 * Sets the name of the course.
	 * @param s String
	 */
	public void setCourse(String s) {
		course = s;
	}

	/**
	 * Sets the name of the question file.
	 * @param s String
	 */
	public void setQuestionsFileName(String s) {
		questionsFileName = s;
	}

} // End of AnswersTestLogStudent class