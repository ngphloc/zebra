/*

    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 

*/
/**
 * AboutBox.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

 /**
  * This class defines the aboutbox dialog.
 */
package vn.spring.WOW.generatelisteditor;

import java.awt.*;

import java.net.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;


public class AboutBox {
    private JPanel jPanel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private TitledBorder titledBorder1;
    private JPanel jPanel2 = new JPanel();
    public URL home = null;
    public JDialog frame;
    private JPanel jPanel3 = new JPanel();
    private JTextArea jTextArea1 = new JTextArea();
   JButton jButton1 = new JButton();

    public AboutBox(JFrame parentFrame, URL base) {
        home = base;

        try {
            jbInit();

            frame = new JDialog(parentFrame, "Load Concept Editor application",
                                true);
            frame.setSize(500, 430);
            frame.setLocation(100, 100);
            frame.getContentPane().add(jPanel1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        titledBorder1 = new TitledBorder("");
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 18));
        jLabel1.setBorder(titledBorder1);
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("About Concept Editor");
        jLabel1.setBounds(new Rectangle(11, 7, 463, 47));
        jPanel1.setLayout(null);

        jPanel2.setBorder(BorderFactory.createEtchedBorder());
        jPanel2.setBounds(new Rectangle(12, 61, 464, 139));
        jPanel2.setLayout(null);

        URL url = null;

        try {
            url = new URL(home.toString() + "icons/agu-hcmuns.gif");
        } catch (Exception e) {
        }

        JButton buttonGif = new JButton(new ImageIcon(url));
        buttonGif.setBounds(new Rectangle(1, 1, 461, 131));
        buttonGif.setBorder(null);
        jPanel3.setBorder(BorderFactory.createEtchedBorder());
        jPanel3.setBounds(new Rectangle(12, 206, 465, 99));
        jPanel3.setLayout(null);

        jTextArea1.setText(
                "Concept Editor v 3.11\n Design by: Paul De Bra, Koen Verheyen" +
                " \n Programmer: Koen Verheyen, Natalia Stash, Bart Berden, \n" +
                " David Smits, reviewed and modified by Loc Nguyen\n" +
                " Build date: " + "20080101");
        jTextArea1.setBounds(new Rectangle(5, 6, 455, 84));
        jButton1.setBounds(new Rectangle(186, 315, 73, 29));
    jButton1.setText("Ok");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton1_actionPerformed(e);
      }
    });
    jPanel1.add(jLabel1, null);
        jPanel1.add(jPanel2, null);
        jPanel2.add(buttonGif);
        jPanel1.add(jPanel3, null);
        jPanel3.add(jTextArea1, null);
    jPanel1.add(jButton1, null);
    }

    public void show() {
        frame.setVisible(true);
    }

  void jButton1_actionPerformed(ActionEvent e) {
     frame.setVisible(false);
  }


}