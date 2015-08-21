/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Filters.java 1.0, June 1, 2008
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

import java.util.*;

import javax.swing.*;

/**
 * This class is used for the filter dialog.
 *
 */
public class Filters {
    public JDialog frame;
    private JPanel jPanel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JPanel jPanel2 = new JPanel();
    private JButton addAll = new JButton();
    private JButton removeAll = new JButton();
    private JButton addOne = new JButton();
    private JButton removeOne = new JButton();
    private JButton cancelButton = new JButton();
    private JButton okButton = new JButton();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    public boolean cancelled;
  private JScrollPane jScrollPane1 = new JScrollPane();
  private JList visibleList = new JList();
  private JScrollPane jScrollPane2 = new JScrollPane();
  private JList filteredList = new JList();

    public Filters(JFrame parentFrame) {
        try {
            cancelled = false;
            jbInit();
            this.initExtraComponents();

            frame = new JDialog(parentFrame, "Add Concept", true);
            frame.setSize(560, 400);
            frame.setLocation(100, 100);
            frame.getContentPane().add(jPanel1);
            frame.getRootPane().setDefaultButton(okButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        frame.setVisible(true);
    }

    private void jbInit() throws Exception {
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 16));
        jLabel1.setBorder(BorderFactory.createLoweredBevelBorder());
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Filters");
        jLabel1.setBounds(new Rectangle(11, 9, 523, 56));
        jPanel1.setLayout(null);
        jPanel2.setBorder(BorderFactory.createLoweredBevelBorder());
        jPanel2.setBounds(new Rectangle(12, 73, 522, 224));
        jPanel2.setLayout(null);
        addAll.setText("<<");
        addAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addAll_actionPerformed(e);
            }
        });
        addAll.setBounds(new Rectangle(236, 179, 48, 27));
        removeAll.setBounds(new Rectangle(235, 141, 48, 27));
        removeAll.setText(">>");
        removeAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeAll_actionPerformed(e);
            }
        });
        addOne.setBounds(new Rectangle(233, 103, 48, 27));
        addOne.setText("<");
        addOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addOne_actionPerformed(e);
            }
        });
        removeOne.setBounds(new Rectangle(232, 66, 48, 27));
        removeOne.setText(">");
        removeOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeOne_actionPerformed(e);
            }
        });
        cancelButton.setBounds(new Rectangle(405, 302, 130, 31));
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelButton_actionPerformed(e);
            }
        });
        okButton.setBounds(new Rectangle(258, 304, 132, 30));
        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButton_actionPerformed(e);
            }
        });
        jLabel2.setText("Visible");
        jLabel2.setBounds(new Rectangle(89, 13, 58, 25));
        jLabel3.setBounds(new Rectangle(368, 11, 58, 25));
        jLabel3.setText("Filtered");
        jScrollPane1.setBounds(new Rectangle(20, 44, 202, 168));
    visibleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    visibleList.setAutoscrolls(true);
    jScrollPane2.setBounds(new Rectangle(297, 44, 211, 163));
    jPanel2.add(jLabel2, null);
        jPanel2.add(jLabel3, null);
        jPanel2.add(removeOne, null);
        jPanel2.add(addOne, null);
        jPanel2.add(removeAll, null);
        jPanel2.add(addAll, null);
    jPanel2.add(jScrollPane1, null);
    jPanel2.add(jScrollPane2, null);
    filteredList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jScrollPane2.getViewport().add(filteredList, null);
    jScrollPane1.getViewport().add(visibleList, null);
        jPanel1.add(jLabel1, null);
        jPanel1.add(cancelButton, null);
        jPanel1.add(okButton, null);
        jPanel1.add(jPanel2, null);
    }

    void initExtraComponents() {
        this.visibleList.setListData(GraphAuthor.visListData);
        this.filteredList.setListData(GraphAuthor.filteredListData);
    }

    void addOne_actionPerformed(ActionEvent e) {
        try {
          String selectedItem = (String) this.filteredList.getSelectedValue();
               GraphAuthor.filteredListData.removeElementAt(
               this.filteredList.getSelectedIndex());

               GraphAuthor.visListData.add(selectedItem);
               this.visibleList.setListData(GraphAuthor.visListData);
            this.filteredList.setListData(GraphAuthor.filteredListData);

        } catch (Exception e1) {
        }

    }

    void removeOne_actionPerformed(ActionEvent e) {
        try {
            String selectedItem = (String) this.visibleList.getSelectedValue();
            GraphAuthor.visListData.removeElementAt(
            this.visibleList.getSelectedIndex());

            GraphAuthor.filteredListData.add(selectedItem);
            this.filteredList.setListData(GraphAuthor.filteredListData);
            this.visibleList.setListData(GraphAuthor.visListData);
        } catch (Exception e1) {
        }
    }

    void removeAll_actionPerformed(ActionEvent e) {
        for (ListIterator i = GraphAuthor.visListData.listIterator();
             i.hasNext();) {
            GraphAuthor.filteredListData.add(i.next());
        }

        GraphAuthor.visListData.clear();

        this.filteredList.setListData(GraphAuthor.filteredListData);
        this.visibleList.setListData(GraphAuthor.visListData);
    }

    void addAll_actionPerformed(ActionEvent e) {
        for (ListIterator i = GraphAuthor.filteredListData.listIterator();
             i.hasNext();) {
            GraphAuthor.visListData.add(i.next());
        }

        GraphAuthor.filteredListData.clear();

        this.filteredList.setListData(GraphAuthor.filteredListData);
        this.visibleList.setListData(GraphAuthor.visListData);
    }

    void cancelButton_actionPerformed(ActionEvent e) {
        cancelled = true;
        frame.setVisible(false);
        frame.dispose();
        frame = null;
    }

    void okButton_actionPerformed(ActionEvent e) {
        frame.setVisible(false);
        frame.dispose();
        frame = null;
    }
}