/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * XMLLogDB.java 2.0 25-05-2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 *
 */

package vn.spring.WOW.WOWDB;

import vn.spring.WOW.datacomponents.DotString;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.WOWStatic;

import java.io.*;
import java.util.*;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vn.spring.zebra.log.UserAccessRecord;
import vn.spring.zebra.log.UserAccessRecord.ACTION;
import vn.spring.zebra.util.CommonUtil;

/**
 * This is the implementation of the LogDB interface that stores
 * logging information in XML files. Modified by Loc Nguyen 2008
 */
public class XMLLogDB extends GenericLogDB {
    //the root directory of this database
    private File root = null;

    public XMLLogDB(File proot) throws DatabaseException {
        WOWStatic.checkNull(proot, "root");

        try {
            if (!proot.exists()) {
                proot.mkdir();
            }
            //set and create the directory if necessary
            root = new File(proot, "log");

            if (!root.exists()) {
                root.mkdir();
            }
            //set and create the accesslog dtd if necessary
            File accessdtd = new File(root, "access.dtd");

            if (!accessdtd.exists()) {
                StringBuffer outstr = new StringBuffer();
                outstr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                outstr.append("<!ELEMENT log (user, record*)>\n\n");
                outstr.append("<!ELEMENT user (#PCDATA)>\n");
                outstr.append("<!ELEMENT record (accessdate, sessionid, name, fragment)>\n");
                outstr.append("<!ELEMENT accessdate (#PCDATA)>\n");
                outstr.append("<!ELEMENT sessionid (#PCDATA)>\n");
                outstr.append("<!ELEMENT name (#PCDATA)>\n");
                outstr.append("<!ELEMENT fragment (#PCDATA)>\n");

                FileWriter dtdout = new FileWriter(accessdtd);
                dtdout.write(outstr.toString());
                dtdout.close();
            }

            //set and create the umupdatelog dtd if necessary
            File umupdatedtd = new File(root, "umupdate.dtd");

            if (!umupdatedtd.exists()) {
                StringBuffer outstr = new StringBuffer();
                outstr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                outstr.append("<!ELEMENT log (user, record*)>\n\n");
                outstr.append("<!ELEMENT user (#PCDATA)>\n");
                outstr.append("<!ELEMENT record (accessdate, name, oldval, newval)>\n");
                outstr.append("<!ELEMENT accessdate (#PCDATA)>\n");
                outstr.append("<!ELEMENT name (#PCDATA)>\n");
                outstr.append("<!ELEMENT oldval (#PCDATA)>\n");
                outstr.append("<!ELEMENT newval (#PCDATA)>\n");

                FileWriter dtdout = new FileWriter(umupdatedtd);
                dtdout.write(outstr.toString());
                dtdout.close();
            }

            //set and create the testlog dtd if necessary
            File testdtd = new File(root, "test.dtd");

            if (!testdtd.exists()) {
                StringBuffer outstr = new StringBuffer();
                outstr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                outstr.append("<!ELEMENT log (user, record*)>\n\n");
                outstr.append("<!ELEMENT user (#PCDATA)>\n");
                outstr.append("<!ELEMENT record (accessdate, testname, result*)>\n");
                outstr.append("<!ELEMENT accessdate (#PCDATA)>\n");
                outstr.append("<!ELEMENT testname (#PCDATA)>\n");
                outstr.append("<!ELEMENT result (question, answer+)>\n");
                outstr.append("<!ELEMENT question (#PCDATA)>\n");
                outstr.append("<!ELEMENT answer (#PCDATA)>\n");

                FileWriter dtdout = new FileWriter(testdtd);
                dtdout.write(outstr.toString());
                dtdout.close();
            }
        } catch (IOException e) {
            throw new DatabaseException("unable to create XMLConceptDB in " + root + ": " + e.getMessage());
        }
    }

    public void addTestLog(String user, String testname, Vector results) throws DatabaseException {
        try {
            if (!needLogging()) return;
            File logfile = new File(root, "test_"+user+".xml");
            boolean newfile = !logfile.exists();
            RandomAccessFile raf = new RandomAccessFile(logfile, "rw");
            if (newfile) {
                raf.writeBytes("<?xml version=\"1.0\"?>\n\n");
                raf.writeBytes("<!DOCTYPE log SYSTEM 'test.dtd'>\n\n");
                raf.writeBytes("<log>\n");
                raf.writeBytes("  <user>"+user+"</user>\n");
                raf.writeBytes("</log>");
            }
            raf.seek(raf.length()-6);
            raf.writeBytes("  <record>\n");
            raf.writeBytes("    <accessdate>"+CommonUtil.DateToString(new Date())+"</accessdate>\n");
            raf.writeBytes("    <testname>"+testname+"</testname>\n");
            for (int i=0;i<results.size();i++) {
                Hashtable result = (Hashtable)results.get(i);
                raf.writeBytes("    <result>\n");
                raf.writeBytes("      <question>"+(String)result.get("question")+"</question>\n");
                Vector answers = (Vector)result.get("answers");
                for (int j=0;j<answers.size();j++) {
                    raf.writeBytes("      <answer>"+(String)answers.get(j)+"</answer>\n");
                }
                raf.writeBytes("    </result>\n");
            }
            raf.writeBytes("  </record>\n");
            raf.writeBytes("</log>");
            raf.close();
        } catch (Exception e) {
            throw new DatabaseException("unable to update 'test' log: "+e.getMessage());
        }
    }

    public void addUMUpdate(String name, String oldval, String newval, String user) throws DatabaseException {
        try {
            if (!needLogging()) return;
            File logfile = new File(root, "umupdate_"+user+".xml");
            boolean newfile = !logfile.exists();
            RandomAccessFile raf = new RandomAccessFile(logfile, "rw");
            if (newfile) {
                raf.writeBytes("<?xml version=\"1.0\"?>\n\n");
                raf.writeBytes("<!DOCTYPE log SYSTEM 'umupdate.dtd'>\n\n");
                raf.writeBytes("<log>\n");
                raf.writeBytes("  <user>"+user+"</user>\n");
                raf.writeBytes("</log>");
            }
            raf.seek(raf.length()-6);
            raf.writeBytes("  <record>\n");
            raf.writeBytes("    <accessdate>"+CommonUtil.DateToString(new Date())+"</accessdate>\n");
            raf.writeBytes("    <name>"+name+"</name>\n");
            raf.writeBytes("    <oldval>"+oldval+"</oldval>\n");
            raf.writeBytes("    <newval>"+newval+"</newval>\n");
            raf.writeBytes("  </record>\n");
            raf.writeBytes("</log>");
            raf.close();
        } catch (Exception e) {
            throw new DatabaseException("unable to update 'umupdate' log: "+e.getMessage());
        }
    }

    public void addAccessLog(String name, String user, String sessionId, boolean fragment) throws  DatabaseException {
        try {
            if (!needLogging()) return;
            File logfile = new File(root, "access_"+user+".xml");
            boolean newfile = !logfile.exists();
            RandomAccessFile raf = new RandomAccessFile(logfile, "rw");
            if (newfile) {
                raf.writeBytes("<?xml version=\"1.0\"?>\n\n");
                raf.writeBytes("<!DOCTYPE log SYSTEM 'access.dtd'>\n\n");
                raf.writeBytes("<log>\n");
                raf.writeBytes("  <user>"+user+"</user>\n");
                raf.writeBytes("</log>");
            }
            raf.seek(raf.length()-6);
            raf.writeBytes("  <record>\n");
            raf.writeBytes("    <accessdate>"+CommonUtil.DateToString(new Date())+"</accessdate>\n");
            raf.writeBytes("    <sessionid>"+sessionId+"</sessionid>\n");
            raf.writeBytes("    <name>"+name+"</name>\n");
            raf.writeBytes("    <fragment>"+String.valueOf(fragment)+"</fragment>\n");
            raf.writeBytes("  </record>\n");
            raf.writeBytes("</log>");
            raf.close();
        } catch (Exception e) {
            throw new DatabaseException("unable to update 'access' log: "+e.getMessage());
        }
    }

    public void clearUMLog() throws  DatabaseException {
		clearLog(new FileFilter() {
			public boolean accept(File f) {
				return (f.getName().endsWith(".xml") && f.getName().startsWith("umupdate_"));
			}
		});
    }
    public void clearUMLog(String loginid) throws  DatabaseException {
    	final String finalLoginid = loginid;
		clearLog(new FileFilter() {
			public boolean accept(File f) {
				return (f.getName().endsWith(".xml") && f.getName().startsWith("umupdate_" + finalLoginid));
			}
		});
    }
    
    public void clearAccessLog() throws  DatabaseException {
		clearLog(new FileFilter() {
			public boolean accept(File f) {
				return (f.getName().endsWith(".xml") && f.getName().startsWith("access_"));
			}
		});
    }
    public void clearAccessLog(String loginid) throws  DatabaseException {
    	final String finalLoginid = loginid;
		clearLog(new FileFilter() {
			public boolean accept(File f) {
				return (f.getName().endsWith(".xml") && f.getName().startsWith("access_" + finalLoginid));
			}
		});
    }
    
    private void clearLog(FileFilter filter) throws  DatabaseException {
        try {
            File[] filelist = root.listFiles(filter);
            for (int i = 0; i < filelist.length; i++) {
                filelist[i].delete();
            }
        }
        catch (Exception e) {
            throw new DatabaseException("unable to delete log: " + e.getMessage());
        }
    }
    
    public ArrayList<UserAccessRecord> getUserAccessLog(String userid, String coursename, Date beginDate) throws WOWException {
    	ArrayList<UserAccessRecord> useraccesslog = new ArrayList<UserAccessRecord>();
        File accesslogfile = new File(root, "access_" + userid + ".xml");
        if(!accesslogfile.exists()) return useraccesslog;
    	
    	try {
			ConceptDB cdb = WOWStatic.DB().getConceptDB();
			DOMParser parser = new DOMParser();
			parser.parse(accesslogfile.toString());
			
			Document doc = parser.getDocument();
			Node usernode = doc.getDocumentElement().getFirstChild().getNextSibling();
			if(usernode.getNodeName().equals(userid))
				throw new Exception("This log file doesn't belong to " + userid);
			
			Node recordnode = usernode.getNextSibling();
			while(recordnode != null) {
				NodeList childs = recordnode.getChildNodes();
				UserAccessRecord record = new UserAccessRecord();
				record.setUserId(userid);
				
				for(int i = 0; i < childs.getLength(); i++) {
					Node child = childs.item(i);
					String childname = child.getNodeName();
					String childvalue = (child.getFirstChild() == null ? null : child.getFirstChild().getNodeValue());
					if(childvalue == null) continue;
					else {
						childvalue = childvalue.trim();
						if(childvalue.length() == 0) continue;
					}
					
					if(childname.equals("accessdate")) {
						//set access date
						Date date = null;
						try {date = CommonUtil.StringToDate(childvalue);}
						catch(Exception e) {continue;}
						if(beginDate == null)
							record.setAccessDate(date);
						else if(!date.before(beginDate))
							record.setAccessDate(date);
						else
							continue;
					}
					else if(childname.equals("name")) {
						//Set resource, action, course, concept
						DotString dsResourse = new DotString(childvalue, "|");
						String resource = dsResourse.get(0);
						record.setResource(resource);
						
						if(dsResourse.size() > 2) {
							if(dsResourse.get(1).equals("logoff") || dsResourse.get(1).equals("logout"))
								record.setAction(ACTION.LOGOUT);
							else
								record.setAction(ACTION.VISIT);
						}
						else
							record.setAction(ACTION.VISIT);
						
						String conceptname = cdb.getLinkedConcept(resource);
						if(conceptname == null) continue; //or do something
						
						//Set course name
						if(coursename == null || coursename.length() == 0) {
							DotString ds = new DotString(conceptname);
							if(ds.size() < 2) continue;
							record.setCourseName(ds.get(0));
						}
						else
							record.setCourseName(coursename);
						if(!record.getCourseName().equals(coursename)) continue;
						
						//Set concept name
						record.setConceptName(conceptname);
					}
					else if(childname.equals("sessionid")) {
						record.setSessionId(childvalue);
					}
					else if(childname.equals("fragment")) {
						record.setFragment(Boolean.getBoolean(childvalue));
					}
				}//end for child list
				if(record.isValid()) {
					useraccesslog.add(record);
				}
				recordnode = recordnode.getNextSibling();
			}
    	}
    	catch (Exception e) {
    		System.out.println("getUserAccessLog causes error: " + e.getMessage());
    		//e.printStackTrace();
    	}
    	return useraccesslog;    	
    }
    

}