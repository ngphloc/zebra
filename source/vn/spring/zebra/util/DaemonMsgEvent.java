package vn.spring.zebra.util;

import java.util.EventObject;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */
public class DaemonMsgEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	protected String msg = null;
	
	public DaemonMsgEvent(Object daemon, String msg) {
		super(daemon);
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
}
