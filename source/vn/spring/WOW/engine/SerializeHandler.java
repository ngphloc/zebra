/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * SerializeHandler.java 1.0, May 20, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.engine;

import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.exceptions.*;

import java.io.*;
import org.w3c.dom.*;
import org.apache.xml.serialize.*;

public class SerializeHandler implements ResourceHandler {
    private static final boolean debug = true;

    public SerializeHandler() {}

    /**
     * Returns if this handler can process the given resource.
     * @param type The resource type that this handler is supposed to
     *        handle.
     * @return If this handler handles the specified resource.
     */
    public boolean handlesResource(ResourceType type) {
        String s = type.getMime();
        return ("text/xhtml".equals(s) || "text/xml".equals(s) || "application/xml".equals(s) || "application/smil".equals(s));
    }

    public static BaseMarkupSerializer getSerializer() {return new MySerializer();}
    public static BaseMarkupSerializer getSerializer(OutputFormat format) {return new MySerializer(format);}
    public static BaseMarkupSerializer getSerializer(java.io.OutputStream output, OutputFormat format) {return new MySerializer(output, format);}
    public static BaseMarkupSerializer getSerializer(java.io.Writer writer, OutputFormat format) {return new MySerializer(writer, format);}


    /**
     * Processes the resource using the user model and database and
     * returns the new resource.
     * @param resource The resource to be processed.
     * @param profile The user profile that is to be used.
     * @param db A reference to the database that is to be used.
     * @return The processed resource.
     */
    public synchronized Resource processResource(Resource resource, Profile profile, WOWDB db) throws
                    ProcessorException {
        Resource result = (Resource)resource.clone();
        try {
            // get the document
            Object data = resource.getObjectData();
            if ((data == null) || (!(data instanceof Document))) {
                result.setReady(true);
                return result;
            }
            Document doc = (Document)data;

            // write the document
            boolean html = "text/xhtml".equals(resource.getType().getMime());
            StringWriter writer = new StringWriter();
            OutputFormat of = new OutputFormat("html", "utf-8", true);
            if (html) of.setOmitDocumentType(true);
            of.setLineWidth(100);
            of.setPreserveSpace(true);
            if (html) of.setNonEscapingElements(new String[] {"style"});
            BaseMarkupSerializer ser = null;
            ser = getSerializer(writer, of);
            ser.serialize(doc);
            String output = writer.toString();
            if (html) output = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"+output;
            InputStream is = new ByteArrayInputStream(output.getBytes("utf-8"));
            result.setInputStream(is);
            result.setType(new ResourceType("text/xhtml"));
            result.setReady(true);
        } catch (Exception e) {
            if (debug) e.printStackTrace();
            String errorreport = "<html><head><title>Error loading document: "+resource.getFakeURL()+"</title></head>"+
                                 "<body><span class=\"error\"><p><h1>[Error loading document: "+resource.getFakeURL()+"]</h1></p></span>"+
                                 WOWStatic.normalize(e.getMessage())+"</body></html>";
            InputStream ais = new ByteArrayInputStream(errorreport.getBytes());
            result.setInputStream(ais);
            result.setType(new ResourceType("text/xhtml"));
            result.setReady(false);
        }
        return result;
    }
}