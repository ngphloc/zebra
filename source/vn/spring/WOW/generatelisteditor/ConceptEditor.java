/*

    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 

*/
/**
 * ConceptEditor.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Rserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

// changed by @Loc Nguyen @ 15-03-2008 Added Tree updated when a concept is removed

package vn.spring.WOW.generatelisteditor;

import vn.spring.WOW.datacomponents.*;

// layout
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

// UI
import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

// events
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// helper
import java.awt.Dimension;
import java.net.URL;
import java.net.MalformedURLException;

class ConceptEditor extends JProtectedPanel
{
	private static final long serialVersionUID = 1L;

    // UI variables
    GridBagLayout layout = new GridBagLayout();
    private JTextField name = new JTextField();
    private JTextArea description = new JTextArea();
    private JTextField resource = new JTextField();
    private JTextField concepttype = new JTextField();
    private JTextField title = new JTextField();
    private JList attribute = new JList(new DefaultListModel());

    private JCheckBox chkHierarchy = new JCheckBox();
    private JTextField firstchild = new JTextField();
    private JTextField nextsib = new JTextField();
    private JTextField parent = new JTextField();

    // The concept to edit
    private Concept concept = null;

    public ConceptEditor(Concept pconcept)
    {
        concept = pconcept;

        loadData();

        this.setLayout(layout);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        JTextField nameText = new JTextField("Name :");
        JTextField descriptionText = new JTextField("Description :");
        JTextField resourceText = new JTextField("Resource :");
        JTextField concepttypeText = new JTextField("Concept Type :");
        JTextField titleText = new JTextField("Title :");
        JTextField hierarchyText = new JTextField("Hierarchy :");
        JTextField attributeText = new JTextField("Attribute :");

        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        nameText.setFocusable(false);
        nameText.setBorder(null);
        nameText.setEditable(false);
        nameText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(nameText, constraints);
        this.add(nameText);

        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        resourceText.setFocusable(false);
        resourceText.setBorder(null);
        resourceText.setEditable(false);
        resourceText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(resourceText, constraints);
        this.add(resourceText);

        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 2;
        descriptionText.setFocusable(false);
        descriptionText.setBorder(null);
        descriptionText.setEditable(false);
        descriptionText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(descriptionText, constraints);
        this.add(descriptionText);

        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 5;
        concepttypeText.setFocusable(false);
        concepttypeText.setBorder(null);
        concepttypeText.setEditable(false);
        concepttypeText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(concepttypeText, constraints);
        this.add(concepttypeText);

        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 7;
        titleText.setFocusable(false);
        titleText.setBorder(null);
        titleText.setEditable(false);
        titleText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(titleText, constraints);
        this.add(titleText);

        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 9;
        hierarchyText.setFocusable(false);
        hierarchyText.setBorder(null);
        hierarchyText.setEditable(false);
        hierarchyText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(hierarchyText, constraints);
        this.add(hierarchyText);

        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 14;
        attributeText.setFocusable(false);
        attributeText.setBorder(null);
        attributeText.setEditable(false);
        attributeText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(attributeText, constraints);
        this.add(attributeText);

        constraints.weightx = 1.0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridx = 1;
        constraints.gridy = 0;
        this.name.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        layout.setConstraints(this.name, constraints);
        this.add(this.name);

        constraints.gridx = 1;
        constraints.gridheight = 3;
        constraints.gridy = 1;
        resource.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        layout.setConstraints(resource, constraints);
        this.add(resource);

        constraints.gridx = 1;
        constraints.gridheight = 3;
        constraints.gridy = 2;
        this.description.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        JScrollPane descriptionHolder = new JScrollPane(this.description);
        descriptionHolder.setPreferredSize(new Dimension(300, 60));
        layout.setConstraints(descriptionHolder, constraints);
        this.add(descriptionHolder);

        constraints.gridx = 1;
        constraints.gridheight = 1;
        constraints.gridy = 5;
        this.concepttype.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        layout.setConstraints(this.concepttype, constraints);
        this.add(this.concepttype);

        constraints.gridx = 1;
        constraints.gridheight = 1;
        constraints.gridy = 7;
        this.title.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        layout.setConstraints(this.title, constraints);
        this.add(this.title);

        chkHierarchy.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setChanged();
                toggleHierarchy();
            }
        });
        constraints.weightx = 0;
        constraints.gridx = 1;
        constraints.gridheight = 1;
        constraints.gridy = 9;
        constraints.gridwidth = 1;
        layout.setConstraints(chkHierarchy, constraints);
        this.add(chkHierarchy);

        // add hierarchy panel
        constraints.gridx = 1;
        constraints.gridheight = 3;
        constraints.gridy = 10;
        JPanel hierarchyPanel = makeHierarchyPanel();
        hierarchyPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        layout.setConstraints(hierarchyPanel, constraints);
        this.add(hierarchyPanel);

        constraints.weightx = 0;
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        constraints.fill=GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridheight = 1;
        constraints.gridy = 13;
        JButton commitChangesButton = new JButton("Commit Changes");
        commitChangesButton.addActionListener(new ActionListener()
        {
            public void actionPerformed( ActionEvent ae)
            {
                save();
            }

        });
        layout.setConstraints(commitChangesButton, constraints);
        this.add(commitChangesButton);

        constraints.fill=GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1.0;
        constraints.gridx = 1;
        constraints.gridheight = 5;
        constraints.gridy = 14;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        attribute.setListData(concept.getAttributes());
        attribute.setVisibleRowCount(5);
        JScrollPane scrollPane = new JScrollPane(attribute);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        layout.setConstraints(scrollPane, constraints);
        this.add(scrollPane);

        constraints.gridheight = 1;
        constraints.weightx = 0;
        constraints.insets.top = 27;
        constraints.gridx = 2;
        constraints.gridy = 14;
        constraints.gridwidth = 1;
        JButton addAttributeButton = new JButton("Add");
        addAttributeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addAttribute();
            }
        });
        layout.setConstraints(addAttributeButton, constraints);
        this.add(addAttributeButton);

        constraints.weightx = 0;
        constraints.insets.top = 0;
        constraints.gridx = 2;
        constraints.gridy = 15;
        constraints.gridwidth = 1;
        JButton editAttributeButton = new JButton("Edit");
        editAttributeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                editAttribute(e);
            }
        });
        layout.setConstraints(editAttributeButton, constraints);
        this.add(editAttributeButton);

        constraints.ipady = 0;
        constraints.insets.top = 0;
        constraints.weightx = 0;
        constraints.gridx = 2;
        constraints.gridy = 16;
        constraints.gridwidth = 1;
        JButton removeAttributeButton = new JButton("Remove");
        removeAttributeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                removeAttribute(e);
            }
        });
        layout.setConstraints(removeAttributeButton, constraints);
        this.add(removeAttributeButton);
    }

    private JPanel makeHierarchyPanel()
    {
        JPanel hierarchyPanel = new JPanel();
        GridBagLayout hierarchyLayout = new GridBagLayout();

        hierarchyPanel.setLayout(hierarchyLayout);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        JTextField txtFirstchild = new JTextField("First child :");
        txtFirstchild.setFocusable(false);
        txtFirstchild.setBorder(null);
        txtFirstchild.setEditable(false);
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridheight = 1;
        constraints.gridy = 0;
        hierarchyLayout.setConstraints(txtFirstchild, constraints);
        hierarchyPanel.add(txtFirstchild);

        JTextField txtNextsib = new JTextField("Next sibling :");
        txtNextsib.setFocusable(false);
        txtNextsib.setBorder(null);
        txtNextsib.setEditable(false);
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        hierarchyLayout.setConstraints(txtNextsib, constraints);
        hierarchyPanel.add(txtNextsib);

        JTextField txtParent = new JTextField("Parent :");
        txtParent.setFocusable(false);
        txtParent.setBorder(null);
        txtParent.setEditable(false);
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 2;
        hierarchyLayout.setConstraints(txtParent, constraints);
        hierarchyPanel.add(txtParent);

        constraints.weightx = 1.0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridx = 1;
        constraints.gridy = 0;
        firstchild.setEnabled(chkHierarchy.getSelectedObjects()!=null);
        firstchild.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        hierarchyLayout.setConstraints(firstchild, constraints);
        hierarchyPanel.add(firstchild);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridx = 1;
        constraints.gridy = 1;
        nextsib.setEnabled(chkHierarchy.getSelectedObjects()!=null);
        nextsib.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        hierarchyLayout.setConstraints(nextsib, constraints);
        hierarchyPanel.add(nextsib);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridx = 1;
        constraints.gridy = 2;
        parent.setEnabled(chkHierarchy.getSelectedObjects()!=null);
        parent.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        hierarchyLayout.setConstraints(parent, constraints);
        hierarchyPanel.add(parent);

        return hierarchyPanel;
    }

    private void toggleHierarchy()
    {
        if (chkHierarchy.getSelectedObjects()!=null) {
            ConceptHierStruct hier = new ConceptHierStruct();
            hier.firstchild = firstchild.getText();
            hier.nextsib = nextsib.getText();
            hier.parent = parent.getText();
            concept.setHierStruct(hier);
        } else {
            concept.setHierStruct(null);
        }
        firstchild.setEnabled(chkHierarchy.getSelectedObjects()!=null);
        nextsib.setEnabled(chkHierarchy.getSelectedObjects()!=null);
        parent.setEnabled(chkHierarchy.getSelectedObjects()!=null);
    }

    private void loadData()
    {
        name.setText(concept.getName());
        description.setText(concept.getDescription());
        concepttype.setText(concept.getType());
        title.setText(concept.getTitle());
        chkHierarchy.setSelected(concept.getHierStruct()!=null);
        if (chkHierarchy.getSelectedObjects()!=null) {
            firstchild.setText(concept.getHierStruct().firstchild);
            nextsib.setText(concept.getHierStruct().nextsib);
            parent.setText(concept.getHierStruct().parent);
        }
        if (concept.getResourceURL() == null) {
            resource.setText("");
        } else {
            resource.setText(concept.getResourceURL().toString());
        }
    }

    private void addAttribute()
    {
        String attrname = "attribute";
        attrname = JOptionPane.showInputDialog("Give a name for a new Attribute: ", attrname);
        if (attrname == null || "".equals(attrname))
            return; // empty name or cancel pressed
        if (GenerateListData.containsAttribute(concept.getName(), attrname)) {
            JOptionPane.showMessageDialog(null,"This attribute already exists","Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Attribute newAttribute = new Attribute(attrname, AttributeType.ATTRINT);
        concept.getAttributes().add(newAttribute);
        Editor.tree.updateUI();
        Editor.showAttribute(new AttributeEditor(newAttribute, concept));
    }

    private void editAttribute(ActionEvent e)
    {
        Attribute selectedAttribute = (Attribute)concept.getAttributes().get(attribute.getSelectedIndex());
        Editor.showAttribute(new AttributeEditor(selectedAttribute, concept));
    }

    private void removeAttribute(ActionEvent e)
    {
        concept.getAttributes().remove(attribute.getSelectedIndex());
        attribute.setListData(concept.getAttributes());
        Editor.tree.updateUI();
    }

    void save()
    {
        String oldName = concept.getName();
        concept.setName(name.getText());
        concept.setDescription(description.getText());
        concept.setType(concepttype.getText());
        concept.setTitle(title.getText());
        if (chkHierarchy.getSelectedObjects()!=null) {
            concept.getHierStruct().firstchild = firstchild.getText();
            concept.getHierStruct().nextsib = nextsib.getText();
            concept.getHierStruct().parent = parent.getText();
        }
        try {concept.setResourceURL(new URL(resource.getText()));} catch (MalformedURLException e) {}
        if (!oldName.equals(concept.getName())) GenerateListData.replaceConceptNames(oldName, concept.getName());
        isChanged = false;
        Editor.tree.treeDidChange();
    }
}
