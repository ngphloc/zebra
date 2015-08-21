package vn.spring.zebra.helperservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vn.spring.zebra.client.*;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public class TriUMQueryService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		try {
			TriUMQuery query = TriUMQueryClientUtil.getTriUMQuery(TriUMQuery.RMI_DEFAULT_QUERY_HOST, TriUMQuery.RMI_QUERY_PORT);
			TriUMQueryArg arg = parseQueryArgs(req);
			TriUMQueryResult result = query.query(arg);
			resp.setContentType("text/plain;charset=UTF-8");
			PrintWriter out = resp.getWriter();
			out.print(result.toString());
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}
	public static TriUMQueryArg parseQueryArgs(HttpServletRequest req) throws Exception {
		HashMap<String, String> argMap = new HashMap<String, String>();
		
		String type = req.getParameter("type");         if(type != null)   argMap.put("type", type);
		String userid = req.getParameter("userid");     if(userid != null) argMap.put("userid", userid);
		String password = req.getParameter("password"); if(userid != null) argMap.put("password", password);
		String course = req.getParameter("course");     if(course != null) argMap.put("course", course);
		String concept = req.getParameter("concept");   if(concept != null) argMap.put("concept", concept);
		String subject = req.getParameter("subject");   if(subject != null) argMap.put("subject", subject);
		String topic = req.getParameter("topic");       if(topic != null) argMap.put("topic", topic);
		
		return TriUMQueryArg.createQueryArg(argMap);
	}
}
