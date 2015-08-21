/*

    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 

*/
/**
 * ActionEditor.java 1.0, August 30, 2008
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
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

// event
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ActionEditor extends JProtectedPanel
{
	private static final long serialVersionUID = 1L;

	GridBagLayout layout = new GridBagLayout();
    private JTextField conceptName = new JTextField();
    private JTextField attributeName = new JTextField();
    private JTextField expression = new JTextField();

    private Assignment assign = null;
    private Action parent = null;
    private Concept concept = null;
    private Attribute attribute = null;

    public ActionEditor(Assignment assignment, Action parent, Attribute attribute, Concept concept)
    {
        assign = assignment;
        this.parent = parent;
        this.concept = concept;
        this.attribute = attribute;

        // set the values of the textfields
        DotString ds = new DotString(assign.getVariable());
        attributeName.setText(ds.get(ds.size()-1));
        ds.set(ds.size()-1, null);
        conceptName.setText(ds.toString());
        expression.setText(assign.getExpression());

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

        // make the name text field
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel nameText = new JLabel("conceptName :");
//        nameText.setBorder(null);
//        nameText.setEditable(false);
//        nameText.setFocusable(false);
        nameText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(nameText, constraints);
        this.add(nameText);

        // make the Description text field.
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel descriptionText = new JLabel("attributeName :");
//        descriptionText.setBorder(null);
//        descriptionText.setEditable(false);
//        descriptionText.setFocusable(false);
        descriptionText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(descriptionText, constraints);
        this.add(descriptionText);

        // make the Requirement text field.
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 2;
        JLabel propertiesText = new JLabel("expression :");
//        propertiesText.setBorder(null);
//        propertiesText.setEditable(false);
//        propertiesText.setFocusable(false);
        propertiesText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(propertiesText, constraints);
        this.add(propertiesText);

        // Adding the data fields
        constraints.weightx = 1.0;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.gridx = 1;
        constraints.gridy = 0;
        conceptName.setBorder(new BevelBorder(BevelBorder.LOWERED));
        conceptName.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        layout.setConstraints(conceptName, constraints);
        this.add(conceptName);

        constraints.fill=GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.gridx = 1;
        constraints.gridheight = 1;
        constraints.gridy = 1;
        attributeName.setBorder(new BevelBorder(BevelBorder.LOWERED));
        attributeName.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        layout.setConstraints(attributeName, constraints);
        this.add(attributeName);

        expression.getDocument().addDocumentListener (new CheckExpr(this.expression));
        constraints.gridx = 1;
        constraints.gridheight = 1;
        constraints.gridy = 2;
        expression.setBorder(new BevelBorder(BevelBorder.LOWERED));
        expression.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        layout.setConstraints(expression, constraints);
        this.add(expression);

        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                doOk(e);
            }
        });
        constraints.weightx = 0;
        constraints.gridx = 2;
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
        constraints.gridx = 2;
        constraints.gridheight = 1;
        constraints.gridy = 1;
        layout.setConstraints(cancel, constraints);
        this.add(cancel);
    }


    private void doOk(ActionEvent e)
    {
        save();
        Editor.showPanel(new GenerateListEditor(parent, attribute, concept));
    }

    private void doCancel(ActionEvent e)
    {
        isChanged = false;
        Editor.showPanel(new GenerateListEditor(parent, attribute, concept));
    }

    void save()
    {
        assign.setVariable(conceptName.getText()+"."+attributeName.getText());
        assign.setExpression(expression.getText());

        // update activation graph
        GenerateListData.setGraphAttribute(concept.getName(), null, attribute);

        isChanged = false;
    }
}