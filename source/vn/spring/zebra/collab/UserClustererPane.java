/**
 * 
 */
package vn.spring.zebra.collab;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import vn.spring.zebra.mining.DistanceProfile;
import vn.spring.zebra.mining.ProfileCluster;
import vn.spring.zebra.mining.UserClusterer;
import vn.spring.zebra.mining.UserClustererChangeEvent;
import vn.spring.zebra.mining.UserClustererChangeListener;
import vn.spring.zebra.um.gui.UIDispose;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class UserClustererPane extends JPanel implements UserClustererChangeListener, CollabMsgListener, UIDispose {
	private static final long serialVersionUID = 1L;

	protected UserClusterer userClusterer = null;
	protected UserClustererGraph userClusterGraph = null;
	protected JTextArea arffText = null; 
	protected JTextArea community = null; 
	protected JTextArea collab = null; 

	public UserClustererPane(UserClusterer userClusterer) {
		JPanel clusterPane = new JPanel();
		clusterPane.setLayout(new BorderLayout());
		TitledBorder title = null;
		
		title = BorderFactory.createTitledBorder("User Communites in Graph");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		userClusterGraph = new UserClustererGraph();
		JPanel graphPane = new JPanel();
		graphPane.setBorder(title);
		graphPane.setLayout(new BorderLayout());
		graphPane.add(userClusterGraph, BorderLayout.CENTER);
		clusterPane.add(graphPane, BorderLayout.CENTER);

		title = BorderFactory.createTitledBorder("Mined data");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		arffText = new JTextArea();
		arffText.setAutoscrolls(true);
		arffText.setEditable(false);
		arffText.setBorder(title);
		//clusterPane.add(arffText, BorderLayout.SOUTH);
		
		title = BorderFactory.createTitledBorder("User Communites in Text");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		community = new JTextArea(5, 20);
		community.setAutoscrolls(true);
		community.setBorder(title);
		community.setEditable(false);
		community.setLineWrap(true);

		JSplitPane communityPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		communityPane.setDividerLocation(250);
		communityPane.add(new JScrollPane(clusterPane), JSplitPane.TOP);
		communityPane.add(new JScrollPane(community), JSplitPane.BOTTOM);
		
		title = BorderFactory.createTitledBorder("Collabarative Area");
		title.setBorder(BorderFactory.createLoweredBevelBorder());
		collab = new JTextArea(5, 20);
		collab.setAutoscrolls(true);
		collab.setBorder(title);
		collab.setEditable(false);
		
		JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainPane.setDividerLocation(150);
		mainPane.add(new JScrollPane(collab), JSplitPane.LEFT);
		mainPane.add(communityPane, JSplitPane.RIGHT);
		
		setLayout(new BorderLayout());
		add(mainPane, BorderLayout.CENTER);
		
		update(userClusterer);
	}
	
	public void dispose() {
		try {userClusterer.removeChangeListener(this);}
		catch(Exception e) {System.out.println("UserClusterPane.dispose call UserClusterer.removeChangeListener causes error: " + e.getMessage()); e.printStackTrace();}
	}
	public void update(UserClusterer userClusterer) {
		this.userClusterer = userClusterer;
		this.userClusterGraph.userClusterer = userClusterer;
		this.userClusterer.addChangeListener(this);
		update(userClusterer.getClusters(), userClusterer.getArff());
	}
	private void update(ArrayList<ProfileCluster> clusters, String arff) {
		userClusterGraph.update(clusters);
		arffText.setText(arff);
		
		String msg = "";
		for(int i = 0; i < clusters.size(); i++) {
			ProfileCluster cluster = clusters.get(i);
			String line = "COMMUNITY " + i + ":\n";
			ArrayList<DistanceProfile> profiles = cluster.getProfiles();
			for(int j = 0; j < profiles.size(); j++) {
				DistanceProfile profile = profiles.get(j);
				line += profile.userid;
				if(j < profiles.size() - 1) line += "\n";
			}
			if(i < clusters.size() - 1) line += "\n\n\n";
			msg += line;
		}
		community.setText(msg);
	}

	public void clustererChanged(UserClustererChangeEvent evt) {
		update(evt.getClusterers(), evt.getArff());
	}
	
	public void msgSended(CollabMsgEvent evt) {
		if(collab.getDocument().getLength() > 10000) collab.setText("");
		collab.append(evt.getMsg() + "\n");
	}

	public UserClusterer getClusterer() {return userClusterer;}
	public String getCourse() {return userClusterer.getCourse();}
}
