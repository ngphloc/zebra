/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AttributeEditor.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.graphauthor.GraphAuthor;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;
import vn.spring.WOW.graphauthor.author.*;

/**
 * Dialog for adding and editing an attribute of a concept
 *
 */

public class AttributeEditor {
  public JFrame parentFrame;
  private JDialog frame;
  private JPanel jPanel1 = new JPanel();
  private JPanel namePanel = new JPanel();
  private JPanel propertiesPanel = new JPanel();
  private JPanel stablePanel = new JPanel();
  private JLabel lblTitle = new JLabel("Attribute Properties");
  private JTextField txtName = new JTextField();
  private JTextField txtDesc = new JTextField();
  private JLabel lblName = new JLabel("Name");
  private JLabel lblDesc = new JLabel("Description");
  private JLabel lblType = new JLabel("Type");
  //private CRTSetDefault setDefaultList = new CRTSetDefault();
  private JLabel lblDefault = new JLabel("Default value");
  private JTextField txtDefault = new JTextField();

  private String[] typeList= { "int", "string", "bool" };
  private JComboBox cmbType = new JComboBox(typeList);

  private JCheckBox chkSystem = new JCheckBox("System");
  private JCheckBox chkPersistent = new JCheckBox("Persistent");
  private JCheckBox chkChangeable = new JCheckBox("Changeable");
  private JCheckBox chkStable = new JCheckBox("Enable Stability");
  private JLabel lblStable = new JLabel("Stable");
  private JLabel lblExpression = new JLabel("Expression");

  private String[] stableList = { "always", "session", "freeze" };
  private JComboBox cmbStable = new JComboBox(stableList);
  private JTextField txtExpression = new JTextField();

  private JButton btnOK = new JButton("OK");
  private JButton btnCancel = new JButton("Cancel");

  private boolean isStable = false;
  private boolean isSystem;
  private boolean isPersistent;
  private boolean isChangeable;
  private boolean NameChangeable;
  private WOWOutAttribute attribute = null;
  private WOWOutConcept concept = null;

  public AttributeEditor(JFrame parentFrame, WOWOutConcept conceptParam, WOWOutAttribute attributeParam, boolean add) {
    this.attribute  = attributeParam;
    this.concept = conceptParam;
    this.parentFrame = parentFrame;
    this.NameChangeable = add;

    isSystem = attribute.isSystem.booleanValue();
    isPersistent = attribute.isPersistent.booleanValue();
    isChangeable = attribute.isChangeable.booleanValue();

    try {
      // init frame
      txtName.setText(attribute.name);
      txtDesc.setText(attribute.description);
      txtDefault.setText(attribute.setDefaultList.setdefault);
      cmbType.setSelectedItem(attribute.type);

      // initialize the stable values
      enableOrDisableStable();
      if (chkStable.isEnabled()) {
        String stable = attribute.stable;
        if (!attribute.stable.equals("")) {
          isStable = true;
          cmbStable.setSelectedItem(stable);
          if (stable.equals("freeze")) {
            txtExpression.setText(attribute.stable_expr);
            txtExpression.setEnabled(true);
          } else {
            txtExpression.setEnabled(false);
          }
        } else {
          isStable = false;
          cmbStable.setEnabled(false);
          txtExpression.setEnabled(false);
          txtExpression.setText("");
          cmbStable.setSelectedIndex( -1);
        }

      }


      txtExpression.setText(attribute.stable_expr);

      // create frame
      jbInit();

      frame = new JDialog(parentFrame, "Attribute Editor", true);
      frame.setSize(580, 450);
      frame.setLocation(100, 100);
      frame.getContentPane().add(jPanel1);
      frame.getRootPane().setDefaultButton(btnOK);
    } catch (Exception e) {
        e.printStackTrace();
    }
  }

  public void show() {
      frame.setVisible(true);
      //Loc Nguyen added to correct warning
  }

  private void jbInit() throws Exception {
      jPanel1.setLayout(null);
      jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));

      namePanel.setLayout(null);
      namePanel.setBounds(new Rectangle(20, 42, 540, 70));
      namePanel.setBorder(BorderFactory.createLoweredBevelBorder());
      jPanel1.add(namePanel);

      propertiesPanel.setLayout(null);
      propertiesPanel.setBounds(new Rectangle(20, 120, 540, 110));
      propertiesPanel.setBorder(BorderFactory.createLoweredBevelBorder());
      jPanel1.add(propertiesPanel);

      stablePanel.setLayout(null);
      stablePanel.setBounds(new Rectangle(20, 270, 540, 70));
      stablePanel.setBorder(BorderFactory.createLoweredBevelBorder());
      jPanel1.add(stablePanel);

      lblTitle.setBounds(new Rectangle(227, 9, 148, 27));
      lblTitle.setFont(new java.awt.Font("Dialog", 1, 16));
      jPanel1.add(lblTitle);

      // name panel
      lblName.setBounds(new Rectangle(20, 5, 130, 27));
      namePanel.add(lblName);
      lblDesc.setBounds(new Rectangle(20, 40, 130, 27));
      namePanel.add(lblDesc);
      txtName.setBounds(new Rectangle(160, 5, 370, 27));
      if (!NameChangeable) {
        txtName.setEnabled(false);
        txtName.setEditable(false);
      }
      namePanel.add(txtName);
      txtDesc.setBounds(new Rectangle(160, 40, 370, 27));
      namePanel.add(txtDesc);

      // properties panel
      lblType.setBounds(new Rectangle(20, 5, 130, 27));
      propertiesPanel.add(lblType);
      cmbType.setBounds(new Rectangle(160, 5, 370, 27));
      propertiesPanel.add(cmbType);

      lblDefault.setBounds(new Rectangle(20, 40, 130, 27));
      propertiesPanel.add(lblDefault);
      txtDefault.setBounds(new Rectangle(160, 40, 370, 27));
      propertiesPanel.add(txtDefault);

      chkSystem.setBounds(new Rectangle(20, 75, 130, 27));
      chkSystem.setSelected(isSystem);
      if (isSystem) {
        chkPersistent.setEnabled(false);
        chkChangeable.setEnabled(false);
        cmbType.setEnabled(false);
        txtName.setEnabled(false);
        txtDesc.setEnabled(false);
        txtDefault.setEnabled(false);
        chkStable.setEnabled(false);
        cmbStable.setEnabled(false);
        txtExpression.setEnabled(false);
        txtExpression.setText("");
        cmbStable.setSelectedIndex(-1);
        chkStable.setSelected(false);
      }
      chkSystem.addActionListener(new ActionListener()
      {
              public void actionPerformed(ActionEvent e)
              {
                      toggleIsSystem();
              }
      });
      propertiesPanel.add(chkSystem);

      chkPersistent.setBounds(new Rectangle(160, 75, 130, 27));
      chkPersistent.setSelected(isPersistent);

      chkPersistent.addActionListener(new ActionListener()
      {
              public void actionPerformed(ActionEvent e)
              {
                isPersistent = chkPersistent.isSelected();
                enableOrDisableStable();
              }
      });
      propertiesPanel.add(chkPersistent);

      chkChangeable.setBounds(new Rectangle(300, 75, 130, 27));
      chkChangeable.setSelected(isChangeable);

      chkChangeable.addActionListener(new ActionListener()
      {
              public void actionPerformed(ActionEvent e)
              {
                isChangeable = chkChangeable.isSelected();
              }
      });
      propertiesPanel.add(chkChangeable);

      chkStable.setBounds(new Rectangle(20, 240, 130, 27));
      chkStable.setSelected(isStable);

      chkStable.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          toggleStable();
        }
      });
      jPanel1.add(chkStable);

      // stable panel
      lblStable.setBounds(new Rectangle(20, 5, 130, 27));
      stablePanel.add(lblStable);
      lblExpression.setBounds(new Rectangle(20, 40, 130, 27));
      stablePanel.add(lblExpression);
      cmbStable.setBounds(new Rectangle(160, 5, 370, 27));

      cmbStable.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          String selectedItem = (String) cmbStable.getSelectedItem();
          if (selectedItem != null) {
            if (selectedItem.equals("freeze")) {
              txtExpression.setEnabled(true);
            }
            else {
              txtExpression.setEnabled(false);
            }
          }
        }
      });
      stablePanel.add(cmbStable);
      txtExpression.setBounds(new Rectangle(160, 40, 370, 27));
      stablePanel.add(txtExpression);

      // ok and cancel buttons
      btnOK.setBounds(new Rectangle(20, 350, 120, 27));
      btnOK.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          // clicked OK
          if (cmbType.getSelectedIndex() == -1) {
            JOptionPane.showConfirmDialog((Component) null,
                                                       "Invalid type! \n You must select a type.",
                                                       "alert",
                                                       JOptionPane.PLAIN_MESSAGE);
            return;
          }

          if (NameChangeable) {
            if (checkName()) {
              save();
              frame.setVisible(false);
              frame.dispose();
              frame = null;
            } else {
              // messagebox, name exists already
              JOptionPane.showConfirmDialog((Component) null,
                                                         "Invalid attribute name! \n This attribute name does already exists, choose another name.",
                                                         "alert",
                                                         JOptionPane.PLAIN_MESSAGE);
              return;
            }
          } else {
            save();
            frame.setVisible(false);
            frame.dispose();
            frame = null;
          }
        }
      });
      jPanel1.add(btnOK);

      btnCancel.setBounds(new Rectangle(440, 350, 120, 27));
      btnCancel.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          // clicked cancel
          frame.setVisible(false);
          frame.dispose();
          frame = null;
        }
      });

      jPanel1.add(btnCancel);
  }

  /**
   *
   */
  private void toggleIsSystem() {
    isSystem = !isSystem;

    // enable it all ...
    chkPersistent.setEnabled(!this.isSystem);
    chkChangeable.setEnabled(!this.isSystem);
    cmbType.setEnabled(!this.isSystem);
    txtName.setEnabled(!this.isSystem);
    txtDesc.setEnabled(!this.isSystem);
    txtDefault.setEnabled(!this.isSystem);
    enableOrDisableStable();
  }

  /**
   *
   */
  private void toggleStable() {
    isStable = !isStable;

    // enable or disable it all ...
    cmbStable.setEnabled(this.isStable);
    chkStable.setSelected(this.isStable);

    if (!isStable) {
      // remove the stable values
      txtExpression.setText("");
      cmbStable.setSelectedIndex(-1);
      txtExpression.setEnabled(false);
    }
  }

  // added by @Loc Nguyen @ 20-03-2008
  // disables or enables the stable input part dependable of system and persistent status
  private void enableOrDisableStable() {
   if (isSystem || (!isPersistent)) {
      // stable part may not be enabled so disable it
      chkStable.setEnabled(false);
      cmbStable.setEnabled(false);
      txtExpression.setEnabled(false);
      // clear the values
      txtExpression.setText("");
      cmbStable.setSelectedIndex(-1);
      chkStable.setSelected(false);
      this.isStable = false;
    } else {
      chkStable.setEnabled(true);
    }
  }

  private void save()
  {
    // store the name, description, requirement, type,
    // isChangeable, isSystem and isPersistent

    if (NameChangeable) {
      this.attribute.name = txtName.getText();
    }
    this.attribute.description = txtDesc.getText();
    if (this.attribute.description == null) {
      this.attribute.description= "";
    }
    this.attribute.type = (String) cmbType.getSelectedItem();
    this.attribute.setDefaultList.setdefault = txtDefault.getText();
    if (this.attribute.setDefaultList.setdefault == null) {
      this.attribute.setDefaultList.setdefault = "";
    }
    this.attribute.isSystem = Boolean.valueOf(this.isSystem);
    this.attribute.isPersistent = Boolean.valueOf(this.isPersistent);
    this.attribute.isChangeable = Boolean.valueOf(this.isChangeable);
    if (isStable && chkStable.isSelected() ) {
      String stableSelection = null;
      stableSelection = (String) cmbStable.getSelectedItem();
      if (stableSelection != null) {
        this.attribute.stable = stableSelection;
        if (stableSelection.equals("freeze")) {
            this.attribute.stable_expr = txtExpression.getText();
        } else {
          this.attribute.stable_expr = "";
        }
      } else {
        this.attribute.stable = "";
        this.attribute.stable_expr = "";
      }
    } else {
      this.attribute.stable = "";
      this.attribute.stable_expr = "";
    }
  }

  /**
   * Checks the given attribute name if that name does already exists, returns true if not, else false.
   */
  private boolean checkName() {
    // loops all the attributes and compares these names with the entered name
    for (Iterator i = concept.attributeList.iterator(); i.hasNext();) {
      WOWOutAttribute tmpAttribute = (WOWOutAttribute) i.next();
      if (tmpAttribute.name.equals(txtName.getText())) {
        return false;
      }
    }
    return true;
  }
}