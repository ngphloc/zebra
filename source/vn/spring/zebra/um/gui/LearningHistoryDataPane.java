/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.JXTreeTable;
import org.w3c.dom.Element;

import vn.spring.zebra.mining.LearningHistoryData;
import vn.spring.zebra.mining.LearningHistoryDataEvent;
import vn.spring.zebra.mining.LearningHistoryDataListener;
import vn.spring.zebra.util.xmlviewer.*;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class LearningHistoryDataPane extends JPanel implements UIDispose, LearningHistoryDataListener {
	private static final long serialVersionUID = 1L;
	
	protected LearningHistoryData hisData = null;
	protected String              userid = null;
	
	protected XMLTreeTable userXMLTable = null;
	protected XMLTreeTable courseXMLTable = null;
	
	public LearningHistoryDataPane(String userid, LearningHistoryData hisData) {
    	super();
    	this.userid = userid;
    	this.hisData = hisData;
    	
		TitledBorder title = null;
    	
		title = BorderFactory.createTitledBorder("HISTORY DATA OF " + userid);
		title.setBorder(BorderFactory.createLoweredBevelBorder());
    	userXMLTable = new XMLTreeTable();
    	userXMLTable.setAutoResizeMode(JXTreeTable.AUTO_RESIZE_ALL_COLUMNS);
    	userXMLTable.setAutoscrolls(true);
    	JPanel userPanel = new JPanel();
    	userPanel.setBorder(title);
    	userPanel.setLayout(new BorderLayout());
    	userPanel.add(new JScrollPane(userXMLTable), BorderLayout.CENTER);
    	
    	
		title = BorderFactory.createTitledBorder("ALL USERS' HISTORY DATA");
    	courseXMLTable = new XMLTreeTable();
    	courseXMLTable.setAutoResizeMode(JXTreeTable.AUTO_RESIZE_ALL_COLUMNS);
    	courseXMLTable.setAutoscrolls(true);
    	JPanel coursePanel = new JPanel();
    	coursePanel.setLayout(new BorderLayout());
    	coursePanel.setBorder(title);
    	coursePanel.add(new JScrollPane(courseXMLTable), BorderLayout.CENTER);
    	
		JSplitPane mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainPane.setDividerLocation(250);
		mainPane.add(userPanel, JSplitPane.TOP);
		mainPane.add(coursePanel, JSplitPane.BOTTOM);

		setLayout(new BorderLayout());
    	add(mainPane, BorderLayout.CENTER);
    	
    	update(LearningHistoryData.getUserData(this.userid, this.hisData.getCourseData()), this.hisData.getCourseData());
    	this.hisData.addListener(this);
    }
	
    public void update(Element userData, Element courseData) {
    	
    	XMLTreeTableModel userModel = new XMLTreeTableModel();
    	if(userData != null) userModel = new XMLTreeTableModel(userData);
    	userXMLTable.setTreeTableModel(userModel);
    	

    	XMLTreeTableModel courseModel = new XMLTreeTableModel();
    	if(courseData != null) courseModel = new XMLTreeTableModel(courseData);
    	courseXMLTable.setTreeTableModel(courseModel);
    }
    
    public String getUserId() {return userid;}

	public void dataUpdated(LearningHistoryDataEvent evt) {
		// TODO Auto-generated method stub
		update(evt.getUserData(), evt.getCourseData());
	}
	public void dispose() {
		try {
			hisData.removeListener(this);
		}
		catch(Exception e) {
			System.out.println("LearningHistoryDataPane.dispose causes error: " + e.getMessage());
		}
    }
}

