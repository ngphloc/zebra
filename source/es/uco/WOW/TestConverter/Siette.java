package es.uco.WOW.TestConverter;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Siette extends DefaultHandler {
    

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
    
    
    

	public Siette ( ) {
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
					// The SIETTE File is start
					//
				if ( local.equals ( "subject" ) )
					flag = 1;
				break;
			case 1 :
					//
					//	subject
					//
					/** it�s a starter element of this document */
				if ( local.equals ( "name" ) )
					flag = 2;
				if ( local.equals ( "isactive" ) )
					flag = 3;
				if ( local.equals ( "items" ) )
					flag = 13;
				break;
			case 2 :
					//
					//	name
					//
					/** Store the name of a Document */
				if ( local.equals ( "isactive" ) )
					flag = 3;
				break;
			case 3 :
					//
					//	isactive
					//
					/** indicae if is terminate the Document */
				if ( local.equals ( "topics" ) )
					flag = 4;
				if ( local.equals ( "name" ) )
					flag = 2;
				if ( local.equals ( "items" ) )
					flag = 13;
				break;
			case 4 :
					//
					//	topics
					//
					/** Store all topics */
				if ( local.equals ( "topic" ) )
					flag = 5;
				break;
			case 5 :
					//
					//	topic
					//
					/** Store the topic */
				if ( local.equals ( "name" ) )
					flag = 6;
				break;
			case 6 :
					//
					//	name
					//
					/** the name of topic */
				if ( local.equals ( "topic" ) )
					flag = 5;
				if ( local.equals ( "isactive" ) )
					flag = 7;
				if ( local.equals ( "items" ) )
					flag = 13;
				break;
			case 7 :
					//
					//	isactive
					//
					/** indicae if is terminate a topic */
				if ( local.equals ( "translations" ) )
					flag = 8;
				break;
			case 8 :
					//
					//	translations
					//
					/** Store all translation of title topic */
				if ( local.equals ( "translation" ) )
					flag = 9;
				break;
			case 9 :
					//
					//	translation
					//
					/** Store one translation of name topic */
				if ( local.equals ( "name" ) )
					flag = 10;
				break;
			case 10 :
					//
					//	name
					//
					/** the translation�s name of topic */
				if ( local.equals ( "language" ) )
					flag = 11;
				break;
			case 11 :
					//
					//	language
					//
					/** Indicate the language of translation */
				if ( local.equals ( "topic" ) )
					flag = 5;
				if ( local.equals ( "subtopics" ) )
					flag = 12;
				if ( local.equals ( "translation" ) )
					flag = 9;
				if ( local.equals ( "items" ) )
					flag = 13;
				break;
			case 12 :
					//
					//	subtopics
					//
					/** Store the all Subtopics of a topic */
				if ( local.equals ( "topic" ) )
					flag = 5;
				if ( local.equals ( "items" ) )
					flag = 13;
				break;
			case 13 :
					//
					//	items
					//
					/** Here store all questions or items */
				if ( local.equals ( "item" ) )
				{
					flag = 14;
					if ( numberOfQuestion > 0 )
					{
				        vpCorrect.removeAllElements ( );
				        vCorrect.removeAllElements ( );
				        
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
			case 14 :
					//
					//	item
					//
					/** indicate a question is start */
				if ( local.equals ( "title" ) )
					flag = 15;
				break;
			case 15 :
					//
					//	title
					//
					/** indicate the name of this question */
				if ( local.equals ( "isactive" ) )
					flag = 16;
				if ( local.equals ( "stem" ) )
					flag = 17;
				break;
			case 16 :
					//
					//	isactive
					//
					/** indicae if is terminate a question */
				if ( local.equals ( "stem" ) )
					flag = 17;
				break;
			case 17 :
					//
					// stem
					//
					/** enunciate */
				if ( local.equals ( "iccparameters" ) )
					flag = 18;
				break;
			case 18 :
					//
					// iccparameters
					//
				/** Now we separe the enunciate of image*/
				storeEnunciateAndImage ( fileQuestion.getEnunciate ( ) );
					/** The parameters of the Question */
				if ( local.equals ( "responses" ) )
					flag = 19;
				break;
			case 19 :
					//
					// responses
					//
					/** here it`s store all answers */
				if ( local.equals ( "response" ) )
				{
					answers++;
					fileQuestion.setCodeAnswer ( answers );
					fileQuestion.setCorrect ( "false" );
						
					fileQuestion.setNumberOfAnswers ( answers );
					flag = 20;
				}
				break;
			case 20 :
					//
					//	response
					//
					/** Start a Question */
				if ( local.equals ( "text" ) )
					flag = 21;
				break;
			case 21 :
					//
					//	text
					//
					/** TextAnswer */
				if ( local.equals ( "response" ) )
				{
					answers++;
					fileQuestion.setCodeAnswer ( answers );
					fileQuestion.setCorrect ( "false" );
						
					fileQuestion.setNumberOfAnswers ( answers );
					
					flag = 20;
				}
				if ( local.equals ( "feedback" ) )
					flag = 22;
				if ( local.equals ( "status" ) )
					flag = 23;
				if ( local.equals ( "score" ) )
					flag = 24;
				if ( local.equals ( "penalty" ) )
					flag = 25;
				if ( local.equals ( "evaluation" ) )
					flag = 26;
				break;
			case 22 :
					//
					//	feedback
					//
					/** Explanation */
				if ( local.equals ( "response" ) )
				{
					answers++;
					fileQuestion.setCodeAnswer ( answers );
					fileQuestion.setCorrect ( "false" );
						
					fileQuestion.setNumberOfAnswers ( answers );
					
					flag = 20;
				}
				if ( local.equals ( "status" ) )
					flag = 23;
				if ( local.equals ( "score" ) )
					flag = 24;
				if ( local.equals ( "penalty" ) )
					flag = 25;
				if ( local.equals ( "evaluation" ) )
					flag = 26;
				break;
			case 23 :
					//
					//	status
					//
				if ( local.equals ( "response" ) )
				{
					answers++;
					fileQuestion.setCodeAnswer ( answers );
					fileQuestion.setCorrect ( "false" );
						
					fileQuestion.setNumberOfAnswers ( answers );
					
					flag = 20;
				}
				if ( local.equals ( "feedback" ) )
					flag = 22;
				if ( local.equals ( "score" ) )
					flag = 24;
				if ( local.equals ( "penalty" ) )
					flag = 25;
				if ( local.equals ( "evaluation" ) )
					flag = 26;
				break;
			case 24 :
					//
					//	score
					//
					/** gift of a incorrect Answer */
				if ( local.equals ( "response" ) )
				{
					answers++;
					fileQuestion.setCodeAnswer ( answers );
					fileQuestion.setCorrect ( "false" );
						
					fileQuestion.setNumberOfAnswers ( answers );
						
					flag = 20;
				}
				if ( local.equals ( "feedback" ) )
					flag = 22;
				if ( local.equals ( "status" ) )
					flag = 23;
				if ( local.equals ( "penalty" ) )
					flag = 25;
				if ( local.equals ( "evaluation" ) )
					flag = 26;
				break;
			case 25 :
					//
					//	penalty
					//
					/** Penalty of a incorrect Answer */
				if ( local.equals ( "response" ) )
				{
					answers++;
					fileQuestion.setCodeAnswer ( answers );
					fileQuestion.setCorrect ( "false" );
						
					fileQuestion.setNumberOfAnswers ( answers );
						
					flag = 20;
				}
				if ( local.equals ( "feedback" ) )
					flag = 22;
				if ( local.equals ( "status" ) )
					flag = 23;
				if ( local.equals ( "score" ) )
					flag = 24;
				if ( local.equals ( "evaluation" ) )
				{
					flag = 26;
				}
				break;
			case 26 :
					//
					//	evaluation
					//
					/** Here are the correct answers */
				if ( local.equals ( "right" ) )
					flag = 27;
				break;
			case 27 :
					//
					//	right
					//
					/** the correct Answer */
				if ( local.equals ( "responseid" ) )
				{
					for ( int i = 0; i < vCorrect.size ( ); i++ )
					{
						String buffer = (String)  vpCorrect.elementAt ( i );
						if ( local.equals ( buffer ) )
						{
							int corrTrue = ((Integer)(vCorrect.elementAt(i))).intValue();
							correctAnswers++;
							fileQuestion.setCorrect ( 0 , corrTrue - 1 , fileQuestion.getCodeQuestion () -1 , "true" );
							fileQuestion.setNumberOfCorrect ( correctAnswers );
						}
					}
					flag = 28;
				}
				break;
			case 28 :
					//
					//	responseid
					//
					/** the id of correct Answer */
				if ( local.equals ( "item" ) )
				{
					flag = 14;
					if ( numberOfQuestion > 0 )
					{
				        
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
				if ( local.equals ( "right" ) )
					flag = 27;
				if ( local.equals ( "responseid" ) )
				{
					for ( int i = 0; i < vCorrect.size ( ); i++ )
					{
						String buffer = (String)  vpCorrect.elementAt ( i );
						if ( local.equals ( buffer ) )
						{
							int corrTrue = ((Integer)(vCorrect.elementAt ( i ))).intValue();
							correctAnswers++;
							fileQuestion.setCorrect ( 0 , corrTrue - 1 , fileQuestion.getCodeQuestion () -1 , "true" );
							fileQuestion.setNumberOfCorrect ( correctAnswers );
						}
					}
					flag = 29;
				}
				break;
			case 29 :
					//
					//	responseid
					//
					/** the id of correct Answer */
				if ( numberOfQuestion > 0 )
				{

			        
					fileQuestion.setNumberOfAnswers ( answers );
					fileQuestion.setNumberOfCorrect	( correctAnswers );
					
					if (minAnswers > answers ) {
						minAnswers = answers;
						fileQuestion.setNumberOfAnswersForFile ( minAnswers );
					}
				}
				if ( local.equals ( "item" ) )
				{
					flag = 14;
					if ( numberOfQuestion > 0 )
					{
				        vpCorrect.removeAllElements ( );
				        vCorrect.removeAllElements ( );
				        
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
				if ( local.equals ( "right" ) )
					flag = 27;
				if ( local.equals ( "responseid" ) )
				{
					for ( int i = 0; i < vCorrect.size ( ); i++ )
					{
						String buffer = (String)  vpCorrect.elementAt ( i );
						if ( local.equals ( buffer ) )
						{
							int corrTrue = ((Integer)(vCorrect.elementAt ( i ))).intValue();
							correctAnswers++;
							fileQuestion.setCorrect ( 0 , corrTrue - 1 , fileQuestion.getCodeQuestion () -1 , "true" );
							fileQuestion.setNumberOfCorrect ( correctAnswers );
						}
					}
					flag = 28;
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
					case 1:
							//
							// Atributes of subjet ( id , topicid , xsi:noNamespaceSchemaLocation )
							//
						if ( attrName.equals ( "id" ) )
						{
							// Do Nothing
						}
						if ( attrName.equals ( "topicid" ) )
						{
							// Do Nothing
						}
						if ( attrName.equals ( "xsi:noNamespaceSchemaLocation" ) )
						{
							// Do Nothing
						}
						break;
					case 14:
							//
							// Attributes of item   ( id , idcollection , type )
							//
						if ( attrName.equals ( "id" ) )
						{
							// Do Nothing
						}
						if ( attrName.equals ( "idcollection" ) )
						{
							// Do Nothing
						}
						if ( attrName.equals ( "type" ) )
						{
							if ( Integer.parseInt ( attrValue ) != 4 && Integer.parseInt ( attrValue ) != 2 )
							{
								fileQuestion.setCodeQuestion ( 0 , fileQuestion.getCodeQuestion ( ) * -1 );
								fileQuestion.setEnunciate ( "type of question: " + attrValue );
							}
						}
						break;
					case 18:
							//
							// Attributes of iccparameters   ( difficulty , discrimination , guessing , topicid )
							//
						if ( attrName.equals ( "difficulty" ) )
						{
							fileQuestion.setDifficulty ( Float.parseFloat ( attrValue ) );
						}
						if ( attrName.equals ( "discrimination" ) )
						{
							fileQuestion.setDiscrimination ( Float.parseFloat ( attrValue ) );
						}
						if ( attrName.equals ( "guessing" ) )
						{
							fileQuestion.setGuessing ( Float.parseFloat ( attrValue ) );
						}
						if ( attrName.equals ( "topicid" ) )
						{
							// Do Nothing
						}
						break;
					case 20:
							//
							// Attributes of response ( id )
							//
						if ( attrName.equals ( "id" ) )
						{
							vpCorrect.addElement ( attrValue );
							vCorrect.addElement (new Integer ( answers ));
						}
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
				
				switch ( flag )
				{
					case 2 :
						//
						//	name
						//
						/**the Course*/
						if ( oldFlag == flag )
							value = fileQuestion.getCourse ( ) + value;
						oldFlag = flag;
						fileQuestion.setCourse ( value );
						break;
					case 15 :
						//
						//	title
						//
						/** Question`s name */
						if ( value.equals ( "title" ) )
							flag = 16;
						break;
					case 17 :
						//
						// stem
						//
						/** enunciate */
						if ( oldFlag == flag )
							value =  fileQuestion.getEnunciate ( ) + value;
						oldFlag = flag;
						fileQuestion.setEnunciate ( value );
						break;
					case 21 :
						//
						//	text
						//
						/** TextAnswer */
						if ( oldFlag == flag )
							value = fileQuestion.getTextAnswer ( ) +value;
						oldFlag = flag;
						fileQuestion.setTextAnswer ( value );
						fileQuestion.setCorrect ( "false" );
						break;
					case 22 :
						//
						//	feedback
						//
						/** Explanation */
						if ( oldFlag == flag )
							value = fileQuestion.getExplanation ( ) + value;
						oldFlag = flag;
						fileQuestion.setExplanation ( value );
						break;
					case 28 :
						//
						//	responseid
						//
						/** the id of correct Answer */
						for ( int i = 0; i < vCorrect.size ( ); i++ )
						{
							String buffer = (String)  vpCorrect.elementAt ( i );
							if ( value.equals ( buffer ) )
							{
								int corrTrue = (( Integer ) (vCorrect.elementAt ( i ))).intValue();
								correctAnswers++;
								fileQuestion.setCorrect ( 0 , corrTrue - 1 , fileQuestion.getCodeQuestion () -1, "true" );
								fileQuestion.setNumberOfCorrect ( correctAnswers );
							}
						}
						break;
					case 29 :
						//
						//	responseid
						//
						/** the id of correct Answer */
						for ( int i = 0; i < vCorrect.size ( ); i++ )
						{
							String buffer = (String)  vpCorrect.elementAt ( i );
							if ( value.equals ( buffer ) )
							{
								int corrTrue = ((Integer ) (vCorrect.elementAt ( i ))).intValue();
								correctAnswers++;
								fileQuestion.setCorrect ( 0 , corrTrue  , fileQuestion.getCodeQuestion () -1, "true" );
								fileQuestion.setNumberOfCorrect ( correctAnswers );
							}
						}
						break;
					default:
						/** An error**/
						break;
				}
			}
		} // characters(char[],int,int);


    //
    // Private  methods
    //

/*    
    NAME: FromSiette ( )
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

	public boolean storeEnunciateAndImage ( String image )
	{
		int i = 0;
		int startPosition = -1;
		int endPosition = -1;
		int initialPosition = -1;
		int finalPosition = -1;

		do {
			if ( ( i = image.indexOf ( "<" , startPosition + 1 ) ) < 0 )
				if ( ( i = image.indexOf ( "&lt;" , startPosition + 1 ) ) < 0 )
					return true;
				else
				{
					startPosition = i;
					i = i + 3;	
				}
			else
				startPosition = i;
			
			do {
				i++;
			} while ( image.charAt ( i ) == ' ' );
			if ( image.charAt ( i ) == 'i' )
			{
				i++;
				if ( image.charAt ( i ) == 'm' )
				{
					i++;
					if ( image.charAt ( i ) == 'g' )
					{
						do {
							i++;
						} while ( image.charAt ( i ) == ' ' );
						if ( image.charAt ( i ) == 's' )
						{
							i++;
							if ( image.charAt ( i ) == 'r' )
							{
								i++;
								if ( image.charAt ( i ) == 'c' )
								{
									do {
										i++;
									} while ( image.charAt ( i ) == ' ' );
									if ( image.charAt ( i ) == '=' )
									{
										do {
											i++;
										} while ( image.charAt ( i ) == ' ' );
										if ( image.charAt ( i ) != '\"' )
										{
											if ( image.charAt ( i ) != '&' )
											{
												i++;
												if ( image.charAt ( i ) != 'q' )
												{
													i++;
													if ( image.charAt ( i ) != 'u' )
													{
														i++;
														if ( image.charAt ( i ) != 'o' )
														{
															i++;
															if ( image.charAt ( i ) != 't' )
															{
																i++;
																if ( image.charAt ( i ) != ';' )
																{
																	i++;
																	initialPosition = i;
																}
															}
														}
													}
												}
											}
										}
										else
										{
											i++;
											initialPosition = i;
										}
									}
								}
							}
						}
					}
				}
			}
			if ( ( finalPosition = image.indexOf ( "\"" , initialPosition ) ) < 0 && finalPosition < image.indexOf ( "&quot;" , initialPosition ) )
			{
				if ( ( finalPosition = image.indexOf ( "&quot;" , initialPosition ) ) < 0 )
					i = finalPosition;
			}else
				i = finalPosition;
			if ( finalPosition >  0 )
			{
				if ( ( endPosition = image.indexOf ( ">" , i ) ) < 0 )
					if ( ( endPosition = image.indexOf ( "&gt;" , i ) ) < 0 )
						return false;
					else
						endPosition = endPosition + 3;

					if ( image.charAt ( i ) == '>' && i < image.length ( ) )
						endPosition = i;
				if ( endPosition > 0 )
				{
					fileQuestion.setImage ( replaceAmp ( image.substring ( initialPosition , finalPosition ) ) );
					fileQuestion.setEnunciate ( replaceAmp ( image.substring ( 0 , startPosition ) ) );
					
				}
			}
		}while (  i > -1 );
		
		return false;
		
	} // storeEnunciateAndImage ( String image )
	
	public FileOfQuestionsStruct getFileQuestion ( )
	{
		return fileQuestion;
	}

} // class FromSiette
