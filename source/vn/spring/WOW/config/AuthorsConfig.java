/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * AuthorsConfig.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW.config;

import java.io.*;
import java.util.*;
import vn.spring.WOW.WOWStatic;

public class AuthorsConfig {
   public Hashtable AuthorHash;
   public String authorlist;
   public File TFile;

   public AuthorsConfig() {
      AuthorHash=new Hashtable();
      authorlist = WOWStatic.config.Get("WOWROOT")+WOWStatic.AUTHORFILESPATH.substring(1)+"authorlistfile.xml";
      TFile = new File(authorlist);
      LoadConfig();
   }

   public WowAuthor GetAuthor(String l) {
     if (l!=null) {
        return (WowAuthor)AuthorHash.get(l);
     } else {
       return null;
     }
   }

   public void PutAuthor(WowAuthor a) {
     if (a!=null) {
        AuthorHash.put(a.login,a);
     }
   }

   public void RemoveAuthor(String a) {
     if ((a!=null) && AuthorHash.containsKey(a)) {
       AuthorHash.remove(a);
     }
   }

   public void StoreConfig() {
     WowAuthor wowAuthor;
     FileOutputStream foutput;
     if (TFile!=null) {
        try {
          foutput=new FileOutputStream(TFile);
          PrintWriter xoutput=new PrintWriter(foutput);
          xoutput.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
          xoutput.println("<!DOCTYPE authorList SYSTEM \"authorlist.dtd\">");
          xoutput.println("<authorList>");
          for (Enumeration e = AuthorHash.elements() ; e.hasMoreElements() ;) {
            wowAuthor=(WowAuthor)e.nextElement();
            xoutput.print("  <author login=\""+wowAuthor.getLogin()+"\" password=\"");
            xoutput.println(wowAuthor.getHashed()+"\">\n"+"    <name>"+wowAuthor.getName()+"</name>\n"+"    <courseList>");
            Vector v = wowAuthor.getCourseList();
            for (int i=0;i<v.size();i++) {
                xoutput.println("      <courseName>"+(String)v.elementAt(i)+"</courseName>");
            }
            xoutput.println("    </courseList>\n"+"  </author>");
          }
          xoutput.println("</authorList>");
          xoutput.close();
          try {
            foutput.close();
          } catch (Exception e) {
            System.err.println("Error closing config file for writing: "+e.getMessage());
            e.printStackTrace(System.err);
          }
        } catch (Exception e) {
          System.err.println("Error opening config file for writing: "+e.getMessage());
            e.printStackTrace(System.err);
        }
     }
   }

   public void LoadConfig() {

       new WowAuthorDOM(AuthorHash, authorlist);

   } // end of method

   public ArrayList<WowAuthor> getAuthorList() {
	   ArrayList<WowAuthor> authors = new ArrayList<WowAuthor>();
	   Enumeration enums = AuthorHash.elements();
	   while(enums.hasMoreElements()) {
		   authors.add((WowAuthor)enums.nextElement());
	   }
	   return authors;
   }
   
   public ArrayList<String> getAllCourses() {
	   ArrayList<String> courses = new ArrayList<String>();
	   
	   ArrayList<WowAuthor> authors = getAuthorList();
	   for(WowAuthor author : authors) {
		   Vector list = author.getCourseList();
		   for(int i = 0; i < list.size(); i++)
			   courses.add((String)list.get(i));
	   }
	   return courses;
   }
   
} // end of class