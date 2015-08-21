package vn.spring.zebra.helperservice;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import vn.spring.zebra.server.TriUMServer;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class WOWContextListener implements ServletContextListener {
    protected static WOWContextListener instance = null;
    protected TriUMServer triUMserver = null;
    
	public void contextInitialized(ServletContextEvent event) {
		try {
			instance = this;
			instance.triUMserver = TriUMServer.getInstance();
		}
		catch(Exception e) {
			System.out.println("WOWContextListener.contextInitialized->TriUMServer.getInstance() causes error: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("WOW context initialized");
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		instance.triUMserver.onDestroyed();
		instance.triUMserver = null;
		instance = null;
		System.out.println("WOW context destroyedd");
	}
	
	public static WOWContextListener getInstance() {return instance;}
	
	public TriUMServer getTriUMServer() {return triUMserver;}
}

