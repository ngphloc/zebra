package vn.spring.WOW.Layout;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.ConceptInfo;
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
public class ZebraStaticTreeView extends StaticTreeView {
	protected String     userid = null;
	protected String     course = null;
	protected TriUMQuery query = null;
	
	
    public ZebraStaticTreeView() {}

    public String getViewType() {return "ZEBRASTATICTREEVIEW";}

	@Override
	public InputStream genBrsCode(String conceptname, Profile profile,
			Map params) {
		// TODO Auto-generated method stub
		try {
	        this.userid = profile.getAttributeValue("personal", "id");
	        this.course = profile.getAttributeValue("personal", "course");
	        this.query = (TriUMQuery) WOWContextListener.getInstance().getTriUMServer().
	        	getCommunicateService().getQueryDelegator();
			return super.genBrsCode(conceptname, profile, params);
		}
		catch(Exception e) {
			e.printStackTrace();
            return errorStream("[Error generating ZebraStaticTreeView]", e.getMessage());
		}
	}

	@Override
	protected void menuList(PNode parent, LinkedList pathlist, int listindex,
			Element table, PNode rootnode, Document doc, Profile profile) {
		try {
	        PNode node = parent.getFirstChild();
	        while (node != null) {
	            table.appendChild(createRowElement(node.getName(), listindex*15, doc, profile, node.getName().equals(rootnode.getName())));
	            if (listindex < pathlist.size())
	                if (node.getName().equals( ((PNode)pathlist.get(listindex)).getName() ) || node.getName().equals(rootnode.getName()))
	                    menuList(node, pathlist, listindex+1, table, rootnode, doc, profile);
	            
	            PNode nextnode = node.getNextSib();
	            if(nextnode == null) break;
	            
	            boolean isNextNodeExistInUM = query.doesExist(userid, course, 
	            		ConceptUtil.getBriefConceptName(course, nextnode.getName()), ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
	            boolean isNextNodeEvidence = query.isKnowledgeEvidence(userid, course, 
	            		ConceptUtil.getBriefConceptName(course, nextnode.getName()), ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
	            if(!isNextNodeExistInUM || isNextNodeEvidence) {
	            	node = nextnode; //still appearence
	            }
	            else {
	            	double curknowledge = ConceptUtil.knowledgeQuery(query, userid, course, node.getName());
		            if(curknowledge < 0.5) {
		            	double nextknowledge = ConceptUtil.knowledgeQuery(query, userid, course, nextnode.getName());
		            	if(nextknowledge < 0.5) {
			                ConceptInfo curinfo = WOWStatic.CourseInfoTbl.getCourseInfo(course).
		                		conceptsInfoTbl.getConceptInfo(node.getName());
			                String info = "You should master previous concept: \"" + curinfo.getTitle() + "\"";
			                table.appendChild(createDisabledRowElement(info, listindex*15, doc));
			                break;
		            	}
		            	else
		            		node = nextnode;
		            }
		            else {
		            	node = nextnode;
		            }
	            }//end if
	        }
		}
		catch(Exception e) {
			System.out.println("ZebraStaticTreeView.menuList causes error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
    protected Element createDisabledRowElement(String info, int width, Document doc) {
        Element tr = doc.createElement("tr");
        Element td = doc.createElement("td"); tr.appendChild(td); td.setAttribute("style", "padding-left: " + width + "px;");
        Element font = doc.createElement("font"); td.appendChild(font);
        font.setAttribute("color", "#808080");
        font.appendChild(doc.createTextNode(info));
        return tr;
    }
	
}
