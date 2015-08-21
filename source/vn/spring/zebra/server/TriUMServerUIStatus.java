/**
 * 
 */
package vn.spring.zebra.server;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import vn.spring.zebra.ZebraStatic;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
class TriUMServerUIStatus extends JPanel {
	private static final long serialVersionUID = 1L;
	
	protected TriUMServerControlPanel serverCP = null;
	
	private String[] networkerInfo = {"Belief Network engine running...",
		"Belief Network engine stopped!"};
	private String[] minerInfo = {"Mining engine running...",
		"Mining engine stopped!"};
	private String[] clustererServiceInfo = {"Community service running...",
		"Community service stopped!"};
	private String[] garbageInfo = {"Garbage Collection service running...",
		"Garbage Collection service stopped!"};
	private String[] collabInfo = {"Collaboration service running...",
		"Collaboration service stopped!"};
	private String[] communicateInfo = {"Communication service running...",
		"Communication service stopped!"};
	private String[] mlInfo = {"Mailing List service running...",
		"Mailing List service stopped!"};
	
	private JLabel gearLable = new JLabel();
	private JLabel networkerLabel = new JLabel();
	private JLabel minerLabel = new JLabel();
	private JLabel clustererServiceLabel = new JLabel();
	private JLabel garbageLabel = new JLabel();
	private JLabel collabLabel = new JLabel();
	private JLabel communicateLabel = new JLabel();
	private JLabel mlLabel = new JLabel();
	
	private JButton networkerButton = new JButton();
	private JButton minerButton = new JButton();
	private JButton clustererServiceButton = new JButton();
	private JButton collabButton = new JButton();
	private JButton garbageButton = new JButton();
	private JButton communicateButton = new JButton();
	private JButton mlButton = new JButton();


	public TriUMServerUIStatus(TriUMServer server) {
		this.serverCP = new TriUMServerControlPanel(server);
		
		JPanel gear = new JPanel();
		gear.setLayout(new BoxLayout(gear, BoxLayout.Y_AXIS));
		gear.add(gearLable);

		JPanel engine = new JPanel();
		engine.setLayout(new BoxLayout(engine, BoxLayout.Y_AXIS));
		//
		JPanel networkerPane = new JPanel();
		networkerPane.setLayout(new BoxLayout(networkerPane, BoxLayout.X_AXIS)); 
		networkerPane.add(networkerLabel); 
		//networkerPane.add(networkerButton);
		engine.add(networkerPane);
		networkerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {buttonAction(networkerButton);}
		});
		//
		JPanel minerPane = new JPanel();
		minerPane.setLayout(new BoxLayout(minerPane, BoxLayout.X_AXIS)); 
		minerPane.add(minerLabel); 
		//minerPane.add(minerButton); 
		engine.add(minerPane);
		minerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {buttonAction(minerButton);}
		});
		
		JPanel service1 = new JPanel();
		service1.setLayout(new BoxLayout(service1, BoxLayout.Y_AXIS));
		//
		JPanel clustererServicePane = new JPanel();
		clustererServicePane.setLayout(new BoxLayout(clustererServicePane, BoxLayout.X_AXIS)); 
		clustererServicePane.add(clustererServiceLabel); 
		//clustererServicePane.add(clustererServiceButton); 
		service1.add(clustererServicePane);
		clustererServiceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {buttonAction(clustererServiceButton);}
		});
		//
		JPanel garbagePane = new JPanel();
		garbagePane.setLayout(new BoxLayout(garbagePane, BoxLayout.X_AXIS)); 
		garbagePane.add(garbageLabel); 
		//garbagePane.add(garbageButton); 
		service1.add(garbagePane);
		garbageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {buttonAction(garbageButton);}
		});

		JPanel service2 = new JPanel();
		service2.setLayout(new BoxLayout(service2, BoxLayout.Y_AXIS));
		//
		JPanel collabPane = new JPanel();
		collabPane.setLayout(new BoxLayout(collabPane, BoxLayout.X_AXIS)); 
		collabPane.add(collabLabel); 
		//collabPane.add(collabButton); 
		service2.add(collabPane);
		collabButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {buttonAction(collabButton);}
		});
		//
		JPanel communicatePane = new JPanel();
		communicatePane.setLayout(new BoxLayout(communicatePane, BoxLayout.X_AXIS)); 
		communicatePane.add(communicateLabel); 
		//communicatePane.add(communicateButton); 
		service2.add(communicatePane);
		communicateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {buttonAction(communicateButton);}
		});
		//
		JPanel mlPane = new JPanel();
		mlPane.setLayout(new BoxLayout(mlPane, BoxLayout.X_AXIS)); 
		mlPane.add(mlLabel); 
		//mlPane.add(mlButton); 
		service2.add(mlPane);
		mlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {buttonAction(mlButton);}
		});
		
		//
		JPanel mainPane = new JPanel();
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.X_AXIS));
		mainPane.add(gear);
		mainPane.add(Box.createRigidArea(new Dimension(10, 0)));
		mainPane.add(engine);
		mainPane.add(Box.createRigidArea(new Dimension(10, 0)));
		mainPane.add(service1);
		mainPane.add(Box.createRigidArea(new Dimension(10, 0)));
		mainPane.add(service2);
		setLayout(new BorderLayout());
		add(new JScrollPane(mainPane), BorderLayout.CENTER);
		
		updateControls();
	}
	protected void updateControls() {
		boolean onestart = false;
		if(serverCP.isZebraNetworkerStarted()) {
			networkerLabel.setText(networkerInfo[0]);
			networkerButton.setText("stop");
			networkerButton.setActionCommand("stop");
			onestart |=true;
		}
		else {
			networkerLabel.setText(networkerInfo[1]);
			networkerButton.setText("start");
			networkerButton.setActionCommand("start");
		}
		
		if(serverCP.isZebraMinerStarted()) {
			minerLabel.setText(minerInfo[0]);
			minerButton.setText("stop");
			minerButton.setActionCommand("stop");
			onestart |=true;
		}
		else {
			minerLabel.setText(minerInfo[1]);
			minerButton.setText("start");
			minerButton.setActionCommand("start");
		}

		if(serverCP.isClustererServiceStarted()) {
			clustererServiceLabel.setText(clustererServiceInfo[0]);
			clustererServiceButton.setText("stop");
			clustererServiceButton.setActionCommand("stop");
			onestart |=true;
		}
		else {
			clustererServiceLabel.setText(clustererServiceInfo[1]);
			clustererServiceButton.setText("start");
			clustererServiceButton.setActionCommand("start");
		}

		if(serverCP.isGarbageServiceStarted()) {
			garbageLabel.setText(garbageInfo[0]);
			garbageButton.setText("stop");
			garbageButton.setActionCommand("stop");
			onestart |=true;
		}
		else {
			garbageLabel.setText(garbageInfo[1]);
			garbageButton.setText("start");
			garbageButton.setActionCommand("start");
		}

		if(serverCP.isCollabServiceStarted()) {
			collabLabel.setText(collabInfo[0]);
			collabButton.setText("stop");
			collabButton.setActionCommand("stop");
			onestart |=true;
		}
		else {
			collabLabel.setText(collabInfo[1]);
			collabButton.setText("start");
			collabButton.setActionCommand("start");
		}
		
		if(serverCP.isCommunicateServiceStarted()) {
			communicateLabel.setText(communicateInfo[0]);
			communicateButton.setText("stop");
			communicateButton.setActionCommand("stop");
			onestart |=true;
		}
		else {
			communicateLabel.setText(communicateInfo[1]);
			communicateButton.setText("start");
			communicateButton.setActionCommand("start");
		}
		
		if(serverCP.isMLServiceStarted()) {
			mlLabel.setText(mlInfo[0]);
			mlButton.setText("stop");
			mlButton.setActionCommand("stop");
			onestart |=true;
		}
		else {
			mlLabel.setText(mlInfo[1]);
			mlButton.setText("start");
			mlButton.setActionCommand("start");
		}

		String servicesImageUrl = "";
		if(onestart) servicesImageUrl = ZebraStatic.IMAGE_DIR + "services-running.gif";
		else         servicesImageUrl = ZebraStatic.IMAGE_DIR + "services-stopped.gif";
		try {
			ImageIcon gearIcon = new ImageIcon(this.getClass().getResource(servicesImageUrl)); 
			gearLable.setIcon(gearIcon);
		}
		catch(Exception e) {
			System.out.println("Load Image icon cause error: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	protected void buttonAction(JButton button) {
		String cmd = button.getActionCommand();
		
		if(button == networkerButton) {
			if(cmd.equals("start")) serverCP.startZebraNetworker();
			else                    serverCP.stopZebraNetworker();
		}
		else if(button == minerButton) {
			if(cmd.equals("start")) serverCP.startZebraMiner();
			else                    serverCP.stopZebraMiner();
		}
		else if(button == clustererServiceButton) {
			if(cmd.equals("start")) serverCP.startClustererService();
			else                    serverCP.stopClustererService();
		}
		else if(button == collabButton) {
			if(cmd.equals("start")) serverCP.startCollabService();
			else                    serverCP.stopCollabService();
		}
		else if(button == communicateButton) {
			if(cmd.equals("start")) serverCP.startCommunicateService();
			else                    serverCP.stopCommunicateService();
		}
		else if(button == garbageButton) {
			if(cmd.equals("start")) serverCP.startGarbageService();
			else                    serverCP.stopGarbageService();
		}
		else if(button == mlButton) {
			if(cmd.equals("start")) serverCP.startMLService();
			else                    serverCP.stopMLService();
		}
		updateControls();
	}
}