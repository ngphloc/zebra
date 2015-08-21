/*

    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 

*/
/**
 * JProtectedPanel.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.generatelisteditor;

import javax.swing.JPanel;
import javax.swing.JOptionPane;

abstract class JProtectedPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	protected boolean isChanged = false;

        /**
         * tests if the data is changed, and if it's changed, it will ask the
         * user if he wants to store the data to memory.
         */
	public void isSaved()
	{
		// is the data in the panel Changed ?
		if (isChanged)
		{
			// Must we store or not ?
			if (AskToStore())
			{
				save();
			}
		}
	}

        /**
         * this function creates a confirmation dialog to ask if the user wants
         * to store the data to memory or not.
         *
         * @return  the boolean value with the answer of the user.
         */
	private boolean AskToStore()
	{
		int result = JOptionPane.showConfirmDialog(this, "Data is changed.\n Do you want to commit these changes to memory? ", "Store Data?", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION)
		{
			return true;
		} else if (result == JOptionPane.NO_OPTION)
		{
			return false;
		}
		JOptionPane.showMessageDialog(this, "unexpected error encountered !");
		return false;
	}


	protected void setChanged()
	{
		isChanged = true;
	}

	abstract void save();
}