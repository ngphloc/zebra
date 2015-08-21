package vn.spring.WOW.strategieseditor.core;

import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

// Referenced classes of package vn.spring.WOW.strategieseditor.core:
//            ShowLink

public class Strategy
{

    private Document strategyRepresentation;
    private ArrayList textLinesToNodes;

    public Strategy()
        throws Exception
    {
        strategyRepresentation = null;
        textLinesToNodes = null;
        DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
        documentbuilderfactory.setValidating(true);
        DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
        DocumentType documenttype = documentbuilder.getDOMImplementation().createDocumentType("strategy", "", "strategy.dtd");
        strategyRepresentation = documentbuilder.getDOMImplementation().createDocument("", documenttype.getName(), documenttype);
    }

    public void setName(String s)
    {
        Element element = (Element)strategyRepresentation.getElementsByTagName("strategy").item(0);
        element.setAttribute("name", s);
    }

    public void setDescription(String s)
    {
        Element element = (Element)strategyRepresentation.getElementsByTagName("strategy").item(0);
        Object obj = strategyRepresentation.getElementsByTagName("description").item(0);
        if(obj == null)
        {
            obj = strategyRepresentation.createElement("description");
            ((Node) (obj)).appendChild(strategyRepresentation.createTextNode(s));
            if(element.getFirstChild() != null)
                element.insertBefore(((Node) (obj)), element.getFirstChild());
            else
                element.appendChild(((Node) (obj)));
        } else
        {
            ((Node) (obj)).getFirstChild().setNodeValue(s);
        }
    }

    public void setDOMRepresentation(Document document)
    {
        strategyRepresentation = document;
    }

    public Document getDOMRepresentation()
    {
        return strategyRepresentation;
    }

    public String getName()
    {
        return strategyRepresentation.getElementsByTagName("strategy").item(0).getAttributes().getNamedItem("name").getNodeValue();
    }

    public String getDescription()
    {
        return strategyRepresentation.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();
    }

    public String serializeForTextArea()
    {
        String s = "";
        textLinesToNodes = new ArrayList();
        NodeList nodelist = strategyRepresentation.getElementsByTagName("if");
        for(int i = 0; i < nodelist.getLength(); i++)
        {
            Node node = nodelist.item(i);
            textLinesToNodes.add(node);
            s = (new StringBuilder()).append(s).append("IF ").toString();
            Node node1 = ((Element)node).getElementsByTagName("condition").item(0);
            s = (new StringBuilder()).append(s).append("(").append(node1.getFirstChild().getNodeValue()).append(")").toString();
            s = (new StringBuilder()).append(s).append("\n").toString();
            Node node2 = ((Element)node).getElementsByTagName("then").item(0);
            if(node2 != null)
            {
                textLinesToNodes.add(node2);
                s = (new StringBuilder()).append(s).append("THEN\n").toString();
                s = (new StringBuilder()).append(s).append(serializeThenStatement((Element)node2)).toString();
            }
            Node node3 = ((Element)node).getElementsByTagName("else").item(0);
            if(node3 != null)
            {
                textLinesToNodes.add(node3);
                s = (new StringBuilder()).append(s).append("ELSE\n").toString();
                s = (new StringBuilder()).append(s).append(serializeThenStatement((Element)node3)).toString();
            }
        }

        return s;
    }

    private String serializeThenStatement(Element element)
    {
        String s = "";
        NodeList nodelist = element.getElementsByTagName("action");
        for(int i = 0; i < nodelist.getLength(); i++)
        {
            s = (new StringBuilder()).append(s).append("ACTION ").toString();
            s = (new StringBuilder()).append(s).append(serializeActionStatement((Element)nodelist.item(i))).toString();
        }

        Node node = element.getElementsByTagName("select").item(0);
        if(node != null)
        {
            s = (new StringBuilder()).append(s).append("SELECT ").toString();
            s = (new StringBuilder()).append(s).append(serializeSelectStatement((Element)node)).toString();
        }
        NodeList nodelist1 = element.getElementsByTagName("set");
        for(int j = 0; j < nodelist1.getLength(); j++)
        {
            s = (new StringBuilder()).append(s).append("SET ").toString();
            s = (new StringBuilder()).append(s).append(serializeSetStatement((Element)nodelist1.item(j))).toString();
        }

        Node node1 = element.getElementsByTagName("sort").item(0);
        if(node1 != null)
        {
            s = (new StringBuilder()).append(s).append("SORT ").toString();
            s = (new StringBuilder()).append(s).append(serializeSortStatement((Element)node1)).toString();
        }
        return s;
    }

    private String serializeSelectStatement(Element element)
    {
        String s = "";
        textLinesToNodes.add(element);
        NodeList nodelist = element.getElementsByTagName("showContent");
        s = (new StringBuilder()).append(s).append("(").toString();
        for(int i = 0; i < nodelist.getLength(); i++)
        {
            Node node = nodelist.item(i);
            if(i != 0)
                s = (new StringBuilder()).append(s).append(",").toString();
            s = (new StringBuilder()).append(s).append(node.getFirstChild().getNodeValue()).toString();
        }

        s = (new StringBuilder()).append(s).append("), ").toString();
        NodeList nodelist1 = element.getElementsByTagName("showContentDefault");
        s = (new StringBuilder()).append(s).append("(").toString();
        for(int j = 0; j < nodelist1.getLength(); j++)
        {
            Node node1 = nodelist1.item(j);
            if(j != 0)
                s = (new StringBuilder()).append(s).append(",").toString();
            s = (new StringBuilder()).append(s).append(node1.getFirstChild().getNodeValue()).toString();
        }

        s = (new StringBuilder()).append(s).append("), ").toString();
        NodeList nodelist2 = element.getElementsByTagName("showLink");
        s = (new StringBuilder()).append(s).append("(").toString();
        for(int k = 0; k < nodelist2.getLength(); k++)
        {
            Node node2 = nodelist2.item(k);
            if(k != 0)
                s = (new StringBuilder()).append(s).append(",").toString();
            s = (new StringBuilder()).append(s).append("(").toString();
            Node node3 = ((Element)node2).getElementsByTagName("linkTo").item(0);
            Node node4 = ((Element)node2).getElementsByTagName("comment").item(0);
            s = (new StringBuilder()).append(s).append(node3.getFirstChild().getNodeValue()).toString();
            s = (new StringBuilder()).append(s).append(", ").toString();
            s = (new StringBuilder()).append(s).append(node4.getFirstChild().getNodeValue()).toString();
            s = (new StringBuilder()).append(s).append(")").toString();
        }

        s = (new StringBuilder()).append(s).append(")\n").toString();
        return s;
    }

    private String serializeSortStatement(Element element)
    {
        String s = "";
        textLinesToNodes.add(element);
        NodeList nodelist = element.getElementsByTagName("linkTo");
        s = (new StringBuilder()).append(s).append("(").toString();
        for(int i = 0; i < nodelist.getLength(); i++)
        {
            Node node = nodelist.item(i);
            if(i != 0)
                s = (new StringBuilder()).append(s).append(",").toString();
            s = (new StringBuilder()).append(s).append(node.getFirstChild().getNodeValue()).toString();
        }

        s = (new StringBuilder()).append(s).append(")\n").toString();
        return s;
    }

    private String serializeSetStatement(Element element)
    {
        String s = "";
        textLinesToNodes.add(element);
        NodeList nodelist = element.getElementsByTagName("expression");
        s = (new StringBuilder()).append(s).append("(").toString();
        s = (new StringBuilder()).append(s).append(nodelist.item(0).getFirstChild().getNodeValue()).toString();
        s = (new StringBuilder()).append(s).append(")\n").toString();
        return s;
    }

    private String serializeActionStatement(Element element)
    {
        String s = "";
        textLinesToNodes.add(element);
        NodeList nodelist = element.getElementsByTagName("UMvariable");
        s = (new StringBuilder()).append(s).append(nodelist.item(0).getFirstChild().getNodeValue()).toString();
        s = (new StringBuilder()).append(s).append(", ").toString();
        NodeList nodelist1 = element.getElementsByTagName("expression");
        s = (new StringBuilder()).append(s).append("(").toString();
        s = (new StringBuilder()).append(s).append(nodelist1.item(0).getFirstChild().getNodeValue()).toString();
        s = (new StringBuilder()).append(s).append(")\n").toString();
        return s;
    }

    public void deleteStatementAtLine(int i)
    {
        Node node = (Node)textLinesToNodes.get(i);
        node.getParentNode().removeChild(node);
    }

    public void addSelectStatement(int i, String s, ArrayList arraylist, ArrayList arraylist1, ArrayList arraylist2)
        throws Exception
    {
        Node node = getThenElseNode(i);
        Element element = strategyRepresentation.createElement("select");
        element.setAttribute("attributeName", s);
        for(int j = 0; j < arraylist.size(); j++)
        {
            Element element1 = strategyRepresentation.createElement("showContent");
            element1.appendChild(strategyRepresentation.createTextNode((String)arraylist.get(j)));
            element.appendChild(element1);
        }

        for(int k = 0; k < arraylist1.size(); k++)
        {
            Element element2 = strategyRepresentation.createElement("showContentDefault");
            element2.appendChild(strategyRepresentation.createTextNode((String)arraylist1.get(k)));
            element.appendChild(element2);
        }

        for(int l = 0; l < arraylist2.size(); l++)
        {
            Element element3 = strategyRepresentation.createElement("showLink");
            Element element4 = strategyRepresentation.createElement("linkTo");
            element4.appendChild(strategyRepresentation.createTextNode(((ShowLink)arraylist2.get(l)).getLinkTo()));
            element3.appendChild(element4);
            Element element5 = strategyRepresentation.createElement("comment");
            element5.appendChild(strategyRepresentation.createTextNode(((ShowLink)arraylist2.get(l)).getComment()));
            element3.appendChild(element5);
            element.appendChild(element3);
        }

        node.appendChild(element);
    }

    private Node getThenElseNode(int i)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(!node.getNodeName().equals("then") && !node.getNodeName().equals("else"))
        {
            node = node.getParentNode();
            if(!node.getNodeName().equals("then") && !node.getNodeName().equals("else"))
                throw new Exception("SELECT statement can be added only to THEN or ELSE statements!");
        }
        return node;
    }

    public void addSortStatement(int i, String s, ArrayList arraylist)
        throws Exception
    {
        Node node = getThenElseNode(i);
        Element element = strategyRepresentation.createElement("sort");
        element.setAttribute("attributeName", s);
        for(int j = 0; j < arraylist.size(); j++)
        {
            Element element1 = strategyRepresentation.createElement("linkTo");
            element1.appendChild(strategyRepresentation.createTextNode((String)arraylist.get(j)));
            element.appendChild(element1);
        }

        node.appendChild(element);
    }

    public void addIfStatement(int i, String s)
    {
        Element element = strategyRepresentation.createElement("if");
        if(i == -1)
        {
            Element element1 = (Element)strategyRepresentation.getElementsByTagName("strategy").item(0);
            element1.appendChild(element);
        } else
        {
            Node node;
            for(node = (Node)textLinesToNodes.get(i); node.getNodeName() != "if"; node = node.getParentNode());
            if(node.getNextSibling() == null)
                node.getParentNode().appendChild(element);
            else
                node.getParentNode().insertBefore(element, node.getNextSibling());
        }
        Element element2 = strategyRepresentation.createElement("condition");
        element.appendChild(element2);
        element2.appendChild(strategyRepresentation.createTextNode(s));
    }

    public void addThenStatement(int i)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "if")
        {
            throw new Exception("THEN statement can be added only to IF statement!");
        } else
        {
            Element element = strategyRepresentation.createElement("then");
            node.appendChild(element);
            return;
        }
    }

    public void addElseStatement(int i)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "if")
        {
            throw new Exception("ELSE statement can be added only to IF statement!");
        } else
        {
            Element element = strategyRepresentation.createElement("else");
            node.appendChild(element);
            return;
        }
    }

    public void addSetStatement(int i, String s, String s1)
        throws Exception
    {
        Node node = getThenElseNode(i);
        Element element = strategyRepresentation.createElement("set");
        element.setAttribute("attributeName", s);
        Element element1 = strategyRepresentation.createElement("expression");
        element1.appendChild(strategyRepresentation.createTextNode(s1));
        element.appendChild(element1);
        node.appendChild(element);
    }

    public void addActionStatement(int i, String s, String s1, String s2)
        throws Exception
    {
        Node node = getThenElseNode(i);
        Element element = strategyRepresentation.createElement("action");
        element.setAttribute("attributeName", s);
        Element element1 = strategyRepresentation.createElement("UMvariable");
        element1.appendChild(strategyRepresentation.createTextNode(s1));
        element.appendChild(element1);
        Element element2 = strategyRepresentation.createElement("expression");
        element2.appendChild(strategyRepresentation.createTextNode(s2));
        element.appendChild(element2);
        node.appendChild(element);
    }

    public String getIfCondition(int i)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "if")
        {
            throw new Exception("getIfCondition can be called only on IF node!");
        } else
        {
            Node node1 = ((Element)node).getElementsByTagName("condition").item(0);
            return node1.getFirstChild().getNodeValue();
        }
    }

    public void setIfCondition(int i, String s)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "if")
        {
            throw new Exception("setIfCondition can be called only on IF node!");
        } else
        {
            Node node1 = ((Element)node).getElementsByTagName("condition").item(0);
            node1.getFirstChild().setNodeValue(s);
            return;
        }
    }

    public String getStatementType(int i)
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() == "if")
            return "if";
        if(node.getNodeName() == "action")
            return "action";
        if(node.getNodeName() == "set")
            return "set";
        if(node.getNodeName() == "sort")
            return "sort";
        if(node.getNodeName() == "select")
            return "select";
        else
            return "";
    }

    public String getActionAttr(int i)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "action")
            throw new Exception("getActionAttr can be called only on ACTION node!");
        else
            return ((Element)node).getAttribute("attributeName");
    }

    public String getActionUMVariable(int i)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "action")
        {
            throw new Exception("getActionAttr can be called only on ACTION node!");
        } else
        {
            Node node1 = ((Element)node).getElementsByTagName("UMvariable").item(0);
            return node1.getFirstChild().getNodeValue();
        }
    }

    public String getActionExpr(int i)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "action")
        {
            throw new Exception("getActionExpr can be called only on ACTION node!");
        } else
        {
            Node node1 = ((Element)node).getElementsByTagName("expression").item(0);
            return node1.getFirstChild().getNodeValue();
        }
    }

    public void setActionAttr(int i, String s)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "action")
        {
            throw new Exception("setActionAttr can be called only on ACTION node!");
        } else
        {
            ((Element)node).setAttribute("attributeName", s);
            return;
        }
    }

    public void setActionUMVariable(int i, String s)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "action")
        {
            throw new Exception("setActionUMVariable can be called only on ACTION node!");
        } else
        {
            Node node1 = ((Element)node).getElementsByTagName("UMvariable").item(0);
            node1.getFirstChild().setNodeValue(s);
            return;
        }
    }

    public void setActionExpr(int i, String s)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "action")
        {
            throw new Exception("setActionExpr can be called only on ACTION node!");
        } else
        {
            Node node1 = ((Element)node).getElementsByTagName("expression").item(0);
            node1.getFirstChild().setNodeValue(s);
            return;
        }
    }

    public String getSetAttr(int i)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "set")
            throw new Exception("getSetAttr can be called only on SET node!");
        else
            return ((Element)node).getAttribute("attributeName");
    }

    public String getSetExpr(int i)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "set")
        {
            throw new Exception("getSetExpr can be called only on SET node!");
        } else
        {
            Node node1 = ((Element)node).getElementsByTagName("expression").item(0);
            return node1.getFirstChild().getNodeValue();
        }
    }

    public void setSetAttr(int i, String s)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "set")
        {
            throw new Exception("setSetAttr can be called only on SET node!");
        } else
        {
            ((Element)node).setAttribute("attributeName", s);
            return;
        }
    }

    public void setSetExpr(int i, String s)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "set")
        {
            throw new Exception("setSetExpr can be called only on SET node!");
        } else
        {
            Node node1 = ((Element)node).getElementsByTagName("expression").item(0);
            node1.getFirstChild().setNodeValue(s);
            return;
        }
    }

    public String getSortAttr(int i)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "sort")
            throw new Exception("getSortAttr can be called only on SORT node!");
        else
            return ((Element)node).getAttribute("attributeName");
    }

    public void setSortAttr(int i, String s)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "sort")
        {
            throw new Exception("setSortAttr can be called only on SORT node!");
        } else
        {
            ((Element)node).setAttribute("attributeName", s);
            return;
        }
    }

    public String getSelectAttr(int i)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "select")
            throw new Exception("getSelectAttr can be called only on SELECT node!");
        else
            return ((Element)node).getAttribute("attributeName");
    }

    public void setSelectAttr(int i, String s)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "select")
        {
            throw new Exception("setSelectAttr can be called only on SELECT node!");
        } else
        {
            ((Element)node).setAttribute("attributeName", s);
            return;
        }
    }

    public ArrayList getSortLinkItems(int i)
        throws Exception
    {
        ArrayList arraylist = new ArrayList();
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "sort")
            throw new Exception("getSortLinkItems can be called only on SORT node!");
        NodeList nodelist = ((Element)node).getElementsByTagName("linkTo");
        for(int j = 0; j < nodelist.getLength(); j++)
            arraylist.add(nodelist.item(j).getFirstChild().getNodeValue());

        return arraylist;
    }

    public void setSortLinkItems(int i, ArrayList arraylist)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "sort")
            throw new Exception("setSortLinkItems can be called only on SORT node!");
        NodeList nodelist = ((Element)node).getElementsByTagName("linkTo");
        ArrayList arraylist1 = new ArrayList();
        for(int j = 0; j < nodelist.getLength(); j++)
            arraylist1.add(nodelist.item(j));

        for(int k = 0; k < arraylist1.size(); k++)
            node.removeChild((Node)arraylist1.get(k));

        for(int l = 0; l < arraylist.size(); l++)
        {
            Element element = strategyRepresentation.createElement("linkTo");
            element.appendChild(strategyRepresentation.createTextNode((String)arraylist.get(l)));
            node.appendChild(element);
        }

    }

    public ArrayList getSelectShowContentItems(int i)
        throws Exception
    {
        ArrayList arraylist = new ArrayList();
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "select")
            throw new Exception("getSelectShowContentItems can be called only on SELECT node!");
        NodeList nodelist = ((Element)node).getElementsByTagName("showContent");
        for(int j = 0; j < nodelist.getLength(); j++)
            arraylist.add(nodelist.item(j).getFirstChild().getNodeValue());

        return arraylist;
    }

    public ArrayList getSelectShowContentDfltItems(int i)
        throws Exception
    {
        ArrayList arraylist = new ArrayList();
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "select")
            throw new Exception("getSelectShowContentDfltItems can be called only on SELECT node!");
        NodeList nodelist = ((Element)node).getElementsByTagName("showContentDefault");
        for(int j = 0; j < nodelist.getLength(); j++)
            arraylist.add(nodelist.item(j).getFirstChild().getNodeValue());

        return arraylist;
    }

    public ArrayList getSelectShowLinkItems(int i)
        throws Exception
    {
        ArrayList arraylist = new ArrayList();
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "select")
            throw new Exception("getSelectShowLinkItems can be called only on SELECT node!");
        NodeList nodelist = ((Element)node).getElementsByTagName("showLink");
        for(int j = 0; j < nodelist.getLength(); j++)
        {
            String s = ((Element)nodelist.item(j)).getElementsByTagName("linkTo").item(0).getFirstChild().getNodeValue();
            String s1 = ((Element)nodelist.item(j)).getElementsByTagName("comment").item(0).getFirstChild().getNodeValue();
            ShowLink showlink = new ShowLink(s, s1);
            arraylist.add(showlink);
        }

        return arraylist;
    }

    public void setSelectShowContentItems(int i, ArrayList arraylist)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "select")
            throw new Exception("setSelectShowContentItems can be called only on SELECT node!");
        NodeList nodelist = ((Element)node).getElementsByTagName("showContent");
        ArrayList arraylist1 = new ArrayList();
        for(int j = 0; j < nodelist.getLength(); j++)
            arraylist1.add(nodelist.item(j));

        for(int k = 0; k < arraylist1.size(); k++)
            node.removeChild((Node)arraylist1.get(k));

        for(int l = 0; l < arraylist.size(); l++)
        {
            Element element = strategyRepresentation.createElement("showContent");
            element.appendChild(strategyRepresentation.createTextNode((String)arraylist.get(l)));
            node.appendChild(element);
        }

    }

    public void setSelectShowContentDfltItems(int i, ArrayList arraylist)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "select")
            throw new Exception("setSelectShowContentDfltItems can be called only on SELECT node!");
        NodeList nodelist = ((Element)node).getElementsByTagName("showContentDefault");
        ArrayList arraylist1 = new ArrayList();
        for(int j = 0; j < nodelist.getLength(); j++)
            arraylist1.add(nodelist.item(j));

        for(int k = 0; k < arraylist1.size(); k++)
            node.removeChild((Node)arraylist1.get(k));

        for(int l = 0; l < arraylist.size(); l++)
        {
            Element element = strategyRepresentation.createElement("showContentDefault");
            element.appendChild(strategyRepresentation.createTextNode((String)arraylist.get(l)));
            node.appendChild(element);
        }

    }

    public void setSelectShowLinkItems(int i, ArrayList arraylist)
        throws Exception
    {
        Node node = (Node)textLinesToNodes.get(i);
        if(node.getNodeName() != "select")
            throw new Exception("setSelectShowLinkItems can be called only on SELECT node!");
        NodeList nodelist = ((Element)node).getElementsByTagName("showLink");
        ArrayList arraylist1 = new ArrayList();
        for(int j = 0; j < nodelist.getLength(); j++)
            arraylist1.add(nodelist.item(j));

        for(int k = 0; k < arraylist1.size(); k++)
            node.removeChild((Node)arraylist1.get(k));

        for(int l = 0; l < arraylist.size(); l++)
        {
            Element element = strategyRepresentation.createElement("showLink");
            Element element1 = strategyRepresentation.createElement("linkTo");
            element1.appendChild(strategyRepresentation.createTextNode(((ShowLink)arraylist.get(l)).getLinkTo()));
            element.appendChild(element1);
            Element element2 = strategyRepresentation.createElement("comment");
            element2.appendChild(strategyRepresentation.createTextNode(((ShowLink)arraylist.get(l)).getComment()));
            element.appendChild(element2);
            node.appendChild(element);
        }

    }
}
