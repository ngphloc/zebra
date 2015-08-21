/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * EditDialog.java 1.0, June 1, 2008
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
import vn.spring.WOW.graphauthor.author.*;

import javax.swing.*;

/**
 * This class displays the renamedialog.
 *
 */
// changed by @Loc Nguyen @  29-04-2008, added nocommit checkbox

public class EditDialog {
     public String newConceptName;
     public String newDescription;
     public String newResource;
     public String newTemplate;
     // added by @Loc Nguyen @  28-05-2008
     public boolean newNoCommit;
     public String newStable;
     public String newStable_expr;
     //added by @David @18-05-2008
     public String newconcepttype;
     public String newtitle;
     private JLabel lblConcepttype = new JLabel("Concept type:");
     private JLabel lblTitle = new JLabel("Title:");
     private JTextField conceptType = new JTextField();
     private JTextField conceptTitle = new JTextField();
     //end added by @David @18-05-2008
     public boolean cancelled;

     private JPanel jPanel1 = new JPanel();
     private JPanel jPanel2 = new JPanel();
     public JDialog frame;
     private JButton OK_button = new JButton("OK");
     private JButton Cancel_button = new JButton("Cancel");
     private JLabel jLabel1 = new JLabel();
     private JLabel jLabel2 = new JLabel();
     private JLabel jLabel3 = new JLabel();
     private JLabel jLabel4 = new JLabel();
     private JLabel jLabel5 = new JLabel();
     private JTextField conceptName = new JTextField();
     private JTextField conceptDescription = new JTextField();
     private JTextField conceptResource = new JTextField();
     private JComboBox TemplateList = new JComboBox();
     private JLabel lblNoCommit = new JLabel("No commit");
     private JCheckBox chkNoCommit = new JCheckBox();
     private boolean orgNoCommit;

     // added by @Loc Nguyen @  10-06-2008
     private JCheckBox chkStable = new JCheckBox("Enable Stability");
     private JLabel lblStable = new JLabel("Stable");
     private JLabel lblExpression = new JLabel("Expression");

     private String[] stableList = { "always", "session", "freeze" };
     private JComboBox cmbStable = new JComboBox(stableList);
     private JTextField txtExpression = new JTextField();
     private JPanel stablePanel = new JPanel();
     private boolean isStable = false;
     private String orgStable;
     private String orgStable_expr;
     private String orgconcepttype;
     private String orgtitle;

   // added by @Loc Nguyen @  28-05-2008
   private boolean advMode = false;
   private boolean setresource;

     // changed by @Loc Nguyen @  29-04-2008
     public EditDialog(JFrame parentFrame, String conceptNamePar, String description, String resource, String Template, boolean nocommit, boolean advancedMode, String stable, String stable_expr, String concepttype, String title) {
       cancelled = false;
       advMode = advancedMode;
       orgNoCommit = nocommit;
       orgStable = stable;
       orgStable_expr = stable_expr;
       orgconcepttype = concepttype;
       orgtitle = title;
       try {
             // create frame
             jbInit();

             // init
             // initialize the stable values
             if (advMode) {
               if (!stable.equals("")) {
                 isStable = true;
                 chkStable.setSelected(true);
                 cmbStable.setSelectedItem(stable);
                 cmbStable.setEnabled(true);
                 if (stable.equals("freeze")) {
                   txtExpression.setText(stable_expr);
                   txtExpression.setEnabled(true);
                 } else {
                   txtExpression.setText("");
                   txtExpression.setEnabled(false);
                 }
               } else {
                 isStable = false;
                 chkStable.setSelected(false);
                 cmbStable.setEnabled(false);
                 txtExpression.setEnabled(false);
                 txtExpression.setText("");
                 cmbStable.setSelectedIndex( -1);
               }
             }
             try {
               conceptName.setText(conceptNamePar.trim());
             } catch (Exception te) {
               conceptName.setText(conceptNamePar);
             }
             this.conceptResource.setText(resource);
             this.conceptDescription.setText(description);
             //added by @David @18-05-2008
             this.conceptType.setText(concepttype);
             this.conceptType.setEditable(false);
             this.conceptTitle.setText(title);
             //end added by @David @18-05-2008
             this.TemplateList.setSelectedItem(Template);
             if (advMode) {
               this.chkNoCommit.setSelected(nocommit);
             }

             frame = new JDialog(parentFrame, "Edit Concept", true);
             if (advMode) {
               // 300
               frame.setSize(600, 466);
             } else {
               frame.setSize(600, 280);
             }
             frame.setLocation(100, 100);
             frame.getContentPane().add(jPanel1);
             frame.getRootPane().setDefaultButton(OK_button);
         } catch (Exception e) {
             e.printStackTrace();
         }
     }


     private void jbInit() throws Exception {

         jPanel1.setLayout(null);
         jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
         jPanel1.setMinimumSize(new Dimension(400, 400));

         jPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
         if (advMode) {
           // 159
           jPanel2.setBounds(new Rectangle(14, 44, 556, 326));
         } else {
           jPanel2.setBounds(new Rectangle(14, 44, 556, 131));
         }
         jPanel2.setLayout(null);
         jPanel1.add(jPanel2, null);

         jLabel2.setFont(new java.awt.Font("Dialog", 1, 16));
         jLabel2.setText("Edit Concept Dialog");
         jLabel2.setBounds(new Rectangle(207, 9, 180, 27));
         jPanel1.add(jLabel2, null);

         jLabel3.setToolTipText("");
         jLabel3.setText("Name: ");
         jLabel3.setBounds(new Rectangle(21, 14, 52, 17));
         jPanel2.add(jLabel3, null);
         conceptName.setBounds(new Rectangle(103, 11, 441, 23));
         jPanel2.add(conceptName, null);

         jLabel1.setText("Description:");
         jLabel1.setBounds(new Rectangle(21, 43, 74, 17));
         jPanel2.add(jLabel1, null);
         conceptDescription.setBounds(new Rectangle(103, 39, 441, 23));
         jPanel2.add(conceptDescription, null);

         jLabel4.setText("Resource:");
         jLabel4.setBounds(new Rectangle(21, 71, 63, 17));
         jPanel2.add(jLabel4, null);

         jPanel2.add(jLabel4, null);
         conceptResource.setText("file:/");
         conceptResource.setBounds(new Rectangle(103, 68, 439, 23));
         conceptResource.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                conceptResource_keyTyped(e);
            }
        });
         jPanel2.add(conceptResource, null);

         jLabel5.setBounds(new Rectangle(21, 99, 63, 17));
         jLabel5.setText("Template:");
         jPanel2.add(jLabel5, null);
         TemplateList.setBounds(new Rectangle(103, 98, 441, 23));

         for (Iterator i = AuthorSTATIC.templateList.iterator(); i.hasNext();) {
             ConceptTemplate ctemp = (ConceptTemplate) i.next();
             this.TemplateList.addItem(ctemp.name);
         }
         TemplateList.addItem("none");
         TemplateList.setSelectedItem("page concept");
         //TemplateList.setEnabled(false);
        TemplateList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                templateList_actionListener(e);
            }
        });
         jPanel2.add(TemplateList, null);

         if (advMode) {
           //added by @David @18-05-2008
           lblConcepttype.setBounds(new Rectangle(21, 127, 102, 17));
           jPanel2.add(lblConcepttype, null);
           conceptType.setBounds(new Rectangle(103, 127, 441, 23));
           jPanel2.add(conceptType);

           lblTitle.setBounds(new Rectangle(21, 155, 63, 17));
           jPanel2.add(lblTitle, null);
           conceptTitle.setBounds(new Rectangle(103, 155, 441, 23));
           jPanel2.add(conceptTitle);
           //end added by @David @18-05-2008

           lblNoCommit.setBounds(new Rectangle(21, 183, 63, 17));
           jPanel2.add(lblNoCommit, null);
           chkNoCommit.setBounds(new Rectangle(103, 183, 63, 23));
           jPanel2.add(chkNoCommit, null);

           // changed by @Loc Nguyen @  10-06-2008
           chkStable.setBounds(new Rectangle(21, 211, 300, 27));
           chkStable.setSelected(false);

           chkStable.addActionListener(new ActionListener()
           {
             public void actionPerformed(ActionEvent e)
             {
               toggleStable();
             }
           });
           jPanel2.add(chkStable);

           stablePanel.setLayout(null);
           stablePanel.setBounds(new Rectangle(10, 241, 536, 70));
           stablePanel.setBorder(BorderFactory.createLoweredBevelBorder());
           jPanel2.add(stablePanel);

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
           cmbStable.setEnabled(false);
           stablePanel.add(cmbStable);
           txtExpression.setBounds(new Rectangle(160, 40, 370, 27));
           txtExpression.setEnabled(false);
           stablePanel.add(txtExpression);
         }

         if (advMode) {
           OK_button.setBounds(new Rectangle(315, 375, 122, 25));
         } else {
           OK_button.setBounds(new Rectangle(315, 181, 122, 25));
         }
         OK_button.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 OK_button_actionPerformed(e);
             }
         });
         jPanel1.add(OK_button, null);

         if (advMode) {
           Cancel_button.setBounds(new Rectangle(447, 375, 122, 25));
         } else {
           Cancel_button.setBounds(new Rectangle(447, 181, 122, 25));
         }
         Cancel_button.addActionListener(new java.awt.event.ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 Cancel_button_actionPerformed(e);
             }
         });
         jPanel1.add(Cancel_button, null);
     }

     public void show() {
         frame.setVisible(true);
     }

     void OK_button_actionPerformed(ActionEvent e) {
         // check for wrong conceptNames
         // only letters and numbers are allowed
         String check = AddConcept.CheckConceptName(conceptName.getText());
         if (check != null) {
             JOptionPane.showConfirmDialog((Component) null,
                 "Invalid concept name!\n"+check,"alert",JOptionPane.PLAIN_MESSAGE);
             return;
         }
         newConceptName = conceptName.getText().trim();
         newDescription = this.conceptDescription.getText().trim();
         newResource = this.conceptResource.getText().trim();
         newTemplate = (String) this.TemplateList.getSelectedItem();
         // added by @Loc Nguyen @  29-04-2008
         if (advMode) {
           //added by @David @18-05-2008
           newconcepttype = this.conceptType.getText().trim();
           newtitle = this.conceptTitle.getText().trim();
           //end added by @David @18-05-2008
           newNoCommit = chkNoCommit.isSelected();

           if (chkStable.isSelected()) {
             // check if a valid stable value is selected
             String stableSelection = null;
             if (cmbStable.getSelectedIndex() != -1) {
               stableSelection = (String) cmbStable.getSelectedItem();
               if (stableSelection != null) {
                 newStable  = stableSelection;
                 if (stableSelection.equals("freeze")) {
                   newStable_expr = txtExpression.getText();
                 } else {
                   newStable_expr = "";
                 }
               } else {
                 newStable = "";
                 newStable_expr = "";
               }
             }
           } else {
             newStable = "";
             newStable_expr = "";
           }

         } else {
           newconcepttype = orgconcepttype;
           newtitle = orgtitle;
           newNoCommit = orgNoCommit;
           newStable = orgStable;
           newStable_expr = orgStable_expr;
         }
         // end added by @Loc Nguyen @  29-04-2008

         frame.setVisible(false);
         frame.dispose();
         frame = null;
     }

   void Cancel_button_actionPerformed(ActionEvent e) {
        cancelled = true;
        frame.setVisible(false);
        frame.dispose();
        frame = null;
  }

    void conceptResource_keyTyped(KeyEvent e) {
        String selectedItem = "";
        selectedItem = (String) this.TemplateList.getSelectedItem();
        setresource = false;
        for (Iterator i = AuthorSTATIC.templateList.iterator(); i.hasNext();) {
            ConceptTemplate ctemp = (ConceptTemplate) i.next();
            if (ctemp.name.equals(selectedItem)) {
                if (ctemp.hasresource.trim().toLowerCase().equals("true"))
                setresource = true;
                break;
            }
        }
        //if (!selectedItem.equals("test concept"))
        if (!setresource)
        this.TemplateList.setSelectedItem("page concept");
    }

    void templateList_actionListener(ActionEvent e) {
        // get selected item
        String selectedItem = "";
        selectedItem = (String) this.TemplateList.getSelectedItem();
        setresource = false;
        if (selectedItem.equals("none")) this.conceptType.setText("none");
        else {
            for (Iterator i = AuthorSTATIC.templateList.iterator(); i.hasNext();) {
                ConceptTemplate ctemp = (ConceptTemplate) i.next();
                if (ctemp.name.equals(selectedItem)) {
                    if (ctemp.hasresource.trim().toLowerCase().equals("true"))
                    setresource = true;
                    this.conceptType.setText(ctemp.concepttype);
                    break;
                }
            }
        }
        if (!setresource) {
          this.conceptResource.setText("");
        }
    }

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

}