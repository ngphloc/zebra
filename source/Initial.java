/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
import javax.servlet.http.*;
import java.io.*;


/*
 * Use: convert dynamic variables within
 * web.xml.in and wowconfig.xml.in, producing
 * web.xml and wowconfig.xml (like automake)
 */


public class Initial extends HttpServlet {

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String webroot;
     public String configpath;
     public String contextpath;


     public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException {

       webroot=antiBill(this.getServletContext().getRealPath("/"));
       configpath=webroot+"WEB-INF/";
        // the antiBill in the next line is probably useless,
        // better safe then sorry
       contextpath=antiBill(request.getContextPath());

       // show something
       response.setContentType("text/html;charset=UTF-8");
       response.addHeader("Cache-Control","no-cache");
       response.getWriter().write("<html>\n</html>\n<h2>Installing Configuration file</h2>");
       try {
         this.create_webxml();
         this.create_wowconfigxml();
       } catch (FileNotFoundException e) {
         response.getWriter().println("File not found "+e.getMessage());
         response.getWriter().write("<br><b>Installation FAILED</b>");
       } catch (IOException e) {
         response.getWriter().println("IOError: "+e.getMessage());
         response.getWriter().write("<br><b>Installation FAILED</b>");
       } catch (Exception e) {
         response.getWriter().println("Unanticipated error: "+e.getMessage());
         response.getWriter().println("<br>This is NOT supposed to happen at all, possibly some \n"+
                                      "JAVA library is not functioning as expected, check if you're \n"+
                                      "realy using JDK 1.4.0 (or better)<p>\n"+
                                      "System.getProperty(\"java.version\")="+System.getProperty("java.version"));
         response.getWriter().write("<br><b>Installation FAILED</b>");

       } finally {
         response.getWriter().write("Configuration file is installed. ");
         response.getWriter().write("If you wish to move WOW!, you have to edit web.xml afterwards.");
         response.getWriter().write("<br>Please restart the server.\n</body></html>");
       }

     }

     public void create_webxml() throws FileNotFoundException,IOException  {
       StringBuffer storage=new StringBuffer();
       File webxml=new File(configpath,"web.xml.in");
       BufferedReader webIn=new BufferedReader((Reader) new FileReader(webxml));
       String search;
       while((search=webIn.readLine())!=null)
         storage.append(search.replaceAll("@WOWCONFIG@",configpath+"wowconfig.xml")+"\n");
       webIn.close();
       webxml=new File(configpath,"web.xml");
       FileOutputStream webOut=new FileOutputStream(webxml);
       webOut.write(storage.toString().getBytes());
       webOut.close();
     }

     public void create_wowconfigxml() throws FileNotFoundException,IOException {
       StringBuffer storage=new StringBuffer();
       File webxml=new File(configpath,"wowconfig.xml.in");
       BufferedReader webIn=new BufferedReader((Reader) new FileReader(webxml));
       String search;
       while((search=webIn.readLine())!=null)
         storage.append(search.replaceAll("@WOWROOT@",webroot).replaceAll("@WEBROOT@",webroot).replaceAll("@XMLROOT@",webroot+"xmlroot").replaceAll("@UIDFILE@",webroot+"xmlroot/uid").replaceAll("@CONTEXTPATH@",contextpath)+"\n");
       webIn.close();
       webxml=new File(configpath,"wowconfig.xml");
       FileOutputStream webOut=new FileOutputStream(webxml);
       webOut.write(storage.toString().getBytes());
       webOut.close();
     }

   // guess what this does ;)
   // regex has problems with escape characters like '\'
   public static String antiBill(String x) {
      return(x.replace('\\','/'));
   }

}

