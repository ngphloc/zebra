/**
 * 
 */
package vn.spring.zebra.server;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQueryFrame;
import vn.spring.zebra.collab.UserClustererPane;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.feedback.UserFeedback;
import vn.spring.zebra.feedback.UserFeedbackReportViewer;
import vn.spring.zebra.feedback.UserFeedbackService;
import vn.spring.zebra.feedback.UserFeedbackTableReportDlg;
import vn.spring.zebra.mail.CourseMailingListDaemon;
import vn.spring.zebra.mail.CourseMailingListManager;
import vn.spring.zebra.mail.MailSenderDlg;
import vn.spring.zebra.mining.textmining.AbstractKeywordDoc;
import vn.spring.zebra.report.ReportManager;
import vn.spring.zebra.report.UserReportDlg;
import vn.spring.zebra.um.TriUM;
import vn.spring.zebra.um.gui.RegisterInputDlg;
import vn.spring.zebra.um.gui.TriUMContainerFinderGUI;
import vn.spring.zebra.um.gui.TriUMContainerTableImpl;
import vn.spring.zebra.um.gui.TriUMPane;
import vn.spring.zebra.um.gui.UserCourseInputDlg;
import vn.spring.zebra.util.CommonUtil;
import vn.spring.zebra.util.UserUtil;
import vn.spring.WOW.WOWDB.ProfileDB;
import vn.spring.WOW.datacomponents.Profile;
import vn.spring.bayes.InterchangeFormat.IFException;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServerUIHelper {
	protected TriUMServerUI serverUI = null;
	
	public TriUMServerUIHelper(TriUMServerUI serverUI) {
		this.serverUI = serverUI;
	}
	
	public static void runServerUI(TriUMServer server) throws ZebraException, IFException, IOException, Exception {
		TriUMServerUIThread uiThread = new TriUMServerUIThread(server);
		uiThread.start();
	}

	public void initialize() throws ZebraException, IFException, IOException, Exception {
		serverUI.setTitle("Zebra: Triangular User Modeling Server");
		serverUI.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		try {
	        Image image = Toolkit.getDefaultToolkit().getImage(
	        		getClass().getResource(ZebraStatic.IMAGE_DIR + ZebraStatic.IMAGE_LARGE_ICON_FILENAME));
	        serverUI.setIconImage(image);
		}
		catch(Exception e) {
			System.out.println("Load Image icon cause error: " + e.getMessage());
			e.printStackTrace();
		}
		
		//Very important
		serverUI.clustererUI = new TriUMserverCommunityUI(serverUI.server.getUserClustererService(), serverUI.server.getCollabService(), serverUI);
		serverUI.monitoredUsers = new TriUMContainerTableImpl(serverUI);
		serverUI.userPane = new TriUMServerUIUserPane(serverUI);
		serverUI.msgArea = new JTextArea(5, 10); serverUI.msgArea.setAutoscrolls(true); serverUI.msgArea.setEditable(false);
		serverUI.msgArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!javax.swing.SwingUtilities.isRightMouseButton(e) ) return;
				JPopupMenu contextMenu = new JPopupMenu();
				JMenuItem clear = CommonUtil.makeMenuItem(null, "Clear", new AbstractAction() {
					private static final long serialVersionUID = 1L;
					public void actionPerformed(ActionEvent e) {
						serverUI.msgArea.setText("");
					}
				});
				contextMenu.add(clear); contextMenu.show((Component)e.getSource(), e.getX(), e.getY());
			}
		});
		
		JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainPane.setDividerLocation(350);
		JPanel monitoredPane = new JPanel(); monitoredPane.setLayout(new BorderLayout());
		monitoredPane.add(new TriUMContainerFinderGUI(serverUI, serverUI.monitoredUsers), BorderLayout.NORTH);
		monitoredPane.add((JComponent)serverUI.monitoredUsers, BorderLayout.CENTER);
		mainPane.add(monitoredPane, JSplitPane.TOP);
		//
		JPanel msgPanel = new JPanel(); msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.PAGE_AXIS));
		boolean initShowMsg = true;
		//
		final JScrollPane msgArea = new JScrollPane(serverUI.msgArea);
		msgArea.setVisible(initShowMsg);
		msgArea.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		//
		final TriUMServerUIStatus statusBar = new TriUMServerUIStatus(serverUI.server);
		statusBar.setVisible(!initShowMsg);
		statusBar.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		//
		final JCheckBox showMsg = new JCheckBox(initShowMsg ? "Show server log" : "Hide server log", initShowMsg);
		showMsg.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		showMsg.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				msgArea.setVisible(showMsg.isSelected());
				statusBar.setVisible(!showMsg.isSelected());
				showMsg.setText(showMsg.isSelected() ? "Show server log" : "Hide server log");
			}
		});
		//
		msgPanel.add(showMsg);
		msgPanel.add(msgArea);
		msgPanel.add(statusBar);
		//
		mainPane.add(msgPanel, JSplitPane.BOTTOM);
		
		JSplitPane centerPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		centerPane.setDividerLocation(280);
		centerPane.add(serverUI.userPane, JSplitPane.LEFT);
		centerPane.add(mainPane, JSplitPane.RIGHT);
		
		
		serverUI.setJMenuBar(createMenuBar());
		Container contentPanel = serverUI.getContentPane();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(createToolBar(), BorderLayout.NORTH);
		contentPanel.add(centerPane, BorderLayout.CENTER);
		
		serverUI.setSize(new Dimension(900, 700));
		//serverUI.setExtendedState(Frame.MAXIMIZED_BOTH);
		//serverUI.pack();
		serverUI.setVisible(true);
	}

	public static ActionListener createRegisterUserActionListener(final TriUMServerUI serverUI) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegisterInputDlg input = new RegisterInputDlg(serverUI);
				if(!input.isOK) return;
				
				final HashMap<String, String> personalAttrs = input.personalAttrs;
				personalAttrs.put("name", input.username);
				if(input.userid == null || input.password == null || input.course == null || input.directory == null) {
					JOptionPane.showMessageDialog(serverUI, "Error! Some important fields are empty");
					return;
				}
				
				try {
					TriUM um = serverUI.server.getUM(input.userid, input.course, false);
					if(um == null) {
						serverUI.server.registerUM(input.userid, input.password, input.course, input.directory, input.personalAttrs);
					}
				}
				catch(Exception ex) {ex.printStackTrace();JOptionPane.showMessageDialog(null, "Can not register this user in this course");}
			}
		}; 
	}

	public static ActionListener createRegisterDefaultUsersActionListener(final TriUMServerUI serverUI, final boolean disable) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(false) {
					JOptionPane.showMessageDialog(serverUI, "This function is disabled because there is much risky");
					return;
				}
				
				if(JOptionPane.showConfirmDialog(serverUI, 
						"Do you want to register (add) default users?\n All existing users are removed ", 
						"Create Users", 
						JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) 
						return;
				final JDialog dlg = new JDialog(serverUI, "Register default users", true);
				dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				dlg.setLayout(new BorderLayout());
				dlg.add(new JLabel("Default User List:"), BorderLayout.NORTH);
				final JTextArea textArea = new JTextArea(5, 10);
				textArea.setAutoscrolls(true);
				StringBuffer data = new StringBuffer();
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(getClass().getResourceAsStream("/vn/spring/zebra/resource/users.txt")));
					String line = null;
					while((line = reader.readLine()) != null) {
						data.append(line + "\n");
					}
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(serverUI, "There is some error when read the file.\n\"/vn/spring/zebra/resource/users.txt\"\nError: " + ex.getMessage(), 
							"Reading file error", JOptionPane.ERROR_MESSAGE);
				}
				textArea.setText(data.toString());
				dlg.add(new JScrollPane(textArea), BorderLayout.CENTER);
				
				JPanel toolbar = new JPanel();
				JButton ok = new JButton(new AbstractAction("OK") {
					private static final long serialVersionUID = 1L;
					public void actionPerformed(ActionEvent e) {
						dlg.setVisible(false); dlg.dispose();
						String text = textArea.getText();
						if(text != null || text.length() > 0) {
							StringReader reader = new StringReader(text);
							serverUI.server.registerDefaultUMs(reader);
						}
					}
				});
				toolbar.add(ok);
				JButton cancel = new JButton(new AbstractAction("Cancel") {
					private static final long serialVersionUID = 1L;
					public void actionPerformed(ActionEvent e) {
						dlg.setVisible(false); dlg.dispose();
					}
				});
				toolbar.add(cancel);
				dlg.add(toolbar, BorderLayout.SOUTH);
				dlg.setSize(600, 400);
				dlg.setVisible(true);
			}
		};
	}

	public static ActionListener createUnregisterUserActionListener(final TriUMServerUI serverUI, final String default_userid) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String final_userid = JOptionPane.showInputDialog(serverUI, "enter userid to remove", default_userid);

				if(final_userid == null || final_userid.length() == 0) {
					JOptionPane.showMessageDialog(serverUI, "User ID musn't be empty");
					return;
				}
				if(JOptionPane.showConfirmDialog(serverUI, 
						"Do you want to unregister (remove) this user? (" + final_userid + ").\nBe careful becauses this user will be removed permanently", 
						"Unregister user", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
					return;
				}
				
				try {
					serverUI.server.unregisterUM(final_userid);
				}
				catch(Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Can not unregister this user in this course");
				}
			}
		}; 
	}

	public static ActionListener createUnregisterAllUsersActionListener(final TriUMServerUI serverUI) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(serverUI, 
						"Do you want to unregister (remove) all existing users?", 
						"Remove Users", 
						JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) 
					return;
				serverUI.server.unregisterAllUMs();
			}
		};
	}

	public static ActionListener createReportManagerActionListener(final TriUMServerUI serverUI) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ReportManager(serverUI);
			}
		};
	}

	public static ActionListener createUserReportActionListener(final TriUMServerUI serverUI, final String defaultuserid, final String defaultcourse) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userid = null, course = null;
				UserCourseInputDlg input = new UserCourseInputDlg(
						serverUI, defaultuserid, defaultcourse, "Enter User ID and course");
				if(!input.isOK) return;
				userid = input.userid;
				course = input.course;
				
				TriUM um = serverUI.monitoredUsers.getUM(userid, course);
				if(um == null) {
					JOptionPane.showMessageDialog(serverUI, "This user must be monitored before");
					return;
				}
				
				new UserReportDlg(serverUI, um);
			}
		};
	}

	public static ActionListener createUserFeedbackReportActionListener(final TriUMServerUI serverUI, final String userid, final String defaultcourse) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String course = CommonUtil.showInputCourseDlg(serverUI, "choose course reported", defaultcourse);
				if(course == null || course.length() == 0) {JOptionPane.showMessageDialog(serverUI, "No course"); return;}
				
				if(userid == null || userid.length() == 0) {
					UserFeedbackService service = new UserFeedbackService(course);
					service.load();
					new UserFeedbackReportViewer(serverUI, service);
				}
				else {
					try {
						UserFeedback user = new UserFeedback(userid, course);
						user.load();
						new UserFeedbackReportViewer(serverUI, user);
					}
					catch(Exception ex) {ex.printStackTrace();}
				}
			}
		};
	}

	public static ActionListener createMailSenderActionListener(final TriUMServerUI serverUI, final ArrayList<String> userids) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MailSenderDlg(serverUI, userids);
			}
		};
	}

	public static ActionListener createMonitorUserActionListener(final TriUMServerUI serverUI, final String default_userid, final String default_course) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserCourseInputDlg input = new UserCourseInputDlg(serverUI, default_userid, default_course, "Monitor - User Course Input");
				if(!input.isOK) return;
				
				if(input.userid == null || input.course == null) {
					JOptionPane.showMessageDialog(serverUI, "Error! user id or course is empty"); 
					return;
				}
				
				try {
					TriUM um = serverUI.server.getUM(input.userid, input.course, false);
					if(um == null) {
						serverUI.server.monitorUM(input.userid, input.course);
					}
				}
				catch(Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(serverUI, "Can not monitor this user in this course");
				}
			}
		}; 
	}
	
	public static ActionListener createMonitorCourseActionListener(final TriUMServerUI serverUI, final String default_course) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String course = CommonUtil.showInputCourseDlg(serverUI, "monitor course", default_course);
				if(course == null) {
					JOptionPane.showMessageDialog(serverUI, "Error! Course is empty");
					return;
				}
				try {
					serverUI.server.monitorCourse(course);
				}
				catch(Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(serverUI, "Can not monitor this course");
				}
			}
		};
	}
	
	public static ActionListener createUnmonitorUserActionListener(final TriUMServerUI serverUI, final String default_userid, final String default_course) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserCourseInputDlg input = new UserCourseInputDlg(serverUI, default_userid, default_course, "Unmonitor - User Course Input");
				if(!input.isOK) return;
				if(input.userid == null || input.course == null) {
					JOptionPane.showMessageDialog(serverUI, "Error! user id or course is empty"); 
					return;
				}
				if(JOptionPane.showConfirmDialog(serverUI, 
						"Do you want to unmonitor this user model? (" + input.userid + ", " + input.course + ")", 
						"Unmonitor user", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
					return;
				}
				
				try {serverUI.server.unmonitorUM(input.userid, input.course);}
				catch(Exception ex) {ex.printStackTrace(); System.out.println(ex.getMessage());}

			}
		}; 
	}
	public static ActionListener createUnmonitorCourseActionListener(final TriUMServerUI serverUI, final String default_course) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String course = CommonUtil.showInputCourseDlg(serverUI, "unmonitor course", default_course);
				if(course == null) {
					JOptionPane.showMessageDialog(serverUI, "Error! Course is empty");
					return;
				}
				serverUI.server.unmonitorCourse(course);
			}
		};
	}
	public static ActionListener createUnmonitorAllUsersActionListener(final TriUMServerUI serverUI) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(serverUI, 
						"Do you want to unmonitor all existing users?", 
						"Unmonitor Users", 
						JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) 
					return;
				serverUI.server.unmonitorAllUMs();
			}
		};
	}
	
	private ActionListener createClusteringActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String course = CommonUtil.showInputCourseDlg(serverUI, "cluster course", null);
				if(course == null) {
					JOptionPane.showMessageDialog(serverUI, "Error! Course is empty");
					return;
				}
				try {serverUI.server.getUserClustererService().addClusterer(course);}
				catch(Exception ex) {ex.printStackTrace();}
				serverUI.clustererUI.setVisible(true);
			}
		}; 
	}
	private ActionListener createUnclusteringActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String course = null;
				UserClustererPane clustererPane = serverUI.clustererUI.getCurClustererPane();
				if(clustererPane != null) course = clustererPane.getCourse();
				course = CommonUtil.showInputCourseDlg(serverUI, "uncluster course", course);
				if(course == null) {
					JOptionPane.showMessageDialog(serverUI, "Error! user id or course is empty");
					return;
				}
				course = course.trim();
				
				try {serverUI.server.getUserClustererService().removeClusterer(course);}
				catch(Exception ex) {ex.printStackTrace();}
				serverUI.clustererUI.setVisible(true);
			}
		}; 
	}
	private ActionListener createUnclusteringAllActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {serverUI.server.getUserClustererService().removeAllClusterers();}
				catch(Exception ex) {ex.printStackTrace();}
				serverUI.clustererUI.setVisible(true);
			}
		}; 
	}
	private ActionListener createTrainingStaticKnowledge() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TriUM um = serverUI.monitoredUsers.getCurUM();
				if(um == null) return;
				final String userid = um.getUserId(), course = um.getCourse();
				try {
					serverUI.server.zebraNetworker.getStaticKnowledgeDaemon(userid, course)._update(userid, course);
				}
				catch(Exception ex) {ex.printStackTrace();}
				TriUMPane umPane = serverUI.monitoredUsers.getUMPane(userid, course);
				if(umPane != null) umPane.selectKnowledgePane();
			}
		}; 
	}
	private ActionListener createTrainingDynKnowledge() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final TriUM um = serverUI.monitoredUsers.getCurUM();
				if(um == null) return;
				
				try {um.getDynKnowledge()._learn();}
				catch(Exception ex) {ex.printStackTrace();}
				
				TriUMPane umPane = serverUI.monitoredUsers.getUMPane(um.getUserId(), um.getCourse());
				if(umPane != null) umPane.selectKnowledgePane();
			}
		}; 
	}
	private ActionListener createTrainingLearningStyle() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final TriUM um = serverUI.monitoredUsers.getCurUM();
				if(um == null) return;
				
				try {um.getLearningStyleModel().perform();}
				catch(Exception ex) {ex.printStackTrace();}
				
				TriUMPane umPane = serverUI.monitoredUsers.getUMPane(um.getUserId(), um.getCourse());
				if(umPane != null) umPane.selectLearningStylePane();
			}
		}; 
	}
	private ActionListener createTrainingLearningHistoryData() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final TriUM um = serverUI.monitoredUsers.getCurUM();
				if(um == null) return;
				
				try {um.getHisData().update();}
				catch(Exception ex) {System.out.println(ex.getMessage()); ex.printStackTrace();}
				
				TriUMPane umPane = serverUI.monitoredUsers.getUMPane(um.getUserId(), um.getCourse());
				if(umPane != null) {
					umPane.selectLearningHistoryPane();
					umPane.getHisPane().selectDataPane();
				}
			}
		}; 
	}
	private ActionListener createTrainingRecommender() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final TriUM um = serverUI.monitoredUsers.getCurUM();
				if(um == null) return;
				
				try {um.getRecommender().perform();}
				catch(Exception ex) {ex.printStackTrace();}
				
				TriUMPane umPane = serverUI.monitoredUsers.getUMPane(um.getUserId(), um.getCourse());
				if(umPane != null) {
					umPane.selectLearningHistoryPane();
					umPane.getHisPane().selectCourseRecommenderPane();
				}
			}
		}; 
	}
	private ActionListener createTrainingLearningPath() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final TriUM um = serverUI.monitoredUsers.getCurUM();
				if(um == null) return;
				
				try {um.getSequence().perform();}
				catch(Exception ex) {ex.printStackTrace();}
				
				TriUMPane umPane = serverUI.monitoredUsers.getUMPane(um.getUserId(), um.getCourse());
				if(umPane != null) {
					umPane.selectLearningHistoryPane();
					umPane.getHisPane().selectCourseRecommenderPane();
				}
			}
		}; 
	}
	private ActionListener createTrainingAll() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final TriUM um = serverUI.monitoredUsers.getCurUM();
				if(um == null) return;
				
				try {
					//final String userid = um.getUserId(), course = um.getCourse();
			        //if(ZebraStatic.NETWORKER_DOES_PERFORM_STATIC_KNOWLEDGE_DAEMON_TASK)
			        //	serverUI.server.zebraNetworker.getStaticKnowledgeDaemon(userid, course)._update(userid, course);
			        //if(ZebraStatic.NETWORKER_DOES_PERFORM_DYN_KNOWLEDGE_DAEMON_TASK)
			        //	um.getDynKnowledge()._learn();
					um.getLearningStyleModel().perform();
					um.getHisData().update();
					um.getRecommender().perform();
					um.getSequence().perform();
				}
				catch(Exception ex) {ex.printStackTrace();}
			}
		}; 
	}
	
	private ActionListener createFeedbackReport(final String defaultcourse) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String course = CommonUtil.showInputCourseDlg(serverUI, "choose the course reported", defaultcourse);
				if(course == null || course.length() == 0) {
					JOptionPane.showMessageDialog(serverUI, "No course"); return;
				}
				UserFeedbackService service = new UserFeedbackService(course);
				service.load();
				new UserFeedbackTableReportDlg(serverUI, service);
			}
		};
	}

	public static ActionListener createDocClassActionListener(final TriUMServerUI serverUI, final String defaultuserid, final String defaultcourse) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserCourseInputDlg input = new UserCourseInputDlg(serverUI, defaultuserid, defaultcourse, "Find User Interest/Document Classification");
				if(!input.isOK) return;
				if(input.userid == null || input.course == null) {
					JOptionPane.showMessageDialog(serverUI, "Error! user id or course is empty"); 
					return;
				}
				try {
					String docclass = serverUI.server.communicateService.
						getQueryDelegator().getDocClass(input.userid, input.course);
					if(AbstractKeywordDoc.isNoneDocClass(docclass))
						JOptionPane.showMessageDialog(serverUI, "User \"" + input.userid + "\" has no Document Class / User Interest in course \"" + 
								input.course + "\"", 
							"User Interest/Document Class", JOptionPane.INFORMATION_MESSAGE);
					else
						JOptionPane.showMessageDialog(serverUI, "Document Class/Interest of user \"" + 
							input.userid + "\" is \"" + docclass + "\".\n In the course \"" + input.course + "\"", 
							"User Interest/Document Class", JOptionPane.INFORMATION_MESSAGE);
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(serverUI, "Can't query user interest", 
							"Error classification", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
				
			}
		};
	}

	public static ActionListener createMLSendMailActionListener(final TriUMServerUI serverUI, final String defaultcourse) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String course = CommonUtil.showInputCourseDlg(serverUI, "choose the course sent mail", defaultcourse);
				if(course == null || course.length() == 0) {
					JOptionPane.showMessageDialog(serverUI, "No course"); return;
				}
				new MailSenderDlg(serverUI, course, new ArrayList<String>());
			}
		};
	}

	public static ActionListener createMLSendReportActionListener(final TriUMServerUI serverUI, final String defaultcourse) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String course = CommonUtil.showInputCourseDlg(serverUI, "choose the course sent mail", defaultcourse);
				if(course == null || course.length() == 0) {
					JOptionPane.showMessageDialog(serverUI, "No course"); return;
				}
				try {
					CourseMailingListDaemon daemon = TriUMServer.getInstance().getMailingListService().
						getCourseMailingListDaemon(course);
					if(daemon == null) {
						JOptionPane.showMessageDialog(serverUI, "Mailing List service isn't started"); 
						return;
					}
					daemon.getCourseMailingList().sendReport(null, null);
					JOptionPane.showMessageDialog(serverUI, "Individual Report is sent to each user"); 
				}
				catch(Exception ex) {ex.printStackTrace();} 
			}
		};
	}

	public static ActionListener createMLRegActionListener(final TriUMServerUI serverUI, final String defaultuserid, final String defaultcourse, final boolean reg) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserCourseInputDlg input = new UserCourseInputDlg(serverUI, defaultuserid, defaultcourse, 
						reg ? "Registering Mailing List" : "Unregistering Mailing List");
				if(!input.isOK) return;
				if(input.userid == null || input.course == null) {
					JOptionPane.showMessageDialog(serverUI, "Error! user id or course is empty"); 
					return;
				}
				try {
					CourseMailingListDaemon daemon = serverUI.server.getMailingListService().getCourseMailingListDaemon(input.course);
					if(daemon == null) {
						JOptionPane.showMessageDialog(serverUI, "Mailing List service daemon isn't started");
					}
					else {
						if(reg) daemon.getCourseMailingList().addUser(input.userid);
						else    daemon.getCourseMailingList().removeUser(input.userid);
					}
				}
				catch(Exception ex) {ex.printStackTrace();}
			}
		};
	}

	public static ActionListener createAllMLRegActionListener(final TriUMServerUI serverUI, final ArrayList<String> userids, final String defaultcourse, final boolean reg) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String course = CommonUtil.showInputCourseDlg(serverUI, " register (unregister) mailing list", defaultcourse);
				if(course == null || course.length() == 0) {
					JOptionPane.showMessageDialog(serverUI, "No course"); return;
				}
				try {
					if(JOptionPane.showConfirmDialog(serverUI, 
						"Are you sure to register (unregister) all users to mailing list of course \n\"" + course + "\"") != JOptionPane.OK_OPTION)
						return;
					CourseMailingListDaemon daemon = serverUI.server.getMailingListService().getCourseMailingListDaemon(course);
					if(daemon == null) {
						JOptionPane.showMessageDialog(serverUI, "Mailing List service daemon isn't started");
					}
					else {
						final ArrayList<String> temp_userids;
						if(userids == null) temp_userids = UserUtil.getProfileIdList(ZebraStatic.IGNORE_ANONYMOUS);
						else                temp_userids = userids;
						for(String userid : temp_userids) {
							if(reg) daemon.getCourseMailingList().addUser(userid);
							else    daemon.getCourseMailingList().removeUser(userid);
						}
					}
				}
				catch(Exception ex) {ex.printStackTrace();}
			}
		};
	}

	public static ActionListener createMLManagerActionListener(final TriUMServerUI serverUI, final String defaultcourse) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String course = CommonUtil.showInputCourseDlg(serverUI, "manage mailing list", defaultcourse);
				if(course == null || course.length() == 0) {
					JOptionPane.showMessageDialog(serverUI, "No course"); return;
				}
				try {
					CourseMailingListDaemon daemon = TriUMServer.getInstance().getMailingListService().
						getCourseMailingListDaemon(course);
					if(daemon == null) {
						JOptionPane.showMessageDialog(serverUI, "Mailing List service isn't started"); 
						return;
					}
					new CourseMailingListManager(serverUI, daemon.getCourseMailingList());
				}
				catch(Exception ex) {ex.printStackTrace();} 
				
			}
		};
	}


	private ActionListener createRefresh() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					serverUI.monitoredUsers.update(serverUI.server.getAllMonitoredUM());
					serverUI.userPane.update();
				}
				catch(Exception ex) {ex.printStackTrace();}
			}
		}; 
	}

	private JMenuBar createMenuBar() {
		String temp_cur_userid = null;
		String temp_cur_course = null;
		TriUM um = serverUI.monitoredUsers.getCurUM();
		if(um != null) {
			temp_cur_userid = um.getUserId();
			temp_cur_course = um.getCourse();
		}
		final String cur_userid = temp_cur_userid;
		final String cur_course = temp_cur_course;
		
		JMenu menuUser = new JMenu("User");
		menuUser.setMnemonic(KeyEvent.VK_U);
		
		JMenuItem register = CommonUtil.makeMenuItem(null, "Register User", createRegisterUserActionListener(serverUI));
		menuUser.add(register);

		JMenuItem registerdefault = CommonUtil.makeMenuItem(null, "Register Default Users", createRegisterDefaultUsersActionListener(serverUI, true));
		menuUser.add(registerdefault);

		menuUser.addSeparator();

		JMenuItem unregister = CommonUtil.makeMenuItem(null, "Unregister User", createUnregisterUserActionListener(serverUI, cur_userid));
		menuUser.add(unregister);
		
		JMenuItem unregisterall = CommonUtil.makeMenuItem(null, "Unregister All Users", createUnregisterAllUsersActionListener(serverUI));
		menuUser.add(unregisterall);

		menuUser.addSeparator();
		
		JMenuItem monitorUser = CommonUtil.makeMenuItem(null, "Monitor User", createMonitorUserActionListener(serverUI, cur_userid, cur_course));
		menuUser.add(monitorUser);
		
		JMenuItem unmonitorUser = CommonUtil.makeMenuItem(null, "Unmonitor User", createUnmonitorUserActionListener(serverUI, cur_userid, cur_course));
		menuUser.add(unmonitorUser);

		menuUser.addSeparator();

		JMenuItem unmonitorAllUsers = CommonUtil.makeMenuItem(null, "Unmonitor All Users", createUnmonitorAllUsersActionListener(serverUI));
		menuUser.add(unmonitorAllUsers);

		menuUser.addSeparator();
		
		JMenu ml = new JMenu("Mailing List");
		menuUser.add(ml);
		//
		JMenuItem mlReg = CommonUtil.makeMenuItem(null, "Add to Mailing List", createMLRegActionListener(serverUI, cur_userid, cur_course, true));
		ml.add(mlReg);
		JMenuItem mlUnreg = CommonUtil.makeMenuItem(null, "Remove from Mailing List", createMLRegActionListener(serverUI, cur_userid, cur_course, true));
		ml.add(mlUnreg);
		//
		ml.addSeparator();
		//
		JMenuItem mlAllReg = CommonUtil.makeMenuItem(null, "Add to Mailing List (ALL)", createAllMLRegActionListener(serverUI, null, cur_course, true));
		ml.add(mlAllReg);
		JMenuItem mlAllUnreg = CommonUtil.makeMenuItem(null, "Remove from Mailing List (ALL)", createAllMLRegActionListener(serverUI, null, cur_course, true));
		ml.add(mlAllUnreg);
		//
		ml.addSeparator();
		//
		JMenuItem mlSendMail = CommonUtil.makeMenuItem(null, "Send Mail (Mailing List)", createMLSendMailActionListener(serverUI, cur_course));
		ml.add(mlSendMail);
		//
		JMenuItem mlSendReport = CommonUtil.makeMenuItem(null, "Send Report (Mailing List)", createMLSendReportActionListener(serverUI, cur_course));
		ml.add(mlSendReport);

		menuUser.addSeparator();

		ArrayList<String> userids = new ArrayList<String>();
		if(cur_userid != null && cur_userid.length() > 0)
			userids.add(cur_userid);
		JMenuItem mailsender = CommonUtil.makeMenuItem(null, "Send Mail", createMailSenderActionListener(serverUI, userids));
		menuUser.add(mailsender);

		JMenu menuCourse = new JMenu("Course");
		menuCourse.setMnemonic(KeyEvent.VK_C);
		
		JMenuItem monitorCourse = CommonUtil.makeMenuItem(null, "Monitor Course", createMonitorCourseActionListener(serverUI, cur_course));
		menuCourse.add(monitorCourse);
		JMenuItem unmonitorCourse = CommonUtil.makeMenuItem(null, "Unmonitor Course", createUnmonitorCourseActionListener(serverUI, cur_course));
		menuCourse.add(unmonitorCourse);
		
		menuCourse.addSeparator();

		JMenuItem addClusterer = CommonUtil.makeMenuItem(null, "Clustering Community", createClusteringActionListener());
		menuCourse.add(addClusterer);
		
		JMenuItem removeClusterer = CommonUtil.makeMenuItem(null, "Unclustering Community", createUnclusteringActionListener());
		menuCourse.add(removeClusterer);

		JMenuItem removeAllClusterers = CommonUtil.makeMenuItem(null, "Unclustering All Communities", createUnclusteringAllActionListener());
		menuCourse.add(removeAllClusterers);

		JMenu menuTraining = new JMenu("Training");
		menuTraining.setMnemonic(KeyEvent.VK_T);
		
		JMenu knowledge = new JMenu("Knowledge");
		if(ZebraStatic.NETWORKER_RUN_STATIC_KNOWLEDGE_DAEMON) {
			JMenuItem staticUM = CommonUtil.makeMenuItem(null, "Static Overlay Bayesian Model", createTrainingStaticKnowledge());
			knowledge.add(staticUM);
		}
		if(ZebraStatic.NETWORKER_RUN_DYN_KNOWLEDGE_DAEMON) {
			JMenuItem dynUM = CommonUtil.makeMenuItem(null, "Dynamic Overlay Bayesian Model", createTrainingDynKnowledge());
			knowledge.add(dynUM);
		}
		menuTraining.add(knowledge);

		JMenuItem ls = CommonUtil.makeMenuItem(null, "Learning Style", createTrainingLearningStyle());
		menuTraining.add(ls);

		JMenu his = new JMenu("Learning History");
		JMenuItem hisData = CommonUtil.makeMenuItem(null, "History Data", createTrainingLearningHistoryData());
		his.add(hisData);
		
		JMenuItem recommender = CommonUtil.makeMenuItem(null, "Course Recommender", createTrainingRecommender());
		his.add(recommender);

		JMenuItem sequences = CommonUtil.makeMenuItem(null, "Learning Path", createTrainingLearningPath());
		his.add(sequences);
		menuTraining.add(his);

		JMenuItem all = CommonUtil.makeMenuItem(null, "All", createTrainingAll());
		menuTraining.add(all);

		JMenu menuView = new JMenu("View");
		menuView.setMnemonic(KeyEvent.VK_V);
		JCheckBoxMenuItem showClusterer = new JCheckBoxMenuItem("Show/hide Communities");
		showClusterer.setSelected(false);
		serverUI.clustererUI.menuItem = showClusterer;
		showClusterer.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						serverUI.clustererUI.setVisible(!serverUI.clustererUI.isVisible());
					}
					catch(Exception ex) {ex.printStackTrace();}
				}
			});
		menuView.add(showClusterer);
		menuView.addSeparator();
		//
		JMenuItem showDocClassifier = new JMenuItem("Document Classifier");
		showDocClassifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new TriUMServerDocClassifierUI(serverUI);
			}
		});
		menuView.add(showDocClassifier);
		menuView.addSeparator();
		//
		JMenuItem showTotalReport = new JMenuItem("Report Manager");
		showTotalReport.addActionListener(createReportManagerActionListener(serverUI));
		menuView.add(showTotalReport);
		//
		JMenuItem showFeedbackReport = new JMenuItem("Feedback Report");
		showFeedbackReport.addActionListener(createFeedbackReport(cur_course));
		menuView.add(showFeedbackReport);
		menuView.addSeparator();
		//
		JMenuItem mlManager = CommonUtil.makeMenuItem(null, "Mailing List Manager", createMLManagerActionListener(serverUI, cur_course));
		menuView.add(mlManager);
		menuView.addSeparator();
		//
		JMenuItem showCI = new JMenuItem("Client Query (CI)");
		showCI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TriUMQueryFrame frame = new TriUMQueryFrame(serverUI);
				frame.setVisible(true);
			}
		});
		menuView.add(showCI);
		
		
		JMenu menuTool = new JMenu("Tool");
		menuTool.setMnemonic(KeyEvent.VK_T);
		
		JMenuItem gc = new JMenuItem("Garbage Collection");
		gc.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						serverUI.server.getGarbageService().task();
						CommonUtil.gc();
					}
					catch(Exception ex) {ex.printStackTrace();}
				}
			});
		menuTool.add(gc);
		//
		menuTool.addSeparator();
		JMenuItem refresh = new JMenuItem("Refresh");
		refresh.addActionListener(createRefresh());
		menuTool.add(refresh);

		
		JMenu menuHelp = new JMenu("Help");
		menuHelp.setMnemonic(KeyEvent.VK_H);
		JMenuItem helpContent = new JMenuItem("Help Content");
		helpContent.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new TriUMServerHelp();
				}
			});
		menuHelp.add(helpContent);
		menuHelp.addSeparator();
		//
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						new TriUMServerUIAbout(null);
					}
					catch(Exception ex) {System.out.println("TriUMServerUI.createMenuBar causes error " + ex.getMessage());ex.printStackTrace();}
				}
			});
		menuHelp.add(about);
		
		JMenuBar mainMenuBar = new JMenuBar();
		mainMenuBar.add(menuUser);
		mainMenuBar.add(menuCourse);
		mainMenuBar.add(menuTraining);
		mainMenuBar.add(menuView);
		mainMenuBar.add(menuTool);
		mainMenuBar.add(menuHelp);
		
		return mainMenuBar;
	}
	
	private JToolBar createToolBar() {
		final TriUMServerUI final_serverUI = serverUI;
		String cur_userid = null;
		String cur_course = null;
		TriUM um = serverUI.monitoredUsers.getCurUM();
		if(um != null) {
			cur_userid = um.getUserId();
			cur_course = um.getCourse();
		}
        
		JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(true);
        URL iconURL = null;
        
        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "registeruser-24x24.gif");
        JButton addUser = CommonUtil.makeToolButton(iconURL, "Register User", "Register (Add) User", "Register (Add) User", 
    			createRegisterUserActionListener(final_serverUI));
        toolbar.add(addUser);

        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "unregisteruser-24x24.gif");
        JButton removeUser = CommonUtil.makeToolButton(iconURL, "Unregister User", "Unregister (Remove) User", "Unregister (Remove) User", 
    			createUnregisterUserActionListener(final_serverUI, cur_userid));
        toolbar.add(removeUser);
        
        toolbar.addSeparator();

        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "monitoruser-24x24.gif");
        JButton monitorUser = CommonUtil.makeToolButton(iconURL, "Monitor User", "Monitor User", "Monitor User", 
    			createMonitorUserActionListener(final_serverUI, cur_userid, cur_course));
        toolbar.add(monitorUser);
        
        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "unmonitoruser-24x24.gif");
        JButton unmonitorUser = CommonUtil.makeToolButton(iconURL, "Unmonitor User", "Unmonitor User", "Unmonitor User", 
    			createUnmonitorUserActionListener(final_serverUI, cur_userid, cur_course));
        toolbar.add(unmonitorUser);

        toolbar.addSeparator();

        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "clustering-24x24.gif");
        JButton addClusterer = CommonUtil.makeToolButton(iconURL, "Clustering Course", "Clustering Course", "Clustering Course", 
    			createClusteringActionListener());
        toolbar.add(addClusterer);

        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "unclustering-24x24.gif");
        JButton removeClusterer = CommonUtil.makeToolButton(iconURL, "Unclustering Course", "Unclustering Course", "Unclustering Course", 
    			createUnclusteringActionListener());
        toolbar.add(removeClusterer);

        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "unclusteringall-24x24.gif");
        JButton removeAllClusterer = CommonUtil.makeToolButton(iconURL, "Unclustering All Courses", "Unclustering All Courses", "Unclustering All Courses", 
    			createUnclusteringAllActionListener());
        toolbar.add(removeAllClusterer);

        toolbar.addSeparator();

        if(ZebraStatic.NETWORKER_RUN_STATIC_KNOWLEDGE_DAEMON) {
	        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "trainingknowledge-24x24.gif");
	        JButton staticUM = CommonUtil.makeToolButton(iconURL, "Training Static Knowledge", "Training Static Knowledge", "Training Static Knowledge", 
	    			createTrainingStaticKnowledge());
	        toolbar.add(staticUM);
        }
        if(ZebraStatic.NETWORKER_RUN_DYN_KNOWLEDGE_DAEMON) {
	        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "trainingdynknowledge-24x24.gif");
	        JButton dynUM = CommonUtil.makeToolButton(iconURL, "Training Dynamic Knowledge", "Training Dynamic Knowledge", "Training Dynamic Knowledge", 
	    			createTrainingDynKnowledge());
	        toolbar.add(dynUM);
        }
        
        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "traininglearningstyle-24x24.gif");
        JButton ls = CommonUtil.makeToolButton(iconURL, "Training Learning Style", "Training Learning Style", "Training Learning Style", 
    			createTrainingLearningStyle());
        toolbar.add(ls);

        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "traininglearninghistory-24x24.gif");
        JButton hisData = CommonUtil.makeToolButton(iconURL, "Training History Data", "Training History Data", "Training History Data", 
    			createTrainingLearningHistoryData());
        toolbar.add(hisData);

        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "courserecommender-24x24.gif");
        JButton recommender = CommonUtil.makeToolButton(iconURL, "Training Course Recommender", "Training Course Recommender", "Training Course Recommender", 
    			createTrainingRecommender());
        toolbar.add(recommender);

        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "traininglearningpath-24x24.gif");
        JButton sequences = CommonUtil.makeToolButton(iconURL, "Training Learning Path", "Training Learning Path", "Training Learning Path", 
    			createTrainingLearningPath());
        toolbar.add(sequences);

        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "trainingall-24x24.gif");
        JButton all = CommonUtil.makeToolButton(iconURL, "Training All", "Training All", "Training All", 
    			createTrainingAll());
        toolbar.add(all);
        
        toolbar.addSeparator();

        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "classifier-24x24.gif");
        JButton classifier = CommonUtil.makeToolButton(iconURL, "Document Classifier", "Document Classifier", "Document Classifier", 
        	new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new TriUMServerDocClassifierUI(serverUI);
				}
			});
        toolbar.add(classifier);

        toolbar.addSeparator();

        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "reportmanager-24x24.gif");
        JButton totalreport = CommonUtil.makeToolButton(iconURL, "Report Manager", "Report Manager", "Report Manager", 
    			createReportManagerActionListener(final_serverUI));
        toolbar.add(totalreport);
        //
        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "feedbackreport-24x24.gif");
        JButton feedbackreport = CommonUtil.makeToolButton(iconURL, "Feedback Report", "Feedback Report", "Feedback Report", 
    			createFeedbackReport(cur_course));
        toolbar.add(feedbackreport);

        toolbar.addSeparator();
        
        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "mailinglist-24x24.gif");
        JButton ml = CommonUtil.makeToolButton(iconURL, "Mailing List Manager", "Mailing List Manager", "Mailing List Manager", createMLManagerActionListener(final_serverUI, cur_course));
        toolbar.add(ml);

        toolbar.addSeparator();

        iconURL = getClass().getResource(ZebraStatic.IMAGE_DIR + "refresh-24x24.gif");
        JButton refresh = CommonUtil.makeToolButton(iconURL, "Refresh", "Refresh", "Refresh", createRefresh());
        toolbar.add(refresh);

        toolbar.setFloatable(true);
		return toolbar;
	}


	private static class TriUMServerUIThread extends Thread {
		TriUMServer server = null;
		
		public TriUMServerUIThread(TriUMServer server) {
			this.server = server;
		}
		
		@Override
		public void run() {
			try {
				new TriUMServerUI(server);
			}
			catch(Exception e) {
				System.out.println("TriUMServerUI.TriUMServerUIThread.run causes error: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
}

class ChangeUserNameDlg extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public ChangeUserNameDlg(final TriUMServerUI serverUI, final String userid) {
		super(serverUI, "Change User Name", true);
		final Profile profile = UserUtil.getProfile(userid);
		if(profile == null) {
			JOptionPane.showMessageDialog(this, "User not exists!", "Error", JOptionPane.ERROR_MESSAGE);
			dispose(); return;
		}
		String username = null;
		try {
			username = profile.getAttributeValue("personal", "name");
		}
		catch(Exception e) {e.printStackTrace(); return;}
		if(username == null || username.trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "Error when access profile database", "Error", JOptionPane.ERROR_MESSAGE);
			dispose(); return;
		}
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		JPanel mainPane = new JPanel(); mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
		
		JPanel tempPane = null;

		final JTextField oldName = new JTextField(username); oldName.setEditable(false);
		tempPane.add(new JLabel("Old User Name:"));
		tempPane.add(oldName);
		mainPane.add(tempPane);
		
		tempPane = new JPanel(); tempPane.setAlignmentX(LEFT_ALIGNMENT);
		final JTextField newName = new JTextField(8);
		tempPane.add(new JLabel("New User Name:"));
		tempPane.add(newName);
		mainPane.add(tempPane);
		
		JButton ok = new JButton(new AbstractAction("OK") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				String sOldName = oldName.getText().trim();
				String sNewName = newName.getText().trim();
				if(sNewName.length() == 0) {
					JOptionPane.showMessageDialog(serverUI, "New name can't be empty", "Error!", JOptionPane.ERROR_MESSAGE);
					dispose(); return;
				}
				if(sOldName.equals(sNewName)) {
					JOptionPane.showMessageDialog(serverUI, "Old name and new name are the same", "Notice", JOptionPane.INFORMATION_MESSAGE);
					dispose(); return;
				}
				try {
					profile.setAttributeValue("personal", "name", sNewName);
					ProfileDB pdb = ZebraStatic.getProfileDB();
					pdb.setProfile(profile.id, profile);
					
					serverUI.userPane.rearrangeUser(userid);
					serverUI.monitoredUsers.refreshShowedInfo(userid);
					JOptionPane.showMessageDialog(serverUI, "Change user name successfully", "Notice", JOptionPane.INFORMATION_MESSAGE);
					dispose(); return;
				}
				catch(Exception ex) {
					ex.printStackTrace();
					dispose(); return;
				}
			}
		});
		JButton cancel = new JButton(new AbstractAction("Cancel") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				dispose(); return;
			}
		});
		tempPane = new JPanel(); tempPane.setAlignmentX(LEFT_ALIGNMENT);
		tempPane.add(ok);
		tempPane.add(cancel);
		mainPane.add(tempPane);
		
		pack();
		setVisible(true);
	}
}
