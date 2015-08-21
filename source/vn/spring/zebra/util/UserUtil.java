/**
 * 
 */
package vn.spring.zebra.util;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.WindowConstants;

import vn.spring.WOW.WOWDB.LogDB;
import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.WOW.exceptions.DatabaseException;
import vn.spring.WOW.exceptions.InvalidProfileException;
import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQuery.QUERY_TYPE;
import vn.spring.zebra.mail.CourseMailingListDaemon;
import vn.spring.zebra.server.TriUMServer;
import vn.spring.zebra.um.DynOverlayBayesUM;
import vn.spring.zebra.um.OverlayBayesUM;
import vn.spring.zebra.um.TriUM;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public final class UserUtil {
	public static String getUserName(String userid) {
		try {
			ProfileDB pdb = ZebraStatic.getProfileDB();
			Profile profile = pdb.getProfile(pdb.findProfile(userid));
			return profile.getAttributeValue("personal", "name");
		}
		catch(Exception e) {e.printStackTrace();}
		return null;
	}
	public static boolean isProfileExists(String userid) {
		try {
			ProfileDB pdb = ZebraStatic.getProfileDB();
			long id = pdb.findProfile(userid);
			return (id > 0 ? true : false);
		}
		catch(Exception e) {return false;}
	}
	public static Profile getProfile(String userid) {
		try {
			ProfileDB pdb = ZebraStatic.getProfileDB();
			return pdb.getProfile(pdb.findProfile(userid));
		}
		catch(Exception e) {return null;}
	}
	public static ArrayList<Profile> getProfileList(boolean doesIgnoreAnonymous) throws DatabaseException, InvalidProfileException {
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		ProfileDB pdb = ZebraStatic.getProfileDB();
		Vector<?> userids = pdb.getProfileList();
		if(userids == null || userids.size() == 0) return profiles;
		for(int i = 0; i < userids.size(); i++) {
			String userid = (String)userids.get(i);
			if(doesIgnoreAnonymous) {
				if(userid.indexOf("an_userID_") != -1 ) continue;
			}
			Profile profile = pdb.getProfile( pdb.findProfile(userid) );
			profiles.add(profile);
		}
		return profiles;
	}
	public static ArrayList<String> getProfileIdList(boolean doesIgnoreAnonymous) throws DatabaseException {
		ArrayList<String> names = new ArrayList<String>();
		ProfileDB pdb = ZebraStatic.getProfileDB();
		Vector<?> userids = pdb.getProfileList();
		if(userids == null || userids.size() == 0) return names;
		for(int i = 0; i < userids.size(); i++) {
			String userid = (String)userids.get(i);
			if(doesIgnoreAnonymous) {
				if(userid.indexOf("an_userID_") != -1) continue;
			}
			names.add(userid);
		}
		return names;
	}
	public static ArrayList<Profile> getAllProfiles(boolean doesIgnoreAnonymous) throws DatabaseException, InvalidProfileException {
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		ProfileDB pdb = ZebraStatic.getProfileDB();
		Vector<?> userids = pdb.getProfileList();
		for(int i = 0; i < userids.size(); i++) {
			String userid = (String)userids.get(i);
			if(doesIgnoreAnonymous) {
				if(userid.indexOf("an_userID_") != -1 ) continue;
			}
			Profile profile = pdb.getProfile(pdb.findProfile(userid));
			profiles.add(profile);
		}
		return profiles;
	}
	
	public static void removeUser(String userid) {
		//Remove profile
		try {
			ProfileDB pdb = ZebraStatic.getProfileDB();
			long id = pdb.findProfile(userid);
			if(id == 0) return;
			pdb.removeProfile(id);
		}
		catch(Throwable e) {
			System.out.println("This user has no profile, causes error: " + e.getMessage());
		}
		finally {}
		
		//remove Bayesian network
		try {
	        String dirPath = ZebraStatic.getWowXmlRoot()  + "/" + OverlayBayesUM.OBUM_DIR_NAME;
	        File rootDir = new File(dirPath);
	        final String ext = OverlayBayesUM.OBUM_EXT;
	        final String fuserid = userid;
	        if(rootDir.exists()) {
	            File[] filelist = rootDir.listFiles(new java.io.FileFilter() {
	                public boolean accept(File file) {
	                    return (file.getName().endsWith(ext) && file.getName().startsWith(fuserid + "$"));
	                }
	            });
	            
	            for (int i = 0; i < filelist.length; i++) {
	                filelist[i].delete();
	            }
	        }
		}
		catch(Throwable e) {
			System.out.println("Removing Bayesian network bif file causes error: " + e.getMessage());
		}
		finally {}

		//remove log file
		try {
			LogDB ldb = ZebraStatic.getLogDB();
			ldb.clearAccessLog(userid);
			ldb.clearUMLog(userid);
		}
		catch(Throwable e) {
			System.out.println("Removing log file causes error: " + e.getMessage());
		}
		finally {}

		//remove Test file
		try {
	        String dirPath = ZebraStatic.getWowXmlRoot()  + ZebraStatic.getWowConfig().Get("itemspath"); 
	        File rootDir = new File(dirPath);
	        if(rootDir.exists()) {
	            File[] courseDirlist = rootDir.listFiles(new java.io.FileFilter() {
	                public boolean accept(File file) {
	                    return (file.isDirectory());
	                }
	            });
	            
	            final String ext = ZebraStatic.getWowConfig().Get("testeditortypefile");
	            for (int i = 0; i < courseDirlist.length; i++) {
	            	File testLogDir = new File(courseDirlist[i], ZebraStatic.getWowConfig().Get("studentlogpath").substring(1));
	    	        final String fuserid = userid;
		            File[] loglist = testLogDir.listFiles(new java.io.FileFilter() {
		                public boolean accept(File file) {
		                    return (file.getName().equals(fuserid + ext));
		                }
		            });
		            for (int j = 0; j < loglist.length; j++) {
		            	loglist[i].delete();
		            }
	            }
	        }
		}
		catch(Throwable e) {
			System.out.println("Removing Test log file causes error: " + e.getMessage());
		}
		finally {}
		
		//remove feedback file
		try {
			File feedbackFile = new File(ZebraStatic.getWowXmlRoot() + ZebraStatic.FEEDBACK_DIR + "feedback_" + userid + ".xml");
			if(feedbackFile.exists()) feedbackFile.delete();
		}
		catch(Throwable e) {
			System.out.println("Removing feedback file causes error: " + e.getMessage());
		}
		finally {}
		
		//remove search file
		try {
			ZebraStatic.searchLogService.clearSearchLog(userid);
		}
		catch(Throwable e) {
			System.out.println("Removing search file causes error: " + e.getMessage());
		}
		finally {}

		//remove in mailing list
		try {
			ArrayList<CourseMailingListDaemon> daemons = 
				TriUMServer.getInstance().getMailingListService().getCourseMailingListDaemons();
			for(CourseMailingListDaemon daemon : daemons) {
				daemon.getCourseMailingList().removeUser(userid);
			}
		}
		catch(Throwable e) {
			System.out.println("Removing in mailing list causes error: " + e.getMessage());
		}
		finally {}
	}
	
	public static void removeUsers(ArrayList<String> userids) {
		for(String userid : userids) {
			removeUser(userid);
		}
	}
	public static void removeAllUsers() {
		try {
			ArrayList<String> userids = getProfileIdList(true);
			removeUsers(userids);
		}
		catch(Exception e) {
			System.out.println("Removing all users causes error: " + e.getMessage());
		}
	}
    public static void monitorAllUsers(String course, boolean ignoreAnonymous) {
    	ArrayList<String> userids = new ArrayList<String>();
    	try {
    		userids = getProfileIdList(ignoreAnonymous);
    	}
    	catch(Exception e) {e.printStackTrace();}
    	
    	for(String userid : userids) {
        	try {
        		TriUMServer.getInstance().monitorUM(userid, course);
        	}
        	catch(Exception e) {e.printStackTrace();}
    	}
    }
    
    public static void unmonitorAllUsers(String course, boolean ignoreAnonymous) {
    	try {
    		TriUMServer server = TriUMServer.getInstance();
    		ArrayList<TriUM> ums = server.getAllMonitoredUM();
    		for(TriUM um : ums) {
    			String userid = um.getUserId();
    			String thiscourse = um.getCourse();
    			if(thiscourse.equals(course)) server.unmonitorUM(userid, thiscourse);
    				
    		}
    	}
    	catch(Exception e) {e.printStackTrace();}
    }
	
    public static OverlayBayesUM getBayesUM(String userid, String course, QUERY_TYPE evaltype) {
		OverlayBayesUM  knowledge = null;
		if(evaltype == QUERY_TYPE.OVERLAY_BAYESIAN) {
			knowledge = TriUM.getBayesUM(userid, course);
		}
		else if(evaltype == QUERY_TYPE.DYN_OVERLAY_BAYESIAN) {
			knowledge = DynOverlayBayesUM.getDynBayesUM(userid, course);
		}
		return knowledge;
    }
	
    public static void showSetUserPasswordDlg(final JFrame parent, final String userid) {
		final Profile profile = getProfile(userid);
		if(profile == null) {
			JOptionPane.showMessageDialog(parent, "User not exists!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		final JDialog changeDlg = new JDialog(parent, "Set User Password", true);
		changeDlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		JPanel mainPane = new JPanel(); mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
		changeDlg.add(mainPane);
		
		JPanel tempPane = null;
		
		tempPane = new JPanel();
		final JPasswordField pwd = new JPasswordField(8);
		tempPane.add(new JLabel("Type password:"));
		tempPane.add(pwd);
		mainPane.add(tempPane);
		
		tempPane = new JPanel();
		final JPasswordField repwd = new JPasswordField(8);
		tempPane.add(new JLabel("Re-type password:"));
		tempPane.add(repwd);
		mainPane.add(tempPane);
		
		JButton ok = new JButton(new AbstractAction("OK") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				String sPwd = pwd.getText().trim();
				String sRepwd = repwd.getText().trim();
				if(sPwd.length() == 0 || sRepwd.length() == 0) {
					JOptionPane.showMessageDialog(parent, "Password can't be empty", "Error!", JOptionPane.ERROR_MESSAGE);
					changeDlg.dispose(); return;
				}
				if(!sPwd.equals(sRepwd)) {
					JOptionPane.showMessageDialog(parent, "You type password in two times differently!", "Notice", JOptionPane.INFORMATION_MESSAGE);
					changeDlg.dispose(); return;
				}
				try {
					profile.setAttributeValue("personal", "password", sPwd);
					ProfileDB pdb = ZebraStatic.getProfileDB();
					pdb.setProfile(profile.id, profile);
					
					JOptionPane.showMessageDialog(parent, "Set user password successfully", "Notice", JOptionPane.INFORMATION_MESSAGE);
					changeDlg.dispose(); return;
				}
				catch(Exception ex) {
					ex.printStackTrace();
					changeDlg.dispose(); return;
				}
			}
		});
		JButton cancel = new JButton(new AbstractAction("Cancel") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
					changeDlg.dispose(); return;
			}
		});
		tempPane = new JPanel(); 
		tempPane.add(ok);
		tempPane.add(cancel);
		mainPane.add(tempPane);
		
		changeDlg.pack();
		changeDlg.setVisible(true);
	}
    
}
