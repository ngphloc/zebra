/**
 * 
 */
package vn.spring.zebra.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vn.spring.WOW.WOWDB.ConceptDB;
import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.WOWDB.XMLConceptDB;
import vn.spring.WOW.WOWDB.XMLUtil;
import vn.spring.WOW.datacomponents.Concept;
import vn.spring.WOW.datacomponents.DotString;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.engine.ConceptInfo;
import vn.spring.WOW.engine.ConceptsInfoTbl;
import vn.spring.WOW.engine.CourseInfo;
import vn.spring.WOW.engine.Dependences;
import vn.spring.WOW.engine.Hierarchy;
import vn.spring.WOW.engine.PNode;
import vn.spring.WOW.engine.TNode;
import vn.spring.WOW.exceptions.DatabaseException;
import vn.spring.WOW.exceptions.InvalidConceptException;
import vn.spring.WOW.util.KoenFormat;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;
import vn.spring.zebra.um.OverlayBayesUM;
import vn.spring.zebra.um.OverlayBayesUMFactory;

import com.sun.xml.tree.XmlDocument;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public final class ConceptUtil {
    public static ArrayList<ConceptInfo> getConceptInfos(String course) throws DatabaseException, InvalidConceptException {
    	ArrayList<ConceptInfo> cInfos = new ArrayList<ConceptInfo>();
    	
    	ConceptsInfoTbl cit = new ConceptsInfoTbl();
    	cit.readConceptInfo(course);
    	Hashtable<?, ?> concepts = cit.getConceptsInfoTbl();
    	Enumeration<?> enums = concepts.elements();
    	while(enums.hasMoreElements()) {
    		ConceptInfo cInfo = (ConceptInfo)enums.nextElement();
    		cInfos.add(cInfo);
    	}
    	return cInfos;
    }
    public static ArrayList<ConceptInfo> getConceptInfos(String course, int level, boolean isDeep) throws DatabaseException, InvalidConceptException {
    	ArrayList<ConceptInfo> cInfos = new ArrayList<ConceptInfo>();
    	
    	ConceptsInfoTbl cit = new ConceptsInfoTbl();
    	cit.readConceptInfo(course);
    	Hashtable<?, ?> concepts = cit.getConceptsInfoTbl();
    	Enumeration<?> enums = concepts.elements();
    	while(enums.hasMoreElements()) {
    		ConceptInfo cInfo = (ConceptInfo)enums.nextElement();
    		int cLevel = getConceptLevel(cInfo.getConceptName());
    		if(cLevel == 0) continue;
    		if(isDeep) {
    			if( cLevel <= level) {
    				cInfos.add(cInfo);
    			}
    		}
    		else {
    			if(cLevel == level)
    				cInfos.add(cInfo);
    		}
    	}
    	return cInfos;
    }
    public static CourseInfo getCourseInfo(String course) throws Exception {
        //Make new CourseInfo object and add it to the CourseInfoTbl
        CourseInfo ci=new CourseInfo();
        ci.courseName=course;
        //Fill Dependences obj with DB data
        ci.dependences=new Dependences();
        ci.dependences.readDependences(course);
        //Fill Hierarchy obj with DB data
        ci.hierarchy=new Hierarchy(ZebraStatic.root);
        ci.hierarchy.readHierarchy(course);
        //Fill ConceptInfo obj with DB data
        ci.conceptsInfoTbl=new ConceptsInfoTbl();
        ci.conceptsInfoTbl.readConceptInfo(course);
        
        return ci;
    }
    public static Hierarchy getCourseHierarchy(String course) throws Exception {
    	Hierarchy hierarchy = new Hierarchy(new File(ZebraStatic.getWowXmlRoot()));
    	hierarchy.readHierarchy(course);
    	return hierarchy;
    }
    
    public static int getConceptLevel(String cName) throws DatabaseException, InvalidConceptException {
        ConceptDB cdb = ZebraStatic.getConceptDB();
        Concept concept = cdb.getConcept(cdb.findConcept(cName));
        int level = 0;
        while(true) {
        	if(concept.getHierStruct() == null) break;
        	String parent = concept.getHierStruct().parent;
        	if(parent == null) break;
        	level++;
        	concept = cdb.getConcept(cdb.findConcept(parent));
        }
    	return level;
    }
    public static ArrayList<String> getConceptNameList(String course) {
        ArrayList<String> cList = new ArrayList<String>();
        ConceptDB cdb = ZebraStatic.getConceptDB();
        
        Vector<?> cnames = null;
        try {
	        cnames = cdb.getConceptList();
        }
        catch(Exception e) {
        	System.out.println("getConceptNameList has some error: " + e.getMessage());
        }
        
        if(cnames == null) return cList;
        for(int i = 0; i < cnames.size(); i++) {
        	String cname = (String)cnames.get(i);
        	if(course != null && course.length() > 0 && !cname.startsWith(course + "."))
        		continue;
        	
        	cList.add(cname);
        }
        return cList;
    }
    public static ArrayList<String> getConceptNameList(String course, QUERY_TYPE evalType) {
    	ArrayList<String> conceptNameList = getConceptNameList(course);
    	ArrayList<String> cList = new ArrayList<String>();
		if(evalType == QUERY_TYPE.OVERLAY)
			cList.addAll(conceptNameList);
		else {
			try {
				OverlayBayesUM um = null;
				if(evalType == QUERY_TYPE.OVERLAY_BAYESIAN)
					um = OverlayBayesUMFactory.loadFromDatabase(null, course, 
						OverlayBayesUM.OBUM_DEFAULT_BAYESNET_TYPE, false);
				else if(evalType == QUERY_TYPE.DYN_OVERLAY_BAYESIAN)
					um = OverlayBayesUMFactory.loadFromDatabase(null, course, 
						OverlayBayesUM.OBUM_DEFAULT_BAYESNET_TYPE, true);
				else
					return cList;
				for(String concept : conceptNameList) {
					if(um.getItem(getBriefConceptName(course, concept)) != null)
						cList.add(concept);
				}
			}
			catch(Exception e) {e.printStackTrace(); return cList;}
		}
		return cList;
    }
    public static ArrayList<Concept> getConceptList(String course) {
        ArrayList<Concept> cList = new ArrayList<Concept>();
        ConceptDB cdb = ZebraStatic.getConceptDB();
        
        Vector<?> cnames = null;
        try {
	        cnames = cdb.getConceptList();
        }
        catch(Exception e) {
        	System.out.println("getConceptNameList has some error: " + e.getMessage());
        }
        
        if(cnames == null) return cList;
        for(int i = 0; i < cnames.size(); i++) {
        	String  cname = (String)cnames.get(i);
        	if(course != null && course.length() > 0 && !cname.startsWith(course + "."))
        		continue;
        	
        	Concept concept = null;
        	try {
        		concept = cdb.getConcept(cdb.findConcept(cname));
        	}
        	catch(Exception e) {}
        	if(concept == null) continue;
        	
        	cList.add(concept);
        }
        return cList;
    }
    public static boolean doesExist(String course, String briefConcept, QUERY_TYPE evalType) {
		if(evalType == QUERY_TYPE.OVERLAY) {
			return true;
		}
		else {
			try {
				OverlayBayesUM um = null;
				if(evalType == QUERY_TYPE.OVERLAY_BAYESIAN)
					um = OverlayBayesUMFactory.loadFromDatabase(null, course, 
						OverlayBayesUM.OBUM_DEFAULT_BAYESNET_TYPE, false);
				else if(evalType == QUERY_TYPE.DYN_OVERLAY_BAYESIAN)
					um = OverlayBayesUMFactory.loadFromDatabase(null, course, 
						OverlayBayesUM.OBUM_DEFAULT_BAYESNET_TYPE, true);
				else
					return false;
				return um.getItem(briefConcept) != null;
			}
			catch(Exception e) {e.printStackTrace(); return false;}
		}
    }
	public static String getLinkedConcept(String resource) throws Exception {
		ConceptDB cdb = ZebraStatic.getConceptDB();
		
		String concept_name = cdb.getLinkedConcept(resource);
		return concept_name;
	}
	public static String getLinkedConceptOfRealFilePath(String filepath) throws Exception {
		String abspath = new File(filepath).getAbsolutePath();
		String webRoot = new File(ZebraStatic.getWowWebRoot()).getAbsolutePath();
		if(abspath.startsWith(webRoot)) {
			abspath = abspath.substring(webRoot.length());
			if(abspath.startsWith(File.separator)) abspath = abspath.substring(1);
			abspath = abspath.replace('\\', '/');
			abspath = "file:/" + abspath;
		}
		return getLinkedConcept(abspath);
	}
	
	public static ArrayList<Concept> getConceptListFromWowFile(String wowFileName, String author, String pconceptname) {
		String graphauthorfiles = ZebraStatic.getWowAuthorFilesPath();
		String wowroot = ZebraStatic.getWowRoot();
		String fileUrl = wowroot + graphauthorfiles.substring(1) + author + "/" + wowFileName +".wow";
		ArrayList<Concept> conceptlist = new ArrayList<Concept>();
        try {
	        ConceptDB cdb = ZebraStatic.getConceptDB();
	        Vector<?> list = cdb.getConceptList();
	        Vector<String> deletelist = new Vector<String>();
	
	        //create a list of all concepts that should be deleted
	        //because they are no longer in the current definition
	        for (int i=0;i<list.size();i++) {
	        	if (((String)list.get(i)).startsWith(pconceptname +".")) 
	        		deletelist.add((String)list.get(i));
	        }
	        
	        if (cdb instanceof XMLConceptDB) ((XMLConceptDB)cdb).disableIndexWriting();
	        try {
	            XmlDocument doc = XMLUtil.getXML(new File(fileUrl));
	            Node node = (Element)doc.getDocumentElement();
	            NodeList nodelist = node.getChildNodes();
	            for (int i=0; i<nodelist.getLength(); i++) {
	                Node child = nodelist.item(i);
	                if (child.getNodeName().equals("concept")) {
	                    Concept concept = KoenFormat.getKoenConcept(child);
	                    if (concept != null) {
	                        conceptlist.add(concept);
	                        //this concept is still in the definition so don't delete it
	                        deletelist.remove(concept.getName());
	                    }
	                }
	            }
	        } catch (Exception e) {
	            System.out.println("ConceptList: getConceptList: " +e);
	        }
	        if (cdb instanceof XMLConceptDB) ((XMLConceptDB)cdb).enableIndexWriting();
		}
		catch(DatabaseException e) {
		}
        return conceptlist;
	}
	public static ArrayList<String> getConceptInfoListFromWowFile(String wowFileName, String author, String pconceptname) throws IOException, DatabaseException {
		File root = new File(ZebraStatic.getWowXmlRoot());
		File conceptRoot = new File(root, "concept");

		ArrayList<String> infos = new ArrayList<String>();
		
		ArrayList<Concept> concepts = getConceptListFromWowFile(wowFileName, author, pconceptname);
		Hashtable<?, ?> idxresource = XMLUtil.loadIndex(conceptRoot, "idxresource");
		Hashtable<?, ?> idxdbconcept = XMLUtil.loadIndex(conceptRoot, "index");
		
		for (int i = 0; i < concepts.size(); i++) {
			Concept concept = (Concept)concepts.get(i);
			String  cname = concept.getName();
			Long    conceptdbid = (Long)(idxdbconcept.get(cname));
			URL     resourceURL = concept.getResourceURL();
        
			Long    resourceid = (resourceURL==null)?null:(Long)(idxresource.get(resourceURL.toString()));
			String  info = "Concept: \"" + cname + "\" has id=" + concept.id + ", " +
    		"dbid=" + conceptdbid + ", " +
    		"resourceID=" + (resourceid==null?"null":resourceid.longValue()) + ", " +
    		"resourceURL=" + (resourceURL==null?"null":resourceURL.toString()) + ", ";
			
			infos.add(info);
		}
		return infos;
	}
	
    public static String getBriefConceptName(String course, String concept) {
		DotString ds = new DotString(concept);
		if(ds.size() > 1 && ds.get(0).equals(course)) ds.set(0, null);
		return ds.toString();
	}
	
    //Kiem tra child co la con cua 1 invisible nao k, neu tra ve khac null thi child duong nhien la unmastered
    //Tra ve nut cha top-most invisible cua concept
	public static PNode getUnmasteredNode(String conceptname, TriUMQuery query, String userid, String course) throws Exception {
		ProfileDB pdb = ZebraStatic.getProfileDB();
		Profile   profile = pdb.getProfile(pdb.findProfile(userid));
		
        PNode curnode = new PNode(ZebraStatic.getCourseInfoTbl().getCourseInfo(course).hierarchy.getTNode(conceptname), profile);
        PNode topmostnode = curnode;
        LinkedList path = new LinkedList();
        while (topmostnode != null) {path.addFirst(topmostnode); topmostnode = topmostnode.getParent();}
        topmostnode = (PNode)path.get(0); //is javatutorial.javatutorial
        return getUnmasteredNode(topmostnode, curnode, query, userid, course);
	}
	//Kiem tra child co la con cua 1 invisible nao k, neu tra ve khac null thi child duong nhien la unmastered
    //Tra ve nut cha top-most invisible cua child
	protected static PNode getUnmasteredNode(PNode root, PNode child, TriUMQuery query, String userid, String course) throws Exception {
		//breath-level browse
        PNode node = root.getFirstChild();
        while (node != null) {
        	if(isInvisible(node, query, userid, course) && (child.getName().equals(node.getName()) || child.isDecendentOf(node)))
        		return node;
        	node = node.getNextSib();
        }
        
        node = root.getFirstChild();
        while (node != null) {
        	PNode unmastered = getUnmasteredNode(node, child, query, userid, course);
        	if(unmastered != null) return unmastered;
        	node = node.getNextSib();
        }
        return null;
	}
	protected static boolean isInvisible(PNode node, TriUMQuery query, String userid, String course) throws Exception {
		PNode previous = node.getPreviousSib();
		if(previous == null) return false;
		
        boolean isExist = query.doesExist(userid, course,
        	getBriefConceptName(course, previous.getName()),
        	ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
        boolean isEvidence = query.isKnowledgeEvidence(userid, course,
        	getBriefConceptName(course, previous.getName()), ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
        
        if(!isExist || isEvidence)
        	return false;
        else {
        	double previousknowledge = knowledgeQuery(query, userid, course, previous.getName());
            if(previousknowledge < 0.5) return true;
            else                        return false;
        }
	}
    public static double knowledgeQuery(TriUMQuery query, String userid, String course, String concept) {
    	double curknowledge = 0;
    	try {
    		curknowledge = query.knowledgeQuery(userid, course, 
    			getBriefConceptName(course, concept), ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
    		return curknowledge;
    	}
    	catch(Throwable e) {
    		System.out.println("####INFO: ZebraStaticTreeView->menuList->knowledgeQuery causes some error: " + e.getMessage() + "####. So do again");
    		try {
    			//Do again
        		curknowledge = query.knowledgeQuery(userid, course, 
        			getBriefConceptName(course, concept), ZebraStatic.USER_KNOWLEDGE_EVAL_TYPE, true);
    		} catch(Throwable e2) {
    			//e2.printStackTrace();
    		}
        	return curknowledge;
    	}
    }
    
	public static boolean isDecendentOf(String userid, String course, String parentConcept, String childConcept) {
		try {
			ProfileDB pdb = ZebraStatic.getProfileDB();
			Profile   profile = pdb.getProfile(pdb.findProfile(userid));
			CourseInfo courseInfo = ZebraStatic.getCourseInfoTbl().getCourseInfo(course);
			if(courseInfo == null) courseInfo = getCourseInfo(course);
			
	        PNode childNode = new PNode(courseInfo.hierarchy.getTNode(childConcept), profile);
	        PNode parentNode = new PNode(courseInfo.hierarchy.getTNode(parentConcept), profile);
	        return childNode.isDecendentOf(parentNode);
		}
		catch(Exception e) {e.printStackTrace();}
		return false;
	}
	
	public static boolean isDecendentOf(String course, String parentConcept, String childConcept) {
		try {
			CourseInfo courseInfo = ZebraStatic.getCourseInfoTbl().getCourseInfo(course);
			if(courseInfo == null) courseInfo = getCourseInfo(course);
			
	        TNode childNode = courseInfo.hierarchy.getTNode(childConcept);
	        TNode parentNode = courseInfo.hierarchy.getTNode(parentConcept);
	        return childNode.isDecendentOf(parentNode);
		}
		catch(Exception e) {e.printStackTrace();}
		return false;
	}
	
	public static String getCourseTitle(String course) {
		try {
			CourseInfo courseInfo = ZebraStatic.getCourseInfoTbl().getCourseInfo(course);
			if(courseInfo == null) courseInfo = getCourseInfo(course);
			
	        ConceptInfo cInfo = courseInfo.conceptsInfoTbl.getConceptInfo(course + "." + course);
	        if(cInfo == null) return null;
	        return cInfo.getTitle();
		}
		catch(Exception e) {e.printStackTrace();}
		return null;
		
	}
}
