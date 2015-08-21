/**
 * 
 */
package vn.spring.zebra.um.gui;

import java.util.EventObject;

/**
 * @author Phuoc - Loc Nguyen
 *
 */
public class UserAttributeTableModelChangeEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	public UserAttributeTableModelChangeEvent(UserAttributeTableModel model) {
		super(model);
	}
}
