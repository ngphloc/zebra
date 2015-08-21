package vn.spring.zebra.um.gui;

import javax.swing.*;

import vn.spring.zebra.um.OverlayBayesUM;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class BayesAuthor extends JApplet {
	private static final long serialVersionUID = 1L;
	OverlayBayesInspector inspector = null;
	public void init() {
		try {
			inspector = new OverlayBayesInspector(getCodeBase(), OverlayBayesUM.OBUM_DEFAULT_BAYESNET_TYPE);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.out.println(e.getMessage());
		}
	}

}
