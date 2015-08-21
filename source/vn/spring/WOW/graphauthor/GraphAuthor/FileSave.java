/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * FileSave.java 1.0, June 1, 2008
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
import java.net.*;
import java.io.*;
import javax.swing.*;

import vn.spring.WOW.graphauthor.author.*;



/**
 * This class saves all relations and concept information to an xml file.
 *
 */
// changed by @Loc Nguyen @ 13-05-2008: set the selected item the current open project

public class FileSave {
    private JPanel jPanel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JButton jButton1 = new JButton();
    public JDialog frame;
    public String fileName;
    public boolean cancelled;
    public URL home;
    public String dirname;
    private String currentProject = "";


  private JButton Cancel_Button = new JButton();
  private JComboBox jComboBox1 = new JComboBox();

    public FileSave(URL base, JFrame parentFrame, String currentOpenProject) {
      home = base;
      String path = home.getPath();
      String pathttemp = path.substring(1, path.length());
      int index = pathttemp.indexOf("/");
      index++;
      dirname = path.substring(0, index);
      if (dirname.equals("/GraphAuthor")) {
		  dirname = "";
	  }
      currentProject = currentOpenProject;
      try {
            jbInit();
            this.fillList();

            frame = new JDialog(parentFrame, "File Save", true);
            frame.setSize(600, 200);
            frame.setLocation(100, 100);
            frame.getContentPane().add(jPanel1);
            frame.getRootPane().setDefaultButton(jButton1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 16));
        jLabel1.setBorder(BorderFactory.createLoweredBevelBorder());
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("File Save Dialog");
        jLabel1.setBounds(new Rectangle(11, 9, 557, 44));
        jPanel1.setLayout(null);
        jButton1.setBounds(new Rectangle(287, 96, 138, 36));
        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButton1_actionPerformed(e);
            }
        });
        Cancel_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Cancel_Button_actionPerformed(e);
            }
        });
    Cancel_Button.setText("Cancel");
    Cancel_Button.setBounds(new Rectangle(429, 96, 138, 36));
    jComboBox1.setEditable(true);
    jComboBox1.setBounds(new Rectangle(12, 58, 558, 36));
    jPanel1.add(jLabel1, null);
    jPanel1.add(jComboBox1, null);
    jPanel1.add(jButton1, null);
    jPanel1.add(Cancel_Button, null);
    }

    public void fillList() {

      try {

         URL url = new URL("http://" + home.getHost() + ":" + home.getPort() +
                           dirname +
                           "/servlet/authorservlets.ListFiles?extention=" +
                           ".gaf" + "&userName=" + AuthorSTATIC.authorName);
         BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
         String sFile = "";
         do {
           sFile = in.readLine();
           if (sFile != null) {
             this.jComboBox1.addItem(sFile.trim());
           }

         } while (sFile != null);
         in.close();

         // added by @Loc Nguyen @ 13-05-2008
         // set the currentopenfile name as selected
         if (!currentProject.equals("")) {
           try {
             this.jComboBox1.setSelectedItem(currentProject);
           } catch (Exception e) {
             System.out.println(
                 "FileSave: fillList: exception setting the currentproject: " +e.getMessage());
             this.jComboBox1.setSelectedIndex( -1);
           }
         }
         // end added by @Loc Nguyen @ 13-05-2008

     } catch (Exception e) {
       System.out.println("error saving file");
     }
   }


    public void show() {
        frame.setVisible(true);
    }


 public boolean CheckCourseName(String name){
   try {

       URL url = new URL("http://" + home.getHost() + ":" + home.getPort() +
                         dirname +
                         "/servlet/authorservlets.CheckCourse?courseName=" +
                         name + "&userName=" + AuthorSTATIC.authorName);
       BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
       String sFile = "";
       do {
        sFile = in.readLine();
        if (sFile.trim().equals("true")) {
          return true;
        }
         } while (sFile != null);
       in.close();

   } catch (Exception e) {
            }
   return false;
 }


    void jButton1_actionPerformed(ActionEvent e) {
      String name = (String) this.jComboBox1.getSelectedItem();
      if (this.CheckCourseName(name.trim())) {
        JOptionPane.showMessageDialog((Component) null,
                                                  "The course name is allready in use by another author.",
                                                  "error",
                                                  JOptionPane.OK_OPTION);
        return;

      }
      String comboText = ((String) this.jComboBox1.getSelectedItem()).trim();
      fileName = comboText.replaceAll(".gaf","");
      frame.setVisible(false);
      frame.dispose();
      frame = null;
    }

  void Cancel_Button_actionPerformed(ActionEvent e) {

      cancelled = true;
      frame.setVisible(false);
      frame.dispose();
        frame = null;

  }
}