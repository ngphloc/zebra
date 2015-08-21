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

public class ToQti20 {
	
    // feature ids

    /** Namespaces feature id (http://xml.org/sax/features/namespaces). */
    protected final String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";

    /** Namespace prefixes feature id (http://xml.org/sax/features/namespace-prefixes). */
    protected final String NAMESPACE_PREFIXES_FEATURE_ID = "http://xml.org/sax/features/namespace-prefixes";

    /** Validation feature id (http://xml.org/sax/features/validation). */
    protected final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";

    /** Schema validation feature id (http://apache.org/xml/features/validation/schema). */
    protected final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";

    /** Schema full checking feature id (http://apache.org/xml/features/validation/schema-full-checking). */
    protected final String SCHEMA_FULL_CHECKING_FEATURE_ID = "http://apache.org/xml/features/validation/schema-full-checking";

    /** Dynamic validation feature id (http://apache.org/xml/features/validation/dynamic). */
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
    //	My Data
    //
    
	protected String inputPath;

	protected String outputPath;
	
	protected String inputsubPath;
    
    protected ReadWOW3 toQti20;

	public ToQti20 ( ) {
	}

    //
    // MAIN
    //

    /** Main program entry point. */
    public boolean convert ( String inputFile , String outputFile )
    {

        // variables
        toQti20 = new ReadWOW3 ( );
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
	            }
	            catch (SAXException e) {
	            	UtilLog.writeException(e);
	            }
	            try {
	                parser.setFeature(NAMESPACE_PREFIXES_FEATURE_ID, namespacePrefixes);
	            }
	            catch (SAXException e) {
	            	UtilLog.writeException(e);
	            }
	            try {
	                parser.setFeature(VALIDATION_FEATURE_ID, validation);
	            }
	            catch (SAXException e) {
	            	UtilLog.writeException(e);
	            }
	            try {
	                parser.setFeature(SCHEMA_VALIDATION_FEATURE_ID, schemaValidation);
	            }
	            catch (SAXNotRecognizedException e) {
	            	UtilLog.writeException(e);
	
	            }
	            catch (SAXNotSupportedException e) {
	            	UtilLog.writeException(e);
	            }
	            try {
	                parser.setFeature(SCHEMA_FULL_CHECKING_FEATURE_ID, schemaFullChecking);
	            }
	            catch (SAXNotRecognizedException e) {
	            	UtilLog.writeException(e);
	
	            }
	            catch (SAXNotSupportedException e) {
	            	UtilLog.writeException(e);
	            }
	            try {
	                parser.setFeature(DYNAMIC_VALIDATION_FEATURE_ID, dynamicValidation);
	            }
	            catch (SAXNotRecognizedException e) {
	            	UtilLog.writeException(e);
	
	            }
	            catch (SAXNotSupportedException e) {
	            	UtilLog.writeException(e);
	            }
	
	            // parse file
	            parser.setContentHandler ( toQti20 );
	            parser.setErrorHandler   ( toQti20 );                

	        try {
	            parser.parse ( inputFile );
	        } catch (Exception e) {
		   		UtilLog.writeException(e);
	        }  

        return makeQti20Files(outputFile );

    } // convert ( String , String )

    //
    // Private methods
    //


/*    
    NAME: makeFiles ( )
    IT'S FROM: Class ToQti20.
    CALLED FROM: Main program 
    CALL TO:
    		from MyParser Class:
    			int getNumberOfQuestions ( )
    			String getName ( )
    			int getCodeQuestion ( int positionQuestion )
    			String getAdaptative ( int positionQuestion )
	    		String getConcept ( )
	    		int getNumberOfAnswers ( int positionQuestion )
	    		int getNumberOfCorrect ( int positionQuestion )
	    		int getCodeAnswer ( int positionQuestion , int positionAnswer )
	    		String getExplanation ( int positionQuestion , int positionAnswer )
				String getTextAnswer ( int positionQuestion , int positionAnswer )
				String getEnunciate ( int positionQuestion )
				String getImage ( int positionQuestion )
	    		
    recibed:
            String FileName
    RETURN:
    		boolean
    FUNCTION:
    		Print why call the program.
    		( Only Can write simple and multiple choice with or without image ).
    */
    public boolean makeQti20Files ( String FileName )
	{
		BufferedWriter quest;
		int numberOfAnswers;
		int numberOfQuestions = toQti20.getNumberOfQuestions ( );
		int numberOfCorrect;
		String altText = "";
		String buffer = "";
		String title = "";
		String timeDepent = "true";
		String adaptative = "false";
		boolean Explanation = false;
		
		if ( numberOfQuestions < 1 || toQti20.flag < 15 )
			return false;
		
		for ( int positionQuestion = 0 ; positionQuestion <= numberOfQuestions ; positionQuestion++ )
		{
			//
			// Here we create a Questions File type Qti 2.0
			//
			quest = null;

			try {
            quest = new BufferedWriter(new FileWriter(FileName + positionQuestion + ".xml"));
			} catch (Exception e) {
				UtilLog.writeException(e);
				return false;
			}
	
			buffer = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
			buffer = "<!-- This is a result of EXPORT WOW system -->";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog ("error: Unable writer in a File." );
        	}
			buffer = "<assessmentItem xmlns=\"http://www.imsglobal.org/xsd/imsqti_v2p0\"";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
			buffer = "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
			buffer = "\txsi:schemaLocation=\"http://www.imsglobal.org/xsd/imsqti_v2p0 imsqti_v2p0.xsd\"";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
			if ( toQti20.getAdaptative ( positionQuestion ) != null )
				adaptative = "true";
			buffer = "\tidentifier=\""
				+ toQti20.getName ( ) + toQti20.getCodeQuestion ( positionQuestion )
	    		+ "\" title=\""
	    		+ toQti20.getConcept ( )
	    		+ "\" adaptive=\""
	    		+ adaptative
	    		+ "\" timeDependent=\""
	    		+ timeDepent
	    		+ "\">";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
			buffer = "\t<responseDeclaration identifier=\"RESPONSE\" cardinality=\"single\" baseType=\"identifier\">";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
			buffer = "\t\t<correctResponse>";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
			numberOfAnswers = toQti20.getNumberOfAnswers ( positionQuestion );
			numberOfCorrect = toQti20.getNumberOfCorrect ( positionQuestion );
			for ( int positionAnswer = 0 ; positionAnswer < numberOfAnswers ; positionAnswer++ )
			{
					//
					// Here write in File the correct Answer
					//
				buffer = toQti20.getCorrect ( positionQuestion , positionAnswer );
				if ( buffer.compareTo ( "true" ) == 0 )
				{
					buffer = "\t\t\t<value>Choice"
							+ toQti20.getCodeAnswer ( positionQuestion , positionAnswer )
							+ "</value>";
					
					try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
				}
				if ( toQti20.getExplanation ( positionQuestion , positionAnswer ) != null )
				{
					Explanation = true;
				}
			}
			numberOfCorrect = toQti20.getNumberOfCorrect ( positionQuestion );
			buffer = "\t\t</correctResponse>";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
			if ( numberOfCorrect > 1 )
			{
				buffer = "\t\t<mapping lowerBound=\"0\" upperBound=\""
						+ ( toQti20.getNumberOfCorrect ( positionQuestion ) + 1 )
						+ "\" defaultValue=\"-" + ( toQti20.getNumberOfCorrect ( positionQuestion ) + 1 )
						+ "\">";
				
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.toLog("error: Unable writer in a File." );
	        	}
				numberOfAnswers = toQti20.getNumberOfAnswers ( positionQuestion );
				numberOfCorrect = toQti20.getNumberOfCorrect ( positionQuestion );
				for ( int positionAnswer = 0 ; positionAnswer < numberOfAnswers ; positionAnswer++ )
				{
					buffer = toQti20.getCorrect ( positionQuestion , positionAnswer );
					if ( buffer.compareTo ( "true" ) == 0 )
						buffer = "1\" />";
					else
						buffer = "-1\" />";
					buffer = "\t\t\t<mapEntry mapKey=\""
						+ toQti20.getCodeAnswer ( positionQuestion , positionAnswer )
						+ "\" mappedValue=\"" + buffer ;
					
					try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
				}
				
				buffer = "</mapping>";
				
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.toLog("error: Unable writer in a File." );
	        	}
	    		buffer = "";
			}
			buffer = "\t</responseDeclaration>";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
			buffer = "\t<outcomeDeclaration identifier=\"SCORE\" cardinality=\"single\" baseType=\"integer\">";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
        	if  ( Explanation )
        	{
        		buffer = "<outcomeDeclaration identifier=\"FEEDBACK\" cardinality=\"single\" baseType=\"identifier\"/>";
        		
            	try {
    				quest.write ( buffer , 0 , buffer.length ( ) );
    				quest.newLine ( );
    			}
            	catch (Exception e) {
            		UtilLog.toLog("error: Unable writer in a File." );
            	}
        	}
			buffer = "\t\t<defaultValue>";
			
			  try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
			catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File." );
			}
			buffer = "\t\t\t<value>0</value>";
			 try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
			catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File." );
			}
			buffer = "\t\t</defaultValue>";
			 try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
			catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File." );
			}
			buffer = "\t</outcomeDeclaration>";
			 try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
			catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File." );
			}
			buffer = "\t<itemBody>";
			 try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
			catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File." );
			}
			buffer = "";
			if ( title.compareTo ( "" ) != 0 )
			{
				buffer = "\t<p>" + title + "</p>";
				 try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.toLog("error: Unable writer in a File." );
	        	}
				buffer = "";
			}
        	if ( toQti20.getImage ( positionQuestion ) != "" )
        	{
    			buffer = "\t\t<p>";
    			 try {
    				quest.write ( buffer , 0 , buffer.length ( ) );
    				quest.newLine ( );
    			}
            	catch (Exception e) {
            		UtilLog.toLog("error: Unable writer in a File." );
            	}
            	altText = ".\\images" + getImage ( positionQuestion );
	        	buffer = "\t\t\t<img src=\""
				+ altText
				+ "\" alt=\""
				+ altText
				+ "\"/>";
				 try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
		    	catch (Exception e) {
		    		UtilLog.toLog("error: Unable writer in a File." );
		    	}
		    	buffer = "\t\t</p>";
				 try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
				catch (Exception e) {
					UtilLog.toLog("error: Unable writer in a File." );
				}
        	}
	        buffer = "\t\t<choiceInteraction responseIdentifier=\"RESPONSE\" shuffle=\""
	        	+ "true" + "\" maxChoices=\"";
	        if ( toQti20.getNumberOfCorrect ( positionQuestion ) > 1 )
	        	buffer = buffer + "0\">";
	        else
	        	buffer = buffer + "1\">";
	         try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
			catch (Exception e) {
				UtilLog.toLog("error: Unable writer in a File." );
			}
			buffer = "";
			buffer = "\t\t\t<prompt>";
			 try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
			buffer = "";
			buffer = "\t\t\t\t" + toQti20.getEnunciate ( positionQuestion );
			 try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
			buffer = "";
			buffer = "\t\t\t</prompt>";
			 try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
			numberOfAnswers = toQti20.getNumberOfAnswers ( positionQuestion );
			numberOfCorrect = toQti20.getNumberOfCorrect ( positionQuestion );
			for ( int positionAnswer = 0 ; positionAnswer < numberOfAnswers ; positionAnswer++ )
			{
					//
					// Here write in File the correct Answer
					//
				buffer = "\t\t\t<simpleChoice identifier=\"Choice"
					+ toQti20.getCodeAnswer ( positionQuestion , positionAnswer )
					+ "\">"
					+ toQti20.getTextAnswer ( positionQuestion , positionAnswer );
				 try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
		        catch (Exception e) {
		        	UtilLog.toLog("error: Unable writer in a File." );
		        }
		        if  ( Explanation )
		        {
					buffer = "\t\t\t\t<feedbackInline outcomeIdentifier=\"FEEDBACK\" identifier=\"Choice"
					+ toQti20.getCodeAnswer ( positionQuestion , positionAnswer )
					+ "\" showHide=\"show\">"
					+ toQti20.getExplanation ( positionQuestion , positionAnswer )
					+ "</feedbackInline>";
					
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
			        catch (Exception e) {
			        	UtilLog.toLog("error: Unable writer in a File." );
			        }
	        	}
		        
				buffer = "\t\t\t</simpleChoice>";
				
				 try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
	        	}
			}
			
			numberOfAnswers = toQti20.getNumberOfAnswers ( positionQuestion );
			
			buffer = "\t\t</choiceInteraction>";
			
			 try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}

			buffer = "\t</itemBody>";
			
			 try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
        	
			buffer = "";

			if ( numberOfCorrect > 1 )
			{
				buffer = "\t<responseProcessing";
				
				 try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.toLog("error: Unable writer in a File." );
	        	}
	        	
				buffer = "\t\ttemplate=\"http://www.imsglobal.org/question/qti_v2p0/rptemplates/map_response\"/>";
				
				 try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.toLog("error: Unable writer in a File." );
	        	}
	    		buffer = "";
			}
			else
			{
				if ( Explanation )
				{
					buffer = "\t<responseProcessing>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t<responseCondition>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t<responseIf>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t\t<match>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t\t\t<variable identifier=\"RESPONSE\"/>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
		        	buffer = "\t\t\t\t\t<correct identifier=\"RESPONSE\"/>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
		        	buffer = "\t\t\t\t</match>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t\t<setOutcomeValue identifier=\"SCORE\">";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t\t\t<baseValue baseType=\"integer\">";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t\t\t\t1";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t\t\t</baseValue>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
		        	buffer = "\t\t\t\t</setOutcomeValue>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
		        	buffer = "\t\t\t</responseIf>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
		        	buffer = "\t\t\t\t<responseElse>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t\t<setOutcomeValue identifier=\"SCORE\">";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t\t\t<baseValue baseType=\"integer\">";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t\t\t\t0";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t\t\t</baseValue>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
		        	buffer = "\t\t\t\t</setOutcomeValue>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t\t</responseElse>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t</responseCondition>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t<setOutcomeValue identifier=\"FEEDBACK\">";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t\t<variable identifier=\"RESPONSE\"/>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t\t</setOutcomeValue>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\t</responseProcessing>";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					numberOfAnswers = toQti20.getNumberOfAnswers ( positionQuestion );
					numberOfCorrect = toQti20.getNumberOfCorrect ( positionQuestion );
					for ( int positionAnswer = 0 ; positionAnswer < numberOfAnswers ; positionAnswer++ )
					{
						buffer = toQti20.getCorrect ( positionQuestion , positionAnswer );
						if ( buffer.compareTo ( "true" ) == 0 )
						{
							buffer = "\t\t<modalFeedback outcomeIdentifier=\"FEEDBACK\" identifier=\"Choice"
									+ toQti20.getCodeAnswer ( positionQuestion , positionAnswer )
									+ "\" showHide=\"show\">"
									+ "Yes, that is correct."
									+ "</modalFeedback>";
							 try {
								quest.write ( buffer , 0 , buffer.length ( ) );
								quest.newLine ( );
							}
				        	catch (Exception e) {
				        		UtilLog.toLog("error: Unable writer in a File." );
				        	}
						}
						else
						{
							buffer = "\t\t<modalFeedback outcomeIdentifier=\"FEEDBACK\" identifier=\"Choice"
								+ toQti20.getCodeAnswer ( positionQuestion , positionAnswer )
								+ "\" showHide=\"show\">"
								+ "No, that is not correct."
								+ "</modalFeedback>";
							 try {
								quest.write ( buffer , 0 , buffer.length ( ) );
								quest.newLine ( );
							}
				        	catch (Exception e) {
				        		UtilLog.toLog("error: Unable writer in a File." );
				        	}
						}
					}
				}
				else
				{
					buffer = "\t<responseProcessing";
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
					buffer = "\t\ttemplate=\"http://www.imsglobal.org/question/qti_v2p0/rptemplates/match_correct\"/>";
					
					 try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
		    		buffer = "";
				}
			}
			buffer = "</assessmentItem>";
			
			 try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
			buffer = "";
			try {
				quest.close ( );
			}
			catch (Exception e) {
				UtilLog.writeException(e);
        	}
			quest = null;
		}
		
		return true;
		
	} // makeFiles ( )

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
		if (toQti20.getImage(positionQuestion) != "") {
			image = toQti20.getImage(positionQuestion);

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
    
} // class ToQti20
