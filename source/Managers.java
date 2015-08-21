/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * Managers.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import vn.spring.WOW.config.*;

public class Managers extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean add = false;
	private boolean change = false;
	private String user = "";
	private String passwHashed = "";

  public Managers() {

  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(true);

    Boolean loggedin=(Boolean)session.getAttribute("loggedin");
    WowConfig conf=(WowConfig)session.getAttribute("config");
    if((loggedin==null) || (!loggedin.booleanValue()) || (conf==null)) response.sendRedirect("Config");
    else {
      response.setContentType("text/html;charset=UTF-8");
      response.addHeader("Cache-Control","no-cache");

  response.getWriter().write(" <html>\n"+
  " <head>\n"+
  " <title>WOW! Configuration Page</title>\n"+
  " \n"+
  " <link rel=\"stylesheet\" href=\"wow.css\">\n"+
          " <script language=\"javascript\"> <!--\n" +
          "   function checkpasswd() { \n"+
          "     if(document.manform.password.value==document.manform.password2.value) { \n"+
          "       document.manform.submit() \n"+
          "     } else { \n"+
          "       alert(\"Passwords are inequal\") \n" +
          "     } \n"+
          "   } \n"+
          " // -->\n" +
          " </script>\n" +
  " </head>\n"+
  " \n"+
  " <body>\n"+
  " <table>\n"+
  " <tr>\n"+
  " <td valign=\"top\">\n"+
  " \n"+
  " <!-- start menu -->\n"+
  " <h2>wow</h2>\n"+
  " <b>The WOW configurator</b>\n"+
  " <br><br>\n"+
  " <a href=\"ConfDB\">Configure DataBase</a><br>\n"+
  " <a href=\"Managers\" title=\"new managers and settings\">Manager Configuration</a><br>\n"+
  " <a href=\"Authors\" title=\"authors and settings\">Authors</a><br>\n"+
  " <a href=\"ConvertCL\" title=\"convert concept list\">Convert concept list from internal format to XML file</a><br>\n"+
  " <a href=\"ConvertXML\" title=\"convert XML file\">Convert concept list from XML file to internal format</a><br>\n"+
  " <a href=\"Logout\" title=\"Exit\">Logout</a><br>\n"+
  " <!-- end menu -->\n"+
  " </td>\n"+
  " <td>\n"+
  " <!-- start content -->\n"+
  " \n");
  WowManager victim=null;
  if (request.getParameter("action")==null) {
    WowManagerList manlist=new WowManagerList(conf.ManagerHash);
    // show\n"+
    response.getWriter().write(" <form method=POST>\n"+
    " <table>\n"+
    " <tr>\n"+
    " <th>Manager administration</th>\n"+
    " <td>\n"+
    " <select name=\"action\">\n"+
    " <option value=\"\">--choose an action--</option>\n"+
    " <option value=\"add\">Add a new user</option>\n"+
    " <option value=\"change\">Change an existing user</option>\n"+
    " <option value=\"remove\">Remove an existing user</option>\n"+
    " </select>\n"+
    " </td>\n"+
    " <td><input type=\"submit\"></td>\n"+
    " </tr>\n"+
    " <tr>\n"+
    " <th></th>\n"+
    " <td>\n"+
    " <select name=\"mlogin\">\n"+
    manlist.getOptionList()+
    " </select>\n"+
    " </td>\n"+
    " </tr>\n"+
    " </table>\n"+
    " </form>\n");

    } else
    // WARNING!
    // what is about to happen here is not suitable for the
    // poor eyes of little children or conservative programmers
    // Asumed is that no well-thinking human being will alter
    // the hashing function (for the sake of people like me).
    // Possibly I'm mistaken.

    // I would like a switch(String x) statement...

    switch(request.getParameter("action").hashCode()) {
      case -1361636432: //"change"
      	user = request.getParameter("mlogin");
      	change = true;
        //victim=conf.GetManager(request.getParameter("mlogin"));
        victim=conf.GetManager(user);
        passwHashed = victim.getHashed();
      case 96417: //"add"
        add = true;
        if (victim==null) {
          // add
          victim=new WowManager("","");
          victim.setUnHashed("");
        }

  response.getWriter().write(" \n"+
  " <html>\n"+
  " <head>\n"+
  " \n"+
  " <!-- implement stylesheet -->\n"+
  " <script language=\"javascript\"><!--\n"+
  " \n"+
  " //-->\n"+
  " </script>\n"+
  " \n"+
  " </head>\n"+
  " \n"+
  " <body>\n"+
  " \n"+
  " <form name=\"manform\" method=POST>\n"+
  " <input type=hidden name=\"action\" value=\"save\">\n"+
  " <table>\n"+
  " <tr>\n"+
  " <th>Manager information</th>\n"+
  " <td>\n"+
  " Login:\n"+
  " </td>\n"+
  " <td>\n"+
  " <input name=\"mlogin\" size=25 value='"+victim.getLogin()+"'>\n"+
  " </td>\n"+
  " <td><input type=\"button\" value=\"OK\" onclick=\"checkpasswd()\"></td>\n"+
  " </tr>\n");
  if (change) response.getWriter().write(" <br>You can change manager's password, but it will remain the same if you do not enter anything.\n");
  response.getWriter().write(" <tr>\n<tr>\n"+
  " <th></th>\n"+
  " <td>\n"+
  " Password:\n"+
  " </td>\n"+
  " <td>\n"+
  " <input type=password name=\"password\" size=25 value=\"\">\n"+
  " </td>\n"+
  " </tr>\n"+
  " <tr>\n"+
  " <th></th>\n"+
  " <td>\n"+
  " Password (verify):\n"+
  " </td>\n"+
  " <td>\n"+
  " <input type=password name=\"password2\" size=25 value=\"\">\n"+
  " </td>\n"+
  " </tr>\n"+
  " <tr>\n"+
  " <th></th>\n"+
  " <td>\n"+
  " Name:\n"+
  " </td>\n"+
  " <td>\n"+
  " <input name=\"name\" size=25 value='"+victim.getName()+"'>\n"+
  " </td>\n"+
  " </tr>\n"+
  " </table>\n"+
  " </form>\n"+
  " \n"+
  " </body>\n"+
  " </html>\n\n");
        break;
      case 3522941: // "save"
      	if (change) conf.RemoveManager(user);
      	if (add && conf.GetManager(request.getParameter("mlogin")) != null) {
		    response.getWriter().write(" <h2>Warning!</h2>\n"+
		    " Manager with login "+request.getParameter("mlogin")+" already exists.<br>\n"+
		    " Please, return back and try another login.\n");
		    break;
		}
        victim=new WowManager(request.getParameter("mlogin"),
                              request.getParameter("name"));
        if (change && request.getParameter("password").equals("")) victim.setHashed(passwHashed);
        else victim.setUnHashed(request.getParameter("password"));


        conf.PutManager(victim);
        // should some exception be caught here??
        response.getWriter().write("<h2>Stored</h2>\n<br>The data will be saved <b>ON LOGOUT</b>\n");
        change = false;
        break;
      case -934610812: // "remove"
      conf.RemoveManager(request.getParameter("mlogin"));
    response.getWriter().write(" <h2>Removed</h2>\n"+
    " Manager with login "+request.getParameter("mlogin")+" has been\n"+
    " removed from the Wow System, this alteration will be saved <b>ON LOGOUT.</b>\n");
        break;

      default:
    response.getWriter().write(" <h2>Default</h2>\n"+
  " not implemented, possibly hashing error\n"+
  " please use SUN JSDK or JRE v1.4, or\n"+
  " adjust the hashcodes in 'Managers.java'\n");
        break;
    } // end switch("action")

    response.getWriter().write(
   " \n <!-- end of content -->\n"+
   " </td>\n </tr>\n </table>\n"+
   " </body>\n </html>\n");

   } // if loggedin==null || loggedin==false || conf==null

  }
}
