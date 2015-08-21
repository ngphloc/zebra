package es.uco.WOW.FileList;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import vn.spring.WOW.WOWStatic;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import es.uco.WOW.TestEditor.TestEditor;
import es.uco.WOW.Utils.UtilLog;

/**
 * <p> Title: Wow! TestEditor </p> 
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p> Copyright: Copyright (c) 2008 </p> 
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: FileList 
 * FUNCTION: This class manages all the options related to the
 *   file that contains all the names of the question files, test configuration
 *   files and log file of the students. 
 *   This class also updates this information in a file that belongs to the author.
 * LAST MODIFICATION: 06-02-2008.
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class FileList {

	/** Contains the path of the root directory of the author */
	private static String authorRoot = WOWStatic.AUTHORFILESPATH;
	/** Path of the WOW! system */
	private static String wowRoot = WOWStatic.config.Get("WOWROOT");
	/** Relative path to the folder where the items and the test are stored */
	private static String itemsPath = WOWStatic.config.Get("XMLROOT") + WOWStatic.config.Get("itemspath");
	/** Type of the file which the TestEditor works with */
	private static String testEditorTypeFile = WOWStatic.config.Get("testeditortypefile");
	/** Name of the file that stores the list of files */
	private static String fileListName = WOWStatic.config.Get("filelistname");
	
	/**
	 * Name of the course that this list of files belongs to
	 */
	private String courseName = null;

	/**
	 * Public constructor
	 * @param aCourseName Name of a course
	 */
	public FileList (final String aCourseName) {
		this.courseName = aCourseName;
	}

	
	/**
	 * Checks if a course exists in the file.
	 * @return true if the course exists in the file.
	 */
	private boolean courseExists() {
		return courseExists(courseName);
	}

	/**
	 * Checks if a course exists in the file.
	 * @param aCourseName Name of the course.
	 * @return true if the course exists in the file.
	 */
	public static boolean courseExists(final String aCourseName) {
		try {	
			// Loads the file
			File file = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(file);
			
			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument,
					"fileList/course[@courseName = \"" + aCourseName + "\"]");

			return (course != null);

		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	}

	
	/**
	 * Checks if the course passed as argument exists in the
	 * .test file of the author.
	 * @param authorName Login of the author
	 * @return true if the course exists in the .test file of the author.
	 */
	private boolean authorCourseExists(final String authorName) {
		String authorFileLocation = wowRoot
				+ authorRoot.substring(1, authorRoot.length()) + authorName + "/";

		try {
			// Loads the author file
			File file = new File(authorFileLocation + courseName + ".test");
			Document xmlDocument = new SAXBuilder().build(file);
			
			// Looks for the course in the file
			Element course = (Element) XPath.selectSingleNode(xmlDocument,
					"fileList/course[@courseName = \"" + courseName + "\"]");

			return (course != null);

		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	}

	
	/**
	 * Copy the DTD Schema file to the adecuated path
	 * @return false if any error occurs, true otherwise 
	 */
	private static boolean copyDTD() {
		// Checks if the destiny file exists
		File fileListDTDNew = new File(itemsPath + "fileList.dtd");
		if (fileListDTDNew.exists()) {
			return true;
		}

		// Copies the DTD file to the folder where the 
		// question file are stored.
		try {
			File fileListDTD = new File(wowRoot.substring(0, wowRoot.length() - 1)
					+ WOWStatic.config.Get("testeditordtdpath") + "fileList.dtd");

			byte [] data = new byte[(int) fileListDTD.length()];

			BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileListDTD));
			DataInputStream dataInputStream = new DataInputStream(input);
			dataInputStream.read(data);
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(itemsPath + "fileList.dtd"));
			DataOutputStream dataOutputStream = new DataOutputStream(out);
			dataOutputStream.write(data);

			input.close();
			dataInputStream.close();
			out.close();
			dataOutputStream.close();

			return true;

		} catch (java.io.IOException e) {
			UtilLog.writeException(e);
			return false;
		}
	}

	
	/**
	 * Creates the file that this class works with.
	 * @return false if any error occurs, true otherwise
	 */
	private static boolean createFileList() {
		// Copies the DTD in the folder where the file will be created
		if (!copyDTD()) 
			return false;
		
		// Contains the document that will be saved in the 
		// question file that is created here 
		Document xmlDocument; 
		// DTD Reference of the file that is created here
		DocType docType;
		
		// Creates the DocType of the Document and assigns this to it.
		docType = new DocType("fileList", "fileList.dtd");
		xmlDocument = new Document().setDocType(docType);

		// Creates the root element of the document
		Element fileList = new Element("fileList");

		// Sets the root node of the document
		xmlDocument.setRootElement(fileList);

		// Saves the new created question file
		try {
			File xmlFile = new File(itemsPath + fileListName + testEditorTypeFile);
			FileWriter xmlFileWriter = new FileWriter(xmlFile);
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
			out.output(xmlDocument, xmlFileWriter);
			xmlFileWriter.close();
			return true;

		} catch (IOException e) {
			UtilLog.writeException(e);
			return false;
		}
	}

	/**
	 * Creates a file that belongs to the author, in his
	 * root folder, in a sub-folder with the name of the course.
	 * This file will contain the names of the 
	 * question file and the name of the test files that
	 * belongs to the author and that course.
	 * @param authorName Login of the author
	 * @return false if any error occurs, true otherwise.
	 */
	private boolean createAuthorFileList(final String authorName) {
		return createAuthorFileList(courseName, authorName);
	}
	
	/**
	 * Creates a file that belongs to the author, in his
	 * root folder, in a sub-folder with the name of the course.
	 * This file will contain the names of the 
	 * question file and the name of the test files that
	 * belongs to the author and that course.
	 * @param aCourseName Name of a Course
	 * @param authorName Login of the author
	 * @return false if any error occurs, true otherwise.
	 */
	private static boolean createAuthorFileList(final String aCourseName, final String authorName) {
		String fileLocation = wowRoot
				+ authorRoot.substring(1, authorRoot.length()) + authorName + "/";
		
		Document xmlDocument; 
		DocType docType;

		// Creates the DocType of the Document and sets this to it.
		docType = new DocType("fileList",
				"../../TestEditor/dtd/fileListAuthor.dtd");
		xmlDocument = new Document().setDocType(docType);

		// Creates the root element of the document
		Element fileList = new Element("fileList");

		// Sets the root node of the document
		xmlDocument.setRootElement(fileList);

		// Saves the new created question file
		try {
			File xmlFile = new File(fileLocation + aCourseName + ".test");
			FileWriter xmlFileWriter = new FileWriter(xmlFile);
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
			out.output(xmlDocument, xmlFileWriter);
			xmlFileWriter.close();
			return true;

		} catch (IOException e) {
			UtilLog.writeException(e);
			return false;
		}
	}

	/**
	 * Adds a course to the file. If the author parameter is not null,
	 * adds this course to the .test file of the author.
	 * @param author Login of the author.
	 * @return false if any error occurs, true otherwise.
	 */
	private boolean addCourse(final String author) {
		return addCourse(courseName, author);
	}
	
	
	/**
	 * Adds a course to the file. If the author parameter is not null,
	 * adds this course to the .test file of the author.
	 * @param aCourseName Name of the course.
	 * @param author Login of the author.
	 * @return false if any error occurs, true otherwise.
	 */
	private static boolean addCourse(final String aCourseName, final String author) {
		Document xmlDocument;
		//	Root element of the XML document
		Element elementFileList; 
		//	Root element of the XML document of the author
		Element elementAuthorFileList; 
		// Path to the author file
		String authorFileLocation = "";
		// Indicates if the course must be added to the author file
		boolean createAuthor = false;

		if (author != null && !author.trim().equals("")) {
			createAuthor = true;
			authorFileLocation = wowRoot
					+ authorRoot.substring(1, authorRoot.length()) + author + "/";
		}

		// Checks if the file exists
		File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
		if(!fileList.exists() || fileList.isDirectory()) {
			// If doesn't exists, creates it 
			if(!createFileList())
				return false;
		} 
				
		try {
			// Try to open the existing file
			xmlDocument = new SAXBuilder().build(fileList);
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
		
		// Gets the root element
		elementFileList = xmlDocument.getRootElement();

		// Creates the "course" node.
		Element course = new Element("course");
		course = course.setAttribute("courseName", aCourseName);

		// Creates the "questionsFile" element.
		Element questionsFile = new Element("questionsFile");

		// Creates the "testFile" element.
		Element testFile = new Element("testFile");

		// Creates the classic element.
		Element classic = new Element(TestEditor.CLASSIC);

		// Creates the adaptive element.
		Element adaptive = new Element(TestEditor.ADAPTIVE);

		// Adds the classic and adaptive elements to the "testFile" element.
		testFile = testFile.addContent(classic);
		testFile = testFile.addContent(adaptive);

		// Creates the "studentFile" element
		Element studentFile = new Element("studentFile");

		// Adds the "questionsFile", "testFile" and "studentFile" elements to the "course" node
		course = course.addContent(questionsFile);
		course = course.addContent(testFile);
		course = course.addContent(studentFile);

		// Adds the "course" element to the root node of the document
		elementFileList = elementFileList.addContent(course);

		// Saves the new file
		try {
			FileWriter xmlFileWriter = new FileWriter(new File(itemsPath + fileListName + testEditorTypeFile));
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
			out.output(xmlDocument, xmlFileWriter);
		} catch (IOException e) {
			UtilLog.writeException(e);
			return false;
		}

		if (createAuthor) {
			File authorFileList = new File(authorFileLocation + aCourseName + ".test");
			if (!authorFileList.exists() || authorFileList.isDirectory()) {
				// If doesn`t exists, creates it
				if (!createAuthorFileList(aCourseName, author))
					return false;
			}
					
			try {
				// Try to open the existing file
				xmlDocument = new SAXBuilder().build(authorFileList);
			} catch (Exception e) {
				UtilLog.writeException(e);
				return false;
			}
				
				
			// Obtains the root element
			elementAuthorFileList = xmlDocument.getRootElement();

			// Creates the "course" node and sets its name.
			Element courseA = new Element("course");
			courseA = courseA.setAttribute("courseName", aCourseName);

			// Creates the "questionsFile" element.
			Element questionsFileA = new Element("questionsFile");

			// Creates the "testFile" element.
			Element testFileA = new Element("testFile");

			// Creates the classic element.
			Element classicA = new Element(TestEditor.CLASSIC);

			// Creates the adaptive element.
			Element adaptiveA = new Element(TestEditor.ADAPTIVE);

			// Adds the classic and adaptive elements to the "testFile" element.
			testFileA = testFileA.addContent(classicA);
			testFileA = testFileA.addContent(adaptiveA);

			// Adds the "questionsFile" and "testFile" elements to the "course" node.
			courseA = courseA.addContent(questionsFileA);
			courseA = courseA.addContent(testFileA);

			// Adds the "course" element to the root node of the document
			elementAuthorFileList = elementAuthorFileList.addContent(courseA);

			// Saves the new author file.
			try {
				File xmlFile = new File(authorFileLocation + aCourseName + ".test");
				XMLOutputter out = new XMLOutputter();
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);

			} catch (IOException e) {
				UtilLog.writeException(e);
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Adds a question file to a course
	 * @param questionsFileName Name of the question file that will be added
	 * @param concept Name of the concept that is associated to the question file
	 * @param numberOfQuestions Number of questions that the question file contains.
	 * @param numberOfAnswersForFile Default number of answers of the question file
	 * @param authorName Login of the author
	 * @return false if any error occurs, true otherwise.
	 */
	public boolean addQuestionsFile(
			final String questionsFileName,
			final String concept,
			final int numberOfQuestions,
			final int numberOfAnswersForFile,
			final String authorName) {
	//{
		Document xmlDocument; 
		boolean add = false;

		// Checks if the file exists
		if (!courseExists()) {
			if (!addCourse(authorName)) {
				return false;
			}
		}

		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument,
					"fileList/course[@courseName=\"" + courseName + "\"]");

			// Looks for a possible question file with the same name.
			// In that case we don't have to add it
			Element elementQuestionsFileName = (Element) XPath.selectSingleNode(
					xmlDocument, "fileList/course[@courseName = \"" + courseName
							+ "\"]" + "/questionsFile"
							+ "/questionsFileName[text() = " + questionsFileName
							+ "]");
			if (elementQuestionsFileName == null)
				add = true;
			else {
				add = false;
				// Updates the values
				elementQuestionsFileName = elementQuestionsFileName.setAttribute(
						"concept", concept);
				elementQuestionsFileName = elementQuestionsFileName.setAttribute(
						"numberOfQuestions", String.valueOf(numberOfQuestions));
				elementQuestionsFileName = elementQuestionsFileName.setAttribute(
						"numberOfAnswersForFile", String
								.valueOf(numberOfAnswersForFile));
			}

			if (add == true) {
				// Adds the data
				elementQuestionsFileName = new Element("questionsFileName");
				elementQuestionsFileName = elementQuestionsFileName
						.setText(questionsFileName);
				elementQuestionsFileName = elementQuestionsFileName.setAttribute(
						"concept", concept);
				elementQuestionsFileName = elementQuestionsFileName.setAttribute(
						"numberOfQuestions", String.valueOf(numberOfQuestions));
				elementQuestionsFileName = elementQuestionsFileName.setAttribute(
						"numberOfAnswersForFile", String
								.valueOf(numberOfAnswersForFile));

				// Adds the "questionsFileName" element to the "questionsFile" node.
				course.getChild("questionsFile").addContent(
						elementQuestionsFileName);
			}

			// Saves the file
			File xmlFile = new File(itemsPath + fileListName + testEditorTypeFile);
			FileWriter xmlFileWriter = new FileWriter(xmlFile);
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
			out.output(xmlDocument, xmlFileWriter);

		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		String authorFileLocation = wowRoot
				+ authorRoot.substring(1, authorRoot.length()) + authorName + "/";

		// Checks if the author file exists
		if (!authorCourseExists(authorName)) {
			if (!addCourse(authorName)) 
				return false;
		}

		try {
			// Loads the author file
			File fileList = new File(authorFileLocation + courseName + ".test");
			xmlDocument = new SAXBuilder().build(fileList);
			
			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument,
					"fileList/course[@courseName=\"" + courseName + "\"]");

			// If the question file already exists, it is not necessary to add it.
			Element elementQuestionsFileName = (Element) XPath.selectSingleNode(
					xmlDocument, "fileList/course[@courseName = \"" + courseName
							+ "\"]" + "/questionsFile"
							+ "/questionsFileName[text() = " + questionsFileName
							+ "]");
			if (elementQuestionsFileName == null)
				add = true;
			else
			{
				add = false;
				elementQuestionsFileName = elementQuestionsFileName.setAttribute(
						"concept", concept);
				elementQuestionsFileName = elementQuestionsFileName.setAttribute(
						"numberOfQuestions", String.valueOf(numberOfQuestions));
				elementQuestionsFileName = elementQuestionsFileName.setAttribute(
						"numberOfAnswersForFile", String
								.valueOf(numberOfAnswersForFile));
			}

			if (add == true)
			{
				elementQuestionsFileName = new Element("questionsFileName");
				elementQuestionsFileName = elementQuestionsFileName
						.setText(questionsFileName);
				elementQuestionsFileName = elementQuestionsFileName.setAttribute(
						"concept", concept);
				elementQuestionsFileName = elementQuestionsFileName.setAttribute(
						"numberOfQuestions", String.valueOf(numberOfQuestions));
				elementQuestionsFileName = elementQuestionsFileName.setAttribute(
						"numberOfAnswersForFile", String
								.valueOf(numberOfAnswersForFile));
				
				course.getChild("questionsFile").addContent(
						elementQuestionsFileName);
			}

			// Saves the author file.
			File xmlFile = new File(authorFileLocation + courseName + ".test");
			FileWriter xmlFileWriter = new FileWriter(xmlFile);
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
			out.output(xmlDocument, xmlFileWriter);

		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		return true;
	}


	
	/**
	 * Add a question to the question file which name is received
	 * as argument. Updates the value of the number of questions
	 * of the fileList file. Also updates the author file.
	 * @param questionsFileName Name of the question file
	 * @param author Login of the author.
	 * @return false if any error occurs, true otherwise
	 */
	public boolean addQuestion(final String questionsFileName, final String author) {
		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);
			
			// Looks for the question file with the same name
			// If it exists, there's no need to add it to the file
			Element elementQuestionsFileName = (Element) XPath.selectSingleNode(
					xmlDocument, "fileList/course[@courseName = \"" + courseName
							+ "\"]" + "/questionsFile"
							+ "/questionsFileName[text() = \"" + questionsFileName
							+ "\"]");
			if (elementQuestionsFileName == null)
				return false;
			else
			{
				elementQuestionsFileName.getAttribute("numberOfQuestions")
						.setValue(
								String.valueOf(Integer.valueOf(
										elementQuestionsFileName.getAttribute(
												"numberOfQuestions").getValue().trim())
										.intValue() + 1));

				// Saves the file
				File xmlFile = new File(itemsPath + fileListName + testEditorTypeFile);
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
		
		// UPDATES THE AUTHOR FILE
		try {
			String authorFileLocation = wowRoot + authorRoot.substring(1, authorRoot.length()) + author + "/";
	
			// Loads the author file
			File fileList = new File(authorFileLocation + courseName + ".test");
			Document xmlDocument = new SAXBuilder().build(fileList);
			
			// Looks for the question file with the same name
			// If it doesn't exist, there's no need to add it to the file
			Element elementQuestionsFileName = (Element) XPath.selectSingleNode(
					xmlDocument, "fileList/course[@courseName = \"" + courseName
							+ "\"]" + "/questionsFile"
							+ "/questionsFileName[text() = \"" + questionsFileName
							+ "\"]");
			if (elementQuestionsFileName != null) {
				elementQuestionsFileName.getAttribute("numberOfQuestions")
						.setValue(
								String.valueOf(Integer.valueOf(
										elementQuestionsFileName.getAttribute(
												"numberOfQuestions").getValue().trim())
										.intValue() + 1));

				// Saves the author file
				File xmlFile = new File(authorFileLocation + courseName + ".test");
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);

			} else {
				return false;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
		
		return true;
	}

	
	/**
	 * Substract a question to the question file which name is received
	 * as argument. Updates the value of the number of questions
	 * of the fileList file. Also updates the author file.
	 * @param questionsFileName Name of the question file
	 * @param author Login of the author
	 * @return false if any error occurs, true otherwise
	 */
	public boolean subtractQuestion(final String questionsFileName, final String author)
	{
		Document xmlDocument;
		
		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			xmlDocument = new SAXBuilder().build(fileList);

			// Looks for a possible question file with the same name.
			// In that case there's no need to substract the question
			Element elementQuestionsFileName = (Element) XPath.selectSingleNode(xmlDocument,
					"fileList/course[@courseName = \"" + courseName + "\"]" + "/questionsFile"
							+ "/questionsFileName[text() = \"" + questionsFileName + "\"]");

			if (elementQuestionsFileName == null)
				return false;
			else {
				elementQuestionsFileName.getAttribute("numberOfQuestions")
						.setValue(
								String.valueOf(Integer.valueOf(
										elementQuestionsFileName.getAttribute(
												"numberOfQuestions").getValue().trim())
										.intValue() - 1));

				if (Integer.valueOf(
						elementQuestionsFileName.getAttribute("numberOfQuestions")
								.getValue().trim()).intValue() < 0)
					elementQuestionsFileName.getAttribute("numberOfQuestions")
							.setValue("0");

				// Saves the file
				File xmlFile = new File(itemsPath + fileListName + testEditorTypeFile);
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
		
		// UPDATES THE AUTHOR FILE
		try {
			// Loads the author file
			String authorFileLocation = wowRoot + authorRoot.substring(1, authorRoot.length()) + author + "/";
			File fileList = new File(authorFileLocation + courseName + ".test");
			xmlDocument = new SAXBuilder().build(fileList);
			
			// Looks for a question file with the same name.
			// If it doesn't exist there's no need to substract the question
			Element elementQuestionsFileName = (Element) XPath.selectSingleNode(
					xmlDocument, "fileList/course[@courseName = \"" + courseName
							+ "\"]" + "/questionsFile"
							+ "/questionsFileName[text() = \"" + questionsFileName
							+ "\"]");

			if (elementQuestionsFileName != null)
			{
				elementQuestionsFileName.getAttribute("numberOfQuestions")
						.setValue(
								String.valueOf(Integer.valueOf(
										elementQuestionsFileName.getAttribute(
												"numberOfQuestions").getValue().trim())
										.intValue() - 1));

				if (Integer.valueOf(
						elementQuestionsFileName.getAttribute("numberOfQuestions")
								.getValue().trim()).intValue() < 0)
					elementQuestionsFileName.getAttribute("numberOfQuestions")
							.setValue("0");

				// Saves the author file
				File xmlFile = new File(authorFileLocation + courseName + ".test");
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		return true;
	}

	
	/**
	 * Adds a classic test, that belongs to the course received as parameter, 
	 * to to the test file. The test also belongs to the author
	 * received as parameter. Also updates the test file of the author.
	 * @param testFileName Name of the test file.
	 * @param authorName Login of the author.
	 * @return false in error case, true otherwise.
	 */
	public boolean addClassicTestFile(final String testFileName, final String authorName)
	{
		Document xmlDocument;

		String authorFileLocation = wowRoot
				+ authorRoot.substring(1, authorRoot.length()) + authorName + "/";

		// Checks if the file exists
		if (!courseExists()) {
			if (!addCourse(authorName))
				return false;
		}

		try {
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			if (!fileList.exists() || fileList.isDirectory()) {
				if (!createFileList())
					return false;
			}

			xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument,
					"fileList/course[@courseName=\"" + courseName + "\"]");

			// Looks for a possible question file with the same name. In that case
			// there's no need to add the test.
			Element elementTestFileName = (Element) XPath.selectSingleNode(
					xmlDocument, "fileList/course[@courseName = \"" + courseName
							+ "\"]" + "/testFile/classic"
							+ "/classicTestFileName[text() = \"" + testFileName
							+ "\"]");

			if (elementTestFileName == null)
			{
				elementTestFileName = new Element("classicTestFileName");
				elementTestFileName = elementTestFileName.setText(testFileName);

				// Adds the "classicTestFileName" element to the classic node.
				course.getChild("testFile").getChild(TestEditor.CLASSIC).addContent(
						elementTestFileName);

				// Saves the file
				File xmlFile = new File(itemsPath + fileListName + testEditorTypeFile);
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		// Checks if the author file exists
		if (!authorCourseExists(authorName)) {
			if (!addCourse(authorName))
				return false;
		}

		try {
			File fileList = new File(authorFileLocation + courseName + ".test");
			if(!fileList.exists() || fileList.isDirectory()) {
				if (!createAuthorFileList(authorName))
					return false;
			}
			
			xmlDocument = new SAXBuilder().build(fileList);
			
			// Looks for the course in the author file
			Element course = (Element) XPath.selectSingleNode(xmlDocument,
					"fileList/course[@courseName=\"" + courseName + "\"]");

			Element elementTestFileName = (Element) XPath.selectSingleNode(
					xmlDocument, "fileList/course[@courseName = \"" + courseName
							+ "\"]" + "/testFile/classic"
							+ "/classicTestFileName[text() = \"" + testFileName
							+ "\"]");

			if (elementTestFileName == null) {
				// If it doesn't exists, it adds it to the author file
				elementTestFileName = new Element("classicTestFileName");
				elementTestFileName = elementTestFileName.setText(testFileName);

				// Adds the "classicTestFileName" element to the classic node.
				course.getChild("testFile").getChild(TestEditor.CLASSIC)
														.addContent(elementTestFileName);

				// Saves the file
				File xmlFile = new File(authorFileLocation + courseName + ".test");
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		return true;
	}

	
	/**
	 * Adds a adaptive test, that belongs to the course received as parameter, to
	 * to the test file. The test also belongs to the author received as
	 * parameter. Also updates the test file of the author.
	 * @param testFileName Name of the test file.
	 * @param authorName Login of the author.
	 * @return false in error case, true otherwise.
	 */
	public boolean addAdaptiveTestFile(final String testFileName, final String authorName){
		// Checks if the file exists
		if (!courseExists()) {
			if (!addCourse(authorName))
				return false;
		}

		try {
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			if (!fileList.exists() || fileList.isDirectory()) {
				if (!createFileList())
					return false;
			}
			
			Document xmlDocument = new SAXBuilder().build(fileList);
			
			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument,
					"fileList/course[@courseName=\"" + courseName + "\"]");

			// Looks for a possible question file with the same name. In that case
			// there's no need to add the test
			Element elementTestFileName = (Element) XPath.selectSingleNode(
					xmlDocument, "fileList/course[@courseName = \"" + courseName
							+ "\"]" + "/testFile/adaptive"
							+ "/adaptiveTestFileName[text() = \"" + testFileName
							+ "\"]");
			
			if (elementTestFileName == null) {
				elementTestFileName = new Element("adaptiveTestFileName");
				elementTestFileName = elementTestFileName.setText(testFileName);

				// Adds the "adaptiveTestFileName" element to the adaptive node.
				course.getChild("testFile").getChild(TestEditor.ADAPTIVE).addContent(elementTestFileName);

				// Saves the file
				File xmlFile = new File(itemsPath + fileListName + testEditorTypeFile);
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
		
		// UPDATES THE AUTHOR FILE IF NEEDED
		
		// Checks if the author file exists
		String authorFileLocation = wowRoot
				+ authorRoot.substring(1, authorRoot.length()) + authorName + "/";
		
		if (!authorCourseExists(authorName)) {
			if (!addCourse(authorName))
				return false;
		}

		try {
			File fileList = new File(authorFileLocation + courseName + ".test");
			if (!fileList.exists() || fileList.isDirectory()) {
				if (!createAuthorFileList(authorName))
					return false;
			}
			
			// Parses the file
			Document xmlDocument = new SAXBuilder().build(fileList);
			
			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument,
					"fileList/course[@courseName=\"" + courseName + "\"]");

			// Looks for a possible adaptive test with the same name. In that case
			// there's no need to add it
			Element elementTestFileName = (Element) XPath.selectSingleNode(
					xmlDocument, "fileList/course[@courseName = \"" + courseName
							+ "\"]" + "/testFile/adaptive"
							+ "/adaptiveTestFileName[text() = \"" + testFileName
							+ "\"]");
			
			if (elementTestFileName == null) {
				elementTestFileName = new Element("adaptiveTestFileName");
				elementTestFileName = elementTestFileName.setText(testFileName);

				// Adds the "adaptiveTestFileName" element to the adaptive node.
				course.getChild("testFile").getChild(TestEditor.ADAPTIVE)
															.addContent(elementTestFileName);

				// Saves the file
				File xmlFile = new File(authorFileLocation + courseName + ".test");
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		return true;
	}

	/**
	 * Adds a student log file to a course. Only updates the test file, not the
	 * author file.
	 * This method must be static and synchronized to avoid problems when several
	 * students begin the test at the same time.
	 * @param aCourseName Name of a course
	 * @param studentFileName Name of the student log file that will be added.
	 * @return false in error case, true otherwise.
	 */
	public synchronized static final boolean addStudentFile(
			final String aCourseName, final String studentFileName) {
		// Checks if the course exists
		if (!courseExists(aCourseName)) {
			// If doesn't exists, adds it
			if (!addCourse(aCourseName, null))
				return false;
		}

		// Checks if the file exists
		File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
		if (!fileList.exists() || fileList.isDirectory()) {
			// If doesn't exists, creates it
			if (!createFileList())
				return false;
		}

		try {
			// Parses the file
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ aCourseName + "\"]");

			// Looks for a possible question file with the same name. In that case,
			// it doesn't need to add the student file
			Element elementStudentFileName = (Element) XPath.selectSingleNode(xmlDocument,
					"fileList/course[@courseName = \"" + aCourseName + "\"]" + "/studentFile"
							+ "/studentFileName[text() = \"" + studentFileName + "\"]");

			if (elementStudentFileName == null) {
				elementStudentFileName = new Element("studentFileName");
				elementStudentFileName = elementStudentFileName.setText(studentFileName);

				// Adds the "questionFileName" element to the "questionFile" node
				course.getChild("studentFile").addContent(elementStudentFileName);

				// Saves the file
				FileWriter xmlFileWriter = new FileWriter(new File(itemsPath + fileListName + testEditorTypeFile));
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		return true;
	}

	
	/**
	 * Deletes a question file from a course. Updates the file and
	 * the author file.
	 * @param questionsFileName Name of the question file that will be deleted.
	 * @param authorName Login of the author.
	 * @return false in error case, true otherwise.
	 */
	public boolean deleteQuestionsFile(final String questionsFileName, final String authorName) {
		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				// Looks for a possible question file with the same name.
				// If exists, we will remove it.
				Element elementQuestionsFileName = (Element) XPath.selectSingleNode(
						xmlDocument, "fileList/course[@courseName = \"" + courseName
								+ "\"]" + "/questionsFile"
								+ "/questionsFileName[text() = \"" + questionsFileName
								+ "\"]");
			
				if (elementQuestionsFileName != null) {
					// Removes the "questionsFileName" element from the
					// "questionsFile" node.
					course.getChild("questionsFile").removeContent(elementQuestionsFileName);

					// Saves the file
					File xmlFile = new File(itemsPath + fileListName + testEditorTypeFile);
					FileWriter xmlFileWriter = new FileWriter(xmlFile);
					XMLOutputter out = new XMLOutputter();
					out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
					out.output(xmlDocument, xmlFileWriter);
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		// UPDATES THE AUTHOR FILE
		String authorFileLocation = wowRoot
				+ authorRoot.substring(1, authorRoot.length()) + authorName + "/";
		try {
			// Loads the author file
			File fileList = new File(authorFileLocation + courseName + ".test");
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				// Looks for the question file
				Element elementQuestionsFileName = (Element) XPath.selectSingleNode(xmlDocument,
						"fileList/course[@courseName = \"" + courseName + "\"]" + "/questionsFile"
								+ "/questionsFileName[text() = \"" + questionsFileName + "\"]");
				if (elementQuestionsFileName != null) {
					// Removes the "questionsFileName" element from the
					// "questionsFile" node.
					course.getChild("questionsFile").removeContent(elementQuestionsFileName);

					// Saves the file
					File xmlFile = new File(authorFileLocation + courseName + ".test");
					FileWriter xmlFileWriter = new FileWriter(xmlFile);
					XMLOutputter out = new XMLOutputter();
					out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
					out.output(xmlDocument, xmlFileWriter);
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		return true;
	}


	/**
	 * Removes a classic test file from a course. Also updates
	 * the author file.
	 * @param testFileName Name of the test file to remove.
	 * @param authorName Login of the author.
	 * @return true if success
	 */
	public boolean deleteClassicTestFile(final String testFileName, final String authorName)
	{
		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				// Looks for the classic test that belongs to the course
				Element elementTestFileName = (Element) XPath.selectSingleNode(xmlDocument,
						"fileList/course[@courseName = \"" + courseName + "\"]" + "/testFile/classic"
								+ "/classicTestFileName[text() = \"" + testFileName + "\"]");

				if (elementTestFileName != null) {
					// Removes the "classicTestFileName" element from the classic node.
					course.getChild("testFile").getChild(TestEditor.CLASSIC)
																.removeContent(elementTestFileName);

					// Saves the file
					File xmlFile = new File(itemsPath + fileListName + testEditorTypeFile);
					FileWriter xmlFileWriter = new FileWriter(xmlFile);
					XMLOutputter out = new XMLOutputter();
					out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
					out.output(xmlDocument, xmlFileWriter);
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		// UPDATES THE AUTHOR FILE
		String authorFileLocation = wowRoot + authorRoot.substring(1, authorRoot.length()) + authorName + "/";

		try {
			File fileList = new File(authorFileLocation + courseName + ".test");
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument,
					"fileList/course[@courseName=\"" + courseName + "\"]");

			if (course != null) {
				// Looks for the classic test
				Element elementTestFileName = (Element) XPath.selectSingleNode(xmlDocument,
						"fileList/course[@courseName = \"" + courseName + "\"]" + "/testFile/classic"
								+ "/classicTestFileName[text() = \"" + testFileName + "\"]");

				if (elementTestFileName != null) {
					// Removes the "classicTestFileName" element from the classic node.
					course.getChild("testFile").getChild(TestEditor.CLASSIC)
															.removeContent(elementTestFileName);

					// Saves the file
					File xmlFile = new File(authorFileLocation + courseName + ".test");
					FileWriter xmlFileWriter = new FileWriter(xmlFile);
					XMLOutputter out = new XMLOutputter();
					out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
					out.output(xmlDocument, xmlFileWriter);
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		return true;
	}

	
	/**
	 * Removes an adaptive test file from a course. Also updates the author file. 
	 * @param testFileName Name of the test file to remove.
	 * @param authorName Login of the author.
	 * @return true or false
	 */
	public boolean deleteAdaptiveTestFile(final String testFileName, final String authorName)
	{
		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				// Looks for the adaptive test file.
				Element elementTestFileName = (Element) XPath.selectSingleNode(xmlDocument,
						"fileList/course[@courseName = \"" + courseName + "\"]" + "/testFile/adaptive"
								+ "/adaptiveTestFileName[text() = \"" + testFileName + "\"]");
				if (elementTestFileName != null) {
					// Removes the "classicTestFileName" element from the adaptive
					// node.
					course.getChild("testFile").getChild(TestEditor.ADAPTIVE)
															.removeContent(elementTestFileName);

					// Saves the file
					File xmlFile = new File(itemsPath + fileListName + testEditorTypeFile);
					FileWriter xmlFileWriter = new FileWriter(xmlFile);
					XMLOutputter out = new XMLOutputter();
					out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
					out.output(xmlDocument, xmlFileWriter);
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		// UPDATES THE AUTHOR FILE
		String authorFileLocation = wowRoot + authorRoot.substring(1, authorRoot.length()) + authorName + "/";
		try {
			// Loads the author file
			File fileList = new File(authorFileLocation + courseName + ".test");
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				// Looks for the adaptive test
				Element elementTestFileName = (Element) XPath.selectSingleNode(xmlDocument,
						"fileList/course[@courseName = \"" + courseName + "\"]" + "/testFile/adaptive"
								+ "/adaptiveTestFileName[text() = \"" + testFileName + "\"]");
				if (elementTestFileName != null) {
					// Removes the "classicTestFileName" element from the adaptive
					// node.
					course.getChild("testFile").getChild(TestEditor.ADAPTIVE)
															.removeContent(elementTestFileName);

					// Saves the file
					File xmlFile = new File(authorFileLocation + courseName + ".test");
					FileWriter xmlFileWriter = new FileWriter(xmlFile);
					XMLOutputter out = new XMLOutputter();
					out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
					out.output(xmlDocument, xmlFileWriter);
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		return true;
	}


	/**
	 * Deletes a student file from a course. It doesn't updates the author file.
	 * @param studentFileName Name of the student file.
	 * @return false in error case, true otherwise.
	 */
	public boolean deleteStudentFile(final String studentFileName)
	{
		try {
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				// Looks for the student file
				Element elementStudentFileName = (Element) XPath.selectSingleNode(xmlDocument,
						"fileList/course[@courseName = \"" + courseName + "\"]" + "/studentFile"
								+ "/studentFileName[text() = \"" + studentFileName + "\"]");

				if (elementStudentFileName != null) {
					// Removes the "studentFileName" from the "studentFile" node.
					course.getChild("studentFile").removeContent(elementStudentFileName);

					// Saves the file
					File xmlFile = new File(itemsPath + fileListName + testEditorTypeFile);
					FileWriter xmlFileWriter = new FileWriter(xmlFile);
					XMLOutputter out = new XMLOutputter();
					out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
					out.output(xmlDocument, xmlFileWriter);
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		return true;
	}

	
	/**
	 * Returns a Vector object with the names of the question files for the
	 * course received as parameter.
	 * @return A Vector object
	 */
	public Vector getQuestionsFileNames()
	{
		Vector questionsFileNames = null;
		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				List questionsFileNameList = course.getChild("questionsFile").getChildren();

				if (questionsFileNameList != null && !questionsFileNameList.isEmpty()) {
					// Adds the data to the vector
					questionsFileNames = new Vector();
					for (int i = 0; i < questionsFileNameList.size(); i++) {
						questionsFileNames.add(((Element) questionsFileNameList.get(i)).getText());
					}
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}

		return questionsFileNames;
	}

	
	/**
	 * Returns a Vector object with the names of the question files associated to
	 * the concepts received for the course received as parameter.
	 * @param conceptNameVector A Vector object with concept names.
	 * @return The names of the question files associated to the concept.
	 */
	public Vector getQuestionsFileNamesForConcept(final Vector conceptNameVector)
	{
		Vector questionsFileNames = null;

		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				// Loops to search the question files
				questionsFileNames = new Vector();

				for (int i = 0; i < conceptNameVector.size(); i++) {
					String conceptName = conceptNameVector.get(i).toString();

					List questionsFileNameList = XPath.selectNodes(xmlDocument, "fileList/course[@courseName = \""
							+ courseName + "\"]" + "/questionsFile//questionsFileName[@concept = \"" + conceptName
							+ "\"]");

					if (questionsFileNameList.isEmpty() == false) {
						for (int j = 0; j < questionsFileNameList.size(); j++) {
							// Adds the question file name to the vector.
							questionsFileNames.add(((Element) questionsFileNameList.get(j)).getText());
						}
					}
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}

		return questionsFileNames;
	}

	
	/**
	 * Returns a Vector object with the number of question that contains the
	 * question files associated with the concepts received in the Vector object.
	 * @param conceptNameVector A Vector that contains concepts names.
	 * @return A Vector with question codes associated to the concepts.
	 */
	public Vector getNumberOfQuestionsForConcept(final Vector conceptNameVector)
	{
		Vector numberOfQuestions = null;
		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				// Loops to search the question files
				numberOfQuestions = new Vector();
				for (int i = 0; i < conceptNameVector.size(); i++) {
					String conceptName = conceptNameVector.get(i).toString();

					List questionsFileNameList = XPath.selectNodes(xmlDocument, "fileList/course[@courseName = \""
							+ courseName + "\"]" + "/questionsFile//questionsFileName[@concept = \"" + conceptName
							+ "\"]");

					if (questionsFileNameList.isEmpty() == false) {
						for (int j = 0; j < questionsFileNameList.size(); j++) {
							// Adds the "numberOfQuestions" attribute to the vector.
							numberOfQuestions.add(((Element) questionsFileNameList.get(j)).getAttribute(
									"numberOfQuestions").getValue().trim());
						}
					}
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}

		return numberOfQuestions;
	}

	
	/**
	 * Returns a Vector object with the default number of answers for the
	 * questions contained in the question files associated to the concepts
	 * received as parameter.
	 * @param conceptNameVector A Vector with concepts names.
	 * @return A Vector object.
	 */
	public Vector getNumberOfAnswersForConcept(final Vector conceptNameVector)
	{
		Vector numberOfAnswers = null;
		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				// Loops to search the question files
				numberOfAnswers = new Vector();
				for (int i = 0; i < conceptNameVector.size(); i++) {
					String conceptName = conceptNameVector.get(i).toString();
	
					List questionsFileNameList = XPath.selectNodes(xmlDocument, "fileList/course[@courseName = \""
							+ courseName + "\"]" + "/questionsFile//questionsFileName[@concept = \"" + conceptName
							+ "\"]");

					if (questionsFileNameList.isEmpty() == false) {
						for (int j = 0; j < questionsFileNameList.size(); j++) {
							numberOfAnswers.add(((Element) questionsFileNameList.get(j)).getAttribute(
									"numberOfAnswersForFile").getValue().trim());
						}
					}
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}

		return numberOfAnswers;
	}


	/**
	 * Returns a Vector object with the names of the concepts associated to the
	 * question files for the course that this class represents
	 * @return A Vector object with names of concepts.
	 */	
	public Vector getConceptsForQuestionsFile()
	{
		Vector conceptForFile = null;
		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				List questionsFileNameList = course.getChild("questionsFile").getChildren();

				if (questionsFileNameList != null && !questionsFileNameList.isEmpty()) {
					conceptForFile = new Vector();
					for (int i = 0; i < questionsFileNameList.size(); i++)
						conceptForFile.add(((Element) questionsFileNameList.get(i)).getAttribute("concept")
								.getValue().trim());
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}

		return conceptForFile;
	}

	
	/**
	 * Return a Vector object with the number of questions that contains the
	 * question files that belongs to the course that this class represents
	 * @return A Vector object.
	 */
	public Vector getNumberOfQuestions() {
		Vector numberOfQuestions = null;
		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				List questionsFileNameList = course.getChild("questionsFile").getChildren();
				if (questionsFileNameList != null && !questionsFileNameList.isEmpty()) {
					numberOfQuestions = new Vector();
					for (int i = 0; i < questionsFileNameList.size(); i++)
						numberOfQuestions.add(((Element) questionsFileNameList.get(i)).getAttribute(
								"numberOfQuestions").getValue().trim());
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}

		return numberOfQuestions;
	}

	
	/**
	 * Returns a Vector object with the default number of answers for the
	 * question files that belongs to the course that this class represents
	 * @return A Vector object
	 */
	public Vector getNumberOfAnswersForQuestionsFile() {
		Vector numberOfAnswersForFile = null;
		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				List questionsFileNameList = course.getChild("questionsFile").getChildren();
				if (questionsFileNameList != null && !questionsFileNameList.isEmpty()) {
					numberOfAnswersForFile = new Vector();
					for (int i = 0; i < questionsFileNameList.size(); i++)
						numberOfAnswersForFile.add(((Element) questionsFileNameList.get(i)).getAttribute(
								"numberOfAnswersForFile").getValue().trim());
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}

		return numberOfAnswersForFile;
	}

	
	/**
	 * Returns a Vector object with the names of the classic test files that
	 * belongs to the course that this class represents
	 * @return A Vector object with the name of classic tests.
	 */ 
	public Vector getClassicTestFileNames() {
		Vector testFileNames = null;
		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Busqueda del curso.
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				List testFileNameList = course.getChild("testFile")
																.getChild(TestEditor.CLASSIC)
																	.getChildren();

				if (testFileNameList != null && !testFileNameList.isEmpty()) {
					testFileNames = new Vector();
					for (int i = 0; i < testFileNameList.size(); i++)
						testFileNames.add(((Element) testFileNameList.get(i)).getText());
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}

		return testFileNames;
	}

	/**
	 * Returns a Vector object with the names of the adaptive test files that
	 * belongs to the course that this class represents
	 * @return A Vector object with the name of adaptive tests.
	 */
	public Vector getAdaptiveTestFileNames() {
		Vector testFileNames = null;
		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				List testFileNameList = course.getChild("testFile")
																.getChild(TestEditor.ADAPTIVE)
																		.getChildren();
				if (testFileNameList != null && !testFileNameList.isEmpty()) {
					testFileNames = new Vector();
					for (int i = 0; i < testFileNameList.size(); i++)
						testFileNames.add(((Element) testFileNameList.get(i)).getText());
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}

		return testFileNames;
	}

	
	/**
	 * Returns a Vector object with the names of the student log files that
	 * belongs to the course that this class represents
	 * @return A Vector with the names of the student log files for the course.
	 */
	public Vector getStudentFileNames() {
		Vector studentFileNames = null;
		try {
			// Loads the file
			File fileList = new File(itemsPath + fileListName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileList);

			// Looks for the course
			Element course = (Element) XPath.selectSingleNode(xmlDocument, "fileList/course[@courseName=\""
					+ courseName + "\"]");

			if (course != null) {
				List studentFileNameList = course.getChild("studentFile").getChildren();

				if (studentFileNameList != null && !studentFileNameList.isEmpty()) {
					studentFileNames = new Vector();
					for (int i = 0; i < studentFileNameList.size(); i++)
						studentFileNames.add(((Element) studentFileNameList.get(i)).getText());
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}

		return studentFileNames;
	}

} // End of FileList class