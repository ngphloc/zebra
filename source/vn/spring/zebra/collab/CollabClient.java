package vn.spring.zebra.collab;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CollabClient extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1L;
	public static final String USERID_SEP = ":";

	private JTextField tf = null;
	private JTextArea ta = null;

	private Socket socket = null;

	private DataOutputStream dout = null;
	private DataInputStream din = null;

	private String userid = null;
	private ArrayList<String> group = new ArrayList<String>();
	
	public CollabClient(String host, int port, String userid, Collection<String> group) {
		this.userid = userid;
		if(group != null) this.group.addAll(group);
		
		TitledBorder title = BorderFactory.createTitledBorder("Collabarative area");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		setBorder(title);
		setLayout(new BorderLayout());
		tf = new JTextField();
		ta = new JTextArea(5, 10);
		ta.setEditable(false);
		add(tf, BorderLayout.NORTH);
		add(new JScrollPane(ta), BorderLayout.CENTER);

		tf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processMessage( e.getActionCommand() );
			}
		});

		try {
			socket = new Socket(host, port);

			System.out.println("Collabarative client connected to " + socket);

			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());
		}
		catch(IOException e) {
			System.out.println("Collabarative client connection error: " + e.getMessage());
		}
		
		new Thread(this).start();
	}

	private void processMessage(String message) {
		try {
			dout.writeUTF(userid + USERID_SEP + message);
			tf.setText("");
		} catch(IOException e) {System.out.println("CollabClient.processMessage causes error" + e.getMessage()); }
	}

	public void run() {
		try {
			// Receive messages one-by-one, forever
			while (true) {
				String message = din.readUTF();
				int idx = message.indexOf(USERID_SEP);
				if(idx != -1) {
					String fromUserId = message.substring(0, idx);
					String info = message.substring(idx + USERID_SEP.length());
					message = fromUserId + USERID_SEP + " " + info;
					if(!group.contains(fromUserId) && !userid.equals(fromUserId)) continue;
				}
				ta.append(message + "\n");
			}
		}
		catch(IOException e) {
			System.out.println("CollabClient.run causes error" + e.getMessage());
		}
		finally {
			try {din.close();}
			catch(Exception e) {System.out.println("CollabClient.run causes error" + e.getMessage());}
		}
	}
}
