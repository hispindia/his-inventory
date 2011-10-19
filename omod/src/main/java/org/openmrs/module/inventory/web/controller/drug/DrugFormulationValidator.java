package org.openmrs.module.inventory.web.controller.drug;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryDrugFormulation;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class DrugFormulationValidator implements Validator {

	/**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
    	return InventoryDrugFormulation.class.equals(clazz);
    }

	/**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object command, Errors error) {
    	InventoryDrugFormulation formulation = (InventoryDrugFormulation) command;
    	
    	if( StringUtils.isBlank(formulation.getName())){
    		error.reject("inventory.drugFormulation.name.required");
    	}
    	if( StringUtils.isBlank(formulation.getDozage())){
    		error.reject("inventory.drugFormulation.dozage.required");
    	}
    	InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
    	InventoryDrugFormulation formulationE = inventoryService.getDrugFormulation(formulation.getName() ,formulation.getDozage());
    	if(formulation.getId() != null){
    		if(formulationE != null && formulationE.getId().intValue() != formulation.getId().intValue()){
    			error.reject("inventory.drugFormulation.dozage.existed");
    		}
    	}else{
    		if(formulationE != null){
    	    		error.reject("inventory.drugFormulation.dozage.existed");
    		}
    	}
    	
    	
    }

}