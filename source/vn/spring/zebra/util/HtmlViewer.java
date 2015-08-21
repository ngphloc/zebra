/**
 * 
 */
package vn.spring.zebra.util;

import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class HtmlViewer extends JEditorPane implements HyperlinkListener {
	private static final long serialVersionUID = 1L;
	
	protected HTMLEditorKit htmlKit = null;
	protected HTMLDocument doc = null;
	
	public HtmlViewer() {
		super();
		this.htmlKit = new HTMLEditorKit();
		this.doc = (HTMLDocument)this.htmlKit.createDefaultDocument();
        this.setEditorKit(htmlKit);
        this.setDocument(doc);
        this.setEditable(false);
        this.addHyperlinkListener(this);
	}
	public HtmlViewer(String htmlData) {
		this();
		setText(htmlData);
        this.setCaretPosition(0);
	}
	public HtmlViewer(URL url) {
		this();
		try {
			setPage(url);
		}
		catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Load Page Error in HtmlViewer: " + e.getMessage(), 
					"HtmlViewer Load Page Error", JOptionPane.ERROR_MESSAGE);
		}
        this.setCaretPosition(0);
	}
	public void addCSSEntry(String entry) {
		//For example: addCSSEntry("body {color:#000; font-family:times; margin: 4px; }")
		htmlKit.getStyleSheet().addRule(entry);
	}
	
	public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (e instanceof HTMLFrameHyperlinkEvent) {
                HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
                HTMLDocument doc = (HTMLDocument)this.getDocument();
                doc.processHTMLFrameHyperlinkEvent(evt);
            }
            else {
                try {
                	URL url = e.getURL();
                	if(url != null) this.setPage(url);
                }
                catch(Throwable t) {
                    t.printStackTrace();
                }
            }
        }
	}
	
}
