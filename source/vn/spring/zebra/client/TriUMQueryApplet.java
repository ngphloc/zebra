/**
 * 
 */
package vn.spring.zebra.client;

import java.awt.BorderLayout;

import javax.swing.JApplet;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMQueryApplet extends JApplet {
	private static final long serialVersionUID = 1L;
	
	public void init() {
		setLayout( new BorderLayout() );
		String userid = getParameter( "userid" );
		if(userid != null) userid = userid.trim();
		if(userid.length() == 0) userid = null;
		
		add(new TriUMQueryGUI(userid), BorderLayout.CENTER);
	}

}
