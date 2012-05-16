package org.openmrs.module.inventory.web.controller.property.editor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Role;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;

public class RolePropertyEditor  extends PropertyEditorSupport {
	private Log log = LogFactory.getLog(this.getClass());
	public RolePropertyEditor() {
	}
	public void setAsText(String text) throws IllegalArgumentException {
		UserService user = (UserService) Context.getService(UserService.class);
		if (text != null && text.trim().length() > 0 ) {
			try {
				setValue(user.getRole(text));
			}
			catch (Exception ex) {
				log.error("Error setting Role by text: " + text, ex);
				throw new IllegalArgumentException("Role not found: " + ex.getMessage());
			}
		} else {
			setValue(null);
		}
	}
	
	public String getAsText() {
		Role s = (Role) getValue();
		if (s == null ) {
			return null; 
		} else {
			return s.getRole();
		}
	}
}
