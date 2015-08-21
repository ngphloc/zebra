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
 * NAME: Question
 * FUNCTION: This class encapsulates all the data that is necessary to
 *  manage all the information about a question of a test.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class Question implements Serializable, Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Indicates if this question is located in the first or
	 * the last place of the file, or if is in the middle
	 * or if is the only one question of the file. Possible values  
	 * are, respectively, "first", "last", "NoLastNoFirst" and "one question".
	 */
	private String firstLastQuestion = "";
	
	/**
	 * Name of the course that the file that 
	 * contains this question belongs to.
	 */
	private String course = ""; 
	
	/**
	 * Name of the concept associated to the question file
	 * where this question is stored.
	 */
	private String concept = ""; 
	
	/**
	 * Name of the question file that contains this question.
	 */
	private String questionsFileName = ""; 
	
	/**
	 * Question code.
	 */
	private String codeQuestion = "";
	
	/**
	 * Enunciation of the question
	 */
	private String enunciate = "";
	
	/**
	 * Relative path to the image associated to the question
	 */
	private String pathImage = ""; 
	
	/**
	 * Indicates if the question has a associated image. 
	 * Possible values are "true", "false" and "INDIFFERENT".
	 */
	private String existImage = ""; 
	
	/**
	 * Difficulty of the question
	 */ 
	private double difficulty = 0;
	
	/**
	 * Maximum difficulty of the question
	 */
	private double difficultyMax = 0;
	
	/**
	 * Minimum difficulty of the question
	 */
	private double difficultyMin = 0;
	
	/**
	 * Discrimination of the question
	 */
	private double discrimination = 0; 
	
	/**
	 * Maximum discrimination of the question
	 */
	private double discriminationMax = 0;
	
	/**
	 * Minimum discrimination of the question
	 */
	private double discriminationMin = 0;
	
	/**
	 * Guess of the question
	 */
	private double guessing = 0; 
	
	/**
	 * Maximum guess of the question
	 */
	private double guessingMax = 0;
	
	/**
	 * Minimum guess of the question
	 */
	private double guessingMin = 0;
	
	/**
	 * Value of the information function for the question
	 */
	private double info = 99999;
	
	/**
	 * Exposition rate to the question
	 */
	private double exhibitionRate = 0; 
	
	/**
	 * Maximum exposition rate to the question
	 */
	private double exhibitionRateMax = 0;
	
	/**
	 * Minimum exposition rate to the question
	 */
	private double exhibitionRateMin = 0;
	
	/**
	 * Medium time to answer this question
	 */
	private int answerTime = 0; 
	
	/**
	 * Maximum time to answer this question
	 */
	private int answerTimeMax = 0;
	
	/**
	 * Minimum time to answer this question
	 */
	private int answerTimeMin = 0;
	
	/**
	 * Medium success rate of the question
	 */
	private double successRate = 0; 
	
	/**
	 * Maximum success rate of the question
	 */
	private double successRateMax = 0;
	
	/**
	 * Minimum success rate of the question
	 */
	private double successRateMin = 0;
	
	/**
	 * Number of times that this question has been used in a test
	 */
	private int numberOfUses = -1; 
	
	/**
	 * Maximum number of times that this question has been used in a test
	 */
	private int numberOfUsesMax = -1; 
	
	/**
	 * Minimum number of times that this question has been used in a test
	 */
	private int numberOfUsesMin = -1; 
	
	/**
	 * Number of times that this question has been successfully answered
	 */
	private int numberOfSuccesses = -1;
	
	/**
	 * This is a list with all the texts of the answers of the question
	 */
	private Vector textAnswers = null; 
	
	/**
	 * This is a list that indicates if the answers are correct or not
	 */
	private Vector correctAnswers = null; 
	
	/**
	 * This is a list with all the texts of the explanation to the
	 * answers of the question
	 */
	private Vector textExplanation = null;
	
	/**
	 * This is a list with the answer codes for all the answers of
	 * this question. 
	 */
	private Vector codeAnswersVector = null; 
	
	/**
	 * Indicates if this question belongs to any classic test.
	 * Possible values are "true", "false" and "INDIFFERENT"
	 */
	private String classicTest = ""; 
	
	/**
	 * A list with the name of the classic test files 
	 * that this question belongs to. 
	 */
	private Vector classicTestVector = null; //Almacena los nombres de los
	
	/**
	 * Indicates if this question belongs to any adaptive test.
	 * Possible values are "true", "false" and "INDIFFERENT"
	 */
	private String adaptiveTest = "";
	
	/**
	 * A list with the name of the adaptive test files 
	 * that this question belongs to.
	 */
	private Vector adaptiveTestVector = null; 
	
	/**
	 * Total number of questions that the file that 
	 * stores this question contains.
	 */
	private int totalQuestionsInFile = 0; 
	
	/**
	 * Number of possible answers that this question has.
	 */
	private int numberOfAnswers = 0;
	
	/**
	 * Maximum number of possible answers that this question has.
	 */
	private int numberOfAnswersMax = 0;
	
	/**
	 * Minimum number of possible answers that this question has.
	 */
	private int numberOfAnswersMin = 0;
	
	/**
	 * Position number that this question take in the question file.
	 */
	private int numberQuestionOrder = 0; 
	
	/**
	 * This is a list that indicates which of the anwers are correct.
	 */
	private Vector answersCorrect = null; 
	
	/**
	 * Indicates if the answer to the question 
	 * has been correct or wrong.
	 */
	private boolean correct = false;

	
	/**
	 * Sets the name of the course that 
	 * this question belongs to.
	 * @param course String
	 */
	public void setCourse(final String course) {
		this.course = course;
	}

	/**
	 * Sets the name of the concept associated to the question file
	 * where this question is stored.
	 * @param concept String
	 */
	public void setConcept(final String concept) {
		this.concept = concept;
	}

	/**
	 * Sets the name of the question file where
	 * this question is stored.
	 * @param questionsFileName String
	 */
	public void setQuestionsFileName(final String questionsFileName) {
		this.questionsFileName = questionsFileName;
	}

	/**
	 * Sets the code of the question.
	 * @param codeQuestion String
	 */
	public void setCodeQuestion(final String codeQuestion) {
		this.codeQuestion = codeQuestion;
	}

	/**
	 * Sets the enunciate of the question
	 * @param enunciate String
	 */
	public void setEnunciate(final String enunciate) {
		this.enunciate = enunciate;
	}

	/**
	 * Sets the relative path to the image associated
	 * to the question.
	 * @param pathImage String
	 */
	public void setPathImage(final String pathImage) {
		this.pathImage = pathImage;
	}

	/**
	 * Sets a value that indicates if the question has
	 * a image associated to it.
	 * @param existImage String
	 */
	public void setExistImage(final String existImage) {
		this.existImage = existImage;
	}

	/**
	 * Sets the difficulty of the question.
	 * @param difficulty double
	 */
	public void setDifficulty(final double difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 * Sets the maximum difficulty of the question.
	 * @param difficultyMax double
	 */
	public void setDifficultyMax(final double difficultyMax) {
		this.difficultyMax = difficultyMax;
	}

	/**
	 * Sets the minimum difficulty of the question.
	 * @param difficultyMin double
	 */
	public void setDifficultyMin(final double difficultyMin) {
		this.difficultyMin = difficultyMin;
	}

	/**
	 * Sets the discrimination of the question.
	 * @param discrimination double
	 */
	public void setDiscrimination(final double discrimination) {
		this.discrimination = discrimination;
	}

	/**
	 * Sets the maximum discrimination of the question.
	 * @param discriminationMax double
	 */
	public void setDiscriminationMax(final double discriminationMax) {
		this.discriminationMax = discriminationMax;
	}

	/**
	 * Sets the minimum discrimination of the question
	 * @param discriminationMin double
	 */
	public void setDiscriminationMin(final double discriminationMin) {
		this.discriminationMin = discriminationMin;
	}

	/**
	 * Sets the guess of the question
	 * @param guessing double
	 */
	public void setGuessing(final double guessing) {
		this.guessing = guessing;
	}

	/**
	 * Sets the maximum guess of the question
	 * @param guessingMax double
	 */
	public void setGuessingMax(final double guessingMax) {
		this.guessingMax = guessingMax;
	}

	/**
	 * Sets the minimum guess of the question.
	 * @param guessingMin double
	 */
	public void setGuessingMin(final double guessingMin) {
		this.guessingMin = guessingMin;
	}

	/**
	 * Sets the value of the information function for the question.
	 * @param info double
	 */
	public void setInformationValue(final double info) {
		this.info = info;
	}

	/**
	 * Sets the exposition rate to the question.
	 * @param exhibitionRate double
	 */
	public void setExhibitionRate(final double exhibitionRate) {
		this.exhibitionRate = exhibitionRate;
	}

	/**
	 * Sets the maximum exposition rate to the question.
	 * @param exhibitionRateMax double
	 */
	public void setExhibitionRateMax(final double exhibitionRateMax) {
		this.exhibitionRateMax = exhibitionRateMax;
	}

	/**
	 * Sets the minimum exposition rate to the question.
	 * @param exhibitionRateMin double
	 */
	public void setExhibitionRateMin(final double exhibitionRateMin) {
		this.exhibitionRateMin = exhibitionRateMin;
	}

	/**
	 * Sets the medium time to answer this question.
	 * @param answerTime int
	 */
	public void setAnswerTime(final int answerTime) {
		this.answerTime = answerTime;
	}

	/**
	 * Sets the maximum time to answer this question.
	 * @param answerTimeMax int
	 */
	public void setAnswerTimeMax(final int answerTimeMax) {
		this.answerTimeMax = answerTimeMax;
	}

	/**
	 * Sets the minimum time to answer this question.
	 * @param answerTimeMin int
	 */
	public void setAnswerTimeMin(final int answerTimeMin) {
		this.answerTimeMin = answerTimeMin;
	}

	/**
	 * Sets the medium success rate of the question.
	 * @param successRate double
	 */
	public void setSuccessRate(final double successRate) {
		this.successRate = successRate;
	}

	/**
	 * Sets the maximum success rate of the question.
	 * @param successRateMax double
	 */
	public void setSuccessRateMax(final double successRateMax) {
		this.successRateMax = successRateMax;
	}

	/**
	 * Sets the minimum success rate of the question.
	 * @param successRateMin double
	 */
	public void setSuccessRateMin(final double successRateMin) {
		this.successRateMin = successRateMin;
	}

	/**
	 * Sets the number of times that this question has been used in a test.
	 * @param numberOfUses int
	 */
	public void setNumberOfUses(final int numberOfUses) {
		this.numberOfUses = numberOfUses;
	}

	/**
	 * Sets the number of times that this question has been 
	 * succesfully answered.
	 * @param numberOfSuccesses int
	 */
	public void setNumberOfSuccesses(final int numberOfSuccesses) {
		this.numberOfSuccesses = numberOfSuccesses;
	}

	/**
	 * Sets a list with all the texts of the answers of the question.
	 * @param textAnswers Vector
	 */
	public void setTextAnswers(final Vector textAnswers) {
		this.textAnswers = textAnswers;
	}

	/**
	 * Sets a list that indicates if the answers are correct or not.
	 * @param correctAnswers Vector
	 */
	public void setCorrectAnswers(final Vector correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	/**
	 * Sets a list with all the texts of the explanation to the
	 * answers of the question.
	 * @param textExplanation Vector
	 */
	public void setTextExplanation(final Vector textExplanation) {
		this.textExplanation = textExplanation;
	}

	/**
	 * Sets a a list with the answer codes for all the answers of
	 * this question.
	 * @param codeAnswersVector Vector
	 */
	public void setCodeAnswers(final Vector codeAnswersVector) {
		this.codeAnswersVector = codeAnswersVector;
	}

	/**
	 * Sets if this question belongs to any classic test.
	 * @param classicTest String
	 */
	public void setClassicTest(final String classicTest) {
		this.classicTest = classicTest;
	}

	/**
	 * Sets a list with the name of the classic test files 
	 * that this question belongs to. 
	 * @param classicTestVector Vector
	 */
	public void setClassicTestVector(final Vector classicTestVector) {
		this.classicTestVector = classicTestVector;
	}

	/**
	 * Sets if this question belongs to any adaptive test.
	 * @param adaptiveTest
	 */
	public void setAdaptiveTest(final String adaptiveTest) {
		this.adaptiveTest = adaptiveTest;
	}

	/**
	 * Sets a list with the name of the addaptive test files 
	 * that this question belongs to. 
	 * @param adaptiveTestVector
	 */
	public void setAdaptiveTestVector(final Vector adaptiveTestVector) {
		this.adaptiveTestVector = adaptiveTestVector;
	}

	/**
	 * Sets the total number of questions that the file that 
	 * stores this question contains.
	 * @param totalQuestionsInFile int
	 */
	public void setTotalQuestionsInFile(final int totalQuestionsInFile) {
		this.totalQuestionsInFile = totalQuestionsInFile;
	}

	/**
	 * Sets the relative position of this question on his file.
	 * @param firstLastQuestion String
	 */
	public void setFirstLastQuestion(final String firstLastQuestion) {
		this.firstLastQuestion = firstLastQuestion;
	}

	/**
	 * Sets the number of answers that this question has.
	 * @param numberOfAnswers int
	 */
	public void setNumberOfAnswers(final int numberOfAnswers) {
		this.numberOfAnswers = numberOfAnswers;
	}

	/**
	 * Sets the maximum number of answers that this question has.
	 * @param numberOfAnswersMax int
	 */
	public void setNumberOfAnswersMax(final int numberOfAnswersMax) {
		this.numberOfAnswersMax = numberOfAnswersMax;
	}

	/**
	 * Sets the minimum number of answers that this question has.
	 * @param numberOfAnswersMin int
	 */
	public void setNumberOfAnswersMin(final int numberOfAnswersMin) {
		this.numberOfAnswersMin = numberOfAnswersMin;
	}

	/**
	 * Sets the position number that this question take in the question file.
	 * @param numberQuestionOrder int
	 */
	public void setNumberQuestionOrder(final int numberQuestionOrder) {
		this.numberQuestionOrder = numberQuestionOrder;
	}

	/**
	 * Sets a list that indicates which of the anwers are correct.
	 * @param answersCorrect Vector
	 */
	public void setAnswersCorrect(final Vector answersCorrect) {
		this.answersCorrect = answersCorrect;
	}

	/**
	 * Sets if the answer to the question has been correct or wrong.
	 * @param correct boolean
	 */
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	/**
	 * Returns the name of the course that the question is associated to. 
	 * @return String
	 */
	public String getCourse() {
		return course.trim();
	}

	/**
	 * Returns the name of the concept that the question file that 
	 * contains this question is associated with.
	 * @return String
	 */
	public String getConcept() {
		return concept.trim();
	}

	/**
	 * Returns the name of the question file that contains this question.
	 * @return String
	 */
	public String getQuestionsFileName() {
		return questionsFileName.trim();
	}

	/** 
	 * Returns the question code.
	 * @return String
	 */
	public String getCodeQuestion() {
		return codeQuestion.trim();
	}

	/**
	 * Returns the enunciate of the question.
	 * @return String
	 */
	public String getEnunciate() {
		return enunciate.trim();
	}

	/**
	 * Returns the relative path to the image associated to the question.
	 * @return String
	 */
	public String getPathImage() {
		return pathImage.trim();
	}

	/**
	 * Returns if the question has a associated image. 
	 * @return String
	 */
	public String getExistImage() {
		return existImage.trim();
	}

	/**
	 * Returns the medium difficulty of the question.
	 * @return double
	 */
	public double getDifficulty() {
		return difficulty;
	}

	/**
	 * Returns the maximum difficulty of the question.
	 * @return double
	 */
	public double getDifficultyMax() {
		return difficultyMax;
	}

	/**
	 * Returns the minimum difficulty of the question.
	 * @return double
	 */
	public double getDifficultyMin() {
		return difficultyMin;
	}

	/**
	 * Returns the medium discrimination of the question.
	 * @return double
	 */
	public double getDiscrimination() {
		return discrimination;
	}

	/**
	 * Returns the maximum discrimination of the question.
	 * @return double
	 */
	public double getDiscriminationMax() {
		return discriminationMax;
	}

	/**
	 * Returns the minimum discrimination of the question.
	 * @return double
	 */
	public double getDiscriminationMin() {
		return discriminationMin;
	}

	/**
	 * Returns the guess of the question.
	 * @return double
	 */
	public double getGuessing() {
		return guessing;
	}

	/**
	 * Returns the maximum guess of the question.
	 * @return double
	 */
	public double getGuessingMax() {
		return guessingMax;
	}

	/**
	 * Returns the minimum guess of the question.
	 * @return double
	 */
	public double getGuessingMin() {
		return guessingMin;
	}

	/**
	 * Returns the value of the information function of the question.
	 * @return double
	 */
	public double getInformationValue() {
		return info;
	}

	/**
	 * Returns the medium exhibition rate of the question.
	 * @return double
	 */
	public double getExhibitionRate() {
		return exhibitionRate;
	}

	/**
	 * Returns the maximum exhibition rate of the question.
	 * @return double
	 */
	public double getExhibitionRateMax() {
		return exhibitionRateMax;
	}

	/**
	 * Returns the minimum exhibition rate of the question.
	 * @return double
	 */
	public double getExhibitionRateMin() {
		return exhibitionRateMin;
	}

	/**
	 * Returns the medium time to answer the question
	 * @return int
	 */
	public int getAnswerTime() {
		return answerTime;
	}

	/**
	 * Returns the maximum time to answer the question
	 * @return int
	 */
	public int getAnswerTimeMax() {
		return answerTimeMax;
	}

	/**
	 * Returns the minimum time to answer the question
	 * @return int
	 */
	public int getAnswerTimeMin() {
		return answerTimeMin;
	}

	/**
	 * Returns the rate of sucess of the question
	 * @return double
	 */
	public double getSuccessRate() {
		return successRate;
	}

	/**
	 * Returns the maximum rate of sucess of the question
	 * @return double
	 */
	public double getSuccessRateMax() {
		return successRateMax;
	}

	/**
	 * Returns the minimum rate of sucess of the question
	 * @return double
	 */
	public double getSuccessRateMin() {
		return successRateMin;
	}

	/**
	 * Returns the number of times that this question has been used in a test.
	 * @return int
	 */
	public int getNumberOfUses() {
		return numberOfUses;
	}

	/**
	 * Returns the maximum number of times that this question has been used in a test.
	 * @return int
	 */
	public int getNumberOfUsesMax() {
		return numberOfUsesMax;
	}

	/**
	 * Returns the minimum number of times that this question has been used in a test.
	 * @return int
	 */
	public int getNumberOfUsesMin() {
		return numberOfUsesMin;
	}

	/**
	 * Returns the number of times that this question has successfully
	 * answered.
	 * @return int
	 */
	public int getNumberOfSuccesses() {
		return numberOfSuccesses;
	}

	/**
	 * Returns a list with all the texts of the answers of the question.
	 * @return Vector
	 */
	public Vector getTextAnswers() {
		return textAnswers;
	}

	/**
	 * Returns a list that indicates if the answers are correct or not. 
	 * @return Vector
	 */
	public Vector getCorrectAnswers() {
		return correctAnswers;
	}

	/**
	 * Returns a a list with all the texts of the explanation to the
	 * answers of the question.
	 * @return Vector
	 */
	public Vector getTextExplanation() {
		return textExplanation;
	}

	/**
	 * Returns a list with the answer codes for all the answers of
	 * this question. 
	 * @return Vector
	 */
	public Vector getCodeAnswers() {
		return codeAnswersVector;
	}

	/**
	 * Returns if this question belongs to any classic test.
	 * @return String
	 */
	public String getClassicTest() {
		return classicTest.trim();
	}

	/**
	 * Returns a list with the name of the classic test files 
	 * that this question belongs to. 
	 * @return Vector
	 */
	public Vector getClassicTestVector() {
		return classicTestVector;
	}

	/**
	 * Returns if this question belongs to any adaptive test.
	 * @return String
	 */
	public String getAdaptiveTest() {
		return adaptiveTest.trim();
	}

	/**
	 * Returns a list with the name of the adaptive test files 
	 * that this question belongs to. 
	 * @return Vector
	 */
	public Vector getAdaptiveTestVector() {
		return adaptiveTestVector;
	}

	/**
	 * Returns the total number of questions that the file that 
	 * stores this question contains.
	 * @return int
	 */
	public int getTotalQuestionsInFile() {
		return totalQuestionsInFile;
	}

	/**
	 * Returns the relative position of this question in his file.
	 * @return String
	 */
	public String getFirstLastQuestion() {
		return firstLastQuestion.trim();
	}

	/**
	 * Returns the number of possible answers that this question has.
	 * @return int
	 */
	public int getNumberOfAnswers() {
		return numberOfAnswers;
	}

	/**
	 * Returns the maximum number of answers that this question has.
	 * @return int
	 */
	public int getNumberOfAnswersMax() {
		return numberOfAnswersMax;
	}

	/**
	 * Returns the minimum number of answers that this question has.
	 * @return int
	 */
	public int getNumberOfAnswersMin() {
		return numberOfAnswersMin;
	}

	/**
	 * Returns the position number that this question take in the question file.
	 * @return int
	 */
	public int getNumberQuestionOrder() {
		return numberQuestionOrder;
	}

	/**
	 * Returns a list that indicates which of the anwers are correct.
	 * @return Vector
	 */
	public Vector getAnswersCorrect() {
		return answersCorrect;
	}

	/**
	 * Returns true if the question has been successfully answered,
	 * false otherwise.
	 * @return boolean
	 */
	public boolean isCorrect() {
		return correct;
	}

	/**
	 * Returns a reference to this object.
	 */
	public Object clone() {
		return this;
	}

} // End of Question class
