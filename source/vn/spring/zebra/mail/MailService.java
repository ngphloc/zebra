/**
 * 
 */
package vn.spring.zebra.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class MailService {
	public final static String DEFAULT_SENDMAIL_PROTOCOL = "smtp";
	public final static String DEFAULT_READMAIL_PROTOCOL = "pop3"; 
	
	protected String  host = null;
	protected String  username = null;
	protected String  password = null;
	protected boolean auth = false;
	protected Session session = null;
	
	public MailService(String host, String username, String password, boolean auth) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.auth = auth;
		if(this.username == null || this.password == null) {
			this.username = null;
			this.password = null;
			this.auth = false;
		}
		
        Properties properties = System.getProperties();
        properties.setProperty("mail."  + DEFAULT_SENDMAIL_PROTOCOL + ".host", this.host);
        properties.setProperty("mail."  + DEFAULT_READMAIL_PROTOCOL + ".host", this.host);
	    if(this.auth) {
	    	properties.put("mail." + DEFAULT_SENDMAIL_PROTOCOL + ".auth", "true");
	    	properties.put("mail." + DEFAULT_READMAIL_PROTOCOL + ".auth", "true");
	    }
        
        this.session = Session.getDefaultInstance(properties);
	}
	
	public void sendMail(Address from, ArrayList<Address> to, ArrayList<Address> cc, ArrayList<Address> bcc,
			String subject, String content, ArrayList<File> attaches, boolean isHtml) throws Exception {

		Message msg = createMsg(session, from, to, cc, bcc, subject, content, attaches, isHtml);
        sendMail(msg);
	}
	public void sendMail(Address from, ArrayList<Address> to, String subject, String content, ArrayList<File> attaches, boolean isHtml) throws Exception {
		sendMail(from, to, new ArrayList<Address>(), new ArrayList<Address>(), subject, content, attaches, isHtml);
	}
	private void sendMail(Message msg) throws Exception {
        //The below code can be replace simply by code: Transport.send(msg)
        Transport transport = session.getTransport(DEFAULT_SENDMAIL_PROTOCOL);
        if(auth) transport.connect(host, username, password);
        else     transport.connect();
        transport.sendMessage(msg, msg.getAllRecipients());
	}
	private static Message createMsg(Session session,
			Address from, ArrayList<Address> to, ArrayList<Address> cc, ArrayList<Address> bcc,
			String subject, String content, ArrayList<File> attaches, boolean isHtml) throws Exception {
		Message msg = new MimeMessage(session);
		prepareMsg(msg, from, to, cc, bcc, subject, content, attaches, isHtml);
		return msg;
	}
	private static void prepareMsg(Message msg,
			Address from, ArrayList<Address> to, ArrayList<Address> cc, ArrayList<Address> bcc,
			String subject, String content, ArrayList<File> attaches, boolean isHtml) throws Exception {
		
        msg.setFrom(from);
        if(to != null)  for(Address addr : to)   msg.addRecipient(Message.RecipientType.TO, addr);
        if(cc != null)  for(Address addr : cc)   msg.addRecipient(Message.RecipientType.CC, addr);
        if(bcc != null) for(Address addr : bcc)  msg.addRecipient(Message.RecipientType.BCC, addr);
        msg.setSubject(subject);
        
        if(attaches == null || attaches.size() == 0) {
	        if(isHtml)
	        	msg.setContent(content, "text/html");
	        else
	        	msg.setContent(content, "text/plain");
        }
        else {
            Multipart multiPart = new MimeMultipart();
            if(content != null) {
    	        BodyPart contentPart = new MimeBodyPart();
    	        if(isHtml)
    	        	contentPart.setContent(content, "text/html");
    	        else
    	        	contentPart.setContent(content, "text/plain");
    	        multiPart.addBodyPart(contentPart);
            }
            
            for(File attach : attaches) {
            	if(attach != null) continue;
            	MimeBodyPart attachPart = new MimeBodyPart();
    	        attachPart.attachFile(attach);
    	        multiPart.addBodyPart(attachPart);
            }
            msg.setContent(multiPart);
        }
        msg.setSentDate(new Date());
	}
	
	public Folder openFolder(String folderName, int openMode) throws Exception {
		//open mode: Folder.READ_ONLY, Folder.READ_WRITE
        Store store = session.getStore(DEFAULT_READMAIL_PROTOCOL);
        store.connect(host, username, password);

        Folder folder = null;
        if(folderName != null && folderName.length() > 0)
        	folder = store.getFolder(folderName);
        else
        	folder = store.getDefaultFolder();
        if(folder.exists()) {
        	folder.open(openMode);
        }
        return folder;
	}
	public Folder createFolder(String folderName, int createMode) throws Exception {
		//Create mode: Folder.HOLDS_FOLDERS, Folder.HOLDS_MESSAGES
        Folder folder = openFolder(folderName, Folder.READ_ONLY);
        if (folder.exists()) {
        	System.out.println("Folder \"" + folderName + "\" existing");
        }
        else {
        	folder.create(createMode);
        }
    	return folder;
	}
	
	public boolean existFolder(String folderName, int createMode) throws Exception {
        Folder folder = openFolder(folderName, Folder.READ_ONLY);
        boolean exist = folder.exists();
        folder.close(false);
        return exist;
	}
	public boolean copyFolder(String srcFolderName, String dstFolderName) throws Exception {
		Folder srcFolder = openFolder(srcFolderName, Folder.READ_ONLY);
		if(!srcFolder.exists()) return false;
		
		Folder dstFolder = openFolder(srcFolderName, Folder.READ_WRITE);
		if(!dstFolder.exists()) dstFolder.create(Folder.HOLDS_MESSAGES);
		srcFolder.copyMessages(srcFolder.getMessages(), dstFolder);
		
		srcFolder.close(false);
		dstFolder.close(false);
		return true;
	}
	public Folder[] listFolder(Folder parent) throws Exception {
		return parent.list();
	}
	public Message[] readMail(String folderName) throws Exception {
        Folder folder = openFolder(folderName, Folder.READ_ONLY);
        if (!folder.exists()) return new Message[0];
        folder.open(Folder.READ_ONLY);
        folder.close(false);
        Message[] msgs = folder.getMessages();
        return msgs;
	}
	
	public Message[] readMail() throws Exception {
		return readMail(null);
	}
	
	public Message replyMail(Message originMsg, String content, ArrayList<File> attaches, boolean replyToAll, boolean isHtml) throws Exception {
        MimeMessage reply = (MimeMessage) originMsg.reply(replyToAll);
        
        Address from = null;
        Address[] recipients = originMsg.getFrom();
        for(Address recipient : recipients) {
        	if(recipient.toString().indexOf(username + "@") != -1) {
        		from = recipient;
        		break;
        	}
        }
        if(from == null) from = recipients[0];

        StringBuffer textContent = new StringBuffer();
        textContent.append(content);
        ArrayList<String> originTexts = extractText(originMsg);
        if(originTexts.size() > 0) {
        	textContent.append("\r\n-------Origin Mail-------\r\n");
        	for(String originText : originTexts) {
        		textContent.append(originText + "\r\n");
        	}
        }
        
        prepareMsg(reply, from, new ArrayList<Address>(), new ArrayList<Address>(), new ArrayList<Address>(), 
        		"Re: " + originMsg.getSubject(), textContent.toString(), attaches, isHtml);
        return reply;
	}
	public Message forwardMail(Message originMsg, boolean isHtml) throws Exception {
        MimeMessage forward = new MimeMessage(session);
        
        forward.setSubject("Fwd: " + originMsg.getSubject());
        forward.setFrom(originMsg.getFrom()[0]);
    	forward.addRecipients(Message.RecipientType.TO, originMsg.getAllRecipients());

        Multipart multipart = new MimeMultipart();
        
        BodyPart bodypart = new MimeBodyPart();
        bodypart.setText("\r\n-------Origin Mail-------\r\n");
        multipart.addBodyPart(bodypart);

        bodypart = new MimeBodyPart();
        bodypart.setDataHandler(originMsg.getDataHandler());
        multipart.addBodyPart(bodypart);

        forward.setContent(multipart);
        return forward;
	}
	public void deleteMail(Folder folder, int msgIdx) throws Exception {
		folder.getMessage(msgIdx).setFlag(Flags.Flag.DELETED, true);
		folder.expunge();
	}
	
	public static ArrayList<String> extractText(Message msg) {
		ArrayList<String> textContent = new ArrayList<String>();
		try {
			Object content = msg.getContent();
	        if(content instanceof String) {
	        	textContent.add((String)content);
	        }
	        else if(content instanceof Multipart) {
	            Multipart multipart = (Multipart) content;
	
	            for (int i = 0; i < multipart.getCount(); i++) {
	                BodyPart bodyPart = multipart.getBodyPart(i);
	                Object bodyContent = bodyPart.getContent();
	                if(bodyContent instanceof String)
	                	textContent.add((String)bodyContent);
	            }
	        }
		}
		catch(Exception e) {e.printStackTrace();}
		return textContent;
	}
	public static ArrayList<BufferedReader> extractNonText(Message msg) {
		ArrayList<BufferedReader> nonTextContent = new ArrayList<BufferedReader>();
		try {
			Object content = msg.getContent();
	        if(!(content instanceof String)) {
	        	nonTextContent.add(new BufferedReader(new InputStreamReader(msg.getInputStream())));
	        }
	        else if(content instanceof Multipart) {
	            Multipart multipart = (Multipart) content;
	
	            for (int i = 0; i < multipart.getCount(); i++) {
	                BodyPart bodyPart = multipart.getBodyPart(i);
	                Object bodyContent = bodyPart.getContent();
	                if(!(bodyContent instanceof String)) {
	    	        	nonTextContent.add(new BufferedReader(new InputStreamReader(bodyPart.getInputStream())));
	                }
	            }
	        }
		}
		catch(Exception e) {e.printStackTrace();}
		return nonTextContent;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(MailUtil.newMailAddress("sdgshfdh <nploc@localhost>", "Nguyen Phuoc Loc"));
	}
}
