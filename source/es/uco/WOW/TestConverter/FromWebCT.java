package es.uco.WOW.TestConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import es.uco.WOW.Utils.UtilLog;

public class FromWebCT {

	protected int answers;

	protected int minAnswers;

	protected int correctAnswers;

	protected WriteWOW3 writeWOW3;
	
	protected FileOfQuestionsStruct fileQuestion;
	
	protected int flag;

	protected int oldFlag;
	
	protected String oldAttrib;

	protected String path;
	
	protected int numberOfQuestion;
    

	public FromWebCT ( ) {
		fileQuestion = new FileOfQuestionsStruct ( );
		writeWOW3 = new WriteWOW3 ( );


		answers = 0;
		minAnswers = 20;
		correctAnswers = 0;
		numberOfQuestion = 0;
	}

    //
    // MAIN
    //

    /** Main program entry point. */
    public boolean convert ( String inputFile , String outputFile )
    {

        	// process arguments
	            // use default parser?
	

    	try {
    		File p = new File ( inputFile );
    		fileQuestion.setPath		( p.getPath ( ) );
    		fileQuestion.setFileName	( p.getName ( ) );
    		fileQuestion.setCourse		( p.getName ( ) );
    	} catch ( Exception e ) {
    		UtilLog.writeException(e);
    	}

	    if ( readTextFile ( inputFile ) )
	    {
			if ( numberOfQuestion > 0 )
			{
				fileQuestion.setNumberOfCorrect	( correctAnswers );
				fileQuestion.setNumberOfAnswers ( answers );
	    		if ( writeWOW3.writeWOWFile ( fileQuestion , outputFile ) )
	    		{
	    			return true;
	    		}
	    		else
	    		{
	    			return false;
	    		}
			}
			else
	    	{
	    		return false;
	    	}
	    }
	    else
	    	return false;
	        
        

    } // convert (String , String )

    //
    // Private  methods
    //

    
	public boolean readTextFile(String File) {
		BufferedReader fileInput = null;
		String tmp = "";

		try {
			fileInput = new BufferedReader(new FileReader(File));
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		while (tmp != null) {
			try {
				tmp = fileInput.readLine();
			} catch (Exception e) {
				UtilLog.writeException(e);
				return false;
			}

			processLine(tmp);
		}
		return true;
	}
	
	public boolean processLine ( String tmp ) {
		if ( tmp == null )
			return true;
		
		System.out.print ( flag + " => " );

		switch ( flag )
		{
			case 0:
				if ( tmp.indexOf ( ":TYPE" ) == 0 )
				{
					if ( tmp.indexOf ( ":MC" ) == 5 )
						if ( tmp.indexOf ( ":N" ) == 8 || tmp.indexOf ( ":1" ) == 8 )
							if ( tmp.indexOf ( ":C" ) == 12 )
							{
								UtilLog.toLog( "Nueva Question" );
								UtilLog.toLog(String.valueOf(numberOfQuestion));

								flag = 1;

								if ( numberOfQuestion > 0 )
								{
									if ( minAnswers > answers ) {
							        	minAnswers = answers;
							        	fileQuestion.setNumberOfAnswersForFile ( minAnswers );
									}
									fileQuestion.setNumberOfCorrect	( correctAnswers );
									fileQuestion.setNumberOfAnswers	( answers );
								}
								
								numberOfQuestion++;
								fileQuestion.setCodeQuestion	( numberOfQuestion );
								
								answers = 0;
								correctAnswers = 0;
							}
							else
								if ( tmp.indexOf ( ":A" ) == 12 )
								{
									UtilLog.toLog ( "Nueva Question erronea" );
									flag = 1;
									if ( numberOfQuestion > 0 )
									{
										fileQuestion.setNumberOfCorrect	( correctAnswers );
										fileQuestion.setNumberOfAnswers	( answers );
									}
									
									numberOfQuestion++;
									fileQuestion.setCodeQuestion	( numberOfQuestion * -1 );
									
									answers = 0;
									correctAnswers = 0;
								}
				}
				else
					return false;
				break;
			case 1:
				if ( tmp.indexOf ( ":TITLE:" ) == 0 )
					flag = 2;
				else
					return false;
				break;
			case 2:
				if ( tmp.indexOf ( ":CAT:" ) == 0 ) 
				{
					fileQuestion.setCourse ( tmp.substring ( 5 ) );
					flag = 3;
				}
				else
					return false;
				break;
			case 3:
				if ( tmp.indexOf ( ":QUESTION:H" ) == 0 ) 
					flag = 4;
				else
					return false;
				break;
			case 4:
				if ( tmp.indexOf ( ":ANSWER" ) == 0 )
				{
					answers++;
					fileQuestion.setCodeAnswer ( answers );
					fileQuestion.setNumberOfAnswers ( answers );
					flag = tmp.indexOf ( ":" , 7 );
					oldFlag  = tmp.indexOf ( ":" , flag + 1 );
					if ( Float.parseFloat ( tmp.substring ( flag + 1 , oldFlag ) ) > 0 )
					{
						UtilLog.toLog(tmp.substring(flag + 1, oldFlag));
						fileQuestion.setCorrect ( "true" );
						correctAnswers++;
					}
					else
						fileQuestion.setCorrect ( "false" );

					UtilLog.toLog (tmp.substring (flag + 1 , oldFlag));
					flag = 5;
				}
				else
				{
					if ( fileQuestion.getEnunciate ( ) != null )
						tmp = fileQuestion.getEnunciate ( ) + tmp;
					fileQuestion.setEnunciate ( tmp );
				}
				break;
			case 5:
				if ( tmp.indexOf ( ":REASON" ) == 0 )
					flag = 6;
				else
				{
					if ( fileQuestion.getTextAnswer ( ) != null )
						tmp = fileQuestion.getTextAnswer ( ) + tmp;
					fileQuestion.setTextAnswer ( tmp );
				}
				break;
			case 6:
				if ( tmp.indexOf ( ":ANSWER" ) == 0 ) 
				{
					answers++;
					fileQuestion.setNumberOfAnswers ( answers );
					fileQuestion.setCodeAnswer ( answers );
					flag = tmp.indexOf ( ":" , 7 );
					oldFlag  = tmp.indexOf ( ":" , flag + 1 );
					if ( Float.parseFloat ( tmp.substring ( flag + 1 , oldFlag ) ) > 0 ) 
					{
						UtilLog.toLog (tmp.substring ( flag + 1 , oldFlag ));
						fileQuestion.setCorrect ( "true" );
						correctAnswers++;
					}
					else
						fileQuestion.setCorrect ( "false" );
					flag = 5;
				}
				else
				{
					
					if ( tmp.indexOf ( ":TYPE" ) == 0 )
					{
						if ( tmp.indexOf ( ":MC" ) == 5 )
							if ( tmp.indexOf ( ":N" ) == 8 || tmp.indexOf ( ":1" ) == 8 )
								if ( tmp.indexOf ( ":C" ) == 12 )
								{
									UtilLog.toLog( "Nueva Question" );
									UtilLog.toLog(String.valueOf(numberOfQuestion));
									flag = 1;
									if ( minAnswers > answers )
							        {
							        	minAnswers = answers;
							        	fileQuestion.setNumberOfAnswersForFile ( minAnswers );
							        }
									if ( numberOfQuestion > 0 )
									{
										fileQuestion.setNumberOfCorrect	( correctAnswers );
										fileQuestion.setNumberOfAnswers ( answers );
									}
									
									numberOfQuestion++;
									fileQuestion.setCodeQuestion	( numberOfQuestion );
									
									answers = 0;
									correctAnswers = 0;
								}
								else if ( tmp.indexOf ( ":A" ) == 12 )
								{
									UtilLog.toLog( "Nueva Question erronea" );
									flag = 1;
									if ( minAnswers > answers )
							        {
							        	minAnswers = answers;
							        	fileQuestion.setNumberOfAnswersForFile ( minAnswers );
							        }
									if ( numberOfQuestion > 0 )
									{
										fileQuestion.setNumberOfCorrect	( correctAnswers );
										fileQuestion.setNumberOfAnswers ( answers );
									}
									
									numberOfQuestion++;
									fileQuestion.setCodeQuestion	( numberOfQuestion * -1 );
									
									answers = 0;
									correctAnswers = 0;
								}
					}
					else
					{
						if ( fileQuestion.getExplanation ( ) != null )
							tmp = fileQuestion.getExplanation ( ) + tmp;
						fileQuestion.setExplanation	( tmp );
					}
				}
				break;
		}
		UtilLog.toLog (String.valueOf(flag));
		
		return true;
	} // processLine ( String )

} // class FromWebCT
