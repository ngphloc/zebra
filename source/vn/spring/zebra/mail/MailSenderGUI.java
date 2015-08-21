/**
 * 
 */
package vn.spring.zebra.mail;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.mail.Address;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.server.TriUMServer;
import vn.spring.zebra.util.UserChooser;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class MailSenderGUI extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int  TEXT_COLUMNS = 10;
	
	protected Window parent = null;
	protected String mlCourse = null;
	protected CourseMailingList ml = null;
	
	protected JTextField smtpHost = new JTextField(TEXT_COLUMNS);
	protected JTextArea  to = new JTextArea(2, TEXT_COLUMNS);
	protected JTextArea  cc = new JTextArea(2, TEXT_COLUMNS);
	protected JTextArea  bcc = new JTextArea(2, TEXT_COLUMNS);
	protected JTextField subject = new JTextField(TEXT_COLUMNS);
	protected JTextArea  content = new JTextArea(8, TEXT_COLUMNS);
	protected JTextField attach = new JTextField(TEXT_COLUMNS);
	
	public MailSenderGUI(Window _parent, String mlCourse, ArrayList<String> mlSentUserIds) {
		super();
		this.parent = _parent;
		this.mlCourse = mlCourse;
		if(this.mlCourse != null) {
			try {
				CourseMailingListDaemon daemon = TriUMServer.getInstance().getMailingListService().
					getCourseMailingListDaemon(this.mlCourse);
				this.ml = daemon.getCourseMailingList();
			}
			catch(Throwable e) {
				this.mlCourse = null;
				this.ml = null;
			}
		}
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		smtpHost.setText(ZebraStatic.MAIL_SMTP_HOST);
		JPanel smtpHostPane = createRowPanel(new JLabel("SMTP host: "), smtpHost);
		smtpHostPane.setVisible(false);
		add(smtpHostPane);
		//
		to.setWrapStyleWord(true);
		JPanel toPane = createRowPanel(new JLabel("To: "), new JScrollPane(to));
		if(this.mlCourse != null) {
			if(mlSentUserIds == null || mlSentUserIds.size() == 0)
				to.setText(
					MailUtil.netAddrListToStr(MailUtil.getNetAddrList(this.ml.getUserIdList()))
					);
			else {
				ArrayList<String> sentUserIds = new ArrayList<String>();
				ArrayList<String> userids = this.ml.getUserIdList();
				for(String userid : mlSentUserIds) {
					if(userids.contains(userid)) sentUserIds.add(userid);
				}
				to.setText(
					MailUtil.netAddrListToStr(MailUtil.getNetAddrList(sentUserIds))
					);
			}
			to.setEditable(false);
		}
		add(toPane);
		//
		JButton more = new JButton(new AbstractAction("More") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				more();
			}
		});
		cc.setWrapStyleWord(true);
		JPanel ccAddPane = new JPanel(); ccAddPane.setLayout(new BorderLayout());
		ccAddPane.add(new JScrollPane(cc), BorderLayout.CENTER);
		ccAddPane.add(more, BorderLayout.EAST);
		JPanel ccPane = createRowPanel(new JLabel("Cc: "), ccAddPane);
		if(this.mlCourse != null) ccPane.setVisible(false);
		add(ccPane);
		//
		bcc.setWrapStyleWord(true);
		JPanel bccPane = createRowPanel(new JLabel("Bcc: "), new JScrollPane(bcc));
		if(this.mlCourse != null) bccPane.setVisible(false);
		add(bccPane);
		//
		subject.setCaretPosition(0);
		JPanel subjectPane = createRowPanel(new JLabel("Subject: "), subject);
		add(subjectPane);
		//
		content.setWrapStyleWord(true);
		JPanel contentPane = createRowPanel(new JLabel("Content: "), new JScrollPane(content));
		add(contentPane);
		//
		JButton browse = new JButton(new AbstractAction("Browse") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
	            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	            	attach.setText(fc.getSelectedFile().getAbsolutePath());
	            }
			}
		});
		JPanel filePane = new JPanel(); filePane.setLayout(new BorderLayout());
		filePane.add(attach, BorderLayout.CENTER);
		filePane.add(browse, BorderLayout.EAST);
		JPanel attachPane = createRowPanel(new JLabel("File attached: "), filePane);
		add(attachPane);
		//
		JButton send = new JButton(new AbstractAction("Send") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				sendMail();
			}
		});
		JButton cancel = new JButton(new AbstractAction("Cancel") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				parent.setVisible(false);
				parent.dispose();
			}
		});
		JPanel two = new JPanel();
		two.add(send);
		two.add(cancel);
		JPanel toolbar = createRowPanel(two, new JPanel());
		add(toolbar);
	}
	public MailSenderGUI(Window parent, ArrayList<String> userids) {
		this(parent, (String)null, new ArrayList<String>());
		if(userids.size() > 0) {
			to.setText(
				MailUtil.netAddrListToStr(MailUtil.getNetAddrList(userids))
				);
			to.setCaretPosition(0);
			to.setEditable(false);
		}
	}
	private void sendMail() {
		ArrayList<Address> a_to = MailUtil.strToNetAddrList(to.getText());
		ArrayList<Address> a_cc = MailUtil.strToNetAddrList(cc.getText());
		ArrayList<Address> a_bcc = MailUtil.strToNetAddrList(bcc.getText());
		String subject = this.subject.getText().trim();
		String content = this.content.getText();
		
		if(a_to.size() == 0) {
			JOptionPane.showMessageDialog(this, "To address is empty");
			this.to.setCaretPosition(0);
			return;
		}
		if(subject.length() == 0) {
			JOptionPane.showMessageDialog(this, "Subject is empty");
			this.subject.setCaretPosition(0);
			return;
		}
		if(content.length() == 0) {
			JOptionPane.showMessageDialog(this, "Content is empty");
			this.content.setCaretPosition(0);
			return;
		}

		File attach = null;
		if(this.attach.getText().length() > 0) {
			attach = new File(this.attach.getText());
			if(!attach.exists()) attach = null;
		}
		
		try {
			if(mlCourse != null)
				ml.sendMail(new ArrayList<String>(), subject, content, attach);
			else {
				MailService service = new MailService(smtpHost.getText(), null, null, false);
				ArrayList<File> attaches = new ArrayList<File>();
				if(attach != null) attaches.add(attach);
				service.sendMail(MailUtil.newMailAddress(ZebraStatic.MAIL_ADMIN_ADDRESS), 
						a_to, a_cc, a_bcc, subject, content, attaches, false);
			}
			JOptionPane.showMessageDialog(null, "Message sent"); 
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Can't send mail! The error is " + e.getMessage(), 
					"Sending Mail Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	private void more() {
		UserChooser uc = new UserChooser(null, new ArrayList<String>());
		ArrayList<String> chosenUserList = new ArrayList<String>(uc.getChosenUserIds());
		ArrayList<Address> choosenNetAddrList = MailUtil.getNetAddrList(chosenUserList);
		ArrayList<Address> existNetAddrList = MailUtil.strToNetAddrList(cc.getText());
		ArrayList<Address> addNetAddrList = new ArrayList<Address>();
		
		for(Address addr : choosenNetAddrList) {
			if(!MailUtil.contains(existNetAddrList, addr)) {
				addNetAddrList.add(addr);
			}
		}
		if(addNetAddrList.size() == 0) return;
		
		String extraStr = MailUtil.netAddrListToStr(addNetAddrList);
		
		String text = cc.getText().trim();
		if(text.length() == 0)
			cc.setText(extraStr);
		else
			cc.setText(text + MailUtil.MAIL_LIST_SEP + " " + extraStr);
		uc.setVisible(false);
		uc.dispose();
	}
	private JPanel createRowPanel(JComponent left, JComponent right) {
		JPanel row = new JPanel();
		row.setLayout(new BorderLayout());
		row.add(left, BorderLayout.WEST); 
		row.add(right, BorderLayout.CENTER);
		row.setAlignmentX(LEFT_ALIGNMENT);
		return row;
	}
}
