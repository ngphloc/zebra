package es.uco.WOW.TestConverter;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Qti12 extends DefaultHandler {
    

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

	
    protected  int answers;
    protected  int minAnswers;
    protected  int correctAnswers;
    protected  Vector vCorrect;
    protected  Vector vpCorrect;
    
    

	public Qti12 ( ) {
	}
	public void startDocument() throws SAXException {
		vCorrect = new Vector ( );
		vpCorrect = new Vector ( );
		numberOfQuestion	= 0;
		flag				= 0;
		oldFlag				= 0;
        answers				= 0;
        minAnswers			= 20;
        correctAnswers		= 0;
        fileQuestion		= new FileOfQuestionsStruct ();
		oldAttrib			= "";
    	
 }
	
	public void startElement(String uri, String local, String raw, Attributes attrs) throws SAXException 
	{
		switch ( flag )
		{
			case 0 :
				//
				// Here it start the file
				//
				if ( local.equals ( "questestinterop"  ) )
						flag = 1;
				break;
			case 1 :
				//
				// questestinterop
				//
				if ( local.equals ( "item"  ) )
				{
					flag = 2;
					if ( numberOfQuestion > 0 )
					{
						vCorrect.removeAllElements ( );
				        vpCorrect.removeAllElements ( );
				        
						fileQuestion.setNumberOfAnswers ( answers );
						fileQuestion.setNumberOfCorrect	( correctAnswers );
						
						if (minAnswers > answers ) {
							minAnswers = answers;
							fileQuestion.setNumberOfAnswersForFile ( minAnswers );
						}
					}
					numberOfQuestion++;
					
					fileQuestion.setCodeQuestion ( numberOfQuestion );
					
			        answers	= 0;
			        correctAnswers = 0;
				}
				break;
			case 2 :
				//
				// item
				//
				if ( local.equals ( "presentation" ) )
					flag = 3;
				break;
			case 3 :
				//
				// presentation
				//
				if ( local.equals ( "flow" ) )
					flag = 4;
				break;
			case 4 :
				//
				// flow
				//
				if ( local.equals ( "material" ) )
					flag = 5;
				break;
			case 5 :
				//
				// material
				//
				if ( local.equals ( "mattext" ) )
					flag = 6;
				if ( local.equals ( "matimage" ) )
					flag = 7;
				break;
			case 6 :
				//
				// mattext
				//
				if ( local.equals ( "matimage" ) )
					flag = 7;
				if ( local.equals ( "response_lid" ) )
					flag = 8;
				break;
			case 7 :
				//
				// matimage
				//
				if ( local.equals ( "response_lid" ) )
					flag = 8;
				if ( local.equals ( "mattext" ) )
					flag = 6;
				break;
			case 8 :
				//
				// response_lid
				//
				if ( local.equals ( "render_choice" ) )
					flag = 9;
				break;
			case 9 :
				//
				// render_choice
				//
				if ( local.equals ( "flow_label" ) )
					flag = 10;
				break;
			case 10 :
				//
				// flow_label
				//
				if ( local.equals ( "response_label" ) )
				{
					answers++;
					fileQuestion.setCodeAnswer ( answers );
					fileQuestion.setCorrect ( "false" );
					
					fileQuestion.setNumberOfAnswers ( answers );

					
					flag = 12;
				}
				break;
			case 11 :
				//
				// response_label
				//
				if ( local.equals ( "flow_mat" ) )
						flag = 12;
				break;
			case 12 :
				//
				// flow_mat
				//
				if ( local.equals ( "material" ) )
					flag = 13;
				break;
			case 13 :
				//
				// material
				//
				if ( local.equals ( "mattext" ) )
					flag = 14;
				break;
			case 14 :
				//
				// mattext
				//
				if ( local.equals ( "flow_label" ) )
					flag = 10;
				if ( local.equals ( "resprocessing" ) )
					flag = 15;
				break;
			case 15 :
				//
				// resprocessing
				//
				if ( local.equals ( "outcomes" ) )
					flag = 16;
				break;
			case 16 :
				//
				// outcomes
				//
				if ( local.equals ( "decvar" ) )
					flag = 17;
				break;
			case 17 :
				//
				// decvar
				//
				if ( local.equals ( "respcondition" ) )
					flag = 18;
				break;
			case 18:
				//
				// respcondition
				//
				if ( local.equals ( "conditionvar" ) )
					flag = 19;
				break;
			case 19 :
				//
				// conditionvar
				//
				if ( local.equals ( "varequal" ) )
					flag = 20;
				break;
			case 20 :
				//
				// varequal
				//
				if ( local.equals ( "setvar" ) )
				{
					flag = 21;
				}
				break;
			case 21 :
				//
				// setvar
				//
				if ( local.equals ( "displayfeedback" ) )
				{
					flag = 22;
				}
				break;
			case 22 :
				//
				// displayfeedback
				//
				if ( local.equals ( "itemfeedback" ) )
					flag = 23;
				break;
			case 23 :
				//
				// itemfeedback
				//
				if ( local.equals ( "flow_mat" ) )
				{
					flag = 24;
					if ( numberOfQuestion > 0 )
					{
						vCorrect.removeAllElements ( );
				        vpCorrect.removeAllElements ( );
				        
						fileQuestion.setNumberOfAnswers ( answers );
						fileQuestion.setNumberOfCorrect	( correctAnswers );
						
						if (minAnswers > answers ) {
							minAnswers = answers;
							fileQuestion.setNumberOfAnswersForFile ( minAnswers );
						}
					}
				}
				break;
			case 24 :
				//
				// flow_mat
				//
				if ( local.equals ( "material" ) )
						flag = 25;
				break;
			case 25 :
				//
				// material
				//
				if ( local.equals ( "mattext" ) )
						flag = 26;
				break;
			case 26 :
				//
				// mattext
				//
				if ( local.equals ( "item" ) )
				{
					flag = 2;
					if ( numberOfQuestion > 0 )
					{
						vCorrect.removeAllElements ( );
				        vpCorrect.removeAllElements ( );
				        
						fileQuestion.setNumberOfAnswers ( answers );
						fileQuestion.setNumberOfCorrect	( correctAnswers );
						
						if (minAnswers > answers ) {
							minAnswers = answers;
							fileQuestion.setNumberOfAnswersForFile ( minAnswers );
						}
					}
					numberOfQuestion++;
					
					fileQuestion.setCodeQuestion ( numberOfQuestion );
					
			        answers	= 0;
			        correctAnswers = 0;
				}

					break;
				default:
						/** An error**/
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
							case 7 :
								//
								//	matimage
								//
								if ( attrName.equals ( "uri" ) )
									fileQuestion.setImage ( attrValue );
								break;
							case 9 :
//								//
//								//	choiceInteraction
//								//
//								if ( attrName.equals ( "responseIdentifier" ) )
//								{
//									;
//								}
//								if ( attrName.equals ( "shuffle" ) )
//								{
//									;
//								}
//								if ( attrName.equals ( "maxChoices" ) )
//								{
//									;
//								}
								break;
							case 12 :
								//
								//	simpleChoice 
								//
								if ( attrName.equals ( "ident" ) )
								{
									vpCorrect.addElement ( attrValue );
									vCorrect.addElement (new Integer ( answers ));
								}
								break;
							case 13 :
								//
								//	feedbackInline
								//
//								if ( attrName.equals ( "outcomeIdentifier" ) )
//								{
//									;
//								}
//								if ( attrName.equals ( "identifier" ) )
//								{
//									;
//								}
//								if ( attrName.equals ( "showHide" ) )
//								{
//									if ( attrValue.equals ( "show" ) )
//										;
//								}
								break;
							case 14 :
								//
								//	modalFeedback
								//
//								if ( attrName.equals ( "xmlns:dc" ) )
//								{
//									;
//								}
								break;
							case 15 :
								//
								//	modalFeedback
								//
//								if ( attrName.equals ( "xmlns:dc" ) )
//								{
//									;
//								}
								break;
							default:
									/** An error**/
								break;
						}
					}
				}


		} // startElement ( String , String , StringAttributes )

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

				if ( value.equals ( "" ) )
					return;
				switch ( flag ) {
					case 6:
							/** enumciate **/
						if ( oldFlag == flag )
							value = value + fileQuestion.getEnunciate ( );
						else
							oldFlag = flag;
						fileQuestion.setEnunciate ( value );
						break;

					case 14:
						/** textAnswer **/
						if ( oldFlag == flag  )
							if ( fileQuestion.getTextAnswer ( ) != null )
								value = value + fileQuestion.getTextAnswer ( );
						else
							oldFlag = flag;
						fileQuestion.setTextAnswer ( value );
						break;
					case 20:
							for ( int i = 0; i < vCorrect.size ( ); i++ )
							{
								String buffer = (String)  vpCorrect.elementAt ( i );
								if ( value.equals ( buffer ) )
								{
									int corrTrue = ((Integer) (vCorrect.elementAt ( i ))).intValue();
									correctAnswers++;
									fileQuestion.setCorrect ( 0 , corrTrue - 1 , fileQuestion.getCodeQuestion () - 1 , "true" );
									fileQuestion.setNumberOfCorrect ( correctAnswers );
								}
							}
						break;
					case 22:
						/** Correct **/
						

						break;
						
					case 24:
							/** Explanation **/ 
						for ( int i = 0; i < vpCorrect.size ( ); i++ )	
							if ( fileQuestion.getCorrect ( 0 , i + 1 ) == "true" )
									fileQuestion.setExplanation ( 0 , i + 1 , value );
							else
								fileQuestion.setExplanation ( 0 , i + 1 , "" );
						
						break;
				}
			}
		} // characters(char[],int,int);


    //
    // Private  methods
    //


/*    
    NAME: FromQti12 ( )
    IT'S FROM: Class My.
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
	
	public FileOfQuestionsStruct getFileQuestion ( )
	{
		return fileQuestion;
	}

} // class FromQti12
