/*
    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) 
    developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 
*/
/**
 * Get.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

import java.io.*;
import java.net.URL;
import java.util.*;

import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.engine.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.exceptions.*;
import javax.servlet.http.*;
import vn.spring.WOW.Layout.*;

/**
 * This servlet generates response secondary windows
 */

public class Get  extends HttpServlet {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;




/**
   * Generates Response for the Browser Request
   */
public void service(HttpServletRequest request, HttpServletResponse response) {
    String s=null;
    String ctype=null;
    String wndName="";
    String conceptName="";
    String params=null;
    Window wnd=null;
    Layout layout=null;
System.out.println("Get: "+request.getRequestURL());
    if (request.getSession().getAttribute("login") == null) {
         WOWStatic.showError(response, "Session has expired, (either you were inactive for a long time or the server was reset).<br><a href=\""+request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"\" target=\"_top\">please login again<a>.", false);
         return;
    }

    try {
        //Set active concepts/resources
        String login = (String)request.getSession().getAttribute("login");
        Profile profile = ((Profile)this.getServletContext().getAttribute(login));
        String course = profile.getAttributeValue("personal", "course");
        if (request.getQueryString() != null) {
            params=request.getQueryString().toString();
            conceptName=WOWStatic.VM().getParam("concept",params);
            String danchor = request.getParameter("danchor");
            wndName=WOWStatic.VM().getParam("wndName",params);
            if(!conceptName.equals("")) { //request for new active concept
                //update activeconcept
                request.getSession().setAttribute("activeconcept",conceptName);
                //get concepts type
                long cnr=WOWStatic.DB().getConceptDB().findConcept(conceptName);
                Concept concept = WOWStatic.DB().getConceptDB().getConcept(cnr);
                ctype=concept.getType();
                //get the triggerwindow of the layout associated with this concepttype
                ConceptType ct = WOWStatic.CourseInfoTbl.getCourseInfo(request.getSession().getAttribute("course").toString()).CT.getConceptType(ctype);
                if (ct == null) throw new WOWException("Unknown concept type found: "+ctype+". Declare this type in ConceptTypeConfig.xml in the course directory.");
                layout=ct.getRepresentation();
                wnd=layout.getTriggerWnd();

                //added by @David @03-09-2008
                //Perform the user model update here
                if (profile==null) throw new WOWException("<br/>User profile in session info does not exist,<br/><a href=\""+request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"\" target=\"_top\">please login again</a>.");
                WOWStatic.PM().accessedConcept(profile, conceptName);
                //do logging
                if (WOWStatic.useLogging(profile)) {
                    vn.spring.WOW.parser.UMVariableLocator umvl = new vn.spring.WOW.parser.UMVariableLocator(profile, WOWStatic.DB().getConceptDB());
                    URL URI = concept.getActiveURL(umvl);
                    if (URI != null) WOWStatic.DB().getLogDB().addAccessLog(URI.toString(), profile.getAttributeValue("personal", "id"), request.getSession().getId(), false);
                }
                //end added by @David
            } else { //request for opening a window
                //get brscode of this window
                long id=WOWStatic.DB().getConceptDB().findConcept(request.getSession().getAttribute("activeconcept").toString());
                Concept concept=WOWStatic.DB().getConceptDB().getConcept(id);
                ctype=concept.getType();
                ConceptType ct = WOWStatic.CourseInfoTbl.getCourseInfo(request.getSession().getAttribute("course").toString()).CT.getConceptType(ctype);
                if (ct == null) throw new WOWException("Unknown concept type found: "+ctype+"\nDeclare this type in ConceptTypeConfig.xml in the course directory");
                layout=ct.getRepresentation();
                wnd=layout.getWnd(wndName);
            }

            //get the HTML code of the window
            if(layout.getTriggerWnd()==wnd){
                //if it's triggerwindow then also generate JS update code for other windows of the layout
                s = wnd.getBrsCode().toString();
                s = s.replaceAll("<html><head>", "<html><head>"+layout.genJSTriggerCode());
            } else {
                if(wnd==null) System.out.println("wnd==null");
                    else {
                        if(wnd.getBrsCode()==null) System.out.println("wnd.getBrsCode()==null");
                    }
                s=wnd.getBrsCode().toString();
            }

            //Add Get querystring params to of view URLs
            params += (danchor != null?"#"+danchor:"");
            boolean nolayout = wnd.getWndOpts().indexOf("layout=0") >= 0;
            String nolayoutViewName = "";
            int newIdxBeg=s.indexOf("<#");
            int endIdx;
            while(newIdxBeg!=-1){
                endIdx=s.indexOf("#>",newIdxBeg);
                //get viewname
                String viewname=s.substring(newIdxBeg+2,endIdx);
                if (nolayout) nolayoutViewName = viewname;

                s=s.replaceAll("<#"+viewname+"#>",viewname+"&"+params);
                s=s.replaceAll("<\\*\\*>","/"+course+"/");

                newIdxBeg=s.indexOf("<#",newIdxBeg+1);
            }
            //Set NO-Cache options(to so it only in generated HTML page doesn't work)
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, private, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, max-stale=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            response.setContentType("text/html;charset=UTF-8");

            //Generate response code and send it to the client
            if (nolayout) {
                Hashtable paramtable = ViewGet.transformParamTable(request.getParameterMap());
                paramtable.put("view", nolayoutViewName);
                String contextpath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
                String pathinfo = request.getPathInfo();
                ViewGet viewget = new ViewGet();
                viewget.doViewGet(contextpath, pathinfo, (String)request.getSession().getAttribute("activeconcept"), profile, paramtable, response);
            } else sendDataToClient(s,response);
        } else {
            //serve the requested file
            String contextpath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
            String pathinfo = request.getPathInfo();
            ViewGet viewget = new ViewGet();
            viewget.doViewGet(contextpath, pathinfo, (String)request.getSession().getAttribute("activeconcept"), profile, new Hashtable(), response);
        }
    } catch (Exception e) {
        e.printStackTrace(System.out);
        try {
            PrintWriter p = response.getWriter();
            p.println("<html><body><h1>Error retrieving concept: "+conceptName+"</h1>\n");
            p.println(e.getMessage()+"</body></html>");
            p.close();
        } catch (Exception ie) {}
    }
}




 /**
  * Second version sendDataToClient: This method is used by all servlets to send data to the client
  */
  private void sendDataToClient(String s, HttpServletResponse response){

    // Send the output to the client
    byte[] buffer = null;

    try {
      OutputStream os = response.getOutputStream();
      buffer=s.getBytes();
      if (buffer.length != 0) os.write(buffer, 0, buffer.length);
      os.close();
      } catch (IOException e) {System.out.println("Get: error output: " + e.getClass().getName() + ": " + e.getMessage());}

  }


}