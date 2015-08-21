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

public class ToMoodleXML {

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

	protected ReadWOW3 toMoodleXML;

	public ToMoodleXML() {}

	//
	// MAIN
	//

	/** Main program entry point. */
	public boolean convert(String inputFile, String outputFile) {

		// variables
		toMoodleXML = new ReadWOW3();
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
		parser.setContentHandler(toMoodleXML);
		parser.setErrorHandler(toMoodleXML);

		try {
			parser.parse(inputFile);
		} catch (Exception e) {
			UtilLog.writeException(e);
		}

		return makeMoodleXML(outputFile);

	} // convert ( String , String )

	//
	// Private methods
	//

	/*
	 * NAME: makeMoodleXML ( )
	 * IT'S FROM: Class ToMoodleXML.
	 * CALLED FROM: Main
	 * program CALL from ReadWOW3 Class:
	 * int getNumberOfQuestions ( )
	 * String getName ( )
	 * int getCodeQuestion ( int positionQuestion )
	 * int getNumberOfAnswers ( int positionQuestion )
	 * int getNumberOfCorrect ( int positionQuestion )
	 * String getExplanation ( int positionQuestion , int positionAnswer )
	 * String getTextAnswer ( int positionQuestion , int positionAnswer )
	 * String getEnunciate ( int positionQuestion )
	 * 
	 * recibed: void RETURN: void FUNCTION: Write in a file why call the program. (
	 * Only Can write simple and multiple choice ).
	 */
	public boolean makeMoodleXML(String FileName) {
		BufferedWriter quest = null;
		int numberOfAnswers;
		int numberOfQuestions = toMoodleXML.getNumberOfQuestions();
		int numberOfCorrect;
		String buffer = "";
		String image = "";
		float percent = 0;

		if (numberOfQuestions < 1 || toMoodleXML.flag < 15)
			return false;

		try {
			quest = new BufferedWriter(new FileWriter(FileName));
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
		//
		// Here we create a Questions File type Moodle-XML
		//
		buffer = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		buffer = "<quiz>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		for (int positionQuestion = 0; positionQuestion <= numberOfQuestions; positionQuestion++) {
			numberOfAnswers = toMoodleXML.getNumberOfAnswers(positionQuestion);
			numberOfCorrect = toMoodleXML.getNumberOfCorrect(positionQuestion);
			percent = (float)100 / (float)numberOfCorrect;
			buffer = "<!--  Question: " + toMoodleXML.getCodeQuestion(positionQuestion) + " -->";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t<question type=\"multichoice\">";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t<name>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t\t<text>" + toMoodleXML.replaceAmp(toMoodleXML.getEnunciate(positionQuestion))
					+ "</text>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t</name>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t<questiontext format=\"html\">";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t\t<text>" + toMoodleXML.replaceAmp(toMoodleXML.getEnunciate(positionQuestion))
					+ "</text>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t</questiontext>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t<image>";
			image = getImage(positionQuestion);
			if ( image.equals ( "" ) )
				buffer = buffer + "</image>";
			else
				buffer = buffer + ".\\images" + image + "</image>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t<penalty>0.1</penalty>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			buffer = "\t\t<hidden>0</hidden>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			if (numberOfCorrect > 1)
				buffer = "\t\t<single>true</single>";
			else
				buffer = "\t\t<single>false</single>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
			for (int positionAnswer = 0; positionAnswer < numberOfAnswers; positionAnswer++) {
				//
				// Here write in File the Answer and indicate if these are correct
				// or no,
				// and percent of correct.

				buffer = toMoodleXML.getCorrect(positionQuestion, positionAnswer);

				if (buffer.compareTo("true") == 0) {
					buffer = "\t\t<answer fraction=\"";
				} else {
					buffer = "\t\t<answer fraction=\"-";
				}
				buffer = buffer + percent + "\">";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.writeException(e);
				}
				buffer = "\t\t\t<text>"
						+ toMoodleXML.replaceAmp(toMoodleXML.getTextAnswer(positionQuestion, positionAnswer))
						+ "</text>";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.writeException(e);
				}
				buffer = "\t\t\t<feedback>";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.writeException(e);
				}
				if (toMoodleXML.getExplanation(positionQuestion, positionAnswer) != null) {
					buffer = "\t\t\t\t<text>"
							+ toMoodleXML.replaceAmp(toMoodleXML.getExplanation(positionQuestion, positionAnswer))
							+ "</text>";
					try {
						quest.write(buffer, 0, buffer.length());
						quest.newLine();
					} catch (Exception e) {
						UtilLog.writeException(e);
					}
				} else {
					buffer = "\t\t\t\t<text></text>";
					try {
						quest.write(buffer, 0, buffer.length());
						quest.newLine();
					} catch (Exception e) {
						UtilLog.writeException(e);
					}
				}
				buffer = "\t\t\t</feedback>";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.writeException(e);
				}
				buffer = "\t\t</answer>";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.writeException(e);
				}
			}
			//
			// Now we Close the Question
			//
			buffer = "\t</question>";
			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
				quest.newLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
			}
		}
		buffer = "</quiz>";
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

	} // makeMoodleXML ( )


	
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
		if (toMoodleXML.getImage(positionQuestion) != "") {
			image = toMoodleXML.getImage(positionQuestion);

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

		//imageName = imageName.substring ( 1 ); 
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
			if ( i < 0 )
			{
				i = output.lastIndexOf ( "/" );
				if ( i < 0 )
					i = 0;
			}
			
			outputPath = output.substring ( 0 , i ) + File.separator;
			p = null;
			p = new File(input);
				// We find the path of input file without Name
			inputPath = p.getPath();
			i = inputPath.lastIndexOf ( "\\" );
			if ( i < 0 )
				i = inputPath.lastIndexOf ( "/" );
			inputPath = inputPath.substring ( 0 , i );
			// We find the path of input file without Name in WOW system.
			inputsubPath = inputPath.substring ( 0, inputPath.indexOf ( token ) + token.length ( ) );
			p = null;
			
			
			p = null;
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
		
		return true;
	}

} // class ToGift
