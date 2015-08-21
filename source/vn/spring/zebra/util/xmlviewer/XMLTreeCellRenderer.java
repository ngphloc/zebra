package vn.spring.zebra.util.xmlviewer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class XMLTreeCellRenderer extends DefaultTreeCellRenderer{ 
	private static final long serialVersionUID = 1L;
	
	Color elementColor = new Color(0, 0, 128); 
    Color attributeColor = new Color(0, 128, 0); 
 
    public XMLTreeCellRenderer(){ 
        setOpenIcon(null); 
        setClosedIcon(null); 
        setLeafIcon(null); 
    } 
 
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus){ 
        Node node = (Node)value; 
        switch(node.getNodeType()){ 
            case Node.ELEMENT_NODE: 
                value = '<'+node.getNodeName()+'>'; 
                break; 
            case Node.ATTRIBUTE_NODE: 
                value = '@'+node.getNodeName(); 
                break; 
            case Node.TEXT_NODE: 
                value = node.getNodeValue(); 
                break; 
            case Node.COMMENT_NODE: 
                value = "<!--"+node.getNodeValue()+"-->"; 
                break; 
            case Node.DOCUMENT_TYPE_NODE: 
                DocumentType dtype = (DocumentType)node; 
                value = "<"+"!DOCTYPE " + dtype.getName() + '>'; 
                break; 
            default: 
                value = node.getNodeName(); 
        } 
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus); 
        if(!selected){ 
            switch(node.getNodeType()){ 
                case Node.ELEMENT_NODE: 
                    setForeground(elementColor); 
                    break; 
                case Node.ATTRIBUTE_NODE: 
                    setForeground(attributeColor); 
                    break; 
            } 
        } 
        return this; 
    } 
} 
