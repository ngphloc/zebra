/*

    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 

*/
/**
 * GenerateListEditor.java 1.0, August 30, 2008
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
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

// Helpers
import javax.swing.DefaultListModel;
import java.awt.Dimension;

// event
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// XML and containers

class GenerateListEditor extends JProtectedPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GridBagLayout layout = new GridBagLayout();
    private JTextArea requirement = new JTextArea();
    private JList trueActions = new JList(new DefaultListModel());
    private JList falseActions = new JList(new DefaultListModel());
    private JCheckBox isPropagating = new JCheckBox("Propagating?");

    private Action action = null;
    private Attribute parent = null;
    private Concept concept = null;

    public GenerateListEditor(Action action, Attribute parent, Concept concept)
    {
        this.action = action;
        this.parent = parent;
        this.concept = concept;

        requirement.setText(action.getCondition());
        trueActions.setListData(action.getTrueStatements());
        falseActions.setListData(action.getFalseStatements());

        trueActions.setVisibleRowCount(5);
        falseActions.setVisibleRowCount(5);

        setLayout(layout);
        makeGui();
    }

    private void makeGui()
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        // make the name text field
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel nameText = new JLabel("If :");
                nameText.setFocusable(false);
        nameText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(nameText, constraints);
        this.add(nameText);

        // make the Description text field.
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel descriptionText = new JLabel("Then :");
                descriptionText.setFocusable(false);
        descriptionText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(descriptionText, constraints);
        this.add(descriptionText);

        // make the Requirement text field.
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 6;
        JLabel propertiesText = new JLabel("Else :");
                propertiesText.setFocusable(false);
        propertiesText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(propertiesText, constraints);
        this.add(propertiesText);

        // Adding the data fields
        constraints.weightx = 1.0;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.gridx = 1;
        constraints.gridy = 0;
        this.requirement.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.requirement.getDocument().addDocumentListener (new CheckExpr(this.requirement));
        this.requirement.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        layout.setConstraints(this.requirement, constraints);
        this.add(this.requirement);

        constraints.fill=GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        //constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.gridwidth = 1;
        constraints.gridx = 1;
        constraints.gridheight = 5;
        constraints.gridy = 1;
        this.trueActions.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.trueActions.setPreferredSize(new Dimension(200, 100));
        layout.setConstraints(this.trueActions, constraints);
        this.add(this.trueActions);

        constraints.gridx = 1;
        constraints.gridheight = 5;
        constraints.gridy = 6;
        this.falseActions.setPreferredSize(new Dimension(200, 100));
        this.falseActions.setBorder(new BevelBorder(BevelBorder.LOWERED));
        layout.setConstraints(this.falseActions, constraints);
        this.add(this.falseActions);
//
//      constraints.weightx = 1.0;
//      constraints.gridx = 1;
//      constraints.gridheight = 5;
//      constraints.gridy = 7;
//
//      generatelistItems.setPreferredSize(new Dimension(200, 100));
//      layout.setConstraints(this.generatelistItems, constraints);
//      this.add(this.generatelistItems);

        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                doOk(e);
            }
        });
        constraints.weightx = 0;
        constraints.gridx = 3;
        constraints.gridheight = 1;
        constraints.gridy = 0;
        layout.setConstraints(ok, constraints);
        this.add(ok);

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                doCancel(e);
            }
        });
        constraints.gridx = 3;
        constraints.gridheight = 1;
        constraints.gridy = 1;
        layout.setConstraints(cancel, constraints);
        this.add(cancel);

        constraints.gridheight = 1;
        JButton addTrueAction = new JButton("Add");
        constraints.weightx = 0;
        constraints.insets.top = 27;
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        addTrueAction.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addTrueAction();
            }
        });
        layout.setConstraints(addTrueAction, constraints);
        this.add(addTrueAction);

        JButton editTrueAction = new JButton("Edit");
        constraints.weightx = 0;
        constraints.insets.top = 0;
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        editTrueAction.addActionListener(new ActionListener ()
        {
            public void actionPerformed(ActionEvent e)
            {
                editTrueAction();
            }
        });
        layout.setConstraints(editTrueAction, constraints);
        this.add(editTrueAction);

        constraints.ipady = 0;
        constraints.weightx = 0;
        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        JButton removeTrueActionButton = new JButton("Remove");
        removeTrueActionButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                removeTrueAction();
            }
        });
        layout.setConstraints(removeTrueActionButton, constraints);
        this.add(removeTrueActionButton);

        constraints.gridheight = 1;
        JButton addFalseActionButton = new JButton("Add");
        constraints.weightx = 0;
        constraints.insets.top = 27;
        constraints.gridx = 2;
        constraints.gridy = 6;
        constraints.gridwidth = 1;
        addFalseActionButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addFalseAction();
            }
        });
        layout.setConstraints(addFalseActionButton, constraints);
        this.add(addFalseActionButton);

        JButton editFalseActionButton = new JButton("Edit");
        constraints.weightx = 0;
        constraints.insets.top = 0;
        constraints.gridx = 2;
        constraints.gridy = 7;
        constraints.gridwidth = 1;
        editFalseActionButton.addActionListener(new ActionListener ()
        {
            public void actionPerformed(ActionEvent e)
            {
                editFalseAction();
            }
        });
        layout.setConstraints(editFalseActionButton, constraints);
        this.add(editFalseActionButton);

        constraints.ipady = 0;
        constraints.weightx = 0;
        constraints.gridx = 2;
        constraints.gridy = 8;
        constraints.gridwidth = 1;
        JButton removeFalseActionButton = new JButton("Remove");

        removeFalseActionButton.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                removeFalseAction();
            }
        });
        layout.setConstraints(removeFalseActionButton, constraints);
        this.add(removeFalseActionButton);

        // Add the is propagating Check box.
        isPropagating.setSelected(action.getTrigger());
        constraints.weightx = 0.5;
        constraints.gridx = 1;
        constraints.gridheight = 1;
        constraints.gridy = 11;
        constraints.gridwidth = 1;
        isPropagating.addActionListener(new ActionListener ()
        {
            public void actionPerformed( ActionEvent ae)
            {
                setChanged();
            }
        });
        layout.setConstraints(isPropagating, constraints);
        this.add(isPropagating);
    }

    private void doOk(ActionEvent e)
    {
        save();
        this.isChanged = false;
        Editor.showAttribute(new AttributeEditor(parent, concept));
    }

    private void doCancel(ActionEvent e)
    {

        this.isChanged = false;
        Editor.showAttribute(new AttributeEditor(parent, concept));
    }

    private void addTrueAction()
    {
        Assignment assign = new Assignment("", "");
        action.getTrueStatements().add(assign);
        trueActions.setListData(action.getTrueStatements());
        Editor.showPanel(new ActionEditor(assign, action, parent, concept));
    }

    private void addFalseAction()
    {
        Assignment assign = new Assignment("", "");
        action.getFalseStatements().add(assign);
        falseActions.setListData(action.getFalseStatements());
        Editor.showPanel(new ActionEditor(assign, action, parent, concept));
    }

    private void removeTrueAction()
    {
        if ((action.getTrueStatements().size() > 1) && (trueActions.getSelectedIndex() != -1)) {
            action.getTrueStatements().remove(trueActions.getSelectedIndex());
            trueActions.setListData(action.getTrueStatements());
        } else {
            JOptionPane.showMessageDialog(null, "Cant Remove Action.\nThere must be at least 1 True Action.");
        }
    }

    private void removeFalseAction()
    {
        if ((action.getFalseStatements().size() > 0) && (falseActions.getSelectedIndex() != -1)) {
            action.getFalseStatements().remove(falseActions.getSelectedIndex());
            falseActions.setListData(action.getFalseStatements());
        }
    }

    private void editTrueAction()
    {
        if (trueActions.getSelectedIndex() != -1) {
            Assignment assign = (Assignment)(action.getTrueStatements().get(trueActions.getSelectedIndex()));
            Editor.showPanel(new ActionEditor(assign, action, parent, concept));
        }
    }

    private void editFalseAction()
    {
        if (falseActions.getSelectedIndex() != -1) {
            Assignment assign = (Assignment)(action.getFalseStatements().get(falseActions.getSelectedIndex()));
            Editor.showPanel(new ActionEditor(assign, action, parent, concept));
        }
    }

    void save()
    {
        action.setCondition(requirement.getText());
        action.setTrigger(isPropagating.isSelected());

        // update activation graph
        GenerateListData.setGraphAttribute(concept.getName(), null, parent);
    }
}