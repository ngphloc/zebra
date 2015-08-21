package vn.spring.zebra.util.xmlviewer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.w3c.dom.Node;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class XMLTreeTableCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;
	
	public static final Color elementColor = new Color(0, 0, 128);
	public static final Color attributeColor = new Color(0, 128, 0);
 
    public XMLTreeTableCellRenderer(){
        setOpenIcon(null);
        setClosedIcon(null);
        setLeafIcon(null);
    }
 
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus){
        Node node = (Node)value;
        switch(node.getNodeType()){
            case Node.ELEMENT_NODE:
            	{
                	Node id = node.getAttributes().getNamedItem("id");
                	if(id != null)
	                    value = "<" + node.getNodeName() + " id=" + id.getNodeValue() + ">";
	            	else
	            		value = '<'+node.getNodeName()+'>';
            	}
                break;
            case Node.ATTRIBUTE_NODE:
            	value = '@'+node.getNodeName();
                break;
            case Node.TEXT_NODE:
                value = "# text";
                break;
            case Node.COMMENT_NODE:
                value = "# comment";
                break;
            case Node.DOCUMENT_TYPE_NODE:
                //DocumentType dtype = (DocumentType)node;
                value = "# doctype";
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
