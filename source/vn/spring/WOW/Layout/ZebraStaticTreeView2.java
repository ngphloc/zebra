/**
 * 
 */
package vn.spring.WOW.Layout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.LinkAdaptation2;
import vn.spring.WOW.engine.PNode;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.helperservice.WOWContextListener;
import vn.spring.zebra.util.ConceptUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ZebraStaticTreeView2 extends StaticTreeView {
	protected String     userid = null;
	protected String     course = null;
	protected TriUMQuery query = null;

	public String getViewType() {return "ZEBRASTATICTREEVIEW2";}

	@Override
	public InputStream genBrsCode(String conceptname, Profile profile,
			Map params) {

		try {
	        this.userid = profile.getAttributeValue("personal", "id");
	        this.course = profile.getAttributeValue("personal", "course");
	        this.query = (TriUMQuery) WOWContextListener.getInstance().getTriUMServer().
	        	getCommunicateService().getQueryDelegator();
	        
            Document doc = newDocument(profile, title);
            Element body = (Element)doc.getDocumentElement().getElementsByTagName("body").item(0);
            if(background != null && background.length() > 0) body.setAttribute("background", background);
            Element table = doc.createElement("table"); body.appendChild(table);

            String course = profile.getAttributeValue("personal", "course");
            //rootnode is selected node
            PNode rootnode = new PNode(WOWStatic.CourseInfoTbl.getCourseInfo(course).hierarchy.getTNode(conceptname), profile);

            PNode curnode = rootnode;
            LinkedList path = new LinkedList();
            while (curnode != null) {path.addFirst(curnode);curnode = curnode.getParent();}
            curnode = (PNode)path.get(0); //first node = "javatutorial.javatutorial"
            //first row (E.g: Java Tutorial)
            table.appendChild(createRowElement(curnode.getName(), 0, doc, profile, curnode.getName().equals(rootnode), false));
            //All remaining row
            menuList(curnode, path, 1, table, rootnode, doc, profile, false);
            
            String result = serializeXML(doc.getDocumentElement());
            return new ByteArrayInputStream(result.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return errorStream("[Error generating StaticTreeView2]", e.getMessage());
        }
	}

	protected void menuList(PNode parent, LinkedList pathlist, int listindex,
			Element table, PNode rootnode, Document doc, Profile profile, boolean gray) {
		try {
	        PNode node = parent.getFirstChild();
	        while (node != null) {
	            table.appendChild(createRowElement(node.getName(), listindex*15, doc, profile, 
	            		node.getName().equals(rootnode.getName()), gray));
	            if (listindex < pathlist.size())
	                if (node.getName().equals( ((PNode)pathlist.get(listindex)).getName() ) || node.getName().equals(rootnode.getName()))
	                    menuList(node, pathlist, listindex+1, table, rootnode, doc, profile, gray);
	            
	            PNode nextnode = node.getNextSib();
	            if(nextnode == null) break;
	            
	            boolean isNextNodeExistInUM = query.doesExist(userid, course, 
	            		ConceptUtil.getBriefConceptName(course, nextnode.getName()), ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
	            boolean isNextNodeEvidence = query.isKnowledgeEvidence(userid, course, 
	            		ConceptUtil.getBriefConceptName(course, nextnode.getName()), ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
	            
	            if(!isNextNodeExistInUM)
	            	node = nextnode;
	            else if(isNextNodeEvidence) {
	            	if(!gray) node = nextnode; //still appearence
	            	else      node = nextnode.getNextSib(); //ignore test
	            }
	            else {
	            	double curknowledge = ConceptUtil.knowledgeQuery(query, userid, course, node.getName());
		            if(curknowledge < 0.5) {
		            	double nextknowledge = ConceptUtil.knowledgeQuery(query, userid, course, nextnode.getName());
		            	if(nextknowledge < 0.5) gray = true;
		            }
	            	node = nextnode;
	            }//end if
	        }
		}
		catch(Exception e) {
			System.out.println("ZebraStaticTreeView2.menuList causes error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
    protected Element createRowElement(String conceptname, int width, Document doc, Profile profile, 
    		boolean bold, boolean gray) {
        Element tr = doc.createElement("tr");
        Element td = doc.createElement("td"); tr.appendChild(td); td.setAttribute("style", "padding-left: "+width+"px;");
        if (bold) {Element b = doc.createElement("b"); td.appendChild(b); td = b;}
        try {
            td.appendChild(LinkAdaptation2.createLink(conceptname, profile, getViewType(), doc, true, gray));
        } catch (Exception e) {
            e.printStackTrace();
            td.appendChild(errorElement("[" + e.getMessage() + ", " + conceptname + "]", doc));
        }
        return tr;
    }
}
