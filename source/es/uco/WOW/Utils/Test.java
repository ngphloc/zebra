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
 * NAME: Test
 * FUNCTION: This is a class that encapsulates all the data about a 
 *   test file.
 * LAST MODIFICATION: 06-02-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class Test implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the course that the test begins to
	 */
	private String course = ""; 
	
	/**
	 * Type of test. Possible values are
	 * "activity" and "exam"
	 */
	private String testType = "";
	
	/**
	 * Type of execution of the test. Possible values 
	 * are "classic" or "adaptive"
	 */
	private String executionType = "";
	
	/**
	 * Name of the abstract concept that evaluates the test
	 */ 
	private String abstractConcept = ""; 
	
	/**
	 * A list with the name of the concepts that
	 * the test is associated with
	 */
	private Vector conceptVector = null; 
	
	/**
	 * Name of the file who stores the test
	 */
	private String testFileName = ""; 
	
	/**
	 * Name of the test
	 */
	private String testName = ""; 
	
	/**
	 * Number of times the test has been used
	 */
	private int numberOfUses = 0; 
	
	/**
	 * Indicates when was the last time this test was used.
	 * Is a String with the DD/MM/YYYY format
	 */
	private String lastUse = ""; 
	
	/**
	 * Number of questions that the test contains 
	 */
	private int totalQuestion = 0; 
	
	/**
	 * Path to the test file
	 */
	private String path = ""; 
	
	/**
	 * Indicates wether the test is enabled to be done or not
	 */
	private boolean enable = true; 
	
	/**
	 * A list where each position is a WOWQuestionFileTest with the
	 * relative path to a question file and a Vector object with 
	 * the questions codes of the questions that contains the test  
	 */
	private Vector testVector = null; 
	
	/**
	 * Indicates if the test presentation must be showed
	 */
	private boolean showInitialInfo = true; 
	
	/**
	 * The order in which the questions will be showed.
	 * Possible values are "SEQUENTIAL" and "RANDOM".
	 */
	private String questionsOrder = ""; 
	
	/**
	 * The order in which the answers of the questions will be showed. 
	 * Possible values are "SEQUENTIAL" and "RANDOM"
	 */
	private String answersOrder = ""; 
	
	/**
	 * Indicates if the explanation to the answers of the
	 * questions must be showed when the questions 
	 * will be evaluated
	 */
	private boolean verbose = false;
	
	/**
	 * Indicates if the correct answer must be showed
	 * when the evaluation of the question is showed
	 */
	private boolean showCorrectAnswers = false;
	
	/**
	 * Maximum response time in seconds for 
	 * any question of the test.
	 */
	private String timeOfAnswer = ""; 
	
	/**
	 * Indicates if the result of the evaluation of 
	 * the question must be showed or not
	 */
	private boolean showQuestionCorrection = true; 
	
	/**
	 * Indicates if the questions not answered
	 * by the student must be showed again or not 
	 */
	private boolean repeatWithoutAnswer = true; 
	
	/**
	 * Indicates if the final information about the test
	 * must be showed
	 */
	private boolean showFinalInfo = true; 
	
	/**
	 * Background color of the page that contains the test
	 */
	private String bgColor = ""; 
	
	/**
	 * Path of the background image of the web page that
	 * contains the test
	 */
	private String background = ""; 
	
	/**
	 * Type (File type) of background image
	 */
	private String backgroundType = ""; 
	
	/**
	 * Colour of the name of the test showed in the
	 * page that contains the test
	 */
	private String titleColor = "";
	
	/**
	 * Indicates if the incorrect answers penalizes
	 * in the final score of the test.
	 */
	private boolean incorrectAnswersPenalize = false; 
	
	/**
	 * Indicates the number of correct answers that a 
	 * incorrect answer penalizes.
	 */
	private int incorrectAnswersPenalizeNumber = -1; 
	
	/**
	 * Indicates if the not answered questions penalizes
	 * in the final score of the test.
	 */
	private boolean withoutAnswersPenalize = false;
	
	/**
	 * Indicates the number of correct answers that a
	 * not answered questions penalizes.
	 */
	private int withoutAnswersPenalizeNumber = -1; 
	
	/**
	 * Percentage of knowledge that this Test object represents.
	 * It has values between [0..100].
	 */
	private String knowledgePercentage = "";
	
	/**
	 * Estimated initial knowledge of the student, 
	 * used when an adaptive test is begun.
	 */ 
	private double irtInitialProficiency = 0.5;
	
	/**
	 * Indicates the IRT Model that will be used to choose
	 * the next question of an adaptive test.
	 * (1, 2 or 3 parameters)
	 */ 
	private int irtModel = 3; 
	
	/**
	 * Indicates the stop rule that an adaptive test will use.
	 * Possible values are "standardError" and "numberItemsAdministred". 
	 */
	private String irtStopCriterion = "standardError"; 
	
	/**
	 * Indicates the value of the standar error that must be
	 * made to end an adaptive test
	 */
	private double irtStandardError = 0.33;
	
	/**
	 * Number of items that must be supplied to end an adaptive test.
	 */
	private int irtNumberItemsAdministred = 1; 
	
	/**
	 * This is a Vector object where each position stores a 
	 * WOWStudent object that contains a student login,
	 * the number of times that this student has done this test
	 * and a value indicating wether can repeat it or not.
	 */
	private Vector studentVector = null; 
	
	
	/**
	 * Sets the name of the course that the test belongs to
	 * @param course String
	 */
	public void setCourse(final String course) {
		this.course = course;
	}

	/**
	 * Sets the type of test. Possible values are
	 * "activity" and "exam"
	 * @param testType String
	 */
	public void setTestType(final String testType) {
		this.testType = testType;
	}

	/**
	 * Sets the type of execution of the test. Possible 
	 * values are "classic" and "adaptive"
	 * @param executionType String 
	 */
	public void setExecutionType(final String executionType) {
		this.executionType = executionType;
	}

	/**
	 * Sets the name of the file who stores the test
	 * (withouth extension)
	 * @param testFileName String
	 */
	public void setTestFileName(final String testFileName) {
		this.testFileName = testFileName;
	}

	/**
	 * Sets the name of the test
	 * @param testName String
	 */
	public void setTestName(final String testName) {
		this.testName = testName;
	}

	/**
	 * Sets the number of times that the test has been done
	 * @param numberOfUses int
	 */
	public void setNumberOfUses(final int numberOfUses) {
		this.numberOfUses = numberOfUses;
	}

	/**
	 * Sets the date where the test was used the last time
	 * in the DD/MM/YYYY format 
	 * @param lastUse String
	 */
	public void setLastUse(final String lastUse) {
		this.lastUse = lastUse;
	}

	/**
	 * Sets the total number of questions that this test contains
	 * @param totalQuestion int
	 */
	public void setTotalQuestion(final int totalQuestion) {
		this.totalQuestion = totalQuestion;
	}

	/**
	 * Sets the relative path to the file that stores this test
	 * @param path String
	 */
	public void setPath(final String path) {
		this.path = path;
	}

	/**
	 * Sets if the test is enabled to be done or not
	 * @param enable boolean
	 */
	public void setEnable(final boolean enable) {
		this.enable = enable;
	}

	/**
	 * Sets the name of the abstract concept that
	 * evaluates the test.
	 * @param abstractConcept String
	 */
	public void setAbstractConcept(final String abstractConcept) {
		this.abstractConcept = abstractConcept;
	}

	/**
	 * Sets a Vector object with the names of the concepts
	 * that are associated with the test
	 * @param conceptVector Vector
	 */
	public void setConceptVector(final Vector conceptVector) {
		this.conceptVector = conceptVector;
	}

	/**
	 * Sets a Vector object with WOWQuestionsFileTest objects
	 * that contains information about the questions on the test 
	 * @param testVector Vector
	 */
	public void setTestVector(final Vector testVector) {
		this.testVector = testVector;
	}

	/**
	 * Sets if the presentation of the test must be showed
	 * @param showInitialInfo boolean
	 */
	public void setShowInitialInfo(final boolean showInitialInfo) {
		this.showInitialInfo = showInitialInfo;
	}

	/**
	 * Sets the order in which the questions must be showed
	 * to the student. Possible values are "SEQUENTIAL" 
	 * and "RANDOM".
	 * @param questionsOrder String
	 */
	public void setQuestionsOrder(final String questionsOrder) {
		this.questionsOrder = questionsOrder;
	}

	/**
	 * Sets the order in which the answers must be showed
	 * to the student. Possible values are "SEQUENTIAL" 
	 * and "RANDOM".
	 * @param answersOrder String
	 */
	public void setAnswersOrder(final String answersOrder) {
		this.answersOrder = answersOrder;
	}

	/**
	 * Sets if the explanation to the answers of the
	 * questions must be showed when the questions 
	 * will be evaluated.
	 * @param verbose boolean
	 */
	public void setVerbose(final boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Sets if the correct answers to the questions must be 
	 * showed when the questions will be evaluated.
	 * @param showCorrectAnswers boolean
	 */
	public void setShowCorrectAnswers(final boolean showCorrectAnswers) {
		this.showCorrectAnswers = showCorrectAnswers;
	}

	/**
	 * Sets if the result of the evaluation of 
	 * the question must be showed or not
	 * @param showQuestionCorrection boolean
	 */
	public void setShowQuestionCorrection(final boolean showQuestionCorrection) {
		this.showQuestionCorrection = showQuestionCorrection;
	}

	/**
	 * Sets if the questions that the student has not answered
	 * must be showed to him again 
	 * @param repeatWithoutAnswer boolean
	 */
	public void setRepeatWithoutAnswer(final boolean repeatWithoutAnswer) {
		this.repeatWithoutAnswer = repeatWithoutAnswer;
	}

	/**
	 * Sets if the final information about the execution of the test
	 * must be showed to the student when he finish it. 
	 * @param showFinalInfo boolean
	 */
	public void setShowFinalInfo(final boolean showFinalInfo) {
		this.showFinalInfo = showFinalInfo;
	}

	/**
	 * Sets the hexadecimal background color to the page that
	 * contains the test.
	 * @param bgColor String
	 */
	public void setBgColor(final String bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * Sets the background image of the page that contains
	 * the test
	 * @param background String
	 */
	public void setBackground(final String background) {
		this.background = background;
	}

	/**
	 * Sets the extension of the file (file type) of the
	 * background image of the page that contains the test 
	 * @param backgroundType String
	 */
	public void setBackgroundType(final String backgroundType) {
		this.backgroundType = backgroundType;
	}

	/**
	 * Sets the colour of the name of the test that will be
	 * showed in the main page of it.
	 * @param titleColor String
	 */
	public void setTitleColor(final String titleColor) {
		this.titleColor = titleColor;
	}

	/**
	 * Sets the maximum time in seconds to respond to any of the questions
	 * of the test.
	 * @param timeOfAnswer String
	 */
	public void setTimeOfAnswer(final String timeOfAnswer) {
		this.timeOfAnswer = timeOfAnswer;
	}

	/**
	 * Sets if the incorrect answers to the questions penalizes in 
	 * the final score of the test.
	 * @param incorrectAnswersPenalize boolean
	 */
	public void setIncorrectAnswersPenalize(final boolean incorrectAnswersPenalize) {
		this.incorrectAnswersPenalize = incorrectAnswersPenalize;
	}

	/**
	 * Sets the number of correct answers that a incorrect answer
	 * penalizes in the final score of the test. 
	 * @param incorrectAnswersPenalizeNumber int
	 */
	public void setIncorrectAnswersPenalizeNumber(final int incorrectAnswersPenalizeNumber) {
		this.incorrectAnswersPenalizeNumber = incorrectAnswersPenalizeNumber;
	}

	/**
	 * Sets if the not answered questions penalizes in the
	 * final score of the test.
	 * @param withoutAnswersPenalize boolean
	 */
	public void setWithoutAnswersPenalize(final boolean withoutAnswersPenalize) {
		this.withoutAnswersPenalize = withoutAnswersPenalize;
	}

	/**
	 * Sets the number of correct answers that a not answered question
	 * penalizes in the final score of the test. 
	 * @param withoutAnswersPenalizeNumber int
	 */
	public void setWithoutAnswersPenalizeNumber(final int withoutAnswersPenalizeNumber) {
		this.withoutAnswersPenalizeNumber = withoutAnswersPenalizeNumber;
	}

	/**
	 * Sets the percentage of knowledge that this test represents.
	 * @param knowledgePercentage String
	 */
	public void setKnowledgePercentage(final String knowledgePercentage) {
		this.knowledgePercentage = knowledgePercentage;
	}

	/**
	 * Sets the initial knowledge of a student who begins the 
	 * execution of this test.
	 * @param irtInitialProficiency double
	 */
	public void setIrtInitialProficiency(final double irtInitialProficiency) {
		this.irtInitialProficiency = irtInitialProficiency;
	}

	/**
	 * Sets the IRT model that will be used to choose the
	 * next question to show to the student.
	 * (1,2 or 3 parameters)
	 * @param irtModel int
	 */
	public void setIrtModel(final int irtModel) {
		this.irtModel = irtModel;
	}

	/**
	 * Sets the stop rule that an adaptive test will use. Possible
	 * values are "standardError" and "numberItemsAdministred".
	 * @param irtStopCriterion String
	 */
	public void setIrtStopCriterion(final String irtStopCriterion) {
		this.irtStopCriterion = irtStopCriterion;
	}

	/**
	 * Sets the standar error that must be made to end an
	 * adaptive test.
	 * @param irtStandardError double
	 */
	public void setIrtStandardError(final double irtStandardError) {
		this.irtStandardError = irtStandardError;
	}

	/**
	 * Sets the number of items that must be supplied to end
	 * an adaptive test.
	 * @param irtNumberItemsAdministred int
	 */
	public void setIrtNumberItemsAdministred(final int irtNumberItemsAdministred) {
		this.irtNumberItemsAdministred = irtNumberItemsAdministred;
	}

	/**
	 * Sets a Vector object with the students that has done the
	 * test and information about them.
	 * @param studentVector Vector
	 */
	public void setStudentVector(final Vector studentVector) {
		this.studentVector = studentVector;
	}

	/**
	 * Retuns the name of the course that this test belongs to
	 * @return String
	 */
	public String getCourse() {
		return course.trim();
	}

	/**
	 * Returns the test type. Possible values are
	 * "activity" and "exam" 
	 * @return String
	 */
	public String getTestType() {
		return testType.trim();
	}

	/**
	 * Returns the type of execution of the test.
	 * Possible values are "classic" and "adaptive" 
	 * @return String
	 */
	public String getExecutionType() {
		return executionType.trim();
	}

	/**
	 * Returns the name of the file that stores the 
	 * information about this test.
	 * @return String
	 */
	public String getTestFileName() {
		return testFileName.trim();
	}

	/**
	 * Returns the name of this test.
	 * @return String
	 */
	public String getTestName() {
		return testName.trim();
	}

	/**
	 * Returns the number of times that this
	 * test has been done.
	 * @return int
	 */
	public int getNumberOfUses() {
		return numberOfUses;
	}

	/**
	 * Returns a String in the format DD/MM/YYYY 
	 * indicating the last time this test was done
	 * @return String
	 */
	public String getLastUse() {
		return lastUse.trim();
	}

	/**
	 * Returns the total number of questions of the test
	 * @return int
	 */
	public int getTotalQuestion() {
		return totalQuestion;
	}

	/**
	 * Returns the relative path to the file that stores
	 * this test
	 * @return String
	 */
	public String getPath() {
		return path.trim();
	}

	/**
	 * Returns a boolean value indicating if this
	 * test is enabled to be done or not
	 * @return boolean
	 */
	public boolean getEnable() {
		return enable;
	}

	/**
	 * Returns the name of the abstract concept
	 * that evaluates this test.
	 * @return String
	 */
	public String getAbstractConcept() {
		return abstractConcept.trim();
	}

	/**
	 * Returns a Vector object where each position contains
	 * the name of the concepts associated to this test
	 * @return Vector
	 */
	public Vector getConceptVector() {
		return conceptVector;
	}

	/**
	 * Returns a Vector object where each position contains a
	 * WOWQuestionFileTest with the relative path to a question 
	 * file and a Vector object with the questions codes of the 
	 * questions that contains the test.
	 * @return Vector
	 */
	public Vector getTestVector() {
		return testVector;
	}

	/**
	 * Returns a boolean value indicating if the presentation
	 * screen of the test must be showed to the student or not.
	 * @return boolean
	 */
	public boolean getShowInitialInfo() {
		return showInitialInfo;
	}

	/**
	 * Returns a value indicating the show order of the questions.
	 * Possible values are "SEQUENTIAL" and "RANDOM".
	 * @return String
	 */
	public String getQuestionsOrder() {
		return questionsOrder.trim();
	}

	/**
	 * Returns a value indicating the show order of the answers.
	 * Possible values are "SEQUENTIAL" and "RANDOM".
	 * @return String
	 */
	public String getAnswersOrder() {
		return answersOrder.trim();
	}

	/**
	 * Returns a boolean value indicating if the explanation to the answers of the
	 * questions must be showed when the questions will be evaluated
	 * @return boolean
	 */
	public boolean getVerbose() {
		return verbose;
	}

	/**
	 * Returns a boolean value indicating if the correct answer must
	 * be showed to the student or not.
	 * @return boolean
	 */
	public boolean getShowCorrectAnswers() {
		return showCorrectAnswers;
	}

	/**
	 * Returns a boolean value if the result of the evaluation of 
	 * the question must be showed or not.
	 * @return boolean
	 */
	public boolean getShowQuestionCorrection() {
		return showQuestionCorrection;
	}

	/**
	 * Returns a boolean value indicating if the not-answered
	 * question must be showed to the student again.
	 * @return boolean
	 */
	public boolean getRepeatWithoutAnswer() {
		return repeatWithoutAnswer;
	}

	/**
	 * Returns a boolean value indicating if the final information
	 * about the made test must be showed to the student or not. 
	 * @return boolean
	 */
	public boolean getShowFinalInfo() {
		return showFinalInfo;
	}

	/**
	 * Returns a String containing an Hexadecimal code that indicates 
	 * the background colour of the page that contains the test.
	 * @return String
	 */
	public String getBgColor() {
		return bgColor.trim();
	}

	/**
	 * Returns the name of the background image of the page
	 * that contains the test. 
	 * @return String
	 */
	public String getBackground() {
		return background.trim();
	}

	/**
	 * Returns the file type of the background image 
	 * of the page that contains the test.
	 * @return String
	 */
	public String getBackgroundType() {
		return backgroundType.trim();
	}

	/**
	 * Returns a String containing an Hexadecimal code that indicates 
	 * the colour of the name of the test.
	 * @return String
	 */
	public String getTitleColor() {
		return titleColor.trim();
	}

	/**
	 * Returns the maximum time in seconds to answer 
	 * any question of the test.  
	 * @return String
	 */
	public String getTimeOfAnswer() {
		return timeOfAnswer.trim();
	}

	/**
	 * Returns a boolean value indicating if the incorrect 
	 * answers penalizes in the final score of the test.
	 * @return boolean
	 */
	public boolean getIncorrectAnswersPenalize() {
		return incorrectAnswersPenalize;
	}

	/**
	 * Returns the number of correct answers that a 
	 * incorrect answer penalizes.
	 * @return int
	 */
	public int getIncorrectAnswersPenalizeNumber() {
		return incorrectAnswersPenalizeNumber;
	}

	/**
	 * Returns a boolean value that indicates if the not 
	 * answered questions penalizes in the final score of the test.
	 * @return boolean
	 */
	public boolean getWithoutAnswersPenalize() {
		return withoutAnswersPenalize;
	}

	/**
	 * Returns the number of correct answers that a 
	 * not answeredr question penalizes.
	 * @return int
	 */
	public int getWithoutAnswersPenalizeNumber() {
		return withoutAnswersPenalizeNumber;
	}

	/**
	 * Returns the percentage of knowledge 
	 * (values between [0..100]) that this test represents. 
	 * @return String
	 */
	public String getKnowledgePercentage() {
		return knowledgePercentage.trim();
	}

	/**
	 * Returns the estimated Initial knowledge of the student, 
	 * used when an adaptive test is begun.
	 * @return double
	 */
	public double getIrtInitialProficiency() {
		return irtInitialProficiency;
	}

	/**
	 * Returns the IRT Model used to choose
	 * the next question of an adaptive test.
	 * (1, 2 or 3 parameters)
	 * @return int
	 */
	public int getIrtModel() {
		return irtModel;
	}

	/**
	 * Returns a value that indicates the stop rule that an adaptive test will use.
	 * Possible values are "standardError" and "numberItemsAdministred". 
	 * @return String
	 */
	public String getIrtStopCriterion() {
		return irtStopCriterion.trim();
	}

	/**
	 * Returns the value of the standar error that must be
	 * made to end an adaptive test.
	 * @return double
	 */
	public double getIrtStandardError() {
		return irtStandardError;
	}

	/**
	 * Returns the mumber of items that must be supplied 
	 * to end an adaptive test
	 * @return int
	 */
	public int getIrtNumberItemsAdministred() {
		return irtNumberItemsAdministred;
	}

	/**
	 * Returns a Vector object where each position contains an WOWStudent
	 * object that contains a student login, the number of times that this 
	 * student has done this test and a value indicating wether can repeat it or not 
	 * @return Vector
	 */
	public Vector getStudentVector() {
		return studentVector;
	}

	/**
	 * When the user tries to clone this object,
	 * a reference to himself is returned, as 
	 * a security mechanism.
	 * @return Object
	 */
	public Object clone() {
		return this;
	}

} // End of test class