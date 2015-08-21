/*


    This file is part of WOW! (Adaptive Hypermedia for All)

    WOW! is free software


*/
/**
 * Attributes.java 1.0, September 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008.
 * All Rights Reserved.
 *
 * This software is proprietary information of University of Science
 * of Technology.
 */
package vn.spring.WOW.graphauthor.GraphAuthor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import vn.spring.WOW.graphauthor.author.*;
import java.util.*;

/**
 * Dialog for applying instructional strategies corresponding to users' learning/cognitive styles
 *
 */
public class Strategies {
    public  JDialog frame;
    public  JFrame parentFrame;
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
    private JList orderedStrategiesList = new JList();
    private JList existingStrategiesList = new JList();

  private JScrollPane jScrollPane2 = new JScrollPane();

    public Strategies(JFrame parentFrame) {
      this.parentFrame = parentFrame;
        try {
          // init frame
          //AuthorSTATIC.strategyList = new Vector();
          jbInit();
          this.initExtraComponents();
          frame = new JDialog(parentFrame, "Application of strategies", true);
          frame.setSize(635, 510);
          frame.setLocation(100, 100);
          frame.getContentPane().add(jPanel1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
      try {
        frame.setVisible(true);
      } catch (Exception e) {
        System.out.println("Strategies: show: Failed to initialize the strategies dialog. Exception: " +e.toString() );
      }
    }

    private void jbInit() throws Exception {
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 16));
        jLabel1.setBorder(BorderFactory.createLoweredBevelBorder());
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Strategies");
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
        jLabel2.setText("Order of application");
        jLabel2.setBounds(new Rectangle(60, 13, 140, 25));
        jLabel3.setBounds(new Rectangle(350, 11, 140, 25));
        jLabel3.setText("Existing strategies");
        jScrollPane1.setBounds(new Rectangle(20, 44, 202, 168));
    orderedStrategiesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    orderedStrategiesList.setAutoscrolls(true);
    jScrollPane2.setBounds(new Rectangle(297, 44, 211, 163));
    jPanel2.add(jLabel2, null);
        jPanel2.add(jLabel3, null);
        jPanel2.add(removeOne, null);
        jPanel2.add(addOne, null);
        jPanel2.add(removeAll, null);
        jPanel2.add(addAll, null);
    jPanel2.add(jScrollPane1, null);
    jPanel2.add(jScrollPane2, null);
    existingStrategiesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jScrollPane2.getViewport().add(existingStrategiesList, null);
    jScrollPane1.getViewport().add(orderedStrategiesList, null);
        jPanel1.add(jLabel1, null);
        jPanel1.add(cancelButton, null);
        jPanel1.add(okButton, null);
        jPanel1.add(jPanel2, null);
    }

    void initExtraComponents() {
        this.orderedStrategiesList.setListData(AuthorSTATIC.strategyList);
        this.existingStrategiesList.setListData(GraphAuthor.existingStrategies);
    }

    void addOne_actionPerformed(ActionEvent e) {
        try {
          String selectedItem = (String) this.existingStrategiesList.getSelectedValue();
               GraphAuthor.existingStrategies.removeElementAt(
               this.existingStrategiesList.getSelectedIndex());
               AuthorSTATIC.strategyList.add(selectedItem);
               this.orderedStrategiesList.setListData( AuthorSTATIC.strategyList);
            this.existingStrategiesList.setListData(GraphAuthor.existingStrategies);

        } catch (Exception e1) {
        }

    }

    void removeOne_actionPerformed(ActionEvent e) {
        try {
            String selectedItem = (String) this.orderedStrategiesList.getSelectedValue();
            AuthorSTATIC.strategyList.removeElementAt(
            this.orderedStrategiesList.getSelectedIndex());

            GraphAuthor.existingStrategies.add(selectedItem);
            this.existingStrategiesList.setListData(GraphAuthor.existingStrategies);
            this.orderedStrategiesList.setListData(AuthorSTATIC.strategyList);
        } catch (Exception e1) {
        }
    }

    void removeAll_actionPerformed(ActionEvent e) {
        for (ListIterator i = AuthorSTATIC.strategyList.listIterator();
             i.hasNext();) {
            GraphAuthor.existingStrategies.add(i.next());
        }

        AuthorSTATIC.strategyList.clear();

        this.existingStrategiesList.setListData(GraphAuthor.existingStrategies);
        this.orderedStrategiesList.setListData(AuthorSTATIC.strategyList);
    }

    void addAll_actionPerformed(ActionEvent e) {
        for (ListIterator i = GraphAuthor.existingStrategies.listIterator();
             i.hasNext();) {
            AuthorSTATIC.strategyList.add(i.next());
        }

        GraphAuthor.existingStrategies.clear();

        this.existingStrategiesList.setListData(GraphAuthor.existingStrategies);
        this.orderedStrategiesList.setListData(AuthorSTATIC.strategyList);
    }

    void cancelButton_actionPerformed(ActionEvent e) {
        cancelled = true;
        frame.setVisible(false);
        frame.dispose();
        frame = null;
    }

    void okButton_actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog((Component) null,
        							"As a result of application the strategies extra attributes can be added to concepts or extra pages will be generated.",
        							"information", JOptionPane.OK_OPTION);
        frame.setVisible(false);
        frame.dispose();
        frame = null;
    }
}
