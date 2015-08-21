package es.uco.WOW.TestConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import es.uco.WOW.Utils.UtilLog;

public class FromGift {

	protected int answers;

	protected int minAnswers;

	protected int correctAnswers;

	protected WriteWOW3 writeWOW3;
	
	protected FileOfQuestionsStruct fileQuestion;
	
	protected int flag;

	protected int oldFlag;
	
	protected String oldAttrib;

	protected String path;
	
	protected int numberOfQuestion;
	
	public FromGift ( ) {
		fileQuestion = new FileOfQuestionsStruct ( );
		writeWOW3 = new WriteWOW3 ( );

		numberOfQuestion = 0;
		answers = 0;
		minAnswers = 20;
		correctAnswers = 0;
		numberOfQuestion = 0;
	}

	//
	// MAIN
	//

	/** Main program entry point. */
	public boolean convert ( String inputFile , String outputFile ) {


		try {

			File p = new File ( inputFile );
			path = p.getPath ( );
			fileQuestion.setFileName ( p.getName ( ) );
			fileQuestion.setCourse ( p.getName ( ) );

		} catch ( Exception e ) {
			UtilLog.writeException(e);
		}

		if ( readTextFile ( inputFile ) ) {
			if ( numberOfQuestion > 0 )
				if ( writeWOW3.writeWOWFile ( fileQuestion , outputFile ) ) {
					UtilLog.toLog("Conversion terminada correctamente.");
					return true;
				} else {
					UtilLog.toLog("No se realizó la conversion por un error en el proceso.");
					return false;
				}
			else {
				UtilLog.toLog("No se realizó la conversion por un error en el proceso.");
				return false;
			}

		} else
			return false;

	} // convert (String , String )

	//
	// Private methods
	//

	public boolean readTextFile(String File) {
		BufferedReader fileInput = null;
		String tmp = "";

		try {
			fileInput = new BufferedReader(new FileReader(File));
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		while (tmp != null) {
			try {
				tmp = fileInput.readLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
				return false;
			}

			processLine(tmp);
		}
		return true;
	}

	/*
	 * NAME: processLine ( String tmp ) IT'S FROM: FromGift. CALLED FROM: Main
	 * program CALL TO: from WriteWOW3 Class: String getName ( ) int
	 * getCodeQuestion ( int positionQuestion ) int getNumberOfAnswers ( int
	 * positionQuestion ) int getNumberOfCorrect ( int positionQuestion ) String
	 * getExplanation ( int positionQuestion , int positionAnswer ) String
	 * getTextAnswer ( int positionQuestion , int positionAnswer ) String
	 * getEnunciate ( int positionQuestion )
	 * 
	 * recibed: void RETURN: boolean FUNCTION: Write in a file why call the
	 * program. ( Only Can write simple and multiple choice ).
	 */
	public boolean processLine(String tmp) {

		if (tmp == null)
			return true;

		if ( tmp.indexOf ( "//" ) >= 0 )
			//
			// it is a coment
			//
			return false;
		else if ( tmp.indexOf ( "::" ) >= 0 )
			//
			// it is a question start line
			//
			flag = 0;
		else if (tmp.indexOf("=") >= 0 || tmp.indexOf("~") >= 0)
			//
			// it is a answer line
			//
			flag = 1;
		else
			//
			// it is order thing
			//
			flag = 0;
		switch (flag) {
			case 0:
				if (tmp.indexOf("//") < 0) {

					if (tmp.indexOf("::") == 0) {
						if (tmp.indexOf("::", tmp.indexOf("::") + 1) > tmp.indexOf("::")) {
							//
							// this is enunciate
							//

							
							if (numberOfQuestion > 0)
							{
								if (minAnswers > answers) {
									minAnswers = answers;
									fileQuestion.setNumberOfAnswersForFile ( minAnswers );
								}
								fileQuestion.setNumberOfCorrect(correctAnswers);
								fileQuestion.setNumberOfAnswers(answers);
							}
							numberOfQuestion++;
							fileQuestion.setCodeQuestion(numberOfQuestion);

							answers = 0;
							correctAnswers = 0;
							if (tmp.indexOf("{") > 0) {
								fileQuestion.setEnunciate(tmp.substring(tmp.indexOf("::", tmp.indexOf("::") + 1) + 2,
										tmp.indexOf("{")));
								flag = 1;
								if (tmp.indexOf("}") > 0 && tmp.indexOf("}") > tmp.indexOf("{")) {
									if (tmp.indexOf("->") > 0) {
										fileQuestion.setCodeQuestion(0, numberOfQuestion * -1);
										fileQuestion.setEnunciate("Error this question is a join question.");
										flag = 0;
									}
									if (tmp.indexOf("#") > 0) {
										if (tmp.indexOf("..") > 0 && tmp.indexOf("..") > tmp.indexOf("#")) {
											fileQuestion.setCodeQuestion(0, numberOfQuestion * -1);
											fileQuestion.setEnunciate("Error this question is a numerical question.");
											flag = 0;
										}
									}
									if (tmp.indexOf("TRUE") > 0) {
										if (tmp.indexOf("#") > 0 && tmp.indexOf("#") == tmp.indexOf("TRUE") + 1) {
											answers++;
											fileQuestion.setNumberOfAnswers(answers);

											fileQuestion.setCodeAnswer(answers);
											if (tmp.indexOf("#", tmp.indexOf("#")) > 0) {
												fileQuestion.setTextAnswer(tmp.substring(tmp.indexOf("#"), tmp.indexOf(
														"#", tmp.indexOf("#"))));
												answers++;

												fileQuestion.setNumberOfAnswers(answers);
												fileQuestion.setCodeAnswer(answers);
												fileQuestion.setTextAnswer(tmp.substring(tmp.indexOf("#", tmp
														.indexOf("#"))));
												flag = 0;
											} else {
												fileQuestion.setTextAnswer(tmp.substring(tmp.indexOf("#")));
												flag = 1;
											}
										}
									}

									if (numberOfQuestion > 0)
									{
										if (minAnswers > answers) {
											minAnswers = answers;
											fileQuestion.setNumberOfAnswersForFile ( minAnswers );
										}
										fileQuestion.setNumberOfCorrect(correctAnswers);
										fileQuestion.setNumberOfAnswers(answers);
									}
									numberOfQuestion++;
									fileQuestion.setCodeQuestion(numberOfQuestion);

									answers = 0;
									correctAnswers = 0;
								}
							} else {
								fileQuestion.setCodeQuestion(0, numberOfQuestion * -1);
								fileQuestion.setEnunciate("Error the structure of this question is incorrect.");
							}
						}
					} else
						return false;
				} else
					return false;
				break;
			case 1:
				if (tmp.indexOf("->") > 0) {
					//
					// the question is a join type
					//
					fileQuestion.setCodeQuestion(0, numberOfQuestion * -1);
					fileQuestion.setEnunciate("Error this question is a join question.");
				} else {
					if (tmp.indexOf("~") > 0) {
						//
						// isn't correct
						//
						if (tmp.indexOf("%", tmp.indexOf("~")) > 0) {
							if (tmp.indexOf("%", tmp.indexOf("%", tmp.indexOf("~") + 1) + 1) > 0) {
								//
								// has a percent
								//

								/** Add a new incorrect answer */

								answers++;

								fileQuestion.setNumberOfAnswers(answers);
								fileQuestion.setCodeAnswer(answers);
								fileQuestion.setCorrect("false");

								if (tmp.indexOf("#", tmp.indexOf("%", tmp.indexOf("%", tmp.indexOf("~")) + 1) + 1) > 0) {
									//
									// has a explanation
									//

									fileQuestion.setTextAnswer(tmp.substring(tmp.indexOf("%", tmp.indexOf("%", tmp
											.indexOf("~") + 1) + 1) + 1, tmp.indexOf("#", tmp.indexOf("%", tmp.indexOf(
											"%", tmp.indexOf("~") + 1)))));
									fileQuestion.setExplanation(tmp.substring(tmp.indexOf("#", tmp.indexOf("%", tmp
											.indexOf("%", tmp.indexOf("~") + 1) + 1) + 1) + 1));
								} else {
									//
									// hasn't a explanation
									//
									fileQuestion.setTextAnswer(tmp.substring(tmp.indexOf("%", tmp.indexOf("%", tmp
											.indexOf("~")))));
								}
							}
						} else {
							//
							// hasn't percent
							//

							/** Add a new incorrect answer */

							answers++;

							fileQuestion.setNumberOfAnswers(answers);
							fileQuestion.setCodeAnswer(answers);
							fileQuestion.setCorrect("false");

							if (tmp.indexOf("#", tmp.indexOf("~")) > 0) {
								//
								// has a explanation
								//
								fileQuestion.setTextAnswer(tmp.substring(tmp.indexOf("~"), tmp.indexOf("#", tmp
										.indexOf("~"))));
								fileQuestion
										.setExplanation(tmp.substring(tmp.indexOf("#", tmp.indexOf("~") + 1) + 1));
							} else {
								//
								// hasn't a explanation
								//
								fileQuestion.setTextAnswer(tmp.substring(tmp.indexOf("~") + 1));
							}
						}
					}
					if (tmp.indexOf("=") > 0) {
						//
						// is Correct
						//
						if (tmp.indexOf("%", tmp.indexOf("=") + 1) > 0) {
							if (tmp.indexOf("%", tmp.indexOf("%", tmp.indexOf("=") + 1)) > 0) {
								//
								// has percent
								//

								/** Add a new corrct answer */

								answers++;

								fileQuestion.setCodeAnswer(answers);

								fileQuestion.setNumberOfAnswers(answers);
								fileQuestion.setCorrect("true");
								correctAnswers++;
								fileQuestion.setNumberOfCorrect(correctAnswers);

								if (tmp.indexOf("#", tmp.indexOf("%", tmp.indexOf("%", tmp.indexOf("=") + 1) + 1)) > 0) {
									//
									// has a explanation
									//
									fileQuestion.setTextAnswer(tmp.substring(tmp.indexOf("%", tmp.indexOf("%", tmp
											.indexOf("=") + 1) + 1) + 1, tmp.indexOf("#", tmp.indexOf("%", tmp.indexOf(
											"%", tmp.indexOf("=")) + 1))));
									fileQuestion.setExplanation(tmp.substring(tmp.indexOf("#", tmp.indexOf("%", tmp
											.indexOf("%", tmp.indexOf("=")) + 1)) + 1));
								} else {
									//
									// hasn't a explanation
									//
									fileQuestion.setTextAnswer(tmp.substring(tmp.indexOf("%", tmp.indexOf("%", tmp
											.indexOf("=")) + 1)));
								}
							}
						} else {
							//
							// hasn't percent
							//

							/** Add a new correct answer */

							answers++;
							correctAnswers++;


							fileQuestion.setNumberOfAnswers ( answers );
							fileQuestion.setCodeAnswer ( answers );
							fileQuestion.setCorrect("true");
							correctAnswers++;
							fileQuestion.setNumberOfCorrect(correctAnswers);

							fileQuestion.setNumberOfCorrect(correctAnswers);

							if (tmp.indexOf("#", tmp.indexOf("=") + 1) > 0) {
								//
								// it has a explanation
								//
								fileQuestion.setTextAnswer(tmp.substring(tmp.indexOf("="), tmp.indexOf("#", tmp
										.indexOf("="))));
								fileQuestion.setExplanation(tmp.substring(tmp.indexOf("#", tmp.indexOf("="))));
							} else
								//
								// it hasn't a explanation
								//
								fileQuestion.setTextAnswer(tmp.substring(tmp.indexOf("=")));
						}
					}
				}
				if (tmp.indexOf("#") > 0) {
					if (tmp.indexOf("..") > 0 && tmp.indexOf("..") > tmp.indexOf("#")) {
						//
						// the question is a numerical type
						//
						fileQuestion.setCodeQuestion(0, numberOfQuestion * -1);
						fileQuestion.setEnunciate("Error this question is a numerical question.");
						flag = 0;
					}
				}
				if (tmp.indexOf("TRUE") > 0) {
					//
					// the question is a true/false type
					//
					if (tmp.indexOf("#") > 0 && tmp.indexOf("#") == tmp.indexOf("TRUE") + 5) {
						answers++;
						correctAnswers++;

						fileQuestion.setNumberOfAnswers(answers);
						fileQuestion.setCodeAnswer(answers);
						fileQuestion.setCorrect("true");
						correctAnswers++;
						fileQuestion.setNumberOfCorrect(correctAnswers);

						fileQuestion.setNumberOfCorrect(correctAnswers);

						if (tmp.indexOf("#", tmp.indexOf("#")) + 1 > 0) {
							fileQuestion.setCorrect("false");
							fileQuestion.setTextAnswer(tmp.substring(tmp.indexOf("#"), tmp.indexOf("#", tmp
									.indexOf("#") + 1) + 1) + 1);
							answers++;
							fileQuestion.setNumberOfAnswers(answers);
							fileQuestion.setCodeAnswer(answers);
							fileQuestion.setTextAnswer(tmp.substring(tmp.indexOf("#", tmp.indexOf("#") + 1) + 1));
							flag = 0;
						} else {
							fileQuestion.setTextAnswer(tmp.substring(tmp.indexOf("#") + 1));
						}
					}
				}
				break;
		}

		return true;
	} // processLine ( String )

} // class FromMoodleXML
