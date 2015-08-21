/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * FileLoad.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.GraphAuthor;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import javax.swing.*;

import vn.spring.WOW.graphauthor.author.*;

/**
 * This class loads a saved xml file into memory and shows the relations on the graph.
 *
 */

public class FileLoad {
    private JPanel jPanel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JButton jButton1 = new JButton();
    public JDialog frame;
    public String fileName;
    public boolean cancelled;
    public URL home;
    public String dirname;

    private JButton Cancel_button = new JButton();
    private JComboBox jComboBox1 = new JComboBox();

    public FileLoad(URL base, JFrame parentFrame) {
      cancelled = false;
      home = base;
      String path = home.getPath();
      String pathttemp = path.substring(1, path.length());
      int index = pathttemp.indexOf("/");
      index++;
      dirname = path.substring(0, index);
      if (dirname.equals("/GraphAuthor")) {
		  dirname = "";
	  }
      try {
            jbInit();
            fillList();
            frame = new JDialog(parentFrame, "Load Graph Author application", true);
            frame.setSize(600, 200);
            frame.setLocation(100, 100);
            frame.getContentPane().add(jPanel1);
            frame.getRootPane().setDefaultButton(jButton1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public void fillList() {

  try {


     URL url = new URL("http://" + home.getHost() + ":" + home.getPort() +
                       dirname +
                       "/servlet/authorservlets.ListFiles?extention=" +
                       ".gaf" + "&userName=" + AuthorSTATIC.authorName);
     BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
     String sFile = "";

     do {
      sFile = in.readLine();
     if (sFile != null) {
       this.jComboBox1.addItem(sFile.trim());
         }

     } while (sFile != null);
     in.close();

 } catch (Exception e) {
     System.out.println("error");
        }
       }

    private void jbInit() throws Exception {
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 16));
        jLabel1.setBorder(BorderFactory.createLoweredBevelBorder());
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("File Load Dialog");
        jLabel1.setBounds(new Rectangle(12, 9, 557, 44));
        jPanel1.setLayout(null);
        jButton1.setBounds(new Rectangle(286, 101, 138, 36));
    	jButton1.setSelected(true);
        jButton1.setText("Load");
        jButton1.setDefaultCapable(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButton1_actionPerformed(e);
            }
        });
        Cancel_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Cancel_button_actionPerformed(e);
            }
        });
    Cancel_button.setText("Cancel");
    Cancel_button.setBounds(new Rectangle(432, 100, 138, 36));
        jComboBox1.setEditable(true);
    jComboBox1.setBounds(new Rectangle(11, 61, 558, 30));
    jPanel1.add(jLabel1, null);
    jPanel1.add(jComboBox1, null);
    jPanel1.add(Cancel_button, null);
    jPanel1.add(jButton1, null);

    }

    public void show() {
        frame.setVisible(true);
    }

    void jButton1_actionPerformed(ActionEvent e) {
        fileName =  ((String) this.jComboBox1.getSelectedItem()).trim();
        frame.setVisible(false);
        frame.dispose();
        frame = null;
    }
  void Cancel_button_actionPerformed(ActionEvent e) {

       cancelled = true;
       frame.setVisible(false);
       frame.dispose();
       frame = null;

  }
}