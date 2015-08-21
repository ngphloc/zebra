/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AddConcept.java 1.0, June 1, 2008
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
import java.util.LinkedList;

import javax.swing.*;

import vn.spring.WOW.graphauthor.author.*;


/**
 * Dialog for adding a concept to the graph.
 *
 */
public class AddConcept {
    JPanel jPanel1 = new JPanel();
    JToggleButton addSaveButton = new JToggleButton();
    JLabel jLabel2 = new JLabel();
    JPanel jPanel2 = new JPanel();
    JLabel jLabel3 = new JLabel();
    public LinkedList conceptlist;
    public WOWOutConcept concept;
    public boolean cancelled;
    private JDialog frame;
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel4 = new JLabel();
    private JTextField conceptName = new JTextField();
    private JTextField conceptDescription = new JTextField();
    private JTextField conceptResource = new JTextField();
    private JToggleButton addButton = new JToggleButton();
    private JToggleButton cancel_button = new JToggleButton();
    private JLabel jLabel5 = new JLabel();
    private JComboBox TemplateList = new JComboBox();
    private JLabel lblNoCommit = new JLabel("No commit");
    private JCheckBox chkNoCommit = new JCheckBox();
    private boolean advMode = false;

    //added by @David @18-05-2008
    private JLabel lblConcepttype = new JLabel("Concept type:");
    private JLabel lblTitle = new JLabel("Title:");
    private JTextField conceptType = new JTextField();
    private JTextField conceptTitle = new JTextField();
    //end added by @David @18-05-2008

    // added by @Loc Nguyen @ 10-06-2008
    private JCheckBox chkStable = new JCheckBox("Enable Stability");
    private JLabel lblStable = new JLabel("Stable");
    private JLabel lblExpression = new JLabel("Expression");

    private String[] stableList = { "always", "session", "freeze" };
    private JComboBox cmbStable = new JComboBox(stableList);
    private JTextField txtExpression = new JTextField();
    private JPanel stablePanel = new JPanel();
    private boolean isStable = false;
    private boolean setresource;

    public AddConcept(JFrame parentFrame, boolean advancedMode) {
        conceptlist = new LinkedList();
        advMode = advancedMode;
        try {

            jbInit();

            frame = new JDialog(parentFrame, "Add Concept", true);
            if (advMode) {
              frame.setSize(600, 466);
            } else {
              frame.setSize(600, 280);
            }
            frame.setLocation(100, 100);
            frame.getContentPane().add(jPanel1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        frame.setVisible(true);
    }

    private void jbInit() throws Exception {

        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
        jPanel1.setMinimumSize(new Dimension(400, 400));

        jPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
        if (advMode) {
          jPanel2.setBounds(new Rectangle(14, 44, 556, 326));
        } else {
          jPanel2.setBounds(new Rectangle(14, 44, 556, 131));
        }
        jPanel2.setLayout(null);
        jPanel1.add(jPanel2, null);

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 16));
        jLabel2.setText("Add new Concept");
        jLabel2.setBounds(new Rectangle(227, 9, 148, 27));
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
            if (ctemp.name.equals("page concept")) this.conceptType.setText(ctemp.concepttype);
        }
        TemplateList.addItem("none");
        TemplateList.setSelectedItem("page concept");
        TemplateList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                templateList_actionListener(e);
            }
        });
        jPanel2.add(TemplateList, null);

        // nocommit and stable
        if (advMode) {
          //added by @David @18-05-2008
          lblConcepttype.setBounds(new Rectangle(21, 127, 102, 17));
          jPanel2.add(lblConcepttype, null);
          conceptType.setBounds(new Rectangle(103, 127, 441, 23));
          conceptType.setEditable(false);
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
// (14, 44, 556, 159) panel 2

          // changed by @Loc Nguyen @ 06-06-2008
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
          addButton.setBounds(new Rectangle(182, 375, 122, 25));
        } else {
          addButton.setBounds(new Rectangle(182, 181, 122, 25));
        }
        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addButton_actionPerformed(e);
            }
        });
        jPanel1.add(addButton, null);

        if (advMode) {
          addSaveButton.setBounds(new Rectangle(315, 375, 122, 25));
        } else {
          addSaveButton.setBounds(new Rectangle(315, 181, 122, 25));
        }
        addSaveButton.setText("Ok");
        addSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSaveButton_actionPerformed(e);
            }
        });
        jPanel1.add(addSaveButton, null);

        if (advMode) {
          cancel_button.setBounds(new Rectangle(447, 375, 122, 25));
        } else {
          cancel_button.setBounds(new Rectangle(447, 181, 122, 25));
        }
        cancel_button.setText("Cancel");
        cancel_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel_button_actionPerformed(e);
            }
        });
        jPanel1.add(cancel_button, null);
    }

    void addSaveButton_actionPerformed(ActionEvent e) {
        if (addConcept()) {
            frame.setVisible(false);
            frame.dispose();
            frame = null;
        }
    }

    public static String CheckConceptName(String cName) {
        if (cName.toLowerCase().equals("personal")) return "The word 'personal' is reserved and may not be used as concept name.";
        if (cName.indexOf("___destination") != -1) return "A concept name may not contain the word '___destination'.";
        if (cName.indexOf("___source") != -1) return "A concept name may not contain the word '___source'.";
        if (cName.indexOf("___parent") != -1) return "A concept name may not contain the word '___parent'.";
        if (cName.indexOf("___child") != -1) return "A concept name may not contain the word '___child'.";
        if (Character.isLetter(cName.charAt(0)) == false ) return "A concept name must start with a letter.";

        int stringLength = cName.length();
        for (int j = 0; j < stringLength;) {
            if (Character.isLetterOrDigit(cName.charAt(j)) == false) {
                return "A concept name may only consist of letters and digits.";
            }

            j++;
        }

        return null;
    }

    private boolean addConcept() {
        String tempSelectedItem = "";

        if (conceptName.getText().equals("")) {
            return false;
        }

        // check for wrong conceptNames
        // only letters and numbers are allowed
        String check = CheckConceptName(conceptName.getText());
        if (check != null) {
            JOptionPane.showConfirmDialog((Component) null,
                "Invalid concept name!\n"+check,"alert",JOptionPane.PLAIN_MESSAGE);
            return false;
        }

        tempSelectedItem = (String) this.TemplateList.getSelectedItem();
        setresource = false;
        for (Iterator i = AuthorSTATIC.templateList.iterator(); i.hasNext();) {
            ConceptTemplate ctemp = (ConceptTemplate) i.next();
            if (ctemp.name.equals(tempSelectedItem)) {
                if (ctemp.hasresource.trim().toLowerCase().equals("true"))
                setresource = true;
                break;
            }
        }
        if (tempSelectedItem.toLowerCase().indexOf("fragment") == -1) {
          // only show resource empty warning if it's not a fragment
        // added by Natalia Stash, 15-07-2008
        // an error message is shown only if the user does not specify a resource for a page concept
          if ( (conceptResource.getText().indexOf("http:") == -1) &&
              (conceptResource.getText().indexOf("file:") == -1) && setresource) {
            JOptionPane.showConfirmDialog( (Component)null,
                "Invalid resource url \n Use 'http:/' or ''file:/'",
                "alert",
                JOptionPane.PLAIN_MESSAGE);
            return false ;
          }
          if (conceptResource.getText().equals("file:/")) {
            JOptionPane.showConfirmDialog( (Component)null,
                "Please, specify a correct resource url!",
                "alert",
                JOptionPane.PLAIN_MESSAGE);
            return false;
          }
        }

        concept = new WOWOutConcept();
        concept.name = conceptName.getText().trim();
        try {
          concept.description = conceptDescription.getText().trim();
        } catch (Exception e1) {
          concept.description = conceptDescription.getText();
        }
        try {
          concept.resource = conceptResource.getText().trim();
        } catch (Exception e2) {
          concept.resource = conceptResource.getText();
        }
        concept.template = (String) this.TemplateList.getSelectedItem();
        try {
            concept.concepttype = conceptType.getText().trim();
        } catch (Exception e3) {
            concept.concepttype = null;
        }
        try {
            concept.title = conceptTitle.getText().trim();
        } catch (Exception e3) {
            concept.title = null;
        }


        // added by @Loc Nguyen @ 28-05-2008
        // changed by @Loc Nguyen @ 10-06-2008
        if (advMode) {
          concept.nocommit = chkNoCommit.isSelected();

          if (chkStable.isSelected()) {
            // check if a valid stable value is selected
            String stableSelection = null;
            if (cmbStable.getSelectedIndex() != -1) {
              stableSelection = (String) cmbStable.getSelectedItem();
              if (stableSelection != null) {
                concept.stable = stableSelection;
                if (stableSelection.equals("freeze")) {
                  concept.stable_expr = txtExpression.getText();
                } else {
                  concept.stable_expr = "";
                }
              } else {
                concept.stable = "";
                concept.stable_expr = "";
              }
            }
          } else {
            concept.stable = "";
            concept.stable_expr = "";
          }
        } else {
          concept.nocommit = false;
          concept.stable = "";
          concept.stable_expr = "";
        }
        // end added by @Loc Nguyen @ 28-05-2008
        // end changed by @Loc Nguyen @ 10-06-2008

        if (conceptlist.contains(concept) == false) {
          // added by @Loc Nguyen @ 25-03-2008
          // add the attributes of the template to the concept
          concept.AddTemplateAttributes();
          conceptlist.add(concept);
        }

        // clear fields
        conceptName.setText("");
        conceptDescription.setText("");
        conceptResource.setText("file:/");
        this.TemplateList.setSelectedItem("page concept");
        return true;
    }

    private void addButton_actionPerformed(ActionEvent e) {
        addConcept();
    }

    void cancel_button_actionPerformed(ActionEvent e) {
        cancelled = true;
        frame.setVisible(false);
        frame.dispose();
        frame = null;
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
        if (!setresource)
        this.TemplateList.setSelectedItem("page concept");
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