package vn.spring.zebra.helperservice;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import vn.spring.zebra.ZebraStatic;


/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class WOWSessionListener implements HttpSessionListener {
	public WOWSessionListener() {
	}
	
	public void sessionCreated(HttpSessionEvent event) {
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		try {
			HttpSession session = event.getSession();
			if(session == null) return;
			String userid = (String)session.getAttribute("login");
			String course = (String)session.getAttribute("course");
			
			if(userid == null || course == null) {
				System.out.println("User has already logged off");
				return;
			}
			ZebraStatic.logoff(session);
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
