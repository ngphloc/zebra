/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ResourceNotFoundException.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.exceptions;

/**
 * This exception is thrown when the resource specified by a URL
 * cannot be found.
 * @see vn.spring.WOW.engine.HandlerManager#locateResource
 */
public class ResourceNotFoundException extends WOWException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ResourceNotFoundException() {super();}
    public ResourceNotFoundException(String s) {super(s);}
}