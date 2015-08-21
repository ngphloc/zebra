package vn.spring.zebra.um.gui;

import java.net.URL;

import java.io.*;
import javax.swing.*;

import vn.spring.bayes.InterchangeFormat.IFException;

import vn.spring.zebra.um.OverlayBayesUM;
import vn.spring.zebra.um.OverlayBayesUMFactory;
import vn.spring.zebra.um.OverlayBayesUM.OBUM_BAYESNET_TYPE;
import vn.spring.zebra.exceptions.ZebraException;
import vn.spring.zebra.helperservice.AuthorService;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class OverlayBayesInspector extends JFrame implements UIDispose {
	private static final long serialVersionUID = 1L;

	protected URL base = null;  //Eg: base = http://localhost:8080/wow/
	protected String author = null;
	protected String course = null;
	protected OBUM_BAYESNET_TYPE bayesType = OverlayBayesUM.OBUM_DEFAULT_BAYESNET_TYPE;
	protected OverlayBayesUMGraph umGraph = null;
	
	public OverlayBayesInspector(URL base, OBUM_BAYESNET_TYPE bayesType) throws IOException, IFException, ZebraException, Exception {
		super();
		new OverlayBayesInspectorHelper(this).initialize();
		load(base, null, bayesType, false);
		setVisible(true);
	}
	public OverlayBayesInspector(URL base, String course, OBUM_BAYESNET_TYPE bayesType) throws IOException, IFException, ZebraException, Exception {
		super();
		new OverlayBayesInspectorHelper(this).initialize();
		load(base, course, bayesType, false);
		setVisible(true);
	}
	public OverlayBayesInspector(URL base, String author, String course, OBUM_BAYESNET_TYPE bayesType) throws IOException, IFException, ZebraException, Exception {
		super();
		new OverlayBayesInspectorHelper(this).initialize();
		load(base, author, course, bayesType, false);
		setVisible(true);
	}
	public OverlayBayesInspector(URL base, String author, String pwd, String course, OBUM_BAYESNET_TYPE bayesType) throws IOException, IFException, ZebraException, Exception {
		super();
		new OverlayBayesInspectorHelper(this).initialize();
		load(base, author, pwd, course, bayesType, false);
		setVisible(true);
	}
	private void clear() {
		author = null; course = null;
		bayesType = OBUM_BAYESNET_TYPE.JAVABAYES;
		umGraph.clearGraph();
	}
	protected void load(URL base, String author, String course, OBUM_BAYESNET_TYPE bayesType, boolean isCreate)  throws IOException, IFException, ZebraException, Exception {
		clear();
		base = AuthorService.checkBase(base); //Eg: base = http://localhost:8080/wow/
		
		this.base = base;
		this.author = author; this.course = course;
		this.bayesType = (bayesType == null ? OBUM_BAYESNET_TYPE.JAVABAYES : bayesType); 
		if(course != null) {
			if(isCreate)
				this.umGraph.createFromAuthor(this.base, this.author, this.course, this, this.bayesType);
			else
				this.umGraph.loadFromAuthor(this.base, this.author, this.course, this, this.bayesType);
		}
	}
	protected void load(URL base, String author, String pwd, String course, OBUM_BAYESNET_TYPE bayesType, boolean isCreate)  throws IOException, IFException, ZebraException, Exception {
		base = AuthorService.checkBase(base); //Eg: base = http://localhost:8080/wow/
		AuthorService service = new AuthorService(base, author);
		if(!service.checkPwd(pwd)) {
			JOptionPane.showMessageDialog(this, "Invalid author name or password");
			clear();
			return;
		}
		load(base, author, course, bayesType, isCreate);
	}
	protected void load(URL base, String course, OBUM_BAYESNET_TYPE bayesType, boolean isCreate)  throws IOException, IFException, ZebraException, Exception {
		base = AuthorService.checkBase(base); //Eg: base = http://localhost:8080/wow/
		AuthorService service = AuthorService.login(base);
		if(service == null) {
			JOptionPane.showMessageDialog(this, "Invalid author name or password");
			clear();
			return;
		}
		load(base, service.getAuthor(), course, bayesType, isCreate);
	}
	
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		OverlayBayesUM um = umGraph.getUserModel();
		if(um.isChanged()) {
			if(JOptionPane.showConfirmDialog(this, "Overlay Bayesian model changed! Do you want to save it?", "Save user model", 
					JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
				try {
					umGraph.saveToAuthor(base, author, course, OverlayBayesUMFactory.getFormatType(OverlayBayesUM.OBUM_EXT), true);
					JOptionPane.showMessageDialog(this, "Saved successfully!");
				}
				catch(Exception e) {
					JOptionPane.showMessageDialog(this, "Saved unsuccessfully! Error: " + e.getMessage());
				}
			}
		}
		setVisible(false);
		super.dispose();
	}

	public static void main(String[] args) throws Exception {
		new OverlayBayesInspector(new URL("http://localhost:8080/wow/"), "author", "javatutorial", OverlayBayesUM.OBUM_DEFAULT_BAYESNET_TYPE);
	}

	
}


