package vn.spring.zebra.collab;

import javax.swing.JApplet;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CollabApplet extends JApplet
{
	private static final long serialVersionUID = 1L;
	
	public void init() {
		String host = getParameter( "host" );
		int port = Integer.parseInt( getParameter( "port" ) );
		String userid = getParameter( "userid" );
		
		String sGroup = getParameter("group");
		String[] aGroup = sGroup.split(",");
		ArrayList<String> group = new ArrayList<String>();
		for(int i = 0; i < aGroup.length; i++) {
			group.add(aGroup[i]);
		}
		
		setLayout( new BorderLayout() );
		add(new CollabClient(host, port, userid, group), BorderLayout.CENTER);
	}
}
