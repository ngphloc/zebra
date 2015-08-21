/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * InvalidProfileException.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.exceptions;

/**
 * This exception is thrown when a profile does not exist or a
 * reference is invalid.
 * @see WOWException
 */
public class InvalidProfileException extends WOWException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public InvalidProfileException() {super();}
    public InvalidProfileException(String s) {super(s);}
}
