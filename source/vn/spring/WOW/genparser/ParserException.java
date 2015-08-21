/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ParserException.java 1.0, December 7, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.genparser;

/**
 * This exception is thrown when there is some semantic error in an
 * expression.
 */
public class ParserException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object info = null;
    public ParserException() {super();}
    public ParserException(String s) {super(s);}
    public ParserException(String s, Object info) {super(s); this.info = info;}
    public Object getInfo() {return info;}
}
