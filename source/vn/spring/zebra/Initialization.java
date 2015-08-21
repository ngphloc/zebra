/**
 * 
 */
package vn.spring.zebra;

//import java.io.File;
//import java.util.Vector;
//
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//
//import com.sun.xml.tree.XmlDocument;
//
//import vn.spring.WOW.Layout.Layout;
//import vn.spring.WOW.Layout.LayoutList;
//import vn.spring.WOW.Layout.SecWndLink;
//import vn.spring.WOW.Layout.SecWndLinks;
//import vn.spring.WOW.Layout.View;
//import vn.spring.WOW.Layout.Window;
//import vn.spring.WOW.WOWDB.XMLUtil;
//import vn.spring.WOW.engine.Annotation;
//import vn.spring.WOW.engine.ConceptType;
//import vn.spring.WOW.engine.ConceptTypes;
//import vn.spring.WOW.engine.ConceptsInfoTbl;
//import vn.spring.WOW.engine.CourseInfo;
//import vn.spring.WOW.engine.Dependences;
//import vn.spring.WOW.engine.Hierarchy;
//import vn.spring.WOW.engine.IconAnnotation;
//import vn.spring.WOW.engine.LinkAnnotation;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public final class Initialization {
	public final static void initWOWandZebra(String course, String directory, boolean reload) {
		vn.spring.WOW.Initialization.fillInMemStructs(course, directory, reload);
	}
	
//	protected final static void fillInMemStructs(String course, String directory){
//		if(ZebraStatic.getCourseInfoTbl().getCourseInfo(course) != null) return;
//		//Caution: should remove this line
//        vn.spring.WOW.Initialization.fillInMemStructs(course, directory);
//		
//  		fillDataSources(course, directory);
//  		
//  		try {fillConceptTypes(course, directory);}
//  		catch(Exception e) {
//  			ZebraStatic.traceService.trace("Initialization.fillCourseInfoTblandViewManager causes error: " + e.getMessage());
//  		}
//  		
//  		try {fillCourseLayout(course, directory);}
//  		catch(Exception e) {
//  			ZebraStatic.traceService.trace("Initialization.fillCourseInfoTblandViewManager causes error: " + e.getMessage());
//  		}
//  	}
//	
//	protected final static void fillDataSources(String courseName, String courseDir){
//		try {
//			//Make new CourseInfo object and add it to the CourseInfoTbl
//			CourseInfo ci=new CourseInfo();
//			ci.courseName=courseName;
//			ZebraStatic.getCourseInfoTbl().setCourseInfo(ci);
//			//Fill Dependences obj with DB data
//			ci.dependences=new Dependences();
//			ci.dependences.readDependences(courseName);
//			//Fill Hierarchy obj with DB data
//			ci.hierarchy=new Hierarchy(ZebraStatic.root);
//			ci.hierarchy.readHierarchy(courseName);
//			//Fill ConceptInfo obj with DB data
//			ci.conceptsInfoTbl=new ConceptsInfoTbl();
//			ci.conceptsInfoTbl.readConceptInfo(courseName);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	protected final static void fillConceptTypes(String courseName, String courseDir){
//		//Create instance of concept info table
//		ZebraStatic.getCourseInfoTbl().getCourseInfo(courseName).linkclasses = new Vector();
//		Vector linkclasses = ZebraStatic.getCourseInfoTbl().getCourseInfo(courseName).linkclasses;
//		ZebraStatic.getCourseInfoTbl().getCourseInfo(courseName).CT=new ConceptTypes();
//
//		File file = new File(ZebraStatic.getWowRoot(), courseDir + "/ConceptTypeConfig.xml");
//		XmlDocument doc=null;
//
//		try{  doc = XMLUtil.getXML(file);}
//		catch(Exception e){ ZebraStatic.traceService.trace("fillConceptTypes:Error reading concepttypeconfig.xml");}
//
//		Element element=null; //XML hierarchy element
//
//		//Get all the concepttypes
//		element = (Element) doc.getElementsByTagName("concepttypes").item(0);
//		element= (Element) element.getFirstChild(); //concepttype
//
//		while(element!=null){
//
//			Element ctch=(Element) element.getFirstChild(); //type el
//			String type=XMLUtil.nodeValue(ctch);
//
//			ctch=(Element) ctch.getNextSibling(); //representation el
//			String representation=XMLUtil.nodeValue(ctch);
//
//			ctch=(Element) ctch.getNextSibling(); //annotation el
//
//			//create annotation object
//			Annotation anno=new Annotation();
//
//			//get linkannotation (in the future this element can be used to init Linkannotation
//			Element linkAnnoEl = (Element) ctch.getFirstChild();//linkanno
//			LinkAnnotation linkAnno = new LinkAnnotation();
//
//			Element linkEl = null;
//			linkEl = (Element)linkAnnoEl.getFirstChild();
//
//			while (linkEl != null) {
//				String linkclass = XMLUtil.nodeValue(linkEl);
//				if (!linkclasses.contains(linkclass)) linkclasses.add(linkclass);
//				String expr = linkEl.getAttribute("expr");
//				String view = linkEl.getAttribute("view");
//				linkAnno.addLinkInfo(linkclass, expr, view);
//				linkEl = (Element)linkEl.getNextSibling();
//			}
//			//add linkannotation to its annotation object
//			anno.setLinkAnno(linkAnno);
//
//			//get iconannotation
//			Element iconAnnoEl = (Element)linkAnnoEl.getNextSibling();
//			IconAnnotation iconAnno = new IconAnnotation();
//
//			Element iconEl = null;
//			iconEl = (Element)iconAnnoEl.getFirstChild();
//
//			while (iconEl != null) {
//				String iconname = XMLUtil.nodeValue(iconEl);
//				String expr = iconEl.getAttribute("expr");
//				String place = iconEl.getAttribute("place");
//				String view = iconEl.getAttribute("view");
//				iconAnno.addIconInfo(iconname, expr, place, view);
//				iconEl = (Element)iconEl.getNextSibling();
//			}
//
//			//add iconannotation to its annotation object
//			anno.setIconAnno(iconAnno);
//
//			//make concepttype object
//			ConceptType ct=new ConceptType(type,representation,anno,courseName);
//
//		      //add new type to the concepttype table
//			ZebraStatic.getCourseInfoTbl().getCourseInfo(courseName).CT.addType(type,ct);
//			//System.out.print("ConceptType added to concept type table: "+type);
//			//get next concepttype
//			element=(Element) element.getNextSibling();
//		}
//	}
//
//	protected static void fillCourseLayout(String courseName, String courseDir){
//		String type = null, title = null, background = null, params = null, name = null; //parameters
//		File file=new File(ZebraStatic.getWowRoot(), courseDir + "/LayoutConfig.xml");
//		XmlDocument doc=null;
//		Element element=null; //XML hierarchy element
//
//		try {doc = XMLUtil.getXML(file);}
//		catch(Exception e){ ZebraStatic.traceService.trace("loadCourseLayout:Error reading LayoutConfig.xml");}
//
//		//Create instance of concept info table
//		ZebraStatic.getCourseInfoTbl().getCourseInfo(courseName).layoutList=new LayoutList();
//
//		//Get all the views
//		element = (Element) doc.getElementsByTagName("viewlist").item(0);
//		element= (Element) element.getFirstChild(); //first view
//
//		while(element!=null){
//
//			//read views paramenters
//			type=element.getAttribute("type");
//			title=element.getAttribute("title");
//			background=element.getAttribute("background");
//			params=element.getAttribute("params").replaceAll(",","&");
//			name=element.getAttribute("name");
//
//			//get its children(secondary link/window list)
//			SecWndLinks swls=null;
//			Element secWndsEl=(Element) element.getFirstChild();
//
//			if (secWndsEl!=null){
//
//				//get all its children(secWndLinks)
//				Element secWndEl= (Element) secWndsEl.getFirstChild();
//				swls=new SecWndLinks();
//				while(secWndEl!=null){
//					//Add secwndlink to swls table
//					SecWndLink swl=new SecWndLink(secWndEl.getAttribute("link"),secWndEl.getAttribute("viewgroup"),secWndEl.getAttribute("img"));
//					swls.addSwl(swl.getLinkName(),swl);
//					secWndEl=(Element) secWndEl.getNextSibling();
//				}
//			}
//
//			//Make View obj and add it to ViewManager
//			try{
//				View v=null;
//				Class viewclass = Class.forName("vn.spring.WOW.Layout."+type);
//				v = (View) viewclass.newInstance();
//				v.setViewName(name);
//				v.setBackgound(background);
//				v.setParams(params);
//				v.setTitle(title);
//				v.setSecWndLinks(swls);
//				ZebraStatic.getViewManager().addView(v);
//			}
//			catch(Exception e){ZebraStatic.traceService.trace("Initialization: Error registering view");}
//
//			//get next view
//			element=(Element) element.getNextSibling();
//		}
//
//		//Views done ======================================================================
//
//		//Get Layout
//		element = (Element) doc.getElementsByTagName("layoutlist").item(0);
//		element= (Element) element.getFirstChild();
//
//		//for each layout
//		while(element!=null){
//
//			String lName=element.getAttribute("name");
//			String triggerWndName=element.getAttribute("trigger");
//			Layout layout=new Layout(lName,triggerWndName);
//
//			//get all the viewgroups(windows)of this layout
//			Element vgEl= (Element) element.getFirstChild();
//			//for each viewgroup
//			while(vgEl!=null){
//
//				String wndName=vgEl.getAttribute("name");
//				String secondary=vgEl.getAttribute("secondary");
//				String wndOpt=vgEl.getAttribute("wndOpt");
//				Window wnd=new Window(wndName,secondary,wndOpt);
//
//				//generate brscode(framesStruct) for this window and add views to it
//				StringBuffer str=new StringBuffer("<html><head><meta http-equiv='Cache-Control' Content='no-cache' />\n");
//				str.append("<meta http-equiv='Expires' content='-1' /><meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\" /><title>"+wnd.getName()+"</title></head>\n");
//
//				boolean oneview=vgEl.getFirstChild().getNodeName().toLowerCase().equals("viewref");
//				if(oneview){
//					//only one view in the window so make a frameset around it
//					str.append("<frameset rows='*' border=0>");
//				}
//				
//				wnd.setBrsCode(str);
//				genHtmlFrames((Element) vgEl.getFirstChild(),wnd);
//
//				StringBuffer sb=wnd.getBrsCode();
//				if(oneview){
//					sb.append("</frameset>");
//				}
//				sb.append("</html>");
//
//				wnd.setBrsCode(sb);
//
//				//add this viewgroup(window) to the layout
//				layout.addWnd(wnd);
//
//				//next viewgroup
//				vgEl=(Element) vgEl.getNextSibling();
//			}
//
//			//add this layout to the layout list
//			ZebraStatic.getCourseInfoTbl().getCourseInfo(courseName).layoutList.addLayout(layout.getName(),layout);
//
//			//next layout
//			element=(Element) element.getNextSibling();
//		}
//
//		// Stylesheet
//		NodeList list = doc.getElementsByTagName("stylesheet");
//		String stylesheet;
//		if (list.getLength()==0) {
//			//stylesheet = "../WOWstandard/wow.css"
//			stylesheet = ZebraStatic.getWowContextPath() + "/WOWstandard/wow.css";
//		}
//		else {
//			element = (Element)list.item(0);
//			//stylesheet = element.getFirstChild().getNodeValue();
//			stylesheet = ZebraStatic.getWowContextPath() + "/" + courseDir + "/" + element.getFirstChild().getNodeValue();
//		}
//		ZebraStatic.getCourseInfoTbl().getCourseInfo(courseName).stylesheet = stylesheet;
//	}
//
//	protected final static void genHtmlFrames(Element element,Window wnd){
//		String layout=null,border=null,name=null; //parameters
//		StringBuffer htmlCode=wnd.getBrsCode();
//
//		//Check if it is a compound or view element
//		if(element.getNodeName().equals("viewref")){//"viewref" element
//			name=element.getAttribute("name");
//
//			//Add its html code
//			String contextpath = ZebraStatic.getWowContextPath();
//			htmlCode.append("<frame src='"+contextpath+"/ViewGet<**>?view=<#"+name+"#>' name='"+name+"' />\n");
//
//			//Add this to wnd
//			wnd.addViewName(name);
//		}
//		else { //"compound" element
//
//			layout=element.getAttribute("framestruct");
//			if (element.getAttribute("border").equals(""))
//				border="border=0";
//			else
//				border="border="+element.getAttribute("border");
//
//			htmlCode.append("<frameset "+layout+" "+border+" >\n");
//			element=(Element) element.getFirstChild();
//
//			//Generate html code also for all children of 'compound' element
//			while(element!=null){
//
//				wnd.setBrsCode(htmlCode);
//				genHtmlFrames(element,wnd);
//				htmlCode=wnd.getBrsCode();
//				element=(Element) element.getNextSibling();
//			}
//
//			//Close the frameset
//			htmlCode.append("</frameset>\n");
//		}
//
//		wnd.setBrsCode(htmlCode);
//     }
}
