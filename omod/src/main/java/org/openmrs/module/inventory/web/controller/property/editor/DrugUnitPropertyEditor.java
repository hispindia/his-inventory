package org.openmrs.module.inventory.web.controller.property.editor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryDrugUnit;
import org.openmrs.module.inventory.InventoryService;

public class DrugUnitPropertyEditor extends PropertyEditorSupport{
	private Log log = LogFactory.getLog(this.getClass());
	public DrugUnitPropertyEditor() {
	}
	public void setAsText(String text) throws IllegalArgumentException {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		if (text != null && text.trim().length() > 0 ) {
			try {
				setValue(inventoryService.getDrugUnitById(NumberUtils.toInt(text)));
			}
			catch (Exception ex) {
				log.error("Error setting text: " + text, ex);
				throw new IllegalArgumentException("DrugUnit not found: " + ex.getMessage());
			}
		} else {
			setValue(null);
		}
	}
	
	public String getAsText() {
		InventoryDrugUnit s = (InventoryDrugUnit) getValue();
		if (s == null ) {
			return null; 
		} else {
			return s.getId()+"";
		}
	}
}
