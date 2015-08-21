package es.uco.WOW.TestConverter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import es.uco.WOW.Utils.UtilLog;

public class HotPotatoesv6 
	extends DefaultHandler {
	
	// My Data

    protected int flag;

	protected int oldFlag;

	protected int numberOfQuestion;


	/** The FileOfQuestionsStruct element */
	protected FileOfQuestionsStruct fileQuestion;

	protected String oldAttrib;

    protected  int answers;
    protected  int minAnswers;
    protected  int correctAnswers;
    

	public HotPotatoesv6 ( ) {
	}



    /** Main program entry point. */
 
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
    
	public void startElement(String uri, String local, String raw, Attributes attrs) throws SAXException {

		System.out.print ( local + " ( " + flag + " ) -> ");
		switch ( flag )
		{
			case 0 :
				//
				// The HotPotatoes v.6 File is start
				//
				if ( local.equals ( "hotpot-jquiz-file" ) )
					flag++;
				break;
			case 1 :
				//
				//	hotpot-jquiz-file
				//
				if ( local.equals ( "RDF" ) )
					flag++;
				break;
			case 2 :
				//	rdf:RDF
				//
				if ( local.equals ( "Description" ) )
					flag++;
				break;
			case 3 :
				//
				//	rdf:Description
				//
				if ( local.equals ( "creator" ) )
					flag++;
				break;
			case 4 :
				//
				//	dc:creator
				//
				if ( local.equals ( "title" ) )
					flag++;
				break;
			case 5 :
				//
				//	dc:title
				//
				if ( local.equals ( "version" ) )
					flag++;
				break;
			case 6 :
				//
				//	version
				//
				if ( local.equals ( "data" ) )
					flag++;
				break;
			case 7 :
				//
				//
				//	data
				if ( local.equals ( "title" ) )
					flag++;
				break;
			case 8 :
				//
				//	title
				//
				if ( local.equals ( "timer" ) )
					flag++;
				break;
			case 9 :
				//
				//	timer
				//
				if ( local.equals ( "seconds" ) )
					flag++;
				break;
			case 10 :
				//
				//	seconds
				//
				if ( local.equals ( "include-timer" ) )
					flag++;
				break;
			case 11 :
				//
				//	include-timer
				//
				if ( local.equals ( "reading" ) )
					flag++;
				break;
			case 12 :
				//
				//	reading
				//
				if ( local.equals ( "include-reading" ) )
					flag++;
				break;
			case 13 :
				//
				//	include-reading
				//
				if ( local.equals ( "reading-title" ) )
					flag++;
				break;
			case 14 :
				//
				//	reading-title
				//
				if ( local.equals ( "reading-text" ) )
					flag++;
				break;
			case 15 :
				//
				//	reading-text
				//
				if ( local.equals ( "questions" ) )
					flag++;
				break;
			case 16 :
				//
				//	questions
				//
				if ( local.equals ( "question-record" ) )
				{
					flag = 17;
					if (numberOfQuestion > 0 )
					{
				        if ( minAnswers > answers )
				        {
				        	minAnswers = answers;
				        	fileQuestion.setNumberOfAnswersForFile (minAnswers);
				        }
				        fileQuestion.setNumberOfCorrect(correctAnswers);
				        fileQuestion.setNumberOfAnswers(answers);
					}
					numberOfQuestion++;
					
					fileQuestion.setCodeQuestion ( numberOfQuestion );
					
			        answers = 0;
			        correctAnswers = 0;
				}
				break;
			case 17 :
				//
				//	question-record
				//
				if ( local.equals ( "question" ) )
					flag = 18;
				break;
			case 18 :
				//
				//	question
				//
				if ( local.equals ( "clue" ) )
				{
					flag = 19;
				}
				break;
			case 19 :
				//
				//	clue
				//
				if ( local.equals ( "category" ) )
					flag++;
				break;
			case 20 :
				//
				//	category
				//
				if ( local.equals ( "weighting" ) )
					flag++;
				break;
			case 21 :
				//
				//	weighting
				//
				if ( local.equals ( "fixed" ) )
					flag++;
				break;
			case 22 :
				//
				//	fixed
				//
				if ( local.equals ( "question-type" ) )
					flag++;
				break;
			case 23 :
				//
				//	question-type
				//
				if ( local.equals ( "answers" ) )
					flag++;
				break;
			case 24 :
				//
				//	answers
				//
				if ( local.equals ( "answer" ) )
				{
					flag++;
					answers++;
					fileQuestion.setCodeAnswer ( answers );
				}
					
				break;
			case 25 :
				//
				//	answer
				//
				if ( local.equals ( "text" ) )
					flag++;
				break;
			case 26 :
				//
				//	text
				//
				if ( local.equals ( "feedback" ) )
					flag++;
				break;
			case 27 :
				//
				//	feedback
				//
				if ( local.equals ( "correct" ) )
					flag++;
				break;
			case 28 :
				//
				//	correct
				//
				if ( local.equals ( "percent-correct" ) )
					flag++;
				break;
			case 29 :
				//
				//	percent-correct
				//
				if ( local.equals ( "include-in-mc-options" ) )
					flag++;
				break;
			case 30 :
				//
				//	include-in-mc-options
				//
				if ( local.equals ( "must-select-all" ) )
				{
					flag = 31;
					if (numberOfQuestion > 0 )
					{
				        if ( minAnswers > answers )
				        {
				        	minAnswers = answers;
				        	fileQuestion.setNumberOfAnswersForFile (minAnswers);
				        }
				        fileQuestion.setNumberOfCorrect(correctAnswers);
				        fileQuestion.setNumberOfAnswers(answers);
					}
					
				}
				if ( local.equals ( "answer" ) )
				{
					flag = 25;
					answers++;
					fileQuestion.setNumberOfAnswers(answers);
					fileQuestion.setCodeAnswer ( answers );
				}
				if ( local.equals ( "question-record" ) )
				{
					flag = 17;
					if (numberOfQuestion > 0 )
					{
				        if ( minAnswers > answers )
				        {
				        	minAnswers = answers;
				        	fileQuestion.setNumberOfAnswersForFile (minAnswers);
				        }
				        fileQuestion.setNumberOfCorrect(correctAnswers);
				        fileQuestion.setNumberOfAnswers(answers);
					}
					numberOfQuestion++;
					
					fileQuestion.setCodeQuestion ( numberOfQuestion );
					
			        answers = 0;
			        correctAnswers = 0;
				}
				break;
			case 31 :
				//
				//	must-select-all
				//
				if ( local.equals ( "question-record" ) )
				{
					flag = 17;
					if (numberOfQuestion > 0 )
					{
				        if ( minAnswers > answers )
				        {
				        	minAnswers = answers;
				        	fileQuestion.setNumberOfAnswersForFile (minAnswers);
				        }
				        fileQuestion.setNumberOfCorrect(correctAnswers);
				        fileQuestion.setNumberOfAnswers(answers);
					}
					numberOfQuestion++;
					
					fileQuestion.setCodeQuestion ( numberOfQuestion );
					
			        answers = 0;
			        correctAnswers = 0;
				}
				if ( local.equals ( "answer" ) )
				{
					flag = 25;
					answers++;
					fileQuestion.setNumberOfAnswers(answers);
					fileQuestion.setCodeAnswer ( answers );
				}
				if ( local.equals ( "hotpot-config-file" ) )
				{
					flag++;
					if (numberOfQuestion > 0 )
					{
				        if ( minAnswers > answers )
				        {
				        	minAnswers = answers;
				        	fileQuestion.setNumberOfAnswersForFile (minAnswers);
				        }
				        fileQuestion.setNumberOfCorrect(correctAnswers);
				        fileQuestion.setNumberOfAnswers(answers);
					}
					
			        answers = 0;
			        correctAnswers = 0;
				}
				break;
			case 32 :
				//
				//	hotpot-config-file
				//
				
				if ( local.equals ( "jquiz" ) )
					flag++;
				break;
			case 33 :
				//
				//	jquiz
				//
				if ( local.equals ( "exercise-subtitle" ) )
					flag++;
				break;
			case 34 :
				//
				//	exercise-subtitle
				//
				if ( local.equals ( "instructions" ) )
					flag++;
				break;
			case 35 :
				//
				//	instructions
				//
				if ( local.equals ( "guess-correct" ) )
					flag++;
				break;
			case 36 :
				//
				//	guess-correct
				//
				if ( local.equals ( "guess-incorrect" ) )
					flag++;
				if ( local.equals ( "partly-incorrect" ) )
					flag= 38;
				break;
			case 37 :
				//
				//	guess-incorrect
				//
				if ( local.equals ( "partly-incorrect" ) )
					flag++;
				break;
			case 38 :
				//
				//	partly-incorrect
				//
				if ( local.equals ( "enter-a-guess" ) )
					flag++;
				break;
			case 39 :
				//
				//	enter-a-guess
				//
				if ( local.equals ( "next-correct-letter" ) )
					flag++;
				break;
			case 40 :
				//
				//	next-correct-letter
				//
				if ( local.equals ( "correct-answers" ) )
					flag++;
				break;
			case 41 :
				//
				//	correct-answers
				//
				if ( local.equals ( "show-answer-caption" ) )
					flag++;
				break;
			case 42 :
				//
				//	show-answer-caption
				//
				if ( local.equals ( "include-hint" ) )
					flag++;
				break;
			case 43 :
				//
				//	include-hint
				//
				if ( local.equals ( "include-show-answer" ) )
					flag++;
				break;
			case 44 :
				//
				//	include-show-answer
				//
				if ( local.equals ( "next-ex-url" ) )
					flag++;
				break;
			case 45 :
				//
				//	next-ex-url
				//
				if ( local.equals ( "send-email" ) )
					flag++;
				break;
			case 46 :
				//
				//	send-email
				//
				if ( local.equals ( "continuous-scoring" ) )
					flag++;
				break;
			case 47 :
				//
				//	continuous-scoring
				//
				if ( local.equals ( "show-correct-first-time" ) )
					flag++;
				break;
			case 48 :
				//
				//	show-correct-first-time
				//
				if ( local.equals ( "shuffle-questions" ) )
					flag++;
				break;
			case 49 :
				//
				//	shuffle-questions
				//
				if ( local.equals ( "shuffle-answers" ) )
					flag++;
				break;
			case 50 :
				//
				//	shuffle-answers
				//
				if ( local.equals ( "show-limited-questions" ) )
					flag++;
				break;
			case 51 :
				//
				//	show-limited-questions
				//
				if ( local.equals ( "questions-to-show" ) )
					flag++;
				break;
			case 52 :
				//
				//	questions-to-show
				//
				if ( local.equals ( "short-answer-tries-on-hybrid-q" ) )
					flag++;
				break;
			case 53 :
				//
				//	short-answer-tries-on-hybrid-q
				//
				if ( local.equals ( "separate-javascript-file" ) )
					flag++;
				break;
			case 54 :
				//
				//	separate-javascript-file
				//
				if ( local.equals ( "case-sensitive" ) )
					flag++;
				break;
			case 55 :
				//
				//	case-sensitive
				//
				if ( local.equals ( "include-keypad" ) )
					flag++;
				break;
			case 56 :
				//
				//	include-keypad
				//
				if ( local.equals ( "global" ) )
					flag++;
				break;
			case 57 :
				//
				//	global
				//
				if ( local.equals ( "your-score-is" ) )
					flag++;
				break;
			case 58 :
				//	your-score-is
				if ( local.equals ( "correct-indicator" ) )
					flag++;
				break;
			case 59 :
				//
				//	correct-indicator
				//
				if ( local.equals ( "incorrect-indicator" ) )
					flag++;
				break;
			case 60 :
				//
				//	incorrect-indicator
				//
				if ( local.equals ( "correct-first-time" ) )
					flag++;
				break;
			case 61 :
				//
				//	correct-first-time
				//
				if ( local.equals ( "keypad-characters" ) )
					flag++;
				break;
			case 62 :
				//
				//	keypad-characters
				//
				if ( local.equals ( "times-up" ) )
					flag++;
				break;
			case 63 :
				//
				//	times-up
				//
				if ( local.equals ( "next-ex-caption" ) )
					flag++;
				break;
			case 64 :
				//
				//	next-ex-caption
				//
				if ( local.equals ( "back-caption" ) )
					flag++;
				break;
			case 65 :
				//
				//	back-caption
				//
				if ( local.equals ( "contents-caption" ) )
					flag++;
				break;
			case 66 :
				//
				//	contents-caption
				//
				if ( local.equals ( "include-next-ex" ) )
					flag++;
				break;
			case 67 :
				//
				//	include-next-ex
				//
				if ( local.equals ( "include-contents" ) )
					flag++;
				break;
			case 68 :
				//
				//	include-contents
				//
				if ( local.equals ( "include-back" ) )
					flag++;
				break;
			case 69 :
				//
				//	include-back
				//
				if ( local.equals ( "contents-url" ) )
					flag++;
				break;
			case 70 :
				//
				//	contents-url
				//
				if ( local.equals ( "graphic-url" ) )
					flag++;
				break;
			case 71 :
				//	
				//graphic-url
				//
				if ( local.equals ( "font-face" ) )
					flag++;
				break;
			case 72 :
				//
				//	font-face
				//
				if ( local.equals ( "font-size" ) )
					flag++;
				break;
			case 73 :
				//
				//	font-size
				//
				if ( local.equals ( "page-bg-color" ) )
					flag++;
				break;
			case 74 :
				//
				//	page-bg-color
				//
				if ( local.equals ( "title-color" ) )
					flag++;
				break;
			case 75 :
				//
				//	title-color
				//
				if ( local.equals ( "ex-bg-color" ) )
					flag++;
				break;
			case 76 :
				//	ex-bg-color
				if ( local.equals ( "text-color" ) )
					flag++;
				break;
			case 77 :
				//
				//	text-color
				//
				if ( local.equals ( "link-color" ) )
					flag++;
				break;
			case 78 :
				//
				//	link-color
				//
				if ( local.equals ( "vlink-color" ) )
					flag++;
				break;
			case 79 :
				//
				//	vlink-color
				//
				if ( local.equals ( "nav-bar-color" ) )
					flag++;
				break;
			case 80 :
				//
				//	nav-bar-color
				//
				if ( local.equals ( "formmail-url" ) )
					flag++;
				break;
			case 81 :
				//	formmail-url
				//
				if ( local.equals ( "email" ) )
					flag++;
				break;
			case 82 :
				//
				//	email
				//
				if ( local.equals ( "name-please" ) )
					flag++;
				break;
			case 83 :
				//
				//	name-please
				//
				if ( local.equals ( "user-string-1" ) )
					flag++;
				break;
			case 84 :	
				//
				//	user-string-1
				//
				if ( local.equals ( "user-string-2" ) )
					flag++;
				break;
			case 85 :
				//
				//	user-string-2
				//
				if ( local.equals ( "user-string-3" ) )
					flag++;
				break;
			case 86 :
				//
				//	user-string-3
				//
				if ( local.equals ( "header-code" ) )
					flag++;
				break;
			case 87 :
				//
				//	header-code
				//
				if ( local.equals ( "correct-first-time" ) )
					flag++;
				break;
			case 88 :
				//
				//	correct-first-time
				//
				if ( local.equals ( "check-caption" ) )
					flag++;
				break;
			case 89 :
				//
				//	check-caption
				//
				if ( local.equals ( "ok-caption" ) )
					flag++;
				break;
			case 90 :
				//
				//	ok-caption
				if ( local.equals ( "next-q-caption" ) )
					flag++;
				break;
			case 91 :
				//
				//	next-q-caption
				//
				if ( local.equals ( "last-q-caption" ) )
					flag++;
				break;
			case 92 :
				//
				//	last-q-caption
				//
				if ( local.equals ( "hint-caption" ) )
					flag++;
				break;
			case 93 :
				//
				//	hint-caption
				//
				if ( local.equals ( "show-all-questions-caption" ) )
					flag++;
				break;
			case 94 :
				//
				//	show-all-questions-caption
				if ( local.equals ( "show-one-by-one-caption" ) )
					flag++;
				break;
			case 95 :
				//
				//	show-one-by-one-caption
				//
				if ( local.equals ( "show-also-correct" ) )
					flag++;
				break;
			case 96 :
				//
				//	show-also-correct
				//
				if ( local.equals ( "process-for-rtl" ) )
					flag++;
				break;
			default:
				/** An error**/
				break;
		}
		UtilLog.toLog(String.valueOf(flag));
//		if (attrs != null)
//		{
//			int attrCount = attrs.getLength ( );
//			String attrName = null;
//			String attrValue = null;
//			
//			for ( int i = 0; i < attrCount; i++ )
//			{
//				attrName  = attrs.getQName ( i );
//				attrValue = attrs.getValue ( i );
//				if ( attrValue == null )
//					;
//				
//				switch ( flag ) {
//					case 2:
//							// Attributes of rdf:RDF ( xmlns:rdf , xmlns:dc )
//						if ( attrName.equals ( "xmlns:rdf" ) )
//						{
//							;
//						}
//						if ( attrName.equals ( "xmlns:dc" ) )
//						{
//							;
//						}
//						break;
//					case 8:
//							// Attributes of rdf:Description ( rdf:about )
//						if ( attrName.equals ( "rdf:about" ) )
//						{
//							;
//						}
//						break;
//					default:
//							/** An error**/
//							break;
//				}
//			}
//		}

	} // startElement ( String , String , StringAttributes )

	public void endDocument() {
	}

	/** Characters. */
	public void characters(char characters[], int start, int length) throws SAXException {

		String value = "";

		if (length > 0) {
			for (int i = start; i < start + length; i++) {
				if (characters[i] != '\n')
					value = value + characters[i];
			}
			value = replaceAmp ( value );
			if ( value.equals ( "" ) )
				return;

			switch ( flag ) {
				case 18:
						/** enumciate **/
					if ( oldFlag == flag )
						value =  fileQuestion.getEnunciate ( ) + value;
					oldFlag = flag;
					fileQuestion.setEnunciate ( value );
					UtilLog.toLog( fileQuestion.getEnunciate ( ) );
					break;
				case 19:
					UtilLog.toLog ( "" );
					UtilLog.toLog ( fileQuestion.getEnunciate ( ) );
					storeEnunciateAndImage ( fileQuestion.getEnunciate ( ) );
					break;
				case 23:
						/**question-type**/
					UtilLog.toLog ( "" );
					UtilLog.toLog ( fileQuestion.getEnunciate ( ) );
					storeEnunciateAndImage ( fileQuestion.getEnunciate ( ) );
					UtilLog.toLog ( fileQuestion.getEnunciate ( ) );
					if ( Integer.parseInt ( value ) !=  4 )
					{
						fileQuestion.setCodeQuestion ( 0 , fileQuestion.getCodeQuestion ( ) * -1 );
						fileQuestion.setEnunciate ( "type of question: " + value );
					}
					
					break;
					
				case 26:
						/** textAnswer **/ 
					if ( flag  == oldFlag )
						if ( fileQuestion.getTextAnswer ( ) != null )
						value = fileQuestion.getTextAnswer ( ) + value;

						
					oldFlag = flag;
					
					fileQuestion.setTextAnswer ( value );
					break;
				case 27:
						/** explanation **/
					
					if ( flag  == oldFlag )
						value = fileQuestion.getExplanation ( ) + value;
					
					oldFlag = flag;
					
					fileQuestion.setExplanation ( value );
					break;
				case 28:
					/** correct **/
					if ( Integer.parseInt ( value ) != 1 )
						value = "false";
					else
					{
						value = "true";
						correctAnswers++;
					}
					fileQuestion.setCorrect ( value );
					break;
				default:
					/** An error **/
					break;
			}
		}
	} // characters(char[],int,int);

    //
    // Private  methods
    //

    
 /*    
    NAME: HotPotatoesv6 ( )
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
	
} // class HotPotatoesv6
