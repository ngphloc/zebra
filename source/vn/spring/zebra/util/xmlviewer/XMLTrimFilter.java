package vn.spring.zebra.util.xmlviewer;

import java.io.CharArrayWriter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class XMLTrimFilter extends XMLFilterImpl{ 
 
    private CharArrayWriter contents = new CharArrayWriter(); 
 
    public XMLTrimFilter(XMLReader parent){ 
        super(parent); 
    } 
 
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException{ 
        writeContents(); 
        super.startElement(uri, localName, qName, atts); 
    } 
 
    public void characters(char ch[], int start, int length){ 
        contents.write(ch, start, length); 
    } 
 
    public void endElement(String uri, String localName, String qName) throws SAXException{ 
        writeContents(); 
        super.endElement(uri, localName, qName); 
    } 
 
    public void ignorableWhitespace(char ch[], int start, int length){} 
 
    private void writeContents() throws SAXException{ 
        char ch[] = contents.toCharArray(); 
        if(!isWhiteSpace(ch)) 
            super.characters(ch, 0, ch.length); 
        contents.reset(); 
    } 
 
    private boolean isWhiteSpace(char ch[]){ 
        for(int i = 0; i<ch.length; i++){ 
            if(!Character.isWhitespace(ch[i])) 
                return false; 
        } 
        return true; 
    } 
} 
