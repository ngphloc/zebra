package vn.spring.zebra.collab;

import java.io.*;
import java.net.*;
import java.util.*;

import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class CollabServer
{
	private ServerSocket serverSocket = null;
	private int port = 0;
	
	private Hashtable<Socket, DataOutputStream> outputStreams = new Hashtable<Socket, DataOutputStream>();
    protected HashSet<CollabMsgListener> listeners = new HashSet<CollabMsgListener>();

	public CollabServer(int port) {
		this.port = port;
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		onDestroyed();
		super.finalize();
	}

	public void onDestroyed() {
		if(serverSocket == null) return;
		
		try {
			serverSocket.close();
			outputStreams.clear();
			listeners.clear();
			serverSocket = null;
		}
		catch(Exception e) {
			System.out.println("CollabServer.onDestroyed causes error: " + e.getMessage());
			serverSocket = null;
		}
	}
	public void start()  throws IOException {
		listen(port);
		ZebraStatic.traceService.trace("CollabServer (of Collaborative service) started!");
	}
	public void addMsgListener(CollabMsgListener listener) {listeners.add(listener);}
	public void removeMsgListener(CollabMsgListener listener) {listeners.remove(listener);}

	public void fireMsgListeners(String plainMsg) {
		String msg = new Date() + "@" + plainMsg;
		for(CollabMsgListener listener : listeners) {
			final CollabMsgListener final_listener = listener;
			final String final_plainMsg = plainMsg;
			final String final_msg = msg;
			new Thread() {
				public void run() {
					final_listener.msgSended(new CollabMsgEvent(this, final_plainMsg, final_msg));
				}
			}.start();
		}
		ZebraStatic.traceService.trace(msg);
	}
	private void listen(int port) throws IOException {
		serverSocket = new ServerSocket(port);

		ZebraStatic.traceService.trace("CollabServer (of Collaborative service) listening on " + serverSocket);

		while (true) {
			Socket socket = serverSocket.accept();

			ZebraStatic.traceService.trace("Connection to CollabServer (of Collaborative service) from " + socket);

			DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

			outputStreams.put(socket, dout);

			new CollabServerThread(this, socket);
		}
	}

	private Enumeration getOutputStreams() {
		return outputStreams.elements();
	}

	public void sendToAll(String message) {
		synchronized(outputStreams) {
			fireMsgListeners(message);
			// For each client ...
			for (Enumeration e = getOutputStreams(); e.hasMoreElements(); ) {
				DataOutputStream dout = (DataOutputStream)e.nextElement();
				try {
					dout.writeUTF(message);
				} catch(IOException ie) {ZebraStatic.traceService.trace(ie.getMessage());}
			}
		}
	}

	public void removeConnection(Socket socket) {
		synchronized(outputStreams) {
			ZebraStatic.traceService.trace("Removing CollabServer (of Collaborative service) connection to " + socket);

			outputStreams.remove(socket);

			try {
				socket.close();
			}
			catch(IOException e) {
				ZebraStatic.traceService.trace( "Error closing Collab server (of Collaborative service) socket" + socket);
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) throws Exception {
		int port = Integer.parseInt( args[0] );
		new CollabServer(port);
	}
}

