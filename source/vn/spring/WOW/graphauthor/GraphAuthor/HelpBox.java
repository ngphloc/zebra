/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * HelpBox.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.GraphAuthor;

import java.awt.*;

import java.net.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

/**
 * This class displays the helpbox.
 *
 */
public class HelpBox {
    private JPanel jPanel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private TitledBorder titledBorder1;
    public URL home = null;
    public JDialog frame;
  JButton jButton1 = new JButton();
  JScrollPane jScrollPane1 = new JScrollPane();
  JEditorPane jEditorPane1 = new JEditorPane();

    public HelpBox(JFrame parentFrame, URL base) {
        home = base;

        try {
            jbInit();

            frame = new JDialog(parentFrame, "Load Graph Author application",
                                true);
            frame.setSize(600, 450);
            frame.setLocation(100, 100);
            frame.getContentPane().add(jPanel1);
            frame.getRootPane().setDefaultButton(jButton1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        titledBorder1 = new TitledBorder("");
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 18));
        jLabel1.setBorder(titledBorder1);
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Help Graph Author");
        jLabel1.setBounds(new Rectangle(11, 7, 549, 47));
        jPanel1.setLayout(null);


        try {
            new URL(home.toString() + "icons/agu-hcmuns.gif");
        } catch (Exception e) {
        }

        jButton1.setBounds(new Rectangle(246, 347, 73, 29));
    jButton1.setText("Ok");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jButton1_actionPerformed(e);
      }
    });
    jScrollPane1.setBounds(new Rectangle(11, 60, 552, 279));
    jEditorPane1.setText("For more information look at the WOW! website:http://wow.hcmuns.edu.vn/");
    jPanel1.add(jLabel1, null);
    jPanel1.add(jButton1, null);
    jPanel1.add(jScrollPane1, null);
    jScrollPane1.getViewport().add(jEditorPane1, null);
    }

    public void show() {
        frame.setVisible(true);
    }

  void jButton1_actionPerformed(ActionEvent e) {
     frame.setVisible(false);
  }
}