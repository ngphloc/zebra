/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * PassThroughHandler.java 1.0, June 1, 2008
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

/**
 * This is the standard pass-through handler, that will do nothing
 * with the data, but will indicate that the resource is ready for
 * the client.
 * @see ResourceHandler
 */
public class PassThroughHandler implements ResourceHandler {

    /**
     * Creates a new pass-through handler.
     */
    public PassThroughHandler() {}

    /**
     * Returns if this handler can process the given resource. This
     * function always returns true in the pass-through handler,
     * because all types can be passed through the server.
     * @param type The resource type that this handler is supposed to
     *        handle.
     * @return true.
     */
    public boolean handlesResource(ResourceType type) {
        return true;
    }

    /**
     * Processes the resource using the user model and database and
     * returns the new resource.
     * @param resource The resource to be passed.
     * @param profile A dummy user profile.
     * @param db A dummy reference.
     * @return The resource that can be sent to the client.
     */
    public Resource processResource(Resource resource, Profile profile, WOWDB db) throws
                    ProcessorException {
        if (resource == null) throw new NullPointerException("Resource object 'resource' is null");
        resource.setReady(true);
        return resource;
    }

}
