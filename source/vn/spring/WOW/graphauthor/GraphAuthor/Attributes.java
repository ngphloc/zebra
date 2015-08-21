/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Attributes.java 1.0, June 1, 2008
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
import javax.swing.DefaultListModel;
import java.util.HashMap;

/**
 * Dialog for adding and editing an attribute of a concept
 *
 */
public class Attributes {
  //private LinkedList conceptlist;
  private WOWOutConcept concept;
  private HashMap attributeMap = new HashMap();

    private JDialog frame;
    private JPanel jPanel1 = new JPanel();
    private JPanel jPanel2 = new JPanel();
    private JButton oKButton = new JButton("OK");
    private JButton addButton = new JButton("Add");
    private JButton editButton = new JButton("Edit");
    private JButton removeButton = new JButton("Remove");
    private JList attributeList = new JList(new DefaultListModel());
    private JLabel lblFrameTitle = new JLabel("Attributes");
    private JLabel lblAttributes = new JLabel("Attributes");
    private JFrame parentFrame;

    public Attributes(JFrame parentFrame, WOWOutConcept conceptParam) {
      this.concept  = conceptParam;
      this.parentFrame = parentFrame;
        try {
          // init frame
          // fill the list with attributes
          DefaultListModel attributeHolder = (DefaultListModel)this.attributeList.getModel();
          attributeHolder = getAttributesListItems(attributeHolder);
          this.attributeList.setVisibleRowCount(5);

          jbInit();

          frame = new JDialog(parentFrame, "Attributes", true);
          frame.setSize(635, 340);
          frame.setLocation(100, 100);
          frame.getContentPane().add(jPanel1);
          frame.getRootPane().setDefaultButton(oKButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
      try {
        frame.setVisible(true);
      } catch (Exception e) {
        System.out.println("Attributes: show: Failed to initialize the attributes dialog. Exception: " +e.toString() );
      }
    }

    private void jbInit() throws Exception {

        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
        jPanel1.setMinimumSize(new Dimension(400, 400));

        jPanel2.setLayout(null);
        jPanel2.setBounds(new Rectangle(24, 41, 584, 199));
//        jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
        jPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
        jPanel2.setMinimumSize(new Dimension(400, 400));
        jPanel1.add(jPanel2);

        lblFrameTitle.setFont(new java.awt.Font("Dialog", 1, 16));
        lblFrameTitle.setBounds(new Rectangle(227, 9, 148, 27));
        jPanel1.add(lblFrameTitle);

//        lblAttributes.setFont(new java.awt.Font("Dialog", 1, 16));
        lblAttributes.setBounds(new Rectangle(21, 12, 74, 18));
        jPanel2.add(lblAttributes);

        // attribute listbox
        JScrollPane attributeHolder = new JScrollPane(this.attributeList);
        attributeHolder.setBounds(new Rectangle(20, 35, 544, 154));
//        attributeHolder.setPreferredSize(new Dimension(300, 40));

        jPanel2.add(attributeHolder);

        addButton.setBounds(new Rectangle(23, 248, 124, 25));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addButton_actionPerformed(e);
            }
        });
        jPanel1.add(addButton);

        editButton.setBounds(new Rectangle(162, 248, 124, 25));
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editButton_actionPerformed(e);
            }
        });
        jPanel1.add(editButton);

        removeButton.setBounds(new Rectangle(301, 248, 124, 25));
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeButton_actionPerformed(e);
            }
        });
        jPanel1.add(removeButton);

        oKButton.setText("Ok");
        oKButton.setBounds(new Rectangle(440, 248, 124, 25));
        oKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                oKButton_actionPerformed(e);
            }
        });
        jPanel1.add(oKButton);
    }

    private void addButton_actionPerformed(ActionEvent e) {
      WOWOutAttribute newAttribute = new WOWOutAttribute();
      concept.attributeList.add(newAttribute);
      AttributeEditor atrEditor = new AttributeEditor(parentFrame, concept, newAttribute, true);
      atrEditor.show();
      // refresh list
      DefaultListModel attributeHolder = (DefaultListModel)this.attributeList.getModel();
      attributeHolder = getAttributesListItems(attributeHolder);
    }

    private void editButton_actionPerformed(ActionEvent e) {
      try {
        WOWOutAttribute attributeListItem = getAttributesListItem(attributeList.getSelectedValue());
        AttributeEditor atrEditor = new AttributeEditor(parentFrame, concept, attributeListItem, false);
        atrEditor.show();
      } catch ( Exception exc) {
      }
    }

    private void removeButton_actionPerformed(ActionEvent e) {
      try {
        // get the selected item.
        WOWOutAttribute attributeListItem = getAttributesListItem(attributeList.getSelectedValue());
        // remove the item
        concept.attributeList.remove(attributeListItem);

        // reload the JList.
        DefaultListModel dataHolder = (DefaultListModel)attributeList.getModel();
        dataHolder = getAttributesListItems(dataHolder);
      } catch ( Exception exc) {
      }
    }

    private void oKButton_actionPerformed(ActionEvent e) {
        frame.setVisible(false);
        frame.dispose();
        frame = null;
    }

    // Added by @Loc Nguyen @  31-03-2008
    public WOWOutAttribute getAttributesListItem(Object key) {
      return (WOWOutAttribute)attributeMap.get(key);
    }

    // Added by @Loc Nguyen @  31-03-2008
    public DefaultListModel getAttributesListItems(DefaultListModel dataHolder)
    {

      dataHolder.clear();
      this.attributeMap.clear();

      try {
        for (Iterator i = concept.attributeList.iterator(); i.hasNext(); ) {
          WOWOutAttribute tmpAttribute = (WOWOutAttribute) i.next();
          dataHolder.addElement(tmpAttribute.name);
          attributeMap.put(tmpAttribute.name, tmpAttribute);
        }
      } catch (Exception e) {
        System.out.println("Attributes: getAttributesListItems: exception: " +e.toString() );
      }
      return dataHolder;
    }

}
