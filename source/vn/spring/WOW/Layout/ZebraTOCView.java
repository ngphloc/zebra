/**
 * 
 */
package vn.spring.WOW.Layout;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.LinkAdaptation;
import vn.spring.WOW.engine.PNode;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.util.ConceptUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class ZebraTOCView extends ZebraStaticTreeView {
    public String getViewType() {return "ZEBRATOCVIEW";}

	@Override
	protected void menuList(PNode parent, LinkedList pathlist, int listindex,
			Element table, PNode rootnode, Document doc, Profile profile) {
		try {
	        PNode node = parent.getFirstChild();
	        while (node != null) {
	            boolean isNextNodeExistInUM = query.doesExist(userid, course, 
	            		ConceptUtil.getBriefConceptName(course, node.getName()), ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
	            boolean isNextNodeEvidence = query.isKnowledgeEvidence(userid, course, 
	            		ConceptUtil.getBriefConceptName(course, node.getName()), ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
	            
	            if(isNextNodeExistInUM && !isNextNodeEvidence) {
		            table.appendChild(createRowElement(node.getName(), listindex*15, doc, profile, node.getName().equals(rootnode.getName())));
		            menuList(node, pathlist, listindex+1, table, rootnode, doc, profile);
	            }
	            node = node.getNextSib();
	        }
		}
		catch(Exception e) {
			System.out.println("ZebraTOCView.menuList causes error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected Element createRowElement(String conceptname, int width, Document doc,
			Profile profile, boolean bold) {
        Element tr = doc.createElement("tr");
        Element td = doc.createElement("td"); tr.appendChild(td); td.setAttribute("style", "padding-left: "+width+"px;");
        if (bold) {Element b = doc.createElement("b"); td.appendChild(b); td = b;}
        try {
            td.appendChild(LinkAdaptation.createLink(conceptname, profile, getViewType(), doc));
            double curknowledge = ConceptUtil.knowledgeQuery(query, userid, course, conceptname);
            td.appendChild(doc.createTextNode(" (" + ((int)(curknowledge * 100.0)) + "% mastered)"));
        } catch (Exception e) {
            e.printStackTrace();
            td.appendChild(errorElement("["+e.getMessage()+", "+conceptname+"]", doc));
        }
        return tr;
	}

}
