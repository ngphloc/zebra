/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Question.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.Utils;

import java.util.Vector;
import java.lang.String;

/**
 * Question is an object class that represents questions and answers
 * in multiple-choice tests.
 */
public class Question {

  private String question;
  private Vector goodanswers;
  private Vector goodfeedback;
  private Vector badanswers;
  private Vector badfeedback;
  private int right_to_show = 0;
  private int answers_to_show = 0;
  private boolean lastanswerwasgood = false;;

  /**
   * The default (and only) constructor initializes the internal structure
   * of a "Question" object.
   */
  public Question() {
    question = new String();
    goodanswers = new Vector(3,1);
    goodfeedback = new Vector(3,1);
    badanswers = new Vector(5,1);
    badfeedback = new Vector(5,1);
  }

  /**
   * Return the question.
   */
  public String getQuestion() {
    return question;
  }

  /**
   * Set the question text.
   */
  public void setQuestion(String s) {
    question = s;
  }

  /**
   * Add text to a question.
   */
  public void concatQuestion(String s) {
    question += "\n" + s;
  }

  /**
   * Return the number of correct answers to show.
   */
  public int getRightToShow() {
    return right_to_show;
  }

  /**
   * Set the number of correct answers to show.
   */
  public void setRightToShow(int i) {
    right_to_show = i;
  }

  /**
   * Return the answers to show.
   */
  public int getAnswersToShow() {
    return answers_to_show;
  }

  /**
   * Set the answers to show.
   */
  public void setAnswersToShow(int i) {
    answers_to_show = i;
  }

  /**
   * Return the incorrect answers.
   */
  public Vector getBadAnswers() {
    return badanswers;
  }

  /**
   * Add an incorrect answer to the list of incorrect answers.
   */
  public void addBadAnswer(String s) {
    badanswers.addElement(s);
    lastanswerwasgood = false;
  }

  /**
   * Add text to the last incorrect answer.
   */
  public void concatBadAnswer(String s) {
    badanswers.setElementAt(badanswers.elementAt(badanswers.size()-1) + "\n" + s, badanswers.size()-1);
  }

  /**
   * Return the correct answers.
   */
  public Vector getGoodAnswers() {
    return goodanswers;
  }

  /**
   * Add a correct answer to the list of correct answers.
   */
  public void addGoodAnswer(String s) {
    goodanswers.addElement(s);
    lastanswerwasgood = true;
  }

  /**
   * Add text to the last correct answer.
   */
  public void concatGoodAnswer(String s) {
    goodanswers.setElementAt(goodanswers.elementAt(goodanswers.size()-1) + "\n" + s, goodanswers.size()-1);
  }

  /**
   * Return feedback on the correct answers.
   */
  public Vector getGoodFeedback() {
    return goodfeedback;
  }

  /**
   * Return feedback on the incorrect answers.
   */
  public Vector getBadFeedback() {
    return badfeedback;
  }

  /**
   * Add feedback to the question that was last added (correct or incorrect).
   */
  public void addFeedback(String s) {
    if (lastanswerwasgood)
      goodfeedback.addElement(s);
    else
      badfeedback.addElement(s);
  }

  /**
   * Add text to the last feedback.
   */
  public void concatFeedback(String s) {
    if (lastanswerwasgood)
      goodfeedback.setElementAt(goodfeedback.elementAt(goodfeedback.size()-1) + "\n" + s, goodfeedback.size()-1);
    else
      badfeedback.setElementAt(badfeedback.elementAt(badfeedback.size()-1) + "\n" + s, badfeedback.size()-1);
  }

  /**
   * Set the number of answers and the number of correct answers.
   */
  public void setSize(int NbAnswers, int NbCorrect) {
    answers_to_show = NbAnswers;
    right_to_show = NbCorrect;
  }

  /**
   * Verify that the number of actual correct and incorrect answers match the
   * numbers that were set.
   *
   * @exception	WowException	If the numbers don't match the existing answers.
   */
  public void verify()
  //  throws WowException
  {
    /*if (question == null || question.length()==0)
      throw new WowException("Invalid quiz file: question text is empty.");
    if (goodanswers.size()<1)
      throw new WowException("Invalid quiz file: question has no correct answers.");
    if (badanswers.size()<1)
      throw new WowException("Invalid quiz file: question has no incorrect answers.");
    if (right_to_show<1)
      throw new WowException("Invalid quiz file: question shows no correct answer.");
    if (answers_to_show<=right_to_show)
      throw new WowException("Invalid quiz file: question shows no incorrect answer.");
    if (goodanswers.size() < right_to_show)
      throw new WowException("Invalid quiz file: question tries to show more correct answers than there are.");
    if (badanswers.size() < answers_to_show-right_to_show)
      throw new WowException("Invalid quiz file: question tries to show more incorrect answers than there are.");
  */
  }
}
