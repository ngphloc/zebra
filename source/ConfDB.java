/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ConfDB.java 1.0, August 30, 2008.
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import vn.spring.WOW.config.*;
public class ConfDB extends HttpServlet {
	private static final long serialVersionUID = 1L;

  public PrintWriter httpOut;

  private String jdbcUrl = "";
  private String dbuser = "";
  private String dbpassword = "";
  private String olddbimpl = "";

  public ConfDB() {

  }

  private void printHead() {
     httpOut.write("<html>\n <title>WOW! Configuration Page</title>\n <head>\n  <link rel=\"stylesheet\" href=\"wow.css\">\n"+
          " <script language=\"javascript\"> <!--\n" +
          "   function checkpasswd() { \n"+
          "     if(document.dbform.dbpasswd.value==document.dbform.dbpasswd2.value) { \n"+
          "       document.dbform.submit() \n"+
          "     } else { \n"+
          "       alert(\"Passwords are inequal\") \n" +
          "     } \n"+
          "   } \n"+
          " // -->\n" +
          " </script>\n" +
          " </head>\n"+
          " <body onload=\"\">\n\n <table>\n <tr> <td valign=\"top\">\n");

  }
  private void printMenu() {
     httpOut.write("<!-- menu -->\n<h2>wow</h2>\n<b>The WOW configurator</b>\n<br><br>\n"+
         " <a href=\"ConfDB\">Configure DataBase</a><br>\n"+
         " <a href=\"Managers\" title=\"new managers and settings\">Manager Configuration</a><br>\n"+
         " <a href=\"Authors\" title=\"authors and settings\">Authors</a><br>\n"+
         " <a href=\"ConvertCL\" title=\"convert concept list\">Convert concept list from internal format to XML file</a><br>\n"+
         " <a href=\"ConvertXML\" title=\"convert XML file\">Create concept list from XML file to internal format</a><br>\n"+
         " <a href=\"Logout\" title=\"Exit\">Logout</a><br>"+
         " <!-- end menu -->");
  }

  private void printFinished() {
      httpOut.write("<h2>Database</h2>\n"+
       "Your WOW database settings are registered.<br>\n"+
       "Database implementation will be set <b>ON LOGOUT</b>.<br>\n");
  }
  private void printConvertToSQL() {
      httpOut.write("<p> \n"+
       "<h2>Conversion</h2>\n"+
       "Database implementation used to be XML, do you want me to convert<br>\n"+
       "the XML data to the database server? <p>\n"+
       "<a href=\"convertDB?from=xml&to=db&jdbcUrl=" + jdbcUrl + "?dbuser=" + dbuser + "&dbpassword=" + dbpassword + "\">Yes, please!</a>\n");
  }
  private void printConvertToXML() {
       httpOut.write(" <p> \n"+
       " <h2>Conversion</h2>\n"+
       " Database implementation used to be Database server, do you want me to convert\n"+
       " the Database server to the XML files? <p>\n"+
       " <a href=\"convertDB?from=db&to=xml&jdbcUrl=" + jdbcUrl + "&dbuser=" + dbuser + "&dbpassword=" + dbpassword + "\">Yes, please!</a>\n");
  }

  public void doGet(HttpServletRequest request, HttpServletResponse  response)
      throws ServletException, IOException {
    doPost(request,response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse  response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(true);
    httpOut = response.getWriter();

    String checkXML,checkDB;
    Boolean loggedin=(Boolean)session.getAttribute("loggedin");
    WowConfig conf=(WowConfig)session.getAttribute("config");

    if((loggedin==null) || (!loggedin.booleanValue()) || (conf==null)) response.sendRedirect("Config");
    else {
      response.setContentType("text/html;charset=UTF-8");
      response.addHeader("Cache-Control","no-cache");
      printHead();
      printMenu();
      httpOut.write(" </td> <td>\n <!-- start content -->\n");

    if (request.getParameter("dbimpl")!=null) {
       // submit! Store the stuff in conf
      olddbimpl=conf.Get("dbimpl");
      jdbcUrl = request.getParameter("jdbcUrl").trim();
      dbuser = request.getParameter("dbuser").trim();
      dbpassword = request.getParameter("dbpassword").trim();
      if (request.getParameter("dbimpl").equals("db")) {
        conf.Put("dbimpl","db");
        conf.Put("jdbcUrl", jdbcUrl);
        conf.Put("dbuser", dbuser);
        conf.Put("dbpassword", dbpassword);
        String doSetup=request.getParameter("dbsetup");
        if((doSetup!=null) && doSetup.equals("on")) {
      // 1: check if user has a correct database OR FULL access
          DBSetup sqlSetup=new DBSetup(jdbcUrl, dbuser, dbpassword);
          String rootPassword=request.getParameter("rootpassword");
          if(rootPassword==null) {
            // no root password available, try our own first

            boolean hasaccess; // can the use create own tables or is root-connection required

            try {
              sqlSetup.openUserConnection();
              sqlSetup.TableSetup();
              sqlSetup.closeConnection();
              hasaccess=true;
            } catch(Exception e) {
              // was to be expected ....
              // first create user etc.... with root password
              hasaccess=false;
            }

            if(!hasaccess) {
/*            httpOut.write( "<center><h2>Root access</h2></center>\n"+
   "To setup the WOW!-Database Server a root password seems required. Please enter the\n"+
   "root-password for the Database Server\n"+
        "<form method=POST>\n"+
        "  <input type=hidden name=\"dbimpl\" value=\"db\">\n"+
        "  <input type=hidden name=\"jdbcUrl\" value=\""+request.getParameter("jdbcUrl")+ "\">\n" +
        "  <input type=hidden name=\"dbuser\" value=\""+request.getParameter("dbuser")+" \">\n" +
        "  <input type=hidden name=\"dbpassword\" value=\""+request.getParameter("dbpassword")+ "\">\n" +
        "  <input type=hidden name=\"dbsetup\" value=\"on\">\n"+
        "  Root password <input type=password name=\"rootpassword\"><br>\n"+
        "  <input type=submit>\n"+
        "</form>\n");*/
            httpOut.write( "<center><h2>Root access</h2></center>\n"+
   "To setup the WOW!-database server a root password seems required. Please enter the\n"+
   "root-password for the Database server\n"+
        "<form method=POST>\n"+
        "  <input type=hidden name=\"dbimpl\" value=\"db\">\n"+
        "  <input type=hidden name=\"jdbcUrl\" value=\""+jdbcUrl+ "\">\n" +
        "  <input type=hidden name=\"dbuser\" value=\""+dbuser+" \">\n" +
        "  <input type=hidden name=\"dbpassword\" value=\""+dbpassword+ "\">\n" +
    "  <input type=hidden name=\"dbsetup\" value=\"on\">\n"+
    "  Root password <input type=password name=\"rootpassword\"><br>\n"+
    "  <input type=submit>\n"+
        "</form>\n");
            }
            else { // database has been created
              httpOut.write("<h2>DataBase created</h2>\nThe WOW/Database Server has been configured! Ready to go...");
              if ((olddbimpl!=null) && olddbimpl.equals("xml"))  // switched from XML to Database: convert??
                 printConvertToSQL();
            }
          } else {
           StringBuffer messages=new StringBuffer();
       // connect via root, got password
       sqlSetup.setRootPasswd(rootPassword);
           if(request.getParameter("dbClient")!=null) sqlSetup.setDBClient(request.getParameter("dbClient"));

           try {
             sqlSetup.openRootConnection();
           } catch (SQLException e) {
               e.printStackTrace();
             messages.append("<b>Error:</b> User root could not connect to Database server<br><em>"+e.getMessage()+"</em><br>");
             messages.append("errorcode="+e.getErrorCode()+"<br>");
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
             messages.append("<b>Error:</b> Could not access Database driver <br><em>"+e.getMessage()+"</em>.<br>");
           }

       // create database (if not exists)
           if (messages.length()==0) {
             try {
               sqlSetup.SQLSetup();
             } catch (SQLException e) {
               e.printStackTrace();
               messages.append("<b>Error:</b> creating Database<br><em>"+e.getMessage()+"</em>.<br>");
             }
           }
       // create user
           if (messages.length()==0) {
             try {
               sqlSetup.UserSetup();
               sqlSetup.closeConnection();
             } catch (SQLException e) {
               e.printStackTrace();
               messages.append("<b>Error:</b> Could not create WOW Database user<br><em>"+e.getMessage()+"</em><br>");
             }
           }

       // create tables
           if (messages.length()==0) {
             try {
               sqlSetup.openRootWowConnection();
               sqlSetup.TableSetup();
             } catch (SQLException e) {
               e.printStackTrace();
               messages.append("<b>Error:</b> Could not create WOW Tables<br><em>"+e.getMessage()+"</em><br>");
             } catch (ClassNotFoundException e) {
               e.printStackTrace();
               messages.append("<b>Error:</b> Could not access Database driver <br><em>"+e.getMessage()+"</em>.<br>");
             }
           }

           printFinished();

           httpOut.write("<h2>Database/User Creation</h2>");
           if (messages.length()>0) {
             httpOut.write(messages.toString());
           } else {
             httpOut.write("The Database tables have been created on your Database server.<br>\n");
//Natasha
                conf.StoreConfig();
             //if ((olddbimpl!=null) && olddbimpl.equals("xml"))  // switched from XML to Database: convert??
             if ((olddbimpl!=null) && olddbimpl.equals("db"))  // switched from XML to Database: convert??
               printConvertToSQL();
             else {
               if (olddbimpl==null) System.out.println("Old DB impl is null");
               else System.out.println("Old DB impl="+olddbimpl);
             }
           }

         }

        } else {
            // database has been created
              httpOut.write("<h2>DataBase created</h2>\nThe WOW/Database server has been configured! Ready to go...");
              if ((olddbimpl!=null) && olddbimpl.equals("xml"))  // switched from XML to Database: convert??
              printConvertToSQL();
        }
    // end Database section
      } else {
        // result from registering XML implementation
        conf.Put("dbimpl","xml");
          printFinished();
          if ((olddbimpl!=null) && olddbimpl.equals("db")) {
      // switched from Database to XML: convert??
             printConvertToXML();
      } // else XML is newly set or DBIMPL used to be XML
      }

    } else {
      // show form
      String charDbimpl=conf.Get("dbimpl");
      if ((charDbimpl==null)||charDbimpl.equals("xml")) {
         checkXML=" checked"; checkDB="";
      } else {
         checkDB=" checked"; checkXML="";
      }
        httpOut.write(" <form name=\"dbform\" method=POST>\n"+
      " <table>\n"+
      " <tr>\n"+
      " <th>Select Database</th>\n"+
      " <td>\n"+
      " <input name=\"dbimpl\" type=\"radio\" value=\"xml\""+ checkXML +">XML\n"+
      " </td>\n <td><input type=\"button\" value=\"OK\" onclick=\"checkpasswd()\"></td>\n"+
      " </tr>\n <tr>\n <th></th>\n <td>\n"+
      " <input name=\"dbimpl\" type=\"radio\" value=\"db\""+ checkDB +">Database Server</td>\n"+
      " </td>\n </tr>\n </table>\n"+
      " <hr>\n <em>Next fields are saved only with Database Server enabled<em><br>\n"+
      " <div id=\"dbpart\">\n <table>\n <tr>\n <th>Database server URL</th>\n"+
      " <td><input class=\"dbon\" name=\"jdbcUrl\" value='"+conf.Get("jdbcUrl")+"'></td>\n"+
      " </tr>\n <tr>\n <th>Database server user login</th>\n"+
      " <td><input class=\"dbon\" name=\"dbuser\" value='"+conf.Get("dbuser")+"'></td>\n"+
      " </tr>\n <tr>\n <th>Database server user passwd</th>\n"+
      " <td><input class=\"dblon\" name=\"dbpassword\" type=password value='"+conf.Get("dbpassword")+
             "'></td>\n"+
      " </tr>\n <tr>\n <th>Database server user verify passwd</th>\n"+
      " <td><input class=\"dbon\" name=\"dbpassword2\" type=password value='"+conf.Get("dbpassword")+
             "'></td>\n"+
      " </tr>\n <tr>\n <th>Create User and Database</th>\n"+
      " <td><input class=\"dbon\" name=\"dbsetup\" type=\"checkbox\"></td>\n"+
      " </tr>\n <tr>\n <th>Webserver ID</th>\n"+
      " <td><input class=\"dbon\" name=\"dbsetup\" value=\"localhost\"></td>\n"+
      " </tr>\n"+
      " </table>\n </div>\n </form>\n");

   } // end (if dbimpl!=null)

     httpOut.write("<!-- end of content -->\n"+
      "</td>\n</tr>\n</table>\n"+
      "</body>\n</html>\n");

   } // if loggedin==null || loggedin==false || conf==null
  } //

} // end servlet
