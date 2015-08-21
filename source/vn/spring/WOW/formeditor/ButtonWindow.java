/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/

/**
 * ButtonWindow.java 1.0 August 30, 2008.
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.formeditor;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.*;


/**
 * Class for the construction and working of a window to add a button to the form.
 *
 */
public class ButtonWindow extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel jLabel1;
    private JLabel jLabel2;
    private JTextField jTextField1;
    private JList jList1;
    private JButton jButton1;
    private JButton jButton2;
    private FormEditor fEdit;

    /**
     * Constructor for the ButtonWindow
     *
     * @param edit the FormEditor this window belongs to
     */
    public ButtonWindow(FormEditor edit) {
        fEdit = edit;
        initComponents();
        setSize(300, 180);
        setVisible(true);
    }

    /**
     * Initializes the components of the DescriptionWindow
     */
    private void initComponents() {
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jTextField1 = new JTextField();

        String[] data = { "submit", "reset" };
        jList1 = new JList(data);
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList1.setSelectedIndex(0);
        jButton1 = new JButton();
        jButton2 = new JButton();

        getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints1;

        setTitle("Button");
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
        gridBagConstraints1.insets = new Insets(15, 10, 0, 0);
        getContentPane().add(jLabel1, gridBagConstraints1);

        jTextField1.setToolTipText("Fill in name of button");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(15, 10, 0, 10);
        gridBagConstraints1.weightx = 0.5;
        getContentPane().add(jTextField1, gridBagConstraints1);

        jLabel2.setText("Type");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 10, 0);
        getContentPane().add(jLabel2, gridBagConstraints1);

        jList1.setToolTipText("Choose between submit or reset button");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.gridheight = 2;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 10, 10);
        gridBagConstraints1.weightx = 0.5;
        getContentPane().add(jList1, gridBagConstraints1);

        jButton1.setText("OK");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                OKAction(evt);
            }
        });
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.gridheight = 1;
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
        fEdit.prop.addButton(jTextField1.getText(),
                             jList1.getSelectedValue().toString());
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
