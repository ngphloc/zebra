package vn.spring.zebra.server;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.JOptionPane;

import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class TriUMServerSysTray extends TrayIcon {
	private TriUMServer server = null;
	private TriUMServerUI controlPanel = null;
	private SystemTray tray;
	
	public static TriUMServerSysTray getSeverSysTray(TriUMServer server) {
        if (SystemTray.isSupported()) {
        	try {
	            Image image = Toolkit.getDefaultToolkit().getImage(
	            		server.getClass().getResource(ZebraStatic.IMAGE_DIR + ZebraStatic.IMAGE_LARGE_ICON_FILENAME));
	        	return new TriUMServerSysTray(server, image);
        	}
        	catch(Exception e) {
        		System.out.println("Creating system tray causes error: " + e.getMessage());
        		e.printStackTrace();
        	}
    	}
        else {
        	System.out.println("System tray is currently not supported.");
    	}        
		return null;
	}
	protected void onDestroyed() {
		if(tray == null) return;
		tray.remove(this);
		disposeControlPanel();
		tray = null;
	}
	private TriUMServerSysTray(final TriUMServer server, Image icon) {
		super(icon, "Zebra: Triangular User Modeling Server");
		this.server = server;
		this.tray = SystemTray.getSystemTray();
		
	    setPopupMenu(createPopupMenu());
	    setImageAutoSize(true);
	    addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        //this.displayMessage("Zebra", "You have used Zebra user modeling system!", TrayIcon.MessageType.INFO);
		    	new TriUMServerUIAbout(null);
			}
	    });
	    addMouseListener(new MouseAdapter() {});

	    try {
	        tray.add(this);
	    }
	    catch (AWTException e) {
	    	System.err.println("TrayIcon could not be added.");
	    	return;
	    }

	}
	
	private void exitServer() {
		onDestroyed();
		server.onDestroyed();
		System.exit(0);
	}
	private PopupMenu createPopupMenu() {
	    PopupMenu popup = new PopupMenu();
	    
	    MenuItem toggleItem = new MenuItem("Show/hide Zebra control panel");
	    toggleItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		toggleControlPanel();
        	}
    	});
	    popup.add(toggleItem);
	    popup.addSeparator();
	    
	    MenuItem refreshItem = new MenuItem("Refresh Zebra control panel");
	    refreshItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		refreshControlPanel();
        	}
    	});
	    popup.add(refreshItem);
	    popup.addSeparator();

	    MenuItem wsItem = new MenuItem("Re-Deploy Web Services");
	    wsItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new TriUMServerHelper(server).deployWebServices();
        		JOptionPane.showMessageDialog(null, "Re-Deploy Web Services successfully!");
        	}
    	});
	    popup.add(wsItem);
	    popup.addSeparator();

	    MenuItem exitItem = new MenuItem("Exit Zebra");
	    exitItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		exitServer();
        	}
    	});
	    popup.add(exitItem);
	    
	    return popup;
	}
	private void toggleControlPanel() {
		if(controlPanel == null || controlPanel.isDisposed())
			createControlPanel();
		else {
			disposeControlPanel();
		}
	}
	
	private void refreshControlPanel() {
		if(controlPanel != null && !controlPanel.isDisposed()) {
			disposeControlPanel();
		}
		createControlPanel();
	}
	private void createControlPanel() {
		try {
			controlPanel = new TriUMServerUI(server);
			controlPanel.setVisible(true);
		}
		catch(Exception e) {
			System.out.println("Create control panel causes error: " + e.getMessage()); 
			controlPanel = null;
			e.printStackTrace();
		}
	}
	private void disposeControlPanel() {
		if(controlPanel == null) return;
		
		controlPanel.setVisible(false);
		controlPanel.dispose();
		controlPanel = null;
	}
}
