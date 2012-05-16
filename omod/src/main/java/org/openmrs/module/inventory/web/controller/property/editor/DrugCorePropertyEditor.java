package org.openmrs.module.inventory.web.controller.property.editor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Drug;
import org.openmrs.api.context.Context;

public class DrugCorePropertyEditor extends PropertyEditorSupport {
	private Log log = LogFactory.getLog(this.getClass());
	public DrugCorePropertyEditor() {
	}
	public void setAsText(String text) throws IllegalArgumentException {
		
		if (text != null && text.trim().length() > 0 ) {
			try {
				setValue(Context.getConceptService().getDrug(text));
			}
			catch (Exception ex) {
				log.error("Error setting text: " + text, ex);
				throw new IllegalArgumentException("Drug not found: " + ex.getMessage());
			}
		} else {
			setValue(null);
		}
	}
	
	public String getAsText() {
		Drug s = (Drug) getValue();
		if (s == null ) {
			return null; 
		} else {
			return s.getName()+"";
		}
	}
}
