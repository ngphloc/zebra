/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * KnowledgeConfig.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.engine;

import java.io.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.WOWDB.*;
import java.util.Vector;
/**
 * The class KnowledgeConfig is a servlet that lets the user
 * change his/her knowledge about some concepts. The concepts that
 * a user may change are specified by the author of the course.
 */

public class KnowledgeConfig {

    public Profile profile;
    public String course;
    private StringBuffer sb;

    /**
     * The actual generation of the HTML form code.
     * //@param ChangeCgi The action of the form that must be generated.
     */
    public Resource genKnowledgeList(Profile profile, String login, String course) throws DatabaseException, InvalidConceptException, InvalidAttributeException {

        this.profile = profile;
        String ChangeCgi = "?handler=KnowledgeChange";
        sb = new StringBuffer();
    // The header generation is "borrowed" from XMLHandler
    sb.append("<html><body>");

        sb.append("<h2>Knowledge of Concepts</h2>");
        sb.append("<p>Please mark concepts you know about, and unmark concepts the system mistakenly assumes knowledge about.</p>\n");
        sb.append("<form method=\"POST\" action=\"" + ChangeCgi + "\">");

        boolean messageemptylist = true;
        WOWDB db = WOWStatic.DB();
        ConceptDB cdb = db.getConceptDB();
        Vector concepts = cdb.getConceptList();
        Vector v = new Vector();
        for (int i=0;i<concepts.size();i++) {
            String conceptname = (String)concepts.get(i);
            String prefix = conceptname.substring(0, conceptname.indexOf("."));
            Vector attributes = cdb.getAttributes(cdb.findConcept(conceptname));
                for (int j=0;j<attributes.size();j++) {
                    Attribute attr = (Attribute)attributes.get(j);
                    if (attr.getName().equals("knowledge") && !attr.isReadonly() && prefix.indexOf(course)>-1) {
                        v.addElement(conceptname);
                        messageemptylist = false;
                    }
                }


        }
        if (messageemptylist) {
            sb.append("<p>Sorry, the author does <b>not</b> allow any changes to knowledge by the user.</p>");
        }
        else {
            Object[] names = WOWStatic.names(v);
            sb.append("<table>");
            for (int i=0; i<names.length; i++) {
                String conceptname = names[i].toString();
                String knowledgeValue = null;
                try {
                    knowledgeValue = profile.getAttributeValue(conceptname, "knowledge");
                } catch (Exception e) {}
		Concept concept = cdb.getConcept(cdb.findConcept(conceptname));
                if (knowledgeValue == null) {
                    String knowledgeDefault = null;
                    knowledgeDefault = concept.getAttribute("knowledge").getDefault();
                    if (knowledgeDefault == null) knowledgeDefault = "0";
                    knowledgeValue = knowledgeDefault;
                }
		String desc = concept.getDescription();
		if (desc == null)
		    desc = "no description";
                sb.append("<tr>\n<td valign=\"BOTTOM\">\n<input type=\"TEXT\" name=\"KNOW_" + conceptname + "\" value="+knowledgeValue+"> " + conceptname.substring(conceptname.indexOf(".")+1) + " (" + desc + ")</td>");
                sb.append("<td valign=\"BOTTOM\">" + "</td>\n</tr>");
            }
            sb.append("</table>");
            sb.append("<input type=\"SUBMIT\" value=\"Effectuate Changes\">");
            sb.append("<input type=\"RESET\" value=\"Reset to Previous Settings\">");
        }
        sb.append("</form>\n");
        sb.append("</body>\n</html>\n");
        InputStream insb = new ByteArrayInputStream(sb.toString().getBytes());
        return new Resource(insb, new ResourceType("text/html"));
    }
}
