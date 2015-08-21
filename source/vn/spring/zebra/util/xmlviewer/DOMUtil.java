package vn.spring.zebra.util.xmlviewer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class DOMUtil { 
    public static Document createDocument(InputSource is) throws Exception{ 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        factory.setNamespaceAware(true);
        factory.setIgnoringElementContentWhitespace(true); 
        factory.setCoalescing(true); 
        DocumentBuilder builder = factory.newDocumentBuilder(); 
        return builder.parse(is); 
    }
    
    public static Document createDocument2(InputSource is) throws Exception{ 
        SAXParserFactory saxFactory = SAXParserFactory.newInstance(); 
        saxFactory.setNamespaceAware(true);
        SAXParser parser = saxFactory.newSAXParser(); 
        XMLReader reader = new XMLTrimFilter(parser.getXMLReader()); 
 
        TransformerFactory factory = TransformerFactory.newInstance(); 
        Transformer transformer = factory.newTransformer(); 
        transformer.setOutputProperty(OutputKeys.INDENT, "no"); 
        DOMResult result = new DOMResult(); 
        transformer.transform(new SAXSource(reader, is), result); 
        return (Document)result.getNode(); 
    } 
} 


