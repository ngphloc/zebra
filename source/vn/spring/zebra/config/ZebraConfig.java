/**
 * ZebraConfig.java 2009
 *
 * Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

/**
 * Main class for the configuration. Developed by Eindhoven University and modified by Loc Nguyen 2009 because this file is very important
 *
 */

package vn.spring.zebra.config;

import java.io.*;

import java.security.MessageDigest;
import java.util.*;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ZebraConfig {
	protected Hashtable ConfigHash = new Hashtable();
	protected Hashtable ManagerHash = new Hashtable();
	protected File configFile = null;

	public ZebraConfig() {
		if ((configFile = getConfigFile())==null) {
			System.err.println("Config-file environment-entry is invalid!");
		}
		else {
			LoadConfig();
		}
	}

	private File getConfigFile() {
		String configfile = null;

		javax.naming.Context ctx=null;
		javax.naming.Context env =null;
		try {
			ctx = new javax.naming.InitialContext();
			env = (javax.naming.Context)ctx.lookup("java:comp/env");
			configfile=(String) env.lookup("ZebraConfigFile");
		}
		catch (Exception e) {
			System.out.println("ZebraConfig: getConfigFile->Naming lookup causes error: "+ e.getMessage() +
					". So try to load zebra.properties so as to load");
			configfile = null;
		}
		if(configfile == null) {
			try {
				Properties props = new Properties();
				props.load(this.getClass().getResourceAsStream("/vn/spring/zebra/zebra.properties"));
				configfile = props.getProperty("zebra.configfile");
			}
			catch (Exception e) {
				System.out.println("ZebraConfig: getConfigFile->getResourceAsStream cause error: " + e.getMessage());
				configfile = null;
			}
		}
		
		if(configfile == null) return null;
		else                   return new File(configfile);
	}


   // fetch the string to be searched for
	public String Get(String s) {
		if (s != null) {
			return (String)ConfigHash.get(s);
		}
		else {
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

	public ZebraManager GetManager(String l) {
		if (l != null) {
			return (ZebraManager)ManagerHash.get(l);
		}
		else {
			return null;
		}
	}

	public void PutManager(ZebraManager m) {
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
	   ZebraManager man;
	   FileOutputStream foutput;

	   if (configFile!=null) {
		   // open ConfigFile
		   try {
			   foutput=new FileOutputStream(configFile);
			   PrintWriter xoutput=new PrintWriter(foutput);

			   xoutput.println("<!-- Autogenerated by Cylon, the Zebra Configuration Tool-->");
			   xoutput.println("<ZebraConfig>");
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
				   man=(ZebraManager)e.nextElement();
				   xoutput.print("    <User login=\""+man.getLogin()+"\" password=\"");
				   xoutput.println(man.getHashed()+"\">"+man.getName()+"</User>");
			   }
			   // done
			   xoutput.println("  </Access>");
			   xoutput.println("</ZebraConfig>");
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
	   ZebraConfigSAX child= new ZebraConfigSAX(ConfigHash,ManagerHash);
	   
	   // start running the search and searching/opening the configfile
	   SAXParserFactory factory = SAXParserFactory.newInstance();
	   try {
		   SAXParser saxParser = factory.newSAXParser();
		   try {
			   saxParser.parse(configFile.getPath(),child);
		   }
		   catch (Throwable e) {
			   saxParser.parse(configFile.toURL().toString(),child);
		   }
	   }
	   catch (Throwable e) {
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

class ZebraConfigSAX extends DefaultHandler {
	private Hashtable ConfigHash,ManagerHash;
	public String ConfigFile;

	// constructor
	public ZebraConfigSAX(Hashtable c,Hashtable m) {
		ConfigHash=c;
		ManagerHash=m;
	}

	// -------------------------
	// Starting handler section
	// -------------------------

	private boolean found;
	private String VarName;
	private String VarPass;
	public  String VarValue;
	private StringBuffer ValueBuffer;
	private Stack TreePath = new Stack();

	public void startElement(String namespaceURI,
	                         String sName, // simple name
	                         String qName, // qualified name
	                         Attributes attrs) throws SAXException {
	      // check whether we are within a 'ZebraConfig' or in a 'Access' tag
		TreePath.push(qName);

		found=false;
		if (TreePath.toString().equals("[ZebraConfig, Settings, Variable]")) {
			if (attrs != null)
				for (int i = 0; i < attrs.getLength(); i++)  {
					if ("id".equals(attrs.getQName(i))) {
						VarName = attrs.getValue(i);
						found = true;
					}
				}
		} else if (TreePath.toString().equals("[ZebraConfig, Access, User]")) {
			VarName=null; VarPass=null; // one may not be altered
			if (attrs != null)
				for (int i = 0; i < attrs.getLength(); i++)  {
					if ("login".equals(attrs.getQName(i))) {
						VarName = attrs.getValue(i);
						found = true;
	                }
	                if ("password".equals(attrs.getQName(i))) {
	                	VarPass=attrs.getValue(i);
	                }
				}
		} // else not a <Variable> or <User> tag
	}

	// check whether these character intervals are not chunked (have to be
	// glued). This can be tested using a file with an extremely large config value
	public void characters(char content[], int start, int len) throws SAXException {
		// check if where within a 'Variable' tag (otherwise newlines and other tags are included)
		if (found) {
			String s = new String(content, start, len);
			if (ValueBuffer == null) {
				ValueBuffer = new StringBuffer(s);
			} else {
				ValueBuffer.append(s);
			}
		}

	}

	public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
		if (found && TreePath.toString().equals("[ZebraConfig, Settings, Variable]")) {
			ConfigHash.put(VarName,ValueBuffer.toString());
		}
		else if (found && TreePath.toString().equals("[ZebraConfig, Access, User]")) {
			ZebraManager man=new ZebraManager(VarName,ValueBuffer.toString());
			// is there ALWAYS a password??
			man.setHashed(VarPass);
			ManagerHash.put(VarName,man);
		}
		found=false;
		ValueBuffer = null;

		String lastOne=(String)TreePath.pop();
		if (qName!=lastOne) System.err.println(qName+" does not match with tag "+lastOne);

	}

	// on document-start
	public void startDocument() throws SAXException {
		// we get the hashtable from the
		// constructor
	}

	// on document-end
	public void endDocument() throws SAXException {
	}
}

class ZebraManager {
	// it can be made possible to add
	// some kind of hierarchy, but in the
	// meantime that doesn't sound neccesary
	public String passwd; //this is hashed or encrypted password
	public String name;
	public String login;

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

	public ZebraManager() {
	}

	// the reason the password is not set is
	// the risk of entering the unhash password
	// instead of the hashed, or vice-versa
	public ZebraManager(String l,String n) {
		login = l;
		name = n;
		passwd = null;
	}

	public void setUnHashed(String unhashed) {
		passwd = getHashString(unhashed);
	}

	public void setHashed(String hashed) {
		passwd = hashed;
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

}