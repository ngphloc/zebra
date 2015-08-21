package vn.spring.zebra.server;

import java.util.EventObject;

import vn.spring.zebra.um.TriUM;

/**
 * @author Loc Nguyen
 * Email: ng_phloc@yahoo.com
 * Phone: 84-90-8222007
 * Affiliation: University of Science, 2008, Vietnam.
 * All Rights Reserved.
 */

public class TriUMServerQueryEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	protected TriUM triUM = null;
	protected String msg = null;
	
	public TriUMServerQueryEvent(TriUMServer server, TriUM triUM, String msg) {
		super(server);
		this.triUM = triUM;
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
	public TriUM getTriUM() {
		return triUM;
	}
}
