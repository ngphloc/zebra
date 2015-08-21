package org.apache.xml.serialize;

import org.w3c.dom.*;

import java.io.*;

public class MySerializer extends XHTMLSerializer {
    public MySerializer() {super();}
    public MySerializer(OutputFormat format) {super(format);}
    public MySerializer(java.io.OutputStream output, OutputFormat format) {super(output, format);}
    public MySerializer(java.io.Writer writer, OutputFormat format) {super(writer, format);}

    protected void characters(String text) throws IOException {
        ElementState state = content();
        state.doCData = false;
        super.characters(text);
    }

    protected void serializeNode(Node node) throws IOException {
        if (node.getNodeType() == Node.ENTITY_REFERENCE_NODE) {
            endCDATA();
            content();
            printText("&"+node.getNodeName()+";", true, true);
        } else {
            super.serializeNode(node);
        }
    }
}
