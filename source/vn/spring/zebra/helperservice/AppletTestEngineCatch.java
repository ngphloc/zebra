/**
 * 
 */
package vn.spring.zebra.helperservice;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.spring.zebra.evaluation.TestEngineEvent;
import vn.spring.zebra.evaluation.TestEngineListener;
import vn.spring.zebra.server.TriUMServer;
import vn.spring.zebra.um.TriUM;

import es.uco.WOW.Utils.Student;

/**
 * @author Phuoc - Loc Nguyen
 *
 */
public class AppletTestEngineCatch extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		ObjectInputStream objectInputStream = new ObjectInputStream(req.getInputStream());
		try {
			Student student = (Student)objectInputStream.readObject();
			String userid = student.getLogin();
			String course = student.getCourse();
			TriUM um = TriUMServer.getInstance().getUM(userid, course, true);
			
			TestEngineListener staticListener = um.getZebraNetworker().getStaticKnowledgeDaemon(userid, course);
			staticListener.testEvaluated(new TestEngineEvent(student, course));

			TestEngineListener dynListener = um.getZebraNetworker().getDynKnowledgeDaemon(userid, course);
			dynListener.testEvaluated(new TestEngineEvent(student, course));
		}
		catch(ClassNotFoundException e) {
			throw new IOException("ObjectInputStream.readObject causes error: " + e.getMessage());
		}
		catch(Exception e) {
			throw new IOException("doPost causes error: " + e.getMessage());
		}
		finally {
			objectInputStream.close();
		}
	}

}
