/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ResourceHandler.java 1.0, June 1, 2008
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
 * The <code>ResourceHandler</code> interface defines the
 * functionality of a class that can process a resource and create
 * another resource, that is better suited to the client. If the
 * <code>ResourceHandler</code> determines that the resource is ready
 * to be sent to the client and needs no further processing, it can
 * use the <code>Resource</code>'s <code>setReady</code> method. A
 * <code>ResourceHandler</code> should be able to indicate if it can
 * handle a specific resource (by using its resource type).
 */
public interface ResourceHandler {

    /**
     * Returns if this handler can process the given resource.
     * @param type The resource type that this handler is supposed to
     *        handle.
     * @return If this handler handles the specified resource.
     */
    public boolean handlesResource(ResourceType type);

    /**
     * Processes the resource using the user model and database and
     * returns the new resource.
     * @param resource The resource to be processed.
     * @param profile The user profile that is to be used.
     * @param db A reference to the database that is to be used.
     * @return The processed resource.
     */
    public Resource processResource(Resource resource, Profile profile, WOWDB db) throws
                    ProcessorException;

}
