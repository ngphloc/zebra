package es.uco.WOW.TestConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Vector;

import vn.spring.WOW.WOWStatic;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import es.uco.WOW.Utils.UtilLog;

public class ToSiette {
	
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
    
    protected ReadWOW3 toSiette;

	public ToSiette ( ) {
	}

    //
    // MAIN
    //

    /** Main program entry point. */
    public boolean convert ( String inputFile , String outputFile )
    {

        // variables
        toSiette = new ReadWOW3 ( );
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
	        }
	    	catch (Exception e) {
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
	            parser.setContentHandler ( toSiette );
	            parser.setErrorHandler   ( toSiette );                

	        try {
	           parser.parse ( inputFile );
	        } catch (Exception e) {
	      	  UtilLog.writeException(e);
		     }

        return makeSietteFile (outputFile);

    } // convert ( String , String )

    //
    // Private methods
    //

    
/*    
    NAME: makeSietteFile ( )
    IT'S FROM: Class ToSiette.
    CALLED FROM: Main program 
    CALL TO:
    		from FileOfQuestionsStruct Class:
    			int getNumberOfQuestions ( )
    			String getName ( )
    			int getCodeQuestion ( int positionQuestion )
	    		String getConcept ( )
	    		int getNumberOfAnswers ( int positionQuestion )
	    		int getNumberOfCorrect ( int positionQuestion )
	    		int getCodeAnswer ( int positionQuestion , int positionAnswer )
	    		String getExplanation ( int positionQuestion , int positionAnswer )
				String getTextAnswer ( int positionQuestion , int positionAnswer )
				String getEnunciate ( int positionQuestion )
				String getImage ( int positionQuestion )
	    		
    recibed:
            FileName String type that where we write the file Siette items.
    RETURN:
    		boolean
    FUNCTION:
    		Print why call the program.
    		( Only Can write simple and multiple choice with or without image ).
    */
    public boolean makeSietteFile ( String FileName )
	{
		BufferedWriter quest = null;
		int numberOfAnswers;
		int numberOfQuestions = toSiette.getNumberOfQuestions ( );
		String buffer = "";
		Vector vCorrect = null;
		
		if ( numberOfQuestions < 1 || toSiette.flag < 15 )
			return false;
		//
		// Here we create a Questions File type Siette
		//
		
        try {
        		quest = new BufferedWriter ( new FileWriter ( FileName ) );
        }
        catch (Exception e) {
      	  UtilLog.writeException(e);
      	  return false;
        }
        
        buffer = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		
        try {
      	  quest.write ( buffer , 0 , buffer.length ( ) );
			  quest.newLine ( );
        } catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "<subject id=\""
        + "-22"
        + "\" topicid=\""
        + "-133"
        + "\" xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\""
        + " xsi:noNamespaceSchemaLocation=\"http://www.lcc.uma.es/siette/xml/siette_english.xsd\">";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t<name>"
        + toSiette.getName ( )
        + "</name>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t<isactive>true</isactive>";
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t<topics>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t<topic id=\"-2084\">";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t<name>Items internos</name>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t<isactive>true</isactive>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t<translations>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t<translation>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t\t<name>Built-in items</name>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t\t<language>english</language>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t</translation>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t</translations>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t<subtopics>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t<topic id=\"-2085\">";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t\t<name>Multiple opci&amp;oacute;n - Respuesta &amp;uacute;nica</name>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t\t<isactive>true</isactive>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t\t<translations>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t\t\t<translation>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t\t\t\t<name>Multiple choice - Single answer</name>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t\t\t\t<language>english</language>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t\t\t</translation>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t\t</translations>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t</topic>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t<topic id=\"-2100\">";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t<name>Multiple opci&amp;oacute;n - Respuesta m&amp;uacute;ltiple</name>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t<isactive>true</isactive>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t<translations>";        
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t\t<translation>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t\t\t<name>Multiple choice - Multiple answers</name>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t\t\t<language>english</language>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t\t</translation>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t</translations>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t\t</topic>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t\t</subtopics>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t\t</topic>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
        buffer = "\t</topics>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
       buffer = "\t<items>";
        
        try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.toLog("error: Unable writer in a File." );
        }
        
		for ( int positionQuestion = 0 ; positionQuestion <= numberOfQuestions ; positionQuestion++ )
		{
	       // numberOfCorrect = toSiette.getNumberOfCorrect ( positionQuestion );
			vCorrect= new Vector ( );
			
			//
			// Here start to write a Question or item.
			//
			
			buffer = "\t\t<item id =\"-133"
			+ toSiette.getCodeQuestion ( positionQuestion )
			+ "\" idcollection=\"-133\" type=\"";
			
			if ( toSiette.getNumberOfCorrect ( positionQuestion ) > 1 )
				buffer = buffer + "4";
			else
				buffer = buffer + "2";
			
			buffer = buffer + "\">";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.toLog("error: Unable writer in a File." );
	        }
			buffer = "\t\t\t<title>"
			+ toSiette.getConcept ( )
			+ "</title>";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.toLog ("error: Unable writer in a File." );
	        }
	        	
			buffer = "\t\t\t<isactive>true</isactive>";

			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.toLog("error: Unable writer in a File." );
	        }
	        	
			buffer = "\t\t\t<stem>";
			
			buffer = buffer + toSiette.getEnunciate ( positionQuestion );
	        if ( toSiette.getImage ( positionQuestion ) != "" )
	        	buffer = buffer + "&lt;img src=&quot;"
	        	+ "images" + getImage ( positionQuestion )
	        	+ "&quot;&gt;";
	        buffer = buffer + "</stem>";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.toLog("error: Unable writer in a File." );
	        }
	        
	        buffer = "\t\t\t<iccparameters difficulty=\""
	        + toSiette.getDifficulty ( positionQuestion )
	        + "\" discrimination=\""
	        + toSiette.getDiscrimination ( positionQuestion )
	        + "\" guessing=\""
	        + toSiette.getGuessing ( positionQuestion )
	        + "\" topicid=\"-";
	        if ( toSiette.getNumberOfCorrect ( positionQuestion ) > 1 )
				buffer = buffer + "2100";
			else
				buffer = buffer + "2085";
				
			buffer = buffer + "\"/>";
				
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.toLog("error: Unable writer in a File." );
	        }
	        
	        buffer = "\t\t\t\t<responses>";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.toLog("error: Unable writer in a File." );
	        }
			numberOfAnswers = toSiette.getNumberOfAnswers ( positionQuestion );
			
			for ( int positionAnswer = 0 ; positionAnswer < numberOfAnswers ; positionAnswer++ )
			{
					//
					// Here write in File the Answer
					//
				
		       	buffer ="\t\t\t\t<response id=\"-"
		       	+ toSiette.getCodeQuestion ( positionQuestion )
		       	+ toSiette.getCodeAnswer ( positionQuestion , positionAnswer )
		       	+ "1\">";
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
		        catch (Exception e) {
		      	  UtilLog.toLog("error: Unable writer in a File." );
		        }
	        	
	        	buffer = "\t\t\t\t\t<text>"
	        	+ toSiette.getTextAnswer ( positionQuestion , positionAnswer )
	        	+ "</text>";
					
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.toLog("error: Unable writer in a File." );
	        	}
	        	
	        	buffer = "\t\t\t\t\t<feedback";
				
	        	if ( toSiette.getExplanation ( positionQuestion , positionAnswer ) == "" )
	        		buffer = buffer + "/>";
	        	else
	        		buffer = buffer + ">"
	        		+ toSiette.getExplanation ( positionQuestion , positionAnswer )
	        		+ "</feedback>";
	        	
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.toLog("error: Unable writer in a File." );
	        	}

	        	if ( toSiette.getCorrect ( positionQuestion , positionAnswer ).equals ( "true" ) )
	        	{
		        	vCorrect.addElement (new Integer(toSiette.getCodeAnswer ( positionQuestion , positionAnswer )) );
		        	
		        	buffer = "\t\t\t\t\t<score>"
		        	+ ( float ) ( 1.0/toSiette.getNumberOfCorrect ( positionQuestion ) )
		        	+ "</score>";
		        		
		        	try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
		        	
		        	buffer = "\t\t\t\t\t<penalty>0.0</penalty>";
	        	}
	        	else
	        	{
		        	buffer = "\t\t\t\t\t<score>0.0</score>";
		        		
		        	try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.toLog("error: Unable writer in a File." );
		        	}
		        	
		        	buffer = "\t\t\t\t\t<penalty>"
		        	+ ( float ) ( 1.0 / toSiette.getNumberOfCorrect ( positionQuestion ) )
		        	+ "</penalty>";	        	
		        }
				
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.toLog("error: Unable writer in a File." );
	        	}
	        	buffer = "\t\t\t\t</response>";
        		
	        	try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.toLog("error: Unable writer in a File." );
	        	}
			}
        	
			buffer = "\t\t\t</responses>";
    		
        	try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
			
        	buffer = "\t\t\t<evaluation>";
       		
        	try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
        	
       		//
       		//	Here we indicate what answer is correct
       		//
       		for ( int j = 0 ; j < vCorrect.size ( ) ; j++ )
       		{
       			buffer = "\t\t\t\t<right>";
       			
            	try {
    				quest.write ( buffer , 0 , buffer.length ( ) );
    				quest.newLine ( );
    			}
            	catch (Exception e) {
            		UtilLog.toLog("error: Unable writer in a File." );
            	}
            	
            	String element = ((Integer) vCorrect.elementAt ( j )).toString();
            	
       			buffer  = "\t\t\t\t\t<responseid>"
        		+ "-"
        		+ toSiette.getCodeQuestion ( positionQuestion )
        		+ element
        		+ "1"
        		+ "</responseid>";
       			
            	try {
    				quest.write ( buffer , 0 , buffer.length ( ) );
    				quest.newLine ( );
    			}
            	catch (Exception e) {
            		UtilLog.toLog("error: Unable writer in a File." );
            	}
       			
       			buffer = "\t\t\t\t</right>";
       			
            	try {
    				quest.write ( buffer , 0 , buffer.length ( ) );
    				quest.newLine ( );
    			}
            	catch (Exception e) {
            		UtilLog.toLog("error: Unable writer in a File." );
            	}
            	
       		}
	        	
        	buffer = "\t\t\t</evaluation>";
       		
        	try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
        	
        	buffer = "\t\t</item>";
        	
        	try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.toLog("error: Unable writer in a File." );
        	}
		}
	        	
		buffer = "\t</items>";
		
    	try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
    	catch (Exception e) {
    		UtilLog.toLog("error: Unable writer in a File." );
    	}
    	
    	buffer = "</subject>";
		
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
			UtilLog.toLog("error: File isn�t closed." );
        }
		
		quest = null;
		
		return true;
		
	} // makeSietteFiles ( )

    /*    
    NAME: getImage ( )
    IT'S FROM: Class ToSiette.
    CALLED FROM: Main program 
    CALL TO:
    		from FileOfQuestionsStruct Class:
    			int getNumberOfQuestions ( )
    			String getName ( )
    			int getCodeQuestion ( int positionQuestion )
	    		String getConcept ( )
	    		int getNumberOfAnswers ( int positionQuestion )
	    		int getNumberOfCorrect ( int positionQuestion )
	    		int getCodeAnswer ( int positionQuestion , int positionAnswer )
	    		String getExplanation ( int positionQuestion , int positionAnswer )
				String getTextAnswer ( int positionQuestion , int positionAnswer )
				String getEnunciate ( int positionQuestion )
				String getImage ( int positionQuestion )
	    		
    recibed:
            positionQuestion integer type  where we say the position of image.
    RETURN:
    		String
    FUNCTION:
    		Copy the image file.
    */
    
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
		if (toSiette.getImage(positionQuestion) != "") {
			image = toSiette.getImage(positionQuestion);

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
		}

		return imageName;
	} // getImage(int positionQuestion)

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

    
} // class ToSiette
