package es.uco.WOW.TestConverter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import es.uco.WOW.Utils.UtilLog;

public class ReadWOW3 extends DefaultHandler {

	//
	// My Data
	//

	/** situation. */
	protected int flag;

	protected int oldFlag;

	/** The FileOfQuestionsStruct element */
	protected FileOfQuestionsStruct fileQuestion;

	protected String oldAttrib;

	//
	// Constructors
	//

	/** Default constructor. */
	public ReadWOW3() {}

	// <init>( )

	//
	// ContentHandler methods
	//

	/** Start document. */
	public void startDocument() throws SAXException {

		flag = 0;
		oldFlag = 0;
		fileQuestion = new FileOfQuestionsStruct();
		oldAttrib = "";

	} // startDocument ()

	/** Start element. */
	public void startElement(String uri, String local, String raw, Attributes attrs) throws SAXException {

		switch (flag) {
			case 0:
				if (local.equals("fileOfQuestions"))
					flag = 1;
				break;
			case 1:
				//
				// fileOfQuestions
				//
				if (local.equals("concept"))
					flag = 2;
				break;
			case 2:
				//
				// concept
				//
				if (local.equals("questions"))
					flag = 3;
			case 3:
				//
				// questions
				//
				if (local.equals("question"))
					flag = 4;
				else {
					// error
				}
				break;
			case 4:
				//
				// question
				//
				if (local.equals("enunciate"))
					flag = 5;
				break;
			case 5:
				//
				// enunciate
				//
				if (local.equals("image"))
					flag = 6;
				break;
			case 6:
				//
				// image
				//
				if (local.equals("answers"))
					flag = 7;
				break;
			case 7:
				//
				// answers
				//
				if (local.equals("answer"))
					flag = 8;
				else {
					// error
				}
				break;
			case 8:
				//
				// answer
				//
				if (local.equals("textAnswer"))
					flag = 9;
				break;
			case 9:
				//
				// texAnswers
				//
				if (local.equals("correct"))
					flag = 10;
				break;
			case 10:
				//
				// correct
				//
				if (local.equals("explanation"))
					flag = 11;
				else
					UtilLog.toLog("Error al pasar a Explanation.");
				break;
			case 11:
				//
				// explanation
				//
				if (local.equals("answer"))
					flag = 8;
				if (local.equals("irtParameters"))
					flag = 12;
				break;
			case 12:
				//
				// irtParameters
				//
				if (local.equals("difficulty"))
					flag = 13;
				break;
			case 13:
				//
				// difficulty
				//
				if (local.equals("discrimination"))
					flag = 14;
				break;
			case 14:
				//
				// discrimination
				//
				if (local.equals("guessing"))
					flag = 15;
				break;
			case 15:
				//
				// guessing
				//
				if (local.equals("statisticParameters"))
					flag = 16;
				break;
			case 16:
				//
				// statisticParameters
				//
				if (local.equals("exhibitionRate"))
					flag = 17;
				break;
			case 17:
				//
				// exhibitionRate
				//
				if (local.equals("answerTime"))
					flag = 18;
				break;
			case 18:
				//
				// answerTime
				//
				if (local.equals("successRate"))
					flag = 19;
				break;
			case 19:
				//
				// successRate
				//
				if (local.equals("numberOfUses"))
					flag = 20;
				break;
			case 20:
				//
				// numberOfUses
				//
				if (local.equals("numberOfSuccesses"))
					flag = 21;
				break;
			case 21:
				//
				// numberOfSuccesses
				//
				if (local.equals("question"))
					flag = 4;
				if (local.equals("testFiles"))
					flag = 22;
				break;
			case 22:
				//
				// testFiles
				//
				if (local.equals("question"))
					flag = 4;
				if (local.equals("classicTest"))
					flag = 23;
				if (local.equals("adaptiveTest"))
					flag = 25;
				break;
			case 23:
				//
				// classicTest
				//
				if (local.equals("question"))
					flag = 4;
				if (local.equals("classicTestFilePath"))
					flag = 24;
				if (local.equals("adaptiveTest"))
					flag = 25;
				break;
			case 24:
				//
				// classicTestFilePath
				//
				if (local.equals("classicTestFilePath"))
					flag = 24;
				if (local.equals("adaptiveTest"))
					flag = 25;
				if (local.equals("question"))
					flag = 4;
				break;
			case 25:
				//
				// adaptiveTest
				//
				if (local.equals("adaptiveTestFilePath"))
					flag = 26;
				if (local.equals("question"))
					flag = 4;
				break;
			case 26:
				//
				// adaptiveTestFilePath
				//
				if (local.equals("question"))
					flag = 4;
				if (local.equals("adaptiveTestFilePath"))
					flag = 26;
				break;
		}
		if (attrs != null) {
			int attrCount = attrs.getLength();
			String attrName = null;
			String attrValue = null;
			for (int i = 0; i < attrCount; i++) {
				attrName = attrs.getQName(i);
				attrValue = attrs.getValue(i);
				attrValue = replaceAmp(attrValue);
				switch (flag) {
					case 1:
						// Attributes of FileofQuestions
						if (attrName.equals("fileName")) {
							if (attrName == oldAttrib)
								attrValue = fileQuestion.getFileName() + attrValue;
							oldAttrib = attrName;
							// Take file's name.
							fileQuestion.setFileName(attrValue);
						}
						if (attrName.equals("name")) {
							if (attrName == oldAttrib)
								attrValue = fileQuestion.getName() + attrValue;
							oldAttrib = attrName;
							// Take name of file wihtout extension.
							fileQuestion.setName(attrValue);
						}
						if (attrName.equals("course")) {
							if (attrName == oldAttrib)
								attrValue = fileQuestion.getCourse() + attrValue;
							oldAttrib = attrName;
							// Take course.
							fileQuestion.setCourse(attrValue);
						}
						if (attrName.equals("path")) {
							if (attrName == oldAttrib)
								attrValue = fileQuestion.getPath() + attrValue;
							oldAttrib = attrName;
							// Take path.
							fileQuestion.setPath(attrValue);
						}
						if (attrName.equals("numberOfAnswersForFile")) {
							if (attrName == oldAttrib)
								attrValue = fileQuestion.getNumberOfAnswersForFile() + attrValue;
							oldAttrib = attrName;
							// Take number of file Items used.
							fileQuestion.setNumberOfAnswersForFile(Integer.parseInt(attrValue));
						}
						break;
					case 2:
						// Attributes of Concept
						if (attrName.equals("value")) {
							if (attrName == oldAttrib)
								attrValue = fileQuestion.getConcept() + attrValue;
							oldAttrib = attrName;
							// Take concept value.
							fileQuestion.setConcept(attrValue);
						}
						break;
					case 4:
						// Attributes of Question
						if (attrName.equals("codeQuestion")) {
							// Take correct Answer of a question.
							/* val = Integer.parseInt (s); */
							if (attrName == oldAttrib)
								attrValue = fileQuestion.getCodeQuestion() + attrValue;
							oldAttrib = attrName;
							fileQuestion.setCodeQuestion(Integer.parseInt(attrValue));
						}
						break;
					case 7:
						// Attributes of Answers
						if (attrName.equals("numberOfCorrect")) {
							// Take correct Answer of a question.
							/* val = Integer.parseInt (s); */
							if (attrName == oldAttrib)
								attrValue = fileQuestion.getNumberOfCorrect() + attrValue;
							oldAttrib = attrName;
							fileQuestion.setNumberOfCorrect(Integer.parseInt(attrValue));
						}
						if (attrName.equals("numberOfAnswers")) {
							// Take Answer of a question.
							/* val = Integer.parseInt (s); */
							if (attrName == oldAttrib)
								attrValue = fileQuestion.getNumberOfAnswers() + attrValue;
							oldAttrib = attrName;
							fileQuestion.setNumberOfAnswers(Integer.parseInt(attrValue));
						}
						break;
					case 8:
						// Attributes of Answer
						if (attrName.equals("codeAnswer"))
							// Take Code of Answer.
							/* val = Integer.parseInt (s); */
							oldAttrib = attrName;
						fileQuestion.setCodeAnswer(Integer.parseInt(attrValue));
						break;
					default:
						/** An error * */
						break;
				}
			}
		}
	} // startElement (String,String,StringAttributes)

	public void endDocument() {}

	/** Characters. */
	public void characters(char characters[], int start, int length) throws SAXException {

		String value = "";

		if (length > 0) {
			for (int i = start; i < start + length; i++) {
				if (characters[i] != '\n')
					value = value + characters[i];
			}

			value = replaceAmp(value);

			switch (flag) {
				case 5:
					/** enumciate * */
					if (flag == oldFlag)
						value = fileQuestion.getEnunciate() + value;
					oldFlag = flag;
					fileQuestion.setEnunciate(value);

					break;
				case 6:
					/** image * */
					if (flag == oldFlag)
						value = fileQuestion.getImage() + value;
					oldFlag = flag;
					fileQuestion.setImage(value);
					break;
				case 9:
					/** textAnswer * */
					if (flag == oldFlag)
						value = fileQuestion.getTextAnswer() + value;
					oldFlag = flag;
					fileQuestion.setTextAnswer(value);
					break;
				case 10:
					/** ecorrect * */
					if (flag == oldFlag)
						value = fileQuestion.getCorrect() + value;
					oldFlag = flag;
					fileQuestion.setCorrect(value);

					break;
				case 11:
					/** explation * */
					if (flag == oldFlag)
						value = fileQuestion.getExplanation() + value;
					oldFlag = flag;
					fileQuestion.setExplanation(value);
					break;
				case 13:
					/** difficulty * */
					if (flag == oldFlag)
						value = fileQuestion.getDifficulty() + value;
					oldFlag = flag;
					fileQuestion.setDifficulty(Float.parseFloat(value));
					break;
				case 14:
					/** discrimination * */
					if (flag == oldFlag)
						value = fileQuestion.getDiscrimination() + value;
					oldFlag = flag;
					fileQuestion.setDiscrimination(Float.parseFloat(value));
					break;
				case 15:
					/** guessing * */
					if (flag == oldFlag)
						value = fileQuestion.getGuessing() + value;
					oldFlag = flag;
					fileQuestion.setGuessing(Float.parseFloat(value));
					break;
				case 17:
					/** exhibitionRate * */
					if (flag == oldFlag)
						value = fileQuestion.getGuessing() + value;
					oldFlag = flag;
					fileQuestion.setExhibitionRate(Float.parseFloat(value));
					break;
				case 18:
					/** answerTime * */
					if (flag == oldFlag)
						value = fileQuestion.getAnswerTime() + value;
					oldFlag = flag;
					fileQuestion.setAnswerTime(Float.parseFloat(value));
					break;
				case 19:
					/** successRate * */
					if (flag == oldFlag)
						value = fileQuestion.getSuccessRate() + value;
					oldFlag = flag;
					fileQuestion.setSuccessRate(Float.parseFloat(value));
					break;
				case 20:
					/** numberOfUses * */
					if (flag == oldFlag)
						value = fileQuestion.getNumberOfUses() + value;
					oldFlag = flag;
					fileQuestion.setNumberOfUses(Integer.parseInt(value));
					break;
				case 21:
					/** numberOfSuccesses * */
					if (flag == oldFlag)
						value = fileQuestion.getNumberOfSuccesses() + value;
					oldFlag = flag;
					fileQuestion.setNumberOfSuccesses(Integer.parseInt(value));

					break;
				case 24:
					/** numberOfSuccesses * */
					fileQuestion.setClassic(value);
					break;
				case 26:
					/** numberOfSuccesses * */
					fileQuestion.setAdaptative(value);
					break;
				default:
					/** An error * */
					break;
			}
		}
	} // characters(char[],int,int);

	public int getNumberOfQuestions() {
		return fileQuestion.getLastQuestion();
	}

	public String getConcept() {
		return fileQuestion.getConcept();
	}

	public String getName() {
		return fileQuestion.getName();
	}

	public int getNumberOfCorrect(int positionQuestion) {
		return fileQuestion.getNumberOfCorrect(positionQuestion);
	}

	public int getCodeQuestion(int positionQuestion) {
		return fileQuestion.getCodeQuestion(positionQuestion);
	}

	public float getDifficulty(int positionQuestion) {
		return fileQuestion.getDifficulty(positionQuestion);
	}

	public float getGuessing(int positionQuestion) {
		return fileQuestion.getGuessing(positionQuestion);
	}

	public float getDiscrimination(int positionQuestion) {
		return fileQuestion.getDiscrimination(positionQuestion);
	}

	public String getEnunciate(int positionQuestion) {
		return fileQuestion.getEnunciate(positionQuestion);
	}

	public String getImage(int positionQuestion) {
		return fileQuestion.getImage(positionQuestion);
	}

	public int getNumberOfAnswers(int positionQuestion) {
		return fileQuestion.getNumberOfAnswers(positionQuestion);
	}

	public int getCodeAnswer(int positionQuestion, int positionAnswer) {
		return fileQuestion.getCodeAnswer(0, positionQuestion, positionAnswer);
	}

	public String getExplanation(int positionAnswer, int positionQuestion) {
		return fileQuestion.getExplanation(0, positionQuestion, positionAnswer);
	}

	public String getTextAnswer(int positionQuestion, int positionAnswer) {
		return fileQuestion.getTextAnswer(0, positionQuestion, positionAnswer);
	}

	public String getCorrect(int positionQuestion, int positionAnswer) {
		return fileQuestion.getCorrect(0, positionQuestion, positionAnswer);
	}

	public String getAdaptative(int positionQuestion) {
		return fileQuestion.getAdaptative(positionQuestion);
	}

	public String getPath() {
		return fileQuestion.getPath();
	}

	//
	// ErrorHandler methods
	//

	/** Warning. */
	public void warning(SAXParseException ex) throws SAXException {
		printError("Warning", ex);
	} // warning(SAXParseException)

	/** Error. */
	public void error(SAXParseException ex) throws SAXException {
		printError("Error", ex);
	} // error(SAXParseException)

	/** Fatal error. */
	public void fatalError(SAXParseException ex) throws SAXException {
		printError("Fatal Error", ex);
		// throw ex;
	} // fatalError(SAXParseException)

	//
	// Protected methods
	//

	/** Prints the error message. */
	protected void printError(String type, SAXParseException ex) {
		UtilLog.toLog("Type="+type);
		UtilLog.writeException(ex);
	}

	String replaceAmp(String original) {
		String partAfter;
		int i = 0;

		do {
			i = original.indexOf("&", i);
			if (i > -1) {
				/*
				 * lt "&#38;#60;" gt "&#62;" amp "&#38;#38;" apos "&#39;" quot
				 * "&#34;" aacute CDATA "&#225;" á eacute CDATA "&#233;" é iacute
				 * CDATA "&#237;" í oacute CDATA "&#243;" ó uacute CDATA "&#250;" ú
				 * Aacute CDATA "&#193;" Á Eacute CDATA "&#201;" É ENTITY Iacute
				 * CDATA "&#205;" Í Oacute CDATA "&#211;" Ó Uacute CDATA "&#218;" Ú
				 */
				if (original.indexOf("&amp;", i) < 0 && original.indexOf("&#38;#38;", i) < 0
						&& original.indexOf("&apos;", i) < 0 && original.indexOf("&#39;", i) < 0
						&& original.indexOf("&gt;", i) < 0 && original.indexOf("&#62;", i) < 0
						&& original.indexOf("&lt;", i) < 0 && original.indexOf("&#38;#60;", i) < 0
						&& original.indexOf("&iquest;", i) < 0 && original.indexOf("&quot;", i) < 0
						&& original.indexOf("&#34;", i) < 0 && original.indexOf("&aacute;", i) < 0
						&& original.indexOf("&#225;", i) < 0 && original.indexOf("&eacute;", i) < 0
						&& original.indexOf("&#233;", i) < 0 && original.indexOf("&iacute;", i) < 0
						&& original.indexOf("&#218;", i) < 0 && original.indexOf("&oacute;", i) < 0
						&& original.indexOf("&#2243;", i) < 0 && original.indexOf("&uacute;", i) < 0
						&& original.indexOf("&#250;", i) < 0 && original.indexOf("&Aacute;", i) < 0
						&& original.indexOf("&#193;", i) < 0 && original.indexOf("&Eacute;", i) < 0
						&& original.indexOf("&#201;", i) < 0 && original.indexOf("&Iacute;", i) < 0
						&& original.indexOf("&#205;", i) < 0 && original.indexOf("&Oacute;", i) < 0
						&& original.indexOf("&#211;", i) < 0 && original.indexOf("&Uacute;", i) < 0
						&& original.indexOf("&#218;", i) < 0) {
					partAfter = original.substring(i + 1);
					original = original.substring(0, i) + "&amp;" + partAfter;
				}
				i++;
			}
		} while (!(i < 0));
		i = 0;
		do {
			/*
			 * find hight than ( ">" gt )
			 */
			i = original.indexOf(">", i);
			if (i > -1) {
				if (i == 0)
					original = "&gt;" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "&gt;";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "&gt;" + partAfter;
					}
				}
			}

			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));
		i = 0;
		do {
			/*
			 * find hight than ( "<" lt )
			 */
			i = original.indexOf("<", i);
			if (i > -1) {
				if (i == 0)
					original = "&lt;" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "&lt;";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "&lt;" + partAfter;
					}
				}
			}
			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));
		i = 0;
		do {
			/*
			 * find hight than ( "¿" iquest )
			 */
			i = original.indexOf("¿", i);
			if (i > -1) {
				if (i == 0)
					original = "" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "" + partAfter;
					}
				}
			}

			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));
		/*
		 * ENTITY aacute CDATA "&#225;" á
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "á" aacute )
			 */
			i = original.indexOf("á", i);
			if (i > -1) {
				if (i == 0)
					original = "a" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "a";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "a" + partAfter;
					}
				}
			}
			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));
		/*
		 * ENTITY eacute CDATA "&#233;" é
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "é" eacute )
			 */
			i = original.indexOf("é", i);
			if (i > -1) {
				if (i == 0)
					original = "e" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "e";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "e" + partAfter;
					}
				}
			}
			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));
		/*
		 * ENTITY iacute CDATA "&#237;" í
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "í" iacute )
			 */
			i = original.indexOf("í", i);
			if (i > -1) {
				if (i == 0)
					original = "i" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "i";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "i" + partAfter;
					}
				}
			}
			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));

		/*
		 * ENTITY oacute CDATA "&#243;" ó
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "ó" oacute )
			 */
			i = original.indexOf("ó", i);
			if (i > -1) {
				if (i == 0)
					original = "o" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "o";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "o" + partAfter;
					}
				}
			}
			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));
		/*
		 * ENTITY uacute CDATA "&#250;" ú
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "ú" aacute )
			 */
			i = original.indexOf("ú", i);
			if (i > -1) {
				if (i == 0)
					original = "u" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "u";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "u" + partAfter;
					}
				}
			}
			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));
		/*
		 * ENTITY Aacute CDATA "&#193;" Á
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "Á" Aacute )
			 */
			i = original.indexOf("Á", i);
			if (i > -1) {
				if (i == 0)
					original = "A" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "A";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "A" + partAfter;
					}
				}
			}
			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));
		/*
		 * ENTITY Eacute CDATA "&#201;" É
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "É" Eacute )
			 */
			i = original.indexOf("É", i);
			if (i > -1) {
				if (i == 0)
					original = "E" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "E";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "E" + partAfter;
					}
				}
			}
			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));
		/*
		 * ENTITY Iacute CDATA "&#205;" Í
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "Í" aacute )
			 */
			i = original.indexOf("Í", i);
			if (i > -1) {
				if (i == 0)
					original = "I" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "I";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "I" + partAfter;
					}
				}
			}
			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));
		/*
		 * ENTITY Oacute CDATA "&#211;" Ó
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "Ó" aacute )
			 */
			i = original.indexOf("Ó", i);
			if (i > -1) {
				if (i == 0)
					original = "O" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "O";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "O" + partAfter;
					}
				}
			}
			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));
		/*
		 * ENTITY Uacute CDATA "&#218;" Ú
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "Ú" Uacute )
			 */
			i = original.indexOf("Ú", i);
			if (i > -1) {
				if (i == 0)
					original = "U" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "U";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "U" + partAfter;
					}
				}
			}
			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));

		do {
			/*
			 * find hight than ( "ñ" )
			 */
			i = original.indexOf("ñ", i);
			if (i > -1) {
				if (i == 0)
					original = "gn" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "gn";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "gn" + partAfter;
					}
				}
			}
			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));

		do {
			/*
			 * find hight than ( "Ñ" )
			 */
			i = original.indexOf("Ñ", i);
			if (i > -1) {
				if (i == 0)
					original = "GN" + original.substring(1);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "GN";
					else {
						partAfter = original.substring(i + 1);
						original = original.substring(0, i) + "GN" + partAfter;
					}
				}
			}
			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));

		return original;
	} // replaceAmp ( String original );

} // class ReadWOW3
