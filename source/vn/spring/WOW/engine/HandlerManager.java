/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * HandlerManager.java 1.0, June 1, 2008
 *
 *Copyright (c) 2006 by Eindhoven University of Technology and modified by University of Science, 2008, Vietnam.
 * All Rights Reserved.
 *
 * This software is proprietary information of Eindhoven University of Technology and University
 * of Science. It may be used according to the GNU license of Eindhoven University of Technology
 */

package vn.spring.WOW.engine;

import vn.spring.WOW.exceptions.*;
import vn.spring.WOW.datacomponents.*;
import vn.spring.WOW.WOWDB.WOWDB;
import vn.spring.WOW.WOWStatic;
import java.io.*;
import java.util.LinkedList;
import java.net.*;

/**
 * The <code>HandlerManager</code> manages all handlers. When
 * initializing this class all handlers that are to be used should be
 * registered here. The manager tries to find the proper resource
 * using the (possibly extended) <code>URLStreamLocatorFactory</code>
 * given a URL and it tries to find the proper handler given a
 * resource. So internally it keeps track of all handlers. The order
 * in which these handlers appear in the list is important, because
 * handlers that are added later are chosen earlier. This is because
 * the first handler that can handle the specified resource is chosen.
 * The manager itself will first add a handler that will simply pass
 * all resources through, to make sure that all types of resource can
 * be 'handled'.
 */
public class HandlerManager {

    // A reference to the database
    private WOWDB db = null;

    // The list of all handlers
    private LinkedList list = null;

    /**
     * Creates a new manager.
     * @param db An instance to the database to be used.
     */
    public HandlerManager(WOWDB db) {
        if (db == null) throw new NullPointerException("WOWDB object 'db' is null");
        this.db = db;
        list = new LinkedList();
        list.add(new PassThroughHandler());
    }

    /**
     * Adds the specified handler to the beginning of the list.
     * @param handler The handler that is to be added.
     */
    public synchronized void addHandler(ResourceHandler handler) {
        if (handler == null) throw new NullPointerException("ResourceHandler object 'handler' is null");
        list.add(handler);
    }

    /**
     * Removes the specified handler.
     * @param handler The handler to be removed.
     */
    public synchronized void removeHandler(ResourceHandler handler) {
        if (handler == null) throw new NullPointerException("ResourceHandler object 'handler' is null");
        list.remove(handler);
    }

    /**
     * Processes the resource using the user model and database and
     * returns the new resource.
     * @param resource The resource to be processed.
     * @param profile The user profile that is to be used.
     * @return The processed resource.
     */
    public Resource processResource(Resource resource, Profile profile) throws
                    ProcessorException,
                    NoHandlerException {
        if (resource == null) throw new NullPointerException("Resource object 'resource' is null");
        Resource result = null;
        synchronized(list) {
            int i=list.size()-1;
            ResourceType type = resource.getType();
            ResourceHandler handler = null;
            while (i>=0) {
                handler = (ResourceHandler)list.get(i);
                try {handler = (ResourceHandler)handler.getClass().newInstance();}
                    catch (Exception e) {throw new ProcessorException(e.getMessage());}
                if (handler.handlesResource(type)) {
                    result = handler.processResource(resource, profile, db);
                    i = -2;
                }
                i--;
            }
            if (i == -1) throw new NoHandlerException();
        }
        return result;
    }

    public Resource processComplete(Resource resource, Profile profile) throws
                    ProcessorException,
                    NoHandlerException {
        int i=20; //don't process a resource more than 20 times
        while (!resource.isReady() && i>0) {
            resource = processResource(resource, profile);
            i--;
        }
        if (i == 0) throw new ProcessorException("Too much processing");
        return resource;
    }

    /**
     * Returns the resource that belongs to the given URL.
     * //@param resourceURL A URL to the resource that should be
     * //       located.
     * @return The located resource.
     */
    // please look at this function carefully, this is AN ACHILLES HEEL!!
    //
    public Resource locateResource(String URI) throws
                    ResourceNotFoundException {
        Resource result = null;
        String uri=URI.toLowerCase();
        WowMime theMime;
        String gotCha;
        if ( uri.startsWith("http:") || uri.startsWith("ftp:")) {
          try {
              URL resourceURL=new URL(URI);
              URLConnection conn = resourceURL.openConnection();
              gotCha=conn.getContentType();
              if (gotCha==null) {
                 theMime=new WowMime(); // why not add it to wowStatic
                 gotCha=theMime.getMime(URI);
              }
              result = new Resource(conn.getInputStream(), new ResourceType(gotCha, null));
              result.setIsFile(false);
              result.setURL(URI);
          } catch (Exception e) {
        	  throw new
              		ResourceNotFoundException("There was an error while locating the resource: "+e.getMessage());}
        } else {
         //file!
         // strip it to the core, relative to WEBROOT
         // (which is usually WOWROOT getURL should expand
         // this to "file:/"+WEBROOT+"/"+URI
          if (uri.startsWith("file:")) {
             URI=URI.substring(5);
             if (URI.toLowerCase().startsWith("/localhost")) URI=URI.substring(10); // double slashes are stripped
             if (URI.toLowerCase().startsWith("\\localhost")) URI=URI.substring(10); // double backslashes also ?!?!?
          }
          if (URI.toLowerCase().startsWith("/")) URI=URI.substring(1);
          if (URI.toLowerCase().startsWith("\\")) URI=URI.substring(1);
          // get mimetype
          theMime=new WowMime();
          gotCha=theMime.getMime(URI);
          if (gotCha==null) gotCha="text/unknown";
          try {
            result = new Resource(new FileInputStream(new File(WOWStatic.config.Get("WEBROOT")+File.separator+URI)),
                                  new ResourceType(gotCha, null));
            result.setIsFile(true);
            result.setURL(URI);
          } catch (Exception e) {
        	  throw new
              	ResourceNotFoundException("There was an error while locating the resource: "+e.getMessage());}
          // return resource
        }
        return result;
    }

}