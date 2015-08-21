/*
   This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University
   WOW! is also open source software; 
   
 */

/**
 * DescriptionWindow.java 1.0, August 30, 2008.
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.formeditor;

import javax.swing.*;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;


/**
 * Class for the construction and working of a window to add a description to the form.
 *
 */
public class DescriptionWindow extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JTextField jTextField2;
    private JButton jButton1;
    private JButton jButton2;
    private FormEditor fEdit;

    /**
     * Constructor for the DescriptionWindow
     *
     * @param edit the FormEditor this window belongs to
     */
    public DescriptionWindow(FormEditor edit) {
        fEdit = edit;
        initComponents();

        //resize(300,150);
        setSize(300, 150);
        setVisible(true);
    }

    /**
     * Initializes the components of the DescriptionWindow
     */
    private void initComponents() {
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jTextField2 = new JTextField();
        jButton1 = new JButton();
        jButton2 = new JButton();

        getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints1;

        setTitle("Description");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(Color.lightGray);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    exitForm(evt);
                }
            });

        jLabel1.setText("Name");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(jLabel1, gridBagConstraints1);

        jLabel3.setText("Element" + fEdit.getCountElem());
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 10);
        gridBagConstraints1.weightx = 0.5;
        getContentPane().add(jLabel3, gridBagConstraints1);

        jLabel2.setText("Content");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(jLabel2, gridBagConstraints1);

        jTextField2.setToolTipText("Fill in extra description");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 10);
        gridBagConstraints1.weightx = 0.5;
        getContentPane().add(jTextField2, gridBagConstraints1);

        jButton1.setText("OK");
        jButton1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    OKAction(evt);
                }
            });
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.insets = new Insets(10, 30, 0, 10);
        getContentPane().add(jButton1, gridBagConstraints1);

        jButton2.setText("Cancel");
        jButton2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    CancelAction(evt);
                }
            });
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.insets = new Insets(10, 30, 0, 10);
        getContentPane().add(jButton2, gridBagConstraints1);

        pack();
    }

    /**
     * Performes the actions necessary when the OK-button is pressed
     *
     * @param evt the event that triggers this action
     */
    private void OKAction(ActionEvent evt) {
        fEdit.prop.addDescription(jLabel3.getText(), jTextField2.getText());
        fEdit.setEditorPanes();
        this.dispose();
    }

    /**
     * Performes the actions necessary when the Cancel-button is pressed
     *
     * @param evt the event that triggers this action
     */
    private void CancelAction(ActionEvent evt) {
        this.dispose();
    }

    /**
     * Exits this window
     *
     * @param evt the event that triggers this action
     */
    private void exitForm(WindowEvent evt) {
        dispose();
    }
}
