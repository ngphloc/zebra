package es.uco.WOW.TestFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
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

import es.uco.WOW.FileList.FileList;
import es.uco.WOW.QuestionsFile.QuestionFile;
import es.uco.WOW.StudentFile.StudentFile;
import es.uco.WOW.TestEditor.TestEditor;
import es.uco.WOW.Utils.Course;
import es.uco.WOW.Utils.Question;
import es.uco.WOW.Utils.QuestionsFileTest;
import es.uco.WOW.Utils.StudentTest;
import es.uco.WOW.Utils.TempFile;
import es.uco.WOW.Utils.Test;
import es.uco.WOW.Utils.UtilLog;

/**
 * <p>Title: Wow! TestEditor</p> 
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * @version 1.0
 */

/**
 * NAME: TestFile
 * FUNCTION: This class manages all the options about the test files. It has methods for creating,
 *            updating and deleting elements from them. It works with the current version of the
 *            TestEditor tool.
 * LAST MODIFICATION: 06-02-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class TestFile {

	/** Name of the folder where the items and test files are stored */
	private static String itemsPath = WOWStatic.config.Get("XMLROOT") + WOWStatic.config.Get("itemspath"); 

	/** File type of the TestEditor tool */
	private static String testEditorTypeFile = WOWStatic.config.Get("testeditortypefile"); 
	
	/** Path of the folder where the configuration files of the test are stored. */
	private static String testPath = WOWStatic.config.Get("testpath"); 

	/** Path of the folder where the background images of the test are located. */
	private static String imagesTestPath = WOWStatic.config.Get("imagestestpath");

	/**
	 * Name of the course that this test belongs to
	 */
	private String courseName = null;
	/**
	 * Name of the test file
	 */
	private String testFileName = null;
	/**
	 * Stores the path to the test file
	 */
	private String pathToFile = null;

	/**
	 * Public constructor
	 * @param aCourseName Name of the course
	 * @param aTestFileName Name of the test file
	 */
	public TestFile (final String aCourseName, final String aTestFileName) {
		this.courseName = aCourseName;
		this.testFileName = aTestFileName;
		// Builds the path to the test file
		if (aTestFileName != null) {
			this.pathToFile = itemsPath + courseName + testPath + testFileName + testEditorTypeFile;
		}
	}
	
	
	/**
	 * Checks if exists a test file with the name passed as parameter
	 * in the course passed as parameter.
	 * @return true if the test file exists, false otherwise.
	 */
	public boolean exists() {
		File file = new File(pathToFile);
		return (file.exists() && file.isFile() && file.canWrite());
	}

	
	/**
	 * Checks if there are any student that has not finished the test. Returns
	 * true if all the students has finished the test, false in other case.
	 * @return true if all the students finished the test, false otherwise.
	 */
	public boolean checkStudentFinishTest() {
	//{
		try {
			// Checks for the test file and builds it
			Document xmlDocument = new SAXBuilder().build(new File(pathToFile));

			List studentList = XPath.selectNodes(xmlDocument,
					"testFile/students//student[@finish = \"false\"]");

			if (studentList != null) {
				return studentList.isEmpty();
			} else {
				return true;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return true;
		}
	}


	/**
	 * Creates the necessary folders to store the test files. Also copies the DTD file that defines a
	 * test file, if needed.
	 * @return true if the operation finish correctly, false otherwise.
	 */
	private boolean createDirectory() {
		// Checks if the folders exists
		File folderClassicTest = new File(itemsPath + courseName + testPath);
		if (!folderClassicTest.exists())
			folderClassicTest.mkdirs();

		// Checks if the DTD file exists
		File testFileDTDNew = new File(itemsPath + "testFile.dtd");
		if (!testFileDTDNew.exists()) {
			// Copies the DTD file to the folder where the test files
			// will be stored.
			try {
				File testFileDTD = new File(WOWStatic.config.Get("WOWROOT").substring(0,
						WOWStatic.config.Get("WOWROOT").length() - 1)
						+ WOWStatic.config.Get("testeditordtdpath") + "testFile.dtd");

				byte [] data = new byte[(int) testFileDTD.length()];

				BufferedInputStream input = new BufferedInputStream(new FileInputStream(testFileDTD));
				DataInputStream dataInputStream = new DataInputStream(input);
				dataInputStream.read(data);
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(itemsPath + "testFile.dtd"));
				DataOutputStream dataOutputStream = new DataOutputStream(out);
				dataOutputStream.write(data);

				input.close();
				dataInputStream.close();
				out.close();
				dataOutputStream.close();

			} catch (java.io.IOException e) {
				UtilLog.writeException(e);
				return false;
			}
		}

		return true;
	}

	
	/**
	  * Adds a reference of a question from a question file to a test file.
	  * @param questionsFileName Name of the question file
	  * @param codeQuestion Code of the question to add
	  * @param executionType Type of test ("classic", "adaptive")
	  * @return A String with a explanation of the process
	  */
	 public String addQuestionToTest(
			 final String questionsFileName,
			 final String codeQuestion,
			 final String executionType) {
	//{
		try {
			// Loads the test file
			SAXBuilder builder = new SAXBuilder();
			File testFile = new File(pathToFile);
			Document xmlDocument = builder.build(testFile);

			Element elementTestFile = xmlDocument.getRootElement();
			Element elementQuestions = elementTestFile.getChild("questions");
			List questionsFileList = elementQuestions.getChildren();

			if (questionsFileList.isEmpty() == false) {
				QuestionFile questionFile = new QuestionFile(courseName, questionsFileName);
				Question question = questionFile.getQuestionByCode(codeQuestion);

				// Checks and updates the concepts that this test evaluates
				Element concepts = elementTestFile.getChild("concepts");
				if (concepts != null) {
					Element concept = (Element) XPath.selectSingleNode(xmlDocument,
							"testFile/concepts/concept[@value = \"" + question.getConcept() + "\"]");

					if (concept == null) {
						// Checks if the test has only one concept.
						// In case yes the question cannot be added, because
						// it is associated to a different concept
						if (elementTestFile.getAttribute("type").getValue().trim().equals("activity")) {
							String message = "ERROR: The question has NOT been ADDED. ";
							message += "This test only evaluates a concept ";
							message += "that is different to the one that is associated to the ";
							message += "question that is trying to add.";
							return message;
						}

						concept = new Element("concept");
						concept = concept.setAttribute("value", question.getConcept());

						// Adds the new "concept" element to their parent "concepts"
						concepts = concepts.addContent(concept);
					}
				}

				String itemsFilesPath = WOWStatic.config.Get("itemsfilespath");
				String pathQuestionsFile = courseName + itemsFilesPath + questionsFileName + testEditorTypeFile;

				// Checks if the desired question is already in the test
				Element elementQuestionsFile = (Element) XPath.selectSingleNode(xmlDocument,
						"testFile/questions/questionsFile[@path = \"" + pathQuestionsFile + "\"]"
								+ "/question[@codeQuestion = \"" + codeQuestion + "\"]/..");

				if (elementQuestionsFile != null) {
					return "ERROR:  The question has NOT been ADDED. "
							+ "At this moment the question is already in the test.";
				} else {
					elementQuestionsFile = (Element) XPath.selectSingleNode(xmlDocument,
							"testFile/questions/questionsFile" + "[@path = \"" + pathQuestionsFile + "\"]");

					if (elementQuestionsFile != null) {
						// Creates the "question" element
						Element elementQuestion = new Element("question");
						elementQuestion = elementQuestion.setAttribute("codeQuestion", codeQuestion);

						// Adds the "codeQuestion" element to its parent
						elementQuestionsFile = elementQuestionsFile.addContent(elementQuestion);

						// Updates the "numberOfQuestions" attribute of the questionsFile element
						String value = String.valueOf(elementQuestionsFile.getChildren().size() - 1);
						elementQuestionsFile.getAttribute("numberOfQuestions").setValue(value);
					} else {
						// Resets the elementQuestionsFile element
						elementQuestionsFile = new Element("questionsFile");
						elementQuestionsFile = elementQuestionsFile.setAttribute("numberOfQuestions", String
								.valueOf(1));

						elementQuestionsFile = elementQuestionsFile.setAttribute("path", pathQuestionsFile);

						elementQuestionsFile = elementQuestionsFile.setAttribute("concept", question.getConcept());

						// Creates the "question" element
						Element elementQuestion = new Element("question");
						elementQuestion = elementQuestion.setAttribute("codeQuestion", codeQuestion);

						// Adds the "question" element to its parent
						elementQuestionsFile = elementQuestionsFile.addContent(elementQuestion);

						// Adds the new child node to the "elementQuestions" node
						elementQuestions = elementQuestions.addContent(elementQuestionsFile);
					}

					// Updates the number of question files that take part in the test
					String numberOfFiles = String.valueOf(elementQuestions.getChildren().size());
					elementQuestions = elementQuestions.setAttribute("numberOfFiles", numberOfFiles);

					// Updates the total number of questions that take part in the test
					String totalQuestion = String.valueOf(XPath.selectNodes(xmlDocument,
							"testFile/questions/questionsFile//question").size());

					elementTestFile = elementTestFile.setAttribute("totalQuestion", totalQuestion);

					// Save the updates
					try {
						File xmlFile = new File(pathToFile);
						FileWriter xmlFileWriter = new FileWriter(xmlFile);
						XMLOutputter out = new XMLOutputter();
						out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
						out.output(xmlDocument, xmlFileWriter);
						xmlFileWriter.close();
						testFile = null;
					} catch (IOException e) {
						UtilLog.writeException(e);
						return "ERROR: The question has NOT been ADDED. "
								+ "An error has happened updating the test file";
					}

					// Sets the name of the test in the recently added question
					boolean setTestFileName = false;
					if (executionType.trim().equals(TestEditor.CLASSIC)) {
						setTestFileName = questionFile.setClassicTestFileName(codeQuestion, testFileName);
					} else {
						setTestFileName = questionFile.setAdaptiveTestFileName(codeQuestion, testFileName);
					}

					if (setTestFileName) {
						// Correct execution
						return "The question has been added.";
					} else {
						// Error adding question
						return "ERROR: The question has NOT been ADDED. "
							+ "An error has happened manipulating the question file.";
					}
				}
			} else {
				// The test file has no questions
				return "ERROR: The question has NOT been ADDED. The test file is empty.";
			}
				
		} catch (Exception e) {
			UtilLog.writeException(e);
			return "ERROR: The question has NOT been ADDED. "
					+ "An error has happened manipulating the test file.";
		}
	} // End of addQuestionToTest method
	
	 
	 /**
	  * Deletes the references to a question in the test files where it appears.
	  * @param questionsFileName Name of the question file
	  * @param codeQuestion Code of the question to remove
	  * @param testVector Relative path to the test files that contains this question
	  * @param authorName Login of the author
	  * @return an Empty String (not null!) or an warning String if any error occurs. 
	  */
	 public String deleteQuestionInTestFiles(
			final String questionsFileName,
			final String codeQuestion,
			final Vector testVector,
			final String authorName) {
	//{
		String resultDelete = ""; // This string is the return of the method
		// Loops over all the test files
		for (int i = 0; i < testVector.size(); i++) {
			String testFilePath = testVector.get(i).toString();
			String pathToTestFile = testFilePath.substring(testFilePath.lastIndexOf("/") + 1, testFilePath
					.lastIndexOf(testEditorTypeFile));
			try {
				// Loads the test file
				File theTestFile = new File(itemsPath + courseName + testPath + pathToTestFile + testEditorTypeFile);
				if (!theTestFile.exists()) {
					// Checks if the test file exists
					resultDelete = resultDelete.concat("\n" + pathToTestFile);
					continue; // Next 'for' loop

				} else  {
					// Parses the test file
					Document xmlDocument = new SAXBuilder().build(theTestFile);
					Element elementTestFile = xmlDocument.getRootElement();

					// Reads the test type and some more parameters
					String executionType = elementTestFile.getAttribute("execution").getValue().trim();
					Element elementQuestions = elementTestFile.getChild("questions");
					List questionsFileList = elementQuestions.getChildren();
					String itemsFilesPath = WOWStatic.config.Get("itemsfilespath");

					if (questionsFileList.isEmpty() == false) {
						String pathQuestionsFile = courseName + itemsFilesPath + questionsFileName
								+ testEditorTypeFile;
	
						Element elementQuestion = (Element) XPath.selectSingleNode(xmlDocument,
								"testFile/questions/questionsFile[@path = \"" + pathQuestionsFile + "\"]"
										+ "/question[@codeQuestion = \"" + codeQuestion + "\"]");
	
						Element elementQuestionsFile = null;
						if (elementQuestion != null) {
							elementQuestionsFile = (Element) elementQuestion.getParent();
							elementQuestionsFile.removeContent(elementQuestion);
	
							if ((elementQuestionsFile.getChildren()).size() == 0) {
								// Reads the name of the concept associated to this file
								String conceptString = elementQuestionsFile.getAttribute("concept").getValue().trim();
	
								// Removes the question file
								elementQuestions.removeContent(elementQuestionsFile);
	
								// If there are no more question files associated to this concept,
								// then the concept must be removed from the test
								elementQuestionsFile = (Element) XPath.selectSingleNode(xmlDocument,
										"testFile/questions" + "/questionsFile[@concept = \"" + conceptString + "\"]");
	
								if (elementQuestionsFile == null) {
									// Removes the concept from the test
									Element elementConcept = (Element) XPath.selectSingleNode(xmlDocument,
											"testFile/concepts" + "/concept[@value = \"" + conceptString + "\"]");
	
									Element elementConcepts = (Element) elementConcept.getParent();
									elementConcepts.removeContent(elementConcept);
								}
							}
	
							if ((elementQuestions.getChildren()).isEmpty()) {
								// The test file has no more question, so it must be deleted.
								// Before we have to take a look to the students and
								// remove the references of that test file in the log file of the students

								// Gets the "students" element
								Element students = elementTestFile.getChild("students");
								List studentsList = null;
								if (students != null) {
									studentsList = students.getChildren();
								} else {
									studentsList = null;
								}

								String pathTestFile = elementTestFile.getAttribute("path").getValue().trim();

								if (studentsList != null) {
									// Loops to delete the references to the test file in the
									// log student test files
									for (int j = 0; j < studentsList.size(); j++) {
										Element student = (Element) studentsList.get(j);
										String login = student.getAttribute("login").getValue().trim();
										StudentFile studentFile = new StudentFile(courseName, login);
										studentFile.deleteTest(pathTestFile);
									}
								}
	
								// Deletes the test file because it has no questions
								theTestFile = new File(itemsPath + courseName + testPath + pathToTestFile + testEditorTypeFile);
	
								boolean deleteTestFile = theTestFile.delete();
								if (!deleteTestFile) {
									theTestFile.deleteOnExit();
								}

								// Removes the name of the file from the list of files of the course
								FileList fileList = new FileList(courseName);
								if (executionType.equals(TestEditor.CLASSIC)) {
									fileList.deleteClassicTestFile(pathToTestFile, authorName);
								} else {
									fileList.deleteAdaptiveTestFile(pathToTestFile, authorName);
								}
	
								String pathBackground = elementTestFile.getChild("parameters")
																.getChild("runningParameters")
																	.getChild("htmlParameters")
																		.getChild("background")
																			.getAttribute("value").getValue().trim();

								if (pathBackground.equals("") == false) {
									QuestionFile.deleteImage(pathBackground);
								}
	
							} else {
								// Updates the number of question files that take part in the test
								String numberOfFiles = String.valueOf(elementQuestions.getChildren().size());
								elementQuestions = elementQuestions.setAttribute("numberOfFiles", numberOfFiles);
	
								// Updates the total number of questions that take part in the test
								String totalQuestion = String.valueOf(XPath.selectNodes(xmlDocument,
										"testFile/questions/" + "questionsFile//question").size());
	
								elementTestFile = elementTestFile.setAttribute("totalQuestion", totalQuestion);
	
								// Saves the test file with the updates
								File xmlFile = new File(itemsPath + courseName + testPath + pathToTestFile
											+ testEditorTypeFile);
	
								FileWriter xmlFileWriter = new FileWriter(xmlFile);
								XMLOutputter out = new XMLOutputter();
								out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
								out.output(xmlDocument, xmlFileWriter);
								xmlFileWriter.close();
							}
						}
					}
				}
			} catch (Exception e) {
				UtilLog.writeException(e);
				resultDelete = resultDelete.concat("\n" + pathToTestFile);
				continue;
			}
		}

		if (resultDelete != null && !resultDelete.trim().equals("")) {
			resultDelete = "The references of this question have not been \n"
					+ "eliminated in the following files of test: " + resultDelete;
		}
		return resultDelete;

	 } // End of deleteQuestionInTestFile mnethod

	 
	/**
	 * Stores the associated image to the test. Creates the necessary folders to store that image if
	 * necessary. Returns true if the operation finish correctly, false otherwise.
	 * @param fileType Extension (File type with dot) of the image.
	 * @param data Image object in byte format.
	 * @return boolean
	 */
	public boolean saveBackground(final String fileType, final byte [] data) {
		boolean createDirectory = false;
		File directoryTestImages = new File(itemsPath + courseName + imagesTestPath);

		createDirectory = directoryTestImages.exists();
		if (createDirectory == false)
			createDirectory = directoryTestImages.mkdirs();

		if (createDirectory == true) {
			try {
				// Creates the image file
				FileOutputStream fileOutputStream = new FileOutputStream(itemsPath + courseName + imagesTestPath
						+ testFileName + fileType);
				DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
				dataOutputStream.write(data);

			} catch (java.io.FileNotFoundException fnfe) {
				UtilLog.writeException(fnfe);
				return false;

			} catch (java.io.IOException ioe) {
				UtilLog.writeException(ioe);
				return false;
			}
		}

		return createDirectory;
	}

	
	/**
	 * Creates a test file.
	 * @param test Test object with the data to create the test file
	 * @param authorName Login of the author of the test.
	 * @return true or false
	 */
	public boolean createTestFileA(final Test test, final String authorName) { // TODO
		// Total number of question that this test contains
		int totalQuestion;

		// Creates the DOCTYPE of the document and sets it
		DocType docType = new DocType("testFile", "../../testFile.dtd");
		Document xmlDocument = new Document().setDocType(docType);

		// Creates the root element of the document
		Element elementTestFile = new Element("testFile");
		elementTestFile = elementTestFile.setAttribute("totalQuestion", "0");
		elementTestFile = elementTestFile.setAttribute("lastUse", test.getLastUse());
		elementTestFile = elementTestFile.setAttribute("fileName", testFileName + testEditorTypeFile);
		elementTestFile = elementTestFile.setAttribute("name", test.getTestName().trim());
		elementTestFile = elementTestFile.setAttribute("course", courseName);
		elementTestFile = elementTestFile.setAttribute("type", test.getTestType().trim());
		elementTestFile = elementTestFile.setAttribute("execution", test.getExecutionType());
		elementTestFile = elementTestFile.setAttribute("path", courseName + testPath
				+ testFileName + testEditorTypeFile);
		elementTestFile = elementTestFile.setAttribute("enable", String.valueOf(test.getEnable()));

		// Sets the root element in the document
		xmlDocument = xmlDocument.setRootElement(elementTestFile);

		// Creates the concepts element
		Element concepts = new Element("concepts");
		if (test.getAbstractConcept().equals("") == false)
			concepts = concepts.setAttribute("abstractConcept", test.getAbstractConcept());

		// Creates the "concept" nodes
		for (int i = 0; i < test.getConceptVector().size(); i++) {
			Element concept = new Element("concept");
			concept = concept.setAttribute("value", test.getConceptVector().get(i).toString().trim());

			// Adds the "concept" element to his "concepts" parent
			concepts = concepts.addContent(concept);
		}

		// Creates the "question" node
		Element questions = new Element("questions");
		questions = questions.setAttribute("numberOfFiles", String.valueOf(test.getTestVector().size()));

		// Obtains the name of the question files and the codes of
		// all the questions of that files, that are on the test
		totalQuestion = 0;
		Vector testVector = test.getTestVector();
		for (int i = 0; i < testVector.size(); i++) {
			QuestionsFileTest questionsFileTest = (QuestionsFileTest) testVector.get(i);

			Element questionsFile = new Element("questionsFile");
			questionsFile = questionsFile.setAttribute("numberOfQuestions", String.valueOf(testVector.size()));

			String itemsFilesPath = WOWStatic.config.Get("itemsfilespath");
			questionsFile = questionsFile.setAttribute("path", test.getCourse().trim() + itemsFilesPath
					+ questionsFileTest.getQuestionsFileName().trim() + testEditorTypeFile);

			questionsFile = questionsFile.setAttribute("concept", questionsFileTest.getConcept());

			Vector codeQuestionVector = questionsFileTest.getCodeQuestions();

			// Sets the "numberOfQuestions" attribute to the "questionFile" node
			// that indicates the number of questions used from that file
			questionsFile = questionsFile.setAttribute("numberOfQuestions", String.valueOf(codeQuestionVector
					.size()));

			for (int j = 0; j < codeQuestionVector.size(); j++) {
				Element question = new Element("question");
				question = question.setAttribute("codeQuestion", codeQuestionVector.get(j).toString().trim());

				questionsFile = questionsFile.addContent(question);

				totalQuestion++;

				// Updates the questions of the question file setting the
				// test file that they will belongs to.
				// It depends on the type of execution of the test
				QuestionFile questionFile = new QuestionFile(test.getCourse(), questionsFileTest.getQuestionsFileName());
				if (test.getExecutionType().equals(TestEditor.CLASSIC)) {
					questionFile.setClassicTestFileName(codeQuestionVector.get(j).toString().trim(), test
							.getTestFileName().trim());
				} else if (test.getExecutionType().equals(TestEditor.ADAPTIVE)) {
					questionFile.setAdaptiveTestFileName(codeQuestionVector.get(j).toString().trim(), test
								.getTestFileName().trim());
				}
			}
			questions = questions.addContent(questionsFile);
		}

		// Creates the "parameters" node
		Element parameters = new Element("parameters");

		// Creates the "runningParameters" node
		Element runningParameters = new Element("runningParameters");

		// Creates the "presentationParameters" node.
		Element presentationParameters = new Element("presentationParameters");

		// Creates the "showInitialInfo" node.
		Element showInitialInfo = new Element("showInitialInfo");
		showInitialInfo = showInitialInfo.setAttribute("value", String.valueOf(test.getShowInitialInfo()));

		// Creates the "questionsOrder" node
		Element questionsOrder = new Element("questionsOrder");
		questionsOrder = questionsOrder.setAttribute("value", test.getQuestionsOrder());

		// Creates the "answersOrder" node
		Element answersOrder = new Element("answersOrder");
		answersOrder = answersOrder.setAttribute("value", test.getAnswersOrder());

		// Creates the "showQuestionCorrection" node
		Element showQuestionCorrection = new Element("showQuestionCorrection");
		showQuestionCorrection = showQuestionCorrection.setAttribute("value", String.valueOf(test
				.getShowQuestionCorrection()));

		// Creates the "verbose" node.
		Element verbose = new Element("verbose");
		verbose = verbose.setAttribute("value", String.valueOf(test.getVerbose()));

		// Creates the "showCorrectAnswers" node
		Element showCorrectAnswers = new Element("showCorrectAnswers");
		showCorrectAnswers = showCorrectAnswers.setAttribute("value", String.valueOf(test
				.getShowCorrectAnswers()));

		// Creates the "timeOfAnswer" node
		Element timeOfAnswer = new Element("timeOfAnswer");
		timeOfAnswer = timeOfAnswer.setAttribute("value", test.getTimeOfAnswer());

		// Creates the "repeatWithoutAnswer" node
		Element repeatWithoutAnswer = new Element("repeatWithoutAnswer");
		repeatWithoutAnswer = repeatWithoutAnswer.setAttribute("value", String.valueOf(test
				.getRepeatWithoutAnswer()));

		// Creates the "showFinalInfo" node
		Element showFinalInfo = new Element("showFinalInfo");
		showFinalInfo = showFinalInfo.setAttribute("value", String.valueOf(test.getShowFinalInfo()));

		// Adds the "showInitialInfo", "questionsOrder", "answersOrder",
		// "verbose", "showCorrection", "timeOfAnswer" and "showFinalInfo" nodes
		// to the "presentationParameters" parent node.
		presentationParameters = presentationParameters.addContent(showInitialInfo);
		presentationParameters = presentationParameters.addContent(questionsOrder);
		presentationParameters = presentationParameters.addContent(answersOrder);
		presentationParameters = presentationParameters.addContent(showQuestionCorrection);
		presentationParameters = presentationParameters.addContent(verbose);
		presentationParameters = presentationParameters.addContent(showCorrectAnswers);
		presentationParameters = presentationParameters.addContent(timeOfAnswer);
		presentationParameters = presentationParameters.addContent(repeatWithoutAnswer);
		presentationParameters = presentationParameters.addContent(showFinalInfo);

		// Creates the "htmlParameters" node
		Element htmlParameters = new Element("htmlParameters");

		// Creates the "bgColor" node
		Element bgColor = new Element("bgColor");
		bgColor = bgColor.setAttribute("value", test.getBgColor());

		// Creates the "background" node
		Element background = new Element("background");
		if (test.getBackgroundType().trim().equals("") == false) {
			background = background.setAttribute("value", test.getCourse() + imagesTestPath
					+ test.getTestFileName() + test.getBackgroundType().toLowerCase());
		} else {
			background = background.setAttribute("value", "");
		}

		// Updates the background value to the Test object.
		// This action allows using this value for another methods
		test.setBackground(background.getAttribute("value").getValue().trim());
		test.setBackgroundType(test.getBackgroundType().toLowerCase());

		// Creates the "titleColor" node
		Element titleColor = new Element("titleColor");
		titleColor = titleColor.setAttribute("value", test.getTitleColor());

		// Adds the "bgColor","background" and "titleColor" nodes
		// to the "htmlParameters" parent node
		htmlParameters = htmlParameters.addContent(bgColor);
		htmlParameters = htmlParameters.addContent(background);
		htmlParameters = htmlParameters.addContent(titleColor);

		// Creates the "evaluationParameters" node
		Element evaluationParameters = new Element("evaluationParameters");

		// Creates the "incorrectAnswer" node
		Element incorrectAnswer = new Element("incorrectAnswer");
		incorrectAnswer = incorrectAnswer.setAttribute("value", String.valueOf(test
				.getIncorrectAnswersPenalizeNumber()));
		incorrectAnswer = incorrectAnswer.setAttribute("penalize", String.valueOf(test
				.getIncorrectAnswersPenalize()));

		// Creates the "withoutAnswer" node
		Element withoutAnswer = new Element("withoutAnswer");
		withoutAnswer = withoutAnswer.setAttribute("value", String.valueOf(test
				.getWithoutAnswersPenalizeNumber()));
		withoutAnswer = withoutAnswer
				.setAttribute("penalize", String.valueOf(test.getWithoutAnswersPenalize()));

		// Creates the "knowledgePercentage" node
		Element knowledgePercentageElement = new Element("knowledgePercentage");
		knowledgePercentageElement = knowledgePercentageElement.setAttribute("value", test
				.getKnowledgePercentage());

		// Adds the "incorrectAnswer", "withoutAnswer" and
		// "knowledgePercentage" nodes to the "evaluationParameters" parent node.
		evaluationParameters = evaluationParameters.addContent(incorrectAnswer);
		evaluationParameters = evaluationParameters.addContent(withoutAnswer);
		evaluationParameters = evaluationParameters.addContent(knowledgePercentageElement);

		// Adds the "presentationParameters", "htmlParameters" and
		// "evaluationParameters" nodes to the "runningParameters" parent node
		runningParameters = runningParameters.addContent(presentationParameters);
		runningParameters = runningParameters.addContent(htmlParameters);
		runningParameters = runningParameters.addContent(evaluationParameters);

		parameters = parameters.addContent(runningParameters);

		// Creates the "irtParameters" node if needed
		if (test.getExecutionType().equals(TestEditor.ADAPTIVE)) {
			Element irtParameters = new Element("irtParameters");

			// Creates the "start" node
			Element start = new Element("start");
			start = start.setAttribute("initialProficiency", String.valueOf(test.getIrtInitialProficiency()));

			// Creates the "continuation" node
			Element continuation = new Element("continuation");
			continuation = continuation.setAttribute("irtModel", String.valueOf(test.getIrtModel()));

			// Creates the "stop" element.
			Element stop = new Element("stop");
			stop = stop.setAttribute("value", test.getIrtStopCriterion());
			if (test.getIrtStopCriterion().equals("standardError"))
				stop = stop.setAttribute("standardError", String.valueOf(test.getIrtStandardError()));
			else {
				if (test.getIrtStopCriterion().equals("numberItemsAdministred"))
					stop = stop.setAttribute("numberItemsAdministred", String.valueOf(test
							.getIrtNumberItemsAdministred()));
			}

			// Adds this nodes to the "irtParameters" node
			irtParameters = irtParameters.addContent(start);
			irtParameters = irtParameters.addContent(continuation);
			irtParameters = irtParameters.addContent(stop);
			parameters = parameters.addContent(irtParameters);
		}

		// Creates the "student" node and the "student" nodes if needed
		Element students = null;
		Vector studentTestVector = test.getStudentVector();
		if (studentTestVector != null && studentTestVector.isEmpty() == false) {
			students = new Element("students");
			for (int i = 0; i < studentTestVector.size(); i++) {
				StudentTest studentTest = (StudentTest) studentTestVector.get(i);

				Element student = new Element("student");

				// Assigns values to the "student" node
				student = student.setAttribute("login", studentTest.getLogin());
				student = student.setAttribute("make", String.valueOf(studentTest.getMake()));
				student = student.setAttribute("canRepeat", String.valueOf(studentTest.getCanRepeat()));
				student = student.setAttribute("score", String.valueOf(studentTest.getLastScore()));
				student = student.setAttribute("finish", String.valueOf(studentTest.getFinish()));
				if (studentTest.getTheta() != 99999)
					student = student.setAttribute("theta", String.valueOf(studentTest.getTheta()));

				if (studentTest.getStandardError() != 99999)
					student = student
							.setAttribute("standardError", String.valueOf(studentTest.getStandardError()));

				// Adds the "student" node to the "students" parent node
				students = students.addContent(student);

				if (studentTest.getFinish() == true) {
					// Sets the test as finished in the student log file
					StudentFile studentFile = new StudentFile(test.getCourse(), studentTest.getLogin());
					studentFile.endTest(test.getTestFileName(), test.getExecutionType());
				}
			}
		}

		// Adds the "questions", "concepts" and "parameters" nodes to the root node
		elementTestFile = elementTestFile.addContent(concepts);
		elementTestFile = elementTestFile.addContent(questions);
		elementTestFile = elementTestFile.addContent(parameters);

		if (students != null)
			elementTestFile = elementTestFile.addContent(students);

		elementTestFile.getAttribute("totalQuestion").setValue(String.valueOf(totalQuestion));

		// Saves the new created test file
		try {
			if (!createDirectory())
				return false;

			FileWriter xmlFileWriter = new FileWriter(new File(pathToFile));
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
			out.output(xmlDocument, xmlFileWriter);
			xmlFileWriter.close();

			// Adds the name of the created file to the list of files
			// It depends on the type of test
			boolean create = false;
			FileList fileList = new FileList(test.getCourse());
 			if (test.getExecutionType().equals(TestEditor.CLASSIC)) {
				create = fileList.addClassicTestFile(test.getTestFileName(), authorName);
 			} else if (test.getExecutionType().equals(TestEditor.ADAPTIVE)) {
				create = fileList.addAdaptiveTestFile(test.getTestFileName(), authorName);
			}

			return create;

		} catch (IOException e) {
			UtilLog.writeException(e);
			return false;
		}
	}


	/**
	* Copies an existing background image to sets it as the associated image to the test.
	* @param pathBackgroundOld Relative path to the source background image file.
	* @param pathBackgroundNew Relative path to the destiny background image file
	* @return true if the operation finish correctly, false otherwise.
	*/
	public static boolean copyBackground(final String pathBackgroundOld, final String pathBackgroundNew) {
		try {
			// Reads the source file
			File backgroundFileIn = new File(itemsPath + pathBackgroundOld);

			// Obtains the bytes of the source image
			byte [] data = new byte[(int) backgroundFileIn.length()];
			BufferedInputStream input = new BufferedInputStream(new FileInputStream(itemsPath + pathBackgroundOld));
			DataInputStream dataInputStream = new DataInputStream(input);
			dataInputStream.read(data);

			// Creates the destiny folder if needed
			File backgroundDirectoryOut = null;
			if (pathBackgroundNew.lastIndexOf("/") != -1)
				backgroundDirectoryOut = new File(itemsPath
						+ pathBackgroundNew.substring(0, pathBackgroundNew.lastIndexOf("/")));
			else if (pathBackgroundNew.lastIndexOf("\\") != -1)
				backgroundDirectoryOut = new File(itemsPath
						+ pathBackgroundNew.substring(0, pathBackgroundNew.lastIndexOf("\\")));
			else
				return false;

			if (backgroundDirectoryOut.exists() == false) {
				boolean createDirectory = backgroundDirectoryOut.mkdirs();
				if (createDirectory == false)
					return false;
			}

			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(itemsPath + pathBackgroundNew));
			DataOutputStream dataOutputStream = new DataOutputStream(out);
			dataOutputStream.write(data);
			return true;

		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	}

	/**
	  * Deletes the references of the questions that take part in the question file
	  * that will be removed from the test files where it appears. 
	  * @param questionsFileName Name of the question file
	  * @param testVector Relative path to the test files that contains that question 
	  * @param authorName Login of the author
	  * @return an Empty String (not null!) or an warning String if any error occurs. 
	  */
	 public String deleteQuestionFileInTestFiles(
			 final String questionsFileName,
			 final Vector testVector,
			 final String authorName) {
	//{
		String resultDelete = ""; // This String is the result of the method
		// Loops over the test file names Vector
		for (int i = 0; i < testVector.size(); i++) {
			String testFilePath = testVector.get(i).toString();
			String theTestFileName = testFilePath.substring(testFilePath.lastIndexOf("/") + 1, testFilePath
					.lastIndexOf(testEditorTypeFile));
			try {
				// Loads the test file
				String thePath = itemsPath + courseName + testPath + theTestFileName + testEditorTypeFile;
				File testFile = new File(thePath);

				if (!testFile.exists()) {
					// This test file doesn't exist. It cannot be removed  
					resultDelete = resultDelete.concat("\n" + theTestFileName);
					continue; // Next 'for' iteration for the next test file

				} else {

					// Parses the test file
					Document xmlDocument =  new SAXBuilder().build(testFile);
					Element elementTestFile = xmlDocument.getRootElement();
	
					// Reads the test type and some attributes more
					String executionType = elementTestFile.getAttribute("execution").getValue().trim();
					Element elementQuestions = elementTestFile.getChild("questions");
					List questionsFileList = elementQuestions.getChildren("questionsFile");
	
					if (questionsFileList.isEmpty() == false) {
						String itemsFilesPath = WOWStatic.config.Get("itemsfilespath");
						String pathQuestionsFile = courseName + itemsFilesPath + questionsFileName
								+ testEditorTypeFile;
	
						Element elementQuestionsFile = (Element) XPath.selectSingleNode(xmlDocument,
								"testFile/questions/questionsFile" + "[@path = \"" + pathQuestionsFile + "\"]");
	
						if (elementQuestionsFile != null) {
							// Reads the name of the concept associated to this file
							String conceptString = elementQuestionsFile.getAttribute("concept").getValue().trim();
	
							// Deletes the question file
							elementQuestions.removeContent(elementQuestionsFile);
	
							// Looks for another question files associated to that concept
							// If there are any more files associated to this concept, 
							// then the concept don't have to be removed from the test
							elementQuestionsFile = (Element) XPath.selectSingleNode(xmlDocument,
									"testFile/questions/questionsFile[@concept = \"" + conceptString + "\"]");
	
							if (elementQuestionsFile == null) {
								// Removes the concept from the test
								Element elementConcept = (Element) XPath.selectSingleNode(xmlDocument,
										"testFile/concepts/concept[@value = \"" + conceptString + "\"]");
	
								Element elementConcepts = (Element) elementConcept.getParent();
								elementConcepts.removeContent(elementConcept);
							}
	
							if ((elementQuestions.getChildren()).isEmpty()) {
								// If the test file has no questions, it must be removed.
								// But before we have to take a look at the students and
								// remove the references of this test file in the log file of the students

								// Gets the "students" element
								Element students = elementTestFile.getChild("students");
								List studentsList = null;
								if (students != null) {
									studentsList = students.getChildren();
								} else {
									studentsList = null;
								}

								String pathTestFile = elementTestFile.getAttribute("path").getValue().trim();
								if (studentsList != null) {
									// Loops to remove the references of this test file in the
									// student log files
									for (int j = 0; j < studentsList.size(); j++) {
										Element student = (Element) studentsList.get(j);
										String login = student.getAttribute("login").getValue().trim();
										StudentFile studentFile = new StudentFile(courseName, login);
										studentFile.deleteTest(pathTestFile);
									}
								}
								// Deletes the test file, because it has no more question
								testFile = new File(thePath);
								boolean deleteTestFile = testFile.delete();
								if (deleteTestFile == false) {
									testFile.deleteOnExit();
								}

								String pathBackground = elementTestFile.getChild("parameters")
															.getChild("runningParameters")
																.getChild("htmlParameters")
																	.getChild("background")
																			.getAttribute("value").getValue().trim();

								if (pathBackground.equals("") == false) {
									// Deletes the image associated
									QuestionFile.deleteImage(pathBackground);
								}

								// Removes the name of the file from the list of files
								FileList fileList = new FileList(courseName);
								if (executionType.equals(TestEditor.CLASSIC)) {
									fileList.deleteClassicTestFile(theTestFileName, authorName);
								} else {
									fileList.deleteAdaptiveTestFile(theTestFileName, authorName);
								}

							} else {
								// The test still has some more questions
								// Updates the number of question files that takes part in the test
								String numberOfFiles = String.valueOf(elementQuestions.getChildren().size());
								elementQuestions = elementQuestions.setAttribute("numberOfFiles", numberOfFiles);
	
								// Updates the total number of questions that take part in the test
								String totalQuestion = String.valueOf(XPath.selectNodes(xmlDocument,
										"testFile/questions/" + "questionsFile//question").size());
	
								elementTestFile = elementTestFile.setAttribute("totalQuestion", totalQuestion);
	
								// Save the updates of the test file because it won't be removed
								FileWriter xmlFileWriter = new FileWriter(new File(thePath));
								XMLOutputter out = new XMLOutputter();
								out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
								out.output(xmlDocument, xmlFileWriter);
								xmlFileWriter.close();
							}
						}
					}
				}
			} catch (Exception e) {
				resultDelete = resultDelete.concat("\n" + theTestFileName);
				UtilLog.writeException(e);
			}
		} // end of 'for' loop

		if (!resultDelete.trim().equals("")) {
			// Returns a descriptive message of the warning
			resultDelete = "The references of this question have not been \n"
					+ "eliminated in the following files of test: " + resultDelete;
		}

		return resultDelete;

	} // End of deleteQuestionsFileInTestFile
	
	
	
	/**
	 * Delete the test file that has the same name that the one which will be created.
	 * Returns true if the file has been correctly removed, false otherwise
	 * @param deleteBackground Indicates if the background image of the test must be deleted
	 * @param deleteStudents Indicates if the test must be deleted from the log file of the students
	 * @param authorName Login of the author
	 * @return true or false
	 */
	public boolean deleteTestFile(
			final boolean deleteBackground,
			final Vector deleteStudents,
			final String authorName) {
	//{
		// XML Document
		Document xmlDocument;
		try {
			File testFile = new File(pathToFile);
			xmlDocument = new SAXBuilder().build(testFile);
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		// Gets the root element
		Element elementTestFile = xmlDocument.getRootElement();

		// Gets the type of test
		String executionType = elementTestFile.getAttribute("execution").getValue().trim();

		Element questions = elementTestFile.getChild("questions");
		List questionList = questions.getChildren();

		// Loop to delete the references to the old test file that
		// exists in the question files
		for (int i = 0; i < questionList.size(); i++) {
			String questionsFileName = ((Element) questionList.get(i)).getAttribute("path").getValue();
			questionsFileName = questionsFileName.substring(questionsFileName.lastIndexOf("/") + 1,
					questionsFileName.lastIndexOf(testEditorTypeFile));

			QuestionFile questionFile = new QuestionFile(courseName, questionsFileName);
			if (executionType.trim().equals(TestEditor.CLASSIC)) {
				questionFile.deleteClassicTest(testFileName);
			} else {
				questionFile.deleteAdaptiveTest(testFileName);
			}
		}

		// Gets the "students" element
		Element students = elementTestFile.getChild("students");
		List studentsList = null;
		if (students != null) {
			studentsList = students.getChildren();
		} else {
			studentsList = null;
		}

		String pathTestFile = elementTestFile.getAttribute("path").getValue().trim();

		// Loops to delete the references to the old test file that
		// exists in the log file of the students
		if (studentsList != null) {
			if (deleteStudents != null && deleteStudents.isEmpty() == false) {
				for (int i = 0; i < studentsList.size(); i++) {
					Element student = (Element) studentsList.get(i);
					String login = student.getAttribute("login").getValue().trim();

					try {
						if (Boolean.valueOf(deleteStudents.get(i).toString().trim()).booleanValue() == true) {
							StudentFile studentFile = new StudentFile(courseName, login);
							studentFile.deleteTest(pathTestFile);
						}
					} catch (Exception e) {
						StudentFile studentFile = new StudentFile(courseName, login);
						studentFile.deleteTest(pathTestFile);
					}
				}
			} else {
				for (int i = 0; i < studentsList.size(); i++) {
					Element student = (Element) studentsList.get(i);
					String login = student.getAttribute("login").getValue().trim();
					StudentFile studentFile = new StudentFile(courseName, login);
					studentFile.deleteTest(pathTestFile);
				}
			}
		}

		File testFile = new File(pathToFile);
		boolean fileTest = testFile.delete();
		if (fileTest == false)
			testFile.deleteOnExit();

		// Deletes the name of the file from the file list
		FileList fileList = new FileList(courseName);
		if (executionType.equals(TestEditor.CLASSIC)) {
			fileList.deleteClassicTestFile(testFileName, authorName);
		} else {
			fileList.deleteAdaptiveTestFile(testFileName, authorName);
		}

		// Deletes the background image if needed
		if (deleteBackground) {
			deleteBackgroundTest(elementTestFile.getChild("parameters").getChild("runningParameters").getChild(
				"htmlParameters").getChild("background").getAttribute("value").getValue().trim());
		}

		return fileTest;
	}

	/**
	  * Analyses a test file and returns a Test object with its data
	  * @return An object of type Test
	  */
	 public Test getTest() {
		try {
			File testFile = new File(pathToFile);
			Document xmlDocument = new SAXBuilder().build(testFile);

			// Gets the root element
			Element elementTestFile = xmlDocument.getRootElement();

			// Sets the course name, the name of the file and the name of the test
			Test test = new Test();
			test.setCourse(courseName);
			test.setLastUse(elementTestFile.getAttribute("lastUse").getValue().trim());
			test.setTestType(elementTestFile.getAttribute("type").getValue().trim());
			test.setTestFileName(testFileName);
			test.setTestName(elementTestFile.getAttribute("name").getValue());
			test.setTotalQuestion(Integer.valueOf(elementTestFile.getAttribute("totalQuestion").getValue())
					.intValue());
			test.setTestType(elementTestFile.getAttribute("type").getValue().trim());
			test.setExecutionType(elementTestFile.getAttribute("execution").getValue().trim());
			test.setPath(elementTestFile.getAttribute("path").getValue().trim());

			if (elementTestFile.getAttribute("enable").getValue().trim().equals("true"))
				test.setEnable(true);
			else
				test.setEnable(false);

			// Gets the "concepts" element
			Element concepts = elementTestFile.getChild("concepts");
			if (concepts.getAttribute("abstractConcept") != null)
				test.setAbstractConcept(concepts.getAttribute("abstractConcept").getValue().trim());

			Vector conceptVector = new Vector();
			List conceptList = concepts.getChildren("concept");

			if (conceptList.isEmpty())
				conceptVector = null;
			else {
				for (int i = 0; i < conceptList.size(); i++) {
					Element concept = (Element) conceptList.get(i);
					conceptVector.add(concept.getAttribute("value").getValue().trim());
				}
			}

			test.setConceptVector(conceptVector);

			// Gets the "questions" element
			Element questions = elementTestFile.getChild("questions");

			// Reads the names of the question files and the codes of the questions of them
			// that take part in the test
			List questionsFileList = questions.getChildren();

			if (questionsFileList.isEmpty()) {
				UtilLog.toLog("Questions file list is empty!");
				return null;
			}

			// Creates the testVector element
			Vector testVector = new Vector();

			// Loop to insert data in testVector.
			for (int i = 0; i < questionsFileList.size(); i++) {
				QuestionsFileTest questionsFileTest = new QuestionsFileTest();

				// Reads the name of the question file
				questionsFileTest
						.setQuestionsFileName(((Element) questionsFileList.get(i)).getAttribute("path").getValue()
								.substring(
										((Element) questionsFileList.get(i)).getAttribute("path").getValue()
												.lastIndexOf("/") + 1,
										((Element) questionsFileList.get(i)).getAttribute("path").getValue()
												.lastIndexOf(testEditorTypeFile)));

				// Reads the associated concept to the question file
				questionsFileTest.setConcept(((Element) questionsFileList.get(i)).getAttribute("concept")
						.getValue().trim());

				List childrenQuestionsFile = ((Element) questionsFileList.get(i)).getChildren();

				// Loop to obtain the code of the questions of the question file
				Vector codeQuestionVector = new Vector();
				for (int j = 0; j < childrenQuestionsFile.size(); j++) {
					codeQuestionVector.add(((Element) childrenQuestionsFile.get(j)).getAttribute("codeQuestion")
							.getValue());
				}

				questionsFileTest.setCodeQuestions(codeQuestionVector);
				testVector.add(questionsFileTest);
			}

			// Adds the testVector to the test object
			test.setTestVector(testVector);

			// Reads the parameters of the test

			// Gets the "parameters" element
			Element parameters = elementTestFile.getChild("parameters");

			// Gets the "runningParameters" element
			Element runningParameters = parameters.getChild("runningParameters");

			// Gets the "presentationParameters" element
			Element presentationParameters = runningParameters.getChild("presentationParameters");

			// Reads the information stored in the child nodes of the
			// "presentationParameters" element
			Element showInitialInfo = presentationParameters.getChild("showInitialInfo");
			if (showInitialInfo.getAttribute("value").getValue().trim().equals("true"))
				test.setShowInitialInfo(true);
			else
				test.setShowInitialInfo(false);

			Element questionsOrder = presentationParameters.getChild("questionsOrder");
			test.setQuestionsOrder(questionsOrder.getAttribute("value").getValue().trim());

			Element answersOrder = presentationParameters.getChild("answersOrder");
			test.setAnswersOrder(answersOrder.getAttribute("value").getValue().trim());

			Element showQuestionCorrection = presentationParameters.getChild("showQuestionCorrection");

			if (showQuestionCorrection.getAttribute("value").getValue().trim().equals("true"))
				test.setShowQuestionCorrection(true);
			else
				test.setShowQuestionCorrection(false);

			Element verbose = presentationParameters.getChild("verbose");
			if (verbose.getAttribute("value").getValue().trim().equals("true"))
				test.setVerbose(true);
			else
				test.setVerbose(false);

			Element showCorrectAnswers = presentationParameters.getChild("showCorrectAnswers");
			if (showCorrectAnswers.getAttribute("value").getValue().trim().equals("true"))
				test.setShowCorrectAnswers(true);
			else
				test.setShowCorrectAnswers(false);

			Element timeOfAnswer = presentationParameters.getChild("timeOfAnswer");
			test.setTimeOfAnswer(timeOfAnswer.getAttribute("value").getValue().trim());

			Element repeatWithoutAnswer = presentationParameters.getChild("repeatWithoutAnswer");
			test.setRepeatWithoutAnswer(Boolean.valueOf(
					repeatWithoutAnswer.getAttribute("value").getValue().trim()).booleanValue());

			Element showFinalInfo = presentationParameters.getChild("showFinalInfo");

			if (showFinalInfo.getAttribute("value").getValue().trim().equals("true"))
				test.setShowFinalInfo(true);
			else
				test.setShowFinalInfo(false);

			// Gets the "htmlParameters" element
			Element htmlParameters = runningParameters.getChild("htmlParameters");

			// Reads the information stored in the child nodes of the
			// "htmlParameters" element
			Element bgColor = htmlParameters.getChild("bgColor");

			test.setBgColor(bgColor.getAttribute("value").getValue().trim());

			Element background = htmlParameters.getChild("background");

			test.setBackground(background.getAttribute("value").getValue().trim());
			if (test.getBackground().equals("") == false)
				test.setBackgroundType(test.getBackground().substring(test.getBackground().lastIndexOf("."))
						.toLowerCase());
			else
				test.setBackgroundType("");

			Element titleColor = htmlParameters.getChild("titleColor");

			test.setTitleColor(titleColor.getAttribute("value").getValue().trim());

			// Gets the "evaluationParameters" element
			Element evaluationParameters = runningParameters.getChild("evaluationParameters");

			// Reads the information stored in the child nodes of the
			// "evaluationParameters" element
			Element incorrectAnswer = evaluationParameters.getChild("incorrectAnswer");

			test.setIncorrectAnswersPenalizeNumber(Integer.valueOf(
					incorrectAnswer.getAttribute("value").getValue().trim()).intValue());

			if (incorrectAnswer.getAttribute("penalize").getValue().trim().equals("true"))
				test.setIncorrectAnswersPenalize(true);
			else
				test.setIncorrectAnswersPenalize(false);

			Element withoutAnswer = evaluationParameters.getChild("withoutAnswer");
			test.setWithoutAnswersPenalizeNumber(Integer.valueOf(
					withoutAnswer.getAttribute("value").getValue().trim()).intValue());

			if (withoutAnswer.getAttribute("penalize").getValue().trim().equals("true"))
				test.setWithoutAnswersPenalize(true);
			else
				test.setWithoutAnswersPenalize(false);

			Element knowledgePercentage = evaluationParameters.getChild("knowledgePercentage");
			test.setKnowledgePercentage(knowledgePercentage.getAttribute("value").getValue().trim());

			// Reads the adaptive running parameters if exist
			if (elementTestFile.getAttribute("execution").getValue().trim().equals(TestEditor.ADAPTIVE)) {
				if (parameters.getChild("irtParameters") != null) {
					Element irtParameters = parameters.getChild("irtParameters");

					// Reads the "start", "continuation" and "stop" nodes
					Element start = irtParameters.getChild("start");
					Element continuation = irtParameters.getChild("continuation");
					Element stop = irtParameters.getChild("stop");

					if (start != null && continuation != null && stop != null) {
						test.setIrtInitialProficiency(Double.valueOf(
								start.getAttribute("initialProficiency").getValue().trim()).doubleValue());

						test.setIrtModel(Integer.valueOf(continuation.getAttribute("irtModel").getValue().trim())
								.intValue());

						test.setIrtStopCriterion(stop.getAttribute("value").getValue().trim());
						if (test.getIrtStopCriterion().equals("standardError"))
							test.setIrtStandardError(Double.valueOf(
									stop.getAttribute("standardError").getValue().trim()).doubleValue());
						else {
							if (test.getIrtStopCriterion().equals("numberItemsAdministred"))
								test.setIrtNumberItemsAdministred(Integer.valueOf(
										stop.getAttribute("numberItemsAdministred").getValue().trim()).intValue());
						}
					}
				}
			}

			// Gets the "students" element
			Element students = elementTestFile.getChild("students");

			if (students != null) {
				List studentList = students.getChildren();

				// Loop to gets the data stored in the "student" nodes
				Vector studentVector = new Vector();

				for (int i = 0; i < studentList.size(); i++) {
					StudentTest studentTest = new StudentTest();

					Element student = (Element) studentList.get(i);
					studentTest.setLogin(student.getAttribute("login").getValue().trim());
					studentTest
							.setMake(Integer.valueOf(student.getAttribute("make").getValue().trim()).intValue());

					if (student.getAttribute("canRepeat").getValue().trim().equals("true"))
						studentTest.setCanRepeat(true);
					else
						studentTest.setCanRepeat(false);

					studentTest.setLastScore(Double.valueOf(student.getAttribute("score").getValue())
							.doubleValue());

					studentTest.setFinish(Boolean.valueOf(student.getAttribute("finish").getValue().trim())
							.booleanValue());

					if (student.getAttribute("theta") != null)
						studentTest.setTheta(Double.valueOf(student.getAttribute("theta").getValue().trim())
								.doubleValue());
					else
						studentTest.setTheta(99999);

					if (student.getAttribute("standardError") != null)
						studentTest.setStandardError(Double.valueOf(
								student.getAttribute("standardError").getValue().trim()).doubleValue());
					else
						studentTest.setStandardError(99999);

					// Adds the StudentTest object to the studentVector vector
					studentVector.add(studentTest);
				}

				// Adds the studentVector vector to the test object
				test.setStudentVector(studentVector);

			} else {
				test.setStudentVector(null);
			}

			return test;

		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}
	}

	/**
	  * Analyses a student log file and returns the information of the file
	  * in a StudentTest object
	  * @param login Login of the student to analyse
	  * @return a Student log Object
	  */
	 public StudentTest getStudentTest(final String login) {
	 //{
		try {
			File testFile = new File(pathToFile);
			Document xmlDocument = new SAXBuilder().build(testFile);

			Element student = (Element) XPath.selectSingleNode(xmlDocument, "testFile/students"
					+ "/student[@login = \"" + login + "\"]");

			if (student != null) {
				StudentTest studentTest = new StudentTest();
				studentTest.setLogin(student.getAttribute("login").getValue().trim());
				studentTest.setMake(Integer.valueOf(student.getAttribute("make").getValue().trim()).intValue());
				if (student.getAttribute("canRepeat").getValue().trim().equals("true"))
					studentTest.setCanRepeat(true);
				else
					studentTest.setCanRepeat(false);

				studentTest.setLastScore(Double.valueOf(student.getAttribute("score").getValue().trim())
						.doubleValue());

				if (student.getAttribute("finish").getValue().trim().equals("true"))
					studentTest.setFinish(true);
				else
					studentTest.setFinish(false);

				if (student.getAttribute("theta") != null)
					studentTest.setTheta(Double.valueOf(student.getAttribute("theta").getValue().trim())
							.doubleValue());
				else
					studentTest.setTheta(99999);

				if (student.getAttribute("standardError") != null)
					studentTest.setStandardError(Double.valueOf(
							student.getAttribute("standardError").getValue().trim()).doubleValue());
				else
					studentTest.setStandardError(99999);

				return studentTest;

			} else {
				// The student doesn't exists in the test file
				return null;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}
	 }

	
	/**
	 * Adds a child node of "student" type to the test file.
	 * This method must be static and synchronized!!
	 * @param aCourseName Name of the course that the test belongs to.
	 * @param aTestFileName Name of the test file.
	 * @param studentTest Data of the student with the necessary information to add the "student"
	 *           node to the test file.
	 * @return boolean false if any trouble exists, true otherwise.
	 */
	public static synchronized final boolean addStudentToTest(
			final String aCourseName,
			final String aTestFileName,
			final StudentTest studentTest) {
	//{
		try {
			// Loads the test file
			File file = new File(itemsPath + aCourseName + testPath + aTestFileName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(file);

			// Obtains the root element
			Element elementTestFile = xmlDocument.getRootElement();

			// Obtains the "students" element or create it in case not exists
			Element students = elementTestFile.getChild("students");
			Element student = null;

			if (students == null) {
				// Creates the "students" element
				students = new Element("students");
				elementTestFile = elementTestFile.addContent(students);
			} else {
				// Looks for a "student" element similar to the one we will create
				student = (Element) XPath.selectSingleNode(xmlDocument, "testFile/students"
						+ "/student[@login = \"" + studentTest.getLogin() + "\"]");
			}

			// Checks the "student" element
			if (student == null) {
				// Creates the new "student" element
				student = new Element("student");

				student = student.setAttribute("login", studentTest.getLogin());
				student = student.setAttribute("make", "1");

				// If the type of test is activity the student can repeat it
				if (elementTestFile.getAttribute("type").getValue().trim().equals("activity"))
					student = student.setAttribute("canRepeat", "true");
				else
					student = student.setAttribute("canRepeat", "false");

				student = student.setAttribute("score", String.valueOf(studentTest.getLastScore()));
				student = student.setAttribute("finish", "false");

				if (studentTest.getTheta() != 99999)
					student = student.setAttribute("theta", String.valueOf(studentTest.getTheta()));

				if (studentTest.getStandardError() != 99999)
					student = student
							.setAttribute("standardError", String.valueOf(studentTest.getStandardError()));

				// Adds the "student" element to their "students" parent
				students = students.addContent(student);
			} else {
				student.getAttribute("make").setValue(
						String.valueOf(Integer.valueOf(student.getAttribute("make").getValue()).intValue() + 1));

				// If the type of test is activity the student can repeat it
				if (elementTestFile.getAttribute("type").getValue().trim().equals("activity"))
					student.getAttribute("canRepeat").setValue("true");
				else
					student.getAttribute("canRepeat").setValue("false");

				student.getAttribute("score").setValue(String.valueOf(studentTest.getLastScore()));
				student.getAttribute("finish").setValue("false");

				if (studentTest.getTheta() != 99999)
					student = student.setAttribute("theta", String.valueOf(studentTest.getTheta()));

				if (studentTest.getStandardError() != 99999)
					student = student
							.setAttribute("standardError", String.valueOf(studentTest.getStandardError()));
			}

			// Updates the "lastUse" attribute of the root element
			Calendar calendar = Calendar.getInstance();
			elementTestFile.getAttribute("lastUse").setValue(
					calendar.get(Calendar.DAY_OF_MONTH) + "/" + String.valueOf(calendar.get(Calendar.MONTH) + 1)
							+ "/" + calendar.get(Calendar.YEAR));

			// Save the updates.
			file = new File(itemsPath + aCourseName + testPath + aTestFileName + testEditorTypeFile);
			FileWriter xmlFileWriter = new FileWriter(file);
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
			out.output(xmlDocument, xmlFileWriter);
			xmlFileWriter.close();

		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		return true;

	}

	 
	/**
	 * Analyses a test file and sets it to finished (change its "finish" attribute to true) to the
	 * student with the login passed as parameter. Also updates his score, theta and standarError
	 * attributes.
	 * This method must be static and synchronized!!
	 * @param aCourseName Name of the course that the test belongs to.
	 * @param aTestFileName Name of the test file.
	 * @param login Student's login.
	 * @param score Final score of the student in the test.
	 * @param theta Knowledge of the student in the test or 99999 in case it is not needed
	 * @param standardError Standar error taken to calculate the estimated knowledge of the student.
	 * @return A boolean value indicating the success of the operation
	 */ 
	public static synchronized final boolean endStudentTest(
		final String aCourseName,
		final String aTestFileName,
		final String login,
		final double score,
		final double theta,
		final double standardError) {
	//{
		try {
			File testFile = new File(itemsPath + aCourseName + testPath + aTestFileName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(testFile);

			// Select the student node
			Element student = (Element) XPath.selectSingleNode(xmlDocument,
					"testFile/students//student[@login = \"" + login + "\"]");

			if (student != null) {
				// Updates the value of the attributes
				student.getAttribute("score").setValue(String.valueOf(score));
				student.getAttribute("finish").setValue("true");

				if (theta != 99999 && student.getAttribute("theta") != null)
					student.getAttribute("theta").setValue(String.valueOf(theta));

				if (standardError != 99999 && student.getAttribute("standardError") != null)
					student.getAttribute("standardError").setValue(String.valueOf(standardError));

				// Save the values
				File xmlFile = new File(itemsPath + aCourseName + testPath + aTestFileName + testEditorTypeFile);
				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
				xmlFileWriter.close();

			} else {
				UtilLog.toLog("The student " + login + " doesn't exist in the test");
				return false; // The student doesn't exist in the test
			}	
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}

		return true;
	}

	 /**
	  * Analyses the folders of the courses received as parameter and returns a Vector
	  * object that contains several Course objects with the names of the courses
	  * and the tests 
	  * @param courseNameVector Vector with course names
	  * @param testType Type of test to obtain
	  * @return A Vector object with Course objects or null
	  */
	 public static Vector getTestsFileNames(final Vector courseNameVector, final String testType) {
		Vector courseVector = new Vector();
		// Loops to obtain the data
		if (testType.trim().equals(TestEditor.CLASSIC)) {
			// Classic test
			for (int i = 0; i < courseNameVector.size(); i++) {
				String courseName = courseNameVector.get(i).toString();
				Course course = new Course();
				course.setName(courseName);
				FileList fileList = new FileList(courseName);
				course.setTestsFileNames(fileList.getClassicTestFileNames());
				courseVector.add(course);
			}
		} else if (testType.trim().equals(TestEditor.ADAPTIVE)) {
			// Adaptive test
			for (int i = 0; i < courseNameVector.size(); i++) {
				String courseName = courseNameVector.get(i).toString();
				Course course = new Course();
				course.setName(courseName);
				FileList fileList = new FileList(courseName);
				course.setTestsFileNames(fileList.getAdaptiveTestFileNames());
				courseVector.add(course);
			}
		}

		if (courseVector.isEmpty()) {
			return null;
		} else {
			return courseVector;
		}
	 }
	
	
	/**
	 * Updates the value of the score and knowledge of the student
	 * in the test file. Return false if any error occurs, true otherwise
	 * @param login Login of the student that makes the test
	 * @param score New score of the student in the test
	 * @param theta Knowledge of the student
	 * @param standardError Standard error
	 * @return true if the process goes OK, false if any error occurs
	 */
	public boolean updateStudentTest(
		final String login,
		final double score,
		final double theta,
		final double standardError) {
	//{
		try {
			File testFile = new File(pathToFile);
			Document xmlDocument = new SAXBuilder().build(testFile);

			Element student = (Element) XPath.selectSingleNode(xmlDocument,
					"testFile/students" + "//student[@login = \"" + login + "\"]");

			if (student != null) {
				// Updates the value of the attributes
				student.getAttribute("score").setValue(String.valueOf(score));

				if (theta != 99999 && student.getAttribute("theta") != null) {
					student.getAttribute("theta").setValue(String.valueOf(theta));
				}

				if (standardError != 99999 && student.getAttribute("standardError") != null) {
					student.getAttribute("standardError").setValue(String.valueOf(standardError));
				}

				// Save the values
				// this is commented because the knowledge of the student is not in the test file,
				// but yes in the student log file
				/*
				 * try { File xmlFile = new File(path + courseName + testPath +
				 * testFileName + testEditorTypeFile); FileWriter xmlFileWriter =
				 * new FileWriter(xmlFile);
				 * out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				 * out.output(xmlDocument, xmlFileWriter); xmlFileWriter.close();
				 * 
				 * return true; }catch (IOException e){ return false; }
				 */

				return true;

			} else {
				UtilLog.toLog("updateStudentTest: There is not a student with the login " + login);
				return false;
			}

		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	}

	 
	 /**
	  * Deletes the background image of a test
	  * @param filePath Relative path to the images folder 
	  * @return true or false
	  */
	 public static boolean deleteBackgroundTest(final String filePath) {
		File backgroundFile = new File(itemsPath + filePath);
		if (backgroundFile.exists()) {
			boolean delete = backgroundFile.delete();
			if (!delete) {
				backgroundFile.deleteOnExit();
				return true;
			} else {
				return delete;
			}
		} else {
			return true;
		}
	}

	
//	/**
//	 * Creates a file named TestMovil.jar in the folder of the course 
//	 * which name is received as parameter, including in that jar file
//	 * the test file that are passed as parameter and the items file associated
//	 * to the test.
//	 * @return A string with the route to the JAR file created, null in error case
//	 */
//	public synchronized final String createMobileJar() {
//		try {
//			// We have to locate the first items file of the test.
//			String wowRoot = WOWStatic.config.Get("WOWROOT");
//			Document xmlDocument = new SAXBuilder().build(new File(pathToFile));
//			Element testFile = xmlDocument.getRootElement();
//			Element elementQuestions = testFile.getChild("questions");
//			List questionsFileList = elementQuestions.getChildren();
//			if (questionsFileList.isEmpty()) {
//				return null;
//			}
//
//			// Copy TestMovil.jar file in a new folder in the temp folder
//      	// Creates a new temp folder
//			// This is because we want to preserve the original one
//      	String tempRoute = TempFile.createTempFolder();
//      	File end = new File(tempRoute + "/TestMovil.jar");
//      	BufferedInputStream fis  = new BufferedInputStream(new FileInputStream(new File(wowRoot + "lib/TestMovil.jar")));
//      	BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(end));
//   	   byte [] buf = new byte[2048];
//   	   int i = 0;
//   	   while((i = fis.read(buf)) != -1){
//   	   	fos.write(buf, 0, i);
//   	   }
//   	   fis.close();
//   	   fos.close();
//
//   	   // Creates the '<NAMEOFCOURSE>/itemsFiles' in the temp folder
//			File itemsFolder = new File(tempRoute + "/" + courseName);
//			if (itemsFolder.exists() && itemsFolder.isDirectory()) {
//				TempFile.deleteFolder(itemsFolder);
//			}
//			if (!itemsFolder.mkdir()) {
//				// Directory creation failed
//				return null;
//			}
//			String itemsFolderName = courseName + "/itemsFiles";
//			itemsFolder = new File(tempRoute + "/" + itemsFolderName);
//			if (!itemsFolder.mkdir()) {
//				// Directory creation failed
//				return null;
//			}
//
//			// Copies all the question files to the '<NAMEOFCOURSE>/itemsFiles' temp folder
//			int numberOfQuestionFiles = questionsFileList.size();
//			for (int k = 0; k < numberOfQuestionFiles; k++) {
//				Element questionFile = (Element)questionsFileList.get(k);
//				String itemsFilePath = questionFile.getAttribute("path").getValue();
//
//				// Copy the items file
//				File in = new File(itemsPath + itemsFilePath);
//				String name = in.getName(); // Gets the name of the items file
//				File out = new File(tempRoute + "/" + itemsFolderName + "/" + name);
//				
//				String itemsString = itemsFolderName + "/" + name;
//				fis = new BufferedInputStream(new FileInputStream(in));
//				fos = new BufferedOutputStream(new FileOutputStream(out));
//				buf = new byte[2048];
//				i = 0;
//				while((i = fis.read(buf))!=-1) {
//					fos.write(buf, 0, i);
//				}
//				fis.close();
//				fos.close();
//
//				// Updates the TestMovil.jar file with the question file
//				Runtime r = Runtime.getRuntime();
//	   		String command = "jar uf \"" + tempRoute + "/TestMovil.jar\" " + itemsString;
//	   		
//	   		UtilLog.toLog(command);
//	   		
//	      	Process p = r.exec(command);
//
//	      	// Waits for the process to stop
//	      	p.waitFor();
//			}
//
//			// Creates the 'test' temp folder
//			File testFolder = new File(tempRoute + "/test");
//			if (testFolder.exists() && testFolder.isDirectory()) {
//				TempFile.deleteFolder(testFolder);
//			}
//			if (!testFolder.mkdir()) {
//				// Directory creation failed
//				return null;
//			}
//
//	   	// Copy test file
//			fis  = new BufferedInputStream(new FileInputStream(new File(pathToFile)));
//			fos = new BufferedOutputStream(new FileOutputStream(new File(tempRoute + "/test/config.xml")));
//    		i = 0;
//    		buf = new byte[2048];
//    		while ((i = fis.read(buf))!=-1) {
//      		fos.write(buf, 0, i);
//      	}
//    		fis.close();
//    		fos.close();
//
//    		// Updates TestMovil.jar file
//      	Runtime r = Runtime.getRuntime();
//   		String command = "jar uf \"" + tempRoute + "/TestMovil.jar\" test/config.xml";
//   		
//   		UtilLog.toLog(command);
//   		
//      	Process p = r.exec(command);
//      	
//      	// Waits for the process to stop
//      	p.waitFor();
//
//   	   // Deletes the path to the WOW! application from the route
//   	   return tempRoute.substring(wowRoot.length()) + "/TestMovil.jar";
//
//		} catch (Exception e) {
//			UtilLog.writeException(e);
//			return null;
//		}
//	}

	 
	 /**
	  * 
	  * @param source
	  * @param destiny
	  * @return true if success
	  * @throws Exception
	  */
	 public static boolean copyFile(final File source, final File destiny) throws Exception {	
			boolean result = true;
			// Checks if the end file exists
			if (destiny.exists()) {
				result = destiny.delete();
			}
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source));
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destiny));
			byte [] buf = new byte[2048];
			int i = 0;
			while ((i = bis.read(buf)) != -1) {
				bos.write(buf, 0, i);
			}
			bis.close();
			bos.close();
			return result;
		}
		

		/**
		 * Creates a file named TestMovil.jar in the folder of the course 
		 * which name is received as parameter, including in that jar file
		 * the test file that are passed as parameter and the items file associated
		 * to the test.
		 * @return A String that indicates the location of the JAR file, null in error case.
		 */
		public synchronized String createMobileJar() {
			// Root folder
			String wowRoot = WOWStatic.config.Get("WOWROOT");
			String xmlRoot = WOWStatic.config.Get("XMLROOT");
			String itemsPath = WOWStatic.config.Get("itemspath");
			String testPath = WOWStatic.config.Get("testpath");
			String tempFolderPath = wowRoot + "lib/" + TempFile.getNewId();
			String typeFile = WOWStatic.config.Get("testeditortypefile");

			try {
				// We have to locate all the items file of the test.
				SAXBuilder builder = new SAXBuilder();
				File FileTest = new File(xmlRoot + itemsPath + courseName + testPath + testFileName + typeFile);
				Document xmlDocument = builder.build(FileTest);

				Element testFile = xmlDocument.getRootElement();
				Element elementQuestions = testFile.getChild("questions");
				List questionsFileList = elementQuestions.getChildren();
				if (questionsFileList.isEmpty()) {
					return null;
				}

				// Creates the temp folder path
				boolean success = true;
				File tempFolder = new File(tempFolderPath);
				if (tempFolder.exists()) {
					TempFile.deleteFolder(tempFolder);
				}
				success = tempFolder.mkdir();
				if (!success) {
					// Directory creation failed
					UtilLog.toLog("The temporal folder cannot be created!");
					return null;
				}

				// Creates the 'test' folder in the temp folder
				File f = new File(tempFolderPath + "/test");
				f.mkdir();
				// Creates the itemsFiles folder
				f = new File(tempFolderPath + "/" + courseName + "/" + "itemsFiles");
				f.mkdirs();
				// Creates the images folder
				f = new File(tempFolderPath + "/images/test");
				f.mkdirs();

				// Copy the TestMovil.jar file to the courseName folder
				String libFolderPath = wowRoot + "lib";
				File source = new File(libFolderPath + "/TestMovil.jar");
				File destiny = new File(wowRoot + courseName + "/TestMovil.jar");
				if (!copyFile(source, destiny)) {
					// JAR file copy failed
					UtilLog.toLog("The JAR file cannot be moved!");
					return null;
				}

				// Copy test file to the temp folder
				source = new File(xmlRoot + itemsPath + courseName + testPath + testFileName + typeFile);
				destiny = new File(tempFolderPath + "/test/config.xml");
				if (!copyFile(source, destiny)) {
					// Items file copy failed
					UtilLog.toLog("The test file cannot be moved!");
					return null;
				}

				// Updates TestMovil.jar file with the test file
				Runtime r = Runtime.getRuntime();
				String command = "jar uf \"" + wowRoot + courseName + "/TestMovil.jar\" -C \"" + tempFolderPath + "\" test/config.xml";
				Process p = r.exec(command);
				p.waitFor(); // Wait to finish the JAR command

				// Copy all the items files
				boolean images = false;
				for (int j = 0; j < questionsFileList.size(); j++) {
					Element questionFile = (Element)questionsFileList.get(j);
					String itemsFilePath = questionFile.getAttribute("path").getValue();
					source = new File(xmlRoot + itemsPath + itemsFilePath);
					String thePath = tempFolderPath + "/" + courseName + "/itemsFiles";
					String theName = source.getName();
					destiny = new File(thePath + "/" + theName);
					if (!copyFile(source, destiny)) {
						// Items file copy failed
						UtilLog.toLog("A items file cannot be moved!");
						return null;
					}

					// Updates TestMovil.jar file with the current items file
					command = "jar uf \"" + wowRoot + courseName + "/TestMovil.jar\" -C \"" + tempFolderPath + "\" " + courseName+"/itemsFiles/" + theName;
					p = r.exec(command);
					p.waitFor(); // Wait to finish the JAR command

					// Checks for images of the question file
					itemsFilePath = itemsFilePath.substring(0, itemsFilePath.length() - theName.length());
					theName = theName.substring(0, theName.length() - typeFile.length()); // Withouth the .xml extension
					f = new File(xmlRoot + itemsPath + itemsFilePath + "images/" + theName);
					if (f.exists()) {
						// There are some images for the questions
						// Copies all the images to the 'images/test' folder
						images = true;
						File [] imageList = f.listFiles();
						for (int k = 0; k < imageList.length; k++) {
							source = imageList[k];
							destiny = new File(tempFolderPath + "/images/test/" + source.getName());
							if (!copyFile(source, destiny)) {
								// Images file copy failed
								UtilLog.toLog("A image file cannot be moved!");
							}
						}
					}
				}

				if (images) {
					// Updates the TestMovil.jar with the all the images in the 'images/test' folder
					f = new File(tempFolderPath + "/images/test/");
					File [] imageList = f.listFiles();
					for (int k = 0; k < imageList.length; k++) {
						command = "jar uf \"" + wowRoot + courseName + "/TestMovil.jar\" -C \"" + tempFolderPath + "\" images/test/" + imageList[k].getName();
						p = r.exec(command);
						p.waitFor(); // Wait to finish the JAR command
					}
				}

				// Creates TestMovil.jad file
				// Calculates the size of the TestMovil.jar file
				FileInputStream fis = new FileInputStream(new File(wowRoot + courseName + "/TestMovil.jar"));
				BufferedInputStream bis = new BufferedInputStream(fis);
				byte[] buf = new byte[1024];
				int i = 0;
				int size = 0;
				while ((i = bis.read(buf)) != -1) {
					size += i;
				}
				bis.close();

				String sizeStr = String.valueOf(size);
				File jadFile = new File(wowRoot + courseName + File.separator + "TestMovil.jad");
				if (jadFile.exists()) {
					success = jadFile.delete();
				}
				if (!success) {
					// JAD file delete failed
					UtilLog.toLog("The JAD file cannot be created");
					return null;
				}

				// Writes the JAD file
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(jadFile));
				PrintStream ps = new PrintStream(bos);
				ps.println("");
				ps.println("HolaServlet-URL: http://localhost:8080/servlet/MidpServlet");
				ps.println("MIDlet-1: TestMovil, TestMovil.png, TestMovil");
				ps.println("MIDlet-Icon: \\imagenes\\creditos.png");
				ps.println("MIDlet-Jar-Size: " + sizeStr);
				ps.println("MIDlet-Jar-URL: TestMovil.jar");
				ps.println("MIDlet-Name: TestMovil");
				ps.println("MIDlet-Vendor: Universidad de Cordoba");
				ps.println("MIDlet-Version: 1.0");
				ps.println("MicroEdition-Configuration: CLDC-1.0");
				ps.println("MicroEdition-Profile: MIDP-2.0");
				
				// Closes the file
				ps.close();
				System.gc();

				// Removes the temp folder
				TempFile.deleteFolder(tempFolder);

				String text = courseName + "/TestMovil.jar";

				return text;

			} catch (Exception e) {
				UtilLog.writeException(e);
				return null;
			}
		}

} // End of TestFile class