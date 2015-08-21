package es.uco.WOW.TestConverter;

import java.io.BufferedWriter;
import java.io.FileWriter;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import es.uco.WOW.Utils.UtilLog;

public class ToWebCT {

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

	protected ReadWOW3 toWebCT;

	public ToWebCT() {}

	//
	// MAIN
	//

	/** Main program entry point. */
	public boolean convert(String inputFile, String outputFile) {

		// variables
		toWebCT = new ReadWOW3();
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
		parser.setContentHandler(toWebCT);
		parser.setErrorHandler(toWebCT);

		// parse file
		parser.setContentHandler(toWebCT);
		parser.setErrorHandler(toWebCT);

		try {
			parser.parse(inputFile);
		} catch (SAXParseException e) {
			// ignore
		} catch (Exception e) {
			UtilLog.writeException(e);
		}

		return makeWebCTFile(outputFile);

	} // convert ( String , String )

	//
	// Private methods
	//


	/*
	 * NAME: makeFiles ( ) IT'S FROM: Class ToWebCT. CALLED FROM: Main program
	 * CALL TO: from ReadWOW3 Class: int getNumberOfQuestions ( ) String getName ( )
	 * int getCodeQuestion ( int positionQuestion ) int getNumberOfAnswers ( int
	 * positionQuestion ) int getNumberOfCorrect ( int positionQuestion ) String
	 * getExplanation ( int positionQuestion , int positionAnswer ) String
	 * getTextAnswer ( int positionQuestion , int positionAnswer ) String
	 * getEnunciate ( int positionQuestion )
	 * 
	 * recibed: String FileName RETURN: void FUNCTION: Write in a file why call
	 * the program.
	 */
	public boolean makeWebCTFile(String FileName) {
		BufferedWriter quest = null;
		int numberOfAnswers;
		int numberOfQuestions = toWebCT.getNumberOfQuestions();
		int numberOfCorrect;
		String buffer = "";

		if (numberOfQuestions < 1 || toWebCT.flag < 15)
			return false;

		try {
			quest = new BufferedWriter(new FileWriter(FileName));
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
		//
		// Here we create a Questions File type WebCT
		//
		for (int positionQuestion = 0; positionQuestion <= numberOfQuestions; positionQuestion++) {
			if (toWebCT.getImage(positionQuestion) == null)
				continue;
			numberOfCorrect = toWebCT.getNumberOfCorrect(positionQuestion);
			if (numberOfCorrect > 1)
				buffer = ":TYPE:MC:N:0:C";
			else
				buffer = ":TYPE:MC:1:0:C";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}
			buffer = ":TITLE:WOW!Export" + toWebCT.getName() + " (" + toWebCT.getCodeQuestion(positionQuestion)
					+ ")";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}
			buffer = ":CAT:WOW!Export" + toWebCT.getName();
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}
			buffer = ":QUESTION:H";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}
			buffer = toWebCT.getEnunciate(positionQuestion);
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			numberOfAnswers = toWebCT.getNumberOfAnswers(positionQuestion);
			for (int positionAnswer = 0; positionAnswer < numberOfAnswers; positionAnswer++) {
				
				buffer = toWebCT.getCorrect(positionQuestion, positionAnswer);
				if (buffer.compareTo("true") == 0 )
						buffer = "" + 100;
				else
					buffer = "0";
				buffer = ":ANSWER" + toWebCT.getCodeAnswer(positionQuestion, positionAnswer ) + ":" + buffer + ":H";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.toLog("error: Unable writer in a File.");
				}
				buffer = toWebCT.getTextAnswer(positionQuestion, positionAnswer);
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.toLog("error: Unable writer in a File.");
				}
				buffer = ":REASON" + toWebCT.getCodeAnswer(positionQuestion, positionAnswer ) + ":H";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.toLog("error: Unable writer in a File.");
				}
				buffer = "";
				if (toWebCT.getExplanation(positionQuestion, positionAnswer) != null)
					buffer = toWebCT.getExplanation(positionQuestion, positionAnswer);
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.toLog("error: Unable writer in a File.");
				}

			}
		}
		//
		// Now we close file
		//

		try {
			quest.close();
		} catch (Exception e) {
			UtilLog.toLog("error: File isn´t closed.");
		}

		quest = null;

		buffer = "";

		return true;

	} // makeWebCTFile ( )

} // class ToWebCT
