/*
   This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University
   WOW! is also open source software; 
   
 */

/**
 * ElementWindow.java 1.0 August 30, 2008.
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
 * Class for the construction and working of a window to add an element to the form.
 *
 */
public class ElementWindow extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblName;
    private JLabel lblSizeAllowed;
    private JLabel lblMaxLengthEnumList;
    private JLabel lblDefault;
    private JLabel lblConcept;
    private JLabel lblAttribute;
    private JLabel lblType;
    private JLabel lblUseDescription;
    private JLabel lblDescription;
    private JLabel lblConceptT;
    private JLabel lblAttributeT;
    private JLabel lblTypeT;
    private JLabel lblNameT;

    //private JTextField jTextField1;
    private JTextField txtSizeAllowed;
    private JTextField txtMaxLengthEnumList;
    private JTextField txtDefault;
    private JTextField txtDescription;
    private JCheckBox cbUseDescription;
    private JButton btnOk;
    private JButton btnCancel;
    private int fWType;
    private FormEditor fEdit;

    /**
     * Constructor for the ElementWindow
     *
     * @param edit the FormEditor this window belongs to
     * @param windowType the type of window (input, select or option)
     * @param concept the concept to which the element is connected
     * @param attr the attribute to which the element is connected
     * @param type the type of the attribute to which the element is connected
     * @param desc the description belonging to the attribute to which the element is connected
     */
    public ElementWindow(FormEditor edit, int windowType, String concept,
                         String attr, String type, String desc) {
        fEdit = edit;
        fWType = windowType;
        initComponents(concept, attr, type, desc);


        setSize(300, 300);
        setVisible(true);
    }

    /**
     * Initializes the components of the ElementWindow
     *
     * @param concept the concept to which the element is connected
     * @param attr the attribute to which the element is connected
     * @param type the type of the attribute to which the element is connected
     * @param desc the description belonging to the attribute to which the element is connected
     */
    private void initComponents(String concept, String attr, String type,
                                String desc) {
        lblName = new JLabel();
        lblSizeAllowed = new JLabel();
        lblMaxLengthEnumList = new JLabel();
        lblDefault = new JLabel();
        lblConcept = new JLabel();
        lblAttribute = new JLabel();
        lblType = new JLabel();
        lblUseDescription = new JLabel();
        lblDescription = new JLabel();
        lblConceptT = new JLabel();
        lblAttributeT = new JLabel();
        lblTypeT = new JLabel();
        lblNameT = new JLabel();

        //jTextField1 = new JTextField();
        txtSizeAllowed = new JTextField();
        txtMaxLengthEnumList = new JTextField();
        txtDefault = new JTextField();
        txtDescription = new JTextField();
        cbUseDescription = new JCheckBox();
        btnOk = new JButton();
        btnCancel = new JButton();

        getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints1;

        switch (fWType) {
        case 1:
            setTitle("Input");

            break;

        case 2:
            setTitle("Select");

            break;

        case 3:
            setTitle("Option");

            break;
        }

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(Color.lightGray);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitForm(evt);
            }
        });

        lblName.setText("Name");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(lblName, gridBagConstraints1);

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(5, 0, 5, 0);
        getContentPane().add(new JSeparator(), gridBagConstraints1);

        lblNameT.setText("Element" + fEdit.getCountElem());
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 10);
        gridBagConstraints1.weightx = 0.5;
        getContentPane().add(lblNameT, gridBagConstraints1);

        switch (fWType) {
        case 1:
            lblSizeAllowed.setText("Size");

            break;

        case 2:
            lblSizeAllowed.setText("Size");

            break;

        case 3:
            lblSizeAllowed.setText("Allowed");

            break;
        }

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(lblSizeAllowed, gridBagConstraints1);

        switch (fWType) {
        case 1:
            lblMaxLengthEnumList.setText("MaxLength");

            break;

        case 2:
            lblMaxLengthEnumList.setText("EnumList");

            break;

        case 3:
            lblMaxLengthEnumList.setText("EnumList");

            break;
        }

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(lblMaxLengthEnumList, gridBagConstraints1);

        lblDefault.setText("Default");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(lblDefault, gridBagConstraints1);

        switch (fWType) {
        case 1:
            txtSizeAllowed.setToolTipText("Fill in size of input field");
            txtMaxLengthEnumList.setToolTipText("Fill in maximum input size");
            txtDefault.setToolTipText("Fill in default value");

            break;

        case 2:
            txtSizeAllowed.setToolTipText("Fill in size of select field");
            txtMaxLengthEnumList.setToolTipText(
                    "Give semi-colon seperated list of values");
            txtDefault.setToolTipText("Fill in default value");
            // brendan: according to the w3c html standard there is no default element in a selection
            txtDefault.setEnabled(false);

            break;

        case 3:
            txtSizeAllowed.setToolTipText("Fill in the allowed number of choices");
            txtMaxLengthEnumList.setToolTipText(
                    "Give semi-colon seperated list of values");
            txtDefault.setToolTipText("Fill in default value");

            break;
        }

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 10);
        gridBagConstraints1.weightx = 0.5;
        getContentPane().add(txtSizeAllowed, gridBagConstraints1);

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 10);
        gridBagConstraints1.weightx = 0.5;
        getContentPane().add(txtMaxLengthEnumList, gridBagConstraints1);

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 10);
        gridBagConstraints1.weightx = 0.5;
        getContentPane().add(txtDefault, gridBagConstraints1);

        lblConcept.setText("Concept");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(lblConcept, gridBagConstraints1);

        lblAttribute.setText("Attribute");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 6;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(lblAttribute, gridBagConstraints1);

        lblType.setText("Type");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(lblType, gridBagConstraints1);

        lblUseDescription.setText("Use Description");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(lblUseDescription, gridBagConstraints1);

        lblDescription.setText("Description");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(lblDescription, gridBagConstraints1);

        txtDescription.setText(desc);
        txtDescription.setToolTipText("The description belonging to the element");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 10);
        gridBagConstraints1.weightx = 0.5;
        getContentPane().add(txtDescription, gridBagConstraints1);

        lblConceptT.setText(concept);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(lblConceptT, gridBagConstraints1);

        lblAttributeT.setText(attr);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 6;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(lblAttributeT, gridBagConstraints1);

        lblTypeT.setText(type);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(lblTypeT, gridBagConstraints1);

        cbUseDescription.setSelected(true);
        cbUseDescription.setBackground(Color.lightGray);
        cbUseDescription.setToolTipText("wether or not the description is used");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new Insets(0, 10, 0, 0);
        getContentPane().add(cbUseDescription, gridBagConstraints1);

        btnOk.setText("OK");
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                OKAction(evt);
            }
        });
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 10;
        gridBagConstraints1.insets = new Insets(10, 30, 0, 10);
        getContentPane().add(btnOk, gridBagConstraints1);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                CancelAction(evt);
            }
        });
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 10;
        gridBagConstraints1.insets = new Insets(10, 30, 0, 10);
        getContentPane().add(btnCancel, gridBagConstraints1);

        pack();
    }

    /**
     * Performes the actions necessary when the OK-button is pressed
     *
     * @param evt the event that triggers this action
     */
    private void OKAction(ActionEvent evt) {
        if (checkInput()) {
            switch (fWType) {
            case 1:
                fEdit.prop.addInput(lblNameT.getText(), txtSizeAllowed.getText(),
                                    txtMaxLengthEnumList.getText(),
                                    txtDefault.getText(),
                                    txtDescription.getText(),
                                    cbUseDescription.isSelected(), lblConceptT.getText(),
                                    lblAttributeT.getText(), lblTypeT.getText());

                break;

            case 2:
                fEdit.prop.addSelect(lblNameT.getText(), txtSizeAllowed.getText(),
                                     txtMaxLengthEnumList.getText(),
                                     txtDefault.getText(),
                                     txtDescription.getText(),
                                     cbUseDescription.isSelected(),
                                     lblConceptT.getText(), lblAttributeT.getText(),
                                     lblTypeT.getText());

                break;

            case 3:
                fEdit.prop.addOption(lblNameT.getText(), txtSizeAllowed.getText(),
                                     txtMaxLengthEnumList.getText(),
                                     txtDefault.getText(),
                                     txtDescription.getText(),
                                     cbUseDescription.isSelected(),
                                     lblConceptT.getText(), lblAttributeT.getText(),
                                     lblTypeT.getText());

                break;
            }

            fEdit.setEditorPanes();
            this.dispose();
        }
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
     * Checks if the given input is correct
     *
     * @return boolean the given input is (not) correct
     */
    private boolean checkInput() {
        boolean ok = false;
        String type = lblTypeT.getText();
        String def = txtDefault.getText();

        if (def.length() == 0) { ok = true;}
        if (def.length() > 0) {
            if (type.equals("bool")) {
                if (!def.equals("true") && !def.equals("false")) {
                    giveWarning("Default is no boolean value!");
                } else {
                    ok = true;
                }
            } else if (type.equals("int")) {
                int p = 0;

                try {
                    p = Integer.parseInt(def);

                    if ((p < 0) || (p > 100)) {
                        giveWarning("Default is no integer between 0 and 100!");
                    } else {
                        ok = true;
                    }
                } catch (NumberFormatException nfe) {
                    giveWarning("Default is no integer value!");
                }
            } else if (type.equals("string")) {
                ok = true;
            }
        }

        if (ok && (fWType > 1)) {
            String list = txtMaxLengthEnumList.getText();
            String in = "";
            int previndex = 0;
            int index = list.indexOf(';', previndex);

            while (ok && (index >= 0)) {
                in = list.substring(previndex, index);

                if (in.length() > 0) {
                    if (type.equals("bool")) {
                        if (!in.equals("true") && !in.equals("false")) {
                            giveWarning(in + " is no boolean value!");
                            ok = false;
                        } else {
                            ok = true;
                        }
                    } else if (type.equals("int")) {
                        int p = 0;

                        try {
                            p = Integer.parseInt(in);

                            if ((p < 0) || (p > 100)) {
                                giveWarning(in +
                                            " is no integer between 0 and 100!");
                                ok = false;
                            } else {
                                ok = true;
                            }
                        } catch (NumberFormatException nfe) {
                            giveWarning(in + " is no integer value!");
                            ok = false;
                        }
                    }
                }

                previndex = index + 1;
                index = list.indexOf(';', previndex);
            }

            in = list.substring(previndex);

            if (ok && (in.length() > 0)) {
                if (type.equals("bool")) {
                    if (!in.equals("true") && !in.equals("false")) {
                        giveWarning(in + " is no boolean value!");
                        ok = false;
                    } else {
                        ok = true;
                    }
                } else if (type.equals("int")) {
                    int p = 0;

                    try {
                        p = Integer.parseInt(in);

                        if ((p < 0) || (p > 100)) {
                            giveWarning(in +
                                        " is no integer between 0 and 100!");
                            ok = false;
                        } else {
                            ok = true;
                        }
                    } catch (NumberFormatException nfe) {
                        giveWarning(in + " is no integer value!");
                        ok = false;
                    }
                }
            }
        }

        return ok;
    }

    /**
     * Gives a warning
     *
     * @param warning the warning to be given
     */
    private void giveWarning(String warning) {
        JOptionPane.showMessageDialog(null, warning, "Error", JOptionPane.ERROR_MESSAGE);
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