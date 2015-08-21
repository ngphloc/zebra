package es.uco.WOW.TestConverter;

import java.io.File;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import es.uco.WOW.Utils.UtilLog;

public class FromQti12 {


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

	protected String path;

	protected Qti12 qti12;
	
	protected WriteWOW3 writeWOW3;
	

	public FromQti12() {}

	//
	// MAIN
	//

	/** Main program entry point. */
	public boolean convert(String inputFile, String outputFile) {

		// variables
		qti12 = new Qti12 ( );
		writeWOW3 = new WriteWOW3 ( inputFile, outputFile);
		XMLReader parser = null;
		boolean namespaces = DEFAULT_NAMESPACES;
		boolean namespacePrefixes = DEFAULT_NAMESPACE_PREFIXES;
		boolean validation = DEFAULT_VALIDATION;
		boolean schemaValidation = DEFAULT_SCHEMA_VALIDATION;
		boolean schemaFullChecking = DEFAULT_SCHEMA_FULL_CHECKING;
		boolean dynamicValidation = DEFAULT_DYNAMIC_VALIDATION;

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
		parser.setContentHandler(qti12);
		parser.setErrorHandler(qti12);
		File p = new File(inputFile);
		path = p.getPath();
		try {
			parser.parse(inputFile);
		} catch (Exception e) {
			// ignore
			UtilLog.writeException(e);
		}

		if ( writeWOW3.writeWOWFile ( qti12.getFileQuestion ( ) , outputFile ) ) {
			return true;
		} else {
			return false;
		}

	} // convert ( String , String )

} // class FromQti12
