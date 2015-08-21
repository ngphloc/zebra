/*


    This file is part of WOW! based on AHA! (Adaptive Hypermedia for All) which is free software (GNU General Public License) developed by Paul De Bra - Eindhoven University of Technology and University

    WOW! is also open source software; 


*/
/**
 * ObjectFilter.java 1.0, June 1, 2008
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

public class ObjectFilter {

	private Profile profile;

	public ObjectFilter(Profile profile) throws InvalidAttributeException {

		this.profile = profile;
	}

	public String getGoodLink () throws InvalidAttributeException {

	return profile.getAttributeValue("personal","goodlink");

	}

	public String getBadLink () throws InvalidAttributeException {

	return profile.getAttributeValue("personal","badlink");

	}

	public String getNeutralLink () throws InvalidAttributeException {

	return profile.getAttributeValue("personal","neutrallink");

	}

	public String getExternalLink () throws InvalidAttributeException {

	return profile.getAttributeValue("personal","externallink");

	}

	public String getExternalVisitedLink () throws InvalidAttributeException {

	return profile.getAttributeValue("personal","externalvisitedlink");

	}

	public String getActiveLink () throws InvalidAttributeException {

	return profile.getAttributeValue("personal","activelink");

	}

}