/*

    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 

*/
/**
 * ChangeDocumentListener.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.generatelisteditor;

import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

public class ChangeDocumentListener implements DocumentListener
{

	private JProtectedPanel fPp;

	public ChangeDocumentListener (JProtectedPanel pp)
	{
		fPp = pp;
	}

	/**
	 * Called when the style of some of the text in the listened-to document changes. This sort of event is fired only from a StyledDocument -- a PlainDocument does not fire these events.
	 */
	public void changedUpdate(DocumentEvent de)
	{
		fPp.setChanged();
	}


	/**
	 * Called when text is inserted into the listened-to document.
	 *
	 * @param   de
	 */
	public void insertUpdate(DocumentEvent de)
	{
		fPp.setChanged();
	}



	/**
	 * Called when text is removed from the listened-to document.
	 *
	 * @param   de
	 */
	public void removeUpdate(DocumentEvent de)
	{
		fPp.setChanged();
	}
}