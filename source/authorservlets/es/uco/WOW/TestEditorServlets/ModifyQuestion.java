package authorservlets.es.uco.WOW.TestEditorServlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uco.WOW.QuestionsFile.QuestionFile;
import es.uco.WOW.TestEditor.TestEditor;
import es.uco.WOW.Utils.Question;
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
 * NAME: ModifyQuestion 
 * FUNCTION: Receives a Question object with all the data about a question
 * and updates the question file with the data of the question received. 
 * LAST MODIFICATION: 06-02-2008
 * @author Isaac R�der Jim�nez, reviewed by Phuoc-Loc Nguyen
 */
public class ModifyQuestion extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		/**
		 * Image file type
		 */
		String fileType = "";
		
		/**
		 * Data of the image associated to the question
		 */
		byte[] data = null;

		/**
		 * Question that will be updated
		 */
		Question question;
		PrintWriter out = null;
		
		try {
			// Reads the parameters
			ObjectInputStream bufferIn = new ObjectInputStream(req.getInputStream());

			// Receives the parameters in Object format
			// DON'T ALTER THE RECEPTION ORDER
			question = (Question) bufferIn.readObject();

			try {
				fileType = ((String) bufferIn.readObject()).toLowerCase();
				// Gets the bytes of the image data
				data = new byte[bufferIn.readInt()];
				bufferIn.readFully(data);
			}
			catch (Exception ioe) {
				fileType = null;
				data = null;
			}

			resp.setContentType("text/plain;charset=UTF-8");
			out = resp.getWriter();

			// Checks if the question file that will be modified exists
			QuestionFile questionFile = new QuestionFile(question.getCourse(), question.getQuestionsFileName());
			if (questionFile.exists()) {
				// Updates the question in the file
				if (questionFile.modifyQuestion(question)) {
					out.println(TestEditor.TEXT_QUESTION_MODIFIED);
					// Updates the image data
					if (fileType != null && data != null && data.length > 0) {	
						if (questionFile.saveImage(question.getCodeQuestion(), fileType, data)) {
							out.println("Image modified");
						} else {
							out.println("But the image HAS NOT BEEN CORRECTLY STORED");
						}
					}
				} else {
					out.println("ERROR: The question HAS NOT BEEN modified");
				}
			} else {
				out.println("The question file doesn't exist");
			}

		} catch (Exception e) {
			resp.setContentType("text/plain;charset=UTF-8");
			if (out == null) {
				out = resp.getWriter();
			}
			out.println("ERROR: Question not modified.");
			out.println("The question data have not been received.");
			UtilLog.writeException(e);

		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
}