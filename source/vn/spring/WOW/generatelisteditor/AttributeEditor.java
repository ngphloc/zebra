/*

    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 

*/
/**
 * AttributeEditor.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.generatelisteditor;

// WOW
import vn.spring.WOW.datacomponents.*;

// layout
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.border.BevelBorder;

// components
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

// Helpers
import javax.swing.DefaultListModel;
import java.awt.Dimension;

//events
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class AttributeEditor extends JProtectedPanel
{
	private static final long serialVersionUID = 1L;

   GridBagLayout layout = new GridBagLayout();

    private JTextField name = new JTextField();
    private JTextArea description = new JTextArea();
    private JTextField txtDefault = new JTextField();
    private JList generatelistItems = new JList(new DefaultListModel());
    private String[] typeList= { "integer", "string", "boolean" };
    private JComboBox type = new JComboBox(typeList);

    // stable controls
    private JCheckBox chkStable = new JCheckBox();
    private String[] stableList = { Attribute.STABLE_ALWAYS, Attribute.STABLE_SESSION, Attribute.STABLE_FREEZE };
    private JComboBox cmbStable = new JComboBox(stableList);
    private JTextField txtStableExpr = new JTextField();

    // casegroup controls
    private JCheckBox chkCaseGroup = new JCheckBox();
    private JTextField txtDefaultCaseGroup = new JTextField();
    private JList casevalueListItems = new JList(new DefaultListModel());
    private JButton addCaseValueListButton = new JButton("Add");
    private JButton editCaseValueListButton = new JButton("Edit");
    private JButton removeCaseValueListButton = new JButton("Remove");

    private boolean isSystem;
    private boolean isStable;
    private boolean isCaseGroup;

    private JCheckBox isChangeableText = new JCheckBox("Changeable?");
    private JCheckBox isPersistentText = new JCheckBox("Persistent?");

    private Attribute attr;
    private Concept parent;

    public AttributeEditor(Attribute attribute, Concept parent)
    {
        attr = attribute;
        this.parent = parent;

        name.setText(attr.getName());
        description.setText(attr.getDescription());
        txtDefault.setText(attr.getDefault());
        type.setSelectedItem(AttributeType.toString(attr.getType()));

        // initialize the checkboxes (indirectly)
        isSystem = attr.isSystem();
        isPersistentText.setSelected(attr.isPersistent());

        // initialize the stable values
        enableOrDisableStable();
        if (chkStable.isEnabled()) {
            String stable = GenerateListData.S2D(attr.getStable());
            if (!stable.equals("")) {
                isStable = true;
                cmbStable.setSelectedItem(stable);
                if (stable.equals(Attribute.STABLE_FREEZE)) {
                    txtStableExpr.setText(attr.getStableExpression());
                    txtStableExpr.setEnabled(true);
                } else {
                    txtStableExpr.setEnabled(false);
                }
            } else {
                isStable = false;
                txtStableExpr.setText("");
                cmbStable.setSelectedIndex(-1);
            }
        }

        // initialize the casegroup values
        if (attr.hasGroupNode()) {
            isCaseGroup = true;
            txtDefaultCaseGroup.setText(attr.getCasegroup().getDefaultFragment());

            // fill listbox casevalueListItems
            casevalueListItems.setListData(attr.getCasegroup().getCaseValues());
            casevalueListItems.setVisibleRowCount(5);
        } else {
            isCaseGroup = false;
            txtDefaultCaseGroup.setText("");

            // disable the buttons
            txtDefaultCaseGroup.setEnabled(isCaseGroup);
            casevalueListItems.setEnabled(isCaseGroup);
            addCaseValueListButton.setEnabled(isCaseGroup);
            editCaseValueListButton.setEnabled(isCaseGroup);
            removeCaseValueListButton.setEnabled(isCaseGroup);
        }

        generatelistItems.setListData(attr.getActions());
        generatelistItems.setVisibleRowCount(5);

        this.setLayout(layout);
        makeGui();
    }

    private void makeGui()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

//      JTextField typeText = new JTextField("Type :");

        // make the name text field
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel nameText = new JLabel("Name :");
                nameText.setFocusable(false);
//        nameText.setBorder(null);
//        nameText.setEditable(false);
        nameText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(nameText, constraints);
        this.add(nameText);

        // make the Description text field.
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel descriptionText = new JLabel("Description :");
                descriptionText.setFocusable(false);
//        descriptionText.setBorder(null);
//        descriptionText.setEditable(false);
        descriptionText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(descriptionText, constraints);
        this.add(descriptionText);

        // make the default text field.
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 4;
        JLabel defaultText = new JLabel("Default :");
//        defaultText.setFocusable(false);
//        defaultText.setBorder(null);
//        defaultText.setEditable(false);
        defaultText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(defaultText, constraints);
        this.add(defaultText);

        // make the Requirement text field.
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 5;
        JLabel propertiesText = new JLabel("Properties :");
                propertiesText.setFocusable(false);
//        propertiesText.setBorder(null);
//        propertiesText.setEditable(false);
//        propertiesText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(propertiesText, constraints);
        this.add(propertiesText);

        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 9;
        JLabel attributeText = new JLabel("Generate list Item :");
//        attributeText.setFocusable(false);
//        attributeText.setBorder(null);
//        attributeText.setEditable(false);
        attributeText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(attributeText, constraints);
        this.add(attributeText);

// added by @Loc Nguyen @ 21-02-2008

        // stability text
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 13;
        JLabel stableText = new JLabel("Stability :");
//        stableText.setFocusable(false);
//        stableText.setBorder(null);
//        stableText.setEditable(false);
        stableText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(stableText, constraints);
        this.add(stableText);

        // casegroup text
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 16;
        JLabel txtCasegroup = new JLabel("Casegroup :");
//        txtCasegroup.setFocusable(false);
//        txtCasegroup.setBorder(null);
//        txtCasegroup.setEditable(false);
        txtCasegroup.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(txtCasegroup, constraints);
        this.add(txtCasegroup);

// end added by @Loc Nguyen @ 21-02-2008

        // Adding the data fields
        constraints.weightx = 1.0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridx = 1;
        constraints.gridy = 0;
        name.setEnabled(!isSystem);
        name.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        layout.setConstraints(name, constraints);
        this.add(name);

        constraints.gridx = 1;
        constraints.gridheight = 2;
        constraints.gridy = 1;
        description.setEnabled(!isSystem);
        description.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        JScrollPane descriptionHolder = new JScrollPane(description);
        descriptionHolder.setPreferredSize(new Dimension(300, 40));
        layout.setConstraints(descriptionHolder, constraints);
        this.add(descriptionHolder);

        // default
        constraints.weightx = 1.0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridx = 1;
        constraints.gridy = 4;
        txtDefault.setEnabled(!isSystem);
        txtDefault.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        layout.setConstraints(txtDefault, constraints);
        this.add(txtDefault);

        // properties
        constraints.gridx = 1;
        constraints.gridheight = 3;
        constraints.gridy = 5;
        JPanel properties = makePropertiesPanel();
        properties.setBorder(new BevelBorder(BevelBorder.LOWERED));
        layout.setConstraints(properties, constraints);
        this.add(properties);

        constraints.weightx = 0;
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        constraints.fill=GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridheight = 1;
        constraints.gridy = 8;
        JButton commitChangesButton = new JButton("Commit Changes");
        commitChangesButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                save();
            }

        });
        layout.setConstraints(commitChangesButton, constraints);
        this.add(commitChangesButton);

        // add generateList listbox
        constraints.fill=GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1.0;
        constraints.gridx = 1;
        constraints.gridheight = 5;
        constraints.gridy = 9;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        generatelistItems.setPreferredSize(new Dimension(200, 100));
        layout.setConstraints(this.generatelistItems, constraints);
        this.add(generatelistItems);

// added by Bart @21-02-2008

        // stable checkbox (is defined above)
        chkStable.setSelected(this.isStable);
        chkStable.addActionListener(new ActionListener()
        {
                public void actionPerformed(ActionEvent e)
                {
                        setChanged();
                        toggleIsStable();
                }
        });
        constraints.weightx = 0;
        // constraints.insets.top = 27;
        constraints.gridx = 1;
        constraints.gridheight = 1;
        constraints.gridy = 13;
        constraints.gridwidth = 1;
        layout.setConstraints(chkStable, constraints);
        this.add(chkStable);

        // add stable panel
        constraints.gridx = 1;
        constraints.gridheight = 2;
        constraints.gridy = 14;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        JPanel stablePanel = makeStablePanel();
        stablePanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        layout.setConstraints(stablePanel, constraints);
        this.add(stablePanel);

        // casegroup checkbox (is defined at the beginning)
        chkCaseGroup.setSelected(this.isCaseGroup);
        chkCaseGroup.addActionListener(new ActionListener()
        {
                public void actionPerformed(ActionEvent e)
                {
                        setChanged();
                        toggleIsCaseGroup();
                }
        });
        constraints.weightx = 0;
        // constraints.insets.top = 27;
        constraints.gridx = 1;
        constraints.gridheight = 1;
        constraints.gridy = 16;
        constraints.gridwidth = 1;
        layout.setConstraints(chkCaseGroup, constraints);
        this.add(chkCaseGroup);

        // add case group panel
        constraints.gridx = 1;
        constraints.gridheight = 5;
        constraints.gridy = 17;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        JPanel caseGroupPanel = makeCaseGroupPanel();
        caseGroupPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        layout.setConstraints(caseGroupPanel, constraints);
        this.add(caseGroupPanel);

// end added by Bart

        constraints.gridheight = 1;
        JButton addGenerateListButton = new JButton("Add");
        constraints.weightx = 0;
        constraints.insets.top = 27;
        constraints.gridx = 2;
        constraints.gridy = 9;
        constraints.gridwidth = 1;
        addGenerateListButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addGenerateList();
            }
        });
        layout.setConstraints(addGenerateListButton, constraints);
        this.add(addGenerateListButton);

        JButton editAttributeButton = new JButton("Edit");
        constraints.weightx = 0;
        constraints.insets.top = 0;
        constraints.gridx = 2;
        constraints.gridy = 10;
        constraints.gridwidth = 1;
        editAttributeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                editGenerateList();
            }
        });
        layout.setConstraints(editAttributeButton, constraints);
        this.add(editAttributeButton);

        constraints.ipady = 0;
        constraints.weightx = 0;
        constraints.gridx = 2;
        constraints.gridy = 11;
        constraints.gridwidth = 1;
        JButton removeAttributeButton = new JButton("Remove");
        removeAttributeButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                removeGenerateList();
            }
        });
        layout.setConstraints(removeAttributeButton, constraints);
        this.add(removeAttributeButton);
    }


    private JPanel makePropertiesPanel()
    {
        JPanel properties = new JPanel();
        GridBagLayout propertiesLayout = new GridBagLayout();

        properties.setLayout(propertiesLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        // add textfield type:
        JTextField typeText = new JTextField("Type :");
        typeText.setFocusable(false);
        typeText.setBorder(null);
        typeText.setEditable(false);
        typeText.setHorizontalAlignment(JTextField.RIGHT);
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridheight = 1;
        constraints.gridy = 0;
        propertiesLayout.setConstraints(typeText, constraints);
        properties.add(typeText);

        // add combobox type
        type.setEnabled(!isSystem);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        constraints.gridx = 1;
        constraints.gridheight = 1;
        constraints.gridy = 0;
        type.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setChanged();
                // toggleIsChangeable();
            }
        });
        propertiesLayout.setConstraints(type, constraints);
        properties.add(type);

        // is changeable checkbox
        isChangeableText.setEnabled(!isSystem);
        isChangeableText.setSelected(!attr.isReadonly());
        isChangeableText.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setChanged();
                // toggleIsChangeable();
            }
        });
        constraints.weightx = 0.5;
        constraints.gridx = 0;
        constraints.gridheight = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        propertiesLayout.setConstraints(isChangeableText, constraints);
        properties.add(isChangeableText);

        isPersistentText.setEnabled(!this.isSystem);
        isPersistentText.setSelected(attr.isPersistent());
        isPersistentText.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setChanged();
                enableOrDisableStable();
            }
        });
        constraints.weightx = 0.5;
        constraints.gridx = 2;
        constraints.gridheight = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        propertiesLayout.setConstraints(isPersistentText, constraints);
        properties.add(isPersistentText);

        // create Object
        JCheckBox isSystemText = new JCheckBox("System?");
        // isSystemText.setEnabled(!this.isSystem);
        isSystemText.setSelected(this.isSystem);
        isSystemText.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setChanged();
                toggleIsSystem();
            }
        });
        // set layout constraints
        constraints.weightx = 0.5;
        constraints.gridx = 0;
        constraints.gridheight = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        // add object to Plane
        propertiesLayout.setConstraints(isSystemText, constraints);
        properties.add(isSystemText);

        return properties;
    }

    private JPanel makeStablePanel()
    {
        JPanel stablePanel = new JPanel();
        GridBagLayout stableLayout = new GridBagLayout();

        stablePanel.setLayout(stableLayout);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        // stable selection textfield
        JTextField txtStable = new JTextField("Stable :");
        txtStable.setFocusable(false);
        txtStable.setBorder(null);
        txtStable.setEditable(false);
        // txtStable.setHorizontalAlignment(JTextField.RIGHT);
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridheight = 1;
        constraints.gridy = 0;
        stableLayout.setConstraints(txtStable, constraints);
        stablePanel.add(txtStable);

        // stable expression textfield
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridheight = 1;
        constraints.gridy = 1;
        JTextField stableExprText = new JTextField("Expr :");
        stableExprText.setFocusable(false);
        stableExprText.setBorder(null);
        stableExprText.setEditable(false);
        // txtStableExpr.setHorizontalAlignment(JTextField.RIGHT);
        stableLayout.setConstraints(stableExprText, constraints);
        stablePanel.add(stableExprText);

        // add combobox cmbStable
        cmbStable.setEnabled(this.isStable);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        constraints.gridx = 1;
        constraints.gridheight = 1;
        constraints.gridy = 0;
        cmbStable.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setChanged();
                String selectedItem = "";
                selectedItem = (String) cmbStable.getSelectedItem();
                if (selectedItem != null) {
                    if (selectedItem.equals(Attribute.STABLE_FREEZE)) {
                        txtStableExpr.setEnabled(true);
                    } else {
                        txtStableExpr.setEnabled(false);
                    }
                }
                // toggleIsChangeable();
            }
        });
        stableLayout.setConstraints(cmbStable, constraints);
        stablePanel.add(cmbStable);

        // add stable expression input box (defined at the beginning)
        constraints.weightx = 1.0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        txtStableExpr.setEnabled(this.isStable);
        txtStableExpr.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        stableLayout.setConstraints(txtStableExpr, constraints);
        stablePanel.add(txtStableExpr);

        return stablePanel;
    }

    private JPanel makeCaseGroupPanel()
    {
        JPanel caseGroupPanel = new JPanel();
        GridBagLayout caseGroupLayout = new GridBagLayout();
        caseGroupPanel.setLayout(caseGroupLayout);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        // casegroup default textfield
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridheight = 1;
        constraints.gridy = 0;
        JTextField CaseDefaultText = new JTextField("Default :");
        CaseDefaultText.setFocusable(false);
        CaseDefaultText.setBorder(null);
        CaseDefaultText.setEditable(false);
        // txtStableExpr.setHorizontalAlignment(JTextField.RIGHT);
        caseGroupLayout.setConstraints(CaseDefaultText, constraints);
        caseGroupPanel.add(CaseDefaultText);

        // casegroup casevalues textfield
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridheight = 1;
        constraints.gridy = 1;
        JTextField CaseValuesText = new JTextField("Casevalues :");
        CaseValuesText.setFocusable(false);
        CaseValuesText.setBorder(null);
        CaseValuesText.setEditable(false);
        // txtStableExpr.setHorizontalAlignment(JTextField.RIGHT);
        caseGroupLayout.setConstraints(CaseValuesText, constraints);
        caseGroupPanel.add(CaseValuesText);

        // add default casevalue input box (is defined at the beginning)
        constraints.weightx = 1.0;
        // constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridwidth = 1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        txtDefaultCaseGroup.setEnabled(this.isCaseGroup);
        txtDefaultCaseGroup.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        caseGroupLayout.setConstraints(txtDefaultCaseGroup, constraints);
        caseGroupPanel.add(txtDefaultCaseGroup);

        // add casevalues listbox
        constraints.fill=GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1.0;
        constraints.gridx = 1;
        constraints.gridheight = 4;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        casevalueListItems.setPreferredSize(new Dimension(200, 100));
        JScrollPane casevalueListHolder = new JScrollPane(this.casevalueListItems);
        caseGroupLayout.setConstraints(casevalueListHolder, constraints);
        caseGroupPanel.add(casevalueListHolder);

        // add the add, edit and remove buttons
        constraints.gridheight = 1;
        // add the add button (defined at the beginning)
        constraints.weightx = 0;
        // constraints.insets.top = 27;
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        addCaseValueListButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addCaseValueList();
            }
        });
        caseGroupLayout.setConstraints(addCaseValueListButton, constraints);
        caseGroupPanel.add(addCaseValueListButton);

        // add the edit button (defined at the beginning)
        constraints.weightx = 0;
        // constraints.insets.top = 27;
        constraints.gridx = 2;
        constraints.gridy =3;
        constraints.gridwidth = 1;
        editCaseValueListButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                editCaseValueList();
            }
        });
        caseGroupLayout.setConstraints(editCaseValueListButton, constraints);
        caseGroupPanel.add(editCaseValueListButton);

        // add the remove button (defined at the beginning)
        constraints.ipady = 0;
        constraints.weightx = 0;
        // constraints.insets.top = 27;
        constraints.gridx = 2;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        removeCaseValueListButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                removeCaseValueList();
            }
        });
        caseGroupLayout.setConstraints(removeCaseValueListButton, constraints);
        caseGroupPanel.add(removeCaseValueListButton);

        return caseGroupPanel;
    }

    // sets or unsets the system setting, enables or disables the controls that depend on this
    private void toggleIsSystem()
    {
        isSystem = !isSystem;

        // enable it all ...
        isPersistentText.setEnabled(!this.isSystem);
        isChangeableText.setEnabled(!this.isSystem);
        type.setEnabled(!this.isSystem);
        name.setEnabled(!this.isSystem);
        description.setEnabled(!this.isSystem);
        // added by @Loc Nguyen @ 24-04-2008
        txtDefault.setEnabled(!this.isSystem);
        enableOrDisableStable();
    }

        // added by @Loc Nguyen @ 20-03-2008
        // disables or enables the stable input part dependable of system and persistent status
        private void enableOrDisableStable() {

          if (isSystem || (!isPersistentText.isSelected())) {
            // stable part may not be enabled so disable it
            chkStable.setEnabled(false);
            cmbStable.setEnabled(false);
            txtStableExpr.setEnabled(false);
            // clear the values
            txtStableExpr.setText("");
            cmbStable.setSelectedIndex(-1);
            chkStable.setSelected(false);
            this.isStable = false;
          } else {
            chkStable.setEnabled(true);
//            cmbStable.setEnabled(true);
//            txtStableExpr.setEnabled(true);
          }

        }

        // sets or unsets the stable setting, enables or disables the controls that depend on this
        private void toggleIsStable()
        {
                isStable = !isStable;

                // enable or disable it all ...
                cmbStable.setEnabled(this.isStable);
                chkStable.setSelected(this.isStable);

                if (!isStable) {
                  // remove the stable values
                  txtStableExpr.setText("");
                  cmbStable.setSelectedIndex(-1);
                  txtStableExpr.setEnabled(false);
                }
        }

    // sets or unsets the casegroup setting, enables or disables the controls that depend on this
    private void toggleIsCaseGroup()
    {
        isCaseGroup = !isCaseGroup;

        // enable or disable it all ...
        chkCaseGroup.setSelected(this.isCaseGroup);
        txtDefaultCaseGroup.setEnabled(this.isCaseGroup);
        casevalueListItems.setEnabled(this.isCaseGroup);
        addCaseValueListButton.setEnabled(this.isCaseGroup);
        editCaseValueListButton.setEnabled(this.isCaseGroup);
        removeCaseValueListButton.setEnabled(this.isCaseGroup);

        if (isCaseGroup) {
            // create node if it doesn't exists
            if (!attr.hasGroupNode()) {
                attr.setCasegroup(new CaseGroup());
            }
        }
    }

    private void addGenerateList() {
        Action action = new Action();
        attr.getActions().add(action);
        generatelistItems.setListData(attr.getActions());
        Editor.showPanel(new GenerateListEditor(action, attr, parent));
    }

    private void editGenerateList()
    {
        if (generatelistItems.getSelectedIndex() != -1) {
            Action action = (Action)(attr.getActions().get(generatelistItems.getSelectedIndex()));
            Editor.showPanel(new GenerateListEditor(action, attr, parent));
        }
    }

    private void removeGenerateList()
    {
        if (generatelistItems.getSelectedIndex() != -1) {
            attr.getActions().remove(generatelistItems.getSelectedIndex());
            generatelistItems.setListData(attr.getActions());
        }
    }

    private void addCaseValueList() {
        Case tcase = new Case();
        attr.getCasegroup().getCaseValues().add(tcase);
        casevalueListItems.setListData(attr.getCasegroup().getCaseValues());
        Editor.showPanel(new CaseValueEditor(tcase, attr, parent));
    }

    private void editCaseValueList()
    {
        if (casevalueListItems.getSelectedIndex() != -1) {
            Case tcase = (Case)(attr.getCasegroup().getCaseValues().get(casevalueListItems.getSelectedIndex()));
            Editor.showPanel(new CaseValueEditor(tcase, attr, parent));
        }
    }

    private void removeCaseValueList()
    {
        if (casevalueListItems.getSelectedIndex() != -1) {
            attr.getCasegroup().getCaseValues().remove(casevalueListItems.getSelectedIndex());
            casevalueListItems.setListData(attr.getCasegroup().getCaseValues());
        }
    }

    void save()
    {
        String oldName = attr.getName();
        attr.setName(name.getText());
        attr.setDescription(description.getText());
        attr.setDefault(txtDefault.getText());
        if (type.getSelectedIndex() == 0) attr.setType(AttributeType.ATTRINT); else
        if (type.getSelectedIndex() == 1) attr.setType(AttributeType.ATTRSTR); else
        if (type.getSelectedIndex() == 2) attr.setType(AttributeType.ATTRBOOL);
        attr.setStable((String)cmbStable.getSelectedItem());
        attr.setStableExpression(txtStableExpr.getText());
        if (chkCaseGroup.isSelected()) {
            attr.getCasegroup().setDefaultFragment(txtDefaultCaseGroup.getText());
        } else {
            attr.setCasegroup(null);
        }
        attr.setPersistent(isPersistentText.isSelected());
        attr.setSystem(isSystem);
        attr.setReadonly(!isChangeableText.isSelected());
        if (!oldName.equals(attr.getName())) GenerateListData.replaceAttributeNames(
            parent.getName()+"."+oldName, parent.getName()+"."+attr.getName());

        // update activation graph
        GenerateListData.setGraphAttribute(parent.getName(), parent.getName()+"."+oldName, attr);

        isChanged = false;
    }
}
