package es.uco.WOW.QuestionsFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import vn.spring.WOW.WOWStatic;

import org.jdom.Attribute;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import es.uco.WOW.FileList.FileList;
import es.uco.WOW.TestConverter.FromGift;
import es.uco.WOW.TestConverter.FromHotPotatoesv6;
import es.uco.WOW.TestConverter.FromMoodleXML;
import es.uco.WOW.TestConverter.FromQti12;
import es.uco.WOW.TestConverter.FromQti20;
import es.uco.WOW.TestConverter.FromSiette;
import es.uco.WOW.TestConverter.FromWebCT;
import es.uco.WOW.TestConverter.ToGift;
import es.uco.WOW.TestConverter.ToHotPotatoesv6;
import es.uco.WOW.TestConverter.ToMoodleXML;
import es.uco.WOW.TestConverter.ToQti12;
import es.uco.WOW.TestConverter.ToQti20;
import es.uco.WOW.TestConverter.ToSiette;
import es.uco.WOW.TestConverter.ToWebCT;
import es.uco.WOW.TestEditor.TestEditor;
import es.uco.WOW.TestFile.TestFile;
import es.uco.WOW.Utils.Course;
import es.uco.WOW.Utils.Question;
import es.uco.WOW.Utils.TempFile;
import es.uco.WOW.Utils.UtilLog;

/**
 * <p>Title: Wow! TestEditor</p> <p>Description: Herramienta para la edicion
 * de preguntas tipo test adaptativas </p> <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */

/**
 * NAME: QuestionFile
 * FUNCTION: This class manages all the options related to
 * the question files of the current version of the TestEditor tool. Creates
 * question files, adds question to them, deletes question from them and updates
 * the data of the questions.
 * LAST MODIFICATION: 06-02-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class QuestionFile {
	
	/** Name of the root folder where the items and tests are stored */
	private static String itemsPath = WOWStatic.config.Get("XMLROOT") + WOWStatic.config.Get("itemspath");

	/** Name of the folder where the question files are stored */
	private static String itemsFilesPath = WOWStatic.config.Get("itemsfilespath");

	/** Path to the folder where the images associated to the question files are stored. */
	private static String imagesItemsPath = WOWStatic.config.Get("imagesitemspath");

	/** Type of file that the TestEditor tool works with */
	private static String testEditorTypeFile = WOWStatic.config.Get("testeditortypefile");

// Member variables
	/**
	 * Name of the course that this question file belongs to
	 */
	private String courseName = null;
	/**
	 * Name of the question file
	 */
	private String questionsFileName = null;

// End of Member variables	
	
	/**
	 * Public Constructor
	 * @param aCourseName Name of a course
	 * @param aQuestionFileName Name of the question file
	 */
	public QuestionFile(final String aCourseName, final String aQuestionFileName) {
		this.courseName = aCourseName;
		this.questionsFileName = aQuestionFileName;
	}
	
	/**
	 * Checks if a file with the same name as the received as parameter exists.
	 * @return true if a file with the same name exists, false otherwise.
	 */
	public boolean exists() {
		File file = new File(itemsPath + courseName + itemsFilesPath + questionsFileName + testEditorTypeFile);
		return (file.exists() && file.isFile() && file.canWrite());
	}

	/**
	 * Checks if the question received as parameter, belongs to any test that has
	 * not been finished by the students.
	 * @param codeQuestion Code that identifies a question
	 * @return false if any test has not been finished, true otherwise.
	 */
	public boolean checkQuestionInTest(final String codeQuestion) {
		try {
			// Loads the question file
			File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
					+ testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileQuestions);

			List classicTestFilePathList = XPath.selectNodes(xmlDocument, "fileOfQuestions/questions"
					+ "/question[@codeQuestion=\"" + codeQuestion + "\"]" + "/testFiles/classicTest"
					+ "//classicTestFilePath");

			List adaptiveTestFilePathList = XPath.selectNodes(xmlDocument, "fileOfQuestions/questions"
					+ "/question[@codeQuestion=\"" + codeQuestion + "\"]" + "/testFiles/adaptiveTest"
					+ "//adaptiveTestFilePath");

			if (!classicTestFilePathList.isEmpty() || !adaptiveTestFilePathList.isEmpty()) {
				boolean testFinished = false;
				for (int i = 0; i < classicTestFilePathList.size(); i++) {
					Element classicTestFilePath = (Element) classicTestFilePathList.get(i);

					String testFileName = classicTestFilePath.getText().substring(
							classicTestFilePath.getText().lastIndexOf("/") + 1,
							classicTestFilePath.getText().lastIndexOf(testEditorTypeFile));

					TestFile testFile = new TestFile(courseName, testFileName);
					testFinished = testFile.checkStudentFinishTest();

					// If any classic test is not finished, returns false
					if (testFinished == false)
						return false;
				}

				for (int i = 0; i < adaptiveTestFilePathList.size(); i++) {
					Element adaptiveTestFilePath = (Element) adaptiveTestFilePathList.get(i);

					String testFileName = adaptiveTestFilePath.getText().substring(
							adaptiveTestFilePath.getText().lastIndexOf("/") + 1,
							adaptiveTestFilePath.getText().lastIndexOf(testEditorTypeFile));
					
					TestFile testFile = new TestFile(courseName, testFileName);
					testFinished = testFile.checkStudentFinishTest();

					// If any adaptive test is not finished, returns false
					if (testFinished == false)
						return false;
				}
			}
			return true; // Otherwise returns true
		} catch (Exception e) {
			UtilLog.writeException(e);
			return true;
		}
	}

	/**
	 * Checks if there are any question in the question file that belongs to a
	 * test that has not been finished by the students
	 * @return false if any test has not been finished, true otherwise.
	 */
	public boolean checkQuestionsFileInTest() {
		try {
			// Loads the question file
			File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
					+ testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileQuestions);

			List classicTestFilePathList = XPath.selectNodes(xmlDocument, "fileOfQuestions/questions"
					+ "//question/testFiles/classicTest" + "//classicTestFilePath");

			List adaptiveTestFilePathList = XPath.selectNodes(xmlDocument, "fileOfQuestions/questions"
					+ "//question/testFiles/adaptiveTest" + "//adaptiveTestFilePath");

			// This Vector object contains the elements already analysed to
			// avoid repetition of elements
			Vector classicTestFilePathVector = new Vector();
			Vector adaptiveTestFilePathVector = new Vector();

			boolean testFinished = false;
			for (int i = 0; i < classicTestFilePathList.size(); i++) {
				Element classicTestFilePath = (Element) classicTestFilePathList.get(i);

				// Avoid repetitions
				if (classicTestFilePathVector.contains(classicTestFilePath.getText()) == false) {
					String testFileName = classicTestFilePath.getText().substring(
							classicTestFilePath.getText().lastIndexOf("/") + 1,
							classicTestFilePath.getText().lastIndexOf(testEditorTypeFile));

					TestFile testFile = new TestFile(courseName, testFileName);
					testFinished = testFile.checkStudentFinishTest();

					// If any classic test is not finished, returns false
					if (testFinished == true)
						classicTestFilePathVector.add(classicTestFilePath.getText());
					else
						return false;
				}
			}

			for (int i = 0; i < adaptiveTestFilePathList.size(); i++) {
				Element adaptiveTestFilePath = (Element) adaptiveTestFilePathList.get(i);

				if (adaptiveTestFilePathVector.contains(adaptiveTestFilePath.getText()) == false) {
					String testFileName = adaptiveTestFilePath.getText().substring(
							adaptiveTestFilePath.getText().lastIndexOf("/") + 1,
							adaptiveTestFilePath.getText().lastIndexOf(testEditorTypeFile));

					TestFile testFile = new TestFile(courseName, testFileName);
					testFinished = testFile.checkStudentFinishTest();

					// If any adaptive test is not finished, returns false
					if (testFinished == true)
						adaptiveTestFilePathVector.add(adaptiveTestFilePath.getText());
					else
						return false;
				}
			}
			return true;

		} catch (Exception e) {
			UtilLog.writeException(e);
			return true;
		}
	}


	/**
	 * Checks if a old question file exists with the same name that the one
	 * that will be exported. Returns true or false
	 * @param testOldPath Path to the file that will be created
	 * @return true or false
	 */
	public static boolean checkQuestionsFileOld(final String testOldPath) {
		File file = new File(WOWStatic.config.Get("WOWROOT") + testOldPath + testEditorTypeFile);
		return file.exists() && file.isFile() && file.canWrite();
	}


	 /**
	  * Creates the folder, in case it doesn't exist, with the name of
	  * the course of the question file.
	  * @return true or false
	  */
	 public boolean createDirectory() {
		 File folderTest = new File(itemsPath + courseName + itemsFilesPath);

		 if (folderTest.exists() == false)
			 folderTest.mkdirs();

		 // Checks if the destiny file exists
		 File fileOfQuestionsDTDNew = new File(itemsPath + "fileOfQuestions.dtd");
		 if (fileOfQuestionsDTDNew.exists() == true)
			 return true;

		// Copies the DTD file to the folder where the question files will be stored
		try {
			File fileOfQuestionsDTD = new File(WOWStatic.config.Get("WOWROOT").substring(0,
					WOWStatic.config.Get("WOWROOT").length() - 1)
					+ WOWStatic.config.Get("testeditordtdpath") + "fileOfQuestions.dtd");

			byte [] data = new byte[(int) fileOfQuestionsDTD.length()];

			BufferedInputStream input = new BufferedInputStream(new FileInputStream(fileOfQuestionsDTD));
			DataInputStream dataInputStream = new DataInputStream(input);
			dataInputStream.read(data);
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(itemsPath + "fileOfQuestions.dtd"));
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
	  * Creates the necessary route of folders, in case that it doesn't exist,
	  * to store the image that will be associated to the question file.
	  * Finally, stores the image in the folder.
	  * @param codeQuestion Code of the question that the image will be associated to
	  * @param fileType File type of the image
	  * @param data Image in byte format.
	  * @return true or false
	  */
	public boolean saveImage(
			final String codeQuestion,
			final String fileType,
			final byte [] data) {
	//{
		boolean createDirectory = false;
		File directoryTestImages = new File(itemsPath + courseName + imagesItemsPath + questionsFileName);

		createDirectory = directoryTestImages.exists();
		if (createDirectory == false)
			createDirectory = directoryTestImages.mkdirs();

		if (createDirectory == true) {
			try {
				// Creates the image file
				FileOutputStream fileOutputStream = new FileOutputStream(itemsPath + courseName + imagesItemsPath
						+ questionsFileName + "/" + questionsFileName + "_" + codeQuestion + fileType);
				DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
				dataOutputStream.write(data);
			} catch (java.io.FileNotFoundException fnfe) {
				return false;
			} catch (java.io.IOException ioe) {
				return false;
			}
		}

		return createDirectory;
	}


	 /**
	  * Copy a image from a location to another one.
	  * @param pathImageOld Source image
	  * @param pathImageNew New location of the image
	  * @return true or false
	  */
	 public static boolean copyImage(final String pathImageOld, final String pathImageNew) {
		try {
			// Reads the source file
			File imageFileIn = new File(itemsPath + pathImageOld);

			// Gets the bytes of the source file
			byte [] data = new byte[(int) imageFileIn.length()];
			BufferedInputStream input = new BufferedInputStream(new FileInputStream(itemsPath + pathImageOld));
			DataInputStream dataInputStream = new DataInputStream(input);
			dataInputStream.read(data);

			// Creates the destiny folder
			File imageDirectoryOut = null;
			if (pathImageNew.lastIndexOf("/") != -1)
				imageDirectoryOut = new File(itemsPath + pathImageNew.substring(0, pathImageNew.lastIndexOf("/")));
			else if (pathImageNew.lastIndexOf("\\") != -1)
				imageDirectoryOut = new File(itemsPath + pathImageNew.substring(0, pathImageNew.lastIndexOf("\\")));
			else
				return false;

			if (!imageDirectoryOut.exists()) {
				if (!imageDirectoryOut.mkdirs())
					return false;
			}

			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(itemsPath + pathImageNew));
			DataOutputStream dataOutputStream = new DataOutputStream(out);
			dataOutputStream.write(data);
			out.close();

			return true;

		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	}


	 /**
	  * Creates the folder to store the exported file.
	  * @param testOldPath Path to a question file of an older WOW! version
	  * @return true or false
	  */
	 public static boolean createDirectoryOld(final String testOldPath) {
		 File folderTest = new File(WOWStatic.config.Get("WOWROOT")
				+ testOldPath.substring(0, testOldPath.lastIndexOf("/")));

		boolean createDirectory = false;
		if (folderTest.exists() == false) {
			createDirectory = folderTest.mkdirs();
		}
		return createDirectory;
	}


	 /**
	  * Creates a new empty question file
	  * @param conceptName Concept that the question file is associated
	  * @param numAnswers Number of answers that each question will have.
	  * @param authorName Login of the author
	  * @return true or false
	  */
	public boolean createQuestionsFile(
			 final String conceptName,
			 final int numAnswers,
			 final String authorName) {
	//{ 
		// Creates the DocType of the document and assigns it
		DocType docType = new DocType("fileOfQuestions", "../../fileOfQuestions.dtd");
		Document xmlDocument = new Document();
		xmlDocument = xmlDocument.setDocType(docType);

		// Creates the root element of the document
		Element fileOfQuestions = new Element("fileOfQuestions");
		fileOfQuestions = fileOfQuestions.setAttribute("fileName", questionsFileName + testEditorTypeFile);
		fileOfQuestions = fileOfQuestions.setAttribute("name", questionsFileName);
		fileOfQuestions = fileOfQuestions.setAttribute("course", courseName);
		fileOfQuestions = fileOfQuestions.setAttribute("path", courseName + itemsFilesPath + questionsFileName
				+ testEditorTypeFile);
		fileOfQuestions = fileOfQuestions.setAttribute("numberOfAnswersForFile", String.valueOf(numAnswers));

		// Sets the root element
		xmlDocument = xmlDocument.setRootElement(fileOfQuestions);

		// Creates another elements of the document

		// Creates the "concept" element
		Element concept = new Element("concept");
		concept = concept.setAttribute("value", courseName + "." + conceptName);

		// Creates the "questions" element
		Element questions = new Element("questions");

		fileOfQuestions = fileOfQuestions.addContent(concept);
		fileOfQuestions = fileOfQuestions.addContent(questions);

		// Saves the new created file
		try {
			boolean create = createDirectory();
			if (create == false)
				return false;

			File xmlFile = new File(itemsPath + courseName + itemsFilesPath + questionsFileName + testEditorTypeFile);
			FileWriter xmlFileWriter = new FileWriter(xmlFile);
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
			out.output(xmlDocument, xmlFileWriter);

			// Adds the name of the file to the list of files
			FileList fileList = new FileList(courseName);
			create = fileList.addQuestionsFile(questionsFileName, courseName + "." + conceptName,
															0, numAnswers, authorName);

			return create;

		} catch (IOException e) {
			UtilLog.writeException(e);
			return false;
		}
	}


	/**
	 * Adds a question to the indicated question file. Sets a code to the added
	 * question that is the result of adding 1 to the code of the last question
	 * of the file.
	 * @param question Question object to add.
	 * @param author Login of the author.
	 * @return A int with a value < 0 in error case, 1 otherwise.
	 */
	public int addQuestionToFile(final Question question, final String author) {
		// Root element of the document
		Element fileOfQuestions;
		// Root node of the questions
		Element elementQuestions;
		// Element that contains a question of the file
		Element elementQuestion;
		// Root node of the answers to a question
		Element elementAnswers;
		// ELement that contains the answer to a question
		Element elementAnswer;
		// List that contains all the questions of the file
		List questionsList;
		// Name of the course
		//                                String courseName = question.getCourse();
		// Name of the question file
		//                                String questionsFile = question.getQuestionsFileName();

		try {
			// Loads the file
			File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
					+ testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileQuestions);

			// Obtains the root element of the document
			fileOfQuestions = xmlDocument.getRootElement();

			// Checks if the question to add belongs to the concept
			// that this question file is associated to
			if (question.getConcept() != null && question.getConcept().trim().equals("") == false) {
				Element concept = fileOfQuestions.getChild("concept");
				if (concept.getAttribute("value").getValue().trim().equals(question.getConcept()) == false)
					return -3;
			}

			// Obtains the root node of the questions of the file
			elementQuestions = fileOfQuestions.getChild("questions");

			// Obtains the list of questions of the file
			questionsList = elementQuestions.getChildren("question");

			// Checks if the question file has reached its maximum capacity
			// This maximum capacity is 50 questions.
			// If this maximum capacity is reached, then returns the -2 code
			if (questionsList.size() >= 200)
				return -2;

			// Calculates the code of the question to add to the file
			int NumberLastQuestion;
			if (questionsList.isEmpty() == false) {
				NumberLastQuestion = Integer.valueOf(
						((Element) questionsList.get(questionsList.size() - 1)).getAttribute("codeQuestion")
								.getValue()).intValue();

				NumberLastQuestion++;
			} else
				NumberLastQuestion = 1;

			// Creates the question to add to the file
			elementQuestion = new Element("question");

			// Sets the attributes of the new question
			question.setCodeQuestion(String.valueOf(NumberLastQuestion).toString());

			elementQuestion = elementQuestion.setAttribute("codeQuestion", question.getCodeQuestion());

			// Sets the data of the question
			elementQuestion = elementQuestion.addContent(new Element("enunciate").setText(question
					.getEnunciate()));

			// Sets the path of the image associated to the question in case it exists
			if (question.getPathImage().trim().equals("") == false)
				question.setPathImage(courseName + imagesItemsPath + questionsFileName + "/" + questionsFileName + "_"
						+ question.getCodeQuestion() + question.getPathImage().toLowerCase());
			else
				question.setPathImage("");

			// Adds the "image" element to its "question" parent
			elementQuestion = elementQuestion.addContent(new Element("image").setText(question.getPathImage()));

			// Creates the answers of the added question
			elementAnswers = new Element("answers");

			int i;
			int numberOfCorrect = 0;
			int numAnswersInt = question.getTextAnswers().size();

			// Loops to set the data of the answers to the question
			for (i = 0; i < numAnswersInt; i++) {
				// Creates the "answer" element.
				elementAnswer = new Element("answer");

				// Creates the children element of the "answer" element
				elementAnswer = elementAnswer.addContent(new Element("textAnswer").setText(question
						.getTextAnswers().get(i).toString()));

				elementAnswer = elementAnswer.addContent(new Element("correct").setText(question
						.getCorrectAnswers().get(i).toString()));

				if (question.getCorrectAnswers().get(i).toString().equals("true"))
					numberOfCorrect++;

				elementAnswer = elementAnswer.addContent(new Element("explanation").setText(question
						.getTextExplanation().get(i).toString()));

				elementAnswer = elementAnswer.setAttribute("codeAnswer", String.valueOf(i + 1));

				elementAnswers = elementAnswers.addContent(elementAnswer);
				elementAnswer = null;
			}

			// Sets the "answers" element attributes.
			elementAnswers = elementAnswers.setAttribute("numberOfCorrect", String.valueOf(numberOfCorrect));
			elementAnswers = elementAnswers.setAttribute("numberOfAnswers", String.valueOf(numAnswersInt));

			// Adds the "answers" element to its "question" parent.
			elementQuestion = elementQuestion.addContent(elementAnswers);

			// Creates the "irtParameters" element.
			Element elementIrtParameters = new Element("irtParameters");

			// Sets the children elements of the "irtParameters" element.
			elementIrtParameters = elementIrtParameters.addContent(new Element("difficulty").setText(String
					.valueOf(question.getDifficulty())));
			elementIrtParameters = elementIrtParameters.addContent(new Element("discrimination").setText(String
					.valueOf(question.getDiscrimination())));
			elementIrtParameters = elementIrtParameters.addContent(new Element("guessing").setText(String
					.valueOf(question.getGuessing())));

			// Adds the "irtParameters" element to its "question" parent.
			elementQuestion = elementQuestion.addContent(elementIrtParameters);

			// Creates the "statisticParameters" element.
			Element elementStatisticParameters = new Element("statisticParameters");

			// Sets the children elements of the "statisticParameters" element.
			elementStatisticParameters = elementStatisticParameters.addContent(new Element("exhibitionRate")
					.setText(String.valueOf(question.getExhibitionRate())));
			elementStatisticParameters = elementStatisticParameters.addContent(new Element("answerTime")
					.setText(String.valueOf(question.getAnswerTime())));
			elementStatisticParameters = elementStatisticParameters.addContent(new Element("successRate")
					.setText(String.valueOf(question.getSuccessRate())));
			elementStatisticParameters = elementStatisticParameters.addContent(new Element("numberOfUses")
					.setText("0"));
			elementStatisticParameters = elementStatisticParameters.addContent(new Element("numberOfSuccesses")
					.setText("0"));

			// Adds the "statisticParameters" element to its "question" parent.
			elementQuestion = elementQuestion.addContent(elementStatisticParameters);

			// Adds the "question" element to its "questions" parent.
			elementQuestions = elementQuestions.addContent(elementQuestion);

			// Saves the file
			File xmlFile = new File(itemsPath + courseName + itemsFilesPath + questionsFileName + testEditorTypeFile);
			FileWriter xmlFileWriter = new FileWriter(xmlFile);
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
			out.output(xmlDocument, xmlFileWriter);
			xmlFileWriter.close();
			fileOfQuestions = null;

			// Updates the values of the list of names of question files in the
			// fileList.xml file
			FileList fileList = new FileList(question.getCourse());
			fileList.addQuestion(question.getQuestionsFileName(), author);
			return 1;

		} catch (Exception e) {
			UtilLog.writeException(e);
			return -1;
		}
	}


	/**
	 * Looks for the question that will be modified, and if it exists, modifies
	 * its values for the ones that takes the question received as parameter.
	 * Stores the updated question in the question file, replacing the old one.
	 * @param question Question to be updated, with the new values.
	 * @return true or false
	 */
	 public boolean modifyQuestion(final Question question) {
		 File fileQuestions; // Question file
		 Element fileOfQuestions; // Root element of the question file
		 Element elementQuestions; // Element that takes all the questions
		 Element elementQuestion; // Element that contains a question of the file
		 Element elementAnswers; // Element that takes all the answers of a question from the file
		 Element elementAnswer; // Element that contains a answer to a question from the file
		 List questionList; // List with all the questions of the file
		 String codeQuestion; // Code of the question that is been updated

		try {
			SAXBuilder builder = new SAXBuilder();
			fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName + testEditorTypeFile);
			Document xmlDocument = builder.build(fileQuestions);

			// Gets the root element of the document
			fileOfQuestions = xmlDocument.getRootElement();

			// Gets the element that takes all the questions
			elementQuestions = fileOfQuestions.getChild("questions");

			// Gets the lists of questions and checks that is not empty
			questionList = elementQuestions.getChildren();
			if (questionList.isEmpty() == false) {
				// Gets the question to modify
				elementQuestion = (Element) XPath.selectSingleNode(xmlDocument,
						"fileOfQuestions/questions/question[@codeQuestion=\"" + question.getCodeQuestion().trim() + "\"]");

				if (elementQuestion != null) {
					// If the question exists, their values will be updated
					codeQuestion = elementQuestion.getAttribute("codeQuestion").getValue();

					// Modifies the question
					elementQuestion.getChild("enunciate").setText(question.getEnunciate());

					if (question.getExistImage().equals("false")) {
						// If the update implies the delete of the questions'
						// associated image, this is deleted from the system
						String imageText = elementQuestion.getChild("image").getText();
						if (imageText != null && !imageText.trim().equals(""))
							deleteImage(imageText);

						elementQuestion.getChild("image").setText("");
					} else
						elementQuestion.getChild("image").setText(
								courseName + imagesItemsPath + questionsFileName + "/" + questionsFileName + "_"
										+ codeQuestion + question.getPathImage().toLowerCase());

					// Created the answers element of the updated question
					elementAnswers = new Element("answers");

					// Sets the new values of the answers
					int numberOfCorrect = 0;
					int numAnswersInt = question.getNumberOfAnswers();
					for (int i = 0; i < numAnswersInt; i++) {
						// Creates the "answer" element
						elementAnswer = new Element("answer");

						// Creates the child nodes of the "answer" element
						elementAnswer = elementAnswer.addContent(new Element("textAnswer").setText(question
								.getTextAnswers().get(i).toString()));
						elementAnswer = elementAnswer.addContent(new Element("correct").setText(question
								.getCorrectAnswers().get(i).toString()));

						if (question.getCorrectAnswers().get(i).toString().equals("true"))
							numberOfCorrect++;

						elementAnswer = elementAnswer.addContent(new Element("explanation").setText(question
								.getTextExplanation().get(i).toString()));

						elementAnswer = elementAnswer.setAttribute("codeAnswer", String.valueOf(i + 1));

						// Adds the "answer" element to their parent "answers"
						elementAnswers = elementAnswers.addContent(elementAnswer);
						elementAnswer = null;
					}

					// Substitutes the old "answers" element for the new one
					Element answersElement = elementQuestion.getChild("answers");
					answersElement.getAttribute("numberOfCorrect").setValue(String.valueOf(numberOfCorrect));
					answersElement.getAttribute("numberOfAnswers").setValue(String.valueOf(numAnswersInt));

					// Add all the answers
					answersElement.getChildren().clear();
					while (elementAnswers.getChildren().size() > 0) {
						Element answer = elementAnswers.getChild("answer");
						answer.detach(); // Deletes the relation with his current parent
						answersElement.addContent(answer);
					}

					// Sets the new IRT parameters of the question
					elementQuestion.getChild("irtParameters").getChild("difficulty").setText(
							String.valueOf(question.getDifficulty()));
					elementQuestion.getChild("irtParameters").getChild("discrimination").setText(
							String.valueOf(question.getDiscrimination()));
					elementQuestion.getChild("irtParameters").getChild("guessing").setText(
							String.valueOf(question.getGuessing()));

					// Sets the new statistical parameters of the question

					// Sets the child nodes of the "irtParameters" element
					elementQuestion.getChild("statisticParameters").getChild("exhibitionRate").setText(
							String.valueOf(question.getExhibitionRate()));
					elementQuestion.getChild("statisticParameters").getChild("answerTime").setText(
							String.valueOf(question.getAnswerTime()));
					elementQuestion.getChild("statisticParameters").getChild("successRate").setText(
							String.valueOf(question.getSuccessRate()));
					elementQuestion.getChild("statisticParameters").getChild("numberOfUses").setText(
							String.valueOf(question.getNumberOfUses()));
					elementQuestion.getChild("statisticParameters").getChild("numberOfSuccesses").setText(
							String.valueOf(question.getNumberOfSuccesses()));

					// Saves the file
					File xmlFile = new File(itemsPath + courseName + itemsFilesPath + questionsFileName + testEditorTypeFile);
					FileWriter xmlFileWriter = new FileWriter(xmlFile);
					XMLOutputter out = new XMLOutputter();
					out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
					out.output(xmlDocument, xmlFileWriter);
					xmlFileWriter.close();
					fileOfQuestions = null;
					return true;

				} else {
					UtilLog.toLog("The question doesn't exists in the file");
					return false;
				}
			} else {
				UtilLog.toLog("There is no questions in the file");
				return false;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	}

	
	/**
	 * Removes a question of the question file. Returns a string explaining the
	 * result of the operation.
	 * @param codeQuestion Code that identifies the question.
	 * @param author Login of the author.
	 * @return A String that explains the result of the operation.
	 */
	public String deleteQuestion(final String codeQuestion, final String author) {
	//{
		// Root element of the question file
		Element fileOfQuestions;
		// Root element of the questions of the file
		Element elementQuestions;
		// Element that contains a question
		Element elementQuestion;
		// List with all the question of the file
		List questionList;
		// Result of the delete of the image
		String resultImageDelete = "";
		// Result of the delete of this question in the test files.
		String resultTestDelete = "";

		try {
			// Loads the file
			File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
					+ testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileQuestions);

			// Obtains the root element of the document
			fileOfQuestions = xmlDocument.getRootElement();

			// Obtains the element that contains all the questions
			elementQuestions = fileOfQuestions.getChild("questions");
			questionList = elementQuestions.getChildren();

			if (questionList.isEmpty() == false) {
				// If there are any question then we look for the
				// one to be removed from the file
				elementQuestion = (Element) XPath.selectSingleNode(xmlDocument, "fileOfQuestions/questions"
						+ "/question[@codeQuestion=\"" + codeQuestion + "\"]");

				String imageText = elementQuestion.getChild("image").getText();
				if (imageText != null && !imageText.trim().equals("")) {
					// If the question to remove has been found and it has
					// a associated image then this image will be also deleted
					if (!deleteImage(imageText.trim()))
						resultImageDelete = "but the file of images associated to this question" + "\n"
								+ "have not been able to be eliminated.";
				}

				// Checks if the question to delete belongs to a test file.
				// In that case calls the method that deletes the reference
				// of this question from the test files that contains it.
				if (elementQuestion.getChild("testFiles") != null) {
					Element elementClassicTest = elementQuestion.getChild("testFiles").getChild("classicTest");
					if (elementClassicTest != null) {
						List classicTestList = elementClassicTest.getChildren();
						Vector classicTestVector = new Vector();
						for (int i = 0; i < classicTestList.size(); i++) {
							Element classicTestFilePath = (Element) classicTestList.get(i);
							classicTestVector.add(classicTestFilePath.getText());
						}
						if (classicTestVector.isEmpty() == false) {
							TestFile testFile = new TestFile(courseName, null);
							resultTestDelete = testFile.deleteQuestionInTestFiles(questionsFileName, codeQuestion,
									classicTestVector, author);
						}
							
					}

					Element elementAdaptiveTest = elementQuestion.getChild("testFiles").getChild("adaptiveTest");
					if (elementAdaptiveTest != null) {
						List adaptiveTestList = elementAdaptiveTest.getChildren();
						Vector adaptiveTestVector = new Vector();
						for (int i = 0; i < adaptiveTestList.size(); i++) {
							Element adaptiveTestFilePath = (Element) adaptiveTestList.get(i);
							adaptiveTestVector.add(adaptiveTestFilePath.getText());
						}
						if (adaptiveTestVector.isEmpty() == false) {
							TestFile testFile = new TestFile(courseName, null);
							testFile.deleteQuestionInTestFiles(questionsFileName, codeQuestion,
									adaptiveTestVector, author);
						}
					}
				}

				// Deletes the question from the question file
				elementQuestions.removeContent(elementQuestion);

				// Saves the question file
				File xmlFile = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
						+ testEditorTypeFile);
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
				xmlFileWriter.close();

				fileOfQuestions = null;

				// Adds the name of the created file to the list of tiles
				// With this updates the values contained in the list of names
				// of question files.
				FileList fileList = new FileList(courseName);
				fileList.subtractQuestion(questionsFileName, author);

				return TestEditor.TEXT_QUESTION_DELETED + "\n" + resultImageDelete + "\n" + resultTestDelete;
			} else
				return "The file doesn't have questions to delete.";

		} catch (Exception e) {
			UtilLog.writeException(e);
			return "Error deleting the question";
		}
	}


	 /**
	  * Imports a question file from another existing format and creates a new
	  * question file of WOW! format, in the current WOW! version
	  * @param conceptName Name of the concept that the new file will be associated to
	  * @param convertFilePath Path to the source file to import
	  * @param theFormat Format of the source file.
	  * @param authorName Name of the author that imports the file
	  * @return true or false
	  * @throws Exception Error making import
	  */	
	 public boolean importQuestionsFile(
			final String conceptName,
			final String convertFilePath,
			final short theFormat,
			final String authorName) throws Exception {
	//{
		boolean result = false;

		try {
			// Builds the path to the source file
			String sourceFile = convertFilePath;

			// Builds the path to the destiny file
			String destinyFile = itemsPath + courseName + itemsFilesPath + questionsFileName + testEditorTypeFile;

			UtilLog.toLog("Importing question file");
			UtilLog.toLog("Source=" + sourceFile);
			UtilLog.toLog("Destiny=" + destinyFile);
			
			switch (theFormat) {
				case TestEditor.GIFT_FORMAT:
					FromGift obj1 = new FromGift();
					result = obj1.convert(sourceFile, destinyFile);
					break;
				case TestEditor.HOTPOTATOESv6_FORMAT:
					FromHotPotatoesv6 obj2 = new FromHotPotatoesv6();
					result = obj2.convert(sourceFile, destinyFile);
					break;
				case TestEditor.MOODLEXML_FORMAT:
					FromMoodleXML obj3 = new FromMoodleXML();
					result = obj3.convert(sourceFile, destinyFile);
					break;
				case TestEditor.QTIv1d2_FORMAT:
					FromQti12 obj4 = new FromQti12();
					result = obj4.convert(sourceFile, destinyFile);
					break;
				case TestEditor.QTIv2d0_FORMAT:
					FromQti20 obj5 = new FromQti20();
					result = obj5.convert(sourceFile, destinyFile);
					break;
				case TestEditor.SIETTE_FORMAT:
					FromSiette obj6 = new FromSiette();
					result = obj6.convert(sourceFile, destinyFile);
					break;
				case TestEditor.WEBCT_FORMAT:
					FromWebCT obj7 = new FromWebCT();
					result = obj7.convert(sourceFile, destinyFile);
					break;
			}

			if (result) {
				// I have to obtain the number of questions and the numberOfAnswersForFile param
				String concept = courseName + "." + conceptName;
				String fileName = questionsFileName + testEditorTypeFile;
				String name = questionsFileName;
				String relativePath = courseName + itemsFilesPath + fileName;

				SAXBuilder builder = new SAXBuilder();
				File newFile = new File(itemsPath + relativePath);
				Document xmlDocument = builder.build(newFile);

				Element fileOfQuestions = xmlDocument.getRootElement();
				
				Attribute at = fileOfQuestions.getAttribute("fileName");
				if (at != null) {
					at.setValue(fileName);
				} else {
					fileOfQuestions.setAttribute("fileName", fileName);
				}

				at = fileOfQuestions.getAttribute("name");
				if (at != null) {
					at.setValue(name);
				} else {
					fileOfQuestions.setAttribute("name", name);
				}
				
				at = fileOfQuestions.getAttribute("course");
				if (at != null) {
					at.setValue(courseName);
				} else {
					fileOfQuestions.setAttribute("course", courseName);
				}

				at = fileOfQuestions.getAttribute("path");
				if (at != null) {
					at.setValue(relativePath);
				} else {
					fileOfQuestions.setAttribute("path", relativePath);
				}

				Element elementConcept = fileOfQuestions.getChild("concept");
				elementConcept.getAttribute("value").setValue(concept);

				int numberOfQuestions = 0;
				Element questions = fileOfQuestions.getChild("questions");
				List question = questions.getChildren();
				if (question != null) {
					numberOfQuestions = question.size();
				}
				int numberOfAnswersForFile = 0;
				at = fileOfQuestions.getAttribute("numberOfAnswersForFile");
				if (at != null)  {
					numberOfAnswersForFile = Integer.parseInt(at.getValue());
				}

				if (numberOfAnswersForFile == 0) {
					numberOfAnswersForFile = numberOfQuestions; // By default
				}

				// Saves the file
				File xmlFile = new File(itemsPath + relativePath);
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
				
				FileList fileList = new FileList(courseName);
				boolean create = fileList.addQuestionsFile(questionsFileName, concept,
									numberOfQuestions, numberOfAnswersForFile, authorName);
				
				return create;
			} 
		} catch (Exception e) {
			UtilLog.writeException(e);
			result = false;
		}
		
		return result;
	}


	 /**
	  * Imports a question file of an older WOW! version and creates a new
	  * question file of the current WOW! version associated to a concept and a course.
	  * @param conceptName Name of the concept associated to the new question file
	  * @param testOldPath Path to the question file to import
	  * @param authorName Login of the author that imports the file
	  * @return true or false
	  */
	 public boolean importWOWQuestionsFile(
			 final String conceptName,
			 final String testOldPath,
			 final String authorName) {
	//{
		Element fileOfQuestions; // Root element of the new question file
		Element test; // Root element of the document from the source file
		Element elementQuestion; // Element that stores a question of the source file
		Element elementQuestionNew; // Stores a question of the destiny file
		List questionsList; // List with all the questions of the source file
		String numberOfAnswersForFile = "0"; // Stores the medium of number of answers
		// that each question of the source file has.
		String numberOfCorrect; // Stores the number of correct responses of as question.
		String enunciate = ""; // Stores the enunciate of a question
		Vector textAnswers; // Stores all the texts of the answers to a question
		Vector correctAnswers; // Stores the boolean value of the answers (a answer is true or false)
		Vector textExplanation; // Stores all the texts of the explanations of the answers

		// Creates the destiny XML Document
		Document xmlDocumentNew = new Document();

		// Creates the DocType of the destiny file
		DocType docType = new DocType("fileOfQuestions", "../../fileOfQuestions.dtd");
		xmlDocumentNew = xmlDocumentNew.setDocType(docType);

		try {
			SAXBuilder builder = new SAXBuilder(true);

			// Reads the source file
			File fileQuestions = new File(WOWStatic.config.Get("WOWROOT") + testOldPath + testEditorTypeFile);
			Document xmlDocument = builder.build(fileQuestions);

			// Creates the root element of the destiny document and sets its attributes
			fileOfQuestions = new Element("fileOfQuestions");
			fileOfQuestions = fileOfQuestions
					.setAttribute("fileName", questionsFileName + testEditorTypeFile);
			fileOfQuestions = fileOfQuestions.setAttribute("name", questionsFileName);
			fileOfQuestions = fileOfQuestions.setAttribute("course", courseName);
			fileOfQuestions = fileOfQuestions.setAttribute("path", courseName + itemsFilesPath
					+ questionsFileName + testEditorTypeFile);

			xmlDocumentNew = xmlDocumentNew.setRootElement(fileOfQuestions);

			Element concept = new Element("concept");
			concept = concept.setAttribute("value", courseName + "." + conceptName);

			Element elementQuestionsNew = new Element("questions");

			// Gets the root element of the source document
			test = xmlDocument.getRootElement();
			questionsList = test.getChildren();

			if (questionsList.isEmpty() == false) {
				int numberOfAnswersForFileInt = 0;

				// Loops to extract the data of the questions of the document and
				// writes them to the destiny document
				for (int i = 0; i < questionsList.size(); i++) {
					// Gets the data of the selected question
					elementQuestion = (Element) questionsList.get(i);

					numberOfCorrect = elementQuestion.getAttribute("right").getValue();
					enunciate = elementQuestion.getTextNormalize();

					// Gets the data of the answers
					List answersList = elementQuestion.getChildren();

					// Calculates the medium of answers of the questions from source file
					numberOfAnswersForFileInt = numberOfAnswersForFileInt + answersList.size();

					if (i == questionsList.size() - 1) {
						numberOfAnswersForFileInt = numberOfAnswersForFileInt / questionsList.size();
						numberOfAnswersForFile = String.valueOf(numberOfAnswersForFileInt);
						fileOfQuestions = fileOfQuestions.setAttribute("numberOfAnswersForFile",
								numberOfAnswersForFile);
					}

					textAnswers = new Vector();
					correctAnswers = new Vector();
					textExplanation = new Vector();

					// Loops to create the answers of the destiny document
					for (int j = 0; j < answersList.size(); j++) {
						Element elementAnswer = (Element) answersList.get(j);

						correctAnswers.add(elementAnswer.getAttribute("correct").getValue());
						textAnswers.add(elementAnswer.getTextNormalize());

						Element elementExplanation = elementAnswer.getChild("explain");
						if (elementExplanation != null)
							textExplanation.add(elementExplanation.getTextNormalize());
						else
							textExplanation.add("");
					}

					// Creates the "question" element, that stores the data of each question
					// of the destiny document
					elementQuestionNew = new Element("question");

					// Sets the attributes of the "question" element
					elementQuestionNew = elementQuestionNew.setAttribute("codeQuestion", String.valueOf(i + 1));

					// Creates the child nodes of the question element and sets their values.
					Element elementTextQuestion = new Element("enunciate");
					elementTextQuestion = elementTextQuestion.setText(enunciate);
					Element elementImage = new Element("image");
					elementImage = elementImage.setText("");

					// Creates the "answers" element that contains all the answers of the new questions
					Element elementAnswers = new Element("answers");
					elementAnswers = elementAnswers.setAttribute("numberOfCorrect", numberOfCorrect);
					elementAnswers = elementAnswers.setAttribute("numberOfAnswers", String.valueOf(answersList
							.size()));

					// Loops to create the child nodes of the "answers" element and sets their values.
					// That values will be the data of the answers extracted from the source file
					for (int j = 0; j < answersList.size(); j++) {
						Element elementAnswer = new Element("answer");
						elementAnswer = elementAnswer.setAttribute("codeAnswer", String.valueOf(j + 1));

						elementAnswer = elementAnswer.addContent(new Element("textAnswer").setText(textAnswers.get(
								j).toString()));
						elementAnswer = elementAnswer.addContent(new Element("correct").setText(correctAnswers.get(
								j).toString()));
						elementAnswer = elementAnswer.addContent(new Element("explanation").setText(textExplanation
								.get(j).toString()));

						elementAnswers = elementAnswers.addContent(elementAnswer);
					}

					// Creates the "irtParameters" element and their child nodes
					Element elementIrtParameters = new Element("irtParameters");
					Element elementDifficulty = new Element("difficulty");
					elementDifficulty = elementDifficulty.setText("0.0");

					Element elementDiscrimination = new Element("discrimination");
					elementDiscrimination = elementDiscrimination.setText("1.0");

					Element elementGuessing = new Element("guessing");
					double guessing = 1.0 / answersList.size();
					elementGuessing = elementGuessing.setText(String.valueOf(guessing));

					// Sets the child nodes of the "irtParameters" element
					elementIrtParameters = elementIrtParameters.addContent(elementDifficulty);
					elementIrtParameters = elementIrtParameters.addContent(elementDiscrimination);
					elementIrtParameters = elementIrtParameters.addContent(elementGuessing);

					// Creates the "statisticParameters" element and their child nodes
					Element elementStatisticParameters = new Element("statisticParameters");

					Element elementExhibitionRate = new Element("exhibitionRate");
					elementExhibitionRate = elementExhibitionRate.setText("0.0");

					Element elementAnswerTime = new Element("answerTime");
					elementAnswerTime = elementAnswerTime.setText("0");

					Element elementSuccessRate = new Element("successRate");
					elementSuccessRate = elementSuccessRate.setText("0.0");

					Element elementNumberOfUses = new Element("numberOfUses");
					elementNumberOfUses = elementNumberOfUses.setText("0");

					Element elementNumberOfSuccesses = new Element("numberOfSuccesses");
					elementNumberOfSuccesses = elementNumberOfSuccesses.setText("0");

					// Sets the child nodes to the "irtParameters" element
					elementStatisticParameters = elementStatisticParameters.addContent(elementExhibitionRate);
					elementStatisticParameters = elementStatisticParameters.addContent(elementAnswerTime);
					elementStatisticParameters = elementStatisticParameters.addContent(elementSuccessRate);
					elementStatisticParameters = elementStatisticParameters.addContent(elementNumberOfUses);
					elementStatisticParameters = elementStatisticParameters.addContent(elementNumberOfSuccesses);

					// Sets all the child nodes(that takes part in the new question) to the "question" element
					elementQuestionNew = elementQuestionNew.addContent(elementTextQuestion);
					elementQuestionNew = elementQuestionNew.addContent(elementImage);
					elementQuestionNew = elementQuestionNew.addContent(elementAnswers);
					elementQuestionNew = elementQuestionNew.addContent(elementIrtParameters);
					elementQuestionNew = elementQuestionNew.addContent(elementStatisticParameters);

					// Sets the new created "question" element to his parent "questions"
					elementQuestionsNew = elementQuestionsNew.addContent(elementQuestionNew);
				}

				// Sets the "questions" element with all the questions to the root element
				// of the destiny document
				fileOfQuestions = fileOfQuestions.addContent(concept);
				fileOfQuestions = fileOfQuestions.addContent(elementQuestionsNew);

				// Saves the new question file
				// Creates the folder in case it doesn't exist
				boolean create = createDirectory();
				if (create == false) {
					return false;
				}

				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				File xmlFileNew = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
						+ testEditorTypeFile);
				FileWriter xmlFileWriter = new FileWriter(xmlFileNew);
				out.output(xmlDocumentNew, xmlFileWriter);

				// Adds the new created file name to the file list of the author
				FileList fileList = new FileList(courseName);
				create = fileList.addQuestionsFile(questionsFileName, courseName + "."
						+ conceptName, questionsList.size(), numberOfAnswersForFileInt, authorName);

				return create; // Returns false if any error occurs

			} else {
				// There are no questions in the source file
				return false;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	} // End of importWOWQuestionsFile method


	 /**
	  * Exports a question file of the current version of WOW! to another question file
	  * in the specified format. Returns the relative path to the new created file.
	  * @param newFileName Path to the new question file that will be created in the server
	  * @param theFormat Format of questions that the destiny file will have.
	  * @return relative path to the new created questions file.
	  */
	 public String exportQuestionFile(final String newFileName, final short theFormat) {
	//{
		try {
			// Saves the output file with a special extension
			String fileType = "";
			switch (theFormat) {
				case TestEditor.GIFT_FORMAT:
				case TestEditor.WEBCT_FORMAT:
					fileType = ".txt";
					break;
				case TestEditor.HOTPOTATOESv6_FORMAT:
					fileType = ".jqz";
					break;
				case TestEditor.MOODLEXML_FORMAT:
				case TestEditor.QTIv1d2_FORMAT:
				case TestEditor.QTIv2d0_FORMAT:
				case TestEditor.SIETTE_FORMAT:
				default:
					fileType = testEditorTypeFile;
					break;
			}

			// Builds the path to the source file
			String sourceFile = itemsPath + courseName + itemsFilesPath + questionsFileName + testEditorTypeFile;

			// Gets the route to the wowRoot
			String wowRoot = WOWStatic.config.Get("WOWROOT");

			// Creates a folder into the temp folder to store the new created question file
			String tempFolder = TempFile.createTempFolder();

			// Builds the path to the destination file
			String destinyFile = tempFolder + "/" + newFileName + fileType;

			// Creates the images folder
			File images = new File(tempFolder + "/images");
			images.mkdir();

			UtilLog.toLog("Exporting question file");
			UtilLog.toLog("Source=" + sourceFile);
			UtilLog.toLog("Destiny=" + destinyFile);

			// Creates the class that makes the export and calls it
			boolean result = false;
			switch (theFormat) {
				case TestEditor.GIFT_FORMAT:
					ToGift obj1 = new ToGift();
					result = obj1.convert(sourceFile, destinyFile);
					break;
				case TestEditor.HOTPOTATOESv6_FORMAT:
					ToHotPotatoesv6 obj2 = new ToHotPotatoesv6();
					result = obj2.convert(sourceFile, destinyFile);
					break;
				case TestEditor.MOODLEXML_FORMAT:
					ToMoodleXML obj3 = new ToMoodleXML();
					result = obj3.convert(sourceFile, destinyFile);
					break;
				case TestEditor.QTIv1d2_FORMAT:
					ToQti12 obj4 = new ToQti12();
					result = obj4.convert(sourceFile, destinyFile);
					break;
				case TestEditor.QTIv2d0_FORMAT:
					ToQti20 obj5 = new ToQti20();
					result = obj5.convert(sourceFile, destinyFile);
					break;
				case TestEditor.SIETTE_FORMAT:
					ToSiette obj6 = new ToSiette();
					result = obj6.convert(sourceFile, destinyFile);
					break;
				case TestEditor.WEBCT_FORMAT:
					ToWebCT obj7 = new ToWebCT();
					result = obj7.convert(sourceFile, destinyFile);
					break;
			}

			if (result) {
				// compress the entire output folder
				String theZipName = TempFile.getNewId();
				File tempZipFile = null;
				File downloadFolder = new File(tempFolder);
				// Creates the ZIP in the parent folder
				tempZipFile = File.createTempFile(theZipName, ".zip", downloadFolder.getParentFile());
				theZipName = tempZipFile.getAbsolutePath();
				Runtime r = Runtime.getRuntime();
				String command = "jar cfM  \"" + theZipName + "\" -C \"" + downloadFolder + "\" .";
				UtilLog.toLog(command);
				Process p = r.exec(command);
				// Waits for the process to stop
				int processResult = p.waitFor();
				// Deletes the temporal folder
				TempFile.deleteFolder(downloadFolder);
				// Takes a look at the result of the process
				if (processResult != 0) {
					return "";
				}

				// Returns the relative path to the file
				return theZipName.substring(wowRoot.length());

			} else {
				// The file has not been correctly exported
				return "";
			}

		} catch (Exception e) {
			UtilLog.writeException(e);
			return "";
		}
	} // End of the exportQuestionFile method


	 /**
	  * Exports a question file of the current version of WOW! to another question file
	  * of an older version of WOW!
	  * @param newFileName Path to the older question file that will be created in the server
	  * @return true or false
	  */
	public boolean exportWOWQuestionFile(final String newFileName) {
		Element fileOfQuestions; // Root element of the source file
		Element test; // Root element of the destiny file
		Element elementQuestions; // Element that stores the questions of the source file
		Element elementQuestion; // Stores a question of the source file
		Element elementQuestionNew; // Stores a question of the destiny file
		List questionsList; // Stores all the questions of the source file

		String enunciate = ""; // Enunciate of the question
		String [] textAnswers; // Stores the texts of the answers of the question
		String [] correctAnswers; // Stores the boolean value of the answers
		String [] textExplanation; // Stores the texts of the explanations of the answers

		try {
			SAXBuilder builder = new SAXBuilder(true);
			Document xmlDocument = new Document();

			// Reads the source file
			File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
						+ testEditorTypeFile);
			xmlDocument = builder.build(fileQuestions);

			fileOfQuestions = xmlDocument.getRootElement();
			elementQuestions = fileOfQuestions.getChild("questions");
			questionsList = elementQuestions.getChildren();

			Document xmlDocumentNew = new Document();

			test = new Element("test");
			test = test.setAttribute("title", fileOfQuestions.getAttribute("name").getValue());
			test = test.setAttribute("concept", fileOfQuestions.getAttribute("name").getValue());
			test = test.setAttribute("total", String.valueOf(questionsList.size()));

			test = test.setAttribute("ask", fileOfQuestions.getAttribute("numberOfAnswersForFile").getValue());
			test = test.setAttribute("verbose", "true");

			if (questionsList.isEmpty() == false) {
				for (int i = 0; i < questionsList.size(); i++) {
					// Gets the data of the question to be exported
					elementQuestion = (Element) questionsList.get(i);

					enunciate = elementQuestion.getChild("enunciate").getText();
					
					// Gets the data of the answers of the question to be exported
					Element elementAnswers = elementQuestion.getChild("answers");

					List answersList = elementAnswers.getChildren();
					textAnswers = new String[answersList.size()];
					correctAnswers = new String[answersList.size()];
					textExplanation = new String[answersList.size()];
					Element elementAnswer = null;

					for (int j = 0; j < answersList.size(); j++) {
						elementAnswer = (Element) answersList.get(j);
						textAnswers[j] = elementAnswer.getChild("textAnswer").getText();
						correctAnswers[j] = elementAnswer.getChild("correct").getText();
						textExplanation[j] = elementAnswer.getChild("explanation").getText();
					}

					// Creates the new exported question and sets their values
					elementQuestionNew = new Element("question");
					elementQuestionNew = elementQuestionNew.setAttribute("answers", String.valueOf(answersList
							.size()));

					elementQuestionNew = elementQuestionNew.setAttribute("right", elementAnswers.getAttribute(
							"numberOfCorrect").getValue());

					elementQuestionNew = elementQuestionNew.setText(enunciate);

					// Creates the answers for the new exported question
					for (int j = 0; j < answersList.size(); j++) {
						Element elementAnswerNew = new Element("answer");
						elementAnswerNew = elementAnswerNew.setAttribute("correct", correctAnswers[j]);
						elementAnswerNew = elementAnswerNew.setText(textAnswers[j]);

						Element elementExplanationNew = new Element("explain");
						elementExplanationNew = elementExplanationNew.setText(textExplanation[j]);

						elementAnswerNew = elementAnswerNew.addContent(elementExplanationNew);

						elementQuestionNew = elementQuestionNew.addContent(elementAnswerNew);
					}
					test = test.addContent(elementQuestionNew);
				}
				xmlDocumentNew = xmlDocumentNew.setRootElement(test);

				// Creates the temporal file and saves the document with the exported file
				boolean createDirectory = createDirectoryOld(newFileName);
				if (createDirectory == false)
					return false;

				FileWriter xmlFileWriter;
				XMLOutputter out = new XMLOutputter();
				File xmlFileNew = new File(WOWStatic.config.Get("WOWROOT") + newFileName + "Temp"
							+ testEditorTypeFile);
				xmlFileWriter = new FileWriter(xmlFileNew);
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocumentNew, xmlFileWriter);

				fileOfQuestions = null;

				// Creates the final file as result
				return createQuestionsFileOld(newFileName);

			} else {
				// The source file is empty
				return false;
			}
		} catch (Exception e) {
			// An error has occurred
			UtilLog.writeException(e);
			return false;
		}
	} // End of exportWOWQuestionFile method


	/**
	 * Deletes a image associated to a question file.
	 * Returns true if the image has been correctly deleted, false otherwise
	 * @param pathImage Relative path to the image
	 * @return true or false
	 */
	public static boolean deleteImage(final String pathImage) {
		File fileImage = new File(itemsPath + pathImage);
		boolean deleteImage = fileImage.delete();
		if (deleteImage == false) {
			fileImage.deleteOnExit();
			return true;
		} else {
			return deleteImage;
		}
	} // End of deleteImage method


	 /**
	  * Deletes all the images associated to the question file that will be created
	  * and will be rewrited. Returns true if the images has been deleted, false otherwise
	  * @return true or false
	  */
	 public boolean deleteImageDirectory() {
		 String [] fileList; // Stores the list of image files associated to the question file

		 // Gets the folder
		 File folderImages = new File(itemsPath + courseName + imagesItemsPath + questionsFileName);

		 if (folderImages.isDirectory() && folderImages.exists()) {
			// Gets the list of files stored in that folder
			fileList = folderImages.list();
			for (int i = 0; i < fileList.length; i++) {
				// Deletes the selected image file
				File imageFile = new File(itemsPath + courseName + imagesItemsPath + questionsFileName + "/"
						+ fileList[i]);
				if (imageFile.exists() == true) {
					imageFile.delete();
				}

				imageFile = null;
			}
			// Deletes the folder at the end
			return folderImages.delete();
		} else {
			// The folder doesn't need to be deleted - It doesn't exist
			return true;
		}
	} // End of deleteImageDirectory method


	 /**
	  * Deletes a question file of a course. Returns true if the file has been
	  * correctly deleted, false otherwise
	  * @param authorName Login of the author
	  * @return true or false
	  */
	 public boolean deleteQuestionsFile(final String authorName) {
	//{
		Element rootElement; // Root element of the XML document
		Element elementQuestions; // Element that contains all the questions of the file
		List questionsList; // List with all the questions of the file

		try {
			// Reads the question file
			SAXBuilder builder = new SAXBuilder();

			File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
						+ testEditorTypeFile);
			Document xmlDocument = builder.build(fileQuestions);

			rootElement = xmlDocument.getRootElement();
			elementQuestions = rootElement.getChild("questions");

			questionsList = elementQuestions.getChildren();

			if (questionsList.isEmpty() == false) {
				List testPathList = XPath.selectNodes(xmlDocument,
						"fileOfQuestions/questions//question/testFiles/classicTest" + "//classicTestFilePath");

				// Loops to delete the references to the questions of this question file
				// from all the classic test files.
				if (testPathList.isEmpty() == false) {
					// Creates a list without repeated elements
					Vector classicTestVector = new Vector();
					for (int i = 0; i < testPathList.size(); i++) {
						if (classicTestVector.contains(((Element) testPathList.get(i)).getText()) == false)
							classicTestVector.add(((Element) testPathList.get(i)).getText());
					}

					TestFile testFile = new TestFile(courseName, null);
					testFile.deleteQuestionFileInTestFiles(questionsFileName, classicTestVector, authorName);
				}

				testPathList = XPath.selectNodes(xmlDocument,
						"fileOfQuestions/questions/question/testFiles/adaptiveTest//adaptiveTestFilePath");

				// Loops to delete the references to the questions of this question file
				// from all the adaptive test files.
				if (testPathList.isEmpty() == false) {
					// Creates a list without repeated elements
					Vector adaptiveTestVector = new Vector();
					for (int i = 0; i < testPathList.size(); i++) {
						if (adaptiveTestVector.contains(((Element) testPathList.get(i)).getText()) == false)
							adaptiveTestVector.add(((Element) testPathList.get(i)).getText());
					}

					TestFile testFile = new TestFile(courseName, null);
					testFile.deleteQuestionFileInTestFiles(questionsFileName, adaptiveTestVector, authorName);
				}
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		// Free memory
		rootElement = null;
		elementQuestions = null;
		questionsList = null;

		// Deletes the file
		File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName + testEditorTypeFile);
		boolean deleteFile = fileQuestions.delete();

		// Deletes the images folder
		if (deleteFile == true) {
			deleteImageDirectory();

			// Deletes the name of the file from the file list of the author
			FileList fileList = new FileList(courseName);
			fileList.deleteQuestionsFile(questionsFileName, authorName);
		}

		return deleteFile;

	} // End of deleteQuestionsFile method


	 /**
	  * Obtains the data of the searched questions in the question file and stores
	  * them in a Vector of Question objects. Each Question object will have the data
	  * of a question and the vector will have each question objects as questions
	  * have been found on the file.
	  * @param searchedData Question with the data to search
	  * @return A list of searched questions in a Vector object or null
	  */
	 public Vector findQuestions(final Question searchedData) {
	//{
		 Question questionReturn = null;
		 Element fileOfQuestions; // Root element of the XML document
		 Vector questionVector; // Vector with the result

		 try {
			 // Loads the question file
			 SAXBuilder builder = new SAXBuilder();
			 File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
						+ testEditorTypeFile);
			 Document xmlDocument = builder.build(fileQuestions);

			 fileOfQuestions = xmlDocument.getRootElement();
			 Element elementConcept = fileOfQuestions.getChild("concept");
			 String concept = elementConcept.getAttribute("value").getValue().trim();

			 Element elementQuestions = fileOfQuestions.getChild("questions");
			 List questionsListAll = elementQuestions.getChildren();

			 if (questionsListAll.isEmpty() == false) {
				 List questionsList = null;
				 // Sets the new values to make the search

				 // Sets the code of the searched question
				 if (searchedData.getCodeQuestion().equals("NoCode")) {
					 searchedData.setCodeQuestion("");
				 } else {
					 searchedData.setCodeQuestion("=\"" + searchedData.getCodeQuestion() + "\"");
				 } 

				 // Builds the XPath string
				 StringBuffer searchString = new StringBuffer();
				 searchString.append("fileOfQuestions/questions").append("//question[@codeQuestion");
				 searchString.append(searchedData.getCodeQuestion()).append("]");

				if (searchedData.getClassicTest().equals("true")) {
					searchString.append("/testFiles/classicTest/../..");
				} else if (searchedData.getClassicTest().equals("false")) {
					searchString.append("[not(child::/testFiles/classicTest)]");
				}

				if (searchedData.getAdaptiveTest().equals("true")) {
					searchString = searchString.append("/testFiles/adaptiveTest/../..");
				} else if (searchedData.getAdaptiveTest().equals("false")) {
					searchString = searchString.append("[not(child::/testFiles/adaptiveTest)]");
				}

				searchString.append("/enunciate[contains(text(),\"");
				searchString.append(searchedData.getEnunciate() + "\"]/..");

				if (searchedData.getExistImage().equals("false")) {
					searchString.append("/image[string-length(text()) = 0]/..");
				} else if (searchedData.getExistImage().equals("true")) {
					searchString.append("/image[string-length(text()) > 0]/..");
				}

				searchString.append("/answers[number(@numberOfAnswers) <= number(");
				searchString.append(searchedData.getNumberOfAnswersMax() + ")]/..");
				searchString.append("/answers[number(@numberOfAnswers) >= number(" + searchedData.getNumberOfAnswersMin());
				searchString.append(")]/..");

				searchString.append("/irtParameters").append("/difficulty[number(text()) <= number(");
				searchString.append(searchedData.getDifficultyMax() + ")]/.." + "/difficulty[number(text()) >= number(");
				searchString.append(searchedData.getDifficultyMin() + ")]/.." + "/discrimination[number(text()) <= number(");
				searchString.append(searchedData.getDiscriminationMax() + ")]/.." + "/discrimination[number(text()) >= number(");
				searchString.append(searchedData.getDiscriminationMin() + ")]/.." + "/guessing[number(text()) <= number(");
				searchString.append(searchedData.getGuessingMax() + ")]/.." + "/guessing[number(text()) >= number(");
				searchString.append(searchedData.getGuessingMin() + ")]/../..");

				searchString.append("/statisticParameters");
				searchString.append("/exhibitionRate[number(text()) <= number(" + searchedData.getExhibitionRateMax() + ")]/..");
				searchString.append("/exhibitionRate[number(text()) >= number(" + searchedData.getExhibitionRateMin() + ")]/..");
				searchString.append("/answerTime[number(text()) <= number(" + searchedData.getAnswerTimeMax() + ")]/..");
				searchString.append("/answerTime[number(text()) >= number(" + searchedData.getAnswerTimeMin() + ")]/..");
				searchString.append("/successRate[number(text()) <= number(" + searchedData.getSuccessRateMax() + ")]/..");
				searchString.append("/successRate[number(text()) >= number(" + searchedData.getSuccessRateMin() + ")]/../..");

				// Makes the query
				questionsList = XPath.selectNodes(xmlDocument, searchString.toString());

				if (questionsList == null || questionsList.size() == 0) {
					return null;
				}

				// Creates the result vector
				questionVector = new Vector();

				// Loops to obtains the data of all the questions found 
				for (int i = 0; i < questionsList.size(); i++) {
					Element elementQuestion = (Element) questionsList.get(i);
					int numberQuestionOrder = questionsListAll.indexOf(elementQuestion);

					// Gets the data of a question
					String codeQuestion = elementQuestion.getAttribute("codeQuestion").getValue();
					String enunciate = elementQuestion.getChild("enunciate").getText();
					String pathImage = elementQuestion.getChild("image").getText();

					Element elementAnswers = elementQuestion.getChild("answers");

					// Gets the data of the answers
					List answersList = elementAnswers.getChildren();
					String numberOfAnswers = String.valueOf(answersList.size());

					Vector textAnswers = new Vector(answersList.size());
					Vector correctAnswers = new Vector(answersList.size());
					Vector textExplanation = new Vector(answersList.size());
					Vector codeAnswers = new Vector(answersList.size());

					Element elementAnswer = null;

					Vector numberOfCorrect = new Vector();

					// Sets the answers data to the intermedium variables
					for (int j = 0; j < answersList.size(); j++) {
						elementAnswer = (Element) answersList.get(j);
						textAnswers.add(elementAnswer.getChild("textAnswer").getText());
						correctAnswers.add(elementAnswer.getChild("correct").getText());
						textExplanation.add(elementAnswer.getChild("explanation").getText());
						codeAnswers.add(elementAnswer.getAttribute("codeAnswer").getValue());

						if (elementAnswer.getChild("correct").getText().equals("true"))
							numberOfCorrect.add(elementAnswer.getAttribute("codeAnswer").getValue());
					}

					String difficulty = elementQuestion.getChild("irtParameters").getChild("difficulty").getText();
					String discrimination = elementQuestion.getChild("irtParameters").getChild("discrimination")
							.getText();
					String guessing = elementQuestion.getChild("irtParameters").getChild("guessing").getText();

					String exhibitionRate = elementQuestion.getChild("statisticParameters").getChild(
							"exhibitionRate").getText();
					String answerTime = elementQuestion.getChild("statisticParameters").getChild("answerTime")
							.getText();
					String successRate = elementQuestion.getChild("statisticParameters").getChild("successRate")
							.getText();
					String numberOfUses = elementQuestion.getChild("statisticParameters").getChild("numberOfUses")
							.getText();
					String numberOfSuccesses = elementQuestion.getChild("statisticParameters").getChild(
							"numberOfSuccesses").getText();

					// Loops to obtain the tests that this question take part
					Vector classicTestVector = null;
					Vector adaptiveTestVector = null;
					if (elementQuestion.getChild("testFiles") != null) {
						Element elementTestFiles = elementQuestion.getChild("testFiles");

						// Classic tests
						if (elementTestFiles.getChild("classicTest") != null) {
							List testFilePathList = elementTestFiles.getChild("classicTest").getChildren();
							classicTestVector = new Vector();
							for (int j = 0; j < testFilePathList.size(); j++) {
								classicTestVector.add(((Element) testFilePathList.get(j)).getText());
							}
						}

						// Adaptive tests
						if (elementTestFiles.getChild("adaptiveTest") != null) {
							List testFilePathList = elementTestFiles.getChild("adaptiveTest").getChildren();
							adaptiveTestVector = new Vector();
							for (int j = 0; j < testFilePathList.size(); j++) {
								adaptiveTestVector.add(((Element) testFilePathList.get(j)).getText());
							}
						}
					}

					// Stores the data
					questionReturn = new Question();

					questionReturn.setCourse(courseName);
					questionReturn.setConcept(concept);
					questionReturn.setQuestionsFileName(questionsFileName);
					questionReturn.setCodeQuestion(codeQuestion);
					questionReturn.setEnunciate(enunciate);
					questionReturn.setPathImage(pathImage);
					questionReturn.setExistImage(String.valueOf(!pathImage.equals("")));
					questionReturn.setDifficulty(Double.valueOf(difficulty).doubleValue());
					questionReturn.setDiscrimination(Double.valueOf(discrimination).doubleValue());
					questionReturn.setGuessing(Double.valueOf(guessing).doubleValue());
					questionReturn.setExhibitionRate(Double.valueOf(exhibitionRate).doubleValue());
					questionReturn.setAnswerTime(Integer.valueOf(answerTime).intValue());
					questionReturn.setSuccessRate(Double.valueOf(successRate).doubleValue());
					questionReturn.setNumberOfUses(Integer.valueOf(numberOfUses).intValue());
					questionReturn.setNumberOfSuccesses(Integer.valueOf(numberOfSuccesses).intValue());
					questionReturn.setTextAnswers(textAnswers);
					questionReturn.setCorrectAnswers(correctAnswers);
					questionReturn.setTextExplanation(textExplanation);
					questionReturn.setCodeAnswers(codeAnswers);

					if (classicTestVector != null && classicTestVector.isEmpty() == false) {
						searchedData.setClassicTest("true");
					} else {
						searchedData.setClassicTest("false");
					}

					if (adaptiveTestVector != null && adaptiveTestVector.isEmpty() == false) {
						searchedData.setAdaptiveTest("true");
					} else {
						searchedData.setAdaptiveTest("false");
					}

					questionReturn.setClassicTestVector(classicTestVector);
					questionReturn.setAdaptiveTestVector(adaptiveTestVector);
					questionReturn.setTotalQuestionsInFile(questionsList.size());
					questionReturn.setFirstLastQuestion("");
					questionReturn.setNumberOfAnswers(Integer.valueOf(numberOfAnswers).intValue());
					questionReturn.setNumberQuestionOrder(numberQuestionOrder);
					questionReturn.setAnswersCorrect(numberOfCorrect);

					questionVector.add(questionReturn);
				}
			} else {
				// The file has no questions
				return null;
			}

		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}

		return questionVector;

	} // End of findQuestions method


	 /**
	  * Obtains the data of the searched question in the question file, and
	  * returns the Question object found.
	  * @param numQuestion Order of the searched question in the file
	  * @return A Question object or null
	  */
	 public Question getQuestionByOrder(final int numQuestion) {
	//{
		Element fileOfQuestions; // Root element of the XML document
		int numberQuestion = numQuestion;

		try {
			SAXBuilder builder = new SAXBuilder();
			File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
					+ testEditorTypeFile);
			Document xmlDocument = builder.build(fileQuestions);

			fileOfQuestions = xmlDocument.getRootElement();
			Element elementConcept = fileOfQuestions.getChild("concept");
			String concept = elementConcept.getAttribute("value").getValue();

			Element elementQuestions = fileOfQuestions.getChild("questions");
			List questionsList = elementQuestions.getChildren();

			if (questionsList.isEmpty() == false) {
				// Gets the position of the question in the file
				// (is the first, the last, etc??)
				String firstLastQuestion = "";
				int lastIndex = questionsList.size() - 1;
				if (questionsList.size() == 1) {
					firstLastQuestion = "one question";
				} else {
					if (numberQuestion == lastIndex) {
						firstLastQuestion = "last";
					} else {
						if (numberQuestion == 0) {
							firstLastQuestion = "first";
						} else {
							firstLastQuestion = "NoLastNoFirst";
						}
					}
				}

				// Gets the question
				Element elementQuestion = (Element) questionsList.get(numberQuestion);
				int numberQuestionOrder = numberQuestion;

				// Gets the data of the question
				String codeQuestion = elementQuestion.getAttribute("codeQuestion").getValue();
				String enunciate = elementQuestion.getChild("enunciate").getText();
				String pathImage = elementQuestion.getChild("image").getText();

				Element elementAnswers = elementQuestion.getChild("answers");

				// Gets the data of the answers of the question
				List answersList = elementAnswers.getChildren();
				String numberOfAnswers = String.valueOf(answersList.size());

				Vector textAnswers = new Vector(answersList.size());
				Vector correctAnswers = new Vector(answersList.size());
				Vector textExplanation = new Vector(answersList.size());
				Vector codeAnswers = new Vector(answersList.size());

				Element elementAnswer = null;

				Vector numberOfCorrect = new Vector();

				// Stores the data of the answers in the variables
				for (int j = 0; j < answersList.size(); j++) {
					elementAnswer = (Element) answersList.get(j);
					textAnswers.add(elementAnswer.getChild("textAnswer").getText());
					correctAnswers.add(elementAnswer.getChild("correct").getText());
					textExplanation.add(elementAnswer.getChild("explanation").getText());
					codeAnswers.add(elementAnswer.getAttribute("codeAnswer").getValue());

					if (elementAnswer.getChild("correct").getText().equals("true"))
						numberOfCorrect.add(elementAnswer.getAttribute("codeAnswer").getValue());
				}

				String difficulty = elementQuestion.getChild("irtParameters").getChild("difficulty").getText();
				String discrimination = elementQuestion.getChild("irtParameters").getChild("discrimination")
						.getText();
				String guessing = elementQuestion.getChild("irtParameters").getChild("guessing").getText();
				String exhibitionRate = elementQuestion.getChild("statisticParameters")
						.getChild("exhibitionRate").getText();
				String answerTime = elementQuestion.getChild("statisticParameters").getChild("answerTime")
						.getText();
				String successRate = elementQuestion.getChild("statisticParameters").getChild("successRate")
						.getText();
				String numberOfUses = elementQuestion.getChild("statisticParameters").getChild("numberOfUses")
						.getText();
				String numberOfSuccesses = elementQuestion.getChild("statisticParameters").getChild(
						"numberOfSuccesses").getText();

				// Loops to obtain the tests that this question take part
				Vector classicTestVector = null;
				Vector adaptiveTestVector = null;
				if (elementQuestion.getChild("testFiles") != null) {
					Element elementTestFiles = elementQuestion.getChild("testFiles");

					// Classic tests
					if (elementTestFiles.getChild("classicTest") != null) {
						List testFilePathList = elementTestFiles.getChild("classicTest").getChildren();
						classicTestVector = new Vector();
						for (int j = 0; j < testFilePathList.size(); j++) {
							classicTestVector.add(((Element) testFilePathList.get(j)).getText());
						}
					}

					// Adaptive tests
					if (elementTestFiles.getChild("adaptiveTest") != null) {
						List testFilePathList = elementTestFiles.getChild("adaptiveTest").getChildren();
						adaptiveTestVector = new Vector();
						for (int j = 0; j < testFilePathList.size(); j++) {
							adaptiveTestVector.add(((Element) testFilePathList.get(j)).getText());
						}
					}
				}

				// Stores the data
				Question question = new Question();
				question.setCourse(courseName);
				question.setConcept(concept);
				question.setQuestionsFileName(questionsFileName);
				question.setCodeQuestion(codeQuestion);
				question.setEnunciate(enunciate);
				question.setPathImage(pathImage);
				question.setExistImage(String.valueOf(!pathImage.equals("")));
				question.setDifficulty(Double.valueOf(difficulty).doubleValue());
				question.setDiscrimination(Double.valueOf(discrimination).doubleValue());
				question.setGuessing(Double.valueOf(guessing).doubleValue());
				question.setExhibitionRate(Double.valueOf(exhibitionRate).doubleValue());
				question.setAnswerTime(Integer.valueOf(answerTime).intValue());
				question.setSuccessRate(Double.valueOf(successRate).doubleValue());
				question.setNumberOfUses(Integer.valueOf(numberOfUses).intValue());
				question.setNumberOfSuccesses(Integer.valueOf(numberOfSuccesses).intValue());
				question.setTextAnswers(textAnswers);
				question.setCorrectAnswers(correctAnswers);
				question.setTextExplanation(textExplanation);
				question.setCodeAnswers(codeAnswers);

				if (classicTestVector != null && classicTestVector.isEmpty() == false) {
					question.setClassicTest("true");
				} else {
					question.setClassicTest("false");
				}

				if (adaptiveTestVector != null && adaptiveTestVector.isEmpty() == false) {
					question.setAdaptiveTest("true");
				} else {
					question.setAdaptiveTest("false");
				}

				question.setClassicTestVector(classicTestVector);
				question.setAdaptiveTestVector(adaptiveTestVector);
				question.setTotalQuestionsInFile(questionsList.size());
				question.setFirstLastQuestion(firstLastQuestion);
				question.setNumberOfAnswers(Integer.valueOf(numberOfAnswers).intValue());
				question.setNumberQuestionOrder(numberQuestionOrder);
				question.setAnswersCorrect(numberOfCorrect);
				
				return question;

			} else {
				// The question file has no questions
				return null;
			}	
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}
	} // End of getQuestionByOrder method



	 /**
	  * Obtains a question searched by their code and returns a
	  * Question object with the data.
	  * @param codeQuestionSearch Code of the question to search
	  * @return A Question object with the found question or null
	  */
	 public Question getQuestionByCode(final String codeQuestionSearch) {
	//{
		try {
			// Loads the question file
			SAXBuilder builder = new SAXBuilder();
			File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
						+ testEditorTypeFile);
			Document xmlDocument = builder.build(fileQuestions);

			Element fileOfQuestions = xmlDocument.getRootElement();
			Element elementConcept = fileOfQuestions.getChild("concept");
			String concept = elementConcept.getAttribute("value").getValue().trim();

			Element elementQuestions = fileOfQuestions.getChild("questions");
			List questionsList = elementQuestions.getChildren();

			if (questionsList.isEmpty() == false) {
				// Looks for the question
				Element elementQuestion = (Element) XPath.selectSingleNode(xmlDocument,
						"fileOfQuestions/questions" + "/question[@codeQuestion=\"" + codeQuestionSearch + "\"]");

				if (elementQuestion != null) {
					// Calculates the position of the question in the file
					// (is the first, the last, etc..?)
					String firstLastQuestion = "";
					if (questionsList.size() == 1) {
						firstLastQuestion = "one question";
					} else {
						if (elementQuestion.equals(questionsList.get(0))) {
							firstLastQuestion = "first";
						} else {
							if (elementQuestion.equals(questionsList.get(questionsList.size() - 1))) {
								firstLastQuestion = "last";
							} else {
								firstLastQuestion = "NoLastNoFirst";
							}
						}
					}

					int numberQuestionOrder = questionsList.indexOf(elementQuestion);

					// Gets the data of the searched question
					String codeQuestion = elementQuestion.getAttribute("codeQuestion").getValue();
					String enunciate = elementQuestion.getChild("enunciate").getText();
					String pathImage = elementQuestion.getChild("image").getText();

					Element elementAnswers = elementQuestion.getChild("answers");

					// Gets the data of the answers of the question
					List answersList = elementAnswers.getChildren();
					String numberOfAnswers = String.valueOf(answersList.size());

					Vector textAnswers = new Vector(answersList.size());
					Vector correctAnswers = new Vector(answersList.size());
					Vector textExplanation = new Vector(answersList.size());
					Vector codeAnswers = new Vector(answersList.size());

					Element elementAnswer = null;

					Vector numberOfCorrect = new Vector();

					// Stores the data of the answers in the variables
					for (int j = 0; j < answersList.size(); j++) {
						elementAnswer = (Element) answersList.get(j);
						textAnswers.add(elementAnswer.getChild("textAnswer").getText());
						correctAnswers.add(elementAnswer.getChild("correct").getText());
						textExplanation.add(elementAnswer.getChild("explanation").getText());
						codeAnswers.add(elementAnswer.getAttribute("codeAnswer").getValue());

						if (elementAnswer.getChild("correct").getText().equals("true"))
							numberOfCorrect.add(elementAnswer.getAttribute("codeAnswer").getValue());
					}

					String difficulty = elementQuestion.getChild("irtParameters").getChild("difficulty").getText();
					String discrimination = elementQuestion.getChild("irtParameters").getChild("discrimination")
							.getText();
					String guessing = elementQuestion.getChild("irtParameters").getChild("guessing").getText();

					String exhibitionRate = elementQuestion.getChild("statisticParameters").getChild(
							"exhibitionRate").getText();
					String answerTime = elementQuestion.getChild("statisticParameters").getChild("answerTime")
							.getText();
					String successRate = elementQuestion.getChild("statisticParameters").getChild("successRate")
							.getText();
					String numberOfUses = elementQuestion.getChild("statisticParameters").getChild("numberOfUses")
							.getText();
					String numberOfSuccesses = elementQuestion.getChild("statisticParameters").getChild(
							"numberOfSuccesses").getText();

					// Loops to obtain the tests that this question take part in
					Vector classicTestVector = null;
					Vector adaptiveTestVector = null;
					if (elementQuestion.getChild("testFiles") != null) {
						Element elementTestFiles = elementQuestion.getChild("testFiles");
						
						// Classic tests
						if (elementTestFiles.getChild("classicTest") != null) {
							List testFilePathList = elementTestFiles.getChild("classicTest").getChildren();
							classicTestVector = new Vector();
							for (int j = 0; j < testFilePathList.size(); j++) {
								classicTestVector.add(((Element) testFilePathList.get(j)).getText());
							}
						}

						// Adaptive tests
						if (elementTestFiles.getChild("adaptiveTest") != null) {
							List testFilePathList = elementTestFiles.getChild("adaptiveTest").getChildren();
							adaptiveTestVector = new Vector();
							for (int j = 0; j < testFilePathList.size(); j++) {
								adaptiveTestVector.add(((Element) testFilePathList.get(j)).getText());
							}
						}
					}

					// Stores the data
					Question question = new Question();
					question.setCourse(courseName);
					question.setConcept(concept);
					question.setQuestionsFileName(questionsFileName);
					question.setCodeQuestion(codeQuestion);
					question.setEnunciate(enunciate);
					question.setPathImage(pathImage);
					question.setExistImage(String.valueOf(!pathImage.equals("")));
					question.setDifficulty(Double.valueOf(difficulty).doubleValue());
					question.setDiscrimination(Double.valueOf(discrimination).doubleValue());
					question.setGuessing(Double.valueOf(guessing).doubleValue());
					question.setExhibitionRate(Double.valueOf(exhibitionRate).doubleValue());
					question.setAnswerTime(Integer.valueOf(answerTime).intValue());
					question.setSuccessRate(Double.valueOf(successRate).doubleValue());
					question.setNumberOfUses(Integer.valueOf(numberOfUses).intValue());
					question.setNumberOfSuccesses(Integer.valueOf(numberOfSuccesses).intValue());
					question.setTextAnswers(textAnswers);
					question.setCorrectAnswers(correctAnswers);
					question.setTextExplanation(textExplanation);
					question.setCodeAnswers(codeAnswers);

					if (classicTestVector != null && classicTestVector.isEmpty() == false)
						question.setClassicTest("true");
					else
						question.setClassicTest("false");

					if (adaptiveTestVector != null && adaptiveTestVector.isEmpty() == false)
						question.setAdaptiveTest("true");
					else
						question.setAdaptiveTest("false");

					question.setClassicTestVector(classicTestVector);
					question.setAdaptiveTestVector(adaptiveTestVector);
					question.setTotalQuestionsInFile(questionsList.size());
					question.setFirstLastQuestion(firstLastQuestion);
					question.setNumberOfAnswers(Integer.valueOf(numberOfAnswers).intValue());
					question.setNumberQuestionOrder(numberQuestionOrder);
					question.setAnswersCorrect(numberOfCorrect);
					
					return question;
					
				} else {
					// Question not found with XPath
					return null;
				}
			} else {
				// The question file has no questions
				return null;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}
	} // End of getQuestionByCode method

	 
	 /**
	  * Obtains the data of all the questions of the question file and stores them
	  * in a Vector of Question objects. The Vector object will have so many Questions
	  * objects as questions the file has.
	  * Returns the vector object or null
	  * @return A Vector object or null
	  */
	 public Vector getQuestions() {
	//{
		try {
			// Loads the question file
			SAXBuilder builder = new SAXBuilder();
			File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
						+ testEditorTypeFile);
			Document xmlDocument = builder.build(fileQuestions);

			Element fileOfQuestions = xmlDocument.getRootElement();
			Element elementConcept = fileOfQuestions.getChild("concept");
			String concept = elementConcept.getAttribute("value").getValue().trim();

			Element elementQuestions = fileOfQuestions.getChild("questions");
			List questionsList = elementQuestions.getChildren();

			if (questionsList.isEmpty() == false) {
				Vector questionVector = new Vector();

				// Loops to obtain the data of all the question in the file
				for (int i = 0; i < questionsList.size(); i++) {

					Element elementQuestion = (Element) questionsList.get(i);
					int numberQuestionOrder = i;

					// Gets the data of a question from the file
					String codeQuestion = elementQuestion.getAttribute("codeQuestion").getValue();
					String enunciate = elementQuestion.getChild("enunciate").getText();
					String pathImage = elementQuestion.getChild("image").getText();

					Element elementAnswers = elementQuestion.getChild("answers");

					// Gets the data of the answers to a question from the file
					List answersList = elementAnswers.getChildren();
					String numberOfAnswers = String.valueOf(answersList.size());

					Vector textAnswers = new Vector(answersList.size());
					Vector correctAnswers = new Vector(answersList.size());
					Vector textExplanation = new Vector(answersList.size());
					Vector codeAnswers = new Vector(answersList.size());

					Element elementAnswer = null;

					Vector numberOfCorrect = new Vector();

					// Stores the data of the answers in the temporal variables
					for (int j = 0; j < answersList.size(); j++) {
						elementAnswer = (Element) answersList.get(j);
						textAnswers.add(elementAnswer.getChild("textAnswer").getText());
						correctAnswers.add(elementAnswer.getChild("correct").getText());
						textExplanation.add(elementAnswer.getChild("explanation").getText());
						codeAnswers.add(elementAnswer.getAttribute("codeAnswer").getValue());

						if (elementAnswer.getChild("correct").getText().equals("true"))
							numberOfCorrect.add(elementAnswer.getAttribute("codeAnswer").getValue());
					}

					String difficulty = elementQuestion.getChild("irtParameters").getChild("difficulty").getText();
					String discrimination = elementQuestion.getChild("irtParameters").getChild("discrimination")
							.getText();
					String guessing = elementQuestion.getChild("irtParameters").getChild("guessing").getText();
					String exhibitionRate = elementQuestion.getChild("statisticParameters").getChild(
							"exhibitionRate").getText();
					String answerTime = elementQuestion.getChild("statisticParameters").getChild("answerTime")
							.getText();
					String successRate = elementQuestion.getChild("statisticParameters").getChild("successRate")
							.getText();
					String numberOfUses = elementQuestion.getChild("statisticParameters").getChild("numberOfUses")
							.getText();
					String numberOfSuccesses = elementQuestion.getChild("statisticParameters").getChild(
							"numberOfSuccesses").getText();

					// Loops to obtain the test that this question take part
					Vector classicTestVector = null;
					Vector adaptiveTestVector = null;
					if (elementQuestion.getChild("testFiles") != null) {
						Element elementTestFiles = elementQuestion.getChild("testFiles");
						
						// Classic tests
						if (elementTestFiles.getChild("classicTest") != null) {
							List testFilePathList = elementTestFiles.getChild("classicTest").getChildren();
							classicTestVector = new Vector();
							for (int j = 0; j < testFilePathList.size(); j++) {
								classicTestVector.add(((Element) testFilePathList.get(j)).getText());
							}
						}

						// Adaptive tests
						if (elementTestFiles.getChild("adaptiveTest") != null) {
							List testFilePathList = elementTestFiles.getChild("adaptiveTest").getChildren();
							adaptiveTestVector = new Vector();
							for (int j = 0; j < testFilePathList.size(); j++) {
								adaptiveTestVector.add(((Element) testFilePathList.get(j)).getText());
							}
						}
					}

					// Stores the data
					Question question = new Question();
					question.setCourse(courseName);
					question.setConcept(concept);
					question.setQuestionsFileName(questionsFileName);
					question.setCodeQuestion(codeQuestion);
					question.setEnunciate(enunciate);
					question.setPathImage(pathImage);
					question.setExistImage(String.valueOf(!pathImage.equals("")));
					question.setDifficulty(Double.valueOf(difficulty).doubleValue());
					question.setDiscrimination(Double.valueOf(discrimination).doubleValue());
					question.setGuessing(Double.valueOf(guessing).doubleValue());
					question.setExhibitionRate(Double.valueOf(exhibitionRate).doubleValue());
					question.setAnswerTime(Integer.valueOf(answerTime).intValue());
					question.setSuccessRate(Double.valueOf(successRate).doubleValue());
					question.setNumberOfUses(Integer.valueOf(numberOfUses).intValue());
					question.setNumberOfSuccesses(Integer.valueOf(numberOfSuccesses).intValue());
					question.setTextAnswers(textAnswers);
					question.setCorrectAnswers(correctAnswers);
					question.setTextExplanation(textExplanation);
					question.setCodeAnswers(codeAnswers);

					if (classicTestVector != null && classicTestVector.isEmpty() == false)  {
						question.setClassicTest("true");
					} else {
						question.setClassicTest("false");
					}
					if (adaptiveTestVector != null && adaptiveTestVector.isEmpty() == false) {
						question.setAdaptiveTest("true");
					} else {
						question.setAdaptiveTest("false");
					}

					question.setClassicTestVector(classicTestVector);
					question.setAdaptiveTestVector(adaptiveTestVector);
					question.setTotalQuestionsInFile(questionsList.size());
					question.setFirstLastQuestion("");
					question.setNumberOfAnswers(Integer.valueOf(numberOfAnswers).intValue());
					question.setNumberQuestionOrder(numberQuestionOrder);
					question.setAnswersCorrect(numberOfCorrect);

					questionVector.add(question);
				}
				
				return questionVector;

			} else {
				// The file has no questions
				return null;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}
	} // End of getQuestions method


	 /**
	  * Obtains the questions from the question file and returns a Vector with
	  * so many Question objects as "numQuestionToGet" parameter.
	  * @param numQuestionsToGet Number of questions to get from the question file.
	  * @return A Vector with Question objects or null.
	  */
	 public Vector getRandomQuestions(final int numQuestionsToGet) {
	//{
		try {
			// Loads the source file
			SAXBuilder builder = new SAXBuilder();
			File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
						+ testEditorTypeFile);
			Document xmlDocument = builder.build(fileQuestions);

			Element fileOfQuestions = xmlDocument.getRootElement();
			Element elementConcept = fileOfQuestions.getChild("concept");
			String concept = elementConcept.getAttribute("value").getValue();

			Element elementQuestions = fileOfQuestions.getChild("questions");
			List questionsList = elementQuestions.getChildren();

			if (questionsList.isEmpty() == false && numQuestionsToGet <= questionsList.size()) {
				Vector questionVector = new Vector();

				// Creates a random number generator
				Random randomGen = new Random();

				// Vector to store the random number already generated
				// to avoid repetitions
				Vector numRandomVector = new Vector();

				int numRandom = -1;

				// Loops to obtain the data of all the questions of the file
				for (int i = 0; i < numQuestionsToGet; i++) {

					// Generates a random number
					boolean randomGenSuccess = false;
					while (randomGenSuccess == false) {
						numRandom = randomGen.nextInt(questionsList.size());
						if (i == 0) {
							numRandomVector.add(String.valueOf(numRandom));
							randomGenSuccess = true;
						} else {
							if (numRandomVector.contains(String.valueOf(numRandom)) == false) {
								numRandomVector.add(String.valueOf(numRandom));
								randomGenSuccess = true;
							} else {
								randomGenSuccess = false;
							}
						}
					}

					// Gets the question selected
					Element elementQuestion = (Element) questionsList.get(numRandom);
					int numberQuestionOrder = numRandom;

					// Gets the data of the question
					String codeQuestion = elementQuestion.getAttribute("codeQuestion").getValue();
					String enunciate = elementQuestion.getChild("enunciate").getText();
					String pathImage = elementQuestion.getChild("image").getText();

					Element elementAnswers = elementQuestion.getChild("answers");

					// Gets the data of the answers
					List answersList = elementAnswers.getChildren();
					String numberOfAnswers = String.valueOf(answersList.size());

					Vector textAnswers = new Vector(answersList.size());
					Vector correctAnswers = new Vector(answersList.size());
					Vector textExplanation = new Vector(answersList.size());
					Vector codeAnswers = new Vector(answersList.size());

					Element elementAnswer = null;
					Vector numberOfCorrect = new Vector();

					for (int j = 0; j < answersList.size(); j++) {
						elementAnswer = (Element) answersList.get(j);
						textAnswers.add(elementAnswer.getChild("textAnswer").getText());
						correctAnswers.add(elementAnswer.getChild("correct").getText());
						textExplanation.add(elementAnswer.getChild("explanation").getText());
						codeAnswers.add(elementAnswer.getAttribute("codeAnswer").getValue());

						if (elementAnswer.getChild("correct").getText().equals("true"))
							numberOfCorrect.add(elementAnswer.getAttribute("codeAnswer").getValue());
					}

					String difficulty = elementQuestion.getChild("irtParameters").getChild("difficulty").getText();
					String discrimination = elementQuestion.getChild("irtParameters").getChild("discrimination")
							.getText();
					String guessing = elementQuestion.getChild("irtParameters").getChild("guessing").getText();

					String exhibitionRate = elementQuestion.getChild("statisticParameters").getChild(
							"exhibitionRate").getText();
					String answerTime = elementQuestion.getChild("statisticParameters").getChild("answerTime")
							.getText();
					String successRate = elementQuestion.getChild("statisticParameters").getChild("successRate")
							.getText();
					String numberOfUses = elementQuestion.getChild("statisticParameters").getChild("numberOfUses")
							.getText();
					String numberOfSuccesses = elementQuestion.getChild("statisticParameters").getChild(
							"numberOfSuccesses").getText();

					// Loops to obtain the tests that this question takes part in
					Vector classicTestVector = null;
					Vector adaptiveTestVector = null;
					if (elementQuestion.getChild("testFiles") != null) {
						Element elementTestFiles = elementQuestion.getChild("testFiles");
						
						// Classic tests
						if (elementTestFiles.getChild("classicTest") != null) {
							List testFilePathList = elementTestFiles.getChild("classicTest").getChildren();
							classicTestVector = new Vector();
							for (int j = 0; j < testFilePathList.size(); j++) {
								classicTestVector.add(((Element) testFilePathList.get(j)).getText());
							}
						}

						// Adaptive tests
						if (elementTestFiles.getChild("adaptiveTest") != null) {
							List testFilePathList = elementTestFiles.getChild("adaptiveTest").getChildren();
							adaptiveTestVector = new Vector();
							for (int j = 0; j < testFilePathList.size(); j++) {
								adaptiveTestVector.add(((Element) testFilePathList.get(j)).getText());
							}
						}
					}

					// Stores data
					Question question = new Question();
					question.setCourse(courseName);
					question.setConcept(concept);
					question.setQuestionsFileName(questionsFileName);
					question.setCodeQuestion(codeQuestion);
					question.setEnunciate(enunciate);
					question.setPathImage(pathImage);
					question.setExistImage(String.valueOf(!pathImage.equals("")));
					question.setDifficulty(Double.valueOf(difficulty).doubleValue());
					question.setDiscrimination(Double.valueOf(discrimination).doubleValue());
					question.setGuessing(Double.valueOf(guessing).doubleValue());
					question.setExhibitionRate(Double.valueOf(exhibitionRate).doubleValue());
					question.setAnswerTime(Integer.valueOf(answerTime).intValue());
					question.setSuccessRate(Double.valueOf(successRate).doubleValue());
					question.setNumberOfUses(Integer.valueOf(numberOfUses).intValue());
					question.setNumberOfSuccesses(Integer.valueOf(numberOfSuccesses).intValue());
					question.setTextAnswers(textAnswers);
					question.setCorrectAnswers(correctAnswers);
					question.setTextExplanation(textExplanation);
					question.setCodeAnswers(codeAnswers);

					if (classicTestVector != null && classicTestVector.isEmpty() == false)
						question.setClassicTest("true");
					else
						question.setClassicTest("false");

					if (adaptiveTestVector != null && adaptiveTestVector.isEmpty() == false)
						question.setAdaptiveTest("true");
					else
						question.setAdaptiveTest("false");

					question.setClassicTestVector(classicTestVector);
					question.setAdaptiveTestVector(adaptiveTestVector);
					question.setTotalQuestionsInFile(questionsList.size());
					question.setFirstLastQuestion("");
					question.setNumberOfAnswers(Integer.valueOf(numberOfAnswers).intValue());
					question.setNumberQuestionOrder(numberQuestionOrder);
					question.setAnswersCorrect(numberOfCorrect);

					questionVector.add(question);
				}
				
				return questionVector;

			} else {
				// The file has no questions
				// Also --> the file has no so many questions as required
				return null;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}
	} // End of getRandomQuestions method


	 /**
	  * Selects several questions from the question file restricted by
	  * some parameters. Returns a Vector with Question objects or null
	  * @param numQuestionsToGet Number of questions to get
	  * @param question Question with the parameters to do the search in the file
	  * @return A Vector with Question object or null
	  */
	 public Vector getRandomRestrictionsQuestions(final int numQuestionsToGet, final Question question) {
	//{
		try {
			// Loads the source file
			SAXBuilder builder = new SAXBuilder();
			File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
						+ testEditorTypeFile);
			Document xmlDocument = builder.build(fileQuestions);

			Element fileOfQuestions = xmlDocument.getRootElement();
			Element elementConcept = fileOfQuestions.getChild("concept");
			String concept = elementConcept.getAttribute("value").getValue().trim();

			Element elementQuestions = fileOfQuestions.getChild("questions");
			List questionsListAll = elementQuestions.getChildren();
			
			if (questionsListAll.isEmpty() == false) {
				// Builds the search string
				StringBuffer searchString = new StringBuffer();
				searchString.append("fileOfQuestions/questions");
				searchString.append("//question[@codeQuestion");
				searchString.append(question.getCodeQuestion() + "]");

				if (question.getClassicTest().equals("false")) {
					searchString.append("[not(child::/testFiles/classicTest)]");
				}

				if (question.getAdaptiveTest().equals("false")) {
					searchString.append("[not(child::/testFiles/adaptiveTest)]");
				}

				searchString.append("/enunciate[contains(text(),\"");
				searchString.append(question.getEnunciate() + "\"]/..");

				if (question.getExistImage().equals("false")) {
					searchString.append("/image[string-length(text()) = 0]/..");
				} else if (question.getExistImage().equals("true")) {
					searchString.append("/image[string-length(text()) > 0]/..");
				}

				searchString.append("/answers[number(@numberOfAnswers) <= number(");
				searchString.append(question.getNumberOfAnswersMax() + ")]/..");
				searchString.append("/answers[number(@numberOfAnswers) >= number(" + question.getNumberOfAnswersMin());
				searchString.append(")]/..");

				searchString.append("/irtParameters" + "/difficulty[number(text()) <= number(");
				searchString.append(question.getDifficultyMax() + ")]/.." + "/difficulty[number(text()) >= number(");
				searchString.append(question.getDifficultyMin() + ")]/.." + "/discrimination[number(text()) <= number(");
				searchString.append(question.getDiscriminationMax() + ")]/.." + "/discrimination[number(text()) >= number(");
				searchString.append(question.getDiscriminationMin() + ")]/.." + "/guessing[number(text()) <= number(");
				searchString.append(question.getGuessingMax() + ")]/.." + "/guessing[number(text()) >= number(");
				searchString.append(question.getGuessingMin() + ")]/../..");

				searchString.append("/statisticParameters");
				searchString.append("/exhibitionRate[number(text()) <= number(" + question.getExhibitionRateMax() + ")]/..");
				searchString.append("/exhibitionRate[number(text()) >= number(" + question.getExhibitionRateMin() + ")]/..");
				searchString.append("/answerTime[number(text()) <= number(" + question.getAnswerTimeMax() + ")]/..");
				searchString.append("/answerTime[number(text()) >= number(" + question.getAnswerTimeMin() + ")]/..");
				searchString.append("/successRate[number(text()) <= number(" + question.getSuccessRateMax() + ")]/..");
				searchString.append("/successRate[number(text()) >= number(" + question.getSuccessRateMin() + ")]/../..");

				// Makes the XPath query
				List questionsList = XPath.selectNodes(xmlDocument, searchString.toString());

				if (questionsList != null && questionsList.size() > 0) {

					Vector questionVector = new Vector();
					
					Random randomGen = new Random();
	
					// Vector to store the random numbers already generated, to avoid repetitions
					Vector numRandomVector = new Vector();
	
					int numRandom = -1;
	
					// Loops to obtain the data of al the questions from the searched questions
					for (int i = 0; i < numQuestionsToGet; i++) {
						if (numRandomVector.size() >= questionsList.size())
							break;
	
						// Generaci�n del n�mero aleatorio.
						boolean randomGenSuccess = false;
						while (randomGenSuccess == false) {
							numRandom = randomGen.nextInt(questionsList.size());
							if (i == 0) {
								numRandomVector.add(String.valueOf(numRandom));
								randomGenSuccess = true;
							} else {
								if (numRandomVector.contains(String.valueOf(numRandom)) == false) {
									numRandomVector.add(String.valueOf(numRandom));
									randomGenSuccess = true;
								} else
									randomGenSuccess = false;
							}
						}
	
						Element elementQuestion = (Element) questionsList.get(numRandom);
						int numberQuestionOrder = questionsListAll.indexOf(elementQuestion);
	
						// Obtains the data of a question
						String codeQuestion = elementQuestion.getAttribute("codeQuestion").getValue();
						String enunciate = elementQuestion.getChild("enunciate").getText();
						String pathImage = elementQuestion.getChild("image").getText();
	
						Element elementAnswers = elementQuestion.getChild("answers");
	
						// Gets the data of the answers to a question
						List answersList = elementAnswers.getChildren();
						String numberOfAnswers = String.valueOf(answersList.size());
	
						Vector textAnswers = new Vector(answersList.size());
						Vector correctAnswers = new Vector(answersList.size());
						Vector textExplanation = new Vector(answersList.size());
						Vector codeAnswers = new Vector(answersList.size());
	
						Element elementAnswer = null;
	
						Vector numberOfCorrect = new Vector();
	
						for (int j = 0; j < answersList.size(); j++) {
							elementAnswer = (Element) answersList.get(j);
							textAnswers.add(elementAnswer.getChild("textAnswer").getText());
							correctAnswers.add(elementAnswer.getChild("correct").getText());
							textExplanation.add(elementAnswer.getChild("explanation").getText());
							codeAnswers.add(elementAnswer.getAttribute("codeAnswer").getValue());
	
							if (elementAnswer.getChild("correct").getText().equals("true"))
								numberOfCorrect.add(elementAnswer.getAttribute("codeAnswer").getValue());
						}
	
						String difficulty = elementQuestion.getChild("irtParameters").getChild("difficulty").getText();
						String discrimination = elementQuestion.getChild("irtParameters").getChild("discrimination")
								.getText();
						String guessing = elementQuestion.getChild("irtParameters").getChild("guessing").getText();
	
						String exhibitionRate = elementQuestion.getChild("statisticParameters").getChild(
								"exhibitionRate").getText();
						String answerTime = elementQuestion.getChild("statisticParameters").getChild("answerTime")
								.getText();
						String successRate = elementQuestion.getChild("statisticParameters").getChild("successRate")
								.getText();
						String numberOfUses = elementQuestion.getChild("statisticParameters").getChild("numberOfUses")
								.getText();
						String numberOfSuccesses = elementQuestion.getChild("statisticParameters").getChild(
								"numberOfSuccesses").getText();
	
						// Loops to get the tests that this question take part in
						Vector classicTestVector = null;
						Vector adaptiveTestVector = null;
						if (elementQuestion.getChild("testFiles") != null) {
							Element elementTestFiles = elementQuestion.getChild("testFiles");
							
							// Classic tests
							if (elementTestFiles.getChild("classicTest") != null) {
								List testFilePathList = elementTestFiles.getChild("classicTest").getChildren();
								classicTestVector = new Vector();
								for (int j = 0; j < testFilePathList.size(); j++) {
									classicTestVector.add(((Element) testFilePathList.get(j)).getText());
								}
							}
	
							// Adaptive tests
							if (elementTestFiles.getChild("adaptiveTest") != null) {
								List testFilePathList = elementTestFiles.getChild("adaptiveTest").getChildren();
								adaptiveTestVector = new Vector();
								for (int j = 0; j < testFilePathList.size(); j++) {
									adaptiveTestVector.add(((Element) testFilePathList.get(j)).getText());
								}
							}
						}
	
						// Stores the data
						Question questionReturn = new Question();
						questionReturn.setCourse(courseName);
						questionReturn.setConcept(concept);
						questionReturn.setQuestionsFileName(questionsFileName);
						questionReturn.setCodeQuestion(codeQuestion);
						questionReturn.setEnunciate(enunciate);
						questionReturn.setPathImage(pathImage);
						questionReturn.setExistImage(String.valueOf(!pathImage.equals("")));
						questionReturn.setDifficulty(Double.valueOf(difficulty).doubleValue());
						questionReturn.setDiscrimination(Double.valueOf(discrimination).doubleValue());
						questionReturn.setGuessing(Double.valueOf(guessing).doubleValue());
						questionReturn.setExhibitionRate(Double.valueOf(exhibitionRate).doubleValue());
						questionReturn.setAnswerTime(Integer.valueOf(answerTime).intValue());
						questionReturn.setSuccessRate(Double.valueOf(successRate).doubleValue());
						questionReturn.setNumberOfUses(Integer.valueOf(numberOfUses).intValue());
						questionReturn.setNumberOfSuccesses(Integer.valueOf(numberOfSuccesses).intValue());
						questionReturn.setTextAnswers(textAnswers);
						questionReturn.setCorrectAnswers(correctAnswers);
						questionReturn.setTextExplanation(textExplanation);
						questionReturn.setCodeAnswers(codeAnswers);
	
						if (classicTestVector != null && classicTestVector.isEmpty() == false)
							question.setClassicTest("true");
						else
							question.setClassicTest("false");
	
						if (adaptiveTestVector != null && adaptiveTestVector.isEmpty() == false)
							question.setAdaptiveTest("true");
						else
							question.setAdaptiveTest("false");
	
						questionReturn.setClassicTestVector(classicTestVector);
						questionReturn.setAdaptiveTestVector(adaptiveTestVector);
						questionReturn.setTotalQuestionsInFile(questionsList.size());
						questionReturn.setFirstLastQuestion("");
						questionReturn.setNumberOfAnswers(Integer.valueOf(numberOfAnswers).intValue());
						questionReturn.setNumberQuestionOrder(numberQuestionOrder);
						questionReturn.setAnswersCorrect(numberOfCorrect);
	
						questionVector.add(questionReturn);
					}
					
					return questionVector;

				} else {
					// There are no questions after the XPath search 
					return null;
				}
			} else {
				// There are no questions in the file
				return null;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}
	} // End of getRandomRestrictionsQuestions method


	 /**
	  * Receives a Vector object where in each position there is a name of a course
	  * and returns another Vector object where in each position there is a Course
	  * object that contains the names of the question files associated to it and
	  * the default number of answers for that course.  
	  * @param courseNameVector Vector object with String objects
	  * @return A Vector object with Course objects or null.
	  */	
	 public static Vector getQuestionsFileNames(final Vector courseNameVector) {
		Vector courseVector = new Vector();

		// Loops to obtain the data
		for (int i = 0; i < courseNameVector.size(); i++) {
			String courseName = (String) courseNameVector.get(i);
			Course course = new Course();
			course.setName(courseName);
			// Obtains the question file names
			FileList fileList = new FileList(courseName);
			course.setQuestionsFileNames(fileList.getQuestionsFileNames());
			// Obtains the default number of answers for that course
			course.setNumAnswersOfQuestionsFile(fileList.getNumberOfAnswersForQuestionsFile());

			courseVector.add(course);
		}

		if (courseVector.isEmpty()) {
			return null;
		} else {
			return courseVector;
		}

	} // End of getQuestionsFileNames method


	 /**
	  * Analyses the folder received as parameter and returns a Vector object
	  * with the names of the files contained in that folder
	  * @param testOldPath Relative path where the old question files are located
	  * @return A Vector object with the names of the files contained in the folder
	  * @throws Exception Error obtaining names
	  */
	 public static Vector getQuestionsFileNamesOld(final String testOldPath) throws Exception {
		Vector questionsFileOldList = null;
		int contAdd = 0;

		// Gets the folder where the files are located
		String testOldDirectoryPath = WOWStatic.config.Get("WOWROOT") + testOldPath;
		if (testOldDirectoryPath.trim().endsWith("/") == false) {
			testOldDirectoryPath = testOldDirectoryPath.concat("/");
		}
		File testOldDirectory = new File(testOldDirectoryPath);
		boolean isDirectory = testOldDirectory.isDirectory();
		if (isDirectory == true && testOldDirectory.exists()) {
			// Gets the list of question files of a course
			File [] listOfFiles = testOldDirectory.listFiles();
			questionsFileOldList = new Vector();

			// Loops to obtains the names of the files from the folder
			for (int k = 0; k < listOfFiles.length; k++) {
				File f = listOfFiles[k];
				if ((f.getName()).lastIndexOf(testEditorTypeFile) != -1
				&& (f.isDirectory()) == false) {
					try {
						// Checks that the question file is an WOW! question file of older version
						boolean validate = validateQuestionsFileOld(testOldDirectory + "/" + f.getName());
						if (validate == true) {
							// Adds the name of the file withouth the .xml ending
							questionsFileOldList.add(f.getName().substring(0, f.getName().indexOf(testEditorTypeFile)));
							contAdd++; // A new file has been added
						}
					} catch (org.jdom.JDOMException e) {}
				}
			}
		} 

		// If there is any file added returns the Vector, else null
		if (contAdd > 0) {
			return questionsFileOldList;
		} else {
			return null;
		}
	} // End of getQuestionsFileNamesOld

	 
	 /**
	  * Returns a Course object with the name of a course, the name of the concept files
	  * of that course, and the names of the question files associated to that concepts.
	  * This method receives a Course object that must contains values for the concepts
	  * of the course because it analyses this values in order to find the question
	  * files associated to that concepts.
	  * @param course Course object received as parameter
	  * @return A Course object or null
	  */
	 public static Course getQuestionsFileNames(final Course course) {
		 Vector conceptCopyVector = (Vector) course.getConceptNames().clone();
		 String courseName = course.getName();
		 Course courseOut = new Course();
		 courseOut.setName(courseName);

		 // Set the abstract concept names and the real concept names
		 courseOut.setAbstractConceptNames(course.getAbstractConceptNames());
		 FileList fileList = new FileList(courseName);
		 courseOut.setConceptNames(fileList.getConceptsForQuestionsFile());

		 // Loops to select the concepts and question files to delete from the copy Vector
		 Vector conceptVector = new Vector();

		 for (int i = 0; i < conceptCopyVector.size(); i++) {
			 String conceptName = (String) conceptCopyVector.get(i);
			 if (conceptCopyVector.contains(conceptName) && courseOut.getConceptNames().contains(conceptName)) {
				 conceptVector.add(conceptName);
			 }
		 }

		 // Finally sets the concepts
		 courseOut.setQuestionsFileNames(fileList.getQuestionsFileNamesForConcept(conceptVector));
		 courseOut.setNumQuestionsOfFile(fileList.getNumberOfQuestionsForConcept(conceptVector));
		 courseOut.setNumAnswersOfQuestionsFile(fileList.getNumberOfAnswersForConcept(conceptVector));
		 courseOut.setConceptNames(conceptVector);

		 if (courseOut.getQuestionsFileNames() == null || courseOut.getQuestionsFileNames().isEmpty()) {
			 return null;
		 } else {
			 return courseOut;
		 }
	 } // End of getQuestionsFileNames method

	 
	 /**
	  * Updates the questions that belongs to a question file of a course, setting
	  * in each question the adaptive test in which the question appears.
	  * @param codeQuestion Code of the question to update
	  * @param testFileName Name of the test file where the question appears.
	  * @return true or false
	  */
	 public boolean setAdaptiveTestFileName(
			 final String codeQuestion,
			 final String testFileName) {
	//{
		try {
			// Loads the question file
			SAXBuilder builder = new SAXBuilder();
			String thePath = itemsPath + courseName + itemsFilesPath + questionsFileName + testEditorTypeFile;
			File fileQuestions = new File(thePath);
			Document xmlDocument = builder.build(fileQuestions);
			Element fileOfQuestions = xmlDocument.getRootElement();
			Element elementQuestions = fileOfQuestions.getChild("questions");
			List questionsList = elementQuestions.getChildren();
			String testPath = WOWStatic.config.Get("testpath");

			if (questionsList.isEmpty() == false) {
				// Selects the question
				Element elementQuestion = (Element) XPath.selectSingleNode(xmlDocument,
						"fileOfQuestions/questions/question[@codeQuestion=\"" + codeQuestion + "\"]");

				if (elementQuestion != null) {
					if (elementQuestion.getChild("testFiles") == null) {
						Element elementTestFiles = new Element("testFiles");
						Element elementAdaptiveTest = new Element("adaptiveTest");
						Element elementAdaptiveTestFilePath = new Element("adaptiveTestFilePath");
						elementAdaptiveTestFilePath = elementAdaptiveTestFilePath.setText(courseName + testPath
								+ testFileName + testEditorTypeFile);

						elementAdaptiveTest = elementAdaptiveTest.addContent(elementAdaptiveTestFilePath);
						elementTestFiles = elementTestFiles.addContent(elementAdaptiveTest);

						elementQuestion = elementQuestion.addContent(elementTestFiles);
					} else {
						Element elementTestFiles = elementQuestion.getChild("testFiles");
						Element elementAdaptiveTest = null;
						if (elementTestFiles.getChild("adaptiveTest") == null) {
							elementAdaptiveTest = new Element("adaptiveTest");
							Element elementAdaptiveTestFilePath = new Element("adaptiveTestFilePath");
							elementAdaptiveTestFilePath = elementAdaptiveTestFilePath.setText(courseName + testPath
									+ testFileName + testEditorTypeFile);

							elementAdaptiveTest = elementAdaptiveTest.addContent(elementAdaptiveTestFilePath);

							elementTestFiles = elementTestFiles.addContent(elementAdaptiveTest);
						} else {
							elementAdaptiveTest = elementTestFiles.getChild("adaptiveTest");
							Element elementAdaptiveTestFilePath = new Element("adaptiveTestFilePath");
							elementAdaptiveTestFilePath = elementAdaptiveTestFilePath.setText(courseName + testPath
									+ testFileName + testEditorTypeFile);

							elementAdaptiveTest = elementAdaptiveTest.addContent(elementAdaptiveTestFilePath);
						}
					}
				}

				// Save the updates.
				FileWriter xmlFileWriter = new FileWriter(new File(thePath));
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
				xmlFileWriter.close();
				fileOfQuestions = null;
				return true;

			} else {
				// There are no questions in the file
				return false;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	} // End of setAdaptiveTestFileNameInQuestion method


	 /**
	  * Checks if the question file analysed is an older WOW! version question file.
	  * In case yes returns true.
	  * @param questionsFileOldPath Relative path to a question file
	  * @return true or false
	  * @throws Exception Error validating file
	  */
	 private static boolean validateQuestionsFileOld(final String questionsFileOldPath) throws Exception {
		try {
			// loads the file and validates it
			File fileQuestions = new File(questionsFileOldPath);
			Document xmlDocument = new SAXBuilder(true).build(fileQuestions);

			// Gets the DTD of the document
			DocType docType = xmlDocument.getDocType();
			String systemID = docType.getSystemID();

			// Gets the path to the DTD's used in TestEditor
			String dtdTestEditorPath = WOWStatic.config.Get("testeditordtdpath");
			
			// Checks if the DTD is of an older WOW! version
			if (systemID != null && systemID.equals("../../.." + dtdTestEditorPath + "fileOfQuestionsOld.dtd")) {
				return true;
			} else {
				// If the DocType has no extern reference, checks the root element
				// If the root element is equals to "test", then is of an older WOW! version
				return docType.getElementName().equals("test");
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	} // End of validateQuestionsFileOld method

	 
	 /**
	  * Deletes the references to the adaptive test in the question
	  * file received as parameter.
	  * @param testFileName Name of the adaptive test file to remove
	  * @return false if any error occurs, true otherwise
	  */
	 public boolean deleteAdaptiveTest(final String testFileName) {
	 //{
		try {
			// Loads the question file
			File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
					+ testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileQuestions);

			Element fileOfQuestions = xmlDocument.getRootElement();
			Element elementQuestions = fileOfQuestions.getChild("questions");
			List questionsList = elementQuestions.getChildren();

			if (!questionsList.isEmpty()) {
				String testPath = WOWStatic.config.Get("testpath");
				String adaptiveTestFilePathString = courseName + testPath + testFileName + testEditorTypeFile;

				// Search the questions that contains references to the adaptive test
				questionsList = XPath.selectNodes(xmlDocument, "fileOfQuestions/questions/question/testFiles/"
						+ "adaptiveTest/adaptiveTestFilePath[text() = \"" + adaptiveTestFilePathString
						+ "\"]/../../..");

				for (int i = 0; i < questionsList.size(); i++) {
					Element elementQuestion = (Element) questionsList.get(i);
					List testFilePathList = elementQuestion.getChild("testFiles").getChild("adaptiveTest")
							.getChildren();

					for (int j = 0; j < testFilePathList.size(); j++) {
						// Updates the values of the questions
						Element elementAdaptiveTestFilePath = (Element) testFilePathList.get(j);

						if (elementAdaptiveTestFilePath.getText().equals(adaptiveTestFilePathString))
							elementQuestion.getChild("testFiles").getChild("adaptiveTest").removeContent(
									elementAdaptiveTestFilePath);
					}

					if (elementQuestion.getChild("testFiles").getChild("adaptiveTest").getChildren().isEmpty()) {
						Element elementAdaptiveTest = elementQuestion.getChild("testFiles")
								.getChild("adaptiveTest");
						elementQuestion.getChild("testFiles").removeContent(elementAdaptiveTest);
					}

					if (elementQuestion.getChild("testFiles").getChildren().isEmpty()) {
						Element elementTestFiles = elementQuestion.getChild("testFiles");
						elementQuestion.removeContent(elementTestFiles);
					}
				}

				// Save the updates.
				File xmlFile = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
						+ testEditorTypeFile);
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
				xmlFileWriter.close();
				fileOfQuestions = null;

				return true;

			} else {
				// The question file has no questions
				return false;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	}
	 
	 /**
	  * Updates the questions that belongs to the question files of a course,
	  * setting the classic test where this questions appears.
	  * @param codeQuestion Code of the question to update
	  * @param testFileName Name of the test file
	  * @return true or false
	  */
	 public boolean setClassicTestFileName(
			 final String codeQuestion,
			 final String testFileName) {
	//{
		try {
			// Loads the question file
			File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
						+ testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileQuestions);

			Element fileOfQuestions = xmlDocument.getRootElement();
			Element elementQuestions = fileOfQuestions.getChild("questions");
			List questionsList = elementQuestions.getChildren();

			if (questionsList.isEmpty() == false) {
				// Search the question to update
				Element elementQuestion = (Element) XPath.selectSingleNode(xmlDocument,
						"fileOfQuestions/questions/question[@codeQuestion=\"" + codeQuestion + "\"]");

				if (elementQuestion != null) {
					String testPath = WOWStatic.config.Get("testpath");
					if (elementQuestion.getChild("testFiles") == null) {
						Element elementTestFiles = new Element("testFiles");
						Element elementClassicTest = new Element("classicTest");
						Element elementClassicTestFilePath = new Element("classicTestFilePath");
						elementClassicTestFilePath = elementClassicTestFilePath.setText(courseName + testPath
								+ testFileName + testEditorTypeFile);

						elementClassicTest = elementClassicTest.addContent(elementClassicTestFilePath);
						elementTestFiles = elementTestFiles.addContent(elementClassicTest);

						elementQuestion = elementQuestion.addContent(elementTestFiles);

					} else {
						Element elementTestFiles = elementQuestion.getChild("testFiles");

						if (elementTestFiles.getChild("classicTest") == null) {
							Element elementClassicTest = new Element("classicTest");
							Element elementClassicTestFilePath = new Element("classicTestFilePath");
							elementClassicTestFilePath = elementClassicTestFilePath.setText(courseName + testPath
									+ testFileName + testEditorTypeFile);

							elementClassicTest = elementClassicTest.addContent(elementClassicTestFilePath);

							// Creates a new "testFiles" element
							Element elementTestFilesAux = new Element("testFiles");

							// Adds the new "classicTest" element to the aux "testFiles" element
							elementTestFilesAux = elementTestFilesAux.addContent(elementClassicTest);

							// Adds the "adaptiveTest" element if exists
							// to the aux "testFiles" element
							if (elementTestFiles.getChild("adaptiveTest") != null) {
								elementTestFilesAux.addContent(elementTestFiles.getChild("adaptiveTest").detach());
							}

							elementTestFiles = null;
							elementTestFiles = elementTestFilesAux;
							elementQuestion.getChild("testFiles").getChildren().clear();

							elementQuestion.getChild("testFiles").addContent(
									elementTestFiles.getChild("classicTest").detach());

							if (elementTestFiles.getChild("adaptiveTest") != null) {
								elementQuestion.getChild("testFiles").addContent(
										elementTestFiles.getChild("adaptiveTest").detach());
							}

						} else {
							Element elementClassicTest = elementTestFiles.getChild("classicTest");
							Element elementClassicTestFilePath = new Element("classicTestFilePath");
							elementClassicTestFilePath = elementClassicTestFilePath.setText(courseName + testPath
									+ testFileName + testEditorTypeFile);

							elementClassicTest = elementClassicTest.addContent(elementClassicTestFilePath);
						}
					}
				}

				// Save the updates
				File xmlFile = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
						+ testEditorTypeFile);
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
				xmlFileWriter.close();
				fileOfQuestions = null;

				return true;

			} else {
				// The question file has no questions
				return false;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	 }


	 /**
	  * Deletes the references to the classic test in the question
	  * file received as parameter.
	  * @param testFileName Name of the classic test file to remove
	  * @return false if any error occurs, true otherwise
	  */
	 public boolean deleteClassicTest(final String testFileName) {
	 //{
		try {
			// Loads the question file
			File fileQuestions = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
					+ testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(fileQuestions);

			Element fileOfQuestions = xmlDocument.getRootElement();
			Element elementQuestions = fileOfQuestions.getChild("questions");

			List questionsList = elementQuestions.getChildren();

			if (questionsList.isEmpty() == false) {
				String testPath = WOWStatic.config.Get("testpath");
				String classicTestFilePathString = courseName + testPath + testFileName + testEditorTypeFile;

				// Search the reference to the test file in the questions of the question file
				questionsList = XPath.selectNodes(xmlDocument, "fileOfQuestions/questions/question/testFiles/"
						+ "classicTest/classicTestFilePath[text() = \"" + classicTestFilePathString
						+ "\"]/../../..");

				for (int i = 0; i < questionsList.size(); i++) {
					// Updates each questinon
					Element elementQuestion = (Element) questionsList.get(i);
					List testFilePathList = elementQuestion.getChild("testFiles").getChild("classicTest")
							.getChildren();

					for (int j = 0; j < testFilePathList.size(); j++) {
						Element elementClassicTestFilePath = (Element) testFilePathList.get(j);

						if (elementClassicTestFilePath.getText().equals(classicTestFilePathString))
							elementQuestion.getChild("testFiles").getChild("classicTest").removeContent(
									elementClassicTestFilePath);
					}

					if (elementQuestion.getChild("testFiles").getChild("classicTest").getChildren().isEmpty()) {
						Element elementClassicTest = elementQuestion.getChild("testFiles").getChild("classicTest");
						elementQuestion.getChild("testFiles").removeContent(elementClassicTest);
					}

					if (elementQuestion.getChild("testFiles").getChildren().isEmpty()) {
						Element elementTestFiles = elementQuestion.getChild("testFiles");
						elementQuestion.removeContent(elementTestFiles);
					}
				}

				// Save the values.
				File xmlFile = new File(itemsPath + courseName + itemsFilesPath + questionsFileName
						+ testEditorTypeFile);
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
				xmlFileWriter.close();
				fileOfQuestions = null;

				return true;

			} else {
				// The question file is empty, with no questions
				return false;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	}
	 
	 

	 /**
	  * Creates the definitively file as the result of exporting a current
	  * WOW! version question file in the exportWOWQuestionFile of this class.
	  * @param testOldPath Relative path to result of file exporting
	  * @return true or false
	  * @see exportWOWQuestionFile
	  */
	 private boolean createQuestionsFileOld(final String testOldPath) {
	 //{
		 try {
			 // Loads the temporal file
			 File tempFile = new File(WOWStatic.config.Get("WOWROOT") + testOldPath + "Temp" + testEditorTypeFile);
			 BufferedReader br = new BufferedReader(new java.io.FileReader(tempFile));

			 // Loads the final file
			 BufferedWriter bw = new BufferedWriter(new FileWriter(WOWStatic.config.Get("WOWROOT") + testOldPath
					+ testEditorTypeFile));

			// Writes in the final file the first lines of it
			// This lines are the version and encoding of the XML file and the DTD reference
			bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			bw.newLine();
			bw.write("<!DOCTYPE test [");
			bw.newLine();
			bw.write("      <!ELEMENT test (question)+>");
			bw.newLine();
			bw.write("      <!ATTLIST test");
			bw.newLine();
			bw.write("            title CDATA #REQUIRED");
			bw.newLine();
			bw.write("            concept CDATA #REQUIRED");
			bw.newLine();
			bw.write("            total CDATA #REQUIRED");
			bw.newLine();
			bw.write("            ask CDATA #REQUIRED");
			bw.newLine();
			bw.write("            verbose (true | false) #REQUIRED");
			bw.newLine();
			bw.write(">");
			bw.newLine();
			bw.write("      <!ELEMENT question (#PCDATA | answer)*>");
			bw.newLine();
			bw.write("      <!ATTLIST question");
			bw.newLine();
			bw.write("            answers CDATA #REQUIRED");
			bw.newLine();
			bw.write("            right CDATA #REQUIRED");
			bw.newLine();
			bw.write(">");
			bw.newLine();
			bw.write("      <!ELEMENT answer (#PCDATA | explain)*>");
			bw.newLine();
			bw.write("      <!ATTLIST answer");
			bw.newLine();
			bw.write("            correct (true | false) #REQUIRED");
			bw.newLine();
			bw.write(">");
			bw.newLine();
			bw.write("      <!ELEMENT explain (#PCDATA)>");
			bw.newLine();
			bw.write("]>");
			bw.newLine();

			// Reads the temporal file and writes the lines in the final file
			String line = "";
			boolean find = false;
			while ((line = br.readLine()) != null) {
				if (line.indexOf("<test title=") != -1 || find == true) {
					find = true;
					bw.write(line);
				}
			}

			// Closes the files
			bw.close();
			br.close();

			// Removes the temporal file
			boolean deleteFile = tempFile.delete();
			if (deleteFile == false) {
				tempFile.deleteOnExit();
			}
			return true;

		} catch (java.io.IOException e) {
			UtilLog.writeException(e);
			return false;
		}
	} // End of createQuestionsFileOld method

} // End of QuestionFile class