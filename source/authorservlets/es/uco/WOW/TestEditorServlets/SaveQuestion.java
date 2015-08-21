package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.QuestionsFile.QuestionFile;
import es.uco.WOW.TestEditor.TestEditor;
import es.uco.WOW.Utils.Question;

/**
 * <p> Title: Wow! TestEditor </p> 
 * <p> Description: Herramienta para la edicion de preguntas tipo test adaptativas </p> 
 * <p> Copyright: Copyright (c) 2008 </p>
 * <p> Company: Universidad de C�rdoba (Espa�a), University of Science </p>
 * 
 * @version 1.0
 */

/**
 * NAME: SaveQuestion. 
 * FUNCTION: Receives a Question object that contains all the necessary data
 * to create a new question in the question file.
 * LAST MODIFICATION: 06-02-2008
 * 
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class SaveQuestion extends HttpServlet
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		/**
		 * File type of the image data
		 */
		String fileType = "";
		
		/**
		 * Data of the image associated to the question 
		 */
		byte[] data = null;
		
		/**
		 * Indicates if the associated image is an already 
		 * existing image in the server
		 */
		boolean copyImage = false;
		
		/**
		 * Path to the source image, when copyImage=true
		 */
		String pathImageOrigin = "";
		
		/**
		 * Question that will be added to the question file
		 */
		Question question;
		
		/**
		 * Login of the author that updates the question
		 */
		String author;
		
		// Gets the parameters
		ObjectInputStream bufferIn = new ObjectInputStream(req.getInputStream());
		PrintWriter out = null;
		
		try {
			// Receives the parameters
			// DON'T MODIFY THE RECEPTION ORDER
			question = (Question) bufferIn.readObject();
			author = (String) bufferIn.readObject();
			
			try {
				fileType = ((String) bufferIn.readObject()).toLowerCase();

				// Gets the image data bytes
				data = new byte[bufferIn.readInt()];
				if (data.length > 0)
					bufferIn.readFully(data);
			} catch (Exception e) {
				fileType = null;
				data = null;
			}
			
			try {
				copyImage = bufferIn.readBoolean();
			} catch (java.io.IOException ioe) {
				copyImage = false;
			}

			resp.setContentType("text/plain;charset=UTF-8");
			out = resp.getWriter();

			// Checks if the question file that will be updated exists
			QuestionFile questionFile = new QuestionFile(question.getCourse(), question.getQuestionsFileName());
			if (questionFile.exists()) {
				// Checks if the associated image comes from moving an existing
				// image in the server. This happens when a question is added to a 
				// question file that is copy from another question file
				if (copyImage == true) {
					pathImageOrigin = question.getPathImage();
					int index = pathImageOrigin.lastIndexOf(".");
					question.setPathImage(pathImageOrigin.substring(index));
				}

				// Adds the received question
				int resultSave = questionFile.addQuestionToFile(question, author);
				if (resultSave >= 0) {

					// Saves the image associated to the test
					boolean checkFile = true;
					if (!copyImage) {
						// Saves the received image
						if (fileType != null && fileType.equals("") == false
								&& data != null && data.length > 0)
							checkFile = questionFile.saveImage(question.getCodeQuestion(), fileType, data);
					} else {
						// Saves an existing image
						checkFile = QuestionFile.copyImage(pathImageOrigin, question.getPathImage());
					}

					if (checkFile) {
						out.println(TestEditor.TEXT_QUESTION_SAVED);
					} else {
						out.println(TestEditor.TEXT_QUESTION_SAVED);
						out.println("But the image HAS NOT BEEN CORRECTLY STORED.");
					}

				} else if (resultSave == -1) {
					out.println("ERROR: The question couldn't be saved. Try it later.");

				} else if (resultSave == -2) {
					out.println("ERROR: This file of questions contains 200 "
											+ "questions at the moment.");
					out.println("You cannot add more questions to this file.");
					out.println("You have to create a new file.");

				} else if (resultSave == -3) {
						out.println("ERROR: The question has NOT been ADDED.");
						out.println("The question file is associated to a different ");
						out.println("concept than the question that you try to add.");
				}
			} else {
				out.println("ERROR: The question file doesn't exist.");
				out.println("Create it before adding questions again.");
			}
		} catch (Exception e) {
			resp.setContentType("text/plain;charset=UTF-8");
			out = resp.getWriter();

			out.println("ERROR: The question couldn't be saved.");
			out.println("The question data have not been received.");

		} finally {
			if (out != null)  {
				out.flush();
				out.close();
			}
		}
	}
}