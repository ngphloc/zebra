package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.datacomponents.AttributeValue;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.ProfileUpdate;
import vn.spring.zebra.evaluation.TestEngineEvent;
import vn.spring.zebra.evaluation.TestEngineListener;
import vn.spring.zebra.helperservice.WOWContextListener;
import vn.spring.zebra.um.TriUM;
import es.uco.WOW.StudentFile.StudentFile;
import es.uco.WOW.TestFile.TestFile;
import es.uco.WOW.Utils.Test;
import es.uco.WOW.Utils.UtilLog;

/**
 * <p>Title: Wow! TestEditor</p> 
 * <p>Description: Herramienta para la edicion de preguntas tipo test adaptativas</p> 
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Universidad de C�rdoba (Espa�a), University of Science</p>
 * @version 1.0
 */

/**
 * NAME: EndTest. 
 * FUNCTION: This servlet receives a name of a student and calls
 * the endTest method of TestFile and the endTest method of StudentFile
 * to finish the execution of the test that this student is doing.
 * LAST MODIFICATION: 06-02-2008
 * @author Isaac R�der Jim�nez, changed by Loc Nguyen
 */
public final class EndTest extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
	//{
		/**
		 * Name of course that the test belongs to
		 */
		String courseName = "";
		
		/**
		 * Name of the test file that will be finished by the student
		 */
		String testFileName = "";
		
		/**
		 * Login of the student
		 */
		String login = "";
		
		/**
		 * Stores the value return by the servlet
		 */
		double lastScore = -1; 
		
		/**
		 * Knowledge of the Student
		 */
		double theta = 99999;
		
		/**
		 * Error to calculate the knowledge (theta) of the student
		 */
		double standardError = 99999;

		/**
		 * Type of test ("classic" or "adaptive")
		 */
		String executionType = "";
		
		/**
		 * Concept that is being evaluated
		 */
		String concept = ""; 
		
		/**
		 * Reference to the Test object that will be set as finished
		 */
		Test test = null;

		ObjectOutputStream objectOutputStream = null;

		try {
			// Get the parameters
			courseName = req.getParameter("courseName");
			testFileName = req.getParameter("testFileName");
			login = req.getParameter("login");
			executionType = req.getParameter("executionType");
			concept = req.getParameter("concept");

			if (req.getParameter("lastScore") != null) {
				lastScore = Double.valueOf(req.getParameter("lastScore")).doubleValue();
			}
			if (req.getParameter("theta") != null) {
				theta = Double.valueOf(req.getParameter("theta")).doubleValue();
			}
			if (req.getParameter("standardError") != null) {
				standardError = Double.valueOf(req.getParameter("standardError")).doubleValue();
			}

			if (courseName != null && testFileName != null
			 && login != null && lastScore != -1
			 && executionType != null && concept != null)
			{
				if (TestFile.endStudentTest(courseName, testFileName, login, lastScore, theta, standardError))
				{
					StudentFile studentFile = new StudentFile(courseName, login);
					if (studentFile.endTest(testFileName, executionType)) {
						// Get the test data
						TestFile testFile = new TestFile(courseName, testFileName);
						test = testFile.getTest();

						// Reads the knowledge percentage that this test represents for 
						// the Concept evaluation 
						double knowledgePercentage = Double.parseDouble(test.getKnowledgePercentage());
						double lastScoreDouble = (lastScore * knowledgePercentage) / 100.0;

						String score = String.valueOf(Math.round(lastScoreDouble));

						// Saves the the test score in the Student profile
						try {
							/*
							 * ProfileDB profileDB = WOWStatic.DB().getProfileDB(); id =
							 * profileDB.findProfile(login); Profile profile =
							 * profileDB.getProfile(id);
							 */

							Profile profile = ((Profile) this.getServletContext().getAttribute(login));

//							WOWDB db = WOWStatic.DB();
//							Hashtable values = profile.getValues();
//							if (values.containsKey(concept + ".knowledge")) {
//								profile.setAttributeValue(concept, "knowledge", String.valueOf(score));
//							} else {
//								AttributeValue attributeValue = new AttributeValue(true);
//								attributeValue.setValue(String.valueOf(score));
//								values.put(concept + ".knowledge", attributeValue);
//							}
//							db.getProfileDB().setProfile(profile.id, profile);

							// TODO - new way to evaluate 
							// Ask David Smits
							AttributeValue avalue = new AttributeValue(true);
							avalue.setValue(String.valueOf(score)); // the new value here
							ProfileUpdate update = new ProfileUpdate(profile, WOWStatic.PM());
							update.updateAttribute(concept, "knowledge", avalue);
							WOWStatic.PM().purgeProfile(profile);
							WOWStatic.DB().getProfileDB().setProfile(profile.id, profile);
							
							//Loc Nguyen add October 09 2009
							try {
								TriUM um = WOWContextListener.getInstance().getTriUMServer().getUM(login, courseName, true);
								TestEngineEvent evt = new TestEngineEvent(login, courseName, concept, lastScoreDouble);
								
								TestEngineListener staticListener = um.getZebraNetworker().
									getStaticKnowledgeDaemon(login, courseName);
								staticListener.testEvaluated(evt);

								TestEngineListener dynListener = um.getZebraNetworker().
									getDynKnowledgeDaemon(login, courseName);
								dynListener.testEvaluated(evt);
								
								System.out.println("EndTest servlet calls to update static and dynamic knowledge successfully");
							}
							catch(Exception e) {
								System.out.println("That EndTest servlet calls to update static and dynamic knowledge causes error: " + e.getMessage());
							}

						} catch (vn.spring.WOW.exceptions.InvalidProfileException ipe) {
							UtilLog.writeException(ipe);
							lastScore = -1;
						} catch (vn.spring.WOW.exceptions.InvalidAttributeException iae) {
							UtilLog.writeException(iae);
							lastScore = -1;
						} catch (vn.spring.WOW.exceptions.DatabaseException dbe) {
							UtilLog.writeException(dbe);
							lastScore = -1;
						} catch (Exception e) {
							// General exception
							UtilLog.writeException(e);
							lastScore = -1;
						}
					} else {
						lastScore = -1;
					}	
				} else {
					lastScore = -1;
				}
			} else {
				lastScore = -1;
			}

			// Returns the score calculated by the test engine or null
			// in error case
			objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			if (lastScore == -1) {
				objectOutputStream.writeObject(null);
			} else {
				objectOutputStream.writeObject(String.valueOf(lastScore));
			}

		} catch (Exception e) {
			objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			objectOutputStream.writeObject(null);
			UtilLog.writeException(e);

		} finally {
			objectOutputStream.close();
		}
	}
}
