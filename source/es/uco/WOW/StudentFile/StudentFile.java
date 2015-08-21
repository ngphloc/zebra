package es.uco.WOW.StudentFile;

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

import es.uco.WOW.FileList.FileList;
import es.uco.WOW.TestEditor.TestEditor;
import es.uco.WOW.TestFile.TestFile;
import es.uco.WOW.Utils.AnswersTestLogStudent;
import es.uco.WOW.Utils.EvalTestLogStudent;
import es.uco.WOW.Utils.QuestionsFileTest;
import es.uco.WOW.Utils.Student;
import es.uco.WOW.Utils.StudentTest;
import es.uco.WOW.Utils.TestLogStudent;
import es.uco.WOW.Utils.UtilLog;

/**
 * <p>Title: Wow! TestEditor</p> <p>Description: Herramienta para la edicion
 * de preguntas tipo test adaptativas </p> <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * 
 * @version 1.0
 */


/**
 * NAME: StudentFile
 * FUNCTION: This class manages all the options related to
 * the student log files. This is the files with the result of the test execution
 * by the students.
 * LAST MODIFICATION: 06-02-2008
 *
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class StudentFile {

	/** Name of the root folder where the items and tests are stored */
	private static String itemsPath = WOWStatic.config.Get("XMLROOT") + WOWStatic.config.Get("itemspath");
	/** Name of the folder where the student log files are stored */
	private static String studentLogPath = WOWStatic.config.Get("studentlogpath");
	/** Type of file that the TestEditor tool works with */
	private static String testEditorTypeFile = WOWStatic.config.Get("testeditortypefile");

// Member variables

	/**
	 * Name of the course that this file belongs to
	 */
	private String courseName = null;
	/**
	 * Name of the student file
	 */
	private String studentFileName = null;

// End of member variables

	/**
	 * Public constructor
	 * @param aCourseName Name of the course that this file belongs to
	 * @param aStudentFileName Name of the student file to
	 */
	public StudentFile (final String aCourseName, final String aStudentFileName) {
		this.courseName = aCourseName;
		this.studentFileName = aStudentFileName;
	}

	/**
	 * @return Returns the courseName.
	 */
	public final String getCourseName() {
		return this.courseName;
	}

	/**
	 * @return Returns the studentFileName.
	 */
	public final String getStudentFileName() {
		return this.studentFileName;
	}

	
	/**
	 * Checks if exists a student log file with the same name as received as parameter.
	 * Returns true if exists, false otherwise
	 * @return true if the file exists, false otherwise
	 */
	public boolean exists() {
		File file = new File(itemsPath + courseName + studentLogPath + studentFileName + testEditorTypeFile);
		return (file.exists() && file.isFile() && file.canWrite());
	} // End of exists method


	/**
	 * Creates the path, if necessary, to store the log files of the students
	 * that have make any test of the course received as parameter
	 * @return true if the folder already exists of if the creation is correct
	 */
	private boolean createDirectory() {
		File folderStudentLog = new File(itemsPath + courseName + studentLogPath);
		if (!folderStudentLog.exists()) {
			folderStudentLog.mkdirs();

			// Copies the DTD file to the folder
			try {
				File studentDTD = new File(WOWStatic.config.Get("WOWROOT")
						.substring(0, WOWStatic.config.Get("WOWROOT").length() - 1)
						+ WOWStatic.config.Get("testeditordtdpath") + "student.dtd");

				byte [] data = new byte[(int) studentDTD.length()];

				BufferedInputStream input = new BufferedInputStream(new FileInputStream(studentDTD));
				DataInputStream dataInputStream = new DataInputStream(input);
				dataInputStream.read(data);
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(itemsPath + "student.dtd"));
				DataOutputStream dataOutputStream = new DataOutputStream(out);
				dataOutputStream.write(data);

				input.close();
				dataInputStream.close();
				out.close();
				dataOutputStream.close();
				
				return true;

			} catch (Exception e) {
				UtilLog.writeException(e);
				return false;
			}
		} else {
			return true;
		}
	} // End of createDirectory method 


	 /**
	  * Creates a student log file
	  * @param student Required data to create the file
	  * @return true if the creation is success
	  */
	 public boolean createStudentFile(final Student student) {
		 // Creates the XML Document and sets their DTD
		 Document xmlDocument = new Document().setDocType(new DocType("student", "../../student.dtd"));

		 // Creates the root element of the document
		 Element elementStudent = new Element("student");
		 elementStudent = elementStudent.setAttribute("login", student.getLogin().trim());
		 elementStudent = elementStudent.setAttribute("name", student.getName().trim());
		 String fileName = student.getFileName().trim() + testEditorTypeFile;
		 elementStudent = elementStudent.setAttribute("fileName", fileName);
		 elementStudent = elementStudent.setAttribute("path", student.getCourse()
				+ studentLogPath + student.getFileName() + testEditorTypeFile);
		 elementStudent = elementStudent.setAttribute("course", student.getCourse());
		 elementStudent = elementStudent.setAttribute("totalClassicTest", "0");
		 elementStudent = elementStudent.setAttribute("totalAdaptiveTest", "0");

		 // Sets the root element in the document
		 xmlDocument = xmlDocument.setRootElement(elementStudent);

		 // Saves the new created file
		 try {
			 // Creates the folder that stores the log file
			 if (!createDirectory()) {
				 return false;
			 }

			File xmlFile = new File(itemsPath + courseName + studentLogPath
					+ studentFileName + testEditorTypeFile);

			FileWriter xmlFileWriter = new FileWriter(xmlFile);
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
			out.output(xmlDocument, xmlFileWriter);
			xmlFileWriter.close();

			// Adds the name of the file to the file list
			return FileList.addStudentFile(this.courseName, this.studentFileName);

		} catch (IOException e) {
			UtilLog.writeException(e);
			return false;
		}
	 } // End of createStudentFile method


	 /**
	  * Adds a "test" child node to the "student" element of the student log file.
	  * the values of this new child node will be with 0 values or without values 
	  * @param testLogStudent Object with the information to insert
	  * @return true if success, false otherwise
	  */
	 public boolean addTest(final TestLogStudent testLogStudent) {
	//{
		boolean testNew = false;
		boolean addNewEvaluatedTest = false;
		String itemsFilesPath = WOWStatic.config.Get("itemsfilespath");
		
		try {
			// Loads the student log file
			File studentFile = new File(itemsPath + courseName + studentLogPath + studentFileName
					+ testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(studentFile);

			// Gets the root element
			Element student = xmlDocument.getRootElement();

			// Looks for a "test" element equals to the one to insert
			String testPath = WOWStatic.config.Get("testpath");
			String pathTestFile = courseName + testPath + testLogStudent.getFileName() + testEditorTypeFile;

			Element test = (Element) XPath.selectSingleNode(xmlDocument, "student/test[@path = \""
					+ pathTestFile + "\"]");

			if (test == null) {
				// The test doesn't exist. It must be append
				test = new Element("test");
				test = test.setAttribute("name", testLogStudent.getName());
				test = test.setAttribute("fileName", testLogStudent.getFileName() + testEditorTypeFile);
				test = test.setAttribute("path", courseName + testPath + testLogStudent.getFileName()
						+ testEditorTypeFile);

				test = test.setAttribute("make", "1");
				test = test.setAttribute("finish", "false");
				test = test.setAttribute("lastScore", "0");
				test = test.setAttribute("executionType", testLogStudent.getExecutionType());

				if (testLogStudent.getExecutionType().equals(TestEditor.ADAPTIVE)) {
					test = test.setAttribute("lastTheta", String.valueOf(testLogStudent.getTheta()));
					test = test.setAttribute("lastStandardError", String
							.valueOf(testLogStudent.getStandardError()));
				}
				addNewEvaluatedTest = true;
				testNew = true;

			} else {
				// The test already exists

				// Checks if the student can repeat the test
				TestFile testFile = new TestFile(courseName, testLogStudent.getFileName());
				StudentTest studentTest = testFile.getStudentTest(studentFileName);
				if (studentTest == null) {
					return false;
				} else {
					if (studentTest.getFinish() && !studentTest.getCanRepeat()) {
						// The student cannot repeat th
						return false;

					} else if (studentTest.getFinish()) {
						// The student had finished the test
						test.getAttribute("finish").setValue("false");
						test.getAttribute("make")
									.setValue(
											String.valueOf(Integer.valueOf(test.getAttribute("make").getValue())
													.intValue() + 1));

						if (testLogStudent.getExecutionType().equals(TestEditor.ADAPTIVE)) {
							test = test.setAttribute("lastTheta", String.valueOf(testLogStudent.getTheta()));
							test = test.setAttribute("lastStandardError", String.valueOf(testLogStudent.getStandardError()));
						}

						addNewEvaluatedTest = true;

					} else {
						// The student didn't finish the test
						addNewEvaluatedTest = false;
					}
				}
				testNew = false;
			}

			if (addNewEvaluatedTest) {
				// Creates the "evaluatedTest" element

				// Obtains the first position because there must not be
				//any more elements in the vector
				EvalTestLogStudent evalTestLogStudent = ((EvalTestLogStudent) testLogStudent.getEvaluatedTest()
						.get(0));

				Element evaluatedTest = new Element("evaluatedTest");

				evaluatedTest = evaluatedTest
						.setAttribute("score", String.valueOf(evalTestLogStudent.getScore()));
				evaluatedTest = evaluatedTest.setAttribute("timeTotal", String.valueOf(evalTestLogStudent
						.getTime()));
				evaluatedTest = evaluatedTest.setAttribute("date", evalTestLogStudent.getDate());
				evaluatedTest = evaluatedTest.setAttribute("totalQuestion", String.valueOf(evalTestLogStudent
						.getTotalQuestion()));
				evaluatedTest = evaluatedTest.setAttribute("correct", String.valueOf(evalTestLogStudent
						.getCorrect()));
				evaluatedTest = evaluatedTest.setAttribute("incorrect", String.valueOf(evalTestLogStudent
						.getIncorrect()));
				evaluatedTest = evaluatedTest.setAttribute("withoutAnswer", String.valueOf(evalTestLogStudent
						.getWithoutAnswer()));
				evaluatedTest = evaluatedTest.setAttribute("questionsDone", "0");

				if (testLogStudent.getExecutionType().equals(TestEditor.ADAPTIVE)) {
					evaluatedTest = evaluatedTest.setAttribute("theta", String.valueOf(testLogStudent.getTheta()));

					evaluatedTest = evaluatedTest.setAttribute("thetaPrevious", String.valueOf(testLogStudent
							.getTheta()));

					evaluatedTest = evaluatedTest.setAttribute("standardError", String.valueOf(testLogStudent
							.getStandardError()));
				}

				// Creates the child nodes of the "questionsFile" node.
				Vector questionsFileVector = evalTestLogStudent.getTestVector();

				for (int i = 0; i < questionsFileVector.size(); i++) {
					QuestionsFileTest questionsFileTest = ((QuestionsFileTest) questionsFileVector.get(i));
					// Creates the "questionsFile" node
					Element questionsFile = new Element("questionsFile");
					questionsFile = questionsFile.setAttribute("numberOfQuestions", String
							.valueOf(questionsFileTest.getCodeQuestions().size()));
					questionsFile = questionsFile.setAttribute("path", courseName + itemsFilesPath
							+ questionsFileTest.getQuestionsFileName() + testEditorTypeFile);

					Vector codeQuestionVector = questionsFileTest.getCodeQuestions();
					for (int j = 0; j < codeQuestionVector.size(); j++) {
						Element question = new Element("question");

						question = question.setAttribute("codeQuestion", codeQuestionVector.get(j).toString());
						question = question.setAttribute("answerValue", "withoutAnswer");
						question = question.setAttribute("time", "0");
						question = question.setAttribute("done", "false");

						// Adds this element to its parent
						questionsFile = questionsFile.addContent(question);
					}

					// Adds the "questionsFile" element to the "evaluatedTest" element
					evaluatedTest = evaluatedTest.addContent(questionsFile);
				}

				// Adds the "evaluatedTest" element to the "test" element
				test = test.addContent(evaluatedTest);

				// Sets the initial score of the test
				test.getAttribute("lastScore").setValue(String.valueOf(evalTestLogStudent.getScore()));
			}

			// Adds the test" element to the "student" element
			if (testNew == true) {
				student = student.addContent(test);

				if (testLogStudent.getExecutionType().equals(TestEditor.CLASSIC)) {
					int totalClassicTest = Integer.valueOf(
							student.getAttribute("totalClassicTest").getValue().trim()).intValue();
					totalClassicTest++;

					student.getAttribute("totalClassicTest").setValue(String.valueOf(totalClassicTest));
				} else {
					int totalAdaptiveTest = Integer.valueOf(
							student.getAttribute("totalAdaptiveTest").getValue().trim()).intValue();
					totalAdaptiveTest++;

					student.getAttribute("totalAdaptiveTest").setValue(String.valueOf(totalAdaptiveTest));
				}
			}

			if (testNew || addNewEvaluatedTest) {
				// Saves the file
				File xmlFile = new File(itemsPath + courseName + studentLogPath + studentFileName
							+ testEditorTypeFile);

				FileWriter xmlFileWriter = new FileWriter(xmlFile);
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
				xmlFileWriter.close();

				return true;

			} else {
				// No changes made
				return true;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	} // End of addTest method

	 
	/**
	 * Deletes a "test" node from the "student" element in a student log file. If after the update
	 * the "student" node has no children, then the student log file is deleted.
	 * @param pathTestFile Relavite path to the test to delete
	 * @return true or false
	 */
	 public boolean deleteTest(final String pathTestFile) {
	//{
		try {
			// Loads the student file
			File studentFile = new File(itemsPath + courseName + studentLogPath + studentFileName + testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(studentFile);

			// Gets the root element
			Element student = xmlDocument.getRootElement();

			// Selects the test from the student log file 
			Element test = (Element) XPath.selectSingleNode(xmlDocument, "student/test[@path = \""
					+ pathTestFile + "\"]");

			if (test != null) {
				// Deletes the selected node
				boolean removeTest = student.removeContent(test);
				if (removeTest == false) {
					return false;
				}

				// Updates the total test count makes with the value - 1
				student.getAttribute("totalClassicTest").setValue(
						String.valueOf(Integer.valueOf(student.getAttribute("totalClassicTest").getValue().trim())
								.intValue() - 1));

				if (Integer.valueOf(student.getAttribute("totalClassicTest").getValue().trim()).intValue() < 0)
					student.getAttribute("totalClassicTest").setValue("0");

				// Checks if the "student" node has no children
				// In case yes, the student log file must be deleted
				if (student.getChildren().size() == 0) {
					// Removes the student log file
					studentFile = new File(itemsPath + courseName + studentLogPath + studentFileName
							+ testEditorTypeFile);

					boolean deleteStudentFile = studentFile.delete();
					if (deleteStudentFile == false) {
						studentFile.deleteOnExit();
					}

					// Removes the file name from the file list
					FileList fileList = new FileList(this.courseName);
					fileList.deleteStudentFile(this.studentFileName);

				} else {
					// Save the updates
					File xmlFile = new File(itemsPath + courseName + studentLogPath + studentFileName
								+ testEditorTypeFile);

					FileWriter xmlFileWriter = new FileWriter(xmlFile);
					XMLOutputter out = new XMLOutputter();
					out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
					out.output(xmlDocument, xmlFileWriter);
					xmlFileWriter.close();
				}
				
				// The process finish correctly
				return true;
				
			} else {
				// The student log file doesn't contains that test file
				return true;
			}
				
		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	} // End of deleteTest method

	 
	/**
	  * Creates a Student object and returns it after setting the data of the student
	  * log file. The student doesn't have finish the test.
	  * @param testFileName Name of the test file
	  * @param executionType Type of test
	  * @return A Student object with the data of the student
	  */
	 public Student getStudentNotFinishedTest(final String testFileName, final String executionType) {
	//{
		try {
			// Loads the student log file
			File studentFile = new File(itemsPath + courseName + studentLogPath + studentFileName
						+ testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(studentFile);

			// Gets the root element
			Element elementStudent = xmlDocument.getRootElement();

			// Creates a new Student object to store the data
			Student student = new Student();

			student.setLogin(elementStudent.getAttribute("login").getValue().trim());
			student.setName(elementStudent.getAttribute("name").getValue().trim());
			student.setFileName(elementStudent.getAttribute("fileName").getValue().trim().substring(0,
					elementStudent.getAttribute("fileName").getValue().trim().lastIndexOf(testEditorTypeFile)));
			student.setPath(elementStudent.getAttribute("path").getValue().trim());
			student.setCourse(elementStudent.getAttribute("course").getValue().trim());
			student.setTotalClassicTest(Integer.valueOf(
					elementStudent.getAttribute("totalClassicTest").getValue().trim()).intValue());
			student.setTotalAdaptiveTest(Integer.valueOf(
					elementStudent.getAttribute("totalAdaptiveTest").getValue().trim()).intValue());

			// Gets the "test" element not finished
			String testPath = WOWStatic.config.Get("testpath");
			String testFilePath = courseName + testPath + testFileName + testEditorTypeFile;
			StringBuffer condition = new StringBuffer();
			condition.append("student/test");
				condition.append("[@finish = \"false\"]");
				condition.append("[@path = \"").append(testFilePath).append("\"]");
				condition.append("[@executionType = \"").append(executionType.trim()).append("\"]");
			condition.append("/evaluatedTest[last()]/..");
			Element test = (Element) XPath.selectSingleNode(xmlDocument, condition.toString());

			if (test != null) {
				// Creates the Vector object that stores the TestLogStudent objects
				Vector testLogStudentVector = new Vector();
	
				// Creates the TestLogStudent object
				TestLogStudent testLogStudent = new TestLogStudent();
	
				testLogStudent.setName(test.getAttribute("name").getValue().trim());
				testLogStudent.setFileName(test.getAttribute("fileName").getValue().trim().substring(0,
						test.getAttribute("fileName").getValue().trim().lastIndexOf(testEditorTypeFile)));
				testLogStudent.setPath(test.getAttribute("path").getValue().trim());
				testLogStudent.setMake(Integer.valueOf(test.getAttribute("make").getValue().trim()).intValue());
				if (test.getAttribute("finish").getValue().trim().equals("true")) {
					testLogStudent.setFinish(true);
				} else {
					testLogStudent.setFinish(false);
				}
	
				testLogStudent.setLastScore(Double.valueOf(test.getAttribute("lastScore").getValue().trim())
						.doubleValue());
	
				testLogStudent.setExecutionType(test.getAttribute("executionType").getValue().trim());
				if (test.getAttribute("lastTheta") != null) {
					testLogStudent.setTheta(Double.valueOf(test.getAttribute("lastTheta").getValue().trim())
							.doubleValue());
				} else {
					testLogStudent.setTheta(99999);
				}
	
				if (test.getAttribute("lastStandardError") != null) {
					testLogStudent.setStandardError(Double.valueOf(
							test.getAttribute("lastStandardError").getValue().trim()).doubleValue());
				} else {
					testLogStudent.setStandardError(99999);
				}
	
				// Gets the last "evaluatedTest" element
				condition = new StringBuffer();
				condition.append("student/test");
				condition.append("[@finish = \"false\"]");
				condition.append("[@path = \"").append(testFilePath).append("\"]");
				condition.append("[@executionType = \"").append(executionType).append("\"]");
				condition.append("/evaluatedTest[last()]");
				Element evaluatedTest = (Element) XPath.selectSingleNode(xmlDocument, condition.toString());
	
				// Creates the Vector object that stores the evalTestLogStudent
				Vector evalTestLogStudentVector = new Vector();
	
				// Creates the EvalTestLogStudent object
				EvalTestLogStudent evalTestLogStudent = new EvalTestLogStudent();
	
				evalTestLogStudent.setScore(Double.valueOf(evaluatedTest.getAttribute("score").getValue().trim())
						.doubleValue());
				evalTestLogStudent.setTime(Integer
						.valueOf(evaluatedTest.getAttribute("timeTotal").getValue().trim()).intValue());
				evalTestLogStudent.setDate(evaluatedTest.getAttribute("date").getValue().trim());
				evalTestLogStudent.setTotalQuestion(Integer.valueOf(
						evaluatedTest.getAttribute("totalQuestion").getValue().trim()).intValue());
				evalTestLogStudent.setCorrect(Integer.valueOf(
						evaluatedTest.getAttribute("correct").getValue().trim()).intValue());
				evalTestLogStudent.setIncorrect(Integer.valueOf(
						evaluatedTest.getAttribute("incorrect").getValue().trim()).intValue());
				evalTestLogStudent.setWithoutAnswer(Integer.valueOf(
						evaluatedTest.getAttribute("withoutAnswer").getValue().trim()).intValue());
				evalTestLogStudent.setQuestionsDone(Integer.valueOf(
						evaluatedTest.getAttribute("questionsDone").getValue().trim()).intValue());
				if (evaluatedTest.getAttribute("theta") != null) {
					evalTestLogStudent.setTheta(Double.valueOf(evaluatedTest.getAttribute("theta").getValue().trim())
							.doubleValue());
				} else
					evalTestLogStudent.setTheta(99999);
	
				if (evaluatedTest.getAttribute("thetaPrevious") != null) {
					evalTestLogStudent.setThetaPrevious(Double.valueOf(
							evaluatedTest.getAttribute("thetaPrevious").getValue().trim()).doubleValue());
				} else
					evalTestLogStudent.setThetaPrevious(evalTestLogStudent.getTheta());
	
				if (evaluatedTest.getAttribute("standardError") != null) {
					evalTestLogStudent.setStandardError(Double.valueOf(
							evaluatedTest.getAttribute("standardError").getValue().trim()).doubleValue());
				} else
					evalTestLogStudent.setStandardError(99999);
	
				// Gets the "questionsFile" element and the codes
				// of the questions used from that question files
				List questionsFileList = evaluatedTest.getChildren("questionsFile");
	
				// Creates a new Vector to store the QuestionFileTest objects
				Vector questionsFileVector = new Vector();
	
				// Loops to read data
				for (int i = 0; i < questionsFileList.size(); i++) {
					// Creates a new QuestionsFileTest object
					QuestionsFileTest questionsFileTest = new QuestionsFileTest();
	
					Element questionsFile = (Element) questionsFileList.get(i);
	
					String questionsFileName = questionsFile.getAttribute("path").getValue().trim();
					questionsFileName = questionsFileName.substring(questionsFileName.lastIndexOf("/") + 1,
							questionsFileName.lastIndexOf(testEditorTypeFile));
	
					questionsFileTest.setQuestionsFileName(questionsFileName);
	
					// Gets the codes of the question of this question file
					List questionList = questionsFile.getChildren("question");
	
					// Creates the Vector objects that stores the data of the "codeQuestion" elements
					Vector time = new Vector();
					Vector answersValues = new Vector();
					Vector codeQuestions = new Vector();
					Vector done = new Vector();
					Vector answers = new Vector();
	
					// Loops to read the data of the "codeQuestion" elements
					for (int j = 0; j < questionList.size(); j++) {
						Element question = (Element) questionList.get(j);
	
						codeQuestions.add(question.getAttribute("codeQuestion").getValue().trim());
	
						answersValues.add(question.getAttribute("answerValue").getValue().trim());
	
						time.add(question.getAttribute("time").getValue().trim());
	
						done.add(question.getAttribute("done").getValue().trim());
	
						// Reads the codes of the answered responses in case it exists
						AnswersTestLogStudent answersTestLogStudent = new AnswersTestLogStudent();
						answersTestLogStudent.setCourse(courseName);
						answersTestLogStudent.setQuestionsFileName(questionsFileName);
						answersTestLogStudent.setCodeQuestion(question.getAttribute("codeQuestion").getValue().trim());
	
						Vector codeAnswersVector = new Vector();
						if (question.getAttribute("done").getValue().trim().equals("true")) {
							List answersList = question.getChildren("answer");
							if (answersList.isEmpty() == false) {
								for (int k = 0; k < answersList.size(); k++) {
									codeAnswersVector.add(((Element) answersList.get(k)).getAttribute("codeAnswer")
											.getValue().trim());
								}
							} else
								codeAnswersVector = null;
						} else
							codeAnswersVector = null;
	
						answersTestLogStudent.setCodeAnswers(codeAnswersVector);
	
						answers.add(answersTestLogStudent);
					}
	
					// Adds the vectors to the questionsFileTest element
					questionsFileTest.setCodeQuestions(codeQuestions);
					questionsFileTest.setAnswersValues(answersValues);
					questionsFileTest.setTimes(time);
					questionsFileTest.setDone(done);
					questionsFileTest.setAnswers(answers);
	
					// Adds the questionsFileTest object to the questionsFileVector Vector
					questionsFileVector.add(questionsFileTest);
				}
	
				// Adds the questionsFileTest Vector to the evalTestLogStudent Object
				evalTestLogStudent.setTestVector(questionsFileVector);
	
				// Adds the evalTestLogStudent object to the evalTestLogStudentVector Vector
				evalTestLogStudentVector.add(evalTestLogStudent);
	
				// Adds the evalTestLogStudentVector object to the testLogStudent object
				testLogStudent.setEvaluatedTest(evalTestLogStudentVector);
	
				// Adds the testLogStudent object to the testLogStudentVector Vector
				testLogStudentVector.add(testLogStudent);
	
				// Adds the testLogStudent object to the student object
				student.setTest(testLogStudentVector);
	
				return student; //Returns the data

			} else {
				// The test cannot be found in the file
				return null;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}
	} // End of getStudentNotFinishedTest method

	 
	 /**
	  * Creates a Student object and returns it after setting the data of the student
	  * log file. The student have finish the test.
	  * @param testFileName Name of the test file
	  * @return A Student object with the data of the student
	  */
	public Student getStudentFinishedTest(final String testFileName) {
	//{
		try {
			// Loads the student log file
			File studentFile = new File(itemsPath + courseName + studentLogPath + studentFileName
						+ testEditorTypeFile);
			Document xmlDocument = new SAXBuilder().build(studentFile);

			// Gets the root element
			Element elementStudent = xmlDocument.getRootElement();

			// Creates a new Student object
			Student student = new Student();

			student.setLogin(elementStudent.getAttribute("login").getValue().trim());
			student.setName(elementStudent.getAttribute("name").getValue().trim());
			student.setFileName(elementStudent.getAttribute("fileName").getValue().trim().substring(0,
					elementStudent.getAttribute("fileName").getValue().trim().lastIndexOf(testEditorTypeFile)));
			student.setPath(elementStudent.getAttribute("path").getValue().trim());
			student.setCourse(elementStudent.getAttribute("course").getValue().trim());
			student.setTotalClassicTest(Integer.valueOf(
					elementStudent.getAttribute("totalClassicTest").getValue().trim()).intValue());
			student.setTotalAdaptiveTest(Integer.valueOf(
					elementStudent.getAttribute("totalAdaptiveTest").getValue().trim()).intValue());

			// Gets the "test" element finished
			String testPath = WOWStatic.config.Get("testpath");
			String testFilePath = courseName + testPath + testFileName + testEditorTypeFile;

			StringBuffer condition = new StringBuffer();
			condition.append("student/test");
				condition.append("[@finish = \"true\"]");
				condition.append("[@path = \"").append(testFilePath).append("\"]");
			condition.append("/evaluatedTest[last()]/..");

			Element test = (Element) XPath.selectSingleNode(xmlDocument, condition.toString());

			if (test != null) {

				// Creates a Vector to store the testLogStudent object
				Vector testLogStudentVector = new Vector();
	
				// Creates a TestLogStudent object
				TestLogStudent testLogStudent = new TestLogStudent();
	
				testLogStudent.setName(test.getAttribute("name").getValue().trim());
				testLogStudent.setFileName(test.getAttribute("fileName").getValue().trim().substring(0,
						test.getAttribute("fileName").getValue().trim().lastIndexOf(testEditorTypeFile)));
				testLogStudent.setPath(test.getAttribute("path").getValue().trim());
				testLogStudent.setMake(Integer.valueOf(test.getAttribute("make").getValue().trim()).intValue());
				if (test.getAttribute("finish").getValue().trim().equals("true"))
					testLogStudent.setFinish(true);
				else
					testLogStudent.setFinish(false);
	
				testLogStudent.setLastScore(Double.valueOf(test.getAttribute("lastScore").getValue().trim())
						.doubleValue());
	
				testLogStudent.setExecutionType(test.getAttribute("executionType").getValue().trim());
				if (test.getAttribute("lastTheta") != null)
					testLogStudent.setTheta(Double.valueOf(test.getAttribute("lastTheta").getValue().trim())
							.doubleValue());
				else
					testLogStudent.setTheta(99999);
	
				if (test.getAttribute("lastStandardError") != null)
					testLogStudent.setStandardError(Double.valueOf(
							test.getAttribute("lastStandardError").getValue().trim()).doubleValue());
				else
					testLogStudent.setStandardError(99999);
	
				// Gets the last evaluatedTest element
				Element evaluatedTest = (Element) XPath.selectSingleNode(xmlDocument, "student/test"
						+ "[@finish = \"true\"]" + "[@path = \"" + testFilePath + "\"]" + "/evaluatedTest[last()]");
	
				// Creates the Vector that contains the evalTestLogStudent object
				Vector evalTestLogStudentVector = new Vector();
	
				// Creates the EvalTestLogStudent object
				EvalTestLogStudent evalTestLogStudent = new EvalTestLogStudent();
	
				evalTestLogStudent.setScore(Double.valueOf(evaluatedTest.getAttribute("score").getValue().trim())
						.doubleValue());
				evalTestLogStudent.setTime(Integer
						.valueOf(evaluatedTest.getAttribute("timeTotal").getValue().trim()).intValue());
				evalTestLogStudent.setDate(evaluatedTest.getAttribute("date").getValue().trim());
				evalTestLogStudent.setTotalQuestion(Integer.valueOf(
						evaluatedTest.getAttribute("totalQuestion").getValue().trim()).intValue());
				evalTestLogStudent.setCorrect(Integer.valueOf(
						evaluatedTest.getAttribute("correct").getValue().trim()).intValue());
				evalTestLogStudent.setIncorrect(Integer.valueOf(
						evaluatedTest.getAttribute("incorrect").getValue().trim()).intValue());
				evalTestLogStudent.setWithoutAnswer(Integer.valueOf(
						evaluatedTest.getAttribute("withoutAnswer").getValue().trim()).intValue());
				evalTestLogStudent.setQuestionsDone(Integer.valueOf(
						evaluatedTest.getAttribute("questionsDone").getValue().trim()).intValue());
				if (evaluatedTest.getAttribute("theta") != null) {
					evalTestLogStudent.setTheta(Double.valueOf(evaluatedTest.getAttribute("theta").getValue().trim())
							.doubleValue());
				} else
					evalTestLogStudent.setTheta(99999);
	
				if (evaluatedTest.getAttribute("thetaPrevious") != null) {
					evalTestLogStudent.setThetaPrevious(Double.valueOf(
							evaluatedTest.getAttribute("thetaPrevious").getValue().trim()).doubleValue());
				} else
					evalTestLogStudent.setThetaPrevious(evalTestLogStudent.getTheta());
	
				if (evaluatedTest.getAttribute("standardError") != null) {
					evalTestLogStudent.setStandardError(Double.valueOf(
							evaluatedTest.getAttribute("standardError").getValue().trim()).doubleValue());
				} else
					evalTestLogStudent.setStandardError(99999);
	
				// Gets the "questionsFile" element and the codes of the questions
				// used from that question files
				List questionsFileList = evaluatedTest.getChildren("questionsFile");
	
				// Creates the Vector object that stores the QuestionsFileTest objects
				Vector questionsFileVector = new Vector();
	
				// Loops to read data
				for (int i = 0; i < questionsFileList.size(); i++) {
					// Creates a QuestionsFileTest object
					QuestionsFileTest questionsFileTest = new QuestionsFileTest();
	
					Element questionsFile = (Element) questionsFileList.get(i);
	
					String questionsFileName = questionsFile.getAttribute("path").getValue().trim();
					questionsFileName = questionsFileName.substring(questionsFileName.lastIndexOf("/") + 1,
							questionsFileName.lastIndexOf(testEditorTypeFile));
	
					questionsFileTest.setQuestionsFileName(questionsFileName);
	
					// Gets the codes of the questions of this question file
					List questionList = questionsFile.getChildren("question");
	
					// Creates the Vector objects that stores the data of the "codeQuestion" elements
					Vector time = new Vector();
					Vector answersValues = new Vector();
					Vector codeQuestions = new Vector();
					Vector done = new Vector();
					Vector answers = new Vector();
	
					// Loops to read the data of the "codeQuestion" elements
					for (int j = 0; j < questionList.size(); j++) {
						Element question = (Element) questionList.get(j);
	
						codeQuestions.add(question.getAttribute("codeQuestion").getValue().trim());
	
						answersValues.add(question.getAttribute("answerValue").getValue().trim());
	
						time.add(question.getAttribute("time").getValue().trim());
	
						done.add(question.getAttribute("done").getValue().trim());
	
						// Read the codes of the answered responses in case it exists
						AnswersTestLogStudent answersTestLogStudent = new AnswersTestLogStudent();
						answersTestLogStudent.setCourse(courseName);
						answersTestLogStudent.setQuestionsFileName(questionsFileName);
						answersTestLogStudent.setCodeQuestion(question.getAttribute("codeQuestion").getValue().trim());
	
						Vector codeAnswersVector = new Vector();
						if (question.getAttribute("done").getValue().trim().equals("true")) {
							List answersList = question.getChildren("answer");
							if (answersList.isEmpty() == false) {
								for (int k = 0; k < answersList.size(); k++) {
									codeAnswersVector.add(((Element) answersList.get(k)).getAttribute("codeAnswer")
											.getValue().trim());
								}
							} else
								codeAnswersVector = null;
						} else
							codeAnswersVector = null;
	
						answersTestLogStudent.setCodeAnswers(codeAnswersVector);
	
						answers.add(answersTestLogStudent);
					}
	
					// Adds the Vector objects to the questionsFileTest object
					questionsFileTest.setCodeQuestions(codeQuestions);
					questionsFileTest.setAnswersValues(answersValues);
					questionsFileTest.setTimes(time);
					questionsFileTest.setDone(done);
					questionsFileTest.setAnswers(answers);
	
					// Adds the questionsFileTest object to the questionsFileVector Vector
					questionsFileVector.add(questionsFileTest);
				}
	
				// Adds the questionsFileTest Vector to the evalTestLogStudent object
				evalTestLogStudent.setTestVector(questionsFileVector);
	
				// Adds the evalTestLogStudent to the  evalTestLogStudentVector Vector
				evalTestLogStudentVector.add(evalTestLogStudent);
	
				// Adds the evalTestLogStudentVector to the testLogStudent Vector
				testLogStudent.setEvaluatedTest(evalTestLogStudentVector);
	
				// Adds the testLogStudent object to the testLogStudentVector Vector
				testLogStudentVector.add(testLogStudent);
	
				// Adds the testLogStudent object to the student object
				student.setTest(testLogStudentVector);
	
				return student; // Returns the data

			} else {
				// The student log file doesn't contain the finished test
				return null;
			}
		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}
	} // End of getStudentFinishedTest method


	/**
	 * Updates the data of the evaluatedTest element that is being executed. Updates data such as the
	 * "done", "answer" and "time" of the "question" element that has been asked. Add to that node a
	 * "respondedAnswer" node with the codes of the answers selected by the student. It also updates
	 * the correct", "incorrect", "withoutAnswer", "score", "timeTotal", "questionsDone" of the
	 * "evaluatedTest" element. This method returns the current score of the test, the same that has
	 * been sent to update, in case the the process goes OK. Returns null if any error occurs.
	 * @param student Student that is making the test and has to be updated.
	 * @return the final score or null if any error occurs
	 */
	public String updateEvaluatedTest(final Student student) {
	//{	
		try {
			// Loads the student file
			String thePath = itemsPath + courseName + studentLogPath + studentFileName + testEditorTypeFile;
			File studentFile = new File(thePath);
			Document xmlDocument = new SAXBuilder().build(studentFile);

			String pathTestFile = ((TestLogStudent) student.getTest().firstElement()).getPath();
			String executionType = ((TestLogStudent) student.getTest().firstElement()).getExecutionType();
			EvalTestLogStudent evalTestLogStudent = ((EvalTestLogStudent) ((TestLogStudent) student.getTest()
					.firstElement()).getEvaluatedTest().firstElement());
			QuestionsFileTest questionsFileTest = ((QuestionsFileTest) evalTestLogStudent.getTestVector().firstElement());
			String pathQuestionsFile = student.getCourse() + WOWStatic.config.Get("itemsfilespath")
			+ questionsFileTest.getQuestionsFileName() + testEditorTypeFile;
			String codeQuestion = questionsFileTest.getCodeQuestions().firstElement().toString().trim();

			// Looks over the "question" element to update
			StringBuffer condition = new StringBuffer();
			condition.append("student/test");
				condition.append("[@path = \"").append(pathTestFile).append("\"]");
				condition.append("[@finish = \"false\"]");
				condition.append("[@executionType = \"").append(executionType).append("\"]");
			condition.append("//evaluatedTest[last()]/questionsFile[@path = \"").append(pathQuestionsFile).append("\"]");
			condition.append("/question[@codeQuestion = \"").append(codeQuestion).append("\"]");

			Element question = (Element) XPath.selectSingleNode(xmlDocument, condition.toString());

			if (question != null) {
				String score = String.valueOf(evalTestLogStudent.getScore());
				double theta = evalTestLogStudent.getTheta();
				double thetaPrevious = evalTestLogStudent.getThetaPrevious();
				double standardError = evalTestLogStudent.getStandardError();
				String answerValue = questionsFileTest.getAnswersValues().firstElement().toString().trim();
				String time = questionsFileTest.getTimes().firstElement().toString().trim();
				Vector answerVector = questionsFileTest.getAnswers();
				AnswersTestLogStudent answersTestLogStudent = ((AnswersTestLogStudent) questionsFileTest
						.getAnswers().firstElement());
				Vector codeAnswersVector = answersTestLogStudent.getCodeAnswers();
				
				// Updates the values
				question.getAttribute("answerValue").setValue(answerValue);
				question.getAttribute("time").setValue(
						String.valueOf(Integer.valueOf(question.getAttribute("time").getValue()).intValue()
								+ Integer.valueOf(time).intValue()));
				question.getAttribute("done").setValue("true");

				if (!answerValue.equals("withoutAnswer") && answerVector != null && !answerVector.isEmpty()) {
					// Creates the "respondedAnswer" elements
					for (int i = 0; i < codeAnswersVector.size(); i++) {
						Element answer = new Element("answer");
						answer = answer.setAttribute("codeAnswer", codeAnswersVector.get(i).toString());
						// Adds the "respondedAnswer" element
						question = question.addContent(answer);
					}
				}

				// Gets the "evaluatedTest" element of this "question" element
				Element evaluatedTest = (Element) question.getParent().getParent();

				// Updates the values of the "evaluatedTest" element

				// Updates the total time to make the test
				evaluatedTest.getAttribute("timeTotal").setValue(
						String.valueOf(Integer.valueOf(evaluatedTest.getAttribute("timeTotal").getValue())
								.intValue()
								+ Integer.valueOf(time).intValue()));

				// Updates the values of the correct, incorrect or not answered answers
				evaluatedTest.getAttribute("correct").setValue(String.valueOf(evalTestLogStudent.getCorrect()));

				evaluatedTest.getAttribute("incorrect").setValue(
						String.valueOf(evalTestLogStudent.getIncorrect()));

				evaluatedTest.getAttribute("withoutAnswer").setValue(
						String.valueOf(evalTestLogStudent.getWithoutAnswer()));

				// Updates the value of the questions already done
				evaluatedTest.getAttribute("questionsDone").setValue(
						String.valueOf(evalTestLogStudent.getQuestionsDone()));

				// Updates the score of the test
				evaluatedTest.getAttribute("score").setValue(score);
				score = evaluatedTest.getAttribute("score").getValue().trim();

				// Updates the theta value if needed
				if (evaluatedTest.getAttribute("theta") != null
						&& evaluatedTest.getAttribute("thetaPrevious") != null
						&& evaluatedTest.getAttribute("standardError") != null && theta != 99999
						&& thetaPrevious != 99999 && standardError != 99999) {
					evaluatedTest.getAttribute("theta").setValue(String.valueOf(theta));
					evaluatedTest.getAttribute("thetaPrevious").setValue(String.valueOf(thetaPrevious));
					evaluatedTest.getAttribute("standardError").setValue(String.valueOf(standardError));
				}

				// Updates the values of the "test" node
				Element test = (Element) evaluatedTest.getParent();

				// Value of the last score in the test.
				test.getAttribute("lastScore").setValue(score);

				// Updates the theta value if needed
				if (test.getAttribute("lastTheta") != null && theta != 99999)
					test.getAttribute("lastTheta").setValue(String.valueOf(theta));

				if (test.getAttribute("lastStandardError") != null && standardError != 99999)
					test.getAttribute("lastStandardError").setValue(String.valueOf(standardError));

				// Saves the new log file created
				FileWriter xmlFileWriter = new FileWriter(new File(thePath));
				XMLOutputter out = new XMLOutputter();
				out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
				out.output(xmlDocument, xmlFileWriter);
				xmlFileWriter.close();

				return score;

			} else {
				UtilLog.toLog("StudentFile.updateEvaluatedTest => The question element is null");
				return null;
			}

		} catch (Exception e) {
			UtilLog.writeException(e);
			return null;
		}
	} // End of updateEvaluatedTest method

	
	/**
	 * Sets the "finish" attribute of the "test" node to "true", avoiding that the student could make
	 * the test another time. Returns true or false.
	 * @param testFileName Name of the test file name
	 * @param executionType Type of test
	 * @return true if the process finish correctly, false otherwise
	 */
	 public boolean endTest(final String testFileName, final String executionType) {
	//{
		try {
			// Loads the student file
			File studentFile = new File(itemsPath + courseName + studentLogPath + studentFileName
					+ testEditorTypeFile);

			Document xmlDocument = new SAXBuilder().build(studentFile);

			// Search the "test" element that is equal to the one to insert
			String testPath = WOWStatic.config.Get("testpath");
			String testFilePath = courseName + testPath + testFileName + testEditorTypeFile;

			Element test = (Element) XPath.selectSingleNode(xmlDocument, "student/test[@path = \""
					+ testFilePath + "\"]" + "[@executionType = \"" + executionType.trim() + "\"]"
					+ "[@finish = \"false\"]");

			if (test != null) {
				// Gets the last "evaluatedTest" element
				Element evaluatedTest = (Element) XPath.selectSingleNode(xmlDocument, "student/test"
						+ "[@path = \"" + testFilePath + "\"]" + "[@executionType = \"" + executionType.trim()
						+ "\"]" + "[@finish = \"false\"]" + "/evaluatedTest[last()]");

				// Updates the value of the "lastScore" attribute
				test.getAttribute("lastScore").setValue(evaluatedTest.getAttribute("score").getValue().trim());

				// Updates the value of the "finish" attribute
				test.getAttribute("finish").setValue("true");

				// Updates the value of the "lastTheta" attribute if needed
				if (executionType.trim().equals(TestEditor.ADAPTIVE) && test.getAttribute("lastTheta") != null
						&& evaluatedTest.getAttribute("theta") != null
						&& !evaluatedTest.getAttribute("theta").getValue().equals("99999")) {
					// Is a adaptive test
					test.getAttribute("lastTheta").setValue(evaluatedTest.getAttribute("theta").getValue().trim());
					test.getAttribute("lastStandardError").setValue(
							evaluatedTest.getAttribute("standardError").getValue().trim());
				}
			}

			// Saves the new file
			File xmlFile = new File(itemsPath + courseName + studentLogPath + studentFileName + testEditorTypeFile);

			FileWriter xmlFileWriter = new FileWriter(xmlFile);
			XMLOutputter out = new XMLOutputter();
			out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
			out.output(xmlDocument, xmlFileWriter);
			xmlFileWriter.close();
			return true;

		} catch (Exception e) {
			UtilLog.writeException(e);
			return false;
		}
	}

} // End of StudentFile class