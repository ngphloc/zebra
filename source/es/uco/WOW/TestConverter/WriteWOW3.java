package es.uco.WOW.TestConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;

import vn.spring.WOW.WOWStatic;
import es.uco.WOW.Utils.UtilLog;



public class WriteWOW3 {



	//
	// My Data
	//

	/** Indicate the number of Questions */
	protected int numberOfQuestion;
	/** The the path of input file  without Name */
	protected String inputPath;
	/** The the path of output file  without Name */
	protected String outputPath;
	/** The the path of input file without Name in WOW sistem */
	protected String outputsubPath;
	/** The the path of image output */
	protected String imagesPath;

	// Constructors
	
	/*
	 * parameters:
	 * 		input -> the file that we read for import items
	 * 		output -> the file that we write with the imported items
	 * 
	 * p -> File type that we used to open files
	 * i -> Integer type that we use to locate the token
	 */

	public WriteWOW3 ( String input , String output ) {
		
		File p = null;
		int i = -1;
		
		String token = WOWStatic.config.Get("XMLROOT") + File.separator + WOWStatic.config.Get("itemspath");

		p = new File(input);
		inputPath = p.getPath();
			// We find the path of input file without Name
		i = input.lastIndexOf ( "\\" );
		if ( i < 0 )
		{
			i = input.lastIndexOf ( "/" );
			if ( i < 0 )
				i = 0;
		}
		
		inputPath = input.substring ( 0 , i ) + File.separator;
		p = null;
		p = new File(output);
			// We find the path of output file without Name
		outputPath = p.getPath();
		i = outputPath.lastIndexOf ( "\\" );
		if ( i < 0 )
			i = outputPath.lastIndexOf ( "/" );
		outputPath = outputPath.substring ( 0 , i );
		// We find the path of output file without Name in WOW system.
		outputsubPath = outputPath.substring ( outputPath.indexOf ( token ) + token.length ( ) );
		p = null;
		// We create the images path
		imagesPath = outputPath +  "/images/";
		p = new File( imagesPath );
		p.mkdir();
		
		numberOfQuestion	= 0;
		
		p = null;
	}
	
	public WriteWOW3 ( ) {
		numberOfQuestion	= 0;
	}

	/*
	 * NAME: WriteWOWFile ( )
	 * IT'S FROM: Class WriteWOW3
	 * CALLED FROM: FromGift
	 * program CALL TO:
	 * 			from FileOfQuestionsStruct Class:
	 * 					String getName ( )
	 * 					int getCodeQuestion ( int positionQuestion )
	 * 					String getAdaptative ( int positionQuestion )
	 * 					String getConcept ( )
	 * 					int getNumberOfAnswers ( int positionQuestion )
	 * 					int getNumberOfCorrect ( int positionQuestion )
	 * 					int getCodeAnswer ( int positionQuestion , int positionAnswer )
	 * 					String getExplanation ( int positionQuestion , int positionAnswer )
	 * 					String getTextAnswer ( int positionQuestion , int positionAnswer )
	 * 					String getEnunciate ( int positionQuestion )
	 * 					String getImage ( int positionQuestion )
	 * 			from WriteWOW3 Class:
	 * 					String getImage ( String image )
	 * 
	 * recibed:
	 * 				fileQuestion -> type FileOfQuestionsStruct 
	 * 				FileName -> type String that say the name of File that we write.
	 * RETURN: boolean
	 *  FUNCTION: Write WOW Questions test File. ( Write simple and multiple choice with or without image ).
	 */

	public boolean writeWOWFile( FileOfQuestionsStruct fileQuestion , String FileName ) {
		

		BufferedWriter quest = null;

		int numberOfAnswers;
		int numberOfCorrect;
		String buffer = "";
		String image = "";
		numberOfQuestion	= fileQuestion.getLastQuestion ( ) + 1;

		if ( numberOfQuestion < 1 )
			return false;
		

		try {
			quest = new BufferedWriter(new FileWriter(FileName));
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		//
		// start write XML file
		//

		buffer = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.toLog("error: Unable writer in a File.");
		}

		buffer = "<!DOCTYPE fileOfQuestions SYSTEM \"../../fileOfQuestions.dtd\">";

		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.toLog("error: Unable writer in a File.");
		}

		buffer = "<fileOfQuestions fileName=\"" + fileQuestion.getCourse() + "\" name=\""
				+ fileQuestion.getFileName() + "\" course=\"" + fileQuestion.getCourse() + "\" path=\""
				+ fileQuestion.getPath() + "_WOW.xml" + "\" numberOfAnswersForFile=\""
				+ fileQuestion.getNumberOfAnswersForFile() + "\">";

		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.toLog("error: Unable writer in a File.");
		}

		if (fileQuestion.getConcept() != null)
			buffer = replaceAmp(fileQuestion.getConcept());
		else
			buffer = "";

		buffer = "\t<concept value=\"" + buffer + "\" />";

		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.toLog("error: Unable writer in a File.");
		}
		numberOfQuestion = fileQuestion.getLastQuestion() + 1;

		buffer = "\t<questions>";

		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.toLog("error: Unable writer in a File.");
		}

		for (int positionQuestion = 0; positionQuestion < numberOfQuestion; positionQuestion++) {
			numberOfCorrect = fileQuestion.getNumberOfCorrect(positionQuestion);
			if (numberOfCorrect < 1)
				continue;
			//
			// Here we create a Questions File type WOW! 1.0
			//
			if (fileQuestion.getCodeQuestion(positionQuestion) < 0) {
				buffer = "<!-- The Question " + fileQuestion.getCodeQuestion(positionQuestion) * -1
						+ " is not posible export because "
						+ replaceAmp(fileQuestion.getEnunciate(positionQuestion)) + " -->";
				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.toLog("error: Unable writer in a File.");
				}
				continue;
			}
			buffer = "\t\t<question codeQuestion=\"" + fileQuestion.getCodeQuestion(positionQuestion) + "\">";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			numberOfCorrect = fileQuestion.getNumberOfCorrect(positionQuestion);
			buffer = "\t\t\t<enunciate>" + replaceAmp(fileQuestion.getEnunciate(positionQuestion))
					+ "</enunciate>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}
			image = fileQuestion.getImage(positionQuestion);
			if (image.length() > 0 && image != "" && image != "\t" && image != null)
				buffer = "\t\t\t<image>" +  getImage( image ) + "</image>";
			else
				buffer = "\t\t\t<image/>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			numberOfAnswers = fileQuestion.getNumberOfAnswers(positionQuestion);

			buffer = "\t\t\t<answers numberOfCorrect=\"" + numberOfCorrect + "\" numberOfAnswers=\""
					+ fileQuestion.getNumberOfAnswers(positionQuestion) + "\">";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			for (int positionAnswer = 0; positionAnswer < fileQuestion.getNumberOfAnswers(positionQuestion); positionAnswer++) {
				//
				// Here write in File the Answer
				//
				buffer = "\t\t\t\t<answer codeAnswer=\""
						+ fileQuestion.getCodeAnswer(0, positionQuestion, positionAnswer) + "\">";

				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.toLog("error: Unable writer in a File.");
				}

				buffer = "\t\t\t\t\t<textAnswer>"
						+ replaceAmp(fileQuestion.getTextAnswer(0, positionQuestion, positionAnswer))
						+ "</textAnswer>";

				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.toLog("error: Unable writer in a File.");
				}

				buffer = "\t\t\t\t\t<correct>"
						+ replaceAmp(fileQuestion.getCorrect(0, positionQuestion, positionAnswer)) + "</correct>";

				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.toLog("error: Unable writer in a File.");
				}

				if (fileQuestion.getExplanation(0, positionAnswer, positionQuestion) != null)
					buffer = "\t\t\t\t\t<explanation>"
							+ replaceAmp(fileQuestion.getExplanation(0, positionAnswer, positionQuestion))
							+ "</explanation>";
				else
					buffer = "\t\t\t\t\t<explanation></explanation>";

				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.toLog("error: Unable writer in a File.");
				}

				buffer = "\t\t\t\t</answer>";

				try {
					quest.write(buffer, 0, buffer.length());
					quest.newLine();
				} catch (Exception e) {
					UtilLog.toLog("error: Unable writer in a File.");
				}
			}

			buffer = "\t\t\t</answers>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			buffer = "\t\t\t<irtParameters>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			buffer = "\t\t\t\t<difficulty>" + fileQuestion.getDifficulty(positionQuestion) + "</difficulty>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}
			if (fileQuestion.getDiscrimination(positionQuestion) > 0)
				buffer = "" + fileQuestion.getDiscrimination(positionQuestion);
			else
				buffer = "" + ((float) numberOfCorrect);

			buffer = "\t\t\t\t<discrimination>" + buffer + "</discrimination>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}
			if (fileQuestion.getGuessing(positionQuestion) > 0)
				buffer = "" + fileQuestion.getGuessing(positionQuestion);
			else
				buffer = "" + ((float) numberOfCorrect / (float) numberOfAnswers);

			buffer = "\t\t\t\t<guessing>" + buffer + "</guessing>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			buffer = "\t\t\t</irtParameters>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			buffer = "\t\t\t<statisticParameters>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			buffer = "\t\t\t\t<exhibitionRate>0.0</exhibitionRate>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			buffer = "\t\t\t\t<answerTime>0</answerTime>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			buffer = "\t\t\t\t<successRate>0.0</successRate>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			buffer = "\t\t\t\t<numberOfUses>0</numberOfUses>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			buffer = "\t\t\t\t<numberOfSuccesses>0</numberOfSuccesses>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			buffer = "\t\t\t</statisticParameters>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}

			buffer = "\t\t</question>";

			try {
				quest.write(buffer, 0, buffer.length());
				quest.newLine();
			} catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File.");
			}
		}

		buffer = "\t</questions>";
		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.toLog("error: Unable writer in a File.");
		}
		buffer = "</fileOfQuestions>";

		try {
			quest.write(buffer, 0, buffer.length());
			quest.newLine();
		} catch (Exception e) {
			UtilLog.toLog("error: Unable writer in a File.");
		}

		try {
			quest.close();
		} catch (Exception e) {
			UtilLog.toLog("error: File isn�t closed.");
		}

		buffer = "";

		quest = null;

		return true;

	} // riteWOWFile ( FileOfQuestionsStruct , String )
	


	public String getImage ( String image ) {

		String imageName = null;

		File in = null;
		File out = null;
		byte[] buf = new byte[1024];
		int i = 0;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		in = new File(image);
		i = image.lastIndexOf ( "\\" );
		if ( i < 0 )
		{
			i = image.lastIndexOf ( "/" );
			if ( i < 0 )
				i = 0;
		}
		imageName = image.substring ( i );

		image = inputPath + image;
		
		in = new File(image);

		out = new File( imagesPath + imageName );
		try {
			fis = new FileInputStream(in);
			fos = new FileOutputStream(out);
		} catch (Exception e) {
			UtilLog.toLog("error: Files aren�t open.");
		}

		try {
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} catch (Exception e) {
			UtilLog.toLog("error: File isn�t read or write.");
		}
		
		try {
			fis.close();
			fos.close();
		} catch (Exception e) {
			UtilLog.toLog("error: Files aren�t closed.");
		}

		return outputsubPath + File.separator + "images" + File.separator + replaceAmp(imageName);
	} // getImage ( )


	public String replaceAmp(String original) {
		String partAfter;
		int i = 0;
		if (original == null)
			return original;
		do {
			i = original.indexOf("&", i);
			if (i > -1) {
				/*
				 * lt "&#38;#60;" gt "&#62;" amp "&#38;#38;" apos "&#39;" quot
				 * "&#34;" aacute CDATA "&#225;" � eacute CDATA "&#233;" � iacute
				 * CDATA "&#237;" � oacute CDATA "&#243;" � uacute CDATA "&#250;" �
				 * Aacute CDATA "&#193;" � Eacute CDATA "&#201;" � ENTITY Iacute
				 * CDATA "&#205;" � Oacute CDATA "&#211;" � Uacute CDATA "&#218;" �
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
			 * find hight than ( "�" iquest )
			 */
			i = original.indexOf("�", i);
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
		 * ENTITY aacute CDATA "&#225;" �
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "�" aacute )
			 */
			i = original.indexOf("�", i);
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
		 * ENTITY eacute CDATA "&#233;" �
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "�" eacute )
			 */
			i = original.indexOf("�", i);
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
		 * ENTITY iacute CDATA "&#237;" �
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "�" iacute )
			 */
			i = original.indexOf("�", i);
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
		 * ENTITY oacute CDATA "&#243;" �
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "�" oacute )
			 */
			i = original.indexOf("�", i);
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
		 * ENTITY uacute CDATA "&#250;" �
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "�" aacute )
			 */
			i = original.indexOf("�", i);
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
		 * ENTITY Aacute CDATA "&#193;" �
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "�" Aacute )
			 */
			i = original.indexOf("�", i);
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
		 * ENTITY Eacute CDATA "&#201;" �
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "�" Eacute )
			 */
			i = original.indexOf("�", i);
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
		 * ENTITY Iacute CDATA "&#205;" �
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "�" aacute )
			 */
			i = original.indexOf("�", i);
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
		 * ENTITY Oacute CDATA "&#211;" �
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "�" aacute )
			 */
			i = original.indexOf("�", i);
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
		 * ENTITY Uacute CDATA "&#218;" �
		 */
		i = 0;
		do {
			/*
			 * find hight than ( "�" Uacute )
			 */
			i = original.indexOf("�", i);
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

		original = whitoutEndSpaces(original);

		return original;
	} // replaceAmp ( String )

	public String whitoutEndSpaces(String original) {
		if (original.endsWith(" ") || original.endsWith("\t")) {
			original = original.substring(0, original.length() - 1);
			original = whitoutEndSpaces(original);

		}
		return original;
	} // whitoutEndSpaces ( String )

} // class WriteWOW3
