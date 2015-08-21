package vn.spring.zebra.mining;

import java.util.EventObject;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public class CourseAccessSequences2ChangeEvent extends EventObject {
    private static final long serialVersionUID = 5516075349620653480L;

    public CourseAccessSequences2ChangeEvent(CourseAccessSequences2 sequences) {
    	super(sequences);
    }

}
