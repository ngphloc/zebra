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

public class ToQti12 {
	
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


    
    
	/** this store the input path file */
	protected String inputPath;
		/** this store the output path file */
	protected String outputPath;
		/** this store the input path file refered of items WOW System */
	protected String inputsubPath;
		/** this class read the items file of WOW System */
    protected ReadWOW3 toQti12;

	public ToQti12 ( ) {
	}

	/*    
    NAME: convert ( )
    IT'S FROM: Class ToQti12.
    CALLED FROM: Main program 
    CALL TO:
    			boolean makeQti12File(outputFile)
	    		
    recibed:
    		inputFile -> String that store the name of file where we read
    					items
			outputFile -> String that store the name of file where we write
			  			items
    RETURN:
    		boolean
    FUNCTION:
    		convert a WOW items file to IMS Qti 1.2 file ( .xml )
    */
	
    public boolean convert ( String inputFile , String outputFile )
    {

        // variables
        toQti12 = new ReadWOW3 ( );
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
	            parser.setContentHandler ( toQti12 );
	            parser.setErrorHandler   ( toQti12 );                

	        try {
	                parser.parse ( inputFile );
	        } catch (Exception e) {
		   	 UtilLog.writeException(e);		
		    }
        if ( makeQti12File ( outputFile ) )
        	return true;
        else
        	return false;

    } // convert ( String , String )

/*    
    NAME: makeQti12Files ( )
    IT'S FROM: Class ToQti12.
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
            void
    RETURN:
    		void
    FUNCTION:
    		Print why call the program.
    		( Only Can write simple and multiple choice with or without image ).
    */
    public boolean makeQti12File (final String FileName) {
		BufferedWriter quest = null;
		int numberOfAnswers;
		int numberOfQuestions = toQti12.getNumberOfQuestions ( );
		int numberOfCorrect;
		int i= 0;
		String buffer = "";
		String image = "";
		Vector vCode = new Vector ( );
		Vector vPosition = new Vector ( );
		
		if ( numberOfQuestions < 1 || toQti12.flag < 15 )
			return false;
		
        try {
        		quest = new BufferedWriter ( new FileWriter ( FileName ) );
        } catch (Exception e) {
            UtilLog.writeException(e);
            return false;
        }

        buffer = "<?xml version = \"1.0\" encoding = \"UTF-8\" standalone = \"no\"?>";
		try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.writeException(e);
        }
		buffer = "<!DOCTYPE questestinterop SYSTEM \"ims_qtiasiv1p2.dtd\">";
		try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.writeException(e);
        }
		buffer = "<questestinterop>";
		
		try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.writeException(e);
        }
        	
		
		for ( int positionQuestion = 0 ; positionQuestion <= numberOfQuestions ; positionQuestion++ )
		{
			//
			// Here we create a Questions File type Qti 1.2
			//
			
	        numberOfCorrect = toQti12.getNumberOfCorrect ( positionQuestion );

			buffer = "\t<item title = \""
			+ toQti12.getName ( )
			+ "\" ident = \""
			+ toQti12.getName ( )
			+ "\">";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.writeException(e);
	        }
	        
			buffer = "\t\t<presentation label = \""
			+ toQti12.getCodeQuestion ( positionQuestion )
			+ "\">";
				
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.writeException(e);
	        }
	        	
	        buffer = "\t\t\t<flow>";
				
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        	catch (Exception e) {
	        		UtilLog.writeException(e);
	        	}
	        	
	        	buffer = "\t\t\t\t<material>";
				
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.writeException(e);
	        	}
	        	
	        	buffer = "\t\t\t\t<mattext>"
	        	+ toQti12.getEnunciate ( positionQuestion )
	        	+ "</mattext>";
				
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.writeException(e);
	        	}
	        	
	        	if ( toQti12.getImage ( positionQuestion ) != "" )
	        	{
	        		buffer ="\t\t\t\t<matimage imagtype = \"image/";
	        		image = getImage ( positionQuestion );
	        		i = image.lastIndexOf ( "." );
	        		if ( image.substring ( i ) == "jpg" )
	        			buffer = buffer + "jpg";
	        		else
	        			if ( image.substring ( i ) == "gif" )
	        					buffer = buffer + "gif";
	        		buffer = buffer
	        		+ "\" uri = \""
	        		+ ".\\images" + image
	        		+ "\"/>";
					try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.writeException(e);
		        	}
	        	}
	        	
	        	buffer = "\t\t\t</material>";
				
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.writeException(e);
	        	}
	        	
	        	buffer = "\t\t\t<response_lid ident = \"WOW_"
	        	+ toQti12.getCodeQuestion ( positionQuestion )
	        	+ "\" rcardinality = \"Single\" rtiming = \"No\">";
				
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.writeException(e);
	        	}
	        	
				buffer = "\t\t\t\t<render_choice shuffle = \"Yes\">";
				
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.writeException(e);
	        	}
	        	
				
				numberOfAnswers = toQti12.getNumberOfAnswers ( positionQuestion );
				numberOfCorrect = toQti12.getNumberOfCorrect ( positionQuestion );
				for ( int positionAnswer = 0 ; positionAnswer < numberOfAnswers ; positionAnswer++ )
				{
						//
						// Here write in File the Answer
						//
					buffer = "\t\t\t\t\t\t<flow_label>";
					
					try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
		        	catch (Exception e) {
		        		UtilLog.writeException(e);
		        	}
		        	
					buffer = "\t\t\t\t\t\t\t<response_label ident = \"A"
					+ positionAnswer 
					+ "\">";
					
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.writeException(e);
	        	}
		        	
				buffer = toQti12.getCorrect ( positionQuestion , positionAnswer );
				
				if ( buffer.compareTo ( "true" ) == 0 )
				{
					vCode.addElement ( new Integer ( toQti12.getCodeQuestion ( positionQuestion ) ) );
					vPosition.addElement ( new Integer ( ( positionAnswer ) ) );
				}
					
	        	buffer = "\t\t\t\t\t\t\t<flow_mat class = \"list\">";
	
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.writeException(e);
	        	}
		        	
	        	buffer = "\t\t\t\t\t\t\t\t\t<material>";

				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.writeException(e);
	        	}

	        	buffer = "\t\t\t\t\t\t\t\t\t\t<mattext>"
	        	+ toQti12.getTextAnswer ( positionQuestion , positionAnswer )
	        	+ "</mattext>";
	
	        	try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
		        catch (Exception e) {
		      	  UtilLog.writeException(e);
		        }
		        	
		        buffer = "\t\t\t\t\t\t\t\t\t</material>";
	
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
		        catch (Exception e) {
		      	  UtilLog.writeException(e);
		        }
	
		        buffer = "\t\t\t\t\t\t\t\t</flow_mat>";
	
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
		        catch (Exception e) {
		      	  UtilLog.writeException(e);
		        }
		        	
	        	buffer = "\t\t\t\t\t\t\t</response_label>";
	        	
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
		       	catch (Exception e) {
		       		UtilLog.writeException(e);
		       	}
				buffer = "\t\t\t\t\t\t</flow_label>";
				
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
		       	catch (Exception e) {
		       		UtilLog.writeException(e);
		       	}
			}
			
			buffer = "\t\t\t\t\t</render_choice>";
				
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.writeException(e);
	        }
				
			buffer = "\t\t\t\t</response_lid>";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.writeException(e);
	        }
	
			buffer = "\t\t\t</flow>";
				
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.writeException(e);
	        }
	        	
			buffer = "\t\t</presentation>";
				
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.writeException(e);
	        }
	    		
			buffer = "\t\t<resprocessing>";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.writeException(e);
	        }
	    		
			buffer = "\t\t\t<outcomes>";
				
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.writeException(e);
	        }
	        	
			buffer = "\t\t\t\t<decvar vartype = \"Integer\" defaultval = \"0\"/>";
				
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.writeException(e);
	        }
	        
			buffer = "\t\t\t</outcomes>";
				
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.writeException(e);
        	}
        	
        	numberOfCorrect = toQti12.getNumberOfCorrect ( positionQuestion );
        	
        	if ( numberOfCorrect > 0 && numberOfCorrect == 1 )
	        	buffer = "\t\t\t<respcondition title = \"Correct\">";
        	else
        		buffer = "\t\t\t<respcondition title = \"FullCorrect\">";
	        	
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.writeException(e);
	        }
	        	
	        buffer = "\t\t\t\t<conditionvar>";
	        	
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.writeException(e);
	        }
	        if ( numberOfCorrect == 1 )
	        {
				buffer = "\t\t\t\t\t\t<varequal respident = \"WOW_"
					+ vCode.firstElement ( )
					+ "\">A"
					+ vPosition.firstElement ( )
					+ "</varequal>";
						
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.writeException(e);
	        	}
		        	
	        	buffer = "\t\t\t\t</conditionvar>";
	        	
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.writeException(e);
	        	}
			}
	        else
	        {
	        	
	        	buffer = "\t\t\t\t\t<and>";
	        	
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.writeException(e);
	        	}
	        	
	        	
				for ( i = 0 ; i < numberOfCorrect; i++ )
				{
					buffer = "\t\t\t\t\t\t<varequal respident = \"WOW_"
					+ vCode.elementAt ( i )
					+ "\">A"
					+ vPosition.elementAt ( i )
					+ "</varequal>";
						
					try {
						quest.write ( buffer , 0 , buffer.length ( ) );
						quest.newLine ( );
					}
			       	catch (Exception e) {
			       		UtilLog.writeException(e);
			       	}
				}

	        	buffer = "\t\t\t\t\t</and>";
		        	
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.writeException(e);
	        	}
	        	
	        	buffer = "\t\t\t\t</conditionvar>";
	        	
				try {
					quest.write ( buffer , 0 , buffer.length ( ) );
					quest.newLine ( );
				}
	        	catch (Exception e) {
	        		UtilLog.writeException(e);
	        	}
	        }
	        
	        buffer = "\t\t\t\t<setvar action = \"Add\">"
	        + numberOfCorrect
	        + "</setvar>";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.writeException(e);
        	}
        	if ( numberOfCorrect > 0 && numberOfCorrect == 1 )
        		buffer = "\t\t\t\t<displayfeedback "
        		+ "feedbacktype = \"Response\" "
        		+ "linkrefid = \"Correct\"/>";
        	else
        		buffer = "\t\t\t\t<displayfeedback "
           		+ "feedbacktype = \"Response\" "
           		+ "linkrefid = \"FullCorrect\"/>";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.writeException(e);
        	}
 
			buffer = "\t\t\t</respcondition>";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
        	catch (Exception e) {
        		UtilLog.writeException(e);
        	}
			
			buffer = "\t\t</resprocessing>";
				
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	       	catch (Exception e) {
	       		UtilLog.writeException(e);
	       	}
	       	
       		if ( numberOfCorrect > 0 && numberOfCorrect == 1 )
       			buffer = "Correct";
    		else
    			buffer = "FullCorrect";
	       			
	       	buffer = "\t\t<itemfeedback ident = \""
	       	+ buffer
	       	+ "\" view = \"Candidate\">";
				
	       	try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	       	catch (Exception e) {
	       		UtilLog.writeException(e);
	       	}
		       	
	       	buffer = "\t\t\t<flow_mat>";
				
	       	try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	       	catch (Exception e) {
	       		UtilLog.writeException(e);
	       	}
		       	
	       	buffer = "\t\t\t\t<material>";
				
	       	try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	       	catch (Exception e) {
	       		UtilLog.writeException(e);
	       	}

		    buffer = "Yes, you are right.";

		    buffer = "\t\t\t\t\t<mattext>"
		    + buffer
		    + "</mattext>";
				
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
		    catch (Exception e) {
		   	 UtilLog.writeException(e);
		    }
		       	
		    buffer = "\t\t\t\t</material>";
					
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	       	catch (Exception e) {
	       		UtilLog.writeException(e);
	       	}
		       	
	       	buffer = "\t\t\t</flow_mat>";
				
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
		    catch (Exception e) {
		   	 UtilLog.writeException(e);
		    }
		       	
		    buffer = "\t\t</itemfeedback>";
					
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
		    catch (Exception e) {
		   	 UtilLog.writeException(e);
		    }
	       	
	       	buffer = "\t</item>";
			
			try {
				quest.write ( buffer , 0 , buffer.length ( ) );
				quest.newLine ( );
			}
	        catch (Exception e) {
	      	  UtilLog.writeException(e);
	        }
			vCode.clear ( );
			
			vPosition.clear ( );
		}
        buffer = "</questestinterop>";
		
		try {
			quest.write ( buffer , 0 , buffer.length ( ) );
			quest.newLine ( );
		}
        catch (Exception e) {
      	  UtilLog.writeException(e);
        }
		
		try {
			quest.close ( );
		}
		catch (Exception e) {
			UtilLog.writeException(e);
    	}
		
		buffer = "";

		quest = null;
		
		vCode.clear ( );
		
		vPosition.clear ( );
		
		copyDTD( );
		
		return true;
		
	} // makeQti12File ( )

	/*
	 * NAME: getImage ( )
	 * IT'S FROM:
	 * 		Class ToQti12.
	 * CALLED FROM:
	 * 		makeHotPotatoesv6 method
	 * 
	 * CALL TO: getImage(positionQuestion)
	 * 
	 * recibed: 
	 * 			positionQuestion -> Integer that store the position
	 * 							where the image is located
	 *  
	 * RETURN: String -> the image's name.
	 * 
	 * FUNCTION: move the image.
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
		if (toQti12.getImage(positionQuestion) != "") {
			image = toQti12.getImage(positionQuestion);

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
	
	/*
	 * NAME: extractPath ( )
	 * IT'S FROM:
	 * 		Class ToQti12.
	 * CALLED FROM:
	 * 		convert method
	 * 
	 * CALL TO: nothing
	 * 
	 * recibed: 
	 * 			input -> String that store the name of file where we read
	 * 				items
	 * 			output -> String that store the name of file where we write
	 * 				items
	 *  
	 * RETURN: boolean
	 * 
	 * FUNCTION: Find paths of  input file and output file
	 */
	
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
	} // extractPath (String input, String output)
	
	//	 Copies src file to dst file.
    // If the dst file does not exist, it is created
    void copyDTD( ) {
    	FileInputStream in = null;
    	
    	FileOutputStream out = null;
    	
    	try {
	    	in = new FileInputStream  ( inputsubPath + "ims_qtiasiv1p2.dtd" );
	    	out = new FileOutputStream  ( outputPath + "ims_qtiasiv1p2.dtd" );
	    
	        // Transfer bytes from in to out
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
		try {
	        in.close();
	        out.close();
		} catch (Exception e) {
			UtilLog.writeException(e);
		}
    } // copyDTD( )
    
} // class ToQti12
