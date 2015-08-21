/**
 * 
 */
package vn.spring.zebra.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import vn.spring.zebra.ZebraStatic;
import vn.spring.zebra.client.TriUMQueryResult;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServerSocketService extends Thread {
	protected TriUMServer server = null;
	protected ServerSocket serverSocket = null; 
	protected int port = 0;
	
	public TriUMServerSocketService(TriUMServer server, int port) {
		super();
		this.server = server;
		this.port = port;
		start();
	}
	protected void onDestroyed() {
		if(serverSocket == null) return;
		try {
			serverSocket.close();
		}
		catch(Exception e) {
			System.out.println("TriUMServerSocketService.onDestroyed causes error: " + e.getMessage());
			serverSocket = null;
		}
	}
	public TriUMServer getServer() {return server;}
	
	public void run() {
		try {
			listen(port);
		}
		catch(IOException e) {
			ZebraStatic.traceService.trace("SocketService (of Communicate Service) run causes error: " + e.getMessage());
		}
	}

	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		super.start();
		ZebraStatic.traceService.trace("SocketService (of Communicate Service) started!");
	}
	
	private void listen(int port) throws IOException {
		// Create the ServerSocket
		serverSocket = new ServerSocket( port );

		// Tell the world we're ready to go
		ZebraStatic.traceService.trace("SocketService (of Communicate Service) listening on " + serverSocket);

		// Keep accepting connections forever
		while (true) {
			// Grab the next incoming connection
			Socket socket = serverSocket.accept();

			// Tell the world we've got it
			ZebraStatic.traceService.trace("Connection to SocketService (of Communicate Service) from " + socket);

			// Create a new thread for this connection, and then forget
			// about it
			new TriUMServerListenerDedicateThread(this, socket);
		}
	}
}

class TriUMServerListenerDedicateThread extends Thread {
	protected TriUMServerSocketService service = null;
	protected Socket socket = null;
	
	public TriUMServerListenerDedicateThread(TriUMServerSocketService service, Socket socket) {
		this.service = service;
		this.socket = socket;
		start();
	}

	@Override
	public void run() {
		try {
			DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
			DataInputStream din = new DataInputStream(socket.getInputStream());
			String queryString = din.readUTF();
			TriUMQueryResult result = service.server.getCommunicateService().
				getQueryDelegator().query(queryString);
			dout.writeUTF(result.toString());
		}
		catch(Exception e) {
			ZebraStatic.traceService.trace("TriUMServerListenerDedicateThread.run (in SocketService of Communicate Service) causes error: " + e.getMessage());
		}
		finally {
			try {
				socket.close();
			}
			catch(IOException e) {
				ZebraStatic.traceService.trace( "Error closing TriUMServerListenerDedicateThread (in SocketService of Communicate Service)" + socket);
				e.printStackTrace();
			}
		}
	}
	
}
