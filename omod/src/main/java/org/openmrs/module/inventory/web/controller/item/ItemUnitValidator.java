package org.openmrs.module.inventory.web.controller.item;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItemUnit;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ItemUnitValidator implements Validator {

	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return InventoryItemUnit.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	InventoryItemUnit unit = (InventoryItemUnit) command;
    	
    	if( StringUtils.isBlank(unit.getName())){
    		error.reject("inventory.itemUnit.name.required");
    	}
    	InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
    	InventoryItemUnit unitE = inventoryService.getItemUnitByName(unit.getName());
    	if(unit.getId() != null){
    		if(unitE != null && unitE.getId().intValue() != unit.getId().intValue()){
    			error.reject("inventory.itemUnit.name.existed");
    		}
    	}else{
    		if(unitE != null){
    	    		error.reject("inventory.itemUnit.name.existed");
    		}
    	}
    	
    	
    }

}