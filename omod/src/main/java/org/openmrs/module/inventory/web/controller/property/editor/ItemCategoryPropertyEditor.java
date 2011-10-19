package org.openmrs.module.inventory.web.controller.property.editor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItemCategory;

public class ItemCategoryPropertyEditor extends PropertyEditorSupport{
	private Log log = LogFactory.getLog(this.getClass());
	public ItemCategoryPropertyEditor() {
	}
	public void setAsText(String text) throws IllegalArgumentException {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
		if (text != null && text.trim().length() > 0 ) {
			try {
				setValue(inventoryService.getItemCategoryById(NumberUtils.toInt(text)));
			}
			catch (Exception ex) {
				log.error("Error setting text: " + text, ex);
				throw new IllegalArgumentException("ItemCategory not found: " + ex.getMessage());
			}
		} else {
			setValue(null);
		}
	}
	
	public String getAsText() {
		InventoryItemCategory s = (InventoryItemCategory) getValue();
		if (s == null ) {
			return null; 
		} else {
			return s.getId()+"";
		}
	}
}
