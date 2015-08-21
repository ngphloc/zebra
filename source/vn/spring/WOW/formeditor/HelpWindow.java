/*
   This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University
   WOW! is also open source software; 
   
 */

/**
 * HelpWindow.java 1.0, August 30, 2008.
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.formeditor;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Class for the construction and working of a HelpWindow
 *
 * @see javax.swing.JFrame
 */
public class HelpWindow extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private URL home;
    private String helptext = "";
    private JEditorPane jEditorPane1;
    private JScrollPane jScrollPane1;
    private JToolBar jToolBar1;
    private JButton jButton1;

    /**
     * Constructor for the HelpWindow
     *
     * @param base the URL of the applet
     */
    public HelpWindow(URL base) {
        home = base;
        loadHelp();
        initComponents();
        setTitle("Help");

        setSize(300, 600);
        setVisible(true);
    }

    /**
     * Initializes the components
     */
    private void initComponents() {
        jEditorPane1 = new JEditorPane();
        jScrollPane1 = new JScrollPane(jEditorPane1);
        jToolBar1 = new JToolBar();
        jButton1 = new JButton();

        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jEditorPane1.setEditable(false);
        jEditorPane1.setEditorKit(new javax.swing.text.html.HTMLEditorKit());
        jEditorPane1.setText(helptext);
        getContentPane().add(jScrollPane1, BorderLayout.CENTER);

        jButton1.setText("Close");
        jButton1.setToolTipText("Close Help Window");
        jButton1.addActionListener(new ButtonAction());
        jToolBar1.setPreferredSize(new Dimension(18, 30));
        jToolBar1.setFloatable(false);
        jToolBar1.setLayout(new FlowLayout());
        jToolBar1.add(jButton1);
        getContentPane().add(jToolBar1, BorderLayout.SOUTH);

        pack();
    }

    /**
     * Loads the helpfile
     */
    private void loadHelp() {
        String urlstring = home.toString() + "help.html";

        try {
            URL url = new URL(urlstring);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            InputStream in = uc.getInputStream();

            byte[] b = new byte[1];
            String instring = new String("");

            while (in.read(b, 0, 1) > 0) {
                String temp = new String(b);
                instring = instring + temp;
            }

            helptext = instring;
            in.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No helpfile available. Please consult the WOW! tutorial for information on the FormEditor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Implementation of an <code>ActionListener</code> for the button of the HelpWindow.
     *
     * @see java.awt.event.ActionListener
     */
    class ButtonAction implements ActionListener {
        public ButtonAction() {
        }

        /**
         * Overrides the <code>actionPerformed</code> of <code>ActionListener</code>.
         *
         * @param evt the event that triggers the action
         */
        public void actionPerformed(ActionEvent evt) {
            JButton source = (JButton) (evt.getSource());
            String item = source.getText();

            if (item.equals("Close")) {
                dispose();
            }
        }
    }
}
