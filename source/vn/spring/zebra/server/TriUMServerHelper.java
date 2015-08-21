/**
 * 
 */
package vn.spring.zebra.server;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

import org.apache.axis.client.AdminClient;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.demo.IndexFiles;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQuery;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.util.FireEventTask;
import vn.spring.zebra.util.FireEventUtil;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServerHelper {
	protected TriUMServer server = null;
	
	public TriUMServerHelper(TriUMServer server) {
		this.server = server;
	}
	protected void initCourses() {
		if(ZebraStatic.DEFAULT_COURSES.length == 0) return;
		
		if(ZebraStatic.MINER_USE_GLOBAL_DAEMON) {
			//default courses, miner
			for(String course : ZebraStatic.DEFAULT_COURSES) {
				try {
					server.zebraMiner.addCourseHisDataDaemon(course);
				}
				catch(Exception e) {e.printStackTrace();}
				
				try {
					server.zebraMiner.addCourseRecommenderDaemon(course);
				}
				catch(Exception e) {e.printStackTrace();}
				
				try {
					server.zebraMiner.addCourseSequenceDaemon(course);
				}
				catch(Exception e) {e.printStackTrace();}
				
				try {
					server.zebraMiner.addCourseDocClassifierDaemon(course);
				}
				catch(Exception e) {e.printStackTrace();}
			}
		}
		
		//default courses, cluster
		for(String course : ZebraStatic.DEFAULT_COURSES) {
			try {
				server.userClustererService.addClusterer(course);
			}
			catch(Exception e) {e.printStackTrace();}
		}

		//Search engine index
		for(String course : ZebraStatic.DEFAULT_COURSES) {
			try {
				File  INDEX_DIR = new File(ZebraStatic.getWowXmlRoot() + ZebraStatic.SEARCH_INDEX_DIR +  course);
			    if(INDEX_DIR.exists()) continue;
			    
			    IndexWriter writer = new IndexWriter(
			    	FSDirectory.open(INDEX_DIR), 
			    	new StandardAnalyzer(Version.LUCENE_CURRENT), true, IndexWriter.MaxFieldLength.LIMITED);
			    
			    ZebraStatic.traceService.trace("Indexing to directory '" + INDEX_DIR + "'...");
			    File  docDir = new File(ZebraStatic.getWowRoot() + course + "/xhtml");
			    if(!docDir.exists()) docDir = new File(ZebraStatic.getWowRoot() + course + "/xml");
			    if(!docDir.exists()) docDir = new File(ZebraStatic.getWowRoot() + course + "/html");
			    
			    IndexFiles.indexDocs(writer, docDir);
			    ZebraStatic.traceService.trace("Optimizing...");
			    writer.optimize();
			    writer.close();
			}
			catch(Exception e) {e.printStackTrace();}
		}

		//default courses, mailing list
		for(String course : ZebraStatic.DEFAULT_COURSES) {
			try {
				server.mailingListService.addCourseMailingListDaemon(course);
			}
			catch(Exception e) {e.printStackTrace();}
		}
	}
	
	protected void deployWebServices() {
		//Web service
		try {
        	File wsddDir = new File(ZebraStatic.getWowRoot() + "src/vn/spring/zebra/ws");
        	if(wsddDir.exists()) {
                AdminClient admin = new AdminClient();
                File[] wsddFiles = wsddDir.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return (file.getName().endsWith(".wsdd"));
                    }
                });
                String url = "http://localhost:" + TriUMQuery.SOAP_QUERY_PORT + 
                	ZebraStatic.getWowContextPath() + "/services/AdminService";
        		for(int i = 0; i < wsddFiles.length; i++) {
        			String[] args = new String[3];
        			args[0] = "-l";
        			args[1] = url;
        			args[2] = wsddFiles[i].getAbsolutePath();
        			admin.process(args);
        		}
        	}
		}
		catch(Exception e) {
			e.printStackTrace(); 
		}
	}

	protected void discardUMs(String userid, String course) {
		Set<String> keys = new HashSet<String>(server.triUMs.keySet());
		String pattern = null;
		if(userid != null && userid.length() > 0) {
			if(course != null && course.length() > 0) pattern = ZebraStatic.makeId(userid, course);
			else                                      pattern = ZebraStatic.makeId(userid, "");
		}
		else {
			if(course != null && course.length() > 0) pattern = ZebraStatic.makeId("", course);
			else                                      pattern = null;
		}
		for(String key : keys) {
			TriUM um = null;
			if(pattern == null)
				um = server.triUMs.remove(key);
			else if(key.indexOf(pattern) != -1)
				um = server.triUMs.remove(key);
			if(um != null) um.discard();
		}
	}

	protected void fireChangeUMEvent(TriUMServerChangeUMEvent evt) {
		FireEventUtil.fireEvent(new FireChangeUMEventTask(evt), ZebraStatic.FIRE_EVENT_WAIT_DONE);
	}
	
	private class FireChangeUMEventTask implements FireEventTask {
		private TriUMServerChangeUMEvent evt = null;
		
		public FireChangeUMEventTask(TriUMServerChangeUMEvent evt) {
			this.evt = evt;
		}
		public void run() {
			for(TriUMServerChangeUMListener listener : server.changeUMListeners) {
				try {
					switch(evt.changeType) {
					case REGISTER:
						listener.umRegistered(evt);
						break;
					case REGISTERDEFAULT:
						listener.umRegisteredDefault(evt);
						break;
					case UNREGISTER:
						listener.umUnregistered(evt);
						break;
					case UNREGISTERALL:
						listener.umUnregisteredAll(evt);
						break;
					case MONITOR:
						listener.umMonitored(evt);
						break;
					case MONITORCOURSE:
						listener.umMonitoredCourse(evt);
						break;
					case UNMONITOR:
						listener.umUnmonitored(evt);
						break;
					case UNMONITORCOURSE:
						listener.umUnmonitoredCourse(evt);
						break;
					case UNMONITORALL:
						listener.umUnmonitoredAll(evt);
						break;
					}
				}
				catch(Exception e) {
					ZebraStatic.traceService.trace("TriUMServer.fireChangeUMEvent causes error: " + e.getMessage());
				}
			}
		}
	}
	
}
