package es.uco.WOW.TestConverter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import es.uco.WOW.Utils.UtilLog;

public class MoodleXML extends DefaultHandler {
    

	//
	// My Data
	//

	/** situation. */
	protected int flag;

	protected int oldFlag;

	protected int numberOfQuestion;


	/** The FileOfQuestionsStruct element */
	protected FileOfQuestionsStruct fileQuestion;

	protected String oldAttrib;

	protected String path;
	
    protected  int answers;
    protected  int minAnswers;
    protected  int correctAnswers;
    

	public MoodleXML ( ) {
	}

	public void startDocument() throws SAXException {
			numberOfQuestion	= 0;
			flag				= 0;
			oldFlag				= 0;
	        answers				= 0;
	        minAnswers			= 20;
	        correctAnswers		= 0;
	        fileQuestion		= new FileOfQuestionsStruct ();
			oldAttrib			= "";
	    	
	 }


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

			switch ( flag ) {
			case 6:
					/** enumciate **/
				if ( flag  == oldFlag )
					value = fileQuestion.getEnunciate (  ) + value;
				oldFlag = flag;
				
				fileQuestion.setEnunciate ( value );
				break;
			case 7:
					/** image **/
				if ( flag  == oldFlag )
					value = fileQuestion.getImage ( ) + value;
				oldFlag = flag;
				fileQuestion.setImage ( value );
				break;
			case 9:
					/** textAnswer **/ 
				if ( flag  == oldFlag )
					value = fileQuestion.getTextAnswer ( ) + value;
					
				oldFlag = flag;
				
				fileQuestion.setTextAnswer ( value );
				break;
			case 11:
					/** explanation **/
				
				if ( flag  == oldFlag )
					value = fileQuestion.getExplanation ( ) + value;
				
				oldFlag = flag;
				
				fileQuestion.setExplanation ( value );
				break;
			default:
				/** An error **/
				break;
		}
		}
	} // characters(char[],int,int);



    
    
/*    
    NAME: FromMoodleXML ( )
    IT'S FROM: Class FromMoodleXML.
    CALLED FROM: Main program 
    CALL TO:
    		from WriteWOW3 Class:
    			String getName ( )
    			int getCodeQuestion ( int positionQuestion )
	    		int getNumberOfAnswers ( int positionQuestion )
	    		int getNumberOfCorrect ( int positionQuestion )
	    		String getExplanation ( int positionQuestion , int positionAnswer )
				String getTextAnswer ( int positionQuestion , int positionAnswer )
				String getEnunciate ( int positionQuestion )
	    		
    recibed:
            void
    RETURN:
    		void
    FUNCTION:
    		Write in a file why call the program.
    		 ( Only Can write simple and multiple choice ).
    */
	public void startElement(String uri, String local, String raw, Attributes attrs) throws SAXException
	{
		switch ( flag )
		{
			case 0:
				if ( local.equals ( "quiz" ) )
					flag = 1;
				if ( local.equals ( "question" ) )
				{
					flag = 2;
					
					if ( numberOfQuestion > 0 )
				        if ( minAnswers > answers ) {
				      	  UtilLog.toLog( "entra");
				        	minAnswers = answers;
				        	fileQuestion.setNumberOfAnswersForFile (minAnswers);
				        }
					numberOfQuestion++;
					fileQuestion.setCodeQuestion ( numberOfQuestion );
			        answers = 0;
			        correctAnswers = 0;
				}
				break;
			case 1:
				//
				// quiz
				//
				if ( local.equals ( "question" ) )
				{
					flag = 2;

					if ( numberOfQuestion > 0 )
				        if ( minAnswers > answers )
				        {
				      	  UtilLog.toLog ( "entra");
				        	minAnswers = answers;
				        	fileQuestion.setNumberOfAnswersForFile (minAnswers);
				        }
					numberOfQuestion++;
					fileQuestion.setCodeQuestion ( numberOfQuestion );
			        answers = 0;
			        correctAnswers = 0;
				}
				break;
			case 2:
				//
				// question
				//
				if ( local.equals ( "name" ) )
					flag = 3;
				break;
			case 3:
				//
				// name
				//
				if ( local.equals ( "text" ) )
					flag = 4;
				break;
			case 4:
				//
				// text
				//
				if ( local.equals ( "questiontext" ) )
					flag = 5;

				break;
			case 5:
				//
				// questiontext
				//
				if ( local.equals ( "text" ) )
					flag = 6;
				break;
			case 6:
				//
				// text
				//
				if ( local.equals ( "image" ) )
					flag = 7;
				else
				{
					if ( local.equals ( "penalty" ) || local.equals ( "hidden" ) || local.equals ( "single" )  )
						flag = -12;
					// error
				}
				break;
			case 7:
				//
				// image
				//
				if ( local.equals ( "answer" ) )
				{
					flag = 8;
					answers++;
					fileQuestion.setNumberOfAnswers ( answers );
					fileQuestion.setCodeAnswer ( answers );
				}
				else
				{
					if ( local.equals ( "penalty" ) || local.equals ( "hidden" ) || local.equals ( "single" )  )
						flag = -12;
					// error
				}
				break;
			case 8:
				//
				// answer
				//
				if ( local.equals ( "text" ) )
					flag = 9;
				break;
			case 9:
				//
				// text
				//
				if ( local.equals ( "feedback" ) )
					flag = 10;
				if ( local.equals ( "answer" ) )
				{
					flag = 8;
					answers++;
					fileQuestion.setNumberOfAnswers ( answers );
					fileQuestion.setCodeAnswer ( answers );
					
				}
				if ( local.equals ( "question" ) )
				{
					flag = 2;
					numberOfQuestion++;
			        if ( minAnswers > answers )
			        {
			      	  UtilLog.toLog ( "entra");
			        	minAnswers = answers;
			        	fileQuestion.setNumberOfAnswersForFile (minAnswers);
			        }
					fileQuestion.setCodeQuestion ( numberOfQuestion );
			        answers = 0;
			        correctAnswers = 0;
				}
				break;
			case 10:
				//
				// feedback
				//
				if ( local.equals ( "text" ) )
					flag = 11;
				break;
			case 11:
				//
				// text
				//
				if ( local.equals ( "answer" ) )
				{
					flag = 8;
					answers++;
					fileQuestion.setNumberOfAnswers ( answers );
					fileQuestion.setCodeAnswer ( answers );
				}
				if ( local.equals ( "question" ) )
				{
					flag = 2;
					numberOfQuestion++;
			        if ( minAnswers > answers )
			        {
			      	  UtilLog.toLog ( "entra");
			        	minAnswers = answers;
			        	fileQuestion.setNumberOfAnswersForFile (minAnswers);
			        }
					fileQuestion.setCodeQuestion ( numberOfQuestion );
			        answers = 0;
			        correctAnswers = 0;
			        }
				break;
			default:
				/** An error**/
				//
				//	penalty
				//	hidden
				//	single
				//
				if ( local.equals ( "question" ) )
				{
					flag = 2;
					numberOfQuestion++;
			        if ( minAnswers > answers )
			        {
			      	  UtilLog.toLog ( "entra");
			        	minAnswers = answers;
			        	fileQuestion.setNumberOfAnswersForFile (minAnswers);
			        }
					fileQuestion.setCodeQuestion ( numberOfQuestion );
			        answers = 0;
			        correctAnswers = 0;
				}
				if ( local.equals ( "image" ) )
					flag = 7;
				if ( local.equals ( "answer" ) )
				{
					flag = 8;
					answers++;
					fileQuestion.setNumberOfAnswers ( answers );
					fileQuestion.setCodeAnswer ( answers );
				}
				break;
		}
		if (attrs != null)
		{
			int attrCount = attrs.getLength ( );
			String attrName = null;
			String attrValue = null;
			
			for ( int i = 0; i < attrCount; i++ )
			{
				attrName  = attrs.getQName ( i );
				attrValue = attrs.getValue ( i );
				
				switch ( flag ) {
					case 2:
							// Attributes of Question
						if ( attrName.equals ( "type" ) )
						{
							if ( attrValue.equals ( "shortanswer" ) )
								flag = -1;
							if ( attrValue.equals ( "matching" ) )
								flag = -2;
							if ( attrValue.equals ( "description" ) )
								flag = -3;
							if ( attrValue.equals ( "numerical" ) )
								flag = -4;
							if ( attrValue.equals ( "cloze" ) )
								flag = -5;
							if ( attrValue.equals ( "unknown" ) )
								flag = -6;
						}
						if ( flag < 0 )
							{
								fileQuestion.setCodeQuestion ( 0 , fileQuestion.getCodeQuestion ( ) * -1 );
								fileQuestion.setEnunciate ( "type of question: " + attrValue );
							}
						break;
					case 8:
							// Attributes of Answer
						if ( attrName.equals ( "fraction" ) )
						{
							if ( fileQuestion.getCorrect ( ) != "false"  && fileQuestion.getCorrect ( ) != "true" )
							{
								// Take if Answer is correct.
								if ( attrValue.equals ( "-" ) )
								{
									attrValue = "false";
								}
								else
								{
									oldAttrib = attrName;
									if ( Float.parseFloat ( attrValue ) > 0 )
									{
										attrValue = "true";
										correctAnswers++;
										fileQuestion.setNumberOfCorrect ( correctAnswers );
									}
									else
									{
										attrValue = "false";
									}
								}
								if ( attrValue != "false"  && attrValue != "true" )
									attrValue = "false";
								if ( fileQuestion.getCorrect ( ) == null )
									fileQuestion.setCorrect ( attrValue );
							}
						}
						break;
					default:
							/** An error**/
							break;
				}
			}
		}

	} // startElement(String , String , String , Attributes )


	
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
	
	public FileOfQuestionsStruct getFileQuestion ( )
	{
		return fileQuestion;
	}

} // class MoodleXML
