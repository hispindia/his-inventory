package org.openmrs.module.inventory.web.controller.drug;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryDrugUnit;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DrugUnitValidator implements Validator {

	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return InventoryDrugUnit.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	InventoryDrugUnit unit = (InventoryDrugUnit) command;
    	
    	if( StringUtils.isBlank(unit.getName())){
    		error.reject("inventory.drugUnit.name.required");
    	}
    	InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
    	InventoryDrugUnit unitE = inventoryService.getDrugUnitByName(unit.getName());
    	if(unit.getId() != null){
    		if(unitE != null && unitE.getId().intValue() != unit.getId().intValue()){
    			error.reject("inventory.drugUnit.name.existed");
    		}
    	}else{
    		if(unitE != null){
    	    		error.reject("inventory.drugUnit.name.existed");
    		}
    	}
    	
    	
    }

}