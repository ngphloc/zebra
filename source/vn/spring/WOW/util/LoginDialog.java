/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * LoginDialog.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.util;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


/**
 * Dialogbox for WOW!-author authorization.
 *
 */
public class LoginDialog extends JDialog  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JButton jButton1 = new JButton();
    public  JDialog frame;
    public boolean id = false;
    private JButton Cancel_button = new JButton();
    private JPanel jPanel2 = new JPanel();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    public JTextField username = new JTextField();
    public JPasswordField password = new JPasswordField();

    public LoginDialog(JFrame parent) {
       super(parent, "Authorization", true);
  try {
			jbInit(parent);
            this.setSize(430, 250);
            this.setResizable(false);
            this.setLocation(100, 100);
            this.getContentPane().add(jPanel1);
            this.setVisible(true);
		}
		catch (Exception e) {
		}

    }

    private void jbInit(JFrame parent) throws Exception {
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 16));
        jLabel1.setBorder(BorderFactory.createLoweredBevelBorder());
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("WOW! Authorization");
        jLabel1.setBounds(new Rectangle(10, 5, 407, 44));
        jPanel1.setLayout(null);
        jButton1.setBounds(new Rectangle(197, 160, 102, 31));
        jButton1.setText("Ok");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButton1_actionPerformed(e);
            }
        });
        this.getRootPane().setDefaultButton(jButton1);
        Cancel_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Cancel_button_actionPerformed(e);
            }
        });
        Cancel_button.setText("Cancel");
        Cancel_button.setBounds(new Rectangle(305, 160, 100, 31));

        jPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
        jPanel2.setBounds(new Rectangle(11, 52, 407, 100));
        jPanel2.setLayout(null);
        jLabel2.setText("Password:");
        jLabel2.setBounds(new Rectangle(19, 51, 63, 17));
        jLabel3.setBounds(new Rectangle(20, 17, 41, 17));
        jLabel3.setText("User:");
        username.setText("");
        username.setBounds(new Rectangle(86, 15, 295, 25));
        password.setText("");
        password.setBounds(new Rectangle(86, 46, 295, 25));
        jPanel1.add(jLabel1, null);
        jPanel1.add(jButton1, null);
        jPanel1.add(Cancel_button, null);
        jPanel1.add(jPanel2, null);
        jPanel2.add(jLabel3, null);
        jPanel2.add(jLabel2, null);
        jPanel2.add(username, null);
        jPanel2.add(password, null);
    }



    void jButton1_actionPerformed(ActionEvent e) {
        id = true;
        this.setVisible(false);
        this.dispose();

    }

    void Cancel_button_actionPerformed(ActionEvent e) {
        id = false;
        this.setVisible(false);
        this.dispose();

    }
}