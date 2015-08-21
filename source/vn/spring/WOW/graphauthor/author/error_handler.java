/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * error_handler.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.author;

import org.xml.sax.*;


/**
 * Error_handler: default xml error handler.
 *
 */
public class error_handler implements ErrorHandler {
    private boolean errors_occurred;

    public error_handler() {
        errors_occurred = false;
    }

    public boolean getErrorsOccurred() {
        return errors_occurred;
    }

    public void error(SAXParseException e) {
        show_message("error", e);
        errors_occurred = true;
    }

    public void fatalError(SAXParseException e) {
        show_message("fatalError", e);
        errors_occurred = true;
    }

    public void warning(SAXParseException e) {
        show_message("warning", e);
    }

    private void show_message(String type, SAXParseException e) {
        e.printStackTrace();
        System.err.println("[" + type + "]: " + e.toString() + " Line: " +
                           e.getLineNumber() + " Column: " +
                           e.getColumnNumber());
    }
}