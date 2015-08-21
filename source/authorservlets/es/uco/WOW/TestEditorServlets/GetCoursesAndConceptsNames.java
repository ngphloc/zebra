package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.WOWDB;
import vn.spring.WOW.WOWDB.ConceptDB;
import vn.spring.WOW.config.WowAuthor;
import vn.spring.WOW.config.AuthorsConfig;
import vn.spring.WOW.datacomponents.Concept;
import es.uco.WOW.Utils.Course;
import es.uco.WOW.Utils.UtilLog;

/**
 * <p> Title: Wow! TestEditor </p> 
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas</p> 
 * <p> Copyright: Copyright (c) 2008 </p>
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: GetCoursesAndConceptsNames
 * FUNCTION: Receives the login of the author and takes the 
 * name of the courses that belongs to the author and the concepts 
 * for each course. This information will be returned in a Vector
 * of Course objects
 * LAST MODIFICATION: 06-02-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public final class GetCoursesAndConceptsNames extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void doPost(final HttpServletRequest req, final HttpServletResponse resp)
		throws ServletException, IOException 
	{
		/**
		 * Vector of Course objects that will be the output of the servlet
		 */
		Vector courseVector = null;
		
		// Gets the parameters
		String login = req.getParameter("userLogin").trim();

		// Returns the data or null in error case
		ObjectOutputStream objectOutputStream = null;
		try {
			// Gets the courses that belongs to the author
			courseVector = getCoursesAndConceptsNames(login);
			objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			objectOutputStream.writeObject(courseVector);
		}
		catch (Exception e) {
			UtilLog.writeException(e);
			if (objectOutputStream ==null) {
				objectOutputStream = new ObjectOutputStream(resp.getOutputStream());
			}
			objectOutputStream.writeObject(null);
		}
		objectOutputStream.close();
	}

	
	/**
	 * Returns a Vector object with the list of concepts that
	 * belongs to the author with the login passed as argument.
	 * @param login Author login
	 * @return A Vector object
	 */
	private Vector getCoursesAndConceptsNames(final String login) throws Exception {
		// Stores the name of the courses
		Vector courseNameVector = null; 
		
		// Vector output object
		Vector courseVector = new Vector();
			
		// Get the name of the courses associated to the author
		courseNameVector = getCoursesNames(login);
		
		// Gets a reference to the WOW database to get the data
		WOWDB db = WOWStatic.DB();
		ConceptDB cdb = db.getConceptDB();

		Vector conceptVector = cdb.getConceptList();

		for (int i = 0; i < courseNameVector.size(); i++) {
			Course course = new Course();
			course.setName(courseNameVector.get(i).toString().trim());

			Vector conceptVectorTemp = new Vector();
			Vector abstractConceptVector = new Vector();
			Vector testConceptVector = new Vector();

			for (int j = 0; j < conceptVector.size(); j++) {
				String conceptName = conceptVector.get(j).toString().trim();
				if (conceptName.startsWith(course.getName())) {
					try {
						// Gets the ID of the current concept
						long id = cdb.findConcept(conceptName);

						// Gets the concept by its ID
						Concept concept = cdb.getConcept(id);
										
						// Difference between the type of concept
						String conceptType = concept.getType().trim();
						if (conceptType.equalsIgnoreCase("abstract")) {
							abstractConceptVector.add(conceptName);
						} else {
							conceptVectorTemp.add(conceptName);
							if (conceptType.equalsIgnoreCase("test")) {
								testConceptVector.add(conceptName);
							}
						}
							

					} catch (Exception e) {
						UtilLog.writeException(e);
					}
				}
			}
			
			// TODO - test type concepts - before we make this
			//course.setConceptNames(conceptVectorTemp);
			//course.setTestConceptNames(testConceptVector);
			//course.setAbstractConceptNames(abstractConceptVector);

			course.setAbstractConceptNames(abstractConceptVector);
			course.setConceptNames(testConceptVector);

			courseVector.add(course);
		}
		return courseVector;
	}

	
	/**
	 * Gets the name of the courses that belongs to the author
	 * Analyse the authorlistfile.xml to obtain the course list.
	 * passed as argument
	 * @param login Author login
	 * @return A Vector object with the name of the course
	 */
	private Vector getCoursesNames(final String login) {
		AuthorsConfig authorConfig = new AuthorsConfig();
		
		WowAuthor wowAuthor = authorConfig.GetAuthor(login);
		
		if(wowAuthor != null)
			return wowAuthor.getCourseList();
		else
			return new Vector();
	}
}