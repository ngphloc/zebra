/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.border.TitledBorder;

import vn.spring.zebra.um.TriUM;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMDlg extends JDialog implements UIDispose {
	private static final long serialVersionUID = 1L;
	protected TriUM um = null;
	protected TriUMPane umPane = null;
	
	public TriUMDlg(JFrame frmParent, TriUM um) throws Exception {
		super(frmParent, "Triangular Learner Model", true);
		this.um = um;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		String title = getMyTitle();
		setTitle(title);
		
		TitledBorder titleBorder = BorderFactory.createTitledBorder(title);
		titleBorder.setBorder(BorderFactory.createLoweredBevelBorder());
		umPane = new TriUMPane(um, frmParent);
		umPane.setBorder(titleBorder);
		
		add(umPane);
		setSize(800, 650);
		setVisible(true);
	}

	@Override
	public void dispose() {
		try {if(umPane != null) umPane.dispose();}
		catch(Exception e) {e.printStackTrace();}

		super.dispose();
	}
	
	private String getMyTitle() {
    	String username = um.toString();
    	try {
    		username = um.getProfile().getAttributeValue("personal", "name");
    	}
    	catch(Exception e) {e.printStackTrace();}
    	return "Triangular Learner Model (TLM) of " + username;
	}
}
