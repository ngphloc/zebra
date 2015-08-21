/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ResourcesEditor.java 1.0, June 1, 2008
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
import java.util.Vector;
import java.util.Iterator;
import javax.swing.*;
import vn.spring.WOW.graphauthor.author.*;
import javax.swing.DefaultListModel;
import java.util.HashMap;

/**
 * Dialog for adding and editing the assigned resources of a concept
 *
 */

public class ResourcesEditor {
  private WOWOutConcept concept;
  private HashMap resourceMap = new HashMap();
  private boolean result;
  private boolean editMode = false;
  private Case editCase = null;

    private JDialog frame;
    private JPanel jPanel1 = new JPanel();
    private JPanel jPanel2 = new JPanel();

    private JLabel lblFrameTitle = new JLabel("Resources");
    private JButton btnOk = new JButton("OK");

    private JLabel lblDefault = new JLabel("Default resource");
    private JTextField txtDefault = new JTextField();

    private JPanel listPanel = new JPanel();
    private JButton btnAdd = new JButton("Add");
    private JButton btnEdit = new JButton("Edit");
    private JButton btnRemove = new JButton("Remove");
    private JList lstResource = new JList(new DefaultListModel());
    private JLabel lblResources = new JLabel("Resources");

    private JPanel addPanel = new JPanel();
    private JLabel lblExpr = new JLabel("Expression");
    private JLabel lblResource = new JLabel("Resource");
    private JTextField txtExpr = new JTextField();
    private JTextField txtResource = new JTextField();
    private JButton btnAddOk = new JButton("OK");
    private JButton btnAddCancel = new JButton("Cancel");

    public  JFrame parentFrame;

    public ResourcesEditor(JFrame parentFrame, WOWOutConcept conceptParam) {
      this.concept  = conceptParam;
      this.parentFrame = parentFrame;
        try {
          // init frame
          result = initFrame();

          if (!result) {
            // messagebox, name exists already
            JOptionPane.showConfirmDialog((Component) null,
                                                       "Showability attribute not found! \n Check the template or add this attribute yourself.",
                                                       "alert",
                                                       JOptionPane.PLAIN_MESSAGE);
            return;
          }

          // fill the list with attributes
          DefaultListModel caseValueHolder = (DefaultListModel)this.lstResource.getModel();
          caseValueHolder = getResourceListItems(caseValueHolder);
          this.lstResource.setVisibleRowCount(5);

          jbInit();
          frame = new JDialog(parentFrame, "Resources", true);
//          frame.setSize(635, 340);
          frame.setSize(635, 500);//435
          frame.setLocation(100, 100);
          frame.getContentPane().add(jPanel1);
          frame.getRootPane().setDefaultButton(btnOk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
      if (frame != null) {
        frame.setVisible(true);
      } else {
        // something went wrong
        System.out.println("ResourcesEditor: show: initialisation of the frame failed so the dialog could not be shown");
      }
    }

    private void jbInit() throws Exception {

        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
        jPanel1.setMinimumSize(new Dimension(600, 800));

        lblFrameTitle.setFont(new java.awt.Font("Dialog", 1, 16));
        lblFrameTitle.setBounds(new Rectangle(250, 10, 148, 27));
        jPanel1.add(lblFrameTitle);

        jPanel2.setLayout(null);
        jPanel2.setBounds(new Rectangle(20, 40, 595, 340));
//        jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
        jPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
        jPanel2.setMinimumSize(new Dimension(400, 400));
        jPanel1.add(jPanel2);

        // default fragment
        lblDefault.setBounds(new Rectangle(20, 5, 130, 27));
        jPanel2.add(lblDefault);

        txtDefault.setBounds(new Rectangle(160, 5, 415, 27));
        jPanel2.add(txtDefault);

        // list panel with listbox and 3 buttons
        listPanel.setLayout(null);
        listPanel.setBounds(new Rectangle(20, 40, 555, 200));
        listPanel.setBorder(BorderFactory.createLoweredBevelBorder());
//        listPanel.setMinimumSize(new Dimension(400, 400));
        jPanel2.add(listPanel);

        lblResources.setBounds(new Rectangle(20, 10, 150, 18));
        listPanel.add(lblResources);

        // attribute listbox
//        lstResource.setBounds(new Rectangle(20, 35, 370, 155));
        JScrollPane resourceHolder = new JScrollPane(this.lstResource);
        resourceHolder.setBounds(new Rectangle(20, 35, 370, 155));
//        attributeHolder.setPreferredSize(new Dimension(300, 40));
        listPanel.add(resourceHolder);

        btnAdd.setBounds(new Rectangle(410, 35, 125, 25));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnAdd_actionPerformed(e);
            }
        });
        listPanel.add(btnAdd);

        btnEdit.setBounds(new Rectangle(410, 100, 125, 25));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnEdit_actionPerformed(e);
            }
        });
        listPanel.add(btnEdit);

        btnRemove.setBounds(new Rectangle(410, 165, 125, 25));
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnRemove_actionPerformed(e);
            }
        });
        listPanel.add(btnRemove);

        // add panel with 2 labels, 2 textfields and two buttons
        addPanel.setLayout(null);
        addPanel.setBounds(new Rectangle(20, 250, 555, 80));
        addPanel.setBorder(BorderFactory.createLoweredBevelBorder());
//        addPanel.setMinimumSize(new Dimension(400, 400));
        jPanel2.add(addPanel);

        lblExpr.setBounds(new Rectangle(20, 10, 110, 27));
        addPanel.add(lblExpr);

        txtExpr.setBounds(new Rectangle(140, 10, 270, 27));
        addPanel.add(txtExpr);

        btnAddOk.setBounds(new Rectangle(420, 10, 125, 25));
        btnAddOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnAddOk_actionPerformed(e);
            }
        });
        addPanel.add(btnAddOk);

        lblResource.setBounds(new Rectangle(20, 45, 110, 27));
        addPanel.add(lblResource);

        txtResource.setBounds(new Rectangle(140, 45, 270, 27));
        addPanel.add(txtResource);

        btnAddCancel.setBounds(new Rectangle(420, 45, 125, 25));
        btnAddCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnAddCancel_actionPerformed(e);
            }
        });
        addPanel.add(btnAddCancel);

        // final OK button of the entire form
        btnOk.setBounds(new Rectangle(470, 400, 125, 25));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnOk_actionPerformed(e);
            }
        });
        jPanel1.add(btnOk);
    }

    private void btnAdd_actionPerformed(ActionEvent e) {
      editMode = false;
      enableDisableAddPanel(true);
      btnAddOk.setText("Add");
    }

    private void btnEdit_actionPerformed(ActionEvent e) {
      try {
        editMode = true;

        btnAddOk.setText("Ok");
        Case resourceListItem = getResourceListItem(lstResource.getSelectedValue());
        editCase = resourceListItem;

        txtExpr.setText(resourceListItem.getValue());
        txtResource.setText(resourceListItem.getReturnfragment());
        enableDisableAddPanel(true);
      } catch ( Exception exc) {
        System.out.println("ResourcesEditor: btnEdit_actionPerformed: exception");
      }
    }

    private void btnRemove_actionPerformed(ActionEvent e) {
      try {
        // get the selected item.
        Case resourceListItem = getResourceListItem(lstResource.getSelectedValue());
        // remove the item
        // find showability attribute
        for (Iterator i = concept.attributeList.iterator(); i.hasNext();) {
          WOWOutAttribute tempAttr = (WOWOutAttribute) i.next();
          if (tempAttr.name.equals("showability")) {
            // add the casegroup
            if (tempAttr.casegroup != null) {
              Vector tempCV = tempAttr.casegroup.getCaseValues();
              tempCV.remove(resourceListItem);
            } else {
              // shout not be possible
              System.out.println("Error");
            }
          }
        }

        // reload the JList.
        DefaultListModel dataHolder = (DefaultListModel)lstResource.getModel();
        dataHolder = getResourceListItems(dataHolder);
      } catch ( Exception exc) {
      }
    }

    private void btnAddOk_actionPerformed(ActionEvent e) {
        // add or edit (to) the list
        Case caseValue = new Case();
        if ((!txtExpr.getText().equals("")) && (!txtResource.getText().equals("")) ) {
          try {
            caseValue.setValue(txtExpr.getText());
            caseValue.setReturnfragment(txtResource.getText());

            // find showability attribute
            for (Iterator i = concept.attributeList.iterator(); i.hasNext(); ) {
              WOWOutAttribute tempAttr = (WOWOutAttribute) i.next();
              if (tempAttr.name.equals("showability")) {
                // add the casegroup
                if (tempAttr.casegroup != null) {
                  Vector tempCV = tempAttr.casegroup.getCaseValues();
                  if (editMode) {
                    tempCV.remove(editCase);
                    tempCV.add(caseValue);
                  }
                  else {
                    tempCV.add(caseValue);
                  }
                }
              }
            }

            // refresh list
            DefaultListModel dataHolder = (DefaultListModel) lstResource.getModel();
            dataHolder = getResourceListItems(dataHolder);

            txtExpr.setText("");
            txtResource.setText("");
            enableDisableAddPanel(false);
          } catch (Exception event) {
            System.out.println("ResourcesEditor: btnAddOk_actionPerformed: Exception occured: " +event.toString() );
          }
        } else {
          // messagebox
          JOptionPane.showConfirmDialog((Component) null,
                                                     "Input fields may not be empty.","alert",JOptionPane.PLAIN_MESSAGE);
          return;
        }

    }

    private void btnAddCancel_actionPerformed(ActionEvent e) {
      txtExpr.setText("");
      txtResource.setText("");
      enableDisableAddPanel(false);
    }

    private void btnOk_actionPerformed(ActionEvent e) {
      save();
      frame.setVisible(false);
      frame.dispose();
      frame = null;
    }

    private void save() {
      // find showability attribute
      for (Iterator i = concept.attributeList.iterator(); i.hasNext(); ) {
        WOWOutAttribute tempAttr = (WOWOutAttribute) i.next();
        if (tempAttr.name.equals("showability")) {
          // get the casegroup
          if (tempAttr.casegroup.getCaseValues().isEmpty() &&
              txtDefault.getText().equals("")) {
            // remove casegroup object
            tempAttr.casegroup = null;
          }

          if (tempAttr.casegroup != null) {
            // store the default fragment
            String defaultText = txtDefault.getText();
            if (defaultText == null) {
              defaultText = "";
            }
            tempAttr.casegroup.setDefaultFragment(defaultText);
          }
        }
      }

    }

    // Added by @Loc Nguyen @ 31-03-2008
    private Case getResourceListItem(Object key) {
      return (Case)resourceMap.get(key);
    }

    // Added by @Loc Nguyen @ 31-03-2008
    private DefaultListModel getResourceListItems(DefaultListModel dataHolder)
    {

      dataHolder.clear();
      this.resourceMap.clear();

      // find showability attribute
      for (Iterator i = concept.attributeList.iterator(); i.hasNext();) {
        WOWOutAttribute tempAttr = (WOWOutAttribute) i.next();
        if (tempAttr.name.equals("showability")) {
          // add the casegroup
          if (tempAttr.casegroup != null) {
            Case tempCase = null;
            Vector tempCV = tempAttr.casegroup.getCaseValues();
            // loop the stored casevalues and add these to this vector
            for (int j=0; j<tempCV.size(); j++) {
              tempCase = (Case) tempCV.get(j);
              String key = tempCase.getValue() +"_" +tempCase.getReturnfragment();
              dataHolder.addElement(key);
              resourceMap.put(key, tempCase);
            }
          }
        }
      }

      return dataHolder;
    }

    private void enableDisableAddPanel(boolean enabled) {
      // disable or enable add panel
      txtExpr.setEnabled(enabled);
      txtResource.setEnabled(enabled);
      btnAddOk.setEnabled(enabled);
      btnAddCancel.setEnabled(enabled);

      // disable or enable all the rest
      txtDefault.setEnabled(!enabled);
      lstResource.setEnabled(!enabled);
      btnAdd.setEnabled(!enabled);
      btnEdit.setEnabled(!enabled);
      btnRemove.setEnabled(!enabled);
      btnOk.setEnabled(!enabled);
    }

    private boolean initFrame() {
      boolean found = false;

      // disable add resource panel at startup
      enableDisableAddPanel(false);

      // find showability attribute
        for (Iterator i = concept.attributeList.iterator(); i.hasNext(); ) {
          WOWOutAttribute tempAttr = (WOWOutAttribute) i.next();
          if (tempAttr.name.equals("showability")) {
            found = true;
            // add the casegroup
            if (tempAttr.casegroup == null) {
              tempAttr.casegroup = new CaseGroup();
            }
            else {
              txtDefault.setText(tempAttr.casegroup.getDefaultFragment());
            }
          }
        }
      return found;
    }
}
