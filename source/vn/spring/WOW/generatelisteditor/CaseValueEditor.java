/*

    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 

*/
/**
 * CaseValueEditor.java 1.0, August 30, 2008
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

// components
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

// event
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CaseValueEditor extends JProtectedPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GridBagLayout layout = new GridBagLayout();
    private JTextField txtCaseValue = new JTextField();
    private JTextField txtReturnFragment = new JTextField();

    private Case tcase = null;
    private Attribute parent = null;
    private Concept concept = null;

    public CaseValueEditor(Case tcase, Attribute parent, Concept concept)
    {
        this.tcase = tcase;
        this.parent = parent;
        this.concept = concept;

        this.txtCaseValue.setText(tcase.getValue());
        this.txtReturnFragment.setText(tcase.getReturnfragment());

        this.setLayout(layout);
        this.makeGui();
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
        JLabel caseValueText = new JLabel("CaseValue :");
//        caseValueText.setFocusable(false);
//        caseValueText.setBorder(null);
//        caseValueText.setEditable(false);
        caseValueText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(caseValueText, constraints);
        this.add(caseValueText);

        // make the Description text field.
        constraints.weightx = 0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel returnFragmentText = new JLabel("Returnfragment :");
//        returnFragmentText.setFocusable(false);
//        returnFragmentText.setBorder(null);
//        returnFragmentText.setEditable(false);
        returnFragmentText.setHorizontalAlignment(JTextField.RIGHT);
        layout.setConstraints(returnFragmentText, constraints);
        this.add(returnFragmentText);

        constraints.weightx = 1.0;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.gridx = 1;
        constraints.gridy = 0;
        this.txtCaseValue.setEnabled(true);
        this.txtCaseValue.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        layout.setConstraints(this.txtCaseValue, constraints);
        this.add(this.txtCaseValue);

        constraints.weightx = 1.0;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.gridx = 1;
        constraints.gridy = 1;
        this.txtReturnFragment.setEnabled(true);
        this.txtReturnFragment.getDocument().addDocumentListener(new ChangeDocumentListener(this));
        layout.setConstraints(this.txtReturnFragment, constraints);
        JButton ok = new JButton("OK");
        this.add(this.txtReturnFragment);

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

    void save()
    {
        tcase.setValue(txtCaseValue.getText());
        tcase.setReturnfragment(txtReturnFragment.getText());
    }
}

