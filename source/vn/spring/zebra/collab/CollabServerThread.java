package vn.spring.zebra.collab;

import java.io.*;
import java.net.*;

import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
class CollabServerThread extends Thread
{
	private CollabServer server = null;
	private Socket socket = null;

	public CollabServerThread(CollabServer server, Socket socket) {
		this.server = server;
		this.socket = socket;

		start();
	}

	public void run() {
		try {

			DataInputStream din = new DataInputStream(socket.getInputStream());
			while (true) {
				String message = din.readUTF();
				ZebraStatic.traceService.trace("CollabServerThread of CollabServer sending \"" + message + "\" all collab client");
				server.sendToAll(message);
			}
		}
		catch(EOFException e) {
		}
		catch(IOException e) {
			ZebraStatic.traceService.trace("The run method of CollabServerThread of CollabServer causes error: " + e.getMessage());
		}
		finally {
			server.removeConnection(socket);
		}
	}
}
