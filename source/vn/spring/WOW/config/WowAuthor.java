/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * WowAuthor.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.config;

import java.security.MessageDigest;
import java.util.Vector;


public class WowAuthor {

   public String passwd; //this is hashed or encrypted password
   public String name;
   public String login;
   private Vector courseList=new Vector();

   public String showHashString(String unhashed) {
     return getHashString(unhashed);
   }

   //
   // advised are MD5 of SHA1
   private String getHashString(String unhashed) {
     // generate a string hash-representation of a string
     try {
       MessageDigest md=MessageDigest.getInstance("MD5");
       StringBuffer hexdigest = new StringBuffer();
       hexdigest.append("MD5:"); // state the used algorithm
       String byteHex;
       byte[] digest=md.digest(unhashed.getBytes());

       for (int i=0; i < digest.length; i++) {
         byteHex = Integer.toHexString(digest[i] & 0xFF); // know no (unsigned) cast
         if (byteHex.length() == 1) hexdigest.append("0" + byteHex);
         else hexdigest.append(byteHex);
      }
      return hexdigest.toString();

     } catch (Exception e) {
       System.err.println("MD5 could not be instantiated, using standard hash which is XTREMELY UNSAFE");
       return "HC:"+Integer.toHexString(unhashed.hashCode());
     }
   }

   public WowAuthor() {

   }

   // the reason the password is not set is
   // the risk of entering the unhash password
   // instead of the hashed, or vice-versa

   public WowAuthor(String l,String n) {
      login=l;
      name=n;
      passwd=null;
   }


   public void setUnHashed(String unhashed) {
      passwd=getHashString(unhashed);
   }

   public void setHashed(String hashed) {
      passwd=hashed;
   }

   // this function is usefull for writing down
   // the user-information
   public String getHashed() {
      return passwd;
   }

   public boolean checkPasswd(String unhashed) {
     return passwd.equals(getHashString(unhashed));
   }

   public void setLogin(String l) {
     login=l;
   }

   public String getLogin() {
     return login;
   }

   public void setName(String n) {
     name=n;
   }

   public String getName() {
     return name;
   }

   public void setCourseList(Vector v) {
     courseList=v;
   }

   public Vector getCourseList() {
     return courseList;
   }

}
