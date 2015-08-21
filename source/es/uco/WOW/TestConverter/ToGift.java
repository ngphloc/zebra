package es.uco.WOW.TestConverter;

import java.io.BufferedWriter;
import java.io.FileWriter;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import es.uco.WOW.Utils.UtilLog;

public class ToGift {

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

	protected ReadWOW3 toGift;

	public ToGift() {}

	//
	// MAIN
	//

	/** Main program entry point. */
	public boolean convert(String inputFile, String outputFile) {

		// variables
		toGift = new ReadWOW3 ( );
		XMLReader parser = null;
		boolean namespaces = DEFAULT_NAMESPACES;
		boolean namespacePrefixes = DEFAULT_NAMESPACE_PREFIXES;
		boolean validation = DEFAULT_VALIDATION;
		boolean schemaValidation = DEFAULT_SCHEMA_VALIDATION;
		boolean schemaFullChecking = DEFAULT_SCHEMA_FULL_CHECKING;
		boolean dynamicValidation = DEFAULT_DYNAMIC_VALIDATION;

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
		parser.setContentHandler(toGift);
		parser.setErrorHandler(toGift);

		try {
			parser.parse(inputFile);
		} catch (Exception e) {
			UtilLog.writeException(e);
		}

		if (makeGIFTFile(outputFile))
			return true;
		else
			return false;

	} // convert ( String , String )

	//
	// Private methods
	//

	/*
	 * NAME: makeFiles ( ) IT'S FROM: Class My. CALLED FROM: Main program CALL
	 * TO: from ReadWOW3 Class: int getNumberOfQuestions ( ) String getName ( )
	 * int getCodeQuestion ( int positionQuestion ) int getNumberOfAnswers ( int
	 * positionQuestion ) int getNumberOfCorrect ( int positionQuestion ) String
	 * getExplanation ( int positionQuestion , int positionAnswer ) String
	 * getTextAnswer ( int positionQuestion , int positionAnswer ) String
	 * getEnunciate ( int positionQuestion )
	 * 
	 * recibed: void RETURN: void FUNCTION: Write in a file why call the program. (
	 * Only Can write simple and multiple choice ).
	 */
	public boolean makeGIFTFile(String FileName) {
		BufferedWriter quest = null;
		int numberOfAnswers;
		int numberOfQuestions = toGift.getNumberOfQuestions();
		int numberOfCorrect;
		String buffer = "";
		float percent = 0;

		if (numberOfQuestions < 1 || toGift.flag < 15)
			return false;

		try {
			quest = new BufferedWriter(new FileWriter(FileName));
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
		//
		// Here we create a Questions File type GIFT
		//
		for (int positionQuestion = 0; positionQuestion <= numberOfQuestions; positionQuestion++) {
			buffer = "// Question: " + toGift.getCodeQuestion(positionQuestion) + " name: "
					+ toGift.getEnunciate(positionQuestion);

			buffer = unReplaceAmp(buffer);

			if (toGift.getImage(positionQuestion) != "") {
				buffer = "// Question: " + toGift.getCodeQuestion(positionQuestion) + " name: "
						+ toGift.getEnunciate(positionQuestion) + "has a image and is imposible export";

				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
					quest.newLine();
				} catch (Exception e) {
					UtilLog.writeException(e);
				}

				continue;
			}
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "::" + toGift.getName() + toGift.getCodeQuestion(positionQuestion) + "::"
					+ toGift.getEnunciate(positionQuestion) + "{";
			buffer = unReplaceAmp(buffer);
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			numberOfAnswers = toGift.getNumberOfAnswers(positionQuestion);
			numberOfCorrect = toGift.getNumberOfCorrect(positionQuestion);
			if ( numberOfCorrect == 1 )
				percent = 100;
			else
				percent = (float) ( 100 ) / ( float ) ( numberOfCorrect + 1);
			for (int positionAnswer = 0; positionAnswer < numberOfAnswers; positionAnswer++) {
				//
				// Here write in File the Answer and indicate if these are correct
				// or no,
				// and percent of correct.
				//
				// "~"-> incorrect
				// "="-> vorrect
				// "#"-> explanation
				// "%" + percent + "%"-> percent of correct
				//
				buffer = toGift.getCorrect(positionQuestion, positionAnswer);
				if (buffer.compareTo("true") == 0) {
					buffer = "\t=%" + percent + "%" + toGift.getTextAnswer(positionQuestion, positionAnswer);
					if (toGift.getExplanation(positionQuestion, positionAnswer) != null)
						buffer = buffer + "#" + toGift.getExplanation(positionQuestion, positionAnswer);
					buffer = unReplaceAmp(buffer);
					try {
						quest.write(buffer, 0, buffer.length());
						quest.newLine();
					} catch (Exception e) {
						UtilLog.writeException(e);
					}
				} else {
					buffer = "\t~%-" + percent + "%" + toGift.getTextAnswer(positionQuestion, positionAnswer);
					if (toGift.getExplanation(positionQuestion, positionAnswer) != null)
						buffer = buffer + "#" + toGift.getExplanation(positionQuestion, positionAnswer);
					buffer = unReplaceAmp(buffer);
					try {
						quest.write(buffer, 0, buffer.length());
						quest.newLine();
					} catch (Exception e) {
						UtilLog.writeException(e);
					}
				}
			}
			//
			// Now we Close the Question
			//
			buffer = "}";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
				quest.newLine();
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
		}
		try {
			quest.close();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}

		quest = null;

		buffer = "";

		return true;

	} // makeGIFTFile ( )

	String unReplaceAmp(String original) {
		String partAfter;
		int i = 0;

		do {
			/*
			 * find hight than ( "&" amp )
			 */
			i = original.indexOf("&amp;", i);
			if (i > -1) {
				if (i == 0)
					original = "&" + original.substring(5);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "&";
					else {
						partAfter = original.substring(i + 5);
						original = original.substring(0, i) + "&" + partAfter;
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
			 * find hight than ( "&" amp )
			 */
			i = original.indexOf("&#38;#38;", i);
			if (i > -1) {
				if (i == 0)
					original = "&" + original.substring(8);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "&";
					else {
						partAfter = original.substring(i + 8);
						original = original.substring(0, i) + "&" + partAfter;
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
			 * find hight than ( "'" apos )
			 */
			i = original.indexOf("&apos;", i);
			if (i > -1) {
				if (i == 0)
					original = "'" + original.substring(6);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "'";
					else {
						partAfter = original.substring(i + 6);
						original = original.substring(0, i) + "'" + partAfter;
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
			 * find hight than ( "'" apos )
			 */
			i = original.indexOf("&#39;", i);
			if (i > -1) {
				if (i == 0)
					original = "'" + original.substring(5);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "'";
					else {
						partAfter = original.substring(i + 5);
						original = original.substring(0, i) + "'" + partAfter;
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
			 * find hight than ( ">" gt )
			 */
			i = original.indexOf("&gt;", i);
			if (i > -1) {
				if (i == 0)
					original = ">" + original.substring(4);
				else {
					if (i == original.length())
						original = original.substring(0, i) + ">";
					else {
						partAfter = original.substring(i + 4);
						original = original.substring(0, i) + ">" + partAfter;
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
			 * find hight than ( ">" gt )
			 */
			i = original.indexOf("&#62;", i);
			if (i > -1) {
				if (i == 0)
					original = ">" + original.substring(5);
				else {
					if (i == original.length())
						original = original.substring(0, i) + ">";
					else {
						partAfter = original.substring(i + 5);
						original = original.substring(0, i) + ">" + partAfter;
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
			i = original.indexOf("&lt;", i);
			if (i > -1) {
				if (i == 0)
					original = "<" + original.substring(4);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "<";
					else {
						partAfter = original.substring(i + 4);
						original = original.substring(0, i) + "<" + partAfter;
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
			i = original.indexOf("&#38;#60;", i);
			if (i > -1) {
				if (i == 0)
					original = "<" + original.substring(9);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "<";
					else {
						partAfter = original.substring(i + 9);
						original = original.substring(0, i) + "<" + partAfter;
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
			i = original.indexOf("&iquest;", i);
			if (i > -1) {
				if (i == 0)
					original = "¿" + original.substring(8);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "¿";
					else {
						partAfter = original.substring(i + 8);
						original = original.substring(0, i) + "¿" + partAfter;
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
			 * find hight than ( "\"" quot )
			 */
			i = original.indexOf("&quot;", i);
			if (i > -1) {
				if (i == 0)
					original = "\"" + original.substring(6);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "\"";
					else {
						partAfter = original.substring(i + 6);
						original = original.substring(0, i) + "\"" + partAfter;
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
			 * find hight than ( "\"" quot )
			 */
			i = original.indexOf("&#34;", i);
			if (i > -1) {
				if (i == 0)
					original = "\"" + original.substring(5);
				else {
					if (i == original.length())
						original = original.substring(0, i) + "\"";
					else {
						partAfter = original.substring(i + 5);
						original = original.substring(0, i) + "\"" + partAfter;
					}
				}
			}

			if (i < original.length() && i > -1)
				i++;
			else
				i = -1;
		} while (!(i < 0));

		return original;

	} // unReplaceAmp ( String original );

} // class ToGift
