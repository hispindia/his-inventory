package org.openmrs.module.inventory.web.controller.item;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItemSubCategory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ItemSubCategoryValidator implements Validator {

	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return InventoryItemSubCategory.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	InventoryItemSubCategory subCategory = (InventoryItemSubCategory) command;
    	
    	if( StringUtils.isBlank(subCategory.getName())){
    		error.reject("inventory.itemSubCategory.name.required");
    	}
    	if( subCategory.getCategory() == null){
    		error.reject("inventory.itemSubCategory.category.required");
    	}
    	InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
    	InventoryItemSubCategory subCategoryE = inventoryService.getItemSubCategoryByName(subCategory.getCategory().getId() , subCategory.getName());
    	if(subCategory.getId() != null){
    		if(subCategoryE != null && subCategoryE.getId().intValue() != subCategory.getId().intValue()){
    			error.reject("inventory.itemSubCategory.name.existed");
    		}
    	}else{
    		if(subCategoryE != null){
    	    		error.reject("inventory.itemSubCategory.name.existed");
    		}
    	}
    	
    	
    }

}