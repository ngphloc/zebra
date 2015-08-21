/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * WOWStatic.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
package vn.spring.WOW;

import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.WOWDB.*;
import vn.spring.WOW.engine.*;
import vn.spring.WOW.config.*;
import vn.spring.WOW.Layout.*;
import vn.spring.WOW.parser.*;
import vn.spring.zebra.helperservice.WOWContextListener;

import java.io.*;
import java.net.URL;
import java.util.*;
import javax.servlet.http.*;

import org.w3c.dom.*;

/**
 * This is a utility class. It contains only static methods and
 * variables. It maintains a permanent instance of the
 * <code>ResourceManager</code>. And a function to obtain a unique
 * identifier that is used by the XMLDB components.
 * Adapted to work with WowConfig class by Rob
 * @author David Smits
 * @version 1.0
 * @see vn.spring.WOW.ResourceManager
 */
 /**
   * changes by Natalia Stash, reviewed and modified by Loc Nguyen, 01-05-2008
   * added
   * public static String normalize(String s)
   * public static Object[] names(Vector v)
*/
public class WOWStatic {

    public static WowConfig config = new WowConfig("wowconfig.xml");

    //constants
    public static final String AUTHORFILESPATH = "/author/authorfiles/";
    public static final String BAYESAUTHORPATH = "/author/BayesAuthor/";
    public static final String FORMEDITORPATH = "/author/FormEditor/";
    public static final String FORMPATH = "/form/";
    public static final String GENLISTEDITORPATH = "/author/GenerateList_Editor/";
    public static final String GRAPHAUTHORPATH = "/author/GraphAuthor/";
    public static final String TESTEDITORPATH = "/author/TestEditor/";
    public static final String AMTPATH = "/author/AMt/";

    //visibility class constants
    public static final int VC1 = 10;
    public static final int VC2 = 20;
    public static final int VC3 = 30;
    public static final int VC4 = 40;
    public static final int VC5 = 50;
    public static final int VC6 = 60;
    public static final int VC7 = 70;
    public static final int VC8 = 80;
    public static final int VC9 = 90;
    public static final int VC10 = 100;

    // Find the root for the XML database implementation
    public final static File root = new File(config.Get("XMLROOT"));

    // Added by @Tomi (WOW-Pitt integration)

       //Name of the active course
       public static String courseName=null;

       // The root for the XML database implementation
       public final static File WOWroot = new File(config.Get("WOWROOT"));

       // The root for the concept directory
       public final static File conceptRoot = new File(root, "concept");

       public static CourseInfoTbl CourseInfoTbl=new CourseInfoTbl();

       //MainView background (temporary Hack solution)
       public static String mainBg=null;

  // End Tomi



    // If this class is properly defined (initialized)
    private static boolean defined = false;

    // Global object identifier, increases all the time
    static long uid = 0;


    public static Object lock = new Object();

    // Defines the class properly
    private static void define() {
        boolean succes = true;
        String jdbcDriver=config.Get("jdbcDriver");
        String jdbcUrl=config.Get("jdbcUrl");
        String muser=config.Get("dbuser");
        String mpassword=config.Get("dbpassword");

        if ((succes) && (_DB == null)) try {
          if (config.Get("dbimpl").equals("xml")) {
            _DB = new WOWDB(new XMLConceptDB(root), new XMLProfileDB(root),
                            new XMLLogDB(root));
          } else {
            // Added by @Zdenek - connection pool init
            ConnectionPool.init(jdbcDriver, jdbcUrl, muser, mpassword);
            // End Zdenek
            _DB = new WOWDB(new DBConceptDB(jdbcUrl, muser, mpassword),
                            new DBProfileDB(jdbcUrl, muser, mpassword),
                            new DBLogDB(jdbcUrl, muser, mpassword));
          }

        } catch (DatabaseException e) {succes = false; _DB = null;}

        if ((succes) && (_PM == null)) _PM = new ProfileManager(_DB);
        if ((succes) && (_HM == null)) _HM = new HandlerManager(_DB);
        _HM.addHandler(new SerializeHandler());
        _HM.addHandler(new XHTMLHandler());
        _HM.addHandler(new XMLHandler());
        _HM.addHandler(new HTMLHandler());
        _HM.addHandler(new CSSHandler());


    //Added by @Tomi(WOW-Pitt integration)
    //Instance of ViewManager
        _VM=new ViewManager();
        //End Tomi

        defined = succes;
    }

    /**
     * A reference to an instance of <code>WOWDB</code>.
     * @see vn.spring.WOW.WOWDB.WOWDB
     */
    public static WOWDB DB() {if (!defined) define(); return _DB;}
    private static WOWDB _DB = null;

    /**
     * A reference to an instance of the <code>HandlerManager</code>.
     * @see vn.spring.WOW.engine.HandlerManager
     */
    public static HandlerManager HM() {if (!defined) define(); return _HM;}
    private static HandlerManager _HM = null;

    /**
     * A reference to an instance of the <code>ProfileManager</code>.
     * @see vn.spring.WOW.engine.ProfileManager
     */
    public static ProfileManager PM() {if (!defined) define(); return _PM;}
    private static ProfileManager _PM = null;

    //Added by @Tomi(WOW-Pitt integration)
    /* A reference to an instance of the <code>ViewManager</code>.*/
    public static  ViewManager VM() {if (!defined) define(); return _VM;}
    private static ViewManager _VM = null;
    //end Tomi


    //A reference to the file used to store the next UID
    private final static File uidData = new File(config.Get("UIDFILE"));

    /**
     * Returns a unique identifier. This unique number is read from
     * the file (config.Get(UIDFILE)). The number is
     * increased by one and then written back to the file.
     * @return the UID, or 0 if an error occurred.
     * This is a critical area: must be synchronized!
     */

    public static synchronized long getUID() {

        try {
            FileWriter out = null;
            FileReader in = null;

            //Check if the directory and the file exist, else make them
            if (!uidData.getParentFile().exists()) uidData.getParentFile().mkdir();
            if (!uidData.exists()) {
                //Create a new file with number 0
                out = new FileWriter(uidData);
                out.write("0", 0, 1);
                out.close();
            }

            //Read the number from the file
            // only if not yet known
            if (uid == 0) {
                in = new FileReader(uidData);
                StringBuffer num = new StringBuffer();
                while (in.ready()) num.append((char)in.read());
                in.close();

//changed by David 7-2-2008
                String nums = num.toString();
                if (nums.indexOf("\n")!=-1) nums = nums.substring(0,nums.indexOf("\n"));
                nums = nums.trim();
                uid = new Long(nums).longValue();
//end changed by David 7-2-2008
            }
            uid++;

            //Write the number back to the file
            String outnum = new Long(uid).toString();
            out = new FileWriter(uidData);
            out.write(outnum, 0, outnum.length());
            out.close();
        } catch (Exception e) {
            //If any error occurred return 0
            return 0;
        }

        return uid;
    }

    // this method replaces all dots in identifiers by underscores,
    // for use in the JEP parser. note that this leaves dots in numbers.
    // note that we do not need the reverse as that is a simple 'replace'
/*    public static String underscoreString(String in) {
        if (in == null || in.equals("")) return "";
        StringBuffer sb = new StringBuffer(in);
        int j;
        for (int i=0;i<sb.length();i++)
        if ((sb.charAt(i) == '.') && (i>0)) {
        for (j=i-1;!(j<0) && Character.isDigit(sb.charAt(j));j--);
            if (!(j<0) && (Character.isLetter(sb.charAt(j))))
        sb.setCharAt(i, '_');
        }
        return sb.toString();
    }
*/
    //this method replaces all underscores by dots for use in the JEP parser
/*    public static String dotString(String in) {
        if (in == null) return null;
        StringBuffer sb = new StringBuffer(in);
        for (int i=0;i<sb.length();i++) if (sb.charAt(i) == '_') sb.setCharAt(i, '.');
        return sb.toString();
    }
*/
    public static void showError(HttpServletResponse response, String message, boolean normalize) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(response.getWriter());
        } catch (IOException e) {throw new IllegalStateException("WOWStatic.showError: Unable to obtain PrintWriter.");}
        response.setContentType("text/html;charset=UTF-8");
        if (normalize) message = normalize(message);
        out.println("<h1>WOW Error</h1>");
        out.println("<p>"+ message + "</p>");
        out.close();
    }

    public static void checkNull(Object object, String name) {
        if (object == null) throw new
            NullPointerException(object.getClass().getName() + " object '" + name + "' is null");
    }

    // "normalize" replaces some characters that have a special
    // meaning by escape codes. this is sometimes needed.
    public static String normalize(String s) {

        StringBuffer buffer = new StringBuffer();
        int len = (s != null) ? s.length() : 0;
        for ( int i = 0; i < len; i++ ) {
            char ch = s.charAt(i);
            switch ( ch ) {

                case '<': {
                    buffer.append("&lt;");
                    break;
                }

                case '>': {
                    buffer.append("&gt;");
                    break;
                }

                case '&': {
                    buffer.append("&amp;");
                    break;
                }

                case '"': {
                    buffer.append("&quot;");
                    break;
                }

//                case '©': {
//                    buffer.append("&copy;");
//                    break;
//                }

                case '\'': {
                    buffer.append("&apos;");
                    break;
                }

                default: {
                    buffer.append(ch);
                }
            }
        }
        return(buffer.toString());
    }

    // The following utility creates a sorted list of names
    // from a vector of objects
    // PDB: I think this needs to be at least improved,
    // but probably moved elsewhere too.
    public static Object[] names(Vector v) {

        Object[] names = new Object[v.size()];
        for (int i=0; i<v.size(); i++) {
            names[i] = v.elementAt(i);
        }

        for ( int i = 0; i < v.size() - 1; i++ ) {
            String name  = names[i].toString();
            int    index = i;
            for ( int j = i + 1; j < v.size(); j++ ) {
                String curName = names[j].toString();
                if ( curName.compareTo(name) < 0 ) {
                    name  = curName;
                    index = j;
                }
            }
            if ( index != i ) {
                Object temp    = names[i];
                names[i]     = names[index];
                names[index] = temp;
            }
        }
        return names;
    }

    // PDB: Check whether a string contains non-ascii chars.
    // Since strings are often put in XML files we must avoid
    // putting non-ascii symbols in the strings.

    public static boolean checkascii(String s) {
    int i;
    for (i=0; i<s.length(); ++i)
        if ((int)(s.charAt(i))>=128)
        return false;
    return true;
    }

    public static UMVariableLocator createUMVariableLocator(Profile profile) {
        return new UMVariableLocator(profile, DB().getConceptDB());
    }

    public static Parser createUMParser(Profile profile) {
        return new Parser(createUMVariableLocator(profile));
    }

    /**
     * Determines if logging should be used. This depends on the
     * global logging flag and on the individual user settings.
     * @param profile The current user profile whose personal
     *                settings should be evaluated.
     * @return If logging should be used.
     */
    public static boolean useLogging(Profile profile) {
        boolean globallog = new Boolean(WOWStatic.config.Get("logging")).booleanValue();
        boolean locallog = false;
        try {
            locallog = new Boolean(profile.getAttributeValue("personal", "logging")).booleanValue();
        } catch (Exception e) {}
        return (globallog || locallog);
    }

    /**
     * Fixes the expression by replacing partial identifiers with
     * full identifiers.
     * @param expr The expression to fix.
     * @param appname The name of the current application.
     * @param conceptname The name of the current concept (without application name!).
     * @return The fixed expression.
     */
    public static String fixExpr(String expr, String appname, String conceptname) {
        vn.spring.WOW.genparser.GenParser parser = new vn.spring.WOW.genparser.GenParser();
        parser.registerParseComponent(new vn.spring.WOW.genparser.PCIdentifier());
        parser.registerParseComponent(new vn.spring.WOW.genparser.PCCommon());
        parser.registerParseComponent(new vn.spring.WOW.genparser.PCSingleQuote());
        vn.spring.WOW.genparser.ParseNode node;
        try {
            node = parser.parse("EXPR", expr);
        } catch (Exception e) {
            e.printStackTrace();
            return expr;
        }
        fixIDs(node, appname, conceptname);
        return node.toString();
    }

    private static void fixIDs(vn.spring.WOW.genparser.ParseNode node, String appname, String conceptname) {
        if (node instanceof vn.spring.WOW.genparser.PCIdentifier.IDNode) fixID((vn.spring.WOW.genparser.PCIdentifier.IDNode)node, appname, conceptname); else {
            LinkedList children = node.getChildList();
            for (int i=0;i<children.size();i++) fixIDs((vn.spring.WOW.genparser.ParseNode)children.get(i), appname, conceptname);
        }
    }

    private static void fixID(vn.spring.WOW.genparser.PCIdentifier.IDNode idnode, String appname, String conceptname) {
        if (idnode.name.indexOf(".") == -1) idnode.name = conceptname+"."+idnode.name;
        //check if it is the personal concept
        if (idnode.name.toLowerCase().startsWith("personal.")) return;
        //check if the concept name exists in the database
        boolean exists = true;
        String cname = idnode.name.substring(0, idnode.name.lastIndexOf("."));
        try {DB().getConceptDB().findConcept(cname);} catch (Exception e) {exists = false;}
        if (!exists) idnode.name = appname+"."+idnode.name;
    }

    /**
     * Returns the application name from the full concept name that
     * is given. This is the part of the concept name before the
     * first period.
     * @param fullconcept The full concept name to use.
     * @return the application name.
     */
    public static String appNameFromConcept(String fullconcept) {
        int index = fullconcept.indexOf(".");
        if (index > 0) return fullconcept.substring(0, fullconcept.indexOf(".")); else return "";
    }

    public static NodeList getChildElementsByTagName(Element element, String name) {
        NodeList children = element.getChildNodes();
        Vector v = new Vector();
        for (int i=0;i<children.getLength();i++) {
            Node n = children.item(i);
            if (n instanceof Element) if ( ((Element)n).getTagName().equals(name) ) v.add(n);
        }
        return new VectorNodeList(v);
    }

	public static void logoff(HttpSession session) {
		String userid = (String)session.getAttribute("login");
		String course = (String)session.getAttribute("course");
		String activeconcept = (String)session.getAttribute("activeconcept");
		
		if(userid == null || course == null) return;
		WOWContextListener.getInstance().getTriUMServer().unmonitorUM(userid, course);
		
		if(activeconcept == null) return;
		try {
			ProfileDB pdb = WOWStatic.DB().getProfileDB();
			ConceptDB cdb = WOWStatic.DB().getConceptDB();
			LogDB ldb = WOWStatic.DB().getLogDB();
			
			Profile profile = pdb.getProfile(pdb.findProfile(userid));
			Concept concept = cdb.getConcept(cdb.findConcept(activeconcept));
            UMVariableLocator umvl = new UMVariableLocator(profile, cdb);
            URL URI = concept.getActiveURL(umvl);
            if (URI != null) {
            	ldb.addAccessLog(URI.toString() + "|logoff", profile.getAttributeValue("personal", "id"), session.getId(), false);
    	        System.out.println("Write sucessfully concept|logoff in log file");
            }
			
			DotString ds = new DotString(activeconcept);
			if(ds.size() != 2 || !ds.get(0).equals(course)) return;
        	profile.setAttributeValue("personal", "start", ds.get(1));
        	pdb.setProfile(profile.id, profile);
	        System.out.println("Write sucessfully personal.start");
		}
		catch(Exception e) {
			System.out.println("Log off causes error: " + e.getMessage());
		}
		session.removeAttribute("login");
		session.removeAttribute("course");
		session.removeAttribute("activeconcept");
		session.invalidate();
	}

	private static class VectorNodeList implements NodeList {
        private Vector v;
        public VectorNodeList(Vector v) {this.v=v;}
        public int getLength() {return v.size();}
        public Node item(int index) {return (Node)v.get(index);}
    }
    
}
