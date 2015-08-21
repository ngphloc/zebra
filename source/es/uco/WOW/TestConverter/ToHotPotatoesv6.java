package es.uco.WOW.TestConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;

import vn.spring.WOW.WOWStatic;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import es.uco.WOW.Utils.UtilLog;

public class ToHotPotatoesv6 {

	// feature ids

	/** Namespaces feature id (http://xml.org/sax/features/namespaces). */
	protected final String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";

	/**
	 * Namespace prefixes feature id
	 * (http://xml.org/sax/features/namespace-prefixes).
	 */
	protected final String NAMESPACE_PREFIXES_FEATURE_ID = "http://xml.org/sax/features/namespace-prefixes";

	/** Validation feature id (http://xml.org/sax/features/validation). */
	protected final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";

	/**
	 * Schema validation feature id
	 * (http://apache.org/xml/features/validation/schema).
	 */
	protected final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";

	/**
	 * Schema full checking feature id
	 * (http://apache.org/xml/features/validation/schema-full-checking).
	 */
	protected final String SCHEMA_FULL_CHECKING_FEATURE_ID = "http://apache.org/xml/features/validation/schema-full-checking";

	/**
	 * Dynamic validation feature id
	 * (http://apache.org/xml/features/validation/dynamic).
	 */
	protected final String DYNAMIC_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/dynamic";

	// default settings

	/** Default parser name. */
	protected final String DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";

	/** Default repetition (1). */
	protected final int DEFAULT_REPETITION = 1;

	/** Default namespaces support (true). */
	protected final boolean DEFAULT_NAMESPACES = true;

	/** Default namespace prefixes (false). */
	protected final boolean DEFAULT_NAMESPACE_PREFIXES = false;

	/** Default validation support (false). */
	protected final boolean DEFAULT_VALIDATION = false;

	/** Default Schema validation support (false). */
	protected final boolean DEFAULT_SCHEMA_VALIDATION = false;

	/** Default Schema full checking support (false). */
	protected final boolean DEFAULT_SCHEMA_FULL_CHECKING = false;

	/** Default dynamic validation support (false). */
	protected final boolean DEFAULT_DYNAMIC_VALIDATION = false;

	/** Default memory usage report (false). */
	protected final boolean DEFAULT_MEMORY_USAGE = false;

	/** Default "tagginess" report (false). */
	protected final boolean DEFAULT_TAGGINESS = false;

	//
	// My Data
	//

	protected String inputPath;

	protected String outputPath;
	
	protected String inputsubPath;
	
	protected ReadWOW3 toHotPotatoesv6;

	public ToHotPotatoesv6() {}

	//
	// MAIN
	//

	/** Main program entry point. */
	public boolean convert(String inputFile, String outputFile) {

		// variables
		toHotPotatoesv6 = new ReadWOW3();
		XMLReader parser = null;
		boolean namespaces = DEFAULT_NAMESPACES;
		boolean namespacePrefixes = DEFAULT_NAMESPACE_PREFIXES;
		boolean validation = DEFAULT_VALIDATION;
		boolean schemaValidation = DEFAULT_SCHEMA_VALIDATION;
		boolean schemaFullChecking = DEFAULT_SCHEMA_FULL_CHECKING;
		boolean dynamicValidation = DEFAULT_DYNAMIC_VALIDATION;

		if (! extractPath ( inputFile , outputFile) )
			return false;
		// use default parser?
		if (parser == null) {

			// create parser
			try {
				parser = XMLReaderFactory.createXMLReader(DEFAULT_PARSER_NAME);
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
		}

		// set parser features
		try {
			parser.setFeature(NAMESPACES_FEATURE_ID, namespaces);
		} catch (SAXException e) {
			UtilLog.writeException(e);
		}
		try {
			parser.setFeature(NAMESPACE_PREFIXES_FEATURE_ID, namespacePrefixes);
		} catch (SAXException e) {
			UtilLog.writeException(e);
		}
		try {
			parser.setFeature(VALIDATION_FEATURE_ID, validation);
		} catch (SAXException e) {
			UtilLog.writeException(e);
		}
		try {
			parser.setFeature(SCHEMA_VALIDATION_FEATURE_ID, schemaValidation);
		} catch (SAXNotRecognizedException e) {
			UtilLog.writeException(e);

		} catch (SAXNotSupportedException e) {
			UtilLog.writeException(e);
		}
		try {
			parser.setFeature(SCHEMA_FULL_CHECKING_FEATURE_ID, schemaFullChecking);
		} catch (SAXNotRecognizedException e) {
			UtilLog.writeException(e);

		} catch (SAXNotSupportedException e) {
			UtilLog.writeException(e);
		}
		try {
			parser.setFeature(DYNAMIC_VALIDATION_FEATURE_ID, dynamicValidation);
		} catch (SAXNotRecognizedException e) {
			UtilLog.writeException(e);

		} catch (SAXNotSupportedException e) {
			UtilLog.writeException(e);
		}

		// parse file
		parser.setContentHandler(toHotPotatoesv6);
		parser.setErrorHandler(toHotPotatoesv6);


		try {
			parser.parse(inputFile);
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		if (makeHotPotatoesv6(outputFile))
			return true;
		else
			return false;

	} // convert ( String , String )


	/*
	 * NAME: makeMoodleXML ( ) IT'S FROM: Class ToHotPotatoesv6. CALLED FROM:
	 * Main program CALL TO: from ReadWOW3 Class: int getNumberOfQuestions ( )
	 * String getName ( ) int getCodeQuestion ( int positionQuestion ) int
	 * getNumberOfAnswers ( int positionQuestion ) int getNumberOfCorrect ( int
	 * positionQuestion ) String getExplanation ( int positionQuestion , int
	 * positionAnswer ) String getTextAnswer ( int positionQuestion , int
	 * positionAnswer ) String getEnunciate ( int positionQuestion )
	 * 
	 * recibed: void RETURN: boolean FUNCTION: Write in a file why call the
	 * program. ( Only Can write simple and multiple choice ).
	 */
	public boolean makeHotPotatoesv6(String FileName) {
		BufferedWriter quest = null;
		int numberOfAnswers;
		int numberOfQuestions = toHotPotatoesv6.getNumberOfQuestions();
		int numberOfCorrect;
		String buffer = "";
		int isTimer = 0;
		int timer = 60;
		String image = null;

		if (numberOfQuestions < 1 || toHotPotatoesv6.flag < 15)
			return false;

		try {
			quest = new BufferedWriter(new FileWriter(FileName));
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
		//
		// Here we create a Questions File type hotpotatoes v.6
		//

		buffer = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "<hotpot-jquiz-file>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t<rdf:Description rdf:about=\"\">";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<dc:creator>EXPORT WOW3!</dc:creator>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<dc:title>" + toHotPotatoesv6.replaceAmp((toHotPotatoesv6.getName())) + "</dc:title>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t</rdf:Description>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t</rdf:RDF>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t<version>6</version>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t<data>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t<title>" + toHotPotatoesv6.replaceAmp(toHotPotatoesv6.getName()) + "</title>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t<timer>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<seconds>" + timer + "</seconds>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<include-timer>" + isTimer + "</include-timer>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t</timer>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t<reading>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<include-reading>" + "0" + "</include-reading>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<reading-title></reading-title>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<reading-text></reading-text>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t</reading>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t<questions>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		for (int positionQuestion = 0; positionQuestion <= numberOfQuestions; positionQuestion++) {
			numberOfAnswers = toHotPotatoesv6.getNumberOfAnswers(positionQuestion);
			numberOfCorrect = toHotPotatoesv6.getNumberOfCorrect(positionQuestion);

			buffer = "\t\t\t<question-record>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t\t<question>" + toHotPotatoesv6.replaceAmp(toHotPotatoesv6.getEnunciate(positionQuestion));
			image = getImage(positionQuestion);
			
			if (image != "")
			{
				buffer = buffer + "&lt;img src=&quot;"
	        	+ "images" + getImage ( positionQuestion )
	        	+ "&quot;&gt;";
			}

			buffer = buffer + "</question>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t\t<clue></clue>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t\t<category></category>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t\t<weighting>100</weighting>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t\t<fixed>0</fixed>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t\t<question-type>4</question-type>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t\t\t<answers>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			for (int positionAnswer = 0; positionAnswer < numberOfAnswers; positionAnswer++) {
				buffer = "\t\t\t\t\t<answer>";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.writeException(e);
				}
				buffer = "\t\t\t\t\t\t<text>"
						+ toHotPotatoesv6.replaceAmp(toHotPotatoesv6.getTextAnswer(positionQuestion, positionAnswer)) + "</text>";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.writeException(e);
				}
				buffer = "\t\t\t\t\t\t<feedback>";
				if (toHotPotatoesv6.getExplanation(positionQuestion, positionAnswer) != null)
					buffer = buffer + toHotPotatoesv6.getExplanation(positionQuestion, positionAnswer);
				buffer = buffer + "</feedback>";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.writeException(e);
				}
				buffer = "\t\t\t\t\t\t<correct>";
				buffer = toHotPotatoesv6.getCorrect(positionQuestion, positionAnswer);
				if (buffer.compareTo("true") == 0)
					buffer = "\t\t\t\t\t\t<correct>" + "1";
				else
					buffer = "\t\t\t\t\t\t<correct>" + "0";
				buffer = buffer + "</correct>";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.writeException(e);
				}
				buffer = toHotPotatoesv6.getCorrect(positionQuestion, positionAnswer);
				if (buffer.compareTo("true") == 0)
					buffer = "\t\t\t\t\t\t<percent-correct>100</percent-correct>";
				else
					buffer = "\t\t\t\t\t\t<percent-correct>0</percent-correct>";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.writeException(e);
				}
				buffer = "\t\t\t\t\t\t<include-in-mc-options>1</include-in-mc-options>";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.writeException(e);
				}
				if (numberOfCorrect > 1)
				{
					buffer = "\t\t\t\t\t\t<must-select-all>1</must-select-all>";
					try {
						quest.write(buffer, 0, buffer.length());
						quest.newLine();
					} catch (Exception e) {
						UtilLog.writeException(e);
					}
				}
				buffer = "\t\t\t\t\t</answer>";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.writeException(e);
				}
			}
			buffer = "\t\t\t\t</answers>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t\t\t<must-select-all>1</must-select-all>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t</question-record>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
		}
		buffer = "\t\t</questions>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t</data>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t<hotpot-config-file>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t<jquiz>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<exercise-subtitle>Quiz</exercise-subtitle>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<instructions></instructions>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<guess-correct>Correct!</guess-correct>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<partly-incorrect>Your answer is partly wrong:</partly-incorrect>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<enter-a-guess>Please enter a guess.</enter-a-guess>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<next-correct-letter>Next correct letter in the answer:</next-correct-letter>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<correct-answers>Correct answers:</correct-answers>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<show-answer-caption>Show answer</show-answer-caption>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<include-hint>1</include-hint>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<include-show-answer>1</include-show-answer>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<next-ex-url>nextpage.htm</next-ex-url>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<send-email>0</send-email>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<continuous-scoring>1</continuous-scoring>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<show-correct-first-time>1</show-correct-first-time>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<shuffle-questions>0</shuffle-questions>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<shuffle-answers>0</shuffle-answers>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<show-limited-questions>0</show-limited-questions>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<questions-to-show>2</questions-to-show>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<short-answer-tries-on-hybrid-q>2</short-answer-tries-on-hybrid-q>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<separate-javascript-file>0</separate-javascript-file>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<case-sensitive>0</case-sensitive>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<include-keypad>0</include-keypad>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t</jquiz>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t<global>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<include-keypad>0</include-keypad>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<your-score-is>Your score is</your-score-is>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<correct-indicator>:-)</correct-indicator>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<incorrect-indicator>X</incorrect-indicator>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<correct-first-time>Questions answered correctly first time:</correct-first-time>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<keypad-characters></keypad-characters>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<times-up>Your time is over!</times-up>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<next-ex-caption>=&amp;#x003E;</next-ex-caption>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<back-caption>&amp;#x003C;=</back-caption>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<contents-caption>Index</contents-caption>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<times-up>Your time is over!</times-up>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<include-next-ex>1</include-next-ex>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<include-contents>1</include-contents>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<include-back>0</include-back>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<contents-url>contents.htm</contents-url>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<graphic-url></graphic-url>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<font-face>Geneva,Arial,sans-serif</font-face>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<font-size>small</font-size>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<page-bg-color>#C0C0C0</page-bg-color>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<title-color>#000000</title-color>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<ex-bg-color>#FFFFFF</ex-bg-color>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<text-color>#000000</text-color>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<link-color>#0000FF</link-color>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<vlink-color>#0000CC</vlink-color>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<nav-bar-color>#000000</nav-bar-color>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<formmail-url>http://yourserver.com/cgi-bin/FormMail.pl</formmail-url>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<email></email>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<name-please>Please enter your name:</name-please>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<user-string-1>one</user-string-1>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<user-string-2>two</user-string-2>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<user-string-3>three</user-string-3>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<header-code></header-code>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<correct-first-time>Questions answered correctly first time:</correct-first-time>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<check-caption>Check</check-caption>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<ok-caption>OK</ok-caption>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<next-q-caption>=&amp;#x003E;</next-q-caption>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<last-q-caption>&amp;#x003C;=</last-q-caption>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<hint-caption>Hint</hint-caption>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<show-all-questions-caption>Show all questions</show-all-questions-caption>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<show-one-by-one-caption>Show questions one by one</show-one-by-one-caption>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<show-also-correct>1</show-also-correct>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t\t\t<process-for-rtl>0</process-for-rtl>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		//
		// Now we Close the Question
		//
		buffer = "\t\t</global>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "\t</hotpot-config-file>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "</hotpot-jquiz-file>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		try {
			quest.close();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}

		quest = null;

		buffer = "";

		return true;

	} // makeHotPotatoesv6 ( )

	public String getImage(int positionQuestion) {
		String image = "";

		String imageName = "";
		int position = 0;

		File in = null;
		File out = null;
		byte[] buf = new byte[1024];
		int i = 0;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		if (toHotPotatoesv6.getImage(positionQuestion) != "") {
			image = toHotPotatoesv6.getImage(positionQuestion);

			imageName = "";
			position = image.lastIndexOf("/");
			if (position < 0)
			{
				position = image.lastIndexOf("\\");
				if (position < 0)
					position = 0;
			}
			
			imageName = image.substring( position );

			in = new File(inputsubPath + image);
			out = new File( outputPath + "\\images" + imageName);
			try {
				fis = new FileInputStream(in);
				fos = new FileOutputStream(out);
			} catch (Exception e) {
				UtilLog.writeException(e);
			}

			try {
				while ((i = fis.read(buf)) != -1) {
					fos.write(buf, 0, i);
				}
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			try {
				fis.close();
				fos.close();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
		}

		 
		return imageName;
	}


	public boolean extractPath (String input, String output)
	{
		File p = null;
		int i = -1;
		
		String token = WOWStatic.config.Get("XMLROOT") + File.separator + WOWStatic.config.Get("itemspath");

		try{
			p = new File(output);
			outputPath = p.getPath();
			// We find the path of output file without Name
			i = output.lastIndexOf ( "\\" );
			if ( i < 0 ) {
				i = output.lastIndexOf ( "/" );
				if ( i < 0 ) {
					i = 0;
				}
			}
			outputPath = output.substring (0, i) + File.separator;

			p = new File(input);
			inputPath = p.getPath();
			// We find the path of input file without Name
			i = inputPath.lastIndexOf ( "\\" );
			if ( i < 0 ) {
				i = inputPath.lastIndexOf ( "/" );
				if ( i < 0 ) {
					i = 0;
				}
			}
			inputPath = inputPath.substring (0, i );

			// We find the path of input file without Name in WOW system.
			inputsubPath = inputPath.substring ( 0, inputPath.indexOf ( token ) + token.length ( ) );

		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
		return true;
	}

} // class ToHotPotatoesv6