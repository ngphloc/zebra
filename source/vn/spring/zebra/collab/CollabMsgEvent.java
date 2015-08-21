package vn.spring.zebra.collab;

import java.util.EventObject;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public class CollabMsgEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	protected String plainMsg = null;
	protected String msg = null;
	
	public CollabMsgEvent(Object collab, String plainMsg, String msg) {
		super(collab);
		this.plainMsg = plainMsg;
		this.msg = msg;
	}
	
	public String getMsg()      {return msg;}
	public String getPlainMsg() {return plainMsg;}
}
