package org.openmrs.module.inventory.web.controller.item;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryItemSpecification;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ItemSpecificationValidator implements Validator {

	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return InventoryItemSpecification.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	InventoryItemSpecification specification = (InventoryItemSpecification) command;
    	
    	if( StringUtils.isBlank(specification.getName())){
    		error.reject("inventory.itemSpecification.name.required");
    	}
    	InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
    	InventoryItemSpecification specificationE = inventoryService.getItemSpecificationByName(specification.getName());
    	if(specification.getId() != null){
    		if(specificationE != null && specificationE.getId().intValue() != specification.getId().intValue()){
    			error.reject("inventory.itemSpecification.name.existed");
    		}
    	}else{
    		if(specificationE != null){
    	    		error.reject("inventory.itemSpecification.name.existed");
    		}
    	}
    	
    	
    }

}