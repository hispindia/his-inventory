package org.openmrs.module.inventory.web.controller.drug;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryDrugCategory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DrugCategoryValidator implements Validator {

	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return InventoryDrugCategory.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	InventoryDrugCategory category = (InventoryDrugCategory) command;
    	
    	if( StringUtils.isBlank(category.getName())){
    		error.reject("inventory.drugCategory.name.required");
    	}
    	InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
    	InventoryDrugCategory categoryE = inventoryService.getDrugCategoryByName(category.getName());
    	if(category.getId() != null){
    		if(categoryE != null && categoryE.getId().intValue() != category.getId().intValue()){
    			error.reject("inventory.drugCategory.name.existed");
    		}
    	}else{
    		if(categoryE != null){
    	    		error.reject("inventory.drugCategory.name.existed");
    		}
    	}
    	
    	
    }

}