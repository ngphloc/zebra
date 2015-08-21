/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ViewGet.java 1.0, August 30, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008. All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */
import java.util.*;
import java.io.*;

import vn.spring.WOW.Layout.*;
import vn.spring.WOW.WOWStatic;
import vn.spring.WOW.Utils.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.engine.*;
import vn.spring.WOW.exceptions.*;
import vn.spring.zebra.feedback.UserFeedback;

import es.uco.WOW.LoadTestEngine.DoAppletTest;

import javax.servlet.http.*;
import javax.servlet.*;

/**
 * This servlet generates response for all view requests
 * Modified by Loc Nguyen 2008, 2009
 */

public class ViewGet  extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ViewGet() {}
    public void doViewGet(String contextpath, String pathinfo, String activeconcept, Profile profile, Map params, HttpServletResponse response) {
    	InputStream is = null;
        String mime = "text/html";
        Resource resource = null;
        try {
            String login = profile.getAttributeValue("personal","id");
            String course = profile.getAttributeValue("personal","course");
            String directory = profile.getAttributeValue("personal","directory");
            String URI = pathinfo.substring(1);

            response.setDateHeader("Expires", -1);
            response.setHeader("Cache-Control", "no-store, no-cache, private, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0; max-stale=0; pre-check=0");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Content-Location", URI.toString()+"test.html");

            String handler = (String)params.get("handler");
            if (handler == null) handler = "";

            if (handler.equals("Done")) {
                Done done = new Done();
                resource = done.getList(profile, course);
                if (resource == null) throw new WOWException("Error in \"Done\" servlet.");
            } else if (handler.equals("Todo")) {
                Todo todo = new Todo();
                resource = todo.getList(profile, course);
                if (resource == null) throw new WOWException("Error in \"Todo\" servlet.");
            } else if (handler.equals("PasswordConfig")) {
                PasswordConfig passwordConfig = new PasswordConfig();
                resource = passwordConfig.getOutput(profile, login, params);
                if (resource == null) throw new WOWException("Error in \"PasswordConfig\" servlet.");
            } else if (handler.equals("ColorConfig")) {
                ColorConfig colorConfig = new ColorConfig();
                resource = colorConfig.genConfig(profile, login);
                if (resource == null) throw new WOWException("Error in \"ColorConfig\" servlet.");
            } else if (handler.equals("ColorChange")) {
                ColorChange colorChange = new ColorChange();
                resource = colorChange.getOutput(profile, login, params);
                if (resource == null) throw new WOWException("Error in \"ColorChange\" servlet.");
            } else if ((handler.equals("FormProcess")) | (pathinfo.endsWith("/FormProcess"))) {
                ChangeProfile changeProfile = new ChangeProfile();
                resource = changeProfile.getOutput(profile, login, course, params);
                if (resource == null) throw new WOWException("Error processing a form.");
            } else if (handler.equals("KnowledgeConfig")) {
                KnowledgeConfig knowledgeConfig = new KnowledgeConfig();
                resource = knowledgeConfig.genKnowledgeList(profile, login, course);
                if (resource == null) throw new WOWException("Error in \"KnowledgeConfig\" servlet.");
            } else if (handler.equals("KnowledgeChange")) {
                KnowledgeChange knowledgeChange = new KnowledgeChange();
                resource = knowledgeChange.getOutput(profile, login, course, params);
                if (resource == null) throw new WOWException("Error in \"KnowledgeChange\" servlet.");
            } else if (URI.endsWith("|DoTest")) {
                DoTest doTest = new DoTest();
                resource = doTest.getOutput(profile, login, pathinfo);
                if (resource == null) throw new WOWException("Error in \"DoTest\" servlet.");
            } else if (URI.endsWith("|EvalTest")) {
                URI = URI.substring(0, URI.indexOf("|EvalTest"));
                StringBuffer sb = new StringBuffer();
                CgiUtil Util = new CgiUtil(params);
                FilterTest filtertest = new FilterTest(login, profile, "/"+URI, sb);
                String Score = filtertest.EvalQuestions(Util);
                
                if(Score == null) {}
                InputStream insb = new ByteArrayInputStream(sb.toString().getBytes());
                resource = new Resource(insb, new ResourceType("text/html"));
                if (resource == null) throw new WOWException("Error evaluating the test.");
            } else if (URI.endsWith("|DoAppletTest")) {
                DoAppletTest doAppletTest = new DoAppletTest();
                resource = doAppletTest.getOutput(profile, login, contextpath, pathinfo);
                if (resource == null) throw new WOWException("Error in \"DoAppletTest\" servlet.");
            } else if (URI.endsWith(".frm")) {
                resource = WOWStatic.HM().locateResource(URI);
                resource.setCallerID("ViewGet");
                resource.setBaseID(contextpath+"/");
                WOWStatic.PM().accessedResource(profile, URI);
                resource = WOWStatic.HM().processComplete(resource, profile);
            //added by Loc Nguyen 2009.12 
            } else if (handler.equals("UserFeedback")) {
            	UserFeedback userFeedback = new UserFeedback(login, course);
                resource = userFeedback.service(params);
                if (resource == null) throw new WOWException("Error in \"UserFeedback\" servlet.");
            } else {
                String viewName = null;
                viewName = (String)params.get("view");
                String servlParams = pathinfo.substring(1);
                if (viewName == null) {
                    if ((servlParams.indexOf("/")>0) && servlParams.startsWith(course)) {
                        servlParams = directory + servlParams.substring(servlParams.indexOf("/"));
                    }
                    resource = WOWStatic.HM().locateResource(servlParams);
                } else {
                    View view = WOWStatic.VM().getView(viewName);
                    if (view == null) {
                        System.out.println("ViewManager.service: View not found: "+viewName);
                        try {
                            response.sendRedirect(servlParams);
                        } catch(Exception e) {
                            System.out.println("Can not redirect to "+servlParams);
                        }
                    } else {
                    	String viewType = view.getViewType().toUpperCase(); 
                  	  
                        if (viewType.equals("MAINVIEW"))
                            response.addCookie(new Cookie("lastWOWpage", contextpath+"/ViewGet"+pathinfo));
                        
                        Hashtable viewparams = new Hashtable();
                        viewparams.putAll(params);
                        //added by Loc Nguyen 2008 
                        viewparams.put("wow_path", contextpath); //E.g: http://localhost:8080/wow
                        viewparams.put("wow_course", course);
                        String conceptname = (String)params.get("concept");
                        if (conceptname == null) {
                        	conceptname = activeconcept;
                        }
                        is = view.genBrsCode(conceptname, profile, viewparams);
                        mime = view.getMime();
                    }
                }
            }

            if (resource != null) {
                if (resource.getURL() == null) resource.setURL(contextpath+"/ViewGet"+pathinfo);
                try {resource = WOWStatic.HM().processComplete(resource, profile);} catch (Exception e) {e.printStackTrace();}
                String cmime = resource.getType().getMime();
                if ("text/css".equals(cmime) || "application/java-archive".equals(cmime)) mime = cmime;
            }
        } catch (Exception e) {e.printStackTrace();WOWStatic.showError(response, "ViewGet: error output: " + e.getClass().getName() + ": " + e.getMessage(), true);return;}

        if (resource != null) is = resource.getInputStream();
        sendDataToClient(is, mime, response);
    }

    /**
     * Generates Response for the Browser Request
     */
	public void service(HttpServletRequest request, HttpServletResponse response) {
        String contextpath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
        String pathinfo = request.getPathInfo();
        
        try {
            String login = (String)request.getSession().getAttribute("login");
            if (login == null) throw new WOWException("<br/>No login defined in session info, <a href=\""+contextpath+"\" target=\"_top\">please login again</a>.");
            Profile profile = ((Profile)this.getServletContext().getAttribute(login));
            if (profile == null) throw new WOWException("<br/>User profile in session info does not exist,<br><a href=\""+contextpath+"\" target=\"_top\">please login again</a>.");
            doViewGet(contextpath, pathinfo, (String)request.getSession().getAttribute("activeconcept"), profile, transformParamTable(request.getParameterMap()), response);
        } catch (Exception e) {e.printStackTrace(); WOWStatic.showError(response, "ViewGet: error output: " + e.getClass().getName() + ": " + e.getMessage(), true);}
    }

    public static Hashtable transformParamTable(Map map) {
        Hashtable result = new Hashtable();
        for (Iterator e=map.keySet().iterator();e.hasNext();) {
            Object key = e.next();
            String[] value = (String[])map.get(key);
            StringBuffer resvalue = new StringBuffer();
            for (int i=0;i<value.length;i++) resvalue.append(value[i]);
            result.put(key, resvalue.toString());
        }
        return result;
    }

 /**
 * First version sendDataToClient: This method is used by MAIN View to send data to the client
 */
 private void sendDataToClient(InputStream is, String mime, HttpServletResponse response){

   byte[] buffer = null;
   buffer = new byte[8192];

   //Write Mime to response
   if (mime==null) mime="text/html";
   response.setContentType(mime + ";charset=utf-8");

   int numRead = 0;
   int data;
   try {

     if (mime.startsWith("image")) {
       ServletOutputStream imageOut = response.getOutputStream();
       while ((data = is.read()) != -1) imageOut.write(data);
       imageOut.close();
     }
     else {
       OutputStream os = response.getOutputStream();
       do {
         numRead = is.read(buffer);
         if (numRead > 0) os.write(buffer, 0, numRead);
       } while ((numRead > 0) && (is.available() > 0));
       os.close();

     }
     is.close();
     } catch (IOException e) {System.out.println("SendDataToClient: error output: " + e.getClass().getName() + ": " + e.getMessage());}

 }


 /**
 *Second Version:This method is used by all other views to send data to the client
 */
  void sendDataToClient(InputStream is, HttpServletResponse response){

    // Send the output to the client
    byte[] buffer = null;
    buffer = new byte[8192];

    int numRead = 0;
    try {
      OutputStream os = response.getOutputStream();
      do {
        numRead = is.read(buffer);
        if (numRead != 0) os.write(buffer, 0, numRead);
      } while ((numRead != 0) && (is.available() > 0));
      os.close();

      is.close();
      } catch (IOException e) {e.printStackTrace();System.out.println("Get: error output: " + e.getClass().getName() + ": " + e.getMessage());}

  }

 /**
  * Second version sendDataToClient: This method is used by all servlets to send data to the client
  */
  void sendDataToClient(String s, HttpServletResponse response){

    // Send the output to the client
    byte[] buffer = null;

    try {
      OutputStream os = response.getOutputStream();
      buffer=s.getBytes();
      if (buffer.length != 0) os.write(buffer, 0, buffer.length);
      os.close();
      } catch (IOException e) {e.printStackTrace();System.out.println("Get: error output: " + e.getClass().getName() + ": " + e.getMessage());}

  }

}
