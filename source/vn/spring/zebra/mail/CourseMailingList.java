/**
 * 
 */
package vn.spring.zebra.mail;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.mail.CourseMailingListEvent.TYPE;
import vn.spring.zebra.report.UserReportService;
import vn.spring.zebra.server.TriUMServer;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.util.CommonUtil;
import vn.spring.zebra.util.FireEventTask;
import vn.spring.zebra.util.FireEventUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CourseMailingList {
	protected String course = null;
	protected MailService mailService = null;
	protected Document doc = null;
	
	protected HashSet<CourseMailingListListener> listeners = new HashSet<CourseMailingListListener>();

	public CourseMailingList(String course) throws Exception {
		this.course = course;
		mailService = new MailService(ZebraStatic.MAIL_SMTP_HOST, null, null, false);

    	File mailDir = new File(ZebraStatic.getWowXmlRoot() + ZebraStatic.MAIL_DIR);
    	if(!mailDir.exists()) mailDir.mkdir();
    	
    	File mlDir = new File(mailDir, "ml");
    	if(!mlDir.exists()) mlDir.mkdir();
    	
    	File courseMLDir = new File(mlDir, course + ".xml");
    	if(!courseMLDir.exists()) {
    		doc = createDocument(course);
    		save();
    	}
    	else  {
			DOMParser parser = new DOMParser();
			parser.parse(courseMLDir.toString());
			doc = parser.getDocument();
    	}
	}
	public synchronized void sendMail(ArrayList<String> userids, String subject, String content, File attach) {
		try {
			ArrayList<String> existUserIds = getUserIdList();
			ArrayList<String> sentUserIds = new ArrayList<String>();
			if(userids == null || userids.size() == 0) {
				sentUserIds.addAll(existUserIds);
			}
			else {
				for(String userid : userids) {
					if(existUserIds.contains(userid)) sentUserIds.add(userid);
				}
			}
			if(sentUserIds.size() == 0) return;

			ArrayList<File> attaches = new ArrayList<File>();
			if(attach != null) attaches.add(attach);
			mailService.sendMail(MailUtil.newMailAddress(ZebraStatic.MAIL_ADMIN_ADDRESS), MailUtil.getNetAddrList(sentUserIds), subject, content, attaches, false);
			FireEventUtil.fireEvent(new CourseMailingListServiceEventTask(
				new CourseMailingListEvent(this, TYPE.SENDMAIL)), ZebraStatic.FIRE_EVENT_WAIT_DONE);
		}
		catch(Exception e) {e.printStackTrace();}
	}
	
	public synchronized void sendReport(ArrayList<String> userids, String html) {
		ArrayList<String> existUserIds = getUserIdList();
		ArrayList<String> sentUserIds = new ArrayList<String>();
		if(userids == null || userids.size() == 0) {
			sentUserIds.addAll(existUserIds);
		}
		else {
			for(String userid : userids) {
				if(existUserIds.contains(userid)) sentUserIds.add(userid);
			}
		}
		if(sentUserIds.size() == 0) return;
		
		TriUMServer server = null;
		try {server = TriUMServer.getInstance();}
		catch(Exception e) {e.printStackTrace(); return;}
		
		synchronized(server) {
			for(String userid : sentUserIds) {
				try {
					if(html == null || html.length() == 0) {
						TriUM um = server.getUM(userid, course, false);
						boolean unmonitor = false;
						if(um == null) {
							unmonitor = true;
							um = server.monitorUM(userid, course);
						}
						UserReportService service = new UserReportService(um);
						html = "<html>\n<body>\n" + service.genHtmlReport() + "\n</body>\n</html>";
						if(unmonitor) server.unmonitorUM(userid, course);
					}
					ArrayList<String> t_userids = new ArrayList<String>();
					t_userids.add(userid);
					mailService.sendMail(MailUtil.newMailAddress(ZebraStatic.MAIL_ADMIN_ADDRESS), MailUtil.getNetAddrList(t_userids), "Your Report", html, null, true);
				}
				catch(Exception e) {e.printStackTrace();}
			}
		}
		FireEventUtil.fireEvent(new CourseMailingListServiceEventTask(
			new CourseMailingListEvent(this, TYPE.SENDREPORT)), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}

	public synchronized void addUser(String userid) {
		Element user = addUserElement(userid);
		if(user != null)
			FireEventUtil.fireEvent(new CourseMailingListServiceEventTask(
				new CourseMailingListEvent(this, TYPE.ADDUSER)), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
	public synchronized void removeUser(String userid) {
		Element user = removeUserElement(userid);
		if(user != null)
			FireEventUtil.fireEvent(new CourseMailingListServiceEventTask(
				new CourseMailingListEvent(this, TYPE.REMOVEUSER)), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
	public synchronized void clear(String userid) {
		removeAllUserElements();
		FireEventUtil.fireEvent(new CourseMailingListServiceEventTask(
			new CourseMailingListEvent(this, TYPE.CLEAR)), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
	public synchronized void addListener(CourseMailingListListener listener) {
		listeners.add(listener);
	}
	public synchronized void removeListener(CourseMailingListListener listener) {
		listeners.remove(listener);
	}
	public synchronized ArrayList<String> getUserIdList() {
		ArrayList<String> userids = new ArrayList<String>();
		ArrayList<UserMailInfo> list = getUserMailInfos();
		for(UserMailInfo info : list) {
			userids.add(info.userid);
		}
		return userids;
	}
	
	private class CourseMailingListServiceEventTask implements FireEventTask {
		private CourseMailingListEvent evt = null; 
		public CourseMailingListServiceEventTask(CourseMailingListEvent evt) {
			this.evt = evt;
		}
		public void run() {
			for(CourseMailingListListener listener : listeners) {
				try {
					switch(evt.type) {
					case SENDMAIL:
						listener.mailSent(evt);
						break;
					case SENDREPORT:
						listener.reportSent(evt);
						break;
					case ADDUSER:
						listener.userAdded(evt);
						break;
					case REMOVEUSER:
						listener.userRemoved(evt);
						break;
					case CLEAR:
						listener.cleared(evt);
						break;
					}
				}
				catch(Exception e) {
					ZebraStatic.traceService.trace("CourseMailingListServiceEventTask.run causes error: " + e.getMessage());
				}
			}
		}
	}
	
	private void save() {
		try {
	    	File xmlFile = new File(ZebraStatic.getWowXmlRoot() + 
	    			ZebraStatic.MAIL_DIR + "ml/" + course + ".xml");
	    	
			Element ml = getMLElement();
			String xml = CommonUtil.serializeXML(ml);
			PrintWriter out = new PrintWriter(new FileWriter(xmlFile));
			out.print(xml);
			out.flush();
			out.close();
		}
		catch(Exception e) {e.printStackTrace();}
	}
	
	private Element getMLElement() {
		return doc.getDocumentElement();
	}
	private Element getUserElement(String userid) {
		Element ml = getMLElement();
		NodeList childrent = ml.getChildNodes();
		for(int i = 0; i < childrent.getLength(); i++) {
			Node child = childrent.item(i);
			if(!child.getNodeName().equals("user")) continue;
			Element user = (Element)child;
			if(user.getAttribute("id").equals(userid)) return user;
		}
		return null;
	}
	private ArrayList<UserMailInfo> getUserMailInfos() {
		ArrayList<UserMailInfo> list = new ArrayList<UserMailInfo>();
		Element ml = getMLElement();
		NodeList childrent = ml.getChildNodes();
		for(int i = 0; i < childrent.getLength(); i++) {
			Node child = childrent.item(i);
			if(!child.getNodeName().equals("user")) continue;
			list.add(new UserMailInfo((Element)child));
			
		}
		return list;
	}
	
	private Element addUserElement(String userid) {
		Element user = getUserElement(userid);
		if(user != null) return user;
		
		try {
			UserMailInfo info = new UserMailInfo(userid);
			if(info.userid == null || info.mail == null) return null;
			
			user = createUserElement(doc, info.userid, info.username, info.mail);
			getMLElement().appendChild(user);
			save();
			return user;
			
		}
		catch(Exception e) {e.printStackTrace();}
		return null;
	}
	
	private Element removeUserElement(String userid) {
		Element user = getUserElement(userid);
		if(user == null) return null;
		user = (Element)getMLElement().removeChild(user);
		save();
		return user;
	}
	
	private void removeAllUserElements() {
		doc = createDocument(course);
		save();
	}
	
	private static Document createDocument(String course) {
		Document doc = new DocumentImpl();
		Element mailinglist = doc.createElement("ml");
		mailinglist.setAttribute("course", course);
		doc.appendChild(mailinglist);
		return doc;
	}
	private static Element createUserElement(Document doc, String userid, String username, String mail) {
		Element user = doc.createElement("user");
		user.setAttribute("id", userid);
		user.setAttribute("name", username);
		user.setAttribute("mail", mail);
		return user;
	}
	public static void main(String[] args) throws Exception {
		CourseMailingList ml = new CourseMailingList("javatutorial");
		ml.addUser("guest");
		ml.addUser("lhxung");
	}
}
