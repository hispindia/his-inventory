package org.openmrs.module.inventory.web.controller.item;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItemCategory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ItemCategoryValidator implements Validator {

	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return InventoryItemCategory.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	InventoryItemCategory category = (InventoryItemCategory) command;
    	
    	if( StringUtils.isBlank(category.getName())){
    		error.reject("inventory.itemCategory.name.required");
    	}
    	InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
    	InventoryItemCategory categoryE = inventoryService.getItemCategoryByName(category.getName());
    	if(category.getId() != null){
    		if(categoryE != null && categoryE.getId().intValue() != category.getId().intValue()){
    			error.reject("inventory.itemCategory.name.existed");
    		}
    	}else{
    		if(categoryE != null){
    	    		error.reject("inventory.itemCategory.name.existed");
    		}
    	}
    	
    	
    }

}