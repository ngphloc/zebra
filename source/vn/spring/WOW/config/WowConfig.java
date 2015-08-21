/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * WowConfig.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

 // Functionality:
 //  The 'Get' Method of this object uses a string parameter: a variable name
 //    and returns a string value: the configuration value (if available)
 //    If the configuration value is not found within the config file then a null
 //    object is returned.
 //  The 'All' method returns a stringrepresentation of the configvalues according
 //    to the Hashtable.toString() method.
 //
 // Example:
 //
 //  WowConfig config=new WowConfig();
 //  String MyFileName = config.Get("WOWROOT")+"/docs/myfile.txt";
 //  String AllValues  = config.All();
 //
 //  (remember that using / or \ on the wrong platform MIGHT cause
 //   problems)

 // Imperfections/features/bugs:
 //  * there is no real handling of 'FileNotFound', it assumes the presence
//    of the configfile (logging is not implemented).

package vn.spring.WOW.config;

import java.io.*;

import java.util.*;

// some of the following can be removed
// i just honestly dont know which
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

public class WowConfig {
   public Hashtable ConfigHash,ManagerHash;
   public File CFile;

   static public String version="1.0";

   // create a new configfile
   public WowConfig() {
      ConfigHash=new Hashtable();
      ManagerHash=new Hashtable();
   }


   public WowConfig(String s) {
     if ((CFile=getConfigFile())==null) {
       // Wow! will not work when this happens, sorry!
       System.err.println("Config-file environment-entry is invalid!");
     } else {
        LoadConfig();
     }
   }

    //added by @David @03-06-2008
    File getStaticConfigFile() {return new File("c:/wow/WEB-INF/wowconfig.xml");}
    //end added by @David @03-06-2008

    //Loc Nguyen add 2009 September
    private File getConfigFile() {
		String configfile = null;
		
		javax.naming.Context ctx=null;
		javax.naming.Context env =null;
		try {
			ctx = new javax.naming.InitialContext();
			env = (javax.naming.Context)ctx.lookup("java:comp/env");
			configfile=(String) env.lookup("ConfigFile");
		}
		catch (Exception e) {
			System.out.println("WowConfig: getConfigFile->Naming lookup causes error: "+ e.getMessage() +
					". So try to load zebra.properties so as to load");
			configfile = null;
		}
		if(configfile == null) {
			try {
				Properties props = new Properties();
				props.load(this.getClass().getResourceAsStream("/vn/spring/zebra/zebra.properties"));
				configfile = props.getProperty("wow.configfile");
			}
			catch (Exception e) {
				System.out.println("WowConfig: getConfigFile->getResourceAsStream cause error: " + e.getMessage());
				configfile = null;
			}
		}
		
		if(configfile == null) return null;
		else                   return new File(configfile);
    }

   // search our configfile
   File SearchFile(String filename,String path, String delimiter) {
      boolean found = false;
      File Check=null;


      StringTokenizer splitpath = new StringTokenizer(path,delimiter);
      while(splitpath.hasMoreTokens() && (!found)) {
         try {
           Check=new File((splitpath.nextToken()),filename);
           found=Check.canWrite(); // should be able to write
         } catch (Exception e) {
           //IOException : something with the filesystem
           //SecurityException: no read permission, quite usefull
           //FileNotFound: ....
           System.err.println("Error while searching config file");
           found=false;
         }
      }
      return Check;
    }

   // set the filename if the config object is new
   public void setFileName(String s) {
      CFile=new File(s);
   }

   // fetch the string to be searched for
   public String Get(String s) {
     if (s!=null) {
        return (String)ConfigHash.get(s);
     } else {
       return null;
     }
   }

   public void Put(String s,String v) {
     if ((s!=null) && (v!=null)) {
        ConfigHash.put(s,v);
     }
   }


   public String All() {
     return ConfigHash.toString();
   }

   public WowManager GetManager(String l) {
     if (l!=null) {
        return (WowManager)ManagerHash.get(l);
     } else {
       return null;
     }
   }

   public void PutManager(WowManager m) {
     if (m!=null) {
        ManagerHash.put(m.login,m);
     }
   }

   // be warned PARAMETER TYPE = STRING
   public void RemoveManager(String m) {
     if ((m!=null) && ManagerHash.containsKey(m)) {
       ManagerHash.remove(m);
     }
   }

   public void StoreConfig() {
     WowManager man;
     FileOutputStream foutput;

     if (CFile!=null) {
     // open ConfigFile
        try {
          foutput=new FileOutputStream(CFile);
          PrintWriter xoutput=new PrintWriter(foutput);

          xoutput.println("<!-- Autogenerated by Cylon, the Wow Configuration Tool-->");
          xoutput.println("<WowConfig>");
          xoutput.println("  <Settings>");



          for (Iterator i = ConfigHash.entrySet().iterator() ; i.hasNext() ;) {
            Map.Entry m=(Map.Entry)i.next();
            xoutput.println("    <Variable id=\""+(String)m.getKey()+"\">"+(String)m.getValue()+"</Variable>");
          }
          xoutput.println("  </Settings>");
          xoutput.println("  <Access>");
       // for each (key,man) in ManagerHash
       // do printf("<Variable login=\"%s\" password=\"%s\">%s</Variable>\n",
          for (Enumeration e = ManagerHash.elements() ; e.hasMoreElements() ;) {
            man=(WowManager)e.nextElement();
            xoutput.print("    <User login=\""+man.getLogin()+"\" password=\"");
            xoutput.println(man.getHashed()+"\">"+man.getName()+"</User>");
          }
       // done
          xoutput.println("  </Access>");
          xoutput.println("</WowConfig>");
          xoutput.close();
          try {
            foutput.close(); // are 2 close actions required??
          } catch (Exception e) {
            // if file could not be closed
            //IOException : something with the filesystem?
            System.err.println("Error closing config file for writing: "+e.getMessage());
            e.printStackTrace(System.err);
          }
        } catch (Exception e) {
           // if file could not be opened
          //IOException : something with the filesystem?
          System.err.println("Error opening config file for writing: "+e.getMessage());
            e.printStackTrace(System.err);
        }
     }
   }

   public void LoadConfig() {
     // clear the hashtable,
     ConfigHash=new Hashtable();
     ManagerHash=new Hashtable();
     WowConfigSAX child= new WowConfigSAX(ConfigHash,ManagerHash);

     // start running the search and searching/opening the configfile
     SAXParserFactory factory = SAXParserFactory.newInstance();
     try {
        SAXParser saxParser = factory.newSAXParser();
    try {
        saxParser.parse(CFile.getPath(),child);
        } catch (Throwable e) {
            saxParser.parse(CFile.toURL().toString(),child);
    }
     } catch (Throwable e) {
        // decide what to do here: create some kind of errorlog
        // a logging function would be very advisable
        // usually this can be ignored, if not -> you're screwed
        System.err.println("Error parsing Config file : "+e.getMessage());
        e.printStackTrace(System.err);
     }

     if (ConfigHash==null) {
        System.err.println("Error parsing Config file : empty configuration table");
     }
   } // end of method

} // end of class
