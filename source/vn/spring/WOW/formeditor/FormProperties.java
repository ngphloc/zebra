/*
   This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University
   WOW! is also open source software; 
   
 */

/**
 * FormProperties.java 1.0, August 30, 2008.
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.formeditor;

import java.net.URL;

import java.util.Vector;


/**
 * Class that contains necessary information about a form and the FormEditor, and methods to handle this info.
 *
 */
public class FormProperties {
    private String lastConcept;
    private String lastAttribute;
    private String lastType;
    private String lastDescription;
    private boolean lastChangeable;
    private boolean XMLChanged;
    private boolean Saved = false;
    private StringBuffer XMLstring = new StringBuffer("");
    private StringBuffer HTMLstring = new StringBuffer("");
    private String filename = "";
    private URL home;
    private String dirname;

    /**
     * Constructor for the FormProperties
     *
     * @param base the URL of the applet
     */
    public FormProperties(URL base) {
        home = base;
        String path = home.getPath();
        String pathttemp = path.substring(1, path.length());
        int index = pathttemp.indexOf("/");
        index++;
        dirname = path.substring(0, index);
        if (dirname.equals("/FormEditor")) {
          dirname = "";
        }
        // brendan: changed to xhtml header
        XMLstring.append("<!DOCTYPE html SYSTEM \""+dirname+"/WOWstandard/xhtml-wowext-1.dtd\">\n");
        XMLstring.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        XMLstring.append("<object data=\"../header.xhtml\" type=\"wow/text\"> </object>\n");
        XMLstring.append("<h2>title of this page</h2>\n");
        XMLstring.append("<form method=\"post\" action=\""+dirname+"/ViewGet/?handler=FormProcess\">\n");

        XMLChanged = false;
        HTMLstring.append("<html> \n <head> \n </head> \n <body> \n");
        HTMLstring.append("<h2>title of this page</h2>\n");
        HTMLstring.append("<form action=" + home.toString() + "CgiHello\" method=\"post\"> \n");

    }

    /**
     * Resets to an empty form.
     */
    public void reset() {
        filename = "";
        XMLstring = new StringBuffer("");
        HTMLstring = new StringBuffer("");

        // brendan: changed to xhtml header
        XMLstring.append("<!DOCTYPE html SYSTEM \""+dirname+"/WOWstandard/xhtml-wowext-1.dtd\">\n");
        XMLstring.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        XMLstring.append("<object data=\"../header.xhtml\" type=\"wow/text\"> </object>\n");
        XMLstring.append("<h2>title of this page</h2>\n");
        XMLstring.append("<form method=\"post\" action=\""+dirname+"/ViewGet/?handler=FormProcess\">\n");

        XMLChanged = false;

        HTMLstring.append("<html> \n <head> \n </head> \n <body> \n");
        HTMLstring.append("<h2>title of this page</h2>\n");
        HTMLstring.append("<form action=" + home.toString() + "CgiHello\" method=\"get\"> \n");
    }

    /**
     * Sets the selected concept.
     *
     * @param concept the selected concept
     */
    public void setConcept(String concept) {
        lastConcept = concept;
    }

    /**
     * Sets the selected attribute
     *
     * @param attribute the selected attribute
     */
    public void setAttribute(String attribute) {
        lastAttribute = attribute;
    }

    /**
     * Sets the type of the selected attribute
     *
     * @param type the type of the selected attribute
     */
    public void setType(String type) {
        lastType = type;
    }

    /**
     * Sets the description that belongs to the selected attribute
     *
     * @param description the description that belongs to the selected attribute
     */
    public void setDescription(String description) {
        lastDescription = description;
    }

    /**
     * Sets the variable <code>lastChangeable</code> according to the changeability of the selected attribute
     *
     * @param change the changeability of the selected attribute
     */
    public void setChangeable(boolean change) {
        lastChangeable = change;
    }

    /**
     * Sets the <code>StringBuffer</code> that contains the XML-code with the appropriate content
     *
     * @param s the String that represents the XML-code of the form
     */
    public void setXML(String s) {
        XMLChanged = true;

        StringBuffer temp = new StringBuffer(s);
        int index = temp.indexOf("</form>");
        temp.setLength(index);
        XMLstring = temp;
    }

    /**
     * Sets the <code>StringBuffer</code> that contains the HTML-code with the appropriate content
     *
     * @param s the String that represents the HTML-code of the form
     */
    public void setHTML(String s) {
        StringBuffer temp = new StringBuffer(s);
        HTMLstring = temp;
    }

    /**
     * Sets the variable <code>Saved</code to the appropriate value
     *
     * @param b the fact if the form is saved since the last changes
     */
    public void setSaved(boolean b) {
        Saved = b;
    }

    /**
     * Sets the <code>filename</code> of the form
     *
     * @param s the filename
     */
    public void setFilename(String s) {
        filename = s;
    }

    /**
     * Adds some XML-code to the buffer
     *
     * @param s the XML-code to be added
     */
    public void addXML(String s) {
        XMLstring.append(s);
    }

    /**
     * Adds some HTML-code to the buffer
     *
     * @param s the HTML-code to be added
     */
    public void addHTML(String s) {
        HTMLstring.append(s);
    }

    /**
     * Gets the last selected concept
     *
     * @return String the last selected concept
     */
    public String getConcept() {
        return lastConcept;
    }

    /**
     * Gets the last selected attribute
     *
     * @return String the last selected attribute
     */
    public String getAttribute() {
        return lastAttribute;
    }

    /**
     * Gets the type of the last selected attribute
     *
     * @return String the type of the last selected attribute
     */
    public String getType() {
        return lastType;
    }

    /**
     * Gets the description belonging to the last selected attribute
     *
     * @return String the description belonging to the last selected attribute
     */
    public String getDescription() {
        return lastDescription;
    }

    /** Gets the changeability of the last selected attribute
     *
     * @return boolean the changeability of the last selected attribute
     */
    public boolean getChangeable() {
        return lastChangeable;
    }

    /**
     * Gets the XML-code of the form
     *
     * @return String the XML-code of the form
     */
    public String getXML() {
        String temp = XMLstring.toString() + "\n</form><br></br><hr></hr><object data=\"../footer.xhtml\" type=\"wow/text\"></object>\n</html>";
        return temp;
    }

    /**
     * Gets the HTML-code of the form
     *
     * @return String the HTML-code of the form
     */
    public String getHTML() {
        return HTMLstring.toString();
    }

    /**
     * Gets the filename of the form
     *
     * @return String the filename of the form
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Says whether or not the form is changed
     *
     * @return boolean whether or not the form is changed
     */
    public boolean isXMLChanged() {
        return XMLChanged;
    }

    /**
     * Says whether or not the form is saved after the last changes
     *
     * @return boolean whether or not the form is saved after the last changes
     */
    public boolean isSaved() {
        return Saved;
    }

    /**
     * Adds the XML-code and HTML-code to the buffers for an input field
     *
     * @param name the name of the field
     * @param size the size of the field
     * @param maxlength the maximum input size of the field
     * @param def the default value for the field
     * @param descrip the description belonging to the field
     * @param usedescrip whether or not to use the description
     * @param concept the concept to which this field is connected
     * @param attr the attribute to which this field is connected
     * @param type the type of the attribute to which this field is connected
     */
    public void addInput(String name, String size, String maxlength, String def, String descrip,
        boolean usedescrip, String concept, String attr, String type) {
        int index = concept.lastIndexOf('.');
        String conceptName = concept.substring((index + 1));
        addXML("<p> " + concept + "." + attr + ": ");
        addXML("\n <input name=\"" + name + "." + conceptName + "." + attr + "\" size=\"" + size +
            "\" maxlength=\"" + maxlength + "\" type=\"" + type + "\" default=\"" + def +
            "\"></input> \n");

        if (usedescrip) {
        addXML(descrip + "\n");
        }

        addXML("</p> \n");
        setSaved(false);

    }

    /**
     * Adds the XML-code and HTML-code to the buffers for select field
     *
     * @param name the name of the field
     * @param size the size of the field
     * @param enumlist the list of values for the select field
     * @param def the default value for the field
     * @param descrip the description belonging to the field
     * @param usedescrip whether or not to use the description
     * @param concept the concept to which this field is connected
     * @param attr the attribute to which this field is connected
     * @param type the type of the attribute to which this field is connected
     */
    public void addSelect(String name, String size, String enumlist, String def, String descrip,
        boolean usedescrip, String concept, String attr, String type) {
        Vector list = new Vector();
        int previndex = 0;
        int index = enumlist.indexOf(';', previndex);

        while (index >= 0) {
            list.addElement(enumlist.substring(previndex, index));
            previndex = index + 1;
            index = enumlist.indexOf(';', previndex);
        }

        list.addElement(enumlist.substring(previndex));
        int indexDot = concept.lastIndexOf('.');
        String conceptName = concept.substring((indexDot + 1));
        addXML("<p> " + concept + "." + attr + ": ");
        addXML("\n <select name=\"" + name + "." + conceptName + "." + attr + "\" size=\"" + size +
            "\" > \n");

        for (int i = 0; i < list.size(); i++) {
            addXML("\t <option value=\"" + list.elementAt(i) + "\" name=\"" + i + "\">" + list.elementAt(i) + "</option>\n");
        }
        addXML("</select> \n");

        if (usedescrip) {
            addXML(" " + descrip + "\n");
        }
        addXML("</p> \n");
        setSaved(false);
    }

    /**
     * Adds the XML-code and HTML-code to the buffers for an option field
     *
     * @param name the name of the field
     * @param allowed the number of allowed options to be choosen
     * @param enumlist the list of values for the option field
     * @param def the default value for the field
     * @param descrip the description belonging to the field
     * @param usedescrip whether or not to use the description
     * @param concept the concept to which this field is connected
     * @param attr the attribute to which this field is connected
     * @param type the type of the attribute to which this field is connected
     */
    public void addOption(String name, String allowed, String enumlist, String def, String descrip,
        boolean usedescrip, String concept, String attr, String type) {
        Vector list = new Vector();
        int previndex = 0;
        int indexdot = concept.lastIndexOf('.');
        String conceptName = concept.substring((indexdot + 1));

        int index = enumlist.indexOf(';', previndex);

        while (index >= 0) {
            list.addElement(enumlist.substring(previndex, index));
            previndex = index + 1;
            index = enumlist.indexOf(';', previndex);
        }

        list.addElement(enumlist.substring(previndex));
        addXML("<p> " + concept + "." + attr + ": ");

        for (int i = 0; i < list.size(); i++) {
            addXML("\n <input type=");

            if (allowed.equals("1") || !(allowed.length() > 0)) {
                     addXML("\"radio\"");
            } else {
                addXML("\"checkbox\"");
            }

            if (def.equals(list.elementAt(i))) {
                addXML(" checked=\"true\" name=\"" + name + "." + conceptName + "." + attr + "\" value=\"" + list.elementAt(i) +
                    "\" >" + list.elementAt(i) + "</input>");
            } else {
                addXML(" name=\"" + name + "." + conceptName + "." + attr + "\" value=\"" + list.elementAt(i) +  "\" >" +
                    list.elementAt(i) + "</input>");
            }

        }

        if (usedescrip) {
            addXML(" " + descrip + "\n");
        }

        addXML("</p> \n");
        setSaved(false);
    }

    /**
     * Adds the XML-code and HTML-code to the buffers for a button
     *
     * @param name the name for the button
     * @param type the type (submit/reset) of the button
     */
    public void addButton(String name, String type) {
         addXML("<p><input type=\"" + type + "\" value=\"" + name + "\"> </input> \n </p>");
         setSaved(false);
    }

    /**
     * Adds the XML-code and HTML-code to the buffers for a description
     *
     * @param name the name of the description
     * @param content the content of the description
     */
    public void addDescription(String name, String content) {
        addXML("</p>" + content + "</p> \n");
         setSaved(false);
    }
}
